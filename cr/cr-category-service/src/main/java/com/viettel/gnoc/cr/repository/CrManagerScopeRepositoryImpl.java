package com.viettel.gnoc.cr.repository;


import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.CrManagerScopeDTO;
import com.viettel.gnoc.cr.model.CrManagerScopeEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class CrManagerScopeRepositoryImpl extends BaseRepository implements
    CrManagerScopeRepository {


  @Override
  public ResultInSideDto insertCrManagerScope(CrManagerScopeDTO crManagerScopeDTO) {
    return insertByModel(crManagerScopeDTO.toEntity(), colId);
  }

  @Override
  public ResultInSideDto updateCrManagerScope(CrManagerScopeDTO crManagerScopeDTO) {
    ResultInSideDto resultDto = new ResultInSideDto();
    resultDto.setKey(RESULT.SUCCESS);
    resultDto.setId(crManagerScopeDTO.getCmseId());
    getEntityManager().merge(crManagerScopeDTO.toEntity());
    return resultDto;
  }

  @Override
  public ResultInSideDto deleteCrManagerScope(Long id) {
    ResultInSideDto resultDto = new ResultInSideDto();
    String sqlUpdate = " update open_pm.CR_MANAGER_SCOPE set is_active = 0 where CMSE_ID =:id ";
    Query query = getEntityManager().createNativeQuery(sqlUpdate);
    query.setParameter("id", id);
    int result = query.executeUpdate();
    if (result > 0) {
      resultDto.setKey(RESULT.SUCCESS);
    } else {
      resultDto.setKey(RESULT.ERROR);
    }
    return resultDto;
  }

  @Override
  public ResultInSideDto deleteListCrManagerScope(List<CrManagerScopeDTO> groupUnitDetailListDTO) {
    ResultInSideDto resultDto = new ResultInSideDto();
    String result = deleteByListDTO(groupUnitDetailListDTO, CrManagerScopeEntity.class, colId);
    resultDto.setKey(result);
    return resultDto;
  }

  @Override
  public CrManagerScopeDTO findCrManagerScopeDTOById(Long id) {
    if (id != null && id > 0) {
      return getEntityManager().find(CrManagerScopeEntity.class, id).toDTO();
    }
    return null;
  }


  @SuppressWarnings("JpaQlInspection")
  @Override
  public ResultInSideDto updateLisCrManagerScope(List<Long> ids) {
    ResultInSideDto resultDto = new ResultInSideDto();
    String sqlUpdate = "update CrManagerScopeEntity set isActive = 0 where cmseId in ( :cmseId )";
    Query query = getEntityManager().createNativeQuery(sqlUpdate);
    query.setParameter("cmseId", ids);
    int result = query.executeUpdate();
    if (result > 0) {
      resultDto.setKey(RESULT.SUCCESS);
    } else {
      resultDto.setKey(RESULT.ERROR);
    }
    return resultDto;
  }

  @Override
  public Datatable getListCrManagerScopeSearch(CrManagerScopeDTO crManagerScopeDTO) {
    Map<String, Object> parameters = new HashMap<>();
    String sql = SQLBuilder.getSqlQueryById("crManagerScope", "getListCrManagerScopeSearch");
    String locale = I18n.getLocale();
    parameters.put("leeLocale", locale);
    if (!StringUtils.isStringNullOrEmpty(crManagerScopeDTO.getCmseCode())) {
      sql += " and LOWER(re.cmseCode ) like LOWER(:cmseCode) escape '\\'";
      parameters
          .put("cmseCode", StringUtils.convertLowerParamContains(crManagerScopeDTO.getCmseCode()));
    }
    if (!StringUtils.isStringNullOrEmpty(crManagerScopeDTO.getCmseName())) {
      sql += " and LOWER(re.cmseName ) like LOWER(:cmseName) escape '\\'";
      parameters
          .put("cmseName", StringUtils.convertLowerParamContains(crManagerScopeDTO.getCmseName()));
    }
    if (!StringUtils.isStringNullOrEmpty(crManagerScopeDTO.getDescription())) {
      sql += " and LOWER(re.description ) like LOWER(:description) escape '\\'";
      parameters
          .put("description",
              StringUtils.convertLowerParamContains(crManagerScopeDTO.getDescription()));
    }

    if (!StringUtils.isStringNullOrEmpty(crManagerScopeDTO.getSearchAll())) {
      sql += "  and (LOWER(re.cmseCode ) like LOWER(:searchAll) escape '\\'";
      sql += "  or LOWER(re.cmseName ) like LOWER(:searchAll) escape '\\')";
      parameters.put("searchAll",
          StringUtils.convertLowerParamContains(crManagerScopeDTO.getSearchAll()));
    }
    sql += " ORDER by re.cmseId DESC";

    return getListDataTableBySqlQuery(sql, parameters, crManagerScopeDTO.getPage(),
        crManagerScopeDTO.getPageSize(),
        CrManagerScopeDTO.class, crManagerScopeDTO.getSortName(), crManagerScopeDTO.getSortType());
  }

  private static final String colId = "cmseId";
}
