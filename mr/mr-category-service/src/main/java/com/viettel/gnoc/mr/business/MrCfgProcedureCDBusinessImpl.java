package com.viettel.gnoc.mr.business;

import com.viettel.gnoc.commons.business.CatItemBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.proxy.CommonStreamServiceProxy;
import com.viettel.gnoc.commons.utils.CellConfigExport;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.ConfigFileExport;
import com.viettel.gnoc.commons.utils.ConfigHeaderExport;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.MR_ITEM_NAME;
import com.viettel.gnoc.maintenance.dto.MrCfgProcedureCDDTO;
import com.viettel.gnoc.mr.repository.MrCfgProcedureCDRepository;
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
public class MrCfgProcedureCDBusinessImpl implements MrCfgProcedureCDBusiness {

  @Autowired
  MrCfgProcedureCDRepository mrCfgProcedureCDRepository;

  @Autowired
  CommonStreamServiceProxy commonStreamServiceProxy;

  @Autowired
  CatItemBusiness catItemBusiness;

  @Value("${application.temp.folder}")
  private String tempFolder;

  @Override
  public Datatable onSearch(MrCfgProcedureCDDTO mrCfgProcedureCDDTO) {
    Datatable datatable = mrCfgProcedureCDRepository.onSearch(mrCfgProcedureCDDTO);
    return datatable;
  }

  @Override
  public File exportSearchData(MrCfgProcedureCDDTO mrCfgProcedureCDDTO) throws Exception {
    List<MrCfgProcedureCDDTO> lstData = mrCfgProcedureCDRepository
        .onSearchExport(mrCfgProcedureCDDTO);
    List<ConfigHeaderExport> lstHeaderSheet = renderHeaderSheet("marketName", "deviceType", "cycle",
        "mrContentId", "procedureName");
    return exportFileEx(lstData, lstHeaderSheet, "");
  }

  private File exportFileEx(List<MrCfgProcedureCDDTO> lstData,
      List<ConfigHeaderExport> lstHeaderSheet, String code) throws Exception {
    String title = I18n.getLanguage("cfgProcedureView.list.grid");
    String sheetName = title;
    String fileNameOut = "Procedure";
    List<ConfigFileExport> fileExports = new ArrayList<>();
    Map<String, String> fieldSplit = new HashMap<>();
    CellConfigExport cellSheet = null;
    if (Constants.RESULT_IMPORT.equals(code)) {
    } else {
      ConfigFileExport configfileExport = new ConfigFileExport(
          lstData
          , sheetName
          , title
          , null
          , 7
          , 3
          , lstHeaderSheet.size()
          , true
          , "language.cfgProcedureView.list.grid"
          , lstHeaderSheet
          , fieldSplit
          , ""
          , I18n.getLanguage("cfgProcedureView.export.firstLeftHeader")
          , I18n.getLanguage("cfgProcedureView.export.secondLeftHeader")
          , I18n.getLanguage("cfgProcedureView.export.firstRightHeader")
          , I18n.getLanguage("cfgProcedureView.export.secondRightHeader")
      );
      cellSheet = new CellConfigExport(7, 0, 0, 0,
          I18n.getLanguage("cfgProcedureView.list.grid.stt"),
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

  @Override
  public List<CatItemDTO> getMrSubCategory() {
    return catItemBusiness
        .getListItemByCategoryAndParent(Constants.MR_ITEM_NAME.MR_SUBCATEGORY, null);
  }

  @Override
  public List<CatItemDTO> getMrPriority() {
    return catItemBusiness.getListItemByCategoryAndParent(MR_ITEM_NAME.MR_PRIORITY, null);
  }

  @Override
  public List<CatItemDTO> getMrImpact() {
    return catItemBusiness.getListItemByCategoryAndParent(MR_ITEM_NAME.MR_IMPACT, null);
  }

  @Override
  public List<CatItemDTO> getOgListWorks(Long parentItemId) {
    if (parentItemId == null) {
      return catItemBusiness.getListItemByCategoryAndParent(MR_ITEM_NAME.MR_WORKS, null);
    } else {
      CatItemDTO catItemDTO = new CatItemDTO();
      catItemDTO.setParentItemId(parentItemId);
      catItemDTO.setSortName("");
      catItemDTO.setSortType("");
      catItemDTO.setProxyLocale(I18n.getLocale());
      return commonStreamServiceProxy.getListCatItemTransDTO(catItemDTO);
    }
  }

  @Override
  public ResultInSideDto onInsertOrUpdate(MrCfgProcedureCDDTO mrCfgProcedureCDDTO) {
    return mrCfgProcedureCDRepository.onInsertOrUpdate(mrCfgProcedureCDDTO);
  }

  @Override
  public MrCfgProcedureCDDTO findById(Long id) {
    return mrCfgProcedureCDRepository.findById(id);
  }

  @Override
  public ResultInSideDto deleteById(Long id) {
    return mrCfgProcedureCDRepository.deleteById(id);
  }
}
