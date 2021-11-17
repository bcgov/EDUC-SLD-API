package ca.bc.gov.educ.api.sld.mappers.v1;

import ca.bc.gov.educ.api.sld.model.SldDiaStudentEntity;
import ca.bc.gov.educ.api.sld.struct.v1.SldDiaStudent;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * The interface sld dia student mapper.
 */
@Mapper(uses = StringMapper.class)
@SuppressWarnings("squid:S1214")
public interface SldDiaStudentMapper {
  /**
   * The constant mapper.
   */
  SldDiaStudentMapper mapper = Mappers.getMapper(SldDiaStudentMapper.class);


  /**
   * To structure sld dia student.
   *
   * @param sldDiaStudentEntity the sld dia student entity
   * @return the sld dia student
   */
  @Mapping(target = "schlNo", source = "sldDiaStudentEntity.sldDiaStudentId.schlNo")
  @Mapping(target = "recordNumber", source = "sldDiaStudentEntity.sldDiaStudentId.recordNumber")
  @Mapping(target = "distNo", source = "sldDiaStudentEntity.sldDiaStudentId.distNo")
  @Mapping(target = "comment", ignore = true)
  @Mapping(target = "pen", source = "sldDiaStudentEntity.pen")
  @Mapping(target = "reportDate", source = "sldDiaStudentEntity.sldDiaStudentId.reportDate")
  SldDiaStudent toStructure(SldDiaStudentEntity sldDiaStudentEntity);

  /**
   * the toModel is only used in unit testing.
   *
   * @param sldDiaStudent the struct which will be converted to entity.
   * @return the converted entity.
   */
  @InheritInverseConfiguration
  SldDiaStudentEntity toModel(SldDiaStudent sldDiaStudent);
}
