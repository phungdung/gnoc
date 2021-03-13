package com.viettel.gnoc.wo.repository;

import com.viettel.cc.webserivce.CompCause;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.config.TimezoneContextHolder;
import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.commons.dto.InfraStationsDTO;
import com.viettel.gnoc.commons.dto.LanguageExchangeDTO;
import com.viettel.gnoc.commons.dto.MessagesDTO;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.dto.UsersDTO;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.commons.dto.WoSearchWebDTO;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.repository.CommonRepository;
import com.viettel.gnoc.commons.repository.UnitRepository;
import com.viettel.gnoc.commons.repository.UserRepository;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.APPLIED_BUSSINESS;
import com.viettel.gnoc.commons.utils.Constants.LANGUAGUE_EXCHANGE_SYSTEM;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.Constants.WO_MASTER_CODE;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.DateUtil;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.wfm.dto.ConfigPropertyDTO;
import com.viettel.gnoc.wo.dto.AutoCreateWoOsDTO;
import com.viettel.gnoc.wo.dto.CatLocationDTO;
import com.viettel.gnoc.wo.dto.CfgMapUnitGnocNimsDTO;
import com.viettel.gnoc.wo.dto.CountWoForVSmartForm;
import com.viettel.gnoc.wo.dto.FieldForm;
import com.viettel.gnoc.wo.dto.KpiCompleteVsamrtForm;
import com.viettel.gnoc.wo.dto.MaterialThresDTO;
import com.viettel.gnoc.wo.dto.SearchWoKpiCDBRForm;
import com.viettel.gnoc.wo.dto.TotalWoForm;
import com.viettel.gnoc.wo.dto.WoCdDTO;
import com.viettel.gnoc.wo.dto.WoCdGroupDTO;
import com.viettel.gnoc.wo.dto.WoCdGroupInsideDTO;
import com.viettel.gnoc.wo.dto.WoChecklistDTO;
import com.viettel.gnoc.wo.dto.WoConfigPropertyDTO;
import com.viettel.gnoc.wo.dto.WoDTO;
import com.viettel.gnoc.wo.dto.WoDTOSearch;
import com.viettel.gnoc.wo.dto.WoHisForAccountDTO;
import com.viettel.gnoc.wo.dto.WoHistoryInsideDTO;
import com.viettel.gnoc.wo.dto.WoInsideDTO;
import com.viettel.gnoc.wo.dto.WoMaterialDeducteInsideDTO;
import com.viettel.gnoc.wo.dto.WoPostInspectionDTO;
import com.viettel.gnoc.wo.dto.WoPriorityDTO;
import com.viettel.gnoc.wo.dto.WoSalaryResponse;
import com.viettel.gnoc.wo.dto.WoTypeServiceInsideDTO;
import com.viettel.gnoc.wo.model.AutoCreateWoOsEntity;
import com.viettel.gnoc.wo.model.WoEntity;
import com.viettel.gnoc.wo.utils.Gnoc1_WoPort;
import com.viettel.security.PassTranformer;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import javax.persistence.LockModeType;
import javax.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.PropertyUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;


@Repository
@Slf4j
public class WoRepositoryImpl extends BaseRepository implements WoRepository {

  @Value("${application.vsmart_download_url}")
  private String vsmartDownloadUrl;

  @Value("${application.ftp.server}")
  private String server;

  @Value("${application.ftp.port}")
  private int port;

  @Value("${application.ftp.user}")
  private String user;

  @Value("${application.ftp.pass}")
  private String pass;

  @Value("${application.ftp.folder}")
  private String ftpUpload;

  @Autowired
  UnitRepository unitRepository;

  @Autowired
  CommonRepository commonRepository;

  @Autowired
  WoHistoryRepository woHistoryRepository;

  @Autowired
  UserRepository userRepository;

  @Autowired
  TicketProvider ticketProvider;

  @Autowired
  Gnoc1_WoPort gnoc1_woPort;

  @Override
  public Datatable getListDataSearchWeb(WoInsideDTO woInsideDTO) {
    Datatable datatable = new Datatable();
    BaseDto baseDto = sqlGetListDataSearchWeb(woInsideDTO);
    BaseDto totalBase = sqlGetTotalDataSearchWeb(woInsideDTO);
//    boolean isPageParam = false;
//    if (woInsideDTO.isIsCd() != null && woInsideDTO.isIsCd()) {
//      isPageParam = true;
//    }
    String sql2 = "select count(*) totalRow from ( " + baseDto.getSqlQuery() + ")";
    int maxResult = woInsideDTO.getPageSize() == null ? 10 : woInsideDTO.getPageSize();

    List<Long> totals = getNamedParameterJdbcTemplateNormal()
        .queryForList(sql2, totalBase.getParameters(), Long.class);
    int total = 0;
    if (totals != null && totals.size() > 0) {
      total = totals.get(0).intValue();
    }
    int pages = (int) Math.ceil(total * 1.0 / maxResult);

//    List<WoInsideDTO> lstData = query.getResultList();
    List<WoInsideDTO> lstData = getListBySqlQuery2(baseDto.getSqlQuery(), baseDto.getParameters(),
        woInsideDTO.getPage(), woInsideDTO.getPageSize(), WoInsideDTO.class, false);
    if (lstData != null && lstData.size() > 0) {
      lstData.get(0).setPage(woInsideDTO.getPage());
      lstData.get(0).setPageSize(woInsideDTO.getPage());
      lstData.get(0).setTotalRow(total);
    }
    datatable.setData(lstData);
    datatable.setTotal(total);
    datatable.setPages(pages);
    return datatable;
  }

  @Override
  public List<WoInsideDTO> getListDataSearchWoDTO(WoInsideDTO woInsideDTO) {
    BaseDto baseDto = sqlGetListDataSearchWeb(woInsideDTO);
    return getNamedParameterJdbcTemplate().query(baseDto.getSqlQuery(), baseDto.getParameters(),
        BeanPropertyRowMapper.newInstance(WoInsideDTO.class));
  }

  @Override
  public List<WoInsideDTO> getListDataSearch1(WoInsideDTO searchDto) {
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_WO, "get-List-Data-Search-1");
    Map<String, Object> parameters = new HashMap<>();
    Double offset = userRepository.getOffsetFromUser(
        commonRepository.getUserByUserId(Long.valueOf(searchDto.getUserId())).getUsername());
    parameters.put("offset", offset);
    parameters.put("unassigned", I18n.getLanguage("wo.status.UNASSIGNED"));
    parameters.put("assigned", I18n.getLanguage("wo.status.ASSIGNED"));
    parameters.put("reject", I18n.getLanguage("wo.status.REJECT"));
    parameters.put("dispatch", I18n.getLanguage("wo.status.DISPATCH"));
    parameters.put("accept", I18n.getLanguage("wo.status.ACCEPT"));
    parameters.put("inProcess", I18n.getLanguage("wo.status.INPROCESS"));
    parameters.put("pending", I18n.getLanguage("wo.status.PENDING"));
    parameters.put("closedCd", I18n.getLanguage("wo.status.CLOSED_CD"));
    parameters.put("closedFt", I18n.getLanguage("wo.status.CLOSED_FT"));
    parameters.put("draft", I18n.getLanguage("wo.status.DRAFT"));
    // Tim theo he thong
    if (StringUtils.isNotNullOrEmpty(searchDto.getWoSystem())) {
      sql += " AND w.wo_system = :woSystem";
      parameters.put("woSystem", searchDto.getWoSystem());
    }
    // Tim theo ma he thong
    if (StringUtils.isNotNullOrEmpty(searchDto.getWoSystemId())) {
      sql += " AND LOWER(w.wo_system_id) = :woSystemId";
      parameters.put("woSystemId", searchDto.getWoSystemId().toLowerCase());
    }
    // Ma cong viec
    if (StringUtils.isNotNullOrEmpty(searchDto.getWoCode())) {
      sql += " AND LOWER(w.wo_code) LIKE :woCode ESCAPE '\\'";
      parameters.put("woCode", StringUtils.convertLowerParamContains(searchDto.getWoCode()));
    }
    if (searchDto.getCdId() != null) {
      sql += " AND w.cd_id = :cdId";
      parameters.put("cdId", searchDto.getCdId());
    }
    // Muc do uu tien
    if (searchDto.getPriorityId() != null) {
      sql += " AND w.priority_id = :priorityId";
      parameters.put("priorityId", searchDto.getPriorityId());
    }
    // Loai cong viec
    if (searchDto.getWoTypeId() != null) {
      sql += " AND w.wo_type_id = :woTypeId";
      parameters.put("woTypeId", searchDto.getWoTypeId());
    }
    // Noi dung cong viec
    if (StringUtils.isNotNullOrEmpty(searchDto.getWoContent())) {
      sql += " AND LOWER(w.wo_content) LIKE :woContent ESCAPE '\\'";
      parameters.put("woContent", StringUtils.convertLowerParamContains(searchDto.getWoContent()));
    }
    // Trang thai hoan thanh
    if (searchDto.getResult() != null) {
      if ("3".equals(String.valueOf(searchDto.getResult()))) {
        sql += " AND w.result is null";
      } else {
        sql += " AND w.result = :result";
        parameters.put("result", searchDto.getResult());
      }
    }
    // Thoi gian khoi tao
    if (searchDto.getStartTimeFrom() != null) {
      sql += " AND w.create_date >= TO_TIMESTAMP(:startTimeFrom,'dd/mm/yyyy hh24:mi:ss')";
      parameters.put("startTimeFrom", DateUtil.date2ddMMyyyyHHMMss(searchDto.getStartTimeFrom()));
    }
    if (searchDto.getStartTimeTo() != null) {
      sql += " AND w.create_date <= TO_TIMESTAMP(:startTimeTo,'dd/mm/yyyy hh24:mi:ss')";
      parameters.put("startTimeTo", DateUtil.date2ddMMyyyyHHMMss(searchDto.getStartTimeTo()));
    }
    // Thoi gian thuc hien
    if (searchDto.getEndTimeFrom() != null) {
      sql += " AND w.start_time >= TO_TIMESTAMP(:endTimeFrom,'dd/mm/yyyy hh24:mi:ss')";
      parameters.put("endTimeFrom", DateUtil.date2ddMMyyyyHHMMss(searchDto.getStartDateFrom()));
    }
    if (searchDto.getEndTimeTo() != null) {
      sql += " AND w.start_time <= TO_TIMESTAMP(:endTimeTo,'dd/mm/yyyy hh24:mi:ss')";
      parameters.put("endTimeTo", DateUtil.date2ddMMyyyyHHMMss(searchDto.getStartDateTo()));
    }
    if (searchDto.getStatus() != null) {
      sql += " AND w.status = :status";
      parameters.put("status", searchDto.getStatus());
    }
    if (searchDto.getWoId() != null) {
      sql += " AND w.wo_id = :woId";
      parameters.put("woId", searchDto.getWoId());
    }
    if (searchDto.getParentId() != null) {
      sql += " AND w.parent_id = :parentId";
      parameters.put("parentId", searchDto.getParentId());
    }
    // Nhan vien tao
    if (StringUtils.isNotNullOrEmpty(searchDto.getCreatePersonName())) {
      sql += " AND LOWER(cp.username) LIKE :createPersonName ESCAPE '\\'";
      parameters.put("createPersonName",
          StringUtils.convertLowerParamContains(searchDto.getCreatePersonName()));
    }
    // Nhan vien thuc hien
    if (StringUtils.isNotNullOrEmpty(searchDto.getFtName())) {
      sql += " AND LOWER(f.username) LIKE :ftName ESCAPE '\\'";
      parameters.put("ftName", StringUtils.convertLowerParamContains(searchDto.getFtName()));
    }
    // Cong viec cha
    if (StringUtils.isNotNullOrEmpty(searchDto.getParentName())) {
      sql += " AND LOWER(p.wo_code) LIKE :parentName ESCAPE '\\'";
      parameters
          .put("parentName", StringUtils.convertLowerParamContains(searchDto.getParentName()));
    }
    // Thue bao
    if (StringUtils.isNotNullOrEmpty(searchDto.getAccountIsdn())) {
      sql += " AND LOWER(wd.account_isdn) LIKE :accountIsdn ESCAPE '\\'";
      parameters
          .put("accountIsdn", StringUtils.convertLowerParamContains(searchDto.getAccountIsdn()));
    }
    // Tim theo don vi
    if (searchDto.getIsContainChildUnit() != null && searchDto.getIsContainChildUnit()) {
      if (searchDto.getCreateUnitId() != null) {
        sql += " AND w.create_person_id in ("
            + "select user_id from common_gnoc.users where unit_id in"
            + " (select unit_id from common_gnoc.unit where level < 50 and status = 1"
            + " start with unit_id = :createUnitId"
            + " connect by prior unit_id = parent_unit_id))";
        parameters.put("createUnitId", searchDto.getCreateUnitId());
      }
      if (searchDto.getProcessUnitId() != null) {
        sql += " AND (w.ft_id in ("
            + "select user_id from common_gnoc.users where unit_id in"
            + " (select unit_id from common_gnoc.unit where level < 50 and status = 1"
            + " start with unit_id = :processUnitId"
            + " connect by prior unit_id = parent_unit_id)))";
        parameters.put("processUnitId", searchDto.getProcessUnitId());
      }
    } else {
      if (searchDto.getCreateUnitId() != null) {
        sql += " AND w.create_person_id in "
            + "(select user_id from common_gnoc.users where unit_id = :createUnitId)";
        parameters.put("createUnitId", searchDto.getCreateUnitId());
      }
      if (searchDto.getProcessUnitId() != null) {
        sql += " AND (w.ft_id in "
            + "(select user_id from common_gnoc.users where unit_id = :processUnitId))";
        parameters.put("processUnitId", searchDto.getProcessUnitId());
      }
    }
    if (searchDto.getUserId() != null) {
      sql += " and (";
      Boolean isHasCondition = false;
      if (searchDto.isIsCreated() != null && searchDto.isIsCreated()) {
        sql += isHasCondition ? " or w.create_person_id = :userIdCreated"
            : " w.create_person_id = :userIdCreated";
        isHasCondition = true;
        parameters.put("userIdCreated", searchDto.getUserId());
      }
      if (searchDto.isIsCd() != null && searchDto.isIsCd()) {
        sql += isHasCondition
            ? " or w.cd_id in (select wo_group_id from wo_cd where user_id = :userIdCd)"
            : "  w.cd_id in (select wo_group_id from wo_cd where user_id = :userIdCd)";
        isHasCondition = true;
        parameters.put("userIdCd", searchDto.getUserId());
      }
      if (searchDto.isIsFt() != null && searchDto.isIsFt()) {
        sql += isHasCondition ? " or w.ft_id = :userIdFt" : "  w.ft_id = :userIdFt";
        isHasCondition = true;
        parameters.put("userIdFt", searchDto.getUserId());
      }
      if (!isHasCondition) {
        sql += "1 = 1";
      }
      sql += ")";
    }
    sql += " ORDER BY w.create_date DESC ";
    return getNamedParameterJdbcTemplate().query(sql, parameters,
        BeanPropertyRowMapper.newInstance(WoInsideDTO.class));
  }

  @Override
  public WoInsideDTO findWoById(Long woId, Double offSetFromUser) {
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_WO, "find-Wo-By-Id");
    Map<String, Object> parameters = new HashMap<>();
    String leeLocale = I18n.getLocale();
    parameters.put("leeLocale", leeLocale);
    parameters.put("offset", offSetFromUser);
    parameters.put("unassigned", I18n.getLanguage("wo.status.UNASSIGNED"));
    parameters.put("assigned", I18n.getLanguage("wo.status.ASSIGNED"));
    parameters.put("reject", I18n.getLanguage("wo.status.REJECT"));
    parameters.put("rejectCd", I18n.getLanguage("wo.status.REJECT_CD"));
    parameters.put("dispatch", I18n.getLanguage("wo.status.DISPATCH"));
    parameters.put("accept", I18n.getLanguage("wo.status.ACCEPT"));
    parameters.put("inProcess", I18n.getLanguage("wo.status.INPROCESS"));
    parameters.put("pending", I18n.getLanguage("wo.status.PENDING"));
    parameters.put("closedCd", I18n.getLanguage("wo.status.CLOSED_CD"));
    parameters.put("closedFt", I18n.getLanguage("wo.status.CLOSED_FT"));
    parameters.put("draft", I18n.getLanguage("wo.status.DRAFT"));

    sql += " AND w.wo_id = :woId";
    parameters.put("woId", woId);

    List<WoInsideDTO> list = getNamedParameterJdbcTemplate().query(sql, parameters,
        BeanPropertyRowMapper.newInstance(WoInsideDTO.class));
    if (list != null && !list.isEmpty()) {
      WoInsideDTO dto = list.get(0);
      return dto;
    }
    return null;
  }

  @Override
  public WoInsideDTO findWoByIdNoWait(Long woId) throws Exception {
    try {
      Map<String, Object> properties = new HashMap<>();
      properties.put("javax.persistence.lock.timeout", "3000");
      WoEntity woEntity = getEntityManager()
          .find(WoEntity.class, woId, LockModeType.PESSIMISTIC_WRITE, properties);
      if (woEntity != null) {
        return woEntity.toDTO();
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw new Exception(I18n.getLanguage("wo.woIsBusy"));
    }
    return null;
  }

  @Override
  public WoInsideDTO findWoByWoCodeNoWait(String woCode) throws Exception {
    try {
      String sql = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_WO, "get-Wo-By-Wo-Code-No-Wait");
      Query query = getEntityManager().createQuery(sql).setLockMode(LockModeType.PESSIMISTIC_WRITE)
          .setHint("javax.persistence.lock.timeout", "3000");
      query.setParameter("woCode", woCode.toLowerCase());
      List<WoEntity> list = query.getResultList();
      if (list != null && list.size() > 0) {
        return list.get(0).toDTO();
      }
    } catch (Exception e) {
      log.info(e.getMessage(), e);
      throw new Exception(I18n.getLanguage("wo.woIsBusy"));
    }
    return null;
  }

  @Override
  public WoInsideDTO findWoByIdNoOffset(Long woId) {
    WoEntity woEntity = getEntityManager().find(WoEntity.class, woId);
    if (woEntity != null) {
      return woEntity.toDTO();
    }
    return null;
  }

  @Override
  public ResultInSideDto insertWo(WoInsideDTO woInsideDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    WoEntity woEntity = getEntityManager().merge(woInsideDTO.toEntity());
    resultInSideDto.setId(woEntity.getWoId());
    resultInSideDto.setKey(RESULT.SUCCESS);
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto updateWo(WoInsideDTO woInsideDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    getEntityManager().merge(woInsideDTO.toEntity());
    resultInSideDto.setId(woInsideDTO.getWoId());
    resultInSideDto.setKey(RESULT.SUCCESS);
    return resultInSideDto;
  }

  @Override
  public String getSeqTableWo(String seq) {
    return getSeqTableBase(seq);
  }

  @Override
  public List<String> getListSequenseWo(String seq, int size) {
    return getListSequense(seq, size);
  }

  @Override
  public ResultInSideDto deleteWo(Long woId) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    WoEntity entity = getEntityManager().find(WoEntity.class, woId);
    getEntityManager().remove(entity);
    resultInSideDto.setKey(RESULT.SUCCESS);
    return resultInSideDto;
  }

  @Override
  public List<CatItemDTO> getListWoSystemInsertWeb() {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_WO, "get-List-Wo-System-Insert-Web");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("categoryCode", WO_MASTER_CODE.WO_SYSTEM_ARRAY);
    parameters.put("itemCode", StringUtils.convertLowerParamContains(WO_MASTER_CODE.WO_SYSTEM_WFM));
    List<CatItemDTO> list = getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(CatItemDTO.class));
    List<LanguageExchangeDTO> lstLanguage = getLanguageExchange(LANGUAGUE_EXCHANGE_SYSTEM.COMMON,
        APPLIED_BUSSINESS.CAT_ITEM.toString());
    try {
      list = setLanguage(list, lstLanguage, "itemId", "itemName");
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return list;
  }

  @Override
  public WoInsideDTO getListFileFromWo(Long woId) {
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_WO, "get-List-File-From-Wo");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("woId", woId);
    List<WoInsideDTO> list = getNamedParameterJdbcTemplate().query(sql, parameters,
        BeanPropertyRowMapper.newInstance(WoInsideDTO.class));
    if (list != null && !list.isEmpty()) {
      WoInsideDTO dto = list.get(0);
      return dto;
    }
    return null;
  }

  @Override
  public CatLocationDTO getCatLocationById(Long locationId) {
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_WO, "get-Cat-Location-By-Id");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("locationId", locationId);
    List<CatLocationDTO> list = getNamedParameterJdbcTemplate().query(sql, parameters,
        BeanPropertyRowMapper.newInstance(CatLocationDTO.class));
    if (list != null && !list.isEmpty()) {
      CatLocationDTO dto = list.get(0);
      return dto;
    }
    return null;
  }

  @Override
  public List<CatItemDTO> getListWoKttsAction(String key) {
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_WO, "get-List-Wo-Ktts-Action");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("p_leeLocale", I18n.getLocale());
    if (StringUtils.isNotNullOrEmpty(key)) {
      switch (key.toLowerCase().trim()) {
        case "action":
          sql += " and ld.parentItemId IS NOT NULL";
          break;
        case "action_source":
          sql += " and ld.parentItemId IS NULL";
          break;
      }
    }
    sql += " order by ld.itemName ";
    return getNamedParameterJdbcTemplate().query(sql, parameters,
        BeanPropertyRowMapper.newInstance(CatItemDTO.class));
  }

  @Override
  public List<WoInsideDTO> getListWoExport(WoInsideDTO woInsideDTO) {
    BaseDto baseDto = sqlGetListDataSearchWeb(woInsideDTO);
    return getNamedParameterJdbcTemplate().query(baseDto.getSqlQuery(), baseDto.getParameters(),
        BeanPropertyRowMapper.newInstance(WoInsideDTO.class));
  }

  @Override
  public List<WoInsideDTO> getListChildAccept(Long parentId) {
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_WO, "get-List-Child-Accept");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("parentId", parentId);
    return getNamedParameterJdbcTemplate().query(sql, parameters,
        BeanPropertyRowMapper.newInstance(WoInsideDTO.class));
  }

  @Override
  public List<WoInsideDTO> getListWoBySystemOtherCode(String code, String system) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_WO, "get-List-Wo-By-System-Other-Code");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("code", code.toLowerCase());
    return getNamedParameterJdbcTemplate().query(sql, parameters,
        BeanPropertyRowMapper.newInstance(WoInsideDTO.class));
  }

  @Override
  public CompCause getCompCause(Long compCauseId) {
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_COMP_CAUSE, "get-Comp-Cause");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("compCauseId", compCauseId);
    List<CompCause> list = getNamedParameterJdbcTemplate().query(sql, parameters,
        BeanPropertyRowMapper.newInstance(CompCause.class));
    if (list != null && !list.isEmpty()) {
      CompCause compCause = list.get(0);
      return compCause;
    }
    return null;
  }

  @Override
  public WoInsideDTO getWoByWoCode(String woCode) {
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_WO, "get-Wo-By-Wo-Code");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("woCode", woCode.toLowerCase());
    List<WoInsideDTO> list = getNamedParameterJdbcTemplate().query(sql, parameters,
        BeanPropertyRowMapper.newInstance(WoInsideDTO.class));
    if (list != null && list.size() > 0) {
      return list.get(0);
    }
    return null;
  }

  @Override
  public List<UsersInsideDto> getUserOfCD(Long cdId) {
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_WO, "get-User-Of-CD");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("cdId", cdId);
    return getNamedParameterJdbcTemplate().query(sql, parameters,
        BeanPropertyRowMapper.newInstance(UsersInsideDto.class));
  }

  @Override
  public List<MessagesDTO> getMessagesForFT(WoInsideDTO woInsideDTO, String content, Date date) {
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_WO, "get-Messages-For-FT");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("woId", woInsideDTO.getWoId());
    return getNamedParameterJdbcTemplate().query(sql, parameters,
        BeanPropertyRowMapper.newInstance(MessagesDTO.class));
  }

  @Override
  public List<MessagesDTO> getMessagesForCd(WoInsideDTO woInsideDTO, String content, Date date) {
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_WO, "get-Messages-For-Cd");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("woId", woInsideDTO.getWoId());
    return getNamedParameterJdbcTemplate().query(sql, parameters,
        BeanPropertyRowMapper.newInstance(MessagesDTO.class));
  }

  @Override
  public Object updateObjectData(Object objSrc, Object objDes) {
    Field[] k = objSrc.getClass().getDeclaredFields();
    for (int i = 0; i < k.length; i++) {
      try {
        PropertyUtils.setSimpleProperty(objDes, k[i].getName(),
            PropertyUtils.getSimpleProperty(objSrc, k[i].getName()));
      } catch (Exception e) {
        log.info(e.getMessage(), e);
      }
    }
    return objDes;
  }

  @Override
  public UnitDTO getUnitCodeMapNims(String unitNimsCode, String businessName) {
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_WO, "get-Unit-Code-Map-Nims");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("unitNimsCode", unitNimsCode.toLowerCase());
    if (StringUtils.isNotNullOrEmpty(businessName)) {
      sql += " AND LOWER(c.BUSINESS_NAME) = :businessName";
      parameters.put("businessName", businessName.toLowerCase());
    }
    List<CfgMapUnitGnocNimsDTO> listCfg = getNamedParameterJdbcTemplate().query(sql, parameters,
        BeanPropertyRowMapper.newInstance(CfgMapUnitGnocNimsDTO.class));
    if (listCfg != null && listCfg.size() > 0) {
      CfgMapUnitGnocNimsDTO cfgDto = listCfg.get(0);
      if (StringUtils.isNotNullOrEmpty(cfgDto.getUnitGnocCode())) {
        return unitRepository.getUnitDTOByUnitCode(cfgDto.getUnitGnocCode());
      } else {
        return null;
      }
    }
    return null;
  }

  @Override
  public Long getCdByUnitId(Long unitId, Long type) {
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_WO, "get-Cd-By-Unit-Id");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("unitId", unitId);
    parameters.put("type", type);
    List<WoCdGroupInsideDTO> listCd = getNamedParameterJdbcTemplate().query(sql, parameters,
        BeanPropertyRowMapper.newInstance(WoCdGroupInsideDTO.class));
    if (listCd != null && listCd.size() > 0) {
      return listCd.get(0).getWoGroupId();
    }
    return null;
  }

  @Override
  public Long getCdByUnitId(Long unitId, Long type, Long woTypeId) {
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_WO, "get-Cd-By-Unit-Id");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("unitId", unitId);
    parameters.put("type", type);
    if (woTypeId != null) {
      sql += " and b.wo_group_id in (select distinct t.wo_group_id from wfm.wo_type_group t"
          + " where t.wo_type_id = :woTypeId)";
      parameters.put("woTypeId", woTypeId);
    }
    List<WoCdGroupInsideDTO> listCd = getNamedParameterJdbcTemplate().query(sql, parameters,
        BeanPropertyRowMapper.newInstance(WoCdGroupInsideDTO.class));
    if (listCd != null && listCd.size() > 0) {
      return listCd.get(0).getWoGroupId();
    }
    return null;
  }

  @Override
  public WoCdGroupInsideDTO getCdByFT(Long ftId, Long woTypeId, String cdGroupType) {
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_WO, "get-Cd-By-FT");
    Map<String, Object> parameters = new HashMap<>();
    if (StringUtils.isNotNullOrEmpty(cdGroupType)) {
      sql += " AND g.GROUP_TYPE_ID = :groupTypeId";
      parameters.put("groupTypeId", Long.valueOf(cdGroupType));
    }
    if (ftId != null) {
      sql += " AND wo_group_id in (select distinct cd_group_id from wfm.wo_cd_group_unit"
          + " where unit_id in (select unit_id from common_gnoc.unit where level < 50"
          + " start with unit_id = (select unit_id from common_gnoc.users where user_id = :userId)"
          + " connect by prior  parent_unit_id = unit_id))";
      parameters.put("userId", ftId);
    }
    if (woTypeId != null) {
      sql += " AND wo_group_id in (select distinct wo_group_id from wfm.wo_type_group t"
          + " where t.wo_type_id = :woTypeId)";
      parameters.put("woTypeId", woTypeId);
    }
    List<WoCdGroupInsideDTO> listCd = getNamedParameterJdbcTemplate().query(sql, parameters,
        BeanPropertyRowMapper.newInstance(WoCdGroupInsideDTO.class));
    if (listCd != null && listCd.size() > 0) {
      return listCd.get(0);
    }
    return null;
  }

  @Override
  public String getStationFollowNode(String node, String nationCode) {
    List<InfraStationsDTO> listStation;
    String sql1 = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_WO, "get-Station-Follow-Node-1");
    Map<String, Object> parameters1 = new HashMap<>();
    parameters1.put("node", node.trim().toLowerCase());
    if (StringUtils.isNotNullOrEmpty(nationCode)) {
      sql1 += " and b.nation_code = :nationCode) and a.nation_code = :nationCode";
      parameters1.put("nationCode", nationCode.trim());
    } else {
      sql1 += " and (b.nation_code is null or nation_code = 'VNM'))"
          + " and (a.nation_code is null or a.nation_code = 'VNM')";
    }
    listStation = getNamedParameterJdbcTemplate().query(sql1, parameters1,
        BeanPropertyRowMapper.newInstance(InfraStationsDTO.class));
    if (listStation != null && listStation.size() > 0) {
      return listStation.get(0).getStationCode();
    } else {
      String sql2 = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_WO, "get-Station-Follow-Node-2");
      Map<String, Object> parameters2 = new HashMap<>();
      parameters2.put("node", node.trim().toLowerCase());
      if (StringUtils.isNotNullOrEmpty(nationCode)) {
        sql2 += " and nation_code = :nationCode";
        parameters2.put("nationCode", nationCode.trim());
      } else {
        sql2 += " and (nation_code is null or nation_code = 'VNM')";
      }
      listStation = getNamedParameterJdbcTemplate().query(sql2, parameters2,
          BeanPropertyRowMapper.newInstance(InfraStationsDTO.class));
      if (listStation != null && listStation.size() > 0) {
        return listStation.get(0).getStationCode();
      }
    }
    return null;
  }

  @Override
  public WoCdGroupInsideDTO getCdByUnitCode(String unitCode, Long woTypeId, String cdGroupType) {
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_WO, "get-Cd-By-FT");
    Map<String, Object> parameters = new HashMap<>();
    if (StringUtils.isNotNullOrEmpty(cdGroupType)) {
      sql += " AND g.GROUP_TYPE_ID = :groupTypeId";
      parameters.put("groupTypeId", Long.valueOf(cdGroupType));
    }
    if (StringUtils.isNotNullOrEmpty(unitCode)) {
      sql += " AND wo_group_id in (select distinct cd_group_id from wfm.wo_cd_group_unit"
          + " where unit_id in (select unit_id from common_gnoc.unit where lower(unit_code) = :unitCode))";
      parameters.put("unitCode", unitCode.toLowerCase());
    }
    if (woTypeId != null) {
      sql += " AND wo_group_id in (select distinct wo_group_id from wfm.wo_type_group t"
          + " where t.wo_type_id = :woTypeId)";
      parameters.put("woTypeId", woTypeId);
    }
    List<WoCdGroupInsideDTO> listCd = getNamedParameterJdbcTemplate().query(sql, parameters,
        BeanPropertyRowMapper.newInstance(WoCdGroupInsideDTO.class));
    if (listCd != null && listCd.size() > 0) {
      return listCd.get(0);
    }
    return null;
  }

  @Override
  public Long checkCloseWoPostInspection(Long woId, Long numRecheck) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_WO, "check-Close-Wo-Post-Inspection");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("woId", woId);
    List<WoPostInspectionDTO> lst = getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(WoPostInspectionDTO.class));
    if (lst != null) {
      Long size = Long.valueOf(lst.size());
      if (size.compareTo(numRecheck) >= 0) {
        return 0L;
      }
    } else {
      return 0L;
    }
    return 1L;
  }

  @Override
  public List<WoPriorityDTO> getWoPriorityByWoTypeID(Long woTypeId) {
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_WO, "get-Wo-Priority-By-Wo-Type-ID");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("leeLocale", I18n.getLocale());
    if (!StringUtils.isStringNullOrEmpty(woTypeId)) {
      sqlQuery += " AND lle.WO_TYPE_ID= :woTypeId ";
      parameters.put("woTypeId", woTypeId);
    } else {
      sqlQuery += " AND lle.WO_TYPE_ID IS NULL ";
    }
    return getNamedParameterJdbcTemplate()
        .query(sqlQuery, parameters, BeanPropertyRowMapper.newInstance(WoPriorityDTO.class));
  }

  @Override
  public Datatable getListWoChild(WoInsideDTO searchDto) {
    String leeLocale = I18n.getLocale();
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_WO, "get-List-Wo-Child");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("leeLocale", leeLocale);
    parameters.put("offset", searchDto.getOffSetFromUser());
    parameters.put("unassigned", I18n.getLanguage("wo.status.UNASSIGNED"));
    parameters.put("assigned", I18n.getLanguage("wo.status.ASSIGNED"));
    parameters.put("reject", I18n.getLanguage("wo.status.REJECT"));
    parameters.put("rejectCd", I18n.getLanguage("wo.status.REJECT_CD"));
    parameters.put("dispatch", I18n.getLanguage("wo.status.DISPATCH"));
    parameters.put("accept", I18n.getLanguage("wo.status.ACCEPT"));
    parameters.put("inProcess", I18n.getLanguage("wo.status.INPROCESS"));
    parameters.put("pending", I18n.getLanguage("wo.status.PENDING"));
    parameters.put("closedCd", I18n.getLanguage("wo.status.CLOSED_CD"));
    parameters.put("closedFt", I18n.getLanguage("wo.status.CLOSED_FT"));
    parameters.put("draft", I18n.getLanguage("wo.status.DRAFT"));
    parameters.put("parentId", searchDto.getWoId());
    return getListDataTableBySqlQuery(sql, parameters,
        searchDto.getPage(), searchDto.getPageSize(), WoInsideDTO.class, searchDto.getSortName(),
        searchDto.getSortType());
  }

  @Override
  public List<WoInsideDTO> getListWoDTO(WoInsideDTO woInsideDTO, int rowStart, int maxRow,
      String sortType, String sortFieldList) {
    return onSearchEntity(WoEntity.class, woInsideDTO, rowStart, maxRow, sortType, sortFieldList);
  }


  @Override
  public List<WoInsideDTO> getListWoDTOByWoSystemId(String woSystemId) {
    List<WoInsideDTO> list = new ArrayList<>();
    List<WoEntity> dataEntity = (List<WoEntity>) findByMultilParam(WoEntity.class,
        "woSystemId",
        woSystemId);
    if (dataEntity != null && dataEntity.size() > 0) {
      for (WoEntity entity : dataEntity) {
        list.add(entity.toDTO());
      }
    }
    return list;
  }


  @Override
  public List<WoInsideDTO> getListWoByWoTypeId(Long woTypeId) {
    List<WoEntity> listEntity = findByMultilParam(WoEntity.class, "woTypeId", woTypeId);
    List<WoInsideDTO> listDTO = new ArrayList<>();
    if (listEntity != null && listEntity.size() > 0) {
      for (WoEntity entity : listEntity) {
        listDTO.add(entity.toDTO());
      }
      return listDTO;
    }
    return null;
  }

  @Override
  public Long getPriorityHot(String woTypeCode, String priorityCode) {
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_WO, "get-Priority-Hot");
    Query query = getEntityManager().createNativeQuery(sql);
    query.setParameter("woTypeCode", woTypeCode);
    query.setParameter("priorityCode", priorityCode);
    BigDecimal bigDecimal = (BigDecimal) query.getSingleResult();
    return bigDecimal.longValue();
  }

  @Override
  public List<SearchWoKpiCDBRForm> searchWoKpiCDBR(String startTime, String endTime) {
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_WO, "search-Wo-Kpi-CDBR");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("copperCable", I18n.getLanguage("materialThres.copperCable"));
    parameters.put("coaxialCable", I18n.getLanguage("materialThres.coaxialCable"));
    parameters.put("key", Constants.AP_PARAM.WO_TYPE_CDBR);
    parameters.put("startTime", startTime);
    parameters.put("endTime", endTime);
    return getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(SearchWoKpiCDBRForm.class));
  }

  @Override
  public List<String> getSequenseWo(String seqName, int size) {
    return getListSequense(seqName, size);
  }

  @Override
  public List<WoDTO> getListWoByListAccount(List<String> lstAccount, Long numDate) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_WO, "get-List-Wo-By-List-Account");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("numDate", numDate);
    parameters.put("lstAccount", lstAccount);
    return getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(WoDTO.class));
  }

  @Override
  public List<WoDTOSearch> getListWoByWoType(String woTypeCode, String createTimeFrom,
      String createTimeTo) {
    if (StringUtils.isNotNullOrEmpty(woTypeCode) && StringUtils.isNotNullOrEmpty(createTimeFrom)
        && StringUtils.isNotNullOrEmpty(createTimeTo)) {
      String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_WO, "get-List-Wo-By-Wo-Type");
      Map<String, Object> parameters = new HashMap<>();
      parameters.put("unassigned", I18n.getLanguage("wo.status.UNASSIGNED"));
      parameters.put("assigned", I18n.getLanguage("wo.status.ASSIGNED"));
      parameters.put("reject", I18n.getLanguage("wo.status.REJECT"));
      parameters.put("dispatch", I18n.getLanguage("wo.status.DISPATCH"));
      parameters.put("accept", I18n.getLanguage("wo.status.ACCEPT"));
      parameters.put("inProcess", I18n.getLanguage("wo.status.INPROCESS"));
      parameters.put("draft", I18n.getLanguage("wo.status.DRAFT"));
      parameters.put("closedFt", I18n.getLanguage("wo.status.CLOSED_FT"));
      parameters.put("closedCd", I18n.getLanguage("wo.status.CLOSED_CD"));
      parameters.put("pending", I18n.getLanguage("wo.status.PENDING"));
      parameters.put("startTimeFrom", createTimeFrom);
      parameters.put("startTimeTo", createTimeTo);
      parameters.put("woTypeCode", woTypeCode.toLowerCase());
      return getNamedParameterJdbcTemplate()
          .query(sql, parameters, BeanPropertyRowMapper.newInstance(WoDTOSearch.class));
    }
    return null;
  }

  @Override
  public List<WoDTOSearch> getListWOByUsers(String username, String summaryStatus, Integer isDetail,
      WoDTOSearch woDTO, int start, int count, int typeSearch) {
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_WO, "get-List-WO-By-Users");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("offset", userRepository.getOffsetFromUser(username));
    parameters.put("unassigned", I18n.getLanguage("wo.status.UNASSIGNED"));
    parameters.put("assigned", I18n.getLanguage("wo.status.ASSIGNED"));
    parameters.put("reject", I18n.getLanguage("wo.status.REJECT"));
    parameters.put("dispatch", I18n.getLanguage("wo.status.DISPATCH"));
    parameters.put("accept", I18n.getLanguage("wo.status.ACCEPT"));
    parameters.put("inProcess", I18n.getLanguage("wo.status.INPROCESS"));
    parameters.put("closedFt", I18n.getLanguage("wo.status.CLOSED_FT"));
    parameters.put("closedCd", I18n.getLanguage("wo.status.CLOSED_CD"));
    parameters.put("pending", I18n.getLanguage("wo.status.PENDING"));
    // tim kiem trang thai ft hoan thanh
    if (summaryStatus != null && !"6".equals(summaryStatus) && !"8".equals(summaryStatus)) {
      if (typeSearch == Constants.WO_TYPE_SEARCH.IS_FT) {
        sql += " and w.status <> 8 and w.status <> 7 and w.status <> 6 and w.status <> 2";
      } else if (typeSearch == Constants.WO_TYPE_SEARCH.IS_CD
          || typeSearch == Constants.WO_TYPE_SEARCH.IS_PROVINCE) {
        sql += " and w.status <> 8 and w.status <> 7"
            + " and (case when w.status = 2 then case when w.ft_id is not null then 1 else 0 end else 1 end ) = 1";
      }
    } else if ("6".equals(summaryStatus)) {
      if (typeSearch == Constants.WO_TYPE_SEARCH.IS_FT) {
        sql += " and w.status <> 8 and w.status <> 7 and w.status <> 2";
      }
    }
    if (woDTO != null) {
      if (StringUtils.isNotNullOrEmpty(woDTO.getStationCode())) {
        sql += " AND lower(w.STATION_CODE) like :stationCode escape '\\'";
        parameters
            .put("stationCode", StringUtils.convertLowerParamContains(woDTO.getStationCode()));
      }
      if (StringUtils.isLong(woDTO.getWoId())) {
        sql += " and w.wo_id = :woId";
        parameters.put("woId", Long.valueOf(woDTO.getWoId()));
      }
      if (StringUtils.isNotNullOrEmpty(woDTO.getWoCode())) {
        sql += " AND lower(w.wo_code) like :woCode escape '\\'";
        parameters
            .put("woCode", StringUtils.convertLowerParamContains(woDTO.getWoCode()));
      }
      if (StringUtils.isNotNullOrEmpty(woDTO.getWoSystemId())) {
        sql += " AND lower(w.wo_content) like :woSystemId escape '\\'";
        parameters
            .put("woSystemId", StringUtils.convertLowerParamContains(woDTO.getWoSystemId()));
      }
      if (StringUtils.isNotNullOrEmpty(woDTO.getAccountIsdn())) {
        sql += " AND lower(wd.account_isdn) like :accountIsdn escape '\\'";
        parameters
            .put("accountIsdn", StringUtils.convertLowerParamContains(woDTO.getAccountIsdn()));
      }
      if (StringUtils.isNotNullOrEmpty(woDTO.getWoTypeCode())) {
        sql += " AND lower(t.wo_type_code) = :woTypeCode";
        parameters.put("woTypeCode", woDTO.getWoTypeCode().toLowerCase());
      }
      if (StringUtils.isNotNullOrEmpty(woDTO.getStartTime())) {
        if (DataUtil.checkDateFormat(woDTO.getStartTime(), "dd/MM/yyyy HH:mm:ss")) {
          sql += " AND w.start_time >= TO_TIMESTAMP(:startTime,'dd/mm/yyyy hh24:mi:ss')";
          parameters.put("startTime", woDTO.getStartTime());
        }
      }
      if (StringUtils.isNotNullOrEmpty(woDTO.getEndTime())) {
        if (DataUtil.checkDateFormat(woDTO.getEndTime(), "dd/MM/yyyy HH:mm:ss")) {
          sql += " AND w.end_time <= TO_TIMESTAMP(:endTime,'dd/mm/yyyy hh24:mi:ss')";
          parameters.put("endTime", woDTO.getEndTime());
        }
      }
      if (woDTO.getCdId() != null) {
        sql += " AND w.cd_id = :cdId";
        parameters.put("cdId", Long.valueOf(woDTO.getCdId()));
      }
      if (StringUtils.isNotNullOrEmpty(woDTO.getStartTimeFrom())) {
        if (DataUtil.checkDateFormat(woDTO.getStartTimeFrom(), "dd/MM/yyyy HH:mm:ss")) {
          sql += " AND w.create_date >= TO_TIMESTAMP(:startTimeFrom,'dd/mm/yyyy hh24:mi:ss')";
          parameters.put("startTimeFrom", woDTO.getStartTimeFrom());
        }
      }
      if (StringUtils.isNotNullOrEmpty(woDTO.getStartTimeTo())) {
        if (DataUtil.checkDateFormat(woDTO.getStartTimeTo(), "dd/MM/yyyy HH:mm:ss")) {
          sql += " AND w.create_date <= TO_TIMESTAMP(:startTimeTo,'dd/mm/yyyy hh24:mi:ss')";
          parameters.put("startTimeTo", woDTO.getStartTimeTo());
        }
      }

      if (!StringUtils.isStringNullOrEmpty(woDTO.getCustomerGroupType())) {
        sql += " AND lower(w.CUSTOMER_GROUP_TYPE) = :customer_group_type ";
        parameters.put("customer_group_type", woDTO.getCustomerGroupType().trim().toLowerCase());
      }

      if (StringUtils.isNotNullOrEmpty(woDTO.getEndTimeFrom())) {
        if (DataUtil.checkDateFormat(woDTO.getEndTimeFrom(), "dd/MM/yyyy HH:mm:ss")) {
          sql += " AND w.end_time >= TO_TIMESTAMP(:endTimeFrom,'dd/mm/yyyy hh24:mi:ss')";
          parameters.put("endTimeFrom", woDTO.getEndTimeFrom());
        }
      }
      if (StringUtils.isNotNullOrEmpty(woDTO.getEndTimeTo())) {
        if (DataUtil.checkDateFormat(woDTO.getEndTimeTo(), "dd/MM/yyyy HH:mm:ss")) {
          sql += " AND w.end_time <= TO_TIMESTAMP(:endTimeTo,'dd/mm/yyyy hh24:mi:ss')";
          parameters.put("endTimeTo", woDTO.getEndTimeTo());
        }
      }

      // tim kiem theo danh sach tram
      if (woDTO.getLstStationCode() != null && woDTO.getLstStationCode().size() > 0) {
        sql += " and lower(w.STATION_CODE) in (";
        StringBuilder strChildStation = new StringBuilder();
        int countStation = 0;
        for (String stationCode : woDTO.getLstStationCode()) {
          String paramName = String.format("itemsta%d", countStation++);
          strChildStation.append(":" + paramName + ",");
          parameters.put(paramName, stationCode.trim().toLowerCase());
        }
        strChildStation.deleteCharAt(strChildStation.length() - 1);
        sql += strChildStation.toString() + ")";
      }
    }
    if (StringUtils.isLong(summaryStatus)) {
      // cho CD tiep nhan 0
      if (Constants.WO_STATUS.UNASSIGNED.equals(summaryStatus)) {
        sql += " and w.status = :status";
        parameters.put("status", Long.valueOf(Constants.WO_STATUS.UNASSIGNED));
      } // CD da tiep nhan 1
      else if (Constants.WO_STATUS.ASSIGNED.equals(summaryStatus)) {
        sql += " and w.status = :status";
        parameters.put("status", Long.valueOf(Constants.WO_STATUS.ASSIGNED));
      } // FT tu choi 2 and ft id not null
      else if (Constants.WO_STATUS.REJECT.equals(summaryStatus)) {
        sql += " and w.status = :status and w.ft_id is not null";
        parameters.put("status", Long.valueOf(Constants.WO_STATUS.REJECT));
      } // Da giao FT 3
      else if (Constants.WO_STATUS.DISPATCH.equals(summaryStatus)) {
        sql += " and w.status = :status";
        parameters.put("status", Long.valueOf(Constants.WO_STATUS.DISPATCH));
      } // FT da tiep nhan 4
      else if (Constants.WO_STATUS.ACCEPT.equals(summaryStatus)) {
        sql += " and w.status = :status";
        parameters.put("status", Long.valueOf(Constants.WO_STATUS.ACCEPT));
      } // Ft dang xu ly 5
      else if (Constants.WO_STATUS.INPROCESS.equals(summaryStatus)) {
        sql += " and w.status = :status";
        parameters.put("status", Long.valueOf(Constants.WO_STATUS.INPROCESS));
      } // Ft hoan thanh 6
      else if (Constants.WO_STATUS.CLOSED_FT.equals(summaryStatus)) {
        sql += " and w.status = :status";
        parameters.put("status", Long.valueOf(Constants.WO_STATUS.CLOSED_FT));
      } // tam dong 9
      else if (Constants.WO_STATUS.PENDING.equals(summaryStatus)) {
        sql += " and w.status = :status";
        parameters.put("status", Long.valueOf(Constants.WO_STATUS.PENDING));
      } // qua han end_time <sysdate
      else if (Constants.WO_STATUS.OVERDUE.equals(summaryStatus)) {
        if (typeSearch == Constants.WO_TYPE_SEARCH.IS_FT) {
          sql += " and (w.end_time < sysdate)";
        } else if (typeSearch == Constants.WO_TYPE_SEARCH.IS_CD
            || typeSearch == Constants.WO_TYPE_SEARCH.IS_PROVINCE) {
          sql += " and (w.end_time < sysdate)"
              + " and (case when w.status = 2 then case when w.ft_id is not null then 1 "
              + "                                       else 0 end"
              + "            else 1"
              + "      end) = 1";
        }
      } // sap qua han
      else if (Constants.WO_STATUS.GOING_OVERDURE.equals(summaryStatus)) {
        if (typeSearch == Constants.WO_TYPE_SEARCH.IS_FT) {
          sql += " and (w.end_time >= sysdate)";
        } else if (typeSearch == Constants.WO_TYPE_SEARCH.IS_CD
            || typeSearch == Constants.WO_TYPE_SEARCH.IS_PROVINCE) {
          sql += " and (w.end_time >= sysdate)"
              + " and (case when w.status = 2 then case when w.ft_id is not null then 1 "
              + "                                       else 0 end"
              + "            else 1"
              + "      end) = 1";
        }
      }// wo da dong
      else if (Constants.WO_STATUS.CLOSED_CD.equals(summaryStatus)) {
        sql += " and w.status = :status";
        parameters.put("status", Long.valueOf(Constants.WO_STATUS.CLOSED_CD));
      }
      //add new key 102 WO hot/Vip
      else if (Constants.WO_STATUS.HOTVIP.equals(summaryStatus)) {
        if (typeSearch == Constants.WO_TYPE_SEARCH.IS_FT) {
          sql += " and w.status in (3,4,5,9) and w.priority_id = p.priority_id"
              + " and p.PRIORITY_NAME in ('Nghiêm trọng','Rất nghiêm trọng','Hot')"
              + " and w.wo_system in ('TT','SPM')";
        } else if (typeSearch == Constants.WO_TYPE_SEARCH.IS_CD) {
          sql += " and w.status in (3,4,5,6,9)"
              + " and p.PRIORITY_NAME in ('Nghiêm trọng','Rất nghiêm trọng','Hot')"
              + " and w.wo_system in ('TT','SPM')";
        } else if (typeSearch == Constants.WO_TYPE_SEARCH.IS_PROVINCE) {
          sql += " and w.status in (3,4,5,6,9)"
              + " and p.PRIORITY_NAME in ('Nghiêm trọng','Rất nghiêm trọng','Hot')"
              + " and w.wo_system in ('TT','SPM')";
        }
      }
      //add new key 103 WO sap qua han
      else if (Constants.WO_STATUS.GOING_OVERTIME.equals(summaryStatus)) {
        if (typeSearch == Constants.WO_TYPE_SEARCH.IS_FT) {
          sql += " and w.status in (3,4,5,9)"
              + " and w.wo_type_id = t.wo_type_id and t.time_over is not null"
              + " and w.end_time BETWEEN sysdate and (sysdate + (t.time_over/24))";
        } else if (typeSearch == Constants.WO_TYPE_SEARCH.IS_CD) {
          sql += " and w.status in (3,4,5,6,9)"
              + " and w.wo_type_id = t.wo_type_id and t.time_over is not null"
              + " and w.end_time BETWEEN sysdate and (sysdate + (t.time_over/24))";
        } else if (typeSearch == Constants.WO_TYPE_SEARCH.IS_PROVINCE) {
          sql += " and w.status in (3,4,5,6,9)"
              + " and w.wo_type_id = t.wo_type_id and t.time_over is not null"
              + " and w.end_time BETWEEN sysdate and (sysdate + (t.time_over/24))";
        }
      }
      //add new key 105 WO khach hang doanh nghiep
      else if (Constants.WO_STATUS.BUSINESS_CUSTOMER.equals(summaryStatus)) {
        if (typeSearch == Constants.WO_TYPE_SEARCH.IS_FT) {
          sql += " and w.status in (3,4,5,9) and wd.CUSTOMER_GROUP_TYPE = '2'";
        } else if (typeSearch == Constants.WO_TYPE_SEARCH.IS_CD) {
          sql += " and wd.CUSTOMER_GROUP_TYPE = '2'";
        } else if (typeSearch == Constants.WO_TYPE_SEARCH.IS_PROVINCE) {
          sql += " and wd.CUSTOMER_GROUP_TYPE = '2'";
        }
      }
    }
    if (StringUtils.isNotNullOrEmpty(username)) {
      if (typeSearch == Constants.WO_TYPE_SEARCH.IS_FT) {
        sql += " and lower(ft.username) = :username";
        parameters.put("username", username.toLowerCase());
      }
      // cong viec giao cho CD tim kiem tat ca cong viec co CD Id = cd cua user
      else if (typeSearch == Constants.WO_TYPE_SEARCH.IS_CD) {
        sql += " and w.cd_id in (select a.wo_group_id"
            + "                  from wfm.wo_cd a"
            + "                  where a.user_id = (select user_id"
            + "                                      from common_gnoc.users"
            + "                                      where lower(username) = :username)"
            + "                   )";
        parameters.put("username", username.toLowerCase());
      } else if (typeSearch == Constants.WO_TYPE_SEARCH.IS_PROVINCE) {
        sql += " AND w.cd_id       IN "
            + "  (SELECT DISTINCT a.wo_group_id "
            + "  FROM wfm.wo_cd_group a "
            + "  WHERE a.group_type_id = 4 "
            + "  AND a.wo_group_id    IN "
            + "    ( SELECT DISTINCT b.cd_group_id "
            + "    FROM wfm.wo_cd_group_unit b "
            + "    WHERE b.unit_id IN "
            + "      ( SELECT DISTINCT c.unit_id "
            + "      FROM common_gnoc.unit c "
            + "        CONNECT BY prior c.unit_id   = c.parent_unit_id "
            + "        START WITH c.parent_unit_id IN ( "
            + "        (SELECT unit_id "
            + "        FROM common_gnoc.users d "
            + "        WHERE lower(d.username) = :username "
            + "        )) "
            + "      ) "
            + "    ) "
            + "  )";
        parameters.put("username", username.toLowerCase());
      }
    }
    if (isDetail != null) {
      if (isDetail == 1) { // TT/PT
        sql += " and (w.wo_system = 'PT' or w.wo_system = 'TT')";
      } else if (isDetail == 2) {  // SOC CDBR
        sql += " and (w.wo_system in ('SPM','SPM_VTNET') )";
      } else if (isDetail == 3) {  // MR
        sql += " and (w.wo_system = 'MR')";
      } else if (isDetail == 4) {  // CR
        sql += " and (w.wo_system = 'CR')";
      } else if (isDetail == 5) {
        sql += " and (w.wo_system = 'WFM-FT' or w.wo_system = 'WFM-OTHERS')";
      } else if (isDetail == 6) {
        sql += " and w.wo_system not in ('WFM-FT','WFM-OTHERS','CR','MR','SPM','TT','PT')";
      } else if (isDetail == 7) {
        sql += " and w.wo_system not in ('MR','SPM','SPM_VTNET','TT')";
      }
    }
    sql += " order by w.End_time asc, w.CUSTOMER_TIME_DESIRE_TO asc";
    Query query = getEntityManager().createNativeQuery(sql);
    query.unwrap(SQLQuery.class).
        addScalar("woId", new StringType()).
        addScalar("woCode", new StringType()).
        addScalar("isNeedSupport", new StringType()).
        addScalar("woContent", new StringType()).
        addScalar("woTypeName", new StringType()).
        addScalar("woTypeCode", new StringType()).
        addScalar("priorityName", new StringType()).
        addScalar("priorityId", new StringType()).
        addScalar("woSystem", new StringType()).
        addScalar("woSystemId", new StringType()).
        addScalar("cdId", new StringType()).
        addScalar("ftId", new StringType()).
        addScalar("planCode", new StringType()).
        addScalar("status", new StringType()).
        addScalar("result", new StringType()).
        addScalar("createPersonName", new StringType()).
        addScalar("createPeronEmail", new StringType()).
        addScalar("createPersonMobile", new StringType()).
        addScalar("woGroupName", new StringType()).
        addScalar("woGroupEmail", new StringType()).
        addScalar("woGroupMobile", new StringType()).
        addScalar("comments", new StringType()).
        addScalar("createDate", new StringType()).
        addScalar("startTime", new StringType()).
        addScalar("endTime", new StringType()).
        addScalar("fileName", new StringType()).
        addScalar("woDescription", new StringType()).
        addScalar("woTypeId", new StringType()).
        addScalar("accountIsdn", new StringType()).
        addScalar("serviceId", new StringType()).
        addScalar("infraType", new StringType()).
        addScalar("infraTypeName", new StringType()).
        addScalar("ccServiceId", new StringType()).
        addScalar("ccGroupId", new StringType()).
        addScalar("isCcResult", new StringType()).
        addScalar("ccResult", new StringType()).
        addScalar("checkQosInternetResult", new StringType()).
        addScalar("checkQosTHResult", new StringType()).
        addScalar("checkQrCode", new StringType()).
        addScalar("endPendingTime", new StringType()).
        addScalar("reasonName", new StringType()).
        addScalar("reasonId", new StringType()).
        addScalar("customer", new StringType()).
        addScalar("phone", new StringType()).
        addScalar("stationCode", new StringType()).
        addScalar("deviceCode", new StringType()).
        addScalar("ftName", new StringType()).
        addScalar("kedbCode", new StringType()).
        addScalar("kedbId", new StringType()).
        addScalar("requiredTtReason", new StringType()).
        addScalar("ableMop", new StringType()).
        addScalar("contractCode", new StringType()).
        addScalar("warehouseCode", new StringType()).
        addScalar("constructionCode", new StringType()).
        addScalar("numComplaint", new StringType()).
        addScalar("customerPhone", new StringType()).
        addScalar("linkCode", new StringType()).
        addScalar("linkId", new StringType()).
        addScalar("alarmId", new StringType()).
        addScalar("amiOneId", new StringType()).
        addScalar("distance", new StringType()).
        addScalar("cableCode", new StringType()).
        addScalar("cableTypeCode", new StringType()).
        addScalar("customerTimeDesireFrom", new StringType()).
        addScalar("customerTimeDesireTo", new StringType()).
        addScalar("portCorrectId", new StringType()).
        addScalar("errTypeNims", new StringType()).
        addScalar("subscriptionId", new StringType()).
        addScalar("numRecheck", new StringType()).
        addScalar("districtRecheck", new StringType()).
        addScalar("locationCode", new StringType()).
        addScalar("pointOk", new StringType()).
        addScalar("deviceType", new StringType()).
        addScalar("deviceTypeName", new StringType()).
        addScalar("failureReason", new StringType()).
        addScalar("customerName", new StringType()).
        addScalar("startWorkingTime", new StringType())
        .setResultTransformer(Transformers.aliasToBean(WoDTOSearch.class));
    if (!parameters.isEmpty()) {
      for (Map.Entry<String, Object> map : parameters.entrySet()) {
        query.setParameter(map.getKey(), map.getValue());
      }
    }
    query.setFirstResult(start * count);
    if (count > 0) {
      query.setMaxResults(count);
    } else {
      query.setMaxResults(50);
    }

    List<WoDTOSearch> list = query.getResultList();
    Map<String, String> mapPriorityColor = new HashMap<>();
    try {
      mapPriorityColor = getMapConfigPropertyValue(Constants.AP_PARAM.PRIORITY_COLOR_CODE);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    for (WoDTOSearch wo : list) {
      wo.setTotalRow(null);
      wo.setIndexRow(null);
      if (StringUtils.isNotNullOrEmpty(wo.getFileName())) {
        String[] fileArr = wo.getFileName().split(",");
        String url = vsmartDownloadUrl;
        SimpleDateFormat toPath = new SimpleDateFormat("yyyy/MM/dd/", Locale.ENGLISH);
        SimpleDateFormat toDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH);
        String fullFileName = "";
        String filePath = "";
        try {
          filePath = url + toPath.format(toDate.parse(wo.getCreateDate()));
        } catch (ParseException ex) {
          log.error(ex.getMessage(), ex);
        }
        for (String file : fileArr) {
          fullFileName += filePath + file.trim() + "|" + file.trim() + ",";
        }
        wo.setFileName(fullFileName);
      }
      // map ma mau
      String color = mapPriorityColor.get(wo.getPriorityName());
      if (color != null) {
        wo.setPriorityColorCode(color);
        wo.setPriorityId(wo.getPriorityName());
      }
    }
    StringUtils.escapeHTMLString(list);

    if (list != null && list.size() > 0) {
      List<LanguageExchangeDTO> lstLanguage = getLanguageExchange(
          Constants.LANGUAGUE_EXCHANGE_SYSTEM.WO,
          Constants.APPLIED_BUSSINESS.WO_PRIORITY);
      List<LanguageExchangeDTO> lstLanguage2 = getLanguageExchange(
          Constants.LANGUAGUE_EXCHANGE_SYSTEM.WO,
          Constants.APPLIED_BUSSINESS.WO_TYPE);
      try {
        list = setLanguage(list, lstLanguage, "priorityId", "priorityName");
        list = setLanguage(list, lstLanguage2, "woTypeId", "woTypeName");
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }

    return list;
  }

  public Map<String, String> getMapConfigPropertyValue(String key) {
    List<ConfigPropertyDTO> result;
    Map<String, String> mapResult = new HashMap<>();
    try {
      String sql = "select a.key, a.value, a.description"
          + "         from common_gnoc.config_property a"
          + "         where a.key = :key";
      Map<String, String> params = new HashMap<>();
      params.put("key", key);
      result = getNamedParameterJdbcTemplate()
          .query(sql, params, BeanPropertyRowMapper.newInstance(ConfigPropertyDTO.class));
      if (result != null && result.size() > 0) {
        ConfigPropertyDTO o = result.get(0);
        String[] value = o.getValue().split(",");
        String[] description = o.getDescription().split(",");
        for (int i = 0; i < value.length; i++) {
          mapResult.put(value[i], description[i]);
        }
        return mapResult;
      }
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      throw ex;
    }
    return null;
  }

  public BaseDto sqlGetListDataSearchWeb(WoInsideDTO searchDto) {
    String leeLocale = I18n.getLocale();
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_WO, "get-List-Data-Search-Web");
    BaseDto baseDto = genParam(sql, leeLocale, searchDto);
    return baseDto;
  }

  @Override
  public List<WoCdGroupInsideDTO> getListCdByLocation(String locationCode) {
    try {
      String sqlQuery = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_WO, "get-list-cd-by-location-vsmart");
      Map<String, Object> parameters = new HashMap<>();
      parameters.put("locationCode", StringUtils.convertLowerParamContains(locationCode));
      return getNamedParameterJdbcTemplate()
          .query(sqlQuery, parameters, BeanPropertyRowMapper.newInstance(WoCdGroupInsideDTO.class));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

  @Override
  public List<WoMaterialDeducteInsideDTO> getListMaterial(
      WoMaterialDeducteInsideDTO woMaterialDeducteInsideDTO) {
    try {
      String sqlQuery = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_WO, "get-list-material-vsmart");
      Map<String, Object> parameters = new HashMap<>();
      parameters.put("woId", woMaterialDeducteInsideDTO.getWoId());

      if (!StringUtils.isStringNullOrEmpty(woMaterialDeducteInsideDTO.getUserName())) {
        sqlQuery += " and lower(a.user_name) = :userName ";
        parameters.put("userName", woMaterialDeducteInsideDTO.getUserName());
      }
      return getNamedParameterJdbcTemplate()
          .query(sqlQuery, parameters,
              BeanPropertyRowMapper.newInstance(WoMaterialDeducteInsideDTO.class));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

  @Override
  public List<CountWoForVSmartForm> getCountWoForVSmart(CountWoForVSmartForm countWoForVSmartForm) {
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_WO, "get-list-count-wo-for-vsmart");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("userName", countWoForVSmartForm.getUserName().toLowerCase());

    if (!StringUtils.isStringNullOrEmpty(countWoForVSmartForm.getSummaryStatus()) && DataUtil
        .isNumber(countWoForVSmartForm.getSummaryStatus())) {
      String summaryStatusValue = countWoForVSmartForm.getSummaryStatus();

      // cho CD tiep nhan 0
      if (Constants.WO_STATUS.UNASSIGNED.equals(summaryStatusValue)) {
        sqlQuery += " and w.status = :status0 ";
        parameters.put("status0", Constants.WO_STATUS.UNASSIGNED);
      }
      // CD da tiep nhan 1
      if (Constants.WO_STATUS.ASSIGNED.equals(summaryStatusValue)) {
        sqlQuery += " and w.status = :status1 ";
        parameters.put("status1", Constants.WO_STATUS.ASSIGNED);
      }
      // Da giao FT 3
      if (Constants.WO_STATUS.DISPATCH.equals(summaryStatusValue)) {
        sqlQuery += " and w.status = :status3 ";
        parameters.put("status3", Constants.WO_STATUS.DISPATCH);
      }

      // FT da tiep nhan 4
      if (Constants.WO_STATUS.ACCEPT.equals(summaryStatusValue)) {
        sqlQuery += " and w.status = :status4 ";
        parameters.put("status4", Constants.WO_STATUS.ACCEPT);
      }
      // Ft dang xu ly 5
      else if (Constants.WO_STATUS.INPROCESS.equals(summaryStatusValue)) {
        sqlQuery += " and w.status = :status5 ";
        parameters.put("status5", Constants.WO_STATUS.INPROCESS);
      }
      // Ft hoan thanh 6
      else if (Constants.WO_STATUS.CLOSED_FT.equals(summaryStatusValue)) {
        sqlQuery += " and (w.status = :status6 ) ";
        parameters.put("status6", Constants.WO_STATUS.CLOSED_FT);
      }
      // tam dong 9
      else if (Constants.WO_STATUS.PENDING.equals(summaryStatusValue)) {
        sqlQuery += " and w.status = :status9 ";
        parameters.put("status9", Constants.WO_STATUS.PENDING);
      }
      // qua han end_time <sysdate
      else if (Constants.WO_STATUS.OVERDUE.equals(summaryStatusValue)) {
        sqlQuery += " and (w.end_time < sysdate ) ";
      }
      // sap qua han
      else if (Constants.WO_STATUS.GOING_OVERDURE.equals(summaryStatusValue)) {
        sqlQuery += " and (w.end_time >= sysdate ) ";
      }
    }
    sqlQuery += " group by g.wo_group_name,g.wo_group_code order by wo_group_code ";

    return getNamedParameterJdbcTemplate()
        .query(sqlQuery, parameters,
            BeanPropertyRowMapper.newInstance(CountWoForVSmartForm.class));
  }

  @Override
  public WoSalaryResponse countWOByFT(WoSalaryResponse woSalaryResponse) {
    WoSalaryResponse response = new WoSalaryResponse();
    List<TotalWoForm> rs = new ArrayList<>();
    List<FieldForm> value;
    try {
      boolean isCheckStart = ""
          .equals(DataUtil.validateDateTimeDdMmYyyy(woSalaryResponse.getStartPeriod()));
      boolean isCheckEnd = ""
          .equals(DataUtil.validateDateTimeDdMmYyyy(woSalaryResponse.getEndPeriod()));
      if (isCheckEnd && isCheckStart) {
        String sqlQuery = SQLBuilder
            .getSqlQueryById(SQLBuilder.SQL_MODULE_WO, "get-list-count-wo-by-ft-vsmart");
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("p_start_time", woSalaryResponse.getStartPeriod());
        parameters.put("p_end_time", woSalaryResponse.getEndPeriod());
        List<FieldForm> lst = getNamedParameterJdbcTemplate()
            .query(sqlQuery, parameters, BeanPropertyRowMapper.newInstance(FieldForm.class));

        Map<String, FieldForm> lstAll = new HashMap<>();
        if (lst != null && !lst.isEmpty()) {
          for (FieldForm tmp : lst) {
            lstAll.put(tmp.getFieldName().trim() + "_" + tmp.getFieldKey(), tmp);
          }
        }

        List<String> username = woSalaryResponse.getUserName();
        if (username != null && !username.isEmpty()) {
          for (String tmp : username) {
            value = new ArrayList<>();
            UsersInsideDto u = commonRepository.getUserByUserName(tmp);
            String id =
                u == null ? null : (u.getUserId() == null ? null : u.getUserId().toString());
            if (id == null || "".equals(id)) {
              rs.add(
                  new TotalWoForm(tmp, Constants.RESULT.ERROR, I18n.getLanguage("wo.ft.notExist"),
                      value));
            } else {
              //A_tong so cong viec hoan thanh dung han trong chu ky
              if (lstAll.containsKey(tmp.trim() + "_A")) {
                value.add(lstAll.get(tmp.trim() + "_A"));
              } else {
                value.add(new FieldForm("A", tmp.trim(), "0"));
              }

              //B_tong so cong viec hoan thanh qua han trong chu ky
              if (lstAll.containsKey(tmp.trim() + "_B")) {
                value.add(lstAll.get(tmp.trim() + "_B"));
              } else {
                value.add(new FieldForm("B", tmp.trim(), "0"));
              }

              //C_tong so cong viec den han hoan thanh nhung chua hoan thanh
              if (lstAll.containsKey(tmp.trim() + "_C")) {
                value.add(lstAll.get(tmp.trim() + "_C"));
              } else {
                value.add(new FieldForm("C", tmp.trim(), "0"));
              }

              //D_so cong viec hoan thanh va chua hoan thanh qua han trong 5 ngay
              if (lstAll.containsKey(tmp.trim() + "_D")) {
                value.add(lstAll.get(tmp.trim() + "_D"));
              } else {
                value.add(new FieldForm("D", tmp.trim(), "0"));
              }

              //E_so cong viec da hoan thanh trong han hoac qua han chua qua 5 ngay
              if (lstAll.containsKey(tmp.trim() + "_E")) {
                value.add(lstAll.get(tmp.trim() + "_E"));
              } else {
                value.add(new FieldForm("E", tmp.trim(), "0"));
              }

              //F_so cong viec chua hoan thanh qua han chua den 5 ngay
              if (lstAll.containsKey(tmp.trim() + "_F")) {
                value.add(lstAll.get(tmp.trim() + "_F"));
              } else {
                value.add(new FieldForm("F", tmp.trim(), "0"));
              }
              rs.add(new TotalWoForm(tmp, Constants.RESULT.SUCCESS, "", value));
            }
          }
        }

        response.setKey(Constants.RESULT.SUCCESS);
        response.setMessage("");
        response.setValue(rs);
      } else if (!isCheckStart) {
        response.setKey(Constants.RESULT.ERROR);
        response.setMessage(I18n.getLanguage("wo.start.time.invalid"));
      } else {
        response.setKey(Constants.RESULT.ERROR);
        response.setMessage(I18n.getLanguage("wo.end.time.invalid"));
      }
      return response;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

  @Override
  public WoHistoryInsideDTO getWoHisFinalClose(Long woId) {
    try {
      Double offset = TimezoneContextHolder.getOffsetDouble();
      WoHistoryInsideDTO woHistoryInsideDTO = new WoHistoryInsideDTO();
      woHistoryInsideDTO.setWoId(woId);
      woHistoryInsideDTO.setOffset(offset);
      woHistoryInsideDTO.setPage(1);
      woHistoryInsideDTO.setPageSize(1);
      Datatable datatable = woHistoryRepository.getListWoHistoryByWoId(woHistoryInsideDTO);
      if (datatable != null && !datatable.getData().isEmpty()) {
        List<WoHistoryInsideDTO> lstData = (List<WoHistoryInsideDTO>) datatable.getData();
        List<WoHistoryInsideDTO> lstTemp = lstData.stream()
            .filter(o -> Constants.WO_STATUS.CLOSED_CD.equals(o.getNewStatus().toString()))
            .collect(Collectors.toList());
        if (lstTemp != null && !lstTemp.isEmpty()) {
          return lstTemp.get(0);
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

  @Override
  public Integer getCountWOByUsers(WoDTOSearch woDTO) {
    try {
      String sqlQuery = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_WO, "get-count-wo-by-user");
      Map<String, Object> parameters = new HashMap<>();

      String summaryStatus = woDTO.getSummaryStatus();
      Integer typeSearch = woDTO.getTypeSearch();
      String username = woDTO.getUsername();
      Integer isDetail = woDTO.getIsDetail();

      if (summaryStatus != null && !"6".equals(summaryStatus) && !"8"
          .equals(summaryStatus)) {  // tim kiem trang thai ft hoan thanh
        if (typeSearch == Constants.WO_TYPE_SEARCH.IS_FT) {
          sqlQuery += " and w.status <> 8 and w.status <> 7 and w.status <> 6 and w.status <> 2 ";
        } else if (typeSearch == Constants.WO_TYPE_SEARCH.IS_CD
            || typeSearch == Constants.WO_TYPE_SEARCH.IS_PROVINCE) {
          sqlQuery += " and w.status <> 8 and w.status <> 7 "
              + " and (case when w.status = 2 then case when w.ft_id is not null then 1 else 0 end else 1 end )=1 ";
        }
      } else if ("6".equals(summaryStatus)) {
        if (typeSearch == Constants.WO_TYPE_SEARCH.IS_FT) {
          sqlQuery += " and w.status <> 8 and w.status <> 7 and w.status <> 2 ";
        }
      }
      if (woDTO != null) {
        if (DataUtil.isNumber(woDTO.getWoId())) {
          sqlQuery += " and w.wo_id = :woId ";
          parameters.put("woId", woDTO.getWoId());
        }
        if (!StringUtils.isStringNullOrEmpty(woDTO.getWoCode())) {
          sqlQuery += " AND lower(w.wo_code) like (:woCode) escape '\\' ";
          parameters.put("woCode", StringUtils.convertLowerParamContains(woDTO.getWoCode()));
        }
        if (!StringUtils.isStringNullOrEmpty(woDTO.getWoSystemId())) {
          sqlQuery += " AND lower(w.wo_content) like (:woSystemId) escape '\\' ";
          parameters
              .put("woSystemId", StringUtils.convertLowerParamContains(woDTO.getWoSystemId()));
        }
        if (!StringUtils.isStringNullOrEmpty(woDTO.getAccountIsdn())) {
          sqlQuery += " AND lower(wd.account_isdn) like (:accountIsdn) escape '\\'";
          parameters
              .put("accountIsdn", StringUtils.convertLowerParamContains(woDTO.getAccountIsdn()));
        }
        if (!StringUtils.isStringNullOrEmpty(woDTO.getWoTypeCode())) {
          sqlQuery += " AND lower(t.wo_type_code) = :woTypeCode ";
          parameters.put("woTypeCode", woDTO.getWoTypeCode().trim().toLowerCase());
        }

        if (!StringUtils.isStringNullOrEmpty(woDTO.getStationCode())) {
          sqlQuery += " AND lower(w.STATION_CODE) like (:stationCode) escape '\\' ";
          parameters
              .put("stationCode", StringUtils.convertLowerParamContains(woDTO.getStationCode()));
        }

        Date startTime = null;
        try {
          if (!StringUtils.isStringNullOrEmpty(woDTO.getStartTime())) {
            startTime = DateTimeUtils
                .convertStringToTime(woDTO.getStartTime(), "dd/MM/yyyy HH:mm:ss");
          }
        } catch (Exception e) {
          log.error(e.getMessage(), e);
        }
        if (startTime != null) {
          System.out.println(startTime);
          sqlQuery += " AND w.start_time >= :startTime ";
          parameters.put("startTime", startTime);
        }
        Date endTime = null;
        try {
          if (!StringUtils.isStringNullOrEmpty(woDTO.getEndTime())) {
            endTime = DateTimeUtils.convertStringToTime(woDTO.getEndTime(), "dd/MM/yyyy HH:mm:ss");
          }
        } catch (Exception e) {
          log.error(e.getMessage(), e);
        }
        if (endTime != null) {
          System.out.println(endTime);
          sqlQuery += " AND w.end_time <= :endTime ";
          parameters.put("endTime", endTime);
        }
        if (woDTO.getCdId() != null) {
          sqlQuery += " AND w.cd_id = :cdId ";
          parameters.put("cdId", Long.valueOf(woDTO.getCdId()));
        }

        // tim kiem theo thoi gian tao
        Date startTimeFrom = null;
        try {
          if (!StringUtils.isStringNullOrEmpty(woDTO.getStartTimeFrom())) {
            startTimeFrom = DateTimeUtils
                .convertStringToTime(woDTO.getStartTimeFrom(), "dd/MM/yyyy HH:mm:ss");
          }
        } catch (Exception e) {
          log.error(e.getMessage(), e);
        }
        if (startTimeFrom != null) {
          System.out.println(startTimeFrom);
          sqlQuery += " AND w.create_date >= :startTimeFrom ";
          parameters.put("startTimeFrom", startTimeFrom);
        }
        Date startTimeTo = null;
        try {
          if (!StringUtils.isStringNullOrEmpty(woDTO.getStartTimeTo())) {
            startTimeTo = DateTimeUtils
                .convertStringToTime(woDTO.getStartTimeTo(), "dd/MM/yyyy HH:mm:ss");
          }
        } catch (Exception e) {
          log.error(e.getMessage(), e);
        }
        if (startTimeTo != null) {
          System.out.println(startTimeTo);
          sqlQuery += " AND w.create_date <= :startTimeTo ";
          parameters.put("startTimeTo", startTimeTo);
        }

        if (StringUtils.isNotNullOrEmpty(woDTO.getEndTimeFrom())) {
          if (DataUtil.checkDateFormat(woDTO.getEndTimeFrom(), "dd/MM/yyyy HH:mm:ss")) {
            sqlQuery += " AND w.end_time >= TO_TIMESTAMP(:endTimeFrom,'dd/mm/yyyy hh24:mi:ss')";
            parameters.put("endTimeFrom", woDTO.getEndTimeFrom());
          }
        }
        if (StringUtils.isNotNullOrEmpty(woDTO.getEndTimeTo())) {
          if (DataUtil.checkDateFormat(woDTO.getEndTimeTo(), "dd/MM/yyyy HH:mm:ss")) {
            sqlQuery += " AND w.end_time <= TO_TIMESTAMP(:endTimeTo,'dd/mm/yyyy hh24:mi:ss')";
            parameters.put("endTimeTo", woDTO.getEndTimeTo());
          }
        }
        if (woDTO.getLstStationCode() != null && woDTO.getLstStationCode().size() > 0) {
          sqlQuery += " and lower(w.STATION_CODE) in (";
          StringBuilder strChildStation = new StringBuilder();
          int countStation = 0;
          for (String stationCode : woDTO.getLstStationCode()) {
            String paramName = String.format("itemsta%d", countStation++);
            strChildStation.append(":" + paramName + ",");
            parameters.put(paramName, stationCode.trim().toLowerCase());
          }
          strChildStation.deleteCharAt(strChildStation.length() - 1);
          sqlQuery += strChildStation.toString() + ")";
        }
      }

      if (DataUtil.isNumber(summaryStatus)) {
        String summaryStatusValue = summaryStatus;
        // cho CD tiep nhan 0
        if (Constants.WO_STATUS.UNASSIGNED.equals(summaryStatusValue)) {
          sqlQuery += " and w.status = :status ";
          parameters.put("status", Constants.WO_STATUS.UNASSIGNED);
        }
        // CD da tiep nhan 1
        else if (Constants.WO_STATUS.ASSIGNED.equals(summaryStatusValue)) {
          sqlQuery += " and w.status = :status ";
          parameters.put("status", Constants.WO_STATUS.ASSIGNED);
        }
        // FT tu choi 2 and ft id not null
        else if (Constants.WO_STATUS.REJECT.equals(summaryStatusValue)) {
          sqlQuery += " and w.status = :status and w.ft_id is not null ";
          parameters.put("status", Constants.WO_STATUS.REJECT);
        }
        // Da giao FT 3
        else if (Constants.WO_STATUS.DISPATCH.equals(summaryStatusValue)) {
          sqlQuery += " and w.status = :status ";
          parameters.put("status", Constants.WO_STATUS.DISPATCH);
        }
        // FT da tiep nhan 4
        else if (Constants.WO_STATUS.ACCEPT.equals(summaryStatusValue)) {
          sqlQuery += " and w.status = :status ";
          parameters.put("status", Constants.WO_STATUS.ACCEPT);
        }
        // Ft dang xu ly 5
        else if (Constants.WO_STATUS.INPROCESS.equals(summaryStatusValue)) {
          sqlQuery += " and w.status = :status ";
          parameters.put("status", Constants.WO_STATUS.INPROCESS);
        }
        // Ft hoan thanh 6
        else if (Constants.WO_STATUS.CLOSED_FT.equals(summaryStatusValue)) {
          sqlQuery += " and (w.status = :status  )  ";
          parameters.put("status", Constants.WO_STATUS.CLOSED_FT);
        }
        // tam dong 9
        else if (Constants.WO_STATUS.PENDING.equals(summaryStatusValue)) {
          sqlQuery += " and w.status = :status ";
          parameters.put("status", Constants.WO_STATUS.PENDING);
        }
        // qua han end_time <sysdate
        else if (Constants.WO_STATUS.OVERDUE.equals(summaryStatusValue)) {
          if (typeSearch == Constants.WO_TYPE_SEARCH.IS_FT) {
            sqlQuery += " and (w.end_time < sysdate )";
          } else if (typeSearch == Constants.WO_TYPE_SEARCH.IS_CD
              || typeSearch == Constants.WO_TYPE_SEARCH.IS_PROVINCE) {
            sqlQuery += " and (w.end_time < sysdate ) and (case when w.status = 2 then case when w.ft_id is not null then 1 else 0 end else 1 end )=1 ";
          }
        }
        // sap qua han
        else if (Constants.WO_STATUS.GOING_OVERDURE.equals(summaryStatusValue)) {
          if (typeSearch == Constants.WO_TYPE_SEARCH.IS_FT) {
            sqlQuery += " and (w.end_time >= sysdate ) ";
          } else if (typeSearch == Constants.WO_TYPE_SEARCH.IS_CD
              || typeSearch == Constants.WO_TYPE_SEARCH.IS_PROVINCE) {
            sqlQuery += " and (w.end_time >= sysdate ) and (case when w.status = 2 then case when w.ft_id is not null then 1 else 0 end else 1 end )=1 ";
          }
        }
        // wo da dong
        else if (Constants.WO_STATUS.CLOSED_CD.equals(summaryStatusValue)) {
          sqlQuery += " and w.status = :status ";
          parameters.put("status", Constants.WO_STATUS.CLOSED_CD);
        }
        //add new key 102 WO hot/Vip
        else if (Constants.WO_STATUS.HOTVIP.equals(summaryStatusValue)) {
          if (typeSearch == Constants.WO_TYPE_SEARCH.IS_FT) {
            sqlQuery += " and w.status in (3,4,5,9) and w.priority_id = p.priority_id"
                + " and p.PRIORITY_NAME in ('Nghiêm trọng','Rất nghiêm trọng','Hot')"
                + " and w.wo_system in ('TT','SPM')";
          } else if (typeSearch == Constants.WO_TYPE_SEARCH.IS_CD) {
            sqlQuery += " and w.status in (3,4,5,6,9)"
                + " and p.PRIORITY_NAME in ('Nghiêm trọng','Rất nghiêm trọng','Hot')"
                + " and w.wo_system in ('TT','SPM')";
          } else if (typeSearch == Constants.WO_TYPE_SEARCH.IS_PROVINCE) {
            sqlQuery += " and w.status in (3,4,5,6,9)"
                + " and p.PRIORITY_NAME in ('Nghiêm trọng','Rất nghiêm trọng','Hot')"
                + " and w.wo_system in ('TT','SPM')";
          }
        }
        //add new key 103 WO sap qua han
        else if (Constants.WO_STATUS.GOING_OVERTIME.equals(summaryStatusValue)) {
          if (typeSearch == Constants.WO_TYPE_SEARCH.IS_FT) {
            sqlQuery += " and w.status in (3,4,5,9)"
                + " and w.wo_type_id = t.wo_type_id and t.time_over is not null"
                + " and w.end_time BETWEEN sysdate and (sysdate + (t.time_over/24))";
          } else if (typeSearch == Constants.WO_TYPE_SEARCH.IS_CD) {
            sqlQuery += " and w.status in (3,4,5,6,9)"
                + " and w.wo_type_id = t.wo_type_id and t.time_over is not null"
                + " and w.end_time BETWEEN sysdate and (sysdate + (t.time_over/24))";
          } else if (typeSearch == Constants.WO_TYPE_SEARCH.IS_PROVINCE) {
            sqlQuery += " and w.status in (3,4,5,6,9)"
                + " and w.wo_type_id = t.wo_type_id and t.time_over is not null"
                + " and w.end_time BETWEEN sysdate and (sysdate + (t.time_over/24))";
          }
        }
        //add new key 105 WO khach hang doanh nghiep
        else if (Constants.WO_STATUS.BUSINESS_CUSTOMER.equals(summaryStatusValue)) {
          if (typeSearch == Constants.WO_TYPE_SEARCH.IS_FT) {
            sqlQuery += " and w.status in (3,4,5,9) and wd.CUSTOMER_GROUP_TYPE = '2'";
          } else if (typeSearch == Constants.WO_TYPE_SEARCH.IS_CD) {
            sqlQuery += " and wd.CUSTOMER_GROUP_TYPE = '2'";
          } else if (typeSearch == Constants.WO_TYPE_SEARCH.IS_PROVINCE) {
            sqlQuery += " and wd.CUSTOMER_GROUP_TYPE = '2'";
          }
        }
      }

      if (!StringUtils.isStringNullOrEmpty(username)) {
        if (typeSearch == Constants.WO_TYPE_SEARCH.IS_FT) {
          sqlQuery += " and lower(ft.username) = :username ";
          parameters.put("username", username.toLowerCase(Locale.ENGLISH).trim());
        } else if (typeSearch
            == Constants.WO_TYPE_SEARCH.IS_CD) { // cong viec giao cho CD tim kiem tat ca cong viec co CD Id = cd cua user
          sqlQuery += " and w.cd_id in (select a.wo_group_id from wfm.wo_cd a where a.user_id = (select user_id from common_gnoc.users where lower(username) =:username)) ";
          parameters.put("username", username.toLowerCase(Locale.ENGLISH).trim());
        }// ThanhLV12_start
        else if (typeSearch == Constants.WO_TYPE_SEARCH.IS_PROVINCE) {
          sqlQuery += " and w.cd_id in ("
              + " select a.wo_group_id from wfm.wo_cd_group a "
              + " where a.group_type_id = 4"
              + " and a.wo_group_id in ("
              + "    select b.cd_group_id from wfm.wo_cd_group_unit b where b.unit_id in ("
              + "        select c.unit_id from common_gnoc.unit c CONNECT by prior c.unit_id = c.parent_unit_id"
              + "        START with c.parent_unit_id = (select unit_id from common_gnoc.users d where lower(d.username) =:username)"
              + "    )"
              + ")"
              + ")";
          parameters.put("username", username.toLowerCase());
        }
        // ThanhLV12_end

      }

      if (isDetail != null) {
        if (isDetail == 1) { // TT/PT
          sqlQuery += " and (w.wo_system = 'PT' or w.wo_system = 'TT')";
        } else if (isDetail == 2) {  // SOC CDBR
          sqlQuery += " and (w.wo_system in ('SPM','SPM_VTNET') )";
        } else if (isDetail == 3) {  // MR
          sqlQuery += " and (w.wo_system = 'MR')";
        } else if (isDetail == 4) {  // CR
          sqlQuery += " and (w.wo_system = 'CR')";
        } else if (isDetail == 5) {
          sqlQuery += " and (w.wo_system = 'WFM-FT' or w.wo_system = 'WFM-OTHERS')";
        } else if (isDetail == 6) {
          sqlQuery += " and w.wo_system not in ('WFM-FT','WFM-OTHERS','CR','MR','SPM','TT','PT')";
        } else if (isDetail == 7) {
          sqlQuery += " and w.wo_system not in ('MR','SPM','SPM_VTNET','TT')";
        }
      }
      sqlQuery += ")";
      List<WoDTOSearch> lst = getNamedParameterJdbcTemplate()
          .query(sqlQuery, parameters, BeanPropertyRowMapper.newInstance(WoDTOSearch.class));
      int count = 0;
      if (lst != null && !lst.isEmpty()) {
        count = lst.get(0).getTotalRow();
      }
      return count;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

  @Override
  public List<KpiCompleteVsamrtForm> getListKpiComplete(List<String> lstUser, Date start,
      Date end) {
    Map<String, Object> parameters = new HashMap<>();
    String sql =
        " select b.username userName,a.is_completed_on_vsmart numUpdateOnVsamrt,count(a.wo_id) numUpdateOnWeb from wo a, common_gnoc.users b "
            + " where a.ft_id = b.user_id and a.wo_system = 'SPM' "
            + " and lower(b.username) in (:lstUser) "
            + " and a.finish_time >=:start "
            + " and a.finish_time <:end "
            + " group by b.username,a.is_completed_on_vsmart ";
    parameters.put("lstUser", lstUser);
    parameters.put("start", start);
    parameters.put("end", end);
    return getNamedParameterJdbcTemplate().query(sql, parameters,
        BeanPropertyRowMapper.newInstance(KpiCompleteVsamrtForm.class));
  }

  @Override
  public List<WoChecklistDTO> getListWoChecklistDetailByWoId(String woId) {
    Map<String, Object> parameters = new HashMap<>();
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_WO, "get-List-Wo-Checklist-Detail-By-WoId");
    parameters.put("woId", woId);
    return getNamedParameterJdbcTemplate().query(sql, parameters,
        BeanPropertyRowMapper.newInstance(WoChecklistDTO.class));
  }

  @Override
  public List<ResultDTO> getWOSummaryInfobyUser(String username, int typeSearch, Long cdId,
      String createTimeFrom, String createTimeTo) {
    List<ResultDTO> lstResult = new ArrayList<>();
    StringBuilder sql = new StringBuilder();
    String sqlTimeFrom = " and CREATE_DATE >= TO_TIMESTAMP(:createTimeFrom,'dd/mm/yyyy hh24:mi:ss')";
    String sqlTimeTo = " and CREATE_DATE <= TO_TIMESTAMP(:createTimeTo,'dd/mm/yyyy hh24:mi:ss')";
    Map<String, Object> parameters = new HashMap<>();
    if (typeSearch == Constants.WO_TYPE_SEARCH.IS_FT) {
      sql.append(
          "select '3' key, count(wo_id) quantitySucc from wo where ft_id = :ftId and status = 3");
      if (StringUtils.isNotNullOrEmpty(createTimeFrom) && StringUtils
          .isNotNullOrEmpty(createTimeTo)) {
        sql.append(sqlTimeFrom);
        sql.append(sqlTimeTo);
      }
      sql.append(
          " union all select '2' key, count(wo_id) quantitySucc from wo where ft_id = :ftId and status = 2 and ft_id is not null");
      if (StringUtils.isNotNullOrEmpty(createTimeFrom) && StringUtils
          .isNotNullOrEmpty(createTimeTo)) {
        sql.append(sqlTimeFrom);
        sql.append(sqlTimeTo);
      }
      sql.append(
          " union all  select '4' key, count(wo_id) quantitySucc from wo where ft_id = :ftId and status = 4");
      if (StringUtils.isNotNullOrEmpty(createTimeFrom) && StringUtils
          .isNotNullOrEmpty(createTimeTo)) {
        sql.append(sqlTimeFrom);
        sql.append(sqlTimeTo);
      }
      sql.append(
          " union all select '5' key, count(wo_id) quantitySucc from wo where ft_id = :ftId and status = 5");
      if (StringUtils.isNotNullOrEmpty(createTimeFrom) && StringUtils
          .isNotNullOrEmpty(createTimeTo)) {
        sql.append(sqlTimeFrom);
        sql.append(sqlTimeTo);
      }
      sql.append(
          " union all select '101' key, count(wo_id) quantitySucc from wo where ft_id = :ftId and status <> 8 and status <> 2 and status <> 6 and status <> 7 and end_time >= sysdate");
      if (StringUtils.isNotNullOrEmpty(createTimeFrom) && StringUtils
          .isNotNullOrEmpty(createTimeTo)) {
        sql.append(sqlTimeFrom);
        sql.append(sqlTimeTo);
      }
      sql.append(
          " union all select '100' key, count(wo_id) quantitySucc from wo where ft_id = :ftId and status <> 8 and status <> 2 and status <> 6 and status <> 7 and end_time < sysdate");
      if (StringUtils.isNotNullOrEmpty(createTimeFrom) && StringUtils
          .isNotNullOrEmpty(createTimeTo)) {
        sql.append(sqlTimeFrom);
        sql.append(sqlTimeTo);
      }
      sql.append(
          " union all select '6' key, count(wo_id) quantitySucc from wo where ft_id = :ftId and status = 6");
      if (StringUtils.isNotNullOrEmpty(createTimeFrom) && StringUtils
          .isNotNullOrEmpty(createTimeTo)) {
        sql.append(sqlTimeFrom);
        sql.append(sqlTimeTo);
      }
      sql.append(
          " union all select '9' key, count(wo_id) quantitySucc from wo where ft_id = :ftId and status = 9");
      if (StringUtils.isNotNullOrEmpty(createTimeFrom) && StringUtils
          .isNotNullOrEmpty(createTimeTo)) {
        sql.append(sqlTimeFrom);
        sql.append(sqlTimeTo);
      }
      // khach hang doanh nghiep
      sql.append(
          " union all select  '105' key , count(a.wo_id) quantitySucc from wo a, wfm.wo_detail d where a.wo_id = d.wo_id"
              + " and ft_id = :ftId and status in (3,4,5,9) and d.CUSTOMER_GROUP_TYPE = '2'");
      if (StringUtils.isNotNullOrEmpty(createTimeFrom) && StringUtils
          .isNotNullOrEmpty(createTimeTo)) {
        sql.append(sqlTimeFrom);
        sql.append(sqlTimeTo);
      }
    } else if (typeSearch == Constants.WO_TYPE_SEARCH.IS_CD
        || typeSearch == Constants.WO_TYPE_SEARCH.IS_PROVINCE) {
      // cho CD tiep nhan
      sql.append(
          " select '0' key, count(wo_id) quantitySucc from wo where cd_id in (:lstCd) and status = 0");
      if (StringUtils.isNotNullOrEmpty(createTimeFrom) && StringUtils
          .isNotNullOrEmpty(createTimeTo)) {
        sql.append(sqlTimeFrom);
        sql.append(sqlTimeTo);
      }
      // CD da tiep nhan
      sql.append(
          " union all select '1' key, count(wo_id) quantitySucc from wo where cd_id in (:lstCd) and status = 1");
      if (StringUtils.isNotNullOrEmpty(createTimeFrom) && StringUtils
          .isNotNullOrEmpty(createTimeTo)) {
        sql.append(sqlTimeFrom);
        sql.append(sqlTimeTo);
      }
      // FT tu choi
      sql.append(
          " union all select '2' key, count(wo_id) quantitySucc from wo where cd_id in (:lstCd) and status = 2 and ft_id is not null");
      if (StringUtils.isNotNullOrEmpty(createTimeFrom) && StringUtils
          .isNotNullOrEmpty(createTimeTo)) {
        sql.append(sqlTimeFrom);
        sql.append(sqlTimeTo);
      }
      sql.append(
          " union all select '5' key, count(wo_id) quantitySucc from wo where cd_id in (:lstCd) and status = 5");
      if (StringUtils.isNotNullOrEmpty(createTimeFrom) && StringUtils
          .isNotNullOrEmpty(createTimeTo)) {
        sql.append(sqlTimeFrom);
        sql.append(sqlTimeTo);
      }
      // Cho CD phe duyet
      sql.append(
          " union all select '9' key, count(wo_id) quantitySucc from wo where cd_id in (:lstCd) and status = 9");
      if (StringUtils.isNotNullOrEmpty(createTimeFrom) && StringUtils
          .isNotNullOrEmpty(createTimeTo)) {
        sql.append(sqlTimeFrom);
        sql.append(sqlTimeTo);
      }
      sql.append(
          " union all select '6' key, count(wo_id) quantitySucc from wo where cd_id in (:lstCd) and status = 6");
      if (StringUtils.isNotNullOrEmpty(createTimeFrom) && StringUtils
          .isNotNullOrEmpty(createTimeTo)) {
        sql.append(sqlTimeFrom);
        sql.append(sqlTimeTo);
      }
      sql.append(
          " union all select '101' key, count(wo_id) quantitySucc from wo where cd_id in (:lstCd) and status <> 8 and status <> 7 and end_time >= sysdate and (case when status = 2 then case when ft_id is not null then 1 else 0 end else 1 end) = 1");
      if (StringUtils.isNotNullOrEmpty(createTimeFrom) && StringUtils
          .isNotNullOrEmpty(createTimeTo)) {
        sql.append(sqlTimeFrom);
        sql.append(sqlTimeTo);
      }
      sql.append(
          " union all  select '100' key, count(wo_id) quantitySucc from wo where cd_id in (:lstCd) and status <> 8 and status <> 7 and end_time < sysdate and (case when status = 2 then case when ft_id is not null then 1 else 0 end else 1 end) = 1");
      if (StringUtils.isNotNullOrEmpty(createTimeFrom) && StringUtils
          .isNotNullOrEmpty(createTimeTo)) {
        sql.append(sqlTimeFrom);
        sql.append(sqlTimeTo);
      }
    }
    String sqlUser = "select u.user_id userId from common_gnoc.users u where lower(u.username) = :username";
    Map<String, Object> paramUser = new HashMap<>();
    paramUser.put("username", username.toLowerCase());
    List<UsersInsideDto> lstUser = getNamedParameterJdbcTemplate()
        .query(sqlUser, paramUser, BeanPropertyRowMapper.newInstance(UsersInsideDto.class));
    Long userId = null;
    if (lstUser != null && lstUser.size() > 0) {
      userId = lstUser.get(0).getUserId();
    }
    if (userId != null) {
      String sqlCd = "select a.wo_group_id from wfm.wo_cd a where a.user_id = :userId";
      Map<String, Object> paramCd = new HashMap<>();
      paramCd.put("userId", userId);
      List<WoCdDTO> lstCdDTO = getNamedParameterJdbcTemplate()
          .query(sqlCd, paramCd, BeanPropertyRowMapper.newInstance(WoCdDTO.class));
      List<Long> lstCd = null;
      if (lstCdDTO != null && lstCdDTO.size() > 0) {
        lstCd = new ArrayList<>();
        for (WoCdDTO woCdDTO : lstCdDTO) {
          lstCd.add(woCdDTO.getWoGroupId());
        }
      }
      if (StringUtils.isNotNullOrEmpty(createTimeFrom) && StringUtils
          .isNotNullOrEmpty(createTimeTo)) {
        parameters.put("createTimeFrom", createTimeFrom);
        parameters.put("createTimeTo", createTimeTo);
      }
      if (typeSearch == Constants.WO_TYPE_SEARCH.IS_CD) {
        if (lstCd == null || lstCd.size() == 0) {
          lstCd = new ArrayList<>();
          lstCd.add(-1000L);
        }
        parameters.put("lstCd", lstCd);
      } else if (typeSearch == Constants.WO_TYPE_SEARCH.IS_FT) {
        parameters.put("ftId", userId);
      } else if (typeSearch == Constants.WO_TYPE_SEARCH.IS_PROVINCE) {
        lstCd = new ArrayList<>();
        if (cdId != null) {
          lstCd.add(cdId);
        } else {
          List<WoCdGroupInsideDTO> lst = getListCdGroup(username);
          if (lst != null && lst.size() > 0) {
            for (WoCdGroupInsideDTO i : lst) {
              lstCd.add(i.getWoGroupId());
            }
          }
        }
        if (lstCd.isEmpty()) {
          lstCd.add(-1000L);
        }
        parameters.put("lstCd", lstCd);
      }
      return getNamedParameterJdbcTemplate()
          .query(sql.toString(), parameters, BeanPropertyRowMapper.newInstance(ResultDTO.class));
    }
    return lstResult;
  }

  @Override
  public WoInsideDTO getWoByAmiOneId(String amiOneId) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_WO, "get-Wo-By-Ami-One-Id");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("amiOneId", amiOneId);
    List<WoInsideDTO> lst = getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(WoInsideDTO.class));
    if (lst != null && lst.size() > 0) {
      return lst.get(0);
    }
    return null;
  }

  public List<WoCdGroupInsideDTO> getListCdGroup(String userName) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_WO_CD_GROUP, "get-List-Cd-Group");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("userName", userName);
    return getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(WoCdGroupInsideDTO.class));
  }

  @Override
  public List<WoDTO> getListWOAndAccount(String username, String fromDate, String toDate,
      String woCode, String cdId, String accountIsdn) {
    try {
      Map<String, Object> parameters = new HashMap<>();
      String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_WO, "get-list-wo-and-account");
      if (!StringUtils.isStringNullOrEmpty(username)) {
        sql = sql + " and u.username =:username ";
        parameters.put("username", username);
      }
      if (!StringUtils.isStringNullOrEmpty(fromDate)) {
        sql = sql + " and a.create_date >= to_date(:fromDate, 'dd/MM/YYYY HH24:mi:ss')";
        parameters.put("fromDate", fromDate);
      }
      if (!StringUtils.isStringNullOrEmpty(toDate)) {
        sql = sql + " and a.create_date <= to_date(:toDate, 'dd/MM/YYYY HH24:mi:ss')";
        parameters.put("toDate", toDate);
      }
      if (!StringUtils.isStringNullOrEmpty(woCode)) {
        sql = sql + " and lower(a.wo_code) like :woCode escape '\\' ";
        parameters.put("woCode", StringUtils.convertLowerParamContains(woCode));
      }
      if (!StringUtils.isStringNullOrEmpty(cdId)) {
        sql = sql + " and a.cd_id =:cdId ";
        parameters.put("cdId", cdId);
      }
      if (!StringUtils.isStringNullOrEmpty(accountIsdn)) {
        sql = sql + " and d.account_isdn like :accountIsdn escape '\\' ";
        parameters.put("accountIsdn", StringUtils.convertLowerParamContains(accountIsdn));
      }

      return getNamedParameterJdbcTemplate()
          .query(sql, parameters,
              BeanPropertyRowMapper.newInstance(WoDTO.class));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

  @Override
  public WoTypeServiceInsideDTO getTypeService(Long woTypeId, Long serviceId) {
    try {
      if (!StringUtils.isLongNullOrEmpty(woTypeId)
          && !StringUtils.isLongNullOrEmpty(serviceId)) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> parameters = new HashMap<>();
        sql.append(
            "select is_check_qos_internet isCheckQosInternet, is_check_qos_th isCheckQosTh, is_check_qr_code isCheckQrCode");
        sql.append(
            " from wo_type_service where wo_type_id = :woTypeId and service_id = :serviceId");
        parameters.put("woTypeId", woTypeId);
        parameters.put("serviceId", serviceId);
        List<WoTypeServiceInsideDTO> lst = getNamedParameterJdbcTemplate()
            .query(sql.toString(), parameters,
                BeanPropertyRowMapper.newInstance(WoTypeServiceInsideDTO.class));
        return !lst.isEmpty() ? lst.get(0) : null;
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

  @Override
  public String getProDuctCodeNotCheckQrCode() {
    try {
      Map<String, Object> parameters = new HashMap<>();
      String sql = "select item_value itemValue from common_gnoc.cat_item where item_code = :itemCode and status = 1";
      parameters.put("itemCode", Constants.SERVICE.PRODUCT_NOT_CHECK_QR);
      List<CatItemDTO> lst = getNamedParameterJdbcTemplate()
          .query(sql, parameters, BeanPropertyRowMapper.newInstance(CatItemDTO.class));
      if (lst != null && !lst.isEmpty()) {
        return lst.get(0).getItemValue();
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

  @Override
  public WoInsideDTO getWoByWoSystemCode(String woSystemId) {
    if (StringUtils.isNotNullOrEmpty(woSystemId)) {
      Map<String, Object> parameters = new HashMap<>();
      String sql = "SELECT * FROM wfm.wo WHERE lower(wo_system_id) = :woSystemId ORDER BY wo_id DESC";
      parameters.put("woSystemId", woSystemId.toLowerCase());
      List<WoInsideDTO> lst = getNamedParameterJdbcTemplate()
          .query(sql, parameters, BeanPropertyRowMapper.newInstance(WoInsideDTO.class));
      if (lst != null && !lst.isEmpty()) {
        return lst.get(0);
      }
    }
    return null;
  }

  @Override
  public List<WoInsideDTO> getListWoByWoCode(List<String> lstWoCode) {
    String sql = "SELECT w.* FROM WO w WHERE w.WO_CODE IN (:lstWoCode)";
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("lstWoCode", lstWoCode);
    return getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(WoInsideDTO.class));
  }

  @Override
  public List<WoInsideDTO> getFullWoByWoSystemCode(String woSystemId) {
    String sql = "SELECT w.* FROM WO w WHERE w.WO_SYSTEM_ID = :woSystemId";
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("woSystemId", woSystemId);
    return getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(WoInsideDTO.class));
  }

  @Override
  public List<WoDTOSearch> getListDataSearch(WoDTOSearch searchDto, Boolean isCount) {
    BaseDto baseDto = buildQueryListDataSearch(searchDto, isCount);
    Datatable result = getListDataTableBySqlQuery(baseDto.getSqlQuery(), baseDto.getParameters(),
        searchDto.getPage(), searchDto.getPageSize(),
        WoDTOSearch.class, searchDto.getSortName(), searchDto.getSortType());
    return (List<WoDTOSearch>) result.getData();
  }

  @Override
  public List<ResultDTO> getWOStatistic(Long unitId, int isSend, int isSearchChild, String fromDate,
      String toDate) throws Exception {
    List<ResultDTO> list;
    StringBuilder str = new StringBuilder();
    if (isSearchChild == 1) {
      str.append(
          " with tbl_user as (select s.user_id from common_gnoc.users s where s.unit_id in ");
      str.append(" (select unit_id from common_gnoc.unit u where level < 50");
      str.append(" start with u.unit_id = :unitId ");
      str.append(" connect by prior unit_id = parent_unit_id))");
    } else {
      str.append(
          " with tbl_user as (select s.user_id from common_gnoc.users s where s.unit_id  = :unitId ) ");
    }

    str.append(" , tbl_wo as (select w.wo_id, w.status, t.priority_code, w.summary_status");
    str.append(" , w.result, w.end_time, w.finish_time,w.completed_time from wo w, wo_priority t ");
    str.append(" where w.priority_id = t.priority_id ");

    if (isSend == 1) {
      str.append(" and w.create_person_id in (select user_id from tbl_user ) ");
    } else {
      str.append(
          " and (w.cd_id in (select distinct(wo_group_id) from wo_cd where user_id in (select user_id from tbl_user ))");
      str.append(" or w.ft_id in (select user_id from tbl_user ))");
    }

    str.append(" and w.create_date > :fromDate and w.create_date < :toDate)");
    //
    // ----- Cho CD tiep nhan trong han ---- //
    str.append(" select 1 as id, a.priority_code key, count(a.wo_id) quantitySucc from tbl_wo a ");
    str.append(" where a.status = 0 and a.end_time >= sysdate ");
    str.append(" group by a.priority_code ");
    //
    // ----- Cho CD tiep nhan qua han ---- //
    str.append(" UNION ALL");
    str.append(" select 2 as id, a.priority_code key, count(a.wo_id) quantitySucc from tbl_wo a ");
    str.append(" where a.status = 0 and a.end_time < sysdate");
    str.append(" group by a.priority_code");
    //
    // ----- Cho FT tiep nhan trong han ---- //
    str.append(" UNION ALL");
    str.append(" select 3 as id, a.priority_code key, count(a.wo_id) quantitySucc from tbl_wo a ");
    str.append(" where a.status = 3 and a.end_time >= sysdate ");
    str.append(" group by a.priority_code");
    //
    // ----- Cho FT tiep nhan qua han ---- //
    str.append(" UNION ALL");
    str.append(" select 4 as id, a.priority_code key, count(a.wo_id) quantitySucc from tbl_wo a ");
    str.append(" where a.status = 3 and a.end_time < sysdate");
    str.append(" group by a.priority_code");
    //
    // ----- Cho FT hoan thanh trong han ---- //
    str.append(" UNION ALL");
    str.append(" select 5 as id, a.priority_code key, count(a.wo_id) quantitySucc from tbl_wo a ");
    str.append(" where (a.status in (4,5,9) and a.end_time >= sysdate) ");
    str.append(" group by a.priority_code");
    //
    // ----- Cho FT hoan thanh qua han ---- //
    str.append(" UNION ALL");
    str.append(" select 6 as id, a.priority_code key, count(a.wo_id) quantitySucc from tbl_wo a ");
    str.append(" where (a.status in (4,5,9) and a.end_time < sysdate) ");
    str.append(" group by a.priority_code");
    //
    // ----- Cho CD phe duyet ket qua trong han ---- //
    str.append(" UNION ALL");
    str.append(" select 7 as id, a.priority_code key, count(a.wo_id) quantitySucc from tbl_wo a ");
    str.append(" where a.status = 6 and a.end_time >= sysdate ");
    str.append(" group by a.priority_code");
    //
    // ----- Cho CD phe duyet ket qua qua han ---- //
    str.append(" UNION ALL");
    str.append(" select 8 as id, a.priority_code key, count(a.wo_id) quantitySucc from tbl_wo a ");
    str.append(" where a.status = 6 and a.end_time < sysdate ");
    str.append(" group by a.priority_code");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("unitId", unitId);
    parameters.put("fromDate", DateTimeUtils.convertStringToDate(fromDate));
    parameters.put("toDate", DateTimeUtils.convertStringToDate(toDate));
    list = getNamedParameterJdbcTemplate()
        .query(str.toString(), parameters, BeanPropertyRowMapper.newInstance(ResultDTO.class));
    for (int type = 1; type <= 8; type++) {
      for (int priority = 1; priority <= 4; priority++) {
        ResultDTO news = new ResultDTO();
        news.setId(String.valueOf(type));
        news.setKey(String.valueOf(priority));
        news.setQuantitySucc(0);
        if (!list.contains(news)) {
          list.add(news);
        }

      }
    }
    return list;
  }

  @Override
  public Integer getWOTotal(WoDTOSearch searchDtoInput) {
    BaseDto baseDto = buildQueryListDataSearch(searchDtoInput, true);
    List<BaseDto> list = getNamedParameterJdbcTemplate()
        .query(baseDto.getSqlQuery(), baseDto.getParameters(),
            BeanPropertyRowMapper.newInstance(BaseDto.class));
    return list == null || list.isEmpty() ? 0 : list.get(0).getTotalRow();
  }

  @Override
  public List<WoInsideDTO> getListWoChildByParentId(Long parentId) {
    List<WoEntity> listEntity = findByMultilParam(WoEntity.class, "parentId", parentId);
    List<WoInsideDTO> listDTO = new ArrayList<>();
    if (listEntity != null && listEntity.size() > 0) {
      for (WoEntity entity : listEntity) {
        listDTO.add(entity.toDTO());
      }
      return listDTO;
    }
    return null;
  }

  @Override
  public List<WoInsideDTO> getListDataForRisk(WoInsideDTO woInsideDTO) {
    BaseDto baseDto = sqlGetListDataSearchWeb(woInsideDTO);
    return getNamedParameterJdbcTemplate().query(baseDto.getSqlQuery(), baseDto.getParameters(),
        BeanPropertyRowMapper.newInstance(WoInsideDTO.class));
  }

  @Override
  public List<Long> getListWoDTOByUserId(Long userId) {
    if (userId != null) {
      String sql = "select a.wo_group_id from wfm.wo_cd a where a.user_id = :userId";
      Query query = getEntityManager().createNativeQuery(sql);
      query.setParameter("userId", userId);
      return query.getResultList();
    }
    return null;
  }

  public BaseDto buildQueryListDataSearch(WoDTOSearch searchDto, Boolean isCount) {
    String sql;
    BaseDto baseDto = new BaseDto();
    if (isCount) {
      sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_WO, "count-get-List-Data-Search");
    } else {
      sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_WO, "get-List-Data-Search");
    }
    List<Long> lstStatus = new ArrayList<>();
    Double offset =
        isCount ? null : userRepository.getOffsetFromUser(Long.parseLong(searchDto.getUserId()));
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("offset", offset);
    parameters.put("unassigned", I18n.getLanguage("wo.status.UNASSIGNED"));
    parameters.put("assigned", I18n.getLanguage("wo.status.ASSIGNED"));
    parameters.put("reject", I18n.getLanguage("wo.status.REJECT"));
    parameters.put("dispatch", I18n.getLanguage("wo.status.DISPATCH"));
    parameters.put("accept", I18n.getLanguage("wo.status.ACCEPT"));
    parameters.put("inProcess", I18n.getLanguage("wo.status.INPROCESS"));
    parameters.put("pending", I18n.getLanguage("wo.status.PENDING"));
    parameters.put("closedCd", I18n.getLanguage("wo.status.CLOSED_CD"));
    parameters.put("closedFt", I18n.getLanguage("wo.status.CLOSED_FT"));
    parameters.put("draft", I18n.getLanguage("wo.status.DRAFT"));
    // Tim theo he thong
    if (StringUtils.isNotNullOrEmpty(searchDto.getWoSystem())) {
      sql += " AND w.wo_system = :woSystem";
      parameters.put("woSystem", searchDto.getWoSystem());
    }
    // Tim theo ma he thong
    if (StringUtils.isNotNullOrEmpty(searchDto.getWoSystemId())) {
      sql += " AND LOWER(w.wo_system_id) = :woSystemId";
      parameters.put("woSystemId", searchDto.getWoSystemId().toLowerCase());
    }
    // Ma cong viec
    if (StringUtils.isNotNullOrEmpty(searchDto.getWoCode())) {
      sql += " AND LOWER(w.wo_code) LIKE :woCode ESCAPE '\\'";
      parameters.put("woCode", StringUtils.convertLowerParamContains(searchDto.getWoCode()));
    }
    if (searchDto.getCdId() != null) {
      sql += " AND w.cd_id = :cdId";
      parameters.put("cdId", searchDto.getCdId());
    }
    // Muc do uu tien
    if (searchDto.getPriorityId() != null) {
      sql += " AND w.priority_id = :priorityId";
      parameters.put("priorityId", searchDto.getPriorityId());
    }
    // Loai cong viec
    if (searchDto.getWoTypeId() != null) {
      sql += " AND w.wo_type_id = :woTypeId";
      parameters.put("woTypeId", searchDto.getWoTypeId());
    }
    // Noi dung cong viec
    if (StringUtils.isNotNullOrEmpty(searchDto.getWoContent())) {
      sql += " AND LOWER(w.wo_content) LIKE :woContent ESCAPE '\\'";
      parameters.put("woContent", StringUtils.convertLowerParamContains(searchDto.getWoContent()));
    }
    // Trang thai hoan thanh
    if (StringUtils.isNotNullOrEmpty(searchDto.getResult())) {
      if ("3".equals(String.valueOf(searchDto.getResult()))) {
        sql += " AND w.result is null";
      } else {
        sql += " AND w.result = :result";
        parameters.put("result", searchDto.getResult());
      }
    }
    // Thoi gian khoi tao
    if (StringUtils.isNotNullOrEmpty(searchDto.getStartTimeFrom())) {
      sql += " AND w.create_date >= TO_TIMESTAMP(:startTimeFrom,'dd/mm/yyyy hh24:mi:ss')";
      parameters.put("startTimeFrom", searchDto.getStartTimeFrom());
    }
    if (StringUtils.isNotNullOrEmpty(searchDto.getStartTimeTo())) {
      sql += " AND w.create_date <= TO_TIMESTAMP(:startTimeTo,'dd/mm/yyyy hh24:mi:ss')";
      parameters.put("startTimeTo", searchDto.getStartTimeTo());
    }
    // Thoi gian thuc hien
    if (StringUtils.isNotNullOrEmpty(searchDto.getEndTimeFrom())) {
      sql += " AND w.start_time >= TO_TIMESTAMP(:endTimeFrom,'dd/mm/yyyy hh24:mi:ss')";
      parameters.put("endTimeFrom", searchDto.getEndTimeFrom());
    }
    if (StringUtils.isNotNullOrEmpty(searchDto.getEndTimeTo())) {
      sql += " AND w.start_time <= TO_TIMESTAMP(:endTimeTo,'dd/mm/yyyy hh24:mi:ss')";
      parameters.put("endTimeTo", searchDto.getEndTimeTo());
    }

    if (StringUtils.isNotNullOrEmpty(searchDto.getCompleteTimeFrom())) {
      sql += " AND (w.finish_time >= TO_TIMESTAMP(:completeTimeFrom, 'dd/mm/yyyy hh24:mi:ss') and w.result is not null)";
      parameters.put("completeTimeFrom", searchDto.getCompleteTimeFrom());
    }

    if (StringUtils.isNotNullOrEmpty(searchDto.getCompleteTimeTo())) {
//            sql.append(" AND exists (select wh.wo_id from wo_history wh "
//                    + " where wh.update_time <= TO_TIMESTAMP(?,'dd/mm/yyyy hh24:mi:ss') "
//                    + " and wh.new_status = 6 and wh.wo_id = w.wo_id)");
      sql += " AND (w.finish_time <= TO_TIMESTAMP(:completeTimeTo ,'dd/mm/yyyy hh24:mi:ss') and w.result is not null)";
      parameters.put("completeTimeTo", searchDto.getCompleteTimeTo());
    }

    if (StringUtils.isNotNullOrEmpty(searchDto.getWoId())) {
      sql += " AND w.wo_id = :woId";
      parameters.put("woId", searchDto.getWoId());
    }
    if (StringUtils.isNotNullOrEmpty(searchDto.getParentId())) {
      sql += " AND w.parent_id = :parentId";
      parameters.put("parentId", searchDto.getParentId());
    }
    // Nhan vien tao
    if (StringUtils.isNotNullOrEmpty(searchDto.getCreatePersonName())) {
      sql += " AND LOWER(cp.username) LIKE :createPersonName ESCAPE '\\'";
      parameters.put("createPersonName",
          StringUtils.convertLowerParamContains(searchDto.getCreatePersonName()));
    }
    // Nhan vien thuc hien
    if (StringUtils.isNotNullOrEmpty(searchDto.getFtName())) {
      sql += " AND LOWER(f.username) LIKE :ftName ESCAPE '\\'";
      parameters.put("ftName", StringUtils.convertLowerParamContains(searchDto.getFtName()));
    }
    // Cong viec cha
    if (StringUtils.isNotNullOrEmpty(searchDto.getParentName())) {
      sql += " AND LOWER(p.wo_code) LIKE :parentName ESCAPE '\\'";
      parameters
          .put("parentName", StringUtils.convertLowerParamContains(searchDto.getParentName()));
    }
    // Thue bao
    if (StringUtils.isNotNullOrEmpty(searchDto.getAccountIsdn())) {
      sql += " AND LOWER(wd.account_isdn) LIKE :accountIsdn ESCAPE '\\'";
      parameters
          .put("accountIsdn", StringUtils.convertLowerParamContains(searchDto.getAccountIsdn()));
    }
    // Tim theo don vi
    if (searchDto.getIsContainChildUnit() != null && searchDto.getIsContainChildUnit()) {
      if (StringUtils.isNotNullOrEmpty(searchDto.getCreateUnitId())) {
        sql += " AND w.create_person_id in ("
            + "select user_id from common_gnoc.users where unit_id in"
            + " (select unit_id from common_gnoc.unit where level < 50 and status = 1"
            + " start with unit_id = :createUnitId"
            + " connect by prior unit_id = parent_unit_id))";
        parameters.put("createUnitId", searchDto.getCreateUnitId());
      }
      if (StringUtils.isNotNullOrEmpty(searchDto.getProcessUnitId())) {
        sql += " AND (w.ft_id in ("
            + "select user_id from common_gnoc.users where unit_id in"
            + " (select unit_id from common_gnoc.unit where level < 50 and status = 1"
            + " start with unit_id = :processUnitId"
            + " connect by prior unit_id = parent_unit_id)))";
        parameters.put("processUnitId", searchDto.getProcessUnitId());
      }
    } else {
      if (StringUtils.isNotNullOrEmpty(searchDto.getCreateUnitId())) {
        sql += " AND w.create_person_id in "
            + "(select user_id from common_gnoc.users where unit_id = :createUnitId)";
        parameters.put("createUnitId", searchDto.getCreateUnitId());
      }
      if (StringUtils.isNotNullOrEmpty(searchDto.getProcessUnitId())) {
        sql += " AND (w.ft_id in "
            + "(select user_id from common_gnoc.users where unit_id = :processUnitId))";
        parameters.put("processUnitId", searchDto.getProcessUnitId());
      }
    }
    if (StringUtils.isNotNullOrEmpty(searchDto.getUserId())) {
      sql += " and (";
      Boolean isHasCondition = false;
      if (searchDto.isIsCreated() != null && searchDto.isIsCreated()) {
        sql += isHasCondition ? " or w.create_person_id = :userIdCreated"
            : " w.create_person_id = :userIdCreated";
        isHasCondition = true;
        parameters.put("userIdCreated", searchDto.getUserId());
      }
      if (searchDto.isIsCd() != null && searchDto.isIsCd()) {
        sql += isHasCondition
            ? " or w.cd_id in (select wo_group_id from wo_cd where user_id = :userIdCd)"
            : "  w.cd_id in (select wo_group_id from wo_cd where user_id = :userIdCd)";
        isHasCondition = true;
        parameters.put("userIdCd", searchDto.getUserId());
      }
      if (searchDto.isIsFt() != null && searchDto.isIsFt()) {
        sql += isHasCondition ? " or w.ft_id = :userIdFt" : "  w.ft_id = :userIdFt";
        isHasCondition = true;
        parameters.put("userIdFt", searchDto.getUserId());
      }
      if (!isHasCondition) {
        sql += "1 = 1";
      }
      sql += ")";
    }

    //day list xuong cuoi cung de ko bi sai lech thu tu
    if (!StringUtils.isStringNullOrEmpty(searchDto.getStatus())) {
      if ("-1".equals(searchDto.getStatus())) {//Chua hoan thanh - Cho CD phe duyet
        sql += " AND w.status <> 7 and w.status <> 8 and ( w.status <> 6 or (w.status = 6 AND w.result is null))";
      }//ducdm1_them trang thai hoan thanh_start
      else if ("6".equals(searchDto.getStatus())) {//Da hoan thanh, CD chua phe duyet
        sql += "  AND w.status = 6 AND w.result is null AND w.finish_time is null ";
      } else if (searchDto.getStatus().contains(",")) {
        if (searchDto.getStatus().contains("6")) {
          sql += " AND (w.status in (:lstStatus) or (w.status = 6 AND w.result is null AND w.finish_time is null )) ";
        } else {
          sql += " AND w.status in (:lstStatus) ";
        }
        List<String> myList = new ArrayList<>(Arrays.asList(searchDto.getStatus().split(",")));
        for (String tmp : myList) {
          if ("10".equals(tmp.trim())) {
            tmp = "2";
          }
          lstStatus.add(Long.parseLong(tmp.trim()));
        }
        parameters.put("lstStatus", lstStatus);
      } //ducdm1_them trang thai hoan thanh_end
      //chi chon trang thai dong
      else if ("8".equals(searchDto.getStatus())) {
        sql += " AND ((w.status = 8 or w.status = 6) AND w.result is not null) ";
      } else if ("10".equals(searchDto.getStatus())) {  // ft tu choi
        sql += " and ( case when w.status = 2 and w.ft_id is null then 1 else 0 end )=1 ";
      } else if ("2".equals(searchDto.getStatus())) {  // cd tu choi
        sql += " and ( case when w.status = 2 and w.ft_id is not null then 1 else 0 end )=1 ";
      } else {
        sql += " AND w.status = :status  ";
        parameters.put("status", searchDto.getStatus());
      }
    }
    if (!StringUtils.isStringNullOrEmpty(searchDto.getStatus()) && searchDto.getStatus()
        .contains("10") && searchDto.getStatus().contains("2")) {
      sql += " ";
    } else if (StringUtils.isStringNullOrEmpty(searchDto.getStatus()) || !searchDto.getStatus()
        .contains("10")) { // cd tu choi
      sql += " and ( case when w.status = 2 and w.ft_id is null then 0 else 1 end )=1 ";
    } else if (!StringUtils.isStringNullOrEmpty(searchDto.getStatus()) && searchDto.getStatus()
        .contains("10")) {
      sql += " and ( case when w.status = 2 and w.ft_id is not null then 0 else 1 end )=1 ";
    }

    if (!isCount) {
      sql += " ORDER BY w.create_date DESC ";
    }

    baseDto.setSqlQuery(sql);
    baseDto.setParameters(parameters);
    return baseDto;
  }

  @Override
  public List<ResultDTO> getWOSummaryInfobyUser2(String username, int typeSearch, Long cdId,
      String createTimeFrom, String createTimeTo) {
    List<ResultDTO> list = new ArrayList<>();
    StringBuilder str = new StringBuilder();
    Map<String, Object> parameters = new HashMap<>();
    if (typeSearch == Constants.WO_TYPE_SEARCH.IS_FT) {

      // giao viec den
      str.append(
          " select  '3' key , count(wo_id) quantitySucc from wo where ft_id = :ftId and status = 3");
      if (!StringUtils.isStringNullOrEmpty(createTimeFrom) && !StringUtils
          .isStringNullOrEmpty(createTimeTo)) {
        str.append(" and CREATE_DATE >= TO_DATE(:createTimeFrom,'dd/MM/yyyy HH24:mi:ss') ");
        str.append(" and CREATE_DATE <= TO_DATE(:createTimeTo,'dd/MM/yyyy HH24:mi:ss') ");
      }

      // khach hang doanh nghiep
      str.append(
          " union all select  '105' key , count(a.wo_id) quantitySucc from wo a, wfm.wo_detail d where a.wo_id = d.wo_id "
              + " and ft_id = :ftId and status in (3,4,5,9) and d.CUSTOMER_GROUP_TYPE = '2' ");
      if (!StringUtils.isStringNullOrEmpty(createTimeFrom) && !StringUtils
          .isStringNullOrEmpty(createTimeTo)) {
        str.append(" and a.CREATE_DATE >= TO_DATE(:createTimeFrom,'dd/MM/yyyy HH24:mi:ss') ");
        str.append(" and a.CREATE_DATE <= TO_DATE(:createTimeTo,'dd/MM/yyyy HH24:mi:ss') ");
      }

      // dang xu ly
      str.append(
          " union all  select '5' key , count(wo_id) quantitySucc from wo where ft_id = :ftId  and status in (4,5) ");
      if (!StringUtils.isStringNullOrEmpty(createTimeFrom) && !StringUtils
          .isStringNullOrEmpty(createTimeTo)) {
        str.append(" and CREATE_DATE >= TO_DATE(:createTimeFrom,'dd/MM/yyyy HH24:mi:ss') ");
        str.append(" and CREATE_DATE <= TO_DATE(:createTimeTo,'dd/MM/yyyy HH24:mi:ss') ");
      }
      //hot/Vip
      str.append(
          " union all  select '102' key , count(w.wo_id) quantitySucc from wo w,wfm.wo_priority p"
              + " where w.ft_id = :ftId  and w.status in (3,4,5,9) and w.priority_id = p.priority_id "
              + " and p.PRIORITY_NAME in ('Nghiêm trọng','Rất nghiêm trọng','Hot')"
              + " and w.wo_system in ('TT','SPM') ");
      if (!StringUtils.isStringNullOrEmpty(createTimeFrom) && !StringUtils
          .isStringNullOrEmpty(createTimeTo)) {
        str.append(" and w.CREATE_DATE >= TO_DATE(:createTimeFrom,'dd/MM/yyyy HH24:mi:ss') ");
        str.append(" and w.CREATE_DATE <= TO_DATE(:createTimeTo,'dd/MM/yyyy HH24:mi:ss') ");
      }
      // sap qua han
      str.append(
          " union all  select '103' key , count(w.wo_id) quantitySucc from wo w,wfm.wo_type c"
              + " where w.ft_id = :ftId  "
              + " and w.status in (3,4,5,9) "
              + " and w.wo_type_id = c.wo_type_id and c.time_over is not null "
              + " and w.end_time BETWEEN sysdate and (sysdate + (c.time_over/24)) ");
      if (!StringUtils.isStringNullOrEmpty(createTimeFrom) && !StringUtils
          .isStringNullOrEmpty(createTimeTo)) {
        str.append(" and CREATE_DATE >= TO_DATE(:createTimeFrom,'dd/MM/yyyy HH24:mi:ss') ");
        str.append(" and CREATE_DATE <= TO_DATE(:createTimeTo,'dd/MM/yyyy HH24:mi:ss') ");
      }
      //qua han
      str.append(
          " union all  select '100' key , count(wo_id) quantitySucc from wo where ft_id = :ftId  and status <> 8 and status <> 2 and status <> 6 and status <> 7 and end_time < sysdate ");
      if (!StringUtils.isStringNullOrEmpty(createTimeFrom) && !StringUtils
          .isStringNullOrEmpty(createTimeTo)) {
        str.append(" and CREATE_DATE >= TO_DATE(:createTimeFrom,'dd/MM/yyyy HH24:mi:ss') ");
        str.append(" and CREATE_DATE <= TO_DATE(:createTimeTo,'dd/MM/yyyy HH24:mi:ss') ");
      }
      //ThanhLV12_bo sung tam dong va hoan thanh chua phe duyet start
      str.append(
          " union all  select '6' key , count(wo_id) quantitySucc from wo where ft_id = :ftId  and status = 6 ");
      if (!StringUtils.isStringNullOrEmpty(createTimeFrom) && !StringUtils
          .isStringNullOrEmpty(createTimeTo)) {
        str.append(" and CREATE_DATE >= TO_DATE(:createTimeFrom,'dd/MM/yyyy HH24:mi:ss') ");
        str.append(" and CREATE_DATE <= TO_DATE(:createTimeTo,'dd/MM/yyyy HH24:mi:ss') ");
      }
      // tam dung
      str.append(
          " union all  select '9' key , count(wo_id) quantitySucc from wo where ft_id = :ftId  and status = 9 ");
      if (!StringUtils.isStringNullOrEmpty(createTimeFrom) && !StringUtils
          .isStringNullOrEmpty(createTimeTo)) {
        str.append(" and CREATE_DATE >= TO_DATE(:createTimeFrom,'dd/MM/yyyy HH24:mi:ss') ");
        str.append(" and CREATE_DATE <= TO_DATE(:createTimeTo,'dd/MM/yyyy HH24:mi:ss') ");
      }
    } //ThanhLV12_end
    //ThanhLv12_bo sung trang thai cho CD_start
    else if (typeSearch == Constants.WO_TYPE_SEARCH.IS_CD
        || typeSearch == Constants.WO_TYPE_SEARCH.IS_PROVINCE) {
      // cho CD tiep nhan
      str.append(
          " select  '0' key , count(wo_id) quantitySucc from wo where cd_id in (:lstCd) and status = 0 ");
      if (!StringUtils.isStringNullOrEmpty(createTimeFrom) && !StringUtils
          .isStringNullOrEmpty(createTimeTo)) {
        str.append(" and CREATE_DATE >= TO_DATE(:createTimeFrom,'dd/MM/yyyy HH24:mi:ss') ");
        str.append(" and CREATE_DATE <= TO_DATE(:createTimeTo,'dd/MM/yyyy HH24:mi:ss') ");
      }
      // khach hang doanh nghiep
      str.append(
          " union all select  '105' key , count(a.wo_id) quantitySucc from wo a, wfm.wo_detail d where a.wo_id = d.wo_id "
              + " and cd_id in (:lstCd) "
              + " and status <> 8 and status <> 7  "
              + " and (case when status = 2 then case when ft_id is not null then 1 else 0 end else 1 end )=1 "
              + " and d.CUSTOMER_GROUP_TYPE = '2' ");
      if (!StringUtils.isStringNullOrEmpty(createTimeFrom) && !StringUtils
          .isStringNullOrEmpty(createTimeTo)) {
        str.append(" and a.CREATE_DATE >= TO_DATE(:createTimeFrom,'dd/MM/yyyy HH24:mi:ss') ");
        str.append(" and a.CREATE_DATE <= TO_DATE(:createTimeTo,'dd/MM/yyyy HH24:mi:ss') ");
      }

      str.append(
          " union all  select '102' key , count(w.wo_id) quantitySucc from wo w,wfm.wo_priority p"
              + " where w.priority_id = p.priority_id and w.cd_id in (:lstCd)  and w.status in (3,4,5,6,9) "
              + " and p.PRIORITY_NAME in ('Nghiêm trọng','Rất nghiêm trọng','Hot')"
              + " and w.wo_system in ('TT','SPM') ");
      if (!StringUtils.isStringNullOrEmpty(createTimeFrom) && !StringUtils
          .isStringNullOrEmpty(createTimeTo)) {
        str.append(" and w.CREATE_DATE >= TO_DATE(:createTimeFrom,'dd/MM/yyyy HH24:mi:ss') ");
        str.append(" and w.CREATE_DATE <= TO_DATE(:createTimeTo,'dd/MM/yyyy HH24:mi:ss') ");
      }
      //dang thuc hien
      str.append(
          " union all  select '5' key , count(wo_id) quantitySucc from wo where cd_id in (:lstCd)  and status in (4,5)");
      if (!StringUtils.isStringNullOrEmpty(createTimeFrom) && !StringUtils
          .isStringNullOrEmpty(createTimeTo)) {
        str.append(" and CREATE_DATE >= TO_DATE(:createTimeFrom,'dd/MM/yyyy HH24:mi:ss') ");
        str.append(" and CREATE_DATE <= TO_DATE(:createTimeTo,'dd/MM/yyyy HH24:mi:ss') ");
      }
      // tam dung
      str.append(
          " union all  select '9' key , count(wo_id) quantitySucc from wo where cd_id in (:lstCd)  and status = 9 ");
      if (!StringUtils.isStringNullOrEmpty(createTimeFrom) && !StringUtils
          .isStringNullOrEmpty(createTimeTo)) {
        str.append(" and CREATE_DATE >= TO_DATE(:createTimeFrom,'dd/MM/yyyy HH24:mi:ss') ");
        str.append(" and CREATE_DATE <= TO_DATE(:createTimeTo,'dd/MM/yyyy HH24:mi:ss') ");
      }
      // hoan thanh
      str.append(
          " union all  select '6' key , count(wo_id) quantitySucc from wo where cd_id in (:lstCd)  and status = 6 ");
      if (!StringUtils.isStringNullOrEmpty(createTimeFrom) && !StringUtils
          .isStringNullOrEmpty(createTimeTo)) {
        str.append(" and CREATE_DATE >= TO_DATE(:createTimeFrom,'dd/MM/yyyy HH24:mi:ss') ");
        str.append(" and CREATE_DATE <= TO_DATE(:createTimeTo,'dd/MM/yyyy HH24:mi:ss') ");
      }
      // sap qua han
      str.append(
          " union all  select '103' key , count(w.wo_id) quantitySucc from wo w,wfm.wo_type c "
              + " where w.cd_id in (:lstCd)  and w.status in (3,4,5,6,9) "
              + " and w.wo_type_id = c.wo_type_id and c.time_over is not null "
              + " and w.end_time BETWEEN sysdate and (sysdate + (c.time_over/24)) ");
      if (!StringUtils.isStringNullOrEmpty(createTimeFrom) && !StringUtils
          .isStringNullOrEmpty(createTimeTo)) {
        str.append(" and CREATE_DATE >= TO_DATE(:createTimeFrom,'dd/MM/yyyy HH24:mi:ss') ");
        str.append(" and CREATE_DATE <= TO_DATE(:createTimeTo,'dd/MM/yyyy HH24:mi:ss') ");
      }
      //qua han
      str.append(
          " union all  select '100' key , count(wo_id) quantitySucc from wo where cd_id in (:lstCd)  "
              + "and status <> 8 and status <> 7 and end_time < sysdate and "
              + "(case when status = 2 then case when ft_id is not null then 1 else 0 end else 1 end )=1 ");
      if (!StringUtils.isStringNullOrEmpty(createTimeFrom) && !StringUtils
          .isStringNullOrEmpty(createTimeTo)) {
        str.append(" and CREATE_DATE >= TO_DATE(:createTimeFrom,'dd/MM/yyyy HH24:mi:ss') ");
        str.append(" and CREATE_DATE <= TO_DATE(:createTimeTo,'dd/MM/yyyy HH24:mi:ss') ");
      }
    }

    //ThanhLv12_bo sung trang thai cho CD_end
    List<UsersInsideDto> usersInsideDtos = userRepository.getListUserDTOByuserName(username);
    if (usersInsideDtos != null && usersInsideDtos.size() > 0) {
      Long id = usersInsideDtos.get(0).getUserId();
      List<Long> lstCd = getListWoDTOByUserId(id);
      if (!StringUtils.isStringNullOrEmpty(createTimeFrom) && !StringUtils
          .isStringNullOrEmpty(createTimeTo)) {
        parameters.put("createTimeFrom", createTimeFrom);
        parameters.put("createTimeTo", createTimeTo);
      }
      if (typeSearch == Constants.WO_TYPE_SEARCH.IS_CD) {
        if (lstCd == null || lstCd.size() == 0) {
          lstCd = new ArrayList<>();
          lstCd.add(-1000L);
        }
        parameters.put("lstCd", lstCd);
      } else if (typeSearch == Constants.WO_TYPE_SEARCH.IS_FT) {
        parameters.put("ftId", id);
      } else if (typeSearch == Constants.WO_TYPE_SEARCH.IS_PROVINCE) {

        lstCd = new ArrayList<>();
        if (cdId != null) {
          lstCd.add(cdId);
        } else {
          List<WoCdGroupInsideDTO> lst = getListCdGroup(username);
          if (lst != null && lst.size() > 0) {
            for (WoCdGroupInsideDTO i : lst) {
              lstCd.add(Long.valueOf(i.getWoGroupId()));
            }
          }
        }
        if (lstCd.isEmpty()) {
          lstCd.add(-1000L);
        }
        parameters.put("lstCd", lstCd);
      }
      list = getNamedParameterJdbcTemplate()
          .query(str.toString(), parameters, BeanPropertyRowMapper.newInstance(ResultDTO.class));
    }

    return list;
  }

  @Override
  public WoSearchWebDTO getWoSearchWebDTOByWoCode(String code) {
    try {
      String sql = "select WO_ID woId, status from wo where WO_CODE = :wocode";
      Map<String, String> parameters = new HashMap<>();
      parameters.put("wocode", code);
      List<WoSearchWebDTO> list = getNamedParameterJdbcTemplate()
          .query(sql, parameters, BeanPropertyRowMapper.newInstance(WoSearchWebDTO.class));
      if (list != null && list.size() > 0) {
        return list.get(0);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

  @Override
  public List<WoDTO> getListSearchWOByCondition(List<ConditionBean> lstCondition,
      int rowStart, int maxRow, String sortType, String sortFieldList) {
    return onSearchByConditionBean(new WoEntity(), lstCondition, rowStart, maxRow, sortType,
        sortFieldList);
  }

  public BaseDto sqlGetTotalDataSearchWeb(WoInsideDTO searchDto) {
    String leeLocale = I18n.getLocale();
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_WO, "get-Total-Data-Search-Web");
    BaseDto baseDto = genParam(sql, leeLocale, searchDto);
    return baseDto;
  }

//  public BaseDto sqlGetListDataSearchWebPerfomance(WoInsideDTO searchDto) {
//    String leeLocale = I18n.getLocale();
//    String sql = SQLBuilder
//        .getSqlQueryById(SQLBuilder.SQL_MODULE_WO, "get-List-Data-Search-Web-Perfomance");
//    BaseDto baseDto = genParam(sql, leeLocale, searchDto);
//    return baseDto;
//  }

  private BaseDto genParam(String sql, String leeLocale, WoInsideDTO searchDto) {
    Map<String, Object> parameters = new HashMap<>();
    List<Long> lstStatus = new ArrayList<>();
    parameters.put("leeLocale", leeLocale);
    parameters.put("offset", searchDto.getOffSetFromUser());
    parameters.put("unassigned", I18n.getLanguage("wo.status.UNASSIGNED"));
    parameters.put("assigned", I18n.getLanguage("wo.status.ASSIGNED"));
    parameters.put("reject", I18n.getLanguage("wo.status.REJECT"));
    parameters.put("rejectCd", I18n.getLanguage("wo.status.REJECT_CD"));
    parameters.put("dispatch", I18n.getLanguage("wo.status.DISPATCH"));
    parameters.put("accept", I18n.getLanguage("wo.status.ACCEPT"));
    parameters.put("inProcess", I18n.getLanguage("wo.status.INPROCESS"));
    parameters.put("pending", I18n.getLanguage("wo.status.PENDING"));
    parameters.put("closedCd", I18n.getLanguage("wo.status.CLOSED_CD"));
    parameters.put("closedFt", I18n.getLanguage("wo.status.CLOSED_FT"));
    parameters.put("draft", I18n.getLanguage("wo.status.DRAFT"));

    //Tim kiem nhanh
    if (StringUtils.isNotNullOrEmpty(searchDto.getSearchAll())) {
      sql += " AND (LOWER(w.wo_code) LIKE :searchAll ESCAPE '\\'";
      sql += " OR LOWER(w.wo_system_id) LIKE :searchAll ESCAPE '\\'";
      sql += " OR LOWER(w.station_code) LIKE :searchAll ESCAPE '\\'";
      sql += " OR LOWER(wd.account_isdn) LIKE :searchAll ESCAPE '\\')";
      parameters.put("searchAll", StringUtils.convertLowerParamContains(searchDto.getSearchAll()));
    }
    //hungtv cap nhat phe duyet gia han start
    if (Boolean.parseBoolean(searchDto.getIsApproveExtend())) {
      sql += " and w.REASON_EXTENTION IS NOT NULL";
    }
    //hungtv cap nhat phe duyet gia han end
    // Tim theo he thong
    if (StringUtils.isNotNullOrEmpty(searchDto.getWoSystem())) {
      sql += " AND w.wo_system = :woSystem";
      parameters.put("woSystem", searchDto.getWoSystem());
    }
    // Tim theo ma he thong
    if (StringUtils.isNotNullOrEmpty(searchDto.getWoSystemId())) {
      sql += " AND LOWER(w.wo_system_id) = :woSystemId";
      parameters.put("woSystemId", searchDto.getWoSystemId().toLowerCase());
    }
    // Ma cong viec
    if (StringUtils.isNotNullOrEmpty(searchDto.getWoCode())) {
      sql += " AND LOWER(w.wo_code) LIKE :woCode ESCAPE '\\'";
      parameters.put("woCode", StringUtils.convertLowerParamContains(searchDto.getWoCode()));
    }
    // Ma ke hoach
    if (StringUtils.isNotNullOrEmpty(searchDto.getPlanCode())) {
      sql += " AND LOWER(w.PLAN_CODE) LIKE :planCode ESCAPE '\\'";
      parameters.put("planCode", StringUtils.convertLowerParamContains(searchDto.getPlanCode()));
    }
//    if (searchDto.getCdId() != null) {
//      sql += " AND w.cd_id = :cdId";
//      parameters.put("cdId", searchDto.getCdId());
//    }
    // Muc do uu tien
    if (searchDto.getPriorityId() != null) {
      sql += " AND w.priority_id = :priorityId";
      parameters.put("priorityId", searchDto.getPriorityId());
    }
    // Loai cong viec
    if (searchDto.getWoTypeId() != null) {
      sql += " AND w.wo_type_id = :woTypeId";
      parameters.put("woTypeId", searchDto.getWoTypeId());
    } else // kiem tra xem co chon nhom loai cong viec khong
      if (searchDto.getWoTypeGroupId() != null) {
        if (!"-1".equals(String.valueOf(searchDto.getWoTypeGroupId()))) {
          sql += " AND w.wo_type_id in (select wo_type_id from wfm.wo_type where WO_GROUP_TYPE = :woTypeGroupId)";
          parameters.put("woTypeGroupId", searchDto.getWoTypeGroupId());
        } else {
          sql += " AND w.wo_type_id in (select wo_type_id from wfm.wo_type where WO_GROUP_TYPE is null)";
        }
      }
    // Noi dung cong viec
    if (StringUtils.isNotNullOrEmpty(searchDto.getWoContent())) {
      sql += " AND LOWER(w.wo_content) LIKE :woContent ESCAPE '\\'";
      parameters.put("woContent", StringUtils.convertLowerParamContains(searchDto.getWoContent()));
    }
    // Trang thai hoan thanh
    if (searchDto.getResult() != null) {
      if ("3".equals(String.valueOf(searchDto.getResult()))) {
        sql += " AND w.result is null";
      } else {
        sql += " AND w.result = :result";
        parameters.put("result", searchDto.getResult());
      }
    }
    // Thoi gian khoi tao
    if (searchDto.getStartTimeFrom() != null) {
      sql += " AND w.create_date >= TO_TIMESTAMP(:startTimeFrom,'dd/mm/yyyy hh24:mi:ss')";
      parameters.put("startTimeFrom", DateUtil.date2ddMMyyyyHHMMss(searchDto.getStartTimeFrom()));
    }
    if (searchDto.getStartTimeTo() != null) {
      sql += " AND w.create_date <= TO_TIMESTAMP(:startTimeTo,'dd/mm/yyyy hh24:mi:ss')";
      parameters.put("startTimeTo", DateUtil.date2ddMMyyyyHHMMss(searchDto.getStartTimeTo()));
    }
    // Thoi gian thuc hien
    if (searchDto.getStartDateFrom() != null) {
      sql += " AND w.start_time >= TO_TIMESTAMP(:startDateFrom,'dd/mm/yyyy hh24:mi:ss')";
      parameters.put("startDateFrom", DateUtil.date2ddMMyyyyHHMMss(searchDto.getStartDateFrom()));
    }
    if (searchDto.getStartDateTo() != null) {
      sql += " AND w.start_time <= TO_TIMESTAMP(:startDateTo,'dd/mm/yyyy hh24:mi:ss')";
      parameters.put("startDateTo", DateUtil.date2ddMMyyyyHHMMss(searchDto.getStartDateTo()));
    }
    // Thoi gian ket thuc
    if (searchDto.getEndTimeFrom() != null) {
      sql += " AND w.end_time >= TO_TIMESTAMP(:endTimeFrom,'dd/mm/yyyy hh24:mi:ss')";
      parameters.put("endTimeFrom", DateUtil.date2ddMMyyyyHHMMss(searchDto.getEndTimeFrom()));
    }
    if (searchDto.getEndTimeTo() != null) {
      sql += " AND w.end_time <= TO_TIMESTAMP(:endTimeTo,'dd/mm/yyyy hh24:mi:ss')";
      parameters.put("endTimeTo", DateUtil.date2ddMMyyyyHHMMss(searchDto.getEndTimeTo()));
    }
    // Thoi gian dong
    if (searchDto.getCompleteTimeFrom() != null) {
      sql += " AND (w.finish_time >= TO_TIMESTAMP(:completeTimeFrom,'dd/mm/yyyy hh24:mi:ss')"
          + " and w.status = 8"
          + " and w.result is not null)";
      parameters
          .put("completeTimeFrom", DateUtil.date2ddMMyyyyHHMMss(searchDto.getCompleteTimeFrom()));
    }
    if (searchDto.getCompleteTimeTo() != null) {
      sql += " AND (w.finish_time <= TO_TIMESTAMP(:completeTimeTo,'dd/mm/yyyy hh24:mi:ss')"
          + " and w.status = 8"
          + " and w.result is not null)";
      parameters.put("completeTimeTo", DateUtil.date2ddMMyyyyHHMMss(searchDto.getCompleteTimeTo()));
    }

    if (searchDto.getWoId() != null) {
      sql += " AND w.wo_id = :woId";
      parameters.put("woId", searchDto.getWoId());
    }
    if (searchDto.getParentId() != null) {
      sql += " AND w.parent_id = :parentId";
      parameters.put("parentId", searchDto.getParentId());
    }
    // Nhan vien tao
    if (StringUtils.isNotNullOrEmpty(searchDto.getCreatePersonName())) {
      sql += " AND LOWER(cp.username) LIKE :createPersonName ESCAPE '\\'";
      parameters.put("createPersonName",
          StringUtils.convertLowerParamContains(searchDto.getCreatePersonName()));
    }
    // Nhan vien thuc hien
    if (StringUtils.isNotNullOrEmpty(searchDto.getFtName())) {
      sql += " AND LOWER(f.username) LIKE :ftName ESCAPE '\\'";
      parameters.put("ftName", StringUtils.convertLowerParamContains(searchDto.getFtName()));
    }
//    // tim kiem theo ma tram
//    if (searchDto.getLstStationCode() != null && searchDto.getLstStationCode().size() > 0) {
//      sql += " AND w.station_code in (";
//      int i = 1;
//      for (String station : searchDto.getLstStationCode()) {
//        if (i == searchDto.getLstStationCode().size()) {
//          sql += ":station";
//        } else {
//          sql += ":station ,";
//        }
//        parameters.put("station", station);
//        i++;
//      }
//      sql += ")";
//    }
    // nhom dieu phoi
    if (searchDto.getCdIdList() != null && searchDto.getCdIdList().size() > 0) {
      sql += " AND w.cd_id in (:lstCdId)";
      parameters.put("lstCdId", searchDto.getCdIdList());
//      int i = 1;
//      for (Long cd : searchDto.getCdIdList()) {
//        if (i == searchDto.getCdIdList().size()) {
//          sql += ":cd";
//        } else {
//          sql += ":cd, ";
//        }
//        parameters.put("cd", cd);
//        i++;
//      }
//      sql += ")";
    }
    // Cong viec cha
    if (StringUtils.isNotNullOrEmpty(searchDto.getParentName())) {
      sql += " AND LOWER(p.wo_code) LIKE :parentName ESCAPE '\\'";
      parameters
          .put("parentName", StringUtils.convertLowerParamContains(searchDto.getParentName()));
    }
    // Thue bao
    if (StringUtils.isNotNullOrEmpty(searchDto.getAccountIsdn())) {
      sql += " AND LOWER(wd.account_isdn) LIKE :accountIsdn ESCAPE '\\'";
      parameters
          .put("accountIsdn", StringUtils.convertLowerParamContains(searchDto.getAccountIsdn()));
    }
    // Tim theo don vi
    if (searchDto.getIsContainChildUnit() != null && searchDto.getIsContainChildUnit()) {
      if (searchDto.getCreateUnitId() != null) {
        sql += " AND w.create_person_id in ("
            + "select user_id from common_gnoc.users where unit_id in"
            + " (select unit_id from common_gnoc.unit where level < 50 and status = 1"
            + " start with unit_id = :createUnitId"
            + " connect by prior unit_id = parent_unit_id))";
        parameters.put("createUnitId", searchDto.getCreateUnitId());
      }
      if (searchDto.getProcessUnitId() != null) {
        sql += " AND (w.ft_id in ("
            + "select user_id from common_gnoc.users where unit_id in"
            + " (select unit_id from common_gnoc.unit where level < 50 and status = 1"
            + " start with unit_id = :processUnitId"
            + " connect by prior unit_id = parent_unit_id)))";
        parameters.put("processUnitId", searchDto.getProcessUnitId());
      }
    } else {
      if (searchDto.getCreateUnitId() != null) {
        sql += " AND w.create_person_id in "
            + "(select user_id from common_gnoc.users where unit_id = :createUnitId)";
        parameters.put("createUnitId", searchDto.getCreateUnitId());
      }
      if (searchDto.getProcessUnitId() != null) {
        sql += " AND (w.ft_id in "
            + "(select user_id from common_gnoc.users where unit_id = :processUnitId))";
        parameters.put("processUnitId", searchDto.getProcessUnitId());
      }
    }
    if (searchDto.getUserId() != null) {
      sql += " and (";
      Boolean isHasCondition = false;
      if (searchDto.isIsCreated() != null && searchDto.isIsCreated()) {
        sql += isHasCondition ? " or w.create_person_id = :userIdCreated"
            : " w.create_person_id = :userIdCreated";
        isHasCondition = true;
        parameters.put("userIdCreated", searchDto.getUserId());
      }
      /*if (searchDto.isIsCd() != null && searchDto.isIsCd()) {
        sql += isHasCondition
            ? " or w.cd_id in (select wo_group_id from wfm.wo_cd where user_id = :userIdCd)"
            : "  w.cd_id in (select wo_group_id from wfm.wo_cd where user_id = :userIdCd)";
        isHasCondition = true;
        parameters.put("userIdCd", searchDto.getUserId());
      }*/
      if (searchDto.isIsCd() != null && searchDto.isIsCd()) {
        String sqlCdId = SQLBuilder
            .getSqlQueryById(SQLBuilder.SQL_MODULE_WO, "get-wo_group_id_by_userId");
        Map<String, Object> paramsChild = new HashMap<>();
        paramsChild.put("userIdCd", searchDto.getUserId());
        List<String> lstCdIds = getNamedParameterJdbcTemplate()
            .queryForList(sqlCdId, paramsChild, String.class);
        String sqlTemp = "";
        if (lstCdIds != null && !lstCdIds.isEmpty()) {
          List<String> tempCdId = new ArrayList<>();
          int idx = 0;
          sqlTemp += "( ";
          for (int i = 0; i < lstCdIds.size(); i++) {
            tempCdId.add(lstCdIds.get(i));
            if ((i != 0 && i % 500 == 0) || i == lstCdIds.size() - 1) {
              String idParam = "id" + idx;
              sqlTemp += (i <= 500) ? " w.cd_id in (:" + idParam + ")"
                  : " OR w.cd_id in (:" + idParam + ")";
              List<String> lstTemp = new ArrayList<>();
              lstTemp.addAll(tempCdId);
              parameters.put(idParam, lstTemp);
              tempCdId.clear();
            }
          }
          sqlTemp += " ) ";

        } else {
          sqlTemp = "(1 = 2)";
        }
//        sql += " or w.cd_id in(36902,23821,77101,2975)";
        sql += isHasCondition
            ? " or " + sqlTemp : sqlTemp;
        isHasCondition = true;
      }

      if (searchDto.isIsFt() != null && searchDto.isIsFt()) {
        sql += isHasCondition ? " or w.ft_id = :userIdFt" : "  w.ft_id = :userIdFt";
        isHasCondition = true;
        parameters.put("userIdFt", searchDto.getUserId());
      }
      if (!isHasCondition) {
        sql += "1 = 1";
      }
      sql += ")";
    }
    // Wo can ho tro
    if (searchDto.getIsNeedSupport() != null && searchDto.getIsNeedSupport()) {
      sql += " AND w.need_support  = :needSupport";
      parameters.put("needSupport", Constants.VSMART_NEED_SUPPORT.YES);
    }

    if (StringUtils.isNotNullOrEmpty(searchDto.getStatusSearchWeb())) {
      if (searchDto.getCompleteTimeFrom() == null && searchDto.getCompleteTimeTo() == null) {
        //Chua hoan thanh - Cho CD phe duyet
        if ("-1".equals(searchDto.getStatusSearchWeb())) {
          sql += " AND w.status <> 7 and w.status <> 8 "
              + "and (w.status <> 6 or (w.status = 6 AND w.result is null))";
        }
        //Da hoan thanh, CD chua phe duyet
        else if ("6".equals(searchDto.getStatusSearchWeb())) {
          sql += " AND w.status = 6 AND w.result is null AND w.finish_time is null";
        } else if (searchDto.getStatusSearchWeb().contains(",")) {
          if (searchDto.getStatusSearchWeb().contains("6")) {
            sql += " AND (w.status in (:lstStatus) or "
                + "(w.status = 6 AND w.result is null AND w.finish_time is null))";
          } else {
            sql += " AND w.status in (:lstStatus)";
          }
          List<String> myList = new ArrayList<>(
              Arrays.asList(searchDto.getStatusSearchWeb().split(",")));
          for (String tmp : myList) {
            if ("10".equals(tmp.trim())) {
              tmp = "2";
            }
            lstStatus.add(Long.valueOf(tmp.trim()));
          }
          if (lstStatus != null && !lstStatus.isEmpty()) {
            parameters.put("lstStatus", lstStatus);
          }
        }
        //chi chon trang thai dong
        else if ("8".equals(searchDto.getStatusSearchWeb())) {
          sql += " AND ((w.status = 8 or w.status = 6) AND w.result is not null)";
        }
        // ft tu choi
        else if ("10".equals(searchDto.getStatusSearchWeb())) {
          sql += " and (case when w.status = 2 and w.ft_id is null then 1 else 0 end) = 1";
        }
        // cd tu choi
        else if ("2".equals(searchDto.getStatusSearchWeb())) {
          sql += " and (case when w.status = 2 and w.ft_id is not null then 1 else 0 end) = 1";
        } else {
          sql += " AND w.status = :statusSearchWeb";
          parameters.put("statusSearchWeb", searchDto.getStatusSearchWeb());
        }
      }
    }
    //bo trang thai CD tu choi
    if (StringUtils.isNotNullOrEmpty(searchDto.getStatusSearchWeb()) &&
        searchDto.getStatusSearchWeb().contains("10") &&
        searchDto.getStatusSearchWeb().contains("2")) {
      sql += " ";
    } else if (StringUtils.isStringNullOrEmpty(searchDto.getStatusSearchWeb()) ||
        !searchDto.getStatusSearchWeb().contains("10")) { // cd tu choi
      sql += " and (case when w.status = 2 and w.ft_id is null then 0 else 1 end) = 1";
    } else if (StringUtils.isNotNullOrEmpty(searchDto.getStatusSearchWeb()) &&
        searchDto.getStatusSearchWeb().contains("10")) {
      sql += " and (case when w.status = 2 and w.ft_id is not null then 0 else 1 end ) = 1";
    }

//    sql += " ORDER BY w.create_date DESC ";
    sql += buildSortCondition(WoInsideDTO.class, searchDto.getSortName(), searchDto.getSortType(),
        "w.create_date", "DESC");
    BaseDto baseDto = new BaseDto();
    baseDto.setParameters(parameters);
    baseDto.setSqlQuery(sql);
    return baseDto;
  }


  @Override
  public WoInsideDTO findWoByWoCodeNoOffset(String woCode) {
    List<WoEntity> lstData = findByMultilParam(WoEntity.class, "woCode", woCode);
    if (lstData != null && lstData.size() > 0) {
      return lstData.get(0).toDTO();
    }
    return null;
  }

  @Override
  public List<MaterialThresDTO> getListMaterialByWoId(Long woId) {
    BaseDto baseDto = new BaseDto();
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_WO_MATERIAL_DEDUCTE, "get-list-wo-material");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("copperCable", I18n.getLanguage("woMaterial.copperCable"));
    parameters.put("coaxialCable", I18n.getLanguage("woMaterial.coaxialCable"));
    parameters.put("woId", woId);
    baseDto.setSqlQuery(sqlQuery);
    baseDto.setParameters(parameters);
    List<MaterialThresDTO> materialThresDTOS = getNamedParameterJdbcTemplate()
        .query(baseDto.getSqlQuery(), baseDto.getParameters(),
            BeanPropertyRowMapper.newInstance(MaterialThresDTO.class));
    return materialThresDTOS;
  }

  public List<ResultDTO> getWOSummaryInfobyType(String username, int typeSearch, Long cdId,
      WoDTOSearch woDTOInput) {
    List<ResultDTO> list = new ArrayList<>();
    StringBuilder str = new StringBuilder();
    Map<String, Object> parameters = new HashMap<>();

    if (typeSearch == Constants.WO_TYPE_SEARCH.IS_FT) {
      // Xu ly su co
      str.append(
          " select  '107' key , count(wo_id) quantitySucc from wfm.wo a where ft_id = :ftId and status in (3,4,5,9)"
              + " and a.wo_system in ('TT') ");
      if (!StringUtils.isStringNullOrEmpty(woDTOInput.getStartTimeFrom()) && !StringUtils
          .isStringNullOrEmpty(woDTOInput.getStartTimeTo())) {
        str.append(" and CREATE_DATE >= TO_DATE(:createTimeFrom,'dd/MM/yyyy HH24:mi:ss') ");
        str.append(" and CREATE_DATE <= TO_DATE(:createTimeTo,'dd/MM/yyyy HH24:mi:ss') ");
      }
      if (!StringUtils.isStringNullOrEmpty(woDTOInput.getEndTimeFrom()) && !StringUtils
          .isStringNullOrEmpty(woDTOInput.getEndTimeTo())) {
        str.append(" and end_time >= TO_DATE(:endTimeFrom,'dd/MM/yyyy HH24:mi:ss') ");
        str.append(" and end_time <= TO_DATE(:endTimeTo,'dd/MM/yyyy HH24:mi:ss') ");
      }

      // CDBR
      str.append(" union all select  '108' key , count(a.wo_id) quantitySucc from wfm.wo a where  "
          + "  ft_id = :ftId and status in (3,4,5,9) and a.wo_system in ('SPM', 'SPM_VTNET') ");
      if (!StringUtils.isStringNullOrEmpty(woDTOInput.getStartTimeFrom()) && !StringUtils
          .isStringNullOrEmpty(woDTOInput.getStartTimeTo())) {
        str.append(" and a.CREATE_DATE >= TO_DATE(:createTimeFrom,'dd/MM/yyyy HH24:mi:ss') ");
        str.append(" and a.CREATE_DATE <= TO_DATE(:createTimeTo,'dd/MM/yyyy HH24:mi:ss') ");
      }
      if (!StringUtils.isStringNullOrEmpty(woDTOInput.getEndTimeFrom()) && !StringUtils
          .isStringNullOrEmpty(woDTOInput.getEndTimeTo())) {
        str.append(" and end_time >= TO_DATE(:endTimeFrom,'dd/MM/yyyy HH24:mi:ss') ");
        str.append(" and end_time <= TO_DATE(:endTimeTo,'dd/MM/yyyy HH24:mi:ss') ");
      }

      // MR_BD
      str.append(
          " union all  select '109' key , count(wo_id) quantitySucc from wfm.wo a where ft_id = :ftId  and status in (3,4,5,9) "
              + " and a.wo_system in ('MR')");
      if (!StringUtils.isStringNullOrEmpty(woDTOInput.getStartTimeFrom()) && !StringUtils
          .isStringNullOrEmpty(woDTOInput.getStartTimeTo())) {
        str.append(" and a.CREATE_DATE >= TO_DATE(:createTimeFrom,'dd/MM/yyyy HH24:mi:ss') ");
        str.append(" and a.CREATE_DATE <= TO_DATE(:createTimeTo,'dd/MM/yyyy HH24:mi:ss') ");
        parameters.put("createTimeFrom", woDTOInput.getStartTimeFrom());
        parameters.put("createTimeTo", woDTOInput.getStartTimeTo());
      }
      if (!StringUtils.isStringNullOrEmpty(woDTOInput.getEndTimeFrom()) && !StringUtils
          .isStringNullOrEmpty(woDTOInput.getEndTimeTo())) {
        str.append(" and a.end_time >= TO_DATE(:endTimeFrom,'dd/MM/yyyy HH24:mi:ss') ");
        str.append(" and a.end_time <= TO_DATE(:endTimeTo,'dd/MM/yyyy HH24:mi:ss') ");
        parameters.put("createTimeFrom", woDTOInput.getStartTimeFrom());
        parameters.put("createTimeTo", woDTOInput.getStartTimeTo());
      }

      // other
      str.append(" union all  select '110' key , count(w.wo_id) quantitySucc from wfm.wo w "
          + " where w.ft_id = :ftId  and w.status in (3,4,5,9) "
          + " and w.wo_system not in ('TT','SPM', 'SPM_VTNET', 'MR') ");
      parameters.put("createTimeFrom", woDTOInput.getStartTimeFrom());
      if (!StringUtils.isStringNullOrEmpty(woDTOInput.getStartTimeFrom()) && !StringUtils
          .isStringNullOrEmpty(woDTOInput.getStartTimeTo())) {
        str.append(" and w.CREATE_DATE >= TO_DATE(:createTimeFrom,'dd/MM/yyyy HH24:mi:ss') ");
        str.append(" and w.CREATE_DATE <= TO_DATE(:createTimeTo,'dd/MM/yyyy HH24:mi:ss') ");
        parameters.put("createTimeFrom", woDTOInput.getStartTimeFrom());
        parameters.put("createTimeTo", woDTOInput.getStartTimeTo());
      }
      if (!StringUtils.isStringNullOrEmpty(woDTOInput.getEndTimeFrom()) && !StringUtils
          .isStringNullOrEmpty(woDTOInput.getEndTimeTo())) {
        str.append(" and w.end_time >= TO_DATE(:endTimeFrom,'dd/MM/yyyy HH24:mi:ss') ");
        str.append(" and w.end_time <= TO_DATE(:endTimeTo,'dd/MM/yyyy HH24:mi:ss') ");
        parameters.put("createTimeFrom", woDTOInput.getStartTimeFrom());
        parameters.put("createTimeTo", woDTOInput.getStartTimeTo());
      }

    } //ThanhLV12_end
    //ThanhLv12_bo sung trang thai cho CD_start
    else if (typeSearch == Constants.WO_TYPE_SEARCH.IS_CD
        || typeSearch == Constants.WO_TYPE_SEARCH.IS_PROVINCE) {

    }
    UsersDTO usersDTO = userRepository.getUserDTOByUsernameLower(username);
    //ThanhLv12_bo sung trang thai cho CD_end

    String id = usersDTO == null ? null : usersDTO.getUserId();

    if (id != null) {
      Map<String, Object> paramWoGroup = new HashMap<>();
      paramWoGroup.put("userId", id);
      List<Long> lstCd = getNamedParameterJdbcTemplate()
          .queryForList("select a.wo_group_id from wfm.wo_cd a where a.user_id = :userId",
              paramWoGroup, Long.class);

      if (!StringUtils.isStringNullOrEmpty(woDTOInput.getStartTimeFrom()) && !StringUtils
          .isStringNullOrEmpty(woDTOInput.getStartTimeTo())) {
        parameters.put("createTimeFrom", woDTOInput.getStartTimeFrom());
        parameters.put("createTimeTo", woDTOInput.getStartTimeTo());
      }

      if (!StringUtils.isStringNullOrEmpty(woDTOInput.getEndTimeFrom()) && !StringUtils
          .isStringNullOrEmpty(woDTOInput.getEndTimeTo())) {
        parameters.put("endTimeFrom", woDTOInput.getEndTimeFrom());
        parameters.put("endTimeTo", woDTOInput.getEndTimeTo());
      }

      if (typeSearch == Constants.WO_TYPE_SEARCH.IS_CD) {
        if (lstCd == null || lstCd.size() == 0) {
          lstCd = new ArrayList<>();
          lstCd.add(-1000L);
        }
        parameters.put("lstCd", lstCd);
      } else if (typeSearch == Constants.WO_TYPE_SEARCH.IS_FT) {
        parameters.put("ftId", id);
      } else if (typeSearch == Constants.WO_TYPE_SEARCH.IS_PROVINCE) {

        lstCd = new ArrayList<>();
        if (cdId != null) {
          lstCd.add(cdId);
        } else {
          List<WoCdGroupInsideDTO> lst = getListCdGroup(username);
          if (lst != null && lst.size() > 0) {
            for (WoCdGroupInsideDTO i : lst) {
              lstCd.add(Long.valueOf(i.getWoGroupId()));
            }
          }
        }
        if (lstCd.isEmpty()) {
          lstCd.add(-1000L);
        }
        parameters.put("lstCd", lstCd);
      }
      list = getNamedParameterJdbcTemplate()
          .query(str.toString(), parameters, BeanPropertyRowMapper.newInstance(ResultDTO.class));
    }
    return list;
  }

  //hungtv bo sung phe duyet gia han wo start
  @Override
  public WoConfigPropertyDTO getTimeApproveExtend(String key) {
    try {
      Map<String, Object> parameters = new HashMap<>();
      String sql = "select * from COMMON_GNOC.CONFIG_PROPERTY cfg where cfg.KEY = :key";
      parameters.put("key", key);
      List<WoConfigPropertyDTO> lstCatItem = getNamedParameterJdbcTemplate().query(sql, parameters,
          BeanPropertyRowMapper.newInstance(WoConfigPropertyDTO.class));
      if (lstCatItem != null && !lstCatItem.isEmpty()) {
        return lstCatItem.get(0);
      }
    } catch (Exception err) {
      log.error(err.getMessage());
    }
    return null;
  }
  //hungtv bo sung phe duyet gia han wo end

  //tiennv bo sung ham nang cap
  @Override
  public List<WoHisForAccountDTO> getListWoHisForAccount(List<String> lstAcc) {
    List<WoHisForAccountDTO> lst = new ArrayList<>();
    try {
      String sql = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_WO, "get-list-wo-his-for-account");
      Map<String, Object> params = new HashMap<>();
      params.put("lstAcc", lstAcc);

      return getNamedParameterJdbcTemplate()
          .query(sql, params, BeanPropertyRowMapper.newInstance(WoHisForAccountDTO.class));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }


  @Override
  public Datatable getListConfigAutoCreateWoOs(AutoCreateWoOsDTO autoCreateWoOsDTO) {
    Datatable datatable = new Datatable();
    try {
      String sql = "select lg.ID, lg.CD_CODE, lg.CONTENT, lg.WO_TYPE_CODE, lg.ARRAY_FILE from WFM.LOG_AUTO_CREATE_WO_OS lg" +
          " where 1 = 1 order by lg.ID DESC";
      Map<String, Object> parameters = new HashMap<>();
      datatable = getListDataTableBySqlQuery(sql, parameters,
          autoCreateWoOsDTO.getPage(), autoCreateWoOsDTO.getPageSize(), AutoCreateWoOsDTO.class,
          autoCreateWoOsDTO.getSortName(), autoCreateWoOsDTO.getSortType());
      if (datatable != null) {
        return datatable;
      }
    } catch (Exception e) {
      log.error(e.getMessage());
    }
    return datatable;
  }

  @Override
  public ResultInSideDto insertOrUpdateAutoCreateWoOs(AutoCreateWoOsDTO autoCreateWoOsDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    try {
      if (StringUtils.isNotNullOrEmpty(autoCreateWoOsDTO.getCdCode())) {
        String sql = "select * from WFM.WO_CD_GROUP WHERE WO_GROUP_CODE = :cdCode";
        Map<String, Object> paramters = new HashMap<>();
        paramters.put("cdCode", autoCreateWoOsDTO.getCdCode());
        List<WoCdGroupDTO> lst = getNamedParameterJdbcTemplate().query(sql, paramters,
            BeanPropertyRowMapper.newInstance(WoCdGroupDTO.class));
        if (lst != null && !lst.isEmpty()) {
          autoCreateWoOsDTO.setCdId(lst.get(0).getWoGroupId());
        }
        autoCreateWoOsDTO.setCreateUpdateTime(null);
        resultInSideDto.setKey(Constants.RESULT.SUCCESS);
        getEntityManager().merge(autoCreateWoOsDTO.toEntity());
      } else {
        resultInSideDto.setKey(Constants.RESULT.FAIL);
        resultInSideDto.setMessage("CD Code is not null");
      }
    } catch (Exception err) {
      log.error(err.getMessage());
    }
    return resultInSideDto;
  }

  @Override
  public AutoCreateWoOsDTO getConfigById(Long id) {
    try {
      String sql = "select * from WFM.LOG_AUTO_CREATE_WO_OS WHERE ID = :id";
      Map<String, Object> paramters = new HashMap<>();
      paramters.put("id", id);
      List<AutoCreateWoOsDTO> lst = getNamedParameterJdbcTemplate().query(sql, paramters,
          BeanPropertyRowMapper.newInstance(AutoCreateWoOsDTO.class));
      if (lst != null && !lst.isEmpty()) {
        return lst.get(0);
      }
    } catch (Exception err) {
      log.error(err.getMessage());
    }
    return null;
  }

  @Override
  public ResultInSideDto delete(Long id) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    AutoCreateWoOsEntity entity = getEntityManager().find(AutoCreateWoOsEntity.class, id);
    getEntityManager().remove(entity);
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto syncFileFromWeb(Long woId) {
    ResultInSideDto resultInSideDto= new ResultInSideDto();
    try {
      WoInsideDTO woInsideDTO = findWoById(woId, 0.0);
      if (woInsideDTO != null) {
        //Lay danh sach file cua WO
        String[] lstWoFileName = null;
        if (woInsideDTO.getFileName() != null) {
          lstWoFileName = woInsideDTO.getFileName().trim().split(",");
        }
        List<GnocFileDto> lstGnocFiles = getListGnocFile(woId.toString());
        List<String> lstGnocFilesName = new ArrayList<>();
        if (lstGnocFiles != null) {
          for (GnocFileDto gnocFileDto : lstGnocFiles) {
            lstGnocFilesName.add(gnocFileDto.getFileName());
          }
        }
        //Case1: Tren Gnoc1 co file, Gnoc2 khong co file -> dua file len Gnoc2
        if (lstWoFileName != null && lstGnocFilesName == null) {
          for (String woFileName : lstWoFileName) {
            syncSingleFileFromDiskToFtp(woFileName.trim(), woInsideDTO);
          }
        }
        //Case2: Tren Gnoc1 co file, Gnoc2 thieu file -> dua file thieu len Gnoc2
        if (lstWoFileName != null && lstGnocFilesName != null) {
          //Check file tren gnoc1 co, gnoc2 khong co -> sync file len gnoc2
          for (String woFileName : lstWoFileName) {
            if (!lstGnocFilesName.contains(woFileName.trim())) {
              syncSingleFileFromDiskToFtp(woFileName.trim(), woInsideDTO);
            }
          }
          //Check file tren gnoc2 co, gnoc1 khong co -> sync file len gnoc1
          for (GnocFileDto file : lstGnocFiles) {
            com.viettel.gnoc1.service.ResultDTO resultDTO = syncSingleFileFromFtpToDisk(file, woInsideDTO);
            if (resultDTO != null) {
              resultInSideDto.setKey(resultDTO.getKey());
              resultInSideDto.setMessage(resultDTO.getMessage());
            }
          }
        }
      } else {
        resultInSideDto.setKey(RESULT.FAIL);
        resultInSideDto.setMessage(I18n.getLanguage("wo.woIsNotExists"));
      }
      resultInSideDto.setKey(RESULT.SUCCESS);
    } catch (Exception e) {
      log.error(e.getMessage());
      resultInSideDto.setKey(RESULT.FAIL);
      resultInSideDto.setMessage(e.getMessage());
    }
    return resultInSideDto;
  }

  private List<GnocFileDto> getListGnocFile(String woId) {
    try {
      String sql = " SELECT  "
          + " gnoc.id,  "
          + " gnoc.business_code,  "
          + " gnoc.business_id,  "
          + " gnoc.path,  "
          + " gnoc.file_name,  "
          + " gnoc.create_unit_id, "
          + " gnoc.create_unit_name, "
          + " gnoc.create_user_id, "
          + " gnoc.create_user_name, "
          + " gnoc.create_time, "
          + " gnoc.mapping_id "
          + "  FROM  "
          + " COMMON_GNOC.GNOC_FILE gnoc "
          + "  WHERE  "
          + " gnoc.business_code = 'WO'"
          + " AND "
          + " gnoc.business_id = :businessId";
      Map<String, String> paramters = new HashMap<>();
      paramters.put("businessId", woId);
      List<GnocFileDto> lstGnocFile = getNamedParameterJdbcTemplate().query(sql, paramters,
          BeanPropertyRowMapper.newInstance(GnocFileDto.class));
      if (lstGnocFile != null && !lstGnocFile.isEmpty()) {
        return lstGnocFile;
      }
    } catch (Exception e) {
      log.error(e.getMessage());
    }
    return null;
  }

  private void syncSingleFileFromDiskToFtp(String originalFilename, WoInsideDTO dto) {
    try {
      //Get file wo
      List<String> lstFileName = new ArrayList<>();
      lstFileName.add(originalFilename.trim());
      List<com.viettel.gnoc1.service.ObjFile> lstWoFile = gnoc1_woPort.getFileFromWo(dto.getWoId().toString(), lstFileName);
      if (lstWoFile != null && !lstWoFile.isEmpty()) {
        System.out.println("Get file " + originalFilename + " from GNOC1 success !");
        byte[] array = lstWoFile.get(0).getFileArr();
        String pathDate = FileUtils.createPathFtpByDate(dto.getCreateDate());
        String fullPath = FileUtils.saveFtpFile(server, port,
            PassTranformer.decrypt(user), PassTranformer.decrypt(pass),
            ftpUpload + "/" + pathDate, originalFilename.trim(), array);
        System.out.println("Save file ${originalFilename} to GNOC2 link : " + fullPath);
        //Insert file table Gnocfile
        GnocFileDto gnocFileDto = new GnocFileDto();
        gnocFileDto.setPath(fullPath);
        gnocFileDto.setFileName(originalFilename.trim());
        gnocFileDto.setBusinessCode(Constants.GNOC_FILE_BUSSINESS.WO);
        gnocFileDto.setBusinessId(dto.getWoId());
        gnocFileDto.setCreateTime(dto.getCreateDate());
        gnocFileDto.setMappingId(dto.getWoId());
        gnocFileDto.setCreateUserId(dto.getCreatePersonId());
        getEntityManager().persist(gnocFileDto.toEntity());
        System.out.println("Save file ${originalFilename} to GNOC2 success !");
      } else {
        System.out.println("File khong ton tai !!!" + originalFilename.trim());
      }
    } catch (Exception e) {
      log.error(e.getMessage());
    }
  }

  private com.viettel.gnoc1.service.ResultDTO syncSingleFileFromFtpToDisk(GnocFileDto gnocFileDto, WoInsideDTO dto) {
    boolean checkExits = false;
    com.viettel.gnoc1.service.ResultDTO resultDTO = null;
    try {
      List<String> lstFileName = new ArrayList<>();
      lstFileName.add(gnocFileDto.getFileName().trim());
      List<com.viettel.gnoc1.service.ObjFile> lstWoFile = gnoc1_woPort.getFileFromWo(dto.getWoId().toString(), lstFileName);
      if (lstWoFile != null && lstWoFile.isEmpty()) {
        checkExits = true;
      }
      if(checkExits) {
        byte[] bytes = FileUtils.getFtpFile(server, port, PassTranformer.decrypt(user), PassTranformer.decrypt(pass),
            gnocFileDto.getPath().trim());
        if (bytes != null) {
          com.viettel.gnoc1.service.WoDTO woDTO = new com.viettel.gnoc1.service.WoDTO();
          woDTO.setWoCode(dto.getWoCode());
          woDTO.setWoId(dto.getWoId().toString());
          woDTO.getListFileName().addAll(lstFileName);
          woDTO.getFileArr().add(bytes);
          System.out.println("Save file " + gnocFileDto.getFileName() + " to GNOC1 ...");
          resultDTO = gnoc1_woPort.updateFileForWo(woDTO);
        }
        else {
          System.out.println("Get file " + gnocFileDto.getFileName() + " from GNOC2 failed !");
        }
      }
    } catch (Exception e) {
      resultDTO = new com.viettel.gnoc1.service.ResultDTO();
      resultDTO.setKey(RESULT.FAIL);
      resultDTO.setMessage(e.getMessage());
      log.error(e.getMessage());
    }
    return resultDTO;
  }
}
