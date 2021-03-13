package com.viettel.gnoc.mr.service;

import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.maintenance.dto.MrCauseWoWasCompletedDTO;
import com.viettel.gnoc.maintenance.dto.MrDTO;
import com.viettel.gnoc.maintenance.dto.MrNodeChecklistDTO;
import com.viettel.gnoc.maintenance.dto.MrNodeChecklistFilesDTO;
import com.viettel.gnoc.maintenance.dto.MrNodesDTO;
import com.viettel.gnoc.maintenance.dto.MrSearchDTO;
import com.viettel.gnoc.ws.dto.UserGroupCategoryDTO;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * @author tripm
 * @version 2.0
 * @since 12/06/2020 09:31:00
 */
@WebService(serviceName = "MrService")
public interface MrService {

  @WebMethod(operationName = "getListMrNodeChecklistForPopUp_VS")
  List<MrNodeChecklistDTO> getListMrNodeChecklistForPopUp_VS(@WebParam(name = "woId") String woId,
      @WebParam(name = "mrNodeId") String mrNodeId);

  @WebMethod(operationName = "updateMrNodeChecklistForPopUp_VS")
  ResultDTO updateMrNodeChecklistForPopUp_VS(List<MrNodeChecklistDTO> lstMrNodeChecklistDTO)
      throws Exception;

  @WebMethod(operationName = "getWoCrNodeList_VS")
  List<MrNodesDTO> getWoCrNodeList_VS(@WebParam(name = "woId") String woId,
      @WebParam(name = "crId") String crId);

  @WebMethod(operationName = "updateWoCrNodeStatus")
  ResultDTO updateWoCrNodeStatus(@WebParam(name = "lstNodes") List<MrNodesDTO> lstNodes);

  @WebMethod(operationName = "updateWODischargeBattery")
  String updateWODischargeBattery(@WebParam(name = "listMrNodeDTO") List<MrNodesDTO> listMrNodeDTO);

  /**
   * @author Dunglv3
   */
  @WebMethod(operationName = "createWoMrCdBatteryForVSmart")
  ResultDTO createWoMrCdBatteryForVSmart(@WebParam(name = "stationCode") String stationCode,
      @WebParam(name = "dcPower") String dcPower);

  @WebMethod(operationName = "validateWo")
  public ResultDTO validateWo(@WebParam(name = "woId") String woId,
      @WebParam(name = "woType") String woType);

  @WebMethod(operationName = "getListUserGroupBySystem")
  public List<UserGroupCategoryDTO> getListUserGroupBySystem(
      @WebParam(name = "lstCondition") UserGroupCategoryDTO lstCondition);

  @WebMethod(operationName = "getWorklogFromWO")
  public List<MrDTO> getWorklogFromWO(
      @WebParam(name = "mrDTO") MrSearchDTO mrDTO, @WebParam(name = "rowStart") int rowStart,
      @WebParam(name = "maxRow") int maxRow);

  @WebMethod(operationName = "updateMrStatus")
  public ResultDTO updateMrStatus(
      @WebParam(name = "crId") String crId,
      @WebParam(name = "woId") String woId);

  @WebMethod(operationName = "updateWoStatus")
  public ResultDTO updateWoStatus(@WebParam(name = "woId") String woId,
      @WebParam(name = "status") String status);

  @WebMethod(operationName = "getReasonWO")
  List<MrCauseWoWasCompletedDTO> getReasonWO(@WebParam(name = "reasonTypeId") String reasonTypeId);

  @WebMethod(operationName = "getListFileMrNodeChecklist_VS")
  public List<MrNodeChecklistFilesDTO> getListFileMrNodeChecklist_VS(
      @WebParam(name = "nodeChecklistId") String nodeChecklistId);
}
