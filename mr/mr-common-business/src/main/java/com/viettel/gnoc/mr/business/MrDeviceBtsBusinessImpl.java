package com.viettel.gnoc.mr.business;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.GnocFileRepository;
import com.viettel.gnoc.commons.repository.UnitRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.GNOC_FILE_BUSSINESS;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.maintenance.dto.CPChecklistFileItemWP;
import com.viettel.gnoc.maintenance.dto.MrChecklistsBtsDTO;
import com.viettel.gnoc.maintenance.dto.MrDeviceBtsDTO;
import com.viettel.gnoc.maintenance.dto.MrScheduleBtsHisDetailDTO;
import com.viettel.gnoc.maintenance.dto.MrScheduleBtsHisFileDTO;
import com.viettel.gnoc.maintenance.dto.Result;
import com.viettel.gnoc.mr.repository.MrCheckListBtsRepository;
import com.viettel.gnoc.mr.repository.MrDeviceBtsRepository;
import com.viettel.gnoc.mr.repository.MrScheduleBtsHisFileRepository;
import com.viettel.gnoc.mr.repository.MrScheduleBtsHisRepository;
import com.viettel.security.PassTranformer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

@Service
@Transactional
@Slf4j
public class MrDeviceBtsBusinessImpl implements MrDeviceBtsBusiness {

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

  @Autowired
  MrDeviceBtsRepository mrDeviceBtsRepository;

  @Autowired
  TicketProvider ticketProvider;

  @Autowired
  UnitRepository unitRepository;

  @Autowired
  GnocFileRepository gnocFileRepository;

  @Autowired
  MrScheduleBtsHisRepository mrScheduleBtsHisRepository;

  @Autowired
  MrCheckListBtsRepository mrCheckListBtsRepository;

  @Autowired
  MrScheduleBtsHisFileRepository mrScheduleBtsHisFileRepository;

  @Override
  public Result updateWOChecklistFiles(String woCode, List<CPChecklistFileItemWP> fileItems) {
    Result result = new Result();
    result.setStatus(1L);
    try {
      StringBuilder errorMsg = new StringBuilder();
      if (StringUtils.isStringNullOrEmpty(woCode)) {
        result.setMessage(I18n.getLanguage("mrDeviceBtsService.check.WoCode"));
        return result;
      }

      if (fileItems != null) {
        for (int i = 0; i < fileItems.size(); i++) {
          CPChecklistFileItemWP item = fileItems.get(i);
          Long valueAIParse = null;
          Long photoRateParse = null;
          if (StringUtils.isNotNullOrEmpty(item.getValueAI())) {
            try {
              valueAIParse = Long.parseLong(item.getValueAI());
            } catch (Exception e) {
              log.error(e.getMessage(), e);
            }
            if (valueAIParse == null || valueAIParse < 0 || valueAIParse > 1) {
              TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
              result.setMessage(I18n.getLanguage("MrDeviceBts.valueAI.invalid"));
              return result;
            }
          }
          if (StringUtils.isNotNullOrEmpty(item.getPhotoRate())) {
            try {
              photoRateParse = Long.parseLong(item.getPhotoRate());
            } catch (Exception e) {
              log.error(e.getMessage(), e);
            }
            if (photoRateParse == null || photoRateParse < 0 || photoRateParse > 100) {
              TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
              result.setMessage(I18n.getLanguage("MrDeviceBts.photoRate.invalid"));
              return result;
            }
          }

          if (StringUtils.isNotNullOrEmpty(woCode) && (valueAIParse != null
              || photoRateParse != null)) {
            String resultAI = mrDeviceBtsRepository
                .updateAIByWoCode(woCode, item.getChecklistId(), valueAIParse, photoRateParse);
            if (!RESULT.SUCCESS.equals(resultAI)) {
              result.setMessage("Have some error!");
              return result;
            }
          }

          Date date = new Date();
          try {
            if (item.getFileContent() == null || item.getFileContent().length == 0) {
              errorMsg.append("{").append(I18n.getLanguage("MrDeviceBts.fileContent.notnull"))
                  .append(" ").append(woCode)
                  .append("},");
              continue;
            }
            String fileName = item.getFileName() == null || item.getFileName().isEmpty() ? "wobd"
                : item.getFileName();
            String dirpath = uploadFolder + "/" + DateTimeUtils
                .getSysDateTime("yyyy/MM/dd") + "/" + woCode + "/" + item.getChecklistId() + "/";
            String fullPath = FileUtils
                .saveFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
                    PassTranformer.decrypt(ftpPass), ftpFolder, fileName, item.getFileContent(),
                    date);
            String fullPathOld = FileUtils
                .saveUploadFile(FileUtils.getFileName(fullPath), item.getFileContent(), dirpath);
            //Save file old
            MrScheduleBtsHisFileDTO bo = new MrScheduleBtsHisFileDTO();
            bo.setWoId(woCode);
            bo.setFileName(fileName);
            bo.setFilePath(fullPathOld);
            bo.setUserUpdate(item.getUpdatedUser());
            bo.setChecklistId(item.getChecklistId());
            bo.setCreatedDate(DateTimeUtils.convertDateToString(date));
            ResultInSideDto resultFileDataOld = mrDeviceBtsRepository
                .insertMrScheduleBtsHisFile(bo);
            if ("SUCCESS".equals(resultFileDataOld.getKey())) {
              //Save file GnocFile
              List<GnocFileDto> gnocFileDtos = new ArrayList<>();
              GnocFileDto gnocFileDto = new GnocFileDto();
              gnocFileDto.setPath(fullPath);
              gnocFileDto.setFileName(fileName);
              gnocFileDto.setCreateTime(new Date());
              gnocFileDto.setCreateUserName(bo.getUserUpdate());
              gnocFileDto.setMappingId(resultFileDataOld.getId());
              gnocFileDtos.add(gnocFileDto);
              gnocFileRepository
                  .saveListGnocFileNotDeleteAll(GNOC_FILE_BUSSINESS.MR_SCHEDULE_BTS_HIS_FILE,
                      resultFileDataOld.getId(),
                      gnocFileDtos);
              //End Save file GnocFile
              result.setStatus(0L);
              result.setMessage(RESULT.SUCCESS);
            } else {
              result.setMessage(RESULT.FAIL);
            }
          } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            if (ex != null) {
              result.setMessage(ex.getMessage());
            }
          }
        }
      }
      if (errorMsg.length() != 0) {
        result.setMessage(errorMsg.toString());
        return result;
      }
    } catch (Exception ex) {
      TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
      log.error(ex.getMessage(), ex);
      if (ex != null) {
        result.setMessage(ex.getMessage());
      }
    }
    return result;
  }

  @Override
  public List<MrScheduleBtsHisFileDTO> getListFileByCheckListWo(String checklistId, String woId) {
    List<MrScheduleBtsHisFileDTO> res = new ArrayList<>();
    List<MrScheduleBtsHisFileDTO> listFiles = mrDeviceBtsRepository
        .getListFileByCheckListWo(checklistId, woId);
    for (MrScheduleBtsHisFileDTO dto : listFiles) {
      try {
        byte[] bytes = FileUtils
            .getFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
                PassTranformer.decrypt(ftpPass), dto.getFilePath());
        dto.setFileContent(bytes);
      } catch (IOException ex) {
        log.error(ex.getMessage(), ex);
      }
      res.add(dto);
    }
    return res;
  }

  @Override
  public List<MrScheduleBtsHisDetailDTO> getListWoBts(String woCode) {
    List<MrScheduleBtsHisDetailDTO> ret = new ArrayList<>();
    try {
      ret = mrScheduleBtsHisRepository.getListWoBts(woCode);
    } catch (Exception ex) {
      log.error(ex.getMessage());
    }
    return ret;
  }

  /**
   * Cap nhat trang thai task. Neu co checklist chua duoc upload anh ma photo req = 1 thi hien thi
   * message
   *
   * @modify on 14/01/2020: Neu co checklist chua duoc upload anh ma photo req = 1 => tra ra fail
   */
  @Override
  public List<String> updateStatusTask(List<MrScheduleBtsHisDetailDTO> mrScheduleBtsHisDetailDTO) {
    List<String> lstResults = new ArrayList<>();
    if (mrScheduleBtsHisDetailDTO != null && !mrScheduleBtsHisDetailDTO.isEmpty()) {
      List<String> lstWoCode = mrScheduleBtsHisDetailDTO.stream().map(MrScheduleBtsHisDetailDTO::getWoCode).distinct().collect(
          Collectors.toList());
      Map<String, List<MrChecklistsBtsDTO>> mapResultAll = new HashMap<>();
      if (lstWoCode != null && !lstWoCode.isEmpty()) {
        List<MrChecklistsBtsDTO> lstCheckAll = mrCheckListBtsRepository.getListScheduleBtsHisDTOByWoCodes(lstWoCode);
        if (lstWoCode != null && !lstWoCode.isEmpty()) {
          List<MrChecklistsBtsDTO> itemExist = null;
          for (MrChecklistsBtsDTO itemToMap : lstCheckAll) {
            if (itemToMap.getChecklistId() == null || StringUtils.isStringNullOrEmpty(itemToMap.getWoCode())) {
              continue;
            }
            String keyMapFirst = itemToMap.getChecklistId() + ";" + itemToMap.getWoCode();
            List<MrChecklistsBtsDTO> itemTemp = new ArrayList();
            if (mapResultAll.containsKey(keyMapFirst)) {
              itemExist = mapResultAll.get(keyMapFirst);
              itemTemp.addAll(itemExist);
            }
            itemTemp.add(itemToMap);
            mapResultAll.put(keyMapFirst, itemTemp);
          }
        }
      }
      for (MrScheduleBtsHisDetailDTO scheduleHisDTO : mrScheduleBtsHisDetailDTO) {
        MrChecklistsBtsDTO dto = new MrChecklistsBtsDTO();
        dto.setChecklistId(
            StringUtils.isNotNullOrEmpty(scheduleHisDTO.getCheckListId()) ?
                Long.valueOf(scheduleHisDTO.getCheckListId()) : null
        );
        dto.setWoCode(scheduleHisDTO.getWoCode());
//        List<MrChecklistsBtsDTO> lstCheck = mrCheckListBtsRepository
//            .getListScheduleBtsHisDTO(dto, 0, 0);
        String keyMap = String.valueOf(dto.getChecklistId()) + ";" + String.valueOf(dto.getWoCode());
        List<MrChecklistsBtsDTO> lstCheck = mapResultAll.get(keyMap);
        if (lstCheck != null && lstCheck.size() == 1) {
          if (Constants.PHOTO_REQ_YES.equals(String.valueOf(lstCheck.get(0).getPhotoReq()))) {
            MrScheduleBtsHisFileDTO scheduleBtsHisFileDTO = new MrScheduleBtsHisFileDTO();
            scheduleBtsHisFileDTO.setChecklistId(String.valueOf(lstCheck.get(0).getChecklistId()));
            scheduleBtsHisFileDTO.setWoId(scheduleHisDTO.getWoCode());
            // kiem tra xem checklist do da duoc upload anh hay chua
//            List<MrScheduleBtsHisFileDTO> lstFile = mrScheduleBtsHisFileRepository.getListMrScheduleBtsHisFileDTO(file, 0, 0);
            //Lay tai GNOC FILE
            List<MrScheduleBtsHisFileDTO> gnocFileDtoList = mrScheduleBtsHisFileRepository.getListMrScheduleBtsHisFileDTO(scheduleBtsHisFileDTO, 0, 0);
//            gnocFileDtoList = mrDeviceBtsRepository
//                .getListGnocFileByCheckListIdAndWoId(scheduleBtsHisFileDTO);

            if (gnocFileDtoList == null || gnocFileDtoList.isEmpty()) {
              lstResults.add(RESULT.FAIL);
              lstResults.add(lstCheck.get(0).getContent() + " " + I18n.getLanguage("MrDeviceBts.image.notupload"));
              return lstResults;
            }
            int maxCount = (lstCheck.get(0).getMaxPhoto()).intValue();
            int minCount = (lstCheck.get(0).getMinPhoto()).intValue();
            // check so anh khong vuot qua max va min
            if (gnocFileDtoList != null && !gnocFileDtoList.isEmpty()) {
              if (gnocFileDtoList.size() > maxCount || gnocFileDtoList.size() < minCount) {
                lstResults.add(RESULT.FAIL);
                lstResults.add(
                    "\"" + lstCheck.get(0).getContent() + "\" " + String.format(I18n.getLanguage("MrDeviceBts.image.invalid"), minCount, maxCount));
                return lstResults;
              }
            }
          }
        }
      }
      lstResults.add(mrDeviceBtsRepository.updateStatusTask(mrScheduleBtsHisDetailDTO));
    }
    return lstResults;
  }

  @Override
  public String updateStatusAfterMaintenance(String woCode, String lastMaintenanceTime,
      String status) {
    Date date = null;
    if (StringUtils.isStringNullOrEmpty(woCode)) {
      return I18n.getLanguage("MrDeviceBts.woCode.notnull");
    }
    if (!StringUtils.isStringNullOrEmpty(lastMaintenanceTime)) {
      try {
        if (StringUtils
            .isStringNullOrEmpty(DataUtil.validateDateTimeDdMmYyyy(lastMaintenanceTime))) {
          date = DateTimeUtils.string2Date(lastMaintenanceTime);
        } else {
          return I18n.getLanguage("MrDeviceBts.lastMaintenanceTime.invalid");
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
        return I18n.getLanguage("MrDeviceBts.lastMaintenanceTime.invalid");
      }

    }
    if (StringUtils.isStringNullOrEmpty(status)) {
      return I18n.getLanguage("MrDeviceBts.status.notnull");
    }
    return mrDeviceBtsRepository.updateStatusAfterMaintenance(woCode, date, status);
  }

  @Override
  public List<MrDeviceBtsDTO> getMrBTSDeviceInfor(String woCode) {
    return mrDeviceBtsRepository.getMrBTSDeviceInfor(woCode);
  }

}

