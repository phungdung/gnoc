package com.viettel.gnoc.wo.business;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.incident.dto.WoSupportDTO;
import com.viettel.gnoc.wo.repository.WoSupportRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class WoSupportBusinessImpl implements WoSupportBusiness {

  @Autowired
  WoSupportRepository woSupportRepository;

  @Override
  public ResultInSideDto insertListWoSupport(List<WoSupportDTO> lstWoSupportDTO) {
    return woSupportRepository.insertListWoSupport(lstWoSupportDTO);
  }
}
