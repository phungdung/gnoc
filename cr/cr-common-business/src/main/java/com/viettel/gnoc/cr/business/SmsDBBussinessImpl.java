package com.viettel.gnoc.cr.business;

import com.viettel.gnoc.cr.repository.SmsDBRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class SmsDBBussinessImpl implements SmsDBBussiness {

  @Autowired
  SmsDBRepository smsDBRepository;

  @Override
  public String sendSMSToLstUserConfig(String crId, String contentType) {
    return smsDBRepository.sendSMSToLstUserConfig(crId, contentType);
  }
}
