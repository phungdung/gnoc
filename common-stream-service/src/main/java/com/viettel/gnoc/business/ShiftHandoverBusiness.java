package com.viettel.gnoc.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.ShiftHandoverDTO;
import com.viettel.gnoc.cr.dto.ShiftCrDTO;
import com.viettel.gnoc.cr.dto.ShiftItDTO;
import com.viettel.gnoc.cr.dto.ShiftWorkDTO;
import java.io.File;
import java.io.IOException;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface ShiftHandoverBusiness {

  ShiftHandoverDTO findShiftHandoverById(Long id);

  Datatable getListShiftHandover(ShiftHandoverDTO shiftHandoverDTO);

  File exportData(ShiftHandoverDTO dto) throws Exception;

  List<ShiftHandoverDTO> getListShiftID();

  ResultInSideDto insertCfgShiftHandover(List<MultipartFile> files,
      ShiftHandoverDTO shiftHandoverDTO) throws IOException;

  ResultInSideDto updateCfgShiftHandover(List<MultipartFile> files,
      ShiftHandoverDTO shiftHandoverDTO) throws IOException;

  ShiftHandoverDTO findListShiftHandOverById(Long id);

  List<ShiftItDTO> countTicketByShift(ShiftHandoverDTO shiftHandoverDTO);

  File exportShiftWorkData(ShiftWorkDTO shiftWorkDTO) throws Exception;

  File exportShiftCrData(ShiftCrDTO shiftCrDTO) throws Exception;

  File exportShiftRow(ShiftHandoverDTO shiftHandoverDTO) throws Exception;

  ResultInSideDto findCrByCrNumber(ShiftCrDTO shiftCrDTO);

  Long getSequenseShiftHandover(String seqName, int i);

  ResultInSideDto importShiftWorkData(MultipartFile file, ShiftWorkDTO shiftWorkDTO);

  File getTemplate() throws IOException;

  ResultInSideDto importShiftCRData(MultipartFile file, ShiftCrDTO shiftCrDTO);

  File getTemplateCR() throws IOException;
}
