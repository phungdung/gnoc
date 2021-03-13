package com.viettel.gnoc.commons.model;

import com.viettel.gnoc.commons.dto.CfgSmsUserGoingOverdueDTO;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(schema = "COMMON_GNOC", name = "CFG_SMS_USER_GOING_OVERDUE")
public class CfgSmsUserGoingOverdueEntity implements Serializable {

  static final long serialVersionUID = 1L;

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "CFG_SMS_USER_GOING_OVERDUE_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "generator")
  @Column(name = "ID")
  private Long id;
  @Column(name = "CFG_ID", nullable = false)
  private Long cfgId;
  @Column(name = "USER_ID", nullable = false)
  private Long userId;
  @Column(name = "TYPE_CFG", nullable = false)
  private Long cfgType;

  public CfgSmsUserGoingOverdueDTO toDTO() {
    CfgSmsUserGoingOverdueDTO dto = new CfgSmsUserGoingOverdueDTO(
        id == null ? null : id.toString(),
        cfgId == null ? null : cfgId.toString(),
        userId == null ? null : userId.toString(),
        cfgType == null ? null : cfgType.toString()
    );
    return dto;
  }
}
