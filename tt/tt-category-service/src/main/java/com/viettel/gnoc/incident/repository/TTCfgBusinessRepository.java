package com.viettel.gnoc.incident.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.incident.dto.TTCfgBusinessDTO;
import com.viettel.gnoc.incident.dto.TTChangeStatusDTO;
import java.util.List;

public interface TTCfgBusinessRepository {

  List<TTCfgBusinessDTO> getListTTCfgBusiness(TTChangeStatusDTO ttChangeStatusDTO,boolean ws);

  ResultInSideDto deleteListTTCfgBusiness(Long ttChangeStatusId);

  ResultInSideDto insertTTCfgBusiness(TTCfgBusinessDTO ttCfgBusinessDTO);
}
