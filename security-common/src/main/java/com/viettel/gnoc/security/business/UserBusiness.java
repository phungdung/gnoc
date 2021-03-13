package com.viettel.gnoc.security.business;


import com.viettel.gnoc.security.dto.RolesDTO;
import com.viettel.gnoc.security.dto.UsersDto;
import java.util.List;
import java.util.Map;


/**
 * @author TungPV
 */
public interface UserBusiness {

  UsersDto getUserDTOByUserName(String userName);
  List<RolesDTO> getRolesByUser(Long userId);
  Map<String, String> getConfigProperty();
}
