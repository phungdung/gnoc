package com.viettel.gnoc.wo.controller;

import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.wo.business.WoCdBusiness;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(Constants.WO_API_PATH_PREFIX + "WoCd")
public class WoCdController {

  @Autowired
  WoCdBusiness woCdBusiness;

  @GetMapping("/getListCdByGroup/woGroupId{woGroupId}")
  public List<UsersInsideDto> getListCdByGroup(@PathVariable Long woGroupId) {
    List<UsersInsideDto> list = woCdBusiness.getListCdByGroup(woGroupId);
    return list;
  }

}
