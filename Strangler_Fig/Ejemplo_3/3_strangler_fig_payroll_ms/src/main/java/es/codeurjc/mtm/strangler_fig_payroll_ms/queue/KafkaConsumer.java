package es.codeurjc.mtm.strangler_fig_payroll_ms.queue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.codeurjc.mtm.strangler_fig_payroll_ms.model.Payroll;
import es.codeurjc.mtm.strangler_fig_payroll_ms.service.PayrollService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaConsumer {

  ObjectMapper mapper;

  private final PayrollService payrollService;

  public KafkaConsumer(PayrollService payrollService) {
    mapper = new ObjectMapper();
    this.payrollService = payrollService;
  }

  @KafkaListener(topics = "${kafka.topic.payroll.name}")
  public void payrollListener(@Payload String message) throws JsonProcessingException {
    Payroll payroll = this.mapper.readValue(message, Payroll.class);
    payrollService.savePayroll(payroll);
  }

}