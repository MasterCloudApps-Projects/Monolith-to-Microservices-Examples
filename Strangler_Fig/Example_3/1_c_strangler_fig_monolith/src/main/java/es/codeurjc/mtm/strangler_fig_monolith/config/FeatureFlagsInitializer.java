package es.codeurjc.mtm.strangler_fig_monolith.config;

import javax.annotation.PostConstruct;
import org.ff4j.FF4j;
import org.springframework.stereotype.Component;

@Component
public class FeatureFlagsInitializer {

  public static String FEATURE_PAYROLL = "enablePayroll";

  private FF4j ff4j;

  public FeatureFlagsInitializer(FF4j ff4j) {
    this.ff4j = ff4j;
  }

  @PostConstruct
  public void initializeFlags() {
    if (!ff4j.exist(FEATURE_PAYROLL)) {
      ff4j.createFeature(FEATURE_PAYROLL, true);
    }
  }

}