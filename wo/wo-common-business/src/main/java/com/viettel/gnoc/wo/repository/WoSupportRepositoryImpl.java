package com.viettel.gnoc.wo.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.incident.dto.WoSupportDTO;
import com.viettel.gnoc.incident.model.WoSupportEntity;
import com.viettel.gnoc.wo.dto.CfgSupportForm;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class WoSupportRepositoryImpl extends BaseRepository implements WoSupportRepository {

  @Override
  public List<CfgSupportForm> listWoSupportInfo(Long woId) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_WO_SUPPORT, "list-Wo-Support-Info");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("woId", woId);
    return getNamedParameterJdbcTemplate().query(sql, parameters,
        BeanPropertyRowMapper.newInstance(CfgSupportForm.class));
  }

  @Override
  public ResultInSideDto insertWoSupport(WoSupportDTO woSupportDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    WoSupportEntity woSupportEntity = getEntityManager().merge(woSupportDTO.toEntity());
    resultInSideDto.setId(woSupportEntity.getId());
    resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto insertListWoSupport(List<WoSupportDTO> lstWoSupportDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    String id = "";
    for (WoSupportDTO dto : lstWoSupportDTO) {
      try {
        WoSupportEntity woSupportEntity = getEntityManager().merge(dto.toEntity());
        if (woSupportEntity != null && !StringUtils.isLongNullOrEmpty(woSupportEntity.getId())) {
          id += woSupportEntity.getId() + ",";
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    resultInSideDto.setIdValue(id);
    resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    return resultInSideDto;
  }
}
