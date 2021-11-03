package es.codeurjc.mtm.strangler_fig_monolith.controller;

import es.codeurjc.mtm.strangler_fig_monolith.service.UserNotificationService;
import org.springframework.http.ResponseEntity;
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

}
