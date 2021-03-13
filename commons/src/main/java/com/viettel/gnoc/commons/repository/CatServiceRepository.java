package com.viettel.gnoc.commons.repository;

import com.viettel.gnoc.commons.dto.CatServiceDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface CatServiceRepository {

  List<CatServiceDTO> getServiceByInfraType(Long infraType);

  List<CatServiceDTO> getListCatServiceCBB();

  Datatable getItemServiceMaster(String system, String business,
      String idColName, String nameCol);

  Long getServiceIdByCcServiceId(String ccServiceId);
}
