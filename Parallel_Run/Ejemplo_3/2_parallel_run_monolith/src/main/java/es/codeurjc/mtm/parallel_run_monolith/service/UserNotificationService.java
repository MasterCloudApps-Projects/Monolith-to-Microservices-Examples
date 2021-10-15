package es.codeurjc.mtm.parallel_run_monolith.service;

public interface UserNotificationService {

  void notify(String message);
  String getNotify(Long id);

}
