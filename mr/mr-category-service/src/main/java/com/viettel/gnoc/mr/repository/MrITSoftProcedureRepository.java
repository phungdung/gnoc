package com.viettel.gnoc.mr.repository;


import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.maintenance.dto.MrITSoftProcedureDTO;
import com.viettel.gnoc.maintenance.dto.MrITSoftScheduleDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface MrITSoftProcedureRepository {

  Datatable getListMrSoftITProcedure(MrITSoftProcedureDTO mrITSoftProcedureDTO);

  MrITSoftProcedureDTO getDetail(Long procedureId);


  ResultInSideDto insertOrUpdate(MrITSoftProcedureDTO mrITSoftProcedureDTO);

  ResultInSideDto delete(Long procedureId);

  List<MrITSoftScheduleDTO> getScheduleInProcedureITSoft(MrITSoftScheduleDTO mrITSoftScheduleDTO);

  List<MrITSoftProcedureDTO> getDataExport(MrITSoftProcedureDTO dto);

}
