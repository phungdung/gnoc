package com.viettel.gnoc.commons.repository;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.CrDtTemplateFileDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;

public interface CrDtTemplateFileRepository {

  BaseDto sqlSearch(CrDtTemplateFileDTO crDtTemplateFileDTO);

  Datatable getListCrDtTemplateFile(CrDtTemplateFileDTO crDtTemplateFileDTO);

  ResultInSideDto saveOrUpdate(CrDtTemplateFileDTO crDtTemplateFileDTO);

  ResultInSideDto delete(Long id);

  CrDtTemplateFileDTO getObjById(Long paramLong);
}
