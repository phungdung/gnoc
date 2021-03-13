package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.maintenance.dto.MrScheduleTelHisDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface MrScheduleTelHisRepository {

  Datatable getListMrScheduleHardHisPage(MrScheduleTelHisDTO mrScheduleTelHisDTO);

  List<MrScheduleTelHisDTO> onSearchExport(MrScheduleTelHisDTO mrScheduleTelHisDTO);

  ResultInSideDto insertScheduleHis(MrScheduleTelHisDTO mrScheduleTelHisDTO);

  ResultInSideDto insertUpdateListScheduleHis(List<MrScheduleTelHisDTO> mrScheduleTelHisDTOs);
}
