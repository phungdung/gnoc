package com.viettel.gnoc.od.repository;


import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.repository.CatItemRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.od.dto.OdTypeDTO;
import com.viettel.gnoc.od.dto.OdTypeDetailDTO;
import com.viettel.gnoc.od.dto.OdTypeExportDTO;
import com.viettel.gnoc.od.dto.OdTypeMapLocationDTO;
import com.viettel.gnoc.od.model.OdTypeEntity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

/**
 * @author NamTN
 */
@SuppressWarnings("rawtypes")
@Repository
@Slf4j
public class OdTypeRepositoryImpl extends BaseRepository implements OdTypeRepository {

  @Autowired
  protected OdTypeDetailRepository odTypeDetailRepository;
  @Autowired
  protected CatItemRepository catItemRepository;
  @Autowired
  protected OdTypeMapLocationRepository odTypeMapLocationRepository;

  private final static String DATA_EXPORT = "DATA_EXPORT";

  @SuppressWarnings("unchecked")
  @Override
  public Datatable search(OdTypeDTO odTypeDTO) {
    List<OdTypeEntity> lst = findAll(OdTypeEntity.class);
    List<OdTypeDTO> list = new ArrayList<>();
    for (OdTypeEntity item : lst) {
      list.add(item.toDTO());
    }
    return null;
  }

  @Override
  public ResultInSideDto delete(Long odTypeId) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
    OdTypeEntity odTypeEntity = getEntityManager().find(OdTypeEntity.class, odTypeId);
    getEntityManager().remove(odTypeEntity);
    return resultInSideDTO;
  }

  @Override
  public ResultInSideDto deleteList(List<Long> listOdTypeId) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    for (Long odTypeId : listOdTypeId) {
      resultInSideDTO = delete(odTypeId);
    }
    return resultInSideDTO;
  }

  @Override
  public ResultInSideDto insertOrUpdateListImport(List<OdTypeDTO> listOdType) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    for (OdTypeDTO odTypeDTO : listOdType) {
      OdTypeDetailDTO odTypeDetailDTO = new OdTypeDetailDTO();
      List<OdTypeDetailDTO> odTypeDetailDTOList = new ArrayList<>();
      odTypeDetailDTO.setPriorityId(odTypeDTO.getPriorityId());
      odTypeDetailDTO.setProcessTime(odTypeDTO.getProcessTime().doubleValue());
      if ("1".equals(odTypeDTO.getCheckValidate())) {
        OdTypeDTO odTypeDTOTmp = checkOdTypeExist(odTypeDTO.getOdTypeCode());
        if (odTypeDTOTmp != null) {
          odTypeDTO.setOdTypeId(odTypeDTOTmp.getOdTypeId());
        }
      }
      if (odTypeDTO.getOdTypeId() != null) {
        odTypeDetailDTO.setOdTypeId(odTypeDTO.getOdTypeId());
        odTypeDetailDTOList.add(odTypeDetailDTO);
        odTypeDTO.setOdTypeDetailDTOS(odTypeDetailDTOList);
        resultInSideDTO = edit(odTypeDTO);
      } else {
        odTypeDetailDTOList.add(odTypeDetailDTO);
        odTypeDTO.setOdTypeDetailDTOS(odTypeDetailDTOList);
        resultInSideDTO = add(odTypeDTO);
      }
    }
    return resultInSideDTO;
  }

  @Override
  public Datatable getListOdType(OdTypeDTO odTypeDTO) {
    BaseDto baseDto = sqlSearch(odTypeDTO, null);
    return getListDataTableBySqlQuery(baseDto.getSqlQuery(),
        baseDto.getParameters(), odTypeDTO.getPage(), odTypeDTO.getPageSize(),
        OdTypeDTO.class, odTypeDTO.getSortName(), odTypeDTO.getSortType());
  }

  @Override
  public Datatable getListDataExport(OdTypeDTO odTypeDTO) {
    BaseDto baseDto = sqlSearch(odTypeDTO, DATA_EXPORT);
    Datatable datatable = new Datatable();
    List<OdTypeExportDTO> odTypeExportDTOS = getNamedParameterJdbcTemplate()
        .query(baseDto.getSqlQuery(), baseDto.getParameters(),
            BeanPropertyRowMapper.newInstance(OdTypeExportDTO.class));
    datatable.setData(odTypeExportDTOS);
    return datatable;
  }

  @Override
  public OdTypeDTO getDetail(Long odTypeId) {
    OdTypeEntity dataEntity = getEntityManager().find(OdTypeEntity.class, odTypeId);
    if (dataEntity != null) {
      OdTypeDTO typeDTO = dataEntity.toDTO();
      List<OdTypeDetailDTO> listTmp = odTypeDetailRepository
          .getListOdTypeDetailByOdTypeId(typeDTO.getOdTypeId());
      if (listTmp != null) {
        typeDTO.setOdTypeDetailDTOS(listTmp);
      }
      List<OdTypeMapLocationDTO> lstOdTypeMapLocation = odTypeMapLocationRepository
          .getListOdTypeMapLocationByOdTypeId(odTypeId);
      if (lstOdTypeMapLocation != null) {
        typeDTO.setOdTypeMapLocationDTOS(lstOdTypeMapLocation);
      }
      return typeDTO;
    }
    return null;
  }

  @Override
  public OdTypeDTO checkOdTypeExist(String odTypeCode) {
    List<OdTypeEntity> dataEntity = (List<OdTypeEntity>) findByMultilParam(OdTypeEntity.class,
        "odTypeCode",
        odTypeCode);
    if (dataEntity != null && dataEntity.size() > 0) {
      return dataEntity.get(0).toDTO();
    }
    return null;
  }

  @Override
  public ResultInSideDto add(OdTypeDTO odTypeDTO) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
    OdTypeEntity odTypeEntity = getEntityManager().merge(odTypeDTO.toEntity());
    Long idTmp = odTypeEntity.getOdTypeId();
    resultInSideDTO.setId(idTmp);
    List<OdTypeDetailDTO> listOdTmp = odTypeDTO.getOdTypeDetailDTOS();
    if (listOdTmp != null) {
      for (OdTypeDetailDTO odTypeDetailDTO : listOdTmp) {
        odTypeDetailDTO.setOdTypeId(idTmp);
        resultInSideDTO = odTypeDetailRepository.add(odTypeDetailDTO);
      }
    }
    List<OdTypeMapLocationDTO> listOdTypeMapLocation = odTypeDTO.getOdTypeMapLocationDTOS();
    if (listOdTypeMapLocation != null) {
      for (OdTypeMapLocationDTO odTypeMapLocationDTO : listOdTypeMapLocation) {
        odTypeMapLocationDTO.setOdTypeId(idTmp);
        resultInSideDTO = odTypeMapLocationRepository.add(odTypeMapLocationDTO);
      }
    }
    return resultInSideDTO;
  }

  @Override
  public ResultInSideDto edit(OdTypeDTO odTypeDTO) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
    getEntityManager().merge(odTypeDTO.toEntity());
    List<OdTypeDetailDTO> listOdTmp = odTypeDTO.getOdTypeDetailDTOS();
    if (listOdTmp != null) {
      for (OdTypeDetailDTO odTypeDetailDTO : listOdTmp) {
        OdTypeDetailDTO odTypeDetailTmp = odTypeDetailRepository
            .checkOdTypeDetailExist(odTypeDTO.getOdTypeId(), odTypeDetailDTO.getPriorityId());
        if (odTypeDetailTmp != null) {
          odTypeDetailTmp.setProcessTime(odTypeDetailDTO.getProcessTime());
          resultInSideDTO = odTypeDetailRepository.edit(odTypeDetailTmp);
        } else {
          odTypeDetailDTO.setOdTypeId(odTypeDTO.getOdTypeId());
          resultInSideDTO = odTypeDetailRepository.add(odTypeDetailDTO);
        }
      }
    }
    //truongnt add new
    odTypeMapLocationRepository.deleteListOdTypeMapLocationDetail(odTypeDTO.getOdTypeId());
    List<OdTypeMapLocationDTO> listOdTypeMapLocation = odTypeDTO.getOdTypeMapLocationDTOS();
    if (listOdTypeMapLocation != null) {
      for (OdTypeMapLocationDTO odTypeMapLocationDTO : listOdTypeMapLocation) {
        odTypeMapLocationDTO.setId(null);
        odTypeMapLocationDTO.setOdTypeId(odTypeDTO.getOdTypeId());
        resultInSideDTO = odTypeMapLocationRepository.add(odTypeMapLocationDTO);
      }
    }
    return resultInSideDTO;
  }

  @Override
  public ResultInSideDto addList(List<OdTypeDTO> odTypeDTO) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    for (OdTypeDTO odTypeTmp : odTypeDTO) {
      resultInSideDTO = add(odTypeTmp);
    }
    return resultInSideDTO;
  }


  @Override
  public String getSeqOdType(String sequense) {
    return getSeqTableBase(sequense);
  }

  public BaseDto sqlSearch(OdTypeDTO odTypeDTO, String export) {
    BaseDto baseDto = new BaseDto();
    String sqlQuery;
    if (DATA_EXPORT.equals(export)) {
      sqlQuery = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_ODTYPE, "od-type-export");
    } else {
      sqlQuery = SQLBuilder.getSqlQueryById(SQLBuilder.SQL_MODULE_ODTYPE, "od-type");
    }
    Map<String, Object> parameters = new HashMap<>();
    if (StringUtils.isNotNullOrEmpty(odTypeDTO.getSearchAll())) {
      sqlQuery += " AND (lower(ot.OD_TYPE_CODE) LIKE :searchAll ESCAPE '\\' OR lower(ot.OD_TYPE_NAME) LIKE :searchAll ESCAPE '\\') ";
      parameters
          .put("searchAll", StringUtils.convertLowerParamContains(odTypeDTO.getSearchAll()));
    }
    if (StringUtils.isNotNullOrEmpty(odTypeDTO.getOdTypeCode())) {
      sqlQuery += " AND LOWER(ot.OD_TYPE_CODE) LIKE :odTypeCode ESCAPE '\\' ";
      parameters
          .put("odTypeCode", StringUtils.convertLowerParamContains(odTypeDTO.getOdTypeCode()));
    }
    if (StringUtils.isNotNullOrEmpty(odTypeDTO.getOdTypeName())) {
      sqlQuery += " AND LOWER(ot.OD_TYPE_NAME) LIKE :odTypeName ESCAPE '\\' ";
      parameters
          .put("odTypeName", StringUtils.convertLowerParamContains(odTypeDTO.getOdTypeName()));
    }
    if (odTypeDTO.getOdGroupTypeId() != null) {
      sqlQuery += " AND ot.OD_GROUP_TYPE_ID = :odGroupTypeId ";
      parameters.put("odGroupTypeId", odTypeDTO.getOdGroupTypeId());
    }
    if (odTypeDTO.getStatus() != null) {
      sqlQuery += " AND ot.STATUS = :status ";
      parameters.put("status", odTypeDTO.getStatus());
    }
    sqlQuery += " order by OD_TYPE_NAME ASC ";
    baseDto.setSqlQuery(sqlQuery);
    baseDto.setParameters(parameters);
    return baseDto;
  }

}
