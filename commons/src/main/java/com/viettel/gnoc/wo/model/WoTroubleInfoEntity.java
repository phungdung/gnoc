package com.viettel.gnoc.wo.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.wo.dto.WoTroubleInfoDTO;
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
@Table(schema = "WFM", name = "WO_TROUBLE_INFO")
public class WoTroubleInfoEntity {

  static final long serialVersionUID = 1L;

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "WO_TROUBLE_INFO_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "ID", unique = true, nullable = false)
  private Long id;

  @Column(name = "WO_ID")
  private Long woId;

  @Column(name = "SOLUTION_GROUP_ID")
  private Long solutionGroupId;

  @Column(name = "SOLUTION_GROUP_NAME")
  private String solutionGroupName;

  @Column(name = "SOLUTION")
  private String solution;

  @Column(name = "REASON_TROUBLE_ID")
  private Long reasonTroubleId;

  @Column(name = "REASON_TROUBLE_NAME")
  private String reasonTroubleName;

  @Column(name = "CLEAR_TIME")
  private Date clearTime;

  @Column(name = "KEDB_CODE")
  private String kedbCode;

  @Column(name = "KEDB_ID")
  private Long kedbId;

  @Column(name = "REQUIRED_TT_REASON")
  private Long requiredTtReason;

  @Column(name = "POLES_DISTANCE")
  private Double polesDistance; // khoang cot

  @Column(name = "SCRIPT_ID")
  private Long scriptId;  // id kich ban

  @Column(name = "SCRIPT_NAME")
  private String scriptName;  // id kich ban

  @Column(name = "CLOSURES_REPLACE")
  private String closuresReplace; // mang xong thay the

  @Column(name = "CODE_SNIPPET_OFF")
  private String codeSnippetOff;// ma doan dut

  @Column(name = "LINE_CUT_CODE")
  private String lineCutCode;// ma tuyen dut

  @Column(name = "ABLE_MOP")
  private Long ableMop;  // able halt

  @Column(name = "LINK_ID")
  private Long linkId;   //id tuyen

  @Column(name = "LINK_CODE")
  private String linkCode; //ma tuyen

  @Column(name = "ALARM_ID")
  private Long alarmId;//id khieu nai

  @Column(name = "CD_AUDIO_NAME")
  private String cdAudioName;//ten file am thanh cd

  @Column(name = "FT_AUDIO_NAME")
  private String ftAudioName;//ten file am thanh ft

  public WoTroubleInfoDTO toDTO() {
    return new WoTroubleInfoDTO(id, woId, solutionGroupId, solutionGroupName, solution,
        reasonTroubleId, reasonTroubleName, clearTime, kedbCode, kedbId, requiredTtReason,
        polesDistance, scriptId, scriptName, closuresReplace, codeSnippetOff, lineCutCode,
        ableMop, linkId, linkCode, alarmId, cdAudioName, ftAudioName);
  }

}
