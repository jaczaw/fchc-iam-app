package pl.jg.iam.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.jg.iam.domain.model.Role;
import pl.jg.iam.domain.model.RoleName;
import pl.jg.iam.domain.model.dto.RoleDto;
import pl.jg.iam.domain.model.dto.mapper.RoleMapper;
import pl.jg.iam.domain.payload.PagedResponse;
import pl.jg.iam.domain.repository.RoleRepository;
import pl.jg.iam.exception.ResourceNotFoundException;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class RoleService {
    private final RoleMapper roleMapper;
    private final RoleRepository roleRepository;

    public PagedResponse<RoleDto> getAllRole(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Role> roles = roleRepository.findAll(pageable);

        if (roles.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), roles.getNumber(),
                    roles.getSize(), roles.getTotalElements(), roles.getTotalPages(), roles.isLast());
        }

        //List<Long> userIds = users.map(User::getId).getContent();
        List<RoleDto> roleDtos = roles.map(roleMapper::toDto).getContent();

        return new PagedResponse<>(roleDtos, roles.getNumber(), roles.getSize(), roles.getTotalElements(), roles.getTotalPages(), roles.isLast());
    }


    public Role getRoleByName(RoleName rolaName) {
        Role rola = roleRepository.findByName(rolaName)
                .orElseThrow(() -> new ResourceNotFoundException("Role", "roleName.", rolaName));
        return Role.builder()
                .name(rola.getName())
                .build();
    }

    public Boolean isExistRoleByName(RoleName roleName) {
        return roleRepository.existsByName(roleName);
    }

    public Set<Role> addRole(Role role) {
        Set<Role> roles = roleRepository.findAll().stream().collect(Collectors.toSet());
        if (!roleRepository.existsByName(role.getName())) {
            roles.add(role);
            this.saveRole(role);
            log.info(String.format("Add new role to database: %s ", role.getName()));
            return roles;
        } else {
            log.info(String.format("Role %s is not add do database Active role: %s ", role.getName(), showRoles(roles)));
            return roles;
        }
    }

    @Transactional
    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }

    @Transactional
    public void deleteRoleById(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Delete Role", "id", id));
        roleRepository.deleteById(id);
        log.info(String.format("Usunieto role ID: %s o nazwie  %s ", role.getId(), role.getName()));
    }

    private String showRoles(Set<Role> roles) {
        StringBuilder sRoles = new StringBuilder();
        for (Role r : roles) {
            sRoles.append(r.getName()).append(", ").toString();
        }
        return sRoles.toString();
    }


}
