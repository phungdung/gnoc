package com.viettel.gnoc.commons.business;

import com.viettel.gnoc.commons.dto.JsonResponseBO;
import com.viettel.gnoc.commons.dto.RequestInputBO;

public interface ActionLogBusiness {

  JsonResponseBO getDataJson(RequestInputBO requestInputBO);

}
