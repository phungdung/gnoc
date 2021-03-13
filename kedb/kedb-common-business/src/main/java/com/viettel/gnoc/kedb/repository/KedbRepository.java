package com.viettel.gnoc.kedb.repository;

import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UserTokenGNOCSimple;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.kedb.dto.KedbDTO;
import com.viettel.gnoc.kedb.dto.KedbFilesDTO;
import com.viettel.gnoc.kedb.model.KedbFilesEntity;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface KedbRepository {

  Datatable getListKedbDTO(KedbDTO kedbDTO);

  List<KedbDTO> getListKedbExport(KedbDTO kedbDTO);

  KedbDTO findKedbById(Long kedbId, String userName);

  ResultInSideDto doInsertKedb(KedbDTO kedbDTO);

  ResultInSideDto doUpdateKedb(KedbDTO kedbDTO);

  String setStateName(KedbDTO kedbDTO);

  ResultInSideDto deleteKedb(Long kedbId);

  List<String> getSequenseKedb(String seqName, int... size);

  String getSeqTableKedb(String seq);

  ResultInSideDto insertOrUpdateListKedb(List<KedbDTO> listKedbDTO);

  String getOffset(UserTokenGNOCSimple userTokenGNOC);

  List<KedbDTO> synchKedbByCreateTime(String fromDate, String toDate);

  List<KedbDTO> getListKedbByCondition(List<ConditionBean> lstCondition,
      int rowStart, int maxRow, String sortType, String sortFieldList);

  List<CatItemDTO> getListSubCategory(Long typeId);

  List<CatItemDTO> getListCatItemDTO(CatItemDTO catItemDTO);

  List<KedbFilesEntity> getListKedbFilesByKedbId(Long kedbId);

  KedbFilesEntity findKedbFilesById(Long kedbFileId);

  ResultInSideDto insertKedbFiles(KedbFilesDTO kedbFilesDTO);

  ResultInSideDto deleteKedbFiles(KedbFilesEntity kedbFilesEntity);

  List<com.viettel.gnoc.ws.dto.KedbDTO> synchKedbByCreateTime2(String fromDate, String toDate);
}
