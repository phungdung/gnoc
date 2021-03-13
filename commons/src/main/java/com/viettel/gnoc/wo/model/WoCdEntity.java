package com.viettel.gnoc.wo.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.wo.dto.WoCdDTO;
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
@Table(schema = "WFM", name = "WO_CD")
public class WoCdEntity {

  static final long serialVersionUID = 1L;

  @Column(name = "WO_GROUP_ID")
  private Long woGroupId;

  @Column(name = "USER_ID")
  private Long userId;

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "WO_CD_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "CD_ID", unique = true, nullable = false)
  private Long cdId;

  public WoCdDTO toDTO() {
    return new WoCdDTO(woGroupId, userId, cdId);
  }

}
