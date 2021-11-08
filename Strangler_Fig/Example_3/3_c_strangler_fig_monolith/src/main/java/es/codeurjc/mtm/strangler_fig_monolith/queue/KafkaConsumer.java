package es.codeurjc.mtm.strangler_fig_monolith.queue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.codeurjc.mtm.strangler_fig_monolith.model.Invoicing;
import es.codeurjc.mtm.strangler_fig_monolith.service.InvoicingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaConsumer {

  ObjectMapper mapper;

  private final InvoicingService invoicingService;

  public KafkaConsumer(InvoicingService invoicingService) {
    mapper = new ObjectMapper();
    this.invoicingService = invoicingService;
  }

  @KafkaListener(topics = "${kafka.topic.invoicing.name}")
  public void invoicingListener(@Payload String message) throws JsonProcessingException {
    Invoicing invoicing = this.mapper.readValue(message, Invoicing.class);
    invoicingService.saveInvoicing(invoicing);
  }
}