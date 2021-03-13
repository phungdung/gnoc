package com.viettel.gnoc.mr.business;

import com.viettel.gnoc.ws.dto.MrMaterialDTO;
import com.viettel.gnoc.ws.dto.MrMaterialDisplacementDTO;
import java.util.List;

public interface MrMaterialDisplacementBusiness {

  List<MrMaterialDTO> getListMrMaterialDTO2(MrMaterialDTO mrMaterialDTO, String userManager,
      String woCode, int rowStart, int maxRow);

  String insertOrUpdateListMrMaterialDisplacement(List<MrMaterialDisplacementDTO> dto);

  List<MrMaterialDisplacementDTO> getListMrMaterialDisplacementDTO2(MrMaterialDisplacementDTO dto);
}
