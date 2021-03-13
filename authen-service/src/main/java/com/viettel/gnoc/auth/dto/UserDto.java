package com.viettel.gnoc.auth.dto;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import viettel.passport.client.UserToken;

@Getter
@Setter
public class UserDto extends UserToken implements Serializable {

  private static final long serialVersionUID = 1L;
}
