package com.viettel.gnoc.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.cr.dto.ShiftItSeriousDTO;

public interface ShiftItSeriousRepository {

  ResultInSideDto deleteShiftItSerious(Long id);

  ResultInSideDto updateShiftItSerious(ShiftItSeriousDTO dto);

  Datatable getListShiftItSerious(ShiftItSeriousDTO dto);

  ShiftItSeriousDTO findShiftItSeriousById(Long id);

  ResultInSideDto insertShiftItSerious(ShiftItSeriousDTO shiftItSeriousDTO);
}
