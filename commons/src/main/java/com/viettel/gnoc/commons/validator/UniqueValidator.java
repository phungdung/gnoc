package com.viettel.gnoc.commons.validator;

import com.viettel.gnoc.commons.repository.CommonRepositoryImpl;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;

@Slf4j
public class UniqueValidator implements ConstraintValidator<Unique, Object> {

  private Class clazz;
  private String uniqueField;
  private String idField;

  //@Autowired
  private CommonRepositoryImpl commonRepository;

  public UniqueValidator(CommonRepositoryImpl commonRepository) {
    this.commonRepository = commonRepository;
  }

  @Override
  public void initialize(Unique unique) {
    clazz = unique.clazz();
    uniqueField = unique.uniqueField();
    idField = unique.idField();
  }

  @Override
  public boolean isValid(Object obj, ConstraintValidatorContext constraintValidatorContext) {
    try {
      Field field = obj.getClass().getDeclaredField(uniqueField);
      field.setAccessible(true);
      Object uniqueValue = field.get(obj);
      Long idFieldValue = 0l;
      if (BeanUtils.getProperty(obj, idField) != null) {
        idFieldValue = Long.valueOf(BeanUtils.getProperty(obj, idField));
      }
      return commonRepository.checkUnique(clazz, uniqueField, uniqueValue, idField, idFieldValue);
    } catch (IllegalAccessException e) {
      log.error("Accessor method is not available for class : {}, exception : {}",
          obj.getClass().getName(), e);
    } catch (InvocationTargetException e) {
      log.error("An exception occurred while accessing class : {}, exception : {}",
          obj.getClass().getName(), e);
    } catch (NoSuchMethodException e) {
      log.error("Method is not present on class : {}, exception : {}",
          obj.getClass().getName(), e);
    } catch (NoSuchFieldException e) {
      log.error("Field is not present on class : {}, exception : {}",
          obj.getClass().getName(), e);
    }
    return false;
  }
}
