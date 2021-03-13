package com.viettel.gnoc.incident.business;

import com.viettel.gnoc.commons.business.CatCfgClosedTicketBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.CatCfgClosedTicketDTO;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.InfraDeviceDTO;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.proxy.WoServiceProxy;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DateUtil;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.incident.dto.CfgTimeTroubleProcessDTO;
import com.viettel.gnoc.incident.dto.TroublesInSideDTO;
import com.viettel.gnoc.incident.repository.TroublesRepository;
import com.viettel.gnoc.wo.dto.RequestApiWODTO;
import com.viettel.gnoc.wo.dto.WoDTO;
import com.viettel.gnoc.wo.dto.WoUpdateStatusForm;
import java.util.ArrayList;
import java.util.Date;
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
public class WOCreateBusinessImpl implements WOCreateBusiness {

  @Autowired
  private CatCfgClosedTicketBusiness catCfgClosedTicketBusiness;

  @Autowired
  WoServiceProxy woServiceProxy;

  @Autowired
  TicketProvider ticketProvider;

  @Override
  public ResultDTO createWO(TroublesInSideDTO troublesDTO, CfgTimeTroubleProcessDTO config,
      TroublesRepository dao) throws Exception {
    log.debug("create WO {}", troublesDTO);
    Date now = new Date();
    // Set gia tri vao WO DTO
    WoDTO woDtoAdd = new WoDTO();
    //Noi dung cong viec
    String description = troublesDTO.getDescription();
    if (!StringUtils.isStringNullOrEmpty(description) && description.length() > 1000) {
      description = description.substring(0, 999);
    }
    woDtoAdd.setWoDescription(description);
    //Loai cong viec WO tự xét vào
    //username nguoi tao
    woDtoAdd.setCreatePersonId(troublesDTO.getCreateUserId() == null ? null
        : String.valueOf(troublesDTO.getCreateUserId()));

    String woContentAdd = "[GNOC_" + troublesDTO.getInsertSource() + "_" + troublesDTO.getTypeName()
        + "] " + ": " + troublesDTO
        .getTroubleName();

    //Ten WO
    woDtoAdd.setWoContent(woContentAdd);
    //HaiNV20 bo sung them huong dan xu ly ticket
    //Thoi gian tao ticket
    woDtoAdd.setStartTime(troublesDTO.getCreatedTime() == null ? null
        : DateUtil.date2ddMMyyyyHHMMss(troublesDTO.getCreatedTime()));

    Date startTime = troublesDTO.getCreatedTime();

    //Mac dinh 1h
    // sửa lại thành mặc định 2 giờ
    //Long incrementTime = 60 * 60 * 1000L;
    Long incrementTime = 120 * 60 * 1000L;
    if (config != null) {
      if (!StringUtils.isStringNullOrEmpty(config.getProcessWoTime())) {
        incrementTime = ((Double) (Double.valueOf(config.getProcessWoTime()) * 60 * 60 * 1000))
            .longValue();
      } else if (!StringUtils.isStringNullOrEmpty(config.getProcessTtTime())) {
        incrementTime = ((Double) ((Double.valueOf(config.getProcessTtTime())) * 60 * 60 * 1000))
            .longValue();
      }
      if (config.getIsCall() != null && 1 == config.getIsCall()) {
        woDtoAdd.setIsCall(Long.valueOf(1));
      } else {
        woDtoAdd.setIsCall(Long.valueOf(0));
      }
      woDtoAdd.setFtAudioName(config.getFtAudioName());
      woDtoAdd.setCdAudioName(config.getCdAudioName());
    }

    Date endTime = new Date(startTime.getTime() + incrementTime);

    //Thoi gian hien tai
    woDtoAdd.setCreateDate(DateUtil.date2ddMMyyyyHHMMss(now));

    woDtoAdd.setEndTime(DateUtil.date2ddMMyyyyHHMMss(endTime));

    //Nguoi thuc hien
    woDtoAdd.setWoSystemId(troublesDTO.getTroubleCode());
    woDtoAdd.setWoSystem("TT");
    woDtoAdd.setCreateWoType(troublesDTO.getAlarmGroupCode());
    CatItemDTO prio = new CatItemDTO();
    prio.setItemId(StringUtils.isStringNullOrEmpty(troublesDTO.getPriorityId()) ? null
        : troublesDTO.getPriorityId());
    Map<String, String> mapProperty = dao.getConfigProperty();

    String priorityCritical = mapProperty.get("WO.TT.PRIORITY.CRITICAL");
    String priorityMajor = mapProperty.get("WO.TT.PRIORITY.MAJOR");
    String priorityMinor = mapProperty.get("WO.TT.PRIORITY.MINOR");

    String priorityValue = null;
    if (priorityCritical.contains("," + troublesDTO.getPriorityId())) {
      priorityValue = priorityCritical.substring(0, priorityCritical.indexOf(","));
    } else if (priorityMajor.contains("," + troublesDTO.getPriorityId())) {
      priorityValue = priorityMajor.substring(0, priorityMajor.indexOf(","));
    } else if (priorityMinor.contains("," + troublesDTO.getPriorityId())) {
      priorityValue = priorityMinor.substring(0, priorityMinor.indexOf(","));
    }

    woDtoAdd.setLinkCode(troublesDTO.getLinkCode());
    woDtoAdd.setLinkId(troublesDTO.getLinkId());
    woDtoAdd.setAlarmId(
        troublesDTO.getAlarmId() == null ? null : String.valueOf(troublesDTO.getAlarmId()));
    woDtoAdd.setAmiOneId(
        troublesDTO.getCameraId() == null ? (troublesDTO.getAmiId() == null ? null
            : String.valueOf(troublesDTO.getAmiId())) : troublesDTO.getCameraId());
    woDtoAdd.setCustomerName(troublesDTO.getCustomerName());
    woDtoAdd.setCustomerPhone(troublesDTO.getCustomerPhone());
    woDtoAdd.setAccountIsdn(troublesDTO.getAccountGline());
    troublesDTO.setAccountGline("");
    troublesDTO.setCustomerName("");
    troublesDTO.setCustomerPhone("");
    woDtoAdd.setLocationCode(troublesDTO.getLocationCode());

    ResultDTO resultWO;
    if (priorityValue != null) {
      woDtoAdd.setPriorityId(priorityValue);
      if (!StringUtils.isStringNullOrEmpty(troublesDTO.getWoType())) {
        woDtoAdd.setWoTypeId(troublesDTO.getWoType());
      } else if ("SPM".equals(troublesDTO.getInsertSource())) {
        woDtoAdd.setWoTypeId(mapProperty.get("WO.SPM.WO_TYPE"));
      } else {
        woDtoAdd.setWoTypeId(mapProperty.get("WO.TT.TYPE.ID_TRANS"));
      }
      if (!StringUtils.isStringNullOrEmpty(troublesDTO.getAmiId()) || !StringUtils
          .isStringNullOrEmpty(troublesDTO.getCameraId())) {
        woDtoAdd.setWoTypeId(mapProperty.get("WO_TYPE_AMI_ONE"));
      }

      woDtoAdd.setCreatePersonId(mapProperty.get("WO.TT.CREATE_PERSON.ID"));
      List<String> lst = new ArrayList<>();
      if (troublesDTO.getLstNode() != null && troublesDTO.getLstNode().size() > 0) {
        for (InfraDeviceDTO i : troublesDTO.getLstNode()) {
          lst.add(i.getDeviceCode());
        }
      } else if (troublesDTO.getAffectedNode() != null) {
        String[] strNode = troublesDTO.getAffectedNode().split(",");
        for (int i = 0; i < strNode.length; i++) {
          lst.add(strNode[i]);
        }
      }
      if (!StringUtils.isStringNullOrEmpty(troublesDTO.getRelatedKedb())) {
        woDtoAdd.setKedbCode(troublesDTO.getRelatedKedb().trim());
        String woId = woDtoAdd.getKedbCode().substring(woDtoAdd.getKedbCode().lastIndexOf("_") + 1,
            woDtoAdd.getKedbCode().length());
        woDtoAdd.setKedbId(woId);
      }
      //chuyen sang nationCode
      if (!"VNM".equalsIgnoreCase(troublesDTO.getNationCode())) {
        woDtoAdd.setNationCode(troublesDTO.getNationCode());
      }

      CatCfgClosedTicketDTO ccctdto = new CatCfgClosedTicketDTO();
      List<CatCfgClosedTicketDTO> lstClosedTicketDTO;
      if (troublesDTO.getInsertSource() != null && troublesDTO.getInsertSource().contains("NOC")) {

        //kiem tra co bat buoc nhap nn hay ko
        ccctdto.setAlarmGroupId(troublesDTO.getAlarmGroupId());
        ccctdto.setWoTypeId(woDtoAdd.getWoTypeId());
        ccctdto.setTypeId(
            troublesDTO.getTypeId() == null ? null : String.valueOf(troublesDTO.getTypeId()));
        lstClosedTicketDTO = catCfgClosedTicketBusiness
            .getListCatCfgClosedTicketDTO(ccctdto, 0, 100, "", "");
        if (lstClosedTicketDTO != null && !lstClosedTicketDTO.isEmpty()) {
          woDtoAdd.setRequiredTtReason("1");
        }
      }
      if (!StringUtils.isStringNullOrEmpty(troublesDTO.getCdId())) {
        woDtoAdd.setUnitCode(troublesDTO.getCdId());
        woDtoAdd.setIsSendFT("0");
      }
      //mop tac dong
      if ("2".equals(troublesDTO.getCheckbox())) {
        woDtoAdd.setAbleMop("1");
      }

      resultWO = woServiceProxy.createWoFollowNode(new RequestApiWODTO(woDtoAdd, lst));
      if ("1".equals(troublesDTO.getCheckbox())) {
        woDtoAdd.setWoTypeId(mapProperty.get("WO.TYPE.CHECK.QLTS.NC.UCTT"));
        endTime = new Date(startTime.getTime() + 24 * 60 * 60 * 1000L);
        woDtoAdd.setEndTime(DateUtil.date2ddMMyyyyHHMMss(endTime));
        if (troublesDTO.getInsertSource() != null && troublesDTO.getInsertSource()
            .contains("NOC")) {

          ccctdto.setAlarmGroupId(troublesDTO.getAlarmGroupId());
          ccctdto.setWoTypeId(woDtoAdd.getWoTypeId());
          ccctdto.setTypeId(
              troublesDTO.getTypeId() == null ? null : String.valueOf(troublesDTO.getTypeId()));
          lstClosedTicketDTO = catCfgClosedTicketBusiness
              .getListCatCfgClosedTicketDTO(ccctdto, 0, 100, "", "");
          if (lstClosedTicketDTO != null && !lstClosedTicketDTO.isEmpty()) {
            woDtoAdd.setRequiredTtReason("1");
          } else {
            woDtoAdd.setRequiredTtReason("0");
          }
        }
        woDtoAdd.setAbleMop("");
        ResultDTO rdto = woServiceProxy.createWoFollowNode(new RequestApiWODTO(woDtoAdd, lst));
        if (RESULT.SUCCESS.equals(resultWO.getKey()) && RESULT.SUCCESS.equals(rdto.getKey())) {
          resultWO.setId(resultWO.getId());
          resultWO.setRequestTime("UCTT: " + rdto.getId() + " WO: " + resultWO.getId());
        } else if (RESULT.SUCCESS.equals(resultWO.getKey()) && !RESULT.SUCCESS
            .equals(rdto.getKey())) {
          resultWO.setId(resultWO.getId());
          resultWO.setKey(RESULT.ERROR);
          resultWO.setMessage("WO: " + resultWO.getId() + " " + rdto.getMessage());
          resultWO.setRequestTime("UCTT:");
        } else if (!RESULT.SUCCESS.equals(resultWO.getKey()) && RESULT.SUCCESS
            .equals(rdto.getKey())) {
          resultWO.setId(null);
          resultWO.setKey(RESULT.ERROR);
          resultWO.setMessage("UCTT: " + rdto.getId() + " " + resultWO.getMessage());
          resultWO.setRequestTime("UCTT:");
        } else if (!RESULT.SUCCESS.equals(resultWO.getKey()) && !RESULT.SUCCESS
            .equals(rdto.getKey())) {
          resultWO.setId(null);
          resultWO.setKey(RESULT.ERROR);
          resultWO.setMessage("WO: " + resultWO.getMessage() + " UCTT: " + rdto.getMessage());

        }
        troublesDTO.setCheckbox(null);
      }

    } else {
      resultWO = new ResultDTO();
      resultWO.setKey(RESULT.ERROR);
      resultWO.setMessage("incident.priority.notExistOnWO");
    }
    return resultWO;
  }

  @Override
  public ResultDTO createWOMobile(TroublesInSideDTO troublesDTO,
      CfgTimeTroubleProcessDTO config,
      TroublesRepository dao) throws Exception {
    Date now = new Date();
    WoDTO woDtoAdd = new WoDTO();
    woDtoAdd.setWoDescription(troublesDTO.getDescription());
    woDtoAdd.setCreatePersonId(troublesDTO.getCreateUserId() == null ? null
        : String.valueOf(troublesDTO.getCreateUserId()));
    String woContentAdd =
        "[GNOC_MOBILE_" + troublesDTO.getInsertSource() + "_" + troublesDTO.getTypeName() + "] "
            + I18n.getLanguage("incident.handle") + ": " + troublesDTO.getTroubleName();
    if (!StringUtils.isStringNullOrEmpty(troublesDTO.getWoContent())) {
      woContentAdd = troublesDTO.getWoContent();
    }
    woDtoAdd.setWoContent(woContentAdd);
    woDtoAdd.setStartTime(troublesDTO.getBeginTroubleTime() == null ? null
        : DateUtil.date2ddMMyyyyHHMMss(troublesDTO.getBeginTroubleTime()));
    woDtoAdd.setCreateDate(DateUtil.date2ddMMyyyyHHMMss(now));
//    woDtoAdd.setEndTime(troublesDTO.getEndTroubleTime() == null ? null
//        : DateUtil.date2ddMMyyyyHHMMss(troublesDTO.getEndTroubleTime()));

    if (Constants.INSERT_SOURCE.SPM_VTNET
        .equalsIgnoreCase(troublesDTO.getInsertSource())) {//spm_soc
      Long incrementTime = 120 * 60 * 1000L;
      if (config != null) {
        if (!StringUtils.isStringNullOrEmpty(config.getProcessWoTime())) {
          incrementTime = ((Double) (Double.valueOf(config.getProcessWoTime()) * 60 * 60 * 1000))
              .longValue();
        } else if (!StringUtils.isStringNullOrEmpty(config.getProcessTtTime())) {
          incrementTime = ((Double) ((Double.valueOf(config.getProcessTtTime())) * 60 * 60 * 1000))
              .longValue();
        }
      }
      Date startTime = troublesDTO.getCreatedTime();
      Date endTime = new Date(startTime.getTime() + incrementTime);
      woDtoAdd.setEndTime(DateUtil.date2ddMMyyyyHHMMss(endTime));
      woDtoAdd.setWoSystem(Constants.INSERT_SOURCE.SPM_VTNET);
    } else {
      woDtoAdd.setEndTime(troublesDTO.getEndTroubleTime() == null ? null
          : DateUtil.date2ddMMyyyyHHMMss(troublesDTO.getEndTroubleTime()));
      woDtoAdd.setWoSystem("SPM");
    }
    woDtoAdd.setFileArr(troublesDTO.getFileDocumentByteArray());
    woDtoAdd.setListFileName(troublesDTO.getArrFileName());
    woDtoAdd.setCustomerName(troublesDTO.getCustomerName());
    woDtoAdd.setCustomerPhone(troublesDTO.getCustomerPhone());
    woDtoAdd.setAccountGline(troublesDTO.getAccountGline());
    woDtoAdd.setCustomerTimeDesireFrom(troublesDTO.getCustomerTimeDesireFrom());
    woDtoAdd.setCustomerTimeDesireTo(troublesDTO.getCustomerTimeDesireTo());
    woDtoAdd.setCcGroupId(troublesDTO.getCcGroupId());
    woDtoAdd.setCcServiceId(troublesDTO.getServiceType().toString());
    woDtoAdd.setTelServiceId(troublesDTO.getTelServiceId());
    woDtoAdd.setInfraType(troublesDTO.getInfraType());
    troublesDTO.setTechnology(troublesDTO.getInfraType());
    woDtoAdd.setIsCcResult(troublesDTO.getIsCcResult());
    woDtoAdd.setProductCode(troublesDTO.getProductCode());
    woDtoAdd.setAccountIsdn(troublesDTO.getSpmName());
    woDtoAdd.setLocationCode(troublesDTO.getLocationCode());
    woDtoAdd.setSpmCode(troublesDTO.getSpmCode());
    woDtoAdd.setCcPriorityCode(troublesDTO.getPriorityCode());

    if (!StringUtils.isStringNullOrEmpty(troublesDTO.getRelatedKedb())) {
      woDtoAdd.setKedbCode(troublesDTO.getRelatedKedb().trim());
      String woId = woDtoAdd.getKedbCode()
          .substring(woDtoAdd.getKedbCode().lastIndexOf("_") + 1, woDtoAdd.getKedbCode().length());
      woDtoAdd.setKedbId(woId);
    }

    Map<String, String> mapProperty = dao.getConfigProperty();
    String priorityCritical = mapProperty.get("WO.TT.PRIORITY.CRITICAL");
    String priorityMajor = mapProperty.get("WO.TT.PRIORITY.MAJOR");
    String priorityMinor = mapProperty.get("WO.TT.PRIORITY.MINOR");
    String priorityValue = null;
    if (priorityCritical.contains("," + troublesDTO.getPriorityId())) {
      priorityValue = priorityCritical.substring(0, priorityCritical.indexOf(","));
    } else if (priorityMajor.contains("," + troublesDTO.getPriorityId())) {
      priorityValue = priorityMajor.substring(0, priorityMajor.indexOf(","));
    } else if (priorityMinor.contains("," + troublesDTO.getPriorityId())) {
      priorityValue = priorityMinor.substring(0, priorityMinor.indexOf(","));
    }
    ResultDTO resultWO;
    if (priorityValue != null) {
      woDtoAdd.setPriorityId(priorityValue);
      woDtoAdd.setWoTypeId(mapProperty.get("WO.SPM.WO_TYPE"));
      if (!StringUtils.isStringNullOrEmpty(troublesDTO.getWoType())) {
        woDtoAdd.setWoTypeId(troublesDTO.getWoType());
      }
      woDtoAdd.setCreatePersonId(mapProperty.get("WO.TT.CREATE_PERSON.ID"));
      if (StringUtils.isStringNullOrEmpty(troublesDTO.getCdId())) {
        resultWO = woServiceProxy.createWoProxy(woDtoAdd);
      } else {
        woDtoAdd.setCdId(troublesDTO.getCdId());
        if (troublesDTO.getInfraType() != null) {
          switch (troublesDTO.getInfraType().toUpperCase()) {
            case "CCN":
              woDtoAdd.setInfraType("1");
              break;
            case "CATV":
              woDtoAdd.setInfraType("2");
              break;
            case "FCN":
              woDtoAdd.setInfraType("3");
              break;
            case "GPON":
              woDtoAdd.setInfraType("4");
              break;
            case "N/A":
              woDtoAdd.setInfraType("0");
              break;
            default:
              break;
          }
        }
        resultWO = woServiceProxy.insertWoForSPMProxy(woDtoAdd);
      }

    } else {
      resultWO = new ResultDTO();
      resultWO.setKey(RESULT.FAIL);
      resultWO.setMessage("incident.priority.notExistOnWO");
      throw new Exception("incident.priority.notExistOnWO");
    }
    return resultWO;
  }

  @Override
  public ResultDTO createWOForCC(TroublesInSideDTO troublesDTO, TroublesRepository dao)
      throws Exception {
    Date now = new Date();
    Map<String, String> mapProperty = dao.getConfigProperty();
    WoDTO woDtoAdd = new WoDTO();
    woDtoAdd.setWoDescription(troublesDTO.getDescription());
    woDtoAdd.setCreatePersonId(troublesDTO.getCreateUserId() == null ? null
        : String.valueOf(troublesDTO.getCreateUserId()));
    woDtoAdd.setWoContent(
        troublesDTO.getTroubleName() + " " + (troublesDTO.getWoContent() == null ? ""
            : troublesDTO.getWoContent()));
    woDtoAdd.setStartTime(troublesDTO.getBeginTroubleTime() == null ? null
        : DateUtil.date2ddMMyyyyHHMMss(troublesDTO.getBeginTroubleTime()));
    woDtoAdd.setCreateDate(DateUtil.date2ddMMyyyyHHMMss(now));
    Long incrementTime = 2 * 60 * 60 * 1000L;
    String timeRate = mapProperty.get("TT.TIME.PER.WO.TIME");
    Long time = 1L;
    Long totalTime = 1L;
    if (timeRate != null && !"".equals(timeRate)) {
      time = Long.valueOf(timeRate.split(":")[1]);
      totalTime = Long.valueOf(timeRate.split(":")[0]) + Long.valueOf(timeRate.split(":")[1]);
    }
    if (!StringUtils.isStringNullOrEmpty(troublesDTO.getTimeProcess())) {
      incrementTime = ((Double) ((troublesDTO.getTimeProcess() * time * 60 * 60 * 1000)
          / totalTime)).longValue();

    }
    Date startTime = troublesDTO.getCreatedTime();
    Date endTime = new Date(startTime.getTime() + incrementTime);
    woDtoAdd.setEndTime(DateUtil.date2ddMMyyyyHHMMss(endTime));
    woDtoAdd.setWoSystemId(troublesDTO.getTroubleCode());
    woDtoAdd.setWoSystem("CC_SCVT");
    woDtoAdd.setFileArr(troublesDTO.getFileDocumentByteArray());
    woDtoAdd.setListFileName(troublesDTO.getArrFileName());
    woDtoAdd.setCcGroupId(troublesDTO.getComplaintGroupId());
    woDtoAdd.setIsCcResult(troublesDTO.getIsCcResult());
    woDtoAdd.setProductCode(troublesDTO.getProductCode());
    woDtoAdd.setAccountIsdn(troublesDTO.getSpmName());
    woDtoAdd.setLocationCode(troublesDTO.getLocationCode());
    if (troublesDTO.getLstNode() != null && !troublesDTO.getLstNode().isEmpty()) {
      List<String> lst = new ArrayList<>();
      for (InfraDeviceDTO deviceDTO : troublesDTO.getLstNode()) {
        lst.add(deviceDTO.getDeviceCode());
      }
      woDtoAdd.setLstStationCode(lst);
    }
    if (!StringUtils.isStringNullOrEmpty(troublesDTO.getRelatedKedb())) {
      woDtoAdd.setKedbCode(troublesDTO.getRelatedKedb().trim());
      String woId = woDtoAdd.getKedbCode()
          .substring(woDtoAdd.getKedbCode().lastIndexOf("_") + 1, woDtoAdd.getKedbCode().length());
      woDtoAdd.setKedbId(woId);
    }
    woDtoAdd.setEndPendingTime(troublesDTO.getDeferredTime() == null ? null
        : DateUtil.date2ddMMyyyyHHMMss(troublesDTO.getDeferredTime()));
    String priorityCritical = mapProperty.get("WO.TT.PRIORITY.CRITICAL");
    String priorityMajor = mapProperty.get("WO.TT.PRIORITY.MAJOR");
    String priorityMinor = mapProperty.get("WO.TT.PRIORITY.MINOR");
    String priorityValue = null;
    if (priorityCritical.contains("," + troublesDTO.getPriorityId())) {
      priorityValue = priorityCritical.substring(0, priorityCritical.indexOf(","));
    } else if (priorityMajor.contains("," + troublesDTO.getPriorityId())) {
      priorityValue = priorityMajor.substring(0, priorityMajor.indexOf(","));
    } else if (priorityMinor.contains("," + troublesDTO.getPriorityId())) {
      priorityValue = priorityMinor.substring(0, priorityMinor.indexOf(","));
    }
    ResultDTO resultWO;
    if (priorityValue != null) {
      woDtoAdd.setPriorityId(priorityValue);
      woDtoAdd.setWoTypeId(mapProperty.get("WO.TYPE.XLSCVT"));
      woDtoAdd.setCreatePersonId(mapProperty.get("WO.TT.CREATE_PERSON.ID"));
      resultWO = woServiceProxy.createWoProxy(woDtoAdd);

    } else {
      resultWO = new ResultDTO();
      resultWO.setKey(RESULT.FAIL);
      resultWO.setMessage("incident.priority.notExistOnWO");
      throw new Exception("incident.priority.notExistOnWO");
    }
    return resultWO;
  }

  @Override
  public ResultDTO changeStatusWo(WoUpdateStatusForm updateForm) {
    UserToken userToken = ticketProvider.getUserToken();
    updateForm.setUserChange(userToken.getUserName());
    return woServiceProxy.changeStatusWoProxy(updateForm);
  }
}
