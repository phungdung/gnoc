package com.viettel.gnoc.commons.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.commons.dto.GnocLanguageDto;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "COMMON_GNOC", name = "GNOC_LANGUAGE")
public class GnocLanguageEntity {

  static final long serialVersionUID = 1L;
  @Id
  @SequenceGenerator(name = "generator", sequenceName = "GNOC_LANGUAGE_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "GNOC_LANGUAGE_ID", unique = true)
  private Long gnocLanguageId;

  @Column(name = "LANGUAGE_KEY")
  private String languageKey;

  @Column(name = "LANGUAGE_NAME")
  private String languageName;

  @Column(name = "LANGUAGE_FLAG")
  private String languageFlag;

  public GnocLanguageDto toDTO() {
    return new GnocLanguageDto(gnocLanguageId, languageKey, languageName, languageFlag);
  }
}
