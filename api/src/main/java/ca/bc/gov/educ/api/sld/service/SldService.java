package ca.bc.gov.educ.api.sld.service;

import ca.bc.gov.educ.api.sld.constant.EntityName;

import java.util.List;

public interface SldService<T> {
  List<T> update(String pen, T t);

  List<T> restore(String pen, T t);

  EntityName getEntityName();
}
