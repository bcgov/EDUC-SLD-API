package ca.bc.gov.educ.api.sld.mappers.v1;

import ca.bc.gov.educ.api.sld.model.SldStudentProgramEntity;
import ca.bc.gov.educ.api.sld.struct.v1.SldUpdateStudentProgramsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * The interface sld update student programs event mapper.
 */
@Mapper(uses = StringMapper.class)
@SuppressWarnings("squid:S1214")
public interface SldUpdateStudentProgramsEventMapper {
  /**
   * The constant mapper.
   */
  SldUpdateStudentProgramsEventMapper mapper = Mappers.getMapper(SldUpdateStudentProgramsEventMapper.class);

  /**
   * To structure sld student program entity.
   *
   * @param sldUpdateStudentProgramsEvent the struct which will be converted.
   * @return the converted struct.
   */
  @Mapping(source = "pen", target = "sldStudentProgramId.pen")
  SldStudentProgramEntity toSldStudentProgramEntity(SldUpdateStudentProgramsEvent sldUpdateStudentProgramsEvent);
}
