package com.viettel.gnoc.cr.service;

import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.cr.dto.CrDTO;
import com.viettel.gnoc.cr.dto.CrFilesAttachDTO;
import com.viettel.gnoc.wo.dto.WoDTO;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService(serviceName = "CrAutoServiceForSR")
public interface CrAutoServiceForSR {

  @WebMethod(operationName = "insertAutoCrForSR")
  public ResultDTO insertAutoCrForSR(@WebParam(name = "crDTO") CrDTO crDTO,
      @WebParam(name = "lstFile") List<CrFilesAttachDTO> lstFile,
      @WebParam(name = "system") String system,
      @WebParam(name = "nationCode") String nationCode,
      @WebParam(name = "lstWo") List<WoDTO> lstWo,
      @WebParam(name = "lstMop") List<String> lstMop,
      @WebParam(name = "lstNodeIp") List<String> lstNodeIp,
      @WebParam(name = "userService") String userService,
      @WebParam(name = "passService") String passService);

  @WebMethod(operationName = "getCrNumber")
  public String getCrNumber(@WebParam(name = "crProcessId") String crProcessId,
      @WebParam(name = "userService") String userService,
      @WebParam(name = "passService") String passService);
}
