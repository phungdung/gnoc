package com.viettel.gnoc.repository;


import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.CfgUnitTtSpmDTO;
import com.viettel.gnoc.commons.dto.CfgUnitTtSpmSearchDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.model.CfgUnitTtSpmEntity;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class CfgUnitTtSpmRepositoryImpl extends BaseRepository implements CfgUnitTtSpmRepository {

  @Override
  public String updateCfgUnitTtSpm(CfgUnitTtSpmDTO cfgUnitTtSpmDTO) {
    getEntityManager().merge(cfgUnitTtSpmDTO.toEntity());
    return RESULT.SUCCESS;
  }

  @Override
  public String deleteCfgUnitTtSpm(Long id) {
    return deleteById(CfgUnitTtSpmEntity.class, id, colId);
  }

  @Override
  public String deleteListCfgUnitTtSpm(List<CfgUnitTtSpmDTO> cfgUnitTtSpmListDTO) {
    return deleteByListDTO(cfgUnitTtSpmListDTO, CfgUnitTtSpmEntity.class, colId);
  }

  @Override
  public List<CfgUnitTtSpmDTO> getListCfgUnitTtSpmDTO(CfgUnitTtSpmDTO cfgUnitTtSpmDTO, int rowStart,
      int maxRow, String sortType, String sortFieldList) {
    Map<String, Object> parameters = new HashMap<>();
    String sql = "select * from CFG_UNIT_TT_SPM a where 1=1";
    if (!StringUtils.isStringNullOrEmpty(cfgUnitTtSpmDTO.getLocationId())) {
      sql += " and a.LOCATION_ID= :locationId";
      parameters.put("locationId", cfgUnitTtSpmDTO.getLocationId());
    }
    if (!StringUtils.isStringNullOrEmpty(cfgUnitTtSpmDTO.getUnitId())) {
      sql += " and a.UNIT_ID=:unitId";
      parameters.put("unitId", cfgUnitTtSpmDTO.getUnitId());
    }
    if (!StringUtils.isStringNullOrEmpty(cfgUnitTtSpmDTO.getTypeUnit())) {
      sql += " and a.TYPE_UNIT=:typeUnit";
      parameters.put("typeUnit", cfgUnitTtSpmDTO.getTypeUnit());
    }
    List<CfgUnitTtSpmDTO> list = getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(CfgUnitTtSpmDTO.class));
    return list;
  }

  @Override
  public ResultInSideDto insertCfgUnitTtSpm(CfgUnitTtSpmDTO cfgUnitTtSpmDTO) {
    return insertByModel(cfgUnitTtSpmDTO.toEntity(), colId);
  }

  @Override
  public String insertOrUpdateListCfgUnitTtSpm(List<CfgUnitTtSpmDTO> cfgUnitTtSpmDTO) {
    for (CfgUnitTtSpmDTO item : cfgUnitTtSpmDTO) {
      CfgUnitTtSpmEntity entity = item.toEntity();
      if (entity.getId() != null && entity.getId() > 0) {
        getEntityManager().merge(entity);
      } else {
        getEntityManager().persist(entity);
      }
    }
    return RESULT.SUCCESS;
  }

  @Override
  public List<String> getSequenseCfgUnitTtSpm(String seqName, int... size) {
    return getListSequense(seqName, size);
  }

  @Override
  public Datatable getListUnitTtSpm(CfgUnitTtSpmDTO cfgUnitTtSpmListDTO) {
    return null;
  }

  @Override
  public CfgUnitTtSpmDTO findCfgUnitTtSpmById(Long id) {
    if (id != null && id > 0) {
      return getEntityManager().find(CfgUnitTtSpmEntity.class, id).toDTO();
    }
    return null;
  }

  @Override
  public Datatable getListUnitTtSpmSearch(CfgUnitTtSpmSearchDTO cfgUnitTtSpmSearchDTO) {
    Map<String, Object> parameters = new HashMap<>();
    String sql = SQLBuilder.getSqlQueryById("cfgUnitTtSpm", "getListUnitTtSpmSearch");
    String locale = I18n.getLocale();
//    String locale = "en_US";
    parameters.put("p_leeLocale", locale);
    if (cfgUnitTtSpmSearchDTO.getTypeId() != null && !"-1"
        .equals(cfgUnitTtSpmSearchDTO.getTypeId())) {
      sql += " and  a.typeId=:typeid ";
      parameters.put("typeid", cfgUnitTtSpmSearchDTO.getTypeId());
    }
    if (cfgUnitTtSpmSearchDTO.getUnitId() != null) {
      sql += " and  a.unitId=:unitid ";
      parameters.put("unitid", cfgUnitTtSpmSearchDTO.getUnitId());
    }
    if (StringUtils.isNotNullOrEmpty(cfgUnitTtSpmSearchDTO.getTypeUnit())) {
      sql += "and  a.typeUnit =:typeunit ";
      parameters.put("typeunit", cfgUnitTtSpmSearchDTO.getTypeUnit());
    }
    if (StringUtils.isNotNullOrEmpty(cfgUnitTtSpmSearchDTO.getLocationName())) {
      sql += " AND a.locationId in (SELECT b.LOCATION_ID FROM COMMON_GNOC.CAT_LOCATION b WHERE LOWER(b.LOCATION_NAME) LIKE :locationName ESCAPE '\\')";
      parameters.put("locationName",
          StringUtils.convertLowerParamContains(cfgUnitTtSpmSearchDTO.getLocationName()));
    }
    if (StringUtils.isNotNullOrEmpty(cfgUnitTtSpmSearchDTO.getSearchAll())) {
      sql += " AND (LOWER(a.locationName) LIKE :searchAll ESCAPE '\\'";
      sql += " OR LOWER(a.unitName) LIKE :searchAll ESCAPE '\\'";
      sql += " OR LOWER(a.typeUnitName) LIKE :searchAll ESCAPE '\\'";
      sql += " OR LOWER(a.typeName) LIKE :searchAll ESCAPE '\\'";

      sql += " ) order by a.typeName";
      parameters.put("searchAll",
          StringUtils.convertLowerParamContains(cfgUnitTtSpmSearchDTO.getSearchAll()));
    }
    return getListDataTableBySqlQuery(sql, parameters, cfgUnitTtSpmSearchDTO.getPage(),
        cfgUnitTtSpmSearchDTO.getPageSize(),
        CfgUnitTtSpmSearchDTO.class, cfgUnitTtSpmSearchDTO.getSortName(),
        cfgUnitTtSpmSearchDTO.getSortType());

  }

  private static final String colId = "id";
}

