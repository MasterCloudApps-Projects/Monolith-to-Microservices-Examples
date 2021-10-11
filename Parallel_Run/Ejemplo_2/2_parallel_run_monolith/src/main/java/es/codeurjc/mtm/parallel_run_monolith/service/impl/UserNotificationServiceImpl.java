package es.codeurjc.mtm.parallel_run_monolith.service.impl;

import es.codeurjc.mtm.parallel_run_monolith.model.Payroll;
import es.codeurjc.mtm.parallel_run_monolith.service.UserNotificationService;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Qualifier("userNotificationServiceImpl")
public class UserNotificationServiceImpl implements UserNotificationService {


  private final ConcurrentMap<Long, String> notifications = new ConcurrentHashMap<>();
  private final AtomicLong nextId = new AtomicLong();
  
  @Override
  public void notify(String message) {

    long id = nextId.getAndIncrement();
    this.notifications.put(id, message);
    log.info(message);
  }
  @Override
  public String getNotify(Long id) {

    return this.notifications.get(id);
  }

  

}
