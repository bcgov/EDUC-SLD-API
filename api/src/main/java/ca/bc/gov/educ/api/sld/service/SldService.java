package ca.bc.gov.educ.api.sld.service;

import ca.bc.gov.educ.api.sld.constant.EntityName;

import java.util.List;
import java.util.Optional;

public interface SldService<S, T> {
  List<T> updateBatch(T o, T t);

  List<T> updateBatchByIds(List<S> ids, T t);

  List<T> updateBatchByExamples(List<T> l, T t);

  Optional<T> update(S id, T t);

  List<T> restore(String pen, T t);

  EntityName getEntityName();
}
