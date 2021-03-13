package com.viettel.gnoc.commons.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.commons.dto.SearchConfigUserDTO;
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
 * @since 28/12/2015 08:39:40
 */
@Entity
@Table(schema = "COMMON_GNOC", name = "SEARCH_CONFIG_USER")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchConfigUserEntity implements Serializable {

  //Fields
  @Id
  @SequenceGenerator(name = "generator", sequenceName = "SEARCH_CONFIG_USER_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "SEARCH_CONFIG_USER_ID", nullable = false)
  private Long searchConfigUserId;

  @Column(name = "USER_ID")
  private Long userId;

  @Column(name = "USER_NAME")
  private String userName;

  @Column(name = "FUNC_KEY")
  private String funcKey;

  @Column(name = "FIELD_NAME")
  private String fieldName;

  @Column(name = "DATA_TYPE")
  private String dataType;

  @Column(name = "FIELD_VALUE")
  private String fieldValue;

  @Column(name = "FIELD_INDEX")
  private Long fieldIndex;

  public SearchConfigUserDTO toDTO() {
    return new SearchConfigUserDTO(
        searchConfigUserId, userId, userName, funcKey, fieldName, dataType, fieldValue, fieldIndex
    );
  }
}
