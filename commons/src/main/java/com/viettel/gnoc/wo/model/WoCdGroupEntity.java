package com.viettel.gnoc.wo.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.wo.dto.WoCdGroupInsideDTO;
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
@Table(schema = "WFM", name = "WO_CD_GROUP")
public class WoCdGroupEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "WFM.WO_CDE_GROUP_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "WO_GROUP_ID", unique = true, nullable = false)
  private Long woGroupId;

  @Column(name = "WO_GROUP_CODE", nullable = false)
  private String woGroupCode;

  @Column(name = "WO_GROUP_NAME", nullable = false)
  private String woGroupName;

  @Column(name = "EMAIL")
  private String email;

  @Column(name = "MOBILE")
  private String mobile;

  @Column(name = "GROUP_TYPE_ID")
  private Long groupTypeId;

  @Column(name = "IS_ENABLE")
  private Long isEnable;

  @Column(name = "NATION_ID")
  private Long nationId;

  public WoCdGroupInsideDTO toDTO() {
    return new WoCdGroupInsideDTO(woGroupId, woGroupCode, woGroupName, email, mobile, groupTypeId,
        isEnable, nationId);
  }

}
