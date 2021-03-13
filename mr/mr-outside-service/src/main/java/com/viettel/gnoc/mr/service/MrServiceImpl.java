package com.viettel.gnoc.mr.service;

import com.viettel.gnoc.commons.business.UserBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UserTokenGNOC;
import com.viettel.gnoc.commons.dto.UsersDTO;
import com.viettel.gnoc.commons.model.UsersEntity;
import com.viettel.gnoc.commons.proxy.MrCategoryProxy;
import com.viettel.gnoc.commons.proxy.WoServiceProxy;
import com.viettel.gnoc.commons.repository.BaseRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.DateUtil;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.maintenance.dto.MrCauseWoWasCompletedDTO;
import com.viettel.gnoc.maintenance.dto.MrCdBatteryDTO;
import com.viettel.gnoc.maintenance.dto.MrConfigDTO;
import com.viettel.gnoc.maintenance.dto.MrDTO;
import com.viettel.gnoc.maintenance.dto.MrDeviceDTO;
import com.viettel.gnoc.maintenance.dto.MrHisDTO;
import com.viettel.gnoc.maintenance.dto.MrNodeChecklistDTO;
import com.viettel.gnoc.maintenance.dto.MrNodeChecklistFilesDTO;
import com.viettel.gnoc.maintenance.dto.MrNodesDTO;
import com.viettel.gnoc.maintenance.dto.MrScheduleTelDTO;
import com.viettel.gnoc.maintenance.dto.MrScheduleTelHisDTO;
import com.viettel.gnoc.maintenance.dto.MrSearchDTO;
import com.viettel.gnoc.maintenance.dto.MrWoTempDTO;
import com.viettel.gnoc.mr.business.MrBusiness;
import com.viettel.gnoc.mr.business.MrCdBatteryCreateWoHisBusiness;
import com.viettel.gnoc.mr.business.MrConfigBusiness;
import com.viettel.gnoc.mr.business.MrNodesBusiness;
import com.viettel.gnoc.mr.business.MrServiceBusiness;
import com.viettel.gnoc.wo.dto.WoDTO;
import com.viettel.gnoc.ws.dto.UserGroupCategoryDTO;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author tripm
 * @version 2.0
 * @since 12/06/2020 09:31:00
 */
@Service
@Slf4j
public class MrServiceImpl extends BaseRepository implements MrService {

  @Autowired
  MrServiceBusiness mrServiceBusiness;

  @Autowired
  MrConfigBusiness mrConfigBusiness;

  @Autowired
  WoServiceProxy woServiceProxy;

  @Autowired
  MrBusiness mrBusiness;

  @Autowired
  MrCategoryProxy mrCategoryProxy;

  @Autowired
  UserBusiness userBusiness;

  @Autowired
  MrCdBatteryCreateWoHisBusiness mrCdBatteryCreateWoHisBusiness;

  @Autowired
  MrNodesBusiness mrNodesBusiness;

  @Resource
  WebServiceContext wsContext;

  @Override
  public List<MrNodeChecklistDTO> getListMrNodeChecklistForPopUp_VS(String woId, String mrNodeId) {
    log.info("Request to getListMrNodeChecklistForPopUp_VS : {}", woId, mrNodeId);
    I18n.setLocaleForService(wsContext);
    return mrBusiness.getListMrNodeChecklistForPopUp_VS(woId, mrNodeId);
  }

  @Override
  public ResultDTO updateMrNodeChecklistForPopUp_VS(
      List<MrNodeChecklistDTO> lstMrNodeChecklistDTO) throws Exception {
    log.info("Request to updateMrNodeChecklistForPopUp_VS : {}");
    I18n.setLocaleForService(wsContext);
    return mrBusiness.updateMrNodeChecklistForPopUp_VS(lstMrNodeChecklistDTO);
  }

  @Override
  public List<MrNodesDTO> getWoCrNodeList_VS(String woId, String crId) {
    log.info("Request to getWoCrNodeList_VS : {}", woId, crId);
    I18n.setLocaleForService(wsContext);
    return mrBusiness.getWoCrNodeList_VS(woId, crId);
  }

  @Override
  public ResultDTO updateWoCrNodeStatus(List<MrNodesDTO> lstNodes) {
    log.info("Request to updateWoCrNodeStatus : {}", lstNodes);
    I18n.setLocaleForService(wsContext);
    return mrBusiness.updateWoCrNodeStatus(lstNodes);
  }

  /**
   * @author Dunglv3
   */
  @Override
  public String updateWODischargeBattery(List<MrNodesDTO> listMrNodeDTO) {
    log.info("Request to updateWODischargeBattery : {}", listMrNodeDTO);
    I18n.setLocaleForService(wsContext);
    String res = "";
    for (MrNodesDTO node : listMrNodeDTO) {
      if (StringUtils.isStringNullOrEmpty(node.getNodeIp())) {
        //node ip tuong ung vs STATION_CODE
        return RESULT.FAIL + ": " + I18n.getLanguage("mrService.nodeIP.notnull");
      }
      if (StringUtils.isStringNullOrEmpty(node.getNodeName())) {
        //node name tuong ung vs DC_POWER
        return RESULT.FAIL + ": " + I18n.getLanguage("mrService.nodeName.notnull");
      }
      if (StringUtils.isStringNullOrEmpty(node.getWoId())) {
        return RESULT.FAIL + ": " + I18n.getLanguage("mrService.woID.notnull");
      }
      if (StringUtils.isNotNullOrEmpty(node.getRecentDischargeCd())) {
        Date recentDischargeCd = DateUtil
            .string2DateByPattern(node.getRecentDischargeCd(), "dd/MM/yyyy HH:mm:ss");
        if (recentDischargeCd == null) {
          return RESULT.FAIL + ": " + I18n.getLanguage("mrService.recentDischargeCd.invalid");
        }
      }
    }
    try {
      for (MrNodesDTO item : listMrNodeDTO) {
        List<MrNodesDTO> lstUpdate = new ArrayList<>();
        lstUpdate.add(item);
        res = mrBusiness.updateWODischargeBattery(lstUpdate);
        if (!RESULT.SUCCESS.equals(res)) {
          res = RESULT.FAIL + I18n.getLanguage("mrService.updateNodeStatus.false");
          return res;
        }
//        String closedWO = mrBusiness.closeWO(lstUpdate);
//        if (!RESULT.SUCCESS.equals(closedWO)) {
//          res = RESULT.FAIL + ": " + closedWO;//fix on 12 June 2019
//        }
        else {
          //update mr_code, wo_code, RECENT_DISCHARGE_CD
          ResultDTO resU = mrBusiness.updateMrAndWoCDBattery(item.getWoId(), DateUtil
                  .string2DateByPattern(item.getRecentDischargeCd(), "dd/MM/yyyy HH:mm:ss"),
              item.getNodeName(), item.getNodeIp());
          res = resU.getMessage();
          if (!RESULT.SUCCESS.equals(res)) {
            res =
                RESULT.FAIL + ": " + I18n.getLanguage("mrService.updateWoMrCode.MrCdBattery.false");
            return res;
          }
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage());
      res = RESULT.FAIL + ": " + e.getMessage();
    }
    return res;
  }

  /**
   * @author Dunglv3
   */
  @Override
  public ResultDTO createWoMrCdBatteryForVSmart(String stationCode, String dcPower) {
    log.info("Request to createWoMrCdBatteryForVSmart : {}", stationCode, dcPower);
    I18n.setLocaleForService(wsContext);
    ResultDTO res = new ResultDTO();
    res.setId("0");
    res.setKey(Constants.RESULT.FAIL);
    try {
      if (StringUtils.isStringNullOrEmpty(stationCode)) {
        res.setMessage(I18n.getLanguage("mrService.stationCode.notnull"));
        return res;
      }
      if (StringUtils.isStringNullOrEmpty(dcPower)) {
        res.setMessage(I18n.getLanguage("mrService.dcPower.notnull"));
        return res;
      }
      MrCdBatteryDTO searchDto = new MrCdBatteryDTO();
      searchDto.setStationCode(stationCode);
      searchDto.setDcPower(dcPower);
      List<MrCdBatteryDTO> lstMrCdBattery = mrServiceBusiness
          .getListMrCdBattery(searchDto);
      if (lstMrCdBattery == null || lstMrCdBattery.isEmpty()) {
        res.setMessage(I18n.getLanguage("mrService.stationCodeDcPower.notfound"));
        return res;
      }
      for (MrCdBatteryDTO mrCdBatteryDTO : lstMrCdBattery) {
        if (!StringUtils.isStringNullOrEmpty(mrCdBatteryDTO.getWoCode())) {
          res.setMessage(I18n.getLanguage("mrService.stationCodeDcPower.woExisted"));
          return res;
        }
        //dungpv 25/08/2020 bo sun kiem tra IsWoAccu
        if (!StringUtils.isStringNullOrEmpty(mrCdBatteryDTO.getIswoAccu()) && "0"
            .equals(mrCdBatteryDTO.getIswoAccu())) {
          res.setMessage(I18n.getLanguage("mrService.IsWoAccu.noconfig"));
          return res;
        }
        //namtn edit on July 08 2019
//        if ("1".equals(mrCdBatteryDTO.getDischargeType())) {
//          res.setMessage(I18n.getLanguage("mrService.invalidDischargeType"));
//          return res;
//        }
//        if (StringUtils.isNotNullOrEmpty(mrCdBatteryDTO.getIswoAccu()) && "0"
//            .equals(mrCdBatteryDTO.getIswoAccu())) {
//          res.setMessage(I18n.getLanguage("mrService.IsWoAccu.noconfig"));
//          return res;
//        }
//        if (!StringUtils.isStringNullOrEmpty(mrCdBatteryDTO.getRecentDischargeCd())) {
//          try {
//            Date recentDischargeCd = DateTimeUtils
//                .convertStringToTime(mrCdBatteryDTO.getRecentDischargeCd(), "yyyy-MM-dd");
//            Calendar nowMinus = Calendar.getInstance();
//            nowMinus.add(Calendar.MONTH, -1);
//            nowMinus.set(Calendar.HOUR, 0);
//            nowMinus.set(Calendar.MINUTE, 0);
//            nowMinus.set(Calendar.SECOND, 0);
//            if (DateTimeUtils.compareDateTime(nowMinus.getTime(), recentDischargeCd) == -1) {
//              res.setMessage(I18n.getLanguage("mrService.recentDischargeCd.error"));
//              return res;
//            }
//          } catch (Exception e) {
//            log.error(e.getMessage());
//            res.setMessage(e.getMessage());
//            return res;
//          }
//        }
        if (!mrCdBatteryCreateWoHisBusiness.isValidCreateWo(stationCode, dcPower)) {
          res.setMessage(I18n.getLanguage("mrService.notValidCreateWo.error"));
          return res;
        }
        //namtn edit on July 08 2019
      }

      MrCdBatteryDTO mrCdBatteryDto = lstMrCdBattery.get(0);
      List<MrConfigDTO> lstConfig = null;
      if ("2".equals(mrCdBatteryDto.getDischargeType())) {
        lstConfig = mrConfigBusiness
            .getConfigByGroup(Constants.MR_CONFIG.TEST_XA_ACQUY_BAN_TU_DONG);
      } else if ("1".equals(mrCdBatteryDto.getDischargeType())) {
//        lstConfig = mrConfigBusiness.getConfigByGroup(Constants.MR_CONFIG.TEST_XA_ACQUY_TU_DONG);
        res.setMessage(I18n.getLanguage("mrService.invalidDischargeType"));
      }

      if (lstConfig == null) {
        res.setMessage(I18n.getLanguage("mrService.invalidDischargeType2"));
        return res;
      }

      //Tao Mr
      MrDTO mrDTO = createMr(mrCdBatteryDto, lstConfig);
      if (mrDTO == null) {
        res.setMessage(I18n.getLanguage("mrService.createMr.error"));
        return res;
      }

      //Tao Wo
      log.info("Bat dau tao WO");
      WoDTO woDTO = createWo(mrCdBatteryDto, lstConfig, mrDTO);
      String logWo = "Wo Content :" + woDTO.getWoContent() + "\n"
          + "Wo FT :" + woDTO.getFtId() + "\n"
          + "Wo CD :" + woDTO.getCdId() + "\n"
          + "Wo CREATE_PERSON_WO :" + woDTO.getCreatePersonId();
      log.info(logWo);
      if (StringUtils.isNotNullOrEmpty(I18n.getLocale())) {
        woDTO.setProxyLocale(I18n.getLocale());
      }
      List<WoDTO> lstWoReSult = new ArrayList<>();

      ResultDTO resultWo = woServiceProxy.createWoProxy(woDTO);
      if (resultWo != null) {
        if (resultWo.getMessage().equalsIgnoreCase(Constants.RESULT.SUCCESS)) {
          System.out.println("Tao WO thanh cong: " + resultWo.getId());
          String woCode = resultWo.getId();
          woDTO.setWoCode(woCode);
          String[] temps = resultWo.getId().split("_");
          String woId = temps[temps.length - 1];
          woDTO.setWoId(woId);
          lstWoReSult.add(woDTO);
          List<MrNodesDTO> lstNodes = new ArrayList<>();
          MrNodesDTO mrNodesDTO = new MrNodesDTO();
          mrNodesDTO.setNodeCode(String.valueOf(mrCdBatteryDto.getDcPowerId()));
          mrNodesDTO.setNodeName(mrCdBatteryDto.getDcPower());
          mrNodesDTO.setNodeIp(mrCdBatteryDto.getStationCode());
          mrNodesDTO.setMrId(mrDTO.getMrId());
          mrNodesDTO.setWoId(woId);
          mrNodesDTO.setStatus("0");
          lstNodes.add(mrNodesDTO);
          ResultInSideDto resNode = mrServiceBusiness.insertList(lstNodes);
          if (!Constants.RESULT.SUCCESS.equals(resNode.getKey())) {
            res.setMessage(I18n.getLanguage("mrService.createNodes.error"));
            woServiceProxy.deleteWo(Long.valueOf(woId));
          }
          System.out.println("Bat dau tao MR");
          UsersEntity usersEntity = mrServiceBusiness
              .getUserByUserId(Long.valueOf(mrDTO.getCreatePersonId()));
          UsersDTO usersDTO = new UsersDTO();
          if (usersEntity != null) {
            usersDTO.setUserId(String.valueOf(usersEntity.getUserId()));
            usersDTO.setUsername(usersEntity.getUsername());
            usersDTO.setUnitId(String.valueOf(usersEntity.getUnitId()));
          }
          List<MrWoTempDTO> lstMrWoTemp = createMrWoTempDTO(lstWoReSult, mrDTO);
          ResultDTO resultMr = mrBusiness.createMrDTO(mrDTO, usersDTO, lstMrWoTemp);
          if (resultMr != null && resultMr.getMessage()
              .equalsIgnoreCase(Constants.RESULT.SUCCESS)) {
            mrCdBatteryDto.setWoCode(woDTO.getWoCode());
            mrCdBatteryDto.setMrCode(mrDTO.getMrCode());
            log.info("Tao Mr thanh cong " + mrDTO.getMrId());

            ResultDTO resUpdate = mrServiceBusiness.updateWoCodeMrCode(mrCdBatteryDto);
            if (!"SUCCESS".equals(resUpdate.getKey())) {
              String logSUCCESS =
                  "Update Mr code, Wo code that bai " + resUpdate.getMessage() + "\n"
                      + "Ma Mr: " + mrCdBatteryDto.getMrCode() + "\n"
                      + "Ma Wo: " + mrCdBatteryDto.getWoCode() + "\n"
                      + "Station code: " + mrCdBatteryDto.getStationCode() + "\n"
                      + "Dc power: " + mrCdBatteryDto.getDcPower();
              log.info(logSUCCESS);
              res.setMessage(I18n.getLanguage("mrService.updateMrWoFailed")
                  .replace("{0}", woDTO.getWoCode())
                  .replace("{1}", mrDTO.getMrCode())
                  .replace("{2}", stationCode)
                  .replace("{3}", dcPower)
              );
              try {
                log.info("Xoa MR, WO, data trong MR_NODES");
                woServiceProxy.deleteWo(Long.valueOf(woId));
                mrBusiness.delete(Long.valueOf(mrDTO.getMrId()));
                mrNodesBusiness.delete(Long.valueOf(resNode.getId()));
              } catch (Exception e) {
                log.error(e.getMessage());
              }
              return res;
            }

            //insert vao bang MR_SCHEDULE_PERIODIC
//            try {
//              MrSchedulePeriodicDTO mrPeriodicDto = new MrSchedulePeriodicDTO();
//              mrPeriodicDto.setMrId(mrDTO.getMrId());
//              mrPeriodicDto.setWoId(woId);
//              mrPeriodicDto.setUserId(mrDTO.getCreatePersonId());
//              mrPeriodicDto.setTimeInsert(DateTimeUtils.getSysdate());
//              mrPeriodicDto.setTimeWoStart(woDTO.getStartTime());
//              mrPeriodicDto.setTimeWoEnd(woDTO.getEndTime());
//              mrPeriodicDto.setPosition("1");
//              mrPeriodicDto.setIsSend("1");
//              mrPeriodicDto.setWoTempId(woId);
//              mrCategoryProxy.insertMrSchedulePeriodic(mrPeriodicDto);
//            } catch (Exception e) {
//              log.error(e.getMessage());
//              System.out.println("Update bang MR_SCHEDULE_PERIODIC " + e.getMessage());
//            }

            res.setId(woCode);
            res.setKey(Constants.RESULT.SUCCESS);
            res.setMessage(Constants.RESULT.SUCCESS);
          } else {
            woServiceProxy.deleteWo(Long.valueOf(woDTO.getWoId()));
            res.setMessage(I18n.getLanguage("mrService.createMr.error"));
            return res;
          }
        } else {
          res.setMessage(
              I18n.getLanguage("mrService.createWo.error") + ": " + resultWo.getMessage());
          return res;
        }
      }
    } catch (Exception e) {
      res.setId("0");
      res.setKey(Constants.RESULT.FAIL);
      res.setMessage(e.getMessage());
      log.error(e.getMessage());
    }
    return res;
  }

  @Override
  public ResultDTO validateWo(String woId, String woType) {
    I18n.setLocaleForService(wsContext);
    if (StringUtils.isStringNullOrEmpty(woId)) {
      return new ResultDTO(Constants.RESULT.FAIL, Constants.RESULT.FAIL,
          I18n.getLanguage("mrService.woID.notnull"));
    } else {
      boolean isOk = mrBusiness.checkNodeStatusByWo(woId);
      if (isOk) {
        return new ResultDTO(Constants.RESULT.SUCCESS, Constants.RESULT.SUCCESS,
            Constants.RESULT.SUCCESS);
      }
      return new ResultDTO(Constants.RESULT.FAIL, Constants.RESULT.FAIL,
          I18n.getLanguage("mr.cd.battery.woHaveNodeNotFinish"));
    }
  }

  @Override
  public List<UserGroupCategoryDTO> getListUserGroupBySystem(UserGroupCategoryDTO lstCondition) {
    I18n.setLocaleForService(wsContext);
    if (StringUtils.isNotNullOrEmpty(I18n.getLocale())) {
      lstCondition.setProxyLocale(I18n.getLocale());
    }
    return mrServiceBusiness.getListUserGroupBySystem(lstCondition);
  }

  @Override
  public List<MrDTO> getWorklogFromWO(MrSearchDTO mrDTO, int rowStart, int maxRow) {
    I18n.setLocaleForService(wsContext);
    mrDTO.setProxyLocale(I18n.getLocale());
    List<MrDTO> lstMr = mrCategoryProxy.getWorklogFromWo(mrDTO);
    if (rowStart == 0 && maxRow == 0) {
      return lstMr;
    }
    if (lstMr != null && lstMr.size() >= rowStart) {
      List<MrDTO> lstResult = new ArrayList<>();
      if (lstMr.size() < maxRow) {
        for (int i = rowStart; i < lstMr.size(); i++) {
          lstResult.add(lstMr.get(i));
        }
      } else {
        maxRow = maxRow + rowStart;
        for (int i = rowStart; i < maxRow; i++) {
          lstResult.add(lstMr.get(i));
        }
      }
      return lstResult;
    }
    return lstMr;
  }

  @Override
  public ResultDTO updateMrStatus(String crId, String woId) {
    return mrCategoryProxy.updateMrStatus(crId, woId);
  }

  @Override
  public ResultDTO updateWoStatus(String woId, String status) {
    ResultDTO res = new ResultDTO();
    try {
      if (!StringUtils.isStringNullOrEmpty(woId)) {
        if (!StringUtils.isStringNullOrEmpty(status)) {
          if ("1".equals(status) || "2".equals(status)) {
            ResultInSideDto resultInSideDto = mrNodesBusiness
                .updateWoStatus(woId, null, null, status, null, true);
            if (RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
              res.setId(StringUtils.isLongNullOrEmpty(resultInSideDto.getId()) ? null
                  : String.valueOf(resultInSideDto.getId()));
              res.setKey(resultInSideDto.getKey());
              res.setMessage(resultInSideDto.getMessage());
              if ("1".equals(status)) {
                List<MrNodesDTO> lstNodes = mrCategoryProxy
                    .getWoCrNodeList(woId, null);
                if (lstNodes.size() > 0) {
                  String mrId = lstNodes.get(0).getMrId();
                  // Dong Mr
                  boolean isOK = mrBusiness.updateMrState(mrId, "6", res);
                  if (isOK) {
                    MrHisDTO mrHisDTO = new MrHisDTO();
                    mrHisDTO.setStatus("6");
                    mrHisDTO.setChangeDate(DateTimeUtils.convertDateToString(new Date()));
                    mrHisDTO.setComments("Đóng MR");
                    mrHisDTO.setMrId(mrId);
                    mrHisDTO.setNotes("Đóng MR sau khi WO hoàn thành");
                    mrHisDTO.setActionCode("4");

                    // Cap nhat du lieu vao bang MR_HIS
                    mrBusiness.insertMrHis(mrHisDTO);

                    List<MrScheduleTelDTO> lsScheduleTel = mrBusiness.getByMrId(mrId);
                    if (lsScheduleTel != null && !lsScheduleTel.isEmpty()) {
                      for (MrScheduleTelDTO mrScheduleTelDTO : lsScheduleTel) {
                        // Insert du lieu vao bang HIS
                        MrScheduleTelHisDTO mrScheduleTelHisDTO = new MrScheduleTelHisDTO();

                        mrScheduleTelHisDTO.setMarketCode(mrScheduleTelDTO.getMarketCode());
                        mrScheduleTelHisDTO.setArrayCode(mrScheduleTelDTO.getArrayCode());
                        mrScheduleTelHisDTO.setDeviceType(mrScheduleTelDTO.getDeviceType());
                        mrScheduleTelHisDTO.setDeviceId(
                            StringUtils.isLongNullOrEmpty(mrScheduleTelDTO.getDeviceId()) ? null
                                : String.valueOf(mrScheduleTelDTO.getDeviceId()));
                        mrScheduleTelHisDTO.setDeviceCode(mrScheduleTelDTO.getDeviceCode());
                        mrScheduleTelHisDTO.setDeviceName(mrScheduleTelDTO.getDeviceName());
                        mrScheduleTelHisDTO
                            .setMrDate(DateTimeUtils.date2ddMMyyyyString(new Date()));
                        mrScheduleTelHisDTO.setMrContent(mrScheduleTelDTO.getMrContentId());
                        mrScheduleTelHisDTO.setMrMode(mrScheduleTelDTO.getMrMode());
                        mrScheduleTelHisDTO.setMrType(mrScheduleTelDTO.getMrType());
                        mrScheduleTelHisDTO.setMrId(
                            StringUtils.isLongNullOrEmpty(mrScheduleTelDTO.getMrId()) ? null
                                : String.valueOf(mrScheduleTelDTO.getMrId()));
                        mrScheduleTelHisDTO.setMrCode(mrScheduleTelDTO.getMrCode());
                        mrScheduleTelHisDTO.setCrId(
                            StringUtils.isLongNullOrEmpty(mrScheduleTelDTO.getCrId()) ? null
                                : String.valueOf(mrScheduleTelDTO.getCrId()));
                        mrScheduleTelHisDTO.setCrNumber(mrScheduleTelDTO.getCrNumber());
                        mrScheduleTelHisDTO.setProcedureId(
                            StringUtils.isLongNullOrEmpty(mrScheduleTelDTO.getProcedureId()) ? null
                                : String.valueOf(mrScheduleTelDTO.getProcedureId()));
                        mrScheduleTelHisDTO.setProcedureName(mrScheduleTelDTO.getProcedureName());

                        ResultDTO objResultHis = mrBusiness
                            .insertMrScheduleTelHis(mrScheduleTelHisDTO);
                        if (RESULT.SUCCESS.equals(objResultHis.getKey())) {
                          // Xoa du lieu o bang MR_SCHEDULE
                          mrBusiness.deleteMrScheduleTel(mrScheduleTelDTO.getScheduleId());
                        }
                        // Cap nhat bang Mr_Device
                        updateStatus(mrScheduleTelDTO, "1");
                      }
                    }
                  }
                }
              }
            }
          } else {
            res = new ResultDTO("0", RESULT.FAIL, "status : Invalid");
          }
        } else {
          res = new ResultDTO("0", RESULT.FAIL, "status : Empty");
        }
      } else {
        res = new ResultDTO("0", RESULT.FAIL, "woId : Empty");
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      res = new ResultDTO("0", RESULT.FAIL, e.getMessage());
    }
    return res;
  }

  public void updateStatus(MrScheduleTelDTO objScheduleTel, String status) {
    try {
      MrDeviceDTO objUpdate = mrBusiness.findDetailById(Long.valueOf(objScheduleTel.getDeviceId()));
      if (objUpdate != null) {
        if ("1".equals(objScheduleTel.getMrHard())) {
          if ("1".equals(objScheduleTel.getMrHardCycle())) {
            objUpdate.setIsComplete1m(Long.valueOf(status));
            objUpdate.setLastDate1m(new Date());
          } else if ("3".equals(objScheduleTel.getMrHardCycle())) {
            objUpdate.setIsComplete3m(Long.valueOf(status));
            objUpdate.setLastDate3m(new Date());
          } else if ("6".equals(objScheduleTel.getMrHardCycle())) {
            objUpdate.setIsComplete6m(Long.valueOf(status));
            objUpdate.setLastDate6m(new Date());
          } else if ("12".equals(objScheduleTel.getMrHardCycle())) {
            objUpdate.setIsComplete12m(Long.valueOf(status));
            objUpdate.setLastDate12m(new Date());
          }
        } else {
          objUpdate.setIsCompleteSoft(Long.valueOf(status));
          objUpdate.setLastDate(new Date());
        }
        objUpdate.setUpdateDate(new Date());
        mrBusiness.updateMrDeviceDTO(objUpdate);
      }
    } catch (Exception e) {
      throw e;
    }
  }

  /**
   * @author Dunglv3
   */
  private MrDTO createMr(MrCdBatteryDTO mrCdBetteryDTO, List<MrConfigDTO> lstConfig) {
    MrDTO mrDTO = new MrDTO();
    try {
      String cfgArrayCode = "";
      String cfgCreateMrUser = "";
      String cfgMrTitle = "";
      String cfgMrTime = "1";
      String cfgPriority = "";
      String cfgImpact = "";
      String cfgMrWorks = "";
      String cfgMrType = "";
      String cfgIsServiceAffected = "0";
      String cfgTectnichcal = "TBM";
      if (lstConfig != null && lstConfig.size() > 0) {
        for (MrConfigDTO item : lstConfig) {
          if (Constants.MR_CONFIG.TEST_XA_MR_ARRAY_CODE.equals(item.getConfigCode())) {
            cfgArrayCode = item.getConfigValue();
          } else if (Constants.MR_CONFIG.TEST_XA_MR_CREATE_USER.equals(item.getConfigCode())) {
            cfgCreateMrUser = item.getConfigValue();
          } else if (Constants.MR_CONFIG.TEST_XA_MR_TITLE.equals(item.getConfigCode())) {
            cfgMrTitle = item.getConfigValue();
          } else if (Constants.MR_CONFIG.TEST_XA_MR_TIME.equals(item.getConfigCode())) {
            cfgMrTime = item.getConfigValue();
          } else if (Constants.MR_CONFIG.TEST_XA_MR_PRIORITY.equals(item.getConfigCode())) {
            cfgPriority = item.getConfigValue();
          } else if (Constants.MR_CONFIG.TEST_XA_MR_IMPACT.equals(item.getConfigCode())) {
            cfgImpact = item.getConfigValue();
          } else if (Constants.MR_CONFIG.TEST_XA_MR_WORKS.equals(item.getConfigCode())) {
            cfgMrWorks = item.getConfigValue();
          } else if (Constants.MR_CONFIG.TEST_XA_MR_TYPE.equals(item.getConfigCode())) {
            cfgMrType = item.getConfigValue();
          } else if (Constants.MR_CONFIG.TEST_XA_MR_IS_SV_AFFECTED.equals(item.getConfigCode())) {
            cfgIsServiceAffected = item.getConfigValue();
          } else if (Constants.MR_CONFIG.TEST_XA_MR_TECHNICHCAL.equals(item.getConfigCode())) {
            cfgTectnichcal = item.getConfigValue();
          }
        }
      }

      // Ngày bắt đầu MR la ngày bắt đầu lịch bảo dưỡng
      Date mrStartDate = cdBatteryMrStartDate(mrCdBetteryDTO);

      //Ngày kết thúc MR = Ngày bắt đầu + thời gian thực hiện MR
      Date mrEndDate = DateUtil.addDay(mrStartDate, Integer.parseInt(cfgMrTime));
      //Tên MR
      mrDTO.setEarliestTime(DateTimeUtils.convertDateTimeStampToString(mrStartDate));
      mrDTO.setLastestTime(DateTimeUtils.convertDateTimeStampToString(mrEndDate));
      //Mr id
      List<String> lstSequence = getListSequense("mr_seq", 1);
      String sequence = lstSequence.get(0);
      mrDTO.setMrId(sequence);
      Date createTime = new Date();

      //Mo ta MR
      mrDTO.setCreatedTime(DateTimeUtils.convertDateTimeToString(createTime));
      //Mr code
      String mrCode =
          "MR_BDC_" + DateTimeUtils.convertDateToString(new Date(), "yyyyMMddHH") + "_" + sequence;
      mrDTO.setMrCode(mrCode);
      //Trang thai
      mrDTO.setState(Constants.MR_STATE_CODE.INACTIVE);
      mrDTO.setPriorityCode(cfgPriority);
      mrDTO.setImpact(cfgImpact);

      //anh huong dich vu
      mrDTO.setIsServiceAffected(cfgIsServiceAffected);
      //mang(subcategory)
      mrDTO.setSubcategory(cfgArrayCode);
      mrDTO.setMrTechnichcal(cfgTectnichcal);

      String userName = cfgCreateMrUser;
      UserTokenGNOC udto = mrServiceBusiness.getUserInfor(userName);
      if (udto != null) {
        mrDTO.setUnitExecute(udto.getUnitCode());
        mrDTO.setCreatePersonId(udto.getUserId().toString());
        mrDTO.setAssignToPerson(udto.getUserId().toString());
      } else {
        return null;
      }
      mrDTO.setCycle("2".equals(mrCdBetteryDTO.getDischargeType()) ? "6"
          : ("1".equals(mrCdBetteryDTO.getProductionTechnology()) ? "1" : "3"));

      mrDTO.setNodeName(null);
      mrDTO.setNodeType(null);
      mrDTO.setDefaultSortField(null);
      if (mrCdBetteryDTO.getStationCode() == null) {
        mrDTO.setMrTitle(cfgMrTitle + " (" + mrCdBetteryDTO.getProvince() + ")");
      } else {
        mrDTO.setMrTitle(cfgMrTitle + " (" + mrCdBetteryDTO.getStationCode() + ")");
      }

      mrDTO.setDescription(mrDTO.getMrTitle());
      mrDTO.setCountry("281");
      mrDTO.setMrWorks(cfgMrWorks);
      mrDTO.setMrType(cfgMrType);
    } catch (Exception e) {
      log.error(e.getMessage());
      return null;
    }
    return mrDTO;
  }

  /**
   * @author Dunglv3
   */
  private WoDTO createWo(MrCdBatteryDTO mrCdBetteryDTO, List<MrConfigDTO> lstConfig, MrDTO mrDTO) {
    WoDTO woDTO = new WoDTO();
    try {
      String cfgWoContent = "";
      String cfgWoDes = "";
      String cfgWoTypeId = "";
      String cfgWoCreateUser = "";
      String cfgWoPriority = "";
      String cfgWoStatus = "";

      if (lstConfig != null && lstConfig.size() > 0) {
        for (MrConfigDTO item : lstConfig) {
          if (Constants.MR_CONFIG.TEST_XA_WO_CONTENT.equals(item.getConfigCode())) {
            cfgWoContent = item.getConfigValue();
          } else if (Constants.MR_CONFIG.TEST_XA_WO_DES.equals(item.getConfigCode())) {
            cfgWoDes = item.getConfigValue();
          } else if (Constants.MR_CONFIG.TEST_XA_WO_TYPE_ID.equals(item.getConfigCode())) {
            cfgWoTypeId = item.getConfigValue();
          } else if (Constants.MR_CONFIG.TEST_XA_WO_CREATE_USER.equals(item.getConfigCode())) {
            cfgWoCreateUser = item.getConfigValue();
          } else if (Constants.MR_CONFIG.TEST_XA_WO_PRIORITY.equals(item.getConfigCode())) {
            cfgWoPriority = item.getConfigValue();
          } else if (Constants.MR_CONFIG.TEST_XA_WO_STATUS.equals(item.getConfigCode())) {
            cfgWoStatus = item.getConfigValue();
          }
        }
      }

      String system = "MR";
      woDTO.setWoSystem(system);
      woDTO.setParentId(null);
      woDTO.setStatus(cfgWoStatus);

      woDTO.setCreatePersonId(cfgWoCreateUser);
      woDTO.setWoTypeId(cfgWoTypeId);
//      woDTO.setCdId();

      woDTO.setStartTime(mrDTO.getEarliestTime());
      woDTO.setEndTime(mrDTO.getLastestTime());

      woDTO.setCreateDate(DateTimeUtils.convertDateTimeToString(new Date()));
      woDTO.setPriorityId(cfgWoPriority);//muc uu tien

      // wo đính kèm mr
      woDTO.setWoSystemId(mrDTO.getMrCode());
      String userName = mrCdBetteryDTO.getStaffMail();
      //fix cung
//      woDTO.setCdId("897");

      if ("2".equals(mrCdBetteryDTO.getDischargeType())) {
        woDTO.setFtId(userName.trim());
        woDTO.setStationCode(mrCdBetteryDTO.getStationCode());
        woDTO.setWoContent(cfgWoContent + " " + mrCdBetteryDTO.getStationCode());
        woDTO.setWoDescription(cfgWoDes + " " + mrCdBetteryDTO.getStationCode());
      } else {
        woDTO.setFtId(userName.trim());
        woDTO.setStationCode(mrCdBetteryDTO.getStationCode());
        woDTO.setWoContent(
            cfgWoContent + " (" + mrCdBetteryDTO.getStationCode() + ": " + mrCdBetteryDTO
                .getDcPower() + ")");
        woDTO.setWoDescription(
            cfgWoDes + " (" + mrCdBetteryDTO.getStationCode() + ": " + mrCdBetteryDTO.getDcPower()
                + ")");
      }

    } catch (Exception e) {
      log.error(e.getMessage());
    }
    return woDTO;
  }

  /**
   * @author Dunglv3
   */
  private List<MrWoTempDTO> createMrWoTempDTO(List<WoDTO> lstWo, MrDTO mrDTO) {
    List<MrWoTempDTO> lst = new ArrayList<>();
    for (WoDTO woDTO : lstWo) {
      MrWoTempDTO mrWoTempDTO = new MrWoTempDTO();
      mrWoTempDTO.setWoWfmId(woDTO.getWoId());
      mrWoTempDTO.setWoCode(woDTO.getWoCode());
      mrWoTempDTO.setWoContent(woDTO.getWoContent());
      mrWoTempDTO.setWoSystem(woDTO.getWoSystem());
      mrWoTempDTO.setWoSystemId(mrDTO.getMrCode());
      mrWoTempDTO.setCreateDate(woDTO.getCreateDate());
      mrWoTempDTO.setWoTypeId(woDTO.getWoTypeId());
      mrWoTempDTO.setCreatePersonId(woDTO.getCreatePersonId());
      mrWoTempDTO.setCdId(woDTO.getCdId());
      mrWoTempDTO.setStatus(woDTO.getStatus());
      mrWoTempDTO.setPriorityId(woDTO.getPriorityId());
      mrWoTempDTO.setStartTime(woDTO.getStartTime());
      mrWoTempDTO.setEndTime(woDTO.getEndTime());
      mrWoTempDTO.setStationCode(woDTO.getStationCode());
      mrWoTempDTO.setWoDescription(woDTO.getWoDescription());
      mrWoTempDTO.setParentId(mrDTO.getMrId());
      lst.add(mrWoTempDTO);
    }
    return lst;
  }
  //dunglv edit

  /**
   * @author Dunglv3
   */
  private Date cdBatteryMrStartDate(MrCdBatteryDTO mrCdBetteryDTO) throws Exception {
    Calendar today = Calendar.getInstance();
    today.setTime(new Date());
    today.add(Calendar.DATE, 1);

//        if ("1".equals(mrCdBetteryDTO.getDischarge_type())) {
//            // Bảo dưỡng tự động => today + 1
//            return today.getTime();
//        }

    if (mrCdBetteryDTO.getRecentDischageGnoc() != null) {
      Calendar cal = Calendar.getInstance();
      cal.setTime(DateTimeUtils.convertStringToDateTime(mrCdBetteryDTO.getRecentDischageGnoc()));
      //neu la ban tu dong thi cycle = 6
      //neu la tu dong thi check production_technology = 1 thi cycle = 1, production_technology != 1 thi cycle = 3
      cal.add(Calendar.MONTH, ("2".equals(mrCdBetteryDTO.getDischargeType())) ? 6
          : ("1".equals(mrCdBetteryDTO.getProductionTechnology()) ? 1 : 3));

      if (new Date().compareTo(cal.getTime()) > 0) {
        return today.getTime();
      } else {
        return cal.getTime();
      }
    } else {
      return today.getTime();
    }
  }

  @Override
  public List<MrCauseWoWasCompletedDTO> getReasonWO(String reasonTypeId) {
    List<MrCauseWoWasCompletedDTO> data = new ArrayList<>();
    try {
      data = mrBusiness.getReasonWO(reasonTypeId);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return data;
  }

  @Override
  public List<MrNodeChecklistFilesDTO> getListFileMrNodeChecklist_VS(String nodeChecklistId) {
    return mrBusiness.getListFileMrNodeChecklist_VS(nodeChecklistId);
  }
}
