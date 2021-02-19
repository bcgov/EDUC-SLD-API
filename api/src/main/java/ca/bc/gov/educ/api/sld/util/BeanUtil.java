package ca.bc.gov.educ.api.sld.util;

import ca.bc.gov.educ.api.sld.exception.SldRuntimeException;
import org.springframework.stereotype.Component;

import java.beans.Expression;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.util.StringUtils.capitalize;

/**
 * The type Bean util.
 */
@Component
public class BeanUtil {

  private BeanUtil() {
  }

  /**
   * Get the declared fields of a Bean.
   *
   * @param bean the Bean
   * @return the Field list
   */
  public static List<Field> getFields(Object bean) {
    var clazz = bean.getClass();
    List<Field> fields = new ArrayList<>();
    var superClazz = clazz;
    while (!superClazz.equals(Object.class)) {
      fields.addAll(Arrays.asList(superClazz.getDeclaredFields()));
      superClazz = superClazz.getSuperclass();
    }

    return fields;
  }

  /**
   * Get the field value
   * @param field the Field
   * @param bean the Bean
   * @return the field value
   */
  public static Object getFieldValue(Field field, Object bean) {
    try {
      var fieldName = capitalize(field.getName());
      var expr = new Expression(bean, "get" + fieldName, new Object[0]);
      return expr.getValue();
    } catch (Exception ex) {
      throw new SldRuntimeException(ex.getMessage());
    }
  }

}
