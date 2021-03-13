package com.viettel.gnoc.wo.dto;

import com.viettel.gnoc.wo.model.WoTroubleInfoEntity;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WoTroubleInfoDTO {

  private Long id;
  private Long woId;
  private Long solutionGroupId;
  private String solutionGroupName;
  private String solution;
  private Long reasonTroubleId;
  private String reasonTroubleName;
  private Date clearTime;
  private String kedbCode;
  private Long kedbId;
  private Long requiredTtReason;
  private Double polesDistance; // khoang cot
  private Long scriptId;  // id kich ban
  private String scriptName;  // id kich ban
  private String closuresReplace; // mang xong thay the
  private String codeSnippetOff;// ma doan dut
  private String lineCutCode;// ma tuyen dut
  private Long ableMop;  // able halt
  private Long linkId;   //id tuyen
  private String linkCode; //ma tuyen
  private Long alarmId;//id khieu nai
  private String cdAudioName;//ten file am thanh cd
  private String ftAudioName;//ten file am thanh ft

  public WoTroubleInfoEntity toEntity() {
    return new WoTroubleInfoEntity(id, woId, solutionGroupId, solutionGroupName, solution,
        reasonTroubleId, reasonTroubleName, clearTime, kedbCode, kedbId, requiredTtReason,
        polesDistance, scriptId, scriptName, closuresReplace, codeSnippetOff, lineCutCode,
        ableMop, linkId, linkCode, alarmId, cdAudioName, ftAudioName);
  }

}
