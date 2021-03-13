package com.viettel.gnoc.cr.service;

import com.viettel.gnoc.cr.business.CrManagerProcessBusiness;
import com.viettel.gnoc.cr.business.CrProcessBusiness;
import com.viettel.gnoc.cr.dto.CrProcessDTO;
import com.viettel.gnoc.cr.dto.CrProcessInsideDTO;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CrProcessServiceImpl implements CrProcessService {

  @Autowired
  CrProcessBusiness crProcessBusiness;

  @Autowired
  CrManagerProcessBusiness crManagerProcessBusiness;

  @Override
  public List<CrProcessDTO> synchCrProcess(List<Long> lstImpactSegment) {
    return crProcessBusiness.synchCrProcess(lstImpactSegment);
  }


  @Override
  public List<CrProcessDTO> getAllCrProcess(Long parentId) {
    return crManagerProcessBusiness.getAllCrProcess(parentId);
  }
}
