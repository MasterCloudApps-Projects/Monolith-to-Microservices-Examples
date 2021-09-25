package es.codeurjc.mtm.parallel_run_notification_ms.repository;

import es.codeurjc.mtm.parallel_run_notification_ms.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification,Long> {

}
