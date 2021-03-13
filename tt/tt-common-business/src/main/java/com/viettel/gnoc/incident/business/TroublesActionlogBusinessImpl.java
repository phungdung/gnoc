package com.viettel.gnoc.incident.business;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.repository.CatItemRepository;
import com.viettel.gnoc.commons.utils.Constants.TROUBLE;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.incident.dto.TroubleActionLogsDTO;
import com.viettel.gnoc.incident.dto.TroublesInSideDTO;
import com.viettel.gnoc.incident.repository.TroubleActionLogsRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@Service
public class TroublesActionlogBusinessImpl implements TroublesActionlogBusiness {

  @Autowired
  TroubleActionLogsRepository troubleActionLogsRepository;

  @Autowired
  CatItemRepository catItemRepository;

  @Override
  public Datatable getListTroubleActionLogsDTO(TroublesInSideDTO troubleDTO) {
    Datatable datatable = new Datatable();
    TroubleActionLogsDTO troubleActionLogsDTO = new TroubleActionLogsDTO();
    troubleActionLogsDTO.setTroubleId(troubleDTO.getTroubleId());
    List<TroubleActionLogsDTO> troublesDTOList = troubleActionLogsRepository
        .getListTroubleActionLogsDTO(troubleActionLogsDTO, 0, Integer.MAX_VALUE,
            troubleDTO.getSortType(),
            troubleDTO.getSortName());
    List<String> lstCategoryCode = new ArrayList<>();
    lstCategoryCode.add(TROUBLE.TT_STATE);
    List<CatItemDTO> catItemDTOS = catItemRepository.getListCatItemDTO(lstCategoryCode, "1");
    Map<String, CatItemDTO> mapItem = new HashMap<>();
    if (catItemDTOS != null && catItemDTOS.size() > 0) {
      catItemDTOS.forEach(i -> {
        mapItem.put(String.valueOf(i.getItemId()), i);
      });
    }

    if (troublesDTOList != null && troublesDTOList.size() > 0) {
      int pageSize = (int) Math.ceil(troublesDTOList.size() * 1.0 / troubleDTO.getPageSize());
      datatable.setTotal(troublesDTOList.size());
      datatable.setPages(pageSize);
      troublesDTOList = (List<TroubleActionLogsDTO>) DataUtil
          .subPageList(troublesDTOList, troubleDTO.getPage(), troubleDTO.getPageSize());
      if (troublesDTOList != null && !troublesDTOList.isEmpty()) {
        for (TroubleActionLogsDTO dto : troublesDTOList) {
          dto.setStateName(
              mapItem.get(String.valueOf(dto.getStateId())) != null ? mapItem
                  .get(String.valueOf(dto.getStateId())).getItemName()
                  : "");
          String language = I18n.getLocale();
          if ("vi".equals(language) || "vi_VN".equals(language) || "vi_vn".equals(language)) {
          } else {
            if (dto.getType() != null && I18n.getLanguage("incident.in")
                .equalsIgnoreCase(dto.getType())) {
              dto.setType(I18n.getLanguage("incident.insert"));
            } else if (dto.getType() != null && I18n.getLanguage("incident.up")
                .equalsIgnoreCase(dto.getType())) {
              dto.setType(I18n.getLanguage("incident.update"));
            } else if (dto.getType() != null && I18n.getLanguage("incident.can")
                .equalsIgnoreCase(dto.getType())) {
              dto.setType(I18n.getLanguage("incident.cancel"));
            }
          }
        }
      }
      datatable.setData(troublesDTOList);
    }

    return datatable;
  }
}
