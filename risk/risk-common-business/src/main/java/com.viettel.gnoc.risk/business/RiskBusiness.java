package com.viettel.gnoc.risk.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.risk.dto.RiskCfgBusinessDTO;
import com.viettel.gnoc.risk.dto.RiskChangeStatusDTO;
import com.viettel.gnoc.risk.dto.RiskDTO;
import com.viettel.gnoc.risk.dto.RiskDTOSearch;
import com.viettel.gnoc.risk.dto.RiskHistoryDTO;
import com.viettel.gnoc.risk.dto.RiskRelationDTO;
import com.viettel.gnoc.risk.dto.RiskSystemDTO;
import com.viettel.gnoc.risk.dto.RiskTypeDTO;
import com.viettel.gnoc.risk.dto.RiskTypeDetailDTO;
import java.io.File;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface RiskBusiness {

  Datatable getListDataSearchWeb(RiskDTO riskDTO);

  ResultInSideDto insertRiskFromWeb(List<MultipartFile> fileAttacks, RiskDTO riskDTO)
      throws Exception;

  RiskDTO findRiskByIdFromWeb(Long riskId);

  List<GnocFileDto> getListFileFromRisk(Long riskId);

  Datatable getListRiskHistoryByRiskId(RiskHistoryDTO riskHistoryDTO);

  Datatable getListRiskRelationByRiskId(RiskRelationDTO riskRelationDTO);

  List<RiskSystemDTO> getListRiskSystemCombobox(RiskSystemDTO riskSystemDTO);

  List<RiskTypeDTO> getListRiskTypeCombobox(RiskTypeDTO riskTypeDTO);

  ResultInSideDto getValueFromValueKey(String configKey, String valueKey);

  UnitDTO findUnitById(Long unitId);

  List<RiskTypeDetailDTO> getListRiskTypeDetail(RiskTypeDetailDTO riskTypeDetailDTO);

  ResultInSideDto updateRiskFromWeb(List<MultipartFile> fileAttacks,
      List<MultipartFile> fileCfgAttacks, RiskDTO riskDTO) throws Exception;

  List<RiskChangeStatusDTO> getListRiskChangeStatus(RiskChangeStatusDTO riskChangeStatusDTO);

  List<RiskCfgBusinessDTO> getListRiskCfgBusiness(RiskCfgBusinessDTO riskCfgBusinessDTO);

  List<Long> getListStatusNext(RiskDTO riskDTO);

  Datatable getListRiskRelationBySystem(RiskRelationDTO riskRelationDTO);

  File exportDataRisk(RiskDTO riskDTO) throws Exception;

  List<GnocFileDto> getFileByBusinessId(Long businessId);

  RiskHistoryDTO findRiskHistoryByIdFromWeb(RiskHistoryDTO riskHistoryDTO);

  List<RiskDTOSearch> getListDataSearchForOther(RiskDTOSearch riskDTOSearch, int rowStart, int maxRow, String sortType, String sortFieldList);

  File onDownloadMultipleFile(RiskDTO riskDTO);

  List<RiskDTO> getListDataSearchByOther(RiskDTO riskDTO);

  ResultInSideDto updateRiskOtherSystem(RiskDTO riskDTO);
}
