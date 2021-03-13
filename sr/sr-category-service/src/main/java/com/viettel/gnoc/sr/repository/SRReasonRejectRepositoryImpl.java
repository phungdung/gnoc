package com.viettel.gnoc.sr.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.sr.dto.SRReasonRejectDTO;
import com.viettel.gnoc.sr.model.SRReasonRejectEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class SRReasonRejectRepositoryImpl extends BaseRepository implements
    SRReasonRejectRepository {

  @Override
  public Datatable getListSRReasonRejectDTO(SRReasonRejectDTO reasonRejectDTO) {
    Map<String, Object> parameters = new HashMap<>();
    String sql = "Select * from Open_pm.SR_REASON_REJECT a where 1=1 ";

    if (!StringUtils.isStringNullOrEmpty(reasonRejectDTO.getSrStatus())) {
      sql += " AND LOWER(a.SR_STATUS) LIKE :srStatus escape '\\' ";
      parameters
          .put("srStatus", StringUtils.convertLowerParamContains(reasonRejectDTO.getSrStatus()));
    }
    if (!StringUtils.isStringNullOrEmpty(reasonRejectDTO.getReason())) {
      sql += " AND LOWER(a.REASON) LIKE :reason escape '\\' ";
      parameters
          .put("reason", StringUtils.convertLowerParamContains(reasonRejectDTO.getReason()));
    }

    if (StringUtils.isNotNullOrEmpty(reasonRejectDTO.getSearchAll())) {
      sql += " AND ( "
          + " (LOWER(a.SR_STATUS) LIKE :searchAll escape '\\') "
          + "  )";
      parameters
          .put("searchAll",
              StringUtils.convertLowerParamContains(reasonRejectDTO.getSearchAll().trim()));
    }

    sql += " order by a.CREATED_TIME desc";

    return getListDataTableBySqlQuery(sql, parameters,
        reasonRejectDTO.getPage(), reasonRejectDTO.getPageSize(), SRReasonRejectDTO.class,
        reasonRejectDTO.getSortName(), reasonRejectDTO.getSortType());
  }

  @Override
  public ResultInSideDto deleteSRReasonReject(Long id) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    SRReasonRejectEntity entity = getEntityManager().find(SRReasonRejectEntity.class, id);
    if (entity != null) {
      getEntityManager().remove(entity);
      resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    } else {
      resultInSideDto.setKey(Constants.RESULT.NODATA);
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto insertOrUpdateSRReason(SRReasonRejectDTO reasonRejectDTO) {
    ResultInSideDto resultDto = new ResultInSideDto();
    SRReasonRejectEntity entity = getEntityManager().merge(reasonRejectDTO.toEntity());
    resultDto.setId(entity.getReasonRejectId());

    resultDto.setKey(Constants.RESULT.SUCCESS);
    return resultDto;
  }

  @Override
  public SRReasonRejectDTO getSRReasonRejectById(Long id) {

    StringBuilder sql = new StringBuilder();
    Map<String, Object> parameters = new HashMap<>();
    sql.append("select * from SR_REASON_REJECT ts where 1=1 ");

    if (!StringUtils.isStringNullOrEmpty(id)) {
      sql.append(" and ts.REASON_REJECT_ID =  :id");
      parameters.put("id", id);
    }

    List<SRReasonRejectDTO> list = getNamedParameterJdbcTemplate()
        .query(sql.toString(), parameters,
            BeanPropertyRowMapper.newInstance(SRReasonRejectDTO.class));

    if (list != null && !list.isEmpty()) {
      return list.get(0);
    }

    return null;
  }
}
