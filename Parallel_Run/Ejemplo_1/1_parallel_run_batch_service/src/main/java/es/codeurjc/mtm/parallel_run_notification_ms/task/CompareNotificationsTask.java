package es.codeurjc.mtm.parallel_run_notification_ms.task;

import es.codeurjc.mtm.parallel_run_notification_ms.service.UserNotificationService;
import java.util.concurrent.ExecutionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Schedule task to update the works status to FINISHED based on their expiration date
 *
 * @author Siroco Team [siroco@qualityobjects.com]
 * @since 1.14.0
 */
@Slf4j
@Component
public class CompareNotificationsTask {

  @Autowired
  private UserNotificationService userNotificationService;

  @Scheduled(cron = "${tasks.compare-notifications.cron}")
  public void execute() throws ExecutionException, InterruptedException {
    log.info(userNotificationService.compareAllNotifications().toString());
  }
}
