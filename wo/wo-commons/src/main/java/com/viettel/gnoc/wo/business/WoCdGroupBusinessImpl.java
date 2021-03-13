package com.viettel.gnoc.wo.business;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.*;
import com.viettel.gnoc.commons.proxy.CommonStreamServiceProxy;
import com.viettel.gnoc.commons.proxy.WoCategoryServiceProxy;
import com.viettel.gnoc.commons.repository.CatItemRepository;
import com.viettel.gnoc.commons.repository.ReceiveUnitRepository;
import com.viettel.gnoc.commons.repository.UserRepository;
import com.viettel.gnoc.commons.utils.CellConfigExport;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.ConfigFileExport;
import com.viettel.gnoc.commons.utils.ConfigHeaderExport;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.APPLIED_BUSSINESS;
import com.viettel.gnoc.commons.utils.Constants.LANGUAGUE_EXCHANGE_SYSTEM;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.Constants.WO_CD_GROUP_MASTER_CODE;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.DateUtil;
import com.viettel.gnoc.commons.utils.ExcelWriterUtils;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.wo.dto.WoCdDTO;
import com.viettel.gnoc.wo.dto.WoCdGroupInsideDTO;
import com.viettel.gnoc.wo.dto.WoCdGroupTypeUserDTO;
import com.viettel.gnoc.wo.dto.WoCdGroupUnitDTO;
import com.viettel.gnoc.wo.dto.WoTypeGroupDTO;
import com.viettel.gnoc.wo.dto.WoTypeInsideDTO;
import com.viettel.gnoc.wo.repository.WoCdGroupRepository;
import com.viettel.gnoc.wo.repository.WoCdGroupUnitRepository;
import com.viettel.gnoc.wo.repository.WoCdRepository;
import com.viettel.gnoc.wo.repository.WoTypeGroupRepository;
import com.viettel.gnoc.wo.repository.WoTypeRepository;
import com.viettel.gnoc.wo.utils.NimsStationForm;
import com.viettel.gnoc.wo.utils.WSNIMS_HTPort;
import com.viettel.gnoc.wo.utils.WSNIMS_HT_GLOBAL_Port;
import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataValidationConstraint.ValidationType;
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
public class WoCdGroupBusinessImpl implements WoCdGroupBusiness {

  @Value("${application.temp.folder}")
  private String tempFolder;

  @Autowired
  WoCdGroupRepository woCdGroupRepository;

  @Autowired
  WoCdGroupUnitRepository woCdGroupUnitRepository;

  @Autowired
  ReceiveUnitRepository receiveUnitRepository;

  @Autowired
  WoCdRepository woCdRepository;

  @Autowired
  UserRepository userRepository;

  @Autowired
  WoTypeGroupRepository woTypeGroupRepository;

  @Autowired
  WoTypeRepository woTypeRepository;

  @Autowired
  CatItemRepository catItemRepository;

  @Autowired
  WoCategoryServiceProxy woCategoryServiceProxy;

  @Autowired
  WSNIMS_HTPort wsnimsHtPort;

  @Autowired
  WSNIMS_HT_GLOBAL_Port wsnimsHtGlobalPort;

  @Autowired
  protected CommonStreamServiceProxy commonStreamServiceProxy;

  @Autowired
  protected TicketProvider ticketProvider;

  private int maxRecord = 500;
  Map<Long, String> mapNation = new HashMap<>();
  Map<Long, String> mapGroupType = new HashMap<>();
  Map<String, String> mapWoType = new HashMap<>();

  @Override
  public Datatable getListWoCdGroupDTO(WoCdGroupInsideDTO woCdGroupInsideDTO) {
    log.debug("Request to getListWoCdGroupDTO: {}", woCdGroupInsideDTO);
//    if (woCdGroupInsideDTO.getWoTypeId() != null) {
//      WoTypeGroupDTO woTypeGroupDTO = new WoTypeGroupDTO();
//      woTypeGroupDTO.setWoTypeId(woCdGroupInsideDTO.getWoTypeId());
//      List<WoTypeGroupDTO> listTypeGroup = woTypeGroupRepository
//          .getListWoTypeGroupDTO(woTypeGroupDTO);
//      if (listTypeGroup != null && listTypeGroup.size() > 0) {
//        List<Long> listWoGroupId = new ArrayList<>();
//        for (WoTypeGroupDTO typeGroupDTO : listTypeGroup) {
//          listWoGroupId.add(typeGroupDTO.getWoGroupId());
//        }
//        woCdGroupInsideDTO.setListWoGroupId(listWoGroupId);
//      }
//    }
    return woCdGroupRepository.getListWoCdGroupDTO(woCdGroupInsideDTO);
  }

  @Override
  public WoCdGroupInsideDTO findWoCdGroupById(Long woCdGroupDTOId) {
    log.debug("Request to findWoCdGroupById: {}", woCdGroupDTOId);
    return woCdGroupRepository.findWoCdGroupById(woCdGroupDTOId);
  }

  @Override
  public ResultInSideDto insertWoCdGroup(WoCdGroupInsideDTO woCdGroupInsideDTO) {
    log.debug("Request to insertWoCdGroup: {}", woCdGroupInsideDTO);
    ResultInSideDto resultInSideDto;
    String woGroupCode = woCdGroupInsideDTO.getWoGroupCode().toUpperCase();
    woCdGroupInsideDTO.setWoGroupCode(woGroupCode);
    resultInSideDto = woCdGroupRepository.insertWoCdGroup(woCdGroupInsideDTO);
    if (resultInSideDto.getKey().equals(RESULT.SUCCESS)) {
      //Add history
      try {
        UserToken userToken = ticketProvider.getUserToken();
        List<String> keys = getAllKeysDTO();
        DataHistoryChange dataHistoryChange = new DataHistoryChange();
        dataHistoryChange.setRecordId(resultInSideDto.getId().toString());
        dataHistoryChange.setType("WO_CD_GROUP_MANAGEMENT");
        //Old Object History
        dataHistoryChange.setOldObject(new WoCdGroupInsideDTO());
        dataHistoryChange.setActionType("add");
        //New Object History
        dataHistoryChange.setNewObject(woCdGroupInsideDTO);
        dataHistoryChange.setUserId(userToken.getUserID().toString());
        dataHistoryChange.setKeys(keys);
        commonStreamServiceProxy.insertHisUserImpact(dataHistoryChange);
      } catch (Exception err) {
        log.error(err.getMessage());
      }
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto updateWoCdGroup(WoCdGroupInsideDTO woCdGroupInsideDTO) {
    log.debug("Request to updateWoCdGroup: {}", woCdGroupInsideDTO);
    ResultInSideDto resultInSideDto;
    String woGroupCode = woCdGroupInsideDTO.getWoGroupCode().toUpperCase();
    woCdGroupInsideDTO.setWoGroupCode(woGroupCode);
    WoCdGroupInsideDTO oldHis = findWoCdGroupById(woCdGroupInsideDTO.getWoGroupId());
    resultInSideDto = woCdGroupRepository.updateWoCdGroup(woCdGroupInsideDTO);
    if (resultInSideDto.getKey().equals(RESULT.SUCCESS)) {
      //Add history
      try {
        UserToken userToken = ticketProvider.getUserToken();
        List<String> keys = getAllKeysDTO();
        DataHistoryChange dataHistoryChange = new DataHistoryChange();
        dataHistoryChange.setRecordId(woCdGroupInsideDTO.getWoGroupId().toString());
        dataHistoryChange.setType("WO_CD_GROUP_MANAGEMENT");
        //Old Object History
        dataHistoryChange.setOldObject(oldHis);
        dataHistoryChange.setActionType("update");
        //New Object History
        dataHistoryChange.setNewObject(woCdGroupInsideDTO);
        dataHistoryChange.setUserId(userToken.getUserID().toString());
        dataHistoryChange.setKeys(keys);
        commonStreamServiceProxy.insertHisUserImpact(dataHistoryChange);
      } catch (Exception err) {
        log.error(err.getMessage());
      }
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto deleteWoCdGroup(Long woGroupId) {
    log.debug("Request to deleteWoCdGroup: {}", woGroupId);
    ResultInSideDto resultInSideDto;
    WoCdGroupInsideDTO oldHis = findWoCdGroupById(woGroupId);
    resultInSideDto = woCdGroupRepository.deleteWoCdGroup(woGroupId);
    if (RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
      //Add history
      try {
        UserToken userToken = ticketProvider.getUserToken();
        List<String> keys = getAllKeysDTO();
        DataHistoryChange dataHistoryChange = new DataHistoryChange();
        dataHistoryChange.setRecordId(woGroupId.toString());
        //Old Object History
        dataHistoryChange.setOldObject(oldHis);
        //New Object History
        dataHistoryChange.setNewObject(new WoCdGroupInsideDTO());
        dataHistoryChange.setType("WO_CD_GROUP_MANAGEMENT");
        dataHistoryChange.setActionType("delete");
        dataHistoryChange.setUserId(userToken.getUserID().toString());
        dataHistoryChange.setKeys(keys);
        commonStreamServiceProxy.insertHisUserImpact(dataHistoryChange);
      } catch (Exception err) {
        log.error(err.getMessage());
      }
      woCdRepository.deleteWoCdByWoGroupId(woGroupId);
      woCdGroupUnitRepository.deleteWoCdGroupUnitByCdGroupId(woGroupId);
      resultInSideDto = woTypeGroupRepository.deleteWoTypeGroupByWoGroupId(woGroupId);
    }
    return resultInSideDto;
  }

  @Override
  public List<WoCdGroupInsideDTO> getListCdGroupByUser(WoCdGroupTypeUserDTO woCdGroupTypeUserDTO) {
    log.debug("Request to getListCdGroupByUser: {}", woCdGroupTypeUserDTO);
    return woCdGroupRepository.getListCdGroupByUser(woCdGroupTypeUserDTO);
  }

  @Override
  public List<ReceiveUnitDTO> getListWoCdGroupUnitDTO(WoCdGroupUnitDTO woCdGroupUnitDTO) {
    log.debug("Request to getListWoCdGroupUnitDTO: {}", woCdGroupUnitDTO);
    List<WoCdGroupUnitDTO> listWoCdGroupUnit = woCdGroupUnitRepository
        .getListWoCdGroupUnitDTO(woCdGroupUnitDTO);
    List<Long> listUnitId = new ArrayList<>();
    if (listWoCdGroupUnit != null && !listWoCdGroupUnit.isEmpty()) {
      for (WoCdGroupUnitDTO unitDTO : listWoCdGroupUnit) {
        listUnitId.add(unitDTO.getUnitId());
      }
      return receiveUnitRepository.getListReceiveUnit(listUnitId);
    }
    return null;
  }

  @Override
  public ResultInSideDto updateWoCdGroupUnit(WoCdGroupUnitDTO woCdGroupUnitDTO) {
    log.debug("Request to updateWoCdGroupUnit: {}", woCdGroupUnitDTO);
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    if (!woCdGroupUnitDTO.getListUnitIdDel().isEmpty()) {
      resultInSideDto = deleteListWoCdGroupUnit(woCdGroupUnitDTO);
    }
    if (!woCdGroupUnitDTO.getListUnitIdInsert().isEmpty()) {
      for (Long unitIdInsert : woCdGroupUnitDTO.getListUnitIdInsert()) {
        woCdGroupUnitDTO.setUnitId(unitIdInsert);
        resultInSideDto = woCdGroupUnitRepository.insertWoCdGroupUnit(woCdGroupUnitDTO);
      }
    }
    if (woCdGroupUnitDTO.getListUnitIdDel().isEmpty() &&
        woCdGroupUnitDTO.getListUnitIdInsert().isEmpty()) {
      resultInSideDto = woCdGroupUnitRepository.updateWoCdGroupUnit(woCdGroupUnitDTO);
    }
    return resultInSideDto;
  }

  public ResultInSideDto deleteListWoCdGroupUnit(WoCdGroupUnitDTO woCdGroupUnitDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    List<WoCdGroupUnitDTO> listWoCdGroupUnit = woCdGroupUnitRepository
        .getListWoCdGroupUnitDTO(woCdGroupUnitDTO);
    List<Long> listCdGroupUnitId = new ArrayList<>();
    if (listWoCdGroupUnit != null && !listWoCdGroupUnit.isEmpty()) {
      for (WoCdGroupUnitDTO dto : listWoCdGroupUnit) {
        listCdGroupUnitId.add(dto.getCdGroupUnitId());
      }
      for (Long cdGroupUnitId : listCdGroupUnitId) {
        resultInSideDto = woCdGroupUnitRepository.deleteWoCdGroupUnit(cdGroupUnitId);
      }
      return resultInSideDto;
    }
    return null;
  }

  @Override
  public List<UsersInsideDto> getListWoCdDTO(WoCdDTO woCdDTO) {
    log.debug("Request to getListWoCdDTO: {}", woCdDTO);
    List<WoCdDTO> listWoCd = woCdRepository.getListWoCdDTO(woCdDTO);
    List<Long> listUserId = new ArrayList<>();
    if (listWoCd != null && !listWoCd.isEmpty()) {
      for (WoCdDTO cdDTO : listWoCd) {
        listUserId.add(cdDTO.getUserId());
      }
      return userRepository.getListUsersByListUserId(listUserId);
    }
    return null;
  }

  @Override
  public ResultInSideDto updateWoCd(WoCdDTO woCdDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    if (!woCdDTO.getListUserIdDel().isEmpty()) {
      resultInSideDto = deleteListWoCd(woCdDTO);
    }
    if (!woCdDTO.getListUserIdInsert().isEmpty()) {
      for (Long userIdInsert : woCdDTO.getListUserIdInsert()) {
        woCdDTO.setUserId(userIdInsert);
        resultInSideDto = woCdRepository.insertWoCd(woCdDTO);
      }
    }
    if (woCdDTO.getListUserIdDel().isEmpty() && woCdDTO.getListUserIdInsert().isEmpty()) {
      resultInSideDto = woCdRepository.updateWoCd(woCdDTO);
    }
    return resultInSideDto;
  }

  public ResultInSideDto deleteListWoCd(WoCdDTO woCdDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    List<WoCdDTO> listWoCd = woCdRepository.getListWoCdDTO(woCdDTO);
    List<Long> listCdId = new ArrayList<>();
    if (listWoCd != null && !listWoCd.isEmpty()) {
      for (WoCdDTO cdDTO : listWoCd) {
        listCdId.add(cdDTO.getCdId());
      }
      for (Long cdId : listCdId) {
        resultInSideDto = woCdRepository.deleteWoCd(cdId);
      }
      return resultInSideDto;
    }
    return null;
  }

  @Override
  public List<WoTypeInsideDTO> getListWoTypeGroupDTO(WoTypeGroupDTO woTypeGroupDTO) {
    log.debug("Request to getListTypeGroupDTO: {}", woTypeGroupDTO);
    List<WoTypeGroupDTO> listWoTypeGroup = woTypeGroupRepository
        .getListWoTypeGroupDTO(woTypeGroupDTO);
    List<Long> listWoTypeId = new ArrayList<>();
    if (listWoTypeGroup != null && !listWoTypeGroup.isEmpty()) {
      for (WoTypeGroupDTO wtgDTO : listWoTypeGroup) {
        listWoTypeId.add(wtgDTO.getWoTypeId());
      }
      return woTypeRepository.getListWoTypeForWoCdGroup(listWoTypeId);
    }
    return null;
  }

  @Override
  public List<WoTypeInsideDTO> getListWoTypeAll() {
    return woTypeRepository.getListWoTypeForWoCdGroup(null);
  }

  @Override
  public ResultInSideDto updateWoTypeGroup(WoTypeGroupDTO woTypeGroupDTO) {
    ResultInSideDto resultInSideDto;
    if (!woTypeGroupDTO.getListWoTypeIdInsert().isEmpty()) {
      resultInSideDto = deleteListWoTypeGroup(woTypeGroupDTO);
      for (Long woTypeIdInsert : woTypeGroupDTO.getListWoTypeIdInsert()) {
        woTypeGroupDTO.setWoTypeId(woTypeIdInsert);
        resultInSideDto = woTypeGroupRepository.insertWoTypeGroup(woTypeGroupDTO);
      }
    } else {
      resultInSideDto = woTypeGroupRepository.updateWoTypeGroup(woTypeGroupDTO);
    }
    return resultInSideDto;
  }

  public ResultInSideDto deleteListWoTypeGroup(WoTypeGroupDTO woTypeGroupDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    List<WoTypeGroupDTO> listWoTypeGroup = woTypeGroupRepository
        .getListWoTypeGroupDTO(woTypeGroupDTO);
    List<Long> listWoTypeGroupId = new ArrayList<>();
    if (listWoTypeGroup != null && !listWoTypeGroup.isEmpty()) {
      for (WoTypeGroupDTO dto : listWoTypeGroup) {
        listWoTypeGroupId.add(dto.getWoTypeGroupId());
      }
      for (Long woTypeGroupId : listWoTypeGroupId) {
        resultInSideDto = woTypeGroupRepository.deleteWoTypeGroup(woTypeGroupId);
      }
      return resultInSideDto;
    }
    return null;
  }

  @Override
  public File exportData(WoCdGroupInsideDTO woCdGroupInsideDTO) throws Exception {
    List<WoCdGroupInsideDTO> list = woCdGroupRepository.getListWoCdGroupExport(woCdGroupInsideDTO);
    if (list != null && !list.isEmpty()) {
      for (WoCdGroupInsideDTO dto : list) {
        if ("0".equals(String.valueOf(dto.getIsEnable()))) {
          dto.setIsEnableName(I18n.getLanguage("woCdGroup.isEnable.0"));
        }
        if ("1".equals(String.valueOf(dto.getIsEnable()))) {
          dto.setIsEnableName(I18n.getLanguage("woCdGroup.isEnable.1"));
        }
      }
    }
    String[] header = new String[]{"woGroupCode", "woGroupName", "groupTypeName", "nationName",
        "email", "mobile", "isEnableName"};
    Date date = DateTimeUtils.convertDateOffset();
    return handleFileExport(list, header, DateUtil.date2ddMMyyyyHHMMss(date),
        "EXPORT_WO_CD_GROUP");
  }

  @Override
  public File getTemplateImport() throws Exception {
    ExcelWriterUtils ewu = new ExcelWriterUtils();
    String templatePathOut = "templates" + File.separator + "TEMPLATE_EXPORT.xlsx";
    Resource resource = new ClassPathResource(templatePathOut);
    InputStream fileTemplate = resource.getInputStream();
    XSSFWorkbook workBook = new XSSFWorkbook(fileTemplate);

    Map<String, CellStyle> styles = CommonExport.createStyles(workBook);

    XSSFSheet sheetMain = workBook.getSheetAt(0);
    XSSFSheet sheetOrther = workBook.createSheet("Orther");

    String[] header = new String[]{
        I18n.getLanguage("common.STT"),
        I18n.getLanguage("woCdGroup.groupTypeName"),
        I18n.getLanguage("woCdGroup.woGroupCode"),
        I18n.getLanguage("woCdGroup.woGroupName"),
        I18n.getLanguage("woCdGroup.nationName"),
        I18n.getLanguage("woCdGroup.email"),
        I18n.getLanguage("woCdGroup.mobile"),
        I18n.getLanguage("woCdGroup.isEnableName"),
        I18n.getLanguage("woCdGroup.actionName")
    };
    String[] headerStar = new String[]{
        I18n.getLanguage("woCdGroup.groupTypeName"),
        I18n.getLanguage("woCdGroup.woGroupCode"),
        I18n.getLanguage("woCdGroup.woGroupName"),
        I18n.getLanguage("woCdGroup.nationName"),
        I18n.getLanguage("woCdGroup.isEnableName"),
        I18n.getLanguage("woCdGroup.actionName")
    };
    XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(sheetMain);
    List<String> listHeader = Arrays.asList(header);
    List<String> listHeaderStar = Arrays.asList(headerStar);

    int sttColumn = listHeader.indexOf(I18n.getLanguage("common.STT"));
    int groupTypeNameColumn = listHeader.indexOf(I18n.getLanguage("woCdGroup.groupTypeName"));
    int woGroupCodeColumn = listHeader.indexOf(I18n.getLanguage("woCdGroup.woGroupCode"));
    int woGroupNameColumn = listHeader.indexOf(I18n.getLanguage("woCdGroup.woGroupName"));
    int nationNameColumn = listHeader.indexOf(I18n.getLanguage("woCdGroup.nationName"));
    int emailColumn = listHeader.indexOf(I18n.getLanguage("woCdGroup.email"));
    int mobileColumn = listHeader.indexOf(I18n.getLanguage("woCdGroup.mobile"));
    int isEnableNameColumn = listHeader.indexOf(I18n.getLanguage("woCdGroup.isEnableName"));
    int actionNameColumn = listHeader.indexOf(I18n.getLanguage("woCdGroup.actionName"));

    String firstLeftHeaderTitle = I18n.getLanguage("common.export.firstLeftHeader");
    String secondLeftHeaderTitle = I18n.getLanguage("common.export.secondLeftHeader");
    String firstRightHeaderTitle = I18n.getLanguage("common.export.firstRightHeader");
    String secondRightHeaderTitle = I18n.getLanguage("common.export.secondRightHeader");

    Font xssFontTopHeader = workBook.createFont();
    xssFontTopHeader.setFontName("Times New Roman");
    xssFontTopHeader.setFontHeightInPoints((short) 10);
    xssFontTopHeader.setColor(IndexedColors.BLACK.index);
    xssFontTopHeader.setBold(true);

    Font xSSFFont = workBook.createFont();
    xSSFFont.setFontName(HSSFFont.FONT_ARIAL);
    xSSFFont.setFontHeightInPoints((short) 20);
    xSSFFont.setBold(true);
    xSSFFont.setColor(IndexedColors.BLACK.index);

    Font xSSFFontHeader = workBook.createFont();
    xSSFFontHeader.setFontName(HSSFFont.FONT_ARIAL);
    xSSFFontHeader.setFontHeightInPoints((short) 10);
    xSSFFontHeader.setColor(IndexedColors.BLUE.index);
    xSSFFontHeader.setBold(true);

    Font rowDataFont = workBook.createFont();
    rowDataFont.setFontName(HSSFFont.FONT_ARIAL);
    rowDataFont.setFontHeightInPoints((short) 10);
    rowDataFont.setColor(IndexedColors.BLACK.index);

    CellStyle cellStyleTopHeader = workBook.createCellStyle();
    cellStyleTopHeader.setAlignment(HorizontalAlignment.CENTER);
    cellStyleTopHeader.setVerticalAlignment(VerticalAlignment.CENTER);
    cellStyleTopHeader.setFont(xssFontTopHeader);

    CellStyle cellStyleTopRightHeader = workBook.createCellStyle();
    cellStyleTopRightHeader.setAlignment(HorizontalAlignment.LEFT);
    cellStyleTopRightHeader.setVerticalAlignment(VerticalAlignment.CENTER);
    cellStyleTopRightHeader.setFont(xssFontTopHeader);

    CellStyle cellStyleTitle = workBook.createCellStyle();
    cellStyleTitle.setAlignment(HorizontalAlignment.CENTER);
    cellStyleTitle.setVerticalAlignment(VerticalAlignment.CENTER);
    cellStyleTitle.setFillForegroundColor(IndexedColors.WHITE.index);
    cellStyleTitle.setFont(xSSFFont);

    CellStyle cellStyleHeader = workBook.createCellStyle();
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

    CellStyle cellText = workBook.createCellStyle();
    cellText.setDataFormat((short) BuiltinFormats.getBuiltinFormat("text"));

    //Tao quoc hieu
    Row headerFirstTitle = sheetMain.createRow(0);
    Row headerSecondTitle = sheetMain.createRow(1);
    int sizeheaderTitle = 7;
    Cell firstLeftHeader = headerFirstTitle.createCell(1);
    firstLeftHeader.setCellStyle(cellStyleTopHeader);
    Cell secondLeftHeader = headerSecondTitle.createCell(1);
    secondLeftHeader.setCellStyle(cellStyleTopHeader);
    Cell firstRightHeader = headerFirstTitle.createCell(sizeheaderTitle - 2);
    firstRightHeader.setCellStyle(cellStyleTopRightHeader);
    Cell secondRightHeader = headerSecondTitle.createCell(sizeheaderTitle - 2);
    secondRightHeader.setCellStyle(cellStyleTopRightHeader);
    firstLeftHeader.setCellValue(firstLeftHeaderTitle);
    secondLeftHeader.setCellValue(secondLeftHeaderTitle);
    firstRightHeader.setCellValue(firstRightHeaderTitle);
    secondRightHeader.setCellValue(secondRightHeaderTitle);
    sheetMain.addMergedRegion(new CellRangeAddress(0, 0, 1,
        3));
    sheetMain.addMergedRegion(new CellRangeAddress(1, 1, 1,
        3));
    sheetMain.addMergedRegion(new CellRangeAddress(0, 0, sizeheaderTitle - 2,
        sizeheaderTitle + 1));
    sheetMain.addMergedRegion(new CellRangeAddress(1, 1, sizeheaderTitle - 2,
        sizeheaderTitle + 1));

    //Tao tieu de
    Row rowMainTitle = sheetMain.createRow(3);
    Cell mainCellTitle = rowMainTitle.createCell(1);
    mainCellTitle.setCellValue(I18n.getLanguage("woCdGroup.template.title"));
    mainCellTitle.setCellStyle(cellStyleTitle);
    sheetMain.addMergedRegion(new CellRangeAddress(3, 3, 1, 7));

    XSSFFont fontStar = workBook.createFont();
    fontStar.setColor(IndexedColors.RED.getIndex());

    //Tao header
    Row headerRow = sheetMain.createRow(5);
    headerRow.setHeight((short) 600);
    for (int i = 0; i < listHeader.size(); i++) {
      Cell headerCell = headerRow.createCell(i);
      XSSFRichTextString rt = new XSSFRichTextString(listHeader.get(i));
      for (String checkHeaderStar : listHeaderStar) {
        if (checkHeaderStar.equalsIgnoreCase(listHeader.get(i))) {
          rt.append(" (*)", fontStar);
        }
      }
      headerCell.setCellValue(rt);
      headerCell.setCellStyle(cellStyleHeader);
    }

    Row rowMobile;
    for (int i = 6; i <= (maxRecord + 6); i++) {
      rowMobile = sheetMain.createRow(i);
      Cell cellMobile = rowMobile.createCell(6);
      cellMobile.setCellStyle(cellText);
    }

    int row = 1;
    Datatable dataGroupType = catItemRepository
        .getItemMaster(WO_CD_GROUP_MASTER_CODE.WO_CD_GROUP_TYPE,
            LANGUAGUE_EXCHANGE_SYSTEM.COMMON, APPLIED_BUSSINESS.CAT_ITEM.toString(),
            Constants.ITEM_ID,
            Constants.ITEM_NAME);
    List<CatItemDTO> listGroupType = (List<CatItemDTO>) dataGroupType.getData();
    for (CatItemDTO dto : listGroupType) {
      ewu.createCell(sheetOrther, 0, row++, dto.getItemName(), styles.get("cell"));
    }
    Name groupTypeName = workBook.createName();
    groupTypeName.setNameName("groupTypeName");
    groupTypeName.setRefersToFormula("Orther!$A$2:$A$" + row);
    XSSFDataValidationConstraint groupTypeConstraint = new XSSFDataValidationConstraint(
        ValidationType.LIST, "groupTypeName");
    CellRangeAddressList cellRangeGroupType = new CellRangeAddressList(6, (maxRecord + 6),
        groupTypeNameColumn, groupTypeNameColumn);
    XSSFDataValidation dataValidationGroupType = (XSSFDataValidation) dvHelper.createValidation(
        groupTypeConstraint, cellRangeGroupType);
    dataValidationGroupType.setShowErrorBox(true);
    sheetMain.addValidationData(dataValidationGroupType);

    row = 1;
    Datatable dataNation = catItemRepository.getItemMaster(WO_CD_GROUP_MASTER_CODE.GNOC_COUNTRY,
        LANGUAGUE_EXCHANGE_SYSTEM.COMMON, APPLIED_BUSSINESS.CAT_ITEM.toString(),
        Constants.ITEM_ID,
        Constants.ITEM_NAME);
    List<CatItemDTO> listNation = (List<CatItemDTO>) dataNation.getData();
    for (CatItemDTO dto : listNation) {
      ewu.createCell(sheetOrther, 1, row++, dto.getItemName(), styles.get("cell"));
    }
    Name nationName = workBook.createName();
    nationName.setNameName("nationName");
    nationName.setRefersToFormula("Orther!$B$2:$B$" + row);
    XSSFDataValidationConstraint nationConstraint = new XSSFDataValidationConstraint(
        ValidationType.LIST, "nationName");
    CellRangeAddressList cellRangeNation = new CellRangeAddressList(6, (maxRecord + 6),
        nationNameColumn, nationNameColumn);
    XSSFDataValidation dataValidationNation = (XSSFDataValidation) dvHelper.createValidation(
        nationConstraint, cellRangeNation);
    dataValidationNation.setShowErrorBox(true);
    sheetMain.addValidationData(dataValidationNation);

    row = 1;
    ewu.createCell(sheetOrther, 2, row++, I18n.getLanguage("woCdGroup.isEnable.1"),
        styles.get("cell"));
    ewu.createCell(sheetOrther, 2, row++, I18n.getLanguage("woCdGroup.isEnable.0"),
        styles.get("cell"));
    Name isEnableName = workBook.createName();
    isEnableName.setNameName("isEnableName");
    isEnableName.setRefersToFormula("Orther!$C$2:$C$" + row);
    XSSFDataValidationConstraint isEnableConstraint = new XSSFDataValidationConstraint(
        ValidationType.LIST, "isEnableName");
    CellRangeAddressList cellRangeIsEnable = new CellRangeAddressList(6, (maxRecord + 6),
        isEnableNameColumn, isEnableNameColumn);
    XSSFDataValidation dataValidationIsEnable = (XSSFDataValidation) dvHelper.createValidation(
        isEnableConstraint, cellRangeIsEnable);
    dataValidationIsEnable.setShowErrorBox(true);
    sheetMain.addValidationData(dataValidationIsEnable);

    row = 1;
    ewu.createCell(sheetOrther, 3, row++, I18n.getLanguage("woCdGroup.action.0"),
        styles.get("cell"));
    ewu.createCell(sheetOrther, 3, row++, I18n.getLanguage("woCdGroup.action.1"),
        styles.get("cell"));
    Name actionName = workBook.createName();
    actionName.setNameName("actionName");
    actionName.setRefersToFormula("Orther!$D$2:$D$" + row);
    XSSFDataValidationConstraint actionConstraint = new XSSFDataValidationConstraint(
        ValidationType.LIST, "actionName");
    CellRangeAddressList cellRangeAction = new CellRangeAddressList(6, (maxRecord + 6),
        actionNameColumn, actionNameColumn);
    XSSFDataValidation dataValidationAction = (XSSFDataValidation) dvHelper.createValidation(
        actionConstraint, cellRangeAction);
    dataValidationAction.setShowErrorBox(true);
    sheetMain.addValidationData(dataValidationAction);

    sheetMain.setColumnWidth(sttColumn, 2000);
    sheetMain.setColumnWidth(groupTypeNameColumn, 8000);
    sheetMain.setColumnWidth(woGroupCodeColumn, 7000);
    sheetMain.setColumnWidth(woGroupNameColumn, 7000);
    sheetMain.setColumnWidth(nationNameColumn, 4000);
    sheetMain.setColumnWidth(emailColumn, 6000);
    sheetMain.setColumnWidth(mobileColumn, 4000);
    sheetMain.setColumnWidth(isEnableNameColumn, 4000);
    sheetMain.setColumnWidth(actionNameColumn, 4000);

    sheetOrther.setSelected(false);
    workBook.setSheetName(0, I18n.getLanguage("woCdGroup.import.title"));
    workBook.setSheetHidden(1, true);

    String fileResult = tempFolder + File.separator;
    String fileName = "TEMPLATE_IMPORT" + "_" + System.currentTimeMillis() + ".xlsx";
    ewu.saveToFileExcel(workBook, fileResult, fileName);
    String resultPath = fileResult + fileName;
    File fileExport = new File(resultPath);
    return fileExport;
  }

  @Override
  public File getTemplateAssignUser() throws Exception {
    ExcelWriterUtils ewu = new ExcelWriterUtils();
    String templatePathOut = "templates" + File.separator + "TEMPLATE_EXPORT.xlsx";
    Resource resource = new ClassPathResource(templatePathOut);
    InputStream fileTemplate = resource.getInputStream();
    XSSFWorkbook workBook = new XSSFWorkbook(fileTemplate);

    Map<String, CellStyle> styles = CommonExport.createStyles(workBook);

    XSSFSheet sheetMain = workBook.getSheetAt(0);
    XSSFSheet sheetOrther = workBook.createSheet("Orther");

    String[] header = new String[]{
        I18n.getLanguage("common.STT"),
        I18n.getLanguage("woCd.woGroupCode"),
        I18n.getLanguage("woCd.userName.choose"),
        I18n.getLanguage("woCd.assign")
    };
    String[] headerStar = new String[]{
        I18n.getLanguage("woCd.woGroupCode"),
        I18n.getLanguage("woCd.userName.choose"),
        I18n.getLanguage("woCd.assign")
    };

    XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(sheetMain);
    List<String> listHeader = Arrays.asList(header);
    List<String> listHeaderStar = Arrays.asList(headerStar);

    int sttColumn = listHeader.indexOf(I18n.getLanguage("common.STT"));
    int woGroupCodeColumn = listHeader.indexOf(I18n.getLanguage("woCd.woGroupCode"));
    int userNameChooseColumn = listHeader.indexOf(I18n.getLanguage("woCd.userName.choose"));
    int assignColumn = listHeader.indexOf(I18n.getLanguage("woCd.assign"));

    String firstLeftHeaderTitle = I18n.getLanguage("common.export.firstLeftHeader");
    String secondLeftHeaderTitle = I18n.getLanguage("common.export.secondLeftHeader");
    String firstRightHeaderTitle = I18n.getLanguage("common.export.firstRightHeader");
    String secondRightHeaderTitle = I18n.getLanguage("common.export.secondRightHeader");

    Font xssFontTopHeader = workBook.createFont();
    xssFontTopHeader.setFontName("Times New Roman");
    xssFontTopHeader.setFontHeightInPoints((short) 10);
    xssFontTopHeader.setColor(IndexedColors.BLACK.index);
    xssFontTopHeader.setBold(true);

    Font xSSFFont = workBook.createFont();
    xSSFFont.setFontName(HSSFFont.FONT_ARIAL);
    xSSFFont.setFontHeightInPoints((short) 20);
    xSSFFont.setBold(true);
    xSSFFont.setColor(IndexedColors.BLACK.index);

    Font xSSFFontHeader = workBook.createFont();
    xSSFFontHeader.setFontName(HSSFFont.FONT_ARIAL);
    xSSFFontHeader.setFontHeightInPoints((short) 10);
    xSSFFontHeader.setColor(IndexedColors.BLUE.index);
    xSSFFontHeader.setBold(true);

    Font rowDataFont = workBook.createFont();
    rowDataFont.setFontName(HSSFFont.FONT_ARIAL);
    rowDataFont.setFontHeightInPoints((short) 10);
    rowDataFont.setColor(IndexedColors.BLACK.index);

    CellStyle cellStyleTopHeader = workBook.createCellStyle();
    cellStyleTopHeader.setAlignment(HorizontalAlignment.CENTER);
    cellStyleTopHeader.setVerticalAlignment(VerticalAlignment.CENTER);
    cellStyleTopHeader.setFont(xssFontTopHeader);

    CellStyle cellStyleTopRightHeader = workBook.createCellStyle();
    cellStyleTopRightHeader.setAlignment(HorizontalAlignment.LEFT);
    cellStyleTopRightHeader.setVerticalAlignment(VerticalAlignment.CENTER);
    cellStyleTopRightHeader.setFont(xssFontTopHeader);

    CellStyle cellStyleTitle = workBook.createCellStyle();
    cellStyleTitle.setAlignment(HorizontalAlignment.CENTER);
    cellStyleTitle.setVerticalAlignment(VerticalAlignment.CENTER);
    cellStyleTitle.setFillForegroundColor(IndexedColors.WHITE.index);
    cellStyleTitle.setFont(xSSFFont);

    CellStyle cellStyleHeader = workBook.createCellStyle();
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

    //Tao quoc hieu
    Row headerFirstTitle = sheetMain.createRow(0);
    Row headerSecondTitle = sheetMain.createRow(1);
    Cell firstLeftHeader = headerFirstTitle.createCell(0);
    firstLeftHeader.setCellStyle(cellStyleTopHeader);
    Cell secondLeftHeader = headerSecondTitle.createCell(0);
    secondLeftHeader.setCellStyle(cellStyleTopHeader);
    Cell firstRightHeader = headerFirstTitle.createCell(2);
    firstRightHeader.setCellStyle(cellStyleTopRightHeader);
    Cell secondRightHeader = headerSecondTitle.createCell(2);
    secondRightHeader.setCellStyle(cellStyleTopRightHeader);
    firstLeftHeader.setCellValue(firstLeftHeaderTitle);
    secondLeftHeader.setCellValue(secondLeftHeaderTitle);
    firstRightHeader.setCellValue(firstRightHeaderTitle);
    secondRightHeader.setCellValue(secondRightHeaderTitle);
    sheetMain.addMergedRegion(new CellRangeAddress(0, 0, 0, 1));
    sheetMain.addMergedRegion(new CellRangeAddress(1, 1, 0, 1));
    sheetMain.addMergedRegion(new CellRangeAddress(0, 0, 2, 3));
    sheetMain.addMergedRegion(new CellRangeAddress(1, 1, 2, 3));

    //Tao tieu de
    Row rowMainTitle = sheetMain.createRow(3);
    Cell mainCellTitle = rowMainTitle.createCell(0);
    mainCellTitle.setCellValue(I18n.getLanguage("woCd.template.title"));
    mainCellTitle.setCellStyle(cellStyleTitle);
    sheetMain.addMergedRegion(new CellRangeAddress(3, 3, 0, 3));

    XSSFFont fontStar = workBook.createFont();
    fontStar.setColor(IndexedColors.RED.getIndex());

    //Tao header
    Row headerRow = sheetMain.createRow(5);
    headerRow.setHeight((short) 600);
    for (int i = 0; i < listHeader.size(); i++) {
      Cell headerCell = headerRow.createCell(i);
      XSSFRichTextString rt = new XSSFRichTextString(listHeader.get(i));
      for (String checkHeaderStar : listHeaderStar) {
        if (checkHeaderStar.equalsIgnoreCase(listHeader.get(i))) {
          rt.append(" (*)", fontStar);
        }
      }
      headerCell.setCellValue(rt);
      headerCell.setCellStyle(cellStyleHeader);
    }

    ewu.createCell(sheetOrther, 0, 0, I18n.getLanguage("woCd.woGroupName").toUpperCase(),
        styles.get("header"));
    ewu.createCell(sheetOrther, 1, 0, I18n.getLanguage("woCd.woGroupCode").toUpperCase(),
        styles.get("header"));
    ewu.createCell(sheetOrther, 2, 0, I18n.getLanguage("woCd.assign").toUpperCase(),
        styles.get("header"));

    int row = 1;
    List<WoCdGroupInsideDTO> listWoCdGroupInsideDTO = woCdGroupRepository
        .getListWoCdGroupExport(new WoCdGroupInsideDTO());
    for (WoCdGroupInsideDTO dto : listWoCdGroupInsideDTO) {
      ewu.createCell(sheetOrther, 0, row++, dto.getWoGroupName(), styles.get("cell"));
    }
    row = 1;
    for (WoCdGroupInsideDTO dto : listWoCdGroupInsideDTO) {
      ewu.createCell(sheetOrther, 1, row++, dto.getWoGroupCode(), styles.get("cell"));
    }

    row = 1;
    ewu.createCell(sheetOrther, 2, row++, I18n.getLanguage("woCd.assign.0"), styles.get("cell"));
    ewu.createCell(sheetOrther, 2, row++, I18n.getLanguage("woCd.assign.1"), styles.get("cell"));
    Name assignName = workBook.createName();
    assignName.setNameName("assignName");
    assignName.setRefersToFormula("Orther!$C$2:$C$" + row);
    XSSFDataValidationConstraint assignConstraint = new XSSFDataValidationConstraint(
        ValidationType.LIST, "assignName");
    CellRangeAddressList cellRangeAssign = new CellRangeAddressList(6, (maxRecord + 6),
        assignColumn, assignColumn);
    XSSFDataValidation dataValidationAssign = (XSSFDataValidation) dvHelper.createValidation(
        assignConstraint, cellRangeAssign);
    dataValidationAssign.setShowErrorBox(true);
    sheetMain.addValidationData(dataValidationAssign);

    for (int i = 0; i <= 2; i++) {
      sheetOrther.autoSizeColumn(i);
    }

    sheetMain.setColumnWidth(sttColumn, 2000);
    sheetMain.setColumnWidth(woGroupCodeColumn, 10000);
    sheetMain.setColumnWidth(userNameChooseColumn, 7000);
    sheetMain.setColumnWidth(assignColumn, 5000);

    sheetOrther.setSelected(false);
    workBook.setSheetName(0, I18n.getLanguage("woCd.template.fileName"));

    String fileResult = tempFolder + File.separator;
    String fileName =
        I18n.getLanguage("woCd.template.fileName") + "_" + System.currentTimeMillis() + ".xlsx";
    ewu.saveToFileExcel(workBook, fileResult, fileName);
    String resultPath = fileResult + fileName;
    File fileExport = new File(resultPath);
    return fileExport;
  }

  @Override
  public File getTemplateAssignTypeGroup() throws Exception {
    ExcelWriterUtils ewu = new ExcelWriterUtils();
    String templatePathOut = "templates" + File.separator + "TEMPLATE_EXPORT.xlsx";
    Resource resource = new ClassPathResource(templatePathOut);
    InputStream fileTemplate = resource.getInputStream();
    XSSFWorkbook workBook = new XSSFWorkbook(fileTemplate);

    Map<String, CellStyle> styles = CommonExport.createStyles(workBook);

    XSSFSheet sheetMain = workBook.getSheetAt(0);
    XSSFSheet sheetOrther = workBook.createSheet("Orther");

    String[] header = new String[]{
        I18n.getLanguage("common.STT"),
        I18n.getLanguage("woTypeGroup.woGroupCode"),
        I18n.getLanguage("woTypeGroup.woTypeCode"),
        I18n.getLanguage("woTypeGroup.assignName")
    };
    String[] headerStar = new String[]{
        I18n.getLanguage("woTypeGroup.woGroupCode"),
        I18n.getLanguage("woTypeGroup.woTypeCode"),
        I18n.getLanguage("woTypeGroup.assignName")
    };

    XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(sheetMain);
    List<String> listHeader = Arrays.asList(header);
    List<String> listHeaderStar = Arrays.asList(headerStar);

    int sttColumn = listHeader.indexOf(I18n.getLanguage("common.STT"));
    int woGroupCodeColumn = listHeader.indexOf(I18n.getLanguage("woTypeGroup.woGroupCode"));
    int woTypeNameColumn = listHeader.indexOf(I18n.getLanguage("woTypeGroup.woTypeCode"));
    int assignColumn = listHeader.indexOf(I18n.getLanguage("woTypeGroup.assign"));

    String firstLeftHeaderTitle = I18n.getLanguage("common.export.firstLeftHeader");
    String secondLeftHeaderTitle = I18n.getLanguage("common.export.secondLeftHeader");
    String firstRightHeaderTitle = I18n.getLanguage("common.export.firstRightHeader");
    String secondRightHeaderTitle = I18n.getLanguage("common.export.secondRightHeader");

    Font xssFontTopHeader = workBook.createFont();
    xssFontTopHeader.setFontName("Times New Roman");
    xssFontTopHeader.setFontHeightInPoints((short) 10);
    xssFontTopHeader.setColor(IndexedColors.BLACK.index);
    xssFontTopHeader.setBold(true);

    Font xSSFFont = workBook.createFont();
    xSSFFont.setFontName(HSSFFont.FONT_ARIAL);
    xSSFFont.setFontHeightInPoints((short) 20);
    xSSFFont.setBold(true);
    xSSFFont.setColor(IndexedColors.BLACK.index);

    Font xSSFFontHeader = workBook.createFont();
    xSSFFontHeader.setFontName(HSSFFont.FONT_ARIAL);
    xSSFFontHeader.setFontHeightInPoints((short) 10);
    xSSFFontHeader.setColor(IndexedColors.BLUE.index);
    xSSFFontHeader.setBold(true);

    Font rowDataFont = workBook.createFont();
    rowDataFont.setFontName(HSSFFont.FONT_ARIAL);
    rowDataFont.setFontHeightInPoints((short) 10);
    rowDataFont.setColor(IndexedColors.BLACK.index);

    CellStyle cellStyleTopHeader = workBook.createCellStyle();
    cellStyleTopHeader.setAlignment(HorizontalAlignment.CENTER);
    cellStyleTopHeader.setVerticalAlignment(VerticalAlignment.CENTER);
    cellStyleTopHeader.setFont(xssFontTopHeader);

    CellStyle cellStyleTopRightHeader = workBook.createCellStyle();
    cellStyleTopRightHeader.setAlignment(HorizontalAlignment.LEFT);
    cellStyleTopRightHeader.setVerticalAlignment(VerticalAlignment.CENTER);
    cellStyleTopRightHeader.setFont(xssFontTopHeader);

    CellStyle cellStyleTitle = workBook.createCellStyle();
    cellStyleTitle.setAlignment(HorizontalAlignment.CENTER);
    cellStyleTitle.setVerticalAlignment(VerticalAlignment.CENTER);
    cellStyleTitle.setFillForegroundColor(IndexedColors.WHITE.index);
    cellStyleTitle.setFont(xSSFFont);

    CellStyle cellStyleHeader = workBook.createCellStyle();
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

    //Tao quoc hieu
    Row headerFirstTitle = sheetMain.createRow(0);
    Row headerSecondTitle = sheetMain.createRow(1);
    Cell firstLeftHeader = headerFirstTitle.createCell(0);
    firstLeftHeader.setCellStyle(cellStyleTopHeader);
    Cell secondLeftHeader = headerSecondTitle.createCell(0);
    secondLeftHeader.setCellStyle(cellStyleTopHeader);
    Cell firstRightHeader = headerFirstTitle.createCell(2);
    firstRightHeader.setCellStyle(cellStyleTopRightHeader);
    Cell secondRightHeader = headerSecondTitle.createCell(2);
    secondRightHeader.setCellStyle(cellStyleTopRightHeader);
    firstLeftHeader.setCellValue(firstLeftHeaderTitle);
    secondLeftHeader.setCellValue(secondLeftHeaderTitle);
    firstRightHeader.setCellValue(firstRightHeaderTitle);
    secondRightHeader.setCellValue(secondRightHeaderTitle);
    sheetMain.addMergedRegion(new CellRangeAddress(0, 0, 0, 1));
    sheetMain.addMergedRegion(new CellRangeAddress(1, 1, 0, 1));
    sheetMain.addMergedRegion(new CellRangeAddress(0, 0, 2, 3));
    sheetMain.addMergedRegion(new CellRangeAddress(1, 1, 2, 3));

    //Tao tieu de
    Row rowMainTitle = sheetMain.createRow(3);
    Cell mainCellTitle = rowMainTitle.createCell(0);
    mainCellTitle.setCellValue(I18n.getLanguage("woTypeGroup.template.title"));
    mainCellTitle.setCellStyle(cellStyleTitle);
    sheetMain.addMergedRegion(new CellRangeAddress(3, 3, 0, 3));

    XSSFFont fontStar = workBook.createFont();
    fontStar.setColor(IndexedColors.RED.getIndex());

    //Tao header
    Row headerRow = sheetMain.createRow(5);
    headerRow.setHeight((short) 600);
    for (int i = 0; i < listHeader.size(); i++) {
      Cell headerCell = headerRow.createCell(i);
      XSSFRichTextString rt = new XSSFRichTextString(listHeader.get(i));
      for (String checkHeaderStar : listHeaderStar) {
        if (checkHeaderStar.equalsIgnoreCase(listHeader.get(i))) {
          rt.append(" (*)", fontStar);
        }
      }
      headerCell.setCellValue(rt);
      headerCell.setCellStyle(cellStyleHeader);
    }

    ewu.createCell(sheetOrther, 0, 0, I18n.getLanguage("woTypeGroup.woGroupName").toUpperCase(),
        styles.get("header"));
    ewu.createCell(sheetOrther, 1, 0, I18n.getLanguage("woTypeGroup.woGroupCode").toUpperCase(),
        styles.get("header"));
    ewu.createCell(sheetOrther, 2, 0, I18n.getLanguage("woTypeGroup.woTypeName").toUpperCase(),
        styles.get("header"));
    ewu.createCell(sheetOrther, 3, 0, I18n.getLanguage("woTypeGroup.woTypeCode").toUpperCase(),
        styles.get("header"));
    ewu.createCell(sheetOrther, 4, 0, I18n.getLanguage("woTypeGroup.assign").toUpperCase(),
        styles.get("header"));

    int row = 1;
    List<WoCdGroupInsideDTO> listWoCdGroupInsideDTO = woCdGroupRepository
        .getListWoCdGroupExport(new WoCdGroupInsideDTO());
    for (WoCdGroupInsideDTO dto : listWoCdGroupInsideDTO) {
      ewu.createCell(sheetOrther, 0, row++, dto.getWoGroupName(), styles.get("cell"));
    }
    row = 1;
    for (WoCdGroupInsideDTO dto : listWoCdGroupInsideDTO) {
      ewu.createCell(sheetOrther, 1, row++, dto.getWoGroupCode(), styles.get("cell"));
    }

    List<WoTypeInsideDTO> listWoTypeInsideDTO = woTypeRepository.getListWoTypeForWoCdGroup(null);
    row = 1;
    for (WoTypeInsideDTO dto : listWoTypeInsideDTO) {
      ewu.createCell(sheetOrther, 2, row++, dto.getWoTypeName(), styles.get("cell"));
    }
    row = 1;
    for (WoTypeInsideDTO dto : listWoTypeInsideDTO) {
      ewu.createCell(sheetOrther, 3, row++, dto.getWoTypeCode() + "("
          + dto.getWoTypeId() + ")", styles.get("cell"));
    }
    row = 1;
    ewu.createCell(sheetOrther, 4, row++, I18n.getLanguage("woTypeGroup.assign.0"),
        styles.get("cell"));
    ewu.createCell(sheetOrther, 4, row++, I18n.getLanguage("woTypeGroup.assign.1"),
        styles.get("cell"));
    Name assignName = workBook.createName();
    assignName.setNameName("assignName");
    assignName.setRefersToFormula("Orther!$E$2:$E$" + row);
    XSSFDataValidationConstraint assignConstraint = new XSSFDataValidationConstraint(
        ValidationType.LIST, "assignName");
    CellRangeAddressList cellRangeAssign = new CellRangeAddressList(6, (maxRecord + 6),
        assignColumn, assignColumn);
    XSSFDataValidation dataValidationAssign = (XSSFDataValidation) dvHelper.createValidation(
        assignConstraint, cellRangeAssign);
    dataValidationAssign.setShowErrorBox(true);
    sheetMain.addValidationData(dataValidationAssign);

    for (int i = 0; i <= 4; i++) {
      sheetOrther.autoSizeColumn(i);
    }

    sheetMain.setColumnWidth(sttColumn, 2000);
    sheetMain.setColumnWidth(woGroupCodeColumn, 10000);
    sheetMain.setColumnWidth(woTypeNameColumn, 7000);
    sheetMain.setColumnWidth(assignColumn, 5000);

    sheetOrther.setSelected(false);
    workBook.setSheetName(0, I18n.getLanguage("woTypeGroup.template.fileName"));

    String fileResult = tempFolder + File.separator;
    String fileName =
        I18n.getLanguage("woTypeGroup.template.fileName") + "_" + System.currentTimeMillis()
            + ".xlsx";
    ewu.saveToFileExcel(workBook, fileResult, fileName);
    String resultPath = fileResult + fileName;
    File fileExport = new File(resultPath);
    return fileExport;
  }

  @Override
  public ResultInSideDto importDataAssignUser(MultipartFile multipartFile) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);

    List<WoCdDTO> listDto;
    List<WoCdDTO> listImportDto;
    Map<String, String> mapImportDTO;

    String[] header = new String[]{"woGroupCode", "userName", "assignName", "resultImport"};

    try {
      if (multipartFile == null || multipartFile.isEmpty()) {
        resultInSideDto.setKey(RESULT.FILE_IS_NULL);
        return resultInSideDto;
      } else {
        String filePath = FileUtils
            .saveTempFile(multipartFile.getOriginalFilename(), multipartFile.getBytes(),
                tempFolder);
        if (!RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
          return resultInSideDto;
        }

        File fileImp = new File(filePath);

        List<Object[]> lstHeader;
        lstHeader = CommonImport.getDataFromExcelFile(fileImp, 0, 5,
            0, 3, 1000);
        if (lstHeader.size() == 0 || !validFileAssignUser(lstHeader)) {
          resultInSideDto.setKey(RESULT.FILE_INVALID_FORMAT);
          return resultInSideDto;
        }

        List<Object[]> lstData = CommonImport.getDataFromExcelFile(fileImp, 0, 6,
            0, 3, 1000);
        if (lstData.size() > maxRecord) {
          resultInSideDto.setKey(RESULT.DATA_OVER);
          resultInSideDto.setMessage(String.valueOf(maxRecord));
          return resultInSideDto;
        }

        listDto = new ArrayList<>();
        listImportDto = new ArrayList<>();
        mapImportDTO = new HashMap<>();

        if (!lstData.isEmpty()) {
          int index = 0;
          int value = 1;
          for (Object[] obj : lstData) {
            WoCdDTO woCdDTO = new WoCdDTO();
            WoCdDTO importDTO = new WoCdDTO();
            if (obj[1] != null) {
              importDTO.setWoGroupCode(obj[1].toString().trim());
            } else {
              importDTO.setWoGroupCode(null);
            }
            if (obj[2] != null) {
              importDTO.setUserName(obj[2].toString().trim());
            } else {
              importDTO.setUserName(null);
            }
            if (obj[3] != null) {
              importDTO.setAssignName(obj[3].toString().trim());
              if (I18n.getLanguage("woCd.assign.0").equals(importDTO.getAssignName())) {
                woCdDTO.setAssign(0L);
                importDTO.setAssign(0L);
              }
              if (I18n.getLanguage("woCd.assign.1").equals(importDTO.getAssignName())) {
                woCdDTO.setAssign(1L);
                importDTO.setAssign(1L);
              }
            } else {
              importDTO.setAssignName(null);
            }
            WoCdDTO tempImportDTO = validateAssignUserInfo(importDTO, mapImportDTO);
            if (tempImportDTO.getCdId() != null) {
              woCdDTO.setCdId(tempImportDTO.getCdId());
            }
            if (tempImportDTO.getResultImport() == null) {
              tempImportDTO.setResultImport(I18n.getLanguage("woCdGroup.result.import.ok"));
              listImportDto.add(tempImportDTO);
              String woGroupCode = tempImportDTO.getWoGroupCode();
              String userName = tempImportDTO.getUserName();
              Long assign = tempImportDTO.getAssign();
              mapImportDTO.put(woGroupCode + "_" + userName + "_" + assign, String.valueOf(value));
              value++;
              woCdDTO.setWoGroupId(tempImportDTO.getWoGroupId());
              woCdDTO.setUserId(tempImportDTO.getUserId());
              listDto.add(woCdDTO);
            } else {
              listImportDto.add(tempImportDTO);
              index++;
            }
          }
          if (index == 0) {
            if (!listDto.isEmpty()) {
              for (WoCdDTO dto : listDto) {
                resultInSideDto = insertOrDeleteWoCd(dto);
              }
            }
          } else {
            File fileExport = handleFileExport(listImportDto, header, null,
                "RESULT_IMPORT_ASSIGN_USER");
            resultInSideDto.setKey(RESULT.ERROR);
            resultInSideDto.setFile(fileExport);
          }
        } else {
          resultInSideDto.setKey(RESULT.NODATA);
          resultInSideDto.setMessage(Constants.FILE_NULL);
          File fileExport = handleFileExport(listImportDto, header, null,
              "RESULT_IMPORT_ASSIGN_USER");
          resultInSideDto.setFile(fileExport);
          return resultInSideDto;
        }
      }
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      resultInSideDto.setKey(RESULT.ERROR);
      resultInSideDto.setMessage(ex.getMessage());
    }
    return resultInSideDto;
  }

  private ResultInSideDto insertOrDeleteWoCd(WoCdDTO dto) {
    ResultInSideDto resultInSideDto;
    if (dto.getCdId() != null) {
      resultInSideDto = woCdRepository.deleteWoCd(dto.getCdId());
    } else {
      resultInSideDto = woCdRepository.insertWoCd(dto);
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto importDataAssignTypeGroup(MultipartFile multipartFile) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);

    List<WoTypeGroupDTO> listDto;
    List<WoTypeGroupDTO> listImportDto;
    Map<String, String> mapImportDTO;

    String[] header = new String[]{"woGroupCode", "woTypeCode", "assignName", "resultImport"};

    try {
      if (multipartFile == null || multipartFile.isEmpty()) {
        resultInSideDto.setKey(RESULT.FILE_IS_NULL);
        return resultInSideDto;
      } else {
        String filePath = FileUtils
            .saveTempFile(multipartFile.getOriginalFilename(), multipartFile.getBytes(),
                tempFolder);
        if (!RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
          return resultInSideDto;
        }

        File fileImp = new File(filePath);

        List<Object[]> lstHeader;
        lstHeader = CommonImport.getDataFromExcelFile(fileImp, 0, 5,
            0, 3, 1000);
        if (lstHeader.size() == 0 || !validFileAssignTypeGroup(lstHeader)) {
          resultInSideDto.setKey(RESULT.FILE_INVALID_FORMAT);
          return resultInSideDto;
        }

        List<Object[]> lstData = CommonImport.getDataFromExcelFile(fileImp, 0, 6,
            0, 3, 1000);
        if (lstData.size() > maxRecord) {
          resultInSideDto.setKey(RESULT.DATA_OVER);
          resultInSideDto.setMessage(String.valueOf(maxRecord));
          return resultInSideDto;
        }

        listDto = new ArrayList<>();
        listImportDto = new ArrayList<>();
        mapImportDTO = new HashMap<>();

        if (!lstData.isEmpty()) {
          int index = 0;
          int value = 1;
          for (Object[] obj : lstData) {
            WoTypeGroupDTO woTypeGroupDTO = new WoTypeGroupDTO();
            WoTypeGroupDTO importDTO = new WoTypeGroupDTO();
            if (obj[1] != null) {
              importDTO.setWoGroupCode(obj[1].toString().trim());
//              woTypeGroupDTO.setWoGroupCode(importDTO.getWoGroupCode());
            } else {
              importDTO.setWoGroupCode(null);
            }
            if (obj[2] != null) {
              importDTO.setWoTypeCode(obj[2].toString().trim());
//              woTypeGroupDTO.setWoTypeCode(importDTO.getWoTypeCode());
            } else {
              importDTO.setWoTypeCode(null);
            }
            if (obj[3] != null) {
              importDTO.setAssignName(obj[3].toString().trim());
              if (I18n.getLanguage("woTypeGroup.assign.0").equals(importDTO.getAssignName())) {
                woTypeGroupDTO.setAssign(0L);
                importDTO.setAssign(0L);
              }
              if (I18n.getLanguage("woTypeGroup.assign.1").equals(importDTO.getAssignName())) {
                woTypeGroupDTO.setAssign(1L);
                importDTO.setAssign(1L);
              }
            } else {
              importDTO.setAssignName(null);
            }
            WoTypeGroupDTO tempImportDTO = validateAssignTypeGroupInfo(importDTO, mapImportDTO);
            if (tempImportDTO.getWoTypeGroupId() != null) {
              woTypeGroupDTO.setWoTypeGroupId(tempImportDTO.getWoTypeGroupId());
            }
            if (tempImportDTO.getResultImport() == null) {
              tempImportDTO.setResultImport(I18n.getLanguage("woTypeGroup.result.import.ok"));
              listImportDto.add(tempImportDTO);
              String woGroupCode = tempImportDTO.getWoGroupCode();
              String woTypeCode = tempImportDTO.getWoTypeCode();
              Long assign = tempImportDTO.getAssign();
              mapImportDTO
                  .put(woGroupCode + "_" + woTypeCode + "_" + assign, String.valueOf(value));
              value++;
              woTypeGroupDTO.setWoGroupId(tempImportDTO.getWoGroupId());
              woTypeGroupDTO.setWoTypeId(tempImportDTO.getWoTypeId());
              listDto.add(woTypeGroupDTO);
            } else {
              listImportDto.add(tempImportDTO);
              index++;
            }
          }
          if (index == 0) {
            if (!listDto.isEmpty()) {
              for (WoTypeGroupDTO dto : listDto) {
                resultInSideDto = insertOrDeleteWoTypeGroup(dto);
              }
            }
          } else {
            File fileExport = handleFileExport(listImportDto, header, null,
                "RESULT_IMPORT_ASSIGN_TYPE_GROUP");
            resultInSideDto.setKey(RESULT.ERROR);
            resultInSideDto.setFile(fileExport);
          }
        } else {
          resultInSideDto.setKey(RESULT.NODATA);
          resultInSideDto.setMessage(Constants.FILE_NULL);
          File fileExport = handleFileExport(listImportDto, header, null,
              "RESULT_IMPORT_ASSIGN_TYPE_GROUP");
          resultInSideDto.setFile(fileExport);
          return resultInSideDto;
        }
      }
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      resultInSideDto.setKey(RESULT.ERROR);
      resultInSideDto.setMessage(ex.getMessage());
    }
    return resultInSideDto;
  }

  private boolean validFileAssignTypeGroup(List<Object[]> lstHeader) {
    Object[] obj = lstHeader.get(0);
    int count = 0;
    for (Object data : obj) {
      if (data != null) {
        count += 1;
      }
    }
    if (count != 4) {
      return false;
    }
    if (obj == null) {
      return false;
    }
    if (obj[0] == null) {
      return false;
    }
    if (!(I18n.getLanguage("common.STT"))
        .equalsIgnoreCase(obj[0].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("woTypeGroup.woGroupCode") + " (*)")
        .equalsIgnoreCase(obj[1].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("woTypeGroup.woTypeCode") + " (*)")
        .equalsIgnoreCase(obj[2].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("woTypeGroup.assignName") + " (*)")
        .equalsIgnoreCase(obj[3].toString().trim())) {
      return false;
    }
    return true;
  }

  private ResultInSideDto insertOrDeleteWoTypeGroup(WoTypeGroupDTO dto) {
    ResultInSideDto resultInSideDto;
    if (dto.getWoTypeGroupId() != null) {
      resultInSideDto = woTypeGroupRepository.deleteWoTypeGroup(dto.getWoTypeGroupId());
    } else {
      resultInSideDto = woTypeGroupRepository.insertWoTypeGroup(dto);
    }
    return resultInSideDto;
  }

  private WoTypeGroupDTO validateAssignTypeGroupInfo(WoTypeGroupDTO importDTO,
      Map<String, String> mapImportDTO) {
    setMapWoType();
    String resultImport = "";
    if (StringUtils.isStringNullOrEmpty(importDTO.getWoGroupCode())) {
      resultImport = resultImport.concat(I18n.getLanguage("woTypeGroup.err.woGroupCode"));
    }
    if (StringUtils.isStringNullOrEmpty(importDTO.getWoTypeCode())) {
      resultImport = checkResultImport(resultImport)
          .concat(I18n.getLanguage("woTypeGroup.err.woTypeCode"));
    }
    if (StringUtils.isStringNullOrEmpty(importDTO.getAssignName())) {
      resultImport = checkResultImport(resultImport)
          .concat(I18n.getLanguage("woTypeGroup.err.assignName"));
    }
    if (StringUtils.isNotNullOrEmpty(resultImport)) {
      importDTO.setResultImport(resultImport);
      return importDTO;
    }
    if (importDTO.getAssign() == null) {
      importDTO.setResultImport(I18n.getLanguage("woTypeGroup.err.assign.notExist"));
      return importDTO;
    }
    if (StringUtils.isNotNullOrEmpty(importDTO.getWoGroupCode())) {
      WoCdGroupInsideDTO woCdGroupInsideDTOTmp = new WoCdGroupInsideDTO();
      woCdGroupInsideDTOTmp.setWoGroupCode(importDTO.getWoGroupCode());
      woCdGroupInsideDTOTmp = woCdGroupRepository.checkWoCdGroupExit(woCdGroupInsideDTOTmp);
      if (woCdGroupInsideDTOTmp != null) {
        Long woGroupId = woCdGroupInsideDTOTmp.getWoGroupId();
        if (StringUtils.isNotNullOrEmpty(importDTO.getWoTypeCode())) {
          importDTO.setWoGroupId(woGroupId);
          String woTypeId = mapWoType.get(importDTO.getWoTypeCode());
          if (StringUtils.isNotNullOrEmpty(woTypeId)) {
            WoTypeGroupDTO woTypeGroupDTOTmp = woTypeGroupRepository
                .checkTypeGroupExitBy2Id(woGroupId, Long.valueOf(woTypeId));
            if (woTypeGroupDTOTmp != null) {
              if (I18n.getLanguage("woTypeGroup.assign.0").equals(importDTO.getAssignName())) {
                importDTO.setResultImport(I18n.getLanguage("woTypeGroup.err.dup-code"));
                return importDTO;
              }
              if (I18n.getLanguage("woTypeGroup.assign.1").equals(importDTO.getAssignName())) {
                importDTO.setWoTypeGroupId(woTypeGroupDTOTmp.getWoTypeGroupId());
                importDTO.setWoTypeId(Long.valueOf(woTypeId));
              }
            } else {
              if (I18n.getLanguage("woTypeGroup.assign.0").equals(importDTO.getAssignName())) {
                importDTO.setWoTypeId(Long.valueOf(woTypeId));
              }
              if (I18n.getLanguage("woTypeGroup.assign.1").equals(importDTO.getAssignName())) {
                importDTO.setResultImport(I18n.getLanguage("woTypeGroup.err.notExist"));
                return importDTO;
              }
            }
          } else {
            importDTO.setResultImport(I18n.getLanguage("woTypeGroup.err.woTypeCode.notExist"));
            return importDTO;
          }
        }
      } else {
        importDTO.setResultImport(I18n.getLanguage("woTypeGroup.err.woGroupCode.notExist"));
        return importDTO;
      }
    }
    String validateDuplicate = validateDuplicateTypeGroup(importDTO, mapImportDTO);
    if (StringUtils.isNotNullOrEmpty(validateDuplicate)) {
      importDTO.setResultImport(validateDuplicate);
      return importDTO;
    }
    return importDTO;
  }

  private String validateDuplicateTypeGroup(WoTypeGroupDTO importDTO,
      Map<String, String> mapImportDTO) {
    String woGroupCode = importDTO.getWoGroupCode();
    String woTypeCode = importDTO.getWoTypeCode();
    Long assign = importDTO.getAssign();
    String key = woGroupCode + "_" + woTypeCode + "_" + String.valueOf(assign);
    String value = mapImportDTO.get(key);
    if (StringUtils.isNotNullOrEmpty(value)) {
      return I18n.getLanguage("woTypeGroup.err.dup-code-in-file").replaceAll("0", value);
    }
    return null;
  }

  private WoCdDTO validateAssignUserInfo(WoCdDTO importDTO, Map<String, String> mapImportDTO) {
    String resultImport = "";
    if (StringUtils.isStringNullOrEmpty(importDTO.getWoGroupCode())) {
      resultImport = resultImport.concat(I18n.getLanguage("woCd.err.woGroupCode"));
    }
    if (StringUtils.isStringNullOrEmpty(importDTO.getUserName())) {
      resultImport = checkResultImport(resultImport)
          .concat(I18n.getLanguage("woCd.err.userName"));
    }
    if (StringUtils.isStringNullOrEmpty(importDTO.getAssignName())) {
      resultImport = checkResultImport(resultImport)
          .concat(I18n.getLanguage("woCd.err.assignName"));
    }
    if (StringUtils.isNotNullOrEmpty(resultImport)) {
      importDTO.setResultImport(resultImport);
      return importDTO;
    }
    if (importDTO.getAssign() == null) {
      importDTO.setResultImport(I18n.getLanguage("woCd.err.assign.notExist"));
      return importDTO;
    }
    if (StringUtils.isNotNullOrEmpty(importDTO.getWoGroupCode())) {
      WoCdGroupInsideDTO woCdGroupInsideDTOTmp = new WoCdGroupInsideDTO();
      woCdGroupInsideDTOTmp.setWoGroupCode(importDTO.getWoGroupCode());
      woCdGroupInsideDTOTmp = woCdGroupRepository.checkWoCdGroupExit(woCdGroupInsideDTOTmp);
      if (woCdGroupInsideDTOTmp != null) {
        Long woGroupId = woCdGroupInsideDTOTmp.getWoGroupId();
        if (StringUtils.isNotNullOrEmpty(importDTO.getUserName())) {
          importDTO.setWoGroupId(woGroupId);
          UsersInsideDto usersInsideDtoTmp = userRepository
              .getUserDTOByUserName(importDTO.getUserName());
          if (usersInsideDtoTmp != null) {
            Long userId = usersInsideDtoTmp.getUserId();
            WoCdDTO woCdDTOTmp = woCdRepository.checkWoCdExitBy2Id(woGroupId, userId);
            if (woCdDTOTmp != null) {
              if (I18n.getLanguage("woCd.assign.0").equals(importDTO.getAssignName())) {
                importDTO.setResultImport(I18n.getLanguage("woCd.err.dup-code"));
                return importDTO;
              }
              if (I18n.getLanguage("woCd.assign.1").equals(importDTO.getAssignName())) {
                importDTO.setCdId(woCdDTOTmp.getCdId());
                importDTO.setUserId(userId);
              }
            } else {
              if (I18n.getLanguage("woCd.assign.0").equals(importDTO.getAssignName())) {
                importDTO.setUserId(userId);
              }
              if (I18n.getLanguage("woCd.assign.1").equals(importDTO.getAssignName())) {
                importDTO.setResultImport(I18n.getLanguage("woCd.err.notExist"));
                return importDTO;
              }
            }
          } else {
            importDTO.setResultImport(I18n.getLanguage("woCd.err.userName.notExist"));
            return importDTO;
          }
        }
      } else {
        importDTO.setResultImport(I18n.getLanguage("woCd.err.woGroupCode.notExist"));
        return importDTO;
      }
    }
    String validateDuplicate = validateDuplicateAssignUser(importDTO, mapImportDTO);
    if (StringUtils.isNotNullOrEmpty(validateDuplicate)) {
      importDTO.setResultImport(validateDuplicate);
      return importDTO;
    }
    return importDTO;
  }

  private String validateDuplicateAssignUser(WoCdDTO importDTO, Map<String, String> mapImportDTO) {
    String woGroupCode = importDTO.getWoGroupCode();
    String userName = importDTO.getUserName();
    Long assign = importDTO.getAssign();
    String key = woGroupCode + "_" + userName + "_" + assign;
    String value = mapImportDTO.get(key);
    if (StringUtils.isNotNullOrEmpty(value)) {
      return I18n.getLanguage("woCd.err.dup-code-in-file").replaceAll("0", value);
    }
    return null;
  }

  private boolean validFileAssignUser(List<Object[]> lstHeader) {
    Object[] obj = lstHeader.get(0);
    int count = 0;
    for (Object data : obj) {
      if (data != null) {
        count += 1;
      }
    }
    if (count != 4) {
      return false;
    }
    if (obj == null) {
      return false;
    }
    if (obj[0] == null) {
      return false;
    }
    if (!(I18n.getLanguage("common.STT"))
        .equalsIgnoreCase(obj[0].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("woCd.woGroupCode") + " (*)")
        .equalsIgnoreCase(obj[1].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("woCd.userName.choose") + " (*)")
        .equalsIgnoreCase(obj[2].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("woCd.assignName") + " (*)")
        .equalsIgnoreCase(obj[3].toString().trim())) {
      return false;
    }
    return true;
  }

  @Override
  public ResultInSideDto importDataWoCdGroup(MultipartFile multipartFile) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);

    List<WoCdGroupInsideDTO> listDto;
    List<WoCdGroupInsideDTO> listImportDto;
    Map<String, String> mapImportDTO;

    String[] header = new String[]{"groupTypeName", "woGroupCode", "woGroupName", "nationName",
        "email", "mobile", "isEnableName", "actionName", "resultImport"};

    try {
      if (multipartFile == null || multipartFile.isEmpty()) {
        resultInSideDto.setKey(RESULT.FILE_IS_NULL);
        return resultInSideDto;
      } else {
        String filePath = FileUtils
            .saveTempFile(multipartFile.getOriginalFilename(), multipartFile.getBytes(),
                tempFolder);
        if (!RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
          return resultInSideDto;
        }

        File fileImp = new File(filePath);

        List<Object[]> lstHeader;
        lstHeader = CommonImport.getDataFromExcelFile(fileImp, 0, 5,
            0, 8, 1000);
        if (lstHeader.size() == 0 || !validFileFormat(lstHeader)) {
          resultInSideDto.setKey(RESULT.FILE_INVALID_FORMAT);
          return resultInSideDto;
        }

        List<Object[]> lstData = CommonImport.getDataFromExcelFile(fileImp, 0, 6,
            0, 8, 1000);
        if (lstData.size() > maxRecord) {
          resultInSideDto.setKey(RESULT.DATA_OVER);
          resultInSideDto.setMessage(String.valueOf(maxRecord));
          return resultInSideDto;
        }

        listDto = new ArrayList<>();
        listImportDto = new ArrayList<>();
        mapImportDTO = new HashMap<>();

        if (!lstData.isEmpty()) {
          int index = 0;
          int value = 1;
          setMapGroupType();
          setMapNation();
          for (Object[] obj : lstData) {
            WoCdGroupInsideDTO woCdGroupInsideDTO = new WoCdGroupInsideDTO();
            WoCdGroupInsideDTO importDTO = new WoCdGroupInsideDTO();
            if (obj[1] != null) {
              importDTO.setGroupTypeName(obj[1].toString().trim());
              for (Map.Entry<Long, String> item : mapGroupType.entrySet()) {
                if (importDTO.getGroupTypeName().equals(item.getValue())) {
                  woCdGroupInsideDTO.setGroupTypeId(item.getKey());
                }
              }
            } else {
              importDTO.setGroupTypeName(null);
            }
            if (obj[2] != null) {
              importDTO.setWoGroupCode(obj[2].toString().trim());
              woCdGroupInsideDTO.setWoGroupCode(importDTO.getWoGroupCode().toUpperCase());
            } else {
              importDTO.setWoGroupCode(null);
            }
            if (obj[3] != null) {
              importDTO.setWoGroupName(obj[3].toString().trim());
              woCdGroupInsideDTO.setWoGroupName(importDTO.getWoGroupName());
            } else {
              importDTO.setWoGroupName(null);
            }
            if (obj[4] != null) {
              importDTO.setNationName(obj[4].toString().trim());
              for (Map.Entry<Long, String> item : mapNation.entrySet()) {
                if (importDTO.getNationName().equals(item.getValue())) {
                  woCdGroupInsideDTO.setNationId(item.getKey());
                }
              }
            } else {
              importDTO.setNationName(null);
            }
            if (obj[5] != null) {
              importDTO.setEmail(obj[5].toString().trim());
              woCdGroupInsideDTO.setEmail(importDTO.getEmail());
            } else {
              importDTO.setEmail(null);
            }
            if (obj[6] != null) {
              importDTO.setMobile(obj[6].toString().trim());
              woCdGroupInsideDTO.setMobile(importDTO.getMobile());
            } else {
              importDTO.setMobile(null);
            }
            if (obj[7] != null) {
              importDTO.setIsEnableName(obj[7].toString().trim());
              if (I18n.getLanguage("woCdGroup.isEnable.0").equals(importDTO.getIsEnableName())) {
                woCdGroupInsideDTO.setIsEnable(0L);
              }
              if (I18n.getLanguage("woCdGroup.isEnable.1").equals(importDTO.getIsEnableName())) {
                woCdGroupInsideDTO.setIsEnable(1L);
              }
            } else {
              importDTO.setIsEnableName(null);
            }
            if (obj[8] != null) {
              importDTO.setActionName(obj[8].toString().trim());
              if (I18n.getLanguage("woCdGroup.action.0").equals(importDTO.getActionName())) {
                woCdGroupInsideDTO.setAction(0L);
                woCdGroupInsideDTO.setActionName("0");
              }
              if (I18n.getLanguage("woCdGroup.action.1").equals(importDTO.getActionName())) {
                woCdGroupInsideDTO.setAction(1L);
                woCdGroupInsideDTO.setActionName("1");
              }
            } else {
              importDTO.setActionName(null);
            }
            WoCdGroupInsideDTO tempImportDTO = validateImportInfo(importDTO, woCdGroupInsideDTO,
                mapImportDTO);
            if (tempImportDTO.getWoGroupId() != null) {
              woCdGroupInsideDTO.setWoGroupId(tempImportDTO.getWoGroupId());
            }
            if (tempImportDTO.getResultImport() == null) {
              tempImportDTO.setResultImport(I18n.getLanguage("woCdGroup.result.import.ok"));
              listImportDto.add(tempImportDTO);
              String woGroupCode = tempImportDTO.getWoGroupCode();
              mapImportDTO.put(woGroupCode, String.valueOf(value));
              value++;
              listDto.add(woCdGroupInsideDTO);
            } else {
              listImportDto.add(tempImportDTO);
              index++;
            }
          }
          if (index == 0) {
            if (!listDto.isEmpty()) {
              for (WoCdGroupInsideDTO dto : listDto) {
                resultInSideDto = insertOrUpdateWoCdGroup(dto);
              }
            }
          } else {
            File fileExport = handleFileExport(listImportDto, header, null, "RESULT_IMPORT");
            resultInSideDto.setKey(RESULT.ERROR);
            resultInSideDto.setFile(fileExport);
          }
        } else {
          resultInSideDto.setKey(RESULT.NODATA);
          resultInSideDto.setMessage(Constants.FILE_NULL);
          File fileExport = handleFileExport(listImportDto, header, null, "RESULT_IMPORT");
          resultInSideDto.setFile(fileExport);
          return resultInSideDto;
        }
      }
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      resultInSideDto.setKey(RESULT.ERROR);
      resultInSideDto.setMessage(ex.getMessage());
    }
    return resultInSideDto;
  }

  @Override
  public File exportDataWoCd(WoCdDTO woCdDTO) throws Exception {
    List<WoCdDTO> listWoCd = woCdRepository.getListWoCdExport(woCdDTO);
    String[] header = new String[]{"woGroupCode", "woGroupName", "userName", "fullName",
        "email", "mobile"};
    Date date = DateTimeUtils.convertDateOffset();
    return handleFileExport(listWoCd, header, DateUtil.date2ddMMyyyyHHMMss(date),
        "EXPORT_WO_CD");
  }

  private ResultInSideDto insertOrUpdateWoCdGroup(WoCdGroupInsideDTO woCdGroupInsideDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    if ("0".equals(woCdGroupInsideDTO.getActionName())) {
      if (woCdGroupInsideDTO.getWoGroupId() == null) {
        resultInSideDto = woCdGroupRepository.insertWoCdGroup(woCdGroupInsideDTO);
      } else {
        resultInSideDto.setKey(RESULT.ERROR);
      }
    }
    if ("1".equals(woCdGroupInsideDTO.getActionName())) {
      if (woCdGroupInsideDTO.getWoGroupId() != null) {
        resultInSideDto = woCdGroupRepository.updateWoCdGroup(woCdGroupInsideDTO);
      } else {
        resultInSideDto.setKey(RESULT.ERROR);
      }
    }
    return resultInSideDto;
  }

  private WoCdGroupInsideDTO validateImportInfo(
      WoCdGroupInsideDTO importDTO, WoCdGroupInsideDTO woCdGroupInsideDTO,
      Map<String, String> mapImportDTO) {
    String resultImport = "";
    if (StringUtils.isStringNullOrEmpty(importDTO.getGroupTypeName())) {
      resultImport = resultImport.concat(I18n.getLanguage("woCdGroup.err.groupTypeName"));
    }
    if (StringUtils.isStringNullOrEmpty(importDTO.getWoGroupCode())) {
      resultImport = checkResultImport(resultImport)
          .concat(I18n.getLanguage("woCdGroup.err.woGroupCode"));
    }
    if (StringUtils.isStringNullOrEmpty(importDTO.getWoGroupName())) {
      resultImport = checkResultImport(resultImport)
          .concat(I18n.getLanguage("woCdGroup.err.woGroupName"));
    }
    if (StringUtils.isStringNullOrEmpty(importDTO.getNationName())) {
      resultImport = checkResultImport(resultImport)
          .concat(I18n.getLanguage("woCdGroup.err.nationName"));
    }
    if (StringUtils.isStringNullOrEmpty(importDTO.getActionName())) {
      resultImport = checkResultImport(resultImport)
          .concat(I18n.getLanguage("woCdGroup.err.actionName"));
    }
    if (StringUtils.isStringNullOrEmpty(importDTO.getIsEnableName())) {
      resultImport = checkResultImport(resultImport)
          .concat(I18n.getLanguage("woCdGroup.err.isEnableName"));
    }
    if (StringUtils.isNotNullOrEmpty(resultImport)) {
      importDTO.setResultImport(resultImport);
      return importDTO;
    }
//    if (StringUtils.isNotNullOrEmpty(importDTO.getWoGroupCode())) {
//      if (importDTO.getWoGroupCode().length() > 50) {
//        resultImport = checkResultImport(resultImport)
//            .concat(I18n.getLanguage("woCdGroup.err.woGroupCode.length"));
//      }
//    }
    if (StringUtils.isNotNullOrEmpty(importDTO.getWoGroupName())) {
      if (importDTO.getWoGroupName().length() > 100) {
        resultImport = checkResultImport(resultImport)
            .concat(I18n.getLanguage("woCdGroup.err.woGroupName.length"));
      }
    }
    if (StringUtils.isNotNullOrEmpty(resultImport)) {
      importDTO.setResultImport(resultImport);
      return importDTO;
    }
    if (StringUtils.isNotNullOrEmpty(woCdGroupInsideDTO.getWoGroupCode())) {
      String woGroupCode = woCdGroupInsideDTO.getWoGroupCode();
      if (!isValidWoGroupCode(woGroupCode)) {
        importDTO.setResultImport(I18n.getLanguage("woCdGroup.err.woGroupCode.notValid"));
        return importDTO;
      } else {
        char cf = woGroupCode.charAt(0);
        char ce = woGroupCode.charAt(woGroupCode.length() - 1);
        if (cf == '_') {
          importDTO.setResultImport(I18n.getLanguage("woCdGroup.err.woGroupCode.underscoreFirst"));
          return importDTO;
        }
        if (ce == '_') {
          importDTO.setResultImport(I18n.getLanguage("woCdGroup.err.woGroupCode.underscoreEnd"));
          return importDTO;
        }
        if (woGroupCode.length() > 50) {
          importDTO.setResultImport(I18n.getLanguage("woCdGroup.err.woGroupCode.length"));
          return importDTO;
        }
      }
    }
    if (woCdGroupInsideDTO.getGroupTypeId() == null) {
      importDTO.setResultImport(I18n.getLanguage("woCdGroup.err.groupTypeId.notExist"));
      return importDTO;
    }
    if (woCdGroupInsideDTO.getNationId() == null) {
      importDTO.setResultImport(I18n.getLanguage("woCdGroup.err.nationId.notExist"));
      return importDTO;
    }
    if (woCdGroupInsideDTO.getIsEnable() == null) {
      importDTO.setResultImport(I18n.getLanguage("woCdGroup.err.isEnable.notExist"));
      return importDTO;
    }
    if (woCdGroupInsideDTO.getAction() == null) {
      importDTO.setResultImport(I18n.getLanguage("woCdGroup.err.action.notExist"));
      return importDTO;
    }
    if (StringUtils.isNotNullOrEmpty(woCdGroupInsideDTO.getEmail())) {
      if (!isValidEmail(woCdGroupInsideDTO.getEmail())) {
        importDTO.setResultImport(I18n.getLanguage("woCdGroup.err.email.notValid"));
        return importDTO;
      }
    }
    if (StringUtils.isNotNullOrEmpty(woCdGroupInsideDTO.getMobile())) {
      int count = 0;
      for (int i = 0; i < woCdGroupInsideDTO.getMobile().length(); i++) {
        if (Character.isDigit(woCdGroupInsideDTO.getMobile().charAt(i))) {
          count++;
        } else {
          importDTO.setResultImport(I18n.getLanguage("woCdGroup.err.mobile.notNumber"));
          return importDTO;
        }
      }
      if (count > 15) {
        importDTO.setResultImport(I18n.getLanguage("woCdGroup.err.mobile.maxLength"));
        return importDTO;
      }
    }
    if (StringUtils.isNotNullOrEmpty(woCdGroupInsideDTO.getWoGroupCode())) {
      WoCdGroupInsideDTO dtoTmp = woCdGroupRepository.checkWoCdGroupExit(woCdGroupInsideDTO);
      if (dtoTmp != null) {
        if (I18n.getLanguage("woCdGroup.action.0").equals(importDTO.getActionName())) {
          importDTO.setResultImport(I18n.getLanguage("woCdGroup.err.dup-code"));
          return importDTO;
        }
        if (I18n.getLanguage("woCdGroup.action.1").equals(importDTO.getActionName())) {
          importDTO.setWoGroupId(dtoTmp.getWoGroupId());
        }
      } else {
        if (I18n.getLanguage("woCdGroup.action.1").equals(importDTO.getActionName())) {
          importDTO.setResultImport(I18n.getLanguage("woCdGroup.err.notExist"));
          return importDTO;
        }
      }
    }
    String validateDuplicate = validateDuplicateImport(importDTO, mapImportDTO);
    if (StringUtils.isNotNullOrEmpty(validateDuplicate)) {
      importDTO.setResultImport(validateDuplicate);
      return importDTO;
    }
    return importDTO;
  }

  private boolean isValidEmail(String email) {
    String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
    return email.matches(regex);
  }

  private boolean isValidWoGroupCode(String woGroupCode) {
    String regex = "([A-Za-z0-9_]+)";
    return woGroupCode.matches(regex);
  }

  private String validateDuplicateImport(WoCdGroupInsideDTO importDTO,
      Map<String, String> mapImportDTO) {
    String key = importDTO.getWoGroupCode();
    String value = mapImportDTO.get(key);
    if (StringUtils.isNotNullOrEmpty(value)) {
      return I18n.getLanguage("woCdGroup.err.dup-code-in-file").replaceAll("0", value);
    }
    return null;
  }

  private String checkResultImport(String resultImport) {
    if (StringUtils.isNotNullOrEmpty(resultImport)) {
      resultImport = resultImport.concat("; \n");
    }
    return resultImport;
  }

  public void setMapNation() {
    Datatable dataCountry = catItemRepository
        .getItemMaster(WO_CD_GROUP_MASTER_CODE.GNOC_COUNTRY,
            LANGUAGUE_EXCHANGE_SYSTEM.COMMON, APPLIED_BUSSINESS.CAT_ITEM.toString(),
            Constants.ITEM_ID,
            Constants.ITEM_NAME);
    List<CatItemDTO> list = (List<CatItemDTO>) dataCountry.getData();
    if (list != null && !list.isEmpty()) {
      for (CatItemDTO dto : list) {
        mapNation.put(dto.getItemId(), dto.getItemName());
      }
    }
  }

  public void setMapGroupType() {
    Datatable dataGroupType = catItemRepository
        .getItemMaster(WO_CD_GROUP_MASTER_CODE.WO_CD_GROUP_TYPE,
            LANGUAGUE_EXCHANGE_SYSTEM.COMMON, APPLIED_BUSSINESS.CAT_ITEM.toString(),
            Constants.ITEM_ID,
            Constants.ITEM_NAME);
    List<CatItemDTO> list = (List<CatItemDTO>) dataGroupType.getData();
    if (list != null && !list.isEmpty()) {
      for (CatItemDTO dto : list) {
        mapGroupType.put(Long.valueOf(dto.getItemValue()), dto.getItemName());
      }
    }
  }

  public void setMapWoType() {
    List<WoTypeInsideDTO> list = woTypeRepository.getListWoTypeForWoCdGroup(null);
    if (list != null && !list.isEmpty()) {
      for (WoTypeInsideDTO dto : list) {
        mapWoType.put(dto.getWoTypeCode() + "(" + dto.getWoTypeId() + ")",
            String.valueOf(dto.getWoTypeId()));
      }
    }
  }

  private boolean validFileFormat(List<Object[]> lstHeader) {
    Object[] obj = lstHeader.get(0);
    int count = 0;
    for (Object data : obj) {
      if (data != null) {
        count += 1;
      }
    }
    if (count != 9) {
      return false;
    }
    if (obj == null) {
      return false;
    }
    if (obj[0] == null) {
      return false;
    }
    if (!(I18n.getLanguage("common.STT"))
        .equalsIgnoreCase(obj[0].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("woCdGroup.groupTypeName") + " (*)")
        .equalsIgnoreCase(obj[1].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("woCdGroup.woGroupCode") + " (*)")
        .equalsIgnoreCase(obj[2].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("woCdGroup.woGroupName") + " (*)")
        .equalsIgnoreCase(obj[3].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("woCdGroup.nationName") + " (*)")
        .equalsIgnoreCase(obj[4].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("woCdGroup.email"))
        .equalsIgnoreCase(obj[5].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("woCdGroup.mobile"))
        .equalsIgnoreCase(obj[6].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("woCdGroup.isEnableName") + " (*)")
        .equalsIgnoreCase(obj[7].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("woCdGroup.actionName") + " (*)")
        .equalsIgnoreCase(obj[8].toString().trim())) {
      return false;
    }
    return true;
  }

  public File handleFileExport(List list, String[] columnExport, String date,
      String code) throws Exception {
    List<ConfigFileExport> fileExports = new ArrayList<>();
    Map<String, String> fieldSplit = new HashMap<>();
    List<ConfigHeaderExport> lstHeaderSheet1 = getListHeaderSheet(columnExport);
    String sheetName = "";
    String title = "";
    String fileNameOut = "";
    String headerPrefix = "";
    int mergeTitleEndIndex = 6;
    int startRow = 7;
    switch (code) {
      case "RESULT_IMPORT":
        sheetName = I18n.getLanguage("woCdGroup.import.sheetname");
        title = I18n.getLanguage("woCdGroup.import.title");
        fileNameOut = I18n.getLanguage("woCdGroup.import.fileNameOut");
        headerPrefix = "language.woCdGroup";
        break;
      case "RESULT_IMPORT_ASSIGN_USER":
        sheetName = I18n.getLanguage("woCd.import.sheetname");
        title = I18n.getLanguage("woCd.import.title");
        fileNameOut = I18n.getLanguage("woCd.import.fileNameOut");
        headerPrefix = "language.woCd";
        mergeTitleEndIndex = 4;
        break;
      case "RESULT_IMPORT_ASSIGN_TYPE_GROUP":
        sheetName = I18n.getLanguage("woTypeGroup.import.sheetname");
        title = I18n.getLanguage("woTypeGroup.import.title");
        fileNameOut = I18n.getLanguage("woTypeGroup.import.fileNameOut");
        headerPrefix = "language.woTypeGroup";
        mergeTitleEndIndex = 4;
        break;
      case "EXPORT_WO_CD_GROUP":
        sheetName = I18n.getLanguage("woCdGroup.export.sheetname");
        title = I18n.getLanguage("woCdGroup.export.title");
        fileNameOut = I18n.getLanguage("woCdGroup.export.fileNameOut");
        headerPrefix = "language.woCdGroup";
        break;
      case "EXPORT_WO_CD":
        sheetName = I18n.getLanguage("woCd.export.sheetname");
        title = I18n.getLanguage("woCd.export.title");
        fileNameOut = I18n.getLanguage("woCd.export.fileNameOut");
        headerPrefix = "language.woCd";
        break;
      default:
        break;
    }
    String subTitle;
    if (StringUtils.isNotNullOrEmpty(date)) {
      subTitle = I18n.getLanguage("woCdGroup.export.eportDate", date);
    } else {
      subTitle = "";
    }
    ConfigFileExport configFileExport = new ConfigFileExport(
        list,
        sheetName,
        title,
        subTitle,
        startRow,
        3,
        mergeTitleEndIndex,
        true,
        headerPrefix,
        lstHeaderSheet1,
        fieldSplit,
        "",
        I18n.getLanguage("common.export.firstLeftHeader"),
        I18n.getLanguage("common.export.secondLeftHeader"),
        I18n.getLanguage("common.export.firstRightHeader"),
        I18n.getLanguage("common.export.secondRightHeader")
    );
    configFileExport.setLangKey(I18n.getLocale());
    List<CellConfigExport> cellConfigExportList = new ArrayList<>();
    CellConfigExport cellConfigExport = new CellConfigExport(7, 0, 0, 0,
        I18n.getLanguage("common.STT"), "HEAD", "STRING");
    cellConfigExportList.add(cellConfigExport);
    configFileExport.setLstCreatCell(cellConfigExportList);
    fileExports.add(configFileExport);
    String fileTemplate = "templates" + File.separator + "TEMPLATE_EXPORT.xlsx";
    String rootPath = tempFolder + File.separator;

    File fileExport = CommonExport.exportExcel(fileTemplate, fileNameOut,
        fileExports, rootPath, null);
    return fileExport;
  }

  private List<ConfigHeaderExport> getListHeaderSheet(String[] columnExport) {
    List<ConfigHeaderExport> lstHeaderSheet1 = new ArrayList<>();
    for (int i = 0; i < columnExport.length; i++) {
      ConfigHeaderExport columnSheet1;
      columnSheet1 = new ConfigHeaderExport(columnExport[i], "LEFT", false,
          0, 0, new String[]{}, null, "STRING");
      lstHeaderSheet1.add(columnSheet1);
    }
    return lstHeaderSheet1;
  }

  @Override
  public ResultInSideDto updateStatusCdGroup(WoCdGroupInsideDTO woCdGroupInsideDTO) {
    log.debug("Request to updateStatusCdGroup: {}", woCdGroupInsideDTO);
    return woCdGroupRepository.updateStatusCdGroup(woCdGroupInsideDTO);
  }

  @Override
  public List<WoCdGroupInsideDTO> getListCdGroup(String userName) {
    log.debug("Request to getListCdGroup: {}", userName);
    return woCdGroupRepository.getListCdGroup(userName);
  }

  @Override
  public List<WoCdGroupInsideDTO> getListWoCdGroupDTOByWoTypeAndGroupType(Long woTypeId,
      Long groupTypeId,
      String locale) {
    return woCdGroupRepository.getListGroupDispatch(woTypeId, groupTypeId, locale);
  }

  @Override
  public WoCdGroupInsideDTO getCdByStationCode(String stationCode, String woTypeId,
      String cdGroupType, String businessName) {
    try {
      NimsStationForm form = wsnimsHtPort.getStationInfo(stationCode);
      if (form != null) {
        UnitDTO u = woCdGroupRepository.getUnitCodeMapNims(form.getDEPT_CODE(), businessName);
        if (u != null) {
          return woCdGroupRepository.getCdByUnitCode(u.getUnitCode(), woTypeId, cdGroupType);
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

  @Override
  public List<WoCdGroupInsideDTO> getListCdGroupByUserDTO(WoCdGroupInsideDTO woCdGroupInsideDTO,
      Long woTypeId,
      Long groupTypeId, Long userId, String locale) {
    return woCdGroupRepository
        .getListCdGroupByUserDTO(woCdGroupInsideDTO, woTypeId, groupTypeId, userId, locale);
  }

  @Override
  public WoCdGroupInsideDTO getCdByStationCodeNation(String stationCode, String woTypeId,
      String cdGroupType, String businessName, String nationCode) {
    try {
      if (!StringUtils.isNotNullOrEmpty(stationCode)) {
        return null;
      }
      wsnimsHtGlobalPort.setNationCode(nationCode);
      NimsStationForm form = wsnimsHtGlobalPort.getStationInfo(stationCode);
      if (form != null) {
        UnitDTO u = woCdGroupRepository.getUnitCodeMapNims(form.getDEPT_CODE(), businessName);
        if (u != null) {
          wsnimsHtGlobalPort.clearSetNationCode();
          return woCdGroupRepository.getCdByUnitCode(u.getUnitCode(), woTypeId, cdGroupType);
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    wsnimsHtGlobalPort.clearSetNationCode();
    return null;
  }

  @Override
  public WoCdGroupInsideDTO getWoCdGroupWoByCdGroupCode(String woGroupCode) {
    return woCdGroupRepository.getWoCdGroupWoByCdGroupCode(woGroupCode);
  }

  @Override
  public List<WoCdGroupInsideDTO> getListWoCdGroupActive(WoCdGroupInsideDTO woCdGroupInsideDTO,
      int rowStart,
      int maxRow, String sortType, String sortFieldList) {
    if (woCdGroupInsideDTO != null) {
      List<WoCdGroupInsideDTO> lst = new ArrayList<>();
      List<WoCdGroupInsideDTO> lstTmp = woCdGroupRepository
          .getListWoCdGroupActive(woCdGroupInsideDTO, rowStart, maxRow, sortType, sortFieldList);
      if (lstTmp != null && lstTmp.size() > 0) {
        for (WoCdGroupInsideDTO i : lstTmp) {
          if (StringUtils.isLongNullOrEmpty(i.getIsEnable()) || i.getIsEnable() == 1) {
            lst.add(i);
          }
        }
      }
      return lst;
    }
    return null;
  }

  @Override
  public List<UsersInsideDto> getListFtByUser(String userId, String keyword, int rowStart,
      int maxRow) {
    return woCdGroupRepository.getListFtByUser(userId, keyword, rowStart, maxRow);
  }

  @Override
  public List<WoCdGroupInsideDTO> getListWoCdGroupDTO(WoCdGroupInsideDTO woCdGroupInsideDTO,
      int rowStart, int maxRow,
      String sortType, String sortFieldList) {
    return woCdGroupRepository
        .getListWoCdGroupDTO(woCdGroupInsideDTO, rowStart, maxRow, sortType, sortFieldList);
  }

  @Override
  public List<WoCdGroupInsideDTO> getListCdGroupByDTO(WoCdGroupInsideDTO woCdGroupDTO) {
    return woCdGroupRepository.getListCdGroupByDTO(woCdGroupDTO);
  }

  //2020-10-03 hungtv add
  private List<String> getAllKeysDTO () {
    try {
      List<String> keys = new ArrayList<>();
      Field[] fields = WoCdGroupInsideDTO.class.getDeclaredFields();
      for (Field key : fields) {
        key.setAccessible(true);
        keys.add(key.getName());
      }
      if (keys != null && !keys.isEmpty()) {
        List<String> rmKeys = Arrays.asList("action", "actionName", "woTypeId", "listWoGroupId", "resultImport");
        for (String rmKey : rmKeys) {
          keys.remove(rmKey);
        }
        return keys;
      }
    } catch (Exception err) {
      log.error(err.getMessage());
    }
    return null;
  }
  //end
}
