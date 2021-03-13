package com.viettel.gnoc.mr.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.maintenance.dto.MrScheduleCdDTO;
import java.io.File;
import org.springframework.web.multipart.MultipartFile;

public interface MrScheduleCDBusiness {

  Datatable onSearch(MrScheduleCdDTO mrScheduleCdDTO);

  File exportSearchData(MrScheduleCdDTO mrScheduleCdDTO) throws Exception;

  ResultInSideDto importMrScheduleCD(MultipartFile fileImport) throws Exception;

  ResultInSideDto deleteById(Long id);

  ResultInSideDto addOrUpdate(MrScheduleCdDTO dto);

  MrScheduleCdDTO findById(Long id);

  File getFileTemplate();
}
