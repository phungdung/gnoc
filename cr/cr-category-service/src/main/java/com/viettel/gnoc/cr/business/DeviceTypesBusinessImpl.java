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
import com.viettel.gnoc.cr.dto.DeviceTypesDTO;
import com.viettel.gnoc.cr.repository.DeviceTypesRepository;
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
public class DeviceTypesBusinessImpl implements DeviceTypesBusiness {

  @Autowired
  private DeviceTypesRepository deviceTypesRepository;

  @Autowired
  protected LanguageExchangeRepository languageExchangeRepository;

  @Autowired
  protected CommonStreamServiceProxy commonStreamServiceProxy;

  @Autowired
  TicketProvider ticketProvider;

  @Value("${application.temp.folder}")
  private String tempFolder;

  @Override
  public ResultInSideDto insertDeviceTypes(DeviceTypesDTO deviceTypesDTO) {
    ResultInSideDto resultInSideDto = deviceTypesRepository.insertDeviceTypes(deviceTypesDTO);
    if (deviceTypesDTO.getListDeviceTypeName() != null) {
      languageExchangeRepository
          .saveListLanguageExchange("OPEN_PM", "OPEN_PM.DEVICE_TYPES", resultInSideDto.getId(),
              deviceTypesDTO.getListDeviceTypeName());
    }
    if (resultInSideDto.getKey().equals(Constants.RESULT.SUCCESS)) {
      UserToken userToken = ticketProvider.getUserToken();
      commonStreamServiceProxy.insertLog(new LogChangeConfigDTO(userToken.getUserName(),
          "Add deviceTypes", "Add deviceTypes ID: " + resultInSideDto.getId(),
          deviceTypesDTO, null));
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto updateDeviceTypes(DeviceTypesDTO deviceTypesDTO) {
    ResultInSideDto resultInSideDto = deviceTypesRepository.updateDeviceTypes(deviceTypesDTO);
    if (deviceTypesDTO.getListDeviceTypeName() != null) {
      languageExchangeRepository
          .saveListLanguageExchange("OPEN_PM", "OPEN_PM.DEVICE_TYPES", resultInSideDto.getId(),
              deviceTypesDTO.getListDeviceTypeName());
    }
    if (resultInSideDto.getKey().equals(Constants.RESULT.SUCCESS)) {
      UserToken userToken = ticketProvider.getUserToken();
      commonStreamServiceProxy.insertLog(new LogChangeConfigDTO(userToken.getUserName(),
          "Update deviceTypes", "Update deviceTypes ID: " + resultInSideDto.getId(),
          deviceTypesDTO, null));
    }
    return resultInSideDto;
  }

  @Override
  public DeviceTypesDTO findDeviceTypesById(Long id) {
    DeviceTypesDTO deviceTypesDTO = deviceTypesRepository.findDeviceTypesById(id);
    deviceTypesDTO.setListDeviceTypeName(languageExchangeRepository
        .getListLanguageExchangeById("OPEN_PM", "OPEN_PM.DEVICE_TYPES", id, null));
    return deviceTypesDTO;
  }

  @Override
  public ResultInSideDto deleteDeviceTypes(Long id) {
    ResultInSideDto resultInSideDto = deviceTypesRepository.deleteDeviceTypes(id);
    UserToken userToken = ticketProvider.getUserToken();
    commonStreamServiceProxy.insertLog(new LogChangeConfigDTO(userToken.getUserName(),
        "Delete deviceTypes", "Delete deviceTypes ID: " + id,
        null, null));
    languageExchangeRepository.deleteListLanguageExchange("OPEN_PM", "OPEN_PM.DEVICE_TYPES", id);
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto deleteListDeviceTypes(List<DeviceTypesDTO> deviceTypesListDTO) {
    return deviceTypesRepository.deleteListDeviceTypes(deviceTypesListDTO);
  }

  @Override
  public Datatable getLisDeviceTypesSearch(DeviceTypesDTO deviceTypesDTO) {
    return deviceTypesRepository.getLisDeviceTypesSearch(deviceTypesDTO);
  }

  @Override
  public File exportData(DeviceTypesDTO deviceTypesDTO) throws Exception {
    deviceTypesDTO.setPage(1);
    deviceTypesDTO.setPageSize(Integer.MAX_VALUE);
    Datatable datatable = getLisDeviceTypesSearch(deviceTypesDTO);
    List<DeviceTypesDTO> lstReturn = (List<DeviceTypesDTO>) datatable.getData();
    UserToken userToken = ticketProvider.getUserToken();
    commonStreamServiceProxy.insertLog(new LogChangeConfigDTO(userToken.getUserName(),
        "Export Data DeviceTypes",
        "Export Data DeviceTypes", deviceTypesDTO, null));
    return exportFileEx(lstReturn);
  }

  private File exportFileEx(List<DeviceTypesDTO> lstData) throws Exception {
    String title = I18n.getLanguage("deviceTypes.title_export");
    String fileNameOut = "DeviceType";
    String subTitle = I18n
        .getLanguage("deviceTypes.export.exportDate", DateTimeUtils.convertDateOffset());
    List<ConfigFileExport> fileExports = new ArrayList<>();
    List<ConfigHeaderExport> lstHeaderSheet1 = renderHeaderSheet("deviceTypeCode",
        "deviceTypeName");
    Map<String, String> fieldSplit = new HashMap<>();
    ConfigFileExport configfileExport = new ConfigFileExport(
        lstData
        , I18n.getLanguage("deviceTypes.title_export")
        , title
        , subTitle
        , 7
        , 3
        , 7
        , true
        , "language.deviceTypes"
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
    cellSheet = new CellConfigExport(7, 0, 0, 0, I18n.getLanguage("deviceTypes.stt"),
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
