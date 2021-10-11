package es.codeurjc.mtm.change_data_capture_batch_service.controller;

import es.codeurjc.mtm.change_data_capture_batch_service.service.DataMigrationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/loyalty")
public class LoyaltyController {

  private DataMigrationService dataMigrationService;

  public LoyaltyController(DataMigrationService dataMigrationService) {
    this.dataMigrationService = dataMigrationService;
  }

  @GetMapping({"/migration"})
  public ResponseEntity migrateLoyalty() {
    dataMigrationService.migrateLoyalty();
    return ResponseEntity.ok().build();
  }

}
