package com.viettel.gnoc.cr.model;

import static javax.persistence.GenerationType.SEQUENCE;

import com.viettel.gnoc.commons.utils.DateTimeUtils;
import com.viettel.gnoc.cr.dto.CfgChildArrayDTO;
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
import lombok.extern.slf4j.Slf4j;

/**
 * @author DungPV
 */
@Entity
@Table(schema = "COMMON_GNOC", name = "CFG_CHILD_ARRAY")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class CfgChildArrayEntity {

  @Id
  @SequenceGenerator(name = "generator", sequenceName = "COMMON_GNOC.CFG_CHILD_ARRAY_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "CHILDREN_ID", nullable = false)
  private Long childrenId;
  @Column(name = "PARENT_ID", nullable = false)
  private Long parentId;
  @Column(name = "CHILDREN_CODE", nullable = false)
  private String childrenCode;
  @Column(name = "CHILDREN_NAME", nullable = false)
  private String childrenName;
  @Column(name = "STATUS", nullable = false)
  private Long status;
  @Column(name = "UPDATED_USER", nullable = false)
  private String updatedUser;
  @Column(name = "UPDATED_TIME", nullable = false)
  private Date updatedTime;

  public CfgChildArrayDTO toDTO() {
    return new CfgChildArrayDTO(childrenId, parentId, childrenName, childrenCode,
        status, updatedUser, updatedTime == null ? null : DateTimeUtils
        .convertDateToString(updatedTime, "dd/MM/yyyy HH:mm:ss"));
  }
}
