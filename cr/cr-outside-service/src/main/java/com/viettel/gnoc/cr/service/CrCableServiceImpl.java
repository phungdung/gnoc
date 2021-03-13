package com.viettel.gnoc.cr.service;

import com.viettel.gnoc.cr.business.CrCableBusiness;
import com.viettel.gnoc.cr.dto.CrCableDTO;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CrCableServiceImpl implements CrCableService {

  @Autowired
  CrCableBusiness crCableBusiness;

  @Override
  public List<CrCableDTO> getListCrCableDTO(CrCableDTO crCableDTO, int rowStart, int maxRow,
      String sortType, String sortFieldList) {
    if (crCableDTO != null) {
      return crCableBusiness
          .getListCrCableDTO(crCableDTO, rowStart, maxRow, sortType, sortFieldList);
    }
    return null;
  }

}
