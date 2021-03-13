package com.viettel.gnoc.cr.repository;

import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.CrAffectedServiceDetailsDTO;
import com.viettel.gnoc.cr.model.CrAffectedServiceDetailsEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class CrAffectedServiceDetailsRepositoryImpl extends BaseRepository implements
    CrAffectedServiceDetailsRepository {

  @Override
  public void deleteAffSerByCrId(String crId) {
    StringBuilder sql = new StringBuilder();
    if (crId == null) {
      return;
    }
    Map<String, Object> param = new HashMap<>();
    param.put("cr_id", crId);
    sql.append(" delete from cr_affected_service_details ");
    sql.append(" where cr_id = :cr_id");
    getNamedParameterJdbcTemplate().update(sql.toString(), param);
    getEntityManager().flush();
  }

  @Override
  public String saveListDTONoIdSession(List<CrAffectedServiceDetailsDTO> obj) {
    try {
      if (obj == null) {
        return RESULT.SUCCESS;
      }
      int i = 0;
      for (CrAffectedServiceDetailsDTO item : obj) {
        if (StringUtils.isStringNullOrEmpty(item.getInsertTime())) {
          item.setInsertTime(DateTimeUtils.getSysDateTime());
        }
        getEntityManager().merge(item.toEntity());
        i++;
        if (i % 100 == 0) {
          getEntityManager().flush();
        }
      }
      return RESULT.SUCCESS;
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      return ex.getMessage();
    }
  }

  @Override
  public List<CrAffectedServiceDetailsDTO> search(CrAffectedServiceDetailsDTO serviceDetailsDTO,
      int rowStart, int maxRow, String sortType, String sortFieldList) {
    return onSearchEntity(CrAffectedServiceDetailsEntity.class, serviceDetailsDTO, rowStart, maxRow,
        sortType, sortFieldList);
  }
}
