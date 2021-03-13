package com.viettel.gnoc.commons.proxy;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.od.dto.ObjKeyValueVsmartDTO;
import com.viettel.gnoc.wo.dto.CfgSupportCaseDTO;
import com.viettel.gnoc.wo.dto.CfgSupportCaseTestDTO;
import com.viettel.gnoc.wo.dto.WoCdGroupInsideDTO;
import com.viettel.gnoc.wo.dto.WoCdGroupTypeDTO;
import com.viettel.gnoc.wo.dto.WoCdGroupTypeUserDTO;
import com.viettel.gnoc.wo.dto.WoMaterialDTO;
import com.viettel.gnoc.wo.dto.WoPriorityDTO;
import com.viettel.gnoc.wo.dto.WoTypeCfgRequiredDTO;
import com.viettel.gnoc.wo.dto.WoTypeInsideDTO;
import com.viettel.gnoc.wo.dto.WoTypeTimeDTO;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "wo-category-service")
public interface WoCategoryServiceProxy {

  @GetMapping("/WoCd/getListCdByGroup/woGroupId{woGroupId}")
  public List<UsersInsideDto> getListCdByGroup(@PathVariable(value = "woGroupId") Long woGroupId);

  @GetMapping("/woType/findWoTypeById/woTypeId{woTypeId}")
  public WoTypeInsideDTO findWoTypeById(@PathVariable(value = "woTypeId") Long woTypeId);

  @GetMapping("/woMaterial/findWoMaterialById/materialId{materialId}")
  public WoMaterialDTO findWoMaterialById(@PathVariable(value = "materialId") Long materialId);

  @PostMapping("/woType/getListWoTypeTimeDTO")
  public List<WoTypeTimeDTO> getListWoTypeTimeDTO(@RequestBody WoTypeTimeDTO woTypeTimeDTO);

  @PostMapping("/WoCdGroup/getListCdGroupByUser")
  public List<WoCdGroupInsideDTO> getListCdGroupByUser(
      @RequestBody WoCdGroupTypeUserDTO woCdGroupTypeUserDTO);

  @PostMapping("/WoCdGroupType/getListWoCdGroupTypeDTO")
  public List<WoCdGroupTypeDTO> getListWoCdGroupTypeDTO(
      @RequestBody WoCdGroupTypeDTO woCdGroupTypeDTO);

  @PostMapping("/woPriority/getListWoPriorityDTO")
  public List<WoPriorityDTO> getListWoPriorityDTO(
      @RequestBody WoPriorityDTO woPriorityDTO);

  @PostMapping("/getListCdGroup")
  public List<WoCdGroupInsideDTO> getListCdGroup(String userName);

  @PostMapping("/cfgSupportCase/getListCfgSupportCaseDTONew")
  public Datatable getListCfgSupportCaseDTONew(@RequestBody CfgSupportCaseDTO cfgSupportCaseDTO);

  @GetMapping("/cfgSupportCase/getListCfgSupportCaseTestId/cfgSuppportCaseId{cfgSuppportCaseId}")
  public List<CfgSupportCaseTestDTO> getListCfgSupportCaseTestId(
      @PathVariable(value = "cfgSuppportCaseId") Long cfgSuppportCaseId);

  @PostMapping("/woType/getListWoTypeByLocaleNotLike")
  public List<WoTypeInsideDTO> getListWoTypeByLocaleNotLike(
      @RequestBody WoTypeInsideDTO woTypeInsideDTO);

  @PostMapping("/WoCdGroup/getListWoCdGroupDTO")
  public Datatable getListWoCdGroupDTO(@RequestBody WoCdGroupInsideDTO woCdGroupInsideDTO);

  @GetMapping("/CfgWoHelpVsmart/getDataHeader/systemId{systemId}/typeId{typeId}")
  public List<ObjKeyValueVsmartDTO> getDataHeader(@PathVariable(value = "systemId") Long systemId,
      @PathVariable(value = "typeId") String typeId);

  @GetMapping("/WoCdGroup/findWoCdGroupById/woGroupId{woGroupId}")
  public WoCdGroupInsideDTO findWoCdGroupById(@PathVariable(value = "woGroupId") Long woGroupId);

  @PostMapping("/woType/getListWoTypeDTO")
  public List<WoTypeInsideDTO> getListWoTypeDTO(@RequestBody WoTypeInsideDTO woTypeInsideDTO);

  @PostMapping("/woType/getListWoTypeCfgRequiredByWoTypeId")
  public List<WoTypeCfgRequiredDTO> getListWoTypeCfgRequiredByWoTypeId(
      @RequestBody WoTypeCfgRequiredDTO woTypeCfgRequiredDTO);

  @PostMapping("/WoCdGroup/getListCdGroupByUserProxy")
  public List<WoCdGroupInsideDTO> getListCdGroupByUserProxy(
      @RequestBody WoCdGroupTypeUserDTO woCdGroupTypeUserDTO);

  @GetMapping("/woType/getWoTypeByCode/woTypeCode{woTypeCode}")
  public WoTypeInsideDTO getWoTypeByCode(
      @PathVariable("woTypeCode") String woTypeCode);

}
