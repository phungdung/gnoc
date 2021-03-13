package com.viettel.gnoc.commons.business;

import com.viettel.gnoc.commons.dto.*;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.viettel.gnoc.wfm.dto.ConfigPropertyDTO;
import org.springframework.stereotype.Service;

/**
 * @author TungPV
 */
@Service
public interface CommonBusiness {

  ResultInSideDto getDBSysDate();

  List<GnocTimezoneDto> getAllGnocTimezone();

  List<GnocLanguageDto> getAllGnocLanguage();

  UsersInsideDto getUserByUserName(String userName);

  List<UsersInsideDto> getListUserOfUnit(Long unitId);

  UsersInsideDto getUserByUserId(Long userId);

  Object updateObjectData(Object objSrc, Object objDes);

  List<DataItemDTO> getListCombobox(ObjectSearchDto objectSearchDto);

  List<TreeDTO> getTreeData(ObjectSearchDto objectSearchDto);

  List<DataItemDTO> getListDataItem(String dataCode);

  Map<String, String> getConfigProperty();

  ResultInSideDto checkRoleSubAdmin(String view);

  List<DashboardDTO> searchDataDashboard(DashboardDTO dashboardDTO);

  List<DashboardDTO> getDataTableDashboard();

  List<RolesDTO> getListRole();

  Datatable getListComment(UserCommentDTO userCommentDTO);

  ResultInSideDto addComment(UserCommentDTO userCommentDTO);

  List<ContactDTO> getListContact();

  //thanhlv12 add 22-09-2020

  ConfigPropertyDTO getConfigPropertyByKey ();

  ResultInSideDto insertHisUserImpact(DataHistoryChange dataHistoryChange);

  Datatable getListHistory(HisUserImpactDTO hisUserImpactDTO);

  File exportData(HisUserImpactDTO hisUserImpactDTO) throws Exception;

  List<CatItemDTO> getListCommonLink(String locale);

  CatItemDTO getConfigIconDislay(String keyCode);
  //end
}
