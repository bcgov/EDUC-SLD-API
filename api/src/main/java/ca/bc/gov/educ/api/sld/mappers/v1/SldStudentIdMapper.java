package ca.bc.gov.educ.api.sld.mappers.v1;

import ca.bc.gov.educ.api.sld.model.SldStudentId;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * The interface sld update single student event mapper.
 */
@Mapper(uses = StringMapper.class)
@SuppressWarnings("squid:S1214")
public interface SldStudentIdMapper {
  /**
   * The constant mapper.
   */
  SldStudentIdMapper mapper = Mappers.getMapper(SldStudentIdMapper.class);

  /**
   * To structure sld student id.
   *
   * @param sldStudentId the struct which will be converted.
   * @return the converted struct.
   */
  SldStudentId toStruct(ca.bc.gov.educ.api.sld.struct.v1.SldStudentId sldStudentId);
}
