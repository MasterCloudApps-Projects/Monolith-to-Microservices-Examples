package es.codeurjc.mtm.parallel_run_monolith;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class ParallelRunMonolithApplication extends SpringBootServletInitializer {

  public static void main(String[] args) {
    SpringApplication.run(ParallelRunMonolithApplication.class, args);
  }

}
