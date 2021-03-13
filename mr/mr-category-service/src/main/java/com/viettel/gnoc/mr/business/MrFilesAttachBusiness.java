package com.viettel.gnoc.mr.business;

import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.maintenance.dto.MrFilesAttachDTO;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface MrFilesAttachBusiness {

  List<GnocFileDto> getListMrFilesSearch(GnocFileDto attach);

  ResultInSideDto deleteFile(GnocFileDto gnocFileDto);

  ResultInSideDto insertFile(List<MultipartFile> lstMultipartFiles,
      MrFilesAttachDTO mrFilesAttachDTO) throws Exception;
}
