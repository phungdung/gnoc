package com.viettel.gnoc.commons.validator;

import com.google.common.base.Charsets;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SizeByteValidator implements ConstraintValidator<SizeByte, String> {

  private int max;
  private int min;

  @Override
  public void initialize(SizeByte sizeByte) {
    this.max = sizeByte.max();
    this.min = sizeByte.min();
  }

  @Override
  public boolean isValid(String obj, ConstraintValidatorContext constraintValidatorContext) {
    if (obj == null) {
      return true;
    }
    return (obj.getBytes(Charsets.UTF_8).length <= this.max) && (obj.getBytes(Charsets.UTF_8).length
        >= this.min);
  }
}
