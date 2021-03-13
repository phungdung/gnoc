package com.viettel.gnoc.commons.business;

import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.utils.ConditionBean;
import java.util.List;

/**
 * @author NamTN
 */
public interface UnitBusiness {

  List<UnitDTO> getListUnitDTO(UnitDTO unitDTO, int rowStart, int maxRow, String sortType,
      String sortFieldList);

  List<UnitDTO> getListUnitByLevel(String level);

  List<UnitDTO> getListUnitDTOByListUnitId(List<Long> listUnitId);

  List<UnitDTO> getListUnitByCondition(List<ConditionBean> conditionBeans, int rowStart,
      int maxRow, String sortType, String sortFieldList);

  UnitDTO findUnitById(Long id);

  List<UnitDTO> getListUnit(UnitDTO unitDTO);

  List<UnitDTO> getListUnit();

  List<com.viettel.gnoc.ws.dto.UnitDTO> getUnitDTO(String fromDate, String toDate);

  List<com.viettel.gnoc.ws.dto.UnitDTO> getUnit(com.viettel.gnoc.ws.dto.UnitDTO unitDTO,
      int rowStart, int maxRow);
}
