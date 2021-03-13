package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.maintenance.dto.MrScheduleBtsHisFileDTO;
import com.viettel.gnoc.maintenance.model.MrScheduleBtsHisFileEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class MrScheduleBtsHisFileRepositoryImpl extends BaseRepository implements
    MrScheduleBtsHisFileRepository {


  @Override
  public ResultInSideDto deleteMrScheduleBtsHisFileByID(Long idFile) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    if (!StringUtils.isStringNullOrEmpty(idFile)) {
      MrScheduleBtsHisFileEntity entity = getEntityManager()
          .find(MrScheduleBtsHisFileEntity.class, idFile);
      if (entity == null) {
        resultInSideDto.setKey(RESULT.FAIL);
      }
      getEntityManager().remove(entity);
    }
    return resultInSideDto;
  }

  @Override
  public List<MrScheduleBtsHisFileDTO> getListMrScheduleBtsHisFileDTO(MrScheduleBtsHisFileDTO dto,
      int rowStart, int maxRow) {
    String sql;
    Map<String, Object> parameters = new HashMap<>();
    sql = " SELECT T1.CHECK_LIST_ID checklistId, T1.ID_FILE idFile "
        + " FROM OPEN_PM.MR_SCHEDULE_BTS_HIS_FILE T1 "
        + " WHERE 1 = 1 ";
    if (!StringUtils.isStringNullOrEmpty(dto.getChecklistId())) {
      sql += " AND T1.CHECK_LIST_ID =:checklistId ";
      parameters.put("checklistId", dto.getChecklistId());
    }
    if (!StringUtils.isStringNullOrEmpty(dto.getWoId())) {
      sql += " AND T1.WO_ID =:woId ";
      parameters.put("woId", dto.getWoId());
    }
    return getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(MrScheduleBtsHisFileDTO.class));
  }

  @Override
  public MrScheduleBtsHisFileDTO findById(Long id) {
    if (id == null) {
      return null;
    }
    MrScheduleBtsHisFileEntity entity = getEntityManager().find(MrScheduleBtsHisFileEntity.class, id);
    if (entity == null) {
      return null;
    }
    return entity.toDTO();
  }
}
