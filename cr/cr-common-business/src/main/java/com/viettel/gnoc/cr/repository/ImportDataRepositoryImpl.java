package com.viettel.gnoc.cr.repository;

import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.CrFileObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class ImportDataRepositoryImpl extends BaseRepository implements ImportDataRepository {

  @Override
  public List<CrFileObject> getListTemplateFileByProcess(String processTypeId, String type,
      String locale) {
    List<CrFileObject> lst = new ArrayList<>();
    try {
      if (StringUtils.isStringNullOrEmpty(processTypeId)) {
        return lst;
      }
      String sql = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_CR, "get-list-cr-template-file-by-process");
      Map<String, Object> params = new HashMap<>();
      params.put("applied_system", Constants.LANGUAGUE_EXCHANGE_SYSTEM.CR_MR);
      params.put("bussiness", 14);
      params.put("business_column", 1);
      params.put("p_leeLocale", locale);
      params.put("cr_process_id", processTypeId.trim());

      if (StringUtils.isStringNullOrEmpty(type)) {
        params.put("file_type", Constants.CR_FILE_TYPE.IMPORT_BY_PROCESS_IN);
      } else {
        params.put("file_type", Long.parseLong(type.trim()));
      }

      lst = getNamedParameterJdbcTemplate()
          .query(sql, params, BeanPropertyRowMapper.newInstance(CrFileObject.class));

    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return lst;
  }
}
