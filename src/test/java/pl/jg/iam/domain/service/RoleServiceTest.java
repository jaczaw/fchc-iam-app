package pl.jg.iam.domain.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import pl.jg.iam.domain.model.Role;
import pl.jg.iam.domain.model.RoleName;
import pl.jg.iam.domain.model.dto.RoleDto;
import pl.jg.iam.domain.repository.RoleRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static pl.jg.iam.domain.model.RoleName.ROLE_ADMIN;
import static pl.jg.iam.domain.model.RoleName.ROLE_GUEST;
import static pl.jg.iam.domain.model.RoleName.ROLE_USER;


@Slf4j
@SpringBootTest
class RoleServiceTest {

    @MockBean
    RoleService roleService;

    Role role;

    @BeforeEach
    void setUp() {

        role = Role.builder()
                .name(RoleName.ROLE_ADMIN)
                .build();
        when(roleService.getRoleByName(ROLE_ADMIN)).thenReturn(role);
        when(roleService.isExistRoleByName(ROLE_ADMIN)).thenReturn(true);
        when(roleService.isExistRoleByName(ROLE_USER)).thenReturn(true);
        when(roleService.isExistRoleByName(ROLE_GUEST)).thenReturn(false);

    }

    @AfterEach
    void tearDown() {

        role = null;
    }

    @Test
    void getRoleByName() {

        assertEquals(role.getName().getNazwaRoli(), roleService.getRoleByName(ROLE_ADMIN).getName().getNazwaRoli());
    }

    @Test
    void isExistRoleAdministrator() {

        assertTrue(roleService.isExistRoleByName(RoleName.decode("ROLA-ADMINISTRATOR")));
    }

    @Test
    void isExistRoleGosc() {

        assertFalse(roleService.isExistRoleByName(RoleName.decode("ROLA-GOSC")));
    }
}