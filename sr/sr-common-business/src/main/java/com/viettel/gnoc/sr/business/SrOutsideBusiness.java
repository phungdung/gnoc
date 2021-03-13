package com.viettel.gnoc.sr.business;

import com.viettel.gnoc.commons.dto.GnocFileDto;
import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.UsersDTO;
import com.viettel.gnoc.sr.dto.SRCatalogDTO;
import com.viettel.gnoc.sr.dto.SRConfigDTO;
import com.viettel.gnoc.sr.dto.SRCreatedFromOtherSysDTO;
import com.viettel.gnoc.sr.dto.SRDTO;
import com.viettel.gnoc.sr.dto.SRWorkLogDTO;
import com.viettel.gnoc.sr.dto.SrInsiteDTO;
import java.util.List;

public interface SrOutsideBusiness {

  //He thong ngoai VIPA
  ResultDTO putResultFromVipa(String srId, String result, String fileContentError);

  //He thong ngoai QLCTKT or CM
  ResultDTO createSRFromOtherSys(SRCreatedFromOtherSysDTO srCreatedFromOtherSysDTO);

  //He thong ngoai Add-On or KHDN
  ResultDTO createSRByConfigGroup(SRDTO srInputDTO, String configGroup);

  List<SRDTO> getListSRByConfigGroup(SRDTO dto, String configGroup);

  List<SRCatalogDTO> getListSRCatalogByConfigGroup(String configGroup);

  UsersDTO checkUserByUserCodeOrName(String userCode, String configGroup);

  List<SRDTO> getListSRForLinkCR(String loginUser, String srCode);

  List<SRDTO> getListSR(SRDTO dto, int rowStart, int maxRow);

  String deleteSRForOutside(Long srId);

  List<SRDTO> getCrNumberCreatedFromSR(SRDTO dto, int rowStart, int maxRow);

  SRDTO getDetailSR(String srId, Long userId);

  SrInsiteDTO getDetailSRById (String srId);

  ResultDTO updateSR(SRDTO srDTO);

  ResultDTO insertSRWorklog(SRWorkLogDTO srWorklogDTO);

  List<GnocFileDto> getListGnocFileForSR(GnocFileDto gnocFileDto);

  ResultDTO updateSRForIBPMSOutSide(SRDTO srDTO);

  List<SRConfigDTO> getByConfigGroup(String configGroup);

  List<SRCatalogDTO> getListSRCatalogByConfigGroupIBPMS(String configGroup);

}
