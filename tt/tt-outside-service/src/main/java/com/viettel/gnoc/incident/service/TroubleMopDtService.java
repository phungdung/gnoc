package com.viettel.gnoc.incident.service;

import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.incident.dto.TroubleMopDtDTO;
import com.viettel.gnoc.kedb.dto.AuthorityDTO;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService(serviceName = "TroubleMopDtService")
public interface TroubleMopDtService {

  @WebMethod(operationName = "insertTroubleMopDt")
  public ResultDTO insertTroubleMopDt(
      @WebParam(name = "troubleMopDtInSideDTO") TroubleMopDtDTO troubleMopDtDTO);

  @WebMethod(operationName = "updateDt")
  public ResultDTO updateDt(@WebParam(name = "requestDTO") AuthorityDTO requestDTO,
      @WebParam(name = "troubleMopDtDTO") TroubleMopDtDTO troubleMopDtDTO);
}
