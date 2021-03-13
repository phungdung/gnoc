package com.viettel.gnoc.cr.business;

import com.viettel.gnoc.cr.dto.CrProcessDTO;
import com.viettel.gnoc.cr.dto.CrProcessInsideDTO;
import com.viettel.gnoc.cr.repository.CrManagerProcessRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
public class CrManagerProcessBusinessImpl implements CrManagerProcessBusiness {

  @Autowired
  CrManagerProcessRepository crManagerProcessRepository;

  @Override
  public List<CrProcessDTO> getAllCrProcess(Long parentId) {
    return crManagerProcessRepository.getAllCrProcess(parentId);
  }
}
