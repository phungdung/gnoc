package com.viettel.gnoc.incident.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.incident.dto.TTChangeStatusDTO;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface TTChangeStatusBusiness {

  Datatable getListTTChangeStatus(TTChangeStatusDTO ttChangeStatusDTO);

  TTChangeStatusDTO getDetailCfg(TTChangeStatusDTO ttChangeStatusDTO);

  ResultInSideDto deleteTTChangeStatus(Long id);

  ResultInSideDto insertOrUpdateTTChangeStatus(TTChangeStatusDTO ttChangeStatusDTO);

  TTChangeStatusDTO findTTChangeStatusById(Long id);
}
