package es.codeurjc.mtm.parallel_run_batch_service.service;

import es.codeurjc.mtm.parallel_run_batch_service.model.Notification;
import es.codeurjc.mtm.parallel_run_batch_service.repository.micro.MicroRepository;
import es.codeurjc.mtm.parallel_run_batch_service.repository.mono.MonolithRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserNotificationService {

  private MonolithRepository monolithRepository;
  private MicroRepository microRepository;

  @Autowired
  UserNotificationService(MicroRepository microRepository, MonolithRepository monolithRepository) {
    this.monolithRepository = monolithRepository;
    this.microRepository = microRepository;
  }

  public Boolean compareAllNotifications() {

    List<Notification> notificationsMono = monolithRepository.getAllNotConsumedNotifications();
    List<Notification> notificationsMicro = microRepository.getAllNotConsumedNotifications();

    int consumitionsCount = 0;
    for (Notification notificationMono : notificationsMono) {
      boolean notificationConsumed = false;

      for (Notification notificationMicro : notificationsMicro) {
        if (!notificationConsumed && !notificationMicro.isConsumed() && notificationMono
            .getMessage()
            .equals(notificationMicro.getMessage())) {
          notificationConsumed = true;
          notificationMicro.setConsumed(true);
          notificationMono.setConsumed(true);
          consumitionsCount++;
        }
      }
    }

    monolithRepository.saveAll(notificationsMono);
    microRepository.saveAll(notificationsMicro);

    log.info("ConsumitionsCount: {} for mono size: {} and micro size: {}", consumitionsCount,  notificationsMono.size(), notificationsMicro.size());

    if (consumitionsCount == notificationsMicro.size() && consumitionsCount == notificationsMono
        .size()) {
      return true;
    }

    return false;

  }

}
