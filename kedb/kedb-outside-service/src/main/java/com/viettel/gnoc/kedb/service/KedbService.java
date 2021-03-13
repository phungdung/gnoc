package com.viettel.gnoc.kedb.service;

import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * @author TrungDuong
 */
@WebService(serviceName = "KedbService")
@XmlSeeAlso({com.viettel.gnoc.ws.dto.KedbDTO.class})
public interface KedbService {

  @WebMethod(operationName = "synchKedbByCreateTime")
  public List<com.viettel.gnoc.ws.dto.KedbDTO> synchKedbByCreateTime(
      @WebParam(name = "fromDate") String fromDate, @WebParam(name = "toDate") String toDate);

}
