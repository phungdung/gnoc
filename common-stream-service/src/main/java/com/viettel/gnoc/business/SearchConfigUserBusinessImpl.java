package com.viettel.gnoc.business;

import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.SearchConfigUserDTO;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.repository.SearchConfigUserRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import viettel.passport.client.UserToken;

@Service
@Transactional
@Slf4j
public class SearchConfigUserBusinessImpl implements SearchConfigUserBusiness {

  @Autowired
  SearchConfigUserRepository searchConfigUserRepository;
  @Autowired
  TicketProvider ticketProvider;

  @Override
  public List<SearchConfigUserDTO> getListSearchConfigUserDTO(
      SearchConfigUserDTO searchConfigUserDTO, int rowStart, int maxRow, String sortType,
      String sortFieldList) {
    UserToken userToken = ticketProvider.getUserToken();
    if (checkIsOwnSystem(searchConfigUserDTO)) {
//      searchConfigUserDTO.setUserName(userToken.getUserName());
      searchConfigUserDTO.setUserId(userToken.getUserID());
    }
    return searchConfigUserRepository
        .getListSearchConfigUserDTO(searchConfigUserDTO, rowStart, maxRow, sortType, sortFieldList);
  }

  @Override
  public List<SearchConfigUserDTO> getListSearchConfigUserByCondition(
      List<ConditionBean> lstCondition,
      int rowStart, int maxRow, String sortType, String sortFieldList) {
    return searchConfigUserRepository
        .getListSearchConfigUserByCondition(lstCondition, rowStart, maxRow, sortType,
            sortFieldList);
  }

  @Override
  public List<String> getSequenseSearchConfigUser(String seqName, int... size) {
    return searchConfigUserRepository.getSequenseSearchConfigUser(seqName, size);
  }

  @Override
  public String updateSearchConfigUser(SearchConfigUserDTO searchConfigUserDTO) {
    if (checkIsOwnSystem(searchConfigUserDTO)) {
      UserToken userToken = ticketProvider.getUserToken();
      searchConfigUserDTO.setUserName(userToken.getUserName());
      searchConfigUserDTO.setUserId(userToken.getUserID());
    }
    return searchConfigUserRepository.updateSearchConfigUser(searchConfigUserDTO);
  }

  @Override
  public ResultInSideDto insertSearchConfigUser(SearchConfigUserDTO searchConfigUserDTO) {
    if (checkIsOwnSystem(searchConfigUserDTO)) {
      UserToken userToken = ticketProvider.getUserToken();
      searchConfigUserDTO.setUserName(userToken.getUserName());
      searchConfigUserDTO.setUserId(userToken.getUserID());
    }
    return searchConfigUserRepository.insertSearchConfigUser(searchConfigUserDTO);
  }

  @Override
  public ResultInSideDto insertOrUpdateListSearchConfigUser(
      SearchConfigUserDTO searchConfigUserDTO) {
    if (checkIsOwnSystem(searchConfigUserDTO)) {
      UserToken userToken = ticketProvider.getUserToken();
      searchConfigUserDTO.setUserName(userToken.getUserName());
      searchConfigUserDTO.setUserId(userToken.getUserID());
    }
    return searchConfigUserRepository.insertOrUpdateListSearchConfigUser(searchConfigUserDTO);
  }

  @Override
  public SearchConfigUserDTO findSearchConfigUserById(Long id) {
    return searchConfigUserRepository.findSearchConfigUserById(id);
  }

  @Override
  public String deleteSearchConfigUser(Long id) {
    return searchConfigUserRepository.deleteSearchConfigUser(id);
  }

  @Override
  public ResultInSideDto deleteListSearchConfigUser(SearchConfigUserDTO searchConfigUserDTO) {
    if (checkIsOwnSystem(searchConfigUserDTO)) {
      UserToken userToken = ticketProvider.getUserToken();
      searchConfigUserDTO.setUserName(userToken.getUserName());
      searchConfigUserDTO.setUserId(userToken.getUserID());
    }
    return searchConfigUserRepository.deleteListSearchConfigUser(searchConfigUserDTO);
  }

  public boolean checkIsOwnSystem(SearchConfigUserDTO searchConfigUserDTO) {
    return (StringUtils.isStringNullOrEmpty(searchConfigUserDTO.getUserName()) &&
        StringUtils.isStringNullOrEmpty(searchConfigUserDTO.getUserId())
    );
  }
}
