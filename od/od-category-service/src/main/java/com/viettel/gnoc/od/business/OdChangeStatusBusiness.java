package com.viettel.gnoc.od.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.od.dto.OdChangeStatusDTO;
import java.io.File;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author TienNV
 */
public interface OdChangeStatusBusiness {

  String getSeqOdChangeStatus();

  Datatable getListOdCfgBusiness(OdChangeStatusDTO odChangeStatusDTO);

  ResultInSideDto deleteCfg(Long odChangeStatusId);

  ResultInSideDto insertOrUpdateCfg(OdChangeStatusDTO odChangeStatusDTO) throws Exception;

  ResultInSideDto addList(List<OdChangeStatusDTO> odChangeStatusDTOs) throws Exception;

  ResultInSideDto deleteListCfg(List<Long> odChangeStatusIds);

  String delete(List<OdChangeStatusDTO> odChangeStatusDTOs);

  String delete(Long id);

  int deleteListOdChangeStatus(List<Long> ids);

  ResultInSideDto importData(MultipartFile uploadfile);

  OdChangeStatusDTO getDetailCfg(Long id);

  File exportData(OdChangeStatusDTO odChangeStatusDTO) throws Exception;

  List<OdChangeStatusDTO> search(OdChangeStatusDTO odChangeStatusDTO);

  public OdChangeStatusDTO getOdChangeStatusDTOByParams(String... params);
}
