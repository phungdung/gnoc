package com.viettel.gnoc.od.model;


import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.od.dto.OdParamInsideDTO;
import java.util.Date;
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

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "WFM", name = "OD_PARAM")
public class OdParamEntity {

  static final long serialVersionUID = 1L;

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "OD_PARAM_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "OD_PARAM_ID", unique = true, nullable = false)
  private Long odParamId;
  @Column(name = "OD_ID")
  private Long odId;
  @Column(name = "UPDATED_TIME")
  private Date updatedTime;
  @Column(name = "VALUE")
  private String value;
  @Column(name = "PARAM_TYPE")
  private String paramType;
  @Column(name = "UPDATED_USER")
  private String updatedUser;
  @Column(name = "KEY")
  private String key;

  public OdParamInsideDTO toDTO() {
    return new OdParamInsideDTO(odParamId, odId, updatedTime, value, paramType, updatedUser, key);
  }
}
