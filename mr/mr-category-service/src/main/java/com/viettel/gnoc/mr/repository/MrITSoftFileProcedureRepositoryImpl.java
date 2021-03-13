package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.maintenance.dto.MrCfgFileProcedureDTO;
import com.viettel.gnoc.maintenance.model.MrCfgFileProcedureEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class MrITSoftFileProcedureRepositoryImpl extends BaseRepository implements
    MrITSoftFileProcedureRepository {

  @Override
  public ResultInSideDto insertOrUpdateFiles(MrCfgFileProcedureDTO mrCfgFileProcedureDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    MrCfgFileProcedureEntity mrCfgFileProcedureEntity = getEntityManager()
        .merge(mrCfgFileProcedureDTO.toEntity());
    resultInSideDto.setId(mrCfgFileProcedureEntity.getFileId());
    return resultInSideDto;
  }

  @Override
  public int deleteByCfgProcedureId(Long cfgProcedureId) {
    String sql = "DELETE FROM " + MrCfgFileProcedureEntity.class.getSimpleName()
        + " t where 1 = 1 AND t.procedureId = :procedureId ";
    Query query = getEntityManager().createQuery(sql);
    query.setParameter("procedureId", cfgProcedureId);
    return query.executeUpdate();
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

  @Override
  public ResultInSideDto delete(Long mrCfgFileId) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    if (mrCfgFileId != null) {
      MrCfgFileProcedureEntity mrCfgFileProcedureEntity = getEntityManager()
          .find(MrCfgFileProcedureEntity.class, mrCfgFileId);
      if (mrCfgFileProcedureEntity != null) {
        getEntityManager().remove(mrCfgFileProcedureEntity);
      }
    }
    return resultInSideDto;
  }

}
