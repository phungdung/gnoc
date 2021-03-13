package com.viettel.gnoc.mr.service;


import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.cr.dto.WorkLogCategoryDTO;
import com.viettel.gnoc.maintenance.dto.WorkLogResultDTO;
import com.viettel.gnoc.wo.dto.WorkLogDTO;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * @author Dunglv3
 * @version 2.0
 * @since 12/06/2020 09:31:00
 */
@WebService(serviceName = "WorkLogService")
public interface WorkLogService {

  @WebMethod(operationName = "insertWorkLog")
  public ResultDTO insertWorkLog(@WebParam(name = "workLogDTO") WorkLogDTO workLogDTO);

  @WebMethod(operationName = "getListWorkLogCategoryDTO")
  public List<WorkLogCategoryDTO> getListWorkLogCategoryDTO(
      @WebParam(name = "workLogCategoryDTO") WorkLogCategoryDTO workLogCategoryDTO);

  @WebMethod(operationName = "getListWorklogSearch")
  public List<WorkLogResultDTO> getListWorklogSearch(@WebParam(name = "workLogDTO") WorkLogDTO dto);
}

