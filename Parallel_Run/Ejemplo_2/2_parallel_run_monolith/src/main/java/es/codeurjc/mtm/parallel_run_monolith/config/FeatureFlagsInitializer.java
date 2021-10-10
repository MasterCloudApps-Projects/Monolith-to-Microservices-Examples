package es.codeurjc.mtm.parallel_run_monolith.config;

import javax.annotation.PostConstruct;
import org.ff4j.FF4j;
import org.springframework.stereotype.Component;

@Component
public class FeatureFlagsInitializer {

  public static String FEATURE_USER_NOTIFICATION_MS = "enableUserNotificationMS";

  private FF4j ff4j;

  public FeatureFlagsInitializer(FF4j ff4j) {
    this.ff4j = ff4j;
  }

  @PostConstruct
  public void initializeFlags() {
    if (!ff4j.exist(FEATURE_USER_NOTIFICATION_MS)) {
      ff4j.createFeature(FEATURE_USER_NOTIFICATION_MS, true);
    }
  }

}