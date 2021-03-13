package com.viettel.gnoc.od.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.od.dto.OdTypeDetailDTO;
import java.util.List;

/**
 * @author NamTN
 */
public interface OdTypeDetailBusiness {


  Datatable getListOdTypeDetailPage(OdTypeDetailDTO odTypeDetailDTO);

  Datatable getListOdTypeDetail(OdTypeDetailDTO odTypeDetailDTO);

  OdTypeDetailDTO checkOdTypeDetailExist(Long odTypeId, Long priorityId);

  List<OdTypeDetailDTO> getListOdTypeDetailByOdTypeId(Long odTypeId);

  ResultInSideDto insertOrUpdateListOdTypeDetail(List<OdTypeDetailDTO> detailDTOList);

  ResultInSideDto updateListOdTypeDetail(List<OdTypeDetailDTO> detailDTOList);

  ResultInSideDto deleteOdTypeDetail(List<OdTypeDetailDTO> lstOdTypeDetailDTO, Long odTypeId);
}
