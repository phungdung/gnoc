package com.viettel.gnoc.wo.repository;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.wo.dto.CfgSupportCaseDTO;
import com.viettel.gnoc.wo.dto.CfgSupportCaseTestDTO;
import com.viettel.gnoc.wo.model.CfgSupportCaseEntity;
import com.viettel.gnoc.wo.model.CfgSupportCaseTestEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class CfgSupportCaseRepositoryImpl extends BaseRepository implements
    CfgSupportCaseRepository {

  private final static String DATA_EXPORT = "DATA_EXPORT";

  @Autowired
  CfgSupportCaseTestRepository cfgSupportCaseTestRepository;

  @Override
  public ResultInSideDto add(CfgSupportCaseDTO cfgSupportCaseDTO) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
    CfgSupportCaseEntity cfgSupportCaseEntity = getEntityManager()
        .merge(cfgSupportCaseDTO.toEntity());
    Long idTmp = cfgSupportCaseEntity.getId();
    List<CfgSupportCaseTestDTO> listCfgTmp = cfgSupportCaseDTO.getCfgSupportCaseTestListDTO();
    if (listCfgTmp != null) {
      for (CfgSupportCaseTestDTO cfgSupportCaseTestDTO : listCfgTmp) {
        cfgSupportCaseTestDTO.setCfgSuppportCaseId(idTmp);
        resultInSideDTO = cfgSupportCaseTestRepository.add(cfgSupportCaseTestDTO);
      }
    }
    resultInSideDTO.setId(idTmp);
    return resultInSideDTO;
  }

  @Override
  public ResultInSideDto edit(CfgSupportCaseDTO cfgSupportCaseDTO) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
    getEntityManager().merge(cfgSupportCaseDTO.toEntity());
    List<CfgSupportCaseTestDTO> listCfgTmp = cfgSupportCaseDTO.getCfgSupportCaseTestListDTO();
    if (listCfgTmp != null) {
      for (CfgSupportCaseTestDTO cfgSupportCaseTestDTO : listCfgTmp) {
        if (cfgSupportCaseTestDTO.getId() != null && cfgSupportCaseTestDTO.getIsDelete()) {
          cfgSupportCaseTestRepository.delete(cfgSupportCaseTestDTO.getId());
        } else {
          CfgSupportCaseTestDTO supportCaseTestTmp = cfgSupportCaseTestRepository
              .checkCfgSupportCaseTestExist(cfgSupportCaseTestDTO.getCfgSuppportCaseId(),
                  cfgSupportCaseTestDTO.getTestCaseName(), cfgSupportCaseTestDTO.getFileRequired());
          if (supportCaseTestTmp != null) {
            supportCaseTestTmp.setTestCaseName(cfgSupportCaseTestDTO.getTestCaseName());
            supportCaseTestTmp.setFileRequired(cfgSupportCaseTestDTO.getFileRequired());
            resultInSideDTO = cfgSupportCaseTestRepository.edit(supportCaseTestTmp);
          } else {
            cfgSupportCaseTestDTO.setCfgSuppportCaseId(cfgSupportCaseDTO.getId());
            resultInSideDTO = cfgSupportCaseTestRepository.add(cfgSupportCaseTestDTO);
          }
        }
      }
    }
    return resultInSideDTO;
  }

  @Override
  public CfgSupportCaseDTO getDetail(Long id) {
    CfgSupportCaseEntity dataEntity = getEntityManager().find(CfgSupportCaseEntity.class, id);
    if (dataEntity != null) {
      CfgSupportCaseDTO typeDTO = dataEntity.toDTO();
      List<CfgSupportCaseTestDTO> listTmp = cfgSupportCaseTestRepository
          .getListCfgSupportCaseTestId(id);
      if (listTmp != null) {
        typeDTO.setCfgSupportCaseTestListDTO(listTmp);
      }
      return typeDTO;
    }
    return null;
  }


  @Override
  public ResultInSideDto deleteCaseAndCaseTest(Long id) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
    CfgSupportCaseEntity odTypeEntity = getEntityManager().find(CfgSupportCaseEntity.class, id);
    getEntityManager().remove(odTypeEntity);
    if (Constants.RESULT.SUCCESS.equals(resultInSideDTO.getKey())) {
      List<CfgSupportCaseTestEntity> list = findByMultilParam(CfgSupportCaseTestEntity.class,
          "cfgSuppportCaseId", id);
      if (list != null && list.size() > 0) {
        for (CfgSupportCaseTestEntity caseEntity : list) {
          getEntityManager().remove(caseEntity);
        }
      }
    }
    return resultInSideDTO;
  }

  @Override
  public Datatable getListCfgSupportCaseDTONew(CfgSupportCaseDTO cfgSupportCaseDTO) {
    BaseDto baseDto = sqlGetListCfgSupportCaseDTONew(cfgSupportCaseDTO, null);
    return getListDataTableBySqlQuery(baseDto.getSqlQuery(),
        baseDto.getParameters(),
        cfgSupportCaseDTO.getPage(), cfgSupportCaseDTO.getPageSize(),
        CfgSupportCaseDTO.class,
        cfgSupportCaseDTO.getSortName(), cfgSupportCaseDTO.getSortType());
  }

  @Override
  public List<CfgSupportCaseDTO> getListCfgSupportCaseExport(CfgSupportCaseDTO cfgSupportCaseDTO) {
    BaseDto baseDto = sqlGetListCfgSupportCaseDTONew(cfgSupportCaseDTO, DATA_EXPORT);
    return getNamedParameterJdbcTemplate()
        .query(baseDto.getSqlQuery(), baseDto.getParameters(),
            BeanPropertyRowMapper.newInstance(CfgSupportCaseDTO.class));
  }

  public BaseDto sqlGetListCfgSupportCaseDTONew(CfgSupportCaseDTO cfgSupportCaseDTO,
      String action) {
    BaseDto baseDto = new BaseDto();
    Map<String, Object> parameters = new HashMap<>();
    String sqlQuery;
    if (DATA_EXPORT.equals(action)) {
      sqlQuery = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_CFG_SUPPORT_CASE,
          "get-list-cfg-support-case-export");
    } else {
      sqlQuery = SQLBuilder
          .getSqlQueryById(SQLBuilder.SQL_MODULE_CFG_SUPPORT_CASE, "get-list-cfg-support-case-new");
    }
    if (StringUtils.isNotNullOrEmpty(cfgSupportCaseDTO.getSearchAll())) {
      sqlQuery += " AND LOWER(a.CASE_NAME) LIKE :searchAll ESCAPE '\\'";
      parameters
          .put("searchAll",
              StringUtils.convertLowerParamContains(cfgSupportCaseDTO.getSearchAll()));
    }
    if (cfgSupportCaseDTO.getCaseName() != null && !"".equals(cfgSupportCaseDTO.getCaseName())) {
      sqlQuery += "AND LOWER(a.CASE_NAME) LIKE :caseName ESCAPE '\\' ";
      parameters
          .put("caseName", StringUtils.convertLowerParamContains(cfgSupportCaseDTO.getCaseName()));
    }
    if (cfgSupportCaseDTO.getServiceID() != null) {
      sqlQuery += " and a.SERVICE_ID=:serviceId";
      parameters.put("serviceId", cfgSupportCaseDTO.getServiceID());
    }
    if (cfgSupportCaseDTO.getInfraTypeID() != null) {
      sqlQuery += " and llex.ITEM_VALUE=:infraTypeId";
      parameters.put("infraTypeId", cfgSupportCaseDTO.getInfraTypeID().toString());
    }
    parameters.put("p_leeLocale", I18n.getLocale());
    sqlQuery += " ORDER BY CASE_NAME ASC";
    baseDto.setSqlQuery(sqlQuery);
    baseDto.setParameters(parameters);
    return baseDto;
  }
}
