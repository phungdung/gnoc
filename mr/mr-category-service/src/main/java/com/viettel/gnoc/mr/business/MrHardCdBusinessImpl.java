package com.viettel.gnoc.mr.business;

import com.viettel.gnoc.commons.business.CatLocationBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.CellConfigExport;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.ConfigFileExport;
import com.viettel.gnoc.commons.utils.ConfigHeaderExport;
import com.viettel.gnoc.maintenance.dto.MrHardCDDTO;
import com.viettel.gnoc.mr.repository.MrHardCdRepository;
import com.viettel.gnoc.wo.dto.WoCdGroupDTO;
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
public class MrHardCdBusinessImpl implements MrHardCdBusiness {

  @Autowired
  MrHardCdRepository mrHardCdRepository;

  @Autowired
  protected CatLocationBusiness catLocationBusiness;

  @Value("${application.temp.folder}")
  private String tempFolder;

  @Override
  public Datatable getListMrHardCDPage(MrHardCDDTO mrHardCDDTO) {
    log.debug("Request to getListMrHardCDPage : {}", mrHardCDDTO);
    return mrHardCdRepository.getListMrHardCDPage(mrHardCDDTO);
  }

  @Override
  public ResultInSideDto insert(MrHardCDDTO mrHardCDDTO) {
    log.debug("Request to insert : {}", mrHardCDDTO);
    return mrHardCdRepository.insertOrUpdate(mrHardCDDTO);
  }

  @Override
  public ResultInSideDto update(MrHardCDDTO mrHardCDDTO) {
    log.debug("Request to update : {}", mrHardCDDTO);
    return mrHardCdRepository.insertOrUpdate(mrHardCDDTO);
  }

  @Override
  public ResultInSideDto delete(Long hardCDId) {
    log.debug("Request to delete : {}", hardCDId);
    return mrHardCdRepository.delete(hardCDId);
  }

  @Override
  public MrHardCDDTO getDetail(Long hardCDId) {
    log.debug("Request to getDetail : {}", hardCDId);
    return mrHardCdRepository.getDetail(hardCDId);
  }

  @Override
  public List<WoCdGroupDTO> getWoCdGroupCBB() {
    log.debug("Request to getWoCdGroupCBB : {}");
    return mrHardCdRepository.getWoCdGroupCBB();
  }

  @Override
  public File exportSearchData(MrHardCDDTO mrHardCDDTO) throws Exception {
    List<MrHardCDDTO> mrHardCDDTOList = mrHardCdRepository
        .getListDataExport(mrHardCDDTO);
    String[] header = new String[]{
        "countryName",
        "regionName",
        "woGroupName",
        "createUser",
        "stationCode"
    };
    String[] align = new String[]{
        "LEFT",
        "LEFT",
        "LEFT",
        "LEFT",
        "LEFT"
    };
    List<ConfigHeaderExport> lstHeaderSheet = renderHeaderSheet(align, header);
    return exportFileEx(mrHardCDDTOList, lstHeaderSheet);
  }

  private File exportFileEx(List<MrHardCDDTO> mrHardCDDTOList,
      List<ConfigHeaderExport> lstHeaderSheet)
      throws Exception {
    String sheetName = I18n.getLanguage("mrHardCd.export.sheetName");
    String fileNameOut = "MR_HARD_CD_EXPORT";
    ConfigFileExport configfileExport;
    String title = I18n.getLanguage("mrHardCd.export.title");
    Map<String, String> fieldSplit = new HashMap<>();
    CellConfigExport cellSheet;
    String rootPath = tempFolder + File.separator;
    configfileExport = new ConfigFileExport(
        mrHardCDDTOList
        , sheetName
        , title
        , null
        , 7
        , 3
        , lstHeaderSheet.size()
        , false
        , "language.mrHardCd"
        , lstHeaderSheet
        , fieldSplit
        , ""
        , I18n.getLanguage("mrHardCd.export.firstLeftHeader")
        , I18n.getLanguage("mrHardCd.export.secondLeftHeader")
        , I18n.getLanguage("mrHardCd.export.firstRightHeader")
        , I18n.getLanguage("mrHardCd.export.secondRightHeader")
    );
    cellSheet = new CellConfigExport(7, 0, 0, 0, I18n.getLanguage("mrHardCd.stt"),
        "HEAD", "STRING");
    File fileExport = CommonExport.exportFileWithTemplateXLSX(
        getFilePathTemplate()
        , fileNameOut
        , configfileExport
        , rootPath
        , null,
        null
        , I18n.getLanguage("mrHardCd.export.title")
    );
    return fileExport;
  }

  private List<ConfigHeaderExport> renderHeaderSheet(String[] align, String... col) {
    List<ConfigHeaderExport> lstHeaderSheet = new ArrayList<>();
    for (int i = 0; i < col.length; i++) {
      lstHeaderSheet.add(new ConfigHeaderExport(col[i], align[i], false, 0, 0, new String[]{}, null,
          "STRING"));
    }
    return lstHeaderSheet;
  }

  private String getFilePathTemplate() {
    String language = I18n.getLocale();
    String fileTemplateName = "TEMPLATE_EXPORT.xlsx";
    if (language != null && (language.toLowerCase().contains("vi") || language.toLowerCase()
        .contains("vi_VN"))) {
      fileTemplateName = "TEMPLATE_EXPORT.xlsx";
    }
    return "templates" + File.separator + fileTemplateName;
  }
}
