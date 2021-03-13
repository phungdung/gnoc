package com.viettel.gnoc.pt.service;

import static com.viettel.gnoc.commons.utils.DateTimeUtils.validateInput;

import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.commons.validator.ValidateAccount;
import com.viettel.gnoc.kedb.dto.AuthorityDTO;
import com.viettel.gnoc.pt.business.ProblemsBusiness;
import com.viettel.gnoc.pt.dto.ProblemsDTO;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ProblemsServiceImpl implements ProblemsService {

  @Autowired
  protected ProblemsBusiness problemsBusiness;

  @Override
  public ResultDTO insertProblems(ProblemsDTO problemsDTO) {
    log.debug("Request to insertProblems : {}", problemsDTO);
    try {
      ResultInSideDto resultInSideDto = problemsBusiness
          .insertProblemsCommon(null, problemsDTO, false);
      return resultInSideDto.toResultDTO();
    } catch (Exception e) {
      return new ResultDTO(null, RESULT.FAIL, e.getMessage());
    }
  }

  @Override
  public ResultDTO onSynchProblem(AuthorityDTO requestDTO, String fromDate, String toDate,
      String insertSource, List<String> lstState) {
    ResultDTO result = new ResultDTO();
    try {
      String startTime = DateTimeUtils.getSysDateTime();
      ValidateAccount account = new ValidateAccount();
      String validate = account.checkAuthenticate(requestDTO);

      if (validate.equals(RESULT.SUCCESS)) {

        validate = validateInput(fromDate, toDate);
        if (validate.equals(RESULT.SUCCESS)) {
          List<ProblemsDTO> lst = problemsBusiness
              .onSynchProblem(fromDate, toDate, insertSource, lstState);
          result.setLstResult(lst);
        }
      }
      String endTime = DateTimeUtils.getSysDateTime();

      if (validate.equals(RESULT.SUCCESS)) {
        result.setKey(RESULT.SUCCESS);
      } else {
        result.setKey(RESULT.FAIL);
      }
      result.setRequestTime(startTime);
      result.setFinishTime(endTime);
      result.setMessage(validate);

    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
    }
    return result;
  }
}
