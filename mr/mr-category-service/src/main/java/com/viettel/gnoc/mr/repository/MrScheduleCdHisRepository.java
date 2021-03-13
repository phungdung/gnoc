package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.maintenance.dto.MrScheduleCdHisDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface MrScheduleCdHisRepository {

  Datatable getListMrScheduleCdHis(MrScheduleCdHisDTO mrScheduleCdHisDTO);

  List<MrScheduleCdHisDTO> onSearchExport(MrScheduleCdHisDTO mrScheduleCdHisDTO);

  ResultInSideDto insertOrUpdate(MrScheduleCdHisDTO dto);
}
