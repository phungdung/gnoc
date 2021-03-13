package com.viettel.gnoc.wo.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.wo.dto.CfgMapUnitGnocNimsDTO;
import java.io.File;
import org.springframework.web.multipart.MultipartFile;

public interface CfgMapUnitGnocNimsBusiness {

  Datatable getListCfgMapUnitGnocNimsPage(CfgMapUnitGnocNimsDTO cfgMapUnitGnocNimsDTO);

  ResultInSideDto delete(Long id);

  File getTemplate() throws Exception;

  File exportData(CfgMapUnitGnocNimsDTO cfgMapUnitGnocNimsDTO) throws Exception;

  ResultInSideDto importData(MultipartFile upLoadFile) throws Exception;

  ResultInSideDto insert(CfgMapUnitGnocNimsDTO cfgMapUnitGnocNimsDTO);

  ResultInSideDto update(CfgMapUnitGnocNimsDTO cfgMapUnitGnocNimsDTO);

  CfgMapUnitGnocNimsDTO findById(Long id);

}
