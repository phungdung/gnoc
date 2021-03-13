package com.viettel.gnoc.od.business;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.*;
import com.viettel.gnoc.commons.proxy.CommonStreamServiceProxy;
import com.viettel.gnoc.commons.repository.CatItemRepository;
import com.viettel.gnoc.commons.utils.CellConfigExport;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.ConfigFileExport;
import com.viettel.gnoc.commons.utils.ConfigHeaderExport;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.ExcelWriterUtils;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.od.dto.OdTypeDTO;
import com.viettel.gnoc.od.dto.OdTypeDetailDTO;
import com.viettel.gnoc.od.dto.OdTypeExportDTO;
import com.viettel.gnoc.od.repository.OdTypeDetailRepository;
import com.viettel.gnoc.od.repository.OdTypeRepository;
import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Row;
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

/**
 * @author NamTN
 */
@Service
@Transactional
@Slf4j
public class OdTypeBusinessImpl implements OdTypeBusiness {

  @Value("${application.temp.folder}")
  private String tempFolder;

  @Autowired
  protected OdTypeRepository odTypeRepository;

  @Autowired
  protected CatItemRepository catItemRepository;

  @Autowired
  protected OdTypeDetailRepository odTypeDetailRepository;

  @Autowired
  protected CommonStreamServiceProxy commonStreamServiceProxy;

  @Autowired
  protected TicketProvider ticketProvider;

  Map<String, String> mapOdGroupTypeName = new HashMap<>();
  Map<String, String> mapPriorityName = new HashMap<>();
  List<OdTypeDTO> listOdTypeDTOS = new ArrayList<>();
  private final static String OD_TYPE_RESULT_IMPORT = "OD_TYPE_RESULT_IMPORT";
  private final static String OD_TYPE_EXPORT = "OD_TYPE_EXPORT";
  private final static String OD_TYPE_SEQ = "OD_TYPE_SEQ";


  @Override
  public Datatable search(OdTypeDTO odTypeDTO) {
    log.debug("Request to search : {}", odTypeDTO);
    return odTypeRepository.search(odTypeDTO);
  }

  @Override
  public Datatable getListOdType(OdTypeDTO odTypeDTO) {
    log.info("Request to getListOdType : {}", odTypeDTO);
    return odTypeRepository.getListOdType(odTypeDTO);
  }

  @Override
  public ResultInSideDto delete(Long odTypeId) {
    log.debug("Request to delete : {}", odTypeId);
    ResultInSideDto resultInSideDto;
    OdTypeDTO oldHis = getDetail(odTypeId);
    resultInSideDto = odTypeRepository.delete(odTypeId);
    if (resultInSideDto.getKey().equals(Constants.RESULT.SUCCESS)) {
      // Add history
      try {
        UserToken userToken = ticketProvider.getUserToken();
        List<String> keys = getAllKeysDTO();
        DataHistoryChange dataHistoryChange = new DataHistoryChange();
        dataHistoryChange.setRecordId(odTypeId.toString());
        dataHistoryChange.setType("OD_TYPE_MANAGEMENT");
        dataHistoryChange.setActionType("delete");
        //Old Object History
        dataHistoryChange.setOldObject(oldHis);
        //New Object History
        dataHistoryChange.setNewObject(new OdTypeDTO());
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
  public ResultInSideDto deleteList(List<Long> listOdTypeId) {
    log.debug("Request to deleteList : {}", listOdTypeId);
    return odTypeRepository.deleteList(listOdTypeId);
  }

  @Override
  public OdTypeDTO getDetail(Long odTypeId) {
    log.debug("Request to getDetail : {}", odTypeId);
    OdTypeDTO odTypeDTO = odTypeRepository.getDetail(odTypeId);
    if (odTypeDTO != null) {
      List<OdTypeDetailDTO> listDetail = odTypeDTO.getOdTypeDetailDTOS();
      if (listDetail != null && !listDetail.isEmpty()) {
        for (int i = 0; i < listDetail.size(); i++) {
          if (listDetail.get(i).getId() == null) {
            listDetail.get(i).setOdTypeId(odTypeId);
          }
        }
      }
    }
    return odTypeDTO;
  }

  @Override
  public OdTypeDTO getInforByODType(String odTypeCode) {
    log.debug("Request to getInforByODType : {}", odTypeCode);
    OdTypeDTO odTypeDTO = odTypeRepository.checkOdTypeExist(odTypeCode);
    if (odTypeDTO != null && odTypeDTO.getOdTypeId() != null && odTypeDTO.getOdTypeId() > 0) {
      return getDetail(odTypeDTO.getOdTypeId());
    }
    return null;
  }

  @Override
  public ResultInSideDto add(OdTypeDTO odTypeDTO) {
    log.debug("Request to add : {}", odTypeDTO);
    ResultInSideDto resultInSideDto;
    resultInSideDto =  odTypeRepository.add(odTypeDTO);
    if (resultInSideDto.getKey().equals(Constants.RESULT.SUCCESS)) {
      // Add history
      try {
        UserToken userToken = ticketProvider.getUserToken();
        List<String> keys = getAllKeysDTO();
        DataHistoryChange dataHistoryChange = new DataHistoryChange();
        dataHistoryChange.setRecordId(resultInSideDto.getId().toString());
        dataHistoryChange.setType("OD_TYPE_MANAGEMENT");
        dataHistoryChange.setActionType("add");
        //Old Object History
        dataHistoryChange.setOldObject(new OdTypeDTO());
        //New Object History
        dataHistoryChange.setNewObject(odTypeDTO);
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
  public ResultInSideDto edit(OdTypeDTO odTypeDTO) {
    log.debug("Request to edit : {}", odTypeDTO);
    ResultInSideDto resultInSideDto;
    OdTypeDTO oldHis = getDetail(odTypeDTO.getOdTypeId());
    resultInSideDto = odTypeRepository.edit(odTypeDTO);
    if (resultInSideDto.getKey().equals(Constants.RESULT.SUCCESS)) {
      // Add history
      try {
        UserToken userToken = ticketProvider.getUserToken();
        List<String> keys = getAllKeysDTO();
        DataHistoryChange dataHistoryChange = new DataHistoryChange();
        dataHistoryChange.setRecordId(odTypeDTO.getOdTypeId().toString());
        dataHistoryChange.setType("OD_TYPE_MANAGEMENT");
        dataHistoryChange.setActionType("update");
        //Old Object History
        dataHistoryChange.setOldObject(oldHis);
        //New Object History
        dataHistoryChange.setNewObject(odTypeDTO);
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
  public String getSeqOdType() {
    log.debug("Request to getSeqOdType : {}");
    return odTypeRepository.getSeqOdType(OD_TYPE_SEQ);
  }

  public void setMapOdGroupTypeName() {
    List<CatItemDTO> lstOdGroupType = catItemRepository
        .getDataItem(Constants.CATEGORY.OD_GROUP_TYPE);
    if (lstOdGroupType != null && !lstOdGroupType.isEmpty()) {
      for (CatItemDTO catItemDTO : lstOdGroupType) {
        mapOdGroupTypeName.put(String.valueOf(catItemDTO.getItemId()), catItemDTO.getItemName());
      }
    }
  }

  public void setMapPriorityName() {
    List<CatItemDTO> lstPriority = catItemRepository.getDataItem(Constants.CATEGORY.OD_PRIORITY);
    if (lstPriority != null && !lstPriority.isEmpty()) {
      for (CatItemDTO catItemPriority : lstPriority) {
        mapPriorityName
            .put(String.valueOf(catItemPriority.getItemId()), catItemPriority.getItemName());
      }
    }
  }

  @Override
  public File exportData(OdTypeDTO odTypeDTO) throws Exception {
    setMapPriorityName();
    List<OdTypeExportDTO> odTypeTmp = new ArrayList<>();
    List<OdTypeExportDTO> odTypeExportDTOS = (List<OdTypeExportDTO>) odTypeRepository
        .getListDataExport(odTypeDTO).getData();
    if (odTypeExportDTOS != null && !odTypeExportDTOS.isEmpty()) {
      for (int i = odTypeExportDTOS.size() - 1; i > -1; i--) {
        OdTypeExportDTO item = odTypeExportDTOS.get(i);
        if ("1".equals(item.getStatus())) {
          item.setStatusName(I18n.getLanguage("odType.statusName.1"));
        } else {
          item.setStatusName(I18n.getLanguage("odType.statusName.0"));
        }
        if (item.getIdOdDetail() == null) {
          odTypeTmp.clear();
          for (String temp : mapPriorityName.values()) {
            OdTypeExportDTO dtoExport = item;
            dtoExport.setPriorityName(temp);
            dtoExport.setProcessTime("");
            odTypeTmp.add(dtoExport);
            odTypeExportDTOS.remove(item);
          }
          if (odTypeTmp != null && !odTypeTmp.isEmpty()) {
            odTypeExportDTOS.addAll(i, odTypeTmp);
          }
        }
      }
    }
    return exportFileEx(odTypeExportDTOS, "");
  }

  // API import Excel
  @Override
  public ResultInSideDto importData(MultipartFile multipartFile) {
    List<OdTypeExportDTO> lstoExportDTOS = new ArrayList<>();
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
    try {
      if (multipartFile == null || multipartFile.isEmpty()) {
        resultInSideDTO.setKey(Constants.RESULT.FILE_IS_NULL);
        return resultInSideDTO;
      } else {
        String filePath = FileUtils
            .saveTempFile(multipartFile.getOriginalFilename(), multipartFile.getBytes(),
                tempFolder);
        if (!Constants.RESULT.SUCCESS.equals(resultInSideDTO.getKey())) {
          return resultInSideDTO;
        }

        File fileImport = new File(filePath);

        List<Object[]> lstHeader;
        lstHeader = CommonImport.getDataFromExcelFile(fileImport,
            0,//sheet
            3,//begin row
            0,//from column
            7,//to column
            1000);

        if (lstHeader.size() == 0 || !validFileFormat(lstHeader)) {
          resultInSideDTO.setKey(Constants.RESULT.FILE_INVALID_FORMAT);
          return resultInSideDTO;
        }

        // Lay du lieu import
        List<Object[]> lstData = CommonImport.getDataFromExcelFile(
            fileImport,
            0,//sheet
            5,//begin row
            0,//from column
            7,//to column
            1000
        );

        if (lstData.size() > 1500) {
          resultInSideDTO.setKey(Constants.RESULT.DATA_OVER);
          return resultInSideDTO;
        }

        listOdTypeDTOS = new ArrayList<>();

        if (!lstData.isEmpty()) {
          int row = 4;
          int index = 0;
          setMapOdGroupTypeName();
          setMapPriorityName();
          for (Object[] obj : lstData) {
            OdTypeExportDTO dto = new OdTypeExportDTO();
            OdTypeDTO odTypeDTO = new OdTypeDTO();
            if (obj[1] != null) {
              dto.setOdTypeCode(obj[1].toString().trim());
              odTypeDTO.setOdTypeCode(dto.getOdTypeCode());
            } else {
              dto.setOdTypeCode(null);
            }
            if (obj[2] != null) {
              dto.setOdTypeName(obj[2].toString().trim());
              odTypeDTO.setOdTypeName(dto.getOdTypeName());
            } else {
              dto.setStatusName(null);
            }
            if (obj[3] != null) {
              dto.setOdGroupTypeName(obj[3].toString().trim());
              for (Map.Entry<String, String> item : mapOdGroupTypeName.entrySet()) {
                if (dto.getOdGroupTypeName().equals(item.getValue())) {
                  odTypeDTO.setOdGroupTypeId(Long.valueOf(item.getKey()));
                  break;
                }
              }
            } else {
              dto.setOdGroupTypeName(null);
            }
            if (obj[4] != null) {
              dto.setStatusName(obj[4].toString().trim());
              if (I18n.getLanguage("odType.statusName.1").equals(dto.getStatusName())) {
                odTypeDTO.setStatus(Long.valueOf(1));
              } else {
                odTypeDTO.setStatus(Long.valueOf(0));
              }
            } else {
              dto.setStatusName(null);
            }
            if (obj[5] != null) {
              dto.setPriorityName(obj[5].toString().trim());
              for (Map.Entry<String, String> item : mapPriorityName.entrySet()) {
                if (dto.getPriorityName().equals(item.getValue())) {
                  odTypeDTO.setPriorityId(Long.valueOf(item.getKey()));
                  break;
                }
              }
            } else {
              dto.setPriorityName(null);
            }
            if (obj[6] != null) {
              dto.setProcessTime(obj[6].toString().trim());
              odTypeDTO.setProcessTime(Double.valueOf(dto.getProcessTime()));
            } else {
              dto.setProcessTime(null);
            }
            if (obj[7] != null) {
              dto.setAction(obj[7].toString().trim());
            }
            OdTypeExportDTO exportDTOTmp = validateImportInfo(dto, lstoExportDTOS);
            if (exportDTOTmp.getOdTypeId() != null) {
              odTypeDTO.setOdTypeId(exportDTOTmp.getOdTypeId());
            }
            if ("1".equals(exportDTOTmp.getValidate())) {
              odTypeDTO.setCheckValidate("1");
            }
            if (exportDTOTmp.getResultImport() == null) {
              exportDTOTmp
                  .setResultImport(I18n.getLanguage("odType.result.import.odTypeCode"));
              lstoExportDTOS.add(exportDTOTmp);
              listOdTypeDTOS.add(odTypeDTO);
            } else {
              lstoExportDTOS.add(exportDTOTmp);
              index++;
            }
            row++;
          }
          if (index == 0) {
            if (!listOdTypeDTOS.isEmpty()) {
              resultInSideDTO = odTypeRepository.insertOrUpdateListImport(listOdTypeDTOS);
            }
          } else {
            File fileExport = exportFileEx(lstoExportDTOS, Constants.RESULT_IMPORT);
            resultInSideDTO.setKey(Constants.RESULT.ERROR);
            resultInSideDTO.setFile(fileExport);
          }
        } else {
          resultInSideDTO.setKey(Constants.RESULT.NODATA);
          resultInSideDTO.setMessage(Constants.FILE_NULL);
          File fileExport = exportFileEx(lstoExportDTOS, Constants.RESULT_IMPORT);
          resultInSideDTO.setFile(fileExport);
          return resultInSideDTO;
        }
      }
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      resultInSideDTO.setKey(Constants.RESULT.ERROR);
      resultInSideDTO.setMessage(ex.getMessage());
    }
    return resultInSideDTO;
  }

  private boolean fomatProcessTime(String processTime) {
    int i = processTime.lastIndexOf('.');
    if (i == -1 || processTime.substring(i + 1).length() == 1) {
      return true;
    }
    return false;
  }

  private boolean validFileFormat(List<Object[]> lstHeader) {
    Object[] obj0 = lstHeader.get(0);
    int count = 0;
    for (Object data : obj0) {
      if (data != null) {
        count += 1;
      }
    }
    if (count != 8) {
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
    if (!(I18n.getLanguage("odType.odTypeCode") + "(*)")
        .equalsIgnoreCase(obj0[1].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("odType.odTypeName") + "(*)")
        .equalsIgnoreCase(obj0[2].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("odType.odGroupTypeName")).equalsIgnoreCase(obj0[3].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("odType.statusName") + "(*)")
        .equalsIgnoreCase(obj0[4].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("odType.priorityName") + "(*)")
        .equalsIgnoreCase(obj0[5].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("odType.processTime") + "(*)")
        .equalsIgnoreCase(obj0[6].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("odType.action") + "(*)").equalsIgnoreCase(obj0[7].toString().trim())) {
      return false;
    }

    return true;
  }

  private OdTypeExportDTO validateImportInfo(OdTypeExportDTO dto,
      List<OdTypeExportDTO> lstoExportDTOS) {
    if (StringUtils.isStringNullOrEmpty(dto.getOdTypeCode())) {
      dto.setResultImport(I18n.getLanguage("odType.err.odTypeCode"));
      return dto;
    }

    if (StringUtils.isStringNullOrEmpty(dto.getOdTypeName())) {
      dto.setResultImport(I18n.getLanguage("odType.err.odTypeName"));
      return dto;
    }

    if (StringUtils.isStringNullOrEmpty(dto.getStatusName())) {
      dto.setResultImport(I18n.getLanguage("odType.err.statusName"));
      return dto;
    }

    if (StringUtils.isStringNullOrEmpty(dto.getPriorityName())) {
      dto.setResultImport(I18n.getLanguage("odType.err.priorityName"));
      return dto;
    }
    if (StringUtils.isStringNullOrEmpty(dto.getProcessTime())) {
      dto.setResultImport(I18n.getLanguage("odType.err.processTime"));
      return dto;
    }
    if (!fomatProcessTime(dto.getProcessTime())) {
      dto.setResultImport(I18n.getLanguage("odType.format.err.processTime"));
      return dto;
    }

    if (StringUtils.isNotNullOrEmpty(dto.getOdTypeCode())) {
      OdTypeDTO odTypeDTOTmp = odTypeRepository.checkOdTypeExist(dto.getOdTypeCode());
      if (odTypeDTOTmp != null && I18n.getLanguage("odType.action.1").equals(dto.getAction())) {
        dto.setResultImport(I18n.getLanguage("odType.err.odTypeCode.dup-code"));
        return dto;
      }

      if (odTypeDTOTmp != null && (I18n.getLanguage("odType.action.0")).equals(dto.getAction())) {
        dto.setOdTypeId(odTypeDTOTmp.getOdTypeId());
      }
    }
    dto = validateDuplicate(dto, lstoExportDTOS);
    return dto;
  }

  private OdTypeExportDTO validateDuplicate(OdTypeExportDTO dto,
      List<OdTypeExportDTO> lstoExportDTOS) {
    String codeTemp = dto.getOdTypeCode();
    if (lstoExportDTOS.size() == 0) {
      if (I18n.getLanguage("odType.action.0").equals(dto.getAction())
          && dto.getOdTypeId() == null) {
        dto.setResultImport(I18n.getLanguage("odType.err.odTypeCode.not-code"));
      }
    }
    for (int i = 0; i < lstoExportDTOS.size(); i++) {
      OdTypeExportDTO dtoCheck = lstoExportDTOS.get(i);
      if (I18n.getLanguage("odType.action.1").equals(dto.getAction()) && codeTemp
          .equals(dtoCheck.getOdTypeCode())) {
        dto.setResultImport(I18n.getLanguage("odType.err.dup-code-in-file")
            .replaceAll("0", String.valueOf((i) + 1)));
        break;
      }
      if (I18n.getLanguage("odType.action.0").equals(dto.getAction())) {
        if (codeTemp.equals(dtoCheck.getOdTypeCode())) {
          dto.setValidate("1");
          break;
        }
        if (!codeTemp.equals(dtoCheck.getOdTypeCode()) && dto.getOdTypeId() == null) {
          dto.setResultImport(I18n.getLanguage("odType.err.odTypeCode.not-code"));
          break;
        }
      }
    }
    return dto;
  }


  @Override
  public File getTemplate() throws Exception {
    ExcelWriterUtils ewu = new ExcelWriterUtils();
    String templatePathOut = "templates" + File.separator
        + "TEMPLATE_EXPORT.xlsx";
    templatePathOut = StringUtils.removeSeparator(templatePathOut);
    Resource resource = new ClassPathResource(templatePathOut);
    InputStream fileTemplate = resource.getInputStream();
//      FileInputStream inputStream = new FileInputStream(templatePathOut);
    XSSFWorkbook workBook = new XSSFWorkbook(fileTemplate);
    XSSFSheet sheetOne = workBook.getSheetAt(0);
//      XSSFSheet sheetParam = workBook.getSheetAt(1);
    XSSFSheet sheetParam = workBook.createSheet("param");
    String[] header = new String[]{I18n.getLanguage("common.STT"),
        I18n.getLanguage("odType.odTypeCode"),
        I18n.getLanguage("odType.odTypeName"),
        I18n.getLanguage("odType.odGroupTypeName"),
        I18n.getLanguage("odType.statusName"),
        I18n.getLanguage("odType.priorityName"),
        I18n.getLanguage("odType.processTime"),
        I18n.getLanguage("odType.action")

    };

    String[] headerStar = new String[]{I18n.getLanguage("odType.odTypeCode"),
        I18n.getLanguage("odType.odTypeName"),
        I18n.getLanguage("odType.statusName"),
        I18n.getLanguage("odType.priorityName"),
        I18n.getLanguage("odType.processTime"),
        I18n.getLanguage("odType.action")

    };

    XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(sheetOne);
    List<String> listHeader = Arrays.asList(header);
    List<String> listHeaderStar = Arrays.asList(headerStar);

    int odGroupTypeColumn = listHeader.indexOf(I18n.getLanguage("odType.odGroupTypeName"));
    int statusColumn = listHeader.indexOf(I18n.getLanguage("odType.statusName"));
    int priorityColumn = listHeader.indexOf(I18n.getLanguage("odType.priorityName"));
    int actionColumn = listHeader.indexOf(I18n.getLanguage("odType.action"));

    Map<String, CellStyle> styles = CommonExport.createStyles(workBook);

    //Tao tieu de
    sheetOne.addMergedRegion(new CellRangeAddress(0, 0, 0, listHeader.size() - 1));
    Row titleRow = sheetOne.createRow(0);
    titleRow.setHeightInPoints(45);
    Cell titleCell = titleRow.createCell(0);
    titleCell.setCellValue(I18n.getLanguage("odType.title"));
    titleCell.setCellStyle(styles.get("title"));

    //Tao note
    sheetOne.addMergedRegion(new CellRangeAddress(2, 2, 0, listHeader.size() - 1));
    Row noteRow = sheetOne.createRow(2);
    noteRow.setHeightInPoints(18);
    Cell noteCell = noteRow.createCell(0);
    noteCell.setCellValue(I18n.getLanguage("wocdgroup.importfile.template.excel.note"));
    noteCell.setCellStyle(styles.get("note"));

    XSSFFont fontStar = workBook.createFont();
    fontStar.setColor(IndexedColors.RED.getIndex());
    //Tao header
    Row headerRow = sheetOne.createRow(4);
    headerRow.setHeightInPoints(18);
    for (int i = 0; i < listHeader.size(); i++) {
      Cell headerCell = headerRow.createCell(i);
      XSSFRichTextString rt = new XSSFRichTextString(listHeader.get(i));
      for (String checkHeader : listHeaderStar) {
        if (checkHeader.equalsIgnoreCase(listHeader.get(i))) {
          rt.append("(*)", fontStar);
        }
      }
      headerCell.setCellValue(rt);
      headerCell.setCellStyle(styles.get("header"));
      sheetOne.setColumnWidth(i, 7000);
    }

    sheetOne.setColumnWidth(0, 3000);

    int row = 5;
    List<CatItemDTO> lstOdGroupType = catItemRepository
        .getDataItem(Constants.CATEGORY.OD_GROUP_TYPE);
    for (CatItemDTO dto : lstOdGroupType) {
      ewu.createCell(sheetParam, 3, row++, dto.getItemName(), styles.get("cell"));
    }

    sheetParam.autoSizeColumn(0);

    Name odTypeGroupName = workBook.createName();
    odTypeGroupName.setNameName("odGroupType");
    odTypeGroupName.setRefersToFormula("param!$D$2:$D$" + row);

    XSSFDataValidationConstraint odGroupTypeConstraint = new XSSFDataValidationConstraint(
        DataValidationConstraint.ValidationType.LIST, "odGroupType");
    CellRangeAddressList cellRangeOdGroupType = new CellRangeAddressList(5, 65000,
        odGroupTypeColumn, odGroupTypeColumn);
    XSSFDataValidation dataValidationOdGroupType = (XSSFDataValidation) dvHelper.createValidation(
        odGroupTypeConstraint, cellRangeOdGroupType);
    dataValidationOdGroupType.setShowErrorBox(true);
    sheetOne.addValidationData(dataValidationOdGroupType);

    row = 5;

    ewu.createCell(sheetParam, 4, row++, I18n.getLanguage("odType.status.0"), styles.get("cell"));
    ewu.createCell(sheetParam, 4, row++, I18n.getLanguage("odType.status.1"), styles.get("cell"));

    sheetParam.autoSizeColumn(1);

    Name status = workBook.createName();
    status.setNameName("status");
    status.setRefersToFormula("param!$E$2:$E$" + row);

    XSSFDataValidationConstraint statusConstraint = new XSSFDataValidationConstraint(
        DataValidationConstraint.ValidationType.LIST, "status");
    CellRangeAddressList statusCreate = new CellRangeAddressList(5, 65000, statusColumn,
        statusColumn);
    XSSFDataValidation dataValidationStatus = (XSSFDataValidation) dvHelper.createValidation(
        statusConstraint, statusCreate);
    dataValidationStatus.setShowErrorBox(true);
    sheetOne.addValidationData(dataValidationStatus);

    row = 5;

    List<CatItemDTO> lstPriority = catItemRepository.getDataItem(Constants.CATEGORY.OD_PRIORITY);
    for (CatItemDTO dto : lstPriority) {
      ewu.createCell(sheetParam, 5, row++, dto.getItemName(), styles.get("cell"));
    }

    sheetParam.autoSizeColumn(0);

    Name priority = workBook.createName();
    priority.setNameName("priority");
    priority.setRefersToFormula("param!$F$2:$F$" + row);

    XSSFDataValidationConstraint priorityConstraint = new XSSFDataValidationConstraint(
        DataValidationConstraint.ValidationType.LIST, "priority");
    CellRangeAddressList cellRangePriority = new CellRangeAddressList(5, 65000, priorityColumn,
        priorityColumn);
    XSSFDataValidation dataValidationPriority = (XSSFDataValidation) dvHelper.createValidation(
        priorityConstraint, cellRangePriority);
    dataValidationPriority.setShowErrorBox(true);
    sheetOne.addValidationData(dataValidationPriority);

    row = 5;

    ewu.createCell(sheetParam, 7, row++, I18n.getLanguage("odType.action.insert"),
        styles.get("cell"));
    ewu.createCell(sheetParam, 7, row++, I18n.getLanguage("odType.action.update"),
        styles.get("cell"));

    sheetParam.autoSizeColumn(1);

    Name action = workBook.createName();
    action.setNameName("action");
    action.setRefersToFormula("param!$H$2:$H$" + row);

    XSSFDataValidationConstraint actionConstraint = new XSSFDataValidationConstraint(
        DataValidationConstraint.ValidationType.LIST, "action");
    CellRangeAddressList actionCreate = new CellRangeAddressList(5, 65000, actionColumn,
        actionColumn);
    XSSFDataValidation dataValidationAction = (XSSFDataValidation) dvHelper.createValidation(
        actionConstraint, actionCreate);
    dataValidationAction.setShowErrorBox(true);
    sheetOne.addValidationData(dataValidationAction);

    workBook.setSheetName(0, I18n.getLanguage("odType.title"));
    workBook.setSheetHidden(1, true);
    sheetParam.setSelected(false);

    String fileResult = tempFolder + File.separator;
    String fileName = "IMPORT_OD_TYPE" + "_" + System.currentTimeMillis() + ".xlsx";
    ewu.saveToFileExcel(workBook, fileResult, fileName);
    String resultPath = fileResult + fileName;
    File fileExport = new File(resultPath);
    return fileExport;
      /*Resource res = new FileResource(fileExport);
      Page.getCurrent().open(res, null, false);*/

  }

  private File exportFileEx(List<OdTypeExportDTO> lstImport, String key) throws Exception {
    String fileNameOut;
    String subTitle;
    String sheetName = I18n.getLanguage("odType.export.title");
    String title = I18n.getLanguage("odType.export.title");
    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    List<ConfigFileExport> fileExports = new ArrayList<>();
    ConfigHeaderExport columnSheet1;
    List<ConfigHeaderExport> lstHeaderSheet1 = new ArrayList<>();
    columnSheet1 = new ConfigHeaderExport("odTypeCode", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    lstHeaderSheet1.add(columnSheet1);
    columnSheet1 = new ConfigHeaderExport("odTypeName", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    lstHeaderSheet1.add(columnSheet1);
    columnSheet1 = new ConfigHeaderExport("odGroupTypeName", "LEFT", false, 0, 0, new String[]{},
        null, "STRING");
    lstHeaderSheet1.add(columnSheet1);
    columnSheet1 = new ConfigHeaderExport("statusName", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    lstHeaderSheet1.add(columnSheet1);
    columnSheet1 = new ConfigHeaderExport("priorityName", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    lstHeaderSheet1.add(columnSheet1);
    columnSheet1 = new ConfigHeaderExport("processTime", "LEFT", false, 0, 0, new String[]{},
        null,
        "STRING");
    if (Constants.RESULT_IMPORT.equals(key)) {
      fileNameOut = OD_TYPE_RESULT_IMPORT;
      subTitle = I18n.getLanguage("odType.export.import", dateFormat.format(new Date()));
      lstHeaderSheet1.add(columnSheet1);
      columnSheet1 = new ConfigHeaderExport("action", "LEFT", false, 0, 0, new String[]{}, null,
          "STRING");
      lstHeaderSheet1.add(columnSheet1);
      columnSheet1 = new ConfigHeaderExport("resultImport", "LEFT", false, 0, 0, new String[]{},
          null,
          "STRING");
    } else {
      fileNameOut = OD_TYPE_EXPORT;
      subTitle = I18n.getLanguage("odType.export.eportDate", dateFormat.format(new Date()));
    }
    lstHeaderSheet1.add(columnSheet1);
    Map<String, String> fieldSplit = new HashMap<>();
    ConfigFileExport configfileExport = new ConfigFileExport(
        lstImport
        , sheetName
        , title
        , subTitle
        , 7
        , 3
        , 9
        , true
        , "language.odType"
        , lstHeaderSheet1
        , fieldSplit
        , ""
        , I18n.getLanguage("common.export.firstLeftHeader")
        , I18n.getLanguage("common.export.secondLeftHeader")
        , I18n.getLanguage("common.export.firstRightHeader")
        , I18n.getLanguage("common.export.secondRightHeader")
    );
    configfileExport.setLangKey(I18n.getLocale());
    List<CellConfigExport> lstCellSheet = new ArrayList<>();
    CellConfigExport cellSheet;

    cellSheet = new CellConfigExport(7, 0, 0, 0, I18n.getLanguage("common.STT"),
        "HEAD", "STRING");
    lstCellSheet.add(cellSheet);
    configfileExport.setLstCreatCell(lstCellSheet);
    fileExports.add(configfileExport);

//    String path = this.getClass().getClassLoader().getResource("").getPath();
//    String fullPath = URLDecoder.decode(path, "UTF-8");
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

  //2020-10-03 hungtv add
  private List<String> getAllKeysDTO () {
    try {
      List<String> keys = new ArrayList<>();
      Field[] fields = OdTypeDTO.class.getDeclaredFields();
      for (Field key : fields) {
        key.setAccessible(true);
        keys.add(key.getName());
      }
      if (keys != null && !keys.isEmpty()) {
        List<String> rmKeys = Arrays.asList("odTypeDetailDTOS", "odTypeMapLocationDTOS");
        for (String key : rmKeys) {
          keys.remove(key);
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
