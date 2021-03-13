package com.viettel.gnoc.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.incident.dto.CfgMapNetLevelIncTypeDTO;
import com.viettel.gnoc.incident.model.CfgMapNetLevelIncTypeEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class CfgMapNetLevelIncTypeRepositoryImpl extends BaseRepository implements
    CfgMapNetLevelIncTypeRepository {

  @Override
  public List<CfgMapNetLevelIncTypeDTO> getListCfgMapNetLevelIncTypeDTO(
      CfgMapNetLevelIncTypeDTO cfgMapNetLevelIncTypeDTO, int rowStart, int maxRow, String sortType,
      String sortFieldList) {
    return onSearchEntity(CfgMapNetLevelIncTypeEntity.class, cfgMapNetLevelIncTypeDTO, rowStart,
        maxRow, sortType, sortFieldList);
  }

  public String getSQL(CfgMapNetLevelIncTypeDTO cfgMapNetLevelIncTypeDTO,
      Map<String, Object> parameters) {
    String sqlQuery =
        "select cg.ID id, cg.TROUBLE_TYPE_ID troubleTypeId, cg.NETWORK_LEVEL_ID networkLevelId,"
            + "cg.NETWORK_LEVEL_NAME networkLevelName, cg.TROUBLE_TYPE_NAME troubleTypeName from ONE_TM.CFG_MAP_NET_LEVEL_INC_TYPE cg where 1=1 ";

    if (StringUtils.isNotNullOrEmpty(cfgMapNetLevelIncTypeDTO.getSearchAll())) {
      sqlQuery += " AND cg.TROUBLE_TYPE_ID = :searchAll  ";
      parameters
          .put("searchAll", cfgMapNetLevelIncTypeDTO.getSearchAll());
    }

    if (cfgMapNetLevelIncTypeDTO.getTroubleTypeId() != null) {
      sqlQuery += " AND cg.TROUBLE_TYPE_ID = :troubleId  ";
      parameters
          .put("troubleId", cfgMapNetLevelIncTypeDTO.getTroubleTypeId());
    }

    if (!StringUtils.isStringNullOrEmpty(cfgMapNetLevelIncTypeDTO.getNetworkLevelId())) {
      sqlQuery += " and lower(cg.NETWORK_LEVEL_ID) LIKE :netWork ESCAPE '\\' ";
      parameters.put("netWork",
          StringUtils.convertLowerParamContains(cfgMapNetLevelIncTypeDTO.getNetworkLevelId()));
    }

    sqlQuery += " order by cg.id asc";
    return sqlQuery;
  }

  @Override
  public Datatable getListCfgMapNetLevelIncTypeDatatable(
      CfgMapNetLevelIncTypeDTO cfgMapNetLevelIncTypeDTO) {
    Map<String, Object> parameters = new HashMap<>();
    String sqlQuery = getSQL(cfgMapNetLevelIncTypeDTO, parameters);
    Datatable datatable = getListDataTableBySqlQuery(sqlQuery, parameters,
        cfgMapNetLevelIncTypeDTO.getPage(), cfgMapNetLevelIncTypeDTO.getPageSize(),
        CfgMapNetLevelIncTypeDTO.class,
        cfgMapNetLevelIncTypeDTO.getSortName(), cfgMapNetLevelIncTypeDTO.getSortType());

//    String system = Constants.LANGUAGUE_EXCHANGE_SYSTEM.COMMON;
//    String business = Constants.COMMON_TRANSLATE_BUSINESS.CATEGORY.toString();
//
//    List<CfgMapNetLevelIncTypeDTO> lst = (List<CfgMapNetLevelIncTypeDTO>) datatable.getData();
//    if (StringUtils.isStringNullOrEmpty(system)) {
//      system = LANGUAGUE_EXCHANGE_SYSTEM.COMMON;
//    }
//    if (StringUtils.isStringNullOrEmpty(business)) {
//      business = APPLIED_BUSSINESS.CAT_ITEM.toString();
//    }
//    List<LanguageExchangeDTO> lstLanguage = getLanguageExchange(
//        system,
//        business);
//
//    try {
//      lst = setLanguage(lst, lstLanguage, "2", "TROUBLE_TYPE_ID");
//      datatable.setData(lst);
//    } catch (Exception e) {
//      log.error(e.getMessage(), e);
//    }
    return datatable;
  }

  public ResultInSideDto insertOrUpdate(CfgMapNetLevelIncTypeDTO cfgMapNetLevelIncTypeDTO) {
    ResultInSideDto resultDto = new ResultInSideDto();
    CfgMapNetLevelIncTypeEntity entity = getEntityManager()
        .merge(cfgMapNetLevelIncTypeDTO.toEntity());
    resultDto.setId(entity.getId());

    resultDto.setKey(Constants.RESULT.SUCCESS);
    return resultDto;
  }

  @Override
  public ResultInSideDto updateCfgMapNetLevelIncType(
      CfgMapNetLevelIncTypeDTO cfgMapNetLevelIncTypeDTO) {
    return insertOrUpdate(cfgMapNetLevelIncTypeDTO);
  }

  @Override
  public ResultInSideDto deleteCfgMapNetLevelIncType(Long id) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    CfgMapNetLevelIncTypeEntity entity = getEntityManager()
        .find(CfgMapNetLevelIncTypeEntity.class, id);
    if (entity != null) {
      getEntityManager().remove(entity);
      resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    } else {
      resultInSideDto.setKey(Constants.RESULT.NODATA);
    }
    return resultInSideDto;
  }

  @Override
  public CfgMapNetLevelIncTypeDTO findCfgMapNetLevelIncTypeById(Long id) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    CfgMapNetLevelIncTypeEntity entity = getEntityManager()
        .find(CfgMapNetLevelIncTypeEntity.class, id);
    if (entity != null) {
      return entity.toDTO();
    }

    return null;
  }

  @Override
  public ResultInSideDto insertCfgMapNetLevelIncType(
      CfgMapNetLevelIncTypeDTO cfgMapNetLevelIncTypeDTO) {
    return insertOrUpdate(cfgMapNetLevelIncTypeDTO);
  }

}
