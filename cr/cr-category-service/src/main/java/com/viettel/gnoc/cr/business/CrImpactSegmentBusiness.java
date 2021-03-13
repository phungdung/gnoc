package com.viettel.gnoc.cr.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.cr.dto.ImpactSegmentDTO;
import java.io.File;

/**
 * @author DungPV
 */
public interface CrImpactSegmentBusiness {

  Datatable getListCrImpactSegment(ImpactSegmentDTO impactSegmentDTO);

  ResultInSideDto addCrImpactSegment(ImpactSegmentDTO impactSegmentDTO);

  ResultInSideDto updateCrImpactSegment(ImpactSegmentDTO impactSegmentDTO);

  ResultInSideDto deleteCrImpactSegment(Long impactSegmentId);

  ResultInSideDto deleteListCrImpactSegment(ImpactSegmentDTO impactSegmentDTO);

  ImpactSegmentDTO getDetail(Long impactSegmentId);

  File exportData(ImpactSegmentDTO impactSegmentDTO) throws Exception;
}
