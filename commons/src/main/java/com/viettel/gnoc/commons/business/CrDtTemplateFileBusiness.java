package com.viettel.gnoc.commons.business;

import com.viettel.gnoc.commons.dto.CrDtTemplateFileDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.cr.dto.CrProcessInsideDTO;
import java.util.List;
import java.util.Map;
import org.springframework.web.multipart.MultipartFile;

public interface CrDtTemplateFileBusiness {

  List<CrProcessInsideDTO> getParentProcessCBB();

  List<CrProcessInsideDTO> getChilProcessCBB(Long parentId);

  Map<String, String> getLstFileTypeCBB();

  Datatable getListCrDtTemplateFile(CrDtTemplateFileDTO crDtTemplateFileDTO);

  ResultInSideDto saveOrUpdate(List<MultipartFile> crFile, CrDtTemplateFileDTO crDtTemplateFileDTO);

  ResultInSideDto delete(Long id);

  CrDtTemplateFileDTO getObjById(Long paramLong);
}
