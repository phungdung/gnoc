package com.viettel.gnoc.mr.service;

import com.viettel.gnoc.maintenance.dto.CPChecklistFileItemWP;
import com.viettel.gnoc.maintenance.dto.MrDeviceBtsDTO;
import com.viettel.gnoc.maintenance.dto.MrScheduleBtsHisDetailDTO;
import com.viettel.gnoc.maintenance.dto.MrScheduleBtsHisFileDTO;
import com.viettel.gnoc.maintenance.dto.Result;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * @author TrungDuong
 */

@WebService(serviceName = "MrDeviceBtsService")
public interface MrDeviceBtsService {

  @WebMethod(operationName = "updateWOChecklistFiles")
  public Result updateWOChecklistFiles(@WebParam(name = "woCode") String woCode,
      @WebParam(name = "file_item") List<CPChecklistFileItemWP> fileItems);

  @WebMethod(operationName = "getListFileByCheckListWo")
  public List<MrScheduleBtsHisFileDTO> getListFileByCheckListWo(
      @WebParam(name = "checklistId") String checklistId, @WebParam(name = "woId") String woId);

  @WebMethod(operationName = "getCheckListByWoId")
  public List<MrScheduleBtsHisDetailDTO> getCheckListByWoId(
      @WebParam(name = "woCode") String woCode);

  @WebMethod(operationName = "deleteFileImageById")
  public String deleteFileImageById(
      @WebParam(name = "mrScheduleBtsHisFileDTO") List<MrScheduleBtsHisFileDTO> mrScheduleBtsHisFileDTO);

  @WebMethod(operationName = "updateStatusTask")
  public List<String> updateStatusTask(
      @WebParam(name = "list_taskDetail") List<MrScheduleBtsHisDetailDTO> mrScheduleBtsHisDetailDTO);

  @WebMethod(operationName = "updateStatusAfterMaintenance")
  public String updateStatusAfterMaintenance(@WebParam(name = "woId") String woId
      , @WebParam(name = "lastMaintenanceTime") String lastMaintenanceTime
      , @WebParam(name = "status") String status);

  @WebMethod(operationName = "getMrBTSDeviceInfor")
  public List<MrDeviceBtsDTO> getMrBTSDeviceInfor(@WebParam(name = "woCode") String woCode);

}
