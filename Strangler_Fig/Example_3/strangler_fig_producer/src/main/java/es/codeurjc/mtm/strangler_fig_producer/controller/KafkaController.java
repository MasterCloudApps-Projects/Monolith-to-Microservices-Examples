package es.codeurjc.mtm.strangler_fig_producer.controller;

import es.codeurjc.mtm.strangler_fig_producer.queue.KafkaProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class KafkaController {

  private final KafkaProducer kafkaProducer;

  @PostMapping("/messages/send-payroll")
  public ResponseEntity<String> sendPayrollMessage(@RequestBody String message) {
    kafkaProducer.sendPayrollMessage(message);
    return ResponseEntity.ok(message);
  }

  @PostMapping("/messages/send-invoicing")
  public ResponseEntity<String> sendInvoicingMessage(@RequestBody String message) {
    kafkaProducer.sendInvoicingMessage(message);
    return ResponseEntity.ok(message);
  }
}