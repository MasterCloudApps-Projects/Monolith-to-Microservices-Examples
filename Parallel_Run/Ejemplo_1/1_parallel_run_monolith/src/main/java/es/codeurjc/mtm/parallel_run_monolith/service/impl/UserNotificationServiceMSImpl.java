package es.codeurjc.mtm.parallel_run_monolith.service.impl;

import es.codeurjc.mtm.parallel_run_monolith.model.Notification;
import es.codeurjc.mtm.parallel_run_monolith.repository.NotificationRepository;
import es.codeurjc.mtm.parallel_run_monolith.service.UserNotificationService;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Qualifier("userNotificationServiceMSImpl")
public class UserNotificationServiceMSImpl implements UserNotificationService {

  @Autowired
  private NotificationRepository notificationRepository;

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

    //Se manda la notificacion y se guarda en el MS

    //Notification notification = new Notification();
    //notification.setNotificationMessage(msg);
    //notificationRepository.save(notification);
  }

  @Override
  public Boolean compareAllNotifications() throws ExecutionException, InterruptedException {
    return false;
  }

  @Async
  public List<Notification> allNotifications() throws ExecutionException, InterruptedException {
    RestTemplate restTemplate = new RestTemplate();
    String url = "http://" + USER_NOTIFICATION_MS_HOST + ":" + USER_NOTIFICATION_MS_PORT + "/notification";
    List<Notification> response = restTemplate.getForObject(url, List.class);
    List<Notification> re = new ArrayList<>();
    re.addAll(response);

  return re;
    //Se manda la notificacion y se guarda en el MS

    //Notification notification = new Notification();
    //notification.setNotificationMessage(msg);
    //notificationRepository.save(notification);
  }
}
