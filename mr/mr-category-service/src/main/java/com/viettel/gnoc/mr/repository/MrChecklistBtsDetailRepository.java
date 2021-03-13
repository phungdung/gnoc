package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.maintenance.dto.MrChecklistsBtsDetailDTO;
import com.viettel.gnoc.maintenance.dto.MrScheduleBtsHisDetailInsiteDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface MrChecklistBtsDetailRepository {

  ResultInSideDto insertMrChecklistBtsDetail(MrChecklistsBtsDetailDTO mrChecklistsBtsDetailDTO);

  ResultInSideDto updateMrChecklistBtsDetail(MrChecklistsBtsDetailDTO mrChecklistsBtsDetailDTO);

  ResultInSideDto deleteMrChecklistBtsDetail(Long checklistDetailId);

  List<MrChecklistsBtsDetailDTO> getListDetailByChecklistId(Long checklistId);

  Datatable getListDataDetail(MrChecklistsBtsDetailDTO mrChecklistsBtsDetailDTO);

  List<MrChecklistsBtsDetailDTO> getListDetail(MrChecklistsBtsDetailDTO mrChecklistsBtsDetailDTO);

  List<MrScheduleBtsHisDetailInsiteDTO> getAllCheckListLv2(String woCodeOrignal);
}
