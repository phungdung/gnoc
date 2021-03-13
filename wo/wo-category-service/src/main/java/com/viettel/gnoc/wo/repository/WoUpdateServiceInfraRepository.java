package com.viettel.gnoc.wo.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.wo.dto.WoUpdateServiceInfraDTO;
import org.springframework.stereotype.Repository;

@Repository
public interface WoUpdateServiceInfraRepository {

  Datatable getListWoUpdateServiceInfraPage(WoUpdateServiceInfraDTO woUpdateServiceInfraDTO);

}
