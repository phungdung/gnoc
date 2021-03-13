package com.viettel.gnoc.od.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.od.dto.OdTypeDTO;
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

/**
 * @author NamTN
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "WFM", name = "OD_TYPE")
public class OdTypeEntity {

  static final long serialVersionUID = 1L;

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "OD_TYPE_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "OD_TYPE_ID", unique = true)
  private Long odTypeId;

  @Column(name = "OD_TYPE_CODE", nullable = false)
  @Nationalized
  private String odTypeCode;

  @Column(name = "OD_TYPE_NAME", nullable = false)
  @Nationalized
  private String odTypeName;

  @Column(name = "STATUS")
  private Long status;

  @Column(name = "OD_GROUP_TYPE_ID")
  private Long odGroupTypeId;

  public OdTypeDTO toDTO() {
    return new OdTypeDTO(odTypeId, odTypeCode, odTypeName, status, odGroupTypeId);
  }
}
