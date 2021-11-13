package pl.jg.iam.domain.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.jg.iam.domain.payload.RoleSummary;
import pl.jg.iam.domain.payload.UserIdentityAvailability;
import pl.jg.iam.domain.payload.UserProfile;
import pl.jg.iam.domain.payload.UserSummary;
import pl.jg.iam.domain.service.UserService;
import pl.jg.iam.security.CurrentUser;
import pl.jg.iam.security.UserPrincipal;


@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    @PreAuthorize("hasRole('ROLE_USER')")
    public UserSummary getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {

        return UserSummary.builder()
                .id(userPrincipal.getId())
                .username(userPrincipal.getUsername())
                .name(userPrincipal.getName())
                .role(userPrincipal.getAuthorities().toString())
                .build();
    }

    @GetMapping("/checkUsernameAvailability")
    public UserIdentityAvailability checkUsernameAvailability(@RequestParam(value = "username") String username) {

        Boolean isAvailable = userService.isExistUserByUserName(username);
        return new UserIdentityAvailability(isAvailable);
    }

    @GetMapping("/checkEmailAvailability")
    public UserIdentityAvailability checkEmailAvailability(@RequestParam(value = "email") String email) {

        Boolean isAvailable = userService.isExistUserByEmail(email);
        return new UserIdentityAvailability(isAvailable);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{username}")
    public UserProfile getUserProfile(@PathVariable(value = "username") String username) {
        return userService.getUserProfileByUserName(username);
    }

    @GetMapping("/roles/me")
    @PreAuthorize("hasRole('ROLE_USER')")
    public RoleSummary getCurrentRoles(@CurrentUser UserPrincipal userPrincipal) {

        return RoleSummary.builder()
                .id(userPrincipal.getId())
                .username(userPrincipal.getUsername())
                .role(userPrincipal.getAuthorities().toString())
                .build();
    }
}
