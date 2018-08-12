package com.abdt.api.repository;

import com.abdt.api.domain.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    User alex = new User("alex@example.com", "alex", "USER", null, null);
    List<User> allUsers = Arrays.asList(alex);

    @Test
    public void whenFindById_thenReturnUser() {
        // given
        entityManager.persist(alex);
        entityManager.flush();

        // when
        Optional found = userRepository.findById(alex.getId());

        // then
        assertThat(found.isPresent())
                .isEqualTo(true);
    }

    @Test
    public void whenFindByUsername_thenReturnUser() {
        // given
        entityManager.persist(alex);
        entityManager.flush();

        // when
        User found = userRepository.findByUsername(alex.getUsername());

        // then
        assertThat(found.getUsername())
                .isEqualTo(alex.getUsername());
    }

    @Test
    public void whenFindAllUsers_thenReturnUser() {

        // given
        entityManager.persist(alex);
        entityManager.flush();

        // when
        List<User> found = userRepository.findAll();

        // then
        assertThat(found)
                .isEqualTo(allUsers);
    }

    @Test
    public void whenSave_thenReturnUserByUsername() {

        // when
        userRepository.save(alex);

        // then
        assertThat(userRepository.findByUsername(alex.getUsername()))
                .isEqualTo(alex);
    }

    @Test
    public void whenDelete_thenReturnNullByUsername() {

        // given
        entityManager.persist(alex);
        entityManager.flush();

        // when
        userRepository.delete(alex);

        // then
        assertThat(userRepository.findByUsername(alex.getUsername()))
                .isEqualTo(null);
    }
}
