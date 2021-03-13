package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.CrHisDTO;
import com.viettel.gnoc.maintenance.dto.MrApproveSearchDTO;
import com.viettel.gnoc.maintenance.dto.MrInsideDTO;
import com.viettel.gnoc.maintenance.model.MrEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class MaintenanceMngtRepositoryImpl extends BaseRepository implements
    MaintenanceMngtRepository {


  @Override
  public List<MrApproveSearchDTO> getLstMrApproveDeptByUser(String userId) {
    Map<String, Object> params = new HashMap<>();
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MR_MAINTENANCE_MNGT, "get-Lst-Mr-Approve-Dept-By-User");
    if (!StringUtils.isStringNullOrEmpty(userId)) {
      params.put("userID", userId);
    }
    List<MrApproveSearchDTO> list = getNamedParameterJdbcTemplate().query(sql, params,
        BeanPropertyRowMapper.newInstance(MrApproveSearchDTO.class));
    return list;
  }

  @Override
  public MrInsideDTO findById(Long id) {
    if (id != null && id > 0) {
      return getEntityManager().find(MrEntity.class, id).toDTO();
    }
    return null;
  }

  @Override
  public List<CrHisDTO> findCrHisByListCrId(List<String> crId) {
    if (crId != null & !crId.isEmpty()) {
      Map<String, Object> parameters = new HashMap<>();
      String sql = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MR_MAINTENANCE_MNGT, "findCrHisByCrId");
      sql += " and his.CR_ID in (:crId) ";
      parameters.put("crId", crId);
      return getNamedParameterJdbcTemplate().query(sql, parameters,
          BeanPropertyRowMapper.newInstance(CrHisDTO.class));
    }
    return null;
  }
}
