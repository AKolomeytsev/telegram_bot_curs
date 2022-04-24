package pro.sky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.sky.telegrambot.model.notificationTtask;

import java.time.LocalDateTime;
import java.util.Collection;

public interface RepositoryNotification_task extends JpaRepository<notificationTtask, Long> {
    Collection<notificationTtask> findAllBy();
    Collection<notificationTtask> findByChatId(long id);
    Collection<notificationTtask> getByDateSend(LocalDateTime sendDate);
}
