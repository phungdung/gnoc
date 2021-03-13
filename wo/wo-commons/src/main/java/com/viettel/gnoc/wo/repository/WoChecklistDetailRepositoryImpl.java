package com.viettel.gnoc.wo.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.wo.dto.WoChecklistDTO;
import com.viettel.gnoc.wo.dto.WoChecklistDetailDTO;
import com.viettel.gnoc.wo.model.WoChecklistDetailEntity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class WoChecklistDetailRepositoryImpl extends BaseRepository implements
    WoChecklistDetailRepository {

  @Override
  public List<WoChecklistDetailDTO> getListWoChecklistDetailDTO(
      WoChecklistDetailDTO woChecklistDetailDTO) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_WO_CHECKLIST_DETAIL,
            "get-List-Wo-Checklist-Detail-DTO");
    Map<String, Object> parameters = new HashMap<>();
    if (woChecklistDetailDTO.getWoChecklistDetailId() != null) {
      sql += " AND a.wo_checklist_detail_id = :woChecklistDetailId";
      parameters.put("woChecklistDetailId", woChecklistDetailDTO.getWoChecklistDetailId());
    }
    if (woChecklistDetailDTO.getWoTypeChecklistId() != null) {
      sql += " AND a.wo_type_checklist_id = :woTypeChecklistId";
      parameters.put("woTypeChecklistId", woChecklistDetailDTO.getWoTypeChecklistId());
    }
    if (StringUtils.isNotNullOrEmpty(woChecklistDetailDTO.getChecklistValue())) {
      sql += " AND a.checklist_value = :checklistValue";
      parameters.put("checklistValue", woChecklistDetailDTO.getChecklistValue());
    }
    if (woChecklistDetailDTO.getWoId() != null) {
      sql += " AND a.wo_id = :woId";
      parameters.put("woId", woChecklistDetailDTO.getWoId());
    }
    return getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(WoChecklistDetailDTO.class));
  }

  @Override
  public ResultInSideDto deleteWoChecklistDetailDTO(Long woChecklistDetailId) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    WoChecklistDetailEntity entity = getEntityManager()
        .find(WoChecklistDetailEntity.class, woChecklistDetailId);
    getEntityManager().remove(entity);
    resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto insertWoChecklistDetailDTO(WoChecklistDetailDTO woChecklistDetailDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    getEntityManager().persist(woChecklistDetailDTO.toEntity());
    resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    return resultInSideDto;
  }

  @Override
  public String insertListWoChecklistDetail(List<WoChecklistDTO> lstChecklist) {
    List<Long> listWoId = new ArrayList<>();
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.FAIL);
    if (lstChecklist != null && lstChecklist.size() > 0) {
      for (WoChecklistDTO dto : lstChecklist) {
        listWoId.add(dto.getWoId());
      }
      String sql = "DELETE WO_CHECKLIST_DETAIL a where a.WO_ID in (:listWoId)";
      Query query = getEntityManager().createNativeQuery(sql);
      query.setParameter("listWoId", listWoId);
//      int result = query.executeUpdate();
      query.executeUpdate();
//      if (result > 0) {
      for (WoChecklistDTO dto : lstChecklist) {
        if (dto.getWoTypeChecklistId() == null) {
          return RESULT.FAIL;
        }
        WoChecklistDetailDTO woChecklistDetailDTO = new WoChecklistDetailDTO();
        woChecklistDetailDTO.setWoTypeChecklistId(dto.getWoTypeChecklistId());
        woChecklistDetailDTO.setChecklistValue(
            dto.getChecklistValue() != null ? dto.getChecklistValue().trim() : "");
        woChecklistDetailDTO.setWoId(dto.getWoId());
        resultInSideDto = insertWoChecklistDetailDTO(woChecklistDetailDTO);
      }
//      }
    }
//    else {
//      resultInSideDto.setKey(RESULT.FAIL);
//    }
    return resultInSideDto.getKey();
  }
}
