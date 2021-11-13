package pl.jg.iam.domain.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import pl.jg.iam.domain.model.User;

import java.time.Instant;


@Data
@Builder
@AllArgsConstructor
public class UserProfile {
    private Long id;
    private String username;
    private String name;
    private Instant joinedAt;
    private Instant lastChanges;

    public static UserProfile convert(User user) {
        if (user == null) {
            return null;
        }
        return new UserProfile(
                user.getId(),
                user.getName(),
                user.getUsername(),
                user.getCreatedAt(),
                user.getUpdatedAt()

        );
    }

    public User convert() {
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setName(name);
        return user;
    }
}
