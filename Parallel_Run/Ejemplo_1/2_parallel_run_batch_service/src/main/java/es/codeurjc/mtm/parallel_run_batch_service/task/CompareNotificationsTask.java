package es.codeurjc.mtm.parallel_run_batch_service.task;

import es.codeurjc.mtm.parallel_run_batch_service.service.UserNotificationService;
import java.util.concurrent.ExecutionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CompareNotificationsTask {

  @Autowired
  private UserNotificationService userNotificationService;

  @Scheduled(cron = "${tasks.compare-notifications.cron}")
  public void execute() throws ExecutionException, InterruptedException {
    log.info("Result: {}", userNotificationService.compareAllNotifications().toString());
  }
}
