package com.viettel.gnoc.common.service;

import com.viettel.gnoc.commons.business.CatItemBusiness;
import com.viettel.gnoc.commons.business.CatLocationBusiness;
import com.viettel.gnoc.commons.business.CompCauseBusiness;
import com.viettel.gnoc.commons.business.UnitBusiness;
import com.viettel.gnoc.commons.business.UserBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.CompCauseDTO;
import com.viettel.gnoc.commons.dto.InfraCellServiceDetailDTO;
import com.viettel.gnoc.commons.dto.InfraDeviceDTO;
import com.viettel.gnoc.commons.dto.UserTokenGNOC;
import com.viettel.gnoc.commons.dto.UsersDTO;
import com.viettel.gnoc.commons.proxy.CrCategoryServiceProxy;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.ItemDataCR;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.ws.dto.CatItemDTO;
import com.viettel.gnoc.ws.dto.CfgChildArrayDTO;
import com.viettel.gnoc.ws.dto.UnitDTO;
import com.viettel.gnoc.ws.dto.WoFileTempDTO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.cxf.headers.Header;
import org.apache.cxf.jaxws.context.WrappedMessageContext;
import org.apache.cxf.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Element;

@Service
@Slf4j
public class VSmartWSImpl implements VSmartWS {

  @Resource
  WebServiceContext wsContext;

  @Autowired
  CompCauseBusiness compCauseBusiness;

  @Autowired
  CatItemBusiness catItemBusiness;

  @Autowired
  UnitBusiness unitBusiness;

  @Autowired
  UserBusiness userBusiness;

  @Autowired
  CatLocationBusiness catLocationBusiness;

  @Autowired
  CrCategoryServiceProxy crCategoryServiceProxy;

  @Override
  public List<CompCauseDTO> getComCauseList(Long serviceTypeId, List<Long> ccGroupId, Long parentId,
      Integer levelId, String lineType, Long cfgType, Boolean isEnable) throws Exception {
    log.info("Request to getComCauseList : {}", serviceTypeId, ccGroupId, parentId, levelId,
        lineType, cfgType, isEnable);
    I18n.setLocaleForService(wsContext);
    return compCauseBusiness.translateList(compCauseBusiness
        .getComCauseList(serviceTypeId, ccGroupId, parentId, levelId, lineType, cfgType,
            getNationCode(), isEnable), I18n.getLocale());
  }

  @Override
  public List<CatItemDTO> getListAction(Long serviceId, Long infraType) {
    log.info("Request to getListAction : {}", serviceId, infraType);
    I18n.setLocaleForService(wsContext);
    return catItemBusiness.translateListDTO(catItemBusiness
            .getListActionByCategory(Constants.CATEGORY_NAME.WO_ACTION_GROUP, serviceId, infraType),
        I18n.getLocale());
  }

  @Override
  public List<CatItemDTO> getListOverReason() {
    log.info("Request to getListOverReason : {}");
    I18n.setLocaleForService(wsContext);
    List<com.viettel.gnoc.commons.dto.CatItemDTO> lst = catItemBusiness
        .getListItemByCategory(Constants.CATEGORY_NAME.WO_MATERIAL_OVER_REASON, null);
    List<CatItemDTO> lstResult = new ArrayList<>();
    if (lst != null && !lst.isEmpty()) {
      for (com.viettel.gnoc.commons.dto.CatItemDTO catItemDTO : lst) {
        lstResult.add(catItemDTO.toOutsideDTO());
      }
    }
    return catItemBusiness.translateListDTO(lstResult, I18n.getLocale());
  }

  @Override
  public List<WoFileTempDTO> getListWoFileTempDTO(WoFileTempDTO woFileTempDTO, int rowStart,
      int maxRow, String sortType, String sortFieldList) {
    log.info("Request to getListWoFileTempDTO : {}", woFileTempDTO, rowStart, maxRow, sortType,
        sortFieldList);
    I18n.setLocaleForService(wsContext);
    if (woFileTempDTO != null) {
      return catItemBusiness
          .searchWoFileTempDTO(woFileTempDTO, rowStart, maxRow, sortType, sortFieldList);
    }
    return null;
  }

  @Override
  public List<CatItemDTO> getListItemByCategory(String categoryCode) {
    log.info("Request to getListItemByCategory : {}", categoryCode);
    I18n.setLocaleForService(wsContext);
    List<com.viettel.gnoc.commons.dto.CatItemDTO> lst = catItemBusiness
        .getListItemByCategory(categoryCode, null);
    List<CatItemDTO> lstResult = new ArrayList<>();
    if (lst != null && !lst.isEmpty()) {
      for (com.viettel.gnoc.commons.dto.CatItemDTO catItemDTO : lst) {
        lstResult.add(catItemDTO.toOutsideDTO());
      }
    }
    return catItemBusiness.translateListDTO(lstResult, I18n.getLocale());
  }

  @Override
  public List<InfraCellServiceDetailDTO> getListCellService(String cellCode, String cellType) {
    log.info("Request to getListCellService : {}", cellCode, cellType);
    I18n.setLocaleForService(wsContext);
    return catItemBusiness.getListCellService(cellCode, cellType);
  }

  @Override
  public List<CatItemDTO> getListItemByCategoryAndParent(String categoryCode, String parentId) {
    log.info("Request to getListItemByCategoryAndParent : {}", categoryCode, parentId);
    I18n.setLocaleForService(wsContext);
    List<com.viettel.gnoc.commons.dto.CatItemDTO> lst = catItemBusiness
        .getListItemByCategoryAndParent(categoryCode, parentId);
    List<CatItemDTO> lstResult = new ArrayList<>();
    if (lst != null && !lst.isEmpty()) {
      for (com.viettel.gnoc.commons.dto.CatItemDTO catItemDTO : lst) {
        lstResult.add(catItemDTO.toOutsideDTO());
      }
    }
    return catItemBusiness.translateListDTO(lstResult, I18n.getLocale());
  }

  @Override
  public List<UnitDTO> getListUnit(UnitDTO unitDTO, int rowStart, int maxRow) {
    log.info("Request to getListUnit : {}", unitDTO, rowStart, maxRow);
    I18n.setLocaleForService(wsContext);
    List<UnitDTO> lstResult = new ArrayList<>();
    if (unitDTO != null) {
      unitDTO.setRoleTypeName("FO");
      List<com.viettel.gnoc.commons.dto.UnitDTO> lst = unitBusiness
          .getListUnit(unitDTO.toInsideDTO());
      if (lst != null && !lst.isEmpty()) {
        for (com.viettel.gnoc.commons.dto.UnitDTO dto : lst) {
          lstResult.add(dto.toOutsideDTO());
        }
      }
    }
    return lstResult;
  }

  @Override
  public List<UnitDTO> getUnit(UnitDTO unitDTO, int rowStart, int maxRow) {
    log.info("Request to getUnit : {}", unitDTO, rowStart, maxRow);
    I18n.setLocaleForService(wsContext);
    if (unitDTO != null) {
      return unitBusiness.getUnit(unitDTO, rowStart, maxRow);
    }
    return null;
  }

  @Override
  public List<InfraDeviceDTO> getListInfraDeviceNonIp(InfraDeviceDTO infraDeviceDTO, int rowStart,
      int maxRow, String sortType, String sortFieldList) {
    log.info("Request to getListInfraDeviceNonIp : {}", infraDeviceDTO, rowStart, maxRow, sortType,
        sortFieldList);
    I18n.setLocaleForService(wsContext);
    if (infraDeviceDTO != null) {
      return catItemBusiness
          .searchNonIp(infraDeviceDTO, rowStart, maxRow, sortType, sortFieldList);
    }
    return null;
  }

  @Override
  public List<UsersDTO> getListUsersDTO(UsersDTO usersDTO, int rowStart, int maxRow,
      String sortType, String sortFieldList) {
    log.info("Request to getListUsersDTO : {}", usersDTO, rowStart, maxRow, sortType,
        sortFieldList);
    I18n.setLocaleForService(wsContext);
    return userBusiness.search(usersDTO, rowStart, maxRow, sortType, sortFieldList);
  }

  @Override
  public List<ItemDataCR> getListLocationByLevelCBB(Object form, Long level, Long parentId) {
    log.info("Request to getListLocationByLevelCBB : {}", form, level, parentId);
    I18n.setLocaleForService(wsContext);
    List<ItemDataCR> lstResult = new ArrayList<>();
    List<ItemDataCRInside> lst = catLocationBusiness
        .getListLocationByLevelCBB(form, level, parentId);
    if (lst != null && !lst.isEmpty()) {
      for (ItemDataCRInside dto : lst) {
        ItemDataCR dtoResult = new ItemDataCR();
        if (!StringUtils.isLongNullOrEmpty(dto.getValueStr())) {
          dtoResult.setValueStr(dto.getValueStr().toString());
        }
        dtoResult.setDisplayStr(dto.getDisplayStr());
        dtoResult.setSecondValue(dto.getSecondValue());
        dtoResult.setThirdValue(dto.getThirdValue());
        lstResult.add(dtoResult);
      }
    }
    return lstResult;
  }

  @Override
  public List<CfgChildArrayDTO> getCbbChildArray(CfgChildArrayDTO dto) {
    log.info("Request to getCbbChildArray : {}", dto);
    I18n.setLocaleForService(wsContext);
    List<CfgChildArrayDTO> lstResult = new ArrayList<>();
    com.viettel.gnoc.cr.dto.CfgChildArrayDTO dtoSearch = dto.toInsideDTO();
    dtoSearch.setProxyLocale(I18n.getLocale());
    List<com.viettel.gnoc.cr.dto.CfgChildArrayDTO> lst = crCategoryServiceProxy
        .getCbbChildArray(dtoSearch);
    if (lst != null && !lst.isEmpty()) {
      for (com.viettel.gnoc.cr.dto.CfgChildArrayDTO cfgChildArrayDTO : lst) {
        lstResult.add(cfgChildArrayDTO.toOutside());
      }
    }
    return lstResult;
  }

  @Override
  public UserTokenGNOC getUserInfor(String userName) {
    log.info("Request to getUserInfor : {}", userName);
    I18n.setLocaleForService(wsContext);
    return userBusiness.getUserInfor(userName);
  }

  private String getNationCode() {
    String nationCode = null;
    try {

      MessageContext mcc = wsContext.getMessageContext();
      Message message = ((WrappedMessageContext) mcc).getWrappedMessage();
      List<Header> headers = (List<Header>) message.get(Header.HEADER_LIST);
      for (Header h : headers) {
        Element e = (Element) h.getObject();

        if (e != null && "nationCode".equalsIgnoreCase(e.getNodeName())) {
          nationCode = e.getTextContent();
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return null;
    }
    return nationCode;
  }
}
