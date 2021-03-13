package com.viettel.gnoc.sr.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.sr.dto.SRConfigDTO;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * @author KienPV
 */
@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(schema = "OPEN_PM", name = "SR_CONFIG")
public class SRConfigEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "open_pm.SR_CONFIG_seq", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "CONFIG_ID", unique = true, nullable = false)
  private Long configId;

  @Column(name = "CONFIG_GROUP")
  private String configGroup;

  @Column(name = "CONFIG_CODE")
  private String configCode;

  @Column(name = "CONFIG_NAME")
  private String configName;

  @Column(name = "CREATED_USER")
  private String createdUser;

  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "CREATED_TIME")
  private Date createdTime;

  @Column(name = "UPDATED_USER")
  private String updatedUser;

  @Column(name = "UPDATED_TIME")
  @Temporal(javax.persistence.TemporalType.DATE)
  private Date updatedTime;

  @Column(name = "STATUS")
  private String status;

  @Column(name = "PARENT_GROUP")
  private String parentGroup;

  @Column(name = "PARENT_CODE")
  private String parentCode;

  @Column(name = "COUNTRY")
  private Long country;

  @Column(name = "AUTO_MATION")
  private String automation;

  @Column(name = "SR_CFG_SERVICE_IDS")
  private String srCfgServiceIds;

  public SRConfigDTO toDTO() {
    SRConfigDTO dto = new SRConfigDTO(
        configId,
        configGroup,
        configCode,
        configName,
        createdUser,
        createdTime == null ? null
            : DateTimeUtils.convertDateToString(createdTime, Constants.ddMMyyyyHHmmss),
        updatedUser,
        updatedTime == null ? null
            : DateTimeUtils.convertDateToString(updatedTime, Constants.ddMMyyyyHHmmss),
        status,
        parentGroup,
        parentCode,
        country == null ? null : String.valueOf(country),
        automation,
        srCfgServiceIds
    );
    return dto;
  }

}
