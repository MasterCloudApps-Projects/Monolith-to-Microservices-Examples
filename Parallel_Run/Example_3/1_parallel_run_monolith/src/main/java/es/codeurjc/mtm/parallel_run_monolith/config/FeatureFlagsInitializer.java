package es.codeurjc.mtm.parallel_run_monolith.config;

import javax.annotation.PostConstruct;
import org.ff4j.FF4j;
import org.springframework.stereotype.Component;

@Component
public class FeatureFlagsInitializer {

  private FF4j ff4j;

  public FeatureFlagsInitializer(FF4j ff4j) {
    this.ff4j = ff4j;
  }

  @PostConstruct
  public void initializeFlags() {

  }

}