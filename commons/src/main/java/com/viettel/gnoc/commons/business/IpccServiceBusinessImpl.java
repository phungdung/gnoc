package com.viettel.gnoc.commons.business;

import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.IpccServiceDTO;
import com.viettel.gnoc.commons.dto.LogChangeConfigDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.model.IpccServiceEntity;
import com.viettel.gnoc.commons.repository.IpccServiceRepository;
import com.viettel.gnoc.commons.repository.LogChangeConfigRepository;
import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.commons.utils.ws.PassTranformerPTTTOld;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import viettel.passport.client.UserToken;

@Service
@Slf4j
@Transactional
public class IpccServiceBusinessImpl implements IpccServiceBusiness {

  @Autowired
  private IpccServiceRepository ipccServiceRepository;
  @Autowired
  private LogChangeConfigRepository logChangeConfigRepository;
  @Autowired
  TicketProvider ticketProvider;

  @Override
  public List<IpccServiceDTO> getListIpccServiceAll() {
    log.info("Request to getListIpccServiceDTOPage : {}");
    return ipccServiceRepository.getListIpccServiceAll();
  }

  @Override
  public Datatable getListIpccServiceDTOPage(IpccServiceDTO ipccServiceDTO) {
    log.info("Request to getListIpccServiceDTOPage : {}", ipccServiceDTO);
    Datatable datatable = ipccServiceRepository.getListIpccServiceDTOPage(ipccServiceDTO);
    List<IpccServiceDTO> lst = (List<IpccServiceDTO>) datatable.getData();
    List<IpccServiceDTO> lstResult = new ArrayList<>();
    if (lst != null && !lst.isEmpty()) {
      for (IpccServiceDTO dto : lst) {
        try {
          dto.setPassword(StringUtils.isStringNullOrEmpty(dto.getPassword()) ? null
              : PassTranformerPTTTOld.decrypt(dto.getPassword()));
        } catch (Exception e) {
          log.error(e.getMessage(), e);
        }
        lstResult.add(dto);
      }
      datatable.setData(lstResult);
    }
    return datatable;
  }

  @Override
  public IpccServiceDTO getDeatilIpccServiceById(Long id) {
    log.info("Request to getDeatilIpccServiceById : {}", id);
    IpccServiceDTO ipccServiceDTO = ipccServiceRepository.getDeatilIpccServiceById(id);
    if (ipccServiceDTO != null) {
      try {
        ipccServiceDTO
            .setPassword(StringUtils.isStringNullOrEmpty(ipccServiceDTO.getPassword()) ? null
                : PassTranformerPTTTOld.decrypt(ipccServiceDTO.getPassword()));
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
    return ipccServiceDTO;
  }

  @Override
  public ResultInSideDto addOrUpdateIpccService(IpccServiceDTO ipccServiceDTO) {
    log.info("Request to addOrUpdateIpccService : {}", ipccServiceDTO);
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    if (StringUtils.isNotNullOrEmpty(ipccServiceDTO.getPassword())) {
      try {
        ipccServiceDTO.setPassword(PassTranformerPTTTOld.encrypt(ipccServiceDTO.getPassword()));
      } catch (Exception e) {
        ipccServiceDTO.setPassword(null);
        log.error(e.getMessage(), e);
      }
    }
    if (ipccServiceDTO.getIsDefault() != null && ipccServiceDTO.getIsDefault() == 1L) {
      List<IpccServiceEntity> lst = ipccServiceRepository
          .findByIsDefault(ipccServiceDTO.getIsDefault());
      for (IpccServiceEntity entity : lst) {
        entity.setIsDefault(0L);
        resultInSideDto = ipccServiceRepository.addOrUpdateIpccService(entity.toDTO());
      }
    }
    resultInSideDto = ipccServiceRepository.addOrUpdateIpccService(ipccServiceDTO);
    if (resultInSideDto.getKey().equals(Constants.RESULT.SUCCESS)) {
      UserToken userToken = ticketProvider.getUserToken();
      if (ipccServiceDTO.getIpccServiceId() != null && ipccServiceDTO.getIpccServiceId() > 0) {
        logChangeConfigRepository.insertLog(new LogChangeConfigDTO(userToken.getUserName(),
            "Update IpccServiceDTO",
            "Update IpccServiceDTO ID: " + resultInSideDto.getId(), ipccServiceDTO,
            null));
      } else {
        logChangeConfigRepository.insertLog(new LogChangeConfigDTO(userToken.getUserName(),
            "Add IpccServiceDTO",
            "Add IpccServiceDTO ID: " + resultInSideDto.getId(), ipccServiceDTO,
            null));
      }
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto deleteIpccService(Long id) {
    log.info("Request to deleteIpccService : {}", id);
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.ERROR);
    if (id != null && id > 0) {
      resultInSideDto = ipccServiceRepository.deleteIpccService(id);
      if (resultInSideDto.getKey().equals(Constants.RESULT.SUCCESS)) {
        UserToken userToken = ticketProvider.getUserToken();
        logChangeConfigRepository.insertLog(new LogChangeConfigDTO(userToken.getUserName(),
            "Delete IpccService",
            "Delete IpccService ID: " + id, null,
            null));

      }
    }
    return resultInSideDto;
  }
}
