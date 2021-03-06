package es.codeurjc.mtm.parallel_run_notification_ms.repository;

import es.codeurjc.mtm.parallel_run_notification_ms.model.Notification;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification,Long> {

  @Query(value = "SELECT n FROM Notification n WHERE n.consumed = false ")
  List<Notification> getAllNotConsumedNotifications();

}
