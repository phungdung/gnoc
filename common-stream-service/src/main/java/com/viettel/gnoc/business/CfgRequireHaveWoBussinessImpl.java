package com.viettel.gnoc.business;

import com.viettel.gnoc.commons.dto.CfgRequireHaveWoDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.CfgRequireHaveWoRepository;
import com.viettel.gnoc.commons.utils.DateUtil;
import com.viettel.gnoc.incident.dto.CatReasonDTO;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class CfgRequireHaveWoBussinessImpl implements CfgRequireHaveWoBussiness {

  @Autowired
  private CfgRequireHaveWoRepository cfgRequireHaveWoRepository;

  @Override
  public Datatable getListCfgRequireHaveWo(CfgRequireHaveWoDTO dto) {
    log.debug("Request to getListCfgRequireHaveWo : {}", dto);
    return cfgRequireHaveWoRepository.getListCfgRequireHaveWo(dto);
  }


  @Override
  public CfgRequireHaveWoDTO getDetail(Long id) {
    log.debug("Request to getDetail : {}", id);
    return cfgRequireHaveWoRepository.getDetail(id);
  }


  @Override
  public ResultInSideDto insert(CfgRequireHaveWoDTO cfgRequireHaveWoDTO) {
    log.debug("Request to insert : {}", cfgRequireHaveWoDTO);
    cfgRequireHaveWoDTO.setLastUpdateTime(DateUtil
        .dateToStringWithPattern(new Date(System.currentTimeMillis()), "dd/MM/yyyy HH:mm:ss"));
    return cfgRequireHaveWoRepository.insert(cfgRequireHaveWoDTO);
  }

  @Override
  public ResultInSideDto update(CfgRequireHaveWoDTO cfgRequireHaveWoDTO) {
    log.debug("Request to update : {}", cfgRequireHaveWoDTO);
    cfgRequireHaveWoDTO.setLastUpdateTime(DateUtil
        .dateToStringWithPattern(new Date(System.currentTimeMillis()), "dd/MM/yyyy HH:mm:ss"));
    return cfgRequireHaveWoRepository.update(cfgRequireHaveWoDTO);

  }

  @Override
  public ResultInSideDto delete(Long id) {
    log.debug("Request to delete : {}", id);
    return cfgRequireHaveWoRepository.delete(id);
  }

  @Override
  public List<CatReasonDTO> getReasonDTOForTree(CatReasonDTO catReasonDTO) {
    return cfgRequireHaveWoRepository.getReasonDTOForTree(catReasonDTO);
  }

}
