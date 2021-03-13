package com.viettel.gnoc.wo.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.wo.dto.WoTypeInsideDTO;
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
@Table(schema = "WFM", name = "WO_TYPE")
public class WoTypeEntity {

  static final long serialVersionUID = 1L;

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "WO_TYPE_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "WO_TYPE_ID", unique = true, nullable = false)
  private Long woTypeId;

  @Column(name = "WO_TYPE_CODE", nullable = false)
  private String woTypeCode;

  @Column(name = "WO_TYPE_NAME", nullable = false)
  private String woTypeName;

  @Column(name = "IS_ENABLE")
  private Long isEnable;

  @Column(name = "WO_GROUP_TYPE")
  private Long woGroupType;

  @Column(name = "ENABLE_CREATE")
  private Long enableCreate;

  @Column(name = "TIME_OVER")
  private Long timeOver;

  @Column(name = "SMS_CYCLE")
  private Long smsCycle;

  @Column(name = "WO_CLOSE_AUTOMATIC_TIME")
  private String woCloseAutomaticTime;

  @Column(name = "USER_TYPE_CODE")
  private String userTypeCode;

  @Column(name = "ALLOW_PENDING")
  private Long allowPending;

  @Column(name = "CREATE_FROM_OTHER_SYS")
  private Long createFromOtherSys;

  @Column(name = "TIME_AUTO_CLOSE_WHEN_OVER")
  private Long timeAutoCloseWhenOver;

  @Column(name = "PROCESS_TIME")
  private Long processTime;

  public WoTypeInsideDTO toDTO() {
    return new WoTypeInsideDTO(
        woTypeId
        , woTypeCode
        , woTypeName
        , isEnable
        , woGroupType
        , null
        , enableCreate
        , timeOver
        , smsCycle
        , woCloseAutomaticTime
        , userTypeCode
        , allowPending
        , createFromOtherSys
        , timeAutoCloseWhenOver
        , processTime
        , null
        , null
        , null
        , null
        , null);
  }

}
