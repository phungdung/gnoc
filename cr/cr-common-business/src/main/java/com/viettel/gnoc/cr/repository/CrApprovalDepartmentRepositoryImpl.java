package com.viettel.gnoc.cr.repository;


import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.cr.dto.CrApprovalDepartmentDTO;
import com.viettel.gnoc.cr.dto.CrApprovalDepartmentInsiteDTO;
import com.viettel.gnoc.cr.dto.CrInsiteDTO;
import com.viettel.gnoc.cr.model.CrApprovalDepartmentEntity;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class CrApprovalDepartmentRepositoryImpl extends BaseRepository implements
    CrApprovalDepartmentRepository {

  @Override
  public void deleteAppDeptByCrId(String crId) {
    StringBuilder sql = new StringBuilder();
    if (crId == null) {
      return;
    }
    sql.append(" delete from OPEN_PM.cr_approval_department ");
    sql.append(" where cr_id = :cr_id ");
    Map<String, Object> params = new HashMap<>();
    params.put("cr_id", crId);
    getNamedParameterJdbcTemplate().update(sql.toString(), params);
  }

  @Override
  public List<CrApprovalDepartmentInsiteDTO> getApprovalDepartmentByCrId(
      CrApprovalDepartmentInsiteDTO dto) {
    String sql = SQLBuilder.getSqlQueryById(
        SQLBuilder.SQL_MODULE_CR_APPROVAL_DEPARTMENT, "get-list-cr-approval-department-dto");
    Map<String, Object> parameters = new HashMap<>();
    if (dto.getCrId() != null) {
      sql += " AND cadt.CR_ID = :crId";
      parameters.put("crId", dto.getCrId());
    }
    return getNamedParameterJdbcTemplate()
        .query(sql, parameters,
            BeanPropertyRowMapper.newInstance(CrApprovalDepartmentInsiteDTO.class));
  }

  @Override
  public List<CrApprovalDepartmentInsiteDTO> getApprovalDepartmentByCreator(
      CrApprovalDepartmentInsiteDTO dto) {
    String sql = SQLBuilder.getSqlQueryById(
        SQLBuilder.SQL_MODULE_CR_APPROVAL_DEPARTMENT, "get-approval-department-by-creator");
    Map<String, Object> parameters = new HashMap<>();
    if (dto.getCreatorId() != null) {
      parameters.put("userId", dto.getCreatorId());
    }
    return getNamedParameterJdbcTemplate()
        .query(sql, parameters,
            BeanPropertyRowMapper.newInstance(CrApprovalDepartmentInsiteDTO.class));
  }

  @Override
  public List<CrApprovalDepartmentInsiteDTO> getApprovalDepartmentByProcess(Long crProcessId) {
    String sql = SQLBuilder.getSqlQueryById(
        SQLBuilder.SQL_MODULE_CR_APPROVAL_DEPARTMENT, "get-approval-department-by-process");
    Map<String, Object> parameters = new HashMap<>();
    if (crProcessId != null) {
      parameters.put("crProcessId", crProcessId);
    }
    return getNamedParameterJdbcTemplate().query(sql, parameters, new BeanPropertyRowMapper<>(
        CrApprovalDepartmentInsiteDTO.class));
  }

  @Override
  public List<CrApprovalDepartmentInsiteDTO> search(CrApprovalDepartmentInsiteDTO tDTO, int start,
      int maxResult, String sortType, String sortField) {
    return onSearchEntity(CrApprovalDepartmentEntity.class, tDTO, start, maxResult, sortType,
        sortField);
  }


  @Override
  public String saveListDTONoIdSession(List<CrApprovalDepartmentInsiteDTO> obj) {
    try {
      if (obj == null) {
        return RESULT.SUCCESS;
      }
      int i = 0;
      for (CrApprovalDepartmentInsiteDTO item : obj) {
        getEntityManager().merge(item.toEntity());
        i++;
        if (i % 100 == 0) {
          getEntityManager().flush();
        }
      }
      getEntityManager().flush();
      return RESULT.SUCCESS;
    } catch (SecurityException ex) {
      log.error(ex.getMessage(), ex);
      return ex.getMessage();
    }
  }

  @Override
  public void updateCurentApprovalDepartmentNoFlush(
      CrApprovalDepartmentInsiteDTO crApprovalDepartmentDTO,
      CrInsiteDTO crDTO,
      Long crId,
      Long actionType,
      Long returnCode,
      Long userId,
      Long deptId) {
    try {
      CrApprovalDepartmentEntity entity = getEntityManager().find(CrApprovalDepartmentEntity.class,
          Long.valueOf(crApprovalDepartmentDTO.getCadtId()));
      if (entity != null) {
        entity.setApprovedDate(new Date());
        entity.setUserId(userId);
        if (Constants.CR_ACTION_CODE.REJECT.equals(actionType)) {
          entity.setStatus(2L);
          entity.setReturnCode(returnCode);
        } else {
          entity.setStatus(1L);
          entity.setReturnCode(null);
        }
        entity.setNotes(crDTO.getActionNotes() == null ? "" : crDTO.getActionNotes().trim());
        getEntityManager().merge(entity);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

  @Override
  public List<CrApprovalDepartmentDTO> onSearch(CrApprovalDepartmentDTO tDTO, int start,
      int maxResult, String sortType, String sortField) {
    return onSearchOutSideDTO(CrApprovalDepartmentEntity.class, tDTO, start, maxResult, sortType,
        sortField);
  }
}
