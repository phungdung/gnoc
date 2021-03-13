package com.viettel.gnoc.mr.business;


import com.viettel.gnoc.mr.repository.MrCdBatteryCreateWoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class MrCdBatteryCreateWoHisBusinessImpl implements MrCdBatteryCreateWoHisBusiness {

  @Autowired
  MrCdBatteryCreateWoRepository mrCdBatteryCreateWoRepository;

  @Override
  public boolean isValidCreateWo(String stationCode, String dcPower) {
    return mrCdBatteryCreateWoRepository.isValidCreateWo(stationCode, dcPower);
  }
}
