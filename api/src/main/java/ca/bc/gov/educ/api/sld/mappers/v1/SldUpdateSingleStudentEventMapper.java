package ca.bc.gov.educ.api.sld.mappers.v1;

import ca.bc.gov.educ.api.sld.model.SldStudentId;
import ca.bc.gov.educ.api.sld.struct.v1.SldUpdateSingleStudentEvent;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * The interface sld update single student event mapper.
 */
@Mapper(uses = StringMapper.class)
@SuppressWarnings("squid:S1214")
public interface SldUpdateSingleStudentEventMapper {
  /**
   * The constant mapper.
   */
  SldUpdateSingleStudentEventMapper mapper = Mappers.getMapper(SldUpdateSingleStudentEventMapper.class);

  /**
   * To structure sld student id.
   *
   * @param sldUpdateSingleStudentEvent the struct which will be converted.
   * @return the converted struct.
   */
  SldStudentId toSldStudentId(SldUpdateSingleStudentEvent sldUpdateSingleStudentEvent);
}
