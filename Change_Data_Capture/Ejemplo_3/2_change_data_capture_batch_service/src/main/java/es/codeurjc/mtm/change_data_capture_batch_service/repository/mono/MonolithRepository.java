package es.codeurjc.mtm.change_data_capture_batch_service.repository.mono;


import es.codeurjc.mtm.change_data_capture_batch_service.model.Loyalty;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MonolithRepository extends JpaRepository<Loyalty, Long> {

  @Query(value = "SELECT * FROM Loyalty l WHERE l.modification_timestamp >= :localDate OR"
      + " l.creation_timestamp >= :localDate ", nativeQuery = true)
  List<Loyalty> getAllPostDateLoyaltyChanged(@Param("localDate") LocalDateTime localDateTime);
}
