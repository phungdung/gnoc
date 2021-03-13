package com.viettel.gnoc.sr.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.sr.dto.SRChildAutoDTO;
import com.viettel.gnoc.sr.dto.SrInsiteDTO;
import java.util.List;

public interface SRChildAutoRepository {

  ResultInSideDto insertOrUpdateSRChildAuto(SRChildAutoDTO srChildAutoDTO);

  List<SRChildAutoDTO> getListSRChildCheckClosed(String srCode);

  List<SrInsiteDTO> getListSRChildAutoByGennerateNo(String srParentCode, Long generateNo);

  SRChildAutoDTO getDetailSRChildAuto(Long id);
}
