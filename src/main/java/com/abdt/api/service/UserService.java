package com.abdt.api.service;

import com.abdt.api.domain.User;
import com.abdt.api.domain.UserPrincipal;
import com.abdt.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    public UserService() {
        super();
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public boolean isUserExist(String username) {
        return userRepository.findByUsername(username) instanceof User;
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public User findById(Long id) {
        Optional<User> result = userRepository.findById(id);
        return result.isPresent() ? result.get() : null;
    }

    public void updateUser(User user) {
        userRepository.save(user);
    }

    public void deleteUserById(long id) {
        userRepository.delete(userRepository.findById(id).get());
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new UserPrincipal(user);
    }
}
