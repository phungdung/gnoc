package com.viettel.gnoc.commons.business;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.*;
import com.viettel.gnoc.commons.model.UsersEntity;
import com.viettel.gnoc.commons.repository.CommonRepository;
import com.viettel.gnoc.commons.repository.UserRepository;
import com.viettel.gnoc.commons.utils.*;

import java.io.File;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;

import com.viettel.gnoc.risk.dto.RiskDTO;
import com.viettel.gnoc.wfm.dto.ConfigPropertyDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import viettel.passport.client.UserToken;

/**
 * @author TungPV
 */
@Service
@Transactional
@Slf4j
public class CommonBusinessImpl implements CommonBusiness {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private CommonRepository commonRepository;

  @Autowired
  private TicketProvider ticketProvider;

  @Value("${application.temp.folder}")
  private String tempFolder;

  @Override
  public ResultInSideDto getDBSysDate() {
    log.debug("Request to getDBSysDate : {}");
    return commonRepository.getDBSysDate();
  }

  @Override
  public List<GnocTimezoneDto> getAllGnocTimezone() {
    log.debug("Request to getAllGnocTimezone : {}");
    return commonRepository.getAllGnocTimezone();
  }

  @Override
  public List<GnocLanguageDto> getAllGnocLanguage() {
    log.debug("Request to getAllGnocLanguage : {}");
    return commonRepository.getAllGnocLanguage();
  }

  @Override
  public UsersInsideDto getUserByUserName(String userName) {
    log.debug("Request to getUserByUserName : {}");
    return commonRepository.getUserByUserName(userName);
  }

  @Override
  public List<UsersInsideDto> getListUserOfUnit(Long unitId) {
    log.debug("Request to getListUserOfUnit : {}");
    List<UsersEntity> list = commonRepository.getListUserOfUnit(unitId);
    List<UsersInsideDto> listDto = new ArrayList<>();
    for (UsersEntity usersEntity : list) {
      UsersInsideDto usersInsideDto = usersEntity.toDTO();
      listDto.add(usersInsideDto);
    }
    return listDto;
  }

  @Override
  public UsersInsideDto getUserByUserId(Long userId) {
    log.debug("Request to getUserByUserId : {}");
    UsersEntity usersEntity = commonRepository.getUserByUserId(userId);
    if (usersEntity != null) {
      UsersInsideDto usersInsideDto = usersEntity.toDTO();
      return usersInsideDto;
    }
    return null;
  }

  /**
   * copy du lieu 2 doi tuong
   */
  @Override
  public Object updateObjectData(Object objSrc, Object objDes) {
    try {
      Field[] k = objSrc.getClass().getDeclaredFields();
      for (int i = 0; i < k.length; i++) {
        try {
          PropertyUtils.setSimpleProperty(objDes, k[i].getName(),
              PropertyUtils.getSimpleProperty(objSrc, k[i].getName()));
        } catch (Exception e) {
          log.info("", e);
        }
      }
    } catch (Exception e) {
      log.info(e.getMessage(), e);
    }
    return objDes;
  }

  @Override
  public List<DataItemDTO> getListCombobox(ObjectSearchDto objectSearchDto) {
    log.debug(
        "Request to getListCombobox : {}", objectSearchDto);
    return commonRepository.getListCombobox(objectSearchDto);
  }

  @Override
  public List<TreeDTO> getTreeData(ObjectSearchDto objectSearchDto) {
    log.debug("Request to getTreeData : {}", objectSearchDto);
    List<TreeDTO> treeDTOS = commonRepository.getTreeData(objectSearchDto);
    return treeDTOS;
  }

  @Override
  public List<DataItemDTO> getListDataItem(String dataCode) {
    log.debug("Request to getListDataItem : {}" + dataCode);
    return commonRepository.getListItemByDataCode(dataCode);
  }

  @Override
  public Map<String, String> getConfigProperty() {
    return commonRepository.getConfigProperty();
  }

  @Override
  public ResultInSideDto checkRoleSubAdmin(String view) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    String valueSubAdminEdit = commonRepository
        .getConfigPropertyValue(Constants.CONFIG_PROPERTY.SUB_ADMIN_EDIT);
    List<String> lstCheck = Arrays.asList(valueSubAdminEdit.split(","));
    UserToken userToken = ticketProvider.getUserToken();
    String roleSubAdmin = userRepository.checkAccountSubAdmin(userToken.getUserName());
    if ("SUB_ADMIN".equals(roleSubAdmin)) {
      if (lstCheck != null && lstCheck.contains(view)) {
        resultInSideDto.setCheck(true);
      } else {
        resultInSideDto.setCheck(false);
      }
    } else {
      resultInSideDto.setCheck(true);
    }
    return resultInSideDto;
  }

  @Override
  public List<DashboardDTO> searchDataDashboard(DashboardDTO dashboardDTO){
    return commonRepository.searchDataDashboard(dashboardDTO);
  }

  @Override
  public List<DashboardDTO> getDataTableDashboard(){
    return  commonRepository.getDataTableDashboard();
  }

  @Override
  public List<RolesDTO> getListRole(){
    return  commonRepository.getListRole();
  }

  @Override
  public  Datatable getListComment(UserCommentDTO userCommentDTO){
    return  commonRepository.getListComment(userCommentDTO);
  }

  @Override
  public ResultInSideDto addComment(UserCommentDTO userCommentDTO){
    return  commonRepository.addComment(userCommentDTO);
  }

  @Override
  public List<ContactDTO> getListContact(){
    return commonRepository.getListContact();
  }

  //thanhlv12 add 22-09-2020

  @Override
  public ConfigPropertyDTO getConfigPropertyByKey () {
    return commonRepository.getConfigPropertyByKey();
  }

  @Override
  public ResultInSideDto insertHisUserImpact (DataHistoryChange dataHistoryChange) {
    return commonRepository.insertHisUserImpact(dataHistoryChange);
  }

  @Override
  public Datatable getListHistory (HisUserImpactDTO hisUserImpactDTO) {
    return commonRepository.getListHistory(hisUserImpactDTO);
  }

  @Override
  public File exportData(HisUserImpactDTO hisUserImpactDTO) throws Exception{
    String[] header = new String[]{"userName", "type", "result", "createTimeDate"};
    List<HisUserImpactDTO> lstData = (List<HisUserImpactDTO>)commonRepository.getListHistory(hisUserImpactDTO).getData();
    for (HisUserImpactDTO his : lstData) {
      if(his.getCreateTime() != null) {
        SimpleDateFormat dfm = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String start = DateTimeUtils.converClientDateToServerDate(dfm.format(his.getCreateTime()), 0.0);
        his.setCreateTimeDate(start);
      }
    }
    Date date = DateTimeUtils.convertDateOffset();
    return handleFileExport(lstData, header, DateUtil.date2ddMMyyyyHHMMss(date));
  }

  @Override
  public List<CatItemDTO> getListCommonLink(String locale) {
    return commonRepository.getListCommonLink(locale);
  }

  @Override
  public CatItemDTO getConfigIconDislay(String keyCode) {
    return  commonRepository.getConfigIconDislay(keyCode);
  }

  public File handleFileExport(List<HisUserImpactDTO> list, String[] columnExport, String date)
      throws Exception {
    List<ConfigFileExport> fileExports = new ArrayList<>();
    Map<String, String> fieldSplit = new HashMap<>();
    List<ConfigHeaderExport> lstHeaderSheet1 = getListHeaderSheet(columnExport);
    String sheetName = I18n.getLanguage("history.export.sheetname");
    String title = I18n.getLanguage("history.export.title");
    String fileNameOut = I18n.getLanguage("history.export.fileNameOut");
    String headerPrefix = "language.history";
    String firstLeftHeader = I18n.getLanguage("history.export.firstLeftHeader");
    String secondLeftHeader = I18n.getLanguage("history.export.secondLeftHeader");
    String firstRightHeader = I18n.getLanguage("history.export.firstRightHeader");
    String secondRightHeader = I18n.getLanguage("history.export.secondRightHeader");
    int mergeTitleEndIndex = 6;
    int startRow = 7;
    int cellTitleIndex = 3;
    String subTitle = I18n.getLanguage("history.export.exportDate", date);
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
        I18n.getLanguage("history.STT"), "HEAD", "STRING");
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
  //end
}
