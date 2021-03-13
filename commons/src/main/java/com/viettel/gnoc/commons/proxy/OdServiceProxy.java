package com.viettel.gnoc.commons.proxy;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.od.dto.OdDTO;
import com.viettel.gnoc.od.dto.OdSearchInsideDTO;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "od-service")
public interface OdServiceProxy {

  @PostMapping("/od/getListDataSearchForOther")
  public List<OdSearchInsideDTO> getListDataSearchForOther(
      @RequestBody OdSearchInsideDTO odDTOSearch);

  @PostMapping("/od/getListDataSearch")
  public Datatable getListDataSearch(
      @RequestBody OdSearchInsideDTO odDTOSearch);

  @PostMapping("/od/updateOdOtherSystem")
  public ResultInSideDto updateOdOtherSystem(
      @RequestBody OdDTO odDTO);
}
