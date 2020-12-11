package ca.bc.gov.educ.api.sld.exception;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

/**
 * ExceptionUtils to provide tools to generate error messages
 */
public class ExceptionUtils {
  private ExceptionUtils() {
  }

  /**
   * To map map.
   *
   * @param <K>       the type parameter
   * @param <V>       the type parameter
   * @param keyType   the key type
   * @param valueType the value type
   * @param entries   the entries
   * @return the map
   */
  public static <K, V> Map<K, V> toMap(
          Class<K> keyType, Class<V> valueType, Object... entries) {
    if (entries.length % 2 == 1)
      throw new IllegalArgumentException("Invalid entries");
    return IntStream.range(0, entries.length / 2).map(i -> i * 2)
            .collect(HashMap::new,
                    (m, i) -> m.put(keyType.cast(entries[i]), valueType.cast(entries[i + 1])),
                    Map::putAll);
  }

}
