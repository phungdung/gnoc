package com.viettel.gnoc.commons.dto;

import com.viettel.gnoc.commons.model.GnocLanguageEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GnocLanguageDto extends BaseDto {

  private Long gnocLanguageId;
  private String languageKey;
  private String languageName;
  private String languageFlag;
  private Boolean isActive;

  public GnocLanguageDto(Long gnocLanguageId, String languageKey, String languageName,
      String languageFlag) {
    this.gnocLanguageId = gnocLanguageId;
    this.languageKey = languageKey;
    this.languageName = languageName;
    this.languageFlag = languageFlag;
  }

  public GnocLanguageEntity toEntity() {
    return new GnocLanguageEntity(gnocLanguageId, languageKey, languageName, languageFlag);
  }
}
