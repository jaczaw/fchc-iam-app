package pl.jg.iam.domain.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import pl.jg.iam.domain.model.Role;
import pl.jg.iam.domain.model.RoleName;


import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RoleRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private RoleRepository roleRepository;

    @Test
    void should_findByName_OK() {
        this.entityManager.persist(Role.builder().name(RoleName.ROLE_GUEST).build());
        Role rola = this.roleRepository.findByName(RoleName.ROLE_GUEST).orElse(null);
        assertThat(rola).isNotNull();
        assertThat(rola.getName()).isEqualTo(RoleName.ROLE_GUEST);
        assertThat(rola.getName().getNazwaRoli()).isEqualTo("ROLA-GOSC");
    }

    @Test
    void should_findByName_NotFound() {
        Role rola = this.roleRepository.findByName(RoleName.ROLE_SUPER_USER).orElse(null);
        assertThat(rola).isNull();
    }

    @Test
    void should_return_true_when_existByName_ROLE_SUPER_ADMIN() {
        this.entityManager.persist(Role.builder().name(RoleName.ROLE_SUPER_ADMIN).build());
        Boolean isExistsByName = this.roleRepository.existsByName(RoleName.ROLE_SUPER_ADMIN);
        assertThat(isExistsByName).isTrue();
    }

    @Test
    void should_return_false_when_not_existByName_ROLE_SUPER_ADMIN() {
        Boolean isExistsByName = this.roleRepository.existsByName(RoleName.ROLE_SUPER_USER);
        assertThat(isExistsByName).isFalse();
    }
}