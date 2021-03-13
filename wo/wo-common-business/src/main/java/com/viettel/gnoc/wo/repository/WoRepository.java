package com.viettel.gnoc.wo.repository;

import com.viettel.cc.webserivce.CompCause;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.MessagesDTO;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.commons.dto.WoSearchWebDTO;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.wo.dto.*;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface WoRepository {

  Datatable getListDataSearchWeb(WoInsideDTO searchDto);

  List<WoInsideDTO> getListDataSearchWoDTO(WoInsideDTO searchDto);

  List<WoInsideDTO> getListDataSearch1(WoInsideDTO searchDto);

  WoInsideDTO findWoById(Long woId, Double offSetFromUser);

  WoInsideDTO findWoByIdNoWait(Long woId) throws Exception;

  WoInsideDTO findWoByWoCodeNoWait(String woCode) throws Exception;

  WoInsideDTO findWoByIdNoOffset(Long woId);

  ResultInSideDto insertWo(WoInsideDTO woInsideDTO);

  ResultInSideDto updateWo(WoInsideDTO woInsideDTO);

  String getSeqTableWo(String seq);

  List<String> getListSequenseWo(String seq, int size);

  ResultInSideDto deleteWo(Long woId);

  List<CatItemDTO> getListWoSystemInsertWeb();

  WoInsideDTO getListFileFromWo(Long woId);

  CatLocationDTO getCatLocationById(Long locationId);

  List<CatItemDTO> getListWoKttsAction(String key);

  List<WoInsideDTO> getListWoExport(WoInsideDTO woInsideDTO);

  List<WoInsideDTO> getListChildAccept(Long parentId);

  List<WoInsideDTO> getListWoBySystemOtherCode(String code, String system);

  CompCause getCompCause(Long compCauseId);

  WoInsideDTO getWoByWoCode(String woCode);

  List<UsersInsideDto> getUserOfCD(Long cdId);

  List<MessagesDTO> getMessagesForFT(WoInsideDTO woInsideDTO, String content, Date date);

  List<MessagesDTO> getMessagesForCd(WoInsideDTO woInsideDTO, String content, Date date);

  Object updateObjectData(Object objSrc, Object objDes);

  UnitDTO getUnitCodeMapNims(String unitNimsCode, String businessName);

  Long getCdByUnitId(Long unitId, Long type);

  Long getCdByUnitId(Long unitId, Long type, Long woTypeId);

  WoCdGroupInsideDTO getCdByFT(Long ftId, Long woTypeId, String cdGroupType);

  String getStationFollowNode(String node, String nationCode);

  WoCdGroupInsideDTO getCdByUnitCode(String unitCode, Long woTypeId, String cdGroupType);

  Long checkCloseWoPostInspection(Long woId, Long numRecheck);

  List<WoPriorityDTO> getWoPriorityByWoTypeID(Long woTypeId);

  Datatable getListWoChild(WoInsideDTO searchDto);

  List<WoInsideDTO> getListWoDTO(WoInsideDTO woInsideDTO, int rowStart, int maxRow, String sortType,
      String sortFieldList);

  List<WoInsideDTO> getListWoDTOByWoSystemId(String woSystemId);

  List<WoInsideDTO> getListWoByWoTypeId(Long woTypeId);

  List<WoCdGroupInsideDTO> getListCdByLocation(String locationCode);

  List<WoMaterialDeducteInsideDTO> getListMaterial(
      WoMaterialDeducteInsideDTO woMaterialDeducteInsideDTO);

  List<CountWoForVSmartForm> getCountWoForVSmart(CountWoForVSmartForm countWoForVSmartForm);

  WoSalaryResponse countWOByFT(WoSalaryResponse woSalaryResponse);

  WoHistoryInsideDTO getWoHisFinalClose(Long woId);

  Integer getCountWOByUsers(WoDTOSearch woDTO);

  Long getPriorityHot(String woTypeCdbr, String hot);

  List<SearchWoKpiCDBRForm> searchWoKpiCDBR(String startTime, String endTime);

  List<String> getSequenseWo(String seqName, int size);

  List<WoDTO> getListWoByListAccount(List<String> lstAccount, Long numDate);

  List<WoDTOSearch> getListWoByWoType(String woTypeCode, String createTimeFrom,
      String createTimeTo);

  List<WoDTOSearch> getListWOByUsers(String username, String summaryStatus, Integer isDetail,
      WoDTOSearch woDTO, int start, int count, int i);

  List<KpiCompleteVsamrtForm> getListKpiComplete(List<String> lstUser, Date start, Date end);

  List<WoChecklistDTO> getListWoChecklistDetailByWoId(String woId);

  List<ResultDTO> getWOSummaryInfobyUser(String username, int typeSearch, Long cdId,
      String createTimeFrom, String createTimeTo);

  WoInsideDTO getWoByAmiOneId(String amiOneId);

  List<WoDTO> getListWOAndAccount(String username, String fromDate, String toDate, String woCode,
      String cdId, String accountIsdn);

  WoTypeServiceInsideDTO getTypeService(Long woTypeId, Long serviceId);

  String getProDuctCodeNotCheckQrCode();

  WoInsideDTO getWoByWoSystemCode(String woSystemId);

  List<WoInsideDTO> getListWoByWoCode(List<String> lstWoCode);

  List<WoInsideDTO> getFullWoByWoSystemCode(String woSystemId);

  List<WoDTOSearch> getListDataSearch(WoDTOSearch searchDto, Boolean isCount);

  List<ResultDTO> getWOStatistic(Long unitId, int isSend, int isSearchChild, String fromDate,
      String toDate) throws Exception;

  Integer getWOTotal(WoDTOSearch searchDtoInput);

  List<WoInsideDTO> getListWoChildByParentId(Long parentId);

  List<WoInsideDTO> getListDataForRisk(WoInsideDTO searchDto);

  List<Long> getListWoDTOByUserId(Long userId);

  List<ResultDTO> getWOSummaryInfobyUser2(String username, int typeSearch, Long cdId,
      String createTimeFrom, String createTimeTo);

  WoSearchWebDTO getWoSearchWebDTOByWoCode(String code);

  List<WoDTO> getListSearchWOByCondition(List<ConditionBean> lstCondition,
      int rowStart, int maxRow, String sortType, String sortFieldList);

  WoInsideDTO findWoByWoCodeNoOffset(String woCode);

  List<MaterialThresDTO> getListMaterialByWoId(Long woId);

  public List<ResultDTO> getWOSummaryInfobyType(String username, int typeSearch, Long cdId, WoDTOSearch woDTOInput);

  //hungtv bo sung phe duyet gia han wo start
  WoConfigPropertyDTO getTimeApproveExtend (String key);
  ////hungtv bo sung phe duyet gia han wo end

  List<WoHisForAccountDTO> getListWoHisForAccount(List<String> lstAcc);

  Datatable getListConfigAutoCreateWoOs(AutoCreateWoOsDTO autoCreateWoOsDTO);

  ResultInSideDto insertOrUpdateAutoCreateWoOs(AutoCreateWoOsDTO autoCreateWoOsDTO);

  AutoCreateWoOsDTO getConfigById(Long id);

  ResultInSideDto delete(Long id);

  ResultInSideDto syncFileFromWeb(Long woId);
}
