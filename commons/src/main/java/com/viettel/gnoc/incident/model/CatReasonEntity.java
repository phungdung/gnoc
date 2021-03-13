package com.viettel.gnoc.incident.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.incident.dto.CatReasonInSideDTO;
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
 * @author ITSOL
 */

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CAT_REASON")
public class CatReasonEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "CAT_REASON_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "ID", unique = true, nullable = false)
  private Long id;
  @Column(name = "REASON_NAME", nullable = false)
  private String reasonName;
  @Column(name = "REASON_CODE")
  private String reasonCode;
  @Column(name = "PARENT_ID")
  private Long parentId;
  @Column(name = "DESCRIPTION")
  private String description;
  @Column(name = "TYPE_ID")
  private Long typeId;
  @Column(name = "REASON_TYPE")
  private Long reasonType;//
  @Temporal(javax.persistence.TemporalType.TIMESTAMP)
  @Column(name = "UPDATE_TIME")
  private Date updateTime;
  @Column(name = "IS_CHECK_SCRIPT")
  private Long isCheckScript;
  @Column(name = "is_update_closure")
  private Long isUpdateClosure;

  public CatReasonInSideDTO toDTO() {
    return new CatReasonInSideDTO(id, reasonName, reasonCode, parentId, description, typeId,
        reasonType, updateTime, isCheckScript, isUpdateClosure);
  }
}
