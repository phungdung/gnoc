package com.viettel.gnoc.commons.business;

import com.viettel.gnoc.commons.dto.*;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * @author hungtv77
 */
@Service
public interface TroubleImportantBusiness {

  Datatable searchListTroubleImportant(TroubleImportantDTO dto);

  ResultInSideDto insert(TroubleImportantDTO dto);

  ResultInSideDto update(TroubleImportantDTO dto);

  TroubleImportantDTO getDetailTrouble(Long id);

  ResultInSideDto delete(Long id);

  File exportData(TroubleImportantDTO troubleImportantDTO) throws Exception;
}
