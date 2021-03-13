package com.viettel.gnoc.incident.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.incident.dto.WoSupportDTO;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "WFM", name = "WO_SUPPORT")
public class WoSupportEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "WO_SUPPORT_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "ID", unique = true, nullable = false)
  private Long id;
  @Column(name = "WO_ID")
  private Long woID;
  @Column(name = "CFG_SUPPORT_CASE_ID")
  private Long cfgSupportCaseID;
  @Column(name = "CFG_SUPPORT_CASE_TEST_ID")
  private Long cfgSupportCaseTestID;
  @Column(name = "RESULT")
  private Long result;
  @Column(name = "DESCRIPTION")
  private String description;
  @Column(name = "FILE_NAME")
  private String fileName;
  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "UPDATE_TIME")
  private Date updateTime;

  public WoSupportDTO toDTO() {
    WoSupportDTO dto = new WoSupportDTO(id, woID, cfgSupportCaseID, cfgSupportCaseTestID, result,
        description, fileName, updateTime);
    return dto;
  }
}
