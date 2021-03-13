package com.viettel.gnoc.sr.dto;

import com.viettel.gnoc.sr.model.SRApproveEntity;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SRApproveDTO {

  private Long approveId;
  private Long srId;
  private Long approveLevel1;
  private Long approveUnitLevel1;
  private Long approveLevel2;
  private Long approveUnitLevel2;
  private String approveUserLevel1;
  private Date approveDateLevel1;
  private String approveUserLevel2;
  private Date approveDateLevel2;

  private String isApproved;
  private Date approveDate;
  private String approveUser;
  private String approveUnit;

  public SRApproveEntity toEntity() {
    return new SRApproveEntity(approveId, srId
        , approveLevel1, approveUnitLevel1
        , approveLevel2, approveUnitLevel2
        , approveUserLevel1
        , approveDateLevel1
        , approveUserLevel2
        , approveDateLevel2);
  }

}
