package es.codeurjc.mtm.parallel_run_batch_service.service;

import es.codeurjc.mtm.parallel_run_batch_service.model.Notification;
import es.codeurjc.mtm.parallel_run_batch_service.repository.micro.MicroRepository;
import es.codeurjc.mtm.parallel_run_batch_service.repository.mono.MonolithRepository;
import java.util.List;
import java.util.function.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserNotificationService {

  private MonolithRepository monolithRepository;
  private MicroRepository microRepository;

  @Autowired
  UserNotificationService(MicroRepository microRepository, MonolithRepository monolithRepository){
    this.monolithRepository = monolithRepository;
    this.microRepository = microRepository;
  }

  public Boolean compareAllNotifications() {

    List<Notification> notificationsMono = monolithRepository.getAllNotConsumedNotifications();
    List<Notification> notificationsMicro = microRepository.getAllNotConsumedNotifications();

    for (Notification n :notificationsMono){
      boolean consumeOneMicro = false;
      for(Notification notification : notificationsMicro) {
        if(!consumeOneMicro && !notification.isConsumed() && n.getMessage().equals(notification.getMessage())){
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

}
