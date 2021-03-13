package com.viettel.gnoc.mr.business;

import com.viettel.gnoc.maintenance.dto.MrHisSearchDTO;
import java.util.List;

public interface MrHisSearchBusiness {

  List<MrHisSearchDTO> getListMrHisSearch(MrHisSearchDTO his);
}
