package ru.naumen.practice.telegram;

import static ru.naumen.practice.Constants.ALL_NAVIGATION_BUTTONS;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

import ru.naumen.practice.Constants;
import ru.naumen.practice.Constants.Answers;
import ru.naumen.practice.Constants.CategoryNames;
import ru.naumen.practice.Constants.InlineCommand;
import ru.naumen.practice.Constants.Messages;
import ru.naumen.practice.Constants.NavigationButtons;
import ru.naumen.practice.Constants.OrderStatuses;
import ru.naumen.practice.Constants.Patterns;
import ru.naumen.practice.Constants.Separators;
import ru.naumen.practice.entities.Category;
import ru.naumen.practice.entities.Client;
import ru.naumen.practice.entities.ClientOrder;
import ru.naumen.practice.entities.EntitiesServiceImpl;
import ru.naumen.practice.entities.OrderProduct;
import ru.naumen.practice.entities.Product;
import ru.naumen.practice.utils.Utils;

/**
 * Реализация {@link TelegramBotConnection}
 * @author vmikolyuk
 * @since 24.06.2021
 */
@Service
public class TelegramBotConnectionImpl implements TelegramBotConnection
{
    private final Map<Long, String> answers = new HashMap<>();

    private class TelegramUpdatesListener implements UpdatesListener
    {
        @Override
        public int process(List<Update> updates)
        {
            updates.forEach(this::processUpdate);
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        }

        private void processCallBack(Update update)
        {
            Long chatId = update.callbackQuery().message().chat().id();
            Long clientId = update.callbackQuery().message().chat().id();
            String clientName = update.callbackQuery().message().chat().username();
            String commandText = update.callbackQuery().data();
            String command = commandText.substring(commandText.indexOf(Separators.COLON) + Separators.COLON.length());

            if (command.contains(InlineCommand.CONFIRM_DELIVERY))
            {
                Long orderId = Long.valueOf(
                        command.substring(command.indexOf(Separators.COLON) + Separators.COLON.length()));
                ClientOrder clientOrder = entitiesService.getOrderById(orderId);
                entitiesService.changeStatusOrder(clientOrder, OrderStatuses.SUCCESS.getCode());
                sendMessage(teleramBotMessageFactory.simpleMessage(chatId, String.format(Messages.ORDER_DELIVERY,
                        clientOrder.getId())));
                return;
            }

            Client client = entitiesService.ensureClient(clientId.intValue(), clientName);
            ClientOrder currentOrder = entitiesService.ensureClientOrder(client);

            if (commandText.contains(InlineCommand.ORDER))
            {
                processOrderCommand(chatId, command, currentOrder);
            }
            else if (commandText.contains(InlineCommand.PRODUCT))
            {
                processProductCommand(chatId, command, currentOrder);
            }
        }

        private void processOrderCommand(Long chatId, String command, ClientOrder order)
        {
            if (entitiesService.getOrderProducts(order).isEmpty())
            {
                sendMessage(teleramBotMessageFactory.simpleMessage(chatId, Messages.ORDER_IS_EMPTY));
                return;
            }

            switch (command)
            {
            case InlineCommand.CONFIRM:
                Client client = order.getClient();
                if (client.getAddress() == null)
                {
                    answers.put(chatId, Answers.ADDRESS_ANSWER);
                    sendMessage(teleramBotMessageFactory.simpleMessage(chatId, Messages.WRITE_ADDRESS));
                }
                else
                {
                    entitiesService.changeStatusOrder(order, OrderStatuses.DELIVERY.getCode());
                    sendMessage(teleramBotMessageFactory.simpleMessage(chatId,
                            String.format(Messages.ORDER_SUCCESS, order.getId(), client.getAddress(),
                                    Utils.getRandomInt(10, 60))));
                }

                break;
            case InlineCommand.CANCEL:
                entitiesService.changeStatusOrder(order, OrderStatuses.CANCEL.getCode());
                sendMessage(teleramBotMessageFactory.simpleMessage(chatId,
                        String.format(Messages.ORDER_CANCEL, order.getId())));
                break;
            }
        }

        private void processProductCommand(Long chatId, String productId, ClientOrder order)
        {
            Product productToAdd = entitiesService.getProductById(Long.valueOf(productId));
            if (productToAdd != null)
            {
                entitiesService.addProductToOrder(order, productToAdd);
                sendMessage(teleramBotMessageFactory.simpleMessage(chatId,
                        String.format(Messages.ADD_PRODUCT_TO_ORDER, productToAdd.getName())));
            }
        }

        private void processMessage(Update update)
        {
            Long chatId = update.message().chat().id();
            Integer clientId = update.message().from().id();
            String clientName = update.message().from().username();
            String messageText = update.message().text();

            Client client = entitiesService.ensureClient(clientId, clientName);
            ClientOrder currentClientOrder = entitiesService.ensureClientOrder(client);
            List<String> categories = entitiesService.getAllCategoryNames();
            List<OrderProduct> orderProducts = entitiesService.getOrderProducts(currentClientOrder);

            if (chatId != null && answers.containsKey(chatId))
            {
                processAnswer(update, answers.get(chatId));
                answers.remove(chatId);
            }
            else if (messageText.contains(NavigationButtons.BACK))
            {
                processBackMenu(chatId, messageText);
            }
            else if (CategoryNames.ABOUT_US.equals(messageText))
            {
                processAboutAs(chatId);
            }
            else if (ALL_NAVIGATION_BUTTONS.contains(messageText))
            {
                processNavigationCommand(chatId, messageText, client, orderProducts);
            }
            else if (categories.contains(messageText))
            {
                processMenuItem(chatId, messageText, orderProducts);
            }
            else
            {
                if (orderProducts.isEmpty())
                {
                    sendMessage(teleramBotMessageFactory.simpleMessage(chatId, Messages.ORDER_IS_EMPTY));
                }
                else
                {
                    sendMessage(
                            teleramBotMessageFactory.simpleMessage(chatId, Utils.getOrderDescription(orderProducts)));
                }
                sendMessage(teleramBotMessageFactory.createMenuItem(chatId, 0L, Messages.ROOT_MENU, false, false));
            }
        }

        private void processMenuItem(Long chatId, String menuItemName, List<OrderProduct> orderProducts)
        {
            Category category = entitiesService.getCategoryByName(menuItemName);
            if (entitiesService.isFinalCategory(category))
            {
                sendMessage(teleramBotMessageFactory.createProductButton(chatId, menuItemName,
                        entitiesService.getCategoryProducts(category)));
            }
            else
            {
                sendMessage(teleramBotMessageFactory.createMenuItem(chatId, category.getId(), Messages.SUB_MENU, true,
                        true));
            }
        }

        private void processAboutAs(Long chatId)
        {
            sendMessage(teleramBotMessageFactory.createAboutAs(chatId));
        }

        private void processNavigationCommand(Long chatId, String commandText, Client client,
                List<OrderProduct> orderProducts)
        {
            switch (commandText)
            {
            case NavigationButtons.MAIN_MENU:
                sendMessage(teleramBotMessageFactory.createMenuItem(chatId, 0L, Messages.ROOT_MENU, false, false));
                break;
            case NavigationButtons.CHECKOUT:
                if (orderProducts.isEmpty())
                {
                    sendMessage(teleramBotMessageFactory.simpleMessage(chatId, Messages.ORDER_IS_EMPTY));
                }
                else
                {
                    sendMessage(
                            teleramBotMessageFactory.simpleMessage(chatId, Utils.getOrderDescription(orderProducts)));
                    sendMessage(teleramBotMessageFactory.createConfirmButtons(chatId,
                            String.format(Messages.CONFIRM_ORDER, client.getFullName())));
                }
                break;
            case NavigationButtons.CURRENT_ORDER:
                if (orderProducts.isEmpty())
                {
                    sendMessage(teleramBotMessageFactory.simpleMessage(chatId, Messages.ORDER_IS_EMPTY));
                }
                else
                {
                    sendMessage(
                            teleramBotMessageFactory.simpleMessage(chatId, Utils.getOrderDescription(orderProducts)));
                }
                break;
            case NavigationButtons.MY_ORDERS:
                sendMessage(teleramBotMessageFactory.simpleMessage(chatId, Messages.MY_ORDERS_LIST));
                entitiesService.getClientOrders(client)
                        .forEach(order -> sendMessage(teleramBotMessageFactory.createOrdersInfoButton(chatId, order)));
                break;
            }
        }

        private void processBackMenu(Long chatId, String command)
        {
            String categoryName = command.substring(
                    command.indexOf(Patterns.IN_PATTERN) + Patterns.IN_PATTERN.length());
            Category category = entitiesService.getCategoryByName(categoryName);
            if (category != null)
            {
                sendMessage(teleramBotMessageFactory.createMenuItem(chatId, category.getParentId(), Messages.SUB_MENU,
                        true, true));
            }
            else
            {
                sendMessage(teleramBotMessageFactory.createMenuItem(chatId, 0L, Messages.ROOT_MENU, false, false));
            }
        }

        private void processUpdate(Update update)
        {
            if (update.callbackQuery() != null)
            {
                processCallBack(update);
            }
            else
            {
                processMessage(update);
            }
        }

        private void processAnswer(Update update, String answer)
        {
            if (Answers.ADDRESS_ANSWER.equals(answer))
            {
                Client client = entitiesService.getClientByExternalId(update.message().from().id());
                ClientOrder currentOrder = entitiesService.ensureClientOrder(client);
                if (client != null)
                {
                    entitiesService.saveClientAddress(client, update.message().text());
                    entitiesService.changeStatusOrder(currentOrder, OrderStatuses.DELIVERY.getCode());
                    sendMessage(teleramBotMessageFactory.simpleMessage(update.message().chat().id(),
                            String.format(Messages.ORDER_SUCCESS, currentOrder.getId(), client.getAddress(),
                                    Utils.getRandomInt(10, 60))));
                }
            }
        }
    }

    private final EntitiesServiceImpl entitiesService;
    private final TeleramBotMessageFactory teleramBotMessageFactory;
    private TelegramBot bot;

    public TelegramBotConnectionImpl(EntitiesServiceImpl entitiesService,
            TeleramBotMessageFactory teleramBotMessageFactory)
    {
        this.entitiesService = entitiesService;
        this.teleramBotMessageFactory = teleramBotMessageFactory;
    }

    @PostConstruct
    public void createConnection()
    {
        bot = new TelegramBot(Constants.BOT_TOKEN);
        bot.setUpdatesListener(new TelegramUpdatesListener());
    }

    @Override
    public void sendMessage(SendMessage message)
    {
        if (bot != null)
        {
            if (!bot.execute(message).isOk())
            {
                throw new RuntimeException("Can't send message");
            }
            return;
        }
        throw new RuntimeException("Telegram bot not initialized");
    }
}
