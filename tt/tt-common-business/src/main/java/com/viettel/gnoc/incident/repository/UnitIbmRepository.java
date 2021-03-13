package com.viettel.gnoc.incident.repository;

import com.viettel.gnoc.incident.dto.UnitIbmDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface UnitIbmRepository {

  List<UnitIbmDTO> getListUnitIbmDTOCombobox();

}
