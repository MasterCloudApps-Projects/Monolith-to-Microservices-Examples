package es.codeurjc.mtm.change_data_capture_monolith.controller;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

import es.codeurjc.mtm.change_data_capture_monolith.model.Loyalty;
import es.codeurjc.mtm.change_data_capture_monolith.service.LoyaltyService;
import java.net.URI;
import java.util.Collection;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/loyalty")
public class LoyaltyController {

  private LoyaltyService loyaltyService;

  public LoyaltyController(
      LoyaltyService loyaltyService) {
    this.loyaltyService = loyaltyService;
  }

  @PostMapping({""})
  public ResponseEntity<Loyalty> createLoyalty(@RequestBody Loyalty loyalty) {
    this.loyaltyService.saveLoyalty(loyalty);
    URI location = fromCurrentRequest().path("/{id}").buildAndExpand(loyalty.getId()).toUri();

    return ResponseEntity.created(location).body(loyalty);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Loyalty> getLoyalty(
      @PathVariable long id) {
    Loyalty loyalty = this.loyaltyService.getLoyalty(id);

    if (loyalty != null) {
      return ResponseEntity.ok(this.loyaltyService.getLoyalty(id));
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @GetMapping({""})
  public ResponseEntity<Collection<Loyalty>> getInventories() {
    return ResponseEntity.ok(loyaltyService.findAll());
  }

  @PostMapping({"/printer"})
  public void createLoyalty(@RequestBody String loyaltyInfo) {
    log.info("loyaltyInfo", loyaltyInfo);
  }

}
