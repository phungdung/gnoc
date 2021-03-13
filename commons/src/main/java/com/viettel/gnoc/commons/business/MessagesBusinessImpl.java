package com.viettel.gnoc.commons.business;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TimezoneContextHolder;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.MessagesDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.RoleUserDTO;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.commons.model.UsersEntity;
import com.viettel.gnoc.commons.repository.CatItemRepository;
import com.viettel.gnoc.commons.repository.MessagesRepository;
import com.viettel.gnoc.commons.repository.TransitionStateConfigRepository;
import com.viettel.gnoc.commons.repository.UnitRepository;
import com.viettel.gnoc.commons.repository.UserRepository;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.CATEGORY;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.DateUtil;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.incident.dto.TroublesInSideDTO;
import com.viettel.gnoc.kedb.dto.UserSmsDTO;
import com.viettel.gnoc.pt.dto.ProblemsInsideDTO;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author TungPV
 */
@Service
@Transactional
@Slf4j
public class MessagesBusinessImpl implements MessagesBusiness {

  @Autowired
  protected MessagesRepository messagesRepository;

  @Autowired
  protected CatItemBusiness catItemBusiness;

  @Autowired
  UnitBusiness unitBusiness;

  @Autowired
  private UserBusiness userBusiness;

  @Autowired
  private RoleUserBusiness roleUserBusiness;

  @Autowired
  CatItemRepository catItemRepository;

  @Autowired
  UserRepository userRepository;

  @Autowired
  UnitRepository unitRepository;

  @Autowired
  TransitionStateConfigRepository transitionStateConfigRepository;

  @Override
  public ResultInSideDto insertOrUpdateCommon(MessagesDTO odDTO) {
    log.debug("Request to search insertOrUpdate : {}");
    return messagesRepository.insertOrUpdateCommon(odDTO);
  }

  @Override
  public ResultInSideDto insertOrUpdateWfm(MessagesDTO odDTO) {
    log.debug("Request to search insertOrUpdate : {}");
    return messagesRepository.insertOrUpdateWfm(odDTO);
  }

  @Override
  public ResultInSideDto insertSMSMessageForPm(String content, String pmGroup,
      ProblemsInsideDTO problemsInsideDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    try {
      List<MessagesDTO> lsMessagesDTOs = new ArrayList<>();
      List<UsersInsideDto> lstUser = getListUserByRolePm(pmGroup);
      List<Long> lstUnitId = new ArrayList<>();
      for (UsersInsideDto usersInsideDTO : lstUser) {
        if (usersInsideDTO.getUnitId() == null || lstUnitId.contains(usersInsideDTO.getUnitId())) {
          continue;
        }
        lstUnitId.add(usersInsideDTO.getUnitId());
      }

      Map<String, String> mapUnit = new HashMap<String, String>();
      if (lstUnitId != null && lstUnitId.size() > 0) {
        List<UnitDTO> lstUnit = unitBusiness.getListUnitDTOByListUnitId(lstUnitId);
        if (lstUnit != null) {
          for (UnitDTO udto : lstUnit) {
            mapUnit.put(String.valueOf(udto.getUnitId()),
                udto.getSmsGatewayId() == null ? null : String.valueOf(udto.getSmsGatewayId()));
          }
        }
      }
      String alias = getAlias(null, problemsInsideDTO);

      if (lstUser != null && !lstUser.isEmpty()) {
        for (UsersInsideDto usersInsideDTO : lstUser) {
          if (usersInsideDTO != null && (usersInsideDTO.getIsNotReceiveMessage() == null || !"1"
              .equals(String.valueOf(usersInsideDTO.getIsNotReceiveMessage())))) {
            MessagesDTO messagesDTO = new MessagesDTO();
            String smsContent = getLang(
                "2".equals(usersInsideDTO.getUserLanguage()) ? new Locale("en_US") : null, content);
            if (smsContent.contains("#pDFDeferredduetime#")) {
              Double offset = TimezoneContextHolder.getOffsetDouble();
              Date deferreTime = new Date(
                  problemsInsideDTO.getDeferredTime().getTime()
                      + offset.longValue() * 60 * 60 * 1000);
              String timeDeferre = DateTimeUtils.convertDateTimeStampToString(deferreTime);
              smsContent = smsContent
                  .replaceAll("#problemCode#", problemsInsideDTO.getProblemCode())
                  .replaceAll("#pDFDeferredduetime#", timeDeferre);
            } else {
              smsContent = smsContent
                  .replaceAll("#problemCode#", problemsInsideDTO.getProblemCode())
                  .replaceAll("#problemName#", problemsInsideDTO.getProblemName());
            }
            messagesDTO.setContent(smsContent);
            messagesDTO.setCreateTime(DateUtil.date2ddMMyyyyHHMMss(new Date()));
            messagesDTO.setReceiverId(String.valueOf(usersInsideDTO.getUserId()));
            messagesDTO.setReceiverUsername(usersInsideDTO.getUsername());
            messagesDTO.setReceiverPhone(usersInsideDTO.getMobile());
            messagesDTO.setSmsGatewayId(mapUnit.get(String.valueOf(usersInsideDTO.getUnitId())));
            messagesDTO.setStatus("0");
            messagesDTO.setAlias(alias);
            lsMessagesDTOs.add(messagesDTO);
          }
        }
      }
      if (lsMessagesDTOs != null && !lsMessagesDTOs.isEmpty()) {
        //tuanpv_fix sms_gateway_id null
        for (int i = lsMessagesDTOs.size() - 1; i >= 0; i--) {
          if ("".equals(lsMessagesDTOs.get(i).getSmsGatewayId())
              || lsMessagesDTOs.get(i).getSmsGatewayId() == null) {
            lsMessagesDTOs.remove(i);
          }
        }
        //tuanpv_fix sms_gateway_id null
        resultInSideDto = messagesRepository.insertOrUpdateListMessagesCommon(lsMessagesDTOs);
      }

    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto insertSMSMessageKEDBInst(String content, List<UserSmsDTO> lstUserSms,
      String smsAdd) {
    ResultInSideDto resultInSideDto = new ResultInSideDto(null, RESULT.SUCCESS, RESULT.SUCCESS);
    List<MessagesDTO> listMessagesDTO = new ArrayList<>();
    List<Long> listUnitId = new ArrayList<>();
    String temp = Constants.KEDB_ALIAS;
    String alias = "";
    List<CatItemDTO> listCatItem = catItemRepository
        .getListItemByCategory(CATEGORY.GNOC_ALIAS, null);
    if (listCatItem != null && !listCatItem.isEmpty()) {
      for (CatItemDTO catItemDTO : listCatItem) {
        if (temp.equals(catItemDTO.getItemCode())) {
          alias = catItemDTO.getItemCode();
          break;
        }
      }
    }

    if (lstUserSms != null && !lstUserSms.isEmpty()) {
      for (UserSmsDTO userSmsDTO : lstUserSms) {
        UsersEntity usersEntity = userRepository.getUserByUserId(userSmsDTO.getUserId());
        if (usersEntity == null) {//skip nhung user khong ton tai trong DB
          userSmsDTO.setUserId(null);
          continue;
        }
        if (usersEntity.getUnitId() == null || listUnitId.contains(usersEntity.getUnitId())) {
          continue;
        }
        UsersInsideDto usersInsideDto = usersEntity.toDTO();
        userSmsDTO.setUnitId(usersInsideDto.getUnitId());
        userSmsDTO.setUserName(usersInsideDto.getUsername());
        userSmsDTO.setMobile(usersInsideDto.getMobile());
        userSmsDTO.setTypeCode(usersInsideDto.getUserLanguage());
        listUnitId.add(usersInsideDto.getUnitId());
      }

      Map<Long, Long> mapUnit = new HashMap<>();
      if (listUnitId != null && listUnitId.size() > 0) {
        List<UnitDTO> listUnit = unitRepository.getListUnitDTOByListUnitId(listUnitId);
        if (listUnit != null) {
          for (UnitDTO unitDTO : listUnit) {
            mapUnit.put(unitDTO.getUnitId(), unitDTO.getSmsGatewayId());
          }
        }
      }
      for (UserSmsDTO userSmsDTO : lstUserSms) {
        if (StringUtils.isStringNullOrEmpty(userSmsDTO.getUserId())) {
          continue;
        }
        String smsGateWay = (mapUnit.get(userSmsDTO.getUnitId()) == null) ? null
            : String.valueOf(mapUnit.get(userSmsDTO.getUnitId()));
        MessagesDTO messagesDTO = new MessagesDTO();
        String smscontent = content + I18n.getLanguage(smsAdd);
        messagesDTO.setContent(smscontent);
        messagesDTO.setCreateTime(DateUtil.date2ddMMyyyyHHMMss(new Date()));
        messagesDTO.setReceiverId(String.valueOf(userSmsDTO.getUserId()));
        messagesDTO.setReceiverUsername(userSmsDTO.getUserName());
        messagesDTO.setReceiverPhone(userSmsDTO.getMobile());
        messagesDTO.setSmsGatewayId(smsGateWay);
        messagesDTO.setStatus("0");
        messagesDTO.setAlias(alias);
        listMessagesDTO.add(messagesDTO);
      }
    }
    if (listMessagesDTO != null && !listMessagesDTO.isEmpty()) {
      for (int i = listMessagesDTO.size() - 1; i >= 0; i--) {
        if ("".equals(listMessagesDTO.get(i).getSmsGatewayId())
            || listMessagesDTO.get(i).getSmsGatewayId() == null) {
          listMessagesDTO.remove(i);
        }
      }
      resultInSideDto = messagesRepository.insertOrUpdateListMessagesCommon(listMessagesDTO);
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto insertMessageForUser(String content, String alias,
      List<UsersInsideDto> lst) {
    ResultInSideDto resultInSideDto = new ResultInSideDto(null, RESULT.SUCCESS, RESULT.SUCCESS);
    List<MessagesDTO> lsMessagesDTOs = new ArrayList<>();
    if (lst != null && !lst.isEmpty()) {
      List<Long> listUnitId = new ArrayList<>();
      Map<Long, Long> mapUnit = new HashMap<>();
      for (UsersInsideDto usersInsideDto : lst) {
        if (usersInsideDto.getUnitId() == null || listUnitId.contains(usersInsideDto.getUnitId())) {
          continue;
        }
        listUnitId.add(usersInsideDto.getUnitId());
      }
      if (listUnitId != null && listUnitId.size() > 0) {
        List<UnitDTO> listUnit = unitRepository.getListUnitDTOByListUnitId(
            listUnitId);//cai nay chi co 1 unit, khong lo bi vuot nguong 1000
        if (listUnit != null) {
          for (UnitDTO unitDTO : listUnit) {
            mapUnit.put(unitDTO.getUnitId(), unitDTO.getSmsGatewayId());
          }
        }
      }

      for (UsersInsideDto usersInsideDto : lst) {
        String smsGateWay = (mapUnit.get(usersInsideDto.getUnitId()) == null) ? null
            : String.valueOf(mapUnit.get(usersInsideDto.getUnitId()));
        usersInsideDto.setSmsGatewayId(smsGateWay);
        if ((usersInsideDto.getIsNotReceiveMessage() == null || !"1"
            .equals(String.valueOf(usersInsideDto.getIsNotReceiveMessage())))) {
          MessagesDTO messagesDTO = new MessagesDTO();
          messagesDTO.setContent(content);
          messagesDTO.setCreateTime(DateUtil.date2ddMMyyyyHHMMss(new Date()));
          messagesDTO.setReceiverId(String.valueOf(usersInsideDto.getUserId()));
          messagesDTO.setReceiverUsername(usersInsideDto.getUsername());
          messagesDTO.setReceiverPhone(usersInsideDto.getMobile());
          messagesDTO.setAlias(alias);
          messagesDTO.setSmsGatewayId(usersInsideDto.getSmsGatewayId());
          messagesDTO.setStatus("0");
          lsMessagesDTOs.add(messagesDTO);
        }
      }
    }
    if (lsMessagesDTOs != null && !lsMessagesDTOs.isEmpty()) {
      for (int i = lsMessagesDTOs.size() - 1; i >= 0; i--) {
        if ("".equals(lsMessagesDTOs.get(i).getSmsGatewayId())
            || lsMessagesDTOs.get(i).getSmsGatewayId() == null) {
          lsMessagesDTOs.remove(i);
        }
      }
      resultInSideDto = messagesRepository.insertOrUpdateListMessagesCommon(lsMessagesDTOs);
    }
    return resultInSideDto;
  }

  @Override
  public void insertSMSMessageForUser(String content, UsersInsideDto usersInsideDTO,
      ProblemsInsideDTO problemsInsideDTO) {
    try {
      List<MessagesDTO> lsMessagesDTOs = new ArrayList<>();
      List<ConditionBean> lstCondition = new ArrayList<>();
      lstCondition.add(
          new ConditionBean("unitId", String.valueOf(usersInsideDTO.getUnitId()),
              Constants.NAME_EQUAL,
              Constants.NUMBER));
      lstCondition.add(new ConditionBean("status", "1", Constants.NAME_EQUAL, Constants.NUMBER));
      List<UnitDTO> lstUnit = unitBusiness
          .getListUnitByCondition(lstCondition, 0, 1000, "asc", "unitName");

      String alias = getAlias(null, problemsInsideDTO);
      //R546092_QuangDX_Start
      if ((usersInsideDTO.getIsNotReceiveMessage() == null || 1L != usersInsideDTO
          .getIsNotReceiveMessage())) {
        MessagesDTO messagesDTO = new MessagesDTO();
        String smsContent = getLang(
            "2".equals(usersInsideDTO.getUserLanguage()) ? new Locale("en_US") : null, content);
        if (smsContent.contains("#pDFDeferredduetime#")) {
          Double offsetResult = TimezoneContextHolder.getOffsetDouble();
          Long offset = (offsetResult == null) ? 0 : offsetResult.longValue();
          Date deferreTime = new Date(
              problemsInsideDTO.getDeferredTime().getTime() + offset * 60 * 60 * 1000);
          String timeDeferre = DateTimeUtils.convertDateTimeStampToString(deferreTime);
          smsContent = smsContent.replaceAll("#problemCode#", problemsInsideDTO.getProblemCode())
              .replaceAll("#pDFDeferredduetime#", timeDeferre);
        } else {
          smsContent = smsContent.replaceAll("#problemCode#", problemsInsideDTO.getProblemCode())
              .replaceAll("#problemName#", problemsInsideDTO.getProblemName());

        }
        messagesDTO.setContent(smsContent);
        messagesDTO.setCreateTime(DateUtil.date2ddMMyyyyHHMMss(new Date()));
        messagesDTO.setReceiverId(
            (usersInsideDTO.getUserId() == null) ? null
                : String.valueOf(usersInsideDTO.getUserId()));
        messagesDTO.setReceiverUsername(usersInsideDTO.getUsername());
        messagesDTO.setReceiverPhone(usersInsideDTO.getMobile());
        messagesDTO.setAlias(alias);

        if (lstUnit != null && !lstUnit.isEmpty()) {
          messagesDTO.setSmsGatewayId(String.valueOf(lstUnit.get(0).getSmsGatewayId()));
        }
        messagesDTO.setStatus("0");
        lsMessagesDTOs.add(messagesDTO);
      }
      if (!lsMessagesDTOs.isEmpty()) {
        //tuanpv_fix sms_gateway_id null
        for (int i = lsMessagesDTOs.size() - 1; i >= 0; i--) {
          if (lsMessagesDTOs.get(i).getSmsGatewayId() == null || ""
              .equals(lsMessagesDTOs.get(i).getSmsGatewayId()) || "null"
              .equalsIgnoreCase(lsMessagesDTOs.get(i).getSmsGatewayId())) {
            lsMessagesDTOs.remove(i);
          }
        }
        messagesRepository.insertOrUpdateListMessagesCommon(lsMessagesDTOs);
      }

    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

  @Override
  public void insertSMSMessageForUnitAllUser(String content, String unitId,
      TroublesInSideDTO troublesDTO,
      ProblemsInsideDTO problemsInsideDTO) {
    try {
      List<MessagesDTO> lsMessagesDTOs = new ArrayList<>();
      List<ConditionBean> lstCondition = new ArrayList<>();
      lstCondition.add(new ConditionBean("unitId", unitId, Constants.NAME_EQUAL, Constants.NUMBER));
      lstCondition.add(new ConditionBean("isEnable", "1", Constants.NAME_EQUAL, Constants.NUMBER));
      List<UsersInsideDto> lstUser = userBusiness
          .getListUsersByCondition(lstCondition, 0, 1000, "asc", "username");
      lstCondition = new ArrayList<>();
      lstCondition.add(new ConditionBean("unitId", unitId, Constants.NAME_EQUAL, Constants.NUMBER));
      lstCondition.add(new ConditionBean("status", "1", Constants.NAME_EQUAL, Constants.NUMBER));
      List<UnitDTO> lstUnit = unitBusiness
          .getListUnitByCondition(lstCondition, 0, 1000, "asc", "unitName");

      String alias = getAlias(troublesDTO, problemsInsideDTO);

      if (lstUser != null && lstUser.size() > 0) {
        for (UsersInsideDto usersInsideDTO : lstUser) {
          //R546092_QuangDX_Start
          if (usersInsideDTO != null && (usersInsideDTO.getIsNotReceiveMessage() == null || !"1"
              .equals(String.valueOf(usersInsideDTO.getIsNotReceiveMessage())))) {
            MessagesDTO messagesDTO = new MessagesDTO();
            String smsContent = getLang(
                "2".equals(usersInsideDTO.getUserLanguage()) ? new Locale("en_US") : null, content);
            if (problemsInsideDTO != null) {
              if (smsContent.contains("#pDFDeferredduetime#")) {

                Double offsetResult = TimezoneContextHolder.getOffsetDouble();
                Long offset = (offsetResult == null) ? 0 : offsetResult.longValue();
                Date deferreTime = new Date(
                    problemsInsideDTO.getDeferredTime().getTime() + offset * 60 * 60 * 1000);
                String timeDeferre = DateTimeUtils.convertDateTimeStampToString(deferreTime);
                smsContent = smsContent
                    .replaceAll("#problemCode#", problemsInsideDTO.getProblemCode())
                    .replaceAll("#pDFDeferredduetime#", timeDeferre);
              } else {
                smsContent = smsContent
                    .replaceAll("#problemCode#", problemsInsideDTO.getProblemCode());
              }
            } else if (troublesDTO != null) {
              Double offsetResult = TimezoneContextHolder.getOffsetDouble();
              Long offset = (offsetResult == null) ? 0 : offsetResult.longValue();
              Date updateTime = new Date(new Date().getTime() + offset * 60 * 60 * 1000);
              String timeUpdate = DateTimeUtils.convertDateTimeStampToString(updateTime);
              smsContent = smsContent.replaceAll("#troubleCode#", troublesDTO.getTroubleCode())
                  .replaceAll("#troubleName#", troublesDTO.getTroubleName())
                  .replaceAll("#updateTime#", timeUpdate)
                  .replaceAll("#createUnit#", troublesDTO.getCreateUnitName())
                  .replaceAll("#receiveUnit#", troublesDTO.getReceiveUnitName())
                  .replaceAll("#currentUser#", troublesDTO.getUnitMoveName())
                  .replaceAll("#createUser#",
                      troublesDTO.getCreateUserName() != null ? troublesDTO.getCreateUserName()
                          : troublesDTO.getCreateUnitName())
                  .replaceAll("#priority#", troublesDTO.getPriorityName())
                  .replaceAll("#unitMoveName#", troublesDTO.getReceiveUnitName() == null ? ""
                      : troublesDTO.getReceiveUnitName())
                  .replaceAll("#stateName#", troublesDTO.getStateName());
            }
            messagesDTO.setContent(smsContent);

            messagesDTO.setCreateTime(DateUtil.date2ddMMyyyyHHMMss(new Date()));
            messagesDTO
                .setReceiverId(StringUtils.isStringNullOrEmpty(usersInsideDTO.getUserId()) ? null
                    : String.valueOf(usersInsideDTO.getUserId()));
            messagesDTO.setReceiverUsername(usersInsideDTO.getUsername());
            messagesDTO.setReceiverPhone(usersInsideDTO.getMobile());
            messagesDTO.setAlias(alias);
            if (lstUnit != null && !lstUnit.isEmpty()) {
              messagesDTO.setSmsGatewayId(String.valueOf(lstUnit.get(0).getSmsGatewayId()));
            }
            messagesDTO.setStatus("0");
            lsMessagesDTOs.add(messagesDTO);
          }
        }
      }
      if (lsMessagesDTOs != null && !lsMessagesDTOs.isEmpty()) {
        //tuanpv_fix sms_gateway_id null
        for (int i = lsMessagesDTOs.size() - 1; i >= 0; i--) {
          if (lsMessagesDTOs.get(i).getSmsGatewayId() == null || ""
              .equals(lsMessagesDTOs.get(i).getSmsGatewayId()) || "null"
              .equalsIgnoreCase(lsMessagesDTOs.get(i).getSmsGatewayId())) {
            lsMessagesDTOs.remove(i);
          }
        }
        //tuanpv_fix sms_gateway_id null
        messagesRepository.insertOrUpdateListMessagesCommon(lsMessagesDTOs);
      }

    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

  private List<UsersInsideDto> getListUserByRolePm(String pmGroup) {
    List<UsersInsideDto> lstUsers = new ArrayList();
    String listUserId = "";
    List<ConditionBean> lstCondition = new ArrayList<ConditionBean>();
    ConditionBean conditionUser = new ConditionBean("roleId", pmGroup, Constants.NAME_EQUAL,
        Constants.NUMBER);
    lstCondition.add(conditionUser);
    try {
      List<RoleUserDTO> lstRoleUser = roleUserBusiness
          .getListRoleUserByCondition(lstCondition, 0, 0, "asc", "roleId");
      if (lstRoleUser != null && !lstRoleUser.isEmpty()) {
        for (RoleUserDTO roleUserDTOs : lstRoleUser) {
          listUserId = listUserId + roleUserDTOs.getUserId() + ",";
        }
      }
      if (listUserId.endsWith(",")) {
        listUserId = listUserId.substring(0, listUserId.length() - 1);
      }
      List<ConditionBean> lstCondition1 = new ArrayList<ConditionBean>();
      ConditionBean conditionUser1 = new ConditionBean("userId", listUserId, Constants.NAME_IN,
          Constants.NUMBER);
      lstCondition1.add(conditionUser1);
      lstUsers = userBusiness.getListUsersByCondition(lstCondition1, 0, 1000, "", "");

    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return lstUsers;
  }

  private String getAlias(TroublesInSideDTO troublesDTO, ProblemsInsideDTO problemsInsideDTO)
      throws Exception {
    String alias = "";
    String temp = "";
    if (problemsInsideDTO != null) {//nhan tin pt
      temp = Constants.PT_ALIAS;
    } else if (troublesDTO != null && troublesDTO.getTypeId() != null
        && troublesDTO.getAlarmGroupCode() != null) { //nhan tin tt
      CatItemDTO catItemDTO = catItemBusiness.getCatItemById(Long.valueOf(troublesDTO.getTypeId()));
      temp = "TT_" + catItemDTO.getItemCode() + "_" + troublesDTO
          .getAlarmGroupCode(); //TT_DD_VT_ACCESS_LOI_TRAM
    }

    List<CatItemDTO> lstCatItem = catItemBusiness.getListItemByCategory(Constants.GNOC_ALIAS, null);
    if (lstCatItem != null && !lstCatItem.isEmpty()) {
      for (CatItemDTO catItemDTO : lstCatItem) {
        if (temp.equals(catItemDTO.getItemCode())) {
          alias = catItemDTO.getItemValue();
          break;
        }
      }
    }
    return alias;
  }

  public static String getLang(Locale locale, String key) {
    if (locale != null) {
      return I18n.getLanguageByLocale(locale, key);
    } else {
      return I18n.getLanguageByLocale(new Locale("vi", "VN"), key);
    }
  }

  @Override
  public void insertMessageForUserCR(String content, UsersEntity usersEntity) {
    List<MessagesDTO> lsMessagesDTOs = new ArrayList<>();
    List<ConditionBean> lstCondition = new ArrayList<>();
    lstCondition.add(
        new ConditionBean("unitId", String.valueOf(usersEntity.getUnitId()), Constants.NAME_EQUAL,
            Constants.NUMBER));
    lstCondition.add(new ConditionBean("status", "1", Constants.NAME_EQUAL, Constants.NUMBER));
    List<UnitDTO> lstUnit = unitBusiness
        .getListUnitByCondition(lstCondition, 0, 1, "asc", "unitName");
    MessagesDTO messagesDTO = new MessagesDTO();
    messagesDTO.setContent(content);
    messagesDTO.setCreateTime(DateUtil.date2ddMMyyyyHHMMss(new Date()));
    messagesDTO.setReceiverId(String.valueOf(usersEntity.getUserId()));
    messagesDTO.setReceiverUsername(usersEntity.getUsername());
    messagesDTO.setReceiverPhone(usersEntity.getMobile());
    if (lstUnit != null && !lstUnit.isEmpty()) {
      messagesDTO.setSmsGatewayId((lstUnit.get(0).getSmsGatewayId() == null) ? null
          : String.valueOf(lstUnit.get(0).getSmsGatewayId()));
    }
    messagesDTO.setStatus("0");
    List<CatItemDTO> lst = catItemBusiness.getListItemByCategory(Constants.GNOC_ALIAS, null);
    if (lst != null && !lst.isEmpty()) {
      for (CatItemDTO itemDTO : lst) {
        if ("CR_ALIAS".equalsIgnoreCase(itemDTO.getItemCode())) {
          messagesDTO.setAlias(itemDTO.getItemValue());
          break;
        }
      }
    }

    lsMessagesDTOs.add(messagesDTO);

    if (!lsMessagesDTOs.isEmpty()) {
      for (int i = lsMessagesDTOs.size() - 1; i >= 0; i--) {
        if (StringUtils.isStringNullOrEmpty(lsMessagesDTOs.get(i).getSmsGatewayId())) {
          lsMessagesDTOs.remove(i);
        }
      }
      messagesRepository.insertOrUpdateListMessagesCommon(lsMessagesDTOs);
    }
  }

  @Override
  public String insertOrUpdateListMessages(List<MessagesDTO> messagesDTO) {
    log.info("Request to insertOrUpdateListMessages : {}", messagesDTO);
    return messagesRepository.insertList(messagesDTO);
  }

  @Override
  public String insertOrUpdateListMessagesWS(List<MessagesDTO> lstMsg) {
    log.info("Request to insertOrUpdateListMessagesWS : {}", lstMsg);
    return messagesRepository.insertListWS(lstMsg);
  }

  @Override
  public void insertSMSMessageForUserForPT(String content, UsersInsideDto usersInsideDTO,
      ProblemsInsideDTO problemsInsideDTO) {
    try {
      List<MessagesDTO> lsMessagesDTOs = new ArrayList<>();
      List<ConditionBean> lstCondition = new ArrayList<>();
      lstCondition.add(
          new ConditionBean("unitId", String.valueOf(usersInsideDTO.getUnitId()),
              Constants.NAME_EQUAL,
              Constants.NUMBER));
      lstCondition.add(new ConditionBean("status", "1", Constants.NAME_EQUAL, Constants.NUMBER));
      List<UnitDTO> lstUnit = unitBusiness
          .getListUnitByCondition(lstCondition, 0, 1000, "asc", "unitName");
      String nameStatus = null;
      List<CatItemDTO> list = transitionStateConfigRepository.getListState(3L);
      for (CatItemDTO catItemDTO : list) {
        if (problemsInsideDTO.getProblemState().equals(catItemDTO.getItemId().toString())) {
          nameStatus = catItemDTO.getItemName();
          break;
        }
      }
      String alias = getAlias(null, problemsInsideDTO);
      //R546092_QuangDX_Start
      if ((usersInsideDTO.getIsNotReceiveMessage() == null || 1L != usersInsideDTO
          .getIsNotReceiveMessage())) {
        MessagesDTO messagesDTO = new MessagesDTO();
        String smsContent = getLang(
            "2".equals(usersInsideDTO.getUserLanguage()) ? new Locale("en_US") : null, content);
        if (smsContent.contains("#pDFDeferredduetime#")) {
          Double offsetResult = TimezoneContextHolder.getOffsetDouble();
          Long offset = (offsetResult == null) ? 0 : offsetResult.longValue();
          Date deferreTime = new Date(
              problemsInsideDTO.getDeferredTime().getTime() + offset * 60 * 60 * 1000);
          String timeDeferre = DateTimeUtils.convertDateTimeStampToString(deferreTime);
          smsContent = smsContent.replaceAll("#problemCode#", problemsInsideDTO.getProblemCode())
              .replaceAll("#pDFDeferredduetime#", timeDeferre);
        } else {
          smsContent = smsContent.replaceAll("#problemCode#", problemsInsideDTO.getProblemCode())
              .replaceAll("#user#", usersInsideDTO.getUsername())
              .replaceAll("#problemName#", problemsInsideDTO.getProblemName())
              .replaceAll("#status#", nameStatus);

        }
        messagesDTO.setContent(smsContent);
        messagesDTO.setCreateTime(DateUtil.date2ddMMyyyyHHMMss(new Date()));
        messagesDTO.setReceiverId(
            (usersInsideDTO.getUserId() == null) ? null
                : String.valueOf(usersInsideDTO.getUserId()));
        messagesDTO.setReceiverUsername(usersInsideDTO.getUsername());
        messagesDTO.setReceiverPhone(usersInsideDTO.getMobile());
        messagesDTO.setAlias(alias);

        if (lstUnit != null && !lstUnit.isEmpty()) {
          messagesDTO.setSmsGatewayId(String.valueOf(lstUnit.get(0).getSmsGatewayId()));
        }
        messagesDTO.setStatus("0");
        lsMessagesDTOs.add(messagesDTO);
      }
      if (!lsMessagesDTOs.isEmpty()) {
        //tuanpv_fix sms_gateway_id null
        for (int i = lsMessagesDTOs.size() - 1; i >= 0; i--) {
          if (lsMessagesDTOs.get(i).getSmsGatewayId() == null || ""
              .equals(lsMessagesDTOs.get(i).getSmsGatewayId())) {
            lsMessagesDTOs.remove(i);
          }
        }
        messagesRepository.insertOrUpdateListMessagesCommon(lsMessagesDTOs);
      }

    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

}
