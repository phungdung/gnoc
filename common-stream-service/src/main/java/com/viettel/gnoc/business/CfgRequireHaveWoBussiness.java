package com.viettel.gnoc.business;

import com.viettel.gnoc.commons.dto.CfgRequireHaveWoDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.incident.dto.CatReasonDTO;
import java.util.List;

public interface CfgRequireHaveWoBussiness {

  Datatable getListCfgRequireHaveWo(CfgRequireHaveWoDTO dto);

  CfgRequireHaveWoDTO getDetail(Long id);

  ResultInSideDto insert(CfgRequireHaveWoDTO dto);

  ResultInSideDto update(CfgRequireHaveWoDTO dto);

  ResultInSideDto delete(Long id);

  List<CatReasonDTO> getReasonDTOForTree(CatReasonDTO catReasonDTO);
}
