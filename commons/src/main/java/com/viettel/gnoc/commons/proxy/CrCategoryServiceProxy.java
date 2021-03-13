package com.viettel.gnoc.commons.proxy;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.cr.dto.CfgChildArrayDTO;
import com.viettel.gnoc.cr.dto.CrProcessInsideDTO;
import com.viettel.gnoc.cr.dto.CrProcessWoDTO;
import com.viettel.gnoc.cr.dto.ImpactSegmentDTO;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "cr-category-service")
public interface CrCategoryServiceProxy {

  @PostMapping("/CfgChildArray/getListCfgChildArray")
  public Datatable getListCfgChildArray(CfgChildArrayDTO cfgChildArrayDTO);

  @GetMapping("/CrImpactSegment/findById/id{id}")
  public ImpactSegmentDTO findById(@PathVariable(value = "id") Long impactSegmentId);

  @PostMapping("/CrManagerProcess/getListCrProcessDTO")
  public List<CrProcessInsideDTO> getListCrProcessDTO(@RequestBody CrProcessInsideDTO crProcessDTO);

  @GetMapping("/CrManagerProcess/getAllCrProcess{parentId}")
  public List<CrProcessInsideDTO> getAllCrProcess(@PathVariable("parentId") Long parentId);

  @GetMapping("/CrManagerProcess/getLstWoFromProcessId/crProcessId{crProcessId}")
  public List<CrProcessWoDTO> getLstWoFromProcessId(
      @PathVariable(value = "crProcessId") Long crProcessId);

  @PostMapping("/CfgChildArray/getCbbChildArray")
  public List<CfgChildArrayDTO> getCbbChildArray(@RequestBody CfgChildArrayDTO cfgChildArrayDTO);
}
