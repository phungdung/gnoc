package com.viettel.gnoc.wo.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.wo.dto.WoChecklistDetailDTO;
import com.viettel.gnoc.wo.dto.WoTypeCheckListDTO;
import java.util.List;

public interface WoTypeCheckListBusiness {


  ResultInSideDto delete(Long woTypeChecklistId);

  List<WoTypeCheckListDTO> findAllByWoTypeID(Long woTypeId);

  ResultInSideDto add(WoTypeCheckListDTO woTypeCheckListDTO);


  Datatable getListWoTypeChecklistDTO(WoTypeCheckListDTO woTypeCheckListDTO);

  ResultInSideDto updateWoChecklistDetail(List<WoChecklistDetailDTO> listWoChecklistDetailDTO);

  List<WoChecklistDetailDTO> getListWoChecklistDetailDTO(WoChecklistDetailDTO woChecklistDetailDTO);
}
