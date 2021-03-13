package com.viettel.gnoc.mr.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.maintenance.dto.MrCheckListDTO;
import java.io.File;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;


public interface MrCheckListBusiness {

  Datatable onSearch(MrCheckListDTO mrCheckListDTO);

  ResultInSideDto insert(MrCheckListDTO mrCheckListDTO);

  ResultInSideDto update(MrCheckListDTO mrCheckListDTO);

  ResultInSideDto deleteMrCheckList(Long checkListId);

  MrCheckListDTO getDetail(Long checkListId);

  List<MrCheckListDTO> getListArrayDeviceTypeNetworkType();

  File exportSearchData(MrCheckListDTO mrCheckListDTO) throws Exception;

  File getFileTemplate() throws Exception;

  ResultInSideDto importMrCheckList(MultipartFile fileImport) throws Exception;

}
