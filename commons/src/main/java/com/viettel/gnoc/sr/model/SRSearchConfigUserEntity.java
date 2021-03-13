package com.viettel.gnoc.sr.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.sr.dto.SRSearchConfigUserDTO;
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
@Table(schema = "OPEN_PM", name = "SR_SEARCH_CONFIG_USER")
public class SRSearchConfigUserEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "SR_SEARCH_CONFIG_USER_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "SEARCH_CONFIG_USER_ID")
  private Long searchConfigUserId;
  @Column(name = "FUNCTION_KEY")
  private String functionKey;
  @Column(name = "FIELD_VALUE")
  private String fieldValue;
  @Column(name = "USER_ID")
  private Long userId;
  @Column(name = "USER_NAME")
  private String userName;
  @Column(name = "FIELD_NAME")
  private String fieldName;
  @Column(name = "DATA_TYPE")
  private String dataType;
  @Column(name = "INDEX_COLUMN")
  private Long indexColumn;

  public SRSearchConfigUserDTO toDTO() {
    return new SRSearchConfigUserDTO(searchConfigUserId, functionKey, fieldValue, userId, userName,
        fieldName, dataType, indexColumn);
  }
}
