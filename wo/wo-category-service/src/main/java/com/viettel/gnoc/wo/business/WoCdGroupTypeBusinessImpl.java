package com.viettel.gnoc.wo.business;

import com.viettel.gnoc.wo.dto.WoCdGroupTypeDTO;
import com.viettel.gnoc.wo.repository.WoCdGroupTypeRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class WoCdGroupTypeBusinessImpl implements WoCdGroupTypeBusiness {

  @Autowired
  WoCdGroupTypeRepository woCdGroupTypeRepository;

  @Override
  public List<WoCdGroupTypeDTO> getListWoCdGroupTypeDTO(WoCdGroupTypeDTO woCdGroupTypeDTO) {
    return woCdGroupTypeRepository
        .getListWoCdGroupTypeDTO(woCdGroupTypeDTO);
  }
}
