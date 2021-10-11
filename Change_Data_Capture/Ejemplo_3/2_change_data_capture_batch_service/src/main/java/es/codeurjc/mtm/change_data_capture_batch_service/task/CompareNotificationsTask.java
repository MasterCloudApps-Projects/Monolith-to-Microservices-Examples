package es.codeurjc.mtm.change_data_capture_batch_service.task;

import es.codeurjc.mtm.change_data_capture_batch_service.service.DataMigrationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CompareNotificationsTask {

  @Autowired
  private DataMigrationService dataMigrationService;

  @Scheduled(cron = "${task.migrate-loyalty.cron}")
  public void execute() {
    dataMigrationService.migrateLoyalty();
  }
}
