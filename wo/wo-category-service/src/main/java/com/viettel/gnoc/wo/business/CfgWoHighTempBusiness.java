package com.viettel.gnoc.wo.business;

import com.viettel.gnoc.commons.dto.DataItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.incident.dto.CatReasonInSideDTOSearch;
import com.viettel.gnoc.wo.dto.CfgWoHighTempDTO;
import java.io.File;
import java.io.IOException;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface CfgWoHighTempBusiness {

  Datatable onSearch(CfgWoHighTempDTO dto);

  ResultInSideDto insertOrUpdate(CfgWoHighTempDTO dto);

  CfgWoHighTempDTO findById(Long id);

  ResultInSideDto delete(Long id);

  File exportData(CfgWoHighTempDTO dto) throws Exception;

  ResultInSideDto importData(MultipartFile upLoadFile);

  File getTemplate() throws IOException;

  List<DataItemDTO> getListPriority();

  List<CatReasonInSideDTOSearch> getListReason(Long parentId);

}
