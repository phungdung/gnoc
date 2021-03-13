package com.viettel.gnoc.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.InfraDeviceDTO;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.repository.InfraDeviceRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class InfraDeviceBusinessImpl implements InfraDeviceBusiness {

  @Autowired
  InfraDeviceRepository infraDeviceRepository;

  @Override
  public List<InfraDeviceDTO> getListInfraDeviceIp(InfraDeviceDTO infraDeviceDTO) {
    return infraDeviceRepository.getListInfraDeviceIp(infraDeviceDTO);
  }

  @Override
  public List<InfraDeviceDTO> getListInfraDeviceIpV2(InfraDeviceDTO infraDeviceDTO) {
    return infraDeviceRepository.getListInfraDeviceIpV2(infraDeviceDTO);
  }

  @Override
  public List<InfraDeviceDTO> getListInfraDeviceIpV2SrProxy(InfraDeviceDTO infraDeviceDTO) {
    return infraDeviceRepository.getListInfraDeviceIpV2SrProxy(infraDeviceDTO);
  }

  @Override
  public Datatable getDatatableInfraDeviceIp(InfraDeviceDTO infraDeviceDTO) {
    Datatable datatable = new Datatable();
    List<InfraDeviceDTO> lstData = getListInfraDeviceIp(infraDeviceDTO);
    List<InfraDeviceDTO> lstResult = new ArrayList<>();
    int size = infraDeviceDTO.getPageSize();
    size = (size > 0) ? size : 5;
    if (lstData != null && !lstData.isEmpty()) {
      for (int i = lstData.size() - 1; i >= 0; i--) {
        InfraDeviceDTO dto = lstData.get(i);
        if (!DataUtil.validateIP(dto.getIp())) {
          lstData.remove(i);
        }
      }
      int pageSize = (int) Math.ceil(lstData.size() * 1.0 / size);
      datatable.setTotal(lstData.size());
      datatable.setPages(pageSize);
      lstResult = (List<InfraDeviceDTO>) DataUtil
          .subPageList(lstData, infraDeviceDTO.getPage(), size);
      if (lstResult != null && lstResult.size() > 0) {
        lstResult.get(0).setTotalRow(lstData.size());
      }
    }
    datatable.setData(lstResult);
    return datatable;
  }

  @Override
  public Datatable getDatatableInfraDeviceIpV2(InfraDeviceDTO infraDeviceDTO) {
    Datatable datatable = new Datatable();
    List<InfraDeviceDTO> lstData = getListInfraDeviceIpV2(infraDeviceDTO);
    List<InfraDeviceDTO> lstResult = new ArrayList<>();
    int size = infraDeviceDTO.getPageSize();
    size = (size > 0) ? size : 5;
    if (lstData != null && !lstData.isEmpty()) {
      for (int i = 0; i < lstData.size(); i++) {
        InfraDeviceDTO dto = lstData.get(i);
        if (!DataUtil.validateIP(dto.getIp())) {
          lstData.remove(i--);
        }
        if (i == infraDeviceDTO.getPage() * size) {
          break;
        }
      }
//      lstData.removeIf(c -> (!DataUtil.validateIP(c.getIp())));
//      for (int i = lstData.size() - 1; i >= 0; i--) {
//        InfraDeviceDTO dto = lstData.get(i);
//        if (!DataUtil.validateIP(dto.getIp())) {
//          lstData.remove(i);
//        }
//      }
      int pageSize = (int) Math.ceil(lstData.size() * 1.0 / size);
      datatable.setTotal(lstData.size());
      datatable.setPages(pageSize);
      lstResult = (List<InfraDeviceDTO>) DataUtil
          .subPageList(lstData, infraDeviceDTO.getPage(), size);
      if (lstResult != null && lstResult.size() > 0) {
        lstResult.get(0).setTotalRow(lstData.size());
      }
    }
    datatable.setData(lstResult);
    return datatable;
  }

  @Override
  public Datatable getDatatableInfraDeviceIpForCr(InfraDeviceDTO infraDeviceDTO) {
    Datatable datatable = new Datatable();
    List<InfraDeviceDTO> lstData = getListInfraDeviceIpV2(infraDeviceDTO);
    int size = infraDeviceDTO.getPageSize();
    size = (size > 0) ? size : 5;
    if (lstData != null && !lstData.isEmpty()) {
      int pageSize = (int) Math.ceil(lstData.size() * 1.0 / size);
      datatable.setTotal(lstData.size());
      datatable.setPages(pageSize);
      int totalRow = lstData.size();
      lstData = (List<InfraDeviceDTO>) DataUtil
          .subPageList(lstData, infraDeviceDTO.getPage(), size);
      lstData.get(0).setTotalRow(totalRow);
    }
    datatable.setData(lstData);
    return datatable;
  }

  @Override
  public List<InfraDeviceDTO> geInfraDeviceByIps(List<String> lstIp, String nationCode) {
    return infraDeviceRepository.geInfraDeviceByIps(lstIp, nationCode);
  }

  @Override
  public List<InfraDeviceDTO> getListInfraDeviceNonIp(InfraDeviceDTO infraDeviceDTO) {
    return infraDeviceRepository.getListInfraDeviceNonIp(infraDeviceDTO);
  }

  @Override
  public List<InfraDeviceDTO> getListInfraDeviceDTOV2(InfraDeviceDTO infraDeviceDTO) {
    return infraDeviceRepository.getListInfraDeviceDTOV2(infraDeviceDTO);
  }
}
