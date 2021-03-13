package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.maintenance.dto.MrApprovalDepartmentDTO;
import com.viettel.gnoc.maintenance.dto.MrApproveRolesDTO;
import com.viettel.gnoc.maintenance.dto.MrApproveSearchDTO;
import com.viettel.gnoc.maintenance.model.MrApprovalDepartmentEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class MrApprovalDepartmentRepositoryImpl extends BaseRepository implements
    MrApprovalDepartmentRepository {

  @Override
  public ResultInSideDto insertMrApprovalDepartment(
      MrApprovalDepartmentDTO mrApprovalDepartmentDTO) {
    MrApprovalDepartmentEntity mrApprovalDepartmentEntity = mrApprovalDepartmentDTO.toEntiy();
    return insertByModel(mrApprovalDepartmentEntity, colId);
  }

  @Override
  public List<MrApproveRolesDTO> getLstUserByRole(MrApproveRolesDTO approveRoles) {
    Map<String, Object> params = new HashMap<>();
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.MR_APPROVAL_DEPARTMENT, "get-Lst-User-By-Role");
    if (approveRoles != null) {
      if (!StringUtils.isStringNullOrEmpty(approveRoles.getUserId())) {
        sql += " and us.user_id =:userId ";
        params.put("userId", approveRoles.getUserId());
      }
      if (!StringUtils.isStringNullOrEmpty(approveRoles.getUnitCode())) {
        sql += " and ut.unit_code =:unitCode ";
        params.put("unitCode", approveRoles.getUnitCode());
      }
      if (!StringUtils.isStringNullOrEmpty(approveRoles.getUserName())) {
        sql += " and us.username =:userName ";
        params.put("userName", approveRoles.getUserName());
      }
      if (!StringUtils.isStringNullOrEmpty(approveRoles.getUnitId())) {
        sql += " and ut.unit_id =:unitId ";
        params.put("unitId", approveRoles.getUnitId());
      }
      if (!StringUtils.isStringNullOrEmpty(approveRoles.getRoleCode())) {
        sql += " and rs.role_code =:roleCode ";
        params.put("roleCode", approveRoles.getRoleCode());
      }
      if (!StringUtils.isStringNullOrEmpty(approveRoles.getUnitName())) {
        sql += " and ut.unit_name =:unitName ";
        params.put("unitName", approveRoles.getUnitName());
      }
      if (!StringUtils.isStringNullOrEmpty(approveRoles.getRoleName())) {
        sql += " and rs.role_name =:roleName ";
        params.put("roleName", approveRoles.getRoleName());
      }
      List<MrApproveRolesDTO> list = getNamedParameterJdbcTemplate().query(sql, params,
          BeanPropertyRowMapper.newInstance(MrApproveRolesDTO.class));
      return list;
    }
    return null;
  }

  @Override
  public String updateMrApprovalDepartment(MrApprovalDepartmentDTO mrApprovalDepartmentDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    MrApprovalDepartmentEntity mrDeviceBtsEntity = getEntityManager()
        .merge(mrApprovalDepartmentDTO.toEntiy());
    resultInSideDto.setId(mrDeviceBtsEntity.getMadtId());
    return resultInSideDto.getKey();
  }

  @Override
  public List<MrApproveSearchDTO> getLstMrApproveSearch(MrApproveSearchDTO dto) {
    Map<String, Object> params = new HashMap<>();
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.MR_APPROVAL_DEPARTMENT, "get-lst-mr-approve-search");
    if (dto != null) {
      if (!StringUtils.isStringNullOrEmpty(dto.getMrId())) {
        sql += " and a.mr_id = :mrId";
        params.put("mrId", dto.getMrId());
      }
      if (!StringUtils.isStringNullOrEmpty(dto.getMadtLevel())) {
        sql += " and a.madt_level = :madtLevel ";
        params.put("madtLevel", dto.getMadtLevel());
      }
      if (!StringUtils.isStringNullOrEmpty(dto.getUserId())) {
        sql += " and us.user_id = :userId ";
        params.put("userId", dto.getUserId());
      }
      if (!StringUtils.isStringNullOrEmpty(dto.getUserName())) {
        sql += " and us.username = :username ";
        params.put("username", dto.getUserName());
      }
      if (!StringUtils.isStringNullOrEmpty(dto.getUnitCode())) {
        sql += " and ut.unit_code = :unitCode ";
        params.put("unitCode", dto.getUnitCode());
      }
      if (!StringUtils.isStringNullOrEmpty(dto.getUnitId())) {
        sql += " and ut.unit_id = :unitId";
        params.put("unitId", dto.getUnitId());
      }
      if (!StringUtils.isStringNullOrEmpty(dto.getUnitName())) {
        sql += " and ut.unit_name = :unitName ";
        params.put("unitName", dto.getUnitName());
      }
    }
    sql += " order by a.madt_level ";
    List<MrApproveSearchDTO> list = getNamedParameterJdbcTemplate().query(sql, params,
        BeanPropertyRowMapper.newInstance(MrApproveSearchDTO.class));
    return list;
  }

  @Override
  public List<MrApproveSearchDTO> getLstMrApproveDeptByUser(String userId) {
    Map<String, Object> params = new HashMap<>();
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MR_MAINTENANCE_MNGT, "get-Lst-Mr-Approve-Dept-By-User");
    if(!StringUtils.isStringNullOrEmpty(userId)){
      params.put("userID", userId);
    }
    return getNamedParameterJdbcTemplate().query(sql, params,
        BeanPropertyRowMapper.newInstance(MrApproveSearchDTO.class));
  }

  private static final String colId = "madtId";
}
