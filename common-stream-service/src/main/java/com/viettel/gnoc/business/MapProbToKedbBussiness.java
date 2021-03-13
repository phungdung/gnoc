package com.viettel.gnoc.business;

//import com.viettel.bccs.cc.service.ProblemGroupDTO;
//import com.viettel.bccs.cc.service.ProblemTypeDTO;

import com.viettel.bccs.cc.service.ProblemGroupDTO;
import com.viettel.bccs.cc.service.ProblemTypeDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.MapProbToKedbDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.kedb.dto.KedbDTO;
import java.io.File;
import java.util.List;

public interface MapProbToKedbBussiness {

  //Lay danh sach bai hoc kinh nghiem
  Datatable getListKedbDTO(KedbDTO kedbDTO);

  //Search
  Datatable getListMapProbToKedbDTO(MapProbToKedbDTO mapProbToKedbDTO);

  List<ProblemGroupDTO> getListGroup();

  List<ProblemGroupDTO> getListKind(Long probGroupId);

  List<ProblemTypeDTO> getListType(Long probGroupId);

  ResultInSideDto insertMapProbToKedb(MapProbToKedbDTO mapProbToKedbDTO);

  ResultInSideDto updateMapProbToKedb(MapProbToKedbDTO mapProbToKedbDTO);

  ResultInSideDto deleteMapProbToKedb(Long id);

  MapProbToKedbDTO getDetail(Long id);

  File exportData(MapProbToKedbDTO mapProbToKedbDTO) throws Exception;

//  ResultInSideDto importData(MultipartFile uploadFile) throws Exception;
//
//  File getTemplate() throws Exception;

}
