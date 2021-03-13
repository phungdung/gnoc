package com.viettel.gnoc.mr.business;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
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
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.maintenance.dto.MrCfgFileProcedureDTO;
import com.viettel.gnoc.maintenance.dto.MrITSoftProcedureDTO;
import com.viettel.gnoc.maintenance.dto.MrITSoftScheduleDTO;
import com.viettel.gnoc.maintenance.dto.MrScheduleITHisDTO;
import com.viettel.gnoc.maintenance.dto.MrSynItDevicesDTO;
import com.viettel.gnoc.mr.repository.MrITHisRepository;
import com.viettel.gnoc.mr.repository.MrITSoftFileProcedureRepository;
import com.viettel.gnoc.mr.repository.MrITSoftProcedureRepository;
import com.viettel.gnoc.mr.repository.MrITSoftScheduleRepository;
import com.viettel.security.PassTranformer;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
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
public class MrITSoftProcedureBusinessImpl implements MrITSoftProcedureBusiness {

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
  MrITSoftProcedureRepository mrITSoftProcedureRepository;

  @Autowired
  UnitRepository unitRepository;


  @Autowired
  GnocFileRepository gnocFileRepository;

  @Autowired
  MrITSoftScheduleBusiness mrITSoftScheduleBusiness;

  @Autowired
  MrSynItSoftDevicesBusiness mrSynItSoftDevicesBusiness;


  @Autowired
  MrITSoftScheduleRepository mrITSoftScheduleRepository;

  @Autowired
  MrITHisRepository mrITHisRepository;

  @Autowired
  MrITSoftFileProcedureRepository mrITSoftFileProcedureRepository;

  private final static String EXPORT_MR_IT_SOFT_PROCEDURE = "EXPORT_MR_IT_SOFT_PROCEDURE";

  @Override
  public Datatable getListMrSoftITProcedure(MrITSoftProcedureDTO mrITSoftProcedureDTO) {
    return mrITSoftProcedureRepository.getListMrSoftITProcedure(mrITSoftProcedureDTO);
  }

  @Override
  public MrITSoftProcedureDTO getDetail(Long procedureId) {
    log.debug("Request to getDetail : {}", procedureId);
    MrITSoftProcedureDTO dto = mrITSoftProcedureRepository.getDetail(procedureId);
    if (procedureId != null && dto != null) {
      dto.setLstGnocFiles(mrITSoftFileProcedureRepository
          .getCfgProcedureFileDetail(procedureId, GNOC_FILE_BUSSINESS.MR_CFG_PROCEDURE_IT_SOFT));
    }
    return dto;
  }


  @Override
  public ResultInSideDto onInsert(List<MultipartFile> fileAttachs,
      MrITSoftProcedureDTO mrITSoftProcedureDTO) throws Exception {
    log.debug("Request to insert : {}", mrITSoftProcedureDTO);
    UserToken userToken = TicketProvider.getUserToken();
    UnitDTO unitToken = unitRepository.findUnitById(userToken.getDeptId());
    ResultInSideDto dto = mrITSoftProcedureRepository.insertOrUpdate(mrITSoftProcedureDTO);
    List<GnocFileDto> gnocFileDtos = new ArrayList<>();
    boolean isUpdate = false;
    if (fileAttachs != null && RESULT.SUCCESS.equals(dto.getKey())) {
      for (MultipartFile multipartFile : fileAttachs) {
        Date date = new Date();
        String fullPath = FileUtils
            .saveFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
                PassTranformer.decrypt(ftpPass), ftpFolder, multipartFile.getOriginalFilename(),
                multipartFile.getBytes(), date);
        String fullPathOld = FileUtils
            .saveUploadFile(multipartFile.getOriginalFilename(), multipartFile.getBytes(),
                uploadFolder, date);

        //Start save file old
        MrCfgFileProcedureDTO fileDTO = new MrCfgFileProcedureDTO();
        fileDTO.setFileName(FileUtils.getFileName(fullPathOld));
        fileDTO.setFilePath(".." + File.separator +FileUtils.getFilePath(fullPathOld));
        fileDTO
            .setProcedureId(dto.getId() == null ? null : Long.valueOf(dto.getId()));
        ResultInSideDto resultFileDataOld = mrITSoftFileProcedureRepository
            .insertOrUpdateFiles(fileDTO);
        mrITSoftProcedureDTO.setProcedureId(Long.valueOf(dto.getId()));
        mrITSoftProcedureDTO.setAttachFileName(FileUtils.getFileName(fullPathOld));
        mrITSoftProcedureDTO.setAttachFilePath(FileUtils.getFilePath(fullPathOld));
        isUpdate = true;
        //End save file old
        GnocFileDto gnocFileDto = new GnocFileDto();
        gnocFileDto.setPath(fullPath);
        gnocFileDto.setFileName(FileUtils.getFileName(fullPathOld));
        gnocFileDto.setCreateUnitId(userToken.getDeptId());
        gnocFileDto.setCreateUnitName(unitToken.getUnitName());
        gnocFileDto.setCreateUserId(userToken.getUserID());
        gnocFileDto.setCreateUserName(userToken.getUserName());
        gnocFileDto.setCreateTime(new Date());
        gnocFileDto.setMappingId(resultFileDataOld.getId());
        gnocFileDtos.add(gnocFileDto);
      }
      gnocFileRepository
          .saveListGnocFileNotDeleteAll(GNOC_FILE_BUSSINESS.MR_CFG_PROCEDURE_IT_SOFT, dto.getId(),
              gnocFileDtos);
    }
    // cap nhat lai dto
    if (isUpdate) {
      dto = mrITSoftProcedureRepository.insertOrUpdate(mrITSoftProcedureDTO);
    }
    return dto;
  }

  @Override
  public ResultInSideDto onUpdate(List<MultipartFile> fileAttachs, MrITSoftProcedureDTO dto)
      throws Exception {
    log.debug("Request to update : {}", dto);
    UserToken userToken = TicketProvider.getUserToken();
    UnitDTO unitToken = unitRepository.findUnitById(userToken.getDeptId());
    MrITSoftProcedureDTO dtoUpdate = mrITSoftProcedureRepository.getDetail(dto.getProcedureId());
    boolean checkUpdate = false;
    if (dto != null) {
      MrITSoftScheduleDTO dtoTel = new MrITSoftScheduleDTO();
      dtoTel.setProcedureId(String.valueOf(dto.getProcedureId()));
      dtoTel.setMrId("1");
      dtoTel.setCrId("1");
      // update type cycle ,cycle , sinh MR truoc ngay, thoi gian thuc hien -----> xóa lịch , update IsCompleteSoft = 1
      MrSynItDevicesDTO deviceDTO = new MrSynItDevicesDTO();
      deviceDTO.setArrayCode(dto.getArrayCode());
      deviceDTO.setDeviceType(dto.getDeviceType());
      deviceDTO.setMrSoft("1");

      List<MrITSoftScheduleDTO> lstScheduleIT = new ArrayList<>();
      List<MrITSoftScheduleDTO> lstScheduleITDel = new ArrayList<>();
      if (dtoUpdate != null) {
        if (!String.valueOf(dto.getCycleType()).equals(String.valueOf(dtoUpdate.getCycleType()))
            || !String.valueOf(dto.getCycle())
            .equals(String.valueOf(dtoUpdate.getCycle()))
            || !String.valueOf(dto.getGenMrBefore())
            .equals(String.valueOf(dtoUpdate.getGenMrBefore())) || !String.valueOf(dto.getMrTime())
            .equals(String.valueOf(dtoUpdate.getMrTime()))) {
          lstScheduleIT = mrITSoftProcedureRepository.getScheduleInProcedureITSoft(dtoTel);
          List<MrSynItDevicesDTO> result = new ArrayList<>();
          if (lstScheduleIT != null && !lstScheduleIT.isEmpty()) {
            for (MrITSoftScheduleDTO item : lstScheduleIT) {
              if (StringUtils.isStringNullOrEmpty(item.getMrId()) && StringUtils
                  .isStringNullOrEmpty(item.getCrId())) {
                lstScheduleITDel.add(item);
                deviceDTO.setObjectId(item.getDeviceId());
                deviceDTO.setMarketCode(item.getMarketCode());
                List<MrSynItDevicesDTO> lstDTO = mrSynItSoftDevicesBusiness
                    .onSearchEntity(deviceDTO, 0, 5000, "asc", "nodeCode");
                result.addAll(lstDTO);
              }
            }
          }

          // update MrDevice IsCompleteSoft  = 1
          Map<String, MrSynItDevicesDTO> mapObjectId = new HashMap<>();
          if (result != null && !result.isEmpty()) {
            for (MrSynItDevicesDTO mrSynItDevicesDTO : result) {
              if (!mapObjectId.containsKey(mrSynItDevicesDTO.getObjectId())) {
                mapObjectId.put(mrSynItDevicesDTO.getObjectId(), mrSynItDevicesDTO);
              }
            }
          }
          List<MrSynItDevicesDTO> resultUpdate = new ArrayList<>();
          if (lstScheduleITDel != null && !lstScheduleITDel.isEmpty()) {
            for (MrITSoftScheduleDTO item : lstScheduleITDel) {
              if (mapObjectId.containsKey(item.getDeviceId())) {
                MrSynItDevicesDTO mrSynItDevicesDTO = mapObjectId.get(item.getDeviceId());
                mrSynItDevicesDTO.setIsCompleteSoft("1");
                resultUpdate.add(mrSynItDevicesDTO);
              }
            }
          }

          if (lstScheduleITDel != null && !lstScheduleITDel.isEmpty()) {
            checkUpdate = true;
            insertMrScheduleITHis(lstScheduleITDel, checkUpdate);
            mrITSoftScheduleRepository.deleteListSchedule(lstScheduleITDel);
          }
          if (resultUpdate != null && !resultUpdate.isEmpty()) {
            mrSynItSoftDevicesBusiness.insertOrUpdateListDevice(resultUpdate);
          }
        }
      }

      List<GnocFileDto> gnocFileOldToDelete = mrITSoftFileProcedureRepository
          .getCfgProcedureFileDetail(dto.getProcedureId(),
              GNOC_FILE_BUSSINESS.MR_CFG_PROCEDURE_IT_SOFT);
      if (dto.getLstGnocFiles() != null && dto.getLstGnocFiles().size() > 0) {
        List<Long> listIdFileIdNew = dto.getLstGnocFiles().stream()
            .map(GnocFileDto::getMappingId).collect(Collectors.toList());
        if (gnocFileOldToDelete != null && gnocFileOldToDelete.size() > 0) {
          gnocFileOldToDelete.removeIf(i -> (listIdFileIdNew.contains(i.getMappingId())));
        }
      }

      //delete file cu
      if (gnocFileOldToDelete != null && gnocFileOldToDelete.size() > 0) {
        gnocFileOldToDelete.forEach(i -> {
          gnocFileRepository.deleteGnocFileByMapping(GNOC_FILE_BUSSINESS.MR_CFG_PROCEDURE_IT_SOFT,
              i.getMappingId());
          mrITSoftFileProcedureRepository.delete(i.getMappingId());
          dto.setAttachFileName(null);
          dto.setAttachFilePath(null);
        });
      }

      //xu ly them file moi
      if (fileAttachs != null && fileAttachs.size() > 0) {
        List<GnocFileDto> gnocFileDtos = new ArrayList<>();
        for (MultipartFile multipartFile : fileAttachs) {
          Date date = new Date();
          String fullPath = FileUtils
              .saveFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
                  PassTranformer.decrypt(ftpPass), ftpFolder, multipartFile.getOriginalFilename(),
                  multipartFile.getBytes(), date);
          String fullPathOld = FileUtils
              .saveUploadFile(multipartFile.getOriginalFilename(), multipartFile.getBytes(),
                  uploadFolder, date);
          MrCfgFileProcedureDTO fileDTO = new MrCfgFileProcedureDTO();
          fileDTO.setFileName(FileUtils.getFileName(fullPathOld));
          fileDTO.setFilePath(".." + File.separator + FileUtils.getFilePath(fullPathOld));
          fileDTO.setProcedureId(dto.getProcedureId());
          ResultInSideDto resultInSideDto = mrITSoftFileProcedureRepository
              .insertOrUpdateFiles(fileDTO);
          dto.setAttachFileName(FileUtils.getFileName(fullPathOld));
          dto.setAttachFilePath(FileUtils.getFilePath(fullPathOld));
          if (RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
            //End save file old
            GnocFileDto gnocFileDto = new GnocFileDto();
            gnocFileDto.setPath(fullPath);
            gnocFileDto.setFileName(FileUtils.getFileName(fullPath));
            gnocFileDto.setCreateUnitId(userToken.getDeptId());
            gnocFileDto.setCreateUnitName(unitToken.getUnitName());
            gnocFileDto.setCreateUserId(userToken.getUserID());
            gnocFileDto.setCreateUserName(userToken.getUserName());
            gnocFileDto.setCreateTime(new Date());
            gnocFileDto.setMappingId(resultInSideDto.getId());
            gnocFileDtos.add(gnocFileDto);

          }
        }
        gnocFileRepository
            .saveListGnocFileNotDeleteAll(GNOC_FILE_BUSSINESS.MR_CFG_PROCEDURE_IT_SOFT,
                dto.getProcedureId(), gnocFileDtos);
      }
      //xu ly cap nhat file cu

      //update thu tuc bao duong
      ResultInSideDto res = mrITSoftProcedureRepository.insertOrUpdate(dto);

      return res;
    }
    return new ResultInSideDto(null, RESULT.ERROR, RESULT.ERROR);
  }

  @Override
  public ResultInSideDto delete(Long procedureId) {
    log.debug("Request to delete : {}", procedureId);
    ResultInSideDto res = new ResultInSideDto(null, RESULT.ERROR, RESULT.ERROR);

    //xoa cau hinh
    MrITSoftScheduleDTO dto = new MrITSoftScheduleDTO();
    dto.setProcedureId(String.valueOf(procedureId));
    List<MrSynItDevicesDTO> lstDevice = new ArrayList<>();
    MrSynItDevicesDTO dtoDevice = new MrSynItDevicesDTO();

    List<MrITSoftScheduleDTO> lstScheduleIT = mrITSoftProcedureRepository
        .getScheduleInProcedureITSoft(dto);
    if (lstScheduleIT != null && !lstScheduleIT.isEmpty()) {
      for (MrITSoftScheduleDTO item : lstScheduleIT) {
        dtoDevice.setObjectId(item.getDeviceId());
        dtoDevice.setDeviceType(item.getDeviceType());
//        if (item.getDeviceType().equals(Constants.MR_ITEM_NAME.MR_BGW)) {
//          dtoDevice.setMarketCode(item.getMarketCode());
//        }
        MrSynItDevicesDTO dtoOld = mrSynItSoftDevicesBusiness
            .findMrDeviceByObjectId(dtoDevice);
        if (dtoOld != null && !"".equals(dtoOld.getObjectId())) {
          dtoOld.setIsCompleteSoft("1");
          dtoOld.setId(item.getId());
          lstDevice.add(dtoOld);
        }
      }
    }
    if (lstDevice != null && !lstDevice.isEmpty()) {
      mrSynItSoftDevicesBusiness.insertOrUpdateListDevice(lstDevice);
    }

    if (procedureId != null) {
      res = mrITSoftProcedureRepository.delete(procedureId);
      if (lstScheduleIT != null && !lstScheduleIT.isEmpty()) {
        mrITSoftScheduleRepository.deleteListSchedule(lstScheduleIT);
        insertMrScheduleITHis(lstScheduleIT, false);
      }
      if (RESULT.SUCCESS.equals(res.getKey())) {
        gnocFileRepository
            .deleteGnocFile(GNOC_FILE_BUSSINESS.MR_CFG_PROCEDURE_IT_SOFT, procedureId);
        mrITSoftFileProcedureRepository.deleteByCfgProcedureId(procedureId);
      }

    }
    return res;
  }

  public void insertMrScheduleITHis(List<MrITSoftScheduleDTO> lstScheduleIT, boolean checkUpdate) {
    List<MrScheduleITHisDTO> lstScheduleITHis = new ArrayList<>();
    for (MrITSoftScheduleDTO item : lstScheduleIT) {
      MrScheduleITHisDTO scheduleHisDTO = new MrScheduleITHisDTO();
      scheduleHisDTO.setMarketCode(item.getMarketCode());
      scheduleHisDTO.setArrayCode(item.getArrayCode());
      scheduleHisDTO.setDeviceType(item.getDeviceType());
      scheduleHisDTO.setDeviceId(item.getDeviceId() == null ? null : item.getDeviceId().toString());
      scheduleHisDTO.setDeviceCode(item.getDeviceCode());
      scheduleHisDTO.setDeviceName(item.getDeviceName());
      scheduleHisDTO.setMrDate(item.getNextDateModify() == null ? null : item.getNextDateModify());
      scheduleHisDTO.setMrContent(item.getMrContentId());
      scheduleHisDTO.setMrMode("S");
      scheduleHisDTO.setMrType(item.getMrType());
      scheduleHisDTO.setMrId(item.getMrId() == null ? null : item.getMrId().toString());
      scheduleHisDTO.setMrCode(item.getMrCode());
      scheduleHisDTO.setCrId(item.getCrId() == null ? null : item.getCrId().toString());
      scheduleHisDTO.setCrNumber(
          genCrNumber(item.getCrId() == null ? null : item.getCrId().toString(), item.getTypeCr(),
              item.getArrayActionName()));
      scheduleHisDTO
          .setProcedureId(item.getProcedureId() == null ? null : item.getProcedureId().toString());
      scheduleHisDTO.setProcedureName(item.getProcedureName());
      scheduleHisDTO.setNetworkType(item.getNetworkType());
      scheduleHisDTO.setCycle(item.getCycle());
      scheduleHisDTO.setRegion(item.getRegion());
      if (checkUpdate) {
        scheduleHisDTO.setNote(I18n.getLanguage("mrCfgProcedure.update.note"));
      } else {
        scheduleHisDTO.setNote(I18n.getLanguage("mrCfgProcedure.delete.node"));
      }
      lstScheduleITHis.add(scheduleHisDTO);
    }

    mrITHisRepository.insertUpdateListScheduleHis(lstScheduleITHis);
  }

  public String genCrNumber(String crId, String sTypeCr, String sArrayActionName) {
    String crTypeLong;
    if (sTypeCr == null) {
      crTypeLong = Constants.CR_TYPE.STANDARD.toString();
    } else {
      crTypeLong = sTypeCr;
    }
    String crType = Constants.CR_TYPE.NORMAL.toString().equalsIgnoreCase(crTypeLong) ? "NORMAL"
        : Constants.CR_TYPE.EMERGENCY.toString().equalsIgnoreCase(crTypeLong) ? "EMERGENCY"
            : Constants.CR_TYPE.STANDARD.toString().equalsIgnoreCase(crTypeLong) ? "STANDARD" : "";
    String crNumber = "CR_"
        + crType + "_"
        + sArrayActionName + "_"
        + crId;
    return crNumber.toUpperCase(Locale.US);
  }

  @Override
  public File exportData(MrITSoftProcedureDTO mrITSoftProcedureDTO) throws Exception {
    List<MrITSoftProcedureDTO> mrITSoftProcedureDTOS = mrITSoftProcedureRepository
        .getDataExport(mrITSoftProcedureDTO);
    return exportFileTemplate(mrITSoftProcedureDTOS);
  }

  private File exportFileTemplate(List<MrITSoftProcedureDTO> dtoList)
      throws Exception {

    String fileNameOut;
    String subTitle;
    String sheetName = I18n.getLanguage("mrCfgProcedureITSoft.export.title");
    String title = I18n.getLanguage("mrCfgProcedureITSoft.export.title");
    List<ConfigFileExport> fileExportList = new ArrayList<>();
    ConfigHeaderExport columnSheet;
    List<ConfigHeaderExport> headerExportList = new ArrayList<>();
    columnSheet = new ConfigHeaderExport("marketName", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("arrayCode", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("cycleTypeName", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");

    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("cycle", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    headerExportList.add(columnSheet);

    columnSheet = new ConfigHeaderExport("procedureName", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    headerExportList.add(columnSheet);

    columnSheet = new ConfigHeaderExport("genMrBefore", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    headerExportList.add(columnSheet);

    columnSheet = new ConfigHeaderExport("deviceType", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    headerExportList.add(columnSheet);

    columnSheet = new ConfigHeaderExport("importantLevelName", "LEFT", false, 0, 0, new String[]{},
        null,
        "STRING");
    headerExportList.add(columnSheet);

    columnSheet = new ConfigHeaderExport("mrContentId", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    headerExportList.add(columnSheet);

    columnSheet = new ConfigHeaderExport("mrModeName", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    headerExportList.add(columnSheet);

    columnSheet = new ConfigHeaderExport("mrTime", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    headerExportList.add(columnSheet);

    columnSheet = new ConfigHeaderExport("mrWorks", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    headerExportList.add(columnSheet);

    columnSheet = new ConfigHeaderExport("typeCrName", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    headerExportList.add(columnSheet);

    columnSheet = new ConfigHeaderExport("arrayActionName", "LEFT", false, 0, 0, new String[]{},
        null,
        "STRING");
    headerExportList.add(columnSheet);

    columnSheet = new ConfigHeaderExport("deviceTypeCRName", "LEFT", false, 0, 0, new String[]{},
        null,
        "STRING");
    headerExportList.add(columnSheet);

    columnSheet = new ConfigHeaderExport("crName", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    headerExportList.add(columnSheet);

    columnSheet = new ConfigHeaderExport("priorityCrName", "LEFT", false, 0, 0, new String[]{},
        null,
        "STRING");
    headerExportList.add(columnSheet);

    columnSheet = new ConfigHeaderExport("genCrName", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("genWoName", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("levelEffectName", "LEFT", false, 0, 0, new String[]{},
        null,
        "STRING");
    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("dutyTypeName", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("serviceEffectName", "LEFT", false, 0, 0, new String[]{},
        null,
        "STRING");
    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("expDate", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("statusName", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    headerExportList.add(columnSheet);

    //kiểm tra đầu vào

    fileNameOut = EXPORT_MR_IT_SOFT_PROCEDURE;
    subTitle = I18n
        .getLanguage("mrCfgProcedureITSoft.export.exportDate", DateTimeUtils.convertDateOffset());
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
        , "language.mrCfgProcedureITSoft"
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


}
