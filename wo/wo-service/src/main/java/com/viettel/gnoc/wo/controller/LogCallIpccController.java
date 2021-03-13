package com.viettel.gnoc.wo.controller;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.wfm.dto.LogCallIpccDTO;
import com.viettel.gnoc.wo.business.LogCallIpccBusiness;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ITSOL
 */
@RestController
@RequestMapping(Constants.OD_API_PATH_PREFIX + "LogCallIpcc")
@Slf4j
public class LogCallIpccController {

  @Autowired
  LogCallIpccBusiness logCallIpccBusiness;

  @PostMapping("/insertLogCallIpcc")
  public ResultInSideDto insertLogCallIpcc(@RequestBody LogCallIpccDTO logCallIpccDTO) {
    return logCallIpccBusiness.insertLogCallIpcc(logCallIpccDTO);
  }

  @GetMapping("/getListLogCallIpccDTO")
  public List<LogCallIpccDTO> getListLogCallIpccDTO(@RequestBody LogCallIpccDTO logCallIpccDTO) {
    return logCallIpccBusiness.getListLogCallIpccDTO(logCallIpccDTO);
  }

  @PostMapping("/updateLogCallIpcc")
  public String updateLogCallIpcc(@RequestBody LogCallIpccDTO logCallIpccDTO) {
    return logCallIpccBusiness.updateLogCallIpcc(logCallIpccDTO);
  }

  @GetMapping("/deleteLogCallIpcc/id{id}")
  public String deleteLogCallIpcc(@PathVariable("id") Long id) {
    return logCallIpccBusiness.deleteLogCallIpcc(id);
  }

  @GetMapping("/deleteListLogCallIpcc")
  public String deleteListLogCallIpcc(@RequestBody List<LogCallIpccDTO> logCallIpccListDTO) {
    return logCallIpccBusiness.deleteListLogCallIpcc(logCallIpccListDTO);
  }

  @GetMapping("/findLogCallIpccById/id{id}")
  public LogCallIpccDTO findLogCallIpccById(@PathVariable("id") Long id) {
    return logCallIpccBusiness.findLogCallIpccById(id);
  }

  @PostMapping("/insertOrUpdateListLogCallIpcc")
  public String insertOrUpdateListLogCallIpcc(
      @RequestBody List<LogCallIpccDTO> logCallIpccListDTO) {
    return logCallIpccBusiness.insertOrUpdateListLogCallIpcc(logCallIpccListDTO);
  }

  @GetMapping("/getSequenseLogCallIpcc/seqName{seqName}/size{size}")
  public List<String> getSequenseLogCallIpcc(@PathVariable("seqName") String seqName,
      @PathVariable("size") int size) {
    return logCallIpccBusiness.getSequenseLogCallIpcc(seqName, size);
  }

  @PostMapping("/getListLogCallIpccByCondition")
  public List<LogCallIpccDTO> getListLogCallIpccByCondition(@RequestBody BaseDto baseDto) {
    return logCallIpccBusiness.getListLogCallIpccByCondition(baseDto);
  }
}
