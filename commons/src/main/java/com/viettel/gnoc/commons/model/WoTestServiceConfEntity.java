package com.viettel.gnoc.commons.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.commons.dto.WoTestServiceConfDTO;
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
@Table(schema = "COMMON_GNOC", name = "WO_TEST_SERVICE_CONF")
public class WoTestServiceConfEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "WO_TEST_SERVICE_CONF_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "ID", unique = true)
  private Long id;

  @Column(name = "NAME")
  private String name;

  @Column(name = "WO_CONTENT")
  private String woContent;

  @Column(name = "WO_TYPE")
  private Long woType;

  @Column(name = "WO_PRIORITY")
  private Long woPriority;

  @Column(name = "FILE_ID")
  private Long fileId;

  @Column(name = "CD_ID")
  private Long cdId;

  @Column(name = "DELTA_TIME1")
  private Double deltaTime1;

  @Column(name = "DELTA_TIME2")
  private Double deltaTime2;

  @Column(name = "WO_PARENT_TYPE")
  private Long woParentType;


  public WoTestServiceConfDTO toDTO() {
    return new WoTestServiceConfDTO(id, name, woContent, woType, woPriority, fileId, cdId,
        deltaTime1,
        deltaTime2, woParentType);
  }
}
