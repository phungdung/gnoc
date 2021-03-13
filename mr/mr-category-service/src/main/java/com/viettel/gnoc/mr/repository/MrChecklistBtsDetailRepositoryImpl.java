package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.maintenance.dto.MrChecklistsBtsDetailDTO;
import com.viettel.gnoc.maintenance.dto.MrScheduleBtsHisDetailInsiteDTO;
import com.viettel.gnoc.maintenance.model.MrChecklistsBtsDetailEntity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class MrChecklistBtsDetailRepositoryImpl extends BaseRepository implements
    MrChecklistBtsDetailRepository {

  @Override
  public ResultInSideDto insertMrChecklistBtsDetail(
      MrChecklistsBtsDetailDTO mrChecklistsBtsDetailDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    getEntityManager().persist(mrChecklistsBtsDetailDTO.toEntity());
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto updateMrChecklistBtsDetail(
      MrChecklistsBtsDetailDTO mrChecklistsBtsDetailDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    getEntityManager().merge(mrChecklistsBtsDetailDTO.toEntity());
    resultInSideDto.setId(mrChecklistsBtsDetailDTO.getChecklistDetailId());
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto deleteMrChecklistBtsDetail(Long checklistDetailId) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
    MrChecklistsBtsDetailEntity entity = getEntityManager()
        .find(MrChecklistsBtsDetailEntity.class, checklistDetailId);
    getEntityManager().remove(entity);
    return resultInSideDTO;
  }

  @Override
  public List<MrChecklistsBtsDetailDTO> getListDetailByChecklistId(Long checklistId) {
    List<MrChecklistsBtsDetailDTO> list = new ArrayList<>();
    List<MrChecklistsBtsDetailEntity> listEntity = findByMultilParam(
        MrChecklistsBtsDetailEntity.class, "checklistId", checklistId);
    if (listEntity != null && listEntity.size() > 0) {
      for (MrChecklistsBtsDetailEntity entity : listEntity) {
        list.add(entity.toDTO());
      }
      return list;
    }
    return null;
  }

  @Override
  public Datatable getListDataDetail(MrChecklistsBtsDetailDTO mrChecklistsBtsDetailDTO) {
    BaseDto baseDto = sqlGetListDataDetail(mrChecklistsBtsDetailDTO);
    Datatable datatable = getListDataTableBySqlQuery(baseDto.getSqlQuery(), baseDto.getParameters(),
        mrChecklistsBtsDetailDTO.getPage(), mrChecklistsBtsDetailDTO.getPageSize(),
        MrChecklistsBtsDetailDTO.class, mrChecklistsBtsDetailDTO.getSortName(),
        mrChecklistsBtsDetailDTO.getSortType());
    return datatable;
  }

  @Override
  public List<MrChecklistsBtsDetailDTO> getListDetail(MrChecklistsBtsDetailDTO mrChecklistsBtsDetailDTO) {
    BaseDto baseDto = sqlGetListDataDetail(mrChecklistsBtsDetailDTO);
    return getNamedParameterJdbcTemplate().query(baseDto.getSqlQuery(), baseDto.getParameters(),
        BeanPropertyRowMapper.newInstance(MrChecklistsBtsDetailDTO.class));
  }

  @Override
  public List<MrScheduleBtsHisDetailInsiteDTO> getAllCheckListLv2(String woCodeOrignal) {
    Map<String, Object> params = new HashMap<>();
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_MR_CHECKLIST_BTS_DETAIL, "get-all-checklist-level2");
    params.put("wo_ori", woCodeOrignal);
    return getNamedParameterJdbcTemplate().query(sql, params, BeanPropertyRowMapper.newInstance(MrScheduleBtsHisDetailInsiteDTO.class));

  }

  private BaseDto sqlGetListDataDetail(MrChecklistsBtsDetailDTO mrChecklistsBtsDetailDTO) {
    BaseDto baseDto = new BaseDto();
    String leeLocale = I18n.getLocale();
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_MR_CHECKLIST_BTS_DETAIL, "sql-Get-List-Data-Detail");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("systemMrCheck", Constants.LANGUAGUE_EXCHANGE_SYSTEM.CR_MR);
    parameters.put("bussinessMrCheck", Constants.APPLIED_BUSSINESS.CHECKLIST_BTS);
    parameters.put("leeLocale", leeLocale);
    parameters.put("bussinessContent", Constants.MR_CHECKLIST_BTS_BUSSINESS_CODE.CONTENT);
    parameters.put("bussinessCapture", Constants.MR_CHECKLIST_BTS_BUSSINESS_CODE.CAPTURE_GUIDE);
    if (mrChecklistsBtsDetailDTO.getChecklistId() != null) {
      sql += " AND d.CHECKLIST_ID = :checklistId";
      parameters.put("checklistId", mrChecklistsBtsDetailDTO.getChecklistId());
    }
    sql += " ORDER BY d.CHECKLIST_DETAIL_ID ASC ";
    baseDto.setSqlQuery(sql);
    baseDto.setParameters(parameters);
    return baseDto;
  }
}
