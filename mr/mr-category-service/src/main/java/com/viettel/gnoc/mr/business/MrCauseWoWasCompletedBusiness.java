package com.viettel.gnoc.mr.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.maintenance.dto.MrCauseWoWasCompletedDTO;
import java.io.File;
import java.io.IOException;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface MrCauseWoWasCompletedBusiness {

  ResultInSideDto insertOrUpdate(MrCauseWoWasCompletedDTO mrCauseWoWasCompletedDTO);

  Datatable onSearch(MrCauseWoWasCompletedDTO dto);

  MrCauseWoWasCompletedDTO findById(Long id);

  File exportData(MrCauseWoWasCompletedDTO dto) throws Exception;

  File getTemplate() throws IOException;

  ResultInSideDto importData(MultipartFile fileImport);

  ResultInSideDto delete(Long id);

  List<MrCauseWoWasCompletedDTO> getReasonWO(String reasonTypeId);
}
