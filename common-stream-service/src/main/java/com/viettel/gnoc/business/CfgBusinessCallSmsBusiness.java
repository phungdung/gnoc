package com.viettel.gnoc.business;

import com.viettel.gnoc.commons.dto.CfgBusinessCallSmsDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.wo.dto.WoCdGroupInsideDTO;
import java.io.File;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface CfgBusinessCallSmsBusiness {

  Datatable getListCfgBusinessCallSmsDTO(CfgBusinessCallSmsDTO cfgBusinessCallSmsDTO);

  List<WoCdGroupInsideDTO> getListWoCdGroupCBB();

  ResultInSideDto insert(CfgBusinessCallSmsDTO cfgBusinessCallSmsDTO);

  ResultInSideDto update(CfgBusinessCallSmsDTO cfgBusinessCallSmsDTO);

  CfgBusinessCallSmsDTO getDetail(Long id);

  ResultInSideDto delete(Long id);

  File exportData(CfgBusinessCallSmsDTO cfgBusinessCallSmsDTO) throws Exception;

  File getTemplate() throws Exception;

  ResultInSideDto importData(MultipartFile uploadFile) throws Exception;
}
