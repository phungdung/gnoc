package com.viettel.gnoc.sr.service;

import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.sr.dto.SRCatalogDTO;
import com.viettel.gnoc.sr.dto.SRDTO;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService(serviceName = "SRNocProService")
public interface SRNocProService {

  //lay mang dich vu
  @WebMethod(operationName = "getListServiceArraySR")
  public List<SRCatalogDTO> getListServiceArraySR(@WebParam(name = "countryId") String countryId);

  //lay nhom dich vu
  @WebMethod(operationName = "getListServiceGroupSR")
  public List<SRCatalogDTO> getListServiceGroupSR(@WebParam(name = "countryId") String countryId);

  //lay ten dich vu
  @WebMethod(operationName = "getListServiceSR")
  public List<SRCatalogDTO> getListServiceSR(@WebParam(name = "countryId") String countryId);

  //lay don vi xu ly tuong ung vs ten dich vu
  @WebMethod(operationName = "getListUnitServiceSR")
  public List<SRCatalogDTO> getListUnitServiceSR(@WebParam(name = "countryId") String countryId);

  //lay nhom xu ly tuong ung vs ten dich vu
  @WebMethod(operationName = "getListRoleServiceSR")
  public List<SRCatalogDTO> getListRoleServiceSR(@WebParam(name = "countryId") String countryId);

  //lay trang thai tuong ung SR
  @WebMethod(operationName = "getListStatusSR")
  public ResultDTO getListStatusSR(@WebParam(name = "lstSrCode") List<String> lstSrCode
      , @WebParam(name = "lstStatus") List<String> lstStatus
      , @WebParam(name = "fromDate") String fromDate
      , @WebParam(name = "toDate") String toDate);

  //tao SR
  @WebMethod(operationName = "createSRForNoc")
  public ResultDTO createSRForNoc(@WebParam(name = "srDTO") SRDTO srDTO);

}
