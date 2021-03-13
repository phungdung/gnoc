package com.viettel.gnoc.commons.dto;

import com.viettel.gnoc.commons.utils.ConditionBean;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WSNocprov4DTO {

  private List<ConditionBean> lstCondition;
  private int rowStart;
  private int maxRow;
  private String sortType;
  private String sortFieldList;


}
