package es.codeurjc.mtm.parallel_run_batch_service.controller;

import es.codeurjc.mtm.parallel_run_batch_service.service.UserNotificationService;
import java.util.concurrent.ExecutionException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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

  @GetMapping({"/compare"})
  public ResponseEntity<Boolean> areSameNotifications()
      throws ExecutionException, InterruptedException {
    Boolean response = userNotificationService.compareAllNotifications();
    return ResponseEntity.ok(response);
  }

}
