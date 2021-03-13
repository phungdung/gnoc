package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.maintenance.dto.MrDeviceDTO;
import com.viettel.gnoc.maintenance.dto.MrHardDevicesCheckListDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface MrHardDevicesCheckListRepository {

  Datatable onSearch(MrHardDevicesCheckListDTO mrCheckListDTO);

  List<MrHardDevicesCheckListDTO> onSearchExport(MrHardDevicesCheckListDTO mrCheckListDTO);

  ResultInSideDto insertOrUpdate(MrHardDevicesCheckListDTO mrCheckListDTO);

  String insertOrUpdateList(List<MrHardDevicesCheckListDTO> listDTO);

  ResultInSideDto deleteMrCheckList(Long checkListId);

  MrHardDevicesCheckListDTO getDetail(Long checkListId);

  List<MrHardDevicesCheckListDTO> getListArrayDeviceTypeNetworkType();

  List<MrHardDevicesCheckListDTO> checkListDTOExisted(MrHardDevicesCheckListDTO mrCheckListDTO);

  List<MrDeviceDTO> getListNetworkType();

  List<MrDeviceDTO> getListDeviceType();
}
