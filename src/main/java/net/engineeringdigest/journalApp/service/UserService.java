package net.engineeringdigest.journalApp.service;


import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.UserRepository;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class UserService {
    static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
   
    @Autowired
    private UserRepository userRepo;

    public void saveNewUser(User user) {

        try {
            if (!user.getPw().startsWith("$2a$")) {
                user.setPw(passwordEncoder.encode(user.getPw()));
            }

            user.setRoles(Arrays.asList("USER"));
            userRepo.save(user);
        } catch (Exception e) {
            logger.info("djljjlksajdkljdja");
            throw new RuntimeException(e);
        }

        ;
    }

    public void saveAdmin(User user) {
        if (!user.getPw().startsWith("$2a$")) {
            user.setPw(passwordEncoder.encode(user.getPw()));
        }

        user.setRoles(Arrays.asList("USER", "ADMIN"));
        userRepo.save(user);
        ;
    }

    public void saveEntry(User user) {
        userRepo.save(user)
        ;
    }

    public List<User> getAll() {
        return userRepo.findAll();
    }

    public Optional<User> findById(ObjectId id) {
        return userRepo.findById(id);
    }

    public void deleteById(ObjectId id) {
        userRepo.deleteById(id);
    }

    public void deleteall() {
        userRepo.deleteAll();
    }

    public User findByUserName(String username) {
        return userRepo.findByUsername(username);
    }
}