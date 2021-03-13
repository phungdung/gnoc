package com.viettel.gnoc.mr.business;

import com.viettel.gnoc.commons.dto.ResultDTO;
import com.viettel.gnoc.commons.dto.ResultInSideDto;
import com.viettel.gnoc.commons.dto.UserTokenGNOC;
import com.viettel.gnoc.commons.model.UsersEntity;
import com.viettel.gnoc.maintenance.dto.MrCdBatteryDTO;
import com.viettel.gnoc.maintenance.dto.MrNodesDTO;
import com.viettel.gnoc.ws.dto.UserGroupCategoryDTO;
import java.util.List;

/**
 * @author Dunglv3
 */
public interface MrServiceBusiness {

  List<MrCdBatteryDTO> getListMrCdBattery(MrCdBatteryDTO dto);

  ResultInSideDto insertList(List<MrNodesDTO> listMrNodesDTO);

  UsersEntity getUserByUserId(Long userId);

  ResultDTO updateWoCodeMrCode(MrCdBatteryDTO dto);

  UserTokenGNOC getUserInfor(String userName);

  List<UserGroupCategoryDTO> getListUserGroupBySystem(UserGroupCategoryDTO dto);

}
