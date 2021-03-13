package com.viettel.gnoc.cr.service;

import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.cr.dto.CrDTO;
import com.viettel.gnoc.cr.dto.CrFilesAttachDTO;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService(serviceName = "CrAutoService")
public interface CrAutoService {

  @WebMethod(operationName = "insertAutoCr")
  public ResultDTO insertAutoCr(@WebParam(name = "crDTO") CrDTO crDTO,
      @WebParam(name = "lstFile") List<CrFilesAttachDTO> lstFile,
      @WebParam(name = "system") String system,
      @WebParam(name = "nationCode") String nationCode,
      @WebParam(name = "ftId") String ftId,
      @WebParam(name = "userService") String userService,
      @WebParam(name = "passService") String passService);

  @WebMethod(operationName = "actionCloseCr")
  public String actionCloseAutoCr(@WebParam(name = "crDTO") CrDTO crDTO,
      @WebParam(name = "userService") String userService,
      @WebParam(name = "passService") String passService);

  @WebMethod(operationName = "actionResolveCr")
  public String actionResolveAutoCr(@WebParam(name = "crDTO") CrDTO crDTO,
      @WebParam(name = "userService") String userService,
      @WebParam(name = "passService") String passService);

  @WebMethod(operationName = "getCrNumber")
  public String getCrNumber(@WebParam(name = "crProcessId") String crProcessId,
      @WebParam(name = "userService") String userService,
      @WebParam(name = "passService") String passService);
}
