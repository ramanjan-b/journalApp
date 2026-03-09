package net.engineeringdigest.journalApp.service;

import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
 class UserServiceTests {
   @Autowired
    User user;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
     void setUp() {

        user.setUsername("ram");

        userRepository.save(user);
    }

    @Test
    void testFindByUserName() {
        User u = userService.findByUserName("ram");
        assertNotNull(u);
    }

}
