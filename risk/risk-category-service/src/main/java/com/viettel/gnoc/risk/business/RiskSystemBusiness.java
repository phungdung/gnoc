package com.viettel.gnoc.risk.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.risk.dto.RiskSystemDTO;
import com.viettel.gnoc.risk.dto.RiskSystemHistoryDTO;
import java.io.File;
import java.io.IOException;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface RiskSystemBusiness {

  Datatable getDataRiskSystemSearchWeb(RiskSystemDTO riskSystemDTO);

  List<RiskSystemDTO> getListRiskSystem(RiskSystemDTO riskSystemDTO);

  ResultInSideDto insertRiskSystemWeb(List<MultipartFile> fileAttacks, RiskSystemDTO riskSystemDTO)
      throws Exception;

  ResultInSideDto updateRiskSystemWeb(List<MultipartFile> fileAttacks, RiskSystemDTO riskSystemDTO)
      throws Exception;

  ResultInSideDto deleteRiskSystem(Long id);

  RiskSystemDTO findRiskSystemByIdFromWeb(Long id);

  List<GnocFileDto> getListFileFromRiskSystem(Long systemId);

  Datatable getListRiskSystemHistoryBySystemId(RiskSystemHistoryDTO riskSystemHistoryDTO);

  File exportDataRiskSystem(RiskSystemDTO riskSystemDTO) throws Exception;

  File getTemplateImport() throws IOException;

  ResultInSideDto importDataRiskSystem(MultipartFile fileImport);

  File getTemplateImportSystemDetail() throws IOException;

  ResultInSideDto importDataSystemDetail(MultipartFile fileImport) throws Exception;
}
