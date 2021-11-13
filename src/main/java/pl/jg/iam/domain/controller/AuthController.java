package pl.jg.iam.domain.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.jg.iam.domain.model.RoleName;
import pl.jg.iam.domain.model.User;
import pl.jg.iam.domain.payload.ApiResponse;
import pl.jg.iam.domain.payload.JwtAuthenticationResponse;
import pl.jg.iam.domain.payload.LoginRequest;
import pl.jg.iam.domain.payload.SignUpRequest;
import pl.jg.iam.domain.service.RoleService;
import pl.jg.iam.domain.service.UserService;
import pl.jg.iam.security.JwtTokenProvider;

import javax.validation.Valid;
import java.net.URI;

import static java.lang.Boolean.TRUE;


@Slf4j
@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    @PostMapping("/signin")
    public ResponseEntity<JwtAuthenticationResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsernameOrEmail(),
                        loginRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {

        RoleName roleName = RoleName.decode(signUpRequest.getRoleName());
        if (TRUE.equals(userService.isExistUserByUserName(signUpRequest.getUsername()))) {
            return new ResponseEntity<>(new ApiResponse(false, "Username is already taken!"),
                    HttpStatus.BAD_REQUEST);
        }

        if (TRUE.equals(userService.isExistUserByEmail(signUpRequest.getEmail()))) {
            return new ResponseEntity<>(new ApiResponse(false, "Email Address already in use!"),
                    HttpStatus.BAD_REQUEST);
        }

        if (TRUE.equals(!roleService.isExistRoleByName(roleName))) {

            return new ResponseEntity<>(new ApiResponse(false, "Role name is not exist!"),
                    HttpStatus.BAD_REQUEST);
        }
        User user = User.builder()
                .name(signUpRequest.getName())
                .username(signUpRequest.getUsername())
                .email(signUpRequest.getEmail())
                .password(signUpRequest.getPassword())
                .build();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(userService.addRole(roleName));
        log.info(String.format("Rola: %s User: %s", roleName, user));
        User saveUser = userService.saveUser(user);
        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/users/{username}")
                .buildAndExpand(saveUser.getUsername()).toUri();
        return ResponseEntity.created(location).body(new ApiResponse(true, "User registered successfully"));
    }

}
