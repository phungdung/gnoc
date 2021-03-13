package com.viettel.gnoc.wo.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.wo.dto.WoAutoCheckDTO;
import com.viettel.gnoc.wo.dto.WoInsideDTO;
import javax.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class WoAutoCheckRepositoryImpl extends BaseRepository implements WoAutoCheckRepository {

  @SuppressWarnings("JpaQlInspection")
  @Override
  public ResultInSideDto actionUpdateWoAutoCheck(WoInsideDTO woInsideDTO,
      WoAutoCheckDTO woAutoCheckDTO,
      boolean isFalse) {
    ResultInSideDto resultDto = new ResultInSideDto();
    String sqlUpdate = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_WO_AUTO_CHECK, "action-Update-Wo-Auto-Check");
    Query query = getEntityManager().createNativeQuery(sqlUpdate);
    if (!isFalse) {
      query.setParameter("status", Constants.WO_AUTO_CHECK_STATUS.NOK);
    } else {
      query.setParameter("status", Constants.WO_AUTO_CHECK_STATUS.OK);
    }
    query.setParameter("resultDescription", woAutoCheckDTO.getErrorDescription());
    query.setParameter("woId", woInsideDTO.getWoId());
    int result = query.executeUpdate();
    if (result > 0) {
      resultDto.setKey(RESULT.SUCCESS);
    } else {
      resultDto.setKey(RESULT.ERROR);
    }
    return resultDto;
  }
}
