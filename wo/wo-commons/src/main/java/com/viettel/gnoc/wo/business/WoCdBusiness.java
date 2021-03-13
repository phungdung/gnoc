package com.viettel.gnoc.wo.business;

import com.viettel.gnoc.commons.dto.UsersInsideDto;
import java.util.List;

public interface WoCdBusiness {

  List<UsersInsideDto> getListCdByGroup(Long woGroupId);
}
