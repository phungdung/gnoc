package com.viettel.gnoc.commons.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.commons.dto.GnocTimezoneDto;
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
import org.hibernate.annotations.Nationalized;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "COMMON_GNOC", name = "GNOC_TIMEZONE")
public class GnocTimezoneEntity {

  static final long serialVersionUID = 1L;
  @Id
  @SequenceGenerator(name = "generator", sequenceName = "GNOC_TIMEZONE_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "GNOC_TIMEZONE_ID", unique = true)
  private Long gnocTimezoneId;

  @Column(name = "TIMEZONE_CODE")
  @Nationalized
  private String timezoneCode;

  @Column(name = "TIMEZONE_NAME")
  @Nationalized
  private String timezoneName;

  @Column(name = "NATION_CODE")
  @Nationalized
  private String nationCode;

  @Column(name = "TIMEZONE_OFFSET", precision = 22, scale = 2)
  private Double timezoneOffset;

  public GnocTimezoneDto toDTO() {
    return new GnocTimezoneDto(gnocTimezoneId, timezoneCode, timezoneName, nationCode,
        timezoneOffset);
  }
}
