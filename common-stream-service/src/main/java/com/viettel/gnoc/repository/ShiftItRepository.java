package com.viettel.gnoc.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.cr.dto.ShiftItDTO;
import java.util.List;

public interface ShiftItRepository {

  ResultInSideDto deleteShiftIt(Long id);

  ResultInSideDto updateShiftIt(ShiftItDTO dto);

  Datatable getListShiftIt(ShiftItDTO dto);

  ShiftItDTO findShiftItById(Long id);

  ResultInSideDto insertShiftIt(ShiftItDTO shiftItDTO);

  List<ShiftItDTO> getListShiftItByShiftID(ShiftItDTO dto);
}
