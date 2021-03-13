package com.viettel.gnoc.commons.business;

import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.CfgCreateWoDTO;
import com.viettel.gnoc.commons.dto.CfgCreateWoInsiteDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import java.util.List;

/**
 * @author TienNV
 */
public interface CfgCreateWoBusiness {

  Datatable getListCfgCreateWoDTOPage(CfgCreateWoInsiteDTO cfgCreateWoDTO);

  CfgCreateWoInsiteDTO getDetailById(Long id);

  List<CatItemDTO> getCmbArrayIncident();

  List<CatItemDTO> getCmbAlarmGroup(Long ItemId);

  List<CfgCreateWoDTO> getListCfgCreateWoDTO(CfgCreateWoDTO cfgCreateWoDTO, int rowStart,
      int maxRow, String sortType, String sortFieldList);

  ResultInSideDto insertOrUpdateCfgCreateWo(CfgCreateWoInsiteDTO cfgCreateWoDTO);

  ResultInSideDto deleteCfgCreateWo(Long id);
}
