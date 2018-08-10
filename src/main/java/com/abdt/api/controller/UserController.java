package com.abdt.api.controller;

import com.abdt.api.model.EmailValidator;
import com.abdt.api.model.User;
import com.abdt.api.model.UserPrincipal;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    private static Logger logger = LogManager.getLogger(UserController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;


    // registration (anyone can access)
    @PostMapping("/user")
    public ResponseEntity<User> createUser(@RequestBody User newUser) {

        if (newUser.getUsername() == null || newUser.getPassword() == null) {
            logger.error("Minimal required info (email and password) is not provided!");
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }

        String username = newUser.getUsername();

        // validate email
        EmailValidator emailValidator = new EmailValidator();
        if (!emailValidator.validateEmail(username)) {
            logger.error("Typed email is not valid!");
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }

        if (userService.isUserExist(username)) {
            logger.warn("A user with name " + username + " already exists");
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        final User created = new User(username, passwordEncoder.encode(newUser.getPassword()), "USER", newUser.getFirst_name(), newUser.getLast_name());
        userService.saveUser(created);
        logger.info("A new user just registered");

        return new ResponseEntity<>(created, HttpStatus.OK);
    }

    // view all users (only for admins)
    @GetMapping("/user")
    public ResponseEntity<List<User>> getAllUsers(Authentication auth) {
        final User user = ((UserPrincipal) auth.getPrincipal()).getUser();
        if (user.getRole().equals("ADMIN")) {
            List<User> result = userService.findAllUsers();
            logger.info("Admin " + user.getUsername() + " requested for all users");
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        logger.warn("Only admins can view list of all users!");
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    // get info about your profile
    @GetMapping("/user/{id}")
    public ResponseEntity<User> getUserById(@PathVariable(value = "id") Long userId, Authentication auth) {
        final User user = ((UserPrincipal) auth.getPrincipal()).getUser();
        if (user.getId() == userId) {
            logger.info("User " + user.getUsername() + " opened his profile");
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else if ("ADMIN".equals(user.getRole())) {
            User found = userService.findById(userId);
            if (found == null) {
                logger.error("User with id " + userId + " not found");
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                logger.info("Admin " + user.getUsername() + " opened profile of user with id " + userId);
                return new ResponseEntity<>(found, HttpStatus.OK);
            }
        } else {
            logger.warn("Unauthorized access to profile of user with id " + userId + " is detected");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    // alter your profile info
    @PutMapping("/user/{id}")
    public ResponseEntity<User> updateUser(@PathVariable(value = "id") Long userId,
                                           @RequestBody User newUser,
                                           Authentication auth) {

        final User user = ((UserPrincipal) auth.getPrincipal()).getUser();
        if (user.getId() == userId) {

            // validate email
            EmailValidator emailValidator = new EmailValidator();
            if (newUser.getUsername() != null) {
                if (emailValidator.validateEmail(newUser.getUsername())) {
                    user.setUsername(newUser.getUsername());
                } else {
                    logger.error("Typed email is not valid!");
                    return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
                }
            }
            if (newUser.getPassword() != null) user.setPassword(passwordEncoder.encode(newUser.getPassword()));
            if (newUser.getFirst_name() != null) user.setFirst_name(newUser.getFirst_name());
            if (newUser.getLast_name() != null) user.setLast_name(newUser.getLast_name());

            userService.updateUser(user);
            logger.info("User updated own profile info");
            return new ResponseEntity<>(user, HttpStatus.OK);

        } else if ("ADMIN".equals(user.getRole())) {
            User found = userService.findById(userId);
            if (found == null) {
                logger.error("Admin " + user.getUsername() + " unable to delete a user with id " + userId + " because not found");
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                // validate email
                EmailValidator emailValidator = new EmailValidator();
                if (newUser.getUsername() != null) {
                    if (emailValidator.validateEmail(newUser.getUsername())) {
                        found.setUsername(newUser.getUsername());
                    } else {
                        logger.error("Typed email is not valid!");
                        return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
                    }
                }
                if (newUser.getPassword() != null) found.setPassword(passwordEncoder.encode(newUser.getPassword()));
                if (newUser.getFirst_name() != null) found.setFirst_name(newUser.getFirst_name());
                if (newUser.getLast_name() != null) found.setLast_name(newUser.getLast_name());

                userService.updateUser(found);
                logger.info("Admin " + user.getUsername() + " updated profile of user with id " + userId);
                return new ResponseEntity<>(found, HttpStatus.OK);
            }
        } else {
            logger.warn("Unauthorized attempt to update profile of user with id " + userId + " is detected");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    // delete own profile
    @DeleteMapping("/user/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable(value = "id") Long userId, Authentication auth) {
        final User user = ((UserPrincipal) auth.getPrincipal()).getUser();
        if (user.getId() == userId) {
            userService.deleteUserById(userId);
            logger.info("User " + user.getUsername() + " deleted own profile successfully");
            return new ResponseEntity<>(HttpStatus.OK);
        } else if ("ADMIN".equals(user.getRole())) {
            User toDelete = userService.findById(userId);
            if (toDelete == null) {
                logger.error("Admin " + user.getUsername() + " unable to delete a user with id " + userId + " because not found");
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                userService.deleteUserById(userId);
                logger.info("Admin " + user.getUsername() + " deleted profile of user with id " + userId + " successfully");
                return new ResponseEntity<>(HttpStatus.OK);
            }
        } else {
            logger.warn("Unauthorized attempt to delete a profile of user with id " + userId + " is detected");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}
