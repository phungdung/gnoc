package com.viettel.gnoc.commons.dto;

import com.viettel.gnoc.commons.model.CfgTroubleCallIpccEntity;
import com.viettel.gnoc.commons.validator.MultiFieldUnique;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MultiFieldUnique(message = "{validation.CfgTroubleCallIpccDTO.unique}", clazz = CfgTroubleCallIpccEntity.class, uniqueFields = "cfgId,levelCall", idField = "id")
public class CfgTroubleCallIpccDTO extends BaseDto {

  private Long id;
  @NotNull(message = "{validation.CfgTroubleCallIpccDTO.null.cfgId}")
  private Long cfgId;
  @NotNull(message = "{validation.CfgTroubleCallIpccDTO.null.levelCall}")
  private Long levelCall;
  @NotNull(message = "{validation.CfgTroubleCallIpccDTO.null.timeProcess}")
  private Long timeProcess;
  @NotEmpty(message = "{validation.CfgTroubleCallIpccDTO.null.fileName}")
  private String fileName;
  private String receiveUserName;

  private String cfgName;
  private String levelCallName;

  public CfgTroubleCallIpccDTO(Long id, Long cfgId, Long levelCall, Long timeProcess,
      String fileName, String receiveUserName) {
    this.id = id;
    this.cfgId = cfgId;
    this.levelCall = levelCall;
    this.timeProcess = timeProcess;
    this.fileName = fileName;
    this.receiveUserName = receiveUserName;
  }

  public CfgTroubleCallIpccEntity toEntity() {
    return new CfgTroubleCallIpccEntity(id, cfgId, levelCall, timeProcess, fileName,
        receiveUserName);
  }
}
