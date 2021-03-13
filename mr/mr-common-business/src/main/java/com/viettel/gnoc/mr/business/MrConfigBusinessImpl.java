package com.viettel.gnoc.mr.business;


import com.viettel.gnoc.maintenance.dto.MrConfigDTO;
import com.viettel.gnoc.mr.repository.MrConfigRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@Slf4j
public class MrConfigBusinessImpl implements MrConfigBusiness {

  @Autowired
  MrConfigRepository mrConfigRepository;

  @Override
  public List<MrConfigDTO> getConfigByGroup(String configGroup) {
    return mrConfigRepository.getConfigByGroup(configGroup);
  }
}
