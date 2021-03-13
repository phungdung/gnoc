package com.viettel.gnoc.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface UnitCommonRepository {

  List<UnitDTO> getListUnit(UnitDTO unitDTO);

  Datatable getListUnitDTO(UnitDTO unitDTO);

  List<UnitDTO> getListUnitDTOExport(UnitDTO unitDTO);

  ResultInSideDto updateUnit(UnitDTO unitDTO);

  ResultInSideDto updateUnitChildren(UnitDTO unitDTO);

  ResultInSideDto deleteUnit(Long id);

  ResultInSideDto deleteListUnit(List<UnitDTO> unitListDTO);

  UnitDTO findUnitById(Long id);

  ResultInSideDto insertUnit(UnitDTO unitDTO);

  ResultInSideDto insertOrUpdateListUnit(List<UnitDTO> unitDTO);

  List<UnitDTO> getListUnitByLevel(String level);

  List<UnitDTO> getUnitDTOForTree(
      Boolean isRoot,
      String status,
      String parentId);

  List<UnitDTO> getUnitVSADTOForTree(
      Boolean isRoot,
      String status,
      String parentId);

  List<UnitDTO> getListUnitVSA(UnitDTO unitDTO);

  List<UnitDTO> getListUnitVSANotName(UnitDTO unitDTO);

  Datatable getListUnitDatatableAll(UnitDTO unitDTO);

  List<UnitDTO> getListUnitNotLike(UnitDTO unitDTO);

  List<UnitDTO> getListUnitVSANotNameAndActive(UnitDTO unitDTO);
}
