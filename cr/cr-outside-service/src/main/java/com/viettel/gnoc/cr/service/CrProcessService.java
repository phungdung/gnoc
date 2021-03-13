package com.viettel.gnoc.cr.service;

import com.viettel.gnoc.cr.dto.CrProcessDTO;
import com.viettel.gnoc.cr.dto.CrProcessInsideDTO;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService(name = "CrProcessService")
public interface CrProcessService {

  @WebMethod(operationName = "synchCrProcess")
  public List<CrProcessDTO> synchCrProcess(
      @WebParam(name = "lstImpactSegment") List<Long> lstImpactSegment);

  @WebMethod(operationName = "getAllCrProcess")
  public List<CrProcessDTO> getAllCrProcess(
      @WebParam(name = "parentId") Long parentId);

}
