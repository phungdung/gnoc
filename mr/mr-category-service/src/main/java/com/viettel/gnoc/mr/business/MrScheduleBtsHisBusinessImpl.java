package com.viettel.gnoc.mr.business;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.CatLocationDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.proxy.WoServiceProxy;
import com.viettel.gnoc.commons.repository.CatLocationRepository;
import com.viettel.gnoc.commons.repository.UserRepository;
import com.viettel.gnoc.commons.utils.CellConfigExport;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.ConfigFileExport;
import com.viettel.gnoc.commons.utils.ConfigHeaderExport;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.DateUtil;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.maintenance.dto.MrCauseWoWasCompletedDTO;
import com.viettel.gnoc.maintenance.dto.MrScheduleBtsHisDTO;
import com.viettel.gnoc.maintenance.dto.MrScheduleBtsHisDetailInsiteDTO;
import com.viettel.gnoc.maintenance.dto.MrUserCfgApprovedSmsBtsDTO;
import com.viettel.gnoc.mr.repository.MrChecklistBtsDetailRepository;
import com.viettel.gnoc.mr.repository.MrDeviceBtsRepository;
import com.viettel.gnoc.mr.repository.MrScheduleBtsHisRepository;
import com.viettel.gnoc.mr.repository.MrScheduleBtsRepository;
import com.viettel.gnoc.mr.repository.MrUserCfgApprovedSmsBtsRepository;
import com.viettel.gnoc.wo.dto.WoDTO;
import com.viettel.gnoc.wo.dto.WoInsideDTO;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import viettel.passport.client.UserToken;

@Service
@Transactional
@Slf4j
public class MrScheduleBtsHisBusinessImpl implements MrScheduleBtsHisBusiness {

  @Autowired
  protected MrScheduleBtsHisRepository mrScheduleBtsHisRepository;
  @Autowired
  protected MrScheduleBtsRepository mrScheduleBtsRepository;
  @Autowired
  protected MrDeviceBtsRepository mrDeviceBtsRepository;
  @Autowired
  WoServiceProxy woServiceProxy;
  @Autowired
  TicketProvider ticketProvider;
  @Autowired
  UserRepository userRepository;
  @Autowired
  CatLocationRepository catLocationRepository;

  @Autowired
  MrUserCfgApprovedSmsBtsRepository mrUserCfgApprovedSmsBtsRepository;

  @Autowired
  MrChecklistBtsDetailRepository mrChecklistBtsDetailRepository;
  @Autowired
  MrCauseWoWasCompletedBusiness mrCauseWoWasCompletedBusiness;

  @Value("${application.temp.folder}")
  private String tempFolder;
  private final static String MR_SCHEDULE_BTS_HIS_EXPORT = "MR_SCHEDULE_BTS_HIS_EXPORT";

  Map<String, String> mapLocationCountry = new HashMap<>();
  Map<String, CatLocationDTO> mapLocationArea = new HashMap<>();
  Map<String, CatLocationDTO> mapLocationProvince = new HashMap<>();


  @Override
  public Datatable getListMrScheduleBtsHisDTO(MrScheduleBtsHisDTO mrScheduleBtsHisDTO) {
    log.debug("Request to getListMrScheduleBtsHisDTO: {}", mrScheduleBtsHisDTO);
    Datatable datatable = mrScheduleBtsHisRepository
        .getListMrScheduleBtsHisDTO(mrScheduleBtsHisDTO);

    List<MrScheduleBtsHisDTO> list = (List<MrScheduleBtsHisDTO>) datatable.getData();
    if (mapLocationCountry.isEmpty()) {
      List<ItemDataCRInside> lstCountry = catLocationRepository
          .getListLocationByLevelCBB(null, 1L, null);
      for (ItemDataCRInside item : lstCountry) {
        mapLocationCountry.put(item.getValueStr().toString(), item.getDisplayStr());
      }
    }
    if (mapLocationArea.isEmpty()) {
      List<CatLocationDTO> lstCountry = catLocationRepository.getCatLocationByLevel("2");
      for (CatLocationDTO item : lstCountry) {
        mapLocationArea.put(item.getLocationCode(), item);
      }
    }
    if (mapLocationProvince.isEmpty()) {
      List<CatLocationDTO> lstCountry = catLocationRepository.getCatLocationByLevel("3");
      for (CatLocationDTO item : lstCountry) {
        mapLocationProvince.put(item.getPreCodeStation(), item);
      }
    }
    if (list != null && list.size() > 0) {
      for (MrScheduleBtsHisDTO resultDTO : list) {
        if (!StringUtils.isStringNullOrEmpty(resultDTO.getStatusWoGL())) {
          Long woStatusGL = Long.valueOf(resultDTO.getStatusWoGL());
          if (Constants.WO_STATE.getStateName().containsKey(woStatusGL)) {
            resultDTO
                .setStatusName(I18n.getLanguage(Constants.WO_STATE.getStateName().get(woStatusGL)));
            resultDTO
                .setStatusNameGL(
                    I18n.getLanguage(Constants.WO_STATE.getStateName().get(woStatusGL)));
          }
        } else if (!StringUtils.isStringNullOrEmpty(resultDTO.getStatusWoGoc())) {
          Long woStatusGoc = Long.valueOf(resultDTO.getStatusWoGoc());
          if (Constants.WO_STATE.getStateName().containsKey(woStatusGoc)) {
            resultDTO
                .setStatusName(
                    I18n.getLanguage(Constants.WO_STATE.getStateName().get(woStatusGoc)));
          }
        }
        if (!StringUtils.isStringNullOrEmpty(resultDTO.getMarketCode()) && mapLocationCountry
            .containsKey(resultDTO.getMarketCode())) {
          resultDTO.setMarketCodeStr(mapLocationCountry.get(resultDTO.getMarketCode()));
        }
        if (!StringUtils.isStringNullOrEmpty(resultDTO.getAreaCode()) && mapLocationArea
            .containsKey(resultDTO.getAreaCode())) {
          resultDTO.setAreaCodeStr(mapLocationArea.get(resultDTO.getAreaCode()).getLocationName());
        }
        if (!StringUtils.isStringNullOrEmpty(resultDTO.getProvinceCode()) && mapLocationProvince
            .containsKey(resultDTO.getProvinceCode())) {
          resultDTO.setProvinceCodeStr(
              mapLocationProvince.get(resultDTO.getProvinceCode()).getLocationName());
        }
//        MrScheduleBtsHisDetailInsiteDTO dto = new MrScheduleBtsHisDetailInsiteDTO();
//        dto.setSerial(resultDTO.getSerial());
//        dto.setDeviceType(resultDTO.getDeviceType());
//        dto.setCycle(resultDTO.getCycle());
//        dto.setWoCode(resultDTO.getWoCode());
//        List<MrScheduleBtsHisDetailInsiteDTO> lstCheckList = mrScheduleBtsHisRepository
//            .getListWoCodeMrScheduleBtsHisDetail(dto);
//        if (lstCheckList != null && !lstCheckList.isEmpty()) {
//          String woCode = "";
//          for (MrScheduleBtsHisDetailInsiteDTO checkList : lstCheckList) {
//            if (!checkList.getWoCode().equals(resultDTO.getWoCode())) {
//              woCode += "," + checkList.getWoCode();// cộng dồn bắt đầu từ ,Wo1,Wo2,Wo3
//            }
//          }
//          if (woCode.startsWith(",")) { // Nếu dấu , đầu tiên
//            woCode = woCode.substring(1); // Thì cắt nó đi vị trí là 1
//          }
//          resultDTO.setWogiaoLai(woCode);
//        }
        if (!StringUtils.isStringNullOrEmpty(resultDTO.getWoLstTag())) {
          resultDTO.setWogiaoLai(resultDTO.getWoLstTag());
        }
      }
    }
    datatable.setData(list);
    return datatable;
  }

  @Override
  public List<MrScheduleBtsHisDetailInsiteDTO> getListWoBts(
      MrScheduleBtsHisDetailInsiteDTO mrScheduleBtsHisDetailDTO) {
    log.debug("Request to getListWoBts: {}", mrScheduleBtsHisDetailDTO);
    List<MrScheduleBtsHisDetailInsiteDTO> ltsDetail = mrScheduleBtsHisRepository
        .getListWoCodeMrScheduleBtsHisDetail(mrScheduleBtsHisDetailDTO);
    MrScheduleBtsHisDetailInsiteDTO mrScheduleBtsHisDetail = new MrScheduleBtsHisDetailInsiteDTO();
    if (ltsDetail != null) {
      mrScheduleBtsHisDetail.setWoCode(ltsDetail.get(0).getWoCode());
    }
    return mrScheduleBtsHisRepository.getListWoBts(mrScheduleBtsHisDetail);
  }

  @Override
  public List<MrScheduleBtsHisDetailInsiteDTO> getListWoCodeMrScheduleBtsHisDetail(
      MrScheduleBtsHisDetailInsiteDTO mrScheduleBtsHisDetailDTO) {
    log.debug("Request to getListWoCodeMrScheduleBtsHisDetail: {}", mrScheduleBtsHisDetailDTO);
    return mrScheduleBtsHisRepository
        .getListWoCodeMrScheduleBtsHisDetail(mrScheduleBtsHisDetailDTO);
  }

  @Override
  public File exportData(MrScheduleBtsHisDTO mrScheduleBtsHisDTO) throws Exception {
    List<MrScheduleBtsHisDTO> mrScheduleBtsHisDTOList = mrScheduleBtsHisRepository
        .getDataExport(mrScheduleBtsHisDTO);
    if (mapLocationCountry.isEmpty()) {
      List<ItemDataCRInside> lstCountry = catLocationRepository
          .getListLocationByLevelCBB(null, 1L, null);
      for (ItemDataCRInside item : lstCountry) {
        mapLocationCountry.put(item.getValueStr().toString(), item.getDisplayStr());
      }
    }
    if (mapLocationArea.isEmpty()) {
      List<CatLocationDTO> lstCountry = catLocationRepository.getCatLocationByLevel("2");
      for (CatLocationDTO item : lstCountry) {
        mapLocationArea.put(item.getLocationCode(), item);
      }
    }
    if (mapLocationProvince.isEmpty()) {
      List<CatLocationDTO> lstCountry = catLocationRepository.getCatLocationByLevel("3");
      for (CatLocationDTO item : lstCountry) {
        mapLocationProvince.put(item.getPreCodeStation(), item);
      }
    }
    if (mrScheduleBtsHisDTOList != null && !mrScheduleBtsHisDTOList.isEmpty()) {
      for (int i = mrScheduleBtsHisDTOList.size() - 1; i > -1; i--) {
        MrScheduleBtsHisDTO mrScheduleBtsHisExport = mrScheduleBtsHisDTOList.get(i);
        if ("MPD".equals(mrScheduleBtsHisExport.getDeviceType())) {
          mrScheduleBtsHisExport
              .setDeviceTypeStr(I18n.getLanguage("mrScheduleBtsHis.deviceType.MPD"));
        } else if ("DH".equals(mrScheduleBtsHisExport.getDeviceType())) {
          mrScheduleBtsHisExport
              .setDeviceTypeStr(I18n.getLanguage("mrScheduleBtsHis.deviceType.DH"));
        }
        if (!StringUtils.isStringNullOrEmpty(mrScheduleBtsHisExport.getStatusWoGL())) {
          Long woStatusGL = Long.valueOf(mrScheduleBtsHisExport.getStatusWoGL());
          if (Constants.WO_STATE.getStateName().containsKey(woStatusGL)) {
            mrScheduleBtsHisExport
                .setStatusName(I18n.getLanguage(Constants.WO_STATE.getStateName().get(woStatusGL)));
          }
        } else {
          //them key getStatusWoGoc == null
          if (StringUtils.isStringNullOrEmpty(mrScheduleBtsHisExport.getStatusWoGoc())) {
            mrScheduleBtsHisExport.setStatusName("");
          } else {
            Long woStatusGoc = Long.valueOf(mrScheduleBtsHisExport.getStatusWoGoc());
            if (Constants.WO_STATE.getStateName().containsKey(woStatusGoc)) {
              mrScheduleBtsHisExport
                  .setStatusName(
                      I18n.getLanguage(Constants.WO_STATE.getStateName().get(woStatusGoc)));
            }
          }
        }
        if (!StringUtils.isStringNullOrEmpty(mrScheduleBtsHisExport.getMarketCode())
            && mapLocationCountry
            .containsKey(mrScheduleBtsHisExport.getMarketCode())) {
          mrScheduleBtsHisExport
              .setMarketCodeStr(mapLocationCountry.get(mrScheduleBtsHisExport.getMarketCode()));
        }
        if (!StringUtils.isStringNullOrEmpty(mrScheduleBtsHisExport.getAreaCode())
            && mapLocationArea
            .containsKey(mrScheduleBtsHisExport.getAreaCode())) {
          mrScheduleBtsHisExport.setAreaCodeStr(
              mapLocationArea.get(mrScheduleBtsHisExport.getAreaCode()).getLocationName());
        }
        if (!StringUtils.isStringNullOrEmpty(mrScheduleBtsHisExport.getProvinceCode())
            && mapLocationProvince
            .containsKey(mrScheduleBtsHisExport.getProvinceCode())) {
          mrScheduleBtsHisExport.setProvinceCodeStr(
              mapLocationProvince.get(mrScheduleBtsHisExport.getProvinceCode()).getLocationName());
        }
      }
    }

    //Lay listWO theo SERIAL
    List<MrScheduleBtsHisDetailInsiteDTO> scheduleBtsHisDetailInsiteDTOS = mrScheduleBtsHisRepository
        .getDataExportDetail(mrScheduleBtsHisDTO);
    return exportFileTemplate(mrScheduleBtsHisDTOList, scheduleBtsHisDetailInsiteDTOS);
  }

  @Override
  public ResultInSideDto reCreateWo(MrScheduleBtsHisDTO mrScheduleBtsHisDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    //<editor-fold desc="trungduong lay thong tin cap phe duyet tu bang cau hinh User_SMS_BTS">
    //Lay danh sách voi userLogin ben bang cau hinh UserApprove SMS
    UserToken userToken = ticketProvider.getUserToken();
    MrUserCfgApprovedSmsBtsDTO userCfgApprovedSmsBtsDTO = mrUserCfgApprovedSmsBtsRepository
        .getApproveLevelByUserLogin(userToken.getUserName());
    //</editor-fold>
    //tim wo moi nhat
    MrScheduleBtsHisDetailInsiteDTO dtoHisDetail = new MrScheduleBtsHisDetailInsiteDTO();
    dtoHisDetail.setSerial(mrScheduleBtsHisDTO.getSerial());
    dtoHisDetail.setDeviceType(mrScheduleBtsHisDTO.getDeviceType());
    dtoHisDetail.setCycle(mrScheduleBtsHisDTO.getCycle());
    dtoHisDetail.setWoCode(mrScheduleBtsHisDTO.getWoCode());
    List<MrScheduleBtsHisDetailInsiteDTO> lstWo1 = mrScheduleBtsRepository
        .getListWoCodeMrScheduleBtsHisDetailNOK(dtoHisDetail);
    if (lstWo1 == null || lstWo1.isEmpty()) {
      resultInSideDto.setKey(RESULT.ERROR);
      resultInSideDto.setMessage(I18n.getLanguage("mrScheduleBtsHis.recreateWo.cantFindChecklist"));
      return resultInSideDto;
    }
    if (lstWo1 != null && StringUtils.isStringNullOrEmpty(lstWo1.get(0).getWoCode())) {
      resultInSideDto.setKey(RESULT.ERROR);
      resultInSideDto.setMessage(I18n.getLanguage("mrScheduleBtsHis.recreateWo.cantFindChecklist"));
      return resultInSideDto;
    }

    /*
    List<MrScheduleBtsHisDetailInsiteDTO> lstChecklist = mrDeviceBtsRepository
        .getListWoBts(lstWo1.get(0).getWoCode());
    boolean isHandoverLevel2 = false;
    if (lstChecklist != null && !lstChecklist.isEmpty()) {
      isHandoverLevel2 = lstChecklist.stream().filter(item -> "0".equals(String.valueOf(item.getTaskApproveArea()))).findFirst().isPresent();
    } else {
      resultInSideDto.setKey(RESULT.ERROR);
      resultInSideDto.setMessage(I18n.getLanguage("mrScheduleBtsHis.recreateWo.cantFindChecklist"));
      return resultInSideDto;
    }*/
    //tim checklist cua wo
    List<MrScheduleBtsHisDetailInsiteDTO> lstChecklist = new ArrayList<>();
    if (userCfgApprovedSmsBtsDTO != null && userCfgApprovedSmsBtsDTO.getApproveLevel() != null
        && "1,2,3"
        .contains(userCfgApprovedSmsBtsDTO.getApproveLevel())) {// bo sung them cho thang cap 1 + 1
      lstChecklist = mrChecklistBtsDetailRepository
          .getAllCheckListLv2(mrScheduleBtsHisDTO.getWoCode());
    }

    //lay danh sach checklist Khong duyet

    //User không tồn tại bên bảng cấu hình. Không được phép giao lại
    List<MrScheduleBtsHisDetailInsiteDTO> lstChecklistFail = new ArrayList<>();
    if (userCfgApprovedSmsBtsDTO == null) {
      resultInSideDto.setKey(RESULT.ERROR);
      resultInSideDto
          .setMessage(I18n.getLanguage("mrScheduleBtsHis.recreateWo.noUserCfgAproveSmsBts"));
      return resultInSideDto;
    } else if (userCfgApprovedSmsBtsDTO != null) {
      for (MrScheduleBtsHisDetailInsiteDTO item : lstChecklist) {
        if ("1,3".contains(userCfgApprovedSmsBtsDTO.getApproveLevel()) && StringUtils
            .isStringNullOrEmpty(item.getTaskApprove())) {
          resultInSideDto.setKey(RESULT.ERROR);
          resultInSideDto.setMessage(I18n.getLanguage("mrScheduleBtsHis.approve.notnull2"));
          return resultInSideDto;
        }
        if ("2,3".contains(userCfgApprovedSmsBtsDTO.getApproveLevel()) && !StringUtils
            .isStringNullOrEmpty(item.getTaskApprove()) && StringUtils
            .isStringNullOrEmpty(item.getTaskApproveArea())) {
          resultInSideDto.setKey(RESULT.ERROR);
          resultInSideDto.setMessage(I18n.getLanguage("mrScheduleBtsHis.approve.notnull2"));
          return resultInSideDto;
        }
        if ("1,3".contains(userCfgApprovedSmsBtsDTO.getApproveLevel())) {
          if ("0".equals(String.valueOf(item.getTaskApprove()))
              || ("0".equals(String.valueOf(item.getTaskApproveArea())) && userCfgApprovedSmsBtsDTO
              .getUserName().equals(item.getApproveUser()))
              || "0".equals(String.valueOf(item.getTaskApproveArea()))
          ) {
            lstChecklistFail.add(item);
          }
        } else if ("2,3".contains(userCfgApprovedSmsBtsDTO.getApproveLevel())) {
          if ("0".equals(String.valueOf(item.getTaskApproveArea()))) {
            lstChecklistFail.add(item);
          }
        }
//        else if ("3".equals(userCfgApprovedSmsBtsDTO.getApproveLevel()) && "0"
//            .equals(String.valueOf(item.getTaskApproveArea()))) {
//          lstChecklistFail.add(item);
//        }
      }
    }

//    for (MrScheduleBtsHisDetailInsiteDTO item : lstChecklist) {
//      if (StringUtils.isStringNullOrEmpty(item.getTaskApprove())) {
//        resultInSideDto.setKey(RESULT.ERROR);
//        resultInSideDto.setMessage(I18n.getLanguage("mrScheduleBtsHis.approve.notnull2"));
//        return resultInSideDto;
//      }
//      if ("0".equals(item.getTaskApprove().toString())) {
//        lstChecklistFail.add(item);
//      }
//    }

    if (lstChecklistFail.isEmpty()) {
      resultInSideDto.setKey(RESULT.ERROR);
      resultInSideDto
          .setMessage(I18n.getLanguage("mrScheduleBtsHis.recreateWo.notHaveChecklistFail"));
      return resultInSideDto;
    } else {
      resultInSideDto.setLstResult(lstChecklistFail);
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto reCreateWoConfirm(MrScheduleBtsHisDTO mrScheduleBtsHisDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    UserToken userToken = ticketProvider.getUserToken();
    MrUserCfgApprovedSmsBtsDTO userCfgApprovedSmsBtsDTO = mrUserCfgApprovedSmsBtsRepository
        .getApproveLevelByUserLogin(userToken.getUserName());
    if (userCfgApprovedSmsBtsDTO == null) {
      resultInSideDto.setKey(RESULT.ERROR);
      resultInSideDto
          .setMessage(I18n.getLanguage("mrScheduleBtsHis.recreateWo.noUserCfgAproveSmsBts"));
      return resultInSideDto;
    }
    MrScheduleBtsHisDetailInsiteDTO dtoHisDetail = new MrScheduleBtsHisDetailInsiteDTO();
    dtoHisDetail.setSerial(mrScheduleBtsHisDTO.getSerial());
    dtoHisDetail.setDeviceType(mrScheduleBtsHisDTO.getDeviceType());
    dtoHisDetail.setCycle(mrScheduleBtsHisDTO.getCycle());
    dtoHisDetail.setWoCode(mrScheduleBtsHisDTO.getWoCode());
    List<MrScheduleBtsHisDetailInsiteDTO> lstWo = mrScheduleBtsRepository
        .getListWoCodeMrScheduleBtsHisDetail(dtoHisDetail);
    try {
      String[] woArr = lstWo.get(lstWo.size() - 1).getWoCode().split("_");
      String woId = woArr[woArr.length - 1];
      WoInsideDTO woInside = woServiceProxy.findWoByIdProxy(Long.valueOf(woId));
      WoDTO woOld = woInside.toModelOutSide();
      if (woOld == null) {
        resultInSideDto.setKey(RESULT.ERROR);
        resultInSideDto.setMessage(I18n.getLanguage("mrScheduleBtsHis.recreateWo.cantFindWo"));
        return resultInSideDto;
      }
      woOld.setWoId(null);
      woOld.setCreateDate(DateUtil.date2ddMMyyyyHHMMss(new Date()));
      woOld.setCreatePersonId(userToken.getUserID().toString());
      woOld.setResult(null);
      woOld.setFinishTime(null);
      woOld.setCompletedTime(null);
      if (!StringUtils.isStringNullOrEmpty(woOld.getFtId())) {
        String ft = userRepository.getUserName(Long.valueOf(woOld.getFtId()));
        woOld.setFtId(ft);
        woOld.setIsSendCd("0");
      } else {
        woOld.setIsSendCd("1");
      }

      List<MrScheduleBtsHisDetailInsiteDTO> lstChecklistFail = mrScheduleBtsHisDTO
          .getMrScheduleBtsHisDetailDTOList();
//      WoInsideDTO lastWoDto = null;
      if (lstChecklistFail != null && !lstChecklistFail.isEmpty()) {
        for (MrScheduleBtsHisDetailInsiteDTO mrScheduleBtsHisDetailInsiteDTO : lstChecklistFail) {
          if ("0".equals(String.valueOf(mrScheduleBtsHisDetailInsiteDTO.getTaskApproveArea()))) {
            woOld.setWoContent(
                woOld.getWoContent() + " - " + I18n.getLanguage("mrbts.his.assignWo.2"));
          } else {
            woOld.setWoContent(
                woOld.getWoContent() + " - " + I18n.getLanguage("mrbts.his.assignWo.1"));
          }
//          lastWoDto = woServiceProxy.findWoByWoCodeNoOffset(mrScheduleBtsHisDetailInsiteDTO.getWoCode());
          break;
        }
      }

      try {
//        Calendar cl = Calendar.getInstance();
//        cl.setTime(new Date());
//        cl.add(Calendar.DAY_OF_MONTH, 10);

//        Date startDate = DateUtil
//            .addDay(new Date(), getTimeReCreateWO(lastWoDto == null ? null : lastWoDto.getReasonRejectId()));
        Date startDate = DateUtil
            .addDay(new Date(), getTimeReCreateWO(null));
        Date dEnd = DateUtil.addDay(startDate, 30);
        woOld.setEndTime(DateUtil.date2ddMMyyyyHHMMss(dEnd));
        woOld.setStartTime(DateUtil.date2ddMMyyyyHHMMss(startDate));
        woOld.setReasonRejectId(null);
      } catch (Exception e) {
        resultInSideDto.setKey(RESULT.ERROR);
        return resultInSideDto;
      }

      ResultDTO resultWo = woServiceProxy.createWoProxy(woOld);
      if ("SUCCESS".equals(resultWo.getMessage())) {
        String woCode = resultWo.getId();
        if (lstChecklistFail != null) {
          for (MrScheduleBtsHisDetailInsiteDTO item : lstChecklistFail) {
            item.setScheduleBtsHisDetailId(null);
//          if (item.getTaskApprove() != null && item.getTaskApprove() == 1L) {
//
//            item.setTaskApproveArea(null);
//            item.setApproveUserArea(null);
//            item.setApproveDateArea(null);
//          } else {
//            item.setTaskApprove(null);
//            item.setApproveUser(null);
//            item.setApproveDate(null);
//            item.setTaskApproveArea(null);
//            item.setApproveUserArea(null);
//            item.setApproveDateArea(null);
//          }
            item.setTaskApprove(null);
            item.setApproveUser(null);
            item.setApproveDate(null);
//            item.setReason(null);
//            tiennv sua lai khi giao lai thi gan nguyen nhan bang checklist cu, chi DinhPT said
            item.setTaskApproveArea(null);
            item.setApproveUserArea(null);
            item.setApproveDateArea(null);
//            item.setReasonArea(null);

            item.setTaskStatus(null);
            item.setImesMessage(null);
            item.setImesErrorCode(null);
            item.setWoCode(woCode);
            item.setWoCodeOriginal(mrScheduleBtsHisDTO.getWoCode());
            resultInSideDto = mrScheduleBtsHisRepository.insertHisDetail(item);
          }
        }
        if (!"SUCCESS".equals(resultInSideDto.getKey())) {
          String[] arrWoCode = woCode.split("_");
          String woIdNew = arrWoCode[arrWoCode.length - 1];
          //xoa wo
          woServiceProxy.deleteWo(Long.valueOf(woIdNew));
          resultInSideDto.setMessage(I18n.getLanguage("mrScheduleBtsHis.recreateWo.fail"));
        } else {
          resultInSideDto.setMessage(I18n.getLanguage("mrScheduleBtsHis.recreateWo.success"));

          //update cot IS_WO_ORIGINAL bang MR_SCHEDULE_BTS => danh dau WO goc
          String res = mrScheduleBtsHisRepository
              .updateMrScheduleBts(mrScheduleBtsHisDTO.getSerial(),
                  mrScheduleBtsHisDTO.getDeviceType(),
                  mrScheduleBtsHisDTO.getCycle());

          if (!"SUCCESS".equals(res)) {
            String[] arrWoCode = woCode.split("_");
            String woIdNew = arrWoCode[arrWoCode.length - 1];
            //xoa wo
            woServiceProxy.deleteWo(Long.valueOf(woIdNew));
            //xoa checklist gan vs wo
            MrScheduleBtsHisDetailInsiteDTO dtoNew = new MrScheduleBtsHisDetailInsiteDTO();
            dtoNew.setWoCode(woCode);
            mrScheduleBtsHisRepository.deleteMrScheduleBtsHisByWoCode(dtoNew);
            resultInSideDto.setMessage(I18n.getLanguage("mrScheduleBtsHis.recreateWo.fail"));
          } else {
            //update is_complete, conplete_date theo WO giao lai
            mrScheduleBtsHisRepository.updateMrScheduleBtsHis(mrScheduleBtsHisDTO.getSerial(),
                mrScheduleBtsHisDTO.getDeviceType(),
                mrScheduleBtsHisDTO.getCycle(),
                mrScheduleBtsHisDTO.getWoCode(),
                "",
                null
            );

            //tiennv comment khong insert vao WO giao lai
            //insert 1 ban ghi vao MR_SCHEDULE_BTS => danh dau WO giao lai
            mrScheduleBtsHisRepository
                .insertReassignWO(lstWo.get(lstWo.size() - 1).getWoCode(), woCode);
          }
        }
      } else {
        resultInSideDto.setMessage("Message from WO: " + resultWo.getMessage());
      }
    } catch (Exception e) {
      resultInSideDto.setMessage("Exception: " + e.getMessage());
    }
    return resultInSideDto;
  }

  private File exportFileTemplate(List<MrScheduleBtsHisDTO> dtoList, List<MrScheduleBtsHisDetailInsiteDTO> lstDetails)
      throws Exception {

    String fileNameOut;
    String subTitle;
    String sheetName = I18n.getLanguage("mrScheduleBtsHis.export.title");
    String title = I18n.getLanguage("mrScheduleBtsHis.export.title");
    List<ConfigFileExport> fileExportList = new ArrayList<>();
    ConfigHeaderExport columnSheet;
    List<ConfigHeaderExport> headerExportList = new ArrayList<>();
    columnSheet = new ConfigHeaderExport("marketCodeStr", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("areaCodeStr", "LEFT", false, 0, 0, new String[]{},
        null,
        "STRING");
    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("provinceCodeStr", "LEFT", false, 0, 0, new String[]{},
        null,
        "STRING");
    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("stationCode", "LEFT", false, 0, 0, new String[]{},
        null,
        "STRING");
    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("deviceTypeStr", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("serial", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("cycle", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("completeDate", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("woCode", "LEFT", false, 0, 0, new String[]{},
        null,
        "STRING");
    headerExportList.add(columnSheet);
//    columnSheet = new ConfigHeaderExport("statusStr", "LEFT", false, 0, 0, new String[]{},
//        null,
//        "STRING");
//    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("userManager", "LEFT", false, 0, 0, new String[]{},
        null,
        "STRING");
    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("wogiaoLai", "LEFT", false, 0, 0, new String[]{},
        null,
        "STRING");
    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("statusName", "LEFT", false, 0, 0, new String[]{},
        null,
        "STRING");
    headerExportList.add(columnSheet);
    fileNameOut = MR_SCHEDULE_BTS_HIS_EXPORT;
    subTitle = I18n
        .getLanguage("mrScheduleBtsHis.export.exportDate", DateTimeUtils.convertDateOffset());
    Map<String, String> fieldSplit = new HashMap<>();
    if (dtoList != null && !dtoList.isEmpty()) {
      for (MrScheduleBtsHisDTO item : dtoList) {
        if (!StringUtils.isStringNullOrEmpty(item.getWoLstTag())) {
          item.setWogiaoLai(item.getWoLstTag());
        }

//        MrScheduleBtsHisDetailInsiteDTO dto = new MrScheduleBtsHisDetailInsiteDTO();
//        dto.setSerial(item.getSerial());
//        dto.setDeviceType(item.getDeviceType());
//        dto.setCycle(item.getCycle());
//        dto.setWoCode(item.getWoCode());
//        List<MrScheduleBtsHisDetailInsiteDTO> lstCheckList = mrScheduleBtsHisRepository
//            .getListWoCodeMrScheduleBtsHisDetail(dto);
//        if (lstCheckList != null && !lstCheckList.isEmpty()) {
//          String woCode = "";
//          for (MrScheduleBtsHisDetailInsiteDTO checkList : lstCheckList) {
//            if (!checkList.getWoCode().equals(item.getWoCode())) {
//              woCode += "," + checkList.getWoCode();// cộng dồn bắt đầu từ ,Wo1,Wo2,Wo3
//            }
//          }
//          if (woCode.startsWith(",")) { // Nếu dấu , đầu tiên
//            woCode = woCode.substring(1); // Thì cắt nó đi vị trí là 1
//          }
//          item.setWogiaoLai(woCode);
//        }

      }
    }

    ConfigFileExport configFileExport = new ConfigFileExport(
        dtoList
        , sheetName
        , title
        , subTitle
        , 7
        , 3
        , 9
        , true
        , "language.mrScheduleBtsHis"
        , headerExportList
        , fieldSplit
        , ""
        , I18n.getLanguage("common.export.firstLeftHeader")
        , I18n.getLanguage("common.export.secondLeftHeader")
        , I18n.getLanguage("common.export.firstRightHeader")
        , I18n.getLanguage("common.export.secondRightHeader")
    );
    configFileExport.setLangKey(I18n.getLocale());
    List<CellConfigExport> lstCellSheet = new ArrayList<>();
    CellConfigExport cellSheet;

    cellSheet = new CellConfigExport(7, 0, 0, 0, I18n.getLanguage("common.STT"),
        "HEAD", "STRING");
    lstCellSheet.add(cellSheet);
    configFileExport.setLstCreatCell(lstCellSheet);
    fileExportList.add(configFileExport);

    //add thêm du lieu List WO theo Rerial vào sheet 2
    if (lstDetails != null && lstDetails.size() > 0) {
      ConfigFileExport configFileExportTwo = getConfigFileExport(lstDetails);
      fileExportList.add(configFileExportTwo);
    }

    //Cấu hình đường dẫn
    String fileTemplate = "templates" + File.separator
        + "TEMPLATE_EXPORT.xlsx";
    String rootPath = tempFolder + File.separator;
    File fileExport = CommonExport.exportExcel(
        fileTemplate
        , fileNameOut
        , fileExportList
        , rootPath
        , new String[]{}
    );
    return fileExport;
  }

  public ConfigFileExport getConfigFileExport(
      List<MrScheduleBtsHisDetailInsiteDTO> dtoListExportSheet2) {
      if (dtoListExportSheet2 != null && dtoListExportSheet2.size() > 0) {
      for (MrScheduleBtsHisDetailInsiteDTO detailInsiteDTOtmp : dtoListExportSheet2) {
        //Loai thiet bi
        if ("MPD".equals(detailInsiteDTOtmp.getDeviceType())) {
          detailInsiteDTOtmp
              .setDeviceTypeStr(I18n.getLanguage("mrScheduleBtsHis.deviceType.MPD"));
        } else if ("DH".equals(detailInsiteDTOtmp.getDeviceType())) {
          detailInsiteDTOtmp
              .setDeviceTypeStr(I18n.getLanguage("mrScheduleBtsHis.deviceType.DH"));
        }

        //Trang thai phe duyet cua tinh
        if ("1".equals(String.valueOf(detailInsiteDTOtmp.getTaskApprove()))) {
          detailInsiteDTOtmp
              .setTaskApproveStr(I18n.getLanguage("mrScheduleBtsHis.exportSheet2.taskApprove.1"));
        } else if ("0".equals(String.valueOf(detailInsiteDTOtmp.getTaskApprove()))) {
          detailInsiteDTOtmp
              .setTaskApproveStr(I18n.getLanguage("mrScheduleBtsHis.exportSheet2.taskApprove.0"));
        }

        //Trang thai phe duyet cua khu vuc
        if ("1".equals(String.valueOf(detailInsiteDTOtmp.getTaskApproveArea()))) {
          detailInsiteDTOtmp
              .setTaskApproveAreaStr(
                  I18n.getLanguage("mrScheduleBtsHis.exportSheet2.taskApprove.1"));
        } else if ("0".equals(String.valueOf(detailInsiteDTOtmp.getTaskApproveArea()))) {
          detailInsiteDTOtmp
              .setTaskApproveAreaStr(
                  I18n.getLanguage("mrScheduleBtsHis.exportSheet2.taskApprove.0"));
        }
      }
    }
    String sheetName = I18n.getLanguage("mrScheduleBtsHis.exportSheet2.titleTwo");
    String title = I18n.getLanguage("mrScheduleBtsHis.exportSheet2.titleTwo");
//    List<ConfigFileExport> fileExports = new ArrayList<>();
    ConfigHeaderExport columnSheet;
    List<ConfigHeaderExport> lstHeaderSheet = new ArrayList<>();
    columnSheet = new ConfigHeaderExport("serial", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    lstHeaderSheet.add(columnSheet);
    columnSheet = new ConfigHeaderExport("woCode", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    lstHeaderSheet.add(columnSheet);
    columnSheet = new ConfigHeaderExport("content", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    lstHeaderSheet.add(columnSheet);
    columnSheet = new ConfigHeaderExport("deviceTypeStr", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    lstHeaderSheet.add(columnSheet);
    columnSheet = new ConfigHeaderExport("photoReq", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    lstHeaderSheet.add(columnSheet);
    columnSheet = new ConfigHeaderExport("minPhoto", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    lstHeaderSheet.add(columnSheet);
    columnSheet = new ConfigHeaderExport("maxPhoto", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    lstHeaderSheet.add(columnSheet);
    columnSheet = new ConfigHeaderExport("cycle", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    lstHeaderSheet.add(columnSheet);
    columnSheet = new ConfigHeaderExport("captureGuide", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    lstHeaderSheet.add(columnSheet);
    columnSheet = new ConfigHeaderExport("taskStatus", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    lstHeaderSheet.add(columnSheet);
    columnSheet = new ConfigHeaderExport("taskApproveStr", "LEFT", false, 0, 0, new String[]{},
        null,
        "STRING");
    lstHeaderSheet.add(columnSheet);
    columnSheet = new ConfigHeaderExport("approveUser", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    lstHeaderSheet.add(columnSheet);
    columnSheet = new ConfigHeaderExport("approveDate", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    lstHeaderSheet.add(columnSheet);
    columnSheet = new ConfigHeaderExport("reason", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    lstHeaderSheet.add(columnSheet);
    columnSheet = new ConfigHeaderExport("taskApproveAreaStr", "LEFT", false, 0, 0, new String[]{},
        null,
        "STRING");
    lstHeaderSheet.add(columnSheet);
    columnSheet = new ConfigHeaderExport("approveUserArea", "LEFT", false, 0, 0, new String[]{},
        null,
        "STRING");
    lstHeaderSheet.add(columnSheet);
    columnSheet = new ConfigHeaderExport("approveDateArea", "LEFT", false, 0, 0, new String[]{},
        null,
        "STRING");
    lstHeaderSheet.add(columnSheet);
    columnSheet = new ConfigHeaderExport("reasonArea", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    lstHeaderSheet.add(columnSheet);
    columnSheet = new ConfigHeaderExport("woCodeOriginal", "LEFT", false, 0, 0, new String[]{},
        null,
        "STRING");
    lstHeaderSheet.add(columnSheet);
    columnSheet = new ConfigHeaderExport("imesErrorCode", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    lstHeaderSheet.add(columnSheet);
    columnSheet = new ConfigHeaderExport("imesMessage", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    lstHeaderSheet.add(columnSheet);

    Map<String, String> fieldSplit = new HashMap<>();
    ConfigFileExport configFileExport = new ConfigFileExport(
        dtoListExportSheet2,
        sheetName,
        title,
        null
        , 7
        , 3
        , 9
        , true
        , "language.mrScheduleBtsHis.exportSheet2",
        lstHeaderSheet,
        fieldSplit,
        ""
        , I18n.getLanguage("common.export.firstLeftHeader")
        , I18n.getLanguage("common.export.secondLeftHeader")
        , I18n.getLanguage("common.export.firstRightHeader")
        , I18n.getLanguage("common.export.secondRightHeader")
    );
    configFileExport.setLangKey(I18n.getLocale());
    List<CellConfigExport> lstCellSheet = new ArrayList<>();
    CellConfigExport cellSheet;

    cellSheet = new CellConfigExport(7, 0, 0, 0, I18n.getLanguage("common.STT"),
        "HEAD", "STRING");
    lstCellSheet.add(cellSheet);
    configFileExport.setLstCreatCell(lstCellSheet);
    return configFileExport;
  }

  public int getTimeReCreateWO(String reasonRejectId) {
    if (StringUtils.isStringNullOrEmpty(reasonRejectId)) {
      return 0;
    }
    try {
      MrCauseWoWasCompletedDTO item = mrCauseWoWasCompletedBusiness.findById(Long.valueOf(reasonRejectId));
      if (item != null && StringUtils.isNotNullOrEmpty(item.getWaitingTime())) {
        return Double.valueOf(item.getWaitingTime()).intValue();
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return 0;
  }
}
