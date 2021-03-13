package com.viettel.gnoc.maintenance.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.maintenance.dto.MrUngCuuTTDTO;
import java.util.Date;
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
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "OPEN_PM", name = "MR_UCTT")
public class MrUngCuuTTEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "OPEN_PM.MR_UCTT_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "UCTT_ID", unique = true, nullable = false)
  private Long ucttId;
  @Column(name = "CD_ID", nullable = false)
  private Long cdId;
  @Column(name = "TITLE", nullable = false)
  private String title;
  @Column(name = "DESCRIPTION", nullable = false)
  private String description;
  @Column(name = "START_DATE", unique = true, nullable = false)
  private Date startDate;

  @Column(name = "END_DATE", unique = true, nullable = false)
  private Date endDate;
  @Column(name = "CREATED_DATE", unique = true, nullable = false)
  private Date createdDate;

  @Column(name = "CREATED_USER", nullable = false)
  private String createdUser;

  @Column(name = "UPDATED_DATE", unique = true, nullable = false)
  private Date updatedDate;

  @Column(name = "UPDATED_USER", nullable = false)
  private String updatedUser;

  @Column(name = "WO_CODE", nullable = false)
  private String woCode;

  public MrUngCuuTTDTO toDTO() {
    return new MrUngCuuTTDTO(
        ucttId == null ? null : ucttId.toString(),
        cdId == null ? null : cdId.toString(),
        title, description,
        startDate,
        endDate,
        createdDate, createdUser, updatedDate, updatedUser, woCode);
  }
}
