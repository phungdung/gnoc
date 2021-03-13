package com.viettel.gnoc.sr.business;

import com.viettel.gnoc.commons.business.CatLocationBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.LogChangeConfigDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.proxy.CommonStreamServiceProxy;
import com.viettel.gnoc.commons.utils.CellConfigExport;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.ConfigFileExport;
import com.viettel.gnoc.commons.utils.ConfigHeaderExport;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.Constants.SR_CATALOG;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.DateUtil;
import com.viettel.gnoc.commons.utils.ExcelWriterUtils;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.sr.dto.SRCatalogDTO;
import com.viettel.gnoc.sr.dto.SRConfigDTO;
import com.viettel.gnoc.sr.dto.SearchConfigServiceDTO;
import com.viettel.gnoc.sr.repository.SRCatalogRepository;
import com.viettel.gnoc.sr.repository.SRServiceManageRepository;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
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
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.streaming.SXSSFDrawing;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFComment;
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
public class SRServiceManageBusinessImpl implements SRServiceManageBusiness {

  @Value("${application.temp.folder}")
  private String tempFolder;

  private final static String SR_SERVICE_ARRAY_EXPORT = "Sr_Service_Array_EXPORT";
  private final static String SR_SERVICE_GROUP_EXPORT = "Sr_Service_Group_EXPORT";

  @Autowired
  protected SRServiceManageRepository srServiceManageRepository;

  @Autowired
  protected SRCatalogRepository srCatalogRepository;

  @Autowired
  TicketProvider ticketProvider;

  @Autowired
  protected CatLocationBusiness catLocationBusiness;

  @Autowired
  protected SRCatalogBusiness srCatalogBusiness;

  @Autowired
  protected CommonStreamServiceProxy commonStreamServiceProxy;

  public static final String XLSX_FILE_EXTENTION = ".xlsx";
  public static final String DOC_FILE_EXTENTION = ".doc";
  public static final String DOCX_FILE_EXTENTION = ".docx";
  public static final String XLSM_FILE_EXTENTION = ".xlsm";
  public static final String PDF_FILE_EXTENTION = ".pdf";

  Map<String, String> mapCountryName = new HashMap<>();
  Map<String, SRConfigDTO> mapAutomation = new HashMap<>();
  private Map<String, String> mapAddObject = new HashMap<>();
  private Map<String, String> mapAddObject2 = new HashMap<>();
  Map<String, SRConfigDTO> mapParentCode = new HashMap<>();

  public void setMapCountryName() {
    List<ItemDataCRInside> lstCountryName = catLocationBusiness
        .getListLocationByLevelCBB(null, 1L, null);
    if (lstCountryName != null && !lstCountryName.isEmpty()) {
      for (ItemDataCRInside itemDataCR : lstCountryName) {
        mapCountryName.put(String.valueOf(itemDataCR.getValueStr()), itemDataCR.getDisplayStr());
      }
    }
  }

  public void setMapAutomation() {
    List<SRConfigDTO> lstConfig = srServiceManageRepository
        .getByConfigGroup(Constants.SR_CONFIG.AUTO_MATION);
    if (lstConfig != null && !lstConfig.isEmpty()) {
      for (SRConfigDTO itemDataCR : lstConfig) {
        mapAutomation.put(String.valueOf(itemDataCR.getConfigCode()), itemDataCR);
      }
    }
  }

  public void setMapParentCode() {
    List<SRConfigDTO> lstConfig = srServiceManageRepository
        .getByConfigGroup(SR_CATALOG.SERVICE_ARRAY);
    if (lstConfig != null && !lstConfig.isEmpty()) {
      for (SRConfigDTO itemDataCR : lstConfig) {
        mapParentCode.put(String.valueOf(itemDataCR.getConfigCode()), itemDataCR);
      }
    }
  }

  @Override
  public Datatable getListSearchSRServiceArray(SRConfigDTO srConfigDTO) {
    setMapCountryName();
    Datatable datatable = srServiceManageRepository.getListSearchSRServiceArray(srConfigDTO);

    List<SRConfigDTO> list = (List<SRConfigDTO>) datatable.getData();
    for (SRConfigDTO dto : list) {
      dto.setBtnDelete(true);
      List<SRCatalogDTO> lst;
      SRCatalogDTO catalogDTO = new SRCatalogDTO();
      catalogDTO.setServiceArray(dto.getConfigCode());
      lst = srCatalogRepository.getListSRCatalogDTO(catalogDTO);

      if (lst != null && !lst.isEmpty()) {
        dto.setBtnDelete(false);
      }

      List<SRConfigDTO> lstConfig;
      SRConfigDTO configDTO = new SRConfigDTO();
      configDTO.setConfigGroup(Constants.SR_CATALOG.SERVICE_GROUP);
      configDTO.setConfigCode(dto.getConfigCode());
      lstConfig = srServiceManageRepository.checkEnableGroupNotConvert(configDTO);
      if (lstConfig != null && !lstConfig.isEmpty()) {
        dto.setBtnDelete(false);
      }

      if (mapCountryName != null && mapCountryName.size() > 0) {
        dto.setCountryDisplay(mapCountryName.get(dto.getCountry()));
      }
    }

    datatable.setData(list);
    return datatable;
  }

  @Override
  public ResultInSideDto insertOrUpdateServiceArray(SRConfigDTO srConfigDTO) {

    ResultInSideDto resultInSideDto = new ResultInSideDto();

    UserToken userToken = ticketProvider.getUserToken();

    srConfigDTO.setConfigCode(srConfigDTO.getConfigCode().trim());
    srConfigDTO.setConfigName(srConfigDTO.getConfigName().trim());
    srConfigDTO.setCreatedUser(userToken.getUserName());
    srConfigDTO.setCreatedTime(DateTimeUtils.date2ddMMyyyyHHMMss(new Date()));
    srConfigDTO.setUpdatedTime(DateTimeUtils.date2ddMMyyyyHHMMss(new Date()));
    srConfigDTO.setUpdatedUser(userToken.getUserName());
    srConfigDTO.setConfigGroup(Constants.SR_CATALOG.SERVICE_ARRAY);
    srConfigDTO.setStatus(Constants.SR_CATALOG.SERVICE_STATUS);

    if (StringUtils.isStringNullOrEmpty(srConfigDTO.getConfigId())
        || srConfigDTO.getConfigId() == 0) {
      boolean checkCode = srServiceManageRepository.checkSrConfigExistedByCode(srConfigDTO);
      boolean checkName = srServiceManageRepository.checkSrConfigExistedByName(srConfigDTO);

      if ((checkCode == true && checkName == false) || (checkCode == false && checkName == true)) {
        resultInSideDto.setKey(RESULT.DUPLICATE);
        resultInSideDto
            .setMessage(I18n.getValidation("SrServiceArray.isDuplicate.configCodeAndName"));
        return resultInSideDto;
      }

      if (srServiceManageRepository.checkSrConfigExisted(srConfigDTO)) {
        SRConfigDTO dtoUpdate = srServiceManageRepository.getSRServiceArrayByDTO(srConfigDTO);
        if (dtoUpdate != null) {
          srConfigDTO.setConfigId(dtoUpdate.getConfigId());
          if (srConfigDTO.getConfigCode().equalsIgnoreCase(dtoUpdate.getConfigCode()) && srConfigDTO
              .getConfigName().equalsIgnoreCase(dtoUpdate.getConfigName())) {
            if ("I".equals(dtoUpdate.getStatus())) {
              dtoUpdate.setCreatedUser(userToken.getUserName());
              dtoUpdate.setCreatedTime(DateTimeUtils.date2ddMMyyyyHHMMss(new Date()));
              dtoUpdate.setUpdatedTime(DateTimeUtils.date2ddMMyyyyHHMMss(new Date()));
              dtoUpdate.setUpdatedUser(userToken.getUserName());
              dtoUpdate.setConfigGroup(Constants.SR_CATALOG.SERVICE_ARRAY);
              dtoUpdate.setStatus(Constants.SR_CATALOG.SERVICE_STATUS);
              return srServiceManageRepository.insertOrUpdateService(dtoUpdate);
            } else {
              resultInSideDto.setKey(RESULT.DUPLICATE);
              resultInSideDto
                  .setMessage(I18n.getValidation("SrServiceArray.isDuplicate.configCodeAndName"));
              return resultInSideDto;
            }
          } else {
            resultInSideDto.setKey(RESULT.DUPLICATE);
            resultInSideDto
                .setMessage(I18n.getValidation("SrServiceArray.isDuplicate.configCodeAndName"));
            return resultInSideDto;
          }
        }
      } else {
        return srServiceManageRepository.insertOrUpdateService(srConfigDTO);
      }
    } else {
      SRConfigDTO dtoUpdate = srServiceManageRepository
          .getSRServiceArrayDetail(srConfigDTO.getConfigId());
      if (dtoUpdate != null) {

        if (!dtoUpdate.getConfigName().equalsIgnoreCase(srConfigDTO.getConfigName())) {
          boolean check = srServiceManageRepository.checkSrConfigExistedByName(srConfigDTO);
          if (check) {
            resultInSideDto.setKey(RESULT.DUPLICATE);
            resultInSideDto.setMessage(I18n.getValidation("SrServiceArray.configName.isExist"));
            return resultInSideDto;
          }
        } else {
          List<SRConfigDTO> list = srServiceManageRepository.getSrConfigExistedByName(srConfigDTO);
          if (list != null && list.size() > 1) {
            resultInSideDto.setKey(RESULT.DUPLICATE);
            resultInSideDto.setMessage(I18n.getValidation("SrServiceArray.configName.isExist"));
            return resultInSideDto;
          }
        }

        dtoUpdate.setCountry(srConfigDTO.getCountry());
        dtoUpdate.setConfigName(srConfigDTO.getConfigName());
        dtoUpdate.setAutomation(srConfigDTO.getAutomation());
        dtoUpdate.setCreatedUser(userToken.getUserName());
        dtoUpdate.setCreatedTime(DateTimeUtils.date2ddMMyyyyHHMMss(new Date()));
        dtoUpdate.setUpdatedTime(DateTimeUtils.date2ddMMyyyyHHMMss(new Date()));
        dtoUpdate.setUpdatedUser(userToken.getUserName());
        dtoUpdate.setConfigGroup(Constants.SR_CATALOG.SERVICE_ARRAY);
        if ("Active".equals(dtoUpdate.getStatus())) {
          dtoUpdate.setStatus("A");
        } else if ("Inactive".equals(dtoUpdate.getStatus())) {
          dtoUpdate.setStatus("I");
        }
        resultInSideDto = srServiceManageRepository.insertOrUpdateService(dtoUpdate);
      }
    }

    if (resultInSideDto.getKey().equals(Constants.RESULT.SUCCESS)) {
      String content = "";
      if (StringUtils.isStringNullOrEmpty(srConfigDTO.getConfigId())
          || srConfigDTO.getConfigId() == 0) {
        content = "Insert arrayService";
      } else {
        content = "Update arrayService";
      }
      commonStreamServiceProxy.insertLog(new LogChangeConfigDTO(userToken.getUserName(),
          content, content + resultInSideDto.getId(),
          srConfigDTO, null));
    }

    return resultInSideDto;
  }

  @Override
  public File exportDataServiceArray(SRConfigDTO srConfigDTO) throws Exception {
    mapCountryName.clear();
    setMapCountryName();
    List<SRConfigDTO> data =
        srServiceManageRepository
            .getListDataExportServiceArray(srConfigDTO);
    if (data != null) {
      for (SRConfigDTO dto : data) {
        if (mapCountryName != null && mapCountryName.size() > 0) {
          dto.setCountryDisplay(mapCountryName.get(dto.getCountry()));
        }
        if ("A".equals(dto.getStatus())) {
          dto.setStatus("Active");
        } else if ("I".equals(dto.getStatus())) {
          dto.setStatus("Inactive");
        }
      }
    }
    UserToken userToken = ticketProvider.getUserToken();
    commonStreamServiceProxy.insertLog(new LogChangeConfigDTO(userToken.getUserName(),
        "exportData", "ExportData Service Array: " + srConfigDTO.getConfigId(),
        srConfigDTO, null));

    return exportFileServiceArray(data, "", false);
  }

  @Override
  public File exportDataServiceGroup(SRConfigDTO srConfigDTO) throws Exception {
    mapCountryName.clear();
    setMapCountryName();
    List<SRConfigDTO> list =
        srServiceManageRepository
            .getListDataExportServiceGroup(srConfigDTO);
    if (list != null) {
      for (SRConfigDTO dto : list) {
        if ("A".equals(dto.getStatus())) {
          dto.setStatus("Active");
        } else if ("I".equals(dto.getStatus())) {
          dto.setStatus("Inactive");
        }

        if (mapCountryName != null && mapCountryName.size() > 0) {
          dto.setCountryDisplay(mapCountryName.get(dto.getCountry()));
        }
      }
    }

    UserToken userToken = ticketProvider.getUserToken();
    commonStreamServiceProxy.insertLog(new LogChangeConfigDTO(userToken.getUserName(),
        "exportData", "ExportData Service Group: " + srConfigDTO.getConfigId(),
        srConfigDTO, null));

    return exportFileServiceGroup(list, "", false);
  }

  @Override
  public File getTemplateServiceArray() throws Exception {
    return exportFileServiceArray(null, "RESULT_IMPORT", false);
  }

  @Override
  public File getTemplateServiceGroup() throws Exception {
    return exportFileServiceGroup(null, "RESULT_IMPORT", false);
  }

  private boolean validFileFormatServiceArray(List<Object[]> lstHeader) {
    Object[] obj0 = lstHeader.get(0);
    int count = 0;

    for (Object data : obj0) {
      if (data != null) {
        count += 1;
      }
    }

    if (count != 6) {
      return false;
    }
    if (obj0 == null) {
      return false;
    }
    if (obj0[0] == null) {
      return false;
    }

    if (!(I18n.getLanguage("common.STT")).equalsIgnoreCase(obj0[0].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("SrServiceArray.configCode") + " (*)")
        .equalsIgnoreCase(obj0[1].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("SrServiceArray.configName") + " (*)")
        .equalsIgnoreCase(obj0[2].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("SrServiceArray.countryCode") + " (*)")
        .equalsIgnoreCase(obj0[3].toString().trim().replace("\n", " "))) {
      return false;
    }
    if (!(I18n.getLanguage("SrServiceArray.automation"))
        .equalsIgnoreCase(obj0[4].toString().trim().replace("\n", " "))) {
      return false;
    }
    if (!(I18n.getLanguage("SrServiceArray.action") + " (*)")
        .equalsIgnoreCase(obj0[5].toString().trim().replace("\n", " "))) {
      return false;
    }
    return true;
  }

  private boolean validFileFormatServiceGroup(List<Object[]> lstHeader) {
    Object[] obj0 = lstHeader.get(0);
    int count = 0;

    for (Object data : obj0) {
      if (data != null) {
        count += 1;
      }
    }

    if (count != 6) {
      return false;
    }
    if (obj0 == null) {
      return false;
    }
    if (obj0[0] == null) {
      return false;
    }

    if (!(I18n.getLanguage("common.STT")).equalsIgnoreCase(obj0[0].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("SrServiceGroup.parentCode") + " (*)")
        .equalsIgnoreCase(obj0[1].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("SrServiceGroup.configCode") + " (*)")
        .equalsIgnoreCase(obj0[2].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("SrServiceGroup.configName") + " (*)")
        .equalsIgnoreCase(obj0[3].toString().trim().replace("\n", " "))) {
      return false;
    }
    if (!(I18n.getLanguage("SrServiceGroup.country") + " (*)")
        .equalsIgnoreCase(obj0[4].toString().trim().replace("\n", " "))) {
      return false;
    }
    if (!(I18n.getLanguage("SrServiceGroup.action") + " (*)")
        .equalsIgnoreCase(obj0[5].toString().trim().replace("\n", " "))) {
      return false;
    }
    return true;
  }

  private boolean validateImportUpdateCatalogInfo(SRConfigDTO dto, String action) {

    if (dto.getCountry().contains("E")) {
      BigDecimal bd = new BigDecimal(dto.getCountry());
      dto.setCountry(bd + "");
    }

    if (StringUtils.isStringNullOrEmpty(dto.getConfigCode())) {
      dto.setResultImport(I18n.getLanguage("sr.arrayCode.required"));
      return false;
    } else if (dto.getConfigCode().length() > 200) {
      dto.setResultImport(I18n.getLanguage("srConfig.serviceCode.Array") + " " + I18n
          .getLanguage("srConfig.import.codeOverLength"));
      return false;
    }

    if (StringUtils.isStringNullOrEmpty(dto.getConfigName())) {
      dto.setResultImport(I18n.getLanguage("sr.arrayName.required"));
      return false;
    } else if (dto.getConfigName().length() > 200) {
      dto.setResultImport(I18n.getLanguage("srConfig.serviceName.Array") + " " + I18n
          .getLanguage("srCatalog.import.nameOverLength"));
      return false;
    }

    if (StringUtils.isStringNullOrEmpty(dto.getCountry())) {
      dto.setResultImport(I18n.getLanguage("srConfig.country.notnull"));
      return false;
    } else if (!StringUtils.isStringNullOrEmpty(action)
        && mapCountryName.get(dto.getCountry()) == null) {
      dto.setResultImport(I18n.getLanguage("srConfig.country.invalid"));
      return false;
    }

    // automation
    if (!StringUtils.isStringNullOrEmpty(dto.getAutomation())) {
      Optional<String> value = mapAutomation.keySet().stream()
          .filter(o -> o.equalsIgnoreCase(dto.getAutomation())).findAny();
      if (!value.isPresent()) {
        dto.setResultImport(I18n.getLanguage("srConfig.automation.invalid"));
        return false;
      } else {
        dto.setAutomation(value.get());
      }
    }
    //
    if (StringUtils.isStringNullOrEmpty(action)) {
      dto.setResultImport(
          I18n.getLanguage("srCatalog.import.action") + " " + I18n.getLanguage("common.notnull"));
      return false;
    } else if (!StringUtils.isStringNullOrEmpty(action) && !"C".equalsIgnoreCase(action.trim())
        && !"D".equalsIgnoreCase(action.trim())) {
      dto.setResultImport(I18n.getLanguage("srCatalog.import.actionError"));
      return false;
    }

    // Check not to change the status when assigned to the catalog
    List<SRCatalogDTO> lstCatalog;
    SRCatalogDTO catalogDTO = new SRCatalogDTO();
    catalogDTO.setServiceArray(dto.getConfigCode());

    lstCatalog = srCatalogBusiness.getListSRCatalogDTO(catalogDTO);
    if (lstCatalog != null && !lstCatalog.isEmpty()) {
      if ("C".equalsIgnoreCase(action.trim())) {
        dto.setResultImport(I18n.getLanguage("srCatalogChild.duplicate"));
        return false;
      }
      if ("D".equalsIgnoreCase(action.trim())) {
        dto.setResultImport(I18n.getLanguage("srConfig.import.error.catalog"));
        return false;
      }
    }

    return true;
  }

  @Override
  public ResultInSideDto importDataServiceArray(MultipartFile multipartFile) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(RESULT.SUCCESS);
    try {
      UserToken userToken = ticketProvider.getUserToken();
      // Lay du lieu import
      if (multipartFile == null || multipartFile.isEmpty()) {
        resultInSideDTO.setKey(RESULT.FILE_IS_NULL);
        return resultInSideDTO;
      } else {
        String filePath = FileUtils
            .saveTempFile(multipartFile.getOriginalFilename(), multipartFile.getBytes(),
                tempFolder);
        if (!RESULT.SUCCESS.equals(resultInSideDTO.getKey())) {
          return resultInSideDTO;
        }
        File fileImport = new File(filePath);

        List<Object[]> lstHeader;
        lstHeader = CommonImport.getDataFromExcelFile(fileImport,
            0,//sheet
            7,//begin row
            0,//from column
            5,//to column
            2);

        if (lstHeader.size() == 0 || !validFileFormatServiceArray(lstHeader)) {
          resultInSideDTO.setKey(RESULT.FILE_INVALID_FORMAT);
          return resultInSideDTO;
        }

        // Lay du lieu import
        List<Object[]> lstData = CommonImport.getDataFromExcelFile(
            fileImport,
            0,//sheet
            8,//begin row
            0,//from column
            5,//to column
            1000
        );

        if (lstData == null || lstData.size() < 1) {
          resultInSideDTO.setKey(RESULT.NODATA);
          return resultInSideDTO;
        }

        if (lstData.size() > 1500) {
          resultInSideDTO.setKey(RESULT.DATA_OVER);
          return resultInSideDTO;
        }

        List<SRConfigDTO> lstAddOrUpdate = new ArrayList<>();
        List<SRConfigDTO> lstErr = new ArrayList<>();

        mapCountryName.clear();
        mapAutomation.clear();
        mapAddObject.clear();
        mapAddObject2.clear();
        setMapCountryName();
        setMapAutomation();

        if (lstData != null && lstData.size() > 0) {
          int row = 1;
          for (Object[] obj : lstData) {
            String action = "";
            SRConfigDTO dto = new SRConfigDTO();
            if (obj[1] != null) {
              dto.setConfigGroup(Constants.SR_CATALOG.SERVICE_ARRAY);
              dto.setConfigCode(obj[1].toString().trim());
            }
            if (obj[2] != null) {
              dto.setConfigName(obj[2].toString());
            }
            if (obj[3] != null) {
              dto.setCountry(obj[3].toString());
            } else {
              dto.setCountry("");
            }
            if (obj[4] != null) {
              dto.setAutomation(obj[4].toString().trim());
            } else {
              dto.setAutomation("");
            }
            if (obj[5] != null) {
              action = obj[5].toString();
              dto.setAction(action);
            } else {
              action = "";
              dto.setAction(action);
            }
            // import array service
            if (validateImportUpdateCatalogInfo(dto, action)) {
              if (mapAddObject.get(dto.getConfigCode() + action) == null) {
                mapAddObject.put(dto.getConfigCode() + action, dto.getConfigCode() + action);
                if (mapAddObject2.get(dto.getConfigName() + action) == null) {
                  mapAddObject2.put(dto.getConfigName() + action, dto.getConfigName() + action);
                  SRConfigDTO dtoCheck = new SRConfigDTO();
                  dtoCheck.setConfigCode(dto.getConfigCode().trim());
                  dtoCheck.setConfigGroup(dto.getConfigGroup());
                  dtoCheck.setConfigName(dto.getConfigName().trim());

                  List<SRConfigDTO> lstCheck = srServiceManageRepository
                      .getListSRConfigImport(dtoCheck);//ham getListSRConfigImport la ham check

                  if (lstCheck == null || lstCheck.isEmpty()) {
                    if ("C".equalsIgnoreCase(action)) {
                      dto.setCreatedUser(userToken.getUserName());
                      dto.setCreatedTime(DateTimeUtils.convertDateTimeStampToString(new Date()));
                      dto.setUpdatedUser(userToken.getUserName());
                      dto.setUpdatedTime(DateTimeUtils.convertDateTimeStampToString(new Date()));
                      dto.setStatus("A");
                      lstAddOrUpdate.add(dto);
                    } else {
                      dto.setResultImport(I18n.getLanguage("srCatalog.import.cantFindRecord"));
                    }
                  } else {
                    if (lstCheck.size() == 1) {
                      SRConfigDTO dtoUpdate = lstCheck.get(0);
                      dto.setConfigId(dtoUpdate.getConfigId());
                      if ("D".equalsIgnoreCase(action)) {
                        if (dto.getConfigName().equalsIgnoreCase(dtoUpdate.getConfigName()) && dto
                            .getConfigCode().equalsIgnoreCase(dtoUpdate.getConfigCode())) {
                          if ("A".equals(dtoUpdate.getStatus())) {
                            dtoUpdate.setUpdatedUser(userToken.getUserName());
                            dtoUpdate.setUpdatedTime(
                                DateTimeUtils.convertDateTimeStampToString(new Date()));
                            dtoUpdate.setStatus("I");
                            lstAddOrUpdate.add(dtoUpdate);
                          } else {
                            dto.setResultImport(I18n.getLanguage("srConfig.import.error.status"));
                          }
                        } else {
                          dto.setResultImport(I18n.getLanguage("srCatalog.import.cantFindRecord"));
                        }
                      } else {

                        if ("A".equalsIgnoreCase(dtoUpdate.getStatus())) {
                          dto.setResultImport(I18n.getLanguage("sr.Arrray.fail"));
                        } else {
                          dtoUpdate.setConfigCode(dto.getConfigCode().trim());
                          dtoUpdate.setConfigName(dto.getConfigName());
                          dtoUpdate.setUpdatedUser(userToken.getUserName());
                          dtoUpdate.setUpdatedTime(
                              DateTimeUtils.convertDateTimeStampToString(new Date()));
                          dtoUpdate.setStatus("A");
                          dtoUpdate.setAutomation(dto.getAutomation());
                          lstAddOrUpdate.add(dtoUpdate);
                        }
                      }
                    } else if (lstCheck.size() == 2) {
                      String statusFirst = lstCheck.get(0).getStatus();
                      String statusSecond = lstCheck.get(1).getStatus();
                      SRConfigDTO dtoUpdate = lstCheck.get(0);
                      if (statusFirst.equals(statusSecond)) {
                        dto.setConfigId(dtoUpdate.getConfigId());
                        if ("C".equalsIgnoreCase(action)) {
                          if ("I".equalsIgnoreCase(dtoUpdate.getStatus())) {
                            if (dto.getConfigCode().equalsIgnoreCase(dtoUpdate.getConfigCode())) {
                              dtoUpdate.setConfigName(dto.getConfigName());
                              dtoUpdate.setUpdatedUser(userToken.getUserName());
                              dtoUpdate.setUpdatedTime(
                                  DateTimeUtils.convertDateTimeStampToString(new Date()));
                              dtoUpdate.setStatus("A");
                              dtoUpdate.setAutomation(dto.getAutomation());
                              lstAddOrUpdate.add(dtoUpdate);
                            } else {
                              dto.setResultImport(
                                  I18n.getLanguage("srCatalog.import.cantFindRecord"));
                            }
                          } else if ("A".equals(dtoUpdate.getStatus())) {
                            dto.setResultImport(I18n.getLanguage("sr.Arrray.fail"));
                          } else {
                            dto.setResultImport(
                                I18n.getLanguage("srCatalog.import.cantFindRecord"));
                          }
                        } else {
                          if ("D".equalsIgnoreCase(action)) {
                            if (dto.getConfigCode().equalsIgnoreCase(dtoUpdate.getConfigCode())
                                && dto.getConfigName()
                                .equalsIgnoreCase(dtoUpdate.getConfigName())) {
                              if ("I".equals(dtoUpdate.getStatus())) {
                                dto.setResultImport(
                                    I18n.getLanguage("srConfig.import.error.status"));
                              } else if ("A".equals(dtoUpdate.getStatus())) {
                                dtoUpdate.setUpdatedUser(userToken.getUserName());
                                dtoUpdate.setUpdatedTime(
                                    DateTimeUtils.convertDateTimeStampToString(new Date()));
                                dtoUpdate.setStatus("I");
                                lstAddOrUpdate.add(dtoUpdate);
                              } else {
                                dto.setResultImport(
                                    I18n.getLanguage("srCatalog.import.cantFindRecord"));
                              }
                            } else {
                              dto.setResultImport(
                                  I18n.getLanguage("srCatalog.import.cantFindRecord"));
                            }

                          }
                        }
                      } else {
                        if ("C".equalsIgnoreCase(action)) {
                          if ("A".equals(dtoUpdate.getStatus())) { // FALSE
                            dto.setResultImport(I18n.getLanguage("sr.Arrray.fail"));
                          }
                          if ("I".equals(dtoUpdate.getStatus())) { // UPDATE
                            dto.setResultImport(I18n.getLanguage("sr.Arrray.fail"));
                          }
                        } else if ("D".equalsIgnoreCase(action)) {
                          if (dto.getConfigCode().equalsIgnoreCase(dtoUpdate.getConfigCode()) && dto
                              .getConfigName().equalsIgnoreCase(dtoUpdate.getConfigName())) {
                            if ("A".equals(dtoUpdate.getStatus())) { // UPDATE
                              dtoUpdate.setUpdatedUser(userToken.getUserName());
                              dtoUpdate.setUpdatedTime(
                                  DateTimeUtils.convertDateTimeStampToString(new Date()));
                              dtoUpdate.setStatus("I");
                              lstAddOrUpdate.add(dtoUpdate);
                            } else if ("I".equals(dtoUpdate.getStatus())) { // INACTIVE
                              dto.setResultImport(I18n.getLanguage("srConfig.import.error.status"));
                            }
                          } else {
                            dto.setResultImport(
                                I18n.getLanguage("srCatalog.import.cantFindRecord"));
                          }
                        }
                      }
                    }
                  }
                } else {
                  dto.setResultImport(I18n.getLanguage("srCatalog.import.updateCatalogDuplicate"));
                }
              } else {
                dto.setResultImport(I18n.getLanguage("srCatalog.import.updateCatalogDuplicate"));
              }
            }
            row++;

            lstErr.add(dto);
          }

          boolean check = false;
          for (SRConfigDTO item : lstErr) {
            if (!StringUtils.isStringNullOrEmpty(item.getResultImport())) {
              check = true;
              break;
            }
          }

          if (check) {
            for (SRConfigDTO item : lstErr) {
              if (StringUtils.isStringNullOrEmpty(item.getResultImport())) {
                item.setResultImport(I18n.getLanguage("SrMappingProcessCr.record.import"));
              }
            }

            File expFile = exportFileServiceArray(lstErr, "RESULT_IMPORT", true);
            resultInSideDTO.setKey(RESULT.ERROR);
            resultInSideDTO.setFile(expFile);
          } else {
            if (!lstAddOrUpdate.isEmpty()) {
              for (SRConfigDTO item : lstAddOrUpdate) {
                item.setConfigGroup(SR_CATALOG.SERVICE_ARRAY);
                resultInSideDTO = srServiceManageRepository.insertOrUpdateService(item);
              }
            }
          }
        }
      }

      if (resultInSideDTO.getKey().equals(Constants.RESULT.SUCCESS)) {
        commonStreamServiceProxy.insertLog(new LogChangeConfigDTO(userToken.getUserName(),
            "Import ArrayService", "ArrayService:" + resultInSideDTO.getId(),
            null, null));
      }

    } catch (IOException e) {
      log.error("Exception:", e);
      resultInSideDTO = new ResultInSideDto();
      resultInSideDTO.setKey(RESULT.ERROR);
      resultInSideDTO.setMessage(e.getMessage());
    } catch (Exception e) {
      log.error("Exception:", e);
    }

    return resultInSideDTO;
  }

  @Override
  public ResultInSideDto importDataServiceGroup(MultipartFile multipartFile) {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(RESULT.SUCCESS);
    try {

      UserToken userToken = ticketProvider.getUserToken();
      // Lay du lieu import
      if (multipartFile == null || multipartFile.isEmpty()) {
        resultInSideDTO.setKey(RESULT.FILE_IS_NULL);
        return resultInSideDTO;
      } else {
        String filePath = FileUtils
            .saveTempFile(multipartFile.getOriginalFilename(), multipartFile.getBytes(),
                tempFolder);
        if (!RESULT.SUCCESS.equals(resultInSideDTO.getKey())) {
          return resultInSideDTO;
        }
        File fileImport = new File(filePath);

        List<Object[]> lstHeader;
        lstHeader = CommonImport.getDataFromExcelFile(fileImport,
            0,//sheet
            7,//begin row
            0,//from column
            5,//to column
            2);

        if (lstHeader.size() == 0 || !validFileFormatServiceGroup(lstHeader)) {
          resultInSideDTO.setKey(RESULT.FILE_INVALID_FORMAT);
          return resultInSideDTO;
        }

        // Lay du lieu import
        List<Object[]> lstData = CommonImport.getDataFromExcelFile(
            fileImport,
            0,//sheet
            8,//begin row
            0,//from column
            5,//to column
            1000
        );

        if (lstData == null || lstData.size() < 1) {
          resultInSideDTO.setKey(RESULT.NODATA);
          return resultInSideDTO;
        }

        if (lstData.size() > 1500) {
          resultInSideDTO.setKey(RESULT.DATA_OVER);
          return resultInSideDTO;
        }

        List<SRConfigDTO> lstErr = new ArrayList<>();

        mapCountryName.clear();
        mapParentCode.clear();
        mapAddObject.clear();
        mapAddObject2.clear();
        setMapCountryName();
        setMapParentCode();
        List<SRConfigDTO> lstAddOrUpdate = new ArrayList<>();

        mapAddObject.clear();
        mapAddObject2.clear();
        if (lstData != null && lstData.size() > 0) {
          int row = 1;
          for (Object[] obj : lstData) {
            String action = "";
            SRConfigDTO dto = new SRConfigDTO();
            if (obj[1] != null) {
              dto.setConfigGroup(Constants.SR_CATALOG.SERVICE_GROUP);
              dto.setParentCode(obj[1].toString());
            }
            if (obj[2] != null) {
              dto.setConfigCode(obj[2].toString());
            }
            if (obj[3] != null) {
              dto.setConfigName(obj[3].toString());
            }

            if (obj[4] != null) {
              dto.setCountry(obj[4].toString());
            } else {
              dto.setCountry("");
            }

            if (obj[5] != null) {
              action = obj[5].toString();
              dto.setAction(action);
            } else {
              action = "";
              dto.setAction(action);
            }
            // import service group
            if (validateImportUpdateConfigInfo(dto, action)) {
              if (mapAddObject.get(dto.getConfigCode() + action) == null) {
                mapAddObject.put(dto.getConfigCode() + action, dto.getConfigCode() + action);
                if (mapAddObject2.get(dto.getConfigName() + action) == null) {
                  mapAddObject2.put(dto.getConfigName() + action, dto.getConfigName() + action);
                  SRConfigDTO dtoCheck = new SRConfigDTO();
                  dtoCheck.setConfigCode(dto.getConfigCode());
                  dtoCheck.setConfigGroup(dto.getConfigGroup());
                  dtoCheck.setConfigName(dto.getConfigName().trim());
                  List<SRConfigDTO> lstCheck = srServiceManageRepository
                      .getListConfigDTOGroup(dtoCheck);

                  if (lstCheck == null || lstCheck.isEmpty()) {
                    if ("C".equalsIgnoreCase(action)) {
                      dto.setCreatedUser(userToken.getUserName());
                      dto.setCreatedTime(DateTimeUtils.convertDateTimeStampToString(new Date()));
                      dto.setUpdatedUser(userToken.getUserName());
                      dto.setUpdatedTime(DateTimeUtils.convertDateTimeStampToString(new Date()));
                      dto.setParentGroup(Constants.SR_CATALOG.SERVICE_ARRAY);
                      dto.setStatus("A");
                      lstAddOrUpdate.add(dto);
                    } else {
                      dto.setResultImport(I18n.getLanguage("srCatalog.import.cantFindRecord"));
                    }
                  } else {
                    if (lstCheck.size() == 1) {
                      SRConfigDTO dtoUpdate = lstCheck.get(0);
                      dto.setConfigId(dtoUpdate.getConfigId());
                      if ("D".equalsIgnoreCase(action)) {
                        if (dto.getParentCode().equalsIgnoreCase(dtoUpdate.getParentCode()) && dto
                            .getConfigCode().equalsIgnoreCase(dtoUpdate.getConfigCode()) && dto
                            .getConfigName().equalsIgnoreCase(dtoUpdate.getConfigName())) {
                          if ("A".equalsIgnoreCase(dtoUpdate.getStatus())) {
                            dtoUpdate.setUpdatedUser(userToken.getUserName());
                            dtoUpdate.setUpdatedTime(
                                DateTimeUtils.convertDateTimeStampToString(new Date()));
                            dtoUpdate.setStatus("I");
                            lstAddOrUpdate.add(dtoUpdate);
                          } else {
                            dto.setResultImport(I18n.getLanguage("srConfig.import.error.status"));
                          }
                        } else {
                          dto.setResultImport(I18n.getLanguage("srCatalog.import.cantFindRecord"));
                        }
                      } else {
                        if ("A".equalsIgnoreCase(dtoUpdate.getStatus())) {
                          dto.setResultImport(I18n.getLanguage("sr.Group.fail"));
                        } else if (dto.getParentCode().equalsIgnoreCase(dtoUpdate.getParentCode())
                            && dto.getConfigCode().equalsIgnoreCase(dtoUpdate.getConfigCode())) {
                          dtoUpdate.setConfigName(dto.getConfigName());
                          dtoUpdate.setUpdatedUser(userToken.getUserName());
                          dtoUpdate
                              .setUpdatedTime(
                                  DateTimeUtils.convertDateTimeStampToString(new Date()));
                          dtoUpdate.setStatus("A");
                          lstAddOrUpdate.add(dtoUpdate);
                        } else {
                          dto.setResultImport(I18n.getLanguage("srCatalog.import.cantFindRecord"));
                        }
                      }
                    } else if (lstCheck.size() == 2) {
                      String statusFirst = lstCheck.get(0).getStatus();
                      String statusSecond = lstCheck.get(1).getStatus();
                      SRConfigDTO dtoUpdate = lstCheck.get(0);
                      if (statusFirst.equalsIgnoreCase(statusSecond)) {
                        dto.setConfigId(dtoUpdate.getConfigId());
                        if ("C".equalsIgnoreCase(action)) {
                          if ("I".equalsIgnoreCase(dtoUpdate.getStatus())) {
                            if (dto.getParentCode().equalsIgnoreCase(dtoUpdate.getParentCode())
                                && dto
                                .getConfigCode().equalsIgnoreCase(dtoUpdate.getConfigCode())) {
                              dtoUpdate.setConfigName(dto.getConfigName());
                              dtoUpdate.setUpdatedUser(userToken.getUserName());
                              dtoUpdate.setUpdatedTime(
                                  DateTimeUtils.convertDateTimeStampToString(new Date()));
                              dtoUpdate.setStatus("A");
                              lstAddOrUpdate.add(dtoUpdate);
                            } else {
                              dto.setResultImport(
                                  I18n.getLanguage("srCatalog.import.cantFindRecord"));
                            }
                          } else if ("A".equalsIgnoreCase(dtoUpdate.getStatus())) {
                            dto.setResultImport(I18n.getLanguage("sr.Group.fail"));
                          } else {
                            dto.setResultImport(
                                I18n.getLanguage("srCatalog.import.cantFindRecord"));
                          }
                        } else {
                          if ("D".equalsIgnoreCase(action)) {
                            if (dto.getParentCode().equalsIgnoreCase(dtoUpdate.getParentCode())
                                && dto
                                .getConfigCode().equalsIgnoreCase(dtoUpdate.getConfigCode()) && dto
                                .getConfigName().equalsIgnoreCase(dtoUpdate.getConfigName())) {
                              if ("I".equalsIgnoreCase(dtoUpdate.getStatus())) {
                                dto.setResultImport(
                                    I18n.getLanguage("srConfig.import.error.status"));
                              } else if ("A"
                                  .equalsIgnoreCase(dtoUpdate.getStatus())) {
                                dtoUpdate.setUpdatedUser(userToken.getUserName());
                                dtoUpdate.setUpdatedTime(
                                    DateTimeUtils.convertDateTimeStampToString(new Date()));
                                dtoUpdate.setStatus("I");
                                lstAddOrUpdate.add(dtoUpdate);
                              } else {
                                dto.setResultImport(
                                    I18n.getLanguage("srCatalog.import.cantFindRecord"));
                              }
                            } else {
                              dto.setResultImport(
                                  I18n.getLanguage("srCatalog.import.cantFindRecord"));
                            }

                          }
                        }
                      } else {

                        if ("C".equalsIgnoreCase(action)) {
                          if ("A".equalsIgnoreCase(dtoUpdate.getStatus())) { // FALSE
                            dto.setResultImport(I18n.getLanguage("sr.Group.fail"));
                          }
                          if ("I".equalsIgnoreCase(dtoUpdate.getStatus())) { // UPDATE
                            dto.setResultImport(I18n.getLanguage("sr.Group.fail"));
                          }
                        } else if ("D".equalsIgnoreCase(action)) {
                          if (dto.getParentCode().equalsIgnoreCase(dtoUpdate.getParentCode()) && dto
                              .getConfigCode().equalsIgnoreCase(dtoUpdate.getConfigCode()) && dto
                              .getConfigName().equalsIgnoreCase(dtoUpdate.getConfigName())) {
                            if ("A".equalsIgnoreCase(dtoUpdate.getStatus())) { // UPDATE
                              dtoUpdate.setUpdatedUser(userToken.getUserName());
                              dtoUpdate.setUpdatedTime(
                                  DateTimeUtils.convertDateTimeStampToString(new Date()));
                              dtoUpdate.setStatus("I");
                              lstAddOrUpdate.add(dtoUpdate);
                            } else if ("I".equals(dtoUpdate.getStatus())) { // INACTIVE
                              dto.setResultImport(I18n.getLanguage("srConfig.import.error.status"));
                            }
                          } else {
                            dto.setResultImport(
                                I18n.getLanguage("srCatalog.import.cantFindRecord"));

                          }
                        }
                      }
                    }
                  }
                } else {
                  dto.setResultImport(I18n.getLanguage("srCatalog.import.updateCatalogDuplicate"));
                }
              } else {
                dto.setResultImport(I18n.getLanguage("srCatalog.import.updateCatalogDuplicate"));
              }
            } else {
            }
            row++;
            lstErr.add(dto);
          }

          boolean check = false;
          for (SRConfigDTO item : lstErr) {
            if (!StringUtils.isStringNullOrEmpty(item.getResultImport())) {
              check = true;
              break;
            }
          }

          if (check) {
            for (SRConfigDTO item : lstErr) {
              if (StringUtils.isStringNullOrEmpty(item.getResultImport())) {
                item.setResultImport(I18n.getLanguage("SrMappingProcessCr.record.import"));
              }
            }

            File expFile = exportFileServiceGroup(lstErr, "RESULT_IMPORT", true);
            resultInSideDTO.setKey(RESULT.ERROR);
            resultInSideDTO.setFile(expFile);
          } else {
            if (!lstAddOrUpdate.isEmpty()) {
              for (SRConfigDTO item : lstAddOrUpdate) {
                item.setConfigGroup(SR_CATALOG.SERVICE_GROUP);
                resultInSideDTO = srServiceManageRepository.insertOrUpdateService(item);
              }
            }
          }

          if (resultInSideDTO.getKey().equals(Constants.RESULT.SUCCESS)) {
            commonStreamServiceProxy.insertLog(new LogChangeConfigDTO(userToken.getUserName(),
                "Import Group Service", "Group Service:" + resultInSideDTO.getId(),
                null, null));
          }
        }
      }

    } catch (IOException e) {
      log.error("Exception:", e);
      resultInSideDTO = new ResultInSideDto();
      resultInSideDTO.setKey(RESULT.ERROR);
      resultInSideDTO.setMessage(e.getMessage());
    } catch (Exception e) {
      log.error("Exception:", e);
    }

    return resultInSideDTO;
  }

  private boolean validateImportUpdateConfigInfo(SRConfigDTO dto, String action) {

    if (dto.getCountry().contains("E")) {
      BigDecimal bd = new BigDecimal(dto.getCountry());
      dto.setCountry(bd + "");
    }

    if (StringUtils.isStringNullOrEmpty(dto.getParentCode())) {
      dto.setResultImport(I18n.getLanguage("srCatalog.import.serviceArrayCode") + " " + I18n
          .getLanguage("common.notnull"));
      return false;
    } else if (dto.getParentCode().length() > 200) {
      dto.setResultImport(I18n.getLanguage("srCatalog.import.serviceArrayCode") + " " + I18n
          .getLanguage("srCatalog.import.codeOverLength"));
      return false;
    } else {
      SRConfigDTO dtoCheck = new SRConfigDTO();
      dtoCheck.setConfigCode(dto.getParentCode());
      dtoCheck.setConfigGroup(Constants.SR_CATALOG.SERVICE_ARRAY);

      List<SRConfigDTO> lst = srServiceManageRepository.getListConfigDTO(dtoCheck); //  remember ???

      if (lst == null || lst.isEmpty()) {
        dto.setResultImport(I18n.getLanguage("srCatalog.import.serviceArrayCodeNotExist"));
        return false;
      }
    }

    if (StringUtils.isStringNullOrEmpty(dto.getConfigCode())) {
      dto.setResultImport(I18n.getLanguage("sr.groupCode.notnull"));
      return false;
    } else if (dto.getConfigCode().length() > 200) {
      dto.setResultImport(I18n.getLanguage("srConfig.serviceCode.Group") + " " + I18n
          .getLanguage("srConfig.import.codeOverLength"));
      return false;
    }

    if (StringUtils.isStringNullOrEmpty(dto.getConfigName())) {
      dto.setResultImport(I18n.getLanguage("sr.groupName.notnull"));
      return false;
    } else if (dto.getConfigName().length() > 200) {
      dto.setResultImport(I18n.getLanguage("srConfig.serviceName.Group") + " " + I18n
          .getLanguage("srCatalog.import.nameOverLength"));
      return false;
    }

//    boolean checkCode =srServiceManageRepository.checkSrConfigExistedByCode(dto);
//    boolean checkName =srServiceManageRepository.checkSrConfigExistedByName(dto);
//
//    if (checkCode == true) {
//      dto.setResultImport( I18n.getValidation("SrServiceGroup.configCode.isExist"));
//      return false;
//    }
//
//    if (checkName == true) {
//      dto.setResultImport( I18n.getValidation("SrServiceGroup.configName.isExist"));
//      return false;
//    }

    // Check not to change the status when assigned to the catalog
    List<SRCatalogDTO> lstCatalog;
    SRCatalogDTO catalogDTO = new SRCatalogDTO();
    catalogDTO.setServiceGroup(dto.getConfigCode());

    lstCatalog = srCatalogRepository.getListSRCatalogDTO(catalogDTO);
    if (lstCatalog != null && !lstCatalog.isEmpty()) {
      dto.setResultImport(I18n.getLanguage("srConfig.import.error.catalog"));
      return false;
    }

    if (StringUtils.isStringNullOrEmpty(dto.getCountry())) {
      dto.setResultImport(I18n.getLanguage("srConfig.country.notnull"));
      return false;
    } else if (!StringUtils.isStringNullOrEmpty(dto.getCountry())
        && mapCountryName.get(dto.getCountry()) == null) {
      dto.setResultImport(I18n.getLanguage("srConfig.country.invalid"));
      return false;
    } else {
      //validate quoc gia co mang hay ko
      SRConfigDTO temp1 = mapParentCode.get(dto.getParentCode());
      String temp2 = dto.getCountry();
      if (temp1 != null && temp2 != null) {
        if (!temp1.getCountry().equals(temp2)) {
          dto.setResultImport(I18n.getLanguage("srConfig.country.invalid2")
              .replace("{0}", dto.getCountry())
              .replace("{1}", dto.getParentCode()));

          return false;
        }
      } else {
        String parentCode = "", country = "";
        if (StringUtils.isStringNullOrEmpty(dto.getParentCode())) {
          parentCode = "";
        } else {
          parentCode = dto.getParentCode();
        }

        if (StringUtils.isStringNullOrEmpty(dto.getCountry())) {
          country = "";
        } else {
          country = dto.getCountry();
        }

        dto.setResultImport(I18n.getLanguage("srConfig.country.invalid2").replace("{0}", country)
            .replace("{1}", parentCode));
        return false;
      }
    }

    if (StringUtils.isStringNullOrEmpty(action)) {
      dto.setResultImport(
          I18n.getLanguage("srCatalog.import.action") + " " + I18n.getLanguage("common.notnull"));
      return false;
    } else if (!StringUtils.isStringNullOrEmpty(action) && !"C".equals(action.toUpperCase()) && !"D"
        .equals(action.toUpperCase())) {
      dto.setResultImport(I18n.getLanguage("srCatalog.import.actionError"));
      return false;
    }

    return true;
  }

//  private boolean validateImportUpdateConfigInfo(SRConfigDTO dto, String action) {
//      dto.setResultImport("");
//      if (StringUtils.isStringNullOrEmpty(dto.getParentCode())) {
//        dto.setResultImport( dto.getResultImport() + ", "+ I18n.getLanguage("srCatalog.import.serviceArrayCode") + " " + I18n.getLanguage("common.notnull"));
//      } else if (dto.getParentCode().length() > 100) {
//        dto.setResultImport(dto.getResultImport() + ", "+ I18n.getLanguage("srCatalog.import.serviceArrayCode") + " " + I18n.getLanguage("srCatalog.import.codeOverLength"));
//      } else {
//        SRConfigDTO dtoCheck = new SRConfigDTO();
//        dtoCheck.setConfigCode(dto.getParentCode());
//        dtoCheck.setConfigGroup(Constants.SR_CATALOG.SERVICE_ARRAY);
//
//        SRConfigDTO srConfigDTO = srServiceManageRepository.getSRServiceGroupByDTO(dtoCheck);
//
//        if (srConfigDTO == null) {
//          dto.setResultImport(dto.getResultImport() + ", "+ I18n.getLanguage("srCatalog.import.serviceArrayCodeNotExist"));
//        }
//      }
//
//      if (StringUtils.isStringNullOrEmpty(dto.getConfigCode())) {
//        dto.setResultImport(dto.getResultImport() + ", "+ I18n.getLanguage("sr.groupCode.notnull"));
//      } else if (dto.getConfigCode().length() > 4000) {
//        dto.setResultImport(dto.getResultImport() + ", "+ I18n.getLanguage("srConfig.serviceCode.Group") + " " + I18n.getLanguage("srConfig.import.codeOverLength"));
//      }
//
//      if (StringUtils.isStringNullOrEmpty(dto.getConfigName())) {
//        dto.setResultImport(dto.getResultImport() + ", "+ I18n.getLanguage("sr.groupName.notnull"));
//      } else if (dto.getConfigName().length() > 200) {
//        dto.setResultImport(dto.getResultImport() + ", "+ I18n.getLanguage("srConfig.serviceCode.Group") + " " + I18n.getLanguage("srCatalog.import.nameOverLength"));
//      }
//
//      boolean checkCode =srServiceManageRepository.checkSrConfigExistedByCode(dto);
//      boolean checkName =srServiceManageRepository.checkSrConfigExistedByName(dto);
//
//      if (checkCode == true) {
//        dto.setResultImport(dto.getResultImport() + ", "+ I18n.getValidation("SrServiceGroup.configCode.isExist"));
//      }
//
//      if (checkName == true) {
//        dto.setResultImport(dto.getResultImport() + ", "+ I18n.getValidation("SrServiceGroup.configName.isExist"));
//      }
//
//      // Check not to change the status when assigned to the catalog
//      List<SRCatalogDTO> lstCatalog;
//      SRCatalogDTO catalogDTO = new SRCatalogDTO();
//      catalogDTO.setServiceGroup(dto.getConfigCode());
//
//      lstCatalog = srCatalogRepository.getListSRCatalogDTO(catalogDTO);
//      if (lstCatalog != null && !lstCatalog.isEmpty()) {
//        dto.setResultImport(dto.getResultImport() + ", "+ I18n.getLanguage("srConfig.import.error.catalog"));
//      }
//
//      if (dto.getCountry().contains("E")) {
//        BigDecimal bd = new BigDecimal(dto.getCountry());
//        dto.setCountry(bd+"");
//      }
//
//      if (StringUtils.isStringNullOrEmpty(dto.getCountry())) {
//        dto.setResultImport(dto.getResultImport() + ", "+ I18n.getLanguage("srConfig.country.notnull"));
//      } else if (!StringUtils.isStringNullOrEmpty(dto.getCountry()) && mapCountryName.get(dto.getCountry()) == null) {
//        dto.setResultImport(dto.getResultImport() + ", "+ I18n.getLanguage("srConfig.country.invalid"));
//      } else {
//        //validate quoc gia co mang hay ko
//        SRConfigDTO temp1  = mapParentCode.get(dto.getParentCode());
//        String temp2 = dto.getCountry();
//        if (temp1 != null && temp2 != null){
//          if (!temp1.getCountry().equals(temp2)) {
//            dto.setResultImport(dto.getResultImport() + ", "+ I18n.getLanguage("srConfig.country.invalid2")
//                .replace("{0}", dto.getCountry())
//                .replace("{1}", dto.getParentCode()));
//          }
//        } else {
//          String parentCode="", country="";
//          if (StringUtils.isStringNullOrEmpty(dto.getParentCode())) {
//            parentCode = "";
//          } else {
//            parentCode = dto.getParentCode();
//          }
//
//          if (StringUtils.isStringNullOrEmpty(dto.getCountry())) {
//            country = "";
//          } else {
//            country = dto.getCountry();
//          }
//
//          dto.setResultImport(dto.getResultImport() + ", "+ I18n.getLanguage("srConfig.country.invalid2").replace("{0}", country)
//              .replace("{1}", parentCode));
//        }
//      }
//
//      if (StringUtils.isStringNullOrEmpty(action)) {
//        dto.setResultImport(dto.getResultImport() + ", "+ I18n.getLanguage("srCatalog.import.action") + " " + I18n.getLanguage("common.notnull"));
//      } else if (!StringUtils.isStringNullOrEmpty(action) && !"C".equals(action.toUpperCase()) && !"D".equals(action.toUpperCase())) {
//        dto.setResultImport(dto.getResultImport() + ", "+ I18n.getLanguage("srCatalog.import.actionError"));
//      }
//
//      if (!StringUtils.isStringNullOrEmpty(dto.getResultImport())) {
//        dto.setResultImport(dto.getResultImport() + ", "+ I18n.getLanguage("srConfig.import.error.result"));
//        String temp = dto.getResultImport().startsWith(",") ? dto.getResultImport().substring(1)
//            : dto.getResultImport();
//        dto.setResultImport(temp.trim());
//        return false;
//      }
//      return true;
//    }

  @Override
  public SRConfigDTO getSRServiceArrayDetail(Long id) {
    return srServiceManageRepository.getSRServiceArrayDetail(id);
  }

  @Override
  public SRConfigDTO getSRServiceGroupDetail(Long id) {
    return srServiceManageRepository.getSRServiceGroupDetail(id);
  }

  @Override
  public ResultInSideDto deleteSRService(Long srConfigDTO) {
    UserToken userToken = ticketProvider.getUserToken();
    ResultInSideDto resultInSideDTO = srServiceManageRepository.deleteSRService(srConfigDTO);
    if (resultInSideDTO.getKey().equals(Constants.RESULT.SUCCESS)) {
      commonStreamServiceProxy.insertLog(new LogChangeConfigDTO(userToken.getUserName(),
          "Delete SRService", "Delete SRService:" + srConfigDTO,
          null, null));
    }

    return resultInSideDTO;
  }

  @Override
  public Datatable getListSearchSRServiceGroup(SRConfigDTO srConfigDTO) {
    mapCountryName.clear();
    setMapCountryName();
    Datatable datatable = srServiceManageRepository.getListSearchSRServiceGroup(srConfigDTO);

    List<SRConfigDTO> list = (List<SRConfigDTO>) datatable.getData();
    for (SRConfigDTO dto : list) {
      dto.setBtnDelete(true);
      List<SRCatalogDTO> lst;
      SRCatalogDTO catalogDTO = new SRCatalogDTO();
      catalogDTO.setServiceGroup(dto.getConfigCode());
      catalogDTO.setServiceArray(dto.getParentCode());
      lst = srCatalogRepository.getListSRCatalogDTO(catalogDTO);

      if (lst != null && !lst.isEmpty()) {
        dto.setBtnDelete(false);
      }

      if (mapCountryName != null && mapCountryName.size() > 0) {
        dto.setCountryDisplay(mapCountryName.get(dto.getCountry()));
      }
    }

    datatable.setData(list);
    return datatable;
  }

  @Override
  public ResultInSideDto insertOrUpdateServiceGroup(SRConfigDTO srConfigDTO) {

    ResultInSideDto resultInSideDto = new ResultInSideDto();

    UserToken userToken = ticketProvider.getUserToken();

    srConfigDTO.setConfigCode(srConfigDTO.getConfigCode().trim());
    srConfigDTO.setConfigName(srConfigDTO.getConfigName().trim());
    srConfigDTO.setCreatedUser(userToken.getUserName());
    srConfigDTO.setCreatedTime(DateTimeUtils.date2ddMMyyyyHHMMss(new Date()));
    srConfigDTO.setUpdatedTime(DateTimeUtils.date2ddMMyyyyHHMMss(new Date()));
    srConfigDTO.setUpdatedUser(userToken.getUserName());
    srConfigDTO.setConfigGroup(Constants.SR_CATALOG.SERVICE_GROUP);
    srConfigDTO.setParentGroup(Constants.SR_CATALOG.SERVICE_ARRAY);
    srConfigDTO.setStatus(Constants.SR_CATALOG.SERVICE_STATUS);

    if (StringUtils.isStringNullOrEmpty(srConfigDTO.getConfigId())
        || srConfigDTO.getConfigId() == 0) {

      boolean checkCode = srServiceManageRepository.checkSrConfigExistedByCode(srConfigDTO);
      boolean checkName = srServiceManageRepository.checkSrConfigExistedByName(srConfigDTO);

      if (checkCode == true) {// && checkName == false) {
        resultInSideDto.setKey(RESULT.DUPLICATE);
        resultInSideDto
            .setMessage(I18n.getValidation("SrServiceGroup.isDuplicate.configCodeAndName"));
        return resultInSideDto;
      }

      if (checkName == true) {// && checkCode == false ) {
        resultInSideDto.setKey(RESULT.DUPLICATE);
        resultInSideDto
            .setMessage(I18n.getValidation("SrServiceGroup.isDuplicate.configCodeAndName"));
        return resultInSideDto;
      }

      SRConfigDTO dtoUpdate = srServiceManageRepository.getSRServiceArrayByDTO(srConfigDTO);
      if (dtoUpdate != null) {
        srConfigDTO.setConfigId(dtoUpdate.getConfigId());
        if (srConfigDTO.getConfigCode().equalsIgnoreCase(dtoUpdate.getConfigCode()) && srConfigDTO
            .getConfigName().equalsIgnoreCase(dtoUpdate.getConfigName())) {
          if ("I".equals(dtoUpdate.getStatus())) {
            dtoUpdate.setCreatedUser(userToken.getUserName());
            dtoUpdate.setCreatedTime(DateTimeUtils.date2ddMMyyyyHHMMss(new Date()));
            dtoUpdate.setUpdatedTime(DateTimeUtils.date2ddMMyyyyHHMMss(new Date()));
            dtoUpdate.setUpdatedUser(userToken.getUserName());
            dtoUpdate.setConfigGroup(Constants.SR_CATALOG.SERVICE_GROUP);
            dtoUpdate.setParentGroup(Constants.SR_CATALOG.SERVICE_ARRAY);
            dtoUpdate.setStatus(Constants.SR_CATALOG.SERVICE_STATUS);
            return srServiceManageRepository.insertOrUpdateService(dtoUpdate);
          } else {
            resultInSideDto.setKey(RESULT.DUPLICATE);
            resultInSideDto
                .setMessage(I18n.getValidation("SrServiceGroup.isDuplicate.configCodeAndName"));
            return resultInSideDto;
          }
        } else {
          resultInSideDto.setKey(RESULT.DUPLICATE);
          resultInSideDto
              .setMessage(I18n.getValidation("SrServiceGroup.isDuplicate.configCodeAndName"));
          return resultInSideDto;
        }
      } else {
        return srServiceManageRepository.insertOrUpdateService(srConfigDTO);
      }
    } else {
      SRConfigDTO dtoUpdate = srServiceManageRepository
          .getSRServiceGroupDetail(srConfigDTO.getConfigId());
      if (dtoUpdate != null) {

        if (!dtoUpdate.getConfigName().equalsIgnoreCase(srConfigDTO.getConfigName())) {
          boolean check = srServiceManageRepository.checkSrConfigExistedByName(srConfigDTO);
          if (check) {
            resultInSideDto.setKey(RESULT.DUPLICATE);
            resultInSideDto
                .setMessage(I18n.getValidation("SrServiceGroup.isDuplicate.configCodeAndName"));
            return resultInSideDto;
          }
        } else {
          List<SRConfigDTO> list = srServiceManageRepository.getSrConfigExistedByName(srConfigDTO);
          if (list != null && list.size() > 1) {
            resultInSideDto.setKey(RESULT.DUPLICATE);
            resultInSideDto
                .setMessage(I18n.getValidation("SrServiceGroup.isDuplicate.configCodeAndName"));
            return resultInSideDto;
          }
        }

        dtoUpdate.setCountry(srConfigDTO.getCountry());
        dtoUpdate.setConfigName(srConfigDTO.getConfigName());
        dtoUpdate.setCreatedUser(userToken.getUserName());
        dtoUpdate.setCreatedTime(DateTimeUtils.date2ddMMyyyyHHMMss(new Date()));
        dtoUpdate.setUpdatedTime(DateTimeUtils.date2ddMMyyyyHHMMss(new Date()));
        dtoUpdate.setUpdatedUser(userToken.getUserName());
        dtoUpdate.setConfigGroup(Constants.SR_CATALOG.SERVICE_GROUP);
        dtoUpdate.setParentGroup(Constants.SR_CATALOG.SERVICE_ARRAY);
        if ("Active".equals(dtoUpdate.getStatus())) {
          dtoUpdate.setStatus("A");
        } else if ("Inactive".equals(dtoUpdate.getStatus())) {
          dtoUpdate.setStatus("I");
        }
        resultInSideDto = srServiceManageRepository.insertOrUpdateService(dtoUpdate);
      }
    }

    if (resultInSideDto.getKey().equals(Constants.RESULT.SUCCESS)) {
      String content = "";
      if (StringUtils.isStringNullOrEmpty(srConfigDTO.getConfigId())
          || srConfigDTO.getConfigId() == 0) {
        content = "Insert Group Service";
      } else {
        content = "Update Group Service";
      }
      commonStreamServiceProxy.insertLog(new LogChangeConfigDTO(userToken.getUserName(),
          content, content + resultInSideDto.getId(),
          srConfigDTO, null));
    }

    return resultInSideDto;
  }

  @Override
  public SRConfigDTO getSRServiceArrayByDTO(SRConfigDTO srConfigDTO) {
    return srServiceManageRepository.getSRServiceArrayByDTO(srConfigDTO);
  }

  @Override
  public boolean checkEnableGroup(SRConfigDTO configDTO) {
    configDTO.setConfigGroup(Constants.SR_CATALOG.SERVICE_GROUP);
    List<SRConfigDTO> list = srServiceManageRepository.checkEnableGroup(configDTO);
    if (list != null && list.size() > 0) {
      return false;
    }
    return true;
  }

  @Override
  public Datatable getListConfigServiceSystem(SRConfigDTO srConfigDTO) {
    return srServiceManageRepository.getListConfigServiceSystem(srConfigDTO);
  }

  @Override
  public ResultInSideDto insertOrUpdateSRConfigService(SRConfigDTO srConfigDTO) {
    UserToken userToken = ticketProvider.getUserToken();

    srConfigDTO.setCreatedTime(
        DateTimeUtils.convertDateToString(new Date(), DateTimeUtils.patternDateTimeMs));
    srConfigDTO.setCreatedUser(userToken.getUserName());
    srConfigDTO.setUpdatedTime(srConfigDTO.getCreatedTime());
    srConfigDTO.setUpdatedUser(userToken.getUserName());
//    srConfigDTO.setConfigId(srConfigServiceImpl.getSequenceSRConfig());
    srConfigDTO.setStatus(Constants.SR_CATALOG.SERVICE_STATUS);

    ResultInSideDto resultInSideDto = srServiceManageRepository.insertOrUpdateService(srConfigDTO);

    if (resultInSideDto.getKey().equals(Constants.RESULT.SUCCESS)) {
      String content = "";
      if (StringUtils.isStringNullOrEmpty(srConfigDTO.getConfigId())
          || srConfigDTO.getConfigId() == 0) {
        content = "Insert SR Config Service";
      } else {
        content = "Update SR Config Service";
      }
      commonStreamServiceProxy.insertLog(new LogChangeConfigDTO(userToken.getUserName(),
          content, content + resultInSideDto.getId(),
          srConfigDTO, null));
    }

    return resultInSideDto;
  }

  @Override
  public ResultInSideDto deleteSRConfig(Long srConfigDTO) {
    return srServiceManageRepository.deleteSRService(srConfigDTO);
  }

  @Override
  public List<SRConfigDTO> getByConfigGroup(String configGroup) {
    return srServiceManageRepository.getByConfigGroup(configGroup);
  }

  @Override
  public List<SRConfigDTO> getByConfigGroupCountry(String country, String configGroup) {
    List<SRConfigDTO> lstServicesArray = srServiceManageRepository.getByConfigGroup(configGroup);
    if (lstServicesArray != null && !lstServicesArray.isEmpty()) {
      for (int i = lstServicesArray.size() - 1; i > -1; i--) {
        if (!country.equals(lstServicesArray.get(i).getCountry())) {
          lstServicesArray.remove(i);
        }
      }
    }

    return lstServicesArray;
  }

  @Override
  public boolean checkSrConfigExisted(SRConfigDTO configDTO) {
    return srServiceManageRepository.checkSrConfigExisted(configDTO);
  }

  @Override
  public SearchConfigServiceDTO getListSearch() {
    SearchConfigServiceDTO searchDTOList = new SearchConfigServiceDTO();
    searchDTOList.setLstSystem(
        srServiceManageRepository.getByConfigGroup(Constants.SR_CONFIG.OTHER_SYS_SERVICE));
    SRCatalogDTO dto = new SRCatalogDTO();
    dto.setStatus("A");
    searchDTOList.setLstServices(srCatalogRepository.getListSRCatalogDTO(dto));

    return searchDTOList;
  }

  @Override
  public List<SRConfigDTO> getListSrConfig(SRConfigDTO srConfigDTO) {
    return srServiceManageRepository.getListSrConfig(srConfigDTO);
  }

  private File exportFileServiceArray(List<SRConfigDTO> lstCrEx, String key, boolean exportErr)
      throws
      Exception {
    String fileNameOut = "";
    String subTitle = "";
    String sheetName = "";
    String title = "";
    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    List<ConfigFileExport> fileExports = new ArrayList<>();
    ConfigHeaderExport columnSheet;
    List<ConfigHeaderExport> lstHeaderSheet = new ArrayList<>();

    if (Constants.RESULT_IMPORT.equals(key)) {
      sheetName = I18n.getLanguage("SrServiceArray.import.title");
      title = I18n.getLanguage("SrServiceArray.import.title");
      columnSheet = new ConfigHeaderExport("configCode", "LEFT", false, 0, 0, new String[]{}, null,
          "STRING");
      lstHeaderSheet.add(columnSheet);

      columnSheet = new ConfigHeaderExport("configName", "LEFT", false, 0, 0, new String[]{}, null,
          "STRING");
      lstHeaderSheet.add(columnSheet);

      columnSheet = new ConfigHeaderExport("country", "LEFT", false, 0, 0, new String[]{}, null,
          "STRING");
      lstHeaderSheet.add(columnSheet);

      columnSheet = new ConfigHeaderExport("automation", "LEFT", false, 0, 0, new String[]{}, null,
          "STRING");
      lstHeaderSheet.add(columnSheet);
      columnSheet = new ConfigHeaderExport("action", "LEFT", false, 0, 0, new String[]{}, null,
          "STRING");
      lstHeaderSheet.add(columnSheet);
    } else {
      sheetName = I18n.getLanguage("SrServiceArray.export.title");
      title = I18n.getLanguage("SrServiceArray.export.title");
      columnSheet = new ConfigHeaderExport("countryDisplay", "LEFT", false, 0, 0, new String[]{},
          null,
          "STRING");
      lstHeaderSheet.add(columnSheet);

      columnSheet = new ConfigHeaderExport("configCode", "LEFT", false, 0, 0, new String[]{}, null,
          "STRING");
      lstHeaderSheet.add(columnSheet);

      columnSheet = new ConfigHeaderExport("configName", "LEFT", false, 0, 0, new String[]{}, null,
          "STRING");
      lstHeaderSheet.add(columnSheet);

      columnSheet = new ConfigHeaderExport("automation", "LEFT", false, 0, 0, new String[]{}, null,
          "STRING");
      lstHeaderSheet.add(columnSheet);
      columnSheet = new ConfigHeaderExport("updatedUser", "LEFT", false, 0, 0, new String[]{}, null,
          "STRING");
      lstHeaderSheet.add(columnSheet);

      columnSheet = new ConfigHeaderExport("updatedTime", "LEFT", false, 0, 0, new String[]{}, null,
          "STRING");
      lstHeaderSheet.add(columnSheet);

      columnSheet = new ConfigHeaderExport("status", "LEFT", false, 0, 0, new String[]{}, null,
          "STRING");
      lstHeaderSheet.add(columnSheet);

      fileNameOut = SR_SERVICE_ARRAY_EXPORT;
      subTitle = I18n
          .getLanguage("SrServiceArray.export.eportDate", dateFormat.format(new Date()));
    }

    if (exportErr) {
      columnSheet = new ConfigHeaderExport("resultImport", "LEFT", false, 0, 0, new String[]{},
          null,
          "STRING");
      lstHeaderSheet.add(columnSheet);
    }

    Map<String, String> fieldSplit = new HashMap<>();
    ConfigFileExport configFileExport = new ConfigFileExport(
        lstCrEx,
        sheetName,
        title,
        subTitle,
        7,
        3,
        7,
        true,
        "language.SrServiceArray",
        lstHeaderSheet,
        fieldSplit,
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

    File fileExport = exportExcelServiceArray(
        fileTemplate
        , fileNameOut
        , fileExports
        , rootPath
        , new String[]{}
    );
    return fileExport;
  }

  private File exportFileServiceGroup(List<SRConfigDTO> lstCrEx, String key, boolean checkErr)
      throws
      Exception {
    String fileNameOut = "";
    String subTitle = "";
    String sheetName = "";
    String title = "";
    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    List<ConfigFileExport> fileExports = new ArrayList<>();
    ConfigHeaderExport columnSheet;
    List<ConfigHeaderExport> lstHeaderSheet = new ArrayList<>();

    if (Constants.RESULT_IMPORT.equals(key)) {
      sheetName = I18n.getLanguage("SrServiceGroup.import.title");
      title = I18n.getLanguage("SrServiceGroup.import.title");

      columnSheet = new ConfigHeaderExport("parentCode", "LEFT", false, 0, 0, new String[]{}, null,
          "STRING");
      lstHeaderSheet.add(columnSheet);

      columnSheet = new ConfigHeaderExport("configCode", "LEFT", false, 0, 0, new String[]{}, null,
          "STRING");
      lstHeaderSheet.add(columnSheet);

      columnSheet = new ConfigHeaderExport("configName", "LEFT", false, 0, 0, new String[]{}, null,
          "STRING");
      lstHeaderSheet.add(columnSheet);

      columnSheet = new ConfigHeaderExport("country", "LEFT", false, 0, 0, new String[]{}, null,
          "STRING");
      lstHeaderSheet.add(columnSheet);

      columnSheet = new ConfigHeaderExport("action", "LEFT", false, 0, 0, new String[]{}, null,
          "STRING");
      lstHeaderSheet.add(columnSheet);

    } else {
      sheetName = I18n.getLanguage("SrServiceGroup.export.title");
      title = I18n.getLanguage("SrServiceGroup.export.title");

      columnSheet = new ConfigHeaderExport("countryDisplay", "LEFT", false, 0, 0, new String[]{},
          null,
          "STRING");
      lstHeaderSheet.add(columnSheet);

      columnSheet = new ConfigHeaderExport("configCode", "LEFT", false, 0, 0, new String[]{}, null,
          "STRING");
      lstHeaderSheet.add(columnSheet);

      columnSheet = new ConfigHeaderExport("configName", "LEFT", false, 0, 0, new String[]{}, null,
          "STRING");
      lstHeaderSheet.add(columnSheet);

      columnSheet = new ConfigHeaderExport("parentName", "LEFT", false, 0, 0, new String[]{}, null,
          "STRING");
      lstHeaderSheet.add(columnSheet);
      columnSheet = new ConfigHeaderExport("updatedUser", "LEFT", false, 0, 0, new String[]{}, null,
          "STRING");
      lstHeaderSheet.add(columnSheet);

      columnSheet = new ConfigHeaderExport("updatedTime", "LEFT", false, 0, 0, new String[]{}, null,
          "STRING");
      lstHeaderSheet.add(columnSheet);

      columnSheet = new ConfigHeaderExport("status", "LEFT", false, 0, 0, new String[]{}, null,
          "STRING");
      lstHeaderSheet.add(columnSheet);
      fileNameOut = SR_SERVICE_GROUP_EXPORT;
      subTitle = I18n
          .getLanguage("SrServiceArray.export.eportDate", dateFormat.format(new Date()));
    }

    if (checkErr) {
      columnSheet = new ConfigHeaderExport("resultImport", "LEFT", false, 0, 0, new String[]{},
          null,
          "STRING");
      lstHeaderSheet.add(columnSheet);
    }

    Map<String, String> fieldSplit = new HashMap<>();
    ConfigFileExport configFileExport = new ConfigFileExport(
        lstCrEx,
        sheetName,
        title,
        subTitle,
        7,
        3,
        7,
        true,
        "language.SrServiceGroup",
        lstHeaderSheet,
        fieldSplit,
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

    File fileExport = exportExcelServiceGroup(
        fileTemplate
        , fileNameOut
        , fileExports
        , rootPath
        , new String[]{}
    );
    return fileExport;
  }

  public File exportExcelServiceArray(
      String pathTemplate,
      String fileNameOut,
      List<ConfigFileExport> config,
      String pathOut,
      String... exportChart
  ) throws Exception {
    File folderOut = new File(pathOut);
    if (!folderOut.exists()) {
      folderOut.mkdir();
    }
    SimpleDateFormat dateFormat = new SimpleDateFormat();
    dateFormat.applyPattern("dd/MM/yyyy HH:mm:ss");
    String strCurTimeExp = dateFormat.format(new Date());
    strCurTimeExp = strCurTimeExp.replaceAll("/", "_");
    strCurTimeExp = strCurTimeExp.replaceAll(" ", "_");
    strCurTimeExp = strCurTimeExp.replaceAll(":", "_");
    pathOut = pathOut + fileNameOut + strCurTimeExp + (exportChart != null && exportChart.length > 0
        ? XLSM_FILE_EXTENTION : XLSX_FILE_EXTENTION);
    try {
      log.info("Start get template file!");
      pathTemplate = StringUtils.removeSeparator(pathTemplate);
      Resource resource = new ClassPathResource(pathTemplate);
      InputStream fileTemplate = resource.getInputStream();
      XSSFWorkbook workbook_temp = new XSSFWorkbook(fileTemplate);
      log.info("End get template file!");
      SXSSFWorkbook workbook = new SXSSFWorkbook(workbook_temp, 1000);

      //<editor-fold defaultstate="collapsed" desc="Declare style">
      CellStyle cellStyleFormatNumber = workbook.createCellStyle();
      cellStyleFormatNumber.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
      cellStyleFormatNumber.setAlignment(HorizontalAlignment.RIGHT);
      cellStyleFormatNumber.setBorderLeft(BorderStyle.THIN);
      cellStyleFormatNumber.setBorderBottom(BorderStyle.THIN);
      cellStyleFormatNumber.setBorderRight(BorderStyle.THIN);
      cellStyleFormatNumber.setBorderTop(BorderStyle.THIN);

      CellStyle cellStyle = workbook.createCellStyle();
      cellStyle.setAlignment(HorizontalAlignment.CENTER);
      cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
      cellStyle.setBorderLeft(BorderStyle.THIN);
      cellStyle.setBorderBottom(BorderStyle.THIN);
      cellStyle.setBorderRight(BorderStyle.THIN);
      cellStyle.setBorderTop(BorderStyle.THIN);
      cellStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.index);
      cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
      cellStyle.setWrapText(true);

      Font xSSFFont = workbook.createFont();
      xSSFFont.setFontName(HSSFFont.FONT_ARIAL);
      xSSFFont.setFontHeightInPoints((short) 20);
      xSSFFont.setBold(true);
      xSSFFont.setColor(IndexedColors.BLACK.index);

      CellStyle cellStyleTitle = workbook.createCellStyle();
      cellStyleTitle.setAlignment(HorizontalAlignment.CENTER);
      cellStyleTitle.setVerticalAlignment(VerticalAlignment.CENTER);
      cellStyleTitle.setFillForegroundColor(IndexedColors.WHITE.index);
      cellStyleTitle.setFont(xSSFFont);

      Font xSSFFontHeader = workbook.createFont();
      xSSFFontHeader.setFontName("Times New Roman");
      xSSFFontHeader.setFontHeightInPoints((short) 10);
      xSSFFontHeader.setColor(IndexedColors.BLUE.index);
      xSSFFontHeader.setBold(true);

      Font subTitleFont = workbook.createFont();
      subTitleFont.setFontName(HSSFFont.FONT_ARIAL);
      subTitleFont.setFontHeightInPoints((short) 10);
      subTitleFont.setColor(IndexedColors.BLACK.index);

      Font xssFontTopHeader = workbook.createFont();
      xssFontTopHeader.setFontName("Times New Roman");
      xssFontTopHeader.setFontHeightInPoints((short) 10);
      xssFontTopHeader.setColor(IndexedColors.BLACK.index);
      xssFontTopHeader.setBold(true);

      Font rowDataFont = workbook.createFont();
      rowDataFont.setFontName(HSSFFont.FONT_ARIAL);
      rowDataFont.setFontHeightInPoints((short) 10);
      rowDataFont.setColor(IndexedColors.BLACK.index);

      CellStyle cellStyleTopHeader = workbook.createCellStyle();
      cellStyleTopHeader.setAlignment(HorizontalAlignment.CENTER);
      cellStyleTopHeader.setVerticalAlignment(VerticalAlignment.CENTER);
      cellStyleTopHeader.setFont(xssFontTopHeader);

      CellStyle cellStyleTopRightHeader = workbook.createCellStyle();
      cellStyleTopRightHeader.setAlignment(HorizontalAlignment.LEFT);
      cellStyleTopRightHeader.setVerticalAlignment(VerticalAlignment.CENTER);
      cellStyleTopRightHeader.setFont(xssFontTopHeader);

      CellStyle cellStyleSubTitle = workbook.createCellStyle();
      cellStyleSubTitle.setAlignment(HorizontalAlignment.CENTER);
      cellStyleSubTitle.setVerticalAlignment(VerticalAlignment.CENTER);
      cellStyleSubTitle.setFont(subTitleFont);

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

      CellStyle cellStyleLeft = workbook.createCellStyle();
      cellStyleLeft.setAlignment(HorizontalAlignment.LEFT);
      cellStyleLeft.setVerticalAlignment(VerticalAlignment.CENTER);
      cellStyleLeft.setBorderLeft(BorderStyle.THIN);
      cellStyleLeft.setBorderBottom(BorderStyle.THIN);
      cellStyleLeft.setBorderRight(BorderStyle.THIN);
      cellStyleLeft.setBorderTop(BorderStyle.THIN);
      cellStyleLeft.setWrapText(true);
      cellStyleLeft.setFont(rowDataFont);
      CellStyle cellStyleRight = workbook.createCellStyle();
      cellStyleRight.setAlignment(HorizontalAlignment.RIGHT);
      cellStyleRight.setVerticalAlignment(VerticalAlignment.CENTER);
      cellStyleRight.setBorderLeft(BorderStyle.THIN);
      cellStyleRight.setBorderBottom(BorderStyle.THIN);
      cellStyleRight.setBorderRight(BorderStyle.THIN);
      cellStyleRight.setBorderTop(BorderStyle.THIN);
      cellStyleRight.setWrapText(true);
      cellStyleRight.setFont(rowDataFont);
      //gnoc_cr
      CellStyle cellStyleHeaderOver = workbook.createCellStyle();
      cellStyleHeaderOver.setAlignment(HorizontalAlignment.LEFT);
      cellStyleHeaderOver.setVerticalAlignment(VerticalAlignment.CENTER);
      cellStyleHeaderOver.setBorderLeft(BorderStyle.THIN);
      cellStyleHeaderOver.setBorderBottom(BorderStyle.THIN);
      cellStyleHeaderOver.setBorderRight(BorderStyle.THIN);
      cellStyleHeaderOver.setBorderTop(BorderStyle.THIN);
      cellStyleHeaderOver.setFillForegroundColor(IndexedColors.LIGHT_GREEN.index);
      cellStyleHeaderOver.setFillPattern(FillPatternType.SOLID_FOREGROUND);
      cellStyleHeaderOver.setWrapText(true);
      cellStyleHeaderOver.setFont(xSSFFontHeader);

      CellStyle cellStyleCenter = workbook.createCellStyle();
      cellStyleCenter.setAlignment(HorizontalAlignment.CENTER);
      cellStyleCenter.setVerticalAlignment(VerticalAlignment.CENTER);
      cellStyleCenter.setBorderLeft(BorderStyle.THIN);
      cellStyleCenter.setBorderBottom(BorderStyle.THIN);
      cellStyleCenter.setBorderRight(BorderStyle.THIN);
      cellStyleCenter.setBorderTop(BorderStyle.THIN);
      cellStyleCenter.setWrapText(true);
      cellStyleRight.setFont(rowDataFont);

      CellStyle right = workbook.createCellStyle();
      right.setAlignment(HorizontalAlignment.RIGHT);
      right.setVerticalAlignment(VerticalAlignment.CENTER);
      right.setWrapText(true);

      CellStyle left = workbook.createCellStyle();
      left.setAlignment(HorizontalAlignment.LEFT);
      left.setVerticalAlignment(VerticalAlignment.CENTER);
      left.setWrapText(true);

      CellStyle center = workbook.createCellStyle();
      center.setAlignment(HorizontalAlignment.CENTER);
      center.setVerticalAlignment(VerticalAlignment.CENTER);
      center.setWrapText(true);
      //</editor-fold>

      for (ConfigFileExport item : config) {

        Map<String, String> fieldSplit = item.getFieldSplit();
        SXSSFSheet sheet;
        if (exportChart != null && exportChart.length > 0) {
          sheet = workbook.getSheetAt(0);
        } else {
          sheet = workbook.createSheet(item.getSheetName());
        }

        if (item.getCellTitleIndex() >= 3) {
          //TienNV them template header
          Row headerFirstTitle = sheet.createRow(0);
          Row headerSecondTitle = sheet.createRow(1);
          int sizeheader = 5;
          Cell firstLeftHeader = headerFirstTitle.createCell(1);
          firstLeftHeader.setCellStyle(cellStyleTopHeader);
          Cell secondLeftHeader = headerSecondTitle.createCell(1);
          secondLeftHeader.setCellStyle(cellStyleTopHeader);
          Cell firstRightHeader = headerFirstTitle.createCell(sizeheader - 1);
          firstRightHeader.setCellStyle(cellStyleTopRightHeader);
          Cell secondRightHeader = headerSecondTitle.createCell(sizeheader - 1);
          secondRightHeader.setCellStyle(cellStyleTopRightHeader);
          firstLeftHeader.setCellValue(
              StringUtils.isStringNullOrEmpty(item.getFirstLeftHeaderTitle()) ? ""
                  : item.getFirstLeftHeaderTitle());
          secondLeftHeader.setCellValue(
              StringUtils.isStringNullOrEmpty(item.getSecondLeftHeaderTitle()) ? ""
                  : item.getSecondLeftHeaderTitle());
          firstRightHeader.setCellValue(
              StringUtils.isStringNullOrEmpty(item.getFirstRightHeaderTitle()) ? ""
                  : item.getFirstRightHeaderTitle());
          secondRightHeader.setCellValue(
              StringUtils.isStringNullOrEmpty(item.getSecondRightHeaderTitle()) ? ""
                  : item.getSecondRightHeaderTitle());
          sheet.addMergedRegion(new CellRangeAddress(0, 0, 1,
              2));
          sheet.addMergedRegion(new CellRangeAddress(1, 1, 1,
              2));
          sheet.addMergedRegion(new CellRangeAddress(0, 0, sizeheader - 1,
              sizeheader));
          sheet.addMergedRegion(new CellRangeAddress(1, 1, sizeheader - 1,
              sizeheader));
          //end tiennv
        }

        // Title
        Row rowMainTitle = sheet.createRow(item.getCellTitleIndex());
        Cell mainCellTitle = rowMainTitle.createCell(1);
        mainCellTitle.setCellValue(item.getTitle() == null ? "" : item.getTitle());
        mainCellTitle.setCellStyle(cellStyleTitle);
        sheet.addMergedRegion(
            new CellRangeAddress(item.getCellTitleIndex(), item.getCellTitleIndex(), 1,
                item.getMergeTitleEndIndex()));

        // Sub title
        int indexSubTitle =
            (StringUtils.isStringNullOrEmpty(item.getSubTitle())) ? item.getCellTitleIndex() + 1
                : item.getCellTitleIndex() + 2;
        Row rowSubTitle = sheet.createRow(indexSubTitle);
        Cell cellTitle = rowSubTitle.createCell(1);
        cellTitle.setCellValue(item.getSubTitle() == null ? "" : item.getSubTitle());
        cellTitle.setCellStyle(cellStyleSubTitle);
        sheet.addMergedRegion(
            new CellRangeAddress(indexSubTitle, indexSubTitle, 1,
                item.getMergeTitleEndIndex()));

        String[] headerStar = new String[]{
            I18n.getLanguage("SrServiceArray.configCode"),
            I18n.getLanguage("SrServiceArray.configName"),
            I18n.getLanguage("SrServiceArray.countryCode"),
            I18n.getLanguage("SrServiceArray.action")
        };

        String[] commentHeader = new String[]{
            I18n.getLanguage("SrServiceArray.actionComment")
        };

        Font fontStar = workbook.createFont();
        fontStar.setColor(IndexedColors.RED.getIndex());
        fontStar.setFontName(HSSFFont.FONT_ARIAL);
        fontStar.setFontHeightInPoints((short) 20);
        fontStar.setBold(true);

        int indexRowData = 0;
        //<editor-fold defaultstate="collapsed" desc="Build header">
        if (item.isCreatHeader()) {
          int index = -1;
          Cell cellHeader = null;
          Row rowHeader = sheet.createRow(item.getStartRow());
          rowHeader.setHeight((short) 500);
          Row rowHeaderSub = null;

          for (ConfigHeaderExport header : item.getHeader()) {
            if (fieldSplit != null) {
              if (fieldSplit.get(header.getFieldName()) != null) {
                String[] fieldSplitHead = fieldSplit.get(header.getFieldName())
                    .split(item.getSplitChar());
                for (String field : fieldSplitHead) {
                  cellHeader = rowHeader.createCell(index + 2);
                  cellHeader.setCellValue(field == null ? "" : field.replaceAll("\\<.*?>", " "));
                  if (header.isHasMerge()) {
                    CellRangeAddress cellRangeAddress = new CellRangeAddress(
                        item.getStartRow(), item.getStartRow() + header.getMergeRow(),
                        index + 2, index + 2 + header.getMergeColumn());
                    sheet.addMergedRegion(cellRangeAddress);
                    RegionUtil.setBorderBottom(BorderStyle.THIN, cellRangeAddress,
                        sheet);
                    RegionUtil.setBorderLeft(BorderStyle.THIN, cellRangeAddress,
                        sheet);
                    RegionUtil.setBorderRight(BorderStyle.THIN, cellRangeAddress,
                        sheet);
                    RegionUtil.setBorderTop(BorderStyle.THIN, cellRangeAddress,
                        sheet);

                    if (header.getMergeRow() > 0) {
                      indexRowData = header.getMergeRow();
                    }
                    if (header.getMergeColumn() > 0) {
                      index++;
                    }

                    if (header.getSubHeader().length > 0) {
                      if (rowHeaderSub == null) {
                        rowHeaderSub = sheet.createRow(item.getStartRow() + 1);
                      }

                      int k = index + 1;
                      int s = 0;
                      for (String sub : header.getSubHeader()) {
                        Cell cellHeaderSub1 = rowHeaderSub.createCell(k);
                        cellHeaderSub1.setCellValue(
                            I18n.getString(item.getHeaderPrefix() + "." + sub));
                        cellHeaderSub1.setCellStyle(cellStyleHeader);

                        k++;
                        s++;
                      }
                    }
                  }
                  cellHeader.setCellStyle(cellStyleHeader);
                  index++;
                }
              } else {
                cellHeader = rowHeader.createCell(index + 2);
                cellHeader.setCellValue(
                    I18n.getString(item.getHeaderPrefix() + "." + header.getFieldName()));
                if (header.isHasMerge()) {
                  CellRangeAddress cellRangeAddress = new CellRangeAddress(
                      item.getStartRow(), item.getStartRow() + header.getMergeRow(),
                      index + 2, index + 2 + header.getMergeColumn());
                  sheet.addMergedRegion(cellRangeAddress);
                  RegionUtil
                      .setBorderBottom(BorderStyle.THIN, cellRangeAddress,
                          sheet);
                  RegionUtil
                      .setBorderLeft(BorderStyle.THIN, cellRangeAddress,
                          sheet);
                  RegionUtil
                      .setBorderRight(BorderStyle.THIN, cellRangeAddress,
                          sheet);
                  RegionUtil
                      .setBorderTop(BorderStyle.THIN, cellRangeAddress, sheet);

                  if (header.getMergeRow() > 0) {
                    indexRowData = header.getMergeRow();
                  }
                  if (header.getMergeColumn() > 0) {
                    index++;
                  }
                }
                cellHeader.setCellStyle(cellStyleHeader);
                index++;
              }
            } else {
              cellHeader = rowHeader.createCell(index + 2);
              cellHeader.setCellValue(
                  I18n.getString(item.getHeaderPrefix() + "." + header.getFieldName()));
              if (header.isHasMerge()) {
                CellRangeAddress cellRangeAddress = new CellRangeAddress(item.getStartRow(),
                    item.getStartRow() + header.getMergeRow(), index + 2,
                    index + 2 + header.getMergeColumn());
                sheet.addMergedRegion(cellRangeAddress);
                RegionUtil
                    .setBorderBottom(BorderStyle.THIN, cellRangeAddress, sheet);
                RegionUtil.setBorderLeft(BorderStyle.THIN, cellRangeAddress, sheet);
                RegionUtil
                    .setBorderRight(BorderStyle.THIN, cellRangeAddress, sheet);
                RegionUtil
                    .setBorderTop(BorderStyle.THIN, cellRangeAddress, sheet);

                if (header.getMergeRow() > 0) {
                  indexRowData = header.getMergeRow();
                }
                if (header.getMergeColumn() > 0) {
                  index++;
                }
              }
              cellHeader.setCellStyle(cellStyleHeader);
              index++;
            }

            SXSSFDrawing drawing = sheet.createDrawingPatriarch();

            if (cellHeader.getStringCellValue()
                .equalsIgnoreCase(I18n.getLanguage("SrServiceArray.action"))) {
              XSSFClientAnchor anchor = (XSSFClientAnchor) drawing
                  .createAnchor(0, 0, 0, 0, 5, 7, 7, 8);
              XSSFComment comment = (XSSFComment) drawing.createCellComment(anchor);
              comment.setString(commentHeader[0]);
              cellHeader.setCellComment(comment);
            }

            for (int i = 0; i < headerStar.length; i++) {
              XSSFRichTextString xssfRichTextString = new XSSFRichTextString(
                  cellHeader.getStringCellValue());
              if (cellHeader.getStringCellValue().equalsIgnoreCase(headerStar[i])) {
                xssfRichTextString.append(" (*) ", (XSSFFont) fontStar);
                cellHeader.setCellStyle(cellStyleHeader);
                cellHeader.setCellValue(xssfRichTextString);
                break;
              }
            }
          }
        }
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Build other cell">
        if (item.getLstCreatCell() != null) {
          Row row;
          for (CellConfigExport cell : item.getLstCreatCell()) {
            row = sheet.getRow(cell.getRow());
            if (row == null) {
              row = sheet.createRow(cell.getRow());
            }
            //row.setHeight((short) -1);
            Cell newCell = row.createCell(cell.getColumn());
            if ("NUMBER".equals(cell.getStyleFormat())) {
              newCell.setCellValue(Double.valueOf(cell.getValue()));
            } else {
              newCell.setCellValue(cell.getValue() == null ? "" : cell.getValue());
            }

            if (cell.getRowMerge() > 0 || cell.getColumnMerge() > 0) {
              CellRangeAddress cellRangeAddress = new CellRangeAddress(cell.getRow(),
                  cell.getRow() + cell.getRowMerge(), cell.getColumn(),
                  cell.getColumn() + cell.getColumnMerge());
              sheet.addMergedRegion(cellRangeAddress);
              RegionUtil
                  .setBorderBottom(BorderStyle.THIN, cellRangeAddress, sheet);
              RegionUtil
                  .setBorderLeft(BorderStyle.THIN, cellRangeAddress, sheet);
              RegionUtil
                  .setBorderRight(BorderStyle.THIN, cellRangeAddress, sheet);
              RegionUtil
                  .setBorderTop(BorderStyle.THIN, cellRangeAddress, sheet);
            }

            if ("HEAD".equals(cell.getAlign())) {
              newCell.setCellStyle(cellStyleHeader);
            }
            if ("CENTER".equals(cell.getAlign())) {
              newCell.setCellStyle(cellStyleCenter);
            }
            if ("LEFT".equals(cell.getAlign())) {
              newCell.setCellStyle(cellStyleLeft);
            }
            if ("RIGHT".equals(cell.getAlign())) {
              newCell.setCellStyle(cellStyleRight);
            }
            if ("CENTER_NONE_BORDER".equals(cell.getAlign())) {
              newCell.setCellStyle(center);
            }
            if ("LEFT_NONE_BORDER".equals(cell.getAlign())) {
              newCell.setCellStyle(left);
            }
            if ("RIGHT_NONE_BORDER".equals(cell.getAlign())) {
              newCell.setCellStyle(right);
            }
          }
        }
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Fill data">
        if (item.getLstData() != null && !item.getLstData().isEmpty()) {
          //init mapColumn
          Object firstRow = item.getLstData().get(0);
          Map<String, Field> mapField = new HashMap<String, Field>();
          for (ConfigHeaderExport header : item.getHeader()) {
            for (Field f : firstRow.getClass().getDeclaredFields()) {
              f.setAccessible(true);
              if (f.getName().equals(header.getFieldName())) {
                mapField.put(header.getFieldName(), f);
              }
              String[] replace = header.getReplace();
              if (replace != null) {
                if (replace.length > 2) {
                  for (int n = 2; n < replace.length; n++) {
                    if (f.getName().equals(replace[n])) {
                      mapField.put(replace[n], f);
                    }
                  }
                }
              }
            }
            if (firstRow.getClass().getSuperclass() != null) {
              for (Field f : firstRow.getClass()
                  .getSuperclass().getDeclaredFields()) {
                f.setAccessible(true);
                if (f.getName().equals(header.getFieldName())) {
                  mapField.put(header.getFieldName(), f);
                }
                String[] replace = header.getReplace();
                if (replace != null) {
                  if (replace.length > 2) {
                    for (int n = 2; n < replace.length; n++) {
                      if (f.getName().equals(replace[n])) {
                        mapField.put(replace[n], f);
                      }
                    }
                  }
                }
              }
            }
          }

          //fillData
          Row row;
          List lstData = item.getLstData();
          List<ConfigHeaderExport> lstHeader = item.getHeader();
          int startRow = item.getStartRow();
          String splitChar = item.getSplitChar();
          for (int i = 0; i < lstData.size(); i++) {
            row = sheet.createRow(i + startRow + 1 + indexRowData);
            row.setHeight((short) 250);
            //row.setHeight((short) -1);
            Cell cell;

            cell = row.createCell(0);
            cell.setCellValue(i + 1);
            cell.setCellStyle(cellStyleCenter);

            int j = 0;
            for (int e = 0; e < lstHeader.size(); e++) {
              ConfigHeaderExport head = lstHeader.get(e);
              String header = head.getFieldName();
              String align = head.getAlign();
              Object obj = lstData.get(i);

              Field f = mapField.get(header);

              if (fieldSplit != null && fieldSplit.containsKey(header)) {
                String[] arrHead = fieldSplit.get(header).split(splitChar);
                String value = "";
                Object tempValue = f.get(obj);
                if (tempValue instanceof Date) {
                  value = tempValue == null ? "" : DateUtil.date2ddMMyyyyHHMMss((Date) tempValue);
                } else {
                  value = tempValue == null ? "" : tempValue.toString();
                }

                String[] fieldSplitValue = value.split(splitChar);
                for (int m = 0; m < arrHead.length; m++) {
                  if (head.isHasMerge() && head.getSubHeader().length > 0) {
                    int s = 0;
                    for (String sub : head.getSubHeader()) {
                      cell = row.createCell(j + 1);
                      String[] replace = head.getReplace();
                      if (replace != null) {
                        List<String> more = new ArrayList<>();
                        if (replace.length > 2) {
                          for (int n = 2; n < replace.length; n++) {
                            Object objStr = mapField.get(replace[n]).get(obj);
                            String valueStr = objStr == null ? "" : objStr.toString();
                            more.add(valueStr);
                          }
                        }
                        if ("NUMBER".equals(head.getStyleFormat())) {
                          double numberValue = CommonExport.replaceNumberValue(replace[0], m,
                              more, s);
                          if (Double.compare(numberValue, -888) == 0) {
                            cell.setCellValue("*");
                          } else if (Double.compare(numberValue, -999) == 0) {
                            cell.setCellValue("-");
                          } else {
                            cell.setCellValue(numberValue);
                          }
                        } else {
                          cell.setCellValue(
                              CommonExport.replaceStringValue(replace[0], m, more, s));
                        }
                        s++;
                      } else {
                        String subValue = "";
                        for (Field subf : firstRow.getClass().getDeclaredFields()) {
                          subf.setAccessible(true);
                          if (subf.getName().equals(sub)) {
                            String[] arrSub = (subf.get(obj) == null ? "" : subf.get(
                                obj).toString()).split(item.getSplitChar());
                            subValue = arrSub[m];
                          }
                        }
                        if ("NUMBER".equals(head.getStyleFormat())) {
                          if (StringUtils.isNotNullOrEmpty(subValue)) {
                            cell.setCellValue(Double.valueOf(subValue));
                          } else {
                            cell.setCellValue(subValue == null ? "" : subValue);
                          }
                        } else {
                          cell.setCellValue(subValue == null ? "" : subValue);
                        }
                      }

                      if ("CENTER".equals(align)) {
                        cell.setCellStyle(cellStyleCenter);
                      }
                      if ("LEFT".equals(align)) {
                        cell.setCellStyle(cellStyleLeft);
                      }
                      if ("RIGHT".equals(align)) {
                        cell.setCellStyle(cellStyleRight);
                      }
                      j++;
                    }
                  } else {
                    cell = row.createCell(j + 1);

                    String[] replace = head.getReplace();
                    if (replace != null) {
                      Object valueReplace = mapField.get(replace[1]).get(obj);
                      List<String> more = new ArrayList<>();
                      if (replace.length > 2) {
                        for (int n = 2; n < replace.length; n++) {
                          Object objStr = mapField.get(replace[n]).get(obj);
                          String valueStr = objStr == null ? "" : objStr.toString();
                          more.add(valueStr);
                        }
                      }
                      if ("NUMBER".equals(head.getStyleFormat())) {
                        double numberValue = CommonExport.replaceNumberValue(replace[0],
                            valueReplace, more, m);
                        if (Double.compare(numberValue, -888) == 0) {
                          cell.setCellValue("*");
                        } else if (Double.compare(numberValue, -999) == 0) {
                          cell.setCellValue("-");
                        } else {
                          cell.setCellValue(numberValue);
                        }
                      } else {
                        cell.setCellValue(
                            CommonExport.replaceStringValue(replace[0], valueReplace, more, m));
                      }
                    } else {
                      if ("NUMBER".equals(head.getStyleFormat())) {
                        if (StringUtils.isNotNullOrEmpty(fieldSplitValue[m])) {
                          cell.setCellValue(Double.valueOf(fieldSplitValue[m]));
                        } else {
                          cell.setCellValue(fieldSplitValue[m] == null ? "" : fieldSplitValue[m]);
                        }
                      } else {
                        cell.setCellValue(fieldSplitValue[m] == null ? "" : fieldSplitValue[m]);
                      }
                    }

                    if ("CENTER".equals(align)) {
                      cell.setCellStyle(cellStyleCenter);
                    }
                    if ("LEFT".equals(align)) {
                      cell.setCellStyle(cellStyleLeft);
                    }
                    if ("RIGHT".equals(align)) {
                      cell.setCellStyle(cellStyleRight);
                    }
                    j++;
                  }
                }
              } else {
                String value = "";
                if (f != null) {
                  Object tempValue = f.get(obj);
                  if (tempValue instanceof Date) {
                    value = tempValue == null ? "" : DateUtil.date2ddMMyyyyHHMMss((Date) tempValue);
                  } else {
                    value = tempValue == null ? "" : tempValue.toString();
                  }
                }
                cell = row.createCell(j + 1);

                String[] replace = head.getReplace();
                if (replace != null) {
                  Object valueReplace = mapField.get(replace[1]).get(obj);
                  List<String> more = new ArrayList<>();
                  if (replace.length > 2) {
                    for (int n = 2; n < replace.length; n++) {
                      Object objStr = mapField.get(replace[n]).get(obj);
                      String valueStr = objStr == null ? "" : objStr.toString();
                      more.add(valueStr);
                    }
                  }
                  if ("NUMBER".equals(head.getStyleFormat())) {
                    double numberValue = CommonExport
                        .replaceNumberValue(replace[0], valueReplace, more,
                            i);
                    if (Double.compare(numberValue, -888) == 0) {
                      cell.setCellValue("*");
                    } else if (Double.compare(numberValue, -999) == 0) {
                      cell.setCellValue("-");
                    } else {
                      cell.setCellValue(numberValue);
                    }
                  } else {
                    cell.setCellValue(
                        CommonExport.replaceStringValue(replace[0], valueReplace, more, i));
                  }
                } else {
                  if ("NUMBER".equals(head.getStyleFormat())) {
                    if (StringUtils.isNotNullOrEmpty(value)) {
                      cell.setCellValue(Double.valueOf(value));
                    } else {
                      cell.setCellValue(value == null ? "" : value);
                    }
                  } else {
                    cell.setCellValue(value == null ? "" : value);
                  }
                }

                if ("CENTER".equals(align)) {
                  cell.setCellStyle(cellStyleCenter);
                }
                if ("LEFT".equals(align)) {
                  cell.setCellStyle(cellStyleLeft);
                }
                if ("RIGHT".equals(align)) {
                  cell.setCellStyle(cellStyleRight);
                }

                j++;
              }
            }
          }
        }
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Merge row">
        if (item.getLstCellMerge() != null) {
          for (CellConfigExport cell : item.getLstCellMerge()) {
            if (cell.getRowMerge() > 0 || cell.getColumnMerge() > 0) {
              CellRangeAddress cellRangeAddress = new CellRangeAddress(cell.getRow(),
                  cell.getRow() + cell.getRowMerge(), cell.getColumn(),
                  cell.getColumn() + cell.getColumnMerge());
              sheet.addMergedRegion(cellRangeAddress);
              RegionUtil
                  .setBorderBottom(BorderStyle.THIN, cellRangeAddress, sheet);
              RegionUtil
                  .setBorderLeft(BorderStyle.THIN, cellRangeAddress, sheet);
              RegionUtil
                  .setBorderRight(BorderStyle.THIN, cellRangeAddress, sheet);
              RegionUtil
                  .setBorderTop(BorderStyle.THIN, cellRangeAddress, sheet);
            }
          }
        }
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Auto size column">
        sheet.trackAllColumnsForAutoSizing();
        for (int i = 0; i <= item.getHeader().size(); i++) {
          sheet.autoSizeColumn(i);
          if (sheet.getColumnWidth(i) > 20000) {
            sheet.setColumnWidth(i, 20000);
          }
        }
        //</editor-fold>
      }

      createComboArrayService(workbook);

      if (exportChart == null || exportChart.length == 0) {
        workbook.removeSheetAt(0);
      }

      if (exportChart != null && exportChart.length > 0) {
        //<editor-fold defaultstate="collapsed" desc="Ve bieu do">
        ConfigFileExport item = config.get(0);
        Sheet sheetConf = workbook_temp.getSheet("conf");

        // Dong bat dau du lieu cua chart
        Row rowStartConf = sheetConf.getRow(0);
        Cell cellStartConf = rowStartConf.getCell(1);
        cellStartConf.setCellValue(item.getStartRow() + 1);

        // Dong ket thuc du lieu cua chart
        Row rowEndConf = sheetConf.getRow(1);
        Cell cellEndConf = rowEndConf.getCell(1);
        cellEndConf.setCellValue(item.getStartRow() + 1 + item.getLstData().size());

        // Cot bat dau du lieu cua chart
        String xStart = "";

        // Cot ket thuc du lieu cua chart
        String xEnd = "";

        // xAxis
        Row rowXvalue = sheetConf.getRow(2);
        Cell cellXvalue = rowXvalue.getCell(1);
        cellXvalue.setCellValue("=" + I18n.getLanguage("common.export.char.total",
            Locale.forLanguageTag("vi")) + "!$" + xStart + "${startRow}:$" + xEnd + "${startRow}");

        // Categories
        Row rowNameList = sheetConf.getRow(3);
        Cell cellNameList = rowNameList.getCell(1);
        cellNameList.setCellValue("=" + I18n.getLanguage("common.export.char.total",
            Locale.forLanguageTag("vi")) + "!$" + exportChart[0] + "${i}");

        // Data
        Row rowDataValue = sheetConf.getRow(4);
        Cell cellDataValue = rowDataValue.getCell(1);
        cellDataValue.setCellValue("=" + I18n.getLanguage("common.export.char.total",
            Locale.forLanguageTag("vi")) + "!$" + xStart + "${i}:$" + xEnd + "${i}");
        //</editor-fold>
      }

      try {
        FileOutputStream fileOut = new FileOutputStream(pathOut);
        workbook.write(fileOut);
        fileOut.flush();
        fileOut.close();
      } catch (IOException e) {
        log.error(e.getMessage(), e);
      }
    } catch (FileNotFoundException e) {
      log.error(e.getMessage(), e);
    }
    return new File(pathOut);
  }

  public File exportExcelServiceGroup(
      String pathTemplate,
      String fileNameOut,
      List<ConfigFileExport> config,
      String pathOut,
      String... exportChart
  ) throws Exception {
    File folderOut = new File(pathOut);
    if (!folderOut.exists()) {
      folderOut.mkdir();
    }
    SimpleDateFormat dateFormat = new SimpleDateFormat();
    dateFormat.applyPattern("dd/MM/yyyy HH:mm:ss");
    String strCurTimeExp = dateFormat.format(new Date());
    strCurTimeExp = strCurTimeExp.replaceAll("/", "_");
    strCurTimeExp = strCurTimeExp.replaceAll(" ", "_");
    strCurTimeExp = strCurTimeExp.replaceAll(":", "_");
    pathOut = pathOut + fileNameOut + strCurTimeExp + (exportChart != null && exportChart.length > 0
        ? XLSM_FILE_EXTENTION : XLSX_FILE_EXTENTION);
    try {
      log.info("Start get template file!");
      pathTemplate = StringUtils.removeSeparator(pathTemplate);
      Resource resource = new ClassPathResource(pathTemplate);
      InputStream fileTemplate = resource.getInputStream();
      XSSFWorkbook workbook_temp = new XSSFWorkbook(fileTemplate);
      log.info("End get template file!");
      SXSSFWorkbook workbook = new SXSSFWorkbook(workbook_temp, 1000);

      //<editor-fold defaultstate="collapsed" desc="Declare style">
      CellStyle cellStyleFormatNumber = workbook.createCellStyle();
      cellStyleFormatNumber.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
      cellStyleFormatNumber.setAlignment(HorizontalAlignment.RIGHT);
      cellStyleFormatNumber.setBorderLeft(BorderStyle.THIN);
      cellStyleFormatNumber.setBorderBottom(BorderStyle.THIN);
      cellStyleFormatNumber.setBorderRight(BorderStyle.THIN);
      cellStyleFormatNumber.setBorderTop(BorderStyle.THIN);

      CellStyle cellStyle = workbook.createCellStyle();
      cellStyle.setAlignment(HorizontalAlignment.CENTER);
      cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
      cellStyle.setBorderLeft(BorderStyle.THIN);
      cellStyle.setBorderBottom(BorderStyle.THIN);
      cellStyle.setBorderRight(BorderStyle.THIN);
      cellStyle.setBorderTop(BorderStyle.THIN);
      cellStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.index);
      cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
      cellStyle.setWrapText(true);

      Font xSSFFont = workbook.createFont();
      xSSFFont.setFontName(HSSFFont.FONT_ARIAL);
      xSSFFont.setFontHeightInPoints((short) 20);
      xSSFFont.setBold(true);
      xSSFFont.setColor(IndexedColors.BLACK.index);

      CellStyle cellStyleTitle = workbook.createCellStyle();
      cellStyleTitle.setAlignment(HorizontalAlignment.CENTER);
      cellStyleTitle.setVerticalAlignment(VerticalAlignment.CENTER);
      cellStyleTitle.setFillForegroundColor(IndexedColors.WHITE.index);
      cellStyleTitle.setFont(xSSFFont);

      Font xSSFFontHeader = workbook.createFont();
      xSSFFontHeader.setFontName("Times New Roman");
      xSSFFontHeader.setFontHeightInPoints((short) 10);
      xSSFFontHeader.setColor(IndexedColors.BLUE.index);
      xSSFFontHeader.setBold(true);

      Font subTitleFont = workbook.createFont();
      subTitleFont.setFontName(HSSFFont.FONT_ARIAL);
      subTitleFont.setFontHeightInPoints((short) 10);
      subTitleFont.setColor(IndexedColors.BLACK.index);

      Font xssFontTopHeader = workbook.createFont();
      xssFontTopHeader.setFontName("Times New Roman");
      xssFontTopHeader.setFontHeightInPoints((short) 10);
      xssFontTopHeader.setColor(IndexedColors.BLACK.index);
      xssFontTopHeader.setBold(true);

      Font rowDataFont = workbook.createFont();
      rowDataFont.setFontName(HSSFFont.FONT_ARIAL);
      rowDataFont.setFontHeightInPoints((short) 10);
      rowDataFont.setColor(IndexedColors.BLACK.index);

      CellStyle cellStyleTopHeader = workbook.createCellStyle();
      cellStyleTopHeader.setAlignment(HorizontalAlignment.CENTER);
      cellStyleTopHeader.setVerticalAlignment(VerticalAlignment.CENTER);
      cellStyleTopHeader.setFont(xssFontTopHeader);

      CellStyle cellStyleTopRightHeader = workbook.createCellStyle();
      cellStyleTopRightHeader.setAlignment(HorizontalAlignment.LEFT);
      cellStyleTopRightHeader.setVerticalAlignment(VerticalAlignment.CENTER);
      cellStyleTopRightHeader.setFont(xssFontTopHeader);

      CellStyle cellStyleSubTitle = workbook.createCellStyle();
      cellStyleSubTitle.setAlignment(HorizontalAlignment.CENTER);
      cellStyleSubTitle.setVerticalAlignment(VerticalAlignment.CENTER);
      cellStyleSubTitle.setFont(subTitleFont);

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

      CellStyle cellStyleLeft = workbook.createCellStyle();
      cellStyleLeft.setAlignment(HorizontalAlignment.LEFT);
      cellStyleLeft.setVerticalAlignment(VerticalAlignment.CENTER);
      cellStyleLeft.setBorderLeft(BorderStyle.THIN);
      cellStyleLeft.setBorderBottom(BorderStyle.THIN);
      cellStyleLeft.setBorderRight(BorderStyle.THIN);
      cellStyleLeft.setBorderTop(BorderStyle.THIN);
      cellStyleLeft.setWrapText(true);
      cellStyleLeft.setFont(rowDataFont);
      CellStyle cellStyleRight = workbook.createCellStyle();
      cellStyleRight.setAlignment(HorizontalAlignment.RIGHT);
      cellStyleRight.setVerticalAlignment(VerticalAlignment.CENTER);
      cellStyleRight.setBorderLeft(BorderStyle.THIN);
      cellStyleRight.setBorderBottom(BorderStyle.THIN);
      cellStyleRight.setBorderRight(BorderStyle.THIN);
      cellStyleRight.setBorderTop(BorderStyle.THIN);
      cellStyleRight.setWrapText(true);
      cellStyleRight.setFont(rowDataFont);
      //gnoc_cr
      CellStyle cellStyleHeaderOver = workbook.createCellStyle();
      cellStyleHeaderOver.setAlignment(HorizontalAlignment.LEFT);
      cellStyleHeaderOver.setVerticalAlignment(VerticalAlignment.CENTER);
      cellStyleHeaderOver.setBorderLeft(BorderStyle.THIN);
      cellStyleHeaderOver.setBorderBottom(BorderStyle.THIN);
      cellStyleHeaderOver.setBorderRight(BorderStyle.THIN);
      cellStyleHeaderOver.setBorderTop(BorderStyle.THIN);
      cellStyleHeaderOver.setFillForegroundColor(IndexedColors.LIGHT_GREEN.index);
      cellStyleHeaderOver.setFillPattern(FillPatternType.SOLID_FOREGROUND);
      cellStyleHeaderOver.setWrapText(true);
      cellStyleHeaderOver.setFont(xSSFFontHeader);

      CellStyle cellStyleCenter = workbook.createCellStyle();
      cellStyleCenter.setAlignment(HorizontalAlignment.CENTER);
      cellStyleCenter.setVerticalAlignment(VerticalAlignment.CENTER);
      cellStyleCenter.setBorderLeft(BorderStyle.THIN);
      cellStyleCenter.setBorderBottom(BorderStyle.THIN);
      cellStyleCenter.setBorderRight(BorderStyle.THIN);
      cellStyleCenter.setBorderTop(BorderStyle.THIN);
      cellStyleCenter.setWrapText(true);
      cellStyleRight.setFont(rowDataFont);

      CellStyle right = workbook.createCellStyle();
      right.setAlignment(HorizontalAlignment.RIGHT);
      right.setVerticalAlignment(VerticalAlignment.CENTER);
      right.setWrapText(true);

      CellStyle left = workbook.createCellStyle();
      left.setAlignment(HorizontalAlignment.LEFT);
      left.setVerticalAlignment(VerticalAlignment.CENTER);
      left.setWrapText(true);

      CellStyle center = workbook.createCellStyle();
      center.setAlignment(HorizontalAlignment.CENTER);
      center.setVerticalAlignment(VerticalAlignment.CENTER);
      center.setWrapText(true);
      //</editor-fold>

      for (ConfigFileExport item : config) {

        Map<String, String> fieldSplit = item.getFieldSplit();
        SXSSFSheet sheet;
        if (exportChart != null && exportChart.length > 0) {
          sheet = workbook.getSheetAt(0);
        } else {
          sheet = workbook.createSheet(item.getSheetName());
        }

        if (item.getCellTitleIndex() >= 3) {
          //TienNV them template header
          Row headerFirstTitle = sheet.createRow(0);
          Row headerSecondTitle = sheet.createRow(1);
          int sizeheader = 5;
          Cell firstLeftHeader = headerFirstTitle.createCell(1);
          firstLeftHeader.setCellStyle(cellStyleTopHeader);
          Cell secondLeftHeader = headerSecondTitle.createCell(1);
          secondLeftHeader.setCellStyle(cellStyleTopHeader);
          Cell firstRightHeader = headerFirstTitle.createCell(sizeheader - 1);
          firstRightHeader.setCellStyle(cellStyleTopRightHeader);
          Cell secondRightHeader = headerSecondTitle.createCell(sizeheader - 1);
          secondRightHeader.setCellStyle(cellStyleTopRightHeader);
          firstLeftHeader.setCellValue(
              StringUtils.isStringNullOrEmpty(item.getFirstLeftHeaderTitle()) ? ""
                  : item.getFirstLeftHeaderTitle());
          secondLeftHeader.setCellValue(
              StringUtils.isStringNullOrEmpty(item.getSecondLeftHeaderTitle()) ? ""
                  : item.getSecondLeftHeaderTitle());
          firstRightHeader.setCellValue(
              StringUtils.isStringNullOrEmpty(item.getFirstRightHeaderTitle()) ? ""
                  : item.getFirstRightHeaderTitle());
          secondRightHeader.setCellValue(
              StringUtils.isStringNullOrEmpty(item.getSecondRightHeaderTitle()) ? ""
                  : item.getSecondRightHeaderTitle());
          sheet.addMergedRegion(new CellRangeAddress(0, 0, 1,
              2));
          sheet.addMergedRegion(new CellRangeAddress(1, 1, 1,
              2));
          sheet.addMergedRegion(new CellRangeAddress(0, 0, sizeheader - 1,
              sizeheader));
          sheet.addMergedRegion(new CellRangeAddress(1, 1, sizeheader - 1,
              sizeheader));
          //end tiennv
        }

        // Title
        Row rowMainTitle = sheet.createRow(item.getCellTitleIndex());
        Cell mainCellTitle = rowMainTitle.createCell(1);
        mainCellTitle.setCellValue(item.getTitle() == null ? "" : item.getTitle());
        mainCellTitle.setCellStyle(cellStyleTitle);
        sheet.addMergedRegion(
            new CellRangeAddress(item.getCellTitleIndex(), item.getCellTitleIndex(), 1,
                item.getMergeTitleEndIndex()));

        // Sub title
        int indexSubTitle =
            (StringUtils.isStringNullOrEmpty(item.getSubTitle())) ? item.getCellTitleIndex() + 1
                : item.getCellTitleIndex() + 2;
        Row rowSubTitle = sheet.createRow(indexSubTitle);
        Cell cellTitle = rowSubTitle.createCell(1);
        cellTitle.setCellValue(item.getSubTitle() == null ? "" : item.getSubTitle());
        cellTitle.setCellStyle(cellStyleSubTitle);
        sheet.addMergedRegion(
            new CellRangeAddress(indexSubTitle, indexSubTitle, 1,
                item.getMergeTitleEndIndex()));

        String[] headerStar = new String[]{
            I18n.getLanguage("SrServiceArray.configCode"),
            I18n.getLanguage("SrServiceGroup.configCode"),
            I18n.getLanguage("SrServiceGroup.configName"),
            I18n.getLanguage("SrServiceArray.countryCode"),
            I18n.getLanguage("SrServiceGroup.action")
        };

        String[] commentHeader = new String[]{
            I18n.getLanguage("SrServiceGroup.actionComment")
        };

        Font fontStar = workbook.createFont();
        fontStar.setColor(IndexedColors.RED.getIndex());
        fontStar.setFontName(HSSFFont.FONT_ARIAL);
        fontStar.setFontHeightInPoints((short) 20);
        fontStar.setBold(true);

        int indexRowData = 0;
        //<editor-fold defaultstate="collapsed" desc="Build header">
        if (item.isCreatHeader()) {
          int index = -1;
          Cell cellHeader = null;
          Row rowHeader = sheet.createRow(item.getStartRow());
          rowHeader.setHeight((short) 500);
          Row rowHeaderSub = null;

          for (ConfigHeaderExport header : item.getHeader()) {
            if (fieldSplit != null) {
              if (fieldSplit.get(header.getFieldName()) != null) {
                String[] fieldSplitHead = fieldSplit.get(header.getFieldName())
                    .split(item.getSplitChar());
                for (String field : fieldSplitHead) {
                  cellHeader = rowHeader.createCell(index + 2);
                  cellHeader.setCellValue(field == null ? "" : field.replaceAll("\\<.*?>", " "));
                  if (header.isHasMerge()) {
                    CellRangeAddress cellRangeAddress = new CellRangeAddress(
                        item.getStartRow(), item.getStartRow() + header.getMergeRow(),
                        index + 2, index + 2 + header.getMergeColumn());
                    sheet.addMergedRegion(cellRangeAddress);
                    RegionUtil.setBorderBottom(BorderStyle.THIN, cellRangeAddress,
                        sheet);
                    RegionUtil.setBorderLeft(BorderStyle.THIN, cellRangeAddress,
                        sheet);
                    RegionUtil.setBorderRight(BorderStyle.THIN, cellRangeAddress,
                        sheet);
                    RegionUtil.setBorderTop(BorderStyle.THIN, cellRangeAddress,
                        sheet);

                    if (header.getMergeRow() > 0) {
                      indexRowData = header.getMergeRow();
                    }
                    if (header.getMergeColumn() > 0) {
                      index++;
                    }

                    if (header.getSubHeader().length > 0) {
                      if (rowHeaderSub == null) {
                        rowHeaderSub = sheet.createRow(item.getStartRow() + 1);
                      }

                      int k = index + 1;
                      int s = 0;
                      for (String sub : header.getSubHeader()) {
                        Cell cellHeaderSub1 = rowHeaderSub.createCell(k);
                        cellHeaderSub1.setCellValue(
                            I18n.getString(item.getHeaderPrefix() + "." + sub));
                        cellHeaderSub1.setCellStyle(cellStyleHeader);

                        k++;
                        s++;
                      }
                    }
                  }
                  cellHeader.setCellStyle(cellStyleHeader);
                  index++;
                }
              } else {
                cellHeader = rowHeader.createCell(index + 2);
                cellHeader.setCellValue(
                    I18n.getString(item.getHeaderPrefix() + "." + header.getFieldName()));
                if (header.isHasMerge()) {
                  CellRangeAddress cellRangeAddress = new CellRangeAddress(
                      item.getStartRow(), item.getStartRow() + header.getMergeRow(),
                      index + 2, index + 2 + header.getMergeColumn());
                  sheet.addMergedRegion(cellRangeAddress);
                  RegionUtil
                      .setBorderBottom(BorderStyle.THIN, cellRangeAddress,
                          sheet);
                  RegionUtil
                      .setBorderLeft(BorderStyle.THIN, cellRangeAddress,
                          sheet);
                  RegionUtil
                      .setBorderRight(BorderStyle.THIN, cellRangeAddress,
                          sheet);
                  RegionUtil
                      .setBorderTop(BorderStyle.THIN, cellRangeAddress, sheet);

                  if (header.getMergeRow() > 0) {
                    indexRowData = header.getMergeRow();
                  }
                  if (header.getMergeColumn() > 0) {
                    index++;
                  }
                }
                cellHeader.setCellStyle(cellStyleHeader);
                index++;
              }
            } else {
              cellHeader = rowHeader.createCell(index + 2);
              cellHeader.setCellValue(
                  I18n.getString(item.getHeaderPrefix() + "." + header.getFieldName()));
              if (header.isHasMerge()) {
                CellRangeAddress cellRangeAddress = new CellRangeAddress(item.getStartRow(),
                    item.getStartRow() + header.getMergeRow(), index + 2,
                    index + 2 + header.getMergeColumn());
                sheet.addMergedRegion(cellRangeAddress);
                RegionUtil
                    .setBorderBottom(BorderStyle.THIN, cellRangeAddress, sheet);
                RegionUtil.setBorderLeft(BorderStyle.THIN, cellRangeAddress, sheet);
                RegionUtil
                    .setBorderRight(BorderStyle.THIN, cellRangeAddress, sheet);
                RegionUtil
                    .setBorderTop(BorderStyle.THIN, cellRangeAddress, sheet);

                if (header.getMergeRow() > 0) {
                  indexRowData = header.getMergeRow();
                }
                if (header.getMergeColumn() > 0) {
                  index++;
                }
              }
              cellHeader.setCellStyle(cellStyleHeader);
              index++;
            }

            SXSSFDrawing drawing = sheet.createDrawingPatriarch();

            if (cellHeader.getStringCellValue()
                .equalsIgnoreCase(I18n.getLanguage("SrServiceArray.action"))) {
              XSSFClientAnchor anchor = (XSSFClientAnchor) drawing
                  .createAnchor(0, 0, 0, 0, 5, 7, 7, 8);
              XSSFComment comment = (XSSFComment) drawing.createCellComment(anchor);
              comment.setString(commentHeader[0]);
              cellHeader.setCellComment(comment);
            }

            for (int i = 0; i < headerStar.length; i++) {
              XSSFRichTextString xssfRichTextString = new XSSFRichTextString(
                  cellHeader.getStringCellValue());
              if (cellHeader.getStringCellValue().equalsIgnoreCase(headerStar[i])) {
                xssfRichTextString.append(" (*) ", (XSSFFont) fontStar);
                cellHeader.setCellStyle(cellStyleHeader);
                cellHeader.setCellValue(xssfRichTextString);
                break;
              }
            }
          }
        }
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Build other cell">
        if (item.getLstCreatCell() != null) {
          Row row;
          for (CellConfigExport cell : item.getLstCreatCell()) {
            row = sheet.getRow(cell.getRow());
            if (row == null) {
              row = sheet.createRow(cell.getRow());
            }
            //row.setHeight((short) -1);
            Cell newCell = row.createCell(cell.getColumn());
            if ("NUMBER".equals(cell.getStyleFormat())) {
              newCell.setCellValue(Double.valueOf(cell.getValue()));
            } else {
              newCell.setCellValue(cell.getValue() == null ? "" : cell.getValue());
            }

            if (cell.getRowMerge() > 0 || cell.getColumnMerge() > 0) {
              CellRangeAddress cellRangeAddress = new CellRangeAddress(cell.getRow(),
                  cell.getRow() + cell.getRowMerge(), cell.getColumn(),
                  cell.getColumn() + cell.getColumnMerge());
              sheet.addMergedRegion(cellRangeAddress);
              RegionUtil
                  .setBorderBottom(BorderStyle.THIN, cellRangeAddress, sheet);
              RegionUtil
                  .setBorderLeft(BorderStyle.THIN, cellRangeAddress, sheet);
              RegionUtil
                  .setBorderRight(BorderStyle.THIN, cellRangeAddress, sheet);
              RegionUtil
                  .setBorderTop(BorderStyle.THIN, cellRangeAddress, sheet);
            }

            if ("HEAD".equals(cell.getAlign())) {
              newCell.setCellStyle(cellStyleHeader);
            }
            if ("CENTER".equals(cell.getAlign())) {
              newCell.setCellStyle(cellStyleCenter);
            }
            if ("LEFT".equals(cell.getAlign())) {
              newCell.setCellStyle(cellStyleLeft);
            }
            if ("RIGHT".equals(cell.getAlign())) {
              newCell.setCellStyle(cellStyleRight);
            }
            if ("CENTER_NONE_BORDER".equals(cell.getAlign())) {
              newCell.setCellStyle(center);
            }
            if ("LEFT_NONE_BORDER".equals(cell.getAlign())) {
              newCell.setCellStyle(left);
            }
            if ("RIGHT_NONE_BORDER".equals(cell.getAlign())) {
              newCell.setCellStyle(right);
            }
          }
        }
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Fill data">
        if (item.getLstData() != null && !item.getLstData().isEmpty()) {
          //init mapColumn
          Object firstRow = item.getLstData().get(0);
          Map<String, Field> mapField = new HashMap<String, Field>();
          for (ConfigHeaderExport header : item.getHeader()) {
            for (Field f : firstRow.getClass().getDeclaredFields()) {
              f.setAccessible(true);
              if (f.getName().equals(header.getFieldName())) {
                mapField.put(header.getFieldName(), f);
              }
              String[] replace = header.getReplace();
              if (replace != null) {
                if (replace.length > 2) {
                  for (int n = 2; n < replace.length; n++) {
                    if (f.getName().equals(replace[n])) {
                      mapField.put(replace[n], f);
                    }
                  }
                }
              }
            }
            if (firstRow.getClass().getSuperclass() != null) {
              for (Field f : firstRow.getClass()
                  .getSuperclass().getDeclaredFields()) {
                f.setAccessible(true);
                if (f.getName().equals(header.getFieldName())) {
                  mapField.put(header.getFieldName(), f);
                }
                String[] replace = header.getReplace();
                if (replace != null) {
                  if (replace.length > 2) {
                    for (int n = 2; n < replace.length; n++) {
                      if (f.getName().equals(replace[n])) {
                        mapField.put(replace[n], f);
                      }
                    }
                  }
                }
              }
            }
          }

          //fillData
          Row row;
          List lstData = item.getLstData();
          List<ConfigHeaderExport> lstHeader = item.getHeader();
          int startRow = item.getStartRow();
          String splitChar = item.getSplitChar();
          for (int i = 0; i < lstData.size(); i++) {
            row = sheet.createRow(i + startRow + 1 + indexRowData);
            row.setHeight((short) 250);
            //row.setHeight((short) -1);
            Cell cell;

            cell = row.createCell(0);
            cell.setCellValue(i + 1);
            cell.setCellStyle(cellStyleCenter);

            int j = 0;
            for (int e = 0; e < lstHeader.size(); e++) {
              ConfigHeaderExport head = lstHeader.get(e);
              String header = head.getFieldName();
              String align = head.getAlign();
              Object obj = lstData.get(i);

              Field f = mapField.get(header);

              if (fieldSplit != null && fieldSplit.containsKey(header)) {
                String[] arrHead = fieldSplit.get(header).split(splitChar);
                String value = "";
                Object tempValue = f.get(obj);
                if (tempValue instanceof Date) {
                  value = tempValue == null ? "" : DateUtil.date2ddMMyyyyHHMMss((Date) tempValue);
                } else {
                  value = tempValue == null ? "" : tempValue.toString();
                }

                String[] fieldSplitValue = value.split(splitChar);
                for (int m = 0; m < arrHead.length; m++) {
                  if (head.isHasMerge() && head.getSubHeader().length > 0) {
                    int s = 0;
                    for (String sub : head.getSubHeader()) {
                      cell = row.createCell(j + 1);
                      String[] replace = head.getReplace();
                      if (replace != null) {
                        List<String> more = new ArrayList<>();
                        if (replace.length > 2) {
                          for (int n = 2; n < replace.length; n++) {
                            Object objStr = mapField.get(replace[n]).get(obj);
                            String valueStr = objStr == null ? "" : objStr.toString();
                            more.add(valueStr);
                          }
                        }
                        if ("NUMBER".equals(head.getStyleFormat())) {
                          double numberValue = CommonExport.replaceNumberValue(replace[0], m,
                              more, s);
                          if (Double.compare(numberValue, -888) == 0) {
                            cell.setCellValue("*");
                          } else if (Double.compare(numberValue, -999) == 0) {
                            cell.setCellValue("-");
                          } else {
                            cell.setCellValue(numberValue);
                          }
                        } else {
                          cell.setCellValue(
                              CommonExport.replaceStringValue(replace[0], m, more, s));
                        }
                        s++;
                      } else {
                        String subValue = "";
                        for (Field subf : firstRow.getClass().getDeclaredFields()) {
                          subf.setAccessible(true);
                          if (subf.getName().equals(sub)) {
                            String[] arrSub = (subf.get(obj) == null ? "" : subf.get(
                                obj).toString()).split(item.getSplitChar());
                            subValue = arrSub[m];
                          }
                        }
                        if ("NUMBER".equals(head.getStyleFormat())) {
                          if (StringUtils.isNotNullOrEmpty(subValue)) {
                            cell.setCellValue(Double.valueOf(subValue));
                          } else {
                            cell.setCellValue(subValue == null ? "" : subValue);
                          }
                        } else {
                          cell.setCellValue(subValue == null ? "" : subValue);
                        }
                      }

                      if ("CENTER".equals(align)) {
                        cell.setCellStyle(cellStyleCenter);
                      }
                      if ("LEFT".equals(align)) {
                        cell.setCellStyle(cellStyleLeft);
                      }
                      if ("RIGHT".equals(align)) {
                        cell.setCellStyle(cellStyleRight);
                      }
                      j++;
                    }
                  } else {
                    cell = row.createCell(j + 1);

                    String[] replace = head.getReplace();
                    if (replace != null) {
                      Object valueReplace = mapField.get(replace[1]).get(obj);
                      List<String> more = new ArrayList<>();
                      if (replace.length > 2) {
                        for (int n = 2; n < replace.length; n++) {
                          Object objStr = mapField.get(replace[n]).get(obj);
                          String valueStr = objStr == null ? "" : objStr.toString();
                          more.add(valueStr);
                        }
                      }
                      if ("NUMBER".equals(head.getStyleFormat())) {
                        double numberValue = CommonExport.replaceNumberValue(replace[0],
                            valueReplace, more, m);
                        if (Double.compare(numberValue, -888) == 0) {
                          cell.setCellValue("*");
                        } else if (Double.compare(numberValue, -999) == 0) {
                          cell.setCellValue("-");
                        } else {
                          cell.setCellValue(numberValue);
                        }
                      } else {
                        cell.setCellValue(
                            CommonExport.replaceStringValue(replace[0], valueReplace, more, m));
                      }
                    } else {
                      if ("NUMBER".equals(head.getStyleFormat())) {
                        if (StringUtils.isNotNullOrEmpty(fieldSplitValue[m])) {
                          cell.setCellValue(Double.valueOf(fieldSplitValue[m]));
                        } else {
                          cell.setCellValue(fieldSplitValue[m] == null ? "" : fieldSplitValue[m]);
                        }
                      } else {
                        cell.setCellValue(fieldSplitValue[m] == null ? "" : fieldSplitValue[m]);
                      }
                    }

                    if ("CENTER".equals(align)) {
                      cell.setCellStyle(cellStyleCenter);
                    }
                    if ("LEFT".equals(align)) {
                      cell.setCellStyle(cellStyleLeft);
                    }
                    if ("RIGHT".equals(align)) {
                      cell.setCellStyle(cellStyleRight);
                    }
                    j++;
                  }
                }
              } else {
                String value = "";
                if (f != null) {
                  Object tempValue = f.get(obj);
                  if (tempValue instanceof Date) {
                    value = tempValue == null ? "" : DateUtil.date2ddMMyyyyHHMMss((Date) tempValue);
                  } else {
                    value = tempValue == null ? "" : tempValue.toString();
                  }
                }
                cell = row.createCell(j + 1);

                String[] replace = head.getReplace();
                if (replace != null) {
                  Object valueReplace = mapField.get(replace[1]).get(obj);
                  List<String> more = new ArrayList<>();
                  if (replace.length > 2) {
                    for (int n = 2; n < replace.length; n++) {
                      Object objStr = mapField.get(replace[n]).get(obj);
                      String valueStr = objStr == null ? "" : objStr.toString();
                      more.add(valueStr);
                    }
                  }
                  if ("NUMBER".equals(head.getStyleFormat())) {
                    double numberValue = CommonExport
                        .replaceNumberValue(replace[0], valueReplace, more,
                            i);
                    if (Double.compare(numberValue, -888) == 0) {
                      cell.setCellValue("*");
                    } else if (Double.compare(numberValue, -999) == 0) {
                      cell.setCellValue("-");
                    } else {
                      cell.setCellValue(numberValue);
                    }
                  } else {
                    cell.setCellValue(
                        CommonExport.replaceStringValue(replace[0], valueReplace, more, i));
                  }
                } else {
                  if ("NUMBER".equals(head.getStyleFormat())) {
                    if (StringUtils.isNotNullOrEmpty(value)) {
                      cell.setCellValue(Double.valueOf(value));
                    } else {
                      cell.setCellValue(value == null ? "" : value);
                    }
                  } else {
                    cell.setCellValue(value == null ? "" : value);
                  }
                }

                if ("CENTER".equals(align)) {
                  cell.setCellStyle(cellStyleCenter);
                }
                if ("LEFT".equals(align)) {
                  cell.setCellStyle(cellStyleLeft);
                }
                if ("RIGHT".equals(align)) {
                  cell.setCellStyle(cellStyleRight);
                }

                j++;
              }
            }
          }
        }
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Merge row">
        if (item.getLstCellMerge() != null) {
          for (CellConfigExport cell : item.getLstCellMerge()) {
            if (cell.getRowMerge() > 0 || cell.getColumnMerge() > 0) {
              CellRangeAddress cellRangeAddress = new CellRangeAddress(cell.getRow(),
                  cell.getRow() + cell.getRowMerge(), cell.getColumn(),
                  cell.getColumn() + cell.getColumnMerge());
              sheet.addMergedRegion(cellRangeAddress);
              RegionUtil
                  .setBorderBottom(BorderStyle.THIN, cellRangeAddress, sheet);
              RegionUtil
                  .setBorderLeft(BorderStyle.THIN, cellRangeAddress, sheet);
              RegionUtil
                  .setBorderRight(BorderStyle.THIN, cellRangeAddress, sheet);
              RegionUtil
                  .setBorderTop(BorderStyle.THIN, cellRangeAddress, sheet);
            }
          }
        }
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Auto size column">
        sheet.trackAllColumnsForAutoSizing();
        for (int i = 0; i <= item.getHeader().size(); i++) {
          sheet.autoSizeColumn(i);
          if (sheet.getColumnWidth(i) > 20000) {
            sheet.setColumnWidth(i, 20000);
          }
        }
        //</editor-fold>
      }

      createComboArrayGroup(workbook);

      if (exportChart == null || exportChart.length == 0) {
        workbook.removeSheetAt(0);
      }

      if (exportChart != null && exportChart.length > 0) {
        //<editor-fold defaultstate="collapsed" desc="Ve bieu do">
        ConfigFileExport item = config.get(0);
        Sheet sheetConf = workbook_temp.getSheet("conf");

        // Dong bat dau du lieu cua chart
        Row rowStartConf = sheetConf.getRow(0);
        Cell cellStartConf = rowStartConf.getCell(1);
        cellStartConf.setCellValue(item.getStartRow() + 1);

        // Dong ket thuc du lieu cua chart
        Row rowEndConf = sheetConf.getRow(1);
        Cell cellEndConf = rowEndConf.getCell(1);
        cellEndConf.setCellValue(item.getStartRow() + 1 + item.getLstData().size());

        // Cot bat dau du lieu cua chart
        String xStart = "";

        // Cot ket thuc du lieu cua chart
        String xEnd = "";

        // xAxis
        Row rowXvalue = sheetConf.getRow(2);
        Cell cellXvalue = rowXvalue.getCell(1);
        cellXvalue.setCellValue("=" + I18n.getLanguage("common.export.char.total",
            Locale.forLanguageTag("vi")) + "!$" + xStart + "${startRow}:$" + xEnd + "${startRow}");

        // Categories
        Row rowNameList = sheetConf.getRow(3);
        Cell cellNameList = rowNameList.getCell(1);
        cellNameList.setCellValue("=" + I18n.getLanguage("common.export.char.total",
            Locale.forLanguageTag("vi")) + "!$" + exportChart[0] + "${i}");

        // Data
        Row rowDataValue = sheetConf.getRow(4);
        Cell cellDataValue = rowDataValue.getCell(1);
        cellDataValue.setCellValue("=" + I18n.getLanguage("common.export.char.total",
            Locale.forLanguageTag("vi")) + "!$" + xStart + "${i}:$" + xEnd + "${i}");
        //</editor-fold>
      }

      try {
        FileOutputStream fileOut = new FileOutputStream(pathOut);
        workbook.write(fileOut);
        fileOut.flush();
        fileOut.close();
      } catch (IOException e) {
        log.error(e.getMessage(), e);
      }
    } catch (FileNotFoundException e) {
      log.error(e.getMessage(), e);
    }
    return new File(pathOut);
  }

  public void createComboArrayService(SXSSFWorkbook workbook) {
    createServiceArrayList(workbook);
    createCountry(workbook);
    createAutomation(workbook);
  }

  public void createServiceArrayList(SXSSFWorkbook workbook) {
    CellStyle cellStyle = workbook.createCellStyle();
    cellStyle.setAlignment(HorizontalAlignment.LEFT);
    Font xSSFFontHeader = workbook.createFont();
    xSSFFontHeader.setFontName("Times New Roman");
    xSSFFontHeader.setFontHeightInPoints((short) 10);
    cellStyle.setFont(xSSFFontHeader);

    mapCountryName.clear();
    setMapCountryName();
    ExcelWriterUtils ewu = new ExcelWriterUtils();
    Map<String, CellStyle> styles = CommonExport.createStyles(workbook);
    XSSFSheet sheetOrther = workbook.getXSSFWorkbook()
        .createSheet(I18n.getLanguage("SrServiceArray.title.export.list"));
    ewu.createCell(sheetOrther, 0, 0,
        I18n.getLanguage("SrServiceArray.countryDisplay"), styles.get("header"));
    ewu.createCell(sheetOrther, 1, 0,
        I18n.getLanguage("SrServiceArray.configCode"), styles.get("header"));
    ewu.createCell(sheetOrther, 2, 0,
        I18n.getLanguage("SrServiceArray.configName"), styles.get("header"));
    ewu.createCell(sheetOrther, 3, 0,
        I18n.getLanguage("SrServiceArray.automation"), styles.get("header"));

    List<SRConfigDTO> lstData = srServiceManageRepository
        .getByConfigGroup(Constants.SR_CATALOG.SERVICE_ARRAY);
    int row = 1;

    if (lstData != null && !lstData.isEmpty()) {
      for (Map.Entry<String, String> rowS2 : mapCountryName.entrySet()) {
        ewu.createCell(sheetOrther, 0, row, rowS2.getValue() == null ? "" : rowS2.getValue().trim(),
            styles.get("cell"));
        for (int k = lstData.size() - 1; k > -1; k--) {
          cellStyle.setWrapText(true);
          if (!rowS2.getKey().equals(lstData.get(k).getCountry())) {
            continue;
          }
          ewu.createCell(sheetOrther, 1, row, lstData.get(k).getConfigCode(), styles.get("cell"))
              .setCellStyle(cellStyle);
          ewu.createCell(sheetOrther, 2, row, lstData.get(k).getConfigName(), styles.get("cell"))
              .setCellStyle(cellStyle);
          ewu.createCell(sheetOrther, 3, row,
              StringUtils.isStringNullOrEmpty(lstData.get(k).getAutomation()) ? ""
                  : (mapAutomation.get(lstData.get(k).getAutomation()) != null ? mapAutomation
                      .get(lstData.get(k).getAutomation()).getConfigName()
                      : lstData.get(k).getAutomation()),
              styles.get("cell")).setCellStyle(cellStyle);
          row++;
        }
      }
    }
  }

  public void createCountry(SXSSFWorkbook workbook) {
    CellStyle cellStyle = workbook.createCellStyle();
    cellStyle.setAlignment(HorizontalAlignment.LEFT);

    Font xSSFFontHeader = workbook.createFont();
    xSSFFontHeader.setFontName("Times New Roman");
    xSSFFontHeader.setFontHeightInPoints((short) 10);
    cellStyle.setFont(xSSFFontHeader);

    ExcelWriterUtils ewu = new ExcelWriterUtils();
    Map<String, CellStyle> styles = CommonExport.createStyles(workbook);
    XSSFSheet sheetOrther = workbook.getXSSFWorkbook()
        .createSheet(I18n.getLanguage("SrServiceArray.countryDisplay"));
    ewu.createCell(sheetOrther, 0, 0,
        I18n.getLanguage("SrServiceArray.countryName"), styles.get("header"));
    ewu.createCell(sheetOrther, 1, 0,
        I18n.getLanguage("SrServiceArray.countryCode"), styles.get("header"));

    List<ItemDataCRInside> lstCountryName = catLocationBusiness
        .getListLocationByLevelCBB(null, 1L, null);
    int row = 1;
    if (lstCountryName != null && !lstCountryName.isEmpty()) {
      for (ItemDataCRInside item : lstCountryName) {
        cellStyle.setWrapText(true);
        ewu.createCell(sheetOrther, 0, row, item.getDisplayStr(), styles.get("cell"))
            .setCellStyle(cellStyle);
        ewu.createCell(sheetOrther, 1, row, item.getValueStr().toString(), styles.get("cell"))
            .setCellStyle(cellStyle);
        row++;
      }
    }
  }

  public void createAutomation(SXSSFWorkbook workbook) {
    CellStyle cellStyle = workbook.createCellStyle();
    cellStyle.setAlignment(HorizontalAlignment.LEFT);

    Font xSSFFontHeader = workbook.createFont();
    xSSFFontHeader.setFontName("Times New Roman");
    xSSFFontHeader.setFontHeightInPoints((short) 10);
    cellStyle.setFont(xSSFFontHeader);

    ExcelWriterUtils ewu = new ExcelWriterUtils();
    Map<String, CellStyle> styles = CommonExport.createStyles(workbook);
    XSSFSheet sheetOrther = workbook.getXSSFWorkbook()
        .createSheet(I18n.getLanguage("SrServiceArray.automation"));
    ewu.createCell(sheetOrther, 0, 0,
        I18n.getLanguage("SrServiceArray.automationCode"), styles.get("header"));
    ewu.createCell(sheetOrther, 1, 0,
        I18n.getLanguage("SrServiceArray.automationName"), styles.get("header"));

    List<SRConfigDTO> lstConfig = srServiceManageRepository
        .getByConfigGroup(Constants.SR_CONFIG.AUTO_MATION);
    int row = 1;
    if (lstConfig != null && !lstConfig.isEmpty()) {
      for (SRConfigDTO item : lstConfig) {
        cellStyle.setWrapText(true);
        ewu.createCell(sheetOrther, 0, row, item.getConfigCode(), styles.get("cell"))
            .setCellStyle(cellStyle);
        ewu.createCell(sheetOrther, 1, row, item.getConfigName(), styles.get("cell"))
            .setCellStyle(cellStyle);
        row++;
      }
    }
  }

  public void createComboArrayGroup(SXSSFWorkbook workbook) {
    createServiceGroupList(workbook);
    createServiceArrayList(workbook);
    createCountry(workbook);
  }

  public void createServiceGroupList(SXSSFWorkbook workbook) {

    CellStyle cellStyle = workbook.createCellStyle();
    cellStyle.setAlignment(HorizontalAlignment.LEFT);

    Font xSSFFontHeader = workbook.createFont();
    xSSFFontHeader.setFontName("Times New Roman");
    xSSFFontHeader.setFontHeightInPoints((short) 10);
    cellStyle.setFont(xSSFFontHeader);

    ExcelWriterUtils ewu = new ExcelWriterUtils();
    Map<String, CellStyle> styles = CommonExport.createStyles(workbook);
    XSSFSheet sheetOrther = workbook.getXSSFWorkbook()
        .createSheet(I18n.getLanguage("SrServiceGroup.title.export.list"));
    ewu.createCell(sheetOrther, 0, 0,
        I18n.getLanguage("SrServiceArray.configCode"), styles.get("header"));
    ewu.createCell(sheetOrther, 1, 0,
        I18n.getLanguage("SrServiceGroup.configCode"), styles.get("header"));
    ewu.createCell(sheetOrther, 2, 0,
        I18n.getLanguage("SrServiceGroup.configName"), styles.get("header"));

    List<SRConfigDTO> lstData = srServiceManageRepository
        .getByConfigGroup(Constants.SR_CATALOG.SERVICE_GROUP);

    int row = 1;

    if (lstData != null && !lstData.isEmpty()) {
      for (SRConfigDTO item : lstData) {
        cellStyle.setWrapText(true);
        ewu.createCell(sheetOrther, 0, row, item.getParentCode(), styles.get("cell"))
            .setCellStyle(cellStyle);
        ewu.createCell(sheetOrther, 1, row, item.getConfigCode(), styles.get("cell"))
            .setCellStyle(cellStyle);
        ewu.createCell(sheetOrther, 2, row, item.getConfigName(), styles.get("cell"))
            .setCellStyle(cellStyle);
        row++;
      }
    }
  }

}
