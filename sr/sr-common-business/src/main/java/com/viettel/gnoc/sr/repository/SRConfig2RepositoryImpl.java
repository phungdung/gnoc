package com.viettel.gnoc.sr.repository;

import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.sr.dto.SRConfig2DTO;
import com.viettel.gnoc.sr.model.SRConfig2Entity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Slf4j
@Transactional
public class SRConfig2RepositoryImpl extends BaseRepository implements SRConfig2Repository {

  @Override
  public List<SRConfig2DTO> getFile(SRConfig2DTO dto) {
    List<SRConfig2DTO> lstResult = new ArrayList<>();
    if (!StringUtils.isStringNullOrEmpty(dto)) {
      Map<String, Object> parameters = new HashMap<>();
      String sql = SQLBuilder.getSqlQueryById("SRConfig2", "getFile");
      parameters.put("configGroup", dto.getConfigGroup());
      if (!StringUtils.isStringNullOrEmpty(dto.getServiceGroup())) {
        sql += "AND SERVICE_GROUP = :serviceGroup";
        parameters.put("serviceGroup", dto.getServiceGroup());
      } else {
        sql += "AND SERVICE_GROUP is null";
      }
      lstResult = getNamedParameterJdbcTemplate()
          .query(sql, parameters, BeanPropertyRowMapper.newInstance(SRConfig2DTO.class));
    }
    return lstResult;
  }

  @Override
  public SRConfig2DTO getFileTypeByConfigCode(String configCode, String status,
      String configGroup) {
    if (StringUtils.isNotNullOrEmpty(configCode)) {
      List<SRConfig2Entity> lst = findByMultilParam(SRConfig2Entity.class, "status", status,
          "configGroup", configGroup, "configCode", configCode);
      return lst.isEmpty() ? null : lst.get(0).toDTO();
    }
    return null;
  }
}
