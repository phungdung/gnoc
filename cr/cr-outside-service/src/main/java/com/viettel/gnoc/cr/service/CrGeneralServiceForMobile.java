/*
 * Copyright (C) 2011 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.gnoc.cr.service;

import com.viettel.bikt.webservices.KpignocForm;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.TroubleStatisticForm;
import com.viettel.gnoc.cr.dto.CrDTO;
import com.viettel.gnoc.cr.dto.ObjResponse;
import com.viettel.gnoc.maintenance.dto.MrClientDetail;
import com.viettel.gnoc.maintenance.dto.MrDTO;
import com.viettel.gnoc.maintenance.dto.MrForNocSearchDTO;
import com.viettel.gnoc.maintenance.dto.MrMobileDTO;
import com.viettel.gnoc.pt.dto.ProblemMonitorDTO;
import com.viettel.gnoc.pt.dto.ProblemsMobileDTO;
import com.viettel.gnoc.wo.dto.WoDTOSearch;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService(serviceName = "CrGeneralServiceForMobile")
public interface CrGeneralServiceForMobile {

  @WebMethod(operationName = "getProblemsMobileUnitNewV2")
  public List<ProblemsMobileDTO> getProblemsMobileUnitNewV2(
      @WebParam(name = "sessionId") String sessionId);

  @WebMethod(operationName = "getKpiCoDienV2")
  public List<KpignocForm> getKpiCoDienV2(@WebParam(name = "updateTime") String updateTime,
      @WebParam(name = "unitId") String unitId,
      @WebParam(name = "sessionId") String sessionId);

  @WebMethod(operationName = "getStatisticTroubleTotalNewV2")
  public List<TroubleStatisticForm> getStatisticTroubleTotalNewV2(
      @WebParam(name = "isCreateUnit") Boolean isCreateUnit,
      @WebParam(name = "searchChild") Boolean searchChild,
      @WebParam(name = "startTime") String startTime,
      @WebParam(name = "endTime") String endTime,
      @WebParam(name = "sessionId") String sessionId);

  @WebMethod(operationName = "getKpiCoDien")
  public List<KpignocForm> getKpiCoDien(@WebParam(name = "updateTime") String updateTime,
      @WebParam(name = "unitId") String unitId,
      @WebParam(name = "sessionId") String sessionId);

  @WebMethod(operationName = "getProblemsMobileUnitAllNew")
  public List<ProblemsMobileDTO> getProblemsMobileUnitAllNew(
      @WebParam(name = "receiveUnitId") String receiveUnitId,
      @WebParam(name = "sessionId") String sessionId);

  @WebMethod(operationName = "getMrChartInfoForNOCNew")
  public MrClientDetail getMrChartInfoForNOCNew(
      @WebParam(name = "mrSearchDTO") MrForNocSearchDTO mrSearchDTO,
      @WebParam(name = "sessionId") String sessionId);

  @WebMethod(operationName = "getListCRBySearchTypePaggingMobileV2")
  public ObjResponse getListCRBySearchTypePaggingMobileV2(@WebParam(name = "crDTO") CrDTO crDTO,
      @WebParam(name = "rowStart") int rowStart,
      @WebParam(name = "maxRow") int maxRow,
      @WebParam(name = "sessionId") String sessionId);

  @WebMethod(operationName = "getListCRBySearchTypePaggingMobile")
  public ObjResponse getListCRBySearchTypePaggingMobile(@WebParam(name = "crDTO") CrDTO crDTO,
      @WebParam(name = "rowStart") int rowStart,
      @WebParam(name = "maxRow") int maxRow,
      @WebParam(name = "sessionId") String sessionId);

  @WebMethod(operationName = "getKpiNIMS")
  public List<KpignocForm> getKpiNIMS(@WebParam(name = "updateTime") String updateTime,
      @WebParam(name = "unitId") String unitId,
      @WebParam(name = "sessionId") String sessionId);

  @WebMethod(operationName = "getKpiNIMSV2")
  public List<KpignocForm> getKpiNIMSV2(@WebParam(name = "updateTime") String updateTime,
      @WebParam(name = "unitId") String unitId,
      @WebParam(name = "sessionId") String sessionId);

  @WebMethod(operationName = "insertCrComment")
  public String insertCrComment(@WebParam(name = "comment") String comment,
      @WebParam(name = "crId") String crId,
      @WebParam(name = "sessionId") String sessionId);

  @WebMethod(operationName = "getKpiVMTSV2")
  public List<KpignocForm> getKpiVMTSV2(@WebParam(name = "updateTime") String updateTime,
      @WebParam(name = "unitId") String unitId,
      @WebParam(name = "sessionId") String sessionId);

  @WebMethod(operationName = "actionGetProvinceMonitoringParamFix")
  public List<ResultDTO> actionGetProvinceMonitoringParamFix(
      @WebParam(name = "sessionId") String sessionId,
      @WebParam(name = "searchChild") String searchChild,
      @WebParam(name = "startDate") String startDate,
      @WebParam(name = "endDate") String endDate);

  @WebMethod(operationName = "getListDataSearchNew")
  public List<WoDTOSearch> getListDataSearchNew(
      @WebParam(name = "woDTOSearch") WoDTOSearch woDTOSearch,
      @WebParam(name = "rowStart") int rowStart, @WebParam(name = "maxRow") int maxRow,
      @WebParam(name = "sortType") String sortType,
      @WebParam(name = "sortFieldList") String sortFieldList,
      @WebParam(name = "sessionId") String sessionId);

  @WebMethod(operationName = "getListCRBySearchTypePaggingMobileFix")
  public ObjResponse getListCRBySearchTypePaggingMobileFix(@WebParam(name = "crDTO") CrDTO crDTO,
      @WebParam(name = "rowStart") int rowStart,
      @WebParam(name = "maxRow") int maxRow,
      @WebParam(name = "sessionId") String sessionId);

  @WebMethod(operationName = "getProblemsMonitorNew")
  public List<ProblemMonitorDTO> getProblemsMonitorNew(
      @WebParam(name = "priorityId") String priorityId,
      @WebParam(name = "unitId") String unitId, @WebParam(name = "fromDate") String fromDate,
      @WebParam(name = "toDate") String toDate,
      @WebParam(name = "findInSubUnit") String findInSubUnit,
      @WebParam(name = "unitType") String unitType,
      @WebParam(name = "sessionId") String sessionId);

  @WebMethod(operationName = "actionGetProvinceMonitoringParamMobile")
  public List<ResultDTO> actionGetProvinceMonitoringParamMobile(
      @WebParam(name = "unitId") String unitId,
      @WebParam(name = "searchChild") String searchChild,
      @WebParam(name = "startDate") String startDate,
      @WebParam(name = "endDate") String endDate,
      @WebParam(name = "sessionId") String sessionId);

  @WebMethod(operationName = "updateUserTimeZoneMobile")
  public String updateUserTimeZoneMobile(@WebParam(name = "sessionId") String sessionId,
      @WebParam(name = "timeZoneId") String timeZoneId);

  @WebMethod(operationName = "getListMrForMobileNew")
  public List<MrDTO> getListMrForMobileNew(@WebParam(name = "mrMobileDTO") MrMobileDTO dto,
      @WebParam(name = "sessionId") String sessionId);

  @WebMethod(operationName = "getWOStatisticNew")
  public List<ResultDTO> getWOStatisticNew(//
      @WebParam(name = "unitId") Long unitId//
      , @WebParam(name = "isSend") int isSend//
      , @WebParam(name = "isSearchChild") int isSearchChild//
      , @WebParam(name = "fromDate") String fromDate//
      , @WebParam(name = "toDate") String toDate,
      @WebParam(name = "sessionId") String sessionId);

  @WebMethod(operationName = "getMrChartInfoForNOCNewV2")
  public MrClientDetail getMrChartInfoForNOCNewV2(
      @WebParam(name = "mrSearchDTO") MrForNocSearchDTO mrSearchDTO,
      @WebParam(name = "sessionId") String sessionId);

  @WebMethod(operationName = "getProblemsMobileUnitNew")
  public List<ProblemsMobileDTO> getProblemsMobileUnitNew(
      @WebParam(name = "receiveUnitId") String receiveUnitId,
      @WebParam(name = "sessionId") String sessionId);

  @WebMethod(operationName = "getListMrForMobileNewV2")
  public List<MrDTO> getListMrForMobileNewV2(@WebParam(name = "mrMobileDTO") MrMobileDTO dto,
      @WebParam(name = "sessionId") String sessionId);

  @WebMethod(operationName = "getListCRBySearchTypeCount")
  public List<CrDTO> getListCRBySearchTypeCount(CrDTO crDTO);

  @WebMethod(operationName = "getListDataSearchNewV2")
  public List<WoDTOSearch> getListDataSearchNewV2(
      @WebParam(name = "woDTOSearch") WoDTOSearch woDTOSearch,
      @WebParam(name = "rowStart") int rowStart, @WebParam(name = "maxRow") int maxRow,
      @WebParam(name = "sortType") String sortType,
      @WebParam(name = "sortFieldList") String sortFieldList,
      @WebParam(name = "sessionId") String sessionId);

  @WebMethod(operationName = "actionGetProvinceMonitoringParamMobileV2")
  public List<ResultDTO> actionGetProvinceMonitoringParamMobileV2(
      @WebParam(name = "searchChild") String searchChild,
      @WebParam(name = "startDate") String startDate,
      @WebParam(name = "endDate") String endDate,
      @WebParam(name = "sessionId") String sessionId);

  @WebMethod(operationName = "insertWoComment")
  public String insertWoComment(@WebParam(name = "comment") String comment,
      @WebParam(name = "woId") String woId,
      @WebParam(name = "woSystem") String woSystem,
      @WebParam(name = "woSystemId") String woSystemId,
      @WebParam(name = "userName") String userName,
      @WebParam(name = "sessionId") String sessionId);

  @WebMethod(operationName = "getProblemsMobileUnitAllNewV2")
  public List<ProblemsMobileDTO> getProblemsMobileUnitAllNewV2(
      @WebParam(name = "sessionId") String sessionId);

  @WebMethod(operationName = "getProblemsMonitorNewV2")
  public List<ProblemMonitorDTO> getProblemsMonitorNewV2(
      @WebParam(name = "priorityId") String priorityId,
      @WebParam(name = "fromDate") String fromDate,
      @WebParam(name = "toDate") String toDate,
      @WebParam(name = "findInSubUnit") String findInSubUnit,
      @WebParam(name = "unitType") String unitType,
      @WebParam(name = "sessionId") String sessionId);

  @WebMethod(operationName = "getKpiVMTS")
  public List<KpignocForm> getKpiVMTS(@WebParam(name = "updateTime") String updateTime,
      @WebParam(name = "unitId") String unitId,
      @WebParam(name = "sessionId") String sessionId);

  @WebMethod(operationName = "getWOTotalNew")
  public Integer getWOTotalNew(@WebParam(name = "woDTOSearch") WoDTOSearch woDTOSearch,
      @WebParam(name = "sessionId") String sessionId);

  @WebMethod(operationName = "getStatisticTroubleTotalNew")
  public List<TroubleStatisticForm> getStatisticTroubleTotalNew(
      @WebParam(name = "unitId") String unitId,
      @WebParam(name = "isCreateUnit") Boolean isCreateUnit,
      @WebParam(name = "searchChild") Boolean searchChild,
      @WebParam(name = "startTime") String startTime,
      @WebParam(name = "endTime") String endTime,
      @WebParam(name = "sessionId") String sessionId);
}
