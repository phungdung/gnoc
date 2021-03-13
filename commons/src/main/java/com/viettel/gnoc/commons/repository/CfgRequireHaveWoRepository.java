package com.viettel.gnoc.commons.repository;

import com.viettel.gnoc.commons.dto.CfgRequireHaveWoDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.incident.dto.CatReasonDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 * @author TienNV
 */
@Repository
public interface CfgRequireHaveWoRepository {

  List<CfgRequireHaveWoDTO> getListCfgRequireHaveWoDTO(CfgRequireHaveWoDTO cfgRequireHaveWoDTO,
      int rowStart, int maxRow, String sortType, String sortFieldList);

  Datatable getListCfgRequireHaveWo(CfgRequireHaveWoDTO dto);

  CfgRequireHaveWoDTO getDetail(Long id);

  ResultInSideDto insert(CfgRequireHaveWoDTO dto);

  ResultInSideDto update(CfgRequireHaveWoDTO dto);

  ResultInSideDto delete(Long id);

  List<CatReasonDTO> getReasonDTOForTree(CatReasonDTO catReasonDTO);
}
