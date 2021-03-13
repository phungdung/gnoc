package com.viettel.gnoc.mr.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.maintenance.dto.MrScheduleBtsDTO;
import java.io.File;
import org.springframework.web.multipart.MultipartFile;

public interface MrScheduleBtsBusiness {

  Datatable onSearch(MrScheduleBtsDTO dto);

  File exportSearchData(MrScheduleBtsDTO mrScheduleBtsDTO) throws Exception;

  File getFileTemplate(MrScheduleBtsDTO mrScheduleBtsDTO);

  ResultInSideDto importMrScheduleBTS(MultipartFile fileImport) throws Exception;

}
