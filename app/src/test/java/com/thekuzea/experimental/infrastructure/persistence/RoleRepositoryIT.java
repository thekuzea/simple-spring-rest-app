package com.thekuzea.experimental.infrastructure.persistence;

import java.util.List;
import java.util.Optional;
import javax.persistence.PersistenceException;

import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import net.ttddyy.dsproxy.asserts.assertj.DataSourceAssertAssertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.thekuzea.experimental.domain.model.Role;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class RoleRepositoryIT {

    @Nested
    @DatabaseSetup(
            value = "classpath:dataset/clean-dataset.xml",
            type = DatabaseOperation.DELETE_ALL
    )
    class RunWithCleanPreparedStatements extends RepositoryIT {

        @Autowired
        private RoleRepository roleRepository;

        @Test
        void shouldNotFindAllRoles() {
            final List<Role> roles = roleRepository.findAll();

            DataSourceAssertAssertions.assertThat(dataSource)
                    .hasSelectCount(1);
            assertThat(roles).isEmpty();
        }

        @Test
        @DatabaseSetup(value = "classpath:dataset/role-dataset.xml")
        void shouldDeleteRoleByName() {
            roleRepository.deleteByName("customRole");
            entityManager.flush();

            DataSourceAssertAssertions.assertThat(dataSource)
                    .hasDeleteCount(1);
        }
    }

    @Nested
    class RunWithoutPreparedStatements extends RepositoryIT {

        @Autowired
        private RoleRepository roleRepository;

        @Test
        void shouldFindAllRoles() {
            final List<Role> roles = roleRepository.findAll();

            DataSourceAssertAssertions.assertThat(dataSource)
                    .hasSelectCount(1);
            assertThat(roles).hasSize(2);
        }

        @Test
        void shouldFindRoleByName() {
            final Optional<Role> role = roleRepository.findByName("admin");

            DataSourceAssertAssertions.assertThat(dataSource)
                    .hasSelectCount(1);
            assertThat(role).isNotEmpty();
        }

        @Test
        void shouldNotFindRoleByName() {
            final Optional<Role> role = roleRepository.findByName("moderator");

            DataSourceAssertAssertions.assertThat(dataSource)
                    .hasSelectCount(1);
            assertThat(role).isEmpty();
        }

        @Test
        void shouldNotFindNonExistingRoleByName() {
            final boolean existsByName = roleRepository.existsByName("moderator");

            DataSourceAssertAssertions.assertThat(dataSource)
                    .hasSelectCount(1);
            assertThat(existsByName).isFalse();
        }

        @Test
        void shouldFindExistingRoleByName() {
            final boolean existsByName = roleRepository.existsByName("admin");

            DataSourceAssertAssertions.assertThat(dataSource)
                    .hasSelectCount(1);
            assertThat(existsByName).isTrue();
        }

        @Test
        void shouldSaveRole() {
            final Role role = Role.builder()
                    .name("sys_admin")
                    .build();

            roleRepository.save(role);
            entityManager.flush();

            DataSourceAssertAssertions.assertThat(dataSource)
                    .hasInsertCount(1);
            assertThat(role.getId()).isNotNull();
        }

        @Test
        void shouldNotSaveRole() {
            final Role role = Role.builder()
                    .name("admin")
                    .build();

            roleRepository.save(role);
            assertThatThrownBy(() -> entityManager.flush())
                    .isInstanceOf(PersistenceException.class);

            DataSourceAssertAssertions.assertThat(dataSource)
                    .hasInsertCount(1);
            assertThat(role.getId()).isNotNull();
        }

        @Test
        void shouldNotDeleteRoleByName() {
            roleRepository.deleteByName("moderator");
            entityManager.flush();

            DataSourceAssertAssertions.assertThat(dataSource)
                    .hasDeleteCount(0);
        }
    }
}
