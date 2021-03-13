package com.viettel.gnoc.pt.business;

import com.viettel.gnoc.commons.business.UserBusiness;
import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.config.TimezoneContextHolder;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.pt.dto.ProblemWoDTO;
import com.viettel.gnoc.pt.dto.ProblemsInsideDTO;
import com.viettel.gnoc.pt.repository.ProblemWoRepository;
import com.viettel.gnoc.wfm.dto.WoSearchDTO;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import viettel.passport.client.UserToken;

@Service
@Transactional
@Slf4j
public class ProblemWoBusinessImpl implements ProblemWoBusiness {


  @Autowired
  ProblemWoRepository problemWoRepsitory;

  @Autowired
  UserBusiness userBusiness;

  @Autowired
  TicketProvider ticketProvider;

  @Override
  public ResultInSideDto insertProblemWo(ProblemWoDTO problemWoDTO) {
    return problemWoRepsitory.insertProblemWo(problemWoDTO);
  }

  @Override
  public String updateProblemWo(ProblemWoDTO problemWoDTO) {
    return problemWoRepsitory.updateProblemWo(problemWoDTO);
  }

  @Override
  public String deleteProblemWo(Long id) {
    return problemWoRepsitory.deleteProblemWo(id);
  }


  @Override
  public String deleteListProblemWo(List<ProblemWoDTO> problemWoDTOList) {
    return problemWoRepsitory.deleteListProblemWo(problemWoDTOList);
  }

  @Override
  public String insertOrUpdateListProblemWo(List<ProblemWoDTO> problemWoDTO) {
    return problemWoRepsitory.insertOrUpdateListProblemWo(problemWoDTO);
  }

  @Override
  public ProblemWoDTO findProblemWoById(Long id) {
    return problemWoRepsitory.findProblemWoById(id);
  }

  @Override
  public List<ProblemWoDTO> getListProblemWoByCondition(List<ConditionBean> lstCondition,
      int rowStart, int maxRow, String sortType, String sortFieldList) {
    return problemWoRepsitory
        .getListProblemWoByCondition(lstCondition, rowStart, maxRow, sortType, sortFieldList);
  }

  @Override
  public Datatable getListProblemWo(ProblemWoDTO problemWoDTO) {
    return problemWoRepsitory.getListProblemWo(problemWoDTO);
  }

  @Override
  public List<String> getSeqProblemWo(String seqName, int... size) {
    return problemWoRepsitory.getSeqProblemWo(seqName, size);
  }

  @Override
  public Datatable getListDataSearchWeb(ProblemsInsideDTO problemsInsideDTO) {
    WoSearchDTO woSearchDTO = new WoSearchDTO();
    woSearchDTO.setPage(problemsInsideDTO.getPage());
    woSearchDTO.setPageSize(problemsInsideDTO.getPageSize());
    woSearchDTO.setSortName(problemsInsideDTO.getSortName());
    woSearchDTO.setSortType(problemsInsideDTO.getSortType());
    UserToken userToken = ticketProvider.getUserToken();
    Double offset = TimezoneContextHolder.getOffsetDouble();
    woSearchDTO.setOffset(offset.longValue());
    woSearchDTO.setUserId((userToken == null) ? null : String.valueOf(userToken.getDeptId()));
    woSearchDTO.setWoSystemId(problemsInsideDTO.getProblemCode());
    return problemWoRepsitory.getListDataSearchWeb(woSearchDTO);
  }
}
