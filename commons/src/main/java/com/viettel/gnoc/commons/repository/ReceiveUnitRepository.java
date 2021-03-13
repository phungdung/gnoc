package com.viettel.gnoc.commons.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ReceiveUnitDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface ReceiveUnitRepository {

  Datatable getListReceiveUnitSearch(ReceiveUnitDTO receiveUnitDTO);

  ReceiveUnitDTO getReceiveUnit(Long receiveUnit);

  List<ReceiveUnitDTO> getListReceiveUnit();

  List<ReceiveUnitDTO> getListReceiveUnit(List<Long> listUnitId);

}
