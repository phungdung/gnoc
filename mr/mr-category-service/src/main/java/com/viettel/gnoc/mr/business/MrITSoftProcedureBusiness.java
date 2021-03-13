package com.viettel.gnoc.mr.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.maintenance.dto.MrITSoftProcedureDTO;
import java.io.File;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface MrITSoftProcedureBusiness {

  Datatable getListMrSoftITProcedure(MrITSoftProcedureDTO mrITSoftProcedureDTO);

  MrITSoftProcedureDTO getDetail(Long procedureId);

  ResultInSideDto onInsert(List<MultipartFile> fileAttachs,
      MrITSoftProcedureDTO mrITSoftProcedureDTO) throws Exception;

  ResultInSideDto onUpdate(List<MultipartFile> fileAttachs,
      MrITSoftProcedureDTO mrSoftITProcedureDTO) throws Exception;

  ResultInSideDto delete(Long procedureId);

  File exportData(MrITSoftProcedureDTO mrITSoftProcedureDTO) throws Exception;

}
