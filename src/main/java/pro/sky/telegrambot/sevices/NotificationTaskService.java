package pro.sky.telegrambot.sevices;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.NotificationTtask;
import pro.sky.telegrambot.repository.INotificationTaskService;
import pro.sky.telegrambot.repository.RepositoryNotificationTask;


import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Collection;

@Service
public class NotificationTaskService implements INotificationTaskService {
    private final RepositoryNotificationTask repositoryNotification_task;
    private final TelegramBot telegramBot;

    private final String reg = "[0-9\\.\\:\\s]{16}";
    private final int MIN_LEN_4_DATA = 16;
    public NotificationTaskService(TelegramBot telegramBot,RepositoryNotificationTask repositoryNotification_task) {
        this.telegramBot = telegramBot;
        this.repositoryNotification_task = repositoryNotification_task;
    }

    public NotificationTtask parseMessage(Long chatId, String messageText) throws ParseException {
        NotificationTtask returnObject = new NotificationTtask();
        if(messageText.length()>0) {
            if(messageText.length()>=MIN_LEN_4_DATA) {
                String dateTimeStr = messageText.substring(0, 16);
                if (dateTimeStr.matches(reg)) {
                    returnObject.setDateSend(dateFormat(dateTimeStr));
                }
            }
            returnObject.setChatId(chatId);
            returnObject.setMessage(messageText);
        }else {
            returnObject = null;
        }
        return returnObject;
    }

    public SendMessage createObject4Send(Long chatId, String messageText) {
        SendMessage sendMessage = new SendMessage(chatId,messageText);
        return sendMessage;
    }

    public void send(SendMessage sendMessage) {
        telegramBot.execute(sendMessage);
    }

    private Collection<NotificationTtask> readData(){
        LocalDateTime dateTime = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        return repositoryNotification_task.getByDateSend(dateTime);
    }

    @Scheduled(cron = "0 0/1 * * * *")
    private void run() {
        Collection<NotificationTtask> sendList = readData();
        if(sendList.size()>0) {
            makeMessage(sendList);
        }
    }

    private void makeMessage(Collection<NotificationTtask> sendList) {
        for(NotificationTtask item : sendList){
            send(createObject4Send(item.getChatId(),item.getMessage()));
        }
    }

    private LocalDateTime dateFormat(String dateString){
        LocalDateTime dateTime = LocalDateTime.parse(dateString, DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
        return dateTime;
    }

    public void save(NotificationTtask objectMessage) {
        repositoryNotification_task.save(objectMessage);
    }
}
