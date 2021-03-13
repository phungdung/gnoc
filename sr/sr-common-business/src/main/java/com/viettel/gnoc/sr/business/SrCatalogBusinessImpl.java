package com.viettel.gnoc.sr.business;

import com.viettel.gnoc.sr.dto.SRCatalogDTO;
import com.viettel.gnoc.sr.repository.SRCatalogRepository2;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
public class SrCatalogBusinessImpl implements SrCatalogBusiness {

  @Autowired
  SRCatalogRepository2 srCatalogRepository;

  @Override
  public List<SRCatalogDTO> getListCatalog(SRCatalogDTO dto) {
    return srCatalogRepository.getListCatalog(dto);
  }

  @Override
  public SRCatalogDTO findByIdCatalog(String serviceId) {
    return srCatalogRepository.findById(Long.valueOf(serviceId));
  }
}
