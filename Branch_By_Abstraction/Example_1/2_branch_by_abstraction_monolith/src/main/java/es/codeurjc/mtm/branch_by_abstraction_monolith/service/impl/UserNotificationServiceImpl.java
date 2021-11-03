package es.codeurjc.mtm.branch_by_abstraction_monolith.service.impl;

import es.codeurjc.mtm.branch_by_abstraction_monolith.service.UserNotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Qualifier("userNotificationServiceImpl")
public class UserNotificationServiceImpl implements UserNotificationService {

  @Override
  public void notify(String message) {
    log.info(message);
  }
}
