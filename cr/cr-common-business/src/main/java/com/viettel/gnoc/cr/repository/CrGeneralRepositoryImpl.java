package com.viettel.gnoc.cr.repository;

import com.viettel.aam.AppGroup;
import com.viettel.aam.IpServiceResult;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.InfraDeviceDTO;
import com.viettel.gnoc.commons.dto.InfraIpDTO;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.dto.UsersDTO;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.commons.model.InfraDeviceEntity;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.repository.InfraIpRepository;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.commons.utils.ConditionBeanUtil;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.CR_SEARCH_TYPE;
import com.viettel.gnoc.commons.utils.Constants.LANGUAGUE_EXCHANGE_SYSTEM;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.CfgChildArrayDTO;
import com.viettel.gnoc.cr.dto.CrCabUsersDTO;
import com.viettel.gnoc.cr.dto.CrCreatedFromOtherSysDTO;
import com.viettel.gnoc.cr.dto.CrDTO;
import com.viettel.gnoc.cr.dto.CrImpactFrameDTO;
import com.viettel.gnoc.cr.dto.CrImpactFrameInsiteDTO;
import com.viettel.gnoc.cr.dto.CrInsiteDTO;
import com.viettel.gnoc.cr.dto.ItemDataCR;
import com.viettel.gnoc.cr.dto.ItemDataCRDTO;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.cr.dto.LogActionBO;
import com.viettel.gnoc.cr.dto.UserCabCrForm;
import com.viettel.gnoc.cr.model.ImpactSegmentEntity;
import com.viettel.gnoc.ws.provider.WSTDTTPort;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class CrGeneralRepositoryImpl extends BaseRepository implements CrGeneralRepository {

  @Value("${application.conf.ipServer: null}")
  private String ipServer1;

  @Autowired
  WSTDTTPort wstdttPort;

  @Autowired
  InfraIpRepository infraIpRepository;

  @Override
  public List<ItemDataCRInside> getListActionCodeById(String id, String locale) {
    List lst = new ArrayList<ItemDataCRInside>();
    try {
      String sql = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_GENERAL, "get-action-code-by-id");
      Map<String, Object> params = new HashMap<>();
      params.put("applied_system", LANGUAGUE_EXCHANGE_SYSTEM.CR_MR);
      params.put("bussiness", 3);
      params.put("p_leeLocale", locale);
      params.put("cr_action_code_id", StringUtils.isNotNullOrEmpty(id) ? id : "");

      lst = getNamedParameterJdbcTemplate()
          .query(sql, params, BeanPropertyRowMapper.newInstance(ItemDataCRInside.class));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return lst;
  }

  @Override
  public List<ItemDataCRInside> getListScopeOfUserForAllRole(CrInsiteDTO crDTO, String locale) {
    ArrayList<ItemDataCRInside> lst = new ArrayList<ItemDataCRInside>();
    try {
      if (crDTO != null) {
        if (crDTO.getUserLogin() == null || "".equals(crDTO.getUserLogin().trim())) {
          return lst;
        }
        if (crDTO.getUserLoginUnit() == null || "".equals(crDTO.getUserLoginUnit().trim())) {
          return lst;
        }
        if (crDTO.getSearchType() == null || "".equals(crDTO.getSearchType().trim())) {
          return lst;
        }
        if (crDTO.getSearchType().equals(Constants.CR_SEARCH_TYPE.WAIT_CAB.toString()) || crDTO
            .getSearchType().equals(Constants.CR_SEARCH_TYPE.QLTD.toString())) {
          Long deptId = Long.parseLong(crDTO.getUserLoginUnit().trim());
          return this.getListScopeOfUserNew(deptId, locale);
        }
        if (crDTO.getSearchType().equals(Constants.CR_SEARCH_TYPE.Z78.toString())) {
          return this.getListScopeOfUserOfCabOrZ78(crDTO, locale);
        }
        if (crDTO.getSearchType().equals(Constants.CR_SEARCH_TYPE.CLOSE.toString())) {
          Long deptId = Long.parseLong(crDTO.getUserLoginUnit().trim());
          return this.getListScopeOfUserNew(deptId, locale);
        }
        if (crDTO.getSearchType().equals(Constants.CR_SEARCH_TYPE.SCHEDULE.toString())) {
          Long deptId = Long.parseLong(crDTO.getUserLoginUnit().trim());
          return this.getListScopeOfUserNew(deptId, locale);
        }
        if (crDTO.getSearchType().equals(Constants.CR_SEARCH_TYPE.VERIFY.toString())) {
          Long deptId = Long.parseLong(crDTO.getUserLoginUnit().trim());
          return this.getListScopeOfUserNew(deptId, locale);
        }
        if (crDTO.getSearchType().equals(CR_SEARCH_TYPE.QLTD_RENEW.toString())) {
          Long deptId = Long.parseLong(crDTO.getUserLoginUnit().trim());
          return this.getListScopeOfUserQLTD(deptId, locale);
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return lst;
  }

  @Override
  public CrInsiteDTO getCRByIDIn30Day(String crId) {
    try {
      String sql = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_GENERAL, "get-cr-by-id-in-thirty-day");
      Map<String, Object> params = new HashMap<>();
      params.put("cr_id", crId);
      List<CrInsiteDTO> list = getNamedParameterJdbcTemplate()
          .query(sql, params, BeanPropertyRowMapper.newInstance(CrInsiteDTO.class));
      if (list != null && !list.isEmpty()) {
        return list.get(0);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }
//
//  public List<ItemDataCRInside> setLanguage(List<ItemDataCRInside> lst, String locale) {
//    List lstResult = new ArrayList<ItemDataCRInside>();
//    try {
//      List lstLanguage = getLanguageExchangeByLocale("2", "3", locale);
//      lstResult = setLanguage(lst,  lstLanguage, "valueStr", "displayStr");
//    } catch (Exception e) {
//      log.error(e.getMessage(), e);
//    }
//    return lstResult;
//  }

  @Override
  public List<ItemDataCRInside> getListSubcategoryCBB(String locale) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_GENERAL, "get-list-sub-cate-CBB");
    Map<String, Object> params = new HashMap<>();
    params.put("applied_system", LANGUAGUE_EXCHANGE_SYSTEM.CR_MR);
    params.put("bussiness", 13);
    params.put("p_leeLocale", locale);
    return getNamedParameterJdbcTemplate()
        .query(sql, params, BeanPropertyRowMapper.newInstance(ItemDataCRInside.class));
  }

  @Override
  public List<ItemDataCRInside> getListImpactSegmentCBB(String locale) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_GENERAL, "get-list-impact-segment-CBB");
    Map<String, Object> params = new HashMap<>();
    params.put("applied_system", LANGUAGUE_EXCHANGE_SYSTEM.CR_MR);
    params.put("bussiness", 10);
    params.put("p_leeLocale", locale);
    return getNamedParameterJdbcTemplate()
        .query(sql, params, BeanPropertyRowMapper.newInstance(ItemDataCRInside.class));
  }

  @Override
  public List<ItemDataCRInside> getListImpactAffectCBB(String locale) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_GENERAL, "get-list-impact-affect-CBB");
    Map<String, Object> params = new HashMap<>();
    params.put("applied_system", LANGUAGUE_EXCHANGE_SYSTEM.CR_MR);
    params.put("bussiness", 1);
    params.put("p_leeLocale", locale);
    return getNamedParameterJdbcTemplate()
        .query(sql, params, BeanPropertyRowMapper.newInstance(ItemDataCRInside.class));
  }

  @Override
  public List<ItemDataCRInside> getListAffectedServiceCBB(Long form, String locale) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_GENERAL, "get-list-affected-service-CBB");
    if (form == null) {
      sql += " where parent_id is null";
    } else {
      sql += " where parent_id is not null";
    }
    sql += " order by displayStr ";
    Map<String, Object> params = new HashMap<>();
    params.put("applied_system", LANGUAGUE_EXCHANGE_SYSTEM.CR_MR);
    params.put("bussiness", 2);
    params.put("p_leeLocale", locale);
    return getNamedParameterJdbcTemplate()
        .query(sql, params, BeanPropertyRowMapper.newInstance(ItemDataCRInside.class));
  }

  @Override
  public List<ItemDataCRInside> getListDutyTypeCBB(CrImpactFrameInsiteDTO form, String locale) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_GENERAL, "get-list-duty-type-CBB");
    Map<String, Object> params = new HashMap<>();
    params.put("lee_locale", locale);
    if (form != null) {
      if (form.getImpactFrameId() != null) {
        sql += " and cife.ife_id = :ifeId";
        params.put("ifeId", form.getImpactFrameId());
      }
      if (!StringUtils.isStringNullOrEmpty(form.getImpactFrameName())) {
        params.put("ife_name", form.getImpactFrameName());
        sql += " and cife.ife_name = :ife_name";
      }
    }
    return getNamedParameterJdbcTemplate()
        .query(sql, params, BeanPropertyRowMapper.newInstance(ItemDataCRInside.class));
  }

  @Override
  public List<ItemDataCRInside> getListDeviceType(CrInsiteDTO crDTO, String locale) {
    List lst = new ArrayList<ItemDataCRInside>();
    try {
      Long impactSegmentId;
      String impactSegmentIdStr = crDTO.getImpactSegment();
      if (StringUtils.isNotNullOrEmpty(impactSegmentIdStr)
          && (impactSegmentId = Long.valueOf(Long.parseLong(impactSegmentIdStr))) != null) {
        String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_CR_GENERAL,
            "get-list-device-type-by-impact-CBB");
        Map<String, Object> params = new HashMap<>();
        params.put("applied_system", LANGUAGUE_EXCHANGE_SYSTEM.CR_MR);
        params.put("bussiness", 8);
        params.put("p_leeLocale", locale);
        params.put("impact_segment_id", impactSegmentId);
        lst = getNamedParameterJdbcTemplate()
            .query(sql, params, BeanPropertyRowMapper.newInstance(ItemDataCRInside.class));
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return lst;
  }

  public List<ItemDataCRInside> getListScopeOfUserNew(Long deptId, String locale) {
    List lst = new ArrayList<ItemDataCRInside>();
    try {
      if (deptId != null) {
        String sql = SQLBuilder
            .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_GENERAL, "get-list-scope-of-user-new");
        Map<String, Object> params = new HashMap<>();
        params.put("manage_unit", deptId);
        params.put("applied_system", LANGUAGUE_EXCHANGE_SYSTEM.CR_MR);
        params.put("bussiness", 6);
        params.put("p_leeLocale", locale);
        lst = getNamedParameterJdbcTemplate()
            .query(sql, params, BeanPropertyRowMapper.newInstance(ItemDataCRInside.class));
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return lst;
  }

  public List<ItemDataCRInside> getListScopeOfUserQLTD(Long deptId, String locale) {
    List<ItemDataCRInside> lst = new ArrayList<>();
    try {
      String sql = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_GENERAL, "get-list-scope-of-user-of-QLTD");
      Map<String, Object> params = new HashMap<>();
      params.put("applied_system", LANGUAGUE_EXCHANGE_SYSTEM.CR_MR);
      params.put("bussiness", 6);
      params.put("p_leeLocale", locale);
      params.put("manage_unit", deptId);
      lst = getNamedParameterJdbcTemplate()
          .query(sql, params, BeanPropertyRowMapper.newInstance(ItemDataCRInside.class));

    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return lst;
  }

  public List<ItemDataCRInside> getListScopeOfUserOfCabOrZ78(CrInsiteDTO crDTO, String locale) {
    List<ItemDataCRInside> lst = new ArrayList<>();
    try {
      Long iscab;
      if (crDTO.getSearchType().equals(Constants.CR_SEARCH_TYPE.CAB.toString())) {
        iscab = Constants.CAB.CAB;
      } else if (crDTO.getSearchType().equals(Constants.CR_SEARCH_TYPE.Z78.toString())) {
        iscab = Constants.CAB.Z78;
      } else {
        return lst;
      }
      String sql = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_GENERAL, "get-list-scope-of-user-ofCabOrz78");
      Map<String, Object> params = new HashMap<>();
      params.put("applied_system", LANGUAGUE_EXCHANGE_SYSTEM.CR_MR);
      params.put("bussiness", 6);
      params.put("p_leeLocale", locale);
      params.put("cmrs_type", iscab);
      params.put("user_id", Long.parseLong(crDTO.getUserLogin()));
      lst = getNamedParameterJdbcTemplate()
          .query(sql, params, BeanPropertyRowMapper.newInstance(ItemDataCRInside.class));

    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return lst;
  }

  @Override
  public List getNetworkNodeFromQLTN(List<AppGroup> lstSelected) {
    List<InfraDeviceDTO> lstData = new ArrayList<>();
    try {
      if (lstSelected != null && !lstSelected.isEmpty()) {
        List<String> lstIp = new ArrayList<>();
        for (AppGroup aGroup : lstSelected) {
          Long aGroupid = aGroup.getAppGroupId();
          IpServiceResult ipRs = wstdttPort.getListIpGroup(aGroupid);
          if (ipRs != null && ipRs.getIpAddress() != null) {
            lstIp.addAll(ipRs.getIpAddress());
          }
        }

        String ipInput = lstIp.toString();
        ipInput = ipInput.substring(1, ipInput.length() - 1);
        List<InfraIpDTO> lstInfraIp = null;
        if (StringUtils.isNotNullOrEmpty(ipInput)) {
          ConditionBean conditionBean = new ConditionBean("ip", ipInput, Constants.NAME_IN,
              Constants.STRING);
          List<ConditionBean> lstConditionBeans = new ArrayList<>();
          lstConditionBeans.add(conditionBean);
          ConditionBeanUtil.sysToOwnListCondition(lstConditionBeans);
          lstInfraIp = infraIpRepository
              .getListInfraIpByCondition(lstConditionBeans, 0, lstIp.size(), "", "");
        }
        if (lstInfraIp != null) {
          Map<String, String> mapDeviceIp = new HashMap<>();
          Map<String, String> mapDeviceIpId = new HashMap<>();
          for (InfraIpDTO infraIp : lstInfraIp) {
            mapDeviceIp.put(infraIp.getDeviceId(), infraIp.getIp());
            mapDeviceIpId.put(infraIp.getDeviceId(), infraIp.getIpId());
          }
          List<String> lstDeviceId = new ArrayList<>();
          for (InfraIpDTO ipDto : lstInfraIp) {
            lstDeviceId.add(ipDto.getDeviceId());
          }
          String deviceInput = lstDeviceId.toString();
          deviceInput = deviceInput.substring(1, deviceInput.length() - 1);
          List<InfraDeviceDTO> lstInfraDevice = null;
          if (StringUtils.isNotNullOrEmpty(deviceInput)) {
            ConditionBean conditionBeanDevice = new ConditionBean("deviceId", deviceInput,
                Constants.NAME_IN, Constants.NUMBER);
            List<ConditionBean> lstConditionBeansDevice = new ArrayList<>();
            lstConditionBeansDevice.add(conditionBeanDevice);
            ConditionBeanUtil.sysToOwnListCondition(lstConditionBeansDevice);
            lstInfraDevice = onSearchByConditionBean(new InfraDeviceEntity(),
                lstConditionBeansDevice, 0, lstDeviceId.size(), "", "");
          }
          if (lstInfraDevice != null) {
            for (InfraDeviceDTO dto : lstInfraDevice) {
              String ip = mapDeviceIp.get(dto.getDeviceId());
              String ipId = mapDeviceIpId.get(dto.getDeviceId());
              if (ip != null && ipId != null) {
                dto.setIp(ip);
                dto.setIpId(ipId);
                lstData.add(dto);
              }
            }
          }
        }
      }

    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    if (lstData != null && !lstData.isEmpty()) {
      for (int i = lstData.size() - 1; i >= 0; i--) {
        InfraDeviceDTO dto = lstData.get(i);
        if (!DataUtil.validateIP(dto.getIp())) {
          lstData.remove(i);
        }
      }
    }
    return lstData;
  }

  @Override
  public List<ItemDataCRInside> getListActionCodeByCode(String code, String locale) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_GENERAL, "get-action-code-by-code");
    Map<String, Object> params = new HashMap<>();
    params.put("applied_system", LANGUAGUE_EXCHANGE_SYSTEM.CR_MR);
    params.put("bussiness", 3);
    params.put("p_leeLocale", locale);
    params.put("action_code",
        StringUtils.isNotNullOrEmpty(code) ? StringUtils.convertLowerParamContains(code) : "");
    return getNamedParameterJdbcTemplate()
        .query(sql, params, BeanPropertyRowMapper.newInstance(ItemDataCRInside.class));
  }

  @Override
  public List<UsersInsideDto> actionGetListUser(String deptId, String userId, String userName,
      String fullName, String staffCode, String deptName, String deptCode, String isAppraise) {
    List lst = new ArrayList<UsersInsideDto>();
    try {
      Map<String, Object> params = new HashMap<>();
      StringBuilder sql = new StringBuilder("");
      sql.append(
          SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_CR_GENERAL, "get-action-get-list-user"));
      if (StringUtils.isNotNullOrEmpty(deptId)) {
        if ("1".equals(isAppraise)) {
          sql.append(" and ut.unit_id in (  ");
          sql.append(" select ut.unit_id from common_gnoc.unit ut ");
          sql.append(" start with ut.unit_id = :deptId ");
          sql.append(" CONNECT BY PRIOR unit_id = parent_unit_id )");
          params.put("deptId", Long.valueOf(deptId.trim()));
        } else {
          sql.append(" and ut.unit_id = :deptId ");
          params.put("deptId", Long.valueOf(deptId.trim()));
        }
      }
      if (StringUtils.isNotNullOrEmpty(userId)) {
        sql.append(" and us.user_id <> :user_id ");
        params.put("user_id", Long.valueOf(userId.trim()));
      }
      if (StringUtils.isNotNullOrEmpty(userName)) {
        sql.append(" and lower(us.username) like :user_name ");
        params.put("user_name", StringUtils.convertLowerParamContains(userName.trim()));
      }
      if (StringUtils.isNotNullOrEmpty(fullName)) {
        sql.append(" and lower(us.fullname) like :fullname ");
        params.put("fullname", StringUtils.convertLowerParamContains(fullName.trim()));
      }
      if (StringUtils.isNotNullOrEmpty(deptCode)) {
        sql.append(" and lower(ut.unit_code) like :deptCode ");
        params.put("deptCode", StringUtils.convertLowerParamContains(deptCode.trim()));
      }
      if (StringUtils.isNotNullOrEmpty(deptName)) {
        sql.append(" and lower(ut.unit_name) like :deptName ");
        params.put("deptName", StringUtils.convertLowerParamContains(deptName.trim()));
      }
      if (StringUtils.isNotNullOrEmpty(staffCode)) {
        sql.append(" and lower(us.staff_Code) like :staff_Code ");
        params.put("staff_Code", StringUtils.convertLowerParamContains(staffCode.trim()));
      }
      sql.append(" order by us.username");
      lst = getNamedParameterJdbcTemplate()
          .query(sql.toString(), params, BeanPropertyRowMapper.newInstance(UsersInsideDto.class));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return lst;
  }

  @Override
  public List<ItemDataCRInside> getListReturnCodeByActionCode(Long actionCode, String locale) {
    List lst = new ArrayList<ItemDataCRInside>();
    try {
      if (actionCode != null) {
        StringBuilder sql = new StringBuilder("");
        sql.append(SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_CR_GENERAL,
            "get-list-return-code-by-action-code"));
        Map<String, Object> params = new HashMap<>();
        params.put("applied_system", LANGUAGUE_EXCHANGE_SYSTEM.CR_MR);
        params.put("bussiness", 12);
        params.put("p_leeLocale", locale);
        params.put("return_code", actionCode);
        lst = getNamedParameterJdbcTemplate()
            .query(sql.toString(), params,
                BeanPropertyRowMapper.newInstance(ItemDataCRInside.class));
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return lst;
  }

  @Override
  public List<UserCabCrForm> getListUserCab(String impactSegmentId, String executeUnitId) {
    List<UserCabCrForm> result = new ArrayList<>();
    try {
      String sql1 = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_GENERAL, "get-list-user-cab");
      String sql2 = "select cr.user_cab userCab,us.username, cr.cr_id numberCr from (select user_id from cr_cab_users where 1=1 ";
      Map<String, Object> params = new HashMap<>();
      if (StringUtils.isNotNullOrEmpty(impactSegmentId)) {
        sql1 = sql1 + " and impact_segment_id= :impactSegmentId ";
        sql2 = sql2 + " and impact_segment_id= :impactSegmentId ";
        params.put("impactSegmentId", Long.valueOf(impactSegmentId));
      }
      if (StringUtils.isNotNullOrEmpty(executeUnitId)) {
        sql1 = sql1 + " and execute_unit_id= :executeUnitId ";
        sql2 = sql2 + " and execute_unit_id= :executeUnitId ";
        params.put("executeUnitId", Long.valueOf(executeUnitId));
      }
      List<UserCabCrForm> lst1 = getNamedParameterJdbcTemplate()
          .query(sql1, params, BeanPropertyRowMapper.newInstance(UserCabCrForm.class));
      Map<String, Long> mapData = new HashMap<>();
      Map<String, UserCabCrForm> mapData2 = new HashMap<>();
      for (UserCabCrForm form : lst1) {
        String key =
            form.getUserCab() + "@" + form.getUsername() + "@" + form.getImpactSegmentId() + "@"
                + form.getExecuteUnitId();
        mapData.put(key, form.getNumberCr());
        mapData2.put(key, form);
      }

      sql2 = sql2
          + " ) tbl\n left join open_pm.cr cr on tbl.user_id=cr.user_cab\n left join common_gnoc.users us on tbl.user_id=us.user_id\n where cr.state in(:waitcab, :cab) and cr.earliest_start_time>sysdate-90";
      params.put("waitcab", Constants.CR_STATE.WAIT_CAB);
      params.put("cab", Constants.CR_STATE.CAB);
      List<UserCabCrForm> lst2 = getNamedParameterJdbcTemplate()
          .query(sql2, params, BeanPropertyRowMapper.newInstance(UserCabCrForm.class));
      Map<String, Long> mapDataCount = new HashMap<>();
      for (UserCabCrForm form2 : lst2) {
        String key = form2.getUserCab() + "@" + form2.getUsername();
        if (form2.getNumberCr() != null) {
          Long number = mapDataCount.get(key);
          if (number == null) {
            number = 1L;
          }
          mapDataCount.put(key, number);
          continue;
        }
        mapDataCount.put(key, 0L);
      }

      for (Map.Entry entry : mapData.entrySet()) {
        UserCabCrForm form3;
        UserCabCrForm temp = new UserCabCrForm();
        String[] keyArr = ((String) entry.getKey()).split("@");
        temp.setUserCab(keyArr[0]);
        temp.setUsername(keyArr[1]);
        temp.setNumberCr(mapDataCount.get(keyArr[0] + "@" + keyArr[1]));
        if (temp.getNumberCr() == null) {
          temp.setNumberCr(Long.valueOf(0));
        }
        if ((form3 = mapData2.get(entry.getKey())) != null) {
          temp.setImpactSegmentId(form3.getImpactSegmentId());
          temp.setExecuteUnitId(form3.getExecuteUnitId());
          temp.setCreateUnitId(form3.getCreateUnitId());
        }
        result.add(temp);
      }
      result.sort(Comparator.comparing(UserCabCrForm::getNumberCr));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return result;
  }

  @Override
  public List<ItemDataCRDTO> getCreatedBySys(String crId) {
    List ret = new ArrayList<ItemDataCRInside>();
    try {
      StringBuilder sql = new StringBuilder("");
      sql.append(
          SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_CR_GENERAL, "get-created-by-sys"));
      Map<String, Object> params = new HashMap<>();
      params.put("cr_id", crId);
      ret = getNamedParameterJdbcTemplate()
          .query(sql.toString(), params, BeanPropertyRowMapper.newInstance(ItemDataCRDTO.class));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return ret;
  }

  @Override
  public List<CfgChildArrayDTO> getCbbChildArray(CfgChildArrayDTO dto) {
    List<CfgChildArrayDTO> lst = new ArrayList<>();
    try {
      String sql = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_GENERAL, "get-cbb-child-array");
      Map<String, Object> params = new HashMap<>();
      params.put("p_leeLocale", I18n.getLocale());
      //namtn edit on November 06
      if (dto != null) {
        if (dto.getParentId() != null) {
          sql += " AND cr.PARENT_ID = :parent_id ";
          params.put("parent_id", dto.getParentId());
        }
        if (dto.getStatus() != null) {
          sql += " AND cr.STATUS = :status ";
          params.put("status", dto.getStatus());
        }
      }

      lst = getNamedParameterJdbcTemplate()
          .query(sql, params, BeanPropertyRowMapper.newInstance(CfgChildArrayDTO.class));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return lst;
  }

  @Override
  public List<UnitDTO> getListUnit(UnitDTO unitDTO) {
    Map<String, Object> params = new HashMap<>();
    try {
      StringBuilder sql = new StringBuilder();
      sql.append(SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_CR_GENERAL, "get-list-unit-dto"));
      if (unitDTO != null) {
        if (!StringUtils.isStringNullOrEmpty(unitDTO.getUnitCode())) {
          sql.append(" and lower(ut.unit_code) like :unitCode ");
          sql.append(" escape '\\'");
          params.put("unitCode", StringUtils.convertLowerParamContains(unitDTO.getUnitCode()));
        }
        if (!StringUtils.isStringNullOrEmpty(unitDTO.getUnitName())) {
          sql.append(" and lower(ut.unit_name) like :unitName ");
          sql.append(" escape '\\'");
          params.put("unitName", StringUtils.convertLowerParamContains(unitDTO.getUnitName()));
        }
      }
      sql.append(" order by ut.unit_code ");
      return getNamedParameterJdbcTemplate()
          .query(sql.toString(), params, BeanPropertyRowMapper.newInstance(UnitDTO.class));

    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return new ArrayList<>();
  }

  @Override
  public ImpactSegmentEntity findImpactSegmentById(Long id) {
    return getEntityManager().find(ImpactSegmentEntity.class, id);
  }

  @Override
  public String insertCrCreatedFromOtherSystem(CrCreatedFromOtherSysDTO crCreatedFromOtherSysDTO) {
    String result = "";
    try {
      StringBuilder sql = new StringBuilder();
      Map<String, Object> params = new HashMap<>();
      sql.append(
          " insert into cr_created_from_other_sys(ccfosm_id,cr_id,system_id,object_id,step_id,is_active,object_code) "
              + "values (cr_created_from_other_sys_seq.nextval,:cr_id, :system_id, :object_id, :step_id, :is_active, :object_code) ");
      params.put("cr_id", Long.valueOf(crCreatedFromOtherSysDTO.getCrId()));
      params.put("system_id", Long.valueOf(crCreatedFromOtherSysDTO.getSystemId()));
      params.put("object_id", Long.valueOf(crCreatedFromOtherSysDTO.getObjectId()));
      params.put("step_id", Long.valueOf(
          crCreatedFromOtherSysDTO.getStepId() != null ? crCreatedFromOtherSysDTO.getStepId()
              : "0"));
      params.put("is_active", Long.valueOf(
          crCreatedFromOtherSysDTO.getIsActive() != null ? crCreatedFromOtherSysDTO.getIsActive()
              : "1"));
      params.put("object_code", crCreatedFromOtherSysDTO.getObjectCode() == null ? ""
          : crCreatedFromOtherSysDTO.getObjectCode());
      getNamedParameterJdbcTemplate().update(sql.toString(), params);
      result = RESULT.SUCCESS;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      result = RESULT.FAIL;
    }

    try {
      if ("2".equals(crCreatedFromOtherSysDTO.getSystemId())) {
        StringBuilder sql2 = new StringBuilder();
        sql2.append(
            " insert into ONE_TM.PROBLEM_CR (PROBLEM_CR_ID, PROBLEM_ID, CR_ID, PT_STATUS_ID) "
                + " values (ONE_TM.PROBLEM_CR_seq.nextval,:object_id, :cr_id, :step_id) ");
        Map<String, Object> param2 = new HashMap<>();
        param2.put("object_id", Long.valueOf(crCreatedFromOtherSysDTO.getObjectId()));
        param2.put("cr_id", Long.valueOf(crCreatedFromOtherSysDTO.getCrId()));
        param2.put("step_id", Long.valueOf(
            crCreatedFromOtherSysDTO.getStepId() != null ? crCreatedFromOtherSysDTO.getStepId()
                : "0"));
        getNamedParameterJdbcTemplate().update(sql2.toString(), param2);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return result;
  }

  // anhlp add
  @Override
  public UsersDTO getUserInfoForMobile(String username) {
    UsersDTO usersDTO = null;
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_GENERAL, "get-user-info-for-mobile");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("username", username);
    List<UsersDTO> list = getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(UsersDTO.class));
    if (list != null && list.size() > 0) {
      usersDTO = list.get(0);
    }
    return usersDTO;
  }

  @Override
  public void insertSession(String userId, String sessionId) {
    StringBuilder sql = new StringBuilder();
    sql.append(
        " insert into common_gnoc.user_mobile_session(session_id,user_id,insert_time) values (:sessionId, :userId,sysdate) ");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("sessionId", sessionId);
    parameters.put("userId", userId);
    getNamedParameterJdbcTemplate().update(sql.toString(), parameters);
  }

  @Override
  public void saveLogAction(String userName, String description) {
    LogActionBO logActionBO = new LogActionBO();
    logActionBO.setAction("doLogin");
    logActionBO.setModule("GNOCMobile");
    logActionBO.setUserName(userName);
    logActionBO.setDescription(description);
    saveLogAction(logActionBO);
  }

  private void saveLogAction(LogActionBO logActionBO) {
    String ipServer = null;
    try {
      ipServer = ipServer1;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    try {
      if (logActionBO != null) {
        logActionBO.setIpAddress(ipServer);
        logActionBO.setUpdateTime(new Date());
        if (logActionBO.getAction() == null) {
          logActionBO.setAction("");
        }
        if (logActionBO.getUserName() == null) {
          logActionBO.setUserName("");
        }
        if (logActionBO.getModule() == null) {
          logActionBO.setModule("");
        }
        if (logActionBO.getResponeTime() == null) {
          logActionBO.setResponeTime(-1L);
        }
        if (logActionBO.getDescription() == null) {
          logActionBO.setDescription("");
        }
        StringBuilder sql = new StringBuilder("");
        sql.append("insert into log_action(lcn_id, ip_address, user_name, "
            + " action, update_time, respone_time, module, description) "
            + " values (log_action_seq.nextval, :ipaddress, :username, :action, :updatetime, :responetime, :module, :description) ");
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("ipaddress", logActionBO.getIpAddress());
        parameters.put("username", logActionBO.getUserName());
        parameters.put("action", logActionBO.getAction());
        parameters.put("updatetime", logActionBO.getUpdateTime());
        parameters.put("responetime", logActionBO.getResponeTime());
        parameters.put("module", logActionBO.getModule());
        parameters.put("description", logActionBO.getDescription());
        getNamedParameterJdbcTemplate().update(sql.toString(), parameters);
      }
    } catch (Exception e) {
      log.debug(e.getMessage(), e);
    }
  }

  @Override
  public void insertSessionV2(String userId, String unitId, String sessionId) {
    StringBuilder sql = new StringBuilder();
    sql.append(
        " insert into common_gnoc.user_mobile_session(session_id,user_id,unit_id,insert_time) values (:sessionId,:userId,:unitId,sysdate) ");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("sessionId", sessionId);
    parameters.put("userId", userId);
    parameters.put("unitId", unitId);
    getNamedParameterJdbcTemplate().update(sql.toString(), parameters);
  }

  @Override
  public List<ItemDataCR> getListScopeOfUserNewForServiceV2(Long deptId, String locale) {
    List lst = new ArrayList<ItemDataCRInside>();
    try {
      if (deptId != null) {
        String sql = SQLBuilder
            .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_GENERAL, "get-list-scope-of-user-new");
        Map<String, Object> params = new HashMap<>();
        params.put("manage_unit", deptId);
        params.put("applied_system", LANGUAGUE_EXCHANGE_SYSTEM.CR_MR);
        params.put("bussiness", 6);
        params.put("p_leeLocale", locale);
        lst = getNamedParameterJdbcTemplate()
            .query(sql, params, BeanPropertyRowMapper.newInstance(ItemDataCR.class));
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return lst;
  }

  @Override
  public List<ItemDataCR> getListScopeOfUserOfCabOrZ78ForServiceV2(CrDTO crDTO, String locale) {
    List<ItemDataCR> lst = new ArrayList<>();
    try {
      Long iscab;
      if (Constants.CR_SEARCH_TYPE.CAB.toString().equals(crDTO.getSearchType())) {
        iscab = Constants.CAB.CAB;
      } else if (Constants.CR_SEARCH_TYPE.Z78.toString().equals(crDTO.getSearchType())) {
        iscab = Constants.CAB.Z78;
      } else {
        return lst;
      }
      String sql = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_GENERAL, "get-list-scope-of-user-ofCabOrz78");
      Map<String, Object> params = new HashMap<>();
      params.put("applied_system", LANGUAGUE_EXCHANGE_SYSTEM.CR_MR);
      params.put("bussiness", 6);
      params.put("p_leeLocale", locale);
      params.put("cmrs_type", iscab);
      params.put("user_id", Long.parseLong(crDTO.getUserLogin()));
      lst = getNamedParameterJdbcTemplate()
          .query(sql, params, BeanPropertyRowMapper.newInstance(ItemDataCR.class));

    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return lst;
  }


  @Override
  public List<ItemDataCR> getListImpactSegmentCBBForServiceV2(String locale) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_GENERAL, "get-list-impact-segment-CBB");
    Map<String, Object> params = new HashMap<>();
    params.put("applied_system", LANGUAGUE_EXCHANGE_SYSTEM.CR_MR);
    params.put("bussiness", 10);
    params.put("p_leeLocale", locale);
    return getNamedParameterJdbcTemplate()
        .query(sql, params, BeanPropertyRowMapper.newInstance(ItemDataCR.class));
  }

  @Override
  public List<ItemDataCR> getListSubcategoryCBBForServiceV2(String locale) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_GENERAL, "get-list-sub-cate-CBB");
    Map<String, Object> params = new HashMap<>();
    params.put("applied_system", LANGUAGUE_EXCHANGE_SYSTEM.CR_MR);
    params.put("bussiness", 13);
    params.put("p_leeLocale", locale);
    return getNamedParameterJdbcTemplate()
        .query(sql, params, BeanPropertyRowMapper.newInstance(ItemDataCR.class));
  }

  @Override
  public List<UsersDTO> actionGetListUserForService(String deptId, String userId, String userName,
      String fullName, String staffCode, String deptName, String deptCode, String isAppraise) {
    List<UsersDTO> lst = new ArrayList<>();
    try {
      Map<String, Object> params = new HashMap<>();
      StringBuilder sql = new StringBuilder("");
      sql.append(
          SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_CR_GENERAL, "get-action-get-list-user"));
      if (StringUtils.isNotNullOrEmpty(deptId)) {
        if ("1".equals(isAppraise)) {
          sql.append(" and ut.unit_id in (  ");
          sql.append(" select ut.unit_id from common_gnoc.unit ut ");
          sql.append(" start with ut.unit_id = :deptId ");
          sql.append(" CONNECT BY PRIOR unit_id = parent_unit_id )");
          params.put("deptId", Long.valueOf(deptId.trim()));
        } else {
          sql.append(" and ut.unit_id = :deptId ");
          params.put("deptId", Long.valueOf(deptId.trim()));
        }
      }
      if (StringUtils.isNotNullOrEmpty(userId)) {
        sql.append(" and us.user_id <> :user_id ");
        params.put("user_id", Long.valueOf(userId.trim()));
      }
      if (StringUtils.isNotNullOrEmpty(userName)) {
        sql.append(" and lower(us.username) like :user_name ");
        params.put("user_name", StringUtils.convertLowerParamContains(userName.trim()));
      }
      if (StringUtils.isNotNullOrEmpty(fullName)) {
        sql.append(" and lower(us.fullname) like :fullname ");
        params.put("fullname", StringUtils.convertLowerParamContains(fullName.trim()));
      }
      if (StringUtils.isNotNullOrEmpty(deptCode)) {
        sql.append(" and lower(ut.unit_code) like :deptCode ");
        params.put("deptCode", StringUtils.convertLowerParamContains(deptCode.trim()));
      }
      if (StringUtils.isNotNullOrEmpty(deptName)) {
        sql.append(" and lower(ut.unit_name) like :deptName ");
        params.put("deptName", StringUtils.convertLowerParamContains(deptName.trim()));
      }
      if (StringUtils.isNotNullOrEmpty(staffCode)) {
        sql.append(" and lower(us.staff_Code) like :staff_Code ");
        params.put("staff_Code", StringUtils.convertLowerParamContains(staffCode.trim()));
      }
      sql.append(" order by us.username");
      lst = getNamedParameterJdbcTemplate()
          .query(sql.toString(), params, BeanPropertyRowMapper.newInstance(UsersDTO.class));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return lst;
  }


  @Override
  public List<ItemDataCR> getListReturnCodeByActionCodeForService(Long actionCode, String locale) {
    List lst = new ArrayList<ItemDataCR>();
    try {
      if (actionCode != null) {
        StringBuilder sql = new StringBuilder("");
        sql.append(SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_CR_GENERAL,
            "get-list-return-code-by-action-code"));
        Map<String, Object> params = new HashMap<>();
        params.put("applied_system", LANGUAGUE_EXCHANGE_SYSTEM.CR_MR);
        params.put("bussiness", 12);
        params.put("p_leeLocale", locale);
        params.put("return_code", actionCode);
        lst = getNamedParameterJdbcTemplate()
            .query(sql.toString(), params, BeanPropertyRowMapper.newInstance(ItemDataCR.class));
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return lst;
  }

  @Override
  public List<ItemDataCR> getListAffectedServiceCBBForService(Object form, String locale) {
    try {
      String sql = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_GENERAL, "get-list-affected-service-CBB");
      if (form == null) {
        sql += " where parent_id is null";
      } else {
        sql += " where parent_id is not null";
      }
      sql += " order by displayStr ";
      Map<String, Object> params = new HashMap<>();
      params.put("applied_system", LANGUAGUE_EXCHANGE_SYSTEM.CR_MR);
      params.put("bussiness", 2);
      params.put("p_leeLocale", locale);
      return getNamedParameterJdbcTemplate()
          .query(sql, params, BeanPropertyRowMapper.newInstance(ItemDataCR.class));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return new ArrayList<>();
  }

  @Override
  public List<ItemDataCR> getListImpactAffectCBBForService(Object form, String locale) {
    try {
      String sql = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_GENERAL, "get-list-impact-affect-CBB");
      Map<String, Object> params = new HashMap<>();
      params.put("applied_system", LANGUAGUE_EXCHANGE_SYSTEM.CR_MR);
      params.put("bussiness", 1);
      params.put("p_leeLocale", locale);
      return getNamedParameterJdbcTemplate()
          .query(sql, params, BeanPropertyRowMapper.newInstance(ItemDataCR.class));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return new ArrayList<>();
  }

  @Override
  public List<ItemDataCR> getListDutyTypeCBB(CrImpactFrameDTO form, String locale) {
    try {
      String sql = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_GENERAL, "get-list-duty-type-CBB");
      Map<String, Object> params = new HashMap<>();
      params.put("lee_locale", locale);
      if (form != null) {
        if (StringUtils.isNotNullOrEmpty(form.getIfeId())) {
          sql += " and cife.ife_id = :ifeId";
          params.put("ifeId", form.getIfeId());
        }
        if (!StringUtils.isStringNullOrEmpty(form.getIfeName())) {
          params.put("ife_name", form.getIfeName());
          sql += " and cife.ife_name = :ife_name";
        }
      }
      return getNamedParameterJdbcTemplate()
          .query(sql, params, BeanPropertyRowMapper.newInstance(ItemDataCR.class));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return new ArrayList<>();
  }

  @Override
  public List<ItemDataCR> getListDeviceTypeCBB(Object form, String locale) {
    try {
      String sql = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_GENERAL, "get-list-device-type-CBB");
      Map<String, Object> params = new HashMap<>();
      params.put("p_leeLocale", locale);
      params.put("applied_system", LANGUAGUE_EXCHANGE_SYSTEM.CR_MR);
      params.put("bussiness", 8);
      return getNamedParameterJdbcTemplate()
          .query(sql, params, BeanPropertyRowMapper.newInstance(ItemDataCR.class));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return new ArrayList<>();
  }

  @Override
  public List<ItemDataCR> getListDeviceType(CrDTO crDTO, String locale) {
    try {
      Long impactSegmentId;
      String impactSegmentIdStr = crDTO.getImpactSegment();
      if (StringUtils.isNotNullOrEmpty(impactSegmentIdStr)
          && (impactSegmentId = Long.valueOf(Long.parseLong(impactSegmentIdStr))) != null) {
        String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_CR_GENERAL,
            "get-list-device-type-by-impact-CBB");
        Map<String, Object> params = new HashMap<>();
        params.put("applied_system", LANGUAGUE_EXCHANGE_SYSTEM.CR_MR);
        params.put("bussiness", 8);
        params.put("p_leeLocale", locale);
        params.put("impact_segment_id", impactSegmentId);
        return getNamedParameterJdbcTemplate()
            .query(sql, params, BeanPropertyRowMapper.newInstance(ItemDataCR.class));
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return new ArrayList<>();
  }

  @Override
  public List<ItemDataCR> getListActionCodeByCodeForService(String code, String locale) {
    try {
      String sql = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_GENERAL, "get-action-code-by-code");
      Map<String, Object> params = new HashMap<>();
      params.put("applied_system", LANGUAGUE_EXCHANGE_SYSTEM.CR_MR);
      params.put("bussiness", 3);
      params.put("p_leeLocale", locale);
      params.put("action_code",
          StringUtils.isNotNullOrEmpty(code) ? StringUtils.convertLowerParamContains(code) : "");
      return getNamedParameterJdbcTemplate()
          .query(sql, params, BeanPropertyRowMapper.newInstance(ItemDataCR.class));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return new ArrayList<>();
  }

  @Override
  public List<CrCabUsersDTO> getListUserCab(String impactSegmentId) {
    Map<String, Object> params = new HashMap<>();
    String sql = " SELECT DISTINCT USER_ID userID FROM OPEN_PM.CR_CAB_USERS where USER_ID is not null ";
    if (StringUtils.isNotNullOrEmpty(impactSegmentId)) {
      sql += " AND IMPACT_SEGMENT_ID = :impactSegmentId ";
      params.put("impactSegmentId", impactSegmentId);
    }
    return getNamedParameterJdbcTemplate()
        .query(sql, params, BeanPropertyRowMapper.newInstance(CrCabUsersDTO.class));
  }

  @Override
  public List<ItemDataCR> getCreatedBySysMobile(String crId) {
    List ret = new ArrayList<ItemDataCRInside>();
    try {
      StringBuilder sql = new StringBuilder("");
      sql.append(
          SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_CR_GENERAL, "get-created-by-sys"));
      Map<String, Object> params = new HashMap<>();
      params.put("cr_id", crId);
      ret = getNamedParameterJdbcTemplate()
          .query(sql.toString(), params, BeanPropertyRowMapper.newInstance(ItemDataCR.class));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return ret;
  }
}
