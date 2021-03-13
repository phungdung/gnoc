package com.viettel.gnoc.commons.business;

import com.viettel.gnoc.commons.dto.CatLocationDTO;
import com.viettel.gnoc.commons.dto.MrLocationDTO;
import com.viettel.gnoc.commons.repository.CatLocationRepository;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author NamTN
 */
@Service
@Transactional
@Slf4j
public class CatLocationBusinessImpl implements CatLocationBusiness {

  @Autowired
  protected CatLocationRepository catLocationRepository;

  @Override
  public List<CatLocationDTO> getCatLocationByParentId(String parentId) {
    return catLocationRepository.getCatLocationByParentId(parentId);
  }

  @Override
  public List<CatLocationDTO> getListCatLocationDTO(CatLocationDTO catLocationDTO, int rowStart,
      int maxRow, String sortType, String sortFieldList) {
    return catLocationRepository
        .getListCatLocationDTO(catLocationDTO, rowStart, maxRow, sortType, sortFieldList);
  }

  @Override
  public List<CatLocationDTO> getCatLocationByLevel(String level) {
    return catLocationRepository.getCatLocationByLevel(level);
  }

  @Override
  public CatLocationDTO getLocationByCode(String locationCode, String locationId,
      String nationCode) {
    return catLocationRepository.getLocationByCode(locationCode, locationId, nationCode);
  }

  @Override
  public List<CatLocationDTO> getListLocationProvince() {
    return catLocationRepository.getListLocationProvince();
  }

  @Override
  public List<ItemDataCRInside> getListLocationByLevelCBB(Object form, Long level, Long parentId) {
    return catLocationRepository.getListLocationByLevelCBB(form, level, parentId);
  }

  @Override
  public List<CatLocationDTO> searchByConditionBean(List<ConditionBean> lstCondition, int rowStart,
      int maxRow, String sortType, String sortFieldList) {
    return catLocationRepository
        .searchByConditionBean(lstCondition, rowStart, maxRow, sortType, sortFieldList);
  }

  /**
   * @author tripm
   */
  @Override
  public List<MrLocationDTO> getListLocationByStationCode(String stationCode) {
    return catLocationRepository.getListLocationByStationCode(stationCode);
  }
}
