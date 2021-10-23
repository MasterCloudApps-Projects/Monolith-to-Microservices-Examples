package es.codeurjc.mtm.parallel_run_monolith.controller;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

import es.codeurjc.mtm.parallel_run_monolith.model.Invoicing;
import es.codeurjc.mtm.parallel_run_monolith.service.impl.InvoicingService;

import java.io.IOException;
import java.net.URI;
import java.util.Collection;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/invoicing")
public class InvoicingController {

  private InvoicingService invoicingService;

  public InvoicingController(
      InvoicingService invoicingService) {
    this.invoicingService = invoicingService;
  }

  @PostMapping({""})
  public ResponseEntity<Invoicing> createInvoicing(@RequestBody Invoicing invoicing) throws IOException {
    this.invoicingService.saveInvoicing(invoicing);
    URI location = fromCurrentRequest().path("/{id}").buildAndExpand(invoicing.getId()).toUri();

    return ResponseEntity.created(location).body(invoicing);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Invoicing> getInvoicing(
      @PathVariable long id) {
    Invoicing invoicing = this.invoicingService.getInvoicing(id);

    if (invoicing != null) {
      return ResponseEntity.ok(this.invoicingService.getInvoicing(id));
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @GetMapping({""})
  public ResponseEntity<Collection<Invoicing>> getInvoicings() {
    return ResponseEntity.ok(invoicingService.findAll());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Invoicing> deleteInvoicing(
      @PathVariable long id) {
    Invoicing invoicing = this.invoicingService.getInvoicing(id);

    if (invoicing != null) {
      this.invoicingService.deleteInvoicing(id);
      return ResponseEntity.ok(invoicing);
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @PutMapping({"/{id}"})
  public ResponseEntity<Invoicing> createInvoicing(@RequestBody Invoicing invoicing,
      @PathVariable long id) {
    Invoicing invoicingUpdated = this.invoicingService.updateInvoicing(id, invoicing);

    if (invoicingUpdated != null) {
      return ResponseEntity.ok(invoicing);
    } else {
      return ResponseEntity.notFound().build();
    }

  }

}
