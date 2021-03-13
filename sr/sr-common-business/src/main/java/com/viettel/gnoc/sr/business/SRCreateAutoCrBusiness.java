package com.viettel.gnoc.sr.business;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.sr.dto.SRCreateAutoCRDTO;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface SRCreateAutoCrBusiness {

  SRCreateAutoCRDTO findSrCreateAutoBySrId(SRCreateAutoCRDTO dto);

  ResultInSideDto insertOrUpdateSRCreateAutoCr(List<MultipartFile> srFileList,
      List<MultipartFile> filesProcess, SRCreateAutoCRDTO dto);
}
