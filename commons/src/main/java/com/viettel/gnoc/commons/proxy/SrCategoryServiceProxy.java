package com.viettel.gnoc.commons.proxy;

import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.sr.dto.SRCreateAutoCRDTO;
import com.viettel.gnoc.sr.dto.SRMappingProcessCRDTO;
import com.viettel.gnoc.sr.dto.SRRoleActionDTO;
import com.viettel.gnoc.sr.dto.SRRoleDTO;
import com.viettel.gnoc.sr.dto.SRRoleUserInSideDTO;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "sr-category-service")
public interface SrCategoryServiceProxy {

  @PostMapping("/srRoleUser/getlistSRRoleUserDTO")
  public List<SRRoleUserInSideDTO> getlistSRRoleUserDTO(
      @RequestBody SRRoleUserInSideDTO srRoleUserDTO);

  @PostMapping("/srRoleActions/getListSRRoleActionDTO")
  public List<SRRoleActionDTO> getListSRRoleActionDTO(
      @RequestBody SRRoleActionDTO srRoleActionDTO);

  @PostMapping("/srMappingProcessCR/getListSRMappingProcessCRDTO")
  public List<SRMappingProcessCRDTO> getListSRMappingProcessCRDTO(
      @RequestBody SRMappingProcessCRDTO srMappingProcessCRDTO);

  @PostMapping("/srRole/getListSRRoleDTO")
  public List<SRRoleDTO> getListSRRoleDTO(
      @RequestBody SRRoleDTO srRoleDTO);

  @GetMapping("/srRoleUser/getListUser/unitId{unitId}/country{country}/username{username}")
  public List<SRRoleUserInSideDTO> getListUser(@PathVariable("unitId") String unitId,
      @PathVariable("country") String country,
      @PathVariable("username") String username);

  @PostMapping("/srMappingProcessCR/getStartTimeEndTimeFromCrImpact")
  public SRMappingProcessCRDTO getStartTimeEndTimeFromCrImpact(@RequestBody SRCreateAutoCRDTO dto);

  @PostMapping("/srMappingProcessCR/getDutyTypeByProcessIdProxy")
  public List<ItemDataCRInside> getDutyTypeByProcessIdProxy(@RequestBody
      SRMappingProcessCRDTO srMappingProcessCRDTO);
}
