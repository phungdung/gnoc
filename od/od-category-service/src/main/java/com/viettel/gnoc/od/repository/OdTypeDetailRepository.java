package com.viettel.gnoc.od.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.od.dto.OdTypeDetailDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 * @author NamTN
 */
@Repository
public interface OdTypeDetailRepository {

  Datatable getListOdTypeDetailPage(OdTypeDetailDTO odTypeDetailDTO);

  Datatable getListOdTypeDetail(OdTypeDetailDTO odTypeDetailDTO);

  OdTypeDetailDTO checkOdTypeDetailExist(Long odTypeId, Long priorityId);

  List<OdTypeDetailDTO> getListOdTypeDetailByOdTypeId(Long odTypeId);

  ResultInSideDto add(OdTypeDetailDTO odTypeDetailDTO);

  ResultInSideDto insertOrUpdateListOdTypeDetail(List<OdTypeDetailDTO> detailDTOList);

  ResultInSideDto edit(OdTypeDetailDTO odTypeDetailDTO);

  ResultInSideDto updateListOdTypeDetail(List<OdTypeDetailDTO> detailDTOList);

  ResultInSideDto delete(Long id);

  ResultInSideDto deleteOdTypeDetail(List<OdTypeDetailDTO> lstOdTypeDetailDTO, Long odTypeId);
}
