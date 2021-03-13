package com.viettel.gnoc.commons.repository;

import com.viettel.gnoc.commons.dto.*;
import com.viettel.gnoc.commons.model.UsersEntity;
import com.viettel.gnoc.wfm.dto.ConfigPropertyDTO;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

/**
 * @author TungPV
 */
@Repository
public interface CommonRepository {

  ResultInSideDto getDBSysDate();

  List<GnocTimezoneDto> getAllGnocTimezone();

  List<GnocLanguageDto> getAllGnocLanguage();

  List<DataItemDTO> getListCombobox(ObjectSearchDto objectSearchDto);

  List<TreeDTO> getTreeData(ObjectSearchDto objectSearchDto);

  UsersInsideDto getUserByUserName(String userName);

  List<UsersEntity> getListUserOfUnit(Long unitId);

  UsersEntity getUserByUserId(Long userId);

  List<DataItemDTO> getListItemByDataCode(String dataCode);

  Map<String, String> getConfigProperty();

  String getConfigPropertyValue(String key);

  ConfigPropertyDTO getConfigPropertyObj(String key);

  List<DashboardDTO> searchDataDashboard(DashboardDTO dashboardDTO);

  List<DashboardDTO> getDataTableDashboard();

  List<RolesDTO> getListRole();

  Datatable getListComment(UserCommentDTO userCommentDTO);

  ResultInSideDto addComment(UserCommentDTO userCommentDTO);

  List<ContactDTO> getListContact();

  //thanhlv12 add 22-09-2020
  Boolean checkMaxRowExport(Integer row);

  ConfigPropertyDTO getConfigPropertyByKey ();

  ResultInSideDto insertHisUserImpact(DataHistoryChange dataHistoryChange);

  Datatable getListHistory(HisUserImpactDTO hisUserImpactDTO);

  List<CatItemDTO> getListCommonLink(String locale);

  CatItemDTO getConfigIconDislay(String keyCode);
  //end
}
