package ru.naumen.practice.utils;

import java.io.InputStream;
import java.util.List;
import java.util.Random;

import ru.naumen.practice.Constants;
import ru.naumen.practice.Constants.Messages;
import ru.naumen.practice.Constants.OrderStatuses;
import ru.naumen.practice.Constants.Separators;
import ru.naumen.practice.entities.OrderProduct;
import ru.naumen.practice.entities.Product;

/**
 * Утилитарные методы
 * @author vmikolyuk
 * @since 25.06.2021
 */
public class Utils
{
    public static String loadAboutUsInfo()
    {
        try (InputStream is = Utils.class.getClassLoader().getResourceAsStream(Constants.ABOUT_US_FILE_PATH))
        {
            return is != null ? new String(is.readAllBytes()) : Separators.EMPTY;
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    public static String getOrderDescription(List<OrderProduct> orderProducts)
    {
        StringBuilder builder = new StringBuilder();
        builder.append(Messages.CURRENT_ORDER).append(Separators.LINE).append(Separators.LINE);

        double total = orderProducts.stream()
                .map(orderProduct ->
                {
                    Product product = orderProduct.getProduct();
                    double productTotal = product.getPrice() * orderProduct.getCountProduct();
                    builder.append(product.getName())
                            .append(Separators.SPACE)
                            .append(orderProduct.getCountProduct())
                            .append(Separators.SPLITTER_X)
                            .append(String.format("%.2f", product.getPrice()))
                            .append(Separators.EQ)
                            .append(String.format("%.2f", productTotal))
                            .append(Separators.LINE)
                            .append(Separators.LINE);
                    return productTotal;
                })
                .mapToDouble(Double::doubleValue).sum();

        builder.append(String.format(Messages.TOTAL, total));

        return builder.toString();
    }

    public static int getRandomInt(int min, int max)
    {
        return new Random().nextInt(max - min) + max;
    }

    public static String getStatusOrderTitleByCode(int code)
    {
        for (OrderStatuses value : OrderStatuses.values())
        {
            if (code == value.getCode())
            {
                return value.getTitle();
            }
        }
        return Separators.EMPTY;
    }
}
