package es.codeurjc.mtm.parallel_run_notification_ms.repository.mono;


import es.codeurjc.mtm.parallel_run_notification_ms.model.Notification;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

public interface MonolithRepository extends JpaRepository<Notification, Long> {

  @Query(value = "SELECT n FROM Notification n WHERE n.consumed = false ")
  List<Notification> getAllNotConsumedNotifications();
}
