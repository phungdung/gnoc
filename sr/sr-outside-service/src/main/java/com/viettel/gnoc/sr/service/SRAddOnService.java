package com.viettel.gnoc.sr.service;

import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.sr.dto.SRCatalogDTO;
import com.viettel.gnoc.sr.dto.SRDTO;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService(serviceName = "SRAddOnService")
public interface SRAddOnService {

  @WebMethod(operationName = "getListSRCatalogByConfigGroup")
  public List<SRCatalogDTO> getListSRCatalogByConfigGroup(
      @WebParam(name = "configGroup") String configGroup);

  @WebMethod(operationName = "getListSRByConfigGroup")
  public List<SRDTO> getListSRByConfigGroup(@WebParam(name = "dto") SRDTO dto,
      @WebParam(name = "configGroup") String configGroup);

  @WebMethod(operationName = "createSRByConfigGroup")
  public ResultDTO createSRByConfigGroup(@WebParam(name = "srInputDTO") SRDTO srInputDTO,
      @WebParam(name = "configGroup") String configGroup);
}
