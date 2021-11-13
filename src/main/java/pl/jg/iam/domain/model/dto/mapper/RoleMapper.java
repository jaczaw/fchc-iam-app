package pl.jg.iam.domain.model.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import pl.jg.iam.domain.model.Role;
import pl.jg.iam.domain.model.dto.RoleDto;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    RoleMapper MAPPER = Mappers.getMapper(RoleMapper.class);

    Role toEntity(RoleDto dto);

    RoleDto toDto(Role entity);

}
