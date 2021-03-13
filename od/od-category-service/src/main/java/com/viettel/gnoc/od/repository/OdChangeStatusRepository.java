package com.viettel.gnoc.od.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.od.dto.OdChangeStatusDTO;
import com.viettel.gnoc.od.dto.OdExportCfgBusinessDTO;
import com.viettel.gnoc.od.model.OdTypeEntity;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 * @author TienNV
 */
@Repository
public interface OdChangeStatusRepository {

  String getSeqOdChangeStatus();

  ResultInSideDto deleteCfg(Long odChangeStatusId);

  ResultInSideDto deleteListCfg(List<Long> listOdTypeId);

  int deleteList(List<Long> ids);

  String delete(Long id);

  String deleteLocaleList(List<Long> ids);

  ResultInSideDto insertOrUpdate(OdChangeStatusDTO odChangeStatusDTO);

  Datatable getListOdCfgBusiness(OdChangeStatusDTO odChangeStatusDTO);

  List<OdChangeStatusDTO> search(OdChangeStatusDTO odChangeStatusDTO);

  OdChangeStatusDTO findOdChangeStatusById(Long odChangeStatusId);

  OdChangeStatusDTO findOdChangeStatusDTOById(Long odChangeStatusId);

  List<OdExportCfgBusinessDTO> getOdCfgBusinessDataExport(OdChangeStatusDTO odChangeStatusDTO);

  List<OdTypeEntity> findAllOdType();

  OdChangeStatusDTO getOdChangeStatusDTOByParams(String... params);

  boolean isExitOdChangeStatusDTO(OdChangeStatusDTO odChangeStatusDTO);

  String checkConstraint(List<Long> lstCondition);

}
