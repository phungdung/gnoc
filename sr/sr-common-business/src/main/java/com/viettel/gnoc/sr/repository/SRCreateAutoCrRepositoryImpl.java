package com.viettel.gnoc.sr.repository;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.sr.dto.SRCreateAutoCRDTO;
import com.viettel.gnoc.sr.model.SRCreateAutoCREntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
@Slf4j
public class SRCreateAutoCrRepositoryImpl extends BaseRepository implements
    SRCreateAutoCrRepository {

  @Override
  public List<SRCreateAutoCRDTO> searchSRCreateAutoCR(SRCreateAutoCRDTO tDTO, int start,
      int maxResult, String sortType, String sortField) {
    return onSearchEntity(SRCreateAutoCREntity.class, tDTO, start, maxResult, sortType, sortField);
  }

  @Override
  public ResultInSideDto insertOrUpdateSRCreateAutoCr(SRCreateAutoCRDTO dto) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    SRCreateAutoCREntity entity = getEntityManager().merge(dto.toEntity());
    resultInSideDto.setId(entity.getId());
    return resultInSideDto;
  }

  @Override
  public List<SRCreateAutoCRDTO> getInforTemplate(SRCreateAutoCRDTO dto) {
    String sql = SQLBuilder.getSqlQueryById("SRCreateAutoCr", "getInforTemplate");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("p_leeLocale", I18n.getLocale());
    if (!StringUtils.isStringNullOrEmpty(dto.getCrProcessId())) {
      parameters.put("p_cr_process_id", dto.getCrProcessId());
    }
    List<SRCreateAutoCRDTO> lst = getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(SRCreateAutoCRDTO.class));
    boolean checkPath = false;
    if (lst != null && !lst.isEmpty()) {
      for(SRCreateAutoCRDTO srCreateAutoCRDTO:lst){
        if(StringUtils.isNotNullOrEmpty(srCreateAutoCRDTO.getPathFileProcess())){
          checkPath= true;
          break;
        }
      }
      if(checkPath || "vi_VN".equalsIgnoreCase(I18n.getLocale())){
        return lst;
      }
    }
    parameters.put("p_leeLocale", "vi_VN");
    return getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(SRCreateAutoCRDTO.class));
  }
}
