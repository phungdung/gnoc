package com.viettel.gnoc.mr.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.maintenance.dto.MrScheduleTelHisDTO;
import java.io.File;

public interface MrScheduleTelHisBusiness {

  Datatable getListMrScheduleTelHisPage(MrScheduleTelHisDTO mrScheduleTelHisDTO);

  File exportSearchData(MrScheduleTelHisDTO mrScheduleTelHisDTO) throws Exception;

}
