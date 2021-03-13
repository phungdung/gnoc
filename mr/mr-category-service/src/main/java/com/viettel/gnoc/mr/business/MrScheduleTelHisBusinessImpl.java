package com.viettel.gnoc.mr.business;

import com.viettel.gnoc.commons.business.CatLocationBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.utils.CellConfigExport;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.ConfigFileExport;
import com.viettel.gnoc.commons.utils.ConfigHeaderExport;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.maintenance.dto.MrScheduleTelHisDTO;
import com.viettel.gnoc.mr.repository.MrScheduleTelHisRepository;
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
public class MrScheduleTelHisBusinessImpl implements MrScheduleTelHisBusiness {

  @Autowired
  MrScheduleTelHisRepository mrScheduleTelHisRepository;

  @Autowired
  protected CatLocationBusiness catLocationBusiness;

  @Value("${application.temp.folder}")
  private String tempFolder;

  @Override
  public Datatable getListMrScheduleTelHisPage(MrScheduleTelHisDTO mrScheduleTelHisDTO) {
    log.debug("Request to getListMrScheduleTelHisPage : {}", mrScheduleTelHisDTO);
    setMapCountryName();
    Datatable datatable = mrScheduleTelHisRepository
        .getListMrScheduleHardHisPage(mrScheduleTelHisDTO);
    List<MrScheduleTelHisDTO> listDTO = (List<MrScheduleTelHisDTO>) datatable.getData();
    if (listDTO != null && listDTO.size() > 0) {
      for (MrScheduleTelHisDTO scheduleTelHisDTO : listDTO) {
        if (scheduleTelHisDTO.getMarketCode() != null) {
          scheduleTelHisDTO.setMarketName(mapCountryName.get(scheduleTelHisDTO.getMarketCode()));
        }
      }
    }
    datatable.setData(listDTO);
    return datatable;
  }

  @Override
  public File exportSearchData(MrScheduleTelHisDTO mrScheduleTelHisDTO) throws Exception {
    List<MrScheduleTelHisDTO> mrScheduleTelHisDTOList = mrScheduleTelHisRepository
        .onSearchExport(mrScheduleTelHisDTO);
    setMapCountryName();
    if (mrScheduleTelHisDTOList != null && !mrScheduleTelHisDTOList.isEmpty()) {
      for (int i = mrScheduleTelHisDTOList.size() - 1; i > -1; i--) {
        MrScheduleTelHisDTO mrScheduleTelHisExport = mrScheduleTelHisDTOList.get(i);
        mrScheduleTelHisExport
            .setMarketName(mapCountryName.get(mrScheduleTelHisExport.getMarketCode()));
        //Mr_MODE_NAME
        if ("H".equals(mrScheduleTelHisExport.getMrMode())) {
          mrScheduleTelHisExport
              .setMrModeName(I18n.getLanguage("mrScheduleTelHis.list.map.mrModeName"));
        }

        //Node_Status_Str
        if ("0".equals(mrScheduleTelHisExport.getNodeStatus())) {
          mrScheduleTelHisExport
              .setNodeStatusStr(I18n.getLanguage("mrScheduleTelHis.list.map.nodeStatusStr.0"));
        } else if ("1".equals(mrScheduleTelHisExport.getNodeStatus())) {
          mrScheduleTelHisExport
              .setNodeStatusStr(I18n.getLanguage("mrScheduleTelHis.list.map.nodeStatusStr.1"));
        }
      }
    }

    String[] header = new String[]{
        "marketName",
        "arrayCode",
        "deviceType",
        "deviceCode",
        "deviceName",
        "mrDate",
        "mrCode",
        "woId",
        "nodeStatusStr",
        "procedureName",
        "mrModeName",
        "mrArrayType"};
    String[] align = new String[]{
        "LEFT",
        "LEFT",
        "LEFT",
        "LEFT",
        "LEFT",
        "LEFT",
        "LEFT",
        "LEFT",
        "LEFT",
        "LEFT",
        "LEFT",
        "LEFT",
    };

    List<ConfigHeaderExport> lstHeaderSheet = renderHeaderSheet(header, align);
    return exportFileEx(mrScheduleTelHisDTOList, lstHeaderSheet);
  }

  private File exportFileEx(List<MrScheduleTelHisDTO> mrScheduleTelHisDTOList,
      List<ConfigHeaderExport> lstHeaderSheet) throws Exception {
    String fileNameOut = "MR_SCHEDULE_TEL_HIS_HARD_EXPORT";
    ConfigFileExport configfileExport;
    Map<String, String> fieldSplit = new HashMap<>();
    CellConfigExport cellSheet;
    String rootPath = tempFolder + File.separator;
    String language = I18n.getLocale();
    String fileTemplate = "TEMPLATE_EXPORT_EN.xlsx";
    if (language != null && (language.toLowerCase().contains("vi") || language.toLowerCase()
        .contains("vi_VN"))) {
      fileTemplate = "TEMPLATE_EXPORT.xlsx";
    }

    configfileExport = new ConfigFileExport(
        mrScheduleTelHisDTOList
        , "CfgTime"
        , I18n.getLanguage("mrScheduleTelHis.export.title")
        , ""
        , 7
        , 4
        , lstHeaderSheet.size()
        , false
        , "language.mrScheduleTelHis.list"
        , lstHeaderSheet
        , fieldSplit
        , ""
        , I18n.getLanguage("mrScheduleTelHis.export.firstLeftHeader")
        , I18n.getLanguage("mrScheduleTelHis.export.secondLeftHeader")
        , I18n.getLanguage("mrScheduleTelHis.export.firstRightHeader")
        , I18n.getLanguage("mrScheduleTelHis.export.secondRightHeader")
    );
    cellSheet = new CellConfigExport(7, 0, 0, 0, I18n.getLanguage("mrScheduleTelHis.list.stt"),
        "HEAD", "STRING");

    configfileExport.setLangKey(I18n.getLocale());
    configfileExport.setIsAutoSize(true);
    List<CellConfigExport> lstCellSheet = new ArrayList<>();
    lstCellSheet.add(cellSheet);
    configfileExport.setLstCreatCell(lstCellSheet);
    List<Integer> lsColumnHidden = new ArrayList<>();
    lsColumnHidden.add(1);

    File fileExport = CommonExport.exportFileWithTemplateXLSX(
        "templates" + File.separator + fileTemplate
        , fileNameOut
        , configfileExport
        , rootPath,
        null,
        null,
        I18n.getLanguage("mrScheduleTelHis.export.title")
    );
    return fileExport;
  }

  private List<ConfigHeaderExport> renderHeaderSheet(String[] col, String[] align) {
    List<ConfigHeaderExport> lstHeaderSheet = new ArrayList<>();
    for (int i = 0; i < col.length; i++) {
      lstHeaderSheet.add(new ConfigHeaderExport(col[i], align[i], false, 0, 0, new String[]{}, null,
          "STRING"));
    }
    return lstHeaderSheet;
  }

  Map<String, String> mapCountryName = new HashMap<>();

  public void setMapCountryName() {
    List<ItemDataCRInside> lstCountryName = catLocationBusiness
        .getListLocationByLevelCBB(null, 1L, null);
    if (lstCountryName != null && !lstCountryName.isEmpty()) {
      for (ItemDataCRInside itemDataCR : lstCountryName) {
        mapCountryName.put(String.valueOf(itemDataCR.getValueStr()), itemDataCR.getDisplayStr());
      }
    }
  }

}
