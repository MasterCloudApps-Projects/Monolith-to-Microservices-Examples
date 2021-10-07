package es.codeurjc.mtm.parallel_run_monolith;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.rawls238.scientist4j.Experiment;
import es.codeurjc.mtm.parallel_run_monolith.service.UserNotificationService;
import es.codeurjc.mtm.parallel_run_monolith.service.impl.UserNotificationServiceImpl;
import es.codeurjc.mtm.parallel_run_monolith.service.impl.UserNotificationServiceMSImpl;
import io.dropwizard.metrics5.MetricRegistry;
import io.dropwizard.metrics5.ConsoleReporter;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;


public class ParallelRunRestTest {

  private Experiment<String> experiment;
  private ConsoleReporter reporter;

  @Autowired
  private UserNotificationServiceImpl userNotificationService = new UserNotificationServiceImpl();

  @Autowired
  private UserNotificationServiceMSImpl userNotificationServiceMS = new UserNotificationServiceMSImpl();

  @BeforeEach
  public void setup() {
    experiment = new Experiment<>("notify",true);
    MetricRegistry metrics = experiment.getMetrics(null);

    reporter = ConsoleReporter.forRegistry(metrics)
        .convertRatesTo(TimeUnit.SECONDS)
        .convertDurationsTo(TimeUnit.MILLISECONDS)
        .build();
    reporter.start(1, TimeUnit.SECONDS);
  }

  @Test
  void scientistExperiment() {

    //De momento falseo resultados y pruebo una nueva forma
    Supplier<String> oldCodePath = () -> userNotificationService.getNotify(1L);
    Supplier<String> newCodePath = () -> userNotificationServiceMS.getNotify(1L);

      try {
        String experimentResult = experiment.run(oldCodePath, newCodePath);
        assertEquals(experimentResult, userNotificationService.getNotify(1L));
      } catch (Exception e) {
        System.out.println(e);
      }
    reporter.report();
  }
}
