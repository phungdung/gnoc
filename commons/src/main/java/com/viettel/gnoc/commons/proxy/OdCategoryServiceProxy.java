package com.viettel.gnoc.commons.proxy;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.od.dto.OdCfgBusinessDTO;
import com.viettel.gnoc.od.dto.OdChangeStatusDTO;
import com.viettel.gnoc.od.dto.OdDTOSearch;
import com.viettel.gnoc.od.dto.OdTypeDTO;
import com.viettel.gnoc.od.dto.OdTypeDetailDTO;
import com.viettel.gnoc.od.dto.OdTypeMapLocationDTO;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "od-category-service")
public interface OdCategoryServiceProxy {

  @GetMapping("/odCfgBusiness/getListOdCfgBusinessDTO/oldStatus/{oldStatus}/newStatus/{newStatus}/odPriority/{odPriority}/odTypeId/{odTypeId}")
  public List<OdCfgBusinessDTO> getListOdCfgBusinessDTO(
      @PathVariable(value = "oldStatus") String oldStatus,
      @PathVariable(value = "newStatus") String newStatus,
      @PathVariable(value = "odPriority") String odPriority,
      @PathVariable(value = "odTypeId") String odTypeId);

  @PostMapping("/odCfgBusiness/getListOdCfgBusinessToUpdateOd")
  public List<OdCfgBusinessDTO> getListOdCfgBusiness(
      @RequestBody OdChangeStatusDTO odChangeStatusDTO);

  @GetMapping("/odCfgBusiness/deleteByOdChangeStatusId/id/{id}")
  public ResultInSideDto deleteByOdChangeStatusId(@PathVariable(value = "id") Long id);

  @PostMapping("/odCfgBusiness/add")
  public ResultInSideDto add(@RequestBody OdChangeStatusDTO odChangeStatusDTO);

  @PostMapping("/odCfgBusiness/searchOdChangeStatus")
  public List<OdChangeStatusDTO> search(@RequestBody OdChangeStatusDTO odChangeStatusDTO);

  @GetMapping("/odCfgBusiness/getOdChangeStatusDTOByParams/params{params}")
  public OdChangeStatusDTO getOdChangeStatusDTOByParams(
      @PathVariable(value = "params") String... params);

  @PostMapping("/odType/getListOdTypeDetail")
  public List<OdTypeDetailDTO> getListOdTypeDetail(@RequestBody OdTypeDetailDTO odTypeDetailDTO);

  @PostMapping("/odType/getListOdType")
  public Datatable getListOdType(@RequestBody OdTypeDTO odTypeDTO);

  @PostMapping("/odType/getListOdTypeMapByOdTypeIdAndLocation")
  public OdTypeMapLocationDTO getListOdTypeMapByOdTypeIdAndLocation(
      @RequestBody OdDTOSearch odDTOSearch);

  @GetMapping("/odType/getInforByODType/odTypeCode{odTypeCode}")
  public OdTypeDTO getInforByODType(@PathVariable(value = "odTypeCode") String odTypeCode);
}
