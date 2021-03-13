package com.viettel.gnoc.pt.business;

import com.viettel.gnoc.commons.business.UserBusiness;
import com.viettel.gnoc.commons.config.I18n;
import com.viettel.gnoc.commons.config.TimezoneContextHolder;
import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UsersInsideDto;
import com.viettel.gnoc.commons.proxy.CrServiceProxy;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.commons.utils.ConditionBeanUtil;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.DataUtil;
import com.viettel.gnoc.commons.utils.DateUtil;
import com.viettel.gnoc.cr.dto.CrDTO;
import com.viettel.gnoc.cr.dto.CrSearchDTO;
import com.viettel.gnoc.pt.dto.ProblemCrDTO;
import com.viettel.gnoc.pt.repository.ProblemCrRepository;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class ProblemCrBusinessImpl implements ProblemCrBusiness {

  @Autowired
  ProblemCrRepository problemCrRepository;

  @Autowired
  private UserBusiness userBusiness;

  @Autowired
  CrServiceProxy crServiceProxy;

  @Override
  public List<ProblemCrDTO> getListProblemCrByCondition(
      List<ConditionBean> lstCondition, int rowStart, int maxRow, String sortType,
      String sortFieldList) {
    log.debug("Request to getListProblemCrByCondition: {}", lstCondition);
    ConditionBeanUtil.sysToOwnListCondition(lstCondition);
    return problemCrRepository.getListProblemCrByCondition(lstCondition, rowStart, maxRow,
        sortType, sortFieldList);
  }

  @Override
  public List<ProblemCrDTO> getListProblemCrDTO(
      ProblemCrDTO problemCrDTO, int rowStart, int maxRow, String sortType,
      String sortFieldList) {
    log.debug("Request to getListProblemCrDTO: {}", problemCrDTO);
    return problemCrRepository.getListProblemCrDTO(problemCrDTO, rowStart,
        maxRow, sortType, sortFieldList);
  }

  @Override
  public String insertOrUpdateListProblemCr(
      List<ProblemCrDTO> problemCrDTO) {
    log.debug("Request to insertOrUpdateListProblemCr: {}", problemCrDTO);
    return problemCrRepository.insertOrUpdateListProblemCr(problemCrDTO);
  }

  @Override
  public ResultInSideDto insertProblemCr(ProblemCrDTO problemCrDTO) {
    log.debug("Request to insertProblemCr: {}", problemCrDTO);
    return problemCrRepository.insertProblemCr(problemCrDTO);
  }

  @Override
  public ProblemCrDTO findProblemCrById(Long id) {
    log.debug("Request to findProblemCrById: {}", id);
    return problemCrRepository.findProblemCrById(id);
  }

  @Override
  public List<String> getSequenseProblemCr(String seqName, int... size) {
    log.debug("Request to getSequenseProblemCr: {}", seqName);
    return problemCrRepository.getSequenseProblemCr(seqName, size);
  }

  @Override
  public String updateProblemCr(ProblemCrDTO problemCrDTO) {
    log.debug("Request to updateProblemCr: {}", problemCrDTO);
    return problemCrRepository.updateProblemCr(problemCrDTO);
  }

  @Override
  public String deleteListProblemCr(List<ProblemCrDTO> problemCrDTO) {
    log.debug("Request to deleteListProblemCr: {}", problemCrDTO);
    return problemCrRepository.deleteListProblemCr(problemCrDTO);
  }

  @Override
  public String deleteProblemCr(Long id) {
    log.debug("Request to deleteProblemCr: {}", id);
    return problemCrRepository.deleteProblemCr(id);
  }

  @Override
  public List<ProblemCrDTO> onSearchProblemCrDTO(ProblemCrDTO dto) {
    log.debug("Request to onSearchProblemCrDTO: {}", dto);
    return problemCrRepository.onSearchProblemCrDTO(dto);
  }

  @Override
  public Datatable searchProblemCr(ProblemCrDTO problemCrDTO) {
    String sortType = problemCrDTO.getSortType();
    String sortName = problemCrDTO.getSortName();
    Datatable datatable = new Datatable();
    Double offset = TimezoneContextHolder.getOffsetDouble();
    int page = problemCrDTO.getPage();
    int size = problemCrDTO.getPageSize();
    size = (size > 0) ? size : 5;
    problemCrDTO.setSortName(null);
    problemCrDTO.setSortType(null);
    problemCrDTO.setPage(1);
    problemCrDTO.setPageSize(Integer.MAX_VALUE);
    List<ProblemCrDTO> lstProblemCrDTO = onSearchProblemCrDTO(problemCrDTO);
    if (lstProblemCrDTO != null && !lstProblemCrDTO.isEmpty()) {
      List<ConditionBean> lstCon = new ArrayList<>();
      String lstCrId = "";
      lstCrId = lstProblemCrDTO.stream().map((dtoProblem) -> dtoProblem.getCrId() + ",")
          .reduce(lstCrId, String::concat);
      if (lstCrId.endsWith(",")) {
        lstCrId = lstCrId.substring(0, lstCrId.length() - 1);
      }
      lstCon.add(new ConditionBean("crId", lstCrId, Constants.NAME_IN, Constants.NUMBER));
      ConditionBeanUtil.sysToOwnListCondition(lstCon);
      List<CrDTO> crDTOS = crServiceProxy
          .getListCrByCondition(new BaseDto(lstCon, 0, Integer.MAX_VALUE, sortType, sortName));
      if (crDTOS != null) {
        List<String> lstUserId = new ArrayList<>();
        for (CrDTO crDTO : crDTOS) {
          lstUserId.add(crDTO.getChangeOrginator());
          crDTO.setState(convertStatusCR(crDTO.getState()));
        }
        List<ConditionBean> lstCondition = new ArrayList<>();
        lstCondition.add(new ConditionBean("userId",
            lstUserId.toString().substring(1, lstUserId.toString().length() - 1), Constants.NAME_IN,
            Constants.NUMBER));
        List<UsersInsideDto> usersInsideDtos = userBusiness
            .getListUsersByCondition(lstCondition, 0, 100, "", "");
        Map<String, String> mapUser = new HashMap<>();
        if (usersInsideDtos != null) {
          for (UsersInsideDto usr : usersInsideDtos) {
            mapUser.put(usr.getUserId().toString(), usr.getUsername());
          }
        }

        int totalSize = crDTOS.size();
        int pageSize = (int) Math.ceil(totalSize * 1.0 / size);
        datatable.setTotal(totalSize);
        datatable.setPages(pageSize);
        List<CrDTO> crSubList = (List<CrDTO>) DataUtil.subPageList(crDTOS, page, size);
        List<CrSearchDTO> crSearchDTOS = new ArrayList<>();
        for (CrDTO crDTO : crSubList) {
          CrSearchDTO crSearchDTO = new CrSearchDTO();
          crSearchDTO.setPage(page);
          crSearchDTO.setPageSize(size);
          crSearchDTO.setTotalRow(totalSize);
          crSearchDTO.setChangeOrginatorName(mapUser.get(crDTO.getChangeOrginator()));
          crSearchDTO.setCrNumber(crDTO.getCrNumber());
          crSearchDTO.setTitle(crDTO.getTitle());
          crSearchDTO.setState(crDTO.getState());
          try {
            Date crEarliestStartTime = DateUtil.string2DateTime(crDTO.getEarliestStartTime());
            Date crLatestStartTime = DateUtil.string2DateTime(crDTO.getLatestStartTime());
            crSearchDTO.setEarliestStartTime(DateUtil.date2ddMMyyyyHHMMss(
                new Date(crEarliestStartTime.getTime() + (long) (offset * 60 * 60 * 1000))));
            crSearchDTO.setLatestStartTime(DateUtil.date2ddMMyyyyHHMMss(
                new Date(crLatestStartTime.getTime() + (long) (offset * 60 * 60 * 1000))));
          } catch (Exception e) {
            log.error(e.getMessage(), e);
          }
          crSearchDTOS.add(crSearchDTO);
        }
        datatable.setData(crSearchDTOS);
      }
    }
    return datatable;
  }

  public String convertStatusCR(String stateId) {
    if (stateId != null) {
      return I18n.getLanguage("cr.state." + stateId);
    }
    return "";
  }
}
