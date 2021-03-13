package com.viettel.gnoc.mr.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.maintenance.dto.MrScheduleTelHisDTO;
import java.io.File;

public interface MrScheduleTelHisSoftBusiness {

  Datatable getListDataSoftSearchWeb(MrScheduleTelHisDTO mrScheduleTelHisDTO);

  File exportDataMrScheduleTelHisSoft(MrScheduleTelHisDTO mrScheduleTelHisDTO) throws Exception;
}
