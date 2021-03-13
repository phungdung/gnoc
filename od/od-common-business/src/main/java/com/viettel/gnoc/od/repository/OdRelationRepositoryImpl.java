package com.viettel.gnoc.od.repository;


import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.OD_MASTER_CODE;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.od.dto.OdRelationDTO;
import com.viettel.gnoc.od.model.OdRelationEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

/**
 * @author NamTN
 */
@SuppressWarnings("rawtypes")
@Repository
@Slf4j
public class OdRelationRepositoryImpl extends BaseRepository implements OdRelationRepository {

  @Override
  public ResultInSideDto insertOrUpdate(OdRelationDTO odDTO) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
    OdRelationEntity oEntity = odDTO.toEntity();
    if (oEntity.getId() != null) {
      getEntityManager().merge(oEntity);
    } else {
      getEntityManager().persist(oEntity);
    }
    resultInSideDTO.setId(oEntity.getId());
    return resultInSideDTO;
  }

  @Override
  public List<OdRelationDTO> getRelationsByOdId(Long odId) {
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_OD, "od-sql-get-relation-by-od");
    Map<String, String> params = new HashMap<>();
    params.put("odStatusCode", OD_MASTER_CODE.OD_STATUS);
    params.put("odId", odId.toString());
    return getNamedParameterJdbcTemplate()
        .query(sqlQuery, params, BeanPropertyRowMapper.newInstance(OdRelationDTO.class));
  }
}
