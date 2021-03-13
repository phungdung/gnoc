package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.maintenance.dto.MrChecklistsBtsDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface MrCheckListBtsRepository {

  List<MrChecklistsBtsDTO> getListScheduleBtsHisDTO(MrChecklistsBtsDTO dto, int rowStart,
      int maxRow);

  List<MrChecklistsBtsDTO> getListScheduleBtsHisDTOByWoCodes(List<String> woCodes);
}
