package com.viettel.gnoc.commons.repository;

import com.viettel.gnoc.commons.dto.CatLocationDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.MrLocationDTO;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 * @author NamTN
 */
@Repository
public interface CatLocationRepository {

  List<CatLocationDTO> getCatLocationByParentId(String parentId);

  List<CatLocationDTO> getListCatLocationDTO(CatLocationDTO catLocationDTO, int rowStart,
      int maxRow, String sortType, String sortFieldList);

  List<CatLocationDTO> getCatLocationByLevel(String level);

  CatLocationDTO getLocationByCode(String locationCode, String locationId, String nationCode);

  CatLocationDTO getNationByLocationId(Long locationId);

  List<CatLocationDTO> getListLocationProvince();

  List<ItemDataCRInside> getListLocationByLevelCBB(Object form, Long level, Long parentId);

  List<CatLocationDTO> searchByConditionBean(
      List<ConditionBean> lstCondition, int rowStart, int maxRow, String sortType,
      String sortFieldList);

  Datatable getLocationDatapicker(CatLocationDTO catLocationDTO);

  /**
   * @author tripm
   */
  List<MrLocationDTO> getListLocationByStationCode(String stationCode);
}
