package com.viettel.gnoc.wo.model;


import com.viettel.gnoc.wo.dto.AutoCreateWoOsDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import java.util.Date;

import static javax.persistence.GenerationType.SEQUENCE;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "WFM", name = "LOG_AUTO_CREATE_WO_OS")
public class AutoCreateWoOsEntity {
  @Id
  @SequenceGenerator(name = "generator", sequenceName = "LOG_AUTO_CREATE_WO_OS_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "ID", unique = true)
  private Long id;

  @Column(name = "CD_CODE")
  private String cdCode;

  @Column(name = "CD_ID")
  private String cdId;

  @Column(name = "WO_TYPE_CODE")
  private String woTypeCode;

  @Column(name = "CONTENT")
  private String content;

  @Column(name = "CREATE_UPDATE_TIME")
  private Date createUpdateTime;

  @Column(name = "TYPE_ID")
  private String typeId;

  @Column(name = "RESULT")
  private String result;

  @Column(name = "ARRAY_FILE")
  private String arrayFile;

  public AutoCreateWoOsDTO toDTO() {
    return new AutoCreateWoOsDTO(id, cdCode, cdId, woTypeCode, content, createUpdateTime, typeId, result, arrayFile);
  }
}
