package com.viettel.gnoc.od.repository;


import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.OD_MASTER_CODE;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.od.dto.OdHistoryDTO;
import com.viettel.gnoc.od.model.OdHistoryEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

/**
 * @author NamTN
 */
@SuppressWarnings("rawtypes")
@Repository
@Slf4j
public class OdHistoryRepositoryImpl extends BaseRepository implements OdHistoryRepository {

  @Override
  public ResultInSideDto delete(Long odHisId) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
    OdHistoryEntity odEntity = getEntityManager().find(OdHistoryEntity.class, odHisId);
    getEntityManager().remove(odEntity);
    return resultInSideDTO;
  }

  @Override
  public ResultInSideDto insertOrUpdate(OdHistoryDTO odDTO) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
    resultInSideDTO.setMessage(Constants.RESULT.SUCCESS);
    OdHistoryEntity oEntity = odDTO.toEntity();
    if (oEntity.getOdHisId() != null) {
      getEntityManager().merge(oEntity);
    } else {
      getEntityManager().persist(oEntity);
    }
    resultInSideDTO.setId(oEntity.getOdHisId());
    return resultInSideDTO;
  }

  @Override
  public String getSeqOdType() {
    String sql = "SELECT OD_HISTORY_SEQ.nextval from dual";
    Query query = getEntityManager().createNativeQuery(sql);
    Integer seq = query.getFirstResult();
    return seq.toString();
  }

  @Override
  public String getSeqOHistory(String sequense) {
    return getSeqTableBase(sequense);
  }

  @Override
  public List<OdHistoryDTO> getOdHistoryByOdId(Long odId) {
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_OD, "od-sql-get-history-by-od");
    Map<String, String> params = new HashMap<>();
    params.put("odStatusCode", OD_MASTER_CODE.OD_STATUS);
    params.put("odId", odId.toString());
    return getNamedParameterJdbcTemplate()
        .query(sqlQuery, params, BeanPropertyRowMapper.newInstance(OdHistoryDTO.class));
  }
}
