package com.viettel.gnoc.mr.business;

import com.viettel.gnoc.commons.business.CatItemBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.proxy.CommonStreamServiceProxy;
import com.viettel.gnoc.commons.repository.GnocFileRepository;
import com.viettel.gnoc.commons.repository.UnitRepository;
import com.viettel.gnoc.commons.utils.CellConfigExport;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.ConfigFileExport;
import com.viettel.gnoc.commons.utils.ConfigHeaderExport;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.GNOC_FILE_BUSSINESS;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.maintenance.dto.MrCfgFileProcedureTelDTO;
import com.viettel.gnoc.maintenance.dto.MrCfgProcedureTelDTO;
import com.viettel.gnoc.maintenance.dto.MrDeviceDTO;
import com.viettel.gnoc.maintenance.dto.MrScheduleTelDTO;
import com.viettel.gnoc.maintenance.dto.MrScheduleTelHisDTO;
import com.viettel.gnoc.mr.repository.MrCfgFileProcedureTelRepository;
import com.viettel.gnoc.mr.repository.MrCfgProcedureTelRepository;
import com.viettel.gnoc.mr.repository.MrScheduleTelHisRepository;
import com.viettel.gnoc.mr.repository.MrScheduleTelRepository;
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
@Transactional
@Slf4j
public class MrCfgProcedureTelBusinessImpl implements MrCfgProcedureTelBusiness {

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
  MrCfgProcedureTelRepository mrCfgProcedureTelRepository;

  @Autowired
  CommonStreamServiceProxy commonStreamServiceProxy;

  @Autowired
  CatItemBusiness catItemBusiness;

  @Autowired
  UnitRepository unitRepository;

  @Autowired
  GnocFileRepository gnocFileRepository;

  @Autowired
  MrCfgFileProcedureTelRepository mrCfgFileProcedureTelRepository;

  @Autowired
  MrDeviceBusiness mrDeviceBusiness;

  @Autowired
  MrScheduleTelRepository mrScheduleTelRepository;

  @Autowired
  MrScheduleTelHisRepository mrScheduleTelHisRepository;

  @Override
  public Datatable onSearch(MrCfgProcedureTelDTO mrCfgProcedureTelDTO) {
    //BD Mem
    mrCfgProcedureTelDTO.setMrMode("S");

    if ("GenCrName".equalsIgnoreCase(mrCfgProcedureTelDTO.getSortName())) {
      mrCfgProcedureTelDTO.setSortName("GenCr");
    }

    return mrCfgProcedureTelRepository.onSearch(mrCfgProcedureTelDTO, "S");
  }

  @Override
  public File exportSearchData(MrCfgProcedureTelDTO mrCfgProcedureTelDTO) throws Exception {
    String[] header = new String[]{
        "marketName", "arrayCode", "networkType", "deviceType", "cycleType",
        "cycle", "mrMode", "procedureName", "genMrBefore", "mrContentId",
        "mrTime", "genCrName", "priorityCode", "reGenMrAfter", "impact",
        "status", "mrWorksName", "strExpDate", "descriptionCr", "typeCrName",
        "risk", "arrayActorName", "deviceTypeCRName", "subcategoryName", "priorityCrName",
        "levelEffectName", "dutyTypeName", "serviceEffectName", "arrayChildName"
    };

    String[] align = new String[]{
        "LEFT", "LEFT", "LEFT", "LEFT", "LEFT", "LEFT", "LEFT", "LEFT", "LEFT", "LEFT", "LEFT",
        "LEFT", "LEFT", "LEFT", "LEFT", "LEFT", "LEFT", "LEFT", "LEFT", "LEFT", "LEFT", "LEFT",
        "LEFT", "LEFT", "LEFT", "LEFT", "LEFT", "LEFT", "LEFT"
    };
    //BD Mem
    mrCfgProcedureTelDTO.setMrMode("S");
    List<MrCfgProcedureTelDTO> lstData = mrCfgProcedureTelRepository
        .onSearchExport(mrCfgProcedureTelDTO, "S");
    List<ConfigHeaderExport> lstHeaderSheet = renderHeaderSheet(header, align);
    return exportFileEx(lstData, lstHeaderSheet);
  }

  private File exportFileEx(List<MrCfgProcedureTelDTO> lstData,
      List<ConfigHeaderExport> lstHeaderSheet) throws Exception {
    String fileNameOut = "Procedure";
    ConfigFileExport configfileExport;
    Map<String, String> fieldSplit = new HashMap<>();
    CellConfigExport cellSheet;
    String rootPath = tempFolder + File.separator;
    String language = I18n.getLocale();
    String fileTemplate = "TEMPLATE_EXPORT_EN.xlsx";
    if (language != null && (language.toLowerCase().contains("vi") || language.toLowerCase()
        .contains("vi_VN"))) {
      fileTemplate = "TEMPLATE_EXPORT.xlsx";
    }

    configfileExport = new ConfigFileExport(
        lstData
        , "CfgTime"
        , I18n.getLanguage("cfgProcedureView.list.grid")
        , ""
        , 7
        , 4
        , lstHeaderSheet.size()
        , false
        , "language.cfgProcedureView.list.grid"
        , lstHeaderSheet
        , fieldSplit
        , ""
        , null
        , null
        , null
        , null
    );
    cellSheet = new CellConfigExport(7, 0, 0, 0, I18n.getLanguage("cfgProcedureView.list.grid.stt"),
        "HEAD", "STRING");

    configfileExport.setLangKey(I18n.getLocale());
    configfileExport.setIsAutoSize(true);
    List<CellConfigExport> lstCellSheet = new ArrayList<>();
    lstCellSheet.add(cellSheet);
    configfileExport.setLstCreatCell(lstCellSheet);
//    List<Integer> lsColumnHidden = new ArrayList<>();
//    lsColumnHidden.add(1);

    File fileExport = CommonExport.exportFileWithTemplateXLSX(
        "templates" + File.separator + fileTemplate
        , fileNameOut
        , configfileExport
        , rootPath,
        null,
        null,
        I18n.getLanguage("cfgProcedureView.list.grid")

    );
    return fileExport;
  }

  private List<ConfigHeaderExport> renderHeaderSheet(String[] col, String[] align) {
    List<ConfigHeaderExport> lstHeaderSheet = new ArrayList<>();
    for (int i = 0; i < col.length; i++) {
      lstHeaderSheet.add(new ConfigHeaderExport(col[i], align[i], false, 0, 0, new String[]{}, null,
          "STRING"));
    }
    return lstHeaderSheet;
  }

  @Override
  public ResultInSideDto onInsert(List<MultipartFile> fileAttachs,
      MrCfgProcedureTelDTO mrCfgProcedureTelDTO) throws Exception {
    log.debug("Request to insert : {}", mrCfgProcedureTelDTO);
    mrCfgProcedureTelDTO.setMrMode("S");
    UserToken userToken = TicketProvider.getUserToken();
    UnitDTO unitToken = unitRepository.findUnitById(userToken.getDeptId());
    ResultInSideDto dto = mrCfgProcedureTelRepository.insertOrUpdate(mrCfgProcedureTelDTO);
    List<GnocFileDto> gnocFileDtos = new ArrayList<>();
    if (fileAttachs != null && RESULT.SUCCESS.equals(dto.getKey())) {
      for (MultipartFile multipartFile : fileAttachs) {
        String fullPath = FileUtils
            .saveFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
                PassTranformer.decrypt(ftpPass), ftpFolder, multipartFile.getOriginalFilename(),
                multipartFile.getBytes(), null);
        String fullPathOld = FileUtils
            .saveUploadFile(multipartFile.getOriginalFilename(), multipartFile.getBytes(),
                uploadFolder, null);
        String fileName = multipartFile.getOriginalFilename();
        MrCfgFileProcedureTelDTO fileDTO = new MrCfgFileProcedureTelDTO();
        //Start save file old
        fileDTO.setFileName(FileUtils.getFileName(fullPathOld));
        fileDTO.setFilePath(FileUtils.getFilePath(fullPathOld));
        fileDTO
            .setProcedureId(dto.getId() == null ? null : Long.valueOf(dto.getId()));
        ResultInSideDto resultFileDataOld = mrCfgFileProcedureTelRepository.insertOrUpdate(fileDTO);
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
      gnocFileRepository
          .saveListGnocFileNotDeleteAll(GNOC_FILE_BUSSINESS.MR_CFG_PROCEDURE_TEL, dto.getId(),
              gnocFileDtos);
    }
    return dto;
  }

  @Override
  public ResultInSideDto onUpdate(List<MultipartFile> fileAttachs, MrCfgProcedureTelDTO dto)
      throws Exception {
    log.debug("Request to update : {}", dto);
    UserToken userToken = TicketProvider.getUserToken();
    UnitDTO unitToken = unitRepository.findUnitById(userToken.getDeptId());
    MrCfgProcedureTelDTO dtoUpdate = mrCfgProcedureTelRepository.getDetail(dto.getProcedureId());
    boolean checkUpdate = false;
    if (dto != null) {
      MrScheduleTelDTO dtoTel = new MrScheduleTelDTO();
      dtoTel.setProcedureId(dto.getProcedureId());
      dtoTel.setMrId(1L);
      dtoTel.setCrId(1L);
      // update type cycle ,cycle , sinh MR truoc ngay, thoi gian thuc hien -----> xóa lịch , update IsCompleteSoft = 1
      MrDeviceDTO deviceDTO = new MrDeviceDTO();
      deviceDTO.setMarketCode(dto.getMarketCode());
      deviceDTO.setArrayCode(dto.getArrayCode());
      deviceDTO.setDeviceType(dto.getDeviceType());
      deviceDTO.setNetworkType(dto.getNetworkType());
      deviceDTO.setMrSoft("1");

      List<MrScheduleTelDTO> lstScheduleTel = new ArrayList<>();
      List<MrScheduleTelDTO> lstScheduleTelDel = new ArrayList<>();
      if (dtoUpdate != null) {
        if (!String.valueOf(dto.getCycleType()).equals(String.valueOf(dtoUpdate.getCycleType()))
            || !String.valueOf(dto.getGenMrBefore())
            .equals(String.valueOf(dtoUpdate.getGenMrBefore())) || !String.valueOf(dto.getMrTime())
            .equals(String.valueOf(dtoUpdate.getMrTime()))) {
          lstScheduleTel = mrCfgProcedureTelRepository.getScheduleInProcedure(dtoTel);
          if (lstScheduleTel != null && !lstScheduleTel.isEmpty()) {
            for (MrScheduleTelDTO item : lstScheduleTel) {
              if (StringUtils.isStringNullOrEmpty(item.getMrId()) && StringUtils
                  .isStringNullOrEmpty(item.getCrId())) {
                lstScheduleTelDel.add(item);
              }
            }
          }

          List<MrDeviceDTO> result = new ArrayList<>();
          int rowStart = 0;
          int maxRow = 5000;
          while (true) {
            List<MrDeviceDTO> lstDTO = mrDeviceBusiness
                .onSearchEntity(deviceDTO, rowStart, maxRow, "asc", "nodeCode");
            if (lstDTO != null && !lstDTO.isEmpty()) {
              result.addAll(lstDTO);
              if (rowStart == 0) {
                rowStart++;
              }
              rowStart += maxRow;
            } else {
              break;
            }
          }

          // update MrDevice IsCompleteSoft  = 1
          List<MrDeviceDTO> resultUpdate = new ArrayList<>();
          if (lstScheduleTelDel != null && !lstScheduleTelDel.isEmpty()) {
            if (result != null && !result.isEmpty()) {
              int size = result.size();
              for (int i = size - 1; i > -1; i--) {
                for (MrScheduleTelDTO item : lstScheduleTelDel) {
                  if (item.getDeviceId().equals(result.get(i).getDeviceId())) {
                    result.get(i).setIsCompleteSoft(1L);
                    resultUpdate.add(result.get(i));
                    break;
                  }
                }
              }
            }
          }

          if (lstScheduleTelDel != null && !lstScheduleTelDel.isEmpty()) {
            checkUpdate = true;
            insertMrScheduleTelHis(lstScheduleTelDel, checkUpdate);
            mrScheduleTelRepository.deleteListSchedule(lstScheduleTelDel);
          }
          if (resultUpdate != null && !resultUpdate.isEmpty()) {
            mrDeviceBusiness.insertOrUpdateListDevice(resultUpdate);
          }
        }
      }

      //update thu tuc bao duong
      ResultInSideDto res = mrCfgProcedureTelRepository.insertOrUpdate(dto);

      List<GnocFileDto> gnocFileOldToDelete = mrCfgFileProcedureTelRepository
          .getCfgProcedureFileDetail(dto.getProcedureId(),
              GNOC_FILE_BUSSINESS.MR_CFG_PROCEDURE_TEL);

      //xu ly cap nhat file cu
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
          gnocFileRepository
              .deleteGnocFileByMapping(GNOC_FILE_BUSSINESS.MR_CFG_PROCEDURE_TEL, i.getMappingId());
          mrCfgFileProcedureTelRepository.delete(i.getMappingId());
        });
      }

      //xu ly them file moi
      if (fileAttachs != null && fileAttachs.size() > 0 && RESULT.SUCCESS.equals(res.getKey())) {
        List<GnocFileDto> gnocFileDtos = new ArrayList<>();
        for (MultipartFile multipartFile : fileAttachs) {
          String fullPath = FileUtils
              .saveFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
                  PassTranformer.decrypt(ftpPass), ftpFolder, multipartFile.getOriginalFilename(),
                  multipartFile.getBytes(), null);
          String fullPathOld = FileUtils
              .saveUploadFile(multipartFile.getOriginalFilename(), multipartFile.getBytes(),
                  uploadFolder, null);
          MrCfgFileProcedureTelDTO fileDTO = new MrCfgFileProcedureTelDTO();
          fileDTO.setFileName(FileUtils.getFileName(fullPathOld));
          fileDTO.setFilePath(FileUtils.getFilePath(fullPathOld));
          fileDTO.setProcedureId(dto.getProcedureId());
          ResultInSideDto resultInSideDto = mrCfgFileProcedureTelRepository.insertOrUpdate(fileDTO);
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
            .saveListGnocFileNotDeleteAll(GNOC_FILE_BUSSINESS.MR_CFG_PROCEDURE_TEL,
                dto.getProcedureId(), gnocFileDtos);
      }

      return res;
    }
    return new ResultInSideDto(null, RESULT.ERROR, RESULT.ERROR);
  }

  @Override
  public ResultInSideDto delete(Long procedureId) {
    log.debug("Request to delete : {}", procedureId);
    ResultInSideDto res = new ResultInSideDto(null, RESULT.ERROR, RESULT.ERROR);
    //xoa cau hinh
    MrScheduleTelDTO dtoTel = new MrScheduleTelDTO();
    dtoTel.setProcedureId(procedureId);
    List<MrScheduleTelDTO> lstScheduleTel = mrCfgProcedureTelRepository
        .getScheduleInProcedure(dtoTel);
    List<MrDeviceDTO> lstDevice = new ArrayList<>();
    MrDeviceDTO dtoDevice = new MrDeviceDTO();
    if (lstScheduleTel != null && !lstScheduleTel.isEmpty()) {
      for (MrScheduleTelDTO item : lstScheduleTel) {
        dtoDevice.setDeviceId(item.getDeviceId());
        MrDeviceDTO dtoOld = mrDeviceBusiness.getDetail(dtoDevice.getDeviceId());
        if (dtoOld != null && !StringUtils.isStringNullOrEmpty(dtoOld.getDeviceId())) {
          dtoOld.setIsCompleteSoft(1L);
          lstDevice.add(dtoOld);
        }
      }
    }
    if (lstDevice != null && !lstDevice.isEmpty()) {
      mrDeviceBusiness.insertOrUpdateListDevice(lstDevice);
    }

    if (procedureId != null) {
      res = mrCfgProcedureTelRepository.delete(procedureId);
      if (lstScheduleTel != null && !lstScheduleTel.isEmpty()) {
        mrScheduleTelRepository.deleteListSchedule(lstScheduleTel);
        insertMrScheduleTelHis(lstScheduleTel, false);
      }
      if (RESULT.SUCCESS.equals(res.getKey())) {
        gnocFileRepository.deleteGnocFile(GNOC_FILE_BUSSINESS.MR_CFG_PROCEDURE_TEL, procedureId);
        mrCfgFileProcedureTelRepository.deleteByCfgProcedureId(procedureId);
      }
    }

    return res;
  }

  @Override
  public MrCfgProcedureTelDTO getDetail(Long procedureId) {
    log.debug("Request to getDetail : {}", procedureId);
    MrCfgProcedureTelDTO dto = mrCfgProcedureTelRepository.getDetail(procedureId);
    if (procedureId != null && dto != null) {
      GnocFileDto gnocFileDto = new GnocFileDto();
      gnocFileDto.setBusinessId(procedureId);
      gnocFileDto.setBusinessCode(GNOC_FILE_BUSSINESS.MR_CFG_PROCEDURE_TEL);
      List<GnocFileDto> lst = gnocFileRepository.getListGnocFileByDto(gnocFileDto);
      if (lst != null && !lst.isEmpty()) {
        dto.setLstGnocFiles(lst);
      }
    }
    return dto;
  }

  private void insertMrScheduleTelHis(List<MrScheduleTelDTO> lstScheduleTel, boolean checkUpdate) {
    List<MrScheduleTelHisDTO> lstScheduleTelHis = new ArrayList<>();
    for (MrScheduleTelDTO item : lstScheduleTel) {
      MrScheduleTelHisDTO scheduleHisDTO = new MrScheduleTelHisDTO();
      scheduleHisDTO.setMarketCode(item.getMarketCode());
      scheduleHisDTO.setArrayCode(item.getArrayCode());
      scheduleHisDTO.setDeviceType(item.getDeviceType());
      scheduleHisDTO.setDeviceId(item.getDeviceId() == null ? null : item.getDeviceId().toString());
      scheduleHisDTO.setDeviceCode(item.getDeviceCode());
      scheduleHisDTO.setDeviceName(item.getDeviceName());
      scheduleHisDTO.setMrDate(item.getNextDateModify() == null ? null : String
          .valueOf(item.getNextDateModify()));
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
      lstScheduleTelHis.add(scheduleHisDTO);
    }

    mrScheduleTelHisRepository.insertUpdateListScheduleHis(lstScheduleTelHis);
  }

  private String genCrNumber(String crId, String sTypeCr, String sArrayActionName) {
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
}
