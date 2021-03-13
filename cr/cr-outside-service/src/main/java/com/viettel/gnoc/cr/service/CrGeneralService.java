package com.viettel.gnoc.cr.service;

import com.viettel.gnoc.cr.dto.ObjResponse;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService(serviceName = "CrGeneralService")
public interface CrGeneralService {

  // anhlp add
  @WebMethod(operationName = "doLogin")
  public ObjResponse doLogin(@WebParam(name = "versionApp") String versionApp,
      @WebParam(name = "locale") String locale, @WebParam(name = "userName") String userName,
      @WebParam(name = "password") String password);

  // anhlp adds
  @WebMethod(operationName = "doLoginV2")
  public ObjResponse doLoginV2(@WebParam(name = "versionApp") String versionApp,
      @WebParam(name = "locale") String locale, @WebParam(name = "userName") String userName,
      @WebParam(name = "password") String password);
}
