package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.maintenance.dto.MrCfgFileProcedureTelDTO;
import com.viettel.gnoc.maintenance.model.MrCfgFileProcedureTelEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class MrCfgFileProcedureTelRepositoryImpl extends BaseRepository implements
    MrCfgFileProcedureTelRepository {

  @Override
  public ResultInSideDto insertOrUpdate(MrCfgFileProcedureTelDTO mrCfgFileProcedureTelDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    MrCfgFileProcedureTelEntity mrCfgProcedureTelEntity = getEntityManager()
        .merge(mrCfgFileProcedureTelDTO.toEntity());
    resultInSideDto.setId(mrCfgProcedureTelEntity.getFileId());
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto delete(Long mrCfgFileId) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    if (mrCfgFileId != null) {
      MrCfgFileProcedureTelEntity mrCfgProcedureTelEntity = getEntityManager()
          .find(MrCfgFileProcedureTelEntity.class, mrCfgFileId);
      if (mrCfgProcedureTelEntity != null) {
        getEntityManager().remove(mrCfgProcedureTelEntity);
      }
    }
    return resultInSideDto;
  }

  @Override
  public int deleteByCfgProcedureId(Long cfgProcedureId) {
    String sql = "DELETE FROM " + MrCfgFileProcedureTelEntity.class.getSimpleName()
        + " t where 1 = 1 AND t.procedureId = :procedureId ";
    Query query = getEntityManager().createQuery(sql);
    query.setParameter("procedureId", cfgProcedureId);
    return query.executeUpdate();
  }

  @Override
  public MrCfgFileProcedureTelDTO getDetail(Long mrCfgFileId) {
    MrCfgFileProcedureTelEntity mrCfgProcedureTelEntity = getEntityManager()
        .find(MrCfgFileProcedureTelEntity.class, mrCfgFileId);
    MrCfgFileProcedureTelDTO mrCfgProcedureTelDTO = mrCfgProcedureTelEntity.toDTO();
    return mrCfgProcedureTelDTO;
  }

  @Override
  public List<MrCfgFileProcedureTelDTO> onSearchEntity(
      MrCfgFileProcedureTelDTO mrCfgFileProcedureTelDTO, int rowStart, int maxRow, String sortType,
      String sortFieldList) {
    return onSearchEntity(MrCfgFileProcedureTelEntity.class, mrCfgFileProcedureTelDTO, rowStart,
        maxRow, sortType, sortFieldList);
  }

  @Override
  public List<GnocFileDto> getCfgProcedureFileDetail(Long cfgProcedureId, String system) {
    Map<String, Object> params = new HashMap<>();
    StringBuilder sql = new StringBuilder(
        SQLBuilder.getSqlQueryById("cfgProcedureTelAttach", "get-files"));
    sql.append(" and a.BUSINESS_CODE = :businessCode ");
    params.put("businessCode", system);

    if (cfgProcedureId != null) {
      sql.append(" and a.BUSINESS_ID = :businessId ");
      params.put("businessId", cfgProcedureId);
    }
    return getNamedParameterJdbcTemplate()
        .query(sql.toString(), params, BeanPropertyRowMapper.newInstance(GnocFileDto.class));
  }
}
