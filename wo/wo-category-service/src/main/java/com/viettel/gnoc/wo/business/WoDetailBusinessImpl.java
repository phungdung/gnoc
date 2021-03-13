package com.viettel.gnoc.wo.business;

import com.viettel.gnoc.wo.dto.WoDetailDTO;
import com.viettel.gnoc.wo.repository.WoDetailRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class WoDetailBusinessImpl implements WoDetailBusiness {

  @Autowired
  protected WoDetailRepository woDetailRepository;


  @Override
  public List<WoDetailDTO> getListWoDetailDTO(WoDetailDTO woDetailDTO) {
    log.debug("Request to getListWoDetailDTO : {}", woDetailDTO);
    return woDetailRepository.getListWoDetailDTO(woDetailDTO);
  }


}
