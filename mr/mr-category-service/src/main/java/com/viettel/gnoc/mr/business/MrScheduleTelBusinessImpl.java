package com.viettel.gnoc.mr.business;

import com.viettel.gnoc.commons.business.CatLocationBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.commons.proxy.CrServiceProxy;
import com.viettel.gnoc.commons.proxy.WoCategoryServiceProxy;
import com.viettel.gnoc.commons.proxy.WoServiceProxy;
import com.viettel.gnoc.commons.repository.UserRepository;
import com.viettel.gnoc.commons.utils.CellConfigExport;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.ConfigFileExport;
import com.viettel.gnoc.commons.utils.ConfigHeaderExport;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.CR_NODE_TYPE;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.DateUtil;
import com.viettel.gnoc.commons.utils.ExcelWriterUtils;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.CrHisDTO;
import com.viettel.gnoc.cr.dto.CrImpactedNodesDTO;
import com.viettel.gnoc.cr.dto.CrInsiteDTO;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.maintenance.dto.MrCfgProcedureTelDTO;
import com.viettel.gnoc.maintenance.dto.MrConfigDTO;
import com.viettel.gnoc.maintenance.dto.MrDeviceDTO;
import com.viettel.gnoc.maintenance.dto.MrInsideDTO;
import com.viettel.gnoc.maintenance.dto.MrScheduleTelDTO;
import com.viettel.gnoc.maintenance.dto.MrScheduleTelHisDTO;
import com.viettel.gnoc.mr.repository.MrCfgProcedureTelRepository;
import com.viettel.gnoc.mr.repository.MrDeviceCDRepository;
import com.viettel.gnoc.mr.repository.MrDeviceRepository;
import com.viettel.gnoc.mr.repository.MrRepository;
import com.viettel.gnoc.mr.repository.MrScheduleTelHisRepository;
import com.viettel.gnoc.mr.repository.MrScheduleTelRepository;
import com.viettel.gnoc.mr.repository.MrServiceRepository;
import com.viettel.gnoc.wo.dto.WoCdGroupInsideDTO;
import com.viettel.gnoc.wo.dto.WoCdGroupTypeUserDTO;
import com.viettel.gnoc.wo.dto.WoDTOSearch;
import com.viettel.gnoc.wo.dto.WoInsideDTO;
import com.viettel.gnoc.wo.dto.WoTypeInsideDTO;
import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
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
@Slf4j
@Transactional
public class MrScheduleTelBusinessImpl implements MrScheduleTelBusiness {

  @Value("${application.temp.folder}")
  private String tempFolder;

  @Value("${application.upload.folder}")
  private String uploadFolder;

  HashMap<String, String> mapMrConfirm = new HashMap<>();
  private final static String MR_SCHEDULE_TEL_HARD_EXPORT = "MR_SCHEDULE_TEL_HARD_EXPORT";
  private final static String MR_SCHEDULE_TEL__HARD_RESULT_IMPORT = "MR_SCHEDULE_TEL__HARD_RESULT_IMPORT";
  private final static String Mr_SCHEDULE_TEL_SOFT_EXPORT = "Mr_SCHEDULE_TEL_SOFT_EXPORT";
  private final static String Mr_SCHEDULE_TEL_SOFT_RESULT_IMPORT = "Mr_SCHEDULE_TEL_SOFT_RESULT_IMPORT";
  private static final String MR_TYPE_HARD = "H";
  private static final String MR_TYPE_SOFT = "S";
  private final static String MR_CONFIG_TYPE = "UPDATE_MR";
  Map<Long, MrScheduleTelDTO> mapDataImport = new HashMap<>();
  @Autowired
  private MrScheduleTelRepository mrScheduleTelRepository;

  @Autowired
  private MrDeviceRepository mrDeviceRepository;

  @Autowired
  private MrScheduleTelHisRepository mrScheduleTelHisRepository;

  @Autowired
  private MrCfgProcedureTelRepository mrCfgProcedureTelRepository;

  @Autowired
  TicketProvider ticketProvider;

  @Autowired
  protected MrCfgProcedureCDBusiness mrCfgProcedureCDBusiness;

  @Autowired
  WoCategoryServiceProxy woCategoryServiceProxy;

  @Autowired
  MrDeviceCDRepository mrDeviceCDRepository;

  @Autowired
  CatLocationBusiness catLocationBusiness;

  @Autowired
  UserRepository userRepository;

  @Autowired
  CrServiceProxy crServiceProxy;

  @Autowired
  MrRepository mrRepository;

  @Autowired
  MrDeviceBusiness mrDeviceBusiness;

  @Autowired
  MrServiceRepository mrServiceRepository;

  @Autowired
  WoServiceProxy woServiceProxy;

  @Override
  public Datatable getListMrScheduleTel(MrScheduleTelDTO mrScheduleTelDTO) {
    log.debug("Request to getListMrScheduleTel: {}", mrScheduleTelDTO);
    mrScheduleTelDTO.setMrHard("1");
    Datatable datatable = mrScheduleTelRepository
        .getListMrScheduleTel(mrScheduleTelDTO, MR_TYPE_HARD);
    List<MrScheduleTelDTO> lst = (List<MrScheduleTelDTO>) datatable.getData();
    datatable.setData(resultList(lst, "H"));
    return datatable;
  }

  @Override
  public Datatable onSearchScheduleTel(MrScheduleTelDTO mrScheduleTelDTO) {
    log.debug("Request to onSearch: {}", mrScheduleTelDTO);
    mrScheduleTelDTO.setMrSoft("1");
    Datatable datatable = mrScheduleTelRepository
        .getListMrScheduleTel(mrScheduleTelDTO, MR_TYPE_SOFT);
    List<MrScheduleTelDTO> lst = (List<MrScheduleTelDTO>) datatable.getData();
    datatable.setData(resultList(lst, MR_TYPE_SOFT));
    return datatable;
  }

  @Override
  public MrScheduleTelDTO getDetail(Long id, String type) {
    log.debug("Request to getDetail: {}", id);
    return mrScheduleTelRepository.getDetail(id, type);
  }

  @Override
  public File exportData(MrScheduleTelDTO mrScheduleTelDTO) throws Exception {
    log.debug("Request to mrScheduleTelDTO: {}", mrScheduleTelDTO);
    mrScheduleTelDTO.setMrHard("1");
    List<MrScheduleTelDTO> lst = mrScheduleTelRepository
        .getDataExport(mrScheduleTelDTO, MR_TYPE_HARD);
    return exportFileEx(resultList(lst, "H"), "EXPORT_DATA", MR_TYPE_HARD);
  }

  @Override
  public ResultInSideDto updateMrScheduleTelDTO(MrScheduleTelDTO mrScheduleTelDTO) {
    log.debug("Request to updateMrScheduleTel: {}", mrScheduleTelDTO);
    UserToken userToken = ticketProvider.getUserToken();
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.ERROR);
    ResultInSideDto vadidateOk = new ResultInSideDto();
    vadidateOk.setKey(RESULT.SUCCESS);
    MrScheduleTelDTO objUpdate = getDetail(mrScheduleTelDTO.getScheduleId(), MR_TYPE_HARD);
    if (StringUtils.isStringNullOrEmpty(objUpdate.getMrHardCycle()) && StringUtils
        .isNotNullOrEmpty(mrScheduleTelDTO.getCycle())) {
      objUpdate.setMrHardCycle(Long.parseLong(mrScheduleTelDTO.getCycle()));
    }

    //dung edit 09/03/2020 không cho phép cập nhật khi đã có MrId
    if (!StringUtils.isStringNullOrEmpty(objUpdate.getMrId())) {
      resultInSideDto.setMessage(I18n.getValidation("mrScheduleTel.mrId.removeConfirm"));
      return resultInSideDto;
    } else if (StringUtils.isStringNullOrEmpty(mrScheduleTelDTO.getMrConfirm())) {
      mrScheduleTelDTO.setMrConfirm(null);
      vadidateOk = validate(mrScheduleTelDTO, false);
    }
    if (vadidateOk.getKey().equals(RESULT.SUCCESS)) {
      MrScheduleTelHisDTO scheduleHisDTO = new MrScheduleTelHisDTO();
      if (!StringUtils.isStringNullOrEmpty(mrScheduleTelDTO.getMrConfirm())) {
        MrDeviceDTO dtoMD = mrDeviceRepository.findMrDeviceById(objUpdate.getDeviceId());
        //update ly do khong bao duong xoa lich
//        if (!"5".equals(mrScheduleTelDTO.getMrConfirm())) {
        mrScheduleTelRepository.deleteMrScheduleTel(mrScheduleTelDTO.getScheduleId());
        dtoMD.setMrConfirmHard(mrScheduleTelDTO.getMrConfirm());
        dtoMD.setMrHard("0");
        if ("M".equals(objUpdate.getCycleType())) {
          if ("1".equals(objUpdate.getCycle())) {
            dtoMD.setIsComplete1m(1L);
          } else if ("3".equals(objUpdate.getCycle())) {
            dtoMD.setIsComplete3m(1L);
          } else if ("6".equals(objUpdate.getCycle())) {
            dtoMD.setIsComplete6m(1L);
          } else if ("12".equals(objUpdate.getCycle())) {
            dtoMD.setIsComplete12m(1L);
          }
//          }
          objUpdate.setMrComment(mrScheduleTelDTO.getMrComment());
          insertMrScheduleTelHis(scheduleHisDTO, objUpdate, mrScheduleTelDTO, MR_TYPE_HARD);
        }
        dtoMD.setUpdateUser(userToken.getUserName());
        dtoMD.setUpdateDate(new Date());

        resultInSideDto = mrDeviceRepository.updateMrDeviceServices(dtoMD);
        if (RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
          resultInSideDto.setMessage(I18n.getValidation("common.update.success"));
        } else {
          resultInSideDto.setKey(RESULT.ERROR);
          resultInSideDto.setMessage(I18n.getValidation("common.update.fail"));
        }

      } else {
        objUpdate.setNextDateModify(mrScheduleTelDTO.getNextDateModify());
        objUpdate.setMrComment(mrScheduleTelDTO.getMrComment());
        objUpdate.setUpdatedDate(new Date());
        resultInSideDto = mrScheduleTelRepository.updateMrScheduleTel(objUpdate);
        if (RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
          MrDeviceDTO dtoMD = mrDeviceRepository.findMrDeviceById(objUpdate.getDeviceId());
          dtoMD.setUpdateUser(userToken.getUserName());
          dtoMD.setUpdateDate(new Date());
          mrDeviceRepository.updateMrDeviceServices(dtoMD);
          resultInSideDto.setMessage(I18n.getValidation("common.update.success"));
          return resultInSideDto;
        } else {
          resultInSideDto.setKey(RESULT.ERROR);
          resultInSideDto.setMessage(I18n.getValidation("common.update.fail"));
          return resultInSideDto;
        }
      }
    }
    return vadidateOk;
  }

  public void insertMrScheduleTelHis(MrScheduleTelHisDTO scheduleHisDTO,
      MrScheduleTelDTO objUpdate, MrScheduleTelDTO mrScheduleTelDTO, String type) {
    //<editor-fold desc="duongnt edit Jun 27 2019 ||  Insert MR_SCHEDULE_TEL_HIS)" defaultstate="collapsed">
    MrInsideDTO mrInsideDTO = new MrInsideDTO();
    if (!StringUtils.isStringNullOrEmpty(objUpdate.getMrId())) {
      mrInsideDTO = mrScheduleTelRepository.findMrById(objUpdate.getMrId());
    }
    MrCfgProcedureTelDTO procedureTelDTO = mrCfgProcedureTelRepository
        .findMrCfgProcedureTelById(objUpdate.getProcedureId());
    if (procedureTelDTO == null) {
      procedureTelDTO = new MrCfgProcedureTelDTO();
    }
    scheduleHisDTO.setMarketCode(objUpdate.getMarketCode());
    scheduleHisDTO.setArrayCode(objUpdate.getArrayCode());
    scheduleHisDTO.setDeviceType(objUpdate.getDeviceType());
    scheduleHisDTO.setDeviceId(
        objUpdate.getDeviceId() != null ? String.valueOf(objUpdate.getDeviceId()) : null);
    scheduleHisDTO.setDeviceCode(objUpdate.getDeviceCode());
    scheduleHisDTO.setDeviceName(objUpdate.getDeviceName());
    if (!StringUtils.isStringNullOrEmpty(objUpdate.getNextDateModify())) {
      try {
        scheduleHisDTO
            .setMrDate(DateTimeUtils.convertStringToDateTime(objUpdate.getNextDateModify()));
      } catch (Exception e) {
        log.error(e.getMessage());
      }
    }
    scheduleHisDTO.setMrContent(procedureTelDTO.getMrContentId());
    scheduleHisDTO.setMrType(mrInsideDTO.getMrType());
    scheduleHisDTO
        .setMrId(objUpdate.getMrId() != null ? String.valueOf(objUpdate.getMrId()) : null);
    scheduleHisDTO.setMrCode(mrInsideDTO.getMrCode());
    scheduleHisDTO
        .setWoId(objUpdate.getWoId() != null ? String.valueOf(objUpdate.getWoId()) : null);
    scheduleHisDTO.setProcedureId(
        objUpdate.getProcedureId() != null ? String.valueOf(objUpdate.getProcedureId()) : null);
    scheduleHisDTO.setProcedureName(procedureTelDTO.getProcedureName());
    scheduleHisDTO.setNetworkType(objUpdate.getNetworkType());
    scheduleHisDTO.setCycle(objUpdate.getCycle());
    scheduleHisDTO.setRegion(objUpdate.getRegion());
    scheduleHisDTO.setTitle(objUpdate.getMrComment());
    scheduleHisDTO
        .setCrId(objUpdate.getCrId() == null ? null : String.valueOf(objUpdate.getCrId()));
    if (type.equals(MR_TYPE_SOFT)) {
      if (!StringUtils.isStringNullOrEmpty(mrScheduleTelDTO.getMrConfirm())) {
        scheduleHisDTO.setNote(I18n.getLanguage("mrScheduleTelhis.note.mrConfirmNotEqualFive"));
      }
      scheduleHisDTO.setMrMode("S");
    } else {
      if (!StringUtils.isStringNullOrEmpty(mrScheduleTelDTO.getMrConfirm())) {
        scheduleHisDTO.setNote(I18n.getLanguage("mrScheduleTelhis.note.mrConfirm"));
      }
      scheduleHisDTO.setMrMode("H");
    }
    mrScheduleTelHisRepository.insertScheduleHis(scheduleHisDTO);
//      </editor-fold>
  }

  public ResultInSideDto validate(MrScheduleTelDTO dto, boolean isAdd) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.ERROR);
    if (StringUtils.isStringNullOrEmpty(dto.getNextDateModify())) {
      resultInSideDto.setMessage(
          I18n.getValidation("mrScheduleTel.list.nextDateModify.null"));
      return resultInSideDto;
    } else {
      Date newDateModify = dto.getNextDateModify();
      if (DateUtil.getYY(newDateModify) < DateUtil.getYY(new Date())) {
        resultInSideDto.setMessage(I18n.getValidation("mrScheduleTel.erro.nextDateModify.invalid"));
        return resultInSideDto;
      } else if (DateUtil.getYY(newDateModify) == DateUtil.getYY(new Date())) {
        // Neu thang sau khi sua < thang hien tai
        if (DateUtil.getMonth(newDateModify) < DateUtil.getMonth(new Date())) {
          resultInSideDto
              .setMessage(I18n.getValidation("mrScheduleTel.erro.nextDateModify.invalid"));
          return resultInSideDto;
        } else if (DateUtil.getMonth(newDateModify) == DateUtil.getMonth(new Date())) {
          // Neu ngay sau khi sua < ngay hien tai
          if (DateUtil.getDayInMonth(newDateModify) < DateUtil.getDayInMonth(new Date())) {
            resultInSideDto
                .setMessage(I18n.getValidation("mrScheduleTel.erro.nextDateModify.invalid"));
            return resultInSideDto;
          }
        }
      }
    }
    if (dto.getMrComment() != null && dto.getMrComment().length() > 1000) {
      resultInSideDto.setMessage(I18n.getValidation("mrScheduleTel.mrComment.tooLong"));
      return resultInSideDto;
    }
    resultInSideDto.setKey(RESULT.SUCCESS);
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto deleteMrScheduleTel(Long scheduleId) {
    log.debug("Request to deleteMrScheduleTel: {}", scheduleId);
    ResultInSideDto resultInSideDto = new ResultInSideDto(null, RESULT.ERROR, RESULT.ERROR);
    boolean checkDel = false;
    MrScheduleTelDTO dtoOld = getDetail(scheduleId, MR_TYPE_HARD);
    if (dtoOld != null) {
      if (StringUtils.isStringNullOrEmpty(dtoOld.getMrId()) && StringUtils
          .isStringNullOrEmpty(dtoOld.getWoId())) {
        if (StringUtils.isNotNullOrEmpty(dtoOld.getMrHard()) && dtoOld.getMrHard().equals("1")) {
          checkDel = true;
        }
      }
      MrDeviceDTO dtoMD = mrDeviceRepository.findMrDeviceById(dtoOld.getDeviceId());
      if ("M".equals(dtoOld.getCycleType())) {
        if ("1".equals(dtoOld.getCycle())) {
          dtoMD.setIsComplete1m(1L);
        } else if ("3".equals(dtoOld.getCycle())) {
          dtoMD.setIsComplete3m(1L);
        } else if ("6".equals(dtoOld.getCycle())) {
          dtoMD.setIsComplete6m(1L);
        } else if ("12".equals(dtoOld.getCycle())) {
          dtoMD.setIsComplete12m(1L);
        }
      }
      if (checkDel) {
        resultInSideDto = mrScheduleTelRepository.deleteMrScheduleTel(dtoOld.getScheduleId());
        mrDeviceRepository.updateMrDeviceServices(dtoMD);
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
  public List<MrConfigDTO> getConfigByGroup(String configGroup) {
    log.debug("Request to getConfigByGroup: {}", configGroup);
    return mrScheduleTelRepository.getConfigByGroup(configGroup);
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
//    XSSFSheet sheetParam1 = workbook.createSheet("param1");
    XSSFSheet sheetParam2 = workbook.createSheet("param2");
    //Tạo 1 mảng lưu header từng cột
    String[] header;
    //Tiêu đề đánh dấu *
    String[] headerStar;
    if ("H".equals(type)) {
      header = new String[]{
          I18n.getLanguage("common.STT"),
          I18n.getLanguage("MrScheduleTel.scheduleId"),
          I18n.getLanguage("MrScheduleTel.nationName"),
//          I18n.getLanguage("MrScheduleTel.region"),
          I18n.getLanguage("MrScheduleTel.arrayCode"),
          I18n.getLanguage("MrScheduleTel.networkType"),
          I18n.getLanguage("MrScheduleTel.deviceType"),
          I18n.getLanguage("MrScheduleTel.deviceCode"),
          I18n.getLanguage("MrScheduleTel.deviceName"),
          I18n.getLanguage("MrScheduleTel.nodeIp"),
          I18n.getLanguage("MrScheduleTel.vendor"),
          I18n.getLanguage("MrScheduleTel.mrId"),
          I18n.getLanguage("MrScheduleTel.woCode"),
          I18n.getLanguage("MrScheduleTel.coordinatingUnitHardName"),
          I18n.getLanguage("MrScheduleTel.cycle"),
          I18n.getLanguage("MrScheduleTel.cycleTypeName"),
          I18n.getLanguage("MrScheduleTel.woContent"),
          I18n.getLanguage("MrScheduleTel.lastDateStr"),
          I18n.getLanguage("MrScheduleTel.nextDateStr"),
          I18n.getLanguage("MrScheduleTel.nextDateModifyStr"),
          I18n.getLanguage("MrScheduleTel.updatedDateStr"),
          I18n.getLanguage("MrScheduleTel.scheduleType"),
          I18n.getLanguage("MrScheduleTel.station"),
          I18n.getLanguage("MrScheduleTel.userMrHard"),
          I18n.getLanguage("MrScheduleTel.mrConfirmName"),
          I18n.getLanguage("MrScheduleTel.mrComment")
      };

      headerStar = new String[]{
          I18n.getLanguage("MrScheduleTel.scheduleId"),
          I18n.getLanguage("MrScheduleTel.nextDateModifyStr")
      };
    } else {
      header = getHeaderSoft();
      //Tiêu đề đánh dấu *
      headerStar = new String[]{
          I18n.getLanguage("MrScheduleTel.scheduleId"),
          I18n.getLanguage("MrScheduleTel.nextDateModifyStr")
      };
    }

    XSSFDataValidationHelper dataValidationHelper = new XSSFDataValidationHelper(sheetOne);
    List<String> listHeader = Arrays.asList(header);
    List<String> listHeaderStar = Arrays.asList(headerStar);

    int marketCodeStrColumn = listHeader
        .indexOf(I18n.getLanguage("MrScheduleTel.nationName"));
    int arrayCodeStrColumn = listHeader.indexOf(I18n.getLanguage("MrScheduleTel.arrayCode"));
    int mrConfirmNameColumn = listHeader.indexOf(I18n.getLanguage("MrScheduleTel.mrConfirmName"));
//    int stationCodeStrColumn = listHeader
//        .indexOf(I18n.getLanguage("MrScheduleTel.station"));
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
    titleCell.setCellValue("H".equals(type) ? I18n.getLanguage("MrScheduleTel.title")
        : I18n.getLanguage("MrScheduleTel.title.soft"));
    titleCell.setCellStyle(styleTitle);

    XSSFFont starFont = workbook.createFont();
    starFont.setColor(IndexedColors.RED.getIndex());

    //Tạo Header

    Row headerRow = sheetOne.createRow(7);
//    Row headerRegion = sheetParam1.createRow(0);
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
      headerCell.setCellValue(richTextString);
      headerCell.setCellStyle(cellStyleHeader);
      sheetOne.setColumnWidth(i, 7000);
    }
//    Cell headerCellMarketCode = headerRegion.createCell(0);
//    Cell headerCellRegion = headerRegion.createCell(1);
//    XSSFRichTextString market = new XSSFRichTextString(
//        I18n.getLanguage("MrScheduleTel.nationName"));
//    XSSFRichTextString reg = new XSSFRichTextString(
//        I18n.getLanguage("MrScheduleTel.region"));
//    headerCellMarketCode.setCellValue(market);
//    headerCellMarketCode.setCellStyle(style.get("header"));
//    headerCellRegion.setCellValue(reg);
//    headerCellRegion.setCellStyle(style.get("header"));
//    sheetParam1.setColumnWidth(0, 15000);
//    sheetParam1.setColumnWidth(1, 15000);

    Cell headerCellArray = headerArray.createCell(0);
    Cell headerCellNetwork = headerArray.createCell(1);
    Cell headerCellDevice = headerArray.createCell(2);
    XSSFRichTextString array = new XSSFRichTextString(
        I18n.getLanguage("MrScheduleTel.arrayCode"));
    XSSFRichTextString network = new XSSFRichTextString(
        I18n.getLanguage("MrScheduleTel.networkType"));
    XSSFRichTextString device = new XSSFRichTextString(
        I18n.getLanguage("MrScheduleTel.deviceType"));
    headerCellArray.setCellValue(array);
    headerCellArray.setCellStyle(style.get("header"));
    headerCellNetwork.setCellValue(network);
    headerCellNetwork.setCellStyle(style.get("header"));
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

//    row = 1;
//    for (ItemDataCRInside marketCode : lstMarketCode) {
//      List<MrDeviceDTO> mrDeviceDTOList = mrDeviceRepository
//          .getListRegionByMarketCode(
//              marketCode.getValueStr() != null ? String.valueOf(marketCode.getValueStr()) : null);
//      excelWriterUtils
//          .createCell(sheetParam1, 0, row, marketCode.getDisplayStr(), style.get("cell"));
//      for (MrDeviceDTO mrDeviceDTO : mrDeviceDTOList) {
//        excelWriterUtils
//            .createCell(sheetParam1, 1, row++, mrDeviceDTO.getRegionHard(), style.get("cell"));
//      }
//    }
//    sheetParam1.autoSizeColumn(0);
//    sheetParam1.autoSizeColumn(1);

    row = 8;
    List<CatItemDTO> lstArrayCode = mrCfgProcedureCDBusiness.getMrSubCategory();
    Map<String, String> mapArrCodeEN = new HashMap<>();
    for (CatItemDTO dto : lstArrayCode) {
      excelWriterUtils
          .createCell(sheetParam, 3, row++, dto.getItemName(), style.get("cell"));
      mapArrCodeEN.put(dto.getItemValue(), dto.getItemName());
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
    List<MrDeviceDTO> lstANT = mrDeviceRepository.getMrDeviceByA_N_T();
    Map<String, String> mapAN = new HashMap<>();
    Map<String, String> mapANT = new HashMap<>();
    if (lstANT != null && !lstANT.isEmpty()) {
      for (MrDeviceDTO item : lstANT) {
        if (!mapAN.containsKey(item.getArrayCode())) {
          if (row > 1) {
            excelWriterUtils
                .createCell(sheetParam2, 0, row, "", cellStyleHeader);
            excelWriterUtils
                .createCell(sheetParam2, 1, row, "", cellStyleHeader);
            excelWriterUtils
                .createCell(sheetParam2, 2, row, "", cellStyleHeader);
            row++; // them ngan cach
          }
          String valueDefault = item.getArrayCode();
          if (mapArrCodeEN.containsKey(valueDefault)) {
            valueDefault = mapArrCodeEN.get(valueDefault);
          }
          excelWriterUtils
              .createCell(sheetParam2, 0, row, valueDefault, style.get("cell"));
        }
        if (!mapANT.containsKey(item.getArrayCode() + item.getNetworkType())) {
          excelWriterUtils
              .createCell(sheetParam2, 1, row, item.getNetworkType(), style.get("cell"));
        }
        excelWriterUtils
            .createCell(sheetParam2, 2, row++, item.getDeviceType(), style.get("cell"));
        mapAN.put(item.getArrayCode(), item.getNetworkType());
        mapANT.put(item.getArrayCode() + item.getNetworkType(), item.getDeviceType());
      }
    }

    sheetParam2.autoSizeColumn(0);
    sheetParam2.autoSizeColumn(1);

    row = 8;
    List<MrConfigDTO> lstMrConfigDTO = mrScheduleTelRepository.getConfigByGroup("LY_DO_KO_BD");
    for (MrConfigDTO dto : lstMrConfigDTO) {
      excelWriterUtils
          .createCell(sheetParam, 24, row++, dto.getConfigName(), style.get("cell"));
    }
    sheetParam.autoSizeColumn(1);
    Name mrConfirmNameStr = workbook.createName();
    mrConfirmNameStr.setNameName("mrConfirmNameStr");
    mrConfirmNameStr.setRefersToFormula("param!$Y$2:$Y$" + row);
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
    if ("H".equals(type)) {
      workbook.setSheetName(0, I18n.getLanguage("MrScheduleTel.title"));
    } else {
      workbook.setSheetName(0, I18n.getLanguage("MrScheduleTel.title.soft"));
    }
//    workbook.setSheetName(2, I18n.getLanguage("MrScheduleTel.region"));
    workbook.setSheetName(2, I18n.getLanguage("MrScheduleTel.arrNet"));
    workbook.setSheetHidden(1, true);
    sheetParam.setSelected(false);

    //set tên file excel
    String fileResult = tempFolder + File.separator;
    String fileName = "IMPORT_MR_SCHEDULE_TEL" + "_" + System.currentTimeMillis() + ".xlsx";
    excelWriterUtils.saveToFileExcel(workbook, fileResult, fileName);
    String resultPath = fileResult + fileName;
    File fileExport = new File(resultPath);
    return fileExport;
  }

  @Override
  public ResultInSideDto importData(MultipartFile multipartFile) throws Exception {
    List<MrScheduleTelDTO> mrScheduleTelDTOArrayList = new ArrayList<>();
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
            24,
            1000
        );
        //Kiểm tra form header có đúng chuẩn
        if (headerList.size() == 0 || !validFileFormat(headerList)) {
          resultInSideDto.setKey(RESULT.ERROR);
          resultInSideDto.setMessage(I18n.getLanguage("common.import.errorTemplate"));
          return resultInSideDto;
        }
        //Lấy dữ liệu import
        List<Object[]> dataImportList = CommonImport.getDataFromExcelFile(
            fileImport,
            0,
            8,
            0,
            24,
            1000
        );
        if (dataImportList.size() > 1500) {
          resultInSideDto.setKey(RESULT.DATA_OVER);
          resultInSideDto.setMessage(I18n.getLanguage("common.importing.maxrow"));
          return resultInSideDto;
        }
        if (!dataImportList.isEmpty()) {
          int index = 0;
          setMrConfirm();
          index = processDataImport(dataImportList, mrScheduleTelDTOArrayList, MR_TYPE_HARD);
          if (index == 0) {
            UserToken userToken = ticketProvider.getUserToken();
            if (mrScheduleTelDTOArrayList != null && !mrScheduleTelDTOArrayList.isEmpty()) {
              for (MrScheduleTelDTO dto : mrScheduleTelDTOArrayList) {
                MrScheduleTelDTO objUpdate = mapDataImport.get(dto.getScheduleId());
                if (StringUtils.isStringNullOrEmpty(objUpdate.getMrHardCycle()) && StringUtils
                    .isNotNullOrEmpty(objUpdate.getCycle())) {
                  objUpdate.setMrHardCycle(Long.parseLong(objUpdate.getCycle()));
                }
                if (StringUtils.isStringNullOrEmpty(objUpdate.getMrId()) || StringUtils
                    .isStringNullOrEmpty(objUpdate.getCrId())) {
                  objUpdate.setNextDateModify(dto.getNextDateModify());
                }
                objUpdate.setModifyUser(userToken.getUserName());
                objUpdate.setUpdatedDate(new Date());
                objUpdate.setModifyDate(new Date());
                objUpdate.setUserMrHard(dto.getUserMrHard());
                objUpdate.setMrConfirm(dto.getMrConfirm());
                objUpdate.setMrComment(dto.getMrComment());
                ResultInSideDto res = new ResultInSideDto(null, RESULT.ERROR, RESULT.ERROR);
                MrScheduleTelDTO old = mapDataImport.get(dto.getScheduleId());

                MrScheduleTelHisDTO scheduleHisDTO = new MrScheduleTelHisDTO();
                if (!StringUtils.isStringNullOrEmpty(objUpdate.getMrConfirm())) {
                  MrDeviceDTO dtoMD = mrDeviceRepository
                      .findMrDeviceById(objUpdate.getDeviceId());
                  mrScheduleTelRepository.deleteMrScheduleTel(objUpdate.getScheduleId());
                  dtoMD.setMrConfirmHard(objUpdate.getMrConfirm());
                  dtoMD.setMrHard("0");
                  if ("M".equals(objUpdate.getCycleType())) {
                    if ("1".equals(objUpdate.getCycle())) {
                      dtoMD.setIsComplete1m(1L);
                    } else if ("3".equals(objUpdate.getCycle())) {
                      dtoMD.setIsComplete3m(1L);
                    } else if ("6".equals(objUpdate.getCycle())) {
                      dtoMD.setIsComplete6m(1L);
                    } else if ("12".equals(objUpdate.getCycle())) {
                      dtoMD.setIsComplete12m(1L);
                    }
                  }
                  insertMrScheduleTelHis(scheduleHisDTO, objUpdate, old, MR_TYPE_HARD);
                  dtoMD.setUpdateUser(userToken.getUserName());
                  dtoMD.setUpdateDate(objUpdate.getUpdatedDate());
                  dtoMD.setUserMrHard(objUpdate.getUserMrHard());
                  res = mrDeviceRepository.updateMrDeviceServices(dtoMD);
                } else {
                  res = mrScheduleTelRepository.updateMrScheduleTel(objUpdate);
                  if (RESULT.SUCCESS.equals(res.getKey())) {
                    MrDeviceDTO dtoMD = mrDeviceRepository
                        .findMrDeviceById(objUpdate.getDeviceId());
                    dtoMD.setUpdateUser(userToken.getUserName());
                    dtoMD.setUpdateDate(new Date());
                    mrDeviceRepository.updateMrDeviceServices(dtoMD);
                  }
                }
                if (RESULT.SUCCESS.equals(res.getKey())) {
                  resultInSideDto.setMessage(I18n.getValidation("common.update.success"));
                } else {
                  resultInSideDto.setKey(RESULT.ERROR);
                  resultInSideDto.setMessage(I18n.getValidation("import.common.fail"));
                  return resultInSideDto;
                }
              }
              if (RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
                mrScheduleTelDTOArrayList.stream()
                    .forEach(c -> c.setResultImport(I18n.getValidation("common.update.success")));
                File fileExportSuccess = exportFileEx(mrScheduleTelDTOArrayList,
                    Constants.RESULT_IMPORT, "H");
                resultInSideDto.setFilePath(fileExportSuccess.getPath());
                resultInSideDto.setFile(fileExportSuccess);
              }
            }
          } else {
            File fileExport = exportFileEx(mrScheduleTelDTOArrayList,
                Constants.RESULT_IMPORT, "H");
            resultInSideDto.setKey(RESULT.ERROR);
            resultInSideDto.setMessage(I18n.getValidation("import.common.fail"));
            resultInSideDto.setFilePath(fileExport.getPath());
            resultInSideDto.setFile(fileExport);
          }
        } else {
          resultInSideDto.setKey(RESULT.NODATA);
          resultInSideDto.setMessage(I18n.getLanguage("common.searh.nodata"));
          File fileExport = exportFileEx(mrScheduleTelDTOArrayList, Constants.RESULT_IMPORT, "H");
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

  public MrScheduleTelDTO validateImportInfo(MrScheduleTelDTO mrScheduleTelDTO,
      List<MrScheduleTelDTO> list, String type) {
    if (StringUtils.isStringNullOrEmpty(mrScheduleTelDTO.getScheduleIdStr())) {
      mrScheduleTelDTO.setResultImport(I18n.getValidation("mrScheduleTel.scheduleId.notEmpty"));
      return mrScheduleTelDTO;
    }
    if (MR_TYPE_SOFT.equals(type)) {
      if ("5".equals(mrScheduleTelDTO.getMrConfirm())) {
        if (StringUtils.isStringNullOrEmpty(mrScheduleTelDTO.getCrIdStr())) {
          mrScheduleTelDTO
              .setResultImport(I18n.getValidation("import.scheduleTelSoft.cr.isrequired"));
          return mrScheduleTelDTO;
        }
        if (DataUtil.isNumber(mrScheduleTelDTO.getCrIdStr().trim())) {
          mrScheduleTelDTO.setCrId(Long.valueOf(mrScheduleTelDTO.getCrIdStr().trim()));
        } else {
          mrScheduleTelDTO
              .setResultImport(I18n.getValidation("mrScheduleTel.crId.invalid"));
          return mrScheduleTelDTO;
        }
      }
    }
    if (StringUtils.isStringNullOrEmpty(mrScheduleTelDTO.getNextDateModifyStr())) {
      mrScheduleTelDTO.setResultImport(I18n.getValidation("mrScheduleTel.nextDateModify.notEmpty"));
      return mrScheduleTelDTO;
    }
    if (!mapDataImport.containsKey(mrScheduleTelDTO.getScheduleId())) {
      mrScheduleTelDTO.setResultImport(I18n.getValidation("mrScheduleTel.scheduleId.notFound"));
      return mrScheduleTelDTO;
    }
    if (!StringUtils.isStringNullOrEmpty(mrScheduleTelDTO.getNextDateModify())) {
      if (DateUtil.getYY(mrScheduleTelDTO.getNextDateModify()) < DateUtil.getYY(new Date())) {
        mrScheduleTelDTO
            .setResultImport(I18n.getValidation("mrScheduleTel.erro.nextDateModify.invalid"));
        return mrScheduleTelDTO;
      } else if (DateUtil.getYY(mrScheduleTelDTO.getNextDateModify()) == DateUtil
          .getYY(new Date())) {
        if (DateUtil.getMonth(mrScheduleTelDTO.getNextDateModify()) < DateUtil
            .getMonth(new Date())) {
          mrScheduleTelDTO
              .setResultImport(I18n.getValidation("mrScheduleTel.erro.nextDateModify.invalid"));
          return mrScheduleTelDTO;
        } else if (DateUtil.getMonth(mrScheduleTelDTO.getNextDateModify()) == DateUtil
            .getMonth(new Date())) {
          if (DateUtil.getDayInMonth(mrScheduleTelDTO.getNextDateModify()) < DateUtil
              .getDayInMonth(new Date())) {
            mrScheduleTelDTO
                .setResultImport(I18n.getValidation("mrScheduleTel.erro.nextDateModify.invalid"));
            return mrScheduleTelDTO;
          }
        }
      }
    }
    if (StringUtils.isNotNullOrEmpty(mrScheduleTelDTO.getMrConfirmName()) && StringUtils
        .isStringNullOrEmpty(mrScheduleTelDTO.getMrConfirm())) {
      mrScheduleTelDTO
          .setResultImport(I18n.getValidation("mrScheduleTel.mrConfirm.invalid"));
      return mrScheduleTelDTO;
    }
    if (mrScheduleTelDTO.getMrComment() != null
        && mrScheduleTelDTO.getMrComment().length() > 1000) {
      mrScheduleTelDTO
          .setResultImport(I18n.getValidation("mrScheduleTel.mrComment.tooLong"));
      return mrScheduleTelDTO;
    }
    if (StringUtils.isNotNullOrEmpty(mrScheduleTelDTO.getUserMrHard())) {
      UsersInsideDto usersInsideDto = userRepository
          .getUserDTOByUserName(mrScheduleTelDTO.getUserMrHard());
      if (usersInsideDto != null) {
        if (!mrScheduleTelDTO.getUserMrHard().equals(usersInsideDto.getUsername())) {
          mrScheduleTelDTO
              .setResultImport(I18n.getValidation("mrScheduleTel.userMrHard.invalid"));
          return mrScheduleTelDTO;
        }
      } else {
        mrScheduleTelDTO
            .setResultImport(I18n.getValidation("mrScheduleTel.userMrHard.invalid"));
        return mrScheduleTelDTO;
      }
    }
    MrScheduleTelDTO objUpdate = mapDataImport.get(mrScheduleTelDTO.getScheduleId());
//    if (!StringUtils.isStringNullOrEmpty(objUpdate.getMrId()) && !StringUtils
//        .isStringNullOrEmpty(mrScheduleTelDTO.getMrId())) {
//      mrScheduleTelDTO.setResultImport(I18n.getValidation("mrScheduleTel.list.mrId.removeConfirm"));
//      return mrScheduleTelDTO;
//    }
    //dung edit 09/03/2020 không cho phép cập nhật khi đã có MrId
    if (!StringUtils.isStringNullOrEmpty(objUpdate.getMrId())) {
      if (MR_TYPE_SOFT.equals(type)) {
        mrScheduleTelDTO
            .setResultImport(I18n.getValidation("mrScheduleTelSoft.mrId.removeConfirm"));
      } else {
        mrScheduleTelDTO.setResultImport(I18n.getValidation("mrScheduleTel.mrId.removeConfirm"));
      }
      return mrScheduleTelDTO;
    }
    mrScheduleTelDTO = validateDuplicate(mrScheduleTelDTO, list);
    return mrScheduleTelDTO;
  }

  public MrScheduleTelDTO validateDuplicate(MrScheduleTelDTO dto,
      List<MrScheduleTelDTO> lstoExportDTOS) {
    if (lstoExportDTOS != null && !lstoExportDTOS.isEmpty()) {
      for (int i = 0; i < lstoExportDTOS.size(); i++) {
        MrScheduleTelDTO dtoCheck = lstoExportDTOS.get(i);
        if (dto.getScheduleId().equals(dtoCheck.getScheduleId())) {
          dto.setResultImport(I18n.getValidation("MrMaterialDTO.unique.file")
              .replaceAll("0", String.valueOf((i) + 1)));
          break;
        }
      }
    }
    return dto;
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

  private boolean validFileFormat(List<Object[]> headerList) {
    Object[] objects = headerList.get(0);
    if (objects == null) {
      return false;
    }
    int count = 0;
    for (Object data : objects) {
      if (data != null) {
        count += 1;
      }
    }
    if (count != 25) {
      return false;
    }
    if (objects[0] == null) {
      return false;
    }
    if (!I18n.getLanguage("common.STT").equalsIgnoreCase(objects[0].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("MrScheduleTel.scheduleId") + "*")
        .equalsIgnoreCase(objects[1].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("MrScheduleTel.nationName"))
        .equalsIgnoreCase(objects[2].toString().trim())) {
      return false;
    }
//    if (!(I18n.getLanguage("MrScheduleTel.region"))
//        .equalsIgnoreCase(objects[3].toString().trim())) {
//      return false;
//    }
    if (!I18n.getLanguage("MrScheduleTel.arrayCode")
        .equalsIgnoreCase(objects[3].toString().trim())) {
      return false;
    }
    if (!I18n.getLanguage("MrScheduleTel.networkType")
        .equalsIgnoreCase(objects[4].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("MrScheduleTel.deviceType"))
        .equalsIgnoreCase(objects[5].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("MrScheduleTel.deviceCode"))
        .equalsIgnoreCase(objects[6].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("MrScheduleTel.deviceName"))
        .equalsIgnoreCase(objects[7].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("MrScheduleTel.nodeIp"))
        .equalsIgnoreCase(objects[8].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("MrScheduleTel.vendor"))
        .equalsIgnoreCase(objects[9].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("MrScheduleTel.mrId"))
        .equalsIgnoreCase(objects[10].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("MrScheduleTel.woCode"))
        .equalsIgnoreCase(objects[11].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("MrScheduleTel.coordinatingUnitHardName"))
        .equalsIgnoreCase(objects[12].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("MrScheduleTel.cycle"))
        .equalsIgnoreCase(objects[13].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("MrScheduleTel.cycleTypeName"))
        .equalsIgnoreCase(objects[14].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("MrScheduleTel.woContent"))
        .equalsIgnoreCase(objects[15].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("MrScheduleTel.lastDateStr"))
        .equalsIgnoreCase(objects[16].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("MrScheduleTel.nextDateStr"))
        .equalsIgnoreCase(objects[17].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("MrScheduleTel.nextDateModifyStr") + "*")
        .equalsIgnoreCase(objects[18].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("MrScheduleTel.updatedDateStr"))
        .equalsIgnoreCase(objects[19].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("MrScheduleTel.scheduleType"))
        .equalsIgnoreCase(objects[20].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("MrScheduleTel.station"))
        .equalsIgnoreCase(objects[21].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("MrScheduleTel.userMrHard"))
        .equalsIgnoreCase(objects[22].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("MrScheduleTel.mrConfirmName"))
        .equalsIgnoreCase(objects[23].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("MrScheduleTel.mrComment"))
        .equalsIgnoreCase(objects[24].toString().trim())) {
      return false;
    }
    return true;
  }

  public File exportFileEx(List<MrScheduleTelDTO> lstData, String key, String type)
      throws Exception {
    String title = "";
    String fileNameOut = "";
    String subTitle = I18n
        .getLanguage("MrScheduleTel.export.exportDate", DateTimeUtils.convertDateOffset());
    List<ConfigFileExport> fileExports = new ArrayList<>();
    List<ConfigHeaderExport> lstHeaderSheet = new ArrayList<>();
    if ("H".equals(type)) {
      if (Constants.RESULT_IMPORT.equals(key)) {
        title = I18n.getLanguage("MrScheduleTel.import.title");
        fileNameOut = MR_SCHEDULE_TEL__HARD_RESULT_IMPORT;
        lstHeaderSheet = readerHeaderSheet("scheduleId", "nationName",
//            "region",
            "arrayCodeStr", "networkType", "deviceType", "deviceCode", "deviceName",
            "nodeIp",
            "vendor", "mrId", "woCode", "coordinatingUnitHardName", "cycle", "cycleTypeName",
            "woContent",
            "lastDateStr", "nextDateStr", "nextDateModifyStr", "updatedDateStr", "scheduleType",
            "station",
            "userMrHard", "mrConfirmName", "mrComment", "resultImport");
      } else {
        title = I18n.getLanguage("MrScheduleTel.export.title");
        fileNameOut = MR_SCHEDULE_TEL_HARD_EXPORT;
        lstHeaderSheet = readerHeaderSheet("scheduleId", "nationName",
//            "region",
            "arrayCodeStr", "networkType", "deviceType", "deviceCode", "deviceName",
            "nodeIp",
            "vendor", "mrId", "woCode", "coordinatingUnitHardName", "cycle", "cycleTypeName",
            "woContent",
            "lastDateStr", "nextDateStr", "nextDateModifyStr", "updatedDateStr", "scheduleType",
            "station",
            "userMrHard", "mrConfirmName", "mrComment");
      }
    } else {
      if (Constants.RESULT_IMPORT.equals(key)) {
        title = I18n.getLanguage("MrScheduleTel.import.title.soft");
        fileNameOut = Mr_SCHEDULE_TEL_SOFT_RESULT_IMPORT;
        lstHeaderSheet = readerHeaderSheet("scheduleId", "nationName",
//            "region",
            "arrayCode", "networkType", "deviceType", "deviceCode", "deviceName",
            "nodeIp",
            "vendor", "mrId", "crIdStr", "implementUnitName", "cycle", "cycleTypeName",
            "woContent",
            "lastDateStr", "nextDateStr", "nextDateModifyStr", "updatedDateStr", "scheduleType",
            "station",
            "userMrHard", "mrConfirmName", "mrComment", "resultImport");
      } else {
        title = I18n.getLanguage("MrScheduleTel.export.title.soft");
        fileNameOut = Mr_SCHEDULE_TEL_SOFT_EXPORT;
        lstHeaderSheet = readerHeaderSheet("scheduleId", "nationName",
//            "region",
            "arrayCode", "networkType", "deviceType", "deviceCode", "deviceName",
            "nodeIp",
            "vendor", "mrId", "crId", "implementUnitName", "checkingUnitName", "cycle",
            "cycleTypeName",
            "crContent",
            "lastDateStr", "nextDateStr", "nextDateModifyStr", "updatedDateStr", "groupCode",
            "impactNode", "scheduleType",
            "station", "mrConfirmName", "mrComment");
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
        "language.MrScheduleTel",
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

  public List<MrScheduleTelDTO> resultList(List<MrScheduleTelDTO> lst, String type) {
    List<MrScheduleTelDTO> lstResult = new ArrayList<>();
    if (lst != null && !lst.isEmpty()) {
      if ("H".equals(type)) {
        List<WoCdGroupInsideDTO> lstCd = woCategoryServiceProxy
            .getListCdGroupByUser(new WoCdGroupTypeUserDTO());
        Map<String, WoCdGroupInsideDTO> mapCdIdHard = new HashMap<>();
        if (lstCd != null && !lstCd.isEmpty()) {
          for (WoCdGroupInsideDTO dto : lstCd) {
            mapCdIdHard.put(dto.getWoGroupId().toString(), dto);
          }
        }
        for (MrScheduleTelDTO dto : lst) {
          dto.setScheduleType(I18n.getLanguage("MrScheduleTel.list.mrHard"));
          if (mapCdIdHard.containsKey(dto.getCoordinatingUnitHard())) {
            dto.setCoordinatingUnitHardName(
                mapCdIdHard.get(dto.getCoordinatingUnitHard()).getWoGroupName());
          }
          lstResult.add(dto);
        }
      } else {
        for (MrScheduleTelDTO dto : lst) {
          dto.setScheduleType(I18n.getLanguage("mrScheduleTel.list.mrSoft"));
          lstResult.add(dto);
        }
      }
    }
    return lstResult;
  }

  @Override
  public File exportSoftData(MrScheduleTelDTO mrScheduleTelDTO) throws Exception {
    log.debug("Request to mrScheduleTelDTO: {}", mrScheduleTelDTO);
    mrScheduleTelDTO.setMrSoft("1");
    List<MrScheduleTelDTO> lst = mrScheduleTelRepository
        .getDataExport(mrScheduleTelDTO, MR_TYPE_SOFT);
    return exportFileEx(resultList(lst, MR_TYPE_SOFT), "EXPORT_DATA", MR_TYPE_SOFT);
  }

  @Override
  public ResultInSideDto importSoftData(MultipartFile multipartFile) throws Exception {
    List<MrScheduleTelDTO> mrScheduleTelDTOArrayList = new ArrayList<>();
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
            26,
            1000
        );

        //Kiem tra form header
        if (headerList == null || headerList.isEmpty() || !validateFileSoftFormat(headerList,
            getHeaderSoft())) {
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
            26,
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
          index = processDataImport(dataImportList, mrScheduleTelDTOArrayList, MR_TYPE_SOFT);
          if (index == 0) {
            UserToken userToken = ticketProvider.getUserToken();
            if (mrScheduleTelDTOArrayList != null && !mrScheduleTelDTOArrayList.isEmpty()) {
              for (MrScheduleTelDTO dto : mrScheduleTelDTOArrayList) {
                MrScheduleTelDTO objUpdate = mapDataImport.get(dto.getScheduleId());
                if (objUpdate.getMrId() == null || objUpdate.getCrId() == null) {
                  objUpdate.setNextDateModify(dto.getNextDateModify());
                }
                objUpdate.setModifyUser(userToken.getUserName());
                objUpdate.setUpdatedDate(new Date());
                objUpdate.setModifyDate(new Date());
                objUpdate.setImpactNode(dto.getImpactNode());
                objUpdate.setMrConfirm(dto.getMrConfirm());
                objUpdate.setMrComment(dto.getMrComment());

                ResultInSideDto res = new ResultInSideDto();
                MrScheduleTelHisDTO scheduleHisDTO = new MrScheduleTelHisDTO();
                MrScheduleTelDTO old = mapDataImport.get(objUpdate.getScheduleId());
                if (StringUtils.isNotNullOrEmpty(dto.getMrConfirm())) {
                  if (!"5".equals(dto.getMrConfirm())) {
                    insertMrScheduleTelHis(scheduleHisDTO, objUpdate, old, MR_TYPE_SOFT);
                    mrScheduleTelRepository.deleteMrScheduleTel(objUpdate.getScheduleId());
                  } else {
                    updateMrconfirmForImport(dto, old); //tam thoi comment lai
                  }

                  MrDeviceDTO mrDeviceDTO = mrDeviceRepository
                      .findMrDeviceById(objUpdate.getDeviceId());
                  mrDeviceDTO.setUpdateUser(userToken.getUserName());
                  mrDeviceDTO.setUpdateDate(objUpdate.getUpdatedDate());
                  mrDeviceDTO.setMrConfirmSoft(objUpdate.getMrConfirm());
                  mrDeviceDTO.setImpactNode(objUpdate.getImpactNode());
                  mrDeviceDTO.setMrSoft("0");
                  mrDeviceDTO.setIsCompleteSoft(1L);
                  res = mrDeviceRepository.updateMrDeviceServices(mrDeviceDTO);
                } else {
                  res = mrScheduleTelRepository.updateMrScheduleTel(objUpdate);
                }

                if (RESULT.SUCCESS.equals(res.getKey())) {
                  try {
                    //March 04 2019
                    //check loi update thiet bi MR_DEVICE, truong hop la Link/Tuyen Viba thi get Device = deviceName
                    MrDeviceDTO mrDeviceDTO = new MrDeviceDTO();
                    if (I18n.getLanguage("mrScheduleTel.networkType.viba")
                        .equalsIgnoreCase(objUpdate.getNetworkType())) {
                      MrDeviceDTO dtoSearch = new MrDeviceDTO();
                      dtoSearch.setDeviceName(objUpdate.getDeviceName());
                      mrDeviceDTO = mrDeviceRepository.onSearchEntity(dtoSearch, 0, 1, "", "")
                          .get(0);
                    } else {
                      mrDeviceDTO = mrDeviceRepository.findMrDeviceById(objUpdate.getDeviceId());
                    }
                    if (mrDeviceDTO != null) {
                      mrDeviceDTO.setUpdateUser(userToken.getUserName());
                      mrDeviceDTO.setUpdateDate(objUpdate.getUpdatedDate());
//                                                mrDeviceDTO.setGroupCode(dto.getGroupCode());
                      mrDeviceDTO.setImpactNode(objUpdate.getImpactNode());
                      mrDeviceRepository.updateMrDeviceServices(mrDeviceDTO);
                    }
                  } catch (Exception ex) {
                    log.error(ex.getMessage(), ex);
                  }
                  resultInSideDto.setMessage(I18n.getValidation("common.update.success"));
                } else {
                  resultInSideDto.setKey(RESULT.ERROR);
                  resultInSideDto.setMessage(I18n.getValidation("import.common.fail"));
                  return resultInSideDto;
                }
              }
            }

          } else {
            File fileExport = exportFileEx(mrScheduleTelDTOArrayList,
                Constants.RESULT_IMPORT, MR_TYPE_SOFT);
            resultInSideDto.setKey(RESULT.ERROR);
            resultInSideDto.setMessage(I18n.getValidation("import.common.fail"));
            resultInSideDto.setFilePath(fileExport.getPath());
            resultInSideDto.setFile(fileExport);
          }
        } else {
          resultInSideDto.setKey(RESULT.NODATA);
          resultInSideDto.setMessage(I18n.getLanguage("common.searh.nodata"));
          File fileExport = exportFileEx(mrScheduleTelDTOArrayList, Constants.RESULT_IMPORT,
              MR_TYPE_SOFT);
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

  boolean validateFileSoftFormat(List<Object[]> headerList, String[] headerConfig) {
    Object[] objects = headerList.get(0);
    if (objects == null) {
      return false;
    }

    if (objects.length != headerConfig.length) {
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

  public String[] getHeaderSoft() {
    return new String[]{
        I18n.getLanguage("common.STT"),
        I18n.getLanguage("MrScheduleTel.scheduleId"),
        I18n.getLanguage("MrScheduleTel.nationName"),
//        I18n.getLanguage("MrScheduleTel.region"),
        I18n.getLanguage("MrScheduleTel.arrayCode"),
        I18n.getLanguage("MrScheduleTel.networkType"),
        I18n.getLanguage("MrScheduleTel.deviceType"),
        I18n.getLanguage("MrScheduleTel.deviceCode"),
        I18n.getLanguage("MrScheduleTel.deviceName"),
        I18n.getLanguage("MrScheduleTel.nodeIp"),
        I18n.getLanguage("MrScheduleTel.vendor"),
        I18n.getLanguage("MrScheduleTel.mrId"),//11
        I18n.getLanguage("MrScheduleTel.crId"),
        I18n.getLanguage("MrScheduleTel.implementUnitName"),
        I18n.getLanguage("MrScheduleTel.checkingUnitName"),
        I18n.getLanguage("MrScheduleTel.cycle"),
        I18n.getLanguage("MrScheduleTel.cycleTypeName"),
        I18n.getLanguage("MrScheduleTel.woContent"),
        I18n.getLanguage("MrScheduleTel.lastDateStr"),
        I18n.getLanguage("MrScheduleTel.nextDateStr"),
        I18n.getLanguage("MrScheduleTel.nextDateModifyStr"),
        I18n.getLanguage("MrScheduleTel.updatedDateStr"),
        I18n.getLanguage("MrScheduleTel.groupCode"),
        I18n.getLanguage("MrScheduleTel.impactNode"),
        I18n.getLanguage("MrScheduleTel.scheduleType"),
        I18n.getLanguage("MrScheduleTel.station"),
        I18n.getLanguage("MrScheduleTel.mrConfirmName"),
        I18n.getLanguage("MrScheduleTel.mrComment")
    };
  }

  public int processDataImport(List<Object[]> dataImportList,
      List<MrScheduleTelDTO> mrScheduleTelDTOArrayList, String type) {
    mapDataImport.clear();
    List<String> idSearch = new ArrayList<>();
    int index = 0;
    for (Object[] obj : dataImportList) {
      if (!StringUtils.isStringNullOrEmpty(obj[1]) && DataUtil.isNumber(obj[1].toString().trim())) {
        idSearch.add(obj[1].toString().trim());
      }
    }
    if (!idSearch.isEmpty() && MR_TYPE_SOFT.equals(type)) {
      mapDataImport = getMapFromList(idSearch);
    }
    if (!idSearch.isEmpty() && MR_TYPE_HARD.equals(type)) {
      mapDataImport = getMapFromListHard(idSearch);
    }
    for (Object[] obj : dataImportList) {
      MrScheduleTelDTO mrScheduleTelDTO = new MrScheduleTelDTO();
      int column = 0;
      column++;
      if (!StringUtils.isStringNullOrEmpty(obj[column])) {
        if (!DataUtil.isNumber(obj[column].toString().trim())) {
          mrScheduleTelDTO
              .setResultImport(I18n.getValidation("mrScheduleTel.scheduleId.invalid"));
          mrScheduleTelDTO.setScheduleIdStr(obj[column].toString().trim());
        } else {
          mrScheduleTelDTO.setScheduleIdStr(obj[column].toString().trim());
          mrScheduleTelDTO.setScheduleId(Long.valueOf(obj[column].toString().trim()));
        }
      }
      column++;
      if (!StringUtils.isStringNullOrEmpty(obj[column])) {
        mrScheduleTelDTO.setNationName(obj[column].toString().trim());
      }
//      column++;
//      if (!StringUtils.isStringNullOrEmpty(obj[column])) {
//        mrScheduleTelDTO.setRegion(obj[column].toString().trim());
//      }
      column++;
      if (!StringUtils.isStringNullOrEmpty(obj[column])) {
        mrScheduleTelDTO.setArrayCodeStr(obj[column].toString().trim());
      }
      column++;
      if (!StringUtils.isStringNullOrEmpty(obj[column])) {
        mrScheduleTelDTO.setNetworkType(obj[column].toString().trim());
      }
      column++;
      if (!StringUtils.isStringNullOrEmpty(obj[column])) {
        mrScheduleTelDTO.setDeviceType(obj[column].toString().trim());
      }
      column++;
      if (!StringUtils.isStringNullOrEmpty(obj[column])) {
        mrScheduleTelDTO.setDeviceCode(obj[column].toString().trim());
      }
      column++;
      if (!StringUtils.isStringNullOrEmpty(obj[column])) {
        mrScheduleTelDTO.setDeviceName(obj[column].toString().trim());
      }
      column++;
      if (!StringUtils.isStringNullOrEmpty(obj[column])) {
        mrScheduleTelDTO.setNodeIp(obj[column].toString().trim());
      }
      column++;
      if (!StringUtils.isStringNullOrEmpty(obj[column])) {
        mrScheduleTelDTO.setVendor(obj[column].toString().trim());
      }
      column++;
      if (!StringUtils.isStringNullOrEmpty(obj[column])) {
        if (!DataUtil.isNumber(obj[column].toString().trim())) {
          mrScheduleTelDTO
              .setResultImport(I18n.getValidation("mrScheduleTel.mrId.invalid"));
          mrScheduleTelDTO.setMrIdStr(obj[column].toString().trim());
        } else {
          mrScheduleTelDTO.setMrIdStr(obj[column].toString().trim());
          mrScheduleTelDTO.setMrId(Long.valueOf(obj[column].toString().trim()));
        }
      }
      column++;
      if (!StringUtils.isStringNullOrEmpty(obj[column])) {
        if (MR_TYPE_HARD.equals(type)) {
          mrScheduleTelDTO.setWoCode(obj[column].toString().trim());
        } else {
          mrScheduleTelDTO.setCrIdStr(obj[column].toString().trim());
        }
      }
      column++;
      if (!StringUtils.isStringNullOrEmpty(obj[column])) {
        if (MR_TYPE_HARD.equals(type)) {
          mrScheduleTelDTO.setCoordinatingUnitHardName(obj[column].toString().trim());
        } else {
          mrScheduleTelDTO.setImplementUnitName(obj[column].toString().trim());
        }
      }

      if (MR_TYPE_SOFT.equals(type)) {
        column++;
        if (!StringUtils.isStringNullOrEmpty(obj[column])) {
          mrScheduleTelDTO.setCheckingUnitName(obj[column].toString().trim());
        }
      }

      column++;
      if (!StringUtils.isStringNullOrEmpty(obj[column])) {
        mrScheduleTelDTO.setCycle(obj[column].toString().trim());
      }
      column++;
      if (!StringUtils.isStringNullOrEmpty(obj[column])) {
        mrScheduleTelDTO.setCycleTypeName(obj[column].toString().trim());
      }
      column++;
      if (!StringUtils.isStringNullOrEmpty(obj[column])) {
        mrScheduleTelDTO.setWoContent(obj[column].toString().trim());
      }
      column++;
      if (!StringUtils.isStringNullOrEmpty(obj[column])) {
        mrScheduleTelDTO.setLastDateStr(obj[column].toString().trim());
      }
      column++;
      if (!StringUtils.isStringNullOrEmpty(obj[column])) {
        mrScheduleTelDTO.setNextDateStr(obj[column].toString().trim());
      }
      column++;
      if (!StringUtils.isStringNullOrEmpty(obj[column])) {
        String checkDate = DataUtil.validateDateTimeDdMmYyyy(obj[column].toString().trim());
        if (StringUtils.isStringNullOrEmpty(checkDate)) {
          SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");
          try {
            mrScheduleTelDTO
                .setNextDateModify(sf.parse(obj[column].toString().trim()));
            mrScheduleTelDTO.setNextDateModifyStr(obj[column].toString().trim());
          } catch (Exception e) {
            log.error(e.getMessage(), e);
            mrScheduleTelDTO.setNextDateModifyStr(obj[column].toString().trim());
            mrScheduleTelDTO
                .setResultImport(I18n.getValidation("mrScheduleTel.nextDateModify.invalid"));
          }
        } else {
          mrScheduleTelDTO.setNextDateModifyStr(obj[column].toString().trim());
          mrScheduleTelDTO
              .setResultImport(I18n.getValidation("mrScheduleTel.nextDateModify.invalid"));
        }
      }
      column++;
      if (!StringUtils.isStringNullOrEmpty(obj[column])) {
        mrScheduleTelDTO.setUpdatedDateStr(obj[column].toString().trim());
      }

      if (MR_TYPE_SOFT.equals(type)) {
        column++;
        if (!StringUtils.isStringNullOrEmpty(obj[column])) {
          mrScheduleTelDTO.setGroupCode(obj[column].toString().trim());
        }
        column++;
        if (!StringUtils.isStringNullOrEmpty(obj[column])) {
          mrScheduleTelDTO.setImpactNode(obj[column].toString().trim());
        }
      }
      column++;
      if (!StringUtils.isStringNullOrEmpty(obj[column])) {
        mrScheduleTelDTO.setScheduleType(obj[column].toString().trim());
      }
      column++;
      if (!StringUtils.isStringNullOrEmpty(obj[column])) {
        mrScheduleTelDTO.setStationStr(obj[column].toString().trim());
        mrScheduleTelDTO.setStation(obj[column].toString().trim());
      }
      if (MR_TYPE_HARD.equals(type)) {
        column++;
        if (!StringUtils.isStringNullOrEmpty(obj[column])) {
          mrScheduleTelDTO.setUserMrHard(obj[column].toString().trim());
        }
      }
      column++;
      if (!StringUtils.isStringNullOrEmpty(obj[column])) {
        mrScheduleTelDTO.setMrConfirmName(obj[column].toString().trim());
        for (Map.Entry<String, String> item : mapMrConfirm.entrySet()) {
          if (mrScheduleTelDTO.getMrConfirmName().equals(item.getValue())) {
            mrScheduleTelDTO.setMrConfirm(item.getKey());
            break;
          }
        }
      }
      column++;
      if (!StringUtils.isStringNullOrEmpty(obj[column])) {
        mrScheduleTelDTO.setMrComment(obj[column].toString().trim());
      }

      MrScheduleTelDTO mrScheduleTelDTOTmp = validateImportInfo(
          mrScheduleTelDTO, mrScheduleTelDTOArrayList, type);

      if (mrScheduleTelDTOTmp.getResultImport() == null) {
        mrScheduleTelDTOTmp
            .setResultImport(I18n.getLanguage("mrHardGroupConfig.result.import"));
        mrScheduleTelDTOArrayList.add(mrScheduleTelDTOTmp);
      } else {
        mrScheduleTelDTOArrayList.add(mrScheduleTelDTO);
        index++;
      }
    }
    return index;
  }

  Map<Long, MrScheduleTelDTO> getMapFromList(List<String> idSearch) {
    Map<Long, MrScheduleTelDTO> mapData = new HashMap<>();
    List<MrScheduleTelDTO> results = mrScheduleTelRepository
        .getListMrSheduleTelByIdForImport(idSearch);
    if (results != null && !results.isEmpty()) {
      results.forEach(item -> {
        if (item.getScheduleId() != null) {
          mapData.put(item.getScheduleId(), item);
        }
      });
    }
    return mapData;
  }

  Map<Long, MrScheduleTelDTO> getMapFromListHard(List<String> idSearch) {
    Map<Long, MrScheduleTelDTO> mapData = new HashMap<>();
    MrScheduleTelDTO dtoSearch = new MrScheduleTelDTO();
    dtoSearch.setScheduleIdList(idSearch);
    List<MrScheduleTelDTO> results = mrScheduleTelRepository
        .getDataExport(dtoSearch, MR_TYPE_HARD);
    if (results != null && !results.isEmpty()) {
      results.forEach(item -> {
        if (item.getScheduleId() != null) {
          mapData.put(item.getScheduleId(), item);
        }
      });
    }
    return mapData;
  }

  @Override
  public ResultInSideDto onUpdateSoft(MrScheduleTelDTO mrScheduleTelDTO) {
    log.debug("Request to updateMrScheduleTel: {}", mrScheduleTelDTO);
    UserToken userToken = ticketProvider.getUserToken();
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.ERROR);
    ResultInSideDto vadidateOk = new ResultInSideDto();
    CrInsiteDTO form = new CrInsiteDTO();
    vadidateOk.setKey(RESULT.SUCCESS);
    List<CrImpactedNodesDTO> lstCrImpactedNodesDTOs = new ArrayList<>();
    MrScheduleTelDTO dtoOld = getDetail(mrScheduleTelDTO.getScheduleId(), MR_TYPE_SOFT);
    mrScheduleTelDTO.setMrHard(dtoOld.getMrHard());
    mrScheduleTelDTO.setMrSoft(dtoOld.getMrSoft());
//    mrScheduleTelDTO.setMrComment(dtoOld.getMrComment());
    String mrConfirmCombobox = mrScheduleTelDTO.getMrConfirm();
    if (mrScheduleTelDTO.getCrId() != null) {
      try {
        form = crServiceProxy.findCrByIdProxy(mrScheduleTelDTO.getCrId());
        if (form != null) {
          CrImpactedNodesDTO crImpactedNodesDTO = new CrImpactedNodesDTO();
          crImpactedNodesDTO.setCrId(mrScheduleTelDTO.getCrId().toString());
          crImpactedNodesDTO
              .setCrCreatedDateStr(DateTimeUtils.date2ddMMyyyyHHMMss(form.getCreatedDate()));
          crImpactedNodesDTO.setEarlierStartTimeStr(
              DateTimeUtils.date2ddMMyyyyHHMMss(form.getEarliestStartTime()));
          crImpactedNodesDTO.setNodeType(CR_NODE_TYPE.EFFECT);
          crImpactedNodesDTO.setSaveType(form.getNodeSavingMode());
          lstCrImpactedNodesDTOs = crServiceProxy.getLisNodeOfCRForProxy(crImpactedNodesDTO);
        } else {
          return new ResultInSideDto(null, RESULT.ERROR,
              I18n.getLanguage("MrScheduleTel.crId") + " " + I18n.getLanguage("common.incorrect"));
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
        return new ResultInSideDto(null, RESULT.ERROR,
            I18n.getLanguage("MrScheduleTel.crId") + I18n.getLanguage("common.incorrect"));
      }
    }
    if (StringUtils.isStringNullOrEmpty(mrScheduleTelDTO.getMrConfirm()) || "0"
        .equals(mrScheduleTelDTO.getMrConfirm())) {
      mrScheduleTelDTO.setMrConfirm(null);
      try {
        vadidateOk = validate(mrScheduleTelDTO, false);
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    } else {
      List<MrConfigDTO> lstMrConfigDTO = mrScheduleTelRepository.getConfigByGroup("LY_DO_KO_BD");
      for (MrConfigDTO mrDto : lstMrConfigDTO) {
        if (mrDto.getConfigCode().equals(mrScheduleTelDTO.getMrConfirm())) {
          if (MR_CONFIG_TYPE.equals(mrDto.getConfigValue())) {
            if (StringUtils.isStringNullOrEmpty(mrScheduleTelDTO.getCrId())) {
              vadidateOk.setKey(RESULT.ERROR);
              vadidateOk.setMessage(
                  I18n.getLanguage("MrScheduleTel.crId") + I18n.getLanguage("common.notnull"));
            } else {
              vadidateOk.setKey(RESULT.SUCCESS);
            }
          }
          break;
        }
      }

      try {
        String validate2 = validate2(mrScheduleTelDTO, dtoOld, form, lstCrImpactedNodesDTOs);
        if (!StringUtils.isStringNullOrEmpty(validate2)) {
          vadidateOk.setKey(RESULT.ERROR);
          vadidateOk.setMessage(validate2);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    if (RESULT.SUCCESS.equals(vadidateOk.getKey())) {
      MrScheduleTelHisDTO scheduleHisDTO = new MrScheduleTelHisDTO();
      MrDeviceDTO deviceDTO = new MrDeviceDTO();
      MrScheduleTelDTO searchDTO = new MrScheduleTelDTO();
      boolean isExisted = false;
      MrDeviceDTO deviceUpdate = mrDeviceRepository
          .findMrDeviceById(Long.valueOf(dtoOld.getDeviceId()));
      if (deviceUpdate.getDeviceId() != null) {
        isExisted = true;
      }
      if (mrScheduleTelDTO.getMrId() != null) {
        MrScheduleTelDTO objUpdate = mrScheduleTelRepository
            .findById(mrScheduleTelDTO.getScheduleId());
        MrDeviceDTO deviceDTO2 = new MrDeviceDTO();
        if (!StringUtils.isStringNullOrEmpty(dtoOld.getDeviceId())) {
          deviceDTO2 = mrDeviceRepository.findMrDeviceById(dtoOld.getDeviceId());
        }
//                                    deviceDTO2.setGroupCode(mrScheduleTelDTO.getGroupCode());
        deviceDTO2.setComments(mrScheduleTelDTO.getMrComment());
        deviceDTO2.setImpactNode(mrScheduleTelDTO.getImpactNode());

        if (!StringUtils.isStringNullOrEmpty(mrScheduleTelDTO.getMrConfirm())) {
          if (!"5".equals(mrScheduleTelDTO.getMrConfirm())) {
            if (isExisted) {
              deviceDTO2.setCreateUserSoft(userToken.getUserName());
              deviceDTO2.setMrSoft("0");
              deviceDTO2.setMrConfirmSoft(mrScheduleTelDTO.getMrConfirm());
              deviceDTO2.setIsCompleteSoft(1L);
            }
            insertMrScheduleTelHis(scheduleHisDTO, objUpdate, mrScheduleTelDTO, MR_TYPE_SOFT);
            mrScheduleTelRepository.deleteMrScheduleTel(mrScheduleTelDTO.getScheduleId());
          }
        }
        if (StringUtils.isStringNullOrEmpty(objUpdate.getMrComment())) {
          objUpdate.setMrComment("");
        }

        if (StringUtils.isStringNullOrEmpty(objUpdate.getImpactNode())) {
          objUpdate.setImpactNode("");
        }
        if (StringUtils.isStringNullOrEmpty(mrScheduleTelDTO.getImpactNode())) {
          mrScheduleTelDTO.setImpactNode("");
        }
        if (
//            !objUpdate.getMrComment().equals(mrScheduleTelDTO.getMrComment())
//                || !objUpdate
//            .getImpactNode().equals(mrScheduleTelDTO.getImpactNode())
//            ||
            !String.valueOf(mrScheduleTelDTO.getMrId())
                .equals(String.valueOf(objUpdate.getMrId()))
                || !DateTimeUtils.date2ddMMyyyyHHMMss(mrScheduleTelDTO.getNextDateModify())
                .equals(DateTimeUtils.date2ddMMyyyyHHMMss(objUpdate.getNextDateModify()))
                || !String.valueOf(mrScheduleTelDTO.getMrConfirm())
                .equals(String.valueOf(objUpdate.getMrConfirm()))) {
          objUpdate.setMrId(null);
          objUpdate.setCrId(null);
        }

//                                    if (!StringUtils.isStringNullOrEmpty(formUpdate.getDisturbanceEndTime())) {
//                                        objUpdate.setLastDate(formUpdate.getDisturbanceEndTime());
//                                    }
        objUpdate.setCrNumber(null);
        objUpdate.setUpdatedDate(
            DateTimeUtils.convertStringToDate(DateUtil.date2ddMMyyyyString(new Date())));
        objUpdate.setNextDateModify(mrScheduleTelDTO.getNextDateModify());
        objUpdate.setMrConfirm(mrScheduleTelDTO.getMrConfirm());
//                                    objUpdate.setGroupCode(mrScheduleTelDTO.getGroupCode());
        objUpdate.setMrComment(mrScheduleTelDTO.getMrComment());

//                                    objUpdate.setImpactNode(mrScheduleTelDTO.getImpactNode());
        if (!"5".equals(mrScheduleTelDTO.getMrConfirm()) && StringUtils
            .isNotNullOrEmpty(mrScheduleTelDTO.getMrConfirm())) {

        } else {
          resultInSideDto = mrScheduleTelRepository.updateMrScheduleTel(objUpdate);
        }
        resultInSideDto = mrDeviceRepository.updateMrDeviceServices(deviceDTO2);
      } else if (!StringUtils.isStringNullOrEmpty(mrScheduleTelDTO.getMrConfirm())) {
        //con xu ly tiep
        MrScheduleTelDTO objUpdate = mrScheduleTelRepository
            .findById(mrScheduleTelDTO.getScheduleId());
        if (!StringUtils.isStringNullOrEmpty(dtoOld.getDeviceId())) {
          deviceDTO = mrDeviceRepository.findMrDeviceById(dtoOld.getDeviceId());
        }

        //<editor-fold desc="Chọn lý do BD # nâng cấp insert Lịch sử && xóa lịch" defaultstate="collapsed">
        if (!"5".equals(mrScheduleTelDTO.getMrConfirm())) {
          insertMrScheduleTelHis(scheduleHisDTO, objUpdate, mrScheduleTelDTO, MR_TYPE_SOFT);
          mrScheduleTelRepository.deleteMrScheduleTel(mrScheduleTelDTO.getScheduleId());
          if (isExisted) {
            deviceDTO.setCreateUserSoft(userToken.getUserName());
            deviceDTO.setComments(mrScheduleTelDTO.getMrComment());

            if (!StringUtils.isStringNullOrEmpty(mrConfirmCombobox)) {
              deviceDTO.setMrSoft("0");
            } else {
              deviceDTO.setMrSoft("1");
            }
            deviceDTO.setIsCompleteSoft(1L);
            deviceDTO.setImpactNode(mrScheduleTelDTO.getImpactNode());
//                                            deviceDTO.setGroupCode(mrScheduleTelDTO.getGroupCode());
            deviceDTO.setMrConfirmSoft(mrScheduleTelDTO.getMrConfirm());
            resultInSideDto = mrDeviceRepository.updateMrDeviceServices(deviceDTO);
          }
        } //<editor-fold desc="Nếu chọn lý do BD là nâng cấp " defaultstate="collapsed">
        else {
          resultInSideDto = updateMrconfirm(mrScheduleTelDTO, dtoOld, userToken);
        }
      } else {
        MrDeviceDTO dtoMD = new MrDeviceDTO();
        try {
          dtoMD = mrDeviceRepository.findMrDeviceById(dtoOld.getDeviceId());
        } catch (Exception e) {
          log.error(e.getMessage(), e);
          MrDeviceDTO dtoSearch = new MrDeviceDTO();
          dtoSearch.setDeviceName(mrScheduleTelDTO.getDeviceName());
          dtoMD = mrDeviceRepository.onSearchEntity(dtoSearch, 0, 1, "", "").get(0);
        }
        if (!StringUtils.isStringNullOrEmpty(dtoMD.getDeviceId())) {
          isExisted = true;
        } else {
          isExisted = false;
        }
        MrScheduleTelDTO objUpdate2 = mrScheduleTelRepository
            .findById(mrScheduleTelDTO.getScheduleId());
        boolean checkNexdateModify = true;
        objUpdate2.setMrId(null);
        objUpdate2.setCrId(null);
        objUpdate2.setUpdatedDate(
            DateTimeUtils.convertStringToDate(DateUtil.date2ddMMyyyyString(new Date())));
        if (!StringUtils.isStringNullOrEmpty(objUpdate2.getGroupCode())) {
          searchDTO.setGroupCode(objUpdate2.getGroupCode());
          List<MrScheduleTelDTO> lstUpdate = mrScheduleTelRepository
              .getDataExport(searchDTO, MR_TYPE_SOFT);
          if (lstUpdate != null && lstUpdate.size() > 0) {
            for (MrScheduleTelDTO item : lstUpdate) {
              MrScheduleTelDTO objUpdate = mrScheduleTelRepository.findById(item.getScheduleId());
//                                                insertMrScheduleTelHis(scheduleHisDTO, objUpdate2, mrScheduleTelDTO,MR_TYPE_SOFT);
              objUpdate.setUpdatedDate(
                  DateTimeUtils.convertStringToDate(DateUtil.date2ddMMyyyyString(new Date())));
              objUpdate.setNextDateModify(mrScheduleTelDTO.getNextDateModify());
              objUpdate.setMrComment(mrScheduleTelDTO.getMrComment());
              objUpdate.setMrConfirm(mrScheduleTelDTO.getMrConfirm());
//                                                    objUpdate.setImpactNode(mrScheduleTelDTO.getImpactNode());
              mrScheduleTelRepository.updateMrScheduleTel(objUpdate);
              checkNexdateModify = false;
            }
          }
        }

        objUpdate2.setUpdatedDate(
            DateTimeUtils.convertStringToDate(DateUtil.date2ddMMyyyyString(new Date())));
        objUpdate2.setNextDateModify(mrScheduleTelDTO.getNextDateModify());
        objUpdate2.setMrComment(mrScheduleTelDTO.getMrComment());
        objUpdate2.setMrConfirm(mrScheduleTelDTO.getMrConfirm());
        objUpdate2.setMrId(mrScheduleTelDTO.getMrId());
        resultInSideDto = mrScheduleTelRepository.updateMrScheduleTel(objUpdate2);
        if (RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
          if (isExisted) {
            dtoMD.setCreateUserSoft(userToken.getUserName());
            dtoMD.setComments(mrScheduleTelDTO.getMrComment());
            dtoMD.setImpactNode(mrScheduleTelDTO.getImpactNode());
            dtoMD.setUpdateUser(userToken.getUserName());
            dtoMD.setUpdateDate(
                DateTimeUtils.convertStringToDate(DateUtil.date2ddMMyyyyString(new Date())));
            resultInSideDto = mrDeviceRepository.updateMrDeviceServices(dtoMD);
          }
        }
      }
      //con do 1 chut nua
    } else {
      return vadidateOk;
    }

    return resultInSideDto;
  }

  public String validate2(MrScheduleTelDTO dto, MrScheduleTelDTO dtoOld, CrInsiteDTO form,
      List<CrImpactedNodesDTO> lstImpact) {
    //duongnt edit start
    if (StringUtils.isStringNullOrEmpty(dto.getNextDateModify())) {
      return I18n.getValidation("mrScheduleTel.list.nextDateModify.null");
    } else {
      // Ngay truoc khi sua
      // Date oldDateModify = DateUtil.string2Date(nextDateModifyProperty);
      // Ngay sau khi sua
      try {
        Date newDateModify = dto.getNextDateModify();
        if (DateUtil.getYY(newDateModify) < DateUtil.getYY(new Date())) {
          return I18n.getValidation("mrScheduleTel.nextDateModify.invalid");
        } else if (DateUtil.getYY(newDateModify) == DateUtil.getYY(new Date())) {
          // Neu thang sau khi sua < thang hien tai
          if (DateUtil.getMonth(newDateModify) < DateUtil.getMonth(new Date())) {
            return I18n.getValidation("mrScheduleTel.nextDateModify.invalid");
          } else if (DateUtil.getMonth(newDateModify) == DateUtil.getMonth(new Date())) {
            // Neu ngay sau khi sua < ngay hien tai
            if (DateUtil.getDayInMonth(newDateModify) < DateUtil.getDayInMonth(new Date())) {
              return I18n.getValidation("mrScheduleTel.nextDateModify.invalid");
            }
          }
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
        return I18n.getValidation("mrScheduleTel.nextDateModify.invalid");
      }

    }

    if (dto.getMrComment() != null && dto.getMrComment().length() > 1000) {
      return I18n.getValidation("mrScheduleTel.mrComment.tooLong");
    }

    CrImpactedNodesDTO dtoImpact = new CrImpactedNodesDTO();
    if (lstImpact != null && !lstImpact.isEmpty()) {
      if (lstImpact != null && !lstImpact.isEmpty()) {
        dtoImpact = lstImpact.get(0);
      }
    }
    if (dto.getMrId() != null) {
      if (dto.getMrId() != null) {
        MrInsideDTO mrDTO = mrRepository.findMrById(Long.valueOf(dto.getMrId()));
        if (mrDTO.getMrId() == null) {
          return I18n.getValidation("mrScheduleTel.mrId.notFound");
        }
      }
    }

    Long crId = dto.getCrId();
    try {
      String newDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
      Date nextDate = dto.getNextDateModify();
      Date checkDate = DateUtil.string2Date(newDate);

      if (DateUtil.compareDateTime(nextDate, checkDate) == -1) {
        return I18n.getValidation("mrScheduleTel.nextDateModify.small");
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }

    try {
      if (!StringUtils.isStringNullOrEmpty(crId)) {
        boolean checkDeviceCode = false;
        if (form != null) {
          if (!StringUtils.isStringNullOrEmpty(dtoImpact)) {
            if (form.getLatestStartTime() != null) {
//              if (deviceCodeProperty != null) {
              if (dtoOld.getDeviceCode() != null) {
//                Date lastestStartTime = DateUtil.string2Date(form.getLatestStartTime());
                Date lastestStartTime = DateUtil
                    .string2Date(DateTimeUtils.convertDateToString(form.getLatestStartTime()));
                Date lastDate = DateUtil
                    .string2Date(DateTimeUtils.convertDateToString(dtoOld.getLastDate()));
                Long checkDate = lastestStartTime.getTime() - lastDate
                    .getTime(); // Ngay ket thuc CR - Ngay BD gan nhat ( so ngay con lai)

                int days = (int) (checkDate / (1000 * 60 * 60 * 24));
                if ("D".equals(dtoOld.getCycleType())) {
                  Long cycleDays = Long.parseLong(dtoOld.getCycle());
                  if (days > cycleDays) { // DK ngay phai nho hon hoac bang chu
                    return I18n.getValidation("mrScheduleTel.lastestStartTime.small.cycle");
                  }
                } else if ("M".equals(dtoOld.getCycleType())) {
                  Integer cycleMonths = Integer.valueOf(dtoOld.getCycle());
                  int daysCycle = cycleMonths * 30;
                  if (days > daysCycle) {// DK ngay phai nho hon hoac bang chu
                    return I18n.getValidation("mrScheduleTel.lastestStartTime.small.cycle");
                  }
                }
                if (lstImpact != null) {
                  for (CrImpactedNodesDTO item : lstImpact) {
                    if (dtoOld.getDeviceCode().equals(item.getDeviceCode())) {
                      checkDeviceCode = true;
                      break;
                    }
                  }
                }
                if (!checkDeviceCode) {
                  return I18n.getValidation("mrScheduleTel.deviceCode.and.NetworkNodeId");
                }
              } else {
                return I18n.getValidation("mrScheduleTel.deviceCode.and.NetworkNodeId");
              }
            }
          }
        }
      }

    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }

    //duongnt edit end
    return "";
  }

  @Override
  public ResultDTO updateScheduleByCrIdCreatrHis(MrScheduleTelDTO mrScheduleTelDTO,
      String lateTimeUpdate) {
    try {

      MrScheduleTelHisDTO mrScheduleTelHisDTO = new MrScheduleTelHisDTO();
      mrScheduleTelHisDTO.setMarketCode(mrScheduleTelDTO.getMarketCode());
      mrScheduleTelHisDTO.setArrayCode(mrScheduleTelDTO.getArrayCode());
      mrScheduleTelHisDTO.setDeviceType(mrScheduleTelDTO.getDeviceType());
      mrScheduleTelHisDTO.setDeviceId(mrScheduleTelDTO.getDeviceId() == null ? null
          : mrScheduleTelDTO.getDeviceId().toString());
      mrScheduleTelHisDTO.setDeviceCode(mrScheduleTelDTO.getDeviceCode());
      mrScheduleTelHisDTO.setNetworkType(mrScheduleTelDTO.getNetworkType());
      mrScheduleTelHisDTO.setDeviceName(mrScheduleTelDTO.getDeviceName());

      mrScheduleTelHisDTO.setMrDate(lateTimeUpdate);

      mrScheduleTelHisDTO.setMrContent(mrScheduleTelDTO.getMrContentId());
      if (mrScheduleTelDTO.getMrSoft() != null) {
        if ("1".equals(mrScheduleTelDTO.getMrSoft())) {
          mrScheduleTelDTO.setMrMode("S");
        }
      }
      mrScheduleTelHisDTO.setMrMode(mrScheduleTelDTO.getMrMode());
      mrScheduleTelHisDTO.setMrType(mrScheduleTelDTO.getMrType());
      mrScheduleTelHisDTO.setMrId(
          mrScheduleTelDTO.getMrId() == null ? null : mrScheduleTelDTO.getMrId().toString());
      mrScheduleTelHisDTO.setMrCode(mrScheduleTelDTO.getMrCode());
      mrScheduleTelHisDTO.setCrId(
          mrScheduleTelDTO.getCrId() == null ? null : mrScheduleTelDTO.getCrId().toString());
      mrScheduleTelHisDTO.setCrNumber(mrScheduleTelDTO.getCrNumber());
      // mrScheduleTelHisDTO.setImportantLevel(mrScheduleTelDTO.getLevelImportant());
      mrScheduleTelHisDTO.setProcedureId(mrScheduleTelDTO.getProcedureId() == null ? null
          : mrScheduleTelDTO.getProcedureId().toString());
      mrScheduleTelHisDTO.setProcedureName(mrScheduleTelDTO.getProcedureName());
//      mrScheduleTelHisDTO.setNote(I18n.getLanguage("cr.close.note"));
      mrScheduleTelHisDTO.setNote(I18n.getLanguage("mrScheduleTelhis.note.mrConfirmEqualFive"));
      mrScheduleTelHisDTO.setRegion(mrScheduleTelDTO.getRegion());
      mrScheduleTelHisDTO.setCycle(mrScheduleTelDTO.getCycle());

      // Cap nhat vao bang HIS
      ResultInSideDto objResultHis = mrScheduleTelHisRepository
          .insertScheduleHis(mrScheduleTelHisDTO);
      if (RESULT.SUCCESS.equals(objResultHis.getKey())) {
        // Xoa du lieu o bang MR_SCHEDULE_TEL
        mrScheduleTelRepository.deleteMrScheduleTel(mrScheduleTelDTO.getScheduleId());
      }

      // Cap nhat bang trang thai cho thiet bi
      mrDeviceBusiness.updateStatusAndLastDate(mrScheduleTelDTO, "1", lateTimeUpdate);
      return new ResultDTO("1", RESULT.SUCCESS);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return new ResultDTO("0", RESULT.FAIL);
    }

  }

  @Override
  public ResultInSideDto deleteMrScheduleTelSoft(List<MrScheduleTelDTO> mrScheduleTelDTOS) {
    ResultInSideDto res = new ResultInSideDto(null, RESULT.ERROR, null);
    MrScheduleTelDTO dto = new MrScheduleTelDTO();
    List<MrScheduleTelDTO> lstSchedule = new ArrayList<>();
    List<MrScheduleTelDTO> lstScheduleDelete = new ArrayList<>();
    List<MrDeviceDTO> lstDevice = new ArrayList<>();
    MrDeviceDTO dtoMD;
    boolean checkDel = true;
    for (MrScheduleTelDTO item : mrScheduleTelDTOS) {
      if (checkDel) {
        if (StringUtils.isStringNullOrEmpty(item.getMrId()) && StringUtils
            .isStringNullOrEmpty(item.getCrId())) {
          if (StringUtils.isNotNullOrEmpty(item.getDeviceCode())) {
            dto.setDeviceCode(item.getDeviceCode());
            dto.setDeviceName(item.getDeviceName());
            dto.setMrSoft(item.getMrSoft());
            lstSchedule = mrScheduleTelRepository.getDataExport(dto, MR_TYPE_SOFT);
            lstScheduleDelete.addAll(lstSchedule);
          }
        } else {
          checkDel = false;
        }
        if (lstSchedule != null && !lstSchedule.isEmpty()) {
          dtoMD = mrDeviceRepository.findMrDeviceById(item.getDeviceId());
          dtoMD.setIsCompleteSoft(1L);
          lstDevice.add(dtoMD);
        }
      }
    }
    if (checkDel) {
      res = mrScheduleTelRepository.deleteListSchedule(lstScheduleDelete);
      mrDeviceRepository.updateList(lstDevice);
    }
    if (!RESULT.SUCCESS.equalsIgnoreCase(res.getKey())) {
      if (!checkDel) {
        res.setMessage(I18n.getLanguage("mrScheduleTel.del.MrCr"));
      }
    }
    return res;
  }

  @Override
  public WoInsideDTO findWoById(Long woId, Long mrId) {
    UserToken userToken = ticketProvider.getUserToken();
    MrInsideDTO mrDTO = mrServiceRepository.findMrById(mrId);
    WoInsideDTO woInsideDTO = woServiceProxy.findWoByIdProxy(woId);
    WoInsideDTO woDetail = new WoInsideDTO();
    if (woInsideDTO != null) {
      woDetail.setWoSystem(woInsideDTO.getWoSystem());
      woDetail.setWoCode(woInsideDTO.getWoCode());
      woDetail.setWoTypeId(woInsideDTO.getWoTypeId());
      woDetail.setWoContent(woInsideDTO.getWoContent());
      woDetail.setCreatePersonId(woInsideDTO.getCreatePersonId());
      woDetail.setResult(woInsideDTO.getResult());
      woDetail.setStationCode(woInsideDTO.getStationCode());
      woDetail.setStartTime(woInsideDTO.getStartTime());
      woDetail.setEndTime(woInsideDTO.getEndTime());
      woDetail.setWoDescription(woInsideDTO.getWoDescription());
      woDetail.setWoId(woInsideDTO.getWoId());
    }
    try {
      if (woInsideDTO.getWoId() != null) {
        WoDTOSearch woDTOSearch = new WoDTOSearch();
        if ("4".equals(mrDTO.getState())) {
          woDTOSearch.setWoSystemId(woInsideDTO.getWoSystemId());
          woDTOSearch
              .setUserId(userToken.getUserID().toString());
          woDTOSearch.setPage(1);
          woDTOSearch.setPageSize(20);
          woDTOSearch.setSortType("ASC");
          woDTOSearch.setSortName("woId");
          woDTOSearch.setProxyLocale(I18n.getLocale());
          List<WoDTOSearch> lstTemp = woServiceProxy
              .getListDataSearchProxy(woDTOSearch);
          if (lstTemp != null && !lstTemp.isEmpty()) {
            woDetail.setStatusName(lstTemp.get(0).getStatusName());
            woDetail.setCdName(lstTemp.get(0).getCdName());
            woDetail
                .setFtName(lstTemp.get(0).getFtName() == null ? "" : lstTemp.get(0).getFtName());
            woDetail.setCreatePersonName(lstTemp.get(0).getCreatePersonName());
            woDetail.setResultName(
                lstTemp.get(0).getResult() == null ? "" : lstTemp.get(0).getResult());
          }
        } else {
          woDTOSearch.setWoSystemId(woInsideDTO.getWoSystemId());
          woDTOSearch.setWoId(woInsideDTO.getWoId().toString());
          woDTOSearch
              .setUserId(userToken.getUserID().toString());
          woDTOSearch.setPage(1);
          woDTOSearch.setPageSize(20);
          woDTOSearch.setSortType("ASC");
          woDTOSearch.setSortName("woCode");
          woDTOSearch.setProxyLocale(I18n.getLocale());
          List<WoDTOSearch> lstTemp = woServiceProxy.getListDataSearchProxy(woDTOSearch);
          if (lstTemp != null && !lstTemp.isEmpty()) {
            woDetail.setStatusName(lstTemp.get(0).getStatusName());
            woDetail.setCdName(lstTemp.get(0).getCdName());
            woDetail
                .setFtName(lstTemp.get(0).getFtName() == null ? "" : lstTemp.get(0).getFtName());
            woDetail.setCreatePersonName(lstTemp.get(0).getCreatePersonName());
            woDetail.setResultName(
                lstTemp.get(0).getResult() == null ? "" : lstTemp.get(0).getResult());
          }
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    try {
      WoTypeInsideDTO woTypeDTOSearch = new WoTypeInsideDTO();
      woTypeDTOSearch.setPage(1);
      woTypeDTOSearch.setPageSize(Integer.MAX_VALUE);
      woTypeDTOSearch.setSortType("");
      woTypeDTOSearch.setSortName("woTypeId");
      List<WoTypeInsideDTO> listWoTypeDTO = woCategoryServiceProxy
          .getListWoTypeDTO(woTypeDTOSearch);
      for (WoTypeInsideDTO woTypeDto : listWoTypeDTO) {
        if (woDetail.getWoTypeId() != null
            && woDetail.getWoTypeId().equals(woTypeDto.getWoTypeId())) {
          woDetail.setWoTypeName(woTypeDto.getWoTypeName() == null
              ? woDetail.getWoTypeId().toString() : woTypeDto.getWoTypeName());
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return woDetail;
  }

  public ResultInSideDto updateMrconfirm(MrScheduleTelDTO mrScheduleTelDTO,
      MrScheduleTelDTO mrScheduleTelOldDTO, UserToken userToken) {
    ResultInSideDto res;
    List<MrInsideDTO> lstMr = new ArrayList<>();
    if (!StringUtils.isStringNullOrEmpty(mrScheduleTelDTO.getCrId())) {
      // cap nhat nang cap
      // ton tai trong bang cr
      List<CrHisDTO> crHisDtos = mrScheduleTelRepository.checkExistCrId(
          mrScheduleTelDTO.getCrId() == null ? null : mrScheduleTelDTO.getCrId().toString(),
          mrScheduleTelOldDTO.getCrId() == null ? null
              : mrScheduleTelOldDTO.getCrId().toString());
//      CrInsiteDTO crInsiteDTO = crServiceProxy.findCrByIdProxy(Long.valueOf(mrScheduleTelDTO.getCrId()));
      MrScheduleTelDTO objUpdate = mrScheduleTelRepository
          .findById(mrScheduleTelDTO.getScheduleId());
//      if("9".equals(crInsiteDTO.getState())) {
      if (crHisDtos == null || crHisDtos.size() < 1) {
        //                vadidateOk = false;
        res = new ResultInSideDto(null, RESULT.ERROR,
            I18n.getValidation("mrScheduleTel.list.crId.notFoundOrNotValid"));
      } else {

        objUpdate.setCrId(mrScheduleTelDTO.getCrId());
        //                objUpdate.setMrId(null);

        // UPDATE Cycle
        MrCfgProcedureTelDTO procedureDto = new MrCfgProcedureTelDTO();
        procedureDto.setProcedureId(objUpdate.getProcedureId());
        MrCfgProcedureTelDTO dtoNew = mrCfgProcedureTelRepository
            .findMrCfgProcedureTelById(procedureDto.getProcedureId());
        //Set lai chu ky đe insert
        objUpdate.setCycle(dtoNew.getCycle() == null ? null : dtoNew.getCycle().toString());
        objUpdate.setProcedureName(dtoNew.getProcedureName());
        // UPDATE Cycle
        //Update thời gian MR =  thời gian CR
        MrInsideDTO mrDTO = new MrInsideDTO();
        if (objUpdate.getMrId() != null) {
          mrDTO.setMrId(objUpdate.getMrId());
          mrDTO = mrRepository.findMrById(mrDTO.getMrId());
          mrDTO.setEarliestTime(
              DateTimeUtils.convertStringToDate(crHisDtos.get(0).getEarliestStartTime()));
          mrDTO.setLastestTime(
              DateTimeUtils.convertStringToDate(crHisDtos.get(0).getLatestEndTime()));
          lstMr.add(mrDTO);
          if (lstMr != null && !lstMr.isEmpty()) {
            mrRepository.insertListMr(lstMr);
          }
        }
        objUpdate.setMrId(null);
        ResultDTO dto = updateScheduleByCrIdCreatrHis(objUpdate,
            crHisDtos.get(0).getLatestEndTime());

        //                ResultDTO dto = mrScheduleTelServiceImpl.updateScheduleByCrIdCreatrHis(objUpdate, crHisDtos.get(0));
        //remove group code
        if (RESULT.SUCCESS.equals(dto.getMessage())) {
          MrDeviceDTO dtoMD = mrDeviceRepository
              .findMrDeviceById(mrScheduleTelOldDTO.getDeviceId());
          dtoMD.setUpdateUser(userToken.getUserName());
          dtoMD.setUpdateDate(DateTimeUtils
              .convertStringToDate(new SimpleDateFormat("dd/MM/yyyy").format(new Date())));
          dtoMD.setGroupCode(null);
          dtoMD.setIsCompleteSoft(1L);
          if (StringUtils.isNotNullOrEmpty(crHisDtos.get(0).getLatestEndTime())) {
            dtoMD.setLastDate(
                DateTimeUtils.convertStringToDate(crHisDtos.get(0).getLatestEndTime()));
          }
          mrDeviceRepository.updateMrDeviceServices(dtoMD);
        }
        res = new ResultInSideDto(null, dto.getMessage(), null);
      }
//      } else { //tiennv them voi nhanh ko phai la cr dong
//        objUpdate.setMrId(null);
//        objUpdate.setCrId(null);
//        res = mrScheduleTelRepository.updateMrScheduleTel(objUpdate);
//      }
    } else {
      MrScheduleTelDTO objUpdate = mrScheduleTelRepository
          .findById(mrScheduleTelDTO.getScheduleId());
      mrScheduleTelRepository.deleteMrScheduleTel(mrScheduleTelDTO.getScheduleId());

      MrDeviceDTO dtoMD = mrDeviceRepository.findMrDeviceById(mrScheduleTelOldDTO.getDeviceId());
      dtoMD.setUpdateUser(userToken.getUserName());
      dtoMD.setUpdateDate(
          DateTimeUtils.convertStringToDate(new SimpleDateFormat("dd/MM/yyyy").format(new Date())));
//            dtoMD.setGroupCode(null);
      if (!"5".equals(mrScheduleTelDTO.getMrConfirm())) {
        if ("1".equals(objUpdate.getMrSoft())) {
          dtoMD.setMrConfirmSoft(mrScheduleTelDTO.getMrConfirm());
          dtoMD.setMrSoft("0");
        } else {
          dtoMD.setMrConfirmHard(mrScheduleTelDTO.getMrConfirm());
          dtoMD.setMrHard("0");
        }
      }
      res = mrDeviceRepository.updateMrDeviceServices(dtoMD);

    }

    return res;
  }

  public ResultInSideDto updateMrconfirmForImport(MrScheduleTelDTO mrScheduleTelDTO,
      MrScheduleTelDTO mrScheduleTelOldDTO) {
    List<MrInsideDTO> lstMr = new ArrayList<>();
    if (!StringUtils.isStringNullOrEmpty(mrScheduleTelDTO.getCrId())) {
      // cap nhat nang cap
      // ton tai trong bang cr
      List<CrHisDTO> crHisDtos = mrScheduleTelRepository.checkExistCrId(
          mrScheduleTelDTO.getCrId() == null ? null : mrScheduleTelDTO.getCrId().toString(),
          mrScheduleTelOldDTO.getCrId() == null ? null
              : mrScheduleTelOldDTO.getCrId().toString());
//      CrInsiteDTO crInsiteDTO = crServiceProxy.findCrByIdProxy(Long.valueOf(mrScheduleTelDTO.getCrId()));
      MrScheduleTelDTO objUpdate = mrScheduleTelRepository
          .findById(mrScheduleTelDTO.getScheduleId());
//      if("9".equals(crInsiteDTO.getState())) {
      if (crHisDtos == null || crHisDtos.size() < 1) {
        //                vadidateOk = false;
        return new ResultInSideDto(null, RESULT.ERROR,
            I18n.getValidation("mrScheduleTel.list.crId.notFoundOrNotValid"));
      } else {

        objUpdate.setCrId(mrScheduleTelDTO.getCrId());
        //                objUpdate.setMrId(null);

        // UPDATE Cycle
        MrCfgProcedureTelDTO procedureDto = new MrCfgProcedureTelDTO();
        procedureDto.setProcedureId(objUpdate.getProcedureId());
        MrCfgProcedureTelDTO dtoNew = mrCfgProcedureTelRepository
            .findMrCfgProcedureTelById(procedureDto.getProcedureId());
        //Set lai chu ky đe insert
        objUpdate.setCycle(dtoNew.getCycle() == null ? null : dtoNew.getCycle().toString());
        objUpdate.setProcedureName(dtoNew.getProcedureName());
        // UPDATE Cycle
        //Update thời gian MR =  thời gian CR
        MrInsideDTO mrDTO = new MrInsideDTO();
        if (objUpdate.getMrId() != null) {
          mrDTO.setMrId(objUpdate.getMrId());
          mrDTO = mrRepository.findMrById(mrDTO.getMrId());
          mrDTO.setEarliestTime(
              DateTimeUtils.convertStringToDate(crHisDtos.get(0).getEarliestStartTime()));
          mrDTO.setLastestTime(
              DateTimeUtils.convertStringToDate(crHisDtos.get(0).getLatestEndTime()));
          lstMr.add(mrDTO);
          if (lstMr != null && !lstMr.isEmpty()) {
            mrRepository.insertListMr(lstMr);
          }
        }
        objUpdate.setMrId(null);
        ResultDTO dto = updateScheduleByCrIdCreatrHis(objUpdate,
            crHisDtos.get(0).getLatestEndTime());

        //                ResultDTO dto = mrScheduleTelServiceImpl.updateScheduleByCrIdCreatrHis(objUpdate, crHisDtos.get(0));
        return new ResultInSideDto(null, dto.getMessage(), null);
      }
//      } else { //tiennv them voi nhanh ko phai la cr dong
//        objUpdate.setMrId(null);
//        objUpdate.setCrId(null);
//        res = mrScheduleTelRepository.updateMrScheduleTel(objUpdate);
//      }
    }
    return new ResultInSideDto(null, RESULT.ERROR, null);
  }
}
