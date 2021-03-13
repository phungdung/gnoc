package com.viettel.gnoc.security.business;

import com.viettel.gnoc.security.dto.RolesDTO;
import com.viettel.gnoc.security.dto.UsersDto;
import com.viettel.gnoc.security.repository.UserRepository;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author TungPV
 */
@Service
@Transactional
@Slf4j
public class UserBusinessImpl implements UserBusiness {
  @Autowired
  protected UserRepository userRepository;

  @Override
  public UsersDto getUserDTOByUserName(String userName) {
    return userRepository.getUserDTOByUserName(userName);
  }

  @Override
  public List<RolesDTO> getRolesByUser(Long userId) {
    return userRepository.getRolesByUser(userId);
  }

  @Override
  public Map<String, String> getConfigProperty() {
    return userRepository.getConfigProperty();
  }

}
