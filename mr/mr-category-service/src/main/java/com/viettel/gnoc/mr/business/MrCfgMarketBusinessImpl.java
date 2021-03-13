package com.viettel.gnoc.mr.business;

import com.viettel.gnoc.commons.config.TicketProvider;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.repository.UserRepository;
import com.viettel.gnoc.commons.utils.Constants.RESULT;
import com.viettel.gnoc.commons.utils.StringUtils;
import com.viettel.gnoc.maintenance.dto.MrCfgMarketDTO;
import com.viettel.gnoc.mr.repository.MrCfgMarketRepository;
import com.viettel.gnoc.mr.repository.MrDeviceRepository;
import com.viettel.gnoc.mr.repository.MrSynItHardDevicesRepository;
import com.viettel.gnoc.mr.repository.MrSynItSoftDevicesRepository;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import viettel.passport.client.UserToken;

@Service
@Transactional
@Slf4j
public class MrCfgMarketBusinessImpl implements MrCfgMarketBusiness {

  @Autowired
  protected MrCfgMarketRepository mrCfgMarketRepository;
  @Autowired
  protected MrDeviceRepository mrDeviceRepository;
  @Autowired
  protected UserRepository userRepository;
  @Autowired
  TicketProvider ticketProvider;
  @Autowired
  MrSynItSoftDevicesRepository mrSynItSoftDevicesRepository;
  @Autowired
  MrSynItHardDevicesRepository mrSynItHardDevicesRepository;

  @Override
  public List<MrCfgMarketDTO> getListCfgMarket(MrCfgMarketDTO mrCfgMarketDTO) {
    log.debug("Request to getListCfgMarket: {}", mrCfgMarketDTO);
    return mrCfgMarketRepository.getListCfgMarket(mrCfgMarketDTO);
  }

  @Override
  public ResultInSideDto updateListMarket(MrCfgMarketDTO mrCfgMarketDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    UserToken userToken = ticketProvider.getUserToken();
    List<MrCfgMarketDTO> lstMarketNew = mrCfgMarketDTO.getMrCfgMarketDTOList();
    if (lstMarketNew != null && lstMarketNew.size() > 0) {
      for (MrCfgMarketDTO marketUpdate : lstMarketNew) {
        MrCfgMarketDTO marketOld = mrCfgMarketRepository
            .findMrCfgMarketById(marketUpdate.getIdMarket());
        marketOld.setUpdatedTime(new Date());
        marketOld.setUpdatedUser(userToken.getUserName());
        if (StringUtils.isStringNullOrEmpty(marketUpdate.getCreatedUserHard()) && !StringUtils
            .isStringNullOrEmpty(marketOld.getCreatedUserHard())) {
          marketOld.setCreatedUserHard(null);
          resultInSideDto = mrCfgMarketRepository.add(marketOld);
          if (RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
            resultInSideDto = mrDeviceRepository.updateCreateUserByMarket(marketOld, "BDC");
          }
        } else if (!StringUtils.isStringNullOrEmpty(marketUpdate.getCreatedUserHard()) && !String
            .valueOf(marketUpdate.getCreatedUserHard())
            .equals(String.valueOf(marketOld.getCreatedUserHard()))) {
          marketOld.setCreatedUserHard(marketUpdate.getCreatedUserHard());
          resultInSideDto = mrCfgMarketRepository.add(marketOld);
          if (RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
            marketOld
                .setCreatedUserHardName(userRepository.getUserName(marketOld.getCreatedUserHard()));
            resultInSideDto = mrDeviceRepository.updateCreateUserByMarket(marketOld, "BDC");
          }
        } else if (!StringUtils.isStringNullOrEmpty(marketUpdate.getCreatedUserHard())
            && !StringUtils.isStringNullOrEmpty(marketOld.getCreatedUserHard()) && marketUpdate
            .getCreatedUserHard()
            .equals(marketOld.getCreatedUserHard())) {
          marketOld.setCreatedUserHard(marketUpdate.getCreatedUserHard());
          resultInSideDto = mrCfgMarketRepository.add(marketOld);
          if (RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
            marketOld
                .setCreatedUserHardName(userRepository.getUserName(marketOld.getCreatedUserHard()));
            resultInSideDto = mrDeviceRepository.updateCreateUserByMarket(marketOld, "BDC");
          }
        }
      }
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto updateListMarketSynItSoft(MrCfgMarketDTO mrCfgMarketDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    UserToken userToken = ticketProvider.getUserToken();
    List<MrCfgMarketDTO> lstMarketNew = mrCfgMarketDTO.getMrCfgMarketDTOList();
    if (lstMarketNew != null && lstMarketNew.size() > 0) {
      for (MrCfgMarketDTO marketUpdate : lstMarketNew) {
        MrCfgMarketDTO marketOld = mrCfgMarketRepository
            .findMrCfgMarketById(marketUpdate.getIdMarket());
        marketOld.setUpdatedTime(new Date());
        marketOld.setUpdatedUser(userToken.getUserName());
        if (StringUtils.isStringNullOrEmpty(marketUpdate.getCreatedUserItSoft()) && !StringUtils
            .isStringNullOrEmpty(marketOld.getCreatedUserItSoft())) {
          marketOld.setCreatedUserItSoft(null);
          resultInSideDto = mrCfgMarketRepository.add(marketOld);
          if (RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
            resultInSideDto = mrSynItHardDevicesRepository
                .updateCreateUserByMarket(marketOld, "BDM");
          }
        } else if (!StringUtils.isStringNullOrEmpty(marketUpdate.getCreatedUserItSoft()) && !String
            .valueOf(marketUpdate.getCreatedUserItSoft())
            .equals(String.valueOf(marketOld.getCreatedUserItSoft()))) {
          marketOld.setCreatedUserItSoft(marketUpdate.getCreatedUserItSoft());
          resultInSideDto = mrCfgMarketRepository.add(marketOld);
          if (RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
            marketOld
                .setCreatedUserSoftName(
                    userRepository.getUserName(marketOld.getCreatedUserItSoft()));
            resultInSideDto = mrSynItHardDevicesRepository
                .updateCreateUserByMarket(marketOld, "BDM");
          }
        } else if (!StringUtils.isStringNullOrEmpty(marketUpdate.getCreatedUserItSoft())
            && !StringUtils.isStringNullOrEmpty(marketOld.getCreatedUserItSoft()) && marketUpdate
            .getCreatedUserItSoft()
            .equals(marketOld.getCreatedUserItSoft())) {
          marketOld.setCreatedUserItSoft(marketUpdate.getCreatedUserItSoft());
          resultInSideDto = mrCfgMarketRepository.add(marketOld);
          if (RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
            marketOld
                .setCreatedUserSoftName(
                    userRepository.getUserName(marketOld.getCreatedUserItSoft()));
            resultInSideDto = mrSynItHardDevicesRepository
                .updateCreateUserByMarket(marketOld, "BDM");
          }
        }
      }
    }
    return resultInSideDto;
  }

  @Override
  public ResultInSideDto updateListMarketSynItHard(MrCfgMarketDTO mrCfgMarketDTO) {
    ResultInSideDto resultInSideDto = new ResultInSideDto();
    resultInSideDto.setKey(RESULT.SUCCESS);
    UserToken userToken = ticketProvider.getUserToken();
    List<MrCfgMarketDTO> lstMarketNew = mrCfgMarketDTO.getMrCfgMarketDTOList();
    if (lstMarketNew != null && lstMarketNew.size() > 0) {
      for (MrCfgMarketDTO marketUpdate : lstMarketNew) {
        MrCfgMarketDTO marketOld = mrCfgMarketRepository
            .findMrCfgMarketById(marketUpdate.getIdMarket());
        marketOld.setUpdatedTime(new Date());
        marketOld.setUpdatedUser(userToken.getUserName());
        if (StringUtils.isStringNullOrEmpty(marketUpdate.getCreatedUserItHard()) && !StringUtils
            .isStringNullOrEmpty(marketOld.getCreatedUserItHard())) {
          marketOld.setCreatedUserItHard(null);
          resultInSideDto = mrCfgMarketRepository.add(marketOld);
          if (RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
            resultInSideDto = mrSynItHardDevicesRepository
                .updateCreateUserByMarket(marketOld, "BDC");
          }
        } else if (!StringUtils.isStringNullOrEmpty(marketUpdate.getCreatedUserItHard()) && !String
            .valueOf(marketUpdate.getCreatedUserItHard())
            .equals(String.valueOf(marketOld.getCreatedUserItHard()))) {
          marketOld.setCreatedUserItHard(marketUpdate.getCreatedUserItHard());
          resultInSideDto = mrCfgMarketRepository.add(marketOld);
          if (RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
            marketOld
                .setCreatedUserHardName(
                    userRepository.getUserName(marketOld.getCreatedUserItHard()));
            resultInSideDto = mrSynItHardDevicesRepository
                .updateCreateUserByMarket(marketOld, "BDC");
          }
        } else if (!StringUtils.isStringNullOrEmpty(marketUpdate.getCreatedUserItHard())
            && !StringUtils.isStringNullOrEmpty(marketOld.getCreatedUserItHard()) && marketUpdate
            .getCreatedUserItHard()
            .equals(marketOld.getCreatedUserItHard())) {
          marketOld.setCreatedUserItHard(marketUpdate.getCreatedUserItHard());
          resultInSideDto = mrCfgMarketRepository.add(marketOld);
          if (RESULT.SUCCESS.equals(resultInSideDto.getKey())) {
            marketOld
                .setCreatedUserHardName(
                    userRepository.getUserName(marketOld.getCreatedUserItHard()));
            resultInSideDto = mrSynItHardDevicesRepository
                .updateCreateUserByMarket(marketOld, "BDC");
          }
        }
      }
    }
    return resultInSideDto;
  }


}
