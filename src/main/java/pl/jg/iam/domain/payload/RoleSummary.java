package pl.jg.iam.domain.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class RoleSummary {
    private Long id;
    private String username;
    private String role;
}
