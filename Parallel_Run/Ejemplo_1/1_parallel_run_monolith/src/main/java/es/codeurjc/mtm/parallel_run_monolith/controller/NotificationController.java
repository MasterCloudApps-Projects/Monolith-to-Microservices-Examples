package es.codeurjc.mtm.parallel_run_monolith.controller;

import es.codeurjc.mtm.parallel_run_monolith.model.Payroll;
import es.codeurjc.mtm.parallel_run_monolith.service.UserNotificationService;
import java.util.concurrent.ExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notification")
public class NotificationController {

  @Autowired
  @Qualifier("userNotificationServiceImpl")
  private UserNotificationService userNotificationService;

  @GetMapping({"/compare"})
  public ResponseEntity<Boolean> areSameNotifications()
      throws ExecutionException, InterruptedException {
    Boolean response = userNotificationService.compareAllNotifications();
    return ResponseEntity.ok(response);
  }

}
