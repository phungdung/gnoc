package com.viettel.gnoc.incident.repository;

import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.incident.dto.TroubleMopDtDTO;
import com.viettel.gnoc.incident.dto.TroubleMopDtInSideDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface TroubleMopDtRepository {

  ResultDTO insertTroubleMopDt(TroubleMopDtDTO troubleMopDtDTO);

  List<TroubleMopDtInSideDTO> getListTroubleMopDtByCondition(List<ConditionBean> lstCondition,
      int rowStart, int maxRow, String sortType, String sortName);

  ResultDTO update(TroubleMopDtInSideDTO troubleMopDtDTO);
}
