package com.viettel.gnoc.cr.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.cr.dto.MethodParameterDTO;
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
@Table(schema = "OPEN_PM", name = "METHOD_PARAMETER")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MethodParameterEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "METHOD_PARAMETER_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "METHOD_PARAMETER_ID", nullable = false)
  private Long methodParameterId;
  @Column(name = "DATE_TYPE", nullable = true)
  private Long dateType;
  @Column(name = "WEBSERVICE_METHOD_ID", nullable = true)
  private Long webserviceMethodId;
  @Column(name = "PARAMETER_NAME", nullable = false)
  private String parameterName;

  public MethodParameterDTO toDTO() {
    MethodParameterDTO dto = new MethodParameterDTO(
        methodParameterId,
        dateType,
        webserviceMethodId,
        parameterName
    );
    return dto;
  }
}
