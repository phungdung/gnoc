package com.viettel.gnoc.wo.utils;

import com.viettel.nims.webservice.ht.CheckPortSubDescriptionByWOForm;
import com.viettel.nims.webservice.ht.JsonResponseBO;
import com.viettel.nims.webservice.ht.ResultCheckStatusCabinet;
import com.viettel.nims.webservice.ht.ResultCheckStatusStations;
import com.viettel.nims.webservice.ht.ResultDesignForm;
import com.viettel.nims.webservice.ht.ResultGetDepartmentByLocationForm;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "nimHtUnmarshallerDTO")
public class ResultCheckStatusCabinetUnmarshallerDTO implements NimHtUnmarshallerDTO {

  @XmlElement(name = "return")
  ResultCheckStatusCabinet resultCheckStatusCabinet;

  @Override
  public List<ResultGetDepartmentByLocationForm> getResultGetDepartmentByLocationForm() {
    return null;
  }

  @Override
  public ResultDesignForm getResultDesignForm() {
    return null;
  }

  @Override
  public JsonResponseBO getJsonResponseBO() {
    return null;
  }

  @Override
  public CheckPortSubDescriptionByWOForm getCheckPortSubDescriptionByWOForm() {
    return null;
  }

  @Override
  public ResultCheckStatusStations getResultCheckStatusStations() {
    return null;
  }

  @Override
  public ResultCheckStatusCabinet getResultCheckStatusCabinet() {
    return resultCheckStatusCabinet;
  }
}
