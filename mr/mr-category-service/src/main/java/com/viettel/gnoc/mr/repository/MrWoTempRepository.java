package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.maintenance.dto.MrWoTempDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface MrWoTempRepository {

  List<MrWoTempDTO> search(MrWoTempDTO mrWoTempDTO, int rowStart, int maxRow, String sortType,
      String sortFieldList);

  ResultInSideDto updateMrWoTemp(MrWoTempDTO mrWoTemp);
}
