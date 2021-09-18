package es.codeurjc.mtm.strangler_fig_cbr.queue;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaConsumer {

  private final KafkaProducer kafkaProducer;

  public KafkaConsumer(KafkaProducer kafkaProducer) {
    this.kafkaProducer = kafkaProducer;
  }

  @KafkaListener(topics = "${kafka.topic.consumer.payroll.name}")
  public void payrollListener(@Payload String message) {
    kafkaProducer.sendPayrollMessage(message);
  }

  @KafkaListener(topics = "${kafka.topic.consumer.invoicing.name}")
  public void invoicingListener(@Payload String message) {
    kafkaProducer.sendInvoicingMessage(message);

  }

}