package com.viettel.gnoc.risk.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.risk.dto.RiskCfgBusinessDTO;
import com.viettel.gnoc.risk.dto.RiskChangeStatusDTO;
import com.viettel.gnoc.risk.dto.RiskChangeStatusRoleDTO;
import com.viettel.gnoc.risk.dto.RiskTypeDTO;
import java.io.File;
import java.io.IOException;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface RiskChangeStatusBusiness {

  Datatable getDataRiskChangeStatusSearchWeb(RiskChangeStatusDTO riskChangeStatusDTO);

  List<RiskChangeStatusDTO> getListRiskChangeStatusDTO(RiskChangeStatusDTO riskChangeStatusDTO);

  List<RiskCfgBusinessDTO> getListRiskCfgBusinessDTO(RiskCfgBusinessDTO riskCfgBusinessDTO);

  List<RiskChangeStatusRoleDTO> getListRiskChangeStatusRoleDTO(
      RiskChangeStatusRoleDTO riskChangeStatusRoleDTO);

  ResultInSideDto insertOrUpdateRiskChangeStatus(RiskChangeStatusDTO riskChangeStatusDTO,
      List<MultipartFile> lstMultipartFile) throws IOException;

  RiskChangeStatusDTO findRiskChangeStatusByIdFromWeb(Long id);

  File exportDataRiskChangeStatus(RiskChangeStatusDTO riskChangeStatusDTO) throws Exception;

  List<RiskTypeDTO> getListRiskTypeDTOCombobox(RiskTypeDTO riskTypeDTO);

  ResultInSideDto deleteRiskChangeStatus(Long id);
}
