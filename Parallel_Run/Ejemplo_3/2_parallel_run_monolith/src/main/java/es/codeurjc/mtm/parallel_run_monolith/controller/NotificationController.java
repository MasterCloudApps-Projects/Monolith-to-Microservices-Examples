package es.codeurjc.mtm.parallel_run_monolith.controller;

import es.codeurjc.mtm.parallel_run_monolith.service.UserNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notification")
public class NotificationController {

  private UserNotificationService userNotificationService;

  public NotificationController(@Qualifier("userNotificationServiceImpl") UserNotificationService userNotificationService){
    this.userNotificationService = userNotificationService;
  }

  @GetMapping("/{id}")
  public ResponseEntity<String> getNotify(
      @PathVariable long id) {
    String notify = this.userNotificationService.getNotify(id);

    if (notify != null) {
      return ResponseEntity.ok(notify);
    } else {
      return ResponseEntity.notFound().build();
    }
  }

}
