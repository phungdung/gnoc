package com.viettel.gnoc.wo.business;

import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.wo.repository.WoCdRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class WoCdBusinessImpl implements WoCdBusiness {

  @Autowired
  WoCdRepository woCdRepository;

  @Override
  public List<UsersInsideDto> getListCdByGroup(Long woGroupId) {
    log.debug("Request to getListCdByGroup: {}", woGroupId);
    return woCdRepository.getListCdByGroup(woGroupId);
  }
}
