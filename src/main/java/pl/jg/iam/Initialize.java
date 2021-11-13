package pl.jg.iam;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.jg.iam.domain.model.Role;
import pl.jg.iam.domain.model.RoleName;
import pl.jg.iam.domain.service.RoleService;

import java.util.List;

import static java.lang.Boolean.*;

@Slf4j
@Component
public class Initialize implements CommandLineRunner {

    @Autowired
    private RoleService roleService;
    private List<RoleName> role = List.of(RoleName.ROLE_GUEST, RoleName.ROLE_USER, RoleName.ROLE_ADMIN);

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        log.info("Initialize role .....{}", role);
        initialize(role);

    }

    private void initialize(List<RoleName> role) {

        for (RoleName roleName : role) {
            if (TRUE.equals(!roleService.isExistRoleByName(roleName))) {
                roleService.saveRole(
                        Role.builder()
                                .name(roleName)
                                .build()
                );
                log.info("Add role .....{}", roleName);
            }
        }
    }
}
