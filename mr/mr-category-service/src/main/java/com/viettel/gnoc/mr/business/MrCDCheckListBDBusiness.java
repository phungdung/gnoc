package com.viettel.gnoc.mr.business;

import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.maintenance.dto.MrCDCheckListBDDTO;
import com.viettel.gnoc.maintenance.dto.MrDeviceCDDTO;
import java.io.File;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface MrCDCheckListBDBusiness {

  Datatable getListMrCDCheckListBDPage(MrCDCheckListBDDTO mrCDCheckListBDDTO);

  ResultInSideDto insert(MrCDCheckListBDDTO mrCDCheckListBDDTO);

  ResultInSideDto update(MrCDCheckListBDDTO mrCDCheckListBDDTO);

  ResultInSideDto delete(Long checkListId);

  MrCDCheckListBDDTO getDetail(Long checkListId);

  List<CatItemDTO> getComboboxArray();

  List<MrDeviceCDDTO> getDeviceTypeCbb(String arrayCode);

  List<CatItemDTO> getComboboxActivities(Long itemId);

  ResultInSideDto importMrCDCheckListBD(MultipartFile fileImport) throws Exception;

  File exportData(MrCDCheckListBDDTO mrCDCheckListBDDTO) throws Exception;

  File getTemplate() throws Exception;

  List<MrCDCheckListBDDTO> getListMrCDCheckListBDDTO(MrCDCheckListBDDTO cDCheckListBDDTO);

}
