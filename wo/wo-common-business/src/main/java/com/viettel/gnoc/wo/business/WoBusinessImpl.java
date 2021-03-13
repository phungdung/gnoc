package com.viettel.gnoc.wo.business;

import static com.viettel.gnoc.commons.repository.BaseRepository.getSqlLanguageExchange;
import static com.viettel.gnoc.commons.repository.BaseRepository.setLanguage;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.viettel.cc.webserivce.CompCause;
import com.viettel.eee2ws.webservice.CpRequestParam;
import com.viettel.eee2ws.webservice.Result;
import com.viettel.gnoc.commons.business.CatServiceBusiness;
import com.viettel.gnoc.commons.business.UserBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.config.TimezoneContextHolder;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.commons.dto.IpccServiceDTO;
import com.viettel.gnoc.commons.dto.LanguageExchangeDTO;
import com.viettel.gnoc.commons.dto.MessagesDTO;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.dto.UsersDTO;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.commons.dto.WoSearchWebDTO;
import com.viettel.gnoc.commons.model.UnitEntity;
import com.viettel.gnoc.commons.model.UsersEntity;
import com.viettel.gnoc.commons.proxy.CommonStreamServiceProxy;
import com.viettel.gnoc.commons.proxy.CrServiceProxy;
import com.viettel.gnoc.commons.proxy.MrCategoryProxy;
import com.viettel.gnoc.commons.proxy.TtServiceProxy;
import com.viettel.gnoc.commons.proxy.WoCategoryServiceProxy;
import com.viettel.gnoc.commons.repository.CatItemRepository;
import com.viettel.gnoc.commons.repository.CatLocationRepository;
import com.viettel.gnoc.commons.repository.CatServiceRepository;
import com.viettel.gnoc.commons.repository.CommonRepository;
import com.viettel.gnoc.commons.repository.GnocFileRepository;
import com.viettel.gnoc.commons.repository.LanguageExchangeRepository;
import com.viettel.gnoc.commons.repository.MessagesRepository;
import com.viettel.gnoc.commons.repository.SmsGatewayRepository;
import com.viettel.gnoc.commons.repository.UnitRepository;
import com.viettel.gnoc.commons.repository.UserRepository;
import com.viettel.gnoc.commons.utils.CellConfigExport;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.commons.utils.ConditionBeanUtil;
import com.viettel.gnoc.commons.utils.ConfigFileExport;
import com.viettel.gnoc.commons.utils.ConfigHeaderExport;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.APPLIED_BUSSINESS;
import com.viettel.gnoc.commons.utils.Constants.AP_PARAM;
import com.viettel.gnoc.commons.utils.Constants.BUSINESS_UNIT_NAME;
import com.viettel.gnoc.commons.utils.Constants.GNOC_FILE_BUSSINESS;
import com.viettel.gnoc.commons.utils.Constants.INSERT_SOURCE;
import com.viettel.gnoc.commons.utils.Constants.LANGUAGUE_EXCHANGE_SYSTEM;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.Constants.WO_MASTER_CODE;
import com.viettel.gnoc.commons.utils.Constants.WO_RESULT;
import com.viettel.gnoc.commons.utils.Constants.WO_STATUS;
import com.viettel.gnoc.commons.utils.Constants.WO_SYSTEM_CODE;
import com.viettel.gnoc.commons.utils.Constants.WO_TYPE_SEARCH;
import com.viettel.gnoc.commons.utils.Constants.WS_RESULT;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.DateUtil;
import com.viettel.gnoc.commons.utils.ErrorUtils;
import com.viettel.gnoc.commons.utils.ExcelWriterUtils;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.CrCreatedFromOtherSysDTO.SYSTEM;
import com.viettel.gnoc.cr.dto.CrDTO;
import com.viettel.gnoc.cr.dto.CrFilesAttachInsiteDTO;
import com.viettel.gnoc.cr.dto.CrImpactedNodesDTO;
import com.viettel.gnoc.cr.dto.CrInsiteDTO;
import com.viettel.gnoc.cr.dto.CrSearchDTO;
import com.viettel.gnoc.incident.dto.TroublesDTO;
import com.viettel.gnoc.incident.dto.WoSupportDTO;
import com.viettel.gnoc.wfm.dto.LogCallIpccDTO;
import com.viettel.gnoc.wfm.dto.WoTypeDTO;
import com.viettel.gnoc.wo.dto.AutoCreateWoOsDTO;
import com.viettel.gnoc.wo.dto.CatLocationDTO;
import com.viettel.gnoc.wo.dto.ExcelSheetDTO;
import com.viettel.gnoc.wo.dto.FieldForm;
import com.viettel.gnoc.wo.dto.FileNameDTO;
import com.viettel.gnoc.wo.dto.HeaderForm;
import com.viettel.gnoc.wo.dto.KpiCompleteVsamrtForm;
import com.viettel.gnoc.wo.dto.KpiCompleteVsmartResult;
import com.viettel.gnoc.wo.dto.MaterialCodienDTO;
import com.viettel.gnoc.wo.dto.MaterialThresDTO;
import com.viettel.gnoc.wo.dto.NocProForm;
import com.viettel.gnoc.wo.dto.ObjFile;
import com.viettel.gnoc.wo.dto.ObjKeyValue;
import com.viettel.gnoc.wo.dto.ObjResponse;
import com.viettel.gnoc.wo.dto.SearchWoKpiCDBRForm;
import com.viettel.gnoc.wo.dto.SupportCaseForm;
import com.viettel.gnoc.wo.dto.SupportCaseTestForm;
import com.viettel.gnoc.wo.dto.VsmartUpdateForm;
import com.viettel.gnoc.wo.dto.WoAutoCheckDTO;
import com.viettel.gnoc.wo.dto.WoCdGroupInsideDTO;
import com.viettel.gnoc.wo.dto.WoChecklistDTO;
import com.viettel.gnoc.wo.dto.WoConfigPropertyDTO;
import com.viettel.gnoc.wo.dto.WoDTO;
import com.viettel.gnoc.wo.dto.WoDTOSearch;
import com.viettel.gnoc.wo.dto.WoDeclareServiceDTO;
import com.viettel.gnoc.wo.dto.WoDetailDTO;
import com.viettel.gnoc.wo.dto.WoHistoryDTO;
import com.viettel.gnoc.wo.dto.WoHistoryInsideDTO;
import com.viettel.gnoc.wo.dto.WoInsideDTO;
import com.viettel.gnoc.wo.dto.WoKTTSInfoDTO;
import com.viettel.gnoc.wo.dto.WoMaterialDeducteDTO;
import com.viettel.gnoc.wo.dto.WoMaterialDeducteInsideDTO;
import com.viettel.gnoc.wo.dto.WoMerchandiseDTO;
import com.viettel.gnoc.wo.dto.WoMerchandiseInsideDTO;
import com.viettel.gnoc.wo.dto.WoMopInfoDTO;
import com.viettel.gnoc.wo.dto.WoPendingDTO;
import com.viettel.gnoc.wo.dto.WoPriorityDTO;
import com.viettel.gnoc.wo.dto.WoTestServiceMapDTO;
import com.viettel.gnoc.wo.dto.WoTroubleInfoDTO;
import com.viettel.gnoc.wo.dto.WoTypeGroupDTO;
import com.viettel.gnoc.wo.dto.WoTypeInsideDTO;
import com.viettel.gnoc.wo.dto.WoTypeServiceInsideDTO;
import com.viettel.gnoc.wo.dto.WoTypeTimeDTO;
import com.viettel.gnoc.wo.dto.WoUpdateStatusForm;
import com.viettel.gnoc.wo.dto.WoWorklogDTO;
import com.viettel.gnoc.wo.dto.WoWorklogInsideDTO;
import com.viettel.gnoc.wo.model.WoMopInfoEntity;
import com.viettel.gnoc.wo.model.WoPendingEntity;
import com.viettel.gnoc.wo.repository.LogCallIpccRepository;
import com.viettel.gnoc.wo.repository.WoAutoCheckRepository;
import com.viettel.gnoc.wo.repository.WoCdGroupRepository;
import com.viettel.gnoc.wo.repository.WoCdTempRepository;
import com.viettel.gnoc.wo.repository.WoDeclareServiceRepository;
import com.viettel.gnoc.wo.repository.WoDetailRepository;
import com.viettel.gnoc.wo.repository.WoHistoryRepository;
import com.viettel.gnoc.wo.repository.WoKTTSInfoRepository;
import com.viettel.gnoc.wo.repository.WoMerchandiseRepository;
import com.viettel.gnoc.wo.repository.WoMopInfoRepository;
import com.viettel.gnoc.wo.repository.WoPendingRepository;
import com.viettel.gnoc.wo.repository.WoRepository;
import com.viettel.gnoc.wo.repository.WoSupportRepository;
import com.viettel.gnoc.wo.repository.WoTroubleInfoRepository;
import com.viettel.gnoc.wo.repository.WoTypeGroupRepository;
import com.viettel.gnoc.wo.repository.WoTypeServiceRepository;
import com.viettel.gnoc.wo.repository.WoWorklogRepository;
import com.viettel.gnoc.wo.utils.BCCS_CC_STLPort;
import com.viettel.gnoc.wo.utils.IPCCMVTPort;
import com.viettel.gnoc.wo.utils.IPCCPort;
import com.viettel.gnoc.wo.utils.IPCCSTLPort;
import com.viettel.gnoc.wo.utils.IPCCVTPPort;
import com.viettel.gnoc.wo.utils.IPCCVTZPort;
import com.viettel.gnoc.wo.utils.KTTSVsmartPort;
import com.viettel.gnoc.wo.utils.NimsStationForm;
import com.viettel.gnoc.wo.utils.NocProPort;
import com.viettel.gnoc.wo.utils.PaymentPort;
import com.viettel.gnoc.wo.utils.WSCC2Port;
import com.viettel.gnoc.wo.utils.WSNIMS_CD_BCCS2_Port;
import com.viettel.gnoc.wo.utils.WSNIMS_HTPort;
import com.viettel.gnoc.wo.utils.WSQLCTKT_CDPort;
import com.viettel.gnoc.wo.utils.WS_AMIONE_Port;
import com.viettel.gnoc.wo.utils.WS_CC_Direction;
import com.viettel.gnoc.wo.utils.WS_CHI_PHI_Port;
import com.viettel.gnoc.wo.utils.WS_CO_DIEN_Port;
import com.viettel.gnoc.wo.utils.WS_HOAN_CONG_Port;
import com.viettel.gnoc.wo.utils.WS_IMES_Port;
import com.viettel.gnoc.wo.utils.WS_NIMS_CD_Direction;
import com.viettel.gnoc.wo.utils.WS_QLCTKT_BCCS_Port;
import com.viettel.gnoc.wo.utils.WS_SAP_Port;
import com.viettel.ipcc.services.AutoCallOutInput;
import com.viettel.ipcc.services.NomalOutput;
import com.viettel.ipcc.unitel.services.Input;
import com.viettel.ipcc.unitel.services.Output;
import com.viettel.nims.webservice.cd.bccs2.SubscriptionInfoForm;
import com.viettel.nims.webservice.ht.CableInfoForm;
import com.viettel.nims.webservice.ht.CheckPortSubDescriptionByWOForm;
import com.viettel.nims.webservice.ht.ResultCheckStatusCabinet;
import com.viettel.nims.webservice.ht.ResultCheckStatusStations;
import com.viettel.nims.webservice.ht.ResultGetDepartmentByLocationForm;
import com.viettel.nocproV4.JsonResponseBO;
import com.viettel.nocproV4.ParameterBO;
import com.viettel.nocproV4.RequestInputBO;
import com.viettel.payment.services.StaffBean;
import com.viettel.qlctkt_cd.webservice.MessageForm;
import com.viettel.qlctkt_cd.webservice.Staff;
import com.viettel.qlctkt_cd.webservice.SysUsersBO;
import com.viettel.qldtktts.service2.CatStationBO;
import com.viettel.qldtktts.service2.CatWarehouseBO;
import com.viettel.qldtktts.service2.CertificateRegistrationBO;
import com.viettel.qldtktts.service2.CntContractBO;
import com.viettel.qldtktts.service2.ConstrConstructionsBO;
import com.viettel.qldtktts.service2.EntityBO;
import com.viettel.qldtktts.service2.ErrorBO;
import com.viettel.qldtktts.service2.Kttsbo;
import com.viettel.qldtktts.service2.MerEntityBO;
import com.viettel.qldtktts.service2.MerchandiseBO;
import com.viettel.qldtktts.service2.MerchandiseOrderBO;
import com.viettel.qos.service.QoSService;
import com.viettel.qos.service.QoSService_Service;
import com.viettel.security.PassTranformer;
import com.viettel.soc.spm.service.ServiceProblemInfoDTO;
import com.viettel.vmsa.MopGnoc;
import com.viettel.vmsa.MopGnocDTO;
import com.viettel.webservice.cc_stl.Param;
import com.viettel.webservice.imes.ResultQoS;
import com.viettel.webservice.qlctkt.bccs.ResultService;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.map.HashedMap;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataValidationConstraint.ValidationType;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;
import org.apache.poi.xssf.usermodel.XSSFDataValidationConstraint;
import org.apache.poi.xssf.usermodel.XSSFDataValidationHelper;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;
import viettel.passport.client.UserToken;

@Service
@Transactional
@Slf4j
public class WoBusinessImpl implements WoBusiness {

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

  @Value("${application.SmsGatewayId:5}")
  private String smsGatewayId;

  @Value("${application.SenderId:400}")
  private String senderId;

  @Value("${application.extensionAllow:null}")
  private String extension;

  @Value("${application.ws.user.Cc.STL:null}")
  private String userCcSTL;

  @Value("${application.ws.pass.Cc.STL:null}")
  private String passCcSTL;

  @Value("${application.record_ft_emergency:null}")
  private String RECORD_FT_EMERGENCY;

  @Value("${application.record_cd_emergency:null}")
  private String RECORD_CD_EMERGENCY;

  @Value("${application.urlForIpcc:null}")
  private String URLFORIPCC;

  @Value("${application.record_ft:null}")
  private String recordFt;

  @Value("${application.record_cd:null}")
  private String recordCd;

  @Value("${application.ws.qos.url:null}")
  private String qosUrl;

  @Value("${application.ws.qos.targetNamespace:null}")
  private String qosTargetNamespace;

  @Value("${application.ws.qos.name:null}")
  private String qosName;

  @Autowired
  WoRepository woRepository;

  @Autowired
  UserRepository userRepository;

  @Autowired
  WoHistoryRepository woHistoryRepository;

  @Autowired
  WoDetailRepository woDetailRepository;

  @Autowired
  WoMerchandiseRepository woMerchandiseRepository;

  @Autowired
  WoKTTSInfoRepository woKTTSInfoRepository;

  @Autowired
  WoWorklogRepository woWorklogRepository;

  @Autowired
  WoCategoryServiceProxy woCategoryServiceProxy;

  @Autowired
  TicketProvider ticketProvider;

  @Autowired
  KTTSVsmartPort kttsVsmartPort;

  @Autowired
  UnitRepository unitRepository;

  @Autowired
  CommonRepository commonRepository;

  @Autowired
  CrServiceProxy crServiceProxy;

  @Autowired
  CommonStreamServiceProxy commonStreamServiceProxy;

  @Autowired
  TtServiceProxy ttServiceProxy;

  @Autowired
  CatItemRepository catItemRepository;

  @Autowired
  MessagesRepository messagesRepository;

  @Autowired
  WoMaterialDeducteBusiness woMaterialDeducteBusiness;

  @Autowired
  WoPendingRepository woPendingRepository;

  @Autowired
  WoAutoCheckRepository woAutoCheckRepository;

  @Autowired
  NocProPort nocPort;

  @Autowired
  WS_CHI_PHI_Port portChiPhi;

  @Autowired
  WoVSmartBusiness vsmartBusiness;

  @Autowired
  WSNIMS_CD_BCCS2_Port wsNims;

  @Autowired
  WS_NIMS_CD_Direction ws_nims_cd_direction;

  @Autowired
  WSQLCTKT_CDPort cdPort;

  @Autowired
  WS_QLCTKT_BCCS_Port qltPort;

  @Autowired
  WSNIMS_HTPort wsHTNims;

  @Autowired
  WoCdTempRepository woCdTempRepository;

  @Autowired
  CatServiceRepository catServiceRepository;

  @Autowired
  WoDeclareServiceRepository woDeclareServiceRepository;

  @Autowired
  WoTroubleInfoRepository woTroubleInfoRepository;

  @Autowired
  PaymentPort paymentPort;

  @Autowired
  BCCS_CC_STLPort ccStlPort;

  @Autowired
  WS_CO_DIEN_Port coDienPort;

  @Autowired
  WS_HOAN_CONG_Port hoanCongPort;

  @Autowired
  UserBusiness userBusiness;

  @Autowired
  CatServiceBusiness catServiceBusiness;

  @Autowired
  SmsGatewayRepository smsGatewayRepository;

  @Autowired
  LogCallIpccRepository logCallIpccRepository;

  @Autowired
  IPCCMVTPort ipccmvtPort;

  @Autowired
  IPCCVTZPort ipccvtzPort;

  @Autowired
  IPCCSTLPort ipccstlPort;

  @Autowired
  IPCCVTPPort ipccvtpPort;

  @Autowired
  IPCCPort ipccPort;

  @Autowired
  CatLocationRepository catLocationRepository;

  @Autowired
  WoSPMBusiness woSPMBusiness;

  @Autowired
  WoTicketBusiness woTicketBusiness;

  @Autowired
  WSCC2Port wscc2Port;

  @Autowired
  LanguageExchangeRepository languageExchangeRepository;

  @Autowired
  WoMopInfoRepository woMopInfoRepository;

  @Autowired
  WoTypeServiceRepository woTypeServiceRepository;

  @Autowired
  MaterialCodienBusiness materialCodienBusiness;

  @Autowired
  WoCdGroupRepository woCdGroupRepository;

  @Autowired
  WoSupportRepository woSupportRepository;

  @Autowired
  WoCdGroupBusiness woCdGroupBusiness;

  @Autowired
  WoTypeBusiness woTypeBusiness;

  @Autowired
  GnocFileRepository gnocFileRepository;

  @Autowired
  WoTypeGroupRepository woTypeGroupRepository;

  @Autowired
  NocProPort nocProPort;

  @Autowired
  WS_SAP_Port ws_sap_port;

  @Autowired
  WoCdBusiness woCdBusiness;

  @Autowired
  WS_AMIONE_Port ws_amione_port;

  @Autowired
  WS_IMES_Port ws_imes_port;

  @Autowired
  WS_CC_Direction ws_cc_direction;

  @Autowired
  MrCategoryProxy mrCategoryProxy;

  private final static String WO_SEQ = "WO_SEQ";
  private int maxRecord = 500;
  public final static String REGEX_NUMBER = "^[+]?[0-9]+([.][0-9][0-9]*)?$";
  Map<String, String> mapResultName = new HashMap<>();
  Map<Integer, String> mapTypeSearch = new HashMap<>();

  @Override
  public Datatable getListDataSearchWeb(WoInsideDTO woInsideDTO) {
    log.debug("Request to getListDataSearchWeb: {}", woInsideDTO);
    Double offsetFromUser;
    if (woInsideDTO.getOffset() != null) {
      offsetFromUser = woInsideDTO.getOffset().doubleValue();
    } else {
      UserToken userToken = ticketProvider.getUserToken();
      offsetFromUser = userRepository.getOffsetFromUser(userToken.getUserID());
    }
    WoInsideDTO searchDto = converTimeFromClientToServerWeb(woInsideDTO, offsetFromUser);
    searchDto.setOffSetFromUser(offsetFromUser);
    Datatable datatable = woRepository.getListDataSearchWeb(searchDto);
    List<WoInsideDTO> listWoInsideDTO = (List<WoInsideDTO>) datatable.getData();
    for (WoInsideDTO dto : listWoInsideDTO) {
      List<UsersInsideDto> listCd = woRepository.getUserOfCD(dto.getCdId());
      dto.setListCd(listCd);
    }
    datatable.setData(listWoInsideDTO);
    return datatable;
  }

  @Override
  public WoInsideDTO findWoByIdFromWeb(Long woId) {
    log.debug("Request to findWoById: {}", woId);
    UserToken userToken = ticketProvider.getUserToken();
    Double offsetFromUser = userRepository.getOffsetFromUser(userToken.getUserID());
    WoInsideDTO woInsideDTO = woRepository.findWoById(woId, offsetFromUser);
    List<UsersInsideDto> listCd = woRepository.getUserOfCD(woInsideDTO.getCdId());
    woInsideDTO.setListCd(listCd);
    return woInsideDTO;
  }

  @Override
  public ResultInSideDto insertWoFromWeb(List<MultipartFile> fileAttacks,
      List<MultipartFile> fileCfgAttacks, WoInsideDTO woInsideDTO) throws Exception {
    log.debug("Request to insertWoFromWeb: {}", woInsideDTO);
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    UserToken userToken = ticketProvider.getUserToken();
    UnitDTO unitToken = unitRepository.findUnitById(userToken.getDeptId());
    Double offsetFromUser = userRepository.getOffsetFromUser(userToken.getUserID());
    Map<String, String> mapConfigProperty = commonRepository.getConfigProperty();
    String nation = getNationFromUnit(userToken.getDeptId());
    Date date = new Date();
    woInsideDTO = convertWoDate2VietNamDate(woInsideDTO, offsetFromUser);
    for (String cd : woInsideDTO.getCdIdList()) {
      Long woId = Long.valueOf(woRepository.getSeqTableWo(WO_SEQ));
      woInsideDTO.setWoId(woId);
      String woCode =
          "WO_" + woInsideDTO.getWoSystem() + "_" + DateUtil.date2StringNoSlash(date) + "_" + woId;
      woInsideDTO.setWoCode(woCode);
      woInsideDTO.setCdId(Long.valueOf(cd));
      woInsideDTO.setCreatePersonId(userToken.getUserID());
      woInsideDTO.setCreateDate(date);
      woInsideDTO.setLastUpdateTime(date);
      // File attachment
      List<GnocFileDto> gnocFileDtos = new ArrayList<>();
      List<String> lstFileName = new ArrayList<>();
      for (MultipartFile multipartFile : fileAttacks) {
        String fullPath = FileUtils
            .saveFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
                PassTranformer.decrypt(ftpPass), ftpFolder, multipartFile.getOriginalFilename(),
                multipartFile.getBytes(), FileUtils.createDateOfFileName(date));
        String fileName = multipartFile.getOriginalFilename();
        //Start save file old
        lstFileName.add(FileUtils.getFileName(fullPath));
        //End save file old
        GnocFileDto gnocFileDto = new GnocFileDto();
        gnocFileDto.setPath(fullPath);
        gnocFileDto.setFileName(FileUtils.getFileName(fullPath));
        gnocFileDto.setCreateUnitId(userToken.getDeptId());
        gnocFileDto.setCreateUnitName(unitToken.getUnitName());
        gnocFileDto.setCreateUserId(userToken.getUserID());
        gnocFileDto.setCreateUserName(userToken.getUserName());
        gnocFileDto.setCreateTime(date);
        gnocFileDto.setMappingId(woId);
        gnocFileDtos.add(gnocFileDto);
      }

      //TienNV bo sung FileDT Test
      String lstDtCode = "";
      if (woInsideDTO.getCrFilesAttachInsiteDTO() != null) {
        CrFilesAttachInsiteDTO crFilesAttachInsiteDTO = woInsideDTO.getCrFilesAttachInsiteDTO();
        //them moi xu ly voi cac file loadFileDT
        if (crFilesAttachInsiteDTO.getLstCustomerFile() != null
            && crFilesAttachInsiteDTO.getLstCustomerFile().size() > 0) {
          for (CrFilesAttachInsiteDTO crFilesAttachInsiteDTODT : crFilesAttachInsiteDTO
              .getLstCustomerFile()) {
            lstDtCode += "," + crFilesAttachInsiteDTODT.getDtCode();
            byte[] bytes = FileUtils
                .getFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
                    PassTranformer.decrypt(ftpPass), crFilesAttachInsiteDTODT.getFilePath());
            String fullPath = FileUtils
                .saveFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
                    PassTranformer.decrypt(ftpPass), ftpFolder,
                    crFilesAttachInsiteDTODT.getFileName(),
                    bytes, null);
            //Start save file old
            lstFileName.add(FileUtils.getFileName(fullPath));
            //End save file old
            GnocFileDto gnocFileDto = new GnocFileDto();
            gnocFileDto.setPath(fullPath);
            gnocFileDto.setFileName(FileUtils.getFileName(fullPath));
            gnocFileDto.setCreateUnitId(userToken.getDeptId());
            gnocFileDto.setCreateUnitName(unitToken.getUnitName());
            gnocFileDto.setCreateUserId(userToken.getUserID());
            gnocFileDto.setCreateUserName(userToken.getUserName());
            gnocFileDto.setCreateTime(date);
            gnocFileDto.setMappingId(woId);
            gnocFileDtos.add(gnocFileDto);
          }
          if (StringUtils.isNotNullOrEmpty(lstDtCode)) {
            lstDtCode = lstDtCode.substring(1);
            woInsideDTO.setWoSystemOutId(lstDtCode);
          }
        }
      }

      // File wo type
      if (woInsideDTO.getGnocFileCreateWoDtos() != null && !woInsideDTO.getGnocFileCreateWoDtos()
          .isEmpty()) {
        GnocFileDto gnocFileCreateDtoOld = new GnocFileDto();
        gnocFileCreateDtoOld.setBusinessCode(GNOC_FILE_BUSSINESS.CFG_FILE_CREATE_WO);
        gnocFileCreateDtoOld.setBusinessId(woInsideDTO.getWoTypeId());
        List<GnocFileDto> gnocFileCreateDtosOld = gnocFileRepository
            .getListGnocFileByDto(gnocFileCreateDtoOld);
        Map<Long, String> mapCfgFileCreateWoOld = new HashMap<>();
        for (GnocFileDto gnocFileDto : gnocFileCreateDtosOld) {
          mapCfgFileCreateWoOld.put(gnocFileDto.getMappingId(), gnocFileDto.getPath());
        }
        for (GnocFileDto gnocFileDto : woInsideDTO.getGnocFileCreateWoDtos()) {
          if (gnocFileDto.getIndexFile() != null) {
            gnocFileDto.setMultipartFile(fileCfgAttacks.get(gnocFileDto.getIndexFile().intValue()));
          }
        }
        for (GnocFileDto gnocFileCreateWoDto : woInsideDTO.getGnocFileCreateWoDtos()) {
          if (gnocFileCreateWoDto.getMultipartFile() != null) {
            String filePathOld = mapCfgFileCreateWoOld.get(gnocFileCreateWoDto.getMappingId());
            MultipartFile multipartFile = gnocFileCreateWoDto.getMultipartFile();
            String filePathNew = FileUtils
                .saveTempFile(multipartFile.getOriginalFilename(), multipartFile.getBytes(),
                    tempFolder);
            if (StringUtils.isNotNullOrEmpty(filePathOld)) {
              byte[] bytes = FileUtils.getFtpFile(ftpServer, ftpPort,
                  PassTranformer.decrypt(ftpUser), PassTranformer.decrypt(ftpPass), filePathOld);
              String filePathNewTemp = FileUtils
                  .saveTempFile(FileUtils.getFileName(filePathOld), bytes,
                      tempFolder);
              if (!ExcelWriterUtils.compareHeader(filePathNewTemp, filePathNew)) {
                resultInSideDto.setKey(RESULT.FILE_INVALID_FORMAT);
                resultInSideDto.setMessage(FileUtils.getFileName(filePathNew));
                return resultInSideDto;
              }
              String fullPath = FileUtils
                  .saveFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
                      PassTranformer.decrypt(ftpPass), ftpFolder,
                      multipartFile.getOriginalFilename(),
                      multipartFile.getBytes(), date);
              String fileName = multipartFile.getOriginalFilename();
              //Start save file old
              lstFileName.add(FileUtils.getFileName(fullPath));
              //End save file old
              GnocFileDto gnocFileDto = new GnocFileDto();
              gnocFileDto.setPath(fullPath);
              gnocFileDto.setFileName(FileUtils.getFileName(fullPath));
              gnocFileDto.setCreateUnitId(userToken.getDeptId());
              gnocFileDto.setCreateUnitName(unitToken.getUnitName());
              gnocFileDto.setCreateUserId(userToken.getUserID());
              gnocFileDto.setCreateUserName(userToken.getUserName());
              gnocFileDto.setCreateTime(date);
              gnocFileDto.setMappingId(woId);
              gnocFileDto.setFileType("1");
              gnocFileDtos.add(gnocFileDto);
            }
          }
        }
      }
      GnocFileDto gnocFileGuideDtoOld = new GnocFileDto();
      gnocFileGuideDtoOld.setBusinessCode(GNOC_FILE_BUSSINESS.WO_TYPE_FILES_GUIDE);
      gnocFileGuideDtoOld.setBusinessId(woInsideDTO.getWoTypeId());
      List<GnocFileDto> gnocFileGuideDtosOld = gnocFileRepository
          .getListGnocFileByDto(gnocFileGuideDtoOld);
      for (GnocFileDto gnocFileGuideDto : gnocFileGuideDtosOld) {
        if (StringUtils.isNotNullOrEmpty(gnocFileGuideDto.getPath())) {
          byte[] bytes = FileUtils.getFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
              PassTranformer.decrypt(ftpPass), gnocFileGuideDto.getPath());
          String fullPath = FileUtils
              .saveFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
                  PassTranformer.decrypt(ftpPass), ftpFolder, gnocFileGuideDto.getFileName(), bytes,
                  date);
          String fileName = gnocFileGuideDto.getFileName();
          //Start save file old
          lstFileName.add(FileUtils.getFileName(fullPath));
          //End save file old
          GnocFileDto gnocFileDto = new GnocFileDto();
          gnocFileDto.setPath(fullPath);
          gnocFileDto.setFileName(FileUtils.getFileName(fullPath));
          gnocFileDto.setCreateUnitId(userToken.getDeptId());
          gnocFileDto.setCreateUnitName(unitToken.getUnitName());
          gnocFileDto.setCreateUserId(userToken.getUserID());
          gnocFileDto.setCreateUserName(userToken.getUserName());
          gnocFileDto.setCreateTime(date);
          gnocFileDto.setMappingId(woId);
          gnocFileDtos.add(gnocFileDto);
        }
      }
      try {
        if (GNOC_FILE_BUSSINESS.SR.equals(woInsideDTO.getOtherSystemType())
            && woInsideDTO.getOtherSystemId() != null) {
          GnocFileDto gnocFileSrDto = new GnocFileDto();
          gnocFileSrDto.setBusinessCode(GNOC_FILE_BUSSINESS.SR);
          gnocFileSrDto.setBusinessId(woInsideDTO.getOtherSystemId());
          List<GnocFileDto> gnocFileSrDtos = gnocFileRepository.getListGnocFileByDto(gnocFileSrDto);
          for (GnocFileDto gnocFileSrDtoAdd : gnocFileSrDtos) {
            if (StringUtils.isNotNullOrEmpty(gnocFileSrDtoAdd.getPath())) {
              String fileName = gnocFileSrDtoAdd.getFileName();
              byte[] bytes = FileUtils
                  .getFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
                      PassTranformer.decrypt(ftpPass), gnocFileSrDtoAdd.getPath());
              String fullPath = FileUtils
                  .saveFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
                      PassTranformer.decrypt(ftpPass), ftpFolder, fileName, bytes,
                      FileUtils.createDateOfFileName(date));
              //Start save file old
              lstFileName.add(FileUtils.getFileName(fullPath));
              //End save file old
              GnocFileDto gnocFileDto = new GnocFileDto();
              gnocFileDto.setPath(fullPath);
              gnocFileDto.setFileName(FileUtils.getFileName(fullPath));
              gnocFileDto.setCreateUnitId(userToken.getDeptId());
              gnocFileDto.setCreateUnitName(unitToken.getUnitName());
              gnocFileDto.setCreateUserId(userToken.getUserID());
              gnocFileDto.setCreateUserName(userToken.getUserName());
              gnocFileDto.setCreateTime(date);
              gnocFileDto.setMappingId(woId);
              gnocFileDtos.add(gnocFileDto);
            }
          }
        }
      } catch (Exception ex) {
        resultInSideDto.setKey(RESULT.ERROR);
        resultInSideDto.setMessage(I18n.getLanguage("sr.notAttachFile"));
        return resultInSideDto;
      }
      gnocFileRepository.saveListGnocFileNotDeleteAll(GNOC_FILE_BUSSINESS.WO, woId, gnocFileDtos);
      woInsideDTO.setFileName(String.join(",", lstFileName));
      woInsideDTO.setSyncStatus(null);
      //xu ly loai cong viec la tai san
      List<WoMerchandiseInsideDTO> lstMer = new ArrayList<>();
      if (fileCfgAttacks != null && !fileCfgAttacks.isEmpty() &&
          checkProperty(mapConfigProperty, String.valueOf(woInsideDTO.getWoTypeId()),
              Constants.CONFIG_PROPERTY.WO_TYPE_QLTS)) {
//        if (!checkProperty(mapConfigProperty, String.valueOf(woInsideDTO.getWoTypeId()),
//            Constants.CONFIG_PROPERTY.WO_TYPE_QLTS_DC_HC)) {
        MultipartFile multipartFile = fileCfgAttacks.get(0);
        try {
          String filePath = FileUtils
              .saveTempFile(multipartFile.getOriginalFilename(), multipartFile.getBytes(),
                  tempFolder);
          File fileImp = new File(filePath);
          List<Object[]> lstHeader = CommonImport.getDataFromExcelFile(fileImp, 0, 2,
              0, 3, 2);
          if (lstHeader.size() == 0) {
            resultInSideDto.setKey(RESULT.FILE_INVALID_FORMAT);
            return resultInSideDto;
          }
          List<Object[]> lstData = CommonImport.getDataFromExcelFile(fileImp, 0, 3,
              1, 3, 2);
          if (lstData != null && !lstData.isEmpty()) {
            String lstMercode = "";
            List<MerchandiseBO> lstMercodeBo = new ArrayList<>();
            String validateImport = "";
            int row = 4;
            for (Object[] obj : lstData) {
              validateImport = validateImport(obj[0], obj[1]);
              MerchandiseBO bo = new MerchandiseBO();
              if (StringUtils.isStringNullOrEmpty(validateImport)) {
                lstMercode = lstMercode + obj[0].toString().trim() + ",";
                WoMerchandiseInsideDTO dtoMer = new WoMerchandiseInsideDTO(null, null,
                    obj[0].toString().trim(),
                    Double.valueOf(obj[1].toString().trim()),
                    obj[2] != null ? obj[2].toString().trim() : null, null);
                bo.setMerCode(obj[0].toString().trim());
                bo.setCount(Double.valueOf(obj[1].toString().trim()));
                lstMer.add(dtoMer);
                lstMercodeBo.add(bo);
                row++;
              } else {
                break;
              }
            }
            if (StringUtils.isNotNullOrEmpty(validateImport)) {
              resultInSideDto.setKey(RESULT.ERROR);
              resultInSideDto
                  .setMessage(
                      I18n.getLanguage("wo.errImportKtts") + " row" + row + validateImport);
              return resultInSideDto;
            } else {
              try {
                Kttsbo resultCheckKtts = null;
                // check file nang cap / nang cap uctt
                if (checkProperty(mapConfigProperty, String.valueOf(woInsideDTO.getWoTypeId()),
                    Constants.CONFIG_PROPERTY.WO_TYPE_QLTS_NC)
                    || checkProperty(mapConfigProperty, String.valueOf(woInsideDTO.getWoTypeId()),
                    Constants.CONFIG_PROPERTY.WO_TYPE_QLTS_NC_THUE)
                    || checkProperty(mapConfigProperty, String.valueOf(woInsideDTO.getWoTypeId()),
                    Constants.CONFIG_PROPERTY.WO_TYPE_QLTS_NC_UCTT)
                    || checkProperty(mapConfigProperty, String.valueOf(woInsideDTO.getWoTypeId()),
                    Constants.CONFIG_PROPERTY.WO_TYPE_QLTS_NC_UCTT_THUE)) {
                  resultCheckKtts = kttsVsmartPort.checkMerchandiseCodeNation(lstMercode, nation);
                  // check file thu hoi
                } else if (checkProperty(mapConfigProperty,
                    String.valueOf(woInsideDTO.getWoTypeId()),
                    Constants.CONFIG_PROPERTY.WO_TYPE_QLTS_THUHOI)) {
                  resultCheckKtts = kttsVsmartPort
                      .checkListMerEntityNation(lstMercodeBo, woInsideDTO.getStationCode(),
                          nation);
                }
                if (resultCheckKtts != null && !"Success"
                    .equalsIgnoreCase(resultCheckKtts.getStatus())) {
                  resultInSideDto.setKey(RESULT.ERROR);
                  resultInSideDto.setMessage(resultCheckKtts.getErrorInfo());
                  return resultInSideDto;
                }
              } catch (Exception e) {
                log.error(e.getMessage(), e);
                resultInSideDto.setKey(RESULT.ERROR);
                resultInSideDto
                    .setMessage(
                        "KTTS:" + I18n.getLanguage("wo.haveSomeErrWhenCheckListMerchandise"));
                return resultInSideDto;
              }
            }
          } else {
            resultInSideDto.setKey(RESULT.ERROR);
            resultInSideDto.setMessage(I18n.getLanguage("wo.fileKttsIsEmpty"));
            return resultInSideDto;
          }
        } catch (Exception ex) {
          log.error(ex.getMessage(), ex);
          resultInSideDto.setKey(RESULT.ERROR);
          resultInSideDto.setMessage(ex.getMessage());
          return resultInSideDto;
        }
//        }
      }

      resultInSideDto = woRepository.insertWo(woInsideDTO);
      if (RESULT.SUCCESS.equals(resultInSideDto.getKey())) {

        // Luu lich su
        WoHistoryInsideDTO woHistoryInsideDTO = new WoHistoryInsideDTO();
        woHistoryInsideDTO.setWoCode(woCode);
        woHistoryInsideDTO.setNewStatus(woInsideDTO.getStatus());
        woHistoryInsideDTO.setWoId(woId);
        woHistoryInsideDTO.setWoContent(woInsideDTO.getWoContent());
        woHistoryInsideDTO.setUserName(userToken.getUserName());
        woHistoryInsideDTO.setUpdateTime(date);
        woHistoryInsideDTO.setCreatePersonId(userToken.getUserID());
        woHistoryInsideDTO.setCdId(Long.valueOf(cd));
        woHistoryInsideDTO.setFtId(woInsideDTO.getFtId());
        woHistoryInsideDTO
            .setComments("(" + DateUtil.date2ddMMyyyyHHMMss(woInsideDTO.getStartTime()) + " - " +
                DateUtil.date2ddMMyyyyHHMMss(woInsideDTO.getEndTime()) + ")");
        woHistoryRepository.insertWoHistory(woHistoryInsideDTO);
        // Luu detail
        WoDetailDTO woDetailDTO = woInsideDTO.getWoDetailDTO();
        woDetailDTO.setWoId(woId);
        woDetailDTO.setLastUpdateTime(date);
        woDetailDTO.setCreateDate(date);
        woDetailRepository.insertUpdateWoDetail(woDetailDTO);
        // Luu vat tu
        if (lstMer != null && lstMer.size() > 0) {
          for (WoMerchandiseInsideDTO woMerchandiseInsideDTO : lstMer) {
            woMerchandiseInsideDTO.setWoId(woId);
            woMerchandiseRepository.insertWoMerchandise(woMerchandiseInsideDTO);
          }
        }
        // Luu thong tin KTTS
        WoKTTSInfoDTO woKTTSInfoDTO = woInsideDTO.getWoKTTSInfoDTO();
        if (woKTTSInfoDTO != null) {
          if (woKTTSInfoDTO.getContractId() != null
              || woKTTSInfoDTO.getProcessActionName() != null || woKTTSInfoDTO.getActiveEnvironmentId() != null || woInsideDTO.getActiveEnvironmentId() != null) {
            woKTTSInfoDTO.setWoId(woId);
            if(woInsideDTO.getActiveEnvironmentId() != null){
              woKTTSInfoDTO.setActiveEnvironmentId(woInsideDTO.getActiveEnvironmentId());
            }
            if(woKTTSInfoDTO.getActiveEnvironmentId() != null){
              woKTTSInfoDTO.setActiveEnvironmentId(woKTTSInfoDTO.getActiveEnvironmentId());
            }
            woKTTSInfoRepository.insertWoKTTSInfo(woKTTSInfoDTO);
          }
        }
        // Ghi worklog
        if (StringUtils.isNotNullOrEmpty(woInsideDTO.getWoWorklogContent())) {
          WoWorklogInsideDTO woWorklogInsideDTO = new WoWorklogInsideDTO(woInsideDTO);
          woWorklogInsideDTO.setUsername(userToken.getUserName());
          woWorklogRepository.insertWoWorklog(woWorklogInsideDTO);
        }
//         thangdt bo sung loai Wo = "Xử lý sự cố cáp" thì sinh wo2 ="Củng cố khôi phục cáp"
        if (checkProperty(mapConfigProperty, String.valueOf(woInsideDTO.getWoTypeId()),
            Constants.AP_PARAM.WO_TYPE_XLSCC)) {
          String username = userToken.getUserName();
          List<UsersInsideDto> listUser = userRepository.getListUserDTOByuserName(username);
          UsersInsideDto usersFtDto = listUser.get(0);
//            prepareWoTypeXLSCC(woInsideDTO, usersFtDto);
          prepareWoTypeXLSCCUpdate(woInsideDTO, usersFtDto);
        }
        // Thưc hien luu cau hinh test dich vu
        List<WoTestServiceMapDTO> listWoTestServiceMap = woInsideDTO.getListWoTestServiceMap();
        if (listWoTestServiceMap != null && listWoTestServiceMap.size() > 0) {
          for (WoTestServiceMapDTO woTestServiceMapDTO : listWoTestServiceMap) {
            woTestServiceMapDTO.setWoId(String.valueOf(woId));
            woTestServiceMapDTO.setInsertTime(DateTimeUtils.date2ddMMyyyyHHMMss(date));
            ResultDTO sTest = commonStreamServiceProxy.insertWoTestServiceMap(woTestServiceMapDTO);
            if (!"SUCCESS".equals(sTest.getMessage())) {
              resultInSideDto.setKey(RESULT.ERROR);
              resultInSideDto.setMessage("wo.err.saveWoTestService");
              return resultInSideDto;
            }
          }
        }

        // tiennv bo sung thuc hien cap nhat thong tin sang SAP tu CR
        try {
          if (StringUtils.isNotNullOrEmpty(lstDtCode)) {
            MopGnoc mopGnoc = new MopGnoc();
            boolean check = false;
            if (StringUtils.isNotNullOrEmpty(woInsideDTO.getWoSystemOutId())) {
              String[] mop = woInsideDTO.getWoSystemOutId().split(",");
              for (String mopId : mop) {
                if (StringUtils.isNotNullOrEmpty(mopId)) {
                  MopGnocDTO mopGnocDTO = new MopGnocDTO();
                  mopGnocDTO.setMopId(mopId);
                  mopGnocDTO.setWorkOrderCode(woCode);
                  mopGnoc.getMopGnoc().add(mopGnocDTO);
                  check = true;
                }
              }
            }

            if (check) {
              //callProxy sang ben CR goi ViPA;
              WoInsideDTO woSAP = new WoInsideDTO();
              woSAP.setCrNumber(woInsideDTO.getCrNumber());
              woSAP.setMopGnoc(mopGnoc);
              woSAP.setTypeOperation(1L);
              ResultInSideDto resultSAP = crServiceProxy.updateWorkOrderForWOPRoxy(woSAP);
              if (!RESULT.SUCCESS.equals(resultSAP.getKey())) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                resultInSideDto.setKey(RESULT.ERROR);
                resultInSideDto.setMessage(resultSAP.getMessage());
                return resultInSideDto;
              }
            }
          }


        } catch (Exception e) {
          log.error(e.getMessage(), e);
        }
      }
      resultInSideDto.setId(woId);
//      resultInSideDto.setObject(woInsideDTO);
    }
    return resultInSideDto;
  }

  public String validateImport(Object merchandiseCode, Object quantity) {
    String r = "";
    if (StringUtils.isStringNullOrEmpty(merchandiseCode)) {
      r = " mercode is null";
    }
    if (StringUtils.isStringNullOrEmpty(quantity)) {
      r = r + " quantity is null";
    }
    try {
      Double.parseDouble(String.valueOf(quantity));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      r = r + " quantity is not valid";
    }
    return r;
  }

  @Override
  public ResultInSideDto updateWoFromWeb(List<MultipartFile> fileAttacks,
      List<MultipartFile> fileCfgAttacks, WoInsideDTO woInsideDTO) throws Exception {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    UserToken userToken = ticketProvider.getUserToken();
    UnitDTO unitToken = unitRepository.findUnitById(userToken.getDeptId());
    Double offsetFromUser = userRepository.getOffsetFromUser(userToken.getUserID());
    Date date = new Date();
    Map<String, String> mapConfigProperty = commonRepository.getConfigProperty();
    String nation = getNationFromUnit(userToken.getDeptId());
    // File attachment
    List<GnocFileDto> gnocFileDtos = new ArrayList<>();
    List<String> lstFileName = new ArrayList<>();
    List<String> lstFileNameOld = new ArrayList<>();
    GnocFileDto objectSearch = new GnocFileDto();
    objectSearch.setBusinessCode(GNOC_FILE_BUSSINESS.WO);
    objectSearch.setBusinessId(woInsideDTO.getWoId());
    objectSearch.setFileType("1");
    List<GnocFileDto> lstFileCfg = gnocFileRepository.getListGnocFileByDto(objectSearch);

    if (lstFileCfg != null & !lstFileCfg.isEmpty()) {
      for (GnocFileDto item : lstFileCfg) {
        gnocFileRepository.deleteGnocFileByDto(item);
      }
    }

    String fileNameOld = woInsideDTO.getFileName();
    if (StringUtils.isNotNullOrEmpty(fileNameOld)) {
      String[] fileNames = fileNameOld.split(",");
      for (int i = 0; i < fileNames.length; i++) {
        lstFileNameOld.add(fileNames[i].trim());
        for (String name : woInsideDTO.getFileNamesDelete()) {
          if (fileNames[i].trim().equals(name.trim())) {
            lstFileNameOld.remove(fileNames[i].trim());
          }
        }
        if (lstFileCfg != null & !lstFileCfg.isEmpty()) {
          for (GnocFileDto item : lstFileCfg) {
            if (fileNames[i].trim().equals(item.getFileName().trim())) {
              lstFileNameOld.remove(fileNames[i].trim());
            }
          }
        }
      }
    }
    for (MultipartFile multipartFile : fileAttacks) {
      String fullPath = FileUtils
          .saveFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
              PassTranformer.decrypt(ftpPass), ftpFolder, multipartFile.getOriginalFilename(),
              multipartFile.getBytes(),
              FileUtils.createDateOfFileName(woInsideDTO.getCreateDate()));
      String fileName = multipartFile.getOriginalFilename();
      //Start save file old
      lstFileName.add(FileUtils.getFileName(fullPath));
      //End save file old
      GnocFileDto gnocFileDto = new GnocFileDto();
      gnocFileDto.setPath(fullPath);
      gnocFileDto.setFileName(FileUtils.getFileName(fullPath));
      gnocFileDto.setCreateUnitId(userToken.getDeptId());
      gnocFileDto.setCreateUnitName(unitToken.getUnitName());
      gnocFileDto.setCreateUserId(userToken.getUserID());
      gnocFileDto.setCreateUserName(userToken.getUserName());
      gnocFileDto.setCreateTime(woInsideDTO.getCreateDate());
      gnocFileDto.setMappingId(woInsideDTO.getWoId());
      gnocFileDtos.add(gnocFileDto);
    }
    // File wo type
    if (woInsideDTO.getGnocFileCreateWoDtos() != null && !woInsideDTO.getGnocFileCreateWoDtos()
        .isEmpty()) {
      GnocFileDto gnocFileCreateDtoOld = new GnocFileDto();
      gnocFileCreateDtoOld.setBusinessCode(GNOC_FILE_BUSSINESS.CFG_FILE_CREATE_WO);
      gnocFileCreateDtoOld.setBusinessId(woInsideDTO.getWoTypeId());
      List<GnocFileDto> gnocFileCreateDtosOld = gnocFileRepository
          .getListGnocFileByDto(gnocFileCreateDtoOld);
      Map<Long, String> mapCfgFileCreateWoOld = new HashMap<>();
      for (GnocFileDto gnocFileDto : gnocFileCreateDtosOld) {
        mapCfgFileCreateWoOld.put(gnocFileDto.getMappingId(), gnocFileDto.getPath());
      }
      for (GnocFileDto gnocFileDto : woInsideDTO.getGnocFileCreateWoDtos()) {
        if (gnocFileDto.getIndexFile() != null) {
          gnocFileDto.setMultipartFile(fileCfgAttacks.get(gnocFileDto.getIndexFile().intValue()));
        }
      }
      for (GnocFileDto gnocFileCreateWoDto : woInsideDTO.getGnocFileCreateWoDtos()) {
        if (gnocFileCreateWoDto.getMultipartFile() != null) {
          String filePathOld = mapCfgFileCreateWoOld.get(gnocFileCreateWoDto.getMappingId());
          MultipartFile multipartFile = gnocFileCreateWoDto.getMultipartFile();
          String filePathNew = FileUtils
              .saveTempFile(multipartFile.getOriginalFilename(), multipartFile.getBytes(),
                  tempFolder);
          if (StringUtils.isNotNullOrEmpty(filePathOld)) {
            byte[] bytes = FileUtils.getFtpFile(ftpServer, ftpPort,
                PassTranformer.decrypt(ftpUser), PassTranformer.decrypt(ftpPass), filePathOld);
            String filePathNewTemp = FileUtils
                .saveTempFile(FileUtils.getFileName(filePathOld), bytes,
                    tempFolder);
            if (!ExcelWriterUtils.compareHeader(filePathNewTemp, filePathNew)) {
              resultInSideDto.setKey(RESULT.FILE_INVALID_FORMAT);
              resultInSideDto.setMessage(FileUtils.getFileName(filePathNew));
              return resultInSideDto;
            }
            String fullPath = FileUtils
                .saveFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
                    PassTranformer.decrypt(ftpPass), ftpFolder, multipartFile.getOriginalFilename(),
                    multipartFile.getBytes(), woInsideDTO.getCreateDate());
            String fileName = gnocFileCreateWoDto.getMultipartFile().getOriginalFilename();
            //Start save file old
            lstFileName.add(FileUtils.getFileName(fullPath));
            //End save file old
            GnocFileDto gnocFileDto = new GnocFileDto();
            gnocFileDto.setPath(fullPath);
            gnocFileDto.setFileName(FileUtils.getFileName(fullPath));
            gnocFileDto.setCreateUnitId(userToken.getDeptId());
            gnocFileDto.setCreateUnitName(unitToken.getUnitName());
            gnocFileDto.setCreateUserId(userToken.getUserID());
            gnocFileDto.setCreateUserName(userToken.getUserName());
            gnocFileDto.setCreateTime(woInsideDTO.getCreateDate());
            gnocFileDto.setMappingId(woInsideDTO.getWoId());
            gnocFileDto.setFileType("1");
            gnocFileDtos.add(gnocFileDto);
          }
        }
      }
    }
    GnocFileDto gnocFileGuideDtoOld = new GnocFileDto();
    gnocFileGuideDtoOld.setBusinessCode(GNOC_FILE_BUSSINESS.WO_TYPE_FILES_GUIDE);
    gnocFileGuideDtoOld.setBusinessId(woInsideDTO.getWoTypeId());
    List<GnocFileDto> gnocFileGuideDtosOld = gnocFileRepository
        .getListGnocFileByDto(gnocFileGuideDtoOld);
    for (GnocFileDto gnocFileGuideDto : gnocFileGuideDtosOld) {
      if (StringUtils.isNotNullOrEmpty(gnocFileGuideDto.getPath())) {
        byte[] bytes = FileUtils
            .getFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
                PassTranformer.decrypt(ftpPass), gnocFileGuideDto.getPath());
        String fullPath = FileUtils
            .saveFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
                PassTranformer.decrypt(ftpPass), ftpFolder, gnocFileGuideDto.getFileName(), bytes,
                FileUtils.createDateOfFileName(woInsideDTO.getCreateDate()));
        String fileName = gnocFileGuideDto.getFileName();
        //Start save file old
        lstFileName.add(FileUtils.getFileName(fullPath));
        //End save file old
        GnocFileDto gnocFileDto = new GnocFileDto();
        gnocFileDto.setPath(fullPath);
        gnocFileDto.setFileName(FileUtils.getFileName(fullPath));
        gnocFileDto.setCreateUnitId(userToken.getDeptId());
        gnocFileDto.setCreateUnitName(unitToken.getUnitName());
        gnocFileDto.setCreateUserId(userToken.getUserID());
        gnocFileDto.setCreateUserName(userToken.getUserName());
        gnocFileDto.setCreateTime(woInsideDTO.getCreateDate());
        gnocFileDto.setMappingId(woInsideDTO.getWoId());
        gnocFileDtos.add(gnocFileDto);
      }
    }
    gnocFileRepository
        .saveListGnocFileNotDeleteAll(GNOC_FILE_BUSSINESS.WO, woInsideDTO.getWoId(), gnocFileDtos);
    if (woInsideDTO.getIdDeleteList() != null && !woInsideDTO.getIdDeleteList().isEmpty()) {
      for (Long id : woInsideDTO.getIdDeleteList()) {
        GnocFileDto gnocFileDto = new GnocFileDto();
        gnocFileDto.setId(id);
        gnocFileRepository.deleteGnocFileByDto(gnocFileDto);
      }
    }
    fileNameOld = lstFileNameOld.size() > 0 ? String.join(",", lstFileNameOld) : "";
    String fileNameNew = lstFileName.size() > 0 ? String.join(",", lstFileName) : "";
    boolean checkFileName = StringUtils.isNotNullOrEmpty(fileNameOld) && StringUtils
        .isNotNullOrEmpty(fileNameNew);
    woInsideDTO.setFileName(
        (StringUtils.isStringNullOrEmpty(fileNameOld) ? "" : fileNameOld)
            + (checkFileName ? "," : "") + (StringUtils.isStringNullOrEmpty(fileNameNew) ? ""
            : fileNameNew));
    woInsideDTO.setSyncStatus(null);
    //xu ly loai cong viec la tai san
    List<WoMerchandiseInsideDTO> lstMer = new ArrayList<>();
    if (checkProperty(mapConfigProperty, String.valueOf(woInsideDTO.getWoTypeId()),
        Constants.CONFIG_PROPERTY.WO_TYPE_QLTS)) {
//      if (!checkProperty(mapConfigProperty, String.valueOf(woInsideDTO.getWoTypeId()),
//          Constants.CONFIG_PROPERTY.WO_TYPE_QLTS_DC_HC)) {
      if (fileCfgAttacks != null && !fileCfgAttacks.isEmpty()) {
        MultipartFile multipartFile = fileCfgAttacks.get(0);
        try {
          String filePath = FileUtils
              .saveTempFile(multipartFile.getOriginalFilename(), multipartFile.getBytes(),
                  tempFolder);
          File fileImp = new File(filePath);
          List<Object[]> lstHeader = CommonImport.getDataFromExcelFile(fileImp, 0, 2,
              0, 3, 2);
          if (lstHeader.size() == 0) {
            resultInSideDto.setKey(RESULT.FILE_INVALID_FORMAT);
            return resultInSideDto;
          }
          List<Object[]> lstData = CommonImport.getDataFromExcelFile(fileImp, 0, 3,
              1, 3, 2);
          if (lstData != null && !lstData.isEmpty()) {
            String lstMercode = "";
            List<MerchandiseBO> lstMercodeBo = new ArrayList<>();
            String validateImport = "";
            int row = 4;
            for (Object[] obj : lstData) {
              validateImport = validateImport(obj[0], obj[1]);
              MerchandiseBO bo = new MerchandiseBO();
              if (StringUtils.isStringNullOrEmpty(validateImport)) {
                lstMercode = lstMercode + obj[0].toString().trim() + ",";
                WoMerchandiseInsideDTO dtoMer = new WoMerchandiseInsideDTO(null, null,
                    obj[0].toString().trim(),
                    Double.valueOf(obj[1].toString().trim()),
                    obj[2] != null ? obj[2].toString().trim() : null, null);
                bo.setMerCode(obj[0].toString().trim());
                bo.setCount(Double.valueOf(obj[1].toString().trim()));
                lstMer.add(dtoMer);
                lstMercodeBo.add(bo);
                row++;
              } else {
                break;
              }
            }
            if (StringUtils.isNotNullOrEmpty(validateImport)) {
              resultInSideDto.setKey(RESULT.ERROR);
              resultInSideDto
                  .setMessage(
                      I18n.getLanguage("wo.errImportKtts") + " row" + row + validateImport);
              return resultInSideDto;
            } else {
              try {
                Kttsbo resultCheckKtts = null;
                // check file nang cap / nang cap uctt
                if (checkProperty(mapConfigProperty, String.valueOf(woInsideDTO.getWoTypeId()),
                    Constants.CONFIG_PROPERTY.WO_TYPE_QLTS_NC)
                    || checkProperty(mapConfigProperty, String.valueOf(woInsideDTO.getWoTypeId()),
                    Constants.CONFIG_PROPERTY.WO_TYPE_QLTS_NC_THUE)
                    || checkProperty(mapConfigProperty, String.valueOf(woInsideDTO.getWoTypeId()),
                    Constants.CONFIG_PROPERTY.WO_TYPE_QLTS_NC_UCTT)
                    || checkProperty(mapConfigProperty, String.valueOf(woInsideDTO.getWoTypeId()),
                    Constants.CONFIG_PROPERTY.WO_TYPE_QLTS_NC_UCTT_THUE)) {
                  resultCheckKtts = kttsVsmartPort.checkMerchandiseCodeNation(lstMercode, nation);
                  // check file thu hoi
                } else if (checkProperty(mapConfigProperty,
                    String.valueOf(woInsideDTO.getWoTypeId()),
                    Constants.CONFIG_PROPERTY.WO_TYPE_QLTS_THUHOI)) {
                  resultCheckKtts = kttsVsmartPort
                      .checkListMerEntityNation(lstMercodeBo, woInsideDTO.getStationCode(),
                          nation);
                }
                if (resultCheckKtts != null && !"Success"
                    .equalsIgnoreCase(resultCheckKtts.getStatus())) {
                  resultInSideDto.setKey(RESULT.ERROR);
                  resultInSideDto.setMessage(resultCheckKtts.getErrorInfo());
                  return resultInSideDto;
                }
              } catch (Exception e) {
                log.error(e.getMessage(), e);
                resultInSideDto.setKey(RESULT.ERROR);
                resultInSideDto
                    .setMessage(
                        "KTTS:" + I18n.getLanguage("wo.haveSomeErrWhenCheckListMerchandise"));
                return resultInSideDto;
              }
            }
          } else {
            resultInSideDto.setKey(RESULT.ERROR);
            resultInSideDto.setMessage(I18n.getLanguage("wo.fileKttsIsEmpty"));
            return resultInSideDto;
          }
        } catch (Exception ex) {
          log.error(ex.getMessage(), ex);
          resultInSideDto.setKey(RESULT.ERROR);
          resultInSideDto.setMessage(ex.getMessage());
          return resultInSideDto;
        }
      }
//      }
    }

    WoDTO woDTOTemp = findWoById(woInsideDTO.getWoId());
    woInsideDTO.setCableCode(woDTOTemp.getCableCode());
    woInsideDTO.setCableTypeCode(woDTOTemp.getCableTypeCode());
    woInsideDTO.setDistance(woDTOTemp.getDistance());
    woInsideDTO.setVibaLineCode(woDTOTemp.getVibaLineCode());
    woInsideDTO.setDeviceType(woDTOTemp.getDeviceType());
    woInsideDTO = convertWoDate2VietNamDate(woInsideDTO, offsetFromUser);
    woInsideDTO.setLastUpdateTime(date);
    resultInSideDto = woRepository.updateWo(woInsideDTO);

    if (RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
      WoDetailDTO woDetailDTO = woInsideDTO.getWoDetailDTO();
      if (woDetailDTO.getWoId() == null) {
        woDetailDTO.setWoId(woInsideDTO.getWoId());
        woDetailDTO.setCreateDate(date);
      } else {
        woDetailDTO = woDetailRepository.findWoDetailById(woDetailDTO.getWoId());
      }
      woDetailDTO.setLastUpdateTime(date);
      resultInSideDto = woDetailRepository.insertUpdateWoDetail(woDetailDTO);
      if (RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
        // Luu lich su
        WoHistoryInsideDTO woHistoryInsideDTO = new WoHistoryInsideDTO();
        if (!WO_STATUS.DRAFT.equals(woInsideDTO.getStatusSearchWeb())
            && !WO_STATUS.ASSIGNED.equals(woInsideDTO.getStatusSearchWeb())
            && !WO_STATUS.DISPATCH.equals(woInsideDTO.getStatusSearchWeb())) {
          woInsideDTO.setStatus(Long.valueOf(WO_STATUS.UNASSIGNED));
          woHistoryInsideDTO.setNewStatus(Long.valueOf(WO_STATUS.UNASSIGNED));
        }
        woHistoryInsideDTO.setWoId(woInsideDTO.getWoId());
        woHistoryInsideDTO.setWoCode(woInsideDTO.getWoCode());
        woHistoryInsideDTO.setWoContent(woInsideDTO.getWoContent());
        woHistoryInsideDTO.setUserName(userToken.getUserName());
        woHistoryInsideDTO.setUpdateTime(date);
        woHistoryInsideDTO.setComments(I18n.getLanguage("wo.comments.edit"));
        woHistoryInsideDTO.setCreatePersonId(userToken.getUserID());
        woHistoryInsideDTO.setCdId(woInsideDTO.getCdId());
        woHistoryInsideDTO.setFtId(woInsideDTO.getFtId());
        resultInSideDto = woHistoryRepository.insertWoHistory(woHistoryInsideDTO);
        if (RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
          insertParentHistory(woInsideDTO.getParentId(), userToken,
              I18n.getLanguage("wo.comments.editChild") + ": " + woInsideDTO.getWoCode());
          if (StringUtils.isNotNullOrEmpty(woInsideDTO.getWoWorklogContent())) {
            WoWorklogInsideDTO woWorklogInsideDTO = new WoWorklogInsideDTO(woInsideDTO);
            woWorklogInsideDTO.setUsername(userToken.getUserName());
            woWorklogRepository.insertWoWorklog(woWorklogInsideDTO);
          }
        }
      }
      if (lstMer != null && lstMer.size() > 0) {
        for (WoMerchandiseInsideDTO merdto : lstMer) {
          merdto.setWoId(woInsideDTO.getWoId());
        }
        woMerchandiseRepository.updateListWoMerchandise(lstMer);
      }
      WoKTTSInfoDTO woKTTSInfoDTO = woInsideDTO.getWoKTTSInfoDTO();
      resultInSideDto = woKTTSInfoRepository.inserOrUpdateWoKTTSInfo(woKTTSInfoDTO);
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto deleteListWo(List<WoInsideDTO> listWoInsideDTO) throws Exception {
    log.debug("Request to deleteListWo: {}", listWoInsideDTO);
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    UserToken userToken = ticketProvider.getUserToken();
    String userName = userToken.getUserName();
    Long userId = userToken.getUserID();
    Double offsetFromUser = userRepository.getOffsetFromUser(userToken.getUserID());
    Map<String, String> mapConfigProperty = commonRepository.getConfigProperty();
    for (WoInsideDTO woInsideDTO : listWoInsideDTO) {
      if (userId.equals(woInsideDTO.getCreatePersonId())) {
//        if (!(woInsideDTO.getStatus().equals(Long.valueOf(Constants.WO_STATUS.UNASSIGNED))
//            || (woInsideDTO.getStatus().equals(Long.valueOf(Constants.WO_STATUS.DISPATCH))
//            && woInsideDTO.getParentId() != null)
//            || (woInsideDTO.getStatus().equals(Long.valueOf(Constants.WO_STATUS.REJECT))
//            && woInsideDTO.getParentId() != null)
//            || woInsideDTO.getStatus().equals(Long.valueOf(Constants.WO_STATUS.DRAFT)))) {
//          resultInSideDto.setKey(RESULT.ERROR);
//          resultInSideDto.setMessage(I18n.getLanguage("wo.delete"));
//          return resultInSideDto;
//        }
        // khong cho xoa WO KTTS
        if (checkProperty(mapConfigProperty, String.valueOf(woInsideDTO.getWoTypeId()),
            Constants.CONFIG_PROPERTY.WO_TYPE_NOT_ALLOW_DELETE)) {
          resultInSideDto.setKey(RESULT.ERROR);
          resultInSideDto.setMessage(I18n.getLanguage("wo.delete.woKTTS"));
          return resultInSideDto;
        }
        if (!woInsideDTO.getStatus().equals(Long.valueOf(WO_STATUS.CLOSED_CD))) {
          // WO la unassigned va chua tung duoc tiep nhan
          // truongnt comment bo check
//          WoHistoryInsideDTO searchReject = new WoHistoryInsideDTO();
//          searchReject.setNewStatus(Long.valueOf(WO_STATUS.REJECT));
//          searchReject.setWoId(woInsideDTO.getWoId());
//          List<WoHistoryInsideDTO> listHistoryReject = woHistoryRepository
//              .getListWoHistoryDTO(searchReject);
//          if (listHistoryReject != null && !listHistoryReject.isEmpty()) {
//            resultInSideDto.setKey(RESULT.ERROR);
//            resultInSideDto.setMessage(I18n.getLanguage("wo.delete"));
//            return resultInSideDto;
//          }
          resultInSideDto = deleteWo(userName, woInsideDTO);
          if (RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
            if (woInsideDTO.getParentId() != null) {
              WoInsideDTO parentDTO = new WoInsideDTO();
              parentDTO.setWoId(woInsideDTO.getParentId());
              parentDTO.setUserId(userToken.getUserID());
              parentDTO.setOffSetFromUser(offsetFromUser);
              List<WoInsideDTO> listWoParent = woRepository.getListDataSearchWoDTO(parentDTO);
              for (WoInsideDTO dto : listWoParent) {
                updateParentStatusWhenDelete(dto, userToken, offsetFromUser);
              }
              insertParentHistory(woInsideDTO.getParentId(), userToken,
                  I18n.getLanguage("wo.comments.deleteChild") + ": " + woInsideDTO.getWoCode());
            }
          }
        } else {
          resultInSideDto.setKey(RESULT.ERROR);
          resultInSideDto.setMessage(I18n.getLanguage("wo.delete"));
          return resultInSideDto;
        }
      } else {
        resultInSideDto.setKey(RESULT.ERROR);
        resultInSideDto.setMessage(I18n.getLanguage("wo.delete.user"));
        return resultInSideDto;
      }
    }
    return resultInSideDto;
  }

  public ResultInSideDto deleteWo(String userName, WoInsideDTO woInsideDTO) {
    ResultInSideDto resultInSideDto;
    WoInsideDTO parentDTO = new WoInsideDTO();
    parentDTO.setParentId(woInsideDTO.getWoId());
    List<WoInsideDTO> listWoChild = woRepository.getListDataSearchWoDTO(parentDTO);
    if (listWoChild != null && !listWoChild.isEmpty()) {
      resultInSideDto = new ResultInSideDto();
      resultInSideDto.setKey(RESULT.ERROR);
      resultInSideDto.setMessage(I18n.getLanguage("wo.delete.child"));
      return resultInSideDto;
    }
    woRepository.deleteWo(woInsideDTO.getWoId());
    WoHistoryInsideDTO delHisDto = new WoHistoryInsideDTO();
    delHisDto.setNewStatus(woInsideDTO.getStatus());
    delHisDto.setOldStatus(null);
    delHisDto.setWoId(woInsideDTO.getWoId());
    delHisDto.setWoCode(woInsideDTO.getWoCode());
    delHisDto.setWoContent(I18n.getLanguage("wo.delete.Content"));
    delHisDto.setUserId(woInsideDTO.getUserId());
    delHisDto.setUserName(userName);
    delHisDto.setUpdateTime(new Date());
    delHisDto.setCreatePersonId(woInsideDTO.getCreatePersonId());
    delHisDto.setCdId(woInsideDTO.getCdId());
    delHisDto.setFtId(woInsideDTO.getFtId());
    delHisDto.setComments(I18n.getLanguage("wo.delete.Content"));
    resultInSideDto = woHistoryRepository.insertWoHistory(delHisDto);
    return resultInSideDto;
  }

  @Override
  public List<CatItemDTO> getListWoSystemInsertWeb() {
    log.debug("Request to deleteLigetListWoSystemInsertWebstWo: {}");
    return woRepository.getListWoSystemInsertWeb();
  }

  @Override
  public List<GnocFileDto> getListFileFromWo(Long woId) {
    log.debug("Request to getListFileFromWo: {}", woId);
    GnocFileDto gnocFileDto = new GnocFileDto();
    gnocFileDto.setBusinessCode(GNOC_FILE_BUSSINESS.WO);
    gnocFileDto.setBusinessId(woId);
    List<GnocFileDto> gnocFileDtos = gnocFileRepository.getListGnocFileByDto(gnocFileDto);
//    for (GnocFileDto fileDto : gnocFileDtos) {
//      fileDto.setFileName(FileUtils.getFileName(fileDto.getPath()));
//    }
    return gnocFileDtos;
  }

  @Override
  public List<CatStationBO> getStationListNation(String stationCode, String date) {
    log.debug("Request to getStationListNation: {}", stationCode);
    UserToken userToken = ticketProvider.getUserToken();
    String nation = getNationFromUnit(userToken.getDeptId());
    Kttsbo kttsbo = kttsVsmartPort.getStationListNation(stationCode, date, nation);
    return kttsbo.getListStation();
  }

  @Override
  public List<CatWarehouseBO> getListWarehouseNation(String warehouseCode, String warehouseName,
      String woType, String staffCode) {
    log.debug("Request to getListWarehouseNation: {}", warehouseCode);
    UserToken userToken = ticketProvider.getUserToken();
    String nation = getNationFromUnit(userToken.getDeptId());
    Kttsbo kttsbo = kttsVsmartPort
        .getListWarehouseNation(warehouseCode, warehouseName, woType, staffCode, nation);
    return kttsbo.getListWarehouse();
  }

  @Override
  public List<CntContractBO> getListContractFromConstrNation(String constrtCode) {
    log.debug("Request to getListContractFromConstrNation: {}", constrtCode);
    UserToken userToken = ticketProvider.getUserToken();
    String nation = getNationFromUnit(userToken.getDeptId());
    Kttsbo kttsbo = kttsVsmartPort.getListContractFromConstrNation(constrtCode, nation);
    return kttsbo.getListContract();
  }

  @Override
  public List<ConstrConstructionsBO> getConstructionListNation(String stationCode) {
    log.debug("Request to getConstructionListNation: {}", stationCode);
    UserToken userToken = ticketProvider.getUserToken();
    String nation = getNationFromUnit(userToken.getDeptId());
    Kttsbo kttsbo = kttsVsmartPort.getConstructionListNation(stationCode, nation);
    return kttsbo.getListConstruction();
  }

  @Override
  public List<CatItemDTO> getListWoKttsAction(String key) {
    log.debug("Request to getListWoKttsAction: {}", key);
    return woRepository.getListWoKttsAction(key);
  }

  @Override
  public File exportDataWo(WoInsideDTO woInsideDTO) throws Exception {
    UserToken userToken = ticketProvider.getUserToken();
    Double offsetFromUser = userRepository.getOffsetFromUser(userToken.getUserID());
    WoInsideDTO searchDto = converTimeFromClientToServerWeb(woInsideDTO, offsetFromUser);
    searchDto.setOffSetFromUser(offsetFromUser);
    List<WoInsideDTO> list = woRepository.getListWoExport(woInsideDTO);
    String[] header = new String[]{"woCode", "parentCode", "woTypeName", "woContent",
        "woDescription", "statusName", "resultName", "woSystem", "woSystemId", "createPersonName",
        "createDate", "cdName", "ftName", "priorityName", "startTime", "endTime", "remainTime",
        "completedTime", "finishTime", "accountIsdn", "createUnitName", "ftAcceptedTime",
        "woWorklogContent"};
    Date date = DateTimeUtils.convertDateOffset();
    return handleFileExport(list, header, DateUtil.date2ddMMyyyyHHMMss(date), "EXPORT_WO");
  }

  @Override
  public File getTemplateImportWo() throws Exception {
    ExcelWriterUtils ewu = new ExcelWriterUtils();
    String templatePathOut = "templates" + File.separator + "TEMPLATE_EXPORT.xlsx";
    Resource resource = new ClassPathResource(templatePathOut);
    InputStream fileTemplate = resource.getInputStream();
    XSSFWorkbook workBook = new XSSFWorkbook(fileTemplate);

    Map<String, CellStyle> styles = CommonExport.createStyles(workBook);

    XSSFSheet sheetMain = workBook.getSheetAt(0);
    XSSFSheet sheetOrther = workBook.createSheet("Orther");

    String[] header = new String[]{
        I18n.getLanguage("wo.STT"),
        I18n.getLanguage("wo.woContent"),
        I18n.getLanguage("wo.woDescription"),
        I18n.getLanguage("wo.woSystemId"),
        I18n.getLanguage("wo.woTypeCode"),
        I18n.getLanguage("wo.priorityCode"),
        I18n.getLanguage("wo.startTime"),
        I18n.getLanguage("wo.endTime"),
        I18n.getLanguage("wo.assign"),
        I18n.getLanguage("wo.businessType"),
        I18n.getLanguage("wo.businessCode"),
        I18n.getLanguage("wo.activeEnvironmentId"),
        I18n.getLanguage("wo.reasonDetail")
    };
    String[] headerStar = new String[]{
        I18n.getLanguage("wo.woContent"),
        I18n.getLanguage("wo.woSystemId"),
        I18n.getLanguage("wo.woTypeCode"),
        I18n.getLanguage("wo.priorityCode"),
        I18n.getLanguage("wo.startTime"),
        I18n.getLanguage("wo.endTime"),
        I18n.getLanguage("wo.assign"),
        I18n.getLanguage("wo.businessType"),
        I18n.getLanguage("wo.businessCode")
    };
    String[] headerFormatTime = new String[]{
        I18n.getLanguage("wo.formatTime")
    };
    XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(sheetMain);
    List<String> listHeader = Arrays.asList(header);
    List<String> listHeaderStar = Arrays.asList(headerStar);
    List<String> listHeaderFormatTime = Arrays.asList(headerFormatTime);

    //int sttColumn = listHeader.indexOf(I18n.getLanguage("wo.STT"));
    //int woContentColumn = listHeader.indexOf(I18n.getLanguage("wo.woContent"));
    //int woDescriptionColumn = listHeader.indexOf(I18n.getLanguage("wo.woDescription"));
    int woSystemIdColumn = listHeader.indexOf(I18n.getLanguage("wo.woSystemId"));
    //int woTypeCodeColumn = listHeader.indexOf(I18n.getLanguage("wo.woTypeCode"));
    int priorityCodeColumn = listHeader.indexOf(I18n.getLanguage("wo.priorityCode"));
    //int startTimeColumn = listHeader.indexOf(I18n.getLanguage("wo.startTime"));
    //int endTimeColumn = listHeader.indexOf(I18n.getLanguage("wo.endTime"));
    int assignColumn = listHeader.indexOf(I18n.getLanguage("wo.assign"));
    int businessTypeColumn = listHeader.indexOf(I18n.getLanguage("wo.businessType"));
    //int businessCodeColumn = listHeader.indexOf(I18n.getLanguage("wo.businessCode"));

    String firstLeftHeaderTitle = I18n.getLanguage("wo.export.firstLeftHeader");
    String secondLeftHeaderTitle = I18n.getLanguage("wo.export.secondLeftHeader");
    String firstRightHeaderTitle = I18n.getLanguage("wo.export.firstRightHeader");
    String secondRightHeaderTitle = I18n.getLanguage("wo.export.secondRightHeader");

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
    mainCellTitle.setCellValue(I18n.getLanguage("wo.template.title"));
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
      for (String checkHeaderFormatTime : listHeaderFormatTime) {
        if (checkHeaderFormatTime.equalsIgnoreCase(listHeader.get(i))) {
          rt.append("\n" + I18n.getLanguage(".wo.formatTime"), fontStar);
        }
      }
      for (String checkHeaderStar : listHeaderStar) {
        if (checkHeaderStar.equalsIgnoreCase(listHeader.get(i))) {
          rt.append(" (*)", fontStar);
        }
      }
      headerCell.setCellValue(rt);
      headerCell.setCellStyle(cellStyleHeader);
      sheetMain.setColumnWidth(i, 7000);
    }
    sheetMain.setColumnWidth(0, 3000);

    ewu.createCell(sheetOrther, 0, 0, I18n.getLanguage("wo.woSystemId").toUpperCase(),
        styles.get("header"));
    ewu.createCell(sheetOrther, 1, 0, I18n.getLanguage("wo.priorityCode").toUpperCase(),
        styles.get("header"));
    ewu.createCell(sheetOrther, 2, 0, I18n.getLanguage("wo.assign").toUpperCase(),
        styles.get("header"));
    ewu.createCell(sheetOrther, 3, 0, I18n.getLanguage("wo.businessType").toUpperCase(),
        styles.get("header"));

    int row = 1;
    List<CatItemDTO> listWoSystem = woRepository.getListWoSystemInsertWeb();
    for (CatItemDTO dto : listWoSystem) {
      ewu.createCell(sheetOrther, 0, row++, dto.getItemName(), styles.get("cell"));
    }
    Name woSystemName = workBook.createName();
    woSystemName.setNameName("woSystemName");
    woSystemName.setRefersToFormula("Orther!$A$2:$A$" + row);
    XSSFDataValidationConstraint woSystemConstraint = new XSSFDataValidationConstraint(
        ValidationType.LIST, "woSystemName");
    CellRangeAddressList cellRangeWoSystem = new CellRangeAddressList(6, (maxRecord + 6),
        woSystemIdColumn, woSystemIdColumn);
    XSSFDataValidation dataValidationWoSystem = (XSSFDataValidation) dvHelper.createValidation(
        woSystemConstraint, cellRangeWoSystem);
    dataValidationWoSystem.setShowErrorBox(true);
    sheetMain.addValidationData(dataValidationWoSystem);

    row = 1;
//    Map<String, String> mapConfigProperty = commonRepository.getConfigProperty();
//    String lstPriorityCode = mapConfigProperty.get("WO.LIST.PRIORITY");
//    String[] priorityCode = lstPriorityCode.split(",");
    List<WoPriorityDTO> woPriorityDTOS = getWoPriorityByWoTypeID(null);
    if (woPriorityDTOS != null) {
      for (WoPriorityDTO code : woPriorityDTOS) {
        ewu.createCell(sheetOrther, 1, row++, code.getPriorityName(), styles.get("cell"));
      }
    }
    Name priorityCodeName = workBook.createName();
    priorityCodeName.setNameName("priorityCodeName");
    priorityCodeName.setRefersToFormula("Orther!$B$2:$B$" + row);
    XSSFDataValidationConstraint priorityCodeConstraint = new XSSFDataValidationConstraint(
        ValidationType.LIST, "priorityCodeName");
    CellRangeAddressList cellRangePriorityCode = new CellRangeAddressList(6, (maxRecord + 6),
        priorityCodeColumn, priorityCodeColumn);
    XSSFDataValidation dataValidationPriorityCode = (XSSFDataValidation) dvHelper.createValidation(
        priorityCodeConstraint, cellRangePriorityCode);
    dataValidationPriorityCode.setShowErrorBox(true);
    sheetMain.addValidationData(dataValidationPriorityCode);

    row = 1;
    ewu.createCell(sheetOrther, 2, row++, I18n.getLanguage("wo.assign.cd"),
        styles.get("cell"));
    ewu.createCell(sheetOrther, 2, row++, I18n.getLanguage("wo.assign.ft"),
        styles.get("cell"));
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
    // loai doi tuong
    row = 1;
    ewu.createCell(sheetOrther, 3, row++, I18n.getLanguage("wo.import.type.userName"),
        styles.get("cell"));
    ewu.createCell(sheetOrther, 3, row++, I18n.getLanguage("wo.import.type.stationCode"),
        styles.get("cell"));
    ewu.createCell(sheetOrther, 3, row++, I18n.getLanguage("wo.import.type.deviceCode"),
        styles.get("cell"));
    ewu.createCell(sheetOrther, 3, row++, I18n.getLanguage("wo.import.type.account"),
        styles.get("cell"));
    ewu.createCell(sheetOrther, 3, row++, I18n.getLanguage("wo.import.type.connectorCode"),
        styles.get("cell"));
    ewu.createCell(sheetOrther, 3, row++, I18n.getLanguage("wo.import.type.cdGroupCode"),
        styles.get("cell"));

    Name bussinessTypeName = workBook.createName();
    bussinessTypeName.setNameName("bussinessTypeName");
    bussinessTypeName.setRefersToFormula("Orther!$D$2:$D$" + row);
    XSSFDataValidationConstraint bussinessConstant = new XSSFDataValidationConstraint(
        ValidationType.LIST, "bussinessTypeName");
    CellRangeAddressList cellRangeAddressListBussinessType = new CellRangeAddressList(6,
        (maxRecord + 6), businessTypeColumn, businessTypeColumn);
    XSSFDataValidation dataValidationBussiness = (XSSFDataValidation) dvHelper.createValidation(
        bussinessConstant, cellRangeAddressListBussinessType);
    dataValidationBussiness.setShowErrorBox(true);
    sheetMain.addValidationData(dataValidationBussiness);
    workBook.setSheetHidden(1, true);
    workBook.setSheetName(0, I18n.getLanguage("wo.add.by.import"));

    // danh sach loai cong viec
    workBook.createSheet("Wo type");
    XSSFSheet sheetType = workBook.getSheet("Wo type");
    row = 1;
    WoTypeInsideDTO woTypeSearch = new WoTypeInsideDTO();
    woTypeSearch.setEnableCreate(1L);
    woTypeSearch.setIsEnable(1L);
    try {
      ewu.createCell(sheetType, 4, 0, I18n.getLanguage("wo.woTypeName"), styles.get("header"));
      ewu.createCell(sheetType, 5, 0, I18n.getLanguage("wo.woTypeCode"), styles.get("header"));
      sheetType.setColumnWidth(4, 20000);
      sheetType.setColumnWidth(5, 15000);
      List<WoTypeInsideDTO> lst = woTypeBusiness.getListWoTypeIsEnable(woTypeSearch);
      for (WoTypeInsideDTO o : lst) {
        ewu.createCell(sheetType, 4, row, o.getWoTypeName(), styles.get("cell"));
        ewu.createCell(sheetType, 5, row, o.getWoTypeCode(), styles.get("cell"));
        row++;
      }

    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }

    // danh sach nhom dieu phoi
    workBook.createSheet("Cd group");
    XSSFSheet sheetCd = workBook.getSheet("Cd group");
    row = 1;

    WoCdGroupInsideDTO cdTmp = new WoCdGroupInsideDTO();
    cdTmp.setGroupTypeId(4L);
    try {
      ewu.createCell(sheetCd, 4, 0, I18n.getLanguage("woCdGroupForm.woGroupName"),
          styles.get("header"));
      ewu.createCell(sheetCd, 5, 0, I18n.getLanguage("woCdGroupForm.woGroupCode"),
          styles.get("header"));
      sheetCd.setColumnWidth(4, 20000);
      sheetCd.setColumnWidth(5, 15000);
      List<WoCdGroupInsideDTO> lstCd = woCdGroupBusiness
          .getListWoCdGroupActive(cdTmp, 0, Integer.MAX_VALUE, "", "woGroupName");
      for (WoCdGroupInsideDTO o : lstCd) {
        ewu.createCell(sheetCd, 4, row, o.getWoGroupName(), styles.get("cell"));
        ewu.createCell(sheetCd, 5, row, o.getWoGroupCode(), styles.get("cell"));
        row++;
      }

    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }

    String fileResult = tempFolder + File.separator;
    String fileName = "IMPORT_WO" + "_" + System.currentTimeMillis() + ".xlsx";
    ewu.saveToFileExcel(workBook, fileResult, fileName);
    String resultPath = fileResult + fileName;
    File fileExport = new File(resultPath);
    return fileExport;
  }

  @Override
  public File getTemplateImportAssetsForWo() throws Exception {
    ExcelWriterUtils ewu = new ExcelWriterUtils();
    String templatePathOut = "templates" + File.separator + "TEMPLATE_EXPORT.xlsx";
    Resource resource = new ClassPathResource(templatePathOut);
    InputStream fileTemplate = resource.getInputStream();
    XSSFWorkbook workBook = new XSSFWorkbook(fileTemplate);

    Map<String, CellStyle> styles = CommonExport.createStyles(workBook);

    XSSFSheet sheetMain = workBook.getSheetAt(0);

    String[] header = new String[]{
        I18n.getLanguage("wo.STT"),
        I18n.getLanguage("wo.infoAsset.content"),
        I18n.getLanguage("wo.woDescription"),
        I18n.getLanguage("wo.startTime"),
        I18n.getLanguage("wo.endTime"),
        I18n.getLanguage("wo.infoAsset.codePlan")
    };
    String[] headerStar = new String[]{
        I18n.getLanguage("wo.infoAsset.content"),
        I18n.getLanguage("wo.startTime"),
        I18n.getLanguage("wo.endTime")
    };
    String[] headerFormatTime = new String[]{
        I18n.getLanguage("wo.formatTime")
    };
    XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(sheetMain);
    List<String> listHeader = Arrays.asList(header);
    List<String> listHeaderStar = Arrays.asList(headerStar);
    List<String> listHeaderFormatTime = Arrays.asList(headerFormatTime);

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

    XSSFFont fontStar = workBook.createFont();
    fontStar.setColor(IndexedColors.RED.getIndex());

    //Tao header
    Row headerRow = sheetMain.createRow(0);
    headerRow.setHeight((short) 600);
    for (int i = 0; i < listHeader.size(); i++) {
      Cell headerCell = headerRow.createCell(i);
      XSSFRichTextString rt = new XSSFRichTextString(listHeader.get(i));
      for (String checkHeaderFormatTime : listHeaderFormatTime) {
        if (checkHeaderFormatTime.equalsIgnoreCase(listHeader.get(i))) {
          rt.append("\n" + I18n.getLanguage(".wo.formatTime"), fontStar);
        }
      }
      for (String checkHeaderStar : listHeaderStar) {
        if (checkHeaderStar.equalsIgnoreCase(listHeader.get(i))) {
          rt.append(" (*)", fontStar);
        }
      }
      headerCell.setCellValue(rt);
      headerCell.setCellStyle(cellStyleHeader);
      sheetMain.setColumnWidth(i, 7000);
    }
    sheetMain.setColumnWidth(0, 3000);
    workBook.setSheetName(0, I18n.getLanguage("wo.infoAsset.info"));
    String fileResult = tempFolder + File.separator;
    String fileName = "IMPORT_INFO_WO" + "_" + System.currentTimeMillis() + ".xlsx";
    ewu.saveToFileExcel(workBook, fileResult, fileName);
    String resultPath = fileResult + fileName;
    File fileExport = new File(resultPath);
    return fileExport;
  }

  @Override
  public File getTemplateImportWoTranSferOfProperty() throws Exception {
    ExcelWriterUtils ewu = new ExcelWriterUtils();
    String templatePathOut = "templates" + File.separator + "TEMPLATE_EXPORT.xlsx";
    Resource resource = new ClassPathResource(templatePathOut);
    InputStream fileTemplate = resource.getInputStream();
    XSSFWorkbook workBook = new XSSFWorkbook(fileTemplate);

    Map<String, CellStyle> styles = CommonExport.createStyles(workBook);

    XSSFSheet sheetMain = workBook.getSheetAt(0);

    String[] header = new String[]{
        I18n.getLanguage("wo.STT"),
        I18n.getLanguage("wo.infoAsset.codeMaterial"),
        I18n.getLanguage("wo.infoAsset.amount"),
        I18n.getLanguage("wo.infoAsset.codeStationSource"),
        I18n.getLanguage("wo.infoAsset.codeStationDestination"),
        I18n.getLanguage("wo.infoAsset.import.homeStationCodeDowngrade"),
        I18n.getLanguage("wo.infoAsset.import.homeStationCodeUpgrade")
    };
    String[] headerStar = new String[]{
        I18n.getLanguage("wo.infoAsset.codeMaterial"),
        I18n.getLanguage("wo.infoAsset.amount"),
        I18n.getLanguage("wo.infoAsset.codeStationSource"),
        I18n.getLanguage("wo.infoAsset.codeStationDestination"),
        I18n.getLanguage("wo.infoAsset.import.homeStationCodeDowngrade"),
        I18n.getLanguage("wo.infoAsset.import.homeStationCodeUpgrade")
    };

    XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(sheetMain);
    List<String> listHeader = Arrays.asList(header);
    List<String> listHeaderStar = Arrays.asList(headerStar);

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

    XSSFFont fontStar = workBook.createFont();
    fontStar.setColor(IndexedColors.RED.getIndex());

    //Tao tieu de
    Row rowMainTitle = sheetMain.createRow(1);
    Cell mainCellTitle = rowMainTitle.createCell(1);
    mainCellTitle.setCellValue(I18n.getLanguage("wo.infoAsset.listMaterialCode"));
    mainCellTitle.setCellStyle(cellStyleTitle);
    sheetMain.addMergedRegion(new CellRangeAddress(1, 1, 1, 4));

    //Tao header
    Row headerRow = sheetMain.createRow(2);
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
      sheetMain.setColumnWidth(i, 7000);
    }
    sheetMain.setColumnWidth(0, 3000);
    workBook.setSheetName(0, I18n.getLanguage("wo.infoAsset.listMaterialCode"));
    String fileResult = tempFolder + File.separator;
    String fileName =
        "IMPORT_TRANSFER_OF_PROPERTY_ATTACH_WO" + "_" + System.currentTimeMillis() + ".xlsx";
    ewu.saveToFileExcel(workBook, fileResult, fileName);
    String resultPath = fileResult + fileName;
    File fileExport = new File(resultPath);
    return fileExport;
  }

  @Override
  public File getTemplateImportWoDowngradeWithDrawal() throws Exception {
    ExcelWriterUtils ewu = new ExcelWriterUtils();
    String templatePathOut = "templates" + File.separator + "TEMPLATE_EXPORT.xlsx";
    Resource resource = new ClassPathResource(templatePathOut);
    InputStream fileTemplate = resource.getInputStream();
    XSSFWorkbook workBook = new XSSFWorkbook(fileTemplate);

    Map<String, CellStyle> styles = CommonExport.createStyles(workBook);

    XSSFSheet sheetMain = workBook.getSheetAt(0);

    String[] header = new String[]{
        I18n.getLanguage("wo.STT"),
        I18n.getLanguage("wo.infoAsset.codeMaterial"),
        I18n.getLanguage("wo.infoAsset.amount"),
        I18n.getLanguage("wo.infoAsset.downgradeStation"),
        I18n.getLanguage("wo.infoAsset.importWarehouse"),
        I18n.getLanguage("wo.infoAsset.import.homeStationCodeDowngrade")
    };
    String[] headerStar = new String[]{
        I18n.getLanguage("wo.infoAsset.codeMaterial"),
        I18n.getLanguage("wo.infoAsset.amount"),
        I18n.getLanguage("wo.infoAsset.downgradeStation"),
        I18n.getLanguage("wo.infoAsset.importWarehouse"),
        I18n.getLanguage("wo.infoAsset.import.homeStationCodeDowngrade")
    };

    XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(sheetMain);
    List<String> listHeader = Arrays.asList(header);
    List<String> listHeaderStar = Arrays.asList(headerStar);

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

    XSSFFont fontStar = workBook.createFont();
    fontStar.setColor(IndexedColors.RED.getIndex());

    //Tao tieu de
    Row rowMainTitle = sheetMain.createRow(1);
    Cell mainCellTitle = rowMainTitle.createCell(1);
    mainCellTitle.setCellValue(I18n.getLanguage("wo.infoAsset.listMaterial"));
    mainCellTitle.setCellStyle(cellStyleTitle);
    sheetMain.addMergedRegion(new CellRangeAddress(1, 1, 1, 4));

    //Tao header
    Row headerRow = sheetMain.createRow(2);
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
      sheetMain.setColumnWidth(i, 7000);
    }
    sheetMain.setColumnWidth(0, 3000);
    workBook.setSheetName(0, I18n.getLanguage("wo.infoAsset.listMaterial"));
    String fileResult = tempFolder + File.separator;
    String fileName =
        "WO_DOWNGRADE_WITH_DRAWAL_ATTACH" + "_" + System.currentTimeMillis() + ".xlsx";
    ewu.saveToFileExcel(workBook, fileResult, fileName);
    String resultPath = fileResult + fileName;
    File fileExport = new File(resultPath);
    return fileExport;
  }

  @Override
  public File getTemplateImportWoUpgradeStation() throws Exception {
    ExcelWriterUtils ewu = new ExcelWriterUtils();
    String templatePathOut = "templates" + File.separator + "TEMPLATE_EXPORT.xlsx";
    Resource resource = new ClassPathResource(templatePathOut);
    InputStream fileTemplate = resource.getInputStream();
    XSSFWorkbook workBook = new XSSFWorkbook(fileTemplate);

    Map<String, CellStyle> styles = CommonExport.createStyles(workBook);

    XSSFSheet sheetMain = workBook.getSheetAt(0);

    String[] header = new String[]{
        I18n.getLanguage("wo.STT"),
        I18n.getLanguage("wo.infoAsset.codeMaterial"),
        I18n.getLanguage("wo.infoAsset.amount"),
        I18n.getLanguage("wo.infoAsset.codeUpgrade"),
        I18n.getLanguage("wo.infoAsset.exportWarehouse"),
        I18n.getLanguage("wo.infoAsset.import.homeStationCodeUpgrade"),
    };
    String[] headerStar = new String[]{
        I18n.getLanguage("wo.infoAsset.codeMaterial"),
        I18n.getLanguage("wo.infoAsset.amount"),
        I18n.getLanguage("wo.infoAsset.codeUpgrade"),
        I18n.getLanguage("wo.infoAsset.exportWarehouse"),
        I18n.getLanguage("wo.infoAsset.import.homeStationCodeUpgrade"),
    };

    XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(sheetMain);
    List<String> listHeader = Arrays.asList(header);
    List<String> listHeaderStar = Arrays.asList(headerStar);

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

    XSSFFont fontStar = workBook.createFont();
    fontStar.setColor(IndexedColors.RED.getIndex());

    //Tao tieu de
    Row rowMainTitle = sheetMain.createRow(1);
    Cell mainCellTitle = rowMainTitle.createCell(1);
    mainCellTitle.setCellValue(I18n.getLanguage("wo.infoAsset.listMaterial"));
    mainCellTitle.setCellStyle(cellStyleTitle);
    sheetMain.addMergedRegion(new CellRangeAddress(1, 1, 1, 4));

    //Tao header
    Row headerRow = sheetMain.createRow(2);
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
      sheetMain.setColumnWidth(i, 7000);
    }
    sheetMain.setColumnWidth(0, 3000);
    workBook.setSheetName(0, I18n.getLanguage("wo.infoAsset.listMaterial"));
    String fileResult = tempFolder + File.separator;
    String fileName = "WO_UPGRADE_STATION_ATTACH" + "_" + System.currentTimeMillis() + ".xlsx";
    ewu.saveToFileExcel(workBook, fileResult, fileName);
    String resultPath = fileResult + fileName;
    File fileExport = new File(resultPath);
    return fileExport;
  }

  @Override
  public Datatable getListWoHistoryByWoId(WoHistoryInsideDTO woHistoryInsideDTO) {
    log.debug("Request to getListWoHistoryByWoId: {}", woHistoryInsideDTO);
    UserToken userToken = ticketProvider.getUserToken();
    Double offsetFromUser = userRepository.getOffsetFromUser(userToken.getUserID());
    woHistoryInsideDTO.setOffset(offsetFromUser);
    return woHistoryRepository.getListWoHistoryByWoId(woHistoryInsideDTO);
  }

  @Override
  public Datatable getListWorklogByWoIdPaging(WoWorklogInsideDTO woWorklogInsideDTO) {
    log.debug("Request to getListWorklogByWoIdPaging: {}", woWorklogInsideDTO);
    UserToken userToken = ticketProvider.getUserToken();
    Double offsetFromUser = userRepository.getOffsetFromUser(userToken.getUserID());
    woWorklogInsideDTO.setOffset(offsetFromUser);
    return woWorklogRepository.getListWorklogByWoIdPaging(woWorklogInsideDTO);
  }

  @Override
  public ResultInSideDto insertWoWorklog(WoInsideDTO woInsideDTO) {
    log.debug("Request to insertWoWorklog: {}", woInsideDTO);
    UserToken userToken = ticketProvider.getUserToken();
    woInsideDTO.setCreatePersonId(userToken.getUserID());
    WoWorklogInsideDTO woWorklogInsideDTO = new WoWorklogInsideDTO(woInsideDTO);
    woWorklogInsideDTO.setUsername(userToken.getUserName());
    woWorklogInsideDTO.setUpdateTime(new Date());
    return woWorklogRepository.insertWoWorklog(woWorklogInsideDTO);
  }

  @Override
  public Datatable loadTroubleCrDTO(WoInsideDTO woInsideDTO) {
    log.debug("Request to loadTroubleCrDTO: {}", woInsideDTO);
    int page = woInsideDTO.getPage();
    int size = woInsideDTO.getPageSize();

    CrInsiteDTO crInsiteDTO = new CrInsiteDTO();
    crInsiteDTO.setSystemId(Long.valueOf(SYSTEM.WO));
    crInsiteDTO.setObjectId(woInsideDTO.getWoId());
    crInsiteDTO.setStepId(null);
    Datatable datatable = new Datatable();
    List<CrDTO> dtoCRResult = crServiceProxy.getListCRFromOtherSystem(crInsiteDTO);
    if (dtoCRResult != null && dtoCRResult.size() > 0) {
      int totalSize = dtoCRResult.size();
      int pageSize = (int) Math.ceil(totalSize * 1.0 / size);
      List<CrDTO> crSubList = (List<CrDTO>) DataUtil.subPageList(dtoCRResult, page, size);
      List<CrSearchDTO> crSearchDTOS = convertMapDataCR(crSubList);
      if (crSearchDTOS != null && crSearchDTOS.size() > 0) {
        crSearchDTOS.get(0).setPage(page);
        crSearchDTOS.get(0).setPageSize(pageSize);
        crSearchDTOS.get(0).setTotalRow(totalSize);
      }
      datatable.setData(crSearchDTOS);
      datatable.setTotal(totalSize);
      datatable.setPages(pageSize);
    }
    return datatable;
  }

  @Override
  public ResultInSideDto updateFileAttack(List<MultipartFile> fileAttacks,
      WoInsideDTO woInsideDTO) throws IOException {
    UserToken userToken = ticketProvider.getUserToken();
    UnitDTO unitToken = unitRepository.findUnitById(userToken.getDeptId());
    WoDTO woDTO = findWoById(woInsideDTO.getWoId());
    List<String> lstFileName = new ArrayList<>();
    //Start save file old
    String fileNameOld = woDTO.getFileName();
    if (StringUtils.isNotNullOrEmpty(fileNameOld)) {
      String[] fileNames = fileNameOld.split(",");
      for (int i = 0; i < fileNames.length; i++) {
        lstFileName.add(fileNames[i].trim());
        for (String name : woInsideDTO.getFileNamesDelete()) {
          if (fileNames[i].trim().equals(name.trim())) {
            lstFileName.remove(fileNames[i].trim());
          }
        }
      }
    }
    //End save file old
    List<GnocFileDto> gnocFileDtos = new ArrayList<>();
    for (MultipartFile multipartFile : fileAttacks) {
      String fullPath = FileUtils
          .saveFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
              PassTranformer.decrypt(ftpPass), ftpFolder, multipartFile.getOriginalFilename(),
              multipartFile.getBytes(),
              FileUtils.createDateOfFileName(woInsideDTO.getCreateDate()));
      String fileName = multipartFile.getOriginalFilename();
      //Start save file old
      lstFileName.add(FileUtils.getFileName(fullPath));
      //End save file old
      GnocFileDto gnocFileDto = new GnocFileDto();
      gnocFileDto.setPath(fullPath);
      gnocFileDto.setFileName(FileUtils.getFileName(fullPath));
      gnocFileDto.setCreateUnitId(userToken.getDeptId());
      gnocFileDto.setCreateUnitName(unitToken.getUnitName());
      gnocFileDto.setCreateUserId(userToken.getUserID());
      gnocFileDto.setCreateUserName(userToken.getUserName());
      gnocFileDto.setCreateTime(woInsideDTO.getCreateDate());
      gnocFileDto.setMappingId(woInsideDTO.getWoId());
      gnocFileDtos.add(gnocFileDto);
    }
//    List<GnocFileDto> gnocFileDtosAll = new ArrayList<>();
//    gnocFileDtosAll.addAll(gnocFileDtos);
//    gnocFileDtosAll.addAll(woInsideDTO.getGnocFileDtos());
    if (woInsideDTO.getIdDeleteList() != null && !woInsideDTO.getIdDeleteList().isEmpty()) {
      for (Long id : woInsideDTO.getIdDeleteList()) {
        GnocFileDto gnocFileDto = new GnocFileDto();
        gnocFileDto.setId(id);
        gnocFileRepository.deleteGnocFileByDto(gnocFileDto);
      }
    }
    gnocFileRepository
        .saveListGnocFileNotDeleteAll(GNOC_FILE_BUSSINESS.WO, woInsideDTO.getWoId(), gnocFileDtos);
    lstFileName = lstFileName.stream().distinct().collect(Collectors.toList());
    woInsideDTO.setFileName(lstFileName.size() > 0 ? String.join(",", lstFileName) : "");
    woInsideDTO.setSyncStatus(null);
    return woRepository.updateWo(woInsideDTO);
  }

  @Override
  public File getTemplateExportWOTestServiceFromCR() throws Exception {
    ExcelWriterUtils ewu = new ExcelWriterUtils();
    String templatePathOut = "templates" + File.separator + "TEMPLATE_EXPORT.xlsx";
    Resource resource = new ClassPathResource(templatePathOut);
    InputStream fileTemplate = resource.getInputStream();
    XSSFWorkbook workBook = new XSSFWorkbook(fileTemplate);
    XSSFSheet sheetMain = workBook.getSheetAt(0);

    String[] header = new String[]{
        I18n.getLanguage("wo.STT"),
        I18n.getLanguage("wo.crNumber")
    };

    List<String> listHeader = Arrays.asList(header);
    int sttColumn = listHeader.indexOf(I18n.getLanguage("wo.STT"));
    int crNumberColumn = listHeader.indexOf(I18n.getLanguage("wo.crNumber"));

    Font xSSFFont = workBook.createFont();
    xSSFFont.setFontName(HSSFFont.FONT_ARIAL);
    xSSFFont.setFontHeightInPoints((short) 20);
    xSSFFont.setBold(true);
    xSSFFont.setColor(IndexedColors.BLACK.index);

    CellStyle cellStyleTitle = workBook.createCellStyle();
    cellStyleTitle.setAlignment(HorizontalAlignment.CENTER);
    cellStyleTitle.setVerticalAlignment(VerticalAlignment.CENTER);
    cellStyleTitle.setFillForegroundColor(IndexedColors.WHITE.index);
    cellStyleTitle.setFont(xSSFFont);

    Font xSSFFontHeader = workBook.createFont();
    xSSFFontHeader.setFontName(HSSFFont.FONT_ARIAL);
    xSSFFontHeader.setFontHeightInPoints((short) 10);
    xSSFFontHeader.setColor(IndexedColors.BLUE.index);
    xSSFFontHeader.setBold(true);

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

    //Tao tieu de
    Row rowMainTitle = sheetMain.createRow(0);
    Cell mainCellTitle = rowMainTitle.createCell(0);
    mainCellTitle.setCellValue(I18n.getLanguage("wo.templateExportWOTestServiceFromCR.title"));
//    mainCellTitle.setCellStyle(styles.get("title"));
    mainCellTitle.setCellStyle(cellStyleTitle);
    sheetMain.addMergedRegion(new CellRangeAddress(0, 0, 0, 1));

    //Tao header
    Row headerRow = sheetMain.createRow(2);
    headerRow.setHeight((short) 600);
    for (int i = 0; i < listHeader.size(); i++) {
      Cell headerCell = headerRow.createCell(i);
      XSSFRichTextString rt = new XSSFRichTextString(listHeader.get(i));
      headerCell.setCellValue(rt);
//      headerCell.setCellStyle(styles.get("header"));
      headerCell.setCellStyle(cellStyleHeader);
    }

    sheetMain.setColumnWidth(sttColumn, 2000);
    sheetMain.setColumnWidth(crNumberColumn, 10000);

    workBook.setSheetName(0, I18n.getLanguage("wo.templateExportWOTestServiceFromCR.sheetName"));
    String fileResult = tempFolder + File.separator;
    String fileName = I18n.getLanguage("wo.templateExportWOTestServiceFromCR.fileName") + ".xlsx";
    ewu.saveToFileExcel(workBook, fileResult, fileName);
    String resultPath = fileResult + fileName;
    File fileExport = new File(resultPath);
    return fileExport;
  }

  @Override
  public File getTemplateExportWOTestServiceFromWO() throws Exception {
    ExcelWriterUtils ewu = new ExcelWriterUtils();
    String templatePathOut = "templates" + File.separator + "TEMPLATE_EXPORT.xlsx";
    Resource resource = new ClassPathResource(templatePathOut);
    InputStream fileTemplate = resource.getInputStream();
    XSSFWorkbook workBook = new XSSFWorkbook(fileTemplate);

    XSSFSheet sheetMain = workBook.getSheetAt(0);

    String[] header = new String[]{
        I18n.getLanguage("wo.STT"),
        I18n.getLanguage("wo.woCode")
    };

    List<String> listHeader = Arrays.asList(header);
    int sttColumn = listHeader.indexOf(I18n.getLanguage("wo.STT"));
    int woCodeColumn = listHeader.indexOf(I18n.getLanguage("wo.woCode"));

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

    //Tao tieu de
    Row rowMainTitle = sheetMain.createRow(0);
    Cell mainCellTitle = rowMainTitle.createCell(0);
    mainCellTitle.setCellValue(I18n.getLanguage("wo.templateExportWOTestServiceFromWO.title"));
    mainCellTitle.setCellStyle(cellStyleTitle);
    sheetMain.addMergedRegion(new CellRangeAddress(0, 0, 0, 1));

    //Tao header
    Row headerRow = sheetMain.createRow(4);
    headerRow.setHeight((short) 600);
    for (int i = 0; i < listHeader.size(); i++) {
      Cell headerCell = headerRow.createCell(i);
      XSSFRichTextString rt = new XSSFRichTextString(listHeader.get(i));
      headerCell.setCellValue(rt);
      headerCell.setCellStyle(cellStyleHeader);
    }

    sheetMain.setColumnWidth(sttColumn, 2000);
    sheetMain.setColumnWidth(woCodeColumn, 10000);

    workBook.setSheetName(0, I18n.getLanguage("wo.templateExportWOTestServiceFromWO.sheetName"));
    String fileResult = tempFolder + File.separator;
    String fileName = I18n.getLanguage("wo.templateExportWOTestServiceFromWO.fileName") + ".xlsx";
    ewu.saveToFileExcel(workBook, fileResult, fileName);
    String resultPath = fileResult + fileName;
    File fileExport = new File(resultPath);
    return fileExport;
  }

  @Override
  public ResultInSideDto acceptWoFromWeb(WoInsideDTO woInsideDTO) throws Exception {
    UserToken userToken = ticketProvider.getUserToken();
    String username = userToken.getUserName();
    Long woId = woInsideDTO.getWoId();
    String comment = woInsideDTO.getComment();
    String role = woInsideDTO.getRole();
    return acceptWoCommon(username, woId, comment, role);
  }

  @Override
  public ResultInSideDto acceptWoCommon(String username, Long woId, String comment, String role)
      throws Exception {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    try {
      List<UsersInsideDto> listUser = userRepository.getListUserDTOByuserName(username);
      Map<String, String> mapConfigProperty = commonRepository.getConfigProperty();
      if (listUser == null || listUser.isEmpty()) {
        throw new Exception(I18n.getLanguage("wo.userNameNotExists"));
      }
      if (woId == null) {
        throw new Exception(I18n.getLanguage("wo.WoIdIsNotNull"));
      }
      WoInsideDTO wo = woRepository.findWoByIdNoWait(woId);
      if (wo == null) {
        throw new Exception(I18n.getLanguage("wo.woIsNotExists"));
      }
      if (StringUtils.isNotNullOrEmpty(comment) && comment.length() > 1000) {
        throw new Exception(I18n.getLanguage("wo.CommentAcceptNotToLongThan1000Char"));
      }
      if (WO_MASTER_CODE.WO_FT.equals(role)) {
        UsersInsideDto usersFtDto = listUser.get(0);
        String nationCode = getNationFromUnit(usersFtDto.getUnitId());
        if (wo.getFtId() == null || !wo.getFtId().equals(usersFtDto.getUserId())) {
          throw new Exception(I18n.getLanguage("wo.UserIsNotHavePermissionForWO"));
        }
        // Check wo status
        if (!wo.getStatus().equals(Long.valueOf(WO_STATUS.DISPATCH))) {
          throw new Exception(
              I18n.getLanguage("wo.UnableToAcceptWOIfStateIs") + " " + convertWoStatus(
                  wo.getStatus()));
        }
        WoInsideDTO parent = null;
        if (wo.getParentId() != null) {
          parent = woRepository.findWoByIdNoOffset(wo.getParentId());
        }
        if (parent != null && parent.getStatus().equals(Long.valueOf(WO_STATUS.REJECT))) {
          throw new Exception(I18n.getLanguage("wo.UnableToAcceptWOWhenParentReject"));
        }
        Date date = new Date();
        wo.setFtAcceptedTime(date);
        updateParentStatus(wo.getParentId(), Long.valueOf(WO_STATUS.INPROCESS), "", usersFtDto, "",
            wo.getWoId());
        updateWoAndHistory(wo, usersFtDto, comment, WO_STATUS.INPROCESS, date);
        // goi sang spm thong bao FT tiep nhan wo
        UnitDTO unit = unitRepository.findUnitById(usersFtDto.getUnitId());
        String commentTT =
            usersFtDto.getUsername() + " : " + DateUtil.date2ddMMyyyyHHMMss(new Date()) + " "
                + I18n.getLanguage("wo.ftAcceptWo") + " " + wo.getWoCode() + ":" + comment;
        ResultDTO resTT = null;
        try {
          if (WO_MASTER_CODE.WO_SPM.equals(wo.getWoSystem()) || INSERT_SOURCE.SPM_VTNET
              .equals(wo.getWoSystem())) {
            resTT = updateStatusTT(null, wo, null, null, unit, usersFtDto, commentTT,
                "UPDATE", "ACCEPT", null);
          } else if (checkProperty(mapConfigProperty, String.valueOf(wo.getWoTypeId()),
              Constants.AP_PARAM.WO_TYPE_XLSCVT)) {
            resTT = updateStatus_SCVT(null, wo, null, null, unit, usersFtDto, commentTT,
                "UPDATE", "ACCEPT", null);
          }
          if (resTT != null && !RESULT.SUCCESS.equals(resTT.getKey())) {
            throw new Exception(": resultTT.getKey() :" + resTT.getMessage());
          }
        } catch (Exception e) {
          log.error(e.getMessage(), e);
          throw new Exception(I18n.getLanguage("wo.errCommunicateSPM") + " " + e.getMessage());
        }
        //update MR status
        if (WO_MASTER_CODE.WO_MR.equals(wo.getWoSystem())) {
          try {
            // chi thuc hien goi MR voi cac loai cong viec khong thuoc danh sach ben duoi
            if (!checkProperty(mapConfigProperty, String.valueOf(wo.getWoTypeId()),
                Constants.AP_PARAM.WO_TYPE_NOT_CALL_MR)) {
              ResultDTO res = mrCategoryProxy.updateMrStatus(null, String.valueOf(woId));
              if (!RESULT.SUCCESS.equals(res.getKey())) {
                throw new Exception(res.getMessage());
              }
            }
          } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new Exception(I18n.getLanguage("wo.errComunicateMR") + ":" + e.getMessage());
          }
        }
//        updateParentStatus(wo.getParentId(), wo.getStatus(), "", usersFtDto, "");
        try {
          // thuc hien goi sang kho tang tai san
          if (checkProperty(mapConfigProperty, String.valueOf(wo.getWoTypeId()),
              Constants.AP_PARAM.WO_TYPE_QLTS_NC)
              || checkProperty(mapConfigProperty, String.valueOf(wo.getWoTypeId()),
              Constants.AP_PARAM.WO_TYPE_QLTS_NC_THUE)
              || checkProperty(mapConfigProperty, String.valueOf(wo.getWoTypeId()),
              Constants.AP_PARAM.WO_TYPE_QLTS_NC_UCTT)
              || checkProperty(mapConfigProperty, String.valueOf(wo.getWoTypeId()),
              Constants.AP_PARAM.WO_TYPE_QLTS_NC_UCTT_THUE)
              || checkProperty(mapConfigProperty, String.valueOf(wo.getWoTypeId()),
              Constants.AP_PARAM.WO_TYPE_QLTS_HC)
              || checkProperty(mapConfigProperty, String.valueOf(wo.getWoTypeId()),
              Constants.AP_PARAM.WO_TYPE_QLTS_THUHOI)) {
            String checkKtts = checkAcceptUCTT(wo, mapConfigProperty);
            if (StringUtils.isNotNullOrEmpty(checkKtts)) {
              throw new Exception("[GNOC]: " + checkKtts);
            }
            if (!checkProperty(mapConfigProperty, String.valueOf(wo.getWoTypeId()),
                Constants.AP_PARAM.WO_TYPE_QLTS_HC)
                && !checkProperty(mapConfigProperty, String.valueOf(wo.getWoTypeId()),
                Constants.AP_PARAM.WO_TYPE_QLTS_THUHOI)) {
              ResultInSideDto dto = callKTTS(wo);
              if (!RESULT.SUCCESS.equals(dto.getKey())) {
                throw new Exception("[KTTS]: " + dto.getMessage());
              }
            }
          }
          // luong dieu chuyen
          else if (checkProperty(mapConfigProperty, String.valueOf(wo.getWoTypeId()),
              Constants.AP_PARAM.WO_TYPE_QLTS_DC_HC)) {
            // goi sang thuc hien dieu chuyen ha cap
            List<WoInsideDTO> lstWo = woRepository
                .getListWoBySystemOtherCode(wo.getWoSystemId(), null);
            WoInsideDTO woUp = null;
            if (lstWo != null && lstWo.size() > 0) {
              for (WoInsideDTO o : lstWo) {
                if (checkProperty(mapConfigProperty, String.valueOf(o.getWoTypeId()),
                    Constants.AP_PARAM.WO_TYPE_QLTS_DC_NC)) {
                  woUp = o;
                  break;
                }
              }
            }
            if (woUp == null) {
              throw new Exception(I18n.getLanguage("wo.notExixtsWoUpgrade"));
            }
            CertificateRegistrationBO up = new CertificateRegistrationBO();
            up.setCertificateRegistrationCode(woUp.getWoCode());
            up.setCertificateRegistrationId(woUp.getWoId());
            up.setCertificateRegistrationType(Constants.AP_PARAM.WO_TYPE_NAME_DC_NC);
            up.setStationCode(woUp.getStationCode());
            up.setTitle(getTitleForKTTS(woUp));

            CertificateRegistrationBO down = new CertificateRegistrationBO();
            down.setCertificateRegistrationCode(wo.getWoCode());
            down.setCertificateRegistrationId(wo.getWoId());
            down.setCertificateRegistrationType(Constants.AP_PARAM.WO_TYPE_NAME_DC_HC);
            down.setStationCode(wo.getStationCode());
            down.setTitle(getTitleForKTTS(wo));
            // lay danh sach hang hoa tu wo nang
            WoMerchandiseInsideDTO merDto = new WoMerchandiseInsideDTO(null, woUp.getWoId(), null,
                null, null, null, null, null, null, null, null, null, null, null, null, null, null);
            List<WoMerchandiseInsideDTO> lstMer = woMerchandiseRepository
                .getListWoMerchandiseDTO(merDto);

            List<MerchandiseBO> lstMerChandise = new ArrayList<>();
            if (lstMerChandise != null) {
              for (WoMerchandiseInsideDTO o : lstMer) {
                MerchandiseBO i = new MerchandiseBO();
                i.setMerCode(o.getMerchandiseCode());
                i.setCount(o.getQuantity() != null ? o.getQuantity() : null);
                lstMerChandise.add(i);
              }
            }
            Kttsbo kttsResult = kttsVsmartPort
                .createAssetMoveCmdNation(up, down, lstMerChandise, usersFtDto.getStaffCode(),
                    nationCode);
            if (kttsResult != null && !RESULT.SUCCESS.equalsIgnoreCase(kttsResult.getStatus())) {
              String err = "";
              if (kttsResult.getListError() != null) {
                for (ErrorBO bo : kttsResult.getListError()) {
                  err = err + bo.getMerCode() + ":" + bo.getDescription() + ";";
                }
              }
              throw new Exception(
                  "[KTTS]:" + ("".equals(err) == false ? err : kttsResult.getErrorInfo()));
            }
          } else if (checkProperty(mapConfigProperty, String.valueOf(wo.getWoTypeId()),
              Constants.AP_PARAM.WO_TYPE_QLTS_DC_NC)) {
            List<WoInsideDTO> lstWo = woRepository
                .getListWoBySystemOtherCode(wo.getWoSystemId(), null);
            WoInsideDTO woDown = null;
            if (lstWo != null && lstWo.size() > 0) {
              for (WoInsideDTO o : lstWo) {
                if (checkProperty(mapConfigProperty, String.valueOf(o.getWoTypeId()),
                    Constants.AP_PARAM.WO_TYPE_QLTS_DC_HC)) {
                  woDown = o;
                  break;
                }
              }
            }
            if (woDown != null &&
                !woDown.getStatus().equals(Long.valueOf(WO_STATUS.CLOSED_FT)) &&
                !woDown.getStatus().equals(Long.valueOf(WO_STATUS.CLOSED_CD))) {
              throw new Exception(
                  I18n.getLanguage("wo.youMustCompletedOrClosedWoDownBefore") + ":" + woDown
                      .getWoCode());
            }
          }
          // luong bao mat
          else if (checkProperty(mapConfigProperty, String.valueOf(wo.getWoTypeId()),
              Constants.AP_PARAM.WO_TYPE_QLTS_BAO_MAT)) {
            WoMerchandiseInsideDTO merDto = new WoMerchandiseInsideDTO(null, wo.getWoId(), null,
                null, null, null, null, null, null, null, null, null, null, null, null, null, null);
            List<WoMerchandiseInsideDTO> lstMer = woMerchandiseRepository
                .getListWoMerchandiseDTO(merDto);
            List<EntityBO> lst = new ArrayList<>();
            for (WoMerchandiseInsideDTO o : lstMer) {
              EntityBO i = new EntityBO();
              i.setCount(o.getQuantity() != null ? o.getQuantity() : null);
              i.setMerCode(o.getMerchandiseCode());
              if (StringUtils.isNotNullOrEmpty(o.getSerial())) {
                i.setSerialNumber(o.getSerial());
                i.setCount(Double.valueOf("1"));
              }
              i.setCauseId(o.getCause() != null ? o.getCause() : null);
              lst.add(i);
            }

            CertificateRegistrationBO cer = new CertificateRegistrationBO();
            cer.setCertificateRegistrationCode(wo.getWoCode());
            cer.setCertificateRegistrationId(wo.getWoId());
            cer.setCertificateRegistrationType(Constants.AP_PARAM.WO_TYPE_NAME_BAO_MAT);
            cer.setStationCode(wo.getStationCode());
            cer.setTitle(getTitleForKTTS(wo));
            cer.setExecutor(usersFtDto.getStaffCode());

            Kttsbo kttsResult = kttsVsmartPort.createAssetLostNation(lst, cer, nationCode);
            if (kttsResult != null && !RESULT.SUCCESS.equalsIgnoreCase(kttsResult.getStatus())) {
              String err = "";
              if (kttsResult.getListError() != null) {
                for (ErrorBO bo : kttsResult.getListError()) {
                  err = err + bo.getMerCode() + ":" + bo.getDescription() + ";";
                }
              }
              throw new Exception(
                  "[KTTS]:" + ("".equals(err) == false ? err : kttsResult.getErrorInfo()));
            } else {
              SimpleDateFormat dfm = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
              String code = wo.getWoCode() + ";" + dfm.format(new Date());
              List<String> lstCode = new ArrayList<>();
              lstCode.add(code);
              closeWoCommon(lstCode, null);
            }
          } // neu la loai cong viec cung co cap sinh tu dong cong viec
          else if (checkProperty(mapConfigProperty, String.valueOf(wo.getWoTypeId()),
              Constants.AP_PARAM.WO_TYPE_XLSCC)) {
            prepareWoTypeXLSCC(wo, usersFtDto);
          }
        } catch (Exception e) {
          log.error(e.getMessage(), e);
          throw new Exception(e.getMessage());
        }
      } else if (WO_MASTER_CODE.WO_CD.equals(role)) {
        UsersInsideDto usersCdDto = listUser.get(0);
        // Check nhom dieu phoi
        if (checkCdRole(wo, username) != null) {
          return checkCdRole(wo, username);
        }
        // Check wo status
        if (!wo.getStatus().equals(Long.valueOf(WO_STATUS.UNASSIGNED))) {
          throw new Exception(
              I18n.getLanguage("wo.UnableToAcceptWOIfStateIs") + " " + convertWoStatus(
                  wo.getStatus()));
        }
        // Accept
        Date date = new Date();
        updateWoAndHistory(wo, usersCdDto, comment, WO_STATUS.ASSIGNED, date);
        // goi sang spm bao CD tiep nhan
        UnitDTO unit = unitRepository.findUnitById(usersCdDto.getUnitId());
        String commentTT =
            usersCdDto.getUsername() + " : " + DateUtil.date2ddMMyyyyHHMMss(new Date()) + " " + I18n
                .getLanguage("wo.cdAcceptWo") + " " + wo.getWoCode() + ":" + comment;
        ResultDTO resTT = null;
        try {
          if (WO_MASTER_CODE.WO_SPM.equals(wo.getWoSystem()) || INSERT_SOURCE.SPM_VTNET
              .equals(wo.getWoSystem())) {
            resTT = updateStatusTT(null, wo, null, null, unit, usersCdDto, commentTT, "UPDATE",
                "ACCEPT", null);
          } else if (checkProperty(mapConfigProperty, String.valueOf(wo.getWoTypeId()),
              Constants.AP_PARAM.WO_TYPE_XLSCVT)) {
            resTT = updateStatus_SCVT(null, wo, null, null, unit, usersCdDto, commentTT, "UPDATE",
                "ACCEPT", null);
          }
          if (resTT != null && !RESULT.SUCCESS.equals(resTT.getKey())) {
            throw new Exception(": resultSpm.getKey() :" + resTT.getMessage());
          }
        } catch (Exception e) {
          log.error(e.getMessage(), e);
          throw new Exception(I18n.getLanguage("wo.errCommunicateSPM") + " " + e.getMessage());
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw new RuntimeException(e.getMessage());
    }
    resultInSideDto.setKey(WS_RESULT.OK);
    resultInSideDto.setMessage(WS_RESULT.OK);
    resultInSideDto.setQuantitySucc(1);
    return resultInSideDto;
  }

  public ResultInSideDto closeWoCommon(List<String> listCode, String system) {
    ResultInSideDto result = new ResultInSideDto();
    try {
      for (String k : listCode) {
        String[] tmp = k.split(";");
        List<WoInsideDTO> lstWo = woRepository.getListWoBySystemOtherCode(tmp[0], system);
        SimpleDateFormat dfm = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = dfm.parse(tmp[1]);
        for (WoInsideDTO i : lstWo) {
          if (!i.getStatus().equals(Long.valueOf(WO_STATUS.CLOSED_CD))) {
            UsersEntity usersEntity = userRepository.getUserByUserIdCheck(i.getCreatePersonId());
            // update lich su tac dong
            i.setFinishTime(date);
            i.setLastUpdateTime(date);
            i.setResult(Long.valueOf(WO_RESULT.OK));
            if (tmp.length > 2) {
              // Lay cong viec cha
              updateParentStatus(i.getParentId(), Long.valueOf(WO_STATUS.CLOSED_CD), WO_RESULT.OK,
                  usersEntity.toDTO(), tmp[2], i.getWoId());
              updateWoAndHistory(i, usersEntity.toDTO(), "WO_Auto close" + ":" + tmp[2],
                  WO_STATUS.CLOSED_CD, new Date());
              i.setCommentComplete(i.getCommentComplete() + "/" + tmp[2]);
            } else {
              // Lay cong viec cha
              updateParentStatus(i.getParentId(), Long.valueOf(WO_STATUS.CLOSED_CD), WO_RESULT.OK,
                  usersEntity.toDTO(), "", i.getWoId());
              updateWoAndHistory(i, usersEntity.toDTO(), "WO_Auto close",
                  WO_STATUS.CLOSED_CD, new Date());
            }
            // Lay cong viec cha
//            if (tmp.length > 2) {
//              updateParentStatus(i.getParentId(), i.getStatus(), WO_RESULT.OK,
//                  usersEntity.toDTO(), tmp[2]);
//            } else {
//              updateParentStatus(i.getParentId(), i.getStatus(), WO_RESULT.OK,
//                  usersEntity.toDTO(), "");
//            }
            woRepository.updateWo(i);
          }
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      result.setKey(RESULT.FAIL);
      result.setId(0L);
      result.setMessage(I18n.getLanguage("wo.haveSomeErrWhenClose"));
      return result;
    }
    result.setId(1L);
    result.setKey(RESULT.SUCCESS);
    result.setMessage("OK");
    return result;
  }

  public void updateParentStatus(Long parentId, Long statusChanged, String idCmbResult,
      UsersInsideDto user, String comment, Long woId) {
    if (parentId != null) {
      WoInsideDTO parentWo = woRepository.findWoByIdNoOffset(parentId);
      WoInsideDTO wo = woRepository.findWoByIdNoOffset(woId);
      String commentParent = "";
      Date date = new Date();
      boolean checkOnly = true;
      List<WoInsideDTO> listAllChildWo = woRepository.getListWoChildByParentId(parentWo.getWoId());
      Long statusChildMin = wo.getStatus();
      int count = 0;
      for (int i = 0; i < listAllChildWo.size(); i++) {
        Long statusChild = listAllChildWo.get(i).getStatus();
        if (statusChild.equals(Long.valueOf(WO_STATUS.PENDING))) {
          statusChild = Long.valueOf(WO_STATUS.INPROCESS);
        }
        if (statusChildMin.compareTo(statusChild) >= 0) {
          count++;
        }
      }
      if (count > 1) {
        checkOnly = false;
      }
      if (statusChildMin.compareTo(statusChanged) < 0
          && statusChildMin.compareTo(Long.valueOf(WO_STATUS.REJECT)) > 0
          && parentWo.getStatus().compareTo(statusChildMin) <= 0 && checkOnly) {
        if (statusChanged.equals(Long.valueOf(WO_STATUS.CLOSED_CD))) {
          if (idCmbResult != null) {
            if (WO_RESULT.NOK_CLOSE.equals(idCmbResult)) {
              parentWo.setResult(Long.valueOf(WO_RESULT.NOK_CLOSE));
            } else if (WO_RESULT.OK.equals(idCmbResult)) {
              parentWo.setResult(Long.valueOf(WO_RESULT.OK));
            }
          } else {
            parentWo.setResult(Long.valueOf(WO_RESULT.OK));
          }
          parentWo.setFinishTime(date);
          updateWoAndHistory(parentWo, user, commentParent, WO_STATUS.CLOSED_CD, date);
        } else {
          updateWoAndHistory(parentWo, user, commentParent, String.valueOf(statusChanged),
              date);
        }
      }
    }
  }

  public void updateParentStatusOld(WoInsideDTO parentWo, Long statusChanged, String idCmbResult,
      UsersInsideDto user, String comment) {
    UserToken userToken = ticketProvider.getUserToken();
    Double offsetFromUser = userRepository.getOffsetFromUser(userToken.getUserID());
    String commentParent = "";
    Date date = new Date();
    List<WoInsideDTO> listChildWo = new ArrayList<>();
    WoInsideDTO dtoParentSearch = new WoInsideDTO();
    dtoParentSearch.setParentId(parentWo.getWoId());
    dtoParentSearch.setUserId(user.getUserId());
    dtoParentSearch.setOffSetFromUser(offsetFromUser);
    List<WoInsideDTO> listAllChildWo = woRepository.getListDataSearch1(dtoParentSearch);
    for (WoInsideDTO woNoClosed : listAllChildWo) {
      Long status = woNoClosed.getStatus();
      if (status.compareTo(statusChanged) < 0
          && !status.equals(Long.valueOf(WO_STATUS.DRAFT))
          && !status.equals(Long.valueOf(WO_STATUS.PENDING))) {
        listChildWo.add(woNoClosed);
      }
    }
    if (listChildWo != null && listChildWo.size() == 0 && WO_STATUS.CLOSED_CD
        .equals(String.valueOf(statusChanged))) {
      WoInsideDTO dtoParentSearch2 = new WoInsideDTO();
      dtoParentSearch2.setParentId(parentWo.getWoId());
      dtoParentSearch2.setUserId(user.getUserId());
      dtoParentSearch2.setStatus(Long.valueOf(WO_STATUS.CLOSED_CD));
      dtoParentSearch2.setOffSetFromUser(offsetFromUser);
      List<WoInsideDTO> listAllChildWo2 = woRepository.getListDataSearch1(dtoParentSearch2);
      boolean checkNok = false;
      boolean checkOk = true;
      for (WoInsideDTO eWo : listAllChildWo2) {
        if (WO_RESULT.NOK_CLOSE.equals(String.valueOf(eWo.getResult()))) {
          checkNok = true;
          checkOk = false;
        } else if (WO_RESULT.NOK_DISPATCH.equals(String.valueOf(eWo.getResult()))) {
          checkOk = false;
        }
      }

      if (checkNok) {
        parentWo.setResult(Long.valueOf(WO_RESULT.NOK_CLOSE));
      } else if (checkOk) {
        parentWo.setResult(Long.valueOf(WO_RESULT.OK));
      }
      parentWo.setFinishTime(date);
      parentWo.setLastUpdateTime(date);
      updateWoAndHistory(parentWo, user, commentParent, String.valueOf(statusChanged), date);
      parentWo.setStatus(Long.valueOf(WO_STATUS.CLOSED_CD));
    } else if (WO_STATUS.INPROCESS.equals(String.valueOf(statusChanged))
        && listChildWo.size() == 0) {
      updateWoAndHistory(parentWo, user, commentParent, String.valueOf(statusChanged), date);
    } else if (WO_STATUS.CLOSED_FT.equals(String.valueOf(statusChanged))
        && listChildWo.size() == 0) {
      updateWoAndHistory(parentWo, user, commentParent, String.valueOf(statusChanged), date);
    } else if (WO_STATUS.DISPATCH.equals(String.valueOf(statusChanged))
        && listChildWo.size() == 0
        //xuat lai thi ko cap nhat trang thai wo
        && parentWo.getStatus().compareTo(Long.valueOf(WO_STATUS.DISPATCH)) < 0) {
      boolean checkParentDispatch = true;
      for (WoInsideDTO eChildWo : listChildWo) {
        if (!WO_STATUS.DISPATCH.equals(String.valueOf(eChildWo.getStatus()))
            && !WO_STATUS.ACCEPT.equals(String.valueOf(eChildWo.getStatus()))
            && !WO_STATUS.CLOSED_CD.equals(String.valueOf(eChildWo.getStatus()))) {
          checkParentDispatch = false;
        }
      }
      if (checkParentDispatch) {
        updateWoAndHistory(parentWo, user, commentParent, WO_STATUS.DISPATCH, date);
      }
    } else {
      boolean checkChangeParentStatus = true;
      // Check change status
      for (WoInsideDTO eChildWo : listChildWo) {
        if (!statusChanged.equals(eChildWo.getStatus())) {
          checkChangeParentStatus = false;
        }
      }
      if (checkChangeParentStatus) {
        updateWoAndHistory(parentWo, user, commentParent, WO_STATUS.DISPATCH, date);
        if (!(WO_STATUS.DISPATCH.equals(String.valueOf(statusChanged))
            && parentWo.getStatus().compareTo(Long.valueOf(WO_STATUS.DISPATCH)) < 0)) {
          parentWo.setStatus(statusChanged);
        }
      }
      // Check change result
      if (idCmbResult != null) {
        if (WO_RESULT.NOK_CLOSE.equals(idCmbResult)) {
          parentWo.setResult(Long.valueOf(WO_RESULT.NOK_CLOSE));
          parentWo.setFinishTime(date);
        } else {
          boolean checkParentOk = true;
          boolean checkParentNokReDispatch = true;
          boolean change = true;
          for (WoInsideDTO eChildWo : listChildWo) {
            if (eChildWo.getResult() == null) {
              change = false;
            } else if (WO_RESULT.NOK_CLOSE
                .equals(String.valueOf(eChildWo.getResult()))) {
              parentWo.setResult(Long.valueOf(WO_RESULT.NOK_CLOSE));
              parentWo.setFinishTime(date);
            } else if (WO_RESULT.NOK_DISPATCH
                .equals(String.valueOf(eChildWo.getResult()))) {
              checkParentOk = false;
            } else if (WO_RESULT.OK.equals(String.valueOf(eChildWo.getResult()))) {
              checkParentNokReDispatch = false;
            }
          }
          if (change && checkParentNokReDispatch) {
            parentWo.setResult(Long.valueOf(WO_RESULT.NOK_DISPATCH));
            parentWo.setFinishTime(date);
          }
          if (change && checkParentOk) {
            parentWo.setResult(Long.valueOf(WO_RESULT.OK));
            parentWo.setFinishTime(date);
          }
        }
      }
      parentWo.setLastUpdateTime(date);
    }
    woRepository.updateWo(parentWo);
  }

  public ResultInSideDto callKTTS(WoInsideDTO woInsideDTO) {
    ResultInSideDto result = new ResultInSideDto();
    try {
      //lay thong tin nhan vien can goi dien
      if (woInsideDTO != null) {
        WoTypeInsideDTO woTypeInsideDTO = woTypeBusiness
            .findWoTypeById(woInsideDTO.getWoTypeId());
        String typeCode = woTypeInsideDTO.getWoTypeCode();
        UsersInsideDto usersInsideDto = userRepository.getUserByUserIdCheck(woInsideDTO.getFtId())
            .toDTO();
        String nationCode = getNationFromUnit(usersInsideDto.getUnitId());
        CertificateRegistrationBO cer = new CertificateRegistrationBO();
        cer.setCertificateRegistrationCode(woInsideDTO.getWoCode());
        cer.setCertificateRegistrationId(woInsideDTO.getWoId());
        cer.setCertificateRegistrationType(typeCode);
        cer.setTitle(getTitleForKTTS(woInsideDTO));
        List<WoKTTSInfoDTO> lstKtts = woKTTSInfoRepository
            .getListWoKTTSInfoByWoId(woInsideDTO.getWoId());
        if (lstKtts != null && lstKtts.size() > 0) {
          WoKTTSInfoDTO ktts = lstKtts.get(0);
          cer.setWorkContent(ktts.getProcessActionName());
          cer.setWorkContentId(ktts.getProcessActionId());
          cer.setContractId(ktts.getContractId() != null ? ktts.getContractId() : null);
          // nguon thuc hien
          if (ktts.getProcessActionId() != null) {
            CatItemDTO item = catItemRepository.getCatItemById(ktts.getProcessActionId());
            if (item != null) {
              try {
                cer.setSourceType(Long.valueOf(item.getItemValue()));
              } catch (Exception e) {
                log.error(e.getMessage(), e);
              }
            }
          }
        }
        WoMerchandiseInsideDTO dtoSearch = new WoMerchandiseInsideDTO(null, woInsideDTO.getWoId(),
            null,
            null, null, null, null, null, null, null, null, null, null, null, null, null, null);
        List<WoMerchandiseInsideDTO> lstMer = woMerchandiseRepository
            .getListWoMerchandiseDTO(dtoSearch);
        List<MerchandiseOrderBO> lst = new ArrayList<>();
        if (lstMer != null && lstMer.size() > 0) {
          for (WoMerchandiseInsideDTO i : lstMer) {
            MerchandiseOrderBO bo = new MerchandiseOrderBO();
            bo.setMerCode(i.getMerchandiseCode());
            bo.setAmount(i.getQuantity());
            lst.add(bo);
          }
        }
        Kttsbo res = kttsVsmartPort
            .createOrderExportNation(woInsideDTO.getWarehouseCode(), usersInsideDto.getStaffCode(),
                woInsideDTO.getStationCode(), woInsideDTO.getConstructionCode(), lst, cer,
                nationCode);
        if (res != null && res.getStatus() != null && RESULT.SUCCESS
            .equalsIgnoreCase(res.getStatus())) {
          result.setKey(RESULT.SUCCESS);
          return result;
        } else {
          result.setKey(RESULT.FAIL);
          result.setMessage(res != null ? res.getErrorInfo() : "");
          return result;
        }
      } else {
        result.setKey(RESULT.FAIL);
        result.setMessage(I18n.getLanguage("wo.woNotExist"));
        return result;
      }
    } catch (Exception e) {
      result.setKey(RESULT.FAIL);
      result.setMessage(e.getMessage());
      log.error(e.getMessage(), e);
    }
    return result;
  }

  public String getTitleForKTTS(WoInsideDTO woInsideDTO) {
    String res = woInsideDTO.getWoContent();
    if (woInsideDTO.getCdAssignId() != null) {
      UsersInsideDto usersInsideDto = userRepository
          .getUserByUserIdCheck(woInsideDTO.getCdAssignId()).toDTO();
      if (usersInsideDto != null) {
        res = res + ";" + usersInsideDto.getStaffCode() + ";" + usersInsideDto.getMobile();
      }
    }
    return res;
  }

  public String checkAcceptUCTT(WoInsideDTO woInsideDTO, Map<String, String> mapCfg) {
    if (StringUtils.isStringNullOrEmpty(woInsideDTO.getStationCode())) {
      return I18n.getLanguage("wo.CDUpdateKtts");
    }
    if (!checkProperty(mapCfg, String.valueOf(woInsideDTO.getWoTypeId()),
        Constants.AP_PARAM.WO_TYPE_QLTS_HC)
        && !checkProperty(mapCfg, String.valueOf(woInsideDTO.getWoTypeId()),
        Constants.AP_PARAM.WO_TYPE_QLTS_THUHOI)) {
      if (StringUtils.isStringNullOrEmpty(woInsideDTO.getConstructionCode())) {
        return I18n.getLanguage("wo.CDUpdateKtts");
      }
    }
    if (StringUtils.isStringNullOrEmpty(woInsideDTO.getWarehouseCode())) {
      return I18n.getLanguage("wo.CDUpdateKtts");
    }
    WoMerchandiseInsideDTO woMerchandiseInsideDTO = new WoMerchandiseInsideDTO();
    woMerchandiseInsideDTO.setWoId(woInsideDTO.getWoId());
    List<WoMerchandiseInsideDTO> lst = woMerchandiseRepository
        .getListWoMerchandiseDTO(woMerchandiseInsideDTO);
    if (lst == null || lst.isEmpty()) {
      return I18n.getLanguage("wo.CDUpdateKtts");
    }
    return "";
  }

  @Override
  public ResultInSideDto dispatchWoFromWeb(WoInsideDTO woInsideDTO) throws Exception {
    UserToken userToken = ticketProvider.getUserToken();
    String username = userToken.getUserName();
    String ftName = woInsideDTO.getFtName();
    Long woId = woInsideDTO.getWoId();
    String comment = woInsideDTO.getComment();
    return dispatchWo(username, ftName, woId, comment);
  }

  @Override
  public ResultInSideDto dispatchWo(String username, String ftName, Long woId, String comment)
      throws Exception {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    List<UsersInsideDto> listUserCd = userRepository.getListUserDTOByuserName(username);
    Map<String, String> mapConfigProperty = commonRepository.getConfigProperty();
    if (listUserCd == null || listUserCd.isEmpty()) {
      throw new Exception("Username " + username + " " + I18n.getLanguage("wo.NotExistsOnSystem"));
    } else {
      UsersInsideDto cd = listUserCd.get(0);
      List<UsersInsideDto> listUserFt = userRepository.getListUserDTOByuserName(ftName);
      if (listUserFt == null || listUserFt.isEmpty()) {
        throw new Exception("Username " + ftName + " " + I18n.getLanguage("wo.NotExistsOnSystem"));
      } else {
        UsersInsideDto ft = listUserFt.get(0);
        if (woId == null) {
          throw new Exception(I18n.getLanguage("wo.WoIdIsNotNull"));
        }
        WoInsideDTO wo = woRepository.findWoByIdNoOffset(woId);
        if (wo == null) {
          throw new Exception(I18n.getLanguage("wo.woIsNotExists"));
        }
        // Check nhom dieu phoi
        if (checkCdRole(wo, username) != null) {
          return checkCdRole(wo, username);
        }
        // Check dispatch role
        String status = String.valueOf(wo.getStatus());
        boolean checkFtReject = false;
        if (status.equals(WO_STATUS.REJECT) && !DataUtil.isNullOrZero(wo.getFtId())) {
          checkFtReject = true;
        }
        if (!status.equals(WO_STATUS.ASSIGNED)
            && !status.equals(WO_STATUS.UNASSIGNED)
            && !status.equals(WO_STATUS.DISPATCH)
            && !status.equals(WO_STATUS.ACCEPT)
            && !status.equals(WO_STATUS.INPROCESS)
            && !checkFtReject) {
          throw new Exception(I18n.getLanguage("wo.NotAssignWoWhenStatusIs") + " "
              + convertWoStatus(wo.getStatus()));
        }
        // Dispatch
        Date date = new Date();
        String commentFinal = StringUtils.isStringNullOrEmpty(comment) ?
            I18n.getLanguage("wo.AssignedToUser") + ftName : comment;
        wo.setFtId(ft.getUserId());
        wo.setCdAssignId(cd.getUserId());
        String statusNew = WO_STATUS.DISPATCH;
        if (status.equals(WO_STATUS.ACCEPT)) {
          statusNew = WO_STATUS.ACCEPT;
        } else if (status.equals(WO_STATUS.INPROCESS)) {
          statusNew = WO_STATUS.INPROCESS;
        }

        // neu nhan vien giao viec la CD va loai cong viec khong phai loai KTTS thi chuyen dang xu ly luon
        if (cd.getUserId().equals(ft.getUserId()) && !checkProperty(mapConfigProperty,
            String.valueOf(wo.getWoTypeId()), Constants.AP_PARAM.WO_TYPE_QLTS)) {
          statusNew = WO_STATUS.INPROCESS;
//          if (checkProperty(mapConfigProperty, String.valueOf(wo.getWoTypeId()),
//              Constants.AP_PARAM.WO_TYPE_XLSCC)) {
//            prepareWoTypeXLSCC(wo, ft);
//          }
        }
        // chuyen sang SPM da giao cho FT
        UnitDTO unit = unitRepository.findUnitById(cd.getUnitId());
        String commentTT =
            "CD: " + cd.getUsername() + ": " + DateUtil.date2ddMMyyyyHHMMss(new Date())
                + " " + I18n.getLanguage("wo.cdDispatchWo") + " FT:" + ft.getUsername()
                + " Mobile: " + ft.getMobile() + " : " + comment;
        ResultDTO resTT = null;
        try {
          if (WO_MASTER_CODE.WO_SPM.equals(wo.getWoSystem()) || INSERT_SOURCE.SPM_VTNET
              .equals(wo.getWoSystem())) {
            resTT = updateStatusTT(null, wo, null, null, unit, cd, commentTT,
                "UPDATE", "DISPATCH", null);
          } else if (checkProperty(mapConfigProperty, String.valueOf(wo.getWoTypeId()),
              Constants.AP_PARAM.WO_TYPE_XLSCVT)) {
            resTT = updateStatus_SCVT(null, wo, null, null, unit, cd,
                commentTT, "UPDATE", "DISPATCH", null);
          }
          if (resTT != null && !RESULT.SUCCESS.equals(resTT.getKey())) {
            throw new Exception(": resultSpm.getKey() :" + resTT.getMessage());
          }
        } catch (Exception e) {
          log.error(e.getMessage(), e);
          throw new Exception(I18n.getLanguage("wo.errCommunicateSPM") + " " + e.getMessage());
        }
        updateWoAndHistory(wo, cd, commentFinal, statusNew, date);
      }
    }
    resultInSideDto.setKey(RESULT.SUCCESS);
    resultInSideDto.setQuantitySucc(1);
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto rejectWoFromWeb(WoInsideDTO woInsideDTO) throws Exception {
    UserToken userToken = ticketProvider.getUserToken();
    String username = userToken.getUserName();
    Long woId = woInsideDTO.getWoId();
    String comment = woInsideDTO.getComment();
    String role = woInsideDTO.getRole();
    return rejectWo(username, woId, comment, role);
  }

  @Override
  public ResultInSideDto rejectWo(String username, Long woId, String comment, String role)
      throws Exception {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    List<UsersInsideDto> listUsers = userRepository.getListUserDTOByuserName(username);
    Map<String, String> mapConfigProperty = commonRepository.getConfigProperty();
    if (listUsers == null || listUsers.isEmpty()) {
      throw new Exception(I18n.getLanguage("wo.userNameNotExists"));
    } else {
      if (woId == null) {
        throw new Exception(I18n.getLanguage("wo.WoIdIsNotNull"));
      }
      WoInsideDTO wo = woRepository.findWoByIdNoOffset(woId);
      if (wo == null) {
        throw new Exception(I18n.getLanguage("wo.woIsNotExists"));
      }
      if (StringUtils.isStringNullOrEmpty(comment)) {
        throw new Exception(I18n.getLanguage("wo.ReasonRejectIsNotNull"));
      }
      if (WO_MASTER_CODE.WO_FT.equals(role)) {
        UsersInsideDto usersFtDto = listUsers.get(0);
        if (wo.getFtId() == null || !wo.getFtId().equals(usersFtDto.getUserId())) {
          throw new Exception(I18n.getLanguage("wo.UserIsNotHavePermissionForWO"));
        }
        if (StringUtils.isNotNullOrEmpty(comment) && comment.length() > 1000) {
          throw new Exception(I18n.getLanguage("wo.ReasonRejectNotToLongThan1000Char"));
        }
        if (!wo.getStatus().equals(Long.valueOf(WO_STATUS.DISPATCH))) {
          throw new Exception(
              I18n.getLanguage("wo.UnableToRejectWOIfStateIs") + " " + convertWoStatus(
                  wo.getStatus()));
        }
        Date date = new Date();
        // tra ve SPM FT tu choi
        TroublesDTO o = new TroublesDTO();
        UnitDTO unit = unitRepository.findUnitById(usersFtDto.getUnitId());
        o.setIsCheck("1");
        String commentTT =
            usersFtDto.getUsername() + " : " + DateUtil.date2ddMMyyyyHHMMss(new Date()) + " "
                + I18n
                .getLanguage("wo.ftRejectWo") + ":" + comment;
        ResultDTO resTT = null;
        try {
          if (WO_MASTER_CODE.WO_SPM.equals(wo.getWoSystem()) || INSERT_SOURCE.SPM_VTNET
              .equals(wo.getWoSystem())) {
            resTT = updateStatusTT(o, wo, null, null, unit, usersFtDto, commentTT, "UPDATE",
                "REJECT", null);
          } else if (checkProperty(mapConfigProperty, String.valueOf(wo.getWoTypeId()),
              Constants.AP_PARAM.WO_TYPE_XLSCVT)) {
            resTT = updateStatus_SCVT(o, wo, null, null, unit, usersFtDto, commentTT, "UPDATE",
                "REJECT", null);
          }
          if (resTT != null && !RESULT.SUCCESS.equals(resTT.getKey())) {
            throw new Exception(": resultSpm.getKey() :" + resTT.getMessage());
          }
        } catch (Exception e) {
          log.error(e.getMessage(), e);
          throw new Exception(I18n.getLanguage("wo.errCommunicateSPM") + " " + e.getMessage());
        }
        updateWoAndHistory(wo, usersFtDto, comment, WO_STATUS.REJECT, date);
        if (wo.getParentId() != null) {
          WoInsideDTO parent = woRepository.findWoByIdNoOffset(wo.getParentId());
          if (parent != null && !parent.getStatus()
              .equals(Long.valueOf(WO_STATUS.REJECT))) {
            WoHistoryInsideDTO parentHistory = new WoHistoryInsideDTO();
            parentHistory.setCdId(parent.getCdId());
            parentHistory.setComments(I18n.getLanguage("wo.WoMovedToRejectBecauseWoChild")
                + wo.getWoCode() + I18n.getLanguage("wo.rejectedBy") + usersFtDto.getUsername());
            parentHistory.setCreatePersonId(parent.getCreatePersonId());
            parentHistory.setFileName(parent.getFileName());
            parentHistory.setFtId(parent.getFtId());
            parentHistory.setNewStatus(Long.valueOf(WO_STATUS.REJECT));
            parentHistory.setOldStatus(parent.getStatus());
            parentHistory.setUpdateTime(date);
            parentHistory.setUserId(usersFtDto.getUserId());
            parentHistory.setUserName(usersFtDto.getUsername());
            parentHistory.setWoCode(parent.getWoCode());
            parentHistory.setWoContent(parent.getWoContent());
            parentHistory.setWoId(parent.getWoId());
            woHistoryRepository.insertWoHistory(parentHistory);
          }
        }
      } else if (WO_MASTER_CODE.WO_CD.equals(role)) {
        UsersInsideDto usersCdDto = listUsers.get(0);
        // Check nhom dieu phoi
        if (checkCdRole(wo, username) != null) {
          return checkCdRole(wo, username);
        }
        //ko cho user CD co quyen tu choi
        if (WO_MASTER_CODE.WO_SPM.equals(wo.getWoSystem()) || INSERT_SOURCE.SPM_VTNET
            .equals(wo.getWoSystem())) {
          throw new Exception(I18n.getLanguage("wo.youNotHavePerMissionReject"));
        }
        // Check reject role
        if (!wo.getStatus().equals(Long.valueOf(WO_STATUS.UNASSIGNED))
            && (!wo.getStatus().equals(Long.valueOf(WO_STATUS.REJECT)) ||
            DataUtil.isNullOrZero(wo.getFtId()))) {
          throw new Exception(
              I18n.getLanguage("wo.UnableToRejectWOIfStateIs") + " " + convertWoStatus(
                  wo.getStatus()));
        }

        // check loai Wo ko dc phep tu choi
        if (checkProperty(mapConfigProperty, String.valueOf(wo.getWoTypeId()),
            Constants.AP_PARAM.WO_TYPE_NOT_ALLOW_REJECT)) {
          throw new Exception(I18n.getLanguage("wo.woTypeNotAllowReject"));
        }

        // Reject
        Date date = new Date();
        wo.setFtId(null);
        wo.setCdAssignId(listUsers.get(0).getUserId());
        //cd tu choi
        TroublesDTO o = new TroublesDTO();
        UnitDTO unit = unitRepository.findUnitById(usersCdDto.getUnitId());
        o.setIsCheck("2");
        String commentTT =
            usersCdDto.getUsername() + " : " + DateUtil.date2ddMMyyyyHHMMss(new Date()) + " "
                + I18n
                .getLanguage("wo.cdRejectWo") + ":" + comment;
        ResultDTO resTT = null;
        try {
          if (WO_MASTER_CODE.WO_SPM.equals(wo.getWoSystem()) || INSERT_SOURCE.SPM_VTNET
              .equals(wo.getWoSystem())) {
            resTT = updateStatusTT(o, wo, null, null, unit, usersCdDto, commentTT, "CLOSED",
                "REJECT", null);
          } else if (checkProperty(mapConfigProperty, String.valueOf(wo.getWoTypeId()),
              Constants.AP_PARAM.WO_TYPE_XLSCVT)) {
            resTT = updateStatus_SCVT(o, wo, null, null, unit, usersCdDto, commentTT, "UPDATE",
                "REJECT", null);
          }
          if (resTT != null && !RESULT.SUCCESS.equals(resTT.getKey())) {
            throw new Exception(": resultSpm.getKey() :" + resTT.getMessage());
          }
        } catch (Exception e) {
          log.error(e.getMessage(), e);
          throw new Exception(I18n.getLanguage("wo.errCommunicateSPM") + " " + e.getMessage());
        }
        updateWoAndHistory(wo, usersCdDto, comment, WO_STATUS.REJECT, date);
      }
    }
    resultInSideDto.setKey(RESULT.SUCCESS);
    resultInSideDto.setMessage("OK");
    resultInSideDto.setQuantitySucc(1);
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto auditWoFromWeb(WoInsideDTO woInsideDTO) throws Exception {
    UserToken userToken = ticketProvider.getUserToken();
    String username = userToken.getUserName();
    String woCode = woInsideDTO.getWoCode();
    String auditResult = woInsideDTO.getAuditResult();
    String comment = woInsideDTO.getComment();
    return auditWo(username, woCode, auditResult, comment);
  }

  public ResultInSideDto auditWo(String username, String woCode, String result, String comment)
      throws Exception {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    WoInsideDTO wo = woRepository.getWoByWoCode(woCode);
    // ko dat giao lai WO luu lai lich su
    if (WO_MASTER_CODE.WO_NOK.equals(result)) {
      // thuc hien cap nhat lai end_time
      if (wo.getFinishTime() != null && wo.getEndTime() != null) {
        Long now = new Date().getTime();
        Long finish = wo.getFinishTime().getTime();
        Long end = wo.getEndTime().getTime();
        Long tmpOld = end - finish;
        Long tmpNew = now + tmpOld;
        Date dTmp = new Date(tmpNew);
        wo.setEndTime(dTmp);
        //set lai finish_time va result
        wo.setFinishTime(null);
        wo.setResult(null);
        woRepository.updateWo(wo);
      }
      WoUpdateStatusForm updateForm = new WoUpdateStatusForm();
      updateForm.setNewStatus(Long.valueOf(WO_STATUS.INPROCESS));
      updateForm.setReasonChange(I18n.getLanguage("wo.woAuditFail") + " " + comment);
      updateForm.setSystemChange("GNOC");
      updateForm.setUserChange(username);
      updateForm.setAuditFail(true);
      updateForm.setWoCode(woCode);
      ResultInSideDto res = changeStatusWoCommon(updateForm);
      // thuc hien nhan tin cho FT
      String smsContent = "Wo: " + wo.getWoCode() + " da bi user: " + username
          + " hau kiem danh gia khong dat. Ly do: " + comment + ". De nghi d/c kiem tra xu ly!";

      UsersInsideDto u = userRepository.getUserByUserIdCheck(wo.getFtId()).toDTO();
      sendMessages(smsContent, u, "GNOC_WO");

      if (RESULT.SUCCESS.equals(res.getKey())) {
        resultInSideDto.setKey(RESULT.SUCCESS);
        resultInSideDto.setMessage(WS_RESULT.OK);
        resultInSideDto.setQuantitySucc(1);
        return resultInSideDto;
      } else {
        resultInSideDto.setKey(RESULT.ERROR);
        resultInSideDto.setMessage(I18n.getLanguage("wo.haveSomeErrWhenAuditWo"));
        return resultInSideDto;
      }
    } else {
      UsersEntity us = userRepository.getUserByUserName(username);
      if (wo != null) {
        updateWoAndHistory(wo, us.toDTO(), comment, WO_STATUS.CLOSED_CD,
            new Date());
        resultInSideDto.setKey(RESULT.SUCCESS);
        resultInSideDto.setMessage(WS_RESULT.OK);
        resultInSideDto.setQuantitySucc(1);
        return resultInSideDto;
      }
    }
    resultInSideDto.setKey(RESULT.SUCCESS);
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto pendingWoFromWeb(WoInsideDTO woInsideDTO) throws Exception {
    UserToken userToken = ticketProvider.getUserToken();
    String user = userToken.getUserName();
    woInsideDTO = convertWoDate2VietNamDate(woInsideDTO,
        TimezoneContextHolder.getOffsetDouble());//tiennv them fix timezone
    String woCode = woInsideDTO.getWoCode();
    Date endPendingTime = woInsideDTO.getEndPendingTime();
    String system = woInsideDTO.getSystem();
    String reasonName = woInsideDTO.getReasonName();
    String reasonId = woInsideDTO.getReasonId();
    String customer = woInsideDTO.getCustomer();
    String phone = woInsideDTO.getPhone();
    return pendingWoCommon(null, woCode, endPendingTime, user, system, reasonName, reasonId,
        customer,
        phone, true, true);
  }

  public ResultInSideDto pendingWoCommon(VsmartUpdateForm updateForm, String woCode,
      Date endPendingTime, String user, String system, String reasonName, String reasonId,
      String customer, String phone, boolean callCC, boolean checkAllow) throws Exception {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    try {
      Date now = new Date();
      Boolean check = false;
      List<UsersInsideDto> listUs = userRepository.getListUserDTOByuserName(user);
      UsersInsideDto us = (listUs != null && !listUs.isEmpty()) ? listUs.get(0) : null;
      Map<String, String> mapConfigProperty = commonRepository.getConfigProperty();
      //tim kiem wo
      // bat buoc nhap wo_code
      if (StringUtils.isStringNullOrEmpty(woCode)) {
        resultInSideDto.setMessage(I18n.getLanguage("wo.WoCodeIsNotNull"));
        resultInSideDto.setKey(RESULT.FAIL);
        return resultInSideDto;
      } // bat buoc nhap thoi gian tam dong
      else if (endPendingTime == null) {
        resultInSideDto.setMessage(I18n.getLanguage("wo.endPendingTimeIsnotNull"));
        check = true;
      } // bat buoc nhap
      else if ((now.getTime() - endPendingTime.getTime()) >= 0) {
        resultInSideDto.setMessage(I18n.getLanguage("wo.pendingTimeIsLagerForNow"));
        check = true;
      }//bat buoc nhap user
      else if (StringUtils.isStringNullOrEmpty(user)) {
        resultInSideDto.setMessage(I18n.getLanguage("wo.userIsNotNull"));
        check = true;
      }// user phai ton tai tren GNOC
      else if (us == null) {
        resultInSideDto.setMessage(I18n.getLanguage("wo.usNotExitsOnGNOC"));
        check = true;
      } else if (StringUtils.isStringNullOrEmpty(system)) {
        resultInSideDto.setMessage(I18n.getLanguage("wo.systemNotExitsOnGNOC"));
        check = true;
      } // check nguyen nhan
      else if (StringUtils.isStringNullOrEmpty(reasonName)) {
        resultInSideDto.setMessage(I18n.getLanguage("wo.reasonIsNotNull"));
        check = true;
      }
      WoInsideDTO wo = woRepository.getWoByWoCode(woCode);
      if (wo == null) {
        resultInSideDto.setMessage(I18n.getLanguage("wo.pendingWoNotExist"));
        resultInSideDto.setKey(RESULT.FAIL);
        return resultInSideDto;
      }
      if (checkAllow && !checkWoType(wo.getWoTypeId(), 1L)) {
        resultInSideDto.setMessage(I18n.getLanguage("wo.pendingWoTypeIsNotAllowPending"));
        resultInSideDto.setKey(RESULT.FAIL);
        return resultInSideDto;
      }
      // <editor-fold defaultstate="collapsed" desc="Validate thong tin tam dong WO xlsc">
      if (checkProperty(mapConfigProperty, String.valueOf(wo.getWoTypeId()),
          Constants.AP_PARAM.WO_TYPE_XLSCVT) && updateForm != null) {
        // nhom giai phap
        if (StringUtils.isStringNullOrEmpty(updateForm.getSolutionGroupId()) || StringUtils
            .isStringNullOrEmpty(updateForm.getSolutionGroupName())) {
          resultInSideDto.setMessage(I18n.getLanguage("wo.solutionGroupIsNotNull"));
          check = true;
        }
        // loai tam dong
        if (StringUtils.isStringNullOrEmpty(updateForm.getPendingType())) {
          resultInSideDto.setMessage(I18n.getLanguage("wo.pendingTypeIsNotNull"));
          check = true;
        }
        // thoi gian du kien khac phuc
        if (StringUtils.isStringNullOrEmpty(updateForm.getEstimateTime())) {
          resultInSideDto.setMessage(I18n.getLanguage("wo.estimateTimeIsNotNull"));
          check = true;
        }
        if ("2".equals(updateForm.getPendingType())) { // tam dong ky thuat hen
          if (StringUtils.isStringNullOrEmpty(updateForm.getLongitude())) {
            resultInSideDto.setMessage(I18n.getLanguage("wo.LongitudeIsNotNull"));
            check = true;
          }
          if (StringUtils.isStringNullOrEmpty(updateForm.getLatitude())) {
            resultInSideDto.setMessage(I18n.getLanguage("wo.latitudeIsNotNull"));
            check = true;
          }
          if (StringUtils.isStringNullOrEmpty(updateForm.getCellService())) {
            resultInSideDto.setMessage(I18n.getLanguage("wo.cellServiceIsNotNull"));
            check = true;
          }
        }
        wo.setSolutionDetail(updateForm.getSolution());
        wo.setSolutionGroupId(updateForm.getSolutionGroupId());
        wo.setSolutionGroupName(updateForm.getSolutionGroupName());
        wo.setCellService(updateForm.getCellService());
        wo.setConcaveAreaCode(updateForm.getConcaveAreaCode());
        wo.setLongitude(updateForm.getLongitude());
        wo.setLatitude(updateForm.getLatitude());
        if (StringUtils.isNotNullOrEmpty(updateForm.getEstimateTime())) {
          wo.setEstimateTime(DateTimeUtils.convertStringToDate(updateForm.getEstimateTime()));
        }
      }
      // </editor-fold>
      if (check) {
        resultInSideDto.setKey(RESULT.FAIL);
        return resultInSideDto;
      }
      // luu file dinh kem
      if (updateForm != null) {
        updateFile(wo, us.getUserId(), updateForm.getListFileName(), updateForm.getFileArr());
      }
      // luu lich su tam dong
      updateWoAndHistory(wo, us, reasonName, WO_STATUS.PENDING, now);
      // thuc hien chuyen trang thai = 9 va cong so lan tam dong len 1
      wo.setStatus(Long.valueOf(WO_STATUS.PENDING));
      Long numPending = wo.getNumPending();
      numPending = numPending == null ? 1L : numPending + 1;
      wo.setNumPending(numPending);
      // luu them thoi gian hen va tinh lai end time
      Long timePending = endPendingTime.getTime() - now.getTime();
      Date endNew = new Date(wo.getEndTime().getTime() + timePending);
      wo.setEndTime(endNew);
      wo.setEndPendingTime(endPendingTime);
      wo.setLastUpdateTime(now);
      //luu them nguyen nhan sdt khach hang va thong tin khach hang
      WoPendingDTO woPending = new WoPendingDTO();
      woPending.setWoId(wo.getWoId());
      woPending.setCusPhone(phone);
      woPending.setCustomer(customer);
      woPending.setEndPendingTime(endPendingTime);
      woPending.setInsertTime(now);
      woPending
          .setReasonPendingId(StringUtils.isStringNullOrEmpty(reasonId) ? null
              : Long.parseLong(reasonId));
      woPending.setReasonPendingName(reasonName);
      if (updateForm != null) {
        woPending.setPendingType(StringUtils.isNotNullOrEmpty(updateForm.getPendingType()) ? Long
            .valueOf(updateForm.getPendingType()) : null);
      }
      UnitDTO unit = unitRepository.findUnitById(us.getUnitId());
      // cap nhat sang SPM
      String commentTT =
          user + ":" + DateUtil.date2ddMMyyyyHHMMss(new Date()) + " " + I18n
              .getLanguage("wo.pendingWo") + "_" + I18n.getLanguage("wo.reason") + ":" + reasonName
              + " " + I18n.getLanguage("wo.pendingTime") + ":" + DateUtil
              .date2ddMMyyyyString(endPendingTime);
      ResultDTO resTT = null;
      try {
        if (callCC) {
          if (WO_MASTER_CODE.WO_SPM.equals(wo.getWoSystem()) || INSERT_SOURCE.SPM_VTNET
              .equals(wo.getWoSystem())) {
            resTT = updateStatusTT(null, wo, woPending, null, unit, us, commentTT,
                "DEFERRED", "PENDING", null);
          } else if (checkProperty(mapConfigProperty, String.valueOf(wo.getWoTypeId()),
              Constants.AP_PARAM.WO_TYPE_XLSCVT)) {
            resTT = updateStatus_SCVT(null, wo, woPending, null, unit, us, commentTT,
                "DEFERRED", "PENDING", null);
          }
          if (resTT != null && !RESULT.SUCCESS.equals(resTT.getKey())) {
            throw new Exception(":" + resTT.getMessage());
          }
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
        throw new Exception(I18n.getLanguage("wo.ErrCallTT") + " " + e.getMessage());
      }
      woPendingRepository.insertWoPending(woPending);
      woRepository.updateWo(wo);
      resultInSideDto.setKey(RESULT.SUCCESS);
      resultInSideDto.setMessage("");
      resultInSideDto.setObject(wo);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw new Exception(e.getMessage());
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto updatePendingWoFromWeb(WoInsideDTO woInsideDTO) throws Exception {
    UserToken userToken = ticketProvider.getUserToken();
    woInsideDTO = convertWoDate2VietNamDate(woInsideDTO, TimezoneContextHolder.getOffsetDouble());
    String user = userToken.getUserName();
    String woCode = woInsideDTO.getWoCode();
    String comment = woInsideDTO.getComment();
    String system = woInsideDTO.getSystem();
    Date endPendingTime = woInsideDTO.getEndPendingTime();
    return updatePendingWoCommon(woCode, endPendingTime, user, comment, system, false);
  }

  @Override
  public ResultInSideDto updatePendingWoCommon(String woCode, Date endPendingTime, String user,
      String comment, String system, boolean callCC) throws Exception {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    try {
      Boolean check = false;
      if (StringUtils.isStringNullOrEmpty(woCode)) {
        resultInSideDto.setMessage(I18n.getLanguage("wo.WoCodeIsNotNull"));
        check = true;
      }
      WoInsideDTO wo = woRepository.getWoByWoCode(woCode);
      List<UsersInsideDto> listUs = userRepository.getListUserDTOByuserName(user);
      Date now = new Date();
      Map<String, String> mapConfigProperty = commonRepository.getConfigProperty();
      if (StringUtils.isStringNullOrEmpty(system)) {
        resultInSideDto.setMessage(I18n.getLanguage("wo.systemNotExitsOnGNOC"));
        check = true;
      }
      if (listUs == null || listUs.isEmpty()) {
        resultInSideDto.setMessage(I18n.getLanguage("wo.usNotExitsOnGNOC"));
        resultInSideDto.setKey(RESULT.FAIL);
        resultInSideDto.setIdValue(RESULT.FAIL);
        return resultInSideDto;
      }
      UsersInsideDto us = listUs.get(0);
      if (endPendingTime == null) {// neu ko cap nhat thoi gian <=> mo tam dung
        //neu endPendingTime rong thi cd tu choi tam hoan
        if (!wo.getStatus().equals(Long.parseLong(WO_STATUS.PENDING))) {
          resultInSideDto.setMessage(I18n.getLanguage("wo.onlyOpenPendingWhenWoIsPending"));
          check = true;
        }
        if (check) {
          resultInSideDto.setKey(RESULT.FAIL);
          return resultInSideDto;
        }
        //luu log lich su
        updateWoAndHistory(wo, us, comment, WO_STATUS.INPROCESS, now);
        // cap nhat lai wo
        List<WoPendingDTO> lst = woPendingRepository.getListWoPendingByWoId(wo.getWoId());
        WoPendingDTO woPending = lst.get(0);

        WoTypeTimeDTO woTypeTimeDTO = null;
        WoTypeTimeDTO woTypeTimeDTOCondition = new WoTypeTimeDTO();
        woTypeTimeDTOCondition.setWoTypeId(wo.getWoTypeId());
        List<WoTypeTimeDTO> lstTime = woTypeBusiness
            .getListWoTypeTimeDTO(woTypeTimeDTOCondition);
        if (lstTime != null && !lstTime.isEmpty()) {
          woTypeTimeDTO = lstTime.get(0);
        }

        wo.setStatus(Long.valueOf(WO_STATUS.INPROCESS));
        Date pendingDate = wo.getEndPendingTime();
        Long timePending = now.getTime() - pendingDate.getTime();

        //neu bi tu choi tam hoan thi bi tinh thoi gian
        if (endPendingTime == null && woTypeTimeDTO != null
            && woTypeTimeDTO.getWaitForApprovePending() != null
            && woTypeTimeDTO.getWaitForApprovePending().equals(1L)) {
          timePending = woPending.getInsertTime().getTime() - pendingDate.getTime();
        }
        if (timePending < 0) { // neu mo truoc han thi cap nhat lai end_time wo
          Date endDateNew = new Date(wo.getEndTime().getTime() + timePending);
          wo.setEndTime(endDateNew);
        }
        // xoa thoi gian tam dong
        wo.setEndPendingTime(null);
        // cap nhat wo_pending

        woPending.setOpenTime(now);
        woPending.setOpenUser(user);
        woPending.setDescription(comment);

        // cap nhat sang SPM
        UnitDTO unit = unitRepository.findUnitById(us.getUnitId());
        String commentTT = user + ":" + DateUtil.date2ddMMyyyyHHMMss(new Date()) + " " + I18n
            .getLanguage("wo.openPendingWo") + " :" + comment;
        ResultDTO resTT = null;
        if (callCC) {
          if ((WO_MASTER_CODE.WO_SPM.equals(wo.getWoSystem()) && !WO_MASTER_CODE.WO_SPM
              .equalsIgnoreCase(system)) || (INSERT_SOURCE.SPM_VTNET.equals(wo.getWoSystem())
              && !INSERT_SOURCE.SPM_VTNET.equalsIgnoreCase(system))) {
            resTT = updateStatusTT(null, wo, woPending, null, unit, us, commentTT, "QUEUE",
                "INPROCESS", null);
          } else if (checkProperty(mapConfigProperty, String.valueOf(wo.getWoTypeId()),
              Constants.AP_PARAM.WO_TYPE_XLSCVT)) {
            resTT = updateStatus_SCVT(null, wo, woPending, null, unit, us, commentTT,
                "QUEUE", "INPROCESS", null);
          }
          if (resTT != null && !RESULT.SUCCESS.equals(resTT.getKey())) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            resultInSideDto.setKey(RESULT.FAIL);
            resultInSideDto.setMessage(I18n.getLanguage("wo.ErrCallTT") + ":" + resTT.getMessage());
            return resultInSideDto;
          }
        }
        woRepository.updateWo(wo);
        woPendingRepository.updateWoPending(woPending);
      } else {  // cap nhat thoi gian tam dung
        if (StringUtils.isStringNullOrEmpty(comment)) {
          resultInSideDto.setMessage(I18n.getLanguage("wo.commentIsNotNull"));
          check = true;
        }
        if (check) {
          resultInSideDto.setKey(RESULT.FAIL);
          return resultInSideDto;
        }
        // cap nhat lai trang thai . update lai end time lui hoac tien
        Date pendingDateOld = wo.getEndPendingTime();
        if (pendingDateOld == null) {
          resultInSideDto.setIdValue(RESULT.FAIL);
          resultInSideDto.setKey(RESULT.FAIL);
          return resultInSideDto;
        }
        Long diffTime = endPendingTime.getTime() - pendingDateOld.getTime();
        //luu log lich su
        updateWoAndHistory(wo, us, I18n.getLanguage("wo.updatePendingTimeFrom") + ": "
            + DateUtil.date2ddMMyyyyHHMMss(pendingDateOld) + " " + I18n.getLanguage("wo.to") + ": "
            + DateUtil.date2ddMMyyyyHHMMss(endPendingTime) + I18n.getLanguage("wo.reason") + " : "
            + comment, WO_STATUS.PENDING, now);
        //cap nhat lai thoi gian end_time cua wo
        Date endDateNew = new Date(wo.getEndTime().getTime() + diffTime);
        wo.setEndTime(endDateNew);
        wo.setEndPendingTime(endPendingTime);
        //cap nhat lai end time ban ghi cuoi cung trong bang wo_pending
        List<WoPendingDTO> lst = woPendingRepository.getListWoPendingByWoId(wo.getWoId());
        WoPendingDTO woPending = lst.get(0);
        woPending.setEndPendingTime(endPendingTime);
        woPending.setDescription(comment);
        // cap nhat sang SPM
        UnitDTO unit = unitRepository.findUnitById(us.getUnitId());
        String commentTT = user + " : " + DateUtil.date2ddMMyyyyHHMMss(new Date()) + " " + I18n
            .getLanguage("wo.pendingWo") + "_" + comment;
        ResultDTO resTT = null;
        try {
          if (callCC) {
            if ((WO_MASTER_CODE.WO_SPM.equals(wo.getWoSystem()) && !WO_MASTER_CODE.WO_SPM
                .equalsIgnoreCase(system)) || (INSERT_SOURCE.SPM_VTNET.equals(wo.getWoSystem())
                && !INSERT_SOURCE.SPM_VTNET.equalsIgnoreCase(system))) {
              resTT = updateStatusTT(null, wo, woPending, null, unit, us, commentTT,
                  "" + "", "PENDING", null);
            } else if (checkProperty(mapConfigProperty, String.valueOf(wo.getWoTypeId()),
                Constants.AP_PARAM.WO_TYPE_XLSCVT)) {
              resTT = updateStatus_SCVT(null, wo, woPending, null, unit, us, commentTT,
                  "" + "", "PENDING", null);
            }
            if (resTT != null && !RESULT.SUCCESS.equals(resTT.getKey())) {
              TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
              throw new Exception(":" + resTT.getMessage());
            }
          }
        } catch (Exception e) {
          log.error(e.getMessage(), e);
          TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
          throw new Exception(I18n.getLanguage("wo.ErrCallSPM") + " " + e.getMessage());
        }
        woRepository.updateWo(wo);
        woPendingRepository.updateWoPending(woPending);
      }
      resultInSideDto.setKey(RESULT.SUCCESS);
      resultInSideDto.setMessage("");
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
      throw new Exception(e.getMessage());
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto approveWoFromWeb(WoInsideDTO woInsideDTO) throws Exception {
    UserToken userToken = ticketProvider.getUserToken();
    String username = userToken.getUserName();
    Long woId = woInsideDTO.getWoId();
    String comment = woInsideDTO.getComment();
    String action = woInsideDTO.getAction();
    String ftName = woInsideDTO.getFtName();
    return approveWoCommon(null, username, woId, comment, action, ftName);
  }

  @Override
  public ResultInSideDto updateStatusFromWeb(WoInsideDTO woInsideDTO) {
    UserToken userToken = ticketProvider.getUserToken();
    String userName = userToken.getUserName();
    woInsideDTO = convertWoDate2VietNamDate(woInsideDTO,
        TimezoneContextHolder.getOffsetDouble());//tiennv cap nhat timezone
    List<WoMaterialDeducteInsideDTO> lstDeducte = woInsideDTO.getLstDeducte();
    String reasonLv1Id = woInsideDTO.getReasonLv1Id();
    String reasonLv1Name = woInsideDTO.getReasonLv1Name();
    String reasonLv2Id = woInsideDTO.getReasonLv2Id();
    String reasonLv2Name = woInsideDTO.getReasonLv2Name();
    String reasonLv3Id = woInsideDTO.getReasonLv3Id();
    String reasonLv3Name = woInsideDTO.getReasonLv3Name();
    Boolean checkRefresh = woInsideDTO.getCheckRefresh();
    WoHistoryInsideDTO woHistoryInsideDTO = woInsideDTO.getWoHistoryInsideDTO();
    String comment = woInsideDTO.getComment();
    Boolean showMaterialInfo = woInsideDTO.getShowMaterialInfo();
    Boolean isShowReasonInfo = woInsideDTO.getIsShowReasonInfo();
    Boolean isShowReasonOverdueInfo = woInsideDTO.getIsShowReasonOverdueInfo();
    String action = woInsideDTO.getAction();
    //tiennv bo sung check NOC
    //
    if (Constants.WO_STATUS.CLOSED_FT.equals(String.valueOf(woInsideDTO.getStatus()))) {
      if (woInsideDTO.getWoSystem() != null && WO_MASTER_CODE.WO_TT
          .equalsIgnoreCase(woInsideDTO.getWoSystem())) {
        ResultDTO resFromTT = ttServiceProxy
            .checkAlarmNOC(woInsideDTO.getWoSystemId(),
                String.valueOf(woInsideDTO.getWoTypeId()));
        if (RESULT.SUCCESS.equalsIgnoreCase(resFromTT.getKey())) {
          if ("NOC".equals(resFromTT.getMessage())
              && StringUtils.isStringNullOrEmpty(resFromTT.getFinishTime())) {
            return new ResultInSideDto(null, RESULT.ERROR,
                I18n.getLanguage("wo.onlyCompleteWhenAlarmClear"));
          }
          if (StringUtils.isNotNullOrEmpty(resFromTT.getFinishTime())) {
            woInsideDTO
                .setClearTime(DateTimeUtils.convertStringToDate(resFromTT.getFinishTime()));
          }
        } else {
          return new ResultInSideDto(null, RESULT.ERROR,
              resFromTT.getMessage());
        }
      }
    }
    return updateStatusFW(woInsideDTO, lstDeducte, reasonLv1Id, reasonLv1Name, reasonLv2Id,
        reasonLv2Name, reasonLv3Id, reasonLv3Name, checkRefresh, woHistoryInsideDTO, userName,
        comment, showMaterialInfo, isShowReasonInfo, isShowReasonOverdueInfo, action);
  }

  public ResultInSideDto updateStatusFW(WoInsideDTO woInsideDTO,
      List<WoMaterialDeducteInsideDTO> lstDeducte,
      String reasonLv1Id, String reasonLv1Name, String reasonLv2Id, String reasonLv2Name,
      String reasonLv3Id, String reasonLv3Name, Boolean checkRefresh, WoHistoryInsideDTO woDtoHis,
      String userName, String comment, Boolean showMaterialInfo, Boolean isShowReasonInfo,
      Boolean isShowReasonOverdueInfo, String action) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    UserToken userToken = ticketProvider.getUserToken();
    try {
      //insert WO_MATERIAL_DEDUCTE
      WoInsideDTO wo = woRepository.findWoByIdNoWait(woInsideDTO.getWoId());
      Long oldStatus = wo.getStatus();
      //thuc hien cap nhat thong tin wo incident
      Map<String, String> mapConfigProperty = commonRepository.getConfigProperty();
      Boolean checkFinalWoTrouble = true;
      if (StringUtils.isNotNullOrEmpty(woInsideDTO.getWoSystemId())) {
        checkFinalWoTrouble = checkFinalWoFromTT(woInsideDTO.getWoSystemId(), mapConfigProperty);
      }
      // check trang thai
      if ((Long.valueOf(WO_STATUS.INPROCESS).equals(woInsideDTO.getStatus())
          && !wo.getStatus().equals(Long.valueOf(WO_STATUS.ACCEPT)))
          || (Long.valueOf(WO_STATUS.CLOSED_FT).equals(woInsideDTO.getStatus())
          && !wo.getStatus().equals(Long.valueOf(WO_STATUS.INPROCESS)))
          || (Long.valueOf(WO_STATUS.CLOSED_CD).equals(woInsideDTO.getStatus())
          && (!wo.getStatus().equals(Long.valueOf(WO_STATUS.CLOSED_FT))
          && !wo.getStatus().equals(Long.valueOf(WO_STATUS.INPROCESS))))
          || (Long.valueOf(WO_STATUS.DISPATCH).equals(woInsideDTO.getStatus())
          && !wo.getStatus().equals(Long.valueOf(WO_STATUS.CLOSED_FT)))) {
        resultInSideDto.setKey(RESULT.ERROR);
        resultInSideDto.setMessage(I18n.getLanguage("wo.woIsBusy"));
        return resultInSideDto;
      }
      // truong hop phe duyet WO SPM check trang thai hien tai phai la hoan thanh
      if (WO_MASTER_CODE.WO_SPM.equals(woInsideDTO.getWoSystem()) || INSERT_SOURCE.SPM_VTNET
          .equals(woInsideDTO.getWoSystem())) {
        if (checkProperty(mapConfigProperty, "1",
            Constants.WO_BUSINESS_CHECK.CHECK_STATUS_APPROVE_WO_SPM)) {
          if (woInsideDTO.getStatus().equals(Long.valueOf(WO_STATUS.CLOSED_CD))) {
            if (!wo.getStatus().equals(Long.valueOf(WO_STATUS.CLOSED_FT))) {
              resultInSideDto.setKey(RESULT.ERROR);
              resultInSideDto.setMessage(I18n.getLanguage("wo.statusApproveIsNotValid"));
              return resultInSideDto;
            }
          }
        }
      }

      // ko cho phep dong cac loai WO dc cau hinh ko cho dong
      if (Constants.WO_STATUS.CLOSED_CD.equals(String.valueOf(woInsideDTO.getStatus()))) {
        if (checkProperty(mapConfigProperty, String.valueOf(wo.getWoTypeId()),
            Constants.WO_BUSINESS_CHECK.CHECK_WO_TYPE_NOT_ALLOW_APPROVE)) {
          throw new Exception(I18n.getLanguage("wo.type.not.close.manual"));
        }
      }

      // <editor-fold defaultstate="collapsed" desc="Danh sach cot them moi ko muon cap nhat">
      woInsideDTO.setVibaLineCode(wo.getVibaLineCode());
      woInsideDTO.setNumAuditFail(wo.getNumAuditFail() != null ? wo.getNumAuditFail() : null);
      woInsideDTO.setCableTypeCode(wo.getCableTypeCode());
      woInsideDTO.setCableCode(wo.getCableCode());
      woInsideDTO.setVibaLineCode(wo.getVibaLineCode());
      woInsideDTO.setDistance(wo.getDistance());
      woInsideDTO.setAmiOneId(wo.getAmiOneId());
      woInsideDTO
          .setFtAcceptedTime((wo.getFtAcceptedTime()) == null ? null : wo.getFtAcceptedTime());
      woInsideDTO.setDeviceType(wo.getDeviceType());
      woInsideDTO.setDeviceTypeName(wo.getDeviceTypeName());
      woInsideDTO.setNumAutoCheck(wo.getNumAutoCheck() != null ? wo.getNumAutoCheck() : null);

      //tiennv cap nhat nang cap WO
      woInsideDTO.setNewFailureReason(wo.getNewFailureReason());
      woInsideDTO.setFailureReason(wo.getFailureReason());
      woInsideDTO.setNationCode(wo.getNationCode());
      woInsideDTO.setDeptCode(wo.getDeptCode());

      woInsideDTO.setSolutionGroupName(wo.getSolutionGroupName());
      woInsideDTO.setSolutionDetail(wo.getSolutionDetail());
      woInsideDTO.setLongitude(wo.getLongitude());
      woInsideDTO.setLatitude(wo.getLatitude());
      woInsideDTO.setConcaveAreaCode(wo.getConcaveAreaCode());
      woInsideDTO.setCellService(wo.getCellService());
      woInsideDTO.setReasonDetail(wo.getReasonDetail());
      woInsideDTO.setWoSystemOutId(wo.getWoSystemOutId());

      woInsideDTO.setNumPending(wo.getNumPending());

      if (woInsideDTO.getReasonInterference() == null && wo.getReasonInterference() != null) {
        woInsideDTO.setReasonInterference(wo.getReasonInterference());
      }
      // </editor-fold>
      wo = (WoInsideDTO) woRepository.updateObjectData(woInsideDTO, wo);
      // validate danh sach vat tu ko cung nhom
      if (lstDeducte != null && lstDeducte.size() > 0) {
        List<String> lstMaterialGroup = new ArrayList<>();
        for (WoMaterialDeducteInsideDTO material : lstDeducte) {
          if (StringUtils.isNotNullOrEmpty(material.getMaterialGroupCode())) {
            if (lstMaterialGroup.contains(material.getMaterialGroupCode())) {
              resultInSideDto.setKey(RESULT.ERROR);
              resultInSideDto.setMessage(I18n.getLanguage("wo.duplicateMaterialGroupCode"));
              return resultInSideDto;
            } else {
              lstMaterialGroup.add(material.getMaterialGroupCode());
            }
          }
        }
        ResultInSideDto rsDedute = woMaterialDeducteBusiness.putMaterialDeducte(lstDeducte);
        if (!RESULT.SUCCESS.equals(rsDedute.getKey())) {
          resultInSideDto.setKey(RESULT.ERROR);
          resultInSideDto.setMessage(I18n.getLanguage("wo.failPutMaterialDeduct"));
          return resultInSideDto;
        }
      } else if (woInsideDTO.getStatus().equals(Long.valueOf(WO_STATUS.CLOSED_FT))
          || woInsideDTO.getStatus()
          .equals(Long.valueOf(WO_STATUS.DISPATCH))) {  // neu dong ko co vat tu
        //thuc hien tru kho cu
        ResultInSideDto rsDedute = woMaterialDeducteBusiness
            .deleteMaterialDeducte(wo.getWoId(), wo.getFtId());
        if (!RESULT.SUCCESS.equals(rsDedute.getKey())) {
          resultInSideDto.setKey(RESULT.ERROR);
          resultInSideDto.setMessage(I18n.getLanguage("wo.failDelMaterialDeduct"));
          return resultInSideDto;
        }
      }
      // luu them thong tin thoi gian hoan thanh / comment hoan thanh
      if (woInsideDTO.getStatus().equals(Long.valueOf(WO_STATUS.CLOSED_FT))) {
        wo.setCommentComplete(comment);
        woInsideDTO.setCommentComplete(comment);
        wo.setCompletedTime(new Date());
        wo.setIsCompletedOnVsmart(0L);
        //ko cho hoan thanh khi nho ho tro
        if (wo.getNeedSupport() != null && wo.getNeedSupport().equals(1L)) {
          if (checkProperty(mapConfigProperty, "1",
              Constants.WO_BUSINESS_CHECK.COMPLETED_FIRST_NEED_SUPPORT)) {
            resultInSideDto.setKey(RESULT.ERROR);
            resultInSideDto.setMessage(I18n.getLanguage("wo.cannotCompleteWhenNeedSupport"));
            return resultInSideDto;
          }
        }
      } else if (woInsideDTO.getStatus()
          .equals(Long.valueOf(WO_STATUS.CLOSED_CD))) {  // truong hop CD la FT
        if (woInsideDTO.getCompletedTime() != null) {
          wo.setCompletedTime(new Date());
          wo.setIsCompletedOnVsmart(0L);
          wo.setCommentComplete(comment);
          woInsideDTO.setCommentComplete(comment);
        }
      }
      // thuc hien validate vat tu
      if (woInsideDTO.getStatus().equals(Long.valueOf(WO_STATUS.CLOSED_FT))) {
        List<WoMaterialDeducteInsideDTO> lst = woMaterialDeducteBusiness
            .getMaterialDeducteKeyByWO(wo.getWoId());
        String resValidateIM = null;
        if (lst != null && lst.size() > 0) { //tiennv commit code nang cap WO
          resValidateIM = woMaterialDeducteBusiness.validateMaterialCompleted(lst);
        }
        if (StringUtils.isNotNullOrEmpty(resValidateIM)) {
          resultInSideDto.setKey(RESULT.ERROR);
          resultInSideDto.setMessage(resValidateIM);
          return resultInSideDto;
        }
      }
      // goi sang NOC bao check cong xuat
      if (checkProperty(mapConfigProperty, String.valueOf(wo.getWoTypeId()),
          Constants.AP_PARAM.WO_TYPE_BD_VIBA)) {
        if (woInsideDTO.getStatus().equals(Long.valueOf(WO_STATUS.CLOSED_FT))) {
          try {
            ResultInSideDto resViba = checkVibaFromNoc(wo.getVibaLineCode(), wo.getStationCode(),
                null, 1L);
            if (!RESULT.SUCCESS.equals(resViba.getKey())) {
              resultInSideDto.setKey(RESULT.ERROR);
              resultInSideDto.setMessage(resViba.getMessage());
              return resultInSideDto;
            }
          } catch (Exception e) {
            log.error(e.getMessage(), e);
            resultInSideDto.setKey(RESULT.ERROR);
            resultInSideDto.setMessage("[GNOC]" + e.getMessage());
            return resultInSideDto;
          }
        }
      }
      // check cong suat tuyen viba
      if (woInsideDTO.getStatus().equals(Long.valueOf(WO_STATUS.CLOSED_CD))) {
        if (checkProperty(mapConfigProperty, String.valueOf(wo.getWoTypeId()),
            Constants.AP_PARAM.WO_TYPE_BDCD_TRAM)
            || checkProperty(mapConfigProperty, String.valueOf(wo.getWoTypeId()),
            Constants.AP_PARAM.WO_TYPE_DO_XANG_DAU)) {
          try {
            List<CpRequestParam> list = new ArrayList<>();
            if (checkProperty(mapConfigProperty, String.valueOf(wo.getWoTypeId()),
                Constants.AP_PARAM.WO_TYPE_DO_XANG_DAU)) {
              // thuc hien lay du lieu chi so dau chi so cuoi xang dau
              List<ObjKeyValue> lst = vsmartBusiness.getDataTestService(wo.getWoId());
              if (lst != null && lst.size() > 0) {
                for (ObjKeyValue i : lst) {
                  if (i.getRow() == 1) {
                    if (i.getCol() == 2) {
//                      CpRequestParam p = new CpRequestParam();
//                      p.setName("remain");
//                      p.setType("Number");
//                      p.setValue(i.getValue());
//                      list.add(p);
                    }
                    if (i.getCol() == 3) {
                      CpRequestParam p = new CpRequestParam();
                      p.setName("added");
                      p.setType("Number");
                      p.setValue(i.getValue());
                      list.add(p);
                    }
                  }
                }
              }
            }
            Result resChiPhi = portChiPhi
                .confirmSucceedWO(wo.getWoTypeId(), wo.getWoCode(), list);
            if (!resChiPhi.getStatus().equals(0L)) {
              resultInSideDto.setKey(RESULT.ERROR);
              resultInSideDto.setMessage("[CPWS] " + resChiPhi.getMessage());
              return resultInSideDto;
            }
          } catch (Exception e) {
            log.error(e.getMessage(), e);
            resultInSideDto.setKey(RESULT.ERROR);
            resultInSideDto.setMessage(e.getMessage());
            return resultInSideDto;
          }
        }
      }
      if (checkProperty(mapConfigProperty, String.valueOf(wo.getWoTypeId()),
          Constants.AP_PARAM.WO_TYPE_BD_VIBA)) {
        if (woInsideDTO.getStatus().equals(Long.valueOf(WO_STATUS.CLOSED_CD))) {
          try {
            ResultInSideDto resViba = checkVibaFromNoc(wo.getVibaLineCode(), wo.getStationCode(),
                wo.getCompletedTime(), 2L);
            if (!RESULT.SUCCESS.equals(resViba.getKey())) {
              resultInSideDto.setKey(RESULT.ERROR);
              resultInSideDto.setMessage(resViba.getMessage());
              return resultInSideDto;
            }
          } catch (Exception e) {
            log.error(e.getMessage(), e);
            resultInSideDto.setKey(RESULT.ERROR);
            resultInSideDto.setMessage("[GNOC]" + e.getMessage());
            return resultInSideDto;
          }
        }
      }
      //phe duyet wo xu ly can nhieu
      if (checkProperty(mapConfigProperty, String.valueOf(wo.getWoTypeId()),
          Constants.AP_PARAM.WO_TYPE_XU_LY_CAN_NHIEU)) {
        if (wo.getReasonInterference() != null && "1"
            .equals(String.valueOf(wo.getReasonInterference()))
            && woInsideDTO.getStatus().equals(Long.valueOf(WO_STATUS.CLOSED_CD))) {
          try {
            ResultInSideDto res = manualCreateWo(wo, 3L);
            // luu thong tin nguyen nhan hoan thanh wo can nhieu
            if (!RESULT.SUCCESS.equals(res.getKey())) {
              resultInSideDto.setKey(RESULT.ERROR);
              resultInSideDto.setMessage(res.getMessage());
              return resultInSideDto;
            }
          } catch (Exception e) {
            log.error(e.getMessage(), e);
            resultInSideDto.setKey(RESULT.ERROR);
            resultInSideDto.setMessage("[GNOC]" + e.getMessage());
            return resultInSideDto;
          }
        }
      }
      if (checkRefresh) {
        // set finish_time = clear_time neu co
        WoTroubleInfoDTO woTroubleInfo = null;
        if (WO_MASTER_CODE.WO_TT.equals(woInsideDTO.getWoSystem())) {
          woTroubleInfo = woTroubleInfoRepository.getWoTroubleInfoByWoId(woInsideDTO.getWoId());
          if (woTroubleInfo != null) {
            if (woTroubleInfo.getClearTime() != null) {
              wo.setFinishTime(woTroubleInfo.getClearTime());
              woInsideDTO.setFinishTime(woTroubleInfo.getClearTime());
            }
          }
        }
        if (("1".equals(woInsideDTO.getIsValidWoTrouble()) || woInsideDTO.getClearTime() != null)
            && WO_MASTER_CODE.WO_TT.equals(woInsideDTO.getWoSystem())) {
          updateWoTroubleInfo(woTroubleInfo != null ? woTroubleInfo : new WoTroubleInfoDTO(),
              woInsideDTO);
        }

        // kiem tra phe duyet neu la KTTS
        if (checkProperty(mapConfigProperty, String.valueOf(wo.getWoTypeId()),
            Constants.AP_PARAM.WO_TYPE_QLTS) && woInsideDTO.getResult() != null) {
          String nationCode = getNationFromUserId(wo.getFtId());
          Kttsbo resKtts = kttsVsmartPort.checkWorkOrderNation(wo.getWoId(), nationCode);
          if (resKtts != null && !RESULT.SUCCESS.equalsIgnoreCase(resKtts.getStatus())) {
            resultInSideDto.setKey(RESULT.ERROR);
            resultInSideDto.setMessage("[KTTS]:" + resKtts.getErrorInfo());
            return resultInSideDto;
          }
        }
        // phe duyet day sang TT
        if (woInsideDTO.getResult() != null
            && WO_MASTER_CODE.WO_TT.equals(woInsideDTO.getWoSystem()) && checkFinalWoTrouble) {
          if (woTroubleInfo != null) {
            closeToIncident(woInsideDTO, woTroubleInfo);
          }
        }
        // phe duyet WO di doi huy tram check tai san toi uu
        if (checkProperty(mapConfigProperty, String.valueOf(wo.getWoTypeId()),
            Constants.AP_PARAM.WO_TYPE_NIMS_HUY_TRAM)
            || checkProperty(mapConfigProperty, String.valueOf(wo.getWoTypeId()),
            Constants.AP_PARAM.WO_TYPE_NIMS_DI_DOI_TRAM)) {
          try {
            ResultDTO resCheckDiDoi;
            if (checkProperty(mapConfigProperty, String.valueOf(wo.getWoTypeId()),
                Constants.AP_PARAM.WO_TYPE_NIMS_HUY_TRAM)) {
              resCheckDiDoi = checkCloseWoDiDoiHuyTram(wo, 1L);
            } else {
              resCheckDiDoi = checkCloseWoDiDoiHuyTram(wo, 2L);
            }
            if (!RESULT.SUCCESS.equals(resCheckDiDoi.getKey())) {
              resultInSideDto.setKey(RESULT.ERROR);
              resultInSideDto.setMessage(resCheckDiDoi.getMessage());
              return resultInSideDto;
            }
          } catch (Exception e) {
            log.error(e.getMessage(), e);
            resultInSideDto.setKey(RESULT.ERROR);
            resultInSideDto.setMessage(e.getMessage());
            return resultInSideDto;
          }
        }
        WoDetailDTO detail = woDetailRepository.findWoDetailById(wo.getWoId());
        UsersEntity us = userRepository.getUserByUserName(userName);

        UsersEntity ft = userRepository.getUserByUserIdCheck(wo.getFtId());
        UnitDTO unFt = unitRepository.findUnitById(ft.getUnitId());
        // goi sang SPM
        if (WO_MASTER_CODE.WO_SPM.equals(woInsideDTO.getWoSystem()) || INSERT_SOURCE.SPM_VTNET
            .equals(woInsideDTO.getWoSystem())
            || (checkProperty(mapConfigProperty, String.valueOf(woInsideDTO.getWoTypeId()),
            Constants.AP_PARAM.WO_TYPE_XLSCVT) && checkProperty(mapConfigProperty, "1",
            Constants.AP_PARAM.IS_CALL_TT_SCVT))
        ) {
          // thuc hien kiem tra neu da autocheck moi cho dong WO
          Date date = new Date();
          if (WO_RESULT.OK.equals(action) && (WO_MASTER_CODE.WO_SPM
              .equals(wo.getWoSystem()) || INSERT_SOURCE.SPM_VTNET
              .equals(woInsideDTO.getWoSystem()))
              && checkProperty(mapConfigProperty, "1",
              Constants.WO_BUSINESS_CHECK.AUTO_CHECKED_WHEN_APPROVE_SPM)
              && StringUtils.isStringNullOrEmpty(wo.getNationCode())) {
            Date completedDate = wo.getCompletedTime();
            if (completedDate != null) {
              if (wo.getNumAutoCheck() != null
                  && wo.getNumAutoCheck() >= 5L) {  // du lan autochek cho pass
              } else {
                Long tmpTime = date.getTime() - completedDate.getTime();
                String strTimeApprove = commonRepository
                    .getConfigPropertyValue(Constants.WO_BUSINESS_CHECK.TIME_APPROVE_WO_SPM);

                Long timeApprove = strTimeApprove != null ? Long.valueOf(strTimeApprove) : null;
                if (timeApprove != null && tmpTime != null && detail != null) {
                  if (tmpTime < timeApprove) {
                    // chua autocheck check chua du thoi gian thi ko cho dong
                    if (detail.getAutoChecked() == null) {
                      resultInSideDto.setKey(RESULT.ERROR);
                      resultInSideDto.setMessage(I18n.getLanguage("wo.AutocheckFirtApprove"));
                      return resultInSideDto;
                    }
                    // dang autocheck
                    if (detail.getAutoChecked() != null && detail.getAutoChecked().equals(1L)) {
                      resultInSideDto.setKey(RESULT.ERROR);
                      resultInSideDto.setMessage(I18n.getLanguage("wo.AutocheckFirtApprove"));
                      return resultInSideDto;
                    }
                    // da autochek thi ok luon
                    if (detail.getAutoChecked() != null && !detail.getAutoChecked().equals(0L)) {

                    }
                  } else { //by pass do qua gio cho phep

                  }
                }
              }
            }
          }
          ResultDTO resTT;
          if (woInsideDTO.getResult() != null) {  // phe duyet
            // bo sung thong tin thoi gian xu ly sang SPM_start
            Date finishTime = wo.getFinishTime();
            Date endTime = wo.getEndTime();
            Date startTime = wo.getStartTime();
            Double totalTime = (finishTime.getTime() - startTime.getTime()) / 1000.0 / 60 / 60;
            List<WoPendingDTO> lstPending = woPendingRepository
                .getListWoPendingByWoId(woInsideDTO.getWoId());
            Double pendingTime = 0.0D;
            if (lstPending != null && lstPending.size() > 0) {
              Long pt = 0L;
              for (WoPendingDTO i : lstPending) {
                if (i.getOpenTime() != null && i.getInsertTime() != null) {
                  // chi tinh thoi gian sau khi WO da start
                  if (i.getOpenTime().getTime() >= wo.getStartTime().getTime()) {
                    if (i.getInsertTime().getTime() < wo.getStartTime()
                        .getTime()) { // tam dung truoc start
                      pt = pt + (i.getOpenTime().getTime() - wo.getStartTime().getTime());
                    } else { // tam dung sau start
                      pt = pt + (i.getOpenTime().getTime() - i.getInsertTime().getTime());
                    }
                  }
                }
              }
              if (pt > 0) {
                pendingTime = pt / 1000.0 / 60 / 60;
              }
            }
            TroublesDTO o = new TroublesDTO();
            o.setTotalPendingTimeGnoc((Math.round(pendingTime * 100.0) / 100.0) + "");
            o.setTotalProcessTimeGnoc(
                (Math.round((totalTime - pendingTime) * 100.0) / 100.0) + "");
            o.setEvaluateGnoc(new Date().getTime() <= endTime.getTime() ? "1" : "0");
            o.setInfoTicket(detail.getInfoTicket());  //tiennv code them nang cap WO
            o.setCustomerTimeDesireFrom(detail.getResultCheckName());
            o.setCustomerTimeDesireTo(detail.getResultCheck());

            if (isShowReasonInfo || detail.getCcResult() != null) { // nguyen nhan 3 cap
              CompCause lv3 = woRepository.getCompCause(detail.getCcResult());
              if (lv3 != null) {
                o.setReasonLv3Id(lv3.getCompCauseId() + "");
                o.setReasonLv3Name(lv3.getName());
                CompCause lv2 = woRepository.getCompCause(lv3.getParentId());
                if (lv2 != null) {
                  o.setReasonLv2Id(lv2.getCompCauseId() + "");
                  o.setReasonLv2Name(lv2.getName());
                  CompCause lv1 = woRepository.getCompCause(lv2.getParentId());
                  if (lv1 != null) {
                    o.setReasonLv1Id(lv1.getCompCauseId() + "");
                    o.setReasonLv1Name(lv1.getName());
                  }
                }
              }
            }
            if (isShowReasonOverdueInfo) {
              o.setReasonOverdueId((woInsideDTO.getReasonOverdueLV1Id() != null && "-1"
                  .equals(woInsideDTO.getReasonOverdueLV1Id())) ? woInsideDTO
                  .getReasonOverdueLV1Id() : null);
              o.setReasonOverdueName(woInsideDTO.getReasonOverdueLV1Name());
              o.setReasonOverdueId2((woInsideDTO.getReasonOverdueLV2Id() != null && "-1"
                  .equals(woInsideDTO.getReasonOverdueLV2Id())) ? woInsideDTO
                  .getReasonOverdueLV2Id() : null);
              o.setReasonOverdueName2(woInsideDTO.getReasonOverdueLV2Name());
            }
            if (WO_RESULT.OK.equals(action)) {
              String commentTT = "FT:" + (ft != null ? ft.getUsername() : "") + " : " + DateUtil
                  .date2ddMMyyyyHHMMss(new Date()) + " " + I18n.getLanguage("wo.cdApproveWO")
                  + " comment:" + comment;
              resTT = updateStatusTT(o, wo, null, null, unFt, ft.toDTO(), commentTT, "CLOSED",
                  "CLOSED_CD", comment);
              if (resTT != null && !RESULT.SUCCESS.equals(resTT.getKey())) {
                resultInSideDto.setKey(RESULT.ERROR);
                resultInSideDto.setMessage(
                    I18n.getLanguage("wo.errCommunicateSPM") + ": resultSpm.getKey() :" + resTT
                        .getMessage());
                return resultInSideDto;
              }
            } else if (WO_RESULT.NOK_CLOSE
                .equals(action)) {//phe duyet NOK thuc hien reject wo
              o.setIsCheck("2");
              String commentTT = "FT:" + (ft != null ? ft.getUsername() : "") + " : " + DateUtil
                  .date2ddMMyyyyHHMMss(new Date()) + " " + I18n.getLanguage("wo.cdApproveNOK")
                  + " comment:" + comment;
              resTT = updateStatusTT(o, wo, null, null, unFt, ft.toDTO(), commentTT, "CLOSED",
                  "REJECT", comment);
              if (resTT != null && !RESULT.SUCCESS.equals(resTT.getKey())) {
                resultInSideDto.setKey(RESULT.ERROR);
                resultInSideDto.setMessage(
                    I18n.getLanguage("wo.errCommunicateSPM") + ": resultSpm.getKey() :" + resTT
                        .getMessage());
                return resultInSideDto;
              }
            }
            // nhan tin cho nhan vien kinh doanh
            if (detail != null) {
              StaffBean nvkd = null;
              try {
                nvkd = paymentPort.getStaffByIsdn(detail.getAccountIsdn());
              } catch (AbstractMethodError e) {
                log.error(e.getMessage(), e);
              }
              if (nvkd != null) {
                MessagesDTO message = new MessagesDTO();
                String smsContent = "Phan anh cua Account:" + detail.getAccountIsdn()
                    + ". Da duoc KTV " + us.getUsername() + "(" + us.getMobile()
                    + ") hoan thanh xu ly vao luc " + DateTimeUtils
                    .convertDateToString(new Date(), Constants.ddMMyyyyHHmmss);
                message.setSmsGatewayId(smsGatewayId);
                message.setReceiverId(String.valueOf(nvkd.getStaffId()));
                message.setReceiverUsername(nvkd.getStaffCode());
                message.setReceiverFullName(nvkd.getName());
                message.setSenderId(senderId);
                message.setReceiverPhone(nvkd.getTelFax());
                message.setStatus("0");
                message.setCreateTime(
                    DateTimeUtils.convertDateToString(new Date(), Constants.ddMMyyyyHHmmss));
                message.setContent(smsContent);
                messagesRepository.insertOrUpdateWfm(message);
              }
            }
          } else {  // cap nhat trang thai
            String commentTT =
                userName + ":" + DateUtil.date2ddMMyyyyHHMMss(new Date()) + " " + I18n
                    .getLanguage("wo.updateStatus") + ":"
                    + getWoStatusName(wo.getStatus().intValue()) + " comment:" + comment;
            resTT = updateStatusTT(null, wo, null, null, unFt, ft.toDTO(), commentTT, "UPDATE",
                getWoStatusName(wo.getStatus().intValue()), comment);
            if (resTT != null && !RESULT.SUCCESS.equals(resTT.getKey())) {
              resultInSideDto.setKey(RESULT.ERROR);
              resultInSideDto.setMessage(
                  I18n.getLanguage("wo.errCommunicateSPM") + ": resultSpm.getKey() :" + resTT
                      .getMessage());
              return resultInSideDto;
            }
          }
        }
        // <editor-fold defaultstate="collapsed" desc="Hoan thanh WO CC_STL">
        if (woInsideDTO.getStatus().equals(Long.valueOf(WO_STATUS.CLOSED_CD))) {
          if (checkProperty(mapConfigProperty, String.valueOf(wo.getWoTypeId()),
              Constants.AP_PARAM.WO_TYPE_CDBR_FOR_STL)) {
            try {
              // thuc hien goi cc stl neu khong co vat tu dong thoi dong WO lai
              com.viettel.webservice.cc_stl.Input input = new com.viettel.webservice.cc_stl.Input();
              input.setUsername(userCcSTL);
              input.setPassword(passCcSTL);
              input.setWscode("updateComplain");

              List<Param> lstParam = new ArrayList();
              lstParam.add(setParam("username", "vsmart"));
              lstParam.add(setParam("woid", String.valueOf(wo.getWoId())));
              lstParam.add(setParam("complainid", wo.getWoSystemId()));
              lstParam.add(setParam("account", detail.getAccountIsdn()));
              lstParam.add(setParam("status", String.valueOf(wo.getStatus())));
              lstParam.add(setParam("resultcontent", wo.getCommentComplete()));
              lstParam.add(setParam("datetime", DateUtil.date2ddMMyyyyHHMMss(new Date())));
              lstParam.add(setParam("staffcode", us.getStaffCode()));

              input.getParam().addAll(lstParam);

              com.viettel.webservice.cc_stl.Output output = ccStlPort.updateComplain(input);

              if (!RESULT.SUCCESS.equalsIgnoreCase(output.getDescription())) { // loi voi gate way
                resultInSideDto.setKey(RESULT.ERROR);
                resultInSideDto
                    .setMessage("[CC_STL]" + output.getError() + "-" + output.getDescription());
                return resultInSideDto;
              } else {
                String original = output.getOriginal();
                String des = original.substring(original.indexOf("<description>") + 13,
                    original.indexOf("</description>"));
                String res = original.substring(original.indexOf("<responseCode>") + 14,
                    original.indexOf("</responseCode>"));

                if (des == null || res == null) { // ko lay dc thong tin original
                  resultInSideDto.setKey(RESULT.ERROR);
                  resultInSideDto.setMessage("[CC_STL]" + "-" + output.getOriginal());
                  return resultInSideDto;
                } else if (!"0".equalsIgnoreCase(res)) {
                  resultInSideDto.setKey(RESULT.ERROR);
                  resultInSideDto.setMessage("[CC_STL]" + res + "-" + des);
                  return resultInSideDto;
                }
              }
            } catch (Exception e) {
              log.error(e.getMessage(), e);
              resultInSideDto.setKey(RESULT.ERROR);
              resultInSideDto.setMessage("[GNOC]" + e.getMessage());
              return resultInSideDto;
            }
          }
        }
        // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="Phe duyet WO sua chua thiet bi co dien goi sang co dien">
        if (woInsideDTO.getStatus().equals(Long.valueOf(WO_STATUS.CLOSED_CD))) {
          if (checkProperty(mapConfigProperty, String.valueOf(wo.getWoTypeId()),
              Constants.AP_PARAM.WO_TYPE_SCTBCD_ACCESS)) {
            // goi co dien cap nhat trang thai tot
            try {
              List<String> lstFileName = null;
              List<byte[]> lstArr = null;
              if (StringUtils.isNotNullOrEmpty(wo.getFileName())) {
                List<ObjFile> lstFile = getFileFromWo(wo.getWoId(), null);
                if (lstFile != null && lstFile.size() > 0) {
                  lstFileName = new ArrayList<>();
                  lstArr = new ArrayList<>();
                  for (ObjFile f : lstFile) {
                    lstFileName.add(f.getFileName());
                    lstArr.add(f.getFileArr());
                  }
                }
              }
              com.viettel.webservice.codien.Result res = coDienPort
                  .updateResultWORepairDevice(wo.getWoCode(), 1L, wo.getNewFailureReason(),
                      lstFileName, lstArr);
              if (!res.getStatus().equals(0L)) {
                resultInSideDto.setKey(RESULT.ERROR);
                resultInSideDto.setMessage(res.getMessage());
                return resultInSideDto;
              }
            } catch (Exception e) {
              log.error(e.getMessage(), e);
              resultInSideDto.setKey(RESULT.ERROR);
              resultInSideDto.setMessage("[EEE2] " + e.getMessage());
              return resultInSideDto;
            }
          }
        }
        // </editor-fold>
        // goi sang noc bao dong ko tao alarm
        if (woInsideDTO.getResult() != null && wo.getConfirmNotCreateAlarm() != null
            && wo.getConfirmNotCreateAlarm().equals(1L)) {
          ResultDTO resNoc = closeNotCreateAlarm(wo.getWoCode());
          if (!RESULT.SUCCESS.equals(resNoc.getKey())) {
            resultInSideDto.setKey(RESULT.ERROR);
            resultInSideDto.setMessage(resNoc.getMessage());
            return resultInSideDto;
          }
        }
        // <editor-fold defaultstate="collapsed" desc="Loai cong viec hoan cong/thi cong/ nghiem thu goi sang hoan cong dien tu">
        if (checkProperty(mapConfigProperty, String.valueOf(wo.getWoTypeId()),
            Constants.AP_PARAM.WO_TYPE_HOAN_CONG_OS)
            || checkProperty(mapConfigProperty, String.valueOf(wo.getWoTypeId()),
            Constants.AP_PARAM.WO_TYPE_NGHIEM_THU_OS)
            || checkProperty(mapConfigProperty, String.valueOf(wo.getWoTypeId()),
            Constants.AP_PARAM.WO_TYPE_THI_CONG_OS)) {
          try {
            String res = hoanCongPort.updateWoToHoanCong(wo.getWoCode());
            if (!RESULT.SUCCESS.equals(res)) {
              resultInSideDto.setKey(RESULT.ERROR);
              resultInSideDto.setMessage("" + res);
              return resultInSideDto;
            }
          } catch (Exception e) {
            log.error(e.getMessage(), e);
            log.error(e.getMessage(), e);
            resultInSideDto.setKey(RESULT.ERROR);
            resultInSideDto.setMessage("[HOAN_CONG]" + e.getMessage());
            return resultInSideDto;
          }
        }
        // </editor-fold>
        // check nims
        if (woInsideDTO.getStatus().equals(Long.valueOf(WO_STATUS.CLOSED_CD))) {
          if (checkProperty(mapConfigProperty, String.valueOf(wo.getWoTypeId()),
              Constants.AP_PARAM.WO_TYPE_NIMS_KTDL)) {
            try {
              CheckPortSubDescriptionByWOForm res = wsHTNims
                  .checkPortSubDescriptionByWO(wo.getWoCode());
              if (!WS_RESULT.OK.equals(res.getResult())) {
                resultInSideDto.setKey(RESULT.ERROR);
                resultInSideDto.setMessage(res.getMessage());
                return resultInSideDto;
              }
            } catch (Exception e) {
              log.error(e.getMessage(), e);
              resultInSideDto.setKey(RESULT.ERROR);
              resultInSideDto.setMessage("[NIMS_HT]" + e.getMessage());
              return resultInSideDto;
            }
          }
        }
        //trukho
        if (showMaterialInfo && woInsideDTO.getResult() != null) {
          ResultInSideDto rsDeducteIM = woMaterialDeducteBusiness
              .putMaterialDeducteToIM(wo.getWoId(), true);
          if (!RESULT.SUCCESS.equals(rsDeducteIM.getKey())) {
            resultInSideDto.setKey(RESULT.ERROR);
            resultInSideDto.setMessage(rsDeducteIM.getMessage());
            return resultInSideDto;
          }
        }
        // Insert table WO_HISTORY
        //INPROCESS --> CLOSED_FT
        woDtoHis.setUserId(userToken.getUserID());
        woDtoHis.setUserName(userToken.getUserName());
        woDtoHis.setUpdateTime(new Date());
        woDtoHis.setNewStatus(woInsideDTO.getStatus());
        woDtoHis.setOldStatus(oldStatus);
        ResultInSideDto resultDTO = woHistoryRepository.insertWoHistory(woDtoHis);
        if (RESULT.SUCCESS.equals(resultDTO.getKey())) {
          // Lay cong viec cha
          if (woInsideDTO.getParentId()
              != null /*&& action != null && !action.equals("")*/) { // phe duyet
            UsersEntity usersEntity = userRepository.getUserByUserName(userName);
            if (usersEntity == null) {
              resultInSideDto.setKey(RESULT.ERROR);
              resultInSideDto.setMessage(
                  "Username " + userName + " " + I18n.getLanguage("wo.notExixtsOnSystem"));
              return resultInSideDto;
            }
            UsersInsideDto cd = usersEntity.toDTO();
//            WoInsideDTO woParent = woRepository.findWoByIdNoOffset(wo.getParentId());
            updateParentStatus(wo.getParentId(), wo.getStatus(), action, cd, comment,
                woInsideDTO.getWoId());
          }
          woRepository.updateWo(wo);
        } else {
          resultInSideDto.setKey(RESULT.ERROR);
          resultInSideDto.setMessage(I18n.getLanguage("wo.haveSomeErrUpdateStatus"));
          return resultInSideDto;
        }
        // <editor-fold defaultstate="collapsed" desc="WO SPM thuoc A,P,THC thuc hien dong luon">
        if (woInsideDTO.getStatus().equals(Long.valueOf(WO_STATUS.CLOSED_FT))
            && (WO_MASTER_CODE.WO_SPM.equals(wo.getWoSystem()) || INSERT_SOURCE.SPM_VTNET
            .equals(wo.getWoSystem()))) {
          List<String> lstByPass = getListConfigPropertyValue(
              Constants.AP_PARAM.SERVICE_CLOSE_WHEN_COMPLETE);
          if (lstByPass != null && lstByPass.size() > 0 && detail != null
              && detail.getServiceId() != null) {
            if (lstByPass.contains(String.valueOf(detail.getServiceId()))) {
              VsmartUpdateForm updateForm = new VsmartUpdateForm();
              updateForm.setByPassAutoCheck(1L);
              ResultInSideDto resApprove = approveWoCommon(updateForm, userName,
                  woInsideDTO.getWoId(),
                  "Auto close WO when complete", WO_RESULT.OK, userName);
              if (!RESULT.SUCCESS.equals(resApprove.getKey())) {
                return resApprove;
              }
            }
          }
        }
        // </editor-fold>

        //tiennv cap nhat code nang cap WO
        // <editor-fold defaultstate="collapsed" desc="thuc hien dong luon voi 1 so loai WO">
        if (Constants.WO_STATUS.CLOSED_FT.equals(String.valueOf(woInsideDTO.getStatus()))
            && checkProperty(mapConfigProperty, String.valueOf(wo.getWoTypeId()),
            Constants.AP_PARAM.WO_TYPE_CLOSE_WHEN_COMPLETE)) {

          VsmartUpdateForm updateForm = new VsmartUpdateForm();

          updateForm.setByPassAutoCheck(1L);
          ResultInSideDto resApprove = approveWoCommon(updateForm, userName, woInsideDTO.getWoId(),
              "Auto close WO when complete", Constants.WO_RESULT.OK, userName);
          if (!RESULT.SUCCESS.equals(resApprove.getKey())) {
            return resApprove;
          }
        }
        // </editor-fold>
      }// thuc hien cap nhat trang thai thong thuong
      resultInSideDto.setKey(RESULT.SUCCESS);
      resultInSideDto.setMessage(RESULT.SUCCESS);
      return resultInSideDto;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      try {
        if (!I18n.getLanguage("wo.woIsBusy").equals(e.getMessage())
            && woInsideDTO.getStatus().equals(Long.valueOf(WO_STATUS.CLOSED_CD))) {
          ResultInSideDto rsRollbackIM = woMaterialDeducteBusiness
              .rollBackDeducteToIM(woInsideDTO.getWoId());
          if (rsRollbackIM != null) {
            log.error(rsRollbackIM.getKey() + ":" + rsRollbackIM.getMessage(), e);
          }
        }
      } catch (Exception ex) {
        log.error(ex.getMessage(), ex);
      }
      resultInSideDto.setKey(RESULT.ERROR);
      resultInSideDto.setMessage(e.getMessage());
      return resultInSideDto;
    }
  }

  @Override
  public ResultInSideDto approveWoCommon(VsmartUpdateForm updateForm, String username, Long woId,
      String comment, String action, String ftName) throws Exception {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    boolean checkUpdateParent = false;
    try {
      WoInsideDTO wo = woRepository.findWoByIdNoWait(woId);
      if (wo == null) {
        throw new Exception(I18n.getLanguage("wo.woIsNotExists"));
      }
      if (!wo.getStatus().equals(Long.parseLong(WO_STATUS.CLOSED_FT))) {
        throw new Exception(I18n.getLanguage("wo.woIsBusy"));
      }
      if (wo == null) {
        throw new Exception(I18n.getLanguage("wo.woIsNotExists"));
      }
      List<UsersInsideDto> listUser = userRepository.getListUserDTOByuserName(username);
      if (listUser == null || listUser.isEmpty()) {
        throw new Exception("Username " + username + I18n.getLanguage("wo.NotExistsOnSystem"));
      }
      UsersInsideDto cd = listUser.get(0);
      UsersInsideDto ft = userRepository.getUserByUserIdCheck(wo.getFtId()).toDTO();
      String nationCode = getNationFromUnit(ft.getUnitId());
      Date date = new Date();
      //ko cho phep phe duyet tren GNOC
      Map<String, String> mapConfigProperty = commonRepository.getConfigProperty();
      if (checkProperty(mapConfigProperty, String.valueOf(wo.getWoTypeId()),
          Constants.AP_PARAM.WO_TYPE_QLTS)) {
        Kttsbo resKtts = kttsVsmartPort.checkWorkOrderNation(wo.getWoId(), nationCode);
        if (resKtts != null && !RESULT.SUCCESS.equalsIgnoreCase(resKtts.getStatus())) {
          throw new Exception("[KTTS]:" + resKtts.getErrorInfo());
        }
      }
      WoDetailDTO detail = woDetailRepository.findWoDetailById(wo.getWoId());
      if (WO_RESULT.OK.equals(action) || WO_RESULT.NOK_CLOSE.equals(action)) {
        //thay doi trang thai dong
        if (WO_RESULT.OK.equals(action)) {
          wo.setResult(Long.valueOf(WO_RESULT.OK));
        } else {
          wo.setResult(Long.valueOf(WO_RESULT.NOK_CLOSE));
        }
        if (!checkUpdateParent) {
          updateParentStatus(wo.getParentId(), Long.valueOf(WO_STATUS.CLOSED_CD), action, cd,
              comment, wo.getWoId());
          checkUpdateParent = true;
        }
        updateWoAndHistory(wo, cd, comment, WO_STATUS.CLOSED_CD, date);
        wo.setFinishTime(date);
        wo.setLastUpdateTime(date);
        wo.setResult(Long.parseLong(action));

        //tiennv code update WO
        if (checkProperty(mapConfigProperty, String.valueOf(wo.getWoTypeId()),
            Constants.WO_BUSINESS_CHECK.CHECK_WO_TYPE_NOT_ALLOW_APPROVE)) {
          throw new Exception(I18n.getLanguage("wo.type.not.close.manual"));
        }

        //trukho
        ResultInSideDto rsDeducteIM = woMaterialDeducteBusiness
            .putMaterialDeducteToIM(wo.getWoId(), true);
        if (!RESULT.SUCCESS.equals(rsDeducteIM.getKey())) {
          throw new Exception(rsDeducteIM.getMessage());
        }
        // chuyen qua SPM khi phe duyet OK
        if (WO_MASTER_CODE.WO_SPM.equals(wo.getWoSystem())
            || INSERT_SOURCE.SPM_VTNET.equals(wo.getWoSystem())
            || (checkProperty(mapConfigProperty,
            String.valueOf(wo.getWoTypeId()), Constants.AP_PARAM.WO_TYPE_XLSCVT)
            && checkProperty(mapConfigProperty,
            "1", AP_PARAM.IS_CALL_TT_SCVT))
        ) {
          boolean checkByPassAutoCheck = false;
          if (updateForm != null && updateForm.getByPassAutoCheck() != null && updateForm
              .getByPassAutoCheck().equals(1L)) {
            checkByPassAutoCheck = true;
          }
          if (!checkByPassAutoCheck) {
            // thuc hien kiem tra neu da autocheck moi cho dong WO
            if (WO_RESULT.OK.equals(action) && (WO_MASTER_CODE.WO_SPM
                .equals(wo.getWoSystem()) || INSERT_SOURCE.SPM_VTNET.equals(wo.getWoSystem()))
                && checkProperty(mapConfigProperty, "1",
                Constants.WO_BUSINESS_CHECK.AUTO_CHECKED_WHEN_APPROVE_SPM)
                && StringUtils.isStringNullOrEmpty(wo.getNationCode())
            ) {
              if (wo.getNumAutoCheck() != null
                  && wo.getNumAutoCheck() >= 5L) {  // du lan autochek cho pass
              } else {
                Date completedDate = wo.getCompletedTime();
                Long tmpTime = date.getTime() - completedDate.getTime();
                String strTimeApprove = commonRepository
                    .getConfigPropertyValue(Constants.WO_BUSINESS_CHECK.TIME_APPROVE_WO_SPM);
                Long timeApprove = strTimeApprove != null ? Long.valueOf(strTimeApprove) : null;
                if (timeApprove != null && tmpTime != null && detail != null) {
                  if (tmpTime < timeApprove) {
                    // chua autocheck check chua du thoi gian thi ko cho dong
                    if (detail.getAutoChecked() == null) {
                      throw new Exception(I18n.getLanguage("wo.AutocheckFirtApprove"));
                    }
                    // dang autocheck
                    if (detail.getAutoChecked() != null && detail.getAutoChecked().equals(1L)) {
                      throw new Exception(I18n.getLanguage("wo.AutocheckFirtApprove"));
                    }
                    // da autochek thi ok luon
                    if (detail.getAutoChecked() != null && !detail.getAutoChecked().equals(0L)) {

                    }
                  } else { //by pass do qua gio cho phep

                  }
                }
              }
            }
          }
          try {
            UsersInsideDto us = listUser.get(0);
            UnitDTO unit = unitRepository.findUnitById(us.getUnitId());
            TroublesDTO o = new TroublesDTO();
            if (detail != null && detail.getCcResult() != null) { // nguyen nhan 3 cap
              CompCause lv3 = woRepository.getCompCause(detail.getCcResult());
              if (lv3 != null) {
                o.setReasonLv3Id(lv3.getCompCauseId() + "");
                o.setReasonLv3Name(lv3.getName());
                CompCause lv2 = woRepository.getCompCause(lv3.getParentId());
                if (lv2 != null) {
                  o.setReasonLv2Id(lv2.getCompCauseId() + "");
                  o.setReasonLv2Name(lv2.getName());
                  CompCause lv1 = woRepository.getCompCause(lv2.getParentId());
                  if (lv1 != null) {
                    o.setReasonLv1Id(lv1.getCompCauseId() + "");
                    o.setReasonLv1Name(lv1.getName());
                  }
                }
              }
            }
            Double totalTime =
                (wo.getFinishTime().getTime() - wo.getStartTime().getTime()) / 1000.0 / 60 / 60;
            List<WoPendingDTO> lstPending = woPendingRepository
                .getListWoPendingByWoId(wo.getWoId());
            Double pendingTime = 0.0D;
            if (lstPending != null && lstPending.size() > 0) {
              Long pt = 0L;
              for (WoPendingDTO i : lstPending) {
                if (i.getOpenTime() != null && i.getInsertTime() != null) {
                  // chi tinh thoi gian sau khi WO da start
                  if (i.getOpenTime().getTime() >= wo.getStartTime().getTime()) {
                    if (i.getInsertTime().getTime() < wo.getStartTime()
                        .getTime()) { // tam dung truoc start
                      pt = pt + (i.getOpenTime().getTime() - wo.getStartTime().getTime());
                    } else { // tam dung sau start
                      pt = pt + (i.getOpenTime().getTime() - i.getInsertTime().getTime());
                    }
                  }
                }
              }
              if (pt > 0) {
                pendingTime = pt / 1000.0 / 60 / 60;
              }
            }
            o.setTotalPendingTimeGnoc((Math.round(pendingTime * 100.0) / 100.0) + "");
            o.setTotalProcessTimeGnoc(
                (Math.round((totalTime - pendingTime) * 100.0) / 100.0) + "");
            o.setEvaluateGnoc(new Date().getTime() <= wo.getEndTime().getTime() ? "1" : "0");
            //tiennv code nang cap WO
            o.setInfoTicket(detail.getInfoTicket());
            o.setCustomerTimeDesireFrom(detail.getResultCheckName());
            o.setCustomerTimeDesireTo(detail.getResultCheck());

            ResultDTO resTT = null;
            if (WO_RESULT.OK.equals(action)) {
              String commentTT = "FT:" + (ft != null ? ft.getUsername() : "") + " : " + DateUtil
                  .date2ddMMyyyyHHMMss(new Date()) + " " + I18n.getLanguage("wo.cdApproveWO")
                  + " comment:" + comment;
              if (WO_MASTER_CODE.WO_SPM.equals(wo.getWoSystem()) || INSERT_SOURCE.SPM_VTNET
                  .equals(wo.getWoSystem())) {
                resTT = updateStatusTT(o, wo, null, null, unit, ft, commentTT, "CLOSED",
                    "CLOSED_CD", comment);
              } else if (checkProperty(mapConfigProperty, String.valueOf(wo.getWoTypeId()),
                  Constants.AP_PARAM.WO_TYPE_XLSCVT)) {
                resTT = updateStatus_SCVT(o, wo, null, null, unit, ft, commentTT, "CLOSED",
                    "CLOSED_CD", comment);
              }
              if (resTT != null && !RESULT.SUCCESS.equals(resTT.getKey())) {
                throw new RuntimeException(
                    I18n.getLanguage("wo.errCommunicateSPM") + ": resultSpm.getKey() :" + resTT
                        .getMessage());
              }
            } else if (WO_RESULT.NOK_CLOSE
                .equals(action)) {//phe duyet NOK thuc hien reject wo
              o.setIsCheck("2");
              String commentTT = "FT:" + (ft != null ? ft.getUsername() : "") + " : " + DateUtil
                  .date2ddMMyyyyHHMMss(new Date()) + " " + I18n.getLanguage("wo.cdApproveNOK")
                  + " comment:" + comment;
              if (WO_MASTER_CODE.WO_SPM.equals(wo.getWoSystem()) || INSERT_SOURCE.SPM_VTNET
                  .equals(wo.getWoSystem())) {
                resTT = updateStatusTT(o, wo, null, null, unit, cd, commentTT, "CLOSED", "REJECT",
                    comment);
              } else if (checkProperty(mapConfigProperty, String.valueOf(wo.getWoTypeId()),
                  Constants.AP_PARAM.WO_TYPE_XLSCVT)) {
                resTT = updateStatus_SCVT(o, wo, null, null, unit, cd, commentTT, "CLOSED",
                    "REJECT", comment);
              }
              if (resTT != null && !RESULT.SUCCESS.equals(resTT.getKey())) {
                throw new RuntimeException(
                    I18n.getLanguage("wo.errCommunicateSPM") + ": resultSpm.getKey() :" + resTT
                        .getMessage());
              }
            }
            // nhan tin cho nhan vien kinh doanh
            if ("SPM".equals(wo.getWoSystem())) { //tiennv code nang cap WO
              StaffBean nvkd = null;
              try {
                nvkd = paymentPort.getStaffByIsdn(detail.getAccountIsdn());
              } catch (AbstractMethodError e) {
                log.error(e.getMessage(), e);
              }
              if (nvkd != null) {
                MessagesDTO message = new MessagesDTO();
                String smsContent = "Phan anh cua Account:" + detail.getAccountIsdn()
                    + ". Da duoc KTV " + us.getUsername() + "(" + us.getMobile()
                    + ") hoan thanh xu ly vao luc " + DateTimeUtils
                    .convertDateToString(new Date(), Constants.ddMMyyyyHHmmss);

                message.setSmsGatewayId(smsGatewayId);
                message.setReceiverId(String.valueOf(nvkd.getStaffId()));
                message.setReceiverUsername(nvkd.getStaffCode());
                message.setReceiverFullName(nvkd.getName());
                message.setSenderId(senderId);  // fix code = 400
                message.setReceiverPhone(nvkd.getTelFax());
                message.setStatus("0");
                message.setCreateTime(
                    DateTimeUtils.convertDateToString(new Date(), Constants.ddMMyyyyHHmmss));
                message.setContent(smsContent);
                messagesRepository.insertOrUpdateWfm(message);
              }
            }
          } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new Exception(I18n.getLanguage("wo.errCommunicateSPM"));
          }
        } else if (WO_MASTER_CODE.WO_TT.equals(wo.getWoSystem()) && checkFinalWoFromTT(
            wo.getWoCode(), mapConfigProperty)) {
          List<WoTroubleInfoDTO> lstWoTrouble = woTroubleInfoRepository
              .getListWoTroubleInfoByWoId(woId);
          if (lstWoTrouble != null && lstWoTrouble.size() > 0) {
            WoTroubleInfoDTO woTroubleInfo = lstWoTrouble.get(0);
            if (woTroubleInfo.getClearTime() != null) {
              wo.setFinishTime(woTroubleInfo.getClearTime());
            }
            if (StringUtils.isStringNullOrEmpty(woTroubleInfo.getReasonTroubleName())) {
              woTroubleInfo.setReasonTroubleName(comment);
            }
            String commentOld = wo.getCommentComplete();
            wo.setCommentComplete(comment);
            closeToIncident(wo, woTroubleInfo);
            wo.setCommentComplete(commentOld);
          }
        }
        // check cong suat tuyen viba
        if (checkProperty(mapConfigProperty, String.valueOf(wo.getWoTypeId()),
            Constants.AP_PARAM.WO_TYPE_BD_VIBA)) {
          try {
            ResultInSideDto resViba = checkVibaFromNoc(wo.getVibaLineCode(), wo.getStationCode(),
                wo.getCompletedTime(), 2L);
            if (!RESULT.SUCCESS.equals(resViba.getKey())) {
              throw new Exception(resViba.getMessage());
            }
          } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new Exception("[GNOC]" + e.getMessage());
          }
        }
        // check nims
        if (checkProperty(mapConfigProperty, String.valueOf(wo.getWoTypeId()),
            Constants.AP_PARAM.WO_TYPE_NIMS_KTDL)) {
          try {
            CheckPortSubDescriptionByWOForm res = wsHTNims
                .checkPortSubDescriptionByWO(wo.getWoCode());
            if (!WS_RESULT.OK.equals(res.getResult())) {
              throw new Exception(res.getMessage());
            }
          } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new Exception("[NIMS_HT]" + e.getMessage());
          }
        }
        // phe duyet loai cong viec sua chua thiet bi co dien access goi hoan cong va co dien
        if (checkProperty(mapConfigProperty, String.valueOf(wo.getWoTypeId()),
            Constants.AP_PARAM.WO_TYPE_SCTBCD_ACCESS)) {
          try {
            // goi co dien cap nhat trang thai tot
            try {
              List<String> lstFileName = null;
              List<byte[]> lstArr = null;
              if (StringUtils.isNotNullOrEmpty(wo.getFileName())) {
                List<ObjFile> lstFile = getFileFromWo(wo.getWoId(), null);
                if (lstFile != null && lstFile.size() > 0) {
                  lstFileName = new ArrayList<>();
                  lstArr = new ArrayList<>();
                  for (ObjFile f : lstFile) {
                    lstFileName.add(f.getFileName());
                    lstArr.add(f.getFileArr());
                  }
                }
              }
              com.viettel.webservice.codien.Result res = coDienPort
                  .updateResultWORepairDevice(wo.getWoCode(), 1L, wo.getNewFailureReason(),
                      lstFileName, lstArr);
              if (!res.getStatus().equals(0L)) {
                throw new Exception(res.getMessage());
              }
            } catch (Exception e) {
              log.error(e.getMessage(), e);
              throw new Exception("[EEE2] " + e.getMessage());
            }
          } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new Exception(e.getMessage());
          }
        }
        // check cong suat tuyen viba
        if (checkProperty(mapConfigProperty, String.valueOf(wo.getWoTypeId()),
            Constants.AP_PARAM.WO_TYPE_BDCD_TRAM)
            || checkProperty(mapConfigProperty, String.valueOf(wo.getWoTypeId()),
            Constants.AP_PARAM.WO_TYPE_DO_XANG_DAU)) {
          try {
            List<CpRequestParam> list = new ArrayList<>();
            if (checkProperty(mapConfigProperty, String.valueOf(wo.getWoTypeId()),
                Constants.AP_PARAM.WO_TYPE_DO_XANG_DAU)) {
              // thuc hien lay du lieu chi so dau chi so cuoi xang dau
              List<ObjKeyValue> lst = vsmartBusiness.getDataTestService(wo.getWoId());
              if (lst != null && lst.size() > 0) {
                for (ObjKeyValue i : lst) {
                  if (i.getRow() == 1) {
                    if (i.getCol() == 2) {
                    }
                    if (i.getCol() == 3) {
                      CpRequestParam p = new CpRequestParam();
                      p.setName("added");
                      p.setType("Number");
                      p.setValue(i.getValue());
                      list.add(p);
                    }
                  }
                }
              }
            }
            Result resChiPhi = portChiPhi
                .confirmSucceedWO(wo.getWoTypeId(), wo.getWoCode(), list);
            if (!resChiPhi.getStatus().equals(0L)) {
              throw new Exception("[CPWS] " + resChiPhi.getMessage());
            }
          } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new Exception(e.getMessage());
          }
        }
        // phe duyet WO di doi huy tram check tai san toi uu
        if (checkProperty(mapConfigProperty, String.valueOf(wo.getWoTypeId()),
            Constants.AP_PARAM.WO_TYPE_NIMS_HUY_TRAM)
            || checkProperty(mapConfigProperty, String.valueOf(wo.getWoTypeId()),
            Constants.AP_PARAM.WO_TYPE_NIMS_DI_DOI_TRAM)) {
          try {
            ResultDTO resCheckDiDoi;
            if (checkProperty(mapConfigProperty, String.valueOf(wo.getWoTypeId()),
                Constants.AP_PARAM.WO_TYPE_NIMS_HUY_TRAM)) {
              resCheckDiDoi = checkCloseWoDiDoiHuyTram(wo, 1L);
            } else {
              resCheckDiDoi = checkCloseWoDiDoiHuyTram(wo, 2L);
            }
            if (!RESULT.SUCCESS.equals(resCheckDiDoi.getKey())) {
              throw new Exception(resCheckDiDoi.getMessage());
            }
          } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new Exception(e.getMessage());
          }
        }
        // <editor-fold defaultstate="collapsed" desc="Hoan thanh WO CC_STL">
        if (checkProperty(mapConfigProperty, String.valueOf(wo.getWoTypeId()),
            Constants.AP_PARAM.WO_TYPE_CDBR_FOR_STL)) {
          try {
            // thuc hien goi cc stl neu khong co vat tu dong thoi dong WO lai
            com.viettel.webservice.cc_stl.Input input = new com.viettel.webservice.cc_stl.Input();
            input.setUsername(userCcSTL);
            input.setPassword(passCcSTL);
            input.setWscode("updateComplain");
            List<Param> lstParam = new ArrayList();
            lstParam.add(setParam("username", "vsmart"));
            lstParam.add(setParam("woid", String.valueOf(wo.getWoId())));
            lstParam.add(setParam("complainid", wo.getWoSystemId()));
            lstParam.add(setParam("account", detail.getAccountIsdn()));
            lstParam.add(setParam("status", String.valueOf(wo.getStatus())));
            lstParam.add(setParam("resultcontent",
                StringUtils.isNotNullOrEmpty(wo.getCommentComplete()) ? wo.getCommentComplete()
                    : "Approve WO from Vsmart"));
            lstParam.add(setParam("datetime", DateUtil.date2ddMMyyyyHHMMss(new Date())));
            lstParam.add(setParam("staffcode", cd.getStaffCode()));

            input.getParam().addAll(lstParam);

            com.viettel.webservice.cc_stl.Output output = ccStlPort.updateComplain(input);

            if (!RESULT.SUCCESS.equalsIgnoreCase(output.getDescription())) { // loi voi gate way
              throw new Exception("[CC_STL]" + output.getError() + "-" + output.getDescription());
            } else {
              String original = output.getOriginal();
              String des = original.substring(original.indexOf("<description>") + 13,
                  original.indexOf("</description>"));
              String res = original.substring(original.indexOf("<responseCode>") + 14,
                  original.indexOf("</responseCode>"));
              if (des == null || res == null) { // ko lay dc thong tin original
                throw new Exception("[CC_STL]" + "-" + output.getOriginal());
              } else if (!"0".equalsIgnoreCase(res)) {
                throw new Exception("[CC_STL]" + res + "-" + des);
              }
            }
          } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new Exception("[GNOC]" + e.getMessage());
          }
        }
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="Hoan thanh wo xu ly can nhieu tao wo ket thuc xu ly can nhieu">
        if (checkProperty(mapConfigProperty, String.valueOf(wo.getWoTypeId()),
            Constants.AP_PARAM.WO_TYPE_XU_LY_CAN_NHIEU)) {
          try {
            if (wo.getReasonInterference() != null && "1"
                .equals(String.valueOf(wo.getReasonInterference()))) {
              ResultInSideDto res = manualCreateWo(wo, 3L);
              if (!RESULT.SUCCESS.equals(res.getKey())) {
                throw new Exception("[GNOC]" + res.getMessage());
              }
              // luu thong tin nguyen nhan hoan thanh wo can nhieu
              if (updateForm != null) {
                wo.setReasonInterference(updateForm.getReasonInterference() != null ? Long
                    .valueOf(updateForm.getReasonInterference()) : null);
              }
            }
          } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new Exception("[GNOC]" + e.getMessage());
          }
        }
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="Loai cong viec hoan cong/thi cong/ nghiem thu goi sang hoan cong dien tu">
        if (checkProperty(mapConfigProperty, String.valueOf(wo.getWoTypeId()),
            Constants.AP_PARAM.WO_TYPE_HOAN_CONG_OS)
            || checkProperty(mapConfigProperty, String.valueOf(wo.getWoTypeId()),
            Constants.AP_PARAM.WO_TYPE_NGHIEM_THU_OS)
            || checkProperty(mapConfigProperty, String.valueOf(wo.getWoTypeId()),
            Constants.AP_PARAM.WO_TYPE_THI_CONG_OS)) {
          try {
            String res = hoanCongPort.updateWoToHoanCong(wo.getWoCode());
            if (!RESULT.SUCCESS.equals(res)) {
              throw new Exception("" + res);
            }
          } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new Exception("[HOAN_CONG]" + e.getMessage());
          }
        }
        // </editor-fold>

        // thuc hien bao cho NOC mo trang thai ko tao alarm
        if (wo.getConfirmNotCreateAlarm() != null && wo.getConfirmNotCreateAlarm().equals(1L)) {
          ResultDTO resNoc = closeNotCreateAlarm(wo.getWoCode());
          if (!RESULT.SUCCESS.equals(resNoc.getKey())) {
            throw new Exception(resNoc.getMessage());
          }
        }
        // thuc hien luu nguyen nhan phe duyet NOK
        if (WO_RESULT.NOK_CLOSE.equals(action)) {
          if (updateForm != null && StringUtils
              .isNotNullOrEmpty(updateForm.getReasonApproveNok())) {
            wo.setReasonApproveNok(updateForm.getReasonApproveNok());
          }
        }
      } else if (WO_RESULT.NOK_DISPATCH.equals(action)) {
        wo.setLastUpdateTime(date);
        wo.setFinishTime(null);
        wo.setCompletedTime(null);
        if (StringUtils.isStringNullOrEmpty(ftName)) {
          throw new Exception(I18n.getLanguage("wo.ftNull"));
        } else {
          // Xu ly update lai wo ve DISPATCH
          Long ftIdOld = wo.getFtId();
          Long ftId = userRepository.getUserId(ftName);
          if (ftId == null) {
            throw new Exception(I18n.getLanguage("wo.ftNotExist"));
          }
          // xoa vat tu lien quan
          if (WO_MASTER_CODE.WO_SPM.equals(wo.getWoSystem()) || INSERT_SOURCE.SPM_VTNET
              .equals(wo.getWoSystem())) {
            ResultInSideDto rsDedute = woMaterialDeducteBusiness
                .deleteMaterialDeducte(wo.getWoId(), ftId);
            if (!RESULT.SUCCESS.equals(rsDedute.getKey())) {
              throw new Exception(I18n.getLanguage("wo.failDelMaterialDeduct"));
            }
          }
          String commentFinal =
              DataUtil.isNullOrEmpty(comment) ? I18n.getLanguage("wo.wasAssignToUser") + ftName
                  : comment;
          if (ftId.equals(ftIdOld)) {
            wo.setStatus(Long.valueOf(WO_STATUS.INPROCESS));
          } else {
            wo.setFtId(ftId);
          }
          if (!checkUpdateParent) {
            updateParentStatus(wo.getParentId(), Long.valueOf(WO_STATUS.DISPATCH), action, cd,
                comment, wo.getWoId());
            checkUpdateParent = true;
          }
          updateWoAndHistory(wo, cd, commentFinal, WO_STATUS.DISPATCH, date);
          wo.setResult(null);
          wo.setFtId(ftId);
        }
        // chuyen sang spm khi thuc hien giao lai
        try {
          List<UsersInsideDto> listUs = userRepository.getListUserDTOByuserName(ftName);
          if (listUs != null && !listUs.isEmpty()) {
            UnitDTO unit = unitRepository.findUnitById(listUs.get(0).getUnitId());
            String commentTT =
                "CD:" + username + " : " + DateUtil.date2ddMMyyyyHHMMss(new Date()) + " " + I18n
                    .getLanguage("wo.ApproveNokDispatch") + " FT:" + ftName + " comment:" + comment;
            ResultDTO resTT = null;
            if (WO_MASTER_CODE.WO_SPM.equals(wo.getWoSystem()) || INSERT_SOURCE.SPM_VTNET
                .equals(wo.getWoSystem())) {
              resTT = updateStatusTT(null, wo, null, null, unit, cd, commentTT, "UPDATE",
                  "DISPATCH",
                  null);
            } else if (checkProperty(mapConfigProperty, String.valueOf(wo.getWoTypeId()),
                Constants.AP_PARAM.WO_TYPE_XLSCVT)) {
              resTT = updateStatus_SCVT(null, wo, null, null, unit, cd, commentTT, "UPDATE",
                  "DISPATCH", null);
            }
            if (resTT != null && !RESULT.SUCCESS.equals(resTT.getKey())) {
              throw new RuntimeException("resultSpm.getKey() :" + resTT.getMessage());
            }
          }
        } catch (Exception e) {
          log.error(e.getMessage(), e);
          throw new Exception(I18n.getLanguage("wo.errCommunicateSPM") + " " + e.getMessage());
        }

      } else if (WO_RESULT.NOT_APPROVED.equals(action)) {
        throw new Exception(I18n.getLanguage("woMaterialDeducte.actionIdInvalid"));
      }
      if (WO_MASTER_CODE.WO_HAU_KIEM.equals(wo.getWoSystem())) {
        //kiem tra hau kiem
        Long result = woRepository.checkCloseWoPostInspection(wo.getWoId(), wo.getNumRecheck());
        if (result.intValue() != 0) {
          throw new Exception(I18n.getLanguage("wo.postNumAccount"));
        }
      }
      if (!checkUpdateParent) {
        updateParentStatus(wo.getParentId(), wo.getStatus(), action, cd, comment, wo.getWoId());
        checkUpdateParent = true;
      }
      woRepository.updateWo(wo);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      try {
        if (!I18n.getLanguage("wo.woIsBusy").equals(e.getMessage())
            && WO_RESULT.OK.equals(action) || WO_RESULT.NOK_CLOSE.equals(action)) {
          woMaterialDeducteBusiness.rollBackDeducteToIM(woId);
        }
      } catch (Exception ex) {
        log.error(ex.getMessage(), ex);
      }
      throw new RuntimeException(e.getMessage());
    }
    resultInSideDto.setId(1L);
    resultInSideDto.setKey(RESULT.SUCCESS);
    resultInSideDto.setMessage(WS_RESULT.OK);
    return resultInSideDto;
  }

  public List<String> getListConfigPropertyValue(String key) {
    try {
      String result = commonRepository.getConfigPropertyValue(key);
      if (StringUtils.isNotNullOrEmpty(result)) {
        String[] arr = result.split(",");
        List<String> lst = new ArrayList<String>();
        for (int i = 0; i < arr.length; i++) {
          lst.add(arr[i]);
        }
        return lst;
      }
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      throw ex;
    }
    return null;
  }

  public ResultDTO closeNotCreateAlarm(String woCode) throws Exception {
    try {
      RequestInputBO req = new RequestInputBO();
      List<ParameterBO> lst = new ArrayList<>();
      ParameterBO bo1 = new ParameterBO();
      bo1.setName("p_wo_trouble_code");
      bo1.setValue(woCode);

      ParameterBO bo2 = new ParameterBO();
      bo2.setFormat("dd/MM/yyyy HH:mm:ss");
      bo2.setName("p_close_time");
      bo2.setValue(DateTimeUtils.date2ddMMyyyyHHMMss(new Date()));

      lst.add(bo1);
      lst.add(bo2);

      req.getParams().addAll(lst);
      req.setCode("CFG_WO_TEST_ALARM_CLOSE");

      JsonResponseBO resNoc = nocProPort.onExecuteMapQuery(req);
      if (resNoc.getStatus() != 0) {
        throw new Exception(resNoc.getDetailError());
      }
      return new ResultDTO(RESULT.SUCCESS, RESULT.SUCCESS, RESULT.SUCCESS);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw e;
    }
  }

  //lay danh sach file tu wo
  @Override
  public List<ObjFile> getFileFromWo(Long woId, List<String> lstFileName) {
    List<ObjFile> lst = new ArrayList<>();
    try {
      if (woId != null) {
        GnocFileDto gnocFileDto = new GnocFileDto();
        gnocFileDto.setBusinessCode(GNOC_FILE_BUSSINESS.WO);
        gnocFileDto.setBusinessId(woId);
        List<GnocFileDto> gnocFileDtos = gnocFileRepository
            .getListGnocFileByDto(gnocFileDto);
        for (GnocFileDto dto : gnocFileDtos) {
          ObjFile obj = new ObjFile();
          byte[] bytes = FileUtils.getFtpFile(ftpServer, ftpPort,
              PassTranformer.decrypt(ftpUser), PassTranformer.decrypt(ftpPass),
              dto.getPath());
          obj.setFileName(FileUtils.getFileName(dto.getPath()));
          obj.setFileArr(bytes);
          lst.add(obj);
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return lst;
  }

  public Param setParam(String name, String value) {
    Param p = new Param();
    p.setName(name);
    p.setValue(value);
    return p;
  }

  public String getWoStatusName(int status) {
    switch (status) {
      case 0:
        return I18n.getLanguage("wo.status.UNASSIGNED");
//                break;
      case 1:
        return I18n.getLanguage("wo.status.ASSIGNED");
//                break;
      case 2:
        return I18n.getLanguage("wo.status.REJECT");
//                break;
      case 3:
        return I18n.getLanguage("wo.status.DISPATCH");
//                break;
      case 4:
        return I18n.getLanguage("wo.status.ACCEPT");
//                break;
      case 5:
        return I18n.getLanguage("wo.status.INPROCESS");
//                break;
      case 6:
        return I18n.getLanguage("wo.status.CLOSED_FT");
//                break;
      case 7:
//        return I18n.getLanguage("wo.status.DRAFT");
        return "DRAFT";
//                break;
      case 8:
        return I18n.getLanguage("wo.status.CLOSED_CD");
//                break;
      case 9:
        return I18n.getLanguage("wo.status.PENDING");
//                break;
      default:
        return "";
//                break;
    }
  }

  public ResultDTO checkCloseWoDiDoiHuyTram(WoInsideDTO wo, Long type) throws Exception {
    try {
      String nationCode = getNationFromUserId(wo.getFtId());
      if (type.equals(1L)) {
        // goi NIMS check huy tram
        ResultCheckStatusCabinet res = wsHTNims.checkStatusCabinet(wo.getDeviceCode());
        if (WS_RESULT.OK.equals(res.getResult())) {
          if (!Constants.AP_PARAM.NIMS_CABINET_STATUS_HUY.equals(res.getCabinetStatus())) {
            throw new Exception("[NIMS]" + I18n.getLanguage("wo.CabinetIsNotRevoke"));
          }
        } else {
          throw new Exception("[NIMS]" + res.getMessage());
        }
        Kttsbo resTS = kttsVsmartPort.checkOffStationNation(wo.getDeviceCode(), 1L, nationCode);
        if (resTS == null || !WS_RESULT.OK.equalsIgnoreCase(resTS.getStatus())) {
          throw new Exception(
              "[KTTS]" + ((resTS != null && resTS.getErrorInfo() != null) ? resTS.getErrorInfo()
                  : I18n.getLanguage("wo.machandiseInStationIsNotRevoke")));
        }
      } else {
        ResultCheckStatusStations res = wsHTNims.checkStatusStations(wo.getStationCode());
        if (WS_RESULT.OK.equals(res.getResult())) {
          if (!"HUY".equals(res.getStationStatus())) {
            throw new Exception("[NIMS]" + I18n.getLanguage("wo.StationIsNotRevoke"));
          }
        } else {
          throw new Exception("[NIMS]" + res.getMessage());
        }
        Kttsbo resTS = kttsVsmartPort.checkOffStationNation(wo.getStationCode(), 2L, nationCode);
        if (resTS == null || !WS_RESULT.OK.equalsIgnoreCase(resTS.getStatus())) {
          throw new Exception(
              "[KTTS]" + ((resTS != null && resTS.getErrorInfo() != null) ? resTS.getErrorInfo()
                  : I18n.getLanguage("wo.machandiseInStationIsNotRevoke")));
        }
      }
      return new ResultDTO(RESULT.SUCCESS, RESULT.SUCCESS, RESULT.SUCCESS);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw new Exception(e.getMessage());
    }
  }

  //goi sang incident tra ve nguyen nhan giai phap
  public void closeToIncident(WoInsideDTO wo, WoTroubleInfoDTO woTroubleInfo) throws Exception {
    if (woTroubleInfo != null && woTroubleInfo.getReasonTroubleId() != null) {
      TroublesDTO trouble = new TroublesDTO();
      trouble.setEndTroubleTime(DateTimeUtils.convertStringToDateTime(wo.getFinishTime()));
      trouble.setReasonId(String.valueOf(woTroubleInfo.getReasonTroubleId()));
      trouble.setRootCause(
          StringUtils.isStringNullOrEmpty(wo.getCommentComplete()) ? woTroubleInfo
              .getReasonTroubleName() : wo.getCommentComplete());
      trouble.setSolutionType(String.valueOf(woTroubleInfo.getSolutionGroupId()));
      if (StringUtils.isNotNullOrEmpty(woTroubleInfo.getSolution())) {
        trouble.setWorkArround(woTroubleInfo.getSolution());
      } else {
        trouble.setWorkArround(
            StringUtils.isStringNullOrEmpty(woTroubleInfo.getSolutionGroupName()) ? "WorkArround"
                : woTroubleInfo.getSolutionGroupName());
      }
      trouble.setReasonName(woTroubleInfo.getReasonTroubleName());
      trouble.setClosuresReplace(woTroubleInfo.getClosuresReplace());
      trouble.setCodeSnippetOff(woTroubleInfo.getCodeSnippetOff());
      trouble.setLineCutCode(woTroubleInfo.getLineCutCode());
      UsersInsideDto us = userRepository.getUserByUserIdCheck(wo.getFtId()).toDTO();
      if (us != null) {
        UnitDTO un = unitRepository.findUnitById(us.getUnitId());
        if (un != null) {
          trouble.setReceiveUnitId(String.valueOf(un.getUnitId()));
          trouble.setReceiveUserId(String.valueOf(us.getUserId()));
          trouble.setReceiveUnitName(un.getUnitName());
          trouble.setReceiveUserName(us.getUsername());
        }
      }
      trouble.setState("8");
      trouble.setTroubleCode(wo.getWoSystemId());
      ResultDTO res = ttServiceProxy.onClosetroubleFromWo(trouble);
      if (!RESULT.SUCCESS.equalsIgnoreCase(res.getKey())) {
        throw new Exception(
            I18n.getLanguage("wo.haveSomeErrWhenUpdateToTrouble") + ":" + res.getMessage());
      }
    }
  }

  public String getNationFromUserId(Long userId) {
    UsersEntity u = userRepository.getUserByUserIdCheck(userId);
    if (u != null && u.getUnitId() != null) {
      return getNationFromUnit(u.getUnitId());
    }
    return null;
  }

  public void updateWoTroubleInfo(WoTroubleInfoDTO woTrouble, WoInsideDTO wo) throws Exception {
    try {
      woTrouble
          .setReasonTroubleId(wo.getReasonTroubleId() != null ? wo.getReasonTroubleId() : null);
      woTrouble.setReasonTroubleName(wo.getReasonTroubleName());
      woTrouble.setSolution(wo.getSolution());
      woTrouble
          .setSolutionGroupId(wo.getSolutionGroupId() != null ? wo.getSolutionGroupId() : null);
      woTrouble.setSolutionGroupName(wo.getSolutionGroupName());
      woTrouble.setClearTime(wo.getClearTime() != null ? wo.getClearTime() : null);
      woTrouble.setPolesDistance(wo.getPolesDistance() != null ? wo.getPolesDistance() : null);
      woTrouble.setScriptId(wo.getScriptId() != null ? wo.getScriptId() : null);
      woTrouble.setScriptName(wo.getScriptName());
      woTroubleInfoRepository.insertWoTroubleInfo(woTrouble);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw new Exception(I18n.getLanguage("wo.haveSomeErrWhenSaveWoTrouble"));
    }
  }

  //ham tao WO tu dong cho cac luong phat sinh
  public ResultInSideDto manualCreateWo(WoInsideDTO wo, Long type) throws Exception {
    WoDTO o = new WoDTO();
    Date now = new Date();
    o.setWoDescription(o.getWoContent());
    o.setWoSystem(wo.getWoSystem());
    o.setWoSystemId(
        StringUtils.isNotNullOrEmpty(wo.getWoSystemId()) ? wo.getWoSystemId() : wo.getWoCode());
    o.setStationCode(wo.getStationCode());
    o.setDeviceCode(wo.getDeviceCode());
    o.setCreatePersonId(
        wo.getCreatePersonId() != null ? String.valueOf(wo.getCreatePersonId()) : null);
    o.setCdId(wo.getCdId() != null ? String.valueOf(wo.getCdId()) : null);
    UsersEntity ft = userRepository.getUserByUserIdCheck(wo.getFtId());
    o.setFtId(ft != null ? ft.getUsername() : null);
    o.setCreateDate(DateTimeUtils.convertDateToString(now, Constants.ddMMyyyyHHmmss));
    o.setStartTime(DateTimeUtils.convertDateToString(now, Constants.ddMMyyyyHHmmss));

    if (type.equals(1L)) { // huy tram
      o.setWoTypeId(
          commonRepository.getConfigPropertyValue(Constants.AP_PARAM.WO_TYPE_NIMS_HUY_TRAM));
      o.setWoContent(I18n.getLanguage("wo.revokeCabinet") + " :" + wo.getDeviceCode() + " " + I18n
          .getLanguage("wo.forWoDiDoi") + ":" + wo.getWoCode());
      o.setPriorityId(
          commonRepository.getConfigPropertyValue(Constants.AP_PARAM.WO_PRIORITY_MINOR));
      now.setDate(now.getDate() + 45);
      o.setEndTime(DateTimeUtils.convertDateToString(now, Constants.ddMMyyyyHHmmss));
    } else if (type.equals(2L)) {
      o.setWoTypeId(
          commonRepository.getConfigPropertyValue(Constants.AP_PARAM.WO_TYPE_NIMS_DI_DOI_TRAM));
      o.setWoContent(
          I18n.getLanguage("wo.revokeStation") + " :" + wo.getStationCode() + " " + I18n
              .getLanguage("wo.forWo") + ":" + wo.getWoCode());
      o.setPriorityId(
          commonRepository.getConfigPropertyValue(Constants.AP_PARAM.WO_PRIORITY_MINOR));
      now.setDate(now.getDate() + 45);
      o.setEndTime(DateTimeUtils.convertDateToString(now, Constants.ddMMyyyyHHmmss));
    } else if (type.equals(3L)) {
      o.setWoTypeId(commonRepository
          .getConfigPropertyValue(Constants.AP_PARAM.WO_TYPE_KET_THUC_XU_LY_CAN_NHIEU));
      o.setWoContent(
          I18n.getLanguage("wo.interferenceContent") + " " + I18n.getLanguage("wo.forWo") + ":"
              + wo.getWoCode());
      o.setPriorityId(
          commonRepository.getConfigPropertyValue(Constants.AP_PARAM.WO_PRIORITY_MINOR));
      String numProcessDate = commonRepository
          .getConfigPropertyValue(Constants.AP_PARAM.NUM_DATE_PROCESS_XL_CAN_NHIEU);
      now.setDate(now.getDate() + Integer.valueOf(numProcessDate));
      o.setEndTime(DateTimeUtils.convertDateToString(now, Constants.ddMMyyyyHHmmss));
      o.setFtId(null);
    }
    return createWoCommon(o);
  }

  @Override
  public ResultInSideDto createWoCommon(WoDTO createWoDto) throws Exception {
    try {
      JAXBContext jaxbContext;
      jaxbContext = JAXBContext.newInstance(WoInsideDTO.class);
      Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
      StringWriter sw = new StringWriter();
      jaxbMarshaller.marshal(createWoDto, sw);
      String xmlString = sw.toString();
      System.out.println("createWoInsideDto: " + xmlString);
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
    }
    ResultInSideDto result = new ResultInSideDto();
    Map<String, String> mapConfigProperty = commonRepository.getConfigProperty();
    if (StringUtils.isStringNullOrEmpty(createWoDto.getWoTypeId())) {
      throw new Exception(I18n.getLanguage("wo.woTypeNull"));
    }
    WoCdGroupInsideDTO woCdGroup = null;
    Long ftId = null;
    String ftName = createWoDto.getFtId();
    String commentInsert = "";
    int quantitySucc = 0;
    if (StringUtils.isNotNullOrEmpty(ftName)) {
      ftId = userRepository.getUserId(ftName);
      if (ftId == null) {
        if (!WO_MASTER_CODE.WO_SPM.equals(createWoDto.getWoSystem())
            && !INSERT_SOURCE.SPM_VTNET.equals(createWoDto.getWoSystem())) {
          throw new Exception(I18n.getLanguage("wo.ftNotExist"));
        } else if (StringUtils.isStringNullOrEmpty(createWoDto.getLocationCode())) {
          throw new Exception(I18n.getLanguage("wo.ftNotExist"));
        } else {  // truong hop SPM ko xac dinh dc FT van giao CD
          quantitySucc = 1;
        }
      } else if (WO_MASTER_CODE.WO_SPM.equals(createWoDto.getWoSystem()) || INSERT_SOURCE.SPM_VTNET
          .equals(createWoDto.getWoSystem())) {
        if (StringUtils.isStringNullOrEmpty(createWoDto.getSpmCode())) {
          throw new Exception(I18n.getLanguage("wo.spmCodeIsnotNull"));
        }
        WoDTO oo = getFtForSPM(createWoDto.getAccountIsdn(), createWoDto.getLocationCode(),
            createWoDto.getNationCode(),
            StringUtils.isNotNullOrEmpty(createWoDto.getWoTypeId()) ? Long
                .valueOf(createWoDto.getWoTypeId()) : null);
        createWoDto.setStationCode(oo.getStationCode());
        createWoDto.setDeptCode(oo.getDeptCode());
        if (StringUtils.isStringNullOrEmpty(oo.getFtId()) && StringUtils
            .isStringNullOrEmpty(oo.getCdId())) {
          throw new Exception(I18n.getLanguage("wo.cdGroupNotExist"));
        } else {
          ftId = StringUtils.isNotNullOrEmpty(oo.getFtId()) ? Long.valueOf(oo.getFtId()) : null;
          createWoDto.setCdId(oo.getCdId());
          createWoDto
              .setInfraType(oo.getInfraType());
          commentInsert = oo.getWoDescription();
        }
      } else {
        woCdGroup = woRepository.getCdByFT(ftId, Long.valueOf(createWoDto.getWoTypeId()), "4");
      }
    } else if (WO_MASTER_CODE.WO_SPM.equals(createWoDto.getWoSystem())
        || INSERT_SOURCE.SPM_VTNET.equals(createWoDto.getWoSystem())) {
      WoDTO oo = getFtForSPM(createWoDto.getAccountIsdn(), createWoDto.getLocationCode(),
          createWoDto.getNationCode(),
          StringUtils.isNotNullOrEmpty(createWoDto.getWoTypeId()) ? Long
              .valueOf(createWoDto.getWoTypeId()) : null);
      createWoDto.setStationCode(oo.getStationCode());
      createWoDto.setDeptCode(oo.getDeptCode());
      if (StringUtils.isStringNullOrEmpty(oo.getFtId()) && StringUtils
          .isStringNullOrEmpty(oo.getCdId())) {
        throw new Exception(I18n.getLanguage("wo.cdGroupNotExist"));
      } else {
        ftId = StringUtils.isNotNullOrEmpty(oo.getFtId()) ? Long.valueOf(oo.getFtId()) : null;
        createWoDto.setCdId(oo.getCdId());
        createWoDto
            .setInfraType(oo.getInfraType());
        commentInsert = oo.getWoDescription();
      }
    } // <editor-fold defaultstate="collapsed" desc="Loai WO xlscvt">
    else if (WO_MASTER_CODE.WO_CC_SCVT.equals(createWoDto.getWoSystem())) {
      createWoDto = getFTSCVT(createWoDto);
      ftId = Long.valueOf(createWoDto.getFtId());
      commentInsert = createWoDto.getCommentComplete();
      createWoDto.setCommentComplete(null);
    }// </editor-fold>
    else if (checkProperty(mapConfigProperty, createWoDto.getWoTypeId(),
        Constants.AP_PARAM.WO_TYPE_THUHOI_CAP_NIMS)) {
      // lay nhom dieu phoi dua vao ma don vi tren NIMS
      if (StringUtils.isStringNullOrEmpty(createWoDto.getUnitCode())) {
        throw new Exception(I18n.getLanguage("wo.unitCodeIsNotNull"));
      }
      UnitDTO unit = woRepository.getUnitCodeMapNims(createWoDto.getUnitCode(), null);
      if (unit != null) {
        Long cd = woRepository
            .getCdByUnitId(unit.getUnitId(), 4L, Long.valueOf(createWoDto.getWoTypeId()));
        if (cd != null) {
          createWoDto.setCdId(String.valueOf(cd));
        }
      }
    } // loai cong viec do nhien lieu thuc hien dinh kem them file
    else if (checkProperty(mapConfigProperty, createWoDto.getWoTypeId(),
        Constants.AP_PARAM.WO_TYPE_DO_XANG_DAU)) {
    }

    //tiennv update code nang cap
    if (checkProperty(mapConfigProperty, createWoDto.getWoTypeId(),
        Constants.AP_PARAM.WO_TYPE_BDNT)) {
      createWoDto.setDeviceType("1");
    }

    if (woCdGroup == null && StringUtils.isStringNullOrEmpty(createWoDto.getCdId())) {
      // thuc hien lay nhom dieu phoi dua vao ma tram
      if (StringUtils.isNotNullOrEmpty(createWoDto.getStationCode())) {
        woCdGroup = getCdByStationCode(createWoDto.getStationCode(),
            Long.valueOf(createWoDto.getWoTypeId()), "4");
      }
      if (woCdGroup == null && StringUtils.isStringNullOrEmpty(createWoDto.getCdId())) {
        throw new Exception(I18n.getLanguage("wo.cdGroupNotExist"));
      }
    }
    createWoDto.setCdId(
        (StringUtils.isStringNullOrEmpty(createWoDto.getCdId()) && woCdGroup != null) ? String
            .valueOf(woCdGroup.getWoGroupId()) : createWoDto.getCdId());

    if (StringUtils.isStringNullOrEmpty(createWoDto.getIsSendFT())
        || createWoDto.getIsSendFT().equals(Constants.WO_IS_SEND_FT.SEND_FT)) {
      createWoDto.setFtId(ftId != null ? String.valueOf(ftId) : null);
    } else {
      createWoDto.setFtId(null);
    }
    // <editor-fold defaultstate="collapsed" desc="Fix ha tang cho cong viec xu ly su co VCAM">
    if (checkProperty(mapConfigProperty, createWoDto.getCcServiceId(),
        Constants.CONFIG_PROPERTY_KEY.CAMERA_CC_SERVICE_ID)) {
      createWoDto.setInfraType(commonRepository
          .getConfigPropertyValue(Constants.CONFIG_PROPERTY_KEY.CAMERA_INFRA_TYPE_ID));
    }
    // </editor-fold>
    String woId = woRepository.getSeqTableWo(WO_SEQ);
    createWoDto.setWoId(woId);
    ResultInSideDto resultIn = insertWoCommon(createWoDto);
    result.setKey(resultIn.getKey());
    result.setMessage(resultIn.getMessage());
    result.setIdValue(resultIn.getIdValue());
    result.setQuantitySucc(quantitySucc);
    if ((WO_MASTER_CODE.WO_SPM.equals(createWoDto.getWoSystem())
        || INSERT_SOURCE.SPM_VTNET.equals(createWoDto.getWoSystem()))
        && RESULT.SUCCESS
        .equals(resultIn.getKey())) {
      result.setMessage(commentInsert);
    }
    return result;
  }

  public WoCdGroupInsideDTO getCdByStationCode(String stationCode, Long woTypeId,
      String cdGroupType) {
    try {
      NimsStationForm form = wsHTNims.getStationInfo(stationCode);
      if (form != null) {
        UnitDTO u = woRepository.getUnitCodeMapNims(form.getDEPT_CODE(), null);
        if (u != null) {
          WoCdGroupInsideDTO model = woRepository
              .getCdByUnitCode(u.getUnitCode(), woTypeId, cdGroupType);
          return model == null ? null : model;
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

  //lay ft cd luong xu ly su co vo tuyen
  public WoDTO getFTSCVT(WoDTO o) throws Exception {
    String commentInsert = "";
    if (o.getLstStationCode() != null && o.getLstStationCode().size() > 0) {
      Map<String, String> mapFt = new HashMap<>();
      Long ftIdTmp = null;
      Boolean checkDiff = false;
      cdPort.setNationCoce(o.getNationCode());
      HeaderForm headerForm = new HeaderForm("nationCode", o.getNationCode());
      List<HeaderForm> listHF = new ArrayList<>();
      listHF.add(headerForm);
      cdPort.setLstHeader(listHF);
      for (String device : o.getLstStationCode()) {
        String stationCode = woRepository.getStationFollowNode(device, o.getNationCode());
        String usedMajorCode = commonRepository
            .getConfigPropertyValue(Constants.AP_PARAM.USED_MAJOR_CODE);
        WoTypeInsideDTO woType = woTypeBusiness.findWoTypeById(Long.valueOf(o.getWoTypeId()));
        if (stationCode != null) {
          List<Staff> lstStaff = cdPort
              .getListStaff(stationCode, woType != null ? woType.getUserTypeCode() : null,
                  usedMajorCode);
          if (lstStaff != null && lstStaff.size() > 0) {
            for (Staff staff : lstStaff) {
              if (!StringUtils.isStringNullOrEmpty(staff.getStaffType())
                  && staff.getStaffType() == 1l) {
                Long ftId = userRepository.getUserId(staff.getUserName());
                if (ftId != null) {
                  ftIdTmp = ftId;
                }
                mapFt.put(staff.getUserName(), String.valueOf(ftId));
                commentInsert =
                    "User:" + staff.getUserName() + " Mobile:" + staff.getTelephone() + " UserName:"
                        + staff.getFullName();
                if (mapFt.size() > 1) {
                  checkDiff = true;
                  break;
                }
              }
            }
          }
        }
        if (checkDiff) {
          break;
        }
      }
      if (ftIdTmp != null) {
        WoCdGroupInsideDTO cdGroup = woRepository
            .getCdByFT(ftIdTmp, Long.valueOf(o.getWoTypeId()), "4");
        if (cdGroup != null) {
          o.setCdId(String.valueOf(cdGroup.getWoGroupId()));
          if (!checkDiff) {
            o.setFtId(String.valueOf(ftIdTmp));
          }
        }
      }
    }
    // giao theo dia ban
    if (o.getCdId() == null && StringUtils.isNotNullOrEmpty(o.getLocationCode())) {
      List<ResultGetDepartmentByLocationForm> lst = wsHTNims
          .getDepartmentByLocation(o.getLocationCode());
      if (lst != null && lst.size() > 0) {
        for (ResultGetDepartmentByLocationForm i : lst) {
          if (i.getDepartmentLevel() != null && i.getDepartmentLevel()
              .equals(4L)) { // don vi muc huyen
            UnitDTO unit = woRepository.getUnitCodeMapNims(i.getDeptCode(), null);
            if (unit != null) {
              Long cd = woRepository.getCdByUnitId(unit.getUnitId(), 4L);
              if (cd != null) {
                commentInsert =
                    commentInsert + " Unit:" + unit.getUnitName() + "/" + unit.getUnitCode();
                o.setCdId(String.valueOf(cd));
                break;
              }
            }
          }
        }
      }
    }
    o.setCommentComplete(commentInsert);
    return o;
  }

  public WoInsideDTO getFtForSPM(String account, String locationCode) {
    try {
      WoInsideDTO wo = new WoInsideDTO();
      SubscriptionInfoForm res = wsNims.getSubscriptionInfo(account, null);
      MessageForm resQlt = cdPort.getUserAssignByAccount(account);
      String commentInsert = "";
      if (resQlt != null && resQlt.getLstUser() != null && !resQlt.getLstUser().isEmpty()) {
        String ftUsername = resQlt.getLstUser().get(0).getUsername().toLowerCase();
        List<UsersInsideDto> listUser = userRepository.getListUserDTOByuserName(ftUsername);
        if (listUser != null && !listUser.isEmpty()) {
          UsersInsideDto usersInsideDto = listUser.get(0);
          wo.setFtId(usersInsideDto.getUserId());
          commentInsert =
              "User:" + usersInsideDto.getUsername() + " Mobile:" + usersInsideDto.getMobile()
                  + " UserName:" + usersInsideDto.getFullname();
        }
      }
      if (res != null) {
        // set ha tang
        if (Constants.INFRA_TYPE.GPON.equals(res.getInfraType().toUpperCase())) {
          wo.setInfraType(Constants.INFRA_TYPE_ID.GPON);
        } else if (Constants.INFRA_TYPE.FCN.equals(res.getInfraType().toUpperCase())) {
          wo.setInfraType(Constants.INFRA_TYPE_ID.FCN);
        } else if (Constants.INFRA_TYPE.CATV.equals(res.getInfraType().toUpperCase())) {
          wo.setInfraType(Constants.INFRA_TYPE_ID.CATV);
        } else if (Constants.INFRA_TYPE.CCN.equals(res.getInfraType().toUpperCase())) {
          wo.setInfraType(Constants.INFRA_TYPE_ID.CCN);
        }
        if (res.getInvestType() != null && res.getInvestType()
            .equals(2L)) {  // neu la don vi thu cap
          ResultService resQltBccs = qltPort
              .getDeptManageConnector(Long.valueOf(wo.getInfraType()), res.getConnectorCode(),
                  null);
          if (resQltBccs == null || !resQltBccs.getResultCode().equals(1L)) {
//            throw new Exception("[QLCTKT] " + resQltBccs.getDescription());
          } else if (StringUtils.isNotNullOrEmpty(resQltBccs.getTeamCode())) {
            UnitDTO unit = woRepository.getUnitCodeMapNims(resQltBccs.getTeamCode(), null);
            if (unit != null) {
              Long cd = woRepository.getCdByUnitId(unit.getUnitId(), 4L);
              if (cd != null) {
                wo.setCdId(cd);
                commentInsert =
                    commentInsert + " Unit:" + unit.getUnitName() + "/" + unit.getUnitCode();
              }
            }
          }
        }
        // neu van khong lay duoc nhom dieu phoi lay nhu luong cu
        if (wo.getCdId() == null) {
          // ConnectorDeptCode -->don vi quan ly ket cuoi
          if (res.getConnectorDeptCode() != null) {
            UnitDTO unit = woRepository.getUnitCodeMapNims(res.getConnectorDeptCode(), null);
            if (unit != null) {
              Long cd = woRepository.getCdByUnitId(unit.getUnitId(), 4L);
              if (cd != null) {
                wo.setCdId(cd);
                commentInsert =
                    commentInsert + " Unit:" + unit.getUnitName() + "/" + unit.getUnitCode();
              }
            }
          } // StationDeptCode  --> don vi quan ly tram
          if (res.getStationDeptCode() != null && wo.getCdId() == null) {
            UnitDTO unit = woRepository.getUnitCodeMapNims(res.getStationDeptCode(), null);
            if (unit != null) {
              Long cd = woRepository.getCdByUnitId(unit.getUnitId(), 4L);
              if (cd != null) {
                wo.setCdId(cd);
                commentInsert =
                    commentInsert + " Unit:" + unit.getUnitName() + "/" + unit.getUnitCode();
              }
            }
          }
        }
        wo.setStationCode(res.getStationCode());
      }
      // don vi dia ban trien khai thue bao
      if (wo.getCdId() == null && StringUtils.isNotNullOrEmpty(locationCode)) {
        List<ResultGetDepartmentByLocationForm> lst = wsHTNims.getSubscriptionInfo(locationCode);
        if (lst != null && lst.size() > 0) {
          for (ResultGetDepartmentByLocationForm i : lst) {
            if (i.getDepartmentLevel() != null && i.getDepartmentLevel()
                .equals(4L)) { // don vi muc huyen
              UnitDTO unit = woRepository.getUnitCodeMapNims(i.getDeptCode(), null);
              if (unit != null) {
                Long cd = woRepository.getCdByUnitId(unit.getUnitId(), 4L);
                if (cd != null) {
                  wo.setCdId(cd);
                  commentInsert =
                      commentInsert + " Unit:" + unit.getUnitName() + "/" + unit.getUnitCode();
                  break;
                }
              }
            }
          }
        }
      }
      wo.setWoDescription(commentInsert);
      return wo;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return null;
    }
  }

  public WoDTO getFtForSPM(String account, String locationCode, String nationCode, Long woTypeId) {
    try {
      WoDTO wo = new WoDTO();
      SubscriptionInfoForm res = ws_nims_cd_direction
          .getSubscriptionInfo(account, null, nationCode);
      cdPort.setNationCoce(nationCode);
      MessageForm resQlt = cdPort.getUserAssignByAccount(account);
      String commentInsert = "";
      if (resQlt != null && resQlt.getLstUser() != null && !resQlt.getLstUser().isEmpty()) {
        String ftUsername = resQlt.getLstUser().get(0).getUsername().toLowerCase();
        List<UsersInsideDto> listUser = userRepository.getListUserDTOByuserName(ftUsername);
        if (listUser != null && !listUser.isEmpty()) {
          UsersInsideDto usersInsideDto = listUser.get(0);
          wo.setFtId(String.valueOf(usersInsideDto.getUserId()));
          commentInsert =
              "User:" + usersInsideDto.getUsername() + " Mobile:" + usersInsideDto.getMobile()
                  + " UserName:" + usersInsideDto.getFullname();
        }
      }
      if (res != null) {
        // set ha tang
        if (Constants.INFRA_TYPE.GPON.equals(res.getInfraType().toUpperCase())) {
          wo.setInfraType(String.valueOf(Constants.INFRA_TYPE_ID.GPON));
        } else if (Constants.INFRA_TYPE.FCN.equals(res.getInfraType().toUpperCase())) {
          wo.setInfraType(String.valueOf(Constants.INFRA_TYPE_ID.FCN));
        } else if (Constants.INFRA_TYPE.CATV.equals(res.getInfraType().toUpperCase())) {
          wo.setInfraType(String.valueOf(Constants.INFRA_TYPE_ID.CATV));
        } else if (Constants.INFRA_TYPE.CCN.equals(res.getInfraType().toUpperCase())) {
          wo.setInfraType(String.valueOf(Constants.INFRA_TYPE_ID.CCN));
        }
        if (res.getInvestType() != null && res.getInvestType()
            .equals(2L)) {  // neu la don vi thu cap
          ResultService resQltBccs = qltPort
              .getDeptManageConnector(Long.valueOf(wo.getInfraType()), res.getConnectorCode(),
                  null);
          if (resQltBccs == null || !resQltBccs.getResultCode().equals(1L)) {
//            throw new Exception("[QLCTKT] " + resQltBccs.getDescription());
          } else if (StringUtils.isNotNullOrEmpty(resQltBccs.getTeamCode())) {
            UnitDTO unit = woRepository.getUnitCodeMapNims(resQltBccs.getTeamCode(), null);
            if (unit != null) {
              Long cd = woRepository.getCdByUnitId(unit.getUnitId(), 4L, woTypeId);
              if (cd != null) {
                wo.setCdId(String.valueOf(cd));
                commentInsert =
                    commentInsert + " Unit:" + unit.getUnitName() + "/" + unit.getUnitCode();
              }
            }
          }
        }
        // neu van khong lay duoc nhom dieu phoi lay nhu luong cu
        if (StringUtils.isStringNullOrEmpty(wo.getCdId())) {
          // ConnectorDeptCode -->don vi quan ly ket cuoi
          if (res.getConnectorDeptCode() != null) {
            UnitDTO unit = woRepository.getUnitCodeMapNims(res.getConnectorDeptCode(), null);
            if (unit != null) {
              Long cd = woRepository.getCdByUnitId(unit.getUnitId(), 4L, woTypeId);
              if (cd != null) {
                wo.setCdId(String.valueOf(cd));
                commentInsert =
                    commentInsert + " Unit:" + unit.getUnitName() + "/" + unit.getUnitCode();
              }
            }
          } // StationDeptCode  --> don vi quan ly tram
          if (res.getStationDeptCode() != null && StringUtils.isStringNullOrEmpty(wo.getCdId())) {
            UnitDTO unit = woRepository.getUnitCodeMapNims(res.getStationDeptCode(), null);
            if (unit != null) {
              Long cd = woRepository.getCdByUnitId(unit.getUnitId(), 4L, woTypeId);
              if (cd != null) {
                wo.setCdId(String.valueOf(cd));
                commentInsert =
                    commentInsert + " Unit:" + unit.getUnitName() + "/" + unit.getUnitCode();
              }
            }
          }
        }
        wo.setStationCode(res.getStationCode());
      }
      // don vi dia ban trien khai thue bao
      if (StringUtils.isStringNullOrEmpty(wo.getCdId()) && StringUtils
          .isNotNullOrEmpty(locationCode)) {
        List<ResultGetDepartmentByLocationForm> lst = wsHTNims
            .getDepartmentByLocation(locationCode, nationCode);
        if (lst != null && lst.size() > 0) {
          for (ResultGetDepartmentByLocationForm i : lst) {
            if (i.getDepartmentLevel() != null && i.getDepartmentLevel()
                .equals(4L)) { // don vi muc huyen
              UnitDTO unit = woRepository.getUnitCodeMapNims(i.getDeptCode(), null);
              if (unit != null) {
                Long cd = woRepository.getCdByUnitId(unit.getUnitId(), 4L, woTypeId);
                if (cd != null) {
                  wo.setDeptCode(i.getDeptCode());
                  wo.setCdId(String.valueOf(cd));
                  commentInsert =
                      commentInsert + " Unit:" + unit.getUnitName() + "/" + unit.getUnitCode();
                  break;
                }
              }
            }
          }
        }
      }
      wo.setWoDescription(commentInsert);
      return wo;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return null;
    }
  }

  public ResultInSideDto checkVibaFromNoc(String vibaLineCode, String stationCode, Date date,
      Long type) {
    ResultInSideDto resultDTO = new ResultInSideDto();
    try {
      RequestInputBO req = new RequestInputBO();
      List<ParameterBO> lst = new ArrayList<>();
      SimpleDateFormat dfm = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
      req.setCompressData(0);
      if (type.equals(1L)) {
        req.setCode("WS_POWER_VIBA_ONLINE");
        ParameterBO bo1 = new ParameterBO();
        bo1.setName("p_line_code");
        bo1.setValue(vibaLineCode);
        lst.add(bo1);
        req.getParams().addAll(lst);
        JsonResponseBO resNoc = nocPort.onExecuteMapQuery(req);
        if (resNoc.getStatus() != 0) {
          resultDTO.setKey(RESULT.FAIL);
          resultDTO.setMessage(resNoc.getDetailError());
          return resultDTO;
        } else {
          resultDTO.setKey(RESULT.SUCCESS);
          resultDTO.setMessage(RESULT.SUCCESS);
          return resultDTO;
        }
      } else {
        req.setCode("WS_Vsmart_GetPowerVibaOnline");
        ParameterBO bo1 = new ParameterBO();
        bo1.setName("p_line_code");
        bo1.setValue(vibaLineCode);
        lst.add(bo1);
        req.getParams().add(bo1);

        ParameterBO bo2 = new ParameterBO();
        bo2.setName("p_time");
        bo2.setValue(dfm.format(date));
        bo2.setFormat("yyyy/MM/dd HH:mm:ss");
        bo2.setType("DATE");
        lst.add(bo2);

        ParameterBO bo3 = new ParameterBO();
        bo3.setName("p_station_code");
        bo3.setValue(stationCode);
        lst.add(bo3);

        req.getParams().addAll(lst);
        JsonResponseBO resNoc = nocPort.getDataJson(req);
        if (resNoc.getStatus() != 0) {
          resultDTO.setKey(RESULT.FAIL);
          resultDTO.setMessage(resNoc.getDetailError());
          return resultDTO;
        } else {
          // part Json
          String response = resNoc.getDataJson();
          Gson gson = new Gson();

          JSONObject json = new JSONObject(response);
          JSONArray info = json.getJSONArray("data");

          TypeToken<List<NocProForm>> token = new TypeToken<>() {
          };
          List<NocProForm> listDataFromNoc = gson.fromJson(info.toString(), token.getType());
          if (listDataFromNoc != null && listDataFromNoc.size() > 0) {
            NocProForm form = listDataFromNoc.get(0);
            String po = form.getInput_power();
            if (StringUtils.isNotNullOrEmpty(po)) {
              String[] arrStr = po.split("/");
              Double minPow = null;
              Double powTmp;
              for (int i = 0; i < arrStr.length; i++) {
                try {
                  powTmp = Double.valueOf(arrStr[i]);
                } catch (Exception e) {
                  log.error(e.getMessage(), e);
                  resultDTO.setKey(RESULT.SUCCESS);
                  resultDTO.setMessage(RESULT.SUCCESS);
                  return resultDTO;
                }
                if (minPow == null) {
                  minPow = powTmp;
                } else if (minPow > powTmp) {
                  minPow = powTmp;
                }
              }
              String pow = commonRepository.getConfigPropertyValue("CFG_INPUT_POWER_VIBA");
              if (minPow < Double.valueOf(pow)) {
                resultDTO.setKey(RESULT.FAIL);
                resultDTO.setMessage(I18n.getLanguage("wo.inputPowerVibaNotReached"));
                return resultDTO;
              }
            }
          } else {
            resultDTO.setKey(RESULT.FAIL);
            resultDTO.setMessage(I18n.getLanguage("wo.inputPowerVibaNotReached"));
            return resultDTO;
          }
          resultDTO.setKey(RESULT.SUCCESS);
          resultDTO.setMessage(RESULT.SUCCESS);
          return resultDTO;
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      resultDTO.setKey(RESULT.FAIL);
      resultDTO.setMessage(I18n.getLanguage("wo.errComunicateNOC") + ":" + e.getMessage());
      return resultDTO;
    }
  }

  //kiem tra co phai wo cuoi cung thi moi goi sang Incident
  public boolean checkFinalWoFromTT(String troubleCode, Map<String, String> mapConfigProperty) {
    boolean check = true;
    List<WoInsideDTO> lst = woRepository.getListWoBySystemOtherCode(troubleCode, null);
    if (lst != null && lst.size() > 0) {
      int num = 0;
      for (WoInsideDTO o : lst) {
        if (!checkProperty(mapConfigProperty, String.valueOf(o.getWoTypeId()),
            Constants.AP_PARAM.WO_TYPE_QLTS)) {
          if (o.getStatus().equals(Long.valueOf(WO_STATUS.CLOSED_CD))
              || (o.getStatus().equals(Long.valueOf(WO_STATUS.REJECT))
              && o.getFtId() == null)) {
          } else {
            num++;
          }
        }
      }
      if (num > 1) {
        check = false;
      }
    }
    return check;
  }

  //check loai cong viec co duoc phep tam dung hoac tao thu cong hay khong
  public boolean checkWoType(Long woTypeId, Long typeCheck) {
    WoTypeInsideDTO woTypeInsideDTO = woTypeBusiness.findWoTypeById(woTypeId);
    if (woTypeInsideDTO != null) {
      if (typeCheck.equals(1L)) { // cho phep tam dung
        if (woTypeInsideDTO.getAllowPending() != null && woTypeInsideDTO.getAllowPending()
            .equals(0L)) {
          return false;
        }
      } else if (typeCheck.equals(2L)) { // cho phep tao thu cong
        if (woTypeInsideDTO.getEnableCreate() != null && woTypeInsideDTO.getEnableCreate()
            .equals(0L)) {
          return false;
        }
      }
    }
    return true;
  }

  //thuc hien cap nhat file
  public void updateFile(WoInsideDTO woInsideDTO, Long userId, List<String> listFileName,
      List<byte[]> fileArr)
      throws Exception {
    if (listFileName != null && listFileName.size() > 0 && fileArr != null
        && fileArr.size() > 0) {
      if (listFileName.size() != fileArr.size()) {
        throw new Exception(I18n.getLanguage("wo.numberFileNotMap"));
      }
      List<GnocFileDto> gnocFileDtos = new ArrayList<>();
      UsersEntity usersEntity = userRepository.getUserByUserId(userId);
      UnitDTO unitToken = unitRepository.findUnitById(usersEntity.getUnitId());
      List<String> fileNames = new ArrayList<>();
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
            throw new Exception(I18n.getLanguage("wo.fileImportInvalidExten"));
          }
        }
        String fullPath = FileUtils
            .saveFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
                PassTranformer.decrypt(ftpPass), ftpFolder, listFileName.get(i), fileArr.get(i),
                FileUtils.createDateOfFileName(woInsideDTO.getCreateDate()));
        fileNames.add(FileUtils.getFileName(fullPath));
        GnocFileDto gnocFileDto = new GnocFileDto();
        gnocFileDto.setPath(fullPath);
        gnocFileDto.setFileName(FileUtils.getFileName(fullPath));
        gnocFileDto.setCreateUnitId(usersEntity.getUnitId());
        gnocFileDto.setCreateUnitName(unitToken.getUnitName());
        gnocFileDto.setCreateUserId(usersEntity.getUserId());
        gnocFileDto.setCreateUserName(usersEntity.getUsername());
        gnocFileDto.setCreateTime(new Date());
        gnocFileDto.setMappingId(woInsideDTO.getWoId());
        gnocFileDtos.add(gnocFileDto);
      }
      gnocFileRepository.saveListGnocFileNotDeleteAll(GNOC_FILE_BUSSINESS.WO, woInsideDTO.getWoId(),
          gnocFileDtos);
      String fileNameNew = fileNames.size() > 0 ? String.join(",", fileNames) : "";
      boolean checkFileName = StringUtils.isNotNullOrEmpty(woInsideDTO.getFileName()) && StringUtils
          .isNotNullOrEmpty(fileNameNew);
      woInsideDTO.setFileName(
          (StringUtils.isStringNullOrEmpty(woInsideDTO.getFileName()) ? ""
              : woInsideDTO.getFileName()) + (checkFileName ? "," : "") + (
              StringUtils.isStringNullOrEmpty(fileNameNew) ? ""
                  : fileNameNew));
      woInsideDTO.setSyncStatus(null);
    }
  }

  public ResultInSideDto changeStatusWoCommon(WoUpdateStatusForm updateForm) throws Exception {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    try {
      validateUpdateStatusForm(updateForm);
      String woCode = updateForm.getWoCode();
      String woId = woCode
          .substring(updateForm.getWoCode().lastIndexOf("_") + 1, updateForm.getWoCode().length());
      WoInsideDTO wo = woRepository.findWoByIdNoWait(Long.valueOf(woId));
      if (wo != null) {
        WoDetailDTO detail = woDetailRepository.findWoDetailById(wo.getWoId());
        Long oldStatus = wo.getStatus();
        List<UsersInsideDto> listUsers = userRepository
            .getListUserDTOByuserName(updateForm.getUserChange());
        if (listUsers == null || listUsers.isEmpty()) {
          throw new Exception(I18n.getLanguage("wo.InvalidUserName") + updateForm.getUserChange());
        }
        UsersInsideDto user = listUsers.get(0);
        String comment =
            updateForm.getSystemChange() + "_" + updateForm.getUserChange() + ":" + updateForm
                .getReasonChange();
        if (updateForm != null && StringUtils.isNotNullOrEmpty(updateForm.getDescription())) {
          wo.setWoDescription(updateForm.getDescription());
        }
        // thuc hien dong
        if (updateForm.getNewStatus() != null && WO_STATUS.CLOSED_CD
            .equals(String.valueOf(updateForm.getNewStatus()))) {
          wo.setResult(updateForm.getResultClose());
          wo.setFinishTime(DateUtil.string2DateTime(updateForm.getFinishTime()));
        }
        // cap nhat trang thai tu CR
        if (WO_MASTER_CODE.WO_CR.equals(updateForm.getSystemChange())) {
          // xoa thong tin hoan thanh
          if (WO_STATUS.CLOSED_CD.equals(String.valueOf(wo.getStatus()))
              && (Long.valueOf(WO_STATUS.CLOSED_CD).compareTo(updateForm.getNewStatus())
              > 0)) {
            wo.setResult(null);
          }
          // thuc hien dong
          if (WO_STATUS.CLOSED_CD.equals(String.valueOf(updateForm.getNewStatus()))) {
            wo.setResult(updateForm.getResultClose());
            // nhan tin cho FT trong trương hop ko dat
            if (updateForm.getResultClose().equals(0L)) {
              String smsContent =
                  "WO: " + wo.getWoCode() + " danh gia khong dat boi nhan su thuc hien CR("
                      + updateForm.getUserChange() + "-" + user.getMobile() + ") Noi dung: "
                      + updateForm.getReasonChange();
              if (wo.getFtId() != null) {
                UsersInsideDto usersInsideDto = userRepository.getUserByUserIdCheck(wo.getFtId())
                    .toDTO();
                sendMessages(smsContent, usersInsideDto, "GNOC_WO");
              } else {
                List<UsersInsideDto> lst = woRepository.getUserOfCD(wo.getCdId());
                if (lst != null && lst.size() > 0) {
                  for (UsersInsideDto u : lst) {
                    sendMessages(smsContent, u, "GNOC_WO");
                  }
                }
              }
            }
          }
        } else if (WO_MASTER_CODE.WO_SPM.equals(updateForm.getSystemChange())
            || INSERT_SOURCE.SPM_VTNET.equals(updateForm.getSystemChange())) {
          if (!wo.getStatus().equals(Long.valueOf(WO_STATUS.CLOSED_CD))) {
            // cap nhat trang thai khi Autocheck
            WoAutoCheckDTO autoCheckDTO = new WoAutoCheckDTO();

            autoCheckDTO.setWoCode(updateForm.getWoCode());
            autoCheckDTO.setErrorDescription(updateForm.getReasonChange());
            if (updateForm.getNewStatus() != null && updateForm.getNewStatus()
                .equals(Long.valueOf(WO_STATUS.CLOSED_CD))) {
              autoCheckDTO.setResult(WS_RESULT.OK);
              wo.setResult(updateForm.getResultClose());
              wo.setFinishTime(DateUtil.string2DateTime(updateForm.getFinishTime()));
            } else if (updateForm.getNewStatus() != null) {
              autoCheckDTO.setResult(WS_RESULT.OK);
              wo.setFinishTime(null);
              wo.setCompletedTime(null);
              wo.setCommentComplete(null);
            } else {
              autoCheckDTO.setResult(WS_RESULT.N_A);
            }
            ResultInSideDto res = updateWoResult(autoCheckDTO);
            wo = (WoInsideDTO) res.getObject();
          } else if (updateForm.getNewStatus() != null) {
            // xoa ft khi chuyen ve cho cd tiep nhan hoac cd da tiep nhan
            if (WO_STATUS.UNASSIGNED.equals(String.valueOf(updateForm.getNewStatus()))
                || WO_STATUS.ASSIGNED.equals(String.valueOf(updateForm.getNewStatus()))) {
              wo.setFtId(null);
            }
            // xoa thong tin hoan thanh
            if (WO_STATUS.CLOSED_FT.equals(String.valueOf(wo.getStatus())) &&
                Long.valueOf(WO_STATUS.CLOSED_FT).compareTo(updateForm.getNewStatus())
                    > 0) {
              wo.setCompletedTime(null);
              wo.setCommentComplete(null);
            }
            // thuc hien dong
            if (WO_STATUS.CLOSED_CD.equals(String.valueOf(updateForm.getNewStatus()))) {
              wo.setResult(Long.valueOf(WO_RESULT.OK));
              wo.setFinishTime(DateUtil.string2DateTime(updateForm.getFinishTime()));
            }
            if (updateForm.getAuditFail() != null && updateForm.getAuditFail()) {
              if (wo.getNumAuditFail() == null) {
                wo.setNumAuditFail(1L);
              } else {
                wo.setNumAuditFail(wo.getNumAuditFail() + 1);
              }
            }
          }
        } else if (WO_MASTER_CODE.WO_BCCS_CC.equals(updateForm.getSystemChange())) {
          // thuc hien xuat lai su co
          if (WO_STATUS.DISPATCH.equals(String.valueOf(updateForm.getNewStatus()))) {
            wo.setResult(null);
            wo.setFinishTime(null);
            wo.setCompletedTime(null);
            wo.setStartTime(DateUtil.string2DateTime(updateForm.getStartTimeNew()));
            wo.setEndTime(DateUtil.string2DateTime(updateForm.getEndTimeNew()));
            comment =
                comment + " (" + updateForm.getStartTimeNew() + " - " + updateForm.getEndTimeNew()
                    + ")";
            if (wo.getFtId() != null) {//tiennv cap nhat code nang cap WO
              String smsContent = "Wo:" + wo.getWoCode()
                  + " da bi CSKH xuat lai."
                  + updateForm.getReasonChange() + ". De nghi d/c kiem tra xu ly triet de!";
              UsersInsideDto ft = userRepository.getUserByUserIdCheck(wo.getFtId()).toDTO();
              if (ft != null) {
                sendMessages(smsContent, ft, "GNOC_WO");
              }
            }
            detail.setReasonReopen(updateForm.getReasonChange());
            woDetailRepository.insertUpdateWoDetail(detail);
          }
        }
        //hungtv bo sung phe duyet gia han wo start
        else if (WO_MASTER_CODE.GNOC_WO_MR.equals(updateForm.getSystemChange())) {
          // xoa thong tin gia han va cap nhat gia han thoi gian WO
          if (updateForm.getAuditFail()) {
            WoConfigPropertyDTO cfg = woRepository.getTimeApproveExtend("WO.APPROVE.EXTEND");
            if (cfg != null) {
              Calendar c = Calendar.getInstance();
              c.setTime(wo.getEndTime());
              c.add(Calendar.DATE, Integer.parseInt(cfg.getValue()));
              wo.setEndTime(c.getTime());
            }
          }
          wo.setReasonExtention(null);
          // tao thong tin luu lich su
          String smsContent;
          if (updateForm.getAuditFail()) { // dong y phe duyet
            comment = updateForm.getSystemChange() + "_" + updateForm.getUserChange() + ": " + I18n.getLanguage("wo.content")
                 + ": " + wo.getWoContent() + ". " + I18n.getLanguage("wo.resultApprove.1") ;
            smsContent = ("Wo: " + wo.getWoCode() + ": " + I18n.getLanguage("wo.content") + ": " + wo.getWoContent() + ". " +
                I18n.getLanguage("wo.resultApprove.1"));
          }
          else { // tu choi phe duyet
            comment = updateForm.getSystemChange() + "_" + updateForm.getUserChange() + ": " + I18n.getLanguage("wo.content")
                + ": " + wo.getWoContent() + ". " + I18n.getLanguage("wo.resultApprove.0") + ": " + updateForm.getContent();
            smsContent = ("Wo: " + wo.getWoCode() + ": " + I18n.getLanguage("wo.content") + ": " + wo.getWoContent() + ". " +
                I18n.getLanguage("wo.resultApprove.0") + ": " + updateForm.getContent());
          }
          if (wo.getFtId() != null) {
            UsersInsideDto ft = userRepository.getUserByUserIdCheck(wo.getFtId()).toDTO();
            if (ft != null) {
              sendMessages(smsContent, ft, "GNOC_WO");
            }
          }
        }
        //hungtv bo sung phe duyet gia han wo end
        if (WO_MASTER_CODE.WO_TT.equals(updateForm.getSystemChange())) {
          // thuc hien tinh lai thoi gian bat dau va ket thuc
          if (WO_STATUS.CLOSED_CD.equals(String.valueOf(oldStatus))) {
            Date now = new Date();
            if (wo.getFinishTime() != null) {
              Long tmp = wo.getEndTime().getTime() - wo.getFinishTime().getTime();
              if (tmp > 0) { // trong han
                Date newD = new Date(now.getTime() + tmp);
                wo.setEndTime(newD);
                wo.setStartTime(now);
              }
            }
            wo.setFinishTime(null);
          }
        }
        if (updateForm.getNewStatus() == null) {
          updateForm.setNewStatus(wo.getStatus());
        }
        // xoa thong tin hoan thanh
        if (WO_STATUS.CLOSED_CD.equals(String.valueOf(wo.getStatus()))
            && Long.valueOf(WO_STATUS.CLOSED_CD).compareTo(updateForm.getNewStatus())
            > 0) {
          wo.setResult(null);
          wo.setFinishTime(null);
        }
        updateWoAndHistory(wo, user, comment, String.valueOf(updateForm.getNewStatus()),
            new Date());
      } else {
        throw new Exception(I18n.getLanguage("wo.woIsNotExists"));
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw new Exception(e.getMessage());
    }
    resultInSideDto.setId(1L);
    resultInSideDto.setKey(RESULT.SUCCESS);
    resultInSideDto.setMessage(RESULT.SUCCESS);
    return resultInSideDto;
  }

  public ResultInSideDto updateWoResult(WoAutoCheckDTO autoCheckDTO) throws Exception {
    ResultInSideDto resultDTO = new ResultInSideDto();
    WoInsideDTO wo = woRepository.findWoByWoCodeNoWait(autoCheckDTO.getWoCode());
    try {
      String result = autoCheckDTO.getResult();
      if (wo == null) {
        throw new Exception(I18n.getLanguage("wo.woIsNotExistVsmart"));
      }
      UsersEntity ue = userRepository.getUserByUserIdCheck(wo.getFtId());
      if (ue == null) {
        throw new Exception(I18n.getLanguage("wo.InvalidUserName") + " " + "FT");
      }
      UsersInsideDto user = ue.toDTO();
      UnitDTO unit = unitRepository.findUnitById(user.getUnitId());
      Map<String, String> mapConfigProperty = commonRepository.getConfigProperty();
      WoDetailDTO woDetail = woDetailRepository.findWoDetailById(wo.getWoId());
      String commentOK = I18n.getLanguage("wo.autocheckSpmMess");
      String smsMessage = "";
      boolean flagResult = false;
      Long isSendFt = 1L;
      if (result == null || WS_RESULT.N_A.equals(result.trim())) {
        // thuc hien tang so lan Autocheck
        wo.setNumAutoCheck(wo.getNumAutoCheck() == null ? 1L : (wo.getNumAutoCheck() + 1L));
        woRepository.updateWo(wo);
        resultDTO.setKey(RESULT.SUCCESS);
        resultDTO.setObject(wo);
        return resultDTO;
      } else if (result.equals(WS_RESULT.OK)) {
        flagResult = true;
        if (wo.getStatus().equals(Long.valueOf(WO_STATUS.CLOSED_CD))) {
          commentOK = I18n.getLanguage("wo.autocheckUpdateResult");
          commentOK = commentOK.replace("#Result", WS_RESULT.OK);
          smsMessage = generateMessagesContent(wo, result, woDetail.getAccountIsdn());
          isSendFt = 2L;
        } else {
          commentOK = commentOK.replace("#Result", WS_RESULT.OK);
          smsMessage = generateMessagesContent(wo, result, woDetail.getAccountIsdn());
          isSendFt = 2L;
          ResultInSideDto rsDeducteIM = woMaterialDeducteBusiness
              .putMaterialDeducteToIM(wo.getWoId(), false);
          if (!RESULT.SUCCESS.equals(rsDeducteIM.getKey())) {
            throw new Exception(rsDeducteIM.getMessage());
          }
          if (WO_MASTER_CODE.WO_SPM.equals(wo.getWoSystem()) || INSERT_SOURCE.SPM_VTNET
              .equals(wo.getWoSystem())) {
            try {
              if (checkProperty(mapConfigProperty, String.valueOf(wo.getWoTypeId()),
                  Constants.AP_PARAM.WO_TYPE_CDBR)) {
                ServiceProblemInfoDTO spmDto = initServiceProblemInfoDTO(woDetail, wo, user,
                    unit);
                TroublesDTO o = new TroublesDTO();
                o.setReasonLv3Id(spmDto.getReasonL3Id());
                o.setReasonLv3Name(spmDto.getReasonL3Name());
                o.setReasonLv2Id(spmDto.getReasonL2Id());
                o.setReasonLv2Name(spmDto.getReasonL2Name());
                o.setReasonLv1Id(spmDto.getReasonL1Id());
                o.setReasonLv1Name(spmDto.getReasonL1Name());
                o.setTotalPendingTimeGnoc(spmDto.getTotalPendingTimeGnoc());
                o.setTotalProcessTimeGnoc(spmDto.getTotalProcessTimeGnoc());
                o.setEvaluateGnoc(spmDto.getEvaluateGnoc());
                ResultDTO resTT = updateStatusTT(o, wo, null, null, unit, user, commentOK,
                    "CLOSED", "CLOSED_CD", commentOK);
                if (!RESULT.SUCCESS.equals(resTT.getKey())) {
                  throw new Exception(
                      I18n.getLanguage("wo.errCommunicateSPM") + ": resultSpm.getKey() :" + resTT
                          .getMessage());
                }
              }
            } catch (Exception e) {
              log.error(e.getMessage(), e);
              throw new Exception(e.getMessage());
            }
          }
        }
        woDetail.setAutoChecked(null);
      } else if (result.equals(WS_RESULT.NOK)) {
        smsMessage = generateMessagesContent(wo, result, woDetail.getAccountIsdn());
        /// goi sang SPM
        TroublesDTO o = new TroublesDTO();
        String commentTT =
            user.getUsername() + " : " + DateUtil.date2ddMMyyyyHHMMss(new Date()) + " "
                + I18n.getLanguage("wo.updateStatus")
                + ":" + Constants.WO_STATUS_LABEL.INPROCESS + " comment:" + autoCheckDTO
                .getErrorDescription();
        ResultDTO resTT;
        try {
          resTT = updateStatusTT(o, wo, null, null, unit, user, commentTT, "UPDATE",
              Constants.WO_STATUS_LABEL.INPROCESS, null);
          if (!RESULT.SUCCESS.equals(resTT.getKey())) {
            throw new Exception(I18n.getLanguage("wo.errCommunicateSPM") + ": resultSpm.getKey() :"
                + resTT.getMessage());
          }
        } catch (Exception e) {
          log.error(e.getMessage(), e);
          throw new Exception(e.getMessage());
        }
        woDetail.setAutoChecked(null);
        wo.setNumAutoCheck(null);
      }
      woAutoCheckRepository.actionUpdateWoAutoCheck(wo, autoCheckDTO, flagResult);
      actionSendSms(wo, smsMessage, isSendFt);
      resultDTO.setKey(RESULT.SUCCESS);
      resultDTO.setIdValue("1");
      resultDTO.setObject(wo);
    } catch (Exception e) {
      try {
        if (wo != null && !I18n.getLanguage("wo.woIsBusy").equals(e.getMessage())) {
          woMaterialDeducteBusiness.rollBackDeducteToIM(wo.getWoId());
        }
      } catch (Exception ex) {
        log.error(ex.getMessage(), ex);
      }
      log.error(e.getMessage(), e);
      throw new Exception(e.getMessage());
    }
    return resultDTO;
  }

  public void actionSendSms(WoInsideDTO wo, String content, Long isSendFt) {
    Date date = new Date();
    List<MessagesDTO> lstFt = getMessagesForFT(wo, content, date);
    List<MessagesDTO> lstCd = getMessagesForCd(wo, content, date);
    List<MessagesDTO> lstAll = new ArrayList<>();
    if (!lstFt.isEmpty() && (isSendFt.equals(1L) || isSendFt.equals(2L))) {
      lstAll.addAll(lstFt);
    }
    if (!lstCd.isEmpty() && (isSendFt.equals(3L) || isSendFt.equals(2L))) {
      lstAll.addAll(lstCd);
    }
    if (!lstAll.isEmpty()) {
      for (MessagesDTO messagesDTO : lstAll) {
        messagesRepository.insertOrUpdateWfm(messagesDTO);
      }
    }
  }

  public List<MessagesDTO> getMessagesForCd(WoInsideDTO woInsideDTO, String content, Date date) {
    List<MessagesDTO> lstMess = woRepository.getMessagesForCd(woInsideDTO, content, date);
    if (lstMess != null && lstMess.size() > 0) {
      for (MessagesDTO messagesDTO : lstMess) {
        String[] contentArr = content.split("#####");
        String cont = contentArr[0];
        if ("2".equals(messagesDTO.getUserLanguage()) && contentArr.length > 1) {
          cont = contentArr[1];
        }
        messagesDTO.setContent(cont);
        messagesDTO.setSenderId(senderId);
        messagesDTO.setSmsGatewayId(smsGatewayId);
        messagesDTO.setStatus("0");
        messagesDTO
            .setCreateTime(DateTimeUtils.convertDateToString(date, Constants.ddMMyyyyHHmmss));
      }
    }
    return lstMess;
  }

  public List<MessagesDTO> getMessagesForFT(WoInsideDTO woInsideDTO, String content, Date date) {
    List<MessagesDTO> lstMess = woRepository.getMessagesForFT(woInsideDTO, content, date);
    if (lstMess != null && lstMess.size() > 0) {
      for (MessagesDTO messagesDTO : lstMess) {
        String[] contentArr = content.split("#####");
        String cont = contentArr[0];
        if ("2".equals(messagesDTO.getUserLanguage()) && contentArr.length > 1) {
          cont = contentArr[1];
        }
        messagesDTO.setContent(cont);
        messagesDTO.setSenderId(senderId);
        messagesDTO.setSmsGatewayId(smsGatewayId);
        messagesDTO.setStatus("0");
        messagesDTO
            .setCreateTime(DateTimeUtils.convertDateToString(date, Constants.ddMMyyyyHHmmss));
      }
    }
    return lstMess;
  }

  public ServiceProblemInfoDTO initServiceProblemInfoDTO(WoDetailDTO woDetailDTO,
      WoInsideDTO woInsideDTO,
      UsersInsideDto usersInsideDto, UnitDTO unitDTO) {
    ServiceProblemInfoDTO spmDto = new ServiceProblemInfoDTO();
    spmDto.setActionType(Constants.SPM_ACTION_TYPE.UPDATE);  // loai hanh dong la cap nhat
    spmDto.setSpmCode(woDetailDTO.getSpmCode());  // ma spm
    spmDto.setSystemId(String.valueOf(woInsideDTO.getWoId()));
    spmDto.setSystemCode(woInsideDTO.getWoCode());
    spmDto.setCode(woInsideDTO.getWoCode());
    spmDto.setStatusName(Constants.WO_STATUS_LABEL.CLOSED_CD);
    spmDto.setUserNameExecute(usersInsideDto.getUsername());
    spmDto.setInsertSource("WFM");
    spmDto.setReasonDetail("Server: " + I18n.getLanguage("wo.autocheckSpmMess"));

    spmDto.setUnitNameExecute(unitDTO.getUnitName());
    if (woDetailDTO != null && woDetailDTO.getCcResult() != null) { // nguyen nhan 3 cap
      CompCause lv3 = woRepository.getCompCause(woDetailDTO.getCcResult());
      if (lv3 != null) {
        spmDto.setReasonL3Id(lv3.getCompCauseId() + "");
        spmDto.setReasonL3Name(lv3.getName());
        CompCause lv2 = woRepository.getCompCause(lv3.getParentId());
        if (lv2 != null) {
          spmDto.setReasonL2Id(lv2.getCompCauseId() + "");
          spmDto.setReasonL2Name(lv2.getName());
          CompCause lv1 = woRepository.getCompCause(lv2.getParentId());
          if (lv1 != null) {
            spmDto.setReasonL1Id(lv1.getCompCauseId() + "");
            spmDto.setReasonL1Name(lv1.getName());
          }
        }
      }
    }
    spmDto.setReasonOverdueL1(woInsideDTO.getReasonOverdueLV1Id());
    spmDto.setReasonOverdueNameL1(woInsideDTO.getReasonOverdueLV1Name());
    spmDto.setReasonOverdueL2(woInsideDTO.getReasonOverdueLV2Id());
    spmDto.setReasonOverdueNameL2(woInsideDTO.getReasonOverdueLV2Name());
    // bo sung thong tin thoi gian xu ly sang SPM_start
    Double totalTime =
        (woInsideDTO.getFinishTime().getTime() - woInsideDTO.getStartTime().getTime()) / 1000.0 / 60
            / 60;
    List<WoPendingDTO> lstPending = woPendingRepository
        .getListWoPendingByWoId(woInsideDTO.getWoId());
    Double pendingTime = 0.0D;
    if (lstPending != null && lstPending.size() > 0) {
      Long pt = 0L;
      for (WoPendingDTO i : lstPending) {
        if (i.getOpenTime() != null && i.getInsertTime() != null) {
          // chi tinh thoi gian sau khi WO da start
          if (i.getOpenTime().getTime() >= woInsideDTO.getStartTime().getTime()) {
            if (i.getInsertTime().getTime() < woInsideDTO.getStartTime()
                .getTime()) { // tam dung truoc start
              pt = pt + (i.getOpenTime().getTime() - woInsideDTO.getStartTime().getTime());
            } else { // tam dung sau start
              pt = pt + (i.getOpenTime().getTime() - i.getInsertTime().getTime());
            }
          }
        }
      }
      if (pt > 0) {
        pendingTime = pt / 1000.0 / 60 / 60;
      }
    }
    spmDto.setTotalPendingTimeGnoc((Math.round(pendingTime * 100.0) / 100.0) + "");
    spmDto.setTotalProcessTimeGnoc((Math.round((totalTime - pendingTime) * 100.0) / 100.0) + "");
    spmDto.setEvaluateGnoc(new Date().getTime() <= woInsideDTO.getEndTime().getTime() ? "1" : "0");
    // bo sung thong tin thoi gian xu ly sang SPM_end
    spmDto.setProcess(
        DateUtil.date2ddMMyyyyHHMMss(new Date()) + "_" + woInsideDTO.getWoCode() + "_CD:"
            + usersInsideDto
            .getUsername() + "_" + I18n.getLanguage("wo.cdApproveWO") + "_" + I18n
            .getLanguage("wo.autocheckSpmMess") + I18n
            .getLanguage("wo.statusName")
            + Constants.WO_STATUS_LABEL.CLOSED_CD);
    return spmDto;
  }

  public String generateMessagesContent(WoInsideDTO wo, String result, String account) {
    String smsMessage = "";
    if (WS_RESULT.OK.equals(result)) {
      smsMessage = I18n.getLanguage("wo.messFormSqmOkUpdate");
    } else if (WS_RESULT.NOK.equals(result)) {
      smsMessage = I18n.getLanguage("wo.messFormSqmOkNoUpdate");
    } else if (WS_RESULT.N_A.equals(result)) {
      smsMessage = I18n.getLanguage("woMessFormSqmNaNoUpdate");
    }
    smsMessage = smsMessage.replace("[WO_CODE]", wo.getWoCode());
    smsMessage = smsMessage.replace("[ACCOUNT_ISDN]", account);
    return smsMessage;
  }

  public void sendMessages(String smsContent, UsersInsideDto usersInsideDto, String alias) {
    MessagesDTO message = new MessagesDTO();
    message.setSmsGatewayId(smsGatewayId);
    message.setReceiverId(String.valueOf(usersInsideDto.getUserId()));
    message.setReceiverUsername(usersInsideDto.getUsername());
    message.setReceiverFullName(usersInsideDto.getFullname());
    message.setSenderId(senderId);
    message.setReceiverPhone(usersInsideDto.getMobile());
    message.setStatus("0");
    message.setCreateTime(DateTimeUtils.convertDateToString(new Date(), Constants.ddMMyyyyHHmmss));
    message.setContent(smsContent);
    if(StringUtils.isNotNullOrEmpty(alias)){
      message.setAlias(alias);
    }
    messagesRepository.insertOrUpdateWfm(message);
  }

  public void validateUpdateStatusForm(WoUpdateStatusForm form) throws Exception {
    if (StringUtils.isStringNullOrEmpty(form.getWoCode())) {
      throw new Exception(I18n.getLanguage("wo.WoCodeIsNotNull"));
    } else if (StringUtils.isStringNullOrEmpty(form.getUserChange())) {
      throw new Exception(I18n.getLanguage("wo.userChangeIsNotNull"));
    } else if (StringUtils.isStringNullOrEmpty(form.getReasonChange())) {
      throw new Exception(I18n.getLanguage("wo.reasonChangeIsNotNull"));
    } else if (StringUtils.isStringNullOrEmpty(form.getSystemChange())) {
      throw new Exception(I18n.getLanguage("wo.systemChangeIsNotNull"));
    } else if (form.getNewStatus() != null && (
        !WO_STATUS.UNASSIGNED.equals(String.valueOf(form.getNewStatus()))
            && !WO_STATUS.ASSIGNED.equals(String.valueOf(form.getNewStatus()))
            && !WO_STATUS.REJECT.equals(String.valueOf(form.getNewStatus()))
            && !WO_STATUS.DISPATCH.equals(String.valueOf(form.getNewStatus()))
            && !WO_STATUS.ACCEPT.equals(String.valueOf(form.getNewStatus()))
            && !WO_STATUS.INPROCESS.equals(String.valueOf(form.getNewStatus()))
            && !WO_STATUS.CLOSED_FT.equals(String.valueOf(form.getNewStatus()))
            && !WO_STATUS.CLOSED_CD.equals(String.valueOf(form.getNewStatus())))) {
      throw new Exception(I18n.getLanguage("wo.newStatusIsNotValid"));
    } else if (form.getNewStatus() != null && WO_STATUS.CLOSED_CD
        .equals(String.valueOf(form.getNewStatus()))
        && StringUtils.isStringNullOrEmpty(form.getFinishTime())) {
      throw new Exception(I18n.getLanguage("wo.finishTimeIsNotNull"));
    } else {
      try {
        DateUtil.string2DateTime(form.getFinishTime());
      } catch (Exception e) {
        log.error(e.getMessage(), e);
        throw new Exception(I18n.getLanguage("wo.finishTimeIsNotValidFomat"));
      }
    }
  }

  public ResultDTO updateStatus_SCVT(TroublesDTO troublesDTO, WoInsideDTO woInsideDTO,
      WoPendingDTO woPendingDTO, WoDetailDTO woDetailDTO, UnitDTO unitDTO,
      UsersInsideDto usersInsideDto, String worklog, String stateName, String woState,
      String commentComplete) throws Exception {
    TroublesDTO trouble = new TroublesDTO();
    if (troublesDTO != null) {
      trouble.setTotalPendingTimeGnoc(troublesDTO.getTotalPendingTimeGnoc());
      trouble.setTotalProcessTimeGnoc(troublesDTO.getTotalProcessTimeGnoc());
      trouble.setEvaluateGnoc(troublesDTO.getEvaluateGnoc());
      trouble.setReasonLv3Id(
          troublesDTO.getReasonLv3Id() != null ? troublesDTO.getReasonLv3Id() + "" : null);
      trouble.setReasonLv3Name(troublesDTO.getReasonLv3Name());
      trouble.setReasonLv2Id(
          troublesDTO.getReasonLv2Id() != null ? troublesDTO.getReasonLv2Id() + "" : null);
      trouble.setReasonLv2Name(troublesDTO.getReasonLv3Name());
      trouble.setReasonLv1Id(
          troublesDTO.getReasonLv1Id() != null ? troublesDTO.getReasonLv1Id() + "" : null);
      trouble.setReasonLv1Name(troublesDTO.getReasonLv1Name());
      trouble.setInfoTicket(troublesDTO.getInfoTicket());
      trouble.setCustomerTimeDesireFrom(troublesDTO.getCustomerTimeDesireFrom());
      trouble.setCustomerTimeDesireTo(troublesDTO.getCustomerTimeDesireTo());
    }
    trouble.setStateWo(woState);
    trouble.setTroubleCode(woInsideDTO.getWoSystemId());
    if (unitDTO != null) {
      trouble.setReceiveUnitId(String.valueOf(unitDTO.getUnitId()));
      trouble.setReceiveUnitName(unitDTO.getUnitName());
    }
    if (usersInsideDto != null) {
      trouble.setReceiveUserId(String.valueOf(usersInsideDto.getUserId()));
      trouble.setReceiveUserName(usersInsideDto.getUsername());
      trouble.setProcessingUserPhone(usersInsideDto.getMobile());
    }
    //tam dong
    if (woPendingDTO != null) {
      trouble.setDeferredReason(woPendingDTO.getReasonPendingName());
      trouble.setEstimateTime(
          woInsideDTO.getEstimateTime() != null ? DateUtil
              .date2ddMMyyyyHHMMss(woInsideDTO.getEstimateTime())
              : null);
      trouble.setDeferredTime(DateUtil.date2ddMMyyyyHHMMss(woPendingDTO.getEndPendingTime()));
      trouble.setDeferType(String.valueOf(woPendingDTO.getPendingType()));
    }
    trouble.setCellService(woInsideDTO.getCellService());
    trouble.setLongitude(woInsideDTO.getLongitude());
    trouble.setLatitude(woInsideDTO.getLatitude());
    trouble.setConcave(woInsideDTO.getConcaveAreaCode());
    trouble.setGroupSolution(woInsideDTO.getSolutionGroupName());
    trouble
        .setNumPending(
            woInsideDTO.getNumPending() != null ? String.valueOf(woInsideDTO.getNumPending())
                : null);
    // dong
    trouble.setEndTroubleTime(
        woInsideDTO.getFinishTime() != null ? DateUtil
            .date2ddMMyyyyHHMMss(woInsideDTO.getFinishTime())
            : null);
    if (woDetailDTO != null && woDetailDTO.getCcResult() != null) { // nguyen nhan 3 cap
      CompCause lv3 = woRepository.getCompCause(woDetailDTO.getCcResult());
      if (lv3 != null) {
        trouble.setReasonLv3Id(lv3.getCompCauseId() + "");
        trouble.setReasonLv3Name(lv3.getName());
        CompCause lv2 = woRepository.getCompCause(lv3.getParentId());
        if (lv2 != null) {
          trouble.setReasonLv2Id(lv2.getCompCauseId() + "");
          trouble.setReasonLv2Name(lv2.getName());
          CompCause lv1 = woRepository.getCompCause(lv2.getParentId());
          if (lv1 != null) {
            trouble.setReasonLv1Id(lv1.getCompCauseId() + "");
            trouble.setReasonLv1Name(lv1.getName());
          }
        }
      }
    }
    trouble.setReasonOverdueId(woInsideDTO.getReasonOverdueLV1Id());
    trouble.setReasonOverdueName(woInsideDTO.getReasonOverdueLV1Name());
    trouble.setReasonOverdueId2(woInsideDTO.getReasonOverdueLV2Id());
    trouble.setReasonOverdueName2(woInsideDTO.getReasonOverdueLV2Name());
    trouble.setWorkLog(worklog);
    trouble.setStateName(stateName);
    //tiennv cap nhat code WO nang cap
    if (!StringUtils.isStringNullOrEmpty(woInsideDTO.getReasonDetail())) {
      trouble.setRootCause(woInsideDTO.getReasonDetail());
    } else if (!StringUtils.isStringNullOrEmpty(commentComplete)) {
      trouble.setRootCause(commentComplete);
    } else {
      trouble.setRootCause("N/A");
    }

    trouble.setWorkArround(woInsideDTO.getSolutionDetail());
    return ttServiceProxy.onUpdateTroubleFromWo(trouble);
  }

  public ResultDTO updateStatusTT(TroublesDTO troublesDTO, WoInsideDTO woInsideDTO, WoPendingDTO
      woPendingDTO, WoDetailDTO woDetailDTO, UnitDTO unitDTO, UsersInsideDto usersInsideDto,
      String worklog, String stateName, String woState, String commentComplete) throws Exception {
    TroublesDTO trouble = new TroublesDTO();
    if (troublesDTO != null) {
      trouble.setTotalPendingTimeGnoc(troublesDTO.getTotalPendingTimeGnoc());
      trouble.setTotalProcessTimeGnoc(troublesDTO.getTotalProcessTimeGnoc());
      trouble.setEvaluateGnoc(troublesDTO.getEvaluateGnoc());
      trouble.setReasonLv3Id(
          troublesDTO.getReasonLv3Id() != null ? troublesDTO.getReasonLv3Id() + "" : null);
      trouble.setReasonLv3Name(troublesDTO.getReasonLv3Name());
      trouble.setReasonLv2Id(
          troublesDTO.getReasonLv2Id() != null ? troublesDTO.getReasonLv2Id() + "" : null);
      trouble.setReasonLv2Name(troublesDTO.getReasonLv3Name());
      trouble.setReasonLv1Id(
          troublesDTO.getReasonLv1Id() != null ? troublesDTO.getReasonLv1Id() + "" : null);
      trouble.setReasonLv1Name(troublesDTO.getReasonLv1Name());
      trouble.setIsCheck(troublesDTO.getIsCheck());
      //file dinh kem
      trouble.setFileDocumentByteArray(troublesDTO.getFileDocumentByteArray());
      trouble.setArrFileName(troublesDTO.getArrFileName());
      //loi hang loat va ma tram
      trouble.setErrorCode(troublesDTO.getErrorCode());
      trouble.setConcave(troublesDTO.getConcave());
    }
    trouble.setStateWo(woState);
    trouble.setTroubleCode(woInsideDTO.getWoSystemId());
    trouble.setNumPending(
        woInsideDTO.getNumPending() != null ? String.valueOf(woInsideDTO.getNumPending()) : null);
    if (unitDTO != null) {
      trouble.setReceiveUnitId(String.valueOf(unitDTO.getUnitId()));
      trouble.setReceiveUnitName(unitDTO.getUnitName());
    }
    if (usersInsideDto != null) {
      trouble.setReceiveUserId(String.valueOf(usersInsideDto.getUserId()));
      trouble.setReceiveUserName(usersInsideDto.getUsername());
      trouble.setProcessingUserPhone(usersInsideDto.getMobile());
      trouble.setFtInfo(
          "FullName:" + usersInsideDto.getFullname() + " Mobile:" + usersInsideDto.getMobile());
    }
    //tam dong
    if (woPendingDTO != null) {
      trouble.setDeferredReason(woPendingDTO.getReasonPendingName());
      trouble.setDeferredTime(DateUtil.date2ddMMyyyyHHMMss(woPendingDTO.getEndPendingTime()));
    }
    // dong
    trouble.setEndTroubleTime(
        woInsideDTO.getFinishTime() != null ? DateUtil
            .date2ddMMyyyyHHMMss(woInsideDTO.getFinishTime()) : null);
    if (woDetailDTO != null && woDetailDTO.getCcResult() != null) { // nguyen nhan 3 cap
      CompCause lv3 = woRepository.getCompCause(woDetailDTO.getCcResult());
      if (lv3 != null) {
        trouble.setReasonLv3Id(lv3.getCompCauseId() + "");
        trouble.setReasonLv3Name(lv3.getName());
        CompCause lv2 = woRepository.getCompCause(lv3.getParentId());
        if (lv2 != null) {
          trouble.setReasonLv2Id(lv2.getCompCauseId() + "");
          trouble.setReasonLv2Name(lv2.getName());
          CompCause lv1 = woRepository.getCompCause(lv2.getParentId());
          if (lv1 != null) {
            trouble.setReasonLv1Id(lv1.getCompCauseId() + "");
            trouble.setReasonLv1Name(lv1.getName());
          }
        }
      }
    }
    trouble.setReasonOverdueId(woInsideDTO.getReasonOverdueLV1Id());
    trouble.setReasonOverdueName(woInsideDTO.getReasonOverdueLV1Name());
    trouble.setReasonOverdueId2(woInsideDTO.getReasonOverdueLV2Id());
    trouble.setReasonOverdueName2(woInsideDTO.getReasonOverdueLV2Name());
    trouble.setWorkLog(worklog);
    trouble.setStateName(stateName);
    trouble.setRootCause(commentComplete);
    if (StringUtils.isNotNullOrEmpty(woInsideDTO.getSolutionDetail())) {
      trouble.setWorkArround(woInsideDTO.getSolutionDetail());
    } else {
      trouble.setWorkArround(
          StringUtils.isStringNullOrEmpty(commentComplete) ? "WorkArround" : commentComplete);
    }
    return ttServiceProxy.onUpdateTroubleMobile(trouble);
  }

  public static Boolean checkProperty(Map<String, String> map, String id, String key) {
    try {
      String type = map.get(key);
      if (type == null) {
        return false;
      }
      String[] arrType = type.split(",");
      List<String> lst = Arrays.asList(arrType);
      if (lst != null && lst.contains(id)) {
        return true;
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return false;
    }
    return false;
  }

  public void updateWoAndHistory(WoInsideDTO wo, UsersInsideDto user, String comment, String status,
      Date date) {
    WoHistoryInsideDTO woHistory = new WoHistoryInsideDTO();
    woHistory.setCdId(wo.getCdId());
    woHistory.setComments(comment);
    woHistory.setCreatePersonId(wo.getCreatePersonId());
    woHistory.setFileName(wo.getFileName());
    woHistory.setFtId(wo.getFtId());
    woHistory.setNewStatus(Long.valueOf(status));
    woHistory.setOldStatus(wo.getStatus());
    woHistory.setUpdateTime(date);
    woHistory.setUserId(user.getUserId());
    woHistory.setUserName(user.getUsername());
    woHistory.setWoCode(wo.getWoCode());
    woHistory.setWoContent(wo.getWoContent());
    woHistory.setWoId(wo.getWoId());
    wo.setStatus(Long.valueOf(status));
    wo.setLastUpdateTime(date);
    woRepository.updateWo(wo);
    woHistoryRepository.insertWoHistory(woHistory);
  }

  private String convertWoStatus(Long woStatus) {
    if (woStatus == null) {
      return "";
    } else if (woStatus == Long.valueOf(WO_STATUS.UNASSIGNED)) {
      return Constants.WO_STATUS_LABEL.UNASSIGNED;
    } else if (woStatus == Long.valueOf(WO_STATUS.ASSIGNED)) {
      return Constants.WO_STATUS_LABEL.ASSIGNED;
    } else if (woStatus == Long.valueOf(WO_STATUS.REJECT)) {
      return Constants.WO_STATUS_LABEL.REJECT;
    } else if (woStatus == Long.valueOf(WO_STATUS.DISPATCH)) {
      return Constants.WO_STATUS_LABEL.DISPATCH;
    } else if (woStatus == Long.valueOf(WO_STATUS.ACCEPT)) {
      return Constants.WO_STATUS_LABEL.ACCEPT;
    } else if (woStatus == Long.valueOf(WO_STATUS.INPROCESS)) {
      return Constants.WO_STATUS_LABEL.INPROCESS;
    } else if (woStatus == Long.valueOf(WO_STATUS.CLOSED_FT)) {
      return Constants.WO_STATUS_LABEL.CLOSED_FT;
    } else if (woStatus == Long.valueOf(WO_STATUS.CLOSED_CD)) {
      return Constants.WO_STATUS_LABEL.CLOSED_CD;
    } else if (woStatus == Long.valueOf(WO_STATUS.PENDING)) {
      return Constants.WO_STATUS_LABEL.PENDING;
    }

    return "";
  }

  public ResultInSideDto checkCdRole(WoInsideDTO wo, String username) {
    ResultInSideDto resultInSideDto = null;
    List<UsersInsideDto> listUserInGroup = woRepository.getUserOfCD(wo.getCdId());
    if (listUserInGroup == null || listUserInGroup.isEmpty()) {
      resultInSideDto = new ResultInSideDto();
      resultInSideDto.setIdValue("NOK");
      resultInSideDto.setKey(RESULT.ERROR);
      resultInSideDto.setMessage(I18n.getLanguage("wo.SystemErrorCanNotAcceptWO"));
    } else {
      boolean check = false;
      for (UsersInsideDto user : listUserInGroup) {
        if (username.toLowerCase().equals(user.getUsername().toLowerCase())) {
          check = true;
        }
      }
      if (!check) {
        resultInSideDto = new ResultInSideDto();
        resultInSideDto.setIdValue("NOK");
        resultInSideDto.setKey(RESULT.ERROR);
        resultInSideDto.setMessage(I18n.getLanguage("wo.youNotHavePerMissionAccept"));
      }
    }
    return resultInSideDto;
  }

  private List<CrSearchDTO> convertMapDataCR(List<CrDTO> dtoCRResult) {
    List<CrSearchDTO> crSearchDTOS = new ArrayList<>();
    if (dtoCRResult != null) {
      for (CrDTO crDTO : dtoCRResult) {
        crDTO.setState(convertStatusCR(crDTO.getState()));
      }
      for (CrDTO crDTO : dtoCRResult) {
        CrSearchDTO crSearchDTO = new CrSearchDTO();
        crSearchDTO.setCrId(crDTO.getCrId());
        crSearchDTO.setChangeOrginatorName(crDTO.getChangeOrginatorName());
        crSearchDTO.setCrNumber(crDTO.getCrNumber());
        crSearchDTO.setTitle(crDTO.getTitle());
        crSearchDTO.setState(crDTO.getState());
        crSearchDTO.setEarliestStartTime(crDTO.getEarliestStartTime());
        crSearchDTO.setLatestStartTime(crDTO.getLatestStartTime());
        crSearchDTOS.add(crSearchDTO);
      }
    }
    return crSearchDTOS;
  }

  public String convertStatusCR(String stateId) {
    if (stateId != null) {
      return I18n.getLanguage("cr.state." + stateId);
    }
    return "";
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
    String firstLeftHeader = I18n.getLanguage("wo.export.firstLeftHeader");
    String secondLeftHeader = I18n.getLanguage("wo.export.secondLeftHeader");
    String firstRightHeader = I18n.getLanguage("wo.export.firstRightHeader");
    String secondRightHeader = I18n.getLanguage("wo.export.secondRightHeader");
    int mergeTitleEndIndex = 6;
    int startRow = 7;
    int cellTitleIndex = 3;
    switch (code) {
      case "EXPORT_WO":
        sheetName = I18n.getLanguage("wo.export.sheetname");
        title = I18n.getLanguage("wo.export.title");
        fileNameOut = I18n.getLanguage("wo.export.fileNameOut");
        headerPrefix = "language.wo";
        break;
      case "EXPORT_WO_FROM_CR":
        sheetName = I18n.getLanguage("wo.exportFromCr.sheetname");
        title = I18n.getLanguage("wo.exportFromCr.title");
        fileNameOut = I18n.getLanguage("wo.exportFromCr.fileNameOut");
        headerPrefix = "language.wo";
        startRow = 4;
        mergeTitleEndIndex = 3;
        cellTitleIndex = 0;
        firstLeftHeader = null;
        secondLeftHeader = null;
        firstRightHeader = null;
        secondRightHeader = null;
        break;
      case "IMPORT_WO_INFO_RESULT":
        sheetName = I18n.getLanguage("wo.infoAsset.info");
        title = I18n.getLanguage("wo.infoAsset.listMaterialCode");
        fileNameOut = I18n.getLanguage("wo.infoAsset.fileNameOut");
        headerPrefix = "language.wo.infoAsset.import";
        startRow = 4;
        mergeTitleEndIndex = 5;
        cellTitleIndex = 0;
        firstLeftHeader = null;
        secondLeftHeader = null;
        firstRightHeader = null;
        secondRightHeader = null;
        break;
      case "IMPORT_WO_INFO_RESULT_TProperty":
        sheetName = I18n.getLanguage("wo.infoAsset.info");
        title = I18n.getLanguage("wo.infoAsset.listMaterialCode");
        fileNameOut = I18n.getLanguage("wo.infoAsset.fileNameOut");
        headerPrefix = "language.wo.infoAsset.import";
        startRow = 4;
        mergeTitleEndIndex = 5;
        cellTitleIndex = 0;
        firstLeftHeader = null;
        secondLeftHeader = null;
        firstRightHeader = null;
        secondRightHeader = null;
        break;
      case "IMPORT_WO_RESULT":
        sheetName = I18n.getLanguage("wo.export.sheetname");
        title = I18n.getLanguage("WO.add.by.import");
        fileNameOut = "IMPORT_WO_RESULT";
        headerPrefix = "language.wo";
        break;
      default:
        break;
    }
    String subTitle;
    if (StringUtils.isNotNullOrEmpty(date)) {
      subTitle = I18n.getLanguage("wo.export.exportDate", date);
    } else {
      subTitle = "";
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
    if ("IMPORT_WO_INFO_RESULT".equals(code)) {
      configFileExport.setCustomColumnWidthNoMerge(
          new String[]{"1500", "3000", "3000", "4000", "4000", "4000", "23000"});
    }
    if ("IMPORT_WO_INFO_RESULT_TProperty".equals(code)) {
      configFileExport.setCustomColumnWidthNoMerge(
          new String[]{"1500", "3000", "3000", "4000", "4000", "4000", "4000", "23000"});
    }

    configFileExport.setLangKey(I18n.getLocale());
    List<CellConfigExport> cellConfigExportList = new ArrayList<>();
    CellConfigExport cellConfigExport = new CellConfigExport(startRow, 0, 0, 0,
        I18n.getLanguage("wo.STT"), "HEAD", "STRING");
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

  public String getNationFromUnit(Long unitId) {
    UnitDTO unitDTO = unitRepository.findUnitById(unitId);
    if (unitDTO != null && unitDTO.getLocationId() != null) {
      CatLocationDTO catLocationDTO = woRepository.getCatLocationById(unitDTO.getLocationId());
      if (catLocationDTO != null) {
        if (StringUtils.isNotNullOrEmpty(catLocationDTO.getNationCode())) {
          return catLocationDTO.getNationCode();
        }
      }
    }
    return "VNM";
  }

  private void updateParentStatusWhenDelete(WoInsideDTO parentWo, UserToken userToken,
      Double offset)
      throws Exception {
    WoInsideDTO dtoParentSearch = new WoInsideDTO();
    dtoParentSearch.setParentId(parentWo.getWoId());
    dtoParentSearch.setUserId(userToken.getUserID());
    List<WoInsideDTO> listChildWo = woRepository.getListDataSearchWoDTO(dtoParentSearch);
    String newStatus = WO_STATUS.DISPATCH;
    if (listChildWo == null || listChildWo.isEmpty()) {
      newStatus = WO_STATUS.ASSIGNED;
    } else {
      List<String> listChildStatus = new ArrayList();
      listChildWo.stream().filter((child) -> (!listChildStatus.contains(child.getStatus())))
          .forEach((child) -> {
            listChildStatus.add(String.valueOf(child.getStatus()));
          });
      if (listChildStatus.contains(WO_STATUS.REJECT)) {
        newStatus = WO_STATUS.REJECT;
      } else if (listChildStatus.contains(WO_STATUS.DISPATCH)) {
        newStatus = WO_STATUS.DISPATCH;
      } else if (listChildStatus.contains(WO_STATUS.ACCEPT)) {
        newStatus = WO_STATUS.ACCEPT;
      } else if (listChildStatus.contains(WO_STATUS.INPROCESS)) {
        newStatus = WO_STATUS.INPROCESS;
      } else if (listChildStatus.contains(WO_STATUS.CLOSED_FT)) {
        newStatus = WO_STATUS.CLOSED_FT;
      } else if (listChildStatus.contains(WO_STATUS.CLOSED_CD)) {
        newStatus = WO_STATUS.CLOSED_CD;
      }
    }
    if (!newStatus.equals(String.valueOf(parentWo.getStatus()))) {
      WoHistoryInsideDTO woHistoryInsideDTO = new WoHistoryInsideDTO();
      woHistoryInsideDTO.setWoId(parentWo.getWoId());
      woHistoryInsideDTO.setWoCode(parentWo.getWoCode());
      woHistoryInsideDTO.setWoContent(parentWo.getWoContent());
      woHistoryInsideDTO.setFileName(parentWo.getFileName());
      woHistoryInsideDTO.setUserName(userToken.getUserName());
      woHistoryInsideDTO.setUpdateTime(new Date());
      woHistoryInsideDTO.setOldStatus(parentWo.getStatus());
      woHistoryInsideDTO.setNewStatus(Long.valueOf(newStatus));
      woHistoryInsideDTO.setComments("");
      woHistoryInsideDTO.setCreatePersonId(parentWo.getCreatePersonId());
      woHistoryInsideDTO.setCdId(parentWo.getCdId());
      woHistoryInsideDTO.setFtId(parentWo.getFtId());

      parentWo.setStatus(Long.valueOf(newStatus));
      parentWo = convertWoDate2VietNamDate(parentWo, offset);
      String rs = updateWo(parentWo.toModelOutSide());
      if (RESULT.SUCCESS.equals(rs)) {
        woHistoryRepository.insertWoHistory(woHistoryInsideDTO);
      }
    }
  }

  private void insertParentHistory(Long parentId, UserToken userToken, String message) {
    if (parentId != null) {
      WoInsideDTO parentDtoSearch = new WoInsideDTO();
      parentDtoSearch.setWoId(parentId);
      parentDtoSearch.setUserId(userToken.getUserID());
      WoInsideDTO parentDto;
      List<WoInsideDTO> listParentDto = woRepository.getListDataSearchWoDTO(parentDtoSearch);
      if (listParentDto != null && !listParentDto.isEmpty()) {
        parentDto = listParentDto.get(0);
        WoHistoryInsideDTO parentHisDto = new WoHistoryInsideDTO();
        parentHisDto.setNewStatus(parentDto.getStatus());
        parentHisDto.setWoId(parentDto.getWoId());
        parentHisDto.setWoCode(parentDto.getWoCode());
        parentHisDto.setWoContent(parentDto.getWoContent());
        parentHisDto.setUserId(parentDto.getUserId());
        parentHisDto.setUserName(userToken.getUserName());
        parentHisDto.setUpdateTime(new Date());
        parentHisDto.setCreatePersonId(parentDto.getCreatePersonId());
        parentHisDto.setCdId(parentDto.getCdId());
        parentHisDto.setFtId(parentDto.getFtId());
        parentHisDto.setComments(message);
        woHistoryRepository.insertWoHistory(parentHisDto);
      }
    }
  }

  public WoInsideDTO convertWoDate2VietNamDate(WoInsideDTO oldDto, Double offset) {
    oldDto.setStartTime(DateTimeUtils.convertDateToOffset(oldDto.getStartTime(), offset, true));
    oldDto.setEndTime(DateTimeUtils.convertDateToOffset(oldDto.getEndTime(), offset, true));
    oldDto.setCreateDate(DateTimeUtils.convertDateToOffset(oldDto.getCreateDate(), offset, true));
    oldDto.setCompletedTime(
        DateTimeUtils.convertDateToOffset(oldDto.getCompletedTime(), offset, true));
    //tiennv  them
    oldDto.setFinishTime(DateTimeUtils.convertDateToOffset(oldDto.getFinishTime(), offset, true));
    oldDto.setLastUpdateTime(
        DateTimeUtils.convertDateToOffset(oldDto.getLastUpdateTime(), offset, true));
    oldDto.setFtAcceptedTime(
        DateTimeUtils.convertDateToOffset(oldDto.getFtAcceptedTime(), offset, true));
    oldDto.setEndPendingTime(
        DateTimeUtils.convertDateToOffset(oldDto.getEndPendingTime(), offset, true));
    return oldDto;
  }

  public WoInsideDTO converTimeFromClientToServerWeb(WoInsideDTO woInsideDTO,
      Double offsetFromUser) {
    try {
      woInsideDTO.setStartTime(
          DateTimeUtils.convertDateToOffset(woInsideDTO.getStartTime(), offsetFromUser, true));
      woInsideDTO.setStartTimeFrom(
          DateTimeUtils.convertDateToOffset(woInsideDTO.getStartTimeFrom(), offsetFromUser, true));
      woInsideDTO.setStartTimeTo(
          DateTimeUtils.convertDateToOffset(woInsideDTO.getStartTimeTo(), offsetFromUser, true));
      woInsideDTO.setEndTime(
          DateTimeUtils.convertDateToOffset(woInsideDTO.getEndTime(), offsetFromUser, true));
      woInsideDTO.setEndTimeFrom(
          DateTimeUtils.convertDateToOffset(woInsideDTO.getEndTimeFrom(), offsetFromUser, true));
      woInsideDTO.setEndTimeTo(
          DateTimeUtils.convertDateToOffset(woInsideDTO.getEndTimeTo(), offsetFromUser, true));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return woInsideDTO;
  }

  @Override
  public ResultInSideDto splitWoFromWeb(List<MultipartFile> fileAttacks, WoInsideDTO woInsideDTO) {
    UserToken userToken = ticketProvider.getUserToken();
    woInsideDTO = convertWoDate2VietNamDate(woInsideDTO,
        TimezoneContextHolder.getOffsetDouble());//tiennv them update timezone
    WoDTO woDTO = findWoById(
        woInsideDTO.getParentWo() != null ? woInsideDTO.getParentWo().getWoId() : null);
    WoInsideDTO parentWo = woDTO != null ? woDTO.toModelInSide() : null;
    if(parentWo != null) {
      if (parentWo.getFtId() == null) {
        parentWo.setFtId(userToken.getUserID());
      }
      parentWo.setStatus(3L);
    }
    List<String> lstUsername = woInsideDTO.getLstUsername();
    List<String> lstDescription = woInsideDTO.getLstDescription();
    List<Boolean> getFileParent = woInsideDTO.getGetFileParent();
    List<WoTestServiceMapDTO> lstWoTestService = woInsideDTO.getLstWoTestService();
    return splitWo(fileAttacks, woInsideDTO, parentWo, lstUsername, lstDescription, getFileParent,
        lstWoTestService);
  }

  public ResultInSideDto splitWo(List<MultipartFile> fileAttacks, WoInsideDTO woInsideDTO,
      WoInsideDTO parentWo,
      List<String> lstUsername,
      List<String> lstDescription, List<Boolean> getFileParent,
      List<WoTestServiceMapDTO> lstWoTestService) {
    UserToken userToken = ticketProvider.getUserToken();
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    List<UsersEntity> lstUser = new ArrayList<>();
    try {
      for (String us : lstUsername) {
        UsersEntity u = userBusiness.getUserByUserName(us);
        if (u == null) {
          resultInSideDto.setKey(RESULT.ERROR);
          resultInSideDto.setMessage(I18n.getLanguage("wo.InvalidUserName") + us);
          return resultInSideDto;
        }
        lstUser.add(u);
      }
      if (woInsideDTO.getCdIdList() == null || woInsideDTO.getCdIdList().size() == 0) {
        resultInSideDto.setKey(RESULT.ERROR);
        resultInSideDto.setMessage(I18n.getLanguage("wo.cdSplitIsNull"));
        return resultInSideDto;
      }
      int i = 0;
      String fileNameTmp = woInsideDTO.getFileName();
      for (UsersEntity u : lstUser) {
        for (String cd : woInsideDTO.getCdIdList()) {
          if (woInsideDTO.getCreateDate() == null) {
            woInsideDTO.setCreateDate(new Date());
          }
          String woId = woRepository.getSeqTableWo(WO_SEQ);
          woInsideDTO.setWoId(Long.valueOf(woId));
          UnitDTO unitToken = unitRepository.findUnitById(userToken.getDeptId());
          List<GnocFileDto> gnocFileDtos = new ArrayList<>();
          List<String> lstFileName = new ArrayList<>();
          for (MultipartFile multipartFile : fileAttacks) {
            String fullPath = FileUtils
                .saveFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
                    PassTranformer.decrypt(ftpPass), ftpFolder, multipartFile.getOriginalFilename(),
                    multipartFile.getBytes(),
                    FileUtils.createDateOfFileName(woInsideDTO.getCreateDate()));
            String fileName = multipartFile.getOriginalFilename();
            //Start save file old
            lstFileName.add(FileUtils.getFileName(fullPath));
            //End save file old
            GnocFileDto gnocFileDto = new GnocFileDto();
            gnocFileDto.setPath(fullPath);
            gnocFileDto.setFileName(FileUtils.getFileName(fullPath));
            gnocFileDto.setCreateUnitId(userToken.getDeptId());
            gnocFileDto.setCreateUnitName(unitToken.getUnitName());
            gnocFileDto.setCreateUserId(userToken.getUserID());
            gnocFileDto.setCreateUserName(userToken.getUserName());
            gnocFileDto.setCreateTime(woInsideDTO.getCreateDate());
            gnocFileDto.setMappingId(woInsideDTO.getWoId());
            gnocFileDtos.add(gnocFileDto);
          }
          gnocFileRepository
              .saveListGnocFileNotDeleteAll(GNOC_FILE_BUSSINESS.WO, woInsideDTO.getWoId(),
                  gnocFileDtos);
          String fileNameNew = lstFileName.size() > 0 ? String.join(",", lstFileName) : "";
          woInsideDTO.setFileName(fileNameNew);
//          woInsideDTO.setFileName(
//              (StringUtils.isStringNullOrEmpty(woInsideDTO.getFileName()) ? ""
//                  : woInsideDTO.getFileName()) + (StringUtils.isStringNullOrEmpty(fileNameNew) ? "" : ("," + fileNameNew)));
          woInsideDTO.setSyncStatus(null);
//          woInsideDTO.setFileName(fileNameTmp);
          WoInsideDTO woInsideDtoTmp = woInsideDTO;
          woInsideDtoTmp.setFtId(u.getUserId());
          woInsideDtoTmp.setCdId(Long.valueOf(cd));
          woInsideDtoTmp.setWoDescription(lstDescription.get(i));
          // chuyen file tu WO chia nho sang cac con moi them tranh trung
          String fileNameMove;
//          if (StringUtils.isNotNullOrEmpty(woInsideDtoTmp.getFileName())) {
//            fileNameMove = getFileNameMove(woInsideDtoTmp, woInsideDtoTmp, true);
//            if (I18n.getLanguage("wo.fileNotFound").equals(fileNameMove)) {
//              resultInSideDto.setKey(RESULT.ERROR);
//              resultInSideDto.setMessage(fileNameMove);
//              return resultInSideDto;
//            }
//            woInsideDtoTmp.setFileName(fileNameMove);
//            woInsideDTO.setFileName(woInsideDtoTmp.getFileName());
//          }
          // chuyen file tu WO cha sang wo con
          if (StringUtils.isNotNullOrEmpty(parentWo.getFileName()) && getFileParent.get(i)) {
            fileNameMove = getFileNameMove(woInsideDTO, parentWo, false);
            if (I18n.getLanguage("wo.fileNotFound").equals(fileNameMove)) {
              resultInSideDto.setKey(RESULT.ERROR);
              resultInSideDto.setMessage(fileNameMove);
              return resultInSideDto;
            }
            woInsideDtoTmp.setFileName(fileNameMove);
            woInsideDtoTmp.setNotGetFileCfgWhenCreate("1");
          }
          ResultInSideDto res = insertWoCommon(woInsideDtoTmp.toModelOutSide());
          if (RESULT.SUCCESS.equals(res.getKey())) {
            // them moi danh sach cau hinh file test
            if (lstWoTestService != null && lstWoTestService.size() > 0) {
              for (WoTestServiceMapDTO k : lstWoTestService) {
                k.setWoId(res.getIdValue()
                    .substring(res.getIdValue().lastIndexOf("_") + 1, res.getIdValue().length()));
              }
              String sTest = commonStreamServiceProxy
                  .insertOrUpdateListWoTestServiceMap(lstWoTestService);
              if (!RESULT.SUCCESS.equals(sTest)) {
                resultInSideDto.setKey(RESULT.ERROR);
                resultInSideDto.setMessage(I18n.getLanguage("wo.splitWo.err.saveWoTestService"));
                return resultInSideDto;
              }
            }
          } else {
            resultInSideDto.setKey(RESULT.ERROR);
            resultInSideDto.setMessage(res.getMessage());
            return resultInSideDto;
          }
        }
        i++;
      }
      woRepository.updateWo(parentWo);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      resultInSideDto.setKey(RESULT.ERROR);
      resultInSideDto.setMessage(e.getMessage());
      return resultInSideDto;
    }
    resultInSideDto.setId(1L);
    resultInSideDto.setKey(RESULT.SUCCESS);
    resultInSideDto.setMessage(RESULT.SUCCESS);
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto insertWoCommon(WoDTO woDTO) throws Exception {
    ResultInSideDto result = new ResultInSideDto();
    String woCode;
    try {
      SimpleDateFormat dfm = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
      String message = validateAdd(woDTO);
      if (StringUtils.isNotNullOrEmpty(message)) {
        throw new Exception(message);
      }
      String creater = userRepository.getUserName(Long.valueOf(woDTO.getCreatePersonId()));
      if (StringUtils.isStringNullOrEmpty(creater)) {
        throw new Exception(I18n.getLanguage("wo.notExistsCreatePersonId"));
      }
      String woId;
      if (StringUtils.isNotNullOrEmpty(woDTO.getWoId())) {
        woId = woDTO.getWoId();
      } else {
        woId = woRepository.getSeqTableWo(WO_SEQ);
      }
      woCode =
          "WO_" + woDTO.getWoSystem() + "_" + DateUtil.date2StringNoSlash(new Date()) + "_"
              + woId;

      WoHistoryInsideDTO woHistoryInsideDTO = new WoHistoryInsideDTO();
      WoDetailDTO woDetailDTO = new WoDetailDTO();

      woDTO.setWoCode(woCode);
      woDTO.setCreateDate(dfm.format(new Date()));

      UsersEntity usersEntity = userRepository
          .getUserByUserId(Long.valueOf(woDTO.getCreatePersonId()));
      UnitDTO unitToken = unitRepository.findUnitById(usersEntity.getUnitId());
      List<GnocFileDto> gnocFileDtos = new ArrayList<>();
      List<String> lstFileName = new ArrayList<>();
      // bo sung file test dich vu
      if (!"1".equals(woDTO.getNotGetFileCfgWhenCreate())) {
        GnocFileDto gnocFileGuideDtoOld = new GnocFileDto();
        gnocFileGuideDtoOld.setBusinessCode(GNOC_FILE_BUSSINESS.WO_TYPE_FILES_GUIDE);
        gnocFileGuideDtoOld.setBusinessId(Long.valueOf(woDTO.getWoTypeId()));
        List<GnocFileDto> gnocFileGuideDtosOld = gnocFileRepository
            .getListGnocFileByDto(gnocFileGuideDtoOld);
        for (GnocFileDto gnocFileGuideDto : gnocFileGuideDtosOld) {
          if (StringUtils.isNotNullOrEmpty(gnocFileGuideDto.getPath())) {
            byte[] bytes = FileUtils
                .getFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
                    PassTranformer.decrypt(ftpPass), gnocFileGuideDto.getPath());
            String fullPath = FileUtils
                .saveFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
                    PassTranformer.decrypt(ftpPass), ftpFolder, gnocFileGuideDto.getFileName(),
                    bytes,
                    DateUtil.string2DateTime(woDTO.getCreateDate()));
            String fileName = gnocFileGuideDto.getFileName();
            //Start save file old
            lstFileName.add(FileUtils.getFileName(fullPath));
            //End save file old
            GnocFileDto gnocFileDto = new GnocFileDto();
            gnocFileDto.setPath(fullPath);
            gnocFileDto.setFileName(fileName);
            gnocFileDto.setCreateUnitId(usersEntity.getUnitId());
            gnocFileDto.setCreateUnitName(unitToken.getUnitName());
            gnocFileDto.setCreateUserId(usersEntity.getUserId());
            gnocFileDto.setCreateUserName(usersEntity.getUsername());
            gnocFileDto.setCreateTime(DateUtil.string2DateTime(woDTO.getCreateDate()));
            gnocFileDto.setMappingId(Long.valueOf(woId));
            gnocFileDtos.add(gnocFileDto);
          }
        }
      }
      if (StringUtils.isStringNullOrEmpty(woDTO.getFtId())) {
        woDTO.setStatus(WO_STATUS.UNASSIGNED);
        // tao tu SPM thi CD tiep nhan luon
        if (WO_MASTER_CODE.WO_SPM.equals(woDTO.getWoSystem()) || INSERT_SOURCE.SPM_VTNET
            .equals(woDTO.getWoSystem())) {
          woDTO.setStatus(WO_STATUS.ASSIGNED);
        }
      } else {
        woDTO.setStatus(WO_STATUS.DISPATCH);
      }
      // tam dong WO khi tao
      if (StringUtils.isNotNullOrEmpty(woDTO.getCustomerTimeDesireFrom())
          && StringUtils.isStringNullOrEmpty(woDTO.getEndPendingTime())) {
        woDTO.setEndPendingTime(woDTO.getCustomerTimeDesireFrom());
      }
      if (StringUtils.isNotNullOrEmpty(woDTO.getEndPendingTime())) {
        woDTO.setStatus(WO_STATUS.PENDING);
        woDTO.setNumPending("1");
        // luu them thoi gian hen va tinh lai end time
        Date pendingDate = DateTimeUtils.convertStringToDate(woDTO.getEndPendingTime());
        Date endTime = DateTimeUtils.convertStringToDate(woDTO.getEndTime());
        Long timePending = pendingDate.getTime() - new Date().getTime();
        Date endNew = new Date(endTime.getTime() + timePending);

        woDTO.setEndTime(DateTimeUtils.convertDateToString(endNew, Constants.ddMMyyyyHHmmss));
        woDTO
            .setLastUpdateTime(DateTimeUtils.convertDateToString(endNew, Constants.ddMMyyyyHHmmss));

        //luu them nguyen nhan sdt khach hang va thong tin khach hang
        WoPendingEntity woPending = new WoPendingEntity();
        woPending.setWoId(Long.valueOf(woId));
        woPending.setCusPhone("");
        woPending.setCustomer("");
        woPending.setEndPendingTime(pendingDate);
        woPending.setInsertTime(new Date());
        woPending.setReasonPendingId(null);
        woPending.setReasonPendingName("");
        woPendingRepository.insertWoPending(woPending.toDTO());
      }
      // Insert detail
      woDetailDTO.setWoId(Long.valueOf(woId));
      woDetailDTO.setLastUpdateTime(DateTimeUtils.convertStringToDate(woDTO.getCreateDate()));
      woDetailDTO.setAccountIsdn(woDTO.getAccountIsdn());
      woDetailDTO.setCcGroupId(
          StringUtils.isNotNullOrEmpty(woDTO.getCcGroupId()) ? Long.valueOf(woDTO.getCcGroupId())
              : null);
      woDetailDTO.setCcServiceId(StringUtils.isNotNullOrEmpty(woDTO.getCcServiceId()) ? Long
          .valueOf(woDTO.getCcServiceId()) : null);
      woDetailDTO.setTelServiceId(StringUtils.isNotNullOrEmpty(woDTO.getTelServiceId()) ? Long
          .valueOf(woDTO.getTelServiceId()) : null);
      woDetailDTO.setInfraType(
          StringUtils.isNotNullOrEmpty(woDTO.getInfraType()) ? Long.valueOf(woDTO.getInfraType())
              : null);
      woDetailDTO.setCustomerPhone(woDTO.getCustomerPhone());
      woDetailDTO.setSpmCode(woDTO.getSpmCode());
      woDetailDTO.setCustomerName(woDTO.getCustomerName());
      woDetailDTO.setPortCorrectId(woDTO.getPortCorrectId());
      woDetailDTO.setErrTypeNims(woDTO.getErrTypeNims());
      woDetailDTO.setSubscriptionId(woDTO.getSubscriptionId());
      woDetailDTO.setCcPriorityCode(woDTO.getCcPriorityCode());
      woDetailDTO.setCustomerGroupType(woDTO.getCustomerGroupType());
      woDetailDTO.setKtrKeyPoint(woDTO.getKtrKeyPoint());

      if (StringUtils.isNotNullOrEmpty(woDTO.getCcServiceId()) && (WO_MASTER_CODE.WO_SPM
          .equals(woDTO.getWoSystem()) || INSERT_SOURCE.SPM_VTNET.equals(woDTO.getWoSystem()))) {
        woDetailDTO
            .setServiceId(catServiceBusiness.getServiceIdByCcServiceId(woDTO.getCcServiceId()));
        if (woDetailDTO.getServiceId() == null) {
          throw new Exception(I18n.getLanguage("wo.notMapServiceOnGNOC"));
        }
      } else if (StringUtils.isNotNullOrEmpty(woDTO.getServiceId())) {
        woDetailDTO.setServiceId(Long.valueOf(woDTO.getServiceId()));
      }
      // insert bang kenh truyen dich vu
      if (Constants.AP_PARAM.WO_TYPE_KBDV_QLCTKT.equalsIgnoreCase(woDTO.getWoTypeCode())) {
        WoDeclareServiceDTO declareService = new WoDeclareServiceDTO();
        declareService.setWoId(Long.valueOf(woId));
        declareService.setAccount(woDTO.getAccountIsdn());
        declareService.setEffectType(woDTO.getEffectType());
        declareService.setService(woDTO.getService());
        declareService.setSubscriberName(woDTO.getSubscriberName());
        declareService.setTechnicalClues(woDTO.getTechnicalClues());
        result = woDeclareServiceRepository.insertOrUpdateWoDeclareService(declareService);
        if (!RESULT.SUCCESS.equals(result.getKey())) {
          throw new Exception(I18n.getLanguage("wo.err.save.woDeclare"));
        }
      }
      woDetailDTO.setIsCcResult(
          StringUtils.isNotNullOrEmpty(woDTO.getIsCcResult()) ? Long.valueOf(woDTO.getIsCcResult())
              : null);
      woDetailDTO.setCreateDate(DateUtil.string2DateTime(woDTO.getCreateDate()));
      woDetailDTO.setProductCode(woDTO.getProductCode());
      // Insert history
      woHistoryInsideDTO.setWoCode(woCode);
      woHistoryInsideDTO.setNewStatus(Long.valueOf(woDTO.getStatus()));
      woHistoryInsideDTO.setWoId(Long.valueOf(woId));
      woHistoryInsideDTO.setWoContent(woDTO.getWoContent());
      woHistoryInsideDTO.setUserName(creater);
      woHistoryInsideDTO.setUpdateTime(DateUtil.string2DateTime(woDTO.getCreateDate()));
      woHistoryInsideDTO.setCreatePersonId(Long.valueOf(woDTO.getCreatePersonId()));
      woHistoryInsideDTO.setCdId(
          StringUtils.isNotNullOrEmpty(woDTO.getCdId()) ? Long.valueOf(woDTO.getCdId()) : null);
      woHistoryInsideDTO.setFtId(
          StringUtils.isNotNullOrEmpty(woDTO.getFtId()) ? Long.valueOf(woDTO.getFtId()) : null);
      woHistoryInsideDTO.setComments("(" + woDTO.getStartTime() + " - " + woDTO.getEndTime() + ")");
      if (woDTO.getListFileName() != null && woDTO.getListFileName().size() > 0
          && woDTO.getFileArr() != null && woDTO.getFileArr().size() > 0) {
        if (woDTO.getListFileName().size() != woDTO.getFileArr().size()) {
          throw new Exception(I18n.getLanguage("wo.numberFileNotMap"));
        }
        for (int i = 0; i < woDTO.getListFileName().size(); i++) {
          if (extension != null) {
            String[] extendArr = extension.split(",");
            Boolean check = false;
            for (String e : extendArr) {
              if (woDTO.getListFileName().get(i).toLowerCase().endsWith(e.toLowerCase())) {
                check = true;
                break;
              }
            }
            if (!check) {
              throw new Exception(I18n.getLanguage("wo.fileImportInvalidExten"));
            }
          }
          String fullPath = FileUtils
              .saveFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
                  PassTranformer.decrypt(ftpPass), ftpFolder, woDTO.getListFileName().get(i),
                  woDTO.getFileArr().get(i),
                  FileUtils.createDateOfFileName(DateUtil.string2DateTime(woDTO.getCreateDate())));
          //Start save file old
          lstFileName.add(FileUtils.getFileName(fullPath));
          //End save file old
          GnocFileDto gnocFileDto = new GnocFileDto();
          gnocFileDto.setPath(fullPath);
          gnocFileDto.setFileName(FileUtils.getFileName(fullPath));
          gnocFileDto.setCreateUnitId(usersEntity.getUnitId());
          gnocFileDto.setCreateUnitName(unitToken.getUnitName());
          gnocFileDto.setCreateUserId(usersEntity.getUserId());
          gnocFileDto.setCreateUserName(usersEntity.getUsername());
          gnocFileDto.setCreateTime(DateUtil.string2DateTime(woDTO.getCreateDate()));
          gnocFileDto.setMappingId(Long.valueOf(woId));
          gnocFileDtos.add(gnocFileDto);
        }
      }
      gnocFileRepository
          .saveListGnocFileNotDeleteAll(GNOC_FILE_BUSSINESS.WO, Long.valueOf(woId), gnocFileDtos);
      String fileNameNew = lstFileName.size() > 0 ? String.join(",", lstFileName) : "";
      boolean checkFileName = StringUtils.isNotNullOrEmpty(woDTO.getFileName()) && StringUtils
          .isNotNullOrEmpty(fileNameNew);
      woDTO.setFileName(
          (StringUtils.isStringNullOrEmpty(woDTO.getFileName()) ? ""
              : (woDTO.getFileName())) + (checkFileName ? "," : "") + (
              StringUtils.isStringNullOrEmpty(fileNameNew) ? ""
                  : fileNameNew));
      woDTO.setSyncStatus(null);
      if (DataUtil.isNullOrEmpty(message)) {
        WoInsideDTO woInsideDTO = woDTO.toModelInSide();
        result = woRepository.insertWo(woInsideDTO);
        if (!RESULT.SUCCESS.equals(result.getKey())) {
          throw new Exception(I18n.getLanguage("wo.err.save.wo"));
        }
        result = woDetailRepository.insertUpdateWoDetail(woDetailDTO);
        if (!RESULT.SUCCESS.equals(result.getKey())) {
          throw new Exception(I18n.getLanguage("wo.err.save.wo"));
        }
        result = woHistoryRepository.insertWoHistory(woHistoryInsideDTO);
        if (!RESULT.SUCCESS.equals(result.getKey())) {
          throw new Exception(I18n.getLanguage("wo.err.save.wo"));
        }

        WoWorklogInsideDTO woWorklogInsideDTO = new WoWorklogInsideDTO(woInsideDTO);
        woWorklogInsideDTO.setUsername(creater);
        woWorklogInsideDTO.setUpdateTime(new Date());
        result = woWorklogRepository.insertWoWorklog(woWorklogInsideDTO);
        if (!RESULT.SUCCESS.equals(result.getKey())) {
          throw new Exception(I18n.getLanguage("wo.err.save.woWorklog"));
        }
        result.setIdValue(woCode);
        UsersEntity us = null;
        if (woInsideDTO.getFtId() != null) {
          us = userRepository.getUserByUserIdCheck(woInsideDTO.getFtId());
        } else {  // lay CD dau tien quan ly
          List<UsersInsideDto> lst = woRepository.getUserOfCD(woInsideDTO.getCdId());
          if (lst != null && lst.size() > 0) {
            us = lst.get(0).toEntity();
          }
        }
        if (woInsideDTO.getIsCall() != null && woInsideDTO.getIsCall().equals(1L)) {
          String recordFileName;
          Date startCall = new Date();
          String transactionId =
              result.getIdValue() + "_" + DateUtil.dateTime2StringNoSlash(startCall);
          if (woInsideDTO.getFtId() != null) {
            us = userRepository.getUserByUserIdCheck(woInsideDTO.getFtId());
            recordFileName = RECORD_FT_EMERGENCY;
            if (!StringUtils.isStringNullOrEmpty(woInsideDTO.getFtAudioName())) {
              recordFileName = woInsideDTO.getFtAudioName();
            }
          } else {  // lay CD dau tien quan ly
            List<UsersInsideDto> lst = woRepository.getUserOfCD(woInsideDTO.getCdId());
            if (lst != null && lst.size() > 0) {
              us = lst.get(0).toEntity();
            }
            recordFileName = RECORD_CD_EMERGENCY;
            if (!StringUtils.isStringNullOrEmpty(woInsideDTO.getCdAudioName())) {
              recordFileName = woInsideDTO.getCdAudioName();
            }
          }
          if (us != null) {
            LogCallIpccDTO dto = new LogCallIpccDTO();
            dto.setPhone(us.getMobile());
            dto.setRecordFileCode(recordFileName);
            dto.setStartCallTime(
                DateTimeUtils.convertDateToString(startCall, Constants.ddMMyyyyHHmmss));
            dto.setUserName(us.getUsername());
            dto.setTransactionId(transactionId);
            dto.setWoId(woId);

            // goi sang IPCC
            try {
              //lay url ipcc dua vao don vi
              String url = null;
              UnitEntity un = unitRepository.findUnitById(us.getUnitId()).toEntity();
              boolean checkCall = true;
              String nation = Constants.IPCC_CODE.VNM;
              if (un != null) {
                IpccServiceDTO s = new IpccServiceDTO();
                if (un.getIpccServiceId() != null && un.getIpccServiceId().equals(-1L)) {
                  checkCall = false;
                } else if (un.getIpccServiceId() != null) {
                  s.setIpccServiceId(un.getIpccServiceId());
                } else {
                  s.setIsDefault(1L);
                }

                List<IpccServiceDTO> lst = smsGatewayRepository.getListIpccServiceDTO(s);
                if (lst != null && lst.size() > 0) {
                  url = lst.get(0).getUrl();
                  nation = lst.get(0).getIpccServiceCode();
                }
              }

              if (StringUtils.isNotNullOrEmpty(url) && checkCall) {
                AutoCallOutInput input = new AutoCallOutInput();
                input.setPhoneNumber(us.getMobile());
                input.setRecordFileCode(recordFileName);
                input.setResponseUrl(URLFORIPCC);
                input.setTransactionId(transactionId);
                NomalOutput outPut;
                if (Constants.IPCC_CODE.VTP.equals(nation)) {
                  outPut = ipccvtpPort.autoCallout(url, input);
                } else if (Constants.IPCC_CODE.VTZ.equals(nation)) {
                  outPut = ipccvtzPort.autoCallout(url, input);
                } else if (Constants.IPCC_CODE.MVT.equals(nation)) {
                  outPut = ipccmvtPort.autoCallout(url, input);
                } else if (Constants.IPCC_CODE.STL.equals(nation)) {
                  Input inputVTP = new Input();
                  inputVTP.setPhoneNumber(
                      new JAXBElement(new QName("http://ipcc.itpro.vn/xsd", "phoneNumber"),
                          String.class, us.getMobile()));
                  inputVTP.setRecordFileCode(
                      new JAXBElement(new QName("http://ipcc.itpro.vn/xsd", "recordFileCode"),
                          String.class, recordFileName));
                  inputVTP.setResponseUrl(
                      new JAXBElement(new QName("http://ipcc.itpro.vn/xsd", "responseUrl"),
                          String.class, URLFORIPCC));
                  inputVTP.setTransactionId(
                      new JAXBElement(new QName("http://ipcc.itpro.vn/xsd", "transactionId"),
                          String.class, transactionId));
                  Output out = ipccstlPort.autoCallout(url, inputVTP);
                  if (out != null && out.getResultCode().getValue() != null) {
                    outPut = new NomalOutput();
                    if ("200".equals(out.getResultCode().getValue())) {
                      outPut.setDescription("SUCCESS");
                    } else {
                      outPut.setDescription(out.getDescription().getValue());
                    }
                  } else {
                    outPut = new NomalOutput();
                    outPut.setDescription("IPCC not response result");
                  }
                } else {
                  outPut = ipccPort.autoCallout(url, input);
                }
                if (outPut != null && !"SUCCESS".equals(outPut.getDescription())) {
                  dto.setResult("-1");
                  dto.setDescription(outPut.getDescription());
                }
              }
            } catch (Exception e) {
              dto.setDescription(I18n.getMessages("wo.fail.communicate.ipcc"));
              log.error(e.getMessage(), e);
            }
            logCallIpccRepository.insertLogCallIpcc(dto);
          }
        }
        // thuc hien nhan tin
        // nhan tin cho nhan vien kinh doanh
        if (WO_MASTER_CODE.WO_SPM.equals(woInsideDTO.getWoSystem()) || INSERT_SOURCE.SPM_VTNET
            .equals(woInsideDTO.getWoSystem())) {
          StaffBean nvkd = null;
          try {
            nvkd = paymentPort.getStaffByIsdn(woInsideDTO.getAccountIsdn());
          } catch (AbstractMethodError e) {
            log.error(e.getMessage(), e);
          }
          if (nvkd != null) {
            MessagesDTO messageKd = new MessagesDTO();
            if (us != null) {
              String smsContent = "Phan anh cua Account:" + woInsideDTO.getAccountIsdn()
                  + I18n.getLanguage("wo.beingKTV") + us.getUsername() + "(" + us.getMobile()
                  + I18n.getLanguage("wo.acceptAnhProcess") + result.getId() + I18n
                  .getLanguage("wo.at") + DateTimeUtils
                  .convertDateToString(new Date(), Constants.ddMMyyyyHHmmss);
              messageKd.setSmsGatewayId(smsGatewayId);  // fix code = 5
              messageKd.setReceiverId(String.valueOf(nvkd.getStaffId()));
              messageKd.setReceiverUsername(nvkd.getStaffCode());
              messageKd.setReceiverFullName(nvkd.getName());
              messageKd.setSenderId(senderId);  // fix code = 400
              messageKd.setReceiverPhone(nvkd.getTelFax());
              messageKd.setStatus("0");
              messageKd.setCreateTime(
                  DateTimeUtils.convertDateToString(new Date(), Constants.ddMMyyyyHHmmss));
              messageKd.setContent(smsContent);
              messagesRepository.insertOrUpdateWfm(messageKd);
            }
          }
        }

        if (
            WO_MASTER_CODE.WO_TT.equals(woInsideDTO.getWoSystem()) ||
                WO_MASTER_CODE.WO_SPM.equals(woInsideDTO.getWoSystem()) ||
                INSERT_SOURCE.SPM_VTNET.equals(woInsideDTO.getWoSystem()) ||
                WO_MASTER_CODE.WO_CC_SCVT.equals(woInsideDTO.getWoSystem()) ||
                !StringUtils.isStringNullOrEmpty(woDTO.getKedbCode())
        ) {
          WoTroubleInfoDTO woTroubleInfo = new WoTroubleInfoDTO();
          woTroubleInfo.setWoId(Long.valueOf(woId));
          woTroubleInfo.setKedbCode(woInsideDTO.getKedbCode());
          woTroubleInfo.setKedbId(woInsideDTO.getKedbId());
          woTroubleInfo.setRequiredTtReason(woInsideDTO.getRequiredTtReason());
          woTroubleInfo.setAbleMop(woInsideDTO.getAbleMop());
          woTroubleInfo.setCdAudioName(woInsideDTO.getCdAudioName());
          woTroubleInfo.setFtAudioName(woInsideDTO.getFtAudioName());
          woTroubleInfo.setAlarmId(woInsideDTO.getAlarmId());
          woTroubleInfo.setLinkId(woInsideDTO.getLinkId());
          woTroubleInfo.setLinkCode(woInsideDTO.getLinkCode());
          woTroubleInfoRepository.insertWoTroubleInfo(woTroubleInfo);
        }
        // them moi danh sach hang hoa
        if (woInsideDTO.getLstMerchandise() != null && woInsideDTO.getLstMerchandise().size() > 0) {
          List<WoMerchandiseDTO> lstMer = woInsideDTO.getLstMerchandise();
          List<WoMerchandiseInsideDTO> lstMerInside = new ArrayList<>();
          for (WoMerchandiseDTO o : lstMer) {
            o.setWoId(woId);
            lstMerInside.add(o.toModelInSide());
          }
          ResultInSideDto resMerTmp = woMerchandiseRepository.updateListWoMerchandise(lstMerInside);
          if (!RESULT.SUCCESS.equals(resMerTmp.getKey())) {
            throw new Exception(I18n.getMessages("wo.FailInsertWoMerchandise"));
          }
        }
        // luu thong tin KTTS
        WoKTTSInfoDTO woKttsInfo = woInsideDTO.getWoKttsInfo();
        if (woKttsInfo != null) {
          if (woKttsInfo.getContractId() != null || woKttsInfo.getProcessActionName() != null || woKttsInfo.getActiveEnvironmentId() != null) {
            woKttsInfo.setWoId(woInsideDTO.getWoId());
            woKTTSInfoRepository.insertWoKTTSInfo(woKttsInfo);
          }
        }else if(woKttsInfo == null && woInsideDTO.getActiveEnvironmentId() != null){
          WoKTTSInfoDTO woKttsInfo1 = new WoKTTSInfoDTO();
          woKttsInfo1.setWoId(woInsideDTO.getWoId());
          if(woInsideDTO.getActiveEnvironmentId() != null){
            woKttsInfo1.setActiveEnvironmentId(woInsideDTO.getActiveEnvironmentId());
          }
          woKTTSInfoRepository.insertWoKTTSInfo(woKttsInfo1);
        }
      }
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      throw new Exception(ex.getMessage());
    }
    result.setIdValue(RESULT.SUCCESS.equals(result.getKey()) ? woCode : null);
    result.setMessage(result.getKey());
    return result;
  }

  private String validateAdd(WoDTO woDTO) throws ParseException {
    // Wo content
    if (StringUtils.isStringNullOrEmpty(woDTO.getWoContent())) {
      return I18n.getLanguage("wo.woContent.null");
    }  // Wo system
    if (StringUtils.isStringNullOrEmpty(woDTO.getWoSystem())) {
      return I18n.getLanguage("wo.woSystem.null");
    }  // Wo type
    if (StringUtils.isStringNullOrEmpty(woDTO.getWoTypeId())) {
      return I18n.getLanguage("wo.woType.null");
    }  // Priority
    if (StringUtils.isStringNullOrEmpty(woDTO.getPriorityId())) {
      return I18n.getLanguage("wo.priority.null");
    }  // From date
    if (StringUtils.isStringNullOrEmpty(woDTO.getStartTime())) {
      return I18n.getLanguage("wo.startTime.null");
    }  // To date
    if (StringUtils.isStringNullOrEmpty(woDTO.getEndTime())) {
      return I18n.getLanguage("wo.endTime.null");
    }
    if (DateUtil.string2DateTime(woDTO.getStartTime()).getTime() < new Date().getTime()) {
      woDTO.setStartTime(DateUtil.date2ddMMyyyyHHMMss(new Date()));
    }
    if (DateUtil.string2DateTime(woDTO.getStartTime()).getTime() > DateUtil
        .string2DateTime(woDTO.getEndTime()).getTime()) {
      return I18n.getLanguage("wo.startTimeOverEndTime");
    }  // Group dispatch
    if (StringUtils.isStringNullOrEmpty(woDTO.getCdId())) {
      return I18n.getLanguage("wo.cdGroup.null");
    }
    if (StringUtils.isStringNullOrEmpty(woDTO.getCreatePersonId())) {
      return I18n.getLanguage("wo.createPerson.null");
    }
    // thuc hien giao nhan vien truc ca
    if (WO_MASTER_CODE.WO_TT.equals(woDTO.getWoSystem())) {
      Long ftNew = woCdTempRepository.getFtByCdConfig(Long.valueOf(woDTO.getCdId()));
      if (ftNew != null) {
        woDTO.setFtId(String.valueOf(ftNew));
      }
    }
    return "";
  }

  public String getFileNameMove(WoInsideDTO wo, WoInsideDTO woParent, boolean typeCheck) {
    UserToken userToken = ticketProvider.getUserToken();
    UnitDTO unitToken = unitRepository.findUnitById(userToken.getDeptId());
    GnocFileDto gnocFileDto = new GnocFileDto();
    gnocFileDto.setBusinessCode(GNOC_FILE_BUSSINESS.WO);
    gnocFileDto.setBusinessId(woParent.getWoId());
    List<GnocFileDto> gnocFileDtos = gnocFileRepository
        .getListGnocFileByDto(gnocFileDto);
    if (typeCheck) {
      wo.setFileName(null);
    }
    Date now = new Date();
    List<String> listFileName = new ArrayList<>();
    String nameReturn =
        StringUtils.isStringNullOrEmpty(wo.getFileName()) ? "" : wo.getFileName();
    List<GnocFileDto> gnocFileDtos1 = new ArrayList<>();
    for (GnocFileDto dto : gnocFileDtos) {
      try {
        byte[] bytes = FileUtils.getFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
            PassTranformer.decrypt(ftpPass), dto.getPath());
        String fullPath = FileUtils.saveFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
            PassTranformer.decrypt(ftpPass), ftpFolder, (now.getTime()) + "_" + dto.getFileName(),
            bytes, woParent.getCreateDate());
        listFileName.add(FileUtils.getFileName(fullPath));
        GnocFileDto gnocFileDto1 = new GnocFileDto();
        gnocFileDto1.setPath(fullPath);
        gnocFileDto1.setFileName(FileUtils.getFileName(fullPath));
        gnocFileDto1.setCreateUnitId(userToken.getDeptId());
        gnocFileDto1.setCreateUnitName(unitToken.getUnitName());
        gnocFileDto1.setCreateUserId(userToken.getUserID());
        gnocFileDto1.setCreateUserName(userToken.getUserName());
        gnocFileDto1.setCreateTime(wo.getCreateDate());
        gnocFileDto1.setMappingId(wo.getWoId());
        gnocFileDtos1.add(gnocFileDto1);
      } catch (Exception e) {
        log.error(e.getMessage(), e);
        return I18n.getLanguage("wo.fileNotFound");
      }
    }
    gnocFileRepository
        .saveListGnocFileNotDeleteAll(GNOC_FILE_BUSSINESS.WO, wo.getWoId(), gnocFileDtos1);
    String fileNameNew = listFileName.size() > 0 ? String.join(",", listFileName) : "";
    boolean checkFileName =
        StringUtils.isNotNullOrEmpty(nameReturn) && StringUtils.isNotNullOrEmpty(fileNameNew);
    return nameReturn + (checkFileName ? "," : "") + (StringUtils.isStringNullOrEmpty(fileNameNew)
        ? "" : fileNameNew);
  }

  public static List readExcelAddBlankRowXSSF(File file, int iSheet, int iBeginRow, int iFromCol,
      int iToCol, int rowBack) throws IOException {
    List lst = new ArrayList();
    FileInputStream fileInput = null;
    SimpleDateFormat sp = new SimpleDateFormat("dd/MM/yyyy");
    XSSFWorkbook workbook = null;
    try {
      fileInput = new FileInputStream(file);
      workbook = new XSSFWorkbook(fileInput);
      XSSFSheet worksheet = workbook.getSheetAt(iSheet);
      int irowBack = 0;
      for (int i = iBeginRow; i <= worksheet.getLastRowNum(); i++) {
        Object[] obj = new Object[iToCol - iFromCol + 1];
        Row row = worksheet.getRow(i);
        if (row != null && true) {
          int iCount = 0;
          int check = 0;
          for (int j = iFromCol; j <= iToCol; j++) {
            Cell cell = row.getCell(j);
            if (cell != null && true) {
              switch (cell.getCellType()) {
                case STRING:
                  obj[iCount] = cell.getStringCellValue().trim();
                  break;
                case NUMERIC:
                  Double doubleValue = (Double) cell.getNumericCellValue();
                  if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    Date date = HSSFDateUtil.getJavaDate(doubleValue);
                    obj[iCount] = sp.format(date);
                    break;
                  }
                  List<String> lstValue = DataUtil.splitDot(String.valueOf(doubleValue));
                  if (lstValue.get(1).matches("[0]+")) {
                    obj[iCount] = lstValue.get(0);
                  } else {
                    obj[iCount] = String.format("%.2f", doubleValue).trim();
                  }
                  break;
                case BLANK:
                  check++;
                  break;
                default:
                  obj[iCount] = cell.getStringCellValue().trim();
                  break;
              }
            } else {
              obj[iCount] = null;
            }
            iCount += 1;
          }
          if (check != (iToCol - iFromCol + 1)) {
            lst.add(obj);
          }
        } else {
          irowBack += 1;
        }
        if (irowBack == rowBack) {
          break;
        }
      }
    } catch (IOException ex) {
      log.error(ex.getMessage(), ex);
      lst = null;
    } finally {
      try {
        if (fileInput != null) {
          fileInput.close();
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
      try {
        if (workbook != null) {
          workbook.close();
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return lst;
  }

  @Override
  public ResultInSideDto callIPCC(WoInsideDTO woInsideDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    try {
      //lay thong tin nhan vien can goi dien
      WoInsideDTO wo = woRepository.findWoByIdNoOffset(woInsideDTO.getWoId());
      UsersInsideDto us = null;
      String recordFileName;
      Date startCall = new Date();
      if (wo != null) {
        String transactionId = wo.getWoCode() + "_" + DateUtil.dateTime2StringNoSlash(startCall);
        if (wo.getFtId() != null && !String.valueOf(wo.getStatus())
            .equals(WO_STATUS.CLOSED_FT)) {
          us = userRepository.getUserByUserIdCheck(wo.getFtId()).toDTO();
          recordFileName = recordFt;
        } else {  // lay CD dau tien quan ly
          List<UsersInsideDto> lst = woRepository.getUserOfCD(wo.getCdId());
          if (lst != null && lst.size() > 0) {
            us = lst.get(0);
          }
          recordFileName = recordCd;
        }
        if (us != null) {
          LogCallIpccDTO dto = new LogCallIpccDTO();
          dto.setPhone(us.getMobile());
          dto.setRecordFileCode(recordFileName);
          dto.setStartCallTime(
              DateTimeUtils.convertDateToString(startCall, Constants.ddMMyyyyHHmmss));
          dto.setUserName(us.getUsername());
          dto.setTransactionId(transactionId);
          dto.setWoId(String.valueOf(woInsideDTO.getWoId()));
          dto.setUserCall(woInsideDTO.getUserCall());
          logCallIpccRepository.insertLogCallIpcc(dto);
          // goi sang IPCC
          //lay url ipcc dua vao don vi
          String url = null;
          boolean checkCall = true;
          String nation = Constants.IPCC_CODE.VNM;
          UnitDTO un = unitRepository.findUnitById(us.getUnitId());
          if (un != null) {
            IpccServiceDTO s = new IpccServiceDTO();
            if (un.getIpccServiceId() != null && un.getIpccServiceId().equals(-1L)) {
              checkCall = false;
            } else if (un.getIpccServiceId() != null) {
              s.setIpccServiceId(un.getIpccServiceId());
            } else {
              s.setIsDefault(1L);
            }
            List<IpccServiceDTO> lst = smsGatewayRepository.getListIpccServiceDTO(s);
            if (lst != null && lst.size() > 0) {
              url = lst.get(0).getUrl();
              nation = lst.get(0).getIpccServiceCode();
            }
          }
          if (StringUtils.isNotNullOrEmpty(url) && checkCall) {
            AutoCallOutInput input = new AutoCallOutInput();
            input.setPhoneNumber(us.getMobile());
            input.setRecordFileCode(recordFileName);
            input.setResponseUrl(URLFORIPCC);
            input.setTransactionId(transactionId);
            NomalOutput outPut;
            if (Constants.IPCC_CODE.VTP.equals(nation)) {
              outPut = ipccvtpPort.autoCallout(url, input);
            } else if (Constants.IPCC_CODE.VTZ.equals(nation)) {
              outPut = ipccvtzPort.autoCallout(url, input);
            } else if (Constants.IPCC_CODE.MVT.equals(nation)) {
              outPut = ipccmvtPort.autoCallout(url, input);
            } else if (Constants.IPCC_CODE.STL.equals(nation)) {
              Input inputVTP = new Input();
              inputVTP.setPhoneNumber(
                  new JAXBElement(new QName("http://ipcc.itpro.vn/xsd", "phoneNumber"),
                      String.class, us.getMobile()));
              inputVTP.setRecordFileCode(
                  new JAXBElement(new QName("http://ipcc.itpro.vn/xsd", "recordFileCode"),
                      String.class, recordFileName));
              inputVTP.setResponseUrl(
                  new JAXBElement(new QName("http://ipcc.itpro.vn/xsd", "responseUrl"),
                      String.class, URLFORIPCC));
              inputVTP.setTransactionId(
                  new JAXBElement(new QName("http://ipcc.itpro.vn/xsd", "transactionId"),
                      String.class, transactionId));
              Output out = ipccstlPort.autoCallout(url, inputVTP);
              if (out != null && out.getResultCode().getValue() != null) {
                outPut = new NomalOutput();
                if ("200".equals(out.getResultCode().getValue())) {
                  outPut.setDescription(RESULT.SUCCESS);
                } else {
                  outPut.setDescription(out.getDescription().getValue());
                }
              } else {
                outPut = new NomalOutput();
                outPut.setDescription("IPCC not response result");
              }
            } else {
              outPut = ipccPort.autoCallout(url, input);
            }
            if (outPut != null && !RESULT.SUCCESS.equals(outPut.getDescription())) {
              TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
              resultInSideDto.setKey(RESULT.ERROR);
              resultInSideDto.setMessage(outPut.getDescription());
              return resultInSideDto;
            }
          }
        } else {
          TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
          resultInSideDto.setKey(RESULT.ERROR);
          resultInSideDto.setMessage(I18n.getLanguage("wo.notFindUser"));
          return resultInSideDto;
        }
      } else {
        resultInSideDto.setKey(RESULT.ERROR);
        resultInSideDto.setMessage(I18n.getLanguage("wo.woNotExist"));
        return resultInSideDto;
      }
      resultInSideDto.setKey(RESULT.SUCCESS);
    } catch (Exception e) {
      resultInSideDto.setKey(RESULT.ERROR);
      resultInSideDto.setMessage(e.getMessage());
      log.error(e.getMessage(), e);
      TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
    }
    return resultInSideDto;
  }

  @Override
  public Datatable getListLogCallIpccDTO(LogCallIpccDTO logCallIpccDTO) {
    return logCallIpccRepository.getListLogCallIpccDTO(logCallIpccDTO);
  }

  @Override
  public ResultInSideDto exportFileTestService(MultipartFile multipartFile) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    List<String> listWoCode;
    List<WoInsideDTO> listWoAll;
    try {
      if (multipartFile.isEmpty()) {
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
        lstHeader = CommonImport.getDataFromExcelFile(fileImp, 0, 4,
            0, 1, 1000);
        if (lstHeader.size() == 0 || !validFileWoTestFormat(lstHeader)) {
          resultInSideDto.setKey(RESULT.FILE_INVALID_FORMAT);
          return resultInSideDto;
        }
        List<Object[]> lstData = CommonImport.getDataFromExcelFile(fileImp, 0, 5,
            0, 1, 1000);
        if (lstData.size() > maxRecord) {
          resultInSideDto.setKey(RESULT.DATA_OVER);
          resultInSideDto.setMessage(String.valueOf(maxRecord));
          return resultInSideDto;
        }
        listWoCode = new ArrayList<>();
        if (!lstData.isEmpty()) {
          for (Object[] obj : lstData) {
            String woCode = null;
            if (obj[1] != null) {
              woCode = obj[1].toString().trim();
            }
            if (StringUtils.isNotNullOrEmpty(woCode)) {
              listWoCode.add(woCode);
            }
          }
          if (!listWoCode.isEmpty()) {
            listWoAll = new ArrayList<>();
            for (int i = 0; i < listWoCode.size(); i++) {
              String woCode = listWoCode.get(i);
              String woId = woCode.substring(woCode.lastIndexOf("_") + 1, woCode.length());
              WoInsideDTO woInsideDTO = woRepository.findWoByIdNoOffset(Long.valueOf(woId));
              WoInsideDTO woTmp = new WoInsideDTO();
              woTmp.setFileName(woInsideDTO.getFileName());
              woTmp.setWoSystem(woInsideDTO.getWoSystem());
              woTmp.setWoSystemId(woInsideDTO.getWoSystemId());
              woTmp.setWoCode(woCode);
              woTmp.setCreateDate(woInsideDTO.getCreateDate());
              woTmp.setWoContent(woInsideDTO.getWoContent());
              woTmp.setWoId(woInsideDTO.getWoId());
              listWoAll.add(woTmp);
            }
            if (listWoAll == null || listWoAll.size() == 0) {
              resultInSideDto.setKey(RESULT.NODATA);
              resultInSideDto.setMessage(I18n.getLanguage("wo.importNotFindWo"));
            } else {
              resultInSideDto = doExportFileTestService(listWoAll);
            }
          }
        } else {
          resultInSideDto.setKey(RESULT.NODATA);
          resultInSideDto.setMessage(Constants.FILE_NULL);
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
  public List<WoPriorityDTO> getWoPriorityByWoTypeID(Long woTypeId) {
    log.debug("Request to getWoPriorityByWoTypeID : {}", woTypeId);
    return woRepository.getWoPriorityByWoTypeID(woTypeId);
  }

  @Override
  public ResultInSideDto completeWoSPM(WoInsideDTO woInsideDTO) {
    ResultInSideDto resultInSideDto = null;
    UserToken userToken = ticketProvider.getUserToken();
    String comment = woInsideDTO.getComment();
    String reasonLv1Id = woInsideDTO.getReasonLv1Id();
    String reasonLv1Name = woInsideDTO.getReasonLv1Name();
    String reasonLv2Id = woInsideDTO.getReasonLv2Id();
    String reasonLv2Name = woInsideDTO.getReasonLv2Name();
    String reasonLv3Id = woInsideDTO.getReasonLv3Id();
    String reasonLv3Name = woInsideDTO.getReasonLv3Name();
    List<WoInsideDTO> listWoInsideDTO = woInsideDTO.getLstWo();
    if (listWoInsideDTO != null && listWoInsideDTO.size() > 0) {
      for (WoInsideDTO dto : listWoInsideDTO) {
        WoInsideDTO woInsideDTOUpdate = new WoInsideDTO();
        setDataWoDTOUpdate(woInsideDTOUpdate, dto);
        woInsideDTOUpdate.setStatus(Long.valueOf(WO_STATUS.CLOSED_FT));
        woInsideDTOUpdate.setLastUpdateTime(new Date());

        WoHistoryInsideDTO woHistoryInsideDTO = new WoHistoryInsideDTO();
        woHistoryInsideDTO.setOldStatus(dto.getStatus());
        woHistoryInsideDTO.setNewStatus(Long.valueOf(WO_STATUS.CLOSED_FT));
        woHistoryInsideDTO.setWoId(woInsideDTOUpdate.getWoId());
        woHistoryInsideDTO.setWoCode(woInsideDTOUpdate.getWoCode());
        woHistoryInsideDTO.setWoContent(woInsideDTOUpdate.getWoContent());
        woHistoryInsideDTO.setFileName(woInsideDTOUpdate.getFileName());
        woHistoryInsideDTO.setUserName(userToken.getUserName());
        woHistoryInsideDTO.setUpdateTime(new Date());
        woHistoryInsideDTO.setComments("Web: " + (StringUtils.isStringNullOrEmpty(comment) ? I18n
            .getLanguage("wo.comments.startProcess") : comment));
        woHistoryInsideDTO.setCreatePersonId(woInsideDTOUpdate.getCreatePersonId());
        woHistoryInsideDTO.setCdId(woInsideDTOUpdate.getCdId());
        woHistoryInsideDTO.setFtId(woInsideDTOUpdate.getFtId());

        woInsideDTOUpdate.setLstDeducte(null);
        woInsideDTOUpdate.setReasonLv1Id(reasonLv1Id);
        woInsideDTOUpdate.setReasonLv1Name(reasonLv1Name);
        woInsideDTOUpdate.setReasonLv2Id(reasonLv2Id);
        woInsideDTOUpdate.setReasonLv2Name(reasonLv2Name);
        woInsideDTOUpdate.setReasonLv3Id(reasonLv3Id);
        woInsideDTOUpdate.setReasonLv3Name(reasonLv3Name);
        woInsideDTOUpdate.setCheckRefresh(true);
        woInsideDTOUpdate.setWoHistoryInsideDTO(woHistoryInsideDTO);
        woInsideDTOUpdate.setComment(comment);
        woInsideDTOUpdate.setShowMaterialInfo(false);
        woInsideDTOUpdate.setIsShowReasonInfo(false);
        woInsideDTOUpdate.setIsShowReasonOverdueInfo(false);
        woInsideDTOUpdate.setAction(null);

        resultInSideDto = updateStatusFromWeb(woInsideDTOUpdate);
        if (!RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
          return resultInSideDto;
        }
      }
    }
    return resultInSideDto;
  }

  @Override
  public Datatable getListWoChild(WoInsideDTO woInsideDTO) {
    log.debug("Request to getListWoChild: {}", woInsideDTO);
    UserToken userToken = ticketProvider.getUserToken();
    Double offsetFromUser = userRepository.getOffsetFromUser(userToken.getUserID());
    WoInsideDTO searchDto = converTimeFromClientToServerWeb(woInsideDTO, offsetFromUser);
    searchDto.setOffSetFromUser(offsetFromUser);
    Datatable datatable = woRepository.getListWoChild(searchDto);
    return datatable;
  }

  @Override
  public List<WoDTO> getListWoDTO(WoDTO woDTO, int rowStart, int maxRow, String sortType,
      String sortFieldList) {
    if (sortType == null || sortFieldList == null) {
      return null;
    }
    List<WoDTO> list = new ArrayList<>();
    List<WoInsideDTO> listIn = woRepository
        .getListWoDTO(woDTO.toModelInSide(), rowStart, maxRow, sortType, sortFieldList);
    if (listIn != null && listIn.size() > 0) {
      for (WoInsideDTO woInsideDTO : listIn) {
        WoDTO woOutSide = woInsideDTO.toModelOutSide();
        woOutSide.setDefaultSortField("name");
        list.add(woOutSide);
      }
      return list;
    }
    return null;
  }

  @Override
  public List<WoInsideDTO> getListWoDTOByWoSystemId(String woSystemId) {
    return woRepository.getListWoDTOByWoSystemId(woSystemId);
  }

  @Override
  public ResultDTO getResultCallIPCC(String userName, String passWord, String input) {
    ResultDTO result = new ResultDTO();
    try {
      String[] arrInput = input.split(";");
      List<LogCallIpccDTO> lst = logCallIpccRepository
          .getListLogCallIpccByTransactionId(arrInput[2]);

      if (lst != null && lst.size() > 0) {
        LogCallIpccDTO dto = lst.get(0);
        dto.setResultTime(DateTimeUtils.date2ddMMyyyyHHMMss(new Date()));
        dto.setResult(arrInput[3]);
        dto.setDescription(arrInput[4]);

        logCallIpccRepository.insertLogCallIpcc(dto);
      }
      result.setKey(RESULT.SUCCESS);
    } catch (Exception e) {
      result.setKey(RESULT.FAIL);
      result.setMessage(e.getMessage());
      log.error(e.getMessage(), e);
    }
    return result;
  }

  @Override
  public List<WoInsideDTO> getListWoByWoTypeId(Long woTypeId) {
    return woRepository.getListWoByWoTypeId(woTypeId);
  }

  @Override
  public ResultDTO insertWo(WoDTO woDTO) throws Exception {
    ResultDTO dto = new ResultDTO();
    Long woId = Long.valueOf(woDTO.getWoId());
    WoInsideDTO woInsideDTO = woRepository.findWoByIdNoOffset(woId);
    if (woInsideDTO != null) {
      dto.setKey(RESULT.ERROR);
      dto.setMessage(I18n.getLanguage("wo.woId.exists"));
      return dto;
    }
    UsersEntity usersEntity = new UsersEntity();
    UnitDTO unitToken = new UnitDTO();
    if (StringUtils.isNotNullOrEmpty(woDTO.getCreatePersonId())) {
      usersEntity = userRepository.getUserByUserId(Long.valueOf(woDTO.getCreatePersonId()));
      unitToken = unitRepository.findUnitById(usersEntity.getUnitId());
    }
    try {
      List<GnocFileDto> gnocFileDtos = new ArrayList<>();
      List<String> lstFileName = new ArrayList<>();
      GnocFileDto gnocFileGuideDtoOld = new GnocFileDto();
      gnocFileGuideDtoOld.setBusinessCode(GNOC_FILE_BUSSINESS.WO_TYPE_FILES_GUIDE);
      gnocFileGuideDtoOld.setBusinessId(Long.valueOf(woDTO.getWoTypeId()));
      List<GnocFileDto> gnocFileGuideDtosOld = gnocFileRepository
          .getListGnocFileByDto(gnocFileGuideDtoOld);
      for (GnocFileDto gnocFileGuideDto : gnocFileGuideDtosOld) {
        if (StringUtils.isNotNullOrEmpty(gnocFileGuideDto.getPath())) {
          byte[] bytes = FileUtils
              .getFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
                  PassTranformer.decrypt(ftpPass), gnocFileGuideDto.getPath());
          String fullPath = FileUtils
              .saveFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
                  PassTranformer.decrypt(ftpPass), ftpFolder, gnocFileGuideDto.getFileName(), bytes,
                  FileUtils.createDateOfFileName(DateUtil
                      .convertStringToTime(woDTO.getCreateDate(), Constants.ddMMyyyyHHmmss)));
          String fileName = gnocFileGuideDto.getFileName();
          //Start save file old
          lstFileName.add(FileUtils.getFileName(fullPath));
          //End save file old
          GnocFileDto gnocFileDto = new GnocFileDto();
          gnocFileDto.setPath(fullPath);
          gnocFileDto.setFileName(FileUtils.getFileName(fullPath));
          gnocFileDto.setCreateUnitId(usersEntity.getUnitId());
          gnocFileDto.setCreateUnitName(unitToken.getUnitName());
          gnocFileDto.setCreateUserId(usersEntity.getUserId());
          gnocFileDto.setCreateUserName(usersEntity.getUsername());
          gnocFileDto.setCreateTime(DateUtil.string2DateTime(woDTO.getCreateDate()));
          gnocFileDto.setMappingId(woId);
          gnocFileDtos.add(gnocFileDto);
        }
      }
      gnocFileRepository
          .saveListGnocFileNotDeleteAll(GNOC_FILE_BUSSINESS.WO, woId, gnocFileDtos);
      String fileNameNew = lstFileName.size() > 0 ? String.join(",", lstFileName) : "";
      boolean checkFileName = StringUtils.isNotNullOrEmpty(woDTO.getFileName()) && StringUtils
          .isNotNullOrEmpty(fileNameNew);
      woDTO.setFileName(
          (StringUtils.isStringNullOrEmpty(woDTO.getFileName()) ? ""
              : woDTO.getFileName()) + (checkFileName ? "," : "") + (
              StringUtils.isStringNullOrEmpty(fileNameNew) ? ""
                  : fileNameNew));
      woDTO.setSyncStatus(null);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    ResultInSideDto res = woRepository.insertWo(woDTO.toModelInSide());
    if (res.getKey().equals("SUCCESS")) {
      //  thangdt bo sung loai Wo = "Xử lý sự cố cáp" thì sinh wo2 ="Củng cố khôi phục cáp"
      Map<String, String> mapConfigProperty = commonRepository.getConfigProperty();
      if (checkProperty(mapConfigProperty, String.valueOf(woDTO.toModelInSide().getWoTypeId()),
          Constants.AP_PARAM.WO_TYPE_XLSCC)) {
        UsersInsideDto usersFtDto = new UsersInsideDto();
        usersFtDto.setUsername(usersEntity.getUsername());
        prepareWoTypeXLSCCUpdate(woDTO.toModelInSide(), usersFtDto);
      }
    }
    dto.setId(res.getId() != null ? String.valueOf(res.getId()) : null);
    dto.setKey(res.getKey());
    dto.setMessage(res.getKey());
    return dto;
  }

  @Override
  public ResultDTO updateWoInfo(WoDTO woDTO) throws Exception {
    ResultDTO resultDTO = new ResultDTO();
    try {
      if (StringUtils.isStringNullOrEmpty(woDTO.getWoCode())) {
        resultDTO.setId("1");
        resultDTO.setKey(RESULT.SUCCESS);
        resultDTO.setMessage(RESULT.SUCCESS);
        return resultDTO;
      }
      WoInsideDTO wo = woRepository.getWoByWoCode(woDTO.getWoCode());
      if (wo == null) {
        resultDTO.setId("1");
        resultDTO.setKey(RESULT.SUCCESS);
        resultDTO.setMessage(RESULT.SUCCESS);
        return resultDTO;
      }
      UsersEntity u = null;
      Locale locale;
      Date endTimeOld = wo.getEndTime();
      if (wo.getFtId() != null) {
        u = userRepository.getUserByUserId(wo.getFtId());
        if ("2".equals(u.getUserLanguage())) {
          locale = new Locale("en");
        } else {
          locale = new Locale("vi");
        }
        if (locale != null) {
          LocaleContextHolder.setLocale(locale);
        }
      }

      String comment = "";
      String smsContent = "";
      WoDetailDTO detail = woDetailRepository.findWoDetailById(wo.getWoId());
      SimpleDateFormat dfm = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

      //tiennv cap nhat nang cap WO
      if ("TT".equals(wo.getWoSystem())) {
        //extent thoi gian theo TT
        if (!StringUtils.isStringNullOrEmpty(woDTO.getEstimateTime())) {
          try {
            Double estimate = Double.valueOf(woDTO.getEstimateTime());
            Double estMinute = estimate * 60;
            Date d = wo.getEndTime();
            wo.setEndTime(new Date(d.getYear(), d.getMonth(), d.getDate(), d.getHours(),
                d.getMinutes() + estMinute.intValue(), d.getSeconds()));
            comment = "Change end time from TT";
          } catch (Exception e) {
            log.error(e.getMessage(), e);
          }
        }
        // cap nhat qua han WO
        if (!StringUtils.isStringNullOrEmpty(woDTO.getEndTime())) {
          comment = "NOCPRO change end time from: " + dfm.format(wo.getEndTime()) + " to: " + woDTO
              .getEndTime();
          wo.setEndTime(DateTimeUtils.convertStringToDate(woDTO.getEndTime()));
        }
      } else if (WO_MASTER_CODE.WO_SPM.equals(wo.getWoSystem())
          || INSERT_SOURCE.SPM_VTNET.equals(wo.getWoSystem())
          || WO_MASTER_CODE.WO_CC_SCVT.equals(wo.getWoSystem())) {
        // tich hot
        if (StringUtils.isNotNullOrEmpty(woDTO.getIsHot()) && "1"
            .equals(woDTO.getIsHot())) {
          detail.setIsHot(1L);
          wo.setPriorityId(woRepository
              .getPriorityHot(Constants.AP_PARAM.WO_TYPE_CDBR, Constants.PRIORITY_BRCD.HOT));
          detail.setCcPriorityCode(commonRepository
              .getConfigPropertyValue(Constants.CONFIG_PROPERTY_KEY.CC_PRIORITY_HOT));

          // tinh lai thoi gian endTime dua vao estimate time do ticket truyen vao
          if (StringUtils.isNotNullOrEmpty(woDTO.getEstimateTime())) {
            try {
              Long now = new Date().getTime();
              Long start = wo.getStartTime().getTime();
              Long end = wo.getEndTime().getTime();

              Double estimate = Double.valueOf(woDTO.getEstimateTime());
              estimate = estimate * 60 * 60 * 1000;
              Long esTmp = estimate.longValue();

              Long endTmp = now + esTmp;

              if (now < start) { // chua den gio bat dau ma bi tich hot
                endTmp = start + esTmp;
              }

              if (endTmp < end) {
                wo.setEndTime(new Date(endTmp));
              }
            } catch (Exception e) {
              log.error(e.getMessage(), e);
            }
          }

//          if (wo.getEndTime() != null) {
//            Long totalTime =
//                new Date().getTime() + Integer.parseInt(woDTO.getEstimateTime()) * 60 * 60 * 1000;
//            if (wo.getEndTime().getTime() > totalTime) {
//              wo.setEndTime(new Date(totalTime));
//            }
//          }
          String endTime = DateUtil.date2ddMMyyyyHHMMss(wo.getEndTime());

          smsContent =
              I18n.getLanguage("wo.sms.customer") + "[" + detail.getAccountIsdn() + "] " + I18n
                  .getLanguage("wo.sms.tickHot") + "," + I18n.getLanguage("wo.endTimeDiv") + "["
                  + endTime + "]. " + I18n.getLanguage("wo.sms.priorityProcess");

          comment = I18n.getLanguage("wo.sms.customer") + "[" + detail.getAccountIsdn() + "] "
              + I18n.getLanguage("wo.sms.tickHot") + ","
              + I18n.getLanguage(("wo.endTimeDiv")
              + endTime);
        }
        // tich tang so lan phan anh
        if (StringUtils.isNotNullOrEmpty(woDTO.getNumComplaint()) && "1"
            .equals(woDTO.getNumComplaint())) {
          detail
              .setNumComplaint(
                  detail.getNumComplaint() == null ? 2L : detail.getNumComplaint() + 1);
          smsContent =
              I18n.getLanguage("wo.sms.customer") + "[" + detail.getAccountIsdn() + "] " + I18n
                  .getLanguage("wo.sms.callBack") + " " + I18n.getLanguage("wo.sms.times") + " "
                  + detail.getNumComplaint() + "," + I18n.getLanguage("wo.sms.priorityProcess");
          comment =
              I18n.getLanguage("wo.sms.customer") + "[" + detail.getAccountIsdn() + "] " + I18n
                  .getLanguage("wo.sms.callBack") + " " + I18n.getLanguage("wo.sms.times") + " "
                  + detail.getNumComplaint();
        }
        // cap nhat description
        if (StringUtils.isNotNullOrEmpty(woDTO.getWoDescription())) {
          wo.setWoDescription((wo.getWoDescription() != null ? wo.getWoDescription() : "")
              + "\r\n" + dfm.format(new Date()) + ": " + woDTO.getWoDescription());
          smsContent =
              I18n.getLanguage("wo.sms.customer") + ": " + detail.getCustomerName() + " Account"
                  + "["
                  + detail.getAccountIsdn() + "] " + I18n.getLanguage("wo.sms.addComment") + "[ "
                  + woDTO.getWoDescription() + "] " + I18n.getLanguage("wo.sms.noteProcess");

          comment =
              I18n.getLanguage("wo.sms.customer") + ": " + detail.getCustomerName() + " Account"
                  + "[" + detail.getAccountIsdn() + "] "
                  + I18n.getLanguage("wo.sms.addComment") + "[ "
                  + woDTO.getWoDescription() + "]";
        }
        // cap nhat lai thoi gian khach hang mong muon
        if (StringUtils.isNotNullOrEmpty(woDTO.getCustomerTimeDesireFrom()) && StringUtils
            .isNotNullOrEmpty(woDTO.getCustomerTimeDesireTo())) {
          // thuc hien tam dung WO
          if (!wo.getStatus().equals(Long.valueOf(Constants.WO_STATUS.CLOSED_CD))
              && !wo.getStatus().equals(Long.valueOf(Constants.WO_STATUS.CLOSED_FT))) {
            wo.setCustomerTimeDesireFrom(
                DateTimeUtils.convertStringToDate1(woDTO.getCustomerTimeDesireFrom()));
            wo.setCustomerTimeDesireTo(
                DateTimeUtils.convertStringToDate1(woDTO.getCustomerTimeDesireTo()));
            smsContent =
                (detail.getCcPriorityCode() != null ? "[" + detail.getCcPriorityCode() + "]" : "")
                    + I18n.getLanguage("wo.sms.customer") + " "
                    + (detail.getCustomerName() != null ? detail.getCustomerName() : "") + "["
                    + detail.getAccountIsdn() + "] " + I18n.getLanguage("wo.sms.desireWO") + " "
                    + woDTO.getCustomerTimeDesireFrom() + " "
                    + I18n.getLanguage("wo.sms.atTimeTo") + " "
                    + woDTO.getCustomerTimeDesireTo()
                    + I18n.getLanguage("wo.sms.forWo") + " "
                    + woDTO.getWoCode() + I18n.getLanguage("wo.sms.pleaseCheckAgain");

            comment =
                (detail.getCcPriorityCode() != null ? "[" + detail.getCcPriorityCode() + "]" : "")
                    + I18n.getLanguage("wo.sms.customer") + " "
                    + (detail.getCustomerName() != null ? detail.getCustomerName() : "") + "["
                    + detail.getAccountIsdn() + "] " + I18n.getLanguage("wo.sms.desireWO")
                    + woDTO.getCustomerTimeDesireFrom() + " " + I18n.getLanguage("wo.sms.atTimeTo")
                    + woDTO.getCustomerTimeDesireTo() + I18n.getLanguage("wo.sms.forWo") + ""
                    + woDTO.getWoCode();

            VsmartUpdateForm updateForm = new VsmartUpdateForm();
            UsersEntity us = userRepository.getUserByUserId(wo.getCreatePersonId());
            ResultInSideDto res = pendingWoCommon(updateForm, wo.getWoCode(),
                DateTimeUtils.convertStringToDate1(woDTO.getCustomerTimeDesireFrom()),
                us.getUsername(), "CUSTOMER", "Customer change pending time ", null, null, null,
                false, false);
            if (!res.getKey().equals(RESULT.SUCCESS)) {
              throw new Exception(res.getMessage());
            }
//            wo = (WoInsideDTO) res.getObject();
//            wo.setCustomerTimeDesireFrom(
//                DateTimeUtils.convertStringToDate1(woDTO.getCustomerTimeDesireFrom()));
//            wo.setCustomerTimeDesireTo(
//                DateTimeUtils.convertStringToDate1(woDTO.getCustomerTimeDesireTo()));
          } else {
            resultDTO.setId("1");
            resultDTO.setKey(RESULT.SUCCESS);
            resultDTO.setMessage(RESULT.SUCCESS);
            return resultDTO;
          }
          if (Constants.WO_STATUS.PENDING.equals(String.valueOf(wo.getStatus()))) {
            wo.setEndPendingTime(
                DateTimeUtils.convertStringToDate(woDTO.getCustomerTimeDesireFrom()));
          }
        }

//        if (StringUtils.isNotNullOrEmpty(woDTO.getEndTime())) {
//          wo.setEndTime(DateUtil.string2DateTime(woDTO.getEndTime()));
//          String changEndTime = "[" + (endTimeOld == null ? null
//              : DateUtil.date2ddMMyyyyHHMMss(endTimeOld)) + "]" + " to: " + "[" + woDTO
//              .getEndTime() + "]";
//          comment = I18n.getLanguage("wo.sms.changeEndTime") + changEndTime;
//        }
      }

      if (wo.getFtId() != null && u != null && StringUtils.isNotNullOrEmpty(smsContent)) {
        MessagesDTO message = new MessagesDTO();
        message.setSmsGatewayId(smsGatewayId);
        message.setReceiverId(String.valueOf(u.getUserId()));
        message.setReceiverUsername(u.getStaffCode());
        message.setReceiverFullName(u.getFullname());
        message.setSenderId(senderId);
        message.setReceiverPhone(u.getMobile());
        message.setStatus("0");
        message.setCreateTime(
            DateTimeUtils.convertDateToString(new Date(), Constants.ddMMyyyyHHmmss));
        message.setContent(smsContent);
        messagesRepository.insertOrUpdateWfm(message);
      }
      UsersEntity us = userRepository.getUserByUserId(wo.getCreatePersonId());
      updateWoAndHistory(wo, us.toDTO(), comment, String.valueOf(wo.getStatus()), new Date());
      woDetailRepository.insertUpdateWoDetail(detail);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw new Exception(e.getMessage());
    }
    resultDTO.setId("1");
    resultDTO.setKey(RESULT.SUCCESS);
    resultDTO.setMessage(RESULT.SUCCESS);
    return resultDTO;
  }

  @Override
  public List<SearchWoKpiCDBRForm> searchWoKpiCDBR(String startTime, String endTime) {
    try {
      return woRepository.searchWoKpiCDBR(startTime, endTime);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return null;
    }
  }

  @Override
  public ResultDTO createWoTKTU(WoDTO createWoDto) {
    ResultDTO result = new ResultDTO();
    try {
      String woTypeCode = createWoDto.getWoTypeId();
      // lay Id loai cong viec dua vao ma cong viec
      WoTypeDTO woTypeDTO = new WoTypeDTO();
      woTypeDTO.setWoTypeCode(woTypeCode);
      WoTypeInsideDTO dto = woTypeDTO.toWoTypeInsideDTO();
      List<WoTypeInsideDTO> lst = woTypeBusiness.getListWoTypeByLocale(dto, null);
      if (lst != null && lst.size() > 0) {
        createWoDto.setWoTypeId(lst.get(0).toWoTypeDTO().getWoTypeId());
        ResultInSideDto resultInSideDto = createWoCommon(createWoDto);
        result.setKey(resultInSideDto.getKey());
        result.setMessage(resultInSideDto.getMessage());
        result.setId(resultInSideDto.getIdValue());
      } else {
        result.setKey(RESULT.FAIL);
        result.setMessage(I18n.getLanguage("wo.woTypeDoesNotExixts"));
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      result.setKey(RESULT.FAIL);
      result.setMessage(e.getMessage());
    }
    return result;
  }

  @Override
  public List<String> getSequenseWo(String seqName, int... size) {
    int number = (size[0] > 0 ? size[0] : 1);
    return woRepository.getSequenseWo(seqName, number);
  }

  @Override
  public ResultDTO updatePendingWo(String woCode, String endPendingTime, String user,
      String comment, String system, boolean callCC) throws Exception {
    ResultDTO resultDTO = new ResultDTO();
    Date endPending = StringUtils.isNotNullOrEmpty(endPendingTime) ? DateTimeUtils
        .convertStringToDateTime(endPendingTime) : null;
    ResultInSideDto res = updatePendingWoCommon(woCode, endPending, user, comment, system, callCC);
    resultDTO.setId(res.getIdValue());
    resultDTO.setKey(res.getKey().equals(RESULT.SUCCESS) ? res.getKey() : RESULT.FAIL);
    resultDTO.setMessage(res.getMessage());
    return resultDTO;
  }

  @Override
  public ResultDTO updateWoForSPM(WoDTO woDTO) {
    return updateFileForWo(woDTO);
  }

  @Override
  public ResultDTO changeStatusWo(WoUpdateStatusForm updateForm) {
    ResultDTO resultDTO = new ResultDTO();
    try {
      return changeStatusWoCommon(updateForm).toResultDTO();
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      resultDTO.setId("0");
      resultDTO.setKey(RESULT.FAIL);
      resultDTO.setMessage(e.getMessage());
      return resultDTO;
    }
  }

  @Override
  public ResultDTO createWoFollowNode(WoDTO createWoDto, List<String> listNode) {
    List<WoDTO> lstWo = new ArrayList<>();
    ResultDTO result = new ResultDTO();
    try {
      Map<String, String> mapConfigProperty = commonRepository.getConfigProperty();
      if (StringUtils.isStringNullOrEmpty(createWoDto.getWoTypeId())) {
        result.setKey(RESULT.FAIL);
        result.setMessage(I18n.getLanguage("wo.woTypeNull"));
        return result;
      }
      if (!checkProperty(mapConfigProperty, createWoDto.getWoTypeId(),
          Constants.AP_PARAM.WO_TYPE_AMI_ONE)) {
        if (listNode == null || listNode.isEmpty()) {
          result.setKey(RESULT.FAIL);
          result.setMessage(I18n.getLanguage("wo.listNodeNull"));
          return result;
        }
      }
      if (StringUtils.isStringNullOrEmpty(createWoDto.getIsSendFT())) { // giao cho FT
        // luong tao Wo AMI.ONE xac dinh FT hoac CD dua vao dia ban
        if (checkProperty(mapConfigProperty, createWoDto.getWoTypeId(),
            Constants.AP_PARAM.WO_TYPE_AMI_ONE)) {
          // xac dinh FT dua vao ma dia ban
          List<SysUsersBO> lstU = cdPort.getListUserByLocation(createWoDto.getLocationCode());
          Long ft = null;
          if (lstU != null && lstU.size() > 0) {
            ft = userRepository.getUserId(lstU.get(0).getUsername());
            if (ft != null) {
              createWoDto.setFtId(String.valueOf(ft));
            }
          } else if (lstU == null || lstU.size() == 0
              || ft == null) { // xac dinh cd dua vao ma dia ban
            if (StringUtils.isStringNullOrEmpty(createWoDto.getCdId())) {
              List<ResultGetDepartmentByLocationForm> lst = wsHTNims
                  .getSubscriptionInfo(createWoDto.getLocationCode());
              if (lst != null && lst.size() > 0) {
                for (ResultGetDepartmentByLocationForm i : lst) {
                  if (i.getDepartmentLevel() != null && i.getDepartmentLevel()
                      .equals(4L)) { // don vi muc huyen
                    UnitDTO unit = woRepository.getUnitCodeMapNims(i.getDeptCode(), null);
                    if (unit != null) {
                      Long cd = woRepository.getCdByUnitId(unit.getUnitId(), 4L);
                      if (cd != null) {
                        createWoDto.setCdId(String.valueOf(cd));
                        break;
                      }
                    }
                  }
                }
              }
            }
          }
          // fix loai cong nghe va dich vu
          createWoDto.setCcServiceId(commonRepository
              .getConfigPropertyValue(Constants.CONFIG_PROPERTY_KEY.AMI_ONE_CC_SERVICE_ID));
          createWoDto.setTelServiceId(commonRepository
              .getConfigPropertyValue(Constants.CONFIG_PROPERTY_KEY.AMI_ONE_TEL_SERVICE_ID));
          createWoDto.setInfraType(commonRepository
              .getConfigPropertyValue(Constants.CONFIG_PROPERTY_KEY.AMI_ONE_INFRA_TYPE_ID));
          createWoDto.setServiceId(commonRepository
              .getConfigPropertyValue(Constants.CONFIG_PROPERTY_KEY.AMI_ONE_SERVICE_ID));
          lstWo.add(createWoDto);
        } else {
          // Get Ft follow node code
          List<HeaderForm> lstheader = new ArrayList<>();
          lstheader.add(new HeaderForm("nationCode", createWoDto.getNationCode()));
          lstheader.add(new HeaderForm("locale ", I18n.getLocale()));
          cdPort.setLstHeader(lstheader);
          for (String node : listNode) {
            // Lay ma nha tram theo node mang
            String stationCode = woRepository
                .getStationFollowNode(node, createWoDto.getNationCode());
            if (StringUtils.isStringNullOrEmpty(stationCode)) {
              result.setKey(RESULT.FAIL);
              result.setMessage(I18n.getLanguage("wo.nodeNotExist"));
              return result;
            }
            createWoDto.setStationCode(stationCode);
            createWoDto.setDeviceCode(node);

            //lay nhan vien quan ly tram
            String usedMajorCode = commonRepository
                .getConfigPropertyValue(Constants.AP_PARAM.USED_MAJOR_CODE);
            WoTypeInsideDTO woType = woTypeBusiness
                .findWoTypeById(Long.valueOf(createWoDto.getWoTypeId()));
            List<Staff> lstStaff = cdPort
                .getListStaff(stationCode, woType != null ? woType.getUserTypeCode() : null,
                    usedMajorCode);
            WoDTO dtoStaff = prepairWoToCreate(lstStaff, createWoDto);

            if (StringUtils.isStringNullOrEmpty(createWoDto.getCreateWoType())
                || createWoDto.getCreateWoType()
                .equals(Constants.CREATE_WO_TYPE.ASSIGN_STAFF)) {//null || = 0
              // neu ko co FT va CD thuc hien lay CD dua vao ma tram
              if (StringUtils.isStringNullOrEmpty(dtoStaff.getCdId())) {
                WoCdGroupInsideDTO group = getCdByStationCode(stationCode,
                    woType != null ? woType.getWoTypeId() : null, "4");
                if (group != null) {
                  dtoStaff.setCdId(String.valueOf(group.getWoGroupId()));
                  dtoStaff.setMessageCreate(null);
                } else {
                  dtoStaff.setMessageCreate(I18n.getLanguage("wo.cdGroupNotExist"));
                }
              }
              if (StringUtils.isStringNullOrEmpty(dtoStaff.getMessageCreate())) {
                lstWo.add(dtoStaff);
              } else {
                result.setKey(RESULT.FAIL);
                result.setMessage(dtoStaff.getMessageCreate());
                return result;
              }
            } else if (StringUtils.isNotNullOrEmpty(createWoDto.getCreateWoType())
                && (createWoDto.getCreateWoType()
                .equals(Constants.CREATE_WO_TYPE.ASSIGN_OUTSOURCE)
                || createWoDto.getCreateWoType()
                .equals(Constants.CREATE_WO_TYPE.ASSIGN_BOTH))) {
              //lay nhan vien cua doi tac
              List<Staff> lstOutsource = cdPort.getListPartnerStaff(stationCode);

              WoDTO dtoOutsource = prepairWoToCreate(lstOutsource, createWoDto);

              // neu ko co FT va CD thuc hien lay CD dua vao ma tram
              if (dtoOutsource.getCdId() == null) {
                WoCdGroupInsideDTO group = getCdByStationCode(stationCode, woType.getWoTypeId(),
                    "4");
                if (group != null) {
                  dtoOutsource.setCdId(String.valueOf(group.getWoGroupId()));
                  dtoOutsource.setMessageCreate(null);
                } else {
                  dtoOutsource.setMessageCreate(I18n.getLanguage("wo.cdGroupNotExist"));
                }
              }
              if (StringUtils.isStringNullOrEmpty(dtoOutsource.getMessageCreate())) {
                lstWo.add(dtoOutsource);
              }
              if ((StringUtils.isNotNullOrEmpty(dtoOutsource.getMessageCreate())
                  || createWoDto.getCreateWoType()
                  .equals(Constants.CREATE_WO_TYPE.ASSIGN_BOTH))
                  && StringUtils.isStringNullOrEmpty(dtoStaff.getMessageCreate())) {
                lstWo.add(dtoStaff);
              } else if (StringUtils.isNotNullOrEmpty(dtoStaff.getMessageCreate())
                  && StringUtils.isNotNullOrEmpty(dtoOutsource.getMessageCreate())) {
                result.setKey(RESULT.FAIL);
                result.setMessage(dtoStaff.getMessageCreate());
                return result;
              }
            }
          }
        }
        String woCode = "";
        for (WoDTO wo : lstWo) {
          String woId = woRepository.getSeqTableWo(WO_SEQ);
          wo.setWoId(woId);
          ResultInSideDto res = insertWoCommon(wo);
          if (RESULT.ERROR.equals(res.getKey())) {
            result.setKey(RESULT.FAIL);
            result.setMessage(
                StringUtils.isNotNullOrEmpty(res.getMessage()) ? res.getMessage() : null);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return result;
          }
          woCode += res.getIdValue() + ",";
        }
        if (woCode.length() > 0) {
          woCode = woCode.substring(0, woCode.length() - 1);
        }
        result.setId(woCode);
        result.setKey(RESULT.SUCCESS);
        result.setMessage(RESULT.SUCCESS);
      } else // giao viec cho CD
        if (StringUtils.isNotNullOrEmpty(createWoDto.getUnitCode())) {
          //dua vao don vi xac dinh nhom dieu phoi
          WoCdGroupInsideDTO cdGroup = woRepository
              .getCdByUnitCode(createWoDto.getUnitCode(), Long.valueOf(createWoDto.getWoTypeId()),
                  "3");
          if (cdGroup != null) {
            createWoDto.setCdId(String.valueOf(cdGroup.getWoGroupId()));
            String woId = woRepository.getSeqTableWo(WO_SEQ);
            createWoDto.setWoId(woId);
            ResultInSideDto res = insertWoCommon(createWoDto);
            if (RESULT.ERROR.equals(res.getKey())) {
              result.setKey(RESULT.FAIL);
              result.setMessage(
                  StringUtils.isNotNullOrEmpty(res.getMessage()) ? res.getMessage() : null);
              TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
              return result;
            }
            result.setId(res.getIdValue());
            result.setKey(RESULT.SUCCESS);
            result.setMessage(RESULT.SUCCESS);
          } else {
            result.setKey(RESULT.FAIL);
            result.setMessage(I18n.getLanguage("wo.cdGroupNotExist"));
            return result;
          }
        } else {
          result.setKey(RESULT.FAIL);
          result.setMessage(I18n.getLanguage("wo.unitCodeIsNotNull"));
          return result;
        }
    } catch (MalformedURLException em) {
      result.setMessage(ErrorUtils.printLog(em));
      result.setKey(RESULT.FAIL);
      TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
      log.error(em.getMessage(), em);
    } catch (Exception ex) {
      result.setMessage(ErrorUtils.printLog(ex));
      result.setKey(RESULT.FAIL);
      TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
      log.error(ex.getMessage(), ex);
    }
    return result;
  }

  @Override
  public String getNationFromUnitId(Long unitId) {
    return getNationFromUnit(unitId);
  }

  @Override
  public ObjResponse getWoDetail(String woId, String userId) {
    ObjResponse obj = new ObjResponse();
    if (StringUtils.isNotNullOrEmpty(woId)) {
      Double offsetFromUser;
      WoInsideDTO dtoSearch = new WoInsideDTO();
      dtoSearch.setWoId(Long.valueOf(woId));
      if (StringUtils.isNotNullOrEmpty(userId)) {
        dtoSearch.setUserId(Long.valueOf(userId));
        offsetFromUser = userRepository.getOffsetFromUser(Long.valueOf(userId));
      } else {
        dtoSearch.setUserId(null);
        offsetFromUser = 0D;
      }
      dtoSearch.setOffSetFromUser(offsetFromUser);
      WoInsideDTO woDetail = woRepository.getListDataSearch1(dtoSearch) == null ? null
          : woRepository.getListDataSearch1(dtoSearch).get(0);
      //check quyen phe duyet
      String canApprove = "0";
      if (woDetail != null) {
        if (woDetail.getCdId() != null
            && (woDetail.getStatus().equals(Long.valueOf(Constants.WO_STATUS.CLOSED_FT))
            || woDetail.getStatus().equals(Constants.WO_STATUS_LABEL.CLOSED_FT))
            && woDetail.getResult() == null) {
          List<UsersInsideDto> listUserInCd = woCategoryServiceProxy
              .getListCdByGroup(woDetail.getCdId());
          if (listUserInCd != null && !listUserInCd.isEmpty()) {
            for (UsersInsideDto tmp : listUserInCd) {
              if (tmp.getUserId().equals(Long.valueOf(userId))) {
                canApprove = "1";
                break;
              }
            }
          }
        }
        // List Field
        List<FieldForm> lstFieldWo = new ArrayList<>();
        lstFieldWo.add(
            new FieldForm("woId", I18n.getLanguage("wo.detail.title.woId"),
                String.valueOf(woDetail.getWoId()), 0L));
        lstFieldWo.add(new FieldForm("parentId", I18n.getLanguage("wo.detail.title.parentId"),
            String.valueOf(woDetail.getParentId()), 0L));
        lstFieldWo.add(new FieldForm("parentName", I18n.getLanguage("wo.detail.title.parentName"),
            woDetail.getParentName(), 1L));
        lstFieldWo.add(
            new FieldForm("woCode", I18n.getLanguage("wo.detail.title.woCode"),
                woDetail.getWoCode(), 1L));
        lstFieldWo.add(new FieldForm("woContent", I18n.getLanguage("wo.detail.title.woContent"),
            woDetail.getWoContent(), 1L));
        lstFieldWo.add(new FieldForm("woSystem", I18n.getLanguage("wo.detail.title.woSystem"),
            woDetail.getWoSystem(), 1L));
        lstFieldWo.add(new FieldForm("woSystemId", I18n.getLanguage("wo.detail.title.woSystemId"),
            woDetail.getWoSystemId(), 1L));
        lstFieldWo.add(
            new FieldForm("woSystemOutId", I18n.getLanguage("wo.detail.title.woSystemOutId"),
                woDetail.getWoSystemOutId(), 0L));
        lstFieldWo.add(
            new FieldForm("createPersonId", I18n.getLanguage("wo.detail.title.createPersonId"),
                String.valueOf(woDetail.getCreatePersonId()), 0L));
        lstFieldWo.add(
            new FieldForm("createPersonName", I18n.getLanguage("wo.detail.title.createPersonName"),
                woDetail.getCreatePersonName(), 1L));
        lstFieldWo.add(new FieldForm("createDate", I18n.getLanguage("wo.detail.title.createDate"),
            woDetail.getCreateDate() == null ? null : woDetail.getCreateDate().toString(), 1L));
        lstFieldWo.add(new FieldForm("woTypeId", I18n.getLanguage("wo.detail.title.woTypeId"),
            String.valueOf(woDetail.getWoTypeId()), 0L));
        lstFieldWo.add(new FieldForm("woTypeName", I18n.getLanguage("wo.detail.title.woTypeName"),
            woDetail.getWoTypeName(), 1L));
        lstFieldWo.add(
            new FieldForm("cdId", I18n.getLanguage("wo.detail.title.cdId"),
                String.valueOf(woDetail.getCdId()), 0L));
        lstFieldWo.add(
            new FieldForm("cdName", I18n.getLanguage("wo.detail.title.cdName"),
                woDetail.getCdName(), 1L));
        lstFieldWo.add(
            new FieldForm("ftId", I18n.getLanguage("wo.detail.title.ftId"),
                String.valueOf(woDetail.getFtId()), 0L));
        lstFieldWo.add(
            new FieldForm("ftName", I18n.getLanguage("wo.detail.title.ftName"),
                woDetail.getFtName(), 1L));
        lstFieldWo.add(
            new FieldForm("status", I18n.getLanguage("wo.detail.title.status"),
                String.valueOf(woDetail.getStatus()), 0L));
        lstFieldWo.add(new FieldForm("statusName", I18n.getLanguage("wo.detail.title.statusName"),
            woDetail.getStatusName(), 1L));
        lstFieldWo.add(new FieldForm("priorityId", I18n.getLanguage("wo.detail.title.priorityId"),
            String.valueOf(woDetail.getPriorityId()), 0L));
        lstFieldWo.add(
            new FieldForm("priorityName", I18n.getLanguage("wo.detail.title.priorityName"),
                woDetail.getPriorityName(), 1L));
        lstFieldWo.add(new FieldForm("startTime", I18n.getLanguage("wo.detail.title.startTime"),
            woDetail.getStartTime() == null ? null : woDetail.getStartTime().toString(), 1L));
        lstFieldWo
            .add(new FieldForm("startTimeFrom", I18n.getLanguage("wo.detail.title.startTimeFrom"),
                woDetail.getStartTimeFrom() == null ? null : woDetail.getStartTimeFrom().toString(),
                0L));
        lstFieldWo.add(new FieldForm("startTimeTo", I18n.getLanguage("wo.detail.title.startTimeTo"),
            woDetail.getStartTimeTo() == null ? null : woDetail.getStartTimeTo().toString(), 0L));
        lstFieldWo.add(new FieldForm("endTime", I18n.getLanguage("wo.detail.title.endTime"),
            woDetail.getEndTime() == null ? null : woDetail.getEndTime().toString(), 1L));
        lstFieldWo.add(new FieldForm("endTimeFrom", I18n.getLanguage("wo.detail.title.endTimeFrom"),
            woDetail.getEndTimeFrom() == null ? null : woDetail.getEndTimeFrom().toString(), 0L));
        lstFieldWo.add(new FieldForm("endTimeTo", I18n.getLanguage("wo.detail.title.endTimeTo"),
            woDetail.getEndTimeTo() == null ? null : woDetail.getEndTimeTo().toString(), 0L));
        lstFieldWo.add(new FieldForm("finishTime", I18n.getLanguage("wo.detail.title.finishTime"),
            woDetail.getFinishTime() == null ? null : woDetail.getFinishTime().toString(), 1L));
        lstFieldWo.add(new FieldForm("result", I18n.getLanguage("wo.detail.title.result"),
            String.valueOf(woDetail.getResult()), 0L));
        lstFieldWo.add(new FieldForm("resultName", I18n.getLanguage("wo.detail.title.resultName"),
            woDetail.getResult() != null ? getResultName(String.valueOf(woDetail.getResult())) : "",
            1L));
        lstFieldWo.add(new FieldForm("stationId", I18n.getLanguage("wo.detail.title.stationId"),
            String.valueOf(woDetail.getStationId()), 0L));
        lstFieldWo.add(new FieldForm("stationCode", I18n.getLanguage("wo.detail.title.stationCode"),
            woDetail.getStationCode(), 1L));
        lstFieldWo.add(
            new FieldForm("lastUpdateTime", I18n.getLanguage("wo.detail.title.lastUpdateTime"),
                woDetail.getLastUpdateTime() == null ? null
                    : woDetail.getLastUpdateTime().toString(), 0L));
        lstFieldWo.add(new FieldForm("fileName", I18n.getLanguage("wo.detail.title.fileName"),
            woDetail.getFileName(), 1L));
        lstFieldWo.add(new FieldForm("comments", I18n.getLanguage("wo.detail.title.comments"),
            woDetail.getComments(), 1L));
        lstFieldWo.add(
            new FieldForm("canApprove", I18n.getLanguage("wo.detail.title.canApprove"), canApprove,
                0L));
        obj.setLstField(lstFieldWo);
        // List child wo
        WoInsideDTO dtoListChild = new WoInsideDTO();
        dtoListChild.setUserId(Long.valueOf(userId));
        dtoListChild.setParentId(Long.valueOf(woId));
        List<WoInsideDTO> listChildWoDTO =
            woRepository.getListDataSearch1(dtoListChild) == null ? null
                : woRepository.getListDataSearch1(dtoListChild);
        List<WoDTOSearch> listChildWo = new ArrayList<>();
        if (listChildWoDTO != null && !listChildWoDTO.isEmpty()) {
          for (WoInsideDTO woInsideDTO : listChildWoDTO) {
            listChildWo.add(woInsideDTO.toWoDTOSearch());
          }
          obj.setListChildWo(listChildWo);
        }
        // List user in group dispatch
        if (woDetail.getCdId() != null) {
          List<UsersInsideDto> listUserInCd = woCategoryServiceProxy
              .getListCdByGroup(woDetail.getCdId());
          if (listUserInCd != null) {
            List<UsersDTO> list = new ArrayList<>();
            for (UsersInsideDto usersInsideDto : listUserInCd) {
              list.add(usersInsideDto.toOutSideDto());
            }
            obj.setListUserInCd(list);
          }
        }
        // List history
        List<WoHistoryInsideDTO> listHistory = getListDataByWoId(Long.valueOf(woId));
        if (listHistory != null) {
          List<WoHistoryDTO> list = new ArrayList<>();
          for (WoHistoryInsideDTO woHistoryInsideDTO : listHistory) {
            list.add(woHistoryInsideDTO.toModelOutSide());
          }
          obj.setListHistory(list);
        }
        // File dinh kem
        if (StringUtils.isNotNullOrEmpty(woDetail.getFileName())) {
          List<FileNameDTO> listFile = new ArrayList<>();
          String[] fileArr = woDetail.getFileName().split(",");
          String filePath = FileUtils.createPathByDate(woDetail.getCreateDate());
          for (String file : fileArr) {
            FileNameDTO fileDto = new FileNameDTO();
            fileDto.setFileName(file.trim());
            fileDto.setFilePath(
                uploadFolder + File.separator + filePath + File.separator + file.trim());
            listFile.add(fileDto);
          }
          if (listFile != null && !listFile.isEmpty()) {
            obj.setListFile(listFile);
          }
        }
      }
    }
    return obj;
  }

  @Override
  public List<CompCause> getListReasonOverdue(Long parentId, String nationCode) {
    List<CompCause> lst = new ArrayList<>();
    String leeLocale = I18n.getLocale();
    try {
      lst = ws_cc_direction.getListReasonOverdue(parentId, nationCode);
      Map<String, Object> map = getSqlLanguageExchange(Constants.LANGUAGUE_EXCHANGE_SYSTEM.WO,
          Constants.APPLIED_BUSSINESS.REASON_OVERDUE, leeLocale);
      String sqlLanguage = (String) map.get("sql");
      Map mapParam = (Map) map.get("mapParam");
      Map mapType = (Map) map.get("mapType");
      List<LanguageExchangeDTO> lstLanguage = languageExchangeRepository
          .findBySql(sqlLanguage, mapParam, mapType, LanguageExchangeDTO.class);
      lst = setLanguage(lst, lstLanguage, "compCauseId", "name");
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return lst;
  }

  @Override
  public List<WoDTOSearch> getListWOByUsers(String userId, String summaryStatus, Integer isDetail,
      WoDTOSearch woDTO, int start, int count, int typeSearch) {
    return woRepository
        .getListWOByUsers(userId, summaryStatus, isDetail, woDTO, start, count, typeSearch);
  }

  @Override
  public ResultDTO pendingWo(String woCode, String endPendingTime, String user, String system,
      String reasonName, String reasonId, String customer, String phone) throws Exception {
    ResultDTO resultDTO = new ResultDTO();
    Date endPending = null;
    try {
      endPending = DateTimeUtils.convertStringToDateTime(endPendingTime);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    ResultInSideDto res = pendingWoCommon(null, woCode, endPending, user, system, reasonName,
        reasonId, customer, phone, true, false);
    resultDTO.setKey(RESULT.SUCCESS.equals(res.getKey()) ? RESULT.SUCCESS : RESULT.FAIL);
    resultDTO.setMessage(res.getMessage());
    return resultDTO;
  }

  public List<WoHistoryInsideDTO> getListDataByWoId(Long woId) {
    UserToken userToken = ticketProvider.getUserToken();
    Double offsetFromUser;
    if (userToken != null) {
      offsetFromUser = userRepository.getOffsetFromUser(userToken.getUserID());
    } else {
      offsetFromUser = 0D;
    }
    return woHistoryRepository.getListDataByWoId(woId, offsetFromUser);
  }

  public void setMapResultName() {
    Datatable dataResult = catItemRepository
        .getItemMaster(WO_MASTER_CODE.WO_RESULT_STATUS,
            LANGUAGUE_EXCHANGE_SYSTEM.COMMON, APPLIED_BUSSINESS.CAT_ITEM.toString(),
            Constants.ITEM_ID,
            Constants.ITEM_NAME);
    List<CatItemDTO> listResult = (List<CatItemDTO>) dataResult.getData();
    if (listResult != null && !listResult.isEmpty()) {
      for (CatItemDTO dto : listResult) {
        mapResultName.put(dto.getItemValue(), dto.getItemName());
      }
    }
  }

  public String getResultName(String result) {
    setMapResultName();
    String resultName = mapResultName.get(result);
    return StringUtils.isNotNullOrEmpty(resultName) ? resultName : result;
  }

  public WoDTO prepairWoToCreate(List<Staff> lstStaff, WoDTO dto) {
    WoDTO woFollowNode = dto;
    boolean hasStaff = false;
    boolean hasStaffInGNOC = false;
    boolean hasCD = false;
    if (lstStaff != null && !lstStaff.isEmpty()) {
      for (Staff staff : lstStaff) {
        if (staff.getStaffType() != null && staff.getStaffType() == 1l) {
          hasStaff = true;
          Long ftId = userRepository.getUserId(staff.getUserName());
          if (ftId == null) {
            continue;
          }
          hasStaffInGNOC = true;
          woFollowNode.setFtId(String.valueOf(ftId));
          if (dto.getCdId() == null) {
            WoCdGroupInsideDTO cdGroup = woRepository.getCdByFT(ftId,
                Long.valueOf(dto.getWoTypeId()), "4");
            if (cdGroup == null) {
              continue;
            }
            hasCD = true;
            woFollowNode.setCdId(String.valueOf(cdGroup.getWoGroupId()));
          } else {
            hasCD = true;
          }
          break;
        }
      }
    } else {
      woFollowNode.setMessageCreate(I18n.getLanguage("wo.stationNotHaveStaff"));
    }
    if (!hasCD) {
      if (hasStaffInGNOC) {
        woFollowNode.setMessageCreate(I18n.getLanguage("wo.staffNotHaveCD"));
      } else if (hasStaff) {
        woFollowNode.setMessageCreate(I18n.getLanguage("wo.staffNotInGNOC"));
      } else {
        woFollowNode.setMessageCreate(I18n.getLanguage("wo.stationNotHaveStaff"));
      }
    }
    return woFollowNode;
  }

  public void setDataWoDTOUpdate(WoInsideDTO woInsideDTOUpdate, WoInsideDTO dtoOld) {
    woInsideDTOUpdate.setWoId(dtoOld.getWoId());
    woInsideDTOUpdate.setWoCode(dtoOld.getWoCode());
    woInsideDTOUpdate.setWoTypeId(dtoOld.getWoTypeId());
    woInsideDTOUpdate.setParentId(dtoOld.getParentId());
    woInsideDTOUpdate.setWoContent(dtoOld.getWoContent());
    woInsideDTOUpdate.setWoSystem(dtoOld.getWoSystem());
    woInsideDTOUpdate.setStationCode(dtoOld.getStationCode());
    woInsideDTOUpdate.setStartTime(dtoOld.getStartTime());
    woInsideDTOUpdate.setEndTime(dtoOld.getEndTime());
    woInsideDTOUpdate.setCreateDate(dtoOld.getCreateDate());
    woInsideDTOUpdate.setStatus(dtoOld.getStatus());
    woInsideDTOUpdate.setWoSystemId(dtoOld.getWoSystemId());
    woInsideDTOUpdate.setCreatePersonId(dtoOld.getCreatePersonId());
    woInsideDTOUpdate.setCdId(dtoOld.getCdId());
    woInsideDTOUpdate.setFtId(dtoOld.getFtId());
    woInsideDTOUpdate.setPriorityId(dtoOld.getPriorityId());
    if (dtoOld.getFinishTime() != null) {
      woInsideDTOUpdate.setFinishTime(dtoOld.getFinishTime());
    }
    woInsideDTOUpdate.setResult(dtoOld.getResult());
    woInsideDTOUpdate.setStationId(dtoOld.getStationId());
    if (woInsideDTOUpdate.getLastUpdateTime() != null) {
      woInsideDTOUpdate.setLastUpdateTime(dtoOld.getLastUpdateTime());
    }
    woInsideDTOUpdate.setFileName(dtoOld.getFileName());
    woInsideDTOUpdate.setWoDescription(dtoOld.getComments());
    woInsideDTOUpdate.setWoWorklogContent(dtoOld.getWoWorklogContent());
    woInsideDTOUpdate.setReasonOverdueLV1Id(dtoOld.getReasonOverdueLV1Id());
    woInsideDTOUpdate.setReasonOverdueLV1Name(dtoOld.getReasonOverdueLV1Name());
    woInsideDTOUpdate.setReasonOverdueLV2Id(dtoOld.getReasonOverdueLV2Id());
    woInsideDTOUpdate.setReasonOverdueLV2Name(dtoOld.getReasonOverdueLV2Name());
    woInsideDTOUpdate.setCommentComplete(dtoOld.getCommentComplete());
    woInsideDTOUpdate.setCompletedTime(dtoOld.getCompletedTime());
    woInsideDTOUpdate.setLineCode(dtoOld.getLineCode());
    woInsideDTOUpdate.setWarehouseCode(dtoOld.getWarehouseCode());
    woInsideDTOUpdate.setConstructionCode(dtoOld.getConstructionCode());
    woInsideDTOUpdate.setIsValidWoTrouble(dtoOld.getIsValidWoTrouble());
    woInsideDTOUpdate.setSolutionGroupId(dtoOld.getSolutionGroupId());
    woInsideDTOUpdate.setSolutionGroupName(dtoOld.getSolutionGroupName());
    woInsideDTOUpdate.setSolution(dtoOld.getSolution());
    woInsideDTOUpdate.setReasonTroubleId(dtoOld.getReasonTroubleId());
    woInsideDTOUpdate.setReasonTroubleName(dtoOld.getReasonTroubleName());
    woInsideDTOUpdate.setClearTime(dtoOld.getClearTime());
    woInsideDTOUpdate.setCdAssignId(dtoOld.getCdAssignId());
    woInsideDTOUpdate.setScriptId(dtoOld.getScriptId());
    woInsideDTOUpdate.setScriptName(dtoOld.getScriptName());
    woInsideDTOUpdate.setPolesDistance(dtoOld.getPolesDistance());
    woInsideDTOUpdate.setDeltaCloseWo(dtoOld.getDeltaCloseWo());
    woInsideDTOUpdate.setConfirmNotCreateAlarm(dtoOld.getConfirmNotCreateAlarm());
    woInsideDTOUpdate.setLineCutCode(dtoOld.getLineCutCode());
    woInsideDTOUpdate.setCodeSnippetOff(dtoOld.getCodeSnippetOff());
    woInsideDTOUpdate.setClosuresReplace(dtoOld.getClosuresReplace());
    woInsideDTOUpdate.setPlanCode(dtoOld.getPlanCode());
    woInsideDTOUpdate.setNeedSupport(dtoOld.getNeedSupport());
    woInsideDTOUpdate.setReasonInterference(dtoOld.getReasonInterference());
  }

  public ResultInSideDto doExportFileTestService(List<WoInsideDTO> listWoAll) throws Exception {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    File fileExport;
    List<String> headersFileTest = new ArrayList<>();
    List<String> headersFileTestSheet = new ArrayList<>();
    List<List<String>> data = new ArrayList<>();
    List<ExcelSheetDTO> lstDataSheetAll = new ArrayList<>();
    if (listWoAll != null && !listWoAll.isEmpty()) {
      boolean isFirstHeaderFillInData = true;
      boolean isFirstHeaderFillInDataSheet = true;
      for (WoInsideDTO woInsideDTO : listWoAll) {
        List<Object[]> lstDataAll;
        List<Object[]> lstDataAllSheet;
        List<List<String>> dataSheet = new ArrayList<>();
        Date date;
        //lay thong tin cr
        String crName = null;
        String crNumber = null;
        StringBuilder nodeImpact = new StringBuilder();
        if (woInsideDTO.getWoSystem().trim().equals(WO_SYSTEM_CODE.CR)) {
          if (StringUtils.isNotNullOrEmpty(woInsideDTO.getWoSystemId())) {
            CrInsiteDTO crInsiteDTO = crServiceProxy.findCrByIdProxy(
                Long.valueOf(woInsideDTO.getWoSystemId()));
            if (crInsiteDTO != null) {
              crNumber = crInsiteDTO.getCrNumber();
              crName = crInsiteDTO.getTitle();
              CrImpactedNodesDTO crImpactedNodesDTO = new CrImpactedNodesDTO();
              crImpactedNodesDTO.setCrId(woInsideDTO.getWoSystemId());
              crImpactedNodesDTO
                  .setInsertTime(DateUtil.date2ddMMyyyyHHMMss(crInsiteDTO.getCreatedDate()));
              List<CrImpactedNodesDTO> listImpactNode = crServiceProxy
                  .getListCrImpactedNodesDTO(crImpactedNodesDTO);
              if (listImpactNode != null && !listImpactNode.isEmpty()) {
                for (CrImpactedNodesDTO node : listImpactNode) {
                  String nodeInfo = "";
                  if (StringUtils.isNotNullOrEmpty(nodeInfo)) {
                    nodeInfo = "";
                  }
                  if (StringUtils.isNotNullOrEmpty(node.getDeviceCode())) {
                    nodeInfo += node.getDeviceCode();
                    if (StringUtils.isNotNullOrEmpty(node.getIp())) {
                      nodeInfo += "/" + node.getIp();
                    }
                  } else {
                    if (StringUtils.isNotNullOrEmpty(node.getIp())) {
                      nodeInfo += node.getIp();
                    }
                  }
                  nodeImpact.append(nodeInfo);
                }
              }
            }
          } else {
            resultInSideDto.setKey(RESULT.ERROR);
            resultInSideDto
                .setMessage(woInsideDTO.getWoCode() + " " + I18n.getLanguage("wo.notFoundCR"));
            return resultInSideDto;
          }
        } else {
          resultInSideDto.setKey(RESULT.ERROR);
          resultInSideDto
              .setMessage(woInsideDTO.getWoCode() + " " + I18n.getLanguage("wo.woNotFromCR"));
          return resultInSideDto;
        }
        date = woInsideDTO.getCreateDate();
        byte[] bytes = FileUtils.getFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
            PassTranformer.decrypt(ftpPass),
            ftpFolder + "/" + FileUtils.createPathFtpByDate(date) + "/" + woInsideDTO
                .getFileName());
        String fullPathTemp = FileUtils.saveTempFile(woInsideDTO.getFileName(), bytes, tempFolder);
        File fileValidate = new File(fullPathTemp);
        if (fileValidate.exists()) {
          int noOfColumn = ExcelWriterUtils
              .findLastColumn(fullPathTemp, 1,
                  woInsideDTO.getFileName(),
                  0);
          lstDataAll = readExcelAddBlankRowXSSF(fileValidate,
              1,//sheet
              0,//begin row
              0,//from column
              noOfColumn,//to column
              20);
          if (lstDataAll != null) {
            for (int i = 0; i < lstDataAll.size(); i++) {
              List<String> woItemData = new ArrayList<>();
              if (isFirstHeaderFillInData) {
                for (int j = 0; j < lstDataAll.get(i).length; j++) {
                  headersFileTest
                      .add(lstDataAll.get(i)[j] != null ? lstDataAll.get(i)[j].toString() : "");
                }
                isFirstHeaderFillInData = false;
              } else if (!isFirstHeaderFillInData && i != 0) {
                //ma wo
                if (woInsideDTO.getWoCode() != null) {
                  woItemData.add(woInsideDTO.getWoCode());
                } else {
                  woItemData.add("");
                }
                //wo cha
                if (woInsideDTO.getParentName() != null) {
                  woItemData.add(woInsideDTO.getParentName());
                } else {
                  woItemData.add("");
                }
                //mo ta cong viec
                if (woInsideDTO.getWoContent() != null) {
                  woItemData.add(woInsideDTO.getWoContent());
                } else {
                  woItemData.add("");
                }
                //so cr
                if (crNumber != null) {
                  woItemData.add(crNumber);
                } else {
                  woItemData.add("");
                }
                //ten cr
                if (crName != null) {
                  woItemData.add(crName);
                } else {
                  woItemData.add("");
                }
                //lay thong tin impact node
                //node mang tac dong
                if (nodeImpact != null) {
                  woItemData.add(nodeImpact.toString());
                } else {
                  woItemData.add("");
                }
                for (int j = 0; j < lstDataAll.get(i).length; j++) {
                  woItemData
                      .add(lstDataAll.get(i)[j] != null ? lstDataAll.get(i)[j].toString() : "");
                }
                data.add(woItemData);
              }
            }
          }
          // sheet test case
          int noOfColumnTest = ExcelWriterUtils
              .findLastColumn(fullPathTemp, 0,
                  woInsideDTO.getFileName(),
                  0);
          lstDataAllSheet = readExcelAddBlankRowXSSF(fileValidate,
              0,//sheet
              0,//begin row
              0,//from column
              noOfColumnTest,//to column
              20);
          if (lstDataAllSheet != null) {
            for (int i = 0; i < lstDataAllSheet.size(); i++) {
              List<String> woItemDataSheet = new ArrayList<>();
              if (isFirstHeaderFillInDataSheet) {
                for (int j = 0; j < lstDataAllSheet.get(i).length; j++) {
                  headersFileTestSheet.add(
                      lstDataAllSheet.get(i)[j] != null ? lstDataAllSheet.get(i)[j].toString()
                          : "");
                }
                isFirstHeaderFillInDataSheet = false;
              } else if (!isFirstHeaderFillInDataSheet && i != 0) {
                for (int j = 0; j < lstDataAllSheet.get(i).length; j++) {
                  woItemDataSheet.add(
                      lstDataAllSheet.get(i)[j] != null ? lstDataAllSheet.get(i)[j].toString()
                          : "");
                }
                dataSheet.add(woItemDataSheet);
              }
            }
          }
          // thuc hien xuat du lieu ra tung sheet
          ExcelSheetDTO oo = new ExcelSheetDTO();
          oo.setData(dataSheet);
          oo.setSheetName(String.valueOf(woInsideDTO.getWoId()));
          oo.setTitles(headersFileTestSheet);
          lstDataSheetAll.add(oo);
        } else {
          resultInSideDto.setKey(RESULT.ERROR);
          resultInSideDto.setMessage(I18n.getLanguage("wo.fileNotFound"));
          return resultInSideDto;
        }
      }
      //doc file excel dinh kem
      //cho them vao file template
      fileExport = exportFileResult(headersFileTest, data, lstDataSheetAll);
      if (fileExport.exists()) {
        resultInSideDto.setKey(RESULT.SUCCESS);
        resultInSideDto.setFile(fileExport);
      } else {
        resultInSideDto.setKey(RESULT.ERROR);
        resultInSideDto.setMessage(I18n.getLanguage("wo.fileNotFound"));
      }
    }
    return resultInSideDto;
  }

  public File exportFileResult(List<String> headersFileTest, List<List<String>> data,
      List<ExcelSheetDTO> lstDataSheetAll) throws Exception {
    ExcelWriterUtils ewu = new ExcelWriterUtils();
    String templatePathOut = "templates" + File.separator + "TEMPLATE_EXPORT.xlsx";
    Resource resource = new ClassPathResource(templatePathOut);
    InputStream fileTemplate = resource.getInputStream();
    XSSFWorkbook workBook = new XSSFWorkbook(fileTemplate);

    Map<String, CellStyle> styles = CommonExport.createStyles(workBook);

    XSSFSheet sheetMain = workBook.getSheetAt(0);

    List<String> listHeader = new ArrayList<>();
    listHeader.add(I18n.getLanguage("wo.STT"));
    listHeader.add(I18n.getLanguage("wo.woCode"));
    listHeader.add(I18n.getLanguage("wo.parentCode"));
    listHeader.add(I18n.getLanguage("wo.woContent"));
    listHeader.add(I18n.getLanguage("wo.crNumber"));
    listHeader.add(I18n.getLanguage("wo.crTitle"));
    listHeader.add(I18n.getLanguage("wo.impactNode"));
    listHeader.addAll(headersFileTest);

    Font xSSFFont = workBook.createFont();
    xSSFFont.setFontName(HSSFFont.FONT_ARIAL);
    xSSFFont.setFontHeightInPoints((short) 20);
    xSSFFont.setBold(true);
    xSSFFont.setColor(IndexedColors.BLACK.index);

    CellStyle cellStyleTitle = workBook.createCellStyle();
    cellStyleTitle.setAlignment(HorizontalAlignment.CENTER);
    cellStyleTitle.setVerticalAlignment(VerticalAlignment.CENTER);
    cellStyleTitle.setFillForegroundColor(IndexedColors.WHITE.index);
    cellStyleTitle.setFont(xSSFFont);

    Font xSSFFontHeader = workBook.createFont();
    xSSFFontHeader.setFontName(HSSFFont.FONT_ARIAL);
    xSSFFontHeader.setFontHeightInPoints((short) 10);
    xSSFFontHeader.setColor(IndexedColors.BLUE.index);
    xSSFFontHeader.setBold(true);

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

    //Tao tieu de
    Row rowMainTitle = sheetMain.createRow(0);
    Cell mainCellTitle = rowMainTitle.createCell(0);
    mainCellTitle.setCellValue(I18n.getLanguage("wo.testServiceExportFile.title"));
//    mainCellTitle.setCellStyle(styles.get("title"));
    mainCellTitle.setCellStyle(cellStyleTitle);
    sheetMain.addMergedRegion(new CellRangeAddress(0, 0, 0, listHeader.size() - 1));

    //Tao date
    Date date = DateTimeUtils.convertDateOffset();
    Row rowDate = sheetMain.createRow(2);
    Cell cellDate = rowDate.createCell(6);
    cellDate.setCellStyle(styles.get("cellSubTitle"));
    cellDate
        .setCellValue(
            I18n.getLanguage("wo.exportTest.exportDate", DateUtil.date2ddMMyyyyHHMMss(date)));
    sheetMain.addMergedRegion(new CellRangeAddress(2, 2, 6, 8));

    //Tao header
    Row headerRow = sheetMain.createRow(4);
    headerRow.setHeight((short) 600);
    for (int i = 0; i < listHeader.size(); i++) {
      Cell headerCell = headerRow.createCell(i);
      XSSFRichTextString rt = new XSSFRichTextString(listHeader.get(i));
      headerCell.setCellValue(rt);
//      headerCell.setCellStyle(styles.get("header"));
      headerCell.setCellStyle(cellStyleHeader);
    }

    //Data
    if (data != null && !data.isEmpty()) {
      //fillData
      for (int i = 0; i < data.size(); i++) {
        Row row = sheetMain.createRow(i + 5);
        for (int j = -1; j < data.get(i).size(); j++) {
          Cell cell = row.createCell(j + 1);
          if (j == -1) {
            cell.setCellValue(i + 1);
            cell.setCellStyle(styles.get("cellCenter"));
          } else {
            cell.setCellValue(data.get(i).get(j) == null ? "" : data.get(i).get(j));
            cell.setCellStyle(styles.get("cellLeft"));
          }
        }
      }
    }

    // fill danh sach sheet
    if (lstDataSheetAll != null && lstDataSheetAll.size() > 0) {
      for (ExcelSheetDTO o : lstDataSheetAll) {
        XSSFSheet sheetCur = workBook.createSheet(o.getSheetName());
        Row headerRowCur = sheetCur.createRow(0);
        int sizeOfTitleSheet = o.getTitles().size();
        for (int i = 0; i < sizeOfTitleSheet; i++) {
          Cell cellHeader = headerRowCur.createCell(i);
          cellHeader.setCellValue(o.getTitles().get(i));
          cellHeader.setCellStyle(styles.get("header"));
        }
        //Data
        if (o.getData() != null && !o.getData().isEmpty()) {
          //fillData
          for (int i = 0; i < o.getData().size(); i++) {
            Row row = sheetCur.createRow(i + 1);
            for (int j = 0; j < o.getData().get(i).size(); j++) {
              Cell cell = row.createCell(j);
              cell.setCellValue(
                  o.getData().get(i).get(j) == null ? "" : o.getData().get(i).get(j));
              cell.setCellStyle(styles.get("cellLeft"));
            }
          }
        }
      }
    }

    //Set Width
    for (int i = 1; i <= listHeader.size(); i++) {
      sheetMain.autoSizeColumn(i);
      if (sheetMain.getColumnWidth(i) < 7000) {
        sheetMain.setColumnWidth(i, 7000);
      }
    }

    workBook.setSheetName(0, I18n.getLanguage("wo.testServiceExportFile.sheetName"));
    String fileResult = tempFolder + File.separator;
    String fileName = I18n.getLanguage("wo.testServiceExportFile.fileName") + ".xlsx";
    ewu.saveToFileExcel(workBook, fileResult, fileName);
    String resultPath = fileResult + fileName;
    File fileExport = new File(resultPath);
    return fileExport;
  }

  @Override
  public ResultInSideDto exportDataWoFromListCr(MultipartFile multipartFile, Date startTimeFrom,
      Date startTimeTo) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    UserToken userToken = ticketProvider.getUserToken();
    List<String> listCrNumber;
    List<WoInsideDTO> listWoAll;
    try {
      if (multipartFile.isEmpty()) {
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
        lstHeader = CommonImport.getDataFromExcelFile(fileImp, 0, 2,
            0, 1, 1000);
        if (lstHeader.size() == 0 || !validFileCRFormat(lstHeader)) {
          resultInSideDto.setKey(RESULT.FILE_INVALID_FORMAT);
          return resultInSideDto;
        }
        List<Object[]> lstData = CommonImport.getDataFromExcelFile(fileImp, 0, 3,
            0, 1, 1000);
        if (lstData.size() > maxRecord) {
          resultInSideDto.setKey(RESULT.DATA_OVER);
          resultInSideDto.setMessage(String.valueOf(maxRecord));
          return resultInSideDto;
        }
        listCrNumber = new ArrayList<>();
        if (!lstData.isEmpty()) {
          for (Object[] obj : lstData) {
            String crNumber = null;
            if (obj[1] != null) {
              crNumber = obj[1].toString().trim();
            }
            if (StringUtils.isNotNullOrEmpty(crNumber)) {
              listCrNumber.add(crNumber);
            }
          }
          if (!listCrNumber.isEmpty()) {
            listWoAll = new ArrayList<>();
            // chi lay loai cong viec test dich vu
            Map<String, String> mapConfigProperty = commonRepository.getConfigProperty();
            String woTypeIdTest = mapConfigProperty
                .get(Constants.CONFIG_PROPERTY.WO_TEST_SERVICE);
            for (int i = 0; i < listCrNumber.size(); i++) {
              String crNumber = listCrNumber.get(i);
              String crId = crNumber.substring(crNumber.lastIndexOf("_") + 1, crNumber.length());
              WoInsideDTO woTmp = new WoInsideDTO();
              woTmp.setWoSystemId(crId);
              woTmp.setStartTimeFrom(startTimeFrom);
              woTmp.setStartTimeTo(startTimeTo);
              woTmp.setUserId(userToken.getUserID());
              woTmp.setWoTypeId(Long.valueOf(woTypeIdTest));
              List<WoInsideDTO> listSearch = woRepository.getListDataSearchWoDTO(woTmp);
              if (listSearch != null) {
                listWoAll.addAll(listSearch);
              }
            }
            if (listWoAll == null || listWoAll.size() == 0) {
              resultInSideDto.setKey(RESULT.NODATA);
              resultInSideDto.setMessage(I18n.getLanguage("wo.importNotFindWo"));
            } else {
              File fileExport = exportDataWoFromCr(listWoAll);
              resultInSideDto.setFile(fileExport);
            }
          }
        } else {
          resultInSideDto.setKey(RESULT.NODATA);
          resultInSideDto.setMessage(Constants.FILE_NULL);
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

  public File exportDataWoFromCr(List<WoInsideDTO> listWoAll) throws Exception {
    String[] header = new String[]{"woCode", "woDescription", "cdName"};
    Date date = DateTimeUtils.convertDateOffset();
    return handleFileExport(listWoAll, header, DateUtil.date2ddMMyyyyHHMMss(date),
        "EXPORT_WO_FROM_CR");
  }

  private boolean validFileCRFormat(List<Object[]> lstHeader) {
    Object[] obj = lstHeader.get(0);
    int count = 0;
    for (Object data : obj) {
      if (data != null) {
        count += 1;
      }
    }
    if (count != 2) {
      return false;
    }
    if (obj == null) {
      return false;
    }
    if (obj[0] == null) {
      return false;
    }
    if (!(I18n.getLanguage("wo.STT"))
        .equalsIgnoreCase(obj[0].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("wo.crNumber"))
        .equalsIgnoreCase(obj[1].toString().trim())) {
      return false;
    }
    return true;
  }

  private boolean validFileWoTestFormat(List<Object[]> lstHeader) {
    Object[] obj = lstHeader.get(0);
    int count = 0;
    for (Object data : obj) {
      if (data != null) {
        count += 1;
      }
    }
    if (count != 2) {
      return false;
    }
    if (obj == null) {
      return false;
    }
    if (obj[0] == null) {
      return false;
    }
    if (!(I18n.getLanguage("wo.STT"))
        .equalsIgnoreCase(obj[0].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("wo.woCode"))
        .equalsIgnoreCase(obj[1].toString().trim())) {
      return false;
    }
    return true;
  }

  @Override
  public ResultDTO aprovePXK(Long woId, Long status, String reason, Long isDestroy) {
    ResultDTO result = new ResultDTO();
    try {
      WoInsideDTO wo = woRepository.findWoByIdNoOffset(woId);
      String nationCode = "";
      UsersEntity usersEntity = commonRepository.getUserByUserId(wo.getFtId());
      if (usersEntity != null && usersEntity.getUnitId() != null) {
        UnitDTO unitDTO = unitRepository.findUnitById(usersEntity.getUnitId());
        if (unitDTO != null && unitDTO.getLocationId() != null) {
          com.viettel.gnoc.commons.dto.CatLocationDTO catLocationDTO = catLocationRepository
              .getNationByLocationId(unitDTO.getLocationId());
          if (catLocationDTO != null && !StringUtils
              .isStringNullOrEmpty(catLocationDTO.getNationCode())) {
            nationCode = catLocationDTO.getNationCode();
          }
        }
      }
      Kttsbo resultKtts = kttsVsmartPort
          .updateWareExpNoteNation(woId, status, reason, isDestroy, nationCode);
      Long userId =
          (usersEntity != null && usersEntity.getUserId() != null) ? usersEntity.getUserId() : null;
      String userName =
          (usersEntity != null && usersEntity.getUsername() != null) ? usersEntity.getUsername()
              : null;
      if ("Success".equalsIgnoreCase(resultKtts.getStatus())) {
        if (!status.equals(1L)) { // ko xac nhan thi cap nhat wo ve hoan thanh
          WoHistoryInsideDTO woHistory = new WoHistoryInsideDTO();
          woHistory.setCdId(wo.getCdId());
          woHistory.setComments(reason);
          woHistory.setCreatePersonId(wo.getCreatePersonId());
          woHistory.setFileName(wo.getFileName());
          woHistory.setFtId(wo.getFtId());
          woHistory.setNewStatus(Long.valueOf(Constants.WO_STATUS.CLOSED_FT));
          woHistory.setOldStatus(wo.getStatus());
          woHistory.setUpdateTime(new Date());
          woHistory.setUserId(userId);
          woHistory.setUserName(userName);
          woHistory.setWoCode(wo.getWoCode());
          woHistory.setWoContent(wo.getWoContent());
          woHistory.setWoId(wo.getWoId());

          wo.setStatus(Long.valueOf(Constants.WO_STATUS.CLOSED_FT));
          wo.setCommentComplete(reason);
          wo.setCompletedTime(new Date());
          wo.setIsCompletedOnVsmart(1L);

          woRepository.updateWo(wo);
          woHistoryRepository.insertWoHistory(woHistory);

        }
      } else {
        result.setKey(RESULT.FAIL);
        result.setMessage(resultKtts.getErrorInfo());
        return result;
      }
      result.setKey(RESULT.SUCCESS);
    } catch (Exception e) {
      result.setKey(RESULT.FAIL);
      result.setMessage(e.getMessage());
      log.error(e.getMessage(), e);
    }
    return result;
  }


  @Override
  public ResultDTO createWoVsmart(WoDTO createWoDto) {
    ResultDTO result = new ResultDTO();
    try {
      String woTypeCode = createWoDto.getWoTypeId();
      Map<String, String> mapConfigProperty = commonRepository.getConfigProperty();
      // lay Id loai cong viec dua vao ma cong viec
      WoTypeInsideDTO dto = new WoTypeInsideDTO();
      dto.setWoTypeCode(woTypeCode);
      List<WoTypeInsideDTO> lst = woCategoryServiceProxy.getListWoTypeByLocaleNotLike(dto);
      if (lst != null && lst.size() > 0) {
        createWoDto.setWoTypeCode(woTypeCode);
        createWoDto.setWoTypeId(String.valueOf(lst.get(0).getWoTypeId()));

        // xac dinh nhan vien tao
        if (StringUtils.isStringNullOrEmpty(createWoDto.getCreatePersonId())
            && StringUtils.isNotNullOrEmpty(createWoDto.getCreatePersonName())) {
          List<UsersInsideDto> listCreateUser = userRepository
              .getListUserDTOByuserName(createWoDto.getCreatePersonName());
          if (listCreateUser != null && listCreateUser.size() > 0) {
            createWoDto.setCreatePersonId(String.valueOf(listCreateUser.get(0).getUserId()));
          }
        }
        // xac dinh FT dua tren FT name
        if (StringUtils.isStringNullOrEmpty(createWoDto.getFtId())
            && StringUtils.isNotNullOrEmpty(createWoDto.getFtName())) {
          createWoDto.setFtId(createWoDto.getFtName());
        }

        //bo sung file dinh kem va xac dinh CD dua vao ma don vi
        if (Constants.AP_PARAM.WO_TYPE_KBDV_QLCTKT.equalsIgnoreCase(woTypeCode)) {
          //xac dinh CD
          if (!StringUtils.isStringNullOrEmpty(createWoDto.getUnitCode())) {
            List<CatItemDTO> listCatItem = catItemRepository
                .getListItemByCategory(Constants.AP_PARAM.CATEGORY_OTHER_CODE,
                    createWoDto.getUnitCode());
            if (listCatItem != null && listCatItem.size() > 0) {
              WoCdGroupInsideDTO woCdGroup = woCdGroupRepository.
                  getWoCdGroupWoByCdGroupCode(listCatItem.get(0).getItemValue());
              if (woCdGroup != null) {
                createWoDto.setCdId(String.valueOf(woCdGroup.getWoGroupId()));
              } else {
                result.setKey(Constants.RESULT.FAIL);
                result.setMessage(I18n.getLanguage("notMapUnitCD"));
                return result;
              }
            } else {
              result.setKey(Constants.RESULT.FAIL);
              result.setMessage(I18n.getLanguage("notMapUnitCD"));
              return result;
            }
            // lay user tao
            List<CatItemDTO> lstUser = catItemRepository
                .getListItemByCategory(Constants.AP_PARAM.CATEGORY_OTHER_CODE,
                    Constants.AP_PARAM.USER_CREATE_KBDV_QLCTKT);
            if (lstUser != null && lstUser.size() > 0) // tao file dinh kem
            {
              createWoDto.setCreatePersonId(
                  (StringUtils.validString(lstUser.get(0).getItemValue()) && (DataUtil
                      .isNumber(lstUser.get(0).getItemValue()))) ? lstUser.get(0).getItemValue()
                      : null);
            } else {
              result.setKey(Constants.RESULT.FAIL);
              result.setMessage(I18n.getLanguage("notCfgUserCreate"));
              return result;
            }
          } else {
            result.setKey(Constants.RESULT.FAIL);
            result.setMessage(I18n.getLanguage("message.wo.unitCode.isNotNull"));
            return result;
          }
          String woId = woRepository.getSeqTableWo(WO_SEQ);
          createWoDto.setWoId(woId);
          ResultInSideDto resultInSideDto = insertWoCommon(createWoDto); // tao giao CD
          result.setKey(resultInSideDto.getKey());
          result.setMessage(resultInSideDto.getMessage());
          result.setId(resultInSideDto.getIdValue());
          return result;
        }

        //<editor-fold defaultstate="collapsed" desc="Loai WO bao hong ha tang">
        if (checkProperty(mapConfigProperty, createWoDto.getWoTypeId(),
            Constants.AP_PARAM.WO_TYPE_BHHT)
            || checkProperty(mapConfigProperty, createWoDto.getWoTypeId(),
            Constants.AP_PARAM.WO_TYPE_BHHTTC)) {

          // thuc hien giao viec ve CD pkt tinh dua vao ma don vi
          if (StringUtils.isNotNullOrEmpty(createWoDto.getUnitCode())) {
            Long cdLevel = 4L;
            UnitDTO unit = null;
            if ("1".equals(createWoDto.getCreateWoType())) { // muc huyen
              unit = woRepository.getUnitCodeMapNims(createWoDto.getUnitCode(), Constants.BUSINESS_UNIT_NAME.BHHT_HUYEN);
              if (createWoDto.getFtName() != null) {
                createWoDto.setFtId(createWoDto.getFtName());
              } else if (!StringUtils.isStringNullOrEmpty(createWoDto.getStationCode())) {
                // lay nhan vien quan ly tram
                List<HeaderForm> lstheader = new ArrayList<>();
                lstheader.add(new HeaderForm("nationCode", createWoDto.getNationCode()));
                lstheader.add(new HeaderForm("locale ", I18n.getLocale()));
                cdPort.setLstHeader(lstheader);
                List<Staff> lstStaff = cdPort.getListStaff(createWoDto.getStationCode(), null, null);

                if (lstStaff != null && !lstStaff.isEmpty()) {
                  for (Staff staff : lstStaff) {
                    if (!StringUtils.isStringNullOrEmpty(staff.getStaffType())
                        && staff.getStaffType() == 1l) {
                      UsersDTO usersDTO = userRepository.getUserDTOByUsernameLower(staff.getUserName());
                      if (usersDTO == null || usersDTO.getUserId() == null) {
                        continue;
                      }
                      createWoDto.setFtId(usersDTO.getUserId());
                      break;
                    }
                  }
                }
              }

            } else if ("2".equals(createWoDto.getCreateWoType())) { // tinh trong OS
              cdLevel = 3L;
              woRepository.getUnitCodeMapNims(createWoDto.getUnitCode(), Constants.BUSINESS_UNIT_NAME.BHHT_TINH_INSOURCE);
            } else if ("3".equals(createWoDto.getCreateWoType())) { // Tinh ngoai OS
              cdLevel = 3L;
              unit = woRepository.getUnitCodeMapNims(createWoDto.getUnitCode(), Constants.BUSINESS_UNIT_NAME.BHHT_TINH_OUTSOURCE);
            } else { // CD muc khu vuc
              cdLevel = 2L;
              unit = woRepository.getUnitCodeMapNims(createWoDto.getUnitCode(), Constants.BUSINESS_UNIT_NAME.BHHT_KV);
            }

            if (unit != null) {
              Long cd = woRepository
                  .getCdByUnitId(unit.getUnitId(), cdLevel, Long.valueOf(createWoDto.getWoTypeId()));
              if (cd != null) {
                createWoDto.setCdId(String.valueOf(cd));
              } else {
                throw new RuntimeException(I18n.getLanguage("wo.cdGroupNotExist"));
              }
            } else {
              throw new RuntimeException(I18n.getLanguage("wo.unitisNotExists"));
            }
          } else {
            throw new RuntimeException(I18n.getLanguage("message.wo.unitCode.isNotNull"));
          }
        }
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Loai Kiem tra nha tram">
        if (checkProperty(mapConfigProperty, createWoDto.getWoTypeId(),
            Constants.AP_PARAM.WO_TYPE_KTNT)
            && StringUtils.isNotNullOrEmpty(createWoDto.getCdGroupType())) {

          // thuc hien giao viec ve CD pkt tinh dua vao ma don vi
          if (StringUtils.isNotNullOrEmpty(createWoDto.getUnitCode())) {

            UnitDTO unit = woRepository
                .getUnitCodeMapNims(createWoDto.getUnitCode(), Constants.BUSINESS_UNIT_NAME.KTNT);
            if (unit != null) {
              Long cd = woRepository
                  .getCdByUnitId(unit.getUnitId(), Long.valueOf(createWoDto.getCdGroupType()),
                      Long.valueOf(createWoDto.getWoTypeId()));
              if (cd != null) {
                createWoDto.setCdId(String.valueOf(cd));
              } else {
                throw new RuntimeException(I18n.getLanguage("wo.cdGroupNotExist"));
              }
            } else {
              throw new RuntimeException(I18n.getLanguage("wo.unitisNotExists"));
            }
          } else {
            throw new RuntimeException(I18n.getLanguage("message.wo.unitCode.isNotNull"));
          }
        }
        //</editor-fold>

        ResultInSideDto resultInSideDto = createWoCommon(createWoDto);
        result.setKey(resultInSideDto.getKey());
        result.setMessage(resultInSideDto.getMessage());
        result.setId(resultInSideDto.getIdValue());
        return result;
      } else {
        result.setKey(Constants.RESULT.FAIL);
        result.setMessage(I18n.getLanguage("wo.woTypeDoesNotExixts"));
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      result.setKey(Constants.RESULT.FAIL);
      result.setMessage(e.getMessage());
    }
    return result;
  }

  @Override
  public String checkProblemSignalForAccount(String subscriptionAccount, String woId,
      String sysId) {
    URL url;
    String rs = "";
    try {
      url = new URL(qosUrl);
      QName qname = new QName(qosTargetNamespace, qosName);
      QoSService_Service serv = new QoSService_Service(url, qname);
      QoSService ws = serv.getQoSServicePort();
      rs = ws.checkProblemSignalForAccount(subscriptionAccount, woId, sysId);
    } catch (MalformedURLException ex) {
      log.error(ex.getMessage(), ex);
    }
    return rs;
  }

  @Override
  public ResultDTO cancelReqBccs(String woCode, String content) {
    try {
      WoInsideDTO wo = woRepository.getWoByWoSystemCode(woCode);
      if (wo != null) {
        UsersInsideDto us = commonRepository.getUserByUserId(wo.getCreatePersonId()).toDTO();
        Date date = new Date();
        if (wo.getFtId() == null) { // chua giao viec cho FT => huy luong ghi lich su huy luong
          wo.setResult(2L);

          updateWoAndHistory(wo, us, I18n.getLanguage("wo.bccs.cacelReg"),
              Constants.WO_STATUS.REJECT, date);
        } else { // cap nhat lai noi dung va trang thai ghi them content luu lich su
//          wo.setStatus(Long.valueOf(Constants.WO_STATUS.DISPATCH));
          wo.setFinishTime(null);
          wo.setResult(null);
          wo.setWoContent(I18n.getLanguage("wo.new.content") + ":" + content + "\r\n\r\n" + I18n
              .getLanguage("wo.old.content") + "(" + wo.getWoContent() + ")");

          updateWoAndHistory(wo, us, I18n.getLanguage("wo.bccs.cacelReg"),
              Constants.WO_STATUS.DISPATCH, date);
        }
      } else {
        return new ResultDTO("FAIL", "FAIL", "FAIL");
      }
      return new ResultDTO("SUCCESS", "SUCCESS", "SUCCESS");
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return new ResultDTO("FAIL", "FAIL", e.getMessage());
    }
  }

  @Override
  public ResultDTO updateFileForWo(WoDTO woDTO) {
    ResultDTO resultDTO = new ResultDTO();
    try {
      WoInsideDTO wo = woRepository.getWoByWoCode(woDTO.getWoCode());
      if (woDTO.getListFileName() != null && woDTO.getListFileName().size() > 0
          && woDTO.getFileArr() != null && woDTO.getFileArr().size() > 0) {
        if (woDTO.getListFileName().size() != woDTO.getFileArr().size()) {
          throw new Exception(I18n.getLanguage("wo.numberFileNotMap"));
        }
        UsersEntity usersEntity = userRepository.getUserByUserId(wo.getCreatePersonId());
        UnitDTO unitToken = unitRepository.findUnitById(usersEntity.getUnitId());
        List<GnocFileDto> gnocFileDtos = new ArrayList<>();
        List<String> lstFileName = new ArrayList<>();
        for (int i = 0; i < woDTO.getListFileName().size(); i++) {
          if (extension != null) {
            String[] extendArr = extension.split(",");
            Boolean check = false;
            for (String e : extendArr) {
              if (woDTO.getListFileName().get(i).toLowerCase().endsWith(e.toLowerCase())) {
                check = true;
                break;
              }
            }
            if (!check) {
              throw new Exception(I18n.getLanguage("wo.fileImportInvalidExten"));
            }
          }
          String fullPath = FileUtils
              .saveFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
                  PassTranformer.decrypt(ftpPass), ftpFolder, woDTO.getListFileName().get(i),
                  woDTO.getFileArr().get(i),
                  FileUtils.createDateOfFileName(wo.getCreateDate()));
          //Start save file old
          lstFileName.add(FileUtils.getFileName(fullPath));
          //End save file old
          GnocFileDto gnocFileDto = new GnocFileDto();
          gnocFileDto.setPath(fullPath);
          gnocFileDto.setFileName(FileUtils.getFileName(fullPath));
          gnocFileDto.setCreateUnitId(usersEntity.getUnitId());
          gnocFileDto.setCreateUnitName(unitToken.getUnitName());
          gnocFileDto.setCreateUserId(usersEntity.getUserId());
          gnocFileDto.setCreateUserName(usersEntity.getUsername());
          gnocFileDto.setCreateTime(wo.getCreateDate());
          gnocFileDto.setMappingId(wo.getWoId());
          gnocFileDtos.add(gnocFileDto);
        }
        gnocFileRepository
            .saveListGnocFileNotDeleteAll(GNOC_FILE_BUSSINESS.WO, wo.getWoId(), gnocFileDtos);
        String fileNameNew = lstFileName.size() > 0 ? String.join(",", lstFileName) : "";
        boolean checkFileName = StringUtils.isNotNullOrEmpty(wo.getFileName()) && StringUtils
            .isNotNullOrEmpty(fileNameNew);
        wo.setFileName(
            (StringUtils.isStringNullOrEmpty(wo.getFileName()) ? "" : wo.getFileName())
                + (checkFileName ? "," : "") + (StringUtils.isStringNullOrEmpty(fileNameNew) ? ""
                : fileNameNew));
        woDTO.setSyncStatus(null);
        woRepository.updateWo(wo);
      }
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      resultDTO.setId("1");
      resultDTO.setKey(RESULT.FAIL);
      resultDTO.setMessage(RESULT.FAIL);
      return resultDTO;
    }
    resultDTO.setId("1");
    resultDTO.setKey(RESULT.SUCCESS);
    resultDTO.setMessage(RESULT.SUCCESS);
    return resultDTO;
  }

  @Override
  public ResultDTO approveWo(VsmartUpdateForm vsmartUpdateForm, String username, String woId,
      String comment, String action,
      String ftName, String sessionId, String ipPortParentNode) throws Exception {
    ResultDTO resultDTO = new ResultDTO();
    ResultInSideDto res = approveWoCommon(vsmartUpdateForm, username, Long.valueOf(woId), comment,
        action,
        ftName);
    if (RESULT.SUCCESS.equals(res.getKey())) {
      resultDTO.setId("1");
      resultDTO.setKey(RESULT.SUCCESS);
      resultDTO.setMessage("OK");
    } else {
      resultDTO.setKey(RESULT.FAIL);
      resultDTO
          .setMessage(StringUtils.isNotNullOrEmpty(res.getMessage()) ? res.getMessage() : null);
    }
    return resultDTO;
  }

  @Override
  public ResultDTO insertWoWorklog(WoWorklogDTO woWorklogDTO) {
    ResultDTO resultDTO = new ResultDTO();
    WoWorklogInsideDTO woWorklogInsideDTO = woWorklogDTO.toModelInSide();
    ResultInSideDto res = woWorklogRepository.insertWoWorklog(woWorklogInsideDTO);
    resultDTO.setId(res.getId() != null ? String.valueOf(res.getId()) : null);
    resultDTO.setKey(StringUtils.isNotNullOrEmpty(res.getKey()) ? res.getKey() : null);
    return resultDTO;
  }

  @Override
  public ResultDTO acceptWo(String username, String woId, String comment) throws Exception {
    ResultDTO resultDTO = new ResultDTO();
    if (StringUtils.isStringNullOrEmpty(woId)) {
      resultDTO.setKey(RESULT.FAIL);
      resultDTO.setMessage(I18n.getLanguage("wo.WoIdIsNotNull"));
    } else {
      ResultInSideDto res = acceptWoCommon(username, Long.valueOf(woId), comment, "CD");
      if ("OK".equals(res.getKey())) {
        resultDTO.setKey("OK");
        resultDTO.setQuantitySucc(res.getQuantitySucc());
      } else {
        resultDTO.setKey(RESULT.FAIL);
        resultDTO.setMessage(res.getMessage());
      }
    }
    return resultDTO;
  }

  @Override
  public ResultDTO createWo(WoDTO createWoDto) throws Exception {
    ResultDTO resultDTO = new ResultDTO();
    ResultInSideDto res = createWoCommon(createWoDto);
    if (RESULT.SUCCESS.equals(res.getKey())) {
      resultDTO.setId(res.getIdValue());
      resultDTO.setKey(res.getKey());
      resultDTO.setMessage(res.getMessage());
    } else {
      resultDTO.setKey(RESULT.FAIL);
      resultDTO.setMessage(res.getMessage());
    }
    resultDTO.setQuantitySucc(res.getQuantitySucc());
    return resultDTO;
  }

  @Override
  public List<UsersDTO> getListCd(Long cdGroupId) {
    List<UsersDTO> lstReturn = new ArrayList<>();
    try {
      List<UsersInsideDto> lst = woRepository.getUserOfCD(cdGroupId);
      if (lst != null && lst.size() > 0) {
        for (UsersInsideDto u : lst) {
          lstReturn.add(u.toOutSideDto());
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return null;
    }
    return lstReturn;
  }

  @Override
  public ResultDTO updateStatus(String username, String woId, String status, String comment)
      throws Exception {
    ResultInSideDto resultInSideDto = updateStatusCommon(null, username, woId, status, comment,
        null, null, null, null, null, null, null, null, null, null, null, null, null);
    return resultInSideDto.toResultDTO();
  }

  @Override
  public List<ResultDTO> getWOSummaryInfobyUser(String userId) {
    return getWOSummaryInfobyUserCommon(userId, Constants.WO_TYPE_SEARCH.IS_FT, null, null, null);
  }

  @Override
  public ResultDTO createWoForOtherSystem(WoDTO woDTO) throws Exception {
//    try {
//      JAXBContext jaxbContext;
//      jaxbContext = JAXBContext.newInstance(WoDTO.class);
//      Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
//      StringWriter sw = new StringWriter();
//      jaxbMarshaller.marshal(woDTO, sw);
//      String xmlString = sw.toString();
//      System.out.println("createWoDto: " + xmlString);
//    } catch (Exception ex) {
//      log.error(ex.getMessage(), ex);
//    }
    ResultDTO result = new ResultDTO();
    try {
      Map<String, String> mapConfigProperty = commonRepository.getConfigProperty();
      SimpleDateFormat dfm = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
      List<HeaderForm> lstheader = new ArrayList<>();
      lstheader.add(new HeaderForm("nationCode", woDTO.getNationCode()));
      cdPort.setLstHeader(lstheader);
      // <editor-fold defaultstate="collapsed" desc="Khoi tao du lieu">
      // nguoi tao
      if (StringUtils.isStringNullOrEmpty(woDTO.getCreatePersonName())) {
        throw new Exception(I18n.getLanguage("wo.createPesonNameIsNotNull"));
      } else {
        UsersEntity uc = userRepository.getUserByUserName(woDTO.getCreatePersonName());
        if (uc == null) {
          throw new Exception(I18n.getLanguage("wo.createPesonNameIsNotExists"));
        } else {
          woDTO.setCreatePersonId(String.valueOf(uc.getUserId()));
        }
      }
      // muc do uu tien
      if (StringUtils.isStringNullOrEmpty(woDTO.getPriorityCode())) {
        throw new Exception(I18n.getLanguage("wo.priorityCodeIsNotNull"));
      } else {
        String priority = commonRepository.getConfigPropertyValue(woDTO.getPriorityCode());
        if (StringUtils.isStringNullOrEmpty(priority)) {
          throw new Exception(I18n.getLanguage("wo.priorityCodeIsNotExists"));
        } else {
          woDTO.setPriorityId(priority);
        }
      }
      // loai cong viec
      if (StringUtils.isStringNullOrEmpty(woDTO.getWoTypeCode())) {
        throw new Exception(I18n.getLanguage("wo.WoTypeCodeIsNotNull"));
      } else {
        WoTypeInsideDTO dto = new WoTypeInsideDTO();
        dto.setWoTypeCode(woDTO.getWoTypeCode());
        List<WoTypeInsideDTO> lst = woCategoryServiceProxy.getListWoTypeByLocaleNotLike(dto);
        if (lst == null || lst.size() == 0) {
          throw new Exception(I18n.getLanguage("wo.WoTypeCodeIsNotExists"));
        } else {
          woDTO.setWoTypeId(String.valueOf(lst.get(0).getWoTypeId()));
        }
      }
      // ko nhap FT ma nhap ma ket cuoi
      if (StringUtils.isNotNullOrEmpty(woDTO.getConnectorCode())) {
        String us = getUserFromConnectorCode(woDTO.getConnectorCode());
        if (us != null) {
          woDTO.setFtName(us);
        }
      }
      //ft thuc hien
      if (StringUtils.isNotNullOrEmpty(woDTO.getFtName())) {
        UsersEntity ft = userRepository.getUserByUserName(woDTO.getFtName());
        if (ft == null) {
          throw new Exception(I18n.getLanguage("wo.ftIsNotExists"));
        } else {
          woDTO.setFtId(String.valueOf(ft.getUserId()));
          if (StringUtils.isStringNullOrEmpty(woDTO.getCdId())) {
            Long cd = woRepository
                .getCdByUnitId(ft.getUnitId(), 4L, Long.valueOf(woDTO.getWoTypeId()));
            if (cd != null) {
              woDTO.setCdId(String.valueOf(cd));
            }
          }
        }
      }
      // neu co truyen ma thiet bi thi suy ra ma tram
      if (StringUtils.isNotNullOrEmpty(woDTO.getDeviceCode())) {
        String stationCode = woRepository.getStationFollowNode(woDTO.getDeviceCode(), null);
        woDTO.setStationCode(stationCode);
      }
      // import co giao cho FT
      if ("1".equals(woDTO.getIsSendFT()) && StringUtils.isNotNullOrEmpty(woDTO.getStationCode())) {
        String usedMajorCode = commonRepository
            .getConfigPropertyValue(Constants.AP_PARAM.USED_MAJOR_CODE);
        WoTypeInsideDTO woType = woTypeBusiness.findWoTypeById(
            Long.valueOf(woDTO.getWoTypeId()));
        List<Staff> lstStaff = cdPort.getListStaff(woDTO.getStationCode(),
            woType != null ? woType.getUserTypeCode() : null, usedMajorCode);
        if (lstStaff != null && lstStaff.size() > 0) {
          for (Staff staff : lstStaff) {
            if (staff.getStaffType() != null && staff.getStaffType() == 1l) {
              Long ftId = userRepository.getUserId(staff.getUserName());
              woDTO.setFtId(String.valueOf(ftId));
              break;
            }
          }
          if (StringUtils.isStringNullOrEmpty(woDTO.getFtId())) {
            throw new Exception(I18n.getLanguage("wo.ftIsNotExists")); // khong ton tai nhan vien FT
          }
        }
//        else {
//          throw new Exception(I18n.getLanguage("wo.ftIsNotExists")); // khong ton tai nhan vien FT
//        }
      }

      if (StringUtils.isStringNullOrEmpty(woDTO.getEndTime())) {
        // thoi gian endTime dua vao loai cong viec
        WoTypeTimeDTO woTypeTimeDTOCondition = new WoTypeTimeDTO();
        woTypeTimeDTOCondition.setWoTypeId(Long.valueOf(woDTO.getWoTypeId()));
        List<WoTypeTimeDTO> lstTime = woTypeBusiness
            .getListWoTypeTimeDTO(woTypeTimeDTOCondition);
        if (lstTime != null && !lstTime.isEmpty()) {
          WoTypeTimeDTO woTypeTimeDTO = lstTime.get(0);
          if (woTypeTimeDTO.getDuration() != null && StringUtils
              .isNotNullOrEmpty(woDTO.getStartTime())) {
            Date startDate = dfm.parse(woDTO.getStartTime());
            Double duration = woTypeTimeDTO.getDuration() * 24 * 60 * 60 * 1000;
            Date enDate = new Date(startDate.getTime() + duration.longValue());
            woDTO.setEndTime(dfm.format(enDate));
          }
        }
      }
      // </editor-fold>
      if (checkProperty(mapConfigProperty, woDTO.getWoTypeId(),
          Constants.AP_PARAM.WO_TYPE_NPMS_XLTTUT)) {
        // lay nhom deu phoi dua vao ma nhom dieu phoi
        if (StringUtils.isStringNullOrEmpty(woDTO.getCdGroupCode())) {
          throw new Exception(I18n.getLanguage("wo.CdGroupCode.isNotNull"));
        } else {
          List<WoCdGroupInsideDTO> lstCd = woCdGroupRepository
              .getListCdGroupByCodeNotLike(woDTO.getCdGroupCode());
          if (lstCd == null || lstCd.size() == 0) {
            throw new Exception(I18n.getLanguage("wo.cdGroupCode.isNotExists"));
          } else {
            woDTO.setCdId(String.valueOf(lstCd.get(0).getWoGroupId()));
          }
        }
        // lay ft la truong phong ktht chi nhanh
        UnitDTO unit = unitRepository.getUnitByUnitCode(woDTO.getUnitCode());
        if (unit != null) {
          Long ftId = userRepository.getLstUserOfUnitByRole(unit.getUnitId(),
              mapConfigProperty.get(Constants.AP_PARAM.USER_ROLE_TP));
          if (ftId == null) {
            throw new Exception(I18n.getLanguage("wo.notFindFtTpOfUnit"));
          } else {
            woDTO.setFtId(String.valueOf(ftId));
          }
        } else {
          throw new Exception(I18n.getLanguage("wo.unitisNotExists"));
        }
      }
      if (checkProperty(mapConfigProperty, woDTO.getWoTypeId(),
          Constants.AP_PARAM.WO_TYPE_SCTBCD_ACCESS)) {
        // thuc hien giao viec ve CD pkt tinh dua vao ma don vi
        if (StringUtils.isNotNullOrEmpty(woDTO.getUnitCode())) {
          UnitDTO unit = woRepository.getUnitCodeMapNims(woDTO.getUnitCode(),
              Constants.BUSINESS_UNIT_NAME.SCTBCD_ACCESS);
          if (unit != null) {
            Long cd = woRepository.getCdByUnitId(unit.getUnitId(), 3L);
            if (cd != null) {
              woDTO.setCdId(String.valueOf(cd));
            } else {
              throw new Exception(I18n.getLanguage("wo.cdGroup.not.exist"));
            }
          } else {
            throw new Exception(I18n.getLanguage("wo.unitisNotExists"));
          }
        } else {
          throw new Exception(I18n.getLanguage("message.wo.unitCode.isNotNull"));
        }
      }

      //<editor-fold defaultstate="collapsed" desc="Loai WO bao hong ha tang">
      if (checkProperty(mapConfigProperty, woDTO.getWoTypeId(), Constants.AP_PARAM.WO_TYPE_BHHT)
          || checkProperty(mapConfigProperty, woDTO.getWoTypeId(),
          Constants.AP_PARAM.WO_TYPE_BHHTTC)) {

        // thuc hien giao viec ve CD pkt tinh dua vao ma don vi
        if (StringUtils.isNotNullOrEmpty(woDTO.getUnitCode())) {
          Long cdLevel = 4L;
          UnitDTO unit = null;
          if ("1".equals(woDTO.getCreateWoType())) { // muc huyen
            unit = woRepository
                .getUnitCodeMapNims(woDTO.getUnitCode(), BUSINESS_UNIT_NAME.BHHT_HUYEN);
            List<Staff> lstStaff = cdPort.getListStaff(woDTO.getStationCode(), null, null);

            if (lstStaff != null && !lstStaff.isEmpty()) {
              for (Staff staff : lstStaff) {
                if (!StringUtils.isStringNullOrEmpty(staff.getStaffType())
                    && staff.getStaffType() == 1l) {
                  UsersDTO usersDTO = userRepository.getUserDTOByUsernameLower(staff.getUserName());
                  if (usersDTO == null || usersDTO.getUserId() == null) {
                    continue;
                  }
                  woDTO.setFtId(usersDTO.getUserId());
                  break;
                }
              }
            }
          } else if ("2".equals(woDTO.getCreateWoType())) { // tinh trong OS
            cdLevel = 3L;
            unit = woRepository.getUnitCodeMapNims(woDTO.getUnitCode(), Constants.BUSINESS_UNIT_NAME.BHHT_TINH_INSOURCE);
          } else if ("3".equals(woDTO.getCreateWoType())) { // Tinh ngoai OS
            cdLevel = 3L;
            unit = woRepository.getUnitCodeMapNims(woDTO.getUnitCode(), Constants.BUSINESS_UNIT_NAME.BHHT_TINH_OUTSOURCE);
          } else { // CD muc khu vuc
            cdLevel = 2L;
            unit = woRepository.getUnitCodeMapNims(woDTO.getUnitCode(), Constants.BUSINESS_UNIT_NAME.BHHT_KV);
          }
          if (unit != null) {
            Long cd = woRepository
                .getCdByUnitId(unit.getUnitId(), 3L, Long.valueOf(woDTO.getWoTypeId()));
            if (cd != null) {
              woDTO.setCdId(String.valueOf(cd));
            } else {
              throw new Exception(I18n.getLanguage("wo.cdGroup.not.exist"));
            }
          } else {
            throw new Exception(I18n.getLanguage("wo.unitisNotExists"));
          }
        } else {
          throw new Exception(I18n.getLanguage("message.wo.unitCode.isNotNull"));
        }
      }
      //</editor-fold>

      if (checkProperty(mapConfigProperty, woDTO.getWoTypeId(),
          Constants.AP_PARAM.WO_TYPE_CDBR_FOR_STL)) {
        // thuc hien giao viec ve CD huyen dua vao ma tram
        if (StringUtils.isNotNullOrEmpty(woDTO.getStationCode())) {
          WoCdGroupInsideDTO woCdGroup = woCdGroupBusiness
              .getCdByStationCodeNation(woDTO.getStationCode(), woDTO.getWoTypeId(), "4", null,
                  Constants.NATION_CODES.STL);
          if (woCdGroup != null) {
            woDTO.setCdId(String.valueOf(woCdGroup.getWoGroupId()));
          } else {
            throw new Exception(I18n.getLanguage("wo.cdGroup.not.exist"));
          }
        }
      }
      // neu ko co nhom CD va co truyen ma tram
      if (StringUtils.isStringNullOrEmpty(woDTO.getCdId()) && StringUtils
          .isNotNullOrEmpty(woDTO.getStationCode())) {
        WoCdGroupInsideDTO woCdGroup = woCdGroupBusiness
            .getCdByStationCode(woDTO.getStationCode(), woDTO.getWoTypeId(), "4", null);
        if (woCdGroup != null) {
          woDTO.setCdId(String.valueOf(woCdGroup.getWoGroupId()));
        }
      }
      // neu ko tim dc CD va co truyen ma CD
      if (StringUtils.isStringNullOrEmpty(woDTO.getCdId()) && StringUtils
          .isNotNullOrEmpty(woDTO.getCdGroupCode())) {
        List<WoCdGroupInsideDTO> lstCd = woCdGroupRepository
            .getListCdGroupByCodeNotLike(woDTO.getCdGroupCode());
        if (lstCd != null && lstCd.size() > 0) {
          woDTO.setCdId(String.valueOf(lstCd.get(0).getWoGroupId()));
        }
      }
      // neu ko tim dc CD va co truyen account khach hang
      if (StringUtils.isStringNullOrEmpty(woDTO.getCdId()) && StringUtils
          .isNotNullOrEmpty(woDTO.getAccountIsdn())) {
        WoDTO woTmp = new WoDTO();
        woTmp = (WoDTO) woRepository.updateObjectData(woDTO, woTmp);
        woTmp = getFtForSPM(woDTO.getAccountIsdn(), woDTO.getLocationCode(), woDTO.getNationCode(),
            StringUtils.isNotNullOrEmpty(woDTO.getWoTypeId()) ? Long.valueOf(woDTO.getWoTypeId())
                : null);
        woDTO.setCdId(woTmp.getCdId());
        if ("1".equals(woDTO.getIsSendFT())) {
          woDTO.setFtId(woTmp.getFtId());
        }
      }
      // neu giao CD thi xoa FT
      if ("0".equals(woDTO.getIsSendFT())) {
        woDTO.setFtId(null);
      }

      if (StringUtils.isStringNullOrEmpty(woDTO.getCdId())) {
        throw new Exception(I18n.getLanguage("wo.cdGroup.not.exist"));
      }

      // kiem tra nhom dieu phoi co dc dieu phoi loai cong viec
      WoTypeGroupDTO typeGroupTmp = new WoTypeGroupDTO();
      typeGroupTmp.setWoGroupId(Long.valueOf(woDTO.getCdId()));
      typeGroupTmp.setWoTypeId(Long.valueOf(woDTO.getWoTypeId()));
      List<WoTypeGroupDTO> lstTypeGroup = woTypeGroupRepository.getListWoTypeGroupDTO(typeGroupTmp);
      if (lstTypeGroup == null || lstTypeGroup.size() == 0) {
        throw new Exception(I18n.getLanguage("woGroupType.isNotValid"));
      }

      String woId = woRepository.getSeqTableWo(WO_SEQ);
      woDTO.setWoId(woId);
      ResultInSideDto res = insertWoCommon(woDTO);
      result.setId(StringUtils.isNotNullOrEmpty(res.getIdValue()) ? res.getIdValue() : null);
      result.setKey(res.getKey());
      result.setMessage(StringUtils.isNotNullOrEmpty(res.getMessage()) ? res.getMessage() : null);
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      throw ex;
    }
    return result;
  }

  public String getUserFromConnectorCode(String connectorCode) {
    try {
      ResultService res = qltPort.getListStaffInfraConnector(connectorCode, null);
      if (res != null && res.getLstUser() != null && res.getLstUser().size() > 0) {
        com.viettel.webservice.qlctkt.bccs.SysUsersBO u = res.getLstUser().get(0);
        return u.getUsername();
      }
    } catch (Exception e) {
    }
    return null;
  }

  @Override
  public ResultDTO callIPCCWithName(String woId, Long userId, String userCall,
      String fileAudioName) {
    ResultDTO result = new ResultDTO();
    try {
      //lay thong tin nhan vien can goi dien
      WoInsideDTO wo = woRepository.findWoByIdNoOffset(Long.valueOf(woId));
      UsersEntity us = userRepository.getUserByUserId(userId);
      Date startCall = new Date();
      if (wo != null) {
        String transactionId = wo.getWoCode() + "_" + DateUtil.dateTime2StringNoSlash(startCall);
        if (us != null) {
          LogCallIpccDTO dto = new LogCallIpccDTO();
          dto.setPhone(us.getMobile());
          dto.setRecordFileCode(fileAudioName);
          dto.setStartCallTime(
              DateTimeUtils.convertDateToString(startCall, Constants.ddMMyyyyHHmmss));
          dto.setUserName(us.getUsername());
          dto.setTransactionId(transactionId);
          dto.setWoId(woId);
          dto.setUserCall(userCall);
          logCallIpccRepository.insertLogCallIpcc(dto);
          // goi sang IPCC
          //lay url ipcc dua vao don vi
          String url = null;
          boolean checkCall = true;
          String nation = Constants.IPCC_CODE.VNM;
          UnitDTO un = unitRepository.findUnitById(us.getUnitId());
          if (un != null) {
            IpccServiceDTO s = new IpccServiceDTO();
            if (un.getIpccServiceId() != null && un.getIpccServiceId().equals(-1L)) {
              checkCall = false;
            } else if (un.getIpccServiceId() != null) {
              s.setIpccServiceId(un.getIpccServiceId());
            } else {
              s.setIsDefault(1L);
            }
            List<IpccServiceDTO> lst = smsGatewayRepository.getListIpccServiceDTO(s);
            if (lst != null && lst.size() > 0) {
              url = lst.get(0).getUrl();
            }
          }
          if (StringUtils.isNotNullOrEmpty(url) && checkCall) {
            AutoCallOutInput input = new AutoCallOutInput();
            input.setPhoneNumber(us.getMobile());
            input.setRecordFileCode(fileAudioName);
            input.setResponseUrl(URLFORIPCC);
            input.setTransactionId(transactionId);

            NomalOutput outPut;
            if (Constants.IPCC_CODE.VTP.equals(nation)) {
              outPut = ipccvtpPort.autoCallout(url, input);
            } else if (Constants.IPCC_CODE.VTZ.equals(nation)) {
              outPut = ipccvtzPort.autoCallout(url, input);
            } else if (Constants.IPCC_CODE.STL.equals(nation)) {
              Input inputVTP = new Input();
              inputVTP.setPhoneNumber(
                  new JAXBElement(new QName("http://ipcc.itpro.vn/xsd", "phoneNumber"),
                      String.class, us.getMobile()));
              inputVTP.setRecordFileCode(
                  new JAXBElement(new QName("http://ipcc.itpro.vn/xsd", "recordFileCode"),
                      String.class, fileAudioName));
              inputVTP.setResponseUrl(
                  new JAXBElement(new QName("http://ipcc.itpro.vn/xsd", "responseUrl"),
                      String.class, URLFORIPCC));
              inputVTP.setTransactionId(
                  new JAXBElement(new QName("http://ipcc.itpro.vn/xsd", "transactionId"),
                      String.class, transactionId));
              Output out = ipccstlPort.autoCallout(url, inputVTP);
              if (out != null && out.getResultCode().getValue() != null) {
                outPut = new NomalOutput();
                if ("200".equals(out.getResultCode().getValue())) {
                  outPut.setDescription(RESULT.SUCCESS);
                } else {
                  outPut.setDescription(out.getDescription().getValue());
                }
              } else {
                outPut = new NomalOutput();
                outPut.setDescription("IPCC not response result");
              }
            } else {
              outPut = ipccPort.autoCallout(url, input);
            }
            if (outPut != null && !RESULT.SUCCESS.equals(outPut.getDescription())) {
              TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
              result.setKey(RESULT.FAIL);
              return result;
            }
          }
        } else {
          TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
          result.setKey(RESULT.FAIL);
          result.setMessage(I18n.getLanguage("wo.notFindUser"));
          return result;
        }
      } else {
        result.setKey(RESULT.FAIL);
        result.setMessage(I18n.getLanguage("wo.woNotExist"));
        return result;
      }
      result.setKey(RESULT.SUCCESS);
    } catch (Exception e) {
      result.setKey(RESULT.FAIL);
      result.setMessage(e.getMessage());
      log.error(e.getMessage(), e);
      TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
    }
    return result;
  }

  @Override
  public String getConfigProperty() {
    String strReturn = null;
    try {
      Map<String, String> mapConfigProperty = commonRepository.getConfigProperty();
      Gson gson = new Gson();
      strReturn = gson.toJson(mapConfigProperty);
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
    }
    return strReturn;
  }

  @Override
  public ResultDTO insertWoForSPM(WoDTO woDTO) throws Exception {
    ResultDTO resultDTO = new ResultDTO();
    String woId = woRepository.getSeqTableWo(WO_SEQ);
    woDTO.setWoId(woId);
    ResultInSideDto res = insertWoCommon(woDTO);
    resultDTO.setId(res.getIdValue());
    resultDTO.setKey(res.getKey());
    resultDTO.setMessage(res.getMessage());
    return resultDTO;
  }

  @Override
  public ResultDTO deleteWOForRollback(String woCode, String reason, String system) {
    ResultDTO result = new ResultDTO();
    try {
      List<String> lstCode = new ArrayList<>();
      lstCode.add(woCode);
      List<WoInsideDTO> lst = woRepository.getListWoByWoCode(lstCode);
      if (lst != null && lst.size() > 0) {
        WoInsideDTO wo = lst.get(0);
        List<WoHistoryInsideDTO> lstHistory = getListDataByWoId(wo.getWoId());
        if (lstHistory != null && lstHistory.size() > 0) {
          for (WoHistoryInsideDTO i : lstHistory) {
            i.setCreateMessage(1L);
            woHistoryRepository.updateWoHistory(i);
          }
        }
        WoDetailDTO woDetailDTO = woDetailRepository.findWoDetailById(wo.getWoId());
        String acc = "";
        if (woDetailDTO != null) {
          acc = woDetailDTO.getAccountIsdn();
        }
        Date date = new Date();
        WoHistoryInsideDTO woHistory = new WoHistoryInsideDTO();
        woHistory.setCdId(wo.getCdId());
        woHistory.setCreatePersonId(wo.getCreatePersonId());
        woHistory.setFileName(wo.getFileName());
        woHistory.setFtId(wo.getFtId());
        woHistory.setNewStatus(null);
        woHistory.setOldStatus(wo.getStatus());
        woHistory.setUpdateTime(date);
        woHistory.setCreateMessage(1L);
        woHistory.setUserName(system);
        woHistory.setWoCode(wo.getWoCode());
        woHistory.setWoContent(wo.getWoContent());
        woHistory.setWoId(wo.getWoId());
        woHistory.setComments(
            I18n.getLanguage("wo.deleteWoFrom") + " " + system + ":" + acc + ":" + reason);
        wo.setLastUpdateTime(date);
        wo.setStatus(Long.valueOf(Constants.WO_STATUS.UNASSIGNED));
        ResultInSideDto resultDe = woRepository.deleteWo(wo.getWoId());
        if (RESULT.SUCCESS.equals(resultDe.getKey())) {
          woHistoryRepository.insertWoHistory(woHistory);
        } else {
          TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
          result.setKey(RESULT.FAIL);
          result.setMessage("[WO_ERROR]");
        }
        result.setKey(RESULT.SUCCESS);
        result.setMessage("rollback success");
      } else {
        result.setKey(RESULT.SUCCESS);
        result.setMessage("[WO_ERROR]: wo is not exixts");
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      result.setKey(RESULT.FAIL);
      result.setMessage("[WO_ERROR]: " + e.getMessage());
    }
    return result;
  }

  @Override
  public String updateWo(WoDTO woDTO) throws ParseException, IOException {
    UsersEntity usersEntity = new UsersEntity();
    UnitDTO unitToken = new UnitDTO();
    if (StringUtils.isNotNullOrEmpty(woDTO.getCreatePersonId())) {
      usersEntity = userRepository.getUserByUserId(Long.valueOf(woDTO.getCreatePersonId()));
      unitToken = unitRepository.findUnitById(usersEntity.getUnitId());
    }
    List<GnocFileDto> gnocFileDtos = new ArrayList<>();
    List<String> lstFileName = new ArrayList<>();
    GnocFileDto gnocFileGuideDtoOld = new GnocFileDto();
    gnocFileGuideDtoOld.setBusinessCode(GNOC_FILE_BUSSINESS.WO_TYPE_FILES_GUIDE);
    gnocFileGuideDtoOld.setBusinessId(Long.valueOf(woDTO.getWoTypeId()));
    List<GnocFileDto> gnocFileGuideDtosOld = gnocFileRepository
        .getListGnocFileByDto(gnocFileGuideDtoOld);
    for (GnocFileDto gnocFileGuideDto : gnocFileGuideDtosOld) {
      if (StringUtils.isNotNullOrEmpty(gnocFileGuideDto.getPath())) {
        byte[] bytes = FileUtils
            .getFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
                PassTranformer.decrypt(ftpPass), gnocFileGuideDto.getPath());
        String fullPath = FileUtils
            .saveFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
                PassTranformer.decrypt(ftpPass), ftpFolder, gnocFileGuideDto.getFileName(), bytes,
                FileUtils.createDateOfFileName(
                    DateUtil.convertStringToTime(woDTO.getCreateDate(), Constants.ddMMyyyyHHmmss)));
        String fileName = gnocFileGuideDto.getFileName();
        //Start save file old
        lstFileName.add(FileUtils.getFileName(fullPath));
        //End save file old
        GnocFileDto gnocFileDto = new GnocFileDto();
        gnocFileDto.setPath(fullPath);
        gnocFileDto.setFileName(FileUtils.getFileName(fullPath));
        gnocFileDto.setCreateUnitId(usersEntity.getUnitId());
        gnocFileDto.setCreateUnitName(unitToken.getUnitName());
        gnocFileDto.setCreateUserId(usersEntity.getUserId());
        gnocFileDto.setCreateUserName(usersEntity.getUsername());
        gnocFileDto.setCreateTime(
            DateUtil.convertStringToTime(woDTO.getCreateDate(), Constants.ddMMyyyyHHmmss));
        gnocFileDto.setMappingId(Long.valueOf(woDTO.getWoId()));
        gnocFileDtos.add(gnocFileDto);
      }
    }
    gnocFileRepository
        .saveListGnocFileNotDeleteAll(GNOC_FILE_BUSSINESS.WO, Long.valueOf(woDTO.getWoId()),
            gnocFileDtos);
    String fileNameNew = lstFileName.size() > 0 ? String.join(",", lstFileName) : "";
    boolean checkFileName = StringUtils.isNotNullOrEmpty(woDTO.getFileName()) && StringUtils
        .isNotNullOrEmpty(fileNameNew);
    woDTO.setFileName(
        (StringUtils.isStringNullOrEmpty(woDTO.getFileName()) ? ""
            : woDTO.getFileName())
            + (checkFileName ? "," : "") + (StringUtils.isStringNullOrEmpty(fileNameNew) ? ""
            : fileNameNew));
    woDTO.setSyncStatus(null);
    WoDTO o = findWoById(Long.valueOf(woDTO.getWoId()));
    woDTO.setCableCode(o.getCableCode());
    woDTO.setCableTypeCode(o.getCableTypeCode());
    woDTO.setDistance(o.getDistance());
    woDTO.setVibaLineCode(o.getVibaLineCode());
    ResultInSideDto resultInSideDto = woRepository.updateWo(woDTO.toModelInSide());
    return resultInSideDto.getKey();
  }

  @Override
  public WoDTO findWoById(Long id) {
    if (id != null && id > 0) {
      WoInsideDTO woInsideDTO = woRepository.findWoByIdNoOffset(id);
      if (woInsideDTO != null) {
        WoDTO woDTO = new WoDTO();
        woDTO = woInsideDTO.toModelOutSide();
        woDTO.setDefaultSortField("name");
        return woDTO;
      }
    }
    return null;
  }

  @Override
  public ResultDTO closeWo(List<String> listCode, String system) {
    ResultInSideDto resultInSideDto = closeWoCommon(listCode, system);
    return resultInSideDto.toResultDTO();
  }

  @Override
  public List<ResultDTO> getWOSummaryInfobyUserCommon(String userId, int typeSearch, Long cdId,
      String createTimeFrom, String createTimeTo) {
    return woRepository
        .getWOSummaryInfobyUser(userId, typeSearch, cdId, createTimeFrom, createTimeTo);
  }

  @Override
  public ResultInSideDto updateStatusCommon(VsmartUpdateForm updateForm, String username,
      String woId, String status, String comment, String ccResult, String qrCode,
      List<WoMaterialDeducteInsideDTO> listMaterial, Long reasonIdLv1, Long reasonIdLv2,
      Long actionKTTS,
      List<WoMerchandiseInsideDTO> lstMerchandise, String reasonKtts, String handoverUser,
      String sessionId, String ipPortParentNode,
      List<String> listFileName, List<byte[]> fileArr)
      throws Exception {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    if (ccResult != null && "-1".equals(ccResult)) {
      ccResult = null;
    }
    List<UsersInsideDto> listUser = userRepository.getListUserDTOByuserName(username);
    //validateUpdateStatus
    // trang thai moi
    if (!Constants.WO_STATUS.INPROCESS.equals(status) && !Constants.WO_STATUS.CLOSED_FT
        .equals(status)) {
      throw new Exception(I18n.getLanguage("wo.InvaidWoStatus"));
    }
    // wo id
    if (StringUtils.isStringNullOrEmpty(woId) || !StringUtils.isLong(woId)) {
      throw new Exception(I18n.getLanguage("wo.WoIdIsNotNull"));
    }
    // comment
    if (comment != null && comment.length() > 1000) {
      throw new Exception(I18n.getLanguage("wo.CommentNotToLongThan1000Char"));
    }
    // user
    if (listUser == null || listUser.isEmpty()) {
      throw new Exception(I18n.getLanguage("wo.userNameNotExists"));
    }
    UsersInsideDto ft = listUser.get(0);
    Long woIdValue = Long.valueOf(woId);
    WoInsideDTO wo = woRepository.findWoByIdNoWait(woIdValue);
    WoDetailDTO woDetail = woDetailRepository.findWoDetailById(woIdValue);

    // <editor-fold defaultstate="collapsed" desc="Validate">
    if (wo == null) {
      throw new Exception(I18n.getLanguage("wo.woIsNotExists"));
    }
    if (wo.getFtId() == null || !wo.getFtId().equals(ft.getUserId())) {
      throw new Exception(I18n.getLanguage("wo.UserIsNotHavePermissionForWO"));
    }
    if (!wo.getStatus().equals(Long.valueOf(Constants.WO_STATUS.ACCEPT))
        && !wo.getStatus().equals(Long.valueOf(Constants.WO_STATUS.INPROCESS))) {
      throw new Exception(
          I18n.getLanguage("wo.notUpdateWhenStatusIs") + " " + convertWoStatus(wo.getStatus()));
    }
    if (status.equals(Constants.WO_STATUS.CLOSED_FT)
        && !wo.getStatus().equals(Long.valueOf(Constants.WO_STATUS.INPROCESS))) {
      throw new Exception(
          I18n.getLanguage("wo.notCloseWhenStatusIs") + " " + convertWoStatus(wo.getStatus()));
    }
    Date now = new Date();
    Map<String, String> mapConfigProperty = commonRepository.getConfigProperty();
    Long timeRemain = now.getTime() - wo.getEndTime().getTime();
    if (status.equals(Constants.WO_STATUS.CLOSED_FT) && timeRemain > 0 && (WO_MASTER_CODE.WO_SPM
        .equals(wo.getWoSystem()) || INSERT_SOURCE.SPM_VTNET.equals(wo.getWoSystem()))) {
      if (reasonIdLv1 == null || reasonIdLv2 == null) {
        throw new Exception(I18n.getLanguage("wo.reasonOverdueIsNotNull"));
      }
    }
    // <editor-fold defaultstate="collapsed" desc="Prepare du lieu vat tu">
    if ((listMaterial != null && listMaterial.size() > 0)
        || (updateForm.getLstGoodsNew() != null && updateForm.getLstGoodsNew().size() > 0)
        || (updateForm.getLstGoodsOld() != null && updateForm.getLstGoodsOld().size() > 0)) {

      if (listMaterial == null || listMaterial.size() == 0) {
        listMaterial = new ArrayList<>();
      }
      // danh sach vat tu
      if (listMaterial.size() > 0) {
        for (WoMaterialDeducteInsideDTO material : listMaterial) {
          material.setWoId(StringUtils.isStringNullOrEmpty(woId)? null : Long.valueOf(woId));
          material.setUserName(username);
          material.setUserId(ft.getUserId());
          material.setType(0L);
        }
      }

      // danh sach thiet bi thay the
      if (updateForm.getLstGoodsNew() != null && updateForm.getLstGoodsNew().size() > 0) {
        for (WoMaterialDeducteDTO material : updateForm.getLstGoodsNew()) {
          material.setWoId(woId);
          material.setUserName(username);
          material.setUserId(ft.getUserId().toString());
          material.setType("1");
          listMaterial.add(material.toModelInSide());
        }
      }

      // danh sach thiet bi thu hoi
      if (updateForm.getLstGoodsOld() != null && updateForm.getLstGoodsOld().size() > 0) {
        for (WoMaterialDeducteDTO material : updateForm.getLstGoodsOld()) {
          material.setWoId(woId);
          material.setUserName(username);
          material.setUserId(ft.getUserId().toString());
          material.setType("2");
          listMaterial.add(material.toModelInSide());
        }
      }
    }

    if ((WO_MASTER_CODE.WO_SPM.equals(wo.getWoSystem()) || INSERT_SOURCE.SPM_VTNET
        .equals(wo.getWoSystem())) && status
        .equals(Constants.WO_STATUS.CLOSED_FT)) {
      if (ccResult == null) {
        throw new Exception(I18n.getLanguage("wo.reason3LvIsNotNull"));
      }
      // neu WO dang tich help ko cho hoan thanh
      if (wo.getNeedSupport() != null && wo.getNeedSupport().equals(1L)) {
        if (checkProperty(mapConfigProperty, "1",
            Constants.WO_BUSINESS_CHECK.COMPLETED_FIRST_NEED_SUPPORT)) {
          throw new Exception(I18n.getLanguage("wo.cannotCompleteWhenNeedSupport"));
        }
      }
      // validate danh sach vat tu ko cung nhom
      if (listMaterial != null && listMaterial.size() > 0) {
        List<String> lstMaterialGroup = new ArrayList<>();
        for (WoMaterialDeducteInsideDTO material : listMaterial) {
          if (StringUtils.isNotNullOrEmpty(material.getMaterialGroupCode())) {
            if (lstMaterialGroup.contains(material.getMaterialGroupCode() + String.valueOf(material.getType()))) {
              throw new Exception(I18n.getLanguage("wo.duplicateMaterialGroupCode"));
            } else {
              lstMaterialGroup.add(material.getMaterialGroupCode() + String.valueOf(material.getType()));
            }
          }
        }
      }
    }
    //neu ton tai wo_detail thi moi cap nhat thong tin
    if (woDetail != null) {
      WoTypeServiceInsideDTO serviceConfig = woTypeServiceRepository
          .getTypeService(wo.getWoTypeId(), woDetail.getServiceId());
      if (serviceConfig != null) {
        if (serviceConfig.getIsCheckQrCode() != null && serviceConfig.getIsCheckQrCode().equals(1L)
            && StringUtils.isStringNullOrEmpty(qrCode) && status
            .equals(Constants.WO_STATUS.CLOSED_FT)) {
          throw new Exception(I18n.getLanguage("wo.notResultCheckServiceQuality"));
        }
      }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Luu file dinh kem">
    updateFile(wo, ft.getUserId(), listFileName, fileArr);
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Luu danh sach vat tu Co dinh">
    if (listMaterial != null && !listMaterial.isEmpty()) {
//      for (WoMaterialDeducteInsideDTO material : listMaterial) {
//        material.setWoId(Long.valueOf(woId));
//        material.setUserName(username);
//        material.setUserId(ft.getUserId());
//      }
      ResultInSideDto rsDedute = woMaterialDeducteBusiness.putMaterialDeducte(listMaterial);
      if (!RESULT.SUCCESS.equals(rsDedute.getKey())) {
        throw new Exception(I18n.getLanguage("wo.failPutMaterialDeduct"));
      }
    } else if (status.equals(Constants.WO_STATUS.CLOSED_FT)) {
      ResultInSideDto rsDedute = woMaterialDeducteBusiness
          .deleteMaterialDeducte(wo.getWoId(), wo.getFtId());
      if (!RESULT.SUCCESS.equals(rsDedute.getKey())) {
        throw new Exception(I18n.getLanguage("wo.failDelMaterialDeduct"));
      }
    }
    // </editor-fold>

    // thuc hien validate vat tu
    if (status.equals(Constants.WO_STATUS.CLOSED_FT)) {
      WoMaterialDeducteInsideDTO dto = new WoMaterialDeducteInsideDTO();
      dto.setWoId(wo.getWoId());
      List<WoMaterialDeducteInsideDTO> lst = woMaterialDeducteBusiness
          .onSearch(dto, 0, Integer.MAX_VALUE, "", "");
      String resValidateIM = null;
      if (lst != null && lst.size() > 0) {
        resValidateIM = woMaterialDeducteBusiness.validateMaterialCompleted(lst);
      }
      if (StringUtils.isNotNullOrEmpty(resValidateIM)) {
//        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        throw new Exception(resValidateIM);
      }
    }

    // <editor-fold defaultstate="collapsed" desc="Thuc hien hoan thanh cap nhat lai file excel">
    if (status.equals(Constants.WO_STATUS.CLOSED_FT)
        && (checkProperty(mapConfigProperty, String.valueOf(wo.getWoTypeId()),
        Constants.AP_PARAM.WO_TEST_SERVICE)
        || checkProperty(mapConfigProperty, String.valueOf(wo.getWoTypeId()),
        Constants.AP_PARAM.WO_TYPE_DO_XANG_DAU)
        || checkProperty(mapConfigProperty, String.valueOf(wo.getWoTypeId()),
        Constants.AP_PARAM.WO_TYPE_XLSCVT)
        || checkProperty(mapConfigProperty, String.valueOf(wo.getWoTypeId()),
        Constants.AP_PARAM.WO_TYPE_DO_XANG_DAU_MYT))) {
      if (updateForm != null && updateForm.getLstDataKeyValue() != null) {
        ResultInSideDto resKeyValue = updateKeyValueExcel(updateForm.getLstDataKeyValue(), wo, ft);
        if (!RESULT.SUCCESS.equals(resKeyValue.getKey())) {
//          TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
          throw new Exception(resKeyValue.getMessage());
        }
      }
    }
    // </editor-fold>
    WoHistoryInsideDTO woHistory = new WoHistoryInsideDTO();
    woHistory.setCdId(wo.getCdId());
    woHistory.setComments(comment);
    woHistory.setCreatePersonId(wo.getCreatePersonId());
    woHistory.setFileName(wo.getFileName());
    woHistory.setFtId(wo.getFtId());
    woHistory.setNewStatus(Long.valueOf(status));
    woHistory.setOldStatus(wo.getStatus());
    woHistory.setUpdateTime(now);
    woHistory.setUserId(ft.getUserId());
    woHistory.setUserName(ft.getUsername());
    woHistory.setWoCode(wo.getWoCode());
    woHistory.setWoContent(wo.getWoContent());
    woHistory.setWoId(wo.getWoId());
    woHistoryRepository.insertWoHistory(woHistory);

    // luu thong tin thoi diem hoan thanh / comment hoan thanh
    if (status.equals(Constants.WO_STATUS.CLOSED_FT)) {
      wo.setCompletedTime(now);
      wo.setCommentComplete(comment);
      wo.setIsCompletedOnVsmart(1L);
    }
    // luu them thong tin nha tram nims
    wo.setStationCodeNims(
        (updateForm != null && StringUtils.isNotNullOrEmpty(updateForm.getStationCodeNims()))
            ? updateForm.getStationCodeNims() : null);
    wo.setStatus(Long.valueOf(status));
    wo.setLastUpdateTime(now);

    // <editor-fold defaultstate="collapsed" desc="Cap nhat nguyen nhan qua han">
    String reasonOverdueLV1Name = "";
    String reasonOverdueLV2Name = "";
    if (reasonIdLv1 != null && !reasonIdLv1.equals(-1L) && reasonIdLv2 != null && !reasonIdLv2
        .equals(-1L)) {
      List<CompCause> lst1 = getListReasonOverdue(null, wo.getNationCode());
      List<CompCause> lst2 = getListReasonOverdue(reasonIdLv1, wo.getNationCode());

      if (lst1 != null && lst1.size() > 0) {
        for (CompCause i : lst1) {
          if (i.getCompCauseId().equals(reasonIdLv1)) {
            reasonOverdueLV1Name = i.getName();
            break;
          }
        }
      }
      if (lst2 != null && lst2.size() > 0) {
        for (CompCause i : lst2) {
          if (i.getCompCauseId().equals(reasonIdLv2)) {
            reasonOverdueLV2Name = i.getName();
            break;
          }
        }
      }
      wo.setReasonOverdueLV1Id(String.valueOf(reasonIdLv1));
      wo.setReasonOverdueLV1Name(reasonOverdueLV1Name);
      wo.setReasonOverdueLV2Id(String.valueOf(reasonIdLv2));
      wo.setReasonOverdueLV2Name(reasonOverdueLV2Name);
    }
    // </editor-fold>

    if (woDetail != null) {
      woDetail
          .setCcResult(StringUtils.isStringNullOrEmpty(ccResult) ? null : Long.valueOf(ccResult));
      woDetail.setCheckQrCode(qrCode);
      //<editor-fold defaultstate="collapsed" desc="Luu thong tin SCVT">
      if (checkProperty(mapConfigProperty, String.valueOf(wo.getWoTypeId()),
          Constants.AP_PARAM.WO_TYPE_XLSCVT)) {

        woDetail.setResultCheck(updateForm.getResultCheck());
        woDetail.setResultCheckName(updateForm.getResultCheckName());
        // luu thong tin ticket info
        String troubleInfo = "result:" + updateForm.getResultCheckName() + ";";
        if (updateForm != null && updateForm.getLstDataKeyValue() != null) {
          for (ObjKeyValue o : updateForm.getLstDataKeyValue()) {
            troubleInfo = troubleInfo + o.getKey() + ":" + o.getValue() + ";";
          }
        }
        woDetail.setInfoTicket(troubleInfo);
      }
      //</editor-fold>
      woDetailRepository.insertUpdateWoDetail(woDetail);
    }

    // set du lieu hoan thanh luong scvt
    if (checkProperty(mapConfigProperty, String.valueOf(wo.getWoTypeId()),
        Constants.AP_PARAM.WO_TYPE_XLSCVT)) {
      if (updateForm != null) {
        if (StringUtils.isNotNullOrEmpty(updateForm.getCellService())) {
          wo.setCellService(updateForm.getCellService());
        }
        if (StringUtils.isNotNullOrEmpty(updateForm.getConcaveAreaCode())) {
          wo.setConcaveAreaCode(updateForm.getConcaveAreaCode());
        }
        if (StringUtils.isNotNullOrEmpty(updateForm.getLongitude())) {
          wo.setLongitude(updateForm.getLongitude());
        }
        if (StringUtils.isNotNullOrEmpty(updateForm.getLatitude())) {
          wo.setLatitude(updateForm.getLatitude());
        }
        if (StringUtils.isNotNullOrEmpty(updateForm.getEstimateTime())) {
          wo.setEstimateTime(DateTimeUtils.convertStringToDate(updateForm.getEstimateTime()));
        }
        if (StringUtils.isNotNullOrEmpty(updateForm.getSolutionGroupName())) {
          wo.setSolutionGroupName(updateForm.getSolutionGroupName());
        }
        if (StringUtils.isNotNullOrEmpty(updateForm.getSolution())) {
          wo.setSolutionDetail(updateForm.getSolution());
        }
        if (StringUtils.isNotNullOrEmpty(updateForm.getReasonDetail())) {
          wo.setReasonDetail(updateForm.getReasonDetail());
        }
        // thuc hien luu worklog
        if (status.equals(Constants.WO_STATUS.CLOSED_FT)) {
          try {
            WoWorklogInsideDTO woWorklogInsideDTO = new WoWorklogInsideDTO(wo);
            woWorklogInsideDTO.setUsername(ft.getUsername());
            String worklogContent =
                I18n.getLanguage("wo.Reasondetail") + " " + wo.getReasonDetail() + "\r\n"
                    + I18n.getLanguage("wo.SolutionDetail") + " " + wo.getSolutionDetail() + "\r\n"
                    + I18n.getLanguage("wo.CellService") + " " + wo.getCellService() + "\r\n"
                    + I18n.getLanguage("wo.Comment") + " " + wo.getCommentComplete();
            woWorklogInsideDTO.setWoWorklogContent(worklogContent);
            woWorklogRepository.insertWoWorklog(woWorklogInsideDTO);
          } catch (Exception e) {
            log.error(e.getMessage(), e);
//            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
          }
        }
      }
    }

    // <editor-fold defaultstate="collapsed" desc="Update sang SPM">
    UnitDTO unit = unitRepository.findUnitById(ft.getUnitId());
    TroublesDTO o = new TroublesDTO();
    o.setFileDocumentByteArray(fileArr);
    o.setArrFileName(listFileName);
    String commentTT =
        ft.getUsername() + " : " + DateUtil.date2ddMMyyyyHHMMss(new Date()) + " " + I18n
            .getLanguage("wo.updateStatus") + ":" + getWoStatusName(Integer.valueOf(status))
            + " comment:" + comment;
    String commentTT_SCVT =
        ft.getUsername() + " : " + DateUtil.date2ddMMyyyyHHMMss(new Date()) + " " + I18n
            .getLanguage("wo.updateStatus") + ":" + getWoStatusName(Integer.valueOf(status))
            + " comment:" + comment
            + " " + I18n.getLanguage("wo.Reasondetail") + wo.getReasonDetail()
            + I18n.getLanguage("wo.SolutionDetail") + wo.getSolutionDetail();
    ResultDTO resTT = null;
    try {
      if (WO_MASTER_CODE.WO_SPM.equals(wo.getWoSystem()) || INSERT_SOURCE.SPM_VTNET
          .equals(wo.getWoSystem())) {
        resTT = updateStatusTT(o, wo, null, null, unit, ft, commentTT, "UPDATE",
            getWoStatusName(Integer.valueOf(status)), null);
      } else if (checkProperty(mapConfigProperty, String.valueOf(wo.getWoTypeId()),
          Constants.AP_PARAM.WO_TYPE_XLSCVT)) {
        o.setCustomerTimeDesireFrom(woDetail.getResultCheckName());
        o.setCustomerTimeDesireTo(woDetail.getResultCheck());
        resTT = updateStatus_SCVT(o, wo, null, null, unit, ft, commentTT_SCVT, "UPDATE",
            getWoStatusName(Integer.valueOf(status)), null);
      }
      if (resTT != null && !RESULT.SUCCESS.equals(resTT.getKey())) {
//        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        throw new Exception(": resultSpm.getKey() :" + resTT.getMessage());
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
//      TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
      throw new Exception(I18n.getLanguage("wo.errCommunicateSPM") + " " + e.getMessage());
    }
    // </editor-fold>

    if (WO_MASTER_CODE.WO_TT.equals(wo.getWoSystem())) {
      // <editor-fold defaultstate="collapsed" desc="Cap nhat thong tin wo_trouble_info">
      try {
        if (status.equals(Constants.WO_STATUS.CLOSED_FT)) {
          WoTroubleInfoDTO woTroubleInfo = woTroubleInfoRepository
              .getWoTroubleInfoByWoId(wo.getWoId());
          WoInsideDTO dto = new WoInsideDTO();
          // tiennv update WO: chi check canh bao voi cac loai khac amiOne
          if (!checkProperty(mapConfigProperty, String.valueOf(wo.getWoTypeId()),
              Constants.AP_PARAM.WO_TYPE_AMI_ONE)) {
            ResultDTO resFromTT = ttServiceProxy
                .checkAlarmNOC(wo.getWoSystemId(), String.valueOf(wo.getWoTypeId()));
            if (RESULT.SUCCESS.equalsIgnoreCase(resFromTT.getKey())) {
              if ("NOC".equals(resFromTT.getMessage()) || StringUtils
                  .isStringNullOrEmpty(resFromTT.getMessage())) {
                if ("NOC".equals(resFromTT.getMessage())
                    && StringUtils.isStringNullOrEmpty(resFromTT.getFinishTime())) {
//                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                  throw new Exception(I18n.getLanguage("wo.onlyCompleteWhenAlarmClear"));
                } else {
                  dto.setClearTime(DateTimeUtils.convertStringToDate(resFromTT.getFinishTime()));
                }
              }
            } else {
              if ("NOC".equals(resFromTT.getMessage())
                  && StringUtils.isStringNullOrEmpty(resFromTT.getFinishTime())) {
//              TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                throw new Exception(I18n.getLanguage("wo.onlyCompleteWhenAlarmClear"));
              }
//            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
              throw new Exception("[TT]:" + resFromTT.getMessage());
            }
            if (woTroubleInfo != null && updateForm != null
                && updateForm.getReasonTroubleId() != null
                && woTroubleInfo.getRequiredTtReason() != null && woTroubleInfo
                .getRequiredTtReason()
                .equals(1L)) {
              if (status.equals(Constants.WO_STATUS.CLOSED_FT)
                  && !checkProperty(mapConfigProperty, String.valueOf(wo.getWoTypeId()),
                  Constants.AP_PARAM.WO_TYPE_QLTS)) {
                dto.setWoId(Long.valueOf(woId));
                dto.setReasonTroubleId(Long.valueOf(updateForm.getReasonTroubleId()));
                dto.setReasonTroubleName(updateForm.getReasonTroubleName());
                dto.setSolution(updateForm.getSolution());
                if (!StringUtils.isLongNullOrEmpty(updateForm.getSolutionGroupId())) {
                  dto.setSolutionGroupId(updateForm.getSolutionGroupId());
                }
                dto.setSolutionGroupName(updateForm.getSolutionGroupName());
                if (!StringUtils.isDoubleNullOrEmpty(updateForm.getPolesDistance())) {
                  dto.setPolesDistance(updateForm.getPolesDistance());
                }
                if (!StringUtils.isLongNullOrEmpty(updateForm.getScriptId())) {
                  dto.setScriptId(updateForm.getScriptId());
                  dto.setScriptName(updateForm.getScriptName());
                }
                woTroubleInfo = woTroubleInfo != null ? woTroubleInfo : new WoTroubleInfoDTO();
                if (updateForm != null) {
                  woTroubleInfo.setClosuresReplace(
                      updateForm.getClosuresReplace() != null ? updateForm.getClosuresReplace()
                          : null);
                  woTroubleInfo.setLineCutCode(
                      updateForm.getLineCutCode() != null ? updateForm.getLineCutCode() : null);
                  woTroubleInfo.setCodeSnippetOff(
                      updateForm.getCodeSnippetOff() != null ? updateForm.getCodeSnippetOff()
                          : null);
                }
                updateWoTroubleInfo(woTroubleInfo, dto);
              }
            } else if (StringUtils.isNotNullOrEmpty(resFromTT.getFinishTime())) {
              dto.setClearTime(DateTimeUtils.convertStringToDate(resFromTT.getFinishTime()));
              woTroubleInfo = woTroubleInfo != null ? woTroubleInfo : new WoTroubleInfoDTO();
              if (updateForm != null) {
                woTroubleInfo.setClosuresReplace(
                    updateForm.getClosuresReplace() != null ? updateForm.getClosuresReplace()
                        : null);
                woTroubleInfo.setLineCutCode(
                    updateForm.getLineCutCode() != null ? updateForm.getLineCutCode() : null);
                woTroubleInfo.setCodeSnippetOff(
                    updateForm.getCodeSnippetOff() != null ? updateForm.getCodeSnippetOff() : null);
              }
              updateWoTroubleInfo(woTroubleInfo, dto);
            }
          }
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
//        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        throw new Exception(
            I18n.getLanguage("wo.haveSomeErrWhenUpdateReasonTrouble") + ": " + e.getMessage());
      }
      // </editor-fold>
    }
    // tao WO thu hoi doan cap
    if (status.equals(Constants.WO_STATUS.CLOSED_FT)) {
      if (checkProperty(mapConfigProperty, String.valueOf(wo.getWoTypeId()),
          Constants.AP_PARAM.WO_TYPE_THUHOI_CAP_NIMS)) {
        if (updateForm.getIsRetrieve() != null && updateForm.getIsRetrieve().equals(1L)) {
          ResultInSideDto resCreate = autoCreateWOThuHoiNims(wo);
          if (!RESULT.SUCCESS.equals(resCreate.getKey())) {
//            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new Exception("[KTTS_Create_Auto]" + resCreate.getMessage());
          }
        }
      }
      // <editor-fold defaultstate="collapsed" desc="Loai cong viec sua chua co dien tram">
      if (checkProperty(mapConfigProperty, String.valueOf(wo.getWoTypeId()),
          Constants.AP_PARAM.WO_TYPE_SCTBCD_ACCESS)) {
        wo.setNewFailureReason(
            StringUtils.isNotNullOrEmpty(updateForm.getNewFailureReason()) ? updateForm
                .getNewFailureReason() : null);
        wo.setHasCost(
            updateForm.getHasCost() != null ? Long.valueOf(updateForm.getHasCost()) : null);
        // luu danh sach thiet bi
      }
      // </editor-fold>

      // <editor-fold defaultstate="collapsed" desc="Cap nhat vat tu luong WO SCTBCD_ACCESS">
      if (checkProperty(mapConfigProperty, String.valueOf(wo.getWoTypeId()),
          Constants.AP_PARAM.WO_TYPE_SCTBCD_ACCESS)) {
        try {
          // xoa het vat tu cu
          materialCodienBusiness.deleteMaterialCodienByWoId(Long.valueOf(woId));
          if (updateForm != null && updateForm.getLstMaterialCodien() != null) {
            // them du lieu moi
            for (MaterialCodienDTO dto : updateForm.getLstMaterialCodien()) {
              dto.setWoId(Long.valueOf(woId));
              ResultInSideDto res = materialCodienBusiness.inserOrUpdateMaterialCodien(dto);
              if (!RESULT.SUCCESS.equals(res.getKey())) {
//                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                throw new Exception(I18n.getLanguage("wo.haveSomeErrWhenSaveMaterialCodien"));
              }
            }
          }
        } catch (Exception e) {
          log.error(e.getMessage(), e);
//          TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
          throw new Exception("[GNOC]" + e.getMessage());
        }
      }
      // </editor-fold>

      // goi sang NOC bao check cong xuat
      // <editor-fold defaultstate="collapsed" desc="Hoan thanh check cong suat Viba">
      if (checkProperty(mapConfigProperty, String.valueOf(wo.getWoTypeId()),
          Constants.AP_PARAM.WO_TYPE_BD_VIBA)) {
        try {
          ResultInSideDto resViba = checkVibaFromNoc(wo.getVibaLineCode(), wo.getStationCode(),
              null, 1L);
          if (!RESULT.SUCCESS.equals(resViba.getKey())) {
//            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new Exception("[GNOC]" + resViba.getMessage());
          }
        } catch (Exception e) {
          log.error(e.getMessage(), e);
//          TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
          throw new Exception("[GNOC]" + e.getMessage());
        }
      }
      // </editor-fold>

      // <editor-fold defaultstate="collapsed" desc="Hoan thanh wo ra soat di roi tao tu dong WO huy tram/di doi tram">
      if (checkProperty(mapConfigProperty, String.valueOf(wo.getWoTypeId()),
          Constants.AP_PARAM.WO_TYPE_NIMS_BAO_CAO_DI_DOI)) {
        try {
          if (updateForm != null) {
            if (updateForm.getIsRetrieve() != null && updateForm.getIsRetrieve()
                .equals(1L)) {// huy tram
              ResultInSideDto res = manualCreateWo(wo, 1L);
              wo.setCommentComplete(Constants.AP_PARAM.NIMS_HUY_TRAM + ": " + comment);
              if (!RESULT.SUCCESS.equals(res.getKey())) {
//                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                throw new Exception("[GNOC]" + res.getMessage());
              }
            } else if (updateForm.getIsRetrieve() != null && updateForm.getIsRetrieve()
                .equals(2L)) {// di doi tram
              ResultInSideDto res = manualCreateWo(wo, 2L);
              wo.setCommentComplete(Constants.AP_PARAM.NIMS_HUY_VI_TRI + ": " + comment);
              if (!RESULT.SUCCESS.equals(res.getKey())) {
//                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                throw new Exception("[GNOC]" + res.getMessage());
              }
            } else {
              wo.setCommentComplete(Constants.AP_PARAM.NIMS_GIU_TRAM + ": " + comment);
            }
          }
        } catch (Exception e) {
          log.error(e.getMessage(), e);
//          TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
          throw new Exception("[GNOC]" + e.getMessage());
        }
      }
      // </editor-fold>

      // <editor-fold defaultstate="collapsed" desc="Hoan thanh wo xu ly can nhieu luu nguyen nhan">
      if (updateForm != null && updateForm.getReasonInterference() != null) {
        wo.setReasonInterference(updateForm.getReasonInterference() != null ? Long
            .valueOf(updateForm.getReasonInterference()) : null);
      }
      // </editor-fold>

      // <editor-fold defaultstate="collapsed" desc="Hoan thanh WO CC_STL">
      if (checkProperty(mapConfigProperty, String.valueOf(wo.getWoTypeId()),
          Constants.AP_PARAM.WO_TYPE_CDBR_FOR_STL)) {
        try {
          // thuc hien goi cc stl neu khong co vat tu dong thoi dong WO lai
          if ("0".equals(updateForm.getHasMaterial())) {
            com.viettel.webservice.cc_stl.Input input = new com.viettel.webservice.cc_stl.Input();
            input.setUsername(userCcSTL);
            input.setPassword(passCcSTL);
            input.setWscode("updateComplain");
            List<Param> lstParam = new ArrayList();
            lstParam.add(setParam("username", "vsmart"));
            lstParam.add(setParam("woid", String.valueOf(wo.getWoId())));
            lstParam.add(setParam("complainid", wo.getWoSystemId()));
            lstParam.add(setParam("account", woDetail.getAccountIsdn()));
            lstParam.add(setParam("status", Constants.WO_STATUS.CLOSED_CD));
            lstParam.add(setParam("resultcontent", comment)); //tiennv update WO
            lstParam.add(setParam("datetime", DateUtil.date2ddMMyyyyHHMMss(new Date())));
            lstParam.add(setParam("staffcode", ft.getStaffCode()));

            input.getParam().addAll(lstParam);

            com.viettel.webservice.cc_stl.Output output = ccStlPort.updateComplain(input);

            if (!RESULT.SUCCESS.equalsIgnoreCase(output.getDescription())) { // loi voi gate way
//              TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
              throw new Exception("[CC_STL]" + output.getError() + "-" + output.getDescription());
            } else {
              String original = output.getOriginal();
              String des = original.substring(original.indexOf("<description>") + 13,
                  original.indexOf("</description>"));
              String res = original.substring(original.indexOf("<responseCode>") + 14,
                  original.indexOf("</responseCode>"));

              if (des == null || res == null) { // ko lay dc thong tin original
                throw new Exception("[CC_STL]" + "-" + output.getOriginal());
              } else if (!"0".equalsIgnoreCase(res)) {
                throw new Exception("[CC_STL]" + res + "-" + des);
              }
            }
            // thuc hien dong WO
            wo.setStatus(Long.valueOf(Constants.WO_STATUS.CLOSED_CD));
            wo.setFinishTime(now);
            wo.setResult(Long.valueOf(Constants.WO_RESULT.OK));

            WoHistoryInsideDTO woHistory2 = new WoHistoryInsideDTO();
            woHistory2.setCdId(wo.getCdId());
            woHistory2.setComments(comment);
            woHistory2.setCreatePersonId(wo.getCreatePersonId());
            woHistory2.setFileName(wo.getFileName());
            woHistory2.setFtId(wo.getFtId());
            woHistory2.setNewStatus(Long.valueOf(Constants.WO_STATUS.CLOSED_CD));
            woHistory2.setOldStatus(Long.valueOf(status));
            woHistory2.setUpdateTime(now);
            woHistory2.setUserId(ft.getUserId());
            woHistory2.setUserName(ft.getUsername());
            woHistory2.setWoCode(wo.getWoCode());
            woHistory2.setWoContent("Auto close WO");
            woHistory2.setWoId(wo.getWoId());

            woHistoryRepository.insertWoHistory(woHistory2);
          }

        } catch (Exception e) {
          log.error(e.getMessage(), e);
//          TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
          throw new Exception("[GNOC]" + e.getMessage());
        }
      }
      // </editor-fold>

      //<editor-fold desc="check Qos iMes">
      if (checkProperty(mapConfigProperty, String.valueOf(wo.getWoTypeId()),
          Constants.AP_PARAM.WO_TYPE_BDNT)
          || checkProperty(mapConfigProperty, String.valueOf(wo.getWoTypeId()),
          Constants.AP_PARAM.MR_BDC_BTS_MFD_DH)) {
        try {
          String nationCode = getNationFromUnit(ft.getUnitId());
          ws_imes_port.setNationCode(nationCode);
          if (wo.getDeviceType() != null
              && checkProperty(mapConfigProperty, "1", Constants.AP_PARAM.IS_CALL_IMES)) {
            ResultQoS res = ws_imes_port
                .checkQoS(wo.getStationCode(), Long.valueOf(wo.getDeviceType()));
            if (res != null) {
              if (res.getStatus() != null && res.getStatus().equals(0L)) {
                wo.setNewFailureReason("[" + res.getErrorCode() + "] " + res.getMessage());
              } else { // thuc hien luu lai nguyen nhan
                wo.setNewFailureReason("[" + res.getErrorCode() + "] " + res.getMessage());
                wo.setStatus(Long.valueOf(Constants.WO_STATUS.INPROCESS));
                woHistory.setNewStatus(Long.valueOf(Constants.WO_STATUS.INPROCESS));
                woHistoryRepository.insertWoHistory(woHistory);
                woRepository.updateWo(wo);
                return new ResultInSideDto(null, RESULT.FAIL,
                    "[iMES]" + "[" + res.getErrorCode() + "] " + res.getMessage());
              }
            } else {
              throw new Exception(" No results returned ");
            }
          }
        } catch (Exception e) {
          throw new Exception("[GNOC]" + e.getMessage());
        }
      }
      //</editor-fold>
      // cap nhat sang spm_end

      // <editor-fold defaultstate="collapsed" desc="thuc hien hoan thanh sang KTTS">
      ResultDTO resKtts = null;
      if (status.equals(Constants.WO_STATUS.CLOSED_FT)
          && checkProperty(mapConfigProperty, String.valueOf(wo.getWoTypeId()),
          Constants.AP_PARAM.WO_TYPE_QLTS)) {
        Long scriptIdTmp = null;
        if (updateForm != null) {
          scriptIdTmp = updateForm.getScriptId();
        }
        resKtts = updateWoKTTS(wo, ft, woIdValue, updateForm, actionKTTS,
            lstMerchandise, reasonKtts, handoverUser, mapConfigProperty, scriptIdTmp);
        if (!"OK".equals(resKtts.getKey())) {
          TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
          throw new Exception(resKtts.getMessage());
        }
      }
      // </editor-fold>
      woRepository.updateWo(wo);
      // <editor-fold defaultstate="collapsed" desc="WO SPM thuoc A,P,THC thuc hien dong luon">
      if (status.equals(Constants.WO_STATUS.CLOSED_FT)
          && (WO_MASTER_CODE.WO_SPM.equals(wo.getWoSystem()) || INSERT_SOURCE.SPM_VTNET
          .equals(wo.getWoSystem()))) {
        List<String> lstByPass = getListConfigPropertyValue(
            Constants.AP_PARAM.SERVICE_CLOSE_WHEN_COMPLETE);
        if (lstByPass != null && lstByPass.size() > 0 && woDetail != null
            && woDetail.getServiceId() != null) {
          if (lstByPass.contains(String.valueOf(woDetail.getServiceId()))) {
            if (updateForm == null) {
              updateForm = new VsmartUpdateForm();
            }
            updateForm.setByPassAutoCheck(1L);
            ResultInSideDto resApprove = approveWoCommon(updateForm, username, Long.valueOf(woId),
                "Auto close WO when complete", Constants.WO_RESULT.OK, username);
            if (!RESULT.SUCCESS.equals(resApprove.getKey())) {
              TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
              return resApprove;
            }
          }
        }

      }
      // </editor-fold>

      // <editor-fold defaultstate="collapsed" desc="Goi sang AmiOne">
      if (status.equals(Constants.WO_STATUS.CLOSED_FT)
          && checkProperty(mapConfigProperty, String.valueOf(wo.getWoTypeId()),
          Constants.AP_PARAM.WO_TYPE_AMI_ONE)) {

        try {
          if (updateForm.getByPassAutoCheck() == null
              || !updateForm.getByPassAutoCheck().equals(1L)) {
            ws_amione_port.setInputHeader(null, Constants.REST_FUNC.AMI_ONE_CHECK_ONLINE);
            ResultDTO amiRes = ws_amione_port.checkOnline(woDetail.getAccountIsdn());
            if (amiRes != null && "SUCCESS".equals(amiRes.getKey())) {
              ResultDTO resApprove = approveWo(updateForm, username,
                  woId, "Auto close WO when check online AmiOne OK",
                  Constants.WO_RESULT.OK, username, sessionId, ipPortParentNode);
              if (!"SUCCESS".equals(resApprove.getKey())) {
                ResultInSideDto resultInSideDto1 = new ResultInSideDto(null, resApprove.getKey(),
                    resApprove.getMessage());
                resultInSideDto1.setQuantitySucc(resApprove.getQuantitySucc());
                return resultInSideDto1;
              }
            } else if (amiRes != null) {
              throw new Exception("[AmiOne] Err:" + amiRes.getMessage());
            } else {
              throw new Exception("[GNOC] Error when call AmiOne ");
            }
          }
        } catch (Exception e) {
          log.error(e.getMessage(), e);
          if (e != null && e.getMessage() != null && e.getMessage().contains("[AmiOne] ")) {
            throw new Exception(e.getMessage());
          } else {
            throw new Exception("[GNOC] Error when call AmiOne ");
          }
        }
      }
      // </editor-fold>

      // <editor-fold defaultstate="collapsed" desc="thuc hien dong luon voi 1 so loai WO">

      if (status.equals(Constants.WO_STATUS.CLOSED_FT)
          && checkProperty(mapConfigProperty, String.valueOf(wo.getWoTypeId()),
          Constants.AP_PARAM.WO_TYPE_CLOSE_WHEN_COMPLETE) || (resKtts != null
          && resKtts.getApprove() != null && resKtts.getApprove().equals(1L))) {

        String commentApprove = "Auto close WO when complete";


        if (updateForm == null) {
          updateForm = new VsmartUpdateForm();
        }
        updateForm.setByPassAutoCheck(1L);
        ResultDTO resApprove = null;

        if(resKtts != null && resKtts.getApprove().equals(1L)){
          commentApprove = "Auto close WO from KTTS complete";
          woId = resKtts.getWoId().toString();
        }

        resApprove = approveWo(updateForm, username, woId, commentApprove,
            Constants.WO_RESULT.OK, username, sessionId, ipPortParentNode);

        if (!"SUCCESS".equals(resApprove.getKey())) {
          ResultInSideDto resultInSideDto1 = new ResultInSideDto(null, resApprove.getKey(),
              resApprove.getMessage());
          resultInSideDto1.setQuantitySucc(resApprove.getQuantitySucc());
          return resultInSideDto1;
        }
      }

      // </editor-fold>
    }
    resultInSideDto.setKey("OK");
    resultInSideDto.setQuantitySucc(1);
    return resultInSideDto;
  }

  public ResultDTO updateWoKTTS(WoInsideDTO wo, UsersInsideDto ft, Long woIdValue,
      VsmartUpdateForm updateForm, Long actionKTTS, List<WoMerchandiseInsideDTO> lstMerchandise,
      String reasonKtts, String handoverUser, Map<String, String> mapConfigProperty,
      Long scriptId) {
    ResultDTO resultDTO = new ResultDTO();
    try {
      String nationCode = getNationFromUnit(ft.getUnitId());
      Map<String, Double> mapMer = new HashMap<>();
      WoMerchandiseInsideDTO dtoSearch = new WoMerchandiseInsideDTO(null, wo.getWoId(), null, null,
          null, null, null, null, null, null, null, null, null, null, null, null, null);
      List<WoMerchandiseInsideDTO> lstWoMer = woMerchandiseRepository
          .getListWoMerchandiseDTO(dtoSearch);
      if (lstWoMer != null && lstWoMer.size() > 0) {
        for (WoMerchandiseInsideDTO merDto : lstWoMer) {
          mapMer.put(merDto.getMerchandiseCode().toLowerCase(),
              merDto.getQuantity() != null ? merDto.getQuantity() : null);
        }
      }
      if (checkProperty(mapConfigProperty, String.valueOf(wo.getWoTypeId()),
          Constants.AP_PARAM.WO_TYPE_QLTS_NC)
          || checkProperty(mapConfigProperty, String.valueOf(wo.getWoTypeId()),
          Constants.AP_PARAM.WO_TYPE_QLTS_NC_THUE)) {
        if (actionKTTS.equals(1L)) { // thuc hien
          UsersEntity us = null;
          if (wo.getCdAssignId() != null) {
            us = userRepository.getUserByUserId(wo.getCdAssignId());
          }
          String cd = us != null ? us.getStaffCode() : null;
          Boolean checkUsed = true;
          List<MerchandiseOrderBO> lstMer = new ArrayList<>();
          if (lstMerchandise != null) {
            for (WoMerchandiseInsideDTO t : lstMerchandise) {
              MerchandiseOrderBO o = new MerchandiseOrderBO();
              o.setAmount(t.getQuantity() != null ? t.getQuantity() : null);
              o.setMerCode(t.getMerchandiseCode());
              o.setSerial(t.getSerial());
              o.setLongTermAssetId(
                  t.getLongTermAssetId() != null ? t.getLongTermAssetId() : null);
              lstMer.add(o);
              if (mapMer.containsKey(o.getMerCode().toLowerCase())) {
                if (mapMer.get(o.getMerCode().toLowerCase()).equals(o.getAmount())) {
                  mapMer.remove(o.getMerCode().toLowerCase());
                } else {
                  checkUsed = false;
                }
              } else {
                checkUsed = false;
              }
            }
            if (mapMer != null && mapMer.size() > 0) {
              checkUsed = false;
            }
          }
          Kttsbo kttsBo = kttsVsmartPort
              .updateAcceptanceUCTTNation(woIdValue, cd, ft.getStaffCode(), reasonKtts, lstMer,
                  scriptId, nationCode);
          // thangdt bo sung dong ktts khi tra ve ket qua Success_Closed
          if (kttsBo != null && !"Success".equalsIgnoreCase(kttsBo.getStatus()) && !"Success_Closed".equalsIgnoreCase(kttsBo.getStatus())) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            resultDTO.setId("NOK");
            resultDTO.setMessage("[KTTS]:" + kttsBo.getErrorInfo());
            return resultDTO;
          } else if (kttsBo != null && "Success_Closed".equalsIgnoreCase(kttsBo.getStatus())) {
            // Success_Closed dong wo
            resultDTO.setKey("OK");
            resultDTO.setQuantitySucc(1);
            resultDTO.setApprove(1L);
            resultDTO.setWoId(wo.getWoId());
            return resultDTO;
          } else if (checkUsed) { // neu ko co hang hoa thu hoi thi de ben KTTS thuc hien goi dong luon
            resultDTO.setKey("OK");
            resultDTO.setQuantitySucc(1);
            return resultDTO;
          }
        } else {  // khong thuc hien
          ResultDTO res = aprovePXK(wo.getWoId(), 0L, reasonKtts, 1L);
          if (!RESULT.SUCCESS.equals(res.getKey())) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            resultDTO.setId("NOK");
            resultDTO.setMessage("[KTTS]:" + res.getMessage());
            return resultDTO;
          }
        }
      } else if (checkProperty(mapConfigProperty, String.valueOf(wo.getWoTypeId()),
          Constants.AP_PARAM.WO_TYPE_QLTS_NC_UCTT)
          || checkProperty(mapConfigProperty, String.valueOf(wo.getWoTypeId()),
          Constants.AP_PARAM.WO_TYPE_QLTS_NC_UCTT_THUE)) { // Nang cap uctt
        if (actionKTTS.equals(1L)) { // hoan thanh co thuc hien
          UsersEntity us = null;
          if (wo.getCdAssignId() != null) {
            us = userRepository.getUserByUserId(wo.getCdAssignId());
          }
          String cd = us != null ? us.getStaffCode() : null;
          Boolean checkUsed = true;
          List<MerchandiseOrderBO> lstMer = new ArrayList<>();
          if (lstMerchandise != null) {
            List<WoMerchandiseInsideDTO> lstHaveSerial = new ArrayList<>();
            List<WoMerchandiseInsideDTO> lstNotSerial = new ArrayList<>();
            for (WoMerchandiseInsideDTO t : lstMerchandise) {
              MerchandiseOrderBO o = new MerchandiseOrderBO();
              o.setAmount(t.getQuantity() != null ? t.getQuantity() : null);
              o.setMerCode(t.getMerchandiseCode());
              o.setSerial(t.getSerial());
              o.setLongTermAssetId(
                  t.getLongTermAssetId() != null ? t.getLongTermAssetId() : null);
              lstMer.add(o);
              if (mapMer.containsKey(o.getMerCode().toLowerCase())) {
                if (mapMer.get(o.getMerCode().toLowerCase()).equals(o.getAmount())) {
                  mapMer.remove(o.getMerCode().toLowerCase());
                } else {
                  checkUsed = false;
                }
              } else {
                checkUsed = false;
              }
              // sinh wo tu dong_start
              if (StringUtils.isStringNullOrEmpty(t.getSerial())) {
                lstNotSerial.add(t);
              } else {
                lstHaveSerial.add(t);
              }
              // sinh wo tu dong_end
            }
            if (mapMer != null && mapMer.size() > 0) {
              checkUsed = false;
            }
            // tu dong sinh WO thu hoi
            if (lstHaveSerial != null && lstHaveSerial.size() > 0) {
              ResultDTO resCreate = autoCreateWOKTTS(wo, lstHaveSerial, 1L);
              if (!RESULT.SUCCESS.equals(resCreate.getKey())) {
                resultDTO.setId("NOK");
                resultDTO.setMessage("[KTTS_Create_Auto]" + resCreate.getMessage());
                return resultDTO;
              }
            }
            if (lstNotSerial != null && lstNotSerial.size() > 0) {
              ResultDTO resCreate = autoCreateWOKTTS(wo, lstNotSerial, 2L);
              if (!RESULT.SUCCESS.equals(resCreate.getKey())) {
                resultDTO.setId("NOK");
                resultDTO.setMessage("[KTTS_Create_Auto]" + resCreate.getMessage());
                return resultDTO;
              }
            }
          }
          Kttsbo kttsBo = kttsVsmartPort
              .updateAcceptanceUCTTNation(woIdValue, cd, ft.getStaffCode(), reasonKtts, lstMer,
                  updateForm.getScriptId(), nationCode);
          if (kttsBo != null && !"Success".equalsIgnoreCase(kttsBo.getStatus()) && !"Success_Closed".equalsIgnoreCase(kttsBo.getStatus())) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            resultDTO.setId("NOK");
            resultDTO.setMessage("[KTTS]:" + kttsBo.getErrorInfo());
            return resultDTO;
          } else if ("Success_Closed".equalsIgnoreCase(kttsBo.getStatus())) {
            // Success_Closed dong wo
            resultDTO.setKey("OK");
            resultDTO.setQuantitySucc(1);
            resultDTO.setApprove(1L);
            resultDTO.setWoId(wo.getWoId());
            return resultDTO;
          } else if (checkUsed) { // neu ko co hang hoa thu hoi thi de ben KTTS thuc hien goi dong luon
            resultDTO.setKey("OK");
            resultDTO.setQuantitySucc(1);
            return resultDTO;
          }
        } else {
          ResultDTO res = aprovePXK(wo.getWoId(), 0L, reasonKtts, 1L);
          if (!RESULT.SUCCESS.equals(res.getKey())) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            resultDTO.setId("NOK");
            resultDTO.setMessage("[KTTS]:" + resultDTO.getMessage());
            return resultDTO;
          }
        }
      } // Thu hoi tai san / ha cap
      else if (checkProperty(mapConfigProperty, String.valueOf(wo.getWoTypeId()),
          Constants.AP_PARAM.WO_TYPE_QLTS_THUHOI)
          || checkProperty(mapConfigProperty, String.valueOf(wo.getWoTypeId()),
          Constants.AP_PARAM.WO_TYPE_QLTS_HC)
          || checkProperty(mapConfigProperty, String.valueOf(wo.getWoTypeId()),
          Constants.AP_PARAM.WO_TYPE_QLTS_BAO_HONG)
          || checkProperty(mapConfigProperty, String.valueOf(wo.getWoTypeId()),
          Constants.AP_PARAM.WO_TYPE_QLTS_BAO_KXD)) {
        if ("1".equals(String.valueOf(actionKTTS))) { // co thuc hien
          // thuc hien validate wo thu hoi tuyen cap NIMS_start
          if (checkProperty(mapConfigProperty, String.valueOf(wo.getWoTypeId()),
              Constants.AP_PARAM.WO_TYPE_QLTS_HC)) {
            if (WO_MASTER_CODE.WO_NIMS.equals(wo.getWoSystem())) {
              List<CableInfoForm> lstCable = wsHTNims.getListCableInfo(1L, wo.getCableCode());
              if (lstCable != null && lstCable.size() > 0) {
                resultDTO.setId("NOK");
                resultDTO.setMessage("[NIMS]:" + I18n.getLanguage("wo.nims.err.cableCodeIsExixts"));
                return resultDTO;
              }
            }
          }
          // thuc hien validate wo thu hoi tuyen cap NIMS_and

          CertificateRegistrationBO cer = new CertificateRegistrationBO();
          cer.setCertificateRegistrationCode(wo.getWoCode());
          cer.setCertificateRegistrationId(wo.getWoId());
          cer.setTitle(getTitleForKTTS(wo));
          // yeu cau moi truong nha tram_start
          if (StringUtils.isNotNullOrEmpty(wo.getWoSystemId())) {
            List<TroublesDTO> lstTrouble = ttServiceProxy.getTroubleByCode(wo.getWoSystemId());
            if (lstTrouble != null && lstTrouble.size() > 0) {
              cer.setGnocDescription(lstTrouble.get(0).getDescription());
            }
          }
          // yeu cau moi truong nha tram_end
          cer.setCertificateRegistrationType(Constants.AP_PARAM.WO_TYPE_NAME_HC);
          if (checkProperty(mapConfigProperty, String.valueOf(wo.getWoTypeId()),
              Constants.AP_PARAM.WO_TYPE_QLTS_THUHOI)) {
            cer.setCertificateRegistrationType(Constants.AP_PARAM.WO_TYPE_NAME_THUHOI);
          } else if (checkProperty(mapConfigProperty, String.valueOf(wo.getWoTypeId()),
              Constants.AP_PARAM.WO_TYPE_QLTS_BAO_HONG)) {
            cer.setCertificateRegistrationType(Constants.AP_PARAM.WO_TYPE_NAME_BAO_HONG);
          } else if (checkProperty(mapConfigProperty, String.valueOf(wo.getWoTypeId()),
              Constants.AP_PARAM.WO_TYPE_QLTS_BAO_KXD)) {
            cer.setCertificateRegistrationType(Constants.AP_PARAM.WO_TYPE_NAME_BAO_KXD);
          }

          List<MerEntityBO> lstEntity = new ArrayList<>();
          if (lstMerchandise != null) {
            for (WoMerchandiseInsideDTO i : lstMerchandise) {
              MerEntityBO bo = new MerEntityBO();
              bo.setMerCode(i.getMerchandiseCode());
              bo.setCount(i.getQuantity() != null ? i.getQuantity() : null);
              bo.setSerialNumber(i.getSerial());
              bo.setConditionalId(
                  StringUtils.isNotNullOrEmpty(updateForm.getStationEnvironment()) ? Long
                      .valueOf(updateForm.getStationEnvironment()) : null);
              lstEntity.add(bo);
            }
          }
          // thangdt update ham
          cer.setExecutor(ft.getUsername());
          Kttsbo result = kttsVsmartPort
              .createImpReqNation(wo.getWarehouseCode(), wo.getStationCode(), ft.getStaffCode(),
                  lstEntity, cer, nationCode);
          if (!"Success".equalsIgnoreCase(result.getStatus())) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            String err = "";
            if (result.getListError() != null) {
              for (ErrorBO bo : result.getListError()) {
                err = err + bo.getMerCode() + ":" + bo.getDescription() + ";";
              }
            }
            resultDTO.setId("NOK");
            resultDTO
                .setMessage("[KTTS]:" + ("".equals(err) == false ? err : result.getErrorInfo()));
            return resultDTO;
          }

        } else {
          wo.setFinishTime(new Date());
          wo.setResult(Long.valueOf(Constants.WO_RESULT.OK));
          wo.setStatus(Long.valueOf(Constants.WO_STATUS.CLOSED_CD));
          updateWoAndHistory(wo, ft, reasonKtts, Constants.WO_STATUS.CLOSED_CD, new Date());
        }
      } // dieu chuyen nang cap
      else if (checkProperty(mapConfigProperty, String.valueOf(wo.getWoTypeId()),
          Constants.AP_PARAM.WO_TYPE_QLTS_DC_NC)) {
        if (actionKTTS.equals(1L)) {
          Kttsbo result = kttsVsmartPort
              .confirmAssetMoveRecvNation(wo.getWoId(), ft.getStaffCode(), nationCode);
          if (!"Success".equalsIgnoreCase(result.getStatus()) && !"Success_Closed".equalsIgnoreCase(result.getStatus())) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            resultDTO.setId("NOK");
            resultDTO.setMessage("[KTTS]:" + result.getErrorInfo());
            return resultDTO;
          } else if("Success_Closed".equalsIgnoreCase(result.getStatus())) { // hàm trả về success_closed thì gọi đóng wo
            resultDTO.setApprove(1L);
            resultDTO.setKey("OK");
            resultDTO.setQuantitySucc(1);
            resultDTO.setWoId(wo.getWoId());
            return resultDTO;
          }
          else {
            resultDTO.setKey("OK");
            resultDTO.setQuantitySucc(1);
            return resultDTO;  // ko chuyen qua hoan thanh ma ma KTTS tu dong dong
          }
        } else {
          List<WoInsideDTO> lstWo = woRepository
              .getListWoBySystemOtherCode(wo.getWoSystemId(), null);
          WoInsideDTO woDown = null;
          if (lstWo != null && lstWo.size() > 0) {
            for (WoInsideDTO o : lstWo) {
              if (checkProperty(mapConfigProperty, String.valueOf(o.getWoTypeId()),
                  Constants.AP_PARAM.WO_TYPE_QLTS_DC_HC)) {
                woDown = o;
                break;
              }
            }
          }
          if (woDown == null) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            resultDTO.setId("NOK");
            resultDTO.setMessage(I18n.getLanguage("wo.notExixtsWoDowngrade"));
            return resultDTO;
          }
          Kttsbo result = kttsVsmartPort
              .rejectAssetMoveConfirmNation(woDown.getWoId(), 2L, ft.getStaffCode(),
                  nationCode); // tu choi nhan
          if (!"Success".equalsIgnoreCase(result.getStatus())) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            resultDTO.setId("NOK");
            resultDTO.setMessage("[KTTS]:" + result.getErrorInfo());
            return resultDTO;
          } else {
            wo.setFinishTime(new Date());
            wo.setResult(Long.valueOf(Constants.WO_RESULT.OK));
            wo.setStatus(Long.valueOf(Constants.WO_STATUS.CLOSED_CD));
            updateWoAndHistory(wo, ft, reasonKtts, Constants.WO_STATUS.CLOSED_CD, new Date());
            // thangdt khi tu choi dieu chuyen thi dong ha cap va nang cap
            if(woDown != null && !woDown.getStatus().equals(Long.valueOf(Constants.WO_STATUS.CLOSED_CD))){
              woDown.setFinishTime(new Date());
              woDown.setResult(Long.valueOf(Constants.WO_RESULT.OK));
              woDown.setStatus(Long.valueOf(Constants.WO_STATUS.CLOSED_CD));
              updateWoAndHistory(woDown, ft, "Auto WO close from KTTS", Constants.WO_STATUS.CLOSED_CD, new Date());
            }

          }
        }
      } // dieu chuyen ha cap
      else if (checkProperty(mapConfigProperty, String.valueOf(wo.getWoTypeId()),
          Constants.AP_PARAM.WO_TYPE_QLTS_DC_HC)) {
        //thang dt lay id dieu chuyen nang cap
        List<WoInsideDTO> lstWo = woRepository
            .getListWoBySystemOtherCode(wo.getWoSystemId(), null);
        WoInsideDTO woUp = null;
        if (lstWo != null && lstWo.size() > 0) {
          for (WoInsideDTO o : lstWo) {
            if (checkProperty(mapConfigProperty, String.valueOf(o.getWoTypeId()),
                Constants.AP_PARAM.WO_TYPE_QLTS_DC_NC)) {
              woUp = o;
              break;
            }
          }
        }
        if (actionKTTS.equals(1L)) {
          List<EntityBO> lst = new ArrayList<>();
          if (lstMerchandise != null) {
            for (WoMerchandiseInsideDTO i : lstMerchandise) {
              EntityBO bo = new EntityBO();
              bo.setMerCode(i.getMerchandiseCode());
              bo.setCount(i.getQuantity() != null ? Double.valueOf(i.getQuantity()) : null);
              bo.setSerialNumber(i.getSerial());
              lst.add(bo);
            }
          }

          Kttsbo result = kttsVsmartPort
              .confirmAssetMoveDeliveryNation(wo.getWoId(), lst, ft.getStaffCode(),
                  I18n.getLanguage("wo.Staff"), nationCode);
          if (!"Success".equalsIgnoreCase(result.getStatus())) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            String err = "";
            if (result.getListError() != null) {
              for (ErrorBO bo : result.getListError()) {
                err = err + bo.getMerCode() + ":" + bo.getDescription() + ";";
              }
            }
            resultDTO.setId("NOK");
            resultDTO
                .setMessage("[KTTS]:" + ("".equals(err) == false ? err : result.getErrorInfo()));
            return resultDTO;
          } else {
            resultDTO.setKey("OK");
            resultDTO.setQuantitySucc(1);
            return resultDTO;  // ko chuyen qua hoan thanh ma ma KTTS tu dong dong
          }
        } else {
          Kttsbo result = kttsVsmartPort
              .rejectAssetMoveConfirmNation(wo.getWoId(), 1L, ft.getStaffCode(),
                  nationCode); // tu choi chuyen
          if (!"Success".equalsIgnoreCase(result.getStatus())) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            resultDTO.setId("NOK");
            resultDTO.setMessage("[KTTS]:" + result.getErrorInfo());
            return resultDTO;
          } else {
            wo.setFinishTime(new Date());
            wo.setResult(Long.valueOf(Constants.WO_RESULT.OK));
            wo.setStatus(Long.valueOf(Constants.WO_STATUS.CLOSED_CD));
            updateWoAndHistory(wo, ft, reasonKtts, Constants.WO_STATUS.CLOSED_CD, new Date());
            // thangdt khi tu choi dieu chuyen thi dong ha cap va nang cap
            if(woUp != null && !woUp.getStatus().equals(Long.valueOf(Constants.WO_STATUS.CLOSED_CD))){
              woUp.setFinishTime(new Date());
              woUp.setResult(Long.valueOf(Constants.WO_RESULT.OK));
              woUp.setStatus(Long.valueOf(Constants.WO_STATUS.CLOSED_CD));
              updateWoAndHistory(woUp, ft, "Auto WO close from KTTS", Constants.WO_STATUS.CLOSED_CD, new Date());
            }
            resultDTO.setKey("OK");
            resultDTO.setQuantitySucc(1);
            return resultDTO;
          }
        }
      }
      resultDTO.setKey("OK");
      resultDTO.setQuantitySucc(1);
      return resultDTO;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
      resultDTO.setId("NOK");
      resultDTO
          .setMessage(I18n.getLanguage("wo.haveSomeErrorWhenCallKTTS") + ": " + e.getMessage());
      return resultDTO;
    }
  }

  public ResultDTO autoCreateWOKTTS(WoInsideDTO wo, List<WoMerchandiseInsideDTO> lstMerchandise,
      Long type) {
    ResultDTO resultDTO = new ResultDTO();
    //type 1 co serial  ---  2 ko co serial
    try {
      WoDTO o = new WoDTO();
      if (type.equals(1L)) {
        o.setWoTypeId(
            commonRepository.getConfigPropertyValue(Constants.AP_PARAM.WO_TYPE_QLTS_THUHOI));
        o.setWoContent(
            I18n.getLanguage("wo.hcth.auto.content") + ": " + wo.getStationCode() + " " + I18n
                .getLanguage("wo.forWoUctt") + ": " + wo.getWoCode());
        o.setPriorityId(
            commonRepository.getConfigPropertyValue(Constants.AP_PARAM.PRIORITY_QLTS_THUHOI));
      } else {
        o.setWoTypeId(commonRepository.getConfigPropertyValue(Constants.AP_PARAM.WO_TYPE_QLTS_HC));
        o.setWoContent(
            I18n.getLanguage("wo.Hc.auto.content") + ": " + wo.getStationCode() + " " + I18n
                .getLanguage("wo.forWoUctt") + ": " + wo.getWoCode());
        o.setPriorityId(
            commonRepository.getConfigPropertyValue(Constants.AP_PARAM.PRIORITY_QLTS_HC));
      }
      List<TroublesDTO> lstTrouble = ttServiceProxy.getTroubleByCode(wo.getWoSystemId());
      if (lstTrouble != null && lstTrouble.size() > 0) {
        o.setWoDescription(lstTrouble.get(0).getDescription());
      } else {
        o.setWoDescription(o.getWoContent());
      }
      o.setWoSystem(wo.getWoSystem());
      o.setWoSystemId(wo.getWoSystemId() != null ? wo.getWoSystemId() : wo.getWoCode());
      Date now = new Date();
      o.setCreateDate(DateTimeUtils.convertDateToString(now, Constants.ddMMyyyyHHmmss));
      o.setStartTime(DateTimeUtils.convertDateToString(now, Constants.ddMMyyyyHHmmss));
      now.setDate(now.getDate() + 1);
      o.setEndTime(DateTimeUtils.convertDateToString(now, Constants.ddMMyyyyHHmmss));
      o.setStationCode(wo.getStationCode());
      o.setCdAssignId(wo.getCdAssignId() != null ? String.valueOf(wo.getCdAssignId()) : null);
      o.setCreatePersonId(String.valueOf(wo.getCreatePersonId()));
      o.setLineCode(wo.getLineCode());
      o.setCdId(String.valueOf(wo.getCdId()));
      UsersEntity ft = null;
      if (wo.getFtId() != null) {
        ft = userRepository.getUserByUserId(wo.getFtId());
      }
      o.setFtId(ft != null ? ft.getUsername() : null);
      ResultInSideDto res = createWoCommon(o);
      if (RESULT.SUCCESS.equals(res.getKey())) { // thuc hien isert danh sach tai san
        String woId = res.getIdValue()
            .substring(res.getIdValue().lastIndexOf("_") + 1, res.getIdValue().length());
        for (WoMerchandiseInsideDTO dto : lstMerchandise) {
          dto.setWoId(Long.valueOf(woId));
          dto.setId(null);
          woMerchandiseRepository.insertWoMerchandise(dto);
        }
      }
      resultDTO.setId(res.getIdValue());
      resultDTO.setKey(res.getKey());
      resultDTO.setMessage(res.getMessage());
      return resultDTO;

    } catch (Exception e) {
      log.error(e.getMessage(), e);
      resultDTO.setId(RESULT.FAIL);
      resultDTO.setKey(RESULT.FAIL);
      resultDTO.setMessage(e.getMessage());
      return resultDTO;
    }
  }

  public ResultInSideDto autoCreateWOThuHoiNims(WoInsideDTO wo) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    try {
      WoDTO o = new WoDTO();
      o.setWoTypeId(commonRepository.getConfigPropertyValue(Constants.AP_PARAM.WO_TYPE_QLTS_HC));
      o.setWoContent(
          I18n.getLanguage("wo.th.nims.auto.content") + ": " + wo.getCableCode() + " " + I18n
              .getLanguage("wo.distance") + ":" + wo.getDistance()
              + " " + I18n.getLanguage("wo.constructionCode") + ": " + (
              wo.getConstructionCode() != null ? wo.getConstructionCode() : "")
      );
      o.setPriorityId(commonRepository.getConfigPropertyValue(Constants.AP_PARAM.PRIORITY_QLTS_HC));
      o.setWoDescription(o.getWoContent());
      o.setWoSystem("NIMS");
      o.setDistance(wo.getDistance());
      o.setCableCode(wo.getCableCode());
      o.setCableTypeCode(wo.getCableTypeCode());
      Date now = new Date();
      o.setCreateDate(DateTimeUtils.convertDateToString(now, Constants.ddMMyyyyHHmmss));
      o.setStartTime(DateTimeUtils.convertDateToString(now, Constants.ddMMyyyyHHmmss));
      now.setDate(now.getDate() + 3);
      o.setEndTime(DateTimeUtils.convertDateToString(now, Constants.ddMMyyyyHHmmss));
      o.setStationCode(wo.getStationCode());
      o.setConstructionCode(wo.getConstructionCode());
      o.setCdAssignId(wo.getCdAssignId() != null ? String.valueOf(wo.getCdAssignId()) : null);
      o.setCreatePersonId(String.valueOf(wo.getCreatePersonId()));
      o.setLineCode(wo.getLineCode());
      o.setCdId(String.valueOf(wo.getCdId()));
      UsersInsideDto ft = userRepository.getUserByUserId(wo.getFtId()).toDTO();
      o.setFtId(ft != null ? ft.getUsername() : null);
      resultInSideDto = createWoCommon(o);
      return resultInSideDto;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      resultInSideDto.setKey(RESULT.FAIL);
      resultInSideDto.setMessage(e.getMessage());
      return resultInSideDto;
    }
  }

  public ResultInSideDto updateKeyValueExcel(List<ObjKeyValue> getLstDataKeyValue, WoInsideDTO wo,
      UsersInsideDto ft) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    try {
      // cap nhat file excel
      String fileNameLst = wo.getFileName();
      if (fileNameLst != null) {
        String[] f = fileNameLst.split(",");
        String fileName = f[0].trim();
        String fileNameFull =
            ftpFolder + "/" + FileUtils.createPathFtpByDate(wo.getCreateDate())
                + "/" + fileName;
        byte[] bytes = FileUtils.getFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
            PassTranformer.decrypt(ftpPass), fileNameFull);
        String pathTemp = FileUtils.saveTempFile(fileName, bytes, tempFolder);
        ExcelWriterUtils ewu = new ExcelWriterUtils();
        Workbook workBook = ewu.readFileExcel(pathTemp);
        Sheet sheetOne = workBook.getSheetAt(0);
        for (ObjKeyValue obj : getLstDataKeyValue) {
          int c = obj.getCol();
          int r = obj.getRow();
          ewu.createCell(sheetOne, c, r, obj.getValue());
        }
        FileOutputStream fileOut = new FileOutputStream(pathTemp);
        workBook.write(fileOut);
        fileOut.close();
        byte[] bytesOut = FileUtils.convertFileToByte(new File(pathTemp));
        FileUtils.saveFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
            PassTranformer.decrypt(ftpPass), ftpFolder + "/"
                + FileUtils.createPathFtpByDate(wo.getCreateDate()), fileName, bytesOut);
        // thuc hien cap nhat file len WO cha
        try {
          if (wo.getParentId() != null) {
            WoInsideDTO parent = woRepository.findWoByIdNoOffset(wo.getParentId());
            FileUtils.saveFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
                PassTranformer.decrypt(ftpPass), ftpFolder + "/"
                    + FileUtils.createPathFtpByDate(parent.getCreateDate()), fileName, bytesOut);
            GnocFileDto gnocFileDto = new GnocFileDto();
            gnocFileDto.setPath(fileNameFull);
            gnocFileDto.setFileName(FileUtils.getFileName(fileNameFull));
            gnocFileDto.setCreateUserId(ft.getUserId());
            gnocFileDto.setCreateUserName(ft.getUsername());
            gnocFileDto.setCreateUnitId(ft.getUnitId());
            gnocFileDto.setCreateUnitName(ft.getUnitName());
            gnocFileDto.setCreateTime(wo.getCreateDate());
            gnocFileDto.setMappingId(parent.getWoId());
            List<GnocFileDto> gnocFileDtos = new ArrayList<>();
            gnocFileDtos.add(gnocFileDto);
            gnocFileRepository.saveListGnocFileNotDeleteAll(GNOC_FILE_BUSSINESS.WO,
                parent.getWoId(), gnocFileDtos);
            String parentFileName = parent.getFileName();
            if (StringUtils.isNotNullOrEmpty(parentFileName)) {
              parent.setFileName(parentFileName + ", " + fileName);
            } else {
              parent.setFileName(fileName);
            }
            parent.setSyncStatus(null);
            woRepository.updateWo(parent);
          }
        } catch (Exception e) {
          log.error(e.getMessage(), e);
          resultInSideDto.setKey(RESULT.FAIL);
          resultInSideDto.setMessage("Co loi khi copy file len WO Cha");
          return resultInSideDto;
        }
      }
      resultInSideDto.setKey(RESULT.SUCCESS);
      resultInSideDto.setMessage(RESULT.SUCCESS);
      return resultInSideDto;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      resultInSideDto.setKey(RESULT.FAIL);
      resultInSideDto.setMessage(e.getMessage());
      return resultInSideDto;
    }
  }

  @Override
  public ResultDTO updateMopInfo(String woCode, String result, String mopId, Long type) {
    try {
      WoInsideDTO wo = woRepository.getWoByWoCode(woCode);
      if (wo == null) {
        return new ResultDTO("FAIL", "FAIL", I18n.getLanguage("woIsNotExists"));
      }
      if (StringUtils.isStringNullOrEmpty(woCode)) {
        return new ResultDTO("FAIL", "FAIL", I18n.getLanguage("wo.woCodeIsNotNull"));
      }
      if (StringUtils.isStringNullOrEmpty(mopId)) {
        return new ResultDTO("FAIL", "FAIL", I18n.getLanguage("wo.mopIdIsNotNull"));
      }
      if (StringUtils.isLongNullOrEmpty(type)) {
        return new ResultDTO("FAIL", "FAIL", I18n.getLanguage("wo.typeUpdateMopIsNotNull"));
      }
      if (type.equals(2L)) {
        if (StringUtils.isStringNullOrEmpty(result)) {
          return new ResultDTO("FAIL", "FAIL", I18n.getLanguage("wo.resultIsNotNull"));
        }
      }
      if (!type.equals(1L) && !type.equals(2L) && !type.equals(3L)) {
        return new ResultDTO("FAIL", "FAIL", I18n.getLanguage("wo.typeInvalid"));
      }
      // thuc hien luu thong tin chay mop
      WoMopInfoDTO dtoSearch = new WoMopInfoDTO();
      dtoSearch.setWoId(wo.getWoId());
      dtoSearch.setMopId(
          (!DataUtil.isNullOrEmpty(mopId) && DataUtil.isNumber(mopId)) ? Long.valueOf(mopId)
              : null);
      WoMopInfoDTO o = new WoMopInfoDTO();
      o.setWoId(wo.getWoId());
      o.setMopId((!DataUtil.isNullOrEmpty(mopId) && DataUtil.isNumber(mopId)) ? Long.valueOf(mopId)
          : null);
      if (!StringUtils.isStringNullOrEmpty(result)) {
        o.setRunTime(new Date());
        o.setResult(DateTimeUtils.convertDateToString(new Date(), Constants.ddMMyyyyHHmmss) + ":"
            + result);
      }
      if (type.equals(1L)) { // goi lan dau
        woMopInfoRepository.saveOrUpdate(o);
      } else if (type.equals(2L) || type.equals(3L)) {
        List<WoMopInfoEntity> lst = woMopInfoRepository.getWoMopInfoByWoIdAndMopId(dtoSearch);
        if (lst == null || lst.size() == 0) {
          return new ResultDTO("FAIL", "FAIL", I18n.getLanguage("wo.runMopFirst"));
        }
        WoMopInfoEntity tmp = lst.get(0);
        if (type.equals(3L)) {
          tmp.setResponseTime(new Date());
        }
        if (tmp.getResult() != null && tmp.getResult().length() > 1500) {
          tmp.setResult(tmp.getResult().substring(1000));
        }
        tmp.setResult((tmp.getResult() != null ? tmp.getResult() + "\r\n" : "") + DateTimeUtils
            .convertDateToString(new Date(), Constants.ddMMyyyyHHmmss) + ":" + result + "\r\n");
        woMopInfoRepository.saveOrUpdate(tmp.toDto());
        UsersEntity us = commonRepository.getUserByUserId(wo.getFtId());
        WoWorklogInsideDTO worklog = new WoWorklogInsideDTO();
        worklog.setWoId(o.getWoId());
        worklog.setUpdateTime(new Date());
        worklog.setUserId(us != null ? us.getUserId() : null);
        worklog.setUsername(us != null ? us.getUsername() : null);
        worklog.setWoSystem("WFM");
        worklog.setWoWorklogContent(result);
        ResultInSideDto resWorklog = woWorklogRepository.insertWoWorklog(worklog);
        if (!RESULT.SUCCESS.equals(resWorklog.getKey())) {
          return new ResultDTO("FAIL", "FAIL", I18n.getLanguage("FailCreateWOWorklog"));
        }
      }
      // thuc hien luu worklog
      return new ResultDTO("SUCCESS", "SUCCESS", "SUCCESS");
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return new ResultDTO("FAIL", "FAIL", e.getMessage());
    }
  }

  @Override
  public List<ResultDTO> createListWoVsmart(List<WoDTO> createWoDto) {
    List<ResultDTO> lst = new ArrayList<ResultDTO>();
    for (WoDTO o : createWoDto) {
      try {
        ResultDTO res = createWoVsmart(o);
        if (RESULT.SUCCESS.equals(res.getMessage())) {
          res.setMessage(o.getWoSystemId());
          res.setKey(RESULT.SUCCESS);
        } else {
          res.setId(o.getWoSystemId());
          res.setKey(RESULT.FAIL);
        }
        lst.add(res);
      } catch (Exception e) {
        ResultDTO res = new ResultDTO();
        res.setId(o.getWoSystemId());
        res.setKey(RESULT.FAIL);
        res.setMessage(e.getMessage());
        lst.add(res);
        log.info(e.getMessage(), e);
      }
    }
    return lst;
  }


  @Override
  public ResultDTO insertWoKTTS(WoDTO woDTO) {
    ResultDTO result = new ResultDTO();
    try {
      String woTypeCode = woDTO.getWoTypeId();
      // lay Id loai cong viec dua vao ma cong viec
      WoTypeInsideDTO dto = new WoTypeInsideDTO();
      dto.setWoTypeCode(woTypeCode);
      UsersEntity us = userRepository.getUserByUserName(woDTO.getFtId());
      if (us != null) {
        List<WoTypeInsideDTO> lst = woCategoryServiceProxy.getListWoTypeByLocaleNotLike(dto);
        if (lst != null && lst.size() > 0) {
          woDTO.setWoTypeId(String.valueOf(lst.get(0).getWoTypeId()));
          if (StringUtils.isStringNullOrEmpty(woDTO.getWoSourceCode())) { // luong tao ktts cu
            WoCdGroupInsideDTO woCdGroup = woRepository.getCdByFT(us.getUserId(),
                !DataUtil.isNullOrEmpty(woDTO.getWoTypeId()) ? Long.valueOf(woDTO.getWoTypeId())
                    : null, "4");
            if (woCdGroup != null) {
              woDTO.setFtId(null);
              woDTO.setCdId(String.valueOf(woCdGroup.getWoGroupId()));
              String woId = woRepository.getSeqTableWo(WO_SEQ);
              woDTO.setWoId(woId);
              ResultInSideDto res = insertWoCommon(woDTO);
              result = res.toResultDTO();
              result.setId(res.getIdValue());
            } else {
              result.setKey(RESULT.FAIL);
              result.setMessage(I18n.getLanguage("wo.cdGroup.not.exist"));
              return result;
            }
          } else { // luong tao ktts moi
            // set them thong tin tu suy
            woDTO = prepareDataWoKTTS(woDTO);
            String woId = woRepository.getSeqTableWo(WO_SEQ);
            woDTO.setWoId(woId);
            ResultInSideDto res = insertWoCommon(woDTO);
            result = res.toResultDTO();
            result.setId(res.getIdValue());
          }
        } else {
          result.setKey(RESULT.FAIL);
          result.setMessage(I18n.getLanguage("wo.woTypeDoesNotExixts"));
        }
      } else {
        result.setKey(RESULT.FAIL);
        result.setMessage(I18n.getLanguage("wo.updateWoForCRErrUser"));
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      result.setKey(RESULT.FAIL);
      result.setMessage(e.getMessage());
    }
    return result;
  }

  public WoDTO prepareDataWoKTTS(WoDTO woDTO) {
    try {
      WoInsideDTO woSource = woRepository.getWoByWoCode(woDTO.getWoSourceCode());
      Date now = new Date();
      SimpleDateFormat dfm = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
      //muc uu tien
      woDTO.setPriorityId(
          commonRepository.getConfigPropertyValue(Constants.AP_PARAM.WO_PRIORITY_MINOR));
      woDTO.setCdId(String.valueOf(woSource.getCdId()));
      //ft
      woDTO.setFtId(null);
      woDTO.setStartTime(dfm.format(now));
      woDTO.setCreateDate(dfm.format(now));
      String processDate = commonRepository.getConfigPropertyValue("PROCESS.TIME.WO.KTTS");
      Long pD;
      try {
        pD = Long.valueOf(processDate);
      } catch (Exception e) {
        log.error(e.getMessage(), e);
        pD = 7L;
      }
      now.setDate(now.getDate() + pD.intValue());
      woDTO.setEndTime(dfm.format(now));
      return woDTO;
    } catch (Exception e) {
      throw e;
    }
  }

  @Override
  public KpiCompleteVsmartResult getKpiComplete(String startTime, String endTime,
      List<String> lstUser) {
    KpiCompleteVsmartResult result = new KpiCompleteVsmartResult();
    try {
      SimpleDateFormat dfm = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
      Date start = dfm.parse(startTime);
      Date end = dfm.parse(endTime);
      List<KpiCompleteVsamrtForm> lstTmp = woRepository.getListKpiComplete(lstUser, start, end);
      List<KpiCompleteVsamrtForm> lstData = new ArrayList<KpiCompleteVsamrtForm>();
      Map<String, KpiCompleteVsamrtForm> map = new HashMap<String, KpiCompleteVsamrtForm>();
      Long numVsmart = 0L;
      Long numWeb = 0L;

      for (KpiCompleteVsamrtForm u : lstTmp) {
        if (map.containsKey(u.getUserName())) {
          KpiCompleteVsamrtForm tmp = map.get(u.getUserName());
          if (u.getNumUpdateOnVsamrt() != null && u.getNumUpdateOnVsamrt().equals(1L)) {
            tmp.setNumUpdateOnVsamrt(u.getNumUpdateOnWeb());
          } else {
            tmp.setNumUpdateOnWeb(
                u.getNumUpdateOnWeb() + (tmp.getNumUpdateOnWeb() != null ? tmp.getNumUpdateOnWeb()
                    : 0L));
          }
          map.put(u.getUserName(), tmp);
        } else {
          KpiCompleteVsamrtForm tmp = new KpiCompleteVsamrtForm();
          tmp.setUserName(u.getUserName());
          if (u.getNumUpdateOnVsamrt() != null && u.getNumUpdateOnVsamrt().equals(1L)) {
            tmp.setNumUpdateOnVsamrt(u.getNumUpdateOnWeb());
          } else {
            tmp.setNumUpdateOnWeb(u.getNumUpdateOnWeb());
          }
          map.put(u.getUserName(), tmp);
        }
      }
      for (Map.Entry<String, KpiCompleteVsamrtForm> entry : map.entrySet()) {
        KpiCompleteVsamrtForm i = entry.getValue();
        numVsmart = numVsmart + (i.getNumUpdateOnVsamrt() != null ? i.getNumUpdateOnVsamrt() : 0L);
        numWeb = numWeb + (i.getNumUpdateOnWeb() != null ? i.getNumUpdateOnWeb() : 0L);
        lstData.add(i);
      }
      result.setTotalOnVsmart(numVsmart);
      result.setTotalOnWeb(numWeb);
      result.setLstData(lstData);
      result.setKey("SUCCESS");
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      result.setKey("FAIL");
      result.setMessage(e.getMessage());
    }
    return result;
  }

  @Override
  public List<WoChecklistDTO> getListWoChecklistDetailByWoId(String woId) {
    return woRepository.getListWoChecklistDetailByWoId(woId);
  }

  @Override
  public WoInsideDTO findWoByIdNoOffset(Long woId) {
    return woRepository.findWoByIdNoOffset(woId);
  }

  @Override
  public ResultInSideDto updateTableWo(WoInsideDTO woInsideDTO) {
    return woRepository.updateWo(woInsideDTO);
  }

  @Override
  public ResultDTO pendingWoForVsmart(VsmartUpdateForm updateForm, String woCode,
      String endPendingTime, String user, String system, String reasonName, String reasonId,
      String customer, String phone) throws Exception {
    ResultDTO resultDTO = new ResultDTO();
    Date endPending = null;
    try {
      endPending = DateTimeUtils.convertStringToDateTime(endPendingTime);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    ResultInSideDto res = pendingWoCommon(updateForm, woCode, endPending, user, system, reasonName,
        reasonId, customer, phone, true, true);
    resultDTO.setKey(RESULT.SUCCESS.equals(res.getKey()) ? RESULT.SUCCESS : RESULT.FAIL);
    resultDTO.setId(res.getIdValue());
    resultDTO.setMessage(res.getMessage());
    return resultDTO;
  }

  @Override
  public WoTypeServiceInsideDTO getIsCheckQrCode(Long woId) {
    WoTypeServiceInsideDTO typeSer = null;
    try {
      if (StringUtils.isLongNullOrEmpty(woId)) {
        return null;
      }
      WoInsideDTO wo = woRepository.findWoByIdNoOffset(woId);
      if (wo == null) {
        return null;
      }
      WoDetailDTO woDetail = woDetailRepository.findWoDetailById(woId);
      if (woDetail == null) {
        return null;
      }
      if (StringUtils.isLongNullOrEmpty(wo.getWoTypeId())
          || StringUtils.isLongNullOrEmpty(woDetail.getServiceId())) {
        return null;
      }
      typeSer = woRepository.getTypeService(wo.getWoTypeId(), woDetail.getServiceId());
      String productCode = woRepository.getProDuctCodeNotCheckQrCode();
      List<String> list = null;
      if (!StringUtils.isStringNullOrEmpty(productCode)) {
        list = new ArrayList<>(Arrays.asList(productCode.split("\\s*,\\s*")));
      }
      if (typeSer != null && typeSer.getIsCheckQrCode() != null
          && typeSer.getIsCheckQrCode() == 1L) {
        if (!StringUtils.isStringNullOrEmpty(woDetail.getProductCode())
            && list != null
            && !list.isEmpty()
            && list.contains(woDetail.getProductCode())) {
          typeSer.setIsCheckQrCode(null);
        } else {
          typeSer.setIsCheckQrCode(1L);
        }
      } else if (null != typeSer) {
        typeSer.setIsCheckQrCode(null);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return null;
    }
    return typeSer;
  }

  @Override
  public Integer getCountListFtByUser(String userName, String keyword) {
    UsersEntity u = userRepository.getUserByUserName(userName);
    if (u != null && !StringUtils.isStringNullOrEmpty(u.getUsername())) {
      List<UsersInsideDto> lst = woCdGroupRepository
          .getListFtByUser(String.valueOf(u.getUserId()), keyword, 0, 0);
      if (lst != null && !lst.isEmpty()) {
        return lst.size();
      }
    }
    return null;
  }

  @Override
  public ResultDTO actionUpdateIsSupportWO(VsmartUpdateForm updateForm, String woIdStr,
      String needSupport, String userName, String content) {
    ResultDTO resultDTO = new ResultDTO();
    try {
      Long woId = DataUtil.isNumber(woIdStr) ? Long.valueOf(woIdStr) : null;
      Date now = new Date();
      if (woId == null) {
        throw new Exception(I18n.getLanguage("wo.id.invalid.vsmart"));
      }
      if (needSupport == null || (!"1".equals(needSupport.trim())
          && !"0".equals(needSupport.trim()))) {
        throw new Exception(I18n.getLanguage("wo.update.needSupport.error.commandFlag"));
      }
      WoInsideDTO wo = woRepository.findWoByIdNoOffset(woId);
      if (wo == null) {
        throw new Exception(I18n.getLanguage("wo.is.not.exist.vsmart"));
      }
      List<Long> listAcceptedStatus = new ArrayList<Long>();
      listAcceptedStatus.add(Long.valueOf(Constants.WO_STATUS.ACCEPT));
      listAcceptedStatus.add(Long.valueOf(Constants.WO_STATUS.INPROCESS));
      listAcceptedStatus.add(Long.valueOf(Constants.WO_STATUS.PENDING));
      if (wo.getStatus() != null && !listAcceptedStatus.contains(wo.getStatus())) {
        throw new Exception(I18n.getLanguage("wo.update.needSupport.error.status"));
      }
      // thanhLV12_them worklog_start
      UsersEntity us = userRepository.getUserByUserId(wo.getFtId());

      if (us == null) {
        throw new Exception(I18n.getLanguage("wo.InvalidUserName") + userName);
      }
      UnitDTO un = unitRepository.findUnitById(us.getUnitId());

      WoWorklogInsideDTO worklog = new WoWorklogInsideDTO();

      worklog.setWoId(woId);
      worklog.setUpdateTime(now);
      worklog.setUserId(us.getUserId());
      worklog.setUsername(userName);
      worklog.setWoSystem("VSMART");
      worklog.setWoWorklogContent(content);

      ResultInSideDto resWorklog = woWorklogRepository.insertWoWorklog(worklog);
      if (!RESULT.SUCCESS.equals(resWorklog.getMessage())) {
        throw new Exception(I18n.getLanguage("wo.FailCreateWOWorklog"));
      }
      // thuc hien luu file dinh kem
      if (updateForm.getListFileName() != null && updateForm.getListFileName().size() > 0) {
        updateFile(wo, wo.getFtId(), updateForm.getListFileName(), updateForm.getFileArr());
      }
      // thuc hien luu lai danh sach case test va file test
      TroublesDTO tDto = new TroublesDTO();
      // set them thong tin loi hang loat va ma tram
      tDto.setErrorCode(updateForm.getIsMultipleErr());
      tDto.setConcave(wo.getStationCode());
      if (updateForm != null && updateForm.getLstSupportCase() != null
          && updateForm.getLstSupportCase().size() > 0) {
        UnitDTO unitToken = unitRepository.findUnitById(us.getUnitId());
        List<SupportCaseForm> lstSupport = updateForm.getLstSupportCase();
        List<byte[]> lstArr = new ArrayList<>();
        List<String> lstFileName = new ArrayList<>();
        for (SupportCaseForm f : lstSupport) {
          List<SupportCaseTestForm> lstSupportTest = f.getLstSuportCaseTest();
          if (lstSupportTest != null && lstSupportTest.size() > 0) {
            for (SupportCaseTestForm i : lstSupportTest) {
              List<GnocFileDto> gnocFileDtos = new ArrayList<>();
              WoSupportDTO o = new WoSupportDTO();
              o.setWoID(wo.getWoId());
              o.setCfgSupportCaseID(DataUtil.isNumber(i.getCfgSuppportCaseId()) ? Long
                  .valueOf(i.getCfgSuppportCaseId()) : null);
              o.setCfgSupportCaseTestID(
                  DataUtil.isNumber(i.getId()) ? Long.valueOf(i.getId()) : null);
              o.setDescription(i.getDescription());
              o.setResult(DataUtil.isNumber(i.getResult()) ? Long.valueOf(i.getResult()) : null);
              String fullPath = null;
              if (!StringUtils.isStringNullOrEmpty(i.getFileName())) {
                fullPath = FileUtils
                    .saveFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
                        PassTranformer.decrypt(ftpPass), ftpFolder, i.getFileName(), i.getFileArr(),
                        wo.getCreateDate());
                o.setFileName(FileUtils.getFileName(fullPath));
                lstArr.add(i.getFileArr());
                lstFileName.add(i.getFileName());
              }
              o.setUpdateTime(now);
              ResultInSideDto resultInSideDto = woSupportRepository.insertWoSupport(o);
              GnocFileDto gnocFileDto = new GnocFileDto();
              gnocFileDto.setPath(fullPath);
              gnocFileDto.setFileName(i.getFileName());
              gnocFileDto.setCreateUnitId(us.getUnitId());
              gnocFileDto.setCreateUnitName(unitToken.getUnitName());
              gnocFileDto.setCreateUserId(us.getUserId());
              gnocFileDto.setCreateUserName(us.getUsername());
              gnocFileDto.setCreateTime(wo.getCreateDate());
              gnocFileDto.setMappingId(resultInSideDto.getId());
              gnocFileDtos.add(gnocFileDto);
              gnocFileRepository.saveListGnocFileNotDeleteAll(GNOC_FILE_BUSSINESS.WO_SUPPORT,
                  resultInSideDto.getId(), gnocFileDtos);
            }
          }
        }
        tDto.setFileDocumentByteArray(lstArr);
        tDto.setArrFileName(lstFileName);
      }

      String comment =
          worklog.getUpdateTime() + " : " + us.getUsername() + " tick WO_HELP :" + (content != null
              ? content : "");
      //neu la wo tao tu TT goi sang TT tra ket qua
      ResultDTO resTT = null;
      Map<String, String> mapConfigProperty = commonRepository.getConfigProperty();
      try {
        if ("SPM".equals(wo.getWoSystem()) || INSERT_SOURCE.SPM_VTNET.equals(wo.getWoSystem())) {
          resTT = updateStatusTT(tDto, wo, null, null, un, us.toDTO(), comment, "SUPPORT",
              getWoStatusName(Integer.valueOf(wo.getStatus().toString())), null);
        } else if (checkProperty(mapConfigProperty, wo.getWoTypeId().toString(),
            Constants.AP_PARAM.WO_TYPE_XLSCVT)) {
          resTT = updateStatus_SCVT(null, wo, null, null, un, us.toDTO(), comment, "IS_HELP",
              "IS_HELP", null);
        } else if ("TT".equals(wo.getWoSystem())) {
          resTT = updateStatusTT(tDto, wo, null, null, un, us.toDTO(), comment, "SUPPORT",
              getWoStatusName(Integer.valueOf(wo.getStatus().toString())), null);
        }
        if (resTT != null && !RESULT.SUCCESS.equals(resTT.getKey())) {
          throw new Exception(resTT.getMessage());
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
        throw new Exception(("wo.errCommunicateSPM") + " " + e.getMessage());
      }
      ///Thanhlv12_them worklog_end
//            woDAO.updateSupportWo(woId, needSupport);
      if (needSupport != null && "1".equals(needSupport.trim())) {
        wo.setNeedSupport(Constants.VSMART_NEED_SUPPORT.YES);
        wo.setNumSupport(wo.getNumSupport() == null ? 1L : (wo.getNumSupport() + 1));
        wo.setAllowSupport(null);
      } else {
        wo.setNeedSupport(null);
      }
      woRepository.updateWo(wo);

      if ("SPM".equals(wo.getWoSystem()) || INSERT_SOURCE.SPM_VTNET.equals(wo.getWoSystem())) {
        if (needSupport != null && "1"
            .equals(needSupport.trim())) {// neu la support thuc hien tam dung WO

          Date nowTmp = new Date();
          Long timePending = Long
              .valueOf(commonRepository.getConfigPropertyValue("TIME_PENDING_WHEN_TICK_HELP"));
          if (timePending == null) {
            timePending = 24L;   // neu chua cau hinh fix tam dung 1 ngay
          }
          nowTmp.setHours(nowTmp.getHours() + timePending.intValue());
          Date endPendingTime = nowTmp;

          ResultInSideDto resultInSideDto = pendingWoCommon(null, wo.getWoCode(), endPendingTime,
              userName, "VSMART",
              "pending when Ft tich help WO", null, null, null, false, false);
          if (resultInSideDto != null && Constants.WS_RESULT.FAIL
              .equals(resultInSideDto.getKey())) {
            ResultDTO result = resultInSideDto.toResultDTO();
            return result;
          }
        } else if (wo.getStatus().equals(Long.valueOf(Constants.WO_STATUS.PENDING))) {
          ResultDTO result = updatePendingWo(wo.getWoCode(), null, userName,
              "Open pending WO when Ticket completed help",
              "VSMART", false);
          if (result != null && Constants.WS_RESULT.FAIL
              .equals(result.getKey())) {
            return result;
          }
        }
      }

      resultDTO.setKey(Constants.WS_RESULT.SUCCESS);
      resultDTO.setMessage(I18n.getLanguage("wo.update.needSupport.success"));
    } catch (Exception e) {
      resultDTO.setKey(Constants.WS_RESULT.FAIL);
      resultDTO.setMessage(e.getMessage());
      log.error(e.getMessage(), e);
    }
    return resultDTO;
  }

  @Override
  public List<WoDTOSearch> getListDataSearch(WoDTOSearch searchDto) {
    return woRepository.getListDataSearch(searchDto, false);
  }

  @Override
  public List<ResultDTO> getWOStatistic(Long unitId, int isSend, int isSearchChild, String fromDate,
      String toDate) {
    try {
      return woRepository.getWOStatistic(unitId, isSend, isSearchChild, fromDate, toDate);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      List<ResultDTO> list = new ArrayList<>();
      ResultDTO result = new ResultDTO();
      result.setMessage(ErrorUtils.printLog(e));
      list.add(result);
      return list;
    }
  }

  @Override
  public Integer getWOTotal(WoDTOSearch searchDto) {
    try {
      return woRepository.getWOTotal(searchDto);
    } catch (Exception e) {
      ErrorUtils.printLog(e);
    }
    return null;
  }

  @Override
  public ResultInSideDto deleteWo(Long woId) {
    return woRepository.deleteWo(woId);
  }

  @Override
  public List<UsersDTO> getListFtByUser(String userName, String keyword, int rowStart, int maxRow) {
    List<UsersDTO> lstOut = new ArrayList<>();
    if (rowStart < 0) {
      return lstOut;
    }
    if (maxRow <= 0) {
      maxRow = Integer.MAX_VALUE;
    }
    List<UsersInsideDto> lstUserInfo = userRepository.getListUserDTOByuserName(userName);

    if (lstUserInfo != null && lstUserInfo.size() > 0) {
      List<UsersInsideDto> lst = woCdGroupBusiness
          .getListFtByUser(String.valueOf(lstUserInfo.get(0).getUserId()), keyword, rowStart,
              maxRow);
      if (lst != null && lst.size() > 0) {
        for (UsersInsideDto dto : lst) {
          lstOut.add(dto.toOutSideDto());
        }
      }
      return lstOut;
    } else {
      return null;
    }
  }

  @Override
  public List<WoInsideDTO> getListDataForRisk(WoInsideDTO woInsideDTO) {
    Double offsetFromUser = woInsideDTO.getOffSetFromUser();
    WoInsideDTO searchDto = converTimeFromClientToServerWeb(woInsideDTO, offsetFromUser);
    return woRepository.getListDataForRisk(searchDto);
  }

  @Override
  public ResultInSideDto insertWoFromWebInMrMNGT(WoInsideDTO woInsideDTO) throws Exception {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    try {
      for (String cd : woInsideDTO.getCdIdList()) {
        // set id tao ma luu WO
        List<String> lstSeq = woRepository.getListSequenseWo("WO_SEQ", 1);
        Integer nextVal = Integer.parseInt(lstSeq.get(0));
        String woCodeAdd =
            "WO_" + woInsideDTO.getWoSystem() + "_" + DateUtil.date2StringNoSlash(new Date()) + "_"
                + nextVal;

        woInsideDTO.setWoId(Long.valueOf(nextVal));
        woInsideDTO.setWoCode(woCodeAdd);
        woInsideDTO.setCdId(Long.valueOf(cd));

        String strFile = getListFileTestService(woInsideDTO);
        if (!StringUtils.isStringNullOrEmpty(strFile)) {
          String currFile = woInsideDTO.getFileName();
          woInsideDTO.setFileName(
              currFile == null || "".equals(currFile.trim()) || ",".equals(currFile.trim())
                  ? strFile : currFile + "," + strFile);
        }

        //tuanpv14_bo sung file huong dan theo loai WO
        //sess.save(woDTO.toModel());
        insertWo(woInsideDTO.toModelOutSide());
        // luu lich su
        WoHistoryInsideDTO woDtoHis = new WoHistoryInsideDTO();

        woDtoHis.setWoCode(woCodeAdd);
        woDtoHis.setNewStatus(
            woInsideDTO.getStatus() == null ? null : Long.valueOf(woInsideDTO.getStatus()));
        woDtoHis
            .setWoId(woInsideDTO.getWoId() == null ? null : Long.valueOf(woInsideDTO.getWoId()));
        woDtoHis.setWoContent(woInsideDTO.getWoContent());
        woDtoHis.setUserName(woInsideDTO.getUser());
        woDtoHis.setUpdateTime(woInsideDTO.getCreateDate());
        woDtoHis.setCreatePersonId(
            woInsideDTO.getCreatePersonId() == null ? null
                : Long.valueOf(woInsideDTO.getCreatePersonId()));
        woDtoHis
            .setCdId(woInsideDTO.getCdId() == null ? null : Long.valueOf(woInsideDTO.getCdId()));
        woDtoHis
            .setFtId(woInsideDTO.getFtId() == null ? null : Long.valueOf(woInsideDTO.getFtId()));
        //sondt20 _ 20160405
        woDtoHis
            .setComments("(" + woInsideDTO.getStartTime() + " - " + woInsideDTO.getEndTime() + ")");

        //sess.save(woDtoHis.toModel());
        woHistoryRepository.insertWoHistory(woDtoHis);

        // luu detail
        woInsideDTO.setWoId(Long.valueOf(nextVal));
        woInsideDTO.setLastUpdateTime(new Date());
        woInsideDTO.setCreateDate(new Date());

        //sess.save(woDetail.toModel());
        woDetailRepository.insertUpdateWoDetail(woInsideDTO.getWoDetailDTO());

        // luu vat tu
        List<WoMerchandiseDTO> lstMerchandise = woInsideDTO.getLstMerchandise();
        if (lstMerchandise != null && lstMerchandise.size() > 0) {
          for (WoMerchandiseDTO j : lstMerchandise) {
            j.setWoId(woInsideDTO.getWoId() == null ? null : String.valueOf(woInsideDTO.getWoId()));
            //sess.save(j.toModel());
            woMerchandiseRepository.insertWoMerchandise(j.toModelInSide());
          }
        }
        // luu thong tin KTTS
        if (woInsideDTO.getWoKttsInfo() != null) {
          if (woInsideDTO.getWoKttsInfo().getContractId() != null
              || woInsideDTO.getWoKttsInfo().getProcessActionName() != null) {
            woInsideDTO.getWoKttsInfo().setWoId(
                woInsideDTO.getWoId() == null ? null : Long.valueOf(woInsideDTO.getWoId()));
            //sess.save(woKttsInfo.toModel());
            woKTTSInfoRepository.inserOrUpdateWoKTTSInfo(woInsideDTO.getWoKttsInfo());
          }
        }
        // ghi worklog
        if (!StringUtils.isStringNullOrEmpty(woInsideDTO.getWoWorklog())) {
          WoWorklogDTO worklogDTO = new WoWorklogDTO(woInsideDTO.toModelOutSide());
          worklogDTO.setUsername(woInsideDTO.getUser());

          //sess.save(worklogDTO.toModel());
          insertWoWorklog(worklogDTO);
        }
        // thưc hien luu cau hinh test dich vu
        List<WoTestServiceMapDTO> lstWoTestService = woInsideDTO.getLstWoTestService();
        if (lstWoTestService != null && lstWoTestService.size() > 0) {
          for (WoTestServiceMapDTO k : lstWoTestService) {
            k.setWoId(nextVal + "");
            ResultDTO sTest = commonStreamServiceProxy.insertWoTestServiceMap(k);
            if (!"SUCCESS".equals(sTest.getMessage())) {
              throw new Exception("wo.splitWo.err.saveWoTestService");
            }
          }
        }
      }
      resultInSideDto.setMessage(RESULT.SUCCESS);
      resultInSideDto.setKey(RESULT.SUCCESS);
      resultInSideDto.setIdValue(RESULT.SUCCESS);
      return resultInSideDto;
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      resultInSideDto.setKey(RESULT.FAIL);
      return resultInSideDto;
    }
  }

  @Override
  public List<ResultDTO> getWOSummaryInfobyUser2(String username, int typeSearch, Long cdId,
      String createTimeFrom, String createTimeTo) {
    setMapTypeSearch();
    String type = mapTypeSearch.get(typeSearch);
    if (StringUtils.isNotNullOrEmpty(type)) {
      return woRepository
          .getWOSummaryInfobyUser2(username, typeSearch, cdId, createTimeFrom, createTimeTo);
    } else {
      return null;
    }
  }

  public String getListFileTestService(WoInsideDTO woDTO) throws IOException {
    GnocFileDto gnocFilesGuideDto = new GnocFileDto();
    gnocFilesGuideDto.setBusinessCode(GNOC_FILE_BUSSINESS.WO_TYPE_FILES_GUIDE);
    gnocFilesGuideDto.setBusinessId(Long.valueOf(woDTO.getWoTypeId()));
    List<GnocFileDto> gnocFileDtos = gnocFileRepository.getListGnocFileByDto(gnocFilesGuideDto);
    if (gnocFileDtos != null && !gnocFileDtos.isEmpty()) {
      ArrayList<String> listFileName = new ArrayList<>();
      for (GnocFileDto dto : gnocFileDtos) {
        try {
          byte[] bytes = FileUtils
              .getFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
                  PassTranformer.decrypt(ftpPass), dto.getPath());
          String fullPath = FileUtils
              .saveFtpFile(ftpServer, ftpPort, PassTranformer.decrypt(ftpUser),
                  PassTranformer.decrypt(ftpPass), ftpFolder, dto.getFileName(), bytes,
                  woDTO.getCreateDate());
          listFileName.add(FileUtils.getFileName(fullPath));
        } catch (Exception ex) {
          log.error(ex.getMessage(), ex);
        }
      }
      return String.join(",", listFileName);
    } else {
      return null;
    }
  }

  public void setMapTypeSearch() {
    mapTypeSearch.put(WO_TYPE_SEARCH.IS_FT, "ft");
    mapTypeSearch.put(WO_TYPE_SEARCH.IS_CD, "cd");
    mapTypeSearch.put(WO_TYPE_SEARCH.IS_PROVINCE, "province");
  }

  @Override
  public ResultInSideDto runMopOnSAP(WoInsideDTO woInsideDTO) {
//    List<Long> lstMopId = new ArrayList<>();
    if ("CR".equalsIgnoreCase(woInsideDTO.getWoSystem()) && StringUtils
        .isNotNullOrEmpty(woInsideDTO.getWoSystemOutId())) {
      List<String> lstMopStr = Arrays.asList(woInsideDTO.getWoSystemOutId().split(","));
      com.viettel.vmsa.ResultDTO resultDTO = ws_sap_port.runMop(lstMopStr.size() < 1 ? null : Long
          .valueOf(lstMopStr.get(0)));//tam thoi lay thang dau tien, cho code moi roi xu ly tiep
      if (resultDTO != null && resultDTO.getResultCode() == 0) {
        return new ResultInSideDto(null, RESULT.SUCCESS, RESULT.SUCCESS);
      } else {
        return new ResultInSideDto(null, RESULT.ERROR,
            resultDTO == null ? RESULT.ERROR : resultDTO.getResultMessage());
      }
    }
    return new ResultInSideDto(null, RESULT.ERROR, I18n.getLanguage("wo.sap.invalid"));
  }

  @Override
  public ResultDTO updateResultTestFromSAP(WoDTO woDto) throws Exception {
    try {
      WoInsideDTO wo = woRepository.getWoByWoCode(woDto.getWoCode());
      Date now = new Date();
      if (wo == null) {
        return new ResultDTO(RESULT.SUCCESS, RESULT.SUCCESS, RESULT.SUCCESS);
      }
      if (StringUtils.isStringNullOrEmpty(woDto.getResult())
          || (!"OK".equals(woDto.getResult()) && !"NOK".equals(woDto.getResult()))) {
        return new ResultDTO(RESULT.FAIL, RESULT.FAIL, "Invalid result!");
      }

      UsersEntity ft = userRepository.getUserByUserName("system");

      String smsContent = null;
      if (woDto.getListFileName() != null && woDto.getListFileName().size() > 0) {
        updateFile(wo, ft.getUserId(), woDto.getListFileName(), woDto.getFileArr());
      }

      // truong hop OK đóng WO luôn WO
      if ("OK".equals(woDto.getResult())) {
        wo.setFinishTime(now);
        wo.setCompletedTime(now);
        wo.setResult(2L);
        updateWoAndHistory(wo, ft.toDTO(), "Auto close Wo from SAP", Constants.WO_STATUS.CLOSED_CD,
            now);
        smsContent = "WO test dich vu: " + wo.getWoCode()
            + " da duoc dong sau khi co ket qua test DAT tu he thong SAP. De nghi d/c kiem tra lai ket qua!";
      } else { // truong hop NOK
        updateWoAndHistory(wo, ft.toDTO(), "SAP return result NOK",
            wo.getStatus() == null ? null : wo.getStatus().toString(), now);
        smsContent = "He thong SAP tra ket qua NOK sau khi thuc hien test WO: " + wo.getWoCode()
            + ". De nghi d/c kiem tra lai ket qua!";
      }

      if (!StringUtils.isStringNullOrEmpty(smsContent)) {
        List<UsersInsideDto> lstU = woCdBusiness.getListCdByGroup(wo.getCdId());
        if (lstU != null && lstU.size() > 0) {
          UsersInsideDto u = lstU.get(0);
          MessagesDTO message = new MessagesDTO();

          message.setSmsGatewayId(smsGatewayId);  // fix code = 5
          message.setReceiverId(u.getUserId().toString());
          message.setReceiverUsername(u.getStaffCode());
          message.setReceiverFullName(u.getFullname());
          message.setSenderId(senderId);  // fix code = 400
          message.setReceiverPhone(u.getMobile());
          message.setStatus("0");
          message.setCreateTime(
              DateTimeUtils.convertDateToString(new Date(), Constants.ddMMyyyyHHmmss));
          message.setContent(smsContent);
          messagesRepository.insertOrUpdateWfm(message);
        }
      }

    } catch (Exception e) {
      log.error(e.getMessage(), e);
      TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
      throw new Exception("Have some error!");
    }
    return new ResultDTO(RESULT.SUCCESS, RESULT.SUCCESS, RESULT.SUCCESS);
  }

  @Override
  public ResultInSideDto importData(MultipartFile upLoadFile, List<MultipartFile> lstAttachments)
      throws Exception {
//    List<MapProvinceCdDTO> listMapProviceCd = new ArrayList<>();
    Map<String, String> mapConfigProperty = commonRepository.getConfigProperty();
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
    List<WoDTO> lstWoImport = new ArrayList<>();
    Map<String, String> mapBusinessType = new HashMap<>();
    mapBusinessType.put(I18n.getLanguage("wo.import.type.userName"), "1");
    mapBusinessType.put(I18n.getLanguage("wo.import.type.stationCode"), "2");
    mapBusinessType.put(I18n.getLanguage("wo.import.type.deviceCode"), "3");
    mapBusinessType.put(I18n.getLanguage("wo.import.type.account"), "4");
    mapBusinessType.put(I18n.getLanguage("wo.import.type.connectorCode"), "5");
    mapBusinessType.put(I18n.getLanguage("wo.import.type.cdGroupCode"), "6");
    UserToken userToken = ticketProvider.getUserToken();
    boolean hasErr = false;
    try {
      if (upLoadFile == null) {
        resultInSideDTO.setKey(Constants.RESULT.FILE_IS_NULL);
        return resultInSideDTO;
      } else {
        String filePath = FileUtils
            .saveTempFile(upLoadFile.getOriginalFilename(), upLoadFile.getBytes(),
                tempFolder);

        File fileImport = new File(filePath);

        List<Object[]> lstHeader;
        lstHeader = CommonImport.getDataFromExcelFile(fileImport,
            0,//sheet
            5,//begin row
            0,//from column
            12,//to column
            1000);

        if (lstHeader.size() == 0 || !validFileFormat(lstHeader)) {
          return new ResultInSideDto(null, Constants.RESULT.FILE_INVALID_FORMAT,
              I18n.getLanguage("common.import.errorTemplate"));
        }

        // Lay du lieu import
        List<Object[]> lstData = CommonImport.getDataFromExcelFile(
            fileImport,
            0,//sheet
            6,//begin row
            1,//from column
            12,//to column
            1000
        );

        if (lstData != null && lstData.size() > 2500) {
          return new ResultInSideDto(null, RESULT.DATA_OVER,
              I18n.getLanguage("common.importing.maxrow"));
        }

        //lay file dinh kem
        List<byte[]> lstFileArray = new ArrayList<>();
        List<String> lstFile = new ArrayList<>();
        if (lstAttachments != null && lstAttachments.size() > 0) {
          for (MultipartFile item : lstAttachments) {
            lstFileArray.add(item.getBytes());
            lstFile.add(item.getOriginalFilename());
          }
        }

        if (lstData != null && lstData.size() > 0) {
          int row = 0;
          Double offset = TimezoneContextHolder.getOffsetDouble();
          List<WoPriorityDTO> woPriorityDTOS = getWoPriorityByWoTypeID(null);
          Map<String, String> mapWoPriorites = new HashMap<>();
          if (woPriorityDTOS != null) {
            for (WoPriorityDTO item : woPriorityDTOS) {
              if (StringUtils.isNotNullOrEmpty(item.getPriorityName())) {
                mapWoPriorites.put(item.getPriorityName(), item.getPriorityCode());
              }
            }
          }
          for (Object[] obj : lstData) {
            WoDTO woDTO = new WoDTO();
            StringBuilder errStr = new StringBuilder();
            //Noi dung cong viec
            if (!StringUtils.isStringNullOrEmpty(obj[0])) {
              woDTO.setWoContent(obj[0].toString().trim());
              if (woDTO.getWoContent().length() > 1000) {
                errStr.append(I18n.getLanguage("wo.list.woContent.maxlength1000")).append(";");
                hasErr = true;
              }
            } else {
              errStr.append(I18n.getLanguage("wo.woContent")).append(" ")
                  .append(I18n.getLanguage("common.notnull")).append(";");
              hasErr = true;
            }

            //Mo ta
            if (!StringUtils.isStringNullOrEmpty(obj[1])) {
              woDTO.setWoDescription(obj[1].toString().trim());
              if (woDTO.getWoDescription().length() > 1000) {
                errStr.append(I18n.getLanguage("wo.list.woDescription.maxlength1000")).append(";");
                hasErr = true;
              }
            }

            //He thong
            if (!StringUtils.isStringNullOrEmpty(obj[2])) {
              woDTO.setWoSystem(obj[2].toString().trim());
              if (!"WFM-FT".equals(woDTO.getWoSystem()) && !"WFM-OTHERS"
                  .equals(woDTO.getWoSystem())) {
                errStr.append(I18n.getLanguage("wo.woSystem")).append(" ")
                    .append(I18n.getLanguage("common.invalid")).append(";");
                hasErr = true;
              }
            } else {
              errStr.append(I18n.getLanguage("wo.woSystem")).append(" ")
                  .append(I18n.getLanguage("common.notnull")).append(";");
              hasErr = true;
            }
            boolean check = false;
            //Loai cong viec
            if (!StringUtils.isStringNullOrEmpty(obj[3])) {
              woDTO.setWoTypeCode(obj[3].toString().trim());
              WoTypeInsideDTO woTypeDTO = woTypeBusiness.getWoTypeByCode(obj[3].toString().trim());
              if (woTypeDTO != null) {
                if (!"1".equals(String.valueOf(woTypeDTO.getEnableCreate()))) {
                  errStr.append(I18n.getLanguage("wo.list.woType.notEnableCreate")).append(";");
                  hasErr = true;
                  check = true;
                }
              } else {
                errStr.append(I18n.getLanguage("wo.WoTypeCodeIsNotExists")).append(";");
                hasErr = true;
                check = true;
              }

            } else {
              errStr.append(I18n.getLanguage("wo.woTypeCode")).append(" ")
                  .append(I18n.getLanguage("common.notnull")).append(";");
              hasErr = true;
            }
            //Muc uu tien
            if (!StringUtils.isStringNullOrEmpty(obj[4])) {
              woDTO.setPriorityCode(obj[4].toString().trim());
              if (!mapWoPriorites.containsKey(obj[4].toString().trim())) {
                hasErr = true;
                errStr.append(I18n.getLanguage("wo.priorityCode")).append(" ")
                    .append(I18n.getLanguage("common.invalid")).append(";");
              }
            } else {
              errStr.append(I18n.getLanguage("wo.priorityCode")).append(" ")
                  .append(I18n.getLanguage("common.notnull")).append(";");
              hasErr = true;
            }
            //start time
            Date st = null;
            if (!StringUtils.isStringNullOrEmpty(obj[5])) {
              if (obj[5].toString().trim().length() == 19) {
                try {
                  st = DateTimeUtils.convertStringToDateTime(obj[5].toString().trim());
                  if (st.getTime() < new Date().getTime()) {
                    errStr.append(I18n.getLanguage("woCdTemp.notify.dateCompareNow")).append(";");
                    hasErr = true;
                  }
                } catch (Exception e) {
                  log.error(e.getMessage(), e);
                  errStr.append(I18n.getLanguage("woCdTemp.list.startTime.invalid")).append(";");
                  hasErr = true;
                }

                woDTO.setStartTime(obj[5].toString().trim());
              } else {
                errStr.append(I18n.getLanguage("woCdTemp.list.startTime.invalid")).append(";");
                hasErr = true;
              }

              woDTO.setStartTime(obj[5].toString().trim());

            } else {
              errStr.append(I18n.getLanguage("wo.startTimeStr")).append(" ")
                  .append(I18n.getLanguage("common.notnull")).append(";");
              hasErr = true;
            }
            //end time
            if (!StringUtils.isStringNullOrEmpty(obj[6])) {
              if (obj[6].toString().trim().length() == 19) {
                try {
                  Date et = DateTimeUtils.convertStringToDateTime(obj[6].toString().trim());

                  if (st != null && et != null) {
                    if (st.getTime() > et.getTime()) {
                      errStr.append(I18n.getLanguage("woCdTemp.notify.dateCompare")).append(";");
                      hasErr = true;
                    }
                  }

                } catch (Exception e) {
                  log.error(e.getMessage(), e);
                  errStr.append(I18n.getLanguage("woCdTemp.list.endTime.invalid")).append(";");
                  hasErr = true;
                }
              } else {
                errStr.append(I18n.getLanguage("woCdTemp.list.endTime.invalid")).append(";");
                hasErr = true;
              }

              woDTO.setEndTime(obj[6].toString().trim());
            } else {
              errStr.append(I18n.getLanguage("wo.endTimeStr")).append(" ")
                  .append(I18n.getLanguage("common.notnull")).append(";");
              hasErr = true;
            }

            //giao viec den ft
            if (!StringUtils.isStringNullOrEmpty(obj[7])) {
              if (!I18n.getLanguage("wo.assign.ft").equalsIgnoreCase(obj[7].toString().trim())
                  && !I18n.getLanguage("wo.assign.cd").equalsIgnoreCase(obj[7].toString().trim())) {
                errStr.append(I18n.getLanguage("wo.assign")).append(" ")
                    .append(I18n.getLanguage("common.invalid")).append(";");
                hasErr = true;
              }
              woDTO.setAssign(obj[7].toString().trim());

            } else {
              errStr.append(I18n.getLanguage("wo.assign")).append(" ")
                  .append(I18n.getLanguage("common.notnull")).append(";");
              hasErr = true;
            }

            //loai doi tuong
            if (!StringUtils.isStringNullOrEmpty(obj[8])) {
              woDTO.setImportBusinessType(obj[8].toString().trim());
            }

            if (StringUtils.isStringNullOrEmpty(woDTO.getImportBusinessType()) || !mapBusinessType
                .containsKey(woDTO.getImportBusinessType())) {
              errStr.append(I18n.getLanguage("wo.businessType")).append(" ")
                  .append(I18n.getLanguage("common.invalid")).append(";");
              hasErr = true;
            }

            //Ma doi tuong
            if (!StringUtils.isStringNullOrEmpty(obj[9])) {
              woDTO.setImportBusinessCode(obj[9].toString().trim());
              if(!StringUtils.isStringNullOrEmpty(woDTO.getWoTypeCode())){
                if (woDTO.getWoTypeCode().equals(Constants.AP_PARAM.WO_TYPE_XLSCC)) {
                  if(mapBusinessType.get(woDTO.getImportBusinessType()) != "2"){
                    errStr.append(I18n.getLanguage("wo.typeObject")).append(";");
                    hasErr = true;
                  }
                }
              }
            } else if (woDTO.getCdGroupCode() == null) {
              errStr.append(I18n.getLanguage("wo.businessCode")).append(" ")
                  .append(I18n.getLanguage("common.notnull")).append(";");
              hasErr = true;
            }
            // thangdt su co bao hong
            if(!(StringUtils.isStringNullOrEmpty(woDTO.getWoTypeCode())) && check == false){
              WoTypeInsideDTO woTypeDTO = woTypeBusiness.getWoTypeByCode(obj[3].toString().trim());
                if (checkProperty(mapConfigProperty, woTypeDTO.getWoTypeId().toString(),
                    Constants.AP_PARAM.WO_TYPE_QLTS_BAO_HONG)) {
                  if (!StringUtils.isStringNullOrEmpty(obj[10])) {
                    woDTO.setActiveEnvironmentId(Long.parseLong(obj[10].toString().trim()));
                  }else{
                    errStr.append(I18n.getLanguage("wo.activeEnvironmentId")).append(" ")
                        .append(I18n.getLanguage("common.notnull")).append(";");
                    hasErr = true;
                  }
                  if (!StringUtils.isStringNullOrEmpty(obj[11])) {
                    woDTO.setReasonDetail(obj[11].toString().trim());
                  }else{
                    errStr.append(I18n.getLanguage("wo.reasonDetail")).append(" ")
                        .append(I18n.getLanguage("common.notnull")).append(";");
                    hasErr = true;
                  }
                }
            }
            if (lstFile != null) {
              woDTO.setListFileName(lstFile);
            }

            if (lstFileArray != null && lstFileArray.size() > 0) {
              woDTO.setFileArr(lstFileArray);
            }


            if (hasErr && !StringUtils.isStringNullOrEmpty(errStr.toString())) {
              woDTO.setResultImport(errStr.toString());
            }
            lstWoImport.add(woDTO);
          }

          for (WoDTO dto : lstWoImport) {
            if (StringUtils.isStringNullOrEmpty(dto.getResultImport())) {
              WoDTO woDto = new WoDTO();
              woDto.setWoContent(dto.getWoContent());
              woDto.setWoDescription(dto.getWoDescription());
              woDto.setWoSystem(dto.getWoSystem());
              woDto.setWoTypeCode(dto.getWoTypeCode());
              woDto.setActiveEnvironmentId(dto.getActiveEnvironmentId());
              woDto.setReasonDetail(dto.getReasonDetail());
              //bo sung them key cho WO_PRIORITY_

              woDto.setPriorityCode(
                  "WO_PRIORITY_" + String.valueOf(mapWoPriorites.get(dto.getPriorityCode()))
                      .toUpperCase());
              woDto.setStartTime(
                  DateTimeUtils.converClientDateToServerDate(dto.getStartTime(), offset));
              woDto
                  .setEndTime(DateTimeUtils.converClientDateToServerDate(dto.getEndTime(), offset));
              woDto.setCreatePersonName(userToken.getUserName());
              woDto.setCreateDate(DateTimeUtils
                  .converClientDateToServerDate(DateTimeUtils.getSysDateTime(), offset));
              woDto.setListFileName(dto.getListFileName());
              woDto.setFileArr(dto.getFileArr());
              if (I18n.getLanguage("wo.assign.ft").equals(dto.getAssign())) {
                woDto.setIsSendFT("1");
              } else {
                woDto.setIsSendFT("0");
              }
              // giao viec cho FT
              switch (String.valueOf(mapBusinessType.get(dto.getImportBusinessType()))) {
                // ma tram
                case "1":
                  woDto.setFtName(dto.getImportBusinessCode());
                  break;
                // ma thiet bi
                case "2":
                  woDto.setStationCode(dto.getImportBusinessCode());
                  break;
                // account khach hang
                case "3":
                  woDto.setDeviceCode(dto.getImportBusinessCode());
                  break;
                case "4":
                  woDto.setAccountIsdn(dto.getImportBusinessCode());
                  break;
                case "5":
                  woDto.setConnectorCode(dto.getImportBusinessCode());
                  break;
                case "6":
                  woDto.setCdGroupCode(dto.getImportBusinessCode());
                  break;
              }

              try {
                ResultDTO result = createWoForOtherSystem(woDto);
                if (result != null && RESULT.SUCCESS.equals(result.getMessage())) {
                  //  thangdt bo sung loai Wo = "Xử lý sự cố cáp" thì sinh wo2 ="Củng cố khôi phục cáp"
                  if (checkProperty(mapConfigProperty, String.valueOf(woDto.toModelInSide().getWoTypeId()),
                      Constants.AP_PARAM.WO_TYPE_XLSCC)) {
                    UsersInsideDto usersFtDto = new UsersInsideDto();
                    usersFtDto.setUsername(userToken.getUserName());
                    prepareWoTypeXLSCCUpdate(woDto.toModelInSide(), usersFtDto);
                  }
//                  dto.setResultImport(I18n.getLanguage("common.insert.import.success") + " " + result.getId());
                  dto.setResultImport(I18n.getLanguage("common.import.validRecord"));
                } else {
                  hasErr = true;
//                  dto.setResultImport(I18n.getLanguage("common.insert.import.fail") + ":" + result.getMessage());
//                  dto.setResultImport(result.getMessage());
                }
              } catch (Exception e) {
                log.error(e.getMessage(), e);
                hasErr = true;
//                dto.setResultImport(I18n.getLanguage("common.insert.import.fail") + ":" + e.getMessage());
                dto.setResultImport(e.getMessage());
              }
            }

          }
//
//            for(CfgFtOnTimeDTO item : lstCfgFtOnTimeDTO) {
//              insertOrUpdate(item);
//              item.setResultImport(I18n.getLanguage("common.insert.import.success"));
//            }
          if (hasErr) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            resultInSideDTO.setKey(RESULT.ERROR);
            resultInSideDTO.setMessage(I18n.getLanguage("import.common.fail"));
          }

          File file = exportFileImport(lstWoImport);
          resultInSideDTO.setFile(file);
          resultInSideDTO.setFilePath(file.getPath());
        } else {
          return new ResultInSideDto(null, RESULT.NODATA,
              "File " + I18n.getLanguage("common.searh.nodata"));
        }


      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return resultInSideDTO;
  }

  @Override
  public ResultInSideDto importDataAssets(MultipartFile upLoadFile,
      List<MultipartFile> lstAttachments, String formDataJson) throws Exception {
    ResultInSideDto resultInSideDTO = new ResultInSideDto();
    resultInSideDTO.setKey(Constants.RESULT.SUCCESS);
    List<WoDTO> lstWoImport = new ArrayList<>();
    UserToken userToken = ticketProvider.getUserToken();
    JSONObject jsonObject = new JSONObject(formDataJson);
    String getTypeNameFile = jsonObject.get("typeFileWoAssets").toString();
    boolean hasErr = false;
    try {
      if (upLoadFile == null || lstAttachments.size() == 0) {
        resultInSideDTO.setId(null);
        resultInSideDTO.setKey("ERROR_NO_DOWNLOAD");
        resultInSideDTO.setMessage(I18n.getLanguage("wo.infoAsset.empty"));
        return resultInSideDTO;
      } else {
        String filePath = FileUtils
            .saveTempFile(upLoadFile.getOriginalFilename(), upLoadFile.getBytes(),
                tempFolder);

        File fileImport = new File(filePath);

        List<Object[]> lstHeader;
        lstHeader = CommonImport.getDataFromExcelFile(fileImport,
            0,//sheet
            0,//begin row
            0,//from column
            5,//to column
            1000);

        if (lstHeader.size() == 0 || !validFileFormatAssets(lstHeader)) {
          return new ResultInSideDto(null, "ERROR_NO_DOWNLOAD",
              I18n.getLanguage("wo.infoAsset.invalidFormat"));
        }

        // Lay du lieu import
        List<Object[]> lstData = CommonImport.getDataFromExcelFile(
            fileImport,
            0,//sheet
            1,//begin row
            1,//from column
            6,//to column
            1000
        );

        // file chi dc them 1 ban ghi
        if (lstData != null && lstData.size() > 1) {
          return new ResultInSideDto(null, "ERROR_NO_DOWNLOAD",
              I18n.getLanguage("wo.infoAsset.maxRow"));
        }

        // check file dinh kem
        String filePathAttachment = FileUtils
            .saveTempFile(lstAttachments.get(0).getOriginalFilename(),
                lstAttachments.get(0).getBytes(),
                tempFolder);

        File fileImportAttachments = new File(filePathAttachment);
        List<Object[]> lstHeaderAttachments;
        if (getTypeNameFile.equals(I18n.getLanguage("wo.infoAsset.formDataJson.TProperty"))) {
          lstHeaderAttachments = CommonImport.getDataFromExcelFile(fileImportAttachments,
              0,//sheet
              2,//begin row
              0,//from column
              6,//to column
              1000);
        } else {
          lstHeaderAttachments = CommonImport.getDataFromExcelFile(fileImportAttachments,
              0,//sheet
              2,//begin row
              0,//from column
              5,//to column
              1000);
        }
        // check bieu mau file dinh kem
        if (lstHeaderAttachments.size() == 0 || !validFileFormatPropertyInformation(
            lstHeaderAttachments, getTypeNameFile)) {
          return new ResultInSideDto(null, "ERROR_NO_DOWNLOAD",
              I18n.getLanguage("wo.infoAsset.invalidFormatInfoProperty"));
        }

        // check size file dinh kem
        if (lstHeaderAttachments != null && lstHeaderAttachments.size() > 2000) {
          return new ResultInSideDto(null, "ERROR_NO_DOWNLOAD",
              I18n.getLanguage("wo.infoAsset.maxRow"));
        }

        // Lay du lieu import file dinh kem
        List<Object[]> lstDataAttachments;
        if (getTypeNameFile.equals(I18n.getLanguage("wo.infoAsset.formDataJson.TProperty"))) {
          lstDataAttachments = CommonImport.getDataFromExcelFile(
              fileImportAttachments,
              0,//sheet
              3,//begin row
              0,//from column
              7,//to column
              1000
          );
        } else {
          lstDataAttachments = CommonImport.getDataFromExcelFile(
              fileImportAttachments,
              0,//sheet
              3,//begin row
              0,//from column
              6,//to column
              1000
          );
        }
        if (lstData != null && lstData.size() > 0 && lstDataAttachments != null
            && lstDataAttachments.size() > 0) {
          int row = 0;
          Double offset = TimezoneContextHolder.getOffsetDouble();
          List<WoPriorityDTO> woPriorityDTOS = getWoPriorityByWoTypeID(null);
          Map<String, String> mapWoPriorites = new HashMap<>();
          if (woPriorityDTOS != null) {
            for (WoPriorityDTO item : woPriorityDTOS) {
              if (StringUtils.isNotNullOrEmpty(item.getPriorityName())) {
                mapWoPriorites.put(item.getPriorityName(), item.getPriorityCode());
              }
            }
          }
          for (Object[] obj : lstData) {
            WoDTO woDTO = new WoDTO();
            //Noi dung cong viec
            if (!StringUtils.isStringNullOrEmpty(obj[0])) {
              StringBuilder content = new StringBuilder();
              if (obj[0].toString().trim().length() > 1000) {
                return new ResultInSideDto(null, "ERROR_NO_DOWNLOAD",
                    I18n.getLanguage("wo.list.woContent.maxlength1000") + " (File" + " " + I18n
                        .getLanguage("wo.infoAsset.info") + ")");
              }
            } else {
              return new ResultInSideDto(null, "ERROR_NO_DOWNLOAD",
                  I18n.getLanguage("wo.woContent") + " " + I18n
                      .getLanguage("common.notnull") + " (File" + " " + I18n
                      .getLanguage("wo.infoAsset.info") + ")");
            }

            //Mo ta
            if (!StringUtils.isStringNullOrEmpty(obj[1])) {
              woDTO.setWoDescription(obj[1].toString().trim());
              if (obj[1].toString().trim().length() > 1000) {
                return new ResultInSideDto(null, "ERROR_NO_DOWNLOAD",
                    I18n.getLanguage("wo.list.woDescription.maxlength1000") + " (File" + " " + I18n
                        .getLanguage("wo.infoAsset.info")
                        + ")");
              }
            }

            //start time
            Date st = null;
            if (!StringUtils.isStringNullOrEmpty(obj[2])) {
              if (obj[2].toString().trim().length() == 19) {
                try {
                  st = DateTimeUtils.convertStringToDateTime(obj[2].toString().trim());
                  if (st.getTime() < new Date().getTime()) {
                    return new ResultInSideDto(null, "ERROR_NO_DOWNLOAD",
                        I18n.getLanguage("woCdTemp.notify.dateCompareNow") + " (File" + " " + I18n
                            .getLanguage("wo.infoAsset.info") + ")");
                  }
                } catch (Exception e) {
                  log.error(e.getMessage(), e);
                  return new ResultInSideDto(null, "ERROR_NO_DOWNLOAD",
                      I18n.getLanguage("woCdTemp.list.startTime.invalid") + " (File" + " " + I18n
                          .getLanguage("wo.infoAsset.info") + ")");
                }

                woDTO.setStartTime(obj[2].toString().trim());
              } else {
                return new ResultInSideDto(null, "ERROR_NO_DOWNLOAD",
                    I18n.getLanguage("woCdTemp.list.startTime.invalid") + " (File" + " " + I18n
                        .getLanguage("wo.infoAsset.info") + ")");
              }

              woDTO.setStartTime(obj[2].toString().trim());

            } else {
              return new ResultInSideDto(null, "ERROR_NO_DOWNLOAD",
                  I18n.getLanguage("wo.startTimeStr") + " " + I18n.getLanguage("common.notnull")
                      + " (File" + " " + I18n.getLanguage("wo.infoAsset.info") + ")");
            }
            //end time
            if (!StringUtils.isStringNullOrEmpty(obj[3])) {
              if (obj[3].toString().trim().length() == 19) {
                try {
                  Date et = DateTimeUtils.convertStringToDateTime(obj[3].toString().trim());
                  if (st != null && et != null) {
                    if (st.getTime() > et.getTime()) {
                      return new ResultInSideDto(null, "ERROR_NO_DOWNLOAD",
                          I18n.getLanguage("woCdTemp.notify.dateCompare") + " (File" + " " + I18n
                              .getLanguage("wo.infoAsset.info") + ")");
                    }
                  }

                } catch (Exception e) {
                  log.error(e.getMessage(), e);
                  return new ResultInSideDto(null, "ERROR_NO_DOWNLOAD",
                      I18n.getLanguage("woCdTemp.list.endTime.invalid") + " (File" + " " + I18n
                          .getLanguage("wo.infoAsset.info") + ")");
                }
              } else {
                return new ResultInSideDto(null, "ERROR_NO_DOWNLOAD",
                    I18n.getLanguage("woCdTemp.list.endTime.invalid") + " (File" + " " + I18n
                        .getLanguage("wo.infoAsset.info") + ")");
              }

              woDTO.setEndTime(obj[3].toString().trim());
            } else {
              return new ResultInSideDto(null, "ERROR_NO_DOWNLOAD",
                  I18n.getLanguage("wo.endTimeStr") + " " + I18n.getLanguage("common.notnull")
                      + " (File" + " " + I18n.getLanguage("wo.infoAsset.info") + ")");
            }

            //Mo ke hoach
            if (!StringUtils.isStringNullOrEmpty(obj[4])) {
              if (obj[4].toString().trim().length() > 1000) {
                return new ResultInSideDto(null, "ERROR_NO_DOWNLOAD",
                    I18n.getLanguage("wo.list.woDescription.maxlength1000") + " " + I18n
                        .getLanguage("common.notnull") + " (File" + " " + I18n
                        .getLanguage("wo.infoAsset.info") + ")");
              }
            }
//            else{
//                return new ResultInSideDto(null, "ERROR_NO_DOWNLOAD",
//                    I18n.getLanguage("wo.infoAsset.codePlan") +" "+I18n
//                        .getLanguage("common.notnull") + " (File" + " " + I18n
//                        .getLanguage("wo.infoAsset.info") + ")");
//            }

          }

          List<WoDTO> listImport = new ArrayList<>();
          List<WoMerchandiseInsideDTO> woMerchandiseInsideDTOList = new ArrayList<>();
          List<WoMerchandiseDTO> woMerchandiseDTOList = new ArrayList<>();
          Object[] listInfoWo = lstData.get(0);
          Map<String, String> mapConfigProperty = commonRepository.getConfigProperty();
          //<editor-fold desc="File TProperty" defaultstate="collapsed">
          if (getTypeNameFile.equals(I18n.getLanguage("wo.infoAsset.formDataJson.TProperty"))) {
            // Kiem tra vat tu(Start)
            String nation = getNationFromUnit(userToken.getDeptId());
            Map<String, Object[]> mapObVatTu = new HashMap();
            Map<String, Object[]> mapObMatramNguon = new HashMap();
            Map<String, Object[]> mapObMatramDich = new HashMap();
            Map<String, Object[]> mapObMaNhatramNguon = new HashMap();
            Map<String, Object[]> mapObMaNhatramDich = new HashMap();
            Map<String, List<CatStationBO>> mapObMatramNguonList = new HashMap();
            Map<String, List<CatStationBO>> mapObMatramDichList = new HashMap();
            Map<String, WoCdGroupInsideDTO> mapObDieuPhoiNguonList = new HashMap();
            Map<String, WoCdGroupInsideDTO> mapObDieuPhoiDichList = new HashMap();
            Map<String, List<MerchandiseBO>> listVatTuVaSoLuong = new HashMap();
            List<Object[]> listVatTuOb = new ArrayList<>();
            List<Object[]> objects = new ArrayList<>();
            String listVatTu = "";
            String listError = null;
            for (Object[] obj1 : lstDataAttachments) {
//              if (!StringUtils.isStringNullOrEmpty(obj1[1])) {
//                mapObVatTu.put(obj1[1].toString().trim(), obj1);
//              }
              if (!StringUtils.isStringNullOrEmpty(obj1[3])) {
                mapObMatramNguon.put(obj1[3].toString().trim(), obj1);
              }
              if (!StringUtils.isStringNullOrEmpty(obj1[4])) {
                mapObMatramDich.put(obj1[4].toString().trim(), obj1);
              }
              if (!StringUtils.isStringNullOrEmpty(obj1[5])) {
                mapObMaNhatramNguon.put(obj1[5].toString().trim(), obj1);
              }
              if (!StringUtils.isStringNullOrEmpty(obj1[6])) {
                mapObMaNhatramDich.put(obj1[6].toString().trim(), obj1);
              }
            }
            // Kiem tra vat tu co ton tai trong tram(Start)
            // loc cac ban ghi trung so luong, ma vat tu va ma tram
            for (Object[] obj1 : lstDataAttachments) {
              if (!StringUtils.isStringNullOrEmpty(obj1[1]) && !StringUtils
                  .isStringNullOrEmpty(obj1[2]) && obj1[2].toString().trim().matches(REGEX_NUMBER)
                  && !StringUtils.isStringNullOrEmpty(obj1[3]) && !obj1[2].toString().trim()
                  .contains(",")) {
                mapObVatTu.put(
                    obj1[1].toString().trim() + "_" + (obj1[2].toString().trim()) + "_" + (obj1[3]
                        .toString().trim()), obj1);
              }
            }
            for (Map.Entry<String, Object[]> e : mapObVatTu.entrySet()) {
              Object[] objects1 = e.getValue();
              listVatTuOb.add(objects1);
            }
            // loc cac ban ghi cung ma tram nhung khac list vat tu

            Map<String, String> errorCodeStation = new HashedMap();
            Map<String, String> listErrorVatTu = new HashMap();

            Kttsbo resultCheckKtts = null;
            for (Object[] obj1 : listVatTuOb) {
              if (listVatTuVaSoLuong.containsKey(obj1[3].toString().trim())) {
                List<MerchandiseBO> lstMercodeBo = new ArrayList<>();
                MerchandiseBO bo = new MerchandiseBO();
                bo.setCount(Double.parseDouble(obj1[2].toString().trim()));
                bo.setMerCode(obj1[1].toString().trim());
                List<MerchandiseBO> lstMercodeBo1 = listVatTuVaSoLuong
                    .get(obj1[3].toString().trim());
                if (lstMercodeBo1.size() > 0) {
                  lstMercodeBo1.add(bo);
                  listVatTuVaSoLuong.put(obj1[3].toString().trim(), lstMercodeBo1);
                } else {
                  lstMercodeBo.add(bo);
                  listVatTuVaSoLuong.put(obj1[3].toString().trim(), lstMercodeBo);
                }

              } else {
                List<MerchandiseBO> lstMercodeBo = new ArrayList<>();
                MerchandiseBO bo = new MerchandiseBO();
                bo.setCount(Double.parseDouble(obj1[2].toString().trim()));
                bo.setMerCode(obj1[1].toString().trim());
                lstMercodeBo.add(bo);
                listVatTuVaSoLuong.put(obj1[3].toString().trim(), lstMercodeBo);
              }
            }
            // Goi ham check vat tu trong tram
            for (Map.Entry<String, List<MerchandiseBO>> e : listVatTuVaSoLuong.entrySet()) {
              List<MerchandiseBO> lstMercodeBo1 = e.getValue();
              resultCheckKtts = kttsVsmartPort
                  .checkListMerEntityNation(lstMercodeBo1, e.getKey(),
                      nation);
              if (resultCheckKtts.getStatus().equals("Error")) {
                if (resultCheckKtts.getErrorCode() != null && resultCheckKtts.getErrorCode()
                    .equals("NOT_EXIST_STATION_CODE")) {
                  errorCodeStation.put(e.getKey(), resultCheckKtts.getErrorInfo());
                } else if (resultCheckKtts.getErrorCode() != null && resultCheckKtts.getErrorCode()
                    .equals("LIST_ENTITY_IS_INVALID")) {
                  for (ErrorBO errorBO : resultCheckKtts.getListError()) {
                    String soLuong = errorBO.getDescription();
                    String key = e.getKey() + "_" + errorBO.getMerCode();
                    if (!StringUtils.isStringNullOrEmpty(listErrorVatTu.get(key))) {
                      listErrorVatTu
                          .put(key, listErrorVatTu.get(key) + errorBO.getDescription() + ";");
                    } else {
                      listErrorVatTu.put(key, errorBO.getDescription() + ";");
                    }
                  }
                }
              }
            }

            // Kiem tra vat tu co ton tai trong tram(End)

            // Kiem tra ma tram nguon(Start)
            for (Map.Entry<String, Object[]> e : mapObMatramNguon.entrySet()) {
              Object[] objects1 = e.getValue();
              List<CatStationBO> listEmpty = new ArrayList<>();
              List<CatStationBO> list = getStationListNation(objects1[3].toString().trim(), null);
              if (list.size() > 0) {
                mapObMatramNguonList.put(objects1[3].toString().trim(), list);
              } else {
                mapObMatramNguonList.put(objects1[3].toString().trim(), listEmpty);
              }
            }
            // Kiem tra ma tram nguon(End)

            // Kiem tra ma tram dich(Start)
            for (Map.Entry<String, Object[]> e : mapObMatramDich.entrySet()) {
              Object[] objects1 = e.getValue();
              List<CatStationBO> listEmpty = new ArrayList<>();
              List<CatStationBO> list = getStationListNation(objects1[4].toString().trim(), null);
              if (list.size() > 0) {
                mapObMatramDichList.put(objects1[4].toString().trim(), list);
              } else {
                mapObMatramDichList.put(objects1[4].toString().trim(), listEmpty);
              }
            }
            // Kiem tra ma tram dich(End)

            // Kiểm tra mã nhóm điều phối nguồn(Start)
            String valueMatramnguon = mapConfigProperty.get("WO.TYPE.CHECK.QLTS.DC.HC");
            for (Map.Entry<String, Object[]> e : mapObMaNhatramNguon.entrySet()) {
              if(!StringUtils.isStringNullOrEmpty(valueMatramnguon)){
                WoCdGroupInsideDTO woCdGroup123 = woCdGroupBusiness
                    .getCdByStationCodeNation(e.getKey(), valueMatramnguon, "4", null,
                        Constants.NATION_CODES.VNM);
                mapObDieuPhoiNguonList.put(e.getKey(), woCdGroup123);
              }else{
                mapObDieuPhoiNguonList.put(e.getKey(), null);
              }
            }
            // Kiểm tra mã nhóm điều phối nguồn (Start)

            // Kiểm tra mã nhóm điều phối đích(Start)
            String valueMatramdich = mapConfigProperty.get("WO.TYPE.CHECK.QLTS.DC.NC");
            for (Map.Entry<String, Object[]> e : mapObMaNhatramDich.entrySet()) {
              WoCdGroupInsideDTO woCdGroup123 = woCdGroupBusiness
                  .getCdByStationCodeNation(e.getKey(), valueMatramdich, "4", null,
                      Constants.NATION_CODES.VNM);
              mapObDieuPhoiDichList.put(e.getKey(), woCdGroup123);
            }
            // Kiểm tra mã nhóm điều phối đích (Start)

            for (Object[] obj1 : lstDataAttachments) {
              WoDTO woDTO = new WoDTO();
              // khai bao
              StringBuilder errStr = new StringBuilder();
              WoMerchandiseInsideDTO woMerchandiseInsideDTO = new WoMerchandiseInsideDTO();
              WoMerchandiseDTO woMerchandiseDTO = new WoMerchandiseDTO();
              boolean checkMaTramNguon = false;
              boolean checkMaTramDich = false;
              boolean checkMaTramHaCap = false;
              boolean checkDauPhay = false;
              if (!StringUtils.isStringNullOrEmpty(obj1[1])) {
                woMerchandiseInsideDTO.setMerchandiseCode(obj1[1].toString().trim());
                woMerchandiseDTO.setMerchandiseCode(obj1[1].toString().trim());
              } else {
                errStr.append(I18n.getLanguage("wo.infoAsset.codeMaterial")).append(" ")
                    .append(I18n.getLanguage("common.notnull")).append(";");
                hasErr = true;
              }
              // so luong
              if (!StringUtils.isStringNullOrEmpty(obj1[2])) {
                if (!obj1[2].toString().trim().matches(REGEX_NUMBER)) {
                  errStr.append(I18n.getLanguage("wo.infoAsset.amount.val")).append(";");
                  hasErr = true;
                } else {
                  if (obj1[2].toString().trim().contains(",")) {
                    errStr.append(I18n.getLanguage("wo.infoAsset.amount.val")).append(";");
                    hasErr = true;
                  } else {
                    Double aMountCompare = Double.parseDouble(obj1[2].toString().trim());
                    if (Double.compare(0.0d, aMountCompare) == 0) {
                      errStr.append(I18n.getLanguage("wo.infoAsset.amount.val")).append(";");
                      checkDauPhay = true;
                      hasErr = true;
                    }
                    woMerchandiseInsideDTO
                        .setQuantity(Double.parseDouble(obj1[2].toString().trim()));
                  }
                }
                woMerchandiseDTO.setQuantity(obj1[2].toString().trim());
              } else {
                errStr.append(I18n.getLanguage("wo.infoAsset.amount")).append(" ")
                    .append(I18n.getLanguage("common.notnull")).append(";");
                hasErr = true;
              }

              StringBuilder content = new StringBuilder();
              StringBuilder description = new StringBuilder();
              // Ma tram nguon
              if (!StringUtils.isStringNullOrEmpty(obj1[3])) {
                woMerchandiseInsideDTO.setCodeStationSource(obj1[3].toString().trim());
                woMerchandiseDTO.setCodeStationSource(obj1[3].toString().trim());
                content.append(listInfoWo[0].toString().trim()).append(" ")
                    .append(I18n.getLanguage("wo.infoAsset.import.downgradeStation.text"))
                    .append(obj1[3].toString().trim()).append(" ");
                if (!StringUtils.isStringNullOrEmpty(listInfoWo[1])) {
                  description.append(listInfoWo[1].toString().trim()).append(" ");
                }
                // check vat tu co ton tai ha cap
                if (!StringUtils.isStringNullOrEmpty(obj1[1]) && !StringUtils
                    .isStringNullOrEmpty(obj1[2]) && obj1[2].toString().trim().matches(REGEX_NUMBER)
                    && checkDauPhay == false) {
                  if (errorCodeStation.containsKey(obj1[3].toString().trim())) {
//                    errStr.append(errorCodeStation.get(obj1[3].toString().trim())).append(";");
                    hasErr = true;
                    checkMaTramNguon = false;
                  } else {
                    String message = listErrorVatTu
                        .get(obj1[3].toString().trim() + "_" + obj1[1].toString().trim());
                    if (!StringUtils.isStringNullOrEmpty(message)) {
                      if (!message.contains("(")) {
                        String[] listMessageSoLuong = message.split(";");
                        errStr.append(listMessageSoLuong[0]).append(";");
                        hasErr = true;
                      } else {
                        String[] listMessageSoLuong = message.split(";");
                        for (int i = 0; i < listMessageSoLuong.length; i++) {
                          if (listMessageSoLuong[i].contains("(") && listMessageSoLuong[i]
                              .contains("(" + obj1[2].toString().trim() + ".0)")) {
                            errStr.append(listMessageSoLuong[i]).append(";");
                            hasErr = true;
                            break;
                          } else if (!listMessageSoLuong[i].contains("(")) {
                            errStr.append(listMessageSoLuong[i]).append(";");
                            hasErr = true;
                            break;
                          }
                        }
                      }
                    }

                  }
                }
                // check ma tram nguon co ton tai
                List<CatStationBO> list = mapObMatramNguonList.get(obj1[3].toString().trim());
                if (list.size() > 0) {
                  for (CatStationBO catStationBO : list) {
                    if (obj1[3].toString().trim().equals(catStationBO.getStationCode())) {
                      checkMaTramNguon = true;
                      break;
                    }
                  }
                }
                if (checkMaTramNguon == false || list.size() < 1) {
                  errStr.append(I18n.getLanguage("wo.infoAsset.codeStationSource")).append(" ")
                      .append(I18n.getLanguage("wo.infoAsset.validExist")).append(";");
                  hasErr = true;
                }
                description
                    .append(I18n.getLanguage("wo.infoAsset.import.downgradeStation.text"))
                    .append(" ").append(obj1[3].toString().trim()).append(" ");
              } else {
                errStr.append(I18n.getLanguage("wo.infoAsset.codeStationSource")).append(" ")
                    .append(I18n.getLanguage("common.notnull")).append(";");
                hasErr = true;
              }

              // Ma tram dich
              if (!StringUtils.isStringNullOrEmpty(obj1[4])) {
                woMerchandiseInsideDTO.setCodeStationDestination(obj1[4].toString().trim());
                woMerchandiseDTO.setCodeStationDestination(obj1[4].toString().trim());
                content.append(I18n.getLanguage("wo.infoAsset.import.stationupgrade.text"))
                    .append(obj1[4].toString().trim());
                description.append(I18n.getLanguage("wo.infoAsset.import.stationupgrade.text"))
                    .append(obj1[4].toString().trim());
                woDTO.setWoContent(content.toString());
                woDTO.setWoDescription(description.toString());
                // check ma tram dich co ton tai
                List<CatStationBO> list = mapObMatramDichList.get(obj1[4].toString().trim());
                if (list.size() > 0) {
                  for (CatStationBO catStationBO : list) {
                    if (obj1[4].toString().trim().equals(catStationBO.getStationCode())) {
                      checkMaTramDich = true;
                      break;
                    }
                  }
                }
                if (checkMaTramDich == false || list.size() < 1) {
                  errStr.append(I18n.getLanguage("wo.infoAsset.codeStationDestination")).append(" ")
                      .append(I18n.getLanguage("wo.infoAsset.validExist")).append(";");
                  hasErr = true;
                }
                if (!StringUtils.isStringNullOrEmpty(obj1[3])) {
                  woDTO.setContentUpgradeAndDownGrade(
                      obj1[3].toString().trim() + " " + obj1[4].toString().trim());
                }
              } else {
                errStr.append(I18n.getLanguage("wo.infoAsset.codeStationDestination")).append(" ")
                    .append(I18n.getLanguage("common.notnull")).append(";");
                hasErr = true;
              }
              // lay nhom dieu phoi(start)
              WoCdGroupInsideDTO woCdGroup = null;
              WoCdGroupInsideDTO woCdGroup1 = null;
              if (!StringUtils.isStringNullOrEmpty(obj1[5])) {
                woMerchandiseInsideDTO.setHomeStationCodeDowngrade(obj1[5].toString().trim());
                woMerchandiseDTO.setHomeStationCodeDowngrade(obj1[5].toString().trim());
                if (!StringUtils.isStringNullOrEmpty(obj1[5]) && checkMaTramNguon == true) {
                  woCdGroup = mapObDieuPhoiNguonList.get(obj1[5].toString().trim());
                  if (woCdGroup == null) {
                    errStr.append(I18n.getLanguage("wo.infoAsset.cdIdNotExistDowngrade"))
                        .append(";");
                    hasErr = true;
                  }
                }
              } else {
                errStr.append(I18n.getLanguage("wo.infoAsset.import.homeStationCodeDowngrade"))
                    .append(" ")
                    .append(I18n.getLanguage("common.notnull")).append(";");
                hasErr = true;
              }
              if (!StringUtils.isStringNullOrEmpty(obj1[6])) {
                woMerchandiseInsideDTO.setHomeStationCodeUpgrade(obj1[6].toString().trim());
                woMerchandiseDTO.setHomeStationCodeUpgrade(obj1[6].toString().trim());
                if (!StringUtils.isStringNullOrEmpty(obj1[6]) && checkMaTramDich == true) {
                  woCdGroup1 = mapObDieuPhoiDichList.get(obj1[6].toString().trim());
                  if (woCdGroup1 == null) {
                    errStr.append(I18n.getLanguage("wo.infoAsset.cdIdNotExistUpgrade")).append(";");
                    hasErr = true;
                  }
                }
              } else {
                errStr.append(I18n.getLanguage("wo.infoAsset.import.homeStationCodeUpgrade"))
                    .append(" ")
                    .append(I18n.getLanguage("common.notnull")).append(";");
                hasErr = true;
              }
              woDTO.setWoSystem("WFM-FT");
//              woDTO.setPriorityId("4000");
              woDTO.setStartTime(listInfoWo[2].toString().trim());
              woDTO.setEndTime(listInfoWo[3].toString().trim());
              if (!StringUtils.isStringNullOrEmpty(listInfoWo[4])) {
                woDTO.setPlanCode(listInfoWo[4].toString().trim());
              }
              woDTO.setStatus("0");
              woDTO.setPriorityCode("WO_PRIORITY_WO_PRIORITY_MAJOR");

              // kiem trau Duplicate
              if (StringUtils.isStringNullOrEmpty(errStr)) {
                String result = resultValidateDuplicate(woMerchandiseInsideDTO,
                    woMerchandiseInsideDTOList, getTypeNameFile);
                if (!StringUtils.isStringNullOrEmpty(result)) {
                  errStr.append(result).append(";");
                  hasErr = true;
                  woMerchandiseInsideDTO.setResultImport(errStr.toString());
                }
              }
              // lay nhom dieu phoi(end)
              woMerchandiseInsideDTO.setResultImport(errStr.toString());
              woMerchandiseDTO.setResultImport(errStr.toString());
              if (StringUtils.isStringNullOrEmpty(woMerchandiseInsideDTO.getResultImport())) {
                if (woCdGroup != null && woCdGroup1 != null) {
                  woMerchandiseInsideDTO
                      .setResultImport(I18n.getLanguage("common.import.validRecord"));
                  woMerchandiseDTO.setResultImport(I18n.getLanguage("common.import.validRecord"));
                  woDTO.setCdId(woCdGroup.getWoGroupId().toString());
                  woDTO.setWoTypeCode("TS_DIEU_CHUYEN_HA_CAP");
                  woDTO.setStationCode(obj1[3].toString().trim());
                  listImport.add(woDTO);

                  WoDTO woDTO1 = new WoDTO();
                  woDTO1.setWoContent(woDTO.getWoContent());
                  woDTO1.setWoDescription(woDTO.getWoDescription());
                  woDTO1.setWoSystem(woDTO.getWoSystem());
                  woDTO1.setPriorityId(woDTO.getPriorityId());
                  woDTO1.setStartTime(woDTO.getStartTime());
                  woDTO1.setEndTime(woDTO.getEndTime());
                  woDTO1.setPlanCode(woDTO.getPlanCode());
                  woDTO1.setStatus("0");
                  woDTO1.setContentUpgradeAndDownGrade(woDTO.getContentUpgradeAndDownGrade());
                  woDTO1.setCdId(woCdGroup1.getWoGroupId().toString());
                  woDTO1.setWoTypeCode("TS_DIEU_CHUYEN_NANG_CAP");
                  woDTO1.setPriorityCode(woDTO.getPriorityCode());
                  woDTO1.setStationCode(obj1[4].toString().trim());
                  listImport.add(woDTO1);
                }
              }

              woMerchandiseInsideDTOList.add(woMerchandiseInsideDTO);
              woMerchandiseDTOList.add(woMerchandiseDTO);
            }

            if (listImport.size() > 0 && hasErr == false) {
              // Xử lý list import wo bị trùng
              Map<String, WoDTO> map = new LinkedHashMap<>();
              for (WoDTO woDTO1 : listImport) {
                map.put(woDTO1.getContentUpgradeAndDownGrade() + " " + woDTO1.getWoTypeCode(),
                    woDTO1);
              }
              List<WoDTO> listAddImport = new ArrayList<>();
              for (Map.Entry<String, WoDTO> e : map.entrySet()) {
                listAddImport.add(e.getValue());
              }
              // remove resuilt import
              for (int i = 0; i < woMerchandiseInsideDTOList.size(); i++) {
                woMerchandiseInsideDTOList.get(i).setResultImport("");
              }
              // insert Wo
              List<String> lstSeq = null;
              String woCodeAdd = null;
              for (WoDTO dto : listAddImport) {
                if (StringUtils.isStringNullOrEmpty(dto.getResultImport())) {
                  // set SystemId
                  WoDTO woDto = new WoDTO();
                  woDto.setWoContent(dto.getWoContent());
                  woDto.setWoDescription(dto.getWoDescription());
                  woDto.setWoTypeCode(dto.getWoTypeCode());
                  woDto.setStatus(dto.getStatus());
                  woDto.setWoSystem(dto.getWoSystem());
                  woDto.setPlanCode(dto.getPlanCode());
                  woDto.setPriorityId(dto.getPriorityId());
                  woDto.setStationCode(dto.getStationCode());
                  woDto.setStartTime(dto.getStartTime());
                  woDto.setEndTime(dto.getEndTime());
                  woDto.setCdId(dto.getCdId());
                  woDto.setWoTypeCode(dto.getWoTypeCode());
                  woDto.setPriorityCode(dto.getPriorityCode());
                  woDto.setCreatePersonName(userToken.getUserName());
                  woDto.setCreateDate(DateTimeUtils
                      .converClientDateToServerDate(DateTimeUtils.getSysDateTime(), offset));
                  woDto.setContentUpgradeAndDownGrade(dto.getContentUpgradeAndDownGrade());
                  if (woDto.getWoTypeCode().equals("TS_DIEU_CHUYEN_HA_CAP")) {
                    lstSeq = woRepository.getListSequenseWo("WO_SEQ", 1);
                    Integer nextVal = Integer.parseInt(lstSeq.get(0));
                    woCodeAdd =
                        "WO_KTTS_" + DateUtil.date2StringNoSlash(new Date()) + "_" + nextVal;
                    woDto.setWoSystemId(woCodeAdd);
                  } else {
                    woDto.setWoSystemId(woCodeAdd);
                  }

                  try {
                    ResultDTO result = createWoForOtherSystem(woDto);
                    if (result != null && RESULT.SUCCESS.equals(result.getMessage())) {
                      String id[] = result.getId().split("_");
                      // Luu vat tu
                      if (lstDataAttachments != null && lstDataAttachments.size() > 0) {
                        for (int i = 0; i < lstDataAttachments.size(); i++) {
                          if (woDto.getContentUpgradeAndDownGrade()
                              .equals(lstDataAttachments.get(i)[3].toString().trim() + " "
                                  + lstDataAttachments.get(i)[4].toString().trim())) {
                            WoMerchandiseInsideDTO woMerchandiseInsideDTO1 = new WoMerchandiseInsideDTO();
                            woMerchandiseInsideDTO1
                                .setMerchandiseCode(lstDataAttachments.get(i)[1].toString().trim());
                            woMerchandiseInsideDTO1.setQuantity(
                                Double.parseDouble(lstDataAttachments.get(i)[2].toString().trim()));
                            woMerchandiseInsideDTO1
                                .setWoId(Long.parseLong(id[id.length - 1].trim()));
                            ResultInSideDto resultInSideDto = woMerchandiseRepository
                                .insertWoMerchandise(woMerchandiseInsideDTO1);
                            if (resultInSideDto == null || !RESULT.SUCCESS
                                .equals(resultInSideDto.getKey())) {
                              hasErr = true;
                              dto.setResultImport(resultInSideDto.getMessage());
                              break;
                            } else {
                              // thêm vào kết quả cột wo
                              StringBuilder stringBuilder = new StringBuilder();
                              stringBuilder.append(I18n.getLanguage("wo.detail.title.woCode"))
                                  .append(": ").append(result.getId());
                              if (StringUtils.isStringNullOrEmpty(
                                  woMerchandiseInsideDTOList.get(i).getResultImport())) {
                                woMerchandiseInsideDTOList.get(i)
                                    .setResultImport(stringBuilder.toString());
                              } else {
                                woMerchandiseInsideDTOList.get(i).setResultImport(
                                    woMerchandiseInsideDTOList.get(i).getResultImport() + "; "
                                        + result.getId());
                              }

                            }
                          }
                        }
                        // luu thong tin KTTS
                        WoKTTSInfoDTO woKttsInfo = new WoKTTSInfoDTO();
                        woKttsInfo.setWoId(Long.parseLong(id[id.length - 1].toString().trim()));
                        ResultInSideDto resultInSideDto = woKTTSInfoRepository
                            .insertWoKTTSInfo(woKttsInfo);
                        if (resultInSideDto == null || !RESULT.SUCCESS
                            .equals(resultInSideDto.getKey())) {
                          hasErr = true;
                          dto.setResultImport(resultInSideDto.getMessage());
                          break;
                        }
                      }
                    }

                  } catch (Exception e) {
                    log.error(e.getMessage(), e);
                    hasErr = true;
                    dto.setResultImport(e.getMessage());
                    for (int i = 0; i < lstDataAttachments.size(); i++) {
                      if (woDto.getContentUpgradeAndDownGrade()
                          .equals(lstDataAttachments.get(i)[3].toString().trim() + " "
                              + lstDataAttachments.get(i)[4].toString().trim())) {
                        woMerchandiseInsideDTOList.get(i).setResultImport(e.getMessage());
                        woMerchandiseDTOList.get(i).setResultImport(e.getMessage());
                      }
                    }
                  }
                }

              }
            } else {
              resultInSideDTO.setKey(RESULT.ERROR);
              resultInSideDTO.setMessage(I18n.getLanguage("import.common.fail"));
            }

          }
          //</editor-fold>

          //<editor-fold desc="File DDrawal" defaultstate="collapsed">

          if (getTypeNameFile.equals(I18n.getLanguage("wo.infoAsset.formDataJson.DDrawal"))) {
            String nation = getNationFromUnit(userToken.getDeptId());
            Map<String, Object[]> mapObVatTu = new HashMap<>();
            Map<String, Object[]> mapObMatram = new HashMap();
            Map<String, Object[]> mapObMakho = new HashMap();
            Map<String, Object[]> mapObMaNhatram = new HashMap();
            Map<String, List<CatStationBO>> mapObMatramList = new HashMap();
            Map<String, Kttsbo> mapObMakhoList = new HashMap();
            Map<String, WoCdGroupInsideDTO> mapObDieuPhoiList = new HashMap();
            List<Object[]> objects = new ArrayList<>();
            Map<String, List<MerchandiseBO>> listVatTuVaSoLuong = new HashMap();
            List<Object[]> listVatTuOb = new ArrayList<>();
            String listVatTu = "";
            String listError = null;
            for (Object[] obj1 : lstDataAttachments) {
              if (!StringUtils.isStringNullOrEmpty(obj1[3])) {
                mapObMatram.put(obj1[3].toString().trim(), obj1);
              }
              if (!StringUtils.isStringNullOrEmpty(obj1[4])) {
                mapObMakho.put(obj1[4].toString().trim(), obj1);
              }
              if (!StringUtils.isStringNullOrEmpty(obj1[5])) {
                mapObMaNhatram.put(obj1[5].toString().trim(), obj1);
              }
            }
            // Kiem tra vat tu co ton tai trong tram(Start)
            // loc cac ban ghi trung so luong, ma vat tu va ma tram
            for (Object[] obj1 : lstDataAttachments) {
              if (!StringUtils.isStringNullOrEmpty(obj1[1]) && !StringUtils
                  .isStringNullOrEmpty(obj1[2]) && obj1[2].toString().trim().matches(REGEX_NUMBER)
                  && !StringUtils.isStringNullOrEmpty(obj1[3]) && !obj1[2].toString().trim()
                  .contains(",")) {
                mapObVatTu.put(
                    obj1[1].toString().trim() + "_" + (obj1[2].toString().trim()) + "_" + (obj1[3]
                        .toString().trim()), obj1);
              }
            }
            for (Map.Entry<String, Object[]> e : mapObVatTu.entrySet()) {
              Object[] objects1 = e.getValue();
              listVatTuOb.add(objects1);
            }
            // loc cac ban ghi cung ma tram nhung khac list vat tu

            Map<String, String> errorCodeStation = new HashedMap();
            Map<String, String> listErrorVatTu = new HashMap();

            Kttsbo resultCheckKtts = null;
            for (Object[] obj1 : listVatTuOb) {
              if (listVatTuVaSoLuong.containsKey(obj1[3].toString().trim())) {
                List<MerchandiseBO> lstMercodeBo = new ArrayList<>();
                MerchandiseBO bo = new MerchandiseBO();
                bo.setCount(Double.parseDouble(obj1[2].toString().trim()));
                bo.setMerCode(obj1[1].toString().trim());
                List<MerchandiseBO> lstMercodeBo1 = listVatTuVaSoLuong
                    .get(obj1[3].toString().trim());
                if (lstMercodeBo1.size() > 0) {
                  lstMercodeBo1.add(bo);
                  listVatTuVaSoLuong.put(obj1[3].toString().trim(), lstMercodeBo1);
                } else {
                  lstMercodeBo.add(bo);
                  listVatTuVaSoLuong.put(obj1[3].toString().trim(), lstMercodeBo);
                }

              } else {
                List<MerchandiseBO> lstMercodeBo = new ArrayList<>();
                MerchandiseBO bo = new MerchandiseBO();
                bo.setCount(Double.parseDouble(obj1[2].toString().trim()));
                bo.setMerCode(obj1[1].toString().trim());
                lstMercodeBo.add(bo);
                listVatTuVaSoLuong.put(obj1[3].toString().trim(), lstMercodeBo);
              }
            }
            // Goi ham check vat tu trong tram
            for (Map.Entry<String, List<MerchandiseBO>> e : listVatTuVaSoLuong.entrySet()) {
              List<MerchandiseBO> lstMercodeBo1 = e.getValue();
              resultCheckKtts = kttsVsmartPort
                  .checkListMerEntityNation(lstMercodeBo1, e.getKey(),
                      nation);
              if (resultCheckKtts.getStatus().equals("Error")) {
                if (resultCheckKtts.getErrorCode() != null && resultCheckKtts.getErrorCode()
                    .equals("NOT_EXIST_STATION_CODE")) {
                  errorCodeStation.put(e.getKey(), resultCheckKtts.getErrorInfo());
                } else if (resultCheckKtts.getErrorCode() != null && resultCheckKtts.getErrorCode()
                    .equals("LIST_ENTITY_IS_INVALID")) {
                  for (ErrorBO errorBO : resultCheckKtts.getListError()) {
                    String soLuong = errorBO.getDescription();
                    String key = e.getKey() + "_" + errorBO.getMerCode();
                    if (!StringUtils.isStringNullOrEmpty(listErrorVatTu.get(key))) {
                      listErrorVatTu
                          .put(key, listErrorVatTu.get(key) + errorBO.getDescription() + ";");
                    } else {
                      listErrorVatTu.put(key, errorBO.getDescription() + ";");
                    }
                  }
                }
              }
            }

            // Kiem tra vat tu co ton tai trong tram(End)
            // Kiem tra ma tram(Start)
            for (Map.Entry<String, Object[]> e : mapObMatram.entrySet()) {
              Object[] objects1 = e.getValue();
              List<CatStationBO> listEmpty = new ArrayList<>();
              List<CatStationBO> list = getStationListNation(objects1[3].toString().trim(), null);
              if (list.size() > 0) {
                mapObMatramList.put(objects1[3].toString().trim(), list);
              } else {
                mapObMatramList.put(objects1[3].toString().trim(), listEmpty);
              }
            }
            // Kiem tra ma tram(End)

            // Kiểm tra ma kho xuat(Start)
            for (Map.Entry<String, Object[]> e : mapObMakho.entrySet()) {
              Object[] objects1 = e.getValue();
              Kttsbo kttsbo = kttsVsmartPort
                  .getListWarehouseNation(objects1[4].toString().trim(), null, null, null, nation);
              mapObMakhoList.put(objects1[4].toString().trim(), kttsbo);
            }
            // Kiểm tra ma kho xuat(Start)

            // Kiểm tra mã nhóm điều phối(Start)
            String valueMatram = mapConfigProperty.get("WO.TYPE.CHECK.QLTS.THUHOI");
            for (Map.Entry<String, Object[]> e : mapObMaNhatram.entrySet()) {
              if(!StringUtils.isStringNullOrEmpty(valueMatram)){
                WoCdGroupInsideDTO woCdGroup123 = woCdGroupBusiness
                    .getCdByStationCodeNation(e.getKey(), valueMatram, "4", null,
                        Constants.NATION_CODES.VNM);
                mapObDieuPhoiList.put(e.getKey(), woCdGroup123);
              }else{
                mapObDieuPhoiList.put(e.getKey(), null);
              }

            }
            // Kiểm tra mã nhóm điều phối(Start)

            for (Object[] obj1 : lstDataAttachments) {
              // khai bao
              WoDTO woDTO = new WoDTO();
              StringBuilder errStr = new StringBuilder();
              WoMerchandiseInsideDTO woMerchandiseInsideDTO = new WoMerchandiseInsideDTO();
              WoMerchandiseDTO woMerchandiseDTO = new WoMerchandiseDTO();
              Boolean checkDauPhay = false;
              boolean checkMaTramHaCap = false;
              if (!StringUtils.isStringNullOrEmpty(obj1[1])) {
                woMerchandiseInsideDTO.setMerchandiseCode(obj1[1].toString().trim());
                woMerchandiseDTO.setMerchandiseCode(obj1[1].toString().trim());
              } else {
                errStr.append(I18n.getLanguage("wo.infoAsset.codeMaterial")).append(" ")
                    .append(I18n.getLanguage("common.notnull")).append(";");
                hasErr = true;
              }

              // so luong
              if (!StringUtils.isStringNullOrEmpty(obj1[2])) {
                if (!obj1[2].toString().trim().matches(REGEX_NUMBER)) {
                  errStr.append(I18n.getLanguage("wo.infoAsset.amount.val")).append(";");
                  hasErr = true;
                } else {
                  if (obj1[2].toString().trim().contains(",")) {
                    checkDauPhay = true;
                    errStr.append(I18n.getLanguage("wo.infoAsset.amount.val")).append(";");
                    hasErr = true;
                  } else {
                    Double aMountCompare = Double.parseDouble(obj1[2].toString().trim());
                    if (Double.compare(0.0d, aMountCompare) == 0) {
                      errStr.append(I18n.getLanguage("wo.infoAsset.amount.val")).append(";");
                      hasErr = true;
                    }
                    woMerchandiseInsideDTO
                        .setQuantity(Double.parseDouble(obj1[2].toString().trim()));
                  }
                }
                woMerchandiseDTO.setQuantity(obj1[2].toString().trim());

              } else {
                errStr.append(I18n.getLanguage("wo.infoAsset.amount")).append(" ")
                    .append(I18n.getLanguage("common.notnull")).append(";");
                hasErr = true;
              }
              WoCdGroupInsideDTO cdTmp = new WoCdGroupInsideDTO();
              StringBuilder content = new StringBuilder();
              StringBuilder description = new StringBuilder();
              // Ma tram ha cap
              if (!StringUtils.isStringNullOrEmpty(obj1[3])) {
                woMerchandiseInsideDTO.setDowngradeStationCode(obj1[3].toString().trim());
                woMerchandiseDTO.setDowngradeStationCode(obj1[3].toString().trim());
                content.append(listInfoWo[0].toString().trim()).append(" ")
                    .append(I18n.getLanguage("wo.infoAsset.downgradeStation")).append(": ")
                    .append(obj1[3].toString().trim()).append(" ");
                if (!StringUtils.isStringNullOrEmpty(listInfoWo[1])) {
                  description.append(listInfoWo[1].toString().trim()).append(" ");
                }

                // check vat tu co ton tai(ha cap hoac thu hoi)
                if (!StringUtils.isStringNullOrEmpty(obj1[1]) && !StringUtils
                    .isStringNullOrEmpty(obj1[2]) && obj1[2].toString().trim().matches(REGEX_NUMBER)
                    && checkDauPhay == false) {
                  if (errorCodeStation.containsKey(obj1[3].toString().trim())) {
                    errStr.append(errorCodeStation.get(obj1[3].toString().trim())).append(";");
                    hasErr = true;
                    checkMaTramHaCap = true;
                  } else {
                    String message = listErrorVatTu
                        .get(obj1[3].toString().trim() + "_" + obj1[1].toString().trim());
                    if (!StringUtils.isStringNullOrEmpty(message)) {
                      if (!message.contains("(")) {
                        String[] listMessageSoLuong = message.split(";");
                        errStr.append(listMessageSoLuong[0]).append(";");
                        hasErr = true;
                      } else {
                        String[] listMessageSoLuong = message.split(";");
                        for (int i = 0; i < listMessageSoLuong.length; i++) {
                          if (listMessageSoLuong[i].contains("(") && listMessageSoLuong[i]
                              .contains("(" + obj1[2].toString().trim() + ".0)")) {
                            errStr.append(listMessageSoLuong[i]).append(";");
                            hasErr = true;
                            break;
                          } else if (!message.contains("(")) {
                            errStr.append(listMessageSoLuong[i]).append(";");
                            hasErr = true;
                            break;
                          }
                        }
                      }
                    }

                  }
                }

//                Map<String, List<MerchandiseBO>> errorCodeStation = new HashedMap();
                cdTmp.setGroupTypeId(4L);
                cdTmp.setWoGroupCode(obj1[3].toString());
                description
                    .append(I18n.getLanguage("wo.infoAsset.downgradeStation")).append(": ")
                    .append(obj1[3].toString().trim()).append(" ");
              } else {
                errStr.append(I18n.getLanguage("wo.infoAsset.import.downgradeStationCode1"))
                    .append(" ")
                    .append(I18n.getLanguage("common.notnull")).append(";");
                hasErr = true;
              }

              // Ma kho nhap
              if (!StringUtils.isStringNullOrEmpty(obj1[4])) {
                Boolean check = false;
                Kttsbo kttsbo = mapObMakhoList.get(obj1[4].toString().trim());
                if (kttsbo.getListWarehouse() != null && kttsbo.getListWarehouse().size() > 0) {
                  for (int i = 0; i < kttsbo.getListWarehouse().size(); i++) {
                    if (obj1[4].toString().trim()
                        .equals(kttsbo.getListWarehouse().get(i).getCode())) {
                      check = true;
                      break;
                    }
                  }
                }
                if (check == false) {
                  errStr.append(I18n.getLanguage("wo.infoAsset.import.entryCode")).append(" ")
                      .append(I18n.getLanguage("wo.infoAsset.validExist")).append(";");
                  hasErr = true;
                }
                woMerchandiseInsideDTO.setEntryCode(obj1[4].toString().trim());
                woMerchandiseDTO.setEntryCode(obj1[4].toString().trim());
                woDTO.setWoContent(content.toString());
                woDTO.setWarehouseCode(obj1[4].toString().trim());
                woDTO.setWoDescription(description.toString());
                if (!StringUtils.isStringNullOrEmpty(obj1[3])) {
                  woDTO.setContentUpgradeAndDownGrade(
                      obj1[3].toString().trim() + " " + obj1[4].toString().trim());
                }
              } else {
                errStr.append(I18n.getLanguage("wo.infoAsset.import.entryCode")).append(" ")
                    .append(I18n.getLanguage("common.notnull")).append(";");
                hasErr = true;
              }
              WoCdGroupInsideDTO woCdGroup = null;
              if (!StringUtils.isStringNullOrEmpty(obj1[5])) {
                // lay nhom dieu phoi
                woMerchandiseInsideDTO.setHomeStationCodeDowngrade(obj1[5].toString().trim());
                woMerchandiseDTO.setHomeStationCodeDowngrade(obj1[5].toString().trim());
                if (!StringUtils.isStringNullOrEmpty(obj1[5]) && checkMaTramHaCap == false) {
                  woCdGroup = mapObDieuPhoiList.get(obj1[5].toString().trim());
                  if (woCdGroup == null) {
                    errStr.append(I18n.getLanguage("wo.infoAsset.cdIdDontNotExist")).append(";");
                    hasErr = true;
                  }
                }
              } else {
                errStr.append(I18n.getLanguage("wo.infoAsset.import.homeStationCodeDowngrade"))
                    .append(" ")
                    .append(I18n.getLanguage("common.notnull")).append(";");
                hasErr = true;
              }

              woDTO.setWoSystem("WFM-FT");
//              woDTO.setPriorityId("4000");
              woDTO.setStartTime(listInfoWo[2].toString().trim());
              woDTO.setEndTime(listInfoWo[3].toString().trim());
              if (!StringUtils.isStringNullOrEmpty(listInfoWo[4])) {
                woDTO.setPlanCode(listInfoWo[4].toString().trim());
              }
              woDTO.setStatus("0");
              woDTO.setPriorityCode("WO_PRIORITY_WO_PRIORITY_MAJOR");

              // kiem trau Duplicate
              if (StringUtils.isStringNullOrEmpty(errStr)) {
                String result = resultValidateDuplicate(woMerchandiseInsideDTO,
                    woMerchandiseInsideDTOList, getTypeNameFile);
                if (!StringUtils.isStringNullOrEmpty(result)) {
                  errStr.append(result).append(";");
                  hasErr = true;
                  woMerchandiseInsideDTO.setResultImport(errStr.toString());
                }
              }
              woMerchandiseInsideDTO.setResultImport(errStr.toString());
              woMerchandiseDTO.setResultImport(errStr.toString());
              if (StringUtils.isStringNullOrEmpty(woMerchandiseInsideDTO.getResultImport())) {
                if (woCdGroup != null) {
                  woMerchandiseInsideDTO
                      .setResultImport(I18n.getLanguage("common.import.validRecord"));
                  woDTO.setCdId(woCdGroup.getWoGroupId().toString());
                }
                woMerchandiseDTO
                    .setResultImport(I18n.getLanguage("common.import.validRecord"));
                woDTO.setWoTypeCode("TS_HA_CAP_TRAM");
                woDTO.setStationCode(obj1[3].toString().trim());
                listImport.add(woDTO);
              }

              woMerchandiseInsideDTOList.add(woMerchandiseInsideDTO);
              woMerchandiseDTOList.add(woMerchandiseDTO);
            }

            if (listImport.size() > 0 && hasErr == false) {
              // Xử lý list import wo bị trùng
              Map<String, WoDTO> map = new HashedMap();
              for (WoDTO woDTO1 : listImport) {
                map.put(woDTO1.getContentUpgradeAndDownGrade(), woDTO1);
              }
              List<WoDTO> listAddImport = new ArrayList<>();
              for (Map.Entry<String, WoDTO> e : map.entrySet()) {
                listAddImport.add(e.getValue());
              }
              // insert Wo
              for (WoDTO dto : listAddImport) {
                if (StringUtils.isStringNullOrEmpty(dto.getResultImport())) {
                  WoDTO woDto = new WoDTO();
                  woDto.setWoContent(dto.getWoContent());
                  woDto.setWoDescription(dto.getWoDescription());
                  woDto.setWoTypeCode(dto.getWoTypeCode());
                  woDto.setStatus(dto.getStatus());
                  woDto.setWoSystem(dto.getWoSystem());
                  woDto.setPlanCode(dto.getPlanCode());
                  woDto.setPriorityId(dto.getPriorityId());
                  woDto.setStationCode(dto.getStationCode());
                  woDto.setStartTime(dto.getStartTime());
                  woDto.setEndTime(dto.getEndTime());
                  woDto.setCdId(dto.getCdId());
                  woDto.setWarehouseCode(dto.getWarehouseCode());
                  woDto.setPriorityCode(dto.getPriorityCode());
                  woDto.setCreatePersonName(userToken.getUserName());
                  woDto.setContentUpgradeAndDownGrade(dto.getContentUpgradeAndDownGrade());
                  woDto.setCreateDate(DateTimeUtils
                      .converClientDateToServerDate(DateTimeUtils.getSysDateTime(), offset));
                  try {
                    ResultDTO result = createWoForOtherSystem(woDto);
                    if (result != null && RESULT.SUCCESS.equals(result.getMessage())) {
                      String id[] = result.getId().split("_");
                      // Luu vat tu
                      if (lstDataAttachments != null && lstDataAttachments.size() > 0) {
                        for (int i = 0; i < lstDataAttachments.size(); i++) {
                          if (woDto.getContentUpgradeAndDownGrade()
                              .equals(lstDataAttachments.get(i)[3].toString().trim() + " "
                                  + lstDataAttachments.get(i)[4].toString().trim())) {
                            WoMerchandiseInsideDTO woMerchandiseInsideDTO1 = new WoMerchandiseInsideDTO();
                            woMerchandiseInsideDTO1
                                .setMerchandiseCode(lstDataAttachments.get(i)[1].toString().trim());
                            woMerchandiseInsideDTO1.setQuantity(
                                Double.parseDouble(lstDataAttachments.get(i)[2].toString().trim()));
                            woMerchandiseInsideDTO1
                                .setWoId(Long.parseLong(id[id.length - 1].toString().trim()));
                            ResultInSideDto resultInSideDto = woMerchandiseRepository
                                .insertWoMerchandise(woMerchandiseInsideDTO1);
                            if (resultInSideDto == null || !RESULT.SUCCESS
                                .equals(resultInSideDto.getKey())) {
                              hasErr = true;
                              dto.setResultImport(resultInSideDto.getMessage());
                              break;
                            } else {
                              // thêm vào kết quả cột wo
                              StringBuilder stringBuilder = new StringBuilder();
                              stringBuilder.append(I18n.getLanguage("wo.detail.title.woCode"))
                                  .append(": ").append(result.getId());
                              woMerchandiseInsideDTOList.get(i)
                                  .setResultImport(stringBuilder.toString());
                            }
                          }
                        }
                        // luu thong tin KTTS
                        WoKTTSInfoDTO woKttsInfo = new WoKTTSInfoDTO();
                        woKttsInfo.setWoId(Long.parseLong(id[id.length - 1].toString().trim()));
                        ResultInSideDto resultInSideDto = woKTTSInfoRepository
                            .insertWoKTTSInfo(woKttsInfo);
                        if (resultInSideDto == null || !RESULT.SUCCESS
                            .equals(resultInSideDto.getKey())) {
                          hasErr = true;
                          dto.setResultImport(resultInSideDto.getMessage());
                          break;
                        }
                      }
                    }
                  } catch (Exception e) {
                    log.error(e.getMessage(), e);
                    hasErr = true;
                    dto.setResultImport(e.getMessage());
                    for (int i = 0; i < lstDataAttachments.size(); i++) {
                      if (woDto.getContentUpgradeAndDownGrade()
                          .equals(lstDataAttachments.get(i)[3].toString().trim() + " "
                              + lstDataAttachments.get(i)[4].toString().trim())) {
                        woMerchandiseInsideDTOList.get(i).setResultImport(e.getMessage());
                        woMerchandiseDTOList.get(i).setResultImport(e.getMessage());
                      }
                    }
                  }
                } else {
                  resultInSideDTO.setKey(RESULT.ERROR);
                  resultInSideDTO.setMessage(I18n.getLanguage("import.common.fail"));
                }
              }
            }
          }
          //</editor-fold>

          //<editor-fold desc="File UpStaion" defaultstate="collapsed">
          if (getTypeNameFile.equals(I18n.getLanguage("wo.infoAsset.formDataJson.UpStaion"))) {
            String nation = getNationFromUnit(userToken.getDeptId());
            Map<String, Object[]> mapObVatTu = new HashMap();
            Map<String, Object[]> mapObMatram = new HashMap();
            Map<String, Object[]> mapObMakho = new HashMap();
            Map<String, Object[]> mapObMatramNha = new HashMap();
            Map<String, List<CatStationBO>> mapObMatramList = new HashMap();
            Map<String, Kttsbo> mapObMakhoList = new HashMap();
            Map<String, WoCdGroupInsideDTO> mapObDieuPhoiList = new HashMap();
            List<Object[]> objects = new ArrayList<>();
            String listVatTu = "";
            String listError = null;
            for (Object[] obj1 : lstDataAttachments) {
              if (!StringUtils.isStringNullOrEmpty(obj1[1])) {
                mapObVatTu.put(obj1[1].toString().trim(), obj1);
              }
              if (!StringUtils.isStringNullOrEmpty(obj1[3])) {
                mapObMatram.put(obj1[3].toString().trim(), obj1);
              }
              if (!StringUtils.isStringNullOrEmpty(obj1[4])) {
                mapObMakho.put(obj1[4].toString().trim(), obj1);
              }
              if (!StringUtils.isStringNullOrEmpty(obj1[5])) {
                mapObMatramNha.put(obj1[5].toString().trim(), obj1);
              }
            }
            // Kiem tra vat tu(Start)
            for (Map.Entry<String, Object[]> e : mapObVatTu.entrySet()) {
              Object[] objects1 = e.getValue();
              listVatTu = listVatTu + objects1[1].toString().trim() + ",";
              objects.add(objects1);
            }
            try {
              Kttsbo resultCheckKtts = null;
              resultCheckKtts = kttsVsmartPort
                  .checkMerchandiseCodeNation(listVatTu, nation);
              if (resultCheckKtts != null && resultCheckKtts.getStatus().equals("Error")) {
                if (resultCheckKtts.getErrorInfo() != null) {
                  listError = resultCheckKtts.getErrorInfo();
                }
              }
            } catch (Exception e) {
              log.error(e.getMessage(), e);
              return new ResultInSideDto(null, "ERROR_NO_DOWNLOAD",
                  "KTTS:" + I18n.getLanguage("wo.haveSomeErrWhenCheckListMerchandise"));
            }
            // Kiem tra vat tu(End)

            // Kiem tra ma tram(Start)
            for (Map.Entry<String, Object[]> e : mapObMatram.entrySet()) {
              Object[] objects1 = e.getValue();
              List<CatStationBO> listEmpty = new ArrayList<>();
              List<CatStationBO> list = getStationListNation(objects1[3].toString().trim(), null);
              if (list.size() > 0) {
                mapObMatramList.put(objects1[3].toString().trim(), list);
              } else {
                mapObMatramList.put(objects1[3].toString().trim(), listEmpty);
              }
            }
            // Kiem tra ma tram(End)

            // Kiểm tra ma kho xuat(Start)
            for (Map.Entry<String, Object[]> e : mapObMakho.entrySet()) {
              Object[] objects1 = e.getValue();
              Kttsbo kttsbo = kttsVsmartPort
                  .getListWarehouseNation(objects1[4].toString().trim(), null, null, null, nation);
              mapObMakhoList.put(objects1[4].toString().trim(), kttsbo);
            }
            // Kiểm tra ma kho xuat(Start)

            // Kiểm tra mã nhóm điều phối(Start)
            String valueMatram = mapConfigProperty.get("WO.TYPE.CHECK.QLTS.NC");
            for (Map.Entry<String, Object[]> e : mapObMatramNha.entrySet()) {
              if(!StringUtils.isStringNullOrEmpty(valueMatram)){
                Object[] objects1 = e.getValue();
                WoCdGroupInsideDTO woCdGroup123 = woCdGroupBusiness
                    .getCdByStationCodeNation(e.getKey(), valueMatram, "4", null,
                        Constants.NATION_CODES.VNM);
                mapObDieuPhoiList.put(e.getKey(), woCdGroup123);
              }else{
                mapObDieuPhoiList.put(e.getKey(), null);
              }

            }
            // Kiểm tra mã nhóm điều phối(Start)

            for (Object[] obj1 : lstDataAttachments) {
              // khai bao
              WoDTO woDTO = new WoDTO();
              StringBuilder errStr = new StringBuilder();
              WoMerchandiseInsideDTO woMerchandiseInsideDTO = new WoMerchandiseInsideDTO();
              WoMerchandiseDTO woMerchandiseDTO = new WoMerchandiseDTO();
              boolean checkMaTramNangCap = false;
              if (!StringUtils.isStringNullOrEmpty(obj1[1])) {
                // Ma vat tu
                if (listError != null) {
                  if (listError.contains(" " + obj1[1].toString().trim() + ",")) {
                    errStr.append(I18n.getLanguage("wo.infoAsset.codeMaterial")).append(" ")
                        .append(I18n.getLanguage("wo.infoAsset.validExist")).append(";");
                    hasErr = true;
                  }
                }

                woMerchandiseInsideDTO.setMerchandiseCode(obj1[1].toString().trim());
                woMerchandiseDTO.setMerchandiseCode(obj1[1].toString().trim());
              } else {
                errStr.append(I18n.getLanguage("wo.infoAsset.codeMaterial")).append(" ")
                    .append(I18n.getLanguage("common.notnull")).append(";");
                hasErr = true;
              }
              // so luong
              if (!StringUtils.isStringNullOrEmpty(obj1[2])) {
                if (!obj1[2].toString().trim().matches(REGEX_NUMBER)) {
                  errStr.append(I18n.getLanguage("wo.infoAsset.amount.val")).append(";");
                  hasErr = true;
                } else {
                  if (obj1[2].toString().trim().contains(",")) {
                    errStr.append(I18n.getLanguage("wo.infoAsset.amount.val")).append(";");
                    hasErr = true;
                  } else {
                    Double aMountCompare = Double.parseDouble(obj1[2].toString().trim());
                    if (Double.compare(0.0d, aMountCompare) == 0) {
                      errStr.append(I18n.getLanguage("wo.infoAsset.amount.val")).append(";");
                      hasErr = true;
                    }
                    woMerchandiseInsideDTO
                        .setQuantity(Double.parseDouble(obj1[2].toString().trim()));
                  }
                }
                woMerchandiseDTO.setQuantity(obj1[2].toString().trim());
              } else {
                errStr.append(I18n.getLanguage("wo.infoAsset.amount")).append(" ")
                    .append(I18n.getLanguage("common.notnull")).append(";");
                hasErr = true;
              }
              StringBuilder content = new StringBuilder();
              StringBuilder description = new StringBuilder();
              WoCdGroupInsideDTO cdTmp = new WoCdGroupInsideDTO();
              // Ma tram nang cap
              if (!StringUtils.isStringNullOrEmpty(obj1[3])) {
                woMerchandiseInsideDTO.setCodeStationupgrade(obj1[3].toString().trim());
                woMerchandiseDTO.setCodeStationupgrade(obj1[3].toString().trim());
                content.append(listInfoWo[0].toString().trim()).append(" ")
                    .append("mã trạm nâng cấp: ")
                    .append(obj1[3].toString().trim()).append(" ");
                if (!StringUtils.isStringNullOrEmpty(listInfoWo[1])) {
                  description.append(listInfoWo[1].toString().trim()).append(" ");
                }
                // check ma tram co ton tai
                List<CatStationBO> list = mapObMatramList.get(obj1[3].toString().trim());
                if (list.size() > 0) {
                  for (CatStationBO catStationBO : list) {
                    if (obj1[3].toString().trim().equals(catStationBO.getStationCode())) {
                      checkMaTramNangCap = true;
                      break;
                    }
                  }
                }
                if (checkMaTramNangCap == false || list.size() < 1) {
                  errStr.append(I18n.getLanguage("wo.infoAsset.codeUpgrade")).append(" ")
                      .append(I18n.getLanguage("wo.infoAsset.validExist")).append(";");
                  hasErr = true;
                }

                cdTmp.setGroupTypeId(4L);
                cdTmp.setWoGroupCode(obj1[3].toString());
                description
                    .append(I18n.getLanguage("wo.infoAsset.import.codeStationupgrade")).append(": ")
                    .append(obj1[3].toString().trim()).append(" ");
              } else {
                errStr.append(I18n.getLanguage("wo.infoAsset.import.codeUpgrade")).append(" ")
                    .append(I18n.getLanguage("common.notnull")).append(";");
                hasErr = true;
              }

              // Ma kho xuat
              if (!StringUtils.isStringNullOrEmpty(obj1[4])) {
                Boolean check = false;
                Kttsbo kttsbo = mapObMakhoList.get(obj1[4].toString().trim());
                if (kttsbo.getListWarehouse() != null && kttsbo.getListWarehouse().size() > 0) {
                  for (int i = 0; i < kttsbo.getListWarehouse().size(); i++) {
                    if (obj1[4].toString().trim()
                        .equals(kttsbo.getListWarehouse().get(i).getCode())) {
                      check = true;
                      break;
                    }
                  }
                }
                if (check == false) {
                  errStr.append(I18n.getLanguage("wo.infoAsset.import.exportWarehouse")).append(" ")
                      .append(I18n.getLanguage("wo.infoAsset.validExist")).append(";");
                  hasErr = true;
                }
                if (!StringUtils.isStringNullOrEmpty(obj1[3])) {
                  woDTO.setContentUpgradeAndDownGrade(
                      obj1[3].toString().trim() + " " + obj1[4].toString().trim());
                }
                woMerchandiseInsideDTO.setExportWarehouse(obj1[4].toString().trim());
                woMerchandiseDTO.setExportWarehouse(obj1[4].toString().trim());
                woDTO.setWoContent(content.toString());
                woDTO.setWarehouseCode(obj1[4].toString().trim());
                woDTO.setWoDescription(description.toString());
              } else {
                errStr.append(I18n.getLanguage("wo.infoAsset.exportWarehouse")).append(" ")
                    .append(I18n.getLanguage("common.notnull")).append(";");
                hasErr = true;
              }
              WoCdGroupInsideDTO woCdGroup = null;
              // Ma nha tram
              if (!StringUtils.isStringNullOrEmpty(obj1[5])) {
                woMerchandiseInsideDTO.setHomeStationCodeUpgrade(obj1[5].toString().trim());
                woMerchandiseDTO.setHomeStationCodeUpgrade(obj1[5].toString().trim());
                // lay nhom dieu phoi
                if (!StringUtils.isStringNullOrEmpty(obj1[5]) && checkMaTramNangCap == true) {
                  woCdGroup = mapObDieuPhoiList.get(obj1[5].toString().trim());
                  if (woCdGroup == null) {
                    errStr.append(I18n.getLanguage("wo.infoAsset.cdIdNotExist"));
                    hasErr = true;
                  }
                }
              } else {
                errStr.append(I18n.getLanguage("wo.infoAsset.import.homeStationCodeUpgrade"))
                    .append(" ")
                    .append(I18n.getLanguage("common.notnull")).append(";");
                hasErr = true;
              }

              woDTO.setWoSystem("WFM-FT");
//              woDTO.setPriorityId("4000");
              woDTO.setStartTime(listInfoWo[2].toString().trim());
              woDTO.setEndTime(listInfoWo[3].toString().trim());
              if (!StringUtils.isStringNullOrEmpty(listInfoWo[4])) {
                woDTO.setPlanCode(listInfoWo[4].toString().trim());
              }
              woDTO.setStatus("0");
              woDTO.setPriorityCode("WO_PRIORITY_WO_PRIORITY_MAJOR");

              // kiem trau Duplicate
              if (StringUtils.isStringNullOrEmpty(errStr)) {
                String result = resultValidateDuplicate(woMerchandiseInsideDTO,
                    woMerchandiseInsideDTOList, getTypeNameFile);
                if (!StringUtils.isStringNullOrEmpty(result)) {
                  errStr.append(result).append(";");
                  hasErr = true;
                  woMerchandiseInsideDTO.setResultImport(errStr.toString());
                }
              }
              woMerchandiseDTO.setResultImport(errStr.toString());
              woMerchandiseInsideDTO.setResultImport(errStr.toString());
              if (StringUtils.isStringNullOrEmpty(woMerchandiseInsideDTO.getResultImport())) {
                if (woCdGroup != null) {
                  woMerchandiseInsideDTO
                      .setResultImport(I18n.getLanguage("common.import.validRecord"));
                  woMerchandiseDTO
                      .setResultImport(I18n.getLanguage("common.import.validRecord"));
                  woDTO.setCdId(woCdGroup.getWoGroupId().toString());
                  woDTO.setWoTypeCode("TS_NANG_CAP_TRAM");
                  woDTO.setStationCode(obj1[3].toString().trim());
                  listImport.add(woDTO);
                }
              }

              woMerchandiseInsideDTOList.add(woMerchandiseInsideDTO);
              woMerchandiseDTOList.add(woMerchandiseDTO);
            }

            if (listImport.size() > 0 && hasErr == false) {
              // Xử lý list import wo bị trùng
              Map<String, WoDTO> map = new HashMap();
              for (WoDTO woDTO1 : listImport) {
                map.put(woDTO1.getContentUpgradeAndDownGrade(), woDTO1);
              }
              List<WoDTO> listAddImport = new ArrayList<>();
              for (Map.Entry<String, WoDTO> e : map.entrySet()) {
                listAddImport.add(e.getValue());
              }
              // insert Wo
              for (WoDTO dto : listAddImport) {
                if (StringUtils.isStringNullOrEmpty(dto.getResultImport())) {
                  WoDTO woDto = new WoDTO();
                  woDto.setWoContent(dto.getWoContent());
                  woDto.setWoDescription(dto.getWoDescription());
                  woDto.setWoTypeCode(dto.getWoTypeCode());
                  woDto.setStatus(dto.getStatus());
                  woDto.setWoSystem(dto.getWoSystem());
                  woDto.setPlanCode(dto.getPlanCode());
                  woDto.setPriorityId(dto.getPriorityId());
                  woDto.setStationCode(dto.getStationCode());
                  woDto.setStartTime(dto.getStartTime());
                  woDto.setEndTime(dto.getEndTime());
                  woDto.setCdId(dto.getCdId());
                  woDto.setContentUpgradeAndDownGrade(dto.getContentUpgradeAndDownGrade());
                  woDto.setWarehouseCode(dto.getWarehouseCode());
                  woDto.setPriorityCode(dto.getPriorityCode());
                  woDto.setCreatePersonName(userToken.getUserName());
                  woDto.setCreateDate(DateTimeUtils
                      .converClientDateToServerDate(DateTimeUtils.getSysDateTime(), offset));
                  try {
                    ResultDTO result = createWoForOtherSystem(woDto);
                    if (result != null && RESULT.SUCCESS.equals(result.getMessage())) {
                      String id[] = result.getId().split("_");
                      // Luu vat tu
                      if (lstDataAttachments != null && lstDataAttachments.size() > 0) {
                        for (int i = 0; i < lstDataAttachments.size(); i++) {
                          if (woDto.getContentUpgradeAndDownGrade()
                              .equals(lstDataAttachments.get(i)[3].toString().trim() + " "
                                  + lstDataAttachments.get(i)[4].toString().trim())) {
                            WoMerchandiseInsideDTO woMerchandiseInsideDTO1 = new WoMerchandiseInsideDTO();
                            woMerchandiseInsideDTO1
                                .setMerchandiseCode(lstDataAttachments.get(i)[1].toString().trim());
                            woMerchandiseInsideDTO1.setQuantity(
                                Double.parseDouble(lstDataAttachments.get(i)[2].toString().trim()));
                            woMerchandiseInsideDTO1
                                .setWoId(Long.parseLong(id[id.length - 1].toString().trim()));
                            ResultInSideDto resultInSideDto = woMerchandiseRepository
                                .insertWoMerchandise(woMerchandiseInsideDTO1);
                            if (resultInSideDto == null || !RESULT.SUCCESS
                                .equals(resultInSideDto.getKey())) {
                              hasErr = true;
                              dto.setResultImport(resultInSideDto.getMessage());
                              break;
                            } else {
                              // thêm vào kết quả cột wo
                              StringBuilder stringBuilder = new StringBuilder();
                              stringBuilder.append(I18n.getLanguage("wo.detail.title.woCode"))
                                  .append(": ").append(result.getId());
                              woMerchandiseInsideDTOList.get(i)
                                  .setResultImport(stringBuilder.toString());
                            }
                          }
                        }
                        // luu thong tin KTTS
                        WoKTTSInfoDTO woKttsInfo = new WoKTTSInfoDTO();
                        woKttsInfo.setWoId(Long.parseLong(id[id.length - 1].toString().trim()));
                        ResultInSideDto resultInSideDto = woKTTSInfoRepository
                            .insertWoKTTSInfo(woKttsInfo);
                        if (resultInSideDto == null || !RESULT.SUCCESS
                            .equals(resultInSideDto.getKey())) {
                          hasErr = true;
                          dto.setResultImport(resultInSideDto.getMessage());
                          break;
                        }
                      }
                    }
                  } catch (Exception e) {
                    log.error(e.getMessage(), e);
                    hasErr = true;
                    dto.setResultImport(e.getMessage());
                    for (int i = 0; i < lstDataAttachments.size(); i++) {
                      if (woDto.getContentUpgradeAndDownGrade()
                          .equals(lstDataAttachments.get(i)[3].toString().trim() + " "
                              + lstDataAttachments.get(i)[4].toString().trim())) {
                        woMerchandiseInsideDTOList.get(i).setResultImport(e.getMessage());
                        woMerchandiseDTOList.get(i).setResultImport(e.getMessage());
                      }
                    }
                  }
                } else {
                  resultInSideDTO.setKey(RESULT.ERROR);
                  resultInSideDTO.setMessage(I18n.getLanguage("import.common.fail"));
                }
              }
            }
          }
          //</editor-fold>
          if (hasErr) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            resultInSideDTO.setKey(RESULT.ERROR);
            resultInSideDTO.setMessage(I18n.getLanguage("import.common.fail"));
            File file = exportFileImportAssetsWoMerchandiseDTO(woMerchandiseDTOList,
                getTypeNameFile);
            resultInSideDTO.setFile(file);
            resultInSideDTO.setFilePath(file.getPath());
          } else {
            File file = exportFileImportAssets(woMerchandiseInsideDTOList, getTypeNameFile);
            resultInSideDTO.setFile(file);
            resultInSideDTO.setFilePath(file.getPath());
          }


        } else {
          return new ResultInSideDto(null, "ERROR_NO_DOWNLOAD",
              "File " + I18n.getLanguage("common.searh.nodata"));
        }


      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      resultInSideDTO.setKey(RESULT.ERROR);
      resultInSideDTO.setMessage(I18n.getLanguage("import.common.fail"));
    }
    return resultInSideDTO;
  }

  private String resultValidateDuplicate(WoMerchandiseInsideDTO woMerchandiseInsideDTO,
      List<WoMerchandiseInsideDTO> woMerchandiseInsideDTOList, String getTypeNameFile) {
    ResultInSideDto resultInSideDto = null;
    String materialCode = woMerchandiseInsideDTO.getMerchandiseCode();
    String codeStation1 = null;
    String codeStation2 = null;
    if (woMerchandiseInsideDTOList.size() > 0) {
      if (getTypeNameFile.equals(I18n.getLanguage("wo.infoAsset.formDataJson.TProperty"))) {
        codeStation1 = woMerchandiseInsideDTO.getCodeStationSource();
        codeStation2 = woMerchandiseInsideDTO.getCodeStationDestination();
        for (int i = 0; i < woMerchandiseInsideDTOList.size(); i++) {
          WoMerchandiseInsideDTO dto1 = woMerchandiseInsideDTOList.get(i);
          if (materialCode.equals(dto1.getMerchandiseCode()) &&
              codeStation1.equals(dto1.getCodeStationSource()) && codeStation2
              .equals(dto1.getCodeStationDestination())) {
            return I18n.getLanguage("wo.infoAsset.import.dup-code-in-file")
                .replaceAll("0", String.valueOf((i) + 1));
          }
        }
      } else if (getTypeNameFile.equals(I18n.getLanguage("wo.infoAsset.formDataJson.DDrawal"))) {
        codeStation1 = woMerchandiseInsideDTO.getDowngradeStationCode();
        codeStation2 = woMerchandiseInsideDTO.getEntryCode();
        for (int i = 0; i < woMerchandiseInsideDTOList.size(); i++) {
          WoMerchandiseInsideDTO dto1 = woMerchandiseInsideDTOList.get(i);
          if (materialCode.equals(dto1.getMerchandiseCode()) &&
              codeStation1.equals(dto1.getDowngradeStationCode()) && codeStation2
              .equals(dto1.getEntryCode())) {
            return I18n.getLanguage("wo.infoAsset.import.dup-code-in-file")
                .replaceAll("0", String.valueOf((i) + 1));
          }
        }
      } else {
        codeStation1 = woMerchandiseInsideDTO.getCodeStationupgrade();
        codeStation2 = woMerchandiseInsideDTO.getExportWarehouse();
        for (int i = 0; i < woMerchandiseInsideDTOList.size(); i++) {
          WoMerchandiseInsideDTO dto1 = woMerchandiseInsideDTOList.get(i);
          if (materialCode.equals(dto1.getMerchandiseCode()) &&
              codeStation1.equals(dto1.getCodeStationupgrade()) && codeStation2
              .equals(dto1.getExportWarehouse())) {
            return I18n.getLanguage("wo.infoAsset.import.dup-code-in-file")
                .replaceAll("0", String.valueOf((i) + 1));
          }
        }
      }
    }
    return "";
  }

  private List<ConfigHeaderExport> renderHeaderSheet(String... col) {
    List<ConfigHeaderExport> lstHeaderSheet = new ArrayList<>();
    for (int i = 0; i < col.length; i++) {
      lstHeaderSheet.add(new ConfigHeaderExport(col[i], "LEFT", false, 0, 0, new String[]{}, null,
          "STRING"));
    }
    return lstHeaderSheet;
  }

  private boolean validFileFormat(List<Object[]> lstHeader) {
    Object[] obj0 = lstHeader.get(0);
    int count = 0;
    for (Object data : obj0) {
      if (data != null) {
        count += 1;
      }
    }
    if (count != 13) {
      return false;
    }
    return true;
  }

  private boolean validFileFormatAssets(List<Object[]> lstHeader) {
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
    if (!(I18n.getLanguage("wo.STT")).equalsIgnoreCase(obj0[0].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("wo.infoAsset.content") + " (*)")
        .equalsIgnoreCase(obj0[1].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("wo.woDescription"))
        .equalsIgnoreCase(obj0[2].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("wo.startTime") + " (*)").equalsIgnoreCase(obj0[3].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("wo.endTime") + " (*)").equalsIgnoreCase(obj0[4].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("wo.infoAsset.codePlan"))
        .equalsIgnoreCase(obj0[5].toString().trim())) {
      return false;
    }
    return true;
  }

  private boolean validFileFormatPropertyInformation(List<Object[]> lstHeader,
      String formDataJson) {
    Object[] obj0 = lstHeader.get(0);
    int count = 0;
    for (Object data : obj0) {
      if (data != null) {
        count += 1;
      }
    }
    if (formDataJson.equals(I18n.getLanguage("wo.infoAsset.formDataJson.TProperty"))) {
      if (count != 7) {
        return false;
      }
    } else {
      if (count != 6) {
        return false;
      }
    }

    if (obj0 == null) {
      return false;
    }
    if (obj0[0] == null) {
      return false;
    }
    if (!(I18n.getLanguage("wo.STT")).equalsIgnoreCase(obj0[0].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("wo.infoAsset.codeMaterial") + " (*)")
        .equalsIgnoreCase(obj0[1].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("wo.infoAsset.amount") + " (*)")
        .equalsIgnoreCase(obj0[2].toString().trim())) {
      return false;
    }

    if (formDataJson.equals(I18n.getLanguage("wo.infoAsset.formDataJson.TProperty"))) {
      if (!(I18n.getLanguage("wo.infoAsset.codeStationSource") + " (*)")
          .equalsIgnoreCase(obj0[3].toString().trim())) {
        return false;
      }
      if (!(I18n.getLanguage("wo.infoAsset.codeStationDestination") + " (*)")
          .equalsIgnoreCase(obj0[4].toString().trim())) {
        return false;
      }
      if (!(I18n.getLanguage("wo.infoAsset.import.homeStationCodeDowngrade") + " (*)")
          .equalsIgnoreCase(obj0[5].toString().trim())) {
        return false;
      }
      if (!(I18n.getLanguage("wo.infoAsset.import.homeStationCodeUpgrade") + " (*)")
          .equalsIgnoreCase(obj0[6].toString().trim())) {
        return false;
      }
    } else if (formDataJson.equals(I18n.getLanguage("wo.infoAsset.formDataJson.DDrawal"))) {
      if (!(I18n.getLanguage("wo.infoAsset.downgradeStation") + " (*)")
          .equalsIgnoreCase(obj0[3].toString().trim())) {
        return false;
      }
      if (!(I18n.getLanguage("wo.infoAsset.importWarehouse") + " (*)")
          .equalsIgnoreCase(obj0[4].toString().trim())) {
        return false;
      }
      if (!(I18n.getLanguage("wo.infoAsset.import.homeStationCodeDowngrade") + " (*)")
          .equalsIgnoreCase(obj0[5].toString().trim())) {
        return false;
      }
    } else {
      if (!(I18n.getLanguage("wo.infoAsset.codeUpgrade") + " (*)")
          .equalsIgnoreCase(obj0[3].toString().trim())) {
        return false;
      }
      if (!(I18n.getLanguage("wo.infoAsset.exportWarehouse") + " (*)")
          .equalsIgnoreCase(obj0[4].toString().trim())) {
        return false;
      }
      if (!(I18n.getLanguage("wo.infoAsset.import.homeStationCodeUpgrade") + " (*)")
          .equalsIgnoreCase(obj0[5].toString().trim())) {
        return false;
      }
    }
    return true;
  }


  public File exportFileImport(List<WoDTO> lstExport) {
    try {
      String[] header = new String[]{
          "woContent", "woDescription", "woSystem", "woTypeCode",
          "priorityCode", "startTime", "endTime",
          "assign", "importBusinessType", "importBusinessCode","activeEnvironmentId","reasonDetail",
          "resultImport"
      };
      return handleFileExport(lstExport, header, DateUtil.date2ddMMyyyyHHMMss(new Date()),
          "IMPORT_WO_RESULT");
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

  public File exportFileImportAssets(List<WoMerchandiseInsideDTO> lstExport,
      String getTypeNameFile) {
    try {
      String[] header;
      if (getTypeNameFile.equals("TProperty")) {
        header = new String[]{
            "merchandiseCode", "quantity", "codeStationSource", "codeStationDestination",
            "homeStationCodeDowngrade", "homeStationCodeUpgrade",
            "resultImport"
        };
        return handleFileExport(lstExport, header, DateUtil.date2ddMMyyyyHHMMss(new Date()),
            "IMPORT_WO_INFO_RESULT_TProperty");
      } else if (getTypeNameFile.equals("DDrawal")) {
        header = new String[]{
            "merchandiseCode", "quantity", "downgradeStationCode", "entryCode",
            "homeStationCodeDowngrade",
            "resultImport"
        };
      } else {
        header = new String[]{
            "merchandiseCode", "quantity", "codeStationupgrade", "exportWarehouse",
            "homeStationCodeUpgrade",
            "resultImport"
        };
      }

      return handleFileExport(lstExport, header, DateUtil.date2ddMMyyyyHHMMss(new Date()),
          "IMPORT_WO_INFO_RESULT");
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

  public File exportFileImportAssetsWoMerchandiseDTO(List<WoMerchandiseDTO> lstExport,
      String getTypeNameFile) {
    try {
      String[] header;
      if (getTypeNameFile.equals("TProperty")) {
        header = new String[]{
            "merchandiseCode", "quantity", "codeStationSource", "codeStationDestination",
            "homeStationCodeDowngrade", "homeStationCodeUpgrade",
            "resultImport"
        };
        return handleFileExport(lstExport, header, DateUtil.date2ddMMyyyyHHMMss(new Date()),
            "IMPORT_WO_INFO_RESULT_TProperty");
      } else if (getTypeNameFile.equals("DDrawal")) {
        header = new String[]{
            "merchandiseCode", "quantity", "downgradeStationCode", "entryCode",
            "homeStationCodeDowngrade",
            "resultImport"
        };
      } else {
        header = new String[]{
            "merchandiseCode", "quantity", "codeStationupgrade", "exportWarehouse",
            "homeStationCodeUpgrade",
            "resultImport"
        };
      }

      return handleFileExport(lstExport, header, DateUtil.date2ddMMyyyyHHMMss(new Date()),
          "IMPORT_WO_INFO_RESULT");
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return null;
  }

  @Override
  public WoSearchWebDTO getWoSearchWebDTOByWoCode(String code) {
    return woRepository.getWoSearchWebDTOByWoCode(code);
  }

  //tach code
  private void prepareWoTypeXLSCC(WoInsideDTO wo, UsersInsideDto usersFtDto) throws Exception {
    WoDTO o = new WoDTO();
    o.setWoSystemId(wo.getWoSystemId());
    if (wo.getFtId() != null) {
      o.setFtId(wo.getFtId().toString());
    }
    o.setWoTypeCode(Constants.AP_PARAM.WO_TYPE_CCKPC);
    o.setCreatePersonName(usersFtDto.getUsername());
    o.setCreateDate(DateUtil.date2ddMMyyyyHHMMss(new Date()));
    o.setStartTime(DateUtil.date2ddMMyyyyHHMMss(new Date()));
    o.setStationCode(wo.getStationCode());
    o.setWoContent(I18n.getLanguage("wo.ConsolidateBrokenCable") + " [" + wo.getWoSystemId() + "]");
    o.setWoDescription(I18n.getLanguage("wo.ConsolidateBrokenCable1") + "\r\n"
        + I18n.getLanguage("wo.ConsolidateBrokenCable2") + "\r\n"
        + "Cable: [" + wo.getWoDescription() + "]" + "\r\n"
        + "Note:" + I18n.getLanguage("wo.ConsolidateBrokenCable3")
    );
    o.setCdId(wo.getCdId().toString());
    o.setWoSystem("WFM");

    o.setPriorityCode(Constants.AP_PARAM.WO_PRIORITY_MINOR);

    ResultDTO res = createWoForOtherSystem(o);

    if (res != null && !"SUCCESS".equals(res.getMessage())) {
      throw new Exception(res.getMessage());
    }
  }

  //tach code
  private void prepareWoTypeXLSCCUpdate(WoInsideDTO wo, UsersInsideDto usersFtDto)
      throws Exception {
    WoDTO o = new WoDTO();
    o.setWoSystemId(wo.getWoSystemId());
    if (wo.getFtId() != null) {
      o.setFtId(wo.getFtId().toString());
    }
    o.setWoTypeCode(Constants.AP_PARAM.WO_TYPE_CCKPC);
    o.setCreatePersonName(usersFtDto.getUsername());
    o.setCreateDate(DateUtil.date2ddMMyyyyHHMMss(new Date()));
    o.setStartTime(DateUtil.date2ddMMyyyyHHMMss(new Date()));
    o.setStationCode(wo.getStationCode());
    o.setWoContent(I18n.getLanguage("wo.ConsolidateBrokenCable") + " [" + wo.getWoCode() + "]");
    o.setWoDescription(I18n.getLanguage("wo.ConsolidateBrokenCable1") + "\r\n"
        + I18n.getLanguage("wo.ConsolidateBrokenCable2") + "\r\n"
        + "Cable: [" + wo.getWoDescription() + "]" + "\r\n"
        + "Note:" + I18n.getLanguage("wo.ConsolidateBrokenCable3")
    );
    o.setCdId(wo.getCdId().toString());
    o.setWoSystem(wo.getWoSystem());
    o.setPriorityCode(Constants.AP_PARAM.WO_PRIORITY_MINOR);
    ResultDTO res = createWoForOtherSystem(o);

    if (res != null && !"SUCCESS".equals(res.getMessage())) {
      throw new Exception(res.getMessage());
    }
  }

  @Override
  public List<WoDTO> getListWoByCondition(List<ConditionBean> lstCondition, int rowStart,
      int maxRow, String sortType, String sortFieldList) {
    ConditionBeanUtil.sysToOwnListCondition(lstCondition);
    return woRepository
        .getListSearchWOByCondition(lstCondition, rowStart, maxRow, sortType, sortFieldList);
  }

  @Override
  public List<WoMerchandiseInsideDTO> getListWoMerchandiseDTO(
      WoMerchandiseInsideDTO woMerchandiseInsideDTO) {
    return woMerchandiseRepository.getListWoMerchandiseDTO(woMerchandiseInsideDTO);
  }

  @Override
  public List<MaterialThresDTO> getListMaterialByWoId(Long woId) {
    return woRepository.getListMaterialByWoId(woId);
  }

  @Override
  public WoInsideDTO findWoByWoCodeNoOffset(String woCode) {
    return woRepository.findWoByWoCodeNoOffset(woCode);
  }

  @Override
  public Datatable getListConfigAutoCreateWoOs(AutoCreateWoOsDTO autoCreateWoOsDTO) {
    return woRepository.getListConfigAutoCreateWoOs(autoCreateWoOsDTO);
  }

  @Override
  public ResultInSideDto insertOrUpdateAutoCreateWoOs(AutoCreateWoOsDTO autoCreateWoOsDTO) {
    return woRepository.insertOrUpdateAutoCreateWoOs(autoCreateWoOsDTO);
  }

  @Override
  public AutoCreateWoOsDTO getConfigById(Long id) {
    return woRepository.getConfigById(id);
  }

  @Override
  public ResultInSideDto delete(Long id) {
    return woRepository.delete(id);
  }

  @Override
  public ResultInSideDto syncFileFromWeb(Long woId) {
    return woRepository.syncFileFromWeb(woId);
  }
}
