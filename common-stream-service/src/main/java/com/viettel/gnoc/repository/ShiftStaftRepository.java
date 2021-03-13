package com.viettel.gnoc.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.cr.dto.ShiftStaftDTO;
import java.util.List;

public interface ShiftStaftRepository {


  ResultInSideDto deleteShiftUser(Long id);

  ResultInSideDto updateShiftUser(ShiftStaftDTO dto);

  Datatable getListShiftUser(ShiftStaftDTO shiftStaftDTO);

  ShiftStaftDTO findShiftUserById(Long id);

  ResultInSideDto insertShiftUser(ShiftStaftDTO shiftStaftDTO);

  List<ShiftStaftDTO> getListShiftStaftById(Long id);
}
