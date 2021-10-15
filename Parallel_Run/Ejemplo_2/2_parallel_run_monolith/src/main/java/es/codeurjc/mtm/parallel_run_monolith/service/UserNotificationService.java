package es.codeurjc.mtm.parallel_run_monolith.service;

import com.github.rawls238.scientist4j.Observation;

public interface UserNotificationService {

  void notify(String message);
  String getNotify(Long id);

}
