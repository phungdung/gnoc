package com.viettel.gnoc.wo.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.wo.dto.MaterialThresInsideDTO;
import com.viettel.gnoc.wo.dto.WoMaterialDTO;
import java.io.File;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface WoMaterialBusiness {

  Datatable getListWoMaterialPage(MaterialThresInsideDTO materialThresDTO);

  ResultInSideDto delete(Long materialThresId);

  Datatable getListDataExport(MaterialThresInsideDTO materialThresDTO);

  File getMaterialTemplate() throws Exception;

  File exportData(MaterialThresInsideDTO materialThresDTO) throws Exception;

  ResultInSideDto importData(MultipartFile uploadfile) throws Exception;

  ResultInSideDto insert(MaterialThresInsideDTO materialThresDTO);

  ResultInSideDto update(MaterialThresInsideDTO materialThresDTO);

  MaterialThresInsideDTO findByMaterialThresId(Long materialThresId);

  List<WoMaterialDTO> findAllMaterial(String materialName);

  WoMaterialDTO findWoMaterialById(Long materialId);

}
