package com.viettel.gnoc.cr.repository;

import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.commons.utils.ConditionBeanUtil;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.cr.dto.CrCableDTO;
import com.viettel.gnoc.cr.dto.CrInsiteDTO;
import com.viettel.gnoc.cr.model.CrCableEntity;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class CrCableRepositoryImpl extends BaseRepository implements CrCableRepository {

  @Override
  public boolean saveOrUpdateCableDetail(List<CrCableDTO> dtoList, Long crId, Date crCreateTime) {
    try {

      Map<String, Object> params = new HashMap<>();
      params.put("crId", crId);
      String sql = "delete OPEN_PM.CR_CABLE WHERE CR_ID = :crId ";
      if (crCreateTime != null) {
        sql = sql.concat(" and CREATED_DATE >= :crCreateTime ");
        crCreateTime = DateUtils.addDays(crCreateTime, -2);
        params.put("crCreateTime", crCreateTime);
      }

      getNamedParameterJdbcTemplate().update(sql, params);
      getEntityManager().flush();
      getEntityManager().clear();

      if (dtoList == null || dtoList.isEmpty()) {
        return true;
      }

      for (CrCableDTO dto : dtoList) {
        CrCableEntity obj = dto.toEntity();
        obj.setCrId(crId);
        obj.setCreatedDate(new Date());
        getEntityManager().merge(obj);
      }
      getEntityManager().flush();
      getEntityManager().clear();

      return true;
    } catch (Exception e) {
      log.error(e.getMessage(), e);

    } finally {
      getEntityManager().flush();
      getEntityManager().clear();
    }
    return false;
  }

  @Override
  public List<CrCableDTO> getListCrCableByCondition(CrInsiteDTO crInsiteDTO) {
    ConditionBean conditionBean = new ConditionBean("crId", crInsiteDTO.getCrId(),
        Constants.NAME_EQUAL, Constants.NUMBER);
    List<ConditionBean> lstConditionBeans = new ArrayList<>();
    lstConditionBeans.add(conditionBean);
    ConditionBeanUtil.sysToOwnListCondition(lstConditionBeans);
    return (List<CrCableDTO>) onSearchByConditionBean(new CrCableEntity(), lstConditionBeans, 0,
        Integer.MAX_VALUE, "desc", "cableCode");
  }

  @Override
  public List<CrCableDTO> getListCrCableDTO(CrCableDTO crCableDTO, int rowStart, int maxRow,
      String sortType, String sortFieldList) {
    if (crCableDTO != null) {
      return onSearchEntity(CrCableEntity.class, crCableDTO, rowStart, maxRow, sortType,
          sortFieldList);
    }
    return null;
  }
}
