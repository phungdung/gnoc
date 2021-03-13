package com.viettel.gnoc.mr.business;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.repository.CatItemRepository;
import com.viettel.gnoc.commons.repository.CatLocationRepository;
import com.viettel.gnoc.commons.repository.UnitRepository;
import com.viettel.gnoc.commons.utils.CellConfigExport;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.ConfigFileExport;
import com.viettel.gnoc.commons.utils.ConfigHeaderExport;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.APPLIED_BUSSINESS;
import com.viettel.gnoc.commons.utils.Constants.LANGUAGUE_EXCHANGE_SYSTEM;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.DateUtil;
import com.viettel.gnoc.commons.utils.ExcelWriterUtils;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.maintenance.dto.MrITSoftCrImplUnitDTO;
import com.viettel.gnoc.maintenance.dto.MrITSoftScheduleDTO;
import com.viettel.gnoc.maintenance.dto.MrSynItDevicesDTO;
import com.viettel.gnoc.mr.repository.MrDeviceRepository;
import com.viettel.gnoc.mr.repository.MrITSoftCrImplUnitRepository;
import com.viettel.gnoc.mr.repository.MrSynItSoftDevicesRepository;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
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
public class MrITSoftCrImplUnitBusinessImpl implements MrITSoftCrImplUnitBusiness {

  @Value("${application.upload.folder}")
  private String uploadFolder;

  @Value("${application.temp.folder}")
  private String tempFolder;
  Map<Long, String> mapMarket = new HashMap<>();
  Map<Long, UnitDTO> mapUnit = new HashMap<>();
  Map<String, CatItemDTO> mapArray = new HashMap<>();
  Map<String, List<String>> mapDevice = new HashMap<>();
  Map<Long, List<String>> mapRegion = new HashMap<>();
  private int maxRecord = 500;
  @Autowired
  CatLocationRepository catLocationRepository;

  @Autowired
  UnitRepository unitRepository;

  @Autowired
  CatItemRepository catItemRepository;

  @Autowired
  MrITSoftCrImplUnitRepository mrITSoftCrImplUnitRepository;

  @Autowired
  MrITSoftScheduleBusiness mrITSoftScheduleBusiness;
  @Autowired
  MrDeviceRepository mrDeviceRepository;

  @Autowired
  MrSynItSoftDevicesRepository mrSynItSoftDevicesRepository;

  @Autowired
  TicketProvider ticketProvider;

  @Override
  public Datatable getListDataMrCfgCrUnitITSoft(MrITSoftCrImplUnitDTO mrITSoftCrImplUnitDTO) {
    log.debug("Request to getListDataMrCfgCrUnitITSoft: {}", mrITSoftCrImplUnitDTO);
    Datatable datatable = mrITSoftCrImplUnitRepository
        .getListDataMrCfgCrUnitITSoft(mrITSoftCrImplUnitDTO);
    if (datatable != null) {
      List<MrITSoftCrImplUnitDTO> list = (List<MrITSoftCrImplUnitDTO>) datatable.getData();
      if (list != null && list.size() > 0) {
        setMapMarket();
        setMapUnit();
        for (MrITSoftCrImplUnitDTO dto : list) {
          setDetailValue(dto);
        }
      }
    }
    return datatable;
  }

  @Override
  public ResultInSideDto insertMrCfgCrUnitIT(MrITSoftCrImplUnitDTO mrITSoftCrImplUnitDTO) {
    log.debug("Request to insertMrCfgCrUnitIT: {}", mrITSoftCrImplUnitDTO);
    UserToken userToken = ticketProvider.getUserToken();
    mrITSoftCrImplUnitDTO.setCreateUser(userToken.getUserName());
    return mrITSoftCrImplUnitRepository.insertOrUpdateMrCfgCrUnitIT(mrITSoftCrImplUnitDTO);
  }

  @Override
  public ResultInSideDto updateMrCfgCrUnitIT(MrITSoftCrImplUnitDTO mrITSoftCrImplUnitDTO) {
    log.debug("Request to updateMrCfgCrUnitIT: {}", mrITSoftCrImplUnitDTO);
    return mrITSoftCrImplUnitRepository.insertOrUpdateMrCfgCrUnitIT(mrITSoftCrImplUnitDTO);
  }

  @Override
  public MrITSoftCrImplUnitDTO findMrCfgCrUnitITById(Long cfgId) {
    log.debug("Request to findMrCfgCrUnitITById: {}", cfgId);
    MrITSoftCrImplUnitDTO mrCfgCrUnitTelDTO = mrITSoftCrImplUnitRepository
        .findMrCfgCrUnitITById(cfgId);
    setMapMarket();
    setMapUnit();
    if (mrCfgCrUnitTelDTO != null) {
      setDetailValue(mrCfgCrUnitTelDTO);
    }
    return mrCfgCrUnitTelDTO;
  }

  @Override
  public ResultInSideDto deleteMrCfgCrUnitIT(Long cfgId) {
    log.debug("Request to deleteMrCfgCrUnitTel: {}", cfgId);
    return mrITSoftCrImplUnitRepository.deleteMrCfgCrUnitIT(cfgId);
  }


  @Override
  public File exportData(MrITSoftCrImplUnitDTO mrITSoftCrImplUnitDTO) throws Exception {
    log.debug("Request to exportData: {}", mrITSoftCrImplUnitDTO);
    List<MrITSoftCrImplUnitDTO> list = mrITSoftCrImplUnitRepository
        .getDataExport(mrITSoftCrImplUnitDTO);
    if (list != null && list.size() > 0) {
      setMapMarket();
      setMapUnit();
      for (MrITSoftCrImplUnitDTO dto : list) {
        setDetailValue(dto);
      }
    }
    String[] header = new String[]{"marketName", "region", "arrayCode", "deviceTypeId",
        "implementUnitName", "checkingUnitName"}; // "manageUnitName"
    Date date = DateTimeUtils.convertDateOffset();
    return handleFileExport(list, header, DateUtil.date2ddMMyyyyHHMMss(date),
        "EXPORT_MR_CFG_CR_UNIT_IT");
  }

  public File handleFileExport(List<MrITSoftCrImplUnitDTO> list, String[] columnExport, String date,
      String code) throws Exception {
    List<ConfigFileExport> fileExports = new ArrayList<>();
    Map<String, String> fieldSplit = new HashMap<>();
    List<ConfigHeaderExport> lstHeaderSheet1 = getListHeaderSheet(columnExport);
    String sheetName = "";
    String title = "";
    String fileNameOut = "";
    String headerPrefix = "language.mrCfgCrUnitIT";
    String firstLeftHeader = I18n.getLanguage("common.export.firstLeftHeader");
    String secondLeftHeader = I18n.getLanguage("common.export.secondLeftHeader");
    String firstRightHeader = I18n.getLanguage("common.export.firstRightHeader");
    String secondRightHeader = I18n.getLanguage("common.export.secondRightHeader");
    int mergeTitleEndIndex = 6;
    int startRow = 7;
    int cellTitleIndex = 3;
    String subTitle;
    if (StringUtils.isNotNullOrEmpty(date)) {
      subTitle = I18n.getLanguage("mrCfgCrUnitIT.export.exportDate", date);
    } else {
      subTitle = "";
    }
    switch (code) {
      case "RESULT_IMPORT":
        sheetName = I18n.getLanguage("mrCfgCrUnitIT.import.sheetName");
        title = I18n.getLanguage("mrCfgCrUnitIT.import.title");
        fileNameOut = I18n.getLanguage("mrCfgCrUnitIT.import.fileNameOut");
        break;
      case "EXPORT_MR_CFG_CR_UNIT_IT":
        sheetName = I18n.getLanguage("mrCfgCrUnitIT.export.sheetname");
        title = I18n.getLanguage("mrCfgCrUnitIT.export.title");
        fileNameOut = I18n.getLanguage("mrCfgCrUnitIT.export.fileNameOut");
        break;
      default:
        break;
    }
    ConfigFileExport configFileExport = new ConfigFileExport(
        list,
        sheetName,
        title,
        subTitle,
        startRow,
        cellTitleIndex,
        mergeTitleEndIndex,
        true,
        headerPrefix,
        lstHeaderSheet1,
        fieldSplit,
        "",
        firstLeftHeader,
        secondLeftHeader,
        firstRightHeader,
        secondRightHeader
    );
    configFileExport.setLangKey(I18n.getLocale());
    List<CellConfigExport> cellConfigExportList = new ArrayList<>();
    CellConfigExport cellConfigExport = new CellConfigExport(startRow, 0, 0, 0,
        I18n.getLanguage("common.STT"), "HEAD", "STRING");
    cellConfigExportList.add(cellConfigExport);
    configFileExport.setLstCreatCell(cellConfigExportList);
    fileExports.add(configFileExport);
    String fileTemplate = "templates" + File.separator + "TEMPLATE_EXPORT_NOVALUE.xlsx";
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
  public ResultInSideDto importDataMrCfgCrUnitIT(MultipartFile multipartFile) {
    UserToken userToken = ticketProvider.getUserToken();
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);

    String[] header = new String[]{"marketName", "region", "arrayName", "deviceTypeId",
        "implementUnitName", "checkingUnitName", "resultImport"}; // , "manageUnitName"

    try {
      if (multipartFile == null || multipartFile.isEmpty()) {
        resultInSideDto.setKey(RESULT.FILE_IS_NULL);
        return resultInSideDto;
      } else {
        String filePath = FileUtils
            .saveTempFile(multipartFile.getOriginalFilename(), multipartFile.getBytes(),
                tempFolder);

        File fileImp = new File(filePath);

        List<Object[]> lstHeader;
        lstHeader = CommonImport.getDataFromExcelFile(fileImp, 0, 5,
            0, 6, 1000);
        if (lstHeader.size() == 0 || !validFileFormat(lstHeader)) {
          resultInSideDto.setKey(RESULT.FILE_INVALID_FORMAT);
          return resultInSideDto;
        }

        List<Object[]> lstData = CommonImport.getDataFromExcelFileNew(fileImp, 0, 6,
            0, 6, 1000);
        if (lstData.size() > maxRecord) {
          resultInSideDto.setKey(RESULT.DATA_OVER);
          resultInSideDto.setMessage(String.valueOf(maxRecord));
          return resultInSideDto;
        }

        List<MrITSoftCrImplUnitDTO> listDto = new ArrayList<>();
        Map<String, String> mapImportDTO = new HashMap<>();

        if (!lstData.isEmpty()) {
          int index = 0;
          int value = 1;
          setMapAll();
          setMapUnit();
          for (Object[] obj : lstData) {
            MrITSoftCrImplUnitDTO importDTO = new MrITSoftCrImplUnitDTO();
            if (obj[1] != null) {
              importDTO.setMarketName(obj[1].toString().trim());
            }
            if (obj[2] != null) {
              importDTO.setRegion(obj[2].toString().trim());
            }
            if (obj[3] != null) {
              importDTO.setArrayName(obj[3].toString().trim());
            }
            if (obj[4] != null) {
              importDTO.setDeviceTypeId(obj[4].toString().trim());
            }
            if (obj[5] != null) {
              importDTO.setImplementUnitName(obj[5].toString().trim());
            }
            if (obj[6] != null) {
              importDTO.setCheckingUnitName(obj[6].toString().trim());
            }
//            if (obj[7] != null) {
//              importDTO.setManageUnitName(obj[7].toString().trim());
//            }
            MrITSoftCrImplUnitDTO tempImportDTO = validateImportInfo(importDTO, mapImportDTO,
                String.valueOf(value));

            if (tempImportDTO.getResultImport() == null) {
              tempImportDTO
                  .setResultImport(I18n.getLanguage("mrCfgCrUnitIT.result.import.ok"));
              listDto.add(tempImportDTO);
            } else {
              listDto.add(importDTO);
              index++;
            }
            value++;
          }
          if (index == 0) {
            if (listDto != null && !listDto.isEmpty()) {
              for (MrITSoftCrImplUnitDTO dto : listDto) {
                if (dto.getCfgId() == null) {
                  dto.setCreateUser(userToken.getUserName());
                }
                resultInSideDto = mrITSoftCrImplUnitRepository.insertOrUpdateMrCfgCrUnitIT((dto));
              }
            }
          } else {
            File fileExport = handleFileExport(listDto, header, null, "RESULT_IMPORT");
            resultInSideDto.setKey(RESULT.ERROR);
            resultInSideDto.setMessage(I18n.getLanguage("import.common.fail"));
            resultInSideDto.setFile(fileExport);
            resultInSideDto.setFilePath(fileExport.getPath());
            return resultInSideDto;
          }
        } else {
          resultInSideDto.setKey(RESULT.NODATA);
          resultInSideDto.setMessage(Constants.FILE_NULL);
          File fileExport = handleFileExport(listDto, header, null, "RESULT_IMPORT");
          resultInSideDto.setFile(fileExport);
          resultInSideDto.setFilePath(fileExport.getPath());
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

  private boolean validFileFormat(List<Object[]> lstHeader) {
    Object[] obj = lstHeader.get(0);
    int count = 0;
    for (Object data : obj) {
      if (data != null) {
        count += 1;
      }
    }
    if (count != 7) {
      return false;
    }
    if (obj == null) {
      return false;
    }
    if (obj[0] == null) {
      return false;
    }
    if (!(I18n.getLanguage("mrCfgCrUnitIT.STT")).equalsIgnoreCase(obj[0].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("mrCfgCrUnitIT.marketName") + " (*)")
        .equalsIgnoreCase(obj[1].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("mrCfgCrUnitIT.region") + " (*)")
        .equalsIgnoreCase(obj[2].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("mrCfgCrUnitIT.arrayCode") + " (*)")
        .equalsIgnoreCase(obj[3].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("mrCfgCrUnitIT.deviceTypeId") + " (*)")
        .equalsIgnoreCase(obj[4].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("mrCfgCrUnitIT.implementUnit") + " (*)")
        .equalsIgnoreCase(obj[5].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("mrCfgCrUnitIT.checkingUnit") + " (*)")
        .equalsIgnoreCase(obj[6].toString().trim())) {
      return false;
    }
//    if (!(I18n.getLanguage("mrCfgCrUnitIT.manageUnit") + " (*)")
//        .equalsIgnoreCase(obj[7].toString().trim())) {
//      return false;
//    }
    return true;
  }


  public void setMapMarket() {
    List<ItemDataCRInside> list = catLocationRepository.getListLocationByLevelCBB(null, 1L, null);
    if (list != null && !list.isEmpty()) {
      for (ItemDataCRInside dto : list) {
        mapMarket.put(dto.getValueStr(), dto.getDisplayStr());
      }
    }
  }

  public void setMapUnit() {
    List<UnitDTO> list = unitRepository.getListUnit(new UnitDTO());
    if (list != null && list.size() > 0) {
      for (UnitDTO dto : list) {
        mapUnit.put(dto.getUnitId(), dto);
      }
    }
  }

  public void setMapAll() {
    List<ItemDataCRInside> listMarket = catLocationRepository
        .getListLocationByLevelCBB(null, 1L, null);
    if (listMarket != null && !listMarket.isEmpty()) {
      for (ItemDataCRInside dto : listMarket) {
        mapMarket.put(dto.getValueStr(), dto.getDisplayStr());
        List<MrITSoftScheduleDTO> listRegion = mrITSoftScheduleBusiness
            .getListRegionByMrSynItDevices(String.valueOf(dto.getValueStr()));
        if (listRegion != null && listRegion.size() > 0) {
          List<String> regionList = new ArrayList<>();
          for (MrITSoftScheduleDTO dtoRegion : listRegion) {
            regionList.add(dtoRegion.getRegion());
          }
          mapRegion.put(dto.getValueStr(), regionList);
        } else {
          mapRegion.put(dto.getValueStr(), new ArrayList<>());
        }
      }
    }
    Datatable dataArray = catItemRepository.getItemMaster(Constants.MR_ITEM_NAME.MR_SUBCATEGORY,
        LANGUAGUE_EXCHANGE_SYSTEM.COMMON, APPLIED_BUSSINESS.CAT_ITEM.toString(),
        Constants.ITEM_ID, Constants.ITEM_NAME);
    List<CatItemDTO> listArray = (List<CatItemDTO>) dataArray.getData();
    if (listArray != null && listArray.size() > 0) {
      for (CatItemDTO dto : listArray) {
        mapArray.put(dto.getItemName(), dto);
        for (CatItemDTO array : listArray) {
          List<MrSynItDevicesDTO> listDevice = mrSynItSoftDevicesRepository
              .getListDeviceTypeByArrayCode(String.valueOf(array.getItemCode()));

          if (listDevice != null && listDevice.size() > 0) {
            List<String> lstDeviceStr = new ArrayList<>();
            for (MrSynItDevicesDTO device : listDevice) {
              lstDeviceStr.add(device.getDeviceType());
            }
            mapDevice.put(array.getItemCode(), lstDeviceStr);
          } else {
            mapDevice.put(array.getItemCode(), new ArrayList<>());
          }
        }
      }
    }
  }

  public void setDetailValue(MrITSoftCrImplUnitDTO mrITSoftCrImplUnitDTO) {
    if (StringUtils.isNotNullOrEmpty(mrITSoftCrImplUnitDTO.getMarketCode())) {
      mrITSoftCrImplUnitDTO
          .setMarketName(mapMarket.get(Long.valueOf(mrITSoftCrImplUnitDTO.getMarketCode())));
    }
    Long implementUnitId = Long.valueOf(mrITSoftCrImplUnitDTO.getImplementUnit());
    if (implementUnitId != null && mapUnit.containsKey(implementUnitId)) {
      mrITSoftCrImplUnitDTO.setImplementUnitName(mapUnit.get(implementUnitId).getUnitName());
    }
    Long checkingUnitId = Long.valueOf(mrITSoftCrImplUnitDTO.getCheckingUnit());
    if (checkingUnitId != null && mapUnit.containsKey(checkingUnitId)) {
      mrITSoftCrImplUnitDTO.setCheckingUnitName(mapUnit.get(checkingUnitId).getUnitName());
    }

//    Long manageUnitId = Long.valueOf(mrITSoftCrImplUnitDTO.getManageUnit());
//    if (manageUnitId != null && mapUnit.containsKey(manageUnitId)) {
//      mrITSoftCrImplUnitDTO.setManageUnitName(mapUnit.get(manageUnitId).getUnitName());
//    }

  }

  public MrITSoftCrImplUnitDTO validateImportInfo(MrITSoftCrImplUnitDTO importDTO,
      Map<String, String> mapImportDTO, String value) {
    if (StringUtils.isNotNullOrEmpty(importDTO.getMarketName())) {
      try {
        if (mapMarket.containsKey(Long.parseLong(importDTO.getMarketName()))) {
          importDTO.setMarketCode(importDTO.getMarketName());
        }
      } catch (Exception e) {
        importDTO.setResultImport(I18n.getLanguage("mrCfgCrUnitIT.err.marketName.valid"));
        return importDTO;
      }
      if (StringUtils.isStringNullOrEmpty(importDTO.getMarketCode())) {
        importDTO.setResultImport(I18n.getLanguage("mrCfgCrUnitIT.err.marketName.valid"));
        return importDTO;
      }
    } else {
      importDTO.setResultImport(I18n.getLanguage("mrCfgCrUnitIT.err.marketName"));
      return importDTO;
    }
    if (StringUtils.isNotNullOrEmpty(importDTO.getRegion())) {
      List<String> listRegion = mapRegion.get(Long.valueOf(importDTO.getMarketCode()));
      if (listRegion.size() > 0) {
        if (!listRegion.contains(importDTO.getRegion())) {
          importDTO.setResultImport(I18n.getLanguage("mrCfgCrUnitIT.err.region.valid"));
          return importDTO;
        }
      }
      if (listRegion.size() == 0) {
        importDTO.setResultImport(I18n.getLanguage("mrCfgCrUnitIT.err.region.valid"));
        return importDTO;
      }
    } else {
      importDTO.setResultImport(I18n.getLanguage("mrCfgCrUnitIT.err.region"));
      return importDTO;
    }
    if (StringUtils.isNotNullOrEmpty(importDTO.getArrayName())) {
      if (mapArray.containsKey(importDTO.getArrayName())) {
        importDTO.setArrayCode(importDTO.getArrayName());
      }
      if (StringUtils.isStringNullOrEmpty(importDTO.getArrayCode())) {
        importDTO.setResultImport(I18n.getLanguage("mrCfgCrUnitIT.err.array.valid"));
        return importDTO;
      }
    } else {
      importDTO.setResultImport(I18n.getLanguage("mrCfgCrUnitIT.err.arrayCode"));
      return importDTO;
    }
    if (StringUtils.isNotNullOrEmpty(importDTO.getDeviceTypeId())) {
      List<String> listDevice = mapDevice.get(importDTO.getArrayCode());
      if (listDevice != null && !listDevice.isEmpty()) {
        if (!listDevice.contains(importDTO.getDeviceTypeId())) {
          importDTO.setResultImport(I18n.getLanguage("mrCfgCrUnitIT.err.deviceType.valid"));
          return importDTO;
        }
      } else {
        importDTO.setResultImport(I18n.getLanguage("mrCfgCrUnitIT.err.deviceType.valid"));
        return importDTO;
      }
    } else {
      importDTO.setResultImport(I18n.getLanguage("mrCfgCrUnitIT.err.deviceTypeId"));
      return importDTO;
    }
    if (StringUtils.isNotNullOrEmpty(importDTO.getImplementUnitName())) {
      try {
        if (mapUnit.containsKey(Long.parseLong(importDTO.getImplementUnitName()))) {
          importDTO.setImplementUnit(String.valueOf(importDTO.getImplementUnitName()));
        }
      } catch (Exception e) {
        importDTO.setResultImport(I18n.getLanguage("mrCfgCrUnitIT.err.implementUnit.valid"));
        return importDTO;
      }
      if (StringUtils.isStringNullOrEmpty(importDTO.getImplementUnit())) {
        importDTO.setResultImport(I18n.getLanguage("mrCfgCrUnitIT.err.implementUnit.valid"));
        return importDTO;
      }
    } else {
      importDTO.setResultImport(I18n.getLanguage("mrCfgCrUnitIT.err.implementUnitName"));
      return importDTO;
    }
    if (StringUtils.isNotNullOrEmpty(importDTO.getCheckingUnitName())) {
      try {
        if (mapUnit.containsKey(Long.parseLong(importDTO.getCheckingUnitName()))) {
          importDTO.setCheckingUnit(importDTO.getCheckingUnitName());
        }
      } catch (Exception e) {
        importDTO.setResultImport(I18n.getLanguage("mrCfgCrUnitIT.err.checkingUnit.valid"));
        return importDTO;
      }
      if (importDTO.getCheckingUnit() == null) {
        importDTO.setResultImport(I18n.getLanguage("mrCfgCrUnitIT.err.checkingUnit.valid"));
        return importDTO;
      }
    } else {
      importDTO.setResultImport(I18n.getLanguage("mrCfgCrUnitIT.err.checkingUnitName"));
      return importDTO;
    }
//    if (StringUtils.isNotNullOrEmpty(importDTO.getManageUnitName())) {
//      try {
//        if (mapUnit.containsKey(Long.parseLong(importDTO.getManageUnitName()))) {
//          importDTO.setManageUnit(importDTO.getManageUnitName());
//        }
//      } catch (Exception e) {
//        importDTO.setResultImport(I18n.getLanguage("mrCfgCrUnitIT.err.manageUnit.valid"));
//        return importDTO;
//      }
//      if (importDTO.getManageUnit() == null) {
//        importDTO.setResultImport(I18n.getLanguage("mrCfgCrUnitIT.err.manageUnit.valid"));
//        return importDTO;
//      }
//    } else {
//      importDTO.setResultImport(I18n.getLanguage("mrCfgCrUnitIT.err.manageUnitName"));
//      return importDTO;
//    }
    String validateDuplicate = validateDuplicateImport(importDTO, mapImportDTO, value);
    if (StringUtils.isNotNullOrEmpty(validateDuplicate)) {
      importDTO.setResultImport(validateDuplicate);
      return importDTO;
    }
    MrITSoftCrImplUnitDTO dtoTmp = mrITSoftCrImplUnitRepository.checkMrCfgCrUnitITExit(importDTO);
    if (dtoTmp != null) {
      importDTO.setCreateUser(dtoTmp.getCreateUser());
      importDTO.setCfgId(dtoTmp.getCfgId());
    }
    return importDTO;
  }

  public String validateDuplicateImport(MrITSoftCrImplUnitDTO importDTO,
      Map<String, String> mapImportDTO, String value) {
    String marketCode = importDTO.getMarketCode();
    String region = importDTO.getRegion();
    String arrayCode = importDTO.getArrayCode();
    String deviceType = importDTO.getDeviceTypeId();
    String key = marketCode + "_" + region + "_" + arrayCode + "_" + deviceType;
    if (mapImportDTO.containsKey(key)) {
      return I18n.getLanguage("mrCfgCrUnitIT.err.dup-code-in-file")
          .replaceAll("0", mapImportDTO.get(key));
    } else {
      mapImportDTO.put(key, value);
    }
    if (StringUtils.isNotNullOrEmpty(value)) {

    }
    return null;
  }


  @Override
  public File getTemplateImport() throws IOException {
    ExcelWriterUtils ewu = new ExcelWriterUtils();
    String templatePathOut = "templates" + File.separator + "TEMPLATE_EXPORT_NOVALUE.xlsx";
    Resource resource = new ClassPathResource(templatePathOut);
    InputStream fileTemplate = resource.getInputStream();
    XSSFWorkbook workBook = new XSSFWorkbook(fileTemplate);

    Map<String, CellStyle> styles = CommonExport.createStyles(workBook);

    XSSFSheet sheetMain = workBook.getSheetAt(0);
    XSSFSheet sheetOrther = workBook.createSheet("Orther");
    XSSFSheet sheetMarketName = workBook.createSheet(I18n.getLanguage("mrCfgCrUnitIT.marketName"));
    XSSFSheet sheetRegion = workBook.createSheet(I18n.getLanguage("mrCfgCrUnitIT.region"));
    XSSFSheet sheetArrayCode = workBook.createSheet(I18n.getLanguage("mrCfgCrUnitIT.arrayCode"));
    XSSFSheet sheetDeviceType = workBook
        .createSheet(I18n.getLanguage("mrCfgCrUnitIT.deviceTypeId"));
    XSSFSheet sheetUnit = workBook.createSheet("Unit");

    String[] header = new String[]{
        I18n.getLanguage("mrCfgCrUnitIT.STT"),
        I18n.getLanguage("mrCfgCrUnitIT.marketName"),
        I18n.getLanguage("mrCfgCrUnitIT.region"),
        I18n.getLanguage("mrCfgCrUnitIT.arrayCode"),
        I18n.getLanguage("mrCfgCrUnitIT.deviceTypeId"),
        I18n.getLanguage("mrCfgCrUnitIT.implementUnit"),
        I18n.getLanguage("mrCfgCrUnitIT.checkingUnit"),
//        I18n.getLanguage("mrCfgCrUnitIT.manageUnit")
    };
    String[] headerStar = new String[]{
        I18n.getLanguage("mrCfgCrUnitIT.marketName"),
        I18n.getLanguage("mrCfgCrUnitIT.region"),
        I18n.getLanguage("mrCfgCrUnitIT.arrayCode"),
        I18n.getLanguage("mrCfgCrUnitIT.deviceTypeId"),
        I18n.getLanguage("mrCfgCrUnitIT.implementUnit"),
        I18n.getLanguage("mrCfgCrUnitIT.checkingUnit"),
//        I18n.getLanguage("mrCfgCrUnitIT.manageUnit")
    };
    XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(sheetMain);
    List<String> listHeader = Arrays.asList(header);
    List<String> listHeaderStar = Arrays.asList(headerStar);

    int sttColumn = listHeader.indexOf(I18n.getLanguage("mrCfgCrUnitIT.STT"));
    int marketColumn = listHeader.indexOf(I18n.getLanguage("mrCfgCrUnitIT.marketName"));
    int regionColumn = listHeader.indexOf(I18n.getLanguage("mrCfgCrUnitIT.region"));
    int arrayColumn = listHeader.indexOf(I18n.getLanguage("mrCfgCrUnitIT.arrayCode"));
    int deviceTypeColumn = listHeader.indexOf(I18n.getLanguage("mrCfgCrUnitIT.deviceTypeId"));
    int implementUnitColumn = listHeader
        .indexOf(I18n.getLanguage("mrCfgCrUnitIT.implementUnit"));
    int checkingUnitColumn = listHeader
        .indexOf(I18n.getLanguage("mrCfgCrUnitIT.checkingUnit"));
//    int manageUnitColumn = listHeader
//        .indexOf(I18n.getLanguage("mrCfgCrUnitIT.manageUnit"));

    String firstLeftHeaderTitle = I18n.getLanguage("common.export.firstLeftHeader");
    String secondLeftHeaderTitle = I18n.getLanguage("common.export.secondLeftHeader");
    String firstRightHeaderTitle = I18n.getLanguage("common.export.firstRightHeader");
    String secondRightHeaderTitle = I18n.getLanguage("common.export.secondRightHeader");

    CellStyle cellStyleTopHeader = CommonExport.setCellStyleTopHeader(workBook);
    CellStyle cellStyleTopRightHeader = CommonExport.setCellStyleTopRightHeader(workBook);
    CellStyle cellStyleTitle = CommonExport.setCellStyleTitle(workBook);
    CellStyle cellStyleHeader = CommonExport.setCellStyleHeader(workBook);

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
    sheetMain.addMergedRegion(new CellRangeAddress(0, 0, 1, 3));
    sheetMain.addMergedRegion(new CellRangeAddress(1, 1, 1, 3));
    sheetMain.addMergedRegion(new CellRangeAddress(0, 0, sizeheaderTitle - 2,
        sizeheaderTitle + 1));
    sheetMain.addMergedRegion(new CellRangeAddress(1, 1, sizeheaderTitle - 2,
        sizeheaderTitle + 1));

    //Tao tieu de
    Row rowMainTitle = sheetMain.createRow(3);
    Cell mainCellTitle = rowMainTitle.createCell(1);
    mainCellTitle.setCellValue(I18n.getLanguage("mrCfgCrUnitIT.template.title"));
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

    int row = 1;
    List<ItemDataCRInside> listMarket = catLocationRepository
        .getListLocationByLevelCBB(null, 1L, null);
    ewu.createCell(sheetMarketName, 0, 0,
        I18n.getLanguage("mrCfgCrUnitIT.headerMarketCode").toUpperCase(), styles.get("header"));
    ewu.createCell(sheetMarketName, 1, 0,
        I18n.getLanguage("mrCfgCrUnitIT.headerMarketName").toUpperCase(), styles.get("header"));
    if (listMarket != null && listMarket.size() > 0) {
      for (ItemDataCRInside dto : listMarket) {
        ewu.createCell(sheetMarketName, 0, row, dto.getValueStr().toString(), styles.get("cell"));
        ewu.createCell(sheetMarketName, 1, row++, dto.getDisplayStr(), styles.get("cell"));
      }
    }
    sheetMarketName.setColumnWidth(0, 3000);
    sheetMarketName.setColumnWidth(1, 7000);
    sheetMarketName.setSelected(false);

    row = 1;
    List<CatItemDTO> listArray = (List<CatItemDTO>) catItemRepository
        .getItemMaster(Constants.MR_ITEM_NAME.MR_SUBCATEGORY,
            LANGUAGUE_EXCHANGE_SYSTEM.COMMON, APPLIED_BUSSINESS.CAT_ITEM.toString(),
            Constants.ITEM_ID, Constants.ITEM_NAME).getData();
    ewu.createCell(sheetArrayCode, 0, 0,
        I18n.getLanguage("mrCfgCrUnitIT.headerArrayCode").toUpperCase(), styles.get("header"));
    ewu.createCell(sheetArrayCode, 1, 0,
        I18n.getLanguage("mrCfgCrUnitIT.headerArrayName").toUpperCase(), styles.get("header"));
    if (listArray != null && listArray.size() > 0) {
      for (CatItemDTO dto : listArray) {
        ewu.createCell(sheetArrayCode, 0, row, dto.getItemCode(), styles.get("cell"));
        ewu.createCell(sheetArrayCode, 1, row++, dto.getItemName(), styles.get("cell"));
      }
    }
    sheetArrayCode.setColumnWidth(0, 20000);
    sheetArrayCode.setColumnWidth(1, 20000);
    sheetArrayCode.setSelected(false);

    List<UnitDTO> listUnit = unitRepository.getListUnit(null);
    row = 1;
    ewu.createCell(sheetUnit, 0, 0, I18n.getLanguage("mrCfgCrUnitIT.unitId").toUpperCase(),
        styles.get("header"));
    ewu.createCell(sheetUnit, 1, 0, I18n.getLanguage("mrCfgCrUnitIT.unitCode").toUpperCase(),
        styles.get("header"));
    ewu.createCell(sheetUnit, 2, 0, I18n.getLanguage("mrCfgCrUnitIT.unitName").toUpperCase(),
        styles.get("header"));
    ewu.createCell(sheetUnit, 3, 0, I18n.getLanguage("mrCfgCrUnitIT.unitParentId").toUpperCase(),
        styles.get("header"));
    for (UnitDTO dto : listUnit) {
      ewu.createCell(sheetUnit, 0, row, String.valueOf(dto.getUnitId()), styles.get("cell"));
      ewu.createCell(sheetUnit, 1, row, dto.getUnitCode(), styles.get("cell"));
      ewu.createCell(sheetUnit, 2, row, dto.getUnitTrueName(), styles.get("cell"));
      ewu.createCell(sheetUnit, 3, row++, String.valueOf(dto.getParentUnitId()),
          styles.get("cell"));
    }
    sheetUnit.setColumnWidth(0, 3000);
    sheetUnit.setColumnWidth(1, 7000);
    sheetUnit.setColumnWidth(2, 10000);
    sheetUnit.setColumnWidth(3, 4000);
    sheetUnit.setSelected(false);

    row = 1;
    ewu.createCell(sheetRegion, 0, 0,
        I18n.getLanguage("mrCfgCrUnitIT.marketName").toUpperCase(), styles.get("header"));
    ewu.createCell(sheetRegion, 1, 0,
        I18n.getLanguage("mrCfgCrUnitIT.region").toUpperCase(), styles.get("header"));
    if (listMarket != null && listMarket.size() > 0) {
      for (ItemDataCRInside dto : listMarket) {
        List<MrITSoftScheduleDTO> listRegion = mrITSoftScheduleBusiness
            .getListRegionByMrSynItDevices(String.valueOf(dto.getValueStr()));
        List<String> lstRegionStr = new ArrayList<>();
        for (MrITSoftScheduleDTO dtoRegion : listRegion) {
          lstRegionStr.add(dtoRegion.getRegion());
        }
        if (lstRegionStr != null && lstRegionStr.size() > 0) {
          for (String region : lstRegionStr) {
            ewu.createCell(sheetRegion, 0, row, dto.getDisplayStr(), styles.get("cell"));
            ewu.createCell(sheetRegion, 1, row++, region, styles.get("cell"));
          }
        }
      }
    }
    sheetRegion.autoSizeColumn(0);
    sheetRegion.autoSizeColumn(1);
    sheetRegion.setSelected(false);

    row = 1;
    int rowDe = 1;

    ewu.createCell(sheetDeviceType, 0, 0,
        I18n.getLanguage("mrCfgCrUnitIT.arrayCode").toUpperCase(), styles.get("header"));
    ewu.createCell(sheetDeviceType, 1, 0,
        I18n.getLanguage("mrCfgCrUnitIT.deviceTypeId").toUpperCase(), styles.get("header"));
    if (listArray != null && listArray.size() > 0) {
      for (CatItemDTO dto : listArray) {
        List<MrSynItDevicesDTO> listDevice = mrSynItSoftDevicesRepository
            .getListDeviceTypeByArrayCode(dto.getItemName());
        List<String> listDeviceStr = new ArrayList<>();
        for (MrSynItDevicesDTO device : listDevice) {
          listDeviceStr.add(device.getDeviceType());
        }

        for (String deviceStr : listDeviceStr) {
          ewu.createCell(sheetDeviceType, 0, rowDe, dto.getItemName(), styles.get("cell"));
          ewu.createCell(sheetDeviceType, 1, rowDe++, deviceStr, styles.get("cell"));
        }

      }
    }

    sheetDeviceType.autoSizeColumn(0);
    sheetDeviceType.autoSizeColumn(1);
    sheetDeviceType.autoSizeColumn(2);
    sheetDeviceType.setSelected(false);

    sheetMain.setColumnWidth(sttColumn, 2000);
    sheetMain.setColumnWidth(marketColumn, 7000);
    sheetMain.setColumnWidth(regionColumn, 7000);
    sheetMain.setColumnWidth(arrayColumn, 10000);
    sheetMain.setColumnWidth(deviceTypeColumn, 6000);
    sheetMain.setColumnWidth(implementUnitColumn, 7000);
    sheetMain.setColumnWidth(checkingUnitColumn, 7000);
//    sheetMain.setColumnWidth(manageUnitColumn, 7000);
    sheetOrther.setSelected(false);
    workBook.setSheetHidden(1, true);
    workBook.setSheetName(0, I18n.getLanguage("mrCfgCrUnitIT.import.sheetName"));

    String fileResult = tempFolder + File.separator;
    String fileName = "IMPORT_MR_CFG_CR_UNIT_TEMPLATE" + "_" + System.currentTimeMillis() + ".xlsx";
    ewu.saveToFileExcel(workBook, fileResult, fileName);
    String resultPath = fileResult + fileName;
    File fileExport = new File(resultPath);
    return fileExport;
  }
}
