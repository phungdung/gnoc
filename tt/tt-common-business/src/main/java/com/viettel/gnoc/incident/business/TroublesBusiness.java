package com.viettel.gnoc.incident.business;

import com.viettel.bccs.cc.service.ProblemGroupDTO;
import com.viettel.bccs.cc.service.ProblemTypeDTO;
import com.viettel.gnoc.commons.dto.*;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.cr.dto.CrSearchDTO;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.incident.dto.*;
import com.viettel.gnoc.kedb.dto.AuthorityDTO;
import com.viettel.gnoc.od.dto.OdSearchInsideDTO;
import com.viettel.gnoc.risk.dto.RiskDTO;
import com.viettel.gnoc.wo.dto.WoDTOSearch;
import com.viettel.gnoc.wo.dto.WoInsideDTO;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.springframework.web.multipart.MultipartFile;

public interface TroublesBusiness {

  List<TroublesInSideDTO> getTroubleInfo(TroublesInSideDTO dto);

  ResultDTO updateTroublesNOC(AuthorityDTO requestDTO, List<TroublesDTO> listTrouble);

  ResultDTO onSynchTrouble(AuthorityDTO requestDTO, String fromDate, String toDate,
      String insertSource, String subCategoryCode, String tableCurrent);

  List<TroublesDTO> onSearchForSPM(TroublesDTO troublesDTO, String account, String spmCode,
      Long typeSearch);

  ResultDTO onUpdateTroubleSPM(AuthorityDTO requestDTO, TroublesDTO troublesDTO);

  List<ResultDTO> onRollBackTroubleSPM(AuthorityDTO requestDTO, List<String> lstSpmCode);

  List<TroublesDTO> onSearchForVsmart(TroublesDTO dto, Integer startRow, Integer pageLength);

  int onSearchCountForVsmart(TroublesDTO troublesDTO);

  ResultDTO onInsertTroubleMobile(AuthorityDTO requestDTO, TroublesDTO troublesDTO,
      List<String> listAccount,
      String[] arrFileName, byte[][] arrFileData);

  ResultDTO onInsertTroubleFileWS(AuthorityDTO requestDTO, TroublesDTO troublesDTO,
      List<String> listAccount, String[] arrFileName, byte[][] arrFileData);

  Datatable onSearch(TroublesInSideDTO dto);

  List<TroublesInSideDTO> searchByConditionBean(
      List<ConditionBean> lstCondition, int rowStart, int maxRow, String sortType,
      String sortFieldList);

  List<String> getSequenseTroubles(String seqName, int... size);

  ResultDTO insertTroublesNOC(AuthorityDTO requestDTO, TroublesDTO troublesDTO,
      List<String> listAccount) throws Exception;

  Datatable onSearchTroubleRelated(TroublesInSideDTO troublesDTO);

  ResultInSideDto onUpdateTrouble(TroublesInSideDTO tForm, boolean callFromTKTU) throws Exception;

  ResultInSideDto updateTrouble(TroublesInSideDTO tForm);

  ResultInSideDto updateTroubleToSPM(TroublesInSideDTO troublesDTO, String type);

  TroublesInSideDTO findTroublesById(Long id) throws Exception;

  ResultInSideDto sendChatListUsers(TroublesInSideDTO dtoTran) throws Exception;

  ResultInSideDto callIPCC(TroublesInSideDTO troublesDTO) throws Exception;

  ResultInSideDto insertTroublesTT(AuthorityDTO requestDTO, List<MultipartFile> files,
      TroublesInSideDTO troublesDTO, List<String> listAccount) throws Exception;

  ResultInSideDto onUpdateTroubleTT(TroublesInSideDTO tForm, List<MultipartFile> multipartFileList)
      throws Exception;

  ResultInSideDto deleteTrouble(Long id);

  Datatable viewCall(TroublesInSideDTO dtoTran);

  List<UsersInsideDto> getListChatUsers(TroublesInSideDTO troublesDTO);

  List<CatItemDTO> onSearchCountByState(TroublesInSideDTO dto);

  Map<String, String> getConfigProperty();

  ResultInSideDto getListReasonBCCS(TroublesInSideDTO dto, Long parentId, int level);

  List getListReasonOverdue(TroublesInSideDTO troubleDTO, Long parentId);

  List getListGroupSolution(TroublesInSideDTO troubleDTO) throws Exception;

  List<CatItemDTO> getLstNetworkLevel(String typeId);

  File getListTroubleSearchExport(TroublesInSideDTO dto) throws Exception;

  Datatable searchCrRelated(CrSearchDTO crSearchDTO);

  Datatable loadTroubleCrDTO(TroublesInSideDTO dto);

  Datatable loadUserSupportGroup(TroublesInSideDTO troublesDTO);

  List<CrSearchDTO> loadCrRelatedDetail(String crRelatedCode);

  ResultInSideDto sendTicketToTKTU(TroublesInSideDTO dto) throws Exception;

  void insertCrCreatedFromOtherSystem(TroublesInSideDTO troublesDTO);

  ResultInSideDto getAlarmClearGNOC(TroublesInSideDTO troubleDTO) throws Exception;

  Datatable getDataOfTabWO(TroublesInSideDTO troublesDTO);

  String checkWoRequiredClosed(TroublesInSideDTO troubleDTO);

  List<WoDTOSearch> getListDataSearchWo(WoDTOSearch woDTOSearch);

  List<TroublesDTO> countTroubleByStation(String stationCode, String startTime, String endTime,
      String priority, int type);

  Datatable searchParentTTForCR(TroublesInSideDTO troublesInSideDTO);

  List<TroublesInSideDTO> countTicketByShift(TroublesInSideDTO troublesDTO);

  ResultDTO onUpdateTroubleMobile(TroublesDTO troublesDTO) throws Exception;

  ResultDTO onClosetroubleFromWo(TroublesDTO troublesDTO);

  ResultDTO checkAlarmNOC(String troubleCode, String typeWo);

  List<TroublesDTO> getTroubleByCode(String troubleCode);

  List<TroubleStatisticForm> getStatisticTroubleTotal(String unitId, Boolean isCreateUnit,
      Boolean searchChild, String startTime, String endTime);

  List<TroublesDTO> getInfoTicketForAMI(TroublesDTO troublesDTO);

  Datatable getListFileAttachByTroubleId(GnocFileDto gnocFileDto);

  ResultInSideDto insertTroubleFilesUpload(List<MultipartFile> files, TroublesInSideDTO troublesDTO)
      throws IOException;

  ResultDTO updateReasonTroubleFromNOC(TroublesDTO tForm);

  ResultInSideDto updateTroubleSpmVTNET(TroublesDTO troublesDTO);

  TroublesDTO checkAccountSPM(List<String> lstAccount, String insertSource);

  List<ProblemGroupDTO> getListProblemGroupParent(CfgServerNocDTO cfgServerNocDTO) throws Exception;

  List<ProblemGroupDTO> getListProblemGroupByParrenId(Long probGroupId,
      CfgServerNocDTO cfgServerNocDTO) throws Exception;

  List<ProblemTypeDTO> getListPobTypeByGroupId(Long probGroupId, CfgServerNocDTO cfgServerNocDTO)
      throws Exception;

  List<String> getListActionInfo(ActionInfoDTO actionInfoDTO);

  //Dunglv start
  UsersDTO getUserByUserLogin(String userId);

  List<TroublesDTO> getTroubleInfoForVsSmart(TroublesDTO troublesDTO);

  TroublesDTO findTroublesDtoById(Long id);

  ResultDTO updateTroubleFromVSMART(TroublesDTO troublesDTO, boolean check);

  List<ItemDataCRInside> getListLocationCombobox(Long parentId);

  TroubleActionLogsDTO getTroubleActionLogDTOByTroubleId(Long troubleId);

  //add tab OD
  List<OdSearchInsideDTO> findListOdByTt(Long troubleId);

  //add tab RISK
  List<RiskDTO> findListRiskByTt(Long troubleId);

  //thangdt
  WoInsideDTO checkStationCodeTTForWo(Long id);

  // Thangdt get danh sach dia ban theo like
  List<DataItemDTO> getListDistrictByLocationName(String name);

  // Thangdt searchOdRelated
  Datatable searchOdRelated(OdSearchInsideDTO dto);

  // Thangdt insertOdCreatedFromOtherSystem
  void insertOdCreatedFromOtherSystem(TroublesInSideDTO troublesDTO);

  // Thangdt searchRiskRelated
  Datatable searchRiskRelated(RiskDTO dto);

  // Thangdt insertRiskCreatedFromOtherSystem
  void insertRiskCreatedFromOtherSystem(TroublesInSideDTO troublesDTO);

  TroublesDTO getDetailTroublesById(Long troubleId);

  void updateFile(Long troubleId, String userLogin, List<String> listFileName,
      List<byte[]> fileArr);

  List<ItemDataTT> getTroubleReasonTreeById(String typeId, String id);

  ResultDTO insertTroubleFromOtherSystem(TroublesInSideDTO troublesDTO,
      List<String> listAccount) throws Exception;

  List<ItemDataTT> getReasonByParentId(String parentId, String typeId);

  List<ItemDataTT> getStatusConfig(TTChangeStatusDTO ttChangeStatusDTO);

  List<TroublesDTO> countTroubleByCable(String lineCutCode, String startTime, String endTime,
      int type);

  List<CatItemDTO> getListUnitApproval();
}
