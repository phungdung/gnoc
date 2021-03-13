package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.maintenance.dto.MrHardCDDTO;
import com.viettel.gnoc.wo.dto.WoCdGroupDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface MrHardCdRepository {

  Datatable getListMrHardCDPage(MrHardCDDTO mrHardCDDTO);

  ResultInSideDto insertOrUpdate(MrHardCDDTO mrHardCDDTO);

  ResultInSideDto delete(Long hardCDId);

  MrHardCDDTO getDetail(Long hardCDId);

  List<WoCdGroupDTO> getWoCdGroupCBB();

  List<MrHardCDDTO> getListDataExport(MrHardCDDTO mrHardCDDTO);
}
