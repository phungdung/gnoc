package com.viettel.gnoc.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.GnocLanguageDto;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.model.GnocLanguageEntity;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class GnocLanguageRepositoryImpl extends BaseRepository implements GnocLanguageRepository {

  @Override
  public ResultInSideDto insertGnocLanguageDTO(GnocLanguageDto gnocLanguageDto) {
    GnocLanguageEntity entity = gnocLanguageDto.toEntity();
    return insertByModel(entity, colId);
  }

  @Override
  public String updateGnocLanguageDTO(GnocLanguageDto dto) {
    GnocLanguageEntity entity = dto.toEntity();
    getEntityManager().merge(entity);
    return RESULT.SUCCESS;
  }

  @Override
  public String deleteGnocLanguageById(Long id) {
    return deleteById(GnocLanguageEntity.class, id, colId);
  }

  @Override
  public GnocLanguageDto findGnocLanguageId(Long id) {
    if (id != null && id > 0) {
      return getEntityManager().find(GnocLanguageEntity.class, id).toDTO();
    }
    return null;
  }

  @Override
  public Datatable getListGnocLanguage(GnocLanguageDto dto) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_COMMON_GNOC_LANGUAGE, "get-list-gnoc-language");
    Map<String, Object> parameters = new HashMap<>();
    if (StringUtils.isNotNullOrEmpty(dto.getLanguageKey())) {
      sql += " AND LOWER(B.LANGUAGE_KEY) LIKE :empUserName ESCAPE '\\'";
      parameters.put("empUserName", StringUtils.convertLowerParamContains(dto.getLanguageKey()));
    }
    if (StringUtils.isNotNullOrEmpty(dto.getLanguageName())) {
      sql += " AND LOWER(B.LANGUAGE_NAME) LIKE:empUserName ESCAPE '\\'";
      parameters.put("languageName", StringUtils.convertLowerParamContains(dto.getLanguageName()));
    }
    if (StringUtils.isNotNullOrEmpty(dto.getSearchAll())) {
      sql += " AND (LOWER(B.LANGUAGE_KEY) LIKE :searchAll ESCAPE '\\'";
      sql += " OR LOWER(B.LANGUAGE_NAME) LIKE :searchAll ESCAPE '\\')";
      parameters.put("searchAll", StringUtils.convertLowerParamContains(dto.getSearchAll()));
    }
    return getListDataTableBySqlQuery(sql, parameters, dto.getPage(),
        dto.getPageSize(), GnocLanguageDto.class,
        dto.getSortName(),
        dto.getSortType());
  }

  private static final String colId = "gnocLanguageId";
}
