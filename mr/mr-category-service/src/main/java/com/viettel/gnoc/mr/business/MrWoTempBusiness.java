package com.viettel.gnoc.mr.business;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.maintenance.dto.MrWoTempDTO;
import java.util.List;

public interface MrWoTempBusiness {

  List<MrWoTempDTO> getListMrWoTempDTO(MrWoTempDTO temp, int i, int i1, String asc,
      String woSystemId);

  ResultInSideDto updateMrWoTemp(MrWoTempDTO woTemp);
}
