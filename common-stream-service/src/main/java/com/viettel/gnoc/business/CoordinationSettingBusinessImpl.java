package com.viettel.gnoc.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.cr.dto.CoordinationSettingDTO;
import com.viettel.gnoc.repository.CoordinationSettinggRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class CoordinationSettingBusinessImpl implements CoordinationSettingBusiness {

  @Autowired
  CoordinationSettinggRepository coordinationSettinggRepository;

  @Override
  public Datatable getListCoordinationSettingPage(CoordinationSettingDTO coordinationSettingDTO) {
    log.debug("Request to getListCoordinationSettingPage : {}", coordinationSettingDTO);
    return coordinationSettinggRepository.getListCoordinationSettingPage(coordinationSettingDTO);
  }

  @Override
  public ResultInSideDto insert(CoordinationSettingDTO coordinationSettingDTO) {
    log.debug("Request to insert : {}", coordinationSettingDTO);
    return coordinationSettinggRepository.insertOrUpdate(coordinationSettingDTO);
  }

  @Override
  public ResultInSideDto update(CoordinationSettingDTO coordinationSettingDTO) {
    log.debug("Request to update : {}", coordinationSettingDTO);
    return coordinationSettinggRepository.insertOrUpdate(coordinationSettingDTO);
  }

  @Override
  public ResultInSideDto deleteCoordinationSetting(Long id) {
    log.debug("Request to deleteCoordinationSetting : {}", id);
    return coordinationSettinggRepository.deleteCoordinationSetting(id);
  }

  @Override
  public CoordinationSettingDTO getDetail(Long id) {
    log.debug("Request to getDetail : {}", id);
    return coordinationSettinggRepository.getDetail(id);
  }
}
