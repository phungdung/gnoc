package com.viettel.gnoc.repository;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.LogChangeConfigDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DateUtil;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.CfgChildArrayDTO;
import com.viettel.gnoc.cr.dto.EmployeeImpactDTO;
import com.viettel.gnoc.cr.dto.ItemDataCRDTO;
import com.viettel.gnoc.cr.model.EmployeeImpactEntity;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;
import viettel.passport.client.UserToken;

@Slf4j
@Repository
public class EmployeeImpactRepositoryImpl extends BaseRepository implements
    EmployeeImpactRepository {

  @Autowired
  TicketProvider ticketProvider;

  @Override
  public ResultInSideDto insertEmpImpact(EmployeeImpactDTO employeeImpactDTO) {
    UserToken userToken = ticketProvider.getUserToken();
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    LogChangeConfigDTO logChangeConfigDTO = new LogChangeConfigDTO();
    logChangeConfigDTO.setUpdateTime(new Date());
    logChangeConfigDTO.setUserChange(userToken.getUserName());
    employeeImpactDTO.setUpdatedUser(userToken.getUserName());
    EmployeeImpactEntity entity = employeeImpactDTO.toEntity();
    String sql = "SELECT * FROM COMMON_GNOC.EMPLOYEE_IMPACT t1 WHERE t1.EMP_ID = :empId AND t1.EMP_ARRAY = :empArray AND t1.EMP_ARRAY_CHILD = :empArrayChild AND t1.EMP_LEVEL = :empLevel";
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("empId", employeeImpactDTO.getEmpId());
    parameters.put("empArray", employeeImpactDTO.getEmpArray());
    parameters.put("empArrayChild", employeeImpactDTO.getEmpArrayChild());
    parameters.put("empLevel", employeeImpactDTO.getEmpLevel());
    List<EmployeeImpactDTO> list = getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(EmployeeImpactDTO.class));
    if (list != null && list.size() > 0) {
      resultInSideDto.setKey(RESULT.DUPLICATE);
      resultInSideDto.setMessage(
          I18n.getLanguage("employeeDayOff.existed"));
      logChangeConfigDTO.setContent("InsertEmpImpact fail");
    } else if (employeeImpactDTO.getStatusUpdate() == 1L) {
      resultInSideDto = insertByModel(entity, colId);
      logChangeConfigDTO.setFunctionCode("UpdateEmpImpact");
      logChangeConfigDTO
          .setContent("UpdateEmpImpact with ID: " + employeeImpactDTO.getIdImpactSave());
    } else {
      logChangeConfigDTO.setFunctionCode("InsertEmpImpact");
      resultInSideDto = insertByModel(entity, colId);
      logChangeConfigDTO.setContent("InsertEmpImpact with ID: " + resultInSideDto.getId());
    }
    getEntityManager().merge(logChangeConfigDTO.toEntity());
    return resultInSideDto;
  }

  @Override
  public List<ItemDataCRDTO> getListParentArray() {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_EMPLOYEE_IMPACT, "getListParentArray");
    Map<String, Object> parameters = new HashMap<>();
    List<ItemDataCRDTO> itemDataCRDTOS = getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper
            .newInstance(ItemDataCRDTO.class));
    return itemDataCRDTOS;
  }

  @Override
  public Datatable getListLevel(CatItemDTO catItemDTO) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_EMPLOYEE_IMPACT, "getListLevel");
    Map<String, Object> parameters = new HashMap<>();
    return getListDataTableBySqlQuery(sql, parameters, catItemDTO.getPage(),
        catItemDTO.getPageSize(), CatItemDTO.class,
        catItemDTO.getSortName(),
        catItemDTO.getSortType());
  }

  @Override
  public List<CfgChildArrayDTO> getListChildArray(CfgChildArrayDTO cfgChildArrayDTO) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_EMPLOYEE_IMPACT, "getListChildArray");
    String locale = I18n.getLocale();
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("p_leeLocale", locale);
    if (cfgChildArrayDTO.getParentId() != null) {
      sql += " AND t1.PARENT_ID = :p_parent_id ";
      parameters.put("p_parent_id", cfgChildArrayDTO.getParentId());
    }
    List<CfgChildArrayDTO> cfgChildArrayDTOS = getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper
            .newInstance(CfgChildArrayDTO.class));
    return cfgChildArrayDTOS;
  }

  @Override
  public Datatable getListEmployeeImpact(EmployeeImpactDTO employeeImpactDTO) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_EMPLOYEE_IMPACT, "getListEmployeeImpact");
    Map<String, Object> parameters = new HashMap<>();
    if (employeeImpactDTO.getEmpId() != null) {
      sql += " AND t1.EMP_ID = :p_emp_id ";
      parameters.put("p_emp_id", employeeImpactDTO.getEmpId());
    }
    if (!StringUtils.isStringNullOrEmpty(employeeImpactDTO.getEmpUsername())) {
      sql += "  AND LOWER(t1.EMP_USERNAME) LIKE :empUserName ESCAPE '\\'";
      parameters.put("empUserName",
          StringUtils.convertLowerParamContains(employeeImpactDTO.getEmpUsername()));
    }
    if (!StringUtils.isStringNullOrEmpty(employeeImpactDTO.getUpdatedUser())) {
      sql += " AND LOWER(t1.UPDATED_USER) LIKE :updatedUser  ESCAPE '\\'";
      parameters.put("updatedUser",
          StringUtils.convertLowerParamContains(employeeImpactDTO.getUpdatedUser()));
    }
    if (!StringUtils.isStringNullOrEmpty(employeeImpactDTO.getUnitId())) {
      sql += " AND t2.UNIT_ID = :unitId ";
      parameters.put("unitId", employeeImpactDTO.getUnitId());

    }
    if (employeeImpactDTO.getEmpArray() != null) {
      sql += " AND t1.EMP_ARRAY = :empArray ";
      parameters.put("empArray", employeeImpactDTO.getEmpArray());
    }
    if (employeeImpactDTO.getEmpLevel() != null) {
      sql += " AND t1.EMP_LEVEL = :level ";
      parameters.put("level", employeeImpactDTO.getEmpLevel());
    }
    if (employeeImpactDTO.getEmpArrayChild() != null) {
      sql += " AND t1.EMP_ARRAY_CHILD = :empArrayChild";
      parameters.put("empArrayChild", employeeImpactDTO.getEmpArrayChild());
    }
    if (employeeImpactDTO.getUpdatedTimeFrom() != null
        && employeeImpactDTO.getUpdatedTimeTo() != null) {
      sql += " AND (t1.UPDATED_TIME BETWEEN TO_DATE(:updatedTimeFrom  , 'dd/mm/yyyy') AND TO_DATE(:updatedTimeTo, 'dd/mm/yyyy'))";
      parameters.put("updatedTimeFrom", DateUtil
          .date2ddMMyyyyString(employeeImpactDTO.getUpdatedTimeFrom()));
      parameters.put("updatedTimeTo", DateUtil
          .date2ddMMyyyyString(employeeImpactDTO.getUpdatedTimeTo()));
    }

    if (StringUtils.isNotNullOrEmpty(employeeImpactDTO.getSearchAll())) {
      sql += " AND (LOWER(t1.EMP_USERNAME) LIKE :searchAll ESCAPE '\\')";
      parameters.put("searchAll",
          StringUtils.convertLowerParamContains(employeeImpactDTO.getSearchAll()));
    }
    sql += " ORDER BY t1.ID_IMPACT DESC ";
    return getListDataTableBySqlQuery(sql, parameters, employeeImpactDTO.getPage(),
        employeeImpactDTO.getPageSize(), EmployeeImpactDTO.class,
        employeeImpactDTO.getSortName(),
        employeeImpactDTO.getSortType());
  }

  @Override
  public String deleteEmpImpact(Long id) {
    return deleteById(EmployeeImpactEntity.class, id, colId);
  }

  @Override
  public String deleteEmpImpactInUpdate(Long idImpact) {
    return deleteById(EmployeeImpactEntity.class, idImpact, colId);
  }

  @Override
  public EmployeeImpactDTO findEmpImpactById(Long id) {
    if (id != null && id > 0) {
      return getEntityManager().find(EmployeeImpactEntity.class, id).toDTO();
    }
    return null;
  }

  private static final String colId = "idImpact";
}
