package com.viettel.gnoc.mr.business;

import com.viettel.gnoc.commons.business.CatLocationBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.proxy.CrServiceProxy;
import com.viettel.gnoc.commons.repository.CatItemRepository;
import com.viettel.gnoc.commons.utils.CellConfigExport;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.ConfigFileExport;
import com.viettel.gnoc.commons.utils.ConfigHeaderExport;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.APPLIED_BUSSINESS;
import com.viettel.gnoc.commons.utils.Constants.LANGUAGUE_EXCHANGE_SYSTEM;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.DateUtil;
import com.viettel.gnoc.commons.utils.ExcelWriterUtils;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.CrDTO;
import com.viettel.gnoc.cr.dto.CrHisDTO;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.maintenance.dto.MrCfgProcedureITHardDTO;
import com.viettel.gnoc.maintenance.dto.MrConfigDTO;
import com.viettel.gnoc.maintenance.dto.MrITHardScheduleDTO;
import com.viettel.gnoc.maintenance.dto.MrInsideDTO;
import com.viettel.gnoc.maintenance.dto.MrScheduleITHisDTO;
import com.viettel.gnoc.maintenance.dto.MrSynItDevicesDTO;
import com.viettel.gnoc.mr.repository.MrITHardScheduleRepository;
import com.viettel.gnoc.mr.repository.MrITHisRepository;
import com.viettel.gnoc.mr.repository.MrRepository;
import com.viettel.gnoc.mr.repository.MrScheduleTelRepository;
import com.viettel.gnoc.mr.repository.MrSynItHardDevicesRepository;
import com.viettel.gnoc.mr.repository.MrSynItSoftDevicesRepository;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;
import org.apache.poi.xssf.usermodel.XSSFDataValidationConstraint;
import org.apache.poi.xssf.usermodel.XSSFDataValidationHelper;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
public class MrITHardScheduleBusinessImpl implements MrITHardScheduleBusiness {

  @Autowired
  MrITHardScheduleRepository mrITHardScheduleRepository;

  @Autowired
  CatLocationBusiness catLocationBusiness;

  @Autowired
  MrITHisBusiness mrITHisBusiness;

  @Autowired
  MrScheduleTelRepository mrScheduleTelRepository;

  @Autowired
  MrSynItHardDevicesRepository mrSynItHardDevicesRepository;

  @Autowired
  TicketProvider ticketProvider;

  @Autowired
  CrServiceProxy crServiceProxy;

  @Autowired
  CatItemRepository catItemRepository;

  @Autowired
  MrITHardProcedureBusiness mrITHardProcedureBusiness;

  @Autowired
  MaintenanceMngtBusiness maintenanceMngtBusiness;

  @Autowired
  MrSynItSoftDevicesRepository mrSynItSoftDevicesRepository;

  @Autowired
  MrRepository mrRepository;

  @Autowired
  MrITHisRepository mrITHisRepository;

  @Value("${application.temp.folder}")
  private String tempFolder;

  private static final String MR_TYPE_HARD = "H";
  private final static String EXPORT_MR_IT_SOFT_SCHEDULE = "EXPORT_MR_IT_SOFT_SCHEDULE";
  private final static String MR_IT_SOFT_SCHEDULE_RESULT_IMPORT = "MR_IT_SOFT_SCHEDULE_RESULT_IMPORT";
  private final static String MR_IT_HARD_SCHEDULE_RESULT_IMPORT = "MR_SCHEDULE_IT_HARD_RESULT_IMPORT";
  private final static String MR_SCHEDULE_IT_HARD_EXPORT = "MR_SCHEDULE_IT_HARD_EXPORT";

  HashMap<String, String> mapMrConfirm = new HashMap<>();
  Map<Long, MrITHardScheduleDTO> mapDataImport = new HashMap<>();

  @Override
  public Datatable getListMrScheduleITHard(MrITHardScheduleDTO mrITHardScheduleDTO) {
    log.debug("Request to getListMrScheduleITHard: {}", mrITHardScheduleDTO);
    return mrITHardScheduleRepository.getListMrScheduleITHard(mrITHardScheduleDTO);
  }

  @Override
  public MrITHardScheduleDTO getDetail(Long scheduleId) {
    log.debug("Request to getDetail : {}", scheduleId);
    return mrITHardScheduleRepository.getDetail(scheduleId);
  }

  @Override
  public ResultInSideDto deleteMrScheduleITHard(Long mrScheduleId) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.ERROR);
    boolean checkDel = false;
    MrITHardScheduleDTO mrITHardScheduleDTO = getDetail(mrScheduleId);
    if (mrITHardScheduleDTO != null) {
      if (StringUtils.isStringNullOrEmpty(mrITHardScheduleDTO.getMrId()) && StringUtils
          .isStringNullOrEmpty(mrITHardScheduleDTO.getWoId())) {
        checkDel = true;
      }
      MrSynItDevicesDTO mrSynItDev = new MrSynItDevicesDTO();
      mrSynItDev.setObjectId(mrITHardScheduleDTO.getDeviceId());
      mrSynItDev.setDeviceType(mrITHardScheduleDTO.getDeviceType());
      MrSynItDevicesDTO mrSynItDevicesDTO = mrSynItSoftDevicesRepository
          .findMrDeviceByObjectId(mrSynItDev);
      if (mrSynItDevicesDTO != null) {
        if ("M".equals(mrITHardScheduleDTO.getCycleType())) {
          if ("1".equals(String.valueOf(mrITHardScheduleDTO.getCycle()))) {
            mrSynItDevicesDTO.setIsComplete1m("1");
          } else if ("3".equals(String.valueOf(mrITHardScheduleDTO.getCycle()))) {
            mrSynItDevicesDTO.setIsComplete3m("1");
          } else if ("6".equals(String.valueOf(mrITHardScheduleDTO.getCycle()))) {
            mrSynItDevicesDTO.setIsComplete6m("1");
          } else if ("12".equals(String.valueOf(mrITHardScheduleDTO.getCycle()))) {
            mrSynItDevicesDTO.setIsComplete12m("1");
          }
        }
        if (checkDel) {
          resultInSideDto = mrITHardScheduleRepository
              .deleteMrScheduleITHard(mrITHardScheduleDTO.getScheduleId());
          insertMrScheduleTelHis(mrITHardScheduleDTO);
          mrSynItHardDevicesRepository.updateMrSynItDevice(mrSynItDevicesDTO);
        }
      }
      if (RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
        resultInSideDto.setMessage(I18n.getValidation("common.delete.success"));
      } else {
        resultInSideDto.setKey(RESULT.ERROR);
        if (!checkDel) {
          resultInSideDto.setMessage(I18n.getValidation("mrScheduleTel.del.MrWo"));
        } else {
          resultInSideDto.setMessage(I18n.getValidation("common.delete.fail"));
        }
      }
    }
    return resultInSideDto;
  }

  @Override
  public File exportData(MrITHardScheduleDTO mrITHardScheduleDTO) throws Exception {
    log.debug("Request to exportData: {}", mrITHardScheduleDTO);
    List<MrITHardScheduleDTO> lstExport = mrITHardScheduleRepository
        .getListDataExport(mrITHardScheduleDTO);
    return exportFileEx(lstExport, "EXPORT_DATA", "H");
  }

  public File exportFileEx(List<MrITHardScheduleDTO> lstData, String key, String type)
      throws Exception {
    String title = "";
    String fileNameOut = "";
    String subTitle = I18n
        .getLanguage("mtItSoftSchedule.export.exportDate", DateTimeUtils.convertDateOffset());
    List<ConfigFileExport> fileExports = new ArrayList<>();
    List<ConfigHeaderExport> lstHeaderSheet = new ArrayList<>();
    if ("H".equals(type)) {
      if (Constants.RESULT_IMPORT.equals(key)) {
        title = I18n.getLanguage("mtItSoftSchedule.import.title.hard");
        fileNameOut = MR_IT_HARD_SCHEDULE_RESULT_IMPORT;
        lstHeaderSheet = readerHeaderSheet("scheduleId", "nationName",
            "region", "arrayCode", "deviceType", "deviceCode", "deviceName",
            "ipNode", "cdIdHardStr", "mrId", "woId", "cycle", "cycleType", "lastDate",
            "nextDate", "nextDateModify", "woContent", "vendor", "updatedDate", "note",
            "mrConfirmName", "resultImport");
      } else {
        title = I18n.getLanguage("mtItSoftSchedule.export.title.hard");
        fileNameOut = MR_SCHEDULE_IT_HARD_EXPORT;
        lstHeaderSheet = readerHeaderSheet("scheduleId", "nationName",
            "region", "arrayCode", "deviceType", "deviceCode", "deviceName",
            "ipNode", "cdIdHardStr", "mrCode", "woCode", "cycle", "cycleType",
            "lastDate", "nextDate", "nextDateModify", "woContent",
            "vendor", "updatedDate", "note", "mrConfirmName");
      }
    } else {
      if (Constants.RESULT_IMPORT.equals(key)) {
        title = I18n.getLanguage("mtItSoftSchedule.import.title.soft");
        fileNameOut = MR_IT_SOFT_SCHEDULE_RESULT_IMPORT;
        lstHeaderSheet = readerHeaderSheet("scheduleId", "nationName",
            "region", "arrayCode", "deviceType", "deviceCode", "deviceName",
            "ipNode",
            "vendor", "bdType", "stationCode", "nodeAffected", "groupCode", "nextDate",
            "lastDate",
            "mrId", "crId", "wlgText", "descriptionCr", "implementUnitName",
            "checkingUnitName",
            "cycle", "cycleType", "note", "ud", "boUnit", "approveStatus", "nextDateModify",
            "mrConfirmName", "resultImport");
      } else {
        title = I18n.getLanguage("mtItSoftSchedule.export.title.soft");
        fileNameOut = EXPORT_MR_IT_SOFT_SCHEDULE;
        lstHeaderSheet = readerHeaderSheet("scheduleId", "marketName",
            "region", "arrayCode", "deviceType", "deviceCode", "deviceName",
            "ipNode",
            "vendor", "bdType", "stationCode", "nodeAffected", "groupCode", "nextDate",
            "lastDate",
            "mrId", "crId", "wlgText", "descriptionCr", "implementUnitName",
            "checkingUnitName",
            "cycle", "cycleType", "note", "ud", "boUnit", "approveStatus", "nextDateModify",
            "mrConfirmName");
      }
    }
    Map<String, String> filedSplit = new HashMap<>();
    ConfigFileExport configFileExport = new ConfigFileExport(
        lstData,
        title,
        title,
        subTitle,
        7,
        3,
        7,
        true,
        "language.mtItSoftSchedule",
        lstHeaderSheet,
        filedSplit,
        "",
        I18n.getLanguage("common.export.firstLeftHeader")
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
    fileExports.add(configFileExport);
    String fileTemplate = "templates" + File.separator
        + "TEMPLATE_EXPORT.xlsx";
    String rootPath = tempFolder + File.separator;
    File fileExport = CommonExport.exportExcel(
        fileTemplate
        , fileNameOut
        , fileExports
        , rootPath
        , new String[]{}
    );
    return fileExport;
  }

  public List<ConfigHeaderExport> readerHeaderSheet(String... col) {
    List<ConfigHeaderExport> lstHeaderSheet = new ArrayList<>();
    for (int i = 0; i < col.length; i++) {
      lstHeaderSheet.add(new ConfigHeaderExport(col[i], "LEFT", false, 0, 0, new String[]{}, null,
          "STRING"));
    }
    return lstHeaderSheet;
  }

  @Override
  public ResultInSideDto onUpdateHard(MrITHardScheduleDTO mrITHardScheduleDTO) throws Exception {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.ERROR);
    MrSynItDevicesDTO dtoDevice = new MrSynItDevicesDTO();
    dtoDevice.setObjectId(mrITHardScheduleDTO.getObjectId() == null ? null
        : String.valueOf(mrITHardScheduleDTO.getObjectId()));
    dtoDevice.setDeviceType(mrITHardScheduleDTO.getDeviceType());
    dtoDevice = mrSynItHardDevicesRepository.findMrDeviceByObjectId(dtoDevice);
    //Khi cập nhật từ Bảo dưỡng về Không bảo dưỡng
    if (dtoDevice != null) {
      if (!StringUtils.isStringNullOrEmpty(mrITHardScheduleDTO.getMrConfirmHard())) {
        //lý do bảo dưỡng khác Nâng cấp
        List<Long> lstMrId = new ArrayList<>();
        List<MrScheduleITHisDTO> lstScheduleHis = new ArrayList<>();
        List<MrITHardScheduleDTO> lstDeleteSchedule = new ArrayList<>();
        String lastUpdate = "";
        if (!"5".equals(mrITHardScheduleDTO.getMrConfirmHard())) {
          List<MrScheduleITHisDTO> lst = mrITHardScheduleRepository
              .getListScheduleMoveToHis(dtoDevice);
          if (lst != null && !lst.isEmpty()) {
            lastUpdate = lst.get(0).getMrDate();
            //move lich sang lich su
            for (MrScheduleITHisDTO mrScheduleITHisDTO : lst) {
              mrScheduleITHisDTO.setNote(I18n.getLanguage("mtItSoftSchedule.list.noteUpdate"));
              mrScheduleITHisDTO.setMrMode("H");
              mrScheduleITHisDTO.setTitle(mrITHardScheduleDTO.getNote());
              mrScheduleITHisDTO.setRegion(mrITHardScheduleDTO.getRegion());
              MrCfgProcedureITHardDTO procedureItDTO = mrITHardProcedureBusiness
                  .getDetail(Long.valueOf(mrScheduleITHisDTO.getProcedureId()));
//              mrScheduleITHisDTO.setCrNumber(
//                  genCrNumber(mrScheduleITHisDTO.getCrId(), procedureItDTO.getTypeCr().toString(),
//                      procedureItDTO.getArrayActionName()));
              MrITHardScheduleDTO schedule = new MrITHardScheduleDTO();
              schedule.setScheduleId(mrScheduleITHisDTO.getScheduleId());
              lstDeleteSchedule.add(schedule);
              lstScheduleHis.add(mrScheduleITHisDTO);
            }
            resultInSideDto = mrITHisBusiness.insertList(lstScheduleHis);
            if (!RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
              return resultInSideDto;
            }
            //xoa lich hien tai
            if (!lstDeleteSchedule.isEmpty()) {
              resultInSideDto = deleteMrScheduleITSoft(
                  Long.valueOf(lstDeleteSchedule.get(0).getScheduleId()));
              if (!RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
                return resultInSideDto;
              }
            }
          }
          //cap nhat thiet bi
          dtoDevice.setIsCompleteSoft("1");
          dtoDevice.setMrSoft("0");
          dtoDevice.setMrConfirmHard(mrITHardScheduleDTO.getMrConfirmHard());
          dtoDevice.setNodeAffected(mrITHardScheduleDTO.getNodeAffected());
          dtoDevice.setLastDateSoft(
              "".equals(lastUpdate) ? null : DateTimeUtils.convertStringToDate(lastUpdate));
          try {
            resultInSideDto = mrSynItHardDevicesRepository.updateMrSynItDevice(dtoDevice);
            if (!RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
              return resultInSideDto;
            }
          } catch (Exception e) {
            throw new Exception(e.getCause().getMessage());
          }
          // end lý do bảo dưỡng khác Nâng cấp
        } else {
          // Khi li do bao duong = 5
          MrITHardScheduleDTO mrScheduleITModel = getDetail(mrITHardScheduleDTO.getScheduleId());
          List<CrHisDTO> crHisDtos = mrScheduleTelRepository
              .checkExistCrId(String.valueOf(mrITHardScheduleDTO.getCrId()),
                  String.valueOf(mrScheduleITModel.getCrId()));
          CrDTO crDTO =
              crServiceProxy.findCrById(Long.valueOf(mrITHardScheduleDTO.getCrId()));
          //Nếu lý do bảo dưỡng = Nâng cấp, CR vừa điền có trạng thái Đóng
          if (!StringUtils.isStringNullOrEmpty(crDTO.getState())) {
            if ("9".equals(crDTO.getState())) {
              if (crHisDtos != null && crHisDtos.size() > 0) {
                //MR  khác null=>>set mã MR, CR của record tương ứng về null
                if (!StringUtils.isStringNullOrEmpty(mrITHardScheduleDTO.getMrId())) {
                  mrScheduleITModel.setRegion(mrITHardScheduleDTO.getRegion());
                  mrScheduleITModel.setNote(mrITHardScheduleDTO.getNote());
                  mrScheduleITModel.setMrId(null);
                  mrScheduleITModel.setCrId(null);
                  mrScheduleITModel.setNextDateModify(mrITHardScheduleDTO.getNextDateModify());
                  try {
                    resultInSideDto = mrITHardScheduleRepository
                        .updateMrSchedule(mrScheduleITModel);
                    if (!RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
                      return resultInSideDto;
                    }
                  } catch (Exception e) {
                    throw new Exception(e.getCause().getMessage());
                  }
                  //update node tac dong, don vi xac nhan BO
                  dtoDevice.setNodeAffected(mrITHardScheduleDTO.getNodeAffected());
                  dtoDevice.setMrConfirmHard(mrITHardScheduleDTO.getMrConfirmHard());
                  try {
                    resultInSideDto = mrSynItHardDevicesRepository.updateMrSynItDevice(dtoDevice);
                    if (!RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
                      return resultInSideDto;
                    }
                  } catch (Exception e) {
                    throw new Exception(e.getCause().getMessage());
                  }
                } else {
                  //Nếu lý do bảo dưỡng = Nâng cấp, CR vừa điền có trạng thái Đóng,MR = null
                  List<MrScheduleITHisDTO> lst = mrITHardScheduleRepository
                      .getListScheduleMoveToHis(dtoDevice);
                  if (lst != null && !lst.isEmpty()) {
                    lastUpdate = lst.get(0).getMrDate();
                    //move lich sang lich su
                    for (MrScheduleITHisDTO mrScheduleITHisDTO : lst) {

                      mrScheduleITHisDTO
                          .setNote(I18n.getLanguage("mtItSoftSchedule.list.noteUpdate"));
                      mrScheduleITHisDTO.setMrMode("S");
                      mrScheduleITHisDTO.setMrId(null);
                      mrScheduleITHisDTO.setCrId(String.valueOf(mrITHardScheduleDTO.getCrId()));
                      mrScheduleITHisDTO.setTitle(mrITHardScheduleDTO.getNote());
                      mrScheduleITHisDTO.setRegion(mrITHardScheduleDTO.getRegion());
                      MrCfgProcedureITHardDTO procedureItDTO = mrITHardProcedureBusiness
                          .getDetail(Long.valueOf(mrScheduleITHisDTO.getProcedureId()));
                      mrScheduleITHisDTO.setCrNumber(genCrNumber(mrScheduleITHisDTO.getCrId(),
                          procedureItDTO.getTypeCr().toString(),
                          procedureItDTO.getArrayActionName()));
                      MrITHardScheduleDTO schedule = new MrITHardScheduleDTO();
                      schedule.setScheduleId(mrScheduleITHisDTO.getScheduleId());
                      lstDeleteSchedule.add(schedule);

                      lstScheduleHis.add(mrScheduleITHisDTO);
                    }
                    resultInSideDto = mrITHisBusiness.insertList(lstScheduleHis);
                    if (!RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
                      return resultInSideDto;
                    }
                    //xoa lich hien tai
                    if (!lstDeleteSchedule.isEmpty()) {
                      resultInSideDto = deleteMrScheduleITSoft(
                          Long.valueOf(lstDeleteSchedule.get(0).getScheduleId()));
                      if (!RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
                        return resultInSideDto;
                      }
                    }
                  }
                  //cap nhat thiet bi
                  dtoDevice.setIsCompleteSoft("1");
                  dtoDevice.setMrSoft("1");
                  dtoDevice.setMrConfirmHard(null);
                  dtoDevice.setNodeAffected(mrITHardScheduleDTO.getNodeAffected());
                  dtoDevice.setLastDateSoft(
                      "".equals(lastUpdate) ? null : DateTimeUtils.convertStringToDate(lastUpdate));
                  try {
                    resultInSideDto = mrSynItHardDevicesRepository.updateMrSynItDevice(dtoDevice);
                    if (!RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
                      return resultInSideDto;
                    }
                  } catch (Exception e) {
                    throw new Exception(e.getCause().getMessage());
                  }
                  //Thời gian bắt đầu kết thúc MR = thời gian bắt đầu kết thúc CR lấy trong CR_HIS
                  if (!StringUtils.isStringNullOrEmpty(mrScheduleITModel.getMrId())) {
                    MrInsideDTO mrDTO = maintenanceMngtBusiness
                        .findById(Long.valueOf(mrScheduleITModel.getMrId()));
                    mrDTO.setEarliestTime(
                        DateTimeUtils.convertStringToDate(crHisDtos.get(0).getEarliestStartTime()));
                    mrDTO.setLastestTime(
                        DateTimeUtils.convertStringToDate(crHisDtos.get(0).getLatestEndTime()));
                    try {
                      resultInSideDto = mrITHardScheduleRepository.updateMrEarAndLastestTime(mrDTO);
                      if (!RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
                        return resultInSideDto;
                      }
                    } catch (Exception e) {
                      throw new Exception(e.getCause().getMessage());
                    }
                  }
                }
              }
              // end CR ở trạng thái đóng
            } else {
              // CR khác đóng
              MrITHardScheduleDTO mrScheuleITUpdate = getDetail(
                  mrITHardScheduleDTO.getScheduleId());
              if (StringUtils.isStringNullOrEmpty(mrITHardScheduleDTO.getMrId())) {
                mrScheuleITUpdate.setMrId(null);
                mrScheuleITUpdate.setCrId(null);
              }
              mrScheuleITUpdate.setRegion(mrITHardScheduleDTO.getRegion());
              mrScheuleITUpdate.setNote(mrITHardScheduleDTO.getNote());
              mrScheuleITUpdate
                  .setNextDateModify(mrITHardScheduleDTO.getNextDateModify());
              try {
                resultInSideDto = mrITHardScheduleRepository.updateMrSchedule(mrScheuleITUpdate);
                if (!RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
                  return resultInSideDto;
                }
              } catch (Exception e) {
                throw new Exception(e.getCause().getMessage());
              }

              //Cap nhat lai Ngay du kiến bảo dưỡng cho các lịch cùng mã nhóm
              if (StringUtils.isStringNullOrEmpty(mrITHardScheduleDTO.getMrId()) && !StringUtils
                  .isStringNullOrEmpty(mrScheduleITModel.getGroupCode())) {
                List<MrITHardScheduleDTO> lstMrScheduleITGroupCode = mrITHardScheduleRepository
                    .findListMrScheduleByGroupCode(mrScheduleITModel.getGroupCode());
                for (MrITHardScheduleDTO mrScheduleIT : lstMrScheduleITGroupCode) {
                  MrITHardScheduleDTO mrScheduleITUpdates = getDetail(mrScheduleIT.getScheduleId());
                  mrScheduleITUpdates
                      .setNextDateModify(mrITHardScheduleDTO.getNextDateModify());
                  try {
                    resultInSideDto = mrITHardScheduleRepository
                        .updateMrSchedule(mrScheuleITUpdate);
                    if (!RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
                      return resultInSideDto;
                    }
                  } catch (Exception e) {
                    throw new Exception(e.getCause().getMessage());
                  }
                }
              }
              //update node tac dong
              dtoDevice.setNodeAffected(mrITHardScheduleDTO.getNodeAffected());
              dtoDevice.setMrConfirmHard(mrITHardScheduleDTO.getMrConfirmHard());
              dtoDevice.setNodeAffected(mrITHardScheduleDTO.getNodeAffected());
              try {
                resultInSideDto = mrSynItHardDevicesRepository.updateMrSynItDevice(dtoDevice);
                if (!RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
                  return resultInSideDto;
                }
              } catch (Exception e) {
                throw new Exception(e.getCause().getMessage());
              }
            }
          }
        }
        //end li do bao duong = 5
      } else {
        MrITHardScheduleDTO mrScheduleITDTO = getDetail(mrITHardScheduleDTO.getScheduleId());
        if (StringUtils.isStringNullOrEmpty(mrITHardScheduleDTO.getMrId())) {
          mrScheduleITDTO.setMrId(null);
          mrScheduleITDTO.setWoId(null);
        }
        mrScheduleITDTO.setRegion(mrITHardScheduleDTO.getRegion());
        mrScheduleITDTO.setNote(mrITHardScheduleDTO.getNote());
        mrScheduleITDTO.setNextDateModify(mrITHardScheduleDTO.getNextDateModify());
        try {
          mrITHardScheduleRepository.updateMrSchedule(mrScheduleITDTO);
        } catch (Exception e) {
          throw new Exception(e.getCause().getMessage());
        }

        //Cap nhat lai Ngay du kiến bảo dưỡng cho các lịch cùng mã nhóm
        if (StringUtils.isStringNullOrEmpty(mrITHardScheduleDTO.getMrId()) && !StringUtils
            .isStringNullOrEmpty(mrScheduleITDTO.getGroupCode())) {
          List<MrITHardScheduleDTO> lstMrScheduleITGroupCode = mrITHardScheduleRepository
              .findListMrScheduleByGroupCode(mrScheduleITDTO.getGroupCode());
          for (MrITHardScheduleDTO mrScheduleIT : lstMrScheduleITGroupCode) {
            MrITHardScheduleDTO mrScheuleITUpdate = getDetail(mrScheduleIT.getScheduleId());
            mrScheuleITUpdate.setNextDateModify(mrITHardScheduleDTO.getNextDateModify());
            try {
              mrITHardScheduleRepository.updateMrSchedule(mrScheuleITUpdate);
            } catch (Exception e) {
              throw new Exception(e.getCause().getMessage());
            }
          }
        }
        //update node tac dong
        dtoDevice.setNodeAffected(mrITHardScheduleDTO.getNodeAffected());
        dtoDevice.setMrConfirmHard(mrITHardScheduleDTO.getMrConfirmHard());
        dtoDevice.setNodeAffected(mrITHardScheduleDTO.getNodeAffected());
        try {
          resultInSideDto = mrSynItHardDevicesRepository.updateMrSynItDevice(dtoDevice);
          if (!RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
            return resultInSideDto;
          }
        } catch (Exception e) {
          throw new Exception(e.getCause().getMessage());
        }
      }
    }
    resultInSideDto.setKey(RESULT.SUCCESS);
    return resultInSideDto;
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

  @Override
  public ResultInSideDto deleteMrScheduleITSoft(Long mrScheduleId) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.ERROR);
    MrITHardScheduleDTO mrITHardScheduleDTO = getDetail(mrScheduleId);
    List<MrSynItDevicesDTO> lstDevice = new ArrayList<>();
    List<MrITHardScheduleDTO> mrITHardScheduleDTOList = new ArrayList<>();
    if (StringUtils.isStringNullOrEmpty(mrITHardScheduleDTO.getMrId()) && StringUtils
        .isStringNullOrEmpty(mrITHardScheduleDTO.getCrId())) {
      mrITHardScheduleDTOList.add(mrITHardScheduleDTO);
      resultInSideDto = mrITHardScheduleRepository.deleteListSchedule(mrITHardScheduleDTOList);
      MrSynItDevicesDTO dtoDevice = new MrSynItDevicesDTO();
      dtoDevice.setObjectId(mrITHardScheduleDTO.getDeviceId());
      dtoDevice.setMarketCode(mrITHardScheduleDTO.getMarketCode());
      dtoDevice.setDeviceType(mrITHardScheduleDTO.getDeviceType());
      dtoDevice = mrSynItHardDevicesRepository.findMrDeviceByObjectId(dtoDevice);
      dtoDevice.setIsCompleteSoft("1");
      lstDevice.add(dtoDevice);
      mrSynItHardDevicesRepository.updateList(lstDevice);
      return resultInSideDto;
    }
    return null;

  }

  @Override
  public File getTemplate(String type) throws Exception {
    ExcelWriterUtils excelWriterUtils = new ExcelWriterUtils();
    String templatePathOut = "templates" + File.separator + "TEMPLATE_EXPORT.xlsx";
    templatePathOut = StringUtils.removeSeparator(templatePathOut);
    Resource resource = new ClassPathResource(templatePathOut);
    InputStream fileTemplate = resource.getInputStream();

    //apache POI XSSF
    XSSFWorkbook workbook = new XSSFWorkbook(fileTemplate);
    XSSFSheet sheetOne = workbook.getSheetAt(0);
    XSSFSheet sheetParam = workbook.createSheet("param");
    XSSFSheet sheetParam1 = workbook.createSheet("param1");
    XSSFSheet sheetParam2 = workbook.createSheet("param2");
    //Tạo 1 mảng lưu header từng cột
    String[] header;
    //Tiêu đề đánh dấu *
    String[] headerStar;
    header = getHeaderHard();
    //Tiêu đề đánh dấu *
    headerStar = new String[]{
        I18n.getLanguage("mtItSoftSchedule.scheduleId"),
        I18n.getLanguage("mtItSoftSchedule.nextDateModify")
    };

    XSSFDataValidationHelper dataValidationHelper = new XSSFDataValidationHelper(sheetOne);
    List<String> listHeader = Arrays.asList(header);
    List<String> listHeaderStar = Arrays.asList(headerStar);

    int marketCodeStrColumn = listHeader
        .indexOf(I18n.getLanguage("mtItSoftSchedule.nationName"));
    int arrayCodeStrColumn = listHeader.indexOf(I18n.getLanguage("mtItSoftSchedule.arrayCode"));
    int mrConfirmNameColumn = listHeader
        .indexOf(I18n.getLanguage("mtItSoftSchedule.mrConfirmName"));
    Map<String, CellStyle> style = CommonExport.createStyles(workbook);

    //Tao tieu de
    Font xssFontTitle = workbook.createFont();
    xssFontTitle.setFontName(HSSFFont.FONT_ARIAL);
    xssFontTitle.setFontHeightInPoints((short) 22);
    xssFontTitle.setColor(IndexedColors.BLACK.index);
    xssFontTitle.setBold(true);

    CellStyle styleTitle = workbook.createCellStyle();
    styleTitle.setAlignment(HorizontalAlignment.CENTER);
    styleTitle.setVerticalAlignment(VerticalAlignment.CENTER);
    styleTitle.setFont(xssFontTitle);

    Font xSSFFontHeader = workbook.createFont();
    xSSFFontHeader.setFontName(HSSFFont.FONT_ARIAL);
    xSSFFontHeader.setFontHeightInPoints((short) 10);
    xSSFFontHeader.setColor(IndexedColors.BLUE.index);
    xSSFFontHeader.setBold(true);

    CellStyle cellStyleHeader = workbook.createCellStyle();
    cellStyleHeader.setAlignment(HorizontalAlignment.CENTER);
    cellStyleHeader.setVerticalAlignment(VerticalAlignment.CENTER);
    cellStyleHeader.setBorderLeft(BorderStyle.THIN);
    cellStyleHeader.setBorderBottom(BorderStyle.THIN);
    cellStyleHeader.setBorderRight(BorderStyle.THIN);
    cellStyleHeader.setBorderTop(BorderStyle.THIN);
    cellStyleHeader.setFillForegroundColor(IndexedColors.LIGHT_GREEN.index);
    cellStyleHeader.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    cellStyleHeader.setWrapText(true);
    cellStyleHeader.setFont(xSSFFontHeader);

    //Tạo tiêu đề
    sheetOne.addMergedRegion(new CellRangeAddress(3, 3, 3, 8));
    Row titleRow = sheetOne.createRow(3);
    titleRow.setHeightInPoints(26.25f);
    Cell titleCell = titleRow.createCell(3);
    titleCell.setCellValue(I18n.getLanguage("mtItSoftSchedule.title.hard"));
    titleCell.setCellStyle(styleTitle);

    XSSFFont starFont = workbook.createFont();
    starFont.setColor(IndexedColors.RED.getIndex());

    //Tạo Header

    Row headerRow = sheetOne.createRow(7);
    Row headerRegion = sheetParam1.createRow(0);
    Row headerArray = sheetParam2.createRow(0);
    headerRow.setHeightInPoints(22.5f);
    for (int i = 0; i < listHeader.size(); i++) {
      Cell headerCell = headerRow.createCell(i);
      XSSFRichTextString richTextString = new XSSFRichTextString(listHeader.get(i));
      for (String headerCheck : listHeaderStar) {
        if (headerCheck.equalsIgnoreCase(listHeader.get(i))) {
          richTextString.append("*", starFont);
        }
      }
      if (i == 14 || i == 15 || i == 16 || i == 18) {
        Drawing drawing = headerCell.getSheet().createDrawingPatriarch();
        CreationHelper factory = workbook.getCreationHelper();
        ClientAnchor anchor = factory.createClientAnchor();
        anchor.setCol1(i + 1);
        anchor.setCol2(i + 2);
        anchor.setRow1(3);
        anchor.setRow2(4);
        Comment comment = drawing.createCellComment(anchor);
        comment.setString(factory.createRichTextString(" Định dạng dd/MM/YYYY hh:mm:ss"));
        headerCell.setCellComment(comment);
      }
      headerCell.setCellValue(richTextString);
      headerCell.setCellStyle(cellStyleHeader);
      sheetOne.setColumnWidth(i, 7000);
    }
    Cell headerCellMarketCode = headerRegion.createCell(0);
    Cell headerCellRegion = headerRegion.createCell(1);
    XSSFRichTextString market = new XSSFRichTextString(
        I18n.getLanguage("mtItSoftSchedule.nationName"));
    XSSFRichTextString reg = new XSSFRichTextString(
        I18n.getLanguage("mtItSoftSchedule.region"));
    headerCellMarketCode.setCellValue(market);
    headerCellMarketCode.setCellStyle(style.get("header"));
    headerCellRegion.setCellValue(reg);
    headerCellRegion.setCellStyle(style.get("header"));
    sheetParam1.setColumnWidth(0, 15000);
    sheetParam1.setColumnWidth(1, 15000);

    Cell headerCellArray = headerArray.createCell(0);
    Cell headerCellDevice = headerArray.createCell(1);
    XSSFRichTextString array = new XSSFRichTextString(
        I18n.getLanguage("mtItSoftSchedule.arrayCode"));
    XSSFRichTextString device = new XSSFRichTextString(
        I18n.getLanguage("mtItSoftSchedule.deviceType"));
    headerCellArray.setCellValue(array);
    headerCellArray.setCellStyle(style.get("header"));
    headerCellDevice.setCellValue(device);
    headerCellDevice.setCellStyle(style.get("header"));
    sheetParam2.setColumnWidth(0, 15000);
    sheetParam2.setColumnWidth(1, 15000);
    sheetParam2.setColumnWidth(2, 15000);
    sheetOne.setColumnWidth(0, 3000);

    // Set dữ liệu vào column dropdown
    int row = 8;
    List<ItemDataCRInside> lstMarketCode = catLocationBusiness
        .getListLocationByLevelCBB(null, 1L, null);
    for (ItemDataCRInside dto : lstMarketCode) {
      excelWriterUtils
          .createCell(sheetParam, 1, row++, dto.getDisplayStr(), style.get("cell"));
    }
    sheetParam.autoSizeColumn(1);
    Name nationName = workbook.createName();
    nationName.setNameName("nationName");
    nationName.setRefersToFormula("param!$B$2:$B$" + row);
    XSSFDataValidationConstraint marketCodeStrConstraint = new XSSFDataValidationConstraint(
        DataValidationConstraint.ValidationType.LIST, "nationName");
    CellRangeAddressList nationNameCreate = new CellRangeAddressList(8, 65000,
        marketCodeStrColumn,
        marketCodeStrColumn);
    XSSFDataValidation dataValidationnAtionName = (XSSFDataValidation) dataValidationHelper
        .createValidation(
            marketCodeStrConstraint, nationNameCreate);
    dataValidationnAtionName.setShowErrorBox(true);
    sheetOne.addValidationData(dataValidationnAtionName);

    row = 1;
    for (ItemDataCRInside marketCode : lstMarketCode) {
      List<MrITHardScheduleDTO> mrDeviceDTOList = mrITHardScheduleRepository
          .getListRegionByMrSynItDevices(
              marketCode.getValueStr() != null ? String.valueOf(marketCode.getValueStr()) : null);
      excelWriterUtils
          .createCell(sheetParam1, 0, row, marketCode.getDisplayStr(), style.get("cell"));
      for (MrITHardScheduleDTO mrDeviceDTO : mrDeviceDTOList) {
        excelWriterUtils
            .createCell(sheetParam1, 1, row++, mrDeviceDTO.getRegion(), style.get("cell"));
      }
    }
    sheetParam1.autoSizeColumn(0);
    sheetParam1.autoSizeColumn(1);

    row = 8;
    Datatable dataArray = catItemRepository.getItemMaster(Constants.MR_ITEM_NAME.MR_SUBCATEGORY,
        LANGUAGUE_EXCHANGE_SYSTEM.COMMON, APPLIED_BUSSINESS.CAT_ITEM.toString(),
        Constants.ITEM_ID, Constants.ITEM_NAME);
    List<CatItemDTO> lstArrayCode = (List<CatItemDTO>) dataArray.getData();
    for (CatItemDTO dto : lstArrayCode) {
      excelWriterUtils
          .createCell(sheetParam, 3, row++, dto.getItemName(), style.get("cell"));
    }
    sheetParam.autoSizeColumn(1);
    Name arrayCode = workbook.createName();
    arrayCode.setNameName("arrayCode");
    arrayCode.setRefersToFormula("param!$D$2:$D$" + row);
    XSSFDataValidationConstraint arrayCodeConstraint = new XSSFDataValidationConstraint(
        DataValidationConstraint.ValidationType.LIST, "arrayCode");
    CellRangeAddressList arrayCodeCreate = new CellRangeAddressList(8, 65000, arrayCodeStrColumn,
        arrayCodeStrColumn);
    XSSFDataValidation dataValidationArrayCode = (XSSFDataValidation) dataValidationHelper
        .createValidation(
            arrayCodeConstraint, arrayCodeCreate);
    dataValidationArrayCode.setShowErrorBox(true);
    sheetOne.addValidationData(dataValidationArrayCode);

    row = 1;
    for (CatItemDTO catDto : lstArrayCode) {
      List<MrSynItDevicesDTO> lstDeviceType = mrSynItHardDevicesRepository
          .getListDeviceTypeByArrayCode(catDto.getItemName());
      excelWriterUtils
          .createCell(sheetParam2, 0, row, catDto.getItemName(), style.get("cell"));
      for (MrSynItDevicesDTO deviceType : lstDeviceType) {
        excelWriterUtils
            .createCell(sheetParam2, 1, row++, deviceType.getDeviceType(), style.get("cell"));
      }
    }
    excelWriterUtils
        .createCell(sheetParam2, 0, row++, null, style.get("cell"));
    sheetParam2.autoSizeColumn(0);
    sheetParam2.autoSizeColumn(1);

    row = 8;
    List<MrConfigDTO> lstMrConfigDTO = mrScheduleTelRepository.getConfigByGroup("LY_DO_KO_BD");
    for (MrConfigDTO dto : lstMrConfigDTO) {
      excelWriterUtils
          .createCell(sheetParam, 17, row++, dto.getConfigName(), style.get("cell"));
    }
    sheetParam.autoSizeColumn(1);
    Name mrConfirmNameStr = workbook.createName();
    mrConfirmNameStr.setNameName("mrConfirmNameStr");
    mrConfirmNameStr.setRefersToFormula("param!$R$2:$R$" + row);
    XSSFDataValidationConstraint mrConfirmNameStrConstraint = new XSSFDataValidationConstraint(
        DataValidationConstraint.ValidationType.LIST, "mrConfirmNameStr");
    CellRangeAddressList mrConfirmNameStrCreate = new CellRangeAddressList(8, 65000,
        mrConfirmNameColumn,
        mrConfirmNameColumn);
    XSSFDataValidation dataValidationmrConfirmNameStr = (XSSFDataValidation) dataValidationHelper
        .createValidation(
            mrConfirmNameStrConstraint, mrConfirmNameStrCreate);
    dataValidationmrConfirmNameStr.setShowErrorBox(true);
    sheetOne.addValidationData(dataValidationmrConfirmNameStr);

    //set tên trang excel
    workbook.setSheetName(0, I18n.getLanguage("mtItSoftSchedule.title.hard"));
    workbook.setSheetName(2, I18n.getLanguage("mtItSoftSchedule.region"));
    workbook.setSheetName(3, I18n.getLanguage("mtItSoftSchedule.arrNet"));
    workbook.setSheetHidden(1, true);
    sheetParam.setSelected(false);

    //set tên file excel
    String fileResult = tempFolder + File.separator;
    String fileName = "IMPORT_MR_SCHEDULE_IT" + "_" + System.currentTimeMillis() + ".xlsx";
    excelWriterUtils.saveToFileExcel(workbook, fileResult, fileName);
    String resultPath = fileResult + fileName;
    File fileExport = new File(resultPath);
    return fileExport;
  }

  @Override
  public List<MrITHardScheduleDTO> getListRegionByMrSynItDevices(String country) {
    return mrITHardScheduleRepository.getListRegionByMrSynItDevices(country);
  }

  @Override
  public ResultInSideDto importData(MultipartFile multipartFile) {
    List<MrITHardScheduleDTO> mrScheduleITDTOArrayList = new ArrayList<>();
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    try {
      if (multipartFile == null || multipartFile.isEmpty()) {
        resultInSideDto.setKey(RESULT.FILE_IS_NULL);
        resultInSideDto.setMessage(I18n.getLanguage("common.fileEmpty"));
        return resultInSideDto;
      } else {
        String filePath = FileUtils
            .saveTempFile(multipartFile.getOriginalFilename(), multipartFile.getBytes(),
                tempFolder);
        File fileImport = new File(filePath);
        List<Object[]> headerList;
        headerList = CommonImport.getDataFromExcelFile(
            fileImport,
            0,
            7,
            0,
            22,
            1000
        );

        //Kiem tra form header
        if (headerList == null || headerList.isEmpty() || !validateFileSoftFormat(headerList,
            getHeaderHard())) {
          resultInSideDto.setKey(RESULT.ERROR);
          resultInSideDto.setMessage(I18n.getLanguage("common.import.errorTemplate"));
          return resultInSideDto;
        }

        //Lấy dữ liệu import
        List<Object[]> dataImportList = CommonImport.getDataFromExcelFileNew(
            fileImport,
            0,
            8,
            0,
            22,
            1000
        );

        if (dataImportList.size() > 1500) {
          resultInSideDto.setKey(RESULT.DATA_OVER);
          resultInSideDto.setMessage(I18n.getLanguage("common.importing.maxrow"));
          return resultInSideDto;
        }
        if (!dataImportList.isEmpty()) {
          int row = 7;
          int index = 0;
          setMrConfirm();
          index = processDataImport(dataImportList, mrScheduleITDTOArrayList, MR_TYPE_HARD);
          if (index == 0) {
            UserToken userToken = ticketProvider.getUserToken();
            if (mrScheduleITDTOArrayList != null && !mrScheduleITDTOArrayList.isEmpty()) {
              for (MrITHardScheduleDTO dto : mrScheduleITDTOArrayList) {
                MrITHardScheduleDTO dtoDetail = getDetail(Long.valueOf(dto.getScheduleId()));
                dtoDetail.setNextDateModify(dto.getNextDateModify());
                dtoDetail.setNote(dto.getNote());
                dtoDetail.setUpdatedDate(new Date());
                ResultInSideDto res = mrITHardScheduleRepository
                    .updateMrSchedule(dtoDetail);
                if (!RESULT.SUCCESS.equals(res.getKey())) {
                  resultInSideDto.setKey(RESULT.ERROR);
                  resultInSideDto.setMessage(I18n.getValidation("import.common.fail"));
                  return resultInSideDto;
                }
              }
            }
          } else {
            File fileExport = exportFileEx(mrScheduleITDTOArrayList,
                Constants.RESULT_IMPORT, MR_TYPE_HARD);
            resultInSideDto.setKey(RESULT.ERROR);
            resultInSideDto.setMessage(I18n.getValidation("import.common.fail"));
            resultInSideDto.setFilePath(fileExport.getPath());
            resultInSideDto.setFile(fileExport);
          }
        } else {
          resultInSideDto.setKey(RESULT.NODATA);
          resultInSideDto.setMessage(I18n.getLanguage("common.searh.nodata"));
          File fileExport = exportFileEx(mrScheduleITDTOArrayList, Constants.RESULT_IMPORT,
              MR_TYPE_HARD);
          resultInSideDto.setFilePath(fileExport.getPath());
          resultInSideDto.setFile(fileExport);
          return resultInSideDto;
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      resultInSideDto.setKey(RESULT.ERROR);
      resultInSideDto.setMessage(e.getMessage());
    }
    return resultInSideDto;
  }

  private int processDataImport(List<Object[]> dataImportList,
      List<MrITHardScheduleDTO> mrScheduleITDTOArrayList, String type) {
    mapDataImport.clear();
    List<String> idSearch = new ArrayList<>();
    int index = 0;
    for (Object[] obj : dataImportList) {
      if (!StringUtils.isStringNullOrEmpty(obj[1]) && DataUtil.isNumber(obj[1].toString().trim())) {
        idSearch.add(obj[1].toString().trim());
      }
    }
    //Lay danh sach lich theo id
    if (!idSearch.isEmpty() && MR_TYPE_HARD.equals(type)) {
      mapDataImport = getMapFromList(idSearch);
    }
//    if (!idSearch.isEmpty() && MR_TYPE_HARD.equals(type)) {
//      mapDataImport = getMapFromListHard(idSearch);
//    }
    for (Object[] obj : dataImportList) {
      MrITHardScheduleDTO mrScheduleITDTO = new MrITHardScheduleDTO();
      int column = 0;
      column++;
      if (!StringUtils.isStringNullOrEmpty(obj[column])) {
        if (!DataUtil.isNumber(obj[column].toString().trim())) {
          mrScheduleITDTO
              .setResultImport(I18n.getValidation("mtItSoftSchedule.scheduleId.invalid"));
          mrScheduleITDTO.setScheduleId(Long.valueOf(obj[column].toString().trim()));
        } else {
          mrScheduleITDTO.setScheduleId(Long.valueOf(obj[column].toString().trim()));
        }
      }
      column++;
      if (!StringUtils.isStringNullOrEmpty(obj[column])) {
        mrScheduleITDTO.setNationName(obj[column].toString().trim());
      }
      column++;
      if (!StringUtils.isStringNullOrEmpty(obj[column])) {
        mrScheduleITDTO.setRegion(obj[column].toString().trim());
      }
      column++;
      if (!StringUtils.isStringNullOrEmpty(obj[column])) {
        mrScheduleITDTO.setArrayCode(obj[column].toString().trim());
      }
      column++;
      if (!StringUtils.isStringNullOrEmpty(obj[column])) {
        mrScheduleITDTO.setDeviceType(obj[column].toString().trim());
      }
      column++;
      if (!StringUtils.isStringNullOrEmpty(obj[column])) {
        mrScheduleITDTO.setDeviceCode(obj[column].toString().trim());
      }
      column++;
      if (!StringUtils.isStringNullOrEmpty(obj[column])) {
        mrScheduleITDTO.setDeviceName(obj[column].toString().trim());
      }
      column++;
      if (!StringUtils.isStringNullOrEmpty(obj[column])) {
        mrScheduleITDTO.setIpNode(obj[column].toString().trim());
      }
      column++;
      if (!StringUtils.isStringNullOrEmpty(obj[column])) {
        mrScheduleITDTO.setCdIdHardStr(obj[column].toString().trim());
      }
      column++;
      if (!StringUtils.isStringNullOrEmpty(obj[column])) {
        if (!DataUtil.isNumber(obj[column].toString().trim())) {
          mrScheduleITDTO
              .setResultImport(I18n.getValidation("mtItSoftSchedule.mrId.invalid"));
          mrScheduleITDTO.setMrId(Long.valueOf(obj[column].toString().trim()));
        } else {
          mrScheduleITDTO.setMrId(Long.valueOf(obj[column].toString().trim()));
        }
      }
      column++;
      if (!StringUtils.isStringNullOrEmpty(obj[column])) {
        if (!DataUtil.isNumber(obj[column].toString().trim())) {
          mrScheduleITDTO
              .setResultImport(I18n.getValidation("mtItSoftSchedule.woId.invalid"));
          mrScheduleITDTO.setWoId(Long.valueOf(obj[column].toString().trim()));
        } else {
          mrScheduleITDTO.setWoId(Long.valueOf(obj[column].toString().trim()));
        }
      }
      column++;
      if (!StringUtils.isStringNullOrEmpty(obj[column])) {
        mrScheduleITDTO.setCycle(Long.valueOf(obj[column].toString().trim()));
      }
      column++;
      if (!StringUtils.isStringNullOrEmpty(obj[column])) {
        mrScheduleITDTO.setCycleType(obj[column].toString().trim());
      }
      column++;
      if (!StringUtils.isStringNullOrEmpty(obj[column])) {
        mrScheduleITDTO
            .setLastDate(DateTimeUtils.convertStringToDate(obj[column].toString().trim()));
      }
      column++;
      if (!StringUtils.isStringNullOrEmpty(obj[column])) {
        mrScheduleITDTO
            .setNextDate(DateTimeUtils.convertStringToDate(obj[column].toString().trim()));
      }
      column++;
      if (!StringUtils.isStringNullOrEmpty(obj[column])) {
        String checkDate = DataUtil.validateDateTimeDdMmYyyy(obj[column].toString().trim());
        if (StringUtils.isStringNullOrEmpty(checkDate)) {
          try {
            mrScheduleITDTO.setNextDateModify(
                DateTimeUtils.convertStringToDate(obj[column].toString().trim()));
          } catch (Exception e) {
            log.error(e.getMessage(), e);
            mrScheduleITDTO.setNextDateModify(
                DateTimeUtils.convertStringToDate(obj[column].toString().trim()));
            mrScheduleITDTO
                .setResultImport(I18n.getValidation("mtItSoftSchedule.nextDateModify.invalid"));
          }
        } else {
          mrScheduleITDTO
              .setNextDateModify(DateTimeUtils.convertStringToDate(obj[column].toString().trim()));
          mrScheduleITDTO
              .setResultImport(I18n.getValidation("mtItSoftSchedule.nextDateModify.invalid"));
        }
      }
      column++;
      if (!StringUtils.isStringNullOrEmpty(obj[column])) {
        mrScheduleITDTO.setWoContent(obj[column].toString().trim());
      }
      column++;
      if (!StringUtils.isStringNullOrEmpty(obj[column])) {
        mrScheduleITDTO
            .setUpdatedDate(DateTimeUtils.convertStringToDate(obj[column].toString().trim()));
      }
      column++;
      if (!StringUtils.isStringNullOrEmpty(obj[column])) {
        mrScheduleITDTO.setVendor(obj[column].toString().trim());
      }
      column++;
      if (!StringUtils.isStringNullOrEmpty(obj[column])) {
        mrScheduleITDTO.setNote(obj[column].toString().trim());
      }
      column++;
      if (!StringUtils.isStringNullOrEmpty(obj[column])) {
        mrScheduleITDTO.setMrConfirmHardName(obj[column].toString().trim());
        for (Map.Entry<String, String> item : mapMrConfirm.entrySet()) {
          if (mrScheduleITDTO.getMrConfirmHardName().equals(item.getValue())) {
            mrScheduleITDTO.setMrConfirmHard(item.getKey());
            break;
          }
        }
      }
      MrITHardScheduleDTO mrScheduleITDTOTmp = validateImportInfo(
          mrScheduleITDTO, mrScheduleITDTOArrayList, type);

      if (mrScheduleITDTOTmp.getResultImport() == null) {
        mrScheduleITDTOTmp
            .setResultImport(I18n.getLanguage("mrHardGroupConfig.result.import"));
        mrScheduleITDTOArrayList.add(mrScheduleITDTOTmp);
      } else {
        mrScheduleITDTOArrayList.add(mrScheduleITDTO);
        index++;
      }
    }
    return index;
  }


  Map<Long, MrITHardScheduleDTO> getMapFromList(List<String> idSearch) {
    Map<Long, MrITHardScheduleDTO> mapData = new HashMap<>();
    List<MrITHardScheduleDTO> results = mrITHardScheduleRepository
        .getListMrSheduleITByIdForImport(idSearch);
    if (results != null && !results.isEmpty()) {
      results.forEach(item -> {
        if (item.getScheduleId() != null) {
          mapData.put(item.getScheduleId(), item);
        }
      });
    }
    return mapData;
  }

  public void setMrConfirm() {
    mapMrConfirm.clear();
    List<MrConfigDTO> lstMrConfigDTO = mrScheduleTelRepository.getConfigByGroup("LY_DO_KO_BD");
    if (lstMrConfigDTO != null && !lstMrConfigDTO.isEmpty()) {
      for (MrConfigDTO mrConfigDTO : lstMrConfigDTO) {
        mapMrConfirm
            .put(String.valueOf(mrConfigDTO.getConfigCode()), mrConfigDTO.getConfigName());
      }
    }
  }

  boolean validateFileSoftFormat(List<Object[]> headerList, String[] headerConfig) {
    Object[] objects = headerList.get(0);
    if (objects == null) {
      return false;
    }

    if (objects.length != headerConfig.length + 1) {
      return false;
    }

    try {
      for (int i = 1; i < headerConfig.length; i++) {
        if (!headerConfig[i].trim()
            .equalsIgnoreCase(objects[i].toString().trim().replace("*", ""))) {
          return false;
        }
      }
      return true;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return false;
  }

  private MrITHardScheduleDTO validateImportInfo(MrITHardScheduleDTO mrScheduleITDTO,
      List<MrITHardScheduleDTO> list, String type) {

    if (StringUtils.isStringNullOrEmpty(mrScheduleITDTO.getScheduleId())) {
      mrScheduleITDTO.setResultImport(I18n.getValidation("mtItSoftSchedule.scheduleId.notEmpty"));
      return mrScheduleITDTO;
    }
    if (mrScheduleITDTO.getScheduleId() != null) {
      if (StringUtils
          .isStringNullOrEmpty(mapDataImport.get(Long.valueOf(mrScheduleITDTO.getScheduleId())))) {
        mrScheduleITDTO.setResultImport(I18n.getValidation("mtItSoftSchedule.scheduleId.notFound"));
        return mrScheduleITDTO;
      }
    } else {
      mrScheduleITDTO.setResultImport(I18n.getValidation("mtItSoftSchedule.scheduleId.notFound"));
      return mrScheduleITDTO;
    }
    if (MR_TYPE_HARD.equals(type)) {
      if ("5".equals(mrScheduleITDTO.getMrConfirmHard())) {
        if (StringUtils.isStringNullOrEmpty(mrScheduleITDTO.getCrId())) {
          mrScheduleITDTO
              .setResultImport(I18n.getValidation("import.mtItSoftSchedule.cr.isrequired"));
          return mrScheduleITDTO;
        }
        if (mrScheduleITDTO.getCrId() != null) {
          mrScheduleITDTO.setCrId(mrScheduleITDTO.getCrId());
        } else {
          mrScheduleITDTO
              .setResultImport(I18n.getValidation("mtItSoftSchedule.crId.invalid"));
          return mrScheduleITDTO;
        }
      }
    }
    if (StringUtils.isStringNullOrEmpty(mrScheduleITDTO.getNextDateModify())) {
      mrScheduleITDTO
          .setResultImport(I18n.getValidation("mtItSoftSchedule.nextDateModify.notEmpty"));
      return mrScheduleITDTO;
    }
    MrITHardScheduleDTO objUpdate = mapDataImport.get(mrScheduleITDTO.getScheduleId());
    //không cho phép cập nhật khi đã có MrId
    if (!StringUtils.isStringNullOrEmpty(objUpdate.getMrId()) && !StringUtils
        .isStringNullOrEmpty(objUpdate.getWoId())) {
      mrScheduleITDTO.setResultImport(I18n.getValidation("mtItHardSchedule.mrId.removeConfirm"));
      return mrScheduleITDTO;
    } else {
      if (DateUtil
          .getYY(mrScheduleITDTO.getNextDateModify()) < DateUtil.getYY(new Date())) {
        mrScheduleITDTO
            .setResultImport(I18n.getValidation("mtItSoftSchedule.error.nextDateModify.invalid"));
        return mrScheduleITDTO;
      } else if (DateUtil.getYY(mrScheduleITDTO.getNextDateModify()) == DateUtil
          .getYY(new Date())) {
        if (DateUtil.getMonth(mrScheduleITDTO.getNextDateModify()) < DateUtil
            .getMonth(new Date())) {
          mrScheduleITDTO
              .setResultImport(I18n.getValidation("mtItSoftSchedule.error.nextDateModify.invalid"));
          return mrScheduleITDTO;
        } else if (DateUtil.getMonth(mrScheduleITDTO.getNextDateModify()) == DateUtil
            .getMonth(new Date())) {
          if (DateUtil.getDayInMonth(mrScheduleITDTO.getNextDateModify()) < DateUtil
              .getDayInMonth(new Date())) {
            mrScheduleITDTO
                .setResultImport(
                    I18n.getValidation("mtItSoftSchedule.error.nextDateModify.invalid"));
            return mrScheduleITDTO;
          }
        }
      }
    }
    if (StringUtils.isNotNullOrEmpty(mrScheduleITDTO.getMrConfirmHardName()) && StringUtils
        .isStringNullOrEmpty(mrScheduleITDTO.getMrConfirmHard())) {
      mrScheduleITDTO
          .setResultImport(I18n.getValidation("mtItSoftSchedule.mrConfirm.invalid"));
      return mrScheduleITDTO;
    }
    if (mrScheduleITDTO.getNote() != null
        && mrScheduleITDTO.getNote().length() > 1000) {
      mrScheduleITDTO
          .setResultImport(I18n.getValidation("mtItSoftSchedule.mrComment.tooLong"));
      return mrScheduleITDTO;
    }

//    if (StringUtils.isStringNullOrEmpty(objUpdate.getMrId()) || StringUtils
//        .isStringNullOrEmpty(objUpdate.getWoId())) {
//    } else {
//      if (!objUpdate.getNextDateModify().equals(mrScheduleITDTO.getNextDateModify())) {
//        mrScheduleITDTO
//            .setResultImport(I18n.getValidation("mtItSoftSchedule.import.nextDateModify.WO"));
//        return mrScheduleITDTO;
//      }
//    }
    mrScheduleITDTO = validateDuplicate(mrScheduleITDTO, list);
    return mrScheduleITDTO;
  }

  private MrITHardScheduleDTO validateDuplicate(MrITHardScheduleDTO dto,
      List<MrITHardScheduleDTO> lstoExportDTOS) {
    if (lstoExportDTOS != null && !lstoExportDTOS.isEmpty()) {
      for (int i = 0; i < lstoExportDTOS.size(); i++) {
        MrITHardScheduleDTO dtoCheck = lstoExportDTOS.get(i);
        if (dto.getScheduleId().equals(dtoCheck.getScheduleId())) {
          dto.setResultImport(I18n.getValidation("MrMaterialDTO.unique.file")
              .replaceAll("0", String.valueOf((i) + 1)));
          break;
        }
      }
    }
    return dto;
  }

  private String[] getHeaderHard() {
    return new String[]{
        I18n.getLanguage("common.STT"),
        I18n.getLanguage("mtItSoftSchedule.scheduleId"),
        I18n.getLanguage("mtItSoftSchedule.nationName"),
        I18n.getLanguage("mtItSoftSchedule.region"),
        I18n.getLanguage("mtItSoftSchedule.arrayCode"),
        I18n.getLanguage("mtItSoftSchedule.deviceType"),
        I18n.getLanguage("mtItSoftSchedule.deviceCode"),
        I18n.getLanguage("mtItSoftSchedule.deviceName"),
        I18n.getLanguage("mtItSoftSchedule.ipNode"),
        I18n.getLanguage("mtItSoftSchedule.cdIdHardStr"),
        I18n.getLanguage("mtItSoftSchedule.mrId"),
        I18n.getLanguage("mtItSoftSchedule.woId"),
        I18n.getLanguage("mtItSoftSchedule.cycle"),
        I18n.getLanguage("mtItSoftSchedule.cycleType"),
        I18n.getLanguage("mtItSoftSchedule.lastDate"),
        I18n.getLanguage("mtItSoftSchedule.nextDate"),
        I18n.getLanguage("mtItSoftSchedule.nextDateModify"),
        I18n.getLanguage("mtItSoftSchedule.woContent"),
        I18n.getLanguage("mtItSoftSchedule.vendor"),
        I18n.getLanguage("mtItSoftSchedule.updatedDate"),
        I18n.getLanguage("mtItSoftSchedule.note"),
        I18n.getLanguage("mtItSoftSchedule.mrConfirmName")
    };
  }

  private ResultInSideDto insertMrScheduleTelHis(MrITHardScheduleDTO scheduleItDTO) {
    MrInsideDTO mrInsideDTO = new MrInsideDTO();
    List<MrScheduleITHisDTO> listScheduleITHis = new ArrayList<>();
    mrInsideDTO.setMrId(scheduleItDTO.getMrId());
    if (mrInsideDTO.getMrId() != null) {
      mrInsideDTO = mrRepository.findMrById(mrInsideDTO.getMrId());
    }
    MrScheduleITHisDTO scheduleHisDTO = new MrScheduleITHisDTO();
    scheduleHisDTO.setMarketCode(scheduleItDTO.getMarketCode());
    scheduleHisDTO.setArrayCode(scheduleItDTO.getArrayCode());
    scheduleHisDTO.setDeviceType(scheduleItDTO.getDeviceType());
    scheduleHisDTO
        .setDeviceId(
            scheduleItDTO.getDeviceId() != null ? String.valueOf(scheduleItDTO.getDeviceId())
                : null);
    scheduleHisDTO.setDeviceCode(scheduleItDTO.getDeviceCode());
    scheduleHisDTO.setDeviceName(scheduleItDTO.getDeviceName());
    scheduleHisDTO
        .setMrDate(scheduleItDTO.getNextDateModify() != null ? DateTimeUtils
            .convertDateToString(scheduleItDTO.getNextDateModify()) : null);
    scheduleHisDTO.setMrContent(mrInsideDTO == null ? null
        : mrInsideDTO.getMrContentId());
    scheduleHisDTO.setMrMode("H");
    scheduleHisDTO.setMrType(mrInsideDTO == null ? null
        : mrInsideDTO.getMrType());
    scheduleHisDTO
        .setMrId(scheduleItDTO.getMrId() != null ? String.valueOf(scheduleItDTO.getMrId()) : null);
    scheduleHisDTO.setMrCode(mrInsideDTO == null ? null
        : mrInsideDTO.getMrCode());
    if (!StringUtils.isStringNullOrEmpty(scheduleItDTO.getWoId())) {
      scheduleHisDTO.setWoId(scheduleItDTO.getWoId().toString());
    }
    scheduleHisDTO.setProcedureId(
        scheduleItDTO.getProcedureId() != null ? String.valueOf(scheduleItDTO.getProcedureId())
            : null);
    scheduleHisDTO.setProcedureName(scheduleItDTO.getProcedureName());
    scheduleHisDTO.setNetworkType(scheduleItDTO.getNetworkType());
    scheduleHisDTO.setCycle(
        scheduleItDTO.getCycle() != null ? String.valueOf(scheduleItDTO.getCycle()) : null);
    scheduleHisDTO.setRegion(scheduleItDTO.getRegion());
    scheduleHisDTO.setNote(I18n.getLanguage("mrScheduleTelhis.note.deleteSched"));
    listScheduleITHis.add(scheduleHisDTO);
    return mrITHisRepository.insertUpdateListScheduleHis(listScheduleITHis);
  }
}
