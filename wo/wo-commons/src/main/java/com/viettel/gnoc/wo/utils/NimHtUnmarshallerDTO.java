package com.viettel.gnoc.wo.utils;

import com.viettel.nims.webservice.ht.CheckPortSubDescriptionByWOForm;
import com.viettel.nims.webservice.ht.JsonResponseBO;
import com.viettel.nims.webservice.ht.ResultCheckStatusCabinet;
import com.viettel.nims.webservice.ht.ResultCheckStatusStations;
import com.viettel.nims.webservice.ht.ResultDesignForm;
import com.viettel.nims.webservice.ht.ResultGetDepartmentByLocationForm;
import java.util.List;


public interface NimHtUnmarshallerDTO {

  List<ResultGetDepartmentByLocationForm> getResultGetDepartmentByLocationForm();
  ResultDesignForm getResultDesignForm();
  JsonResponseBO getJsonResponseBO();
  CheckPortSubDescriptionByWOForm getCheckPortSubDescriptionByWOForm();
  ResultCheckStatusStations getResultCheckStatusStations();
  ResultCheckStatusCabinet getResultCheckStatusCabinet();
}
