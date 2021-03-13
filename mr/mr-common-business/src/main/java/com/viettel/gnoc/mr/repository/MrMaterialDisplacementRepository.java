package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.ws.dto.MrMaterialDTO;
import com.viettel.gnoc.ws.dto.MrMaterialDisplacementDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface MrMaterialDisplacementRepository {

  List<MrMaterialDTO> getListMrMaterialDTO2(MrMaterialDTO mrMaterialDTO, String userManager,
      String woCode, int rowStart, int maxRow);

  ResultDTO insertOrUpdateListMrMaterialDisplacement(MrMaterialDisplacementDTO dto);

  List<MrMaterialDisplacementDTO> getListMrMaterialDisplacementDTO2(MrMaterialDisplacementDTO dto);
}
