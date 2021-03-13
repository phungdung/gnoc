package com.viettel.gnoc.od.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.od.dto.OdTypeDTO;
import java.io.File;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author NamTN
 */
public interface OdTypeBusiness {

  Datatable search(OdTypeDTO odTypeDTO);

  Datatable getListOdType(OdTypeDTO odTypeDTO);

  ResultInSideDto delete(Long odTypeId);

  ResultInSideDto deleteList(List<Long> listOdTypeId);

  OdTypeDTO getDetail(Long odTypeId);

  OdTypeDTO getInforByODType(String odTypeCode);

  ResultInSideDto add(OdTypeDTO odTypeDTO);

  ResultInSideDto edit(OdTypeDTO odTypeDTO);

  File exportData(OdTypeDTO odTypeDTO) throws Exception;

  String getSeqOdType();

  ResultInSideDto importData(MultipartFile uploadfile);

  File getTemplate() throws Exception;
}
