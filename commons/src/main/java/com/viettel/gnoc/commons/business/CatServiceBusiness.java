package com.viettel.gnoc.commons.business;


import com.viettel.gnoc.commons.dto.CatServiceDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import java.util.List;


public interface CatServiceBusiness {

  List<CatServiceDTO> getListCatServiceCBB();

  Datatable getItemServiceMaster(String system, String type, String idColName, String nameCol);

  Long getServiceIdByCcServiceId(String ccServiceId);
}
