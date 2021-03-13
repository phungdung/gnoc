package com.viettel.gnoc.mr.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.maintenance.dto.MrConfigTestXaDTO;
import java.io.File;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface MrConfigTestXaBusiness {

  Datatable getListMrConfigTestXa(MrConfigTestXaDTO mrConfigTestXaDTO);

  ResultInSideDto insert(MrConfigTestXaDTO mrConfigTestXaDTO);

  ResultInSideDto update(MrConfigTestXaDTO mrConfigTestXaDTO);

  ResultInSideDto deleteMrConfigTestXa(Long configId);

  MrConfigTestXaDTO getDetail(Long configId);

  List<MrConfigTestXaDTO> getListStation();

  File exportData(MrConfigTestXaDTO mrConfigTestXaDTO) throws Exception;

  File getTemplate() throws Exception;

  ResultInSideDto importData(MultipartFile uploadFile) throws Exception;
}
