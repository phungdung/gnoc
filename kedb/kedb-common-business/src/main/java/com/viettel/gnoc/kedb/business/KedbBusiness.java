package com.viettel.gnoc.kedb.business;

import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.dto.UserTokenGNOCSimple;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.kedb.dto.KedbDTO;
import java.io.File;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface KedbBusiness {

  Datatable getListKedbDTO(KedbDTO kedbDTO);

  KedbDTO findKedbById(Long kedbId);

  ResultInSideDto insertKedb(List<MultipartFile> files, KedbDTO kedbDTO) throws Exception;

  ResultInSideDto updateKedb(List<MultipartFile> files, KedbDTO kedbDTO) throws Exception;

  ResultInSideDto deleteKedb(Long kedbId);

  ResultInSideDto deleteListKedb(List<Long> listKedbId);

  List<String> getSequenseKedb();

  ResultInSideDto insertOrUpdateListKedb(List<KedbDTO> listKedbDTO);

  String getOffset(UserTokenGNOCSimple userTokenGNOC);

  List<KedbDTO> synchKedbByCreateTime(String fromDate, String toDate);

  List<KedbDTO> getListKedbByCondition(List<ConditionBean> lstCondition,
      int rowStart, int maxRow, String sortType, String sortFieldList);

  File exportData(KedbDTO kedbDTO) throws Exception;

  File getTemplate() throws Exception;

  List<CatItemDTO> getListSubCategory(Long typeId);

  ResultInSideDto importData(MultipartFile fileImport, List<MultipartFile> files);

  List<UnitDTO> getListUnitCheckKedb(KedbDTO kedbDTO);

  List<com.viettel.gnoc.ws.dto.KedbDTO> synchKedbByCreateTime2(String fromDate, String toDate);
}
