package com.viettel.gnoc.wo.service;

import com.viettel.gnoc.commons.dto.UsersDTO;
import com.viettel.gnoc.wfm.dto.WoTypeDTO;
import com.viettel.gnoc.wo.dto.WoTypeTimeDTO;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService(serviceName = "WoTypeService")
public interface WoTypeService {

  @WebMethod(operationName = "getListWoTypeByWoGroup")
  List<WoTypeDTO> getListWoTypeByWoGroup(@WebParam(name = "cdGroupId") Long cdGroupId,
      @WebParam(name = "system") String system,
      @WebParam(name = "locale") String locale);

  @WebMethod(operationName = "getListWoTypeByLocale")
  List<WoTypeDTO> getListWoTypeByLocale(
      @WebParam(name = "woTypeDTO") WoTypeDTO woTypeDTO,
      @WebParam(name = "locale") String locale);

  @WebMethod(operationName = "getListWoTypeTimeDTO")
  List<WoTypeTimeDTO> getListWoTypeTimeDTO(
      @WebParam(name = "woTypeTimeDTO") com.viettel.gnoc.wo.dto.WoTypeTimeDTO woTypeTimeDTO);

  @WebMethod(operationName = "findWoTypeById")
  WoTypeDTO findWoTypeById(@WebParam(name = "woTypeDTOId") Long woTypeDTOId);

  @WebMethod(operationName = "getListWoTypeDTO")
  List<WoTypeDTO> getListWoTypeDTO(@WebParam(name = "woTypeDTO") WoTypeDTO woTypeDTO,
      @WebParam(name = "rowStart") int rowStart,
      @WebParam(name = "maxRow") int maxRow,
      @WebParam(name = "sortType") String sortType,
      @WebParam(name = "sortFieldList") String sortFieldList);

  @WebMethod(operationName = "getWoTypeByCode")
  WoTypeDTO getWoTypeByCode(@WebParam(name = "woTypeCode") String woTypeCode);

  @WebMethod(operationName = "getUnitNameByUserName")
  UsersDTO getUnitNameByUserName(@WebParam(name = "username") String username);
}
