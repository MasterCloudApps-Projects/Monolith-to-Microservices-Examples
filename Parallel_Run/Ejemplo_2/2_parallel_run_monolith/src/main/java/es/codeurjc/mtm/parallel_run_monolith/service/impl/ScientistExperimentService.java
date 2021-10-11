package main.java.es.codeurjc.mtm.parallel_run_monolith.service.impl;

import es.codeurjc.mtm.parallel_run_monolith.model.Payroll;
import es.codeurjc.mtm.parallel_run_monolith.service.UserNotificationService;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ScientistExperimentService {
      
    private Experiment<String> experiment;
    private ConsoleReporter reporter;
  
    @Autowired
    private UserNotificationServiceImpl userNotificationService = new UserNotificationServiceImpl();
  
    @Autowired
    private UserNotificationServiceMSImpl userNotificationServiceMS = new UserNotificationServiceMSImpl();
  
    public Boolean scientistExperiment() {

      experiment = new Experiment<>("notify",true);
      MetricRegistry metrics = experiment.getMetrics(null);
  
      reporter = ConsoleReporter.forRegistry(metrics)
          .convertRatesTo(TimeUnit.SECONDS)
          .convertDurationsTo(TimeUnit.MILLISECONDS)
          .build();
      reporter.start(1, TimeUnit.SECONDS);
      
      Supplier<String> oldCodePath = () -> userNotificationService.getNotify(1L);
      Supplier<String> newCodePath = () -> userNotificationServiceMS.getNotify(1L);
  
        try {
          String experimentResult = experiment.run(oldCodePath, newCodePath);
        } catch (Exception e) {
            System.out.println(e);
        }
        reporter.report();
        return experimentResult == userNotificationService.getNotify(1L);
    }
  }
  

}
