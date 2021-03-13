package com.viettel.gnoc.od.service;

import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.od.dto.ObjKeyValueVsmartDTO;
import com.viettel.gnoc.od.dto.OdChangeStatusForm;
import com.viettel.gnoc.od.dto.OdDTO;
import com.viettel.gnoc.od.dto.OdDTOSearch;
import com.viettel.gnoc.od.dto.OdTypeDTO;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;

@WebService(serviceName = "OdService")
@XmlSeeAlso({OdDTO.class})
public interface OdService {

  @WebMethod(operationName = "getCountListDataSearchForOther")
  public Integer getCountListDataSearchForOther(@WebParam(name = "odDTO") OdDTOSearch odDTO);

  @WebMethod(operationName = "getListDataSearchForOther")
  public List<OdDTOSearch> getListDataSearchForOther(
      @WebParam(name = "odDTOSearch") OdDTOSearch odDTOSearch,
      @WebParam(name = "rowStart") int rowStart, @WebParam(name = "maxRow") int maxRow,
      @WebParam(name = "sortType") String sortType,
      @WebParam(name = "sortFieldList") String sortFieldList);


  @WebMethod(operationName = "getListDataSearchVsmart")
  public List<OdDTOSearch> getListDataSearchVsmart(
      @WebParam(name = "odDTOSearch") OdDTOSearch odDTOSearch);

  @WebMethod(operationName = "insertOdFromVsmart")
  public ResultDTO insertOdFromVsmart(
      @WebParam(name = "lstObjDto") List<ObjKeyValueVsmartDTO> lstObjDto,
      @WebParam(name = "userName") String userName,
      @WebParam(name = "odTypeCode") String odTypeCode,
      @WebParam(name = "woId") String woId,
      @WebParam(name = "insertSource") String insertSource,
      @WebParam(name = "createUnitCode") String createUnitCode,
      @WebParam(name = "createUnitId") String createUnitId
  );

  @WebMethod(operationName = "insertOdFromOtherSystem")
  public ResultDTO insertOdFromOtherSystem(@WebParam(name = "odDTO") OdDTOSearch odDTO);

  @WebMethod(operationName = "changeStatusOd")
  public ResultDTO changeStatusOd(
      @WebParam(name = "odChangeStatusForm") OdChangeStatusForm odChangeStatusForm);

  @WebMethod(operationName = "getInforByODType")
  public OdTypeDTO getInforByODType(@WebParam(name = "odTypeCode") String odTypeCode);
}
