package com.viettel.gnoc.od.business;

import static com.viettel.gnoc.commons.repository.BaseRepository.getSqlLanguageExchange;
import static com.viettel.gnoc.commons.repository.BaseRepository.setLanguage;

import com.viettel.gnoc.commons.business.CatItemBusiness;
import com.viettel.gnoc.commons.business.CommonBusiness;
import com.viettel.gnoc.commons.business.MessagesBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.commons.dto.LanguageExchangeDTO;
import com.viettel.gnoc.commons.dto.MessagesDTO;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.commons.model.CatItemEntity;
import com.viettel.gnoc.commons.model.UnitEntity;
import com.viettel.gnoc.commons.proxy.OdCategoryServiceProxy;
import com.viettel.gnoc.commons.repository.CatItemRepository;
import com.viettel.gnoc.commons.repository.GnocFileRepository;
import com.viettel.gnoc.commons.repository.LanguageExchangeRepository;
import com.viettel.gnoc.commons.repository.UnitRepository;
import com.viettel.gnoc.commons.utils.CellConfigExport;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.ConfigFileExport;
import com.viettel.gnoc.commons.utils.ConfigHeaderExport;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.GNOC_FILE_BUSSINESS;
import com.viettel.gnoc.commons.utils.Constants.OD_STATUS;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.DateUtil;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.od.dto.ObjKeyValueVsmartDTO;
import com.viettel.gnoc.od.dto.OdDTO;
import com.viettel.gnoc.od.dto.OdDTOSearch;
import com.viettel.gnoc.od.dto.OdFileDTO;
import com.viettel.gnoc.od.dto.OdParamInsideDTO;
import com.viettel.gnoc.od.dto.OdSearchInsideDTO;
import com.viettel.gnoc.od.dto.OdTypeDTO;
import com.viettel.gnoc.od.dto.OdTypeDetailDTO;
import com.viettel.gnoc.od.dto.OdTypeMapLocationDTO;
import com.viettel.gnoc.od.repository.OdCommonRepository;
import com.viettel.gnoc.od.repository.OdRepository;
import com.viettel.security.PassTranformer;
import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * @author TungPV
 */
@Service
@Transactional
@Slf4j
public class OdCommonBusinessImpl implements OdCommonBusiness {

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

  @Value("${application.temp.folder}")
  private String tempFolder;

  @Value("${application.upload.folder}")
  private String uploadFolder;

  @Value("${application.SmsGatewayId:5}")
  private String smsGatewayId;

  @Value("${application.SenderId:400}")
  private String senderId;

  @Value("${application.extensionAllow:null}")
  private String extension;

  @Autowired
  OdCommonRepository odCommonRepository;

  @Autowired
  CommonBusiness commonBusiness;

  @Autowired
  CatItemRepository catItemRepository;

  @Autowired
  LanguageExchangeRepository languageExchangeRepository;

  @Autowired
  OdParamBusiness odParamBusiness;

  @Autowired
  CatItemBusiness catItemBusiness;

  @Autowired
  OdCategoryServiceProxy odCategoryServiceProxy;

  @Autowired
  OdHistoryBusiness odHistoryBusiness;

  @Autowired
  OdRelationBusiness odRelationBusiness;

  @Autowired
  OdRepository odRepository;

  @Autowired
  MessagesBusiness messagesBusiness;

  @Autowired
  UnitRepository unitRepository;

  @Autowired
  GnocFileRepository gnocFileRepository;

  private final static String OD_RESULT_IMPORT = "OD_RESULT_IMPORT";
  private final static String OD_EXPORT = "OD_EXPORT";

  @Override
  public Datatable getListDataSearch(OdSearchInsideDTO odSearchInsideDTO) {
    log.debug("Request to getListDataSearch : {}", odSearchInsideDTO);
    return odCommonRepository.getListDataSearch(odSearchInsideDTO);
  }

  @Override
  public Integer getCountListDataSearchForOther(OdSearchInsideDTO odDTO) {
    log.debug("Request to getCountListDataSearchForOther : {}", odDTO);
    odDTO.setPage(1);
    odDTO.setPageSize(10);
    Datatable datatable = odCommonRepository.getListDataSearch(odDTO);
    return datatable.getTotal();
  }

  @Override
  public List<OdSearchInsideDTO> getListDataSearchForOther(OdSearchInsideDTO odDTOSearch) {
    log.debug("Request to getListDataSearchForOther : {}", odDTOSearch);
    List<OdSearchInsideDTO> listDataSearch = (List<OdSearchInsideDTO>) odCommonRepository
        .getListDataSearch(odDTOSearch).getData();
    String leeLocale = I18n.getLocale();
    if (listDataSearch != null && listDataSearch.size() > 0) {
      try {
        Map<String, Object> map = getSqlLanguageExchange(Constants.LANGUAGUE_EXCHANGE_SYSTEM.WO,
            Constants.APPLIED_BUSSINESS.WO_PRIORITY, leeLocale);
        String sqlLanguage = (String) map.get("sql");
        Map mapParam = (Map) map.get("mapParam");
        Map mapType = (Map) map.get("mapType");
        List<LanguageExchangeDTO> lstLanguage = languageExchangeRepository
            .findBySql(sqlLanguage, mapParam, mapType, LanguageExchangeDTO.class);

        listDataSearch = setLanguage(listDataSearch, lstLanguage, "priorityId", "priorityName");

        Map<String, Object> map2 = getSqlLanguageExchange(Constants.LANGUAGUE_EXCHANGE_SYSTEM.WO,
            Constants.APPLIED_BUSSINESS.WO_TYPE, leeLocale);
        String sqlLanguage2 = (String) map2.get("sql");
        Map mapParam2 = (Map) map2.get("mapParam");
        Map mapType2 = (Map) map2.get("mapType");
        List<LanguageExchangeDTO> lstLanguage2 = languageExchangeRepository
            .findBySql(sqlLanguage2, mapParam2, mapType2, LanguageExchangeDTO.class);
        listDataSearch = setLanguage(listDataSearch, lstLanguage2, "woTypeId", "woTypeName");
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return listDataSearch;
  }

  @Override
  public List<OdSearchInsideDTO> getListDataSearchVsmart(OdSearchInsideDTO odDTOSearch) {
//    Users cu = gettDAO().getUserByUserName(odDTOSearch.getUserName());
    List<OdSearchInsideDTO> lstOdSearch = odCommonRepository.getListDataExport(odDTOSearch);
    // lay danh sach ten trang thai OD
    if (lstOdSearch != null && lstOdSearch.size() > 0) {
      List<CatItemDTO> lstStatus = (List<CatItemDTO>) catItemBusiness
          .getItemMaster(Constants.OD_MASTER_CODE.OD_STATUS,
              Constants.OD_MASTER_CODE.OD_MASTER_DATA,
              Constants.OD_MASTER_CODE.OD_MASTER_DATA_STATUS, "itemValue", "itemName").getData();

      List<CatItemDTO> lstPriority = (List<CatItemDTO>) catItemBusiness
          .getItemMaster(Constants.OD_MASTER_CODE.OD_PRIORITY,
              Constants.OD_MASTER_CODE.OD_MASTER_DATA,
              Constants.OD_MASTER_CODE.OD_MASTER_DATA_PRIORITY, "itemId", "itemName").getData();

      Map<String, String> mapStatus = new HashMap<String, String>();
      Map<String, String> mapPriority = new HashMap<String, String>();

      if (lstStatus != null && lstStatus.size() > 0) {
        for (CatItemDTO i : lstStatus) {
          mapStatus.put(i.getItemValue(), i.getItemName());
        }
      }

      if (lstPriority != null && lstPriority.size() > 0) {
        for (CatItemDTO i : lstPriority) {
          mapPriority.put(i.getItemId().toString(), i.getItemName());
        }
      }

      for (OdSearchInsideDTO o : lstOdSearch) {
        o.setStatusName(mapStatus.get(o.getStatus()));
        o.setPriorityName(mapPriority.get(o.getPriorityId()));
      }

    }
    return lstOdSearch;

  }

  @Override
  public ResultDTO insertOdFromVsmart(List<ObjKeyValueVsmartDTO> o, String userName,
      String odTypeCode, String woId, String insertSource, String createUnitCode,
      String crateUnitId) {
    OdSearchInsideDTO odDTO = prepareDataInsertVsmart(o);
    odDTO.setCreatePersonName(userName);
    odDTO.setOdTypeCode(odTypeCode);
    if (!StringUtils.isStringNullOrEmpty(woId)) {
      odDTO.setWoId(Long.parseLong(woId));
    }
    Map<String, String> mapConfigProperty = commonBusiness.getConfigProperty();

    odDTO.setInsertSource(mapConfigProperty.get(insertSource));
    odDTO.setCreateUnitCode(createUnitCode);
    if (StringUtils.isNotNullOrEmpty(crateUnitId)) {
      odDTO.setCreateUnitId(Long.parseLong(crateUnitId));
    }
    Date startDate = odDTO.getStartTime();
    Date endDate = odDTO.getEndTime();
    if (startDate.compareTo(endDate) == 1) {
      throw new RuntimeException(I18n.getLanguage("od.StartTime.invalidValue"));
    }
    ResultDTO result = insertOdFromOtherSystem(odDTO);
    if (result.getKey().equals(RESULT.SUCCESS)) {
      String[] odCodeStr = result.getId().toString().split("_");
      String odId = odCodeStr[odCodeStr.length - 1];
      String updateTimeTmp = DateTimeUtils.getSysDateTime();
      Date updateTimeConvert = DateTimeUtils.convertStringToDate(updateTimeTmp);
      for (ObjKeyValueVsmartDTO objDTO : o) {
        if ("2".equals(objDTO.getType()) //Type 2:thuoc
            && objDTO.getValue() != null && !"".equals(objDTO.getValue())) {
          OdParamInsideDTO odParamInsideDTO = new OdParamInsideDTO();
          odParamInsideDTO.setKey(objDTO.getKey());
          odParamInsideDTO.setValue(objDTO.getValue());
          odParamInsideDTO.setOdId(Long.valueOf(odId));
          odParamInsideDTO.setUpdatedUser(userName);
          odParamInsideDTO.setParamType(insertSource);
          odParamInsideDTO.setUpdatedTime(updateTimeConvert);
          odParamBusiness.add(odParamInsideDTO);
        }
      }
    }
    return result;
  }

  @Override
  public ResultDTO insertOdFromOtherSystem(OdSearchInsideDTO o) {
    ResultDTO resultOutside = new ResultDTO();
    ResultInSideDto result = new ResultInSideDto();
    try {
      OdDTO odDTO = new OdDTO();
      o = prepareDataInsert(o);
      odDTO = (OdDTO) commonBusiness.updateObjectData(o, odDTO);
      String odId = odCommonRepository.getSeqTableOD("OD_SEQ");
      String odCode =
          "OD_" + odDTO.getInsertSource() + "_" + DateUtil.date2StringNoSlash(new Date()) + "_"
              + odId;

      odDTO.setOdCode(odCode);
      odDTO.setOdId(Long.valueOf(odId));
      odDTO.setStatus(Constants.OD_STATUS.NEW);
      odDTO.setLastUpdateTime(new Date());
      result = odRepository.add(odDTO);
      if (!RESULT.SUCCESS.equals(result.getKey())) {
        throw new RuntimeException(I18n.getLanguage("od.wo.err.save.od"));
      }
      // luu lich su
      UsersInsideDto usersInsideDto = commonBusiness
          .getUserByUserId(Long.valueOf(odDTO.getCreatePersonId()));
      result = odHistoryBusiness.insertOdHistory(odDTO, null, odDTO.getStatus(),
          I18n.getLanguage("od.AddNewOd") + "(" + odDTO.getStartTime() + " - " + odDTO.getEndTime()
              + ")", usersInsideDto.toEntity());
      if (!RESULT.SUCCESS.equals(result.getKey())) {
        throw new RuntimeException(I18n.getLanguage("wo.err.save.odHis"));
      }
      // luu file dinh kem
      if (o.getLstFileName() != null && o.getLstFileName().size() > 0 && o.getLstFileArr() != null
          && o.getLstFileArr().size() > 0) {
        updateFile(odDTO, usersInsideDto, o.getLstFileName(), o.getLstFileArr());
      }
      // luu thong tin lien ket
      result = odRelationBusiness.insertLstRelation(odDTO);
      if (!RESULT.SUCCESS.equals(result.getKey())) {
        throw new RuntimeException(I18n.getLanguage("wo.err.save.odRelation"));
      }
      // thuc hien nhan tin nhan vien tao va don vi xu ly
      sendMesseageCreateOd(odDTO, usersInsideDto);

      resultOutside.setId(odCode);
      resultOutside.setKey(RESULT.SUCCESS);
      resultOutside.setMessage(RESULT.SUCCESS);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw new RuntimeException(e.getMessage());
    }
    return resultOutside;
  }

  @Override
  public File exportData(OdSearchInsideDTO odSearchInsideDTO) throws Exception {
    List<OdSearchInsideDTO> odSearchInsideDTOS = odCommonRepository.getListDataExport(
        odSearchInsideDTO);
    return exportFileEx(odSearchInsideDTOS, "");
  }

  private File exportFileEx(List<OdSearchInsideDTO> lstImport, String key) throws Exception {
    String title = "";
    String fileNameOut = "";
    String sheetName = "";
    List<ConfigFileExport> fileExports = new ArrayList<>();
    ConfigHeaderExport columnSheet1;
    List<ConfigHeaderExport> lstHeaderSheet1 = new ArrayList<>();
    columnSheet1 = new ConfigHeaderExport("odCode", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    lstHeaderSheet1.add(columnSheet1);
    columnSheet1 = new ConfigHeaderExport("odName", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    lstHeaderSheet1.add(columnSheet1);
    columnSheet1 = new ConfigHeaderExport("statusName", "LEFT", false, 0, 0, new String[]{},
        null, "STRING");
    lstHeaderSheet1.add(columnSheet1);
    columnSheet1 = new ConfigHeaderExport("odTypeName", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    lstHeaderSheet1.add(columnSheet1);
    columnSheet1 = new ConfigHeaderExport("priorityName", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    lstHeaderSheet1.add(columnSheet1);
    columnSheet1 = new ConfigHeaderExport("createTime", "LEFT", false, 0, 0, new String[]{},
        null,
        "STRING");
    lstHeaderSheet1.add(columnSheet1);
    columnSheet1 = new ConfigHeaderExport("remainTime", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    lstHeaderSheet1.add(columnSheet1);
    columnSheet1 = new ConfigHeaderExport("createUnitName", "LEFT", false, 0, 0, new String[]{},
        null,
        "STRING");
    lstHeaderSheet1.add(columnSheet1);
    columnSheet1 = new ConfigHeaderExport("receiveUnitName", "LEFT", false, 0, 0, new String[]{},
        null,
        "STRING");
    lstHeaderSheet1.add(columnSheet1);
    columnSheet1 = new ConfigHeaderExport("receiveUserName", "LEFT", false, 0, 0, new String[]{},
        null,
        "STRING");
    if (Constants.RESULT_IMPORT.equals(key)) {
      fileNameOut = OD_RESULT_IMPORT;
      sheetName = OD_RESULT_IMPORT;
      title = OD_RESULT_IMPORT;
      lstHeaderSheet1.add(columnSheet1);
      columnSheet1 = new ConfigHeaderExport("resultImport", "LEFT", false, 0, 0, new String[]{},
          null,
          "STRING");
    } else {
      fileNameOut = OD_EXPORT;
      title = OD_EXPORT;
      sheetName = OD_EXPORT;
    }
    lstHeaderSheet1.add(columnSheet1);
    Map<String, String> fieldSplit = new HashMap<>();
    ConfigFileExport configfileExport = new ConfigFileExport(
        lstImport
        , sheetName
        , title
        , ""
        , 5
        , 2
        , 9
        , true
        , "language.od"
        , lstHeaderSheet1
        , fieldSplit
        , ""
    );
    configfileExport.setLangKey(I18n.getLocale());
    List<CellConfigExport> lstCellSheet = new ArrayList<>();
    CellConfigExport cellSheet;

    cellSheet = new CellConfigExport(5, 0, 0, 0, "STT",
        "HEAD", "STRING");
    lstCellSheet.add(cellSheet);
    configfileExport.setLstCreatCell(lstCellSheet);
    fileExports.add(configfileExport);

    String path = this.getClass().getClassLoader().getResource("").getPath();
    String fullPath = URLDecoder.decode(path, "UTF-8");
    String fileTemplate = fullPath + "templates" + File.separator
        + "TEMPLATE_EXPORT.xlsx";
    String rootPath = tempFolder + File.separator;

    File fileExport = CommonExport.exportExcel(
        fileTemplate
        , fileNameOut
        , fileExports
        , rootPath
        , null
    );
    return fileExport;
  }

  /*
    chuan bi du lieu them moi tu he VSMART
    */
  public OdSearchInsideDTO prepareDataInsertVsmart(List<ObjKeyValueVsmartDTO> lstObjDTO) {
    OdSearchInsideDTO odDto = new OdSearchInsideDTO();
    try {
      for (ObjKeyValueVsmartDTO objDTO : lstObjDTO) {
        if (objDTO.getKeyCode() != null && "odName".equals(objDTO.getKeyCode())) {
          odDto.setOdName(objDTO.getValue());
        } else if (objDTO.getKeyCode() != null && "receiveUnitCode".equals(objDTO.getKeyCode())) {
          odDto.setReceiveUnitCode(objDTO.getValue());
        } else if (objDTO.getKeyCode() != null && "priorityCode".equals(objDTO.getKeyCode())) {
          odDto.setPriorityCode(objDTO.getValue());
        } else if (objDTO.getKeyCode() != null && "odDescription".equals(objDTO.getKeyCode())) {
          odDto.setDescription(objDTO.getValue());
        } else if (objDTO.getKeyCode() != null && "startTime".equals(objDTO.getKeyCode())) {
          odDto.setStartTime(DateTimeUtils.convertStringToDateTime(objDTO.getValue()));
        } else if (objDTO.getKeyCode() != null && "endTime".equals(objDTO.getKeyCode())) {
          odDto.setEndTime(DateTimeUtils.convertStringToDateTime(objDTO.getValue()));
        } else if (objDTO.getKeyCode() != null && "odFile".equals(objDTO.getKeyCode()) && "Uploader"
            .equals(objDTO
                .getControlType())) {
          odDto.setLstFileName(objDTO.getLstFileName());
          odDto.setLstFileArr(objDTO.getLstFileArr());
        }
      }
      odDto.setIsUnitOfGnoc(true);
      odDto.setCreateTime(DateTimeUtils.convertStringToDateTime(DateTimeUtils.getSysDateTime()));
      return odDto;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw new RuntimeException(e);
    }
  }

  /*
    chuan bi du lieu them moi tu he thong khac
    */
  public OdSearchInsideDTO prepareDataInsert(OdSearchInsideDTO dto) {
    //nguoi tao
    Date now = new Date();
    if (StringUtils.isStringNullOrEmpty(dto.getCreatePersonName())) {
      throw new RuntimeException(I18n.getLanguage("od.createUser.isNotNull"));
    } else {
      UsersInsideDto cu = commonBusiness.getUserByUserName(dto.getCreatePersonName());
      if (cu != null) {
        dto.setCreatePersonId(cu.getUserId());
      } else {
        throw new RuntimeException(I18n.getLanguage("od.createUser.not.exists"));
      }
    }
    //loai od
    if (StringUtils.isStringNullOrEmpty(dto.getOdTypeCode())) {
      throw new RuntimeException(I18n.getLanguage("od.odTypeCode.isNotNull"));
    } else {
      OdTypeDTO odType = odCommonRepository.checkOdTypeExist(dto.getOdTypeCode());
      if (odType != null) {
        dto.setOdTypeId(odType.getOdTypeId());
        // suy ra don vi tao va don vi xu ly dua vao ma dia ban
        if (!StringUtils.isStringNullOrEmpty(dto.getLocationCode())) {
          OdDTOSearch oTmp = new OdDTOSearch(odType.getOdTypeId().toString(),
              dto.getLocationCode());
          OdTypeMapLocationDTO odTypeMapLocationDTO = odCategoryServiceProxy
              .getListOdTypeMapByOdTypeIdAndLocation(oTmp);
          if (odTypeMapLocationDTO != null) {
            if (!StringUtils.isStringNullOrEmpty(odTypeMapLocationDTO.getCreateUnitId())) {
              dto.setCreateUnitId(odTypeMapLocationDTO.getCreateUnitId());
            }
            if (!StringUtils.isStringNullOrEmpty(odTypeMapLocationDTO.getReceiveUnitId())) {
              dto.setReceiveUnitId(odTypeMapLocationDTO.getReceiveUnitId());
            }
          }
        }
      } else {
        throw new RuntimeException(I18n.getLanguage("od.odTypeCode.not.exists"));
      }
    }

    // bo sung don vi tao dua vao ma don vi
    if (StringUtils.isStringNullOrEmpty(dto.getCreateUnitId())) {
      if (StringUtils.isNotNullOrEmpty(dto.getCreateUnitCode())) {
        UnitEntity ur = null;
        if (dto.getIsUnitOfGnoc()) {
          ur = odCommonRepository.getUnitByUnitCode(dto.getReceiveUnitCode());

        } else {
          ur = odCommonRepository.getUnitCodeMapNims(dto.getReceiveUnitCode());
        }
        if (ur != null) {
          if (!"0".equals(String.valueOf(ur.getStatus()))) {
            dto.setCreateUnitId(ur.getUnitId());
          }
        }
      }
    }

    //don vi xu ly lay ma don vi tren NIMS ==> don vi tren GNOC
    if (StringUtils.isStringNullOrEmpty(dto.getReceiveUnitCode())) {
      throw new RuntimeException(I18n.getLanguage("od.ReceiveUnitCode.isNotNull"));
    } else {
      UnitEntity ur = null;
      if (dto.getIsUnitOfGnoc()) {
        ur = odCommonRepository.getUnitByUnitCode(dto.getReceiveUnitCode());

      } else {
        ur = odCommonRepository.getUnitCodeMapNims(dto.getReceiveUnitCode());
      }
      if (ur != null) {
        dto.setReceiveUnitId(ur.getUnitId());
      } else {
        throw new RuntimeException(I18n.getLanguage("od.receiveUnitCode.not.exists"));
      }
    }
    //muu uu tien suy ra tu ma muc uu tien
    if (StringUtils.isStringNullOrEmpty(dto.getPriorityCode())) {
      throw new RuntimeException(I18n.getLanguage("od.PriorityCode.isNotNull"));
    } else {
      CatItemEntity pri = catItemRepository.getItemByItemCode(dto.getPriorityCode());
      if (pri == null || pri.getItemId() == null) {
        throw new RuntimeException(I18n.getLanguage("od.PriorityCode.isNotExists"));
      } else {
        dto.setPriorityId(pri.getItemId());
      }
    }
    //start time
    if (StringUtils.isStringNullOrEmpty(dto.getStartTime())) {
      throw new RuntimeException(I18n.getLanguage("od.StartTime.isNotNull"));
    } else if (!DateTimeUtils.isDateHH24(
        DateTimeUtils.convertDateToString(dto.getStartTime(), Constants.formatterDateTimeText))) {
      throw new RuntimeException(I18n.getLanguage("od.StartTime.invalidFomat"));
    }
    //end time
    if (StringUtils.isStringNullOrEmpty(dto.getEndTime())) {
      // lay End time tu cau hinh
      try {

        OdTypeDetailDTO detailDto = new OdTypeDetailDTO();
        detailDto.setPriorityId(Long.valueOf(dto.getPriorityId()));
        detailDto.setOdTypeId(Long.valueOf(dto.getOdTypeId()));
        List<OdTypeDetailDTO> lstDetail = odCategoryServiceProxy
            .getListOdTypeDetail(detailDto);
        if (lstDetail != null && lstDetail.size() > 0) {
          OdTypeDetailDTO o = lstDetail.get(0);
          if (o != null && !StringUtils.isStringNullOrEmpty(o.getProcessTime())) {
            now.setHours(now.getHours() + o.getProcessTime().intValue());
            dto.setEndTime(now);
          }
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
      if (StringUtils.isStringNullOrEmpty(dto.getEndTime())) {
        throw new RuntimeException(I18n.getLanguage("od.EndTime.isNotNull"));
      }
    } else if (!DateTimeUtils.isDateHH24(
        DateTimeUtils.convertDateToString(dto.getEndTime(), Constants.formatterDateTimeText))) {
      throw new RuntimeException(I18n.getLanguage("od.EndTime.invalidFomat"));
    }
    // od name
    if (StringUtils.isStringNullOrEmpty(dto.getOdName())) {
      throw new RuntimeException(I18n.getLanguage("od.OdName.isNotNull"));
    }
    // create time
    if (StringUtils.isStringNullOrEmpty(dto.getCreateTime())) {
      throw new RuntimeException(I18n.getLanguage("od.CreateTime.isNotNull"));
    } else if (!DateTimeUtils.isDateHH24(
        DateTimeUtils.convertDateToString(dto.getCreateTime(), Constants.formatterDateTimeText))) {
      throw new RuntimeException(I18n.getLanguage("od.CreateTime.invalidFomat"));
    }
    return dto;
  }

  public void sendMesseageCreateOd(OdDTO odDTO, UsersInsideDto u) {
    try {
      // nhan tin nhan vien tao
      String createSmsContent = getLang("2".equals(u.getUserLanguage()) ? new Locale("en_US") : null, "OdCreateContentSMS");
      createSmsContent = replaceSmsContent(odDTO, createSmsContent);
      createMessage(u, createSmsContent);
    } catch (Exception e) {
      log.error("GNOC_OD: Co loi khi nhan tin cho nhan vien tao OD:" + odDTO.getOdCode(), e);
    }
    // nhan tin don vi xu ly
    if (!StringUtils.isStringNullOrEmpty(odDTO.getReceiveUnitId())) {
      List<UsersInsideDto> lstUs = commonBusiness
          .getListUserOfUnit(Long.valueOf(odDTO.getReceiveUnitId()));
      for (UsersInsideDto i : lstUs) {
        try {
          String receiveUnitSmsContent = getLang("2".equals(i.getUserLanguage()) ?  new Locale("en_US") : null, "OdReceiveUnitSmsContent");
          receiveUnitSmsContent = replaceSmsContent(odDTO, receiveUnitSmsContent);
          createMessage(i, receiveUnitSmsContent);
        } catch (Exception e) {
          log.error("GNOC_OD: Co loi khi nhan tin cho nhan vien:" + i.getUsername(), e);
        }
      }
    }

  }

  public String replaceSmsContent(OdDTO odDTO, String content) {
    if (odDTO.getOdCode() != null) {
      content = content.replace("[odCode]", odDTO.getOdCode());
    }
    if (odDTO.getOdName() != null) {
      content = content.replace("[odName]", odDTO.getOdName());
    }
    if (odDTO.getCreateTime() != null) {
      content = content.replace("[createTime]", odDTO.getCreateTime().toString());
    }
    if (odDTO.getDescription() != null) {
      content = content.replace("[description]", odDTO.getDescription());
    }
    if (odDTO.getStartTime() != null) {
      content = content.replace("[startTime]", odDTO.getStartTime().toString());
    }
    if (odDTO.getEndTime() != null) {
      content = content.replace("[endTime]", odDTO.getEndTime().toString());
    }
    return content;
  }

  public void createMessage(UsersInsideDto u, String smsContent) {
    MessagesDTO message = new MessagesDTO();
    message.setSmsGatewayId(smsGatewayId);  // fix code = 5
    message.setReceiverId(String.valueOf(u.getUserId()));
    message.setReceiverUsername(u.getUsername());
    message.setReceiverFullName(u.getFullname());
    message
        .setSenderId(senderId);  // fix code = 400
    message.setReceiverPhone(u.getMobile());
    message.setStatus(String.valueOf(OD_STATUS.REJECT));
    try {
      message.setCreateTime(
          DateTimeUtils.convertDateToString((new Date()), Constants.formatterDateTimeText));
      message.setContent(smsContent);

      messagesBusiness.insertOrUpdateWfm(message);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }

  }

  public void updateFile(OdDTO od, UsersInsideDto usersInsideDto, List<String> listFileName,
      List<byte[]> fileArr) {
    if (listFileName != null && listFileName.size() > 0 && fileArr != null && fileArr.size() > 0) {
      if (listFileName.size() != fileArr.size()) {
        throw new RuntimeException(I18n.getLanguage("wo.numberFileNotMap"));
      }
      UnitDTO unitToken = unitRepository.findUnitById(usersInsideDto.getUnitId());
      List<GnocFileDto> gnocFileDtos = new ArrayList<>();
      for (int i = 0; i < listFileName.size(); i++) {
        if (extension != null) {
          String[] extendArr = extension.split(",");
          Boolean checkExt = false;
          for (String e : extendArr) {
            if (listFileName.get(i).toLowerCase().endsWith(e.toLowerCase())) {
              checkExt = true;
              break;
            }
          }
          if (!checkExt) {
            throw new RuntimeException(I18n.getLanguage("wo.fileImportInvalidExten"));
          }
        }
        try {
          String fullPath = FileUtils
              .saveFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
                  PassTranformer.decrypt(ftpPass), ftpFolder, listFileName.get(i), fileArr.get(i),
                  null);
          //Start save file old
          String fullPathOld = FileUtils
              .saveUploadFile(listFileName.get(i), fileArr.get(i), uploadFolder, null);
          OdFileDTO odFileDTO = new OdFileDTO();
          odFileDTO.setOdId(od.getOdId());
          odFileDTO.setPath(FileUtils.getFilePath(fullPathOld));
          odFileDTO.setFileName(FileUtils.getFileName(fullPathOld));
          ResultInSideDto resultFileDataOld = odRepository.insertOdFile(odFileDTO);
          //End save file old
          GnocFileDto gnocFileDto = new GnocFileDto();
          gnocFileDto.setPath(fullPath);
          gnocFileDto.setFileName(listFileName.get(i));
          gnocFileDto.setCreateUnitId(usersInsideDto.getUnitId());
          gnocFileDto.setCreateUnitName(unitToken.getUnitName());
          gnocFileDto.setCreateUserId(usersInsideDto.getUserId());
          gnocFileDto.setCreateUserName(usersInsideDto.getUsername());
          gnocFileDto.setCreateTime(new Date());
          gnocFileDto.setMappingId(resultFileDataOld.getId());
          gnocFileDtos.add(gnocFileDto);
        } catch (IOException e) {
          log.error(e.getMessage(), e);
        }
      }
      gnocFileRepository
          .saveListGnocFileNotDeleteAll(GNOC_FILE_BUSSINESS.OD, od.getOdId(), gnocFileDtos);
    }
  }
  public static String getLang(Locale locale, String key) {
    if (locale != null) {
      return I18n.getLanguageByLocale(locale, key);
    } else {
      return I18n.getLanguageByLocale(new Locale("vi", "VN"), key);
    }
  }
}
