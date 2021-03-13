package com.viettel.gnoc.sr.business;

import com.viettel.gnoc.commons.business.UnitBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.LogChangeConfigDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.proxy.CommonStreamServiceProxy;
import com.viettel.gnoc.commons.proxy.CrCategoryServiceProxy;
import com.viettel.gnoc.commons.proxy.CrServiceProxy;
import com.viettel.gnoc.commons.proxy.WoCategoryServiceProxy;
import com.viettel.gnoc.commons.utils.CellConfigExport;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.ConfigFileExport;
import com.viettel.gnoc.commons.utils.ConfigHeaderExport;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.DateUtil;
import com.viettel.gnoc.commons.utils.ExcelWriterUtils;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.CrImpactFrameInsiteDTO;
import com.viettel.gnoc.cr.dto.CrProcessInsideDTO;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.sr.dto.SRCatalogDTO;
import com.viettel.gnoc.sr.dto.SRConfigDTO;
import com.viettel.gnoc.sr.dto.SRCreateAutoCRDTO;
import com.viettel.gnoc.sr.dto.SRMappingProcessCRDTO;
import com.viettel.gnoc.sr.dto.SRSearchDTO;
import com.viettel.gnoc.sr.repository.SRCatalogRepository;
import com.viettel.gnoc.sr.repository.SRConfigRepository;
import com.viettel.gnoc.sr.repository.SRMappingProcessCRRepository;
import com.viettel.gnoc.wo.dto.WoCdGroupInsideDTO;
import com.viettel.gnoc.wo.dto.WoCdGroupTypeUserDTO;
import com.viettel.gnoc.wo.dto.WoInsideDTO;
import com.viettel.gnoc.wo.dto.WoTypeInsideDTO;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
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
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
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
public class SRMappingProcessCRBusinessImpl implements SRMappingProcessCRBusiness {

  @Value("${application.temp.folder}")
  private String tempFolder;

  private final static String SR_MAPPING_PROCESS_CR_EXPORT = "SR_MAPPING_PROCESS_CR_EXPORT";

  @Autowired
  protected SRMappingProcessCRRepository srMappingProcessCRRepository;

  @Autowired
  protected SRCatalogRepository srCatalogRepository;

  @Autowired
  TicketProvider ticketProvider;

  @Autowired
  protected CommonStreamServiceProxy commonStreamServiceProxy;

  @Autowired
  protected SRCatalogBusiness srCatalogBusiness;

  @Autowired
  protected CrServiceProxy crServiceProxy;

  @Autowired
  WoCategoryServiceProxy woCategoryServiceProxy;

  @Autowired
  UnitBusiness unitBusiness;

  @Autowired
  CrCategoryServiceProxy crCategoryServiceProxy;

  @Autowired
  SRConfigRepository srConfigRepository;

  public static final String XLSX_FILE_EXTENTION = ".xlsx";
  public static final String XLSM_FILE_EXTENTION = ".xlsm";

  Map<String, SRCatalogDTO> mapService = new HashMap<>();
  Map<String, String> mapProcess = new HashMap<>();
  Map<String, String> mapWo = new HashMap<>();
  Map<Long, String> mapAffectedService = new HashMap<>();
  Map<Long, String> mapWoCdGroup = new HashMap<>();
  Map<Long, String> mapUnitImplement = new HashMap<>();
  Map<Long, String> mapDutyType = new HashMap<>();
  Map<String, String> mapWoPriority = new HashMap<>();
  Map<String, String> mapWoTypeFT = new HashMap<>();
  Map<String, String> mapProcessTypeLv3Id = new HashMap<>();

  private final Map<String, String> mapAddObject = new HashMap<>();

  public void setMapService() {
    List<SRCatalogDTO> lstServices = srCatalogBusiness.getListServiceNameToMapping();
    if (lstServices != null && !lstServices.isEmpty()) {
      for (SRCatalogDTO dto : lstServices) {
        mapService.put(dto.getServiceCode(), dto);
      }
    }
  }

  public void setMapProcess() {
    List<SRMappingProcessCRDTO> lstProcessAll = srMappingProcessCRRepository.getListAllProcess();
    if (lstProcessAll != null && !lstProcessAll.isEmpty()) {
      for (SRMappingProcessCRDTO dto : lstProcessAll) {
        mapProcess.put(String.valueOf(dto.getCrProcessCode()), dto.getCrProcessName());
      }
    }
  }

  public void setMapWo() {
    List<SRMappingProcessCRDTO> lstWoAll = srMappingProcessCRRepository.getListAllWo();
    if (lstWoAll != null && !lstWoAll.isEmpty()) {
      for (SRMappingProcessCRDTO dto : lstWoAll) {
        mapWo.put(String.valueOf(dto.getCrProcessCode()), dto.getCrProcessName());
        mapProcessTypeLv3Id.put(String.valueOf(dto.getCrProcessId()), dto.getCrProcessName());
      }
    }
  }

  @Override
  public Datatable getListMappingProcessCR(SRMappingProcessCRDTO srMappingProcessCRDTO) {
    log.info("Request to getListMappingProcessCR : {}", srMappingProcessCRDTO);
    Datatable datatable = srMappingProcessCRRepository
        .getListMappingProcessCR(srMappingProcessCRDTO);
    List<SRMappingProcessCRDTO> list = (List<SRMappingProcessCRDTO>) datatable.getData();
    mapProcessTypeLv3Id.clear();
    setMapWo();
    for (SRMappingProcessCRDTO dto : list) {
      dto.setBtnDelete(true);
      List<SRMappingProcessCRDTO> lst = srMappingProcessCRRepository
          .checkDeleteSRMappingProcess(dto);
      if (lst != null && !lst.isEmpty()) {
        dto.setBtnDelete(false);
      }
      if (StringUtils.isNotNullOrEmpty(dto.getProcessTypeLv3Id())) {
        String[] arrProcessTypeLv3Id = dto.getProcessTypeLv3Id().split(",");
        if (arrProcessTypeLv3Id != null && arrProcessTypeLv3Id.length > 0) {
          String processTypeLv3IdName = "";
          for (String process3Id : arrProcessTypeLv3Id) {
            if (mapProcessTypeLv3Id.containsKey(process3Id)) {
              processTypeLv3IdName += mapProcessTypeLv3Id.get(process3Id) + " // ";
            }
          }
          if (StringUtils.isNotNullOrEmpty(processTypeLv3IdName) && processTypeLv3IdName.trim()
              .endsWith("//")) {
            dto.setProcessTypeLv3IdName(processTypeLv3IdName
                .substring(0, processTypeLv3IdName.length() - 3));
          }
        }
      }
    }
    datatable.setData(list);
    return datatable;
  }

  @Override
  public ResultInSideDto insertOrUpdate(SRMappingProcessCRDTO srMappingProcessCRDTO) {
    log.info("Request to insertOrUpdate : {}", srMappingProcessCRDTO);
    ResultInSideDto resultInSideDto = new ResultInSideDto(null, RESULT.ERROR, RESULT.ERROR);
    UserToken userToken = ticketProvider.getUserToken();
    srMappingProcessCRDTO.setCrProcessId(null);
    srMappingProcessCRDTO.setCrProcessParentId(Long.parseLong(srMappingProcessCRDTO.getProcess()));
    resultInSideDto = validate(srMappingProcessCRDTO);
    if (!resultInSideDto.getKey().equals(RESULT.SUCCESS)) {
      return resultInSideDto;
    }
    resultInSideDto = srMappingProcessCRRepository
        .insertOrUpdate(srMappingProcessCRDTO);
    if (resultInSideDto.getKey().equals(Constants.RESULT.SUCCESS)) {
      commonStreamServiceProxy.insertLog(new LogChangeConfigDTO(userToken.getUserName(),
          "insertOrUpdate SRMappingProcessCR",
          "insertOrUpdate SRMappingProcessCR: " + resultInSideDto.getId(),
          srMappingProcessCRDTO, null));
    }
    return resultInSideDto;
  }

  @Override
  public SRMappingProcessCRDTO getSRMappingProcessCRDetail(Long id) {
    log.info("Request to getSRMappingProcessCRDetail : {}", id);
    SRMappingProcessCRDTO srMappingProcessCRDTO = new SRMappingProcessCRDTO();
    srMappingProcessCRDTO.setId(id);
    return srMappingProcessCRRepository.getSRMappingProcessCRDetail(srMappingProcessCRDTO);
  }

  @Override
  public ResultInSideDto deleteSRMappingProcessCR(Long id) {
    log.info("Request to deleteSRMappingProcessCR : {}", id);
    ResultInSideDto resultInSideDto = srMappingProcessCRRepository.deleteSRMappingProcessCR(id);
    if (resultInSideDto.getKey().equals(Constants.RESULT.SUCCESS)) {
      UserToken userToken = ticketProvider.getUserToken();
      commonStreamServiceProxy.insertLog(new LogChangeConfigDTO(userToken.getUserName(),
          "insertOrUpdate SRMappingProcessCR",
          "insertOrUpdate SRMappingProcessCR: " + resultInSideDto.getId(),
          null, null));
    }
    return resultInSideDto;
  }

  @Override
  public SRSearchDTO getListSearchDTO() {
    log.info("Request to getListSearchDTO : {}");
    SRSearchDTO searchDTOList = new SRSearchDTO();
    searchDTOList.setLstProcess(srMappingProcessCRRepository.getListParentChilLevel2());
    searchDTOList.setLstService(srCatalogRepository.getListServiceNameToMapping());
    return searchDTOList;
  }

  @Override
  public List<WoInsideDTO> getLstPriority() {
    log.info("Request to getLstPriority : {}");
    return srMappingProcessCRRepository.getLstPriority();
  }

  @Override
  public List<SRMappingProcessCRDTO> getListWoById(Long crProcessId) {
    log.info("Request to getListWoById : {}", crProcessId);
    return srMappingProcessCRRepository.getListWo(crProcessId);
  }

  @Override
  public List<SRMappingProcessCRDTO> getListSRMappingProcessCRDTO(SRMappingProcessCRDTO dto) {
    log.info("Request to getListSRMappingProcessCRDTO : {}", dto);
    return srMappingProcessCRRepository.getListSRMappingProcessCRDTO(dto);
  }

  @Override
  public SRMappingProcessCRDTO getStartTimeEndTimeFromCrImpact(SRCreateAutoCRDTO dto) {
    log.info("Request to getStartTimeEndTimeFromCrImpact : {}", dto);
    return srMappingProcessCRRepository.getStartTimeEndTimeFromCrImpact(dto);
  }

  @Override
  public List<WoCdGroupInsideDTO> getLstWoCdGroupCBB() {
    log.info("Request to getLstWoCdGroupCBB : {}");
    return woCategoryServiceProxy.getListCdGroupByUser(new WoCdGroupTypeUserDTO());
  }

  @Override
  public List<ItemDataCRInside> getLstDutyTypeCBB() {
    log.info("Request to getLstDutyTypeCBB : {}");
    return crServiceProxy.getListDutyTypeCBB(new CrImpactFrameInsiteDTO());
  }

  @Override
  public List<ItemDataCRInside> getDutyTypeByProcessId(Long processId) {
    log.info("Request to getDutyTypeByProcessId : {}", processId);
    return srMappingProcessCRRepository.getDutyTypeByProcessId(processId);
  }

  @Override
  public List<ItemDataCRInside> getLstAffectedServiceCBB() {
    log.info("Request to getLstAffectedServiceCBB : {}");
    return crServiceProxy.getListAffectedServiceCBProxy(null);
  }

  @Override
  public List<WoTypeInsideDTO> getListWoTypeDTOCBB(WoTypeInsideDTO woTypeInsideDTO) {
    log.info("Request to getListWoTypeDTOCBB : {}", woTypeInsideDTO);
    woTypeInsideDTO.setIsOtherSys(false);
    return woCategoryServiceProxy.getListWoTypeDTO(woTypeInsideDTO);
  }

  @Override
  public List<WoTypeInsideDTO> getListWoTestTypeCBB() {
    log.info("Request to getListWoTestTypeCBB : {}");
    List<WoTypeInsideDTO> lstResult = new ArrayList<>();
    SRConfigDTO srConfigDTO = new SRConfigDTO();
    srConfigDTO.setConfigGroup(Constants.SR_CONFIG.WO_TYPE_CODE_TEST_SERVICE);
    List<SRConfigDTO> listTestService = srConfigRepository
        .getByConfigGroup(srConfigDTO);
    if (listTestService != null && !listTestService.isEmpty()) {
      WoTypeInsideDTO woTypeInsideDTO = woCategoryServiceProxy
          .getWoTypeByCode(listTestService.get(0).getConfigCode());
      lstResult.add(woTypeInsideDTO);
    }
    return lstResult;
  }

  @Override
  public Map<String, String> getListStatusCrCBB(Long crProcessId) {
    log.info("Request to getListStatusCrCBB : {}", crProcessId);
    Map<String, String> mapResult = new HashMap<>();
    CrProcessInsideDTO crProcessDTO = new CrProcessInsideDTO();
    crProcessDTO.setCrProcessId(crProcessId);
    List<CrProcessInsideDTO> listCrProcess = crCategoryServiceProxy
        .getListCrProcessDTO(crProcessDTO);
    for (CrProcessInsideDTO crProcessInsideDTO : listCrProcess) {
      if (crProcessInsideDTO.getCrTypeId() != null) {
        if (Constants.CR_TYPE.EMERGENCY.equals(crProcessInsideDTO.getCrTypeId())) {
          mapResult.put("4", I18n.getLanguage("cr.state.4"));
        } else if (Constants.CR_TYPE.STANDARD.equals(crProcessInsideDTO.getCrTypeId())) {
          mapResult.put("5", I18n.getLanguage("cr.state.5"));
        } else {
          mapResult.put("2", I18n.getLanguage("cr.state.2"));
        }
      }
    }
    return mapResult;
  }

  @Override
  public File exportSRMappingProcessCR(SRMappingProcessCRDTO srMappingProcessCRDTO)
      throws Exception {
    log.info("Request to exportSRMappingProcessCR : {}", srMappingProcessCRDTO);
    List<SRMappingProcessCRDTO> list = srMappingProcessCRRepository
        .exportSRMappingProcessCR(srMappingProcessCRDTO);
    mapDutyType.clear();
    mapWoCdGroup.clear();
    mapAffectedService.clear();
    setMapWoCdGroup();
    setMapDutyType();
    setMapAffectedService();
    mapProcessTypeLv3Id.clear();
    setMapWo();
    if (list != null && !list.isEmpty()) {
      list.stream().forEach(c -> {
        if (c.getAutoCreateCR() != null && c.getAutoCreateCR() == 1L) {
          c.setAutoCreateCRStr(I18n.getLanguage("srMappingProcessCR.common.1"));
          if (c.getServiceAffecting() != null && c.getServiceAffecting() == 1L) {
            c.setServiceAffectingStr(I18n.getLanguage("srMappingProcessCR.common.1"));
          } else {
            c.setServiceAffectingStr(I18n.getLanguage("srMappingProcessCR.common.0"));
          }
          if (c.getIsCrNodes() != null && c.getIsCrNodes() == 1L) {
            c.setIsCrNodesStr(I18n.getLanguage("srCatalog.isCrNodes.1"));
          } else {
            c.setIsCrNodesStr(I18n.getLanguage("srCatalog.isCrNodes.0"));
          }
          if (c.getGroupCdFtService() != null && mapWoCdGroup
              .containsKey(c.getGroupCdFtService())) {
            c.setGroupCdFtServiceStr(mapWoCdGroup.get(c.getGroupCdFtService()));
          }
          if (c.getGroupCDFT() != null && mapWoCdGroup.containsKey(c.getGroupCDFT())) {
            c.setGroupCDFTStr(mapWoCdGroup.get(c.getGroupCDFT()));
          }
          if (c.getDutyType() != null && mapDutyType.containsKey(c.getDutyType())) {
            c.setDutyTypeStr(mapDutyType.get(c.getDutyType()));
          }
          if (StringUtils.isNotNullOrEmpty(c.getAffectingService())) {
            String[] arrAS = c.getAffectingService().split(",");
            if (arrAS != null && arrAS.length > 0) {
              String affectiongSevice = "";
              for (int i = 0; i < arrAS.length; i++) {
                if (mapAffectedService
                    .containsKey((arrAS[i] != null ? Long.parseLong(arrAS[i]) : null))) {
                  affectiongSevice += mapAffectedService.get(Long.parseLong(arrAS[i])) + ",";
                }
              }
              if (StringUtils.isNotNullOrEmpty(affectiongSevice) && affectiongSevice
                  .endsWith(",")) {
                affectiongSevice = affectiongSevice.substring(0, affectiongSevice.length() - 1);
              }
              c.setAffectingService(affectiongSevice);
            }
          }
        } else {
          c.setAutoCreateCRStr(I18n.getLanguage("srMappingProcessCR.common.0"));
        }
        if (StringUtils.isNotNullOrEmpty(c.getProcessTypeLv3Id())) {
          String[] arrProcessTypeLv3Id = c.getProcessTypeLv3Id().split(",");
          if (arrProcessTypeLv3Id != null && arrProcessTypeLv3Id.length > 0) {
            String processTypeLv3IdName = "";
            for (String process3Id : arrProcessTypeLv3Id) {
              if (mapProcessTypeLv3Id.containsKey(process3Id)) {
                processTypeLv3IdName += mapProcessTypeLv3Id.get(process3Id) + " // ";
              }
            }
            if (StringUtils.isNotNullOrEmpty(processTypeLv3IdName) && processTypeLv3IdName.trim()
                .endsWith("//")) {
              c.setProcessTypeLv3IdName(processTypeLv3IdName
                  .substring(0, processTypeLv3IdName.length() - 3));
            }
          }
        }
      });
    }
    return exportFileSrMappingCr(list, "", false);
  }

  private boolean validFileFormat(List<Object[]> lstHeader) {
    Object[] obj0 = lstHeader.get(0);
    int count = 0;

    for (Object data : obj0) {
      if (data != null) {
        count += 1;
      }
    }

    if (count != 23) {
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
    if (!(I18n.getLanguage("SrMappingProcessCr.serviceCode") + " (*)")
        .equalsIgnoreCase(obj0[1].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("SrMappingProcessCr.processCode") + " (*)")
        .equalsIgnoreCase(obj0[2].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("SrMappingProcessCr.woCode"))
        .equalsIgnoreCase(obj0[3].toString().trim().replace("\n", " "))) {
      return false;
    }
    if (!(I18n.getLanguage("SrMappingProcessCr.autoCreateCRStr"))
        .equalsIgnoreCase(obj0[4].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("SrMappingProcessCr.isCrNodesStr"))
        .equalsIgnoreCase(obj0[5].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("SrMappingProcessCr.crTitle"))
        .equalsIgnoreCase(obj0[6].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("SrMappingProcessCr.descriptionCr"))
        .equalsIgnoreCase(obj0[7].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("SrMappingProcessCr.unitImplementName"))
        .equalsIgnoreCase(obj0[8].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("SrMappingProcessCr.serviceAffectingStr"))
        .equalsIgnoreCase(obj0[9].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("SrMappingProcessCr.affectingService"))
        .equalsIgnoreCase(obj0[10].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("SrMappingProcessCr.totalAffectingCustomers"))
        .equalsIgnoreCase(obj0[11].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("SrMappingProcessCr.totalAffectingMinutes"))
        .equalsIgnoreCase(obj0[12].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("SrMappingProcessCr.woFtTypeIdStr"))
        .equalsIgnoreCase(obj0[13].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("SrMappingProcessCr.woContentCDFT"))
        .equalsIgnoreCase(obj0[14].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("SrMappingProcessCr.woFtDescription"))
        .equalsIgnoreCase(obj0[15].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("SrMappingProcessCr.groupCDFT"))
        .equalsIgnoreCase(obj0[16].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("SrMappingProcessCr.woFtPriorityStr"))
        .equalsIgnoreCase(obj0[17].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("SrMappingProcessCr.woTestTypeIdStr"))
        .equalsIgnoreCase(obj0[18].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("SrMappingProcessCr.woContentService"))
        .equalsIgnoreCase(obj0[19].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("SrMappingProcessCr.woTestDescription"))
        .equalsIgnoreCase(obj0[20].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("SrMappingProcessCr.groupCdFtService"))
        .equalsIgnoreCase(obj0[21].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("SrMappingProcessCr.woTestPriorityStr"))
        .equalsIgnoreCase(obj0[22].toString().trim())) {
      return false;
    }
    return true;
  }

  @Override
  public File getTemplate() throws Exception {
    log.info("Request to getTemplate : {}");
    ExcelWriterUtils excelWriterUtils = new ExcelWriterUtils();
    String templatePathOut = "templates" + File.separator + "TEMPLATE_EXPORT.xlsx";
    templatePathOut = StringUtils.removeSeparator(templatePathOut);
    Resource resource = new ClassPathResource(templatePathOut);
    InputStream fileTemplate = resource.getInputStream();

    //apache POI XSSF
    XSSFWorkbook workbook = new XSSFWorkbook(fileTemplate);
    XSSFSheet sheetOne = workbook.getSheetAt(0);
    XSSFSheet sheetParam = workbook.createSheet("Other");
    XSSFSheet sheetParam1 = workbook.createSheet("param");
    XSSFSheet sheetParam2 = workbook.createSheet("param1");
    XSSFSheet sheetUnitImplement = workbook.createSheet("unit");
    XSSFSheet sheetAffectedService = workbook.createSheet("affectedService");
    XSSFSheet sheetGroupCDFT = workbook.createSheet("groupCDFT ");
    XSSFSheet sheetGroupCdFtService = workbook.createSheet("groupCdFtService");
//    XSSFSheet sheetDutyType = workbook.createSheet("dutyType");
    XSSFSheet sheetPriority = workbook.createSheet("priority");
    XSSFSheet sheetTestService = workbook.createSheet("testService");
    XSSFSheet sheetCoordinationFT = workbook.createSheet("coordinationFT");

    //Tạo 1 mảng lưu header từng cột
    String[] header = new String[]{
        I18n.getLanguage("common.STT"),
        I18n.getLanguage("SrMappingProcessCr.serviceCode"),
        I18n.getLanguage("SrMappingProcessCr.processCode"),
        I18n.getLanguage("SrMappingProcessCr.woCode"),

        I18n.getLanguage("SrMappingProcessCr.autoCreateCRStr"),
        I18n.getLanguage("SrMappingProcessCr.isCrNodesStr"),
        //CR
        I18n.getLanguage("SrMappingProcessCr.crTitle"),
        I18n.getLanguage("SrMappingProcessCr.descriptionCr"),
//        I18n.getLanguage("SrMappingProcessCr.dutyTypeStr"),
        I18n.getLanguage("SrMappingProcessCr.unitImplementName"),
//        I18n.getLanguage("SrMappingProcessCr.crStatusStr"),
        I18n.getLanguage("SrMappingProcessCr.serviceAffectingStr"),
        I18n.getLanguage("SrMappingProcessCr.affectingService"),
        I18n.getLanguage("SrMappingProcessCr.totalAffectingCustomers"),
        I18n.getLanguage("SrMappingProcessCr.totalAffectingMinutes"),
        //WO1
        I18n.getLanguage("SrMappingProcessCr.woFtTypeIdStr"),
        I18n.getLanguage("SrMappingProcessCr.woContentCDFT"),
        I18n.getLanguage("SrMappingProcessCr.woFtDescription"),
        I18n.getLanguage("SrMappingProcessCr.groupCDFT"),
        I18n.getLanguage("SrMappingProcessCr.woFtPriorityStr"),
//        I18n.getLanguage("SrMappingProcessCr.woFtStartTimeStr"),
//        I18n.getLanguage("SrMappingProcessCr.woFtEndTimeStr"),
        //WO2
        I18n.getLanguage("SrMappingProcessCr.woTestTypeIdStr"),
        I18n.getLanguage("SrMappingProcessCr.woContentService"),
        I18n.getLanguage("SrMappingProcessCr.woTestDescription"),
        I18n.getLanguage("SrMappingProcessCr.groupCdFtService"),
        I18n.getLanguage("SrMappingProcessCr.woTestPriorityStr"),
//        I18n.getLanguage("SrMappingProcessCr.woTestStartTimeStr"),
//        I18n.getLanguage("SrMappingProcessCr.woTestEndTimeStr")
    };

    String[] header_service = new String[]{
        I18n.getLanguage("SrMappingProcessCr.serviceCode"),
        I18n.getLanguage("SrMappingProcessCr.serviceName")
    };
    String[] header_process = new String[]{
        I18n.getLanguage("SrMappingProcessCr.processCode"),
        I18n.getLanguage("SrMappingProcessCr.processName"),
        I18n.getLanguage("SrMappingProcessCr.woCode"),
        I18n.getLanguage("SrMappingProcessCr.woName")
    };

    //Tiêu đề đánh dấu *
    String[] headerStar = new String[]{
        I18n.getLanguage("SrMappingProcessCr.serviceCode"),
        I18n.getLanguage("SrMappingProcessCr.processCode")
    };

    XSSFDataValidationHelper dataValidationHelper = new XSSFDataValidationHelper(sheetOne);
    List<String> listHeader = Arrays.asList(header);
    List<String> listHeaderStar = Arrays.asList(headerStar);
    List<String> listHeaderLstService = Arrays.asList(header_service);
    List<String> listHeaderLstProcess = Arrays.asList(header_process);

    int autoCreateCRStrColumn = listHeader
        .indexOf(I18n.getLanguage("SrMappingProcessCr.autoCreateCRStr"));
    int isCrNodesStrColumn = listHeader
        .indexOf(I18n.getLanguage("SrMappingProcessCr.isCrNodesStr"));
    int serviceAffectingStrColumn = listHeader
        .indexOf(I18n.getLanguage("SrMappingProcessCr.serviceAffectingStr"));
    Map<String, CellStyle> style = CommonExport.createStyles(workbook);

    //Tạo tiêu đề
    sheetOne.addMergedRegion(new CellRangeAddress(3, 3, 0, listHeader.size() - 1));
    Row titleRow = sheetOne.createRow(3);
    titleRow.setHeightInPoints(25);
    Cell titleCell = titleRow.createCell(0);
    titleCell.setCellValue(I18n.getLanguage("SrMappingProcessCr.title.import.list"));
    titleCell.setCellStyle(style.get("title"));

    sheetParam1.addMergedRegion(new CellRangeAddress(1, 1, 0, listHeaderLstService.size() - 1));
    Row titleRow_lstcountry = sheetParam1.createRow(1);
    titleRow_lstcountry.setHeightInPoints(25);
    Cell titleCell1 = titleRow_lstcountry.createCell(0);
    titleCell1.setCellValue(I18n.getLanguage("SrMappingProcessCr.service"));
    titleCell1.setCellStyle(style.get("title"));

    sheetParam2.addMergedRegion(new CellRangeAddress(1, 1, 0, listHeaderLstProcess.size() - 1));
    Row titleRow_lstCheckListBD = sheetParam2.createRow(1);
    titleRow_lstCheckListBD.setHeightInPoints(25);
    Cell titleCell2 = titleRow_lstCheckListBD.createCell(0);
    titleCell2.setCellValue(I18n.getLanguage("SrMappingProcessCr.processWo"));
    titleCell2.setCellStyle(style.get("title"));

    sheetUnitImplement
        .addMergedRegion(new CellRangeAddress(1, 1, 0, listHeaderLstProcess.size() - 1));
    Row titleRow_UnitImplement = sheetUnitImplement.createRow(1);
    titleRow_UnitImplement.setHeightInPoints(25);
    Cell titleCellUnitImplement = titleRow_UnitImplement.createCell(0);
    titleCellUnitImplement.setCellValue(I18n.getLanguage("SrMappingProcessCr.unitImplementName"));
    titleCellUnitImplement.setCellStyle(style.get("title"));

    sheetAffectedService
        .addMergedRegion(new CellRangeAddress(1, 1, 0, listHeaderLstProcess.size() - 1));
    Row titleRow_AffectedService = sheetAffectedService.createRow(1);
    titleRow_AffectedService.setHeightInPoints(25);
    Cell titleCellAffectedService = titleRow_AffectedService.createCell(0);
    titleCellAffectedService.setCellValue(I18n.getLanguage("SrMappingProcessCr.affectingService"));
    titleCellAffectedService.setCellStyle(style.get("title"));

    sheetGroupCDFT
        .addMergedRegion(new CellRangeAddress(1, 1, 0, listHeaderLstProcess.size() - 1));
    Row titleRow_GroupCDFT = sheetGroupCDFT.createRow(1);
    titleRow_GroupCDFT.setHeightInPoints(25);
    Cell titleCellGroupCDFT = titleRow_GroupCDFT.createCell(0);
    titleCellGroupCDFT.setCellValue(I18n.getLanguage("SrMappingProcessCr.groupCDFT.title"));
    titleCellGroupCDFT.setCellStyle(style.get("title"));

    sheetGroupCdFtService
        .addMergedRegion(new CellRangeAddress(1, 1, 0, listHeaderLstProcess.size() - 1));
    Row titleRow_GroupCdFtService = sheetGroupCdFtService.createRow(1);
    titleRow_GroupCdFtService.setHeightInPoints(25);
    Cell titleCellGroupCdFtService = titleRow_GroupCdFtService.createCell(0);
    titleCellGroupCdFtService
        .setCellValue(I18n.getLanguage("SrMappingProcessCr.groupCdFtService.title"));
    titleCellGroupCdFtService.setCellStyle(style.get("title"));

    sheetCoordinationFT
        .addMergedRegion(new CellRangeAddress(1, 1, 0, listHeaderLstProcess.size() - 1));
    Row titleRow_CoordinationFT = sheetCoordinationFT.createRow(1);
    titleRow_CoordinationFT.setHeightInPoints(25);
    Cell titleCellCoordinationFT = titleRow_CoordinationFT.createCell(0);
    titleCellCoordinationFT.setCellValue(I18n.getLanguage("SrMappingProcessCr.woFtTypeIdStr"));
    titleCellCoordinationFT.setCellStyle(style.get("title"));

    sheetPriority
        .addMergedRegion(new CellRangeAddress(1, 1, 0, listHeaderLstProcess.size() - 1));
    Row titleRow_Priority = sheetPriority.createRow(1);
    titleRow_Priority.setHeightInPoints(25);
    Cell titleCellPriority = titleRow_Priority.createCell(0);
    titleCellPriority.setCellValue(I18n.getLanguage("SrMappingProcessCr.woPriority"));
    titleCellPriority.setCellStyle(style.get("title"));

    Row firstLeftHeaderRow = sheetOne.createRow(0);
    Cell firstLeftHeaderCell = firstLeftHeaderRow.createCell(1);
    firstLeftHeaderRow.setHeightInPoints(16);
    firstLeftHeaderCell.setCellValue(I18n.getLanguage("common.export.firstLeftHeader"));
    firstLeftHeaderCell.setCellStyle(style.get("indexTitle"));
    Row secondLeftHeaderRow = sheetOne.createRow(1);
    Cell secondLeftHeaderCell = secondLeftHeaderRow.createCell(1);
    secondLeftHeaderRow.setHeightInPoints(16);
    secondLeftHeaderCell.setCellValue(I18n.getLanguage("common.export.secondLeftHeader"));
    secondLeftHeaderCell.setCellStyle(style.get("indexTitle"));
    Cell firstRightHeaderCell = firstLeftHeaderRow.createCell(4);
    firstLeftHeaderRow.setHeightInPoints(16);
    firstRightHeaderCell.setCellValue(I18n.getLanguage("common.export.firstRightHeader"));
    firstRightHeaderCell.setCellStyle(style.get("indexTitle"));
    Cell secondRightHeaderCell = secondLeftHeaderRow.createCell(4);
    secondLeftHeaderRow.setHeightInPoints(16);
    secondRightHeaderCell
        .setCellValue(I18n.getLanguage("common.export.secondRightHeader"));
    secondRightHeaderCell.setCellStyle(style.get("indexTitle"));

    XSSFFont starFont = workbook.createFont();
    starFont.setColor(IndexedColors.RED.getIndex());

    //Tạo Header
    Row headerRow = sheetOne.createRow(7);
    Row headerListService = sheetParam1.createRow(3);
    Row headerListProcess = sheetParam2.createRow(3);
    headerRow.setHeightInPoints(16);

    for (int i = 0; i < listHeader.size(); i++) {
      Cell headerCell = headerRow.createCell(i);
      XSSFRichTextString richTextString = new XSSFRichTextString(listHeader.get(i));
      for (String headerCheck : listHeaderStar) {
        if (headerCheck.equalsIgnoreCase(listHeader.get(i))) {
          richTextString.append(" (*)", starFont);
        }
      }
      headerCell.setCellValue(richTextString);
      headerCell.setCellStyle(style.get("header"));
      sheetOne.setColumnWidth(i, 7000);
    }

    //Của sheet service
    Cell headerCellServiceCode = headerListService.createCell(0);
    Cell headerCellServiceName = headerListService.createCell(1);

    //Của sheet process
    Cell headerCellProcessCode = headerListProcess.createCell(0);
    Cell headerCellProcessName = headerListProcess.createCell(1);
    Cell headerCellWoCode = headerListProcess.createCell(2);
    Cell headerCellWoName = headerListProcess.createCell(3);

    XSSFRichTextString stt = new XSSFRichTextString(I18n.getLanguage("common.STT"));
    XSSFRichTextString serviceCode = new XSSFRichTextString(
        I18n.getLanguage("SrMappingProcessCr.serviceCode"));
    XSSFRichTextString serviceName = new XSSFRichTextString(
        I18n.getLanguage("SrMappingProcessCr.serviceName"));
    XSSFRichTextString processCode = new XSSFRichTextString(
        I18n.getLanguage("SrMappingProcessCr.processCode"));
    XSSFRichTextString processName = new XSSFRichTextString(
        I18n.getLanguage("SrMappingProcessCr.processName"));
    XSSFRichTextString woCode = new XSSFRichTextString(
        I18n.getLanguage("SrMappingProcessCr.woCode"));
    XSSFRichTextString woName = new XSSFRichTextString(
        I18n.getLanguage("SrMappingProcessCr.woName"));

    headerCellServiceCode.setCellValue(serviceCode);
    headerCellServiceCode.setCellStyle(style.get("header"));
    headerCellServiceName.setCellValue(serviceName);
    headerCellServiceName.setCellStyle(style.get("header"));

    headerCellProcessCode.setCellValue(processCode);
    headerCellProcessCode.setCellStyle(style.get("header"));
    headerCellProcessName.setCellValue(processName);
    headerCellProcessName.setCellStyle(style.get("header"));
    headerCellWoCode.setCellValue(woCode);
    headerCellWoCode.setCellStyle(style.get("header"));
    headerCellWoName.setCellValue(woName);
    headerCellWoName.setCellStyle(style.get("header"));

    sheetOne.setColumnWidth(0, 6000);

    int row = 8;
    excelWriterUtils
        .createCell(sheetParam, autoCreateCRStrColumn, row++,
            I18n.getLanguage("srMappingProcessCR.common.1")
            , style.get("cell"));
    excelWriterUtils
        .createCell(sheetParam, autoCreateCRStrColumn, row++,
            I18n.getLanguage("srMappingProcessCR.common.0")
            , style.get("cell"));
    sheetParam.autoSizeColumn(1);
    Name autoCreateCRStr = workbook.createName();
    autoCreateCRStr.setNameName("autoCreateCRStr");
    autoCreateCRStr.setRefersToFormula("Other!$E$2:$E$" + row);
    XSSFDataValidationConstraint autoCreateCRStrConstraint = new XSSFDataValidationConstraint(
        DataValidationConstraint.ValidationType.LIST, "autoCreateCRStr");

    CellRangeAddressList autoCreateCRStrCreate = new CellRangeAddressList(8, 65000,
        autoCreateCRStrColumn, autoCreateCRStrColumn);
    XSSFDataValidation dataValidationAutoCreateCRStr = (XSSFDataValidation) dataValidationHelper
        .createValidation(
            autoCreateCRStrConstraint, autoCreateCRStrCreate);
    dataValidationAutoCreateCRStr.setShowErrorBox(true);
    sheetOne.addValidationData(dataValidationAutoCreateCRStr);

    row = 8;
    excelWriterUtils
        .createCell(sheetParam, isCrNodesStrColumn, row++, I18n.getLanguage("srCatalog.isCrNodes.1")
            , style.get("cell"));
    excelWriterUtils
        .createCell(sheetParam, isCrNodesStrColumn, row++, I18n.getLanguage("srCatalog.isCrNodes.0")
            , style.get("cell"));
    sheetParam.autoSizeColumn(1);
    Name isCrNodesStr = workbook.createName();
    isCrNodesStr.setNameName("isCrNodes");
    isCrNodesStr.setRefersToFormula("Other!$F$2:$F$" + row);
    XSSFDataValidationConstraint isCrNodesStrConstraint = new XSSFDataValidationConstraint(
        DataValidationConstraint.ValidationType.LIST, "isCrNodes");
    CellRangeAddressList isCrNodesStrCreate = new CellRangeAddressList(8, 65000,
        isCrNodesStrColumn, isCrNodesStrColumn);
    XSSFDataValidation dataValidationIsCrNodesStr = (XSSFDataValidation) dataValidationHelper
        .createValidation(
            isCrNodesStrConstraint, isCrNodesStrCreate);
    dataValidationAutoCreateCRStr.setShowErrorBox(true);
    sheetOne.addValidationData(dataValidationIsCrNodesStr);

    row = 8;
    excelWriterUtils
        .createCell(sheetParam, serviceAffectingStrColumn, row++,
            I18n.getLanguage("srMappingProcessCR.common.1")
            , style.get("cell"));
    excelWriterUtils
        .createCell(sheetParam, serviceAffectingStrColumn, row++,
            I18n.getLanguage("srMappingProcessCR.common.0")
            , style.get("cell"));
    sheetParam.autoSizeColumn(1);
    Name serviceAffectingStr = workbook.createName();
    serviceAffectingStr.setNameName("serviceAffectingStr");
    serviceAffectingStr.setRefersToFormula("Other!$E$2:$E$" + row);
    XSSFDataValidationConstraint serviceAffectingStrConstraint = new XSSFDataValidationConstraint(
        DataValidationConstraint.ValidationType.LIST, "serviceAffectingStr");
    CellRangeAddressList serviceAffectingStrCreate = new CellRangeAddressList(8, 65000,
        serviceAffectingStrColumn, serviceAffectingStrColumn);
    XSSFDataValidation dataValidationServiceAffectingStr = (XSSFDataValidation) dataValidationHelper
        .createValidation(
            serviceAffectingStrConstraint, serviceAffectingStrCreate);
    dataValidationServiceAffectingStr.setShowErrorBox(true);
    sheetOne.addValidationData(dataValidationServiceAffectingStr);

    row = 4;
    int k = 1;
    List<SRCatalogDTO> lstServices = srCatalogBusiness.getListServiceNameToMapping();
    for (SRCatalogDTO dto : lstServices) {
      excelWriterUtils
          .createCell(sheetParam1, 0, row, dto.getServiceCode(), style.get("cell"));
      excelWriterUtils.createCell(sheetParam1, 1, row, dto.getServiceName(), style.get("cell"));
      row++;
      k++;
    }
    sheetParam1.setColumnWidth(0, 13000);
    sheetParam1.setColumnWidth(1, 13000);

    List<SRMappingProcessCRDTO> lstProcess = srMappingProcessCRRepository.getListParentChil();
    row = 4;
    k = 1;
    if (lstProcess != null && !lstProcess.isEmpty()) {
      for (SRMappingProcessCRDTO item : lstProcess) {
        if (StringUtils.isNotNullOrEmpty(item.getCrProcessParentCode())) {
          excelWriterUtils
              .createCell(sheetParam2, 0, row, item.getCrProcessParentCode(), style.get("cell"));
          excelWriterUtils.createCell(sheetParam2, 1, row, item.getProcess(), style.get("cell"));
          excelWriterUtils
              .createCell(sheetParam2, 2, row, item.getCrProcessCode(), style.get("cell"));
          excelWriterUtils.createCell(sheetParam2, 3, row, item.getWo(), style.get("cell"));
          row++;
        }
      }
    }
    sheetParam2.setColumnWidth(0, 15000);
    sheetParam2.setColumnWidth(1, 15000);
    sheetParam2.setColumnWidth(2, 15000);
    sheetParam2.setColumnWidth(3, 15000);

    row = 4;
    k = 1;
    List<ItemDataCRInside> lstAffectedService = getLstAffectedServiceCBB();
    if (lstAffectedService != null && !lstAffectedService.isEmpty()) {
      for (ItemDataCRInside itemDataCRInside : lstAffectedService) {
        excelWriterUtils.createCell(sheetAffectedService, 0, row,
            (itemDataCRInside.getValueStr() != null ? itemDataCRInside.getValueStr().toString()
                : null),
            style.get("cell"));
        excelWriterUtils
            .createCell(sheetAffectedService, 1, row, itemDataCRInside.getDisplayStr(),
                style.get("cell"));
        row++;
      }
    }
    sheetAffectedService.setColumnWidth(0, 15000);
    sheetAffectedService.setColumnWidth(1, 15000);

    row = 4;
    k = 1;
    List<WoCdGroupInsideDTO> lstWoCdGroup = getLstWoCdGroupCBB();
    if (lstWoCdGroup != null && !lstWoCdGroup.isEmpty()) {
      for (WoCdGroupInsideDTO woCdGroupInsideDTO : lstWoCdGroup) {
        excelWriterUtils.createCell(sheetGroupCDFT, 0, row,
            (woCdGroupInsideDTO.getWoGroupId() != null ? woCdGroupInsideDTO.getWoGroupId()
                .toString()
                : null),
            style.get("cell"));
        excelWriterUtils
            .createCell(sheetGroupCDFT, 1, row, woCdGroupInsideDTO.getWoGroupName(),
                style.get("cell"));
        excelWriterUtils.createCell(sheetGroupCdFtService, 0, row,
            (woCdGroupInsideDTO.getWoGroupId() != null ? woCdGroupInsideDTO.getWoGroupId()
                .toString()
                : null),
            style.get("cell"));
        excelWriterUtils
            .createCell(sheetGroupCdFtService, 1, row, woCdGroupInsideDTO.getWoGroupName(),
                style.get("cell"));
        row++;
      }
    }

    sheetGroupCDFT.setColumnWidth(0, 15000);
    sheetGroupCDFT.setColumnWidth(1, 15000);
    sheetGroupCdFtService.setColumnWidth(0, 15000);
    sheetGroupCdFtService.setColumnWidth(1, 15000);

    row = 4;
    k = 1;
    List<UnitDTO> unitDTOS = unitBusiness.getListUnit(new UnitDTO());
    if (unitDTOS != null && !unitDTOS.isEmpty()) {
      for (UnitDTO unitDTO : unitDTOS) {
        excelWriterUtils.createCell(sheetUnitImplement, 0, row,
            (unitDTO.getUnitId() != null ? unitDTO.getUnitId().toString() : null),
            style.get("cell"));
        excelWriterUtils
            .createCell(sheetUnitImplement, 1, row, unitDTO.getUnitName(), style.get("cell"));
        row++;
      }
    }
    sheetUnitImplement.setColumnWidth(0, 15000);
    sheetUnitImplement.setColumnWidth(1, 15000);

    row = 4;
    k = 1;
    List<WoInsideDTO> lstPriority = getLstPriority();

    if (lstPriority != null && !lstPriority.isEmpty()) {
      for (WoInsideDTO woInsideDTO : lstPriority) {
        excelWriterUtils.createCell(sheetPriority, 0, row,
            (woInsideDTO.getPriorityId() != null ? woInsideDTO.getPriorityId().toString()
                : null),
            style.get("cell"));
        excelWriterUtils
            .createCell(sheetPriority, 1, row, woInsideDTO.getPriorityName(),
                style.get("cell"));
        row++;
      }
    }
    sheetPriority.setColumnWidth(0, 15000);
    sheetPriority.setColumnWidth(1, 15000);

    row = 4;
    k = 1;
    List<WoTypeInsideDTO> lstWoFT = getListWoTypeDTOCBB(new WoTypeInsideDTO());

    if (lstWoFT != null && !lstWoFT.isEmpty()) {
      for (WoTypeInsideDTO woTypeInsideDTO : lstWoFT) {
        excelWriterUtils.createCell(sheetCoordinationFT, 0, row,
            (woTypeInsideDTO.getWoTypeId() != null ? woTypeInsideDTO.getWoTypeId().toString()
                : null),
            style.get("cell"));
        excelWriterUtils
            .createCell(sheetCoordinationFT, 1, row, woTypeInsideDTO.getWoTypeName(),
                style.get("cell"));
        row++;
      }
    }
    sheetCoordinationFT.setColumnWidth(0, 15000);
    sheetCoordinationFT.setColumnWidth(1, 15000);

    //set tên trang excel
    workbook.setSheetName(0, I18n.getLanguage("SrMappingProcessCr.title.import.list"));
    workbook.setSheetName(1, "Other");
    workbook.setSheetName(2, I18n.getLanguage("srCatalog.serviceCode"));
    workbook.setSheetName(3, I18n.getLanguage("SrMappingProcessCr.processWo"));
    workbook.setSheetName(4, I18n.getLanguage("SrMappingProcessCr.unitImplementName"));
    workbook.setSheetName(5, I18n.getLanguage("SrMappingProcessCr.affectingService"));
    workbook.setSheetName(6, I18n.getLanguage("SrMappingProcessCr.groupCDFT.title"));
    workbook.setSheetName(7, I18n.getLanguage("SrMappingProcessCr.groupCdFtService.title"));
    workbook.setSheetName(8, I18n.getLanguage("SrMappingProcessCr.woPriority"));
    workbook.setSheetName(9, I18n.getLanguage("SrMappingProcessCr.woFtTypeIdStr"));

    workbook.setSheetHidden(1, true);
    sheetParam.setSelected(false);
    //set tên file excel
    String fileResult = tempFolder + File.separator;
    String fileName = "SR_MAPPING_CR_TEMPLATE.xlsx";
    excelWriterUtils.saveToFileExcel(workbook, fileResult, fileName);
    String resultPath = fileResult + fileName;
    File fileExport = new File(resultPath);
    return fileExport;
  }

  private void setMapAffectedService() {
    List<ItemDataCRInside> lstAffectedService = getLstAffectedServiceCBB();
    if (lstAffectedService != null && !lstAffectedService.isEmpty()) {
      for (ItemDataCRInside itemDataCRInside : lstAffectedService) {
        if (!mapAffectedService.containsKey(itemDataCRInside.getValueStr())) {
          mapAffectedService.put(itemDataCRInside.getValueStr(), itemDataCRInside.getDisplayStr());
        }
      }
    }
  }

  private void setMapWoCdGroup() {
    List<WoCdGroupInsideDTO> lstWoCdGroup = getLstWoCdGroupCBB();
    if (lstWoCdGroup != null && !lstWoCdGroup.isEmpty()) {
      for (WoCdGroupInsideDTO woCdGroupInsideDTO : lstWoCdGroup) {
        if (!mapWoCdGroup.containsKey(woCdGroupInsideDTO.getWoGroupId())) {
          mapWoCdGroup.put(woCdGroupInsideDTO.getWoGroupId(), woCdGroupInsideDTO.getWoGroupName());
        }
      }
    }
  }

  private void setMapUnitImplement() {
    List<UnitDTO> unitDTOS = unitBusiness.getListUnit(new UnitDTO());
    if (unitDTOS != null && !unitDTOS.isEmpty()) {
      for (UnitDTO unitDTO : unitDTOS) {
        if (!mapUnitImplement.containsKey(unitDTO.getUnitId())) {
          mapUnitImplement.put(unitDTO.getUnitId(), unitDTO.getUnitName());
        }
      }
    }
  }

  private void setMapDutyType() {
    List<ItemDataCRInside> listDutyTypeCBB = getLstDutyTypeCBB();
    if (listDutyTypeCBB != null && !listDutyTypeCBB.isEmpty()) {
      for (ItemDataCRInside itemDataCRInside : listDutyTypeCBB) {
        if (!mapDutyType.containsKey(itemDataCRInside.getValueStr())) {
          mapDutyType.put(itemDataCRInside.getValueStr(), itemDataCRInside.getDisplayStr());
        }
      }
    }
  }

  private void setMapWoPriority() {
    List<WoInsideDTO> listPriority = getLstPriority();
    if (listPriority != null && !listPriority.isEmpty()) {
      for (WoInsideDTO woInsideDTO : listPriority) {
        if (!mapWoPriority.containsKey(woInsideDTO.getPriorityId())) {
          mapWoPriority.put(woInsideDTO.getPriorityId().toString(), woInsideDTO.getPriorityName());
        }
      }
    }
  }

  private void setMapWoTypeFT() {
    List<WoTypeInsideDTO> listWoTypeDTO = getListWoTypeDTOCBB(new WoTypeInsideDTO());
    if (listWoTypeDTO != null && !listWoTypeDTO.isEmpty()) {
      for (WoTypeInsideDTO woTypeInsideDTO : listWoTypeDTO) {
        if (!mapWoTypeFT.containsKey(woTypeInsideDTO.getWoTypeId())) {
          mapWoTypeFT
              .put(woTypeInsideDTO.getWoTypeId().toString(), woTypeInsideDTO.getWoTypeName());
        }
      }
    }
  }

  @Override
  public ResultInSideDto importData(MultipartFile multipartFile) throws Exception {
    log.info("Request to importData : {}", multipartFile);
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(RESULT.SUCCESS);
    // Lay du lieu import
    if (multipartFile == null || multipartFile.isEmpty()) {
      resultInSideDTO.setKey(RESULT.FILE_IS_NULL);
      return resultInSideDTO;
    } else {
      String filePath = FileUtils
          .saveTempFile(multipartFile.getOriginalFilename(), multipartFile.getBytes(), tempFolder);
      if (!RESULT.SUCCESS.equals(resultInSideDTO.getKey())) {
        return resultInSideDTO;
      }
      File fileImport = new File(filePath);

      List<Object[]> lstHeader;
      lstHeader = CommonImport.getDataFromExcelFile(fileImport,
          0,//sheet
          7,//begin row
          0,//from column
          23,//to column
          2);

      if (lstHeader.size() == 0 || !validFileFormat(lstHeader)) {
        resultInSideDTO.setKey(RESULT.FILE_INVALID_FORMAT);
        return resultInSideDTO;
      }

      // Lay du lieu import
      List<Object[]> lstData = CommonImport.getDataFromExcelFileNew(
          fileImport,
          0,//sheet
          8,//begin row
          0,//from column
          23,//to column
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

      List<SRMappingProcessCRDTO> lstAddOrUpdate = new ArrayList<>();
      List<SRMappingProcessCRDTO> lstError = new ArrayList<>();
      mapService.clear();
      mapProcess.clear();
      mapAddObject.clear();
      mapWo.clear();
      mapAffectedService.clear();
      mapWoCdGroup.clear();
      mapUnitImplement.clear();
      mapWoPriority.clear();
      mapWoTypeFT.clear();
      setMapAffectedService();
      setMapWoCdGroup();
      setMapWo();
      setMapService();
      setMapProcess();
      setMapUnitImplement();
      setMapWoPriority();
      setMapWoTypeFT();
      if (lstData != null && lstData.size() > 0) {
        for (Object[] obj : lstData) {
          SRMappingProcessCRDTO dto = new SRMappingProcessCRDTO();
          int column = 0;
          column++;
          if (obj[column] != null) {
            dto.setServiceCode(obj[column].toString().trim());
          } else {
            dto.setServiceCode("");
          }
          column++;
          if (obj[column] != null) {
            dto.setCrProcessParentCode(obj[column].toString());
          } else {
            dto.setCrProcessParentCode(null);
          }
          column++;
          if (obj[column] != null) {
            dto.setCrProcessCode(obj[column].toString().trim());
          } else {
            dto.setCrProcessCode(null);
          }
          column++;
          if (obj[column] != null) {
            dto.setAutoCreateCRStr(obj[column].toString().trim());
          } else {
            dto.setAutoCreateCRStr(null);
          }
          column++;
          if (obj[column] != null) {
            dto.setIsCrNodesStr(obj[column].toString().trim());
          } else {
            dto.setIsCrNodesStr(null);
          }
          column++;
          if (obj[column] != null) {
            dto.setCrTitle(obj[column].toString().trim());
          } else {
            dto.setCrTitle(null);
          }
          column++;
          if (obj[column] != null) {
            dto.setDescriptionCr(obj[column].toString().trim());
          } else {
            dto.setDescriptionCr(null);
          }
          column++;
          if (obj[column] != null) {
            dto.setUnitImplementName(obj[column].toString().trim());
          } else {
            dto.setUnitImplementName(null);
          }
          column++;
          if (obj[column] != null) {
            dto.setServiceAffectingStr(obj[column].toString().trim());
          } else {
            dto.setServiceAffectingStr(null);
          }
          column++;
          if (obj[column] != null) {
            dto.setAffectingService(obj[column].toString().trim());
          } else {
            dto.setAffectingService(null);
          }
          column++;
          if (obj[column] != null) {
            dto.setTotalAffectingCustomersStr(obj[column].toString().trim());
          } else {
            dto.setTotalAffectingCustomersStr(null);
          }
          column++;
          if (obj[column] != null) {
            dto.setTotalAffectingMinutesStr(obj[column].toString().trim());
          } else {
            dto.setTotalAffectingMinutesStr(null);
          }
          column++;
          if (obj[column] != null) {
            dto.setWoFtTypeIdStr(obj[column].toString().trim());
          } else {
            dto.setWoFtTypeIdStr(null);
          }
          column++;
          if (obj[column] != null) {
            dto.setWoContentCDFT(obj[column].toString().trim());
          } else {
            dto.setWoContentCDFT(null);
          }
          column++;
          if (obj[column] != null) {
            dto.setWoFtDescription(obj[column].toString().trim());
          } else {
            dto.setWoFtDescription(null);
          }
          column++;
          if (obj[column] != null) {
            dto.setGroupCDFTStr(obj[column].toString().trim());
          } else {
            dto.setGroupCDFTStr(null);
          }
          column++;
          if (obj[column] != null) {
            dto.setWoFtPriorityStr(obj[column].toString().trim());
          } else {
            dto.setWoFtPriorityStr(null);
          }
          column++;
          if (obj[column] != null) {
            dto.setWoTestTypeIdStr(obj[column].toString().trim());
          } else {
            dto.setWoTestTypeIdStr(null);
          }
          column++;
          if (obj[column] != null) {
            dto.setWoContentService(obj[column].toString().trim());
          } else {
            dto.setWoContentService(null);
          }
          column++;
          if (obj[column] != null) {
            dto.setWoTestDescription(obj[column].toString().trim());
          } else {
            dto.setWoTestDescription(null);
          }
          column++;
          if (obj[column] != null) {
            dto.setGroupCdFtServiceStr(obj[column].toString().trim());
          } else {
            dto.setGroupCdFtServiceStr(null);
          }
          column++;
          if (obj[column] != null) {
            dto.setWoTestPriorityStr(obj[column].toString().trim());
          } else {
            dto.setWoTestPriorityStr(null);
          }
          if (validateImportInfo(dto)) {
            List<SRMappingProcessCRDTO> lstUpdate = srMappingProcessCRRepository
                .getListAllMappingProcessCR(dto);
            if (lstUpdate == null) {
              lstAddOrUpdate.add(dto);
            } else {
              dto.setId(lstUpdate.get(0).getId());
              lstAddOrUpdate.add(dto);
            }

          }
          lstError.add(dto);
        }

        boolean check = false;
        for (SRMappingProcessCRDTO item : lstError) {
          if (!StringUtils.isStringNullOrEmpty(item.getResultImport())) {
            check = true;
            break;
          }
        }

        if (check) {
          for (SRMappingProcessCRDTO item : lstError) {
            if (StringUtils.isStringNullOrEmpty(item.getResultImport())) {
              item.setResultImport(I18n.getLanguage("SrMappingProcessCr.record.import"));
            }
          }

          File expFile = exportFileSrMappingCr(lstError, "RESULT_IMPORT", true);
          resultInSideDTO.setKey(RESULT.ERROR);
          resultInSideDTO.setFile(expFile);
        } else {
          if (!lstAddOrUpdate.isEmpty()) {
            for (SRMappingProcessCRDTO item : lstAddOrUpdate) {
              List<SRMappingProcessCRDTO> lstCrProcessId = srMappingProcessCRRepository
                  .getCrProcessIdOrWoId(item.getCrProcessParentCode());
              String crProcessId = "";
              if (StringUtils.isNotNullOrEmpty(item.getCrProcessCode())) {
                String[] arrProcessTypeLv3Id = item.getCrProcessCode().split(",");
                for (String processTypeLv3Id : arrProcessTypeLv3Id) {
                  List<SRMappingProcessCRDTO> lstWoId = srMappingProcessCRRepository
                      .getCrProcessIdOrWoId(processTypeLv3Id);
                  if (lstWoId != null && !lstWoId.isEmpty()
                      && lstWoId.get(0).getCrProcessId() != null
                      && lstWoId.get(0).getCrProcessId() > 0) {
                    crProcessId += lstWoId.get(0).getCrProcessId().toString() + ",";
                  }
                }
              }
              item.setCrProcessParentId(lstCrProcessId.get(0).getCrProcessId());
              if (StringUtils.isNotNullOrEmpty(crProcessId) && crProcessId.endsWith(",")) {
                item.setProcessTypeLv3Id(crProcessId.substring(0, crProcessId.length() - 1));
              }
              List<ItemDataCRInside> lst = getDutyTypeByProcessId(item.getCrProcessParentId());
              if (lst != null && !lst.isEmpty()) {
                item.setDutyType(lst.get(0).getValueStr());
              }
              Map<String, String> mapStatus = getListStatusCrCBB(item.getCrProcessParentId());
              if (!mapStatus.isEmpty()) {
                for (Map.Entry<String, String> map : mapStatus.entrySet()) {
                  item.setCrStatus(Long.parseLong(map.getKey()));
                  break;
                }
              }
              resultInSideDTO = srMappingProcessCRRepository.insertOrUpdate(item);
            }

            if (resultInSideDTO.getKey().equals(Constants.RESULT.SUCCESS)) {
              UserToken userToken = ticketProvider.getUserToken();
              commonStreamServiceProxy.insertLog(new LogChangeConfigDTO(userToken.getUserName(),
                  "Import SRMappingProcessCR",
                  "Import SRMappingProcessCR: " + resultInSideDTO.getId(),
                  null, null));
            }
          }
        }

      }
    }

    return resultInSideDTO;
  }

  private boolean validateImportInfo(SRMappingProcessCRDTO dto) {
    if (StringUtils.isStringNullOrEmpty(dto.getServiceCode())) {
      dto.setResultImport(
          I18n.getLanguage("srMapping.import.service.notnull"));
      return false;
    } else if (!mapService.containsKey(dto.getServiceCode())) {
      dto.setResultImport(I18n.getLanguage("srMapping.import.service.notexist"));
      return false;
    } else if (dto.getServiceCode().length() > 200) {
      dto.setResultImport(I18n.getLanguage("SrMappingProcessCr.serviceCode") + " " + I18n
          .getLanguage("srConfig.import.codeOverLength"));
      return false;
    }
    if (StringUtils.isStringNullOrEmpty(dto.getCrProcessParentCode())) {
      dto.setResultImport(I18n.getLanguage("srMapping.import.process.notnull"));
      return false;
    } else if (!mapProcess.containsKey(dto.getCrProcessParentCode())) {
      dto.setResultImport(I18n.getLanguage("srMapping.import.process.notexist"));
      return false;
    }
    if (StringUtils.isNotNullOrEmpty(dto.getCrProcessCode())) {
      String[] arrProcessCode = dto.getCrProcessCode().split(",");
      if (arrProcessCode != null && arrProcessCode.length > 0) {
        List<SRMappingProcessCRDTO> lstWoId = srMappingProcessCRRepository
            .getCrProcessIdOrWoId(dto.getCrProcessParentCode());
        List<SRMappingProcessCRDTO> lstChil = srMappingProcessCRRepository
            .getListWo(lstWoId.get(0).getCrProcessId());
        for (String processTypeLv3 : arrProcessCode) {
          boolean check = false;
          if (!mapWo.containsKey(processTypeLv3)) {
            dto.setResultImport(I18n.getLanguage("srMapping.import.wo.notexist"));
            return false;
          }
          if (lstChil != null && !lstChil.isEmpty()) {
            for (SRMappingProcessCRDTO srMapDTO : lstChil) {
              if (srMapDTO.getCrProcessCode().equals(processTypeLv3)) {
                check = true;
              }
            }
            if (!check) {
              dto.setResultImport(
                  processTypeLv3 + " : " + I18n.getLanguage("srMapping.import.parent.notexist"));
              return false;
            }
          } else {
            dto.setResultImport(
                processTypeLv3 + " : " + I18n.getLanguage("srMapping.import.parent.notexist"));
            return false;
          }
        }
      }
    }
    if (StringUtils.isNotNullOrEmpty(dto.getAutoCreateCRStr())) {
      if (I18n.getLanguage("srMappingProcessCR.common.0").equals(dto.getAutoCreateCRStr())) {
        dto.setAutoCreateCR(0L);
      } else if (I18n.getLanguage("srMappingProcessCR.common.1")
          .equals(dto.getAutoCreateCRStr())) {
        dto.setAutoCreateCR(1L);
      } else {
        dto.setResultImport(I18n.getLanguage("srMappingProcessCR.autoCreateCRStr.notExist"));
        return false;
      }
      if (dto.getAutoCreateCR() != null && dto.getAutoCreateCR() == 1L) {
        if (StringUtils.isNotNullOrEmpty(dto.getIsCrNodesStr())) {
          if (I18n.getLanguage("srCatalog.isCrNodes.1").equals(dto.getIsCrNodesStr())) {
            dto.setIsCrNodes(1L);
          } else if (I18n.getLanguage("srCatalog.isCrNodes.0")
              .equals(dto.getIsCrNodesStr())) {
            dto.setIsCrNodes(0L);
          } else {
            dto.setResultImport(I18n.getLanguage("srMappingProcessCR.isCrNodes.notExist"));
            return false;
          }
        } else {
          dto.setResultImport(I18n.getLanguage("srMappingProcessCR.isCrNodes.notnull"));
          return false;
        }
        if (StringUtils.isStringNullOrEmpty(dto.getCrTitle())) {
          dto.setResultImport(I18n.getLanguage("srMappingProcessCR.crTitle.notnull"));
          return false;
        }
        if (!StringUtils.isStringNullOrEmpty(dto.getCrTitle()) && dto.getCrTitle().length() > 500) {
          dto.setResultImport(I18n.getLanguage("srMappingProcessCR.crTitle.tooLong"));
          return false;
        }
        if (StringUtils.isNotNullOrEmpty(dto.getUnitImplementName())) {
          if (DataUtil.isLong(dto.getUnitImplementName())) {
            if (mapUnitImplement.containsKey(Long.parseLong(dto.getUnitImplementName()))) {
              dto.setUnitImplement(Long.parseLong(dto.getUnitImplementName()));
            } else {
              dto.setResultImport(
                  I18n.getLanguage("srMappingProcessCR.unitImplementName.notExist"));
              return false;
            }
          } else {
            dto.setResultImport(I18n.getLanguage("srMappingProcessCR.unitImplementName.notExist"));
            return false;
          }
        }
        if (StringUtils.isNotNullOrEmpty(dto.getServiceAffectingStr())) {
          if (I18n.getLanguage("srMappingProcessCR.common.0")
              .equals(dto.getServiceAffectingStr())) {
            dto.setServiceAffecting(0L);
          } else if (I18n.getLanguage("srMappingProcessCR.common.1")
              .equals(dto.getServiceAffectingStr())) {
            dto.setServiceAffecting(1L);
          } else {
            dto.setResultImport(
                I18n.getLanguage("srMappingProcessCR.serviceAffectingStr.notExist"));
            return false;
          }
          if (dto.getServiceAffecting() != null && dto.getServiceAffecting() == 1L) {
            if (StringUtils.isNotNullOrEmpty(dto.getAffectingService())) {
              String[] arrAffectingService = dto.getAffectingService().split(",");
              if (arrAffectingService != null && arrAffectingService.length > 0) {
                for (int i = 0; i < arrAffectingService.length; i++) {
                  if (DataUtil.isLong(arrAffectingService[i])) {
                    if (!mapAffectedService.containsKey(Long.parseLong(arrAffectingService[i]))) {
                      dto.setResultImport(arrAffectingService[i] + " : " + I18n
                          .getLanguage("srMappingProcessCR.serviceAffecting.notExist"));
                      return false;
                    }
                  } else {
                    dto.setResultImport(
                        I18n.getLanguage("srMappingProcessCR.serviceAffecting.notExist"));
                    return false;
                  }
                }
              }
            } else {
              dto.setResultImport(I18n.getLanguage("srMappingProcessCR.serviceAffecting.notnull"));
              return false;
            }
            if (StringUtils.isNotNullOrEmpty(dto.getTotalAffectingCustomersStr())) {
              if (DataUtil.isInteger(dto.getTotalAffectingCustomersStr())) {
                dto.setTotalAffectingCustomers(Long.parseLong(dto.getTotalAffectingCustomersStr()));
              } else {
                dto.setResultImport(
                    I18n.getLanguage("srMappingProcessCR.totalAffectingCustomersStr.tooLong"));
                return false;
              }
            } else {
              dto.setResultImport(
                  I18n.getLanguage("srMappingProcessCR.totalAffectingCustomersStr.notnull"));
              return false;
            }

            if (StringUtils.isNotNullOrEmpty(dto.getTotalAffectingMinutesStr())) {
              if (DataUtil.isInteger(dto.getTotalAffectingMinutesStr())) {
                dto.setTotalAffectingMinutes(Long.parseLong(dto.getTotalAffectingMinutesStr()));
              } else {
                dto.setResultImport(
                    I18n.getLanguage("srMappingProcessCR.totalAffectingMinutesStr.tooLong"));
                return false;
              }
            } else {
              dto.setResultImport(
                  I18n.getLanguage("srMappingProcessCR.totalAffectingMinutesStr.notnull"));
              return false;
            }

          }
        }

        if (StringUtils.isNotNullOrEmpty(dto.getWoFtTypeIdStr())) {
          if (mapWoTypeFT.containsKey(dto.getWoFtTypeIdStr())) {
            dto.setWoFtTypeId(Long.parseLong(dto.getWoTestTypeIdStr()));
          } else {
            dto.setResultImport(I18n.getLanguage("srMappingProcessCR.woFtTypeIdStr.notExist"));
            return false;
          }
          if (dto.getWoFtTypeId() != null) {
            if (StringUtils.isStringNullOrEmpty(dto.getWoContentCDFT())) {
              dto.setResultImport(I18n.getLanguage("srMappingProcessCR.woContentCDFT.notnull"));
              return false;
            }
            if (!StringUtils.isStringNullOrEmpty(dto.getWoFtDescription())
                && dto.getWoFtDescription().length() > 2000) {
              dto.setResultImport(I18n.getLanguage("srMappingProcessCR.woFtDescription.tooLong"));
              return false;
            }
            if (StringUtils.isNotNullOrEmpty(dto.getGroupCDFTStr())) {
              if (DataUtil.isLong(dto.getGroupCDFTStr())) {
                if (mapWoCdGroup.containsKey(Long.parseLong(dto.getGroupCDFTStr()))) {
                  dto.setGroupCDFT(Long.parseLong(dto.getGroupCDFTStr()));
                } else {
                  dto.setResultImport(I18n.getLanguage("srMappingProcessCR.groupCDFTStr.notExist"));
                  return false;
                }
              } else {
                dto.setResultImport(I18n.getLanguage("srMappingProcessCR.groupCDFTStr.notExist"));
                return false;
              }
            } else {
              dto.setResultImport(I18n.getLanguage("srMappingProcessCR.groupCDFTStr.notnull"));
              return false;
            }
            if (StringUtils.isNotNullOrEmpty(dto.getWoFtPriorityStr())) {
              if (mapWoPriority.containsKey(dto.getWoFtPriorityStr())) {
                dto.setWoFtPriority(Long.parseLong(dto.getWoFtPriorityStr()));
              } else {
                dto.setResultImport(
                    I18n.getLanguage("srMappingProcessCR.woFtPriorityStr.notExist"));
                return false;
              }
            } else {
              dto.setResultImport(I18n.getLanguage("srMappingProcessCR.woFtPriorityStr.notnull"));
              return false;
            }
          }
        }
        if (StringUtils.isNotNullOrEmpty(dto.getWoTestTypeIdStr())) {
          if (mapWoTypeFT.containsKey(dto.getWoTestTypeIdStr())) {
            dto.setWoTestTypeId(Long.parseLong(dto.getWoTestTypeIdStr()));
          } else {
            dto.setResultImport(I18n.getLanguage("srMappingProcessCR.woTestTypeIdStr.notExist"));
            return false;
          }
          if (dto.getWoTestTypeId() != null) {
            if (StringUtils.isStringNullOrEmpty(dto.getWoContentService())) {
              dto.setResultImport(I18n.getLanguage("srMappingProcessCR.woContentService.notnull"));
              return false;
            }
            if (!StringUtils.isStringNullOrEmpty(dto.getWoTestDescription())
                && dto.getWoTestDescription().length() > 2000) {
              dto.setResultImport(I18n.getLanguage("srMappingProcessCR.woTestDescription.tooLong"));
              return false;
            }
            if (StringUtils.isNotNullOrEmpty(dto.getGroupCdFtServiceStr())) {
              if (DataUtil.isLong(dto.getGroupCdFtServiceStr())) {
                if (mapWoCdGroup.containsKey(Long.parseLong(dto.getGroupCdFtServiceStr()))) {
                  dto.setGroupCdFtService(Long.parseLong(dto.getGroupCdFtServiceStr()));
                } else {
                  dto.setResultImport(
                      I18n.getLanguage("srMappingProcessCR.groupCdFtServiceStr.notExist"));
                  return false;
                }
              } else {
                dto.setResultImport(
                    I18n.getLanguage("srMappingProcessCR.groupCdFtServiceStr.notExist"));
                return false;
              }
            } else {
              dto.setResultImport(
                  I18n.getLanguage("srMappingProcessCR.groupCdFtServiceStr.notnull"));
              return false;
            }
            if (StringUtils.isNotNullOrEmpty(dto.getWoTestPriorityStr())) {
              if (mapWoPriority.containsKey(dto.getWoTestPriorityStr())) {
                dto.setWoTestPriority(Long.parseLong(dto.getWoTestPriorityStr()));
              } else {
                dto.setResultImport(
                    I18n.getLanguage("srMappingProcessCR.woTestPriorityStr.notExist"));
                return false;
              }
            } else {
              dto.setResultImport(I18n.getLanguage("srMappingProcessCR.woTestPriorityStr.notnull"));
              return false;
            }
          }
        }
      }
    }

    if (mapAddObject.get(dto.getServiceCode() + dto.getCrProcessParentCode()) == null) {
      mapAddObject.put(dto.getServiceCode() + dto.getCrProcessParentCode(), dto.getServiceCode());
    } else {
      dto.setResultImport(I18n.getLanguage("srCatalog.import.updateCatalogDuplicate"));
      return false;
    }
    return true;
  }

  private File exportFileSrMappingCr(List<SRMappingProcessCRDTO> lstCrEx, String key,
      boolean exportErr) throws
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
      sheetName = I18n.getLanguage("SrMappingProcessCr.title.import.list");
      title = I18n.getLanguage("SrMappingProcessCr.title.import.list");
      lstHeaderSheet = readerHeaderSheet("serviceCode", "crProcessParentCode", "crProcessCode",
          "autoCreateCRStr",
          "isCrNodesStr", "crTitle", "descriptionCr", "unitImplementName", "crStatusStr",
          "dutyTypeStr",
          "serviceAffectingStr", "affectingService", "totalAffectingCustomersStr",
          "totalAffectingMinutesStr", "woFtTypeIdStr", "woContentCDFT", "woFtDescription",
          "groupCDFTStr", "woFtPriorityStr",
          "woTestTypeIdStr", "woContentService", "woTestDescription", "groupCdFtServiceStr",
          "woTestPriorityStr"
      );
    } else {
      sheetName = I18n.getLanguage("SrMappingProcessCr.export.title");
      title = I18n.getLanguage("SrMappingProcessCr.export.title");
      lstHeaderSheet = readerHeaderSheet("serviceCode", "serviceName", "process",
          "processTypeLv3IdName",
          "autoCreateCRStr",
          "isCrNodesStr", "crTitle", "descriptionCr", "unitImplementName", "dutyTypeStr",
          "serviceAffectingStr", "affectingService", "totalAffectingCustomers",
          "totalAffectingMinutes", "woFtTypeIdStr", "woContentCDFT", "woFtDescription",
          "groupCDFTStr", "woFtPriorityStr",
          "woTestTypeIdStr", "woContentService", "woTestDescription", "groupCdFtServiceStr",
          "woTestPriorityStr"
      );
      fileNameOut = SR_MAPPING_PROCESS_CR_EXPORT;
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
        "language.SrMappingProcessCr",
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

    File fileExport = exportExcelMapping(
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

  public File exportExcelMapping(
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
          Cell firstRightHeader = headerFirstTitle.createCell(sizeheader - 2);
          firstRightHeader.setCellStyle(cellStyleTopRightHeader);
          Cell secondRightHeader = headerSecondTitle.createCell(sizeheader - 2);
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
          sheet.addMergedRegion(new CellRangeAddress(0, 0, sizeheader - 2,
              4));
          sheet.addMergedRegion(new CellRangeAddress(1, 1, sizeheader - 2,
              4));
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
            I18n.getLanguage("SrMappingProcessCr.serviceCode"),
            I18n.getLanguage("SrMappingProcessCr.processCode"),
            I18n.getLanguage("SrMappingProcessCr.woCode")
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

      createComboMapping(workbook);

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

  public void createComboMapping(SXSSFWorkbook workbook) {
    createService(workbook);
    createProcess(workbook);
  }

  public void createService(SXSSFWorkbook workbook) {
    ExcelWriterUtils ewu = new ExcelWriterUtils();
    Map<String, CellStyle> styles = CommonExport.createStyles(workbook);
    XSSFSheet sheetOrther = workbook.getXSSFWorkbook()
        .createSheet(I18n.getLanguage("SrMappingProcessCr.service"));
    ewu.createCell(sheetOrther, 0, 0,
        I18n.getLanguage("SrMappingProcessCr.serviceCode"), styles.get("header"));
    ewu.createCell(sheetOrther, 1, 0,
        I18n.getLanguage("SrMappingProcessCr.serviceName"), styles.get("header"));

    List<SRCatalogDTO> lstServices = srCatalogBusiness.getListServiceNameToMapping();

    int row = 1;
    if (lstServices != null && !lstServices.isEmpty()) {
      for (SRCatalogDTO item : lstServices) {
        ewu.createCell(sheetOrther, 0, row, item.getServiceCode(), styles.get("cell"));
        ewu.createCell(sheetOrther, 1, row, item.getServiceName(), styles.get("cell"));
        row++;
      }
    }

    sheetOrther.setColumnWidth(0, 13000);
    sheetOrther.setColumnWidth(1, 13000);
  }

  public void createProcess(SXSSFWorkbook workbook) {
    ExcelWriterUtils ewu = new ExcelWriterUtils();
    Map<String, CellStyle> styles = CommonExport.createStyles(workbook);
    XSSFSheet sheetOrther = workbook.getXSSFWorkbook()
        .createSheet(I18n.getLanguage("SrMappingProcessCr.processWo"));
    ewu.createCell(sheetOrther, 0, 0,
        I18n.getLanguage("SrMappingProcessCr.processCode"), styles.get("header"));
    ewu.createCell(sheetOrther, 1, 0,
        I18n.getLanguage("SrMappingProcessCr.processName"), styles.get("header"));
    ewu.createCell(sheetOrther, 2, 0,
        I18n.getLanguage("SrMappingProcessCr.woCode"), styles.get("header"));
    ewu.createCell(sheetOrther, 3, 0,
        I18n.getLanguage("SrMappingProcessCr.woName"), styles.get("header"));

    List<SRMappingProcessCRDTO> lstServices = srMappingProcessCRRepository.getListParentChil();

    int row = 1;
    if (lstServices != null && !lstServices.isEmpty()) {
      for (SRMappingProcessCRDTO item : lstServices) {
        ewu.createCell(sheetOrther, 0, row, item.getCrProcessParentCode(), styles.get("cell"));
        ewu.createCell(sheetOrther, 1, row, item.getProcess(), styles.get("cell"));
        ewu.createCell(sheetOrther, 2, row, item.getCrProcessCode(), styles.get("cell"));
        ewu.createCell(sheetOrther, 3, row, item.getWo(), styles.get("cell"));
        row++;
      }
    }
    sheetOrther.setColumnWidth(0, 9000);
    sheetOrther.setColumnWidth(1, 13000);
    sheetOrther.setColumnWidth(2, 13000);
    sheetOrther.setColumnWidth(3, 13000);
  }

  private ResultInSideDto validate(SRMappingProcessCRDTO dto) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.ERROR);
    if (dto.getAutoCreateCR() != null && dto.getAutoCreateCR() == 1L) {
      if (StringUtils.isStringNullOrEmpty(dto.getCrTitle())) {
        resultInSideDto.setMessage(I18n.getValidation("cr.crTitle.required"));
        return resultInSideDto;
      }
      if (!StringUtils.isStringNullOrEmpty(dto.getCrTitle()) && dto.getCrTitle().length() > 500) {
        resultInSideDto.setMessage(I18n.getLanguage("srMappingProcessCR.crTitle.tooLong"));
        return resultInSideDto;
      }
      if (StringUtils.isStringNullOrEmpty(dto.getDutyType())) {
        resultInSideDto.setMessage(I18n.getValidation("cr.dutyType.required"));
        return resultInSideDto;
      }
      if (StringUtils.isStringNullOrEmpty(dto.getCrStatus())) {
        resultInSideDto.setMessage(I18n.getValidation("cr.crStatus.required"));
        return resultInSideDto;
      }
      if (dto.getServiceAffecting() != null && dto.getServiceAffecting() == 1L) {
        if (StringUtils.isStringNullOrEmpty(dto.getAffectingService())) {
          resultInSideDto.setMessage(I18n.getValidation("cr.affectingService.required"));
          return resultInSideDto;
        }
        if (StringUtils.isStringNullOrEmpty(dto.getTotalAffectingCustomers())) {
          resultInSideDto.setMessage(I18n.getValidation("cr.totalAffectingCustomers.required"));
          return resultInSideDto;
        } else {
          try {
            Long totalCustomers = dto.getTotalAffectingCustomers();
            if (totalCustomers <= 0) {
              resultInSideDto.setMessage(I18n.getValidation("cr.totalAffectingCustomers.negative"));
              return resultInSideDto;
            }
          } catch (Exception e) {
            resultInSideDto.setMessage(I18n.getValidation("cr.totalAffectingCustomers.notNumber"));
            log.error(e.getMessage(), e);
            return resultInSideDto;
          }
        }
        if (StringUtils.isStringNullOrEmpty(dto.getTotalAffectingMinutes())) {
          resultInSideDto.setMessage(I18n.getValidation("cr.totalAffectingMinutes.required"));
          return resultInSideDto;
        } else {
          try {
            Long totalMinutes = dto.getTotalAffectingMinutes();
            if (totalMinutes <= 0) {
              resultInSideDto.setMessage(I18n.getValidation("cr.totalAffectingMinutes.negative"));
              return resultInSideDto;
            }
          } catch (Exception e) {
            resultInSideDto.setMessage(I18n.getValidation("cr.totalAffectingMinutes.notNumber"));
            log.error(e.getMessage(), e);
            return resultInSideDto;
          }
        }
      }
      if (dto.getWoFtTypeId() != null) {
        if (StringUtils.isStringNullOrEmpty(dto.getWoContentCDFT())) {
          resultInSideDto.setMessage(I18n.getValidation("cr.woContent.CDFT.required"));
          return resultInSideDto;
        }
        if (!StringUtils.isStringNullOrEmpty(dto.getWoFtDescription())
            && dto.getWoFtDescription().length() > 2000) {
          resultInSideDto
              .setMessage(I18n.getLanguage("srMappingProcessCR.woFtDescription.tooLong"));
          return resultInSideDto;
        }
        if (StringUtils.isStringNullOrEmpty(dto.getGroupCDFT())) {
          resultInSideDto.setMessage(I18n.getValidation("cr.groupCDPT.required"));
          return resultInSideDto;
        }
        if (StringUtils.isStringNullOrEmpty(dto.getWoFtPriority())) {
          resultInSideDto.setMessage(I18n.getValidation("cr.woFtPriority.required"));
          return resultInSideDto;
        }
      }
      if (dto.getWoTestTypeId() != null) {
        if (StringUtils.isStringNullOrEmpty(dto.getWoContentService())) {
          resultInSideDto.setMessage(I18n.getValidation("cr.woContent.testService.required"));
          return resultInSideDto;
        }
        if (!StringUtils.isStringNullOrEmpty(dto.getWoTestDescription())
            && dto.getWoTestDescription().length() > 2000) {
          resultInSideDto
              .setMessage(I18n.getLanguage("srMappingProcessCR.woTestDescription.tooLong"));
          return resultInSideDto;
        }
        if (StringUtils.isStringNullOrEmpty(dto.getGroupCdFtService())) {
          resultInSideDto.setMessage(I18n.getValidation("cr.groupCDPT2.required"));
          return resultInSideDto;
        }
        if (StringUtils.isStringNullOrEmpty(dto.getWoTestPriority())) {
          resultInSideDto.setMessage(I18n.getValidation("cr.woTestPriority.required"));
          return resultInSideDto;
        }
      }
    }
    resultInSideDto.setKey(RESULT.SUCCESS);
    return resultInSideDto;
  }
}
