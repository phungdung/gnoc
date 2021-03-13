package com.viettel.gnoc.wo.business;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.wo.dto.ObjKeyValue;
import com.viettel.gnoc.wo.dto.WoPostInspectionDTO;
import com.viettel.gnoc.wo.dto.WoPostInspectionInsideDTO;
import java.util.List;

public interface WoPostInspectionBusiness {


  String insertWOPostInspectionFromVsmart(List<WoPostInspectionDTO> lstInspectionDTO,
      List<ObjKeyValue> lstObjKeyValue);

  String insertWOPostInspection(List<WoPostInspectionDTO> lstInspectionDTO);

  List<WoPostInspectionInsideDTO> getListWOPostInspection(
      WoPostInspectionInsideDTO woPostInspectionDTO,
      int rowStart, int maxRow, String sortType, String sortFieldList);

  List<ObjKeyValue> loadWoPostInspectionChecklist(String woId, String accountName);

  ResultInSideDto delete(Long id);
}
