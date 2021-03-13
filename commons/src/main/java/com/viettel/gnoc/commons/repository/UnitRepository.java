package com.viettel.gnoc.commons.repository;

import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.utils.ConditionBean;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 * @author TungPV
 */
@Repository
public interface UnitRepository {

  List<UnitDTO> getListUnitDTO(UnitDTO unitDTO, int rowStart, int maxRow, String sortType,
      String sortFieldList);

  List<UnitDTO> getListUnitByLevel(String level);

  List<UnitDTO> getListUnitDTOByListUnitId(List<Long> listUnitId);

  UnitDTO getUnitDTOByUnitCode(String unitCode);

  List<UnitDTO> getListUnitByCondition(List<ConditionBean> conditionBeans, int rowStart,
      int maxRow, String sortType, String sortFieldList);

  UnitDTO findUnitById(Long id);

  List<UnitDTO> getUnitByUnitDTO(UnitDTO unitDTO);

  List<UnitDTO> getListUnitByCodeOrName(UnitDTO unitDTO);

  List<UnitDTO> getListUnit(UnitDTO unitDTO);

  UnitDTO getUnitByUnitCode(String unitCode);

  List<UnitDTO> getListUnit();

  List<UnitDTO> getListUnitChildren(Long parentUnitId);

  List<com.viettel.gnoc.ws.dto.UnitDTO> getUnitDTO(String fromDate, String toDate);

  List<com.viettel.gnoc.ws.dto.UnitDTO> getUnit(com.viettel.gnoc.ws.dto.UnitDTO unitDTO,
      int rowStart, int maxRow);
}
