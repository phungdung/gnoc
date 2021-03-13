package com.viettel.gnoc.sr.business;

import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.sr.dto.SRFlowExecuteDTO;
import com.viettel.gnoc.sr.dto.SRRoleActionDTO;
import com.viettel.gnoc.sr.repository.SRFlowExecuteRepository;
import com.viettel.gnoc.sr.repository.SRRoleActionRepository;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import viettel.passport.client.UserToken;

@Service
@Transactional
@Slf4j
public class SRRoleActionBusinessImpl implements SRRoleActionBusiness {

  @Autowired
  protected SRRoleActionRepository srRoleActionRepository;

  @Autowired
  protected SRFlowExecuteRepository srFlowExecuteRepository;

  @Autowired
  TicketProvider ticketProvider;

  @Override
  public List<SRRoleActionDTO> getComboBoxStatus() {
    log.debug("Request to getComboBoxStatus : {}");
    return srRoleActionRepository.getComboBoxStatus();
  }

  @Override
  public List<SRRoleActionDTO> getComboBoxRoleType() {
    log.debug("Request to getComboBoxRoleType : {}");
    return srRoleActionRepository.getComboBoxRoleType();
  }

  @Override
  public List<SRRoleActionDTO> getComboBoxActions(String roleType) {
    log.debug("Request to getComboBoxActions : {}", roleType);
    return srRoleActionRepository.getComboBoxActions(roleType);
  }

  @Override
  public List<SRRoleActionDTO> getComboBoxGroupRole() {
    log.debug("Request to getComboBoxGroupRole : {}");
    return srRoleActionRepository.getComboBoxGroupRole();
  }

  @Override
  public Datatable getListSRRoleActionPage(SRRoleActionDTO srRoleActionDTO) {
    log.debug("Request to getListSRRoleActionPage : {}", srRoleActionDTO);
    return srRoleActionRepository.getListSRRoleActionPage(srRoleActionDTO);
  }

  @Override
  public List<SRRoleActionDTO> getListSRRoleActionDTO(SRRoleActionDTO srRoleActionDTO) {
    log.debug("Request to getListSRRoleActionDTO : {}", srRoleActionDTO);
    return srRoleActionRepository.getListSRRoleActionDTO(srRoleActionDTO);
  }

  @Override
  public ResultInSideDto insert(SRFlowExecuteDTO srFlowExecuteDTO) {
    log.debug("Request to insert : {}", srFlowExecuteDTO);
    UserToken userToken = ticketProvider.getUserToken();
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    if (srFlowExecuteDTO.getFlowName() != null) {
      if (srFlowExecuteRepository.isDuplicateFlowName(srFlowExecuteDTO.getFlowName())) {
        resultInSideDto.setKey(RESULT.DUPLICATE);
      } else {
        //Xử lý cho chọn nhiều quốc gia, nhận vào 1 FlowExecuteDTOMainList
        for (SRFlowExecuteDTO srFlowExecuteDTOTmp : srFlowExecuteDTO.getFlowExecuteDTOMainList()) {
          srFlowExecuteDTOTmp.setFlowName(srFlowExecuteDTO.getFlowName());
          srFlowExecuteDTOTmp.setFlowDescription(srFlowExecuteDTO.getFlowDescription());
          srFlowExecuteDTOTmp.setLstRoleActionDTO(srFlowExecuteDTO.getLstRoleActionDTO());
          ResultInSideDto resultInSideDtoFlow = srFlowExecuteRepository
              .insertOrUpdate(srFlowExecuteDTOTmp);
          if (srFlowExecuteDTOTmp.getLstRoleActionDTO() != null && !srFlowExecuteDTOTmp
              .getLstRoleActionDTO()
              .isEmpty()) {
            List<SRRoleActionDTO> srRoleActionDTOList = new ArrayList<>();
            if (RESULT.SUCCESS.equals(resultInSideDtoFlow.getKey())) {
              for (SRRoleActionDTO dto : srFlowExecuteDTOTmp.getLstRoleActionDTO()) {
                dto.setFlowId(resultInSideDtoFlow.getId());
                dto.setCountry(String.valueOf(srFlowExecuteDTOTmp.getCountry()));
                dto.setCreatedUser(userToken.getUserName());
                dto.setCreatedTime(new Date(System.currentTimeMillis()));
                srRoleActionDTOList.add(dto);
              }
              srRoleActionRepository.insertOrUpdateList(srRoleActionDTOList);
            }
          }
        }
      }
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto update(SRFlowExecuteDTO srFlowExecuteDTO) {
    log.debug("Request to updateRoleActions : {}", srFlowExecuteDTO);
    UserToken userToken = ticketProvider.getUserToken();
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    //Delete Flow Old
    List<SRFlowExecuteDTO> listSRFlowExecuteDTODetail = srFlowExecuteDTO
        .getFlowExecuteDTODetailList();
    List<Long> listDetailFlowId = listSRFlowExecuteDTODetail.stream()
        .map(SRFlowExecuteDTO::getFlowId).collect(Collectors.toList());
    List<SRFlowExecuteDTO> listSRFlowExecuteDTOMain = srFlowExecuteDTO.getFlowExecuteDTOMainList();
    List<Long> listMainFlowId = listSRFlowExecuteDTOMain.stream()
        .map(SRFlowExecuteDTO::getFlowId).collect(Collectors.toList());
    if (listDetailFlowId != null && listDetailFlowId.size() > 0 &&
        listMainFlowId != null && listMainFlowId.size() > 0) {
      listDetailFlowId.removeAll(listMainFlowId);
    }
    if (listDetailFlowId != null && listDetailFlowId.size() > 0) {
      for (Long flowIdTmp : listDetailFlowId) {
        deleteFlowExecuteAndRoleAction(flowIdTmp);
      }
    }
    //Check trùng
    //Neu trung flowName trong DB nhung khac flowName cua chinh' no' thi bao trung
    if (srFlowExecuteRepository.isDuplicateFlowName(srFlowExecuteDTO.getFlowName())
        && !srFlowExecuteDTO.getFlowName()
        .equals(listSRFlowExecuteDTODetail.get(0).getFlowName())) {
      resultInSideDto.setKey(RESULT.DUPLICATE);
    } else {
      for (SRFlowExecuteDTO dto : listSRFlowExecuteDTOMain) {
        dto.setFlowName(srFlowExecuteDTO.getFlowName());
        dto.setFlowDescription(srFlowExecuteDTO.getFlowDescription());
        ResultInSideDto resultInSideDtoFlow = srFlowExecuteRepository.insertOrUpdate(dto);
        if (dto.getFlowId() != null) {
          //Delete Role Action Old
          srRoleActionRepository.deleteRoleActionByFlowID(dto.getFlowId());
        }
        for (SRRoleActionDTO srRoleActionDTO : srFlowExecuteDTO.getLstRoleActionDTO()) {
          srRoleActionDTO.setRoleActionId(null);
          srRoleActionDTO.setFlowId(resultInSideDtoFlow.getId());
          srRoleActionDTO.setCreatedUser(userToken.getUserName());
          srRoleActionDTO.setCreatedTime(new Date(System.currentTimeMillis()));
          srRoleActionRepository.insertOrUpdate(srRoleActionDTO);
        }
      }
    }
    return resultInSideDto;
  }

  public ResultInSideDto deleteFlowExecuteAndRoleAction(Long flowID) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    if (flowID != null) {
      srFlowExecuteRepository.delete(flowID);
      srRoleActionRepository.deleteRoleActionByFlowID(flowID);
    }
    return resultInSideDto;
  }

  @Override
  public SRRoleActionDTO getDetail(Long roleActionId) {
    log.debug("Request to getDetail : {}", roleActionId);
    SRRoleActionDTO dto = srRoleActionRepository.getDetail(roleActionId);
    String[] action = dto.getActions().split(",");
    List<SRRoleActionDTO> listName = srRoleActionRepository.getComboBoxActions(dto.getRoleType());
    if (StringUtils.isStringNullOrEmpty(dto.getActionsName())) {
      dto.setActionsName("");
    }
    for (SRRoleActionDTO temp : listName) {
      for (int i = 0; i < action.length; i++) {
        if (action[i].equalsIgnoreCase(temp.getActions())) {
          dto.setActionsName(dto.getActionsName() + "," + temp.getActionsName());
        }
      }
    }
    return dto;
  }

  @Override
  public ResultInSideDto deleteByRoleActionsId(Long roleActionId) {
    log.debug("Request to deleteByRoleActionsId : {}", roleActionId);
    return srRoleActionRepository.deleteByRoleActionsId(roleActionId);
  }
}
