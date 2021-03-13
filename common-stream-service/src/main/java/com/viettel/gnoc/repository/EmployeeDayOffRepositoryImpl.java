package com.viettel.gnoc.repository;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DateUtil;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.EmployeeDayOffDTO;
import com.viettel.gnoc.cr.model.EmployeeDayOffEntity;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class EmployeeDayOffRepositoryImpl extends BaseRepository implements
    EmployeeDayOffRepository {


  @Override
  public Datatable getListEmployeeDayOff(EmployeeDayOffDTO dto) {
    BaseDto baseDto = sqlListEmployeeDayOff(dto);
    return getListDataTableBySqlQuery(baseDto.getSqlQuery(), baseDto.getParameters(), dto.getPage(),
        dto.getPageSize(), EmployeeDayOffDTO.class,
        dto.getSortName(),
        dto.getSortType());
  }

  @Override
  public List<EmployeeDayOffDTO> getListEmployeeDayOffExport(EmployeeDayOffDTO dto) {
    BaseDto baseDto = sqlListEmployeeDayOff(dto);
    return getNamedParameterJdbcTemplate()
        .query(baseDto.getSqlQuery(), baseDto.getParameters(),
            BeanPropertyRowMapper.newInstance(EmployeeDayOffDTO.class));
  }


  public BaseDto sqlListEmployeeDayOff(EmployeeDayOffDTO dto) {
    BaseDto baseDto = new BaseDto();
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_EMPLOYEE_DAYOFF, "getListEmployeeDayOff");
    Map<String, Object> parameters = new HashMap<>();
    if (!StringUtils.isStringNullOrEmpty(dto.getEmpId())) {
      sql += " AND t1.EMP_ID = :empId";
      parameters.put("empId", dto.getEmpId());
    }
    if (!StringUtils.isStringNullOrEmpty(dto.getDayOff())) {
      sql += " AND to_char(t1.DAYOFF, 'dd/mm/yyyy') =:dayOff ";
      parameters.put("dayOff", DateUtil
          .date2ddMMyyyyString(dto.getDayOff()));
    }
    if (!StringUtils.isStringNullOrEmpty(dto.getEmpUsername())) {
      sql += " AND t1.EMP_USERNAME = :empUsername";
      parameters.put("empUsername", dto.getEmpUsername());
    }
    if (!StringUtils.isStringNullOrEmpty(dto.getUnitId())) {
      sql += " AND t3.UNIT_ID = :unitId";
      parameters.put("unitId", dto.getUnitId());
    }
    if (!StringUtils.isStringNullOrEmpty(dto.getEmpUnit())) {
      sql += " AND t1.EMP_UNIT = :empUnit";
      parameters.put("empUnit", dto.getEmpUnit());
    }
    if (!StringUtils.isStringNullOrEmpty(dto.getVacation())) {
      sql += " AND t1.VACATION = :vacation";
      parameters.put("vacation", dto.getVacation());
    }
    if (!StringUtils.isStringNullOrEmpty(dto.getSearchAll())) {
      sql += " AND (LOWER(t1.EMP_USERNAME)  LIKE :searchAll ESCAPE '\\')";
      parameters.put("searchAll", StringUtils.convertLowerParamContains(dto.getSearchAll()));
    }

    if (!StringUtils.isStringNullOrEmpty(dto.getDayOffFrom()) && !StringUtils
        .isStringNullOrEmpty(dto.getDayOffTo())) {
      sql +=
          " AND (t1.DAYOFF BETWEEN  TO_DATE(:dayOffFrom , 'dd/mm/yyyy') AND TO_DATE(:dayOffTo, 'dd/mm/yyyy'))";
      parameters.put("dayOffFrom", DateUtil.date2ddMMyyyyString(dto.getDayOffFrom()));
      parameters.put("dayOffTo", DateUtil.date2ddMMyyyyString(dto.getDayOffTo()));
    }

    sql += " ORDER BY t1.ID_DAYOFF DESC ";
    baseDto.setSqlQuery(sql);
    baseDto.setParameters(parameters);
    return baseDto;
  }


  @Override
  public EmployeeDayOffDTO findEmployeeDayOffById(Long id) {
    if (id != null && id > 0) {
      return getEntityManager().find(EmployeeDayOffEntity.class, id).toDTO();
    }
    return null;
  }

  @Override
  public String deleteEmployeeDayOffById(Long id) {
    return deleteById(EmployeeDayOffEntity.class, id, colId);
  }

  @Override
  public List<EmployeeDayOffDTO> getListEmployeeDayOffExport2() {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_EMPLOYEE_DAYOFF, "getListEmployeeIDExport2");
    Map<String, Object> parameters = new HashMap<>();
    return getNamedParameterJdbcTemplate()
        .query(sql, parameters,
            BeanPropertyRowMapper.newInstance(EmployeeDayOffDTO.class));
  }

  @Override
  public ResultInSideDto insertEmployeeDayOff(List<EmployeeDayOffDTO> employeeDayOffDTOS) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();

    for (EmployeeDayOffDTO dto : employeeDayOffDTOS) {
      String sql =
          "SELECT * FROM COMMON_GNOC.EMPLOYEE_DAYOFF t1 WHERE t1.EMP_ID = :empId AND t1.DAYOFF = to_date(:dayOff  , 'DD-MM-YYYY HH24:MI:SS')";
      Map<String, Object> paramters = new HashMap<>();
      paramters.put("empId", dto.getEmpId());
      paramters.put("dayOff", DateUtil.date2ddMMyyyyHHMMss(dto.getDayOff()));
      List<EmployeeDayOffDTO> datas = getNamedParameterJdbcTemplate()
          .query(sql, paramters, BeanPropertyRowMapper.newInstance(EmployeeDayOffDTO.class));
      if (datas != null && datas.size() > 0) {
        resultInSideDto.setKey(RESULT.DUPLICATE);
        resultInSideDto.setMessage(
            I18n.getLanguage("employeeDayOff.existed"));
        break;
      } else {
        EmployeeDayOffEntity entity = dto.toEntity();
        resultInSideDto = insertByModel(entity, colId);
        resultInSideDto.setKey(RESULT.SUCCESS);
      }
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto updateEmployeeDayOff(EmployeeDayOffDTO dto) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    deleteEmployeeDayOffById(dto.getIdDayOff());
    List<EmployeeDayOffDTO> employeeDayOffDTOList = new ArrayList<>();
    employeeDayOffDTOList.add(dto);
    dto.setIdDayOff(null);
    resultInSideDto = insertEmployeeDayOff(employeeDayOffDTOList);
    return resultInSideDto;
  }

  @Override
  public List<UsersInsideDto> getListUserInUnit(UnitDTO dto) {
    String sql = "";
    Map<String, Object> parameters = new HashMap<>();
    if (dto.getIsCommittee() == 0L || StringUtils.isStringNullOrEmpty(dto.getIsCommittee())) {
      sql = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_EMPLOYEE_DAYOFF, "getListUserInUnit1");
      parameters.put("unitId", dto.getUnitId());
    } else {
      sql = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_EMPLOYEE_DAYOFF, "getListUserInUnit2");
      parameters.put("unitId", dto.getUnitId());
    }
    List<UsersInsideDto> usersInsideDtos = getNamedParameterJdbcTemplate().query(sql, parameters,
        BeanPropertyRowMapper.newInstance(UsersInsideDto.class));
    return usersInsideDtos;
  }

  @Override
  public List<UsersInsideDto> checkUserIsEnable(String userId) {
    String sql = "SELECT * FROM COMMON_GNOC.USERS t1 WHERE t1.USER_ID = :userId AND t1.IS_ENABLE = 0";
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("userId", userId);
    List<UsersInsideDto> usersInsideDtos = getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(
            UsersInsideDto.class));
    return usersInsideDtos;
  }

  @Override
  public EmployeeDayOffDTO checkEmployeeDayOffExist(Long empId, Date dayOff, String vacation) {
    String sql =
        "SELECT * FROM COMMON_GNOC.EMPLOYEE_DAYOFF t1 WHERE t1.EMP_ID = :empId AND t1.DAYOFF = TO_DATE('"
            + DateUtil.date2ddMMyyyyString(dayOff)
            + "'  , 'dd/mm/yyyy') AND t1.VACATION = :vacation";
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("empId", empId);
    parameters.put("vacation", vacation);
    List<EmployeeDayOffDTO> dataDTO = getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(EmployeeDayOffDTO.class));
    if (dataDTO != null && dataDTO.size() > 0) {
      return dataDTO.get(0);
    }
    return null;
  }

  private static final String colId = "idDayOff";
}
