package com.viettel.gnoc.commons.business;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.GnocLanguageDto;
import com.viettel.gnoc.commons.dto.LogChangeConfigDTO;
import com.viettel.gnoc.commons.dto.MessagesDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.RoleUserDTO;
import com.viettel.gnoc.commons.dto.RolesDTO;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.dto.UserTokenGNOC;
import com.viettel.gnoc.commons.dto.UserUpdateHisDTO;
import com.viettel.gnoc.commons.dto.UsersDTO;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.commons.model.RoleUserEntity;
import com.viettel.gnoc.commons.model.RolesEntity;
import com.viettel.gnoc.commons.model.Users;
import com.viettel.gnoc.commons.model.UsersEntity;
import com.viettel.gnoc.commons.proxy.CommonStreamServiceProxy;
import com.viettel.gnoc.commons.repository.CatItemRepository;
import com.viettel.gnoc.commons.repository.CommonRepository;
import com.viettel.gnoc.commons.repository.MessagesRepository;
import com.viettel.gnoc.commons.repository.RoleUserRepository;
import com.viettel.gnoc.commons.repository.UnitRepository;
import com.viettel.gnoc.commons.repository.UserRepository;
import com.viettel.gnoc.commons.repository.UserSmsRepository;
import com.viettel.gnoc.commons.utils.CellConfigExport;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.CommonImport;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.commons.utils.ConditionBeanUtil;
import com.viettel.gnoc.commons.utils.ConfigFileExport;
import com.viettel.gnoc.commons.utils.ConfigHeaderExport;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.Constants.ROLE;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.DateUtil;
import com.viettel.gnoc.commons.utils.ExcelWriterUtils;
import com.viettel.gnoc.commons.utils.FileUtils;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.commons.ws.VSAAdminPort;
import com.viettel.gnoc.cr.dto.ImpactSegmentDTO;
import com.viettel.gnoc.kedb.dto.UserSmsDTO;
import com.viettel.vsaadmin.service.Response;
import com.viettel.vsaadmin.service.UserRoleInfo;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.Drawing;
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
 * @author TungPV
 */
@Service
@Transactional
@Slf4j
public class UserBusinessImpl implements UserBusiness {

  @Value("${application.temp.folder}")
  private String tempFolder;

  @Autowired
  protected UserRepository userRepository;

  @Autowired
  protected UnitRepository unitRepository;

  @Autowired
  protected MessagesRepository messagesRepository;

  @Autowired
  protected UserSmsRepository userSmsRepository;

  @Autowired
  protected RoleUserRepository roleUserRepository;

  @Autowired
  TicketProvider ticketProvider;

  @Autowired
  private CommonStreamServiceProxy commonStreamServiceProxy;

  @Autowired
  CatItemRepository catItemRepository;

  @Autowired
  CommonRepository commonRepository;

  @Autowired
  UnitBusiness unitBusiness;

  @Autowired
  VSAAdminPort vsaAdminPort;

  private int maxRecord = 500;
  Map<String, UnitDTO> mapUnit = new HashMap<>();

  Map<String, UsersInsideDto> mapCheckUserName = new HashMap<>();

  Map<String, String> mapCheckStaffCode = new HashMap<>();

  @Override
  public UsersEntity getUserByUserId(Long userId) {
    return userRepository.getUserByUserId(userId);
  }

  @Override
  public UsersEntity getUserByUserName(String userName) {
    return userRepository.getUserByUserName(userName);
  }

  //20200901 Start hieunv1 sửa chức năng phê duyệt và từ chối
  @Override
  public ResultInSideDto approveUser(UsersInsideDto usersInsideDto) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.NODATA);
    if (usersInsideDto != null && usersInsideDto.getLstUserId() != null && !usersInsideDto
        .getLstUserId().isEmpty()) {
      for (String userId : usersInsideDto.getLstUserId()) {
        if (!StringUtils.isStringNullOrEmpty(userId)) {
          resultInSideDto = updateUserApprove(true, Long.valueOf(userId), null,
              usersInsideDto.getUnitIdNew(), usersInsideDto.getDeleteGroup());
          if (!resultInSideDto.getKey().equals(RESULT.SUCCESS)) {
            resultInSideDto.setKey(RESULT.ERROR);
            break;
          }
        }
      }
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto refuseUser(UsersInsideDto usersInsideDto) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.NODATA);
    if (usersInsideDto != null && usersInsideDto.getLstUserId() != null && !usersInsideDto
        .getLstUserId()
        .isEmpty()) {
      for (String userId : usersInsideDto.getLstUserId()) {
        if (!StringUtils.isStringNullOrEmpty(userId)) {
          resultInSideDto = updateUserApprove(false, Long.valueOf(userId),
              !StringUtils.isStringNullOrEmpty(usersInsideDto.getReasonRefusal()) ? usersInsideDto
                  .getReasonRefusal() : "", null, usersInsideDto.getDeleteGroup());
          if (!resultInSideDto.getKey().equals(RESULT.SUCCESS)) {
            resultInSideDto.setKey(RESULT.ERROR);
            break;
          }
        }
      }
    }
    return resultInSideDto;
  }

  private ResultInSideDto updateUserApprove(Boolean approve, Long userId, String note,
      Long unitIdNew, Long deleteGroup) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    UsersEntity usersEntity = getUserByUserId(userId);
    if (StringUtils.isLongNullOrEmpty(unitIdNew)) {
      if (usersEntity.getUnitIdNew() != null && usersEntity.getUnitIdNew() > 0L) {
        unitIdNew = usersEntity.getUnitIdNew();
      } else if (StringUtils.isNotNullOrEmpty(usersEntity.getRelateUnits())) {
        unitIdNew = Long.parseLong(usersEntity.getRelateUnits());
      }
    }
    UnitDTO unitDTO = unitRepository.findUnitById(unitIdNew);
    UsersInsideDto usersInsideDto = usersEntity.toDTO();
    usersInsideDto.setSmsGatewayId(StringUtils.isStringNullOrEmpty(unitDTO.getSmsGatewayId()) ? null
        : unitDTO.getSmsGatewayId().toString());
    List<UsersInsideDto> lstSmsUser = new ArrayList<>();
    UserToken userToken = ticketProvider.getUserToken();
    if (approve != null) {
      if (approve) {
        usersInsideDto.setUnitId(unitIdNew);
        if (deleteGroup != null && deleteGroup > 0L) {
          usersInsideDto.setDeleteGroup(deleteGroup);
        }
        usersInsideDto.setRelateUnits(null);
        usersInsideDto.setUnitIdNew(null);
        usersInsideDto.setApproveStatus(2L);
        usersInsideDto.setUpdateTimeUnit(new Date());
        usersInsideDto.setApproveUser(userToken.getUserName());
        usersInsideDto.setResourceCode(userToken.getUserName());
        resultInSideDto = userRepository.updateUserApprove(usersInsideDto);
        if (RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
          commonStreamServiceProxy.insertLog(new LogChangeConfigDTO(userToken.getUserName(),
              "Approve UserApprove",
              "Approve UserApprove ID: " + userId, null,
              null));
          String content = I18n.getValidation("account.accept").
              replaceAll("#unitName#", unitDTO.getUnitName());
          lstSmsUser.add(usersInsideDto);
          insertMessageForUser(content, "GNOC_COMMON", lstSmsUser);
          resultInSideDto.setMessage(I18n.getLanguage("common.approve.success"));
          return resultInSideDto;
        } else {
          resultInSideDto.setKey(RESULT.ERROR);
          resultInSideDto.setMessage(I18n.getLanguage("common.approve.fail"));
          return resultInSideDto;
        }
      } else {
        if (StringUtils.isStringNullOrEmpty(note)) {
          resultInSideDto.setMessage(I18n.getValidation("cr.list.reasonTitle") + " "
              + I18n.getValidation("common.isRequired"));
        } else {
          usersInsideDto.setRelateUnits(null);
          usersInsideDto.setUnitIdNew(null);
          usersInsideDto.setApproveStatus(3L);
          usersInsideDto.setUpdateTimeUnit(new Date());
          usersInsideDto.setApproveUser(userToken.getUserName());
          usersInsideDto.setResourceCode(userToken.getUserName());
          resultInSideDto = userRepository.updateUserApprove(usersInsideDto);
          if (RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
            commonStreamServiceProxy.insertLog(new LogChangeConfigDTO(userToken.getUserName(),
                "Not approve UserApprove",
                "Not approve UserApprove ID: " + userId, null,
                null));
            String content = I18n.getValidation("account.inaccept").
                replaceAll("#unitName#", unitDTO.getUnitName()) + " " + note;
            lstSmsUser.add(usersInsideDto);
            insertMessageForUser(content, "GNOC_COMMON", lstSmsUser);
            resultInSideDto.setMessage(I18n.getLanguage("common.approve.success"));
            return resultInSideDto;
          } else {
            resultInSideDto.setKey(RESULT.ERROR);
            resultInSideDto.setMessage(I18n.getLanguage("common.approve.fail"));
            return resultInSideDto;
          }
        }
      }
    } else {
      resultInSideDto.setKey(RESULT.ERROR);
      resultInSideDto.setMessage(I18n.getValidation("feedbackList.list.importantLevel") + " " + I18n
          .getValidation("common.isRequired"));
    }
    return resultInSideDto;
  }
//20200901 End hieunv1 sửa chức năng phê duyệt và từ chối

  public void insertMessageForUser(String content, String alias, List<UsersInsideDto> lst) {
    try {
      List<MessagesDTO> lsMessagesDTOs = new ArrayList<>();
      if (lst != null && !lst.isEmpty()) {
        for (UsersInsideDto usersDTO : lst) {
          if ((usersDTO.getIsNotReceiveMessage() == null || !"1"
              .equals(usersDTO.getIsNotReceiveMessage()))) {
            MessagesDTO messagesDTO = new MessagesDTO();

            messagesDTO.setContent(content);
            messagesDTO.setCreateTime(DateUtil.date2ddMMyyyyHHMMss(new Date()));
            messagesDTO.setReceiverId(usersDTO.getUserId().toString());
            messagesDTO.setReceiverUsername(usersDTO.getUsername());
            messagesDTO.setReceiverPhone(usersDTO.getMobile());
            messagesDTO.setAlias(alias);
            messagesDTO.setSmsGatewayId(usersDTO.getSmsGatewayId());
            messagesDTO.setStatus("0");
            lsMessagesDTOs.add(messagesDTO);
          }
        }
      }

      if (lsMessagesDTOs != null && !lsMessagesDTOs.isEmpty()) {
        for (int i = lsMessagesDTOs.size() - 1; i >= 0; i--) {
          if ("".equals(lsMessagesDTOs.get(i).getSmsGatewayId())
              || lsMessagesDTOs.get(i).getSmsGatewayId() == null) {
            lsMessagesDTOs.remove(i);
          }
        }
        messagesRepository.insertOrUpdateListMessagesCommon(lsMessagesDTOs);
      }

    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

  @Override
  public List<UsersInsideDto> getListUsersByCondition(List<ConditionBean> conditionBeans,
      int rowStart,
      int maxRow, String sortType, String sortFieldList) {
    ConditionBeanUtil.sysToOwnListCondition(conditionBeans);
    return userRepository
        .getListUsersByCondition(conditionBeans, rowStart, maxRow, sortType, sortFieldList);
  }

  @Override
  public Datatable getListUsersDTO(UsersInsideDto dto) {
    Datatable datatable = new Datatable();
    UserToken userToken = ticketProvider.getUserToken();
    boolean isFullRoleVSA = false;
    if (userToken != null) {
      isFullRoleVSA = isCheckRoleAdminVsa(userToken.getUserName());
      List<RolesDTO> roles = roleUserRepository.getListRoleCodeByUserId(userToken.getUserID());
      if (isFullRoleVSA) {
        return userRepository.getListUsersDTO(dto);
      } else if (roles != null && roles.stream()
          .filter(o -> ROLE.ROLE_USER_ROLE_TP.equals(o.getRoleCode()))
          .findFirst().isPresent()) {
        UsersEntity usersEntity = userRepository.getUserByUserName(userToken.getUserName());
        if (usersEntity != null) {
          List<String> lstUnit = new ArrayList<>();
          if (usersEntity.getUnitId() != null && usersEntity.getUnitId() > 0) {
            lstUnit.add(usersEntity.getUnitId().toString());
          }
          if (StringUtils.isNotNullOrEmpty(usersEntity.getRelateUnits())) {
            lstUnit.add(usersEntity.getRelateUnits());
          }
          if (lstUnit != null && !lstUnit.isEmpty()) {
            dto.setLstUnitLogin(lstUnit);
            return userRepository.getListUsersDTO(dto);
          }
        }
      } else {
        dto.setUserId(userToken.getUserID());
        return userRepository.getListUsersDTO(dto);
      }
    }
    return datatable;
  }

  //20200901 Start -- hieunv1 lấy danh sách users chờ phê duyệt
  @Override
  public Datatable getListUsersApproveDTO(UsersInsideDto dto) {
    Datatable datatable = new Datatable();
    UserToken userToken = ticketProvider.getUserToken();
    if (userToken != null) {
      if (isCheckRoleAdminForApproveVsa(userToken.getUserName())) {
        dto.setApproveStatus(1L);
        dto.setCheckingAdmin("1");
        return userRepository.getListUsersDTO(dto);
      } else if (userRepository.checkRoleOfUser(ROLE.ROLE_USER_ROLE_TP, userToken.getUserID())) {
        dto.setRelateUnits(String.valueOf(userToken.getDeptId()));
        dto.setApproveStatus(1L);
        return userRepository.getListUsersDTO(dto);
      }
    }
    return datatable;
  }
  //20200901 End -- hieunv1 lấy danh sách users chờ phê duyệt

  //20200903 Start --hieunv1 bổ sung import,export phê duyệt chuyển đv
  @Override
  public File getApproveTemplate() throws Exception {
    ExcelWriterUtils excelWriterUtils = new ExcelWriterUtils();
    String templatePathOut = "templates" + File.separator + "TEMPLATE_EXPORT.xlsx";
    templatePathOut = StringUtils.removeSeparator(templatePathOut);
    Resource resource = new ClassPathResource(templatePathOut);
    InputStream fileTemplate = resource.getInputStream();

    //apache POI XSSF
    XSSFWorkbook workbook = new XSSFWorkbook(fileTemplate);
    XSSFSheet sheetOne = workbook.getSheetAt(0);
    XSSFSheet sheetParam = workbook.createSheet("param");
    //Tạo 1 mảng lưu header từng cột
    String[] header = new String[]{
        I18n.getLanguage("common.STT"),
        I18n.getLanguage("employee.userIdName"),
        I18n.getLanguage("employee.approveStatusName"),
        I18n.getLanguage("employee.deleteGroupName"),
        I18n.getLanguage("employee.reasonRefusal")
    };
    //Tiêu đề đánh dấu *
    String[] headerStar = new String[]{
        I18n.getLanguage("employee.userIdName"),
        I18n.getLanguage("employee.approveStatusName")
    };

    XSSFDataValidationHelper dataValidationHelper = new XSSFDataValidationHelper(sheetOne);
    List<String> listHeader = Arrays.asList(header);
    List<String> listHeaderStar = Arrays.asList(headerStar);

    int approveStatusNameColumn = listHeader
        .indexOf(I18n.getLanguage("employee.approveStatusName"));
    int deleteGroupNameColumn = listHeader.indexOf(I18n.getLanguage("employee.deleteGroupName"));
    Map<String, CellStyle> style = CommonExport.createStyles(workbook);

    //Tạo tiêu đề
    sheetOne.addMergedRegion(new CellRangeAddress(2, 2, 0, listHeader.size() - 1));
    Row titleRow = sheetOne.createRow(2);
    titleRow.setHeightInPoints(25);
    Cell titleCell = titleRow.createCell(0);
    titleCell.setCellValue(I18n.getLanguage("employee.approve.title"));
    titleCell.setCellStyle(style.get("title"));

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
    Cell firstRightHeaderCell = firstLeftHeaderRow.createCell(3);
    firstLeftHeaderRow.setHeightInPoints(16);
    firstRightHeaderCell.setCellValue(I18n.getLanguage("common.export.firstRightHeader"));
    firstRightHeaderCell.setCellStyle(style.get("indexTitle"));
    Cell secondRightHeaderCell = secondLeftHeaderRow.createCell(3);
    secondLeftHeaderRow.setHeightInPoints(16);
    secondRightHeaderCell.setCellValue(I18n.getLanguage("common.export.secondRightHeader"));
    secondRightHeaderCell.setCellStyle(style.get("indexTitle"));

    XSSFFont starFont = workbook.createFont();
    starFont.setColor(IndexedColors.RED.getIndex());

    //Tạo Header

    Row headerRow = sheetOne.createRow(4);
    headerRow.setHeightInPoints(16);
    for (int i = 0; i < listHeader.size(); i++) {
      Cell headerCell = headerRow.createCell(i);
      XSSFRichTextString richTextString = new XSSFRichTextString(listHeader.get(i));
      for (String headerCheck : listHeaderStar) {
        if (headerCheck.equalsIgnoreCase(listHeader.get(i))) {
          richTextString.append("*", starFont);
        }
      }
      headerCell.setCellValue(richTextString);
      headerCell.setCellStyle(style.get("header"));
      sheetOne.setColumnWidth(i, 7000);
    }
    sheetOne.setColumnWidth(0, 3000);

    // Set dữ liệu vào column dropdown
    int row = 5;
    excelWriterUtils.createCell(sheetParam, 2, row++, I18n.getLanguage("employee.approveStatus.2")
        , style.get("cell"));
    excelWriterUtils.createCell(sheetParam, 2, row++, I18n.getLanguage("employee.approveStatus.3")
        , style.get("cell"));
    sheetParam.autoSizeColumn(1);
    Name approveStatusName = workbook.createName();
    approveStatusName.setNameName("approveStatusName");
    approveStatusName.setRefersToFormula("param!$C$3:$C$" + row);
    XSSFDataValidationConstraint approveStatusNameConstraint = new XSSFDataValidationConstraint(
        DataValidationConstraint.ValidationType.LIST, "approveStatusName");

    CellRangeAddressList approveStatusNameCreate = new CellRangeAddressList(5, 65000,
        approveStatusNameColumn,
        approveStatusNameColumn);
    XSSFDataValidation dataValidationApproveStatus = (XSSFDataValidation) dataValidationHelper
        .createValidation(
            approveStatusNameConstraint, approveStatusNameCreate);
    dataValidationApproveStatus.setShowErrorBox(true);
    sheetOne.addValidationData(dataValidationApproveStatus);

    row = 5;
    excelWriterUtils.createCell(sheetParam, 3, row++, I18n.getLanguage("employee.deleteGroup.1")
        , style.get("cell"));
    excelWriterUtils.createCell(sheetParam, 3, row++, I18n.getLanguage("employee.deleteGroup.0")
        , style.get("cell"));
    sheetParam.autoSizeColumn(1);
    Name deleteGroupName = workbook.createName();
    deleteGroupName.setNameName("deleteGroupName");
    deleteGroupName.setRefersToFormula("param!$D$4:$D$" + row);
    XSSFDataValidationConstraint deleteGroupNameConstraint = new XSSFDataValidationConstraint(
        DataValidationConstraint.ValidationType.LIST, "deleteGroupName");
    CellRangeAddressList deleteGroupNameCreate = new CellRangeAddressList(5, 65000,
        deleteGroupNameColumn,
        deleteGroupNameColumn);
    XSSFDataValidation dataValidationDeleteGroupName = (XSSFDataValidation) dataValidationHelper
        .createValidation(
            deleteGroupNameConstraint, deleteGroupNameCreate);
    dataValidationDeleteGroupName.setShowErrorBox(true);
    sheetOne.addValidationData(dataValidationDeleteGroupName);

    //set tên trang excel
    workbook.setSheetName(0, I18n.getLanguage("employee.approve.title"));
    workbook.setSheetHidden(1, true);
    sheetParam.setSelected(false);

    //set tên file excel
    String fileResult = tempFolder + File.separator;
    String fileName = "IMPORT_APPROVE_USERS" + "_" + System.currentTimeMillis() + ".xlsx";
    excelWriterUtils.saveToFileExcel(workbook, fileResult, fileName);
    String resultPath = fileResult + fileName;
    File fileExport = new File(resultPath);
    return fileExport;
  }

  @Override
  public ResultInSideDto importApproveUsers(MultipartFile multipartFile) throws Exception {
    UserToken userToken = ticketProvider.getUserToken();
    List<UsersInsideDto> usersInsideDtoList = new ArrayList<>();
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
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
        File fileImport = new File(filePath);

        List<Object[]> headerList;
        headerList = CommonImport.getDataFromExcelFile(
            fileImport,
            0,
            4,
            0,
            4,
            1000
        );
        //Kiểm tra form header có đúng chuẩn
        if (headerList.size() == 0 || !validApproveUserFileFormat(headerList)) {
          resultInSideDto.setKey(RESULT.FILE_INVALID_FORMAT);
          return resultInSideDto;
        }
        //Lấy dữ liệu import
        List<Object[]> dataImportList = CommonImport.getDataFromExcelFileNew(
            fileImport,
            0,
            5,
            0,
            4,
            1000
        );
        if (dataImportList.size() > 1500) {
          resultInSideDto.setKey(RESULT.DATA_OVER);
          return resultInSideDto;
        }
        if (!dataImportList.isEmpty()) {
          //dungpv add quyen 23/09/2020
          UsersInsideDto userSearch = new UsersInsideDto();
          userSearch.setApproveStatus(1L);
          List<RolesDTO> roles = roleUserRepository.getListRoleCodeByUserId(userToken.getUserID());
          if (roles != null) {
            if (roles.stream().filter(
                o -> (ROLE.ROLE_USER_ADMIN.equals(o.getRoleCode()) || ROLE.ROLE_USER_GNOC_EDIT_USER
                    .equals(o.getRoleCode()))).findFirst().isPresent()) {
              userSearch.setCheckingAdmin("1");
            } else if (roles.stream().filter(o -> ROLE.ROLE_USER_ROLE_TP.equals(o.getRoleCode()))
                .findFirst().isPresent()) {
              userSearch.setRelateUnits(String.valueOf(userToken.getDeptId()));
            } else {
              resultInSideDto.setKey(RESULT.FAIL);
              resultInSideDto.setMessage(I18n.getLanguage("employee.roleImport.approve"));
              return resultInSideDto;
            }
          } else {
            resultInSideDto.setKey(RESULT.FAIL);
            resultInSideDto.setKey(I18n.getLanguage("employee.roleImport.approve"));
            return resultInSideDto;
          }
          List<UsersInsideDto> lstUserApprove = userRepository.getListUsersDTOS(userSearch);
          Map<Long, String> mapCheckUserApprove = new HashMap<>();
          for (UsersInsideDto usersInsideDto : lstUserApprove) {
            if (usersInsideDto.getUserId() != null && usersInsideDto.getUserId() > 0) {
              mapCheckUserApprove.put(usersInsideDto.getUserId(), usersInsideDto.getUsername());
            }
          }
          //end
          int row = 4;
          int index = 0;
          for (Object[] obj : dataImportList) {
            UsersInsideDto usersInsideDto = new UsersInsideDto();
            if (obj[1] != null) {
              if (!DataUtil.isInteger(obj[1].toString().trim())) {
                usersInsideDto
                    .setResultImport(usersInsideDto.getResultImport() + "\n" + I18n
                        .getLanguage("employee.errType.userIdName"));
                usersInsideDto.setUserIdName(obj[1].toString().trim());
              } else {
                usersInsideDto.setUserIdName(obj[1].toString().trim());
                usersInsideDto.setUserId(Long.valueOf(obj[1].toString().trim()));
              }
            } else {
              usersInsideDto.setUserIdName(null);
            }
            if (obj[2] != null) {
              usersInsideDto.setApproveStatusName(obj[2].toString().trim());
              if (I18n.getLanguage("employee.approveStatus.2")
                  .equals(usersInsideDto.getApproveStatusName())) {
                usersInsideDto.setApproveStatus(2L);
              } else if (I18n.getLanguage("employee.approveStatus.3")
                  .equals(usersInsideDto.getApproveStatusName())) {
                usersInsideDto.setApproveStatus(3L);
              } else {
                usersInsideDto.setApproveStatus(null);
              }
            } else {
              usersInsideDto.setApproveStatusName(null);
            }
            if (obj[3] != null) {
              usersInsideDto.setDeleteGroupName(obj[3].toString().trim());
              if (I18n.getLanguage("employee.deleteGroup.1")
                  .equals(usersInsideDto.getDeleteGroupName())) {
                usersInsideDto.setDeleteGroup(1L);
              } else if (I18n.getLanguage("employee.deleteGroup.0")
                  .equals(usersInsideDto.getDeleteGroupName())) {
                usersInsideDto.setDeleteGroup(0L);
              } else {
                usersInsideDto.setDeleteGroup(null);
              }
            } else {
              usersInsideDto.setDeleteGroupName(null);
            }
            if (obj[4] != null) {
              usersInsideDto.setReasonRefusal(obj[4].toString().trim());
            } else {
              usersInsideDto.setReasonRefusal(null);
            }
            UsersInsideDto usersInsideDtoTmp = validateApproveUserImportInfo(
                usersInsideDto, usersInsideDtoList, mapCheckUserApprove);

            if (usersInsideDtoTmp.getResultImport() == null) {
              usersInsideDtoTmp
                  .setResultImport(I18n.getLanguage("employee.result.import"));
              usersInsideDtoList.add(usersInsideDtoTmp);
            } else {
              usersInsideDtoList.add(usersInsideDtoTmp);
              index++;
            }
            row++;
          }
          if (index == 0) {
            if (!usersInsideDtoList.isEmpty()) {
              resultInSideDto = insertImport(usersInsideDtoList);
              if (RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
                File fileExport = exportFileEx(usersInsideDtoList,
                    Constants.RESULT_IMPORT);
                resultInSideDto.setKey(RESULT.SUCCESS);
                resultInSideDto.setFile(fileExport);
              }
            }
          } else {
            File fileExport = exportFileEx(usersInsideDtoList,
                Constants.RESULT_IMPORT);
            resultInSideDto.setKey(RESULT.ERROR);
            resultInSideDto.setFile(fileExport);
          }
        } else {
          resultInSideDto.setKey(RESULT.NODATA);
          resultInSideDto.setMessage(Constants.FILE_NULL);
          File fileExport = exportFileEx(usersInsideDtoList,
              Constants.RESULT_IMPORT);
          resultInSideDto.setFile(fileExport);
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

  @Override
  public File exportApproveUsersData(UsersInsideDto usersInsideDto) throws Exception {
    log.debug("Request to exportData: {}", usersInsideDto);
    List<UsersInsideDto> lstInsideUser = null;
    usersInsideDto.setCheckingExport("1");
    if (usersInsideDto != null) {
      UserToken userToken = ticketProvider.getUserToken();
      UnitDTO unitDTO = unitRepository.findUnitById(userToken.getDeptId());
      if (unitDTO != null) {
        if (userRepository.checkRoleOfUser(ROLE.ROLE_USER_ADMIN, userToken.getUserID())) {
          usersInsideDto.setApproveStatus(1L);
          usersInsideDto.setCheckingAdmin("1");
          lstInsideUser = userRepository.listUserByDTO(usersInsideDto);
        } else if (userRepository.checkRoleOfUser(ROLE.ROLE_USER_ROLE_TP, userToken.getUserID())) {
          usersInsideDto.setRelateUnits(String.valueOf(unitDTO.getUnitId()));
          usersInsideDto.setApproveStatus(1L);
          lstInsideUser = userRepository.listUserByDTO(usersInsideDto);
        }
      }
    }
    if (lstInsideUser != null && !lstInsideUser.isEmpty()) {
      for (UsersInsideDto user : lstInsideUser) {
        if (user.getIsEnable() != null) {
          if (user.getIsEnable() == 1L) {
            user.setEnableName(I18n.getLanguage("employee.isEnable.1"));
          } else {
            user.setEnableName(I18n.getLanguage("employee.isEnable.0"));
          }
        }
        if (!StringUtils.isStringNullOrEmpty(user.getDeleteGroup())) {
          if (user.getDeleteGroup() == 1L) {
            user.setDeleteGroupName(I18n.getLanguage("employee.deleteGroup.1"));
          } else {
            user.setDeleteGroupName(I18n.getLanguage("employee.deleteGroup.0"));
          }
        }
        if (!StringUtils.isStringNullOrEmpty(user.getApproveStatus())) {
          if (user.getApproveStatus() == 1L) {
            user.setApproveStatusName(I18n.getLanguage("employee.approveStatus.1"));
          } else if (user.getApproveStatus() == 2L) {
            user.setApproveStatusName(I18n.getLanguage("employee.approveStatus.2"));
          } else if (user.getApproveStatus() == 3L) {
            user.setApproveStatusName(I18n.getLanguage("employee.approveStatus.3"));
          }
        }
        if (StringUtils.isStringNullOrEmpty(user.getUnitNameNew())) {
          user.setUnitNameNew(user.getRelateUnitsName());
        }
      }
    }
    return exportFileEx(lstInsideUser, "EXPORT_DATA");
  }

  public File exportFileEx(List<UsersInsideDto> lstData, String key)
      throws Exception {
    String title;
    String fileNameOut;
    String subTitle = I18n
        .getLanguage("employee.export.exportDate", DateTimeUtils.convertDateOffset());
    List<ConfigFileExport> fileExports = new ArrayList<>();
    List<ConfigHeaderExport> lstHeaderSheet;
    if (Constants.RESULT_IMPORT.equals(key)) {
      title = I18n.getLanguage("employee.approve.title");
      fileNameOut = "APPROVE_USERS_RESULT_IMPORT";
      lstHeaderSheet = readerHeaderSheet("userIdName", "approveStatusName",
          "deleteGroupName", "reasonRefusal", "resultImport");
    } else {
      title = I18n.getLanguage("employee.approve.title");
      fileNameOut = "APPROVE_USERS_EXPORT";
      lstHeaderSheet = readerHeaderSheet("userId", "username",
          "fullname", "staffCode", "email", "mobile", "unitName",
          "unitNameNew", "relateUnitsName", "hrUnitName", "updateTimeUnit", "deleteGroupName",
          "approveStatusName", "approveUser", "resourceCode");
    }
    Map<String, String> filedSplit = new HashMap<>();
    ConfigFileExport configFileExport = new ConfigFileExport(
        lstData,
        title,
        title,
        subTitle,
        7,
        3,
        7,
        true,
        "language.employee",
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

  public List<ConfigHeaderExport> readerHeaderSheet(String... col) {
    List<ConfigHeaderExport> lstHeaderSheet = new ArrayList<>();
    for (int i = 0; i < col.length; i++) {
      lstHeaderSheet.add(new ConfigHeaderExport(col[i], "LEFT", false, 0, 0, new String[]{}, null,
          "STRING"));
    }
    return lstHeaderSheet;
  }
  //20200903 End --hieunv1 bổ sung import,export phê duyệt chuyển đv

  @Override
  public UsersInsideDto getUserDetailById(Long id) {
    UsersInsideDto usersInsideDto = userRepository.getUserByUserId(id).toDTO();
    return usersInsideDto;
  }

  @Override
  public List<UsersInsideDto> getListUsersDTOS(UsersInsideDto dto) {
    return userRepository.getListUsersDTOS(dto);
  }

  @Override
  public Datatable getListUsersByList(UsersInsideDto dto) {
    return userRepository.getListUsersByList(dto);
  }

  @Override
  public UsersInsideDto getUserDTOByUserName(String userName) {
    return userRepository.getUserDTOByUserName(userName.trim().toLowerCase());
  }

  @Override
  public UsersInsideDto getUserDTOByUserNameInnerJoint(String userName) {
    return userRepository.getUserDTOByUserNameInnerJoint(userName.trim().toLowerCase());
  }

  @Override
  public List<UsersInsideDto> getListUsersByListUserId(List<Long> ids) {
    return userRepository.getListUsersByListUserId(ids);
  }

  @Override
  public Double getOffsetFromUser(Long userId) {
    return userRepository.getOffsetFromUser(userId);
  }

  @Override
  public ResultInSideDto updateUserTimeZone(String userId, String timeZoneId) {
    return userRepository.updateUserTimeZone(userId, timeZoneId);
  }

  @Override
  public ResultInSideDto updateUserLanguage(String userId, String languageId) {
    return userRepository.updateUserLanguage(userId, languageId);
  }

  @Override
  public List<Users> getListUserByUnitCode(String unitCode, String allOfChildUnit) {
    return userRepository.getListUserByUnitCode(unitCode, allOfChildUnit);
  }

  @Override
  public UsersDTO getUserInfo(String userName, String staffCode) {
    return userRepository.getUserInfo(userName, staffCode);
  }

  @Override
  public UsersInsideDto getUserInfor() {
    UsersInsideDto usersInsideDto = new UsersInsideDto();
    UserToken userToken = ticketProvider.getUserToken();
    UsersEntity usersEntity = userRepository.getUserByUserName(userToken.getUserName());
    if (usersEntity != null) {
      usersInsideDto = usersEntity.toDTO();
      UnitDTO unitDTO = unitRepository.findUnitById(usersInsideDto.getUnitId());
      if (unitDTO != null) {
        usersInsideDto.setUnitName(unitDTO.getUnitName());
      }
      UserSmsDTO userSmsDTO = userSmsRepository.getDetail(usersInsideDto.getUserId());
      if (userSmsDTO != null) {
        usersInsideDto.setSmsType(userSmsDTO.getSmsType());
        usersInsideDto.setTypeCode(userSmsDTO.getTypeCode());
      }
    }
    return usersInsideDto;
  }

  @Override
  public ResultInSideDto updateUserInfor(UsersInsideDto usersInsideDto) {
    ResultInSideDto resultInSideDto = userRepository.updateUserApprove(usersInsideDto);
    if (resultInSideDto.getKey().equals(RESULT.SUCCESS)) {
      UserSmsDTO userSmsDTO = new UserSmsDTO();
      userSmsDTO.setUserId(usersInsideDto.getUserId());
      userSmsDTO.setTypeCode(usersInsideDto.getTypeCode());
      userSmsDTO.setSmsType(usersInsideDto.getSmsType());
      userSmsDTO.setLastUpdateTime(new Date());
      userSmsDTO.setEmail(usersInsideDto.getEmail());
      userSmsDTO.setMobile(usersInsideDto.getMobile());
      resultInSideDto = userSmsRepository.add(userSmsDTO);
    }
    return resultInSideDto;
  }

  @Override
  public Users getUserModelInfo(String userName, String staffCode) {
    return userRepository.getUserModelInfo(userName, staffCode);
  }

  @Override
  public List<ImpactSegmentDTO> getImpactSegment(String system, String active) {
    return userRepository.getImpactSegment(system, active);
  }

  @Override
  public ResultInSideDto deleteUser(Long userId) {
    return userRepository.deleteUser(userId);
  }

  @Override
  public ResultInSideDto addUser(UsersInsideDto usersInsideDto) {
    ResultInSideDto resultInSideDto = userRepository.addUser(usersInsideDto);
    try {
      //New user create
      UsersEntity user = userRepository.getUserByUserName(usersInsideDto.getUsername());
      if (user != null) {
        //add user sms
        if (resultInSideDto.getKey().equals(RESULT.SUCCESS)
            && usersInsideDto.getMessage() != null) {
          UserSmsDTO userSmsDTO = new UserSmsDTO();
          userSmsDTO.setSmsType(usersInsideDto.getMessage());
          userSmsDTO.setEmail(usersInsideDto.getEmail());
          userSmsDTO.setMobile(usersInsideDto.getMobile());
          userSmsDTO.setUserId(user.getUserId());
          userSmsDTO.setTypeCode(usersInsideDto.getTypeCode());
          resultInSideDto = userSmsRepository.insertUserSms(userSmsDTO);
        }

        //add role user
        if (resultInSideDto.getKey().equals(RESULT.SUCCESS) && !usersInsideDto.getLstRole()
            .isEmpty()) {
          RoleUserDTO roleUserDTO = new RoleUserDTO();
          List<String> lstRole = usersInsideDto.getLstRole();
          for (String role : lstRole) {
            roleUserDTO.setUserId(user.getUserId().toString());
            RolesEntity rolesEntity = roleUserRepository.findRoleById(Long.parseLong(role));
            if (rolesEntity != null) {
              roleUserDTO.setIsActive(rolesEntity.getStatus().toString());
              if (ROLE.ROLE_USER_ADMIN.equals(rolesEntity.getRoleCode().toUpperCase())) {
                roleUserDTO.setIsAdmin("1");
              } else {
                roleUserDTO.setIsAdmin("0");
              }
            }
            roleUserDTO.setRoleId(role);
            resultInSideDto = roleUserRepository.addRoleUser(roleUserDTO);
          }
        }
      }
      return resultInSideDto;
    } catch (Exception err) {
      resultInSideDto.setKey(RESULT.FAIL);
      log.error(err.getMessage(), err);
      return resultInSideDto;
    }
  }

  @Override
  public UsersInsideDto getUserDetaiById(Long id) {
    return userRepository.getUserDetaiById(id);
  }

  //08092020 Strat hieunv thêm hàm check role
  @Override
  public UsersInsideDto checkRole() {
    UsersInsideDto usersInsideDto = new UsersInsideDto();
    UserToken userToken = ticketProvider.getUserToken();
    List<RolesDTO> lstRolesDTO = new ArrayList<>();
    if (userToken != null) {
      if (isCheckRoleAdminVsa(userToken.getUserName())) {
        RolesDTO roles = new RolesDTO();
        roles.setRoleCode(ROLE.ROLE_USER_ADMIN);
        roles.setRoleName(ROLE.ROLE_USER_ADMIN);
        roles.setStatus("1");
        lstRolesDTO.add(roles);
        usersInsideDto.setLstRolesDTO(lstRolesDTO);
      } else {
        List<RolesDTO> lstRolesDTO1 = roleUserRepository
            .getListRoleCodeByUserId(userToken.getUserID());
        if (lstRolesDTO1 != null && lstRolesDTO1.stream()
            .filter(o -> ROLE.ROLE_USER_ROLE_TP.equals(o.getRoleCode()))
            .findFirst().isPresent()) {
          RolesDTO roles = new RolesDTO();
          roles.setRoleCode(ROLE.ROLE_USER_ROLE_TP);
          roles.setRoleName(ROLE.ROLE_USER_ROLE_TP);
          roles.setStatus("1");
          lstRolesDTO.add(roles);
        }
      }

      usersInsideDto.setLstRolesDTO(lstRolesDTO);
    }
    return usersInsideDto;
  }
  //08092020 End hieunv thêm hàm check role

  @Override
  public ResultInSideDto updateUser(UsersInsideDto usersInsideDto) {
    UserToken userToken = ticketProvider.getUserToken();
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    try {

      //Start -- 27082020 hieunv1 bổ sung xử lý chuyển đơn vị
      if (!StringUtils.isStringNullOrEmpty(usersInsideDto.getUnitIdNew())) {
        if (getRoleUserToClient(userToken) < 2) {
          usersInsideDto.setUnitId(usersInsideDto.getUnitIdNew());
//          usersInsideDto.setUnitIdNew(null);
          usersInsideDto.setRelateUnits(null);
          usersInsideDto.setApproveStatus(2L);
          usersInsideDto.setApproveUser(userToken.getUserName());
          usersInsideDto.setResourceCode(userToken.getUserName());
          usersInsideDto.setUpdateTimeUnit(new Date());
          if (!StringUtils.isStringNullOrEmpty(usersInsideDto.getDeleteGroup()) && "0"
              .equals(String.valueOf(usersInsideDto.getDeleteGroup()))) {
            usersInsideDto.setDeleteGroup(null);
          } else {
            //chưa có chức năng xóa nhóm nghiệp vụ
          }
        } else {
          usersInsideDto.setRelateUnits(String.valueOf(usersInsideDto.getUnitIdNew()));
          usersInsideDto.setApproveStatus(1L);
          usersInsideDto.setUpdateTimeUnit(new Date());
          usersInsideDto.setResourceCode(userToken.getUserName());
        }
      }
      //End -- 27082020 hieunv1 bổ sung xử lý chuyển đơn vị
      resultInSideDto = userRepository.updateUser(usersInsideDto);

      //Update user SMS
      if (usersInsideDto.getMessage() != null) {
        UserSmsDTO userSmsDTO = new UserSmsDTO();
        UserSmsDTO userSms = userSmsRepository.getDetail(usersInsideDto.getUserId());
        userSmsDTO.setSmsType(usersInsideDto.getMessage());
        userSmsDTO.setEmail(usersInsideDto.getEmail());
        userSmsDTO.setMobile(usersInsideDto.getMobile());
        userSmsDTO.setUserId(usersInsideDto.getUserId());
        userSmsDTO.setTypeCode(usersInsideDto.getTypeCode());
        if (userSms != null) {
          resultInSideDto = userSmsRepository.updateUserSms(userSmsDTO);
        } else {
          resultInSideDto = userSmsRepository.insertUserSms(userSmsDTO);
        }
      }

      //add role user
      List<String> lstRole = usersInsideDto.getLstRole();
      if (lstRole != null && !lstRole.isEmpty()) {
        RoleUserDTO roleUserDTO = new RoleUserDTO();
        List<RoleUserEntity> lstRoleUser = roleUserRepository
            .listRoleUser(usersInsideDto.getUserId());
        if (lstRoleUser != null) {
          for (RoleUserEntity roleUser : lstRoleUser) {
            if (!lstRole.contains(roleUser.getRoleId().toString())) {
              roleUserRepository.deleteRoleUser(roleUser);
            } else {
              lstRole.remove(roleUser.getRoleId().toString());
            }
          }
          for (String role : lstRole) {
            roleUserDTO.setUserId(usersInsideDto.getUserId().toString());
            RolesEntity rolesEntity = roleUserRepository.findRoleById(Long.parseLong(role));
            if (rolesEntity != null) {
              roleUserDTO.setIsActive(rolesEntity.getStatus().toString());
              if (ROLE.ROLE_USER_ADMIN.equals(rolesEntity.getRoleCode().toUpperCase())) {
                roleUserDTO.setIsAdmin("1");
              } else {
                roleUserDTO.setIsAdmin("0");
              }
            }
            roleUserDTO.setRoleId(role);
            roleUserRepository.addRoleUser(roleUserDTO);
          }
        } else {
          for (String role : lstRole) {
            roleUserDTO.setUserId(usersInsideDto.getUserId().toString());
            RolesEntity rolesEntity = roleUserRepository.findRoleById(Long.parseLong(role));
            if (rolesEntity != null) {
              roleUserDTO.setIsActive(rolesEntity.getStatus().toString());
              if (ROLE.ROLE_USER_ADMIN.equals(rolesEntity.getRoleCode().toUpperCase())) {
                roleUserDTO.setIsAdmin("1");
              } else {
                roleUserDTO.setIsAdmin("0");
              }
            }
            roleUserDTO.setRoleId(role);
            roleUserRepository.addRoleUser(roleUserDTO);
          }
        }
      } else if (lstRole != null && lstRole.isEmpty()) {
        List<RoleUserEntity> lstRoleUser = roleUserRepository
            .listRoleUser(usersInsideDto.getUserId());
        for (RoleUserEntity roleUser : lstRoleUser) {
          roleUserRepository.deleteRoleUser(roleUser);
        }
      }
      return resultInSideDto;
    } catch (Exception err) {
      resultInSideDto.setKey(RESULT.FAIL);
      log.error(err.getMessage(), err);
      return resultInSideDto;
    }
  }

  @Override
  public UsersDTO getUnitNameByUserName(String username) {
    return userRepository.getUnitNameByUserName(username);
  }

  //20200904 Start itsol_hieunv sửa, bổ sung export qlttnv
  @Override
  public File exportData(UsersInsideDto usersInsideDto) throws Exception {
    usersInsideDto.setCheckingExport("1");
    List<UsersInsideDto> lstInsideUser = new ArrayList<>();
    UserToken userToken = ticketProvider.getUserToken();
    List<RolesDTO> roles = roleUserRepository.getListRoleCodeByUserId(userToken.getUserID());
    if (userToken != null && roles != null) {
      if (roles.stream().filter(
          o -> (ROLE.ROLE_USER_ADMIN.equals(o.getRoleCode()) || ROLE.ROLE_USER_GNOC_EDIT_USER
              .equals(o.getRoleCode()))).findFirst().isPresent()) {
        lstInsideUser = userRepository.listUserByDTO(usersInsideDto);
      } else if (roles.stream().filter(o -> ROLE.ROLE_USER_ROLE_TP.equals(o.getRoleCode()))
          .findFirst().isPresent()) {
        UsersEntity usersEntity = userRepository.getUserByUserName(userToken.getUserName());
        if (usersEntity != null) {
          List<String> lstUnit = new ArrayList<>();
          if (usersEntity.getUnitId() != null && usersEntity.getUnitId() > 0) {
            lstUnit.add(usersEntity.getUnitId().toString());
          }
          if (StringUtils.isNotNullOrEmpty(usersEntity.getRelateUnits())) {
            lstUnit.add(usersEntity.getRelateUnits());
          }
          if (lstUnit != null && !lstUnit.isEmpty()) {
            usersInsideDto.setLstUnitLogin(lstUnit);
          }
        }
        lstInsideUser = userRepository.listUserByDTO(usersInsideDto);
      } else {
        usersInsideDto.setUserId(userToken.getUserID());
        lstInsideUser = userRepository.listUserByDTO(usersInsideDto);
      }
    }
    SimpleDateFormat dfm = new SimpleDateFormat("dd/MM/yyyy");
    Map<String, String> mapActionLog = Constants.USER_ACTION_LOG.getActionLogName();
    if (lstInsideUser != null && !lstInsideUser.isEmpty()) {
      for (UsersInsideDto user : lstInsideUser) {
        if (user.getIsEnable() != null) {
          if (user.getIsEnable() == 1L) {
            user.setEnableName(I18n.getLanguage("employee.isEnable.1"));
          } else {
            user.setEnableName(I18n.getLanguage("employee.isEnable.0"));
          }
        }
        if (user.getBirthDay() != null) {
          String birthDay = DateTimeUtils
              .convertDateToString(user.getBirthDay(), Constants.ddMMyyyy);
          user.setBirthDay(dfm.parse(birthDay));
        } else {
          user.setBirthDay(null);
        }
        if (!StringUtils.isStringNullOrEmpty(user.getDeleteGroup())) {
          if (user.getDeleteGroup() == 0L) {
            user.setDeleteGroupName(I18n.getLanguage("employee.deleteGroup.0"));
          } else {
            user.setDeleteGroupName(I18n.getLanguage("employee.deleteGroup.1"));
          }
        }
        if (!StringUtils.isStringNullOrEmpty(user.getIsNotReceiveMessage())) {
          if (user.getIsNotReceiveMessage() == 1L) {
            user.setIsNotReceiveMessageName(I18n.getLanguage("employee.isNotReceiveMessage.1"));
          } else {
            user.setIsNotReceiveMessageName(I18n.getLanguage("employee.isNotReceiveMessage.0"));
          }
        }
        if (user.getUpdateTimeUnit() != null) {
          String updateTimeUnit = DateTimeUtils
              .convertDateToString(user.getUpdateTimeUnit(), Constants.ddMMyyyy);
          user.setUpdateTimeUnit(dfm.parse(updateTimeUnit));
        } else {
          user.setUpdateTimeUnit(null);
        }
        if (!StringUtils.isStringNullOrEmpty(user.getActionLog())) {
          if (!mapActionLog.isEmpty()) {
            for (Map.Entry<String, String> map : mapActionLog.entrySet()) {
              user.setActionLog(
                  user.getActionLog().replace(map.getKey(), I18n.getLanguage(map.getValue())));
            }
          }
        }
      }
    }
    String[] header = new String[]{
        "username", "fullname", "staffCode", "unitName", "enableName", "email", "mobile",
        "birthDay", "isNotReceiveMessageName", "unitNameNew",
        "deleteGroupName", "relateUnitsName", "hrUnitName", "updateTimeUnit", "resourceCode",
        "approveUser", "actionLog"
    };
    return handleFileExport(lstInsideUser, header, DateUtil.date2ddMMyyyyHHMMss(new Date()),
        "");
  }
  //20200904 End itsol_hieunv sửa, bổ sung export qlttnv

  @Override
  public File getTemplate() throws IOException {
//    dungpv add quyen 23/09/2020
    UserToken userToken = ticketProvider.getUserToken();
    List<RolesDTO> roles = roleUserRepository.getListRoleCodeByUserId(userToken.getUserID());
    boolean checkAdmin = false;
    if (userToken != null && roles != null) {
      if (roles.stream().filter(
          o -> (ROLE.ROLE_USER_ADMIN.equals(o.getRoleCode()) || ROLE.ROLE_USER_GNOC_EDIT_USER
              .equals(o.getRoleCode()))).findFirst().isPresent()) {
        checkAdmin = true;
      } else if (roles.stream().filter(o -> ROLE.ROLE_USER_ROLE_TP.equals(o.getRoleCode()))
          .findFirst().isPresent()) {
        checkAdmin = false;
      } else {
        return null;
      }
    }
    //end
    ExcelWriterUtils ewu = new ExcelWriterUtils();
    String templatePathOut = "templates" + File.separator + "TEMPLATE_EXPORT.xlsx";
    Resource resource = new ClassPathResource(templatePathOut);
    InputStream fileTemplate = resource.getInputStream();
    XSSFWorkbook workBook = new XSSFWorkbook(fileTemplate);

    Map<String, CellStyle> styles = CommonExport.createStyles(workBook);

    XSSFSheet sheetMain = workBook.getSheetAt(0);
    XSSFSheet sheetOrther = workBook.createSheet("Orther");
    XSSFSheet sheetRole = workBook.createSheet("Role");
    XSSFSheet sheetUnit = workBook.createSheet("Unit");

    String[] header = new String[]{
        I18n.getLanguage("employee.stt"),
        I18n.getLanguage("employee.username"),
        I18n.getLanguage("employee.staffCode"),
        I18n.getLanguage("employee.mobile"),
        I18n.getLanguage("employee.fullname"),
        I18n.getLanguage("employee.birthDay"),
        I18n.getLanguage("employee.email"),
        I18n.getLanguage("employee.unitCode"),
        I18n.getLanguage("employee.userLanguage"),
        I18n.getLanguage("employee.enableName"),
        I18n.getLanguage("employee.roleCode"),
        I18n.getLanguage("employee.unitNameNew"),
        I18n.getLanguage("employee.deleteGroupName"),
        I18n.getLanguage("employee.action")
    };
    String[] headerStar = new String[]{
        I18n.getLanguage("employee.username"),
        I18n.getLanguage("employee.staffCode"),
        I18n.getLanguage("employee.mobile"),
        I18n.getLanguage("employee.fullname"),
        I18n.getLanguage("employee.birthDay"),
        I18n.getLanguage("employee.email"),
        I18n.getLanguage("employee.unitCode"),
        I18n.getLanguage("employee.userLanguage"),
        I18n.getLanguage("employee.enableName"),
        I18n.getLanguage("employee.action")
    };
    XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(sheetMain);
    List<String> listHeader = Arrays.asList(header);
    List<String> listHeaderStar = Arrays.asList(headerStar);

    int sttColumn = listHeader.indexOf(I18n.getLanguage("employee.stt"));
    int usernameColumn = listHeader.indexOf(I18n.getLanguage("employee.username"));
    int staffCodeColumn = listHeader.indexOf(I18n.getLanguage("employee.staffCode"));
    int mobileColumn = listHeader.indexOf(I18n.getLanguage("employee.mobile"));
    int fullnameColumn = listHeader.indexOf(I18n.getLanguage("employee.fullname"));
    int birthDayColumn = listHeader.indexOf(I18n.getLanguage("employee.birthDay"));
    int emailColumn = listHeader.indexOf(I18n.getLanguage("employee.email"));
    int unitIdColumn = listHeader.indexOf(I18n.getLanguage("employee.unitCode"));
    int userLanguageColumn = listHeader.indexOf(I18n.getLanguage("employee.userLanguage"));
    int statusColumn = listHeader.indexOf(I18n.getLanguage("employee.enableName"));
    int roleCodeColumn = listHeader.indexOf(I18n.getLanguage("employee.roleCode"));
    int actionColumn = listHeader.indexOf(I18n.getLanguage("employee.action"));
    int deleteGroupNameColumn = listHeader.indexOf(I18n.getLanguage("employee.deleteGroupName"));
    int unitNameNewColumn = listHeader.indexOf(I18n.getLanguage("employee.unitNameNew"));

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
    mainCellTitle.setCellValue(I18n.getLanguage("employee.template.title"));
    mainCellTitle.setCellStyle(cellStyleTitle);
    sheetMain.addMergedRegion(new CellRangeAddress(3, 3, 1, 7));

    XSSFFont fontStar = workBook.createFont();
    fontStar.setColor(IndexedColors.RED.getIndex());

    //Tao header
    Row headerRow = sheetMain.createRow(5);
    Row headerRole = sheetRole.createRow(0);
    Row headerUnit = sheetUnit.createRow(0);
    headerRow.setHeight((short) 600);
    for (int i = 0; i < listHeader.size(); i++) {
      Cell headerCell = headerRow.createCell(i);
      XSSFRichTextString rt = new XSSFRichTextString(listHeader.get(i));
      for (String checkHeaderStar : listHeaderStar) {
        if (checkHeaderStar.equalsIgnoreCase(listHeader.get(i))) {
          rt.append(" (*)", fontStar);
        }
      }
      if (i == 11 || i == 12) {
        Drawing drawing = headerCell.getSheet().createDrawingPatriarch();
        CreationHelper factory = workBook.getCreationHelper();
        ClientAnchor anchor = factory.createClientAnchor();
        anchor.setCol1(i + 1);
        anchor.setCol2(i + 2);
        anchor.setRow1(3);
        anchor.setRow2(4);
        Comment comment = drawing.createCellComment(anchor);
        comment.setString(
            factory.createRichTextString(I18n.getLanguage("employee.createRichTextString")));
        headerCell.setCellComment(comment);
      }
      headerCell.setCellValue(rt);
      headerCell.setCellStyle(cellStyleHeader);
    }

    //set data select
    int row = 1;
    List<String> listStatus = new ArrayList<>();
    listStatus.add(I18n.getLanguage("employee.isEnable.1"));
    listStatus.add(I18n.getLanguage("employee.isEnable.0"));
    for (String dto : listStatus) {
      ewu.createCell(sheetOrther, 0, row++, dto, styles.get("cell"));
    }
    Name userStatusName = workBook.createName();
    userStatusName.setNameName("userStatusName");
    userStatusName.setRefersToFormula("Orther!$A$2:$A$" + row);
    XSSFDataValidationConstraint userStatusConstraint = new XSSFDataValidationConstraint(
        DataValidationConstraint.ValidationType.LIST, "userStatusName");
    CellRangeAddressList cellRangeUserStatus = new CellRangeAddressList(6, (maxRecord + 6),
        statusColumn, statusColumn);
    XSSFDataValidation dataValidationUserStatus = (XSSFDataValidation) dvHelper
        .createValidation(userStatusConstraint, cellRangeUserStatus);
    dataValidationUserStatus.setShowErrorBox(true);
    sheetMain.addValidationData(dataValidationUserStatus);

    row = 1;
    List<GnocLanguageDto> listLanguage = commonRepository.getAllGnocLanguage();
    for (GnocLanguageDto dto : listLanguage) {
      ewu.createCell(sheetOrther, 1, row++, dto.getLanguageName(), styles.get("cell"));
    }
    Name userLanguageName = workBook.createName();
    userLanguageName.setNameName("userLanguageName");
    userLanguageName.setRefersToFormula("Orther!$B$2:$B$" + row);
    XSSFDataValidationConstraint userLanguageConstraint = new XSSFDataValidationConstraint(
        DataValidationConstraint.ValidationType.LIST, "userLanguageName");
    CellRangeAddressList cellRangeUserLanguage = new CellRangeAddressList(6, (maxRecord + 6),
        userLanguageColumn, userLanguageColumn);
    XSSFDataValidation dataValidationUserLanguage = (XSSFDataValidation) dvHelper.createValidation(
        userLanguageConstraint, cellRangeUserLanguage);
    dataValidationUserLanguage.setShowErrorBox(true);
    sheetMain.addValidationData(dataValidationUserLanguage);

    row = 1;
    ewu.createCell(sheetOrther, 2, row++, I18n.getLanguage("employee.action.0"),
        styles.get("cell"));
    ewu.createCell(sheetOrther, 2, row++, I18n.getLanguage("employee.action.1"),
        styles.get("cell"));
    Name actionName = workBook.createName();
    actionName.setNameName("actionName");
    actionName.setRefersToFormula("Orther!$C$2:$C$" + row);
    XSSFDataValidationConstraint actionConstraint = new XSSFDataValidationConstraint(
        DataValidationConstraint.ValidationType.LIST, "actionName");
    CellRangeAddressList cellRangeAction = new CellRangeAddressList(6, (maxRecord + 6),
        actionColumn, actionColumn);
    XSSFDataValidation dataValidationAction = (XSSFDataValidation) dvHelper.createValidation(
        actionConstraint, cellRangeAction);
    dataValidationAction.setShowErrorBox(true);
    sheetMain.addValidationData(dataValidationAction);

    row = 1;
    ewu.createCell(sheetOrther, 3, row++, I18n.getLanguage("employee.deleteGroup.1"),
        styles.get("cell"));
    ewu.createCell(sheetOrther, 3, row++, I18n.getLanguage("employee.deleteGroup.0"),
        styles.get("cell"));
    Name deleteGroupName = workBook.createName();
    deleteGroupName.setNameName("deleteGroupName");
    deleteGroupName.setRefersToFormula("Orther!$D$2:$D$" + row);
    XSSFDataValidationConstraint deleteGroupNameConstraint = new XSSFDataValidationConstraint(
        DataValidationConstraint.ValidationType.LIST, "deleteGroupName");
    CellRangeAddressList cellRangeDeleteGroupName = new CellRangeAddressList(6, (maxRecord + 6),
        deleteGroupNameColumn, deleteGroupNameColumn);
    XSSFDataValidation dataValidationDeleteGroupName = (XSSFDataValidation) dvHelper
        .createValidation(
            deleteGroupNameConstraint, cellRangeDeleteGroupName);
    dataValidationDeleteGroupName.setShowErrorBox(true);
    sheetMain.addValidationData(dataValidationDeleteGroupName);

    sheetMain.setColumnWidth(sttColumn, 2000);
    sheetMain.setColumnWidth(staffCodeColumn, 6000);
    sheetMain.setColumnWidth(mobileColumn, 8000);
    sheetMain.setColumnWidth(fullnameColumn, 7000);
    sheetMain.setColumnWidth(birthDayColumn, 6000);
    sheetMain.setColumnWidth(emailColumn, 8000);
    sheetMain.setColumnWidth(unitIdColumn, 8000);
    sheetMain.setColumnWidth(roleCodeColumn, 8000);
    sheetMain.setColumnWidth(statusColumn, 6000);
    sheetMain.setColumnWidth(usernameColumn, 6000);
    sheetMain.setColumnWidth(userLanguageColumn, 4000);
    sheetMain.setColumnWidth(actionColumn, 7000);
    sheetMain.setColumnWidth(deleteGroupNameColumn, 6000);
    sheetMain.setColumnWidth(unitNameNewColumn, 6000);

    Cell roleNameColunm = headerRole.createCell(0);
    Cell roleCodeColunm = headerRole.createCell(1);
    XSSFRichTextString roleName = new XSSFRichTextString(
        I18n.getLanguage("employee.import.roleName"));
    XSSFRichTextString roleCode = new XSSFRichTextString(
        I18n.getLanguage("employee.import.roleCode"));
    roleNameColunm.setCellValue(roleName);
    roleNameColunm.setCellStyle(styles.get("header"));
    roleCodeColunm.setCellValue(roleCode);
    roleCodeColunm.setCellStyle(styles.get("header"));
    sheetRole.setColumnWidth(0, 10000);
    sheetRole.setColumnWidth(1, 10000);

    Cell headerCellStt = headerUnit.createCell(0);
    Cell headerCellUnitId = headerUnit.createCell(1);
    Cell headerCellUnit = headerUnit.createCell(2);
    XSSFRichTextString stt = new XSSFRichTextString(I18n.getLanguage("employee.stt"));
    XSSFRichTextString unitId = new XSSFRichTextString(I18n.getLanguage("employee.unitId"));
    XSSFRichTextString unit = new XSSFRichTextString(I18n.getLanguage("employee.unitName"));
    headerCellStt.setCellValue(stt);
    headerCellStt.setCellStyle(styles.get("header"));
    headerCellUnitId.setCellValue(unitId);
    headerCellUnitId.setCellStyle(styles.get("header"));
    headerCellUnit.setCellValue(unit);
    headerCellUnit.setCellStyle(styles.get("header"));
    sheetUnit.setColumnWidth(1, 12000);
    sheetUnit.setColumnWidth(2, 20000);

    //Set data to sheet role user
//    dungpv edit 23/09/2020
    UnitDTO unitDTO = new UnitDTO();
    if (checkAdmin) {
      List<RolesDTO> lstRole = commonRepository.getListRole();
      row = 1;
      for (RolesDTO role : lstRole) {
        ewu.createCell(sheetRole, 0, row, role.getRoleName(), styles.get("cell"));
        ewu.createCell(sheetRole, 1, row++, role.getRoleCode(), styles.get("cell"));
      }
    } else {
      UsersEntity usersEntity = userRepository.getUserByUserName(userToken.getUserName());
      if (usersEntity != null) {
        List<String> lstUnit = new ArrayList<>();
        if (usersEntity.getUnitId() != null && usersEntity.getUnitId() > 0) {
          lstUnit.add(usersEntity.getUnitId().toString());
        }
        if (StringUtils.isNotNullOrEmpty(usersEntity.getRelateUnits())) {
          lstUnit.add(usersEntity.getRelateUnits());
        }
        if (lstUnit != null && !lstUnit.isEmpty()) {
          unitDTO.setLstUnitIds(lstUnit);
        }
      }
    }
    row = 1;
    List<UnitDTO> unitNameList = unitBusiness.getListUnit(unitDTO);
    for (UnitDTO dto : unitNameList) {
      ewu.createCell(sheetUnit, 1, row, dto.getUnitId().toString(), styles.get("cell"));
      ewu.createCell(sheetUnit, 2, row++, dto.getUnitName(), styles.get("cell"));
    }
    for (row = 1; row <= unitNameList.size(); row++) {
      ewu.createCell(sheetUnit, 0, row, String.valueOf(row), styles.get("cell"));
    }
//    end
    sheetUnit.autoSizeColumn(0);
    sheetUnit.autoSizeColumn(1);

    //Set default data
    ewu.createCell(sheetMain, 3, 4, I18n.getLanguage("employee.import.require"),
        styles.get("cell"));

    sheetOrther.setSelected(false);
    workBook.setSheetHidden(1, true);
    workBook.setSheetName(0, I18n.getLanguage("employee.import.title"));
    workBook.setSheetName(2, I18n.getLanguage("employee.import.role"));
    workBook.setSheetName(3, I18n.getLanguage("employee.import.unit"));
    String fileResult = tempFolder + File.separator;
    String fileName = "IMPORT_EMPLOYEE" + "_" + System.currentTimeMillis() + ".xlsx";
    ewu.saveToFileExcel(workBook, fileResult, fileName);
    String resultPath = fileResult + fileName;
    File fileExport = new File(resultPath);
    return fileExport;
  }

  @Override
  public ResultInSideDto importData(MultipartFile multipartFile) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    UserToken userToken = ticketProvider.getUserToken();
    List<UsersInsideDto> listDto;
    List<UsersInsideDto> listImportDto;
    Map<String, String> mapImportDTO;

    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

    String[] header = new String[]{
        "username", "staffCode", "mobile", "fullname", "birthDay", "email", "unitCode",
        "userLanguage", "enableName", "roleCode", "actionName", "unitNameNew", "deleteGroupName",
        "resultImport"};
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
            0, 13, 1000);
        if (lstHeader.size() == 0 || !validFileFormat(lstHeader)) {
          resultInSideDto.setKey(RESULT.FILE_INVALID_FORMAT);
          return resultInSideDto;
        }

        List<Object[]> lstData = CommonImport.getDataFromExcelFileNew(fileImp, 0, 6,
            0, 13, 1000);
        if (lstData != null && lstData.size() > maxRecord) {
          resultInSideDto.setKey(RESULT.DATA_OVER);
          resultInSideDto.setMessage(String.valueOf(maxRecord));
          return resultInSideDto;
        }
//        dungpv add 23/09/2020
        List<RolesDTO> roles = roleUserRepository.getListRoleCodeByUserId(userToken.getUserID());
        boolean checkAdmin = false;
        if (userToken != null && roles != null) {
          if (roles.stream().filter(
              o -> (ROLE.ROLE_USER_ADMIN.equals(o.getRoleCode()) || ROLE.ROLE_USER_GNOC_EDIT_USER
                  .equals(o.getRoleCode()))).findFirst().isPresent()) {
            checkAdmin = true;
          } else if (roles.stream().filter(o -> ROLE.ROLE_USER_ROLE_TP.equals(o.getRoleCode()))
              .findFirst().isPresent()) {
            checkAdmin = false;
          } else {
            return null;
          }
        }

//end
        listDto = new ArrayList<>();
        listImportDto = new ArrayList<>();
        mapImportDTO = new HashMap<>();
        if (!lstData.isEmpty()) {
          int index = 0;
          int value = 1;
          //dungpv add 23/09/2020
          mapUnit.clear();
          getMapUnit(checkAdmin, userToken.getUserName());
          mapCheckStaffCode.clear();
          mapCheckUserName.clear();
          getUserStaffCode(lstData);
          //end
          for (Object[] obj : lstData) {
            UsersInsideDto importDTO = new UsersInsideDto();
            if (obj[1] != null) {
              importDTO.setUsername(obj[1].toString().trim().toLowerCase());
            }
            if (obj[2] != null) {
              importDTO.setStaffCode(obj[2].toString().trim());
            }
            if (obj[3] != null) {
              importDTO.setMobile(obj[3].toString().trim());
            }
            if (obj[4] != null) {
              importDTO.setFullname(obj[4].toString().trim());
            }
            if (obj[5] != null) {
              Date birthDay = sdf.parse(obj[5].toString().trim());
              importDTO.setBirthDay(birthDay);
            }
            if (obj[6] != null) {
              importDTO.setEmail(obj[6].toString().trim().toLowerCase());
            }
            if (obj[7] != null) {
              importDTO.setUnitCode(obj[7].toString().trim());
            }
            if (obj[8] != null) {
              importDTO.setUserLanguage(obj[8].toString().trim());
            }
            if (obj[9] != null) {
              importDTO.setEnableName(obj[9].toString().trim());
            }
            if (obj[10] != null) {
              if (checkAdmin) {
                importDTO.setRoleCode(obj[10].toString().trim());
              }
            }
            if (obj[11] != null) {
              importDTO.setUnitNameNew(obj[11].toString().trim());
            }
            if (obj[12] != null) {
              importDTO.setDeleteGroupName(obj[12].toString().trim());
            }
            if (obj[13] != null) {
              importDTO.setActionName(obj[13].toString().trim());
            }
            UsersInsideDto tempImportDTO = validateImportInfo(importDTO, mapImportDTO);
            if (tempImportDTO.getResultImport() == null) {
              tempImportDTO.setResultImport(I18n.getLanguage("employee.result.import.ok"));
              listImportDto.add(tempImportDTO);
              listDto.add(tempImportDTO);
            } else {
              listImportDto.add(tempImportDTO);
              index++;
            }
            value++;
          }
          if (index == 0) {
            if (!listDto.isEmpty()) {
              for (UsersInsideDto dto : listDto) {
                if ("1".equals(String.valueOf(dto.getAction()))) {
                  resultInSideDto = updateUser(dto);
                } else {
                  resultInSideDto = insertOrUpdateUser(dto);
                }
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

  private void getUserStaffCode(List<Object[]> lstData) {
    List<String> lstUserName = new ArrayList<>();
    List<String> lstStaffCode = new ArrayList<>();
    List<String> lstUserNameSearch = new ArrayList<>();
    List<String> lstStaffCodeSearch = new ArrayList<>();
    for (Object[] obj : lstData) {
      if (obj[1] != null) {
        lstUserName.add(obj[1].toString().trim().toLowerCase());
      }
      if (obj[2] != null) {
        lstStaffCode.add(obj[2].toString().trim());
      }
    }
    if (lstUserName != null && !lstUserName.isEmpty()) {
      for (int i = 0; i < lstUserName.size(); i++) {
        lstUserNameSearch.add(lstUserName.get(i));
        if ((i != 0 && i % 500 == 0) || i == lstUserName.size() - 1) {
          List<UsersInsideDto> lstUsers = userRepository
              .getLstUsersByUserNameOrStaffCode(lstUserName, null);
          if (lstUsers != null && !lstUsers.isEmpty()) {
            lstUsers.forEach(item -> {
              if (StringUtils.isNotNullOrEmpty(item.getUsername()) && !mapCheckUserName
                  .containsKey(item.getUsername())) {
                mapCheckUserName
                    .put(item.getUsername().toLowerCase(), item);
              }
            });
          }
          lstUserNameSearch.clear();
        }
      }
    }
    if (lstStaffCode != null && !lstStaffCode.isEmpty()) {
      for (int i = 0; i < lstStaffCode.size(); i++) {
        lstStaffCodeSearch.add(lstUserName.get(i));
        if ((i != 0 && i % 500 == 0) || i == lstStaffCode.size() - 1) {
          List<UsersInsideDto> lstStaff = userRepository
              .getLstUsersByUserNameOrStaffCode(null, lstStaffCode);
          if (lstStaff != null && !lstStaff.isEmpty()) {
            lstStaff.forEach(item -> {
              if (StringUtils.isNotNullOrEmpty(item.getStaffCode()) && !mapCheckStaffCode
                  .containsKey(item.getStaffCode())) {
                mapCheckStaffCode
                    .put(item.getStaffCode().toLowerCase(), item.getStaffCode().toLowerCase());
              }
            });
          }
          lstStaffCodeSearch.clear();
        }
      }
    }
  }

  @Override
  public Datatable getListUserHistory(UserUpdateHisDTO userUpdateHisDTO) {
    return userRepository.getListUserHistory(userUpdateHisDTO);
  }

  @Override
  public UserUpdateHisDTO findUserHistoryById(Long hisId) {
    return userRepository.findUserHistoryById(hisId);
  }

  private File handleFileExport(List<UsersInsideDto> listExport, String[] columnExport,
      String date, String code) throws Exception {
    List<ConfigFileExport> fileExports = new ArrayList<>();
    Map<String, String> fieldSplit = new HashMap<>();
    List<ConfigHeaderExport> lstHeaderSheet1 = getListHeaderSheet(columnExport);
    String sheetName = I18n.getLanguage("employee.export.sheetname");
    String title = I18n.getLanguage("employee.export.title");
    String fileNameOut = I18n.getLanguage("employee.export.title");
    String subTitle;
    if (date != null) {
      Date fromDateTmp = DataUtil.convertStringToDate(date);
      DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
      subTitle = I18n
          .getLanguage("employee.export.eportDate", dateFormat.format(fromDateTmp));
    } else {
      subTitle = "";
    }
    ConfigFileExport configFileExport = new ConfigFileExport(
        listExport,
        sheetName,
        title,
        subTitle,
        7,
        3,
        6,
        true,
        "language.employee",
        lstHeaderSheet1,
        fieldSplit,
        "",
        I18n.getLanguage("schedule.cr.report.export.firstLeftHeader"),
        I18n.getLanguage("schedule.cr.report.export.secondLeftHeader"),
        I18n.getLanguage("schedule.cr.report.export.firstRightHeader"),
        I18n.getLanguage("schedule.cr.report.export.secondRightHeader")
    );
    configFileExport.setLangKey(I18n.getLocale());
    List<CellConfigExport> cellConfigExportList = new ArrayList<>();
    CellConfigExport cellConfigExport = new CellConfigExport(7, 0, 0, 0,
        I18n.getLanguage("employee.stt"), "HEAD", "STRING");
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

  private boolean validFileFormat(List<Object[]> lstHeader) {
    Object[] obj = lstHeader.get(0);
    int count = 0;
    for (Object data : obj) {
      if (data != null) {
        count += 1;
      }
    }
    if (count != 14) {
      return false;
    }
    if (obj == null) {
      return false;
    }
    if (obj[0] == null) {
      return false;
    }
    if (!(I18n.getLanguage("employee.stt"))
        .equalsIgnoreCase(obj[0].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("employee.username") + " (*)")
        .equalsIgnoreCase(obj[1].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("employee.staffCode") + " (*)")
        .equalsIgnoreCase(obj[2].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("employee.mobile") + " (*)")
        .equalsIgnoreCase(obj[3].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("employee.fullname") + " (*)")
        .equalsIgnoreCase(obj[4].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("employee.birthDay") + " (*)")
        .equalsIgnoreCase(obj[5].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("employee.email") + " (*)")
        .equalsIgnoreCase(obj[6].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("employee.unitCode") + " (*)")
        .equalsIgnoreCase(obj[7].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("employee.userLanguage") + " (*)")
        .equalsIgnoreCase(obj[8].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("employee.enableName") + " (*)")
        .equalsIgnoreCase(obj[9].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("employee.roleCode"))
        .equalsIgnoreCase(obj[10].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("employee.unitNameNew"))
        .equalsIgnoreCase(obj[11].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("employee.deleteGroupName"))
        .equalsIgnoreCase(obj[12].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("employee.action") + " (*)")
        .equalsIgnoreCase(obj[13].toString().trim())) {
      return false;
    }
    return true;
  }

  private UsersInsideDto validateImportInfo(UsersInsideDto importDTO,
      Map<String, String> mapImportDTO) {
    String resultImport = "";
    //Check null
    if (StringUtils.isStringNullOrEmpty(importDTO.getUsername())) {
      resultImport = resultImport.concat(I18n.getLanguage("employee.err.username"));
    }
    if (StringUtils.isStringNullOrEmpty(importDTO.getStaffCode())) {
      resultImport = checkResultImport(resultImport)
          .concat(I18n.getLanguage("employee.err.staffCode"));
    }
    if (StringUtils.isStringNullOrEmpty(importDTO.getMobile())) {
      resultImport = checkResultImport(resultImport)
          .concat(I18n.getLanguage("employee.err.mobile"));
    }
    if (StringUtils.isStringNullOrEmpty(importDTO.getFullname())) {
      resultImport = checkResultImport(resultImport)
          .concat(I18n.getLanguage("employee.err.fullname"));
    }
    if (StringUtils.isStringNullOrEmpty(importDTO.getBirthDay())) {
      resultImport = checkResultImport(resultImport)
          .concat(I18n.getLanguage("employee.err.birthDay"));
    }
    if (StringUtils.isStringNullOrEmpty(importDTO.getEmail())) {
      resultImport = checkResultImport(resultImport)
          .concat(I18n.getLanguage("employee.err.email"));
    }
    if (StringUtils.isStringNullOrEmpty(importDTO.getUnitCode())) {
      resultImport = checkResultImport(resultImport)
          .concat(I18n.getLanguage("employee.err.unitCode"));
    }
    if (!StringUtils.isStringNullOrEmpty(importDTO.getUnitCode()) && !StringUtils
        .isInteger(importDTO.getUnitCode())) {
      resultImport = checkResultImport(resultImport)
          .concat(I18n.getLanguage("employee.err.unitCode.isInteger"));
    }
    if (StringUtils.isStringNullOrEmpty(importDTO.getUserLanguage())) {
      resultImport = checkResultImport(resultImport)
          .concat(I18n.getLanguage("employee.err.userLanguage"));
    }
    if (StringUtils.isStringNullOrEmpty(importDTO.getEnableName())) {
      resultImport = checkResultImport(resultImport)
          .concat(I18n.getLanguage("employee.err.enableName"));
    }
    if (StringUtils.isStringNullOrEmpty(importDTO.getActionName())) {
      resultImport = checkResultImport(resultImport)
          .concat(I18n.getLanguage("employee.err.actionName"));
    }
    if (!StringUtils.isStringNullOrEmpty(importDTO.getUnitNameNew())) {
      if (!StringUtils.isInteger(importDTO.getUnitNameNew())) {
        resultImport = checkResultImport(resultImport)
            .concat(I18n.getLanguage("employee.err.unitIdNew.isInteger"));
      } else {
        importDTO.setUnitIdNew(Long.valueOf(importDTO.getUnitNameNew()));
      }
    }
    if (StringUtils.isNotNullOrEmpty(resultImport)) {
      importDTO.setResultImport(resultImport);
      return importDTO;
    }
    //Check value
    if (importDTO.getUsername() != null) {
      if (importDTO.getUsername().length() > 50) {
        importDTO.setResultImport(I18n.getLanguage("employee.err.username.valid"));
      }
      if (!checkUserName(importDTO.getUsername().trim())) {
        importDTO.setResultImport(I18n.getLanguage("employee.err.username.invalid"));
      }
    }
    if (importDTO.getStaffCode() != null) {
      if (importDTO.getStaffCode().length() > 50) {
        importDTO.setResultImport(I18n.getLanguage("employee.err.staffcode.valid"));
      }
      if (!checkUserName(importDTO.getStaffCode().trim())) {
        importDTO.setResultImport(I18n.getLanguage("employee.err.username.invalid"));
      }
    }
    if (importDTO.getMobile() != null) {
      if (importDTO.getMobile().length() > 15) {
        importDTO.setResultImport(I18n.getLanguage("employee.err.mobile.validmax"));
      }
      if (importDTO.getMobile().length() < 9) {
        importDTO.setResultImport(I18n.getLanguage("employee.err.mobile.validmin"));
      }
      if (!checkMobile(importDTO.getMobile().trim())) {
        importDTO.setResultImport(I18n.getLanguage("employee.err.mobile.valid"));
      }
    }
    if (importDTO.getEmail() != null) {
      if (!checkEmail(importDTO.getEmail().trim())) {
        importDTO.setResultImport(I18n.getLanguage("employee.err.email.valid"));
      }
    }
    if (importDTO.getFullname() != null) {
      if (importDTO.getFullname().length() > 100) {
        importDTO.setResultImport(I18n.getLanguage("employee.val.fullname"));
      }
    }
    if (importDTO.getUserLanguage() != null) {
      if (importDTO.getUserLanguage()
          .equalsIgnoreCase(I18n.getLanguage("employee.import.language.1"))) {
        importDTO.setUserLanguage("1");
      } else if (importDTO.getUserLanguage()
          .equalsIgnoreCase(I18n.getLanguage("employee.import.language.2"))) {
        importDTO.setUserLanguage("2");
      } else {
        importDTO.setUserLanguage("0");
      }
    }
    if (importDTO.getActionName() != null) {
      if (I18n.getLanguage("employee.action.0").equals(importDTO.getActionName())) {
        if (mapCheckUserName.containsKey(importDTO.getUsername().toLowerCase())) {
          importDTO.setResultImport(I18n.getLanguage("employee.err.exist"));
          return importDTO;
        }
        importDTO.setAction(0L);
        if (mapCheckStaffCode.containsKey(importDTO.getStaffCode().toLowerCase())) {
          importDTO.setResultImport(I18n.getLanguage("employee.staffCode.isExits"));
          return importDTO;
        }
      } else if (I18n.getLanguage("employee.action.1").equals(importDTO.getActionName())) {
        if (!mapCheckUserName.containsKey(importDTO.getUsername().toLowerCase())) {
          importDTO.setResultImport(I18n.getLanguage("employee.err.notfound"));
          return importDTO;
        }
        UsersInsideDto usersInsideDto = mapCheckUserName.get(importDTO.getUsername().toLowerCase());
        importDTO.setUserId(usersInsideDto.getUserId());
        importDTO.setAction(1L);
        if (!usersInsideDto.getStaffCode().toLowerCase()
            .equals(importDTO.getStaffCode().toLowerCase())) {
          if (mapCheckStaffCode.containsKey(importDTO.getStaffCode())) {
            importDTO.setResultImport(I18n.getLanguage("employee.staffCode.isExits"));
            return importDTO;
          }
        }
      }
    }
    if (importDTO.getEnableName() != null) {
      if (I18n.getLanguage("employee.isEnable.0").equals(importDTO.getEnableName())) {
        importDTO.setIsEnable(0L);
      } else if (I18n.getLanguage("employee.isEnable.1").equals(importDTO.getEnableName())) {
        importDTO.setIsEnable(1L);
      }
    }
    if (importDTO.getUnitCode() != null) {
      UnitDTO unitDto = mapUnit.get(importDTO.getUnitCode());
      if (unitDto == null) {
        importDTO.setResultImport(I18n.getLanguage("employee.unit.isExits"));
      } else {
        importDTO.setUnitId(unitDto.getUnitId());
      }
    }
    if (importDTO.getRoleCode() != null) {
      String[] lstRole = importDTO.getRoleCode().split(",");
      if (lstRole != null && lstRole.length > 0) {
        for (String roleCode : lstRole) {
          RolesDTO role = roleUserRepository.findRoleByCode(roleCode);
          if (role == null) {
            importDTO.setResultImport(
                "Role " + roleCode + " " + I18n.getLanguage("employee.role.isExits"));
          }
        }
      }
    }
    if (!StringUtils.isStringNullOrEmpty(importDTO.getAction()) && !StringUtils
        .isStringNullOrEmpty(importDTO.getUnitIdNew()) && "0"
        .equals(String.valueOf(importDTO.getAction()))) {
      importDTO.setResultImport(I18n.getLanguage("employee.err.unitNameNew.valid"));
      return importDTO;
    }
    if (!StringUtils.isStringNullOrEmpty(importDTO.getAction()) && !StringUtils
        .isStringNullOrEmpty(importDTO.getUnitIdNew()) && "1"
        .equals(String.valueOf(importDTO.getAction()))) {
      if (!mapUnit.containsKey(String.valueOf(importDTO.getUnitIdNew()))) {
        importDTO.setResultImport(I18n.getLanguage("employee.unit.isExits"));
        return importDTO;
      }
    }
    if (importDTO.getDeleteGroupName() != null) {
      if (I18n.getLanguage("employee.deleteGroup.0").equals(importDTO.getDeleteGroupName())) {
        importDTO.setDeleteGroup(0L);
      } else if (I18n.getLanguage("employee.deleteGroup.1")
          .equals(importDTO.getDeleteGroupName())) {
        importDTO.setDeleteGroup(1L);
      } else {
        importDTO.setResultImport(I18n.getLanguage("employee.err.deleteGroupName.exist"));
        return importDTO;
      }
    }
    String validateDuplicate = validateDuplicateImport(importDTO, mapImportDTO);
    if (StringUtils.isNotNullOrEmpty(validateDuplicate)) {
      importDTO.setResultImport(validateDuplicate);
      return importDTO;
    }
    return importDTO;
  }

  private String checkResultImport(String resultImport) {
    if (StringUtils.isNotNullOrEmpty(resultImport)) {
      resultImport = resultImport.concat("; \n");
    }
    return resultImport;
  }

  static boolean checkEmail(String email) {
    String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
    return email.matches(regex);
  }

  static boolean checkUserName(String userName) {
    String regex = "^[a-zA-Z0-9\\_\\-.]*$";
    return userName.matches(regex);
  }

  static boolean checkMobile(String mobile) {
    String regex = "^[+]?[0-9]*$";
    return mobile.matches(regex);
  }

  private String validateDuplicateImport(UsersInsideDto importDTO,
      Map<String, String> mapImportDTO) {
    String userName = importDTO.getUsername();
    String key = userName;
    String value = mapImportDTO.get(key);
    if (StringUtils.isNotNullOrEmpty(value)) {
      return I18n.getLanguage("employee.err.dup-code-in-file").replaceAll("0", value);
    }
    return null;
  }

  private ResultInSideDto insertOrUpdateUser(UsersInsideDto usersInsideDto) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    try {
      resultInSideDto = userRepository.insertOrUpdateUser(usersInsideDto);
      if (resultInSideDto.getKey() == RESULT.SUCCESS) {
        UsersEntity user = userRepository.getUserByUserName(usersInsideDto.getUsername());
        if (user != null) {
          usersInsideDto.setUserId(user.getUserId());
        }
        resultInSideDto = roleUserRepository.insertOrUpdateRoleUser(usersInsideDto);
      }
      return resultInSideDto;
    } catch (Exception err) {
      log.error(err.getMessage(), err);
      resultInSideDto.setKey(RESULT.FAIL);
      return resultInSideDto;
    }
  }

  private boolean validApproveUserFileFormat(List<Object[]> headerList) {
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
    if (count != 5) {
      return false;
    }
    if (objects[0] == null) {
      return false;
    }
    if (!I18n.getLanguage("common.STT").equalsIgnoreCase(objects[0].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("employee.userIdName") + "*")
        .equalsIgnoreCase(objects[1].toString().trim())) {
      return false;
    }
    if (!(I18n.getLanguage("employee.approveStatusName") + "*")
        .equalsIgnoreCase(objects[2].toString().trim())) {
      return false;
    }
    if (!I18n.getLanguage("employee.deleteGroupName")
        .equalsIgnoreCase(objects[3].toString().trim())) {
      return false;
    }
    if (!I18n.getLanguage("employee.reasonRefusal")
        .equalsIgnoreCase(objects[4].toString().trim())) {
      return false;
    }
    return true;
  }

  private UsersInsideDto validateApproveUserImportInfo(UsersInsideDto usersInsideDto,
      List<UsersInsideDto> list, Map<Long, String> mapCheckUserApprove) {
    if (StringUtils.isStringNullOrEmpty(usersInsideDto.getUserIdName())) {
      usersInsideDto.setResultImport(I18n.getLanguage("employee.err.userIdName"));
      return usersInsideDto;
    }

    if (StringUtils.isStringNullOrEmpty(usersInsideDto.getApproveStatusName())) {
      usersInsideDto.setResultImport(I18n.getLanguage("employee.err.approveStatusName"));
      return usersInsideDto;
    }

    if (StringUtils.isStringNullOrEmpty(usersInsideDto.getApproveStatus())) {
      usersInsideDto.setResultImport(I18n.getLanguage("employee.err.approveStatusName.exist"));
      return usersInsideDto;
    }

    if (!StringUtils.isStringNullOrEmpty(usersInsideDto.getDeleteGroupName()) && StringUtils
        .isStringNullOrEmpty(usersInsideDto.getDeleteGroup())) {
      usersInsideDto.setResultImport(I18n.getLanguage("employee.err.deleteGroupName.exist"));
      return usersInsideDto;
    }

    if ("3".equals(usersInsideDto.getApproveStatus().toString()) && StringUtils
        .isStringNullOrEmpty(usersInsideDto.getReasonRefusal())) {
      usersInsideDto.setResultImport(I18n.getLanguage("employee.err.reasonRefusal"));
      return usersInsideDto;
    }

    if (!mapCheckUserApprove.containsKey(usersInsideDto.getUserId())) {
      usersInsideDto.setResultImport(I18n.getLanguage("employee.errType.userIdName"));
      return usersInsideDto;
    }

    if (list != null && list.size() > 0 && usersInsideDto.getResultImport() == null) {
      usersInsideDto = validateApproveUserDuplicate(list, usersInsideDto);
    }
    return usersInsideDto;
  }

  private UsersInsideDto validateApproveUserDuplicate(List<UsersInsideDto> list,
      UsersInsideDto usersInsideDto) {
    for (int i = 0; i < list.size(); i++) {
      UsersInsideDto usersInsideDtoTmp = list.get(i);
      if (I18n.getLanguage("employee.result.import")
          .equals(usersInsideDtoTmp.getResultImport()) && usersInsideDtoTmp.getUserId()
          .equals(usersInsideDto.getUserId())) {
        usersInsideDto
            .setResultImport(I18n.getLanguage("employee.err.dup-code-in-file")
                .replaceAll("0", String.valueOf((i) + 1)));
        break;
      }
    }
    return usersInsideDto;
  }

  private ResultInSideDto insertImport(List<UsersInsideDto> usersInsideDtoList) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    for (UsersInsideDto usersInsideDto : usersInsideDtoList) {
      if (usersInsideDto != null) {
        if (!StringUtils.isStringNullOrEmpty(usersInsideDto.getApproveStatus()) && "2"
            .equals(String.valueOf(usersInsideDto.getApproveStatus()))) {
          resultInSideDto = updateUserApprove(true, usersInsideDto.getUserId(), null,
              usersInsideDto.getUnitIdNew(), usersInsideDto.getDeleteGroup());
        } else if (!StringUtils.isStringNullOrEmpty(usersInsideDto.getApproveStatus()) && "3"
            .equals(String.valueOf(usersInsideDto.getApproveStatus()))) {
          resultInSideDto = updateUserApprove(false, usersInsideDto.getUserId(),
              !StringUtils.isStringNullOrEmpty(usersInsideDto.getReasonRefusal()) ? usersInsideDto
                  .getReasonRefusal() : "", null, usersInsideDto.getDeleteGroup());
        }
      }
    }
    return resultInSideDto;
  }

  private void getMapUnit(boolean checkAdmin, String userName) {
    UnitDTO unitDTO = new UnitDTO();
    if (!checkAdmin) {
      UsersEntity usersEntity = userRepository.getUserByUserName(userName);
      if (usersEntity != null) {
        List<String> lstUnit = new ArrayList<>();
        if (usersEntity.getUnitId() != null && usersEntity.getUnitId() > 0) {
          lstUnit.add(usersEntity.getUnitId().toString());
        }
        if (StringUtils.isNotNullOrEmpty(usersEntity.getRelateUnits())) {
          lstUnit.add(usersEntity.getRelateUnits());
        }
        if (lstUnit != null && !lstUnit.isEmpty()) {
          unitDTO.setLstUnitIds(lstUnit);
        }
      }
    }
    List<UnitDTO> lsUnit = unitRepository.getListUnit(unitDTO);
    if (lsUnit != null && lsUnit.size() > 0) {
      for (UnitDTO dto : lsUnit) {
        mapUnit.put(dto.getUnitId().toString(), dto);
      }
    }
  }

  private boolean isCheckRoleAdminVsa(String username) {
    boolean isFullRoleVSA = false;
    try {
      Response response = vsaAdminPort.getRoleOfUsers(username);
      if (response != null) {
        List<Object> roleInfos = response.getValues();
        if (roleInfos != null) {
          StringUtils.printLogData("ROLE USER:", response, Response.class);
          isFullRoleVSA = roleInfos.stream().filter(item -> (
              (ROLE.ROLE_VSA_GNOC_ADMIN.equals(((UserRoleInfo) item).getRoleCode())
                  || ROLE.ROLE_VSA_GNOC_EDIT_USER
                  .equals(((UserRoleInfo) item).getRoleCode())) && "1"
                  .equals(String.valueOf(((UserRoleInfo) item).getRoleStatus())) && "1"
                  .equals(String.valueOf(((UserRoleInfo) item).getUserRoleStatus())))).findFirst()
              .isPresent();
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return isFullRoleVSA;
  }

  private boolean isCheckRoleAdminForApproveVsa(String username) {
    boolean isFullRoleVSA = false;
    try {
      Response response = vsaAdminPort.getRoleOfUsers(username);
      if (response != null) {
        List<Object> roleInfos = response.getValues();
        if (roleInfos != null) {
          StringUtils.printLogData("ROLE USER:", response, Response.class);
          isFullRoleVSA = roleInfos.stream().filter(item -> (
              (ROLE.ROLE_VSA_GNOC_ADMIN.equals(((UserRoleInfo) item).getRoleCode())) && "1"
                  .equals(String.valueOf(((UserRoleInfo) item).getUserRoleStatus())))).findFirst()
              .isPresent();
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return isFullRoleVSA;
  }

  public Long getRoleUserToClient(UserToken userToken) {
    boolean isFullRoleVSA = isCheckRoleAdminVsa(userToken.getUserName());
    if (isFullRoleVSA) {
      return 0L; //full quyen
    }
    List<RolesDTO> roles = roleUserRepository.getListRoleCodeByUserId(userToken.getUserID());
    if (roles != null && roles.stream()
        .filter(o -> ROLE.ROLE_USER_ROLE_TP.equals(o.getRoleCode()))
        .findFirst().isPresent()) {
      return 1L; //quyen TP
    } else {
      return 2L; //quyen view voi user hien tai
    }
  }

  @Override
  public List<UsersDTO> getUserDTO() {
    return userRepository.getUserDTO();
  }

  @Override
  public boolean checkRoleOfUser(String roleCode, Long userId) {
    return userRepository.checkRoleOfUser(roleCode, userId);
  }

  @Override
  public List<UsersInsideDto> getListUserByUnitId(Long unitId) {
    return userRepository.getListUserByUnitId(unitId);
  }

  @Override
  public UserTokenGNOC getUserInfor(String userName) {
    return userRepository.getUserInfor(userName);
  }

  @Override
  public List<UsersDTO> search(UsersDTO usersDTO, int rowStart, int maxRow, String sortType,
      String sortFieldList) {
    List<UsersDTO> lstResult = new ArrayList<>();
    List<UsersInsideDto> lst = userRepository
        .search(usersDTO.toInSideDto(), rowStart, maxRow, sortType, sortFieldList);
    if (lst != null && !lst.isEmpty()) {
      for (UsersInsideDto usersInsideDto : lst) {
        lstResult.add(usersInsideDto.toOutSideDto());
      }
    }
    return lstResult;
  }
}
