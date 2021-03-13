package com.viettel.gnoc.wo.repository;

import com.viettel.gnoc.wo.dto.WoCdGroupTypeDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface WoCdGroupTypeRepository {

  List<WoCdGroupTypeDTO> getListWoCdGroupTypeDTO(WoCdGroupTypeDTO woCdGroupTypeDTO);
}
