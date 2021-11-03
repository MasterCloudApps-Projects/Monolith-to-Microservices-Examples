package es.codeurjc.mtm.strangler_fig_payroll_ms.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class UserNotificationService {

  @Value("${monolith.host}")
  private String MONOLITH_HOST;

  @Value("${monolith.port}")
  private int MONOLITH_PORT;

  @Async
  public CompletableFuture<String> notify(String msg) {
    RestTemplate restTemplate = new RestTemplate();
    String url = "http://" + MONOLITH_HOST + ":" + MONOLITH_PORT + "/notification";
    String response = restTemplate.postForObject(url, msg, String.class);
    return CompletableFuture.completedFuture(response);
  }
}