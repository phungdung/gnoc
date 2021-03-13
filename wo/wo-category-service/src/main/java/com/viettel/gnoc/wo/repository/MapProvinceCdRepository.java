package com.viettel.gnoc.wo.repository;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.wo.dto.MapProvinceCdDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface MapProvinceCdRepository {


  ResultInSideDto add(MapProvinceCdDTO mapProvinceCdDTO);

  ResultInSideDto insertOrUpdateListImport(List<MapProvinceCdDTO> list);

  ResultInSideDto edit(MapProvinceCdDTO mapProvinceCdDTO);

  ResultInSideDto deleteMapProvinceCd(Long id);

  Datatable getListDTOSearchWeb(MapProvinceCdDTO mapProvinceCdDTO);

  MapProvinceCdDTO checkMapProvinceCdExist(String locationCode);

  List<MapProvinceCdDTO> getListDTOSearchWebExport(MapProvinceCdDTO mapProvinceCdDTO);

  MapProvinceCdDTO getDetail(Long id);
}
