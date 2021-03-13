package com.viettel.gnoc.wo.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.wo.dto.WoTypeCheckListDTO;
import com.viettel.gnoc.wo.model.WoTypeCheckListEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class WoTypeCheckListRepositoryImpl extends BaseRepository implements
    WoTypeCheckListRepository {


  @Override
  public ResultInSideDto delete(Long woTypeChecklistId) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    WoTypeCheckListEntity woPriorityEntity = getEntityManager()
        .find(WoTypeCheckListEntity.class, woTypeChecklistId);
    getEntityManager().remove(woPriorityEntity);
    return resultInSideDto;
  }

  @Override
  public List<WoTypeCheckListDTO> findAllByWoTypeID(Long woTypeId) {

    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_WO_TYPE_CHECKLIST, "wo-Type-CheckList");
    Map<String, Object> parameters = new HashMap<>();
    if (!StringUtils.isStringNullOrEmpty(woTypeId)) {
      sqlQuery += " AND c.WO_TYPE_ID= :woTypeId ";
      parameters.put("woTypeId", woTypeId);
    }
    return getNamedParameterJdbcTemplate()
        .query(sqlQuery, parameters, BeanPropertyRowMapper.newInstance(WoTypeCheckListDTO.class));

  }

  @Override
  public ResultInSideDto add(WoTypeCheckListDTO woTypeCheckListDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    WoTypeCheckListEntity woTypeCheckListEntity = getEntityManager()
        .merge(woTypeCheckListDTO.toEntity());
    resultInSideDto.setId(woTypeCheckListEntity.getWoTypeChecklistId());
    return resultInSideDto;
  }

  @Override
  public Datatable getListWoTypeChecklistDTO(WoTypeCheckListDTO woTypeCheckListDTO) {
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_WO_TYPE_CHECKLIST,
        "get-List-Wo-Type-Check-list-DTO");
    Map<String, Object> parameters = new HashMap<>();
    if (woTypeCheckListDTO.getWoTypeChecklistId() != null) {
      sql += " AND a.wo_type_checklist_id = :woTypeChecklistId";
      parameters.put("woTypeChecklistId", woTypeCheckListDTO.getWoTypeChecklistId());
    }
    if (woTypeCheckListDTO.getWoTypeId() != null) {
      sql += " AND a.wo_type_id = :woTypeId";
      parameters.put("woTypeId", woTypeCheckListDTO.getWoTypeId());
    }
    if (StringUtils.isNotNullOrEmpty(woTypeCheckListDTO.getChecklistName())) {
      sql += " AND a.checklist_name = :checklistName";
      parameters.put("checklistName", woTypeCheckListDTO.getChecklistName());
    }
    if (woTypeCheckListDTO.getIsEnable() != null) {
      sql += " AND a.is_enable = :isEnable";
      parameters.put("isEnable", woTypeCheckListDTO.getIsEnable());
    }
    if (StringUtils.isNotNullOrEmpty(woTypeCheckListDTO.getDefaultValue())) {
      sql += " AND a.default_value = :defaultValue";
      parameters.put("defaultValue", woTypeCheckListDTO.getDefaultValue());
    }
    sql += " ORDER BY checklist_name";
    return getListDataTableBySqlQuery(sql, parameters, woTypeCheckListDTO.getPage(),
        woTypeCheckListDTO.getPageSize(), WoTypeCheckListDTO.class,
        woTypeCheckListDTO.getSortName(), woTypeCheckListDTO.getSortType());
  }

//  @Override
//  public WoTypeCheckListDTO checkWoTypeCheckListExist(Long a, Long b) {
//    return null;
//  }
}
