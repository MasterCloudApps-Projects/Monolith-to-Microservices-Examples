package es.codeurjc.mtm.parallel_run_notification_ms.controller;

import es.codeurjc.mtm.parallel_run_notification_ms.service.UserNotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notification")
public class UserNotificationController {

  private UserNotificationService userNotificationService;

  public UserNotificationController(
      UserNotificationService userNotificationService) {
    this.userNotificationService = userNotificationService;
  }

  @PostMapping({""})
  public ResponseEntity<String> createNotification(@RequestBody String msg) {
    this.userNotificationService.notify(msg);

    return ResponseEntity.ok().body(msg);
  }

  @GetMapping("/{id}")
  public ResponseEntity<String> getNotification(
          @PathVariable long id) {
    String notify = this.userNotificationService.getNotify(id);

    if (notify != null) {
      return ResponseEntity.ok(notify);
    } else {
      return ResponseEntity.notFound().build();
    }
  }


}
