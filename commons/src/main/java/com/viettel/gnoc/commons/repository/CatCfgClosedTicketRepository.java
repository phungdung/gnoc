package com.viettel.gnoc.commons.repository;

import com.viettel.gnoc.commons.dto.CatCfgClosedTicketDTO;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 * @author TienNV
 */
@Repository
public interface CatCfgClosedTicketRepository {

  List<CatCfgClosedTicketDTO> getListCatCfgClosedTicketDTO(
      CatCfgClosedTicketDTO catCfgClosedTicketDTO, int start, int maxResult, String sortType,
      String sortField);

  Datatable getListCatCfgClosedTicket(CatCfgClosedTicketDTO dto);

  List<CatItemDTO> getListSubCategory(Long typeId);

  CatCfgClosedTicketDTO getDetail(Long id);

  ResultInSideDto insert(CatCfgClosedTicketDTO catCfgClosedTicketDTO);

  ResultInSideDto edit(CatCfgClosedTicketDTO catCfgClosedTicketDTO);

  ResultInSideDto delete(Long id);

  CatCfgClosedTicketDTO checkExist(CatCfgClosedTicketDTO dto);
}
