package com.viettel.gnoc.commons.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.commons.dto.CfgWhiteListIpDTO;
import java.util.Date;
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
@Table(schema = "COMMON_GNOC", name = "CFG_WHITE_LIST_IP")
public class CfgWhiteListIpEntity {

  static final long serialVersionUID = 1L;
  @Id
  @SequenceGenerator(name = "generator", sequenceName = "COMMON_GNOC.CFG_WHITE_LIST_IP_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "ID", unique = true)
  private Long id;

  @Column(name = "USER_NAME")
  private String userName;

  @Column(name = "IP")
  private String ip;

  @Column(name = "SYSTEM_NAME")
  private String systemName;

  @Column(name = "CREATE_DATE")
  private Date createDate;

  @Column(name = "STATUS")
  private Long status;

  public CfgWhiteListIpDTO toDTO() {
    return new CfgWhiteListIpDTO(id, userName, ip, systemName, createDate, status);
  }
}
