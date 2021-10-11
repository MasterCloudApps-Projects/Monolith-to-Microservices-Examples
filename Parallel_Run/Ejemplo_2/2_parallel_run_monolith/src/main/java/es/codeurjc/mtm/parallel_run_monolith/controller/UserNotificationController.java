package es.codeurjc.mtm.parallel_run_monolith.controller;

import es.codeurjc.mtm.parallel_run_batch_service.service.UserNotificationService;
import java.util.concurrent.ExecutionException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notification")
public class UserNotificationController {

  @Autowired
  private ScientistExperimentService scientistExperimentService;

  @GetMapping({"/comparation"})
  public ResponseEntity<Boolean> scientistExperiment() {
    Boolean response = scientistExperimentService.scientistExperiment();
    return ResponseEntity.ok(response);
  }

}
