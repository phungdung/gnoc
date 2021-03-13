package com.viettel.gnoc.cr.business;

import com.viettel.gnoc.cr.dto.CrProcessDTO;
import com.viettel.gnoc.cr.dto.CrProcessInsideDTO;
import com.viettel.gnoc.cr.dto.ItemDataCR;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.cr.repository.CrProcessRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class CrProcessBusinessImpl implements CrProcessBusiness {

  @Autowired
  CrProcessRepository crProcessRepository;

  @Override
  public CrProcessInsideDTO findCrProcessById(Long id) {
    return crProcessRepository.findCrProcessById(id);
  }

  @Override
  public List<ItemDataCRInside> getListCrProcessCBB(CrProcessInsideDTO crProcessDTO) {
    return crProcessRepository.getListCrProcessCBB(crProcessDTO);
  }

  @Override
  public List<CrProcessInsideDTO> getListCrProcessLevel3CBB(CrProcessInsideDTO crProcessDTO) {
    return crProcessRepository.getListCrProcessLevel3CBB(crProcessDTO);
  }

  @Override
  public List<CrProcessDTO> synchCrProcess(List<Long> lstImpactSegment) {
    return crProcessRepository.synchCrProcess(lstImpactSegment);
  }

  @Override
  public List<ItemDataCR> getListCrProcessCBB(CrProcessDTO form, String locale) {
    return crProcessRepository.getListCrProcessCBB(form, locale);
  }
}
