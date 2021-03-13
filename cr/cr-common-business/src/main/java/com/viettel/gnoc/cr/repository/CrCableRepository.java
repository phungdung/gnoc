package com.viettel.gnoc.cr.repository;

import com.viettel.gnoc.cr.dto.CrCableDTO;
import com.viettel.gnoc.cr.dto.CrInsiteDTO;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface CrCableRepository {

  boolean saveOrUpdateCableDetail(List<CrCableDTO> dtoList, Long crId, Date crCreateTime);

  List<CrCableDTO> getListCrCableByCondition(CrInsiteDTO crInsiteDTO);

  List<CrCableDTO> getListCrCableDTO(CrCableDTO crCableDTO, int rowStart, int maxRow,
      String sortType, String sortFieldList);
}
