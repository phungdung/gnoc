package com.viettel.gnoc.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.cr.dto.ShiftCrDTO;
import java.util.List;

public interface ShiftCrRepository {


  ResultInSideDto deleteShiftCr(Long id);

  ResultInSideDto updateShiftCr(ShiftCrDTO dto);

  Datatable getListShiftCr(ShiftCrDTO dto);

  ShiftCrDTO findShiftCrById(Long id);

  ResultInSideDto insertShiftCr(ShiftCrDTO shiftCrDTO);

  List<ShiftCrDTO> getListShiftCrByShiftID(ShiftCrDTO dto);
}
