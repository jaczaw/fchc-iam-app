package pl.jg.iam.domain.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.jg.iam.domain.model.Role;
import pl.jg.iam.domain.model.dto.RoleDto;
import pl.jg.iam.domain.model.dto.UserDto;
import pl.jg.iam.domain.payload.ApiResponse;
import pl.jg.iam.domain.payload.PagedResponse;
import pl.jg.iam.domain.payload.UserProfile;
import pl.jg.iam.domain.service.RoleService;
import pl.jg.iam.domain.service.UserService;
import pl.jg.iam.security.CurrentUser;
import pl.jg.iam.security.UserPrincipal;
import pl.jg.iam.util.AppConstants;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/manage")
@AllArgsConstructor
@Slf4j
public class UserManageController {

    private final UserService userService;
    private final RoleService roleService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/users")
    public PagedResponse<UserProfile> getAllUsersProfile(@CurrentUser UserPrincipal currentUser,
                                                         @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                                         @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
        return userService.getAllUserProfile(currentUser, page, size);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/users")
    public UserDto saveUser(@RequestBody UserDto userDto) {
        return userService.saveUserDto(userDto);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/users/{id}")
    public ResponseEntity<ApiResponse> updateUser(@PathVariable(value = "id") Long id,
                                                  @Valid @RequestBody UserDto userDto) {
        log.info("UPDATE User :  {} ", userDto);
        UserDto user = userService.update(id, userDto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/users/{id}")
                .buildAndExpand(user.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "User Updated Successfully"));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/users/{id}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable(value = "id") Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.ok(new ApiResponse(true, "User deleted successfully"));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/users/roles")
    public PagedResponse<RoleDto> getAllRoles(@RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                                              @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
        return roleService.getAllRole(page, size);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/users/roles")
    public Role saveRole(@RequestBody Role role) {
        return roleService.saveRole(role);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/users/roles/{id}")
    public ResponseEntity<ApiResponse> deleteRole(@PathVariable(value = "id") Long id) {
        roleService.deleteRoleById(id);
        return ResponseEntity.ok(new ApiResponse(true, "Role deleted successfully"));
    }

}
