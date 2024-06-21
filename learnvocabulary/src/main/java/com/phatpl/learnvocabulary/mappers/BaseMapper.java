package com.phatpl.learnvocabulary.mappers;


import org.mapstruct.MapperConfig;
import org.mapstruct.MappingInheritanceStrategy;

import java.util.List;

@MapperConfig(mappingInheritanceStrategy = MappingInheritanceStrategy.AUTO_INHERIT_ALL_FROM_CONFIG)
public interface BaseMapper<E, DTO> {
    DTO toDTO(E entity);
    E toEntity(DTO dto);
    List<DTO> toListDTO(List<E> e);
    List<E> toListEntity(List<DTO> dto);
}