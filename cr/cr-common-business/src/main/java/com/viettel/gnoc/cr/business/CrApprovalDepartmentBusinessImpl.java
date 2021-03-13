package com.viettel.gnoc.cr.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.utils.Constants.CR_ACTION;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.cr.dto.CrApprovalDepartmentDTO;
import com.viettel.gnoc.cr.dto.CrApprovalDepartmentInsiteDTO;
import com.viettel.gnoc.cr.repository.CrApprovalDepartmentRepository;
import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class CrApprovalDepartmentBusinessImpl implements CrApprovalDepartmentBusiness {

  @Autowired
  CrApprovalDepartmentRepository crApprovalDepartmentRepository;

  @Override
  public Datatable searchSQL(CrApprovalDepartmentInsiteDTO dto) {
    Datatable data = new Datatable();
    List<CrApprovalDepartmentInsiteDTO> lstAll;
    List<CrApprovalDepartmentInsiteDTO> lst;
    if (CR_ACTION.IS_EDIT.equals(dto.getSortType())) {
      lstAll = crApprovalDepartmentRepository.getApprovalDepartmentByCreator(dto);
      if (dto.getCrProcessDTO() != null && dto.getCrProcessDTO().getCrProcessId() != null) {
        lst = crApprovalDepartmentRepository
            .getApprovalDepartmentByProcess(dto.getCrProcessDTO().getCrProcessId());
        if (lstAll == null) {
          lstAll = new ArrayList<>();
        }
        if (lst != null && !lst.isEmpty()) {
          lstAll.addAll(lst);
        }
      }
    } else {
      lstAll = crApprovalDepartmentRepository.getApprovalDepartmentByCrId(dto);
    }
    if (lstAll != null && lstAll.size() > 0) {
      data.setTotal(lstAll.size());
      int pages = (int) Math.ceil(lstAll.size() * 1.0 / dto.getPageSize());
      data.setPages(pages);
      data.setData(DataUtil.subPageList(lstAll, dto.getPage(), dto.getPageSize()));
    }
    return data;
  }

  @Override
  public List<CrApprovalDepartmentInsiteDTO> getApprovalDepartmentByProcess(Long crProcessId) {
    List<CrApprovalDepartmentInsiteDTO> lst = new ArrayList<>();
    if (crProcessId != null) {
      lst = crApprovalDepartmentRepository
          .getApprovalDepartmentByProcess(crProcessId);
    }
    return lst;
  }

  @Override
  public List<CrApprovalDepartmentInsiteDTO> search(CrApprovalDepartmentInsiteDTO tDTO, int start,
      int maxResult, String sortType, String sortField) {
    return crApprovalDepartmentRepository.search(tDTO, start, maxResult, sortType,
        sortField);
  }

  @Override
  public List<CrApprovalDepartmentDTO> onSearch(CrApprovalDepartmentDTO tDTO, int start,
      int maxResult, String sortType, String sortField) {
    return crApprovalDepartmentRepository.onSearch(tDTO, start, maxResult, sortType, sortField);
  }
}
