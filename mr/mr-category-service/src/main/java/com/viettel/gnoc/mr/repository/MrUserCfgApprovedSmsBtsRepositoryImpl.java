package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.CatLocationDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.maintenance.dto.MrUserCfgApprovedSmsBtsDTO;
import com.viettel.gnoc.maintenance.model.MrUserCfgApprovedSmsBtsEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class MrUserCfgApprovedSmsBtsRepositoryImpl extends BaseRepository implements
    MrUserCfgApprovedSmsBtsRepository {

  @Override
  public Datatable getListMrUserCfgApprovedSmsBts(MrUserCfgApprovedSmsBtsDTO smsBtsDTO) {
    BaseDto baseDto = sqlGetListDataSearchWeb(smsBtsDTO);
    Datatable datatable = getListDataTableBySqlQuery(baseDto.getSqlQuery(), baseDto.getParameters(),
        smsBtsDTO.getPage(), smsBtsDTO.getPageSize(), MrUserCfgApprovedSmsBtsDTO.class,
        smsBtsDTO.getSortName(), smsBtsDTO.getSortType());
    return datatable;
  }

  @Override
  public List<MrUserCfgApprovedSmsBtsDTO> onSearchExport(MrUserCfgApprovedSmsBtsDTO smsBtsDTO) {
    BaseDto baseDto = sqlGetListDataSearchWeb(smsBtsDTO);
    List<MrUserCfgApprovedSmsBtsDTO> listExportBySearch = getNamedParameterJdbcTemplate()
        .query(baseDto.getSqlQuery(), baseDto.getParameters(),
            BeanPropertyRowMapper.newInstance(MrUserCfgApprovedSmsBtsDTO.class));
    return listExportBySearch;
  }

  @Override
  public MrUserCfgApprovedSmsBtsDTO getDetail(Long id) {
    MrUserCfgApprovedSmsBtsEntity smsBtsEntity = getEntityManager()
        .find(MrUserCfgApprovedSmsBtsEntity.class, id);
    MrUserCfgApprovedSmsBtsDTO smsBtsDTO = smsBtsEntity.toDTO();
    return smsBtsDTO;
  }

  @Override
  public MrUserCfgApprovedSmsBtsDTO getApproveLevelByUserLogin(String userNameLogin) {
    Map<String, Object> parameters = new HashMap<>();
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_MR_USER_CFG_APPROVED_SMS_BTS,
            "get-Approve-Level-By-User-Login");
    if (StringUtils.isNotNullOrEmpty(userNameLogin)) {
      sql += " AND x.USERNAME =:userNameLogin ";
      parameters.put("userNameLogin", userNameLogin);
    }
    List<MrUserCfgApprovedSmsBtsDTO> list = getNamedParameterJdbcTemplate()
        .query(sql, parameters,
            BeanPropertyRowMapper.newInstance(MrUserCfgApprovedSmsBtsDTO.class));
    if (!list.isEmpty() && list.size() > 0) {
      return list.get(0);
    }
    return null;
  }

  @Override
  public ResultInSideDto insertOrUpdate(MrUserCfgApprovedSmsBtsDTO smsBtsDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    MrUserCfgApprovedSmsBtsEntity smsBtsEntity = getEntityManager().merge(smsBtsDTO.toEntity());
    resultInSideDto.setId(smsBtsEntity.getUserCfgApprovedSmsId());
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto deleteMrUserCfgApprovedSmsBts(Long id) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    MrUserCfgApprovedSmsBtsEntity smsBtsEntity = getEntityManager()
        .find(MrUserCfgApprovedSmsBtsEntity.class, id);
    getEntityManager().remove(smsBtsEntity);
    return resultInSideDto;
  }

  @Override
  public List<CatLocationDTO> getLstProvinceNamebyCode(String provinceCode) {
    Map<String, Object> parameters = new HashMap<>();
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_MR_USER_CFG_APPROVED_SMS_BTS,
            "get-Province-Name-By-Code");
    parameters.put("provinceCode", provinceCode);
    return getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(CatLocationDTO.class));
  }

  private BaseDto sqlGetListDataSearchWeb(MrUserCfgApprovedSmsBtsDTO smsBtsDTO) {
    BaseDto baseDto = new BaseDto();
    Map<String, Object> parameters = new HashMap<>();
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_MR_USER_CFG_APPROVED_SMS_BTS,
            "get-List-User-Cfg-Approved-Sms-Bts");
    //Cấp phê duyệt
    parameters.put("approveLV1", I18n.getLanguage("mrUserCfgApprovedSmsBts.list.approveLV1.1"));
    parameters.put("approveLV2", I18n.getLanguage("mrUserCfgApprovedSmsBts.list.approveLV2.2"));
    parameters.put("approveLV1andLV2",
        I18n.getLanguage("mrUserCfgApprovedSmsBts.list.approveLV1andLV2.3"));

    //Nhận tin nhắn bảo dưỡng điều hòa
    parameters
        .put("receiveMessage0", I18n.getLanguage("mrUserCfgApprovedSmsBts.list.receiveMessage0.0"));
    parameters
        .put("receiveMessage1", I18n.getLanguage("mrUserCfgApprovedSmsBts.list.receiveMessage1.1"));

    if (smsBtsDTO != null) {
      if (StringUtils.isNotNullOrEmpty(smsBtsDTO.getSearchAll())) {
        sql += " AND (LOWER(t1.USERNAME) LIKE :searchAll ESCAPE '\\' ";
        sql += " OR LOWER(t1.FULLNAME) LIKE :searchAll ESCAPE '\\' ";
        sql += " OR LOWER(t1.MOBILE) LIKE :searchAll ESCAPE '\\' ) ";
        parameters.put("searchAll",
            StringUtils.convertLowerParamContains(smsBtsDTO.getSearchAll()));
      }
      if (StringUtils.isNotNullOrEmpty(smsBtsDTO.getMarketCode())) {
        sql += " AND t1.MARKET_CODE = :marketCode";
        parameters.put("marketCode", smsBtsDTO.getMarketCode());
      }
      if (StringUtils.isNotNullOrEmpty(smsBtsDTO.getAreaCode())) {
        sql += " AND t1.AREA_CODE = :areaCode";
        parameters.put("areaCode", smsBtsDTO.getAreaCode());
      }
      if (StringUtils.isNotNullOrEmpty(smsBtsDTO.getProvinceCode())) {
        sql += " AND t1.PROVINCE_CODE = :provinceCode";
        parameters.put("provinceCode", smsBtsDTO.getProvinceCode());
      } else if (StringUtils.isNotNullOrEmpty(smsBtsDTO.getKeyProvinceNull()) && "1"
          .equals(smsBtsDTO.getKeyProvinceNull())) {
        sql += " AND t1.PROVINCE_CODE is null ";
      }
      if (StringUtils.isNotNullOrEmpty(smsBtsDTO.getUserName())) {
        sql += " AND LOWER(t1.USERNAME) LIKE :userName ESCAPE '\\' ";
        parameters.put("userName", StringUtils.convertLowerParamContains(smsBtsDTO.getUserName()));
      }
      if (StringUtils.isNotNullOrEmpty(smsBtsDTO.getFullName())) {
        sql += " AND LOWER(t1.FULLNAME) LIKE :fullName ESCAPE '\\' ";
        parameters.put("fullName", StringUtils.convertLowerParamContains(smsBtsDTO.getFullName()));
      }
      if (StringUtils.isNotNullOrEmpty(smsBtsDTO.getMobile())) {
        sql += " AND LOWER(t1.MOBILE) LIKE :mobile ESCAPE '\\' ";
        parameters.put("mobile", StringUtils.convertLowerParamContains(smsBtsDTO.getMobile()));
      }
      if (smsBtsDTO.getApproveLevel() != null) {
        sql += " AND t1.APPROVE_LEVEL = :approveLevel";
        parameters.put("approveLevel", smsBtsDTO.getApproveLevel());
      }
      if (smsBtsDTO.getReceiveMessageBD() != null) {
        sql += " AND t1.RECEIVE_MESSAGE_BD = :receiveMessageBD";
        parameters.put("receiveMessageBD", smsBtsDTO.getReceiveMessageBD());
      }
    }
    sql += " ORDER BY t1.CREATE_TIME DESC nulls last ";
    baseDto.setSqlQuery(sql);
    baseDto.setParameters(parameters);
    return baseDto;
  }

  //check trùng
  @Override
  public List<MrUserCfgApprovedSmsBtsDTO> checkExisted(
      MrUserCfgApprovedSmsBtsDTO smsBtsDTO) {
    Map<String, Object> params = new HashMap<>();
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_MR_USER_CFG_APPROVED_SMS_BTS,
            "check-Existed");
    if (smsBtsDTO != null) {
      if (!StringUtils.isStringNullOrEmpty(smsBtsDTO.getUserName())) {
        sql += " AND UPPER(t1.USERNAME) =:userName ";
        params.put("userName", smsBtsDTO.getUserName().toUpperCase());
      }
      if (!StringUtils.isStringNullOrEmpty(smsBtsDTO.getUserCfgApprovedSmsId())) {
        sql += " AND UPPER(t1.USER_CFG_APPROVED_SMS_ID) <> :userCfgApprovedSmsId ";
        params.put("userCfgApprovedSmsId", smsBtsDTO.getUserCfgApprovedSmsId());
      }
    }
    List<MrUserCfgApprovedSmsBtsDTO> mrUserCfgApprovedSmsBtsDTOS = getNamedParameterJdbcTemplate()
        .query(sql, params, BeanPropertyRowMapper.newInstance(MrUserCfgApprovedSmsBtsDTO.class));
    if (mrUserCfgApprovedSmsBtsDTOS != null && !mrUserCfgApprovedSmsBtsDTOS.isEmpty()) {
      return mrUserCfgApprovedSmsBtsDTOS;
    }
    return null;
  }

  @Override
  public List<MrUserCfgApprovedSmsBtsDTO> getLstCountryAreaProvince() {
    Map<String, Object> params = new HashMap<>();
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_MR_USER_CFG_APPROVED_SMS_BTS,
            "get-Lst-Country-Area-Province");
    List<MrUserCfgApprovedSmsBtsDTO> mrUserCfgApprovedSmsBtsDTOS = getNamedParameterJdbcTemplate()
        .query(sql, params, BeanPropertyRowMapper.newInstance(MrUserCfgApprovedSmsBtsDTO.class));
    return mrUserCfgApprovedSmsBtsDTOS;
  }

  @Override
  public List<MrUserCfgApprovedSmsBtsDTO> getListUserInSystem() {
    Map<String, Object> params = new HashMap<>();
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_MR_USER_CFG_APPROVED_SMS_BTS,
            "get-Lst-User-In-System");
    List<MrUserCfgApprovedSmsBtsDTO> lstUserInSystem = getNamedParameterJdbcTemplate()
        .query(sql, params, BeanPropertyRowMapper.newInstance(MrUserCfgApprovedSmsBtsDTO.class));
    return lstUserInSystem;
  }

  @Override
  public List<ItemDataCRInside> getLstCountryMap() {
    Map<String, Object> params = new HashMap<>();
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_MR_USER_CFG_APPROVED_SMS_BTS,
            "get-Lst-Country-Map");
    List<ItemDataCRInside> itemDataCRInsideList = getNamedParameterJdbcTemplate()
        .query(sql, params, BeanPropertyRowMapper.newInstance(ItemDataCRInside.class));
    return itemDataCRInsideList;
  }

}
