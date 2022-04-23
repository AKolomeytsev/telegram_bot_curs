package pro.sky.telegrambot;

import com.pengrad.telegrambot.TelegramBot;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import pro.sky.telegrambot.configuration.TelegramBotConfiguration;
import pro.sky.telegrambot.listener.TelegramBotUpdatesListener;

@SpringBootApplication
@EnableScheduling
public class TelegramBotApplication {

	public static void main(String[] args) {
		SpringApplication.run(TelegramBotApplication.class, args);
		TelegramBot bot = new TelegramBotConfiguration().telegramBot();
		TelegramBotUpdatesListener listener = new TelegramBotUpdatesListener();
	}

}
