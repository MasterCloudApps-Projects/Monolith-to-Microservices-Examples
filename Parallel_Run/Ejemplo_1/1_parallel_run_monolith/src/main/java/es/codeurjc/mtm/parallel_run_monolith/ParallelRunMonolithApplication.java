package es.codeurjc.mtm.parallel_run_monolith;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class ParallelRunMonolithApplication {

  public static void main(String[] args) {
    SpringApplication.run(ParallelRunMonolithApplication.class, args);
  }

}
