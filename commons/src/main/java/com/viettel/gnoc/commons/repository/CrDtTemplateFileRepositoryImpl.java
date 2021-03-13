package com.viettel.gnoc.commons.repository;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.CrDtTemplateFileDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.model.CrDtTemplateFileEntity;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class CrDtTemplateFileRepositoryImpl extends BaseRepository implements
    CrDtTemplateFileRepository {

  @Override
  public BaseDto sqlSearch(CrDtTemplateFileDTO crDtTemplateFileDTO) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_COMMON, "getListCrDtTemplateFile");
    Map<String, Object> parameters = new HashMap<>();
    if (crDtTemplateFileDTO != null) {
      if (!StringUtils.isStringNullOrEmpty(crDtTemplateFileDTO.getCrProcessParentId())) {
        sql += " and c.CR_PROCESS_ID = :crPROCESSParentId ";
        parameters.put("crPROCESSParentId", crDtTemplateFileDTO.getCrProcessParentId());
      }
      if (!StringUtils.isStringNullOrEmpty(crDtTemplateFileDTO.getCrProcessId())) {
        sql += ("and a.CR_PROCESS_ID = :crPROCESSId  ");
        parameters.put("crPROCESSId", crDtTemplateFileDTO.getCrProcessId());
      }
      if (StringUtils.isNotNullOrEmpty(crDtTemplateFileDTO.getFileName())) {
        sql += ("and lower(a.FILE_NAME) like :fileName ESCAPE '\\' ");
        parameters.put("fileName",
            StringUtils.convertLowerParamContains(crDtTemplateFileDTO.getFileName()));
      }
      if (StringUtils.isNotNullOrEmpty(crDtTemplateFileDTO.getTemplateType())) {
        sql += ("and a.TEMPLATE_TYPE = :templateType  ");
        parameters.put("templateType", crDtTemplateFileDTO.getTemplateType());
      }
      if (!StringUtils.isStringNullOrEmpty(crDtTemplateFileDTO.getCrDtTemplateFileId())) {
        sql += " and a.CR_DT_TEMPLATE_FILE_ID = :id ";
        parameters.put("id", crDtTemplateFileDTO.getCrDtTemplateFileId());
      }
    }
    BaseDto baseDto = new BaseDto();
    baseDto.setSqlQuery(sql);
    baseDto.setParameters(parameters);
    return baseDto;
  }

  @Override
  public Datatable getListCrDtTemplateFile(CrDtTemplateFileDTO crDtTemplateFileDTO) {
    BaseDto baseDto = sqlSearch(crDtTemplateFileDTO);
    return getListDataTableBySqlQuery(baseDto.getSqlQuery(), baseDto.getParameters(),
        crDtTemplateFileDTO.getPage(), crDtTemplateFileDTO.getPageSize(), CrDtTemplateFileDTO.class,
        crDtTemplateFileDTO.getSortName(), crDtTemplateFileDTO.getSortType());
  }

  @Override
  public ResultInSideDto saveOrUpdate(CrDtTemplateFileDTO crDtTemplateFileDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    CrDtTemplateFileEntity entity = getEntityManager().merge(crDtTemplateFileDTO.toEntity());
    resultInSideDto.setId(entity.getCrDtTemplateFileId());
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto delete(Long id) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    CrDtTemplateFileEntity entity = getEntityManager().find(CrDtTemplateFileEntity.class, id);
    getEntityManager().remove(entity);
    resultInSideDto.setId(id);
    return resultInSideDto;
  }

  @Override
  public CrDtTemplateFileDTO getObjById(Long paramLong) {
    CrDtTemplateFileDTO crDtTemplateFileDTO = new CrDtTemplateFileDTO();
    crDtTemplateFileDTO.setCrDtTemplateFileId(paramLong);
    BaseDto baseDto = sqlSearch(crDtTemplateFileDTO);
    List<CrDtTemplateFileDTO> lst = getNamedParameterJdbcTemplate()
        .query(baseDto.getSqlQuery(), baseDto.getParameters(),
            BeanPropertyRowMapper.newInstance(CrDtTemplateFileDTO.class));
    return lst.isEmpty() ? null : lst.get(0);
  }
}
