package pl.jg.iam.domain.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.jg.iam.domain.model.Role;
import pl.jg.iam.domain.model.RoleName;
import pl.jg.iam.domain.model.User;
import pl.jg.iam.domain.model.dto.UserDto;
import pl.jg.iam.domain.model.dto.mapper.UserMapper;
import pl.jg.iam.domain.payload.PagedResponse;
import pl.jg.iam.domain.payload.UserProfile;
import pl.jg.iam.domain.repository.RoleRepository;
import pl.jg.iam.domain.repository.UserRepository;
import pl.jg.iam.exception.ResourceNotFoundException;
import pl.jg.iam.security.UserPrincipal;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private UserMapper userMapper;


    public PagedResponse<UserProfile> getAllUserProfile(UserPrincipal currentUser, int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        Page<User> users = userRepository.findAll(pageable);

        if (users.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), users.getNumber(),
                    users.getSize(), users.getTotalElements(), users.getTotalPages(), users.isLast());
        }

        //List<Long> userIds = users.map(User::getId).getContent();
        List<UserProfile> userProfiles = users.map(UserProfile::convert).getContent();

        return new PagedResponse<>(userProfiles, users.getNumber(), users.getSize(), users.getTotalElements(), users.getTotalPages(), users.isLast());
    }


    public Boolean isExistUserByUserName(String username) {
        return userRepository.existsByUsername(username);
    }

    public Boolean isExistUserByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public UserProfile getUserProfileByUserName(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
        return UserProfile.builder()
                .id(user.getId())
                .name(user.getName())
                .username(user.getUsername())
                .joinedAt(user.getCreatedAt())
                .lastChanges(user.getUpdatedAt())
                .build();
    }

    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Transactional
    public UserDto saveUserDto(UserDto userDto) {
        User userNew = userRepository.save(userMapper.toEntity(userDto));
        return userMapper.toDto(userNew);
    }

    @Transactional
    public UserDto deleteUserById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Delete User", "id", id));
        userRepository.deleteById(id);
        log.info("Usunieto uzytkownika ID: {} o nazwie  {} i adresie e-mail: {}", user.getId(), user.getEmail(), user.getUsername());
        return userMapper.toDto(user);

    }

    public Set<Role> addRoleToUser(Role role, User user) {
        Set<Role> actualUserRole = new HashSet<>();
        Role roleNew = roleRepository.findByName(role.getName())
                .orElseThrow(() -> new ResourceNotFoundException("Role ", "name", role.getName()));
        actualUserRole = (user.getRoles() != null) ? user.getRoles() : actualUserRole;

        if (!actualUserRole.contains(roleNew)) {
            actualUserRole.add(roleNew);
            log.info(String.format("Add new role: %s to user: %s", roleNew.getName(), user.getName()));
            return actualUserRole;
        } else
            return actualUserRole;
    }

    public Set<Role> addRole(RoleName roleName) {
        Set<Role> userRoles = new HashSet<>();
        Role roleNew = roleRepository.findByName(roleName)
                .orElseThrow(() -> new ResourceNotFoundException("Role ", "name", roleName));
        userRoles.add(roleNew);
        return userRoles;
    }

    @Transactional
    public UserDto update(Long id, UserDto newDto) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setName(newDto.getName());
                    user.setEmail(newDto.getEmail());
                    user.setUsername(newDto.getUsername());
                    user.setPassword(passwordEncoder.encode(newDto.getName()));
                    return userMapper.toDto(userRepository.save(user));
                })
                .orElseThrow(() -> new ResourceNotFoundException("UPDATE User", "id", id));
    }


}

