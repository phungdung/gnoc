package com.viettel.gnoc.mr.business;

import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UserTokenGNOC;
import com.viettel.gnoc.commons.model.UsersEntity;
import com.viettel.gnoc.maintenance.dto.MrCdBatteryDTO;
import com.viettel.gnoc.maintenance.dto.MrNodesDTO;
import com.viettel.gnoc.mr.repository.MrServiceRepository;
import com.viettel.gnoc.ws.dto.UserGroupCategoryDTO;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j

/**
 *
 * @author Dunglv3
 */
public class MrServiceBusinessImpl implements MrServiceBusiness {

  @Autowired
  MrServiceRepository mrServiceRepository;

  @Override
  public List<MrCdBatteryDTO> getListMrCdBattery(MrCdBatteryDTO dto) {
    return mrServiceRepository.getListMrCdBattery(dto);
  }

  @Override
  public ResultInSideDto insertList(List<MrNodesDTO> listMrNodesDTO) {
    return mrServiceRepository.insertList(listMrNodesDTO);
  }

  @Override
  public UsersEntity getUserByUserId(Long userId) {
    return mrServiceRepository.getUserByUserId(userId);
  }

  @Override
  public ResultDTO updateWoCodeMrCode(MrCdBatteryDTO dto) {
    return mrServiceRepository.updateWoCodeMrCode(dto);
  }

  @Override
  public UserTokenGNOC getUserInfor(String userName) {
    return mrServiceRepository.getUserInfor(userName);
  }

  @Override
  public List<UserGroupCategoryDTO> getListUserGroupBySystem(UserGroupCategoryDTO dto) {
    return mrServiceRepository.getListUserGroupBySystem(dto);
  }
}
