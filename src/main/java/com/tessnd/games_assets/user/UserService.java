package com.tessnd.games_assets.user;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.tessnd.games_assets.user.exceptions.EmailAlreadyTakenException;
import com.tessnd.games_assets.user.exceptions.UsernameAlreadyTakenException;

import jakarta.annotation.PostConstruct;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired RoleRepository roleRepository;
    
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public User registerUser(UserCreateDTO user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new UsernameAlreadyTakenException("Логин уже занят!");
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new EmailAlreadyTakenException("Электронная почта уже занята!");
        }
        

        User userToSave = new User();
        userToSave.setUsername(user.getUsername());
        userToSave.setEmail(user.getEmail());
        userToSave.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        Set<Role> roles = new HashSet<>();

        Role userRole = roleRepository.findByName("USER").get();
        roles.add(userRole);

        userToSave.setRoles(roles);

        return userRepository.save(userToSave);
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).get();
    }

    @PostConstruct
    public void init() {
        createRoleIfNotFound("USER");
    }

    private void createRoleIfNotFound(String name) {
        Optional<Role> role = roleRepository.findByName(name);
        if (!role.isPresent()) {
            Role newRole = new Role();
            newRole.setName(name);
            roleRepository.save(newRole);
        }
    }
}
