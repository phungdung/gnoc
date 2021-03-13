package com.viettel.gnoc.mr.business;


import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.utils.CellConfigExport;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.ConfigFileExport;
import com.viettel.gnoc.commons.utils.ConfigHeaderExport;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.maintenance.dto.SearchDeviceNIMSDTO;
import com.viettel.gnoc.mr.repository.SearchDeviceNIMSRepository;
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

@Service
@Transactional
@Slf4j
public class SearchDeviceNIMSBusinessImpl implements SearchDeviceNIMSBusiness {

  @Autowired
  SearchDeviceNIMSRepository searchDeviceNIMSRepository;

  @Value("${application.temp.folder}")
  private String tempFolder;

  @Override
  public Datatable getListSearchDeviceNIMS(SearchDeviceNIMSDTO searchDeviceNIMSDTO) {
    Datatable datatable = searchDeviceNIMSRepository.getListSearchDeviceNIMS(searchDeviceNIMSDTO);
    return datatable;
  }

  @Override
  public List<SearchDeviceNIMSDTO> getComboboxNetworkClass() {
    return searchDeviceNIMSRepository.getComboboxNetworkClass();
  }

  @Override
  public List<SearchDeviceNIMSDTO> getComboboxNetworkType() {
    return searchDeviceNIMSRepository.getComboboxNetworkType();
  }

  @Override
  public File exportSearchData(SearchDeviceNIMSDTO searchDeviceNIMSDTO) throws Exception {
    List<SearchDeviceNIMSDTO> lstData = searchDeviceNIMSRepository
        .onSearchExport(searchDeviceNIMSDTO);
    List<ConfigHeaderExport> lstHeaderSheet = renderHeaderSheet("networkType", "networkClass",
        "ipNode", "deviceName", "deviceCode",
        "stationName", "creatDate");
    return exportFileEx(lstData, lstHeaderSheet, "");
  }

  private File exportFileEx(List<SearchDeviceNIMSDTO> lstData,
      List<ConfigHeaderExport> lstHeaderSheet, String code) throws Exception {
    String title = I18n.getLanguage("searchDeviceNIMS.report");
    String sheetName = title;
    String fileNameOut = "SearchDeviceNIMSReport";
    ConfigFileExport configfileExport = null;
    List<ConfigFileExport> fileExports = new ArrayList<>();
    Map<String, String> fieldSplit = new HashMap<>();
    CellConfigExport cellSheet = null;
    if (Constants.RESULT_IMPORT.equals(code)) {
    } else {
      configfileExport = new ConfigFileExport(
          lstData
          , sheetName
          , title
          , null
          , 7
          , 3
          , lstHeaderSheet.size()
          , true
          , "language.searchDeviceNIMS.list"
          , lstHeaderSheet
          , fieldSplit
          , ""
          , I18n.getLanguage("cfgProcedureView.export.firstLeftHeader")
          , I18n.getLanguage("cfgProcedureView.export.secondLeftHeader")
          , I18n.getLanguage("cfgProcedureView.export.firstRightHeader")
          , I18n.getLanguage("cfgProcedureView.export.secondRightHeader")
      );
      cellSheet = new CellConfigExport(7, 0, 0, 0, I18n.getLanguage("searchDeviceNIMS.list.stt"),
          "HEAD", "STRING");
      List<CellConfigExport> lstCellSheet = new ArrayList<>();
      lstCellSheet.add(cellSheet);
      configfileExport.setLstCreatCell(lstCellSheet);
      configfileExport.setLangKey(I18n.getLocale());
      fileExports.add(configfileExport);
    }
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
