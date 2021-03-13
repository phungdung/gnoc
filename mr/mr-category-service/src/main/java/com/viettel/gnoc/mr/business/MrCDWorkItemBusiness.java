package com.viettel.gnoc.mr.business;

import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.maintenance.dto.MrCDWorkItemDTO;
import com.viettel.gnoc.maintenance.dto.MrDeviceCDDTO;
import java.io.File;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface MrCDWorkItemBusiness {

  Datatable getListMrCDWorkItemPage(MrCDWorkItemDTO mrCDWorkItemDTO);

  ResultInSideDto insert(MrCDWorkItemDTO mrCDWorkItemDTO);

  ResultInSideDto update(MrCDWorkItemDTO mrCDWorkItemDTO);

  ResultInSideDto delete(Long wiId);

  MrCDWorkItemDTO getDetail(Long wiId);

  List<CatItemDTO> getComboboxArray();

  List<MrDeviceCDDTO> getDeviceTypeCbb(String arrayCode);

  List<CatItemDTO> getComboboxActivities(Long itemId);

  ResultInSideDto importMrCDWorkItem(MultipartFile fileImport) throws Exception;

  File exportData(MrCDWorkItemDTO mrCDWorkItemDTO) throws Exception;

  File getTemplate() throws Exception;

  List<MrCDWorkItemDTO> getListMrCDWorkItemDTO(MrCDWorkItemDTO cDWorkItemDTO);

}
