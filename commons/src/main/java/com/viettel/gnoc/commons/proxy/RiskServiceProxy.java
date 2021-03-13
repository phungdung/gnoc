package com.viettel.gnoc.commons.proxy;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.risk.dto.RiskDTO;
import com.viettel.gnoc.risk.dto.RiskDTOSearch;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "risk-service")
public interface RiskServiceProxy {

  @PostMapping("/Risk/getListDataSearchForOther/rowStart{rowStart}/maxRow{maxRow}/sortType{sortType}/sortFieldList{sortFieldList}")
  public List<RiskDTOSearch> getListDataSearchForOther(@RequestBody RiskDTOSearch riskDTOSearch,
      @PathVariable(value = "rowStart") int rowStart, @PathVariable(value = "maxRow") int maxRow,
      @PathVariable(value = "sortType") String sortType,
      @PathVariable(value = "sortFieldList") String sortFieldList);

  //Dunglv add call proxy update tt
  @PostMapping("/Risk/getListDataSearchByOther")
  public List<RiskDTO> getListRiskByTt(@RequestBody RiskDTO riskDTO);

  //thangdt  get list risk Related
  @PostMapping("/Risk/getListDataSearchWeb")
  public Datatable getListDataSearchWeb(@RequestBody RiskDTO riskDTO);

  //thangdt  update risk Related
  @PostMapping("/Risk/updateRiskOtherSystem")
  public ResultInSideDto updateRiskOtherSystem(
      @RequestBody RiskDTO riskDTO);
}
