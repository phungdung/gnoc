package com.viettel.gnoc.mr.business;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.proxy.WoServiceProxy;
import com.viettel.gnoc.commons.repository.GnocFileRepository;
import com.viettel.gnoc.commons.repository.UnitRepository;
import com.viettel.gnoc.commons.utils.CellConfigExport;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.ConfigFileExport;
import com.viettel.gnoc.commons.utils.ConfigHeaderExport;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.GNOC_FILE_BUSSINESS;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.DateUtil;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.maintenance.dto.MrUngCuuTTDTO;
import com.viettel.gnoc.maintenance.dto.MrUngCuuTTFilesDTO;
import com.viettel.gnoc.mr.repository.MrUngCuuTTRepository;
import com.viettel.gnoc.wo.dto.WoDTO;
import com.viettel.security.PassTranformer;
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
import org.springframework.web.multipart.MultipartFile;
import viettel.passport.client.UserToken;

@Service
@Slf4j
@Transactional
public class MrUngCuuTTBusinessImpl implements MrUngCuuTTBusiness {

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
  MrUngCuuTTRepository mrUngCuuTTRepository;

  @Autowired
  TicketProvider ticketProvider;

  @Autowired
  WoServiceProxy woServiceProxy;

  @Autowired
  GnocFileRepository gnocFileRepository;
  @Autowired
  UnitRepository unitRepository;

  private final static String MR_UCTT_RESULT_IMPORT = "MR_UCTT_RESULT_IMPORT";
  private final static String EXPORT_MR_UCTT = "EXPORT_MR_UCTT";

  @Override
  public Datatable getListMrUctt(MrUngCuuTTDTO mrUngCuuTTDTO) {
    Datatable listMrUctt = mrUngCuuTTRepository.getListMrUctt(mrUngCuuTTDTO);
    return listMrUctt;
  }

  @Override
  public ResultInSideDto insertMrUctt(List<MultipartFile> mrFileList, MrUngCuuTTDTO mrUngCuuTTDTO) {
    log.debug("Request to insert : {}", mrUngCuuTTDTO);
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    UserToken userToken = ticketProvider.getUserToken();
    List<WoDTO> lstWoDTOInsert = setDataInsertWo(mrUngCuuTTDTO, mrFileList);
    for (WoDTO woDTO : lstWoDTOInsert) {
      ResultDTO resWo = woServiceProxy.insertWoForSPMProxy(woDTO);
      if (RESULT.SUCCESS.equals(resWo.getMessage())) {
        mrUngCuuTTDTO.setCreatedDate(new Date());
        mrUngCuuTTDTO.setCreatedUser(userToken.getUserName());
        mrUngCuuTTDTO.setUpdatedDate(new Date());
        mrUngCuuTTDTO.setUpdatedUser(userToken.getUserName());

        woDTO.setWoCode(resWo.getId());
        String[] temps = resWo.getId().split("_");
        String id = temps[temps.length - 1];
        woDTO.setWoId(id);
        mrUngCuuTTDTO.setWoCode(woDTO.getWoCode());
        mrUngCuuTTDTO.setCdId(woDTO.getCdId());
        resultInSideDto = mrUngCuuTTRepository.insertMrUcttDTO(mrUngCuuTTDTO);

        if (RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
          UnitDTO unitToken = unitRepository.findUnitById(userToken.getDeptId());
          if (mrFileList != null && mrFileList.size() != 0) {
            List<GnocFileDto> gnocFileDtos = new ArrayList<>();
            try {
              for (MultipartFile multipartFile : mrFileList) {
                String fullPath = FileUtils
                    .saveFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
                        PassTranformer.decrypt(ftpPass), ftpFolder,
                        multipartFile.getOriginalFilename(), multipartFile.getBytes(), null);
                String fullPathOld = FileUtils
                    .saveUploadFile(multipartFile.getOriginalFilename(), multipartFile.getBytes(),
                        uploadFolder, null);
                String fileName = multipartFile.getOriginalFilename();
                //Start save file old
                MrUngCuuTTFilesDTO mrUngCuuTTFilesDTO = new MrUngCuuTTFilesDTO();
                mrUngCuuTTFilesDTO.setCreatedTime(new Date());
                mrUngCuuTTFilesDTO.setCreatedUser(userToken.getUserName());
                mrUngCuuTTFilesDTO.setUpdatedTime(new Date());
                mrUngCuuTTFilesDTO.setUpdatedUser(userToken.getUserName());
                mrUngCuuTTFilesDTO.setUcttId(String.valueOf(resultInSideDto.getId()));
                mrUngCuuTTFilesDTO.setFileName(FileUtils.getFileName(fullPathOld));
                mrUngCuuTTFilesDTO.setFilePath(FileUtils.getFilePath(fullPathOld));
                ResultInSideDto resultFileDataOld = mrUngCuuTTRepository
                    .insertMrUcttFilesDTO(mrUngCuuTTFilesDTO);
                //End save file old
                GnocFileDto gnocFileDto = new GnocFileDto();
                gnocFileDto.setPath(fullPath);
                gnocFileDto.setFileName(fileName);
                gnocFileDto.setCreateUnitId(userToken.getDeptId());
                gnocFileDto.setCreateUnitName(unitToken.getUnitName());
                gnocFileDto.setCreateUserId(userToken.getUserID());
                gnocFileDto.setCreateUserName(userToken.getUserName());
                gnocFileDto.setCreateTime(new Date());
                gnocFileDto.setMappingId(resultFileDataOld.getId());
                gnocFileDtos.add(gnocFileDto);
              }
            } catch (Exception e) {
              log.error(e.getMessage(), e);
            }
            gnocFileRepository
                .saveListGnocFileNotDeleteAll(GNOC_FILE_BUSSINESS.MR_UCTT,
                    resultInSideDto.getId(),
                    gnocFileDtos);
          }

        }
      } else {
        resultInSideDto.setKey(RESULT.ERROR);
        resultInSideDto.setMessage(I18n.getValidation("mrUCTT.createWo.fail"));
        return resultInSideDto;
      }
    }
    return resultInSideDto;
  }

  private List<WoDTO> setDataInsertWo(MrUngCuuTTDTO ucttDTO, List<MultipartFile> mrFileList) {
    List<WoDTO> lstWoDTO = new ArrayList<>();
    WoDTO woDTO = new WoDTO();
    try {
      String[] arrCD = ucttDTO.getCdId().split(",");
      for (String cdId : arrCD) {
        woDTO = new WoDTO();
        woDTO.setCdId(cdId.trim());
        String woCreateUser = mrUngCuuTTRepository
            .getConfigUCTTForCreateWo(Constants.MR_UCTT.MR_UCTT, Constants.MR_UCTT.WO_CREATE_USER);
        String woPriority = mrUngCuuTTRepository
            .getConfigUCTTForCreateWo(Constants.MR_UCTT.MR_UCTT, Constants.MR_UCTT.WO_PRIORITY);
        String woStatus = mrUngCuuTTRepository
            .getConfigUCTTForCreateWo(Constants.MR_UCTT.MR_UCTT, Constants.MR_UCTT.WO_STATUS);
        String woSystem = mrUngCuuTTRepository
            .getConfigUCTTForCreateWo(Constants.MR_UCTT.MR_UCTT, Constants.MR_UCTT.WO_SYSTEM);
        String woTypeId = mrUngCuuTTRepository
            .getConfigUCTTForCreateWo(Constants.MR_UCTT.MR_UCTT, Constants.MR_UCTT.WO_TYPE_ID);
        UserToken userToken = ticketProvider.getUserToken();
        woDTO.setCreatePersonId(
            woCreateUser == null ? userToken.getUserID().toString() : woCreateUser);
        woDTO.setPriorityId(woPriority == null ? "" : woPriority);
        woDTO.setStatus(woStatus == null ? "" : woStatus);
        woDTO.setWoSystem(woSystem == null ? "" : woSystem);
        woDTO.setWoTypeId(woTypeId == null ? "" : woTypeId);

        woDTO.setWoDescription(ucttDTO.getDescription());
        woDTO.setWoContent(ucttDTO.getTitle());
        woDTO.setStartTime(DateTimeUtils.convertDateTimeStampToString(ucttDTO.getStartDate()));
        woDTO.setEndTime(DateTimeUtils.convertDateTimeStampToString(ucttDTO.getEndDate()));
        woDTO.setCreateDate(DateUtil.date2ddMMyyyyHHMMss(new Date()));
        if (mrFileList != null && mrFileList.size() != 0) {
          List<String> fileNames = new ArrayList<>();
          List<byte[]> fileBytes = new ArrayList<>();
          for (MultipartFile item : mrFileList) {
            fileNames.add(item.getOriginalFilename());
            fileBytes.add(item.getBytes());
          }
          woDTO.setFileArr(fileBytes);
          woDTO.setListFileName(fileNames);
        }
        lstWoDTO.add(woDTO);
      }

    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return lstWoDTO;
  }

  @Override
  public File exportSearchData(MrUngCuuTTDTO mrUngCuuTTDTO) throws Exception {
    List<MrUngCuuTTDTO> mrUngCuuTTDTOList = mrUngCuuTTRepository
        .getDataExport(mrUngCuuTTDTO);
    return exportFileTemplate(mrUngCuuTTDTOList, "", "0");
  }

  private File exportFileTemplate(List<MrUngCuuTTDTO> dtoList, String key, String isImportError)
      throws Exception {

    String fileNameOut;
    String subTitle;
    String sheetName = I18n.getLanguage("mrUctt.export.title");
    String title = I18n.getLanguage("mrUctt.export.title");
    List<ConfigFileExport> fileExportList = new ArrayList<>();
    ConfigHeaderExport columnSheet;
    List<ConfigHeaderExport> headerExportList = readerHeaderSheet("cdName", "woCode", "title",
        "startDate", "endDate");
    //kiểm tra đầu vào
    if (Constants.RESULT_IMPORT.equals(key)) {
      fileNameOut = MR_UCTT_RESULT_IMPORT;
      subTitle = I18n
          .getLanguage("mrUctt.export.exportDate", DateTimeUtils.convertDateOffset());
      columnSheet = new ConfigHeaderExport("resultImport", "LEFT", false, 0, 0, new String[]{},
          null,
          "STRING");
      headerExportList.add(columnSheet);
    } else {
      fileNameOut = EXPORT_MR_UCTT;
      subTitle = I18n
          .getLanguage("mrUctt.export.exportDate", DateTimeUtils.convertDateOffset());
    }
    Map<String, String> fieldSplit = new HashMap<>();
    ConfigFileExport configFileExport = new ConfigFileExport(
        dtoList
        , sheetName
        , title
        , subTitle
        , 7
        , 3
        , 9
        , true
        , "language.mrUctt"
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

  private List<ConfigHeaderExport> readerHeaderSheet(String... col) {
    List<ConfigHeaderExport> lstHeaderSheet = new ArrayList<>();
    for (int i = 0; i < col.length; i++) {
      lstHeaderSheet.add(new ConfigHeaderExport(col[i], "LEFT", false, 0, 0, new String[]{}, null,
          "STRING"));
    }
    return lstHeaderSheet;
  }
}
