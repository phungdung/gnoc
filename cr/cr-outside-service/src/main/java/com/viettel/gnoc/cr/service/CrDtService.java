package com.viettel.gnoc.cr.service;

import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.cr.dto.ItemDataCR;
import com.viettel.gnoc.cr.dto.VMSAMopDetailDTO;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService(name = "CrDtService")
public interface CrDtService {

  @WebMethod(operationName = "getAllActiveAffectedService")
  public List<ItemDataCR> getAllActiveAffectedService(
      @WebParam(name = "userService") String userService,
      @WebParam(name = "passService") String passService);

  @WebMethod(operationName = "insertVMSADT")
  public ResultDTO insertVMSADT(
      @WebParam(name = "userService") String userService,
      @WebParam(name = "passService") String passService,
      @WebParam(name = "systemCode") String systemCode,
      @WebParam(name = "crId") Long crId,
      @WebParam(name = "validateKey") Long validateKey,
      @WebParam(name = "createMopSuccess") int createMopSuccess,
      @WebParam(name = "createMopDetail") String createMopDetail,
      @WebParam(name = "mopDTOList") List<VMSAMopDetailDTO> mopDTOList,
      @WebParam(name = "nationCode") String nationCode);
}
