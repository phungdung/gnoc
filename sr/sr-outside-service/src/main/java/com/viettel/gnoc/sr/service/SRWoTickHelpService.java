package com.viettel.gnoc.sr.service;

import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.od.dto.ObjKeyValueVsmartDTO;
import com.viettel.gnoc.sr.dto.SRDTO;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService(serviceName = "SRWoTickHelpService")
public interface SRWoTickHelpService {

  @WebMethod(operationName = "getListSRForWOTHVSmart")
  public List<SRDTO> getListSRForWOTHVSmart(@WebParam(name = "srDTO") SRDTO srDTO,
      @WebParam(name = "woId") String woId);

  @WebMethod(operationName = "getDetailSRForWOTHVSmart")
  public SRDTO getDetailSRForWOTHVSmart(@WebParam(name = "srId") String srId);

  @WebMethod(operationName = "createSRForWOTHVSmart")
  public ResultDTO createSRForWOTHVSmart(
      @WebParam(name = "lstObjKeyValueVsmartDTO") List<ObjKeyValueVsmartDTO> lstObjKeyValueVsmartDTO,
      @WebParam(name = "createUser") String createUser,
      @WebParam(name = "serviceCode") String serviceCode);
}
