package com.viettel.gnoc.common.service;

import com.viettel.gnoc.commons.dto.MrLocationDTO;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * @author tripm
 * @version 2.0
 * @since 23/06/2020 15:00:00
 */
@WebService(serviceName = "CatLocationService")
public interface CatLocationService {

  @WebMethod(operationName = "getListLocationByStationCode")
  public List<MrLocationDTO> getListLocationByStationCode(
      @WebParam(name = "stationCode") String stationCode);
}
