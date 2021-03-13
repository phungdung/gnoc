package com.viettel.gnoc.wo.dto;

import com.viettel.gnoc.commons.dto.BaseDto;
import com.viettel.gnoc.wo.model.WoConfigPropertyEntity;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WoConfigPropertyDTO extends BaseDto {

  @Size(max = 100, message = "{validation.woConfigProperty.key.tooLong}")
  @NotEmpty(message = "{validation.woConfigProperty.null.key}")
  @Pattern(regexp = "^([a-zA-Z0-9_.]{1,200})?$", message = "{validation.woConfigProperty.key.pattern}")
  private String key;

  @NotEmpty(message = "{validation.woConfigProperty.null.value}")
  private String value;

  @NotEmpty(message = "{validation.woConfigProperty.null.description}")
  private String description;

  public WoConfigPropertyEntity toEntity() {
    return new WoConfigPropertyEntity(key, value, description);
  }
}
