package com.viettel.gnoc.wo.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.wo.dto.WoWorklogInsideDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface WoWorklogRepository {

  ResultInSideDto insertWoWorklog(WoWorklogInsideDTO woWorklogInsideDTO);

  Datatable getListWorklogByWoIdPaging(WoWorklogInsideDTO woWorklogInsideDTO);

  List<WoWorklogInsideDTO> getListDataByWoId(Long woId);

  List<WoWorklogInsideDTO> getListDataByWoIdPaging(String woId, int rowStart, int maxRow,
      String sortType, String sortFieldList);
}
