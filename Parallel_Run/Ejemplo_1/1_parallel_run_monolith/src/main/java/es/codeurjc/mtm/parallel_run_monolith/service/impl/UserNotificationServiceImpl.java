package es.codeurjc.mtm.parallel_run_monolith.service.impl;

import es.codeurjc.mtm.parallel_run_monolith.model.Notification;
import es.codeurjc.mtm.parallel_run_monolith.repository.NotificationRepository;
import es.codeurjc.mtm.parallel_run_monolith.service.UserNotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Qualifier("userNotificationServiceImpl")
public class UserNotificationServiceImpl implements UserNotificationService {

  @Autowired
  private NotificationRepository notificationRepository;


  @Override
  public void notify(String message) {
    log.info(message);
    Notification notification = new Notification();
    notification.setNotification(message);
    notificationRepository.save(notification);
  }
}
