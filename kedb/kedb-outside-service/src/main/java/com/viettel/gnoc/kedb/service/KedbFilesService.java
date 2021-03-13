package com.viettel.gnoc.kedb.service;

import com.viettel.gnoc.ws.dto.KedbFilesDTO;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * @author TrungDuong
 */
@WebService(serviceName = "KedbFilesService")
@XmlSeeAlso({KedbFilesDTO.class})
public interface KedbFilesService {

  @WebMethod(operationName = "getListKedbFilesDTO")
  public List<KedbFilesDTO> getListKedbFilesDTO(
      @WebParam(name = "kedbFilesDTO") com.viettel.gnoc.ws.dto.KedbFilesDTO kedbFilesDTO,
      @WebParam(name = "rowStart") int rowStart, @WebParam(name = "maxRow") int maxRow,
      @WebParam(name = "sortType") String sortType,
      @WebParam(name = "sortFieldList") String sortFieldList);

}
