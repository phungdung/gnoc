package com.viettel.gnoc.common.service;

import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.ws.dto.CatItemDTO;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * @author TrungDuong
 */
@WebService(serviceName = "CatItemService")
@XmlSeeAlso({com.viettel.gnoc.ws.dto.CatItemDTO.class})
public interface CatItemService {

  @WebMethod(operationName = "getListCateItem")
  public ResultDTO getListCateItem(@WebParam(name = "category") String category,
      @WebParam(name = "fromDate") String fromDate,
      @WebParam(name = "toDate") String toDate);

  /**
   * @author tripm
   */
  @WebMethod(operationName = "getListCatItemDTO")
  public List<CatItemDTO> getListCatItemDTO(@WebParam(name = "catItemDTO") CatItemDTO catItemDTO,
      @WebParam(name = "rowStart") int rowStart, @WebParam(name = "maxRow") int maxRow,
      @WebParam(name = "sortType") String sortType,
      @WebParam(name = "sortFieldList") String sortFieldList);
}
