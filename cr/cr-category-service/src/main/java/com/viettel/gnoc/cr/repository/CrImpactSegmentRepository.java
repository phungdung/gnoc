package com.viettel.gnoc.cr.repository;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.cr.dto.ImpactSegmentDTO;
import java.util.List;

/**
 * @author DungPV
 */
public interface CrImpactSegmentRepository {

  BaseDto sqlSearch(ImpactSegmentDTO impactSegmentDTO);

  Datatable getListImpactSegment(ImpactSegmentDTO impactSegmentDTO);

  ResultInSideDto addOrEditImpactSegment(ImpactSegmentDTO impactSegmentDTO);

  ResultInSideDto deleteImpactSegment(Long crImpactSegmentId);

  List<ImpactSegmentDTO> getListDataExport(ImpactSegmentDTO impactSegmentDTO);

  ImpactSegmentDTO getDetail(Long crId);

  List<ImpactSegmentDTO> getListImpactSegmentDTO(ImpactSegmentDTO impactSegmentDTO);

  ImpactSegmentDTO findImpactSegmentBy(ImpactSegmentDTO impactSegmentDTO);
}
