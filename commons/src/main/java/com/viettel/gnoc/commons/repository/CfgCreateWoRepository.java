package com.viettel.gnoc.commons.repository;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.CfgCreateWoDTO;
import com.viettel.gnoc.commons.dto.CfgCreateWoInsiteDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 * @author TienNV
 */
@Repository
public interface CfgCreateWoRepository {

  BaseDto sqlSearch(CfgCreateWoInsiteDTO cfgCreateWoDTO);

  Datatable getListCfgCreateWoDTOPage(CfgCreateWoInsiteDTO cfgCreateWoDTO);

  CfgCreateWoInsiteDTO getDetailById(Long id);

  List<CfgCreateWoDTO> getListCfgCreateWoDTO(CfgCreateWoDTO cfgCreateWoDTO, int rowStart,
      int maxRow, String sortType, String sortFieldList);

  ResultInSideDto addOrEditCfgCreate(CfgCreateWoInsiteDTO cfgCreateWoDTO);

  ResultInSideDto deleteCfgCreateWo(Long id);
}
