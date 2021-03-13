package com.viettel.gnoc.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.MapFlowTemplatesDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.ConditionBean;
import java.util.List;

public interface MapFlowTemplatesBusiness {

  ResultInSideDto updateMapFlowTemplates(MapFlowTemplatesDTO mapFlowTemplatesDTO);

  ResultInSideDto deleteMapFlowTemplates(Long id);

  ResultInSideDto deleteListMapFlowTemplates(List<MapFlowTemplatesDTO> mapFlowTemplatesListDTO);

  MapFlowTemplatesDTO findMapFlowTemplatesById(Long id);

  ResultInSideDto insertMapFlowTemplates(MapFlowTemplatesDTO mapFlowTemplatesDTO);

  ResultInSideDto insertOrUpdateListMapFlowTemplates(List<MapFlowTemplatesDTO> mapFlowTemplatesDTO);

  List<String> getSequenseMapFlowTemplates(String seqName, int... size);

  List<MapFlowTemplatesDTO> getListMapFlowTemplatesByCondition(List<ConditionBean> lstCondition,
      int rowStart, int maxRow, String sortType, String sortFieldList);

  Datatable getListMapFlowTemplatesDTO(MapFlowTemplatesDTO mapFlowTemplatesDTO);

  List<MapFlowTemplatesDTO> getListMapFlowTemplates(MapFlowTemplatesDTO mapFlowTemplatesDTO,
      int rowStart, int maxRow, String sortType, String sortFieldList);
}
