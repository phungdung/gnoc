package com.viettel.gnoc.business;


import com.viettel.gnoc.commons.dto.CfgUnitTtSpmDTO;
import com.viettel.gnoc.commons.dto.CfgUnitTtSpmSearchDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import java.util.List;

public interface CfgUnitTtSpmBusiness {

  //List<CatItemDTO> getListCatItemDTOByListCategory();
  String updateCfgUnitTtSpm(CfgUnitTtSpmDTO cfgUnitTtSpmDTO);

  String deleteCfgUnitTtSpm(Long id);

  String deleteListCfgUnitTtSpm(List<CfgUnitTtSpmDTO> cfgUnitTtSpmListDTO);

  List<CfgUnitTtSpmDTO> getListCfgUnitTtSpmDTO(CfgUnitTtSpmDTO cfgUnitTtSpmDTO, int rowStart,
      int maxRow, String sortType, String sortFieldList);

  ResultInSideDto insertCfgUnitTtSpm(CfgUnitTtSpmDTO cfgUnitTtSpmDTO);

  String insertOrUpdateListCfgUnitTtSpm(List<CfgUnitTtSpmDTO> cfgUnitTtSpmDTO);

  List<String> getSequenseCfgUnitTtSpm(String seqName, int... size);

  CfgUnitTtSpmDTO findCfgUnitTtSpmById(Long id);

  Datatable getListUnitTtSpmSearch(CfgUnitTtSpmSearchDTO cfgUnitTtSpmSearchDTO);
}

