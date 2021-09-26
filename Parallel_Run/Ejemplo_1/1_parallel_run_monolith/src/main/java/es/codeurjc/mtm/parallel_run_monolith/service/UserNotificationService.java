package es.codeurjc.mtm.parallel_run_monolith.service;

import es.codeurjc.mtm.parallel_run_monolith.model.Notification;
import java.util.List;
import java.util.concurrent.ExecutionException;
import org.springframework.stereotype.Component;

public interface UserNotificationService {

  void notify(String message);

  Boolean compareAllNotifications() throws ExecutionException, InterruptedException;

  List<Notification> allNotifications() throws ExecutionException, InterruptedException;
}
