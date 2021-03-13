package com.viettel.gnoc.cr.repository;

import com.viettel.gnoc.cr.dto.CrProcessDTO;
import com.viettel.gnoc.cr.dto.CrProcessInsideDTO;
import com.viettel.gnoc.cr.dto.ItemDataCR;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import java.util.List;

public interface CrProcessRepository {

  CrProcessInsideDTO findCrProcessById(Long id);

  List<CrProcessInsideDTO> findCrProcessIdByTDTT(String id);

  List<ItemDataCRInside> getListCrProcessCBB(CrProcessInsideDTO crProcessDTO);

  List<CrProcessInsideDTO> getListCrProcessLevel3CBB(CrProcessInsideDTO crProcessDTO);

  List<CrProcessDTO> synchCrProcess(List<Long> lstImpactSegment);

  List<ItemDataCR> getListCrProcessCBB(CrProcessDTO form, String locale);
}
