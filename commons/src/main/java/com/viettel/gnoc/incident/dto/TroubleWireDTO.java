package com.viettel.gnoc.incident.dto;

import com.viettel.gnoc.incident.model.TroubleWireEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TroubleWireDTO {

  private Long id;
  private Long troubleId;
  private String serviceType;
  private String telServiceId;
  private String infraType;
  private String isCcResult;
  private String productCode;
  private String accountIsdn;
  private String customerPhone;
  private String content;

  public TroubleWireEntity toEntity() {
    return new TroubleWireEntity(id, troubleId, serviceType, telServiceId, infraType, isCcResult,
        productCode, accountIsdn, customerPhone, content);
  }
}
