package com.viettel.gnoc.cr.repository;

import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.repository.UserRepository;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.CrHisDTO;
import com.viettel.gnoc.cr.model.CrHisEntity;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class CrHisRepositoryImpl extends BaseRepository implements CrHisRepository {

  @Autowired
  UserRepository userRepository;

  @Autowired
  TicketProvider ticketProvider;

  @Override
  public ResultInSideDto insertOrUpdate(CrHisDTO dto) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(RESULT.SUCCESS);
    resultInSideDTO.setMessage(RESULT.SUCCESS);
    CrHisEntity entity = dto.toEntity();
    entity.setChangeDate(new Date());
    if (entity.getChsId() != null) {
      getEntityManager().merge(entity);
    } else {
      getEntityManager().persist(entity);
    }
    resultInSideDTO.setId(entity.getChsId());
    return resultInSideDTO;
  }

  @Override
  public List<CrHisDTO> getListCrHis(CrHisDTO crhisdto) {
    String sql = SQLBuilder.getSqlQueryById(
        SQLBuilder.SQL_MODULE_CR_HIS, "get-list-cr-his");
    Map<String, Object> parameters = new HashMap<>();
    Double offset = userRepository.getOffsetFromUser(ticketProvider.getUserToken().getUserID());
    if (crhisdto != null) {
      parameters.put("offset", offset);

      if (!StringUtils.isStringNullOrEmpty(crhisdto.getCrId())) {
        parameters.put("crId", crhisdto.getCrId());
      }
    }
//    return getListDataTableBySqlQuery(sql, parameters,
//        crhisdto.getPage(), crhisdto.getPageSize(),
//        CrHisDTO.class, crhisdto.getSortName(),
//        crhisdto.getSortType());
    return getNamedParameterJdbcTemplate()
        .query(sql, parameters, BeanPropertyRowMapper.newInstance(CrHisDTO.class));
  }

  @Override
  public List<CrHisDTO> search(CrHisDTO tDTO, int start, int maxResult, String sortType,
      String sortField) {
    return onSearchEntity(CrHisEntity.class, tDTO, start, maxResult, sortType, sortField);
  }
}
