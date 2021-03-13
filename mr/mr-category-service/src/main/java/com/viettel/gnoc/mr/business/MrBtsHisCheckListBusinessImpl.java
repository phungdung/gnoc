package com.viettel.gnoc.mr.business;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.commons.model.UsersEntity;
import com.viettel.gnoc.commons.proxy.WoServiceProxy;
import com.viettel.gnoc.commons.repository.UserRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.Constants.ROLE;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.maintenance.dto.MrChecklistsBtsDetailDTO;
import com.viettel.gnoc.maintenance.dto.MrDeviceBtsDTO;
import com.viettel.gnoc.maintenance.dto.MrScheduleBtsHisDetailInsiteDTO;
import com.viettel.gnoc.maintenance.dto.MrScheduleBtsHisFileDTO;
import com.viettel.gnoc.maintenance.dto.MrUserCfgApprovedSmsBtsDTO;
import com.viettel.gnoc.mr.repository.MrChecklistBtsDetailRepository;
import com.viettel.gnoc.mr.repository.MrDeviceBtsRepository;
import com.viettel.gnoc.mr.repository.MrScheduleBtsHisRepository;
import com.viettel.gnoc.mr.repository.MrScheduleBtsRepository;
import com.viettel.gnoc.mr.repository.MrUserCfgApprovedSmsBtsRepository;
import com.viettel.gnoc.wo.dto.WoDTO;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import viettel.passport.client.UserToken;

@Service
@Transactional
@Slf4j
public class MrBtsHisCheckListBusinessImpl implements MrBtsHisCheckListBusiness {

  @Autowired
  UserRepository userRepository;

  @Autowired
  MrScheduleBtsRepository mrScheduleBtsRepository;

  @Autowired
  MrDeviceBtsRepository mrDeviceBtsRepository;

  @Autowired
  MrScheduleBtsHisRepository mrScheduleBtsHisRepository;

  @Autowired
  WoServiceProxy woServiceProxy;

  @Autowired
  MrUserCfgApprovedSmsBtsRepository mrUserCfgApprovedSmsBtsRepository;

  @Autowired
  MrChecklistBtsDetailRepository mrChecklistBtsDetailRepository;

  @Autowired
  TicketProvider ticketProvider;

  @Value("${application.temp.folder}")
  private String tempFolder;

  @Override
  public ResultInSideDto checkIsApprovalReturnLstMrScheBtsHisDetail(String typeView,
      MrScheduleBtsHisDetailInsiteDTO form) {
    ResultInSideDto resultInSideDto = new ResultInSideDto(null, RESULT.SUCCESS, RESULT.SUCCESS);
    List<MrScheduleBtsHisDetailInsiteDTO> lstWo = getListWoCodeMrScheduleBtsHisDetailNew(form);
    List<MrScheduleBtsHisDetailInsiteDTO> lstDetail = new ArrayList<>();
    //<editor-fold desc="trungduong lay thong tin cap phe duyet tu bang cau hinh User_SMS_BTS">
    //Lay danh sách voi userLogin ben bang cau hinh UserApprove SMS
    UserToken userToken = ticketProvider.getUserToken();
    MrUserCfgApprovedSmsBtsDTO userCfgApprovedSmsBtsDTO = mrUserCfgApprovedSmsBtsRepository
        .getApproveLevelByUserLogin(userToken.getUserName());
    //</editor-fold>

    if (lstWo == null || lstWo.isEmpty()) {
      lstWo = new ArrayList<>();
      lstWo.add(form);
    }
    if (!"HISTORY".equals(typeView)) {
      if (lstWo.size() > 1) {
        List<MrScheduleBtsHisDetailInsiteDTO> lstTmp = new ArrayList<>();
        lstTmp.add(lstWo.get(lstWo.size() - 1));
        lstWo.clear();
        lstWo.addAll(lstTmp);
      }
    }
    Boolean isApproveAble = false;
    MrScheduleBtsHisDetailInsiteDTO woTemp = null;
    if (lstWo != null && !lstWo.isEmpty()) {
      for (int i = lstWo.size() - 1; i > -1; i--) {
        MrScheduleBtsHisDetailInsiteDTO w = lstWo.get(i);
        try {
          String[] arrWoCode = w.getWoCode().split("_");
          String woId = arrWoCode[arrWoCode.length - 1];
          WoDTO wo = woServiceProxy.findWoByIdWSProxy(Long.valueOf(woId));
          if (StringUtils.isStringNullOrEmpty(wo.getStatus())) {
            w.setWoStatus("");
          } else {
            w.setWoStatus(I18n.getLanguage(
                Constants.WO_STATE.getStateName().get(Long.valueOf(wo.getStatus()))));
          }
          String woResult = "8".equals(wo.getStatus()) ? wo.getResult() : "";
          w.setWoResult(
              StringUtils.isStringNullOrEmpty(woResult) ? "" : " - " + ("2".equals(woResult)
                  ? I18n.getLanguage("mrScheduleBtsHis.wo.result.ok")
                  : I18n.getLanguage("mrScheduleBtsHis.wo.result.nok")));
//          form.setWoFinishTime("8".equals(wo.getStatus()) ? wo.getFinishTime() : "");
          resultInSideDto.setFinishTime("8".equals(wo.getStatus()) ? wo.getFinishTime() : "");
          if (!StringUtils.isStringNullOrEmpty(wo.getFtId())) {
            UsersEntity ft = userRepository.getUserByUserId(Long.valueOf(wo.getFtId()));
            if (ft != null && ft.getUserId() != null) {
              w.setFtName(ft.getFullname() + " (" + ft.getUsername() + ")");
            }
          }

          //i == 0 la ban ghi wo cuoi cung
          if (i == 0) {
            //TrungDuong them dieu kien phe duyet lien quan den UserCfgApproveSmsBts
            // Hien thi nut phe duyet
            if ((!"8".equals(wo.getStatus()) || !"2".equals(wo.getResult()))) {
              isApproveAble = false;
            } else if (userCfgApprovedSmsBtsDTO == null) {
              isApproveAble = false;
            } else if (userCfgApprovedSmsBtsDTO != null) {
              if ("1".equals(userCfgApprovedSmsBtsDTO.getApproveLevel()) && "8"
                  .equals(wo.getStatus())
                  && w.getTaskApprove() <= 0) {
                isApproveAble = true;
              } else if ("2".equals(userCfgApprovedSmsBtsDTO.getApproveLevel())
                  && "8".equals(wo.getStatus()) && w.getTaskApprove() == 1) {
                isApproveAble = true;
              } else if ("3".equals(userCfgApprovedSmsBtsDTO.getApproveLevel()) && "8"
                  .equals(wo.getStatus())) {
                if (w.getTaskApproveArea() <= 0 || w.getTaskApprove() <= 0) {
                  isApproveAble = true;
                }
              }
            }
          }
          if (woTemp == null) {
            woTemp = w;
          }
          //tiennv them list tra ve client
          List<MrScheduleBtsHisDetailInsiteDTO> lstWorkInTab = checkShowRedAndGetFiles(
              w.getWoCode());
          if (lstWorkInTab != null && lstWorkInTab.size() > 0) {
            long checkTaskArea = 1;
            for (MrScheduleBtsHisDetailInsiteDTO tmpWoInTab : lstWorkInTab) {
//              if ("1".equals(String.valueOf(tmpWoInTab.getTaskApproveArea()))) {
//                w.setTaskApproveArea(1L);
//              }else
              if ("0".equals(String.valueOf(tmpWoInTab.getTaskApproveArea()))) {
//                w.setTaskApproveArea(0L);
                checkTaskArea = 0;
                break;
              } else if (tmpWoInTab.getTaskApproveArea() == null) {
                checkTaskArea = -1;
              }
            }
            if (checkTaskArea != -1) {
              w.setTaskApproveArea(checkTaskArea);
            }
          }

          w.setLstWorkInTab(lstWorkInTab);
          w.setIsApproveAble(isApproveAble);
          lstDetail.add(w);
        } catch (Exception e) {
          log.error(e.getMessage(), e);
          resultInSideDto.setMessage("Exception WO service: " + e.getMessage());
          resultInSideDto.setKey(RESULT.ERROR);
          resultInSideDto.setObject(null);
          resultInSideDto.setCheck(false);
          //isApproveAble = false;
          return resultInSideDto;
        }
      }

      //tiennv bo sung BTS them tab Phe duyet cap 2
      if (userCfgApprovedSmsBtsDTO != null && userCfgApprovedSmsBtsDTO.getApproveLevel() != null
          && "1,2,3".contains(userCfgApprovedSmsBtsDTO.getApproveLevel())) {
        List<MrScheduleBtsHisDetailInsiteDTO> lst = getChkListAndFilesLv2(form.getWoCode());
        MrScheduleBtsHisDetailInsiteDTO item = new MrScheduleBtsHisDetailInsiteDTO();

        if (lst != null && lstDetail != null && !lstDetail.isEmpty() && lstDetail.size() > 1) {
          //StringBuilder wo_code_name = new StringBuilder();
          lstDetail.forEach(u -> {
            //wo_code_name.append("," + u.getWoCode());
            u.setIsApproveAble(false);
          });
//          if (wo_code_name.toString().startsWith(",")) {
////            item.setWoCode(wo_code_name.toString().substring(1));
//            item.setWoCode("APPROVE_2");
//          }
          item.setWoCode("APPROVE_2");
          isApproveAble = true;
          if (woTemp != null) {
            item.setWoStatus(woTemp.getWoStatus());
            item.setWoResult(woTemp.getWoResult());
            item.setFtName(woTemp.getFtName());
            if ("-1".equals(String.valueOf(lst.get(0).getTotalRow()))) {
              item.setTaskApproveArea(0L);
              item.setIsApproveAble(true);
            } else if ("-2".equals(String.valueOf(lst.get(0).getTotalRow()))) {
              item.setTaskApproveArea(1L);
              item.setIsApproveAble(false);
              isApproveAble = false;
            } else {
              item.setTaskApproveArea(-999999L);
              item.setIsApproveAble(true);
            }
          }
          if ("1".equals(userCfgApprovedSmsBtsDTO.getApproveLevel())) {
            isApproveAble = false;
          }
          item.setTaskApprove(1L);
          item.setLstWorkInTab(lst);
          lstDetail.add(item);
        }
      }
    }
    resultInSideDto.setCheck(isApproveAble);
    resultInSideDto.setObject(lstDetail);

    return resultInSideDto;
  }

  @Override
  public ResultInSideDto checkIsHaveRole(String typeView, String woCode) {
//    boolean isHaveRole = false;
    ResultInSideDto resultInSideDto = new ResultInSideDto(null, RESULT.SUCCESS, RESULT.SUCCESS);
    resultInSideDto.setObject(false);
    try {
      UserToken userToken = TicketProvider.getUserToken();
      UsersInsideDto dto = new UsersInsideDto();
      dto.setRoleCode(ROLE.ROLE_CNKTT_VTNET);
      dto.setUsername(userToken.getUserName());
      resultInSideDto.setObject(userRepository.checkAccountHaveRole(dto));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return resultInSideDto;
  }

  @Override
  public List<MrScheduleBtsHisDetailInsiteDTO> getListWoCodeMrScheduleBtsHisDetail(
      MrScheduleBtsHisDetailInsiteDTO dto) {
    return mrScheduleBtsRepository.getListWoCodeMrScheduleBtsHisDetail(dto);
  }

  //TrungDuong them
  @Override
  public List<MrScheduleBtsHisDetailInsiteDTO> getListWoCodeMrScheduleBtsHisDetailNew(
      MrScheduleBtsHisDetailInsiteDTO dto) {
    return mrScheduleBtsRepository.getListWoCodeMrScheduleBtsHisDetailNew(dto);
  }

  //mo checklist BTS detail goi ham nay truoc tien, neu khong co wo nao thi ko mo tab
  @Override
  public List<MrScheduleBtsHisDetailInsiteDTO> getCheckListByWoId(String woCode) {
    try {
      return mrDeviceBtsRepository.getListWoBts(woCode);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return new ArrayList<>();
  }

  @Override
  public List<MrScheduleBtsHisDetailInsiteDTO> checkShowRedAndGetFiles(String woCode) {
    List<MrScheduleBtsHisDetailInsiteDTO> lstTmp = mrDeviceBtsRepository.getListWoBts(woCode);
    //<editor-fold desc="trungduong lay thong tin cap phe duyet tu bang cau hinh User_SMS_BTS">
    //Lay thong tin userLogin ben bang cau hinh UserApprove SMS
    UserToken userToken = ticketProvider.getUserToken();
    MrUserCfgApprovedSmsBtsDTO userCfgApprovedSmsBtsDTO = mrUserCfgApprovedSmsBtsRepository
        .getApproveLevelByUserLogin(userToken.getUserName());
    //</editor-fold>
    if (lstTmp != null && !lstTmp.isEmpty()) {
      int i = 1;
      boolean showRed = false;
      for (MrScheduleBtsHisDetailInsiteDTO w : lstTmp) {
        //Xu ly phe duyet
        //<editor-fold desc="Comment doi xu ly ben tien trinh">
        // Kiem tra "time hien tai - thoi gian phe duyet cap 1 >= 30 ngay >> tu dong chuyen sang phe duyet cap 2 OK"
//          if (w.getApproveDate() != null && "1".equals(String.valueOf(w.getTaskApprove()))) {
//            Date sysdate = new Date();
//            Date approveDate = w.getApproveDate();
//            if ((sysdate.getTime() - approveDate.getTime()) / (1000 * 60 * 60 * 24) >= 30) {
//              w.setTaskApproveArea(1L);
//            }
//          }
        //</editor-fold>
        //validate hien tick xanh
        List<MrScheduleBtsHisFileDTO> lstFiles = mrDeviceBtsRepository.getListFileByCheckListWo(
            StringUtils.isStringNullOrEmpty(w.getCheckListId()) ? null
                : String.valueOf(w.getCheckListId()), w.getWoCode());
        if (!StringUtils.isStringNullOrEmpty(w.getPhotoReq()) && "1".equals(w.getPhotoReq())) {
          //co anh + duyet ok + trang thai ok => hien tick xanh
          if (lstFiles == null || lstFiles.isEmpty()) {
            //khong co anh
            showRed = true;
            w.setIsHaveLstFile("0");
          } else {
            w.setIsHaveLstFile("1");
            w.setLstFile(lstFiles);
          }

          if (!"1".equals(String.valueOf(w.getTaskApprove()))) {
            //duyet nok hoac chua duyet
            showRed = true;
          }
          if (StringUtils.isStringNullOrEmpty(w.getTaskStatus()) || "NOK"
              .equals(w.getTaskStatus())) {
            //trang thai nok
            showRed = true;
          }

        } else if (!StringUtils.isStringNullOrEmpty(w.getPhotoReq()) && "0"
            .equals(w.getPhotoReq())) {
          //khong co anh + duyet ok/chua duyet + trang thai ok => hien tick xanh
          if (StringUtils.isStringNullOrEmpty(w.getTaskStatus()) || "NOK"
              .equals(w.getTaskStatus())) {
            //trang thai nok
            showRed = true;
          }
          if (!"1".equals(String.valueOf(w.getTaskApprove()))) {
            //duyet nok --> sua thanh khac duyet OK
            showRed = true;
          }
          if (lstFiles != null && !lstFiles.isEmpty()) {
            w.setLstFile(lstFiles);
          }
        }

        //Trungduong them dieu kien nang cap
        if (userCfgApprovedSmsBtsDTO != null && StringUtils.isNotNullOrEmpty(userCfgApprovedSmsBtsDTO.getApproveLevel())) {
          if ("1".equals(userCfgApprovedSmsBtsDTO.getApproveLevel()) && !"1"
              .equals(String.valueOf(w.getTaskApprove()))) {
            showRed = true;
          } else if ("2".equals(userCfgApprovedSmsBtsDTO.getApproveLevel())
              && w.getTaskApproveArea() == null) {
            showRed = true;
          } else if ("3".equals(userCfgApprovedSmsBtsDTO.getApproveLevel()) && "1"
              .equals(String.valueOf(w.getTaskApproveArea()))) {
            showRed = true;
          }
        }

        w.setIsShowRed(showRed);
      }
    }
    return lstTmp;
  }

  @Override
  public ResultInSideDto actionSaveList(MrScheduleBtsHisDetailInsiteDTO dtoUpdate) {
    //<editor-fold desc="trungduong lay thong tin cap phe duyet tu bang cau hinh User_SMS_BTS">
    //Lay user ben bang cau hinh tu UserLogin
    UserToken userToken = ticketProvider.getUserToken();
    boolean isOnlyApproveLv1 = true;
    MrUserCfgApprovedSmsBtsDTO userCfgApprovedSmsBtsDTO = mrUserCfgApprovedSmsBtsRepository
        .getApproveLevelByUserLogin(userToken.getUserName());
    //</editor-fold>

    List<MrScheduleBtsHisDetailInsiteDTO> lstUpdate = dtoUpdate.getLstWorkInTab();
    if (lstUpdate != null && !lstUpdate.isEmpty()) {
      for (MrScheduleBtsHisDetailInsiteDTO item : lstUpdate) {
        //xu ly cho user cap 3:
        if ("1".equals(String.valueOf(item.getTaskApprove()))) {
          item.setReason("");
        }
        if ("1".equals(String.valueOf(item.getTaskApproveArea()))) {
          item.setReasonArea("");
          isOnlyApproveLv1 = false;
        }
        if("3".equals(userCfgApprovedSmsBtsDTO.getApproveLevel())){
          //Neu chua phe duyet tu cap 1 thi set het gia tri cho 6 truong sau:
          if(StringUtils.isStringNullOrEmpty(item.getApproveUser()) && StringUtils.isStringNullOrEmpty(item.getApproveDate())){
            item.setApproveDate(new Date());
            item.setApproveUser(userCfgApprovedSmsBtsDTO.getUserName());
            item.setApproveDateArea(new Date());
            item.setApproveUserArea(userCfgApprovedSmsBtsDTO.getUserName());
            if("3".equals(userCfgApprovedSmsBtsDTO.getApproveLevel())){
              item.setTaskApprove(item.getTaskApproveArea());
            }
          }else if(!StringUtils.isStringNullOrEmpty(item.getApproveUser()) && !StringUtils.isStringNullOrEmpty(item.getApproveDate())){
            //Neu da phe duyet cap 1 roi thi chi update cua truong khu vuc
            item.setApproveDateArea(new Date());
            item.setApproveUserArea(userCfgApprovedSmsBtsDTO.getUserName());
          }
        }
        mrScheduleBtsHisRepository.editHisDetail(item);
      }

      boolean isComplete = true;
      boolean isApproveLv1 = false;
      if (userCfgApprovedSmsBtsDTO != null) {
        //Xu ly cu neu User co quyen cap 1
        if ("1".equals(userCfgApprovedSmsBtsDTO.getApproveLevel())) {
          for (MrScheduleBtsHisDetailInsiteDTO item : lstUpdate) {
              isApproveLv1 = true;
            if (!StringUtils.isStringNullOrEmpty(item.getPhotoReq()) && "1"
                .equals(item.getPhotoReq())) {
              if ("1".equals(item.getIsHaveLstFile()) && "1"
                  .equals(String.valueOf(item.getTaskApprove()))
                  && "OK".equals(item.getTaskStatus())) {
                continue;
              } else {
                isComplete = false;
                break;
              }
            } else if (!StringUtils.isStringNullOrEmpty(item.getPhotoReq()) && "0"
                .equals(item.getPhotoReq())) {
              if ("OK".equals(item.getTaskStatus()) && (
                  "1".equals(String.valueOf(item.getTaskApprove())) || StringUtils
                      .isStringNullOrEmpty(item.getTaskApprove()))) {
                continue;
              } else {
                isComplete = false;
                break;
              }
            }
          }
        } else if ("2".equals(userCfgApprovedSmsBtsDTO.getApproveLevel()) || "3"
            .equals(userCfgApprovedSmsBtsDTO.getApproveLevel())) {
          //Neu User co quyen cap 2
          for (MrScheduleBtsHisDetailInsiteDTO item : lstUpdate) {
            if (!StringUtils.isStringNullOrEmpty(item.getPhotoReq()) && "1"
                .equals(item.getPhotoReq())) {
              if ("1".equals(item.getIsHaveLstFile()) && "1"
                  .equals(String.valueOf(item.getTaskApproveArea()))
                  && "OK".equals(item.getTaskStatus())) {
                continue;
              } else {
                isComplete = false;
                break;
              }
            } else if (!StringUtils.isStringNullOrEmpty(item.getPhotoReq()) && "0"
                .equals(item.getPhotoReq())) {
              if ("OK".equals(item.getTaskStatus()) && (
                  "1".equals(String.valueOf(item.getTaskApproveArea())) || StringUtils
                      .isStringNullOrEmpty(item.getTaskApproveArea()))) {
                continue;
              } else {
                isComplete = false;
                break;
              }
            }
          }
        }
      }

      //update IS_COMPLETE trong DB
      String isCompleteUpdate = "";
      if (userCfgApprovedSmsBtsDTO != null) {
        if (isApproveLv1) {
          if ("1".equals(userCfgApprovedSmsBtsDTO.getApproveLevel())) {
            isCompleteUpdate = (isComplete ? "1" : "0");//cot can update
          } else {
            isComplete = false;
          }
        } else {
          if ("2".equals(userCfgApprovedSmsBtsDTO.getApproveLevel()) || "3"
              .equals(userCfgApprovedSmsBtsDTO.getApproveLevel())) {
            isCompleteUpdate = (isComplete ? "4" : "5");//cot can update
          } else {
            isComplete = false;
          }
        }
      } else {
        isComplete = false;
      }

      String woCode = "";
      if (!StringUtils.isStringNullOrEmpty(lstUpdate.get(0).getWoCodeOriginal())) {
        woCode = lstUpdate.get(0).getWoCodeOriginal();//dieu kien
      } else {
        woCode = lstUpdate.get(0).getWoCode();//dieu kien
      }

      mrScheduleBtsHisRepository
          .updateMrScheduleBtsHis(lstUpdate.get(0).getSerial(), lstUpdate.get(0).getDeviceType(),
              lstUpdate.get(0).getCycle(), woCode, isCompleteUpdate, isOnlyApproveLv1 ? dtoUpdate.getWoFinishTime() : null);

      if (isComplete) {
        //neu duyet het thi cap nhat maintenance_time trong mr_device_bts
        MrDeviceBtsDTO device = new MrDeviceBtsDTO();
        device.setSerial(lstUpdate.get(0).getSerial());
        device.setDeviceType(lstUpdate.get(0).getDeviceType());
        List<MrDeviceBtsDTO> listMrDeviceBtsDTO = mrDeviceBtsRepository
            .getListMrDeviceBtsDTO(device);
        if (listMrDeviceBtsDTO != null && !listMrDeviceBtsDTO.isEmpty()) {
          if (isOnlyApproveLv1) {
            for (MrDeviceBtsDTO mrDeviceBtsDTO : listMrDeviceBtsDTO) {
              mrDeviceBtsDTO.setMaintenanceTime(dtoUpdate.getWoFinishTime());
            }
            mrDeviceBtsRepository.updateMaintenanceTimeMrDeviceBts(listMrDeviceBtsDTO);
          }
          try {
            String resDelSchedule = mrScheduleBtsHisRepository
                .deleteMrScheduleBtsByWoCode(lstUpdate.get(0).getWoCode());
            if (!"SUCCESS".equals(resDelSchedule)) {
              System.err.println("Xoa lich that bai, WO_CODE: " + lstUpdate.get(0).getWoCode());
            }
          } catch (Exception e) {
            log.error(e.getMessage());
            System.err.println(e.getMessage());
          }
        }
      }
      return new ResultInSideDto(null, RESULT.SUCCESS, RESULT.SUCCESS);
    }
    return new ResultInSideDto(null, RESULT.ERROR, RESULT.ERROR);
  }

  public List<MrScheduleBtsHisDetailInsiteDTO> getChkListAndFilesLv2(String woCode) {
    List<MrScheduleBtsHisDetailInsiteDTO> lstTmp = mrChecklistBtsDetailRepository.getAllCheckListLv2(woCode);
    boolean isAllowAproveLv2 = false;
    boolean isNotOK = false;
    //</editor-fold>
    if (lstTmp != null && !lstTmp.isEmpty()) {
      for (MrScheduleBtsHisDetailInsiteDTO w : lstTmp) {
        if (!"1".equals(String.valueOf(w.getTaskApprove()))) {
          return null;
        }
        if ("0".equals(String.valueOf(w.getTaskApproveArea()))) {
          isNotOK = true;
        }

        if (w.getTaskApproveArea() == null || "0".equals(String.valueOf(w.getTaskApproveArea()))) {
          isAllowAproveLv2 = true;
        }
        List<MrScheduleBtsHisFileDTO> lstFiles = mrDeviceBtsRepository.getListFileByCheckListWo(
            StringUtils.isStringNullOrEmpty(w.getCheckListId()) ? null
                : String.valueOf(w.getCheckListId()), w.getWoCode());
        if (!StringUtils.isStringNullOrEmpty(w.getPhotoReq()) && "1".equals(w.getPhotoReq())) {
          if (lstFiles == null || lstFiles.isEmpty()) {
            w.setIsHaveLstFile("0");
          } else {
            w.setIsHaveLstFile("1");
            w.setLstFile(lstFiles);
          }

        } else if (!StringUtils.isStringNullOrEmpty(w.getPhotoReq()) && "0"
            .equals(w.getPhotoReq())) {
          if (lstFiles != null && !lstFiles.isEmpty()) {
            w.setLstFile(lstFiles);
          }
        }
      }

      if (isAllowAproveLv2) {
        if (isNotOK) {
          lstTmp.get(0).setTotalRow(-1);
        } else {
          lstTmp.get(0).setTotalRow(-9);
        }
      } else {
        lstTmp.get(0).setTotalRow(-2);
      }
      return lstTmp;
    }
    return  null;
  }

}
