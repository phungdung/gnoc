package com.viettel.gnoc.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.cr.dto.ShiftWorkDTO;
import java.util.List;

public interface ShiftWorkRepository {

  ResultInSideDto deleteShiftWork(Long id);

  ResultInSideDto updateShiftWork(ShiftWorkDTO dto);

  Datatable getListShiftWork(ShiftWorkDTO dto);

  ShiftWorkDTO findShiftWorkById(Long id);

  ResultInSideDto insertShiftWork(ShiftWorkDTO shiftWorkDTO);

  List<ShiftWorkDTO> getListShiftWorkByShiftID(ShiftWorkDTO dto);

}
