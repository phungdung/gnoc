package com.viettel.gnoc.wo.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VsmartUpdateForm {

  private List<String> listFileName;
  private List<byte[]> fileArr;

  private List<ObjKeyValue> lstDataKeyValue;
  private Long solutionGroupId;
  private String solutionGroupName;
  private String solution;
  private Double polesDistance; // khoang cot
  private String contractCode; // ma hop dong
  private Long contractId; // Id hop dong
  private Long scriptId;  // id kich ban
  private String scriptName;  // id kich ban
  private String reasonTroubleId;// nguyen nhan su co tu tt
  private String reasonTroubleName; // nguyen nhan su co tu tt
  private String clearTime;
  private String id;
  private String key;
  private String message;
  private String closuresReplace; // mang xong thay the
  private String lineCutCode;// ma tuyen dut
  private String codeSnippetOff;// ma doan dut
  private String stationEnvironment;
  private String stationCodeNims;
  private String reasonApproveNok;

  private String cellService;
  private String concaveAreaCode;
  private String longitude;
  private String latitude;
  private String pendingType;
  private String estimateTime;  // thoi gian du kien khac phuc
  private List<SupportCaseForm> lstSupportCase;
  private String reasonDetail;

  private String reasonInterference;
  private List<MaterialCodienDTO> lstMaterialCodien;
  private Long isRetrieve;  // co thu hoi cap hay khong 1 co 0 khong
  private String newFailureReason;
  private String hasCost;
  private String hasMaterial;

  private String isMultipleErr;
  private Long byPassAutoCheck;

  private String intendedTime;
  private String resultCheck;
  private String resultCheckName;

  private List<WoMaterialDeducteDTO> lstGoodsNew;
  private List<WoMaterialDeducteDTO> lstGoodsOld;
}
