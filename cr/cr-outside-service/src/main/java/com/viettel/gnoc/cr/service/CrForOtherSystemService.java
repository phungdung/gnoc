package com.viettel.gnoc.cr.service;

import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.cr.dto.CrCreatedFromOtherSysDTO;
import com.viettel.gnoc.cr.dto.CrDTO;
import com.viettel.gnoc.cr.dto.CrFileAttachOutput;
import com.viettel.gnoc.cr.dto.CrFileAttachOutputWithContent;
import com.viettel.gnoc.cr.dto.CrOutputForOCSDTO;
import com.viettel.gnoc.cr.dto.CrOutputForQLTNDTO;
import com.viettel.gnoc.cr.dto.CrOutputForSOCDTO;
import com.viettel.gnoc.cr.dto.SelectionResultDTO;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService(serviceName = "CrForOtherSystemService")
public interface CrForOtherSystemService {

  @WebMethod(operationName = "getListData")
  public List<CrCreatedFromOtherSysDTO> getListData(@WebParam(name = "crId") Long crId,
      @WebParam(name = "systemId") Long systemId, @WebParam(name = "objectId") Long objectId);

  @WebMethod(operationName = "getListDataByObjectId")
  public List<CrCreatedFromOtherSysDTO> getListDataByObjectId(
      @WebParam(name = "objectId") Long objectId);

  @WebMethod(operationName = "getCrCreatedFromOtherSysDTO")
  public CrCreatedFromOtherSysDTO getCrCreatedFromOtherSysDTO(
      @WebParam(name = "crId") Long crId);

  @WebMethod(operationName = "checkWoCloseAutoSetting")
  public boolean checkWoCloseAutoSetting(
      @WebParam(name = "unitId") Long unitId, @WebParam(name = "woTypeId") Long woTypeId);

  @WebMethod(operationName = "getlistCRFromTimeInterval")
  public SelectionResultDTO getlistCRFromTimeInterval(
      @WebParam(name = "userService") String userService,
      @WebParam(name = "passService") String passService, @WebParam(name = "minute") String minute);

  @WebMethod(operationName = "getCrForQLTN")
  public CrOutputForQLTNDTO getCrForQLTN(@WebParam(name = "userService") String userService,
      @WebParam(name = "passService") String passService,
      @WebParam(name = "crNumber") String crNumber);

  @WebMethod(operationName = "getCrForOCS")
  public List<CrOutputForOCSDTO> getCrForOCS(@WebParam(name = "userService") String userService,
      @WebParam(name = "passService") String passService,
      @WebParam(name = "userName") String userName, @WebParam(name = "startTime") String startTime);

  @WebMethod(operationName = "getListDeviceAffectForSOC")
  public List<CrOutputForSOCDTO> getListDeviceAffectForSOC(
      @WebParam(name = "lastUpdateTime") String lastUpdateTime);

  @WebMethod(operationName = "getCrFileDTAttachWithContent")
  public List<CrFileAttachOutputWithContent> getCrFileDTAttachWithContent(
      @WebParam(name = "userService") String userService,
      @WebParam(name = "passService") String passService,
      @WebParam(name = "crNumber") String crNumber,
      @WebParam(name = "attachTime") String attachTime,
      @WebParam(name = "fileType") String fileType);

  @WebMethod(operationName = "getCrFileDTAttach")
  public List<CrFileAttachOutput> getCrFileDTAttach(
      @WebParam(name = "crNumber") String crNumber,
      @WebParam(name = "attachTime") String attachTime);

  @WebMethod(operationName = "createCRTraceFileAttach")
  public ResultDTO createCRTraceFileAttach(@WebParam(name = "userService") String userService,
      @WebParam(name = "passService") String passService,
      @WebParam(name = "username") String username,
      @WebParam(name = "crId") String crId, @WebParam(name = "fileType") String fileType,
      @WebParam(name = "fileName") String fileName,
      @WebParam(name = "fileContent") String fileContent);

  @WebMethod(operationName = "actionResolveCrOcs")
  public String actionResolveCrOcs(@WebParam(name = "userService") String userService,
      @WebParam(name = "passService") String passService,
      @WebParam(name = "userName") String userName,
      @WebParam(name = "crNumber") String crNumber,
      @WebParam(name = "returnCode") String returnCode);

  @WebMethod(operationName = "insertFile")
  public ResultDTO insertFile(@WebParam(name = "userService") String userService,
      @WebParam(name = "passService") String passService,
      @WebParam(name = "userName") String userName,
      @WebParam(name = "crNumber") String crNumber,
      @WebParam(name = "fileType") String fileType,
      @WebParam(name = "fileName") String fileName,
      @WebParam(name = "fileContent") String fileContent);

  @WebMethod(operationName = "updateDtInfo")
  public ResultDTO updateDtInfo(@WebParam(name = "userService") String userService,
      @WebParam(name = "passService") String passService,
      @WebParam(name = "userName") String userName,
      @WebParam(name = "crNumber") String crNumber, @WebParam(name = "dtCode") String dtCode,
      @WebParam(name = "lstIpImpact") List<String> lstIpImpact,
      @WebParam(name = "lstIpAffect") List<String> lstIpAffect,
      @WebParam(name = "mopFile") String mopFile,
      @WebParam(name = "mopFileContent") String mopFileContent,
      @WebParam(name = "mopRollbackFile") String mopRollbackFile,
      @WebParam(name = "mopRollbackFileContent") String mopRollbackFileContent,
      @WebParam(name = "lstAffectService") List<String> lstAffectService,
      @WebParam(name = "nationCode") String nationCode);

  @WebMethod(operationName = "createCRTrace")
  public ResultDTO createCRTrace(@WebParam(name = "userService") String userService,
      @WebParam(name = "passService") String passService,
      @WebParam(name = "crDTO") CrDTO crDTO);

  @WebMethod(operationName = "getCrByCode")
  public CrOutputForQLTNDTO getCrByCode(@WebParam(name = "userService") String userService, @WebParam(name = "passService") String passService,
      @WebParam(name = "crNumber") String crNumber);
}
