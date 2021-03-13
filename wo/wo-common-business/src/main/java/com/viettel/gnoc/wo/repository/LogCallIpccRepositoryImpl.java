package com.viettel.gnoc.wo.repository;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.SQLBuilder;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.wfm.dto.LogCallIpccDTO;
import com.viettel.gnoc.wo.model.LogCallIpccEntity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class LogCallIpccRepositoryImpl extends BaseRepository implements LogCallIpccRepository {

  @Override
  public ResultInSideDto insertLogCallIpcc(LogCallIpccDTO logCallIpccDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    LogCallIpccEntity logCallIpccEntity = getEntityManager().merge(logCallIpccDTO.toEntity());
    resultInSideDto.setId(logCallIpccEntity.getId());
    resultInSideDto.setKey(RESULT.SUCCESS);
    return resultInSideDto;
  }

  @Override
  public Datatable getListLogCallIpccDTO(LogCallIpccDTO logCallIpccDTO) {
    String sql = SQLBuilder
        .getSqlQueryById(SQLBuilder.SQL_MODULE_LOG_CALL_IPCC, "get-List-Log-Call-Ipcc-DTO");
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("woId", logCallIpccDTO.getWoId());
    return getListDataTableBySqlQuery(sql, parameters, logCallIpccDTO.getPage(),
        logCallIpccDTO.getPageSize(), LogCallIpccDTO.class, logCallIpccDTO.getSortName(),
        logCallIpccDTO.getSortType());
  }

  @Override
  public List<LogCallIpccDTO> getListLogCallIpccByTransactionId(String transactionId) {
    if (StringUtils.isNotNullOrEmpty(transactionId)) {
      List<LogCallIpccEntity> listEntity = findByMultilParam(LogCallIpccEntity.class,
          "transactionId", transactionId);
      List<LogCallIpccDTO> list = new ArrayList<>();
      if (listEntity != null && listEntity.size() > 0) {
        for (LogCallIpccEntity entity : listEntity) {
          list.add(entity.toDTO());
        }
      }
      return list;
    }
    return null;
  }

  @Override
  public String updateLogCallIpcc(LogCallIpccDTO logCallIpccDTO) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
    getEntityManager().merge(logCallIpccDTO.toEntity());
    return resultInSideDTO.getKey();
  }

  @Override
  public String deleteLogCallIpcc(Long id) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
    LogCallIpccEntity logCallIpccEntity = getEntityManager().find(LogCallIpccEntity.class, id);
    getEntityManager().remove(logCallIpccEntity);
    return resultInSideDTO.getKey();
  }

  @Override
  public String deleteListLogCallIpcc(List<LogCallIpccDTO> logCallIpccListDTO) {
    String result = "";
    for (LogCallIpccDTO dto : logCallIpccListDTO) {
      if (!DataUtil.isNullOrEmpty(dto.getId())) {
        result = deleteLogCallIpcc(Long.valueOf(dto.getId()));
      }
    }
    return result;
  }

  @Override
  public LogCallIpccDTO findLogCallIpccById(Long id) {
    LogCallIpccEntity dataEntity = getEntityManager().find(LogCallIpccEntity.class, id);
    if (dataEntity != null) {
      return dataEntity.toDTO();
    }
    return null;
  }

  @Override
  public String insertOrUpdateListLogCallIpcc(List<LogCallIpccDTO> logCallIpccListDTO) {
    String result = "";
    for (LogCallIpccDTO dto : logCallIpccListDTO) {
      if (!DataUtil.isNullOrEmpty(dto.getId())) {
        result = updateLogCallIpcc(dto);
      } else {
        result = insertLogCallIpcc(dto).getKey();
      }
    }
    return result;
  }

  @Override
  public List<String> getSequenseLogCallIpcc(String seqName, int... size) {
    return getListSequense(seqName, size);
  }

  @Override
  public List<LogCallIpccDTO> getListLogCallIpccByCondition(BaseDto baseDto) {
    return onSearchByConditionBean(new LogCallIpccEntity(), baseDto.getLstCondition(),
        baseDto.getPage(), baseDto.getPageSize(),
        baseDto.getSortType(), baseDto.getSortName());
  }

}
