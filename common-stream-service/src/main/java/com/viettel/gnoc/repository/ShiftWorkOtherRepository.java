package com.viettel.gnoc.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.cr.dto.ShiftWorkOtherDTO;

public interface ShiftWorkOtherRepository {


  ResultInSideDto deleteShiftWorkOther(Long id);

  ResultInSideDto updateShiftWorkOther(ShiftWorkOtherDTO dto);

  Datatable getListShiftWorkOther(ShiftWorkOtherDTO dto);

  ShiftWorkOtherDTO findShiftWorkOtherById(Long id);

  ResultInSideDto insertShiftWorkOther(ShiftWorkOtherDTO shiftWorkOtherDTO);
}
