package com.viettel.gnoc.commons.repository;

import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.*;
import com.viettel.gnoc.commons.model.TroubleImportantEntity;
import com.viettel.gnoc.commons.model.UsersEntity;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.DateUtil;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author hungtv77
 */
@SuppressWarnings("rawtypes")
@Repository
@Slf4j
@Transactional
public class TroubleImportantRepositoryImpl extends BaseRepository implements TroubleImportantRepository {

  @Autowired
  TicketProvider ticketProvider;

  @Override
  public Datatable searchListTroubleImportant(TroubleImportantDTO dto) {
    try {
      Map<String, String> parameters = new HashMap<>();
      String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_COMMON, "get-List-Trouble-Important");
      if (StringUtils.isNotNullOrEmpty(dto.getSearchAll())) {
        sql += " AND LOWER(t.TROUBLE_NAME) like :searchAll ESCAPE '\\' ";
        parameters.put("searchAll", StringUtils.convertLowerParamContains(dto.getSearchAll()));
      }
      // Thoi gian khoi tao
      if (dto.getCreateTimeFrom() != null) {
        sql += " AND t.START_TIME >= TO_TIMESTAMP(:createTimeFrom,'dd/mm/yyyy hh24:mi:ss')";
        parameters.put("createTimeFrom", DateUtil.date2ddMMyyyyHHMMss(dto.getCreateTimeFrom()));
      }
      if (dto.getCreateTimeTo() != null) {
        sql += " AND t.START_TIME <= TO_TIMESTAMP(:createTimeTo,'dd/mm/yyyy hh24:mi:ss')";
        parameters.put("createTimeTo", DateUtil.date2ddMMyyyyHHMMss(dto.getCreateTimeTo()));
      }
      Datatable datatable = getListDataTableBySqlQuery(sql, parameters,
          dto.getPage(), dto.getPageSize(), TroubleImportantDTO.class, dto.getSortName(), dto.getSortType());
      if (datatable != null) {
        return datatable;
      }
    } catch (Exception err) {
      log.error(err.getMessage());
    }
    return null;
  }

  @Override
  public ResultInSideDto insert(TroubleImportantDTO dto) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    try {
       resultInSideDto.setKey(Constants.RESULT.SUCCESS);
        getEntityManager().persist(dto.toEntity());
    } catch (Exception err) {
      resultInSideDto.setKey(Constants.RESULT.FAIL);
      log.error(err.getMessage());
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto update(TroubleImportantDTO dto) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    try {
      resultInSideDto.setKey(Constants.RESULT.SUCCESS);
      getEntityManager().merge(dto.toEntity());
    } catch (Exception err) {
      resultInSideDto.setKey(Constants.RESULT.FAIL);
      log.error(err.getMessage());
    }
    return resultInSideDto;
  }

  @Override
  public TroubleImportantDTO getDetailTrouble(Long id) {
    try {
      TroubleImportantEntity troubleImportantEntity = getEntityManager().find(TroubleImportantEntity.class, id);
      if (troubleImportantEntity != null) {
        return troubleImportantEntity.toDTO();
      }
    } catch (Exception err) {
      log.error(err.getMessage());
    }
    return null;
  }

  @Override
  public ResultInSideDto delete(Long id) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    try {
      resultInSideDto.setKey(Constants.RESULT.SUCCESS);
      TroubleImportantEntity troubleImportantEntity = getEntityManager().find(TroubleImportantEntity.class, id);
      if (troubleImportantEntity != null) {
        getEntityManager().remove(troubleImportantEntity);
      }
    } catch (Exception err) {
      resultInSideDto.setKey(Constants.RESULT.FAIL);
      log.error(err.getMessage());
    }
    return resultInSideDto;
  }
}
