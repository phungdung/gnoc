package com.viettel.gnoc.incident.repository;

import com.viettel.bccs.cc.service.ProblemGroupDTO;
import com.viettel.bccs.cc.service.ProblemTypeDTO;
import com.viettel.gnoc.commons.dto.*;
import com.viettel.gnoc.commons.model.UnitEntity;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.incident.dto.CatReasonDTO;
import com.viettel.gnoc.incident.dto.CfgServerNocDTO;
import com.viettel.gnoc.incident.dto.CfgTimeTroubleProcessDTO;
import com.viettel.gnoc.incident.dto.CommonFileDTO;
import com.viettel.gnoc.incident.dto.ItemDataTT;
import com.viettel.gnoc.incident.dto.TTChangeStatusDTO;
import com.viettel.gnoc.incident.dto.TroubleActionLogsDTO;
import com.viettel.gnoc.incident.dto.TroublesDTO;
import com.viettel.gnoc.incident.dto.TroublesInSideDTO;
import com.viettel.gnoc.incident.model.TroubleFileEntity;
import com.viettel.gnoc.incident.model.TroublesEntity;
import com.viettel.gnoc.wo.dto.WoInsideDTO;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface TroublesRepository {

  ResultInSideDto insertTroubles(TroublesEntity troublesEntity);

  String updateTroubles(TroublesEntity troublesEntity);

  Datatable onSearch(TroublesInSideDTO dto);

  List<TroublesInSideDTO> searchByConditionBean(
      List<ConditionBean> lstCondition, int rowStart, int maxRow, String sortType,
      String sortFieldList);

  List<String> getSequenseTroubles(String seqName, int... size);

  List<MapFlowTemplatesDTO> getMapFlowTemplate(String typeId, String alarmGroupId);

  CfgUnitTtSpmDTO getUnitByLocation(String locationId, String typeId, String typeUnit);

  CfgTimeTroubleProcessDTO getConfigTimeTroubleProcess(Long typeId, Long subCategoryId,
      Long priority, String country);

  String getLocationNameFull(String locationId);

  Map<String, String> getConfigProperty();

  Datatable onSearchTroubleRelated(TroublesInSideDTO troublesDTO);

  TroublesInSideDTO getTroubleDTO(String troubleId, String troubleCode, String spmCode,
      String amiId,
      String complaintId, String fromDate, String toDate);

  TroublesEntity findTroublesById(Long id);

  CfgServerNocDTO getServerDTO(CfgServerNocDTO cfgServerNocDTO);

  ResultInSideDto onUpdatebatchTrouble(TroublesInSideDTO tForm, String state) throws Exception;

  List<CatItemDTO> onSearchCountByState(TroublesInSideDTO dto);

  List<TroublesInSideDTO> getTroublesSearchExport(TroublesInSideDTO dto);

  ResultInSideDto delete(Long id);

  List<TroublesInSideDTO> getTroubleInfo(TroublesInSideDTO dto);

  List<TroublesDTO> onSynchTrouble(String fromDate, String toDate, String insertSource,
      String subCategoryCode, String tableCurrent);

  List<TroublesDTO> onSearchForSPM(TroublesDTO dto, String account, String spmCode,
      Long typeSearch);

  List<TroublesDTO> onSearchForVsmart(TroublesDTO dto, Integer startRow, Integer pageLength);

  int onSearchCountForVsmart(TroublesDTO dto);

  List<UnitEntity> getUnitByUnitDTO(UnitDTO unitId);

  List<TroublesDTO> countTroubleByStation(String stationCode, String startTime, String endTime,
      String priority, int type);

  Datatable searchParentTTForCR(TroublesInSideDTO troublesInSideDTO);

  List<TroublesInSideDTO> countTicketByShift(TroublesInSideDTO troublesDTO);

  List<TroublesDTO> getTroubleByCode(String troubleCode);

  List<TroubleStatisticForm> getStatisticTroubleDetail(String unitId, Boolean isCreateUnit,
      Boolean searchChild, String startTime, String endTime);

  List<GnocFileDto> getFileByTrouble(TroublesDTO trouble);

  ResultInSideDto insertCommonFile(CommonFileDTO commonFileDTO);

  ResultInSideDto insertTroubleFile(TroubleFileEntity entity);

  List<GnocFileDto> getTroubleFileDTO(GnocFileDto gnocFileDto);

  ResultInSideDto deleteTroubleFileByTroubleId(Long troubleId);

  Datatable getListFileAttachByTroubleId(GnocFileDto gnocFileDto);

  //duongnt start
  ResultInSideDto onInsertWorkLog(TroublesInSideDTO tForm) throws Exception;

  ResultInSideDto onUpdateTrouble(TroublesInSideDTO tForm);

  ResultInSideDto onInsertTroubleActionLogs(TroubleActionLogsDTO tForm);

  TroublesDTO checkAccountSPM(List<String> lstAccount, String insertSource);

  ResultInSideDto updateTroubleSpmVTNET(TroublesDTO troublesDTO);
  //duongnt end

  List<ProblemGroupDTO> getListProblemGroupParent(CfgServerNocDTO cfgServerNocDTO) throws Exception;

  List<ProblemGroupDTO> getListProblemGroupByParrenId(Long probGroupId,
      CfgServerNocDTO cfgServerNocDTO) throws Exception;

  List<ProblemTypeDTO> getListPobTypeByGroupId(Long probGroupId, CfgServerNocDTO cfgServerNocDTO)
      throws Exception;

  String getKedbByComplaint(String complaintId);
  //Dunglv start
  UsersDTO getUserByUserLogin(String userName);

  List<TroublesDTO> getTroubleInfoForVsSmart(TroublesDTO troublesDTO);

  TroublesDTO findTroublesDtoById(Long id);

  ResultDTO updateTroubleFromVSMART(TroublesDTO troublesDTO);

  UsersDTO getUnitIdByUserId(Long Id);

  List<ItemDataCRInside> getListLocationCombobox(Long parentId);

  ResultInSideDto insertTroublesHistory(TroubleActionLogsDTO troubleActionLogsDTO);

  TroubleActionLogsDTO getTroubleActionLogDTOByTroubleId(Long id);
  //dunglv end

  WoInsideDTO checkStationCodeTTForWo(Long id);

  // thangdt convert id -> name
  CatItemDTO convertIdToName(Long itemId, Long categoryId);

  // Thangdt get danh sach dia ban theo like
  List<DataItemDTO> getListDistrictByLocationName(String name);

  List<ItemDataTT> getTroubleReasonTreeById(String typeId,String id);

  // hungtv start add
  CatItemDTO getItemByCode(String category, String itemCode, String parentId);

  CatLocationDTO getLocationByCode(String code);

  UnitDTO getUnitByCode(String code, Boolean isUnitGnoc);

  CatItemDTO getPriorityTrouble(TroublesInSideDTO dto);
  // end

  List<ItemDataTT> getReasonByParentId(String parentId,String typeId);

  List<ItemDataTT> getStatusConfig(TTChangeStatusDTO ttChangeStatusDTO);

  List<TroublesDTO> countTroubleByCable(String lineCutCode, String startTime, String endTime,
      int type);

  List<CatItemDTO> getListUnitApproval();
}
