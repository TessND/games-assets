package com.tessnd.games_assets.user;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.tessnd.games_assets.user.exceptions.EmailAlreadyTakenException;
import com.tessnd.games_assets.user.exceptions.UsernameAlreadyTakenException;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired RoleRepository roleRepository;
    
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public User registerUser(UserCreateDTO user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new UsernameAlreadyTakenException("Username is already taken!");
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new EmailAlreadyTakenException("Email is already taken!");
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
}
