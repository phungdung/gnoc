package com.viettel.gnoc.mr.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.maintenance.dto.MrCfgProcedureTelDTO;
import java.io.File;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface MrCfgProcedureTelBusiness {

  Datatable onSearch(MrCfgProcedureTelDTO mrCfgProcedureTelDTO);

  File exportSearchData(MrCfgProcedureTelDTO mrCfgProcedureTelDTO) throws Exception;

  ResultInSideDto onInsert(List<MultipartFile> fileAttachs,
      MrCfgProcedureTelDTO mrCfgProcedureTelDTO) throws Exception;

  ResultInSideDto onUpdate(List<MultipartFile> fileAttachs, MrCfgProcedureTelDTO dto)
      throws Exception;

  ResultInSideDto delete(Long procedureId);

  MrCfgProcedureTelDTO getDetail(Long procedureId);
}
