package com.viettel.gnoc.mr.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.maintenance.dto.MrScheduleCdHisDTO;
import java.io.File;

public interface MrScheduleCdHisBusiness {

  Datatable searchMrScheduleCdHisDTOs(MrScheduleCdHisDTO mrScheduleCdHisDTO);

  File exportSearchData(MrScheduleCdHisDTO mrScheduleCdHisDTO) throws Exception;
}
