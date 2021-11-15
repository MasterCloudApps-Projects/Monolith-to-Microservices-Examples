package es.codeurjc.mtm.parallel_run_notification_ms.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Service
public class UserNotificationService {

  private final ConcurrentMap<Long, String> notifications = new ConcurrentHashMap<>();
  private final AtomicLong nextId = new AtomicLong();

  public void notify(String message) {
    long id = nextId.getAndIncrement();
    this.notifications.put(id, message);
    log.info(message);
  }
  public String getNotify(Long id) {
    return this.notifications.get(id);
  }
}
