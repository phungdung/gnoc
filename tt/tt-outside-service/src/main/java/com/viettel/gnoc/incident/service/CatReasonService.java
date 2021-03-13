package com.viettel.gnoc.incident.service;

import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.incident.dto.CatReasonDTO;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService(serviceName = "CatReasonService")
public interface CatReasonService {

  @WebMethod(operationName = "getReasonDTOForTreeByTroubleCode")
  public List<CatReasonDTO> getReasonDTOForTreeByTroubleCode(
      @WebParam(name = "isRoot") Boolean isRoot,
      @WebParam(name = "troubleCode") String troubleCode,
      @WebParam(name = "parentId") String parentId);

  @WebMethod(operationName = "getReasonDTOForVsmart")
  public List<CatReasonDTO> getReasonDTOForVsmart(
      @WebParam(name = "troubleCode") String troubleCode,
      @WebParam(name = "parentId") String parentId,
      @WebParam(name = "level") int level);

  @WebMethod
  public ResultDTO getReasonDTO(
      @WebParam(name = "fromDate") String fromDate,
      @WebParam(name = "toDate") String toDate);

}
