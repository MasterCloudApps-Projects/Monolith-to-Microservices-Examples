package es.codeurjc.mtm.parallel_run_notification_ms.controller;

import es.codeurjc.mtm.parallel_run_notification_ms.model.Notification;
import es.codeurjc.mtm.parallel_run_notification_ms.service.UserNotificationService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notification")
public class UserNotificationController {

  private UserNotificationService userNotificationService;

  public UserNotificationController(
      UserNotificationService userNotificationService) {
    this.userNotificationService = userNotificationService;
  }

  @PostMapping({""})
  public ResponseEntity<String> createInvoicing(@RequestBody String msg) {
    this.userNotificationService.notify(msg);

    return ResponseEntity.ok().body(msg);
  }

  @GetMapping({""})
  public ResponseEntity<List<Notification>> getAllNotConsumedNotifications() {
    return ResponseEntity.ok().body(this.userNotificationService.getAllNotConsumedNotifications());
  }

}
