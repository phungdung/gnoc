package com.viettel.gnoc.sr.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.sr.dto.SRParamDTO;
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
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
@Entity
@Table(schema = "OPEN_PM", name = "SR_PARAM")
public class SRParamEntity {

  @Column(name = "SR_ID")
  private Long srId;

  @Column(name = "UPDATED_TIME")
  private Date updatedTime;

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "SR_PARAM_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "SR_PARAM_ID")
  private Long srParamId;

  @Column(name = "VALUE")
  private String value;

  @Column(name = "PARAM_TYPE")
  private String paramType;

  @Column(name = "UPDATED_USER")
  private String updatedUser;

  @Column(name = "KEY")
  private String key;

  public SRParamDTO toDTO() {
    try {
      return new SRParamDTO(
          srId == null ? null : srId.toString()
          , DateTimeUtils.date2ddMMyyyyString(updatedTime)
          , srParamId == null ? null : srParamId.toString()
          , value
          , paramType
          , updatedUser
          , key
      );
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }
}
