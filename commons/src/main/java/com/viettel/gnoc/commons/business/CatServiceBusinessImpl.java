package com.viettel.gnoc.commons.business;

import com.viettel.gnoc.commons.dto.CatServiceDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.repository.CatServiceRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class CatServiceBusinessImpl implements CatServiceBusiness {

  @Autowired
  protected CatServiceRepository catServiceRepository;


  @Override
  public List<CatServiceDTO> getListCatServiceCBB() {
    log.info("Request to getListCatServiceCBB : {}");
    return catServiceRepository.getListCatServiceCBB();
  }

  @Override
  public Datatable getItemServiceMaster(String system, String type, String idColName,
      String nameCol) {
    log.info("Request to getItemServiceMaster : {}");
    return catServiceRepository.getItemServiceMaster(system, type, idColName, nameCol);
  }

  @Override
  public Long getServiceIdByCcServiceId(String ccServiceId) {
    log.info("Request to getServiceIdByCcServiceId : {}");
    return catServiceRepository.getServiceIdByCcServiceId(ccServiceId);
  }
}
