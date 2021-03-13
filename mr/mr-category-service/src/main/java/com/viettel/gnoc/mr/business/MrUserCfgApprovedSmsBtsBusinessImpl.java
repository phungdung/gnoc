package com.viettel.gnoc.mr.business;

import com.viettel.gnoc.commons.business.CatItemBusiness;
import com.viettel.gnoc.commons.business.CatLocationBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.CatLocationDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.commons.model.UsersEntity;
import com.viettel.gnoc.commons.repository.UserRepository;
import com.viettel.gnoc.commons.utils.CellConfigExport;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.ConfigFileExport;
import com.viettel.gnoc.commons.utils.ConfigHeaderExport;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.ExcelWriterUtils;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.maintenance.dto.MrUserCfgApprovedSmsBtsDTO;
import com.viettel.gnoc.mr.repository.MrDeviceRepository;
import com.viettel.gnoc.mr.repository.MrScheduleTelRepository;
import com.viettel.gnoc.mr.repository.MrUserCfgApprovedSmsBtsRepository;
import java.io.File;
import java.io.InputStream;
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
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
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
public class MrUserCfgApprovedSmsBtsBusinessImpl implements MrUserCfgApprovedSmsBtsBusiness {

  @Value("${application.temp.folder}")
  private String tempFolder;
  private Map<String, String> mapAreaName = new HashMap<>();
  private Map<String, String> mapMarketName = new HashMap<>();
  private Map<String, String> mapAreaID = new HashMap<>();
  private Map<String, String> mapProvinceId = new HashMap<>();
  private Map<String, String> mapProvinceName = new HashMap<>();
  private Map<String, String> mapAreaByMarketCode = new HashMap<>();
  private Map<String, String> mapProvinceByAreaCode = new HashMap<>();
  private Map<String, MrUserCfgApprovedSmsBtsDTO> mapUserInSystem = new HashMap<>();
  public final static String REGEX_NUMBER_MOBILE = "([+?]*[0-9])([0-9])*";
  public final static String REGEX_NUMBER = "([0-9])([0-9])*";


  private List<ItemDataCRInside> lstCountry;

  @Autowired
  MrUserCfgApprovedSmsBtsRepository mrUserCfgApprovedSmsBtsRepository;

  @Autowired
  protected CatLocationBusiness catLocationBusiness;

  @Autowired
  TicketProvider ticketProvider;

  @Autowired
  CatItemBusiness catItemBusiness;

  @Autowired
  MrScheduleTelRepository mrScheduleTelRepository;

  @Autowired
  MrDeviceRepository mrDeviceRepository;

  @Autowired
  MrCfgProcedureCDBusiness mrCfgProcedureCDBusiness;

  @Autowired
  UserRepository userRepository;


  @Override
  public Datatable getListMrUserCfgApprovedSmsBts(MrUserCfgApprovedSmsBtsDTO smsBtsDTO) {
    Datatable datatable = mrUserCfgApprovedSmsBtsRepository
        .getListMrUserCfgApprovedSmsBts(smsBtsDTO);
    List<MrUserCfgApprovedSmsBtsDTO> list = (List<MrUserCfgApprovedSmsBtsDTO>) datatable.getData();
    for (MrUserCfgApprovedSmsBtsDTO dto : list) {
      if ("CT".equals(dto.getAreaCode())) {
        dto.setAreaName(I18n.getLanguage("mrUserCfgApprovedSmsBts.areaCode.isCompany"));
      }
    }
    datatable.setData(list);
    return datatable;
  }

  @Override
  public MrUserCfgApprovedSmsBtsDTO getDetail(Long id) {
    mapAreaName.clear();
    mapMarketName.clear();
    mapAreaID.clear();
    mapProvinceId.clear();
    setMapCountryName();
    MrUserCfgApprovedSmsBtsDTO dto = mrUserCfgApprovedSmsBtsRepository.getDetail(id);
    setMapProvinceName(dto.getProvinceCode());
    List<CatLocationDTO> dataList = catLocationBusiness
        .getCatLocationByParentId(dto.getMarketCode());
    if (dataList != null && !dataList.isEmpty()) {
      for (CatLocationDTO catLocationDTO : dataList) {
        mapAreaName.put(String.valueOf(catLocationDTO.getLocationCode()),
            catLocationDTO.getLocationName());
        mapAreaID
            .put(String.valueOf(catLocationDTO.getLocationCode()), catLocationDTO.getLocationId());
      }
    }
    dto.setAreaId(mapAreaID.get(dto.getAreaCode()));
    dto.setAreaName(mapAreaName.get(dto.getAreaCode()));
    dto.setMarketName(mapMarketName.get(dto.getMarketCode()));
    dto.setProvinceName(mapProvinceName.get(dto.getProvinceCode()));
    dto.setProvinceId(mapProvinceId.get(dto.getProvinceCode()));
    return dto;
  }

  @Override
  public ResultInSideDto insertOrUpdate(MrUserCfgApprovedSmsBtsDTO smsBtsDTO) {
    //check trùng
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    UserToken userToken = ticketProvider.getUserToken();
    List<MrUserCfgApprovedSmsBtsDTO> listCheckExisted = mrUserCfgApprovedSmsBtsRepository
        .checkExisted(smsBtsDTO);
    if (listCheckExisted != null && !listCheckExisted.isEmpty()) {
      resultInSideDto.setKey(RESULT.DUPLICATE);
      resultInSideDto
          .setMessage(I18n.getLanguage("mrUserCfgApprovedSmsBts.list.grid.userName") + " "
              + listCheckExisted.get(0).getUserName() + " " + I18n
              .getLanguage("mrUserCfgApprovedSmsBts.insertOrUpdate.existed"));
    } else {
      if (smsBtsDTO.getUserCfgApprovedSmsId() == null) {
        smsBtsDTO.setCreateTime(DateTimeUtils.convertDateTimeStampToString(new Date()));
      } else {
        smsBtsDTO.setUpdateTime(DateTimeUtils.convertDateTimeStampToString(new Date()));
        smsBtsDTO.setUserUpdate(userToken.getUserName());
      }
      resultInSideDto = mrUserCfgApprovedSmsBtsRepository.insertOrUpdate(smsBtsDTO);
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto deleteMrUserCfgApprovedSmsBts(Long userCfgApprovedSmsId) {
    return mrUserCfgApprovedSmsBtsRepository.deleteMrUserCfgApprovedSmsBts(userCfgApprovedSmsId);
  }

  @Override
  public UsersInsideDto getUserByUserId(Long userId) {
    UsersEntity usersEntity = userRepository.getUserByUserId(userId);
    UsersInsideDto usersInsideDto = usersEntity.toDTO();
    return usersInsideDto;
  }

  @Override
  public MrUserCfgApprovedSmsBtsDTO getApproveLevelByUserLogin() {
    UserToken userToken = ticketProvider.getUserToken();
    return mrUserCfgApprovedSmsBtsRepository.getApproveLevelByUserLogin(userToken.getUserName());
  }

  @Override
  public File exportData(MrUserCfgApprovedSmsBtsDTO smsBtsDTO) throws Exception {
    List<MrUserCfgApprovedSmsBtsDTO> listExportBySearch = mrUserCfgApprovedSmsBtsRepository
        .onSearchExport(smsBtsDTO);
    for (MrUserCfgApprovedSmsBtsDTO dto : listExportBySearch) {
      if ("CT".equals(dto.getAreaCode())) {
        dto.setAreaName(I18n.getLanguage("mrUserCfgApprovedSmsBts.areaCode.isCompany"));
      }
    }
    return exportFileEx(listExportBySearch);
  }

  private File exportFileEx(List<MrUserCfgApprovedSmsBtsDTO> listExportBySearch) throws Exception {
    String title = I18n.getLanguage("mrUserCfgApprovedSmsBts.list.grid");
    String fileNameOut = "EXPORT_MR_USER_CFG_APPROVED_SMS_BTS";
    List<ConfigFileExport> fileExports = new ArrayList<>();
    List<ConfigHeaderExport> lstHeaderSheet = new ArrayList<>();
    String subTitle = I18n
        .getLanguage("mrUserCfgApprovedSmsBts.export.exportDate",
            DateTimeUtils.convertDateOffset());
    lstHeaderSheet = readerHeaderSheet("userName", "fullName", "mobile", "marketName", "areaName",
        "provinceName", "approveLevelStr", "receiveMessageBDStr");

    Map<String, String> filedSplit = new HashMap<>();
    ConfigFileExport configFileExport = new ConfigFileExport(
        listExportBySearch,
        I18n.getLanguage("mrUserCfgApprovedSmsBts.list.grid"),
        title,
        subTitle,
        7,
        3,
        7,
        true,
        "language.mrUserCfgApprovedSmsBts.list.grid",
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
    configFileExport.setCustomColumnWidthNoMerge(
        new String[]{"1500", "7000", "7000", "7000", "7000",
            "7000", "7000", "7000", "7000"});
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

  private File exportFileImport(List<MrUserCfgApprovedSmsBtsDTO> listExportByImport, String key)
      throws Exception {
    String title = I18n.getLanguage("mrUserCfgApprovedSmsBts.import.title");
    String fileNameOut = "RESULT_IMPORT_MR_USER_CFG_APPROVED_SMS_BTS";
    List<ConfigFileExport> fileExports = new ArrayList<>();
    List<ConfigHeaderExport> lstHeaderSheet = new ArrayList<>();
    if (Constants.RESULT_IMPORT.equalsIgnoreCase(key)) {
      lstHeaderSheet = readerHeaderSheet("marketLocationCode", "areaCode", "provinceCode",
          "userName", "approveLevel", "receiveMessageBD", "resultImport");
      Map<String, String> filedSplit = new HashMap<>();
      ConfigFileExport configFileExport = new ConfigFileExport(
          listExportByImport,
          I18n.getLanguage("mrUserCfgApprovedSmsBts.list.grid"),
          title,
          "",
          7,
          3,
          7,
          true,
          "language.mrUserCfgApprovedSmsBts.list.grid",
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
      configFileExport.setCustomColumnWidthNoMerge(
          new String[]{"1500", "5000", "5000", "5000", "7000", "3000", "7000", "10000"});
      fileExports.add(configFileExport);
    }

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

  @Override
  public ResultInSideDto importData(MultipartFile multipartFile) throws Exception {
    List<MrUserCfgApprovedSmsBtsDTO> mrUserCfgApprovedSmsBtsDTOS = new ArrayList<>();
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    mapAreaByMarketCode.clear();
    mapProvinceByAreaCode.clear();
    mapUserInSystem.clear();
    setMapUserName();
    lstCountry = mrUserCfgApprovedSmsBtsRepository.getLstCountryMap();
    Map<String, ItemDataCRInside> mapMarket = new HashMap<>();

    if (lstCountry != null) {
      for (ItemDataCRInside item : lstCountry) {
        mapMarket.put(item.getDisplayStr(), item);
      }
    }
    try {
      if (multipartFile == null || multipartFile.isEmpty()) {
        resultInSideDto.setKey(RESULT.FILE_IS_NULL);
        resultInSideDto.setMessage(I18n.getLanguage("common.fileEmpty"));
        return resultInSideDto;
      } else {
        List<MrUserCfgApprovedSmsBtsDTO> lstMarketAreaProvince = mrUserCfgApprovedSmsBtsRepository
            .getLstCountryAreaProvince();

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
            6,
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
            6,
            1000
        );
        if (dataImportList.size() > 1000) {
          resultInSideDto.setKey("ERROR_NO_DOWNLOAD");
          resultInSideDto.setMessage(
              I18n.getLanguage("mrUserCfgApprovedSmsBts.import.checkDataMaxrow.dataOver"));
          return resultInSideDto;
        }

        if (dataImportList != null && dataImportList.size() > 0) {
          //lay ra AreaCode có dung voi MarketCode ko?
          if (lstMarketAreaProvince != null && !lstMarketAreaProvince.isEmpty()) {
            for (MrUserCfgApprovedSmsBtsDTO dtoMap : lstMarketAreaProvince) {
              mapAreaByMarketCode
                  .put(dtoMap.getMarketCode() + "_" + dtoMap.getAreaCode(), dtoMap.getAreaCode());
            }
          }
          //lay ra ProvinceCode có dung voi AreaCode ko?
          if (lstMarketAreaProvince != null && !lstMarketAreaProvince.isEmpty()) {
            for (MrUserCfgApprovedSmsBtsDTO dtoMap : lstMarketAreaProvince) {
              mapProvinceByAreaCode.put(
                  dtoMap.getMarketCode() + "_" + dtoMap.getAreaCode() + "_" + dtoMap
                      .getProvinceCode(),
                  dtoMap.getProvinceCode());
            }
          }

          for (int i = dataImportList.size() - 1; i >= 0; i--) {
            if (dataImportList.get(i)[0] == null && dataImportList.get(i)[1] == null
                && dataImportList.get(i)[2] == null && dataImportList.get(i)[3] == null
                && dataImportList.get(i)[4] == null && dataImportList.get(i)[5] == null
                && dataImportList.get(i)[6] == null
            ) {
              dataImportList.remove(i);
            }
          }
          if (dataImportList.size() <= 1000) {
            int row = 8;
            int index = 0;
            for (Object[] obj : dataImportList) {
              MrUserCfgApprovedSmsBtsDTO dto = new MrUserCfgApprovedSmsBtsDTO();
              if (obj[1] != null) {
                dto.setMarketLocationCode(obj[1].toString().trim());
              } else {
                dto.setMarketLocationCode(null);
              }
              if (obj[2] != null) {
                dto.setAreaCode(obj[2].toString().trim());
              } else {
                dto.setAreaCode(null);
              }
              if (obj[3] != null) {
                dto.setProvinceCode(obj[3].toString().trim());
              } else {
                dto.setProvinceCode(null);
              }
              if (obj[4] != null) {
                dto.setUserName(obj[4].toString().trim());
              } else {
                dto.setUserName(null);
              }
//              if (obj[5] != null) {
//                dto.setMobile(obj[5].toString().trim());
//              } else {
//                dto.setMobile(null);
//              }
//              if (obj[6] != null) {
//                dto.setFullName(obj[6].toString().trim());
//              } else {
//                dto.setFullName(null);
//              }
              if (obj[5] != null) {
                dto.setApproveLevel(obj[5].toString().trim());
              } else {
                dto.setApproveLevel(null);
              }
              if (obj[6] != null) {
                dto.setReceiveMessageBD(obj[6].toString().trim());
              } else {
                dto.setReceiveMessageBD(null);
              }

              MrUserCfgApprovedSmsBtsDTO dtoValidate = validateImportInfo(
                  mrUserCfgApprovedSmsBtsDTOS, dto,
                  mapMarket, mapAreaByMarketCode, mapProvinceByAreaCode, lstMarketAreaProvince);
              if (StringUtils.isStringNullOrEmpty(dtoValidate.getResultImport())) {
                dtoValidate
                    .setResultImport(I18n.getLanguage("mrUserCfgApprovedSmsBts.result.import"));
                mrUserCfgApprovedSmsBtsDTOS.add(dtoValidate);
              } else {
                mrUserCfgApprovedSmsBtsDTOS.add(dtoValidate);
                index++;
              }
              row++;
            }
            if (index == 0) {
              if (!mrUserCfgApprovedSmsBtsDTOS.isEmpty()) {
                int k = 0;
                for (MrUserCfgApprovedSmsBtsDTO dtoTmp : mrUserCfgApprovedSmsBtsDTOS) {
                  resultInSideDto = insertOrUpdate(dtoTmp);
                  if (RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
                    if (I18n.getLanguage("mrUserCfgApprovedSmsBts.result.import")
                        .equals(dtoTmp.getResultImport())) {
                      dtoTmp.setResultImport(I18n.getLanguage("common.success1"));
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
            File fileExport = exportFileImport(mrUserCfgApprovedSmsBtsDTOS,
                Constants.RESULT_IMPORT);
            resultInSideDto.setFilePath(fileExport.getPath());
            resultInSideDto.setFile(fileExport);
          } else {
            resultInSideDto = new ResultInSideDto(null, RESULT.ERROR,
                I18n.getLanguage("common.importing.maxrow"));
          }
        } else {
          resultInSideDto.setKey(RESULT.NODATA);
          resultInSideDto.setMessage(I18n.getLanguage("common.searh.nodata"));
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
    if (count != 7) {
      return false;
    }
    if (objects[0] == null) {
      return false;
    }
    if (!I18n.getLanguage("mrUserCfgApprovedSmsBts.list.grid.stt")
        .equalsIgnoreCase(objects[0].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("mrUserCfgApprovedSmsBts.list.grid.marketCode") + "*")
        .equalsIgnoreCase(objects[1].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("mrUserCfgApprovedSmsBts.list.grid.areaCode") + "*")
        .equalsIgnoreCase(objects[2].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("mrUserCfgApprovedSmsBts.list.grid.provinceCode"))
        .equalsIgnoreCase(objects[3].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("mrUserCfgApprovedSmsBts.list.grid.userName") + "*")
        .equalsIgnoreCase(objects[4].toString().trim())) {
      return false;
    }
//    if (!(I18n.getLanguage("mrUserCfgApprovedSmsBts.list.grid.mobile"))
//        .equalsIgnoreCase(objects[5].toString().trim())) {
//      return false;
//    }
//    if (!(I18n.getLanguage("mrUserCfgApprovedSmsBts.list.grid.fullName"))
//        .equalsIgnoreCase(objects[6].toString().trim())) {
//      return false;
//    }
    if (!(I18n.getLanguage("mrUserCfgApprovedSmsBts.list.grid.approveLevel"))
        .equalsIgnoreCase(objects[5].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("mrUserCfgApprovedSmsBts.list.grid.receiveMessageBD") + "*")
        .equalsIgnoreCase(objects[6].toString().trim())) {
      return false;
    }
    return true;
  }

  @Override
  public File getFileTemplate() throws Exception {
    ExcelWriterUtils excelWriterUtils = new ExcelWriterUtils();
    String templatePathOut = "templates" + File.separator + "TEMPLATE_EXPORT.xlsx";
    templatePathOut = StringUtils.removeSeparator(templatePathOut);
    Resource resource = new ClassPathResource(templatePathOut);
    InputStream fileTemplate = resource.getInputStream();

    //apache POI XSSF
    XSSFWorkbook workbook = new XSSFWorkbook(fileTemplate);
    XSSFSheet sheetOne = workbook.getSheetAt(0);
    XSSFSheet sheetParam2 = workbook.createSheet("param2");
    //Tạo 1 mảng lưu header từng cột
    String[] header;
    //Tiêu đề đánh dấu *
    String[] headerStar;
    header = new String[]{
        I18n.getLanguage("mrUserCfgApprovedSmsBts.list.grid.stt"),
        I18n.getLanguage("mrUserCfgApprovedSmsBts.list.grid.marketCode"),
        I18n.getLanguage("mrUserCfgApprovedSmsBts.list.grid.areaCode"),
        I18n.getLanguage("mrUserCfgApprovedSmsBts.list.grid.provinceCode"),
        I18n.getLanguage("mrUserCfgApprovedSmsBts.list.grid.userName"),
//        I18n.getLanguage("mrUserCfgApprovedSmsBts.list.grid.mobile"),
//        I18n.getLanguage("mrUserCfgApprovedSmsBts.list.grid.fullName"),
        I18n.getLanguage("mrUserCfgApprovedSmsBts.list.grid.approveLevel"),
        I18n.getLanguage("mrUserCfgApprovedSmsBts.list.grid.receiveMessageBD")
    };

    headerStar = new String[]{
        I18n.getLanguage("mrUserCfgApprovedSmsBts.list.grid.marketCode"),
        I18n.getLanguage("mrUserCfgApprovedSmsBts.list.grid.areaCode"),
        I18n.getLanguage("mrUserCfgApprovedSmsBts.list.grid.userName"),
        I18n.getLanguage("mrUserCfgApprovedSmsBts.list.grid.receiveMessageBD")
//        I18n.getLanguage("mrUserCfgApprovedSmsBts.list.grid.mobile")
    };
    List<String> listHeader = Arrays.asList(header);
    List<String> listHeaderStar = Arrays.asList(headerStar);
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
    sheetOne.addMergedRegion(new CellRangeAddress(3, 3, 2, 8));
    Row titleRow = sheetOne.createRow(3);
    titleRow.setHeightInPoints(26.25f);
    Cell titleCell = titleRow.createCell(2);
    titleCell.setCellValue(I18n.getLanguage("mrUserCfgApprovedSmsBts.import.title"));
    titleCell.setCellStyle(styleTitle);

    XSSFFont starFont = workbook.createFont();
    starFont.setColor(IndexedColors.RED.getIndex());

    //Tạo Header
    Row headerRow = sheetOne.createRow(7);
    Row headerArray = sheetParam2.createRow(0);
    headerRow.setHeightInPoints(22.5f);
    sheetOne.setColumnWidth(7, 7000);
    for (int i = 0; i < listHeader.size(); i++) {
      Cell headerCell = headerRow.createCell(i);
      XSSFRichTextString richTextString = new XSSFRichTextString(listHeader.get(i));
      for (String headerCheck : listHeaderStar) {
        if (headerCheck.equalsIgnoreCase(listHeader.get(i))) {
          richTextString.append("*", starFont);
        }
      }

      //Tạo comment cho Header
      if (i == 5) {
        Drawing drawing = headerCell.getSheet().createDrawingPatriarch();
        CreationHelper factory = workbook.getCreationHelper();
        ClientAnchor anchor = factory.createClientAnchor();
        anchor.setCol1(i + 1);
        anchor.setCol2(i + 3);
        anchor.setRow1(3);
        anchor.setRow2(5);
        anchor.setDx1(5000);
        anchor.setDx2(5000);
        anchor.setDy1(5000);
        anchor.setDy2(5000);
        Comment comment = drawing.createCellComment(anchor);
        comment.setString(factory.createRichTextString(
            "Nhập 1, 2 hoặc 3 trong đó (1: Cấp 1) hoặc (2: Cấp 2) hoặc (3: Cả 2 cấp)"));
        headerCell.setCellComment(comment);
      }

      if (i == 6) {
        Drawing drawing = headerCell.getSheet().createDrawingPatriarch();
        CreationHelper factory = workbook.getCreationHelper();
        ClientAnchor anchor = factory.createClientAnchor();
        anchor.setCol1(i + 1);
        anchor.setCol2(i + 3);
        anchor.setRow1(3);
        anchor.setRow2(5);
        anchor.setDx1(5000);
        anchor.setDx2(5000);
        anchor.setDy1(5000);
        anchor.setDy2(5000);
        Comment comment2 = drawing.createCellComment(anchor);
        comment2.setString(
            factory.createRichTextString("Nhập 0 hoặc 1 trong đó (0: Không) hoặc (1: Có)"));
        headerCell.setCellComment(comment2);
      }

      headerCell.setCellValue(richTextString);
      headerCell.setCellStyle(cellStyleHeader);
      sheetOne.setColumnWidth(i, 7000);
    }

    Cell headerCellMarketCode = headerArray.createCell(0);
    Cell headerCellMarketName = headerArray.createCell(1);
    Cell headerCellAreaCode = headerArray.createCell(2);
    Cell headerCellAreaName = headerArray.createCell(3);
    Cell headerCellProvinceCode = headerArray.createCell(4);
    Cell headerCellProvinceName = headerArray.createCell(5);

    XSSFRichTextString marketCode = new XSSFRichTextString(
        I18n.getLanguage("mrUserCfgApprovedSmsBts.list.template.marketCode"));
    XSSFRichTextString marketName = new XSSFRichTextString(
        I18n.getLanguage("mrUserCfgApprovedSmsBts.list.template.marketName"));

    XSSFRichTextString areaCode = new XSSFRichTextString(
        I18n.getLanguage("mrUserCfgApprovedSmsBts.list.template.areaCode"));
    XSSFRichTextString areaName = new XSSFRichTextString(
        I18n.getLanguage("mrUserCfgApprovedSmsBts.list.template.areaName"));

    XSSFRichTextString provinceCode = new XSSFRichTextString(
        I18n.getLanguage("mrUserCfgApprovedSmsBts.list.template.provinceCode"));
    XSSFRichTextString provinceName = new XSSFRichTextString(
        I18n.getLanguage("mrUserCfgApprovedSmsBts.list.template.provinceName"));

    //cột quốc gia
    headerCellMarketCode.setCellValue(marketCode);
    headerCellMarketCode.setCellStyle(style.get("header"));
    headerCellMarketName.setCellValue(marketName);
    headerCellMarketName.setCellStyle(style.get("header"));

    //cột khu vực
    headerCellAreaCode.setCellValue(areaCode);
    headerCellAreaCode.setCellStyle(style.get("header"));
    headerCellAreaName.setCellValue(areaName);
    headerCellAreaName.setCellStyle(style.get("header"));

    //cột tỉnh
    headerCellProvinceCode.setCellValue(provinceCode);
    headerCellProvinceCode.setCellStyle(style.get("header"));
    headerCellProvinceName.setCellValue(provinceName);
    headerCellProvinceName.setCellStyle(style.get("header"));

    sheetParam2.setColumnWidth(0, 7000);
    sheetParam2.setColumnWidth(1, 7000);
    sheetParam2.setColumnWidth(2, 7000);
    sheetParam2.setColumnWidth(3, 7000);
    sheetParam2.setColumnWidth(4, 7000);
    sheetParam2.setColumnWidth(5, 7000);
    sheetOne.setColumnWidth(0, 3000);

    Map<String, String> mapArrCodeEN = new HashMap<>();
    int row = 1;
    List<MrUserCfgApprovedSmsBtsDTO> lstANT = mrUserCfgApprovedSmsBtsRepository
        .getLstCountryAreaProvince();

//    List<MrDeviceDTO> lstANT = mrDeviceRepository.getMrDeviceByA_N_T();
    Map<String, String> mapAN = new HashMap<>();
    Map<String, String> mapANT = new HashMap<>();

    if (lstANT != null && !lstANT.isEmpty()) {
      //Nếu Khu vực = Công ty
      if (row == 1) {
        excelWriterUtils
            .createCell(sheetParam2, 0, row, "", style.get("cell"));
        excelWriterUtils
            .createCell(sheetParam2, 1, row, "", style.get("cell"));
        excelWriterUtils
            .createCell(sheetParam2, 2, row, "CT", style.get("cell"));
        excelWriterUtils
            .createCell(sheetParam2, 3, row,
                I18n.getLanguage("mrUserCfgApprovedSmsBts.areaCode.isCompany"), style.get("cell"));
        excelWriterUtils
            .createCell(sheetParam2, 4, row, "", style.get("cell"));
        excelWriterUtils
            .createCell(sheetParam2, 5, row, "", style.get("cell"));
        row++; // them ngan cach
      }

      for (MrUserCfgApprovedSmsBtsDTO item : lstANT) {
        if (!mapAN.containsKey(item.getMarketCode())) {
          if (row > 1) {
            excelWriterUtils
                .createCell(sheetParam2, 0, row, "", cellStyleHeader);
            excelWriterUtils
                .createCell(sheetParam2, 1, row, "", cellStyleHeader);
            excelWriterUtils
                .createCell(sheetParam2, 2, row, "", cellStyleHeader);
            excelWriterUtils
                .createCell(sheetParam2, 3, row, "", cellStyleHeader);
            excelWriterUtils
                .createCell(sheetParam2, 4, row, "", cellStyleHeader);
            excelWriterUtils
                .createCell(sheetParam2, 5, row, "", cellStyleHeader);
            row++; // them ngan cach
          }
          String valueDefault = item.getMarketCode();
          if (mapArrCodeEN.containsKey(valueDefault)) {
            valueDefault = mapArrCodeEN.get(valueDefault);
          }
          excelWriterUtils
              .createCell(sheetParam2, 0, row, item.getMarketLocationCode(), style.get("cell"));
          excelWriterUtils
              .createCell(sheetParam2, 1, row, item.getMarketName(), style.get("cell"));
        }
        if (!mapANT.containsKey(item.getMarketCode() + item.getAreaCode())) {
          excelWriterUtils
              .createCell(sheetParam2, 2, row, item.getAreaCode(), style.get("cell"));
          excelWriterUtils
              .createCell(sheetParam2, 3, row, item.getAreaName(), style.get("cell"));
        }
        excelWriterUtils
            .createCell(sheetParam2, 4, row, item.getProvinceCode(), style.get("cell"));
        excelWriterUtils
            .createCell(sheetParam2, 5, row++, item.getProvinceName(), style.get("cell"));
        mapAN.put(item.getMarketCode(), item.getAreaCode());
        mapANT.put(item.getMarketCode() + item.getAreaCode(), item.getProvinceCode());
      }
    }

    sheetParam2.autoSizeColumn(0);
    sheetParam2.autoSizeColumn(1);
    sheetParam2.autoSizeColumn(2);
    sheetParam2.autoSizeColumn(3);
    sheetParam2.autoSizeColumn(4);
    sheetParam2.autoSizeColumn(5);
    //set tên trang excel

    workbook.setSheetName(0, I18n.getLanguage("mrUserCfgApprovedSmsBts.export.sheetname"));
    workbook.setSheetName(1, I18n.getLanguage("mrUserCfgApprovedSmsBts.export.sheetname1"));

    //set tên file excel
    String fileResult = tempFolder + File.separator;
    String fileName =
        "IMPORT_MR_USER_CFG_APPROVE_SMS_BTS" + "_" + System.currentTimeMillis() + ".xlsx";
    excelWriterUtils.saveToFileExcel(workbook, fileResult, fileName);
    String resultPath = fileResult + fileName;
    File fileExport = new File(resultPath);
    return fileExport;
  }

  public MrUserCfgApprovedSmsBtsDTO validateImportInfo(List<MrUserCfgApprovedSmsBtsDTO> listDTO,
      MrUserCfgApprovedSmsBtsDTO mrUserCfgApprovedSmsBtsDTO,
      Map<String, ItemDataCRInside> mapMarket, Map<String, String> mapAreaByMarketCode,
      Map<String, String> mapProvinceByAreaCode,
      List<MrUserCfgApprovedSmsBtsDTO> lstMarketAreaProvince) {
    boolean check = true;
//    List<MrUserCfgApprovedSmsBtsDTO> lstMarketAreaProvince = mrUserCfgApprovedSmsBtsRepository.getLstCountryAreaProvince();
    //Quốc gia
    if (StringUtils.isStringNullOrEmpty(mrUserCfgApprovedSmsBtsDTO.getMarketLocationCode())) {
      mrUserCfgApprovedSmsBtsDTO.setResultImport(
          I18n.getLanguage("mrUserCfgApprovedSmsBts.list.template.marketCode") + " " + I18n
              .getLanguage("common.notnull"));
      return mrUserCfgApprovedSmsBtsDTO;
    } else if (!mapMarket.containsKey(mrUserCfgApprovedSmsBtsDTO.getMarketLocationCode())) {
      mrUserCfgApprovedSmsBtsDTO.setResultImport(
          I18n.getLanguage("mrUserCfgApprovedSmsBts.list.template.marketCode") + " " + I18n
              .getLanguage("notify.invalid"));
      return mrUserCfgApprovedSmsBtsDTO;
    }else if(mapMarket.containsKey(mrUserCfgApprovedSmsBtsDTO.getMarketLocationCode())){
      ItemDataCRInside dtoTmp = mapMarket.get(mrUserCfgApprovedSmsBtsDTO.getMarketLocationCode());
      mrUserCfgApprovedSmsBtsDTO.setMarketCode(String.valueOf(dtoTmp.getValueStr()));
    }

    //Khu vực
    if (StringUtils.isStringNullOrEmpty(mrUserCfgApprovedSmsBtsDTO.getAreaCode())) {
      mrUserCfgApprovedSmsBtsDTO.setResultImport(
          I18n.getLanguage("mrUserCfgApprovedSmsBts.list.template.areaCode") + " " + I18n
              .getLanguage("common.notnull"));
      return mrUserCfgApprovedSmsBtsDTO;
    } else {
      check = false;
      if ("CT".equals(mrUserCfgApprovedSmsBtsDTO.getAreaCode())) {
        check = true;
      } else {
        for (MrUserCfgApprovedSmsBtsDTO dtoMapArea : lstMarketAreaProvince) {
          if (mrUserCfgApprovedSmsBtsDTO.getAreaCode().equals(dtoMapArea.getAreaCode())) {
            check = true;
            break;
          }
        }
      }
      if (!check) {
        mrUserCfgApprovedSmsBtsDTO.setResultImport(
            I18n.getLanguage("mrUserCfgApprovedSmsBts.list.template.areaCode") + " " + I18n
                .getLanguage("mrUserCfgApprovedSmsBts.dataImport.notexist"));
        return mrUserCfgApprovedSmsBtsDTO;
      }
      //so sánh AreaCode nhap vao co dung voi MarketCode hay ko?
      if (check == true) {
        if (!"CT".equals(mrUserCfgApprovedSmsBtsDTO.getAreaCode())
            && !mrUserCfgApprovedSmsBtsDTO.getAreaCode().equals(mapAreaByMarketCode.get(
            mrUserCfgApprovedSmsBtsDTO.getMarketCode() + "_" + mrUserCfgApprovedSmsBtsDTO
                .getAreaCode()))) {
          mrUserCfgApprovedSmsBtsDTO.setResultImport(
              I18n.getLanguage("mrUserCfgApprovedSmsBts.list.template.areaCode") + " " + I18n
                  .getLanguage("mrUserCfgApprovedSmsBts.areaCode.falseMarketCode"));
          return mrUserCfgApprovedSmsBtsDTO;
        }
      }
    }

    //Tỉnh
    if (!StringUtils.isStringNullOrEmpty(mrUserCfgApprovedSmsBtsDTO.getProvinceCode())) {
//      mrUserCfgApprovedSmsBtsDTO.setResultImport(
//          I18n.getLanguage("mrUserCfgApprovedSmsBts.list.template.provinceCode") + " " + I18n
//              .getLanguage("common.notnull"));
//      return mrUserCfgApprovedSmsBtsDTO;
      check = false;
      for (MrUserCfgApprovedSmsBtsDTO dtoMapProvince : lstMarketAreaProvince) {
        if (mrUserCfgApprovedSmsBtsDTO.getProvinceCode().equals(dtoMapProvince.getProvinceCode())) {
          check = true;
          break;
        }
      }
      if (!check) {
        mrUserCfgApprovedSmsBtsDTO.setResultImport(
            I18n.getLanguage("mrUserCfgApprovedSmsBts.list.template.provinceCode") + " " + I18n
                .getLanguage("mrUserCfgApprovedSmsBts.dataImport.notexist"));
        return mrUserCfgApprovedSmsBtsDTO;
      }
      //so sánh ProvinceCode nhap vao co dung voi AreaCode hay ko?
      if (check == true) {
        if (!mrUserCfgApprovedSmsBtsDTO.getProvinceCode()
            .equals(mapProvinceByAreaCode.get(
                mrUserCfgApprovedSmsBtsDTO.getMarketCode() + "_" + mrUserCfgApprovedSmsBtsDTO
                    .getAreaCode() + "_" + mrUserCfgApprovedSmsBtsDTO.getProvinceCode()))) {
          mrUserCfgApprovedSmsBtsDTO.setResultImport(
              I18n.getLanguage("mrUserCfgApprovedSmsBts.list.template.provinceCode") + " " + I18n
                  .getLanguage("mrUserCfgApprovedSmsBts.provinceCode.falseAreaCode"));
          return mrUserCfgApprovedSmsBtsDTO;
        }
      }
    }

    //Account/UserName
    if (StringUtils.isStringNullOrEmpty(mrUserCfgApprovedSmsBtsDTO.getUserName())) {
      mrUserCfgApprovedSmsBtsDTO.setResultImport(
          I18n.getLanguage("mrUserCfgApprovedSmsBts.list.grid.userName") + " " + I18n
              .getLanguage("common.notnull"));
      return mrUserCfgApprovedSmsBtsDTO;
    } else if (!StringUtils.isStringNullOrEmpty(mrUserCfgApprovedSmsBtsDTO.getUserName())) {
      //check User da ton tai ben bang cau hinh hay chua??
      MrUserCfgApprovedSmsBtsDTO userExisted = mrUserCfgApprovedSmsBtsRepository
          .getApproveLevelByUserLogin(mrUserCfgApprovedSmsBtsDTO.getUserName());
      if (userExisted == null) {
        //check account import có tồn tại trong bảng User hay ko.
        //Neu ton tai thi lay ra fullName cho ban ghi do
        MrUserCfgApprovedSmsBtsDTO dtoUserSystemOK = mapUserInSystem
            .get(mrUserCfgApprovedSmsBtsDTO.getUserName());
        if (dtoUserSystemOK == null) {
          mrUserCfgApprovedSmsBtsDTO.setResultImport(
              I18n.getLanguage("mrUserCfgApprovedSmsBts.list.grid.userName") + " " + I18n
                  .getLanguage("mrUserCfgApprovedSmsBts.import.checkDataUser.inSystem"));
          return mrUserCfgApprovedSmsBtsDTO;
        } else {
          if (mrUserCfgApprovedSmsBtsDTO.getUserName()
              .equalsIgnoreCase(dtoUserSystemOK.getUserName())) {
            //set lại giá trị FullName, Mobile cho User nếu hợp lệ.
            mrUserCfgApprovedSmsBtsDTO.setFullName(dtoUserSystemOK.getFullName());
            mrUserCfgApprovedSmsBtsDTO.setMobile(dtoUserSystemOK.getMobile());
          }
        }
      } else if (userExisted != null) {
        mrUserCfgApprovedSmsBtsDTO.setResultImport(
            I18n.getLanguage("mrUserCfgApprovedSmsBts.list.grid.userName") + " " + I18n
                .getLanguage("mrUserCfgApprovedSmsBts.insertOrUpdate.existed"));
        return mrUserCfgApprovedSmsBtsDTO;
      }
    }

//    if (!StringUtils.isStringNullOrEmpty(mrUserCfgApprovedSmsBtsDTO.getMobile())) {
//      if (!mrUserCfgApprovedSmsBtsDTO.getMobile().matches(REGEX_NUMBER_MOBILE)) {
//        mrUserCfgApprovedSmsBtsDTO.setResultImport(
//            I18n.getLanguage("mrUserCfgApprovedSmsBts.list.grid.mobile") + " " + I18n
//                .getLanguage("mrUserCfgApprovedSmsBts.import.checkData.number"));
//        return mrUserCfgApprovedSmsBtsDTO;
//      } else if (mrUserCfgApprovedSmsBtsDTO.getMobile().matches(REGEX_NUMBER_MOBILE)) {
//        if (mrUserCfgApprovedSmsBtsDTO.getMobile().length() > 15) {
//          mrUserCfgApprovedSmsBtsDTO.setResultImport(
//              I18n.getLanguage("mrUserCfgApprovedSmsBts.list.grid.mobile") + " " + I18n
//                  .getLanguage("mrUserCfgApprovedSmsBts.import.checkLength.mobile"));
//          return mrUserCfgApprovedSmsBtsDTO;
//        }
//      }
//    }

    //Cấp phê duyệt
    if (!StringUtils.isStringNullOrEmpty(mrUserCfgApprovedSmsBtsDTO.getApproveLevel())) {
      if (!mrUserCfgApprovedSmsBtsDTO.getApproveLevel().matches(REGEX_NUMBER)) {
        mrUserCfgApprovedSmsBtsDTO.setResultImport(
            I18n.getLanguage("mrUserCfgApprovedSmsBts.list.grid.approveLevel") + " " + I18n
                .getLanguage("mrUserCfgApprovedSmsBts.import.checkData.number"));
        return mrUserCfgApprovedSmsBtsDTO;
      } else if (mrUserCfgApprovedSmsBtsDTO.getApproveLevel().matches(REGEX_NUMBER)) {
        if (Long.valueOf(mrUserCfgApprovedSmsBtsDTO.getApproveLevel()) != 1
            && Long.valueOf(mrUserCfgApprovedSmsBtsDTO.getApproveLevel()) != 2
            && Long.valueOf(mrUserCfgApprovedSmsBtsDTO.getApproveLevel()) != 3) {
          mrUserCfgApprovedSmsBtsDTO.setResultImport(
              I18n.getLanguage("mrUserCfgApprovedSmsBts.list.grid.approveLevel") + " " + I18n
                  .getLanguage("mrUserCfgApprovedSmsBts.import.checkData.approveLevel"));
          return mrUserCfgApprovedSmsBtsDTO;
        }
      }
    }

    //Nhận tin nhắn bảo dưỡng
    if (StringUtils.isStringNullOrEmpty(mrUserCfgApprovedSmsBtsDTO.getReceiveMessageBD())) {
      mrUserCfgApprovedSmsBtsDTO.setResultImport(
          I18n.getLanguage("mrUserCfgApprovedSmsBts.list.grid.receiveMessageBD") + " " + I18n
              .getLanguage("common.notnull"));
      return mrUserCfgApprovedSmsBtsDTO;
    } else if (!mrUserCfgApprovedSmsBtsDTO.getReceiveMessageBD().matches(REGEX_NUMBER)) {
      mrUserCfgApprovedSmsBtsDTO.setResultImport(
          I18n.getLanguage("mrUserCfgApprovedSmsBts.list.grid.receiveMessageBD") + " " + I18n
              .getLanguage("mrUserCfgApprovedSmsBts.import.checkData.number"));
      return mrUserCfgApprovedSmsBtsDTO;
    } else if (mrUserCfgApprovedSmsBtsDTO.getReceiveMessageBD().matches(REGEX_NUMBER)) {
      if (Long.valueOf(mrUserCfgApprovedSmsBtsDTO.getReceiveMessageBD()) != 0
          && Long.valueOf(mrUserCfgApprovedSmsBtsDTO.getReceiveMessageBD()) != 1) {
        mrUserCfgApprovedSmsBtsDTO.setResultImport(
            I18n.getLanguage("mrUserCfgApprovedSmsBts.list.grid.receiveMessageBD") + " " + I18n
                .getLanguage("mrUserCfgApprovedSmsBts.import.checkData.receiveMessageBD"));
        return mrUserCfgApprovedSmsBtsDTO;
      }
    }

    // validate bản ghi bị trùng trong File
    String msg = validateDuplicate(mrUserCfgApprovedSmsBtsDTO, listDTO);
    if (StringUtils.isNotNullOrEmpty(msg)) {
      mrUserCfgApprovedSmsBtsDTO.setResultImport(msg);
      return mrUserCfgApprovedSmsBtsDTO;
    }
    if (StringUtils.isNotNullOrEmpty(mrUserCfgApprovedSmsBtsDTO.getUserName())) {
      List<MrUserCfgApprovedSmsBtsDTO> dtoTmpList = mrUserCfgApprovedSmsBtsRepository
          .checkExisted(mrUserCfgApprovedSmsBtsDTO);
      if (dtoTmpList != null && !dtoTmpList.isEmpty()) {
        mrUserCfgApprovedSmsBtsDTO
            .setResultImport(I18n.getLanguage("mrUserCfgApprovedSmsBts.insertOrUpdate.existed"));
      }
    }
    return mrUserCfgApprovedSmsBtsDTO;
  }

  public String validateDuplicate(
      MrUserCfgApprovedSmsBtsDTO mrUserCfgApprovedSmsBtsDTO,
      List<MrUserCfgApprovedSmsBtsDTO> listDto) {
    String msg = "";
    if (listDto != null && !listDto.isEmpty()) {
      for (int i = 0; i < listDto.size(); i++) {
        MrUserCfgApprovedSmsBtsDTO dtoCheck = listDto.get(i);
        if (dtoCheck.getMarketCode().equals(mrUserCfgApprovedSmsBtsDTO.getMarketCode())
            && dtoCheck.getAreaCode().equals(mrUserCfgApprovedSmsBtsDTO.getAreaCode())
            && dtoCheck.getUserName().equals(mrUserCfgApprovedSmsBtsDTO.getUserName())
            && dtoCheck.getReceiveMessageBD()
            .equals(mrUserCfgApprovedSmsBtsDTO.getReceiveMessageBD())) {
          msg = I18n.getLanguage("mrUserCfgApprovedSmsBts.err.dup-code-in-file")
              .replaceAll("0", String.valueOf((i) + 1));
          break;
        }
      }
    }
    return msg;
  }

  public void setMapCountryName() {
    List<ItemDataCRInside> lstCountryName = catLocationBusiness
        .getListLocationByLevelCBB(null, 1L, null);
    if (lstCountryName != null && !lstCountryName.isEmpty()) {
      for (ItemDataCRInside itemDataCR : lstCountryName) {
        mapMarketName.put(String.valueOf(itemDataCR.getValueStr()), itemDataCR.getDisplayStr());
      }
    }
  }


  public void setMapUserName() {
    List<MrUserCfgApprovedSmsBtsDTO> lstUserInSystem = mrUserCfgApprovedSmsBtsRepository
        .getListUserInSystem();
    if (lstUserInSystem != null && !lstUserInSystem.isEmpty()) {
      for (MrUserCfgApprovedSmsBtsDTO userInSystem : lstUserInSystem) {
        mapUserInSystem.put(userInSystem.getUserName(), userInSystem);
      }
    }
  }


  private void setMapProvinceName(String provinceCode) {
    List<CatLocationDTO> dataList = mrUserCfgApprovedSmsBtsRepository
        .getLstProvinceNamebyCode(provinceCode);
    if (dataList != null && !dataList.isEmpty()) {
      for (CatLocationDTO catLocationDTO : dataList) {
        mapProvinceName.put(String.valueOf(catLocationDTO.getLocationCode()),
            catLocationDTO.getLocationName());
        mapProvinceId
            .put(String.valueOf(catLocationDTO.getLocationCode()), catLocationDTO.getLocationId());
      }
    }

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
