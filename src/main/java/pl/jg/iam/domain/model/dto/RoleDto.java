package pl.jg.iam.domain.model.dto;

import lombok.Builder;
import lombok.Data;
import pl.jg.iam.domain.model.RoleName;


@Data
@Builder
public class RoleDto {

    private Long id;
    private RoleName name;
}
