package com.viettel.gnoc.commons.business;

import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.dto.UsersDTO;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.commons.repository.UnitRepository;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.commons.utils.ConditionBeanUtil;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author TungPV
 */
@Service
@Transactional
@Slf4j
public class UnitBusinessImpl implements UnitBusiness {

  @Autowired
  protected UnitRepository unitRepository;

  @Override
  public List<UnitDTO> getListUnitDTO(UnitDTO unitDTO, int rowStart, int maxRow, String sortType,
      String sortFieldList) {
    return unitRepository.getListUnitDTO(unitDTO, rowStart, maxRow, sortType, sortFieldList);
  }

  @Override
  public List<UnitDTO> getListUnitByLevel(String level) {
    return unitRepository.getListUnitByLevel(level);
  }

  @Override
  public List<UnitDTO> getListUnitDTOByListUnitId(List<Long> listUnitId) {
    return unitRepository.getListUnitDTOByListUnitId(listUnitId);
  }

  @Override
  public List<UnitDTO> getListUnitByCondition(List<ConditionBean> conditionBeans, int rowStart,
      int maxRow, String sortType, String sortFieldList) {
    ConditionBeanUtil.sysToOwnListCondition(conditionBeans);
    return unitRepository
        .getListUnitByCondition(conditionBeans, rowStart, maxRow, sortType, sortFieldList);
  }

  @Override
  public UnitDTO findUnitById(Long id) {
    return unitRepository.findUnitById(id);
  }

  @Override
  public List<UnitDTO> getListUnit(UnitDTO unitDTO) {
    return unitRepository.getListUnit(unitDTO);
  }

  @Override
  public List<UnitDTO> getListUnit() {
    return unitRepository.getListUnit();
  }

  @Override
  public List<com.viettel.gnoc.ws.dto.UnitDTO> getUnitDTO(String fromDate, String toDate) {
    return unitRepository.getUnitDTO(fromDate, toDate);
  }

  @Override
  public List<com.viettel.gnoc.ws.dto.UnitDTO> getUnit(com.viettel.gnoc.ws.dto.UnitDTO unitDTO,
      int rowStart, int maxRow) {
    return unitRepository.getUnit(unitDTO, rowStart, maxRow);
  }
}
