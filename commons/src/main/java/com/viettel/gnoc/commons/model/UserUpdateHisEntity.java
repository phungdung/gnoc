package com.viettel.gnoc.commons.model;

import com.viettel.gnoc.commons.dto.UserUpdateHisDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

import static javax.persistence.GenerationType.SEQUENCE;

/**
 * @author tuanpv14
 * @version 1.0
 * @since 8/27/2015 5:34 PM
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "COMMON_GNOC", name = "USER_UPDATE_HIS")
public class UserUpdateHisEntity {

  //Fields
  @Id
  @SequenceGenerator(name = "generator", sequenceName = "USER_UPDATE_HIS_SEQ", allocationSize = 1)
  @GeneratedValue(strategy = SEQUENCE, generator = "generator")
  @Column(name = "ID", unique = true)
  private Long id;

  @Column(name = "USER_ACTION")
  private Long userAction;

  @Column(name = "USER_ID")
  private Long userId;

  @Column(name = "ACTION_NAME")
  private String actionName;

  @Column(name = "CONTENT")
  private String content;

  @Column(name= "UPDATE_TIME")
  private Date updateTime;

  public UserUpdateHisDTO toDTO() {
    return new UserUpdateHisDTO(id, userAction, userId, actionName, content, updateTime);
  }
}

