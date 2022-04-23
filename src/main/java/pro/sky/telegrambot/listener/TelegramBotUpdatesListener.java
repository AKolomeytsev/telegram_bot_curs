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
import pro.sky.telegrambot.model.notification_task;
import pro.sky.telegrambot.repository.RepositoryNotification_task;

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
            sendGreetings(chatId,"Привет, я тебя слушаю...");
        }else{
            notification_task objectMessage = (notification_task) parseMessage(chatId,messageText);
            if(objectMessage!=null){
                repositoryNotification_task.save(objectMessage);
            }
        }

    }

    private Object parseMessage(Long chatId, String messageText) throws ParseException {
        notification_task returnObject = new notification_task();
        String dateTimeStr = messageText.substring(0,16);
        if(dateTimeStr.matches("[0-9\\.\\:\\s]{16}")){
            returnObject.setChatId(chatId);
            returnObject.setMessage(messageText.substring(16));
            returnObject.setDateSend(dateFormat(dateTimeStr));
        }
        return returnObject;
    }

    private void sendGreetings(Long chatId, String messageText) {
        SendMessage sendMessage = new SendMessage(chatId,messageText);
        send(sendMessage);
    }

    public void send(SendMessage sendMessage) {
        telegramBot.execute(sendMessage);
    }

    public Collection<notification_task> readData(){
        return repositoryNotification_task.findAll();
    }

    @Scheduled(cron = "0 0/1 * * * *")
    public void run() {
        Collection<notification_task> sendList = readData();
        makeMessage(sendList);
    }

    private void makeMessage(Collection<notification_task> sendList) {
        LocalDateTime dateTime =LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        for(notification_task item : sendList){
            if(item.getDateSend()!=null){
                if(item.getDateSend().equals(dateTime)){
                    System.out.println("find");
                    sendGreetings(item.getChatId(),item.getMessage());
                }
            }
        }
    }

    private LocalDateTime dateFormat(String dateString){
        LocalDateTime dateTime = LocalDateTime.parse(dateString, DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
        return dateTime;
    }

}
