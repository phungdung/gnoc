package com.viettel.gnoc.mr.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.maintenance.dto.MrDTO;
import com.viettel.gnoc.maintenance.dto.MrInsideDTO;
import com.viettel.gnoc.maintenance.dto.MrSearchDTO;
import com.viettel.gnoc.wo.dto.WorkLogInsiteDTO;
import java.util.List;

public interface MrServiceBusiness {

  Datatable getListMrCrWoNew(MrSearchDTO mrSearchDTO);

  List<MrDTO> getWorklogFromWo(MrSearchDTO mrSearchDTO);

  List<String> getIdSequence();

  ResultInSideDto insertMr(MrInsideDTO mrInsideDTO);

  ResultInSideDto updateMr(MrInsideDTO mrDTO);

  List<WorkLogInsiteDTO> getListWorklogSearch(WorkLogInsiteDTO workLogInsiteDTO);
}
