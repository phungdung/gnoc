package com.viettel.gnoc.incident.business;

import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.incident.dto.CatReasonDTO;
import com.viettel.gnoc.incident.dto.CatReasonInSideDTO;
import com.viettel.gnoc.incident.dto.CatReasonInSideDTOSearch;
import com.viettel.gnoc.incident.dto.TroublesInSideDTO;
import com.viettel.gnoc.incident.repository.CatReasonRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class CatReasonBusinessImpl implements CatReasonBusiness {

  @Autowired
  CatReasonRepository catReasonRepository;

  @Autowired
  TroublesBusiness troublesBusiness;

  @Override
  public List<CatReasonInSideDTOSearch> getListReasonSearch(CatReasonInSideDTO reasonDto) {
    return catReasonRepository.getListReasonSearch(reasonDto);
  }

  @Override
  public List<CatReasonInSideDTOSearch> getListReasonSearchForWo(CatReasonInSideDTO reasonDto) {
    return catReasonRepository.getListReasonSearchForWo(reasonDto);
  }

  @Override
  public Map<String, CatReasonInSideDTO> getCatReasonData() {
    log.debug("Request to getCatReasonData");
    return catReasonRepository.getCatReasonData();
  }

  @Override
  public List<CatItemDTO> getListCatReason(Long parentId, String excludeCode) {
    List<CatItemDTO> lstReturn = new ArrayList();
    try {
      CatReasonInSideDTO dtoSearch = new CatReasonInSideDTO();
      dtoSearch.setParentId(parentId);
      List<CatReasonInSideDTOSearch> lstReason = getListReasonSearch(dtoSearch);
      for (CatReasonInSideDTOSearch reason : lstReason) {
        CatItemDTO catDTO = new CatItemDTO();
        if (!excludeCode.equals(reason.getReasonCode())) {
          catDTO.setItemId(reason.getId());
          catDTO.setItemCode(reason.getReasonCode());
          catDTO.setItemName(reason.getReasonName());
          catDTO.setDescription(reason.getDescription());
          lstReturn.add(catDTO);
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return lstReturn;
  }

  @Override
  public List<CatReasonDTO> getReasonDTOForTreeByTroubleCode(Boolean isRoot, String troubleCode,
      String parentId) {
    log.debug("Request to getReasonDTOForTreeByTroubleCode: {}", parentId);
    TroublesInSideDTO troubles = new TroublesInSideDTO();
    troubles.setTroubleCode(troubleCode);
    List<TroublesInSideDTO> lst = troublesBusiness.getTroubleInfo(troubles);
    String typeId;
    if (lst != null && !lst.isEmpty()) {
      typeId = lst.get(0).getTypeId().toString();
    } else {
      return null;
    }
    return catReasonRepository.getReasonDTOForTree(isRoot, typeId, parentId);
  }

  @Override
  public List<CatReasonDTO> getReasonDTOForVsmart(String troubleCode, String parentId, int level) {
    log.debug("Request to getReasonDTOForVsmart: {}", parentId);
    if (troubleCode == null || "".equals(troubleCode)) {
      return null;
    }
    TroublesInSideDTO troubles = new TroublesInSideDTO();
    troubles.setTroubleCode(troubleCode);
    List<TroublesInSideDTO> lst = troublesBusiness.getTroubleInfo(troubles);
    String typeId;
    if (lst != null && !lst.isEmpty()) {
      typeId = lst.get(0).getTypeId().toString();
    } else {
      return null;
    }
    return catReasonRepository.getReasonDTOForVsmart(typeId, parentId, level);
  }

  @Override
  public List<CatReasonDTO> getReasonDTO(String fromDate, String toDate) {
    return catReasonRepository.getReasonDTO(fromDate, toDate);
  }
}
