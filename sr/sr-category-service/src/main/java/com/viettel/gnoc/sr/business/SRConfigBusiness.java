package com.viettel.gnoc.sr.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.sr.dto.SRConfigDTO;
import java.util.List;

public interface SRConfigBusiness {

  Datatable getListSRConfigPage(SRConfigDTO srConfigDTO);

  ResultInSideDto insert(SRConfigDTO srConfigDTO);

  SRConfigDTO getDetail(Long configId);

  ResultInSideDto update(SRConfigDTO srConfigDTO);

  ResultInSideDto delete(Long configId);

  List<SRConfigDTO> getListConfigGroup(String parentCode);

  List<SRConfigDTO> getByConfigGroup(SRConfigDTO srConfigDTO);

}
