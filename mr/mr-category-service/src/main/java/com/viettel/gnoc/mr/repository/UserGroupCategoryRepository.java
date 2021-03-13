package com.viettel.gnoc.mr.repository;

import com.viettel.gnoc.wo.dto.UserGroupCategoryDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface UserGroupCategoryRepository {

  List<UserGroupCategoryDTO> getListUserGroupBySystem(UserGroupCategoryDTO dto);
}
