package com.viettel.gnoc.incident.repository;

import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.incident.dto.TroubleActionLogsDTO;
import com.viettel.gnoc.incident.dto.TroubleWorklogInsiteDTO;
import com.viettel.gnoc.incident.dto.TroublesDTO;
import com.viettel.gnoc.incident.dto.TroublesInSideDTO;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface TroublesServiceForCCRepository {

  List<TroublesInSideDTO> getTroubleDTOForRollback(List<String> lstTroubleCode, String complaintId,
      String fromDate, String toDate);

  int onSearchCountForCC(TroublesDTO troublesDTO);

  List<TroublesDTO> onSearchForCC(TroublesDTO troublesDTO, Integer startRow, Integer pageLength);

  List<TroublesDTO> getTroubleInfo(TroublesDTO troublesDTO);

  Map<String, String> getConfigProperty();

  List<TroubleActionLogsDTO> getListTroubleActionLog(String troubleCode);

  List<TroubleWorklogInsiteDTO> searchByConditionBean(List<ConditionBean> lstCondition,
      int rowStart,
      int maxRow, String sortType, String sortFieldList);

  List<UnitDTO> getListUnitByTrouble(String troubleCode);
}
