package com.viettel.gnoc.incident.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.incident.dto.CfgSupportFormDTO;
import org.springframework.stereotype.Repository;

@Repository
public interface TroublesTickHelpRepository {

  Datatable getListInfoTickHelpByWoCode(CfgSupportFormDTO dto);
}
