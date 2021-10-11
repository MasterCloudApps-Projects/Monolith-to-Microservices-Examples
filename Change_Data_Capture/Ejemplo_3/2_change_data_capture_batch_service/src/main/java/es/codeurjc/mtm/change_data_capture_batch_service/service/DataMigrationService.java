package es.codeurjc.mtm.change_data_capture_batch_service.service;

import es.codeurjc.mtm.change_data_capture_batch_service.model.Loyalty;
import es.codeurjc.mtm.change_data_capture_batch_service.repository.micro.MicroRepository;
import es.codeurjc.mtm.change_data_capture_batch_service.repository.mono.MonolithRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DataMigrationService {

  private MonolithRepository monolithRepository;
  private MicroRepository microRepository;

  @Autowired
  DataMigrationService(MicroRepository microRepository, MonolithRepository monolithRepository) {
    this.monolithRepository = monolithRepository;
    this.microRepository = microRepository;
  }

  public void migrateLoyalty() {
    List<Loyalty> loyaltyChanged = monolithRepository.getAllPostDateLoyaltyChanged(
        LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT));

    log.info("Data migration quantity: {} from {}", loyaltyChanged.size(), LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT).toString());

    microRepository.saveAll(loyaltyChanged);
  }

}
