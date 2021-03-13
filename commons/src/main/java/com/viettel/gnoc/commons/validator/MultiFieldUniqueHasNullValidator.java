package com.viettel.gnoc.commons.validator;

import com.viettel.gnoc.commons.repository.CommonRepositoryImpl;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;

@Slf4j
public class MultiFieldUniqueHasNullValidator implements
    ConstraintValidator<MultiFieldUniqueHasNull, Object> {

  private Class clazz;
  private String uniqueFields;
  private String idField;

  //@Autowired
  private CommonRepositoryImpl commonRepository;

  public MultiFieldUniqueHasNullValidator(CommonRepositoryImpl commonRepository) {
    this.commonRepository = commonRepository;
  }

  @Override
  public void initialize(MultiFieldUniqueHasNull unique) {
    clazz = unique.clazz();
    uniqueFields = unique.uniqueFields();
    idField = unique.idField();
  }

  @Override
  public boolean isValid(Object obj, ConstraintValidatorContext constraintValidatorContext) {
    try {
      List<String> fields = Stream.of(uniqueFields.split(",")).collect(Collectors.toList());
      List<Object> params = new ArrayList<>();
      Map<String, Object> mapFields = new HashMap<>();
      for (String field : fields) {
        Field fieldClass = obj.getClass().getDeclaredField(field);
        fieldClass.setAccessible(true);
        params.add(field);
//        params.add(fieldClass.get(obj) != null ? fieldClass.get(obj) : new Object());
        mapFields.put(fieldClass.getName(), fieldClass.get(obj));
      }
      Long idFieldValue = 0l;
      if (BeanUtils.getProperty(obj, idField) != null) {
        idFieldValue = Long.valueOf(BeanUtils.getProperty(obj, idField));
      }
      return commonRepository
          .checkUniqueWithMultiFieldsHasNull(clazz, idField, idFieldValue, mapFields,
              params.toArray());
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
