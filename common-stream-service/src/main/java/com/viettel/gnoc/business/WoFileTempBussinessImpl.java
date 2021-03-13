package com.viettel.gnoc.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.WoFileTempDto;
import com.viettel.gnoc.repository.WoFileTempRepository;
import com.viettel.gnoc.wo.dto.WoTypeInsideDTO;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class WoFileTempBussinessImpl implements WoFileTempBussiness {

  @Autowired
  private WoFileTempRepository woFileTempRepository;

  @Override
  public Datatable getListWoFileTemp(WoFileTempDto dto) {
    log.debug("Request to getListWoFileTemp : {}", dto);
    return woFileTempRepository.getListWoFileTemp(dto);
  }

  @Override
  public WoFileTempDto getDetail(Long woFileTempId) {
    log.debug("Request to getDetail : {}", woFileTempId);
    return woFileTempRepository.getDetail(woFileTempId);
  }


  @Override
  public List<WoTypeInsideDTO> getListWoTypeCBB() {
    return woFileTempRepository.getListWoTypeCBB();
  }

  @Override
  public ResultInSideDto insertWoFileTemp(WoFileTempDto woFileTempDto) {
    log.debug("Request to insertWoFileTemp : {}", woFileTempDto);
    return woFileTempRepository.insertWoFileTemp(woFileTempDto);
  }

  @Override
  public ResultInSideDto updateWoFileTemp(WoFileTempDto dto) {
    log.debug("Request to updateWoFileTemp : {}", dto);
    return woFileTempRepository.updateWoFileTemp(dto);
  }

  @Override
  public ResultInSideDto deleteWoFileTempById(Long id) {
    log.debug("Request to deleteWoFileTempById : {}", id);
    return woFileTempRepository.deleteWoFileTempById(id);
  }
}
