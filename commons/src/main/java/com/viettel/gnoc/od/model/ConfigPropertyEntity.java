/**
 * @(#)AffectedServicesBO.java 8/14/2015 9:50 AM, Copyright 2011 Viettel Telecom. All rights
 * reserved VIETTEL PROPRIETARY/CONFIDENTIAL
 */
package com.viettel.gnoc.od.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.od.dto.ConfigPropertyDTO;
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

/**
 * @author itsol
 * @version 2.0
 * @since 12/03/2018
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "COMMON_GNOC", name = "CONFIG_PROPERTY")
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
    return new ConfigPropertyDTO(key, value, description);
  }

}
