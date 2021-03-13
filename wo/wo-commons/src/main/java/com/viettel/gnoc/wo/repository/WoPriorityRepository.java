package com.viettel.gnoc.wo.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.wo.dto.WoPriorityDTO;
import com.viettel.gnoc.wo.dto.WoTypeInsideDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface WoPriorityRepository {


  ResultInSideDto delete(Long priorityId);

  List<WoPriorityDTO> findAllByWoTypeID(Long woTypeId);

  ResultInSideDto add(WoPriorityDTO woPriorityDTO);

  ResultInSideDto insertList(WoTypeInsideDTO woTypeInsideDTO);

  List<WoPriorityDTO> getListWoPriorityDTO(WoPriorityDTO woPriorityDTO);

}
