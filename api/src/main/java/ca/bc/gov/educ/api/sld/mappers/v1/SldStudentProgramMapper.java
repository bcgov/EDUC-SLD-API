package ca.bc.gov.educ.api.sld.mappers.v1;

import ca.bc.gov.educ.api.sld.model.SldStudentProgramEntity;
import ca.bc.gov.educ.api.sld.struct.v1.SldStudentProgram;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * The interface sld student program mapper.
 */
@Mapper(uses = StringMapper.class)
@SuppressWarnings("squid:S1214")
public interface SldStudentProgramMapper {
  /**
   * The constant mapper.
   */
  SldStudentProgramMapper mapper = Mappers.getMapper(SldStudentProgramMapper.class);


  /**
   * To structure sld student program.
   *
   * @param sldStudentProgramEntity the sld student program entity
   * @return the sld student program
   */
  @Mapping(target = "distNo", source = "sldStudentProgramEntity.sldStudentProgramId.distNo")
  @Mapping(target = "pen", source = "sldStudentProgramEntity.sldStudentProgramId.pen")
  @Mapping(target = "schlNo", source = "sldStudentProgramEntity.sldStudentProgramId.schlNo")
  @Mapping(target = "enrolledProgramCode", source = "sldStudentProgramEntity.sldStudentProgramId.enrolledProgramCode")
  @Mapping(target = "reportDate", source = "sldStudentProgramEntity.sldStudentProgramId.reportDate")
  SldStudentProgram toStructure(SldStudentProgramEntity sldStudentProgramEntity);

  /**
   * the toModel.
   *
   * @param sldStudentProgram the struct which will be converted to entity.
   * @return the converted entity.
   */
  @InheritInverseConfiguration
  SldStudentProgramEntity toModel(SldStudentProgram sldStudentProgram);
}
