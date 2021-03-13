package com.viettel.gnoc.sr.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.sr.dto.SRMopDTO;
import com.viettel.gnoc.sr.model.SRMopEntity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class SRMopRepositoryImpl extends BaseRepository implements SRMopRepository {

  @Override
  public List<SRMopDTO> getListSRMopDTO(SRMopDTO dto) {
    log.info("Request to getListSRMopNotSR : {}", dto);
    List<SRMopEntity> lstMop = findByMultilParam(SRMopEntity.class, "srId",
        Long.valueOf(dto.getSrId()));
    List<SRMopDTO> lstDto = new ArrayList<>();
    for (SRMopEntity item : lstMop) {
      SRMopDTO mopDTO = item.toDTO();
      if (mopDTO != null) {
        lstDto.add(mopDTO);
      }
    }
    return lstDto;
  }

  @Override
  public List<SRMopDTO> getListSRMopNotSR(SRMopDTO dto) {
    log.info("Request to getListSRMopNotSR : {}", dto);
    String sqlQuery = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_SR_MOP, "getListSRMopNotSR");
    Map<String, Object> parameters = new HashMap<>();
    return getNamedParameterJdbcTemplate()
        .query(sqlQuery, parameters, BeanPropertyRowMapper.newInstance(SRMopDTO.class));
  }

  @Override
  public ResultInSideDto insertListSRMop(List<SRMopDTO> lsSrMop) {
    log.info("Request to insertListSRMop : {}", lsSrMop);
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    String lstId = "";
    for (SRMopDTO dto : lsSrMop) {
      try {
        SRMopEntity srMopEntity = getEntityManager().merge(dto.toEntity());
        if (srMopEntity != null) {
          log.info("\nRequest to insertListSRMop :" + "MOP ID :" + String.valueOf(dto.getId())
              + "MOP SR_ID: " + String.valueOf(dto.getSrId()));
          log.info(
              "\nMOP ENTITY :" + (String.valueOf(srMopEntity.getId()) + "_" + String
                  .valueOf(srMopEntity.getSrId())));
          lstId += srMopEntity.getId() + ",";
        }
      } catch (Exception e) {
        resultInSideDto.setKey(Constants.RESULT.FAIL);
        resultInSideDto.setMessage(e.getMessage());
        return resultInSideDto;
      }
    }
    if (lstId != null && lstId.endsWith(",")) {
      lstId = lstId.substring(0, lstId.length() - 1);
      resultInSideDto.setKey(Constants.RESULT.SUCCESS);
      resultInSideDto.setMessage(Constants.RESULT.SUCCESS);
      resultInSideDto.setIdValue(lstId);
    }

    return resultInSideDto;
  }

  @Override
  public ResultInSideDto updateListSRMop(List<SRMopDTO> lsSrMop) {
    log.info("Request to updateListSRMop : {}", lsSrMop);
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    for (SRMopDTO dto : lsSrMop) {
      try {
        log.info("\nRequest to updateListSRMop :" + "MOP ID :" + String.valueOf(dto.getId())
            + "MOP SR_ID: " + String.valueOf(dto.getSrId()));
        getEntityManager().merge(dto.toEntity());
      } catch (Exception e) {
        resultInSideDto.setKey(Constants.RESULT.FAIL);
        resultInSideDto.setMessage(e.getMessage());
        return resultInSideDto;
      }
    }
    resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    resultInSideDto.setMessage(Constants.RESULT.SUCCESS);
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto deleteListSRMop(List<SRMopDTO> lsSrMop) {
    log.info("Request to deleteListSRMop : {}", lsSrMop);
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    for (SRMopDTO dto : lsSrMop) {
      try {
        SRMopEntity srMopEntity = getEntityManager().find(SRMopEntity.class,
            StringUtils.isStringNullOrEmpty(dto.getId()) ? null : Long.parseLong(dto.getId()));
        log.info("\nRequest to deleteListSRMop :" + "MOP ID :" + String.valueOf(dto.getId())
            + "MOP SR_ID: " + String.valueOf(dto.getSrId()));
        log.info(
            "\nMOP ENTITY :" + (srMopEntity != null ? (String.valueOf(srMopEntity.getId()) + "_"
                + String
                .valueOf(srMopEntity.getSrId())) : ""));
        getEntityManager().remove(srMopEntity);
      } catch (Exception e) {
        resultInSideDto.setKey(Constants.RESULT.FAIL);
        resultInSideDto.setMessage(e.getMessage());
        return resultInSideDto;
      }
    }
    resultInSideDto.setKey(Constants.RESULT.SUCCESS);
    resultInSideDto.setMessage(Constants.RESULT.SUCCESS);
    return resultInSideDto;
  }
}
