package com.viettel.gnoc.incident.business;

import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.incident.dto.CatReasonDTO;
import com.viettel.gnoc.incident.dto.CatReasonInSideDTO;
import com.viettel.gnoc.incident.dto.CatReasonInSideDTOSearch;
import java.util.List;
import java.util.Map;

public interface CatReasonBusiness {

  List<CatReasonInSideDTOSearch> getListReasonSearch(CatReasonInSideDTO reasonDto);

  List<CatReasonInSideDTOSearch> getListReasonSearchForWo(CatReasonInSideDTO reasonDto);

  Map<String, CatReasonInSideDTO> getCatReasonData();

  List<CatItemDTO> getListCatReason(Long parentId, String excludeCode);

  List<CatReasonDTO> getReasonDTOForTreeByTroubleCode(Boolean isRoot, String troubleCode,
      String parentId);

  List<CatReasonDTO> getReasonDTOForVsmart(String troubleCode, String parentId, int level);

  List<CatReasonDTO> getReasonDTO(String fromDate, String toDate);
}
