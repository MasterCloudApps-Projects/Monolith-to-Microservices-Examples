package es.codeurjc.mtm.change_data_capture_batch_service.repository.micro;


import es.codeurjc.mtm.change_data_capture_batch_service.model.Loyalty;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MicroRepository extends JpaRepository<Loyalty, Long> {

}
