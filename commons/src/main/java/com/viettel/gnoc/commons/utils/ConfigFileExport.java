package com.viettel.gnoc.commons.utils;

import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by VTN-PTPM-NV04 on 2/24/2018.
 */
@Getter
@Setter
public class ConfigFileExport {

  private List lstData;
  private String sheetName;
  private String title;
  private String subTitle;
  private int startRow;
  private int cellTitleIndex;
  private int mergeTitleEndIndex;
  private boolean creatHeader;
  private String headerPrefix;
  private List<ConfigHeaderExport> header;
  private Map<String, String> fieldSplit;
  private String splitChar;
  private List<CellConfigExport> lstCreatCell;
  private String langKey;
  private String firstLeftHeaderTitle;
  private String secondLeftHeaderTitle;
  private String firstRightHeaderTitle;
  private String secondRightHeaderTitle;
  private List<CellConfigExport> lstCellMerge;
  private String[] customTitle;  //[1:0] co aligncenter ko, String alignLeft, String background, String numberRowMerge
  private String[] customColumnWidth;
  private String[] customColumnWidthNoMerge;
  private List<Integer> lsColumnHidden;
  private Boolean isAutoSize;
  private Map<Integer, Integer> mapCustomColumnWidth;

  public ConfigFileExport(
      List lstData
      , String sheetName
      , String title
      , String subTitle
      , int startRow
      , int cellTitleIndex
      , int mergeTitleEndIndex
      , boolean creatHeader
      , String headerPrefix
      , List<ConfigHeaderExport> header
      , Map<String, String> fieldSplit
      , String splitChar
  ) {
    this.lstData = lstData;
    this.sheetName = sheetName;
    this.title = title;
    this.subTitle = subTitle;
    this.startRow = startRow;
    this.cellTitleIndex = cellTitleIndex;
    this.mergeTitleEndIndex = mergeTitleEndIndex;
    this.creatHeader = creatHeader;
    this.headerPrefix = headerPrefix;
    this.header = header;
    this.fieldSplit = fieldSplit;
    this.splitChar = splitChar;
    this.lstCreatCell = null;
  }

  public ConfigFileExport(
      List lstData
      , String sheetName
      , String title
      , String subTitle
      , int startRow
      , int cellTitleIndex
      , int mergeTitleEndIndex
      , boolean creatHeader
      , String headerPrefix
      , List<ConfigHeaderExport> header
      , Map<String, String> fieldSplit
      , String splitChar
      , String firstLeftHeaderTitle
      , String secondLeftHeaderTitle
      , String firstRightHeaderTitle
      , String secondRightHeaderTitle
  ) {
    this.lstData = lstData;
    this.sheetName = sheetName;
    this.title = title;
    this.subTitle = subTitle;
    this.startRow = startRow;
    this.cellTitleIndex = cellTitleIndex;
    this.mergeTitleEndIndex = mergeTitleEndIndex;
    this.creatHeader = creatHeader;
    this.headerPrefix = headerPrefix;
    this.header = header;
    this.fieldSplit = fieldSplit;
    this.splitChar = splitChar;
    this.lstCreatCell = null;
    this.firstLeftHeaderTitle = firstLeftHeaderTitle;
    this.secondLeftHeaderTitle = secondLeftHeaderTitle;
    this.firstRightHeaderTitle = firstRightHeaderTitle;
    this.secondRightHeaderTitle = secondRightHeaderTitle;
  }
}
