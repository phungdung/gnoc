package com.viettel.gnoc.commons.business;


import com.viettel.gnoc.commons.dto.CatLocationDTO;
import com.viettel.gnoc.commons.dto.MrLocationDTO;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import java.util.List;

/**
 * @author NamTN
 */
public interface CatLocationBusiness {

  List<CatLocationDTO> getCatLocationByParentId(String parentId);

  List<CatLocationDTO> getListCatLocationDTO(CatLocationDTO catLocationDTO, int rowStart,
      int maxRow, String sortType, String sortFieldList);

  List<CatLocationDTO> getCatLocationByLevel(String level);

  CatLocationDTO getLocationByCode(String locationCode, String locationId, String nationCode);

  List<CatLocationDTO> getListLocationProvince();

  List<ItemDataCRInside> getListLocationByLevelCBB(Object form, Long level, Long parentId);

  List<CatLocationDTO> searchByConditionBean(
      List<ConditionBean> lstCondition, int rowStart, int maxRow, String sortType,
      String sortFieldList);

  /**
   * @author tripm
   */
  List<MrLocationDTO> getListLocationByStationCode(String stationCode);
}
