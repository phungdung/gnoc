package com.viettel.gnoc.risk.repository;

import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.risk.dto.RiskDTO;
import com.viettel.gnoc.risk.dto.RiskDTOSearch;
import com.viettel.gnoc.risk.model.RiskEntity;

import java.io.File;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface RiskRepository {

  Datatable getListDataSearchWeb(RiskDTO riskDTO);

  String getSeqTableWo(String seq);

  ResultInSideDto insertRisk(RiskDTO riskDTO);

  RiskDTO findRiskByIdFromWeb(Long riskId, Double offsetFromUser);

  String getValueFromValueKey(String configKey, String valueKey);

  ResultInSideDto updateRisk(RiskDTO riskDTO);

  RiskEntity findRiskByRiskId(Long riskId);

  List<RiskDTO> getListRiskExport(RiskDTO riskDTO);

  List<RiskDTOSearch> getListDataSearchForOther(RiskDTOSearch searchDtoInput, int start, int count);

  CatItemDTO getDoNextAction(String itemId);

  String getValueFromDesKey(String configKey, String desKey);

  File onDownloadMultipleFile(RiskDTO riskDTO);

  ResultInSideDto updateRiskOtherSystem(RiskDTO riskDTO);


}
