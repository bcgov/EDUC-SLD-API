package ca.bc.gov.educ.api.sld.support;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.repository.CrudRepository;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SldTestUtil {
  public static <T, E> void createSampleDBData(String jsonFileName, TypeReference<List<T>> dataType, CrudRepository<E, ?> repository, Function<T, E> mapper) throws IOException {
    final File file = new File(
      Objects.requireNonNull(SldTestUtil.class.getClassLoader().getResource(jsonFileName)).getFile()
    );
    var entities = new ObjectMapper().readValue(file, dataType);
    repository.saveAll(entities.stream().map(mapper).collect(Collectors.toList()));
  }
}
