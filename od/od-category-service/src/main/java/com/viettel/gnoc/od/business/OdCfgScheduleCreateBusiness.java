package com.viettel.gnoc.od.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.od.dto.OdCfgScheduleCreateDTO;
import java.io.File;
import java.io.IOException;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface OdCfgScheduleCreateBusiness {

  Datatable getListOdCfgScheduleCreateDTOSearchWeb(OdCfgScheduleCreateDTO odCfgScheduleCreateDTO);

  OdCfgScheduleCreateDTO findOdCfgScheduleCreateById(Long id);

  ResultInSideDto insertOdCfgScheduleCreate(List<MultipartFile> files,
      OdCfgScheduleCreateDTO odCfgScheduleCreateDTO)
      throws IOException;

  ResultInSideDto updateOdCfgScheduleCreate(List<MultipartFile> files,
      OdCfgScheduleCreateDTO odCfgScheduleCreateDTO)
      throws IOException;

  ResultInSideDto deleteOdCfgScheduleCreate(Long id);

  ResultInSideDto deleteListOdCfgScheduleCreate(List<Long> listId);

  String getSequenseOdCfgScheduleCreate();

  File exportData(OdCfgScheduleCreateDTO odCfgScheduleCreateDTO) throws Exception;

  ResultInSideDto importData(MultipartFile fileImport, List<MultipartFile> files)
      throws IOException;

  File getTemplate() throws Exception;
}
