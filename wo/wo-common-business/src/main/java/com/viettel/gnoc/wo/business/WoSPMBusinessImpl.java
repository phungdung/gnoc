package com.viettel.gnoc.wo.business;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.MessagesDTO;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.commons.model.UsersEntity;
import com.viettel.gnoc.commons.repository.CommonRepository;
import com.viettel.gnoc.commons.repository.MessagesRepository;
import com.viettel.gnoc.commons.repository.UserRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.wo.dto.Wo;
import com.viettel.gnoc.wo.dto.WoDTO;
import com.viettel.gnoc.wo.dto.WoDTOSearch;
import com.viettel.gnoc.wo.dto.WoDetailDTO;
import com.viettel.gnoc.wo.dto.WoHistoryInsideDTO;
import com.viettel.gnoc.wo.dto.WoInsideDTO;
import com.viettel.gnoc.wo.dto.WoPendingDTO;
import com.viettel.gnoc.wo.repository.WoDetailRepository;
import com.viettel.gnoc.wo.repository.WoHistoryRepository;
import com.viettel.gnoc.wo.repository.WoPendingRepository;
import com.viettel.gnoc.wo.repository.WoRepository;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class WoSPMBusinessImpl implements WoSPMBusiness {

  @Value("${application.SmsGatewayId:5}")
  private String smsGatewayId;

  @Value("${application.SenderId:400}")
  private String senderId;

  @Autowired
  WoRepository woRepository;

  @Autowired
  UserRepository userRepository;

  @Autowired
  WoDetailRepository woDetailRepository;

  @Autowired
  MessagesRepository messagesRepository;

  @Autowired
  WoHistoryRepository woHistoryRepository;

  @Autowired
  CommonRepository commonRepository;

  @Autowired
  WoPendingRepository woPendingRepository;

  @Override
  public ResultDTO updateHelpFromSPM(String woCode, String description, Long result) {
    ResultDTO resultDTO = new ResultDTO();
    try {
      WoInsideDTO wo = woRepository.getWoByWoCode(woCode);
      if (result.equals(1L)) {
        wo.setAllowSupport(2L);
      }
      if (wo.getFtId() != null) {
        UsersEntity us = userRepository.getUserByUserId(wo.getFtId());
        if (!wo.getStatus().equals(8L)) {
          MessagesDTO message = new MessagesDTO();
          String smsContent = "";
          if (result.equals(1L)) {
            smsContent = I18n.getLanguage("wo.sms.check.help.ok");
          } else {
            smsContent = I18n.getLanguage("wo.sms.check.help.nok");
          }
          WoDetailDTO wd = woDetailRepository.findWoDetailById(wo.getWoId());

          smsContent = smsContent.replace("[WO_CODE]", wo.getWoCode());
          smsContent = smsContent.replace("[ACCOUNT_ISDN]", wd.getAccountIsdn());
          smsContent = smsContent.replace("[DESCRIPTION]", description);

          String[] contentArr = smsContent.split("#####");
          String cont = contentArr[0];
          if ("2".equals(us.getUserLanguage()) && contentArr.length > 1) {
            cont = contentArr[1];
          }

          message.setSmsGatewayId(smsGatewayId);
          message.setReceiverId(us.getUserId().toString());
          message.setReceiverUsername(us.getUsername());
          message.setReceiverFullName(us.getFullname());
          message.setSenderId(senderId);
          message.setReceiverPhone(us.getMobile());
          message.setStatus("0");
          message
              .setCreateTime(
                  DateTimeUtils.convertDateToString(new Date(), Constants.ddMMyyyyHHmmss));
          message.setContent(cont);
          messagesRepository.insertOrUpdateWfm(message);
        }
        updateWoAndHistory(wo, us.toDTO(),
            "Update result support from SPM: " + (result.equals(1L) ? "SUCCESS" : "FAIL") + " "
                + description, String.valueOf(wo.getStatus()), new Date());
        resultDTO.setId(RESULT.SUCCESS);
        resultDTO.setKey(RESULT.SUCCESS);
        resultDTO.setMessage(RESULT.SUCCESS);
        return resultDTO;
      } else {
        resultDTO.setId(RESULT.FAIL);
        resultDTO.setKey(RESULT.FAIL);
        return resultDTO;
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      resultDTO.setId(RESULT.FAIL);
      resultDTO.setKey(RESULT.FAIL);
      resultDTO.setMessage(e.getMessage());
      return resultDTO;
    }
  }

  @Override
  public List<WoDTO> getListWoForAccount(List<String> lstAccount) {
    Long numDate = 15L;
    try {
      String numDateTmp = commonRepository
          .getConfigPropertyValue(Constants.AP_PARAM.NUM_DATE_CHECK_ACC_SPM);
      Long num = Long.valueOf(numDateTmp);
      numDate = num;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return woRepository.getListWoByListAccount(lstAccount, numDate);
  }

  @Override
  public List<WoDTOSearch> getListWoByWoType(String woTypeCode, String createTimeFrom,
      String createTimeTo) {
    try {
      return woRepository.getListWoByWoType(woTypeCode, createTimeFrom, createTimeTo);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return null;
    }
  }

  @Override
  public ResultDTO updateDescriptionWoSPM(WoDTO woDTO) {
    try {
      WoInsideDTO wo = woRepository.findWoByIdNoWait(Long.valueOf(woDTO.getWoId()));
      if (StringUtils.isNotNullOrEmpty(woDTO.getWoDescription())) {
        String desNew = woDTO.getWoDescription();
        if (desNew.length() > 1501) {
          desNew = desNew.substring(0, 1500);
        }
        wo.setWoDescription(desNew);
      }
      if (StringUtils.isNotNullOrEmpty(woDTO.getWoContent())) {
        String contentNew = woDTO.getWoContent() + "\r\n" + wo.getWoContent();
        if (contentNew.length() > 1501) {
          contentNew = contentNew.substring(0, 1500);
        }
        wo.setWoContent(contentNew);
      }
      String comment = "Update description from BCCS CC: ";
      if (StringUtils.isNotNullOrEmpty(woDTO.getEndTime())) {
        comment = comment + " EndTimeOld:" + DateTimeUtils
            .convertDateToString(wo.getEndTime(), Constants.ddMMyyyyHHmmss);
        comment = comment + " EndTimeNew:" + woDTO.getEndTime();
        wo.setEndTime(DateTimeUtils.convertStringToDate(woDTO.getEndTime()));
      }

      WoDetailDTO woDetail = woDetailRepository.findWoDetailById(wo.getWoId());

      if (StringUtils.isNotNullOrEmpty(woDTO.getCcGroupId())) {
        comment = comment + " CcGroupIdOld:" + woDetail.getCcGroupId();
        comment = comment + " CcGroupIdNew:" + woDTO.getCcGroupId();
        woDetail.setCcGroupId(Long.valueOf(woDTO.getCcGroupId()));
        if (StringUtils.isNotNullOrEmpty(woDTO.getStartTime())) {
          comment = comment + " startTimeOld:" + DateTimeUtils
              .convertDateToString(wo.getStartTime(), Constants.ddMMyyyyHHmmss);
          comment = comment + " startTimeNew:" + woDTO.getStartTime();
          wo.setStartTime(DateTimeUtils.convertStringToDate(woDTO.getStartTime()));
          // xoa thong tin tam dung set trang thai ve da giao FT
          WoPendingDTO dtoS = new WoPendingDTO();
          dtoS.setWoId(wo.getWoId());
          List<WoPendingDTO> lstPending = woPendingRepository.getListWoPendingByWoId(wo.getWoId());

          if (lstPending != null && lstPending.size() > 0) {
            for (WoPendingDTO i : lstPending) {
              woPendingRepository.deleteWoPending(i.getWoPendingId());
            }
          }
          wo.setNumPending(null);
          wo.setEndPendingTime(null);
          if (wo.getFtId() != null) {
            wo.setStatus(Long.valueOf(Constants.WO_STATUS.DISPATCH));
          }
        }

      }
      if (StringUtils.isNotNullOrEmpty(woDTO.getWoDescription())) {
        comment = comment + ":" + woDTO.getWoDescription();
      }
      if (comment.length() > 1501) {
        comment = comment.substring(0, 1500);
      }
      UsersEntity user = userRepository.getUserByUserId(wo.getCreatePersonId());

      updateWoAndHistory(wo, user.toDTO(), comment, String.valueOf(wo.getStatus()), new Date());
      woDetailRepository.insertUpdateWoDetail(woDetail);
      if (wo.getFtId() != null && !wo.getStatus().equals(8L)) {
        UsersEntity us = userRepository.getUserByUserId(wo.getFtId());

        MessagesDTO message = new MessagesDTO();
        String smsContent = "Wo:" + wo.getWoCode()
            + " vua duoc cap nhat cong viec, Noi dung: " + wo.getWoContent();
        message.setSmsGatewayId(smsGatewayId);
        message.setReceiverId(String.valueOf(us.getUserId()));
        message.setReceiverUsername(us.getUsername());
        message.setReceiverFullName(us.getFullname());
        message.setSenderId(senderId);
        message.setReceiverPhone(us.getMobile());
        message.setStatus("0");
        message.setCreateTime(
            DateTimeUtils.convertDateToString(new Date(), Constants.ddMMyyyyHHmmss));
        message.setContent(smsContent);
        messagesRepository.insertOrUpdateWfm(message);
      }
      return new ResultDTO("SUCCESS", "SUCCESS", "SUCCESS");
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return new ResultDTO("FAIL", "FAIL", e.getMessage());
    }
  }

  @Override
  public ResultDTO closeWoForSPM(List<Wo> lstWo, String system, String user, Long reasonLevel3Id) {
    ResultDTO resultDTO = new ResultDTO();
    Date now = new Date();
    String res = "";
    try {
      for (Wo i : lstWo) {
        List<WoInsideDTO> lstWoNew = woRepository.getFullWoByWoSystemCode(i.getWoSystemId());
        if (lstWoNew == null || lstWoNew.isEmpty()) {
          res =
              res + "###" + I18n.getLanguage("wo.notExitWoForWoSystemId") + " " + i.getWoSystemId();
        } else {
          for (WoInsideDTO wo : lstWoNew) {
            UsersEntity us = userRepository.getUserByUserId(wo.getCreatePersonId());
            if (!wo.getStatus().equals(Long.valueOf(Constants.WO_STATUS.CLOSED_CD))) {
              // truong hop tam dong thi mo tam dong ra trươc
              if (wo.getStatus().equals(Long.valueOf(Constants.WO_STATUS.PENDING))) {
                Date pendingDate = wo.getEndPendingTime();
                Long timePending = now.getTime() - pendingDate.getTime();
                if (timePending < 0) { // neu mo truoc han thi cap nhat lai end_time wo
                  Date endDateNew = new Date(wo.getEndTime().getTime() + timePending);
                  wo.setEndTime(endDateNew);
                }
                // xoa thoi gian tam dong
                wo.setEndPendingTime(null);
                // cap nhat wo_pending
                List<WoPendingDTO> lst = woPendingRepository.getListWoPendingByWoId(wo.getWoId());
                if (lst == null) {
                  lst = new ArrayList<>();
                }
                WoPendingDTO woPending = lst.get(0);
                woPending.setOpenTime(now);
                woPending.setOpenUser(user);
                woPending.setDescription(I18n.getLanguage("wo.OpenpendingFromBCCS2"));
                woPendingRepository.updateWoPending(woPending);
              }
              // chuyen wo ve dong
              wo.setFinishTime(new Date());
              wo.setLastUpdateTime(new Date());
              wo.setResult(Long.parseLong(Constants.WO_RESULT.OK));
              wo.setReasonDetail(i.getReasonDetail());
              wo.setSolutionDetail(i.getSolutionDetail());
              updateWoAndHistory(wo, us.toDTO(), "WO_Auto close :" + system,
                  Constants.WO_STATUS.CLOSED_CD, new Date());
              if (reasonLevel3Id != null) {
                WoDetailDTO detail = woDetailRepository.findWoDetailById(wo.getWoId());
                if (detail != null) {
                  detail.setCcResult(reasonLevel3Id);
                  woDetailRepository.insertUpdateWoDetail(detail);
                }
              }
            }
            res = res + "###" + getInfoClose(wo);
          }
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    }
    resultDTO.setId("1");
    resultDTO.setKey(RESULT.SUCCESS);
    resultDTO.setMessage(res);
    return resultDTO;
  }

  public String getInfoClose(WoInsideDTO wo) {
    Double totalTime =
        (wo.getFinishTime().getTime() - wo.getStartTime().getTime()) / 1000.0 / 60 / 60;
    Double pendingTime = 0.0D;
    List<WoPendingDTO> lstPending = woPendingRepository.getListWoPendingByWoId(wo.getWoId());
    if (lstPending != null && lstPending.size() > 0) {
      Long pt = 0L;
      for (WoPendingDTO i : lstPending) {
        if (i.getOpenTime() != null && i.getInsertTime() != null) {
          pt = pt + (i.getOpenTime().getTime() - i.getInsertTime().getTime());
        }
      }
      if (pt > 0) {
        pendingTime = pt / 1000.0 / 60 / 60;
      }
    }
    return wo.getWoCode() + ";" + (Math.round(pendingTime * 100.0) / 100.0) + ";"
        + (Math.round((totalTime - pendingTime) * 100.0) / 100.0) + ";"
        + (new Date().getTime() <= wo.getEndTime().getTime() ? "1" : "0");
  }

  public void updateWoAndHistory(WoInsideDTO wo, UsersInsideDto user, String comment, String status,
      Date date) {
    WoHistoryInsideDTO woHistory = new WoHistoryInsideDTO();
    woHistory.setCdId(wo.getCdId());
    woHistory.setComments(comment);
    woHistory.setCreatePersonId(wo.getCreatePersonId());
    woHistory.setFileName(wo.getFileName());
    woHistory.setFtId(wo.getFtId());
    woHistory.setNewStatus(Long.valueOf(status));
    woHistory.setOldStatus(wo.getStatus());
    woHistory.setUpdateTime(date);
    woHistory.setUserId(user.getUserId());
    woHistory.setUserName(user.getUsername());
    woHistory.setWoCode(wo.getWoCode());
    woHistory.setWoContent(wo.getWoContent());
    woHistory.setWoId(wo.getWoId());
    wo.setStatus(Long.valueOf(status));
    wo.setLastUpdateTime(date);
    woRepository.updateWo(wo);
    woHistoryRepository.insertWoHistory(woHistory);
  }
}
