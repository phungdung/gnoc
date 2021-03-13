package com.viettel.gnoc.business;

import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.TableConfigUserDTO;
import com.viettel.gnoc.commons.utils.ConditionBean;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.repository.TableConfigUserRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import viettel.passport.client.UserToken;

@Service
@Transactional
@Slf4j
public class TableConfigUserBusinessImpl implements TableConfigUserBusiness {

  @Autowired
  TableConfigUserRepository tableConfigUserRepository;
  @Autowired
  TicketProvider ticketProvider;

  @Override
  public List<TableConfigUserDTO> getListTableConfigUserDTO(TableConfigUserDTO tableConfigUserDTO,
      int rowStart, int maxRow, String sortType, String sortFieldList) {
    UserToken userToken = ticketProvider.getUserToken();
    if (userToken == null) {
      log.error("TableConfigUserDTO: UserToken is null");
    } else {
      if (checkIsOwnSystem(tableConfigUserDTO)) {
        tableConfigUserDTO.setUserName(userToken.getUserName());
      }
    }
    return tableConfigUserRepository
        .getListTableConfigUserDTO(tableConfigUserDTO, rowStart, maxRow, sortType, sortFieldList);
  }

  @Override
  public List<TableConfigUserDTO> getListTableConfigUserByCondition(
      List<ConditionBean> lstCondition,
      int rowStart, int maxRow, String sortType, String sortFieldList) {
    return tableConfigUserRepository
        .getListTableConfigUserByCondition(lstCondition, rowStart, maxRow, sortType, sortFieldList);
  }

  @Override
  public List<String> getSequenseTableConfigUser(String seqName, int... size) {
    return tableConfigUserRepository.getSequenseTableConfigUser(seqName, size);
  }

  @Override
  public String updateTableConfigUser(TableConfigUserDTO tableConfigUserDTO) {
    UserToken userToken = ticketProvider.getUserToken();
    if (checkIsOwnSystem(tableConfigUserDTO)) {
      tableConfigUserDTO.setUserId(userToken.getUserID());
      tableConfigUserDTO.setUserName(userToken.getUserName());
    }
    return tableConfigUserRepository.updateTableConfigUser(tableConfigUserDTO);
  }

  @Override
  public ResultInSideDto insertTableConfigUser(TableConfigUserDTO tableConfigUserDTO) {
    UserToken userToken = ticketProvider.getUserToken();
    if (checkIsOwnSystem(tableConfigUserDTO)) {
      tableConfigUserDTO.setUserId(userToken.getUserID());
      tableConfigUserDTO.setUserName(userToken.getUserName());
    }
    return tableConfigUserRepository.insertTableConfigUser(tableConfigUserDTO);
  }

  @Override
  public String insertOrUpdateListTableConfigUser(List<TableConfigUserDTO> tableConfigUserDTO) {
    return tableConfigUserRepository.insertOrUpdateListTableConfigUser(tableConfigUserDTO);
  }

  @Override
  public TableConfigUserDTO findTableConfigUserById(Long id) {
    return tableConfigUserRepository.findTableConfigUserById(id);
  }

  @Override
  public ResultInSideDto deleteTableConfigUser(Long id) {
    return tableConfigUserRepository.deleteTableConfigUser(id);
  }

  @Override
  public String deleteListTableConfigUser(List<TableConfigUserDTO> tableConfigUserDTO) {
    return tableConfigUserRepository.deleteListTableConfigUser(tableConfigUserDTO);
  }

  public boolean checkIsOwnSystem(TableConfigUserDTO tableConfigUserDTO) {
    return (StringUtils.isStringNullOrEmpty(tableConfigUserDTO.getUserName()) &&
        StringUtils.isStringNullOrEmpty(tableConfigUserDTO.getUserId())
    );
  }
}
