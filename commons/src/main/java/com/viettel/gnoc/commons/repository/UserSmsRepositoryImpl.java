package com.viettel.gnoc.commons.repository;

import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.commons.model.UserSmsEntity;
import com.viettel.gnoc.commons.model.UsersEntity;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.kedb.dto.UserSmsDTO;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class UserSmsRepositoryImpl extends BaseRepository implements
    UserSmsRepository {

  @Override
  public ResultInSideDto add(UserSmsDTO userSmsDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    UserSmsEntity woCdTempEntity = getEntityManager().merge(userSmsDTO.toEntity());
    resultInSideDto.setId(woCdTempEntity.getUserId());
    return resultInSideDto;
  }

  @Override
  public UserSmsDTO getDetail(Long userId) {
    UserSmsEntity userSmsEntity = getEntityManager().find(UserSmsEntity.class, userId);
    if(userSmsEntity != null){
      UserSmsDTO userSmsDTO = userSmsEntity.toDTO();
      return userSmsDTO;
    }
    else{
      return null;
    }
  }

  @Override
  public List<UserSmsDTO> getUserReceiveSMSEmailByTypeCode(String typeCode, String emailSMS) {
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_COMMON,
        "get-User-Receive-SMS-Email-By-Type-Code");
    Map<String, Object> parameters = new HashMap<>();
    if (StringUtils.isNotNullOrEmpty(typeCode)) {
      sql += " AND LOWER(u.TYPE_CODE) LIKE :typeCode ESCAPE '\\'";
      parameters.put("typeCode", StringUtils.convertLowerParamContains(typeCode));
    }
    if (StringUtils.isNotNullOrEmpty(emailSMS)) {
      if (emailSMS.equals(Constants.EMAIL)) {
        sql += " AND u.SMS_TYPE IN (623, 624)";
      } else if (emailSMS.equals(Constants.SMS)) {
        sql += " AND u.SMS_TYPE IN (622, 624)";
      }
    } else {
      sql += " AND u.SMS_TYPE IN (622, 623, 624)";
    }
    return getNamedParameterJdbcTemplate().query(sql, parameters,
        BeanPropertyRowMapper.newInstance(UserSmsDTO.class));
  }

  @Override
  public ResultInSideDto insertUserSms(UserSmsDTO userSmsDTO){
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    try{
      String typeCodeValue = userSmsDTO.getTypeCode();
      if(typeCodeValue != null){
        String[] typeArray = typeCodeValue.trim().split(",");
        String typeCode = "";
        for(String itemId : typeArray){
          String sql = "select ITEM_CODE from COMMON_GNOC.CAT_ITEM where ITEM_ID = :itemId";
          Map<String, Object> parameters = new HashMap<>();
          parameters.put("itemId", itemId);
          List<CatItemDTO> catItem = getNamedParameterJdbcTemplate()
              .query(sql, parameters, BeanPropertyRowMapper.newInstance(CatItemDTO.class));
          if(catItem != null && !catItem.isEmpty()){
            typeCode = typeCode + catItem.get(0).getItemCode() + ",";
          }
        }
        if(typeCode != null){
          typeCode = typeCode.substring(0, typeCode.length() - 1);
          userSmsDTO.setTypeCode(typeCode);
        }
      }
      else if(typeCodeValue == null){
        userSmsDTO.setTypeCode(null);
      }
      userSmsDTO.setLastUpdateTime(new Date());
      resultInSideDto.setKey(RESULT.SUCCESS);
      getEntityManager().persist(userSmsDTO.toEntity());
      return resultInSideDto;
    }catch (Exception err){
      resultInSideDto.setKey(RESULT.FAIL);
      log.error(err.getMessage(), err);
      return  resultInSideDto;
    }
  }

  @Override
  public ResultInSideDto updateUserSms(UserSmsDTO userSmsDTO){
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    try{
      String typeCodeValue = userSmsDTO.getTypeCode();
      if(typeCodeValue != null){
        String[] typeArray = typeCodeValue.trim().split(",");
        String typeCode = "";
        for(String itemId : typeArray){
          String sql = "select ITEM_CODE from COMMON_GNOC.CAT_ITEM where ITEM_ID = :itemId";
          Map<String, Object> parameters = new HashMap<>();
          parameters.put("itemId", itemId);
          List<CatItemDTO> catItem = getNamedParameterJdbcTemplate()
              .query(sql, parameters, BeanPropertyRowMapper.newInstance(CatItemDTO.class));
          if(catItem != null && !catItem.isEmpty()){
            typeCode = typeCode + catItem.get(0).getItemCode() + ",";
          }
        }
        if(typeCode != null){
          typeCode = typeCode.substring(0, typeCode.length() - 1);
          userSmsDTO.setTypeCode(typeCode);
        }
      }
      else if(typeCodeValue == null){
        userSmsDTO.setTypeCode(null);
      }
      userSmsDTO.setLastUpdateTime(new Date());
      getEntityManager().merge(userSmsDTO.toEntity());
      resultInSideDto.setKey(RESULT.SUCCESS);
      return resultInSideDto;
    }catch (Exception err){
      resultInSideDto.setKey(RESULT.FAIL);
      log.error(err.getMessage(), err);
      return resultInSideDto;
    }
  }
}
