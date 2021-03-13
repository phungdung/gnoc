package com.viettel.gnoc.wo.business;

import com.viettel.gnoc.wo.dto.WoDetailDTO;
import java.util.List;

public interface WoDetailBusiness {


  List<WoDetailDTO> getListWoDetailDTO(WoDetailDTO woDetailDTO);
}
