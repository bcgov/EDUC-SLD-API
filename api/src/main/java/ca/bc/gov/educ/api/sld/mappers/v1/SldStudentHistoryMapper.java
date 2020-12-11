package ca.bc.gov.educ.api.sld.mappers.v1;

import ca.bc.gov.educ.api.sld.model.SldStudentHistoryEntity;
import ca.bc.gov.educ.api.sld.struct.v1.SldStudentHistory;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * The interface sld student history mapper.
 */
@Mapper(uses = StringMapper.class)
@SuppressWarnings("squid:S1214")
public interface SldStudentHistoryMapper {
  /**
   * The constant mapper.
   */
  SldStudentHistoryMapper mapper = Mappers.getMapper(SldStudentHistoryMapper.class);


  /**
   * To structure sld student history.
   *
   * @param sldStudentHistoryEntity the sld student history entity
   * @return the sld
   */
  @Mapping(target = "localStudentId", source="sldStudentHistoryEntity.sldStudentHistoryIdEntity.localStudentId")
  @Mapping(target = "reportDate", source="sldStudentHistoryEntity.sldStudentHistoryIdEntity.reportDate")
  SldStudentHistory toStructure(SldStudentHistoryEntity sldStudentHistoryEntity);

  /**
   * the toModel is only used in unit testing.
   *
   * @param sldStudentHistory the struct which will be converted to entity.
   * @return the converted entity.
   */
  @InheritInverseConfiguration
  SldStudentHistoryEntity toModel(SldStudentHistory sldStudentHistory);
}
