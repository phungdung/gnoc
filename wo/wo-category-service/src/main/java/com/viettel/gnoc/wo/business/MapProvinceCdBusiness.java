package com.viettel.gnoc.wo.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.wo.dto.MapProvinceCdDTO;
import java.io.File;
import org.springframework.web.multipart.MultipartFile;

public interface MapProvinceCdBusiness {


  ResultInSideDto deleteMapProvinceCd(Long id);

  ResultInSideDto add(MapProvinceCdDTO mapProvinceCdDTO);

  ResultInSideDto edit(MapProvinceCdDTO mapProvinceCdDTO);

  MapProvinceCdDTO getDetail(Long id);

  Datatable getListDTOSearchWeb(MapProvinceCdDTO mapProvinceCdDTO);

  File exportData(MapProvinceCdDTO mapProvinceCdDTO) throws Exception;

  File getTemplate() throws Exception;

  // API import Excel
  ResultInSideDto importData(MultipartFile uploadfile);
}
