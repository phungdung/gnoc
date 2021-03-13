package com.viettel.gnoc.incident.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.incident.dto.ItAccountDTO;
import com.viettel.gnoc.incident.dto.TroublesInSideDTO;
import com.viettel.gnoc.incident.model.ItAccountEntity;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface ItAccountRepository {

  ResultInSideDto insertItAccount(ItAccountEntity entity);

  Datatable getListItAccountDTO(TroublesInSideDTO dto);

  ResultInSideDto deleteItAccountByIncidentId(Long troubleId);

  List<ItAccountDTO> getListItAccountByCondition(List<ConditionBean> lstCondition, int rowStart,
      int maxRow, String sortType, String sortFieldList);
}
