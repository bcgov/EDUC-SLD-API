package ca.bc.gov.educ.api.sld.mappers.v1;

import ca.bc.gov.educ.api.sld.model.SldStudentEntity;
import ca.bc.gov.educ.api.sld.struct.v1.SldStudent;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * The interface sld student mapper.
 */
@Mapper(uses = StringMapper.class)
@SuppressWarnings("squid:S1214")
public interface SldStudentMapper {
  /**
   * The constant mapper.
   */
  SldStudentMapper mapper = Mappers.getMapper(SldStudentMapper.class);


  /**
   * To structure sld student.
   *
   * @param sldStudentEntity the sld entity
   * @return the sld
   */
  @Mapping(target = "studentId", source="sldStudentEntity.sldStudentIdEntity.studentId")
  @Mapping(target = "reportDate", source="sldStudentEntity.sldStudentIdEntity.reportDate")
  SldStudent toStructure(SldStudentEntity sldStudentEntity);

  /**
   * the toModel is only used in unit testing.
   *
   * @param sldStudent the struct which will be converted to entity.
   * @return the converted entity.
   */
  @InheritInverseConfiguration
  SldStudentEntity toModel(SldStudent sldStudent);
}
