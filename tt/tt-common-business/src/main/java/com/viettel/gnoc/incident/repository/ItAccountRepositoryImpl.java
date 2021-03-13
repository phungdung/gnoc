package com.viettel.gnoc.incident.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.incident.dto.ItAccountDTO;
import com.viettel.gnoc.incident.dto.TroublesInSideDTO;
import com.viettel.gnoc.incident.model.ItAccountEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class ItAccountRepositoryImpl extends BaseRepository implements
    ItAccountRepository {

  @Override
  public ResultInSideDto insertItAccount(ItAccountEntity entity) {
    return insertByModel(entity, "id");
  }

  @Override
  public Datatable getListItAccountDTO(TroublesInSideDTO dto) {
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_TROUBLES_IT_ACCOUNT,
        "get_it_account_by_trouble_id");
    sql += " AND INCIDENT_ID =:troubleId";
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("troubleId", dto.getTroubleId());
    return getListDataTableBySqlQuery(sql, parameters, dto.getPage(),
        dto.getPageSize(), ItAccountDTO.class, dto.getSortName(),
        dto.getSortType());
  }

  @Override
  public ResultInSideDto deleteItAccountByIncidentId(Long incidentId) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    List<ItAccountEntity> list = (List<ItAccountEntity>) findByMultilParam(
        ItAccountEntity.class, "incidentId", incidentId);
    if (list != null && list.size() > 0) {
      for (ItAccountEntity entity : list) {
        getEntityManager().remove(entity);
      }
    }
    resultInSideDto.setKey(RESULT.SUCCESS);
    return resultInSideDto;
  }

  @Override
  public List<ItAccountDTO> getListItAccountByCondition(List<ConditionBean> lstCondition,
      int rowStart,
      int maxRow, String sortType, String sortFieldList) {
    return onSearchByConditionBean(new ItAccountEntity(), lstCondition, rowStart, maxRow, sortType,
        sortFieldList);
  }
}
