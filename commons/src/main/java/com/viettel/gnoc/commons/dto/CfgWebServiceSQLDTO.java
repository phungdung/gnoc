package com.viettel.gnoc.commons.dto;

import com.viettel.gnoc.commons.model.CfgWebServiceSQLEntity;
import com.viettel.gnoc.commons.validator.MultiFieldUnique;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
@MultiFieldUnique(message = "{validation.CfgWebServiceSQL.config.unique}", clazz = CfgWebServiceSQLEntity.class, uniqueFields = "code,system", idField = "id")
public class CfgWebServiceSQLDTO extends BaseDto {

  private Long id;

  private String code;

  private String sqlText;

  private String system;

  private Long status;

  private Date createTime;

  private Date updateTime;

  private String formatDate;

  private Long writeLog;

  private String columnKey;

  public CfgWebServiceSQLEntity toEntity() {
    CfgWebServiceSQLEntity model = new CfgWebServiceSQLEntity(
        this.id,
        this.code,
        this.sqlText,
        this.system,
        this.status,
        this.createTime,
        this.updateTime,
        this.formatDate,
        this.writeLog,
        this.columnKey
    );
    return model;
  }

}
