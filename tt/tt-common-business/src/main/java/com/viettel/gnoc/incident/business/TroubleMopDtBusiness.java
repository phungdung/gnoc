package com.viettel.gnoc.incident.business;

import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.incident.dto.TroubleMopDtDTO;
import com.viettel.gnoc.incident.dto.TroubleMopDtInSideDTO;
import com.viettel.gnoc.kedb.dto.AuthorityDTO;
import java.util.List;

public interface TroubleMopDtBusiness {

  ResultDTO insertTroubleMopDt(TroubleMopDtDTO troubleMopDtDTO);

  List<TroubleMopDtInSideDTO> getListTroubleMopDtByCondition(List<ConditionBean> lstCondition,
      int rowStart, int maxRow, String sortType, String sortName);

  ResultDTO updateDt(AuthorityDTO requestDTO, TroubleMopDtDTO troubleMopDtDTO) throws Exception;
}
