package com.viettel.gnoc.od.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.od.dto.OdTypeDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 * @author NamTN
 */
@Repository
public interface OdTypeRepository {

  // OdTypeDTO getDetailUserByUsername(String username);

  Datatable search(OdTypeDTO odTypeDTO);

  ResultInSideDto delete(Long odTypeId);

  ResultInSideDto deleteList(List<Long> listOdTypeId);

  ResultInSideDto insertOrUpdateListImport(List<OdTypeDTO> listOdType);

  Datatable getListOdType(OdTypeDTO odTypeDTO);

  Datatable getListDataExport(OdTypeDTO odTypeDTO);

  OdTypeDTO getDetail(Long odTypeId);

  OdTypeDTO checkOdTypeExist(String odTypeCode);

  ResultInSideDto add(OdTypeDTO odTypeDTO);

  ResultInSideDto addList(List<OdTypeDTO> odTypeDTO);

  ResultInSideDto edit(OdTypeDTO odTypeDTO);

  String getSeqOdType(String sequense);
  // ResultInSideDto checkExistOdType(String username, Long userId);
}
