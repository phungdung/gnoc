package com.viettel.gnoc.security.repository;

import com.viettel.gnoc.security.dto.RolesDTO;
import com.viettel.gnoc.security.dto.UsersDto;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

/**
 * @author TungPV
 */
@Repository
public interface UserRepository {

  UsersDto getUserDTOByUserName(String userName);
  List<RolesDTO> getRolesByUser(Long userId);
  Map<String, String> getConfigProperty();
}
