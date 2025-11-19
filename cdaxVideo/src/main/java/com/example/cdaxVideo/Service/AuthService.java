package com.example.cdaxVideo.Service;


import com.example.cdaxVideo.Entity.User;
import com.example.cdaxVideo.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    public String registerUser(User user) {

        if (userRepository.existsByEmail(user.getEmail())) {
            return "Email already exists";
        }

        if (userRepository.existsByMobile(user.getMobile())) {
            return "Mobile number already registered";
        }

        if (!user.getPassword().equals(user.getCpassword())) {
            return "Passwords do not match";
        }

        userRepository.save(user);
        return "Registration successful"; // FIXED
    }


    public String loginUser(User user) {
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());

        if (existingUser.isPresent()) {
            if (existingUser.get().getPassword().equals(user.getPassword())) {
                return "Login successful";
            } else {
                return "Incorrect password";
            }
        }
        return "Email not found";
    }

    public String getFirstNameByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(User::getFirstName)
                .orElse(null);
    }

    /**
     * Get full user details using email.
     */
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    /**
     * Toggle the subscribed status of a user.
     */
    public boolean toggleSubscription(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setSubscribed(!user.isSubscribed());
            userRepository.save(user);
            return true;
        }
        return false;
    }
}
