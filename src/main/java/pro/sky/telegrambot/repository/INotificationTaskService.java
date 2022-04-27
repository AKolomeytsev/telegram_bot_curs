package pro.sky.telegrambot.repository;

import com.pengrad.telegrambot.request.SendMessage;
import pro.sky.telegrambot.model.NotificationTtask;

import java.text.ParseException;

public interface INotificationTaskService {
    NotificationTtask parseMessage(Long chatId, String messageText) throws ParseException;

    SendMessage createObject4Send(Long chatId, String messageText);

    void send(SendMessage sendMessage);

    void save(NotificationTtask objectMessage);
}
