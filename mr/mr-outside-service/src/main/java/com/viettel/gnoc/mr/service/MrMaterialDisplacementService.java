package com.viettel.gnoc.mr.service;

import com.viettel.gnoc.ws.dto.MrMaterialDTO;
import com.viettel.gnoc.ws.dto.MrMaterialDisplacementDTO;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * @author TrungDuong
 */
@WebService(serviceName = "MrMaterialDisplacementService")
public interface MrMaterialDisplacementService {

  @WebMethod(operationName = "getListMrMaterialDTO2")
  public List<MrMaterialDTO> getListMrMaterialDTO2(
      @WebParam(name = "mrMaterialDTO") MrMaterialDTO mrMaterialDTO,
      @WebParam(name = "userManager") String userManager, @WebParam(name = "woCode") String woCode,
      @WebParam(name = "rowStart") int rowStart, @WebParam(name = "maxRow") int maxRow);

  @WebMethod(operationName = "insertOrUpdateListMrMaterialDisplacement")
  public String insertOrUpdateListMrMaterialDisplacement(
      @WebParam(name = "mrMaterialDisplacementDTO") List<MrMaterialDisplacementDTO> mrMaterialDisplacementDTO);

  @WebMethod(operationName = "getListMrMaterialDisplacementDTO2")
  public List<MrMaterialDisplacementDTO> getListMrMaterialDisplacementDTO2(
      @WebParam(name = "mrMaterialDisplacementDTO") MrMaterialDisplacementDTO mrMaterialDisplacementDTO,
      @WebParam(name = "rowStart") int rowStart, @WebParam(name = "maxRow") int maxRow,
      @WebParam(name = "lstExclude") List<MrMaterialDisplacementDTO> lstExclude);
}
