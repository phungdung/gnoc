package com.viettel.gnoc.sr.service;


import com.viettel.gnoc.sr.dto.SRCatalogDTO;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService(serviceName = "SRCatalogService")
public interface SRCatalogService {


  @WebMethod(operationName = "getDetailSRCatalog")
  public SRCatalogDTO getDetailSRCatalog(@WebParam(name = "serviceId") String serviceId);

  @WebMethod(operationName = "getListCatalog")
  public List<SRCatalogDTO> getListCatalog(
      @WebParam(name = "srCatalogDTO") SRCatalogDTO srCatalogDTO);
}
