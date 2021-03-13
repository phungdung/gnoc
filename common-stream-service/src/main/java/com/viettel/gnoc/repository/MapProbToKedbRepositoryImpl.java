package com.viettel.gnoc.repository;

import com.viettel.bccs.cc.service.ProblemGroupDTO;
import com.viettel.bccs.cc.service.ProblemTypeDTO;
import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.MapProbToKedbDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.WSNocprov4DTO;
import com.viettel.gnoc.commons.model.MapProbToKedbEntity;
import com.viettel.gnoc.commons.proxy.KedbProxy;
import com.viettel.gnoc.commons.proxy.TtServiceProxy;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.commons.utils.ConditionBeanUtil;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.incident.dto.CfgServerNocDTO;
import com.viettel.gnoc.kedb.dto.KedbDTO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class MapProbToKedbRepositoryImpl extends BaseRepository implements MapProbToKedbRepository {

  @Autowired
  KedbProxy kedbProxy;

  @Autowired
  TtServiceProxy ttServiceProxy;


  @Override
  public Datatable getListMapProbToKedbDTO(MapProbToKedbDTO mapProbToKedbDTO) {
    BaseDto baseDto = sqlSearch(mapProbToKedbDTO);
    return getListDataTableBySqlQuery(baseDto.getSqlQuery(),
        baseDto.getParameters(),
        mapProbToKedbDTO.getPage(), mapProbToKedbDTO.getPageSize(),
        MapProbToKedbDTO.class,
        mapProbToKedbDTO.getSortName(), mapProbToKedbDTO.getSortType());
  }

  public BaseDto sqlSearch(MapProbToKedbDTO mapProbToKedbDTO) {
    BaseDto baseDto = new BaseDto();
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_MAP_PROB_TO_KEDB, "getListMapProbToKedb");
    Map<String, Object> parameters = new HashMap<>();
    if (mapProbToKedbDTO != null) {
      if (StringUtils.isNotNullOrEmpty(mapProbToKedbDTO.getSearchAll())) {
        sqlQuery += " AND (lower(a.KEDB_CODE) LIKE :searchAll ESCAPE '\\' )";
        parameters
            .put("searchAll",
                StringUtils.convertLowerParamContains(mapProbToKedbDTO.getSearchAll()));
      }
      if (!StringUtils.isStringNullOrEmpty(mapProbToKedbDTO.getKedbCode())) {
        sqlQuery += " AND (lower(a.KEDB_CODE) LIKE :kedbCode ESCAPE '\\' ) ";
        parameters
            .put("kedbCode", StringUtils.convertLowerParamContains(mapProbToKedbDTO.getKedbCode()));
      }

      if (mapProbToKedbDTO.getProbTypeIdLv1() != null) {
        sqlQuery += " AND a.PROB_TYPE_ID_LV1 = :probTypeIdLv1 ";
        parameters
            .put("probTypeIdLv1", mapProbToKedbDTO.getProbTypeIdLv1());
      }

      if (mapProbToKedbDTO.getProbTypeIdLv2() != null) {
        sqlQuery += " AND a.PROB_TYPE_ID_LV2 = :probTypeIdLv2  ";
        parameters
            .put("probTypeIdLv2", mapProbToKedbDTO.getProbTypeIdLv2());
      }

      if (mapProbToKedbDTO.getProbTypeIdLv3() != null) {
        sqlQuery += " AND a.PROB_TYPE_ID_LV3 = :probTypeIdLv3  ";
        parameters
            .put("probTypeIdLv3", mapProbToKedbDTO.getProbTypeIdLv3());
      }
    }
    sqlQuery += " order by a.id asc";
    baseDto.setSqlQuery(sqlQuery);
    baseDto.setParameters(parameters);
    return baseDto;
  }

  public Datatable getListKedbDTO(KedbDTO kedbDTO) {
    return kedbProxy.getListKedbDTO(kedbDTO);
  }

  //Lay danh sach Nhom
  public List<ProblemGroupDTO> getListGroup() {
    List<ProblemGroupDTO> lstGroup = new ArrayList<>();
    try {
      List<CfgServerNocDTO> lstServer = getListServer();
      if (lstServer != null && !lstServer.isEmpty()) {
        lstGroup = ttServiceProxy.getListProblemGroupParent(lstServer.get(0));
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return lstGroup;
  }

  public List<ProblemGroupDTO> getListKind(Long probGroupId) {
    List<ProblemGroupDTO> lstKind = new ArrayList<>();
    try {
      List<CfgServerNocDTO> lstServer = getListServer();
      if ("".equals(probGroupId) || probGroupId == null) {
        return lstKind;
      } else {
        if (lstServer != null && !lstServer.isEmpty()) {
          lstKind = ttServiceProxy
              .getListProblemGroupByParrenId(Long.valueOf(probGroupId), lstServer.get(0));
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return lstKind;
  }

  public List<ProblemTypeDTO> getListType(Long probGroupId) {
    List<ProblemTypeDTO> lstType = new ArrayList<>();
    try {
      List<CfgServerNocDTO> lstServer = getListServer();
      if ("".equals(probGroupId) || probGroupId == null) {
        return lstType;
      } else {
        lstType = ttServiceProxy
            .getListPobTypeByGroupId(Long.valueOf(probGroupId), lstServer.get(0));
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return lstType;
  }


  public List<CfgServerNocDTO> getListServer() {
    List<CfgServerNocDTO> lstServer = new ArrayList<>();
    try {
      List<ConditionBean> lstCondition = new ArrayList<>();
      lstCondition
          .add(new ConditionBean("insertSource", "BCCS", Constants.NAME_EQUAL, Constants.STRING));
      WSNocprov4DTO wsNocprov4DTO = new WSNocprov4DTO();
      ConditionBeanUtil.sysToOwnListCondition(lstCondition);
      wsNocprov4DTO.setLstCondition(lstCondition);
      wsNocprov4DTO.setRowStart(0);
      wsNocprov4DTO.setMaxRow(100);
      wsNocprov4DTO.setSortType("");
      wsNocprov4DTO.setSortFieldList("");
      lstServer = ttServiceProxy
          .getListCfgServerNocByCondition(wsNocprov4DTO);
      if (lstServer == null || lstServer.isEmpty()) {
        return lstServer;
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return lstServer;
  }


  @Override
  public ResultInSideDto insertMapProbToKedb(MapProbToKedbDTO mapProbToKedbDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    MapProbToKedbEntity mapProbToKedbEntity = getEntityManager().merge(mapProbToKedbDTO.toEntity());
    resultInSideDto.setId(mapProbToKedbEntity.getId());
    return resultInSideDto;
  }

  @Override
  public MapProbToKedbDTO getDetail(Long id) {
    MapProbToKedbEntity mapProbToKedbEntity = getEntityManager()
        .find(MapProbToKedbEntity.class, id);
    MapProbToKedbDTO mapProbToKedbDTO = mapProbToKedbEntity.toDTO();
    return mapProbToKedbDTO;
  }

  @Override
  public ResultInSideDto updateMapProbToKedb(MapProbToKedbDTO mapProbToKedbDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    MapProbToKedbEntity mapProbToKedbEntity = getEntityManager().merge(mapProbToKedbDTO.toEntity());
    resultInSideDto.setId(mapProbToKedbEntity.getId());
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto deleteMapProbToKedb(Long id) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    MapProbToKedbEntity mapProbToKedbEntity = getEntityManager()
        .find(MapProbToKedbEntity.class, id);
    if (mapProbToKedbEntity != null) {
      getEntityManager().remove(mapProbToKedbEntity);
    }
    return resultInSideDto;
  }

  @Override
  public List<MapProbToKedbDTO> getDataExport(MapProbToKedbDTO dto) {
    BaseDto baseDto = sqlSearch(dto);
    return getNamedParameterJdbcTemplate().query(baseDto.getSqlQuery(), baseDto.getParameters(),
        BeanPropertyRowMapper.newInstance(MapProbToKedbDTO.class));
  }

//  @Override
//  public MapProbToKedbDTO checkExist(Long groupId, Long kindId, Long typeId){
//
//    List<MapProbToKedbEntity> dataEntity = (List<MapProbToKedbEntity>) findByMultilParam(
//        MapProbToKedbEntity.class,
//        "probTypeIdLv1", groupId,
//        "probTypeIdLv2", kindId,
//        "probTypeIdLv3", typeId);
//    if (dataEntity != null && dataEntity.size() > 0) {
//      return dataEntity.get(0).toDTO();
//    }
//    return null;
//  }

}


