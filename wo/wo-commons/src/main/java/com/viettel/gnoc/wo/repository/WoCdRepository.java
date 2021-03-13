package com.viettel.gnoc.wo.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.wo.dto.WoCdDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface WoCdRepository {

  List<WoCdDTO> getListWoCdDTO(WoCdDTO woCdDTO);

  ResultInSideDto insertWoCd(WoCdDTO woCdDTO);

  ResultInSideDto updateWoCd(WoCdDTO woCdDTO);

  ResultInSideDto deleteWoCd(Long cdId);

  ResultInSideDto deleteWoCdByWoGroupId(Long woGroupId);

  List<WoCdDTO> getListWoCdExport(WoCdDTO woCdDTO);

  WoCdDTO checkWoCdExitBy2Id(Long woGroupId, Long userId);

  List<UsersInsideDto> getListCdByGroup(Long woGroupId);
}
