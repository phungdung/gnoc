package com.viettel.gnoc.mr.business;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.maintenance.dto.MrHisDTO;

public interface MrHisServiceBusiness {

  ResultInSideDto insertMrHis(MrHisDTO hisDto);
}
