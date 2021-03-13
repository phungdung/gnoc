package com.viettel.gnoc.incident.business;

import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.incident.dto.InfraCableLaneDTO;
import com.viettel.gnoc.incident.dto.LinkInfoDTO;
import com.viettel.gnoc.incident.dto.TroublesInSideDTO;
import java.util.List;

public interface TransmissionInfoBusiness {

  List<CatItemDTO> getListCatReason(String itemId);

  //loai mang
  List<CatItemDTO> getListCableType(String lineCutCode, String codeSnippetOff);

  //ma doan dut
  List<CatItemDTO> getListSnippetOff(String lineCutCode);

  List<LinkInfoDTO> getListLinkInfoDTO(String codeSnippetOff);

  //search ma dut tuyen
  Datatable onSearchInfraCableLaneDTO(InfraCableLaneDTO infraCableLaneDTO);

  //search mang xong thay the
  Datatable onSearchInfraSleevesDTO(String nameCode, String codeSnippetOff,
      TroublesInSideDTO troublesDTO);
}
