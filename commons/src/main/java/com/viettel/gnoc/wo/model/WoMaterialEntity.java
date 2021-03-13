
package com.viettel.gnoc.wo.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.wo.dto.WoMaterialDTO;
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
@Table(schema = "wfm", name = "WO_MATERIAL")
public class WoMaterialEntity {

  static final long serialVersionUID = 1L;

  //Fields
  @Id
  @SequenceGenerator(name = "generator", sequenceName = "womaterial_seq", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "MATERIAL_ID", nullable = false)
  private Long materialId;

  @Column(name = "MATERIAL_GROUP_CODE")
  private String materialGroupCode;

  @Column(name = "MATERIAL_CODE")
  private String materialCode;

  @Column(name = "MATERIAL_NAME")
  private String materialName;

  @Column(name = "MATERIAL_GROUP_NAME")
  private String materialGroupName;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "UPDATE_TIME")
  private Date updateTime;

  @Column(name = "UNIT_ID")
  private Long unitId;

  @Column(name = "UNIT_NAME")
  private String unitName;

  @Column(name = "IS_ENABLE")
  private Long isEnable;

  @Column(name = "NATION_CODE")
  private String nationCode;

  @Column(name = "NATION_ID")
  private Long nationId;


  public WoMaterialDTO toDTO() {
    WoMaterialDTO dto = new WoMaterialDTO(
        materialId
        , materialGroupCode
        , materialCode
        , materialName
        , materialGroupName
        , updateTime
        , unitId
        , unitName
        , isEnable
        , nationCode
        , nationId
    );
    return dto;
  }
}

