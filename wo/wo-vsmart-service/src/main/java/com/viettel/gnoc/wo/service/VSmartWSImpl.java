package com.viettel.gnoc.wo.service;

import com.viettel.cc.webserivce.CompCause;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.UsersDTO;
import com.viettel.gnoc.commons.model.Users;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.od.dto.ObjKeyValueVsmartDTO;
import com.viettel.gnoc.wo.business.MaterialThresBusiness;
import com.viettel.gnoc.wo.business.WoBusiness;
import com.viettel.gnoc.wo.business.WoVSmartBusiness;
import com.viettel.gnoc.wo.dto.CdInfoForm;
import com.viettel.gnoc.wo.dto.CfgWoTickHelpDTO;
import com.viettel.gnoc.wo.dto.CountWoForVSmartForm;
import com.viettel.gnoc.wo.dto.KpiCompleteVsmartResult;
import com.viettel.gnoc.wo.dto.MaterialThresDTO;
import com.viettel.gnoc.wo.dto.ObjFile;
import com.viettel.gnoc.wo.dto.ObjKeyValue;
import com.viettel.gnoc.wo.dto.SupportCaseForm;
import com.viettel.gnoc.wo.dto.VsmartUpdateForm;
import com.viettel.gnoc.wo.dto.WoCdGroupDTO;
import com.viettel.gnoc.wo.dto.WoCdGroupInsideDTO;
import com.viettel.gnoc.wo.dto.WoChecklistDTO;
import com.viettel.gnoc.wo.dto.WoDTO;
import com.viettel.gnoc.wo.dto.WoDTOSearch;
import com.viettel.gnoc.wo.dto.WoHisForAccountDTO;
import com.viettel.gnoc.wo.dto.WoInsideDTO;
import com.viettel.gnoc.wo.dto.WoMaterialDeducteDTO;
import com.viettel.gnoc.wo.dto.WoMaterialDeducteInsideDTO;
import com.viettel.gnoc.wo.dto.WoMerchandiseDTO;
import com.viettel.gnoc.wo.dto.WoPostInspectionDTO;
import com.viettel.gnoc.wo.dto.WoPostInspectionInsideDTO;
import com.viettel.gnoc.wo.dto.WoSalaryResponse;
import com.viettel.gnoc.wo.dto.WoTypeServiceDTO;
import com.viettel.gnoc.wo.dto.WoWorklogDTO;
import com.viettel.gnoc.wo.dto.WoWorklogInsideDTO;
import com.viettel.nocproV4.RequestInputBO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.cxf.headers.Header;
import org.apache.cxf.jaxws.context.WrappedMessageContext;
import org.apache.cxf.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Element;

@Service
@Slf4j
public class VSmartWSImpl implements VSmartWS {

  @Resource
  private WebServiceContext wsContext;

  @Autowired
  protected WoVSmartBusiness woVSmartBusiness;

  @Autowired
  protected WoBusiness woBusiness;

  @Autowired
  MaterialThresBusiness materialThresBusiness;

  @Override
  public WoDTO findWoById(Long woId) {
    I18n.setLocaleForService(wsContext);
    if (woId != null && woId > 0) {
      return woVSmartBusiness.findWoById(woId);
    }
    return null;
  }

  @Override
  public String insertWOPostInspectionFromVsmart(List<WoPostInspectionDTO> lstInspectionDTO,
      List<ObjKeyValue> lstObjKeyValue) {
    I18n.setLocaleForService(wsContext);
    return woVSmartBusiness
        .insertWOPostInspectionFromVsmart(lstInspectionDTO, lstObjKeyValue);
  }

  @Override
  public ResultDTO cancelReqBccs(String woCode, String content) {
    I18n.setLocaleForService(wsContext);
    return woVSmartBusiness.cancelReqBccs(woCode, content);
  }

  @Override
  public ResultDTO updateMopInfo(String woCode, String result, String mopId, Long type) {
    I18n.setLocaleForService(wsContext);
    return woVSmartBusiness.updateMopInfo(woCode, result, mopId, type);
  }

  @Override
  public List<WoPostInspectionDTO> getListWOPostInspection(WoPostInspectionDTO inspectionDTO,
      int rowStart,
      int maxRow, String sortType, String sortFieldList) {
    I18n.setLocaleForService(wsContext);
    return woVSmartBusiness
        .getListWOPostInspection(inspectionDTO, rowStart, maxRow, sortType, sortFieldList);
  }

  @Override
  public ResultDTO dispatchWo(String username, String ftName, String woId, String comment) {
    I18n.setLocaleForService(wsContext);
    return woVSmartBusiness.dispatchWo(username, ftName, woId, comment);
  }

  @Override
  public List<WoWorklogDTO> getListWorklogByWoId(String woId) {
    return woVSmartBusiness.getListWorklogByWoId(woId);
  }

  @Override
  public List<CompCause> getListCompCause3Level(String ccResult) {
    I18n.setLocaleForService(wsContext);
    return woVSmartBusiness.getListCompCause3Level(ccResult);
  }

  @Override
  public List<ResultDTO> createListWo(List<WoDTO> createWoDto) {
    I18n.setLocaleForService(wsContext);
    return woVSmartBusiness.createListWo(createWoDto);
  }

  @Override
  public ResultDTO updateWOPostInspection(WoPostInspectionDTO inspectionDTO, String userName) {
    I18n.setLocaleForService(wsContext);
    inspectionDTO.setUserName(userName);
    return woVSmartBusiness.updateWOPostInspection(inspectionDTO);
  }

  @Override
  public ResultDTO createWo(WoDTO createWoDto) {
    I18n.setLocaleForService(wsContext);
    return woVSmartBusiness.createWo(createWoDto);
  }

  @Override
  public ResultDTO approveWoVsmart(VsmartUpdateForm updateForm, String username, String woId,
      String comment, String action, String ftName) {
    I18n.setLocaleForService(wsContext);
    try {
//      I18n.setLocaleForService(wsContext);
      return woVSmartBusiness.approveWoVsmart(updateForm, username, woId, comment, action, ftName);
    } catch (Exception e) {
      log.info(e.getMessage(), e);
      return new ResultDTO("NOK", e.getMessage());
    }

  }

  @Override
  public ResultDTO insertWoKTTS(WoDTO woDTO) {
    I18n.setLocaleForService(wsContext);
    return woVSmartBusiness.insertWoKTTS(woDTO);
  }

  @Override
  public List<Users> getListUserByUnitCode(String unitCode, String allOfChildUnit) {
    I18n.setLocaleForService(wsContext);
    return woVSmartBusiness.getListUserByUnitCode(unitCode, allOfChildUnit);
  }

  @Override
  public List<WoMerchandiseDTO> getListWoMerchandise(String woId) {
    I18n.setLocaleForService(wsContext);
    return woVSmartBusiness.getListWoMerchandise(woId);
  }

  @Override
  public KpiCompleteVsmartResult getKpiComplete(String startTime, String endTime,
      List<String> lstUser) {
    I18n.setLocaleForService(wsContext);
    return woVSmartBusiness.getKpiComplete(startTime, endTime, lstUser);
  }

  @Override
  public List<WoChecklistDTO> getListWoChecklistDetailByWoId(String woId) {
    I18n.setLocaleForService(wsContext);
    return woVSmartBusiness.getListWoChecklistDetailByWoId(woId);
  }

  @Override
  public Users getUserInfo(String userName, String staffCode) {
    I18n.setLocaleForService(wsContext);
    return woVSmartBusiness.getUserModelInfo(userName, staffCode);
  }

  @Override
  public List<ObjFile> getFileFromWo(String woId, List<String> lstFileName) {
    I18n.setLocaleForService(wsContext);
    return woVSmartBusiness.getFileFromWo(woId, lstFileName);
  }

  @Override
  public ResultDTO insertCfgWoTickHelp(CfgWoTickHelpDTO cfgWoTickHelpDTO) {
    I18n.setLocaleForService(wsContext);
    return woVSmartBusiness.insertCfgWoTickHelp(cfgWoTickHelpDTO);
  }

  @Override
  public String insertWOPostInspection(List<WoPostInspectionDTO> lstInspectionDTO) {
    I18n.setLocaleForService(wsContext);
    return woVSmartBusiness.insertWOPostInspection(lstInspectionDTO);
  }

  @Override
  public List<ObjKeyValue> loadWoPostInspectionChecklist(String woId, String accountName) {
    I18n.setLocaleForService(wsContext);
    return woVSmartBusiness.loadWoPostInspectionChecklist(woId, accountName);
  }

  @Override
  public List<UsersDTO> getListFtByUser(String userId, String keyword, int rowStart,
      int maxRow) {
    I18n.setLocaleForService(wsContext);
    return woBusiness.getListFtByUser(userId, keyword, rowStart, maxRow);
  }

  @Override
  public ResultDTO rejectWo(String username, String woId, String comment, Boolean isFt) {
    I18n.setLocaleForService(wsContext);
    return woVSmartBusiness.rejectWo(username, woId, comment, isFt);
  }

  @Override
  public String deleteWoPostInspection(Long id) {
    I18n.setLocaleForService(wsContext);
    return woVSmartBusiness.deleteWoPost(id);
  }

  @Override
  public ResultDTO updateStatus(VsmartUpdateForm updateForm, String username, String woId,
      String status, String comment, String ccResult, String qrCode,
      List<WoMaterialDeducteDTO> listMaterial, Long reasonIdLv1, Long reasonIdLv2, Long actionKTTS,
      List<WoMerchandiseDTO> lstMerchandise, String reasonKtts, String handoverUser,
      List<String> listFileName, List<byte[]> fileArr) {
    I18n.setLocaleForService(wsContext);
    String sessionId = "", ipPortParentNode = "";
    try {
      MessageContext mc = wsContext.getMessageContext();
      HttpServletRequest req = (HttpServletRequest) mc.get(MessageContext.SERVLET_REQUEST);
      if (req.getAttribute("sessionId") == null) {
        sessionId = (String) req.getAttribute("sessionId");
      }
      String ip;
      String portServer;
//            if (null != req) {
      ip = req.getLocalAddr();
      portServer = String.valueOf(req.getLocalPort());
//            }
      ipPortParentNode = ip + ":" + portServer;

    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return woVSmartBusiness.updateStatus(updateForm, username, woId, status, comment, ccResult,
        qrCode, listMaterial, reasonIdLv1, reasonIdLv2, actionKTTS,
        lstMerchandise, reasonKtts, handoverUser, sessionId, ipPortParentNode, listFileName,
        fileArr);
  }

  @Override
  public List<ResultDTO> getWOSummaryInfobyUser(String userId, int typeSearch, Long cdId,
      String createTimeFrom, String createTimeTo) throws Exception {
    I18n.setLocaleForService(wsContext);
    if (StringUtils.isNotNullOrEmpty(createTimeFrom)) {
      try {
        DateTimeUtils.convertStringToTime1(createTimeFrom, "dd/MM/yyyy HH:mm:ss");
      } catch (Exception e) {
        log.error(e.getMessage(), e);
        throw new Exception(e.getMessage());
      }
    }
    if (StringUtils.isNotNullOrEmpty(createTimeTo)) {
      try {
        DateTimeUtils.convertStringToTime1(createTimeTo, "dd/MM/yyyy HH:mm:ss");
      } catch (Exception e) {
        log.error(e.getMessage(), e);
        throw new Exception(e.getMessage());
      }
    }
    return woVSmartBusiness
        .getWOSummaryInfobyUser(userId, typeSearch, cdId, createTimeFrom, createTimeTo);
  }

  @Override
  public ResultDTO checkDeviceCodeOfWo(String woCode, String deviceCode) {
    I18n.setLocaleForService(wsContext);
    WoInsideDTO woInsideDTO = new WoInsideDTO();
    woInsideDTO.setWoCode(woCode);
    woInsideDTO.setDeviceCode(deviceCode);
    return woVSmartBusiness.checkDeviceCodeOfWo(woInsideDTO);
  }

  @Override
  public ResultDTO confirmNotCreateAlarm(RequestInputBO inputBO, Long woId) {
    I18n.setLocaleForService(wsContext);
    try {
      return woVSmartBusiness.confirmNotCreateAlarm(inputBO, woId);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return new ResultDTO("NOK", "NOK", e.getMessage());
    }
  }

  @Override
  public ResultDTO updatePendingWo(String woCode, String endPendingTime, String user,
      String comment, String system) {
    I18n.setLocaleForService(wsContext);
//    ResultDTO resultDTO = new ResultDTO();
    try {
      WoInsideDTO woInsideDTO = new WoInsideDTO();
//      if (StringUtils.isStringNullOrEmpty(woCode)) {
//        resultDTO.setMessage(I18n.getLanguage("wo.WoCodeIsNotNull"));
//        resultDTO.setKey(RESULT.FAIL);
//        return resultDTO;
//      }
//      if (StringUtils.isStringNullOrEmpty(user)) {
//        resultDTO.setMessage("User is not null");
//        resultDTO.setKey(RESULT.FAIL);
//        return resultDTO;
//      }
//      if (StringUtils.isStringNullOrEmpty(comment)) {
//        resultDTO.setMessage("Comment is not null");
//        resultDTO.setKey(RESULT.FAIL);
//        return resultDTO;
//      }
//      if (StringUtils.isStringNullOrEmpty(comment)) {
//        resultDTO.setMessage("System is not null");
//        resultDTO.setKey(RESULT.FAIL);
//        return resultDTO;
//      }
//      if (StringUtils.isStringNullOrEmpty(endPendingTime)) {
//        resultDTO.setMessage("End Pending Time is not null");
//        resultDTO.setKey(RESULT.FAIL);
//        return resultDTO;
//      }
//      if (!"".equals(DataUtil.validateDateTimeDdMmYyyy_HhMmSs(endPendingTime))) {
//        resultDTO.setMessage("End Pending Time invalid");
//        resultDTO.setKey(RESULT.FAIL);
//        return resultDTO;
//      }
      woInsideDTO.setWoCode(woCode);
      woInsideDTO.setUser(user);
      woInsideDTO.setComment(comment);
      woInsideDTO.setSystem(system);
      woInsideDTO.setEndPendingTime((endPendingTime == null || !""
          .equals(DataUtil.validateDateTimeDdMmYyyy_HhMmSs(endPendingTime))) ? null :
          DateTimeUtils.convertStringToTime(endPendingTime, DateTimeUtils.patternDateTimeMs));
      return woVSmartBusiness.updatePendingWo(woInsideDTO, false);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return new ResultDTO(RESULT.FAIL, RESULT.FAIL, e.getMessage());
    }
  }

  @Override
  public ResultDTO updateWOPostInspectionFromVsmart(WoPostInspectionDTO inspectionDTO,
      String username, List<ObjKeyValue> lstObjKeyValue) {
    I18n.setLocaleForService(wsContext);
    if (inspectionDTO != null) {
      inspectionDTO.setUserName(username);
      inspectionDTO.setLstObjKeyValue(lstObjKeyValue);
    }
    return woVSmartBusiness.updateWOPostInspectionFromVsmart(inspectionDTO);
  }

  @Override
  public List<String> getSequenseWo(String string, int... size) {
    I18n.setLocaleForService(wsContext);
    int number = (size[0] > 0 ? size[0] : 1);
    return woVSmartBusiness.getListSequenseWo(string, number);
  }

  @Override
  public Integer getCountWOByUsers(String userId, String summaryStatus, WoDTOSearch wdtos,
      Integer isDetail, int typeSearch) {
    I18n.setLocaleForService(wsContext);
    if (wdtos != null) {
    } else {
      wdtos = new WoDTOSearch();
    }
    wdtos.setUsername(userId);
    wdtos.setSummaryStatus(summaryStatus);
    wdtos.setIsDetail(isDetail);
    wdtos.setTypeSearch(typeSearch);
    return woVSmartBusiness.getCountWOByUsers(wdtos);
  }

  @Override
  public List<CdInfoForm> getListCdInfo(List<String> lstWoCode, List<Long> lstWoId) {
    I18n.setLocaleForService(wsContext);
    CdInfoForm cdInfoForm = new CdInfoForm();
    cdInfoForm.setLstWoId(lstWoId);
    cdInfoForm.setLstWoCode(lstWoCode);
    return woVSmartBusiness.getListCdInfo(cdInfoForm);
  }

  @Override
  public WoSalaryResponse countWOByFT(List<String> username, String startTime, String endTime) {
    I18n.setLocaleForService(wsContext);
    WoSalaryResponse woSalaryResponse = new WoSalaryResponse();
    woSalaryResponse.setEndPeriod(endTime);
    woSalaryResponse.setStartPeriod(startTime);
    woSalaryResponse.setUserName(username);
    return woVSmartBusiness.countWOByFT(woSalaryResponse);
  }

  @Override
  public String deleteListWoPostInspection(List<WoPostInspectionDTO> woPostInspectionListDTO) {
    I18n.setLocaleForService(wsContext);
    ResultDTO res = woVSmartBusiness.deleteListWoPostInspection(woPostInspectionListDTO);
    return res != null ? res.getKey() : null;
  }

  @Override
  public int onSearchCount(WoPostInspectionDTO inspectionDTO) {
    I18n.setLocaleForService(wsContext);
    return woVSmartBusiness.onSearchCount(inspectionDTO);
  }

  @Override
  public ResultDTO checkUpdateSupportWO(String woId) {
    I18n.setLocaleForService(wsContext);
    ResultDTO res = new ResultDTO();
    try {
      res = woVSmartBusiness.checkUpdateSupportWO(Long.valueOf(woId));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      res.setKey(RESULT.FAIL);
      res.setId(RESULT.FAIL);
      res.setMessage(I18n.getLanguage("wo.haveSomeErrUpdateStatus"));
    }
    return res;
  }

  @Override
  public List<ObjKeyValueVsmartDTO> getDataCfgWoHelp(Long systemId, String typeId) {
    I18n.setLocaleForService(wsContext);
    return woVSmartBusiness.getDataCfgWoHelp(systemId, typeId);
  }

  @Override
  public VsmartUpdateForm getScriptId(String woId) {
    I18n.setLocaleForService(wsContext);
    VsmartUpdateForm result = new VsmartUpdateForm();
    if (!StringUtils.isStringNullOrEmpty(woId) && DataUtil.isNumber(woId)) {
      return woVSmartBusiness.getScriptId(Long.valueOf(woId));
    }
    result.setId(Constants.RESULT.SUCCESS);
    result.setKey(Constants.RESULT.SUCCESS);
    return result;
  }

  @Override
  public ResultDTO checkInfraForComplete(WoDTO woDTO) {
    I18n.setLocaleForService(wsContext);
    return woVSmartBusiness.checkInfraForComplete(woDTO);
  }

  @Override
  public ResultDTO acceptWo(String username, String woId, String comment, Boolean isFt) {
    I18n.setLocaleForService(wsContext);
    try {
      return woVSmartBusiness
          .acceptWo(username, StringUtils.isLong(woId) ? Long.valueOf(woId) : null, comment, isFt);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return new ResultDTO("NOK", RESULT.FAIL, e.getMessage());
    }
  }

  @Override
  public List<CountWoForVSmartForm> getCountWoForVSmart(String userName, String summaryStatus) {
    I18n.setLocaleForService(wsContext);
    CountWoForVSmartForm countWoForVSmartForm = new CountWoForVSmartForm();
    countWoForVSmartForm.setUserName(userName);
    countWoForVSmartForm.setSummaryStatus(summaryStatus);
    return woVSmartBusiness.getCountWoForVSmart(countWoForVSmartForm);
  }

  @Override
  public List<WoPostInspectionDTO> onSearch(WoPostInspectionDTO inspectionDTO, int startRow,
      int pageLength) {
    I18n.setLocaleForService(wsContext);
    List<WoPostInspectionInsideDTO> lst = woVSmartBusiness
        .onSearch(inspectionDTO, startRow, pageLength);
    if (lst != null && !lst.isEmpty()) {
      List<WoPostInspectionDTO> lstReturn = new ArrayList<>();
      for (WoPostInspectionInsideDTO item : lst) {
        WoPostInspectionDTO dto = item.toOutsideDto();
        if (dto != null) {
          lstReturn.add(dto);
        }
      }
      return lstReturn;
    }
    return null;
  }

  @Override
  public String insertListWoChecklistDetail(List<WoChecklistDTO> lstChecklist) {
    try {
      return woVSmartBusiness.insertListWoChecklistDetail(lstChecklist);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return RESULT.FAIL;
    }
  }

  @Override
  public List<SupportCaseForm> getLstSupportCase(String woId) {
    I18n.setLocaleForService(wsContext);
    if (!StringUtils.isStringNullOrEmpty(woId) && DataUtil.isNumber(woId)) {
      return woVSmartBusiness.getLstSupportCase(Long.valueOf(woId));
    }
    return null;
  }

  @Override
  public List<WoPostInspectionDTO> getListExistedWoPostInspection(String locationCode,
      String woType) {
    I18n.setLocaleForService(wsContext);
    WoPostInspectionDTO woPostInspectionDTO = new WoPostInspectionDTO();
    woPostInspectionDTO.setLocationCode(locationCode);
    woPostInspectionDTO.setWoType(woType);
    List<WoPostInspectionInsideDTO> lst = woVSmartBusiness
        .getListExistedWoPostInspection(woPostInspectionDTO.toInsideDto());
    if (lst != null && !lst.isEmpty()) {
      List<WoPostInspectionDTO> lstReturn = new ArrayList<>();
      for (WoPostInspectionInsideDTO item : lst) {
        WoPostInspectionDTO dto = item.toOutsideDto();
        if (dto != null) {
          lstReturn.add(dto);
        }
      }
      return lstReturn;
    }
    return null;
  }

  @Override
  public List<WoMaterialDeducteDTO> getListMaterial(Long woId, String userName) {
    I18n.setLocaleForService(wsContext);
    WoMaterialDeducteInsideDTO woMaterialDeducteInsideDTO = new WoMaterialDeducteInsideDTO();
    woMaterialDeducteInsideDTO.setWoId(woId);
    woMaterialDeducteInsideDTO.setUserName(userName);
    List<WoMaterialDeducteInsideDTO> lst = woVSmartBusiness
        .getListMaterial(woMaterialDeducteInsideDTO);
    if (lst != null && !lst.isEmpty()) {
      List<WoMaterialDeducteDTO> lstReturn = new ArrayList<>();
      for (WoMaterialDeducteInsideDTO item : lst) {
        WoMaterialDeducteDTO dto = item.toModelOutSide();
        if (dto != null) {
          if (dto.getSendImTime() != null && dto.getSendImTime().endsWith(".0")) {
            dto.setSendImTime(dto.getSendImTime().substring(0, dto.getSendImTime().indexOf('.')));
          }
          if (dto.getCreateDate() != null && dto.getCreateDate().endsWith(".0")) {
            dto.setCreateDate(dto.getCreateDate().substring(0, dto.getCreateDate().indexOf('.')));
          }
          dto.setDefaultSortField("woMaterialDeducteId");
          lstReturn.add(dto);
        }
      }
      return lstReturn;
    }
    return null;
  }

  @Override
  public List<WoWorklogDTO>
  getListWorklogByWoIdPaging(String woId, int rowStart, int maxRow,
      String sortType, String sortFieldList) {
    I18n.setLocaleForService(wsContext);
    List<WoWorklogDTO> lstOutSide = new ArrayList<>();
    List<WoWorklogInsideDTO> lstInside = woVSmartBusiness
        .getListDataByWoIdPaging(woId, rowStart, maxRow, sortType, sortFieldList);
    for (WoWorklogInsideDTO dto : lstInside) {
      WoWorklogDTO worklogDTO = dto.toModelOutSide();
      worklogDTO.setDefaultSortField("name");
      lstOutSide.add(worklogDTO);
    }
    return lstOutSide;
  }

  @Override
  public ResultDTO aprovePXK(Long woId, Long status, String reason) {
    I18n.setLocaleForService(wsContext);
    return woVSmartBusiness.aprovePXK(woId, status, reason, null);
  }

  @Override
  public List<WoCdGroupDTO> getListCdByLocation(String locationCode) {
    I18n.setLocaleForService(wsContext);
    List<WoCdGroupInsideDTO> lst = woVSmartBusiness.getListCdByLocation(locationCode);
    if (lst != null && !lst.isEmpty()) {
      List<WoCdGroupDTO> lstReturn = new ArrayList<>();
      for (WoCdGroupInsideDTO item : lst) {
        WoCdGroupDTO dto = item.toDtoOutSide();
        if (dto != null) {
          lstReturn.add(dto);
        }
      }
      return lstReturn;
    }
    return null;
  }

  @Override
  public List<WoDTO> getListWOAndAccount(String username, String fromDate, String toDate,
      String woCode, String cdId, String accountIsdn) {
    I18n.setLocaleForService(wsContext);
    return woVSmartBusiness
        .getListWOAndAccount(username, fromDate, toDate, woCode, cdId, accountIsdn);
  }

  @Override
  public ResultDTO insertWoWorklog(WoWorklogDTO woWorklogDTO) {
    I18n.setLocaleForService(wsContext);
    return woVSmartBusiness.insertWoWorklog(woWorklogDTO);
  }

  @Override
  public ResultDTO checkRequiredStation(String woCode) {
    I18n.setLocaleForService(wsContext);
    return woVSmartBusiness.checkRequiredStation(woCode);
  }

  @Override
  public ResultDTO pendingWoForVsmart(VsmartUpdateForm updateForm, String woCode,
      String endPendingTime, String user, String system, String reasonName, String reasonId,
      String customer, String phone) throws Exception {
    I18n.setLocaleForService(wsContext);
    return woVSmartBusiness
        .pendingWoForVsmart(updateForm, woCode, endPendingTime, user, system, reasonName, reasonId,
            customer, phone);
  }

  @Override
  public List<MaterialThresDTO> getListMaterialDTOByAction(Long actionId, Long serviceId,
      Long infraType, boolean isEnable) {
    I18n.setLocaleForService(wsContext);
    return materialThresBusiness
        .getListMaterialDTOByAction(actionId, serviceId, infraType, isEnable, getNationCode(),
            null);
  }

  @Override
  public WoTypeServiceDTO getIsCheckQrCode(Long woId) {
    I18n.setLocaleForService(wsContext);
    return woVSmartBusiness.getIsCheckQrCode(woId);
  }

  @Override
  public Integer getCountListFtByUser(String userName, String keyword) {
    I18n.setLocaleForService(wsContext);
    return woVSmartBusiness.getCountListFtByUser(userName, keyword);
  }

  @Override
  public List<CompCause> getListReasonOverdue(Long parentId) {
    I18n.setLocaleForService(wsContext);
    return woVSmartBusiness.getListReasonOverdue(parentId, getNationCode());
  }

  @Override
  public ResultDTO actionUpdateIsSupportWO(VsmartUpdateForm updateForm, String woIdStr,
      String needSupport, String userName, String content) {
    I18n.setLocaleForService(wsContext);
    try {
      return woVSmartBusiness
          .actionUpdateIsSupportWO(updateForm, woIdStr, needSupport, userName, content);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

  @Override
  public List<WoDTOSearch> getListWOByUsers(String userId, String summaryStatus, Integer isDetail,
      WoDTOSearch wdtos, int start, int count, int typeSearch) {
    I18n.setLocaleForService(wsContext);
    return woVSmartBusiness
        .getListWOByUsers(userId, summaryStatus, isDetail, wdtos, start, count, typeSearch);
  }

  @Override
  public ResultDTO updateCfgWoTickHelpVsmart(CfgWoTickHelpDTO cfgWoTickHelpDTO) {
    I18n.setLocaleForService(wsContext);
    return woVSmartBusiness.updateCfgWoTickHelpVsmart(cfgWoTickHelpDTO);
  }

  @Override
  public List<ObjKeyValue> getDataTestService(Long woId) {
    I18n.setLocaleForService(wsContext);
    return woVSmartBusiness.getDataTestService(woId);
  }

  @Override
  public ResultDTO updateTechnicalClues(String woSystemId, String technicalClues) {
    I18n.setLocaleForService(wsContext);
    return woVSmartBusiness.updateTechnicalClues(woSystemId, technicalClues);
  }

  @Override
  public List<WoCdGroupDTO> getListCdGroup(String userName) {
    I18n.setLocaleForService(wsContext);
    return woVSmartBusiness.getListCdGroup(userName);
  }

  @Override
  public List<ObjKeyValue> getObjKeyValueFromFile(Long woId, Long type) {
    I18n.setLocaleForService(wsContext);
    return woVSmartBusiness.getDataTestService(woId);
  }

  @Override
  public List<WoCdGroupDTO> getListCdGroupByDTO(WoCdGroupDTO woCdGroupDTO) {
    List<WoCdGroupDTO> list = new ArrayList<>();
    List<WoCdGroupInsideDTO> insideDTOList = woVSmartBusiness
        .getListCdGroupByDTO(woCdGroupDTO.toDtoInside());
    if (insideDTOList != null && insideDTOList.size() > 0) {
      for (WoCdGroupInsideDTO dto : insideDTOList) {
        list.add(dto.toDtoOutSide());
      }
    }
    return list;
  }

  @Override
  public List<ResultDTO> getWOSummaryInfobyUser2(String username, int typeSearch, Long cdId,
      String createTimeFrom, String createTimeTo) throws Exception {
    if (StringUtils.isNotNullOrEmpty(createTimeFrom)) {
      try {
        DateTimeUtils.convertStringToTime1(createTimeFrom, "dd/MM/yyyy HH:mm:ss");
      } catch (Exception e) {
        log.error(e.getMessage(), e);
        throw new Exception(e.getMessage());
      }
    }
    if (StringUtils.isNotNullOrEmpty(createTimeTo)) {
      try {
        DateTimeUtils.convertStringToTime1(createTimeTo, "dd/MM/yyyy HH:mm:ss");
      } catch (Exception e) {
        log.error(e.getMessage(), e);
        throw new Exception(e.getMessage());
      }
    }
    return woBusiness
        .getWOSummaryInfobyUser2(username, typeSearch, cdId, createTimeFrom, createTimeTo);
  }

  @Override
  public List<WoDTOSearch> getListDataSearch(WoDTOSearch woDTOSearch, int rowStart, int maxRow,
      String sortType, String sortFieldList) {
    woDTOSearch.setPage(rowStart);
    woDTOSearch.setPageSize(maxRow);
    woDTOSearch.setSortType(sortType);
    woDTOSearch.setSortName(sortFieldList);
    return woBusiness.getListDataSearch(woDTOSearch);
  }

  private String getNationCode() {
    if (wsContext != null) {
      MessageContext mcc = wsContext.getMessageContext();
      Message message = ((WrappedMessageContext) mcc).getWrappedMessage();
      List<Header> headers = (List<Header>) message.get(Header.HEADER_LIST);
      for (Header h : headers) {
        try {
          Element e = (Element) h.getObject();
          if (e != null && "nationCode".equalsIgnoreCase(e.getNodeName())) {
            return e.getTextContent();
          }
        } catch (Exception e) {
          log.info("----------------- Error when get nationCode from server -----------------", e);
          return null;
        }
      }
    }
    return null;
  }


  @Override
  public List<MaterialThresDTO> getListGoodsDTOByAction(Long actionId, Long serviceId,
      Long infraType, boolean isEnable) {
    return materialThresBusiness
        .getListMaterialDTOByAction(actionId, serviceId, infraType, isEnable, getNationCode(), 1L);
  }

  @Override
  public ResultDTO startWork(WoDTO woDto) {
    I18n.setLocaleForService(wsContext);
    return woVSmartBusiness.startWork(woDto);
  }

  @Override
  public List<ResultDTO> getWOSummaryInfobyType(String userId, int typeSearch, Long cdId,
      WoDTOSearch woDTOInput) {
    return woVSmartBusiness.getWOSummaryInfobyType(userId, typeSearch, cdId, woDTOInput);
  }

  @Override
  public List<WoHisForAccountDTO> getListWoHisForAccount(WoDTOSearch woDTOSearch) {
    I18n.setLocaleForService(wsContext);
    return woVSmartBusiness.getListWoHisForAccount(woDTOSearch, I18n.getLocale());
  }
}
