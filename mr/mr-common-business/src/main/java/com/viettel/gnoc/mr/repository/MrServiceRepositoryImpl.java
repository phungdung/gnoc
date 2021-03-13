package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UserTokenGNOC;
import com.viettel.gnoc.commons.model.UsersEntity;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.maintenance.dto.MrCdBatteryDTO;
import com.viettel.gnoc.maintenance.dto.MrNodesDTO;
import com.viettel.gnoc.maintenance.model.MrNodesEntity;
import com.viettel.gnoc.ws.dto.UserGroupCategoryDTO;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
/**
 *
 * @author Dunglv3
 */
public class MrServiceRepositoryImpl extends BaseRepository implements MrServiceRepository {

  @Override
  public List<MrCdBatteryDTO> getListMrCdBattery(MrCdBatteryDTO dto) {
    String sql = " ";
    try {
      Map<String, Object> params = new HashMap<>();
      sql = "  SELECT DC_POWER_ID,"
          + "  STATION_ID, "
          + "  STATION_CODE, "
          + "  DC_POWER, "
          + "  PROVINCE, "
          + "  STAFF_NAME, "
          + "  SUBSTR(STAFF_EMAIL,0,INSTR(STAFF_EMAIL,'@', 1, 1) - 1) staffMail, "
          + "  STAFF_PHONE, "
          + "  DISCHARGE_TYPE, "
          + "  TIME_DISCHARGE, "
          + "  RECENT_DISCHARGE_CD, "
          + "  MR_CODE, "
          + "  WO_CODE, "
          + "  STATUS, "
          + "  CREATED_TIME, "
          + "  UPDATED_TIME, "
          + "  UPDATED_USER, "
          + "  RECENT_DISCHAGE_NOC, "
          + "  RECENT_DISCHAGE_GNOC, "
          + "  PRODUCTION_TECHNOLOGY, "
          + "  DISTRICT_CODE, "
          + "  DISCHARGE_CONFIRM, "
          + "  RESULT_DISCHARGE, "
          + "  DISCHARGE_NUMBER, "
          + "  DISCHARGE_REASON_FAIL, "
          + "  ISWOACCU IsWoAccu"
          + "  FROM  MR_CD_BATTERY "
          + "  WHERE 1 = 1 ";
      if (dto != null) {
        if (!StringUtils.isStringNullOrEmpty(dto.getStaffName())) {
          sql += " AND STAFF_NAME = :staff_name";
          params.put("staff_name", dto.getStaffName());
        }
        if (!StringUtils.isStringNullOrEmpty(dto.getStaffMail())) {
          sql += " AND STAFF_EMAIL LIKE :staff_email";
          params.put("saff_email", dto.getStaffMail() + "@%");
        }
        if (!StringUtils.isStringNullOrEmpty(dto.getStationCode())) {
          sql += " AND STATION_CODE = :station_code ";
          params.put("station_code", dto.getStationCode());
        }
        if (!StringUtils.isStringNullOrEmpty(dto.getDcPower())) {
          sql += " AND DC_POWER = :dc_power ";
          params.put("dc_power", dto.getDcPower());
        }
        if (!StringUtils.isStringNullOrEmpty(dto.getProvince())) {
          sql += " AND PROVINCE = :province ";
          params.put("province", dto.getProvince());
        }
        if (StringUtils.isNotNullOrEmpty(dto.getDischargeType())) {
          sql += " AND DISCHARGE_TYPE =:dischargeType ";
          params.put("dischargeType", dto.getDischargeType());
        }
      }
      return getNamedParameterJdbcTemplate()
          .query(sql, params, BeanPropertyRowMapper.newInstance(MrCdBatteryDTO.class));
    } catch (Exception e) {
      log.error(e.getMessage());
    }

    return null;
  }

  @Override
  public ResultInSideDto insertList(List<MrNodesDTO> listMrNodesDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    if (listMrNodesDTO != null && !listMrNodesDTO.isEmpty()) {
      for (MrNodesDTO item : listMrNodesDTO) {
        MrNodesEntity entity = getEntityManager().merge(item.toEntity());
        resultInSideDto.setId(entity.getMrNodeId());
        resultInSideDto.setKey(Constants.RESULT.SUCCESS);
      }
    }
    return resultInSideDto;
  }

  @Override
  public UsersEntity getUserByUserId(Long userId) {
    return getEntityManager().find(UsersEntity.class, userId);
  }

  @Override
  public ResultDTO updateWoCodeMrCode(MrCdBatteryDTO dto) throws DataAccessException {
    ResultDTO resultDTO = new ResultDTO();
    String sql;
    try {
      Map<String, Object> params = new HashMap<>();
      sql = " UPDATE OPEN_PM.MR_CD_BATTERY SET MR_CODE = :mrCode , WO_CODE = :woCode "
          + " WHERE STATION_CODE = :stationCode"
          + " AND DC_POWER = :dcPower "
          + " AND DISCHARGE_TYPE = :dischargeType "
          + " AND STATUS = '1' ";
      params.put("mrCode", dto.getMrCode());
      params.put("woCode", dto.getWoCode());
      params.put("stationCode", dto.getStationCode());
      params.put("dcPower", dto.getDcPower());
      params.put("dischargeType", dto.getDischargeType());
      getNamedParameterJdbcTemplateNormal().update(sql, params);
//      getNamedParameterJdbcTemplate().update(sql, params);
      resultDTO.setId("1");
      resultDTO.setKey("SUCCESS");
    } catch (Exception e) {
      resultDTO.setId("0");
      resultDTO.setKey("FAIL");
      resultDTO.setMessage(e.getMessage());
      log.error(e.getMessage());
    }
    return resultDTO;
  }

  @Override
  public UserTokenGNOC getUserInfor(String userName) {
    UserTokenGNOC userTokenGNOC = new UserTokenGNOC();
    String sql;
    try {
      if (userName != null && !"".equals(userName.trim())) {
        Map<String, Object> params = new HashMap<>();
        sql = "  select us.username userName, us.fullname fullName, us.user_id userId, "
            + "   us.unit_id unitId, ut.unit_code unitCode, ut.unit_name unitName, "
            + "   us.mobile, us.user_time_zone userTimeZone "
            + "   from common_gnoc.users us"
            + "   left join common_gnoc.unit ut on us.unit_id = ut.unit_id"
            + "   where us.is_enable=1 and lower(us.username) =:username";
        params.put("username", userName);
        List<UserTokenGNOC> list = getNamedParameterJdbcTemplate()
            .query(sql, params, BeanPropertyRowMapper.newInstance(UserTokenGNOC.class));
        if (list != null && list.size() > 0) {
          userTokenGNOC = list.get(0);
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage());
    }
    return userTokenGNOC;
  }

  @Override
  public List<UserGroupCategoryDTO> getListUserGroupBySystem(UserGroupCategoryDTO dto) {
    Map<String, Object> params = new HashMap<>();
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_MR, "get-List-User-Group-By-System");
    params.put("leeLocale", dto.getProxyLocale());
    if (!StringUtils.isStringNullOrEmpty(dto.getUgcySystem())) {
      sql += " AND leugc.ugcy_system = :ugcySystem ";
      params.put("ugcySystem", dto.getUgcySystem());
    }
    sql += " ORDER BY leugc.ugcy_code ";
    List<UserGroupCategoryDTO> list = getNamedParameterJdbcTemplate().query(sql, params,
        BeanPropertyRowMapper.newInstance(UserGroupCategoryDTO.class));
    return list;
  }
}
