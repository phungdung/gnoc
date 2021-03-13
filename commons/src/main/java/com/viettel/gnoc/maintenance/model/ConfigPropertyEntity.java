package com.viettel.gnoc.maintenance.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.maintenance.dto.ConfigPropertyDTO;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(schema = "COMMON_GNOC", name = "CONFIG_PROPERTY")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConfigPropertyEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "CONFIG_PROPERTY_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "KEY", unique = true, nullable = false)
  private String key;

  @Column(name = "VALUE")
  private String value;

  @Column(name = "DESCRIPTION")
  private String description;

  public ConfigPropertyDTO toDTO() {
    ConfigPropertyDTO dto = new ConfigPropertyDTO(
        key, value, description
    );
    return dto;
  }
}
