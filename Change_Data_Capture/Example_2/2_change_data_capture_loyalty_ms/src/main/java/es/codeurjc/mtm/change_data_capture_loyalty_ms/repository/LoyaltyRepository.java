package es.codeurjc.mtm.change_data_capture_loyalty_ms.repository;

import es.codeurjc.mtm.change_data_capture_loyalty_ms.model.Loyalty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoyaltyRepository extends JpaRepository<Loyalty,Long> {

}
