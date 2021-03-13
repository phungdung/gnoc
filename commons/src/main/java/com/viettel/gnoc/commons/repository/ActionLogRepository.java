package com.viettel.gnoc.commons.repository;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.commons.dto.RequestInputBO;
import java.util.List;
import java.util.Map;

public interface ActionLogRepository {

  BaseDto getSQLByCode(RequestInputBO requestInputBO);

  List<Map<String, Object>> getDataFromSQL(String sql, Map<String, Object> params);
}
