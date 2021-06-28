package ru.naumen.practice;

import static ru.naumen.practice.Constants.NavigationButtons.*;
import static ru.naumen.practice.Constants.Separators.COLON;

import java.util.Arrays;
import java.util.List;

/**
 * Константы
 * @author vmikolyuk
 * @since 23.06.2021
 */
public interface Constants
{
    String BOT_TOKEN = "1798126309:AAGTdcOdJ3h1KZtAich_9sVS_NZ9loKeBiw";
    String ABOUT_US_FILE_PATH = "info.html";

    interface Separators
    {
        String COLON = ":";
        String LINE = "\n";
        String EQ = "=";
        String SPLITTER_X = "x";
        String SPACE = " ";
        String EMPTY = "";
    }

    interface Messages
    {
        String ROOT_MENU = "Основное меню";
        String SUB_MENU = "Переход в подменю";
        String CURRENT_ORDER = "Текущий заказ:";
        String TOTAL = "Итого %.2f руб.";
        String ADD_PRODUCT_TO_ORDER = "'%s' добавлен в заказ.";
        String CONFIRM_ORDER = "%s, Вам необходимо подтвердить оформление заказа";
        String ORDER_SUCCESS = "Заказ №%s подтвержден. Курьер уже едет к Вам по адресу %s. Приблизительное время "
                + "доставки %s мин.";
        String ORDER_CANCEL = "Заказ №%s отменен.";
        String ORDER_IS_EMPTY = "Заказ пуст.";
        String ORDER_INFO = "Заказ №%s на сумму %.2f руб. Статус: %s.";
        String WRITE_ADDRESS = "Введите адрес доставки:";
        String MY_ORDERS_LIST = "Список моих заказов:";
        String ORDER_DELIVERY = "Заказ №%s успешно выполнен.";
    }

    interface CategoryNames
    {
        String MENU = "Меню";
        String ABOUT_US = "О нас";
        String PIZZA = "Пицца";
        String BURGERS = "Бургеры";
        String BURGERS_CLASSIC = "Классические бургеры";
        String BURGERS_SPICY = "Острые бургеры";
        String ROLLS = "Роллы";
        String ROLLS_CLASSIC = "Классические роллы";
        String ROLLS_BAKED = "Запеченные роллы";
        String ROLLS_SWEET = "Сладкие роллы";
        String ROLLS_SETS = "Наборы";
    }

    interface NavigationButtons
    {
        String MAIN_MENU = "В основное меню";
        String CHECKOUT = "Оформить заказ";
        String CONFIRM = "Подтвердить";
        String CONFIRM_DELIVERY = "Подтвердить получение заказа";
        String CANCEL = "Отменить";
        String BACK = "Назад";
        String CANCEL_ORDER = "Отменить заказ";
        String CURRENT_ORDER = "Посмотреть текущий заказ";
        String MY_ORDERS = "Мои заказы";
    }

    interface InlineCommand
    {
        String ORDER = "order";
        String PRODUCT = "product";
        String CONFIRM = "confirm";
        String CANCEL = "cancel";
        String CONFIRM_DELIVERY = "confirmDelivery";
        String CONFIRM_COMMAND = ORDER + COLON + CONFIRM;
        String CONFIRM_DELIVERY_COMMAND = ORDER + COLON + CONFIRM_DELIVERY + COLON + "%d";
        String CANCEL_COMMAND = ORDER + COLON + CANCEL;
        String PRODUCT_COMMAND = PRODUCT + COLON + "%d";
    }

    interface Patterns
    {
        String IN_PATTERN = " в ";
        String BACK_PATTERN = NavigationButtons.BACK + IN_PATTERN + "%s";
        String PRODUCT_NAME_PATTERN = "%s. Цена: %.2f руб.";
    }

    interface Answers
    {
        String ADDRESS_ANSWER = "addressAnswer";
    }

    enum OrderStatuses
    {
        CREATED(1, "Создан"),
        CANCEL(2, "Отменен"),
        SUCCESS(3, "Выполнен"),
        DELIVERY(4, "Доставка");

        private final int code;
        private final String title;

        OrderStatuses(int code, String title)
        {
            this.code = code;
            this.title = title;
        }

        public int getCode()
        {
            return code;
        }

        public String getTitle()
        {
            return title;
        }

    }

    List<String> ALL_NAVIGATION_BUTTONS = Arrays.asList(MAIN_MENU, CHECKOUT, CONFIRM, CANCEL, BACK, CANCEL_ORDER,
            CURRENT_ORDER, MY_ORDERS);
}
