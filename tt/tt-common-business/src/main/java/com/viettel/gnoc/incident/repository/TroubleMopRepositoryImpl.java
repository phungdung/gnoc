package com.viettel.gnoc.incident.repository;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.commons.utils.Constants.GNOC_FILE_BUSSINESS;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.incident.dto.TroubleMopDtInSideDTO;
import com.viettel.gnoc.incident.dto.TroubleMopInsiteDTO;
import com.viettel.gnoc.incident.model.TroubleMopEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class TroubleMopRepositoryImpl extends BaseRepository implements TroubleMopRepository {

  @Override
  public ResultInSideDto insertTroubleMop(TroubleMopInsiteDTO troubleMopDTO) {
    if (troubleMopDTO.getTroubleMopId() == null) {
      return insertByModel(troubleMopDTO.toEntity(), "troubleMopId");
    } else {
      TroubleMopEntity mopEntity = troubleMopDTO.toEntity();
      getEntityManager().merge(mopEntity);
      return new ResultInSideDto(mopEntity.getTroubleMopId(), RESULT.SUCCESS, RESULT.SUCCESS);
    }
  }

  @Override
  public Datatable getListTroubleMopDTO(TroubleMopInsiteDTO troubleMopDTO) {
    Map<String, Object> parameters = new HashMap<>();
    StringBuilder sql = new StringBuilder();
    sql.append(
        "SELECT  a.MOP_NAME mopName,a.GROUP_MOP_NAME groupMopName,a.TROUBLE_MOP_ID troubleMopId,a.SYSTEM system ,\n"
            + "a.NUMBER_RUN numberRun,a.RUN_CYCLE runCycle,a.MAX_NUMBER_RUN maxNumberRun ,\n"
            + "a.WORK_LOG workLog,a.CREATE_TIME createTime,a.ALARM_ID alarmId,a.MOP_ID mopId,a.RUN_TYPE runType,a.TBL_ALARM_CURRENT tblAlarmCurrent\n"
            + ",a.RULE rule,a.STATE_MOP stateMop,a.DOMAIN domain,a.GROUP_MOP_ID groupMopId,a.LAST_UPDATE_TIME lastUpdateTime,a.IS_RUN isRun,a.TROUBLE_ID troubleId ,\n"
            + "CASE a.RUN_TYPE WHEN 1 THEN '").append(I18n.getString("troubleMOP.list.runType1"))
        .append("' ");
    sql.append(" WHEN 2 THEN '").append(I18n.getString("troubleMOP.list.runType2")).append("' ");
    sql.append(" WHEN 3 THEN '").append(I18n.getString("troubleMOP.list.runType3")).append("' ");
    sql.append(" ELSE '' END runTypeName, ");
    sql.append(" CASE a.STATE_MOP WHEN 0 THEN '")
        .append(I18n.getString("troubleMOP.list.stateMop0")).append("' ");
    sql.append(" WHEN 1 THEN '").append(I18n.getString("troubleMOP.list.stateMop1")).append("' ");
    sql.append(" WHEN 2 THEN '").append(I18n.getString("troubleMOP.list.stateMop2")).append("' ");
    sql.append(" WHEN 3 THEN '").append(I18n.getString("troubleMOP.list.stateMop3")).append("' ");
    sql.append(" ELSE '' END stateMopName ");
    sql.append(" FROM ONE_TM.TROUBLE_MOP a WHERE 1=1 ");
    if (troubleMopDTO.getTroubleId() != null) {
      sql.append(" and  a.TROUBLE_ID=:troubleId ");
      parameters.put("troubleId", troubleMopDTO.getTroubleId());
    }
    if (troubleMopDTO.getTroubleMopId() != null) {
      sql.append(" and  a.TROUBLE_MOP_ID=:troubleMopId ");
      parameters.put("troubleMopId", troubleMopDTO.getTroubleMopId());
    }
    return getListDataTableBySqlQuery(sql.toString(), parameters, troubleMopDTO.getPage(),
        troubleMopDTO.getPageSize(),
        TroubleMopInsiteDTO.class, troubleMopDTO.getSortName(), troubleMopDTO.getSortType());
  }

  @Override
  public TroubleMopInsiteDTO findById(Long id) {
    if (id != null && id > 0) {
      return getEntityManager().find(TroubleMopEntity.class, id).toDTO();
    }
    return null;
  }

  @Override
  public Datatable getListTroubleMopDtDTO(TroubleMopDtInSideDTO troubleMopDtInSideDTO) {
    Map<String, Object> parameters = new HashMap<>();
    String sql = "select a.TROUBLE_DT_ID troubleDtId,a.TROUBLE_MOP_ID troubleMopId ,a.DT_ID dtId \n"
        + "        , a.DT_NAME dtName,\n"
        + "         b.PATH,a.CREATE_TIME createTime,\n"
        + "         a.STATE STATE\n"
        + "        ,a.NODES nodes,\n"
        + "         a.RESULT_DETAIL resultDetail,\n"
        + "         a.DT_FILE_TYPE dtFileType \n"
        + "          FROM COMMON_GNOC.GNOC_FILE b\n"
        + "         LEFT JOIN ONE_TM.TROUBLE_MOP_DT a\n"
        + "        ON (a.TROUBLE_DT_ID = b.MAPPING_ID)\n"
        + " WHERE 1 = 1 AND\n"
        + " b.BUSINESS_CODE  = :businessCode";
    parameters.put("businessCode", GNOC_FILE_BUSSINESS.TROUBLE_MOP_DT);
    if (troubleMopDtInSideDTO.getTroubleMopId() != null) {
      sql += " and a.TROUBLE_MOP_ID=:troubleMopId";
      parameters.put("troubleMopId", troubleMopDtInSideDTO.getTroubleMopId());
    }
    return getListDataTableBySqlQuery(sql, parameters, troubleMopDtInSideDTO.getPage(),
        troubleMopDtInSideDTO.getPageSize(),
        TroubleMopDtInSideDTO.class, troubleMopDtInSideDTO.getSortName(),
        troubleMopDtInSideDTO.getSortType());
  }

  @Override
  public String updateTroubleMop(TroubleMopInsiteDTO troubleMopDTO) {
    getEntityManager().merge(troubleMopDTO.toEntity());
    return RESULT.SUCCESS;
  }

  @Override
  public List<TroubleMopInsiteDTO> getListTroubleMopByCondition(List<ConditionBean> lstCondition,
      int rowStart, int maxRow, String sortType, String sortName) {
    return onSearchByConditionBean(new TroubleMopEntity(), lstCondition, rowStart, maxRow,
        sortType, sortName);
  }

}
