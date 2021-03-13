package com.viettel.gnoc.wo.repository;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.wfm.dto.LogCallIpccDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface LogCallIpccRepository {

  ResultInSideDto insertLogCallIpcc(LogCallIpccDTO logCallIpccDTO);

  Datatable getListLogCallIpccDTO(LogCallIpccDTO logCallIpccDTO);

  List<LogCallIpccDTO> getListLogCallIpccByTransactionId(String transactionId);

  String updateLogCallIpcc(LogCallIpccDTO logCallIpccDTO);

  String deleteLogCallIpcc(Long id);

  String deleteListLogCallIpcc(List<LogCallIpccDTO> logCallIpccListDTO);

  LogCallIpccDTO findLogCallIpccById(Long id);

  String insertOrUpdateListLogCallIpcc(List<LogCallIpccDTO> logCallIpccListDTO);

  List<String> getSequenseLogCallIpcc(String seqName, int... size);

  List<LogCallIpccDTO> getListLogCallIpccByCondition(BaseDto baseDto);
}
