package com.viettel.gnoc.kedb.service;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.kedb.business.KedbFilesBusiness;
import com.viettel.gnoc.ws.dto.KedbFilesDTO;
import java.util.List;
import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KedbFilesServiceImpl implements KedbFilesService {

  @Autowired
  KedbFilesBusiness kedbFilesBusiness;

  @Resource
  WebServiceContext wsContext;

  @Override
  public List<KedbFilesDTO> getListKedbFilesDTO(
      com.viettel.gnoc.ws.dto.KedbFilesDTO kedbFilesDTO, int rowStart, int maxRow,
      String sortType, String sortFieldList) {
    log.info("Request to getListKedbFilesDTO : {}", kedbFilesDTO, rowStart, maxRow, sortType,
        sortFieldList);
    I18n.setLocaleForService(wsContext);
    if (kedbFilesDTO != null) {
      return kedbFilesBusiness.onSearch(kedbFilesDTO,rowStart,maxRow,sortType,sortFieldList);
    }
    return null;
  }
}
