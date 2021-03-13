package com.viettel.gnoc.sr.business;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.model.UsersEntity;
import com.viettel.gnoc.commons.repository.GnocFileRepository;
import com.viettel.gnoc.commons.repository.UnitRepository;
import com.viettel.gnoc.commons.repository.UserRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.GNOC_FILE_BUSSINESS;
import com.viettel.gnoc.commons.utils.Constants.SR_CONFIG;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.sr.dto.SRConfigDTO;
import com.viettel.gnoc.sr.dto.SRDTO;
import com.viettel.gnoc.sr.dto.SRFilesDTO;
import com.viettel.gnoc.sr.repository.SRConfigRepository;
import com.viettel.gnoc.sr.repository.SROutsideRepository;
import com.viettel.gnoc.sr.repository.SrRepository;
import com.viettel.security.PassTranformer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.xml.security.utils.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
public class SrAomBusinessImpl implements SrAomBusiness {

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
  SRConfigRepository srConfigRepository;

  @Autowired
  SROutsideRepository srOutsideRepository;

  @Autowired
  SrRepository srRepository;

  @Autowired
  SrBusiness srBusiness;

  @Autowired
  UserRepository userRepository;

  @Autowired
  UnitRepository unitRepository;

  @Autowired
  GnocFileRepository gnocFileRepository;

  @Override
  public ResultDTO getListSRForGatePro(String fromDate, String toDate) {
    ResultDTO res = new ResultDTO();
    res.setKey(Constants.RESULT.FAIL);
    try {
      if (StringUtils.isStringNullOrEmpty(fromDate)) {
        res.setMessage(I18n.getLanguage("sr.error.fromDate.null"));
        return res;
      }
      try {
        DateTimeUtils.convertStringToDateTime(fromDate);
      } catch (Exception e) {
        log.error(e.getMessage(), e);
        res.setMessage(I18n.getLanguage("sr.error.fromDate.invalid"));
        return res;
      }
      //ko bat buoc nhap toDate
      if (!StringUtils.isStringNullOrEmpty(toDate)) {
        try {
          DateTimeUtils.convertStringToDateTime(toDate);
        } catch (Exception e) {
          log.error(e.getMessage(), e);
          res.setMessage(I18n.getLanguage("sr.error.toDate.invalid"));
          return res;
        }
      }
      List<SRConfigDTO> lstAomConfig = srConfigRepository
          .getByConfigGroup(Constants.SR_CONFIG.DICH_VU_AOM);
      if (lstAomConfig == null || lstAomConfig.isEmpty()) {
        res.setMessage(I18n.getLanguage("sr.error.serviceNotConfig"));
        return res;
      }
      List<SRDTO> lstSR = null;
      try {
        lstSR = srOutsideRepository.getListSRForGatePro(fromDate, toDate, null);
        res.setLstResult(lstSR == null ? new ArrayList<SRDTO>() : lstSR);
      } catch (Exception e) {
        log.error(e.getMessage());
      }
      res.setKey(lstSR != null ? Constants.RESULT.SUCCESS : res.getKey());
      return res;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      res.setMessage(e.getMessage());
      return res;
    }
  }

  @Override
  public ResultDTO updateSRForGatePro(String srCode, String status, String fileContent) {
    ResultDTO res = new ResultDTO();
    res.setKey(Constants.RESULT.FAIL);
    try {
      if (StringUtils.isStringNullOrEmpty(srCode)) {
        res.setMessage(I18n.getLanguage("sr.error.srCode.notnull"));
        return res;
      }
      if (StringUtils.isStringNullOrEmpty(status)) {
        res.setMessage(I18n.getLanguage("sr.error.statusNotNull"));
        return res;
      }
      if (StringUtils.isStringNullOrEmpty(fileContent)) {
        res.setMessage(I18n.getLanguage("sr.error.fileContent.notnull"));
        return res;
      }
      String[] arrSrCode = srCode.split("_");
      String srId = arrSrCode[arrSrCode.length - 1];
      SRDTO srDTO = new SRDTO();
      srDTO.setSrId(srId);
      List<SRDTO> lstSR;
      try {
        lstSR = srOutsideRepository.getListSRForGatePro(null, null, srDTO);
        if (lstSR == null || lstSR.isEmpty()) {
          res.setMessage(I18n.getLanguage("sr.error.cantFindSR"));
          return res;
        }
      } catch (Exception e) {
        log.error(e.getMessage());
        res.setMessage(I18n.getLanguage("sr.error.cantFindSR"));
        return res;
      }
      String statusConfig = "";
      List<SRConfigDTO> lstStatus = srConfigRepository.getByConfigGroup(Constants.SR_CONFIG.STATUS);
      if (lstStatus != null) {
        Optional<SRConfigDTO> optConfig = lstStatus.stream()
            .filter(s -> status.equalsIgnoreCase(s.getConfigCode())).findAny();
        if (!optConfig.isPresent()) {
          res.setMessage(I18n.getLanguage("sr.error.statusIncorrect"));
          return res;
        }
        statusConfig = optConfig.get().getConfigCode();
      }
      if (StringUtils.isStringNullOrEmpty(statusConfig)) {
        res.setMessage(I18n.getLanguage("sr.error.statusIncorrect"));
        return res;
      } else {
        if (statusConfig.equalsIgnoreCase(lstSR.get(0).getStatus())) {
          res.setMessage(I18n.getLanguage("sr.error.sr.status.already")
              .replace("srCode", lstSR.get(0).getSrCode()).replace("status", statusConfig));
          return res;
        }
        if (lstSR.get(0).getStatus().equalsIgnoreCase(Constants.SR_STATUS.DRAFT)
            || lstSR.get(0).getStatus().equalsIgnoreCase(Constants.SR_STATUS.NEW)
            || lstSR.get(0).getStatus().equalsIgnoreCase(Constants.SR_STATUS.REJECTED)
            || lstSR.get(0).getStatus().equalsIgnoreCase(Constants.SR_STATUS.CANCELLED)
            || lstSR.get(0).getStatus().equalsIgnoreCase(Constants.SR_STATUS.CLOSED)) {
          res.setMessage(I18n.getLanguage("sr.error.sr.status.changeError")
              .replace("[0]", lstSR.get(0).getSrCode()).replace("[1]", statusConfig));
          return res;
        }
      }
      // Save file
      try {
        UsersEntity usersEntity = userRepository.getUserByUserName(lstSR.get(0).getSrUser());
        UnitDTO unitToken = unitRepository.findUnitById(usersEntity.getUnitId());
        List<GnocFileDto> gnocFileDtos = new ArrayList<>();
        String fileName = "FileResultGatePro.xlsx";
        org.apache.xml.security.Init.init();
        String fullPath = FileUtils
            .saveFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
                PassTranformer.decrypt(ftpPass), ftpFolder, fileName, Base64.decode(fileContent),
                null);
        String fullPathOld = FileUtils
            .saveUploadFile(fileName, Base64.decode(fileContent), uploadFolder, null);
        //Start save file old
        SRFilesDTO srFileDto = new SRFilesDTO();
        srFileDto.setFileGroup(SR_CONFIG.FILE_GROUP_SR);
        srFileDto.setFileType(SR_CONFIG.FILE_TYPE_OTHER);
        srFileDto.setObejctId(Long.valueOf(srId));
        srFileDto.setFilePath(fullPathOld);
        srFileDto.setFileName(FileUtils.getFileName(fullPathOld));
        srFileDto.setFileContent(fileContent);
        ResultInSideDto resultFileDataOld = srRepository.addSRFile(srFileDto);
        //End save file old
        GnocFileDto gnocFileDto = new GnocFileDto();
        gnocFileDto.setPath(fullPath);
        gnocFileDto.setFileName(fileName);
        gnocFileDto.setCreateUnitId(usersEntity.getUnitId());
        gnocFileDto.setCreateUnitName(unitToken.getUnitName());
        gnocFileDto.setCreateUserId(usersEntity.getUserId());
        gnocFileDto.setCreateUserName(usersEntity.getUsername());
        gnocFileDto.setCreateTime(new Date());
        gnocFileDto.setFileType(SR_CONFIG.FILE_TYPE_OTHER);
        gnocFileDto.setContent(fileContent);
        gnocFileDto.setMappingId(resultFileDataOld.getId());
        gnocFileDtos.add(gnocFileDto);
        gnocFileRepository
            .saveListGnocFileNotDeleteAll(GNOC_FILE_BUSSINESS.SR, Long.valueOf(srId), gnocFileDtos);
      } catch (Exception e) {
        log.error(e.getMessage(), e);
        res.setMessage("Decode file content failed: " + e.getMessage());
        return res;
      }
      String rsUpdate = srBusiness.updateStatusSRForProcess(srId, statusConfig);
      if (Constants.RESULT.SUCCESS.equalsIgnoreCase(rsUpdate)) {
        res.setMessage(rsUpdate);
        res.setKey(rsUpdate);
        return res;
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      res.setMessage(e.getMessage());
    }
    return res;
  }
}
