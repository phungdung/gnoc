package com.viettel.gnoc.pt.service;

import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.kedb.dto.AuthorityDTO;
import com.viettel.gnoc.pt.dto.ProblemsDTO;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService(serviceName = "ProblemsService")
public interface ProblemsService {

  @WebMethod(operationName = "insertProblems")
  ResultDTO insertProblems(@WebParam(name = "problemsDTO") ProblemsDTO problemsDTO);

  @WebMethod
  public ResultDTO onSynchProblem(@WebParam(name = "requestDTO") AuthorityDTO requestDTO,
      @WebParam(name = "fromDate") String fromDate, @WebParam(name = "toDate") String toDate,
      @WebParam(name = "insertSource") String insertSource,
      @WebParam(name = "state") List<String> state
  );
}
