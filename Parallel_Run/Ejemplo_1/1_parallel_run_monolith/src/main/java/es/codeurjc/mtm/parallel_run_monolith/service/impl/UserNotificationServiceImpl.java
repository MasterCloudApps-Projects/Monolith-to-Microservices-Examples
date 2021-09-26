package es.codeurjc.mtm.parallel_run_monolith.service.impl;

import es.codeurjc.mtm.parallel_run_monolith.model.Notification;
import es.codeurjc.mtm.parallel_run_monolith.repository.NotificationRepository;
import es.codeurjc.mtm.parallel_run_monolith.service.UserNotificationService;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.function.Predicate;
import java.util.stream.Collectors;
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

  @Qualifier("userNotificationServiceMSImpl")
  @Autowired
  private UserNotificationService userNotificationService;

  @Override
  public void notify(String message) {
    log.info(message);
    //Create notification for Monolith
    Notification notification = new Notification();
    notification.setNotificationMessage(message);
    notificationRepository.save(notification);
  }

  public Boolean compareAllNotifications() throws ExecutionException, InterruptedException {

    List<Notification> notificationsMono = notificationRepository.getAllNotConsumedNotifications();
    List<Notification> notificationsMicro = userNotificationService.allNotifications();

    for (Notification n :notificationsMono){
      boolean consumeOneMicro = false;
      for(Notification notification : notificationsMicro) {
        if(!consumeOneMicro && !notification.isConsumed() && n.getNotificationMessage().equals(notification.getNotificationMessage())){
          notificationsMicro.forEach(notification1 -> {if(notification1.getId()==notification.getId()){notification1.setConsumed(true);}});
          notificationsMono.forEach(notification1 -> {if(notification1.getId()==n.getId()){notification1.setConsumed(true);}});
          consumeOneMicro = true;
        }
      };
    };
    Predicate<Notification> consumed = Notification::isConsumed;
    int consumedMono = (int) notificationsMono.stream().filter(consumed).count();
    int consumedMicro = (int) notificationsMicro.stream().filter(consumed).count();

    return notificationsMicro.size() == consumedMicro && notificationsMono.size() == consumedMono;
  }

  @Override
  public List<Notification> allNotifications() {
    return null;
  }
}
