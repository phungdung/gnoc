package com.viettel.gnoc.mr.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.maintenance.dto.MrChecklistsBtsDTO;
import com.viettel.gnoc.maintenance.dto.MrDeviceBtsDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface MrChecklistBtsBusiness {

  Datatable getListDataSearchWeb(MrChecklistsBtsDTO mrChecklistsBtsDTO);

  ResultInSideDto insertMrChecklistBts(MrChecklistsBtsDTO mrChecklistsBtsDTO);

  ResultInSideDto updateMrChecklistBts(MrChecklistsBtsDTO mrChecklistsBtsDTO);

  MrChecklistsBtsDTO findMrChecklistBtsByIdFromWeb(MrChecklistsBtsDTO mrChecklistsBtsDTO);

  ResultInSideDto deleteMrChecklistBts(Long checklistId);

  File exportDataMrChecklistBts(MrChecklistsBtsDTO mrChecklistsBtsDTO) throws Exception;

  List<MrDeviceBtsDTO> getListSupplierBtsCombobox(MrDeviceBtsDTO mrDeviceBtsDTO);

  ResultInSideDto importData(MultipartFile multipartFile) throws IOException;

  File getFileTemplate();
}
