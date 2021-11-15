package es.codeurjc.mtm.change_data_capture_loyalty_ms.service;

import es.codeurjc.mtm.change_data_capture_loyalty_ms.model.Loyalty;
import es.codeurjc.mtm.change_data_capture_loyalty_ms.repository.LoyaltyRepository;
import java.util.Collection;
import org.springframework.stereotype.Service;

@Service
public class LoyaltyService {

  private LoyaltyRepository loyaltyRepository;

  public LoyaltyService(LoyaltyRepository loyaltyRepository) {
    this.loyaltyRepository = loyaltyRepository;
  }

  public Collection<Loyalty> findAll() {
    return loyaltyRepository.findAll();
  }

  public void saveLoyalty(Loyalty loyalty) {
    loyaltyRepository.save(loyalty);
  }

  public Loyalty getLoyalty(Long id) {
    return loyaltyRepository.findById(id).get();
  }

}
