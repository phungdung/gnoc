package com.viettel.gnoc.wo.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.incident.dto.WoSupportDTO;
import com.viettel.gnoc.wo.dto.CfgSupportForm;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface WoSupportRepository {

  List<CfgSupportForm> listWoSupportInfo(Long woId);

  ResultInSideDto insertWoSupport(WoSupportDTO woSupportDTO);

  ResultInSideDto insertListWoSupport(List<WoSupportDTO> lstWoSupportDTO);
}
