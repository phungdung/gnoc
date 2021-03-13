package com.viettel.gnoc.commons.proxy;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.pt.dto.ProblemActionLogsDTO;
import com.viettel.gnoc.pt.dto.ProblemMonitorDTO;
import com.viettel.gnoc.pt.dto.ProblemsInsideDTO;
import com.viettel.gnoc.pt.dto.ProblemsMobileDTO;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "pt-service")
public interface PtServiceProxy {

  @PostMapping("/ProblemWo/getListDataSearchWeb")
  public ResponseEntity<Datatable> getListDataSearchWeb(
      @RequestBody ProblemsInsideDTO problemsInsideDTO);

  @PostMapping("/ProblemActionLogsService/insertProblemActionLogs")
  public ResponseEntity<ResultInSideDto> insertProblemActionLogs(
      @RequestBody ProblemActionLogsDTO dto);

  @PostMapping("/Problems/updateProblems")
//  public ResponseEntity<ResultInSideDto> updateProblems(@RequestBody ProblemsInsideDTO problemsInsideDTO);
  public ResponseEntity<ResultInSideDto> updateProblems(
      @RequestBody ProblemsInsideDTO problemsInsideDTO) throws Exception;

  @PostMapping("/Problems/searchParentPTForCR")
  public Datatable searchParentPTForCR(@RequestBody ProblemsInsideDTO dto);

  @GetMapping("/Problems/getProblemsMobileUnitProxy/receiveUnitId{receiveUnitId}")
  public List<ProblemsMobileDTO> getProblemsMobileUnitProxy(
      @PathVariable("receiveUnitId") String receiveUnitId);

  @GetMapping("/Problems/getProblemsMobileUnitAllProxy/receiveUnitId{receiveUnitId}")
  public List<ProblemsMobileDTO> getProblemsMobileUnitAllProxy(
      @PathVariable("receiveUnitId") String receiveUnitId);

  @PostMapping("/Problems/getProblemsMonitor")
  public List<ProblemMonitorDTO> getProblemsMonitor(@RequestBody ProblemsInsideDTO dto);
}
