package com.viettel.gnoc.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.ShiftHandoverDTO;
import com.viettel.gnoc.cr.dto.ShiftCrDTO;
import com.viettel.gnoc.cr.dto.ShiftHandoverFileDTO;
import com.viettel.gnoc.cr.dto.ShiftWorkDTO;
import com.viettel.gnoc.cr.model.ShiftHandoverFileEntity;
import com.viettel.gnoc.incident.dto.CommonFileDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface ShiftHandoverRepository {

  ShiftHandoverDTO findShiftHandoverById(Long id);

  List<ShiftHandoverDTO> getListShiftHandoverExport(ShiftHandoverDTO shiftHandoverDTO);

  Datatable getListShiftHandover(ShiftHandoverDTO shiftHandoverDTO);

  List<ShiftHandoverDTO> getListShiftID();

  ResultInSideDto insertCfgShiftHandover(ShiftHandoverDTO shiftHandoverDTO);

  ResultInSideDto updateCfgShiftHandover(ShiftHandoverDTO shiftHandoverDTO);

  ShiftHandoverDTO findListShiftHandOverById(Long id);

  List<ShiftWorkDTO> getListShiftWorkByShiftID(ShiftWorkDTO shiftWorkDTO);

  List<ShiftCrDTO> getListShiftCrkByShiftID(ShiftCrDTO shiftCrDTO);

  boolean checkDuplicateRecord(ShiftHandoverDTO shiftHandoverDTO);

  List<String> getSequenseShiftHandover(String seqName, int size);

  ResultInSideDto insertOrUpdateCfgShiftHandover(ShiftHandoverDTO shiftHandoverDTO);

  List<ShiftHandoverFileEntity> findShiftHandOverFile(Long shiftHandoverId);

  ResultInSideDto insertShiftHandOverFile(ShiftHandoverFileDTO shiftHandoverFileDTO);

  ResultInSideDto deleteShiftHandOverFile(ShiftHandoverFileDTO shiftHandoverFileDTO);

  ResultInSideDto insertCommonFile(CommonFileDTO commonFileDTO);

  ResultInSideDto deleteCommonFile(CommonFileDTO commonFileDTO);

  List<ShiftHandoverDTO> getListShiftHandoverNew(ShiftHandoverDTO shiftHandoverDTO);
}
