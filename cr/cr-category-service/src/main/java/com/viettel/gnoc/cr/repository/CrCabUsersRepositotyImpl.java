package com.viettel.gnoc.cr.repository;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.DataItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.CrCabUsersDTO;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.cr.model.CrCabUsersEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class CrCabUsersRepositotyImpl extends BaseRepository implements CrCabUsersRepositoty {

  @Override
  public ResultInSideDto insertCrCabUsers(CrCabUsersDTO crCabUsersDTO) {
    return insertByModel(crCabUsersDTO.toEntity(), colId);
  }

  @Override
  public ResultInSideDto updateCrCabUsers(CrCabUsersDTO crCabUsersDTO) {
    ResultInSideDto resultDto = new ResultInSideDto();
    resultDto.setKey(RESULT.SUCCESS);
    resultDto.setId(crCabUsersDTO.getCrCabUsersId());
    getEntityManager().merge(crCabUsersDTO.toEntity());
    return resultDto;
  }

  @Override
  public ResultInSideDto deleteCrCabUsers(Long id) {
    ResultInSideDto resultDto = new ResultInSideDto();
    resultDto.setKey(deleteById(CrCabUsersEntity.class, id, colId));
    return resultDto;
  }

  @Override
  public ResultInSideDto deleteListCrCabUsers(List<CrCabUsersDTO> crCabUsersDTOS) {
    ResultInSideDto resultDto = new ResultInSideDto();
    String result = deleteByListDTO(crCabUsersDTOS, CrCabUsersEntity.class, colId);
    resultDto.setKey(result);
    return resultDto;
  }

  @Override
  public Datatable getAllInfoCrCABUsers(CrCabUsersDTO crCabUsersDTO) {
    BaseDto baseDto = sqlSearch(crCabUsersDTO);
    return getListDataTableBySqlQuery(baseDto.getSqlQuery(), baseDto.getParameters(),
        baseDto.getPage(), baseDto.getPageSize(), CrCabUsersDTO.class,
        crCabUsersDTO.getSortName(),
        crCabUsersDTO.getSortType());

  }

  @Override
  public List<UnitDTO> getAllUserInUnitCrCABUsers(Long deptId, Long userId, String userName,
      String fullName, String staffCode, String deptName, String deptCode) {
    //get-all-userInnitCrCABUsers
    Map<String, Object> parameters = new HashMap<>();
    String sqlQuery = SQLBuilder.getSqlQueryById("crcabusers", "get-all-userInnitCrCABUsers");
    if (deptId != null) {
      sqlQuery += " and  ut.unit_id =:unit_id";
      parameters.put("unit_id", deptId);
    }
    if (userId != null) {
      sqlQuery += " and  us.user_id =:unit_ids";
      parameters.put("unit_ids", userId);
    }
    if (userName != null && !"".equals(userName.trim())) {
      sqlQuery += " and lower(us.username) like lower(:username)";
      parameters.put("username", StringUtils.convertLowerParamContains(userName));
    }
    if (fullName != null && !"".equals(fullName.trim())) {
      sqlQuery += " and lower(us.fullname) like lower(:fullname)";
      parameters.put("fullname", StringUtils.convertLowerParamContains(fullName));
    }
    if (deptCode != null && !"".equals(deptCode.trim())) {
      sqlQuery += " and lower(ut.unit_code) like lower(:unit_code)";
      parameters.put("unit_code", StringUtils.convertLowerParamContains(deptCode));
    }
    if (deptName != null && !"".equals(deptName.trim())) {
      sqlQuery += " and lower(ut.unit_name) like lower(:unit_name)";
      parameters.put("unit_name", StringUtils.convertLowerParamContains(deptName));
    }
    if (staffCode != null && !"".equals(staffCode.trim())) {
      sqlQuery += " and lower(us.staff_Code) like lower(:staff_Code)";
      parameters.put("staff_Code", StringUtils.convertLowerParamContains(staffCode));
    }
    sqlQuery += " order by us.username";
    return getNamedParameterJdbcTemplate()
        .query(sqlQuery, parameters, BeanPropertyRowMapper.newInstance(UnitDTO.class));
  }

  @Override
  public List<ItemDataCRInside> getListImpactSegmentCBB() {
    Map<String, Object> parameters = new HashMap<>();
    String sqlQuery = "select ist.impact_segment_id valueStr, ist.impact_segment_name displayStr,ist.impact_segment_code secondValue from OPEN_PM.impact_segment ist where is_active = 1 and applied_system = 2";
    return getNamedParameterJdbcTemplate()
        .query(sqlQuery, parameters, BeanPropertyRowMapper.newInstance(ItemDataCRInside.class));
  }

  @Override
  public List<UnitDTO> getListUnitCrCABUsers(UnitDTO unitDTO) {
    return null;
  }

  @Override
  public List<CrCabUsersDTO> getListUserFullName() {
    Map<String, Object> parameters = new HashMap<>();
    String sqlQuery = "select us.USER_ID userID,us.username || '('||us.fullname||')' userFullName from  common_gnoc.users us";
    return getNamedParameterJdbcTemplate()
        .query(sqlQuery, parameters, BeanPropertyRowMapper.newInstance(CrCabUsersDTO.class));
  }

  @Override
  public List<CrCabUsersDTO> getListSegmentName() {
    Map<String, Object> parameters = new HashMap<>();
    String sqlQuery = "select ist.impact_segment_id impactSegmentId,ist.impact_segment_name segmentName from open_pm.impact_segment ist where is_active = 1 and applied_system = 2";
    return getNamedParameterJdbcTemplate()
        .query(sqlQuery, parameters, BeanPropertyRowMapper.newInstance(CrCabUsersDTO.class));
  }

  @Override
  public List<CrCabUsersDTO> getListUnitName() {
    Map<String, Object> parameters = new HashMap<>();
    String sqlQuery = " select  u1.unit_id executeUnitId,u1.unit_code || '('||u1.unit_name||')' executeUnitName from common_gnoc.unit u1";
    return getNamedParameterJdbcTemplate()
        .query(sqlQuery, parameters, BeanPropertyRowMapper.newInstance(CrCabUsersDTO.class));
  }

  @Override
  public List<DataItemDTO> getLisFullNameMapUnitCab() {
    Map<String, Object> parameters = new HashMap<>();
    String sqlQuery = SQLBuilder.getSqlQueryById("crcabusers", "getLisFullNameMapUnitCab");
    return getNamedParameterJdbcTemplate()
        .query(sqlQuery, parameters, BeanPropertyRowMapper.newInstance(DataItemDTO.class));
  }

  @Override
  public CrCabUsersDTO findById(Long id) {
    if (id != null && id > 0) {
      return getEntityManager().find(CrCabUsersEntity.class, id).toDTO();
    }
    return null;
  }

  @Override
  public CrCabUsersDTO checkCrCabUsersExist(CrCabUsersDTO dto) {
    List<CrCabUsersEntity> dataEntity = (List<CrCabUsersEntity>) findByMultilParam(
        CrCabUsersEntity.class, "impactSegmentId",
        dto.getImpactSegmentId(), "executeUnitId", dto.getExecuteUnitId(), "cabUnitId",
        dto.getCabUnitId(), "userID", dto.getUserID());
    CrCabUsersDTO crCabUsersDTO = new CrCabUsersDTO();
    if (dataEntity != null && dataEntity.size() > 0) {
      crCabUsersDTO = dataEntity.get(0).toDTO();
      return crCabUsersDTO;
    }
    return crCabUsersDTO;
  }


  public BaseDto sqlSearch(CrCabUsersDTO crCabUsersDTO) {
    BaseDto baseDto = new BaseDto();
    Map<String, Object> parameters = new HashMap<>();
    String sqlQuery = "select * from (";
    sqlQuery += SQLBuilder.getSqlQueryById("crcabusers", "get-all-info-CrCABUsers");
    if (crCabUsersDTO.getImpactSegmentId() != null) {
      sqlQuery += " and cc.IMPACT_SEGMENT_ID =:impactSegmentId";
      parameters.put("impactSegmentId", crCabUsersDTO.getImpactSegmentId());

    }
    if (crCabUsersDTO.getCabUnitId() != null) {
      sqlQuery += " and cc.CAB_UNIT_ID =:cabUnitId ";
      parameters.put("cabUnitId", crCabUsersDTO.getCabUnitId());
    }
    if (crCabUsersDTO.getExecuteUnitId() != null) {
      sqlQuery += " and cc.EXECUTE_UNIT_ID =:executeUnitId ";
      parameters.put("executeUnitId", crCabUsersDTO.getExecuteUnitId());
    }
    if (crCabUsersDTO.getUserID() != null) {
      sqlQuery += " and cc.USER_ID =:userID ";
      parameters.put("userID", crCabUsersDTO.getUserID());
    }
    if (crCabUsersDTO.getCreationUnitId() != null) {
      sqlQuery += " and cc.creation_unit_id = :creationUnitId ";
      parameters.put("creationUnitId", crCabUsersDTO.getCreationUnitId());
    }
    sqlQuery += ")a where 1=1 ";
    if (StringUtils.isNotNullOrEmpty(crCabUsersDTO.getSearchAll())) {
      sqlQuery += "AND LOWER(  a.userFullName) LIKE LOWER(:searchAll) ESCAPE '\\'";
      parameters.put("searchAll", StringUtils.convertLowerParamContains(
          crCabUsersDTO.getSearchAll()));
    }
    sqlQuery += "order by a.crCabUsersId DESC";
    baseDto.setPage(crCabUsersDTO.getPage());
    baseDto.setPageSize(crCabUsersDTO.getPageSize());
    baseDto.setSqlQuery(sqlQuery);
    baseDto.setParameters(parameters);
    return baseDto;
  }

  private static final String colId = "crCabUsersId";
}
