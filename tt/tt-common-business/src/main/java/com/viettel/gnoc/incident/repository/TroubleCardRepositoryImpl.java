package com.viettel.gnoc.incident.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.incident.dto.TroubleCardDTO;
import com.viettel.gnoc.incident.model.TroubleCardEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class TroubleCardRepositoryImpl extends BaseRepository implements TroubleCardRepository {

  @Override
  public Datatable getListTroubleCardDTO(TroubleCardDTO troubleCardDTO) {
    Map<String, Object> parameters = new HashMap<>();
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_TROUBLECARD,
        "get-list-troubleCard-by-troubleId");
    if (!StringUtils.isStringNullOrEmpty(troubleCardDTO.getTroubleId())) {
      sql += " and  a.TROUBLE_ID=:troubleId";
      parameters.put("troubleId", troubleCardDTO.getTroubleId());
    }
    if (!StringUtils.isStringNullOrEmpty(troubleCardDTO.getMerCode())) {
      sql += " AND ((a.MER_CODE) LIKE  UPPER(:mercode)  )";
      parameters.put("mercode", "%" + troubleCardDTO.getMerCode() + "%");
    }
    if (!StringUtils.isStringNullOrEmpty(troubleCardDTO.getStationCode())) {
      sql += " AND ((a.STATION_CODE) LIKE UPPER(:station)  )";
      parameters.put("station", "%" + troubleCardDTO.getStationCode() + "%");
    }
    if (!StringUtils.isStringNullOrEmpty(troubleCardDTO.getSerialNo())) {
      sql += " AND ((a.SERIAL_NO) LIKE UPPER(:serialNo)  )";
      parameters.put("serialNo", "%" + troubleCardDTO.getSerialNo() + "%");
    }
    return getListDataTableBySqlQuery(sql, parameters, troubleCardDTO.getPage(),
        troubleCardDTO.getPageSize(),
        TroubleCardDTO.class, troubleCardDTO.getSortName(),
        troubleCardDTO.getSortType());
  }

  @Override
  public ResultInSideDto updateTroubleCardDTO(TroubleCardDTO troubleCardDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    getEntityManager().merge(troubleCardDTO.toModel());
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto insertTroubleCardDTO(TroubleCardDTO troubleCardDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    TroubleCardEntity troubleCardEntity = getEntityManager().merge(troubleCardDTO.toModel());
    resultInSideDto.setId(troubleCardEntity.getTroubleCardId());
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto insertOrUpdateTroubleCard(List<TroubleCardDTO> troubleCardDTOS) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    for (TroubleCardDTO item : troubleCardDTOS) {
      TroubleCardEntity entity = item.toModel();
      if (entity.getTroubleCardId() != null && entity.getTroubleCardId() > 0) {
        getEntityManager().merge(entity);
      } else {
        getEntityManager().persist(entity);
      }
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto deleteTroubleCard(Long id) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(deleteById(TroubleCardEntity.class, id, colId));
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto deleteListTroubleCard(List<TroubleCardDTO> troubleCardDTOS) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    String result = deleteByListDTO(troubleCardDTOS, TroubleCardEntity.class, colId);
    resultInSideDto.setKey(result);
    return resultInSideDto;
  }

  @Override
  public List<TroubleCardDTO> getListTroubleCardDTOByTroubleId(Long troubleId) {
    Map<String, Object> parameters = new HashMap<>();
    String sql = "select * from ONE_TM.TROUBLE_CARD a where 1=1 and a.TROUBLE_ID =:troubleId";
    parameters.put("troubleId", troubleId);
    List<TroubleCardDTO> list = getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper
            .newInstance(TroubleCardDTO.class));
    return list;
  }

  private static final String colId = "troubleCardId";
}

