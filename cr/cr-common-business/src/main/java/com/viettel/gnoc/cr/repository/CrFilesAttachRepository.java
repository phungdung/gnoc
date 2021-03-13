package com.viettel.gnoc.cr.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.cr.dto.CrFileObjectInsite;
import com.viettel.gnoc.cr.dto.CrFilesAttachDTO;
import com.viettel.gnoc.cr.dto.CrFilesAttachInsiteDTO;
import com.viettel.gnoc.cr.dto.CrFilesAttachResultDTO;
import com.viettel.gnoc.cr.dto.TempImportDataDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface CrFilesAttachRepository {

  List<CrFilesAttachInsiteDTO> getListCrFilesAttachDTO(CrFilesAttachInsiteDTO crFilesAttachDTO,
      int rowStart,
      int maxRow, String sortType, String sortFieldList);

  ResultInSideDto add(CrFilesAttachInsiteDTO crFilesAttachDTO);

  List<CrFilesAttachInsiteDTO> getListCrFilesAttachByCondition(List<ConditionBean> lstCondition,
      int rowStart, int maxRow, String sortType, String sortFieldList);

  String onUpdateIsRunMop(String crIdParent, List<String> lstDtCode, String crIdChild);

  List<TempImportDataDTO> getListTempImportDataDTO(TempImportDataDTO tempImportDataDTO);

  CrFilesAttachInsiteDTO findFileAttachById(Long id);

  Datatable getListFilesSearchDataTable(CrFilesAttachInsiteDTO crFilesAttachDTO);

  List<CrFilesAttachInsiteDTO> getListCrFilesSearch(CrFilesAttachInsiteDTO crFilesAttachDTO);

  List<CrFileObjectInsite> getListTemplateFileByProcess(String crProcessId, String fileType);

  String deleteCrFilesAttachByCrIdAndType(String crId, String fileType);

  List<CrFilesAttachInsiteDTO> getListCrFilesByCrId(String crId, boolean isImportByProcess);

  void deleteListAttachByListId(List<String> fileId, Long crId);

  String deleteCrFilesAttach(Long id);

  List<UsersInsideDto> actionGetUserByUserName(String userName);

  ResultInSideDto insertByModel(CrFilesAttachInsiteDTO crFilesAttachDTO, String colId);

  String deleteListTempImportDataDTO(List<TempImportDataDTO> tempImportDataDTOs);

  String insertTempImportDataDTO(List<TempImportDataDTO> tempImportDataDTOs);

  List<CrFilesAttachDTO> getListFileImportByProcess(CrFilesAttachInsiteDTO dto);

  List<CrFilesAttachInsiteDTO> getListCrFilesToUpdateOrInsert(
      List<CrFilesAttachInsiteDTO> lstUpload, boolean isImportByProcess);

  List<CrFilesAttachInsiteDTO> getListCrFilesToUpdateOrInsertForList(
      List<CrFilesAttachInsiteDTO> lstUpload,
      boolean isImportByProcess, boolean isRemove);

  List<CrFilesAttachDTO> search(CrFilesAttachDTO tDTO, int start, int maxResult,
      String sortType, String sortField);

  List<CrFilesAttachResultDTO> getListFileImportByProcessOutSide(CrFilesAttachDTO dto);

  List<CrFilesAttachInsiteDTO> checkExistMop(String crId);

  List<CrFilesAttachDTO> getCrFileDT(String crId);

  void deleteFileDT(List<String> fileId, Long crId);

  List<CrFilesAttachDTO> getCrFileAttachForOutSide(CrFilesAttachDTO crFilesAttachDTO, int rowStart, int maxRow, String sortType, String sortFieldList);
}
