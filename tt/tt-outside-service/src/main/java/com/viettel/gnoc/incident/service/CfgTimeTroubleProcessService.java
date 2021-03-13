package com.viettel.gnoc.incident.service;

import com.viettel.gnoc.incident.dto.CfgTimeTroubleProcessDTO;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService(serviceName = "CfgTimeTroubleProcessService")
public interface CfgTimeTroubleProcessService {

  @WebMethod(operationName = "getListCfgTimeTroubleProcessDTO")
  public List<CfgTimeTroubleProcessDTO> getListCfgTimeTroubleProcessDTO(
      @WebParam(name = "cfgTimeTroubleProcessDTO") CfgTimeTroubleProcessDTO cfgTimeTroubleProcessDTO);

}
