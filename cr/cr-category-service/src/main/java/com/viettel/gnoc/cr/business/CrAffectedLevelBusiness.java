package com.viettel.gnoc.cr.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.cr.dto.CrAffectedLevelDTO;
import java.io.File;

/**
 * @author DungPV
 */
public interface CrAffectedLevelBusiness {

  Datatable getListCrAffectedLevel(CrAffectedLevelDTO crAffectedLevelDTO);

  ResultInSideDto addCrAffectedLevel(CrAffectedLevelDTO crAffectedLevelDTO);

  ResultInSideDto updateCrAffectedLevel(CrAffectedLevelDTO crAffectedLevelDTO);

  ResultInSideDto deleteCrAffectedLevel(Long affectedLevelId);

  ResultInSideDto deleteListCrAffectedLevel(CrAffectedLevelDTO crAffectedLevelDTO);

  CrAffectedLevelDTO getDetail(Long affectedLevelId);

  File exportData(CrAffectedLevelDTO crAffectedLevelDTO) throws Exception;
}
