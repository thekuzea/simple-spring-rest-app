package com.thekuzea.experimental.infrastructure.persistence;

import java.util.List;
import java.util.Optional;
import javax.persistence.PersistenceException;

import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import net.ttddyy.dsproxy.asserts.assertj.DataSourceAssertAssertions;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.thekuzea.experimental.domain.model.Role;
import com.thekuzea.experimental.domain.model.User;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class UserRepositoryIT {

    @Nested
    @DatabaseSetup(
            value = "classpath:dataset/clean-dataset.xml",
            type = DatabaseOperation.DELETE_ALL
    )
    class RunWithCleanPreparedStatements extends RepositoryIT {

        @Autowired
        private UserRepository userRepository;

        @Test
        void shouldNotFindAllUsers() {
            final List<User> users = userRepository.findAll();

            DataSourceAssertAssertions.assertThat(dataSource)
                    .hasSelectCount(1);
            assertThat(users).isEmpty();
        }
    }

    @Nested
    @DatabaseSetup(value = "classpath:dataset/user-dataset.xml")
    class RunWithPreparedStatements extends RepositoryIT {

        @Autowired
        private UserRepository userRepository;

        @Test
        void shouldFindAllUsers() {
            final List<User> users = userRepository.findAll();

            DataSourceAssertAssertions.assertThat(dataSource)
                    .hasSelectCount(1);
            assertThat(users).hasSize(2);
        }

        @Test
        void shouldFindUserByUsername() {
            final Optional<User> user = userRepository.findByUsername("Larry");

            DataSourceAssertAssertions.assertThat(dataSource)
                    .hasSelectCount(1);
            assertThat(user).isNotEmpty();
        }

        @Test
        void shouldFindExistingUserByUsername() {
            final boolean existsByUsername = userRepository.existsByUsername("Larry");

            DataSourceAssertAssertions.assertThat(dataSource)
                    .hasSelectCount(1);
            assertThat(existsByUsername).isTrue();
        }

        @Test
        void shouldUpdateUser() {
            final User user = entityManager.getEntityManager()
                    .createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                    .setParameter("username", "Larry")
                    .getSingleResult();
            user.setUsername("FunnyArt");

            userRepository.save(user);
            entityManager.flush();

            DataSourceAssertAssertions.assertThat(dataSource)
                    .hasUpdateCount(1);
        }

        @Test
        void shouldDeleteUserByUsername() {
            userRepository.deleteByUsername("Kate");
            entityManager.flush();

            DataSourceAssertAssertions.assertThat(dataSource)
                    .hasDeleteCount(1);
        }
    }

    @Nested
    class RunWithoutPreparedStatements extends RepositoryIT {

        @Autowired
        private UserRepository userRepository;

        @Test
        void shouldNotFindUserByUsername() {
            final Optional<User> user = userRepository.findByUsername("Emily");

            DataSourceAssertAssertions.assertThat(dataSource)
                    .hasSelectCount(1);
            assertThat(user).isEmpty();
        }

        @Test
        void shouldNotFindNonExistingUserByUsername() {
            final boolean existsByUsername = userRepository.existsByUsername("Nataly");

            DataSourceAssertAssertions.assertThat(dataSource)
                    .hasSelectCount(1);
            assertThat(existsByUsername).isFalse();
        }

        @Test
        void shouldSaveUser() {
            final User user = User.builder()
                    .username("testUser")
                    .password(RandomStringUtils.randomAlphabetic(60))
                    .role(findRole())
                    .build();

            userRepository.save(user);
            entityManager.flush();

            DataSourceAssertAssertions.assertThat(dataSource)
                    .hasInsertCount(1);
            assertThat(user.getId()).isNotNull();
        }

        @Test
        void shouldNotSaveUser() {
            final User user = User.builder()
                    .username("root")
                    .password("")
                    .role(findRole())
                    .build();

            userRepository.save(user);
            assertThatThrownBy(() -> entityManager.flush())
                    .isInstanceOf(PersistenceException.class);

            DataSourceAssertAssertions.assertThat(dataSource)
                    .hasInsertCount(1);
            assertThat(user.getId()).isNotNull();
        }

        @Test
        void shouldNotDeleteUserByUsername() {
            userRepository.deleteByUsername("testUser");
            entityManager.flush();

            DataSourceAssertAssertions.assertThat(dataSource)
                    .hasDeleteCount(0);
        }

        private Role findRole() {
            return entityManager.getEntityManager()
                    .createQuery("SELECT r FROM Role r WHERE r.name = :name", Role.class)
                    .setParameter("name", "user")
                    .getSingleResult();
        }
    }
}
