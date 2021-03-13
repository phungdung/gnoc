package com.viettel.gnoc.od.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.od.dto.OdTypeMapLocationDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 * @author TruongNT
 */
@Repository
public interface OdTypeMapLocationRepository {

  List<OdTypeMapLocationDTO> getListOdTypeMapLocationByOdTypeId(Long odTypeId);

  ResultInSideDto add(OdTypeMapLocationDTO odTypeMapLocationDTO);

  ResultInSideDto deleteListOdTypeMapLocationDetail(Long odTypeId);

  OdTypeMapLocationDTO getListOdTypeMapByOdTypeIdAndLocation(Long odTypeId, String locationCode);
}
