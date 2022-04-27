package pro.sky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.telegrambot.model.NotificationTtask;

import java.time.LocalDateTime;
import java.util.Collection;

public interface RepositoryNotificationTask extends JpaRepository<NotificationTtask, Long> {
    Collection<NotificationTtask> findAllBy();

    Collection<NotificationTtask> findByChatId(long id);

    Collection<NotificationTtask> getByDateSend(LocalDateTime sendDate);
}
