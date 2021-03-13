package com.viettel.gnoc.cr.repository;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.CrOcsScheduleDTO;
import com.viettel.gnoc.cr.dto.CrProcessDeptGroupDTO;
import com.viettel.gnoc.cr.dto.CrProcessGroup;
import com.viettel.gnoc.cr.dto.CrProcessInsideDTO;
import com.viettel.gnoc.cr.dto.CrProcessTemplateDTO;
import com.viettel.gnoc.cr.dto.CrProcessWoDTO;
import com.viettel.gnoc.cr.dto.GroupUnitDTO;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.cr.dto.TempImportDTO;
import com.viettel.gnoc.cr.model.CrOscScheduleEntity;
import com.viettel.gnoc.cr.model.CrProcessDeptGroupEntity;
import com.viettel.gnoc.cr.model.CrProcessEntity;
import com.viettel.gnoc.cr.model.CrProcessTemplateEntity;
import com.viettel.gnoc.cr.model.CrProcessWoEntity;
import com.viettel.gnoc.wo.dto.WoTypeInsideDTO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
@Slf4j
public class CrManagerProcessRepositoryImpl extends BaseRepository implements
    CrManagerProcessRepository {


  public String getSQL(CrProcessInsideDTO crProcessDTO, Map<String, Object> parameters) {
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_PROCESS_MANAGER, "getListSearchCrProcess");
    parameters.put("p_leeLocale", I18n.getLocale());
    parameters.put("p_system", "OPEN_PM");
    parameters.put("p_bussiness", "OPEN_PM.CR_PROCESS");
    if (crProcessDTO.getParentId() != null) {
      parameters.put("p_parent", crProcessDTO.getParentId());
    } else {
      parameters.put("p_parent", null);
    }
    if (StringUtils.isNotNullOrEmpty(crProcessDTO.getSearchAll())) {
      sqlQuery += " AND ( "
          + " lower(ld.CR_PROCESS_CODE) LIKE :searchAll ESCAPE '\\' "
          + " OR lower(ld.CR_PROCESS_NAME) LIKE :searchAll ESCAPE '\\' )";
      parameters
          .put("searchAll", StringUtils.convertLowerParamContains(crProcessDTO.getSearchAll()));
    }
    if (StringUtils.isNotNullOrEmpty(crProcessDTO.getCrProcessName())) {
      sqlQuery += " and lower(ld.CR_PROCESS_NAME) like :crProessName escape '\\' ";
      parameters.put("crProessName",
          StringUtils.convertLowerParamContains(crProcessDTO.getCrProcessName()));
    }
    if (StringUtils.isNotNullOrEmpty(crProcessDTO.getCrProcessCode())) {
      sqlQuery += " and lower(ld.CR_PROCESS_CODE) like :crProessCode escape '\\' ";
      parameters.put("crProessCode",
          StringUtils.convertLowerParamContains(crProcessDTO.getCrProcessCode()));
    }
    if (crProcessDTO.getCrTypeId() != null) {
      sqlQuery += " and ld.CR_TYPE_ID =:crTypeId ";
      parameters.put("crTypeId", crProcessDTO.getCrTypeId());
    }
    if (crProcessDTO.getRiskLevel() != null) {
      sqlQuery += " and ld.RISK_LEVEL =:riskLevel ";
      parameters.put("riskLevel", crProcessDTO.getRiskLevel());
    }
    if (crProcessDTO.getDeviceTypeId() != null) {
      sqlQuery += " and ld.DEVICE_TYPE_ID =:deviceTypeId ";
      parameters.put("deviceTypeId", crProcessDTO.getDeviceTypeId());
    }
    if (crProcessDTO.getImpactSegmentId() != null) {
      sqlQuery += " and ld.IMPACT_SEGMENT_ID =:impactSegmentId ";
      parameters.put("impactSegmentId", crProcessDTO.getImpactSegmentId());
    }
    sqlQuery += " order by ld.cr_process_level, NLSSORT(ld.CR_PROCESS_NAME, 'nls_sort = Vietnamese')";
    return sqlQuery;
  }

  @Override
  public Datatable getListSearchCrProcess(CrProcessInsideDTO crProcessDTO) {
    Map<String, Object> parameters = new HashMap<>();
    String sqlQuery = getSQL(crProcessDTO, parameters);
    return getListDataTableBySqlQuery(sqlQuery, parameters,
        crProcessDTO.getPage(), crProcessDTO.getPageSize(), CrProcessInsideDTO.class,
        crProcessDTO.getSortName(), crProcessDTO.getSortType());
  }

  @Override
  public List<CrProcessInsideDTO> actionGetListProcessType(CrProcessInsideDTO crProcessDTO) {

    StringBuilder sqlQuery = new StringBuilder();
    sqlQuery.append(SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_PROCESS_MANAGER, "actionGetListProcessType"));

    Map<String, Object> parameters = new HashMap<>();
    if (crProcessDTO.getCrTypeId() != null) {
      sqlQuery.append(" and cps.CR_TYPE_ID =:CR_TYPE_ID ");
      parameters.put("CR_TYPE_ID", crProcessDTO.getCrTypeId());
    }

    if (crProcessDTO.getRiskLevel() != null && crProcessDTO.getRiskLevel() > 0) {
      sqlQuery.append(" and cps.RISK_LEVEL =:RISK_LEVEL ");
      parameters.put("RISK_LEVEL", crProcessDTO.getRiskLevel());
    }

    if (crProcessDTO.getDeviceTypeId() != null && crProcessDTO.getDeviceTypeId() > 0) {
      sqlQuery.append(" and cps.DEVICE_TYPE_ID =:DEVICE_TYPE_ID ");
      parameters.put("DEVICE_TYPE_ID", crProcessDTO.getDeviceTypeId());
    }
    if (crProcessDTO.getImpactSegmentId() != null && crProcessDTO.getImpactSegmentId() > 0) {
      sqlQuery.append(" and cps.IMPACT_SEGMENT_ID =:IMPACT_SEGMENT_ID ");
      parameters.put("IMPACT_SEGMENT_ID", crProcessDTO.getImpactSegmentId());
    }
    List<CrProcessInsideDTO> lst = getNamedParameterJdbcTemplate().query(
        sqlQuery.toString(), parameters, BeanPropertyRowMapper.newInstance(CrProcessInsideDTO.class)
    );

    return lst;
  }

  @Override
  public List<ItemDataCRInside> getListCrProcessCBB(CrProcessInsideDTO crProcessDTO) {
    List<ItemDataCRInside> lst = new ArrayList();
    List<CrProcessInsideDTO> list = actionGetListProcessType(crProcessDTO);
    if ((list == null) || (list.isEmpty())) {
      return lst;
    }

    StringBuilder sqlQuery = new StringBuilder();
    sqlQuery.append(SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_PROCESS_MANAGER, "getListCrProcessCBB"));

    String temp = "";
    for (CrProcessInsideDTO processDTO : list) {
      temp = temp + "  (1," + processDTO.getCrProcessId() + "),";
    }
    temp = temp.substring(0, temp.length() - 1);

    sqlQuery.append(temp);
    sqlQuery.append(" ) CONNECT BY PRIOR parent_id = cr_process_id ) ");
    Map<String, Object> parameters = new HashMap<>();
    if (StringUtils.isNotNullOrEmpty(crProcessDTO.getCrProcessName())) {
      sqlQuery.append(" and lower(prc.CR_PROCESS_NAME) like :crProessName escape '\\' ");
      parameters.put("crProessName",
          StringUtils.convertLowerParamContains(crProcessDTO.getCrProcessName()));
    }
    if (StringUtils.isNotNullOrEmpty(crProcessDTO.getCrProcessCode())) {
      sqlQuery.append(" and lower(prc.CR_PROCESS_CODE) like :crProessCode escape '\\' ");
      parameters.put("crProessCode",
          StringUtils.convertLowerParamContains(crProcessDTO.getCrProcessCode()));
    }
    sqlQuery.append("ORDER BY prc.path");
    lst = getNamedParameterJdbcTemplate().query(sqlQuery.toString(), parameters,
        BeanPropertyRowMapper.newInstance(ItemDataCRInside.class));
    return lst;
  }

  @Override
  public List<CrProcessInsideDTO> getRootCrProcess() {
    Map<String, Object> parameters = new HashMap<>();
    StringBuilder sql = new StringBuilder();
    sql.append(
        SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_CR_PROCESS_MANAGER, "getRootCrProcess"));

    List<CrProcessInsideDTO> processDTOList = getNamedParameterJdbcTemplate().query(
        sql.toString(), parameters, BeanPropertyRowMapper.newInstance(CrProcessInsideDTO.class)
    );
    return processDTOList;
  }

  @Override
  public List<CrProcessGroup> getLstFileFromProcessId(CrProcessGroup crProcessGroup) {
    Map<String, Object> parameters = new HashMap<>();
    StringBuilder sql = new StringBuilder();
    sql.append(SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_PROCESS_MANAGER, "getLstFileFromProcessId"));

    if (!StringUtils.isStringNullOrEmpty(crProcessGroup.getCrProcessId())) {
      sql.append(" and a.cr_process_id like :crProcessId ");
      parameters.put("crProcessId", crProcessGroup.getCrProcessId());
    }

    if (!StringUtils.isStringNullOrEmpty(crProcessGroup.getFileType())) {
      sql.append(" and b.file_type like :file_type ");
      parameters.put("file_type", crProcessGroup.getFileType());
    }

    if (!StringUtils.isStringNullOrEmpty(crProcessGroup.getCode())) {
      sql.append(" and c.code like :code ");
      parameters.put("code", crProcessGroup.getCode());
    }

    if (!StringUtils.isStringNullOrEmpty(crProcessGroup.getName())) {
      sql.append(" and c.name like :name ");
      parameters.put("name", crProcessGroup.getName());
    }

    List<CrProcessGroup> processGroupList = getNamedParameterJdbcTemplate()
        .query(sql.toString(), parameters,
            BeanPropertyRowMapper.newInstance(CrProcessGroup.class));
    return processGroupList;
  }

  @Override
  public List<CrProcessGroup> getLstUnitFromProcessId(Long crProcessId) {
    Map<String, Object> parameters = new HashMap<>();
    StringBuilder sql = new StringBuilder();
    sql.append(SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_PROCESS_MANAGER, "getLstUnitFromProcessId"));

    if (!StringUtils.isStringNullOrEmpty(crProcessId)) {
      sql.append(" and a.cr_process_id like  :crProcessId");
      parameters.put("crProcessId", crProcessId);
    }
    List<CrProcessGroup> processGroupList = getNamedParameterJdbcTemplate()
        .query(sql.toString(), parameters,
            BeanPropertyRowMapper.newInstance(CrProcessGroup.class));

    return processGroupList;
  }

  @Override
  public List<CrProcessInsideDTO> getLstAllChildrenByProcessId(Long crProcessId) {
    StringBuilder sql = new StringBuilder();
    Map<String, Object> parameters = new HashMap<>();
    sql.append(SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_PROCESS_MANAGER, "getLstAllChildrenByProcessId"));

    if (!StringUtils.isStringNullOrEmpty(crProcessId)) {
      parameters.put("parent_id", crProcessId);
    } else {
      parameters.put("parent_id", 0);
    }
    sql.append("  connect by prior a.cr_process_id = a.parent_id ");

    List<CrProcessInsideDTO> list = getNamedParameterJdbcTemplate()
        .query(sql.toString(), parameters,
            BeanPropertyRowMapper.newInstance(CrProcessInsideDTO.class));

    return list;
  }

  @Override
  public CrProcessInsideDTO getParentByProcessId(Long crProcessId) {
    StringBuilder sql = new StringBuilder();
    Map<String, Object> parameters = new HashMap<>();
    sql.append(SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_PROCESS_MANAGER, "getParentByProcessId"));

    if (!StringUtils.isStringNullOrEmpty(crProcessId)) {
      parameters.put("crProcessId", crProcessId);
    } else {
      parameters.put("crProcessId", 0);
    }

    sql.append("  connect by prior a.cr_process_id = a.parent_id ");

    List<CrProcessInsideDTO> list = getNamedParameterJdbcTemplate()
        .query(sql.toString(), parameters,
            BeanPropertyRowMapper.newInstance(CrProcessInsideDTO.class));

    if (list != null && !list.isEmpty()) {
      return list.get(0);
    }
    return null;
  }

  @Override
  public CrProcessInsideDTO getCrProcessDetail(Long crProcessId) {
    StringBuilder sql = new StringBuilder();
    Map<String, Object> parameters = new HashMap<>();
    sql.append(
        SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_CR_PROCESS_MANAGER, "getCrProcessDetail"));
    parameters.put("crProcessId", crProcessId);

    List<CrProcessInsideDTO> list = getNamedParameterJdbcTemplate()
        .query(sql.toString(), parameters,
            BeanPropertyRowMapper.newInstance(CrProcessInsideDTO.class));

    if (list != null && !list.isEmpty()) {
      CrProcessInsideDTO crProcessDTO = list.get(0);

      if (!StringUtils.isStringNullOrEmpty(crProcessDTO.getParentId())) {
        CrProcessInsideDTO parent = getCrProcessById(crProcessDTO.getParentId());
        crProcessDTO.setParentCode(parent.getCrProcessCode());
      }

      crProcessDTO.setListCrProcessWo(getLstWoFromProcessId(crProcessDTO.getCrProcessId()));
      crProcessDTO.setListCrProcessDeptGroup(getCrProcessDeptGroup(crProcessDTO.getCrProcessId()));
      crProcessDTO.setListCrProcessTemplate(getCrProcessTemplate(crProcessDTO.getCrProcessId()));
      return crProcessDTO;
    }

    return null;
  }

  @Override
  public CrProcessInsideDTO getCrProcessLevelByCode(String crProcessCode) {
    StringBuilder sql = new StringBuilder();
    Map<String, Object> parameters = new HashMap<>();
    sql.append(SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_PROCESS_MANAGER, "getCrProcessLevelByCode"));
    parameters.put("crProcessCode", crProcessCode);

    List<CrProcessInsideDTO> list = getNamedParameterJdbcTemplate()
        .query(sql.toString(), parameters,
            BeanPropertyRowMapper.newInstance(CrProcessInsideDTO.class));

    if (list != null && !list.isEmpty()) {
      return list.get(0);
    }

    return null;
  }


  @Override
  public CrProcessInsideDTO getCrProcessById(Long crProcessId) {
    StringBuilder sql = new StringBuilder();
    Map<String, Object> parameters = new HashMap<>();
    sql.append(
        SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_CR_PROCESS_MANAGER, "getCrProcessDetail"));
    parameters.put("crProcessId", crProcessId);
    List<CrProcessInsideDTO> list = getNamedParameterJdbcTemplate()
        .query(sql.toString(), parameters,
            BeanPropertyRowMapper.newInstance(CrProcessInsideDTO.class));
    if (list != null && !list.isEmpty()) {
      return list.get(0);
    }
    return null;
  }

  public List<CrProcessTemplateDTO> getCrProcessTemplate(Long crProcessId) {
    StringBuilder sql = new StringBuilder();
    Map<String, Object> parameters = new HashMap<>();
    sql.append(SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_PROCESS_MANAGER, "getCrProcessTemplate"));
    parameters.put("crProcessId", crProcessId);
    return getNamedParameterJdbcTemplate()
        .query(sql.toString(), parameters,
            BeanPropertyRowMapper.newInstance(CrProcessTemplateDTO.class));
  }

  public List<CrProcessDeptGroupDTO> getCrProcessDeptGroup(Long crProcessId) {
    StringBuilder sql = new StringBuilder();
    Map<String, Object> parameters = new HashMap<>();
    sql.append(SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_PROCESS_MANAGER, "getCrProcessDeptGroup"));
    parameters.put("crProcessId", crProcessId);
    return getNamedParameterJdbcTemplate()
        .query(sql.toString(), parameters,
            BeanPropertyRowMapper.newInstance(CrProcessDeptGroupDTO.class));
  }

  @Override
  public CrProcessInsideDTO findCrProcess(CrProcessInsideDTO dto) {
    StringBuilder sql = new StringBuilder();
    Map<String, Object> parameters = new HashMap<>();
    sql.append("select * from cr_process a where 1=1 ");

    if (!StringUtils.isStringNullOrEmpty(dto.getCrProcessCode())) {
      sql.append(" and a.CR_PROCESS_CODE like  :crProcessCode");
      parameters.put("crProcessCode", dto.getCrProcessCode());
    }

    List<CrProcessInsideDTO> list = getNamedParameterJdbcTemplate()
        .query(sql.toString(), parameters,
            BeanPropertyRowMapper.newInstance(CrProcessInsideDTO.class));

    if (list != null && !list.isEmpty()) {
      return list.get(0);
    }

    return null;
  }


  void deleteThreeList(CrProcessInsideDTO dto) {

    deleteByMultilParam(CrProcessTemplateEntity.class, "crProcessId", dto.getCrProcessId());

    deleteByMultilParam(CrProcessDeptGroupEntity.class, "crProcessId", dto.getCrProcessId());

    deleteByMultilParam(CrProcessWoEntity.class, "crProcessId", dto.getCrProcessId());
  }

  void deleteGroupUnit(CrProcessInsideDTO dto) {
    deleteByMultilParam(CrProcessDeptGroupEntity.class, "crProcessId", dto.getCrProcessId());
  }


  public boolean checkDuplicateTemp(Long crProcessId, Long tempImportId) {
    StringBuilder sql = new StringBuilder();
    Map<String, Object> parameters = new HashMap<>();
    sql.append(
        "SELECT * FROM OPEN_PM.CR_PROCESS_TEMPLATE a WHERE a.CR_PROCESS_ID =:crProcessId and a.TEMP_IMPORT_ID =:tempImportId ");

    parameters.put("crProcessId", crProcessId);
    parameters.put("tempImportId", tempImportId);

    List<CrProcessTemplateDTO> list = getNamedParameterJdbcTemplate()
        .query(sql.toString(), parameters,
            BeanPropertyRowMapper.newInstance(CrProcessTemplateDTO.class));

    if (list != null && list.size() > 0) {
      return true;
    }

    return false;
  }

  public boolean checkDuplicateGroupUnit(Long crProcessId, Long groupUnitId) {
    StringBuilder sql = new StringBuilder();
    Map<String, Object> parameters = new HashMap<>();
    sql.append(
        "SELECT * FROM OPEN_PM.CR_PROCESS_DEPT_GROUP a WHERE a.CPDGP_TYPE =1 and a.CR_PROCESS_ID =:crProcessId and a.GROUP_UNIT_ID =:groupUnitId ");

    parameters.put("crProcessId", crProcessId);
    parameters.put("groupUnitId", groupUnitId);

    List<GroupUnitDTO> list = getNamedParameterJdbcTemplate()
        .query(sql.toString(), parameters,
            BeanPropertyRowMapper.newInstance(GroupUnitDTO.class));

    if (list != null && list.size() > 0) {
      return true;
    }

    return false;
  }

  @Override
  public void saveDataImport(Long crProcessId, List<TempImportDTO> tempImport,
      List<GroupUnitDTO> groupUnit) {

    if (tempImport != null && tempImport.size() > 0) {
      deleteByMultilParam(CrProcessTemplateEntity.class, "crProcessId", crProcessId);

      for (TempImportDTO tempImportDTO : tempImport) {
        CrProcessTemplateDTO crProcessTemplateDTO = new CrProcessTemplateDTO();
        crProcessTemplateDTO.setCrProcessId(crProcessId);
        crProcessTemplateDTO.setFileType(tempImportDTO.getFileType());
        crProcessTemplateDTO.setTempImportId(tempImportDTO.getTempImportId());

        if (!checkDuplicateTemp(crProcessTemplateDTO.getCrProcessId(),
            crProcessTemplateDTO.getTempImportId())) {
          getEntityManager().merge(crProcessTemplateDTO.toEntity());
        }
      }
    }

    if (groupUnit != null && groupUnit.size() > 0) {
      deleteByMultilParam(CrProcessDeptGroupEntity.class, "crProcessId", crProcessId);

      for (GroupUnitDTO groupUnitDTO : groupUnit) {
        CrProcessDeptGroupDTO crProcessDeptGroupDTO = new CrProcessDeptGroupDTO();
        crProcessDeptGroupDTO.setCrProcessId(crProcessId);
        crProcessDeptGroupDTO.setCpdgpType(1l);
        crProcessDeptGroupDTO.setGroupUnitId(groupUnitDTO.getGroupUnitId());

        if (!checkDuplicateGroupUnit(crProcessDeptGroupDTO.getCrProcessId(),
            crProcessDeptGroupDTO.getGroupUnitId())) {
          getEntityManager().merge(crProcessDeptGroupDTO.toEntity());
        }
      }

      CrProcessInsideDTO crProcessDTO = getCrProcessDetail(crProcessId);
      if (crProcessDTO != null) {
        if (crProcessDTO.getCrProcessLevel() == 2) {
          List<CrProcessDeptGroupDTO> list = getListGroupUnit(crProcessId);
          if (list != null) {
            for (CrProcessDeptGroupDTO item : list) {
              deleteByMultilParam(CrProcessDeptGroupEntity.class, "crProcessId",
                  item.getCrProcessId());
              for (GroupUnitDTO groupUnitDTO : groupUnit) {
                CrProcessDeptGroupDTO crProcessDeptGroupDTO = new CrProcessDeptGroupDTO();
                crProcessDeptGroupDTO.setCrProcessId(item.getCrProcessId());
                crProcessDeptGroupDTO.setCpdgpType(1l);
                crProcessDeptGroupDTO.setGroupUnitId(groupUnitDTO.getGroupUnitId());

                if (!checkDuplicateGroupUnit(crProcessDeptGroupDTO.getCrProcessId(),
                    crProcessDeptGroupDTO.getGroupUnitId())) {
                  getEntityManager().merge(crProcessDeptGroupDTO.toEntity());
                }
              }
            }
          }
        }
      }
    }
  }

  public void updateChildByParent(Long crProcessId, CrProcessInsideDTO dtoClient,
      boolean isImport) {
    StringBuilder sql = new StringBuilder();
    Map<String, Object> parameters = new HashMap<>();
    sql.append(
        " select *     from cr_process a     where a.parent_id = :cr_process_id ");

    if (!StringUtils.isStringNullOrEmpty(crProcessId)) {
      parameters.put("cr_process_id", crProcessId);
    } else {
      parameters.put("cr_process_id", 0);
    }

    List<CrProcessInsideDTO> list = getNamedParameterJdbcTemplate()
        .query(sql.toString(), parameters,
            BeanPropertyRowMapper.newInstance(CrProcessInsideDTO.class));

    List<CrProcessDeptGroupDTO> groupList = new ArrayList<>();
    if (isImport) {
      CrProcessInsideDTO dtoClientRs = getCrProcessDetail(dtoClient.getCrProcessId());
      groupList = dtoClientRs.getListCrProcessDeptGroup();
    } else {
      groupList = dtoClient.getListCrProcessDeptGroup();
    }

    if (list != null && list.size() > 0) {

      if (dtoClient != null) {
        for (CrProcessInsideDTO dto : list) {
          int num = deleteByMultilParam(CrProcessDeptGroupEntity.class, "crProcessId",
              dto.getCrProcessId());
          updateLevel3(dto, dtoClient);

          if (groupList != null && groupList.size() > 0) {
            for (CrProcessDeptGroupDTO crProcessDeptGroupDTO : groupList) {
              crProcessDeptGroupDTO.setCpdgpId(0l);
              crProcessDeptGroupDTO.setCrProcessId(dto.getCrProcessId());
              crProcessDeptGroupDTO.setCpdgpType(1l);
              getEntityManager().merge(crProcessDeptGroupDTO.toEntity());
            }
          }

          getEntityManager().merge(dto.toEntity());
        }
      }
    }

  }

  @Override
  public ResultInSideDto saveAllList(CrProcessInsideDTO dto) {
    ResultInSideDto resultDto = new ResultInSideDto();

    deleteThreeList(dto);

    List<CrProcessTemplateDTO> listCrProcessTemplate = dto.getListCrProcessTemplate();
    List<CrProcessDeptGroupDTO> listCrProcessDeptGroup = dto.getListCrProcessDeptGroup();
    List<CrProcessWoDTO> listCrProcessWo = dto.getListCrProcessWo();

    dto.setIsActive(1l);
    dto.setIsVmsaActiveCellProcess(dto.getIsVmsaActiveCellProcess());

    CrProcessEntity crProcessEntity = getEntityManager().merge(dto.toEntity());
    resultDto.setId(crProcessEntity.getCrProcessId());

    // check your child to eat some school to follow your father
    CrProcessInsideDTO dtoLevel = getCrProcessDetail(crProcessEntity.getCrProcessId());
    if (dtoLevel != null) {

      if (listCrProcessTemplate != null && listCrProcessTemplate.size() > 0) {
        for (CrProcessTemplateDTO crProcessTemplateDTO : listCrProcessTemplate) {
          crProcessTemplateDTO.setCrProcessId(crProcessEntity.getCrProcessId());
          crProcessTemplateDTO.setFileType(crProcessTemplateDTO.getFileType());
          crProcessTemplateDTO.setTempImportId(crProcessTemplateDTO.getTempImportId());

          getEntityManager().merge(crProcessTemplateDTO.toEntity());
        }

      }

      if (listCrProcessDeptGroup != null && listCrProcessDeptGroup.size() > 0) {
        for (CrProcessDeptGroupDTO crProcessDeptGroupDTO : listCrProcessDeptGroup) {
          crProcessDeptGroupDTO.setCrProcessId(crProcessEntity.getCrProcessId());
          crProcessDeptGroupDTO.setCpdgpType(1l);
          getEntityManager().merge(crProcessDeptGroupDTO.toEntity());
        }
      }

      if (listCrProcessWo != null && listCrProcessWo.size() > 0) {
        for (CrProcessWoDTO crProcessWoDTO : listCrProcessWo) {
          crProcessWoDTO.setCrProcessId(crProcessEntity.getCrProcessId());
          getEntityManager().merge(crProcessWoDTO.toEntity());
        }
      }

      if (dtoLevel.getCrProcessLevel() == 2) {
        updateChildByParent(resultDto.getId(), dto, false);
      }
    }

    resultDto.setKey(Constants.RESULT.SUCCESS);
    return resultDto;
  }


  @Override
  public CrProcessWoDTO getcCrProcessWo(Long id) {
    if (id != null && id > 0) {
      return getEntityManager().find(CrProcessWoEntity.class, id).toDTO();
    }
    return null;
  }

  @Override
  public List<WoTypeInsideDTO> getListWoType() {
    StringBuilder sql = new StringBuilder();
    Map<String, Object> parameters = new HashMap<>();
    sql.append(
        "select * from WFM.WO_TYPE a where 1=1 and a.ENABLE_CREATE = 1 order by WO_TYPE_NAME");

    List<WoTypeInsideDTO> list = getNamedParameterJdbcTemplate()
        .query(sql.toString(), parameters,
            BeanPropertyRowMapper.newInstance(WoTypeInsideDTO.class));

    return list;
  }


  public List<CrProcessDeptGroupDTO> getListGroupUnit(Long id) {
    StringBuilder sql = new StringBuilder();
    Map<String, Object> parameters = new HashMap<>();
    sql.append(
        "select *     from cr_process a     where a.parent_id = :p_id ");
    parameters.put("p_id", id);
    List<CrProcessDeptGroupDTO> list = getNamedParameterJdbcTemplate()
        .query(sql.toString(), parameters,
            BeanPropertyRowMapper.newInstance(CrProcessDeptGroupDTO.class));

    return list;
  }


  @Override
  public List<WoTypeInsideDTO> getListWoTypeWithEnable() {
    StringBuilder sql = new StringBuilder();
    Map<String, Object> parameters = new HashMap<>();
    sql.append(
        "select * from WFM.WO_TYPE a where 1=1 and a.IS_ENABLE = 1 order by WO_TYPE_NAME");

    List<WoTypeInsideDTO> list = getNamedParameterJdbcTemplate()
        .query(sql.toString(), parameters,
            BeanPropertyRowMapper.newInstance(WoTypeInsideDTO.class));

    return list;
  }

  @Override
  public List<CrProcessInsideDTO> getListCrProcessDTO(CrProcessInsideDTO crProcessDTO, int rowStart,
      int maxRow,
      String sortType, String sortFieldList) {
    return onSearchEntity(CrProcessEntity.class, crProcessDTO, rowStart, maxRow, sortType,
        sortFieldList);
  }

  @Override
  public List<CrProcessInsideDTO> getAllCrProcess(Long parentId) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_PROCESS_MANAGER, "getAllCrProcess");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("p_leeLocale", I18n.getLocale());
    if (!StringUtils.isStringNullOrEmpty(parentId)) {
      sql += " and lcp.parent_id=:prarentId ";
      parameters.put("prarentId", parentId);
    } else {
      sql += " and lcp.parent_id is null ";
    }
    return getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(CrProcessInsideDTO.class));
  }

  @Override
  public WoTypeInsideDTO getWoTypeInsideDTOByCode(String code) {
    StringBuilder sql = new StringBuilder();
    Map<String, Object> parameters = new HashMap<>();
    sql.append(
        "select * from WFM.WO_TYPE a where 1=1 and a.ENABLE_CREATE = 1 and lower(a.WO_TYPE_CODE) like :code escape '\\' order by WO_TYPE_NAME ");
    parameters.put("code", StringUtils.convertLowerParamContains(code));

    List<WoTypeInsideDTO> list = getNamedParameterJdbcTemplate()
        .query(sql.toString(), parameters,
            BeanPropertyRowMapper.newInstance(WoTypeInsideDTO.class));

    if (list != null && list.size() > 0) {
      return list.get(0);
    }

    return null;
  }

  @Override
  public ResultInSideDto deleteGroupUnitOrFileByProcessId(Long crProcessId) {
    ResultInSideDto resultDto = new ResultInSideDto();
    CrProcessEntity entity = getEntityManager().
        find(CrProcessEntity.class, crProcessId);
    if (entity != null) {
      getEntityManager().remove(entity);
      resultDto.setKey(Constants.RESULT.SUCCESS);
    } else {
      resultDto.setKey(Constants.RESULT.NODATA);
    }
    return resultDto;
  }

  @Override
  public ResultInSideDto deleteAllChildByParent(Long crProcessId) {
    ResultInSideDto resultDto = new ResultInSideDto();
    Map<String, Object> parameters = new HashMap<>();
    StringBuilder sql = new StringBuilder();
    CrProcessEntity entity = getEntityManager().
        find(CrProcessEntity.class, crProcessId);

    if (entity == null) {
      resultDto.setKey(Constants.RESULT.NODATA);
      return resultDto;
    }

    sql.append(SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_PROCESS_MANAGER, "deleteAllChildByParent"));

    if (!StringUtils.isStringNullOrEmpty(crProcessId)) {
      parameters.put("cr_process_id", crProcessId);
    }

    getNamedParameterJdbcTemplate()
        .query(sql.toString(), parameters,
            BeanPropertyRowMapper.newInstance(CrProcessInsideDTO.class));

    resultDto.setKey(Constants.RESULT.SUCCESS);
    return resultDto;
  }

  @Override
  public ResultInSideDto deleteFileAndDataWhenChangeProcess(String crId) {
    ResultInSideDto resultDto = new ResultInSideDto();
    if ((crId == null) || ("".equals(crId))) {
      resultDto.setKey(Constants.RESULT.NODATA);
    }
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_PROCESS_MANAGER, "deleteCrFileAttach");
    Query query = getEntityManager().createNativeQuery(sql);
    query.setParameter("cr_id", crId);
    query.executeUpdate();

    String sql1 = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_PROCESS_MANAGER, "deleteTempImportData");
    Query query1 = getEntityManager().createNativeQuery(sql1);
    query1.setParameter("cr_id", crId);
    query1.executeUpdate();

    String sql2 = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_PROCESS_MANAGER, "updateCrTable");
    Query query2 = getEntityManager().createNativeQuery(sql2);
    query2.setParameter("cr_id", crId);
    int result2 = query2.executeUpdate();
    if (result2 > 0) {
      resultDto.setKey(RESULT.SUCCESS);
    } else {
      resultDto.setKey(RESULT.ERROR);
    }
    return resultDto;
  }

  @Override
  public ResultInSideDto saveCrProcessWo(CrProcessWoDTO crProcessWoDTO) {
    ResultInSideDto resultDto = new ResultInSideDto();
    CrProcessWoEntity entity = getEntityManager().merge(crProcessWoDTO.toEntity());
    resultDto.setId(entity.getCrProcessWoId());

    resultDto.setKey(Constants.RESULT.SUCCESS);
    return resultDto;
  }

  @Override
  public ResultInSideDto saveCrProcessTemplate(CrProcessTemplateDTO crProcessTemplateDTO) {
    ResultInSideDto resultDto = new ResultInSideDto();
    CrProcessTemplateEntity entity = getEntityManager().merge(crProcessTemplateDTO.toEntity());
    resultDto.setId(entity.getCpteId());

    resultDto.setKey(Constants.RESULT.SUCCESS);
    return resultDto;
  }

  @Override
  public ResultInSideDto saveCrProcessGroupDept(CrProcessDeptGroupDTO crProcessDeptGroupDTO) {
    ResultInSideDto resultDto = new ResultInSideDto();
    CrProcessDeptGroupEntity entity = getEntityManager().merge(crProcessDeptGroupDTO.toEntity());
    resultDto.setId(entity.getCpdgpId());

    resultDto.setKey(Constants.RESULT.SUCCESS);
    return resultDto;
  }

  public ResultInSideDto delete(Long crProcessWoId) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    CrProcessWoEntity entity = getEntityManager().find(CrProcessWoEntity.class, crProcessWoId);
    if (entity != null) {
      getEntityManager().remove(entity);
      resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    } else {
      resultInSideDto.setKey(Constants.RESULT.NODATA);
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto deleteListCrProcessWo(List<Long> lstCrProcessWoId) {

    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    for (Long id : lstCrProcessWoId) {
      resultInSideDTO = delete(id);
    }
    return resultInSideDTO;
  }

  @Override
  public ResultInSideDto deleteCrProcessWo(Long lstCrProcessWoId) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();

    int number = deleteByMultilParam(CrProcessWoEntity.class, "crProcessId", lstCrProcessWoId);
    if (number != 0) {
      resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
    } else {
      resultInSideDTO.setKey(RESULT.NODATA);
    }
    return resultInSideDTO;
  }

  @Override
  public List<CrProcessWoDTO> getLstWoFromProcessId(Long crProcessId) {

    Map<String, Object> parameters = new HashMap<>();
    StringBuilder sql = new StringBuilder();
    sql.append(SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_PROCESS_MANAGER, "getLstWoFromProcessId"));

    if (!StringUtils.isStringNullOrEmpty(crProcessId)) {
      parameters.put("crProcessId", crProcessId);
    } else {
      parameters.put("crProcessId", 0);
    }

    List<CrProcessWoDTO> crProcessWoDTOList = getNamedParameterJdbcTemplate().query(
        sql.toString(), parameters, BeanPropertyRowMapper.newInstance(CrProcessWoDTO.class)
    );

    return crProcessWoDTOList;
  }

  @Override
  public List<CrOcsScheduleDTO> getListCrOcsScheduleDTO(CrOcsScheduleDTO crOcsScheduleDTO) {
    Map<String, Object> parameters = new HashMap<>();
    StringBuilder sql = new StringBuilder();
    sql.append(SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_CR_PROCESS_MANAGER, "getListCrOcsScheduleDTO"));

    if (!StringUtils.isStringNullOrEmpty(crOcsScheduleDTO.getCrOcsScheduleId())) {
      sql.append(" and a.cr_ocs_schedule_id like :crOscScheduleId");
      parameters.put("crOscScheduleId", crOcsScheduleDTO.getCrOcsScheduleId());
    }
    if (!StringUtils.isStringNullOrEmpty(crOcsScheduleDTO.getUserId())) {
      sql.append(" and a.user_id=:userId");
      parameters.put("userId", crOcsScheduleDTO.getUserId());
    }
    if (!StringUtils.isStringNullOrEmpty(crOcsScheduleDTO.getCrProcessId())) {
      sql.append(" and a.cr_process_id=:crProcessId");
      parameters.put("crProcessId", crOcsScheduleDTO.getCrProcessId());
    }
    sql.append(" order by b.username desc ");

    List<CrOcsScheduleDTO> crProcessWoDTOList = getNamedParameterJdbcTemplate().query(
        sql.toString(), parameters, BeanPropertyRowMapper.newInstance(CrOcsScheduleDTO.class)
    );

    return crProcessWoDTOList;
  }

  @Override
  public ResultInSideDto insertOrUpdateCrOcsScheduleDTO(CrOcsScheduleDTO crOcsScheduleDTO) {
    ResultInSideDto resultDto = new ResultInSideDto();
    CrOscScheduleEntity entity = getEntityManager().merge(crOcsScheduleDTO.toEntity());
    resultDto.setId(entity.getCrOscScheduleId());

    resultDto.setKey(Constants.RESULT.SUCCESS);
    return resultDto;
  }


  public void updateLevel3(CrProcessInsideDTO crProcessDTO, CrProcessInsideDTO checkLevel) {
    crProcessDTO.setImpactType(checkLevel.getImpactType());
    crProcessDTO.setImpactCharacteristic(checkLevel.getImpactCharacteristic());
    crProcessDTO.setCrTypeId(checkLevel.getCrTypeId());
    crProcessDTO.setRiskLevel(checkLevel.getRiskLevel());
    crProcessDTO.setImpactSegmentId(checkLevel.getImpactSegmentId());
    crProcessDTO.setDeviceTypeId(checkLevel.getDeviceTypeId());
    crProcessDTO.setOtherDept(checkLevel.getOtherDept());
//    crProcessDTO.setIsVmsaActiveCellProcess(checkLevel.getIsVmsaActiveCellProcess());
//    crProcessDTO.setRequireFileLog(checkLevel.getRequireFileLog());
//    crProcessDTO.setRequireApprove(checkLevel.getRequireApprove());
//    crProcessDTO.setCloseCrWhenResolveSuccess(checkLevel.getCloseCrWhenResolveSuccess());
//    crProcessDTO.setIsLaneImpact(checkLevel.getIsLaneImpact());
//    crProcessDTO.setRequireMop(checkLevel.getRequireMop());
//    crProcessDTO.setVmsaActiveCellProcessKey(checkLevel.getVmsaActiveCellProcessKey());
    crProcessDTO.setApprovalLevel(checkLevel.getApprovalLevel());
  }

  @Override
  public ResultInSideDto insertOrUpdateCrProcessDTO(CrProcessInsideDTO crProcessDTO) {

    // check neu update level 2
    if ("2".equalsIgnoreCase(crProcessDTO.getIsAdd())) {
      CrProcessInsideDTO checkLevel = getCrProcessLevelByCode(crProcessDTO.getCrProcessCode());
      if (checkLevel != null && checkLevel.getCrProcessLevel() == 2) {
        updateChildByParent(checkLevel.getCrProcessId(), crProcessDTO, true);
      }

      CrProcessInsideDTO parentCheck = getCrProcessById(crProcessDTO.getParentId());
      if (parentCheck != null && parentCheck.getCrProcessLevel() == 2) {
        updateChildByParent(parentCheck.getCrProcessId(), parentCheck, true);
        updateLevel3(crProcessDTO, parentCheck);
      }

    } else if ("1".equalsIgnoreCase(crProcessDTO.getIsAdd())) {
      if (!StringUtils.isStringNullOrEmpty(crProcessDTO.getParentId())) {
        CrProcessInsideDTO checkLevel = getCrProcessById(crProcessDTO.getParentId());
        if (checkLevel != null && checkLevel.getCrProcessLevel() == 2) {
          updateChildByParent(checkLevel.getCrProcessId(), checkLevel, true);
          updateLevel3(crProcessDTO, checkLevel);
        }
      }
    }

    ResultInSideDto resultDto = new ResultInSideDto();
    crProcessDTO.setIsActive(1L);
    CrProcessEntity entity = getEntityManager().merge(crProcessDTO.toEntity());
    resultDto.setId(entity.getCrProcessId());

    resultDto.setKey(Constants.RESULT.SUCCESS);
    return resultDto;
  }

  public ResultInSideDto deleteCrOcsScheduleDTO(Long crOscScheduleId) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    CrOscScheduleEntity entity = getEntityManager()
        .find(CrOscScheduleEntity.class, crOscScheduleId);
    if (entity != null) {
      getEntityManager().remove(entity);
      resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    } else {
      resultInSideDto.setKey(Constants.RESULT.NODATA);
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto deleteListCrOcsScheduleDTO(List<Long> crOcsScheduleDTOs) {
    ResultInSideDto resultDto = new ResultInSideDto();
    for (Long id : crOcsScheduleDTOs) {
      resultDto = deleteCrOcsScheduleDTO(id);
    }
    return resultDto;
  }

  @Override
  public List<CrProcessInsideDTO> getListDataExport(CrProcessInsideDTO crProcessDTO) {
    Map<String, Object> parameters = new HashMap<>();
    String sql = getSQL(crProcessDTO, parameters);

    List<CrProcessInsideDTO> lstDTO = getNamedParameterJdbcTemplate()
        .query(sql, parameters,
            BeanPropertyRowMapper.newInstance(CrProcessInsideDTO.class));

    return lstDTO;
  }

  public BaseDto sqlSearch(CrProcessInsideDTO crProcessDTO) {
    BaseDto baseDto = new BaseDto();
    String sqlQuery = "";
    sqlQuery = "SELECT * FROM OPEN_PM.CR_PROCESS a WHERE 1=1 ";

    Map<String, Object> parameters = new HashMap<>();
    if (StringUtils.isNotNullOrEmpty(crProcessDTO.getSearchAll())) {
      sqlQuery += "  and lower(a.FAULT_NAME) like :searchAll escape '\\' ";
      parameters
          .put("searchAll", StringUtils.convertLowerParamContains(crProcessDTO.getSearchAll()));
    }

    if (crProcessDTO.getCrProcessId() != null && crProcessDTO.getCrProcessId() > 0) {
      sqlQuery += " and a.CR_PROCESS_ID = :crProcessId ";
      parameters.put("crProcessId", crProcessDTO.getCrProcessId());
    }

    if (crProcessDTO.getIsActive() != null
        && crProcessDTO.getIsActive() > -1) {
      sqlQuery += " and a.IS_ACTIVE = :isActive ";
      parameters.put("isActive", crProcessDTO.getIsActive());
    }

    if (StringUtils.isNotNullOrEmpty(crProcessDTO.getCrProcessCode())) {
      sqlQuery += " and a.CR_PROCESS_CODE like :crProcessCode ";
      parameters.put("crProcessCode", crProcessDTO.getCrProcessCode());
    }
    if (StringUtils.isNotNullOrEmpty(crProcessDTO.getCrProcessName())) {
      sqlQuery += " and a.CR_PROCESS_NAME like :crProcessName ";
      parameters.put("crProcessName", crProcessDTO.getCrProcessName());
    }
    if (crProcessDTO.getImpactSegmentId() != null && crProcessDTO.getImpactSegmentId() > 0) {
      sqlQuery += " and a.IMPACT_SEGMENT_ID = :impSegId ";
      parameters.put("impSegId", crProcessDTO.getImpactSegmentId());
    }
    if (crProcessDTO.getDeviceTypeId() != null && crProcessDTO.getDeviceTypeId() > 0) {
      sqlQuery += " and a.DEVICE_TYPE_ID = :deviceTypeId ";
      parameters.put("deviceTypeId", crProcessDTO.getDeviceTypeId());
    }
    if (crProcessDTO.getSubcategoryId() != null && crProcessDTO.getSubcategoryId() > 0) {
      sqlQuery += " and a.SUBCATEGORY_ID = :subCateId ";
      parameters.put("subCateId", crProcessDTO.getSubcategoryId());
    }

    baseDto.setSqlQuery(sqlQuery);
    baseDto.setParameters(parameters);
    return baseDto;
  }

  @Override
  public CrProcessInsideDTO checkCrProcessExist(CrProcessInsideDTO dto) {
    StringBuilder sql = new StringBuilder();
    Map<String, Object> parameters = new HashMap<>();
    sql.append("select * from cr_process a where 1=1 ");

    if (!StringUtils.isStringNullOrEmpty(dto.getCrProcessCode())) {
      sql.append(" and a.CR_PROCESS_CODE like  :crProcessCode");
      parameters.put("crProcessCode", dto.getCrProcessCode());
    } else {
      sql.append(" and a.CR_PROCESS_CODE like  :crProcessCode");
      parameters.put("crProcessCode", null);
    }

    List<CrProcessInsideDTO> list = getNamedParameterJdbcTemplate()
        .query(sql.toString(), parameters,
            BeanPropertyRowMapper.newInstance(CrProcessInsideDTO.class));

    if (list != null && !list.isEmpty()) {
      return list.get(0);
    }

    return null;
  }

  @Override
  public Datatable getLstFileTemplate(CrProcessGroup crProcessGroup) {
    Map<String, Object> parameters = new HashMap<>();
    StringBuilder sql = new StringBuilder();
    sql.append(
        SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_CR_PROCESS_MANAGER, "getLstFileTemplate"));

    if (!StringUtils.isStringNullOrEmpty(crProcessGroup.getCode())) {
      sql.append(
          " and (( LOWER(c.code) LIKE '%' || LOWER(:code) || '%')  or (LOWER(c.code) LIKE LOWER(:code) ))");
      parameters.put("code", crProcessGroup.getCode());
    }

    if (!StringUtils.isStringNullOrEmpty(crProcessGroup.getName())) {
      sql.append(
          " and ((LOWER(c.name) LIKE '%' || LOWER(:name) || '%') or (LOWER(c.name) LIKE LOWER(:name) )) ");
      parameters.put("name", crProcessGroup.getName());
    }

    sql.append(" ORDER BY c.code asc");

    return getListDataTableBySqlQuery(sql.toString(), parameters,
        crProcessGroup.getPage(), crProcessGroup.getPageSize(), CrProcessGroup.class,
        crProcessGroup.getSortName(), crProcessGroup.getSortType());
  }

  public List<TempImportDTO> getListTempImportDTO(String search) {
    Map<String, Object> parameters = new HashMap<>();
    StringBuilder sql = new StringBuilder();
    sql.append(
        SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_CR_PROCESS_MANAGER, "getLstFileTemplate"));

    if (!StringUtils.isStringNullOrEmpty(search)) {
      sql.append(" AND LOWER(c.code) = :code ");
      parameters.put("code", search.toLowerCase());
    }

    List<TempImportDTO> list = getNamedParameterJdbcTemplate()
        .query(sql.toString(), parameters,
            BeanPropertyRowMapper.newInstance(TempImportDTO.class));

    return list;
  }

  @Override
  public ResultInSideDto deleteListCrProcess(List<Long> crProcessIds) {
    ResultInSideDto resultDto = new ResultInSideDto();
    for (Long id : crProcessIds) {
      resultDto = deleteCrProcessDTO(id);
    }
    return resultDto;
  }

  @Override
  public List<Long> deleteChildByParent(Long crProcessId) {
    List<Long> crProcessIdDelete = new ArrayList<>();
    StringBuilder sql = new StringBuilder();
    Map<String, Object> parameters = new HashMap<>();
    sql.append(
        " select *     from cr_process a   start with a.parent_id = :cr_process_id  connect by prior a.cr_process_id = a.parent_id ");

    if (!StringUtils.isStringNullOrEmpty(crProcessId)) {
      parameters.put("cr_process_id", crProcessId);
    } else {
      parameters.put("cr_process_id", 0);
    }

    List<CrProcessInsideDTO> list = getNamedParameterJdbcTemplate()
        .query(sql.toString(), parameters,
            BeanPropertyRowMapper.newInstance(CrProcessInsideDTO.class));

    if (list != null && list.size() > 0) {
      for (CrProcessInsideDTO dto : list) {
        CrProcessEntity entity = getEntityManager()
            .find(CrProcessEntity.class, dto.getCrProcessId());
        if (entity != null) {
          crProcessIdDelete.add(dto.getCrProcessId());
          getEntityManager().remove(entity);
        }
      }
    }
    return crProcessIdDelete;
  }

  @Override
  public CrProcessWoDTO getCrProcessWoDTO(Long crProcessWoId) {
    StringBuilder sql = new StringBuilder();
    Map<String, Object> parameters = new HashMap<>();
    sql.append(" select DISTINCT * from CR_PROCESS_WO a Where a.CR_PROCESS_WO_ID = :cr_process_id");

    if (!StringUtils.isStringNullOrEmpty(crProcessWoId)) {
      parameters.put("cr_process_id", crProcessWoId);
    } else {
      parameters.put("cr_process_id", 0);
    }

    List<CrProcessWoDTO> list = getNamedParameterJdbcTemplate()
        .query(sql.toString(), parameters,
            BeanPropertyRowMapper.newInstance(CrProcessWoDTO.class));

    if (list != null && list.size() > 0) {
      return list.get(0);
    }

    return null;
  }

  @Override
  public CrProcessInsideDTO generateCrProcessCode(CrProcessInsideDTO crProcessDTO) {
    CrProcessInsideDTO crProcessDTOResult = new CrProcessInsideDTO();
    String sqlQuery = " SELECT MAX(CP.CR_PROCESS_INDEX) crProcessIndex FROM OPEN_PM.CR_PROCESS CP WHERE CP.PARENT_ID = :p_parentId ";
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("p_parentId", crProcessDTO.getParentId());
    List<CrProcessInsideDTO> crProcessDTOS = getNamedParameterJdbcTemplate()
        .query(sqlQuery, parameters,
            BeanPropertyRowMapper.newInstance(CrProcessInsideDTO.class));
    if (!crProcessDTOS.isEmpty()) {
      Long genIndexTmp = crProcessDTOS.get(0).getCrProcessIndex();
      Long genIndex;
      if (genIndexTmp == null) {
        genIndex = 1L;
      } else {
        genIndex = genIndexTmp + 1L;
      }
      String crProcessCodeResult = crProcessDTO.getParentCode() + "_" + genIndex.toString();
      crProcessDTOResult.setCrProcessCode(crProcessCodeResult);
      crProcessDTOResult.setCrProcessIndex(genIndex);
    }
    return crProcessDTOResult;
  }

  @Override
  public ResultInSideDto deleteCrProcessDTO(Long crProcessId) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    CrProcessEntity entity = getEntityManager().find(CrProcessEntity.class, crProcessId);
    if (entity != null) {
      getEntityManager().remove(entity);
      resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    } else {
      resultInSideDto.setKey(Constants.RESULT.NODATA);
    }
    return resultInSideDto;
  }

  @Override
  public List<GroupUnitDTO> getGroupUnitDTO(GroupUnitDTO groupUnitDTO) {
    Map<String, Object> parameters = new HashMap<>();
    StringBuilder sql = new StringBuilder();
    sql.append("Select * from GROUP_UNIT a where a.IS_ACTIVE = 1");

    if (!StringUtils.isStringNullOrEmpty(groupUnitDTO)) {
      sql.append(" AND LOWER(a.GROUP_UNIT_CODE) = :code ");
      parameters.put("code", groupUnitDTO.getGroupUnitCode().toLowerCase().trim());
    }

    List<GroupUnitDTO> list = getNamedParameterJdbcTemplate().query(
        sql.toString(), parameters, BeanPropertyRowMapper.newInstance(GroupUnitDTO.class)
    );

    return list;
  }

  @Override
  public boolean checkIsParent(Long crProcessId) {
    StringBuilder sql = new StringBuilder();
    Map<String, Object> parameters = new HashMap<>();
    sql.append("select * from cr_process where  1 =1");

    if (!StringUtils.isStringNullOrEmpty(crProcessId)) {
      sql.append(" AND parent_id = :crProcessId ");
      parameters.put("crProcessId", crProcessId);
    } else {
      sql.append(" AND parent_id = :crProcessId ");
      parameters.put("crProcessId", 0);
    }

    List<CrProcessInsideDTO> list = getNamedParameterJdbcTemplate()
        .query(sql.toString(), parameters,
            BeanPropertyRowMapper.newInstance(CrProcessInsideDTO.class));

    if (list != null && !list.isEmpty()) {
      return true;
    }

    return false;
  }
}
