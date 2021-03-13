package com.viettel.gnoc.sr.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.cr.dto.CrInsiteDTO;
import com.viettel.gnoc.od.dto.OdSearchInsideDTO;
import com.viettel.gnoc.sr.dto.SRApproveDTO;
import com.viettel.gnoc.sr.dto.SRCatalogDTO;
import com.viettel.gnoc.sr.dto.SRConfig2DTO;
import com.viettel.gnoc.sr.dto.SRConfigDTO;
import com.viettel.gnoc.sr.dto.SRDTO;
import com.viettel.gnoc.sr.dto.SREvaluateDTO;
import com.viettel.gnoc.sr.dto.SRMopDTO;
import com.viettel.gnoc.sr.dto.SRParamDTO;
import com.viettel.gnoc.sr.dto.SRReasonRejectDTO;
import com.viettel.gnoc.sr.dto.SRRenewDTO;
import com.viettel.gnoc.sr.dto.SRRoleDTO;
import com.viettel.gnoc.sr.dto.SRRoleUserInSideDTO;
import com.viettel.gnoc.sr.dto.SRWorkLogDTO;
import com.viettel.gnoc.sr.dto.SRWorklogTypeDTO;
import com.viettel.gnoc.sr.dto.SrInsiteDTO;
import com.viettel.gnoc.sr.dto.SrWsToolCrDTO;
import com.viettel.gnoc.sr.dto.UnitSRCatalogDTO;
import com.viettel.gnoc.sr.model.SRParamEntity;
import com.viettel.gnoc.wo.dto.WoDTOSearch;
import java.io.File;
import java.io.IOException;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface SrBusiness {

  Datatable getListSR(SrInsiteDTO srDTO);

  Datatable getListSRByUserLogin(SrInsiteDTO srDTO);

  ResultInSideDto insertSR(SrInsiteDTO srDTO);

  ResultInSideDto insertSRDTO(SrInsiteDTO srDTO);

  ResultInSideDto updateSR(SrInsiteDTO srDTO);

  ResultInSideDto saveSrChild(SrInsiteDTO srChild);

  ResultInSideDto getSRCode(String country, Long srId);

  List<SRConfigDTO> getServiceArrayCBB(String srUser, String country);

  List<SRConfigDTO> getServiceGroupCBB(String configCode, String country);

  List<SRCatalogDTO> getListServicesCBB(String servicesGroup);

  List<UnitDTO> getListUnitCBB();

  List<UnitDTO> getListUnitBySeviceCBB(String services, boolean isMoreUnit, String srUser);

  SrInsiteDTO getChangeExecutionTimeAndFlowExecute(String startTime, String executionTime,
      String countryId);

  List<SRRoleDTO> getListSRRoleCBB(Long country);

  File exportData(SrInsiteDTO srDTO) throws Exception;

  SrInsiteDTO findNationByUnitId(Long unitId);

  List<SRCatalogDTO> getListCBBSeviceInsert(SrInsiteDTO srInsiteDTO);

  List<SRRoleUserInSideDTO> getCmbRoleCode(String unit, String service);

  List<SRRoleUserInSideDTO> getCmbSrUser(SRRoleUserInSideDTO srRoleUserDTO);

  SrInsiteDTO getSRDetail(SrInsiteDTO srInsiteDTO);

  List<SRConfigDTO> getByConfigGroup(String configGroup);

  List<SRWorklogTypeDTO> getBySRStatus(String srStatus);

  List<SRWorkLogDTO> getListSRWorklogWithUnit(Long srId);

  ResultInSideDto insertSRWorklog(SRWorkLogDTO srWorklogDTO);

  List<SRReasonRejectDTO> getCmbReason(Long wlTypeId);

//  file đính kèm

  List<SRConfig2DTO> getCBBFileType(SrInsiteDTO srInsiteDTO);

  ResultInSideDto deleteSRFile(GnocFileDto gnocFileDto);

  ResultInSideDto insertSRFile(List<MultipartFile> srFileList, SrInsiteDTO srInsiteDTO)
      throws IOException;

  List<GnocFileDto> getListSRFile(GnocFileDto gnocFileDto);

  List<ResultInSideDto> getCBBCmbFileName(SrWsToolCrDTO srWsToolCrDTO) throws Exception;

  String fileByPath(SrWsToolCrDTO srWsToolCrDTO) throws Exception;

  List<GnocFileDto> getListSRDialogFile(GnocFileDto gnocFileDto);

  ResultInSideDto insertSRDialogFile(List<MultipartFile> srFileList, SrInsiteDTO srInsiteDTO);

  //namtn
  //add tab Thong tin phe duyet
  SRApproveDTO findSRApproveBySrId(Long srId);

  ResultInSideDto insertOrUpdateSRApprove(SRApproveDTO srApproveDTO);

  //add tab Thong tin gia han
  SRRenewDTO findSRRenewBySrId(Long srId);

  ResultInSideDto insertOrUpdateSRRenew(SRRenewDTO srRenewDTO);

  //add tab Danh sach key-value
  ResultInSideDto insertListSRParam(List<SRParamDTO> lsSrParam);

  List<SRParamEntity> findListSRParamBySrId(Long srId);

  //add tab Thong tin Mop
  List<SRMopDTO> getListSRMopDTO(SRMopDTO srMopDTO);

  List<SRMopDTO> getListSRMopNotSR(SRMopDTO srMopDTO);

  ResultInSideDto insertListSRMop(List<SRMopDTO> lsSrMop);

  ResultInSideDto updateListSRMop(List<SRMopDTO> lsSrMop);

  ResultInSideDto deleteListSRMop(List<SRMopDTO> lsSrMop);

  //add tab Thong tin danh gia
  ResultInSideDto insertOrUpdateSREvaluate(SREvaluateDTO srEvaluateDTO);

  List<SREvaluateDTO> findSREvaluateBySrId(Long srId);

  //add tab OD
  List<OdSearchInsideDTO> findListOdBySr(Long srId);

  //add tab WO
  List<WoDTOSearch> findListWoBySr(SrInsiteDTO srDto);

  //add tab CR
  List<CrInsiteDTO> findListCrBySr(Long srId);

  List<UnitSRCatalogDTO> getListUnitSRCatalog(SRCatalogDTO dto);

  ResultInSideDto deleteSR(SrInsiteDTO srInsiteDTO);

  ResultInSideDto deleteSRChild(SrInsiteDTO srChild);

  ResultInSideDto roleActionSRChild(String createdUser);

  String updateStatusSRForProcess(String srId, String status);

  List<SRConfigDTO> getDataByConfigCode(SRConfigDTO srConfigDTO);

  ResultInSideDto updateSRDTO(SrInsiteDTO srInsiteDTO);

  SrInsiteDTO setStartTimeAndEndTimeSR(SrInsiteDTO srDTO, String locationId,
      String executionTime, String isAddDay);

  //truongnt add start
  List<SRApproveDTO> getListSRApprove(Long srId);

  List<SRRenewDTO> getListSRRenewDTO(SrInsiteDTO srInsiteDTO);

  //truongnt add end
  ResultInSideDto deleteMopFileWS(SrInsiteDTO srInsiteDTO);

  List<SRDTO> getListSRForWO(SRDTO srdto);

  //thangdt nang cap pt
  List<SrInsiteDTO> getListDataSearchForPt(SrInsiteDTO srInsiteDTO);

  //tripm nang cap od
  SrInsiteDTO finSrFromOdByProxyId(Long srId);

  boolean checkSRConcluded();

  ResultInSideDto getListSRFileCheckRole(GnocFileDto gnocFileDto);

  ResultInSideDto closedSR(Long srId);

  ResultInSideDto updateCrNumberForSR(String crNumber, Long srId);

  ResultInSideDto updateCrNumberTabSRForCR(CrInsiteDTO crInsiteDTO);

  Datatable getListTabSrChild(SrInsiteDTO srInsiteDTO);
}
