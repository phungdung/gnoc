package com.viettel.gnoc.incident.business;

import com.viettel.bccs2.CauseDTO;
import com.viettel.bccs2.CauseErrorExpireDTO;
import com.viettel.bccs2.TroubleNetworkSolutionDTO;
import com.viettel.gnoc.commons.dto.ConcaveDTO;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.incident.dto.CfgServerNocDTO;
import com.viettel.gnoc.incident.dto.CommonDTO;
import com.viettel.gnoc.incident.dto.TroubleActionLogsDTO;
import com.viettel.gnoc.incident.dto.TroubleWorklogInsiteDTO;
import com.viettel.gnoc.incident.dto.TroublesDTO;
import com.viettel.gnoc.incident.dto.TroublesInSideDTO;
import com.viettel.gnoc.wo.dto.WoHistoryDTO;
import java.util.List;

public interface TroublesServiceForCCBusiness {

  List<ResultDTO> onRollBackTroubleForCC(List<CommonDTO> lstComplaint);

  int onSearchCountForCC(TroublesDTO troublesDTO);

  List<TroublesDTO> onSearchForCC(TroublesDTO troublesDTO, Integer startRow, Integer pageLength);

//  String getListSolution(String insertSource);

  ResultDTO getTroubleInfoForCC(TroublesDTO troublesDTO);

  ResultDTO onInsertTroubleForCC(TroublesDTO troublesDTO);

  TroublesInSideDTO findTroublesById(Long id);

  List<String> getSequenseTroubles(String seqName, int[] size);

  ResultDTO reassignTicketForCC(TroublesDTO troublesDTO);

  List<TroubleActionLogsDTO> getListTroubleActionLog(String troubleCode);

  List<TroubleWorklogInsiteDTO> getListWorkLog(String troubleCode);

  String getConcaveByTicket(String troubleCode) throws Exception;

  ResultDTO sendTicketToTKTU(TroublesInSideDTO tForm) throws Exception;

  List<ConcaveDTO> getConcaveByCellAndLocation(List<String> lstCell, String lng, String lat)
      throws Exception;

  List<UnitDTO> getListUnitByTrouble(String troubleCode);

  ResultDTO onUpdateTroubleFromTKTU(TroublesInSideDTO troublesDTO) throws Exception;

  ResultDTO onUpdateTroubleFromWo(TroublesDTO troublesDTO) throws Exception;

  ResultDTO onUpdateTroubleCC(TroublesDTO tForm) throws Exception;

  List<WoHistoryDTO> getListWoLog(String troubleCode);

  List<CauseDTO> getCompCauseDTOForCC3(String parentId, String serviceTypeId, String probGroupId,
      CfgServerNocDTO nocDTO);

  List<CauseErrorExpireDTO> getCauseErrorExpireForCC3(String parentId, CfgServerNocDTO nocDTO);

  public List<TroubleNetworkSolutionDTO> getGroupSolution(CfgServerNocDTO nocDTO);
}
