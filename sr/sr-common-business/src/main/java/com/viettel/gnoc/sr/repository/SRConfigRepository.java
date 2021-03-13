package com.viettel.gnoc.sr.repository;

import com.viettel.gnoc.sr.dto.SRConfigDTO;
import java.util.List;

public interface SRConfigRepository {

  List<SRConfigDTO> getByConfigGroup(String configGroup);

  List<SRConfigDTO> getConfigGroup(String parentCode);

  List<SRConfigDTO> getSmsContent(String nextStatus, String serviceArray, String serviceGroup,
      String serviceCode, String configGroup);

  List<SRConfigDTO> getDataByConfigCode(SRConfigDTO dto);

  List<SRConfigDTO> getSmsContentByConfig(SRConfigDTO configDTO);

  List<SRConfigDTO> getCrInforByParentGroup(String parentGroup);
}
