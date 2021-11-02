package es.codeurjc.mtm.parallel_run_monolith;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class ParallelRunMonolithApplication extends SpringBootServletInitializer {

  public static void main(String[] args) {
    SpringApplication.run(ParallelRunMonolithApplication.class, args);
  }

}
