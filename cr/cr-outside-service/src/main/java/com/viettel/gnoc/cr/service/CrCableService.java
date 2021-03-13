package com.viettel.gnoc.cr.service;

import com.viettel.gnoc.cr.dto.CrCableDTO;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService(name = "CrCableService")
public interface CrCableService {

  @WebMethod(operationName = "getListCrCableDTO")
  public List<CrCableDTO> getListCrCableDTO(@WebParam(name = "crCableDTO") CrCableDTO crCableDTO,
      @WebParam(name = "rowStart") int rowStart, @WebParam(name = "maxRow") int maxRow,
      @WebParam(name = "sortType") String sortType,
      @WebParam(name = "sortFieldList") String sortFieldList);

}
