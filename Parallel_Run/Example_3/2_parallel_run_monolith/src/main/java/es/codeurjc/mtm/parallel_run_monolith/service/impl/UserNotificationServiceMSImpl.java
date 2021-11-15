package es.codeurjc.mtm.parallel_run_monolith.service.impl;

import es.codeurjc.mtm.parallel_run_monolith.model.Payroll;
import es.codeurjc.mtm.parallel_run_monolith.service.UserNotificationService;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Qualifier("userNotificationServiceMSImpl")
public class UserNotificationServiceMSImpl implements UserNotificationService {

  @Value("${user-notification-ms.host}")
  private String USER_NOTIFICATION_MS_HOST;

  @Value("${user-notification-ms.port}")
  private int USER_NOTIFICATION_MS_PORT;

  @Async
  public void notify(String msg) {
    RestTemplate restTemplate = new RestTemplate();
    String url = "http://" + USER_NOTIFICATION_MS_HOST + ":" + USER_NOTIFICATION_MS_PORT + "/notification";
    String response = restTemplate.postForObject(url, msg, String.class);
    CompletableFuture.completedFuture(response);
  }

  @Override
  public String getNotify(Long id) {
    RestTemplate restTemplate = new RestTemplate();
    String url = "http://" + USER_NOTIFICATION_MS_HOST + ":" + USER_NOTIFICATION_MS_PORT + "/notification/"+id;
    String response = restTemplate.getForObject(url, String.class);
    CompletableFuture.completedFuture(response);
    return response;
  }


}
