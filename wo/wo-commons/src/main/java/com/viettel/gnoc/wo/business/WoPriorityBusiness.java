package com.viettel.gnoc.wo.business;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.wo.dto.WoPriorityDTO;
import com.viettel.gnoc.wo.dto.WoTypeInsideDTO;
import java.util.List;

public interface WoPriorityBusiness {


  ResultInSideDto delete(Long priorityId);

  List<WoPriorityDTO> findAllByWoTypeID(Long woTypeId);

  ResultInSideDto insertListPriority(WoTypeInsideDTO woTypeInsideDTO);

  List<WoPriorityDTO> getListWoPriorityDTO(WoPriorityDTO woPriorityDTO);

}
