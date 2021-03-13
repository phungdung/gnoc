package com.viettel.gnoc.common.service;

import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.UsersDTO;
import com.viettel.gnoc.kedb.dto.AuthorityDTO;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * @author TrungDuong
 */
@WebService(serviceName = "UsersService")
@XmlSeeAlso({UsersDTO.class})
public interface UsersService {

  @WebMethod(operationName = "getUserDTO")
  public ResultDTO getUserDTO(@WebParam(name = "requestDTO") AuthorityDTO requestDTO);

}
