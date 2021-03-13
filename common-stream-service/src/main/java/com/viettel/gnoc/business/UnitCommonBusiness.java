package com.viettel.gnoc.business;

import com.viettel.gnoc.commons.dto.CatLocationDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import java.io.File;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface UnitCommonBusiness {

  List<UnitDTO> getListUnit(UnitDTO unitDTO);

  Datatable getListUnitDTO(UnitDTO unitDTO);

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

  File exportData(UnitDTO unitDTO) throws Exception;

  File getUnitTemplate() throws Exception;

  ResultInSideDto importData(MultipartFile uploadFile) throws Exception;

  Datatable getListUnitDatatableAll(UnitDTO unitDTO);

  Datatable getListLocationAll(CatLocationDTO catLocationDTO);
}
