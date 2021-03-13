package com.viettel.gnoc.incident.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.incident.dto.TroublesInSideDTO;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class RelatedTTRepositoryImpl extends BaseRepository implements RelatedTTRepository {

  @Override
  public Datatable getListRelatedTT(TroublesInSideDTO dto) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_TROUBLES_RELATED_TT, "get_list_related_TT");
    Map<String, Object> paramters = new HashMap<>();
    paramters.put("troubleCode", dto.getTroubleCode());
    return getListDataTableBySqlQuery(sql, paramters, dto.getPage(),
        dto.getPageSize(),
        TroublesInSideDTO.class, dto.getSortName(), dto.getSortType());
  }

  @Override
  public Datatable getListRelatedTTByPopup(TroublesInSideDTO dto) {
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_TROUBLES_RELATED_TT_POPUP,
        "get_list_related_TT_By_Popup");
    Map<String, Object> parameters = new HashMap<>();
    if (StringUtils.isNotNullOrEmpty(dto.getTroubleCode()) && StringUtils
        .isNotNullOrEmpty(dto.getTroubleName())) {
      sql += "  AND LOWER(a.TROUBLE_CODE) LIKE :troubleCode ESCAPE '\\'";
      parameters.put("troubleCode",
          StringUtils.convertLowerParamContains(dto.getTroubleCode()));

      sql += "  AND LOWER(a.TROUBLE_NAME) LIKE :troubleName ESCAPE '\\'";
      parameters.put("troubleName",
          StringUtils.convertLowerParamContains(dto.getTroubleName()));
    } else if (StringUtils.isNotNullOrEmpty(dto.getTroubleCode())) {
      sql += "  AND LOWER(a.TROUBLE_CODE) LIKE :troubleCode ESCAPE '\\'";
      parameters.put("troubleCode",
          StringUtils.convertLowerParamContains(dto.getTroubleCode()));
    } else if (StringUtils.isNotNullOrEmpty(dto.getTroubleName())) {
      sql += "  AND LOWER(a.TROUBLE_NAME) LIKE :troubleName ESCAPE '\\'";
      parameters.put("troubleName",
          StringUtils.convertLowerParamContains(dto.getTroubleName()));
    } else if (StringUtils
        .isStringNullOrEmpty(dto.getTroubleCode()) && StringUtils
        .isStringNullOrEmpty(dto.getTroubleName()) && StringUtils
        .isNotNullOrEmpty(dto.getCreatedTimeFrom()) && StringUtils
        .isNotNullOrEmpty(dto.getCreatedTimeTo())) {
      sql +=
          " AND (a.CREATED_TIME BETWEEN TO_DATE('" + dto.getCreatedTimeFrom()
              + "', 'dd/MM/yyyy HH24:MI:ss') AND TO_DATE('"
              + dto
              .getCreatedTimeTo() + "', 'dd/MM/yyyy HH24:MI:ss'))";
    }
    if (StringUtils.isNotNullOrEmpty(dto.getCreatedTimeFrom()) && StringUtils
        .isNotNullOrEmpty(dto.getCreatedTimeTo())) {
      sql +=
          " AND (a.CREATED_TIME BETWEEN TO_DATE('" + dto.getCreatedTimeFrom()
              + "', 'dd/MM/yyyy HH24:MI:ss') AND TO_DATE('"
              + dto
              .getCreatedTimeTo() + "', 'dd/MM/yyyy HH24:MI:ss'))";
    }
    return getListDataTableBySqlQuery(sql, parameters, dto.getPage(), dto.getPageSize(),
        TroublesInSideDTO.class, dto.getSortName(), dto.getSortType());
  }

  @Override
  public Datatable getListRelatedTTByPopupAdd(TroublesInSideDTO dto) {
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_TROUBLES_RELATED_TT_POPUP,
        "get_list_related_TT_By_Popup_Add");
    Map<String, Object> parameters = new HashMap<>();
    if (!dto.getListTroubleId().isEmpty() && dto.getListTroubleId().size() > 0) {
      List<Long> id = dto.getListTroubleId();
      sql += " IN (";
      for (int i = 0; i < id.size(); i++) {
        if (i != id.size() - 1) {
          sql += id.get(i) + ",";
        } else {
          sql += id.get(i);
        }
      }
      sql += ")";
    }
    return getListDataTableBySqlQuery(sql, parameters, dto.getPage(), dto.getPageSize(),
        TroublesInSideDTO.class, dto.getSortName(), dto.getSortType());
  }


}
