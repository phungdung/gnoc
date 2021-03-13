package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.maintenance.dto.SearchDeviceNIMSDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface SearchDeviceNIMSRepository {

  Datatable getListSearchDeviceNIMS(SearchDeviceNIMSDTO searchDeviceNIMSDTO);

  List<SearchDeviceNIMSDTO> getComboboxNetworkClass();

  List<SearchDeviceNIMSDTO> getComboboxNetworkType();

  List<SearchDeviceNIMSDTO> onSearchExport(SearchDeviceNIMSDTO searchDeviceNIMSDTO);
}
