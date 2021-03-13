package com.viettel.gnoc.wo.business;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.wfm.dto.LogCallIpccDTO;
import java.util.List;

public interface LogCallIpccBusiness {


  ResultInSideDto insertLogCallIpcc(LogCallIpccDTO logCallIpccDTO);

  List<LogCallIpccDTO> getListLogCallIpccDTO(LogCallIpccDTO logCallIpccDTO);

  String updateLogCallIpcc(LogCallIpccDTO logCallIpccDTO);

  String deleteLogCallIpcc(Long id);

  String deleteListLogCallIpcc(List<LogCallIpccDTO> logCallIpccListDTO);

  LogCallIpccDTO findLogCallIpccById(Long id);

  String insertOrUpdateListLogCallIpcc(List<LogCallIpccDTO> logCallIpccListDTO);

  List<String> getSequenseLogCallIpcc(String seqName, int... size);

  List<LogCallIpccDTO> getListLogCallIpccByCondition(BaseDto baseDto);
}
