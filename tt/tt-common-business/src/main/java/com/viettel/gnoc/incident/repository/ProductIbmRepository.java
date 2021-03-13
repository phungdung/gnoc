package com.viettel.gnoc.incident.repository;

import com.viettel.gnoc.incident.dto.ProductIbmDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductIbmRepository {

  List<ProductIbmDTO> getListProductIbmDTOCombobox();
}
