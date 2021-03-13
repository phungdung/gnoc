package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.maintenance.dto.MrCheckListDTO;
import com.viettel.gnoc.maintenance.dto.MrDeviceDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface MrCheckListRepository {

  Datatable onSearch(MrCheckListDTO mrCheckListDTO);

  List<MrCheckListDTO> onSearchExport(MrCheckListDTO mrCheckListDTO);

  ResultInSideDto insertOrUpdate(MrCheckListDTO mrCheckListDTO);

  String insertOrUpdateList(List<MrCheckListDTO> listDTO);

  ResultInSideDto deleteMrCheckList(Long checkListId);

  MrCheckListDTO getDetail(Long checkListId);

  List<MrCheckListDTO> getListArrayDeviceTypeNetworkType();

  List<MrCheckListDTO> checkListDTOExisted(MrCheckListDTO mrCheckListDTO);

  List<MrDeviceDTO> getListNetworkType();

  List<MrDeviceDTO> getListDeviceType();
}
