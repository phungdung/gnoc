package com.viettel.gnoc.commons.repository;


import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.GnocLanguageDto;
import com.viettel.gnoc.commons.dto.LanguageExchangeDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.model.LanguageExchangeEntity;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

/**
 * @author NamTN
 */
@SuppressWarnings("rawtypes")
@Repository
@Slf4j
public class LanguageExchangeRepositoryImpl extends BaseRepository implements
    LanguageExchangeRepository {

  @Override
  public List<LanguageExchangeDTO> findBySql(String sql, Map<String, String> mapParam,
      Map<String, String> mapType, Class type) {
    List<LanguageExchangeDTO> exchangeDTOList = getNamedParameterJdbcTemplate()
        .query(sql, mapParam, BeanPropertyRowMapper.newInstance(LanguageExchangeDTO.class));
    return exchangeDTOList;
  }

  @Override
  public Map<String, Object> mapLanguageExchange(String leelocale, String mySystem,
      String myBussiness) {
    return getSqlLanguageExchange(mySystem, myBussiness, leelocale);
  }

  @Override
  public List<LanguageExchangeDTO> getListLanguageExchangeById(String system, String bussiness,
      Long bussinessId, String leeLocale) {
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_COMMON, "get-list-language-exchange-by-id");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("p_system", system);
    parameters.put("p_bussiness", bussiness);
    parameters.put("p_bussinessId", bussinessId);
    parameters.put("p_leeLocale", leeLocale);
    return getNamedParameterJdbcTemplate().query(sqlQuery, parameters,
        BeanPropertyRowMapper.newInstance(LanguageExchangeDTO.class));
  }

  @Override
  public List<CatItemDTO> getListTableBySystem(String systemName) {
    String sqlQuery = "select tablez.* from common_gnoc.cat_item schemas \n"
        + "join common_gnoc.cat_item tablez\n"
        + "on tablez.item_code like schemas.item_code || '%'\n"
        + "where schemas.CATEGORY_ID = 263 and tablez.CATEGORY_ID = 262 and schemas.item_code = :systemCode";
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("systemCode", systemName);
    return getNamedParameterJdbcTemplate().query(sqlQuery, parameters,
        BeanPropertyRowMapper.newInstance(CatItemDTO.class));
  }

  @Override
  public Datatable getListLanguageExchange(LanguageExchangeDTO dto) {
    BaseDto baseDto = sqlSearch(dto);
    return getListDataTableBySqlQuery(baseDto.getSqlQuery(),
        baseDto.getParameters(),
        dto.getPage(), dto.getPageSize(),
        LanguageExchangeDTO.class,
        dto.getSortName(), dto.getSortType());
  }

  @Override
  public List<LanguageExchangeDTO> getDataExport(LanguageExchangeDTO dto) {
    BaseDto baseDto = sqlSearch(dto);
    return getNamedParameterJdbcTemplate().query(baseDto.getSqlQuery(), baseDto.getParameters(),
        BeanPropertyRowMapper.newInstance(LanguageExchangeDTO.class));
  }

  @Override
  public List<GnocLanguageDto> getListLanguage() {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_COMMON_GNOC_LANGUAGE, "get-list-gnoc-language");

    return getNamedParameterJdbcTemplate().query(sql,
        BeanPropertyRowMapper.newInstance(GnocLanguageDto.class));
  }


  @Override
  public LanguageExchangeDTO getDetailLanguageExchange(Long langExchangeId) {
    LanguageExchangeEntity languageExchangeEntity = getEntityManager()
        .find(LanguageExchangeEntity.class, langExchangeId);
    LanguageExchangeDTO languageExchangeDTO = languageExchangeEntity.toDTO();
    return languageExchangeDTO;
  }

  @Override
  public ResultInSideDto updateLanguageExchange(LanguageExchangeDTO languageExchangeDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    LanguageExchangeEntity languageExchangeEntity = getEntityManager()
        .merge(languageExchangeDTO.toEntity());
    resultInSideDto.setId(languageExchangeEntity.getLeeId());
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto insertLanguageExchange(LanguageExchangeDTO languageExchangeDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    LanguageExchangeEntity languageExchangeEntity = getEntityManager()
        .merge(languageExchangeDTO.toEntity());
    resultInSideDto.setId(languageExchangeEntity.getLeeId());
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto saveListLanguageExchangeImport(
      List<LanguageExchangeDTO> languageExchangeDTOS) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    try {
      Map<String, List<LanguageExchangeDTO>> mapFilter = languageExchangeDTOS
          .stream()
          .map(o -> new AbstractMap.SimpleEntry<>(
              o.getAppliedSystem() + "_" + o.getAppliedBussiness(), o))
          .collect(Collectors.groupingBy(Map.Entry::getKey,
              Collectors.mapping(Map.Entry::getValue, Collectors.toList())));
      for (Map.Entry<String, List<LanguageExchangeDTO>> entry : mapFilter.entrySet()) {
        String key = entry.getKey();//cặp "hệ thống - bảng"
        log.info(key);
        List<LanguageExchangeDTO> lstValue = entry.getValue();
        List<LanguageExchangeEntity> lstInsertUpdate = new ArrayList<>();
        for (LanguageExchangeDTO item : lstValue) {
          item.setBussinessId(Long.valueOf(item.getBusinessIdImport()));
          List<LanguageExchangeEntity> lstExist = findByMultilParam(LanguageExchangeEntity.class
              , "appliedSystem", item.getAppliedSystem()
              , "appliedBussiness", item.getAppliedBussiness()
              , "leeLocale", item.getLeeLocale()
          );

          LanguageExchangeEntity languageExchangeEntity = new LanguageExchangeEntity();
          languageExchangeEntity.setAppliedSystem(item.getAppliedSystem());
          languageExchangeEntity.setAppliedBussiness(item.getAppliedBussiness());
          languageExchangeEntity.setBussinessId(item.getBussinessId());
          languageExchangeEntity.setBussinessCode(item.getBussinessCode());
          languageExchangeEntity.setLeeLocale(item.getLeeLocale());
          languageExchangeEntity.setLeeValue(item.getLeeValue());

          if (lstExist != null && !lstExist.isEmpty()) {
            Optional<LanguageExchangeEntity> opt = lstExist.stream()
                .filter(o -> item.getBussinessId().toString().equals(o.getBussinessId().toString()))
                .findAny();
            if (opt.isPresent()) {
              languageExchangeEntity.setLeeId(opt.get().getLeeId());
            }
          }
          lstInsertUpdate.add(languageExchangeEntity);
        }
        if (!lstInsertUpdate.isEmpty()) {
          for (LanguageExchangeEntity entity : lstInsertUpdate) {
            getEntityManager().merge(entity);
          }
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto deleteLanguageExchangeById(Long id) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    LanguageExchangeEntity languageExchangeEntity = getEntityManager()
        .find(LanguageExchangeEntity.class, id);
    if (languageExchangeEntity != null) {
      getEntityManager().remove(languageExchangeEntity);
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto saveListLanguageExchange(String system, String bussiness, Long bussinessId,
      List<LanguageExchangeDTO> languageExchangeDTOS) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_COMMON, "get-config-language-exchange");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("p_system", system);
    parameters.put("p_bussiness", bussiness);
    List<CatItemDTO> catItemDTOS = getNamedParameterJdbcTemplate().query(sqlQuery, parameters,
        BeanPropertyRowMapper.newInstance(CatItemDTO.class));
    Map<String, Long> mapCatItem = new HashMap<>();
    for (CatItemDTO catItemDTO : catItemDTOS) {
      mapCatItem.put(catItemDTO.getItemCode(), Long.valueOf(catItemDTO.getItemValue()));
    }
    deleteByMultilParam(LanguageExchangeEntity.class,
        "appliedSystem", mapCatItem.get(system),
        "appliedBussiness", mapCatItem.get(bussiness),
        "bussinessId", bussinessId);
    List<LanguageExchangeDTO> lstLanguageExchangeDTO = new ArrayList<>();
    for (LanguageExchangeDTO languageExchangeDTO : languageExchangeDTOS) {
      if (StringUtils.isNotNullOrEmpty(languageExchangeDTO.getLeeValue())) {
        LanguageExchangeEntity languageExchangeEntity = new LanguageExchangeEntity();
        languageExchangeEntity.setAppliedSystem(mapCatItem.get(system));
        languageExchangeEntity.setAppliedBussiness(mapCatItem.get(bussiness));
        languageExchangeEntity.setBussinessId(bussinessId);
        languageExchangeEntity.setBussinessCode(languageExchangeDTO.getBussinessCode());
        languageExchangeEntity.setLeeLocale(languageExchangeDTO.getLeeLocale());
        languageExchangeEntity.setLeeValue(languageExchangeDTO.getLeeValue());
        LanguageExchangeDTO temp = getEntityManager().merge(languageExchangeEntity).toDTO();
        lstLanguageExchangeDTO.add(temp);
      }
    }
    resultInSideDto.setObject(lstLanguageExchangeDTO);
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto deleteListLanguageExchange(String system, String bussiness,
      Long bussinessId) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_COMMON, "get-config-language-exchange");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("p_system", system);
    parameters.put("p_bussiness", bussiness);
    List<CatItemDTO> catItemDTOS = getNamedParameterJdbcTemplate().query(sqlQuery, parameters,
        BeanPropertyRowMapper.newInstance(CatItemDTO.class));
    Map<String, Long> mapCatItem = new HashMap<>();
    for (CatItemDTO catItemDTO : catItemDTOS) {
      mapCatItem.put(catItemDTO.getItemCode(), Long.valueOf(catItemDTO.getItemValue()));
    }
    deleteByMultilParam(LanguageExchangeEntity.class,
        "appliedSystem", mapCatItem.get(system),
        "appliedBussiness", mapCatItem.get(bussiness),
        "bussinessId", bussinessId);
    return resultInSideDto;
  }

  @Override
  public LanguageExchangeDTO checkLanguageExchangeExist(Long appliedSystem, Long appliedBussiness,
      Long bussinessId,
      String leeLocale) {

    List<LanguageExchangeEntity> dataEntity = (List<LanguageExchangeEntity>) findByMultilParam(
        LanguageExchangeEntity.class,
        "appliedSystem", appliedSystem,
        "appliedBussiness", appliedBussiness,
        "bussinessId", bussinessId,
        "leeLocale", leeLocale);
    if (dataEntity != null && dataEntity.size() > 0) {
      return dataEntity.get(0).toDTO();
    }
    return null;
  }


  public BaseDto sqlSearch(LanguageExchangeDTO dto) {
    BaseDto baseDto = new BaseDto();
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_LANGUAGE_EXCHANGE, "getListLanguageExchange");
    Map<String, Object> parameters = new HashMap<>();
    if (dto != null) {
      if (!StringUtils.isStringNullOrEmpty(dto.getSearchAll())) {
        sqlQuery += " AND (LOWER(LE.LEE_VALUE) LIKE :searchAll ESCAPE '\\' OR to_char(LE.BUSSINESS_ID) =:searchAllId)";
        parameters
            .put("searchAll",
                StringUtils.convertLowerParamContains(dto.getSearchAll()));
        parameters
            .put("searchAllId", dto.getSearchAll());
      }
      if (dto.getBussinessId() != null) {
        sqlQuery += " and LE.BUSSINESS_ID = :businessId";
        parameters.put("businessId", dto.getBussinessId());
      }
      if (dto.getLeeLocale() != null) {
        sqlQuery += " and LE.LEE_LOCALE = :p_leeLocale ";
        parameters.put("p_leeLocale", dto.getLeeLocale());
      }

      if (!StringUtils.isStringNullOrEmpty(dto.getLeeValue())) {
        sqlQuery += " AND (LOWER(LE.LEE_VALUE) LIKE :leeValue ESCAPE '\\') ";
        parameters.put("leeValue", StringUtils.convertLowerParamContains(dto.getLeeValue()));
      }

      if (dto.getAppliedSystem() != null) {
        sqlQuery += " and LE.APPLIED_SYSTEM = :appliedSystem";
        parameters.put("appliedSystem", dto.getAppliedSystem());
      }

      if (dto.getAppliedBussiness() != null) {
        sqlQuery += " and LE.APPLIED_BUSSINESS = :appliedBussiness";
        parameters.put("appliedBussiness", dto.getAppliedBussiness());
      }
    }

    sqlQuery += " order by LE.LEE_ID desc ";
    baseDto.setSqlQuery(sqlQuery);
    baseDto.setParameters(parameters);
    return baseDto;
  }

  public List<LanguageExchangeDTO> getListExchangeNotConfig(
      LanguageExchangeDTO languageExchangeDTO) {
    List<LanguageExchangeDTO> list = new ArrayList<LanguageExchangeDTO>();
    Map<String, Object> parameters = new HashMap<>();
    try {
      String sql = "";
      String sqlTemp = "select DESCRIPTION leeLocale from COMMON_GNOC.CAT_ITEM where ITEM_CODE =:itemCode";
      parameters.put("itemCode", languageExchangeDTO.getBussinessCode());
      list = getNamedParameterJdbcTemplate()
          .query(sqlTemp, parameters,
              BeanPropertyRowMapper.newInstance(LanguageExchangeDTO.class));
      if (list != null && !list.isEmpty()) {
        sql = list.get(0).getLeeLocale();
      }
      if (StringUtils.isNotNullOrEmpty(sql)) {
        parameters = new HashMap<>();
        sql = sql + " ( select BUSSINESS_ID from COMMON_GNOC.LANGUAGE_EXCHANGE WHERE  "
            + "APPLIED_SYSTEM = :appliedSystemPa "
            + "AND APPLIED_BUSSINESS = :appliedBussinessPa ) ";
        parameters.put("appliedSystemPa", languageExchangeDTO.getAppliedSystem() == null ? -1L
            : languageExchangeDTO.getAppliedSystem());
        parameters.put("appliedBussinessPa",
            languageExchangeDTO.getAppliedBussiness() == null ? -1L
                : languageExchangeDTO.getAppliedBussiness());
        list = getNamedParameterJdbcTemplate()
            .query(sql, parameters,
                BeanPropertyRowMapper.newInstance(LanguageExchangeDTO.class));
        if (list != null && !list.isEmpty()) {
          for (LanguageExchangeDTO exchangeDTO : list) {
            exchangeDTO.setAppliedBusinessName(languageExchangeDTO.getBussinessCode());
          }
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return list;
  }

  @Override
  public Map<String, String> translateMe(String myLanguage, String mySystem, String myBussiness,
      String myOption) {
    Map<String, String> mapOfMyWords = new HashMap<>();
    if (myOption == null) {
      return mapOfMyWords;
    }
    if (myOption.trim().equals(Constants.TRANSLATE_OPTION.ID)) {
      return translateMeByIdOrCode(myLanguage, mySystem, myBussiness, true);
    }
    if (myOption.trim().equals(Constants.TRANSLATE_OPTION.CODE)) {
      return translateMeByIdOrCode(myLanguage, mySystem, myBussiness, false);
    }
    return mapOfMyWords;
  }

  public Map<String, String> translateMeByIdOrCode(
      String myLanguage,
      String mySystem,
      String myBussiness, boolean checkById) {
    Map<String, String> mapOfMyWords = new HashMap<>();
    try {
      if (myLanguage == null || "".equals(myLanguage.trim())) {
        return mapOfMyWords;
      }
      if (mySystem == null || "".equals(mySystem.trim())) {
        return mapOfMyWords;
      }
      if (myBussiness == null || "".equals(myBussiness.trim())) {
        return mapOfMyWords;
      }
      String sql = " select LEE_VALUE as leeValue, "
          + " BUSSINESS_ID as bussinessId "
          + " BUSSINESS_CODE as bussinessCode "
          + " from LANGUAGE_EXCHANGE "
          + " where APPLIED_BUSSINESS =:appliedBussiness "
          + " and APPLIED_SYSTEM =:appliedSystem "
          + " and LEE_LOCALE = :lee_Locale ";
      Map<String, Object> parameters = new HashMap<>();
      parameters.put("appliedBussiness", myBussiness);
      parameters.put("appliedSystem", mySystem);
      parameters.put("lee_Locale", myLanguage);
      List<LanguageExchangeDTO> lst = getNamedParameterJdbcTemplate()
          .query(sql, parameters, BeanPropertyRowMapper.newInstance(LanguageExchangeDTO.class));
      if (lst != null && !lst.isEmpty()) {
        for (LanguageExchangeDTO languageExchangeDTO : lst) {
          String temp = null;
          if (checkById) {
            temp =
                languageExchangeDTO.getBussinessId() != null ? languageExchangeDTO.getBussinessId()
                    .toString() : null;
          } else {
            temp = languageExchangeDTO.getBussinessCode();
          }
          if (temp != null && mapOfMyWords.get(temp) == null) {
            mapOfMyWords.put(temp, languageExchangeDTO.getLeeValue());
          }
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return mapOfMyWords;
  }
}
