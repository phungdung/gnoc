package com.viettel.gnoc.wo.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.wo.dto.WoChecklistDTO;
import com.viettel.gnoc.wo.dto.WoChecklistDetailDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface WoChecklistDetailRepository {

  List<WoChecklistDetailDTO> getListWoChecklistDetailDTO(WoChecklistDetailDTO woChecklistDetailDTO);

//  ResultInSideDto deleteListWoChecklistDetailDTO(
//      List<WoChecklistDetailDTO> listWoChecklistDetailDTO);

  ResultInSideDto deleteWoChecklistDetailDTO(Long woChecklistDetailId);

  ResultInSideDto insertWoChecklistDetailDTO(WoChecklistDetailDTO woChecklistDetailDTO);

  String insertListWoChecklistDetail(List<WoChecklistDTO> lstChecklist);
}
