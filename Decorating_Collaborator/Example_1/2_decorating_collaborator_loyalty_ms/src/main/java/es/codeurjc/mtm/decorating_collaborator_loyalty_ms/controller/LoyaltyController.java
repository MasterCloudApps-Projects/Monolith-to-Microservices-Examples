package es.codeurjc.mtm.decorating_collaborator_loyalty_ms.controller;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

import es.codeurjc.mtm.decorating_collaborator_loyalty_ms.model.Loyalty;
import es.codeurjc.mtm.decorating_collaborator_loyalty_ms.service.LoyaltyService;
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
@RequestMapping("/loyalty")
public class LoyaltyController {

  private LoyaltyService loyaltyService;

  public LoyaltyController(
      LoyaltyService loyaltyService) {
    this.loyaltyService = loyaltyService;
  }

  @PostMapping({"/{userName}"})
  public ResponseEntity<Loyalty> createLoyalty(@PathVariable String userName) {
    Loyalty loyalty = this.loyaltyService.saveLoyalty(userName);
    URI location = fromCurrentRequest().path("/{id}").buildAndExpand(loyalty.getId()).toUri();

    return ResponseEntity.created(location).body(loyalty);
  }

  @GetMapping("/{userName}")
  public ResponseEntity<Loyalty> getLoyalty(
      @PathVariable String userName) {
    Loyalty loyalty = this.loyaltyService.getLoyaltyByUserName(userName);

    if (loyalty != null) {
      return ResponseEntity.ok(this.loyaltyService.getLoyaltyByUserName(userName));
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @GetMapping({""})
  public ResponseEntity<Collection<Loyalty>> getInventories() {
    return ResponseEntity.ok(loyaltyService.findAll());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Loyalty> deleteLoyalty(
      @PathVariable long id) {
    Loyalty loyalty = this.loyaltyService.getLoyalty(id);

    if (loyalty != null) {
      this.loyaltyService.deleteLoyalty(id);
      return ResponseEntity.ok(loyalty);
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @PutMapping({"/{id}"})
  public ResponseEntity<Loyalty> createLoyalty(@RequestBody Loyalty loyalty,
      @PathVariable long id) {
    Loyalty loyaltyUpdated = this.loyaltyService.updateLoyalty(id, loyalty);

    if (loyaltyUpdated != null) {
      return ResponseEntity.ok(loyalty);
    } else {
      return ResponseEntity.notFound().build();
    }

  }

}
