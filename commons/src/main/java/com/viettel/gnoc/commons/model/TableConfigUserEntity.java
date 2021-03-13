package com.viettel.gnoc.commons.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.commons.dto.TableConfigUserDTO;
import java.io.Serializable;
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

/**
 * @author tuanpv14
 * @version 1.0
 * @since 18/12/2015 11:36:38
 */
@Entity
@Table(schema = "COMMON_GNOC", name = "TABLE_CONFIG_USER")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TableConfigUserEntity implements Serializable {

  //Fields
  @Id
  @SequenceGenerator(name = "generator", sequenceName = "table_config_user_id_seq", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "TABLE_CONFIG_USER_ID", nullable = false)
  private Long tableConfigUserId;

  @Column(name = "USER_ID")
  private Long userId;

  @Column(name = "USER_NAME")
  private String userName;

  @Column(name = "HEADER_KEY")
  private String headerKey;

  @Column(name = "NOTE")
  private String note;

  @Column(name = "HEADER_CONFIG")
  private String headerConfig;

  public TableConfigUserDTO toDTO() {
    return new TableConfigUserDTO(
        tableConfigUserId, userId, userName, headerKey, note, headerConfig
    );
  }

}
