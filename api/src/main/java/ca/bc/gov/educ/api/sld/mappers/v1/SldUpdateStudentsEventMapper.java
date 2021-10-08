package ca.bc.gov.educ.api.sld.mappers.v1;

import ca.bc.gov.educ.api.sld.model.SldStudentEntity;
import ca.bc.gov.educ.api.sld.struct.v1.SldUpdateStudentsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * The interface sld update students event mapper.
 */
@Mapper(uses = StringMapper.class)
@SuppressWarnings("squid:S1214")
public interface SldUpdateStudentsEventMapper {
  /**
   * The constant mapper.
   */
  SldUpdateStudentsEventMapper mapper = Mappers.getMapper(SldUpdateStudentsEventMapper.class);

  /**
   * To structure sld student entity.
   *
   * @param sldUpdateStudentsEvent the struct which will be converted.
   * @return the converted struct.
   */
  @Mapping(source = "pen", target = "sldStudentId.pen")
  SldStudentEntity toSldStudentEntity(SldUpdateStudentsEvent sldUpdateStudentsEvent);
}
