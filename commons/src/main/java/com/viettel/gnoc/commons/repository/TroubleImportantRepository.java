package com.viettel.gnoc.commons.repository;

import com.viettel.gnoc.commons.dto.*;
import com.viettel.gnoc.commons.model.UsersEntity;
import com.viettel.gnoc.wfm.dto.ConfigPropertyDTO;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author hungtv77
 */
@Repository
public interface TroubleImportantRepository {

  Datatable searchListTroubleImportant(TroubleImportantDTO dto);

  ResultInSideDto insert(TroubleImportantDTO dto);

  ResultInSideDto update(TroubleImportantDTO dto);

  TroubleImportantDTO getDetailTrouble(Long id);

  ResultInSideDto delete(Long id);
}
