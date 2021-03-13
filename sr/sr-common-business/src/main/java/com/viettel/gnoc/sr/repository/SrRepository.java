package com.viettel.gnoc.sr.repository;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UnitDTO;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.sr.dto.SRActionCodeDTO;
import com.viettel.gnoc.sr.dto.SRDTO;
import com.viettel.gnoc.sr.dto.SREvaluateDTO;
import com.viettel.gnoc.sr.dto.SRFilesDTO;
import com.viettel.gnoc.sr.dto.SRRenewDTO;
import com.viettel.gnoc.sr.dto.SRRoleDTO;
import com.viettel.gnoc.sr.dto.SRRoleUserInSideDTO;
import com.viettel.gnoc.sr.dto.SrInsiteDTO;
import com.viettel.gnoc.sr.model.SREntity;
import com.viettel.gnoc.sr.model.SRFilesEntity;
import com.viettel.gnoc.sr.model.SRRenewEntity;
import java.util.Date;
import java.util.List;

public interface SrRepository {

  BaseDto sqlSearch(SrInsiteDTO srdto);

  Datatable getListSR(SrInsiteDTO srdto);

  Datatable getListSRByUserLogin(SrInsiteDTO srInsiteDTO);

  ResultInSideDto insertSR(SrInsiteDTO srDTO);

  List<SRActionCodeDTO> searchSrActionCode(SRActionCodeDTO tDTO,
      int start, int maxResult, String sortType, String sortField);

  List<SrInsiteDTO> getListSRExport(SrInsiteDTO srdto);

  List<UnitDTO> getListSRUnitForDetail(SrInsiteDTO dto);

  List<SRRoleDTO> getListSRRole(SRRoleDTO srRoleDTO);

  SrInsiteDTO findNationByLocationId(Long locationId);

  List<SrInsiteDTO> getCrNumberCreatedFromSR(SrInsiteDTO dto, int rowStart, int maxRow,
      boolean outSide);

  List<Date> getDayOffForExecutionTime(String locationId);

  List<SrInsiteDTO> getWorkLog(SrInsiteDTO dto, int rowStart, int maxRow, boolean outSide);

  List<SREvaluateDTO> getListSREvaluate(SREvaluateDTO srEvaluateDTO, int rowStart, int maxRow,
      String sortType, String sortFieldList);

  List<SREvaluateDTO> getListSREvaluateNew(SREvaluateDTO srEvaluateDTO);

  List<SRRoleUserInSideDTO> searchSRRoleUser(SRRoleUserInSideDTO srRoleUserDTO);

  List<String> getLeaderApprove(String roleCode, String unitId);

  List<String> getListSequenseSR(String seq, int size);

  SrInsiteDTO getDetail(Long srId, String userToken);

  SrInsiteDTO getDetailNoOffset(Long srId);

  List<SrInsiteDTO> findByParenCode(String parenCode);

  boolean checkUserLoginIsLeader(String roleCode, String createdUser, String loginUser,
      String unitId, String type);

  String getUnitParentForApprove(String type, String unitId);

  ResultInSideDto deleteSR(Long srId);

  List<SrInsiteDTO> checkSRCreatedFromOtherSys(String srCode);

  List<SRDTO> getCrInfoCreatedFromSR(Long srId);

  ResultInSideDto updateStepIdCr(String crId, String stepId, Long srId);

  List<SRRenewDTO> getListSRRenewDTO(SRRenewEntity srRenewEntity,
      List<ConditionBean> lstConditionBean, int rowStart, int maxRow,
      String sortType, String sortFieldList);

  List<SRFilesEntity> getListSRFileByObejctId(Long obejctId);

  List<SRFilesEntity> getListSRFileByObejctId(String fileGroup, String fileType, Long obejctId);

  ResultInSideDto addSRFile(SRFilesDTO srFilesDTO);

  ResultInSideDto deleteSRFile(Long fileId);

  List<SrInsiteDTO> getTotalSRProcessTime(List<Long> lstSrId);

  List<SrInsiteDTO> getTotalCreateCrSlow(List<Long> lstSrId);

  List<SRDTO> getListSRForLinkCR(String loginUser, String srCode);

  List<SRDTO> getListSRForWO(SRDTO srdto);

  List<SRDTO> getListSRForOutside(SrInsiteDTO srdto);

  //tripm nang cap od
  SrInsiteDTO finSrFromOdByProxyId(Long srId);

  List<SREntity> getListSRChild(SrInsiteDTO srInsiteDTO);

  List<SrInsiteDTO> getListSRChildCodeByParentCode(List<String> srParentCode);

  int checkSRConcluded(String userLogin);

  ResultInSideDto updateSRProcessMess(String srCode);

  ResultInSideDto updateCrNumberForSR(String crNumber,Long srId);

  Datatable getListTabSrChild(SrInsiteDTO srInsiteDTO);
}
