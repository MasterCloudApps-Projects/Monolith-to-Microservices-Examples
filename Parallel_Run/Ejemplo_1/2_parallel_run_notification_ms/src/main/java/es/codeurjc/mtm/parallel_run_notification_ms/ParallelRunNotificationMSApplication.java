package es.codeurjc.mtm.parallel_run_notification_ms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.Transactional;

@SpringBootApplication
//@EnableJpaRepositories
public class ParallelRunNotificationMSApplication extends SpringBootServletInitializer {

  public static void main(String[] args) {
    SpringApplication.run(ParallelRunNotificationMSApplication.class, args);
  }

}
