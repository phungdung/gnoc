package com.viettel.gnoc.sr.service;

import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.od.dto.ObjKeyValueVsmartDTO;
import com.viettel.gnoc.sr.dto.SRCatalogDTO;
import com.viettel.gnoc.sr.dto.SRConfigDTO;
import com.viettel.gnoc.sr.dto.SRDTO;
import com.viettel.gnoc.sr.dto.SRRoleUserDTO;
import com.viettel.gnoc.sr.dto.UnitSRCatalogDTO;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService(serviceName = "SRVsmartService")
public interface SRVsmartService {

  @WebMethod(operationName = "getSRReviewForVSmart")
  public List<SRConfigDTO> getSRReviewForVSmart(@WebParam(name = "userName") String userName);

  @WebMethod(operationName = "getListSRCatalogForVSmart")
  public List<SRCatalogDTO> getListSRCatalogForVSmart(@WebParam(name = "userName") String userName,
      @WebParam(name = "serviceGroup") String serviceGroup);

  @WebMethod(operationName = "getListSRUserForVSmart")
  public List<SRRoleUserDTO> getListSRUserForVSmart(
      @WebParam(name = "serviceCode") String serviceCode,
      @WebParam(name = "unitId") Long unitId,
      @WebParam(name = "roleCode") String roleCode,
      @WebParam(name = "country") String country
  );

  @WebMethod(operationName = "getListSRForVSmart")
  public List<SRDTO> getListSRForVSmart(@WebParam(name = "dto") SRDTO dto);

  @WebMethod(operationName = "getListSRStatusForVSmart")
  public List<SRConfigDTO> getListSRStatusForVSmart(@WebParam(name = "userName") String userName);

  @WebMethod(operationName = "updateSRForVSmart")
  public ResultDTO updateSRForVSmart(@WebParam(name = "srInputDTO") SRDTO srInputDTO);

  @WebMethod(operationName = "getDetailSRForVSmart")
  public SRDTO getDetailSRForVSmart(@WebParam(name = "srId") String srId,
      @WebParam(name = "loginUser") String loginUser);

  @WebMethod(operationName = "getListRoleUserForVsmart")
  public List<SRRoleUserDTO> getListRoleUserForVsmart(
      @WebParam(name = "srRoleUserDTO") SRRoleUserDTO srRoleUserDTO);

  @WebMethod(operationName = "getListUnitSRCatalogForVsmart")
  public List<UnitSRCatalogDTO> getListUnitSRCatalogForVsmart(
      @WebParam(name = "dto") SRCatalogDTO dto);

  @WebMethod(operationName = "getListServiceGrouprForVsmart")
  public List<SRConfigDTO> getListServiceGrouprForVsmart();

  @WebMethod(operationName = "getListSRForWOTHVSmart")
  public List<SRDTO> getListSRForWOTHVSmart(@WebParam(name = "srDTO") SRDTO srDTO,
      @WebParam(name = "woId") String woId);

  @WebMethod(operationName = "getDetailSRForWOTHVSmart")
  public SRDTO getDetailSRForWOTHVSmart(@WebParam(name = "srId") String srId);

  @WebMethod(operationName = "createSRForWOTHVSmart")
  public ResultDTO createSRForWOTHVSmart(
      @WebParam(name = "lstObjKeyValueVsmartDTO") List<ObjKeyValueVsmartDTO> lstObjKeyValueVsmartDTO,
      @WebParam(name = "createUser") String createUser,
      @WebParam(name = "serviceCode") String serviceCode);

  @WebMethod(operationName = "getListSRCatalogByConfigGroup")
  public List<SRCatalogDTO> getListSRCatalogByConfigGroup(
      @WebParam(name = "configGroup") String configGroup);
}
