package com.viettel.gnoc.cr.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.commons.utils.Constants;
import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.cr.dto.CrCableDTO;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(schema = "OPEN_PM", name = "CR_CABLE")
public class CrCableEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "CR_CABLE_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "ID", nullable = false)
  private Long id;

  @Column(name = "CR_ID")
  private Long crId;

  @Column(name = "CABLE_CODE")
  private String cableCode;

  @Column(name = "START_POINT")
  private String startPoint;

  @Column(name = "END_POINT")
  private String endPoint;

  @Column(name = "NATION_CODE")
  private String nationCode;

  @Column(name = "CREATED_DATE")
  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  private Date createdDate;

  @Column(name = "TYPE")
  private Long type;

  public CrCableEntity(Long id, Long crId, String cableCode, String startPoint, String endPoint,
      String nationCode, Date createdDate, Long type) {
    this.id = id;
    this.crId = crId;
    this.cableCode = cableCode;
    this.startPoint = startPoint;
    this.endPoint = endPoint;
    this.nationCode = nationCode;
    this.createdDate = createdDate;
    this.type = type;
  }

  public CrCableDTO toDTO() {
    CrCableDTO dto = new CrCableDTO(
        id == null ? null : id.toString(),
        crId == null ? null : crId.toString(),
        cableCode,
        startPoint,
        endPoint,
        nationCode,
        createdDate == null ? null : DateTimeUtils
            .convertDateToString(createdDate, Constants.ddMMyyyyHHmmss),
        type == null ? null : type.toString()
    );
    return dto;
  }
}
