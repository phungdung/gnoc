package com.viettel.gnoc.kedb.business;

import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.kedb.dto.KedbFilesDTO;
import com.viettel.gnoc.kedb.repository.KedbFilesRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class KedbFilesBusinessImpl implements KedbFilesBusiness {

  @Autowired
  KedbFilesRepository kedbFilesRepository;

  @Override
  public List<com.viettel.gnoc.ws.dto.KedbFilesDTO> onSearch(
      com.viettel.gnoc.ws.dto.KedbFilesDTO kedbFilesDTO, int rowStart, int maxRow,
      String sortType, String sortFieldList) {
    List<KedbFilesDTO> lst = kedbFilesRepository
        .onSearch(kedbFilesDTO, rowStart, maxRow, sortType, sortFieldList);
    List<com.viettel.gnoc.ws.dto.KedbFilesDTO> lstResult = new ArrayList<>();
    if (lst != null && !lst.isEmpty()) {
      lst.forEach(item -> {
        com.viettel.gnoc.ws.dto.KedbFilesDTO dto = new com.viettel.gnoc.ws.dto.KedbFilesDTO();
        dto.setKedbFileId(StringUtils.isLongNullOrEmpty(item.getKedbFileId()) ? null
            : item.getKedbFileId().toString());
        dto.setContent(item.getContent());
        dto.setKedbFileName(item.getKedbFileName());
        dto.setCreateUnitId(StringUtils.isLongNullOrEmpty(item.getCreateUnitId()) ? null
            : item.getCreateUnitId().toString());
        dto.setCreateUnitName(item.getCreateUnitName());
        dto.setCreateUnitId(StringUtils.isLongNullOrEmpty(item.getCreateUserId()) ? null
            : item.getCreateUserId().toString());
        dto.setCreateUserName(item.getCreateUserName());
        dto.setKedbId(
            StringUtils.isLongNullOrEmpty(item.getKedbId()) ? null : item.getKedbId().toString());
        lstResult.add(dto);
      });
    }
    return lstResult;
  }
}
