package com.viettel.gnoc.sr.service;

import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.sr.dto.SRConfigDTO;
import com.viettel.gnoc.sr.dto.SRCreatedFromOtherSysDTO;
import com.viettel.gnoc.sr.dto.SRDTO;
import com.viettel.gnoc.sr.dto.SRWorkLogDTO;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService(serviceName = "SROutSideService")
public interface SROutSideService {

  //VIPA
  @WebMethod(operationName = "putResultFromVipa")
  public ResultDTO putResultFromVipa(@WebParam(name = "srId") String srId
      , @WebParam(name = "result") String result
      , @WebParam(name = "fileContentError") String fileContentError);

  //QLCTKT or CM
  @WebMethod(operationName = "createSRFromOtherSys")
  public ResultDTO createSRFromOtherSys(
      @WebParam(name = "srCreatedFromOtherSysDTO") SRCreatedFromOtherSysDTO srCreatedFromOtherSysDTO);

  //KHDN
  @WebMethod(operationName = "createSRByConfigGroup")
  public ResultDTO createSRByConfigGroup(@WebParam(name = "srInputDTO") SRDTO srInputDTO,
      @WebParam(name = "configGroup") String configGroup);

  @WebMethod(operationName = "getListSRForLinkCR")
  public List<SRDTO> getListSRForLinkCR(@WebParam(name = "loginUser") String loginUser,
      @WebParam(name = "srCode") String srCode);

  @WebMethod(operationName = "getListSR")
  public List<SRDTO> getListSR(@WebParam(name = "srDTO") SRDTO dto,
      @WebParam(name = "rowStart") int rowStart, @WebParam(name = "maxRow") int maxRow);

  @WebMethod(operationName = "deleteSR")
  public String deleteSR(@WebParam(name = "srId") Long srId);

  @WebMethod(operationName = "getCrNumberCreatedFromSR")
  public List<SRDTO> getCrNumberCreatedFromSR(@WebParam(name = "srDTO") SRDTO dto,
      @WebParam(name = "rowStart") int rowStart, @WebParam(name = "maxRow") int maxRow);

  @WebMethod(operationName = "getDetailSR")
  public SRDTO getDetailSR(@WebParam(name = "srId") String srId
      , @WebParam(name = "userId") Long userId);

  @WebMethod(operationName = "updateSR")
  public ResultDTO updateSR(@WebParam(name = "srDTO") SRDTO dto);

  @WebMethod(operationName = "insertSRWorklog")
  public ResultDTO insertSRWorklog(@WebParam(name = "SRWorkLogDTO") SRWorkLogDTO srWorklogDTO);

  @WebMethod(operationName = "getListGnocFileForSR")
  public List<GnocFileDto> getListGnocFileForSR(
      @WebParam(name = "GnocFileDto") GnocFileDto gnocFileDto);

  @WebMethod(operationName = "updateStatusSRForProcess")
  public String updateStatusSRForProcess(
      @WebParam(name = "srId") String srId,
      @WebParam(name = "status") String status);

  @WebMethod(operationName = "getByConfigGroup")
  public List<SRConfigDTO> getByConfigGroup(
      @WebParam(name = "configGroup") String configGroup);
}
