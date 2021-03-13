package com.viettel.gnoc.wo.service;

import com.viettel.gnoc.commons.dto.UsersDTO;
import com.viettel.gnoc.wo.dto.WoCdGroupDTO;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService(serviceName = "WoCdGroupService")
public interface WoCdGroupService {

  @WebMethod(operationName = "getListWoCdGroupDTOByWoTypeAndGroupType")
  List<WoCdGroupDTO> getListWoCdGroupDTOByWoTypeAndGroupType(
      @WebParam(name = "woTypeId") Long woTypeId,
      @WebParam(name = "groupTypeId") Long groupTypeId,
      @WebParam(name = "locale") String locale);

  @WebMethod(operationName = "getCdByStationCode")
  WoCdGroupDTO getCdByStationCode(@WebParam(name = "stationCode") String stationCode,
      @WebParam(name = "woTypeId") String woTypeId,
      @WebParam(name = "cdGroupType") String cdGroupType,
      @WebParam(name = "businessName") String businessName);

  @WebMethod(operationName = "getCdByStationCodeNation")
  WoCdGroupDTO getCdByStationCodeNation(@WebParam(name = "stationCode") String stationCode,
      @WebParam(name = "woTypeId") String woTypeId,
      @WebParam(name = "cdGroupType") String cdGroupType,
      @WebParam(name = "businessName") String businessName,
      @WebParam(name = "nationCode") String nationCode);

  @WebMethod(operationName = "getWoCdGroupWoByCdGroupCode")
  WoCdGroupDTO getWoCdGroupWoByCdGroupCode(
      @WebParam(name = "woGroupCode") String woGroupCode);

  @WebMethod(operationName = "getListFtByUser")
  List<UsersDTO> getListFtByUser(@WebParam(name = "userId") String userId,
      @WebParam(name = "keyword") String keyword,
      @WebParam(name = "rowStart") int rowStart,
      @WebParam(name = "maxRow") int maxRow);

  @WebMethod(operationName = "findWoCdGroupById")
  WoCdGroupDTO findWoCdGroupById(@WebParam(name = "woCdGroupDTOId") Long id);

  @WebMethod(operationName = "getListCdGroupByUser")
  List<WoCdGroupDTO> getListCdGroupByUser(@WebParam(name = "woTypeId") Long woTypeId,
      @WebParam(name = "groupTypeId") Long groupTypeId,
      @WebParam(name = "userId") Long userId,
      @WebParam(name = "locale") String locale);

  @WebMethod(operationName = "getListWoCdGroupDTO")
  List<WoCdGroupDTO> getListWoCdGroupDTO(
      @WebParam(name = "woCdGroupDTO") WoCdGroupDTO woCdGroupDTO,
      @WebParam(name = "rowStart") int rowStart,
      @WebParam(name = "maxRow") int maxRow,
      @WebParam(name = "sortType") String sortType,
      @WebParam(name = "sortFieldList") String sortFieldList);

  @WebMethod(operationName = "getListCdGroupByUserDTO")
  List<WoCdGroupDTO> getListCdGroupByUserDTO(
      @WebParam(name = "woCdGroupDTO") WoCdGroupDTO woCdGroupDTO,
      @WebParam(name = "woTypeId") Long woTypeId,
      @WebParam(name = "groupTypeId") Long groupTypeId,
      @WebParam(name = "userId") Long userId,
      @WebParam(name = "locale") String locale);

  @WebMethod(operationName = "getListWoCdGroupActive")
  List<WoCdGroupDTO> getListWoCdGroupActive(
      @WebParam(name = "woCdGroupDTO") WoCdGroupDTO woCdGroupDTO,
      @WebParam(name = "rowStart") int rowStart,
      @WebParam(name = "maxRow") int maxRow,
      @WebParam(name = "sortType") String sortType,
      @WebParam(name = "sortFieldList") String sortFieldList);
}
