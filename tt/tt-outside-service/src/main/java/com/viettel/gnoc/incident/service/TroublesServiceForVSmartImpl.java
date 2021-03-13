package com.viettel.gnoc.incident.service;

import com.viettel.gnoc.commons.business.ActionLogBusiness;
import com.viettel.gnoc.commons.business.CatItemBusiness;
import com.viettel.gnoc.commons.business.CommonBusiness;
import com.viettel.gnoc.commons.business.MessagesBusiness;
import com.viettel.gnoc.commons.business.UnitBusiness;
import com.viettel.gnoc.commons.business.UserBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.JsonResponseBO;
import com.viettel.gnoc.commons.dto.MessagesDTO;
import com.viettel.gnoc.commons.dto.ObjectSearchDto;
import com.viettel.gnoc.commons.dto.RequestInputBO;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.ResultTroubleDTO;
import com.viettel.gnoc.commons.dto.TreeDTO;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.commons.proxy.TtCategoryServiceProxy;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.APPLIED_BUSSINESS;
import com.viettel.gnoc.commons.utils.Constants.LANGUAGUE_EXCHANGE_SYSTEM;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.Constants.TT_MASTER_CODE;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.DateUtil;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.incident.business.TroublesBusiness;
import com.viettel.gnoc.incident.dto.ItemDataTT;
import com.viettel.gnoc.incident.dto.TTCfgBusinessDTO;
import com.viettel.gnoc.incident.dto.TTChangeStatusDTO;
import com.viettel.gnoc.incident.dto.TTChangeStatusRoleDTO;
import com.viettel.gnoc.incident.dto.TroublesDTO;
import com.viettel.gnoc.incident.dto.TroublesInSideDTO;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TroublesServiceForVSmartImpl implements TroublesServiceForVSmart {

  @Resource
  private WebServiceContext wsContext;

  @Autowired
  private TroublesBusiness troublesBusiness;

  @Autowired
  TtCategoryServiceProxy ttCategoryServiceProxy;

  @Autowired
  CatItemBusiness catItemBusiness;

  @Autowired
  CommonBusiness commonBusiness;

  @Autowired
  ActionLogBusiness actionLogBusiness;

  @Autowired
  UserBusiness userBusiness;

  @Autowired
  UnitBusiness unitBusiness;

  @Autowired
  MessagesBusiness messagesBusiness;

  @Override
  public ResultTroubleDTO getDataConfig(String troubleId, String statusOld, String statusNew,
      String userLogin) {
    log.debug("Request to getDataConfig : {}", troubleId, statusOld, statusNew, userLogin);
    I18n.setLocaleForService(wsContext);
    ResultTroubleDTO resultDTO = new ResultTroubleDTO();
    resultDTO.setKey(RESULT.FAIL);
    resultDTO.setRequestTime(DateTimeUtils.getSysDateTime());
    if (StringUtils.isStringNullOrEmpty(troubleId)) {
      resultDTO.setMessage(I18n.getValidation("troublesServiceForVSmart.troubleId.notnull"));
      return resultDTO;
    }
    if (StringUtils.isStringNullOrEmpty(statusOld)) {
      resultDTO.setMessage(I18n.getValidation("troublesServiceForVSmart.statusOld.notnull"));
      return resultDTO;
    }
    if (StringUtils.isStringNullOrEmpty(statusNew)) {
      resultDTO.setMessage(I18n.getValidation("troublesServiceForVSmart.statusNew.notnull"));
      return resultDTO;
    }
    if (StringUtils.isStringNullOrEmpty(userLogin)) {
      resultDTO.setMessage(I18n.getValidation("troublesServiceForVSmart.userLogin.notnull"));
      return resultDTO;
    }
    TTChangeStatusDTO ttChangeStatusDTO = new TTChangeStatusDTO();
    if (StringUtils.isNotNullOrEmpty(troubleId)) {
      try {
        TroublesInSideDTO troublesDTO = troublesBusiness
            .findTroublesById(Long.parseLong(troubleId));
        if (troublesDTO != null && StringUtils.isNotNullOrEmpty(statusOld) && StringUtils
            .isNotNullOrEmpty(statusNew)) {
          TTChangeStatusDTO searchDTO = new TTChangeStatusDTO();
          searchDTO.setProxyLocale(I18n.getLocale());
          searchDTO.setAlarmGroup(troublesDTO.getAlarmGroupId());
          searchDTO.setTtTypeId(troublesDTO.getTypeId());
          searchDTO.setOldStatus(Long.valueOf(statusOld));
          searchDTO.setNewStatus(Long.valueOf(statusNew));
          List<Long> lstScopeOfUse = new ArrayList<>();
          lstScopeOfUse.add(1L);
          lstScopeOfUse.add(3L);
          searchDTO.setLstScopeOfUse(lstScopeOfUse);
          ttChangeStatusDTO = ttCategoryServiceProxy.getDetailCfg(searchDTO);
          if (ttChangeStatusDTO == null) {
            searchDTO.setAlarmGroup(null);
            searchDTO.setTtTypeId(null);
            searchDTO.setIsDefault(1L);
            ttChangeStatusDTO = ttCategoryServiceProxy.getDetailCfg(searchDTO);
          }
        }
        Map<String, String> mapCheckColumnName = new HashMap<>();
        Datatable datatable = catItemBusiness.getItemMaster(TT_MASTER_CODE.TT_CFG_BUSINESS_COLUMN,
            LANGUAGUE_EXCHANGE_SYSTEM.COMMON, String.valueOf(APPLIED_BUSSINESS.CAT_ITEM),
            Constants.ITEM_ID, Constants.ITEM_NAME);
        if (datatable != null && datatable.getData() != null && datatable.getData().size() > 0) {
          for (CatItemDTO i : (List<CatItemDTO>) datatable.getData()) {
            if (!mapCheckColumnName.containsKey(i.getItemValue())) {
              ItemDataTT itemDataTT = new ItemDataTT();
              itemDataTT.setItemCode(i.getItemValue());
              itemDataTT.setItemName(i.getItemName());
              mapCheckColumnName.put(i.getItemValue(), i.getItemName());
            }
          }
        }
        if (StringUtils.isNotNullOrEmpty(userLogin) && ttChangeStatusDTO != null
            && ttChangeStatusDTO.getTtChangeStatusRoleDTO() != null && !ttChangeStatusDTO
            .getTtChangeStatusRoleDTO().isEmpty()) {
          if (checkRole(ttChangeStatusDTO.getTtChangeStatusRoleDTO(), troublesDTO, userLogin)) {
            TTChangeStatusDTO searchDTO = new TTChangeStatusDTO();
            searchDTO.setAlarmGroup(troublesDTO.getAlarmGroupId());
            searchDTO.setTtTypeId(troublesDTO.getTypeId());
            searchDTO.setOldStatus(Long.valueOf(statusOld));
            List<ItemDataTT> lstStatusNew = troublesBusiness.getStatusConfig(searchDTO);
            if (lstStatusNew.isEmpty()) {
              searchDTO.setAlarmGroup(null);
              searchDTO.setTtTypeId(null);
              searchDTO.setIsDefault(1L);
              lstStatusNew = troublesBusiness.getStatusConfig(searchDTO);
            }
            if (lstStatusNew != null && !lstStatusNew.isEmpty()) {
              Map<String, String> checkStatus = new HashMap<>();
              for (int i = lstStatusNew.size() - 1; i > -1; i--) {
                TTChangeStatusRoleDTO roleDTO = new TTChangeStatusRoleDTO();
                if (StringUtils.isNotNullOrEmpty(lstStatusNew.get(i).getDescription())) {
                  roleDTO.setRoleId(Long.parseLong(lstStatusNew.get(i).getDescription()));
                }
                List<TTChangeStatusRoleDTO> lstRole = new ArrayList<>();
                lstRole.add(roleDTO);
                if (!checkRole(lstRole, troublesDTO, userLogin)) {
                  lstStatusNew.remove(i);
                  continue;
                }
                if (checkStatus.containsKey(lstStatusNew.get(i).getItemValue())) {
                  lstStatusNew.remove(i);
                  continue;
                }
                checkStatus
                    .put(lstStatusNew.get(i).getItemValue(), lstStatusNew.get(i).getItemValue());
                lstStatusNew.get(i).setDescription(null);
              }
            }
            if (ttChangeStatusDTO.getTtCfgBusinessDTO() != null && !ttChangeStatusDTO
                .getTtCfgBusinessDTO().isEmpty()) {
              TroublesDTO dtoOld = troublesDTO.toModelOutSide();
              List<CatItemDTO> lstTTControl = catItemBusiness
                  .getListItemByCategory(TT_MASTER_CODE.TT_CONTROL, null);
              Map<Long, String> mapTTControl = new HashMap<>();
              if (lstTTControl != null && !lstTTControl.isEmpty()) {
                for (CatItemDTO catItemDTO : lstTTControl) {
                  mapTTControl.put(catItemDTO.getItemId(), catItemDTO.getItemValue());
                }
              }
              for (TTCfgBusinessDTO item : ttChangeStatusDTO.getTtCfgBusinessDTO()) {
                item.setStt(null);
                item.setScopeOfUse(null);
                if (StringUtils.isNotNullOrEmpty(mapTTControl.get(item.getTypeControl()))) {
                  item.setTypeControl(Long.valueOf(mapTTControl.get(item.getTypeControl())));
                }
                if (mapCheckColumnName.get(item.getColumnName()) != null) {
                  item.setColumnNameValue(mapCheckColumnName.get(item.getColumnName()));
                }
                if (item.getColumnName() != null && "state".equals(item.getColumnName())
                    && lstStatusNew != null && !lstStatusNew.isEmpty()) {
                  item.setLstData(lstStatusNew);
                }
                if (StringUtils.isNotNullOrEmpty(item.getColumnName())) {
                  try {
                    Method method = TroublesDTO.class
                        .getMethod("get" + item.getColumnName().substring(0, 1).toUpperCase() + item
                            .getColumnName().substring(1));
                    Object value = method.invoke(dtoOld);
                    Field field = TroublesInSideDTO.class.getDeclaredField(item.getColumnName());
                    if (field.getType().getName().toLowerCase().endsWith("string")
                        && value != null) {
                      item.setItemValue(String.valueOf(value));
                    } else if (field.getType().getName().toLowerCase().endsWith("long")
                        && value != null) {
                      item.setItemValue(String.valueOf(value));
                    } else if (field.getType().getName().toLowerCase().endsWith("date")
                        && value != null) {
                      item.setItemValue(String.valueOf(value));
                    }
                  } catch (Exception ex) {
                    log.error(ex.getMessage());
                  }
                }
              }
            }
            ttChangeStatusDTO.setTtChangeStatusRoleDTO(new ArrayList<>());
            ttChangeStatusDTO.setTroubleId(troublesDTO.getTroubleId());
            ttChangeStatusDTO.setLongitude(troublesDTO.getLongitude());
            ttChangeStatusDTO.setLatitude(troublesDTO.getLatitude());
            resultDTO.setTtChangeStatusDTO(ttChangeStatusDTO);
            resultDTO.setFinishTime(DateTimeUtils.getSysDateTime());
            resultDTO.setKey(RESULT.SUCCESS);
            return resultDTO;
          } else {
            resultDTO.setFinishTime(DateTimeUtils.getSysDateTime());
            resultDTO
                .setMessage(I18n.getValidation("troublesServiceForVSmart.userLogin.notPermission"));
            return resultDTO;
          }
        } else {
          resultDTO.setFinishTime(DateTimeUtils.getSysDateTime());
          resultDTO.setMessage(I18n.getValidation("troublesServiceForVSmart.noConfig"));
          return resultDTO;
        }
      } catch (Exception ex) {
        log.error(ex.getMessage(), ex);
        resultDTO.setFinishTime(DateTimeUtils.getSysDateTime());
        resultDTO.setMessage(ex.getMessage());
        return resultDTO;
      }
    }
    resultDTO.setFinishTime(DateTimeUtils.getSysDateTime());
    return resultDTO;
  }

  private boolean checkRole(List<TTChangeStatusRoleDTO> roleDTOS, TroublesInSideDTO troublesDTO,
      String userLogin) {
    if (roleDTOS != null && !roleDTOS.isEmpty() && StringUtils.isNotNullOrEmpty(userLogin)) {
      UsersInsideDto usersInsideDto = userBusiness.getUserDTOByUserName(userLogin);
      if (usersInsideDto != null) {
        for (TTChangeStatusRoleDTO item : roleDTOS) {
          if (!StringUtils.isLongNullOrEmpty(item.getRoleId())) {
            if (TT_MASTER_CODE.TT_ROLE_NVT.equals(item.getRoleId().toString())) {
              if (!StringUtils.isLongNullOrEmpty(usersInsideDto.getUserId()) && !StringUtils
                  .isLongNullOrEmpty(troublesDTO.getCreateUserId()) && usersInsideDto
                  .getUserId().equals(troublesDTO.getCreateUserId())) {
                return true;
              }
            } else if (TT_MASTER_CODE.TT_ROLE_NVXL.equals(item.getRoleId().toString())) {
              if (!StringUtils.isLongNullOrEmpty(usersInsideDto.getUserId()) && !StringUtils
                  .isLongNullOrEmpty(troublesDTO.getReceiveUserId()) && usersInsideDto
                  .getUserId().equals(troublesDTO.getReceiveUserId())) {
                return true;
              }
            } else if (TT_MASTER_CODE.TT_ROLE_DVXL.equals(item.getRoleId().toString())) {
              if (!StringUtils.isLongNullOrEmpty(usersInsideDto.getUnitId()) && !StringUtils
                  .isLongNullOrEmpty(troublesDTO.getReceiveUnitId()) && usersInsideDto.getUnitId()
                  .equals(troublesDTO.getReceiveUnitId())) {
                return true;
              }
            } else if (TT_MASTER_CODE.TT_ROLE_DVT.equals(item.getRoleId().toString())) {
              if (!StringUtils.isLongNullOrEmpty(usersInsideDto.getUnitId()) && !StringUtils
                  .isLongNullOrEmpty(troublesDTO.getCreateUnitId()) && usersInsideDto.getUnitId()
                  .equals(troublesDTO.getCreateUnitId())) {
                return true;
              }

            } else if (TT_MASTER_CODE.TT_ROLE_LDDVT.equals(item.getRoleId().toString())) {
              if (!StringUtils.isLongNullOrEmpty(usersInsideDto.getUnitId()) && !StringUtils
                  .isLongNullOrEmpty(troublesDTO.getCreateUnitId()) && usersInsideDto.getUnitId()
                  .equals(troublesDTO.getCreateUnitId()) && userBusiness
                  .checkRoleOfUser("TP", usersInsideDto.getUserId())) {
                return true;
              }
            }
          }
        }
      }
    }
    return false;
  }

  @Override
  public ResultDTO updateTTFromVSMART(TTChangeStatusDTO ttChangeStatusDTO) {
    log.debug("Request to updateTTFromVSMART : {}", ttChangeStatusDTO);
    I18n.setLocaleForService(wsContext);
    ResultDTO resultDTO = new ResultDTO(null, RESULT.FAIL, RESULT.FAIL);
    if (ttChangeStatusDTO != null) {
      if (StringUtils.isStringNullOrEmpty(ttChangeStatusDTO.getUserLogin())) {
        resultDTO.setMessage(I18n.getValidation("troublesServiceForVSmart.userLogin.notnull"));
        return resultDTO;
      }
      if (StringUtils.isLongNullOrEmpty(ttChangeStatusDTO.getNewStatus())) {
        resultDTO.setMessage(I18n.getValidation("troublesServiceForVSmart.statusNew.notnull"));
        return resultDTO;
      }
      Map<String, String> mapDataUpdate = new HashMap<>();
      Map<String, String> mapColNameRelate = new HashMap<>();
      if (ttChangeStatusDTO.getTtCfgBusinessDTO() != null && !ttChangeStatusDTO
          .getTtCfgBusinessDTO()
          .isEmpty()) {
        for (TTCfgBusinessDTO ttCfgBusinessDTO : ttChangeStatusDTO.getTtCfgBusinessDTO()) {
          if (StringUtils.isNotNullOrEmpty(ttCfgBusinessDTO.getColumnName())) {
            mapDataUpdate.put(ttCfgBusinessDTO.getColumnName(), ttCfgBusinessDTO.getItemValue());
          }
          if (StringUtils.isNotNullOrEmpty(ttCfgBusinessDTO.getColumnName())) {
            mapColNameRelate
                .put(ttCfgBusinessDTO.getColNameRelate(), ttCfgBusinessDTO.getItemNameValue());
          }
        }
      }
      TroublesDTO dtoOld = troublesBusiness
          .getDetailTroublesById(Long.valueOf(ttChangeStatusDTO.getTroubleId()));
      if (dtoOld != null) {
        TTChangeStatusDTO searchDTO = new TTChangeStatusDTO();
        searchDTO.setProxyLocale(I18n.getLocale());
        searchDTO.setAlarmGroup(dtoOld.getAlarmGroupId());
        if (StringUtils.isNotNullOrEmpty(dtoOld.getTypeId())) {
          searchDTO.setTtTypeId(Long.parseLong(dtoOld.getTypeId()));
        }
        if (StringUtils.isNotNullOrEmpty(dtoOld.getState())) {
          searchDTO.setOldStatus(Long.valueOf(dtoOld.getState()));
        }
        if (!StringUtils.isLongNullOrEmpty(ttChangeStatusDTO.getNewStatus())) {
          searchDTO.setNewStatus(ttChangeStatusDTO.getNewStatus());
        }
        List<Long> lstScopeOfUse = new ArrayList<>();
        lstScopeOfUse.add(1L);
        lstScopeOfUse.add(3L);
        searchDTO.setLstScopeOfUse(lstScopeOfUse);
        TTChangeStatusDTO dataConfigs = ttCategoryServiceProxy.getDetailCfg(searchDTO);
        if (dataConfigs == null) {
          searchDTO.setAlarmGroup(null);
          searchDTO.setTtTypeId(null);
          searchDTO.setIsDefault(1L);
          dataConfigs = ttCategoryServiceProxy.getDetailCfg(searchDTO);
        }
        if (dataConfigs != null) {
          if (checkRole(dataConfigs.getTtChangeStatusRoleDTO(),
              dtoOld.toModelInSide(), ttChangeStatusDTO.getUserLogin())) {
            if (dataConfigs.getTtCfgBusinessDTO() != null && !dataConfigs
                .getTtCfgBusinessDTO().isEmpty()) {
              List<CatItemDTO> lstTTControl = catItemBusiness
                  .getListItemByCategory(TT_MASTER_CODE.TT_CONTROL, null);
              Map<Long, String> mapTTControl = new HashMap<>();
              if (lstTTControl != null && !lstTTControl.isEmpty()) {
                for (CatItemDTO catItemDTO : lstTTControl) {
                  mapTTControl.put(catItemDTO.getItemId(), catItemDTO.getItemValue());
                }
              }
              for (TTCfgBusinessDTO item : dataConfigs.getTtCfgBusinessDTO()) {
                if (StringUtils.isNotNullOrEmpty(item.getColumnName())) {
                  try {
//                    Method method = TroublesDTO.class
//                        .getMethod("get" + item.getColumnName().substring(0, 1).toUpperCase() + item
//                            .getColumnName().substring(1));
                    String value = mapDataUpdate.get(item.getColumnName());
                    String valueName = mapColNameRelate.get(item.getColNameRelate());
//                    if ("1".equals(String.valueOf(item.getIsRequired())) && StringUtils
//                        .isStringNullOrEmpty(value)) {
//                      resultDTO.setMessage(item.getColumnName() + " : " + I18n
//                          .getValidation("troublesServiceForVSmart.common.notnull"));
//                      return resultDTO;
//                    }
//                    if (!"1".equals(String.valueOf(item.getEditable())) && StringUtils
//                        .isNotNullOrEmpty(value) && !value
//                        .equals(String.valueOf(method.invoke(dtoOld)))) {
//                      resultDTO.setMessage(item.getColumnName() + " : " + I18n
//                          .getValidation("troublesServiceForVSmart.notEdit"));
//                      return resultDTO;
//                    }
                    if ("-1".equals(value) && mapTTControl.containsKey(item.getTypeControl()) && "3"
                        .equals(mapTTControl.get(item.getTypeControl()))) {
                      value = "";
                    }
                    if ("1".equals(String.valueOf(item.getEditable()))) {
                      Field field = TroublesDTO.class.getDeclaredField(item.getColumnName());
                      if (field.getType().getName().toLowerCase().endsWith("string")) {
                        Method method2 = TroublesDTO.class.getMethod(
                            "set" + item.getColumnName().substring(0, 1).toUpperCase() + item
                                .getColumnName().substring(1), String.class);
                        method2.invoke(dtoOld, (String) value);
                      } else if (field.getType().getName().toLowerCase().endsWith("long")) {
                        Method method2 = TroublesDTO.class
                            .getMethod(
                                "set" + item.getColumnName().substring(0, 1).toUpperCase() + item
                                    .getColumnName().substring(1), Long.class);
                        method2.invoke(dtoOld, Long.valueOf(value));
                      } else if (field.getType().getName().toLowerCase().endsWith("date")) {
                        Method method2 = TroublesDTO.class
                            .getMethod(
                                "set" + item.getColumnName().substring(0, 1).toUpperCase() + item
                                    .getColumnName().substring(1), Date.class);
                        method2.invoke(dtoOld, DateUtil.string2DateTime(value));
                      }
                      if (StringUtils.isNotNullOrEmpty(item.getColNameRelate())) {
                        Field field2 = TroublesDTO.class.getDeclaredField(item.getColNameRelate());
                        if (field2.getType().getName().toLowerCase().endsWith("string")) {
                          Method method2 = TroublesDTO.class.getMethod(
                              "set" + item.getColNameRelate().substring(0, 1).toUpperCase() + item
                                  .getColNameRelate().substring(1), String.class);
                          method2.invoke(dtoOld, (String) valueName);
                        } else if (field2.getType().getName().toLowerCase().endsWith("long")) {
                          Method method2 = TroublesDTO.class
                              .getMethod(
                                  "set" + item.getColNameRelate().substring(0, 1).toUpperCase()
                                      + item
                                      .getColNameRelate().substring(1), Long.class);
                          method2.invoke(dtoOld, (String) valueName);
                        }
                      }
                    }
                  } catch (Exception ex) {
                    log.error(ex.getMessage());
                  }
                }
              }
            }
            if (ttChangeStatusDTO.getLstFileName() != null
                && ttChangeStatusDTO.getLstFileName().size() > 0
                && ttChangeStatusDTO.getLstFileArr() != null
                && ttChangeStatusDTO.getLstFileArr().size() > 0) {
              troublesBusiness
                  .updateFile(ttChangeStatusDTO.getTroubleId(), ttChangeStatusDTO.getUserLogin(),
                      ttChangeStatusDTO.getLstFileName(), ttChangeStatusDTO.getLstFileArr());
            }
            dtoOld.setLongitude(ttChangeStatusDTO.getLongitude());
            dtoOld.setLatitude(ttChangeStatusDTO.getLatitude());
            resultDTO = troublesBusiness.updateTroubleFromVSMART(dtoOld, false);
            if (RESULT.SUCCESS.equals(resultDTO.getKey())) {
              List<MessagesDTO> lsMessagesDTOs = new ArrayList<>();
              String alias = getAlias(dtoOld.getTypeId(), dtoOld.getAlarmGroupId());
              Map<String, String> mapCheckUsersSMS = new HashMap<>();
              if (!StringUtils.isLongNullOrEmpty(dataConfigs.getSendCreate())
                  && dataConfigs.getSendCreate() == 1L && StringUtils
                  .isNotNullOrEmpty(dtoOld.getCreateUserId())) {
                //gui sms cho nguoi tao
                UsersInsideDto userExecute = userBusiness
                    .getUserDetailById(Long.parseLong(dtoOld.getCreateUserId()));
                UnitDTO unitExecute = unitBusiness.findUnitById(userExecute.getUnitId());
                if (!mapCheckUsersSMS.containsKey(userExecute.getUsername())) {
                  MessagesDTO messagesDTO = new MessagesDTO();
                  messagesDTO.setContent(getSmsContenFromCfg(userExecute.getUserLanguage(),
                      dataConfigs.getCreateContent(), dtoOld));
                  messagesDTO.setReceiverId(String.valueOf(userExecute.getUserId()));
                  messagesDTO.setReceiverUsername(userExecute.getUsername());
                  messagesDTO.setReceiverPhone(userExecute.getMobile());
                  messagesDTO.setStatus("0");
                  if (!StringUtils.isLongNullOrEmpty(unitExecute.getSmsGatewayId())) {
                    messagesDTO.setSmsGatewayId(String.valueOf(unitExecute.getSmsGatewayId()));
                  }
                  messagesDTO.setCreateTime(DateUtil.date2ddMMyyyyHHMMss(new Date()));
                  messagesDTO.setAlias(alias);
                  mapCheckUsersSMS.put(userExecute.getUsername(), userExecute.getUsername());
                  lsMessagesDTOs.add(messagesDTO);
                }
              }
              if (!StringUtils.isLongNullOrEmpty(dataConfigs.getSendReceiveUser())
                  && dataConfigs.getSendReceiveUser() == 1L && StringUtils
                  .isNotNullOrEmpty(dtoOld.getReceiveUserId())) {
                //gui sms cho nguoi xu ly
                UsersInsideDto userExecute = userBusiness
                    .getUserDetailById(Long.parseLong(dtoOld.getReceiveUserId()));
                UnitDTO unitExecute = unitBusiness.findUnitById(userExecute.getUnitId());
                if (!mapCheckUsersSMS.containsKey(userExecute.getUsername())) {
                  MessagesDTO messagesDTO = new MessagesDTO();
                  messagesDTO.setContent(getSmsContenFromCfg(userExecute.getUserLanguage(),
                      dataConfigs.getReceiveUserContent(), dtoOld));
                  messagesDTO.setReceiverId(String.valueOf(userExecute.getUserId()));
                  messagesDTO.setReceiverUsername(userExecute.getUsername());
                  messagesDTO.setReceiverPhone(userExecute.getMobile());
                  messagesDTO.setStatus("0");
                  if (!StringUtils.isLongNullOrEmpty(unitExecute.getSmsGatewayId())) {
                    messagesDTO.setSmsGatewayId(String.valueOf(unitExecute.getSmsGatewayId()));
                  }
                  messagesDTO.setCreateTime(DateUtil.date2ddMMyyyyHHMMss(new Date()));
                  messagesDTO.setAlias(alias);
                  mapCheckUsersSMS.put(userExecute.getUsername(), userExecute.getUsername());
                  lsMessagesDTOs.add(messagesDTO);
                }
              }
              if (!StringUtils.isLongNullOrEmpty(dataConfigs.getSendCreateUnit()) && StringUtils
                  .isNotNullOrEmpty(dtoOld.getCreateUnitId())) {
                //gui sms cho don vi tao
                if (dataConfigs.getSendCreateUnit() == 1L) {
                  UnitDTO unitExecute = unitBusiness
                      .findUnitById(Long.parseLong(dtoOld.getCreateUnitId()));
                  List<UsersInsideDto> lstUsers = userBusiness
                      .getListUserByUnitId(unitExecute.getUnitId());
                  if (lstUsers != null && !lstUsers.isEmpty()) {
                    for (UsersInsideDto usersInsideDto : lstUsers) {
                      if (!mapCheckUsersSMS.containsKey(usersInsideDto.getUsername())) {
                        MessagesDTO messagesDTO = new MessagesDTO();
                        messagesDTO.setContent(getSmsContenFromCfg(usersInsideDto.getUserLanguage(),
                            dataConfigs.getCreateUnitContent(), dtoOld));
                        messagesDTO.setReceiverId(String.valueOf(usersInsideDto.getUserId()));
                        messagesDTO.setReceiverUsername(usersInsideDto.getUsername());
                        messagesDTO.setReceiverPhone(usersInsideDto.getMobile());
                        messagesDTO.setStatus("0");
                        if (!StringUtils.isLongNullOrEmpty(unitExecute.getSmsGatewayId())) {
                          messagesDTO
                              .setSmsGatewayId(String.valueOf(unitExecute.getSmsGatewayId()));
                        }
                        messagesDTO.setCreateTime(DateUtil.date2ddMMyyyyHHMMss(new Date()));
                        messagesDTO.setAlias(alias);
                        mapCheckUsersSMS
                            .put(usersInsideDto.getUsername(), usersInsideDto.getUsername());
                        lsMessagesDTOs.add(messagesDTO);
                      }
                    }
                  }
                } else if (dataConfigs.getSendCreateUnit() == 2L) {
                  //gui sms cho lãnh dạo  don vi tạo
                  UnitDTO unitExecute = unitBusiness
                      .findUnitById(Long.parseLong(dtoOld.getCreateUnitId()));
                  List<UsersInsideDto> lstUsers = userBusiness
                      .getListUserByUnitId(unitExecute.getUnitId());
                  if (lstUsers != null && !lstUsers.isEmpty()) {
                    for (UsersInsideDto usersInsideDto : lstUsers) {
                      if (!mapCheckUsersSMS.containsKey(usersInsideDto.getUsername())) {
                        if (userBusiness.checkRoleOfUser("TP", usersInsideDto.getUserId())) {
                          MessagesDTO messagesDTO = new MessagesDTO();
                          messagesDTO.setContent(
                              getSmsContenFromCfg(usersInsideDto.getUserLanguage(),
                                  dataConfigs.getCreateUnitContent(), dtoOld));
                          messagesDTO.setReceiverId(String.valueOf(usersInsideDto.getUserId()));
                          messagesDTO.setReceiverUsername(usersInsideDto.getUsername());
                          messagesDTO.setReceiverPhone(usersInsideDto.getMobile());
                          messagesDTO.setStatus("0");
                          if (!StringUtils.isLongNullOrEmpty(unitExecute.getSmsGatewayId())) {
                            messagesDTO
                                .setSmsGatewayId(String.valueOf(unitExecute.getSmsGatewayId()));
                          }
                          messagesDTO.setCreateTime(DateUtil.date2ddMMyyyyHHMMss(new Date()));
                          messagesDTO.setAlias(alias);
                          mapCheckUsersSMS
                              .put(usersInsideDto.getUsername(), usersInsideDto.getUsername());
                          lsMessagesDTOs.add(messagesDTO);
                        }
                      }
                    }
                  }
                }
              }
              if (StringUtils.isNotNullOrEmpty(dtoOld.getUnitMove())) {
                if (!StringUtils.isLongNullOrEmpty(dataConfigs.getSendReceiveUnitLv1())
                    && dataConfigs.getSendReceiveUnitLv1() == 1L) {
                  //gui sms cho don vi xu ly cap 1
                  UnitDTO unitExecute = unitBusiness
                      .findUnitById(Long.parseLong(dtoOld.getUnitMove()));
                  List<UsersInsideDto> lstUsers = userBusiness
                      .getListUserByUnitId(unitExecute.getUnitId());
                  if (lstUsers != null && !lstUsers.isEmpty()) {
                    for (UsersInsideDto usersInsideDto : lstUsers) {
                      if (!mapCheckUsersSMS.containsKey(usersInsideDto.getUsername())) {
                        MessagesDTO messagesDTO = new MessagesDTO();
                        messagesDTO.setContent(getSmsContenFromCfg(usersInsideDto.getUserLanguage(),
                            dataConfigs.getReceiveUnitLv1Content(), dtoOld));
                        messagesDTO.setReceiverId(String.valueOf(usersInsideDto.getUserId()));
                        messagesDTO.setReceiverUsername(usersInsideDto.getUsername());
                        messagesDTO.setReceiverPhone(usersInsideDto.getMobile());
                        messagesDTO.setStatus("0");
                        if (!StringUtils.isLongNullOrEmpty(unitExecute.getSmsGatewayId())) {
                          messagesDTO
                              .setSmsGatewayId(String.valueOf(unitExecute.getSmsGatewayId()));
                        }
                        messagesDTO.setCreateTime(DateUtil.date2ddMMyyyyHHMMss(new Date()));
                        messagesDTO.setAlias(alias);
                        mapCheckUsersSMS
                            .put(usersInsideDto.getUsername(), usersInsideDto.getUsername());
                        lsMessagesDTOs.add(messagesDTO);
                      }
                    }
                  }
                } else if (!StringUtils.isLongNullOrEmpty(dataConfigs.getSendReceiveUnitLv1())
                    && dataConfigs.getSendReceiveUnitLv1() == 2L) {
                  //gui sms cho don vi lãnh dạo xu ly cap 1
                  UnitDTO unitExecute = unitBusiness
                      .findUnitById(Long.parseLong(dtoOld.getUnitMove()));
                  List<UsersInsideDto> lstUsers = userBusiness
                      .getListUserByUnitId(unitExecute.getUnitId());
                  if (lstUsers != null && !lstUsers.isEmpty()) {
                    for (UsersInsideDto usersInsideDto : lstUsers) {
                      if (!mapCheckUsersSMS.containsKey(usersInsideDto.getUsername())) {
                        if (userBusiness.checkRoleOfUser("TP", usersInsideDto.getUserId())) {
                          MessagesDTO messagesDTO = new MessagesDTO();
                          messagesDTO.setContent(
                              getSmsContenFromCfg(usersInsideDto.getUserLanguage(),
                                  dataConfigs.getReceiveUnitLv1Content(), dtoOld));
                          messagesDTO.setReceiverId(String.valueOf(usersInsideDto.getUserId()));
                          messagesDTO.setReceiverUsername(usersInsideDto.getUsername());
                          messagesDTO.setReceiverPhone(usersInsideDto.getMobile());
                          messagesDTO.setStatus("0");
                          if (!StringUtils.isLongNullOrEmpty(unitExecute.getSmsGatewayId())) {
                            messagesDTO
                                .setSmsGatewayId(String.valueOf(unitExecute.getSmsGatewayId()));
                          }
                          messagesDTO.setCreateTime(DateUtil.date2ddMMyyyyHHMMss(new Date()));
                          messagesDTO.setAlias(alias);
                          mapCheckUsersSMS
                              .put(usersInsideDto.getUsername(), usersInsideDto.getUsername());
                          lsMessagesDTOs.add(messagesDTO);
                        }
                      }
                    }
                  }
                }
                if (!StringUtils.isLongNullOrEmpty(dataConfigs.getSendReceiveUnitLv2())
                    && dataConfigs.getSendReceiveUnitLv2() == 1L) {
                  //gui sms cho don vi xu ly cap 2
                  UnitDTO unitExecute = unitBusiness
                      .findUnitById(Long.parseLong(dtoOld.getReceiveUnitId()));
                  List<UsersInsideDto> lstUsers = userBusiness
                      .getListUserByUnitId(unitExecute.getUnitId());
                  if (lstUsers != null && !lstUsers.isEmpty()) {
                    for (UsersInsideDto usersInsideDto : lstUsers) {
                      if (!mapCheckUsersSMS.containsKey(usersInsideDto.getUsername())) {
                        MessagesDTO messagesDTO = new MessagesDTO();
                        messagesDTO.setContent(getSmsContenFromCfg(usersInsideDto.getUserLanguage(),
                            dataConfigs.getReceiveUnitLv2Content(), dtoOld));
                        messagesDTO.setReceiverId(String.valueOf(usersInsideDto.getUserId()));
                        messagesDTO.setReceiverUsername(usersInsideDto.getUsername());
                        messagesDTO.setReceiverPhone(usersInsideDto.getMobile());
                        messagesDTO.setStatus("0");
                        if (!StringUtils.isLongNullOrEmpty(unitExecute.getSmsGatewayId())) {
                          messagesDTO
                              .setSmsGatewayId(String.valueOf(unitExecute.getSmsGatewayId()));
                        }
                        messagesDTO.setCreateTime(DateUtil.date2ddMMyyyyHHMMss(new Date()));
                        messagesDTO.setAlias(alias);
                        mapCheckUsersSMS
                            .put(usersInsideDto.getUsername(), usersInsideDto.getUsername());
                        lsMessagesDTOs.add(messagesDTO);
                      }
                    }
                  }
                } else if (!StringUtils.isLongNullOrEmpty(dataConfigs.getSendReceiveUnitLv2())
                    && dataConfigs.getSendReceiveUnitLv2() == 2L) {
                  //gui sms cho don vi lãnh dạo xu ly cap 2
                  UnitDTO unitExecute = unitBusiness
                      .findUnitById(Long.parseLong(dtoOld.getReceiveUnitId()));
                  List<UsersInsideDto> lstUsers = userBusiness
                      .getListUserByUnitId(unitExecute.getUnitId());
                  if (lstUsers != null && !lstUsers.isEmpty()) {
                    for (UsersInsideDto usersInsideDto : lstUsers) {
                      if (!mapCheckUsersSMS.containsKey(usersInsideDto.getUsername())) {
                        if (userBusiness.checkRoleOfUser("TP", usersInsideDto.getUserId())) {
                          MessagesDTO messagesDTO = new MessagesDTO();
                          messagesDTO.setContent(
                              getSmsContenFromCfg(usersInsideDto.getUserLanguage(),
                                  dataConfigs.getReceiveUnitLv2Content(), dtoOld));
                          messagesDTO.setReceiverId(String.valueOf(usersInsideDto.getUserId()));
                          messagesDTO.setReceiverUsername(usersInsideDto.getUsername());
                          messagesDTO.setReceiverPhone(usersInsideDto.getMobile());
                          messagesDTO.setStatus("0");
                          if (!StringUtils.isLongNullOrEmpty(unitExecute.getSmsGatewayId())) {
                            messagesDTO
                                .setSmsGatewayId(String.valueOf(unitExecute.getSmsGatewayId()));
                          }
                          messagesDTO.setCreateTime(DateUtil.date2ddMMyyyyHHMMss(new Date()));
                          messagesDTO.setAlias(alias);
                          mapCheckUsersSMS
                              .put(usersInsideDto.getUsername(), usersInsideDto.getUsername());
                          lsMessagesDTOs.add(messagesDTO);
                        }
                      }
                    }
                  }
                }
              } else {
                if (!StringUtils.isLongNullOrEmpty(dataConfigs.getSendReceiveUnitLv1())
                    && dataConfigs.getSendReceiveUnitLv1() == 1L) {
                  //gui sms cho don vi xu ly cap 1
                  UnitDTO unitExecute = unitBusiness
                      .findUnitById(Long.parseLong(dtoOld.getReceiveUnitId()));
                  List<UsersInsideDto> lstUsers = userBusiness
                      .getListUserByUnitId(unitExecute.getUnitId());
                  if (lstUsers != null && !lstUsers.isEmpty()) {
                    for (UsersInsideDto usersInsideDto : lstUsers) {
                      if (!mapCheckUsersSMS.containsKey(usersInsideDto.getUsername())) {
                        MessagesDTO messagesDTO = new MessagesDTO();
                        messagesDTO.setContent(getSmsContenFromCfg(usersInsideDto.getUserLanguage(),
                            dataConfigs.getReceiveUnitLv1Content(), dtoOld));
                        messagesDTO.setReceiverId(String.valueOf(usersInsideDto.getUserId()));
                        messagesDTO.setReceiverUsername(usersInsideDto.getUsername());
                        messagesDTO.setReceiverPhone(usersInsideDto.getMobile());
                        messagesDTO.setStatus("0");
                        if (!StringUtils.isLongNullOrEmpty(unitExecute.getSmsGatewayId())) {
                          messagesDTO
                              .setSmsGatewayId(String.valueOf(unitExecute.getSmsGatewayId()));
                        }
                        messagesDTO.setCreateTime(DateUtil.date2ddMMyyyyHHMMss(new Date()));
                        messagesDTO.setAlias(alias);
                        mapCheckUsersSMS
                            .put(usersInsideDto.getUsername(), usersInsideDto.getUsername());
                        lsMessagesDTOs.add(messagesDTO);
                      }
                    }
                  }
                } else if (!StringUtils.isLongNullOrEmpty(dataConfigs.getSendReceiveUnitLv1())
                    && dataConfigs.getSendReceiveUnitLv1() == 2L) {
                  //gui sms cho don vi lanh dao xu ly cap 2
                  UnitDTO unitExecute = unitBusiness
                      .findUnitById(Long.parseLong(dtoOld.getReceiveUnitId()));
                  List<UsersInsideDto> lstUsers = userBusiness
                      .getListUserByUnitId(unitExecute.getUnitId());
                  if (lstUsers != null && !lstUsers.isEmpty()) {
                    for (UsersInsideDto usersInsideDto : lstUsers) {
                      if (!mapCheckUsersSMS.containsKey(usersInsideDto.getUsername())) {
                        if (userBusiness.checkRoleOfUser("TP", usersInsideDto.getUserId())) {
                          MessagesDTO messagesDTO = new MessagesDTO();
                          messagesDTO.setContent(
                              getSmsContenFromCfg(usersInsideDto.getUserLanguage(),
                                  dataConfigs.getReceiveUnitLv1Content(), dtoOld));
                          messagesDTO.setReceiverId(String.valueOf(usersInsideDto.getUserId()));
                          messagesDTO.setReceiverUsername(usersInsideDto.getUsername());
                          messagesDTO.setReceiverPhone(usersInsideDto.getMobile());
                          messagesDTO.setStatus("0");
                          if (!StringUtils.isLongNullOrEmpty(unitExecute.getSmsGatewayId())) {
                            messagesDTO
                                .setSmsGatewayId(String.valueOf(unitExecute.getSmsGatewayId()));
                          }
                          messagesDTO.setCreateTime(DateUtil.date2ddMMyyyyHHMMss(new Date()));
                          messagesDTO.setAlias(alias);
                          mapCheckUsersSMS
                              .put(usersInsideDto.getUsername(), usersInsideDto.getUsername());
                          lsMessagesDTOs.add(messagesDTO);
                        }
                      }
                    }
                  }
                }
              }
              if (lsMessagesDTOs != null && !lsMessagesDTOs.isEmpty()) {
                messagesBusiness.insertOrUpdateListMessages(lsMessagesDTOs);
              }
              resultDTO.setId(resultDTO.getId());
              resultDTO.setMessage(I18n.getLanguage("tt.update.success"));
            }
          } else {
            resultDTO
                .setMessage(I18n.getValidation("troublesServiceForVSmart.userLogin.notPermission"));
            return resultDTO;
          }
        } else {
          resultDTO.setMessage(I18n.getValidation("troublesServiceForVSmart.noConfig"));
          return resultDTO;
        }
      }
    }
    return resultDTO;
  }

  @Override
  public List<ItemDataTT> getListCombo(String keyCode, String id, String parentId, String typeId) {
    log.debug("Request to getListCombo : {}", keyCode, id, parentId, typeId);
    I18n.setLocaleForService(wsContext);
    List<ItemDataTT> lstResult = new ArrayList<>();
    if (StringUtils.isNotNullOrEmpty(keyCode)) {
      List<CatItemDTO> data = new ArrayList<>();
      if (TT_MASTER_CODE.CAT_REASON.equals(keyCode)) {
        ObjectSearchDto objectSearchDto = new ObjectSearchDto();
        objectSearchDto.setModuleName(keyCode);
        if (StringUtils.isStringNullOrEmpty(parentId) && StringUtils.isNotNullOrEmpty(typeId)
            && StringUtils.isStringNullOrEmpty(id)) {
          objectSearchDto.setParent(parentId);
          objectSearchDto.setParam(typeId);
          List<TreeDTO> lst = commonBusiness.getTreeData(objectSearchDto);
          if (lst != null && !lst.isEmpty()) {
            List<ItemDataTT> lstDataCombo = new ArrayList<>();
            ItemDataTT item = new ItemDataTT();
            for (TreeDTO treeDTO : lst) {
              ItemDataTT dataShow = new ItemDataTT();
              dataShow.setItemId(treeDTO.getKey());
              dataShow.setItemCode(treeDTO.getValue());
              dataShow.setItemName(treeDTO.getTitle());
              if (treeDTO.getIsLeaf()) {
                dataShow.setIsleaf("1");
              } else {
                dataShow.setIsleaf("0");
              }
              lstDataCombo.add(dataShow);
            }
            item.setLstDataCombo(lstDataCombo);
            lstResult.add(item);
          }
        } else if (StringUtils.isNotNullOrEmpty(parentId) && StringUtils.isNotNullOrEmpty(typeId)
            && StringUtils.isStringNullOrEmpty(id)) {
          List<ItemDataTT> lstData = troublesBusiness.getTroubleReasonTreeById(typeId, parentId);
          ItemDataTT item = new ItemDataTT();
          if (lstData != null && !lstData.isEmpty()) {
            int count = 1;
            for (ItemDataTT itemDataTT : lstData) {
              if (parentId.equals(itemDataTT.getItemId())) {
                item = itemDataTT;
                item.setLevel(String.valueOf(count));
                break;
              }
              count++;
            }
          }
          objectSearchDto.setParent(parentId);
          objectSearchDto.setParam(typeId);
          List<TreeDTO> lst = commonBusiness.getTreeData(objectSearchDto);
          if (lst != null && !lst.isEmpty()) {
            List<ItemDataTT> lstDataCombo = new ArrayList<>();
            String level = null;
            if (item != null && StringUtils.isNotNullOrEmpty(item.getLevel())) {
              level = String.valueOf(Long.parseLong(item.getLevel()) + 1);
            }
            for (TreeDTO treeDTO : lst) {
              ItemDataTT dataShow = new ItemDataTT();
              dataShow.setItemId(treeDTO.getKey());
              dataShow.setItemCode(treeDTO.getValue());
              dataShow.setItemName(treeDTO.getTitle());
              if (treeDTO.getIsLeaf()) {
                dataShow.setIsleaf("1");
              } else {
                dataShow.setIsleaf("0");
              }
              if (StringUtils.isNotNullOrEmpty(level)) {
                dataShow.setLevel(level);
              }
              lstDataCombo.add(dataShow);
            }
            item.setLstDataCombo(lstDataCombo);
            lstResult.add(item);
          }
        } else if (StringUtils.isNotNullOrEmpty(id) && StringUtils.isNotNullOrEmpty(typeId)
            && StringUtils.isStringNullOrEmpty(parentId)) {
          objectSearchDto.setModuleName(keyCode);
          objectSearchDto.setParam(typeId);
          objectSearchDto.setParent(null);
          List<TreeDTO> lstParent = commonBusiness.getTreeData(objectSearchDto);
          List<ItemDataTT> lstDataParent = new ArrayList<>();
          if (lstParent != null && !lstParent.isEmpty()) {
            for (TreeDTO treeDTO : lstParent) {
              ItemDataTT dataShow = new ItemDataTT();
              dataShow.setItemId(treeDTO.getKey());
              dataShow.setItemCode(treeDTO.getValue());
              dataShow.setItemName(treeDTO.getTitle());
              lstDataParent.add(dataShow);
            }
          }
          List<ItemDataTT> lst = troublesBusiness.getTroubleReasonTreeById(typeId, id);
          if (lst != null && !lst.isEmpty()) {
            int count = 1;
            for (int i = 0; i < lst.size(); i++) {
              List<ItemDataTT> lstDataCombo = new ArrayList<>();
              ItemDataTT item = lst.get(i);
              item.setLevel(String.valueOf(count));
              if (StringUtils.isStringNullOrEmpty(lst.get(i).getParentItemId())
                  && lstDataParent != null && !lstDataParent.isEmpty()) {
                lstDataCombo.addAll(lstDataParent);
              } else {
                List<ItemDataTT> lstTree = troublesBusiness
                    .getReasonByParentId(lst.get(i).getParentItemId(), typeId);
                lstDataCombo.addAll(lstTree);
              }
              item.setLstDataCombo(lstDataCombo);
              lstResult.add(item);
              count++;
            }
          }
        }
      } else if (TT_MASTER_CODE.ALARM_GROUP.equals(keyCode) || TT_MASTER_CODE.PT_SUB_CATEGORY
          .equals(keyCode)) {
        data = catItemBusiness
            .getListItemByCategoryAndParent(keyCode, parentId);
      } else {
        Datatable datatable = catItemBusiness
            .getItemMaster(keyCode, "1", "3", Constants.ITEM_ID,
                Constants.ITEM_NAME);
        if (datatable != null && datatable.getData() != null && !datatable.getData().isEmpty()) {
          data = (List<CatItemDTO>) datatable.getData();
        }
      }
      if (data != null && !data.isEmpty()) {
        if (data != null && !data.isEmpty()) {
          ItemDataTT item = new ItemDataTT();
          item.setItemCode(keyCode);
          List<ItemDataTT> lstDataCobo = new ArrayList<>();
          for (CatItemDTO catItemDTO : data) {
            ItemDataTT result = new ItemDataTT();
            if (!StringUtils.isLongNullOrEmpty(catItemDTO.getItemId())) {
              result.setItemId(catItemDTO.getItemId().toString());
            }
            result.setItemValue(catItemDTO.getItemValue());
            result.setItemCode(catItemDTO.getItemCode());
            result.setItemName(catItemDTO.getItemName());
            result.setDescription(catItemDTO.getDescription());
            if (!StringUtils.isLongNullOrEmpty(catItemDTO.getStatus())) {
              result.setStatus(catItemDTO.getStatus().toString());
            }
            if (!StringUtils.isLongNullOrEmpty(catItemDTO.getPosition())) {
              result.setPosition(catItemDTO.getPosition().toString());
            }
            if (!StringUtils.isLongNullOrEmpty(catItemDTO.getParentItemId())) {
              result.setParentItemId(catItemDTO.getParentItemId().toString());
            }
            lstDataCobo.add(result);
          }
          if (lstDataCobo != null && !lstDataCobo.isEmpty()) {
            item.setLstDataCombo(lstDataCobo);
          }
          lstResult.add(item);
        }
      }
    }
    return lstResult;
  }

  @Override
  public JsonResponseBO getDataJSon(RequestInputBO requestInputBO) {
    log.debug("Request to getDataJSon : {}", requestInputBO);
    I18n.setLocaleForService(wsContext);
    return actionLogBusiness.getDataJson(requestInputBO);
  }

  @Override
  public List<ItemDataTT> getDataDetailColumnJSon(RequestInputBO requestInputBO) {
    log.debug("Request to getDataDetailColumnJSon : {}", requestInputBO);
    I18n.setLocaleForService(wsContext);
    Map<String, String> mapCheckColumnName = new HashMap<>();
    List<ItemDataTT> lstResult = new ArrayList<>();
    Datatable datatable = catItemBusiness.getItemMaster(TT_MASTER_CODE.TT_CFG_BUSINESS_COLUMN,
        LANGUAGUE_EXCHANGE_SYSTEM.COMMON, String.valueOf(APPLIED_BUSSINESS.CAT_ITEM),
        Constants.ITEM_ID, Constants.ITEM_NAME);
    if (datatable != null && datatable.getData() != null && datatable.getData().size() > 0) {
      for (CatItemDTO i : (List<CatItemDTO>) datatable.getData()) {
        if (!mapCheckColumnName.containsKey(i.getItemValue())) {
          ItemDataTT itemDataTT = new ItemDataTT();
          itemDataTT.setItemCode(i.getItemValue());
          itemDataTT.setItemName(i.getItemName());
          mapCheckColumnName.put(i.getItemValue(), i.getItemName());
        }
      }
    }
    JsonResponseBO jsonResponseBO = actionLogBusiness.getDataJson(requestInputBO);
    if (jsonResponseBO != null && StringUtils.isNotNullOrEmpty(jsonResponseBO.getDataJson())) {
      String data = jsonResponseBO.getDataJson();
      try {
        org.json.JSONObject jsonObject = new org.json.JSONObject(data);
        Iterator<String> keys = jsonObject.keys();
        while (keys.hasNext()) {
          String key = keys.next();
          log.info(key);
          if (jsonObject.get(key) instanceof JSONArray) {
            JSONArray array = (JSONArray) jsonObject.get(key);
            JSONObject object = (JSONObject) array.get(0);
            Iterator<String> innerKeys = object.keys();
            while (innerKeys.hasNext()) {
              String innerKey = innerKeys.next();
              Object innerValue = object.get(innerKey);
              log.info(innerKey);
              log.info(String.valueOf(innerValue));
              ItemDataTT itemDataTT = new ItemDataTT();
              itemDataTT.setItemCode(innerKey);
              itemDataTT.setItemName(innerKey);
              if (innerValue instanceof String) {
                itemDataTT.setItemValue((String) innerValue);
              } else if (innerValue instanceof Date) {
                itemDataTT
                    .setItemValue(DateUtil.date2ddMMyyyyHHMMss((Date) innerValue));
              } else {
                itemDataTT.setItemValue(String.valueOf(innerValue));
              }
              if (mapCheckColumnName.containsKey(innerKey)) {
                itemDataTT.setItemName(mapCheckColumnName.get(innerKey));
              }
              lstResult.add(itemDataTT);
            }
          }
        }
      } catch (JSONException e) {
        log.error(e.getMessage(), e);
      }
    }
    return lstResult;
  }

  private String getAlias(String typeId, String alarmGroup) {
    String alias = "";
    if (StringUtils.isNotNullOrEmpty(typeId) && StringUtils
        .isNotNullOrEmpty(alarmGroup)) { //nhan tin tt
      CatItemDTO catItem = catItemBusiness.getCatItemById(Long.parseLong(typeId));
      if (catItem != null) {
        String temp = "TT_" + catItem.getItemCode();
        catItem = catItemBusiness.getCatItemById(Long.parseLong(alarmGroup));
        if (catItem != null) {
          temp = temp + "_" + catItem.getItemCode();
          CatItemDTO catItemDTO = new CatItemDTO();
          catItemDTO.setItemCode(temp);
          catItemDTO.setStatus(1l);
          List<CatItemDTO> lstCatItem = catItemBusiness.getListCatItemDTO(catItemDTO);
          if (lstCatItem != null && !lstCatItem.isEmpty()) {
            alias = lstCatItem.get(0).getItemValue();
          }
        }
      }
    }
    if (StringUtils.isStringNullOrEmpty(alias)) {
      alias = "GNOC_TT";
    }
    return alias;
  }

  public String getSmsContenFromCfg(String userLanguage, String content,
      TroublesDTO troublesDTO) {
    String resultContent = null;
    if (StringUtils.isNotNullOrEmpty(content)) {
      String[] c = content.split("####");

      if ("2".equals(userLanguage) || StringUtils
          .isStringNullOrEmpty(userLanguage)) {
        resultContent = c.length == 2 ? c[1] : c[0];
      } else {
        resultContent = c[0];
      }
      if (StringUtils.isNotNullOrEmpty(resultContent)) {
        resultContent = replaceSmsContent(troublesDTO, resultContent);
      }
    }
    return resultContent;
  }

  public String replaceSmsContent(TroublesDTO troublesDTO, String content) {
    try {
      Field[] arrString = troublesDTO.getClass().getDeclaredFields();
      for (int i = 0; i < arrString.length; i++) {
        if (content.contains("[" + arrString[i].getName() + "]")) {
          Object value = null;
          try {
            Method method = TroublesDTO.class
                .getMethod(
                    "get" + arrString[i].getName().substring(0, 1).toUpperCase() + arrString[i]
                        .getName().substring(1));
            value = method.invoke(troublesDTO);
          } catch (Exception e) {
            log.error(e.getMessage(), e);
            value = null;
          }
          if (value != null && value != "") {
            content = content.replace("[" + arrString[i].getName() + "]", String.valueOf(value));
          } else {
            content = content.replace("[" + arrString[i].getName() + "]", " ");
          }
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return content;
  }
}
