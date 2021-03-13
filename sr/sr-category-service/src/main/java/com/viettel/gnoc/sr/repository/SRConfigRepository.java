package com.viettel.gnoc.sr.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.sr.dto.SRConfigDTO;
import java.util.List;


public interface SRConfigRepository {

  Datatable getListSRConfigPage(SRConfigDTO srConfigDTO);

  List<SRConfigDTO> getListConfigGroup(String parentCode);

  List<SRConfigDTO> getByConfigGroup(SRConfigDTO srConfigDTO);

  ResultInSideDto add(SRConfigDTO srConfigDTO);

  SRConfigDTO getDetail(Long configId);

  ResultInSideDto edit(SRConfigDTO srConfigDTO);

  SRConfigDTO checkConfigCatalog(String configCode, String configGroup);

  SRConfigDTO getConfigGroupByConfigCode(String configCode, String status, String parentGroup,
      Long serviceId);

  ResultInSideDto delete(Long ConfigId);
}
