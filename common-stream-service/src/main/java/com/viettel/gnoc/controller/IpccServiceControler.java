package com.viettel.gnoc.controller;

import com.viettel.gnoc.commons.business.IpccServiceBusiness;
import com.viettel.gnoc.commons.config.CustomSecurityAnnotation;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.IpccServiceDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.CONFIG_PROPERTY;
import com.viettel.gnoc.commons.utils.Constants.SUB_ADMIN_EDIT_VIEW;
import java.util.List;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(Constants.COMMON_STREAM_API_PATH_PREFIX + "IpccService")
public class IpccServiceControler {

  @Autowired
  private IpccServiceBusiness ipccServiceBusiness;

  @PostMapping("/getListIpccServiceDTOPage")
  public ResponseEntity<Datatable> getListIpccServiceDTOPage(
      @RequestBody IpccServiceDTO ipccServiceDTO) {
    return new ResponseEntity<>(ipccServiceBusiness.getListIpccServiceDTOPage(ipccServiceDTO),
        HttpStatus.OK);
  }

  @GetMapping("/getListIpccServiceAll")
  public ResponseEntity<List<IpccServiceDTO>> getListIpccServiceAll() {
    return new ResponseEntity<>(ipccServiceBusiness.getListIpccServiceAll(),
        HttpStatus.OK);
  }


  @GetMapping("/getDeatilIpccServiceById")
  public ResponseEntity<IpccServiceDTO> getDeatilIpccServiceById(Long id) {
    return new ResponseEntity<>(ipccServiceBusiness.getDeatilIpccServiceById(id), HttpStatus.OK);
  }

  @CustomSecurityAnnotation(target = CONFIG_PROPERTY.SUB_ADMIN_EDIT, permission = SUB_ADMIN_EDIT_VIEW.IPCC_SERVICE)
  @PostMapping("/addOrUpdateIpccService")
  public ResponseEntity<ResultInSideDto> addOrUpdateIpccService(
      @Valid @RequestBody IpccServiceDTO ipccServiceDTO) {
    return new ResponseEntity<>(ipccServiceBusiness.addOrUpdateIpccService(ipccServiceDTO),
        HttpStatus.OK);
  }

  @CustomSecurityAnnotation(target = CONFIG_PROPERTY.SUB_ADMIN_EDIT, permission = SUB_ADMIN_EDIT_VIEW.IPCC_SERVICE)
  @DeleteMapping("/deleteIpccService")
  public ResponseEntity<ResultInSideDto> deleteIpccService(Long id) {
    return new ResponseEntity<>(ipccServiceBusiness.deleteIpccService(id),
        HttpStatus.OK);
  }
}
