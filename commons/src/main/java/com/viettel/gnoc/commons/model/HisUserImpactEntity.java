package com.viettel.gnoc.commons.model;

import static javax.persistence.GenerationType.SEQUENCE;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.viettel.gnoc.commons.dto.ContactDTO;
import com.viettel.gnoc.commons.dto.HisUserImpactDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "COMMON_GNOC", name = "HIS_USER_IMPACT")
public class HisUserImpactEntity {

  //thanhlv12 add 25-09-2020
  @Id
  @SequenceGenerator(name = "generator", sequenceName = "HIS_USER_IMPACT_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "HIS_ID")
  private Long hisId;

  @Column(name = "USER_ID")
  private String userId;

  @Column(name = "TYPE")
  private String type;

  @Column(name = "ACTION_TYPE")
  private String actionType;

  @Column(name = "NEW_OBJECT")
  private String newObject;

  @Column(name = "CREATE_TIME")
  private Date createTime;

  @Column(name = "RESULT")
  private String result;

  public HisUserImpactDTO toDTO() {
    return  new HisUserImpactDTO(hisId, userId, type, actionType, newObject, createTime, result);
  }
}


