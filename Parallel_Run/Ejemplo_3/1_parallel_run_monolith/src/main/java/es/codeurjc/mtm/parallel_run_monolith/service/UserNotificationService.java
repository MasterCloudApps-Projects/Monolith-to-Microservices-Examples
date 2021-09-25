package es.codeurjc.mtm.parallel_run_monolith.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserNotificationService {

  public void notify(String message) {
    log.info(message);
  }
}
