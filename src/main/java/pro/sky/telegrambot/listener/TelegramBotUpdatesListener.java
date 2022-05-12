package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.NotificationTtask;
import pro.sky.telegrambot.sevices.NotificationTaskService;


import javax.annotation.PostConstruct;
import java.text.ParseException;

import java.util.List;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {
    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);
    @Autowired
    private TelegramBot telegramBot;
    @Autowired
    private NotificationTaskService notificationTaskService;
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
            notificationTaskService.send(notificationTaskService.createObject4Send(chatId,"Привет, я тебя слушаю..."));
        }else{
            NotificationTtask objectMessage = (NotificationTtask) notificationTaskService.parseMessage(chatId,messageText);
            if(objectMessage!=null){
                notificationTaskService.save(objectMessage);
            }
        }
    }
}
