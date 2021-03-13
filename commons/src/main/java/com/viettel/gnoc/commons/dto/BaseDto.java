package com.viettel.gnoc.commons.dto;

import com.viettel.gnoc.commons.utils.ConditionBean;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author TungPV
 */
@Getter
@Setter
@NoArgsConstructor
public class BaseDto {

  private Integer page;
  private Integer pageSize;
  private String sortName;
  private String sortType;
  private Integer totalRow;
  private Integer indexRow;
  private String langKey;
  private String timeOffset;
  private String sqlQuery;
  private List<Long> listId;
  private Map<String, Object> parameters;
  private String searchAll;
  private String proxyLocale;
  private List<ConditionBean> lstCondition;

  public BaseDto(List<ConditionBean> lstCondition, Integer page, Integer pageSize, String sortType,
      String sortName) {
    this.lstCondition = lstCondition;
    this.page = page;
    this.pageSize = pageSize;
    this.sortType = sortType;
    this.sortName = sortName;
  }
}
