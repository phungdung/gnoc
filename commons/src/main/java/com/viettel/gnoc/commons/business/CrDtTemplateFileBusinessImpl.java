package com.viettel.gnoc.commons.business;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.CrDtTemplateFileDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.LogChangeConfigDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.proxy.CrCategoryServiceProxy;
import com.viettel.gnoc.commons.repository.CrDtTemplateFileRepository;
import com.viettel.gnoc.commons.repository.LogChangeConfigRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.CrProcessInsideDTO;
import com.viettel.security.PassTranformer;
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
import org.springframework.web.multipart.MultipartFile;
import viettel.passport.client.UserToken;

@Service
@Slf4j
@Transactional
public class CrDtTemplateFileBusinessImpl implements CrDtTemplateFileBusiness {

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
  private CrDtTemplateFileRepository crDtTemplateFileRepository;

  @Autowired
  private CrCategoryServiceProxy crCategoryServiceProxy;

  @Autowired
  private LogChangeConfigRepository logChangeConfigRepository;

  @Autowired
  TicketProvider ticketProvider;

  @Override
  public List<CrProcessInsideDTO> getParentProcessCBB() {
    log.info("Request to getListCrDtTemplateFile : {}");
    List<CrProcessInsideDTO> lst = crCategoryServiceProxy.getAllCrProcess(null);
    return lst;
  }

  @Override
  public List<CrProcessInsideDTO> getChilProcessCBB(Long parentId) {
    log.info("Request to getListCrDtTemplateFile : {}");
    List<CrProcessInsideDTO> lst = crCategoryServiceProxy.getAllCrProcess(parentId);
    return lst;
  }

  @Override
  public Map<String, String> getLstFileTypeCBB() {
    log.info("Request to getLstFileTypeCBB : {}");
    Map<String, String> result = new HashMap<>();
    for (String key : Constants.CR_FILE_TYPE.getGetText().keySet()) {
      if (("1".equals(key))
          || ("2".equals(key))
          || ("3".equals(key))) {
        result.put(key, getMessageFromChangemanage(key, Constants.CR_FILE_TYPE.getGetText()));
      }
    }
    return result;
  }

  @Override
  public Datatable getListCrDtTemplateFile(CrDtTemplateFileDTO crDtTemplateFileDTO) {
    log.info("Request to getListCrDtTemplateFile : {}", crDtTemplateFileDTO);
    List<CrDtTemplateFileDTO> lstResult = new ArrayList<>();
    Datatable datatable = crDtTemplateFileRepository.getListCrDtTemplateFile(crDtTemplateFileDTO);
    List<CrDtTemplateFileDTO> lst = (List<CrDtTemplateFileDTO>) datatable.getData();
    if (lst != null & !lst.isEmpty()) {
      for (CrDtTemplateFileDTO dto : lst) {
        String result = dto.getTemplateType();
        for (String key : Constants.CR_FILE_TYPE.getGetText().keySet()) {
          if ((result.equals(key))) {
            dto.setTemplateType(
                getMessageFromChangemanage(key, Constants.CR_FILE_TYPE.getGetText()));
            try {
              dto.setPathFile(ftpFolder + "/" + dto.getFileName());
            } catch (Exception e) {
              log.error(e.getMessage(), e);
            }
          }
        }
        lstResult.add(dto);
      }
      datatable.setData(lstResult);
    }
    return datatable;
  }

  @Override
  public ResultInSideDto saveOrUpdate(List<MultipartFile> crFileList,
      CrDtTemplateFileDTO crDtTemplateFileDTO) {
    log.info("Request to saveOrUpdate : {}", crDtTemplateFileDTO);
    crDtTemplateFileDTO.setModifiedDate(new Date());
    if (crFileList != null && !crFileList.isEmpty()) {
      try {
        MultipartFile multipartFile = crFileList.get(0);
        String fullPath = FileUtils.saveFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
            PassTranformer.decrypt(ftpPass), ftpFolder,
            multipartFile.getOriginalFilename(), multipartFile.getBytes());
        crDtTemplateFileDTO.setFileName(FileUtils.getFileName(fullPath));
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    ResultInSideDto resultInSideDto = crDtTemplateFileRepository.saveOrUpdate(crDtTemplateFileDTO);
    if (resultInSideDto.getKey().equals(Constants.RESULT.SUCCESS)) {
      UserToken userToken = ticketProvider.getUserToken();
      if (crDtTemplateFileDTO.getCrDtTemplateFileId() != null
          && crDtTemplateFileDTO.getCrDtTemplateFileId() > 0) {
        logChangeConfigRepository.insertLog(new LogChangeConfigDTO(userToken.getUserName(),
            "Update CrDtTemplateFileDTO",
            "Update CrDtTemplateFileDTO ID: " + resultInSideDto.getId(), crDtTemplateFileDTO,
            null));
      } else {
        logChangeConfigRepository.insertLog(new LogChangeConfigDTO(userToken.getUserName(),
            "Add CrDtTemplateFileDTO",
            "Add CrDtTemplateFileDTO ID: " + resultInSideDto.getId(), crDtTemplateFileDTO,
            null));
      }
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto delete(Long id) {
    log.info("Request to delete : {}", id);
    ResultInSideDto resultInSideDto = crDtTemplateFileRepository.delete(id);
    if (resultInSideDto.getKey().equals(Constants.RESULT.SUCCESS)) {
      UserToken userToken = ticketProvider.getUserToken();
      logChangeConfigRepository.insertLog(new LogChangeConfigDTO(userToken.getUserName(),
          "Delete CrDtTemplateFileDTO",
          "Delete CrDtTemplateFileDTO ID: " + id, null,
          null));

    }
    return resultInSideDto;
  }

  @Override
  public CrDtTemplateFileDTO getObjById(Long paramLong) {
    log.info("Request to getObjById : {}", paramLong);
    CrDtTemplateFileDTO dto = crDtTemplateFileRepository.getObjById(paramLong);
    if (dto != null && StringUtils.isNotNullOrEmpty(dto.getFileName())) {
      try {
        dto.setPathFile(ftpFolder + "/" + dto.getFileName());
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return dto;
  }

  private String getMessageFromChangemanage(String key, Map<String, String> map) {
    Map<String, String> mapFile = Constants.CR_FILE_TYPE.getGetText();
    if (mapFile.containsKey(key)) {
      String strReturn = mapFile.get(key);
      return I18n.getChangeManagement(strReturn);
    }
    return null;
  }
}
