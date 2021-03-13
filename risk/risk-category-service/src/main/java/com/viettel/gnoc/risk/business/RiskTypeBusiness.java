package com.viettel.gnoc.risk.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.risk.dto.RiskTypeDTO;
import com.viettel.gnoc.risk.dto.RiskTypeDetailDTO;
import java.io.File;
import java.io.IOException;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface RiskTypeBusiness {

  Datatable getDataRiskTypeSearchWeb(RiskTypeDTO riskTypeDTO);

  List<RiskTypeDTO> getListRiskTypeDTO(RiskTypeDTO riskTypeDTO);

  List<RiskTypeDetailDTO> getListRiskTypeDetail(RiskTypeDetailDTO riskTypeDetailDTO);

  RiskTypeDTO findRiskTypeByIdFromWeb(Long riskTypeId);

  ResultInSideDto insertOrUpdateRiskType(RiskTypeDTO riskTypeDTO);

  ResultInSideDto delete(Long riskTypeId);

  File exportDataRiskType(RiskTypeDTO riskTypeDTO) throws Exception;

  File getTemplateImport() throws IOException;

  ResultInSideDto importDataRiskType(MultipartFile fileImport);
}
