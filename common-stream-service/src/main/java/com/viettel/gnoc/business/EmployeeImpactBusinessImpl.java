package com.viettel.gnoc.business;

import com.viettel.gnoc.commons.business.CatItemBusiness;
import com.viettel.gnoc.commons.business.LogChangeConfigBusiness;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.LogChangeConfigDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.cr.dto.CfgChildArrayDTO;
import com.viettel.gnoc.cr.dto.EmployeeImpactDTO;
import com.viettel.gnoc.cr.dto.ItemDataCRDTO;
import com.viettel.gnoc.repository.EmployeeImpactRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import viettel.passport.client.UserToken;

@Slf4j
@Transactional
@Service
public class EmployeeImpactBusinessImpl implements EmployeeImpactBusiness {

  @Autowired
  private EmployeeImpactRepository employeeImpactRepository;

  @Autowired
  TicketProvider ticketProvider;

  @Autowired
  private LogChangeConfigBusiness logChangeConfigBusiness;

  @Autowired
  private CatItemBusiness catItemBusiness;

  @Override
  public ResultInSideDto insertEmpImpact(EmployeeImpactDTO employeeImpactDTO) {
    log.debug("Request to insertEmpImpact : {}", employeeImpactDTO);
    if (employeeImpactDTO.getStatusUpdate() == null) {
      employeeImpactDTO.setStatusUpdate(0L);
    } else if (employeeImpactDTO.getStatusUpdate() == 1L) {
      employeeImpactDTO.setStatusUpdate(1L);
    }
    return employeeImpactRepository.insertEmpImpact(employeeImpactDTO);
  }

  @Override
  public List<ItemDataCRDTO> getListParentArray() {
    log.debug("Request to getListParentArray : {}");
    return employeeImpactRepository.getListParentArray();
  }

  @Override
  public Datatable getListLevel(CatItemDTO catItemDTO) {
    log.debug("Request to getListLevel : {}", catItemDTO);
    return employeeImpactRepository.getListLevel(catItemDTO);
  }

  @Override
  public List<CfgChildArrayDTO> getListChildArray(CfgChildArrayDTO cfgChildArrayDTO) {
    log.debug("Request to getListLevel : {}");
    return employeeImpactRepository.getListChildArray(cfgChildArrayDTO);
  }

  @Override
  public Datatable getListEmployeeImpact(EmployeeImpactDTO employeeImpactDTO) {
    log.debug("Request to getListLevel : {}", employeeImpactDTO);
    Datatable datatable = employeeImpactRepository.getListEmployeeImpact(employeeImpactDTO);
    Map<String, String> mapLevel = setLevelName();
    if (mapLevel.entrySet() != null && !mapLevel.entrySet().isEmpty()) {
      List<EmployeeImpactDTO> lstResult = (List<EmployeeImpactDTO>) datatable.getData();
      if (lstResult != null && !lstResult.isEmpty()) {
        for (EmployeeImpactDTO employee : lstResult) {
          if (employee != null && !StringUtils.isLongNullOrEmpty(employee.getEmpLevel()) && mapLevel
              .containsKey(String.valueOf(employee.getEmpLevel()))) {
            employee.setEmpLevelName(mapLevel.get(String.valueOf(employee.getEmpLevel())));
          }
        }
        datatable.setData(lstResult);
      }
    }
    return datatable;
  }

  private Map<String, String> setLevelName() {
    Map<String, String> mapLevelName = new HashMap<>();
    List<CatItemDTO> lstLevel = (List<CatItemDTO>) catItemBusiness
        .getItemMaster("GNOC_IMPACT", "1", "3", "itemId", "itemName").getData();
    if (lstLevel != null && !lstLevel.isEmpty()) {
      for (CatItemDTO dto : lstLevel) {
        if (!mapLevelName.containsKey(dto.getItemValue())) {
          mapLevelName.put(dto.getItemValue(), dto.getItemName());
        }
      }
    }
    return mapLevelName;
  }

  @Override
  public String deleteEmpImpact(Long id) {
    log.debug("Request to deleteEmpImpact : {}", id);
    return employeeImpactRepository.deleteEmpImpact(id);
  }

  @Override
  public ResultInSideDto updateEmpImpact(EmployeeImpactDTO dto) {
    log.debug("Request to updateEmpImpact : {}", dto);
    dto.setIdImpactSave(dto.getIdImpact());
    dto.setStatusUpdate(1L);
    deleteEmpImpactInUpdate(dto.getIdImpact());
    dto.setIdImpact(null);
    return insertEmpImpact(dto);
  }

  private String deleteEmpImpactInUpdate(Long idImpact) {
    log.debug("Request to deleteEmpImpactInUpdate : {}", idImpact);
    UserToken userToken = ticketProvider.getUserToken();
    logChangeConfigBusiness.insertLog(new LogChangeConfigDTO(userToken.getUserName(),
        "DeleteEmpImpactInUpdate", "DeleteEmpImpactInUpdate with ID: " + idImpact,
        null, null));
    return employeeImpactRepository.deleteEmpImpactInUpdate(idImpact);
  }

  @Override
  public EmployeeImpactDTO findEmpImpactById(Long id) {
    log.debug("Request to findEmpImpactById : {}", id);
    return employeeImpactRepository.findEmpImpactById(id);
  }
}
