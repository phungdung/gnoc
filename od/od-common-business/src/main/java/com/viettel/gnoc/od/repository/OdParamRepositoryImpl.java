package com.viettel.gnoc.od.repository;


import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.od.dto.OdParamDTO;
import com.viettel.gnoc.od.dto.OdParamInsideDTO;
import com.viettel.gnoc.od.model.OdParamEntity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

/**
 * @author NamTN
 */
@SuppressWarnings("rawtypes")
@Repository
@Slf4j
public class OdParamRepositoryImpl extends BaseRepository implements OdParamRepository {


  @SuppressWarnings("unchecked")
  @Override
  public List<OdParamInsideDTO> findAll() {
    List<OdParamEntity> lst = findAll(OdParamEntity.class);
    List<OdParamInsideDTO> list = new ArrayList<>();
    for (OdParamEntity item : lst) {
      list.add(item.toDTO());
    }
    return list;
  }

  @Override
  public List<OdParamDTO> getListOdParamByOdId(Long id) {
    String sql =
        "SELECT p.OD_PARAM_ID odParamId, p.OD_ID odId, p.PARAM_TYPE, p.KEY key,p.VALUE value, \n"
            + "p.UPDATED_USER updatedUser, to_char(p.UPDATED_TIME) updatedTime FROM OD_PARAM p WHERE p.OD_ID = :odId";
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("odId", id);
    return getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(OdParamDTO.class));
  }


  @Override
  public ResultInSideDto add(OdParamInsideDTO odParamInsideDTO) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
    OdParamEntity odParamEntity = getEntityManager().merge(odParamInsideDTO.toEntity());
    resultInSideDTO.setId(odParamEntity.getOdId());
    return resultInSideDTO;
  }


}
