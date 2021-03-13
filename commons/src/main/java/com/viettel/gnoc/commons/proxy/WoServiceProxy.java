package com.viettel.gnoc.commons.proxy;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.WoSearchWebDTO;
import com.viettel.gnoc.wfm.dto.LogCallIpccDTO;
import com.viettel.gnoc.wo.dto.CfgWoTickHelpDTO;
import com.viettel.gnoc.wo.dto.RequestApiWODTO;
import com.viettel.gnoc.wo.dto.WoDTO;
import com.viettel.gnoc.wo.dto.WoDTOSearch;
import com.viettel.gnoc.wo.dto.WoHistoryDTO;
import com.viettel.gnoc.wo.dto.WoHistoryInsideDTO;
import com.viettel.gnoc.wo.dto.WoInsideDTO;
import com.viettel.gnoc.wo.dto.WoPriorityDTO;
import com.viettel.gnoc.wo.dto.WoStatisticDTO;
import com.viettel.gnoc.wo.dto.WoUpdateStatusForm;
import com.viettel.gnoc.wo.dto.WoWorklogDTO;
import java.text.ParseException;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "wo-service")
public interface WoServiceProxy {

  @GetMapping("/Wo/getListWoByWoTypeId/woTypeId{woTypeId}")
  public List<WoInsideDTO> getListWoByWoTypeId(@PathVariable(value = "woTypeId") Long woTypeId);

  @PostMapping("/Wo/insertWoWorklogProxy")
  public ResultDTO insertWoWorklogProxy(@RequestBody WoWorklogDTO woWorklogDTO);

  @PostMapping("/Wo/getListDataSearchProxy")
  public List<WoDTOSearch> getListDataSearchProxy(@RequestBody WoDTOSearch woDTOSearch);

  @PostMapping("/Wo/getWOStatisticProxy")
  public List<ResultDTO> getWOStatisticProxy(@RequestBody WoStatisticDTO woStatisticDTO);

  @PostMapping("/Wo/getWOTotalProxy")
  Integer getWOTotalProxy(@RequestBody WoDTOSearch woDTOSearch);

  @PostMapping("/Wo/findWoByIdProxy{woId}")
  public WoInsideDTO findWoByIdProxy(@PathVariable(value = "woId") Long woId);

  @PostMapping("/Wo/insertWoForSPMProxy")
  public ResultDTO insertWoForSPMProxy(@RequestBody WoDTO woDTO);

  @PostMapping("/Wo/changeStatusWoProxy")
  public ResultDTO changeStatusWoProxy(@RequestBody WoUpdateStatusForm woDTO);

  @GetMapping("/Wo/findWoByIdWSProxy/woId{woId}")
  public WoDTO findWoByIdWSProxy(@PathVariable(value = "woId") Long woId);

  @PostMapping("/WoVsmart/updateCfgWoTickHelpVsmart")
  public ResultDTO updateCfgWoTickHelpVsmart(
      @RequestBody CfgWoTickHelpDTO cfgWoTickHelpDTO);

  @PostMapping("/Wo/createWoProxy")
  public ResultDTO createWoProxy(@RequestBody WoDTO woDTO);

  @GetMapping("/Wo/deleteWOForRollbackProxy/woCode{woCode}/reason{reason}/system{system}")
  public ResultDTO deleteWOForRollbackProxy(@PathVariable(value = "woCode") String woCode,
      @PathVariable(value = "reason") String reason, @PathVariable(value = "system") String system);

  @PostMapping("/Wo/updateWoProxy")
  public String updateWoProxy(@RequestBody WoDTO woDTO);

  @GetMapping("/Wo/deleteWo/woId{woId}")
  public ResultInSideDto deleteWo(@PathVariable("woId") Long woId);

  @PostMapping("/Wo/getListDataForRisk")
  public List<WoInsideDTO> getListDataForRisk(@RequestBody WoInsideDTO woInsideDTO);

  @PostMapping("/Wo/getWoPriorityByWoTypeID")
  public ResponseEntity<List<WoPriorityDTO>> getWoPriorityByWoTypeID(Long woTypeId);

  @PostMapping("/Wo/insertWoFromWebInMrMNGT")
  public ResultInSideDto insertWoFromWebInMrMNGT(
      @RequestBody WoInsideDTO woInsideDTO) throws Exception;

  @GetMapping("/WoServices/findWoById")
  public WoDTO findWoById(@PathVariable("woDTOId") Long valueOf);

  @PostMapping("/Wo/getListDataSearchWeb")
  public Datatable getListDataSearchWeb(@RequestBody WoInsideDTO woInsideDTO);

  @PostMapping("/Wo/getListDataSearchWebProxy")
  public List<WoInsideDTO> getListDataSearchWebProxy(@RequestBody WoInsideDTO woInsideDTO);

  @GetMapping("/Wo/getWoSearchWebDTOByWoCode/code{code}")
  public WoSearchWebDTO getWoSearchWebDTOByWoCode(
      @PathVariable("code") String code);

  @PostMapping("/Wo/completeWorkHelp")
  public ResultDTO completeWorkHelp(@RequestBody RequestApiWODTO requestApiWODTO);

  @PostMapping("/Wo/closeWoProxy")
  public ResultDTO closeWoProxy(@RequestBody RequestApiWODTO requestApiWODTO);

  @PostMapping("/Wo/updateWoForSPM")
  public ResultDTO updateWoForSPMProxy(@RequestBody WoDTO woDTO);

  @PostMapping("/Wo/updateWoInfo")
  public ResultDTO updateWoInfoProxy(@RequestBody WoDTO woDTO) throws Exception;

  @PostMapping("/Wo/updatePendingWo")
  public ResultDTO updatePendingWo(@RequestBody RequestApiWODTO requestApiWODTO) throws Exception;

  @PostMapping("/Wo/pendingWo")
  public ResultDTO pendingWo(@RequestBody RequestApiWODTO requestApiWODTO) throws Exception;

  @PostMapping("/Wo/closeWoForSPM")
  public ResultDTO closeWoForSPM(@RequestBody RequestApiWODTO requestApiWODTO);

  @GetMapping("/Wo/getConfigProperty")
  public String getConfigProperty();

  @PostMapping("/Wo/createWoFollowNode")
  public ResultDTO createWoFollowNode(@RequestBody RequestApiWODTO requestApiWODTO);

  @PostMapping("/Wo/getListWoHistoryBySystem")
  public List<WoHistoryDTO> getListWoHistoryBySystem(@RequestBody RequestApiWODTO requestApiWODTO)
      throws ParseException;

  @PostMapping("/Wo/insertWoHistory")
  public ResultInSideDto insertWoHistory(@RequestBody WoHistoryInsideDTO woHistoryInsideDTO);

  @PostMapping("/Wo/getListWoByCondition")
  public List<WoDTO> getListWoByCondition(@RequestBody BaseDto baseDto);

  @GetMapping("/Wo/getSequenseWoProxy/sequenseWo{sequenseWo}/size{size}")
  List<String> getSequenseWoProxy(@PathVariable("sequenseWo") String sequenseWo,
      @PathVariable("size") int size);

  @PostMapping("/Wo/insertWoProxy")
  ResultDTO insertWoProxy(@RequestBody WoDTO woDTO);

  /*@PostMapping("/Wo/getListWoDTO/rowStart{rowStart}/maxRow{maxRow}/sortType{sortType}/sortFieldList{sortFieldList}")
  public List<WoDTO> getListWoDTO(@RequestBody WoDTO woDTO,
      @PathVariable(value="rowStart") int rowStart, @PathVariable(value="maxRow") int maxRow,
      @PathVariable(value="sortType") String sortType, @PathVariable(value="sortFieldList") String sortFieldList);*/

  @PostMapping("/Wo/getListWoDTOByWoSystemId/woSystemId{woSystemId}")
  public List<WoInsideDTO> getListWoDTOByWoSystemId(@PathVariable(value="woSystemId") String woSystemId);

  @PostMapping("/LogCallIpcc/insertLogCallIpcc")
  public ResultInSideDto insertLogCallIpcc(@RequestBody LogCallIpccDTO logCallIpccDTO);

  @GetMapping("/LogCallIpcc/getListLogCallIpccDTO")
  public List<LogCallIpccDTO> getListLogCallIpccDTO(@RequestBody LogCallIpccDTO logCallIpccDTO);

  @PostMapping("/LogCallIpcc/updateLogCallIpcc")
  public String updateLogCallIpcc(@RequestBody LogCallIpccDTO logCallIpccDTO);

  @GetMapping("/LogCallIpcc/deleteLogCallIpcc/id{id}")
  public String deleteLogCallIpcc(@PathVariable("id") Long id);

  @GetMapping("/LogCallIpcc/deleteListLogCallIpcc")
  public String deleteListLogCallIpcc(@RequestBody List<LogCallIpccDTO> logCallIpccListDTO);

  @GetMapping("/LogCallIpcc/findLogCallIpccById/id{id}")
  public LogCallIpccDTO findLogCallIpccById(@PathVariable("id") Long id);

  @PostMapping("/LogCallIpcc/insertOrUpdateListLogCallIpcc")
  public String insertOrUpdateListLogCallIpcc(
      @RequestBody List<LogCallIpccDTO> logCallIpccListDTO);

  @GetMapping("/LogCallIpcc/getSequenseLogCallIpcc/seqName{seqName}/size{size}")
  public List<String> getSequenseLogCallIpcc(@PathVariable("seqName") String seqName,
      @PathVariable("size") int size);

  @PostMapping("/LogCallIpcc/getListLogCallIpccByCondition")
  public List<LogCallIpccDTO> getListLogCallIpccByCondition(@RequestBody BaseDto baseDto);

  @GetMapping("/Wo/findWoByWoCodeNoOffset/woSystemId{woSystemId}")
  public WoInsideDTO findWoByWoCodeNoOffset(@PathVariable("woSystemId") String woSystemId);


}
