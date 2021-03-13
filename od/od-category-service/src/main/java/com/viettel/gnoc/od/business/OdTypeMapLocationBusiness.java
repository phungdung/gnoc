package com.viettel.gnoc.od.business;

import com.viettel.gnoc.od.dto.OdTypeMapLocationDTO;

/**
 * @author TruongNT
 */
public interface OdTypeMapLocationBusiness {

  OdTypeMapLocationDTO getListOdTypeMapByOdTypeIdAndLocation(Long odTypeId, String locationCode);
}
