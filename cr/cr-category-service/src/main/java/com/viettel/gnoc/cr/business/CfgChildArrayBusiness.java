package com.viettel.gnoc.cr.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.cr.dto.CfgChildArrayDTO;
import com.viettel.gnoc.cr.dto.ImpactSegmentDTO;
import java.util.List;

/**
 * @author DungPV
 */
public interface CfgChildArrayBusiness {

  Datatable getListCfgChildArray(CfgChildArrayDTO cfgChildArrayDTO);

  List<ImpactSegmentDTO> getListImpactSegmentCBB();

  ResultInSideDto delete(Long childrenId);

  CfgChildArrayDTO getDetail(Long childrenId);

  ResultInSideDto add(CfgChildArrayDTO cfgChildArrayDTO);

  ResultInSideDto update(CfgChildArrayDTO cfgChildArrayDTO);

  List<CfgChildArrayDTO> getCbbChildArray(CfgChildArrayDTO dto);
}
