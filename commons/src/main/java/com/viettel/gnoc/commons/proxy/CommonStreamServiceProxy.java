package com.viettel.gnoc.commons.proxy;

import com.viettel.gnoc.commons.dto.*;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.incident.dto.CfgMapNetLevelIncTypeDTO;
import com.viettel.gnoc.wo.dto.WoTestServiceMapDTO;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "common-stream-service")
public interface CommonStreamServiceProxy {

  @PostMapping("/LogChangeConfig/insertLog")
  public ResultInSideDto insertLog(
      @RequestBody LogChangeConfigDTO logChangeConfigDTO);

  @PostMapping("/InfraDeviceService/geInfraDeviceByIps")
  public List<InfraDeviceDTO> geInfraDeviceByIps(
      @RequestBody InfraDeviceDTO infraDeviceDTO);

  @PostMapping("/InfraDeviceService/getListInfraDeviceIpV2")
  public List<InfraDeviceDTO> getListInfraDeviceIpV2(
      @RequestBody InfraDeviceDTO infraDeviceDTO);

  @PostMapping("/InfraDeviceService/getListInfraDeviceIpV2SrProxy")
  public List<InfraDeviceDTO> getListInfraDeviceIpV2SrProxy(
      @RequestBody InfraDeviceDTO infraDeviceDTO);

  @GetMapping("/CatLocationService/getListLocationByLevelCBBProxy/level{level}/parentId{parentId}")
  public List<ItemDataCRInside> getListLocationByLevelCBBProxy(
      @PathVariable(value = "level") Long level, @PathVariable(value = "parentId") Long parentId);

  @PostMapping("/CatItemService/getListCatItemTransDTO")
  public List<CatItemDTO> getListCatItemTransDTO(@RequestBody CatItemDTO catItemDTO);

  @PostMapping("/unitCommon/getListUnit")
  public List<UnitDTO> getListUnit(@RequestBody UnitDTO unitDTO);

  @PostMapping("/UsersService/getListUserDTOByProxy")
  public List<UsersInsideDto> getListUserDTOByProxy(@RequestBody UsersInsideDto usersInsideDto);

  @PostMapping("/CatLocationService/getCatLocationByLevel")
  public List<CatLocationDTO> getCatLocationByLevel(String level);

  @PostMapping("/CatItemService/getListCatItemDTO")
  public List<CatItemDTO> getListCatItemDTO(@RequestBody CatItemDTO catItemDTO);

  @PostMapping("/InfraDeviceService/getListInfraDeviceNonIp")
  public List<InfraDeviceDTO> getListInfraDeviceNonIp(InfraDeviceDTO infraDeviceDTO);

  @PostMapping("/InfraDeviceService/getListInfraDeviceDTOV2")
  public List<InfraDeviceDTO> getListInfraDeviceDTOV2(InfraDeviceDTO infraDeviceDTO);

  @PostMapping("/categoryService/getListCategory")
  public List<CategoryDTO> getListCategory(CategoryDTO categoryDTO);

  @PostMapping("/cfgMapNetworkLevelInc/getListCfgMapNetLevelIncTypeDTO")
  List<CfgMapNetLevelIncTypeDTO> getListCfgMapNetLevelIncTypeDTO(
      @RequestBody CfgMapNetLevelIncTypeDTO cfgMapNetLevelIncTypeDTO);

  @PostMapping("/WoTestServiceMap/getListWoTestServiceMapDTO/rowStart{rowStart}/maxRow{maxRow}/sortType{sortType}/sortFieldList{sortFieldList}")
  public List<WoTestServiceMapDTO> getListWoTestServiceMapDTO(@RequestBody WoTestServiceMapDTO woTestServiceMapDTO,
      @PathVariable(value="rowStart") int rowStart, @PathVariable(value="maxRow") int maxRow,
      @PathVariable(value="sortType") String sortType, @PathVariable(value="sortFieldList") String sortFieldList);

  @PostMapping("/WoTestServiceMap/insertWoTestServiceMap")
  public ResultDTO insertWoTestServiceMap(@RequestBody WoTestServiceMapDTO woTestServiceMapDTO);

  @PostMapping("/WoTestServiceMap/insertOrUpdateListWoTestServiceMap")
  public String insertOrUpdateListWoTestServiceMap(@RequestBody List<WoTestServiceMapDTO> lsWoTestServiceMapDTOS);

  //thanhlv12 add 29-09-2020
  @PostMapping("/commonStreamAPI/insertHisUserImpact")
  public  ResultInSideDto insertHisUserImpact(@RequestBody DataHistoryChange dataHistoryChange);

  // end

  @PostMapping("/categoryService/getListCategoryDTO2")
  public List<CategoryDTO> getListCategoryDTO2(@RequestBody CategoryDTO categoryDTO);
}
