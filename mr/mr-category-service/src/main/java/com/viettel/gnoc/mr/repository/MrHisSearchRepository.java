package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.maintenance.dto.MrHisSearchDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface MrHisSearchRepository {

  List<MrHisSearchDTO> getListMrHisSearch(MrHisSearchDTO his);
}
