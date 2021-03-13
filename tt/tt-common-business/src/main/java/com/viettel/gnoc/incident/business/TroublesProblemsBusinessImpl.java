package com.viettel.gnoc.incident.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.incident.repository.ProblemsRepository;
import com.viettel.gnoc.pt.dto.ProblemsInsideDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
public class TroublesProblemsBusinessImpl implements TroublesProblemsBusiness {

  @Autowired
  ProblemsRepository problemsRepository;

  @Override
  public Datatable getListProblems(ProblemsInsideDTO problemsInsideDTO) {
    log.debug("Request to getListProblems : {}", problemsInsideDTO);
    return problemsRepository.getListProblems(problemsInsideDTO);
  }
}
