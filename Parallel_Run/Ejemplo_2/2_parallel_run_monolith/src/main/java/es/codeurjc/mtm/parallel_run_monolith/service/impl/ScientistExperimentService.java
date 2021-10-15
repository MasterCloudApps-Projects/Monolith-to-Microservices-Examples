package es.codeurjc.mtm.parallel_run_monolith.service.impl;

import com.github.rawls238.scientist4j.Experiment;
import com.github.rawls238.scientist4j.metrics.DropwizardMetricsProvider;
import es.codeurjc.mtm.parallel_run_monolith.service.UserNotificationService;
import io.dropwizard.metrics5.Counter;
import io.dropwizard.metrics5.MetricName;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.concurrent.Callable;

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

        DropwizardMetricsProvider metricRegistry = new DropwizardMetricsProvider();
        Experiment<String> experiment = new Experiment<>("notify", metricRegistry);

        Callable<String> oldCodePath = () -> userNotificationService.getNotify(id);
        Callable<String> newCodePath = () -> userNotificationServiceMS.getNotify(id);
        try {
            experiment.run(oldCodePath, newCodePath);
        } catch (Exception e) {
            System.out.println(e);
        }

        MetricName metricName = MetricName.build("scientist.notify.mismatch");
        Counter result = metricRegistry.getRegistry().getCounters().get(metricName);

        log.info("The ScientistExperiment result with compare oldCode/newCode is " + result.getCount() + " mismatch");
        return result.getCount() == 0;
    }
}

