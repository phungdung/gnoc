package com.viettel.gnoc.commons.business;

import com.viettel.gnoc.commons.dto.CatCfgClosedTicketDTO;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import java.io.File;
import java.util.List;

public interface CatCfgClosedTicketBusiness {

  List<CatCfgClosedTicketDTO> getListCatCfgClosedTicketDTO(
      CatCfgClosedTicketDTO catCfgClosedTicketDTO, int start, int maxResult, String sortType,
      String sortField);

  Datatable getListCatCfgClosedTicket(CatCfgClosedTicketDTO dto);

  List<CatItemDTO> getListSubCategory(Long typeId);

  CatCfgClosedTicketDTO getDetail(Long id);

  ResultInSideDto insert(CatCfgClosedTicketDTO catCfgClosedTicketDTO);

  ResultInSideDto update(CatCfgClosedTicketDTO catCfgClosedTicketDTO);

  File exportCatCfgClosedTicket(CatCfgClosedTicketDTO catCfgClosedTicketDTO) throws Exception;

  ResultInSideDto delete(Long id);
}
