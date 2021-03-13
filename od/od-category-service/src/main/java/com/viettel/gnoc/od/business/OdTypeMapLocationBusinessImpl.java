package com.viettel.gnoc.od.business;

import com.viettel.gnoc.od.dto.OdTypeMapLocationDTO;
import com.viettel.gnoc.od.repository.OdTypeMapLocationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author TruongNT
 */
@Service
@Transactional
@Slf4j
public class OdTypeMapLocationBusinessImpl implements OdTypeMapLocationBusiness {

  @Autowired
  OdTypeMapLocationRepository odTypeMapLocationRepository;


  @Override
  public OdTypeMapLocationDTO getListOdTypeMapByOdTypeIdAndLocation(Long odTypeId,
      String locationCode) {
    return odTypeMapLocationRepository
        .getListOdTypeMapByOdTypeIdAndLocation(odTypeId, locationCode);
  }
}
