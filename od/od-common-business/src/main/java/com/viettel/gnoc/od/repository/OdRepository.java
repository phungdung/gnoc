package com.viettel.gnoc.od.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.model.UsersEntity;
import com.viettel.gnoc.od.dto.*;
import com.viettel.gnoc.od.model.OdFileEntity;
import com.viettel.gnoc.wfm.dto.ConfigPropertyDTO;
import com.viettel.gnoc.wfm.dto.WoTypeTimeDTO;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

/**
 * @author NamTN
 */
@Repository
public interface OdRepository {

  @SuppressWarnings("unchecked")
  Datatable getListDataSearch(OdSearchInsideDTO odSearchInsideDTO);

  List<OdSearchInsideDTO> getListDataExport(OdSearchInsideDTO odSearchInsideDTO);

  OdDTO findOdById(Long odTypeId);

  OdDTO getDetailOdDTOById(Long odId);

  ResultInSideDto delete(Long odTypeId);

  ResultInSideDto deleteListOd(List<OdDTO> odDTOList);

  List<String> getSeqOd(String sequense, int size);

  ResultInSideDto add(OdDTO odDTO);

  ResultInSideDto edit(OdDTO odDTO);

  UsersEntity getUserByUserId(Long userId);

  UsersEntity getUserByUserName(String userName);

  Map<String, String> getConfigProperty() throws Exception;

  List<UsersEntity> getListUserOfUnit(Long unitId);

  ResultInSideDto insertOrUpdate(OdDTO odDTO);

  Datatable getListStatusNext(Long odId, String userId, String unitId);

  List<WoTypeTimeDTO> getListWoTypeTimeDtosByWoTypeId(Long odTypeId);

  String getConfigPropertyValue(String key);

  List<UsersEntity> getManagerReceiverUnit(Long unitId);

  ConfigPropertyDTO getConfigPropertyOd(String key);

  List<OdFileEntity> getListOdFileByOdId(Long odId);

  ResultInSideDto insertOdFile(OdFileDTO odFileDTO);

  ResultInSideDto deleteOdFile(Long odFileId);

//  OdDTO findOdByOdCode(String odCode);

  ResultInSideDto updateOdOtherSystem(OdDTO odDTO);

  OdHistoryDTO getListOdHistory(Long odId);

  ResultInSideDto insertApprovalPause(OdPendingDTO odPendingDTO);
}
