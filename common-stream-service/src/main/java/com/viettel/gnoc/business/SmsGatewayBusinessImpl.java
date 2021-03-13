package com.viettel.gnoc.business;

import com.viettel.gnoc.commons.dto.Datatable;
import com.viettel.gnoc.commons.dto.IpccServiceDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.SmsGatewayDTO;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.commons.utils.ws.PassTranformerPTTTOld;
import com.viettel.gnoc.repository.SmsGatewayCommonRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@SuppressWarnings("rawtypes")
@Service
@Slf4j
@Transactional
public class SmsGatewayBusinessImpl implements SmsGatewayBusiness {

  @Value("${application.ws.user_service:null}")
  private String userConf;
  @Value("${application.ws.pass_service:null}")
  private String passConf;
  @Value("${application.ws.salt_service:null}")
  private String saltConf;

  @Autowired
  public SmsGatewayCommonRepository smsGatewayCommonRepository;

  @Override
  public Datatable getListSmsGatewayDTO(SmsGatewayDTO smsGatewayDTO) {
    return smsGatewayCommonRepository.getListSmsGatewayDTO(smsGatewayDTO);
  }

  @Override
  public List<SmsGatewayDTO> getListSmsGatewayAll(SmsGatewayDTO smsGatewayDTO) {
    return smsGatewayCommonRepository.getListSmsGatewayAll(smsGatewayDTO);
  }

  public void hiddenOrShowPass(SmsGatewayDTO smsGatewayDTO, boolean check) {
    if (!StringUtils.isStringNullOrEmpty(smsGatewayDTO.getUserName()) && !StringUtils
        .isStringNullOrEmpty(smsGatewayDTO.getPassWord())) {
      try {
        String pass = "";
        if (check) {
          pass = PassTranformerPTTTOld.encrypt(smsGatewayDTO.getPassWord());
        } else {
          pass = PassTranformerPTTTOld.decrypt(smsGatewayDTO.getPassWord());
        }
        if (!passConf.equals(pass)) {
          smsGatewayDTO.setPassWord(pass);
        }
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
  }


  @Override
  public ResultInSideDto updateSmsGateway(SmsGatewayDTO smsGatewayDTO) {
    if (!StringUtils.isStringNullOrEmpty(smsGatewayDTO.getAlias())) {
      smsGatewayDTO.setAlias(smsGatewayDTO.getAlias().toUpperCase());
    }

    hiddenOrShowPass(smsGatewayDTO, true);
    //20200212 - kienpv_start
    smsGatewayDTO.setContentTypeText("0");
    smsGatewayDTO.setDefaultSessionId("0");
    smsGatewayDTO.setStatusNotCharge("1");
    //20200212 - kienpv_end
    return smsGatewayCommonRepository.updateSmsGateway(smsGatewayDTO);
  }

  @Override
  public ResultInSideDto deleteSmsGateway(Long smsGatewayId) {
    return smsGatewayCommonRepository.deleteSmsGateway(smsGatewayId);
  }

  @Override
  public SmsGatewayDTO findSmsGatewayById(Long smsGatewayId) {
    SmsGatewayDTO dto = smsGatewayCommonRepository.findSmsGatewayById(smsGatewayId);
    if (dto != null) {
      hiddenOrShowPass(dto, false);
    }

    return dto;
  }

  @Override
  public ResultInSideDto insertSmsGateway(SmsGatewayDTO smsGatewayDTO) {
    if (!StringUtils.isStringNullOrEmpty(smsGatewayDTO.getAlias())) {
      smsGatewayDTO.setAlias(smsGatewayDTO.getAlias().toUpperCase());
    }
    hiddenOrShowPass(smsGatewayDTO, true);
    smsGatewayDTO.setContentTypeText("0");
    smsGatewayDTO.setDefaultSessionId("0");
    smsGatewayDTO.setStatusNotCharge("1");
    return smsGatewayCommonRepository.insertSmsGateway(smsGatewayDTO);
  }

  @Override
  public Datatable getListIpccServiceDatatable(IpccServiceDTO ipccServiceDTO) {
    return smsGatewayCommonRepository.getListIpccServiceDatatable(ipccServiceDTO);
  }

  @Override
  public List<IpccServiceDTO> getListIpccServiceDTO(IpccServiceDTO ipccServiceDTO) {
    return smsGatewayCommonRepository.getListIpccServiceDTO(ipccServiceDTO);
  }

  @Override
  public ResultInSideDto insertIpccServiceDTO(IpccServiceDTO ipccServiceDTO) {
    return smsGatewayCommonRepository.insertIpccServiceDTO(ipccServiceDTO);
  }

  @Override
  public ResultInSideDto updateIpccServiceDTO(IpccServiceDTO ipccServiceDTO) {
    return smsGatewayCommonRepository.updateIpccServiceDTO(ipccServiceDTO);
  }

  @Override
  public ResultInSideDto deleteIpccServiceDTO(Long ipccServiceId) {
    return smsGatewayCommonRepository.deleteIpccServiceDTO(ipccServiceId);
  }
}
