package com.viettel.gnoc.mr.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.maintenance.dto.MrDeviceCDDTO;
import java.io.File;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface MrDeviceCDBusiness {

  List<MrDeviceCDDTO> getComboboxDeviceType();

  List<MrDeviceCDDTO> getComboboxStationCode();

  Datatable onSearch(MrDeviceCDDTO mrDeviceCDDTO);

  File exportSearchData(MrDeviceCDDTO mrDeviceCDDTO) throws Exception;

  ResultInSideDto importMrDeviceCD(MultipartFile fileImport) throws Exception;

  File getFileTemplate();
}
