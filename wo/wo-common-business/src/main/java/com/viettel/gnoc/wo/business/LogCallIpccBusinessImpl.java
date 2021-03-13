package com.viettel.gnoc.wo.business;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.wfm.dto.LogCallIpccDTO;
import com.viettel.gnoc.wo.repository.LogCallIpccRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class LogCallIpccBusinessImpl implements LogCallIpccBusiness {

  @Autowired
  LogCallIpccRepository logCallIpccRepository;

  @Override
  public ResultInSideDto insertLogCallIpcc(LogCallIpccDTO logCallIpccDTO) {
    return logCallIpccRepository.insertLogCallIpcc(logCallIpccDTO);
  }

  @Override
  public List<LogCallIpccDTO> getListLogCallIpccDTO(LogCallIpccDTO logCallIpccDTO) {
    return (List<LogCallIpccDTO>) logCallIpccRepository.getListLogCallIpccDTO(logCallIpccDTO)
        .getData();
  }

  @Override
  public String updateLogCallIpcc(LogCallIpccDTO logCallIpccDTO) {
    return logCallIpccRepository.updateLogCallIpcc(logCallIpccDTO);
  }

  @Override
  public String deleteLogCallIpcc(Long id) {
    return logCallIpccRepository.deleteLogCallIpcc(id);
  }

  @Override
  public String deleteListLogCallIpcc(List<LogCallIpccDTO> logCallIpccListDTO) {
    return logCallIpccRepository.deleteListLogCallIpcc(logCallIpccListDTO);
  }

  @Override
  public LogCallIpccDTO findLogCallIpccById(Long id) {
    return logCallIpccRepository.findLogCallIpccById(id);
  }

  @Override
  public String insertOrUpdateListLogCallIpcc(List<LogCallIpccDTO> logCallIpccListDTO) {
    return logCallIpccRepository.insertOrUpdateListLogCallIpcc(logCallIpccListDTO);
  }

  @Override
  public List<String> getSequenseLogCallIpcc(String seqName, int... size) {
    return logCallIpccRepository.getSequenseLogCallIpcc(seqName, size);
  }

  @Override
  public List<LogCallIpccDTO> getListLogCallIpccByCondition(BaseDto baseDto) {
    return logCallIpccRepository.getListLogCallIpccByCondition(baseDto);
  }


}
