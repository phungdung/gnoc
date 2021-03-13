package com.viettel.gnoc.repository;

import com.viettel.bccs.cc.service.ProblemGroupDTO;
import com.viettel.bccs.cc.service.ProblemTypeDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.MapProbToKedbDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.kedb.dto.KedbDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface MapProbToKedbRepository {

  Datatable getListMapProbToKedbDTO(MapProbToKedbDTO mapProbToKedbDTO);

  Datatable getListKedbDTO(KedbDTO kedbDTO);

  List<ProblemGroupDTO> getListGroup();

  List<ProblemGroupDTO> getListKind(Long probGroupId);

  List<ProblemTypeDTO> getListType(Long probGroupId);

//  List<CfgServerNocDTO> getListServer();

  ResultInSideDto insertMapProbToKedb(MapProbToKedbDTO mapProbToKedbDTO);

  ResultInSideDto updateMapProbToKedb(MapProbToKedbDTO mapProbToKedbDTO);

  ResultInSideDto deleteMapProbToKedb(Long id);

  MapProbToKedbDTO getDetail(Long id);

  List<MapProbToKedbDTO> getDataExport(MapProbToKedbDTO dto);

//  MapProbToKedbDTO checkExist(Long groupId, Long kindId, Long typeId);
}
