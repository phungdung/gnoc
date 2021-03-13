package com.viettel.gnoc.maintenance.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.maintenance.dto.MrITSoftCrImplUnitDTO;
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
@Table(schema = "OPEN_PM", name = "MR_CFG_CR_IMPL_UNIT")
public class MrITSoftCrImplUnitEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "MR_CFG_CR_IMPL_UNIT_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "CFG_ID", unique = true, nullable = false)
  private Long cfgId;

  @Column(name = "MARKET_CODE", nullable = false)
  private String marketCode;

  @Column(name = "ARRAY_CODE", nullable = false)
  private String arrayCode;

  @Column(name = "DEVICE_TYPE_ID", nullable = false)
  private String deviceTypeId;

  @Column(name = "IMPLEMENT_UNIT", nullable = false)
  private Long implementUnit;

  @Column(name = "CHECKING_UNIT", nullable = false)
  private Long checkingUnit;

  @Column(name = "CREATE_USER")
  private String createUser;

  @Column(name = "APPROVE_USER_LEVEL1")
  private String approveUserLv1;

  @Column(name = "APPROVE_USER_LEVEL2")
  private String approveUserLv2;

  @Column(name = "CREATE_WO", nullable = false)
  private Long isCreateWO;

  @Column(name = "MANAGE_UNIT", nullable = false)
  private Long manageUnit;

  @Column(name = "REGION")
  private String region;


  public MrITSoftCrImplUnitDTO toDTO() {
    return new MrITSoftCrImplUnitDTO(
        cfgId,
        marketCode,
        arrayCode,
        deviceTypeId,
        implementUnit == null ? null : implementUnit.toString(),
        checkingUnit == null ? null : checkingUnit.toString(),
        createUser,
        approveUserLv1,
        approveUserLv2,
        isCreateWO,
        manageUnit == null ? null : manageUnit.toString(),
        region);
  }

}
