package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.maintenance.dto.MrScheduleTelHisDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface MrScheduleTelHisSoftRepository {

  Datatable getListDataSoftSearchWeb(MrScheduleTelHisDTO mrScheduleTelHisDTO);

  List<MrScheduleTelHisDTO> getListMrScheduleTelHisSoftExport(
      MrScheduleTelHisDTO mrScheduleTelHisDTO);
}
