package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.maintenance.dto.MrCDCheckListBDDTO;
import com.viettel.gnoc.maintenance.dto.MrDeviceCDDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface MrCDCheckListBDRepository {

  Datatable getListMrCDCheckListBDPage(MrCDCheckListBDDTO mrCDCheckListBDDTO);

  ResultInSideDto insertOrUpdate(MrCDCheckListBDDTO mrCDCheckListBDDTO);

  ResultInSideDto delete(Long checkListId);

  MrCDCheckListBDDTO getDetail(Long checkListId);

  List<CatItemDTO> getComboboxArray();

  List<MrDeviceCDDTO> getDeviceTypeCbb(String arrayCode);

  List<CatItemDTO> getComboboxActivities(Long itemId);

  List<MrCDCheckListBDDTO> getListMrCDCheckListBDDTO(MrCDCheckListBDDTO cDCheckListBDDTO,
      boolean isCheckDup);

  List<MrCDCheckListBDDTO> getListAll(MrCDCheckListBDDTO mrCDCheckListBDDTO);

  List<MrCDCheckListBDDTO> findDTO(MrCDCheckListBDDTO mrCDCheckListBDDTO);

  List<MrCDCheckListBDDTO> checkThreeParams(MrCDCheckListBDDTO mrCDCheckListBDDTO);
}
