package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.maintenance.dto.MrHisSearchDTO;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class MrHisSearchRepositoryImpl extends BaseRepository implements MrHisSearchRepository {

  @Override
  public List<MrHisSearchDTO> getListMrHisSearch(MrHisSearchDTO dto) {
    BaseDto baseDto = sqlGetListMrHisSearch(dto);
    List<MrHisSearchDTO> lst = getNamedParameterJdbcTemplate()
        .query(baseDto.getSqlQuery(), baseDto.getParameters(),
            BeanPropertyRowMapper.newInstance(MrHisSearchDTO.class));
    return lst;
  }

  public BaseDto sqlGetListMrHisSearch(MrHisSearchDTO dto) {
    Map<String, Object> params = new HashMap<>();
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.MR_HIS_SEARCH, "get-list-mr-his-search");
    if (dto != null) {
      if (!StringUtils.isStringNullOrEmpty(dto.getMrId())) {
        sql += "and a.mr_id = :mrId";
        params.put("mrId", dto.getMrId());
      }
      if (!StringUtils.isStringNullOrEmpty(dto.getUnitId())) {
        sql += " and ut.unit_id = :unitId";
        params.put("unitId", dto.getUnitId());
      }
      if (!StringUtils.isStringNullOrEmpty(dto.getUnitCode())) {
        sql += " and ut.unit_code = :unitCode";
        params.put("unitCode", dto.getUnitCode());
      }

      if (!StringUtils.isStringNullOrEmpty(dto.getUnitName())) {
        sql += " and ut.unit_name = :unitName";
        params.put("unitName", dto.getUnitName());
      }
      if (!StringUtils.isStringNullOrEmpty(dto.getUserId())) {
        sql += " and us.user_id = :userId";
        params.put("userId", dto.getUserId());
      }
      if (!StringUtils.isStringNullOrEmpty(dto.getUserName())) {
        sql += " and us.username = :userName";
        params.put("userName", dto.getUserName());
      }
    }
    sql += " order by a.mhs_id ";
    BaseDto baseDto = new BaseDto();
    baseDto.setSqlQuery(sql);
    baseDto.setParameters(params);
    return baseDto;
  }


}
