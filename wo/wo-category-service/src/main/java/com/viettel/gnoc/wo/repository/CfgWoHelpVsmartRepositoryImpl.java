package com.viettel.gnoc.wo.repository;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.wo.dto.CfgWoHelpVsmartDTO;
import com.viettel.gnoc.wo.model.CfgWoHelpVsmartEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class CfgWoHelpVsmartRepositoryImpl extends BaseRepository implements
    CfgWoHelpVsmartRepository {

  @Override
  public ResultInSideDto insertCfgWoHelpVsmart(CfgWoHelpVsmartDTO cfgWoHelpVsmartDTO) {
    CfgWoHelpVsmartEntity cfgWoHelpVsmartEntity = cfgWoHelpVsmartDTO.toEntity();
    if (cfgWoHelpVsmartDTO.getId() != null) {
      getEntityManager().persist(cfgWoHelpVsmartEntity);
    } else {
      getEntityManager().merge(cfgWoHelpVsmartEntity);
    }
    return new ResultInSideDto(cfgWoHelpVsmartEntity.getId(), RESULT.SUCCESS, RESULT.SUCCESS);
  }

  @Override
  public ResultInSideDto updateCfgWoHelpVsmart(CfgWoHelpVsmartDTO cfgWoHelpVsmartDTO) {
    ResultInSideDto resultDto = new ResultInSideDto();
    resultDto.setKey(RESULT.SUCCESS);
    getEntityManager().merge(cfgWoHelpVsmartDTO.toEntity());
    return resultDto;
  }

  @Override
  public CfgWoHelpVsmartDTO findCfgWoHelpVsmartsById(Long id) {
    if (id != null && id > 0) {
      return getEntityManager().find(CfgWoHelpVsmartEntity.class, id).toDTO();
    }
    return null;
  }

  @Override
  public ResultInSideDto deleteCfgWoHelpVsmart(Long id) {
    ResultInSideDto resultDto = new ResultInSideDto();
    resultDto.setKey(RESULT.SUCCESS);
    CfgWoHelpVsmartEntity cfgWoHelpVsmartEntity = getEntityManager()
        .find(CfgWoHelpVsmartEntity.class, id);
    getEntityManager().remove(cfgWoHelpVsmartEntity);
    return resultDto;
  }

  @Override
  public ResultInSideDto deleteListCfgWoHelpVsmart(List<CfgWoHelpVsmartDTO> cfgWoHelpVsmartDTOS) {
    ResultInSideDto resultDto = new ResultInSideDto();
    String result = deleteByListDTO(cfgWoHelpVsmartDTOS, CfgWoHelpVsmartEntity.class, colId);
    resultDto.setKey(result);
    return resultDto;
  }

  @Override
  public Datatable getListCfgWoHelpVsmartDTOSearchWeb(CfgWoHelpVsmartDTO cfgWoHelpVsmartDTO) {
    Map<String, Object> parameters = new HashMap<>();
    String odSystem = I18n.getLanguage("woHelpVsmart.odsystem");
    String srSystem = I18n.getLanguage("woHelpVsmart.srsystem");
    String sql =
        "select b.id id, b.system_id systemId, b.type_id typeId,  b.file_id fileId, b.type_name typeName, decode(b.SYSTEM_ID,1,'"
            + odSystem + "',2,'" + srSystem + "') systemName \n"
            + "  from wfm.CFG_WO_HELP_VSMART b  where 1=1";
    if (!StringUtils.isStringNullOrEmpty(cfgWoHelpVsmartDTO.getTypeId())) {
      sql += " and b.type_id=:typeId";
      parameters.put("typeId", cfgWoHelpVsmartDTO.getTypeId());
    }
    if (cfgWoHelpVsmartDTO.getSystemId() != null) {
      sql += " and  b.system_id=:systemId";
      parameters.put("systemId", cfgWoHelpVsmartDTO.getSystemId());
    }
    if (StringUtils.isNotNullOrEmpty(cfgWoHelpVsmartDTO.getSearchAll())) {
      String resul = checkSearchAllBySystem(cfgWoHelpVsmartDTO.getSearchAll());
      sql += " and (LOWER(b.type_name) LIKE LOWER(:searchAll) ESCAPE '\\'";
      if ("hethongod".equals(resul) || "searchall".equals(resul)) {
        sql += " or b.system_id=1";
      }
      if ("hethongsr".equals(resul) || "searchall".equals(resul)) {
        sql += " or b.system_id=2";
      }
      sql += ")";
      parameters.put("searchAll",
          StringUtils.convertLowerParamContains(cfgWoHelpVsmartDTO.getSearchAll()));
    }

    return getListDataTableBySqlQuery(sql, parameters, cfgWoHelpVsmartDTO.getPage(),
        cfgWoHelpVsmartDTO.getPageSize(),
        CfgWoHelpVsmartDTO.class, cfgWoHelpVsmartDTO.getSortName(),
        cfgWoHelpVsmartDTO.getSortType());
  }

  public String checkSearchAllBySystem(String SearchAll) {
    String resul = "";
    String check = SearchAll.replaceAll("\\s", "").toLowerCase();
    if (check != null && check.length() > 1) {
      String odSystem = I18n.getLanguage("woHelpVsmart.odsystem.regex");
      String srSystem = I18n.getLanguage("woHelpVsmart.srsystem.regex");
      Boolean test1 = odSystem.matches(".*" + check + ".*");
      Boolean test2 = srSystem.matches(".*" + check + ".*");
      if (test1 == true && test2 != true) {
        resul = "hethongod";
      } else if (test2 == true && test1 != true) {
        resul = "hethongsr";
      } else if (test1 == true && test2 == true) {
        resul = "searchall";
      } else {
        resul = "";
      }
    }
    return resul;
  }

  @Override
  public List<CatItemDTO> getListCbbSystem() {
    Map<String, Object> parameters = new HashMap<>();
    String sql = "select * from COMMON_GNOC.CAT_ITEM a,COMMON_GNOC.CATEGORY b where A.CATEGORY_ID = B.CATEGORY_ID and B.CATEGORY_CODE = 'CFG_WO_HELP_SYSTEM'";
    List<CatItemDTO> list = getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(CatItemDTO.class));
    return list;

  }

  private static final String colId = "id";

  @Override
  public String findCfgWoHelpVsmartDTO(Long systemId, String typeId) {
    try {
      Map<String, Object> parameters = new HashMap<>();
      String sql = "SELECT id FROM WFM.CFG_WO_HELP_VSMART WHERE SYSTEM_ID = :systemId and TYPE_ID = :typeId ";
      parameters.put("systemId", systemId);
      parameters.put("typeId", typeId);
      List<CfgWoHelpVsmartDTO> lst = getNamedParameterJdbcTemplate()
          .query(sql, parameters, BeanPropertyRowMapper.newInstance(CfgWoHelpVsmartDTO.class));
      if (lst != null && !lst.isEmpty()) {
        String id = lst.get(0).getId().toString();
        return id;
      }
    } catch (Exception he) {
      log.error(he.getMessage(), he);
    }
    return null;
  }

  @Override
  public String getSequenseCfgWoHelpVsmart(String sequense) {
    return getSeqTableBase(sequense);
  }
}
