package com.viettel.gnoc.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.RolesDTO;
import com.viettel.gnoc.commons.dto.UserImpactSegmentDTO;
import com.viettel.gnoc.cr.dto.ImpactSegmentDTO;
import com.viettel.gnoc.repository.UserImpactSegmentRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class UserImpactSegmentBusinessImpl implements UserImpactSegmentBusiness {

  @Autowired
  private UserImpactSegmentRepository userImpactSegmentRepository;

  @Override
  public Datatable getListUserImpactSegmentOfCr(UserImpactSegmentDTO dto) {
    log.debug("Request to getListUserImpactSegmentOfCr : {}", dto);
    return userImpactSegmentRepository.getListUserImpactSegmentOfCr(dto);
  }

  @Override
  public List<ImpactSegmentDTO> getListImpactSegmentCBB() {
    log.debug("Request to getListImpactSegmentCBB : {}");
    return userImpactSegmentRepository.getListImpactSegmentCBB();
  }

  @Override
  public List<RolesDTO> getListRolesCBB() {
    log.debug("Request to getListRolesCBB : {}");
    return userImpactSegmentRepository.getListRolesCBB();
  }
}
