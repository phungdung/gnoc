package com.viettel.gnoc.cr.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.cr.dto.WebServiceDTO;
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
@Table(schema = "OPEN_PM", name = "WEBSERVICE")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WebServiceEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "WEBSERVICE_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "WEBSERVICE_ID", nullable = false)
  private Long webServiceId;
  @Column(name = "WEBSERVICE_NAME", nullable = false)
  private String webServiceName;
  @Column(name = "URL", nullable = false)
  private String url;
  @Column(name = "NAMESPACE_URI", nullable = false)
  private String nameSpaceURI;
  @Column(name = "LOCALPART", nullable = false)
  private String localPart;
  @Column(name = "WEBSERVICE_CLASS_PATH", nullable = false)
  private String webServiceClassPath;
  @Column(name = "GET_PORT_METHOD", nullable = false)
  private String getPortMethod;
  @Column(name = "USER_NAME", nullable = true)
  private String userName;
  @Column(name = "PASSWORD", nullable = true)
  private String password;
  @Column(name = "IS_ACTIVE", nullable = true)
  private Long isActive;
  @Column(name = "IS_EDITABLE", nullable = true)
  private Long isEditable;

  public WebServiceDTO toDTO() {
    return new WebServiceDTO(
        webServiceId,
        webServiceName,
        url,
        nameSpaceURI,
        localPart,
        webServiceClassPath,
        getPortMethod,
        userName,
        password,
        isActive,
        isEditable
    );
  }
}
