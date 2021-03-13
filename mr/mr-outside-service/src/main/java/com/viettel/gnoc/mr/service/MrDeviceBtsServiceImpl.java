package com.viettel.gnoc.mr.service;

import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.maintenance.dto.CPChecklistFileItemWP;
import com.viettel.gnoc.maintenance.dto.MrDeviceBtsDTO;
import com.viettel.gnoc.maintenance.dto.MrScheduleBtsHisDetailDTO;
import com.viettel.gnoc.maintenance.dto.MrScheduleBtsHisFileDTO;
import com.viettel.gnoc.maintenance.dto.Result;
import com.viettel.gnoc.mr.business.MrDeviceBtsBusiness;
import com.viettel.gnoc.mr.business.MrScheduleBtsHisFileBusiness;
import java.util.List;
import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author TrungDuong
 */
@Service
@Slf4j
public class MrDeviceBtsServiceImpl implements MrDeviceBtsService {

  @Autowired
  MrDeviceBtsBusiness mrDeviceBtsBusiness;

  @Autowired
  MrScheduleBtsHisFileBusiness mrScheduleBtsHisFileBusiness;

  @Resource
  private WebServiceContext wsContext;

  @Override
  public Result updateWOChecklistFiles(String woCode,
      List<CPChecklistFileItemWP> fileItems) {
    I18n.setLocaleForService(wsContext);
    return mrDeviceBtsBusiness.updateWOChecklistFiles(woCode, fileItems);
  }

  @Override
  public List<MrScheduleBtsHisFileDTO> getListFileByCheckListWo(String checklistId, String woId) {
    I18n.setLocaleForService(wsContext);
    return mrDeviceBtsBusiness.getListFileByCheckListWo(checklistId, woId);
  }

  @Override
  public List<MrScheduleBtsHisDetailDTO> getCheckListByWoId(String woCode) {
    I18n.setLocaleForService(wsContext);
    return mrDeviceBtsBusiness.getListWoBts(woCode);
  }

  @Override
  public String deleteFileImageById(List<MrScheduleBtsHisFileDTO> mrScheduleBtsHisFileDTO) {
    I18n.setLocaleForService(wsContext);
    return mrScheduleBtsHisFileBusiness.delete(mrScheduleBtsHisFileDTO);
  }

  @Override
  public List<String> updateStatusTask(List<MrScheduleBtsHisDetailDTO> mrScheduleBtsHisDetailDTO) {
    I18n.setLocaleForService(wsContext);
    return mrDeviceBtsBusiness.updateStatusTask(mrScheduleBtsHisDetailDTO);
  }

  @Override
  public String updateStatusAfterMaintenance(String woCode, String lastMaintenanceTime,
      String status) {
    I18n.setLocaleForService(wsContext);
    return mrDeviceBtsBusiness.updateStatusAfterMaintenance(woCode, lastMaintenanceTime, status);
  }

  @Override
  public List<MrDeviceBtsDTO> getMrBTSDeviceInfor(String woCode) {
    I18n.setLocaleForService(wsContext);
    return mrDeviceBtsBusiness.getMrBTSDeviceInfor(woCode);
  }
}
