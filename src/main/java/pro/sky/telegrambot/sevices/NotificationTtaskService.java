package pro.sky.telegrambot.sevices;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.notificationTtask;
import pro.sky.telegrambot.repository.RepositoryNotification_task;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Collection;

@Service
public class NotificationTtaskService {
    @Autowired
    private RepositoryNotification_task repositoryNotification_task;

    @Autowired
    private TelegramBot telegramBot;
    public notificationTtask parseMessage(Long chatId, String messageText) throws ParseException {
        notificationTtask returnObject = new notificationTtask();
        String dateTimeStr = messageText.substring(0,16);
        if(dateTimeStr.matches("[0-9\\.\\:\\s]{16}")){
            returnObject.setChatId(chatId);
            returnObject.setMessage(messageText.substring(16));
            returnObject.setDateSend(dateFormat(dateTimeStr));
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

    public Collection<notificationTtask> readData(){
        LocalDateTime dateTime = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        Collection<notificationTtask> data = repositoryNotification_task.getByDateSend(dateTime);
        return data;
    }

    @Scheduled(cron = "0 0/1 * * * *")
    public void run() {
        Collection<notificationTtask> sendList = readData();
        if(sendList.size()>0) {
            makeMessage(sendList);
        }
    }

    private void makeMessage(Collection<notificationTtask> sendList) {

        for(notificationTtask item : sendList){
            send(createObject4Send(item.getChatId(),item.getMessage()));
        }
    }

    private LocalDateTime dateFormat(String dateString){
        LocalDateTime dateTime = LocalDateTime.parse(dateString, DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
        return dateTime;
    }
}
