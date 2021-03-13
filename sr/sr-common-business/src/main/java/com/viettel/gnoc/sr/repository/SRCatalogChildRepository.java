package com.viettel.gnoc.sr.repository;

import com.viettel.gnoc.sr.dto.SRCatalogChildDTO;
import java.util.List;

public interface SRCatalogChildRepository {

  List<SRCatalogChildDTO> getListCatalogChild(SRCatalogChildDTO srCatalogChildDTO);

  String checkGenerateNo(SRCatalogChildDTO srCatalogChildDTO);
}
