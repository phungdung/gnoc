package com.viettel.gnoc.mr.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.maintenance.dto.MrHardCDDTO;
import com.viettel.gnoc.wo.dto.WoCdGroupDTO;
import java.io.File;
import java.util.List;

public interface MrHardCdBusiness {

  Datatable getListMrHardCDPage(MrHardCDDTO mrHardCDDTO);

  ResultInSideDto insert(MrHardCDDTO mrHardCDDTO);

  ResultInSideDto update(MrHardCDDTO mrHardCDDTO);

  ResultInSideDto delete(Long hardCDId);

  MrHardCDDTO getDetail(Long hardCDId);

  List<WoCdGroupDTO> getWoCdGroupCBB();

  File exportSearchData(MrHardCDDTO mrHardCDDTO) throws Exception;

}
