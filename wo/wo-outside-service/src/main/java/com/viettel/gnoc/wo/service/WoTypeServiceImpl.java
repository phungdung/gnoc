package com.viettel.gnoc.wo.service;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.LanguageExchangeDTO;
import com.viettel.gnoc.commons.dto.UsersDTO;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.wfm.dto.WoTypeDTO;
import com.viettel.gnoc.wo.business.WoTypeBusiness;
import com.viettel.gnoc.wo.dto.WoTypeInsideDTO;
import com.viettel.gnoc.wo.dto.WoTypeTimeDTO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WoTypeServiceImpl implements WoTypeService {

  @Autowired
  protected WoTypeBusiness woTypeBusiness;

  @Resource
  WebServiceContext wsContext;

  @Override
  public List<WoTypeDTO> getListWoTypeByWoGroup(Long cdGroupId, String system, String locale) {
    List<WoTypeDTO> lstOutSide = new ArrayList<>();
    try {
      List<WoTypeInsideDTO> lstInside = woTypeBusiness
          .getListWoTypeByWoGroup(cdGroupId, system, locale);
      List<LanguageExchangeDTO> lstLanguage = woTypeBusiness
          .getLanguageExchangeWoType(Constants.LANGUAGUE_EXCHANGE_SYSTEM.WO,
              Constants.APPLIED_BUSSINESS.WO_TYPE);
      List<WoTypeInsideDTO> lstWoType = DataUtil
          .setLanguage(lstInside, lstLanguage, "woTypeId", "woTypeName");
      if (lstWoType != null && lstWoType.size() > 0) {
        for (WoTypeInsideDTO dto : lstWoType) {
          lstOutSide.add(dto.toWoTypeDTO());
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return null;
    }
    return lstOutSide;
  }

  @Override
  public List<WoTypeDTO> getListWoTypeByLocale(
      WoTypeDTO woTypeDTO, String locale) {
    WoTypeInsideDTO woTypeInsideDTO = woTypeDTO.toWoTypeInsideDTO();
    List<WoTypeInsideDTO> lstInside = woTypeBusiness.getListWoTypeByLocale(woTypeInsideDTO, locale);
    List<WoTypeDTO> lstOutSide = new ArrayList<>();
    for (WoTypeInsideDTO dto : lstInside) {
      lstOutSide.add(dto.toWoTypeDTO());
    }
    return lstOutSide;
  }

  @Override
  public List<WoTypeTimeDTO> getListWoTypeTimeDTO(
      com.viettel.gnoc.wo.dto.WoTypeTimeDTO woTypeTimeDTO) {
    return woTypeBusiness.getListWoTypeTimeDTO(woTypeTimeDTO);
  }

  @Override
  public WoTypeDTO findWoTypeById(Long woTypeDTOId) {
    WoTypeDTO woTypeDTO = null;
    if (woTypeDTOId != null && woTypeDTOId > 0) {
      WoTypeInsideDTO woTypeInsideDTO = woTypeBusiness.findWoTypeById(woTypeDTOId);
      if (woTypeInsideDTO != null) {
        woTypeDTO = woTypeInsideDTO.toWoTypeDTO();
        woTypeDTO.setDefaultSortField("name");
      }

    }

    return woTypeDTO;
  }

  @Override
  public List<WoTypeDTO> getListWoTypeDTO(WoTypeDTO woTypeDTO, int rowStart, int maxRow,
      String sortType, String sortFieldList) {
    I18n.setLocaleForService(wsContext);
    List<WoTypeDTO> lstOutSide = new ArrayList<>();
    if (woTypeDTO != null) {
      WoTypeInsideDTO woTypeInsideDTO = woTypeDTO.toWoTypeInsideDTO();
      List<WoTypeInsideDTO> listInside = woTypeBusiness.getListWoTypeIsEnable(woTypeInsideDTO);
      if (listInside != null && listInside.size() > 0) {
        for (WoTypeInsideDTO dto : listInside) {
          lstOutSide.add(dto.toWoTypeDTO());
        }
      }
    }
    return lstOutSide;
  }

  @Override
  public WoTypeDTO getWoTypeByCode(String woTypeCode) {
    if (!StringUtils.isNotNullOrEmpty(woTypeCode)) {
      return null;
    }
    WoTypeDTO woTypeDTO = null;
    WoTypeInsideDTO woTypeInsideDTO = woTypeBusiness.getWoTypeByCode(woTypeCode);
    if (woTypeInsideDTO != null) {
      woTypeDTO = woTypeInsideDTO.toWoTypeDTO();
      woTypeDTO.setDefaultSortField("name");
    }
    return woTypeDTO;
  }

  @Override
  public UsersDTO getUnitNameByUserName(String username) {
    return woTypeBusiness.getUnitNameByUserName(username);
  }
}
