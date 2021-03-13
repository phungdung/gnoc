package com.viettel.gnoc.wo.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.wo.dto.WoPostInspectionDTO;
import com.viettel.gnoc.wo.dto.WoPostInspectionInsideDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface WoPostInspectionRepository {

  ResultInSideDto add(WoPostInspectionInsideDTO woPostInspectionDTO);

  ResultInSideDto insertListInsideDTO(List<WoPostInspectionInsideDTO> woPostInspectionDTO);

  ResultInSideDto insertList(List<WoPostInspectionDTO> woPostInspectionDTO);

  String getSeqPostInspection(String sequense);

  List<WoPostInspectionInsideDTO> getListExistedWoPostInspection(
      WoPostInspectionInsideDTO woPostInspectionDTO);

  List<WoPostInspectionInsideDTO> onSearch(WoPostInspectionInsideDTO inspectionDTO, int startRow,
      int pageLength);

  ResultInSideDto deleteListWoPostInspection(
      List<WoPostInspectionInsideDTO> woPostInspectionListDTO);

  ResultInSideDto updateWOPostInspection(WoPostInspectionInsideDTO inspectionDTO);

  WoPostInspectionInsideDTO findWoInspectionById(Long woId);

  List<WoPostInspectionInsideDTO> getListWOPostInspection(
      WoPostInspectionInsideDTO woPostInspectionDTO,
      int rowStart,
      int maxRow, String sortType, String sortFieldList);

  ResultInSideDto delete(Long id);
}
