package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.notificationTtask;
import pro.sky.telegrambot.repository.RepositoryNotification_task;
import pro.sky.telegrambot.sevices.NotificationTtaskService;

import javax.annotation.PostConstruct;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.List;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);
    @Autowired
    private RepositoryNotification_task repositoryNotification_task;

    @Autowired
    private TelegramBot telegramBot;

    @Autowired
    private NotificationTtaskService notificationTtaskService;

    public TelegramBotUpdatesListener() {

    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            logger.info("Processing update: {}", update);
            try {
                readerUpdate(update);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private void readerUpdate(Update update) throws ParseException {
        String messageText = update.message().text();
        Long chatId = update.message().chat().id();
        if(messageText.equals("/start")){
            notificationTtaskService.send(notificationTtaskService.createObject4Send(chatId,"Привет, я тебя слушаю..."));
        }else{
            notificationTtask objectMessage = (notificationTtask) notificationTtaskService.parseMessage(chatId,messageText);
            if(objectMessage!=null){
                repositoryNotification_task.save(objectMessage);
            }
        }

    }



}
