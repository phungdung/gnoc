package com.viettel.gnoc.cr.repository;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.cr.dto.CrAffectedLevelDTO;
import java.util.List;

/**
 * @author DungPV
 */
public interface CrAffectedLevelRepository {

  BaseDto sqlSearch(CrAffectedLevelDTO crAffectedLevelDTO);

  Datatable getListCrAffectedLevel(CrAffectedLevelDTO crAffectedLevelDTO);

  ResultInSideDto addOrUpdateCrAffectedLevel(CrAffectedLevelDTO crAffectedLevelDTO);

  ResultInSideDto deleteCrAffectedLevel(Long id);

  CrAffectedLevelDTO getDetail(Long crId);

  List<CrAffectedLevelDTO> getListDataExport(CrAffectedLevelDTO crAffectedLevelDTO);
}
