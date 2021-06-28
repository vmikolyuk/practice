package ru.naumen.practice.telegram;

import com.pengrad.telegrambot.request.SendMessage;

/**
 * Подключение к АПИ телеграмма
 * @author vmikolyuk
 * @since 24.06.2021
 */
public interface TelegramBotConnection
{
    void sendMessage(SendMessage message);
}
