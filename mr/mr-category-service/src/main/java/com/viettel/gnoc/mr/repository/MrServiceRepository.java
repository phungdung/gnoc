package com.viettel.gnoc.mr.repository;


import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.maintenance.dto.MrDTO;
import com.viettel.gnoc.maintenance.dto.MrInsideDTO;
import com.viettel.gnoc.maintenance.dto.MrSearchDTO;
import com.viettel.gnoc.wo.dto.WorkLogInsiteDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface MrServiceRepository {

  Datatable getListMrCrWoNew(MrSearchDTO dto);

  List<MrDTO> getListMrCrWoNewForExport(MrSearchDTO dto);

  List<MrDTO> getWorklogFromWo(MrSearchDTO dto);

  List<String> getIdSequence();

  ResultInSideDto insertMr(MrInsideDTO mrDTO);

  ResultInSideDto updateMr(MrInsideDTO mrDTO);

  List<WorkLogInsiteDTO> getListWorklogSearch(WorkLogInsiteDTO workLogInsiteDTO);

  MrInsideDTO findMrById(Long mrId);
}
