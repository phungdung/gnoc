package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.maintenance.dto.MrDeviceCDDTO;
import com.viettel.gnoc.maintenance.model.MrDeviceCDEntity;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface MrDeviceCDRepository {

  List<MrDeviceCDDTO> getComboboxDeviceType();

  List<MrDeviceCDDTO> getComboboxStationCode();

  Datatable onSearch(MrDeviceCDDTO mrDeviceCDDTO);

  List<MrDeviceCDDTO> exportSearchData(MrDeviceCDDTO mrDeviceCDDTO);

  List<MrDeviceCDDTO> getListMrDeviceCDDTO(MrDeviceCDDTO mrDeviceCDDTO, int rowStart, int maxRow,
      String sortType, String sortFieldList);

  String insertOrUpdateListMrDeviceCD(List<MrDeviceCDDTO> lstData);

  MrDeviceCDEntity findMrDeviceById(Long mrId);

  ResultInSideDto insertOrUpdate(MrDeviceCDEntity mrDeviceCDEntity);
}
