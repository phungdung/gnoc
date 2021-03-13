package com.viettel.gnoc.cr.business;

import com.viettel.aam.MopFileResult;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.InfraDeviceDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.cr.dto.AttachDtDTO;
import com.viettel.gnoc.cr.dto.CrFileObjectInsite;
import com.viettel.gnoc.cr.dto.CrFilesAttachDTO;
import com.viettel.gnoc.cr.dto.CrFilesAttachInsiteDTO;
import com.viettel.gnoc.cr.dto.CrFilesAttachResultDTO;
import com.viettel.gnoc.cr.dto.TemplateImportDTO;
import com.viettel.vipa.MopDetailOutputDTO;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import viettel.passport.client.UserToken;

public interface CrFileAttachBusiness {

  ResultInSideDto insertList(
      CrFilesAttachInsiteDTO crFilesAttachDTO,
      List<MultipartFile> lstMutilKPI, List<MultipartFile> lstMutilDT,
      List<MultipartFile> lstMutilTest, List<MultipartFile> lstMutilRoll,
      List<MultipartFile> lstMutilPlant, List<MultipartFile> lstMutilImpactScenario,
      List<MultipartFile> lstMutilForm, List<MultipartFile> lstMutilFile,
      List<MultipartFile> lstMutilTxt, List<MultipartFile> lstMutilFileOther,
      List<MultipartFile> lstMutilProcess, List<MultipartFile> lstMultilLogImpact)
      throws Exception;

  List<AttachDtDTO> loadDtFromTDTT(String userName, String crProcess, String lstWork);

  List<AttachDtDTO> loadDtFromVIPA(String userName, String system, String crProcess,
      String lstWork);

  MopDetailOutputDTO getMopInfo(String dtCode);//Neu la vipa thi goi lai ham lay IP va file

  List<InfraDeviceDTO> getNetworkNodeTDTTV2(List<String> lstIp, String nationCode);

  MopFileResult getMopFile(String dtCode, String crNumber);

  CrFilesAttachInsiteDTO findFileAttachById(Long id);

  List<CrFilesAttachInsiteDTO> getListCrFilesSearch(CrFilesAttachInsiteDTO crFilesAttachDTO);

  Datatable getListFilesSearchDataTable(CrFilesAttachInsiteDTO crFilesAttachDTO);

  List<CrFilesAttachInsiteDTO> getListCrFilesAttachDTO(CrFilesAttachInsiteDTO crFilesAttachDTO,
      int rowStart, int maxRow, String sortType, String sortFieldList);

  List<CrFileObjectInsite> getListTemplateFileByProcess(String crProcessId, String actionRight);

  String insertListNoID(List<CrFilesAttachInsiteDTO> lstUpload);

  ResultInSideDto selectDTFile(List<AttachDtDTO> lstAttachDtSelected, String system,
      String crNumber, String crId);

  List<TemplateImportDTO> insertListNoIDForImport(List<CrFilesAttachInsiteDTO> lstUpload,
      UserToken userToken, UnitDTO unitToken, boolean isRemove);

  List<TemplateImportDTO> insertListNoIDForImportForSR(List<CrFilesAttachInsiteDTO> lstUpload,
      UserToken userToken, UnitDTO unitToken, boolean isRemove);

  List<AttachDtDTO> loadDtTestFromVIPA(String userName);

  ResultInSideDto selectDTTestFile(List<AttachDtDTO> lstAttachDtSelected, String system,
      String crNumber, String crId);

  List<CrFilesAttachDTO> search(CrFilesAttachDTO tDTO, int start, int maxResult,
      String sortType, String sortField);

  List<CrFilesAttachResultDTO> getListFileImportByProcess(CrFilesAttachDTO dto);

  ResultInSideDto deleteFileDT(String crId);

  ResultInSideDto getListCrFilesSearchCheckRole(CrFilesAttachInsiteDTO crFilesAttachDTO);

  List<CrFilesAttachDTO> getCrFileAttachForOutSide(CrFilesAttachDTO crFilesAttachDTO,
      int rowStart, int maxRow, String sortType, String sortFieldList);
}
