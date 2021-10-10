package es.codeurjc.mtm.parallel_run_notification_ms.service;

import es.codeurjc.mtm.parallel_run_notification_ms.model.Notification;
import es.codeurjc.mtm.parallel_run_notification_ms.repository.NotificationRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserNotificationService {

  @Autowired
  private NotificationRepository notificationRepository;

  public void notify(String message) {
    log.info(message);
    Notification notification = new Notification();
    notification.setMessage(message);
    notificationRepository.save(notification);
  }

  public List<Notification> getAllNotConsumedNotifications() {
    return notificationRepository.getAllNotConsumedNotifications();
  }
}
