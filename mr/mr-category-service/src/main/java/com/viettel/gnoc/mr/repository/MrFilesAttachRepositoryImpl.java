package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.maintenance.dto.MrFilesAttachDTO;
import com.viettel.gnoc.maintenance.model.MrFilesAttachEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class MrFilesAttachRepositoryImpl extends BaseRepository implements MrFilesAttachRepository {

  @Override
  public List<GnocFileDto> getListMrFilesSearch(GnocFileDto dto) {
    BaseDto baseDto = sqlGetListMrFilesSearch(dto);
    List<GnocFileDto> lst = getNamedParameterJdbcTemplate()
        .query(baseDto.getSqlQuery(), baseDto.getParameters(),
            BeanPropertyRowMapper.newInstance(GnocFileDto.class));
    return lst;
  }

  public BaseDto sqlGetListMrFilesSearch(GnocFileDto dto) {
    Map<String, Object> params = new HashMap<>();
    String sql = SQLBuilder.getSqlQueryById(SQLBuilder.MR_FILES_ATTACH, "get-list-mr-files-search");
    if (dto != null) {
      if (!StringUtils.isStringNullOrEmpty(dto.getBusinessCode())) {
        sql += " and a.BUSINESS_CODE = :businessCode ";
        params.put("businessCode", dto.getBusinessCode());
      }
      if (!StringUtils.isStringNullOrEmpty(dto.getBusinessId())) {
        sql += " and a.BUSINESS_ID = :businessId ";
        params.put("businessId", dto.getBusinessId());
      }
    }
    BaseDto baseDto = new BaseDto();
    baseDto.setSqlQuery(sql);
    baseDto.setParameters(params);
    return baseDto;
  }

  @Override
  public ResultInSideDto add(MrFilesAttachDTO mrFilesAttachDTO) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
    MrFilesAttachEntity attachEntity = getEntityManager()
        .merge(mrFilesAttachDTO.toEntity());
    resultInSideDTO.setId(attachEntity.getFileId());
    return resultInSideDTO;
  }

  @Override
  public ResultInSideDto deleteFileById(Long fileId) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    MrFilesAttachEntity mrFilesAttachEntity = getEntityManager()
        .find(MrFilesAttachEntity.class, fileId);
    getEntityManager().remove(mrFilesAttachEntity);
    return resultInSideDto;
  }
}
