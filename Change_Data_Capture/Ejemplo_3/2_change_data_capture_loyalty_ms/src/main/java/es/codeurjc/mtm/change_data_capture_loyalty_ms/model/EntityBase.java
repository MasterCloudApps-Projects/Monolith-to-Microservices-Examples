package es.codeurjc.mtm.change_data_capture_loyalty_ms.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Data
@EqualsAndHashCode(callSuper = false)
@MappedSuperclass
public abstract class EntityBase<I> {

  @JsonProperty("rowVersion")
  @Column(name = "row_version")
  @Version
  Integer version = 0;

  @Column(updatable = false)
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  @CreationTimestamp
  private LocalDateTime creationTimestamp;

  @JsonFormat(shape = JsonFormat.Shape.STRING)
  @UpdateTimestamp
  private LocalDateTime modificationTimestamp;

  public abstract I getId();

}