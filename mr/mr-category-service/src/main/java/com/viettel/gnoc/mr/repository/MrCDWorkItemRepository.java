package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.maintenance.dto.MrCDWorkItemDTO;
import com.viettel.gnoc.maintenance.dto.MrDeviceCDDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface MrCDWorkItemRepository {

  Datatable getListMrCDWorkItemPage(MrCDWorkItemDTO mrCDWorkItemDTO);

  ResultInSideDto insertOrUpdate(MrCDWorkItemDTO mrCDWorkItemDTO);

  ResultInSideDto delete(Long wiId);

  MrCDWorkItemDTO getDetail(Long wiId);

  List<CatItemDTO> getComboboxArray();

  List<MrDeviceCDDTO> getDeviceTypeCbb(String arrayCode);

  List<CatItemDTO> getComboboxActivities(Long itemId);

  List<MrCDWorkItemDTO> getListMrCDWorkItemDTO(MrCDWorkItemDTO cDWorkItemDTO);
}
