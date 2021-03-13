
package com.viettel.gnoc.wo.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.wo.dto.CfgFileCreateWoDTO;
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

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(schema = "WFM", name = "CFG_FILE_CREATE_WO")
public class CfgFileCreateWoEntity {

  static final long serialVersionUID = 1L;

  //Fields
  @Id
  @SequenceGenerator(name = "generator", sequenceName = "CFG_FILE_CREATE_WO_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "CFG_FILE_CREATE_WO_ID", nullable = false)
  private Long cfgFileCreateWoId;

  @Column(name = "WO_TYPE_ID")
  private Long woTypeId;

  @Column(name = "FILE_NAME")
  private String fileName;

  @Column(name = "REQUIRED")
  private Long required;

  @Column(name = "FILE_PATH")
  private String filePath;


  public CfgFileCreateWoDTO toDTO() {
    CfgFileCreateWoDTO dto = new CfgFileCreateWoDTO(
        cfgFileCreateWoId
        , woTypeId
        , fileName
        , required
        , filePath
    );
    return dto;
  }
}

