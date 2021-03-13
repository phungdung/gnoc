package com.viettel.gnoc.wo.model;

import com.viettel.gnoc.wo.dto.WoConfigPropertyDTO;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "COMMON_GNOC", name = "CONFIG_PROPERTY")
public class WoConfigPropertyEntity {

  @Id
  @Column(name = "KEY", unique = true, nullable = false)
  private String key;
  @Column(name = "VALUE")
  private String value;
  @Column(name = "DESCRIPTION")
  private String description;

  public WoConfigPropertyDTO toDTO() {
    WoConfigPropertyDTO dto = new WoConfigPropertyDTO(key, value, description);
    return dto;
  }
}
