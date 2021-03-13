package com.viettel.gnoc.incident.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.incident.dto.CatReasonInSideDTO;
import com.viettel.gnoc.incident.dto.CfgMapNetLevelIncTypeDTO;
import com.viettel.gnoc.incident.dto.InfraCableLaneDTO;
import com.viettel.gnoc.incident.dto.InfraSleevesDTO;
import com.viettel.gnoc.incident.dto.TroublesInSideDTO;
import com.viettel.gnoc.incident.model.TroublesEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class TransmissionInfoRepositoryImpl extends BaseRepository implements
    TransmissionInfoRepository {

  @Override
  public List<CfgMapNetLevelIncTypeDTO> getListCfgMapNetLevelIncTypeDTO(
      CfgMapNetLevelIncTypeDTO cfgMapNetLevelIncTypeDTO) {
    Map<String, Object> parameters = new HashMap<>();
    String sql = "select a.ID id,"
        + "a.TROUBLE_TYPE_ID troubleTypeId,"
        + "a.NETWORK_LEVEL_NAME networkLevelName,"
        + "a.NETWORK_LEVEL_ID networkLevelId,"
        + "a.TROUBLE_TYPE_NAME troubleTypeName  "
        + "from ONE_TM.CFG_MAP_NET_LEVEL_INC_TYPE a where 1=1";
    if (!StringUtils.isStringNullOrEmpty(cfgMapNetLevelIncTypeDTO.getNetworkLevelId())) {
      sql += " and  a.NETWORK_LEVEL_ID=:networkLevelId";
      parameters.put("networkLevelId", cfgMapNetLevelIncTypeDTO.getNetworkLevelId());
    }
    if (!StringUtils.isStringNullOrEmpty(cfgMapNetLevelIncTypeDTO.getNetworkLevelName())) {
      sql += " and  a.NETWORK_LEVEL_NAME=:networkLevelName";
      parameters.put("networkLevelName", cfgMapNetLevelIncTypeDTO.getNetworkLevelName());
    }
    if (!StringUtils.isStringNullOrEmpty(cfgMapNetLevelIncTypeDTO.getTroubleTypeId())) {
      sql += " and  a.TROUBLE_TYPE_ID=:troubleTypeId";
      parameters.put("troubleTypeId", cfgMapNetLevelIncTypeDTO.getTroubleTypeId());
    }
    if (!StringUtils.isStringNullOrEmpty(cfgMapNetLevelIncTypeDTO.getTroubleTypeName())) {
      sql += " and  a.TROUBLE_TYPE_NAME=:troubleTypeName";
      parameters.put("troubleTypeName", cfgMapNetLevelIncTypeDTO.getTroubleTypeName());
    }
    return getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(CfgMapNetLevelIncTypeDTO.class));
  }

  @Override
  public List<CatReasonInSideDTO> getListReasonSearch(CatReasonInSideDTO reasonDto) {
    Map<String, Object> parameters = new HashMap<>();
    String sql =
        " SELECT  a.id, a.reason_name reasonName, a.parent_id parentId,a.description description, a.type_id typeId,a.UPDATE_TIME updateTime, a.reason_type reasonType, a.reason_code reasonCode,a.IS_CHECK_SCRIPT isCheckScript, a.is_update_closure isUpdateClosure   \n"
            + " FROM   CAT_REASON a  WHERE 1 = 1";
    if (!StringUtils.isStringNullOrEmpty(reasonDto.getReasonCode())) {
      sql += "   AND LOWER(a.reason_code) LIKE :reasonCode ";
      parameters.put("reasonCode", "%" + reasonDto.getReasonCode().toLowerCase() + "%");
    }
    if (!StringUtils.isStringNullOrEmpty(reasonDto.getReasonName())) {
      sql += "   AND LOWER(a.reason_name) LIKE :reasonName ";
      parameters.put("reasonName", "%" + reasonDto.getReasonName().toLowerCase() + "%");
    }

    if (reasonDto.getTypeId() != null && !"-1".equals(reasonDto.getTypeId())) {
      sql += " and a.type_id = :typeId ";
      parameters.put("typeId", reasonDto.getTypeId());
    }
    if (reasonDto.getParentId() != null && !"".equals(reasonDto.getParentId())) {
      sql += " and  parent_id = :parentId ";
      parameters.put("parentId", reasonDto.getParentId());
    }
    return getNamedParameterJdbcTemplate().query(sql, parameters, BeanPropertyRowMapper.newInstance(
        CatReasonInSideDTO.class));
  }

  @Override
  public Datatable onSearchTroubleByLineCutCode(TroublesInSideDTO troublesDTO) {
    Map<String, Object> parameters = new HashMap<>();
    String sql = "select a.LINE_CUT_CODE lineCutCode, a.CODE_SNIPPET_OFF codeSnippetOff from one_tm.TROUBLES a where 1=1 and ( LOWER(a.LINE_CUT_CODE) LIKE :keyword ESCAPE '\\')";
    parameters.put("keyword", troublesDTO.getLineCutCode());
    return getListDataTableBySqlQuery(sql, parameters, troublesDTO.getPage(),
        troublesDTO.getPageSize(),
        TroublesInSideDTO.class, troublesDTO.getSortName(), troublesDTO.getSortType());
  }

  @Override
  public Datatable onSearchInfraCableLaneDTO(InfraCableLaneDTO infraCableLaneDTO) {
    Map<String, Object> parameters = new HashMap<>();

    String sql = "select a.LANE_CODE laneCode, a.LANE_ID laneId,a.START_POINT startPoint,a.END_POINT endPoint,a.LENGTH length from COMMON_GNOC.infra_cable_lane a where  1=1";
    if (!StringUtils.isStringNullOrEmpty(infraCableLaneDTO.getLaneCode())) {
      sql += " AND (LOWER( a.LANE_CODE) LIKE LOWER(:keyWord) ESCAPE '\\')";
      parameters
          .put("keyWord", StringUtils.convertLowerParamContains(infraCableLaneDTO.getLaneCode()));
    }

    return getListDataTableBySqlQuery(sql, parameters, infraCableLaneDTO.getPage(),
        infraCableLaneDTO.getPageSize(),
        InfraCableLaneDTO.class, infraCableLaneDTO.getSortName(), infraCableLaneDTO.getSortType());
  }

  @Override
  public String updateTroubles1(TroublesEntity troublesEntity) {
    getEntityManager().merge(troublesEntity);
    return RESULT.SUCCESS;
  }

  @Override
  public Datatable getInfraSleevesBO(InfraSleevesDTO infraSleevesDTO) {
    Map<String, Object> parameters = new HashMap<>();
    String sql = "select a.SLEEVE_ID,a.SLEEVE_CODE,a.SLEEVE_NAME,a.MODIFY_DATE,a.LAST_UPDATE_TIME,a.POOL_ID from NIMS_FCN.INFRA_SLEEVES a where 1=1 ";
    if (!StringUtils.isStringNullOrEmpty(infraSleevesDTO.getPoolId())) {
      sql += " and  a.POOL_ID = :poolId ";
      parameters.put("poolId", infraSleevesDTO.getPoolId());
    }
    if (!StringUtils.isStringNullOrEmpty(infraSleevesDTO.getSleeveCode())) {
      sql += " and  a.SLEEVE_CODE= :sleeveCode ";
      parameters.put("sleeveCode", infraSleevesDTO.getSleeveCode());
    }
    return getListDataTableBySqlQuery(sql, parameters, infraSleevesDTO.getPage(),
        infraSleevesDTO.getPageSize(),
        InfraSleevesDTO.class, infraSleevesDTO.getSortName(), infraSleevesDTO.getSortType());
  }

}
