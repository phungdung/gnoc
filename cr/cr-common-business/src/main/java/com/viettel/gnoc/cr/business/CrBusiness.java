package com.viettel.gnoc.cr.business;

import com.viettel.aam.AppGroup;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.dto.UsersDTO;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.cr.dto.AppGroupInsite;
import com.viettel.gnoc.cr.dto.CrDTO;
import com.viettel.gnoc.cr.dto.CrInsiteDTO;
import com.viettel.gnoc.cr.dto.CrProcessWoDTO;
import com.viettel.gnoc.cr.dto.CrUpdateMopStatusHisDTO;
import com.viettel.gnoc.cr.dto.ItemDataCRInside;
import com.viettel.gnoc.cr.dto.ObjResponse;
import com.viettel.gnoc.cr.dto.WorkLogCategoryInsideDTO;
import com.viettel.gnoc.wfm.dto.WoSearchDTO;
import com.viettel.gnoc.wo.dto.UserGroupCategoryDTO;
import com.viettel.gnoc.wo.dto.WoCdGroupInsideDTO;
import com.viettel.gnoc.wo.dto.WoCdGroupTypeDTO;
import com.viettel.gnoc.wo.dto.WoCdGroupTypeUserDTO;
import com.viettel.gnoc.wo.dto.WoInsideDTO;
import com.viettel.gnoc.wo.dto.WoPriorityDTO;
import com.viettel.gnoc.wo.dto.WoTypeInsideDTO;
import com.viettel.gnoc.wo.dto.WorkLogInsiteDTO;
import com.viettel.vmsa.MopGnoc;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import viettel.passport.client.UserToken;

public interface CrBusiness {

  Datatable getListCRBySearchTypePagging(CrInsiteDTO crDTO, String locale);

  ResultInSideDto addNewCrClient(CrInsiteDTO crDTO);

  CrInsiteDTO findCrById(Long id);

  CrInsiteDTO getCrById(Long id, UserToken userToken);

  List<ItemDataCRInside> getListScopeOfUserForAllRole(CrInsiteDTO crInsiteDTO);

  Datatable getNetworkNodeFromQLTN(AppGroupInsite appGroupInsite);

  Datatable searchParentCr(String system, String code, int page, int size);

  ResultInSideDto onUpdateCrClient(CrInsiteDTO crDTO);

  String actionApproveCR(CrInsiteDTO crDTO, String locale);

  String actionAppraiseCr(CrInsiteDTO crDTO, String locale);

  String actionAppraiseCrForServer(CrInsiteDTO crDTO, String locale);

  String actionVerify(CrInsiteDTO crDTO, String locale);

  String actionVerifyForServer(CrInsiteDTO crDTO, String locale);

  String actionScheduleCr(CrInsiteDTO crDTO, String locale);

  String actionReceiveCr(CrInsiteDTO crDTO, String locale);

  List<String> getSequenseCr(String seqName, int... size);

  List<CrInsiteDTO> getListSecondaryCr(CrInsiteDTO crInsiteDTO);

  List<CrInsiteDTO> getListPreApprovedCr(CrInsiteDTO crInsiteDTO);

  String actionResolveCr(CrInsiteDTO crDTO, String locale);

  String actionResolveCrForService(CrInsiteDTO crDTO, String locale);

  String actionCloseCr(CrInsiteDTO crDTO, String locale);

  String actionAssignCab(CrInsiteDTO crDTO, String locale);

  String actionCab(CrInsiteDTO crDTO, String locale);

  String actionCabForServer(CrInsiteDTO crDTO, String locale);

  String actionEditCr(CrInsiteDTO crDTO, String locale);

  String actionCancelCr(CrInsiteDTO crDTO);

  ResultInSideDto checkDuplicateCr(CrInsiteDTO crInsiteDTO) throws Exception;

  ResultInSideDto importCheckCr(MultipartFile fileImport);

  Datatable loadWorkOrder(WoSearchDTO woSearchDTO);

  List<WoTypeInsideDTO> getListWoTypeDTO(WoTypeInsideDTO woTypeDTO);

  File exportSearchData(CrInsiteDTO crDTO);

  Datatable getListCrForRelateOrPreApprove(CrInsiteDTO crDTO);

  Datatable getListWorklogSearch(WorkLogInsiteDTO dto);

  List<UserGroupCategoryDTO> getListUserGroupBySystem(UserGroupCategoryDTO lstCondition,
      String step);

  List<WorkLogCategoryInsideDTO> getListWorkLogCategoryDTO(
      WorkLogCategoryInsideDTO workLogCategoryDTO);

  ResultInSideDto insertWorkLog(WorkLogInsiteDTO workLogDTO);

  String sendSMSToLstUserConfig(String crId, String contentType);

  List<WoCdGroupInsideDTO> getListCdGroupByUser(WoCdGroupTypeUserDTO woCdGroupTypeUserDTO);

  List<WoCdGroupTypeDTO> getListWoCdGroupTypeDTO(WoCdGroupTypeDTO woCdGroupTypeDTO);

  List<WoPriorityDTO> getListWoPriorityDTO(WoPriorityDTO woPriorityDTO);

  Datatable getListDataSearch(WoSearchDTO woSearchDTO);

  Datatable loadCRRelated(CrInsiteDTO crInsiteDTO);

  ResultInSideDto loadMop(CrInsiteDTO crInsiteDTO) throws Exception;

  ResultInSideDto actionAssignCabMulti(List<CrInsiteDTO> crInsiteDTOS, String locale);

  ResultInSideDto insertWoFromWeb(List<MultipartFile> fileAttacks,
      List<MultipartFile> fileCfgAttacks, WoInsideDTO woInsideDTO);

  ResultInSideDto actionApproveCRGeneral(CrInsiteDTO crDTO);

  ResultInSideDto actionAppraisCRGeneral(CrInsiteDTO crDTO);

  ResultInSideDto actionVerifyCRGeneral(CrInsiteDTO crDTOSave);

  String actionScheduleCrForServer(CrInsiteDTO crDTO, String locale);

  ResultInSideDto actionScheduleCRGeneral(CrInsiteDTO crDTO);

  ResultInSideDto actionReceiveCRGeneral(CrInsiteDTO crDTO);

  ResultInSideDto actionResolveCRGeneral(CrInsiteDTO crDTO);

  ResultInSideDto actionAssignCabCRGeneral(CrInsiteDTO crDTO);

  ResultInSideDto actionCabCRGeneral(CrInsiteDTO crDTO);

  ResultInSideDto actionEditCRGeneral(CrInsiteDTO crDTO);

  List<CrInsiteDTO> actionGetListDuplicateCRImpactedNode(CrInsiteDTO crDTO);

//  WorkLogCategoryDTO findWorkLogCategoryById(Long id);

  Datatable getListCRFromOtherSystem(CrInsiteDTO crDTO);

  List<AppGroup> getListAppGroup();

  ResultInSideDto changeCheckboxAction(CrInsiteDTO crDTO);

  ResultInSideDto doAssignHandoverCa(CrInsiteDTO crInsiteDTO, List<MultipartFile> lstFile)
      throws IOException;

  ResultInSideDto createObject(CrInsiteDTO tForm);

  ResultInSideDto delete(Long id);

  ResultDTO updateCrWithNoti(CrInsiteDTO tForm, String locale);

  String actionUpdateNotify(CrInsiteDTO crDTO, Long actionCode);

  UsersDTO getUserInfo(String userName);

  UnitDTO getUnitInfo(String unitId);

  CrProcessWoDTO processWOTabAdd(CrInsiteDTO crInsiteDTO);

  int insertMopUpdateHis(CrUpdateMopStatusHisDTO hisDTO);

  ObjResponse getListCRBySearchTypePagging(CrDTO crDTO, int start, int maxResult, String locale);

  List<WorkLogInsiteDTO> getListWorkLogDTO(WorkLogInsiteDTO workLogDTO);

  List<ResultDTO> actionGetProvinceMonitoringParamFix(String userId, String unitId,
      String searchChild, String startDate, String endDate);

  Datatable getDataTableSecondaryCr(CrInsiteDTO crInsiteDTO);

  Datatable getDataTablePreApprovedCr(CrInsiteDTO crInsiteDTO);

  ResultInSideDto actionCloseGeneralCr(CrInsiteDTO crDTO, String locale);

  ResultInSideDto checkCreateWOWhenCloserCr(CrInsiteDTO crDTO);

  List<CrDTO> getListCrInfo(CrInsiteDTO crDTO);

  String actionApproveServiceCR(CrInsiteDTO crDTO, String locale);

  String actionReceiveServiceCR(CrInsiteDTO crDTO, String locale);

  String actionEditServiceCR(CrInsiteDTO crDTO, String locale);

  String actionAssignCabServiceCR(CrInsiteDTO crDTO, String locale);

  String updateWorkOrder(String crNumber, Long typeOperation, MopGnoc mopGnoc) throws Exception;

  ResultInSideDto deletWoMopTest(WoInsideDTO woInsideDTO);

  List<CrDTO> getListCRForExportServiceV2(CrInsiteDTO crDTO, String lstCrId,
      Date earliestCrCreatedTime, Date earliestCrStartTime, Date lastestCrStartTime,
      Date latestCrUpdateTime, int start, int end, String locale);

  List<CrDTO> getListSecondaryCr(CrDTO crDTO);

  List<CrInsiteDTO> getListCrByCondition(List<ConditionBean> lstCondition, int rowStart,
      int maxRow, String sortType, String sortFieldList);

  ResultInSideDto approveAssign(CrInsiteDTO crDTOMain);

  ResultInSideDto rejectAssign(CrInsiteDTO crDTOMain);

  void processDayOff(CrInsiteDTO crDTO, String actionRight);

  String actionCancelCrGeneral(CrInsiteDTO crDTO);

  //TrungDuong them getList CR voi CR co IP tac đong = IP vua click tuong ung
  Datatable getListCrByIp(CrInsiteDTO crInsiteDTO);

  ResultInSideDto updateCrTimeOverdue(CrInsiteDTO crDTO);

  ResultInSideDto updateCrTimeOverdueToMop(CrInsiteDTO crDTO, CrInsiteDTO crRollBack);

  ResultInSideDto insertWorkLogProxy(WorkLogInsiteDTO workLogInsiteDTO);
}



