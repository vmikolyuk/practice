package ru.naumen.practice.telegram;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;

import ru.naumen.practice.Constants.InlineCommand;
import ru.naumen.practice.Constants.Messages;
import ru.naumen.practice.Constants.NavigationButtons;
import ru.naumen.practice.Constants.OrderStatuses;
import ru.naumen.practice.Constants.Patterns;
import ru.naumen.practice.entities.Category;
import ru.naumen.practice.entities.ClientOrder;
import ru.naumen.practice.entities.EntitiesServiceImpl;
import ru.naumen.practice.entities.Product;
import ru.naumen.practice.utils.Utils;

/**
 * Фабрика сообщений для телеграмм бота
 * @author vmikolyuk
 * @since 23.06.2021
 */
@Service
public class TeleramBotMessageFactory
{
    private final EntitiesServiceImpl entitiesService;

    public TeleramBotMessageFactory(EntitiesServiceImpl entitiesService)
    {
        this.entitiesService = entitiesService;
    }

    public SendMessage createMenuItem(Long chatId, Long parentId, String description, boolean addMain, boolean addBack)
    {
        Category parentCategory = entitiesService.getCategoryById(parentId);

        List<KeyboardButton> categories = entitiesService.getCategoriesByParentId(parentId)
                .stream()
                .map(category -> new KeyboardButton(category.getName()))
                .collect(Collectors.toList());

        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup(categories.toArray(KeyboardButton[]::new));
        markup.resizeKeyboard(true);
        markup.addRow(new KeyboardButton(NavigationButtons.CHECKOUT));
        markup.addRow(new KeyboardButton(NavigationButtons.CURRENT_ORDER));
        markup.addRow(new KeyboardButton(NavigationButtons.MY_ORDERS));

        if (addMain)
        {
            markup.addRow(new KeyboardButton(NavigationButtons.MAIN_MENU));
        }

        if (addBack && parentCategory != null)
        {
            markup.addRow(new KeyboardButton(String.format(Patterns.BACK_PATTERN, parentCategory.getName())));
        }

        return new SendMessage(chatId, description).replyMarkup(markup);
    }

    public SendMessage createAboutAs(Long chatId)
    {
        return new SendMessage(chatId, Utils.loadAboutUsInfo())
                .replyMarkup(new ReplyKeyboardMarkup(new KeyboardButton(NavigationButtons.BACK)).resizeKeyboard(true));
    }

    public SendMessage simpleMessage(Long chatId, String text)
    {
        return new SendMessage(chatId, text);
    }

    public SendMessage createProductButton(Long chatId, String menuText, List<Product> products)
    {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        products.forEach(product -> markup.addRow(
                new InlineKeyboardButton(String.format(Patterns.PRODUCT_NAME_PATTERN, product.getName(),
                        product.getPrice()))
                        .callbackData(String.format(InlineCommand.PRODUCT_COMMAND, product.getId()))));

        return new SendMessage(chatId, menuText).replyMarkup(markup);
    }

    public SendMessage createConfirmButtons(Long chatId, String message)
    {
        return new SendMessage(chatId, message).replyMarkup(
                new InlineKeyboardMarkup(
                        new InlineKeyboardButton(NavigationButtons.CONFIRM).callbackData(InlineCommand.CONFIRM_COMMAND),
                        new InlineKeyboardButton(NavigationButtons.CANCEL).callbackData(InlineCommand.CANCEL_COMMAND)));
    }

    public SendMessage createOrdersInfoButton(Long chatId, ClientOrder order)
    {
        SendMessage message = new SendMessage(chatId, String.format(Messages.ORDER_INFO, order.getId(),
                order.getTotal() == null ? 0 : order.getTotal(), Utils.getStatusOrderTitleByCode(order.getStatus())));
        if (OrderStatuses.DELIVERY.getCode() == order.getStatus())
        {
            message.replyMarkup(new InlineKeyboardMarkup(
                    new InlineKeyboardButton(NavigationButtons.CONFIRM_DELIVERY).callbackData(
                            String.format(InlineCommand.CONFIRM_DELIVERY_COMMAND, order.getId()))));
        }
        return message;
    }
}
