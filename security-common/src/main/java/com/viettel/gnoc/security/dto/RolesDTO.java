
package com.viettel.gnoc.security.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RolesDTO {
	//Fields
    private String roleId;
    private String status;
    private String roleName;
    private String description;
    private String roleCode;

}

