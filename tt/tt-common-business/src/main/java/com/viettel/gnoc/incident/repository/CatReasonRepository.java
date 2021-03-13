package com.viettel.gnoc.incident.repository;

import com.viettel.gnoc.incident.dto.CatReasonDTO;
import com.viettel.gnoc.incident.dto.CatReasonInSideDTO;
import com.viettel.gnoc.incident.dto.CatReasonInSideDTOSearch;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public interface CatReasonRepository {

  List<CatReasonInSideDTOSearch> getListReasonSearch(CatReasonInSideDTO reasonDto);

  List<CatReasonInSideDTOSearch> getListReasonSearchForWo(CatReasonInSideDTO reasonDto);

  List<CatReasonDTO> getReasonDTOForTree(Boolean isRoot, String typeId, String parentId);

  Map<String, CatReasonInSideDTO> getCatReasonData();

  List<CatReasonDTO> getReasonDTOForVsmart(String typeId, String parentId, int level);

  List<CatReasonDTO> getReasonDTO(String fromDate, String toDate);
}
