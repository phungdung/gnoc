package com.viettel.gnoc.maintenance.model;


import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.maintenance.dto.MrCDWorkItemDTO;
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

/**
 * @author trungduong
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(schema = "OPEN_PM", name = "MR_CD_WORKITEM")
public class MrCDWorkItemEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "OPEN_PM.MR_CD_WORKITEM_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "WI_ID", unique = true, nullable = false)
  private Long wiId;

  @Column(name = "MARKET_CODE")
  private String marketCode;

  @Column(name = "ARRAY_CODE")
  private String arrayCode;

  @Column(name = "DEVICE_TYPE")
  private String deviceType;

  @Column(name = "CYCLE")
  private Long cycle;

  @Column(name = "MR_MODE")
  private String mrMode;

  @Column(name = "WORKITEMS")
  private String workItems;

  @Column(name = "CREATED_DATE")
  private Date createdDate;

  @Column(name = "CREATED_USER")
  private String createdUser;

  @Column(name = "UPDATED_DATE")
  private Date updatedDate;

  @Column(name = "UPDATED_USER")
  private String updatedUser;


  public MrCDWorkItemDTO toDTO() {
    MrCDWorkItemDTO dto = new MrCDWorkItemDTO(
        wiId
        , marketCode
        , arrayCode
        , deviceType
        , cycle
        , mrMode
        , workItems
        , createdDate
        , createdUser
        , updatedDate
        , updatedUser
    );
    return dto;
  }

}
