package com.viettel.gnoc.cr.repository;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.LanguageExchangeDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.repository.LanguageExchangeRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.CrManagerUnitsOfScopeDTO;
import com.viettel.gnoc.cr.dto.CrUnitsScopeDeviceTypeDTO;
import com.viettel.gnoc.cr.dto.ImpactSegmentDTO;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.cr.model.CrManagerUnitsOfScopeEntity;
import com.viettel.gnoc.cr.model.CrUnitsScopeDeviceTypeEntity;
import com.viettel.gnoc.cr.model.ImpactSegmentEntity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author DungPV
 */
@Repository
@Transactional
@Slf4j
public class CrManagerUnitsOfScopeRepositoryImpl extends BaseRepository implements
    CrManagerUnitsOfScopeRepository {

  @Autowired
  protected LanguageExchangeRepository languageExchangeRepository;

  @Autowired
  private CrUnitsScopeDeviceTypeRepository crUnitsScopeDeviceTypeRepository;

  @Override
  public BaseDto sqlSearch(CrManagerUnitsOfScopeDTO crManagerUnitsOfScopeDTO) {
    BaseDto baseDto = new BaseDto();
    String sqlQuery = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_Cr_Manager_Units_Of_Scope,
        "crManagerUnitsOfScope-list");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("p_leeLocale", I18n.getLocale());
    if (StringUtils.isNotNullOrEmpty(crManagerUnitsOfScopeDTO.getSearchAll())) {
      StringBuilder sqlSearchAll = new StringBuilder(
          " AND (lower(ls.cmse_code) LIKE :searchAll ESCAPE '\\' ");
      sqlSearchAll.append(" OR lower(ls.cmse_name) LIKE :searchAll ESCAPE '\\' ) ");
      sqlQuery += sqlSearchAll.toString();
      parameters
          .put("searchAll",
              StringUtils.convertLowerParamContains(crManagerUnitsOfScopeDTO.getSearchAll()));
    }

    if (StringUtils.isNotNullOrEmpty(crManagerUnitsOfScopeDTO.getUnitCode())) {
      sqlQuery += " AND LOWER(ls.unit_code) LIKE :unit_code ESCAPE '\\' ";
      parameters.put("unit_code", StringUtils.convertLowerParamContains
          (crManagerUnitsOfScopeDTO.getUnitCode()));
    }
    if (StringUtils.isNotNullOrEmpty(crManagerUnitsOfScopeDTO.getUnitName())) {
      sqlQuery += " AND LOWER(ls.unit_name) LIKE :unit_name ESCAPE '\\' ";
      parameters.put("unit_name", StringUtils.convertLowerParamContains
          (crManagerUnitsOfScopeDTO.getUnitName()));
    }
    if (StringUtils.isNotNullOrEmpty(crManagerUnitsOfScopeDTO.getCmseCode())) {
      sqlQuery += " AND LOWER(ls.cmse_code) LIKE :cmsecode ESCAPE '\\' ";
      parameters.put("cmsecode", StringUtils.convertLowerParamContains
          (crManagerUnitsOfScopeDTO.getCmseCode()));
    }
    if (StringUtils.isNotNullOrEmpty(crManagerUnitsOfScopeDTO.getCmseName())) {
      sqlQuery += " AND LOWER(ls.cmse_name) LIKE :cmse_name ESCAPE '\\' ";
      parameters.put("cmse_name", StringUtils.convertLowerParamContains
          (crManagerUnitsOfScopeDTO.getCmseName()));
    }
    if (crManagerUnitsOfScopeDTO.getCrTypeId() != null
        && crManagerUnitsOfScopeDTO.getCrTypeId() > 0) {
      sqlQuery += " AND ls.CR_TYPE =:IMPACT_SEGMENT_Id ";
      parameters.put("IMPACT_SEGMENT_Id", crManagerUnitsOfScopeDTO.getCrTypeId());
    }
    if (crManagerUnitsOfScopeDTO.getDeviceType() != null
        && crManagerUnitsOfScopeDTO.getDeviceType() > 0) {
      sqlQuery += " and ls.CMNOSE_ID in (Select u.CR_UNITS_SCOPE_ID from OPEN_PM.CR_UNITS_SCOPE_DEVICE_TYPE u where u.DEVICE_TYPE_ID =:deviceType) ";
      parameters.put("deviceType", crManagerUnitsOfScopeDTO.getDeviceType());
    }
//    sqlQuery += " order by ls.cmse_code ASC";
    sqlQuery += " order by ls.CMNOSE_ID DESC";
    baseDto.setSqlQuery(sqlQuery);
    baseDto.setParameters(parameters);
    return baseDto;
  }

  @Override
  public Datatable getListCrManagerUnitsOfScope(CrManagerUnitsOfScopeDTO crManagerUnitsOfScopeDTO) {
    BaseDto baseDto = sqlSearch(crManagerUnitsOfScopeDTO);
    return getList(getListDataTableBySqlQuery(baseDto.getSqlQuery(),
        baseDto.getParameters(), crManagerUnitsOfScopeDTO.getPage(),
        crManagerUnitsOfScopeDTO.getPageSize(), CrManagerUnitsOfScopeDTO.class,
        crManagerUnitsOfScopeDTO.getSortName(), crManagerUnitsOfScopeDTO.getSortType()));
  }

  private Datatable getList(Datatable datatable) {
    Datatable data = new Datatable();
    List<CrManagerUnitsOfScopeDTO> lstReturn = new ArrayList<>();
    if (!datatable.getData().isEmpty() && datatable.getData().size() > 0) {
      List<CrManagerUnitsOfScopeDTO> lst = (List<CrManagerUnitsOfScopeDTO>) datatable.getData();
      for (CrManagerUnitsOfScopeDTO dto : lst) {
        dto.setDeviceTypeName(returnDevice(dto.getCmnoseId()));
        lstReturn.add(dto);
      }
      data.setTotal(datatable.getTotal());
      data.setPages(datatable.getPages());
      data.setData(lstReturn);
    }
    return data;
  }

  private String returnDevice(Long cmnoseId) {
    String sTemp = "";
    List<CrUnitsScopeDeviceTypeDTO> lstDeviceType = getListUnitsScopeDeviceTypeByCmnoseId(cmnoseId);
    for (CrUnitsScopeDeviceTypeDTO deviceType : lstDeviceType) {
      sTemp += (deviceType.getDeviceTypeName() + " /");
    }
    if (sTemp.endsWith("/")) {
      sTemp = sTemp.substring(0, sTemp.length() - 1);
    }
    return sTemp;
  }

  @Override
  public CrManagerUnitsOfScopeDTO getDetail(Long cmnoseId) {
    Map<String, Object> parameter = new HashMap<>();
    String sqlQuery = SQLBuilder.getSqlQueryById
        (SQLBuilder.SQL_MODULE_Cr_Manager_Units_Of_Scope, "crManagerUnitsOfScope-list");
    sqlQuery += " AND ls.CMNOSE_ID =:CMNOSE_ID ";
    parameter.put("p_leeLocale", I18n.getLocale());
    parameter.put("CMNOSE_ID", cmnoseId);
    List<CrManagerUnitsOfScopeDTO> lst = getNamedParameterJdbcTemplate().query(
        sqlQuery, parameter, BeanPropertyRowMapper.newInstance(CrManagerUnitsOfScopeDTO.class));
    if (lst.isEmpty()) {
      return null;
    }
    CrManagerUnitsOfScopeDTO data = lst.get(0);
    data.setLstCrUnitsScopeDeviceTypeDTO(getListUnitsScopeDeviceTypeByCmnoseId(cmnoseId));
    return data;
  }

  @Override
  public ResultInSideDto deleteCrManagerUnitsOfScope(Long cmnoseId) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    CrManagerUnitsOfScopeEntity entity = getEntityManager().
        find(CrManagerUnitsOfScopeEntity.class, cmnoseId);
    getEntityManager().remove(entity);
    return resultInSideDto;
  }

  @Override
  public Datatable getListDataExport(CrManagerUnitsOfScopeDTO crManagerUnitsOfScopeDTO) {
    BaseDto baseDto = sqlSearch(crManagerUnitsOfScopeDTO);
    Datatable datatable = new Datatable();
    List<CrManagerUnitsOfScopeDTO> lstEx = getNamedParameterJdbcTemplate()
        .query(baseDto.getSqlQuery(), baseDto.getParameters(),
            BeanPropertyRowMapper.newInstance(CrManagerUnitsOfScopeDTO.class));
    datatable.setData(lstEx);
    return getList(datatable);
  }

  @Override
  public ResultInSideDto edit(CrManagerUnitsOfScopeDTO crManagerUnitsOfScopeDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    if (checkDuplicate(crManagerUnitsOfScopeDTO)) {
      resultInSideDto.setKey(Constants.RESULT.DUPLICATE);
      resultInSideDto.setMessage(I18n.getValidation("CrManagerUnitsOfScopeDTO.unique"));
      return resultInSideDto;
    }
    CrManagerUnitsOfScopeEntity update = getEntityManager()
        .merge(crManagerUnitsOfScopeDTO.toModel());
    List<CrUnitsScopeDeviceTypeEntity> lstUnitsScopeDeviceTypeDelete = findByMultilParam(
        CrUnitsScopeDeviceTypeEntity.class, "crUnitsScopeId",
        crManagerUnitsOfScopeDTO.getCmnoseId());
    if (!lstUnitsScopeDeviceTypeDelete.isEmpty() && lstUnitsScopeDeviceTypeDelete.size() > 0) {
      for (CrUnitsScopeDeviceTypeEntity entity : lstUnitsScopeDeviceTypeDelete) {
        crUnitsScopeDeviceTypeRepository.delete(entity.getId());
      }
    }
    if (crManagerUnitsOfScopeDTO.getLstCrUnitsScopeDeviceTypeDTO() != null
        && crManagerUnitsOfScopeDTO.getLstCrUnitsScopeDeviceTypeDTO().size() > 0) {
      for (CrUnitsScopeDeviceTypeDTO dto : crManagerUnitsOfScopeDTO
          .getLstCrUnitsScopeDeviceTypeDTO()) {
        dto.setCrUnitsScopeId(update.getCmnoseId());
        resultInSideDto = crUnitsScopeDeviceTypeRepository.addCrUnitsScopeDeviceType(dto);
      }
    }
    resultInSideDto.setId(update.getCmnoseId());
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto add(CrManagerUnitsOfScopeDTO crManagerUnitsOfScopeDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    if (checkDuplicate(crManagerUnitsOfScopeDTO)) {
      resultInSideDto.setKey(Constants.RESULT.DUPLICATE);
      resultInSideDto.setMessage(I18n.getValidation("CrManagerUnitsOfScopeDTO.unique"));
      return resultInSideDto;
    }
    CrManagerUnitsOfScopeEntity add = getEntityManager()
        .merge(crManagerUnitsOfScopeDTO.toModel());
    if (crManagerUnitsOfScopeDTO.getLstCrUnitsScopeDeviceTypeDTO() != null
        && crManagerUnitsOfScopeDTO.getLstCrUnitsScopeDeviceTypeDTO().size() > 0) {
      for (CrUnitsScopeDeviceTypeDTO dto : crManagerUnitsOfScopeDTO
          .getLstCrUnitsScopeDeviceTypeDTO()) {
        dto.setCrUnitsScopeId(add.getCmnoseId());
        resultInSideDto = crUnitsScopeDeviceTypeRepository.addCrUnitsScopeDeviceType(dto);
      }
    }
    resultInSideDto.setId(add.getCmnoseId());
    return resultInSideDto;
  }

  @Override
  public List<ItemDataCRInside> getListDeviceTypeByImpactSegmentCBB(Long impactSegmentId) {
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_Cr_Manager_Units_Of_Scope,
            "getListDeviceTypeByImpactSegmentCBB");
    Map<String, Object> parameter = new HashMap<>();
    if (impactSegmentId != null && impactSegmentId > 0) {
      sqlQuery += " and dts.device_type_id in ( select cps.device_type_id from cr_process cps where 1=1 ";
      sqlQuery += " and cps.impact_segment_id =:impactSegmentId ";
      sqlQuery += " and not exists (select 1 from cr_process where parent_id = cps.cr_process_id)) ";
      parameter.put("impactSegmentId", impactSegmentId);
    }
    List<ItemDataCRInside> ListDeviceTypeByImpactSegmentCBB = getNamedParameterJdbcTemplate()
        .query(sqlQuery, parameter, BeanPropertyRowMapper.newInstance(ItemDataCRInside.class));
    return ListDeviceTypeByImpactSegmentCBB;
  }

  @Override
  public List<ImpactSegmentDTO> getListImpactSegmentCBB() {
    List<ImpactSegmentEntity> lstEntity = findByMultilParam(ImpactSegmentEntity.class, "isActive",
        1L);
    List<ImpactSegmentDTO> lstDTO = new ArrayList<>();
    for (ImpactSegmentEntity entity : lstEntity) {
      lstDTO.add(entity.toDTO());
    }
    return lstDTO;
  }

  private List<CrUnitsScopeDeviceTypeDTO> getListUnitsScopeDeviceTypeByCmnoseId(Long cmnoseId) {
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_Cr_Manager_Units_Of_Scope,
            "getListUnitsScopeDeviceTypeByCmnoseId");
    Map<String, Object> parameter = new HashMap<>();
    parameter.put("cmnoseId", cmnoseId);
    List<CrUnitsScopeDeviceTypeDTO> lstCrUnitsScopeDeviceTypeDTO = getNamedParameterJdbcTemplate()
        .query(sqlQuery, parameter,
            BeanPropertyRowMapper.newInstance(CrUnitsScopeDeviceTypeDTO.class));
    try {
      String locale = I18n.getLocale();
      Map<String, Object> map = getSqlLanguageExchange(Constants.LANGUAGUE_EXCHANGE_SYSTEM.CR_MR,
          Constants.APPLIED_BUSSINESS.DEVICE_TYPES, locale);
      String sqlLanguage = (String) map.get("sql");
      Map mapParam = (Map) map.get("mapParam");
      Map mapType = (Map) map.get("mapType");
      List<LanguageExchangeDTO> lstLanguage = languageExchangeRepository
          .findBySql(sqlLanguage, mapParam, mapType, LanguageExchangeDTO.class);
      lstCrUnitsScopeDeviceTypeDTO = setLanguage(lstCrUnitsScopeDeviceTypeDTO, lstLanguage,
          "deviceTypeId", "deviceTypeName");
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return lstCrUnitsScopeDeviceTypeDTO;
  }

  private boolean checkLong(Long value) {
    if (value != null && value > 0) {
      return true;
    }
    return false;
  }

  private boolean checkDuplicate(CrManagerUnitsOfScopeDTO crManagerUnitsOfScopeDTO) {
    if (checkLong(crManagerUnitsOfScopeDTO.getCmseId()) && checkLong(
        crManagerUnitsOfScopeDTO.getUnitId()) && !checkLong(
        crManagerUnitsOfScopeDTO.getCrTypeId())) {
      List<CrManagerUnitsOfScopeEntity> lst = findByMultilParam(CrManagerUnitsOfScopeEntity.class,
          "cmseId", crManagerUnitsOfScopeDTO.getCmseId(), "unitId",
          crManagerUnitsOfScopeDTO.getUnitId());
      CrManagerUnitsOfScopeDTO dto = getDetail(crManagerUnitsOfScopeDTO.getCmnoseId());
      if (dto != null && String.valueOf(dto.getCmseId())
          .equals(String.valueOf(crManagerUnitsOfScopeDTO.getCmseId())) && String.valueOf(dto
          .getUnitId()).equals(String.valueOf(crManagerUnitsOfScopeDTO.getUnitId()))) {
        return false;
      } else {
        if (!lst.isEmpty() && lst.size() > 0) {
          return true;
        }
      }
    } else if (checkLong(crManagerUnitsOfScopeDTO.getCmseId()) && checkLong(
        crManagerUnitsOfScopeDTO.getUnitId()) && checkLong(
        crManagerUnitsOfScopeDTO.getCrTypeId())) {
      List<CrManagerUnitsOfScopeEntity> lst = findByMultilParam(CrManagerUnitsOfScopeEntity.class,
          "cmseId", crManagerUnitsOfScopeDTO.getCmseId(), "unitId",
          crManagerUnitsOfScopeDTO.getUnitId(), "crTypeId",
          crManagerUnitsOfScopeDTO.getCrTypeId());
      CrManagerUnitsOfScopeDTO dto = getDetail(crManagerUnitsOfScopeDTO.getCmnoseId());
      if (dto != null && String.valueOf(dto.getCmseId())
          .equals(String.valueOf(crManagerUnitsOfScopeDTO.getCmseId())) && String.valueOf(dto
          .getUnitId()).equals(String.valueOf(crManagerUnitsOfScopeDTO.getUnitId())) && String
          .valueOf(dto.getCrTypeId())
          .equals(String.valueOf(crManagerUnitsOfScopeDTO.getCrTypeId()))) {
        return false;
      } else {
        if (!lst.isEmpty() && lst.size() > 0) {
          return true;
        }
      }
    }
    return false;
  }
}
