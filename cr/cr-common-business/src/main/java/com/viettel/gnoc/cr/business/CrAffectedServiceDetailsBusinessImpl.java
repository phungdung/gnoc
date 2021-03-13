package com.viettel.gnoc.cr.business;

import com.viettel.gnoc.cr.dto.CrAffectedServiceDetailsDTO;
import com.viettel.gnoc.cr.repository.CrAffectedServiceDetailsRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class CrAffectedServiceDetailsBusinessImpl implements CrAffectedServiceDetailsBusiness {

  @Autowired
  CrAffectedServiceDetailsRepository crAffectedServiceDetailsRepository;

  @Override
  public List<CrAffectedServiceDetailsDTO> search(CrAffectedServiceDetailsDTO serviceDetailsDTO,
      int rowStart, int maxRow, String sortType, String sortFieldList) {
    return crAffectedServiceDetailsRepository
        .search(serviceDetailsDTO, rowStart, maxRow, sortType, sortFieldList);
  }
}
