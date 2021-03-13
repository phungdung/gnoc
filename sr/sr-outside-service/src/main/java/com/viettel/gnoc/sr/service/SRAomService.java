package com.viettel.gnoc.sr.service;

import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.sr.dto.SRDTO;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;

@WebService(serviceName = "SRAomService")
@XmlSeeAlso({SRDTO.class})
public interface SRAomService {

  @WebMethod(operationName = "getListSRForGatePro")
  public ResultDTO getListSRForGatePro(@WebParam(name = "fromDate") String fromDate,
      @WebParam(name = "toDate") String toDate);

  @WebMethod(operationName = "updateSRForGatePro")
  public ResultDTO updateSRForGatePro(@WebParam(name = "srCode") String srCode,
      @WebParam(name = "status") String status, @WebParam(name = "fileContent") String fileContent);

  //  public SRDTO test();
}
