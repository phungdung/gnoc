package com.viettel.gnoc.cr.business;


import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.LogChangeConfigDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.proxy.CommonStreamServiceProxy;
import com.viettel.gnoc.commons.repository.LanguageExchangeRepository;
import com.viettel.gnoc.commons.utils.CellConfigExport;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.ConfigFileExport;
import com.viettel.gnoc.commons.utils.ConfigHeaderExport;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.cr.dto.GroupUnitDetailDTO;
import com.viettel.gnoc.cr.dto.GroupUnitDetailNameDTO;
import com.viettel.gnoc.cr.repository.GroupUnitDetailRepository;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import viettel.passport.client.UserToken;

@Slf4j
@Transactional
@Service
public class GroupUnitDetailBusinessImpl implements GroupUnitDetailBusiness {

  @Autowired
  GroupUnitDetailRepository groupUnitDetailRepository;

  @Autowired
  protected LanguageExchangeRepository languageExchangeRepository;

  @Autowired
  protected CommonStreamServiceProxy commonStreamServiceProxy;

  @Autowired
  TicketProvider ticketProvider;

  @Value("${application.temp.folder}")
  private String tempFolder;

  @Override
  public Datatable getListGroupUnitDetailDTO(GroupUnitDetailDTO dto) {
    return groupUnitDetailRepository.getListGroupUnitDetailDTO(dto);
  }

  @Override
  public ResultInSideDto insertGroupUnitDetail(GroupUnitDetailDTO groupUnitDetailDTO) {
    ResultInSideDto resultInSideDto = groupUnitDetailRepository
        .insertGroupUnitDetail(groupUnitDetailDTO);
    if (resultInSideDto.getKey().equals(Constants.RESULT.SUCCESS)) {
      UserToken userToken = ticketProvider.getUserToken();
      commonStreamServiceProxy.insertLog(new LogChangeConfigDTO(userToken.getUserName(),
          "Add groupUnitDetail", "Add groupUnitDetail ID: " + resultInSideDto.getId(),
          groupUnitDetailDTO, null));
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto updateGroupUnitDetail(GroupUnitDetailDTO groupUnitDetailDTO) {
    ResultInSideDto resultInSideDto = groupUnitDetailRepository
        .updateGroupUnitDetail(groupUnitDetailDTO);
    if (resultInSideDto.getKey().equals(Constants.RESULT.SUCCESS)) {
      UserToken userToken = ticketProvider.getUserToken();
      commonStreamServiceProxy.insertLog(new LogChangeConfigDTO(userToken.getUserName(),
          "Update groupUnitDetail", "Update groupUnitDetail ID: " + resultInSideDto.getId(),
          groupUnitDetailDTO, null));
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto deleteGroupUnitDetail(Long id) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    if (id != null) {
      resultInSideDto = groupUnitDetailRepository.deleteGroupUnitDetail(id);
      if (resultInSideDto.getKey().equals(Constants.RESULT.SUCCESS)) {
        UserToken userToken = ticketProvider.getUserToken();
        resultInSideDto = commonStreamServiceProxy
            .insertLog(new LogChangeConfigDTO(userToken.getUserName(),
                "Delete groupUnitDetail", "Delete groupUnitDetail ID: " + id,
                null, null));
      }
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto deleteListGroupUnitDetail(
      List<GroupUnitDetailDTO> groupUnitDetailListDTO) {
    return groupUnitDetailRepository.deleteListGroupUnitDetail(groupUnitDetailListDTO);
  }

  @Override
  public GroupUnitDetailDTO findById(Long id) {
    return groupUnitDetailRepository.findById(id);
  }

  @Override
  public Datatable getListUnitOfGroup(GroupUnitDetailNameDTO groupUnitDetailNameDTO) {
    return groupUnitDetailRepository.getListUnitOfGroup(groupUnitDetailNameDTO);
  }

  @Override
  public Datatable getIDGroup(GroupUnitDetailDTO groupUnitDetailDTO) {
    return groupUnitDetailRepository.getIDGroup(groupUnitDetailDTO);
  }

  @Override
  public File exportData(GroupUnitDetailNameDTO groupUnitDetailNameDTO) throws Exception {
    groupUnitDetailNameDTO.setPage(1);
    groupUnitDetailNameDTO.setPageSize(Integer.MAX_VALUE);
    Datatable datatable = getListUnitOfGroup(groupUnitDetailNameDTO);
    List<GroupUnitDetailNameDTO> lstReturn = (List<GroupUnitDetailNameDTO>) datatable.getData();
    UserToken userToken = ticketProvider.getUserToken();
    commonStreamServiceProxy.insertLog(new LogChangeConfigDTO(userToken.getUserName(),
        "ExportData groupUnitDetail",
        "ExportData getGroupUnitDetailId ID: " + groupUnitDetailNameDTO.getGroupUnitDetailId(),
        null, null));
    return exportFileEx(lstReturn);
  }

  private File exportFileEx(List<GroupUnitDetailNameDTO> lstData) throws Exception {
    String title = I18n.getLanguage("groupUnitDetail.title_export");
    String fileNameOut = "GroupUnitDetail";
    String subTitle = I18n
        .getLanguage("TempImport.export.eportDate", DateTimeUtils.convertDateOffset());
    List<ConfigFileExport> fileExports = new ArrayList<>();
    List<ConfigHeaderExport> lstHeaderSheet1 = renderHeaderSheet("groupUnitCode", "groupUnitName",
        "unitCode", "unitName");
    Map<String, String> fieldSplit = new HashMap<>();
    ConfigFileExport configfileExport = new ConfigFileExport(
        lstData
        , I18n.getLanguage("groupUnitDetail.title_export")
        , title
        , subTitle
        , 7
        , 3
        , 6
        , true
        , "language.groupUnitDetail"
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
    cellSheet = new CellConfigExport(7, 0, 0, 0, I18n.getLanguage("groupUnitDetail.stt"),
        "HEAD", "STRING");
    lstCellSheet.add(cellSheet);
    configfileExport.setLstCreatCell(lstCellSheet);
    fileExports.add(configfileExport);
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

  private List<ConfigHeaderExport> renderHeaderSheet(String... col) {
    List<ConfigHeaderExport> lstHeaderSheet = new ArrayList<>();
    for (int i = 0; i < col.length; i++) {
      lstHeaderSheet.add(new ConfigHeaderExport(col[i], "LEFT", false, 0, 0, new String[]{}, null,
          "STRING"));
    }
    return lstHeaderSheet;
  }

}
