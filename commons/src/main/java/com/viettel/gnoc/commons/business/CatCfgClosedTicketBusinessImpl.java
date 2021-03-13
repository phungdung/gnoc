package com.viettel.gnoc.commons.business;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.dto.CatCfgClosedTicketDTO;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.CatCfgClosedTicketRepository;
import com.viettel.gnoc.commons.utils.CellConfigExport;
import com.viettel.gnoc.commons.utils.CommonExport;
import com.viettel.gnoc.commons.utils.ConfigFileExport;
import com.viettel.gnoc.commons.utils.ConfigHeaderExport;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.utils.DateUtil;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author TienNV
 */
@Service
@Transactional
@Slf4j
public class CatCfgClosedTicketBusinessImpl implements CatCfgClosedTicketBusiness {

  @Autowired
  protected CatCfgClosedTicketRepository catCfgClosedTicketRepository;

  @Value("${application.temp.folder}")
  private String tempFolder;

  private final static String CFG_CLOSED_TICKET_RESULT_IMPORT = "CFG_CLOSED_TICKET_RESULT_IMPORT";

  private final static String EXPORT_CAT_CFG_CLOSED_TICKET = "EXPORT_CAT_CFG_CLOSED_TICKET";

  @Override
  public List<CatCfgClosedTicketDTO> getListCatCfgClosedTicketDTO(
      CatCfgClosedTicketDTO catCfgClosedTicketDTO, int start, int maxResult, String sortType,
      String sortField) {
    return catCfgClosedTicketRepository
        .getListCatCfgClosedTicketDTO(catCfgClosedTicketDTO, start, maxResult, sortType, sortField);
  }

  @Override
  public Datatable getListCatCfgClosedTicket(CatCfgClosedTicketDTO dto) {
    log.debug("Request to getListCatCfgClosedTicket : {}", dto);
    return catCfgClosedTicketRepository.getListCatCfgClosedTicket(dto);
  }

  @Override
  public List<CatItemDTO> getListSubCategory(Long typeId) {
    log.debug("Request to getListSubCategory: {}", typeId);
    return catCfgClosedTicketRepository.getListSubCategory(typeId);
  }

  @Override
  public CatCfgClosedTicketDTO getDetail(Long id) {
    log.debug("Request to getDetail : {}", id);
    return catCfgClosedTicketRepository.getDetail(id);
  }

  @Override
  public ResultInSideDto insert(CatCfgClosedTicketDTO catCfgClosedTicketDTO) {
    log.debug("Request to insert : {}", catCfgClosedTicketDTO);
    ResultInSideDto result = new ResultInSideDto();
    if (catCfgClosedTicketRepository.checkExist(catCfgClosedTicketDTO) != null) {
      result.setKey(RESULT.DUPLICATE);
      return result;
    } else {
      catCfgClosedTicketDTO.setLastUpdateTime(DateUtil
          .dateToStringWithPattern(new Date(System.currentTimeMillis()), "dd/MM/yyyy HH:mm:ss"));
      return catCfgClosedTicketRepository.insert(catCfgClosedTicketDTO);
    }

  }

  @Override
  public ResultInSideDto update(CatCfgClosedTicketDTO catCfgClosedTicketDTO) {
    log.debug("Request to update : {}", catCfgClosedTicketDTO);
    ResultInSideDto result = new ResultInSideDto();
    if (catCfgClosedTicketRepository.checkExist(catCfgClosedTicketDTO) != null) {
      result.setKey(RESULT.DUPLICATE);
      return result;
    } else {
      catCfgClosedTicketDTO.setLastUpdateTime(DateUtil
          .dateToStringWithPattern(new Date(System.currentTimeMillis()), "dd/MM/yyyy HH:mm:ss"));
      return catCfgClosedTicketRepository.edit(catCfgClosedTicketDTO);
    }
  }


  @Override
  public ResultInSideDto delete(Long id) {
    log.debug("Request to delete : {}", id);
    return catCfgClosedTicketRepository.delete(id);
  }

  @Override
  public File exportCatCfgClosedTicket(CatCfgClosedTicketDTO catCfgClosedTicketDTO)
      throws Exception {
    List<CatCfgClosedTicketDTO> catCfgClosedTicketList = (List<CatCfgClosedTicketDTO>) catCfgClosedTicketRepository
        .getListCatCfgClosedTicket(catCfgClosedTicketDTO).getData();

    return exportFileTemplate(catCfgClosedTicketList, "");
  }

  private File exportFileTemplate(List<CatCfgClosedTicketDTO> dtoList, String key)
      throws Exception {

    String fileNameOut;
    String subTitle;
    String sheetName = I18n.getLanguage("cfgClosedTicket.export.title");
    String title = I18n.getLanguage("cfgClosedTicket.export.title");
    List<ConfigFileExport> fileExportList = new ArrayList<>();
    ConfigHeaderExport columnSheet;
    List<ConfigHeaderExport> headerExportList = new ArrayList<>();
    columnSheet = new ConfigHeaderExport("woTypeName", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("typeName", "LEFT", false, 0, 0, new String[]{}, null,
        "STRING");
    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("alarmGroupName", "LEFT", false, 0, 0, new String[]{},
        null,
        "STRING");
    headerExportList.add(columnSheet);
    columnSheet = new ConfigHeaderExport("lastUpdateTime", "LEFT", false, 0, 0, new String[]{},
        null,
        "STRING");
    headerExportList.add(columnSheet);

    //kiểm tra đầu vào
    if (Constants.RESULT_IMPORT.equals(key)) {
      fileNameOut = CFG_CLOSED_TICKET_RESULT_IMPORT;
      subTitle = I18n
          .getLanguage("cfgClosedTicket.export.exportDate", DateTimeUtils.convertDateOffset());
      columnSheet = new ConfigHeaderExport("resultImport", "LEFT", false, 0, 0, new String[]{},
          null,
          "STRING");
      headerExportList.add(columnSheet);
    } else {
      fileNameOut = EXPORT_CAT_CFG_CLOSED_TICKET;
      subTitle = I18n
          .getLanguage("cfgClosedTicket.export.exportDate", DateTimeUtils.convertDateOffset());
    }
    Map<String, String> fieldSplit = new HashMap<>();
    ConfigFileExport configFileExport = new ConfigFileExport(
        dtoList
        , sheetName
        , title
        , subTitle
        , 7
        , 3
        , 9
        , true
        , "language.cfgClosedTicket"
        , headerExportList
        , fieldSplit
        , ""
        , I18n.getLanguage("common.export.firstLeftHeader")
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
    fileExportList.add(configFileExport);
    //Cấu hình đường dẫn
    String fileTemplate = "templates" + File.separator
        + "TEMPLATE_EXPORT.xlsx";
    String rootPath = tempFolder + File.separator;
    File fileExport = CommonExport.exportExcel(
        fileTemplate
        , fileNameOut
        , fileExportList
        , rootPath
        , new String[]{}
    );
    return fileExport;
  }


}
