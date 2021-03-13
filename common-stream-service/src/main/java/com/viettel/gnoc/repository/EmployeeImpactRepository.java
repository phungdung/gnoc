package com.viettel.gnoc.repository;

import com.viettel.gnoc.commons.dto.CatItemDTO;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.cr.dto.CfgChildArrayDTO;
import com.viettel.gnoc.cr.dto.EmployeeImpactDTO;
import com.viettel.gnoc.cr.dto.ItemDataCRDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeImpactRepository {

  ResultInSideDto insertEmpImpact(EmployeeImpactDTO employeeImpactDTO);

  List<ItemDataCRDTO> getListParentArray();

  Datatable getListLevel(CatItemDTO catItemDTO);

  List<CfgChildArrayDTO> getListChildArray(CfgChildArrayDTO cfgChildArrayDTO);

  Datatable getListEmployeeImpact(EmployeeImpactDTO employeeImpactDTO);

  String deleteEmpImpact(Long id);

  // ResultInSideDto updateEmpImpact(EmployeeImpactDTO dto);

  EmployeeImpactDTO findEmpImpactById(Long id);

  String deleteEmpImpactInUpdate(Long idImpact);
}
