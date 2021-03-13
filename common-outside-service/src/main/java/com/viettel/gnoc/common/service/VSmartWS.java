package com.viettel.gnoc.common.service;

import com.viettel.gnoc.commons.dto.CompCauseDTO;
import com.viettel.gnoc.commons.dto.InfraCellServiceDetailDTO;
import com.viettel.gnoc.commons.dto.InfraDeviceDTO;
import com.viettel.gnoc.commons.dto.UserTokenGNOC;
import com.viettel.gnoc.commons.dto.UsersDTO;
import com.viettel.gnoc.cr.dto.ItemDataCR;
import com.viettel.gnoc.ws.dto.CatItemDTO;
import com.viettel.gnoc.ws.dto.CfgChildArrayDTO;
import com.viettel.gnoc.ws.dto.UnitDTO;
import com.viettel.gnoc.ws.dto.WoFileTempDTO;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService(serviceName = "VSmartWS")
public interface VSmartWS {

  @WebMethod(operationName = "getComCauseList")
  public List<CompCauseDTO> getComCauseList(
      @WebParam(name = "serviceTypeId") Long serviceTypeId//
      , @WebParam(name = "ccGroupId") List<Long> ccGroupId//
      , @WebParam(name = "parentId") Long parentId//
      , @WebParam(name = "levelId") Integer levelId, @WebParam(name = "lineType") String lineType,
      @WebParam(name = "cfgType") Long cfgType, @WebParam(name = "isEnable") Boolean isEnable)
      throws Exception;

  //
  @WebMethod(operationName = "getListAction")
  public List<CatItemDTO> getListAction(@WebParam(name = "serviceId") Long serviceId,
      @WebParam(name = "infraType") Long infraType);
  //

  @WebMethod(operationName = "getListOverReason")
  public List<CatItemDTO> getListOverReason();

  @WebMethod(operationName = "getListWoFileTempDTO")
  public List<WoFileTempDTO> getListWoFileTempDTO(
      @WebParam(name = "woFileTempDTO") WoFileTempDTO woFileTempDTO,
      @WebParam(name = "rowStart") int rowStart,
      @WebParam(name = "maxRow") int maxRow,
      @WebParam(name = "sortType") String sortType,
      @WebParam(name = "sortFieldList") String sortFieldList);

  @WebMethod(operationName = "getListItemByCategory")
  public List<CatItemDTO> getListItemByCategory(
      @WebParam(name = "categoryCode") String categoryCode);

  @WebMethod(operationName = "getListCellService")
  public List<InfraCellServiceDetailDTO> getListCellService(
      @WebParam(name = "cellCode") String cellCode,
      @WebParam(name = "cellType") String cellType);

  @WebMethod(operationName = "getListItemByCategoryAndParent")
  public List<CatItemDTO> getListItemByCategoryAndParent(
      @WebParam(name = "categoryCode") String categoryCode,
      @WebParam(name = "parentId") String parentId);

  @WebMethod(operationName = "getListUnit")
  public List<UnitDTO> getListUnit(
      @WebParam(name = "unitDTO") UnitDTO unitDTO,
      @WebParam(name = "rowStart") int rowStart,
      @WebParam(name = "maxRow") int maxRow);

  @WebMethod(operationName = "getUnit")
  public List<UnitDTO> getUnit(
      @WebParam(name = "unitDTO") UnitDTO unitDTO,
      @WebParam(name = "rowStart") int rowStart,
      @WebParam(name = "maxRow") int maxRow);

  @WebMethod(operationName = "getListInfraDeviceNonIp")
  public List<InfraDeviceDTO> getListInfraDeviceNonIp(
      @WebParam(name = "infraDeviceDTO") InfraDeviceDTO infraDeviceDTO,
      @WebParam(name = "rowStart") int rowStart,
      @WebParam(name = "maxRow") int maxRow,
      @WebParam(name = "sortType") String sortType,
      @WebParam(name = "sortFieldList") String sortFieldList);

  @WebMethod(operationName = "getListUsersDTO")
  public List<UsersDTO> getListUsersDTO(
      @WebParam(name = "usersDTO") UsersDTO usersDTO,
      @WebParam(name = "rowStart") int rowStart,
      @WebParam(name = "maxRow") int maxRow,
      @WebParam(name = "sortType") String sortType,
      @WebParam(name = "sortFieldList") String sortFieldList);

  @WebMethod(operationName = "getListLocationByLevelCBB")
  public List<ItemDataCR> getListLocationByLevelCBB(@WebParam(name = "form") Object form,
      @WebParam(name = "level") Long level,
      @WebParam(name = "parentId") Long parentId);

  @WebMethod(operationName = "getCbbChildArray")
  public List<CfgChildArrayDTO> getCbbChildArray(
      @WebParam(name = "cfgChildArrayDTO") CfgChildArrayDTO dto);

  @WebMethod(operationName = "getUserInfor")
  public UserTokenGNOC getUserInfor(@WebParam(name = "userName") String userName);
}
