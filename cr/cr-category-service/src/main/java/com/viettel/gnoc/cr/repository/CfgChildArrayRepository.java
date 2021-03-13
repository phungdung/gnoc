package com.viettel.gnoc.cr.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.cr.dto.CfgChildArrayDTO;
import com.viettel.gnoc.cr.dto.ImpactSegmentDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 * @author DungPV
 */
@Repository
public interface CfgChildArrayRepository {

  Datatable getListCfgChildArray(CfgChildArrayDTO cfgChildArrayDTO);

  List<ImpactSegmentDTO> getListImpactSegmentCBB();

  ResultInSideDto delete(Long childrenId);

  CfgChildArrayDTO getDetail(Long childrenId);

  ResultInSideDto addOrUpdate(CfgChildArrayDTO cfgChildArrayDTO);

  List<CfgChildArrayDTO> getCbbChildArray(CfgChildArrayDTO dto);
}
