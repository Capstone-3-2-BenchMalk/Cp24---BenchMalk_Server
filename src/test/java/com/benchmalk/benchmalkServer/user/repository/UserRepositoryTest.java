package com.benchmalk.benchmalkServer.user.repository;

import com.benchmalk.benchmalkServer.user.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    void findById() {
        User user = new User("id1", "user1", "123");
        userRepository.save(user);

        assertThat(userRepository.findById(user.getId()).get()).isEqualTo(user);
        assertThat(userRepository.findByUsername(user.getUsername()).get()).isEqualTo(user);
    }
}