package com.viettel.gnoc.wo.business;

import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.wo.dto.CfgFtOnTimeDTO;
import java.io.File;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface CfgFtOnTimeBusiness {

  //  ResultInSideDto delete(Long id);
//
//  File getTemplate() throws Exception;
//
//  File exportData(CfgMapUnitGnocNimsDTO cfgMapUnitGnocNimsDTO) throws Exception;
//
//  ResultInSideDto importData(MultipartFile upLoadFile) throws Exception;
//
  ResultInSideDto insertOrUpdate(CfgFtOnTimeDTO cfgFtOnTimeDTO);

  //
//  ResultInSideDto update(CfgMapUnitGnocNimsDTO cfgMapUnitGnocNimsDTO);
//
  CfgFtOnTimeDTO findById(Long id);

  List<UsersInsideDto> getListUserByCdGroupCBB(String cdGroupId);

  Datatable onSearch(CfgFtOnTimeDTO cfgFtOnTimeDTO);

  File exportSearchData(CfgFtOnTimeDTO cfgFtOnTimeDTO);

  List<CatItemDTO> getLstBusinessCBB();

  ResultInSideDto importData(MultipartFile upLoadFile) throws Exception;

  File getTemplate() throws Exception;

  ResultInSideDto delete(Long id);

  ResultInSideDto getWoCdGroupByUnitCurrentSession();

}
