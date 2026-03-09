package net.engineeringdigest.journalApp.controller;

import lombok.extern.slf4j.Slf4j;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAll();
    }

    @GetMapping("{username}")
    public ResponseEntity<String> getById(@PathVariable String username) {
        User u = userService.findByUserName(username);
        if (u.getUsername() != null) return new ResponseEntity<>(u.getPw(), HttpStatus.OK);
        else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody User user) {
        try {
            userService.saveNewUser(user);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Sorry!, already exists", HttpStatus.EXPECTATION_FAILED);
        }

    }

    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody User user) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        User userInDb = userService.findByUserName(username);

        if (userInDb == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Only update password if provided
        if (user.getPw() != null && !user.getPw().isEmpty()) {
            userInDb.setPw(user.getPw());
            userService.saveNewUser(userInDb);  // encode + save
        } else {
            userService.saveEntry(userInDb);    // save without re-encoding
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAll() {
        userService.deleteall();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
