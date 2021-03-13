
package com.viettel.gnoc.sr.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.sr.dto.SRStatusDTO;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(schema = "OPEN_PM", name = "SR_CONFIG")
public class SRStatusEntity {

  static final long serialVersionUID = 1L;

  //Fields
  @Id
  @SequenceGenerator(name = "generator", sequenceName = "SR_CONFIG_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "CONFIG_ID", nullable = false)
  private Long configId;

  @Column(name = "CONFIG_GROUP")
  private String configGroup;

  @Column(name = "CONFIG_CODE")
  private String configCode;

  @Column(name = "CONFIG_NAME")
  private String configName;

  @Column(name = "STATUS")
  private String status;

  @Column(name = "CREATED_USER")
  private String createdUser;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "CREATED_TIME")
  private Date createdTime;

  @Column(name = "UPDATED_USER")
  private String updatedUser;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "UPDATED_TIME")
  private Date updatedTime;

  public SRStatusDTO toDTO() {
    SRStatusDTO dto = new SRStatusDTO(
        configId
        , configGroup
        , configCode
        , configName
        , status
        , createdUser
        , createdTime
        , updatedUser
        , updatedTime
    );
    return dto;
  }
}

