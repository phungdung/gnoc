package com.viettel.gnoc.commons.repository;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.CfgWhiteListIpDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.model.CfgWhiteListIpEntity;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.StringUtils;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author ITSOL
 */
@SuppressWarnings("rawtypes")
@Repository
@Slf4j
@Transactional
public class CfgWhiteListIpRepositoryImpl extends BaseRepository implements
    CfgWhiteListIpRepository {

  @Override
  public Datatable getListDataSearchWeb(CfgWhiteListIpDTO dto) {
    BaseDto baseDto = sqlDataWhiteListIpSearchWeb(dto);
    Datatable datatable = getListDataTableBySqlQuery(baseDto.getSqlQuery(), baseDto.getParameters(),
        dto.getPage(), dto.getPageSize(), CfgWhiteListIpDTO.class, dto.getSortName(),
        dto.getSortType());
    return datatable;
  }

  @Override
  public ResultInSideDto add(CfgWhiteListIpDTO coCfgWhiteListIpDTO) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    Map<String, Object> parameters = new HashMap<>();
    String sql = "SELECT * FROM CFG_WHITE_LIST_IP t WHERE";
    if (coCfgWhiteListIpDTO.getUserName() != null) {
      sql += " t.USER_NAME LIKE :p_user_name ";
      parameters.put("p_user_name", coCfgWhiteListIpDTO.getUserName());
      List<CfgWhiteListIpDTO> list = getNamedParameterJdbcTemplate()
          .query(sql, parameters, BeanPropertyRowMapper.newInstance(CfgWhiteListIpDTO.class));
      if (list != null && list.size() > 0) {
        resultInSideDTO.setKey(Constants.RESULT.FAIL);
        resultInSideDTO.setMessage("USER_ERROR");
        return resultInSideDTO;
      }
    }
    if (coCfgWhiteListIpDTO.getSystemName() != null) {
      sql += "OR t.SYSTEM_NAME LIKE :p_system_name ";
      parameters.put("p_system_name", coCfgWhiteListIpDTO.getSystemName());
      List<CfgWhiteListIpDTO> list = getNamedParameterJdbcTemplate()
          .query(sql, parameters, BeanPropertyRowMapper.newInstance(CfgWhiteListIpDTO.class));
      if (list != null && list.size() > 0) {
        resultInSideDTO.setKey(Constants.RESULT.FAIL);
        resultInSideDTO.setMessage("SYSTEM_ERROR");
        return resultInSideDTO;
      }
    }
    if (coCfgWhiteListIpDTO.getIp() != null) {
      String[] lstIp = coCfgWhiteListIpDTO.getIp().split(",");
//      for (String ip : lstIp) {
//        if (!validIP(ip)) {
//          resultInSideDTO.setKey(Constants.RESULT.FAIL);
//          resultInSideDTO.setMessage("IP_ERROR");
//          return resultInSideDTO;
//        }
//      }
    }
    resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
    coCfgWhiteListIpDTO.setCreateDate(new Date());
    CfgWhiteListIpEntity cfgWhiteListIpEntity = getEntityManager()
        .merge(coCfgWhiteListIpDTO.toEntity());
    resultInSideDTO.setId(cfgWhiteListIpEntity.getId());
    return resultInSideDTO;
  }

  @Override
  public CfgWhiteListIpDTO getDetailById(Long id) {
    Map<String, Object> parameters = new HashMap<>();
    String sql = "SELECT * FROM CFG_WHITE_LIST_IP WHERE ID = :p_id ";
    parameters.put("p_id", id);
    List<CfgWhiteListIpDTO> list = getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(CfgWhiteListIpDTO.class));
    if (list != null && list.size() > 0) {
      return list.get(0);
    }
    return null;
  }

  @Override
  public ResultInSideDto edit(CfgWhiteListIpDTO coCfgWhiteListIpDTO) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    if (coCfgWhiteListIpDTO.getIp() != null) {
      String[] lstIp = coCfgWhiteListIpDTO.getIp().split(",");
//      for (String ip : lstIp) {
//        if (!validIP(ip)) {
//          resultInSideDTO.setKey(Constants.RESULT.FAIL);
//          resultInSideDTO.setMessage("IP_ERROR");
//          return resultInSideDTO;
//        }
//      }
    }
    coCfgWhiteListIpDTO.setCreateDate(new Date());
    resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
    getEntityManager().merge(coCfgWhiteListIpDTO.toEntity());
    return resultInSideDTO;
  }

  @Override
  public ResultInSideDto delete(Long id) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
    CfgWhiteListIpEntity userPassWordEntity = getEntityManager()
        .find(CfgWhiteListIpEntity.class, id);
    getEntityManager().remove(userPassWordEntity);
    return resultInSideDTO;
  }

  @Override
  public CfgWhiteListIpDTO checkIpSystemName(String userName) {
    List<CfgWhiteListIpEntity> dataEntity = (List<CfgWhiteListIpEntity>) findByMultilParam(
        CfgWhiteListIpEntity.class, "userName", userName, "status", 1L);
    if (dataEntity != null && dataEntity.size() > 0) {
      return dataEntity.get(0).toDTO();
    }
    return null;
  }

  private BaseDto sqlDataWhiteListIpSearchWeb(CfgWhiteListIpDTO dto) {
    BaseDto baseDto = new BaseDto();
    Map<String, Object> parameters = new HashMap<>();
    String sql = "SELECT * FROM CFG_WHITE_LIST_IP t where 1 = 1";
    if (dto != null) {
      if (StringUtils.isNotNullOrEmpty(dto.getSearchAll())) {
        sql += " AND (LOWER(t.USER_NAME)) LIKE :searchAll ESCAPE '\\'";
        parameters
            .put("searchAll", StringUtils.convertLowerParamContains(dto.getSearchAll()));
      }
      if (StringUtils.isNotNullOrEmpty(dto.getUserName())) {
        sql += " AND (LOWER(t.USER_NAME)) LIKE :userName ESCAPE '\\'";
        parameters
            .put("userName", StringUtils.convertLowerParamContains(dto.getUserName()));
      }
      if (StringUtils.isNotNullOrEmpty(dto.getIp())){
        sql += " AND (LOWER(t.IP)) LIKE :ip ESCAPE '\\'";
        parameters.put("ip", StringUtils.convertLowerParamContains(dto.getIp()));
      }
      if (StringUtils.isNotNullOrEmpty(dto.getSystemName())) {
        sql += " AND (LOWER(t.SYSTEM_NAME)) LIKE :systemName ESCAPE '\\'";
        parameters
            .put("systemName", StringUtils.convertLowerParamContains(dto.getSystemName()));
      }
    }

    baseDto.setSqlQuery(sql);
    baseDto.setParameters(parameters);
    return baseDto;
  }

  public static boolean validIP(String ip) {
    if (ip == null || ip.isEmpty()) {
      return false;
    }
    ip = ip.trim();
    if ((ip.length() < 6) & (ip.length() > 15)) {
      return false;
    }
    try {
      Pattern pattern = Pattern.compile(
          "^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$");
      Matcher matcher = pattern.matcher(ip);
      return matcher.matches();
    } catch (PatternSyntaxException ex) {
      return false;
    }
  }

}
