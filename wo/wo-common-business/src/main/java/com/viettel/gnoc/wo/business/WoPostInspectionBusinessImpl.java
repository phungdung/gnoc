package com.viettel.gnoc.wo.business;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.viettel.gnoc.commons.business.CommonBusiness;
import com.viettel.gnoc.commons.business.UserBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.model.UsersEntity;
import com.viettel.gnoc.commons.repository.GnocFileRepository;
import com.viettel.gnoc.commons.repository.UnitRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.GNOC_FILE_BUSSINESS;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.ExcelWriterUtils;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.wo.dto.ObjKeyValue;
import com.viettel.gnoc.wo.dto.WoDTO;
import com.viettel.gnoc.wo.dto.WoInsideDTO;
import com.viettel.gnoc.wo.dto.WoPostInspectionDTO;
import com.viettel.gnoc.wo.dto.WoPostInspectionInsideDTO;
import com.viettel.gnoc.wo.repository.WoPostInspectionRepository;
import com.viettel.gnoc.wo.repository.WoRepository;
import com.viettel.security.PassTranformer;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class WoPostInspectionBusinessImpl implements WoPostInspectionBusiness {

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

  @Value("${application.temp.folder}")
  private String tempFolder;

  @Value("${application.upload.folder}")
  private String uploadFolder;

  @Value("${application.extensionAllow:null}")
  private String extension;

  @Autowired
  CommonBusiness commonBusiness;

  @Autowired
  UserBusiness userBusiness;

  @Autowired
  WoBusiness woBusiness;

  @Autowired
  WoRepository woRepository;

  @Autowired
  WoPostInspectionRepository woPostInspectionRepository;

  @Autowired
  UnitRepository unitRepository;

  @Autowired
  GnocFileRepository gnocFileRepository;

  @Override
  public String insertWOPostInspectionFromVsmart(List<WoPostInspectionDTO> lstInspectionDTO,
      List<ObjKeyValue> lstObjKeyValue) {
    boolean checkResult = true;
    Integer point = 0;
    try {
      for (ObjKeyValue objKeyValue : lstObjKeyValue) {
        if (StringUtils.isInteger(objKeyValue.getValue())) {
          point += Integer.parseInt(objKeyValue.getValue());
        } else if ("Sai".equals(objKeyValue.getValue())) {
          checkResult = false;
        }
      }
      String pointTargetType =
          lstInspectionDTO.get(0).getWoTypeName().equals(Constants.AP_PARAM.WO_TYPE_NAME_HKSC)
              ? Constants.AP_PARAM.POINT_OK_HKSC : Constants.AP_PARAM.POINT_OK_HKTK;
      Map<String, String> map = commonBusiness.getConfigProperty();
      Long pointTarget = Long.parseLong(map.get(pointTargetType));
      if (point < pointTarget) {
        checkResult = false;
      }
      TypeToken<List<ObjKeyValue>> token = new TypeToken<List<ObjKeyValue>>() {
      };
      String json = new Gson().toJson(lstObjKeyValue, token.getType());
      lstInspectionDTO.get(0).setDataJson(json);
      lstInspectionDTO.get(0).setPoint(point.toString());
      lstInspectionDTO.get(0).setResult(checkResult ? "OK" : "NOK");
      if (lstInspectionDTO.get(0).getWoTypeName().equals(Constants.AP_PARAM.WO_TYPE_NAME_HKSC)) {
        String[] accountWoId = lstInspectionDTO.get(0).getAccountWoId().split("_");
        lstInspectionDTO.get(0).setAccountWoId(accountWoId[accountWoId.length - 1]);
      }

    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
    }
    return insertWOPostInspection(lstInspectionDTO);
  }

  @Override
  public String insertWOPostInspection(List<WoPostInspectionDTO> lstInspectionDTO) {
    if (lstInspectionDTO != null && !lstInspectionDTO.isEmpty()) {
      for (WoPostInspectionDTO inspectionDTO : lstInspectionDTO) {
        if (StringUtils.isStringNullOrEmpty(inspectionDTO.getWoId())) {
          return "WoId is not null";
        }
        if (StringUtils.isStringNullOrEmpty(inspectionDTO.getAccount())) {
          return "Account is not null";
        }
        if (StringUtils.isStringNullOrEmpty(inspectionDTO.getNote())) {
          return "Note is not null";
        }

        if (StringUtils.isStringNullOrEmpty(inspectionDTO.getCreatedTime())) {
          return "CreatedTime is not null";
        }
        if (!"".equals(DataUtil.validateDateTimeDdMmYyyy(inspectionDTO.getCreatedTime()))) {
          return "CreatedTime invalid";
        }
        String id = woPostInspectionRepository.getSeqPostInspection("WO_POST_INSPECTION_SEQ");
        inspectionDTO.setId(id);
        UsersEntity users = userBusiness.getUserByUserName(inspectionDTO.getReceiveUserName());
        try {
          updateFile(inspectionDTO, users);
        } catch (Exception ex) {
          log.info(ex.getMessage(), ex);
        }
        if ("NOK".equalsIgnoreCase(inspectionDTO.getResult()) && !StringUtils
            .isStringNullOrEmpty(inspectionDTO.getReceiveUserName())) {
          Date now = new Date();
          Calendar calendar = Calendar.getInstance();
          calendar.setTime(now);
          SimpleDateFormat dfm = new SimpleDateFormat("dd/MM/yyyy");
          calendar.add(Calendar.DATE, 1);
          String startWO = dfm.format(calendar.getTime()) + " 00:00:01";
          calendar.add(Calendar.DATE, 6);
          String endWO = dfm.format(calendar.getTime()) + " 23:59:59";

          WoDTO woDTO = new WoDTO();
          woDTO.setWoContent(
              I18n.getLanguage("wo.post.account").replaceAll("####", inspectionDTO.getAccount()));
          String pointTargetType =
              inspectionDTO.getWoTypeName().equals(Constants.AP_PARAM.WO_TYPE_NAME_HKSC)
                  ? Constants.AP_PARAM.POINT_OK_HKSC : Constants.AP_PARAM.POINT_OK_HKTK;
          String woTypeName =
              inspectionDTO.getWoTypeName().equals(Constants.AP_PARAM.WO_TYPE_NAME_HKSC)
                  ? I18n.getLanguage("wo.woTypeNameHKSC") : I18n.getLanguage("wo.woTypeNameHKTK");
          try {
            Map<String, String> map = commonBusiness.getConfigProperty();
            woDTO.setWoTypeId(map.get(Constants.AP_PARAM.VTT_KHAC_PHUC_HAU_KIEM));
            woDTO.setPriorityId(map.get(Constants.AP_PARAM.PRIORITY_HAU_KIEM));
            woDTO.setWoDescription(woTypeName + I18n.getLanguage("wo.woInspectionDescription")
                + inspectionDTO.getPoint() + "/"
                + map.get(pointTargetType) + ". Note:" + inspectionDTO.getNote());
          } catch (Exception e) {
            log.info(e.getMessage(), e);
          }
          woDTO.setWoSystem("HAU_KIEM_XLSC");

          woDTO.setCreateDate(DateTimeUtils.convertDateToString(now, Constants.ddMMyyyyHHmmss));
          woDTO.setStartTime(startWO);
          woDTO.setEndTime(endWO);
          woDTO.setCreatePersonId(users.getUserId().toString());
          woDTO.setFtName(inspectionDTO.getReceiveUserName());
          woDTO.setListFileName(inspectionDTO.getArrFileName());
          woDTO.setFileArr(inspectionDTO.getFileDocumentByteArray());
          ResultDTO res = woBusiness.createWoVsmart(woDTO);
          if (!"SUCCESS".equalsIgnoreCase(res.getKey())) {
            return res.getMessage();
          }
          inspectionDTO.setWoCodePin(res.getId());
        }

      }
    } else {
      return "lstInspectionDTO is not null";
    }
    ResultInSideDto resultInSideDto = woPostInspectionRepository.insertList(lstInspectionDTO);
    return resultInSideDto.getKey();
  }

  public void updateFile(WoPostInspectionDTO inspectionDTO, UsersEntity usersEntity)
      throws Exception {
    if (inspectionDTO.getArrFileName() != null && inspectionDTO.getArrFileName().size() > 0
        && inspectionDTO.getFileDocumentByteArray() != null
        && inspectionDTO.getFileDocumentByteArray().size() > 0) {
      if (inspectionDTO.getArrFileName().size() != inspectionDTO.getFileDocumentByteArray()
          .size()) {
        throw new Exception(I18n.getLanguage("wo.numberFileNotMap"));
      }
      List<GnocFileDto> gnocFileDtos = new ArrayList<>();

      if (usersEntity == null) {
        usersEntity = userBusiness.getUserByUserName("system");
      }
      List<String> fileNames = new ArrayList<>();
      for (int i = 0; i < inspectionDTO.getArrFileName().size(); i++) {
        if (extension != null) {
          String[] extendArr = extension.split(",");
          Boolean checkExt = false;
          for (String e : extendArr) {
            if (inspectionDTO.getArrFileName().get(i).toLowerCase().endsWith(e.toLowerCase())) {
              checkExt = true;
              break;
            }
          }
          if (!checkExt) {
            throw new Exception(I18n.getLanguage("wo.fileImportInvalidExten"));
          }
        }
        String fullPath = FileUtils
            .saveFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
                PassTranformer.decrypt(ftpPass), ftpFolder, inspectionDTO.getArrFileName().get(i),
                inspectionDTO.getFileDocumentByteArray().get(i),
                DateTimeUtils.convertStringToDate(inspectionDTO.getCreatedTime()));
        fileNames.add(FileUtils.getFileName(fullPath));

        try {
          UnitDTO unitToken = unitRepository.findUnitById(usersEntity.getUnitId());
          GnocFileDto gnocFileDto = new GnocFileDto();
          gnocFileDto.setPath(fullPath);
          gnocFileDto.setFileName(inspectionDTO.getArrFileName().get(i));
          gnocFileDto.setCreateUnitId(usersEntity.getUnitId());
          gnocFileDto.setCreateUnitName(unitToken.getUnitName());
          gnocFileDto.setCreateUserId(usersEntity.getUserId());
          gnocFileDto.setCreateUserName(usersEntity.getUsername());
          gnocFileDto
              .setCreateTime(DateTimeUtils.convertStringToDate(inspectionDTO.getCreatedTime()));
          gnocFileDto.setMappingId(Long.valueOf(inspectionDTO.getId()));
          gnocFileDtos.add(gnocFileDto);
        } catch (Exception e) {
          log.error(e.getMessage(), e);
        }
      }
      gnocFileRepository.saveListGnocFileNotDeleteAll(
          GNOC_FILE_BUSSINESS.WO_POST_INSPECTION, Long.valueOf(inspectionDTO.getId()),
          gnocFileDtos);
      inspectionDTO.setGnocFileDtos(gnocFileDtos);
      inspectionDTO.setFilename((StringUtils.isStringNullOrEmpty(inspectionDTO.getFilename()) ? ""
          : (inspectionDTO.getFilename() + ",")) + String.join(",", fileNames));
    }
  }

  @Override
  public List<WoPostInspectionInsideDTO> getListWOPostInspection(
      WoPostInspectionInsideDTO woPostInspectionDTO,
      int rowStart, int maxRow, String sortType, String sortFieldList) {
    return woPostInspectionRepository
        .getListWOPostInspection(woPostInspectionDTO, rowStart, maxRow, sortType, sortFieldList);
  }

  @Override
  public List<ObjKeyValue> loadWoPostInspectionChecklist(String woId, String accountName) {
    WoPostInspectionInsideDTO inspectionDTO = new WoPostInspectionInsideDTO();
    inspectionDTO.setAccount(accountName);
    inspectionDTO.setWoId(Long.valueOf(woId));
    List<WoPostInspectionInsideDTO> listInspectionDTO = woPostInspectionRepository
        .getListWOPostInspection(inspectionDTO, 0, Integer.MAX_VALUE, "ASC", "woId");
    List<ObjKeyValue> lst = new ArrayList<ObjKeyValue>();
    try {
      if (listInspectionDTO.isEmpty()) {
        WoInsideDTO wo = woBusiness.findWoByIdNoOffset(Long.parseLong(woId));
        if (wo != null && !StringUtils.isStringNullOrEmpty(wo.getFileName())) {
          String[] fileName = wo.getFileName().split(",");
          String path = ftpFolder + "/" + FileUtils.createPathFtpByDate(wo.getCreateDate()) + "/"
              + fileName[0].trim();
          byte[] bytes = FileUtils
              .getFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
                  PassTranformer.decrypt(ftpPass), path);
          String fullPath = FileUtils.saveTempFile(fileName[0].trim(), bytes, tempFolder);
          File fileSource = new File(fullPath);
          return getExcelDataForWoPostInspection(fileSource);
        }
      } else {
        inspectionDTO = listInspectionDTO.get(0);
        String jsonString = inspectionDTO.getDataJson();
        Gson gson = new Gson();
        JSONArray jsonArr = new JSONArray(jsonString);
        TypeToken<List<ObjKeyValue>> token = new TypeToken<>() {
        };
        lst = gson.fromJson(jsonArr.toString(), token.getType());
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return lst;
  }

  public List<ObjKeyValue> getExcelDataForWoPostInspection(File fileSource) {
    List<ObjKeyValue> lstResult = new ArrayList<>();
    List<Object[]> lstDataAll;
    //Read sheet 1
    try {
      lstDataAll = ExcelWriterUtils.readExcelAddBlankRowXSSF(fileSource,
          0,//sheet
          1,//begin row
          0,//from column
          4,//to column
          20);
      if (lstDataAll != null) {
        for (Object[] obj : lstDataAll) {
          ObjKeyValue objDTO = new ObjKeyValue();
          // Luu key
          if (obj[0] != null && !"".equals(obj[0])) {
            objDTO.setKey(obj[0].toString().trim());
          }
          // Luu defaultValue
          if (obj[2] != null && !"".equals(obj[2])) {
            objDTO.setDefaulValue(obj[2].toString().trim());
          }
          // bat buoc nhap ghi chu
          if (obj[3] != null && !"".equals(obj[3])) {
            objDTO.setRequiredComment(obj[3].toString().trim());
          }
          // bat buoc nhap anh
          if (obj[4] != null && !"".equals(obj[4])) {
            objDTO.setRequiredImage(obj[4].toString().trim());
          }
          lstResult.add(objDTO);
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return lstResult;
  }

  @Override
  public ResultInSideDto delete(Long id) {
    return woPostInspectionRepository.delete(id);
  }
}
