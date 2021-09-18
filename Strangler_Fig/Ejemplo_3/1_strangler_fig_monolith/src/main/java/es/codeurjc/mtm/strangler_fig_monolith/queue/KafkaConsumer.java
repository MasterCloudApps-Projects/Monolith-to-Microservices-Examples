package es.codeurjc.mtm.strangler_fig_monolith.queue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.codeurjc.mtm.strangler_fig_monolith.model.Invoicing;
import es.codeurjc.mtm.strangler_fig_monolith.model.Payroll;
import es.codeurjc.mtm.strangler_fig_monolith.service.InvoicingService;
import es.codeurjc.mtm.strangler_fig_monolith.service.PayrollService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaConsumer {

  ObjectMapper mapper;

  private final InvoicingService invoicingService;
  private final PayrollService payrollService;

  public KafkaConsumer(InvoicingService invoicingService, PayrollService payrollService) {
    mapper = new ObjectMapper();
    this.invoicingService = invoicingService;
    this.payrollService = payrollService;
  }

  @KafkaListener(topics = "${kafka.topic.payroll.name}")
  public void payrollListener(@Payload String message) throws JsonProcessingException {
    Payroll payroll = this.mapper.readValue(message, Payroll.class);
    payrollService.savePayroll(payroll);
  }

  @KafkaListener(topics = "${kafka.topic.invoicing.name}")
  public void invoicingListener(@Payload String message) throws JsonProcessingException {
    Invoicing invoicing = this.mapper.readValue(message, Invoicing.class);
    invoicingService.saveInvoicing(invoicing);
  }
}