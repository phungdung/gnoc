package com.viettel.gnoc.incident.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.incident.dto.ProductIbmDTO;
import com.viettel.gnoc.incident.dto.TroublesIbmDTO;
import com.viettel.gnoc.incident.dto.UnitIbmDTO;
import java.io.IOException;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface TroublesIbmBusiness {

  Datatable getListTroublesIbmDTO(TroublesIbmDTO troublesIbmDTO);

  List<UnitIbmDTO> getListUnitIbmDTOCombobox();

  List<ProductIbmDTO> getListProductIbmDTOCombobox();

  ResultInSideDto insertTroublesIbm(List<MultipartFile> files, TroublesIbmDTO troublesIbmDTO)
      throws IOException;

  TroublesIbmDTO findById(Long id);
}
