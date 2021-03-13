package com.viettel.gnoc.maintenance.model;


import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.maintenance.dto.MrCDCheckListBDDTO;
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
 * @author kienpv
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(schema = "OPEN_PM", name = "MR_CD_CHECKLIST_BD")
public class MrCDCheckListBDEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "OPEN_PM.MR_CD_CHECKLIST_BD_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "CHECKLIST_ID", unique = true, nullable = false)
  private Long checkListId;

  @Column(name = "MARKET_CODE")
  private String marketCode;

  @Column(name = "ARRAY_CODE")
  private String arrayCode;

  @Column(name = "DEVICE_TYPE")
  private String deviceType;

  @Column(name = "CYCLE")
  private Long cycle;

  @Column(name = "CREATED_DATE")
  private Date createdDate;

  @Column(name = "CREATED_USER")
  private String createdUser;

  @Column(name = "UPDATED_DATE")
  private Date updatedDate;

  @Column(name = "UPDATED_USER")
  private String updatedUser;

  @Column(name = "PURPOSE")
  private String purPose;

  @Column(name = "CONTENT")
  private String content;

  @Column(name = "GOAL")
  private String goal;

  public MrCDCheckListBDDTO toDTO() {
    MrCDCheckListBDDTO dto = new MrCDCheckListBDDTO(
        checkListId
        , marketCode
        , arrayCode
        , deviceType
        , String.valueOf(cycle)
        , createdDate
        , createdUser
        , updatedDate
        , updatedUser
        , purPose
        , content
        , goal
    );
    return dto;
  }

}
