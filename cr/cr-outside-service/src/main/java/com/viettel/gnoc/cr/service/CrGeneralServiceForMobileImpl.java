/*
 * Copyright (C) 2011 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.gnoc.cr.service;

import com.viettel.bikt.webservices.KpignocForm;
import com.viettel.gnoc.commons.business.UserBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.TroubleStatisticForm;
import com.viettel.gnoc.commons.dto.TroubleStatisticFormDTO;
import com.viettel.gnoc.commons.proxy.MrCategoryProxy;
import com.viettel.gnoc.commons.proxy.PtServiceProxy;
import com.viettel.gnoc.commons.proxy.TtServiceProxy;
import com.viettel.gnoc.commons.proxy.WoServiceProxy;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.business.CrBusiness;
import com.viettel.gnoc.cr.business.CrGeneralForMobileBusiness;
import com.viettel.gnoc.cr.business.CrMobileBusiness;
import com.viettel.gnoc.cr.business.CrProvinceMonitoringBusiness;
import com.viettel.gnoc.cr.dto.CrDTO;
import com.viettel.gnoc.cr.dto.ObjResponse;
import com.viettel.gnoc.cr.dto.SessionResponse;
import com.viettel.gnoc.maintenance.dto.MrClientDetail;
import com.viettel.gnoc.maintenance.dto.MrDTO;
import com.viettel.gnoc.maintenance.dto.MrForNocSearchDTO;
import com.viettel.gnoc.maintenance.dto.MrMobileDTO;
import com.viettel.gnoc.pt.dto.ProblemMonitorDTO;
import com.viettel.gnoc.pt.dto.ProblemsInsideDTO;
import com.viettel.gnoc.pt.dto.ProblemsMobileDTO;
import com.viettel.gnoc.wo.dto.WoDTOSearch;
import com.viettel.gnoc.wo.dto.WoStatisticDTO;
import com.viettel.gnoc.wo.dto.WoWorklogDTO;
import com.viettel.gnoc.wo.dto.WorkLogInsiteDTO;
import com.viettel.gnoc.ws.provider.WSBIKTPort;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author sondt20
 * @version 1.0
 * @since 8/20/2015 4:51 PM
 */
@Service
@Slf4j
public class CrGeneralServiceForMobileImpl implements CrGeneralServiceForMobile {

  @Resource
  private WebServiceContext wsContext;

  @Autowired
  CrGeneralForMobileBusiness crGeneralForMobileBusiness;

  @Autowired
  PtServiceProxy ptServiceProxy;

  @Autowired
  WSBIKTPort wsbiktPort;

  @Autowired
  CrBusiness crBusiness;

  @Autowired
  MrCategoryProxy mrCategoryProxy;

  @Autowired
  TtServiceProxy ttServiceProxy;

  @Autowired
  CrMobileBusiness crMobileBusiness;

  @Autowired
  CrProvinceMonitoringBusiness crProvinceMonitoringBusiness;

  @Autowired
  UserBusiness userBusiness;

  @Autowired
  WoServiceProxy woServiceProxy;

  @Override
  public List<ProblemsMobileDTO> getProblemsMobileUnitNewV2(String sessionId) {
    try {
      setLocale();
      List<ProblemsMobileDTO> rs = new ArrayList<>();
      SessionResponse sessionInfo = crGeneralForMobileBusiness.getSessionInfo(sessionId);
      if (sessionInfo != null) {
        String receiveUnitId = sessionInfo.getUnitId();
        rs = ptServiceProxy.getProblemsMobileUnitProxy(receiveUnitId);
      }
      return rs;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return new ArrayList<>();
  }

  @Override
  public List<KpignocForm> getKpiCoDienV2(String updateTime,
      String unitId, String sessionId) {
    List<KpignocForm> rs = new ArrayList<>();
    try {
      SessionResponse sessionInfo = crGeneralForMobileBusiness.getSessionInfo(sessionId);
      if (sessionInfo != null) {
        rs = getCodienGeneral(updateTime, sessionInfo.getUnitId());
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return rs;
  }

  @Override
  public List<KpignocForm> getKpiCoDien(String updateTime,
      String unitId, String sessionId) {
    List<KpignocForm> rs = new ArrayList<>();
    try {
      String userId = crGeneralForMobileBusiness.checkSession(sessionId);
      if (StringUtils.isNotNullOrEmpty(userId)) {
        rs = getCodienGeneral(updateTime, unitId);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return rs;
  }

  @Override
  public List<ProblemsMobileDTO> getProblemsMobileUnitAllNew(String receiveUnitId,
      String sessionId) {
    List<ProblemsMobileDTO> rs = new ArrayList<>();
    try {
      String userId = crGeneralForMobileBusiness.checkSession(sessionId);
      if (StringUtils.isNotNullOrEmpty(userId)) {
        rs = ptServiceProxy.getProblemsMobileUnitAllProxy(receiveUnitId);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return rs;
  }

  @Override
  public MrClientDetail getMrChartInfoForNOCNew(MrForNocSearchDTO mrSearchDTO,
      String sessionId) {
    MrClientDetail rs = new MrClientDetail();
    try {
      String userId = crGeneralForMobileBusiness.checkSession(sessionId);
      if (StringUtils.isNotNullOrEmpty(userId)) {
        rs = mrCategoryProxy.getMrChartInfoForNOC(mrSearchDTO);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return rs;
  }

  @Override
  public ObjResponse getListCRBySearchTypePaggingMobile(CrDTO crDTO, int start, int maxResult,
      String sessionId) {
    setLocale();
    ObjResponse obj = new ObjResponse();
    try {
      String userId = crGeneralForMobileBusiness.checkSession(sessionId);
      if (StringUtils.isNotNullOrEmpty(userId)) {
        obj = crBusiness.getListCRBySearchTypePagging(crDTO, start, maxResult, I18n.getLocale());
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return obj;
  }

  @Override
  public ObjResponse getListCRBySearchTypePaggingMobileV2(CrDTO crDTO, int start, int maxResult,
      String sessionId) {
    ObjResponse obj = new ObjResponse();
    setLocale();
    try {
      SessionResponse sessionInfo = crGeneralForMobileBusiness.getSessionInfo(sessionId);
      if (sessionInfo != null) {
        crDTO.setUserLogin(sessionInfo.getUserId());
        crDTO.setUserLoginUnit(sessionInfo.getUnitId());
        obj = crBusiness.getListCRBySearchTypePagging(crDTO, start, maxResult, I18n.getLocale());
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return obj;
  }

  @Override
  public List<KpignocForm> getKpiNIMSV2(String updateTime,
      String unitId, String sessionId) {
    List<KpignocForm> rs = new ArrayList<>();
    try {
      SessionResponse sessionInfo = crGeneralForMobileBusiness.getSessionInfo(sessionId);
      if (sessionInfo != null) {
        String locationCode = crGeneralForMobileBusiness
            .getLocationByUnitId(sessionInfo.getUnitId());
        if (StringUtils.isNotNullOrEmpty(locationCode) && locationCode.length() >= 4) {
          List<KpignocForm> lstProvince = wsbiktPort.getKvProvince();
          String provinceCode = wsbiktPort.getProvinceByLocationCode(locationCode, lstProvince);
          String kvCode = wsbiktPort.getKvCodeByProvinceCode(provinceCode, lstProvince);
          rs = wsbiktPort.getKpiNIMS(updateTime, kvCode, provinceCode);
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return rs;
  }

  @Override
  public List<KpignocForm> getKpiNIMS(String updateTime,
      String unitId, String sessionId) {
    List<KpignocForm> rs = new ArrayList<>();
    try {
      String userId = crGeneralForMobileBusiness.checkSession(sessionId);
      if (StringUtils.isNotNullOrEmpty(userId)) {
        String locationCode = crGeneralForMobileBusiness.getLocationByUnitId(unitId);
        if (StringUtils.isNotNullOrEmpty(locationCode) && locationCode.length() >= 4) {
          List<KpignocForm> lstProvince = wsbiktPort.getKvProvince();
          String provinceCode = wsbiktPort.getProvinceByLocationCode(locationCode, lstProvince);
          String kvCode = wsbiktPort.getKvCodeByProvinceCode(provinceCode, lstProvince);
          rs = wsbiktPort.getKpiNIMS(updateTime, kvCode, provinceCode);
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return rs;
  }

  @Override
  public String insertCrComment(String comment, String crId, String sessionId) {
    String rs = "";
    String userId = crGeneralForMobileBusiness.checkSession(sessionId);
    if (StringUtils.isNotNullOrEmpty(userId)) {
      WorkLogInsiteDTO workLogDTO = new WorkLogInsiteDTO();
      workLogDTO.setUserGroupAction(13L);
      workLogDTO.setWlgEffortHours(0L);
      workLogDTO.setWlgEffortMinutes(0L);
      workLogDTO.setCreatedDate(new Date());
      workLogDTO.setWlgText(comment);
      workLogDTO.setWlgObjectId(Long.valueOf(crId));
      workLogDTO.setUserId(Long.valueOf(userId));
      workLogDTO.setWlgObjectType(2L);//1 : MR, 2 : CR
      workLogDTO.setWlayId(80L);
      ResultInSideDto rDto = crBusiness.insertWorkLogProxy(workLogDTO);
      rs = rDto.getMessage();
    }
    return rs;
  }


  @Override
  public List<KpignocForm> getKpiVMTSV2(String updateTime,
      String unitId, String sessionId) {
    List<KpignocForm> rs = new ArrayList<>();
    try {
      SessionResponse sessionInfo = crGeneralForMobileBusiness.getSessionInfo(sessionId);
      if (sessionInfo != null) {
        String locationCode = crGeneralForMobileBusiness
            .getLocationByUnitId(sessionInfo.getUnitId());
        if (StringUtils.isNotNullOrEmpty(locationCode) && locationCode.length() >= 4) {
          List<KpignocForm> lstProvince = wsbiktPort.getKvProvince();
          String provinceCode = wsbiktPort.getProvinceByLocationCode(locationCode, lstProvince);
          String kvCode = wsbiktPort.getKvCodeByProvinceCode(provinceCode, lstProvince);
          rs = wsbiktPort.getKpiVMTS(updateTime, kvCode, provinceCode);
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return rs;
  }

  @Override
  public List<ResultDTO> actionGetProvinceMonitoringParamFix(
      String sessionId,
      String searchChild,
      String startDate,
      String endDate) {
    List<ResultDTO> lst = new ArrayList<>();
    try {
      SessionResponse sessionInfo = crGeneralForMobileBusiness.getSessionInfo(sessionId);
      if (sessionInfo != null) {
        lst = crBusiness
            .actionGetProvinceMonitoringParamFix(sessionInfo.getUserId(), sessionInfo.getUnitId(),
                searchChild, startDate, endDate);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return lst;
  }

  @Override
  public List<TroubleStatisticForm> getStatisticTroubleTotalNewV2(Boolean isCreateUnit,
      Boolean searchChild, String startTime, String endTime, String sessionId) {
    List<TroubleStatisticForm> rs = new ArrayList<>();
    try {
      SessionResponse sessionInfo = crGeneralForMobileBusiness.getSessionInfo(sessionId);
      if (sessionInfo != null) {
        String unitId = sessionInfo.getUnitId();
        TroubleStatisticFormDTO dto = new TroubleStatisticFormDTO(unitId, isCreateUnit, searchChild,
            startTime, endTime);
        rs = ttServiceProxy.getStatisticTroubleTotal(dto);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return rs;
  }


  @Override
  public List<WoDTOSearch> getListDataSearchNew(WoDTOSearch woDTOSearch, int rowStart, int maxRow,
      String sortType, String sortFieldList,
      String sessionId) {
    List<com.viettel.gnoc.wo.dto.WoDTOSearch> lst = new ArrayList<>();
    try {
      String userId = crGeneralForMobileBusiness.checkSession(sessionId);
      if (StringUtils.isNotNullOrEmpty(userId)) {
        int page = 1;
        woDTOSearch.setPageSize(maxRow);
        woDTOSearch.setSortType(sortType);
        woDTOSearch.setSortName(sortFieldList);
        if (maxRow != 0) {
          page = (int) Math.ceil((rowStart + 1) * 1.0 / maxRow);
          woDTOSearch.setPage(page);
        }
        lst = woServiceProxy.getListDataSearchProxy(woDTOSearch);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return lst;
  }

  @Override
  public ObjResponse getListCRBySearchTypePaggingMobileFix(CrDTO crDTO, int start, int maxResult,
      String sessionId) {
    setLocale();
    ObjResponse obj = new ObjResponse();
    SessionResponse sessionInfo = crGeneralForMobileBusiness.getSessionInfo(sessionId);
    if (sessionInfo != null) {
      crDTO.setUserLogin(sessionInfo.getUserId());
      crDTO.setUserLoginUnit(sessionInfo.getUnitId());
      obj = crMobileBusiness
          .getListCRBySearchTypePaggingMobile(crDTO, start, maxResult, I18n.getLocale());
    }
    return obj;
  }

  @Override
  public List<ProblemMonitorDTO> getProblemsMonitorNew(String priorityId, String unitId,
      String fromDate,
      String toDate, String findInSubUnit, String unitType, String sessionId) {
    List<ProblemMonitorDTO> rs = new ArrayList<>();
    String userId = crGeneralForMobileBusiness.checkSession(sessionId);
    try {
      if (StringUtils.isNotNullOrEmpty(userId)) {
        ProblemsInsideDTO problemsInsideDTO = new ProblemsInsideDTO();
        problemsInsideDTO.setPriorityId(
            StringUtils.isStringNullOrEmpty(priorityId) ? null : Long.valueOf(priorityId));
        problemsInsideDTO.setUnitId(unitId);
        problemsInsideDTO.setFromDate(fromDate);
        problemsInsideDTO.setToDate(toDate);
        problemsInsideDTO.setFindInSubUnit(findInSubUnit);
        problemsInsideDTO.setUnitType(unitType);
        rs = ptServiceProxy
            .getProblemsMonitor(problemsInsideDTO);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return rs;
  }

  @Override
  public List<ResultDTO> actionGetProvinceMonitoringParamMobile(String unitId,
      String searchChild,
      String startDate,
      String endDate,
      String sessionId) {
    List<ResultDTO> lst = new ArrayList<ResultDTO>();
    String userId = crGeneralForMobileBusiness.checkSession(sessionId);
    if (StringUtils.isNotNullOrEmpty(userId)) {
      lst = crProvinceMonitoringBusiness
          .actionGetProvinceMonitoringParam(unitId, searchChild, startDate, endDate);
    }
    return lst;
  }

  @Override
  public String updateUserTimeZoneMobile(String sessionId, String timeZoneId) {
    SessionResponse sessionInfo = crGeneralForMobileBusiness.getSessionInfo(sessionId);
    if (sessionInfo != null) {
      ResultInSideDto result = userBusiness.updateUserTimeZone(sessionInfo.getUserId(), timeZoneId);
      if (result != null) {
        return result.getKey();
      }
    }
    return "FAIL";
  }

  @Override
  public List<MrDTO> getListMrForMobileNew(MrMobileDTO dto,
      String sessionId) {
    List<MrDTO> rs = new ArrayList<>();
    String userId = crGeneralForMobileBusiness.checkSession(sessionId);
    if (StringUtils.isNotNullOrEmpty(userId)) {
      rs = mrCategoryProxy.getListMrForMobile(dto);
    }
    return rs;
  }

  @Override
  public List<ResultDTO> getWOStatisticNew(
      Long unitId, int isSend, int isSearchChild,
      String fromDate, String toDate, String sessionId) {
    List<ResultDTO> rs = new ArrayList<ResultDTO>();
    String userId = crGeneralForMobileBusiness.checkSession(sessionId);
    if (StringUtils.isNotNullOrEmpty(userId)) {
      WoStatisticDTO woStatisticDTO = new WoStatisticDTO();
      woStatisticDTO.setIsSend(isSend);
      woStatisticDTO.setUnitId(unitId);
      woStatisticDTO.setIsSearchChild(isSearchChild);
      woStatisticDTO.setFromDate(fromDate);
      woStatisticDTO.setToDate(toDate);
      rs = woServiceProxy.getWOStatisticProxy(woStatisticDTO);
    }
    return rs;
  }

  @Override
  public MrClientDetail getMrChartInfoForNOCNewV2(MrForNocSearchDTO mrSearchDTO,
      String sessionId) {
    MrClientDetail rs = new MrClientDetail();
    SessionResponse sessionInfo = crGeneralForMobileBusiness.getSessionInfo(sessionId);
    if (sessionInfo != null) {
      mrSearchDTO.setUnitId(sessionInfo.getUnitId());
      rs = mrCategoryProxy.getMrChartInfoForNOC(mrSearchDTO);

    }
    return rs;
  }

  @Override
  public List<ProblemsMobileDTO> getProblemsMobileUnitNew(String receiveUnitId,
      String sessionId) {
    List<ProblemsMobileDTO> rs = new ArrayList<>();
    String userId = crGeneralForMobileBusiness.checkSession(sessionId);
    if (StringUtils.isNotNullOrEmpty(userId)) {
      rs = ptServiceProxy.getProblemsMobileUnitProxy(receiveUnitId);
    }
    return rs;
  }

  @Override
  public List<MrDTO> getListMrForMobileNewV2(MrMobileDTO dto,
      String sessionId) {
    List<MrDTO> rs = new ArrayList<MrDTO>();
    try {
      SessionResponse sessionInfo = crGeneralForMobileBusiness.getSessionInfo(sessionId);
      if (sessionInfo != null) {
        dto.setUserId(sessionInfo.getUserId());
        dto.setUnitId(sessionInfo.getUnitId());
        rs = mrCategoryProxy.getListMrForMobile(dto);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return rs;
  }

  @Override
  public List<CrDTO> getListCRBySearchTypeCount(CrDTO crDTO) {
    return crMobileBusiness.getListCRBySearchTypeCount(crDTO);
  }

  @Override
  public List<WoDTOSearch> getListDataSearchNewV2(WoDTOSearch woDTOSearch, int rowStart, int maxRow,
      String sortType, String sortFieldList,
      String sessionId) {
    List<WoDTOSearch> lst = new ArrayList<>();
    try {
      SessionResponse sessionInfo = crGeneralForMobileBusiness.getSessionInfo(sessionId);
      if (sessionInfo != null) {
        woDTOSearch.setUserId(sessionInfo.getUserId());
        lst = woServiceProxy.getListDataSearchProxy(woDTOSearch);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return lst;
  }

  @Override
  public List<ResultDTO> actionGetProvinceMonitoringParamMobileV2(
      String searchChild,
      String startDate,
      String endDate,
      String sessionId) {
    List<ResultDTO> lst = new ArrayList<>();
    SessionResponse sessionInfo = crGeneralForMobileBusiness.getSessionInfo(sessionId);
    if (sessionInfo != null) {
      lst = crProvinceMonitoringBusiness
          .actionGetProvinceMonitoringParam(sessionInfo.getUnitId(), searchChild, startDate,
              endDate);
    }
    return lst;
  }

  @Override
  public List<ProblemsMobileDTO> getProblemsMobileUnitAllNewV2(
      String sessionId) {
    List<ProblemsMobileDTO> rs = new ArrayList<>();
    try {
      SessionResponse sessionInfo = crGeneralForMobileBusiness.getSessionInfo(sessionId);
      if (sessionInfo != null) {
        String receiveUnitId = sessionInfo.getUnitId();
        rs = ptServiceProxy.getProblemsMobileUnitAllProxy(receiveUnitId);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return rs;

  }

  @Override
  public String insertWoComment(String comment, String woId, String woSystem,
      String woSystemId, String userName, String sessionId) {
    String rs = "";
    String userId = crGeneralForMobileBusiness.checkSession(sessionId);
    try {
      if (StringUtils.isNotNullOrEmpty(userId)) {
        WoWorklogDTO woWorklogDTO = new WoWorklogDTO();
        woWorklogDTO.setWoId(woId);
        woWorklogDTO.setWoWorklogContent(comment);
        woWorklogDTO.setUpdateTime(DateTimeUtils.date2ddMMyyyyHHMMss(new Date()));
        woWorklogDTO.setWoSystem(woSystem);
        woWorklogDTO.setWoSystemId(woSystemId);
        woWorklogDTO.setUserId(userId);
        woWorklogDTO.setUsername(userName);
        ResultDTO rDto = woServiceProxy.insertWoWorklogProxy(woWorklogDTO);
        rs = rDto.getMessage();
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return rs;

  }

  @Override
  public List<ProblemMonitorDTO> getProblemsMonitorNewV2(String priorityId, String fromDate,
      String toDate, String findInSubUnit, String unitType, String sessionId) {
    List<ProblemMonitorDTO> rs = new ArrayList<>();
    try {
      SessionResponse sessionInfo = crGeneralForMobileBusiness.getSessionInfo(sessionId);
      if (sessionInfo != null) {
        String unitId = sessionInfo.getUnitId();
        ProblemsInsideDTO problemsInsideDTO = new ProblemsInsideDTO();
        problemsInsideDTO.setPriorityId(
            StringUtils.isStringNullOrEmpty(priorityId) ? null : Long.valueOf(priorityId));
        problemsInsideDTO.setUnitId(unitId);
        problemsInsideDTO.setFromDate(fromDate);
        problemsInsideDTO.setToDate(toDate);
        problemsInsideDTO.setFindInSubUnit(findInSubUnit);
        problemsInsideDTO.setUnitType(unitType);
        rs = ptServiceProxy.getProblemsMonitor(problemsInsideDTO);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return rs;
  }

  @Override
  public List<KpignocForm> getKpiVMTS(String updateTime,
      String unitId, String sessionId) {
    List<KpignocForm> rs = new ArrayList<>();
    try {
      String userId = crGeneralForMobileBusiness.checkSession(sessionId);
      if (StringUtils.isNotNullOrEmpty(userId)) {
        String locationCode = crGeneralForMobileBusiness.getLocationByUnitId(unitId);
        if (StringUtils.isNotNullOrEmpty(locationCode) && locationCode.length() >= 4) {
          List<KpignocForm> lstProvince = wsbiktPort.getKvProvince();
          String provinceCode = wsbiktPort.getProvinceByLocationCode(locationCode, lstProvince);
          String kvCode = wsbiktPort.getKvCodeByProvinceCode(provinceCode, lstProvince);
          rs = wsbiktPort.getKpiVMTS(updateTime, kvCode, provinceCode);
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return rs;
  }

  @Override
  public Integer getWOTotalNew(WoDTOSearch woDTOSearch,
      String sessionId) {
    Integer rs = 0;
    try {
      String userId = crGeneralForMobileBusiness.checkSession(sessionId);
      if (StringUtils.isNotNullOrEmpty(userId)) {
        rs = woServiceProxy.getWOTotalProxy(woDTOSearch);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return rs;
  }

  @Override
  public List<TroubleStatisticForm> getStatisticTroubleTotalNew(String unitId, Boolean isCreateUnit,
      Boolean searchChild, String startTime, String endTime, String sessionId) {
    List<TroubleStatisticForm> rs = new ArrayList<>();
    try {
      String userId = crGeneralForMobileBusiness.checkSession(sessionId);
      if (StringUtils.isNotNullOrEmpty(userId)) {
        TroubleStatisticFormDTO dto = new TroubleStatisticFormDTO(unitId, isCreateUnit, searchChild,
            startTime, endTime);
        rs = ttServiceProxy.getStatisticTroubleTotal(dto);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return rs;
  }

  private void setLocale() {
    I18n.setLocaleForService(wsContext);
  }

  private List<KpignocForm> getCodienGeneral(String updateTime, String unitId) {
    String locationCode = crGeneralForMobileBusiness.getLocationByUnitId(unitId);
    if (StringUtils.isNotNullOrEmpty(locationCode) && locationCode.length() >= 4) {
      List<KpignocForm> lstProvince = wsbiktPort.getKvProvince();
      String provinceCode = wsbiktPort.getProvinceByLocationCode(locationCode, lstProvince);
      String kvCode = wsbiktPort.getKvCodeByProvinceCode(provinceCode, lstProvince);
      log.info("provinceCode:" + provinceCode);
      log.info("kvCode:" + kvCode);
      return wsbiktPort.getKpiCoDien(updateTime, kvCode, provinceCode);
    }
    return new ArrayList<>();
  }
}
