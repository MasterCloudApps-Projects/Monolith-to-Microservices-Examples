package es.codeurjc.mtm.parallel_run_batch_service.repository.micro;


import es.codeurjc.mtm.parallel_run_batch_service.model.Notification;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MicroRepository extends JpaRepository<Notification, Long> {

  @Query(value = "SELECT n FROM Notification n WHERE n.consumed = false ")
  List<Notification> getAllNotConsumedNotifications();
}
