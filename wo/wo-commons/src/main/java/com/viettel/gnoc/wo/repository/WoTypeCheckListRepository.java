package com.viettel.gnoc.wo.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.wo.dto.WoTypeCheckListDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface WoTypeCheckListRepository {


  ResultInSideDto delete(Long woTypeChecklistId);

  List<WoTypeCheckListDTO> findAllByWoTypeID(Long woTypeId);

  ResultInSideDto add(WoTypeCheckListDTO woTypeCheckListDTO);

  Datatable getListWoTypeChecklistDTO(WoTypeCheckListDTO woTypeCheckListDTO);

//  WoTypeCheckListDTO checkWoTypeCheckListExist(Long a, Long b);
}
