package com.viettel.gnoc.wo.business;

import com.google.gson.Gson;
import com.viettel.cc.webserivce.CompCause;
import com.viettel.gnoc.commons.business.CompCauseBusiness;
import com.viettel.gnoc.commons.business.UserBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.CompCauseDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.commons.dto.MessagesDTO;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.dto.UsersDTO;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.commons.model.Users;
import com.viettel.gnoc.commons.model.UsersEntity;
import com.viettel.gnoc.commons.proxy.WoCategoryServiceProxy;
import com.viettel.gnoc.commons.repository.CatItemRepository;
import com.viettel.gnoc.commons.repository.CommonRepository;
import com.viettel.gnoc.commons.repository.GnocFileRepository;
import com.viettel.gnoc.commons.repository.MessagesRepository;
import com.viettel.gnoc.commons.repository.UnitRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.GNOC_FILE_BUSSINESS;
import com.viettel.gnoc.commons.utils.Constants.INSERT_SOURCE;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.Constants.WS_RESULT;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.DateUtil;
import com.viettel.gnoc.commons.utils.ExcelWriterUtils;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.od.dto.ObjKeyValueVsmartDTO;
import com.viettel.gnoc.wo.dto.CdInfoForm;
import com.viettel.gnoc.wo.dto.CfgSupportCaseDTO;
import com.viettel.gnoc.wo.dto.CfgSupportCaseTestDTO;
import com.viettel.gnoc.wo.dto.CfgWoTickHelpDTO;
import com.viettel.gnoc.wo.dto.CfgWoTickHelpInsideDTO;
import com.viettel.gnoc.wo.dto.CountWoForVSmartForm;
import com.viettel.gnoc.wo.dto.KpiCompleteVsmartResult;
import com.viettel.gnoc.wo.dto.ObjFile;
import com.viettel.gnoc.wo.dto.ObjKeyValue;
import com.viettel.gnoc.wo.dto.SupportCaseForm;
import com.viettel.gnoc.wo.dto.SupportCaseTestForm;
import com.viettel.gnoc.wo.dto.VsmartUpdateForm;
import com.viettel.gnoc.wo.dto.WoCdGroupDTO;
import com.viettel.gnoc.wo.dto.WoCdGroupInsideDTO;
import com.viettel.gnoc.wo.dto.WoChecklistDTO;
import com.viettel.gnoc.wo.dto.WoDTO;
import com.viettel.gnoc.wo.dto.WoDTOSearch;
import com.viettel.gnoc.wo.dto.WoDetailDTO;
import com.viettel.gnoc.wo.dto.WoHisForAccountDTO;
import com.viettel.gnoc.wo.dto.WoHistoryInsideDTO;
import com.viettel.gnoc.wo.dto.WoInsideDTO;
import com.viettel.gnoc.wo.dto.WoMaterialDeducteDTO;
import com.viettel.gnoc.wo.dto.WoMaterialDeducteInsideDTO;
import com.viettel.gnoc.wo.dto.WoMerchandiseDTO;
import com.viettel.gnoc.wo.dto.WoMerchandiseInsideDTO;
import com.viettel.gnoc.wo.dto.WoPostInspectionDTO;
import com.viettel.gnoc.wo.dto.WoPostInspectionInsideDTO;
import com.viettel.gnoc.wo.dto.WoSalaryResponse;
import com.viettel.gnoc.wo.dto.WoTroubleInfoDTO;
import com.viettel.gnoc.wo.dto.WoTypeCfgRequiredDTO;
import com.viettel.gnoc.wo.dto.WoTypeInsideDTO;
import com.viettel.gnoc.wo.dto.WoTypeServiceDTO;
import com.viettel.gnoc.wo.dto.WoTypeServiceInsideDTO;
import com.viettel.gnoc.wo.dto.WoWorklogDTO;
import com.viettel.gnoc.wo.dto.WoWorklogInsideDTO;
import com.viettel.gnoc.wo.dto.WorkTimeDTO;
import com.viettel.gnoc.wo.model.WoDeclareServiceEntity;
import com.viettel.gnoc.wo.model.WoHistoryEntity;
import com.viettel.gnoc.wo.repository.CfgWoTickHelpRepository;
import com.viettel.gnoc.wo.repository.MaterialThresRepository;
import com.viettel.gnoc.wo.repository.WoChecklistDetailRepository;
import com.viettel.gnoc.wo.repository.WoDeclareServiceRepository;
import com.viettel.gnoc.wo.repository.WoDetailRepository;
import com.viettel.gnoc.wo.repository.WoHistoryRepository;
import com.viettel.gnoc.wo.repository.WoMerchandiseRepository;
import com.viettel.gnoc.wo.repository.WoPostInspectionRepository;
import com.viettel.gnoc.wo.repository.WoRepository;
import com.viettel.gnoc.wo.repository.WoTroubleInfoRepository;
import com.viettel.gnoc.wo.repository.WoWorklogRepository;
import com.viettel.gnoc.wo.utils.NocProPort;
import com.viettel.gnoc.wo.utils.SPM_ANALYS_Port;
import com.viettel.gnoc.wo.utils.WSNIMS_CDPort;
import com.viettel.nocproV4.JsonResponseBO;
import com.viettel.nocproV4.RequestInputBO;
import com.viettel.security.PassTranformer;
import com.viettel.spm.analys.webservice.ParamBO;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

@Service
@Transactional
@Slf4j
public class WoVSmartBusinessImpl implements WoVSmartBusiness {

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

  @Value("${application.temp.folder:null}")
  private String tempFolder;

  @Value("${application.upload.folder:null}")
  private String uploadFolder;

  @Value("${application.extensionAllow:null}")
  private String extension;

  @Value("${application.smsGatewayId:null}")
  private String smsGatewayId;

  @Value("${application.senderId:null}")
  private String senderId;

  @Autowired
  WoRepository woRepository;

  @Autowired
  WoDetailRepository woDetailRepository;

  @Autowired
  WSNIMS_CDPort wsnims_cdPort;

  @Autowired
  WoHistoryRepository woHistoryRepository;

  @Autowired
  WoWorklogRepository woWorklogRepository;

  @Autowired
  WoTroubleInfoRepository woTroubleInfoRepository;

  @Autowired
  CommonRepository commonRepository;

  @Autowired
  WoPostInspectionRepository woPostInspectionRepository;

  @Autowired
  WoCategoryServiceProxy woCategoryServiceProxy;

  @Autowired
  CatItemRepository catItemRepository;

  @Autowired
  CfgWoTickHelpRepository cfgWoTickHelpRepository;

  @Autowired
  WoMerchandiseRepository woMerchandiseRepository;

  @Autowired
  WoBusiness woBusiness;

  @Autowired
  UserBusiness userBusiness;

  @Autowired
  UnitRepository unitRepository;

  @Autowired
  WoCdGroupBusiness woCdGroupBusiness;

  @Autowired
  protected WoPostInspectionBusiness woPostInspectionBusiness;

  @Autowired
  protected CompCauseBusiness compCauseBusiness;

  @Autowired
  MaterialThresRepository materialThresRepository;

  @Autowired
  MessagesRepository messagesRepository;

  @Autowired
  WoDeclareServiceRepository woDeclareServiceRepository;

  @Autowired
  WoChecklistDetailRepository woChecklistDetailRepository;

  @Autowired
  NocProPort nocPort;

  @Autowired
  SPM_ANALYS_Port port;

  @Autowired
  GnocFileRepository gnocFileRepository;

  @Autowired
  WoTimeBusiness woTimeBusiness;

  @Autowired
  WoMaterialDeducteBusiness woMaterialDeducteBusiness;

  @Override
  public List<ObjKeyValue> getDataTestService(Long woId) {
    List<ObjKeyValue> lst = new ArrayList<>();
    try {
      WoInsideDTO wo = woRepository.findWoByIdNoOffset(woId);
      if (wo != null && StringUtils.isNotNullOrEmpty(wo.getFileName())) {
        String[] fileName = wo.getFileName().split(",");
        String pathDate = "";
        if (wo.getCreateDate() != null) {
          pathDate = FileUtils.createPathFtpByDate(wo.getCreateDate());
        }
        String filePathOld = ftpFolder + "/" + pathDate + "/" + fileName[fileName.length - 1].trim();
        byte[] bytes = FileUtils.getFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
            PassTranformer.decrypt(ftpPass), filePathOld);
        InputStream inputStreams = new ByteArrayInputStream(bytes);
        Map<Long, ObjKeyValue> mapHeader = getHeader(filePathOld, inputStreams);
        inputStreams = new ByteArrayInputStream(bytes);
        return getDataExcelTestService(mapHeader, filePathOld, inputStreams);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return lst;
  }

  @Override
  public ResultDTO checkInfraForComplete(WoDTO createWoDto) {
    ResultDTO resultDTO = new ResultDTO();
    try {
      WoDetailDTO detail = woDetailRepository.findWoDetailById(Long.valueOf(createWoDto.getWoId()));
      if (detail != null && detail.getInfraType() == null) {
        com.viettel.nims.webservice.SubscriptionInfoForm res = wsnims_cdPort
            .getSubscriptionInfo(detail.getAccountIsdn(), null);
        if (res != null) {
          if (res.getInfraType() != null && !"N/A".equals(res.getInfraType())) {
            // set ha tang
            if (Constants.INFRA_TYPE.GPON.equals(res.getInfraType().toUpperCase())) {
              detail.setInfraType(Long.valueOf(Constants.INFRA_TYPE_ID.GPON));
            } else if (Constants.INFRA_TYPE.FCN.equals(res.getInfraType().toUpperCase())) {
              detail.setInfraType(Long.valueOf(Constants.INFRA_TYPE_ID.FCN));
            } else if (Constants.INFRA_TYPE.CATV.equals(res.getInfraType().toUpperCase())) {
              detail.setInfraType(Long.valueOf(Constants.INFRA_TYPE_ID.CATV));
            } else if (Constants.INFRA_TYPE.CCN.equals(res.getInfraType().toUpperCase())) {
              detail.setInfraType(Long.valueOf(Constants.INFRA_TYPE_ID.CCN));
            }
            if (detail.getInfraType() == null) {
              resultDTO.setId(RESULT.FAIL);
              resultDTO.setKey(RESULT.FAIL);
              resultDTO.setMessage(RESULT.FAIL);
              return resultDTO;
            }
            woDetailRepository.insertUpdateWoDetail(detail);
            resultDTO.setId(String.valueOf(detail.getInfraType()));
            resultDTO.setKey(RESULT.SUCCESS);
            resultDTO.setMessage(res.getInfraType().toUpperCase());
            return resultDTO;
          } else {
            return checkListInfraForComplete(detail);
          }
        } else {
          return checkListInfraForComplete(detail);
        }
      } else {
        resultDTO.setId(RESULT.FAIL);
        resultDTO.setKey(RESULT.FAIL);
        resultDTO.setMessage(RESULT.FAIL);
        return resultDTO;
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      resultDTO.setId(RESULT.FAIL);
      resultDTO.setKey(RESULT.FAIL);
      resultDTO.setMessage(RESULT.FAIL);
      return resultDTO;
    }
  }

  // lay danh sach nhom khieu nai ko can check
  private ResultDTO checkListInfraForComplete(WoDetailDTO detail) {
    ResultDTO resultDTO = new ResultDTO();
    List<CatItemDTO> lsItem = catItemRepository
        .getListCatItemByItemCode(Constants.AP_PARAM.COMP_GROUP_DEPLOY_NEW);
    if (lsItem != null && lsItem.size() > 0) {
      CatItemDTO item = lsItem.get(0);
      if (StringUtils.isNotNullOrEmpty(item.getItemValue())) {
        String[] arrGroup = item.getItemValue().split(",");
        List<String> lstGr = Arrays.asList(arrGroup);
        if (lstGr != null && lstGr.contains(String.valueOf(detail.getCcGroupId()))) {
          resultDTO.setId("0");
          resultDTO.setKey(RESULT.SUCCESS);
          resultDTO.setMessage("ALL");
          return resultDTO;
        }
      }
    }
    resultDTO.setId(RESULT.FAIL);
    resultDTO.setKey(RESULT.FAIL);
    resultDTO.setMessage(RESULT.FAIL);
    return resultDTO;
  }

  public Map<Long, ObjKeyValue> getHeader(String fileName, InputStream inputStreams)
      throws Exception {
    Map<Long, ObjKeyValue> mapHeader = new HashMap<Long, ObjKeyValue>();
    if (fileName.endsWith(".xls") || fileName.endsWith(".XLS")) {
      HSSFSheet sheet = new HSSFWorkbook(inputStreams).getSheetAt(0);
      Row row = sheet.getRow(0);
      int indexHeader = 0;
      while (row.getCell(indexHeader) != null) {
        String cellValue = row.getCell(indexHeader).getStringCellValue();
        if (cellValue.contains("*")) {
          ObjKeyValue obj = new ObjKeyValue();
          obj.setKey(cellValue);
          obj.setCol(indexHeader);
          if (cellValue.contains("{") && cellValue.contains("}")) {
            String value = cellValue.substring(cellValue.indexOf("{") + 1, cellValue.indexOf("}"));
            obj.setDefaulValue(value);
          }
          mapHeader.put(Long.valueOf(indexHeader), obj);
        }
        indexHeader++;
      }
    } else if (fileName.endsWith(".xlsx") || fileName.endsWith(".XLSX")) {
      XSSFSheet sheet = new XSSFWorkbook(inputStreams).getSheetAt(0);
      Row row = sheet.getRow(0);
      int indexHeader = 0;
      while (row.getCell(indexHeader) != null) {
        String cellValue = row.getCell(indexHeader).getStringCellValue();
        if (cellValue.contains("*")) {
          ObjKeyValue obj = new ObjKeyValue();
          obj.setKey(cellValue);
          obj.setCol(indexHeader);
          if (cellValue.contains("{") && cellValue.contains("}")) {
            String value = cellValue.substring(cellValue.indexOf("{") + 1, cellValue.indexOf("}"));
            obj.setDefaulValue(value);
          }
          mapHeader.put(Long.valueOf(indexHeader), obj);
        }
        indexHeader++;
      }
    }
    inputStreams.close();
    return mapHeader;
  }

  public List<ObjKeyValue> getDataExcelTestService(Map<Long, ObjKeyValue> map, String fileName,
      InputStream inputStreams) throws Exception {
    List<ObjKeyValue> lst = new ArrayList<>();
    HSSFWorkbook hssfWorkbook = null;
    XSSFWorkbook xssfWorkbook = null;
    try {
      if (fileName.endsWith(".xls") || fileName.endsWith(".XLS")) {
        hssfWorkbook = new HSSFWorkbook(inputStreams);
        HSSFSheet sheet = hssfWorkbook.getSheetAt(0);
        int startRow = 1;
        int endRow = sheet.getLastRowNum();
        while (startRow <= endRow) {
          Row row = sheet.getRow(startRow);
          if (ExcelWriterUtils.isRowEmpty(row)) {
            startRow++;
            continue;
          }
          for (Map.Entry<Long, ObjKeyValue> entry : map.entrySet()) {
            ObjKeyValue objTmp = entry.getValue();
            objTmp.setRow(startRow);
            int col = entry.getKey().intValue();
            String cellValue;
            if (row.getCell(col).getCellType() == CellType.NUMERIC) {
              cellValue = row.getCell(col).getNumericCellValue() + "";
            } else {
              cellValue = row.getCell(col).getStringCellValue();
            }
            objTmp.setValue(cellValue);

            lst.add(objTmp);
          }
          startRow++;
        }
      } else if (fileName.endsWith(".xlsx") || fileName.endsWith(".XLSX")) {
        xssfWorkbook = new XSSFWorkbook(inputStreams);
        XSSFSheet sheet = xssfWorkbook.getSheetAt(0);
        int startRow = 1;
        int endRow = sheet.getLastRowNum();
        while (startRow <= endRow) {
          Row row = sheet.getRow(startRow);
          if (ExcelWriterUtils.isRowEmpty(row)) {
            startRow++;
            continue;
          }
          for (Map.Entry<Long, ObjKeyValue> entry : map.entrySet()) {
            ObjKeyValue obj = entry.getValue();
            ObjKeyValue objTmp = obj.clone();
            if (objTmp != null) {
              objTmp.setRow(startRow);
              int col = entry.getKey().intValue();
              String cellValue = "";
              Cell cell = row.getCell(col);
              if (cell != null) {
                if (row.getCell(col).getCellType() == CellType.NUMERIC) {
                  cellValue = row.getCell(col).getNumericCellValue() + "";
                } else {
                  cellValue = row.getCell(col).getStringCellValue();
                }
              }
              objTmp.setValue(cellValue);
              lst.add(objTmp);
            }
          }
          startRow++;
        }
      }
//    inputStreams.close();
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    } finally {
      if (hssfWorkbook != null) {
        hssfWorkbook.close();
      }
      if (xssfWorkbook != null) {
        xssfWorkbook.close();
      }
      if (inputStreams != null) {
        inputStreams.close();
      }
    }
    return lst;
  }

  @Override
  public List<WoCdGroupInsideDTO> getListCdByLocation(String locationCode) {
    log.debug("Request to getListCdByLocation: {}", locationCode);
    return woRepository.getListCdByLocation(locationCode);
  }

  @Override
  public List<WoWorklogInsideDTO> getListWorklogByWoIdPaging(
      WoWorklogInsideDTO woWorklogInsideDTO) {
    log.debug("Request to getListWorklogByWoIdPaging: {}", woWorklogInsideDTO);
    Datatable datatable = woWorklogRepository.getListWorklogByWoIdPaging(woWorklogInsideDTO);
    if (datatable != null && datatable.getData() != null && !datatable.getData().isEmpty()) {
      return (List<WoWorklogInsideDTO>) datatable.getData();
    }
    return null;
  }

  @Override
  public List<WoMaterialDeducteInsideDTO> getListMaterial(
      WoMaterialDeducteInsideDTO woMaterialDeducteInsideDTO) {
    log.debug("Request to getListMaterial: {}", woMaterialDeducteInsideDTO);
    return woRepository.getListMaterial(woMaterialDeducteInsideDTO);
  }

  @Override
  public List<SupportCaseForm> getLstSupportCase(Long woId) {
    List<SupportCaseForm> lst = new ArrayList<>();
    try {
      WoDetailDTO detail = woDetailRepository.findWoDetailById(woId);
      if (detail != null && detail.getInfraType() != null && detail.getServiceId() != null) {
        CfgSupportCaseDTO dto = new CfgSupportCaseDTO();
        dto.setInfraTypeID(detail.getInfraType());
        dto.setServiceID(detail.getServiceId());
        dto.setPage(1);
        dto.setPageSize(Integer.MAX_VALUE);

        Datatable datatable = woCategoryServiceProxy.getListCfgSupportCaseDTONew(dto);
        if (datatable != null && !datatable.getData().isEmpty()) {
          List<CfgSupportCaseDTO> lstSC = new ArrayList<>();
          if (datatable != null && datatable.getData() != null) {
            List<LinkedHashMap> lstHasMap = (List<LinkedHashMap>) datatable.getData();
            if (lstHasMap != null) {
              lstHasMap.forEach(item -> {
                Object id = item.get("id");
                Object cfgSupportCaseID = item.get("cfgSupportCaseID");
                Object caseName = item.get("caseName");
                Object serviceID = item.get("serviceID");
                Object serviceName = item.get("serviceName");
                Object infraTypeID = item.get("infraTypeID");
                Object infraTypeName = item.get("infraTypeName");
                CfgSupportCaseDTO cfgSupportCaseDTO = new CfgSupportCaseDTO();
                cfgSupportCaseDTO.setId(id == null ? null : Long.valueOf(id.toString()));
                cfgSupportCaseDTO.setCfgSupportCaseID(
                    cfgSupportCaseID == null ? null : cfgSupportCaseID.toString());
                cfgSupportCaseDTO.setCaseName(caseName == null ? null : caseName.toString());
                cfgSupportCaseDTO
                    .setServiceID(serviceID == null ? null : Long.valueOf(serviceID.toString()));
                cfgSupportCaseDTO
                    .setServiceName(serviceName == null ? null : serviceName.toString());
                cfgSupportCaseDTO.setInfraTypeID(
                    infraTypeID == null ? null : Long.valueOf(infraTypeID.toString()));
                cfgSupportCaseDTO
                    .setInfraTypeName(infraTypeName == null ? null : infraTypeName.toString());
                lstSC.add(cfgSupportCaseDTO);
              });
            }
          }

          for (CfgSupportCaseDTO i : lstSC) {
            SupportCaseForm form = new SupportCaseForm();
            form.setCaseName(i.getCaseName());
            form.setCfgSupportCaseID(i.getCfgSupportCaseID());
            form.setInfraTypeID(i.getInfraTypeID() != null ? i.getInfraTypeID().toString() : null);
            form.setServiceID(i.getServiceID() != null ? i.getServiceID().toString() : null);

            List<SupportCaseTestForm> lstCaseTest = new ArrayList<>();
            List<CfgSupportCaseTestDTO> lstSCT = woCategoryServiceProxy.getListCfgSupportCaseTestId(
                !DataUtil.isNumber(i.getCfgSupportCaseID()) ? null
                    : Long.valueOf(i.getCfgSupportCaseID()));
            if (lstSCT != null && !lstSCT.isEmpty()) {
              for (CfgSupportCaseTestDTO k : lstSCT) {
                SupportCaseTestForm f = new SupportCaseTestForm();
                f.setCfgSuppportCaseId(
                    k.getCfgSuppportCaseId() != null ? k.getCfgSuppportCaseId().toString() : null);
                f.setTestCaseName(k.getTestCaseName());
                f.setFileRequired(
                    k.getFileRequired() != null ? k.getFileRequired().toString() : null);
                f.setId(k.getId() != null ? k.getId().toString() : null);
                lstCaseTest.add(f);
              }
            }
            form.setLstSuportCaseTest(lstCaseTest);
            lst.add(form);
          }
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return lst;
  }

  @Override
  public List<WoPostInspectionInsideDTO> getListExistedWoPostInspection(
      WoPostInspectionInsideDTO woPostInspectionDTO) {
    log.debug("Request to getListExistedWoPostInspection: {}", woPostInspectionDTO);
    return woPostInspectionRepository.getListExistedWoPostInspection(woPostInspectionDTO);
  }

  @Override
  public List<WoPostInspectionInsideDTO> onSearch(WoPostInspectionDTO woPostInspectionDTO,
      int startRow, int pageLength) {
    log.debug("Request to onSearch: {}", woPostInspectionDTO);
    return woPostInspectionRepository
        .onSearch(woPostInspectionDTO.toInsideDto(), startRow, pageLength);
  }

  @Override
  public Integer onSearchCount(WoPostInspectionDTO woPostInspectionDTO) {
    log.debug("Request to onSearchCount: {}", woPostInspectionDTO);
    List<WoPostInspectionInsideDTO> lstData = onSearch(woPostInspectionDTO, 0, 0);
    int size = 0;
    if (lstData != null && !lstData.isEmpty()) {
      size = lstData.size();
    }
    return size;
  }

  @Override
  public List<CountWoForVSmartForm> getCountWoForVSmart(CountWoForVSmartForm countWoForVSmartForm) {
    log.debug("Request to getCountWoForVSmart: {}", countWoForVSmartForm);
    return woRepository.getCountWoForVSmart(countWoForVSmartForm);
  }

  @Override
  public VsmartUpdateForm getScriptId(Long woId) {
    try {
      VsmartUpdateForm result = new VsmartUpdateForm();
      WoInsideDTO wo = woRepository.findWoByIdNoOffset(woId);
      if (wo != null) {
        List<WoInsideDTO> lst = woRepository.getListWoBySystemOtherCode(wo.getWoSystemId(), null);
        lst.removeIf(c -> (c.getWoTypeId() == null));
        Double amount = null;
        if (lst != null && lst.size() > 0) {
          Long scriptId = null;
          Boolean checkClose = true;
          Map<String, String> mapConfigProperty = commonRepository.getConfigProperty();
          for (WoInsideDTO o : lst) {
            if (!WoBusinessImpl.checkProperty(mapConfigProperty, o.getWoTypeId().toString(),
                Constants.AP_PARAM.WO_TYPE_QLTS_NC_UCTT)
                && !WoBusinessImpl.checkProperty(mapConfigProperty, o.getWoTypeId().toString(),
                Constants.AP_PARAM.WO_TYPE_QLTS_NC_UCTT_THUE)) {
              List<WoTroubleInfoDTO> lstInfo = woTroubleInfoRepository
                  .getListWoTroubleInfoByWoId(o.getWoId());
              if (lstInfo != null && lstInfo.size() > 0) {
                WoTroubleInfoDTO info = lstInfo.get(0);
                if (info.getScriptId() != null) {
                  scriptId = info.getScriptId();
                  amount = info.getPolesDistance();
                  result.setScriptName(info.getScriptName());
                  result.setScriptId(info.getScriptId());
                  result.setPolesDistance(amount);
                  break;
                }
              }
              if (!o.getStatus().equals(8L) && !o.getStatus().equals(6L)
                  && !o.getStatus().equals(2L) && !o.getStatus().equals(7L)) { // chua hoan thanh
                checkClose = false;
              }
            }
          }
          if (scriptId != null) {
            result.setId(Constants.RESULT.SUCCESS);
            result.setKey(Constants.RESULT.SUCCESS);
            result.setPolesDistance(amount);
            return result;
          } else if (checkClose) {
            result.setId(Constants.RESULT.SUCCESS);
            result.setKey(Constants.RESULT.SUCCESS);
            return result;
          } else {
            result.setId(Constants.RESULT.ERROR);
            result.setKey(Constants.RESULT.ERROR);
            return result;
          }
        }
      }
      result.setId(Constants.RESULT.SUCCESS);
      result.setKey(Constants.RESULT.SUCCESS);
      return result;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

  @Override
  public ResultDTO checkUpdateSupportWO(Long woId) {
    ResultDTO resultDTO = new ResultDTO();
    resultDTO.setKey(RESULT.SUCCESS);
    resultDTO.setMessage(RESULT.SUCCESS);
    try {
      WoInsideDTO wo = woRepository.findWoByIdNoOffset(woId);
      String isUsedCheckUpdateSupportWO = commonRepository
          .getConfigPropertyValue(Constants.AP_PARAM.IS_USED_CHECK_UDATE_SUPORT_WO);
      if (("SPM".equals(wo.getWoSystem()) || INSERT_SOURCE.SPM_VTNET.equals(wo.getWoSystem()))
          && "1".equals(isUsedCheckUpdateSupportWO)) {
        if (wo.getAllowSupport() != null && wo.getAllowSupport().equals(2L)) {
          resultDTO.setMessage(Constants.RESULT.SUCCESS);
          resultDTO.setKey(Constants.RESULT.SUCCESS);
          return resultDTO;
        } else {
          //thuc hien goi SOC
          WoDetailDTO wd = woDetailRepository.findWoDetailById(woId);
          UsersEntity us = commonRepository.getUserByUserId(wo.getFtId());
          List<ParamBO> lstParam = new ArrayList<>();
          ParamBO p1 = new ParamBO();
          p1.setParamKey("actor");
          p1.setParamValue("5");

          ParamBO p2 = new ParamBO();
          p2.setParamKey("woCode");
          p2.setParamValue(wo.getWoCode());

          ParamBO p3 = new ParamBO();
          p3.setParamKey("account");
          p3.setParamValue(wd.getAccountIsdn());

          lstParam.add(p1);
          lstParam.add(p2);
          lstParam.add(p3);

          com.viettel.spm.analys.webservice.ResultDTO res = port.doRequestAnalys(lstParam);
          if (res != null && Constants.RESULT.SUCCESS.equals(res.getKey())) {
            wo.setAllowSupport(1L);
          } else {
            resultDTO.setMessage(res != null ? res.getMessage() : "have some error!");
            resultDTO.setKey(RESULT.FAIL);
            return resultDTO;
          }
          updateWoAndHistory(wo, us, "Check support to SPM:" + res.getMessage(),
              wo.getStatus().intValue(), new Date());
          resultDTO.setKey(RESULT.FAIL);
          resultDTO.setMessage(I18n.getLanguage("wo.checkingSpmFirts"));
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      resultDTO.setKey(RESULT.FAIL);
      resultDTO.setId(RESULT.FAIL);
      resultDTO.setMessage(I18n.getLanguage("wo.haveSomeErrUpdateStatus"));
    }
    return resultDTO;
  }

  public void updateWoAndHistory(WoInsideDTO wo, UsersEntity user, String comment, Integer status,
      Date now)
      throws Exception {
    WoHistoryEntity woHistory = new WoHistoryEntity();
    woHistory.setCdId(wo.getCdId());
    woHistory.setComments(comment);
    woHistory.setCreatePersonId(wo.getCreatePersonId());
    woHistory.setFileName(wo.getFileName());
    woHistory.setFtId(wo.getFtId());
    woHistory.setNewStatus(Long.valueOf(status));
    woHistory.setOldStatus(wo.getStatus());
    woHistory.setUpdateTime(now);
    woHistory.setUserId(user.getUserId());
    woHistory.setUserName(user.getUsername());
    woHistory.setWoCode(wo.getWoCode());
    woHistory.setWoContent(wo.getWoContent());
    woHistory.setWoId(wo.getWoId());
    wo.setStatus(Long.valueOf(status));
    wo.setLastUpdateTime(now);
    ResultInSideDto resultUpdateWo = woRepository.updateWo(wo);
    if (resultUpdateWo != null && !Constants.RESULT.SUCCESS.equals(resultUpdateWo.getKey())) {
      throw new Exception(I18n.getLanguage("ErrUpdateWo"));
    }
    try {
      woHistoryRepository.insertWoHistory(woHistory.toDTO());
    } catch (Exception he) {
      log.error(he.getMessage(), he);
      throw new Exception(I18n.getLanguage("ErrUpdateWoHis"));
    }
  }

  @Override
  public ResultDTO deleteListWoPostInspection(List<WoPostInspectionDTO> woPostInspectionListDTO) {
    log.debug("Request to deleteListWoPostInspection: {}", woPostInspectionListDTO);
    ResultDTO resultDTO = new ResultDTO();
    resultDTO.setKey(Constants.RESULT.SUCCESS);
    resultDTO.setMessage(Constants.RESULT.SUCCESS);
    try {
      if (woPostInspectionListDTO != null && !woPostInspectionListDTO.isEmpty()) {
        for (WoPostInspectionDTO item : woPostInspectionListDTO) {
          WoPostInspectionInsideDTO woPostInspectionInsideDTO = item.toInsideDto();
          if (woPostInspectionInsideDTO != null && woPostInspectionInsideDTO.getId() != null) {
            ResultInSideDto res = woPostInspectionRepository
                .delete(woPostInspectionInsideDTO.getId());
            resultDTO.setKey(res.getKey());
            resultDTO.setMessage(res.getMessage());
          } else {
            return new ResultDTO("", I18n.getLanguage("wo.validate.Id"), "");
          }
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      resultDTO.setKey(e.getMessage());
      resultDTO.setMessage(e.getMessage());
    }
    return resultDTO;
  }

  @Override
  public WoSalaryResponse countWOByFT(WoSalaryResponse woSalaryResponse) {
    log.debug("Request to countWOByFT: {}", woSalaryResponse);
    return woRepository.countWOByFT(woSalaryResponse);
  }

  @Override
  public List<CdInfoForm> getListCdInfo(CdInfoForm cdInfoForm) {
    log.debug("Request to getListCdInfo: {}", cdInfoForm);
    List<CdInfoForm> lst = new ArrayList<>();
    try {
      if (cdInfoForm.getLstWoCode() != null && cdInfoForm.getLstWoCode().size() > 0) {
        for (String woCode : cdInfoForm.getLstWoCode()) {

          WoInsideDTO wo = woRepository.getWoByWoCode(woCode);
          if (wo != null) {
            CdInfoForm i = creatCdInfoForm(wo);
            lst.add(i);
          }
        }
      } else {
        for (Long woId : cdInfoForm.getLstWoId()) {
          WoInsideDTO wo = woRepository.findWoByIdNoOffset(woId);
          if (wo != null) {
            CdInfoForm i = creatCdInfoForm(wo);
            lst.add(i);
          }
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return lst;
  }

  private CdInfoForm creatCdInfoForm(WoInsideDTO wo) {
    CdInfoForm i = new CdInfoForm();
    i.setWoCode(wo.getWoCode());
    i.setWoId(wo.getWoId().toString());
    if (wo != null && wo.getStatus().equals(Long.valueOf(Constants.WO_STATUS.CLOSED_CD))) {
      WoHistoryInsideDTO woHis = woRepository.getWoHisFinalClose(wo.getWoId());
      if (woHis != null && woHis.getUserName() != null) {
        UsersInsideDto u = commonRepository.getUserByUserName(woHis.getUserName());
        if (u != null) {
          i.setEmail(u.getEmail());
          i.setUserName(u.getUsername());
          i.setFullName(u.getFullname());
          i.setMobile(u.getMobile());
        }
      }
    }
    return i;
  }

  @Override
  public String getSequenseWo(String seq) {
    log.debug("Request to getSequenseWo: {}", seq);
    return woRepository.getSeqTableWo(seq);
  }

  @Override
  public List<String> getListSequenseWo(String seq, int size) {
    return woRepository.getListSequenseWo(seq, size);
  }

  @Override
  public ResultDTO updateWOPostInspectionFromVsmart(WoPostInspectionDTO woPostInspectionDTO) {
    boolean checkResult = true;
    Long point = 0L;
    ResultDTO res = new ResultDTO();
    res.setKey(Constants.RESULT.SUCCESS);
    res.setMessage(Constants.RESULT.SUCCESS);
    try {
      for (ObjKeyValue objKeyValue : woPostInspectionDTO.getLstObjKeyValue()) {
        if (DataUtil.isNumber(objKeyValue.getValue())) {
          point += Integer.parseInt(objKeyValue.getValue());
        } else if ("Sai".equals(objKeyValue.getValue())) {
          checkResult = false;
        }
      }
      String pointTargetType =
          Constants.AP_PARAM.WO_TYPE_NAME_HKSC.equals(woPostInspectionDTO.getWoTypeName())
              ? Constants.AP_PARAM.POINT_OK_HKSC : Constants.AP_PARAM.POINT_OK_HKTK;
      Map<String, String> map = commonRepository.getConfigProperty();
      Long pointTarget = Long.parseLong(map.get(pointTargetType));
      if (point < pointTarget) {
        checkResult = false;
      }
      String json = new Gson().toJson(woPostInspectionDTO.getLstObjKeyValue());
      woPostInspectionDTO.setDataJson(json);
      woPostInspectionDTO.setPoint(point.toString());
      woPostInspectionDTO.setResult(checkResult ? "OK" : "NOK");
      if (woPostInspectionDTO.getWoTypeName().equals(Constants.AP_PARAM.WO_TYPE_NAME_HKSC)) {
        String[] accountWoId = woPostInspectionDTO.getAccountWoId().split("_");
        woPostInspectionDTO.setAccountWoId(accountWoId[accountWoId.length - 1]);
      }
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      res.setId(RESULT.FAIL);
      res.setKey(RESULT.FAIL);
      res.setMessage(ex.getMessage());
      return res;
    }
    return updateWOPostInspection(woPostInspectionDTO);
  }

  @Override
  public ResultDTO updateWOPostInspection(WoPostInspectionDTO inspectionDTO) {
    //cap nhat hau kiem
    ResultDTO res = new ResultDTO();
    try {
      if (inspectionDTO != null) {
        if (StringUtils.isStringNullOrEmpty(inspectionDTO.getId())) {
          res.setMessage("Id is not null");
          res.setKey(RESULT.FAIL);
          return res;
        }
        if (StringUtils.isStringNullOrEmpty(inspectionDTO.getWoId())) {
          res.setMessage("WoId is not null");
          res.setKey(RESULT.FAIL);
          return res;
        }
        if (StringUtils.isStringNullOrEmpty(inspectionDTO.getAccount())) {
          res.setMessage("Account is not null");
          res.setKey(RESULT.FAIL);
          return res;
        }
        if (StringUtils.isStringNullOrEmpty(inspectionDTO.getNote())) {
          res.setMessage("Note is not null");
          res.setKey(RESULT.FAIL);
          return res;
        }
        if (StringUtils.isStringNullOrEmpty(inspectionDTO.getCreatedTime())) {
          res.setMessage("CreatedTime is not null");
          res.setKey(RESULT.FAIL);
          return res;
        }
        //cap nhat file dinh kem
        UsersInsideDto users = commonRepository.getUserByUserName(inspectionDTO.getUserName());
        updateFile(inspectionDTO, users);
        Map<String, String> map = commonRepository.getConfigProperty();
        WoPostInspectionInsideDTO wpidto = woPostInspectionRepository
            .findWoInspectionById(Long.valueOf(inspectionDTO.getId()));
        //tao wo khac phuc su co
        if ("NOK".equalsIgnoreCase(inspectionDTO.getResult())
            && !StringUtils.isStringNullOrEmpty(inspectionDTO.getReceiveUserName())
            && StringUtils.isStringNullOrEmpty(wpidto.getReceiveUserName())) {
          Date now = new Date();
          Calendar calendar = Calendar.getInstance();
          calendar.setTime(now);
          calendar.add(Calendar.DATE, 1);
          calendar.set(Calendar.HOUR_OF_DAY, 0);
          calendar.set(Calendar.MINUTE, 0);
          calendar.set(Calendar.SECOND, 1);
          Date startWO = calendar.getTime();
          calendar.add(Calendar.DATE, 6);
          calendar.set(Calendar.HOUR_OF_DAY, 23);
          calendar.set(Calendar.MINUTE, 59);
          calendar.set(Calendar.SECOND, 59);
          Date endWO = calendar.getTime();
          WoDTO woDTO = new WoDTO();
          try {
            String woTypeId = map.get(Constants.AP_PARAM.VTT_KHAC_PHUC_HAU_KIEM);
            woDTO.setWoTypeCode(woTypeId);

            String woPriorityId = map.get(Constants.AP_PARAM.PRIORITY_HAU_KIEM);
            woDTO.setPriorityId(woPriorityId);
          } catch (Exception e) {
            log.error(e.getMessage(), e);
          }
          woDTO.setWoContent(
              I18n.getLanguage("wo.post.account").replaceAll("####", inspectionDTO.getAccount()));

          String pointTargetType =
              Constants.AP_PARAM.WO_TYPE_NAME_HKSC.equals(inspectionDTO.getWoTypeName())
                  ? Constants.AP_PARAM.POINT_OK_HKSC : Constants.AP_PARAM.POINT_OK_HKTK;
          String woTypeName =
              Constants.AP_PARAM.WO_TYPE_NAME_HKSC.equals(inspectionDTO.getWoTypeName())
                  ? I18n.getLanguage("wo.woTypeNameHKSC") : I18n.getLanguage("wo.woTypeNameHKTK");
          woDTO.setWoDescription(woTypeName + I18n.getLanguage("wo.woInspectionDescription")
              + inspectionDTO.getPoint() + "/"
              + map.get(pointTargetType) + ". Note:" + inspectionDTO.getNote());
          woDTO.setWoSystem("HAU_KIEM_XLSC");

          woDTO.setCreateDate(DateTimeUtils.date2ddMMyyyyHHMMss(now));
          woDTO.setStartTime(DateTimeUtils.date2ddMMyyyyHHMMss(startWO));
          woDTO.setEndTime(DateTimeUtils.date2ddMMyyyyHHMMss(endWO));
          woDTO.setCreatePersonId(String.valueOf(users.getUserId()));
          woDTO.setFtName(inspectionDTO.getReceiveUserName());
          UsersInsideDto receiveUserName = commonRepository
              .getUserByUserName(inspectionDTO.getReceiveUserName());
          woDTO.setFtId(String.valueOf(receiveUserName.getUserId()));

          woDTO.setListFileName(inspectionDTO.getArrFileName());
          woDTO.setFileArr(inspectionDTO.getFileDocumentByteArray());
          ResultInSideDto resultInSideDto = createWoVsmart(woDTO);
          if (!Constants.RESULT.SUCCESS.equalsIgnoreCase(resultInSideDto.getKey())) {
            res.setMessage(resultInSideDto.getKey());
            res.setKey(resultInSideDto.getKey());
            throw new Exception("" + res.getMessage());
          } else {
            res.setId(resultInSideDto.getIdValue());
            res.setKey(res.getKey());
            res.setMessage(RESULT.SUCCESS);
          }
          inspectionDTO.setWoCodePin(resultInSideDto.getIdValue());
        }
        ResultInSideDto key = woPostInspectionRepository
            .updateWOPostInspection(inspectionDTO.toInsideDto());
        res.setKey(key.getKey());
      }
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      res.setId(RESULT.FAIL);
      res.setKey(RESULT.FAIL);
      res.setMessage(ex.getMessage());
    }
    return res;
  }

  private void updateFile(WoPostInspectionDTO inspectionDTO, UsersInsideDto usersInsideDto)
      throws Exception {
    if (inspectionDTO.getArrFileName() != null && inspectionDTO.getArrFileName().size() > 0
        && inspectionDTO.getFileDocumentByteArray() != null
        && inspectionDTO.getFileDocumentByteArray().size() > 0) {
      if (inspectionDTO.getArrFileName().size() != inspectionDTO.getFileDocumentByteArray()
          .size()) {
        throw new Exception(I18n.getLanguage("wo.numberFileNotMap"));
      }
      List<GnocFileDto> gnocFileDtos = new ArrayList<>();
      UnitDTO unitToken = unitRepository.findUnitById(usersInsideDto.getUnitId());
      List<String> fileNames = new ArrayList<>();
      for (int i = 0; i < inspectionDTO.getArrFileName().size(); i++) {
        if (extension != null) {
          String[] extendArr = extension.split(",");
          Boolean checkExt = false;
          for (String e : extendArr) {
            if (inspectionDTO.getArrFileName().get(i).toLowerCase().endsWith(e.toLowerCase())) {
              checkExt = true;
              break;
            }
          }
          if (!checkExt) {
            throw new Exception(I18n.getLanguage("wo.fileImportInvalidExten"));
          }
        }
        String fullPath = FileUtils.saveFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
            PassTranformer.decrypt(ftpPass), ftpFolder, inspectionDTO.getArrFileName().get(i),
            inspectionDTO.getFileDocumentByteArray().get(i),
            DateUtil.convertStringToTime(inspectionDTO.getCreatedTime(), Constants.ddMMyyyyHHmmss));
        fileNames.add(FileUtils.getFileName(fullPath));
        GnocFileDto gnocFileDto = new GnocFileDto();
        gnocFileDto.setPath(fullPath);
        gnocFileDto.setFileName(inspectionDTO.getArrFileName().get(i));
        gnocFileDto.setCreateUnitId(usersInsideDto.getUnitId());
        gnocFileDto.setCreateUnitName(unitToken.getUnitName());
        gnocFileDto.setCreateUserId(usersInsideDto.getUserId());
        gnocFileDto.setCreateUserName(usersInsideDto.getUsername());
        gnocFileDto.setCreateTime(
            DateUtil.convertStringToTime(inspectionDTO.getCreatedTime(), Constants.ddMMyyyyHHmmss));
        gnocFileDto.setMappingId(Long.valueOf(inspectionDTO.getId()));
        gnocFileDtos.add(gnocFileDto);
      }
      gnocFileRepository.saveListGnocFileNotDeleteAll(
          GNOC_FILE_BUSSINESS.WO_POST_INSPECTION, Long.valueOf(inspectionDTO.getId()),
          gnocFileDtos);
      inspectionDTO.setFilename((StringUtils.isStringNullOrEmpty(inspectionDTO.getFilename()) ? ""
          : (inspectionDTO.getFilename() + ",")) + String.join(",", fileNames));
    }
  }

  public ResultInSideDto createWoVsmart(WoDTO createWoDto) {
    ResultInSideDto result = new ResultInSideDto();
    try {
      String woTypeCode = createWoDto.getWoTypeCode();
      // lay Id loai cong viec dua vao ma cong viec
      WoTypeInsideDTO dto = new WoTypeInsideDTO();
      dto.setWoTypeCode(woTypeCode == null ? null : woTypeCode);
      List<WoTypeInsideDTO> lst = woCategoryServiceProxy.getListWoTypeByLocaleNotLike(dto);
      if (lst != null && lst.size() > 0) {
        createWoDto.setWoTypeCode(woTypeCode == null ? null : woTypeCode);
        createWoDto.setWoTypeId(String.valueOf(lst.get(0).getWoTypeId()));
        //bo sung file dinh kem va xac dinh CD dua vao ma don vi
        if (woTypeCode != null && Constants.AP_PARAM.WO_TYPE_KBDV_QLCTKT
            .equalsIgnoreCase(woTypeCode)) {
          //xac dinh CD
          if (!StringUtils.isStringNullOrEmpty(createWoDto.getUnitCode())) {
            List<CatItemDTO> lstItem = catItemRepository
                .getListItemByCategory(Constants.AP_PARAM.CATEGORY_OTHER_CODE,
                    createWoDto.getUnitCode());
            if (lstItem != null && lstItem.size() > 0) // tao file dinh kem
            {
              WoCdGroupInsideDTO woCdGroupInsideDTO = new WoCdGroupInsideDTO();
              woCdGroupInsideDTO.setWoGroupCode(lstItem.get(0).getItemValue());
              Datatable datatable = woCategoryServiceProxy.getListWoCdGroupDTO(woCdGroupInsideDTO);
              if (datatable == null || datatable.getData() == null || datatable.getData()
                  .isEmpty()) {
                result.setKey(Constants.RESULT.ERROR);
                result.setMessage(I18n.getLanguage("wo.notMapUnitCD"));
                return result;
              }
              List<WoCdGroupInsideDTO> lstCd = (List<WoCdGroupInsideDTO>) datatable.getData();
              if (lstCd != null && lstCd.size() > 0 && lstCd.get(0).getWoGroupId() != null) {
                createWoDto.setCdId(String.valueOf(lstCd.get(0).getWoGroupId()));
              } else {
                result.setKey(Constants.RESULT.ERROR);
                result.setMessage(I18n.getLanguage("wo.notMapUnitCD"));
                return result;
              }
            } else {
              result.setKey(Constants.RESULT.ERROR);
              result.setMessage(I18n.getLanguage("wo.notMapUnitCD"));
              return result;
            }
            // lay user tao
            List<CatItemDTO> lstUser = catItemRepository
                .getListItemByCategory(Constants.AP_PARAM.CATEGORY_OTHER_CODE,
                    Constants.AP_PARAM.USER_CREATE_KBDV_QLCTKT);
            if (lstUser != null && lstUser.size() > 0) // tao file dinh kem
            {
              createWoDto.setCreatePersonId(lstUser.get(0).getItemValue() == null ? null
                  : lstUser.get(0).getItemValue());
            } else {
              result.setKey(Constants.RESULT.ERROR);
              result.setMessage(I18n.getLanguage("wo.notCfgUserCreate"));
              return result;
            }
          } else {
            result.setKey(Constants.RESULT.ERROR);
            result.setMessage(I18n.getLanguage("wo.unitCode.isNotNull"));
            return result;
          }
          return woBusiness.insertWoCommon(createWoDto);  // tao giao CD
        }
        result = woBusiness.createWoCommon(createWoDto);
      } else {
        result.setKey(Constants.RESULT.ERROR);
        result.setMessage(I18n.getLanguage("wo.woTypeDoesNotExixts"));
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      result.setKey(Constants.RESULT.ERROR);
      result.setMessage(e.getMessage());
    }
    return result;
  }

  @Override
  public ResultDTO checkDeviceCodeOfWo(WoInsideDTO woInsideDTO) {
    ResultDTO res = new ResultDTO();
    res.setId(Constants.RESULT.FAIL);
    res.setKey(Constants.RESULT.FAIL);
    res.setMessage(Constants.RESULT.FAIL);
    try {
      if (StringUtils.isStringNullOrEmpty(woInsideDTO.getWoCode())) {
        res.setMessage(I18n.getLanguage("wo.woCodeIsNotNull"));
        return res;
      }
      if (StringUtils.isStringNullOrEmpty(woInsideDTO.getDeviceCode())) {
        res.setMessage(I18n.getLanguage("wo.deviceCodeIsNotNull"));
        return res;
      }
      WoInsideDTO wo = woRepository.getWoByWoCode(woInsideDTO.getWoCode());
      if (wo == null) {
        res.setMessage(I18n.getLanguage("wo.woIsNotExists"));
        return res;
      }
      if (!woInsideDTO.getDeviceCode().equalsIgnoreCase(wo.getDeviceCode())) {
        return res;
      }
      res.setId(Constants.RESULT.SUCCESS);
      res.setKey(Constants.RESULT.SUCCESS);
      res.setMessage(Constants.RESULT.SUCCESS);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      res.setMessage(e.getMessage());
    }
    return res;
  }

  @Override
  public Integer getCountWOByUsers(WoDTOSearch woDTOSearch) {
    log.debug("Request to getCountWOByUsers: {}", woDTOSearch);
    return woRepository.getCountWOByUsers(woDTOSearch);
  }

  @Override
  public ResultDTO updatePendingWo(WoInsideDTO woInsideDTO, Boolean callCC) {
    log.debug("Request to updatePendingWo: {}", woInsideDTO);
    ResultDTO resultDTO = new ResultDTO();
    try {
      ResultInSideDto res = woBusiness
          .updatePendingWoCommon(woInsideDTO.getWoCode(), woInsideDTO.getEndPendingTime(),
              woInsideDTO.getUser(), woInsideDTO.getComment(), woInsideDTO.getSystem(), callCC);
//      resultDTO = res.toResultDTO();
      resultDTO.setKey(res.getKey());
      resultDTO.setId(res.getIdValue());
      resultDTO.setMessage(res.getMessage());
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      resultDTO.setKey(RESULT.FAIL);
      resultDTO.setMessage(e.getMessage());
    }
    return resultDTO;
  }

  @Override
  public ResultDTO aprovePXK(Long woId, Long status, String reason, Long isDestroy) {
    log.debug("Request to aprovePXK: {}", woId + "_" + status + "_" + reason);
    ResultDTO resultDTO = woBusiness.aprovePXK(woId, status, reason, null);
    return resultDTO;
  }

  @Override
  public ResultDTO confirmNotCreateAlarm(RequestInputBO inputBO, Long woId) throws Exception {
    log.debug("Request to confirmNotCreateAlarm: {}", woId + "_" + inputBO);
    try {
      WoInsideDTO wo = woRepository.findWoByIdNoOffset(woId);
      if (wo == null) {
        throw new Exception(I18n.getLanguage("wo.woIsNotExists"));
      } else {
        JsonResponseBO resNoc = nocPort.onExecuteMapQuery(inputBO);
        if (resNoc.getStatus() != 0) {
          throw new Exception(resNoc.getDetailError());
        } else {
          wo.setConfirmNotCreateAlarm(1L);
          woRepository.updateWo(wo);
        }
      }
      ResultDTO resultDTO = new ResultDTO();
      resultDTO.setMessage(Constants.RESULT.SUCCESS);
      resultDTO.setKey(Constants.RESULT.SUCCESS);
      return resultDTO;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw new Exception(I18n.getLanguage("wo.errComunicateNOC") + ":" + e.getMessage());
    }
  }

  @Override
  public List<ObjKeyValueVsmartDTO> getDataCfgWoHelp(Long systemId, String typeId) {
    log.debug("Request to getDataCfgWoHelp: {}", systemId + "_" + typeId);
    return woCategoryServiceProxy.getDataHeader(systemId, typeId);
  }

  @Override
  public ResultDTO acceptWo(String username, Long woId, String comment, Boolean isFt) {
    try {
      ResultInSideDto res = woBusiness.acceptWoCommon(username, woId, comment, isFt ? "FT" : "CD");
      ResultDTO resultDTO = new ResultDTO();
      resultDTO.setKey(res.getKey());
      resultDTO.setId(res.getIdValue());
      resultDTO.setMessage(res.getMessage());
      return resultDTO;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw new RuntimeException(e.getMessage());
    }
  }

  @Override
  public WoDTO findWoById(Long woId) {
    return woBusiness.findWoById(woId);
  }

  @Override
  public String insertWOPostInspectionFromVsmart(List<WoPostInspectionDTO> lstInspectionDTO,
      List<ObjKeyValue> lstObjKeyValue) {
    return woPostInspectionBusiness
        .insertWOPostInspectionFromVsmart(lstInspectionDTO, lstObjKeyValue);
  }

  @Override
  public ResultDTO cancelReqBccs(String woCode, String content) {
    return woBusiness.cancelReqBccs(woCode, content);
  }

  @Override
  public ResultDTO updateMopInfo(String woCode, String result, String mopId, Long type) {
    return woBusiness.updateMopInfo(woCode, result, mopId, type);
  }

  @Override
  public List<WoPostInspectionDTO> getListWOPostInspection(WoPostInspectionDTO inspectionDTO,
      int rowStart,
      int maxRow, String sortType, String sortFieldList) {
    List<WoPostInspectionDTO> woPostInspectionDTO = new ArrayList<>();
    List<WoPostInspectionInsideDTO> list = woPostInspectionBusiness
        .getListWOPostInspection(inspectionDTO.toInsideDto(), rowStart, maxRow, sortType,
            sortFieldList);
    if (list != null && list.size() > 0) {
      for (WoPostInspectionInsideDTO dto : list) {
        woPostInspectionDTO.add(dto.toOutsideDto());
      }
    }
    return woPostInspectionDTO;
  }

  @Override
  public ResultDTO dispatchWo(String username, String ftName, String woId, String comment) {
    try {
      Long woIdTmp =
          (!DataUtil.isNullOrEmpty(woId) && DataUtil.isNumber(woId)) ? Long.valueOf(woId) : null;
      ResultDTO resultDTO = new ResultDTO();
      ResultInSideDto res = woBusiness.dispatchWo(username, ftName, woIdTmp, comment);
      resultDTO.setKey(RESULT.SUCCESS.equals(res.getKey()) ? WS_RESULT.OK : WS_RESULT.NOK);
      resultDTO.setQuantitySucc(res.getQuantitySucc());
      return resultDTO;
    } catch (Exception e) {
      log.info(e.getMessage(), e);
      return new ResultDTO("NOK", e.getMessage());
    }
  }

  @Override
  public List<CompCause> getListCompCause3Level(String ccResult) {
    List<CompCause> causeList = new ArrayList<>();
    try {
      List<CompCauseDTO> list = compCauseBusiness.getCompCause(Long.valueOf(ccResult));
      for (CompCauseDTO compCauseDTO : list) {
        CompCause compCause = new CompCause();
        compCause.setName(compCauseDTO.getName());
        compCause.setCompCauseId(StringUtils.isNotNullOrEmpty(compCauseDTO.getCompCauseId()) ? Long
            .valueOf(compCauseDTO.getCompCauseId()) : null);
        compCause.setParentId(StringUtils.isNotNullOrEmpty(compCauseDTO.getParentId()) ? Long
            .valueOf(compCauseDTO.getParentId()) : null);
        compCause.setLevelId(StringUtils.isNotNullOrEmpty(compCauseDTO.getLevelId()) ? Long
            .valueOf(compCauseDTO.getLevelId()) : null);
        causeList.add(compCause);
      }
    } catch (Exception e) {
      log.info(e.getMessage(), e);
    }

    return causeList;
  }

  @Override
  public List<ResultDTO> createListWo(List<WoDTO> createWoDto) {
    return woBusiness.createListWoVsmart(createWoDto);
  }

  @Override
  public ResultDTO createWo(WoDTO createWoDto) {
    return woBusiness.createWoVsmart(createWoDto);
  }

  @Override
  public ResultDTO approveWoVsmart(VsmartUpdateForm updateForm, String username, String woId,
      String comment, String action, String ftName) throws Exception {
    Long woIdTmp =
        (!DataUtil.isNullOrEmpty(woId) && DataUtil.isNumber(woId)) ? Long.valueOf(woId) : null;
    if (woIdTmp == null) {
      throw new Exception("null");
    }
    ResultInSideDto resultInSideDto = woBusiness
        .approveWoCommon(updateForm, username, woIdTmp, comment, action, ftName);
    return new ResultDTO(String.valueOf(resultInSideDto.getId()), resultInSideDto.getKey(),
        resultInSideDto.getMessage());

  }

  @Override
  public ResultDTO insertWoKTTS(WoDTO woDTO) {
    return woBusiness.insertWoKTTS(woDTO);
  }

  @Override
  public List<Users> getListUserByUnitCode(String unitCode, String allOfChildUnit) {
    return userBusiness.getListUserByUnitCode(unitCode, allOfChildUnit);
  }

  @Override
  public UsersDTO getUserInfo(String userName, String staffCode) {
    return userBusiness.getUserInfo(userName, staffCode);
  }

  @Override
  public Users getUserModelInfo(String userName, String staffCode) {
    return userBusiness.getUserModelInfo(userName, staffCode);
  }

  @Override
  public List<WoMerchandiseDTO> getListWoMerchandise(String woId) {
    List<WoMerchandiseDTO> dtoList = new ArrayList<>();
    if (!StringUtils.isStringNullOrEmpty(woId)) {
      List<WoMerchandiseInsideDTO> list = woMerchandiseRepository
          .getListWoMerchandise(Long.valueOf(woId));
      if (list != null && list.size() > 0) {
        for (WoMerchandiseInsideDTO dto : list) {
          dtoList.add(dto.toModelOutSide());
        }
      }
    }
    return dtoList;
  }

  @Override
  public List<WoWorklogDTO> getListWorklogByWoId(String woId) {
    List<WoWorklogDTO> woWorklogDTOList = new ArrayList<>();
    if (StringUtils.isNotNullOrEmpty(woId)) {
      List<WoWorklogInsideDTO> lists = woWorklogRepository.getListDataByWoId(Long.valueOf(woId));
      if (lists != null && lists.size() > 0) {
        for (WoWorklogInsideDTO dto : lists) {
          woWorklogDTOList.add(dto.toModelOutSide());
        }
      }
    }
    return woWorklogDTOList;
  }

  @Override
  public KpiCompleteVsmartResult getKpiComplete(String startTime, String endTime,
      List<String> lstUser) {
    return woBusiness.getKpiComplete(startTime, endTime, lstUser);
  }

  @Override
  public List<WoChecklistDTO> getListWoChecklistDetailByWoId(String woId) {
    return woBusiness.getListWoChecklistDetailByWoId(woId);
  }

  @Override
  public List<ObjFile> getFileFromWo(String woId, List<String> lstFileName) {
    return woBusiness
        .getFileFromWo(
            (StringUtils.isNotNullOrEmpty(woId) && StringUtils.isLong(woId)) ? Long.valueOf(woId)
                : null, lstFileName);
  }

  @Override
  public ResultDTO insertCfgWoTickHelp(CfgWoTickHelpDTO cfgWoTickHelpDTO) {
    ResultDTO resultDTO = new ResultDTO();
    resultDTO.setKey(RESULT.SUCCESS);
    try {
      if (cfgWoTickHelpDTO != null) {
        cfgWoTickHelpDTO.setId(null);
      } else {
        return new ResultDTO(RESULT.FAIL, RESULT.FAIL, RESULT.FAIL);
      }
      ResultInSideDto resultInSideDto = cfgWoTickHelpRepository.add(cfgWoTickHelpDTO.toInsideDto());
      resultDTO = resultInSideDto.toResultDTO();
      if (!RESULT.ERROR.equals(resultInSideDto.getKey())) {
        WoInsideDTO wo = woBusiness.findWoByIdNoOffset(Long.valueOf(cfgWoTickHelpDTO.getWoId()));
        if (wo != null) {
          if (StringUtils.isLongNullOrEmpty(wo.getNeedSupport())) {
            wo.setNeedSupport(1L);
            if (StringUtils.isLongNullOrEmpty(wo.getNumSupport())) {
              wo.setNumSupport(1L);
            } else {
              Long numSupport = wo.getNumSupport() + 1;
              wo.setNumSupport(numSupport);
            }
          }
          ResultInSideDto resultInSideDto1 = woBusiness.updateTableWo(wo);
          if (!RESULT.SUCCESS.equals(resultInSideDto1.getKey())) {
            resultDTO.setMessage(resultInSideDto1.getKey());
            resultDTO.setId(Constants.RESULT.FAIL);
            resultDTO.setKey(Constants.RESULT.FAIL);
          }
        }
      }
      if (RESULT.ERROR.equalsIgnoreCase(resultDTO.getKey())) {
        resultDTO.setKey(null);
        resultDTO.setMessage(Constants.RESULT.FAIL);
      }
      return resultDTO;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return new ResultDTO(RESULT.FAIL, RESULT.FAIL, e.getMessage());
    }
  }


  @Override
  public String insertWOPostInspection(List<WoPostInspectionDTO> lstInspectionDTO) {
    return woPostInspectionBusiness.insertWOPostInspection(lstInspectionDTO);
  }

  @Override
  public List<ObjKeyValue> loadWoPostInspectionChecklist(String woId, String accountName) {
    return woPostInspectionBusiness.loadWoPostInspectionChecklist(woId, accountName);
  }

  @Override
  public List<UsersDTO> getListFtByUser(String userId, String keyword, int rowStart,
      int maxRow) {
    List<UsersInsideDto> list = woCdGroupBusiness
        .getListFtByUser(userId, keyword, rowStart, maxRow);
    List<UsersDTO> dtoListOutSide = new ArrayList<>();
    if (list != null && list.size() > 0) {
      for (UsersInsideDto dto : list) {
        dtoListOutSide.add(dto.toOutSideDto());
      }
    }
    return dtoListOutSide;
  }

  @Override
  public ResultDTO rejectWo(String username, String woId, String comment, Boolean isFt) {
    try {
      ResultInSideDto resultInSideDto = woBusiness
          .rejectWo(username, !DataUtil.isNullOrEmpty(woId) ? Long.valueOf(woId) : null, comment,
              isFt ? "FT" : "CD");
      return new ResultDTO(String.valueOf(resultInSideDto.getId()), resultInSideDto.getKey(),
          resultInSideDto.getMessage());
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return new ResultDTO("NOK", e.getMessage());
    }
  }

  @Override
  public String deleteWoPost(Long id) {
    ResultInSideDto resultInSideDto = woPostInspectionBusiness.delete(id);
    return resultInSideDto.getKey();
  }

  @Override
  public ResultDTO updateStatus(VsmartUpdateForm updateForm, String username, String woId,
      String status, String comment, String ccResult, String qrCode,
      List<WoMaterialDeducteDTO> listMaterial,
      Long reasonIdLv1, Long reasonIdLv2, Long actionKTTS, List<WoMerchandiseDTO> lstMerchandise,
      String reasonKtts, String handoverUser,
      String sessionId, String ipPortParentNode,
      List<String> listFileName, List<byte[]> fileArr) {
    try {
      List<WoMaterialDeducteInsideDTO> listWoMaterialDeducte = new ArrayList<>();
      List<WoMerchandiseInsideDTO> listWoMerchandiseInside = new ArrayList<>();
      if (listMaterial != null && listMaterial.size() > 0) {
        for (WoMaterialDeducteDTO dto : listMaterial) {
          listWoMaterialDeducte.add(dto.toModelInSide());
        }
      }
      if (lstMerchandise != null && lstMerchandise.size() > 0) {
        for (WoMerchandiseDTO dto1 : lstMerchandise) {
          listWoMerchandiseInside.add(dto1.toModelInSide());
        }
      }
      ResultInSideDto resultInSideDto = woBusiness
          .updateStatusCommon(updateForm, username, woId, status, comment, ccResult,
              qrCode, listWoMaterialDeducte, reasonIdLv1, reasonIdLv2, actionKTTS,
              listWoMerchandiseInside,
              reasonKtts, handoverUser, sessionId, ipPortParentNode, listFileName, fileArr);
      return resultInSideDto.toResultDTO();
    } catch (Exception e) {
      TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
      if (e != null && e.getMessage() != null) {
        return new ResultDTO("NOK", e.getMessage());
      } else {
        return new ResultDTO("NOK", I18n.getLanguage("wo.haveSomeError"));
      }
    }
  }


  @Override
  public List<ResultDTO> getWOSummaryInfobyUser(String userId, int typeSearch, Long cdId,
      String createTimeFrom, String createTimeTo) {
//    return woBusiness
//        .getWOSummaryInfobyUserCommon(userId, typeSearch, cdId, createTimeFrom, createTimeTo);
    return woBusiness
        .getWOSummaryInfobyUser2(userId, typeSearch, cdId, createTimeFrom, createTimeTo);
  }


  @Override
  public List<WoDTO> getListWOAndAccount(String username, String fromDate, String toDate,
      String woCode, String cdId, String accountIsdn) {
    return woRepository.getListWOAndAccount(username, fromDate, toDate, woCode, cdId, accountIsdn);
  }

  @Override
  public ResultDTO insertWoWorklog(WoWorklogDTO woWorklogDTO) {
    woWorklogDTO.setWoWorklogId(null);
    return woBusiness.insertWoWorklog(woWorklogDTO);
  }

  @Override
  public ResultDTO checkRequiredStation(String woCode) {
    try {
      WoInsideDTO wo = woRepository.getWoByWoCode(woCode);
      WoTypeCfgRequiredDTO dto = new WoTypeCfgRequiredDTO();
      dto.setWoTypeId(wo.getWoTypeId());
      dto.setCfgCode(Constants.AP_PARAM.WO_TYPE_REQUIRED_STATION);
      List<WoTypeCfgRequiredDTO> lst = woCategoryServiceProxy
          .getListWoTypeCfgRequiredByWoTypeId(dto);
      if (lst != null && lst.size() > 0) {
        WoTypeCfgRequiredDTO o = lst.get(0);
        if (o.getValue() != null && o.getValue() == 1L) {
          if (StringUtils.isStringNullOrEmpty(wo.getStationCode())) {
            return new ResultDTO(Constants.RESULT.SUCCESS, Constants.RESULT.SUCCESS,
                Constants.RESULT.SUCCESS);
          } else {
            String stationCode = woRepository.getStationFollowNode(wo.getStationCode(), null);
            if (StringUtils.isStringNullOrEmpty(stationCode)) {
              return new ResultDTO(Constants.RESULT.SUCCESS, Constants.RESULT.SUCCESS,
                  Constants.RESULT.SUCCESS);
            }
          }
        }
      }
      return new ResultDTO(RESULT.FAIL, Constants.RESULT.FAIL, Constants.RESULT.FAIL);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return new ResultDTO(Constants.RESULT.FAIL, Constants.RESULT.FAIL, e.getMessage());
    }
  }

  @Override
  public ResultDTO pendingWoForVsmart(VsmartUpdateForm updateForm, String woCode,
      String endPendingTime, String user, String system, String reasonName, String reasonId,
      String customer, String phone) throws Exception {
    return woBusiness
        .pendingWoForVsmart(updateForm, woCode, endPendingTime, user, system, reasonName, reasonId,
            customer, phone);
  }

  /*
  @Override
  public List<MaterialThresDTO> getListMaterialDTOByAction(Long actionId, Long serviceId,
      Long infraType, boolean isEnable, String nationCode) {
    MaterialThresInsideDTO materialThresDTO = new MaterialThresInsideDTO();
    List<MaterialThresDTO> listReturn = new ArrayList<>();

    materialThresDTO.setActionId(actionId);
    materialThresDTO.setServiceId(serviceId);
    materialThresDTO.setInfraType(infraType);
    List<MaterialThresInsideDTO> listTmp = materialThresRepository
        .getListMaterialDTOByAction(materialThresDTO, isEnable, nationCode);
    if (listTmp != null && listTmp.size() > 0) {
      for (MaterialThresInsideDTO dto : listTmp) {
        listReturn.add(dto.toOutSide());
      }
    }
    return listReturn;
  }*/

  @Override
  public WoTypeServiceDTO getIsCheckQrCode(Long woId) {
    WoTypeServiceInsideDTO woTypeServiceInsideDTO = woBusiness.getIsCheckQrCode(woId);
    return woTypeServiceInsideDTO != null ? woTypeServiceInsideDTO.toWoTypeServiceDTO() : null;
  }

  @Override
  public Integer getCountListFtByUser(String userName, String keyword) {
    return woBusiness.getCountListFtByUser(userName, keyword);
  }

  @Override
  public List<CompCause> getListReasonOverdue(Long parentId, String nationCode) {
    return woBusiness.getListReasonOverdue(parentId, nationCode);
  }

  @Override
  public ResultDTO actionUpdateIsSupportWO(VsmartUpdateForm updateForm, String woIdStr,
      String needSupport, String userName, String content) throws Exception {
    return woBusiness.actionUpdateIsSupportWO(updateForm, woIdStr, needSupport, userName, content);
  }

  @Override
  public List<WoDTOSearch> getListWOByUsers(String userName, String summaryStatus, Integer isDetail,
      WoDTOSearch woDTO, int start, int count, int typeSearch) {
    return woRepository
        .getListWOByUsers(userName, summaryStatus, isDetail, woDTO, start, count, typeSearch);
  }

  @Override
  public ResultDTO updateCfgWoTickHelpVsmart(CfgWoTickHelpDTO cfgWoTickHelpDTO) {
    try {
      CfgWoTickHelpInsideDTO cfgInside = cfgWoTickHelpDTO.toInsideDto();
      cfgInside.setPage(0);
      cfgInside.setPageSize(0);
      cfgInside.setSortType("");
      cfgInside.setSortName("id");
      List<CfgWoTickHelpInsideDTO> lstCfgEntity = cfgWoTickHelpRepository
          .searchEntity(cfgInside);
      if ((lstCfgEntity == null) || (lstCfgEntity.isEmpty())) {
        return new ResultDTO(Constants.RESULT.FAIL, Constants.RESULT.FAIL,
            I18n.getLanguage("message.cfgWoTickHelp.notfound"));
      }
      CfgWoTickHelpInsideDTO cfgDTO = lstCfgEntity.get(0);
      cfgDTO.setStatus(1L);
      String result = cfgWoTickHelpRepository.add(cfgDTO).getKey();
      if (result.equals(Constants.RESULT.SUCCESS)) {
        //Tim nhung tickhelp con lai cua WO
        CfgWoTickHelpDTO searchDTO = new CfgWoTickHelpDTO();
        cfgInside.setStatus(0L);
        cfgInside.setWoId(DataUtil.isNullOrEmpty(cfgWoTickHelpDTO.getWoId()) ? null
            : Long.valueOf(cfgWoTickHelpDTO.getWoId()));
        List<CfgWoTickHelpInsideDTO> lstDTO = cfgWoTickHelpRepository.searchEntity(cfgInside);
        if ((lstDTO == null) || (lstDTO.isEmpty())) {
          //Dong needSupport cua WO
          if (!DataUtil.isNullOrEmpty(cfgWoTickHelpDTO.getWoId())) {
            WoDTO woDTO = woBusiness.findWoById(Long.valueOf(cfgWoTickHelpDTO.getWoId()));
            woDTO.setNeedSupport(null);
            ResultInSideDto woResult = woRepository.updateWo(woDTO.toModelInSide());
            if (!woResult.getKey().equals(Constants.RESULT.SUCCESS)) {
              return new ResultDTO(Constants.RESULT.FAIL, Constants.RESULT.FAIL,
                  woResult.getMessage());
            }
            //gui Tin nhan hoan thanh tick help cho FT
            MessagesDTO messageKd = new MessagesDTO();
            UsersEntity us = commonRepository.getUserByUserId(
                DataUtil.isNullOrEmpty(woDTO.getFtId()) ? null : Long.valueOf(woDTO.getFtId()));
            if (us != null) {
              String smsContent = "WO: " + woDTO.getWoCode()
                  + I18n.getLanguage("finishWoTickHelp") + us.getUsername() + "(" + us.getMobile()
                  + I18n.getLanguage("at") + DateTimeUtils
                  .convertDateToString(new Date(), Constants.ddMMyyyyHHmmss);
              messageKd.setReceiverPhone(us.getMobile());
              messageKd.setReceiverUsername(us.getUsername());
              messageKd.setReceiverFullName(us.getFullname());
              messageKd.setReceiverId(us.getUserId().toString());
              messageKd.setCreateTime(
                  DateTimeUtils.convertDateToString(new Date(), Constants.ddMMyyyyHHmmss));
              messageKd.setContent(smsContent);
              messageKd.setStatus("0");
              messageKd.setSmsGatewayId(smsGatewayId);  // fix code = 5
              messageKd.setSenderId(senderId);  // fix code = 400
              messagesRepository.insertOrUpdateCommon(messageKd);
            }
          }
        }
      } else {
        return new ResultDTO(Constants.RESULT.FAIL, Constants.RESULT.FAIL, result);
      }
      return new ResultDTO(Constants.RESULT.SUCCESS, Constants.RESULT.SUCCESS,
          Constants.RESULT.SUCCESS);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return new ResultDTO(Constants.RESULT.FAIL, Constants.RESULT.FAIL, e.getMessage());
    }
  }

  @Override
  public ResultDTO updateTechnicalClues(String woCode, String technicalClues) {
    try {
      WoInsideDTO wo = woRepository.getWoByWoSystemCode(woCode);
      if (wo != null) {
        WoDeclareServiceEntity dec = woDeclareServiceRepository.findById(wo.getWoId());
        if (dec != null) {
          dec.setTechnicalClues(technicalClues);
          woDeclareServiceRepository.insertOrUpdateWoDeclareService(dec.toDTO());
          // cap nhat file excel
          String fileNameLst = wo.getFileName();
          if (fileNameLst != null) {
            String[] f = fileNameLst.split(",");
            String fileName = f[0].trim();
            String fileNameFull = ftpFolder + "/"
                + FileUtils.createPathFtpByDate(wo.getCreateDate()) + "/" + fileName;
            byte[] bytes = FileUtils.getFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
                PassTranformer.decrypt(ftpPass), fileNameFull);
            String pathTemp = FileUtils.saveTempFile(fileName, bytes, tempFolder);
            ExcelWriterUtils ewu = new ExcelWriterUtils();
            Workbook workBook = ewu.readFileExcel(pathTemp);
            Sheet sheetOne = workBook.getSheetAt(0);
            int c = 1;
            int r = 2;
            Row rowD = sheetOne.getRow(r);
            Cell cellD = rowD.getCell(c);
            ewu.createCell(sheetOne, c, r, technicalClues, cellD.getCellStyle());
            ewu.saveToFileExcel(fileNameFull);
            FileOutputStream fileOut = new FileOutputStream(pathTemp);
            workBook.write(fileOut);
            fileOut.close();
            byte[] bytesOut = FileUtils.convertFileToByte(new File(pathTemp));
            FileUtils.saveFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
                PassTranformer.decrypt(ftpPass), ftpFolder + "/"
                    + FileUtils.createPathFtpByDate(wo.getCreateDate()), fileName, bytesOut);
          }

        } else {
          return new ResultDTO(Constants.RESULT.FAIL, Constants.RESULT.FAIL,
              "Wo declare does not exists");
        }
      } else {
        return new ResultDTO(Constants.RESULT.FAIL, Constants.RESULT.FAIL, "Wo does not exists");
      }
      return new ResultDTO(Constants.RESULT.SUCCESS, Constants.RESULT.SUCCESS,
          Constants.RESULT.SUCCESS);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return new ResultDTO(Constants.RESULT.FAIL, Constants.RESULT.FAIL, e.getMessage());
    }
  }

  @Override
  public List<WoCdGroupDTO> getListCdGroup(String userName) {
    List<WoCdGroupDTO> dtoList = new ArrayList<>();
    List<WoCdGroupInsideDTO> list = woCdGroupBusiness.getListCdGroup(userName);
    if (list != null && list.size() > 0) {
      for (WoCdGroupInsideDTO dto : list) {
        WoCdGroupDTO woCdGroupOut = dto.toDtoOutSide();
        woCdGroupOut.setDefaultSortField("woGroupName");
        dtoList.add(woCdGroupOut);
      }
    }
    return dtoList;
  }

  @Override
  public String insertListWoChecklistDetail(List<WoChecklistDTO> lstChecklist) {
    return woChecklistDetailRepository.insertListWoChecklistDetail(lstChecklist);
  }

  @Override
  public List<WoWorklogInsideDTO> getListDataByWoIdPaging(String woId, int rowStart, int maxRow,
      String sortType, String sortFieldList) {
    return woWorklogRepository
        .getListDataByWoIdPaging(woId, rowStart, maxRow, sortType, sortFieldList);
  }

  @Override
  public List<WoCdGroupInsideDTO> getListCdGroupByDTO(WoCdGroupInsideDTO woCdGroupDTO) {
    return woCdGroupBusiness.getListCdGroupByDTO(woCdGroupDTO);
  }

  @Override
  public List<ObjKeyValue> getObjKeyValueFromFile(Long woId, Long type) {
    List<ObjKeyValue> lst = new ArrayList<>();
    try {
      WoInsideDTO wo = woRepository.findWoByIdNoOffset(woId);
      if (wo != null && StringUtils.isNotNullOrEmpty(wo.getFileName())) {
        String[] fileName = wo.getFileName().split(",");
        String pathDate = "";
        if (wo.getCreateDate() != null) {
          pathDate = FileUtils.createPathFtpByDate(wo.getCreateDate());
        }
        String filePathOld = ftpFolder + "/" + pathDate + "/" + fileName[fileName.length - 1].trim();
//        String filePathOld = ftpFolder + "/" + fileName[fileName.length - 1].trim();
        byte[] bytes = FileUtils.getFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
            PassTranformer.decrypt(ftpPass), filePathOld);
        InputStream inputStreams = new ByteArrayInputStream(bytes);
        Map<Long, ObjKeyValue> mapHeader = getHeader(filePathOld, inputStreams);
        inputStreams = new ByteArrayInputStream(bytes);

        return getDataExcelTestService(mapHeader, filePathOld, inputStreams);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null; //dang test gio
  }

  @Override
  public ResultDTO startWork(WoDTO woDto) {
    Date now = new Date();
    try {

      if (woDto == null) {
        return new ResultDTO("FAIL", "FAIL", I18n.getLanguage("woIsNotExists"));
      }
      if (StringUtils.isStringNullOrEmpty(woDto.getLongitude())) {
        return new ResultDTO("FAIL", "FAIL", I18n.getLanguage("longitudeIsnotNull"));
      }

      if (StringUtils.isStringNullOrEmpty(woDto.getLatitude())) {
        return new ResultDTO("FAIL", "FAIL", I18n.getLanguage("latitudeIsNotNull"));
      }

      WoInsideDTO wo = woRepository.findWoByIdNoOffset(Long.valueOf(woDto.getWoId()));
      UsersEntity us = userBusiness.getUserByUserId(wo.getFtId());

      if (wo.getStartWorkingTime() == null) {
        wo.setStartLongitude(woDto.getLongitude());
        wo.setStartLatitude(woDto.getLatitude());
        wo.setStartWorkingTime(DateTimeUtils.getSysDateTime());
      }

      // update lich su start
      WorkTimeDTO workTime = new WorkTimeDTO();
      workTime.setWoId(wo.getWoId().toString());
      workTime.setUserId(wo.getFtId().toString());
      workTime.setLongtitude(woDto.getLongitude());
      workTime.setLatitude(woDto.getLatitude());
      workTime.setInsertTime(DateTimeUtils.date2ddMMyyyyHHMMss(now));

      ResultDTO res = woTimeBusiness.insertOrUpdateWorkTime(workTime);

      wo.setWorkStatus("1");

      updateWoAndHistory(wo, us, "Update start working", wo.getStatus().intValue(), now);

      return new ResultDTO("SUCCESS", "SUCCESS", wo.getStartWorkingTime());
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return new ResultDTO("FAIL", "FAIL", I18n.getLanguage("wo.haveSomeError"));
    }
  }

  @Override
  public List<ResultDTO> getWOSummaryInfobyType(String username, int typeSearch, Long cdId,
      WoDTOSearch woDTOInput) {
    return woRepository.getWOSummaryInfobyType(username, typeSearch, cdId, woDTOInput);
  }

  @Override
  public List<WoHisForAccountDTO> getListWoHisForAccount(WoDTOSearch woDTOSearch, String locale) {
    List<WoHisForAccountDTO> lst = new ArrayList<>();
    try {
      if (woDTOSearch != null && woDTOSearch.getLstAccount() != null) {
        lst = woRepository.getListWoHisForAccount(woDTOSearch.getLstAccount());
        if (lst != null) {
          for (WoHisForAccountDTO o : lst) {
            // lay nguyen nhan 3 cap
            CompCause lv3 = woRepository.getCompCause(o.getReasonLevel3Id());
            if (lv3 != null) {
              o.setReasonLevel3Id(lv3.getCompCauseId());
              o.setReasonLevel3Name(lv3.getName());
              CompCause lv2 = woRepository.getCompCause(lv3.getParentId());
              if (lv2 != null) {
                o.setReasonLevel2Id(lv2.getCompCauseId());
                o.setReasonLevel2Name(lv2.getName());
                CompCause lv1 = woRepository.getCompCause(lv2.getParentId());
                if (lv1 != null) {
                  o.setReasonLevel1Id(lv1.getCompCauseId());
                  o.setReasonLevel1Name(lv1.getName());
                }
              }
            }
            // lay danh sach tai san
            List<WoMaterialDeducteDTO> lstMaterialDeducteDTOs = woMaterialDeducteBusiness
                .getMaterialDeducteKeyByWOOutSide(o.getWoId());
            o.setLstMaterial(lstMaterialDeducteDTOs);
          }
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return lst;
  }
}
