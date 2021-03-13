package com.viettel.gnoc.business;


import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.LogChangeConfigDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.proxy.CommonStreamServiceProxy;
import com.viettel.gnoc.commons.proxy.CrServiceProxy;
import com.viettel.gnoc.commons.repository.CatItemRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.CfgFollowWorkTimeDTO;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.repository.CfgFollowWorkTimeRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import viettel.passport.client.UserToken;

@Service
@Transactional
@Slf4j
public class CfgFollowWorkTimeBusinessImpl implements CfgFollowWorkTimeBusiness {

  @Autowired
  CfgFollowWorkTimeRepository cfgFollowWorkTimeRepository;

  @Autowired
  CrServiceProxy crServiceProxy;

  @Autowired
  protected CatItemRepository catItemRepository;

  @Autowired
  TicketProvider ticketProvider;

  @Autowired
  protected CommonStreamServiceProxy commonStreamServiceProxy;

  @Override
  public Datatable getListCfgFollowWorkTimePage(CfgFollowWorkTimeDTO cfgFollowWorkTimeDTO) {
    log.debug("Request to getListConfigWorkTimeFollowPage : {}", cfgFollowWorkTimeDTO);
    return cfgFollowWorkTimeRepository.getListCfgFollowWorkTimePage(cfgFollowWorkTimeDTO);
  }

  @Override
  public ResultInSideDto insert(CfgFollowWorkTimeDTO cfgFollowWorkTimeDTO) {
    log.debug("Request to insert : {}", cfgFollowWorkTimeDTO);
    ResultInSideDto resultInSideDto = cfgFollowWorkTimeRepository
        .insertOrUpdate(cfgFollowWorkTimeDTO);
    if (resultInSideDto.getKey().equals(Constants.RESULT.SUCCESS)) {
      UserToken userToken = ticketProvider.getUserToken();
      String content = "";
      if (StringUtils.isStringNullOrEmpty(cfgFollowWorkTimeDTO.getConfigFollowWorkTimeId())) {
        content = "Insert cfgFollowWorkTimeDTO";
      }
      commonStreamServiceProxy.insertLog(new LogChangeConfigDTO(userToken.getUserName(),
          content, content + " " + resultInSideDto.getId(),
          cfgFollowWorkTimeDTO, null));
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto update(CfgFollowWorkTimeDTO cfgFollowWorkTimeDTO) {
    log.debug("Request to update : {}", cfgFollowWorkTimeDTO);
    if (!cfgFollowWorkTimeDTO.getSystemOLD().equals(cfgFollowWorkTimeDTO.getSystem())) {
      if ("WO".equals(cfgFollowWorkTimeDTO.getSystem())) {
        cfgFollowWorkTimeDTO.setCrType(null);
        cfgFollowWorkTimeDTO.setTtActionClass(null);
      } else if ("PT".equals(cfgFollowWorkTimeDTO.getSystem())) {
        cfgFollowWorkTimeDTO.setCrType(null);
        cfgFollowWorkTimeDTO.setTtActionClass(null);
      } else if ("TT".equals(cfgFollowWorkTimeDTO.getSystem())) {
        cfgFollowWorkTimeDTO.setCrType(null);
      } else if ("CR".equals(cfgFollowWorkTimeDTO.getSystem())) {
        cfgFollowWorkTimeDTO.setTtActionClass(null);
      }
    }
    ResultInSideDto resultInSideDto = cfgFollowWorkTimeRepository
        .insertOrUpdate(cfgFollowWorkTimeDTO);
    if (resultInSideDto.getKey().equals(Constants.RESULT.SUCCESS)) {
      UserToken userToken = ticketProvider.getUserToken();
      String content = "";
      if (!StringUtils.isStringNullOrEmpty(cfgFollowWorkTimeDTO.getConfigFollowWorkTimeId())) {
        content = "Update cfgFollowWorkTimeDTO";
      }
      commonStreamServiceProxy.insertLog(new LogChangeConfigDTO(userToken.getUserName(),
          content, content + " " + resultInSideDto.getId(),
          cfgFollowWorkTimeDTO, null));
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto delete(Long configFollowWorkTimeId) {
    log.debug("Request to delete : {}", configFollowWorkTimeId);
    ResultInSideDto resultInSideDto = cfgFollowWorkTimeRepository.delete(configFollowWorkTimeId);
    if (resultInSideDto.getKey().equals(Constants.RESULT.SUCCESS)) {
      UserToken userToken = ticketProvider.getUserToken();
      String content = "";
      if (!StringUtils.isStringNullOrEmpty(configFollowWorkTimeId)) {
        content = "Delete cfgFollowWorkTimeDTO";
      }
      commonStreamServiceProxy.insertLog(new LogChangeConfigDTO(userToken.getUserName(),
          content, content + " " + resultInSideDto.getId(),
          configFollowWorkTimeId, null));
    }
    return resultInSideDto;
  }

  @Override
  public CfgFollowWorkTimeDTO getDetail(Long configFollowWorkTimeId) {
    log.debug("Request to getDetail : {}", configFollowWorkTimeId);
    return cfgFollowWorkTimeRepository.getDetail(configFollowWorkTimeId);
  }

  @Override
  public List<CatItemDTO> getListItemByCategory(String systemCode) {
    //lay ra loai-nhom WO-TT-PT: "WO_GROUP_TYPE, PT_TYPE"
    //lay ra buoc thuc hien cho: TT-PT: "TT_STATE"
    List<CatItemDTO> catItemDTOList = new ArrayList<>();
    switch (systemCode) {
      case "CR":
        List<ItemDataCRInside> itemDataCRInsides = crServiceProxy.getListImpactSegmentCBB();
        for (ItemDataCRInside itemDataCRInside : itemDataCRInsides) {
          CatItemDTO catItemDTO = new CatItemDTO();
          catItemDTO.setItemId(itemDataCRInside.getValueStr());
          catItemDTO.setItemName(itemDataCRInside.getDisplayStr());
          catItemDTO.setItemValue(itemDataCRInside.getSecondValue());
          catItemDTOList.add(catItemDTO);
        }
        break;
      case "WO":
        catItemDTOList = catItemRepository.getListItemByCategory("WO_GROUP_TYPE", null);
        break;
      case "TT":
      case "PT":
        catItemDTOList = catItemRepository.getListItemByCategory("PT_TYPE", null);
        break;
      default:
        break;
    }
    return catItemDTOList;
  }

  @Override
  public List<CatItemDTO> getListItemByCat(String systemCode) {
    List<CatItemDTO> catItemDTOList = new ArrayList<>();
    switch (systemCode) {
      case "CR":
        Map<Long, String> crStateMap = Constants.CR_STATE.getGetStateName();
        for (Map.Entry<Long, String> temp : crStateMap.entrySet()) {
          CatItemDTO catItemDTO = new CatItemDTO();
          catItemDTO.setItemId(temp.getKey());
          catItemDTO.setItemName(I18n.getChangeManagement(temp.getValue()));
          catItemDTOList.add(catItemDTO);
        }
        break;
      case "WO":
        Map<Long, String> woStateMap = Constants.WO_STATE.getStateName();
        for (Map.Entry<Long, String> temp : woStateMap.entrySet()) {
          CatItemDTO catItemDTO = new CatItemDTO();
          catItemDTO.setItemId(temp.getKey());
          catItemDTO.setItemName(I18n.getLanguage(temp.getValue()));
          catItemDTOList.add(catItemDTO);
        }
        break;
      case "TT":
      case "PT":
        catItemDTOList = catItemRepository.getListItemByCategory("TT_STATE", null);
        break;
      default:
        break;
    }
    return catItemDTOList;
  }

}
