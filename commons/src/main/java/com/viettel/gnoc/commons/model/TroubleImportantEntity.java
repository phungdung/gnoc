package com.viettel.gnoc.commons.model;

import com.viettel.gnoc.commons.dto.TroubleImportantDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

import static javax.persistence.GenerationType.SEQUENCE;


/**
 * @author hungtv77
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "COMMON_GNOC", name = "MANAGERMENT_TROUBLE_IMPORTANT")
public class TroubleImportantEntity implements Serializable {

  static final long serialVersionUID = 1L;

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "TROUBLE_IMPORTANT_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "TROUBLE_ID", unique = true)
  private Long troubleId;

  @Column(name = "TROUBLE_NAME")
  private String troubleName;

  @Column(name = "COUNTRY_ID")
  private String countryId;

  @Column(name = "PRIORITY_ID")
  private String priorityId;

  @Column(name = "CLASS_NETWORK")
  private String classNetwork;

  @Column(name = "ARRAY")
  private String array;

  @Column(name = "IS_ALARM")
  private String isAlarm;

  @Column(name = "START_TIME")
  private Date startTime;

  @Column(name = "END_TIME")
  private Date endTime;

  @Column(name = "PROCESS_TIME")
  private Date processTime;

  @Column(name = "NUMBER_PAKH")
  private String numberPakh;

  @Column(name = "NUMBER_PAKH_REAL")
  private String numberPakhReal;

  @Column(name = "GROUP_REASON")
  private String groupReason;

  @Column(name = "NATURE")
  private String nature;

  @Column(name = "DETAIL")
  private String detail;

  @Column(name = "COMFIRM_TROUBLE")
  private String confirmTrouble;

  @Column(name = "SERVICE_OWNER")
  private String serviceOwner;

  @Column(name = "UNIT_ID")
  private String unitId;

  @Column(name = "PT_CODE")
  private String ptCode;

  @Column(name = "VENDER")
  private String vender;

  @Column(name = "IS_NOT_ALARM")
  private String isNotAlarm;

  @Column(name = "TRIGGER_UNIT")
  private String triggerUnit;

  @Column(name = "WEEK")
  private String week;

  @Column(name = "DETECT_TIME")
  private Date detectTime;

  @Column(name = "DETECT_TROUBLE_TIME")
  private Date detectTroubleTime;

  @Column(name = "USER_CREATE")
  private String userCreate;

  @Column(name = "USER_DETECT")
  private String userDetect;

  @Column(name = "USER_PROCESS")
  private String userProcess;

  @Column(name = "COORDINATION")
  private String coordination;

  @Column(name = "ARRAY_NAME")
  private String arrayName;

  @Column(name = "GROUP_REASON_NAME")
  private String groupReasonName;

  public TroubleImportantDTO toDTO() {
    return new TroubleImportantDTO(troubleId, troubleName, countryId, priorityId, classNetwork, array, isAlarm, startTime,
        endTime, processTime, numberPakh, numberPakhReal, groupReason, nature, detail, confirmTrouble, serviceOwner,
        unitId, ptCode, vender, isNotAlarm, triggerUnit, week, detectTime, detectTroubleTime, userCreate, userDetect,
        userProcess, coordination, arrayName, groupReasonName);
  }
}
