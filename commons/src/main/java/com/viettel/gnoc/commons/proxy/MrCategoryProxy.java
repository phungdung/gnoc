package com.viettel.gnoc.commons.proxy;

import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.cr.dto.WorkLogCategoryDTO;
import com.viettel.gnoc.cr.dto.WorkLogCategoryInsideDTO;
import com.viettel.gnoc.maintenance.dto.MrApproveRolesDTO;
import com.viettel.gnoc.maintenance.dto.MrApproveSearchDTO;
import com.viettel.gnoc.maintenance.dto.MrClientDetail;
import com.viettel.gnoc.maintenance.dto.MrDTO;
import com.viettel.gnoc.maintenance.dto.MrForNocSearchDTO;
import com.viettel.gnoc.maintenance.dto.MrMobileDTO;
import com.viettel.gnoc.maintenance.dto.MrNodesDTO;
import com.viettel.gnoc.maintenance.dto.MrSchedulePeriodicDTO;
import com.viettel.gnoc.maintenance.dto.MrSearchDTO;
import com.viettel.gnoc.wo.dto.UserGroupCategoryDTO;
import com.viettel.gnoc.wo.dto.WorkLogInsiteDTO;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "mr-category-service")
public interface MrCategoryProxy {

  @PostMapping("/MaintenanceMngt/getListWorkLogCategoryDTO")
  public List<WorkLogCategoryInsideDTO> getListWorkLogCategoryDTO(
      @RequestBody WorkLogCategoryInsideDTO workLogCategoryDTO);

  @GetMapping("/MaintenanceMngt/findWorkLogCategoryById")
  public WorkLogCategoryDTO findWorkLogCategoryById(Long id);

  @PostMapping("/MaintenanceMngt/getListWorklogSearch")
  public List<WorkLogInsiteDTO> getListWorklogSearch(
      @RequestBody WorkLogInsiteDTO workLogInsiteDTO);
//
//  @PostMapping("/MaintenanceMngt/insertWorkLogProxy")
//  public ResultInSideDto insertWorkLogProxy(
//      @RequestBody WorkLogInsiteDTO workLogInsiteDTO);

  @PostMapping("/MaintenanceMngt/getListWorkLogDTO")
  public List<WorkLogInsiteDTO> getListWorkLogDTO(
      @RequestBody WorkLogInsiteDTO workLogInsiteDTO);

  @PostMapping("/MaintenanceMngt/getListUserGroupBySystem")
  public List<UserGroupCategoryDTO> getListUserGroupBySystem(
      @RequestBody UserGroupCategoryDTO userGroupCategoryDTO);

  @GetMapping("/MaintenanceMngt/updateMrStatus/crId{crId}/woId{woId}")
  public ResultDTO updateMrStatus(@PathVariable("crId") String crId,
      @PathVariable("woId") String woId);

  @GetMapping("/MaintenanceMngt/reCreatedOrCloseCr/crId{crId}/status{status}")
  public ResultDTO reCreatedOrCloseCr(@PathVariable("crId") String crId,
      @PathVariable("status") String status);

  @PostMapping("/MaintenanceMngt/getMrChartInfoForNOC")
  public MrClientDetail getMrChartInfoForNOC(@RequestBody MrForNocSearchDTO mrSearchDTO);

  @PostMapping("/MaintenanceMngt/getListMrForMobile")
  public List<MrDTO> getListMrForMobile(@RequestBody MrMobileDTO dto);

  @GetMapping("/MrApprovalDepartment/getLstMrApproveDeptByUser/userId{userId}")
  public List<MrApproveSearchDTO> getLstMrApproveDeptByUser(@PathVariable("userId") String userId);

  @PostMapping("/MrApprovalDepartment/getLstMrApproveUserByRole")
  public List<MrDTO> getLstMrApproveUserByRole(@RequestBody MrApproveRolesDTO mrRole);

  @PostMapping("/MrSchedulePeriodic/insertMrSchedulePeriodic")
  public List<MrDTO> insertMrSchedulePeriodic(
      @RequestBody MrSchedulePeriodicDTO mrSchedulePeriodicDTO);

  @PostMapping("/MaintenanceMngt/getWorklogFromWo")
  public List<MrDTO> getWorklogFromWo(@RequestBody MrSearchDTO mrSearchDTO);

  @GetMapping("/MaintenanceMngt/getWoCrNodeList/woId{woId}/crId{crId}")
  List<MrNodesDTO> getWoCrNodeList(@PathVariable("woId") String woId,
      @PathVariable("crId") String crId);
}
