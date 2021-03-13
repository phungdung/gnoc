package com.viettel.gnoc.wo.model;


import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.wo.dto.WoWorklogInsideDTO;
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
@Table(schema = "WFM", name = "WO_WORKLOG")
public class WoWorklogEntity {

  static final long serialVersionUID = 1L;

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "WO_WORKLOG_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "WO_WORKLOG_ID", unique = true, nullable = false)
  private Long woWorklogId;

  @Column(name = "WO_ID")
  private Long woId;

  @Column(name = "WO_WORKLOG_CONTENT")
  private String woWorklogContent;

  @Column(name = "WO_SYSTEM")
  private String woSystem;

  @Column(name = "WO_SYSTEM_ID")
  private String woSystemId;

  @Column(name = "USER_ID")
  private Long userId;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "UPDATE_TIME")
  private Date updateTime;

  @Column(name = "USERNAME")
  private String username;

  @Column(name = "NATION")
  private String nation;

  public WoWorklogInsideDTO toDTO() {
    return new WoWorklogInsideDTO(woWorklogId, woId, woWorklogContent, woSystem, woSystemId, userId,
        updateTime, username, nation);
  }
}
