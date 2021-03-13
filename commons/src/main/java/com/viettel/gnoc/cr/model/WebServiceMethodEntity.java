package com.viettel.gnoc.cr.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.cr.dto.WebServiceMethodDTO;
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
@Table(schema = "OPEN_PM", name = "WEBSERVICE_METHOD")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class WebServiceMethodEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "WEBSERVICE_METHOD_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "WEBSERVICE_METHOD_ID", nullable = false)
  private Long webServiceMethodId;
  @Column(name = "WEBSERVICE_ID", nullable = false)
  private Long webServiceId;
  @Column(name = "METHOD_NAME", nullable = false)
  private String methodName;
  @Column(name = "SUCESS_RETURN_VALUE", nullable = true)
  private Long sucessReturnValue;
  @Column(name = "CLASS_PATH", nullable = true)
  private String classPath;
  @Column(name = "ID_FIELD", nullable = true)
  private String idField;
  @Column(name = "RETURN_VALUE_FIELD", nullable = true)
  private String returnValueField;
  @Column(name = "IS_MERGE", nullable = true)
  private Long isMerge;

  public WebServiceMethodDTO toDTO() {
    return new WebServiceMethodDTO(
        webServiceMethodId,
        webServiceId,
        methodName,
        sucessReturnValue,
        classPath,
        idField,
        returnValueField,
        isMerge
    );
  }
}
