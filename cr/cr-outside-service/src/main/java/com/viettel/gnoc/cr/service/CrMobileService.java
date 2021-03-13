package com.viettel.gnoc.cr.service;

import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.UserTokenGNOCSimple;
import com.viettel.gnoc.commons.dto.UsersDTO;
import com.viettel.gnoc.cr.dto.CrAlarmDTO;
import com.viettel.gnoc.cr.dto.CrApprovalDepartmentDTO;
import com.viettel.gnoc.cr.dto.CrCableDTO;
import com.viettel.gnoc.cr.dto.CrDTO;
import com.viettel.gnoc.cr.dto.CrFilesAttachDTO;
import com.viettel.gnoc.cr.dto.CrFilesAttachResultDTO;
import com.viettel.gnoc.cr.dto.CrHisDTO;
import com.viettel.gnoc.cr.dto.CrImpactFrameDTO;
import com.viettel.gnoc.cr.dto.CrImpactedNodesDTO;
import com.viettel.gnoc.cr.dto.CrModuleDetailDTO;
import com.viettel.gnoc.cr.dto.CrProcessDTO;
import com.viettel.gnoc.cr.dto.CrVendorDetailDTO;
import com.viettel.gnoc.cr.dto.ItemDataCR;
import com.viettel.gnoc.cr.dto.ObjResponse;
import com.viettel.gnoc.cr.dto.UserCabCrForm;
import java.util.Date;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService(serviceName = "CrMobileService")
public interface CrMobileService {

  @WebMethod(operationName = "getListCRByCount")
  public List<CrDTO> getListCRByCount(@WebParam(name = "crDTO") CrDTO crDTO);

  @WebMethod(operationName = "getListCRByMobileFix")
  public ObjResponse getListCRByMobileFix(@WebParam(name = "crDTO") CrDTO crDTO,
      @WebParam(name = "rowStart") int rowStart,
      @WebParam(name = "maxRow") int maxRow,
      @WebParam(name = "userName") String userName);

  @WebMethod(operationName = "actionGetMonitoringParamFix")
  public List<ResultDTO> actionGetMonitoringParamFix(
      @WebParam(name = "userName") String userName,
      @WebParam(name = "searchChild") String searchChild,
      @WebParam(name = "startDate") String startDate,
      @WebParam(name = "endDate") String endDate);

  @WebMethod(operationName = "getListScopeOfUserForAllRole")
  public List<ItemDataCR> getListScopeOfUserForAllRole(@WebParam(name = "crDTO") CrDTO crDTO);

  @WebMethod(operationName = "getListImpactSegmentCBB")
  public List<ItemDataCR> getListImpactSegmentCBB(@WebParam(name = "form") Object form);

  @WebMethod(operationName = "getListSubcategoryCBB")
  public List<ItemDataCR> getListSubcategoryCBB(@WebParam(name = "form") Object form);


  @WebMethod(operationName = "getListCRForExport")
  public List<CrDTO> getListCRForExport(
      @WebParam(name = "crDTO") CrDTO crDTO,
      @WebParam(name = "lstCrId") String lstCrId,
      @WebParam(name = "earliestCrCreatedTime") String earliestCrCreatedTime,
      @WebParam(name = "earliestCrStartTime") String earliestCrStartTime,
      @WebParam(name = "latestCrStartTimeStr") String latestCrStartTimeStr,
      @WebParam(name = "latestCrUpdateTime") String latestCrUpdateTime,
      @WebParam(name = "rowStart") int rowStart,
      @WebParam(name = "maxRow") int maxRow);

  @WebMethod(operationName = "getListCrApprovalDepartmentDTO")
  public List<CrApprovalDepartmentDTO> getListCrApprovalDepartmentDTO(
      @WebParam(name = "crApprovalDepartmentDTO") CrApprovalDepartmentDTO crApprovalDepartmentDTO,
      @WebParam(name = "rowStart") int rowStart,
      @WebParam(name = "maxRow") int maxRow,
      @WebParam(name = "sortType") String sortType,
      @WebParam(name = "sortFieldList") String sortFieldList);

  @WebMethod(operationName = "getListCrFilesAttachDTO")
  public List<CrFilesAttachDTO> getListCrFilesAttachDTO(
      @WebParam(name = "crFilesAttachDTO") CrFilesAttachDTO crFilesAttachDTO,
      @WebParam(name = "rowStart") int rowStart,
      @WebParam(name = "maxRow") int maxRow,
      @WebParam(name = "sortType") String sortType,
      @WebParam(name = "sortFieldList") String sortFieldList);

  @WebMethod(operationName = "getListCrHisDTO")
  public List<CrHisDTO> getListCrHisDTO(@WebParam(name = "crHisDTO") CrHisDTO crHisDTO,
      @WebParam(name = "rowStart") int rowStart,
      @WebParam(name = "maxRow") int maxRow,
      @WebParam(name = "sortType") String sortType,
      @WebParam(name = "sortFieldList") String sortFieldList);


  @WebMethod(operationName = "getLisNodeOfCR")
  public List<CrImpactedNodesDTO> getLisNodeOfCR(
      @WebParam(name = "crId") Long crId,
      @WebParam(name = "crCreatedDate") String crCreatedDate,
      @WebParam(name = "earlierStartTime") String earlierStartTime,
      @WebParam(name = "nodeType") String nodeType,
      @WebParam(name = "saveType") String saveType
  );

  @WebMethod(operationName = "getListCrImpactedNodesDTO")
  public List<CrImpactedNodesDTO> getListCrImpactedNodesDTO(
      @WebParam(name = "crImpactedNodesDTO") CrImpactedNodesDTO crImpactedNodesDTO,
      @WebParam(name = "rowStart") int rowStart,
      @WebParam(name = "maxRow") int maxRow,
      @WebParam(name = "sortType") String sortType,
      @WebParam(name = "sortFieldList") String sortFieldList);

  @WebMethod(operationName = "getListCrImpactedNodes")
  public List<CrImpactedNodesDTO> getListCrImpactedNodes(
      @WebParam(name = "crImpactedNodesDTO") CrImpactedNodesDTO crImpactedNodesDTO,
      @WebParam(name = "startDate") Date startDate,
      @WebParam(name = "earlierStartTime") Date earlierStartTime);

  @WebMethod(operationName = "getListPreApprovedCr")
  public List<CrDTO> getListPreApprovedCr(@WebParam(name = "crDTO") CrDTO crDTO);

  @WebMethod(operationName = "getListSecondaryCr")
  public List<CrDTO> getListSecondaryCr(@WebParam(name = "crDTO") CrDTO crDTO);


  @WebMethod(operationName = "getListFileImportByProcess")
  public List<CrFilesAttachResultDTO> getListFileImportByProcess(
      @WebParam(name = "crFilesAttachDTO") CrFilesAttachDTO crFilesAttachDTO);

  @WebMethod(operationName = "getListModuleByCr")
  public List<CrModuleDetailDTO> getListModuleByCr(@WebParam(name = "crDTO") CrDTO cdto);

  @WebMethod(operationName = "getListVendorByCr")
  public List<CrVendorDetailDTO> getListVendorByCr(@WebParam(name = "crDTO") CrDTO cdto);

  @WebMethod(operationName = "getListAlarmByCr")
  public List<CrAlarmDTO> getListAlarmByCr(@WebParam(name = "crDTO") CrDTO cdto);

  @WebMethod(operationName = "getListCrCableDTO")
  public List<CrCableDTO> getListCrCableDTO(@WebParam(name = "crCableDTO") CrCableDTO crCableDTO,
      @WebParam(name = "rowStart") int rowStart,
      @WebParam(name = "maxRow") int maxRow,
      @WebParam(name = "sortType") String sortType,
      @WebParam(name = "sortFieldList") String sortFieldList);

  @WebMethod(operationName = "getListCrProcessCBB")
  public List<ItemDataCR> getListCrProcessCBB(@WebParam(name = "form") CrProcessDTO form);

  @WebMethod(operationName = "getListUserCab")
  public List<UserCabCrForm> getListUserCab(
      @WebParam(name = "impactSegmentId") String impactSegmentId,
      @WebParam(name = "executeUnitId") String executeUnitId);

  @WebMethod(operationName = "actionApproveCR")
  public String actionApproveCR(@WebParam(name = "crDTO") CrDTO crDTO);

  @WebMethod(operationName = "actionVerify")
  public String actionVerify(@WebParam(name = "crDTO") CrDTO crDTO);

  //
  @WebMethod(operationName = "actionAppraiseCr")
  public String actionAppraiseCr(@WebParam(name = "crDTO") CrDTO crDTO);

  @WebMethod(operationName = "actionReceiveCr")
  public String actionReceiveCr(@WebParam(name = "crDTO") CrDTO crDTO);

  @WebMethod(operationName = "actionScheduleCr")
  public String actionScheduleCr(@WebParam(name = "crDTO") CrDTO crDTO);

  @WebMethod(operationName = "actionResolveCr")
  public String actionResolveCr(@WebParam(name = "crDTO") CrDTO crDTO);

  @WebMethod(operationName = "actionCloseCr")
  public String actionCloseCr(@WebParam(name = "crDTO") CrDTO crDTO);

  @WebMethod(operationName = "actionCancelCr")
  public String actionCancelCr(@WebParam(name = "crDTO") CrDTO crDTO);

  @WebMethod(operationName = "actionUpdateNotify")
  public String actionUpdateNotify(@WebParam(name = "crDTO") CrDTO crDTO,
      @WebParam(name = "actionCode") Long actionCode);

  @WebMethod(operationName = "actionCab")
  public String actionCab(@WebParam(name = "crDTO") CrDTO crDTO);

  @WebMethod(operationName = "actionEditCr")
  public String actionEditCr(@WebParam(name = "crDTO") CrDTO crDTO);

  @WebMethod(operationName = "actionGetListUser")
  public List<UsersDTO> actionGetListUser(@WebParam(name = "deptId") String deptId,
      @WebParam(name = "userId") String userId,
      @WebParam(name = "userName") String userName,
      @WebParam(name = "fullName") String fullName,
      @WebParam(name = "staffCode") String staffCode,
      @WebParam(name = "unitName") String unitName,
      @WebParam(name = "unitCode") String unitCode,
      @WebParam(name = "isAppraise") String isAppraise);

  @WebMethod(operationName = "getCrByIdAndResolveStatus")
  public List<CrDTO> getCrByIdAndResolveStatus(
      @WebParam(name = "crIds") List<Long> crIds,
      @WebParam(name = "resolveStatus") Long resolveStatus
  );

  @WebMethod(operationName = "getListReturnCodeByActionCode")
  public List<ItemDataCR> getListReturnCodeByActionCode(
      @WebParam(name = "actionCode") String actionCode);

  @WebMethod(operationName = "getListImpactAffectCBB")
  public List<ItemDataCR> getListImpactAffectCBB(@WebParam(name = "form") Object form);

  @WebMethod(operationName = "getListAffectedServiceCBB")
  public List<ItemDataCR> getListAffectedServiceCBB(@WebParam(name = "form") Object form);

  @WebMethod(operationName = "getListDutyTypeCBB")
  public List<ItemDataCR> getListDutyTypeCBB(@WebParam(name = "form") CrImpactFrameDTO form);

  @WebMethod(operationName = "getListDeviceTypeCBB")
  public List<ItemDataCR> getListDeviceTypeCBB(@WebParam(name = "form") Object form);

  @WebMethod(operationName = "getListDeviceTypeByImpactSegmentCBB")
  public List<ItemDataCR> getListDeviceTypeByImpactSegmentCBB(@WebParam(name = "form") CrDTO form);

  @WebMethod(operationName = "getListActionCodeByCode")
  public List<ItemDataCR> getListActionCodeByCode(@WebParam(name = "code") String code);

  @WebMethod(operationName = "getFileByPath")
  public CrFilesAttachDTO getFileByPath(@WebParam(name = "path") String path);

  @WebMethod(operationName = "findCrById")
  public CrDTO findCrById(@WebParam(name = "crDTOId") Long id,
      @WebParam(name = "UserTokenGNOCSimple") UserTokenGNOCSimple userTokenGNOC);

  @WebMethod(operationName = "getListCRBySearchTypePagging")
  public ObjResponse getListCRBySearchTypePagging(@WebParam(name = "crDTO") CrDTO crDTO, @WebParam(name = "rowStart") int rowStart, @WebParam(name = "maxRow") int maxRow);

  @WebMethod(operationName = "actionAssignCab")
  public String actionAssignCab(@WebParam(name = "crDTO") CrDTO crDTO);

  @WebMethod(operationName = "getCreatedBySys")
  public List<ItemDataCR> getCreatedBySys(@WebParam(name = "crId") String crId);
}
