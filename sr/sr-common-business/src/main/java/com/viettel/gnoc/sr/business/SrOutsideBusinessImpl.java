package com.viettel.gnoc.sr.business;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.CatLocationDTO;
import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.dto.UsersDTO;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.commons.model.UsersEntity;
import com.viettel.gnoc.commons.proxy.SrCategoryServiceProxy;
import com.viettel.gnoc.commons.repository.CatLocationRepository;
import com.viettel.gnoc.commons.repository.GnocFileRepository;
import com.viettel.gnoc.commons.repository.UnitRepository;
import com.viettel.gnoc.commons.repository.UserRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.GNOC_FILE_BUSSINESS;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.Constants.SR_CONFIG;
import com.viettel.gnoc.commons.utils.Constants.SR_STATUS;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.DateUtil;
import com.viettel.gnoc.commons.utils.ExcelWriterUtils;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.sr.dto.SRCatalogDTO;
import com.viettel.gnoc.sr.dto.SRConfigDTO;
import com.viettel.gnoc.sr.dto.SRCreatedFromOtherSysDTO;
import com.viettel.gnoc.sr.dto.SRDTO;
import com.viettel.gnoc.sr.dto.SRFilesDTO;
import com.viettel.gnoc.sr.dto.SRRoleDTO;
import com.viettel.gnoc.sr.dto.SRWorkLogDTO;
import com.viettel.gnoc.sr.dto.SrInsiteDTO;
import com.viettel.gnoc.sr.repository.SRCatalogRepository2;
import com.viettel.gnoc.sr.repository.SRConfigRepository;
import com.viettel.gnoc.sr.repository.SRCreatedFromOtherSysRepository;
import com.viettel.gnoc.sr.repository.SRHisRepository;
import com.viettel.gnoc.sr.repository.SROutsideRepository;
import com.viettel.gnoc.sr.repository.SRWorkLogRepository;
import com.viettel.gnoc.sr.repository.SrRepository;
import com.viettel.security.PassTranformer;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.xml.security.utils.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
public class SrOutsideBusinessImpl implements SrOutsideBusiness {

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

  @Autowired
  SrBusiness srBusiness;

  @Autowired
  SRConfigRepository srConfigRepository;

  @Autowired
  SRCatalogRepository2 srCatalogRepository2;

  @Autowired
  SrRepository srRepository;

  @Autowired
  CatLocationRepository catLocationRepository;

  @Autowired
  UnitRepository unitRepository;

  @Autowired
  UserRepository userRepository;

  @Autowired
  SrCategoryServiceProxy srCategoryServiceProxy;

  @Autowired
  SROutsideRepository srOutsideRepository;

  @Autowired
  SRCreatedFromOtherSysRepository srCreatedFromOtherSysRepository;

  @Autowired
  GnocFileRepository gnocFileRepository;

  @Autowired
  SRWorkLogRepository srWorkLogRepository;

  @Autowired
  SRHisRepository srHisRepository;

  @Override
  public ResultDTO putResultFromVipa(String srId, String result, String fileContentError) {
    ResultDTO resultDTO = new ResultDTO();
    try {
      List<SRConfigDTO> vipaFileName = srConfigRepository
          .getByConfigGroup(Constants.SR_CONFIG.VIPA_FILE_NAME);
      String fileName = vipaFileName.get(0).getConfigCode() + ".xlsx";
      Calendar cal = Calendar.getInstance();
      String fullPath = "";
      String fullPathOld = "";
      try {
        Base64.decode(fileContentError);
        fullPath = FileUtils.saveFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
            PassTranformer.decrypt(ftpPass), ftpFolder, fileName, Base64.decode(fileContentError),
            null);
        fullPathOld = FileUtils
            .saveUploadFile(fileName, Base64.decode(fileContentError), uploadFolder, null);
      } catch (Exception e) {
        log.error(e.getMessage());
        String filePath = uploadFolder + "/"
            + cal.get(Calendar.YEAR)
            + "/" + (cal.get(Calendar.MONTH) + 1)
            + "/" + cal.get(Calendar.DAY_OF_MONTH)
            + "/";
        fullPath = filePath + cal.get(Calendar.HOUR)
            + "" + cal.get(Calendar.MINUTE)
            + "" + cal.get(Calendar.SECOND) + fileName;
      }
      Long id = null;
      if (!StringUtils.isStringNullOrEmpty(srId)) {
        if (StringUtils.isLong(srId)) {
          id = Long.valueOf(srId);
        } else {
          resultDTO.setMessage("attempt to create saveOrUpdate event with null entity");
          return resultDTO;
        }
      }
      SRFilesDTO srFileDto = new SRFilesDTO();
      srFileDto.setComments(result);//OK hoac NOK
      srFileDto.setFileGroup(SR_CONFIG.FILE_GROUP_SR);
      srFileDto.setFileType(SR_CONFIG.FILE_TYPE_OTHER);
      srFileDto.setFilePath(fullPathOld);
      srFileDto.setFileName(FileUtils.getFileName(fullPathOld));
      srFileDto.setFileContent(fileContentError);
      srFileDto.setObejctId(id);
      ResultInSideDto resultFileDataOld = srRepository.addSRFile(srFileDto);
      if (Constants.WS_RESULT.SUCCESS.equals(resultFileDataOld.getKey())) {
        resultDTO.setMessage(Constants.WS_RESULT.OK);

        try {
          UsersEntity usersEntity = userRepository.getUserByUserName("system");
          UnitDTO unitToken = unitRepository.findUnitById(usersEntity.getUnitId());
          List<GnocFileDto> gnocFileDtos = new ArrayList<>();
          GnocFileDto gnocFileDto = new GnocFileDto();
          gnocFileDto.setPath(fullPath);
          gnocFileDto.setFileName(fileName);
          gnocFileDto.setCreateUnitId(usersEntity.getUnitId());
          gnocFileDto.setCreateUserName(usersEntity.getUsername());
          gnocFileDto.setCreateUnitName(unitToken.getUnitName());
          gnocFileDto.setCreateUserId(usersEntity.getUserId());
          gnocFileDto.setCreateTime(new Date());
          gnocFileDto.setContent(fileContentError);
          gnocFileDto.setFileType(SR_CONFIG.FILE_TYPE_OTHER);
          gnocFileDto.setComments(result);
          gnocFileDto.setMappingId(resultFileDataOld.getId());
          gnocFileDtos.add(gnocFileDto);
          gnocFileRepository
              .saveListGnocFileNotDeleteAll(GNOC_FILE_BUSSINESS.SR, Long.valueOf(srId),
                  gnocFileDtos);
        } catch (Exception e) {
          log.error(e.getMessage());
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      resultDTO.setMessage(e.getMessage());
      return resultDTO;
    }
    return resultDTO;
  }

  @Override
  public ResultDTO createSRFromOtherSys(SRCreatedFromOtherSysDTO srCreatedFromOtherSysDTO) {
    ResultDTO res = new ResultDTO();
    try {
      if (srCreatedFromOtherSysDTO != null) {
        //<editor-fold desc="validate" defaultstate="collapsed">
        if (StringUtils.isStringNullOrEmpty(srCreatedFromOtherSysDTO.getSystem())) {
          res.setMessage(I18n.getLanguage("sr.error.systemIsNotNull"));
          res.setKey(Constants.RESULT.FAIL);
          return res;
        } else if (!"1".equals(srCreatedFromOtherSysDTO.getSystem())
            && !"2".equals(srCreatedFromOtherSysDTO.getSystem())) {
          res.setMessage(I18n.getLanguage("sr.error.incorrectSystem"));
          res.setKey(Constants.RESULT.FAIL);
          return res;
        }
        if (StringUtils.isStringNullOrEmpty(srCreatedFromOtherSysDTO.getSubOrderId())) {
          res.setMessage(I18n.getLanguage("sr.error.subOrderIdIsNotNull"));
          res.setKey(Constants.RESULT.FAIL);
          return res;
        } else if (!StringUtils.isLong(srCreatedFromOtherSysDTO.getSubOrderId()) || (
            StringUtils.isLong(srCreatedFromOtherSysDTO.getSubOrderId())
                && Long.valueOf(srCreatedFromOtherSysDTO.getSubOrderId()) <= 0)) {
          res.setMessage(I18n.getLanguage("sr.error.subOrderIdMustBeNumber"));
          res.setKey(Constants.RESULT.FAIL);
          return res;
        }

        if (StringUtils.isStringNullOrEmpty(srCreatedFromOtherSysDTO.getServiceName())) {
          res.setMessage(I18n.getLanguage("sr.error.serviceNameIsNotNull"));
          res.setKey(Constants.RESULT.FAIL);
          return res;
        } else if (!I18n.getLanguage("sr.error.khaiBao")
            .equalsIgnoreCase(srCreatedFromOtherSysDTO.getServiceName())
            && !I18n.getLanguage("sr.error.huy")
            .equalsIgnoreCase(srCreatedFromOtherSysDTO.getServiceName())
            && !I18n.getLanguage("sr.error.chan")
            .equalsIgnoreCase(srCreatedFromOtherSysDTO.getServiceName())
            && !I18n.getLanguage("sr.error.thayDoi")
            .equalsIgnoreCase(srCreatedFromOtherSysDTO.getServiceName())) {
          res.setMessage(I18n.getLanguage("sr.error.incorrectServiceName"));
          res.setKey(Constants.RESULT.FAIL);
          return res;
        }
        if (StringUtils.isStringNullOrEmpty(srCreatedFromOtherSysDTO.getContent())) {
          res.setMessage(I18n.getLanguage("sr.error.contentIsNotNull"));
          res.setKey(Constants.RESULT.FAIL);
          return res;
        } else if (srCreatedFromOtherSysDTO.getContent().length() > 1000) {
          res.setMessage(I18n.getLanguage("sr.error.contentMaxLength"));
          res.setKey(Constants.RESULT.FAIL);
          return res;
        }
        if (!StringUtils.isStringNullOrEmpty(srCreatedFromOtherSysDTO.getCustomer())
            && 125 < srCreatedFromOtherSysDTO.getCustomer().length()) {
          res.setMessage(I18n.getLanguage("sr.error.customerMaxLength"));
          res.setKey(Constants.RESULT.FAIL);
          return res;
        }
        if (!StringUtils.isStringNullOrEmpty(srCreatedFromOtherSysDTO.getAddress())
            && 500 < srCreatedFromOtherSysDTO.getAddress().length()) {
          res.setMessage(I18n.getLanguage("sr.error.addressMaxLength"));
          res.setKey(Constants.RESULT.FAIL);
          return res;
        }
        if (!StringUtils.isStringNullOrEmpty(srCreatedFromOtherSysDTO.getAccountSipTrunk())
            && 125 < srCreatedFromOtherSysDTO.getAccountSipTrunk().length()) {
          res.setMessage(I18n.getLanguage("sr.error.accountMaxLength"));
          res.setKey(Constants.RESULT.FAIL);
          return res;
        }
        if (!StringUtils.isStringNullOrEmpty(srCreatedFromOtherSysDTO.getIpPbx())
            && 125 < srCreatedFromOtherSysDTO.getIpPbx().length()) {
          res.setMessage(I18n.getLanguage("sr.error.ipPBXMaxLength"));
          res.setKey(Constants.RESULT.FAIL);
          return res;
        }
        if (!StringUtils.isStringNullOrEmpty(srCreatedFromOtherSysDTO.getIpProxy())
            && 125 < srCreatedFromOtherSysDTO.getIpProxy().length()) {
          res.setMessage(I18n.getLanguage("sr.error.ipProxyMaxLength"));
          res.setKey(Constants.RESULT.FAIL);
          return res;
        }
        if (!StringUtils.isStringNullOrEmpty(srCreatedFromOtherSysDTO.getSubscribers())
            && 500 < srCreatedFromOtherSysDTO.getSubscribers().length()) {
          res.setMessage(I18n.getLanguage("sr.error.subsMaxLength"));
          res.setKey(Constants.RESULT.FAIL);
          return res;
        }
        //</editor-fold>

        //<editor-fold desc="khoi tao SRDTO" defaultstate="collapsed">
        //Trang thai, quoc gia, tieu de, ngay tao, ngay gui SR
        SRDTO srDTO = new SRDTO();
        srDTO.setStatus(Constants.SR_STATUS.NEW);
        srDTO.setCountry("281");//VN
        srDTO.setTitle(srCreatedFromOtherSysDTO.getContent());
        srDTO.setDescription(srCreatedFromOtherSysDTO.getContent());
        srDTO.setSendDate(DateTimeUtils.getSysDateTime());
        srDTO.setCreatedTime(DateTimeUtils.getSysDateTime());
        srDTO.setUpdatedTime(DateTimeUtils.getSysDateTime());
        srDTO.setCreatedUser("system");

        //ma SR
        List<String> lstSrId = srRepository.getListSequenseSR("OPEN_PM.SR_SEQ", 1);
        String srCode =
            "SR_VN_" + DateTimeUtils.convertDateToString(new Date(), "yyyyMMdd") + "_" + lstSrId
                .get(0);
        srDTO.setSrCode(srCode);
        srDTO.setSrId(lstSrId.get(0));

        //Mang, nhom, ten, don vi xu ly, thoi gian xu ly SR
        String configGroup = "";
        if (I18n.getLanguage("sr.error.khaiBao")
            .equalsIgnoreCase(srCreatedFromOtherSysDTO.getServiceName())) {
          configGroup = Constants.SR_SERVICE_OTHER_SYS.DICH_VU_SIP_TRUNK_KHAI_BAO;
          srCreatedFromOtherSysDTO.setServiceName(I18n.getLanguage("sr.error.khaiBao"));
        } else if (I18n.getLanguage("sr.error.huy")
            .equalsIgnoreCase(srCreatedFromOtherSysDTO.getServiceName())) {
          configGroup = Constants.SR_SERVICE_OTHER_SYS.DICH_VU_SIP_TRUNK_HUY;
          srCreatedFromOtherSysDTO.setServiceName(I18n.getLanguage("sr.error.huy"));
        } else if (I18n.getLanguage("sr.error.chan")
            .equalsIgnoreCase(srCreatedFromOtherSysDTO.getServiceName())) {
          configGroup = Constants.SR_SERVICE_OTHER_SYS.DICH_VU_SIP_TRUNK_CHAN;
          srCreatedFromOtherSysDTO.setServiceName(I18n.getLanguage("sr.error.chan"));
        } else if (I18n.getLanguage("sr.error.thayDoi")
            .equalsIgnoreCase(srCreatedFromOtherSysDTO.getServiceName())) {
          configGroup = Constants.SR_SERVICE_OTHER_SYS.DICH_VU_SIP_TRUNK_THAY_DOI;
          srCreatedFromOtherSysDTO.setServiceName(I18n.getLanguage("sr.error.thayDoi"));
        }
        List<SRConfigDTO> lstConfig = srConfigRepository.getByConfigGroup(configGroup);
        if (lstConfig == null || lstConfig.isEmpty()) {
          res.setMessage(I18n.getLanguage("sr.error.serviceNotConfig"));
          res.setKey(Constants.RESULT.FAIL);
          return res;
        }
        List<SRCatalogDTO> lstCatalog = srCatalogRepository2
            .getListSRCatalogDTO(new SRCatalogDTO(lstConfig.get(0).getConfigCode()));
        if (lstCatalog == null || lstCatalog.isEmpty()) {
          res.setMessage(I18n.getLanguage("sr.error.serviceNotConfig"));
          res.setKey(Constants.RESULT.FAIL);
          return res;
        }
        srDTO.setServiceArray(lstCatalog.get(0).getServiceArray());
        srDTO.setServiceGroup(lstCatalog.get(0).getServiceGroup());
        srDTO.setServiceId(lstCatalog.get(0).getServiceId().toString());
        srDTO.setSrUnit(lstCatalog.get(0).getExecutionUnit());
        srDTO.setExecutionTime(lstCatalog.get(0).getExecutionTime());

        //Ngay gio bat dau, ket thuc
        SrInsiteDTO srInsiteDto = srDTO.toInsideDTO();
        Long isAddDay = StringUtils.isLongNullOrEmpty(lstCatalog.get(0).getIsAddDay()) ? 0L
            : lstCatalog.get(0).getIsAddDay();
        srBusiness.setStartTimeAndEndTimeSR(srInsiteDto, srDTO.getCountry(),
            lstCatalog.get(0).getExecutionTime(), isAddDay.toString());
        //</editor-fold>

        //Insert SR, gui tin nhan den don vi xu ly
        ResultInSideDto insertSR = srBusiness.insertSR(srInsiteDto);
        if (Constants.RESULT.SUCCESS.equals(insertSR.getKey())) {
          res.setKey(Constants.RESULT.SUCCESS);
          UsersEntity usersEntity = userRepository.getUserByUserName(srInsiteDto.getCreatedUser());
          UnitDTO unitToken = unitRepository.findUnitById(usersEntity.getUnitId());
          List<GnocFileDto> gnocFileDtos = new ArrayList<>();
          //<editor-fold desc="Dinh kem file" defaultstate="collapsed">
          ExcelWriterUtils ewu = new ExcelWriterUtils();
          Date date = new Date();
          Calendar calendar = Calendar.getInstance();
          calendar.setTime(date);
          String fileName = "SR_"
              + calendar.get(Calendar.YEAR) + ""
              + (calendar.get(Calendar.MONTH) + 1) + ""
              + calendar.get(Calendar.DAY_OF_MONTH) + ""
              + "_"
              + calendar.get(Calendar.HOUR_OF_DAY) + ""
              + calendar.get(Calendar.MINUTE) + ""
              + calendar.get(Calendar.SECOND) + ".xls";
          String filePath = tempFolder + File.separator + FileUtils.createPathByDate(date);
          ewu.createWorkBook(filePath + File.separator + fileName);
          Workbook workbook = ewu.readFileExcel(filePath + File.separator + fileName);
          workbook.createSheet("SR");
          Sheet sheet = workbook.getSheetAt(0);
          CellStyle cellSt1 = workbook.createCellStyle();
          cellSt1.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
          cellSt1.setFillPattern(FillPatternType.SOLID_FOREGROUND);
          cellSt1.setBorderBottom(BorderStyle.THIN);
          cellSt1.setBorderLeft(BorderStyle.THIN);
          cellSt1.setBorderRight(BorderStyle.THIN);
          cellSt1.setBorderTop(BorderStyle.THIN);
          ewu.createCell(sheet, 0, 0, I18n.getLanguage("sr.column.title.system"), cellSt1);
          ewu.createCell(sheet, 1, 0, I18n.getLanguage("sr.column.title.subOrderId"), cellSt1);
          ewu.createCell(sheet, 2, 0, I18n.getLanguage("sr.column.title.serviceName"), cellSt1);
          ewu.createCell(sheet, 3, 0, I18n.getLanguage("sr.column.title.content"), cellSt1);
          ewu.createCell(sheet, 4, 0, I18n.getLanguage("sr.column.title.customer"), cellSt1);
          ewu.createCell(sheet, 5, 0, I18n.getLanguage("sr.column.title.address"), cellSt1);
          ewu.createCell(sheet, 6, 0, I18n.getLanguage("sr.column.title.account"), cellSt1);
          ewu.createCell(sheet, 7, 0, I18n.getLanguage("sr.column.title.ipPBX"), cellSt1);
          ewu.createCell(sheet, 8, 0, I18n.getLanguage("sr.column.title.ipProxy"), cellSt1);
          ewu.createCell(sheet, 9, 0, I18n.getLanguage("sr.column.title.subs"), cellSt1);
          ewu.createCell(sheet, 10, 0, I18n.getLanguage("sr.column.title.calls"), cellSt1);

          CellStyle cellSt = workbook.createCellStyle();
          cellSt.setBorderBottom(BorderStyle.THIN);
          cellSt.setBorderLeft(BorderStyle.THIN);
          cellSt.setBorderRight(BorderStyle.THIN);
          cellSt.setBorderTop(BorderStyle.THIN);
          ewu.createCell(sheet, 0, 1,
              ("1".equals(srCreatedFromOtherSysDTO.getSystem()) ? "QLCTKT" : "CM"), cellSt);
          ewu.createCell(sheet, 1, 1, srCreatedFromOtherSysDTO.getSubOrderId() == null ? null
              : srCreatedFromOtherSysDTO.getSubOrderId().toString(), cellSt);
          ewu.createCell(sheet, 2, 1, srCreatedFromOtherSysDTO.getServiceName(), cellSt);
          ewu.createCell(sheet, 3, 1, srCreatedFromOtherSysDTO.getContent(), cellSt);
          ewu.createCell(sheet, 4, 1, srCreatedFromOtherSysDTO.getCustomer(), cellSt);
          ewu.createCell(sheet, 5, 1, srCreatedFromOtherSysDTO.getAddress(), cellSt);
          ewu.createCell(sheet, 6, 1, srCreatedFromOtherSysDTO.getAccountSipTrunk(), cellSt);
          ewu.createCell(sheet, 7, 1, srCreatedFromOtherSysDTO.getIpPbx(), cellSt);
          ewu.createCell(sheet, 8, 1,
              StringUtils.isStringNullOrEmpty(srCreatedFromOtherSysDTO.getIpProxy()) ? ""
                  : srCreatedFromOtherSysDTO.getIpProxy(), cellSt);
          ewu.createCell(sheet, 9, 1, srCreatedFromOtherSysDTO.getSubscribers(), cellSt);
          ewu.createCell(sheet, 10, 1, srCreatedFromOtherSysDTO.getCalls() == null ? null
              : srCreatedFromOtherSysDTO.getCalls().toString(), cellSt);

          ewu.saveToFileExcel(filePath + File.separator, fileName);

          //Start save file old
          File fileWrite = new File(filePath + File.separator + fileName);
          byte[] bytes = FileUtils.convertFileToByte(fileWrite);
          String fullPath = FileUtils
              .saveFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
                  PassTranformer.decrypt(ftpPass),
                  ftpFolder + "/" + FileUtils.createPathFtpByDate(date), fileName, bytes);
          SRFilesDTO srFileDto = new SRFilesDTO();
          String comments =
              "Created from : " + ("1".equals(srCreatedFromOtherSysDTO.getSystem()) ? "QLCTKT"
                  : "CM");
          srFileDto.setComments(comments);
          srFileDto.setFileGroup(SR_CONFIG.FILE_GROUP_SR);
          srFileDto.setFileType(SR_CONFIG.FILE_TYPE_OTHER);
          srFileDto.setObejctId(srInsiteDto.getSrId());
          srFileDto.setFilePath(fullPath);
          srFileDto.setFileName(FileUtils.getFileName(fullPath));
          ResultInSideDto resultFileDataOld = srRepository.addSRFile(srFileDto);
          //End save file old
          GnocFileDto gnocFileDto = new GnocFileDto();
          gnocFileDto.setPath(fullPath);
          gnocFileDto.setFileName(fileName);
          gnocFileDto.setCreateUnitId(usersEntity.getUnitId());
          gnocFileDto.setCreateUnitName(unitToken.getUnitName());
          gnocFileDto.setCreateUserId(usersEntity.getUserId());
          gnocFileDto.setCreateUserName(usersEntity.getUsername());
          gnocFileDto.setCreateTime(date);
          gnocFileDto.setFileType(SR_CONFIG.FILE_TYPE_OTHER);
          gnocFileDto.setComments(comments);
          gnocFileDto.setMappingId(resultFileDataOld.getId());
          gnocFileDtos.add(gnocFileDto);
          gnocFileRepository
              .saveListGnocFileNotDeleteAll(GNOC_FILE_BUSSINESS.SR, srInsiteDto.getSrId(),
                  gnocFileDtos);
          //</editor-fold>

          //insert vao bang SR_CREATED_FROM_OTHER_SYS
          srCreatedFromOtherSysDTO.setSrCode(srDTO.getSrCode());
          ResultInSideDto resOtherSys = srCreatedFromOtherSysRepository
              .insertSRCreateFromOtherSys(srCreatedFromOtherSysDTO);
          if (Constants.RESULT.FAIL.equals(resOtherSys.getKey())) {
            res.setMessage(
                I18n.getLanguage("sr.error.createDataIn") + " SR_CREATED_FROM_OTHER_SYS");
            res.setKey(Constants.RESULT.FAIL);
            return res;
          }
        } else {
          res.setMessage(I18n.getLanguage("sr.error.createDataIn") + " SR");
          res.setKey(Constants.RESULT.FAIL);
          return res;
        }
      } else {
        res.setMessage(I18n.getLanguage("sr.error.dataIsNull"));
        res.setKey(Constants.RESULT.FAIL);
        return res;
      }
      return res;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      res.setMessage(e.getMessage());
      res.setKey(Constants.RESULT.FAIL);
      return res;
    }
  }

  @Override
  public ResultDTO createSRByConfigGroup(SRDTO srInputDTO, String configGroup) {
    ResultDTO res = new ResultDTO();
    try {
      Long serviceId = null;
      String serviceArray = "";
      String serviceGroup = "";
      String executionUnit = "";
      String executionTime = "";
      String locationId = "";
      String locationCode = "";
      Long isAddDay = null;
      if (StringUtils.isStringNullOrEmpty(configGroup)) {
        res.setKey("0");
        res.setMessage(I18n.getLanguage("sr.error.configGroupIsNotNull"));
        return res;
      }
      if (StringUtils.isStringNullOrEmpty(srInputDTO.getServiceCode())) {
        res.setKey("0");
        res.setMessage(I18n.getLanguage("sr.error.serviceCodeIsNotNull"));
        return res;
      } else if (!StringUtils.isStringNullOrEmpty(srInputDTO.getServiceCode())) {
        List<SRCatalogDTO> lstCatalog = getListSRCatalogByConfigGroup(configGroup);
        if (lstCatalog == null || lstCatalog.isEmpty()) {
          res.setKey("0");
          res.setMessage(I18n.getLanguage("sr.error.serviceNotConfig"));
          return res;
        } else {
          boolean error = true;
          for (SRCatalogDTO item : lstCatalog) {
            if (item.getServiceCode() != null && item.getServiceCode()
                .equals(srInputDTO.getServiceCode())) {
              error = false;
              break;
            }
          }
          if (error) {
            res.setKey("0");
            res.setMessage(I18n.getLanguage("sr.error.serviceCodeIncorrect"));
            return res;
          }
          lstCatalog = srCatalogRepository2
              .getListSRCatalogDTO(new SRCatalogDTO(srInputDTO.getServiceCode()));
          if (Constants.SR_CONFIG.DICH_VU_VSMART.equals(configGroup)) {
            if (lstCatalog != null && !lstCatalog.isEmpty()) {
              if (StringUtils.isStringNullOrEmpty(srInputDTO.getExecutionUnit())) {
                res.setKey("0");
//                res.setMessage(I18n.getLanguage("sr.error.executionUnitIsNotNull"));
                res.setMessage(I18n.getLanguage("sr.error.srUnitNotNull"));
                return res;
              } else {
                boolean isContainsUnit = false;
                for (SRCatalogDTO unit : lstCatalog) {
                  if (unit.getExecutionUnit().equals(srInputDTO.getExecutionUnit()) && unit
                      .getServiceCode().equalsIgnoreCase(srInputDTO.getServiceCode())) {
                    isContainsUnit = true;
                    executionUnit = srInputDTO.getExecutionUnit();
                    serviceId = unit.getServiceId();
                    serviceArray = unit.getServiceArray();
                    serviceGroup = unit.getServiceGroup();
                    executionTime = unit.getExecutionTime();
                    locationId = unit.getCountry();
                    isAddDay = unit.getIsAddDay();
                    break;
                  }
                }
                if (!isContainsUnit) {
                  res.setKey("0");
                  res.setMessage(I18n.getLanguage("sr.error.executionUnitInvalidSrCatalog"));
                  return res;
                }
              }
              srInputDTO.setSrUnit(srInputDTO.getExecutionUnit());
            }
          } else {
            if (StringUtils.isStringNullOrEmpty(srInputDTO.getSrUnit())) {
              res.setKey("0");
              res.setMessage(I18n.getLanguage("sr.error.srUnitNotNull"));
              return res;
            } else {
              UnitDTO dtoU = new UnitDTO();
              dtoU.setUnitId(Long.valueOf(srInputDTO.getSrUnit()));
              List<UnitDTO> lstUnit = unitRepository.getListUnitDTO(dtoU, 0, 0, "", "");
              if (lstUnit == null || lstUnit.isEmpty()) {
                res.setKey("0");
                res.setMessage(I18n.getLanguage("sr.error.srUnitNotFound"));
                return res;
              }

              boolean check = false;
              for (SRCatalogDTO s : lstCatalog) {
                if (s.getExecutionUnit().equals(srInputDTO.getSrUnit()) && s.getServiceCode()
                    .equalsIgnoreCase(srInputDTO.getServiceCode())) {
                  check = true;
                  serviceId = s.getServiceId();
                  serviceArray = s.getServiceArray();
                  serviceGroup = s.getServiceGroup();
                  executionUnit = s.getExecutionUnit();
                  executionTime = s.getExecutionTime();
                  locationId = s.getCountry();
                  isAddDay = s.getIsAddDay();
                  break;
                }
              }
              if (!check) {
                res.setKey("0");
                res.setMessage(I18n.getLanguage("sr.error.srUnitIncorrect"));
                return res;
              }
            }
          }

          if (StringUtils.isStringNullOrEmpty(srInputDTO.getRoleCode())) {
            res.setKey("0");
            res.setMessage(I18n.getLanguage("sr.error.roleCodeIsNotNull"));
            return res;
          } else {
            if (!Constants.SR_CONFIG.DICH_VU_VSMART.equals(configGroup)) {
              SRRoleDTO dtoRole = new SRRoleDTO();
              dtoRole.setRoleCode(srInputDTO.getRoleCode());
              List<SRRoleDTO> lstRole = srCategoryServiceProxy.getListSRRoleDTO(dtoRole);
              if (lstRole == null || lstRole.isEmpty()) {
                res.setKey("0");
                res.setMessage(I18n.getLanguage("sr.error.roleCodeInvalid2"));
                return res;
              }
            }

            boolean isContainsRoleCode = false;
            for (SRCatalogDTO unit : lstCatalog) {
              if (!StringUtils.isStringNullOrEmpty(unit.getRoleCode()) && unit.getServiceCode()
                  .equalsIgnoreCase(srInputDTO.getServiceCode()) && unit.getExecutionUnit()
                  .equals(srInputDTO.getSrUnit())) {
                String[] arrRole = unit.getRoleCode().split(",");
                for (String r : arrRole) {
                  if (r.equals(srInputDTO.getRoleCode())) {
                    isContainsRoleCode = true;
                    break;
                  }
                }
              }
            }
            if (!isContainsRoleCode) {
              res.setKey("0");
              res.setMessage(I18n.getLanguage("sr.error.roleCodeInvalid"));
              return res;
            }
          }
        }
      }

      if (!Constants.SR_CONFIG.DICH_VU_VSMART.equals(configGroup)) {
        if (!StringUtils.isStringNullOrEmpty(srInputDTO.getCountry())) {
          if (!locationId.equals(srInputDTO.getCountry())) {
            res.setKey("0");
            res.setMessage(I18n.getLanguage("sr.error.cantFindListCountry4"));
            return res;
          }
        }
      }
      CatLocationDTO catLocation = catLocationRepository
          .getNationByLocationId(Long.valueOf(locationId));
      if (catLocation == null) {
        res.setKey("0");
        res.setMessage(I18n.getLanguage("sr.error.cantFindListCountry5"));
        return res;
      } else {
        locationCode = catLocation.getLocationCode();
      }

      if (StringUtils.isStringNullOrEmpty(srInputDTO.getTitle())) {
        res.setKey("0");
        res.setMessage(I18n.getLanguage("sr.error.titleIsNotNull"));
        return res;
      } else if (srInputDTO.getTitle().length() > 1000) {
        res.setKey("0");
        res.setMessage(I18n.getLanguage("sr.error.titleMaxLength"));
        return res;
      }
      if (StringUtils.isStringNullOrEmpty(srInputDTO.getDescription())) {
        res.setKey("0");
        res.setMessage(I18n.getLanguage("sr.error.desIsNotNull"));
        return res;
      } else if (srInputDTO.getDescription().length() > 1000) {
        res.setKey("0");
        res.setMessage(I18n.getLanguage("sr.error.descMaxLength"));
        return res;
      }
      if (StringUtils.isStringNullOrEmpty(srInputDTO.getCreatedUser())) {
        res.setKey("0");
        res.setMessage(I18n.getLanguage("sr.error.createdUserIsNotNull"));
        return res;
      } else {
        UsersDTO user = checkUserByUserCodeOrName(srInputDTO.getCreatedUser(), configGroup);
        if (user == null) {
          res.setKey("0");
          res.setMessage(I18n.getLanguage("sr.error.createdUserIncorrect"));
          return res;
        }
        //fix bug
        if (Constants.SR_CONFIG.DICH_VU_ADD_ON.equals(configGroup)) {
          srInputDTO.setCreatedUser(user.getUsername());
        }
      }

      if (SR_CONFIG.DICH_VU_WO_HELP.equals(configGroup)) {
        if (StringUtils.isStringNullOrEmpty(srInputDTO.getWoCode())) {
          res.setKey("0");
          res.setMessage(I18n.getLanguage("sr.error.woIdNotNull"));
          return res;
        } else if (!StringUtils.isLong(srInputDTO.getWoCode())) {
          res.setKey("0");
          res.setMessage(I18n.getLanguage("sr.error.woIdInvalid"));
          return res;
        }
      }

      //neu la dich vu goi tu NOC thi input bat buoc them trang thai
      if (Constants.SR_CONFIG.DICH_VU_NOC.equals(configGroup)) {
        if (StringUtils.isStringNullOrEmpty(srInputDTO.getStatus())) {
          res.setKey("0");
          res.setMessage(I18n.getLanguage("sr.error.statusNotNull"));
          return res;
        } else if (!Constants.SR_STATUS.NEW.equals(srInputDTO.getStatus())
            && !Constants.SR_STATUS.DRAFT.equals(srInputDTO.getStatus())) {
          res.setKey("0");
          res.setMessage(I18n.getLanguage("sr.error.statusIncorrect"));
          return res;
        }
      }

      SRDTO srDTO = new SRDTO();
      srDTO.setCountry(locationId);
      String status = (Constants.SR_CONFIG.DICH_VU_WO_HELP.equals(configGroup)
          || Constants.SR_CONFIG.DICH_VU_VSMART.equals(configGroup))
          ? Constants.SR_STATUS.NEW
          : (StringUtils.isStringNullOrEmpty(srInputDTO.getStatus()) ? Constants.SR_STATUS.NEW
              : srInputDTO.getStatus());
      srDTO.setStatus(status);
      srDTO.setServiceArray(serviceArray);
      srDTO.setServiceGroup(serviceGroup);
      srDTO.setServiceId(serviceId.toString());
      srDTO.setSrUnit(executionUnit);
      srDTO.setTitle(srInputDTO.getTitle());
      srDTO.setDescription(srInputDTO.getDescription());
      srDTO.setCreatedUser(srInputDTO.getCreatedUser());
      srDTO.setSendDate(DateTimeUtils.getSysDateTime());
      srDTO.setCreatedTime(DateTimeUtils.getSysDateTime());
      srDTO.setUpdatedTime(DateTimeUtils.getSysDateTime());
      List<String> lstSrId = srRepository.getListSequenseSR("OPEN_PM.SR_SEQ", 1);
      String srCode =
          "SR_" + locationCode + "_" + DateTimeUtils.convertDateToString(new Date(), "yyyyMMdd")
              + "_" + lstSrId.get(0);
      srDTO.setSrCode(srCode);
      srDTO.setSrId(lstSrId.get(0));
      srDTO.setRoleCode(srInputDTO.getRoleCode());
      srDTO.setInsertSource(
          srInputDTO.getInsertSource() == null ? "" : srInputDTO.getInsertSource());

      SrInsiteDTO dtoTmp = srDTO.toInsideDTO();

      dtoTmp = srBusiness.setStartTimeAndEndTimeSR(dtoTmp, srDTO.getCountry(), executionTime,
          isAddDay == null ? "" : isAddDay.toString());

      ResultInSideDto insertSR = srBusiness.insertSR(dtoTmp);
      if (Constants.RESULT.SUCCESS.equals(insertSR.getKey())) {
        //20201210 dungpv edit cho VSMART them moi file
        UsersEntity usersEntity = userRepository.getUserByUserName(dtoTmp.getCreatedUser());
        UnitDTO unitToken = unitRepository.findUnitById(usersEntity.getUnitId());
        //<editor-fold desc="dinh kem danh sach file">
        if (srInputDTO.getLstFile() != null && !srInputDTO.getLstFile().isEmpty()) {
          try {
            for (SRFilesDTO fileDto : srInputDTO.getLstFile()) {
              if (!StringUtils.isStringNullOrEmpty(fileDto.getFileContent())) {
                if (StringUtils.isStringNullOrEmpty(fileDto.getFileName())) {
                  res.setKey("0");
                  res.setMessage(I18n.getLanguage("sr.error.fileName.notnull"));
                  return res;
                }
                String fullPath = FileUtils
                    .saveFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
                        PassTranformer.decrypt(ftpPass), ftpFolder, fileDto.getFileName(),
                        Base64.decode(fileDto.getFileContent()), null);
                String fullPathOld = FileUtils.saveUploadFile(fileDto.getFileName(),
                    Base64.decode(fileDto.getFileContent()), uploadFolder, null);

                //Start save file old
                SRFilesDTO srFileDto = new SRFilesDTO();
                srFileDto.setFileGroup(Constants.SR_CONFIG.FILE_GROUP_SR);
                srFileDto.setFileType(Constants.SR_CONFIG.FILE_TYPE_OTHER);
                srFileDto.setObejctId(Long.valueOf(srDTO.getSrId()));
                srFileDto.setFileName(FileUtils.getFileName(fullPathOld));
                srFileDto.setFilePath(fullPathOld);
                srFileDto.setFileContent(fileDto.getFileContent());
                ResultInSideDto resultFileDataOld = srRepository.addSRFile(srFileDto);
                //End save file old
                if (resultFileDataOld != null && RESULT.SUCCESS
                    .equals(resultFileDataOld.getKey())) {
                  List<GnocFileDto> gnocFileDtos = new ArrayList<>();
                  GnocFileDto gnocFileDto = new GnocFileDto();
                  gnocFileDto.setPath(fullPath);
                  gnocFileDto.setFileName(fileDto.getFileName());
                  gnocFileDto.setCreateUnitId(usersEntity.getUnitId());
                  gnocFileDto.setCreateUnitName(unitToken.getUnitName());
                  gnocFileDto.setCreateUserId(usersEntity.getUserId());
                  gnocFileDto.setCreateUserName(usersEntity.getUsername());
                  gnocFileDto.setCreateTime(new Date());
                  gnocFileDto.setFileType(SR_CONFIG.FILE_TYPE_OTHER);
                  gnocFileDto.setContent(fileDto.getFileContent());
                  gnocFileDto.setMappingId(resultFileDataOld.getId());
                  gnocFileDtos.add(gnocFileDto);
                  gnocFileRepository.saveListGnocFileNotDeleteAll(GNOC_FILE_BUSSINESS.SR,
                      Long.valueOf(srDTO.getSrId()), gnocFileDtos);
                }
              }
            }
          } catch (Exception e) {
            log.error(e.getMessage(), e);
            res.setMessage(e.getMessage());
            res.setKey("0");
            return res;
          }
        }
        //</editor-fold>
        //end
      } else {
        res.setKey("0");
        res.setMessage(I18n.getLanguage("sr.error.createDataIn") + " SR");
      }
      SrInsiteDTO srInsiteDTO = srRepository.getDetailNoOffset(dtoTmp.getSrId());
      if (srInsiteDTO != null) {
        List<SRDTO> lstResult = new ArrayList<>();
        lstResult.add(srInsiteDTO.toOutsideDTO());
        res.setLstResult(lstResult);
      }
      res.setMessage(Constants.RESULT.SUCCESS);
      res.setKey(srCode);
      return res;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      res.setKey("0");
      res.setMessage(e.getMessage());
      return res;
    }

  }

  @Override
  public UsersDTO checkUserByUserCodeOrName(String userCode, String configGroup) {
    UsersDTO userFinal = new UsersDTO();
    if (!StringUtils.isStringNullOrEmpty(userCode)) {
      if (Constants.SR_CONFIG.DICH_VU_ADD_ON.equals(configGroup)) {
        UsersInsideDto user = userRepository.getUserByStaffCode(userCode);
        if (user != null && !StringUtils.isLongNullOrEmpty(user.getUserId()) && !StringUtils
            .isStringNullOrEmpty(user.getUsername()) && "1"
            .equals(String.valueOf(user.getIsEnable()))) {
          userFinal = user.toOutSideDto();
          return userFinal;
        }
      } else {
        UsersEntity user = userRepository.getUserByUserName(userCode);
        if (user != null && !StringUtils.isStringNullOrEmpty(user.getUnitId())
            && "1".equals(String.valueOf(user.getIsEnable()))) {
          userFinal.setUsername(user.getUsername());
          userFinal.setUserId(user.getUserId().toString());
          userFinal.setUnitId(user.getUnitId().toString());
          return userFinal;
        }
      }
    }
    return null;
  }

  @Override
  public List<SRDTO> getListSRByConfigGroup(SRDTO dto, String configGroup) {
    List<SRDTO> lstSr = new ArrayList<>();
    if (!StringUtils.isStringNullOrEmpty(configGroup)) {
      if (!StringUtils.isStringNullOrEmpty(dto.getCreatedUser())) {
        UsersDTO user = checkUserByUserCodeOrName(dto.getCreatedUser(), configGroup);
        if (user == null) {
          SRDTO error = new SRDTO("0", I18n.getLanguage("sr.error.createdUserIncorrect"));
          error.setDefaultSortField("name");
          lstSr.add(error);
          return lstSr;
        } else {
          dto.setCreatedUser(user.getUsername());
        }
      }
      lstSr = srOutsideRepository.getListSRByConfigGroup(dto, configGroup);
    } else {
      SRDTO error = new SRDTO("0", I18n.getLanguage("sr.error.createdUserIncorrect"));
      error.setDefaultSortField("name");
      lstSr.add(error);
    }
    return lstSr;
  }

  @Override
  public List<SRCatalogDTO> getListSRCatalogByConfigGroup(String configGroup) {
    List<SRCatalogDTO> lstSrCatalog = new ArrayList<>();
    if (!StringUtils.isStringNullOrEmpty(configGroup)) {
      return srOutsideRepository.getListSRCatalogByConfigGroup(configGroup);
    }
    return lstSrCatalog;
  }

  @Override
  public List<SRDTO> getListSRForLinkCR(String loginUser, String srCode) {
    List<SRDTO> lst = new ArrayList<>();
    if (StringUtils.isStringNullOrEmpty(loginUser)) {
      lst.add(new SRDTO("0", I18n.getLanguage("sr.error.loginUserNotNull")));
      return lst;
    } else {
      UsersDTO u = checkUserByUserCodeOrName(loginUser, "");
      if (StringUtils.isStringNullOrEmpty(u)) {
        lst.add(new SRDTO("0", I18n.getLanguage("sr.error.loginUserIncorrect")));
        return lst;
      }
    }
    if (!StringUtils.isStringNullOrEmpty(srCode)) {
      String id = srCode.substring(srCode.lastIndexOf("_") + 1);
      try {
        Long srId = Long.valueOf(id);
        SrInsiteDTO srInsiteDTO = srRepository.getDetailNoOffset(srId);
        if (srInsiteDTO == null) {
          lst.add(new SRDTO("0", I18n.getLanguage("sr.error.cantFindSR")));
          return lst;
        }
      } catch (Exception e) {
        lst.add(new SRDTO("0", I18n.getLanguage("sr.error.cantFindSR")));
        return lst;
      }
    }
    lst = srRepository.getListSRForLinkCR(loginUser, srCode);
    if (lst == null || lst.isEmpty()) {
      lst = new ArrayList<>();
      lst.add(new SRDTO("0", I18n.getLanguage("sr.error.listDataNull").replaceAll("list", "SR")));
      return lst;
    }
    return lst;
  }

  @Override
  public List<SRDTO> getListSR(SRDTO dto, int rowStart, int maxRow) {
    SrInsiteDTO srInsiteDTO = dto.toInsideDTO();
    srInsiteDTO.setPage(rowStart);
    srInsiteDTO.setPageSize(maxRow);
    List<SRDTO> lst = srRepository.getListSRForOutside(srInsiteDTO);
    if (lst == null) {
      lst = new ArrayList<>();
    }
    String pattern = "dd/MM/yyyy HH:mm";
    for (SRDTO item : lst) {
      try {
        item.setStartTime(DateUtil
            .dateToStringWithPattern(DateUtil.stringYYYYmmDDhhMMssToDate(item.getStartTime()),
                pattern));
        item.setEndTime(DateUtil
            .dateToStringWithPattern(DateUtil.stringYYYYmmDDhhMMssToDate(item.getEndTime()),
                pattern));
        item.setCreatedTime(DateUtil
            .dateToStringWithPattern(DateUtil.stringYYYYmmDDhhMMssToDate(item.getCreatedTime()),
                pattern));
        item.setUpdatedTime(DateUtil
            .dateToStringWithPattern(DateUtil.stringYYYYmmDDhhMMssToDate(item.getUpdatedTime()),
                pattern));
        item.setSendDate(item.getSendDate() == null ? null : DateUtil
            .dateToStringWithPattern(DateUtil.stringYYYYmmDDhhMMssToDate(item.getSendDate()),
                pattern));
      } catch (Exception e) {
        log.error(e.getMessage());
      }
    }
    return lst;
  }

  @Override
  public String deleteSRForOutside(Long srId) {
    ResultInSideDto resultInSideDto = srRepository.deleteSR(srId);
    String keyResult = resultInSideDto.getKey();
    return keyResult;
  }

  @Override
  public List<SRDTO> getCrNumberCreatedFromSR(SRDTO dto, int rowStart, int maxRow) {
    List<SRDTO> lstResult = new ArrayList<>();
    SrInsiteDTO srSearch = new SrInsiteDTO();
    try {
      srSearch.setCreateFromDate(
          dto.getCreateFromDate() != null ? DateUtil.string2Date(dto.getCreateFromDate()) : null);
      srSearch.setCreateToDate(
          dto.getCreateToDate() != null ? DateUtil.string2Date(dto.getCreateToDate()) : null);
      srSearch.setSrId(
          StringUtils.isStringNullOrEmpty(dto.getSrId()) ? null : Long.parseLong(dto.getSrId()));
      List<SrInsiteDTO> lst = srRepository
          .getCrNumberCreatedFromSR(srSearch, rowStart, maxRow, true);
      if (lst != null && !lst.isEmpty()) {
        for (SrInsiteDTO srInsiteDTO : lst) {
          SRDTO srdto = new SRDTO();
          srdto.setSrId(srInsiteDTO.getSrId() != null ? srInsiteDTO.getSrId().toString() : null);
          srdto.setCrNumber(srInsiteDTO.getCrNumber());
          srdto.setOffset(srInsiteDTO.getOffset());
          lstResult.add(srdto);
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return lstResult;
  }

  @Override
  public SRDTO getDetailSR(String srId, Long userId) {
    log.info("Request to getDetailSR : {}", srId, userId);
    if (StringUtils.isNotNullOrEmpty(srId)) {
      SrInsiteDTO srInsiteDTO = srRepository.getDetailNoOffset(Long.parseLong(srId));
      if (srInsiteDTO != null) {
        Double offset = 0.0D;
        if (userId != null && userId > 0) {
          offset = userRepository.getOffsetFromUser(userId);
        }
        return convertSRDate2VietNamDate(srInsiteDTO, -offset).toOutsideDTO();
      }
    }
    return null;
  }

  @Override
  public SrInsiteDTO getDetailSRById(String srId) {
    log.info("Request to getDetailSRById : {}", srId);
    if (StringUtils.isNotNullOrEmpty(srId)) {
      SrInsiteDTO srInsiteDTO = srRepository.getDetailNoOffset(Long.parseLong(srId));
      if (srInsiteDTO != null) {
        srInsiteDTO.setSrHisDTOList(srHisRepository.getListSRHisDTOBySrId(srInsiteDTO.getSrId()));
        GnocFileDto gnocFileDto = new GnocFileDto();
        gnocFileDto.setBusinessCode(GNOC_FILE_BUSSINESS.SR);
        gnocFileDto.setBusinessId(srInsiteDTO.getSrId());
        List<GnocFileDto> lstSRFiles = gnocFileRepository.getListGnocFileForSR(gnocFileDto);
        if (lstSRFiles != null && !lstSRFiles.isEmpty()) {
          for (GnocFileDto fileDto : lstSRFiles) {
            if (StringUtils.isNotNullOrEmpty(fileDto.getPath())) {
              try {
                byte[] bFile = FileUtils
                    .getFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
                        PassTranformer.decrypt(ftpPass), fileDto.getPath());
                fileDto.setContent(org.apache.xml.security.utils.Base64.encode(bFile));
              } catch (Exception ex) {
                log.error(ex.getMessage(), ex);
              }
            }
          }
          srInsiteDTO.setGnocFileDtos(lstSRFiles);
        }
      }
      return srInsiteDTO;
    }
    return null;
  }

  @Override
  public ResultDTO updateSR(SRDTO srDTO) {
    log.info("Request to updateSR : {}", srDTO);
    ResultDTO resultDTO = new ResultDTO();
    resultDTO.setKey(RESULT.FAIL);
    ResultInSideDto resultInSideDto = srBusiness.updateSR(srDTO.toInsideDTO());
    if (resultInSideDto != null && StringUtils.isNotNullOrEmpty(resultInSideDto.getKey())) {
      resultDTO.setKey(resultInSideDto.getKey());
      resultDTO.setMessage(resultInSideDto.getMessage());
    }
    return resultDTO;
  }

  @Override
  public ResultDTO insertSRWorklog(SRWorkLogDTO srWorklogDTO) {
    log.info("Request to insertSRWorklog : {}", srWorklogDTO);
    ResultDTO resultDTO = new ResultDTO(null, RESULT.SUCCESS, RESULT.SUCCESS);
    srWorklogDTO.setCreatedTime(new Date());
    ResultInSideDto resultInSideDto = srWorkLogRepository.insertSRWorklog(srWorklogDTO);
    if (resultInSideDto != null && resultInSideDto.getId() != null) {
      resultDTO.setId(String.valueOf(resultInSideDto.getId()));
    }
    return resultDTO;
  }

  @Override
  public List<GnocFileDto> getListGnocFileForSR(GnocFileDto gnocFileDto) {
    log.info("Request to getListGnocFileForSR : {}", gnocFileDto);
    return gnocFileRepository.getListGnocFileForSR(gnocFileDto);
  }

  @Override
  public ResultDTO updateSRForIBPMSOutSide(SRDTO srDTO) {
    log.info("Request to updateSRForOutSide : {}", srDTO);
    ResultDTO resultDTO = new ResultDTO();
    resultDTO.setKey(RESULT.FAIL);
    SrInsiteDTO srInsiteDTO = srRepository.getDetailNoOffset(Long.parseLong(srDTO.getSrId()));
    if (SR_STATUS.REJECTED.equals(srInsiteDTO.getStatus()) && SR_STATUS.NEW
        .equals(srDTO.getStatus())) {
      if (StringUtils.isStringNullOrEmpty(srDTO.getServiceId()) && StringUtils
          .isStringNullOrEmpty(srDTO.getServiceCode())) {
        resultDTO.setMessage(I18n.getValidation("SrInsiteDTO.null.serviceId"));
      }
      if (StringUtils.isStringNullOrEmpty(srDTO.getSrUnit())) {
        resultDTO.setMessage(I18n.getValidation("SrInsiteDTO.null.srUnit"));
        return resultDTO;
      }
      if (StringUtils.isStringNullOrEmpty(srDTO.getRoleCode())) {
        resultDTO.setMessage(I18n.getValidation("SrInsiteDTO.null.roleCode"));
        return resultDTO;
      }
      SRCatalogDTO srCatalogDTO = null;
      if (StringUtils.isNotNullOrEmpty(srDTO.getServiceId())) {
        srCatalogDTO = srCatalogRepository2
            .findById(Long.parseLong(srDTO.getServiceId()));
      } else {
        List<SRCatalogDTO> lstCatalog = srCatalogRepository2
            .getListSRCatalogDTO(new SRCatalogDTO(srDTO.getServiceCode()));
        for (SRCatalogDTO unit : lstCatalog) {
          if (unit.getExecutionUnit().equals(srDTO.getSrUnit()) && unit
              .getServiceCode().equalsIgnoreCase(srDTO.getServiceCode())) {
            srCatalogDTO = unit;
            break;
          }
        }
      }
      if (srCatalogDTO == null) {
        resultDTO.setMessage(I18n.getValidation("SrInsiteDTO.notExist.serviceId"));
        return resultDTO;
      }
      if (!StringUtils.isLongNullOrEmpty(srCatalogDTO.getServiceId())) {
        srInsiteDTO.setServiceId(String.valueOf(srCatalogDTO.getServiceId()));
      } else {
        resultDTO.setMessage(I18n.getValidation("SrInsiteDTO.null.serviceId"));
      }
      srInsiteDTO.setServiceArray(srCatalogDTO.getServiceArray());
      srInsiteDTO.setServiceGroup(srCatalogDTO.getServiceGroup());
      if (StringUtils.isNotNullOrEmpty(srDTO.getSrUnit()) && srDTO.getSrUnit()
          .equals(srCatalogDTO.getExecutionUnit())) {
        srInsiteDTO.setSrUnit(Long.valueOf(srDTO.getSrUnit()));
      } else {
        resultDTO.setMessage(I18n.getLanguage("sr.error.executionUnitInvalid"));
        return resultDTO;
      }
      SRRoleDTO dtoRole = new SRRoleDTO();
      dtoRole.setRoleCode(srDTO.getRoleCode());
      List<SRRoleDTO> lstRole = srCategoryServiceProxy.getListSRRoleDTO(dtoRole);
      if (lstRole == null || lstRole.isEmpty()) {
        resultDTO.setMessage(I18n.getLanguage("sr.error.roleCodeInvalid2"));
        return resultDTO;
      }
      if (StringUtils.isNotNullOrEmpty(srCatalogDTO.getRoleCode())) {
        String[] arrRole = srCatalogDTO.getRoleCode().split(",");
        boolean isContainsRoleCode = false;
        for (String r : arrRole) {
          if (r.equals(srDTO.getRoleCode())) {
            isContainsRoleCode = true;
            srInsiteDTO.setRoleCode(srDTO.getRoleCode());
            break;
          }
        }
        if (!isContainsRoleCode) {
          resultDTO.setMessage(I18n.getLanguage("sr.error.roleCodeInvalid"));
          return resultDTO;
        }
      }
      if (StringUtils.isNotNullOrEmpty(srDTO.getStartTime())) {
        try {
          srInsiteDTO.setStartTime(DateTimeUtils.convertStringToDateTime(srDTO.getStartTime()));
        } catch (Exception e) {
          log.error(e.getMessage(), e);
        }
      } else {
        resultDTO.setMessage(I18n.getValidation("SrInsiteDTO.null.startTime"));
        return resultDTO;
      }
      if (StringUtils.isNotNullOrEmpty(srDTO.getUpdatedUser())) {
        srInsiteDTO.setUpdatedUser(srDTO.getUpdatedUser());
      }
      srInsiteDTO.setSrUser(null);
      srInsiteDTO.setStatus(SR_STATUS.NEW);
      srInsiteDTO.setInsertSource("IBPMS");
      srInsiteDTO.setUpdatedTime(new Date());
      ResultInSideDto resultInSideDto = srBusiness.updateSR(srInsiteDTO);
      if (resultInSideDto != null && StringUtils.isNotNullOrEmpty(resultInSideDto.getKey())) {
        resultDTO.setKey(resultInSideDto.getKey());
        resultDTO.setMessage(resultInSideDto.getMessage());
      }
    } else {
      resultDTO.setMessage(I18n.getValidation("SrInsiteDTO.permission.status"));
    }
    return resultDTO;
  }

  @Override
  public List<SRConfigDTO> getByConfigGroup(String configGroup) {
    log.info("Request to getByConfigGroup : {}", configGroup);
    return srConfigRepository.getByConfigGroup(configGroup);
  }

  @Override
  public List<SRCatalogDTO> getListSRCatalogByConfigGroupIBPMS(String configGroup) {
    log.info("Request to getListSRCatalogByConfigGroupIBPMS : {}", configGroup);
    List<SRCatalogDTO> lstSrCatalog = new ArrayList<>();
    if (!StringUtils.isStringNullOrEmpty(configGroup)) {
      return srOutsideRepository.getListSRCatalogByConfigGroupIBPMS(configGroup);
    }
    return lstSrCatalog;
  }

  private SrInsiteDTO convertSRDate2VietNamDate(SrInsiteDTO oldDto, Double offset) {
    try {
      oldDto.setStartTime(converClientDateToServerDate(oldDto.getStartTime(), offset));
      oldDto.setEndTime(converClientDateToServerDate(oldDto.getEndTime(), offset));
      oldDto.setCreatedTime(converClientDateToServerDate(oldDto.getCreatedTime(), offset));
      oldDto.setSendDate(converClientDateToServerDate(oldDto.getSendDate(), offset));
      oldDto.setUpdatedTime(converClientDateToServerDate(oldDto.getUpdatedTime(), offset));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return oldDto;
  }

  private Date converClientDateToServerDate(Date clientTime, Double offset) {
    Date result = clientTime;
    if (offset == null || offset.equals(0.0)) {
      return result;
    }
    if (clientTime == null) {
      return result;
    }
    try {
      Calendar cal = Calendar.getInstance();
      Date date = result;
      cal.setTime(date);
      cal.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY) - offset.intValue());
      return cal.getTime();
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return result;
  }

}
