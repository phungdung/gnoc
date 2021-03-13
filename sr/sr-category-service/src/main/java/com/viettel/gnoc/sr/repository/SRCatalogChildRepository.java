package com.viettel.gnoc.sr.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.sr.dto.SRCatalogChildDTO;
import java.util.List;

public interface SRCatalogChildRepository {

  List<SRCatalogChildDTO> getListCatalogChild(SRCatalogChildDTO srCatalogChildDTO);

  ResultInSideDto delete(Long childId);

  ResultInSideDto deleteList(List<Long> childIdList);

  ResultInSideDto add(SRCatalogChildDTO srCatalogChildDTO);

  ResultInSideDto insertList(List<SRCatalogChildDTO> srCatalogChildDTOList);

  SRCatalogChildDTO getDetail(Long childId);


}
