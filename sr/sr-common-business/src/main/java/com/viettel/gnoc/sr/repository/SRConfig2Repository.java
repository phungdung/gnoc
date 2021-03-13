package com.viettel.gnoc.sr.repository;

import com.viettel.gnoc.sr.dto.SRConfig2DTO;
import java.util.List;

public interface SRConfig2Repository {

  List<SRConfig2DTO> getFile(SRConfig2DTO dto);

  SRConfig2DTO getFileTypeByConfigCode(String configCode, String status, String configGroup);
}
