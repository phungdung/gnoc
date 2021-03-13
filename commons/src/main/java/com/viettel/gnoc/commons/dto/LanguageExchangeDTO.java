/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.viettel.gnoc.commons.dto;

import com.viettel.gnoc.commons.model.LanguageExchangeEntity;
import com.viettel.gnoc.commons.validator.MultiFieldUnique;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author tuanpv14
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MultiFieldUnique(message = "{validation.languageExchange.null.unique}", clazz = LanguageExchangeEntity.class, uniqueFields = "appliedSystem,appliedBussiness,bussinessId,leeLocale", idField = "leeId")
public class LanguageExchangeDTO extends BaseDto {

  private Long leeId;
  private Long appliedSystem;
  private Long appliedBussiness;
  private Long bussinessId;
  private String bussinessCode;
  private String leeLocale;
  private String leeValue;

  private Long indexFile;
  private String fileName;
  private String filePath;
  private MultipartFile file;
  private String leeLocaleName;
  private String leeLocaleFlag;
  private String appliedBusinessName;
  private String appliedSystemName;
  private String appliedSystemCode;
  private String appliedBussinessCode;
  private String resultImport;
  private String actionName;
  private String action;
  private String businessIdImport;
  private String filePathNew;

  public LanguageExchangeDTO(Long leeId, Long appliedSystem, Long appliedBussiness,
      Long bussinessId, String bussinessCode, String leeLocale, String leeValue) {
    this.leeId = leeId;
    this.appliedSystem = appliedSystem;
    this.appliedBussiness = appliedBussiness;
    this.bussinessId = bussinessId;
    this.bussinessCode = bussinessCode;
    this.leeLocale = leeLocale;
    this.leeValue = leeValue;
  }

  public LanguageExchangeEntity toEntity() {
    LanguageExchangeEntity model = new LanguageExchangeEntity(
        leeId,
        appliedSystem,
        appliedBussiness,
        bussinessId,
        bussinessCode,
        leeLocale,
        leeValue);
    return model;
  }
}
