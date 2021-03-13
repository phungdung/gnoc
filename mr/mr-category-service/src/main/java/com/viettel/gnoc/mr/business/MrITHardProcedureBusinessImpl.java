package com.viettel.gnoc.mr.business;

import com.viettel.gnoc.commons.business.CatItemBusiness;
import com.viettel.gnoc.commons.business.CatLocationBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.UnitRepository;
import com.viettel.gnoc.commons.utils.CellConfigExport;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.ConfigFileExport;
import com.viettel.gnoc.commons.utils.ConfigHeaderExport;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.MR_ITEM_NAME;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.ErrorInfo;
import com.viettel.gnoc.commons.utils.ExcelWriterUtils;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.maintenance.dto.MrCfgProcedureITHardDTO;
import com.viettel.gnoc.maintenance.dto.MrITHardScheduleDTO;
import com.viettel.gnoc.maintenance.dto.MrScheduleITHisDTO;
import com.viettel.gnoc.maintenance.dto.MrSynItDevicesDTO;
import com.viettel.gnoc.mr.repository.MrITHardProcedureRepository;
import com.viettel.gnoc.mr.repository.MrITHardScheduleRepository;
import com.viettel.gnoc.mr.repository.MrITHisRepository;
import com.viettel.gnoc.mr.repository.MrITSoftProcedureRepository;
import com.viettel.gnoc.mr.repository.MrITSoftScheduleRepository;
import com.viettel.security.PassTranformer;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@Slf4j
public class MrITHardProcedureBusinessImpl implements MrITHardProcedureBusiness {

  @Autowired
  private MrITHardProcedureRepository mrITHardProcedureRepository;

  @Autowired
  private MrITSoftProcedureRepository mrITSoftProcedureRepository;

  @Autowired
  private MrITSoftScheduleRepository mrITSoftScheduleRepository;

  @Autowired
  private MrITHisRepository mrITHisRepository;

  @Autowired
  UnitRepository unitRepository;

  @Autowired
  MrSynItSoftDevicesBusiness mrSynItSoftDevicesBusiness;

  @Autowired
  protected CatLocationBusiness catLocationBusiness;

  @Autowired
  MrCfgProcedureCDBusiness mrCfgProcedureCDBusiness;

  @Autowired
  CatItemBusiness catItemBusiness;

  @Autowired
  MrITHardScheduleRepository mrITHardScheduleRepository;

  private static final String MR_PROCEDURE_IT_HARD_RESULT_IMPORT = "MR_PROCEDURE_IT_HARD_RESULT_IMPORT";

  private static final String MR_PROCEDURE_IT_HARD_EXPORT = "MR_PROCEDURE_IT_HARD_EXPORT";

  private List<ItemDataCRInside> lstCountry;

  private String pathTemplate = "templates" + File.separator + "maintenance" + File.separator;
  private Map<String, String> mapErrorResult = new HashMap<>();
  private List<ErrorInfo> cellErrorList = new ArrayList<>();
  public final static String REGEX_NUMBER = "([+-]*[1-9])([0-9])*";
  public final static String REGEX_DATE_MONTH_YEAR = "^(?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)(\\/|-|\\.)(?:0?[1,3-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(\\/|-|\\.)0?2\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])(\\/|-|\\.)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$";
  public final static String REGEX_MONTH_YEAR = "^(0?[1-9]|1[0-2])/(19|2[0-9])\\d{2}$";

  @Value("${application.temp.folder}")
  private String tempFolder;

  @Value("${application.upload.folder}")
  private String uploadFolder;

  @Override
  public Datatable getListMrHardITProcedure(MrCfgProcedureITHardDTO mrCfgProcedureITHardDTO) {
    log.debug("Request to getListMrHardITProcedure: {}", mrCfgProcedureITHardDTO);
    return mrITHardProcedureRepository.getListMrHardITProcedure(mrCfgProcedureITHardDTO);
  }

  @Override
  public MrCfgProcedureITHardDTO getDetail(Long procedureId) {
    log.debug("Request to getDetail: {}", procedureId);
    return mrITHardProcedureRepository.getDetail(procedureId);
  }

  @Override
  public ResultInSideDto insertMrCfgProcedureITHard(
      MrCfgProcedureITHardDTO mrCfgProcedureITHardDTO) {
    log.debug("Request to insertMrCfgProcedureITHard: {}", mrCfgProcedureITHardDTO);
    mrCfgProcedureITHardDTO.setMrMode("H");
    mrCfgProcedureITHardDTO.setGenCr("0");
    mrCfgProcedureITHardDTO.setIsServiceAffected("0");
    if (mrITHardProcedureRepository
        .checkDuplicateCfgProcedureITHardByDTO(mrCfgProcedureITHardDTO)) {
      return new ResultInSideDto(null, RESULT.DUPLICATE,
          I18n.getValidation("mr.cfgProcedureItHard.isDupplicate"));
    }
    return mrITHardProcedureRepository.insertOrUpdate(mrCfgProcedureITHardDTO);
  }

  @Override
  public ResultInSideDto updateMrCfgProcedureITHard(
      MrCfgProcedureITHardDTO mrCfgProcedureITHardDTO) {
    log.debug("Request to update : {}", mrCfgProcedureITHardDTO);
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    boolean checkUpdate = false;

    if (mrCfgProcedureITHardDTO != null) {
      //Check trùng
      if (mrITHardProcedureRepository
          .checkDuplicateCfgProcedureITHardByDTO(mrCfgProcedureITHardDTO)) {
        return new ResultInSideDto(null, RESULT.DUPLICATE,
            I18n.getValidation("mr.cfgProcedureItHard.isDupplicate"));
      }
      MrCfgProcedureITHardDTO dtoUpdate = mrITHardProcedureRepository
          .getDetail(mrCfgProcedureITHardDTO.getProcedureId());
      MrITHardScheduleDTO dtoScheduleIT = new MrITHardScheduleDTO();
      dtoScheduleIT.setProcedureId(mrCfgProcedureITHardDTO.getProcedureId());

      // update cycleType, Cycle , sinh MR truoc ngay, thoi gian thuc hien -----> xóa lịch , Cập nhật is_complete_1M/ 3M/ 6M/ 12M = 1 ở MR_DEVICE
      List<MrITHardScheduleDTO> lstScheduleIT = new ArrayList<>();
      List<MrITHardScheduleDTO> lstScheduleITDel = new ArrayList<>();
      if (!StringUtils.isStringNullOrEmpty(dtoUpdate)) {
        // Nếu CycleType hoặc Cycle hoặc genMrbefore hoặc Mrtime có thay đổi thì
        if (!mrCfgProcedureITHardDTO.getCycleType().equals(dtoUpdate.getCycleType()) || !String
            .valueOf(mrCfgProcedureITHardDTO.getCycle())
            .equals(String.valueOf(dtoUpdate.getCycle()))
            || !mrCfgProcedureITHardDTO.getGenMrBefore().equals(dtoUpdate.getGenMrBefore())
            || mrCfgProcedureITHardDTO.getMrTime() != dtoUpdate.getMrTime()) {

          lstScheduleIT = mrITHardProcedureRepository.getScheduleInProcedureITHard(dtoScheduleIT);
          if (lstScheduleIT != null && !lstScheduleIT.isEmpty()) {
            for (MrITHardScheduleDTO item : lstScheduleIT) {
              //Trường hợp: MrId null && WoID null
              if (StringUtils.isStringNullOrEmpty(item.getMrId()) && StringUtils
                  .isStringNullOrEmpty(item.getWoId())) {
                lstScheduleITDel.add(item);
              }
            }
          }
          // update MrDevice
          MrSynItDevicesDTO deviceDTO = new MrSynItDevicesDTO();
          deviceDTO.setMarketCode(mrCfgProcedureITHardDTO.getMarketCode());
          deviceDTO.setArrayCode(mrCfgProcedureITHardDTO.getArrayCode());
          deviceDTO.setDeviceType(mrCfgProcedureITHardDTO.getDeviceType());

          deviceDTO.setMrHard("1");

          List<MrSynItDevicesDTO> result = new ArrayList<>();
          int rowStart = 0;
          int maxRow = 5000;
          while (true) {
            List<MrSynItDevicesDTO> lstDTO = mrSynItSoftDevicesBusiness
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

          // Cập nhật is_complete_1M/ 3M/ 6M/ 12M = 1 ở MR_DEVICE
          List<MrSynItDevicesDTO> resultUpdate = new ArrayList<>();
          if (lstScheduleITDel != null && !lstScheduleITDel.isEmpty()) {
            if (result != null && !result.isEmpty()) {
              int size = result.size();
              for (int i = size - 1; i > -1; i--) {
                for (MrITHardScheduleDTO item : lstScheduleITDel) {
                  if (item.getDeviceCode().equals(result.get(i).getObjectCode())) {
                    if ("M".equals(item.getCycleType())) {
                      if (!StringUtils.isStringNullOrEmpty(item.getCycle())) {
                        if (1L == Long.valueOf(item.getCycle())) {
                          result.get(i).setIsComplete1m("1");
                          resultUpdate.add(result.get(i));
                          break;
                        } else if (3L == Long.valueOf(item.getCycle())) {
                          result.get(i).setIsComplete3m("1");
                          resultUpdate.add(result.get(i));
                          break;
                        } else if (6L == Long.valueOf(item.getCycle())) {
                          result.get(i).setIsComplete6m("1");
                          resultUpdate.add(result.get(i));
                          break;
                        } else if (12L == Long.valueOf(item.getCycle())) {
                          result.get(i).setIsComplete12m("1");
                          resultUpdate.add(result.get(i));
                          break;
                        }
                      }
                    }
                  }
                }
              }
            }
          }
          if (lstScheduleITDel != null && !lstScheduleITDel.isEmpty()) {
            checkUpdate = true;
            insertMrScheduleITHis(lstScheduleITDel, checkUpdate);
            mrITHardScheduleRepository.deleteListSchedule(lstScheduleITDel);
          }
          if (resultUpdate != null && !resultUpdate.isEmpty()) {
            mrSynItSoftDevicesBusiness.insertOrUpdateListDevice(resultUpdate);
          }
        }
      }
      //update thu tuc bao duong
      resultInSideDto = mrITHardProcedureRepository.insertOrUpdate(mrCfgProcedureITHardDTO);
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto deleteMrCfgProcedureITHard(Long procedureId) {
    log.debug("Request to delete : {}", procedureId);
    ResultInSideDto resultInSideDto = new ResultInSideDto(null, RESULT.ERROR, RESULT.ERROR);
    //xoa cau hinh
    //Lay ra 2 listMrScheduleTel va listMrDevice
    MrITHardScheduleDTO scheduleITDTO = new MrITHardScheduleDTO();
    scheduleITDTO.setProcedureId(procedureId);
    List<MrITHardScheduleDTO> lstMrScheduleIT = mrITHardProcedureRepository
        .getScheduleInProcedureITHard(scheduleITDTO);
    List<MrITHardScheduleDTO> lstMrScheduleDelete = new ArrayList<>();
    List<MrSynItDevicesDTO> lstMrDevice = new ArrayList<>();
    MrSynItDevicesDTO dtoDevice = new MrSynItDevicesDTO();
    if (lstMrScheduleIT != null && !lstMrScheduleIT.isEmpty()) {
      for (MrITHardScheduleDTO item : lstMrScheduleIT) {
        if (StringUtils.isStringNullOrEmpty(item.getMrId()) && StringUtils
            .isStringNullOrEmpty(item.getWoId())) {
          dtoDevice.setObjectId(item.getDeviceId());
          dtoDevice.setDeviceType(item.getDeviceType());
          MrSynItDevicesDTO dtoOld = mrSynItSoftDevicesBusiness
              .findMrDeviceByObjectId(dtoDevice);
          //Cap nhat is_complete_1M/ 3M/ 6M/ 12M = 1 ở MR_DEVICE
          if (dtoOld != null && !StringUtils.isStringNullOrEmpty(dtoOld.getObjectId())) {
            if ("M".equals(item.getCycleType())) {
              if (!StringUtils.isStringNullOrEmpty(item.getCycle())) {
                if (1L == Long.valueOf(item.getCycle())) {
                  dtoOld.setIsComplete1m("1");
                } else if (3L == Long.valueOf(item.getCycle())) {
                  dtoOld.setIsComplete3m("1");
                } else if (6L == Long.valueOf(item.getCycle())) {
                  dtoOld.setIsComplete6m("1");
                } else if (12L == Long.valueOf(item.getCycle())) {
                  dtoOld.setIsComplete12m("1");
                }
              }
            }
            lstMrDevice.add(dtoOld);
          }
          lstMrScheduleDelete.add(item);
        }
      }
    }
    if (lstMrDevice != null && !lstMrDevice.isEmpty()) {
      mrSynItSoftDevicesBusiness.insertOrUpdateListDevice(lstMrDevice);
    }

    if (procedureId != null) {
      resultInSideDto = mrITHardProcedureRepository.deleteMrCfgProcedureITHard(procedureId);
      if (lstMrScheduleDelete != null && !lstMrScheduleDelete.isEmpty()) {
        mrITHardScheduleRepository.deleteListSchedule(lstMrScheduleDelete);
        insertMrScheduleITHis(lstMrScheduleDelete, false);
      }
    }
    return resultInSideDto;
  }

  private void insertMrScheduleITHis(List<MrITHardScheduleDTO> lstScheduleIT, boolean checkUpdate) {
    List<MrScheduleITHisDTO> lstScheduleITHis = new ArrayList<>();
    for (MrITHardScheduleDTO item : lstScheduleIT) {
      MrScheduleITHisDTO scheduleHisDTO = new MrScheduleITHisDTO();
      scheduleHisDTO.setMarketCode(item.getMarketCode());
      scheduleHisDTO.setArrayCode(item.getArrayCode());
      scheduleHisDTO.setDeviceType(item.getDeviceType());
      scheduleHisDTO.setDeviceId(item.getDeviceId() == null ? null : item.getDeviceId());
      scheduleHisDTO.setDeviceCode(item.getDeviceCode());
      scheduleHisDTO.setDeviceName(item.getDeviceName());
      scheduleHisDTO.setMrDate(item.getNextDateModify() == null ? ""
          : DateTimeUtils.convertDateToString(item.getNextDateModify()));
      scheduleHisDTO.setMrContent(item.getMrContentId());
      scheduleHisDTO.setMrMode("H");
      scheduleHisDTO.setMrType(item.getMrType());
      scheduleHisDTO.setMrId(item.getMrId() == null ? "" : String.valueOf(item.getMrId()));
      scheduleHisDTO.setMrCode(item.getMrCode());

      scheduleHisDTO
          .setProcedureId(
              item.getProcedureId() == null ? "" : String.valueOf(item.getProcedureId()));
      scheduleHisDTO.setProcedureName(item.getProcedureName());

      scheduleHisDTO.setCycle(String.valueOf(item.getCycle()));
      scheduleHisDTO.setRegion(item.getRegion());
      if (checkUpdate) {
        scheduleHisDTO.setNote(I18n.getLanguage("MrCfgProcedureITHard.update.note"));
      } else {
        scheduleHisDTO.setNote(I18n.getLanguage("MrCfgProcedureITHard.delete.node"));
      }
      lstScheduleITHis.add(scheduleHisDTO);
    }

    mrITHisRepository.insertUpdateListScheduleHis(lstScheduleITHis);
  }

  public String validateFileImport(String path) {
    String msg = "";
    if (path.contains(".xls")) {
      boolean check = validateFileData(path);
      if (!check) {
        msg = I18n.getLanguage("common.import.errorTemplate");
      }
    } else {
      msg = I18n.getLanguage("common.import.file.extend.invalid");
    }
    return msg;
  }

  private boolean validateFileData(String fileImportPathOut) {
    String language = I18n.getLocale();
    String fileTemplateName = "IMPORT_CFG_PROCEDURE_IT_HARD_TEMPLATE_EN.xlsx";
    if (language != null && (language.toLowerCase().contains("vi") || language.toLowerCase()
        .contains("vi_VN"))) {
      fileTemplateName = "IMPORT_CFG_PROCEDURE_IT_HARD_TEMPLATE_VI.xlsx";
    }
    boolean resultOut = true;
    String pathFileImport = pathTemplate + fileTemplateName;
    ExcelWriterUtils ewu = new ExcelWriterUtils();
    Workbook workBook = null;
    Workbook workBook2 = null;
    try {
      workBook = ewu.readFileExcelFromTemplate(pathFileImport);
      Sheet sheetTemplate = workBook.getSheetAt(0);

      workBook2 = ewu.readFileExcel(fileImportPathOut);
      Sheet sheetFileImport = workBook2.getSheetAt(0);

      Row rowFileImport = sheetFileImport.getRow(7);
      Row rowTemplate = sheetTemplate.getRow(7);
      String arrHeaderTemplate = "";
      String arrHeaderFileImport = "";
      int k = rowTemplate.getLastCellNum();
      for (int i = 0; i < k; i++) {
        if (rowTemplate.getCell(i).getStringCellValue() != null) {
          arrHeaderTemplate += "," + rowTemplate.getCell(i).getStringCellValue();
        } else {
          break;
        }
      }
      int l = rowFileImport.getLastCellNum();
      for (int j = 0; j < l; j++) {
        if (rowFileImport.getCell(j).getStringCellValue() != null) {
          arrHeaderFileImport += "," + rowFileImport.getCell(j).getStringCellValue();
        } else {
          break;
        }
      }
      if (!arrHeaderTemplate.equals(arrHeaderFileImport)) {
        resultOut = false;
      }

    } catch (Exception e) {
      resultOut = false;
      log.error(e.getMessage(), e);
    } finally {
      try {
        workBook.close();
        workBook2.close();
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return resultOut;
  }


  private void setMapResult(Long index, String msg) {
    mapErrorResult.put(index.toString(), msg);
  }

  HashMap<String, String> mapArrayByDeviceType = new HashMap<>();

  public void setMapArrayByDeviceType() {
    //Lấy ra ds Mảng, loại mạng, loại thiết bị
    List<MrSynItDevicesDTO> deviceDTOList = mrITHardProcedureRepository.getListDeviceType();
    if (deviceDTOList != null && !deviceDTOList.isEmpty()) {
      for (MrSynItDevicesDTO dto : deviceDTOList) {
        mapArrayByDeviceType
            .put(dto.getDeviceType() + "_" + dto.getArrayCode(), dto.getArrayCode());
      }
    }
  }

  private boolean validateImportInfo(Long index, MrCfgProcedureITHardDTO dto,
      Map<String, ItemDataCRInside> mapCountry, Map<String, CatItemDTO> mapOgWork,
      Map<String, Long> mapSubCat) {
    boolean check = true;
    //Quốc gia
    if (StringUtils.isStringNullOrEmpty(dto.getMarketCode())) {
      setMapResult(index, I18n.getLanguage("cfgProcedureHardView.list.grid.marketCode") + " " + I18n
          .getLanguage("common.notnull"));
      return false;
    } else if (!mapCountry.containsKey(dto.getMarketCode())) {
      setMapResult(index, I18n.getLanguage("cfgProcedureHardView.list.grid.marketCode") + " " + I18n
          .getLanguage("notify.invalid"));
      return false;
    }

    //Mảng arrayCode
    if (StringUtils.isStringNullOrEmpty(dto.getArrayCode())) {
      setMapResult(index, I18n.getLanguage("cfgProcedureHardView.list.grid.arrayCode") + " " + I18n
          .getLanguage("common.notnull"));
      return false;
    } else {
      check = false;
      List<CatItemDTO> lstCatItem = mrCfgProcedureCDBusiness.getMrSubCategory();
      for (CatItemDTO cat : lstCatItem) {
        if (cat.getItemName().equals(dto.getArrayCode())) {
          check = true;
          break;
        }
      }
      if (!check) {
        setMapResult(index,
            I18n.getLanguage("cfgProcedureHardView.list.grid.arrayCode") + " " + I18n
                .getLanguage("notify.invalid"));
        return false;
      }
    }

    //Loại thiết bị deviceType
    if (StringUtils.isStringNullOrEmpty(dto.getDeviceType())) {
      setMapResult(index, I18n.getLanguage("cfgProcedureHardView.list.grid.deviceType") + " " + I18n
          .getLanguage("common.notnull"));
      return false;
    } else {
      check = false;
      List<MrSynItDevicesDTO> lstDevice = mrITHardProcedureRepository.getListDeviceType();
      for (MrSynItDevicesDTO item : lstDevice) {
        if (item.getDeviceType().equals(dto.getDeviceType())) {
          check = true;
          break;
        }
      }
      if (!check) {
        setMapResult(index,
            I18n.getLanguage("cfgProcedureHardView.list.grid.deviceType") + " " + I18n
                .getLanguage("cfgProcedureHardView.dataImport.notexist"));
        return false;
      }
      //so sánh DeviceType nhap vao co dung voi Mang hay ko?
      if (check == true) {
        if (!dto.getArrayCode()
            .equals(mapArrayByDeviceType.get(dto.getDeviceType() + "_" + dto.getArrayCode()))) {
          setMapResult(index, I18n.getLanguage("cfgProcedureHardView.deviceType.falseDeviceType"));
          return false;
        }
      }

    }

    //Loại chu kỳ
    if (!StringUtils.isStringNullOrEmpty(dto.getCycleType())) {
      if ("D".equalsIgnoreCase(dto.getCycleType()) || "M".equalsIgnoreCase(dto.getCycleType())) {
        dto.setCycleType(dto.getCycleType().toUpperCase());
      } else {
        setMapResult(index,
            I18n.getLanguage("cfgProcedureHardView.list.grid.cycleType") + " " + I18n
                .getLanguage("notify.invalid"));
        return false;
      }
    }

    //Chu kỳ
    if (StringUtils.isStringNullOrEmpty(dto.getCycleStr())) {

    } else if (!dto.getCycleStr().matches(REGEX_NUMBER)) {
      setMapResult(index, I18n.getLanguage("cfgProcedureHardView.check.cycle") + " " + I18n
          .getLanguage("cfgProcedureHardView.checkData.number"));
      return false;
    } else if (dto.getCycleStr().matches(REGEX_NUMBER)) {
      if (Long.parseLong(dto.getCycleStr()) > 0 && (dto.getCycleStr().equals("1") || dto
          .getCycleStr().equals("3") || dto.getCycleStr().equals("6") || dto.getCycleStr()
          .equals("12"))) {
        dto.setCycle(Long.parseLong(dto.getCycleStr()));
      } else {
        setMapResult(index, I18n.getLanguage("cfgProcedureHardView.check.cycle") + " " + I18n
            .getLanguage("cfgProcedureHardITView.checkData.number"));
        return false;
      }
    }
    dto.setMrMode("H");
//    //Hình thức BD mrMode
//    if (StringUtils.isStringNullOrEmpty(dto.getMrMode())) {
//      setMapResult(index, I18n.getLanguage("cfgProcedureHardView.list.grid.mrMode") + " " + I18n
//          .getLanguage("common.notnull"));
//      return false;
//    } else {
//      if ("H".equalsIgnoreCase(dto.getMrMode())) {
//        dto.setMrMode(dto.getMrMode().toUpperCase());
//      } else {
//        setMapResult(index, I18n.getLanguage("cfgProcedureHardView.list.grid.mrMode") + " " + I18n
//            .getLanguage("notify.invalid"));
//        return false;
//      }
//    }

    //Tên thủ tục procedureName
    if (StringUtils.isStringNullOrEmpty(dto.getProcedureName())) {
      setMapResult(index,
          I18n.getLanguage("cfgProcedureHardView.list.grid.procedureName") + " " + I18n
              .getLanguage("common.notnull"));
      return false;
    } else if (dto.getProcedureName().length() > 250) {
      setMapResult(index,
          I18n.getLanguage("cfgProcedureHardITView.list.grid.procedureName.maxLength.250"));
      return false;
    }

    //Sinh MR trước(Ngày) genMrBefore
    if (StringUtils.isStringNullOrEmpty(dto.getGenMrBeforeStr())) {
      setMapResult(index,
          I18n.getLanguage("cfgProcedureHardView.list.grid.genMrBefore") + " " + I18n
              .getLanguage("common.notnull"));
      return false;
    } else if (!dto.getGenMrBeforeStr().matches(REGEX_NUMBER)) {
      setMapResult(index,
          I18n.getLanguage("cfgProcedureHardView.check.genMrBefore") + " " + I18n
              .getLanguage("cfgProcedureHardView.checkData.number"));
      return false;
    } else if (dto.getGenMrBeforeStr().matches(REGEX_NUMBER)) {
      if (Long.parseLong(dto.getGenMrBeforeStr()) > 0) {
        dto.setGenMrBefore(Long.parseLong(dto.getGenMrBeforeStr()));
      } else {
        setMapResult(index,
            I18n.getLanguage("cfgProcedureHardView.check.genMrBefore") + " " + I18n
                .getLanguage("cfgProcedureHardView.checkData.number"));
        return false;
      }
    }

    //Mã loại hoạt động mrContentId
    if (StringUtils.isStringNullOrEmpty(dto.getMrContentId())) {
      setMapResult(index,
          I18n.getLanguage("cfgProcedureHardView.list.grid.mrContentId") + " " + I18n
              .getLanguage("common.notnull"));
      return false;
    } else {
      check = false;
      List<CatItemDTO> dataMrContent = catItemBusiness
          .getListItemByCategoryAndParent(MR_ITEM_NAME.MR_TYPE, null);
      for (CatItemDTO cat : dataMrContent) {
        if (cat.getItemValue().equals(dto.getMrContentId())) {
          //set lai itemName vao DB chu ko luu bang itemValue
          dto.setMrContentId(cat.getItemName());
          check = true;
          break;
        }
      }
      if (!check) {
        setMapResult(index,
            I18n.getLanguage("cfgProcedureHardView.list.grid.mrContentId") + " " + I18n
                .getLanguage("notify.invalid"));
        return false;
      }
    }

//    Thời gian thực hiện(Ngày) mrTime
//    if (StringUtils.isStringNullOrEmpty(dto.getMrTimeStr())) {
//      setMapResult(index,
//          I18n.getLanguage("cfgProcedureHardView.list.grid.mrTime") + " " + I18n
//              .getLanguage("common.notnull"));
//      return false;
//    } else
    if (!StringUtils.isStringNullOrEmpty(dto.getMrTimeStr())) {
      if (!dto.getMrTimeStr().matches(REGEX_NUMBER)) {
        setMapResult(index,
            I18n.getLanguage("cfgProcedureHardView.check.mrTime") + " " + I18n
                .getLanguage("cfgProcedureHardView.checkData.number"));
        return false;
      } else if (dto.getMrTimeStr().matches(REGEX_NUMBER)) {
        if (Long.parseLong(dto.getMrTimeStr()) > 0) {
          dto.setMrTime(Long.parseLong(dto.getMrTimeStr()));
        } else {
          setMapResult(index,
              I18n.getLanguage("cfgProcedureHardView.check.mrTime") + " " + I18n
                  .getLanguage("cfgProcedureHardView.checkData.number"));
          return false;
        }
      }
    }

    //Mức độ ưu tiên priorityCode
    if (StringUtils.isStringNullOrEmpty(dto.getPriorityCode())) {

    } else if (!"1".equals(dto.getPriorityCode()) && !"2".equals(dto.getPriorityCode())) {
      setMapResult(index,
          I18n.getLanguage("cfgProcedureHardView.list.grid.priorityCode") + " " + I18n
              .getLanguage("cfgProcedureHardView.checkData.priorityCode"));
      return false;
    } else if ("1".equals(dto.getPriorityCode()) || "2".equals(dto.getPriorityCode())) {
      dto.setPriorityCode(
          I18n.getLanguage("cfgProcedureHardView.list.grid.level.priorityCode") + " " + dto
              .getPriorityCode());
    }

    //Tạo lại MR sau reGenMrAfter
    if (StringUtils.isStringNullOrEmpty(dto.getReGenMrAfterStr())) {

    } else if (!dto.getReGenMrAfterStr().matches(REGEX_NUMBER)) {
      setMapResult(index,
          I18n.getLanguage("cfgProcedureHardView.check.reGenMrAfter") + " " + I18n
              .getLanguage("cfgProcedureHardView.checkData.number"));
      return false;
    } else if (dto.getReGenMrAfterStr().matches(REGEX_NUMBER)) {
      if (Long.parseLong(dto.getReGenMrAfterStr()) > 0) {
        dto.setReGenMrAfter(Long.parseLong(dto.getReGenMrAfterStr()));
      } else {
        setMapResult(index,
            I18n.getLanguage("cfgProcedureHardView.check.reGenMrAfter") + " " + I18n
                .getLanguage("cfgProcedureHardView.checkData.number"));
        return false;
      }
    }

    //Mức ảnh hưởng impact
    if (StringUtils.isStringNullOrEmpty(dto.getImpact())) {

    } else if (
        !"1".equals(dto.getImpact()) && !"2".equals(dto.getImpact())) {
      setMapResult(index, I18n.getLanguage("cfgProcedureHardView.list.grid.impact") + " " + I18n
          .getLanguage("cfgProcedureHardView.checkData.impact"));
      return false;
    } else if ("1".equals(dto.getImpact()) || "2".equals(dto.getImpact())) {
      dto.setImpact(
          I18n.getLanguage("cfgProcedureHardView.list.grid.level.impact") + " " + dto.getImpact());
    }

    //Trạng thái status
    if (StringUtils.isStringNullOrEmpty(dto.getStatus())) {
      setMapResult(index,
          I18n.getLanguage("cfgProcedureHardView.list.grid.status") + " " + I18n
              .getLanguage("common.notnull"));
      return false;
    } else {
      if ("A".equalsIgnoreCase(dto.getStatus()) || "I".equalsIgnoreCase(dto.getStatus())) {
        dto.setStatus(dto.getStatus().toUpperCase());
      } else {
        setMapResult(index, I18n.getLanguage("cfgProcedureHardView.list.grid.status") + " " + I18n
            .getLanguage("cfgProcedureHardView.checkData.status"));
        return false;
      }
    }

    // Đầu việc mrWorks MR_WORKS
    if (StringUtils.isStringNullOrEmpty(dto.getMrWorks())) {
      setMapResult(index,
          I18n.getLanguage("cfgProcedureHardView.list.grid.mrWorks") + " " + I18n
              .getLanguage("common.notnull"));
      return false;
    } else {
      try {
        String arrayMrWork[] = dto.getMrWorks().split(",");
        //Lấy ra mã đầu việc theo mảng
        boolean isCheck = true;
        Long subCatItem = mapSubCat.get(dto.getArrayCode());
        String msg = "";
        if (arrayMrWork != null) {
          for (int i = 0; i < arrayMrWork.length; i++) {
            if (mapOgWork.containsKey(arrayMrWork[i].trim())) {
              CatItemDTO itemDTO = mapOgWork.get(arrayMrWork[i].trim());
              if (itemDTO != null && itemDTO.getParentItemId() != null) {
                if (!itemDTO.getParentItemId().equals(subCatItem)) {
                  isCheck = false;
                  msg += itemDTO.getItemId() + ",";
                  setMapResult(index, msg);
                }
              } else {
                isCheck = false;
                msg += arrayMrWork[i].trim() + ",";
                setMapResult(index, msg);
              }
            } else {
              setMapResult(index,
                  I18n.getLanguage("cfgProcedureHardView.list.grid.mrWorks") + " " + I18n
                      .getLanguage("notify.invalid"));
              isCheck = false;
            }
            setMapResult(index, I18n.getLanguage("cfgProcedureHardView.mrWorks.nok") + " " + msg);
          }
          if (!isCheck) {
            return false;
          }
        }
      } catch (Exception e) {
        setMapResult(index, I18n.getLanguage("cfgProcedureHardView.list.grid.mrWorks") + " " + I18n
            .getLanguage("notify.invalid"));
        return false;
      }
    }

    // Ngày hết hiệu lực expDate
    if (StringUtils.isStringNullOrEmpty(dto.getStrExpDate())) {

    } else if (!dto.getStrExpDate().matches(REGEX_DATE_MONTH_YEAR)) {
      setMapResult(index, I18n.getLanguage("cfgProcedureHardView.list.grid.strExpDate") + " " + I18n
          .getLanguage("cfgProcedureHardView.strExpDate.format"));
      return false;
    } else if (dto.getStrExpDate().matches(REGEX_DATE_MONTH_YEAR)) {
      dto.setExpDate(DateTimeUtils.convertStringToDate(dto.getStrExpDate()));
    }

    //Nội dung công việc woContent
    if (StringUtils.isStringNullOrEmpty(dto.getWoContent())) {
      setMapResult(index,
          I18n.getLanguage("cfgProcedureHardView.list.grid.woContent") + " " + I18n
              .getLanguage("common.notnull"));
      return false;
    } else if (dto.getWoContent().length() > 2000) {
      setMapResult(index, I18n.getLanguage("cfgProcedureHardView.list.grid.woContent") + " " + I18n
          .getLanguage("cfgProcedureHardITView.list.grid.woContent.maxLength.2000"));
      return false;
    }

    //Check dupplicate
    if (mrITHardProcedureRepository
        .checkDuplicateCfgProcedureITHardByDTO(dto)) {
      setMapResult(index, I18n.getLanguage("cfgProcedureHardView.validation.isDuplicate"));
      return false;
    }

    //Xử lý tìm trong DB nếu đã tồn tại thì mặc định là Update
    if (dto != null) {
      List<MrCfgProcedureITHardDTO> cfgProcedureTelDTOList = mrITHardProcedureRepository
          .checkMrCfgProcedureITHardExist(dto);
      if (cfgProcedureTelDTOList != null && cfgProcedureTelDTOList.size() > 0) {
        MrCfgProcedureITHardDTO dtoTmp = cfgProcedureTelDTOList.get(0);
        //set lai ProcedureId đã tồn tại trong Database cho DTO mới
        dto.setProcedureId(dtoTmp.getProcedureId());
      }
    }
    return true;
  }

  private String getValidateResult(Long index) {
    return mapErrorResult.get(index.toString());
  }

  @Override
  public ResultInSideDto importData(MultipartFile multipartFile) throws Exception {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    mapErrorResult.clear();
    cellErrorList.clear();
    setMapArrayByDeviceType();
    lstCountry = catLocationBusiness.getListLocationByLevelCBB(null, 1L, null);
    Map<String, CatItemDTO> mapOgWork = new HashMap<>();
    Map<String, Long> mapSubCat = new HashMap<>();
    Map<String, ItemDataCRInside> mapMarket = new HashMap<>();
    if (lstCountry != null) {
      for (ItemDataCRInside item : lstCountry) {
        mapMarket.put(String.valueOf(item.getValueStr()), item);
      }
    }
    try {
      if (multipartFile == null) {
        resultInSideDto.setKey(RESULT.FILE_IS_NULL);
        resultInSideDto.setMessage(I18n.getLanguage("common.fileEmpty"));
        return resultInSideDto;
      } else {
        String filePath = FileUtils
            .saveTempFile(multipartFile.getOriginalFilename(), multipartFile.getBytes(),
                tempFolder);
        String validate = validateFileImport(filePath);
        if (StringUtils.isStringNullOrEmpty(validate)) {
          //Lấy list Mảng, List Đầu việc
          List<CatItemDTO> dataOgWork = mrCfgProcedureCDBusiness.getOgListWorks(null);
          List<CatItemDTO> lstSubCat = mrCfgProcedureCDBusiness.getMrSubCategory();
          //xử lý map đầu việc
          if (dataOgWork != null && !dataOgWork.isEmpty()) {
            dataOgWork.forEach(i -> {
              mapOgWork.put(String.valueOf(i.getItemId()), i);
            });
          }
          //xử lý map mảng
          if (lstSubCat != null && !lstSubCat.isEmpty()) {
            lstSubCat.forEach(i -> {
              mapSubCat.put(i.getItemName(), i.getItemId());
            });
          }
          File fileImp = new File(filePath);
          List<Object[]> lstData;
          if (filePath.contains("xlsx")) {
            lstData = CommonImport.getDataFromExcelFileNew(fileImp, 0, 8,
                0, 18, 2);
          } else {
            lstData = CommonImport.getDataFromExcel(fileImp, 0, 8,
                0, 18, 2);
          }
          List<MrCfgProcedureITHardDTO> lstAddOrUpdate = new ArrayList<>();
          if (lstData != null && lstData.size() > 0) {
            for (int i = lstData.size() - 1; i >= 0; i--) {
              if (lstData.get(i)[0] == null && lstData.get(i)[1] == null
                  && lstData.get(i)[2] == null && lstData.get(i)[3] == null
                  && lstData.get(i)[4] == null && lstData.get(i)[5] == null
                  && lstData.get(i)[6] == null && lstData.get(i)[7] == null
                  && lstData.get(i)[8] == null && lstData.get(i)[9] == null
                  && lstData.get(i)[10] == null && lstData.get(i)[11] == null
                  && lstData.get(i)[12] == null && lstData.get(i)[13] == null
                  && lstData.get(i)[14] == null && lstData.get(i)[15] == null
                  && lstData.get(i)[16] == null && lstData.get(i)[17] == null
              ) {
                lstData.remove(i);
              }
            }
            if (lstData.size() <= 1000) {
              boolean allTrue = true;
              int row = 8;
              for (Object[] obj : lstData) {
                MrCfgProcedureITHardDTO dto = new MrCfgProcedureITHardDTO();
                if (obj[1] != null) {
                  dto.setMarketCode(obj[1].toString().trim());
                } else {
                  dto.setMarketCode(null);
                }
                if (obj[2] != null) {
                  dto.setArrayCode(obj[2].toString().trim());
                } else {
                  dto.setArrayCode(null);
                }

                if (obj[3] != null) {
                  dto.setDeviceType(obj[3].toString().trim());
                } else {
                  dto.setDeviceType(null);
                }
                if (obj[4] != null) {
                  dto.setCycleType(obj[4].toString().trim());
                } else {
                  dto.setCycleType(null);
                }
                if (obj[5] != null) {
                  dto.setCycleStr(obj[5].toString().trim());
                } else {
                  dto.setCycleStr(null);
                }
//                if (obj[6] != null) {
//                  dto.setMrMode(obj[6].toString().trim());
//                } else {
//                  dto.setMrMode(null);
//                }
                if (obj[6] != null) {
                  dto.setProcedureName(obj[6].toString().trim());
                } else {
                  dto.setProcedureName(null);
                }
                if (obj[7] != null) {
                  dto.setGenMrBeforeStr(obj[7].toString().trim());
                } else {
                  dto.setGenMrBeforeStr(null);
                }

                if (obj[8] != null) {
                  dto.setMrContentId(obj[8].toString().trim());
                } else {
                  dto.setMrContentId(null);
                }
                if (obj[9] != null) {
                  dto.setMrTimeStr(obj[9].toString().trim());
                } else {
                  dto.setMrTimeStr(null);
                }
                if (obj[10] != null) {
                  dto.setPriorityCode(obj[10].toString().trim());
                } else {
                  dto.setPriorityCode(null);
                }
                if (obj[11] != null) {
                  dto.setReGenMrAfterStr(obj[11].toString().trim());
                } else {
                  dto.setReGenMrAfterStr(null);
                }
                if (obj[12] != null) {
                  dto.setImpact(obj[12].toString().trim());
                } else {
                  dto.setImpact(null);
                }
                if (obj[13] != null) {
                  dto.setStatus(obj[13].toString().trim());
                } else {
                  dto.setStatus(null);
                }
                if (obj[14] != null) {
                  dto.setMrWorks(obj[14].toString().trim().replaceAll(" ", ""));
                } else {
                  dto.setMrWorks(null);
                }
                if (obj[15] != null) {
                  dto.setStrExpDate(obj[15].toString().trim());
                } else {
                  dto.setExpDate(null);
                }

                if (obj[16] != null) {
                  dto.setWoContent(obj[16].toString().trim());
                } else {
                  dto.setWoContent(null);
                }

                if (validateImportInfo(Long.valueOf(row), dto, mapMarket, mapOgWork, mapSubCat)) {
                  cellErrorList
                      .add(new ErrorInfo(row, I18n.getLanguage("common.import.validRecord")));
                } else {
                  allTrue = false;
                  cellErrorList.add(new ErrorInfo(row, getValidateResult(Long.valueOf(row))));
                }
                lstAddOrUpdate.add(dto);
                row++;
              }

              if (allTrue) {
                if (!lstAddOrUpdate.isEmpty()) {
                  int k = 0;
                  for (MrCfgProcedureITHardDTO dtoTmp : lstAddOrUpdate) {
                    if (dtoTmp.getProcedureId() == null) {
                      resultInSideDto = insertMrCfgProcedureITHard(dtoTmp);
                    } else {
                      resultInSideDto = updateMrCfgProcedureITHard(dtoTmp);
                    }
                    if (RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
                      ErrorInfo resultColumn = cellErrorList.get(k);
                      if (dtoTmp.getProcedureId() == null) {
                        resultColumn.setMsg(I18n.getLanguage("common.success1"));
                      } else {
                        resultColumn
                            .setMsg(I18n.getLanguage("cfgProcedureHardView.update.importUpdate"));
                      }
                    }
                    k++;
                  }
                } else {
                  resultInSideDto = new ResultInSideDto(null, RESULT.ERROR,
                      I18n.getLanguage("import.common.fail"));
                }
              } else {
                resultInSideDto = new ResultInSideDto(null, RESULT.ERROR,
                    I18n.getLanguage("import.common.fail"));
              }

              //>>>===Xuất file kết quả===<<<
              resultInSideDto.setFilePath(filePath);
              exportFileImport(lstData, filePath, cellErrorList);

            } else {
              resultInSideDto = new ResultInSideDto(null, RESULT.ERROR,
                  I18n.getLanguage("common.importing.maxrow"));
            }
          } else {
            resultInSideDto = new ResultInSideDto(null, RESULT.NODATA,
                I18n.getLanguage("common.searh.nodata"));
          }
        } else {
          resultInSideDto = new ResultInSideDto(null, RESULT.ERROR, validate);
        }
      }

    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return resultInSideDto;
  }

  public void exportFileImport(List<Object[]> lstData, String fileTempPath,
      List<ErrorInfo> lstError) {
    try {
      ExcelWriterUtils ewu = new ExcelWriterUtils();
      Workbook workBook = processFileTemplate(ewu);
      Sheet sheetOne = workBook.getSheetAt(0);
      //insert data from line i + 1
      int i = 8;
//      CellStyle cellStResult = sheetOne.getRow(6).getCell(0).getCellStyle();
      CellStyle cellStyleHeader = workBook.createCellStyle();
      Row row = sheetOne.getRow(6);
      Cell cell = row.createCell(17);

      Row row1 = sheetOne.getRow(7);
      Cell cell1 = row1.createCell(17);

      sheetOne.addMergedRegion(new CellRangeAddress(6, 7, 17, 17));
      cellStyleHeader = sheetOne.getRow(7).getCell(0).getCellStyle();
      cellStyleHeader.setBorderLeft(BorderStyle.THIN);
      cellStyleHeader.setBorderBottom(BorderStyle.THIN);
      cellStyleHeader.setBorderRight(BorderStyle.THIN);
      cellStyleHeader.setBorderTop(BorderStyle.THIN);
      cell1.setCellStyle(cellStyleHeader);
      cell.setCellStyle(cellStyleHeader);
      cell.setCellValue(I18n.getLanguage("cfgProcedureHardView.list.grid.importError"));

      for (Object[] c_row : lstData) {
        if (c_row[8] == null && c_row[1] == null && c_row[2] == null && c_row[3] == null
            && c_row[4] == null && c_row[5] == null && c_row[6] == null && c_row[7] == null) {
          break;
        }
        for (int j = 0; j < 17; j++) {
          ewu.createCell(sheetOne, j, i, c_row[j] == null ? "" : c_row[j].toString().trim(), null);
        }
        i++;
      }
      for (ErrorInfo err : lstError) {
        row = sheetOne.getRow(err.getRow());
        cell = row.createCell(17);
        cell.setCellValue(err.getMsg());
      }
      sheetOne.autoSizeColumn(17, true);
      FileOutputStream fileOut = null;
      try {

        // R3292_EDIT_DUNGNV50_13122012_START
        fileOut = new FileOutputStream(fileTempPath);
        // R3292_EDIT_DUNGNV50_13122012_END
        workBook.write(fileOut);
      } catch (Exception ex) {
        log.error(ex.getMessage(), ex);
      } finally {
        try {
          if (fileOut != null) {
            fileOut.close();
          }
          workBook.close();
        } catch (IOException ex) {
          log.error(ex.getMessage(), ex);
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

  @Override
  public File exportData(MrCfgProcedureITHardDTO mrCfgProcedureITHardDTO) throws Exception {
    log.debug("Request to exportData: {}", mrCfgProcedureITHardDTO);
    List<MrCfgProcedureITHardDTO> lstData = mrITHardProcedureRepository
        .getListDataExport(mrCfgProcedureITHardDTO);
    return exportFileEx(lstData, "exportData");
  }

  public File exportFileEx(List<MrCfgProcedureITHardDTO> lstData, String key) throws Exception {
    String title = "";
    String fileNameOut = "";
    String subTitle = I18n
        .getLanguage("MrCfgProcedureITHard.export.exportDate", DateTimeUtils.convertDateOffset());
    List<ConfigFileExport> fileExports = new ArrayList<>();
    List<ConfigHeaderExport> lstHeaderSheet = new ArrayList<>();
    if (Constants.RESULT_IMPORT.equals(key)) {
      title = I18n.getLanguage("MrCfgProcedureITHard.import.title");
      fileNameOut = MR_PROCEDURE_IT_HARD_RESULT_IMPORT;
    } else {
      title = I18n.getLanguage("MrCfgProcedureITHard.export.title");
      fileNameOut = MR_PROCEDURE_IT_HARD_EXPORT;
      lstHeaderSheet = readerHeaderSheet("marketName", "arrayCodeName", "deviceType", "cycle",
          "cycleTypeName", "mrModeName", "procedureName", "genMrBefore", "mrContentId", "mrTime",
          "priorityCode", "reGenMrAfterStr", "impact", "statusName", "mrWorksName", "expDateStr",
          "woContent");
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
        "language.MrCfgProcedureITHard",
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

  private List<ConfigHeaderExport> readerHeaderSheet(String... col) {
    List<ConfigHeaderExport> lstHeaderSheet = new ArrayList<>();
    for (int i = 0; i < col.length; i++) {
      lstHeaderSheet.add(new ConfigHeaderExport(col[i], "LEFT", false, 0, 0, new String[]{}, null,
          "STRING"));
    }
    return lstHeaderSheet;
  }

  @Override
  public File getTemplate() {
    Workbook workBook = null;
    ExcelWriterUtils ewu = new ExcelWriterUtils();
    String pathFolder =
        tempFolder + File.separator + FileUtils.createPathByDate(new Date()) + File.separator;
    try {
      workBook = processFileTemplate(ewu);
      ewu.saveToFileExcel(workBook, pathFolder, getFileTemplateName());
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    } finally {
      if (workBook != null) {
        try {
          workBook.close();
        } catch (IOException ex) {
          log.error(ex.getMessage(), ex);
        }
      }
    }
    return new File(pathFolder + getFileTemplateName());
  }

  private String getFileTemplateName() {
    String language = I18n.getLocale();
    String fileTemplateName = "IMPORT_CFG_PROCEDURE_IT_HARD_TEMPLATE_EN.xlsx";
    if (language != null && (language.toLowerCase().contains("vi") || language.toLowerCase()
        .contains("vi_VN"))) {
      fileTemplateName = "IMPORT_CFG_PROCEDURE_IT_HARD_TEMPLATE_VI.xlsx";
    }
    return fileTemplateName;
  }

  public Workbook processFileTemplate(ExcelWriterUtils ewu) throws Exception {
    Workbook workBook;
//    Quốc gia
    lstCountry = catLocationBusiness.getListLocationByLevelCBB(null, 1L, null);
    String pathFileImport = pathTemplate + getFileTemplateName();
    workBook = ewu.readFileExcelFromTemplate(pathFileImport);
    Sheet sheetTwo = workBook.getSheetAt(1);
    int k = 2;
    int l = 1;
    CellStyle cellSt1 = null;
    for (ItemDataCRInside rowS2 : lstCountry) {
      if (rowS2 == null) {
        break;
      }
      ewu.createCell(sheetTwo, 0, k, String.valueOf(l), cellSt1);
      ewu.createCell(sheetTwo, 1, k,
          rowS2.getValueStr() == null ? "" : String.valueOf(rowS2.getValueStr()).trim(), cellSt1);
      ewu.createCell(sheetTwo, 2, k,
          rowS2.getDisplayStr() == null ? "" : rowS2.getDisplayStr().trim(), cellSt1);
      k++;
      l++;
    }

    //Mảng
    List<CatItemDTO> data = mrCfgProcedureCDBusiness.getMrSubCategory();
    Sheet sheetThree = workBook.getSheetAt(2);
    k = 2;
    l = 1;
    CellStyle cellSt2 = null;
    for (CatItemDTO array : data) {
      if (array == null) {
        break;
      }
      ewu.createCell(sheetThree, 0, k, String.valueOf(l), cellSt2);
      ewu.createCell(sheetThree, 1, k, array.getItemName() == null ? "" : array.getItemName(),
          cellSt2);
      k++;
      l++;
    }

    //Tao tieu de
    Font xssFontTitle = workBook.createFont();
    xssFontTitle.setFontName("Times New Roman");
    xssFontTitle.setFontHeightInPoints((short) 22);
    xssFontTitle.setColor(IndexedColors.BLACK.index);
    xssFontTitle.setBold(true);

    //set font chữ cho header
    Font xSSFFontHeader = workBook.createFont();
    xSSFFontHeader.setFontName("Times New Roman");
    xSSFFontHeader.setFontHeightInPoints((short) 12);
    xSSFFontHeader.setColor(IndexedColors.BLACK.index);
    xSSFFontHeader.setBold(true);

    CellStyle cellStyleHeader = workBook.createCellStyle();
    cellStyleHeader.setAlignment(HorizontalAlignment.CENTER);
    cellStyleHeader.setVerticalAlignment(VerticalAlignment.CENTER);
    cellStyleHeader.setBorderLeft(BorderStyle.THIN);
    cellStyleHeader.setBorderBottom(BorderStyle.THIN);
    cellStyleHeader.setBorderRight(BorderStyle.THIN);

    HSSFWorkbook hwb = null;
    hwb = new HSSFWorkbook();
    HSSFPalette palette = hwb.getCustomPalette();
    //set màu cho header
    HSSFColor myColor = palette
        .findSimilarColor(Integer.valueOf(146), Integer.valueOf(208),
            Integer.valueOf(80));
    cellStyleHeader.setBorderTop(BorderStyle.THIN);
    cellStyleHeader.setFillForegroundColor(myColor.getIndex());
    cellStyleHeader.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    cellStyleHeader.setWrapText(true);
    cellStyleHeader.setFont(xSSFFontHeader);

    //Loại thiết bị
    //phai tao thêm các sheet trong code. vi tao trong template se bi overload data read excel
    Sheet sheetFive = workBook
        .createSheet(I18n.getLanguage("cfgProcedureHardView.list.grid.sheetDeviceType"));
    //set heigh cho thanh header
    Row rowHeadStyle5 = sheetFive.createRow(1);
    rowHeadStyle5.setHeightInPoints(25);
    List<MrSynItDevicesDTO> listDeviceType = mrITHardProcedureRepository.getListDeviceType();
    k = 2;
    l = 1;
    CellStyle cellSt4 = null;
    ewu.createCell(sheetFive, 0, k - 1, I18n.getLanguage("common.list.grid.stt"), cellStyleHeader);
    ewu.createCell(sheetFive, 1, k - 1,
        I18n.getLanguage("cfgProcedureHardView.list.grid.sheetDeviceType.arrayCode"),
        cellStyleHeader);
    ewu.createCell(sheetFive, 2, k - 1,
        I18n.getLanguage("cfgProcedureHardView.list.grid.sheetDeviceType.deviceType"),
        cellStyleHeader);
    for (int i = 0; i <= 2; i++) {
      sheetFive.autoSizeColumn(i);
      sheetFive.setColumnWidth(1, 10000);
      sheetFive.setColumnWidth(2, 10000);
    }

    for (MrSynItDevicesDTO mrDeviceDTO : listDeviceType) {
      if (mrDeviceDTO == null) {
        break;
      }
      ewu.createCell(sheetFive, 0, k, String.valueOf(l), cellSt4);
      ewu.createCell(sheetFive, 1, k,
          mrDeviceDTO.getArrayCode() == null ? "" : mrDeviceDTO.getArrayCode(), cellSt4);
      ewu.createCell(sheetFive, 2, k,
          mrDeviceDTO.getDeviceType() == null ? "" : mrDeviceDTO.getDeviceType(), cellSt4);
      k++;
      l++;
    }

    //Loại hoạt động
    Sheet sheetSix = workBook
        .createSheet(I18n.getLanguage("cfgProcedureHardView.list.grid.sheetMrContent"));
    //set heigh cho thanh header
    Row rowHeadStyle6 = sheetSix.createRow(1);
    rowHeadStyle6.setHeightInPoints(25);
    List<CatItemDTO> dataMrContent = catItemBusiness
        .getListItemByCategoryAndParent(MR_ITEM_NAME.MR_TYPE, null);
    k = 2;
    l = 1;
    CellStyle cellSt5 = null;
    ewu.createCell(sheetSix, 0, k - 1, I18n.getLanguage("common.list.grid.stt"), cellStyleHeader);
    ewu.createCell(sheetSix, 1, k - 1,
        I18n.getLanguage("cfgProcedureHardView.list.grid.sheetMrContent.mrContentID"),
        cellStyleHeader);
    ewu.createCell(sheetSix, 2, k - 1,
        I18n.getLanguage("cfgProcedureHardView.list.grid.sheetMrContent.mrContentName"),
        cellStyleHeader);
    for (int i = 0; i <= 2; i++) {
      sheetSix.autoSizeColumn(i);
      sheetSix.setColumnWidth(1, 15000);
      sheetSix.setColumnWidth(2, 15000);
    }
    for (CatItemDTO mrContent : dataMrContent) {
      if (mrContent == null) {
        break;
      }
      ewu.createCell(sheetSix, 0, k, String.valueOf(l), cellSt5);
      ewu.createCell(sheetSix, 1, k,
          mrContent.getItemValue() == null ? "" : mrContent.getItemValue(), cellSt5);
      ewu.createCell(sheetSix, 2, k, mrContent.getItemName() == null ? "" : mrContent.getItemName(),
          cellSt5);
      k++;
      l++;
    }

    //Đầu việc
    Sheet sheetSeven = workBook
        .createSheet(I18n.getLanguage("cfgProcedureHardView.list.grid.sheetMrWork"));
    //set heigh cho thanh header
    Row rowHeadStyle7 = sheetSeven.createRow(1);
    rowHeadStyle7.setHeightInPoints(25);
    k = 2;
    l = 1;
    CellStyle cellSt6 = null;
    ewu.createCell(sheetSeven, 0, k - 1, I18n.getLanguage("common.list.grid.stt"), cellStyleHeader);
    ewu.createCell(sheetSeven, 1, k - 1,
        I18n.getLanguage("cfgProcedureHardView.list.grid.sheetDeviceType.arrayCode"),
        cellStyleHeader);
    ewu.createCell(sheetSeven, 2, k - 1,
        I18n.getLanguage("cfgProcedureHardView.list.grid.sheetMrContent.mrWorkCode"),
        cellStyleHeader);
    ewu.createCell(sheetSeven, 3, k - 1,
        I18n.getLanguage("cfgProcedureHardView.list.grid.sheetMrContent.mrWorkName"),
        cellStyleHeader);
    for (int i = 0; i <= 3; i++) {
      sheetSeven.autoSizeColumn(i);
      sheetSeven.setColumnWidth(1, 20000);
      sheetSeven.setColumnWidth(2, 5000);
      sheetSeven.setColumnWidth(3, 30000);
    }
    //Lấy danh sách mảng
    List<CatItemDTO> lstDataArrayCode = mrCfgProcedureCDBusiness.getMrSubCategory();
    Long itemId = null;
    if (lstDataArrayCode != null) {
      for (CatItemDTO dtoArray : lstDataArrayCode) {
        //Duyệt từng array lấy ra list đầu việc tương ứng theo ItemId
        itemId = dtoArray.getItemId();
        List<CatItemDTO> dataWorkByArray = mrCfgProcedureCDBusiness.getOgListWorks(itemId);
        for (CatItemDTO dto : dataWorkByArray) {
          if (dto == null) {
            break;
          }
          ewu.createCell(sheetSeven, 0, k, String.valueOf(l), cellSt6);
          ewu.createCell(sheetSeven, 1, k,
              dtoArray.getItemValue() == null ? "" : dtoArray.getItemValue(), cellSt6);
          ewu.createCell(sheetSeven, 2, k,
              dto.getItemId() == null ? "" : dto.getItemId().toString(), cellSt6);
          ewu.createCell(sheetSeven, 3, k, dto.getItemValue() == null ? "" : dto.getItemValue(),
              cellSt6);
          k++;
          l++;
        }
      }
    }
    return workBook;
  }
}
