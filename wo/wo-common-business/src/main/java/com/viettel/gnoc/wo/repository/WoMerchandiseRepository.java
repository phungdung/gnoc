package com.viettel.gnoc.wo.repository;

import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.wo.dto.WoMerchandiseInsideDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface WoMerchandiseRepository {

  ResultInSideDto insertWoMerchandise(WoMerchandiseInsideDTO woMerchandiseInsideDTO);

  ResultInSideDto updateListWoMerchandise(List<WoMerchandiseInsideDTO> listMerDTO);

  List<WoMerchandiseInsideDTO> getListWoMerchandiseDTO(
      WoMerchandiseInsideDTO woMerchandiseInsideDTO);

  List<WoMerchandiseInsideDTO> getListWoMerchandise(Long woId);
}
