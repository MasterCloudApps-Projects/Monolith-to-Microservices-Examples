package es.codeurjc.mtm.parallel_run_monolith.service.impl;

import com.github.rawls238.scientist4j.Experiment;
import com.github.rawls238.scientist4j.ExperimentBuilder;
import es.codeurjc.mtm.parallel_run_monolith.service.UserNotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.function.Supplier;

@Slf4j
@Service
public class ScientistExperimentService {

    @Autowired
    @Qualifier("userNotificationServiceImpl")
    private UserNotificationService userNotificationService;
  
    @Autowired
    @Qualifier("userNotificationServiceMSImpl")
    private UserNotificationService userNotificationServiceMS;

    public Boolean scientistExperiment(Long id) {
      Experiment<String> experiment = new ExperimentBuilder<String>().withName("notify").build();

      Supplier<String> oldCodePath = () -> userNotificationService.getNotify(id);
      Supplier<String> newCodePath = () -> userNotificationServiceMS.getNotify(id);
      String experimentResult = "";
        try {
          experimentResult = experiment.run(oldCodePath, newCodePath);
        } catch (Exception e) {
            System.out.println(e);
        }
        Boolean result = experimentResult == userNotificationService.getNotify(id);
        log.info("The ScientistExperiment result with compare oldCode/newCode is "+result);
        return result;
    }
}

