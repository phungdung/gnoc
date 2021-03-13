package com.viettel.gnoc.wo.business;

import com.viettel.cc.webserivce.CompCause;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.MessagesDTO;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.commons.model.UsersEntity;
import com.viettel.gnoc.commons.proxy.TtServiceProxy;
import com.viettel.gnoc.commons.repository.GnocFileRepository;
import com.viettel.gnoc.commons.repository.MessagesRepository;
import com.viettel.gnoc.commons.repository.UserRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.incident.dto.TroublesDTO;
import com.viettel.gnoc.wo.dto.AmiOneForm;
import com.viettel.gnoc.wo.dto.CfgSupportForm;
import com.viettel.gnoc.wo.dto.ObjFile;
import com.viettel.gnoc.wo.dto.WoDetailDTO;
import com.viettel.gnoc.wo.dto.WoInsideDTO;
import com.viettel.gnoc.wo.dto.WoWorklogInsideDTO;
import com.viettel.gnoc.wo.repository.WoDetailRepository;
import com.viettel.gnoc.wo.repository.WoRepository;
import com.viettel.gnoc.wo.repository.WoSupportRepository;
import com.viettel.gnoc.wo.repository.WoWorklogRepository;
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
public class WoTicketBusinessImpl implements WoTicketBusiness {

  @Value("${application.ftp.server}")
  private String ftpServer;

  @Value("${application.ftp.port}")
  private int ftpPort;

  @Value("${application.ftp.user}")
  private String ftpUser;

  @Value("${application.ftp.pass}")
  private String ftpPass;

  @Value("${application.ftp.folder}")
  private String ftpFolder;

  @Value("${application.upload.folder}")
  private String uploadFolder;

  @Value("${application.SmsGatewayId:5}")
  private String smsGatewayId;

  @Value("${application.SenderId:400}")
  private String senderId;

  @Autowired
  WoRepository woRepository;

  @Autowired
  WoSupportRepository woSupportRepository;

  @Autowired
  UserRepository userRepository;

  @Autowired
  WoBusiness woBusiness;

  @Autowired
  WoDetailRepository woDetailRepository;

  @Autowired
  WoWorklogRepository woWorklogRepository;

  @Autowired
  MessagesRepository messagesRepository;

  @Autowired
  GnocFileRepository gnocFileRepository;

  @Autowired
  TtServiceProxy ttServiceProxy;

  @Override
  public List<CfgSupportForm> getListWoSupportInfo(String woCode) {
    List<CfgSupportForm> lstRes = new ArrayList<>();
    try {
      WoInsideDTO wo = woRepository.getWoByWoCode(woCode);
      if (wo != null) {
        lstRes = woSupportRepository.listWoSupportInfo(wo.getWoId());
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return lstRes;
  }

  @Override
  public ResultDTO completeWorkHelp(String woCode, String userName, String worklog,
      String reasonCcId) throws Exception {
    ResultDTO resultDTO = new ResultDTO();
    try {
      if (StringUtils.isStringNullOrEmpty(woCode)) {
        throw new Exception(I18n.getLanguage("wo.WoCodeIsNotNull"));
      }
      List<UsersInsideDto> listUs = userRepository.getListUserDTOByuserName(userName);
      if (listUs == null || listUs.isEmpty()) {
        throw new Exception(I18n.getLanguage("wo.userNameNotExists"));
      }
      UsersInsideDto us = listUs.get(0);
      // cap nhat nguyen nhan
      WoInsideDTO wo = woRepository.getWoByWoCode(woCode);
      if (wo != null) {
        Long woId = wo.getWoId();
        // cap nhat tich help
        wo.setNeedSupport(null);
        if (wo.getStatus().equals(Long.valueOf(Constants.WO_STATUS.PENDING))) { // mo tam dung
          woBusiness.updatePendingWo(woCode, null, userName, worklog, "TT", false);
        }
        // cap nhat nguyen nhan 3 cap
        WoDetailDTO wd = woDetailRepository.findWoDetailById(wo.getWoId());
        if (StringUtils.isNotNullOrEmpty(reasonCcId)) {
          if (!"-1".equals(reasonCcId)) { // hoan thanh tich help
            wd.setCcResult(Long.valueOf(reasonCcId));
          }
        }
        // cap nhat worklog
        WoWorklogInsideDTO worklogDto = new WoWorklogInsideDTO();
        worklogDto.setWoId(wo.getWoId());
        worklogDto.setUpdateTime(new Date());
        worklogDto.setUserId(us.getUserId());
        worklogDto.setUsername(userName);
        worklogDto.setWoSystem("TT");
        worklogDto.setWoWorklogContent("TT tra ket qua sau khi ho tro xong:" + worklog);

        ResultInSideDto resWorklog = woWorklogRepository.insertWoWorklog(worklogDto);
        if (!RESULT.SUCCESS.equals(resWorklog.getKey())) {
          throw new Exception(I18n.getLanguage("wo.FailCreateWOWorklog"));
        }
        WoInsideDTO woUpdate = woRepository.findWoByIdNoOffset(woId);
        woUpdate.setNeedSupport(null);
        woRepository.updateWo(woUpdate);
        woDetailRepository.insertUpdateWoDetail(wd);
        //thuc hien nhan tin cho Ft
        try {
          MessagesDTO message = new MessagesDTO();
          if (wo.getFtId() != null) {
            UsersInsideDto ft = userRepository.getUserByUserId(wo.getFtId()).toDTO();
            if (ft != null) {
              String smsContent =
                  "Wo:" + woCode + " da duoc nhan vien " + userName + "(" + us.getMobile()
                      + ") ho tro xong. De nghi d/c kiem tra xu ly!";
              if ("-1".equals(reasonCcId)) { // tu choi help
                smsContent = "Wo:" + woCode + " da bi nhan vien " + userName + "(" + us.getMobile()
                    + ") tu choi ho tro. Ly do:"
                    + worklog + ". De nghi d/c kiem tra lai!";
              }
              message.setSmsGatewayId(smsGatewayId);
              message.setReceiverId(String.valueOf(wo.getFtId()));
              message.setReceiverUsername(ft.getStaffCode());
              message.setReceiverFullName(ft.getUsername());
              message.setSenderId(senderId);
              message.setReceiverPhone(ft.getMobile());
              message.setStatus("0");
              message.setCreateTime(
                  DateTimeUtils.convertDateToString(new Date(), Constants.ddMMyyyyHHmmss));
              message.setContent(smsContent);
              messagesRepository.insertOrUpdateWfm(message);
            }
          }
        } catch (Exception e) {
          log.info(e.getMessage(), e);
          throw new Exception(I18n.getLanguage("wo.haveSomeErrWhenCreateMessages"));
        }
        resultDTO.setId("");
        resultDTO.setKey(RESULT.SUCCESS);
        resultDTO.setMessage(RESULT.SUCCESS);
        return resultDTO;
      } else {
        throw new Exception(I18n.getLanguage("wo.woIsNotExists"));
      }
    } catch (Exception e) {
      log.info(e.getMessage(), e);
      throw new Exception(I18n.getLanguage("wo.haveSomeError"));
    }
  }

  @Override
  public List<AmiOneForm> getInfoAmiOne(List<String> lstAmiOneId) {
    List<AmiOneForm> lstRes = new ArrayList<>();
    try {
      for (String amiOneId : lstAmiOneId) {
        AmiOneForm res = new AmiOneForm();
        WoInsideDTO wo = woRepository.getWoByAmiOneId(amiOneId);
        if (wo == null) { // goi sang TT lay thong tin
          List<String> lstId = new ArrayList<>();
          lstId.add(amiOneId);
          TroublesDTO troublesDTO = new TroublesDTO();
          troublesDTO.setTroubleCodes(null);
          troublesDTO.setAmiIds(lstId);
          List<TroublesDTO> lstTT = ttServiceProxy.getInfoTicketForAMI(troublesDTO);
          if (lstTT != null && lstTT.size() > 0) {
            TroublesDTO tt = lstTT.get(0);
            res.setCompletedTime(tt.getClearTime());
            res.setDescription("");
            res.setFileArr(tt.getFileDocumentByteArray());
            res.setGnocCode(tt.getTroubleCode());
            res.setListFileName(tt.getArrFileName());
            res.setReasonName(tt.getReasonName());
            res.setUserExecute(tt.getClearUserName());
          }
        } else {
          res.setCompletedTime(wo.getCompletedTime() == null ? null
              : DateTimeUtils.convertDateToString(wo.getCompletedTime(), Constants.ddMMyyyyHHmmss));
          res.setDescription(wo.getCommentComplete());
          res.setGnocCode(wo.getWoCode());
          if (wo.getFtId() != null) {
            UsersEntity ft = userRepository.getUserByUserId(wo.getFtId());
            if (ft != null) {
              res.setUserExecute(ft.getUsername());
            }
          }
          //lay danh sach file
          if (StringUtils.isNotNullOrEmpty(wo.getFileName())) {
            List<ObjFile> lstFile = woBusiness.getFileFromWo(wo.getWoId(), null);
            if (lstFile != null && lstFile.size() > 0) {
              List<String> lstFileName = new ArrayList<>();
              List<byte[]> lstArr = new ArrayList<>();
              for (ObjFile f : lstFile) {
                lstFileName.add(f.getFileName());
                lstArr.add(f.getFileArr());
              }
              res.setListFileName(lstFileName);
              res.setFileArr(lstArr);
            }
          }
          WoDetailDTO wd = woDetailRepository.findWoDetailById(wo.getWoId());
          if (wd != null) {
            CompCause lv3 = woRepository.getCompCause(wd.getCcResult());
            if (lv3 != null) {
              res.setReasonName(lv3.getName());
              if (lv3.getCode() != null && lv3.getCode().startsWith("AMI.ONE.KQ")) {
                res.setReasonType("1");
              } else {
                res.setReasonType("0");
              }
            }
          }
        }
        lstRes.add(res);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    }
    return lstRes;
  }
}
