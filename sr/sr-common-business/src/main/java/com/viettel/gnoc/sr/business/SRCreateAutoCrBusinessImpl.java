package com.viettel.gnoc.sr.business;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.commons.proxy.CrServiceProxy;
import com.viettel.gnoc.commons.proxy.SrCategoryServiceProxy;
import com.viettel.gnoc.commons.repository.GnocFileRepository;
import com.viettel.gnoc.commons.repository.UserRepository;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.Constants.GNOC_FILE_BUSSINESS;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.Constants.SR_CONFIG;
import com.viettel.gnoc.commons.utils.DateUtil;
import com.viettel.gnoc.commons.utils.ExcelWriterUtils;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.sr.dto.SRCatalogDTO;
import com.viettel.gnoc.sr.dto.SRCreateAutoCRDTO;
import com.viettel.gnoc.sr.dto.SRFilesDTO;
import com.viettel.gnoc.sr.dto.SRMappingProcessCRDTO;
import com.viettel.gnoc.sr.dto.SRMopDTO;
import com.viettel.gnoc.sr.dto.SrInsiteDTO;
import com.viettel.gnoc.sr.model.SRFilesEntity;
import com.viettel.gnoc.sr.repository.SRCatalogRepository2;
import com.viettel.gnoc.sr.repository.SRCreateAutoCrRepository;
import com.viettel.gnoc.sr.repository.SrRepository;
import com.viettel.security.PassTranformer;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import viettel.passport.client.UserToken;

@Service
@Transactional
@Slf4j
public class SRCreateAutoCrBusinessImpl implements SRCreateAutoCrBusiness {

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
  SRCreateAutoCrRepository srCreateAutoCrRepository;

  @Autowired
  SRCatalogRepository2 srCatalogRepository2;

  @Autowired
  SrCategoryServiceProxy srCategoryServiceProxy;

  @Autowired
  SrRepository srRepository;

  @Autowired
  TicketProvider ticketProvider;

  @Autowired
  GnocFileRepository gnocFileRepository;

  @Autowired
  UserRepository userRepository;

  @Autowired
  CrServiceProxy crServiceProxy;

  private Map<String, GnocFileDto> gnocFilePathProceeMap = new HashMap<>();

  @Override
  public SRCreateAutoCRDTO findSrCreateAutoBySrId(SRCreateAutoCRDTO dto) {
    log.info("Request to findSrCreateAutoBySrId : {}", dto);
    UserToken userToken = ticketProvider.getUserToken();
    SRCreateAutoCRDTO resultDTO = new SRCreateAutoCRDTO();
    SRMappingProcessCRDTO srMappingProcessCRDTO = new SRMappingProcessCRDTO();
    srMappingProcessCRDTO.setServiceCode(dto.getServiceCode());
    List<SRMappingProcessCRDTO> listSRMapping = srCategoryServiceProxy
        .getListSRMappingProcessCRDTO(srMappingProcessCRDTO);
    List<SRCreateAutoCRDTO> lst = srCreateAutoCrRepository.searchSRCreateAutoCR(dto, 0, 0, "", "");
    Double offset = userRepository.getOffsetFromUser(userToken.getUserName());
    if (lst != null && !lst.isEmpty()) {
      resultDTO = lst.isEmpty() ? null : lst.get(0);
      converDate(resultDTO, -offset);
    } else {
      if (listSRMapping != null && !listSRMapping.isEmpty() && listSRMapping.get(0) != null) {
        resultDTO = listSRMapping.get(0).toSRCreateAutoCRDTO();
      }
    }
    if (!dto.isInsert()) {
      if (resultDTO == null) {
        resultDTO = new SRCreateAutoCRDTO();
      }
      String pathProcess = "";
      String fileTypeId = "";
      SrInsiteDTO srInsiteDTO = srRepository.getDetail(dto.getSrId(), userToken.getUserName());
      if (!StringUtils.isStringNullOrEmpty(srInsiteDTO.getSrUser()) && userToken.getUserName()
          .equals(srInsiteDTO.getSrUser())) {
        if (resultDTO.getFileTypeId() != null && resultDTO.getPathFileProcess() != null) {
          gnocFilePathProceeMap.clear();
          mapFileProcess(resultDTO.getId());
          String[] arrFilrType = resultDTO.getFileTypeId().split(";");
          String[] arrpath = resultDTO.getPathFileProcess().split(";");
          if (arrFilrType.length == arrpath.length) {
            for (int i = 0; i < arrFilrType.length; i++) {
              if (arrFilrType[i] != null) {
                fileTypeId += arrFilrType[i] + ";";
                GnocFileDto gnocFileDto =
                    gnocFilePathProceeMap.containsKey(arrFilrType[i]) ? gnocFilePathProceeMap
                        .get(arrFilrType[i]) : null;
                if (gnocFileDto != null) {
                  pathProcess += gnocFileDto.getPath() + ";";
                }
              }
            }
          }
        }
        if (StringUtils.isNotNullOrEmpty(pathProcess) && StringUtils.isNotNullOrEmpty(fileTypeId)) {
          pathProcess = pathProcess.substring(0, pathProcess.length() - 1);
          fileTypeId = fileTypeId.substring(0, fileTypeId.length() - 1);
          resultDTO.setFileTypeId(fileTypeId);
          resultDTO.setPathFileProcess(pathProcess);
        }
        GnocFileDto gnocFileDto = new GnocFileDto();
        gnocFileDto.setBusinessCode(GNOC_FILE_BUSSINESS.SR_CREATE_AUTO_CR);
        gnocFileDto.setBusinessId(resultDTO.getId());
        List<GnocFileDto> lstFile = gnocFileRepository.getListGnocFileForSR(gnocFileDto);
        resultDTO.setGnocFileDtos(lstFile);
        if (lstFile != null && lstFile.isEmpty()) {
          resultDTO.setFileName("");
        }
        if (listSRMapping != null && !listSRMapping.isEmpty()) {
          if (listSRMapping.get(0).getIsCrNodes() != null
              && listSRMapping.get(0).getIsCrNodes() == 1L) {
            resultDTO.setIsCrNodes(1L);
          }
          try {
            srMappingProcessCRDTO.setCrProcessParentId(listSRMapping.get(0).getCrProcessParentId());
            srMappingProcessCRDTO.setProxyLocale(I18n.getLocale());
            List<ItemDataCRInside> lstDutyType = srCategoryServiceProxy
                .getDutyTypeByProcessIdProxy(srMappingProcessCRDTO);
            if (lstDutyType != null && !lstDutyType.isEmpty()) {
              resultDTO.setDutyType(lstDutyType.get(0).getDisplayStr());
            }
            SRCreateAutoCRDTO srCreateAutoCRDTO = new SRCreateAutoCRDTO();
            srCreateAutoCRDTO.setExecutionTime(dto.getExecutionTime());
            srCreateAutoCRDTO.setExecutionEndTime(dto.getExecutionEndTime());
            srCreateAutoCRDTO.setSrId(dto.getSrId());
            if (StringUtils.isStringNullOrEmpty(listSRMapping.get(0).getProcessTypeLv3Id())) {
              srCreateAutoCRDTO
                  .setCrProcessId(String.valueOf(listSRMapping.get(0).getCrProcessParentId()));
            } else {
              srCreateAutoCRDTO
                  .setCrProcessId(listSRMapping.get(0).getProcessTypeLv3Id());
            }
            List<SRCreateAutoCRDTO> lstInforTemplateResult = new ArrayList<>();
            if (StringUtils.isNotNullOrEmpty(srCreateAutoCRDTO.getCrProcessId())) {
              String[] arrProcessTypeIdLv3 = srCreateAutoCRDTO.getCrProcessId().split(",");
              if (arrProcessTypeIdLv3 != null && arrProcessTypeIdLv3.length > 0) {
                for (String processTypeIdLv3 : arrProcessTypeIdLv3) {
                  srCreateAutoCRDTO.setCrProcessId(processTypeIdLv3);
                  List<SRCreateAutoCRDTO> lstInforTemplate = srCreateAutoCrRepository
                      .getInforTemplate(srCreateAutoCRDTO);
                  if (lstInforTemplate != null && !lstInforTemplate.isEmpty()) {
                    int size = lstInforTemplate.size();
                    for (int i = (size - 1); i > -1; i--) {
                      if (StringUtils.isStringNullOrEmpty(lstInforTemplate.get(i).getName())
                          && StringUtils
                          .isStringNullOrEmpty(lstInforTemplate.get(i).getPathFileProcess())) {
                        lstInforTemplate.remove(lstInforTemplate.get(i));
                      }
                    }
                  }
                  if (lstInforTemplate != null && !lstInforTemplate.isEmpty()) {
                    lstInforTemplateResult.addAll(lstInforTemplate);
                  }
                }
              }
            }
            if (lstInforTemplateResult != null && !lstInforTemplateResult.isEmpty()) {
              resultDTO.setLstInforTemplate(lstInforTemplateResult);
            }
          } catch (Exception e) {
            log.info(" CrProcessId is null ");
          }
        }
      }
    }
    return resultDTO;
  }

  @Override
  public ResultInSideDto insertOrUpdateSRCreateAutoCr(List<MultipartFile> srFileList,
      List<MultipartFile> filesProcess, SRCreateAutoCRDTO dto) {
    log.info("Request to insertOrUpdateSRCreateAutoCr : {}", srFileList, filesProcess, dto);
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    dto.setSyncStatus(null);
    UserToken userToken = ticketProvider.getUserToken();
    UsersInsideDto unitToken = userRepository.getUserDTOByUserName(userToken.getUserName());
    SRCreateAutoCRDTO srCreateDto = new SRCreateAutoCRDTO();
    srCreateDto.setSrId(dto.getSrId());
    List<GnocFileDto> lstGnocFileDelete = new ArrayList<>();
    List<GnocFileDto> gnocFilesInsert = new ArrayList<>();
    List<SRCreateAutoCRDTO> lst = srCreateAutoCrRepository
        .searchSRCreateAutoCR(srCreateDto, 0, 0, "", "");
    Map<String, String> mapFilePathProcessOld = new HashMap<>();
    Date date = new Date();
    if (lst != null && !lst.isEmpty()) {
      if (StringUtils.isNotNullOrEmpty(lst.get(0).getFileTypeId()) && StringUtils
          .isNotNullOrEmpty(lst.get(0).getPathFileProcess())) {
        String[] arrPathProcess = lst.get(0).getPathFileProcess().split(";");
        String[] arrFileTypeId = lst.get(0).getFileTypeId().split(";");
        if (arrPathProcess.length == arrFileTypeId.length) {
          for (int i = 0; i < arrFileTypeId.length; i++) {
            mapFilePathProcessOld.put(arrFileTypeId[i], arrPathProcess[i]);
          }
        }
      }
    }
    if (filesProcess != null && !filesProcess.isEmpty()) {
      if (dto.getLstInforTemplate() != null && !dto.getLstInforTemplate().isEmpty()) {
        // lấy tất cả file có trong GNOC_FILE
        gnocFilePathProceeMap.clear();
        mapFileProcess(dto.getId());
        String fullPathOld = "";
        String fileTypeId = "";
        Map<String, MultipartFile> mapFiles = new HashMap<>();
        for (int i = 0; i < dto.getLstFileProcess().size(); i++) {
          mapFiles.put(dto.getLstFileProcess().get(i).getTempImportId(), filesProcess.get(i));
        }
        if (mapFiles != null && !mapFiles.isEmpty()) {
          int j = 0;
          for (SRCreateAutoCRDTO dtoTemplate : dto.getLstInforTemplate()) {
            if (mapFiles.containsKey(dtoTemplate.getTempImportId())) {
              GnocFileDto gnocFileAdd = new GnocFileDto();
              try {
                // File luu
                MultipartFile multipartFile = mapFiles.get(dtoTemplate.getTempImportId());
                GnocFileDto filePathSrCr = new GnocFileDto();
                filePathSrCr.setFileName(multipartFile.getOriginalFilename());
                filePathSrCr.setBytes(multipartFile.getBytes());
                filePathSrCr.setCreateTime(date);
                GnocFileDto filePathDto = crServiceProxy.getFilePathSrCr(filePathSrCr);
                if (filePathDto == null) {
                  resultInSideDto.setKey(RESULT.ERROR);
                  resultInSideDto.setMessage(RESULT.ERROR);
                  return resultInSideDto;
                }
                String pathUploadProcessFtp = filePathDto.getPath();
                String pathUploadProcessOld = filePathDto.getPathTemplate();
                // File check validate
                byte[] bytesFileTemplateProcess = FileUtils
                    .getFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
                        PassTranformer.decrypt(ftpPass), dtoTemplate.getPathFileProcess());
                String pathTemplateProcess = FileUtils
                    .saveTempFile(FileUtils.getFileName(dtoTemplate.getPathFileProcess()),
                        bytesFileTemplateProcess,
                        tempFolder);
                // Check
                ResultInSideDto validateSrFileProcess = validateFileProcess(pathTemplateProcess,
                    pathUploadProcessOld);
                if (!validateSrFileProcess.getKey().equals(RESULT.SUCCESS)) {
                  validateSrFileProcess
                      .setMessage(validateSrFileProcess.getMessage() + ": " + mapFiles
                          .get(dtoTemplate.getTempImportId()).getOriginalFilename());
                  return validateSrFileProcess;
                }
                // them vao GnocFile
                gnocFileAdd.setPath(pathUploadProcessFtp);
                gnocFileAdd.setFileName(FileUtils.getFileName(pathUploadProcessFtp));
                gnocFileAdd.setTemplateId(dtoTemplate.getTempImportId() != null ? Long
                    .parseLong(dtoTemplate.getTempImportId()) : null);
                gnocFileAdd.setFileType(dtoTemplate.getFileType());
                gnocFileAdd.setCreateUnitId(userToken.getDeptId());
                gnocFileAdd.setCreateUnitName(unitToken.getUnitName());
                gnocFileAdd.setCreateUserId(userToken.getUserID());
                gnocFileAdd.setCreateUserName(userToken.getUserName());
                gnocFileAdd.setCreateTime(date);
                gnocFileAdd.setMappingId(dto.getId());
                gnocFilesInsert.add(gnocFileAdd);
                String key = dtoTemplate.getFileType() + "_" + dtoTemplate.getTempImportId();
                if (gnocFilePathProceeMap.containsKey(key)) {
                  lstGnocFileDelete.add(gnocFilePathProceeMap.get(key));
                }
                fullPathOld += pathUploadProcessOld + ";";
                fileTypeId += key + ";";
              } catch (Exception e) {
                log.error(e.getMessage(), e);
              }
            } else {
              if (dto != null && StringUtils.isNotNullOrEmpty(dto.getFileTypeId())) {
                String[] arrFileTypeId = dto.getFileTypeId().split(";");
                fileTypeId += arrFileTypeId[j] + ";";
                if (mapFilePathProcessOld.get(arrFileTypeId[j]) != null) {
                  fullPathOld += mapFilePathProcessOld.get(arrFileTypeId[j]) + ";";
                }
              }
            }
            j++;
          }
        }
        if (StringUtils.isNotNullOrEmpty(fullPathOld) && fullPathOld.endsWith(";")) {
          fullPathOld = fullPathOld.substring(0, fullPathOld.length() - 1);
          dto.setPathFileProcess(fullPathOld);
        }
        if (StringUtils.isNotNullOrEmpty(fileTypeId) && fileTypeId.endsWith(";")) {
          fileTypeId = fileTypeId.substring(0, fileTypeId.length() - 1);
          dto.setFileTypeId(fileTypeId);
        }
        dto.setTimeAttach(date);
      }
    } else {
      if (lst != null && !lst.isEmpty()) {
        dto.setPathFileProcess(lst.get(0).getPathFileProcess());
      }
    }
    Double offset = userRepository.getOffsetFromUser(userToken.getUserName());
    converDate(dto, offset);
    ResultInSideDto validateFileWo = validate(dto, srFileList);
    if (validateFileWo.getKey().equals(RESULT.FAIL)) {
      return validateFileWo;
    } else {
      if (lst != null && !lst.isEmpty()) {
        resultInSideDto = srCreateAutoCrRepository.insertOrUpdateSRCreateAutoCr(dto);
//        update File
        if (RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
          List<SRFilesEntity> srFilesOldToDelete = srRepository
              .getListSRFileByObejctId(SR_CONFIG.FILE_GROUP_SR_AUTO, SR_CONFIG.FILE_TYPE_OTHER,
                  lst.get(0).getId());
          if (dto.getWoTestTypeId() != null) {
            //xu ly cap nhat file cu
            if (srFilesOldToDelete != null && !srFilesOldToDelete.isEmpty()) {
              if (dto.getGnocFileDtos() != null && dto.getGnocFileDtos().size() > 0) {
                List<Long> listIdFileIdNew = dto.getGnocFileDtos().stream()
                    .map(GnocFileDto::getMappingId).collect(Collectors.toList());
                if (srFilesOldToDelete != null && srFilesOldToDelete.size() > 0) {
                  srFilesOldToDelete.removeIf(i -> (listIdFileIdNew.contains(i.getFileId())));
                }
              }

              //delete file cu
              if (srFilesOldToDelete != null && srFilesOldToDelete.size() > 0) {
                srFilesOldToDelete.forEach(i -> {
                  gnocFileRepository
                      .deleteGnocFileByMapping(GNOC_FILE_BUSSINESS.SR_CREATE_AUTO_CR,
                          i.getFileId());
                  srRepository.deleteSRFile(i.getFileId());
                });
              }
            }
            //xu ly them file moi
            if (dto.getGnocFileDtos() != null || !dto.getGnocFileDtos().isEmpty()) {
              if (srFileList != null && !srFileList.isEmpty()) {
                List<GnocFileDto> gnocFileInserts = new ArrayList<>();
                try {
                  for (GnocFileDto gnocFileAdd : dto.getGnocFileDtos()) {
                    if (gnocFileAdd.getIndexFile() == null) {
                      continue;
                    }
                    MultipartFile multipartFile = srFileList
                        .get(gnocFileAdd.getIndexFile().intValue());
                    String fullPath = FileUtils
                        .saveFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
                            PassTranformer.decrypt(ftpPass), ftpFolder,
                            multipartFile.getOriginalFilename(), multipartFile.getBytes(), date);
                    String fullPathOld = FileUtils
                        .saveUploadFile(multipartFile.getOriginalFilename(),
                            multipartFile.getBytes(),
                            uploadFolder, date);
                    //Start save file old
                    SRFilesDTO srFilesDTO = new SRFilesDTO();
                    srFilesDTO.setFileId(gnocFileAdd.getMappingId());
                    srFilesDTO.setFilePath(fullPathOld);
                    srFilesDTO.setFileName(FileUtils.getFileName(fullPathOld));
                    srFilesDTO.setFileType(SR_CONFIG.FILE_TYPE_OTHER);
                    srFilesDTO.setObejctId(lst.get(0).getId());
                    srFilesDTO.setFileGroup(SR_CONFIG.FILE_GROUP_SR_AUTO);
                    srFilesDTO.setRequireCreateSR(gnocFileAdd.getRequired());
                    ResultInSideDto resultFileDataOld = srRepository.addSRFile(srFilesDTO);
                    //End save file old
                    gnocFileAdd.setPath(fullPath);
                    gnocFileAdd.setFileName(multipartFile.getOriginalFilename());
                    gnocFileAdd.setCreateUnitId(userToken.getDeptId());
                    gnocFileAdd.setCreateUnitName(unitToken.getUnitName());
                    gnocFileAdd.setCreateUserId(userToken.getUserID());
                    gnocFileAdd.setCreateUserName(userToken.getUserName());
                    gnocFileAdd.setCreateTime(date);
                    gnocFileAdd.setFileType(SR_CONFIG.FILE_TYPE_OTHER);
                    gnocFileAdd.setMappingId(resultFileDataOld.getId());
                    gnocFileInserts.add(gnocFileAdd);
                  }
                  gnocFileRepository
                      .saveListGnocFileNotDeleteAll(GNOC_FILE_BUSSINESS.SR_CREATE_AUTO_CR,
                          lst.get(0).getId(), gnocFileInserts);
                } catch (Exception e) {
                  log.error(e.getMessage(), e);
                }
              } else {
                if (dto.getGnocFileDtos() != null && !dto.getGnocFileDtos().isEmpty()) {
                  gnocFileRepository
                      .saveListGnocFileNotDeleteAll(GNOC_FILE_BUSSINESS.SR_CREATE_AUTO_CR,
                          lst.get(0).getId(), dto.getGnocFileDtos());
                }
              }
            }
          } else {
            if (srFilesOldToDelete != null && !srFilesOldToDelete.isEmpty()) {
              for (SRFilesEntity srFilesEntity : srFilesOldToDelete) {
                srRepository.deleteSRFile(srFilesEntity.getFileId());
              }
              GnocFileDto gnocFileDto = new GnocFileDto();
              gnocFileDto.setBusinessCode(GNOC_FILE_BUSSINESS.SR_CREATE_AUTO_CR);
              gnocFileDto.setBusinessId(lst.get(0).getId());
              gnocFileRepository
                  .deleteGnocFile(GNOC_FILE_BUSSINESS.SR_CREATE_AUTO_CR, lst.get(0).getId());
            }
          }
        }
        //xóa fileProcess trong GnocFile trong trường hợp update
        if (lstGnocFileDelete != null && !lstGnocFileDelete.isEmpty()) {
          for (GnocFileDto deleteGnocFile : lstGnocFileDelete) {
            gnocFileRepository.deleteGnocFileByDto(deleteGnocFile);
          }
        }
        // thêm mới fileProcess vào trong Gnoc_File
        if (gnocFilesInsert != null && !gnocFilesInsert.isEmpty()) {
          gnocFileRepository
              .saveListGnocFileNotDeleteAll(GNOC_FILE_BUSSINESS.SR_CREATE_AUTO_CR_PROCESS,
                  dto.getId(), gnocFilesInsert);
        }
      } else {
        resultInSideDto = srCreateAutoCrRepository.insertOrUpdateSRCreateAutoCr(dto);
      }
    }
    return resultInSideDto;
  }

  private void mapFileProcess(Long id) {
    GnocFileDto gnocFileSearch = new GnocFileDto();
    gnocFileSearch.setBusinessId(id);
    gnocFileSearch.setBusinessCode(GNOC_FILE_BUSSINESS.SR_CREATE_AUTO_CR_PROCESS);
    gnocFileSearch.setMappingId(id);
    List<GnocFileDto> lstGnocFileDto = gnocFileRepository.getListGnocFileByDto(gnocFileSearch);
    if (lstGnocFileDto != null && !lstGnocFileDto.isEmpty()) {
      for (GnocFileDto gnocFileDto : lstGnocFileDto) {
        String key = gnocFileDto.getFileType() + "_" + gnocFileDto.getTemplateId();
        gnocFilePathProceeMap.put(key, gnocFileDto);
      }
    }
  }

  private ResultInSideDto vailidateFileMappingIp(String path, String pathTemplate,
      Map<String, String> mapFile, Map<String, String> ipMops) {
    ResultInSideDto resultInSideDto = new ResultInSideDto(null, RESULT.FAIL, RESULT.FAIL);
    if (!path.contains(".xlsx") && !path.contains(".xls")) {
      resultInSideDto.setMessage(I18n.getValidation("sr.import.file.invalid"));
      return resultInSideDto;
    } else {
      boolean check = validateFileData(path, pathTemplate);
      if (!check) {
        resultInSideDto.setMessage(I18n.getValidation("sr.import.errorTemplate"));
        return resultInSideDto;
      }
    }
    File fileImport = new File(path);
    try {
      List<Object[]> dataImportList = CommonImport.getDataFromExcelFile(
          fileImport,
          0,
          1,
          0,
          2,
          1000
      );
      if (dataImportList.isEmpty()) {
        resultInSideDto.setMessage("File" + I18n.getLanguage("common.searh.nodata"));
        return resultInSideDto;
      } else {
        int index = 0;
        List<SRFilesDTO> lstResult = new ArrayList<>();
        for (Object[] obj : dataImportList) {
          SRFilesDTO srResilt = new SRFilesDTO();
          if (obj[1] != null) {
            srResilt.setIpMop(obj[1].toString().trim());
            if (!ipMops.containsKey(obj[1].toString().trim())) {
              srResilt.setComments(I18n.getLanguage("sr.template.erro.ipMop"));
            }
          } else {
            srResilt.setComments(I18n.getLanguage("sr.template.ipMop.notnull"));
          }
          if (obj[2] != null) {
            srResilt.setFileName(obj[2].toString().trim());
            if (!mapFile.containsKey(obj[2].toString().trim())) {
              srResilt.setComments(
                  (srResilt.getComments() != null ? (srResilt.getComments() + ";") : "") + I18n
                      .getLanguage("sr.template.erro.fileWo"));
            }
          } else {
            srResilt.setComments(
                (srResilt.getComments() != null ? (srResilt.getComments() + ";") : "") + I18n
                    .getLanguage("sr.template.fileName.notnull"));
          }
          if (StringUtils.isNotNullOrEmpty(srResilt.getComments())) {
            index++;
          } else {
            if (lstResult != null && !lstResult.isEmpty()) {
              for (SRFilesDTO dto : lstResult) {
                if ((dto.getIpMop() == srResilt.getIpMop())) {
                  srResilt.setComments(I18n.getLanguage("sr.template.IP.exists"));
                  index++;
                  break;
                }
              }
            }
          }
          lstResult.add(srResilt);
        }
        if (index != 0) {
          resultInSideDto.setFilePath(exportResultAutoCreateCr(lstResult));
          resultInSideDto.setMessage(
              I18n.getLanguage("common.update.fail"));
          return resultInSideDto;
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return new ResultInSideDto(null, RESULT.SUCCESS, RESULT.SUCCESS);
  }

  private String exportResultAutoCreateCr(List<SRFilesDTO> lstFileResult) {
    try {
      ExcelWriterUtils ewu = new ExcelWriterUtils();
      String pathFileImport =
          "templates" + File.separator + I18n.getLanguage("sr.template.fileMapping");
      Workbook workBook = ewu.readFileExcelFromTemplate(pathFileImport);
      Sheet sheet = workBook.getSheetAt(0);
      int i = 1;
      CellStyle cellSt1 = sheet.getRow(0).getCell(0).getCellStyle();
      ewu.createCell(sheet, 3, 0, I18n.getLanguage("common.importError"), cellSt1);
      for (SRFilesDTO row : lstFileResult) {
        ewu.createCell(sheet, 1, i, row.getIpMop() == null ? "" : row.getIpMop());
        ewu.createCell(sheet, 2, i, row.getFileName() == null ? "" : row.getFileName());
        ewu.createCell(sheet, 3, i, row.getComments() == null ? "" : row.getComments());
        i++;
      }
      String fileResult = tempFolder + File.separator + "ResultImport" + "_" + System
          .currentTimeMillis() + I18n.getLanguage("sr.template.fileMapping");
      ewu.saveToFileExcel(fileResult);
      return fileResult;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

  private ResultInSideDto validate(SRCreateAutoCRDTO dto, List<MultipartFile> srFileList) {
    UserToken userToken = ticketProvider.getUserToken();
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.FAIL);
    if (StringUtils.isStringNullOrEmpty(dto.getCrTitle())) {
      resultInSideDto.setMessage(I18n.getValidation("cr.crTitle.required"));
      return resultInSideDto;
    }
    if (StringUtils.isStringNullOrEmpty(dto.getCrStatus())) {
      resultInSideDto.setMessage(I18n.getValidation("cr.crStatus.required"));
      return resultInSideDto;
    }
    if (!StringUtils.isLongNullOrEmpty(dto.getServiceAffecting()) && "1"
        .equals(dto.getServiceAffecting().toString())) {
      if (StringUtils.isStringNullOrEmpty(dto.getAffectingService())) {
        resultInSideDto.setMessage(I18n.getValidation("cr.affectingService.required"));
        return resultInSideDto;
      }
      if (StringUtils.isStringNullOrEmpty(dto.getTotalAffectingCustomers())) {
        resultInSideDto.setMessage(I18n.getValidation("cr.totalAffectingCustomers.required"));
        return resultInSideDto;
      } else {
        if (dto.getTotalAffectingCustomers() <= 0) {
          resultInSideDto.setMessage(I18n.getValidation("cr.totalAffectingCustomers.negative"));
          return resultInSideDto;
        }
      }
      if (StringUtils.isStringNullOrEmpty(dto.getTotalAffectingMinutes())) {
        resultInSideDto.setMessage(I18n.getValidation("cr.totalAffectingMinutes.required"));
        return resultInSideDto;
      } else {
        if (dto.getTotalAffectingMinutes() <= 0) {
          resultInSideDto.setMessage(I18n.getValidation("cr.totalAffectingMinutes.negative"));
          return resultInSideDto;
        }
      }
    }
    if (dto.getWoFtTypeId() != null) {
      if (StringUtils.isStringNullOrEmpty(dto.getWoContentCDFT())) {
        resultInSideDto.setMessage(I18n.getValidation("cr.woContent.CDFT.required"));
        return resultInSideDto;
      }
      if (StringUtils.isStringNullOrEmpty(dto.getGroupCDFT())) {
        resultInSideDto.setMessage(I18n.getValidation("cr.groupCDPT.required"));
        return resultInSideDto;
      }
      if (StringUtils.isStringNullOrEmpty(dto.getWoFtPriority())) {
        resultInSideDto.setMessage(I18n.getValidation("cr.woFtPriority.required"));
        return resultInSideDto;
      }
    }
    if (dto.getWoTestTypeId() != null) {
      if (StringUtils.isStringNullOrEmpty(dto.getWoContentService())) {
        resultInSideDto.setMessage(I18n.getValidation("cr.woContent.testService.required"));
        return resultInSideDto;
      }
      if (StringUtils.isStringNullOrEmpty(dto.getGroupCdFtService())) {
        resultInSideDto.setMessage(I18n.getValidation("cr.groupCDPT2.required"));
        return resultInSideDto;
      }
      if (StringUtils.isStringNullOrEmpty(dto.getWoTestPriority())) {
        resultInSideDto.setMessage(I18n.getValidation("cr.woTestPriority.required"));
        return resultInSideDto;
      }
    }
    if (dto.getId() != null && dto.getId() > 0) {
      SrInsiteDTO srInsiteDTO = srRepository.getDetail(dto.getSrId(), userToken.getUserName());
      if (srInsiteDTO != null) {
        if (!StringUtils.isStringNullOrEmpty(srInsiteDTO.getSrUser()) && userToken.getUserName()
            .equals(srInsiteDTO.getSrUser())) {
          if (dto.getWoFtTypeId() != null) {
            if (StringUtils.isStringNullOrEmpty(dto.getWoFtStartTime())) {
              resultInSideDto.setMessage(I18n.getValidation("cr.woFtStartTime.required"));
              return resultInSideDto;
            } else if (DateUtil.compareDateTime(dto.getWoFtStartTime(), new Date()) != 1) {
              resultInSideDto.setMessage(I18n.getValidation("cr.error.woFtStartTime"));
              return resultInSideDto;
            }
            if (StringUtils.isStringNullOrEmpty(dto.getWoFtEndTime())) {
              resultInSideDto.setMessage(I18n.getValidation("cr.woFtEndTime.required"));
              return resultInSideDto;
            } else if (DateUtil.compareDateTime(dto.getWoFtEndTime(), dto.getWoFtStartTime())
                != 1) {
              resultInSideDto.setMessage(I18n.getValidation("cr.error.woFtEndTime"));
              return resultInSideDto;
            }
          }
          if (dto.getWoTestTypeId() != null) {
            if (StringUtils.isStringNullOrEmpty(dto.getWoTestStartTime())) {
              resultInSideDto.setMessage(I18n.getValidation("cr.woTestStartTime.required"));
              return resultInSideDto;
            } else if (DateUtil.compareDateTime(dto.getWoTestStartTime(), new Date()) != 1) {
              resultInSideDto.setMessage(I18n.getValidation("cr.error.woTestStartTime"));
              return resultInSideDto;
            }
            if (StringUtils.isStringNullOrEmpty(dto.getWoTestEndTime())) {
              resultInSideDto.setMessage(I18n.getValidation("cr.woTestEndTime.required"));
              return resultInSideDto;
            } else if (DateUtil.compareDateTime(dto.getWoTestEndTime(), dto.getWoTestStartTime())
                != 1) {
              resultInSideDto.setMessage(I18n.getValidation("cr.error.woTestEndTime"));
              return resultInSideDto;
            }
          }
          SRCatalogDTO sRCatalogDTO = srCatalogRepository2
              .findById(Long.valueOf(srInsiteDTO.getServiceId()));
          SRMappingProcessCRDTO srmpcrdto = new SRMappingProcessCRDTO();
          srmpcrdto.setServiceCode(sRCatalogDTO.getServiceCode());
          List<SRMappingProcessCRDTO> listSRMapping = srCategoryServiceProxy
              .getListSRMappingProcessCRDTO(srmpcrdto);
          boolean isCrNodes = false;
          if (listSRMapping != null && !listSRMapping.isEmpty()) {
            if (listSRMapping.get(0).getIsCrNodes() != null
                && listSRMapping.get(0).getIsCrNodes() == 1L) {
              isCrNodes = true;
              if (dto.getWoTestTypeId() == null) {
                resultInSideDto
                    .setMessage(I18n.getValidation("cr.erro.WoTest.testService.required"));
                return resultInSideDto;
              }
            }
          }
          resultInSideDto = validateDutyTypeCr(dto.getExecutionTime(), dto.getExecutionEndTime(),
              listSRMapping.get(0).getCrProcessParentId());
          if (!RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
            return resultInSideDto;
          }
          if (dto.getWoTestTypeId() != null) {
            if (isCrNodes) {
              Map<String, String> ipMop = new HashMap<>();
              if (dto.getLstMop() != null && !dto.getLstMop().isEmpty()) {
                for (SRMopDTO srMopDTO : dto.getLstMop()) {
                  if (!ipMop.containsKey(srMopDTO.getIpNode())) {
                    ipMop.put(srMopDTO.getIpNode(), srMopDTO.getIpNode());
                  }
                }
              }
              //validate form excel
              Map<String, String> mapFile = new HashMap<>();
              Long indexMaping = null;
              String filePathMapping = null;
              boolean checkFileMapping = false;
              if (dto.getGnocFileDtos() != null && !dto.getGnocFileDtos().isEmpty()) {
                for (GnocFileDto gnocFileDto : dto.getGnocFileDtos()) {
                  // là file Mapping
                  if (gnocFileDto.getRequired() != null && gnocFileDto.getRequired() == 1L) {
                    indexMaping = gnocFileDto.getIndexFile();
                    filePathMapping = gnocFileDto.getPath();
                    checkFileMapping = true;
                  } else {
                    if (StringUtils.isStringNullOrEmpty(gnocFileDto.getIndexFile())) {
                      String fileName = gnocFileDto.getFileName();
                      if (StringUtils.isNotNullOrEmpty(fileName) && !mapFile
                          .containsKey(fileName)) {
                        // add những file trong database không phải là file mapping
                        mapFile.put(fileName, fileName);
                      }
                    }
                  }
                }
              }
              if (!checkFileMapping) {
                resultInSideDto.setMessage(I18n.getValidation("sr.erro.FileMapping.required"));
                return resultInSideDto;
              }

              try {
                String pathSaveTempFile = "";
                String fileNameMaping = "&#FileMappingIP#&";
                if (!srFileList.isEmpty() && srFileList != null) {
                  if (indexMaping != null) {
                    MultipartFile multipartFile = srFileList.get(indexMaping.intValue());
                    fileNameMaping = multipartFile.getOriginalFilename();
                  }
                  for (MultipartFile file : srFileList) {
                    String originalFilename = file.getOriginalFilename();
                    // kiểm tra file tải lên có trùng tên file không
                    if (mapFile.containsKey(originalFilename)) {
                      resultInSideDto.setKey(RESULT.FAIL);
                      resultInSideDto.setMessage(
                          originalFilename + " : " + I18n
                              .getLanguage("sr.template.erro.fileWoName"));
                      return resultInSideDto;
                    } else {
                      // add tên file không phải là tên file mapping ip
                      if (!fileNameMaping.toLowerCase()
                          .equals(originalFilename.toLowerCase())) {
                        mapFile.put(originalFilename, originalFilename);
                      }
                    }
                  }
                }
                if (mapFile == null || mapFile.entrySet().isEmpty()) {
                  resultInSideDto.setKey(RESULT.FAIL);
                  resultInSideDto.setMessage(I18n.getLanguage("sr.template.erro.fileWoTest"));
                  return resultInSideDto;
                }
                String pathFileTemplate =
                    "templates" + File.separator + I18n.getLanguage("sr.template.fileMapping");
                if (indexMaping != null) {
                  MultipartFile multipartFile = srFileList.get(indexMaping.intValue());
                  pathSaveTempFile = FileUtils
                      .saveTempFile(multipartFile.getOriginalFilename(), multipartFile.getBytes(),
                          tempFolder);
                  ResultInSideDto validate = vailidateFileMappingIp(pathSaveTempFile,
                      pathFileTemplate,
                      mapFile, ipMop);
                  if (validate.getKey().equals(RESULT.FAIL)) {
                    return validate;
                  }
                } else if (indexMaping == null && StringUtils.isNotNullOrEmpty(filePathMapping)) {
                  byte[] bytes = FileUtils
                      .getFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
                          PassTranformer.decrypt(ftpPass), filePathMapping);
                  pathSaveTempFile = FileUtils
                      .saveTempFile(FileUtils.getFileName(filePathMapping), bytes,
                          tempFolder);
                  ResultInSideDto validate = vailidateFileMappingIp(pathSaveTempFile,
                      pathFileTemplate,
                      mapFile, ipMop);
                  if (validate.getKey().equals(RESULT.FAIL)) {
                    return validate;
                  }
                }
              } catch (Exception e) {
                log.error(e.getMessage(), e);
              }
            } else {
              if (srFileList != null && !srFileList.isEmpty()) {
                if (srFileList.size() > 1) {
                  resultInSideDto.setKey(RESULT.FAIL);
                  resultInSideDto.setMessage(I18n.getValidation("sr.import.file.one"));
                  return resultInSideDto;
                }
                try {
                  MultipartFile multipartFile = srFileList.get(0);
                  String fullPath = FileUtils
                      .saveTempFile(multipartFile.getOriginalFilename(), multipartFile.getBytes(),
                          tempFolder);
                  String pathFileTemplate =
                      "templates" + File.separator + I18n.getLanguage("sr.template.import");
                  ResultInSideDto validate = validateFileImport(fullPath, pathFileTemplate);
                  if (validate.getKey().equals(RESULT.FAIL)) {
                    return validate;
                  }
                } catch (Exception e) {
                  log.error(e.getMessage(), e);
                }
              } else if (dto.getGnocFileDtos() != null && !dto.getGnocFileDtos().isEmpty()) {
                if (dto.getGnocFileDtos().size() > 1) {
                  resultInSideDto.setKey(RESULT.FAIL);
                  resultInSideDto.setMessage(I18n.getValidation("sr.import.file.one"));
                  return resultInSideDto;
                }
                GnocFileDto gnocFileCheck = dto.getGnocFileDtos().get(0);
                try {
                  byte[] bytes = FileUtils
                      .getFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
                          PassTranformer.decrypt(ftpPass), gnocFileCheck.getPath());
                  String pathCheck = FileUtils
                      .saveTempFile(FileUtils.getFileName(gnocFileCheck.getPath()), bytes,
                          tempFolder);
                  String pathFileTemplate =
                      "templates" + File.separator + I18n.getLanguage("sr.template.import");
                  ResultInSideDto validate = validateFileImport(pathCheck, pathFileTemplate);
                  if (validate.getKey().equals(RESULT.FAIL)) {
                    return validate;
                  }
                } catch (Exception e) {
                  log.info(e.getMessage(), e);
                }
              }
            }
          }
        }
      }
    }
    resultInSideDto.setKey(RESULT.SUCCESS);
    return resultInSideDto;
  }

  public Workbook readFileExcelResource(String filePathName) {
    Workbook workbook = null;
    InputStream inp = null;
    try {
      Resource resource = new ClassPathResource(filePathName);
      inp = resource.getInputStream();
      workbook = WorkbookFactory.create(inp);
    } catch (FileNotFoundException ex) {
      log.error(ex.getMessage(), ex);
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
    } finally {
      try {
        if (inp != null) {
          inp.close();
        }
      } catch (IOException ex) {
        log.error(ex.getMessage(), ex);
      }
    }
    return workbook;
  }

  private ResultInSideDto validateFileImport(String path, String pathTemplate) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    if (!path.contains(".xlsx") && !path.contains(".xls")) {
      resultInSideDto.setMessage(I18n.getValidation("sr.import.file.invalid"));
      return resultInSideDto;
    } else {
      boolean check = validateFileData(path, pathTemplate);
      if (!check) {
        resultInSideDto.setKey(RESULT.FAIL);
        resultInSideDto.setMessage(I18n.getLanguage("sr.template.1CR.nNode.import"));
        return resultInSideDto;
      }
    }
    return resultInSideDto;
  }

  private boolean validateFileData(String fileImportPathOut, String pathTemplate) {
    ExcelWriterUtils ewu = new ExcelWriterUtils();
    Workbook workBook = readFileExcelResource(pathTemplate);
    Sheet sheetTemplate = workBook.getSheetAt(0);

    Workbook workBook2 = ewu.readFileExcel(fileImportPathOut);
    Sheet sheetFileImport = workBook2.getSheetAt(0);

    Row rowFileImport = sheetFileImport.getRow(0);
    Row rowTemplate = sheetTemplate.getRow(0);
    String arrHeaderTemplate = "";
    String arrHeaderFileImport = "";
    int k = rowTemplate.getLastCellNum();
    for (int i = 1; i < k; i++) {
      if (rowTemplate.getCell(i).getStringCellValue() != null) {
        if (rowTemplate.getCell(i).getStringCellValue().contains("(*)")) {
          arrHeaderTemplate += "," + rowTemplate.getCell(i).getStringCellValue()
              .substring(0, rowTemplate.getCell(i).getStringCellValue().lastIndexOf(" "));
        } else {
          arrHeaderTemplate += "," + rowTemplate.getCell(i).getStringCellValue().trim();
        }
      } else {
        break;
      }
    }
    if (rowFileImport != null) {
      try {
        int l = rowFileImport.getLastCellNum();
        for (int j = 1; j < l; j++) {
          if (rowFileImport.getCell(j).getStringCellValue() != null) {
            if (rowFileImport.getCell(j).getStringCellValue().contains("(*)")) {
              arrHeaderFileImport += "," + rowFileImport.getCell(j).getStringCellValue()
                  .substring(0, rowFileImport.getCell(j).getStringCellValue().lastIndexOf(" "));
            } else {
              arrHeaderFileImport += "," + rowFileImport.getCell(j).getStringCellValue().trim();
            }
          } else {
            break;
          }
        }
      } catch (Exception e) {
        log.info(e.getMessage(), e);
        return false;
      }
    } else {
      return false;
    }

    if (!arrHeaderTemplate.equals(arrHeaderFileImport)) {
      return false;
    }

    return true;
  }

  public boolean getFileData(String filePathOut, String fileupload) {
    ExcelWriterUtils ewu = new ExcelWriterUtils();
    String templatePathOut = fileupload;//BIEU MAU
    if (templatePathOut.toLowerCase().endsWith(".xlsx") || templatePathOut.toLowerCase()
        .endsWith(".xls")) {
      Workbook workBook = ewu.readFileExcel(templatePathOut);
      if (workBook == null) {
        return false;
      }
      Sheet sheetTemplate = workBook.getSheetAt(0); //BIEU MAU

      Workbook workBook2 = ewu.readFileExcel(filePathOut);//FILE IMPORT VAO`
      Sheet sheetFileImport = workBook2.getSheetAt(0);
      int size = sheetTemplate.getLastRowNum();
//            boolean checkAll = true;
      for (int t = 0; t <= size; t++) {
        if (sheetTemplate.getRow(t) != null) {
          if (sheetFileImport.getRow(t) != null) {
            Row rowTemplate = sheetTemplate.getRow(t);
            Row rowFileImport = sheetFileImport.getRow(t);
            String arrHeaderTemplate = "";
            String arrHeaderFileImport = "";
            int k = rowTemplate.getLastCellNum();
            for (int i = 0; i < k; i++) {
              if (rowTemplate.getCell(i) == null) {
                continue;
              }
              if (rowTemplate.getCell(i).getStringCellValue() != null) {
                if (rowTemplate.getCell(i).getStringCellValue().contains("(*)")) {
                  arrHeaderTemplate += "," + rowTemplate.getCell(i).getStringCellValue()
                      .substring(0, rowTemplate.getCell(i).getStringCellValue().lastIndexOf(" "));
                } else {
                  arrHeaderTemplate += "," + rowTemplate.getCell(i).getStringCellValue().trim();
                }
              } else {
                break;
              }
            }
            int l = rowFileImport.getLastCellNum();
            for (int j = 0; j < l; j++) {
              if (rowFileImport.getCell(j) == null) {
                continue;
              }
              if (rowFileImport.getCell(j).getStringCellValue() != null) {
                if (rowFileImport.getCell(j).getStringCellValue().contains("(*)")) {
                  arrHeaderFileImport += "," + rowFileImport.getCell(j).getStringCellValue()
                      .substring(0, rowFileImport.getCell(j).getStringCellValue().lastIndexOf(" "));
                } else {
                  arrHeaderFileImport += "," + rowFileImport.getCell(j).getStringCellValue().trim();
                }
              } else {
                break;
              }
            }
            if (!arrHeaderTemplate.equals(arrHeaderFileImport)) {
              return false;
            } else {
              return true;
            }
          } else {
            return false;
          }
        } else {
          return false;
        }
      }
    } else {
      return false;
    }
    return true;
  }

  private ResultInSideDto validateFileProcess(String filePathOut, String fileupload) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    if (filePathOut.substring(filePathOut.lastIndexOf("."), filePathOut.length())
        .equalsIgnoreCase(fileupload.substring(fileupload.lastIndexOf("."), fileupload.length()))) {
      if (filePathOut.toLowerCase().endsWith(".xlsx") || filePathOut.toLowerCase()
          .endsWith(".xls")) {
        if (!getFileData(filePathOut, fileupload)) {
          resultInSideDto.setKey(RESULT.ERROR);
          resultInSideDto.setMessage(I18n.getValidation("sr.upload.file.notTemp"));
        }
      }
    } else {
      resultInSideDto.setKey(RESULT.ERROR);
      resultInSideDto.setMessage(I18n.getValidation("srRoleAction.import.errorTemplate"));
    }
    return resultInSideDto;
  }

  private ResultInSideDto validateDutyTypeCr(Date startDate, Date endDate, Long processId) {
    ResultInSideDto resultInSideDto = new ResultInSideDto(null, RESULT.FAIL, RESULT.FAIL);
    if (startDate != null
        && startDate.compareTo(new Date()) < 0) {
      resultInSideDto.setMessage(I18n.getValidation("cr.validate.executionTime.fail"));
      return resultInSideDto;
    }
    if (endDate != null
        && endDate.compareTo(startDate) < 0) {
      resultInSideDto.setMessage(I18n.getValidation("cr.validate.executionEndTime.fail"));
      return resultInSideDto;
    }
    SRMappingProcessCRDTO srMappingProcessCRDTO = new SRMappingProcessCRDTO();
    srMappingProcessCRDTO.setCrProcessParentId(processId);
    srMappingProcessCRDTO.setProxyLocale(I18n.getLocale());
    List<ItemDataCRInside> lstDutyType = srCategoryServiceProxy
        .getDutyTypeByProcessIdProxy(srMappingProcessCRDTO);
    if (lstDutyType != null && !lstDutyType.isEmpty()) {
      ItemDataCRInside dataCR = lstDutyType.get(0);
      String[] startendarray = dataCR.getSecondValue().split(",");
      if (startendarray.length > 1) {
        String[] startDuty = startendarray[0].split(":");
        String[] endDuty = startendarray[1].split(":");
        if (startDuty.length > 2 && endDuty.length > 2) {
          Calendar startDutyCal = Calendar.getInstance();
          startDutyCal.clear();
          startDutyCal.setTime(startDate);
          if (startDate != null) {
            startDutyCal.set(startDate.getYear() + 1900, startDate.getMonth(), startDate.getDate(),
                Integer.valueOf(startDuty[0]), Integer.valueOf(startDuty[1]),
                Integer.valueOf(startDuty[2]));
          }
          Date startDutyDate = startDutyCal.getTime();
          Calendar endDutyCal = Calendar.getInstance();
          endDutyCal.clear();
          if (startDate != null) {
            endDutyCal.set(startDate.getYear() + 1900, startDate.getMonth(), startDate.getDate(),
                Integer.valueOf(endDuty[0]), Integer.valueOf(endDuty[1]),
                Integer.valueOf(endDuty[2]));
          }
          Date endDutyDate;
          if (Integer.valueOf(startDuty[0]) > Integer.valueOf(endDuty[0])) {//tac dong dem
            Calendar startDutyCalCheck = Calendar.getInstance();
            startDutyCalCheck.clear();
            if (startDate != null) {
              startDutyCalCheck
                  .set(startDate.getYear() + 1900, startDate.getMonth(), startDate.getDate(),
                      0, 0, 0);
            }
            Date checkstartDutyDate = startDutyCalCheck.getTime();
            Calendar endDutyCalCheck = startDutyCalCheck;
            endDutyCalCheck.clear();
            if (endDate != null) {
              endDutyCalCheck.set(endDate.getYear() + 1900, endDate.getMonth(), endDate.getDate(),
                  0, 0, 0);
            }
            Date checkendDutyDate = endDutyCalCheck.getTime();//1445014800000 | 1445014800000
            if (startDate != null) {
              if (endDate != null && checkstartDutyDate.equals(checkendDutyDate)) {//Cung ngay
                if (endDate.getHours() <= (Integer.valueOf(endDuty[0])
                    + 1)) {//sang hom sau (00h-5h)
                  startDutyCal
                      .set(endDate.getYear() + 1900, endDate.getMonth(), endDate.getDate() - 1,
                          Integer.valueOf(startDuty[0]), Integer.valueOf(startDuty[1]),
                          Integer.valueOf(startDuty[2]));
                  startDutyDate = startDutyCal.getTime();
                  endDutyCal.set(endDate.getYear() + 1900, endDate.getMonth(), endDate.getDate(),
                      Integer.valueOf(endDuty[0]), Integer.valueOf(endDuty[1]),
                      Integer.valueOf(endDuty[2]));
//                            endDutyDate = endDutyCal.getTime();
                } else {//dem hom truoc (23h-24h)
                  startDutyCal
                      .set(startDate.getYear() + 1900, startDate.getMonth(), startDate.getDate(),
                          Integer.valueOf(startDuty[0]), Integer.valueOf(startDuty[1]),
                          Integer.valueOf(startDuty[2]));
                  startDutyDate = startDutyCal.getTime();
                  endDutyCal
                      .set(startDate.getYear() + 1900, startDate.getMonth(),
                          startDate.getDate() + 1,
                          Integer.valueOf(endDuty[0]), Integer.valueOf(endDuty[1]),
                          Integer.valueOf(endDuty[2]));
//                            endDutyDate = endDutyCal.getTime();
                }
              } else {
                endDutyCal
                    .set(startDate.getYear() + 1900, startDate.getMonth(), startDate.getDate() + 1,
                        Integer.valueOf(endDuty[0]), Integer.valueOf(endDuty[1]),
                        Integer.valueOf(endDuty[2]));
//                        endDutyDate = endDutyCal.getTime();
              }
            }
          }
//          endDutyCal.add(Calendar.MINUTE, 1);
          endDutyDate = endDutyCal.getTime();
          if (startDate != null && endDate != null) {
            if (startDate.compareTo(startDutyDate) < 0
                || endDate.compareTo(endDutyDate) > 0) {
              resultInSideDto
                  .setMessage(I18n.getValidation("cr.executionTime.fail")
                      .replace("{0}", dataCR.getDisplayStr()));
              return resultInSideDto;
            }
          }
        }
      }
    }
    return resultInSideDto = new ResultInSideDto(null, RESULT.SUCCESS, RESULT.SUCCESS);
  }

  private void converDate(SRCreateAutoCRDTO srCreateAutoCRDTO, Double offset) {
    srCreateAutoCRDTO.setWoFtStartTime(
        converClientDateToServerDate(srCreateAutoCRDTO.getWoFtStartTime(), offset));
    srCreateAutoCRDTO.setWoFtEndTime(
        converClientDateToServerDate(srCreateAutoCRDTO.getWoFtEndTime(), offset));
    srCreateAutoCRDTO.setWoTestStartTime(
        converClientDateToServerDate(srCreateAutoCRDTO.getWoTestStartTime(), offset));
    srCreateAutoCRDTO.setWoTestEndTime(
        converClientDateToServerDate(srCreateAutoCRDTO.getWoTestEndTime(), offset));
    srCreateAutoCRDTO.setExecutionTime(
        converClientDateToServerDate(srCreateAutoCRDTO.getExecutionTime(), offset));
    srCreateAutoCRDTO.setExecutionEndTime(
        converClientDateToServerDate(srCreateAutoCRDTO.getExecutionEndTime(), offset));
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
