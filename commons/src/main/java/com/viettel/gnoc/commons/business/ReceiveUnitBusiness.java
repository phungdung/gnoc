package com.viettel.gnoc.commons.business;


import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ReceiveUnitDTO;

public interface ReceiveUnitBusiness {

  Datatable getListReceiveUnitSearch(ReceiveUnitDTO receiveUnitDTO);

}
