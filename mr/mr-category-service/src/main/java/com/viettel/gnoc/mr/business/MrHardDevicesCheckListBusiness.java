package com.viettel.gnoc.mr.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.maintenance.dto.MrHardDevicesCheckListDTO;
import java.io.File;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;


public interface MrHardDevicesCheckListBusiness {

  Datatable onSearch(MrHardDevicesCheckListDTO mrCheckListDTO);

  ResultInSideDto insert(MrHardDevicesCheckListDTO mrCheckListDTO);

  ResultInSideDto update(MrHardDevicesCheckListDTO mrCheckListDTO);

  ResultInSideDto deleteMrCheckList(Long checkListId);

  MrHardDevicesCheckListDTO getDetail(Long checkListId);

  List<MrHardDevicesCheckListDTO> getListArrayDeviceTypeNetworkType();

  File exportSearchData(MrHardDevicesCheckListDTO mrCheckListDTO) throws Exception;

  File getFileTemplate() throws Exception;

  ResultInSideDto importMrCheckList(MultipartFile fileImport) throws Exception;

}
