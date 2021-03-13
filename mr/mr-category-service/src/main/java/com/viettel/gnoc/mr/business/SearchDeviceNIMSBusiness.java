package com.viettel.gnoc.mr.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.maintenance.dto.SearchDeviceNIMSDTO;
import java.io.File;
import java.util.List;

public interface SearchDeviceNIMSBusiness {

  Datatable getListSearchDeviceNIMS(SearchDeviceNIMSDTO searchDeviceNIMSDTO);

  List<SearchDeviceNIMSDTO> getComboboxNetworkClass();

  List<SearchDeviceNIMSDTO> getComboboxNetworkType();

  File exportSearchData(SearchDeviceNIMSDTO searchDeviceNIMSDTO) throws Exception;
}
