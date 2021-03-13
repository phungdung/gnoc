package com.viettel.gnoc.commons.proxy;

import com.viettel.gnoc.risk.dto.RiskCfgBusinessDTO;
import com.viettel.gnoc.risk.dto.RiskChangeStatusDTO;
import com.viettel.gnoc.risk.dto.RiskChangeStatusRoleDTO;
import com.viettel.gnoc.risk.dto.RiskSystemDTO;
import com.viettel.gnoc.risk.dto.RiskTypeDTO;
import com.viettel.gnoc.risk.dto.RiskTypeDetailDTO;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "risk-category-service")
public interface RiskCategoryServiceProxy {

  @PostMapping("/RiskSystem/getListRiskSystem")
  public List<RiskSystemDTO> getListRiskSystem(@RequestBody RiskSystemDTO riskSystemDTO);

  @PostMapping("/RiskType/getListRiskTypeDTO")
  public List<RiskTypeDTO> getListRiskTypeDTO(@RequestBody RiskTypeDTO riskTypeDTO);

  @PostMapping("/RiskType/getListRiskTypeDetail")
  public List<RiskTypeDetailDTO> getListRiskTypeDetail(
      @RequestBody RiskTypeDetailDTO riskTypeDetailDTO);

  @PostMapping("/RiskChangeStatus/getListRiskChangeStatusDTO")
  public List<RiskChangeStatusDTO> getListRiskChangeStatusDTO(
      @RequestBody RiskChangeStatusDTO riskChangeStatusDTO);

  @PostMapping("/RiskChangeStatus/getListRiskCfgBusinessDTO")
  public List<RiskCfgBusinessDTO> getListRiskCfgBusinessDTO(
      @RequestBody RiskCfgBusinessDTO riskCfgBusinessDTO);

  @PostMapping("/RiskChangeStatus/getListRiskChangeStatusRoleDTO")
  List<RiskChangeStatusRoleDTO> getListRiskChangeStatusRoleDTO(
      @RequestBody RiskChangeStatusRoleDTO riskChangeStatusRoleDTO);

}
