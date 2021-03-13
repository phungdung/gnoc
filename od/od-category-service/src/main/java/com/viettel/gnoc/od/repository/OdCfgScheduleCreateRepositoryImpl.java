package com.viettel.gnoc.od.repository;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.commons.dto.ReceiveUnitDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.repository.GnocFileRepository;
import com.viettel.gnoc.commons.repository.ReceiveUnitRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.od.dto.OdCfgScheduleCreateDTO;
import com.viettel.gnoc.od.dto.OdFileDTO;
import com.viettel.gnoc.od.dto.OdTypeDTO;
import com.viettel.gnoc.od.model.OdCfgScheduleCreateEntity;
import com.viettel.gnoc.od.model.OdFileEntity;
import com.viettel.gnoc.od.model.OdTypeEntity;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class OdCfgScheduleCreateRepositoryImpl extends BaseRepository implements
    OdCfgScheduleCreateRepository {

  @Autowired
  ReceiveUnitRepository receiveUnitRepository;

  @Autowired
  GnocFileRepository gnocFileRepository;

  @Override
  public Datatable getListOdCfgScheduleCreateDTOSearchWeb(
      OdCfgScheduleCreateDTO odCfgScheduleCreateDTO) {
    BaseDto baseDto = sqlSearch(odCfgScheduleCreateDTO);
    return getListDataTableBySqlQuery(baseDto.getSqlQuery(), baseDto.getParameters(),
        odCfgScheduleCreateDTO.getPage(),
        odCfgScheduleCreateDTO.getPageSize(), OdCfgScheduleCreateDTO.class,
        odCfgScheduleCreateDTO.getSortName(),
        odCfgScheduleCreateDTO.getSortType());
  }

  @Override
  public OdCfgScheduleCreateDTO findOdCfgScheduleCreateById(Long id) {
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_OD_CFG_SCHEDULE_CREATE, "od-Cfg-Schedule-Create");
    sqlQuery += " AND o.cfg_id = :id ";
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("categoryCode", Constants.OD_MASTER_CODE.OD_SCHEDULE);
    parameters.put("id", id);
    List<OdCfgScheduleCreateDTO> list = getNamedParameterJdbcTemplate().query(sqlQuery, parameters,
        BeanPropertyRowMapper.newInstance(OdCfgScheduleCreateDTO.class));
    if (list != null && !list.isEmpty()) {
      OdCfgScheduleCreateDTO dto = list.get(0);
      dto.setReceiveUnitDTOList(getListReceiveUnit(dto));
      GnocFileDto gnocFileDto = new GnocFileDto();
      gnocFileDto.setBusinessCode(Constants.GNOC_FILE_BUSSINESS.OD_CFG_SCHEDULE_CREATE);
      gnocFileDto.setBusinessId(dto.getId());
      dto.setGnocFileDtos(gnocFileRepository.getListGnocFileByDto(gnocFileDto));
      return dto;
    }
    return null;
  }

  @Override
  public ResultInSideDto insertOdCfgScheduleCreate(OdCfgScheduleCreateDTO odCfgScheduleCreateDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    odCfgScheduleCreateDTO.setLastUpdateTime(new Date());
    OdCfgScheduleCreateEntity odCfgScheduleCreateEntity = getEntityManager()
        .merge(odCfgScheduleCreateDTO.toEntity());
    resultInSideDto.setId(odCfgScheduleCreateEntity.getId());
    resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto updateOdCfgScheduleCreate(OdCfgScheduleCreateDTO odCfgScheduleCreateDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    odCfgScheduleCreateDTO.setLastUpdateTime(new Date());
    getEntityManager().merge(odCfgScheduleCreateDTO.toEntity());
    resultInSideDto.setId(odCfgScheduleCreateDTO.getId());
    resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto insertOrUpdateListOdCfgScheduleCreate(
      List<OdCfgScheduleCreateDTO> listScheduleCreate) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    for (OdCfgScheduleCreateDTO dto : listScheduleCreate) {
      if (dto.getId() != null) {
        resultInSideDto = updateOdCfgScheduleCreate(dto);
      } else {
        resultInSideDto = insertOdCfgScheduleCreate(dto);
      }
    }
    return resultInSideDto;
  }

  @Override
  public OdCfgScheduleCreateDTO checkOdCfgScheduleCreateExit(
      OdCfgScheduleCreateDTO odCfgScheduleCreateDTO) {
    List<OdCfgScheduleCreateEntity> dataEntity = (List<OdCfgScheduleCreateEntity>) findByMultilParam(
        OdCfgScheduleCreateEntity.class,
        "odTypeId", odCfgScheduleCreateDTO.getOdTypeId(),
        "odPriority", odCfgScheduleCreateDTO.getOdPriority(),
        "receiveUnit", odCfgScheduleCreateDTO.getReceiveUnit());
    if (dataEntity != null && dataEntity.size() > 0) {
      return dataEntity.get(0).toDTO();
    }
    return null;
  }

  @Override
  public ResultInSideDto deleteOdCfgScheduleCreate(Long id) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    OdCfgScheduleCreateEntity entity = getEntityManager().find(OdCfgScheduleCreateEntity.class, id);
    getEntityManager().remove(entity);
    resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    return resultInSideDto;
  }

  @Override
  public String getSequenseOdCfgScheduleCreate(String sequense) {
    return getSeqTableBase(sequense);
  }

  @Override
  public Datatable getListDataExport(OdCfgScheduleCreateDTO odCfgScheduleCreateDTO) {
    BaseDto baseDto = sqlSearch(odCfgScheduleCreateDTO);
    Datatable datatable = new Datatable();
    List<OdCfgScheduleCreateDTO> scheduleCreateDTOList = getNamedParameterJdbcTemplate()
        .query(baseDto.getSqlQuery(), baseDto.getParameters(),
            BeanPropertyRowMapper.newInstance(OdCfgScheduleCreateDTO.class));
    if (scheduleCreateDTOList != null && !scheduleCreateDTOList.isEmpty()) {
      for (int i = 0; i < scheduleCreateDTOList.size(); i++) {
        scheduleCreateDTOList.get(i)
            .setReceiveUnitName(getListReceiveUnitName(scheduleCreateDTOList.get(i)));
      }
    }
    datatable.setData(scheduleCreateDTOList);
    return datatable;
  }

  @Override
  public List<OdTypeDTO> getListOdType() {
    List<OdTypeDTO> list = new ArrayList<>();
    List<OdTypeEntity> dataEntity = findAll(OdTypeEntity.class);
    ;
    if (dataEntity != null && dataEntity.size() > 0) {
      for (OdTypeEntity entity : dataEntity) {
        list.add(entity.toDTO());
      }
    }
    return list;
  }

  @Override
  public List<OdTypeDTO> getListOdTypeByGroupId(Long odGroupId) {
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_OD_CFG_SCHEDULE_CREATE, "get-Od-Type-By-Group-Id");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("odGroupId", odGroupId);
    return getNamedParameterJdbcTemplate().query(sqlQuery, parameters,
        BeanPropertyRowMapper.newInstance(OdTypeDTO.class));
  }

  public List<ReceiveUnitDTO> getListReceiveUnit(OdCfgScheduleCreateDTO odCfgScheduleCreateDTO) {
    List<ReceiveUnitDTO> receiveUnitDTOS = new ArrayList<>();
    String receiveUnitId = odCfgScheduleCreateDTO.getReceiveUnit();
    if (StringUtils.isNotNullOrEmpty(receiveUnitId)) {
      List<Long> receiveUnitIdList = (List<Long>) StringUtils
          .stringToArray(Long.class, receiveUnitId, ",");
      for (Long id : receiveUnitIdList) {
        ReceiveUnitDTO dto = receiveUnitRepository.getReceiveUnit(id);
        receiveUnitDTOS.add(dto);
      }
    }
    return receiveUnitDTOS;
  }

  public String getListReceiveUnitName(OdCfgScheduleCreateDTO odCfgScheduleCreateDTO) {
    String receiveUnit = "";
    List<ReceiveUnitDTO> list = getListReceiveUnit(odCfgScheduleCreateDTO);
    for (int i = 0; i < list.size(); i++) {
      if (i != 0) {
        receiveUnit = receiveUnit.concat(", \n");
      }
      receiveUnit = receiveUnit
          .concat(list.get(i).getUnitName() + "(" + list.get(i).getUnitCode() + ")");
    }
    return receiveUnit;
  }

  public BaseDto sqlSearch(OdCfgScheduleCreateDTO odCfgScheduleCreateDTO) {
    BaseDto baseDto = new BaseDto();
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_OD_CFG_SCHEDULE_CREATE, "od-Cfg-Schedule-Create");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("categoryCode", Constants.OD_MASTER_CODE.OD_SCHEDULE);
    if (StringUtils.isNotNullOrEmpty(odCfgScheduleCreateDTO.getSearchAll())) {
      sqlQuery += " AND (lower(o.od_name) LIKE :searchAll ESCAPE '\\' OR lower(o.od_description) LIKE :searchAll ESCAPE '\\') ";
      parameters.put("searchAll",
          StringUtils.convertLowerParamContains(odCfgScheduleCreateDTO.getSearchAll()));
    }
    if (StringUtils.isNotNullOrEmpty(odCfgScheduleCreateDTO.getOdName())) {
      sqlQuery += " AND lower(o.od_name) LIKE :odName ESCAPE '\\' ";
      parameters
          .put("odName", StringUtils.convertLowerParamContains(odCfgScheduleCreateDTO.getOdName()));
    }
    if (StringUtils.isNotNullOrEmpty(odCfgScheduleCreateDTO.getOdDescription())) {
      sqlQuery += " AND lower(o.od_description) LIKE :odDescription ESCAPE '\\' ";
      parameters.put("odDescription",
          StringUtils.convertLowerParamContains(odCfgScheduleCreateDTO.getOdDescription()));
    }
    if (odCfgScheduleCreateDTO.getOdTypeId() != null) {
      sqlQuery += " AND o.od_type_id = :odTypeId ";
      parameters.put("odTypeId", odCfgScheduleCreateDTO.getOdTypeId());
    }
    if (odCfgScheduleCreateDTO.getOdPriority() != null) {
      sqlQuery += " AND o.od_priority = :odPriority ";
      parameters.put("odPriority", odCfgScheduleCreateDTO.getOdPriority());
    }
    if (odCfgScheduleCreateDTO.getSchedule() != null) {
      sqlQuery += " AND o.schedule = :schedule ";
      parameters.put("schedule", odCfgScheduleCreateDTO.getSchedule());
    }
    sqlQuery += " order by o.cfg_id DESC";
    baseDto.setSqlQuery(sqlQuery);
    baseDto.setParameters(parameters);
    return baseDto;
  }

  @Override
  public ResultInSideDto insertOdFile(OdFileDTO odFileDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    OdFileEntity odFileEntity = getEntityManager().merge(odFileDTO.toEntity());
    resultInSideDto.setId(odFileEntity.getOdFileId());
    resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto deleteListOdFile(List<Long> odFileIds) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    for (Long odFile : odFileIds) {
      deleteByMultilParam(OdFileEntity.class,
          "odFileId", odFile);
    }
    return resultInSideDto;
  }
}
