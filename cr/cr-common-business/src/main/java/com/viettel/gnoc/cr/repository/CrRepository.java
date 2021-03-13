package com.viettel.gnoc.cr.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.dto.UsersDTO;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.cr.dto.CrCreatedFromOtherSysDTO;
import com.viettel.gnoc.cr.dto.CrDTO;
import com.viettel.gnoc.cr.dto.CrInsiteDTO;
import com.viettel.gnoc.cr.dto.CrUpdateMopStatusHisDTO;
import com.viettel.gnoc.cr.dto.ObjResponse;
import com.viettel.gnoc.wo.model.WorkLogEntity;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Repository;
import viettel.passport.client.UserToken;

@Repository
public interface CrRepository {

  Datatable getListCRBySearchType(CrInsiteDTO crDTO, String locale);

  List<String> getSequenseCr(String seqName, int[] size);

  ResultInSideDto insertCr(CrInsiteDTO crDTO);

  String updateCr(CrInsiteDTO crDTO);

  void actionUpdateOriginalTimeOfCR(CrInsiteDTO crDTO);

  void actionUpdateRelatedCR(CrInsiteDTO crDTO);

  String actionCreateMappingCRwithOtherSys(CrInsiteDTO crDTO);

  String actionResetApproveCRIncaseOfEditCR(CrInsiteDTO crDTO);

  ResultDTO actionAutomaticApproveCrIncaseOfManagerCreateOrEditCR(
      CrInsiteDTO crDTO, String locale);

  CrInsiteDTO findCrById(Long id, UserToken userTokenGNOC);

  int insertMopUpdateHis(CrUpdateMopStatusHisDTO hisDTO);

  String actionApproveCR(CrInsiteDTO crDTO, String locale);

  String actionAppraiseCr(CrInsiteDTO crDTO, String locale);

  String actionVerify(CrInsiteDTO crDTO, String locale);

  String actionScheduleCr(CrInsiteDTO crDTO, String locale);

  String actionReceiveCr(CrInsiteDTO crDTO, String locale);

  List<CrInsiteDTO> getListPreApprovedCr(CrInsiteDTO crInsiteDTO);

  List<CrInsiteDTO> getListSecondaryCr(CrInsiteDTO crInsiteDTO);

  String actionResolveCr(CrInsiteDTO crDTO, String locale);

  String actionCloseCr(CrInsiteDTO crDTO, String locale);

  String actionAssignCab(CrInsiteDTO crDTO, String locale);

  String actionCab(CrInsiteDTO crDTO, String locale);

  String actionEditCr(CrInsiteDTO crDTO, String locale);

  String actionCancelCR(CrInsiteDTO crDTO);

  List<CrInsiteDTO> getListCRIdForExport(CrInsiteDTO crDTO);

  List<CrInsiteDTO> getListCRForExport(CrInsiteDTO crDTO, String lstCrId,
      Date earliestCrCreatedTime, Date earliestCrStartTime, Date lastestCrStartTime,
      Date latestCrUpdateTime, String locale);

  List<CrInsiteDTO> getListCRByLstId(String lstId);

  List<CrInsiteDTO> getListCrByCondition(List<ConditionBean> lstCondition, int rowStart, int maxRow,
      String sortType, String sortFieldList);

  String actionLoadMopFromCRParent(String crId, String isLoadMop);

  List<CrCreatedFromOtherSysDTO> getCrCreatedFromOtherSys(
      CrCreatedFromOtherSysDTO crCreatedFromOtherSysDTO);

  Datatable getListCRFromOtherSystem(CrInsiteDTO crDTO);

  ResultInSideDto delete(Long id);

  CrDTO getCrByNumber(String crNumber, Date startCreatedDate, Date endCreatedDate);

  String actionUpdateNotify(CrInsiteDTO crDTO, Long actionCode);

  UsersDTO getUserInfo(String userName);

  UnitDTO getUnitInfo(String unitId);

  ObjResponse getListCRBySearchTypePagging(CrDTO crDTO, int start, int maxResult, String locale);

  List<ResultDTO> actionGetProvinceMonitoringParamFix(String userId, String unitId,
      String searchChild,
      String startDate,
      String endDate);

  List<Long> getlistCrIdsByNodeInfo(Date startDate, Date endDate, List<Long> ipIds);

  List<CrDTO> getCrByIdAndResolveStatuṣ̣(List<Long> crIds, Long resolveStatus);

  List<CrDTO> getListCrInfo(String crNumber, Date startCreatedDate, Date endCreatedDate);

  List<CrDTO> getListCRForExportServiceV2(CrInsiteDTO crDTO, String lstCrId,
      Date earliestCrCreatedTime, Date earliestCrStartTime, Date lastestCrStartTime,
      Date latestCrUpdateTime, int start, int end, String locale);

  List<CrDTO> getListSecondaryCrOutSide(CrDTO crDTO);

  CrInsiteDTO findCrById(Long id);

  void flushSession();

  String actionVerifyMrIT(CrInsiteDTO crDTO, String locale);

  //TrungDuong them getList CR voi CR co IP tac đong = IP vua click tuong ung
  Datatable getListCrByIp(CrInsiteDTO crInsiteDTO);

  List<CrDTO> getCRbyImpactIP(CrInsiteDTO crInsiteDTO);

  void processDayOff(CrInsiteDTO crDTO, String actionRight);

  ResultInSideDto insertWorkLog(WorkLogEntity workLogEntity);
}
