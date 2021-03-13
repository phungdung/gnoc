package com.viettel.gnoc.common.service;

import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.kedb.dto.AuthorityDTO;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * @author TrungDuong
 */
@WebService(serviceName = "UnitService")
@XmlSeeAlso({com.viettel.gnoc.ws.dto.UnitDTO.class})
public interface UnitService {

  @WebMethod
  public ResultDTO getUnitDTO(@WebParam(name = "requestDTO") AuthorityDTO requestDTO, @WebParam(name = "fromDate") String fromDate, @WebParam(name = "toDate") String toDate);

}
